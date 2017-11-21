package com.pay.account.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_ACC_ENTRY entity.
 * 
 * @author administrator
 */
public class PayAccEntry {
    public String txnCod;
    public String txnSubCod;
    public String accSeq;
    public String drCrFlag;
    public String subjectFrom;
    public String subject;
    public String accOrgNo;
    public String rmkCod;
    public String id;
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String  glCode;
    public String glName;
    
    public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	public String getGlName() {
		return glName;
	}
	public void setGlName(String glName) {
		this.glName = glName;
	}
	public String getTxnCod(){
         return txnCod;
    }
    public void setTxnCod(String txnCod){
         this.txnCod=txnCod;
    }
    public String getTxnSubCod(){
         return txnSubCod;
    }
    public void setTxnSubCod(String txnSubCod){
         this.txnSubCod=txnSubCod;
    }
    public String getAccSeq(){
         return accSeq;
    }
    public void setAccSeq(String accSeq){
         this.accSeq=accSeq;
    }
    public String getDrCrFlag(){
         return drCrFlag;
    }
    public void setDrCrFlag(String drCrFlag){
         this.drCrFlag=drCrFlag;
    }
    public String getSubjectFrom(){
         return subjectFrom;
    }
    public void setSubjectFrom(String subjectFrom){
         this.subjectFrom=subjectFrom;
    }
    public String getSubject(){
         return subject;
    }
    public void setSubject(String subject){
         this.subject=subject;
    }
    public String getAccOrgNo(){
         return accOrgNo;
    }
    public void setAccOrgNo(String accOrgNo){
         this.accOrgNo=accOrgNo;
    }
    public String getRmkCod(){
         return rmkCod;
    }
    public void setRmkCod(String rmkCod){
         this.rmkCod=rmkCod;
    }
    public String toString(){
        String temp = "";
        temp = temp + "txnCod="+txnCod+"\n";
        temp = temp + "txnSubCod="+txnSubCod+"\n";
        temp = temp + "accSeq="+accSeq+"\n";
        temp = temp + "drCrFlag="+drCrFlag+"\n";
        temp = temp + "subjectFrom="+subjectFrom+"\n";
        temp = temp + "subject="+subject+"\n";
        temp = temp + "accOrgNo="+accOrgNo+"\n";
        temp = temp + "rmkCod="+rmkCod+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("txnCod",txnCod);
        json.put("txnSubCod",txnSubCod);
        json.put("accSeq",accSeq);
        json.put("drCrFlag",drCrFlag);
        json.put("subjectFrom",subjectFrom);
        json.put("subject",subject);
        json.put("accOrgNo",accOrgNo);
        json.put("rmkCod",rmkCod);
        json.put("glCode",rmkCod);
        json.put("glName",glName);
        json.put("id",id);
        return json;
    }
}