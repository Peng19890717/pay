package com.pay.order.dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
/**
 * Table PAY_TRANSFER_ACC_ORDER entity.
 * 
 * @author administrator
 */
public class PayTransferAccOrder {
    public String tranOrdno;
    public Date tranTime;
    public String tranTimeStart;
    public String tranTimeEnd;
    public String clearDate;
    public String status;
    public String ccy;
    public Long txamt;
    public String tgetAccNo;
    public String tgetAccName;
    public Long totamt;
    public String backError;
    public String bankCode;
    public String custId;
    public String custName;
    public String batTranCustOrdno;
    public String batNo;
    public String tranType;
    public String filed1;
    public String filed2;
    public String cardBankBranch;
    public String type;
    public String issuser;
    public Date tranSuccessTime;
    public String custType;
    public String tgetAccType;
    
    public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getTgetAccType() {
		return tgetAccType;
	}
	public void setTgetAccType(String tgetAccType) {
		this.tgetAccType = tgetAccType;
	}
	public String getTranTimeStart() {
		return tranTimeStart;
	}
	public void setTranTimeStart(String tranTimeStart) {
		this.tranTimeStart = tranTimeStart;
	}
	public String getTranTimeEnd() {
		return tranTimeEnd;
	}
	public void setTranTimeEnd(String tranTimeEnd) {
		this.tranTimeEnd = tranTimeEnd;
	}
	public String getTranOrdno(){
         return tranOrdno;
    }
    public void setTranOrdno(String tranOrdno){
         this.tranOrdno=tranOrdno;
    }
    public Date getTranTime(){
         return tranTime;
    }
    public void setTranTime(Date tranTime){
         this.tranTime=tranTime;
    }
    public String getClearDate(){
         return clearDate;
    }
    public void setClearDate(String clearDate){
         this.clearDate=clearDate;
    }
    public String getStatus(){
         return status;
    }
    public void setStatus(String status){
         this.status=status;
    }
    public String getCcy(){
         return ccy;
    }
    public void setCcy(String ccy){
         this.ccy=ccy;
    }
    public Long getTxamt(){
         return txamt;
    }
    public void setTxamt(Long txamt){
         this.txamt=txamt;
    }
    public String getTgetAccNo(){
         return tgetAccNo;
    }
    public void setTgetAccNo(String tgetAccNo){
         this.tgetAccNo=tgetAccNo;
    }
    public String getTgetAccName(){
         return tgetAccName;
    }
    public void setTgetAccName(String tgetAccName){
         this.tgetAccName=tgetAccName;
    }
    public Long getTotamt(){
         return totamt;
    }
    public void setTotamt(Long totamt){
         this.totamt=totamt;
    }
    public String getBackError(){
         return backError;
    }
    public void setBackError(String backError){
         this.backError=backError;
    }
    public String getBankCode(){
         return bankCode;
    }
    public void setBankCode(String bankCode){
         this.bankCode=bankCode;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getBatTranCustOrdno(){
         return batTranCustOrdno;
    }
    public void setBatTranCustOrdno(String batTranCustOrdno){
         this.batTranCustOrdno=batTranCustOrdno;
    }
    public String getBatNo(){
         return batNo;
    }
    public void setBatNo(String batNo){
         this.batNo=batNo;
    }
    public String getTranType(){
         return tranType;
    }
    public void setTranType(String tranType){
         this.tranType=tranType;
    }
    public String getFiled1(){
         return filed1;
    }
    public void setFiled1(String filed1){
         this.filed1=filed1;
    }
    public String getFiled2(){
         return filed2;
    }
    public void setFiled2(String filed2){
         this.filed2=filed2;
    }
    public String getCardBankBranch(){
         return cardBankBranch;
    }
    public void setCardBankBranch(String cardBankBranch){
         this.cardBankBranch=cardBankBranch;
    }
    public String getType(){
         return type;
    }
    public void setType(String type){
         this.type=type;
    }
    public String getIssuser(){
         return issuser;
    }
    public void setIssuser(String issuser){
         this.issuser=issuser;
    }
    public Date getTranSuccessTime(){
         return tranSuccessTime;
    }
    public void setTranSuccessTime(Date tranSuccessTime){
         this.tranSuccessTime=tranSuccessTime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "tranOrdno="+tranOrdno+"\n";
        temp = temp + "tranTime="+tranTime+"\n";
        temp = temp + "clearDate="+clearDate+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "ccy="+ccy+"\n";
        temp = temp + "txamt="+txamt+"\n";
        temp = temp + "tgetAccNo="+tgetAccNo+"\n";
        temp = temp + "tgetAccName="+tgetAccName+"\n";
        temp = temp + "totamt="+totamt+"\n";
        temp = temp + "backError="+backError+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "batTranCustOrdno="+batTranCustOrdno+"\n";
        temp = temp + "batNo="+batNo+"\n";
        temp = temp + "tranType="+tranType+"\n";
        temp = temp + "filed1="+filed1+"\n";
        temp = temp + "filed2="+filed2+"\n";
        temp = temp + "cardBankBranch="+cardBankBranch+"\n";
        temp = temp + "type="+type+"\n";
        temp = temp + "issuser="+issuser+"\n";
        temp = temp + "tranSuccessTime="+tranSuccessTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("tranOrdno",tranOrdno);
        try{
            json.put("tranTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tranTime));
        } catch (Exception e) {}
        json.put("clearDate",clearDate);
        json.put("status",status);
        json.put("ccy",ccy);
        json.put("txamt",String.format("%.2f",(float)txamt*0.01));
        json.put("tgetAccNo",tgetAccNo);
        json.put("tgetAccName",tgetAccName);
        json.put("totamt",String.format("%.2f",(float)totamt*0.01));
        json.put("fee",String.format("%.2f",(float)(totamt-txamt)*0.01));
        json.put("backError",backError);
        json.put("bankCode",bankCode);
        json.put("custId",custId);
        json.put("custName",custName);
        json.put("batTranCustOrdno",batTranCustOrdno);
        json.put("batNo",batNo);
        json.put("tranType",tranType);
        json.put("filed1",filed1);
        json.put("filed2",filed2);
        json.put("cardBankBranch",cardBankBranch);
        json.put("type",type);
        json.put("issuser",issuser);
        json.put("tgetAccType",tgetAccType);
        try{
            json.put("tranSuccessTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tranSuccessTime));
        } catch (Exception e) {}
        return json;
    }
}