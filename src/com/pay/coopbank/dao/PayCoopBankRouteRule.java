package com.pay.coopbank.dao;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_COOP_BANK_ROUTE_RULE entity.
 * 
 * @author administrator
 */
public class PayCoopBankRouteRule {
    public String id;
    public String ruleType;
    public Long divisionCount;
    public Long divisionMaxAmt;
    public String channelCode;
    public String tranType;
    public Long weight;
    public String remark;
    public String createId;
    public Date createTime;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getRuleType(){
         return ruleType;
    }
    public void setRuleType(String ruleType){
         this.ruleType=ruleType;
    }
    public Long getDivisionCount(){
         return divisionCount;
    }
    public void setDivisionCount(Long divisionCount){
         this.divisionCount=divisionCount;
    }
    public Long getDivisionMaxAmt(){
         return divisionMaxAmt;
    }
    public void setDivisionMaxAmt(Long divisionMaxAmt){
         this.divisionMaxAmt=divisionMaxAmt;
    }
    public String getChannelCode(){
         return channelCode;
    }
    public void setChannelCode(String channelCode){
         this.channelCode=channelCode;
    }
    public String getTranType(){
         return tranType;
    }
    public void setTranType(String tranType){
         this.tranType=tranType;
    }
    public Long getWeight(){
         return weight;
    }
    public void setWeight(Long weight){
         this.weight=weight;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String getCreateId(){
         return createId;
    }
    public void setCreateId(String createId){
         this.createId=createId;
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
        temp = temp + "ruleType="+ruleType+"\n";
        temp = temp + "divisionCount="+divisionCount+"\n";
        temp = temp + "divisionMaxAmt="+divisionMaxAmt+"\n";
        temp = temp + "channelCode="+channelCode+"\n";
        temp = temp + "tranType="+tranType+"\n";
        temp = temp + "weight="+weight+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "createId="+createId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("ruleType",ruleType);
        json.put("divisionCount",String.valueOf(divisionCount));
        json.put("divisionMaxAmt",String.valueOf(divisionMaxAmt));
        json.put("channelCode",channelCode);
        json.put("tranType",tranType);
        json.put("weight",String.valueOf(weight));
        json.put("remark",remark);
        json.put("createId",createId);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}