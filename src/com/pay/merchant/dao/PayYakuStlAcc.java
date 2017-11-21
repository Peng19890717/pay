package com.pay.merchant.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_YAKU_STL_ACC entity.
 * 
 * @author administrator
 */
public class PayYakuStlAcc {
    public String merno;
    public String accName;
    public String bankCode;
    public String bankBranchName;
    public String accNo;
    public String bankBranchCode;
    public String province;
    public String city;
    public String credentialNo;
    public String tel;
    public String storeName;
    
    public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getMerno(){
         return merno;
    }
    public void setMerno(String merno){
         this.merno=merno;
    }
    public String getAccName(){
         return accName;
    }
    public void setAccName(String accName){
         this.accName=accName;
    }
    public String getBankCode(){
         return bankCode;
    }
    public void setBankCode(String bankCode){
         this.bankCode=bankCode;
    }
    public String getBankBranchName(){
         return bankBranchName;
    }
    public void setBankBranchName(String bankBranchName){
         this.bankBranchName=bankBranchName;
    }
    public String getAccNo(){
         return accNo;
    }
    public void setAccNo(String accNo){
         this.accNo=accNo;
    }
    public String getBankBranchCode(){
         return bankBranchCode;
    }
    public void setBankBranchCode(String bankBranchCode){
         this.bankBranchCode=bankBranchCode;
    }
    public String getProvince(){
         return province;
    }
    public void setProvince(String province){
         this.province=province;
    }
    public String getCity(){
         return city;
    }
    public void setCity(String city){
         this.city=city;
    }
    public String getCredentialNo(){
         return credentialNo;
    }
    public void setCredentialNo(String credentialNo){
         this.credentialNo=credentialNo;
    }
    public String getTel(){
         return tel;
    }
    public void setTel(String tel){
         this.tel=tel;
    }
    public String toString(){
        String temp = "";
        temp = temp + "merno="+merno+"\n";
        temp = temp + "accName="+accName+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "bankBranchName="+bankBranchName+"\n";
        temp = temp + "accNo="+accNo+"\n";
        temp = temp + "bankBranchCode="+bankBranchCode+"\n";
        temp = temp + "province="+province+"\n";
        temp = temp + "city="+city+"\n";
        temp = temp + "credentialNo="+credentialNo+"\n";
        temp = temp + "tel="+tel+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("merno",merno);
        json.put("accName",accName);
        json.put("bankCode",bankCode);
        json.put("bankBranchName",bankBranchName);
        json.put("accNo",accNo);
        json.put("bankBranchCode",bankBranchCode);
        json.put("province",province);
        json.put("city",city);
        json.put("credentialNo",credentialNo);
        json.put("tel",tel);
        json.put("storeName",storeName);
        return json;
    }
}