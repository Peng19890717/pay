package com.pay.merchantinterface.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_REAL_NAME_AUTH entity.
 * 
 * @author administrator
 */
public class PayRealNameAuth {
    public String id;
    public String userId;
    public String way;
    public String channelId;
    public String sn;
    public String bankGeneralName;
    public String bankName;
    public String bankCode;
    public String accountType;
    public String accountNo;
    public String accountName;
    public String idType;
    public String certId;
    public String tel;
    public String status;
    public String field1;
    public String field2;
    public String remark;
    public Date createTime;
    public String tranId;
    public Date successTime;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getUserId(){
         return userId;
    }
    public void setUserId(String userId){
         this.userId=userId;
    }
    public String getWay(){
         return way;
    }
    public void setWay(String way){
         this.way=way;
    }
    public String getChannelId(){
         return channelId;
    }
    public void setChannelId(String channelId){
         this.channelId=channelId;
    }
    public String getSn(){
         return sn;
    }
    public void setSn(String sn){
         this.sn=sn;
    }
    public String getBankGeneralName(){
         return bankGeneralName;
    }
    public void setBankGeneralName(String bankGeneralName){
         this.bankGeneralName=bankGeneralName;
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
    public String getIdType(){
         return idType;
    }
    public void setIdType(String idType){
         this.idType=idType;
    }
    public String getCertId(){
         return certId;
    }
    public void setCertId(String certId){
         this.certId=certId;
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
    public String getField1(){
         return field1;
    }
    public void setField1(String field1){
         this.field1=field1;
    }
    public String getField2(){
         return field2;
    }
    public void setField2(String field2){
         this.field2=field2;
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
    public String getTranId(){
         return tranId;
    }
    public void setTranId(String tranId){
         this.tranId=tranId;
    }
    public Date getSuccessTime(){
         return successTime;
    }
    public void setSuccessTime(Date successTime){
         this.successTime=successTime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "userId="+userId+"\n";
        temp = temp + "way="+way+"\n";
        temp = temp + "channelId="+channelId+"\n";
        temp = temp + "sn="+sn+"\n";
        temp = temp + "bankGeneralName="+bankGeneralName+"\n";
        temp = temp + "bankName="+bankName+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "accountType="+accountType+"\n";
        temp = temp + "accountNo="+accountNo+"\n";
        temp = temp + "accountName="+accountName+"\n";
        temp = temp + "idType="+idType+"\n";
        temp = temp + "certId="+certId+"\n";
        temp = temp + "tel="+tel+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "field1="+field1+"\n";
        temp = temp + "field2="+field2+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "tranId="+tranId+"\n";
        temp = temp + "successTime="+successTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("userId",userId);
        json.put("way",way);
        json.put("channelId",channelId);
        json.put("sn",sn);
        json.put("bankGeneralName",bankGeneralName);
        json.put("bankName",bankName);
        json.put("bankCode",bankCode);
        json.put("accountType",accountType);
        json.put("accountNo",accountNo);
        json.put("accountName",accountName);
        json.put("idType",idType);
        json.put("certId",certId);
        json.put("tel",tel);
        json.put("status",status);
        json.put("field1",field1);
        json.put("field2",field2);
        json.put("remark",remark);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        json.put("tranId",tranId);
        try{
            json.put("successTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(successTime));
        } catch (Exception e) {}
        return json;
    }
}