package com.pay.account.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_GENERAL_LEDGER_HIS entity.
 * 
 * @author administrator
 */
public class PayGeneralLedgerHis {
    public String seqNo;
    public String brNo;
    public String ccy;
    public String glCode;
    public String glName;
	public Date acDate;
    public Long prevDrBal;
    public Long prevCrBal;
    public Long drCount;
    public Long drAmt;
    public Long crCount;
    public Long crAmt;
    public Long currDrBal;
    public Long currCrBal;
    
    public String getSeqNo(){
         return seqNo;
    }
    public void setSeqNo(String seqNo){
         this.seqNo=seqNo;
    }
    public String getBrNo(){
         return brNo;
    }
    public void setBrNo(String brNo){
         this.brNo=brNo;
    }
    public String getCcy(){
         return ccy;
    }
    public void setCcy(String ccy){
         this.ccy=ccy;
    }
    public String getGlCode(){
         return glCode;
    }
    public void setGlCode(String glCode){
         this.glCode=glCode;
    }
    public String getGlName() {
		return glName;
	}
	public void setGlName(String glName) {
		this.glName = glName;
	}
    public Date getAcDate(){
         return acDate;
    }
    public void setAcDate(Date acDate){
         this.acDate=acDate;
    }
    public Long getPrevDrBal(){
         return prevDrBal;
    }
    public void setPrevDrBal(Long prevDrBal){
         this.prevDrBal=prevDrBal;
    }
    public Long getPrevCrBal(){
         return prevCrBal;
    }
    public void setPrevCrBal(Long prevCrBal){
         this.prevCrBal=prevCrBal;
    }
    public Long getDrCount(){
         return drCount;
    }
    public void setDrCount(Long drCount){
         this.drCount=drCount;
    }
    public Long getDrAmt(){
         return drAmt;
    }
    public void setDrAmt(Long drAmt){
         this.drAmt=drAmt;
    }
    public Long getCrCount(){
         return crCount;
    }
    public void setCrCount(Long crCount){
         this.crCount=crCount;
    }
    public Long getCrAmt(){
         return crAmt;
    }
    public void setCrAmt(Long crAmt){
         this.crAmt=crAmt;
    }
    public Long getCurrDrBal(){
         return currDrBal;
    }
    public void setCurrDrBal(Long currDrBal){
         this.currDrBal=currDrBal;
    }
    public Long getCurrCrBal(){
         return currCrBal;
    }
    public void setCurrCrBal(Long currCrBal){
         this.currCrBal=currCrBal;
    }
    public String toString(){
        String temp = "";
        temp = temp + "seqNo="+seqNo+"\n";
        temp = temp + "brNo="+brNo+"\n";
        temp = temp + "ccy="+ccy+"\n";
        temp = temp + "glCode="+glCode+"\n";
        temp = temp + "glName="+glName+"\n";
        temp = temp + "acDate="+acDate+"\n";
        temp = temp + "prevDrBal="+prevDrBal+"\n";
        temp = temp + "prevCrBal="+prevCrBal+"\n";
        temp = temp + "drCount="+drCount+"\n";
        temp = temp + "drAmt="+drAmt+"\n";
        temp = temp + "crCount="+crCount+"\n";
        temp = temp + "crAmt="+crAmt+"\n";
        temp = temp + "currDrBal="+currDrBal+"\n";
        temp = temp + "currCrBal="+currCrBal+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("seqNo",seqNo);
        json.put("brNo",brNo);
        json.put("ccy",ccy);
        json.put("glCode",glCode);
        json.put("glName",glName);
        try{
            json.put("acDate", new java.text.SimpleDateFormat("yyyy-MM-dd").format(acDate));
        } catch (Exception e) {}
        json.put("prevDrBal",String.valueOf(prevDrBal));
        json.put("prevCrBal",String.valueOf(prevCrBal));
        json.put("drCount",String.valueOf(drCount));
        json.put("drAmt",String.valueOf(drAmt));
        json.put("crCount",String.valueOf(crCount));
        json.put("crAmt",String.valueOf(crAmt));
        json.put("currDrBal",String.valueOf(currDrBal));
        json.put("currCrBal",String.valueOf(currCrBal));
        return json;
    }
}