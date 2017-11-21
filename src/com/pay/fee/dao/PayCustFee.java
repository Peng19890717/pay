package com.pay.fee.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_CUST_FEE entity.
 * 
 * @author administrator
 */
public class PayCustFee {
    public String id;
    public String custType;
    public String custId;
    public String tranType;
    public String feeCode;
    public Date createTime;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getCustType(){
         return custType;
    }
    public void setCustType(String custType){
         this.custType=custType;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getTranType(){
         return tranType;
    }
    public void setTranType(String tranType){
         this.tranType=tranType;
    }
    public String getFeeCode(){
         return feeCode;
    }
    public void setFeeCode(String feeCode){
         this.feeCode=feeCode;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "custType="+custType+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "tranType="+tranType+"\n";
        temp = temp + "feeCode="+feeCode+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("custType",custType);
        json.put("custId",custId);
        json.put("tranType",tranType);
        json.put("feeCode",feeCode);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}