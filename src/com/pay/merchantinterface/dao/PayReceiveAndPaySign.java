package com.pay.merchantinterface.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_RECEIVE_AND_PAY_SIGN entity.
 * 
 * @author administrator
 */
public class PayReceiveAndPaySign {
    public String id;
    public String corpAcctNo;
    public String businessCode;
    public String protocolType;
    public String sn;
    public String protocolNo;
    public String bankName;
    public String bankCode;
    public String accountType;
    public String accountNo;
    public String accountName;
    public String idType;
    public String certId;
    public String beginDate;
    public String endDate;
    public String tel;
    public String status;
    public String field1;
    public String field2;
    public String remark;
    public Date createTime;
    public String signProtocolChannel;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getCorpAcctNo(){
         return corpAcctNo;
    }
    public void setCorpAcctNo(String corpAcctNo){
         this.corpAcctNo=corpAcctNo;
    }
    public String getBusinessCode(){
         return businessCode;
    }
    public void setBusinessCode(String businessCode){
         this.businessCode=businessCode;
    }
    public String getProtocolType(){
         return protocolType;
    }
    public void setProtocolType(String protocolType){
         this.protocolType=protocolType;
    }
    public String getSn(){
         return sn;
    }
    public void setSn(String sn){
         this.sn=sn;
    }
    public String getProtocolNo(){
         return protocolNo;
    }
    public void setProtocolNo(String protocolNo){
         this.protocolNo=protocolNo;
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
    public String getBeginDate(){
         return beginDate;
    }
    public void setBeginDate(String beginDate){
         this.beginDate=beginDate;
    }
    public String getEndDate(){
         return endDate;
    }
    public void setEndDate(String endDate){
         this.endDate=endDate;
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
    
    public String getSignProtocolChannel() {
		return signProtocolChannel;
	}
	public void setSignProtocolChannel(String signProtocolChannel) {
		this.signProtocolChannel = signProtocolChannel;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "corpAcctNo="+corpAcctNo+"\n";
        temp = temp + "businessCode="+businessCode+"\n";
        temp = temp + "protocolType="+protocolType+"\n";
        temp = temp + "sn="+sn+"\n";
        temp = temp + "protocolNo="+protocolNo+"\n";
        temp = temp + "bankName="+bankName+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "accountType="+accountType+"\n";
        temp = temp + "accountNo="+accountNo+"\n";
        temp = temp + "accountName="+accountName+"\n";
        temp = temp + "idType="+idType+"\n";
        temp = temp + "certId="+certId+"\n";
        temp = temp + "beginDate="+beginDate+"\n";
        temp = temp + "endDate="+endDate+"\n";
        temp = temp + "tel="+tel+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "field1="+field1+"\n";
        temp = temp + "field2="+field2+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("corpAcctNo",corpAcctNo);
        json.put("businessCode",businessCode);
        json.put("protocolType",protocolType);
        json.put("sn",sn);
        json.put("protocolNo",protocolNo);
        json.put("bankName",bankName);
        json.put("bankCode",bankCode);
        json.put("accountType",accountType);
        json.put("accountNo",accountNo);
        json.put("accountName",accountName);
        json.put("idType",idType);
        json.put("certId",certId);
        json.put("beginDate",beginDate);
        json.put("endDate",endDate);
        json.put("tel",tel);
        json.put("status",status);
        json.put("field1",field1);
        json.put("field2",field2);
        json.put("remark",remark);
        json.put("signProtocolChannel",signProtocolChannel);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}