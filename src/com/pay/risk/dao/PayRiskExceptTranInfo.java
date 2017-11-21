package com.pay.risk.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_RISK_EXCEPT_TRAN_INFO entity.
 * 
 * @author administrator
 */
public class PayRiskExceptTranInfo {
    public String id;
    public String tranId;
    public String entityType;
    public String tranSource;
    public String custId;
    public String tranType;
    public String bankCardNo;
    public String bankDepNo;
    public String bankSignVal;
    public Date regdtTime;
    public String warnType;
    public String ruleCode;
    public String exceptTranFlag;
    public String updateUser;
    public Date updateTime;
    public String remark;
    
    // ============start================
    public String storeName; // 客户名称
    public String prdordno; // 商品订单号
    
    /*public Long ordamt; // 商品金额
    public String ordstatus; // 订单状态*/    
    
    public String ruleName; // 规则名称
    public String ruleLevelItem; // 预警项
    // ============end================
    
    // 查询字段新增
    public Date regdtTimeSearchEnd;
    
	public Date getRegdtTimeSearchEnd() {
		return regdtTimeSearchEnd;
	}
	public void setRegdtTimeSearchEnd(Date regdtTimeSearchEnd) {
		this.regdtTimeSearchEnd = regdtTimeSearchEnd;
	}
	
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getTranId(){
         return tranId;
    }
    public void setProductOrderId(String tranId){
         this.tranId=tranId;
    }
    public String getEntityType(){
         return entityType;
    }
    public void setEntityType(String entityType){
         this.entityType=entityType;
    }
    public String getTranSource(){
         return tranSource;
    }
    public void setTranSource(String tranSource){
         this.tranSource=tranSource;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getTranType(){
         return tranType;
    }
    public void setTranType(String tranType){
         this.tranType=tranType;
    }
    public String getBankCardNo(){
         return bankCardNo;
    }
    public void setBankCardNo(String bankCardNo){
         this.bankCardNo=bankCardNo;
    }
    public String getBankDepNo(){
         return bankDepNo;
    }
    public void setBankDepNo(String bankDepNo){
         this.bankDepNo=bankDepNo;
    }
    public String getBankSignVal(){
         return bankSignVal;
    }
    public void setBankSignVal(String bankSignVal){
         this.bankSignVal=bankSignVal;
    }
    public Date getRegdtTime(){
         return regdtTime;
    }
    public void setRegdtTime(Date regdtTime){
         this.regdtTime=regdtTime;
    }
    public String getWarnType(){
         return warnType;
    }
    public void setWarnType(String warnType){
         this.warnType=warnType;
    }
    public String getRuleCode(){
         return ruleCode;
    }
    public void setRuleCode(String ruleCode){
         this.ruleCode=ruleCode;
    }
    public String getExceptTranFlag(){
         return exceptTranFlag;
    }
    public void setExceptTranFlag(String exceptTranFlag){
         this.exceptTranFlag=exceptTranFlag;
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
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    
    // ============start================
    public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getPrdordno() {
		return prdordno;
	}
	public void setPrdordno(String prdordno) {
		this.prdordno = prdordno;
	}
	/*public Long getOrdamt() {
		return ordamt;
	}
	public void setOrdamt(Long ordamt) {
		this.ordamt = ordamt;
	}
	public String getOrdstatus() {
		return ordstatus;
	}
	public void setOrdstatus(String ordstatus) {
		this.ordstatus = ordstatus;
	}*/
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getRuleLevelItem() {
		return ruleLevelItem;
	}
	public void setRuleLevelItem(String ruleLevelItem) {
		this.ruleLevelItem = ruleLevelItem;
	}
	// ============start================
	
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "tranId="+tranId+"\n";
        temp = temp + "entityType="+entityType+"\n";
        temp = temp + "tranSource="+tranSource+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "tranType="+tranType+"\n";
        temp = temp + "bankCardNo="+bankCardNo+"\n";
        temp = temp + "bankDepNo="+bankDepNo+"\n";
        temp = temp + "bankSignVal="+bankSignVal+"\n";
        temp = temp + "regdtTime="+regdtTime+"\n";
        temp = temp + "warnType="+warnType+"\n";
        temp = temp + "ruleCode="+ruleCode+"\n";
        temp = temp + "exceptTranFlag="+exceptTranFlag+"\n";
        temp = temp + "updateUser="+updateUser+"\n";
        temp = temp + "updateTime="+updateTime+"\n";
        temp = temp + "remark="+remark+"\n";
        
        // ============start================
        temp = temp + "storeName="+storeName+"\n";
        temp = temp + "prdordno="+prdordno+"\n";
        /*temp = temp + "ordamt="+ordamt+"\n";
        temp = temp + "ordstatus="+ordstatus+"\n";*/        
        temp = temp + "ruleName="+ruleName+"\n";
        temp = temp + "ruleLevelItem="+ruleLevelItem+"\n";
        // ============end================
        temp = temp + "regdtTimeSearchEnd="+regdtTimeSearchEnd+"\n";
        
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("tranId",tranId);
        json.put("entityType",entityType);
        json.put("tranSource",tranSource);
        json.put("custId",custId);
        json.put("tranType",tranType);
        json.put("bankCardNo",bankCardNo);
        json.put("bankDepNo",bankDepNo);
        json.put("bankSignVal",bankSignVal);
        try{
            json.put("regdtTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(regdtTime));
        } catch (Exception e) {}
        json.put("warnType",warnType);
        json.put("ruleCode",ruleCode);
        json.put("exceptTranFlag",exceptTranFlag);
        json.put("updateUser",updateUser);
        try{
            json.put("updateTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(updateTime));
        } catch (Exception e) {}
        json.put("remark",remark);
        
        // ============start================
        json.put("storeName",storeName);
        json.put("prdordno",prdordno);
        /*json.put("ordamt",ordamt);
        json.put("ordstatus",ordstatus);*/
        json.put("ruleName",ruleName);
        json.put("ruleLevelItem",ruleLevelItem);
        // ============end================
        try{
        	json.put("regdtTimeSearchEnd", new java.text.SimpleDateFormat("yyyy-MM-dd").format(regdtTimeSearchEnd));
        } catch (Exception e) {}
        
        return json;
    }
}