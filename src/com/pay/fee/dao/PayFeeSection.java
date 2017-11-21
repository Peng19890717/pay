package com.pay.fee.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_FEE_SECTION entity.
 * 
 * @author administrator
 */
public class PayFeeSection {
    public String seqNo;
    public String feeCode;
    public Long maxAmt;
    public Long feeAmt;
    public Long feeRatio;
    public String getSeqNo(){
         return seqNo;
    }
    public void setSeqNo(String seqNo){
         this.seqNo=seqNo;
    }
    public String getFeeCode(){
         return feeCode;
    }
    public void setFeeCode(String feeCode){
         this.feeCode=feeCode;
    }
    public long getMaxAmt(){
         return maxAmt;
    }
    public void setMaxAmt(long maxAmt){
         this.maxAmt=maxAmt;
    }
    public long getFeeAmt(){
         return feeAmt;
    }
    public void setFeeAmt(long feeAmt){
         this.feeAmt=feeAmt;
    }
    public long getFeeRatio(){
         return feeRatio;
    }
    public void setFeeRatio(long feeRatio){
         this.feeRatio=feeRatio;
    }
    public String toString(){
        String temp = "";
        temp = temp + "seqNo="+seqNo+"\n";
        temp = temp + "feeCode="+feeCode+"\n";
        temp = temp + "maxAmt="+maxAmt+"\n";
        temp = temp + "feeAmt="+feeAmt+"\n";
        temp = temp + "feeRatio="+feeRatio+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("seqNo",seqNo);
        json.put("feeCode",feeCode);
        json.put("maxAmt",String.valueOf(maxAmt));
        json.put("feeAmt",String.valueOf(feeAmt));
        json.put("feeRatio",String.valueOf(feeRatio));
        return json;
    }
}