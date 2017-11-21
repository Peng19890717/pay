package com.pay.qtbao.dao;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_FUNDIN_FUNDOUT entity.
 * 
 * @author administrator
 */
public class PayFundinFundout {
    public String merchantid;
    public String application;
    public String userid;
    public String orderno;
    public String amt;
    public String remark;
    public Date timestamp;
    public String timestampStart;
    public String timestampEnd;
    public String sign;
    public String custType;
    
    
    public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getTimestampStart() {
		return timestampStart;
	}
	public void setTimestampStart(String timestampStart) {
		this.timestampStart = timestampStart;
	}
	public String getTimestampEnd() {
		return timestampEnd;
	}
	public void setTimestampEnd(String timestampEnd) {
		this.timestampEnd = timestampEnd;
	}
	public String getMerchantid(){
         return merchantid;
    }
    public void setMerchantid(String merchantid){
         this.merchantid=merchantid;
    }
    public String getApplication(){
         return application;
    }
    public void setApplication(String application){
         this.application=application;
    }
    public String getUserid(){
         return userid;
    }
    public void setUserid(String userid){
         this.userid=userid;
    }
    public String getOrderno(){
         return orderno;
    }
    public void setOrderno(String orderno){
         this.orderno=orderno;
    }
    public String getAmt(){
         return amt;
    }
    public void setAmt(String amt){
         this.amt=amt;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign(){
         return sign;
    }
    public void setSign(String sign){
         this.sign=sign;
    }
    public String toString(){
        String temp = "";
        temp = temp + "merchantid="+merchantid+"\n";
        temp = temp + "application="+application+"\n";
        temp = temp + "userid="+userid+"\n";
        temp = temp + "orderno="+orderno+"\n";
        temp = temp + "amt="+amt+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "timestamp="+timestamp+"\n";
        temp = temp + "sign="+sign+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("merchantid",merchantid);
        json.put("application",application);
        json.put("userid",userid);
        json.put("orderno",orderno);
        json.put("amt",String.format("%.2f",(float)(Long.parseLong(amt))*0.01));
        json.put("remark",remark);
        json.put("custType",custType);
        try{
        	json.put("timestamp", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
        } catch (Exception e) {}
        json.put("sign",sign);
        return json;
    }
}