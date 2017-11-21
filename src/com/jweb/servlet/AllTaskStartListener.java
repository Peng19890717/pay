package com.jweb.servlet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.InitDataFromDB;
import util.JWebConstant;
import util.PayUtil;
import util.PropertyReader;
import util.SHA1;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.jweb.service.ClearJwebUserMap;
import com.pay.business.service.PayBusinessParameterService;
import com.pay.contract.service.RemindContractExpiresListener;
import com.pay.risk.service.PayRiskRuleListener;
import com.pay.settlement.service.PayAutoSettlementListener;
import com.pay.settlement.service.PayDbBackupListener;


public class AllTaskStartListener implements ServletContextListener {
	private static final Log log = LogFactory.getLog(AllTaskStartListener.class);
	private Timer timer = new Timer();
	public void contextInitialized(ServletContextEvent event) {
		//系统启动密码验证，包括各种属性文件、数据库xml配置文件
		if(!login(event)){
			log.info("系统启动失败："+event.getServletContext().getContextPath());
		}
		JWebConstant.APP_PATH = event.getServletContext().getRealPath("");//平台配置
		try {
			//载入系统默认参数（数据库数据）
			timer.schedule(new InitDataFromDB(),3000);
			//创建用户缓存列表
			Map userMap =  new Hashtable();
			event.getServletContext().setAttribute("USER_MAP",userMap);
			//启动定时清理用户缓存列表 每小时一次 主要清理登录次数超限用户
			timer.schedule(new ClearJwebUserMap(userMap),60*60*1000,60*60*1000);
//			timer.schedule(new ClearJwebUserMap(userMap),0);
			//5秒后启动系统定时任务
			timer.schedule(new BatchTaskService(timer),5000);
			//启动雅酷，中信更换任务
//			YKPayService.changeChannel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private boolean login(ServletContextEvent event){
		//读取加密配置文件
		String SYS_ENCRYPT_FLAG="";
		String SYS_PASSWORD_CIPHER="";
        try {
        	String fn = "resources/encrypt_parameter.properties";
        	ResourceBundle bundle = ResourceBundle.getBundle(fn.substring(0, fn.lastIndexOf(".properties")));
            SYS_ENCRYPT_FLAG = bundle.getString("SYS_ENCRYPT_FLAG");
            if("0".equals(SYS_ENCRYPT_FLAG))return true;
            else if("1".equals(SYS_ENCRYPT_FLAG)){
            	SYS_PASSWORD_CIPHER = bundle.getString("SYS_PASSWORD");
            	PropertyReader.SYS_ENCRYPT_FLAG="1";
            }
            else throw new Exception("配置SYS_ENCRYPT_FLAG（0/1）读取错误");
        }
        catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
        //读取、验证密码
        String pwd = "";
        while(true){
        	System.out.print(event.getServletContext().getContextPath()+"启动密码：");
        	pwd = new String(System.console().readPassword());
        	if(SHA1.SHA1String(pwd).equals(SYS_PASSWORD_CIPHER))break;
        }
        //设置密码到缓存
        PropertyReader.CONFIG_FILE_PASSWORD = pwd;
        //载入所有读取配置文件的类f(触发一下载入资源文件的方法即可)
        PayConstant.init();
        //30秒钟后，所有属性文件加载后，清除缓存密码
        Thread t = new Thread(){
        	public void run(){
        		try {
					Thread.sleep(30*1000);
					PropertyReader.CONFIG_FILE_PASSWORD="";
				} catch (InterruptedException e) {e.printStackTrace();}
        	}
        };
        t.start();
        return true;
	}
	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		//保存缓存信息
		log.info("保存缓存日志开始...........");
		Blog.addBatchLog();
		log.info("保存缓存日志结束.");
	}
}
class BatchTaskService  extends TimerTask {
	private static final Log log = LogFactory.getLog(BatchTaskService.class);
	private Timer timer;
	public BatchTaskService(Timer timer){
		this.timer = timer;
	}
	@Override
	public void run() {
		try {
			//载入系统业务参数
			PayBusinessParameterService.executePayBusinessParameter("0");
			//系统结算跑批时间判断，若不存在默认1点
			if(PayConstant.PAY_CONFIG.get("settlement_time")==null||
					PayConstant.PAY_CONFIG.get("settlement_time").trim().length()==0){
				PayConstant.PAY_CONFIG.put("settlement_time", "01:00:00");
			}
			//系统数据备份时间判断，若不存在默认4点
			if(PayConstant.PAY_CONFIG.get("db_bak_time")==null||
					PayConstant.PAY_CONFIG.get("db_bak_time").trim().length()==0){
				PayConstant.PAY_CONFIG.put("db_bak_time", "04:00:00");
			}
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(new Date());
			gc.add(Calendar.DATE, 1);
			Date stlT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").
					format(gc.getTime()) + " " + PayConstant.PAY_CONFIG.get("settlement_time"));
			Date dbBakT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").
					format(gc.getTime()) + " " + PayConstant.PAY_CONFIG.get("db_bak_time"));
			if("0".equals(PayConstant.PAY_CONFIG.get("server_type"))){
				log.info("服务端跑批开始=============================");
				//结算跑批，开启结算跑批
				if("1".equals(PayConstant.PAY_CONFIG.get("settlement_run_flag"))){
					timer.schedule(new PayAutoSettlementListener(),stlT, 24*60*60*1000);
				}
				//合同提醒
				timer.schedule(new RemindContractExpiresListener(),stlT, 24*60*60*1000);
				//风控跑批
				timer.schedule(new PayRiskRuleListener(stlT),stlT, 24*60*60*1000);
				//数据库备份跑批
				if("1".equals(PayConstant.PAY_CONFIG.get("db_bak_run_flag"))){
					timer.schedule(new PayDbBackupListener(),dbBakT, 24*60*60*1000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			PayUtil.exceptionToString(e);
		}
	}
}
