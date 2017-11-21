package com.pay.risk.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_RISK_USER_LIMIT_SUB entity.
 * 
 * @author administrator
 */
public class PayRiskUserLimitSub {
    public String id;
    public String limitBusClient;
    public Long minAmt;
    public Long maxAmt;
    public Long dayTimes;
    public Long daySucTimes;
    public Long dayAmt;
    public Long daySucAmt;
    public Long monthTimes;
    public Long monthSucTimes;
    public Long monthAmt;
    public Long monthSucAmt;
    public String riskUserLimitId;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getLimitBusClient(){
         return limitBusClient;
    }
    public void setLimitBusClient(String limitBusClient){
         this.limitBusClient=limitBusClient;
    }
    public Long getMinAmt(){
         return minAmt;
    }
    public void setMinAmt(Long minAmt){
         this.minAmt=minAmt;
    }
    public Long getMaxAmt(){
         return maxAmt;
    }
    public void setMaxAmt(Long maxAmt){
         this.maxAmt=maxAmt;
    }
    public Long getDayTimes(){
         return dayTimes;
    }
    public void setDayTimes(Long dayTimes){
         this.dayTimes=dayTimes;
    }
    public Long getDaySucTimes(){
         return daySucTimes;
    }
    public void setDaySucTimes(Long daySucTimes){
         this.daySucTimes=daySucTimes;
    }
    public Long getDayAmt(){
         return dayAmt;
    }
    public void setDayAmt(Long dayAmt){
         this.dayAmt=dayAmt;
    }
    public Long getDaySucAmt(){
         return daySucAmt;
    }
    public void setDaySucAmt(Long daySucAmt){
         this.daySucAmt=daySucAmt;
    }
    public Long getMonthTimes(){
         return monthTimes;
    }
    public void setMonthTimes(Long monthTimes){
         this.monthTimes=monthTimes;
    }
    public Long getMonthSucTimes(){
         return monthSucTimes;
    }
    public void setMonthSucTimes(Long monthSucTimes){
         this.monthSucTimes=monthSucTimes;
    }
    public Long getMonthAmt(){
         return monthAmt;
    }
    public void setMonthAmt(Long monthAmt){
         this.monthAmt=monthAmt;
    }
    public Long getMonthSucAmt(){
         return monthSucAmt;
    }
    public void setMonthSucAmt(Long monthSucAmt){
         this.monthSucAmt=monthSucAmt;
    }
    public String getRiskUserLimitId(){
         return riskUserLimitId;
    }
    public void setRiskUserLimitId(String riskUserLimitId){
         this.riskUserLimitId=riskUserLimitId;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "limitBusClient="+limitBusClient+"\n";
        temp = temp + "minAmt="+minAmt+"\n";
        temp = temp + "maxAmt="+maxAmt+"\n";
        temp = temp + "dayTimes="+dayTimes+"\n";
        temp = temp + "daySucTimes="+daySucTimes+"\n";
        temp = temp + "dayAmt="+dayAmt+"\n";
        temp = temp + "daySucAmt="+daySucAmt+"\n";
        temp = temp + "monthTimes="+monthTimes+"\n";
        temp = temp + "monthSucTimes="+monthSucTimes+"\n";
        temp = temp + "monthAmt="+monthAmt+"\n";
        temp = temp + "monthSucAmt="+monthSucAmt+"\n";
        temp = temp + "riskUserLimitId="+riskUserLimitId+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("limitBusClient",limitBusClient);
        json.put("minAmt",String.valueOf(minAmt));
        json.put("maxAmt",String.valueOf(maxAmt));
        json.put("dayTimes",String.valueOf(dayTimes));
        json.put("daySucTimes",String.valueOf(daySucTimes));
        json.put("dayAmt",String.valueOf(dayAmt));
        json.put("daySucAmt",String.valueOf(daySucAmt));
        json.put("monthTimes",String.valueOf(monthTimes));
        json.put("monthSucTimes",String.valueOf(monthSucTimes));
        json.put("monthAmt",String.valueOf(monthAmt));
        json.put("monthSucAmt",String.valueOf(monthSucAmt));
        json.put("riskUserLimitId",riskUserLimitId);
        return json;
    }
}