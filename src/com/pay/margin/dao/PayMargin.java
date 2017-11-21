package com.pay.margin.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_MARGIN entity.
 * 
 * @author administrator
 */
public class PayMargin {
    public String seqNo;
    public String custType;
    public String custId;
    public String pactNo;
    public String marginSign;
    public String marginPayType;
    public Long marginAmt;
    public Long paidInAmt;
    public String marginAc;
    public String custProvisionAcNo;
    public String marginRaSign;
    public String mark;
    public String creOperId;
    public Date creTime;
    public String lstUptOperId;
    public Date lstUptTime;
    public String lstUptTimeStart;
    public String lstUptTimeEnd;
    public String storeName;
    
    public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getSeqNo(){
         return seqNo;
    }
    public void setSeqNo(String seqNo){
         this.seqNo=seqNo;
    }
    
    public String getCustType() {
		return custType;
	}
	public void setCustType(String custType) {
		this.custType = custType;
	}
	public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getPactNo(){
         return pactNo;
    }
    public void setPactNo(String pactNo){
         this.pactNo=pactNo;
    }
    public String getMarginSign(){
         return marginSign;
    }
    public void setMarginSign(String marginSign){
         this.marginSign=marginSign;
    }
    public String getMarginPayType(){
         return marginPayType;
    }
    public void setMarginPayType(String marginPayType){
         this.marginPayType=marginPayType;
    }
    public Long getMarginAmt(){
         return marginAmt;
    }
    public void setMarginAmt(Long marginAmt){
         this.marginAmt=marginAmt;
    }
    public Long getPaidInAmt(){
         return paidInAmt;
    }
    public void setPaidInAmt(Long paidInAmt){
         this.paidInAmt=paidInAmt;
    }
    public String getMarginAc(){
         return marginAc;
    }
    public void setMarginAc(String marginAc){
         this.marginAc=marginAc;
    }
    public String getCustProvisionAcNo(){
         return custProvisionAcNo;
    }
    public void setCustProvisionAcNo(String custProvisionAcNo){
         this.custProvisionAcNo=custProvisionAcNo;
    }
    public String getMarginRaSign(){
         return marginRaSign;
    }
    public void setMarginRaSign(String marginRaSign){
         this.marginRaSign=marginRaSign;
    }
    public String getMark(){
         return mark;
    }
    public void setMark(String mark){
         this.mark=mark;
    }
    public String getCreOperId(){
         return creOperId;
    }
    public void setCreOperId(String creOperId){
         this.creOperId=creOperId;
    }
    public Date getCreTime(){
         return creTime;
    }
    public void setCreTime(Date creTime){
         this.creTime=creTime;
    }
    public String getLstUptOperId(){
         return lstUptOperId;
    }
    public void setLstUptOperId(String lstUptOperId){
         this.lstUptOperId=lstUptOperId;
    }
    public Date getLstUptTime(){
         return lstUptTime;
    }
    
    public String getLstUptTimeStart() {
		return lstUptTimeStart;
	}
	public void setLstUptTimeStart(String lstUptTimeStart) {
		this.lstUptTimeStart = lstUptTimeStart;
	}
	public String getLstUptTimeEnd() {
		return lstUptTimeEnd;
	}
	public void setLstUptTimeEnd(String lstUptTimeEnd) {
		this.lstUptTimeEnd = lstUptTimeEnd;
	}
	public void setLstUptTime(Date lstUptTime){
         this.lstUptTime=lstUptTime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "seqNo="+seqNo+"\n";
        temp = temp + "custType="+custType+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "pactNo="+pactNo+"\n";
        temp = temp + "marginSign="+marginSign+"\n";
        temp = temp + "marginPayType="+marginPayType+"\n";
        temp = temp + "marginAmt="+marginAmt+"\n";
        temp = temp + "paidInAmt="+paidInAmt+"\n";
        temp = temp + "marginAc="+marginAc+"\n";
        temp = temp + "custProvisionAcNo="+custProvisionAcNo+"\n";
        temp = temp + "marginRaSign="+marginRaSign+"\n";
        temp = temp + "mark="+mark+"\n";
        temp = temp + "creOperId="+creOperId+"\n";
        temp = temp + "creTime="+creTime+"\n";
        temp = temp + "lstUptOperId="+lstUptOperId+"\n";
        temp = temp + "lstUptTime="+lstUptTime+"\n";
        temp = temp + "storeName="+storeName+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("seqNo",seqNo);
        json.put("custType",custType);
        json.put("custId",custId);
        json.put("pactNo",pactNo);
        json.put("marginSign",marginSign);
        json.put("marginPayType",marginPayType);
        json.put("marginAmt",String.format("%.2f",(float)marginAmt*0.01));
        json.put("paidInAmt",String.format("%.2f",(float)paidInAmt*0.01));
        json.put("marginAc",marginAc);
        json.put("custProvisionAcNo",custProvisionAcNo);
        json.put("marginRaSign",marginRaSign);
        json.put("mark",mark);
        json.put("creOperId",creOperId);
        json.put("storeName",storeName);
        try{
            json.put("creTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(creTime));
        } catch (Exception e) {}
        json.put("lstUptOperId",lstUptOperId);
        try{
            json.put("lstUptTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lstUptTime));
        } catch (Exception e) {}
        return json;
    }
}