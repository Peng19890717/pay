package com.pay.biztype.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_BIZ_TYPE entity.
 * 
 * @author administrator
 */
public class PayBizType {
    public String seqNo;
    public String code;
    public String name;
    public String isActive;
    public String isRealname;
    public String remark;
    public Date creTime;
    public String creOperId;
    public Date uptTime;
    public String uptOperId;
    public String getSeqNo(){
         return seqNo;
    }
    public void setSeqNo(String seqNo){
         this.seqNo=seqNo;
    }
    public String getCode(){
         return code;
    }
    public void setCode(String code){
         this.code=code;
    }
    public String getName(){
         return name;
    }
    public void setName(String name){
         this.name=name;
    }
    public String getIsActive(){
         return isActive;
    }
    public void setIsActive(String isActive){
         this.isActive=isActive;
    }
    public String getIsRealname(){
         return isRealname;
    }
    public void setIsRealname(String isRealname){
         this.isRealname=isRealname;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public Date getCreTime(){
         return creTime;
    }
    public void setCreTime(Date creTime){
         this.creTime=creTime;
    }
    public String getCreOperId(){
         return creOperId;
    }
    public void setCreOperId(String creOperId){
         this.creOperId=creOperId;
    }
    public Date getUptTime(){
         return uptTime;
    }
    public void setUptTime(Date uptTime){
         this.uptTime=uptTime;
    }
    public String getUptOperId(){
         return uptOperId;
    }
    public void setUptOperId(String uptOperId){
         this.uptOperId=uptOperId;
    }
    public String toString(){
        String temp = "";
        temp = temp + "seqNo="+seqNo+"\n";
        temp = temp + "code="+code+"\n";
        temp = temp + "name="+name+"\n";
        temp = temp + "isActive="+isActive+"\n";
        temp = temp + "isRealname="+isRealname+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "creTime="+creTime+"\n";
        temp = temp + "creOperId="+creOperId+"\n";
        temp = temp + "uptTime="+uptTime+"\n";
        temp = temp + "uptOperId="+uptOperId+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("seqNo",seqNo);
        json.put("code",code);
        json.put("name",name);
        json.put("isActive",isActive);
        json.put("isRealname",isRealname);
        json.put("remark",remark);
        try{
            json.put("creTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(creTime));
        } catch (Exception e) {}
        json.put("creOperId",creOperId);
        try{
            json.put("uptTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(uptTime));
        } catch (Exception e) {}
        json.put("uptOperId",uptOperId);
        return json;
    }
}