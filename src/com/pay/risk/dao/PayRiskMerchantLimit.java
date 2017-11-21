package com.pay.risk.dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
/**
 * Table PAY_RISK_MERCHANT_LIMIT entity.
 * 
 * @author administrator
 */
public class PayRiskMerchantLimit {
    public String id;
    public String limitType;
    public String limitCompCode;
    public String limitCompType;
    public String limitBusCode;
    public String limitBusClient;
    public Long limitMinAmt;
    public Long limitMaxAmt;
    public Long limitDayTimes;
    public Long limitDaySuccessTimes;
    public Long limitDayAmt;
    public Long limitDaySuccessAmt;
    public Long limitMonthTimes;
    public Long limitMonthSuccessTimes;
    public Long limitMonthAmt;
    public Long limitMonthSuccessAmt;
    public Long limitYearTimes=0l;
    public Long limitYearSuccessTimes=0l;
    public Long limitYearAmt=0l;
    public Long limitYearSuccessAmt=0l;
    public Date limitStartDate;
    public Date limitEndDate;
    public String limitAddinfo;
    public String isUse;
    public String compCode;
    public String compName;
    public String createUser;
    public Date createTime;
    public String updateUser;
    public Date updateTime;
    public String limitTimeFlag;
    public String limitRiskLevel;
    
    // 创建时间范围结束点
    public Date createTimeEnd;
    
    //商户名字
    public String storeName;
    
    
    public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getLimitType(){
         return limitType;
    }
    public void setLimitType(String limitType){
         this.limitType=limitType;
    }
    public String getLimitCompCode(){
         return limitCompCode;
    }
    public void setLimitCompCode(String limitCompCode){
         this.limitCompCode=limitCompCode;
    }
    public String getLimitCompType(){
         return limitCompType;
    }
    public void setLimitCompType(String limitCompType){
         this.limitCompType=limitCompType;
    }
    public String getLimitBusCode(){
         return limitBusCode;
    }
    public void setLimitBusCode(String limitBusCode){
         this.limitBusCode=limitBusCode;
    }
    public String getLimitBusClient(){
         return limitBusClient;
    }
    public void setLimitBusClient(String limitBusClient){
         this.limitBusClient=limitBusClient;
    }
    public Long getLimitMinAmt(){
         return limitMinAmt;
    }
    public void setLimitMinAmt(Long limitMinAmt){
         this.limitMinAmt=limitMinAmt;
    }
    public Long getLimitMaxAmt(){
         return limitMaxAmt;
    }
    public void setLimitMaxAmt(Long limitMaxAmt){
         this.limitMaxAmt=limitMaxAmt;
    }
    public Long getLimitDayTimes(){
         return limitDayTimes;
    }
    public void setLimitDayTimes(Long limitDayTimes){
         this.limitDayTimes=limitDayTimes;
    }
    public Long getLimitDaySuccessTimes(){
         return limitDaySuccessTimes;
    }
    public void setLimitDaySuccessTimes(Long limitDaySuccessTimes){
         this.limitDaySuccessTimes=limitDaySuccessTimes;
    }
    public Long getLimitDayAmt(){
         return limitDayAmt;
    }
    public void setLimitDayAmt(Long limitDayAmt){
         this.limitDayAmt=limitDayAmt;
    }
    public Long getLimitDaySuccessAmt(){
         return limitDaySuccessAmt;
    }
    public void setLimitDaySuccessAmt(Long limitDaySuccessAmt){
         this.limitDaySuccessAmt=limitDaySuccessAmt;
    }
    public Long getLimitMonthTimes(){
         return limitMonthTimes;
    }
    public void setLimitMonthTimes(Long limitMonthTimes){
         this.limitMonthTimes=limitMonthTimes;
    }
    public Long getLimitMonthSuccessTimes(){
         return limitMonthSuccessTimes;
    }
    public void setLimitMonthSuccessTimes(Long limitMonthSuccessTimes){
         this.limitMonthSuccessTimes=limitMonthSuccessTimes;
    }
    public Long getLimitMonthAmt(){
         return limitMonthAmt;
    }
    public void setLimitMonthAmt(Long limitMonthAmt){
         this.limitMonthAmt=limitMonthAmt;
    }
    public Long getLimitMonthSuccessAmt(){
         return limitMonthSuccessAmt;
    }
    public void setLimitMonthSuccessAmt(Long limitMonthSuccessAmt){
         this.limitMonthSuccessAmt=limitMonthSuccessAmt;
    }
    public Long getLimitYearTimes(){
         return limitYearTimes;
    }
    public void setLimitYearTimes(Long limitYearTimes){
         this.limitYearTimes=limitYearTimes;
    }
    public Long getLimitYearSuccessTimes(){
         return limitYearSuccessTimes;
    }
    public void setLimitYearSuccessTimes(Long limitYearSuccessTimes){
         this.limitYearSuccessTimes=limitYearSuccessTimes;
    }
    public Long getLimitYearAmt(){
         return limitYearAmt;
    }
    public void setLimitYearAmt(Long limitYearAmt){
         this.limitYearAmt=limitYearAmt;
    }
    public Long getLimitYearSuccessAmt(){
         return limitYearSuccessAmt;
    }
    public void setLimitYearSuccessAmt(Long limitYearSuccessAmt){
         this.limitYearSuccessAmt=limitYearSuccessAmt;
    }
    public Date getLimitStartDate(){
         return limitStartDate;
    }
    public void setLimitStartDate(Date limitStartDate){
         this.limitStartDate=limitStartDate;
    }
    public Date getLimitEndDate(){
         return limitEndDate;
    }
    public void setLimitEndDate(Date limitEndDate){
         this.limitEndDate=limitEndDate;
    }
    public String getLimitAddinfo(){
         return limitAddinfo;
    }
    public void setLimitAddinfo(String limitAddinfo){
         this.limitAddinfo=limitAddinfo;
    }
    public String getIsUse(){
         return isUse;
    }
    public void setIsUse(String isUse){
         this.isUse=isUse;
    }
    public String getCompCode(){
         return compCode;
    }
    public void setCompCode(String compCode){
         this.compCode=compCode;
    }
    public String getCompName(){
         return compName;
    }
    public void setCompName(String compName){
         this.compName=compName;
    }
    public String getCreateUser(){
         return createUser;
    }
    public void setCreateUser(String createUser){
         this.createUser=createUser;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String getUpdateUser(){
         return updateUser;
    }
    public void setUpdateUser(String updateUser){
         this.updateUser=updateUser;
    }
    public Date getUpdateTime(){
         return updateTime;
    }
    public void setUpdateTime(Date updateTime){
         this.updateTime=updateTime;
    }
    public String getLimitTimeFlag(){
        return limitTimeFlag;
   }
   public void setLimitTimeFlag(String limitTimeFlag){
        this.limitTimeFlag=limitTimeFlag;
   }
   public String getLimitRiskLevel(){
       return limitRiskLevel;
  }
  public void setLimitRiskLevel(String limitRiskLevel){
       this.limitRiskLevel=limitRiskLevel;
  }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "limitType="+limitType+"\n";
        temp = temp + "limitCompCode="+limitCompCode+"\n";
        temp = temp + "limitCompType="+limitCompType+"\n";
        temp = temp + "limitBusCode="+limitBusCode+"\n";
        temp = temp + "limitBusClient="+limitBusClient+"\n";
        temp = temp + "limitMinAmt="+limitMinAmt+"\n";
        temp = temp + "limitMaxAmt="+limitMaxAmt+"\n";
        temp = temp + "limitDayTimes="+limitDayTimes+"\n";
        temp = temp + "limitDaySuccessTimes="+limitDaySuccessTimes+"\n";
        temp = temp + "limitDayAmt="+limitDayAmt+"\n";
        temp = temp + "limitDaySuccessAmt="+limitDaySuccessAmt+"\n";
        temp = temp + "limitMonthTimes="+limitMonthTimes+"\n";
        temp = temp + "limitMonthSuccessTimes="+limitMonthSuccessTimes+"\n";
        temp = temp + "limitMonthAmt="+limitMonthAmt+"\n";
        temp = temp + "limitMonthSuccessAmt="+limitMonthSuccessAmt+"\n";
        temp = temp + "limitYearTimes="+limitYearTimes+"\n";
        temp = temp + "limitYearSuccessTimes="+limitYearSuccessTimes+"\n";
        temp = temp + "limitYearAmt="+limitYearAmt+"\n";
        temp = temp + "limitYearSuccessAmt="+limitYearSuccessAmt+"\n";
        temp = temp + "limitStartDate="+limitStartDate+"\n";
        temp = temp + "limitEndDate="+limitEndDate+"\n";
        temp = temp + "limitAddinfo="+limitAddinfo+"\n";
        temp = temp + "isUse="+isUse+"\n";
        temp = temp + "compCode="+compCode+"\n";
        temp = temp + "compName="+compName+"\n";
        temp = temp + "createUser="+createUser+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "createTimeEnd="+createTimeEnd+"\n";
        temp = temp + "updateUser="+updateUser+"\n";
        temp = temp + "updateTime="+updateTime+"\n";
        temp = temp + "limitTimeFlag="+limitTimeFlag+"\n";
        temp = temp + "limitRiskLevel="+limitRiskLevel+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("limitType",limitType);
        json.put("limitCompCode","-1".equals(limitCompCode) ? "所有商户" : limitCompCode);
        json.put("limitCompType",limitCompType);
        json.put("limitBusCode",limitBusCode);
        json.put("limitBusClient",limitBusClient);
        
        json.put("limitMinAmt",String.format("%.2f",(float)limitMinAmt*0.01));
        json.put("limitMaxAmt",String.format("%.2f",(float)limitMaxAmt*0.01));
        json.put("limitDayTimes",String.valueOf(limitDayTimes));
        json.put("limitDaySuccessTimes",String.valueOf(limitDaySuccessTimes));
        json.put("limitDayAmt",String.format("%.2f",(float)limitDayAmt*0.01));
        json.put("limitDaySuccessAmt",String.format("%.2f",(float)limitDaySuccessAmt*0.01));
        json.put("limitMonthTimes",String.valueOf(limitMonthTimes));
        json.put("limitMonthSuccessTimes",String.valueOf(limitMonthSuccessTimes));
        json.put("limitMonthAmt",String.format("%.2f",(float)limitMonthAmt*0.01));
        json.put("limitMonthSuccessAmt",String.format("%.2f",(float)limitMonthSuccessAmt*0.01));
        json.put("limitYearTimes",String.valueOf(limitYearTimes));
        json.put("limitYearSuccessTimes",String.valueOf(limitYearSuccessTimes));
        json.put("limitYearAmt",String.valueOf(limitYearAmt));
        json.put("limitYearSuccessAmt",String.valueOf(limitYearSuccessAmt));
        try{
            json.put("limitStartDate", "1".equals(limitTimeFlag) ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(limitStartDate) : "永久");
        } catch (Exception e) {}
        try{
            json.put("limitEndDate", "1".equals(limitTimeFlag) ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(limitEndDate) : "永久");
        } catch (Exception e) {}
        json.put("limitAddinfo",limitAddinfo);
        json.put("isUse",isUse);
        json.put("compCode",compCode);
        json.put("compName",compName);
        json.put("createUser",createUser);
        json.put("storeName",storeName);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
            json.put("createTimeEnd", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTimeEnd));
        } catch (Exception e) {}
        json.put("updateUser",updateUser);
        try{
            json.put("updateTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime));
        } catch (Exception e) {}
        json.put("limitTimeFlag",limitTimeFlag);
        json.put("limitRiskLevel","2".equals(limitType) ? "000" : limitRiskLevel);
        return json;
    }
}