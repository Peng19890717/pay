package com.pay.coopbank.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_CHANNEL_MAX_AMT entity.
 * 
 * @author administrator
 */
public class PayChannelMaxAmt {
    public String id;
    public String bankCode;
    public String tranType;
    public Long maxAmt;
    public Date createTime;
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
    public String getTranType(){
         return tranType;
    }
    public void setTranType(String tranType){
         this.tranType=tranType;
    }
    public Long getMaxAmt(){
         return maxAmt;
    }
    public void setMaxAmt(Long maxAmt){
         this.maxAmt=maxAmt;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "tranType="+tranType+"\n";
        temp = temp + "maxAmt="+maxAmt+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("bankCode",bankCode);
        json.put("tranType",tranType);
        json.put("maxAmt",String.valueOf(maxAmt));
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}