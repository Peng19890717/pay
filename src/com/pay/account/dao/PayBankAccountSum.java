package com.pay.account.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * 对账汇总 entity.
 * 
 * @author administrator
 */
public class PayBankAccountSum {
    public String bankcode;
    public String bankName;
    public String actdate;
    public String actdateStart;
    public String actdateEnd;
    public String cwType;
    public String chktype;
    public Long cwCount = 0l;
    public Long cwAmt = 0l;
    public Long sumCount = 0l;
    public Long sumAmt = 0l;
    public Long sucCount = 0l;
    public Long sucAmt = 0l;
    public Long failCount = 0l;
    public Long failAmt = 0l;
    public String getBankcode(){
         return bankcode;
    }
    public void setBankcode(String bankcode){
         this.bankcode=bankcode;
    }
    
    public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getActdate(){
         return actdate;
    }
    public void setActdate(String actdate){
         this.actdate=actdate;
    }
    public String getActdateStart() {
		return actdateStart;
	}
	public void setActdateStart(String actdateStart) {
		this.actdateStart = actdateStart;
	}
	public String getActdateEnd() {
		return actdateEnd;
	}
	public void setActdateEnd(String actdateEnd) {
		this.actdateEnd = actdateEnd;
	}
	public Long getFailAmt() {
		return failAmt;
	}
	public void setFailAmt(Long failAmt) {
		this.failAmt = failAmt;
	}
	public String getChktype(){
         return chktype;
    }
    public void setChktype(String chktype){
         this.chktype=chktype;
    }
    public Long getSucCount() {
		return sucCount;
	}
	public void setSucCount(Long sucCount) {
		this.sucCount = sucCount;
	}
	
	public Long getSumAmt() {
		return sumAmt;
	}
	public void setSumAmt(Long sumAmt) {
		this.sumAmt = sumAmt;
	}
	public Long getSumCount() {
		return sumCount;
	}
	public void setSumCount(Long sumCount) {
		this.sumCount = sumCount;
	}
	public Long getSucAmt() {
		return sucAmt;
	}
	public void setSucAmt(Long sucAmt) {
		this.sucAmt = sucAmt;
	}
	public Long getFailCount() {
		return failCount;
	}
	public void setFailCount(Long failCount) {
		this.failCount = failCount;
	}
	public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("bankcode",bankcode);
        json.put("bankName",bankName);
        try {
        	json.put("actdate",actdate.substring(0,4)+"-"+actdate.substring(4,6)+"-"+actdate.substring(6,8));
		} catch (Exception e) {
			json.put("actdate","");
		}
        json.put("chktype",chktype);
        json.put("sumCount",sumCount);
        json.put("sumAmt",String.format("%.2f",(float)sumAmt*0.01));
        json.put("sucCount",sucCount);
        json.put("sucAmt",String.format("%.2f",(float)sucAmt*0.01));
        json.put("failCount",failCount);
        json.put("failAmt",String.format("%.2f",(float)failAmt*0.01));
        return json;
    }
}