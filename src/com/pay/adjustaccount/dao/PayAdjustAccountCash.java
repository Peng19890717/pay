package com.pay.adjustaccount.dao;

import java.math.BigDecimal;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_ADJUST_ACCOUNT_CASH entity.
 * 
 * @author administrator
 */
public class PayAdjustAccountCash {
    public String id;
    public String custId;
    public String acType;
    public String ccy;
    public Long amt;
    public String adjustType;
    public String applyUser;
    public String applyIp;
    public Date applyTime;
    public String checkUser;
    public String checkIp;
    public Date checkTime;
    public String status;
    public String remark;
    public String adjustBussType;
    public String storeName;
    
    public Long cashAcBal;
    
    public String applyName;
    
    public String checkName;
    
    
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public void setCashAcBal(Long cashAcBal) {
		this.cashAcBal = cashAcBal;
	}
	public Long getCashAcBal() {
		return cashAcBal;
	}
	public void setCashAcBal(String Long) {
		this.cashAcBal = cashAcBal;
	}
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getAcType(){
         return acType;
    }
    public void setAcType(String acType){
         this.acType=acType;
    }
    public String getCcy(){
         return ccy;
    }
    public void setCcy(String ccy){
         this.ccy=ccy;
    }
    public Long getAmt(){
         return amt;
    }
    public void setAmt(Long amt){
         this.amt=amt;
    }
    public String getAdjustType(){
         return adjustType;
    }
    public void setAdjustType(String adjustType){
         this.adjustType=adjustType;
    }
    public String getApplyUser(){
         return applyUser;
    }
    public void setApplyUser(String applyUser){
         this.applyUser=applyUser;
    }
    public String getApplyIp(){
         return applyIp;
    }
    public void setApplyIp(String applyIp){
         this.applyIp=applyIp;
    }
    public Date getApplyTime(){
         return applyTime;
    }
    public void setApplyTime(Date applyTime){
         this.applyTime=applyTime;
    }
    public String getCheckUser(){
         return checkUser;
    }
    public void setCheckUser(String checkUser){
         this.checkUser=checkUser;
    }
    public String getCheckIp(){
         return checkIp;
    }
    public void setCheckIp(String checkIp){
         this.checkIp=checkIp;
    }
    public Date getCheckTime(){
         return checkTime;
    }
    public void setCheckTime(Date checkTime){
         this.checkTime=checkTime;
    }
    public String getStatus(){
         return status;
    }
    public void setStatus(String status){
         this.status=status;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String getAdjustBussType() {
		return adjustBussType;
	}
	public void setAdjustBussType(String adjustBussType) {
		this.adjustBussType = adjustBussType;
	}
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "acType="+acType+"\n";
        temp = temp + "ccy="+ccy+"\n";
        temp = temp + "amt="+amt+"\n";
        temp = temp + "adjustType="+adjustType+"\n";
        temp = temp + "applyUser="+applyUser+"\n";
        temp = temp + "applyIp="+applyIp+"\n";
        temp = temp + "applyTime="+applyTime+"\n";
        temp = temp + "checkUser="+checkUser+"\n";
        temp = temp + "checkIp="+checkIp+"\n";
        temp = temp + "checkTime="+checkTime+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "remark="+remark+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("custId",custId);
        json.put("acType",acType);
        json.put("ccy",ccy);
        json.put("amt",new BigDecimal(amt).divide(new BigDecimal(100)));
        json.put("adjustType",adjustType);
        json.put("applyUser",applyUser);
        json.put("applyIp",applyIp);
        try{
            json.put("applyTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(applyTime));
        } catch (Exception e) {}
        json.put("checkUser",checkUser);
        json.put("checkIp",checkIp);
        try{
            json.put("checkTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(checkTime));
        } catch (Exception e) {}
        json.put("status",status);
        json.put("remark",remark);
        json.put("cashAcBal",String.format("%.2f",(float)cashAcBal*0.01));
        json.put("applyName",applyName);
        json.put("checkName",checkName);
        json.put("adjustBussType",adjustBussType);
        json.put("storeName",storeName);
        return json;
    }
}