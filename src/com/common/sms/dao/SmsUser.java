package com.common.sms.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table SMS_USER entity.
 * 
 * @author administrator
 */
public class SmsUser {
    public String userId;
    public String name;
    public String tel;
    public String groupId;
    public String groupName;
    public String remark;
    public String createId;
    public Date createTime;
    public String getUserId(){
         return userId;
    }
    public void setUserId(String userId){
         this.userId=userId;
    }
    public String getName(){
         return name;
    }
    public void setName(String name){
         this.name=name;
    }
    public String getTel(){
         return tel;
    }
    public void setTel(String tel){
         this.tel=tel;
    }
    public String getGroupId(){
         return groupId;
    }
    public void setGroupId(String groupId){
         this.groupId=groupId;
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
        temp = temp + "userId="+userId+"\n";
        temp = temp + "name="+name+"\n";
        temp = temp + "tel="+tel+"\n";
        temp = temp + "groupId="+groupId+"\n";
        temp = temp + "groupName="+groupName+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "createId="+createId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("userId",userId);
        json.put("name",name);
        json.put("tel",tel);
        json.put("groupId",groupId);
        json.put("groupName",groupName);
        json.put("remark",remark);
        json.put("createId",createId);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}