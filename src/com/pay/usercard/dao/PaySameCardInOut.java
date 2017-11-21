package com.pay.usercard.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_SAME_CARD_IN_OUT entity.
 * 
 * @author administrator
 */
public class PaySameCardInOut {
    public String id;
    public String custId;
    public String bankName;
    public String bankCode;
    public String accountType;
    public String accountNo;
    public String accountName;
    public String credentialType;
    public String credentialNo;
    public String tel;
    public String status;
    public String remark;
    public Date createTime;
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
    public String getBankName(){
         return bankName;
    }
    public void setBankName(String bankName){
         this.bankName=bankName;
    }
    public String getBankCode(){
         return bankCode;
    }
    public void setBankCode(String bankCode){
         this.bankCode=bankCode;
    }
    public String getAccountType(){
         return accountType;
    }
    public void setAccountType(String accountType){
         this.accountType=accountType;
    }
    public String getAccountNo(){
         return accountNo;
    }
    public void setAccountNo(String accountNo){
         this.accountNo=accountNo;
    }
    public String getAccountName(){
         return accountName;
    }
    public void setAccountName(String accountName){
         this.accountName=accountName;
    }
    public String getCredentialType(){
         return credentialType;
    }
    public void setCredentialType(String credentialType){
         this.credentialType=credentialType;
    }
    public String getCredentialNo(){
         return credentialNo;
    }
    public void setCredentialNo(String credentialNo){
         this.credentialNo=credentialNo;
    }
    public String getTel(){
         return tel;
    }
    public void setTel(String tel){
         this.tel=tel;
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
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "bankName="+bankName+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "accountType="+accountType+"\n";
        temp = temp + "accountNo="+accountNo+"\n";
        temp = temp + "accountName="+accountName+"\n";
        temp = temp + "credentialType="+credentialType+"\n";
        temp = temp + "credentialNo="+credentialNo+"\n";
        temp = temp + "tel="+tel+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("custId",custId);
        json.put("bankName",bankName);
        json.put("bankCode",bankCode);
        json.put("accountType",accountType);
        json.put("accountNo",accountNo);
        json.put("accountName",accountName);
        json.put("credentialType",credentialType);
        json.put("credentialNo",credentialNo);
        json.put("tel",tel);
        json.put("status",status);
        json.put("remark",remark);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}