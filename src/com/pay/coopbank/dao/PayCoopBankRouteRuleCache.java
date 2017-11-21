package com.pay.coopbank.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_COOP_BANK_ROUTE_RULE_CACHE entity.
 * 
 * @author administrator
 */
public class PayCoopBankRouteRuleCache {
    public String id;
    public String curDate;
    public Long divisionCount;
    public String channelCode;
    public String tranType;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getCurDate(){
         return curDate;
    }
    public void setCurDate(String curDate){
         this.curDate=curDate;
    }
    public Long getDivisionCount(){
         return divisionCount;
    }
    public void setDivisionCount(Long divisionCount){
         this.divisionCount=divisionCount;
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
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "curDate="+curDate+"\n";
        temp = temp + "divisionCount="+divisionCount+"\n";
        temp = temp + "channelCode="+channelCode+"\n";
        temp = temp + "tranType="+tranType+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("curDate",curDate);
        json.put("divisionCount",String.valueOf(divisionCount));
        json.put("channelCode",channelCode);
        json.put("tranType",tranType);
        return json;
    }
}