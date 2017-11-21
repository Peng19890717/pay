package com.pay.cardbin.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_CARD_BIN entity.
 * 
 * @author administrator
 */
public class PayCardBin {
    public String binId;
    public String bankCode;
    public String cardType;
    public String cardName;
    public String binNo;
    public Long cardLength;
    public String bankNo;
    public String extensions;
    public String enableFlag;
    public String memo;
    public String version;
    public Date gmtCreate;
    public Date gmtModify;
    public String bankName;
    public String getBinId(){
         return binId;
    }
    public void setBinId(String binId){
         this.binId=binId;
    }
    public String getBankCode(){
         return bankCode;
    }
    public void setBankCode(String bankCode){
         this.bankCode=bankCode;
    }
    public String getCardType(){
         return cardType;
    }
    public void setCardType(String cardType){
         this.cardType=cardType;
    }
    public String getCardName(){
         return cardName;
    }
    public void setCardName(String cardName){
         this.cardName=cardName;
    }
    public String getBinNo(){
         return binNo;
    }
    public void setBinNo(String binNo){
         this.binNo=binNo;
    }
    public Long getCardLength(){
         return cardLength;
    }
    public void setCardLength(Long cardLength){
         this.cardLength=cardLength;
    }
    public String getBankNo(){
         return bankNo;
    }
    public void setBankNo(String bankNo){
         this.bankNo=bankNo;
    }
    public String getExtensions(){
         return extensions;
    }
    public void setExtensions(String extensions){
         this.extensions=extensions;
    }
    public String getEnableFlag(){
         return enableFlag;
    }
    public void setEnableFlag(String enableFlag){
         this.enableFlag=enableFlag;
    }
    public String getMemo(){
         return memo;
    }
    public void setMemo(String memo){
         this.memo=memo;
    }
    public String getVersion(){
         return version;
    }
    public void setVersion(String version){
         this.version=version;
    }
    public Date getGmtCreate(){
         return gmtCreate;
    }
    public void setGmtCreate(Date gmtCreate){
         this.gmtCreate=gmtCreate;
    }
    public Date getGmtModify(){
         return gmtModify;
    }
    public void setGmtModify(Date gmtModify){
         this.gmtModify=gmtModify;
    }
    public String getBankName(){
         return bankName;
    }
    public void setBankName(String bankName){
         this.bankName=bankName;
    }
    public String toString(){
        String temp = "";
        temp = temp + "binId="+binId+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "cardType="+cardType+"\n";
        temp = temp + "cardName="+cardName+"\n";
        temp = temp + "binNo="+binNo+"\n";
        temp = temp + "cardLength="+cardLength+"\n";
        temp = temp + "bankNo="+bankNo+"\n";
        temp = temp + "extensions="+extensions+"\n";
        temp = temp + "enableFlag="+enableFlag+"\n";
        temp = temp + "memo="+memo+"\n";
        temp = temp + "version="+version+"\n";
        temp = temp + "gmtCreate="+gmtCreate+"\n";
        temp = temp + "gmtModify="+gmtModify+"\n";
        temp = temp + "bankName="+bankName+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("binId",binId);
        json.put("bankCode",bankCode);
        json.put("cardType",cardType);
        json.put("cardName",cardName);
        json.put("binNo",binNo);
        json.put("cardLength",String.valueOf(cardLength));
        json.put("bankNo",bankNo);
        json.put("extensions",extensions);
        json.put("enableFlag",enableFlag);
        json.put("memo",memo);
        json.put("version",version);
        try{
            json.put("gmtCreate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(gmtCreate));
        } catch (Exception e) {}
        try{
            json.put("gmtModify", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(gmtModify));
        } catch (Exception e) {}
        json.put("bankName",bankName);
        return json;
    }
}