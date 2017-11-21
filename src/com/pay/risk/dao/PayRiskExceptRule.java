package com.pay.risk.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_RISK_EXCEPT_RULE entity.
 * 
 * @author administrator
 */
public class PayRiskExceptRule {
    public String ruleCode;
    public String ruleName;
    public String ruleType;
    public String isOnline;
    public String excpType;
    public String comTypeNo;
    public String ruleDes;
    public String ruleLevel;
    public String ruleLevelItem;
    public Date ruleStartDate;
    public Date ruleEndDate;
    public String createName;
    public Date createTime;
    public String updateName;
    public Date updateTime;
    public String ruleVer = "1";
    public String isUse = "0";
    public String belongCompany;
    public List<PayRiskExceptRuleParam> paramList = new ArrayList<PayRiskExceptRuleParam>();
    public String getRuleCode(){
         return ruleCode;
    }
    public void setRuleCode(String ruleCode){
         this.ruleCode=ruleCode;
    }
    public String getRuleName(){
         return ruleName;
    }
    public void setRuleName(String ruleName){
         this.ruleName=ruleName;
    }
    public String getRuleType(){
         return ruleType;
    }
    public void setRuleType(String ruleType){
         this.ruleType=ruleType;
    }
    public String getIsOnline(){
         return isOnline;
    }
    public void setIsOnline(String isOnline){
         this.isOnline=isOnline;
    }
    public String getExcpType(){
         return excpType;
    }
    public void setExcpType(String excpType){
         this.excpType=excpType;
    }
    public String getComTypeNo(){
         return comTypeNo;
    }
    public void setComTypeNo(String comTypeNo){
         this.comTypeNo=comTypeNo;
    }
    public String getRuleDes(){
         return ruleDes;
    }
    public void setRuleDes(String ruleDes){
         this.ruleDes=ruleDes;
    }
    public String getRuleLevel(){
         return ruleLevel;
    }
    public void setRuleLevel(String ruleLevel){
         this.ruleLevel=ruleLevel;
    }
    public String getRuleLevelItem(){
         return ruleLevelItem;
    }
    public void setRuleLevelItem(String ruleLevelItem){
         this.ruleLevelItem=ruleLevelItem;
    }
    public Date getRuleStartDate(){
         return ruleStartDate;
    }
    public void setRuleStartDate(Date ruleStartDate){
         this.ruleStartDate=ruleStartDate;
    }
    public Date getRuleEndDate(){
         return ruleEndDate;
    }
    public void setRuleEndDate(Date ruleEndDate){
         this.ruleEndDate=ruleEndDate;
    }
    public String getCreateName(){
         return createName;
    }
    public void setCreateName(String createName){
         this.createName=createName;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String getUpdateName(){
         return updateName;
    }
    public void setUpdateName(String updateName){
         this.updateName=updateName;
    }
    public Date getUpdateTime(){
         return updateTime;
    }
    public void setUpdateTime(Date updateTime){
         this.updateTime=updateTime;
    }
    public String getRuleVer(){
         return ruleVer;
    }
    public void setRuleVer(String ruleVer){
         this.ruleVer=ruleVer;
    }
    public String getIsUse(){
         return isUse;
    }
    public void setIsUse(String isUse){
         this.isUse=isUse;
    }
    public String getBelongCompany(){
         return belongCompany;
    }
    public void setBelongCompany(String belongCompany){
         this.belongCompany=belongCompany;
    }
    public String toString(){
        String temp = "";
        temp = temp + "ruleCode="+ruleCode+"\n";
        temp = temp + "ruleName="+ruleName+"\n";
        temp = temp + "ruleType="+ruleType+"\n";
        temp = temp + "isOnline="+isOnline+"\n";
        temp = temp + "excpType="+excpType+"\n";
        temp = temp + "comTypeNo="+comTypeNo+"\n";
        temp = temp + "ruleDes="+ruleDes+"\n";
        temp = temp + "ruleLevel="+ruleLevel+"\n";
        temp = temp + "ruleLevelItem="+ruleLevelItem+"\n";
        temp = temp + "ruleStartDate="+ruleStartDate+"\n";
        temp = temp + "ruleEndDate="+ruleEndDate+"\n";
        temp = temp + "createName="+createName+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "updateName="+updateName+"\n";
        temp = temp + "updateTime="+updateTime+"\n";
        temp = temp + "ruleVer="+ruleVer+"\n";
        temp = temp + "isUse="+isUse+"\n";
        temp = temp + "belongCompany="+belongCompany+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("ruleCode",ruleCode);
        json.put("ruleName",ruleName);
        json.put("ruleType",ruleType);
        json.put("isOnline",isOnline);
        json.put("excpType",excpType);
        json.put("comTypeNo",comTypeNo);
        json.put("ruleDes",ruleDes);
        json.put("ruleLevel",ruleLevel);
        json.put("ruleLevelItem",ruleLevelItem);
        try{
            json.put("ruleStartDate", new java.text.SimpleDateFormat("yyyy-MM-dd").format(ruleStartDate));
        } catch (Exception e) {}
        try{
            json.put("ruleEndDate", new java.text.SimpleDateFormat("yyyy-MM-dd").format(ruleEndDate));
        } catch (Exception e) {}
        json.put("createName",createName);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(createTime));
        } catch (Exception e) {}
        json.put("updateName",updateName);
        try{
            json.put("updateTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(updateTime));
        } catch (Exception e) {}
        json.put("ruleVer",ruleVer);
        json.put("isUse",isUse);
        json.put("belongCompany",belongCompany);
        return json;
    }
}