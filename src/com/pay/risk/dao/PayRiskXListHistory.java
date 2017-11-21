package com.pay.risk.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_RISK_X_LIST_HISTORY entity.
 * 
 * @author administrator
 */
public class PayRiskXListHistory {
    public String id;
    public String clientType;
    public String clientCode;
    public String xType;
    public Date regdtTime;
    public String ruleCode;
    public String casType;
    public String casBuf;
    public String createUser;
    public Date createDatetime;
    public String updateUser;
    public Date updateDatetime;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getClientType(){
         return clientType;
    }
    public void setClientType(String clientType){
         this.clientType=clientType;
    }
    public String getClientCode(){
         return clientCode;
    }
    public void setClientCode(String clientCode){
         this.clientCode=clientCode;
    }
    public String getXType(){
         return xType;
    }
    public void setXType(String xType){
         this.xType=xType;
    }
    public Date getRegdtTime(){
         return regdtTime;
    }
    public void setRegdtTime(Date regdtTime){
         this.regdtTime=regdtTime;
    }
    public String getRuleCode(){
         return ruleCode;
    }
    public void setRuleCode(String ruleCode){
         this.ruleCode=ruleCode;
    }
    public String getCasType(){
         return casType;
    }
    public void setCasType(String casType){
         this.casType=casType;
    }
    public String getCasBuf(){
         return casBuf;
    }
    public void setCasBuf(String casBuf){
         this.casBuf=casBuf;
    }
    public String getCreateUser(){
         return createUser;
    }
    public void setCreateUser(String createUser){
         this.createUser=createUser;
    }
    public Date getCreateDatetime(){
         return createDatetime;
    }
    public void setCreateDatetime(Date createDatetime){
         this.createDatetime=createDatetime;
    }
    public String getUpdateUser(){
         return updateUser;
    }
    public void setUpdateUser(String updateUser){
         this.updateUser=updateUser;
    }
    public Date getUpdateDatetime(){
         return updateDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime){
         this.updateDatetime=updateDatetime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "clientType="+clientType+"\n";
        temp = temp + "clientCode="+clientCode+"\n";
        temp = temp + "xType="+xType+"\n";
        temp = temp + "regdtTime="+regdtTime+"\n";
        temp = temp + "ruleCode="+ruleCode+"\n";
        temp = temp + "casType="+casType+"\n";
        temp = temp + "casBuf="+casBuf+"\n";
        temp = temp + "createUser="+createUser+"\n";
        temp = temp + "createDatetime="+createDatetime+"\n";
        temp = temp + "updateUser="+updateUser+"\n";
        temp = temp + "updateDatetime="+updateDatetime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("clientType",clientType);
        json.put("clientCode",clientCode);
        json.put("xType",xType);
        try{
            json.put("regdtTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(regdtTime));
        } catch (Exception e) {}
        json.put("ruleCode",ruleCode);
        json.put("casType",casType);
        json.put("casBuf",casBuf);
        json.put("createUser",createUser);
        try{
            json.put("createDatetime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createDatetime));
        } catch (Exception e) {}
        json.put("updateUser",updateUser);
        try{
            json.put("updateDatetime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateDatetime));
        } catch (Exception e) {}
        return json;
    }
}