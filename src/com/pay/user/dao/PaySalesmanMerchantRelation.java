package com.pay.user.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_SALESMAN_MERCHANT_RELATION entity.
 * 
 * @author administrator
 */
public class PaySalesmanMerchantRelation {
    public String userId;
    public String merNo;
    public String uName;
    public String mName;
    public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getUserId(){
         return userId;
    }
    public void setUserId(String userId){
         this.userId=userId;
    }
    public String getMerNo(){
         return merNo;
    }
    public void setMerNo(String merNo){
         this.merNo=merNo;
    }
    public String toString(){
        String temp = "";
        temp = temp + "userId="+userId+"\n";
        temp = temp + "merNo="+merNo+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("userId",userId);
        json.put("merNo",merNo);
        json.put("uName",uName);
        json.put("mName",mName);
        return json;
    }
}