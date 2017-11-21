package com.pay.muser.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import util.MailUtil;
import util.SHA1;
import util.SMSUtil;
import util.Tools;

import com.PayConstant;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.muser.dao.PayMerchantUser;
import com.pay.muser.dao.PayMerchantUserDAO;

/**
 * Object PAY_MERCHANT_USER service. 
 * @author Administrator
 *
 */
public class PayMerchantUserService {
	private static final Log log = LogFactory.getLog(PayMerchantUserService.class);
    /**
     * Get records list(json).
     * @return
     */
    public String getPayMerchantUserList(PayMerchantUser payMerchantUser,int page,int rows,String sort,String order){
        try {
            PayMerchantUserDAO payMerchantUserDAO = new PayMerchantUserDAO();
            List list = payMerchantUserDAO.getPayMerchantUserList(payMerchantUser, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payMerchantUserDAO.getPayMerchantUserCount(payMerchantUser)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayMerchantUser)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayMerchantUser
     * @param payMerchantUser
     * @throws Exception
     */
    public void addPayMerchantUser(PayMerchantUser payMerchantUser) throws Exception {
        new PayMerchantUserDAO().addPayMerchantUser(payMerchantUser);
    }
    /**
     * remove PayMerchantUser
     * @param userId
     * @throws Exception
     */
    public void removePayMerchantUser(String userId) throws Exception {
        new PayMerchantUserDAO().removePayMerchantUser(userId);
    }
    /**
     * update PayMerchantUser
     * @param payMerchantUser
     * @throws Exception
     */
    public void updatePayMerchantUser(PayMerchantUser payMerchantUser) throws Exception {
        new PayMerchantUserDAO().updatePayMerchantUser(payMerchantUser);
    }
    /**
     * detail PayMerchantUser
     * @param userId
     * @throws Exception
     */
    public PayMerchantUser detailPayMerchantUser(String userId) throws Exception {
        return new PayMerchantUserDAO().detailPayMerchantUser(userId);
    }
    
    /**
     * 组织商户操作员帐号的信息组织以及添加
     * @param payMerchant
     * @param merchantUser
     * @throws Exception
     */
	public void addPayMerchantUser(PayMerchant payMerchant,PayMerchantUser merchantUser) throws Exception {
		String pwd = Tools.createRandPwd();
		merchantUser.userId = payMerchant.merchantOperatorId;
    	merchantUser.custId = payMerchant.custId;
    	merchantUser.userNam = "商户管理员";
    	merchantUser.userPwd = SHA1.SHA1String(pwd);
    	merchantUser.creaSign = "0";
    	merchantUser.random = String.valueOf(System.currentTimeMillis());
    	merchantUser.state="0";//1
    	merchantUser.tel = payMerchant.attentionLineTel;
    	merchantUser.email = payMerchant.attentionLineEmail;
    	merchantUser.mailflg = "0";
    	new PayMerchantUserDAO().addPayMerchantUser(merchantUser);
    	//发送短信
    	log.info("商户开户短信："+merchantUser.tel+","+"商户【"+payMerchant.storeName+"】操作员开户成功，登录详情已发送到邮箱【"+merchantUser.email+"】。");
    	new SMSUtil().send(merchantUser.tel, "商户【"+payMerchant.storeName+"】操作员开户成功，登录详情已发送到邮箱【"+merchantUser.email+"】。");
    	//发送邮件
    	String msg = "<html><body><p>亲爱的“"+payMerchant.storeName+"”操作员您好：<br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;下面是贵公司商户操作员登录信息！<br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;登录网址："+PayConstant.PAY_CONFIG.get("merchant_face_url")+"<br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;登录名称："+merchantUser.userId+"<br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;登录密码："+pwd+"<br/>" +
				"商祺！</p></body></html>";
    	log.info("商户开户邮件："+merchantUser.email+","+msg);
    	new MailUtil().sendMail(
				PayConstant.PAY_CONFIG.get("service_mail_host"),
				PayConstant.PAY_CONFIG.get("service_mail_email"),
				PayConstant.PAY_CONFIG.get("service_mail_password"),
				PayConstant.PAY_CONFIG.get("service_mail_email"),
				merchantUser.email, "商户“"+payMerchant.storeName+"”操作员登录信息【钱通支付】",msg);
	}
	/**
	 * 通过表的列检查其值是否存在
	 * @param custId
	 * @return
	 */
	public boolean isExistRecordByColumn(String col,String value){
		return new PayMerchantUserDAO().isExistRecordByColumn(col,value);
	}
	/**
	 * 商户操作员的状态更改
	 * @param custId
	 * @param columName
	 * @param opration
	 * @throws Exception 
	 */
	public void updatePayMerchantUserStatus(String custId,String columName, String opration) throws Exception {
		new PayMerchantUserDAO().updatePayMerchantUserStatus(custId,columName,opration);
	}
	
	/**
	 * 商户操作员重置密码
	 * @param payMerchantUser
	 * @throws Exception 
	 */
	public void updatePayMerchantUserPwd(PayMerchantUser payMerchantUser) throws Exception {
		PayMerchant payMerchant = new PayMerchantDAO().selectByCustId(payMerchantUser.custId);
		String pwd = Tools.createRandPwd();
		payMerchantUser.userPwd = SHA1.SHA1String(pwd);
		payMerchantUser.lastUppwdDate = new Date();
		new PayMerchantUserDAO().updatePayMerchantUserForResetPwd(payMerchantUser);
		//发送短信
//    	new SmsLogService().send(payMerchant.bizTelNo, "商户“"+payMerchant.storeName+"”操作员重置密码成功，密码为："+pwd);
		//发送邮件
    	new MailUtil().sendMail(
				PayConstant.PAY_CONFIG.get("service_mail_host"),
				PayConstant.PAY_CONFIG.get("service_mail_email"),
				PayConstant.PAY_CONFIG.get("service_mail_password"),
				PayConstant.PAY_CONFIG.get("service_mail_email"),
				payMerchant.attentionLineEmail, "商户“"+payMerchant.storeName+"”操作员重置密码信息【钱通支付】","<html><body><p>亲爱的“"+payMerchant.storeName+"”操作员您好：<br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;下面是贵公司商户操作员重置的密码和登录信息！<br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;登录网址："+PayConstant.PAY_CONFIG.get("merchant_face_url")+"<br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;登录名称："+payMerchantUser.userId+"<br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;登录密码："+pwd+"<br/>" +
				"商祺！</p></body></html>");
	}
	
	/**
	 * 根据商户id查询商户操作员信息
	 * @param custId 查询条件
	 * @return 返回查询结果
	 * @throws Exception 
	 */
	public PayMerchantUser selectByCustId(String custId) throws Exception {
		return new PayMerchantUserDAO().selectByCustId(custId);
	}
}