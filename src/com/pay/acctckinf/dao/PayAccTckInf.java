package com.pay.acctckinf.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_ACC_TCK_INF entity.
 * 
 * @author administrator
 */
public class PayAccTckInf {
    public String accDat;
    public String tckNo;
    public String tckSubNo;
    public String bizTypCode;
    public Date txnTime;
    public String txnOrgNo;
    public String reEntry;
    public String updTckFlag;
    public String chkFlag;
    public String accOrgNo;
    public String ccy;
    public String accSubject;
    public String accNo;
    public String drCrFlag;
    public Long txnAmt;
    public Long accNoBal;
    public String vchTyp;
    public String vchCod;
    public String oppAccSign;
    public String oppAccNo;
    public String oppAccName;
    public String rmkCodInf;
    public String tckInf;
    public Date txnTimeStart;
    public Date txnTimeEnd;
    
    public Date getTxnTimeStart() {
		return txnTimeStart;
	}
	public void setTxnTimeStart(Date txnTimeStart) {
		this.txnTimeStart = txnTimeStart;
	}
	public Date getTxnTimeEnd() {
		return txnTimeEnd;
	}
	public void setTxnTimeEnd(Date txnTimeEnd) {
		this.txnTimeEnd = txnTimeEnd;
	}
	public String getAccDat(){
         return accDat;
    }
    public void setAccDat(String accDat){
         this.accDat=accDat;
    }
    public String getTckNo(){
         return tckNo;
    }
    public void setTckNo(String tckNo){
         this.tckNo=tckNo;
    }
    public String getTckSubNo(){
         return tckSubNo;
    }
    public void setTckSubNo(String tckSubNo){
         this.tckSubNo=tckSubNo;
    }
    public String getBizTypCode(){
         return bizTypCode;
    }
    public void setBizTypCode(String bizTypCode){
         this.bizTypCode=bizTypCode;
    }
    public Date getTxnTime(){
         return txnTime;
    }
    public void setTxnTime(Date txnTime){
         this.txnTime=txnTime;
    }
    public String getTxnOrgNo(){
         return txnOrgNo;
    }
    public void setTxnOrgNo(String txnOrgNo){
         this.txnOrgNo=txnOrgNo;
    }
    public String getReEntry(){
         return reEntry;
    }
    public void setReEntry(String reEntry){
         this.reEntry=reEntry;
    }
    public String getUpdTckFlag(){
         return updTckFlag;
    }
    public void setUpdTckFlag(String updTckFlag){
         this.updTckFlag=updTckFlag;
    }
    public String getChkFlag(){
         return chkFlag;
    }
    public void setChkFlag(String chkFlag){
         this.chkFlag=chkFlag;
    }
    public String getAccOrgNo(){
         return accOrgNo;
    }
    public void setAccOrgNo(String accOrgNo){
         this.accOrgNo=accOrgNo;
    }
    public String getCcy(){
         return ccy;
    }
    public void setCcy(String ccy){
         this.ccy=ccy;
    }
    public String getAccSubject(){
         return accSubject;
    }
    public void setAccSubject(String accSubject){
         this.accSubject=accSubject;
    }
    public String getAccNo(){
         return accNo;
    }
    public void setAccNo(String accNo){
         this.accNo=accNo;
    }
    public String getDrCrFlag(){
         return drCrFlag;
    }
    public void setDrCrFlag(String drCrFlag){
         this.drCrFlag=drCrFlag;
    }
    public Long getTxnAmt(){
         return txnAmt;
    }
    public void setTxnAmt(Long txnAmt){
         this.txnAmt=txnAmt;
    }
    public Long getAccNoBal(){
         return accNoBal;
    }
    public void setAccNoBal(Long accNoBal){
         this.accNoBal=accNoBal;
    }
    public String getVchTyp(){
         return vchTyp;
    }
    public void setVchTyp(String vchTyp){
         this.vchTyp=vchTyp;
    }
    public String getVchCod(){
         return vchCod;
    }
    public void setVchCod(String vchCod){
         this.vchCod=vchCod;
    }
    public String getOppAccSign(){
         return oppAccSign;
    }
    public void setOppAccSign(String oppAccSign){
         this.oppAccSign=oppAccSign;
    }
    public String getOppAccNo(){
         return oppAccNo;
    }
    public void setOppAccNo(String oppAccNo){
         this.oppAccNo=oppAccNo;
    }
    public String getOppAccName(){
         return oppAccName;
    }
    public void setOppAccName(String oppAccName){
         this.oppAccName=oppAccName;
    }
    public String getRmkCodInf(){
         return rmkCodInf;
    }
    public void setRmkCodInf(String rmkCodInf){
         this.rmkCodInf=rmkCodInf;
    }
    public String getTckInf(){
         return tckInf;
    }
    public void setTckInf(String tckInf){
         this.tckInf=tckInf;
    }
    public String toString(){
        String temp = "";
        temp = temp + "accDat="+accDat+"\n";
        temp = temp + "tckNo="+tckNo+"\n";
        temp = temp + "tckSubNo="+tckSubNo+"\n";
        temp = temp + "bizTypCode="+bizTypCode+"\n";
        temp = temp + "txnTime="+txnTime+"\n";
        temp = temp + "txnOrgNo="+txnOrgNo+"\n";
        temp = temp + "reEntry="+reEntry+"\n";
        temp = temp + "updTckFlag="+updTckFlag+"\n";
        temp = temp + "chkFlag="+chkFlag+"\n";
        temp = temp + "accOrgNo="+accOrgNo+"\n";
        temp = temp + "ccy="+ccy+"\n";
        temp = temp + "accSubject="+accSubject+"\n";
        temp = temp + "accNo="+accNo+"\n";
        temp = temp + "drCrFlag="+drCrFlag+"\n";
        temp = temp + "txnAmt="+txnAmt+"\n";
        temp = temp + "accNoBal="+accNoBal+"\n";
        temp = temp + "vchTyp="+vchTyp+"\n";
        temp = temp + "vchCod="+vchCod+"\n";
        temp = temp + "oppAccSign="+oppAccSign+"\n";
        temp = temp + "oppAccNo="+oppAccNo+"\n";
        temp = temp + "oppAccName="+oppAccName+"\n";
        temp = temp + "rmkCodInf="+rmkCodInf+"\n";
        temp = temp + "tckInf="+tckInf+"\n";
        temp = temp + "txnTimeStart="+txnTimeStart+"\n";
        temp = temp + "txnTimeEnd="+txnTimeEnd+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("accDat",accDat);
        json.put("tckNo",tckNo);
        json.put("tckSubNo",tckSubNo);
        json.put("bizTypCode",bizTypCode);
        try{
            json.put("txnTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(txnTime));
            json.put("txnTimeStart", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(txnTimeStart));
            json.put("txnTimeEnd", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(txnTimeEnd));
        } catch (Exception e) {}
        json.put("txnOrgNo",txnOrgNo);
        json.put("reEntry",reEntry);
        json.put("updTckFlag",updTckFlag);
        json.put("chkFlag",chkFlag);
        json.put("accOrgNo",accOrgNo);
        json.put("ccy",ccy);
        json.put("accSubject",accSubject);
        json.put("accNo",accNo);
        json.put("drCrFlag",drCrFlag);
        json.put("accNoBal",String.valueOf(accNoBal));
        json.put("vchTyp",vchTyp);
        json.put("vchCod",vchCod);
        json.put("oppAccSign",oppAccSign);
        json.put("oppAccNo",oppAccNo);
        json.put("oppAccName",oppAccName);
        json.put("rmkCodInf",rmkCodInf);
        json.put("tckInf",tckInf);
        json.put("txnAmt",String.format("%.2f",(float)txnAmt*0.01));
        return json;
    }
}