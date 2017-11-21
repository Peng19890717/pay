package com.common.sms.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table SMS_TEMPLATE entity.
 * 
 * @author administrator
 */
public class SmsTemplate {
    public String id;
    public String name;
    public String content;
    public String remark;
    public String modifyId;
    public Date modifyTime;
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
    public String getContent(){
         return content;
    }
    public void setContent(String content){
         this.content=content;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String getModifyId(){
         return modifyId;
    }
    public void setModifyId(String modifyId){
         this.modifyId=modifyId;
    }
    public Date getModifyTime(){
         return modifyTime;
    }
    public void setModifyTime(Date modifyTime){
         this.modifyTime=modifyTime;
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
        temp = temp + "content="+content+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "modifyId="+modifyId+"\n";
        temp = temp + "modifyTime="+modifyTime+"\n";
        temp = temp + "createId="+createId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("name",name);
        json.put("content",content);
        json.put("remark",remark);
        json.put("modifyId",modifyId);
        try{
            json.put("modifyTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(modifyTime));
        } catch (Exception e) {}
        json.put("createId",createId);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}