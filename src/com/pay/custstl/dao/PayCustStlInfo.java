package com.pay.custstl.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_CUST_STL_INFO entity.
 * 
 * @author administrator
 */
public class PayCustStlInfo {
    public String seqNo;
    public String payAcNo;
    public String custId;
    public String custBankDepositName;
    public String depositBankCode;
    public String depositBankBrchName;
    public String custStlBankAcNo;
    public String custStlType;
    public String custSetPeriod;
    public String custSetFrey;
    public String custSetFreyAgent;
    public String custSetPeriodAgent;
    public String custStlTimeSetAgent;
    public String custStlTimeSet;
    public Long minStlBalance;
    public String creOperId;
    public Date creTime;
    public String lstUptOperId;
    public Date lstUptTime;
    public String custSetDay;
    public String custSetPeriodDaishou;
    public String custStlTimeSetDaishou;
    public String custStlBankProvince;
    public String custStlBankCity;
    public String custStlIdno;
    public String custStlMobileno;
    public String custStlBankNumber;
    
    public String getCustStlBankNumber() {
		return custStlBankNumber;
	}
	public void setCustStlBankNumber(String custStlBankNumber) {
		this.custStlBankNumber = custStlBankNumber;
	}
	public String getCustStlBankProvince() {
		return custStlBankProvince;
	}
	public void setCustStlBankProvince(String custStlBankProvince) {
		this.custStlBankProvince = custStlBankProvince;
	}
	public String getCustStlBankCity() {
		return custStlBankCity;
	}
	public void setCustStlBankCity(String custStlBankCity) {
		this.custStlBankCity = custStlBankCity;
	}
	public String getCustStlIdno() {
		return custStlIdno;
	}
	public void setCustStlIdno(String custStlIdno) {
		this.custStlIdno = custStlIdno;
	}
	public String getCustStlMobileno() {
		return custStlMobileno;
	}
	public void setCustStlMobileno(String custStlMobileno) {
		this.custStlMobileno = custStlMobileno;
	}
	public void setMinStlBalance(Long minStlBalance) {
		this.minStlBalance = minStlBalance;
	}
	public String getCustSetPeriodDaishou() {
		return custSetPeriodDaishou;
	}
	public void setCustSetPeriodDaishou(String custSetPeriodDaishou) {
		this.custSetPeriodDaishou = custSetPeriodDaishou;
	}
	public String getCustStlTimeSetDaishou() {
		return custStlTimeSetDaishou;
	}
	public void setCustStlTimeSetDaishou(String custStlTimeSetDaishou) {
		this.custStlTimeSetDaishou = custStlTimeSetDaishou;
	}
	public String getCustSetFreyAgent() {
		return custSetFreyAgent;
	}
	public void setCustSetFreyAgent(String custSetFreyAgent) {
		this.custSetFreyAgent = custSetFreyAgent;
	}
	public String getCustSetPeriodAgent() {
		return custSetPeriodAgent;
	}
	public void setCustSetPeriodAgent(String custSetPeriodAgent) {
		this.custSetPeriodAgent = custSetPeriodAgent;
	}
	public String getCustStlTimeSetAgent() {
		return custStlTimeSetAgent;
	}
	public void setCustStlTimeSetAgent(String custStlTimeSetAgent) {
		this.custStlTimeSetAgent = custStlTimeSetAgent;
	}
	public String getSeqNo(){
         return seqNo;
    }
    public void setSeqNo(String seqNo){
         this.seqNo=seqNo;
    }
    public String getPayAcNo(){
         return payAcNo;
    }
    public void setPayAcNo(String payAcNo){
         this.payAcNo=payAcNo;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getCustBankDepositName(){
         return custBankDepositName;
    }
    public void setCustBankDepositName(String custBankDepositName){
         this.custBankDepositName=custBankDepositName;
    }
    public String getDepositBankCode(){
         return depositBankCode;
    }
    public void setDepositBankCode(String depositBankCode){
         this.depositBankCode=depositBankCode;
    }
    public String getDepositBankBrchName(){
         return depositBankBrchName;
    }
    public void setDepositBankBrchName(String depositBankBrchName){
         this.depositBankBrchName=depositBankBrchName;
    }
    public String getCustStlBankAcNo(){
         return custStlBankAcNo;
    }
    public void setCustStlBankAcNo(String custStlBankAcNo){
         this.custStlBankAcNo=custStlBankAcNo;
    }
    public String getCustStlType(){
         return custStlType;
    }
    public void setCustStlType(String custStlType){
         this.custStlType=custStlType;
    }
    public String getCustSetPeriod(){
         return custSetPeriod;
    }
    public void setCustSetPeriod(String custSetPeriod){
         this.custSetPeriod=custSetPeriod;
    }
    public String getCustSetFrey(){
         return custSetFrey;
    }
    public void setCustSetFrey(String custSetFrey){
         this.custSetFrey=custSetFrey;
    }
    public String getCustStlTimeSet(){
         return custStlTimeSet;
    }
    public void setCustStlTimeSet(String custStlTimeSet){
         this.custStlTimeSet=custStlTimeSet;
    }
    public long getMinStlBalance(){
         return minStlBalance;
    }
    public void setMinStlBalance(long minStlBalance){
         this.minStlBalance=minStlBalance;
    }
    public String getCreOperId(){
         return creOperId;
    }
    public void setCreOperId(String creOperId){
         this.creOperId=creOperId;
    }
    public Date getCreTime(){
         return creTime;
    }
    public void setCreTime(Date creTime){
         this.creTime=creTime;
    }
    public String getLstUptOperId(){
         return lstUptOperId;
    }
    public void setLstUptOperId(String lstUptOperId){
         this.lstUptOperId=lstUptOperId;
    }
    public Date getLstUptTime(){
         return lstUptTime;
    }
    public void setLstUptTime(Date lstUptTime){
         this.lstUptTime=lstUptTime;
    }
    public String getCustSetDay(){
         return custSetDay;
    }
    public void setCustSetDay(String custSetDay){
         this.custSetDay=custSetDay;
    }
    public String toString(){
        String temp = "";
        temp = temp + "seqNo="+seqNo+"\n";
        temp = temp + "payAcNo="+payAcNo+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "custBankDepositName="+custBankDepositName+"\n";
        temp = temp + "depositBankCode="+depositBankCode+"\n";
        temp = temp + "depositBankBrchName="+depositBankBrchName+"\n";
        temp = temp + "custStlBankAcNo="+custStlBankAcNo+"\n";
        temp = temp + "custStlType="+custStlType+"\n";
        temp = temp + "custSetPeriod="+custSetPeriod+"\n";
        temp = temp + "custSetFrey="+custSetFrey+"\n";
        temp = temp + "custStlTimeSet="+custStlTimeSet+"\n";
        temp = temp + "minStlBalance="+minStlBalance+"\n";
        temp = temp + "creOperId="+creOperId+"\n";
        temp = temp + "creTime="+creTime+"\n";
        temp = temp + "lstUptOperId="+lstUptOperId+"\n";
        temp = temp + "lstUptTime="+lstUptTime+"\n";
        temp = temp + "custSetDay="+custSetDay+"\n";
        temp = temp + "custSetPeriodAgent="+custSetPeriodAgent+"\n";
        temp = temp + "custStlTimeSetAgent="+custStlTimeSetAgent+"\n";
        temp = temp + "custSetFreyAgent="+custSetFreyAgent+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("seqNo",seqNo);
        json.put("payAcNo",payAcNo);
        json.put("custId",custId);
        json.put("custBankDepositName",custBankDepositName);
        json.put("depositBankCode",depositBankCode);
        json.put("depositBankBrchName",depositBankBrchName);
        json.put("custStlBankAcNo",custStlBankAcNo);
        json.put("custStlType",custStlType);
        json.put("custSetPeriod",custSetPeriod);
        json.put("custSetFrey",custSetFrey);
        json.put("custStlTimeSet",custStlTimeSet);
        json.put("minStlBalance",String.valueOf(minStlBalance));
        json.put("creOperId",creOperId);
        try{
            json.put("creTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(creTime));
        } catch (Exception e) {}
        json.put("lstUptOperId",lstUptOperId);
        try{
            json.put("lstUptTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lstUptTime));
        } catch (Exception e) {}
        json.put("custSetDay",custSetDay);
        json.put("custSetPeriodAgent",custSetPeriodAgent);
        json.put("custStlTimeSetAgent",custStlTimeSetAgent);
        json.put("custSetFreyAgent",custSetFreyAgent);
        json.put("custSetPeriodDaishou",custSetPeriodDaishou);
        json.put("custStlTimeSetDaishou",custStlTimeSetDaishou);
        return json;
    }
}