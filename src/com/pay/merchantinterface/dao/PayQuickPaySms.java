package com.pay.merchantinterface.dao;

import java.util.Date;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_QUICK_PAY_SMS entity.
 * 
 * @author administrator
 */
public class PayQuickPaySms {
	public PayQuickPaySms(){}
	public PayQuickPaySms(String orderNo){
		this.orderNo = orderNo;
		Random r = new Random();
		code = String.valueOf(r.nextInt(100000000)+100000000);
		code = code.substring(code.length()-6);
	}
	public String orderNo;
	public String code;
	public long times=3;
	public Date time=new Date();
    public String getOrderNo(){
         return orderNo;
    }
    public void setOrderNo(String orderNo){
         this.orderNo=orderNo;
    }
    public String getCode(){
         return code;
    }
    public void setCode(String code){
         this.code=code;
    }
    public Long getTimes(){
         return times;
    }
    public void setTimes(Long times){
         this.times=times;
    }
    public Date getTime(){
         return time;
    }
    public void setTime(Date time){
         this.time=time;
    }
    public String toString(){
        String temp = "";
        temp = temp + "orderNo="+orderNo+"\n";
        temp = temp + "code="+code+"\n";
        temp = temp + "times="+times+"\n";
        temp = temp + "time="+time+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("orderNo",orderNo);
        json.put("code",code);
        json.put("times",String.valueOf(times));
        try{
            json.put("time", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
        } catch (Exception e) {}
        return json;
    }
}