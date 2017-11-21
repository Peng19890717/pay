package com.pay.merchantinterface.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.PayConstant;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.merchant.dao.PayMerchant;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.user.dao.PayTranUserInfo;

public class PayRequest {
	public String merchantId = "";
	public String merchantOrderId = "";
	public String merchantName = "";
	public String merchantOrderAmt = "";
	public String merchantOrderDesc = "";
	public String merchantNotifyUrl = "";
	public String merchantFrontEndUrl = "";
	public String userMobileNo = "";
	public String userName = "";
	public String payerId = "";
	public String salerId = "";
	public String guaranteeAmt = "";
	public String userType = "0";
	public String accountType = "";
	public String bankId = "";
	public String credentialType = "";
	public String credentialNo = "";
	public String msgExt = "";
	public String orderTime = "";
	public String bizType = "";
	public String rptType = "";
	public String payMode = "0";
	public String merchantRefundAmt = "";
	public String application="";
	public String version="";
	public String respCode = "000";
	public String respDesc = "处理成功";
	public String reqXml = "";
	public String checkCode="";
	public String respXml = "";
	public String sign = "";
	public String accountDate = "";
	public String accountFilePath = "";
	public String responseStr = "";
	public String cardNo = "";
	public String cvv2 = "";
	public String validPeriod = "";
	public String bankName = "";
	public String guaranteeNoticeStatus="";
	public Date curTime = new Date();
	public Long tranAmt = 0l;
	public PayProductOrder productOrder = null;
	public PayOrder payOrder = null;
	public PayTranUserInfo certPayUser = null;
	public String bindId="";
	public String cardType="";
	public boolean isSaveUserSum = false;
	public PayMerchant merchant = null;
	//代收付增加部分========开始
	public String tranId = "";
	public String authTranId = "";
	public String signTranId = "";
	public String queryTranId = "";
	public String receivePayNotifyUrl = "";
	public String receivePayType = "";
	public String timestamp = "";
	public String accountProp = "";
	public String timeliness = "";
	public String subMerchantId = "";
	public String appointmentTime = "";
	public String bankGeneralName = "";
	public long totalCount = 0l;
	public long totalAmt = 0l;
	public String accType = "";
	public String accNo = "";
	public String accName = "";
	public String protocolNo = "";
	public String amount = "";
	public String tel = "";
	public String summary = "";
	public String receivePayRes="";//用户畅捷代收通知结果保存，包装快捷使用
	public PayReceiveAndPay receiveAndPaySingle=null;//单笔代收付封装对象
	public List<PayReceiveAndPay> receivePayList = new ArrayList<PayReceiveAndPay>();
	public PayCoopBank rpChannel;//代收付渠道
	//代收付增加部分========结束
	public Map<String, PayTranUserInfo> tranUserMap = new HashMap<String, PayTranUserInfo>();
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantOrderAmt() {
		return merchantOrderAmt;
	}
	public void setMerchantOrderAmt(String merchantOrderAmt) {
		this.merchantOrderAmt = merchantOrderAmt;
	}
	public String getMerchantOrderDesc() {
		return merchantOrderDesc;
	}
	public void setMerchantOrderDesc(String merchantOrderDesc) {
		this.merchantOrderDesc = merchantOrderDesc;
	}
	public String getMerchantNotifyUrl() {
		return merchantNotifyUrl;
	}
	public void setMerchantNotifyUrl(String merchantNotifyUrl) {
		this.merchantNotifyUrl = merchantNotifyUrl;
	}
	public String getMerchantFrontEndUrl() {
		return merchantFrontEndUrl;
	}
	public void setMerchantFrontEndUrl(String merchantFrontEndUrl) {
		this.merchantFrontEndUrl = merchantFrontEndUrl;
	}
	public String getUserMobileNo() {
		return userMobileNo;
	}
	public void setUserMobileNo(String userMobileNo) {
		this.userMobileNo = userMobileNo;
	}
	public String getCredentialType() {
		return credentialType;
	}
	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}
	public String getCredentialNo() {
		return credentialNo;
	}
	public void setCredentialNo(String credentialNo) {
		this.credentialNo = credentialNo;
	}
	public String getMsgExt() {
		return msgExt;
	}
	public void setMsgExt(String msgExt) {
		this.msgExt = msgExt;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getRptType() {
		return rptType;
	}
	public void setRptType(String rptType) {
		this.rptType = rptType;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getMerchantRefundAmt() {
		return merchantRefundAmt;
	}
	public void setMerchantRefundAmt(String merchantRefundAmt) {
		this.merchantRefundAmt = merchantRefundAmt;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	public String getReqXml() {
		return reqXml;
	}
	public void setReqXml(String reqXml) {
		this.reqXml = reqXml;
	}
	
	public String getRespXml() {
		return respXml;
	}
	public void setRespXml(String respXml) {
		this.respXml = respXml;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getAccountDate() {
		return accountDate;
	}
	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}
	public String getAccountFilePath() {
		return accountFilePath;
	}
	public void setAccountFilePath(String accountFilePath) {
		this.accountFilePath = accountFilePath;
	}
	public String getResponseStr() {
		return responseStr;
	}
	public void setResponseStr(String responseStr) {
		this.responseStr = responseStr;
	}
	public PayOrder getPayOrder() {
		return payOrder;
	}
	public void setPayOrder(PayOrder payOrder) {
		this.payOrder = payOrder;
	}
	public void setRespInfo(String respCode){
		this.respCode = respCode;
		respDesc = PayConstant.RESP_CODE_DESC.get(respCode);
		if(respDesc==null)respDesc="未知编码("+respCode+")";
	}
	public void copyTo(PayRequest toRequest){
		toRequest.merchantId=this.merchantId;
		toRequest.merchantOrderId=this.merchantOrderId;
		toRequest.merchantName=this.merchantName;
		toRequest.merchantOrderAmt=this.merchantOrderAmt;
		toRequest.merchantOrderDesc=this.merchantOrderDesc;
		toRequest.merchantNotifyUrl=this.merchantNotifyUrl;
		toRequest.merchantFrontEndUrl=this.merchantFrontEndUrl;
		toRequest.userMobileNo=this.userMobileNo;
		toRequest.userName=this.userName;
		toRequest.payerId=this.payerId;
		toRequest.salerId=this.salerId;
		toRequest.guaranteeAmt=this.guaranteeAmt;
		toRequest.userType=this.userType;
		toRequest.accountType=this.accountType;
		toRequest.bankId=this.bankId;
		toRequest.credentialType=this.credentialType;
		toRequest.credentialNo=this.credentialNo;
		toRequest.msgExt=this.msgExt;
		toRequest.orderTime=this.orderTime;
		toRequest.bizType=this.bizType;
		toRequest.rptType=this.rptType;
		toRequest.payMode=this.payMode;
		toRequest.merchantRefundAmt=this.merchantRefundAmt;
		toRequest.application=this.application;
		toRequest.version=this.version;
		toRequest.respCode=this.respCode;
		toRequest.respDesc=this.respDesc;
		toRequest.reqXml=this.reqXml;
		toRequest.checkCode=this.checkCode;
		toRequest.respXml=this.respXml;
		toRequest.sign=this.sign;
		toRequest.accountDate=this.accountDate;
		toRequest.accountFilePath=this.accountFilePath;
		toRequest.responseStr=this.responseStr;
		toRequest.cardNo=this.cardNo;
		toRequest.cvv2=this.cvv2;
		toRequest.validPeriod=this.validPeriod;
		toRequest.bankName=this.bankName;
		toRequest.guaranteeNoticeStatus=this.guaranteeNoticeStatus;
		toRequest.curTime=this.curTime;
		toRequest.tranAmt=this.tranAmt;
		toRequest.productOrder=this.productOrder;
		toRequest.payOrder=this.payOrder;
		toRequest.certPayUser=this.certPayUser;
		toRequest.bindId=this.bindId;
		toRequest.cardType=this.cardType;
		toRequest.isSaveUserSum=this.isSaveUserSum;
		toRequest.merchant=this.merchant;
		toRequest.tranId=this.tranId;
		toRequest.authTranId=this.authTranId;
		toRequest.signTranId=this.signTranId;
		toRequest.queryTranId=this.queryTranId;
		toRequest.receivePayNotifyUrl=this.receivePayNotifyUrl;
		toRequest.receivePayType=this.receivePayType;
		toRequest.timestamp=this.timestamp;
		toRequest.accountProp=this.accountProp;
		toRequest.timeliness=this.timeliness;
		toRequest.subMerchantId=this.subMerchantId;
		toRequest.appointmentTime=this.appointmentTime;
		toRequest.bankGeneralName=this.bankGeneralName;
		toRequest.totalCount=this.totalCount;
		toRequest.totalAmt=this.totalAmt;
		toRequest.accType=this.accType;
		toRequest.accNo=this.accNo;
		toRequest.accName=this.accName;
		toRequest.protocolNo=this.protocolNo;
		toRequest.amount=this.amount;
		toRequest.tel=this.tel;
		toRequest.summary=this.summary;
		toRequest.receivePayRes=this.receivePayRes;
		toRequest.receiveAndPaySingle=this.receiveAndPaySingle;
		toRequest.receivePayList=this.receivePayList;
		toRequest.tranUserMap=this.tranUserMap;
	}
	public String toString(){
		String tmp = "";
		tmp=tmp+"version="+version+"\n";
		tmp=tmp+"application="+application+"\n";
		tmp=tmp+"merchantId="+merchantId+"\n";
		tmp=tmp+"merchantOrderId="+merchantOrderId+"\n";
		tmp=tmp+"merchantName="+merchantName+"\n";
		tmp=tmp+"merchantOrderAmt="+merchantOrderAmt+"\n";
		tmp=tmp+"merchantOrderDesc="+merchantOrderDesc+"\n";
		tmp=tmp+"merchantNotifyUrl="+merchantNotifyUrl+"\n";
		tmp=tmp+"merchantFrontEndUrl="+merchantFrontEndUrl+"\n";
		tmp=tmp+"userMobileNo="+userMobileNo+"\n";
		tmp=tmp+"credentialType="+credentialType+"\n";
		tmp=tmp+"credentialNo="+credentialNo+"\n";
		tmp=tmp+"msgExt="+msgExt+"\n";
		tmp=tmp+"orderTime="+orderTime+"\n";
		tmp=tmp+"bizType="+bizType+"\n";
		tmp=tmp+"rptType="+rptType+"\n";
		tmp=tmp+"payMode="+payMode+"\n";
		tmp=tmp+"merchantRefundAmt="+merchantRefundAmt+"\n";
		tmp=tmp+"application="+application+"\n";
		tmp=tmp+"version="+version+"\n";
		tmp=tmp+"respCode="+respCode+"\n";
		tmp=tmp+"respDesc="+respDesc+"\n";
		tmp=tmp+"responseStr="+responseStr+"\n";
		tmp=tmp+"accountDate="+accountDate+"\n";
		return tmp;
	}
}
