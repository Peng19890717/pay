package com.pay.account.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_BANK_ACCOUNT_CHECK entity.
 * 
 * @author administrator
 */
public class PayBankAccountCheck {
    public String bankcode;
    public String bankName;
    public String actdate;
    public String actdateStart;
    public String actdateEnd;
    public String bnkordno;
    public String cwtype;
    public String chktype;
    public String bankdate;
    public String banklogno;
    public String ccycod;
    public Long txnamt;
    public Long fee;
    public Long inamt;
    public Long totalAmount;
    public String bkglogno;
    public String bkgdate;
    public String payactno;
    public String mercode;
    public String merName;
    public String godordcode;
    public String bkgtxncod;
    public String bkgtxndate;
    public String bkgtxntim;
    public Long chknum;
    public String srefno;
    public String filed1;
    public String filed2;
    public String status;
    public String lastUpdateUser;
    public Date lastUpdateTime;
    public String remark;
    public Date orderTime;
    public Date payTime;
    
    public Long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getBankcode(){
         return bankcode;
    }
    public void setBankcode(String bankcode){
         this.bankcode=bankcode;
    }
    public String getActdate(){
         return actdate;
    }
    public void setActdate(String actdate){
         this.actdate=actdate;
    }
    
    public String getActdateStart() {
		return actdateStart;
	}
	public void setActdateStart(String actdateStart) {
		this.actdateStart = actdateStart;
	}
	public String getActdateEnd() {
		return actdateEnd;
	}
	public void setActdateEnd(String actdateEnd) {
		this.actdateEnd = actdateEnd;
	}
	public String getBnkordno(){
         return bnkordno;
    }
    public void setBnkordno(String bnkordno){
         this.bnkordno=bnkordno;
    }
    public String getCwtype(){
         return cwtype;
    }
    public void setCwtype(String cwtype){
         this.cwtype=cwtype;
    }
    public String getChktype(){
         return chktype;
    }
    public void setChktype(String chktype){
         this.chktype=chktype;
    }
    public String getBankdate(){
         return bankdate;
    }
    public void setBankdate(String bankdate){
         this.bankdate=bankdate;
    }
    public String getBanklogno(){
         return banklogno;
    }
    public void setBanklogno(String banklogno){
         this.banklogno=banklogno;
    }
    public String getCcycod(){
         return ccycod;
    }
    public void setCcycod(String ccycod){
         this.ccycod=ccycod;
    }
    public Long getTxnamt(){
         return txnamt;
    }
    public void setTxnamt(Long txnamt){
         this.txnamt=txnamt;
    }
    public Long getFee(){
         return fee;
    }
    public void setFee(Long fee){
         this.fee=fee;
    }
    public Long getInamt(){
         return inamt;
    }
    public void setInamt(Long inamt){
         this.inamt=inamt;
    }
    public String getBkglogno(){
         return bkglogno;
    }
    public void setBkglogno(String bkglogno){
         this.bkglogno=bkglogno;
    }
    public String getBkgdate(){
         return bkgdate;
    }
    public void setBkgdate(String bkgdate){
         this.bkgdate=bkgdate;
    }
    public String getPayactno(){
         return payactno;
    }
    public void setPayactno(String payactno){
         this.payactno=payactno;
    }
    public String getMercode(){
         return mercode;
    }
    public void setMercode(String mercode){
         this.mercode=mercode;
    }
    public String getGodordcode(){
         return godordcode;
    }
    public void setGodordcode(String godordcode){
         this.godordcode=godordcode;
    }
    public String getBkgtxncod(){
         return bkgtxncod;
    }
    public void setBkgtxncod(String bkgtxncod){
         this.bkgtxncod=bkgtxncod;
    }
    public String getBkgtxndate(){
         return bkgtxndate;
    }
    public void setBkgtxndate(String bkgtxndate){
         this.bkgtxndate=bkgtxndate;
    }
    public String getBkgtxntim(){
         return bkgtxntim;
    }
    public void setBkgtxntim(String bkgtxntim){
         this.bkgtxntim=bkgtxntim;
    }
    public Long getChknum(){
         return chknum;
    }
    public void setChknum(Long chknum){
         this.chknum=chknum;
    }
    public String getSrefno(){
         return srefno;
    }
    public void setSrefno(String srefno){
         this.srefno=srefno;
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
    public String getStatus(){
         return status;
    }
    public void setStatus(String status){
         this.status=status;
    }
    public String getLastUpdateUser(){
         return lastUpdateUser;
    }
    public void setLastUpdateUser(String lastUpdateUser){
         this.lastUpdateUser=lastUpdateUser;
    }
    public Date getLastUpdateTime(){
         return lastUpdateTime;
    }
    public void setLastUpdateTime(Date lastUpdateTime){
         this.lastUpdateTime=lastUpdateTime;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    
    public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getMerName() {
		return merName;
	}
	public void setMerName(String merName) {
		this.merName = merName;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public String toString(){
        String temp = "";
        temp = temp + "bankcode="+bankcode+"\n";
        temp = temp + "actdate="+actdate+"\n";
        temp = temp + "bnkordno="+bnkordno+"\n";
        temp = temp + "cwtype="+cwtype+"\n";
        temp = temp + "chktype="+chktype+"\n";
        temp = temp + "bankdate="+bankdate+"\n";
        temp = temp + "banklogno="+banklogno+"\n";
        temp = temp + "ccycod="+ccycod+"\n";
        temp = temp + "txnamt="+txnamt+"\n";
        temp = temp + "fee="+fee+"\n";
        temp = temp + "inamt="+inamt+"\n";
        temp = temp + "bkglogno="+bkglogno+"\n";
        temp = temp + "bkgdate="+bkgdate+"\n";
        temp = temp + "payactno="+payactno+"\n";
        temp = temp + "mercode="+mercode+"\n";
        temp = temp + "godordcode="+godordcode+"\n";
        temp = temp + "bkgtxncod="+bkgtxncod+"\n";
        temp = temp + "bkgtxndate="+bkgtxndate+"\n";
        temp = temp + "bkgtxntim="+bkgtxntim+"\n";
        temp = temp + "chknum="+chknum+"\n";
        temp = temp + "srefno="+srefno+"\n";
        temp = temp + "filed1="+filed1+"\n";
        temp = temp + "filed2="+filed2+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "lastUpdateUser="+lastUpdateUser+"\n";
        temp = temp + "lastUpdateTime="+lastUpdateTime+"\n";
        temp = temp + "remark="+remark+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("bankcode",bankcode);
        json.put("bankName",bankName);
        json.put("actdate",actdate.substring(0,4)+"-"+actdate.substring(4,6)+"-"+actdate.substring(6,8));
        json.put("bnkordno",bnkordno);
        json.put("cwtype",cwtype);
        json.put("chktype",chktype);
        json.put("bankdate",bankdate);
        json.put("banklogno",banklogno);
        json.put("ccycod",ccycod);
        json.put("txnamt",String.format("%.2f",(float)txnamt*0.01));
        json.put("fee",String.format("%.2f",(float)fee*0.01));
        json.put("inamt",String.format("%.2f",(float)inamt*0.01));
        json.put("totalAmount",String.format("%.2f",(float)totalAmount*0.01));
        json.put("bkglogno",bkglogno);
        json.put("bkgdate",bkgdate);
        json.put("payactno",payactno);
        json.put("mercode",mercode);
        json.put("merName",merName);
        json.put("godordcode",godordcode);
        json.put("bkgtxncod",bkgtxncod);
        json.put("bkgtxndate",bkgtxndate);
        json.put("bkgtxntim",bkgtxntim);
        json.put("chknum",String.valueOf(chknum));
        json.put("srefno",srefno);
        json.put("filed1",filed1);
        json.put("filed2",filed2);
        json.put("status",status);
        json.put("lastUpdateUser",lastUpdateUser);
        try{
            json.put("lastUpdateTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastUpdateTime));
        } catch (Exception e) {}
        try{
            json.put("orderTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderTime));
        } catch (Exception e) {}
        try{
            json.put("payTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTime));
        } catch (Exception e) {}
        json.put("remark",remark);
        return json;
    }
}