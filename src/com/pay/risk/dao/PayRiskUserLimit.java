package com.pay.risk.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_RISK_USER_LIMIT entity.
 * 
 * @author administrator
 */
public class PayRiskUserLimit {
    public String id;
    public String userType;
    public String xListType;
    public String tranType;
    public String userCode;
    public String busCode;
    public Date startDate;
    public Date endDate;
    public String isUse;
    public Long maxCardNum;
    public Long crebitCardOnceAmt=0l;
    public Long crebitCardDayAmt=0l;
    public Long crebitCardTxnInterval=0l;
    public Long debitCardOnceAmt=0l;
    public Long debitCardDayAmt=0l;
    public Long debitCardTxnInterval=0l;
    public String remark;
    public String createUserId;
    public Date createTime;
    public String updateUserId;
    public Date updateTime;
    public String limitTimeFlag;
    public List<PayRiskUserLimitSub> subLimitList = new ArrayList<PayRiskUserLimitSub>();
    
    public List<PayRiskUserLimitSub> getSubLimitList() {
		return subLimitList;
	}
	public void setSubLimitList(List<PayRiskUserLimitSub> subLimitList) {
		this.subLimitList = subLimitList;
	}
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getUserType(){
         return userType;
    }
    public void setUserType(String userType){
         this.userType=userType;
    }
    public String getXListType(){
         return xListType;
    }
    public void setXListType(String xListType){
         this.xListType=xListType;
    }
    public String getTranType(){
         return tranType;
    }
    public void setTranType(String tranType){
         this.tranType=tranType;
    }
    public String getUserCode(){
         return userCode;
    }
    public void setUserCode(String userCode){
         this.userCode=userCode;
    }
    public String getBusCode(){
         return busCode;
    }
    public void setBusCode(String busCode){
         this.busCode=busCode;
    }
    public Date getStartDate(){
         return startDate;
    }
    public void setStartDate(Date startDate){
         this.startDate=startDate;
    }
    public Date getEndDate(){
         return endDate;
    }
    public void setEndDate(Date endDate){
         this.endDate=endDate;
    }
    public String getIsUse(){
         return isUse;
    }
    public void setIsUse(String isUse){
         this.isUse=isUse;
    }
    public Long getMaxCardNum(){
         return maxCardNum;
    }
    public void setMaxCardNum(Long maxCardNum){
         this.maxCardNum=maxCardNum;
    }
    public Long getCrebitCardOnceAmt(){
         return crebitCardOnceAmt;
    }
    public void setCrebitCardOnceAmt(Long crebitCardOnceAmt){
         this.crebitCardOnceAmt=crebitCardOnceAmt;
    }
    public Long getCrebitCardDayAmt(){
         return crebitCardDayAmt;
    }
    public void setCrebitCardDayAmt(Long crebitCardDayAmt){
         this.crebitCardDayAmt=crebitCardDayAmt;
    }
    public Long getCrebitCardTxnInterval(){
         return crebitCardTxnInterval;
    }
    public void setCrebitCardTxnInterval(Long crebitCardTxnInterval){
         this.crebitCardTxnInterval=crebitCardTxnInterval;
    }
    public Long getDebitCardOnceAmt(){
         return debitCardOnceAmt;
    }
    public void setDebitCardOnceAmt(Long debitCardOnceAmt){
         this.debitCardOnceAmt=debitCardOnceAmt;
    }
    public Long getDebitCardDayAmt(){
         return debitCardDayAmt;
    }
    public void setDebitCardDayAmt(Long debitCardDayAmt){
         this.debitCardDayAmt=debitCardDayAmt;
    }
    public Long getDebitCardTxnInterval(){
         return debitCardTxnInterval;
    }
    public void setDebitCardTxnInterval(Long debitCardTxnInterval){
         this.debitCardTxnInterval=debitCardTxnInterval;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String getCreateUserId(){
         return createUserId;
    }
    public void setCreateUserId(String createUserId){
         this.createUserId=createUserId;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String getUpdateUserId(){
         return updateUserId;
    }
    public void setUpdateUserId(String updateUserId){
         this.updateUserId=updateUserId;
    }
    public Date getUpdateTime(){
         return updateTime;
    }
    public void setUpdateTime(Date updateTime){
         this.updateTime=updateTime;
    }
    
    public String getxListType() {
		return xListType;
	}
	public void setxListType(String xListType) {
		this.xListType = xListType;
	}
	public String getLimitTimeFlag() {
		return limitTimeFlag;
	}
	public void setLimitTimeFlag(String limitTimeFlag) {
		this.limitTimeFlag = limitTimeFlag;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "userType="+userType+"\n";
        temp = temp + "xListType="+xListType+"\n";
        temp = temp + "tranType="+tranType+"\n";
        temp = temp + "userCode="+userCode+"\n";
        temp = temp + "busCode="+busCode+"\n";
        temp = temp + "startDate="+startDate+"\n";
        temp = temp + "endDate="+endDate+"\n";
        temp = temp + "isUse="+isUse+"\n";
        temp = temp + "maxCardNum="+maxCardNum+"\n";
        temp = temp + "crebitCardOnceAmt="+crebitCardOnceAmt+"\n";
        temp = temp + "crebitCardDayAmt="+crebitCardDayAmt+"\n";
        temp = temp + "crebitCardTxnInterval="+crebitCardTxnInterval+"\n";
        temp = temp + "debitCardOnceAmt="+debitCardOnceAmt+"\n";
        temp = temp + "debitCardDayAmt="+debitCardDayAmt+"\n";
        temp = temp + "debitCardTxnInterval="+debitCardTxnInterval+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "createUserId="+createUserId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "updateUserId="+updateUserId+"\n";
        temp = temp + "updateTime="+updateTime+"\n";
        temp = temp + "limitTimeFlag="+limitTimeFlag+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("userType",userType);
        json.put("xListType",xListType);
        json.put("tranType",tranType);
        json.put("userCode",userCode);
        json.put("busCode",busCode);
        try{
            json.put("startDate", "1".equals(limitTimeFlag)?new SimpleDateFormat("yyyy-MM-dd").format(startDate):"");
        } catch (Exception e) {}
        try{
            json.put("endDate", "1".equals(limitTimeFlag)?new SimpleDateFormat("yyyy-MM-dd").format(endDate):"");
        } catch (Exception e) {}
        json.put("isUse",isUse);
        json.put("maxCardNum",String.valueOf(maxCardNum));
        json.put("crebitCardOnceAmt",String.valueOf(crebitCardOnceAmt));
        json.put("crebitCardDayAmt",String.valueOf(crebitCardDayAmt));
        json.put("crebitCardTxnInterval",String.valueOf(crebitCardTxnInterval));
        json.put("debitCardOnceAmt",String.valueOf(debitCardOnceAmt));
        json.put("debitCardDayAmt",String.valueOf(debitCardDayAmt));
        json.put("debitCardTxnInterval",String.valueOf(debitCardTxnInterval));
        json.put("remark",remark);
        json.put("createUserId",createUserId);
        try{
            json.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        json.put("updateUserId",updateUserId);
        try{
            json.put("updateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime));
        } catch (Exception e) {}
        json.put("limitTimeFlag",limitTimeFlag);
        return json;
    }
}