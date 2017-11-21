package com.pay.bank.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_BANK entity.
 * 
 * @author administrator
 */
public class PayBank {
	public String id;
    public String bankCode;
    public String bankName;
    public String bankNo;
    public String jiejiCard;
    public String daijiCard;
    public String compCard;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getBankCode(){
         return bankCode;
    }
    public void setBankCode(String bankCode){
         this.bankCode=bankCode;
    }
    public String getBankName(){
         return bankName;
    }
    public void setBankName(String bankName){
         this.bankName=bankName;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "bankName="+bankName+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("bankCode",bankCode);
        json.put("bankName",bankName);
        return json;
    }
}