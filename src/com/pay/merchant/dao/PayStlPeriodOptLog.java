package com.pay.merchant.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_STL_PERIOD_OPT_LOG entity.
 * 
 * @author administrator
 */
public class PayStlPeriodOptLog {
    public String id;
    public String merNo;
    public String stlType;
    public Long accBalance;
    public Long frozenAmt;
    public String custSetPeriodOld;
    public String custStlTimeSetOld;
    public Date createTime;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getMerNo(){
         return merNo;
    }
    public void setMerNo(String merNo){
         this.merNo=merNo;
    }
    public Long getAccBalance(){
         return accBalance;
    }
    public void setAccBalance(Long accBalance){
         this.accBalance=accBalance;
    }
    public Long getFrozenAmt(){
         return frozenAmt;
    }
    public void setFrozenAmt(Long frozenAmt){
         this.frozenAmt=frozenAmt;
    }
    public String getCustSetPeriodOld(){
         return custSetPeriodOld;
    }
    public void setCustSetPeriodOld(String custSetPeriodOld){
         this.custSetPeriodOld=custSetPeriodOld;
    }
    public String getCustStlTimeSetOld(){
         return custStlTimeSetOld;
    }
    public void setCustStlTimeSetOld(String custStlTimeSetOld){
         this.custStlTimeSetOld=custStlTimeSetOld;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    
    public String getStlType() {
		return stlType;
	}
	public void setStlType(String stlType) {
		this.stlType = stlType;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "merNo="+merNo+"\n";
        temp = temp + "accBalance="+accBalance+"\n";
        temp = temp + "frozenAmt="+frozenAmt+"\n";
        temp = temp + "custSetPeriodOld="+custSetPeriodOld+"\n";
        temp = temp + "custStlTimeSetOld="+custStlTimeSetOld+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("merNo",merNo);
        json.put("accBalance",String.format("%.2f", ((double)accBalance)/100d));
        json.put("frozenAmt",String.format("%.2f", ((double)frozenAmt)/100d));
        json.put("custSetPeriodOld",custSetPeriodOld);
        json.put("custStlTimeSetOld",custStlTimeSetOld);
        json.put("stlType",stlType);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
}