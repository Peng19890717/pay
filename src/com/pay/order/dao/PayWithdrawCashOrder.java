package com.pay.order.dao;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
/**
 * Table PAY_WITHDRAW_CASH_ORDER entity.
 * 
 * @author administrator
 */
public class PayWithdrawCashOrder {
    public String casordno = "";
    public Date actTime;
//    public Date sucTime = new Date();
    public Date sucTime;
    public String ordstatus = "";
    public String txccy = "";
    public Long txamt = 0l;
    public Long fee = 0l;
    public Long netrecAmt = 0l;
    public String withdrawWay = "";
    public String bankCode = "";
    public String bankName = "";
    public Date bankstlDate = new Date();
    public String bankjrnno = "";
    public String bankpayacno = "";
    public String bankpayusernm = "";
    public String bankerror = "";
    public String backerror = "";
    public String notifyurl = "";
    public String custId = "";
    public String payret = "";
    public Date bnkdat = new Date();
    public String bnkjnl = "";
    public String tracenumber = "";
    public Date tracetime = new Date();
    public Date updateTime = new Date();
    public String updateUser = "";
    public String remark = "";
    public String filed1 = "";
    public String filed2 = "";
    public String withdrawType = "";
    public String custType = "";
    public String issuer = "";
    public String mobileNo = "";
    public String accountName = "";
    public String branchCity = "";
    public String branchProvince = "";
    public String certType = "";
    public String certNo = "";
    public String resMessage="";
    public String withdrawChannel="";
    // 查询条件补充
    public Date actTimeEnd;
    
    public Date getActTimeEnd() {
		return actTimeEnd;
	}
	public void setActTimeEnd(Date actTimeEnd) {
		this.actTimeEnd = actTimeEnd;
	}
	
	public String getCasordno(){
         return casordno;
    }
    public void setCasordno(String casordno){
         this.casordno=casordno;
    }
    public Date getActTime(){
         return actTime;
    }
    public void setActTime(Date actTime){
         this.actTime=actTime;
    }
    public Date getSucTime(){
         return sucTime;
    }
    public void setSucTime(Date sucTime){
         this.sucTime=sucTime;
    }
    public String getOrdstatus(){
         return ordstatus;
    }
    public void setOrdstatus(String ordstatus){
         this.ordstatus=ordstatus;
    }
    public String getTxccy(){
         return txccy;
    }
    public void setTxccy(String txccy){
         this.txccy=txccy;
    }
    public Long getTxamt(){
         return txamt;
    }
    public void setTxamt(Long txamt){
         this.txamt=txamt;
    }
    public Long getFee(){
         return fee;
    }
    public void setFee(Long fee){
         this.fee=fee;
    }
    public Long getNetrecAmt(){
         return netrecAmt;
    }
    public void setNetrecAmt(Long netrecAmt){
         this.netrecAmt=netrecAmt;
    }
    public String getWithdrawWay(){
         return withdrawWay;
    }
    public void setWithdrawWay(String withdrawWay){
         this.withdrawWay=withdrawWay;
    }
    public String getBankCode(){
         return bankCode;
    }
    public void setBankCode(String bankCode){
         this.bankCode=bankCode;
    }
    public String getBankName(){
         return bankName;
    }
    public void setBankName(String bankName){
         this.bankName=bankName;
    }
    public Date getBankstlDate(){
         return bankstlDate;
    }
    public void setBankstlDate(Date bankstlDate){
         this.bankstlDate=bankstlDate;
    }
    public String getBankjrnno(){
         return bankjrnno;
    }
    public void setBankjrnno(String bankjrnno){
         this.bankjrnno=bankjrnno;
    }
    public String getBankpayacno(){
         return bankpayacno;
    }
    public void setBankpayacno(String bankpayacno){
         this.bankpayacno=bankpayacno;
    }
    public String getBankpayusernm(){
         return bankpayusernm;
    }
    public void setBankpayusernm(String bankpayusernm){
         this.bankpayusernm=bankpayusernm;
    }
    public String getBankerror(){
         return bankerror;
    }
    public void setBankerror(String bankerror){
         this.bankerror=bankerror;
    }
    public String getBackerror(){
         return backerror;
    }
    public void setBackerror(String backerror){
         this.backerror=backerror;
    }
    public String getNotifyurl(){
         return notifyurl;
    }
    public void setNotifyurl(String notifyurl){
         this.notifyurl=notifyurl;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getPayret(){
         return payret;
    }
    public void setPayret(String payret){
         this.payret=payret;
    }
    public Date getBnkdat(){
         return bnkdat;
    }
    public void setBnkdat(Date bnkdat){
         this.bnkdat=bnkdat;
    }
    public String getBnkjnl(){
         return bnkjnl;
    }
    public void setBnkjnl(String bnkjnl){
         this.bnkjnl=bnkjnl;
    }
    public String getTracenumber(){
         return tracenumber;
    }
    public void setTracenumber(String tracenumber){
         this.tracenumber=tracenumber;
    }
    public Date getTracetime(){
         return tracetime;
    }
    public void setTracetime(Date tracetime){
         this.tracetime=tracetime;
    }
    public Date getUpdateTime(){
         return updateTime;
    }
    public void setUpdateTime(Date updateTime){
         this.updateTime=updateTime;
    }
    public String getUpdateUser(){
         return updateUser;
    }
    public void setUpdateUser(String updateUser){
         this.updateUser=updateUser;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String getFiled1(){
         return filed1;
    }
    public void setFiled1(String filed1){
         this.filed1=filed1;
    }
    public String getFiled2(){
         return filed2;
    }
    public void setFiled2(String filed2){
         this.filed2=filed2;
    }
    
    public String getWithdrawType() {
		return withdrawType;
	}
	public void setWithdrawType(String withdrawType) {
		this.withdrawType = withdrawType;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBranchCity() {
		return branchCity;
	}
	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}
	public String getBranchProvince() {
		return branchProvince;
	}
	public void setBranchProvince(String branchProvince) {
		this.branchProvince = branchProvince;
	}
	
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getResMessage() {
		return resMessage;
	}
	public void setResMessage(String resMessage) {
		this.resMessage = resMessage;
	}
	
	public String getWithdrawChannel() {
		return withdrawChannel;
	}
	public void setWithdrawChannel(String withdrawChannel) {
		this.withdrawChannel = withdrawChannel;
	}
	public String toString(){
        String temp = "";
        temp = temp + "casordno="+casordno+"\n";
        temp = temp + "actTime="+actTime+"\n";
        temp = temp + "actTimeEnd="+actTimeEnd+"\n";
        temp = temp + "sucTime="+sucTime+"\n";
        temp = temp + "ordstatus="+ordstatus+"\n";
        temp = temp + "txccy="+txccy+"\n";
        temp = temp + "txamt="+txamt+"\n";
        temp = temp + "fee="+fee+"\n";
        temp = temp + "netrecAmt="+netrecAmt+"\n";
        temp = temp + "withdrawWay="+withdrawWay+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "bankName="+bankName+"\n";
        temp = temp + "bankstlDate="+bankstlDate+"\n";
        temp = temp + "bankjrnno="+bankjrnno+"\n";
        temp = temp + "bankpayacno="+bankpayacno+"\n";
        temp = temp + "bankpayusernm="+bankpayusernm+"\n";
        temp = temp + "bankerror="+bankerror+"\n";
        temp = temp + "backerror="+backerror+"\n";
        temp = temp + "notifyurl="+notifyurl+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "payret="+payret+"\n";
        temp = temp + "bnkdat="+bnkdat+"\n";
        temp = temp + "bnkjnl="+bnkjnl+"\n";
        temp = temp + "tracenumber="+tracenumber+"\n";
        temp = temp + "tracetime="+tracetime+"\n";
        temp = temp + "updateTime="+updateTime+"\n";
        temp = temp + "updateUser="+updateUser+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "certType="+certType+"\n";
        temp = temp + "certNo="+certNo+"\n";
        temp = temp + "filed1="+filed1+"\n";
        temp = temp + "filed2="+filed2+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("casordno",casordno);
        try{
            json.put("actTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actTime));
            json.put("actTimeEnd", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actTimeEnd));
        } catch (Exception e) {}
        try{
            json.put("sucTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sucTime));
        } catch (Exception e) {}
        json.put("ordstatus",ordstatus);
        json.put("txccy",txccy);
        json.put("txamt",String.format("%.2f",(float)txamt*0.01));
        json.put("fee",String.format("%.2f",(float)fee*0.01));
        json.put("netrecAmt",String.valueOf(netrecAmt));
        json.put("withdrawWay",withdrawWay);
        PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(bankpayacno);
        json.put("bankCode",cardBin==null?"":cardBin.bankCode);
        json.put("bankName",cardBin==null?"":cardBin.bankName);
        try{
            json.put("bankstlDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bankstlDate));
        } catch (Exception e) {}
        json.put("bankjrnno",bankjrnno);
        json.put("bankpayacno",bankpayacno);
        json.put("bankpayusernm",bankpayusernm);
        json.put("bankerror",bankerror);
        json.put("backerror",backerror);
        json.put("notifyurl",notifyurl);
        json.put("custId",custId);
        json.put("payret",payret);
        try{
            json.put("bnkdat", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bnkdat));
        } catch (Exception e) {}
        json.put("bnkjnl",bnkjnl);
        json.put("tracenumber",tracenumber);
        try{
            json.put("tracetime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tracetime));
        } catch (Exception e) {}
        try{
            json.put("updateTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime));
        } catch (Exception e) {}
        json.put("updateUser",updateUser);
        json.put("remark",remark);
        json.put("filed1",filed1);
        json.put("filed2",filed2);
        json.put("withdrawType",withdrawType);
        json.put("custType",custType);
        json.put("issuer",issuer);
        json.put("mobileNo",mobileNo);
        json.put("accountName",accountName);
        json.put("branchCity",branchCity);
        json.put("branchProvince",branchProvince);
        json.put("resMessage",resMessage);
        json.put("withdrawChannel",withdrawChannel);
        return json;
    }
}