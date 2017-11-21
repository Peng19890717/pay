package com.pay.coopbank.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_CHANNEL_ROTATION_LOG entity.
 * 
 * @author administrator
 */
public class PayChannelRotationLog {
    public String channelId;
    public String day;
    public Long dayAmt;
    public Long daySuccessAmt;
    public Long dayCount;
    public Long daySuccessCount;
    public Long channelFee;
    public String channelAcc;
    public Long dayCountZfbScan=0l;
    public Long dayCountWeixinScan=0l;
    public Long dayCountQqScan=0l;
    public Long daySucCountZfbScan=0l;
    public Long daySucCountWeixinScan=0l;
    public Long daySucCountQqScan=0l;
    public Long dayAmtZfbScan=0l;
    public Long dayAmtWeixinScan=0l;
    public Long dayAmtQqScan=0l;
    public Long daySucAmtZfbScan=0l;
    public Long daySucAmtWeixinScan=0l;
    public Long daySucAmtQqScan=0l;
    public String getChannelId(){
         return channelId;
    }
    public void setChannelId(String channelId){
         this.channelId=channelId;
    }
    public String getDay(){
         return day;
    }
    public void setDay(String day){
         this.day=day;
    }
    public Long getDayAmt(){
         return dayAmt;
    }
    public void setDayAmt(Long dayAmt){
         this.dayAmt=dayAmt;
    }
    public Long getDaySuccessAmt(){
         return daySuccessAmt;
    }
    public void setDaySuccessAmt(Long daySuccessAmt){
         this.daySuccessAmt=daySuccessAmt;
    }
    public Long getDayCount(){
         return dayCount;
    }
    public void setDayCount(Long dayCount){
         this.dayCount=dayCount;
    }
    public Long getDaySuccessCount(){
         return daySuccessCount;
    }
    public void setDaySuccessCount(Long daySuccessCount){
         this.daySuccessCount=daySuccessCount;
    }
    public Long getChannelFee(){
         return channelFee;
    }
    public void setChannelFee(Long channelFee){
         this.channelFee=channelFee;
    }
    public String getChannelAcc(){
         return channelAcc;
    }
    public void setChannelAcc(String channelAcc){
         this.channelAcc=channelAcc;
    }
    public Long getDayCountZfbScan(){
         return dayCountZfbScan;
    }
    public void setDayCountZfbScan(Long dayCountZfbScan){
         this.dayCountZfbScan=dayCountZfbScan;
    }
    public Long getDayCountWeixinScan(){
         return dayCountWeixinScan;
    }
    public void setDayCountWeixinScan(Long dayCountWeixinScan){
         this.dayCountWeixinScan=dayCountWeixinScan;
    }
    public Long getDayCountQqScan(){
         return dayCountQqScan;
    }
    public void setDayCountQqScan(Long dayCountQqScan){
         this.dayCountQqScan=dayCountQqScan;
    }
    public Long getDaySucCountZfbScan(){
         return daySucCountZfbScan;
    }
    public void setDaySucCountZfbScan(Long daySucCountZfbScan){
         this.daySucCountZfbScan=daySucCountZfbScan;
    }
    public Long getDaySucCountWeixinScan(){
         return daySucCountWeixinScan;
    }
    public void setDaySucCountWeixinScan(Long daySucCountWeixinScan){
         this.daySucCountWeixinScan=daySucCountWeixinScan;
    }
    public Long getDaySucCountQqScan(){
         return daySucCountQqScan;
    }
    public void setDaySucCountQqScan(Long daySucCountQqScan){
         this.daySucCountQqScan=daySucCountQqScan;
    }
    public Long getDayAmtZfbScan(){
         return dayAmtZfbScan;
    }
    public void setDayAmtZfbScan(Long dayAmtZfbScan){
         this.dayAmtZfbScan=dayAmtZfbScan;
    }
    public Long getDayAmtWeixinScan(){
         return dayAmtWeixinScan;
    }
    public void setDayAmtWeixinScan(Long dayAmtWeixinScan){
         this.dayAmtWeixinScan=dayAmtWeixinScan;
    }
    public Long getDayAmtQqScan(){
         return dayAmtQqScan;
    }
    public void setDayAmtQqScan(Long dayAmtQqScan){
         this.dayAmtQqScan=dayAmtQqScan;
    }
    public Long getDaySucAmtZfbScan(){
         return daySucAmtZfbScan;
    }
    public void setDaySucAmtZfbScan(Long daySucAmtZfbScan){
         this.daySucAmtZfbScan=daySucAmtZfbScan;
    }
    public Long getDaySucAmtWeixinScan(){
         return daySucAmtWeixinScan;
    }
    public void setDaySucAmtWeixinScan(Long daySucAmtWeixinScan){
         this.daySucAmtWeixinScan=daySucAmtWeixinScan;
    }
    public Long getDaySucAmtQqScan(){
         return daySucAmtQqScan;
    }
    public void setDaySucAmtQqScan(Long daySucAmtQqScan){
         this.daySucAmtQqScan=daySucAmtQqScan;
    }
    public String toString(){
        String temp = "";
        temp = temp + "channelId="+channelId+"\n";
        temp = temp + "day="+day+"\n";
        temp = temp + "dayAmt="+dayAmt+"\n";
        temp = temp + "daySuccessAmt="+daySuccessAmt+"\n";
        temp = temp + "dayCount="+dayCount+"\n";
        temp = temp + "daySuccessCount="+daySuccessCount+"\n";
        temp = temp + "channelFee="+channelFee+"\n";
        temp = temp + "channelAcc="+channelAcc+"\n";
        temp = temp + "dayCountZfbScan="+dayCountZfbScan+"\n";
        temp = temp + "dayCountWeixinScan="+dayCountWeixinScan+"\n";
        temp = temp + "dayCountQqScan="+dayCountQqScan+"\n";
        temp = temp + "daySucCountZfbScan="+daySucCountZfbScan+"\n";
        temp = temp + "daySucCountWeixinScan="+daySucCountWeixinScan+"\n";
        temp = temp + "daySucCountQqScan="+daySucCountQqScan+"\n";
        temp = temp + "dayAmtZfbScan="+dayAmtZfbScan+"\n";
        temp = temp + "dayAmtWeixinScan="+dayAmtWeixinScan+"\n";
        temp = temp + "dayAmtQqScan="+dayAmtQqScan+"\n";
        temp = temp + "daySucAmtZfbScan="+daySucAmtZfbScan+"\n";
        temp = temp + "daySucAmtWeixinScan="+daySucAmtWeixinScan+"\n";
        temp = temp + "daySucAmtQqScan="+daySucAmtQqScan+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("channelId",channelId);
        json.put("day",day);
        json.put("dayAmt",String.format("%.2f",(float)dayAmt*0.01));
        json.put("daySuccessAmt",String.format("%.2f",(float)daySuccessAmt*0.01));
        json.put("dayCount",String.valueOf(dayCount));
        json.put("daySuccessCount",String.valueOf(daySuccessCount));
        json.put("channelFee",String.format("%.2f",(float)channelFee*0.01));
        json.put("channelAcc",channelAcc);
        json.put("dayCountZfbScan",String.valueOf(dayCountZfbScan));
        json.put("dayCountWeixinScan",String.valueOf(dayCountWeixinScan));
        json.put("dayCountQqScan",String.valueOf(dayCountQqScan));
        json.put("daySucCountZfbScan",String.valueOf(daySucCountZfbScan));
        json.put("daySucCountWeixinScan",String.valueOf(daySucCountWeixinScan));
        json.put("daySucCountQqScan",String.valueOf(daySucCountQqScan));
        json.put("dayAmtZfbScan",String.format("%.2f",(float)dayAmtZfbScan*0.01));
        json.put("dayAmtWeixinScan",String.format("%.2f",(float)dayAmtWeixinScan*0.01));
        json.put("dayAmtQqScan",String.format("%.2f",(float)dayAmtQqScan*0.01));
        json.put("daySucAmtZfbScan",String.format("%.2f",(float)daySucAmtZfbScan*0.01));
        json.put("daySucAmtWeixinScan",String.format("%.2f",(float)daySucAmtWeixinScan*0.01));
        json.put("daySucAmtQqScan",String.format("%.2f",(float)daySucAmtQqScan*0.01));
        return json;
    }
}