package com.pay.contract.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.MailUtil;
import util.PayUtil;
import util.SMSUtil;

import com.PayConstant;
import com.common.sms.service.SmsLogService;
import com.jweb.dao.Blog;
import com.pay.contract.dao.PayContract;

public class RemindContractExpiresListener extends TimerTask {
	private static final Log log = LogFactory.getLog(RemindContractExpiresListener.class);
	SmsLogService smsService = new SmsLogService();
	public static void main(String [] args){
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.add(Calendar.DATE, 30);
		System.out.println(gc.getTime().toLocaleString());
	}
	public void run(){
		remind();
	}
	public void test(){
		remind();
	}
	private void remind(){
		try{
			PayContractService service = new PayContractService();
			//取得合同期限设置信息
			Map map = service.getRemindPayContractExpires();
			int days = 0;
			try {days = Integer.parseInt((String)map.get("expires"));} catch (Exception e) {return;}
			//计算days天之后的日期
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.add(Calendar.DATE, days);
			//取得快到期的商户(每三天一次，一共3次)
			String firsttDate = new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime());
			gc.add(Calendar.DATE,-3);
			String secondDate = new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime());
			gc.add(Calendar.DATE,-3);
			String thirdDate = new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime());
			List mList = service.getExpiresMerchant(firsttDate,secondDate,thirdDate);
			String msg="";
			for(int i = 0; i<mList.size(); i++){
				PayContract contract = (PayContract)mList.get(i);
				msg=msg+contract.pactName+"（"+contract.seqNo+"，"+new SimpleDateFormat("yyyy-MM-dd").format(contract.pactLoseEffDate)+"），";
			}
			if(msg.length()==0)return;
			msg="合同到期提醒："+msg.substring(0,msg.length()-1);
//			sendMsg((String)map.get("mobile"),msg);
			sendEmail((String)map.get("email"),msg);
		} catch (Exception e){
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
	private void sendMsg(String mobiles,String msg) throws Exception{
		if(mobiles == null||msg==null)return;
		new Blog(this.getClass().getSimpleName(),"发送短信"+msg,"system","","");
		String [] ms = mobiles.split(",");
		for(int i = 0; i<ms.length; i++){
			//检查手机合法性
			try{if(String.valueOf(Long.parseLong(ms[i])).length() !=11)continue;}catch (Exception e){continue;}
			new SMSUtil().send(ms[i], msg);
		}
	}
	private void sendEmail(String email,String msg){
		if(email == null||msg==null)return;
		new Blog(this.getClass().getSimpleName(),"发送邮件"+msg,"system","","");
		MailUtil mailUtil = new MailUtil();
		String [] ms = email.split(",");
		for(int i = 0; i<ms.length; i++){
			try{
				mailUtil.sendMail(
					PayConstant.PAY_CONFIG.get("service_mail_host"),
					PayConstant.PAY_CONFIG.get("service_mail_email"),
					PayConstant.PAY_CONFIG.get("service_mail_password"),
					PayConstant.PAY_CONFIG.get("service_mail_email"),
					ms[i], "合同到期提醒","<html><body>"+msg+"</body></html>");
			}catch (Exception e){e.printStackTrace();continue;}
		}
	}
}
