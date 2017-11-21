package com.pay.marginrcv.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_MARGIN_RCV_DET entity.
 * 
 * @author administrator
 */
public class PayMarginRcvDet {
    public String seqNo;
    public String custId;
    public Long paidInAmt;
    public String marginAc;
    public String custProvisionAcNo;
    public Date marginRcvTime;
    public String mark;
    public Long marginCurAmt;
    public String pactNo;
    
    // 保证金搜索开始时间点
    public String marginRcvTimeStart;
    // 保证金搜索结束时间点
    public String marginRcvTimeEnd;
    
    public String getMarginRcvTimeStart() {
		return marginRcvTimeStart;
	}
	public void setMarginRcvTimeStart(String marginRcvTimeStart) {
		this.marginRcvTimeStart = marginRcvTimeStart;
	}
    public String getMarginRcvTimeEnd() {
		return marginRcvTimeEnd;
	}
	public void setMarginRcvTimeEnd(String marginRcvTimeEnd) {
		this.marginRcvTimeEnd = marginRcvTimeEnd;
	}
	
	public String getSeqNo(){
         return seqNo;
    }
    public void setSeqNo(String seqNo){
         this.seqNo=seqNo;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
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
    public Date getMarginRcvTime(){
         return marginRcvTime;
    }
    public void setMarginRcvTime(Date marginRcvTime){
         this.marginRcvTime=marginRcvTime;
    }
    public String getMark(){
         return mark;
    }
    public void setMark(String mark){
         this.mark=mark;
    }
    public Long getMarginCurAmt(){
         return marginCurAmt;
    }
    public void setMarginCurAmt(Long marginCurAmt){
         this.marginCurAmt=marginCurAmt;
    }
    public String getPactNo(){
         return pactNo;
    }
    public void setPactNo(String pactNo){
         this.pactNo=pactNo;
    }
    public String toString(){
        String temp = "";
        temp = temp + "seqNo="+seqNo+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "paidInAmt="+paidInAmt+"\n";
        temp = temp + "marginAc="+marginAc+"\n";
        temp = temp + "custProvisionAcNo="+custProvisionAcNo+"\n";
        temp = temp + "marginRcvTime="+marginRcvTime+"\n";
        temp = temp + "mark="+mark+"\n";
        temp = temp + "marginCurAmt="+marginCurAmt+"\n";
        temp = temp + "pactNo="+pactNo+"\n";
        temp = temp + "marginRcvTimeStart="+marginRcvTimeStart+"\n";
        temp = temp + "marginRcvTimeEnd="+marginRcvTimeEnd+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("seqNo",seqNo);
        json.put("custId",custId);
        json.put("paidInAmt",String.format("%.2f",(float)paidInAmt*0.01));
        json.put("marginAc",marginAc);
        json.put("custProvisionAcNo",custProvisionAcNo);
        try{
            json.put("marginRcvTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(marginRcvTime));
        } catch (Exception e) {}
        json.put("mark",mark);
        json.put("marginCurAmt",String.format("%.2f",(float)marginCurAmt*0.01));
        json.put("pactNo",pactNo);
        json.put("marginRcvTimeStart",marginRcvTimeStart);
        json.put("marginRcvTimeEnd",marginRcvTimeEnd);
        return json;
    }
}