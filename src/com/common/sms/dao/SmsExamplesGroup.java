package com.common.sms.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table SMS_EXAMPLES_GROUP entity.
 * 
 * @author administrator
 */
public class SmsExamplesGroup {
    public String id;
    public String name;
    public String createId;
    public Date createTime;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getName(){
         return name;
    }
    public void setName(String name){
         this.name=name;
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
        temp = temp + "name="+name+"\n";
        temp = temp + "createId="+createId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("name",name);
        json.put("createId",createId);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}