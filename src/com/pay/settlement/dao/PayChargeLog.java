package com.pay.settlement.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_CHARGE_LOG entity.
 * 
 * @author administrator
 */
public class PayChargeLog {
    public String id;
    public String custType;
    public String custId;
    public String chargeType;
    public String chargeFrom;
    public Long amt;
    public String remark;
    public String createId;
    public Date createTime;
    public Date settleTime;
    public Date createTimeStart;
    public Date createTimeEnd;
    public String custName;
    public Long curStorageFee;
    public Date getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
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
    public String getChargeType(){
         return chargeType;
    }
    public void setChargeType(String chargeType){
         this.chargeType=chargeType;
    }
    public String getChargeFrom(){
         return chargeFrom;
    }
    public void setChargeFrom(String chargeFrom){
         this.chargeFrom=chargeFrom;
    }
    public Long getAmt(){
         return amt;
    }
    public void setAmt(Long amt){
         this.amt=amt;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String getCreateId(){
         return createId;
    }
    public void setCreateId(String createId){
         this.createId=createId;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    
    public Date getSettleTime() {
		return settleTime;
	}
	public void setSettleTime(Date settleTime) {
		this.settleTime = settleTime;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "custType="+custType+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "chargeType="+chargeType+"\n";
        temp = temp + "chargeFrom="+chargeFrom+"\n";
        temp = temp + "amt="+amt+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "createId="+createId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("custType",custType);
        json.put("custId",custId);
        json.put("chargeType",chargeType);
        json.put("chargeFrom",chargeFrom);
        json.put("amt",String.format("%.2f", ((double)amt)/100d));
        json.put("remark",remark);
        json.put("createId",createId);
        json.put("custName",custName);
        json.put("curStorageFee",String.format("%.2f", ((double)curStorageFee)/100d));
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        try{
            json.put("settleTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(settleTime));
        } catch (Exception e) {}
        try{
            json.put("createTimeStart", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTimeStart));
        } catch (Exception e) {}
        try{
            json.put("createTimeEnd", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTimeEnd));
        } catch (Exception e) {}
        return json;
    }
}