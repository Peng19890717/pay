package com.pay.account.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_BANK_ACCOUNT_CHECK_TMP entity.
 * 
 * @author administrator
 */
public class PayBankAccountCheckTmp {
    public String bankcode;
    public String actdate;
    public String bnkordno;
    public String chktype;
    public Long txnamt;
    public Long fee;
    public Long inamt;
    public String banklogno;
    public String status;
    public String getBankcode(){
         return bankcode;
    }
    public void setBankcode(String bankcode){
         this.bankcode=bankcode;
    }
    public String getActdate(){
         return actdate;
    }
    public void setActdate(String actdate){
         this.actdate=actdate;
    }
    public String getBnkordno(){
         return bnkordno;
    }
    public void setBnkordno(String bnkordno){
         this.bnkordno=bnkordno;
    }
    public String getChktype(){
         return chktype;
    }
    public void setChktype(String chktype){
         this.chktype=chktype;
    }
    public long getTxnamt(){
         return txnamt;
    }
    public void setTxnamt(long txnamt){
         this.txnamt=txnamt;
    }
    public long getFee(){
         return fee;
    }
    public void setFee(long fee){
         this.fee=fee;
    }
    public long getInamt(){
         return inamt;
    }
    public void setInamt(long inamt){
         this.inamt=inamt;
    }
    public String getBanklogno(){
         return banklogno;
    }
    public void setBanklogno(String banklogno){
         this.banklogno=banklogno;
    }
    
    public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setTxnamt(Long txnamt) {
		this.txnamt = txnamt;
	}
	public void setFee(Long fee) {
		this.fee = fee;
	}
	public void setInamt(Long inamt) {
		this.inamt = inamt;
	}
	public String toString(){
        String temp = "";
        temp = temp + "bankcode="+bankcode+"\n";
        temp = temp + "actdate="+actdate+"\n";
        temp = temp + "bnkordno="+bnkordno+"\n";
        temp = temp + "chktype="+chktype+"\n";
        temp = temp + "txnamt="+txnamt+"\n";
        temp = temp + "fee="+fee+"\n";
        temp = temp + "inamt="+inamt+"\n";
        temp = temp + "banklogno="+banklogno+"\n";
        temp = temp + "status="+status+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("bankcode",bankcode);
        json.put("actdate",actdate);
        json.put("bnkordno",bnkordno);
        json.put("chktype",chktype);
        json.put("txnamt",String.valueOf(txnamt));
        json.put("fee",String.valueOf(fee));
        json.put("inamt",String.valueOf(inamt));
        json.put("banklogno",banklogno);
        json.put("status",status);
        return json;
    }
}