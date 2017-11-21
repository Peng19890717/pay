package com.pay.risk.dao;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_RISK_DAY_TRAN_SUM entity.
 * 
 * @author administrator
 */
public class PayRiskDayTranSum {
    public String id;
    public String timeType;
    public String custType;
    public String custId;
//    public String userCode;
//    public String merNo;
    public String statDate;
    public Long allAmount=0l;
    public Long refundAmount=0l;
    public Long saleAmount=0l;
    public Long rechargeAmount=0l;
    public Long transferAmount=0l;
    public Long cashAmount=0l;
    public Long predepositAmount=0l;
    public Long allCount=0l;
    public Long refundCount;
    public Long saleCount=0l;
    public Long predepositCount=0l;
    public Long lessCount=0l;
    public Long creditCount=0l;
    public Long intCount=0l;
    public Long averAmount=0l;
    public Long maxAmount=0l;
    public Long allSuccessAmount=0l;
    public Long refundSuccessAmount=0l;
    public Long saleSuccessAmount=0l;
    public Long rechargeSuccessAmount=0l;
    public Long transferSuccessAmount=0l;
    public Long cashSuccessAmount=0l;
    public Long predepositSuccessAmount=0l;
    public Long allSuccessCount=0l;
    public Long refundSuccessCount=0l;
    public Long saleSuccessCount=0l;
    public Long predepositSuccessCount=0l;
    public Long lessSuccessCount=0l;
    public Long creditSuccessCount=0l;
    public Long intSuccessCount=0l;
    public Long transferCount=0l;
    public Long transferSuccessCount=0l;
    public Long cashCount=0l;
    public Long cashSuccessCount=0l;
    public Long saleClientAmt=0l;
    public Long saleClientCount=0l;
    public Long saleClientSuccessAmt=0l;
    public Long saleClientSuccessCount=0l;
    public Long saleQuickAmt=0l;
    public Long saleQuickCount=0l;
    public Long saleQuickSuccessAmt=0l;
    public Long saleQuickSuccessCount=0l;
    public Long saleAccAmt=0l;
    public Long saleAccCount=0l;
    public Long saleAccSuccessAmt=0l;
    public Long saleAccSuccessCount=0l;
    public Date cardLastTranTime=new Date();
    public Long rechargeCount=0l;
    public Long rechargeSuccessCount=0l;
    public Long receiveAmt=0l;
    public Long receiveCount=0l;
    public Long receiveSuccessAmt=0l;
    public Long receiveSuccessCount=0l;
    public Long payAmt=0l;
    public Long payCount=0l;
    public Long paySuccessAmt=0l;
    public Long paySuccessCount=0l;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getTimeType(){
         return timeType;
    }
    public void setTimeType(String timeType){
         this.timeType=timeType;
    }
    public String getCustType(){
         return custType;
    }
    public void setCustType(String custType){
         this.custType=custType;
    }
//    public String getUserCode(){
//         return userCode;
//    }
//    public void setUserCode(String userCode){
//         this.userCode=userCode;
//    }
//    public String getMerNo(){
//         return merNo;
//    }
//    public void setMerNo(String merNo){
//         this.merNo=merNo;
//    }
    public String getStatDate(){
         return statDate;
    }
    public void setStatDate(String statDate){
         this.statDate=statDate;
    }
    public Long getAllAmount(){
         return allAmount;
    }
    public void setAllAmount(Long allAmount){
         this.allAmount=allAmount;
    }
    public Long getRefundAmount(){
         return refundAmount;
    }
    public void setRefundAmount(Long refundAmount){
         this.refundAmount=refundAmount;
    }
    public Long getSaleAmount(){
         return saleAmount;
    }
    public void setSaleAmount(Long saleAmount){
         this.saleAmount=saleAmount;
    }
    public Long getRechargeAmount(){
         return rechargeAmount;
    }
    public void setRechargeAmount(Long rechargeAmount){
         this.rechargeAmount=rechargeAmount;
    }
    public Long getTransferAmount(){
         return transferAmount;
    }
    public void setTransferAmount(Long transferAmount){
         this.transferAmount=transferAmount;
    }
    public Long getCashAmount(){
         return cashAmount;
    }
    public void setCashAmount(Long cashAmount){
         this.cashAmount=cashAmount;
    }
    public Long getPredepositAmount(){
         return predepositAmount;
    }
    public void setPredepositAmount(Long predepositAmount){
         this.predepositAmount=predepositAmount;
    }
    public Long getAllCount(){
         return allCount;
    }
    public void setAllCount(Long allCount){
         this.allCount=allCount;
    }
    public Long getRefundCount(){
         return refundCount;
    }
    public void setRefundCount(Long refundCount){
         this.refundCount=refundCount;
    }
    public Long getSaleCount(){
         return saleCount;
    }
    public void setSaleCount(Long saleCount){
         this.saleCount=saleCount;
    }
    public Long getPredepositCount(){
         return predepositCount;
    }
    public void setPredepositCount(Long predepositCount){
         this.predepositCount=predepositCount;
    }
    public Long getLessCount(){
         return lessCount;
    }
    public void setLessCount(Long lessCount){
         this.lessCount=lessCount;
    }
    public Long getCreditCount(){
         return creditCount;
    }
    public void setCreditCount(Long creditCount){
         this.creditCount=creditCount;
    }
    public Long getIntCount(){
         return intCount;
    }
    public void setIntCount(Long intCount){
         this.intCount=intCount;
    }
    public Long getAverAmount(){
         return averAmount;
    }
    public void setAverAmount(Long averAmount){
         this.averAmount=averAmount;
    }
    public Long getMaxAmount(){
         return maxAmount;
    }
    public void setMaxAmount(Long maxAmount){
         this.maxAmount=maxAmount;
    }
    public Long getAllSuccessAmount(){
         return allSuccessAmount;
    }
    public void setAllSuccessAmount(Long allSuccessAmount){
         this.allSuccessAmount=allSuccessAmount;
    }
    public Long getRefundSuccessAmount(){
         return refundSuccessAmount;
    }
    public void setRefundSuccessAmount(Long refundSuccessAmount){
         this.refundSuccessAmount=refundSuccessAmount;
    }
    public Long getSaleSuccessAmount(){
         return saleSuccessAmount;
    }
    public void setSaleSuccessAmount(Long saleSuccessAmount){
         this.saleSuccessAmount=saleSuccessAmount;
    }
    public Long getRechargeSuccessAmount(){
         return rechargeSuccessAmount;
    }
    public void setRechargeSuccessAmount(Long rechargeSuccessAmount){
         this.rechargeSuccessAmount=rechargeSuccessAmount;
    }
    public Long getTransferSuccessAmount(){
         return transferSuccessAmount;
    }
    public void setTransferSuccessAmount(Long transferSuccessAmount){
         this.transferSuccessAmount=transferSuccessAmount;
    }
    public Long getCashSuccessAmount(){
         return cashSuccessAmount;
    }
    public void setCashSuccessAmount(Long cashSuccessAmount){
         this.cashSuccessAmount=cashSuccessAmount;
    }
    public Long getPredepositSuccessAmount(){
         return predepositSuccessAmount;
    }
    public void setPredepositSuccessAmount(Long predepositSuccessAmount){
         this.predepositSuccessAmount=predepositSuccessAmount;
    }
    public Long getAllSuccessCount(){
         return allSuccessCount;
    }
    public void setAllSuccessCount(Long allSuccessCount){
         this.allSuccessCount=allSuccessCount;
    }
    public Long getRefundSuccessCount(){
         return refundSuccessCount;
    }
    public void setRefundSuccessCount(Long refundSuccessCount){
         this.refundSuccessCount=refundSuccessCount;
    }
    public Long getSaleSuccessCount(){
         return saleSuccessCount;
    }
    public void setSaleSuccessCount(Long saleSuccessCount){
         this.saleSuccessCount=saleSuccessCount;
    }
    public Long getPredepositSuccessCount(){
         return predepositSuccessCount;
    }
    public void setPredepositSuccessCount(Long predepositSuccessCount){
         this.predepositSuccessCount=predepositSuccessCount;
    }
    public Long getLessSuccessCount(){
         return lessSuccessCount;
    }
    public void setLessSuccessCount(Long lessSuccessCount){
         this.lessSuccessCount=lessSuccessCount;
    }
    public Long getCreditSuccessCount(){
         return creditSuccessCount;
    }
    public void setCreditSuccessCount(Long creditSuccessCount){
         this.creditSuccessCount=creditSuccessCount;
    }
    public Long getIntSuccessCount(){
         return intSuccessCount;
    }
    public void setIntSuccessCount(Long intSuccessCount){
         this.intSuccessCount=intSuccessCount;
    }
    public Long getTransferCount() {
		return transferCount;
	}
	public void setTransferCount(Long transferCount) {
		this.transferCount = transferCount;
	}
	public Long getTransferSuccessCount() {
		return transferSuccessCount;
	}
	public void setTransferSuccessCount(Long transferSuccessCount) {
		this.transferSuccessCount = transferSuccessCount;
	}
	
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public Long getCashCount() {
		return cashCount;
	}
	public void setCashCount(Long cashCount) {
		this.cashCount = cashCount;
	}
	public Long getCashSuccessCount() {
		return cashSuccessCount;
	}
	public void setCashSuccessCount(Long cashSuccessCount) {
		this.cashSuccessCount = cashSuccessCount;
	}
    public Long getSaleClientAmt(){
        return saleClientAmt;
   }
   public void setSaleClientAmt(Long saleClientAmt){
        this.saleClientAmt=saleClientAmt;
   }
   public Long getSaleClientCount(){
        return saleClientCount;
   }
   public void setSaleClientCount(Long saleClientCount){
        this.saleClientCount=saleClientCount;
   }
   public Long getSaleClientSuccessAmt(){
        return saleClientSuccessAmt;
   }
   public void setSaleClientSuccessAmt(Long saleClientSuccessAmt){
        this.saleClientSuccessAmt=saleClientSuccessAmt;
   }
   public Long getSaleClientSuccessCount(){
        return saleClientSuccessCount;
   }
   public void setSaleClientSuccessCount(Long saleClientSuccessCount){
        this.saleClientSuccessCount=saleClientSuccessCount;
   }
   public Long getSaleQuickAmt(){
        return saleQuickAmt;
   }
   public void setSaleQuickAmt(Long saleQuickAmt){
        this.saleQuickAmt=saleQuickAmt;
   }
   public Long getSaleQuickCount(){
        return saleQuickCount;
   }
   public void setSaleQuickCount(Long saleQuickCount){
        this.saleQuickCount=saleQuickCount;
   }
   public Long getSaleQuickSuccessAmt(){
        return saleQuickSuccessAmt;
   }
   public void setSaleQuickSuccessAmt(Long saleQuickSuccessAmt){
        this.saleQuickSuccessAmt=saleQuickSuccessAmt;
   }
   public Long getSaleQuickSuccessCount(){
        return saleQuickSuccessCount;
   }
   public void setSaleQuickSuccessCount(Long saleQuickSuccessCount){
        this.saleQuickSuccessCount=saleQuickSuccessCount;
   }
   
   public Long getSaleAccAmt() {
		return saleAccAmt;
	}
	public void setSaleAccAmt(Long saleAccAmt) {
		this.saleAccAmt = saleAccAmt;
	}
	public Long getSaleAccCount() {
		return saleAccCount;
	}
	public void setSaleAccCount(Long saleAccCount) {
		this.saleAccCount = saleAccCount;
	}
	public Long getSaleAccSuccessAmt() {
		return saleAccSuccessAmt;
	}
	public void setSaleAccSuccessAmt(Long saleAccSuccessAmt) {
		this.saleAccSuccessAmt = saleAccSuccessAmt;
	}
	public Long getSaleAccSuccessCount() {
		return saleAccSuccessCount;
	}
	public void setSaleAccSuccessCount(Long saleAccSuccessCount) {
		this.saleAccSuccessCount = saleAccSuccessCount;
	}
	
	public Date getCardLastTranTime() {
		return cardLastTranTime;
	}
	public void setCardLastTranTime(Date cardLastTranTime) {
		this.cardLastTranTime = cardLastTranTime;
	}
	public Long getRechargeCount() {
		return rechargeCount;
	}
	public void setRechargeCount(Long rechargeCount) {
		this.rechargeCount = rechargeCount;
	}
	public Long getRechargeSuccessCount() {
		return rechargeSuccessCount;
	}
	public void setRechargeSuccessCount(Long rechargeSuccessCount) {
		this.rechargeSuccessCount = rechargeSuccessCount;
	}
	
	public Long getReceiveAmt() {
		return receiveAmt;
	}
	public void setReceiveAmt(Long receiveAmt) {
		this.receiveAmt = receiveAmt;
	}
	public Long getReceiveCount() {
		return receiveCount;
	}
	public void setReceiveCount(Long receiveCount) {
		this.receiveCount = receiveCount;
	}
	public Long getReceiveSuccessAmt() {
		return receiveSuccessAmt;
	}
	public void setReceiveSuccessAmt(Long receiveSuccessAmt) {
		this.receiveSuccessAmt = receiveSuccessAmt;
	}
	public Long getReceiveSuccessCount() {
		return receiveSuccessCount;
	}
	public void setReceiveSuccessCount(Long receiveSuccessCount) {
		this.receiveSuccessCount = receiveSuccessCount;
	}
	public Long getPayAmt() {
		return payAmt;
	}
	public void setPayAmt(Long payAmt) {
		this.payAmt = payAmt;
	}
	public Long getPayCount() {
		return payCount;
	}
	public void setPayCount(Long payCount) {
		this.payCount = payCount;
	}
	public Long getPaySuccessAmt() {
		return paySuccessAmt;
	}
	public void setPaySuccessAmt(Long paySuccessAmt) {
		this.paySuccessAmt = paySuccessAmt;
	}
	public Long getPaySuccessCount() {
		return paySuccessCount;
	}
	public void setPaySuccessCount(Long paySuccessCount) {
		this.paySuccessCount = paySuccessCount;
	}
	public String toString(){
       String temp = "";
       temp=temp+"id="+id+"\n";
       temp=temp+"timeType="+timeType+"\n";
       temp=temp+"custType="+custType+"\n";
       temp=temp+"custId="+custId+"\n";
       temp=temp+"statDate="+statDate+"\n";
       temp=temp+"allAmount="+allAmount+"\n";
       temp=temp+"refundAmount="+refundAmount+"\n";
       temp=temp+"saleAmount="+saleAmount+"\n";
       temp=temp+"rechargeAmount="+rechargeAmount+"\n";
       temp=temp+"transferAmount="+transferAmount+"\n";
       temp=temp+"cashAmount="+cashAmount+"\n";
       temp=temp+"predepositAmount="+predepositAmount+"\n";
       temp=temp+"allCount="+allCount+"\n";
       temp=temp+"refundCount="+refundCount+"\n";
       temp=temp+"saleCount="+saleCount+"\n";
       temp=temp+"predepositCount="+predepositCount+"\n";
       temp=temp+"lessCount="+lessCount+"\n";
       temp=temp+"creditCount="+creditCount+"\n";
       temp=temp+"intCount="+intCount+"\n";
       temp=temp+"averAmount="+averAmount+"\n";
       temp=temp+"maxAmount="+maxAmount+"\n";
       temp=temp+"allSuccessAmount="+allSuccessAmount+"\n";
       temp=temp+"refundSuccessAmount="+refundSuccessAmount+"\n";
       temp=temp+"saleSuccessAmount="+saleSuccessAmount+"\n";
       temp=temp+"rechargeSuccessAmount="+rechargeSuccessAmount+"\n";
       temp=temp+"transferSuccessAmount="+transferSuccessAmount+"\n";
       temp=temp+"cashSuccessAmount="+cashSuccessAmount+"\n";
       temp=temp+"predepositSuccessAmount="+predepositSuccessAmount+"\n";
       temp=temp+"allSuccessCount="+allSuccessCount+"\n";
       temp=temp+"refundSuccessCount="+refundSuccessCount+"\n";
       temp=temp+"saleSuccessCount="+saleSuccessCount+"\n";
       temp=temp+"predepositSuccessCount="+predepositSuccessCount+"\n";
       temp=temp+"lessSuccessCount="+lessSuccessCount+"\n";
       temp=temp+"creditSuccessCount="+creditSuccessCount+"\n";
       temp=temp+"intSuccessCount="+intSuccessCount+"\n";
       temp=temp+"transferCount="+transferCount+"\n";
       temp=temp+"transferSuccessCount="+transferSuccessCount+"\n";
       temp=temp+"cashCount="+cashCount+"\n";
       temp=temp+"cashSuccessCount="+cashSuccessCount+"\n";
       temp=temp+"saleClientAmt="+saleClientAmt+"\n";
       temp=temp+"saleClientCount="+saleClientCount+"\n";
       temp=temp+"saleClientSuccessAmt="+saleClientSuccessAmt+"\n";
       temp=temp+"saleClientSuccessCount="+saleClientSuccessCount+"\n";
       temp=temp+"saleQuickAmt="+saleQuickAmt+"\n";
       temp=temp+"saleQuickCount="+saleQuickCount+"\n";
       temp=temp+"saleQuickSuccessAmt="+saleQuickSuccessAmt+"\n";
       temp=temp+"saleQuickSuccessCount="+saleQuickSuccessCount+"\n";
       temp=temp+"saleAccAmt="+saleAccAmt+"\n";
       temp=temp+"saleAccCount="+saleAccCount+"\n";
       temp=temp+"saleAccSuccessAmt="+saleAccSuccessAmt+"\n";
       temp=temp+"saleAccSuccessCount="+saleAccSuccessCount+"\n";
       temp=temp+"cardLastTranTime="+cardLastTranTime+"\n";
       temp=temp+"rechargeCount="+rechargeCount+"\n";
       temp=temp+"rechargeSuccessCount="+rechargeSuccessCount+"\n";
       temp=temp+"receiveAmt="+receiveAmt+"\n";
       temp=temp+"receiveCount="+receiveCount+"\n";
       temp=temp+"receiveSuccessAmt="+receiveSuccessAmt+"\n";
       temp=temp+"receiveSuccessCount="+receiveSuccessCount+"\n";
       temp=temp+"payAmt="+payAmt+"\n";
       temp=temp+"payCount="+payCount+"\n";
       temp=temp+"paySuccessAmt="+paySuccessAmt+"\n";
       temp=temp+"paySuccessCount="+paySuccessCount+"\n";
       return temp;
   }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("timeType",timeType);
        json.put("custType",custType);
//        json.put("userCode",userCode);
        json.put("statDate",statDate);
        json.put("allAmount",String.valueOf(allAmount));
        json.put("refundAmount",String.valueOf(refundAmount));
        json.put("saleAmount",String.valueOf(saleAmount));
        json.put("rechargeAmount",String.valueOf(rechargeAmount));
        json.put("transferAmount",String.valueOf(transferAmount));
        json.put("cashAmount",String.valueOf(cashAmount));
        json.put("predepositAmount",String.valueOf(predepositAmount));
        json.put("allCount",String.valueOf(allCount));
        json.put("refundCount",String.valueOf(refundCount));
        json.put("saleCount",String.valueOf(saleCount));
        json.put("predepositCount",String.valueOf(predepositCount));
        json.put("lessCount",String.valueOf(lessCount));
        json.put("creditCount",String.valueOf(creditCount));
        json.put("intCount",String.valueOf(intCount));
        json.put("averAmount",String.valueOf(averAmount));
        json.put("maxAmount",String.valueOf(maxAmount));
        json.put("allSuccessAmount",String.valueOf(allSuccessAmount));
        json.put("refundSuccessAmount",String.valueOf(refundSuccessAmount));
        json.put("saleSuccessAmount",String.valueOf(saleSuccessAmount));
        json.put("rechargeSuccessAmount",String.valueOf(rechargeSuccessAmount));
        json.put("transferSuccessAmount",String.valueOf(transferSuccessAmount));
        json.put("cashSuccessAmount",String.valueOf(cashSuccessAmount));
        json.put("predepositSuccessAmount",String.valueOf(predepositSuccessAmount));
        json.put("allSuccessCount",String.valueOf(allSuccessCount));
        json.put("refundSuccessCount",String.valueOf(refundSuccessCount));
        json.put("saleSuccessCount",String.valueOf(saleSuccessCount));
        json.put("predepositSuccessCount",String.valueOf(predepositSuccessCount));
        json.put("lessSuccessCount",String.valueOf(lessSuccessCount));
        json.put("creditSuccessCount",String.valueOf(creditSuccessCount));
        json.put("intSuccessCount",String.valueOf(intSuccessCount));
        json.put("transferCount",String.valueOf(transferCount));
        json.put("transferSuccessCount",String.valueOf(transferSuccessCount));
        json.put("cashCount",String.valueOf(cashCount));
        json.put("cashSuccessCount",String.valueOf(cashSuccessCount));
//        json.put("merNo",merNo);
        json.put("custId",custId);
        json.put("saleClientAmt",String.valueOf(saleClientAmt));
        json.put("saleClientCount",String.valueOf(saleClientCount));
        json.put("saleClientSuccessAmt",String.valueOf(saleClientSuccessAmt));
        json.put("saleClientSuccessCount",String.valueOf(saleClientSuccessCount));
        json.put("saleQuickAmt",String.valueOf(saleQuickAmt));
        json.put("saleQuickCount",String.valueOf(saleQuickCount));
        json.put("saleQuickSuccessAmt",String.valueOf(saleQuickSuccessAmt));
        json.put("saleQuickSuccessCount",String.valueOf(saleQuickSuccessCount));
        json.put("saleAccAmt",saleAccAmt);
        json.put("saleAccCount",String.valueOf(saleAccCount));
        json.put("saleAccSuccessAmt",saleAccSuccessAmt);
        json.put("saleAccSuccessCount",String.valueOf(saleAccSuccessCount));
        json.put("rechargeCount",String.valueOf(rechargeCount));
        json.put("rechargeSuccessCount",String.valueOf(rechargeSuccessCount));
        json.put("receiveAmt",String.valueOf(receiveAmt));
        json.put("receiveCount",String.valueOf(receiveCount));
        json.put("receiveSuccessAmt",String.valueOf(receiveSuccessAmt));
        json.put("receiveSuccessCount",String.valueOf(receiveSuccessCount));
        json.put("payAmt",String.valueOf(payAmt));
        json.put("payCount",String.valueOf(payCount));
        json.put("paySuccessAmt",String.valueOf(paySuccessAmt));
        json.put("paySuccessCount",String.valueOf(paySuccessCount));
        return json;
    }
}