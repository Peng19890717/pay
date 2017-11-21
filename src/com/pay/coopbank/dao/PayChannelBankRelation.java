package com.pay.coopbank.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_CHANNEL_BANK_RELATION entity.
 * 
 * @author administrator
 */
public class PayChannelBankRelation {

    public String id;
    public String bankCode;
    public String channelCode;
    public String supportedUserType;
    public String quickUserType;
    public String withdrawUserType;
    public String receiveUserType;
    public Long webDebitMaxAmt;
    public Long webCreditMaxAmt;
    public Long webPublicMaxAmt;
    public Long quickDebitMaxAmt;
    public Long quickCeditMaxAmt;
    public Long receiveMaxAmt;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getBankCode(){
         return bankCode;
    }
    public void setBankCode(String bankCode){
         this.bankCode=bankCode;
    }
    public String getChannelCode(){
         return channelCode;
    }
    public void setChannelCode(String channelCode){
         this.channelCode=channelCode;
    }
    public String getSupportedUserType(){
         return supportedUserType;
    }
    public void setSupportedUserType(String supportedUserType){
         this.supportedUserType=supportedUserType;
    }
    public String getQuickUserType(){
         return quickUserType;
    }
    public void setQuickUserType(String quickUserType){
         this.quickUserType=quickUserType;
    }
    public String getWithdrawUserType(){
         return withdrawUserType;
    }
    public void setWithdrawUserType(String withdrawUserType){
         this.withdrawUserType=withdrawUserType;
    }
    public String getReceiveUserType(){
         return receiveUserType;
    }
    public void setReceiveUserType(String receiveUserType){
         this.receiveUserType=receiveUserType;
    }
    public Long getWebDebitMaxAmt(){
         return webDebitMaxAmt;
    }
    public void setWebDebitMaxAmt(Long webDebitMaxAmt){
         this.webDebitMaxAmt=webDebitMaxAmt;
    }
    public Long getWebCreditMaxAmt(){
         return webCreditMaxAmt;
    }
    public void setWebCreditMaxAmt(Long webCreditMaxAmt){
         this.webCreditMaxAmt=webCreditMaxAmt;
    }
    public Long getWebPublicMaxAmt(){
         return webPublicMaxAmt;
    }
    public void setWebPublicMaxAmt(Long webPublicMaxAmt){
         this.webPublicMaxAmt=webPublicMaxAmt;
    }
    public Long getQuickDebitMaxAmt(){
         return quickDebitMaxAmt;
    }
    public void setQuickDebitMaxAmt(Long quickDebitMaxAmt){
         this.quickDebitMaxAmt=quickDebitMaxAmt;
    }
    public Long getQuickCeditMaxAmt(){
         return quickCeditMaxAmt;
    }
    public void setQuickCeditMaxAmt(Long quickCeditMaxAmt){
         this.quickCeditMaxAmt=quickCeditMaxAmt;
    }
    public Long getReceiveMaxAmt(){
         return receiveMaxAmt;
    }
    public void setReceiveMaxAmt(Long receiveMaxAmt){
         this.receiveMaxAmt=receiveMaxAmt;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "channelCode="+channelCode+"\n";
        temp = temp + "supportedUserType="+supportedUserType+"\n";
        temp = temp + "quickUserType="+quickUserType+"\n";
        temp = temp + "withdrawUserType="+withdrawUserType+"\n";
        temp = temp + "receiveUserType="+receiveUserType+"\n";
        temp = temp + "webDebitMaxAmt="+webDebitMaxAmt+"\n";
        temp = temp + "webCreditMaxAmt="+webCreditMaxAmt+"\n";
        temp = temp + "webPublicMaxAmt="+webPublicMaxAmt+"\n";
        temp = temp + "quickDebitMaxAmt="+quickDebitMaxAmt+"\n";
        temp = temp + "quickCeditMaxAmt="+quickCeditMaxAmt+"\n";
        temp = temp + "receiveMaxAmt="+receiveMaxAmt+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("bankCode",bankCode);
        json.put("channelCode",channelCode);
        json.put("supportedUserType",supportedUserType);
        json.put("quickUserType",quickUserType);
        json.put("withdrawUserType",withdrawUserType);
        json.put("receiveUserType",receiveUserType);
        json.put("webDebitMaxAmt",String.valueOf(webDebitMaxAmt));
        json.put("webCreditMaxAmt",String.valueOf(webCreditMaxAmt));
        json.put("webPublicMaxAmt",String.valueOf(webPublicMaxAmt));
        json.put("quickDebitMaxAmt",String.valueOf(quickDebitMaxAmt));
        json.put("quickCeditMaxAmt",String.valueOf(quickCeditMaxAmt));
        json.put("receiveMaxAmt",String.valueOf(receiveMaxAmt));
        return json;
    }
}