package com.pay.merchantinterface.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_TRAN_USER_QUICK_CARD entity.
 * 
 * @author administrator
 */
public class PayTranUserQuickCard {
    public String id;
    public String credentialType;
    public String credentialNo;
    public String cardNo;
    public String cvv2;
    public String validPeriod;
    public String bankId;
    public String status;
    public String name;
    public String mobileNo;
    public Date createTime;
    public String merchantId;
    public String payerId;
    public String bindId;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getCredentialType(){
         return credentialType;
    }
    public void setCredentialType(String credentialType){
         this.credentialType=credentialType;
    }
    public String getCredentialNo(){
         return credentialNo;
    }
    public void setCredentialNo(String credentialNo){
         this.credentialNo=credentialNo;
    }
    public String getCardNo(){
         return cardNo;
    }
    public void setCardNo(String cardNo){
         this.cardNo=cardNo;
    }
    public String getCvv2(){
         return cvv2;
    }
    public void setCvv2(String cvv2){
         this.cvv2=cvv2;
    }
    public String getValidPeriod(){
         return validPeriod;
    }
    public void setValidPeriod(String validPeriod){
         this.validPeriod=validPeriod;
    }
    public String getBankId(){
         return bankId;
    }
    public void setBankId(String bankId){
         this.bankId=bankId;
    }
    public String getStatus(){
         return status;
    }
    public void setStatus(String status){
         this.status=status;
    }
    public String getName(){
         return name;
    }
    public void setName(String name){
         this.name=name;
    }
    public String getMobileNo(){
         return mobileNo;
    }
    public void setMobileNo(String mobileNo){
         this.mobileNo=mobileNo;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String getMerchantId(){
         return merchantId;
    }
    public void setMerchantId(String merchantId){
         this.merchantId=merchantId;
    }
    public String getPayerId(){
         return payerId;
    }
    public void setPayerId(String payerId){
         this.payerId=payerId;
    }
	public String toString(){

        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "credentialType="+credentialType+"\n";
        temp = temp + "credentialNo="+credentialNo+"\n";
        temp = temp + "cardNo="+cardNo+"\n";
        temp = temp + "cvv2="+cvv2+"\n";
        temp = temp + "validPeriod="+validPeriod+"\n";
        temp = temp + "bankId="+bankId+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "name="+name+"\n";
        temp = temp + "mobileNo="+mobileNo+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "merchantId="+merchantId+"\n";
        temp = temp + "payerId="+payerId+"\n";
        temp = temp + "bindId="+bindId+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("credentialType",credentialType);
        json.put("credentialNo",credentialNo);
        json.put("cardNo",cardNo);
        json.put("cvv2",cvv2);
        json.put("validPeriod",validPeriod);
        json.put("bankId",bankId);
        json.put("status",status);
        json.put("name",name);
        json.put("mobileNo",mobileNo);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        json.put("merchantId",merchantId);
        json.put("payerId",payerId);
        json.put("bindId",bindId);
        return json;
    }
}