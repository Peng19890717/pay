package com.pay.merchant.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_NOTIFY_FAIL_URL entity.
 * 
 * @author administrator
 */
public class PayNotifyFailUrl {
    public String id;
    public String merchantNo;
    public String url;
    public String errorMsg;
    public Date createTime;
    // 商户搜索时间段结束时间点
    public Date createEndTime;
    
    public Date getCreateEndTime() {
		return createEndTime;
	}
	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getMerchantNo(){
         return merchantNo;
    }
    public void setMerchantNo(String merchantNo){
         this.merchantNo=merchantNo;
    }
    public String getUrl(){
         return url;
    }
    public void setUrl(String url){
         this.url=url;
    }
    public String getErrorMsg(){
         return errorMsg;
    }
    public void setErrorMsg(String errorMsg){
         this.errorMsg=errorMsg;
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
        temp = temp + "merchantNo="+merchantNo+"\n";
        temp = temp + "url="+url+"\n";
        temp = temp + "errorMsg="+errorMsg+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("merchantNo",merchantNo);
        json.put("url",url);
        json.put("errorMsg",errorMsg);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}