package com.common.sms.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table SMS_EXAMPLES entity.
 * 
 * @author administrator
 */
public class SmsExamples {
    public String examplesId;
    public String content;
    public String groupId;
    public String groupName;
    public String createId;
    public Date createTime;
    public String getExamplesId(){
         return examplesId;
    }
    public void setExamplesId(String examplesId){
         this.examplesId=examplesId;
    }
    public String getContent(){
         return content;
    }
    public void setContent(String content){
         this.content=content;
    }
    public String getGroupId(){
         return groupId;
    }
    public void setGroupId(String groupId){
         this.groupId=groupId;
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
        temp = temp + "examplesId="+examplesId+"\n";
        temp = temp + "content="+content+"\n";
        temp = temp + "groupId="+groupId+"\n";
        temp = temp + "createId="+createId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("examplesId",examplesId);
        json.put("content",content);
        json.put("groupId",groupId);
        json.put("groupName",groupName);
        json.put("createId",createId);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}