package com.pay.merchant.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_YAKU_STL_ACC_CACHE entity.
 * 
 * @author administrator
 */
public class PayYakuStlAccCache {
    public String merno;
    public String accNo;
    public Long amt;
    public String stlDate;
    public String getMerno(){
         return merno;
    }
    public void setMerno(String merno){
         this.merno=merno;
    }
    public String getAccNo(){
         return accNo;
    }
    public void setAccNo(String accNo){
         this.accNo=accNo;
    }
    public Long getAmt(){
         return amt;
    }
    public void setAmt(Long amt){
         this.amt=amt;
    }
    public String getStlDate(){
         return stlDate;
    }
    public void setStlDate(String stlDate){
         this.stlDate=stlDate;
    }
    public String toString(){
        String temp = "";
        temp = temp + "merno="+merno+"\n";
        temp = temp + "accNo="+accNo+"\n";
        temp = temp + "amt="+amt+"\n";
        temp = temp + "stlDate="+stlDate+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("merno",merno);
        json.put("accNo",accNo);
        json.put("amt",String.valueOf(amt));
        json.put("stlDate",stlDate);
        return json;
    }
}