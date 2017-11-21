package com.pay.user.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import jxl.CellType;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.json.JSONArray;
import org.json.JSONObject;

import util.JWebConstant;
import util.SHA1;
import util.Tools;

import com.PayConstant;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.risk.dao.PayRiskXListDAO;
import com.pay.user.dao.PayTranUserInfo;
import com.pay.user.dao.PayTranUserInfoDAO;

/**
 * Object PAY_TRAN_USER_INFO service. 
 * @author Administrator
 *
 */
public class PayTranUserInfoService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayTranUserInfoList(PayTranUserInfo payTranUserInfo,int page,int rows,String sort,String order){
        try {
            PayTranUserInfoDAO payTranUserInfoDAO = new PayTranUserInfoDAO();
            List list = payTranUserInfoDAO.getPayTranUserInfoList(payTranUserInfo, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payTranUserInfoDAO.getPayTranUserInfoCount(payTranUserInfo)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayTranUserInfo)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
//    /**
//     * add PayTranUserInfo
//     * @param payTranUserInfo
//     * @throws Exception
//     */
//    public void addPayTranUserInfo(PayTranUserInfo payTranUserInfo) throws Exception {
//        new PayTranUserInfoDAO().addPayTranUserInfo(payTranUserInfo);
//    }
    /**
     * remove PayTranUserInfo
     * @param id
     * @throws Exception
     */
    public void removePayTranUserInfo(String id) throws Exception {
        new PayTranUserInfoDAO().removePayTranUserInfo(id);
    }
    /**
     * update PayTranUserInfo
     * @param payTranUserInfo
     * @throws Exception
     */
    public void updatePayTranUserInfo(PayTranUserInfo payTranUserInfo) throws Exception {
        new PayTranUserInfoDAO().updatePayTranUserInfo(payTranUserInfo);
    }
    /**
     * detail PayTranUserInfo
     * @param id
     * @throws Exception
     */
    public PayTranUserInfo detailPayTranUserInfo(String id) throws Exception {
        return new PayTranUserInfoDAO().detailPayTranUserInfo(id);
    }
    
    /**
     * detail PayTranUserInfo
     * @param USER_ID
     * @throws Exception
     */
    public PayTranUserInfo detailPayTranUserInfoByUserId(String id) throws Exception {
        return new PayTranUserInfoDAO().detailPayTranUserInfoByCustId(id);
    }
    
    
    /**
	 * 导出列表
	 * @param 
	 * @return
	 */
	public byte[] exportExcelList(PayTranUserInfo payTranUserInfo) {
		
		PayTranUserInfoDAO payTranUserInfoDAO = new PayTranUserInfoDAO();
    	String randomName = Tools.getUniqueIdentify();
    	//临时目录
    	File tmpFile = new File(JWebConstant.APP_PATH+"/dat/download/"+randomName);
    	tmpFile.mkdir();
    	//文件模板名称
    	String templetName ="payTranUserInfo";
    	//模板文件
    	File templetFile = new File(JWebConstant.APP_PATH+"/templet/"+templetName+".xls");
    	//每个Excel最多条数
    	int excelRecordCount = 30000;
    	int fileNum = 0;
        try {
        	List list = new ArrayList();
        	int step = 0;
        	//取得业务数据
        	list = payTranUserInfoDAO.getPayTranUserInfoList_excel(payTranUserInfo, step, step + excelRecordCount, null, null);
        	while(list.size()==excelRecordCount){
        		//加入Excel
        		writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        		step = step + excelRecordCount;
        		//取得业务数据
        		list = payTranUserInfoDAO.getPayTranUserInfoList_excel(payTranUserInfo, step, step + excelRecordCount, null, null);
        	}
        	//加入结尾的记录到Excel
        	if(list.size()<excelRecordCount){
        		writeListToExcel(list,templetFile,new File(tmpFile.getAbsolutePath()+"/"+(fileNum++)+".xls"));
        	}
        	File zipfile=new File(JWebConstant.APP_PATH+"/dat/download/"+
        		new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"_"+randomName+".zip");
        	File srcfile[] = new File[fileNum];
        	for(int i=0; i<srcfile.length; i++)srcfile[i] = new File(tmpFile.getAbsolutePath()+"/"+(i)+".xls");
        	Tools.zipFiles(srcfile, zipfile);
			FileInputStream fis = new FileInputStream(zipfile);
			byte [] b = new byte[fis.available()]; 
			fis.read(b);
			fis.close();
			Tools.deleteFile(tmpFile);
			Tools.deleteFile(zipfile);
			return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	private void writeListToExcel(List list,File templetFile,File tmpFile){
    	Workbook rw = null;
		WritableWorkbook wwb = null; 
		WritableSheet ws = null;
		try {
			if (!Tools.copy(templetFile.getAbsolutePath(), tmpFile.getAbsolutePath())) return;
			rw = jxl.Workbook.getWorkbook(tmpFile);
			wwb = Workbook.createWorkbook(tmpFile, rw);
			ws = wwb.getSheet(0);
			writeData(list, ws);
			wwb.write();
			wwb.close();
			rw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	//循环输入数据
	public void writeData(List list,WritableSheet ws){
  		
  		for (int i=0; i < list.size(); i++) {
  			PayTranUserInfo payTranUserInfo  = (PayTranUserInfo)list.get(i);
  			
  			if(payTranUserInfo.userId!=null && !"".equals(payTranUserInfo.userId)) setCellValue(ws, 0, i + 1,payTranUserInfo.userId);
  			if(payTranUserInfo.realName!=null && !"".equals(payTranUserInfo.realName)) setCellValue(ws, 1, i + 1,payTranUserInfo.realName);
  			if(payTranUserInfo.userType!=null && !"".equals(payTranUserInfo.userType)){
  				if(payTranUserInfo.userType.equalsIgnoreCase("0"))
  					setCellValue(ws, 2, i + 1,"买家");
  				if(payTranUserInfo.userType.equalsIgnoreCase("1"))
  					setCellValue(ws, 2, i + 1,"卖家");
  			}
  			if(payTranUserInfo.province!=null && !"".equals(payTranUserInfo.province)) setCellValue(ws, 3, i + 1,payTranUserInfo.province);
  			if(payTranUserInfo.city!=null && !"".equals(payTranUserInfo.city)) setCellValue(ws, 4, i + 1,payTranUserInfo.province);
  			if(payTranUserInfo.mobile!=null && !"".equals(payTranUserInfo.mobile)) setCellValue(ws, 5, i + 1,payTranUserInfo.mobile);
  			if(payTranUserInfo.registerTime!=null) setCellValue(ws, 6, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.registerTime));
  			if(payTranUserInfo.registerType!=null && !"".equals(payTranUserInfo.registerType)){
  				if(payTranUserInfo.registerType.equalsIgnoreCase("0"))
  					setCellValue(ws, 7, i + 1,"无");
  				if(payTranUserInfo.registerType.equalsIgnoreCase("1"))
  					setCellValue(ws, 7, i + 1,"手机号");
  				if(payTranUserInfo.registerType.equalsIgnoreCase("2"))
  					setCellValue(ws, 7, i + 1,"邮箱");
  			}
  			if(payTranUserInfo.cretType!=null && !"".equals(payTranUserInfo.cretType)){
  			// 证件类型 01身份证,02军官证,03护照,04回乡证,05台胞证,06警官证,07士兵证,99其他 默认01
  				if(payTranUserInfo.cretType.equalsIgnoreCase("01"))
  					setCellValue(ws, 8, i + 1,"身份证");
				if(payTranUserInfo.cretType.equalsIgnoreCase("02"))
					setCellValue(ws, 8, i + 1,"军官证");				
				if(payTranUserInfo.cretType.equalsIgnoreCase("03"))
					setCellValue(ws, 8, i + 1,"护照");	
				if(payTranUserInfo.cretType.equalsIgnoreCase("04"))
					setCellValue(ws, 8, i + 1,"回乡证");
				if(payTranUserInfo.cretType.equalsIgnoreCase("05"))
					setCellValue(ws, 8, i + 1,"台胞证");
				if(payTranUserInfo.cretType.equalsIgnoreCase("06"))
					setCellValue(ws, 8, i + 1,"警官证");
				if(payTranUserInfo.cretType.equalsIgnoreCase("07"))
					setCellValue(ws, 8, i + 1,"士兵证");	
				if(payTranUserInfo.cretType.equalsIgnoreCase("99"))
					setCellValue(ws, 8, i + 1,"其他");
  			}
  			if(payTranUserInfo.cretNo!=null && !"".equals(payTranUserInfo.cretNo))
  				setCellValue(ws, 9, i + 1,payTranUserInfo.cretNo);
  			if(payTranUserInfo.certSubmitTime!=null)
  				setCellValue(ws, 10, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.certSubmitTime));
  			//认证状态 0：未认证 1：审核中 2：已通过  3：未通过 默认0
  			if(payTranUserInfo.userStatus!=null && !"".equals(payTranUserInfo.userStatus)){
  				if(payTranUserInfo.userStatus.equalsIgnoreCase("0"))
  					setCellValue(ws, 11, i + 1,"未认证");
  				if(payTranUserInfo.userStatus.equalsIgnoreCase("1"))
  					setCellValue(ws, 11, i + 1,"审核中");
  				if(payTranUserInfo.userStatus.equalsIgnoreCase("2"))
  					setCellValue(ws, 11, i + 1,"已通过");
  				if(payTranUserInfo.userStatus.equalsIgnoreCase("3"))
  					setCellValue(ws, 11, i + 1,"未通过");
  			}
  			if(payTranUserInfo.checkTime!=null)
  				setCellValue(ws, 12, i + 1,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.checkTime));
  			if(payTranUserInfo.trialStatus!=null && !"".equals(payTranUserInfo.trialStatus)){
  				if(payTranUserInfo.trialStatus.equalsIgnoreCase("0"))
  					setCellValue(ws, 13, i + 1,"试用");
  				if(payTranUserInfo.trialStatus.equalsIgnoreCase("1"))
  					setCellValue(ws, 13, i + 1,"正常");
  			}
  			//账户状态 0-正常；1-未激活；2-冻结；9-已销户
  			if(payTranUserInfo.acstatus!=null && !"".equals(payTranUserInfo.acstatus)){
  				if(payTranUserInfo.acstatus.equalsIgnoreCase("0"))
  					setCellValue(ws, 16, i + 1,"正常");
				if(payTranUserInfo.acstatus.equalsIgnoreCase("1"))
					setCellValue(ws, 16, i + 1,"未激活");			
				if(payTranUserInfo.acstatus.equalsIgnoreCase("2"))
					setCellValue(ws, 16, i + 1,"冻结");	
				if(payTranUserInfo.acstatus.equalsIgnoreCase("9"))
					setCellValue(ws, 16, i + 1,"已销户");
  			}
  			//现金余额。
  			if(payTranUserInfo.cashacbal!=null){
  				setCellValue(ws, 17, i + 1,String.format("%.2f", ((double)payTranUserInfo.cashacbal)/100d));
  			}
  			//账户余额。
  			if(payTranUserInfo.consacbal!=null){
  				setCellValue(ws, 18, i + 1,String.format("%.2f", ((double)payTranUserInfo.consacbal)/100d));
  			}
  			//冻结余额。
  			if(payTranUserInfo.frozbalance!=null){
  				setCellValue(ws, 19, i + 1,String.format("%.2f", ((double)payTranUserInfo.frozbalance)/100d));
  			}
  			//操作人。
  			if(payTranUserInfo.checkUserId!=null && !"".equals(payTranUserInfo.checkUserId)){
  				setCellValue(ws, 20, i + 1,payTranUserInfo.checkUserId);
  			}
  			
  		}
  	}
  	private void setCellValue(WritableSheet writeSheet, int x, int y,
			String value) {
		try {
			WritableCell cell = writeSheet.getWritableCell(x, y);
			if (cell.getType() == CellType.EMPTY) {
				Label write = new Label(x, y, value);
				writeSheet.addCell(write);
			} else {
				Label write = (Label) cell;
				write.setString(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Boolean existUser(String limitUserCode) throws Exception {
		return new PayTranUserInfoDAO().existUser(limitUserCode);
	}
    public String checkPayPwd(PayTranUserInfo payer,String password){
    	Date curTime = new Date();
    	try {
    		//密码错误次数判断
    		if(JWebConstant.SYS_CONFIG.get("LOGIN_FAIL_MAX_COUNT") == null)JWebConstant.SYS_CONFIG.put("LOGIN_FAIL_MAX_COUNT","3");
    		if(payer.payPwdFailCount >= Long.parseLong((String)JWebConstant.SYS_CONFIG.get("LOGIN_FAIL_MAX_COUNT"))){
    			long loginTimeAgain = Long.parseLong((String)JWebConstant.SYS_CONFIG.get("LOGIN_FAIL_MAX_COUNT_FREE_TIME"))*60*1000
    				-(curTime.getTime() - payer.payPwdLastTime.getTime());
    			if(loginTimeAgain > 0){
    				return "支付密码输入超过"+Long.parseLong((String)JWebConstant.SYS_CONFIG.get("LOGIN_FAIL_MAX_COUNT"))+"次,请"
    						+(loginTimeAgain/(60*1000)+1)+"分钟后使用";
    			} else {
    				payer.payPwdFailCount=0l;
    				payer.payPwdLastTime = curTime;
        			new  PayTranUserInfoDAO().updatePayTranUserPwdCount(payer);
    			}
    		}
    		if(payer.payPassword != null && payer.payPassword.equals(SHA1.SHA1String(password))){
    			payer.payPwdFailCount=0l;
    			new  PayTranUserInfoDAO().updatePayTranUserPwdCount(payer);
    		} else {//密码错误
    			//更新密码输入次数信息
    			payer.payPwdFailCount++;
    			payer.payPwdLastTime = curTime;
    			new  PayTranUserInfoDAO().updatePayTranUserPwdCount(payer);
    			return "支付密码输入错误";
    		}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
    	return "";
    }
	/**
     * 登录验证
     * @param payMerchantUser
     * @return
     */
	public String checkLoginPwd(PayRequest payRequest,HttpServletRequest request)throws Exception {
		// 查询是否有此帐号
		PayTranUserInfo payTranUserInfo = new PayTranUserInfoDAO().selectSingleUser(request.getParameter("userId"));
		if(payTranUserInfo == null)return "登录名或密码错误";
		if(new PayRiskXListDAO().getClientFromXList(PayConstant.CUST_TYPE_USER, payTranUserInfo.userId))return "黑名单用户";
		PayTranUserInfoDAO payTranUserInfoDAO = new PayTranUserInfoDAO();
    	payTranUserInfo.loginPwdLastTime = payTranUserInfo.loginPwdLastTime == null?new Date():payTranUserInfo.loginPwdLastTime;
		long sysTime = Long.valueOf((JWebConstant.SYS_CONFIG.get("LOGIN_FAIL_MAX_COUNT_FREE_TIME").toString())); // 系统设置的过时时间
		//支付密码输入错误不到30分钟
		if(new Date().getTime() - payTranUserInfo.loginPwdLastTime.getTime() <= sysTime*60000){
			long maxTimes = Long.parseLong((String)JWebConstant.SYS_CONFIG.get("LOGIN_FAIL_MAX_COUNT"));
			if(payTranUserInfo.loginPwdFailCount>=maxTimes) return "登录密码已达到错误输入上限（"+maxTimes
					+"），请"+JWebConstant.SYS_CONFIG.get("LOGIN_FAIL_MAX_COUNT_FREE_TIME").toString()+"分钟后登录";
		} else payTranUserInfo.loginPwdFailCount = 0l;//登录错误时间超出30分钟后,支付次数清零。
		if(!SHA1.SHA1String(request.getParameter("loginPassword")).equals(payTranUserInfo.loginPassword) ){//输错密码
			//错误次数累加,累加后的错误次数和错误时间写入PAY_MERCHANT_USER表中。
			payTranUserInfo.loginPwdFailCount++;
			payTranUserInfo.loginPwdLastTime = new Date();
			payTranUserInfoDAO.updatePayTranUserInfo(payTranUserInfo);
			return "登录名或密码错误";
		} else {//支付密码正确，支付密码错误次数清零。
			payTranUserInfo.loginPwdFailCount = 0L;
			payTranUserInfoDAO.updatePayTranUserInfo(payTranUserInfo);//更新错误次数和错误时间。
			payRequest.payerId = payTranUserInfo.userId;
			payRequest.productOrder.custId = payTranUserInfo.userId;
			payRequest.payOrder.custId = payTranUserInfo.userId;
			payRequest.tranUserMap.put(payTranUserInfo.userId, payTranUserInfo);
			//更新订单cust_id
			new PayInterfaceDAO().updateOrderForCustId(payRequest);
			return "";
		}
	}
	/**
	 * 设置买/卖家状态
	 * @param userId
	 * @param userType
	 */
	public void setUserTypebyUserId(String userId, String userType) {
		new PayTranUserInfoDAO().setUserTypebyUserId(userId,userType);
	}
}