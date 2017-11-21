package com.pay.merchant.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * Table PAY_MERCHANT_NEWS entity.
 * 
 * @author administrator
 */
public class PayMerchantNews {
    public String id;
    public String type;
    public String title;
    public String content;
    public Date optTime;
    public Date optTimeStart;
    public Date optTimeEnd;
    public String optUserId;
    public String flag2;
    
    public static Map<String,String> typeMap = new HashMap<String, String>();
    public static Map<String,String> flagMap = new HashMap<String, String>();
    
    static{
    	typeMap.put("0", "新闻");
    	typeMap.put("1", "公告");
    	typeMap.put("2", "通知");
    	
    	flagMap.put("0", "有效");
    	flagMap.put("1", "作废");
    }
    
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getType(){
         return type;
    }
    public void setType(String type){
         this.type=type;
    }
    public String getTitle(){
         return title;
    }
    public void setTitle(String title){
         this.title=title;
    }
    public String getContent(){
         return content;
    }
    public void setContent(String content){
         this.content=content;
    }
    public Date getOptTime(){
         return optTime;
    }
    public void setOptTime(Date optTime){
         this.optTime=optTime;
    }
    public String getOptUserId(){
         return optUserId;
    }
    public void setOptUserId(String optUserId){
         this.optUserId=optUserId;
    }
    public String getFlag2() {
		return flag2;
	}
	public void setFlag2(String flag2) {
		this.flag2 = flag2;
	}
	public Date getOptTimeStart() {
		return optTimeStart;
	}
	public void setOptTimeStart(Date optTimeStart) {
		this.optTimeStart = optTimeStart;
	}
	public Date getOptTimeEnd() {
		return optTimeEnd;
	}
	public void setOptTimeEnd(Date optTimeEnd) {
		this.optTimeEnd = optTimeEnd;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "type="+type+"\n";
        temp = temp + "title="+title+"\n";
        temp = temp + "content="+content+"\n";
        temp = temp + "optTime="+optTime+"\n";
        temp = temp + "optUserId="+optUserId+"\n";
        temp = temp + "flag="+flag2+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("type",type);
        json.put("title",title);
        json.put("content",content);
        try{
            json.put("optTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(optTime));
        } catch (Exception e) {}
        json.put("optUserId",optUserId);
        json.put("flag",flag2);
        return json;
    }
}