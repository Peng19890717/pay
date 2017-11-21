package com.pay.settlement.dao;

import java.util.Date;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.PayConstant;
import com.jweb.dao.JWebUser;
import com.pay.custstl.service.PayCustStlInfoService;
import com.pay.merchant.dao.PayMerchant;
/**
 * Table PAY_MERCHANT_SETTLEMENT entity.
 * 
 * @author administrator
 */
public class PayMerchantSettlement {
    public String stlId;
    public String stlMerId;
    public String storeName;
    public String stlMerCustno;
    public String stlMerPayacno;
    public String stlStltype;
    public Long stlApplystlamt=0l;
    public Long stlRefundAmt=0l;
    public Date stlFromTime;
    public Date stlToTime;
    public String stlStatus;
    public String searchCustSetPeriod;
    public String stlApplicants;
    public Date stlApplTime;
    public String stlApplIp;
    public String stlAuditPer;
    public Date stlAuditTime;
    public String stlAuditIp;
    public Date stlSucTime;
    public String stlSucOperator;
    public String stlSeqno;
    public String stlType;
    public Long stlFee=0l;
    public Long stlTotalordamt=0l;
    public Long stlTxncomamt=0l;
    public Long stlNetrecamt=0l;
    public Date stlApplDate;
    public String stlApplDateStart;
    public String stlApplDateEnd;
    public String stlAutoFlag;
    public Long stlApplystlCount=0l;
    public Long stlRefundCount=0l;
    public String remark;
    public PayMerchant merchant = new PayMerchant();
    public String stlFeeCode;
    public String stlFeeName;
    public String stlPeriod="";
    public String stlPeriodDaySet="";
    public String settlementWay;
    public Long agentStlIn=0l;
    public Long agentStlOut=0l;
    public String stlPeriodAgent="";
    public String stlPeriodDaySetAgent="";
    public Date stlFromTimeAgent;
    public Date stlToTimeAgent;
    public String chargeWay="";
    public Long receiveCount=0l;
    public Long receiveAmt=0l;
    public Long receiveFee=0l;
    public String stlPeriodReceive="";
    public Date stlFromTimeReceive;
    public Date stlToTimeReceive;
    public String stlPeriodSetReceive="";
    public Long stlAmtOver=0l;
    public Long stlAmtFeeOver=0l;
    public String getStlId(){
         return stlId;
    }
    public void setStlId(String stlId){
         this.stlId=stlId;
    }
    public String getStlMerId(){
         return stlMerId;
    }
    public void setStlMerId(String stlMerId){
         this.stlMerId=stlMerId;
    }
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStlMerCustno(){
         return stlMerCustno;
    }
    public void setStlMerCustno(String stlMerCustno){
         this.stlMerCustno=stlMerCustno;
    }
    public String getStlMerPayacno(){
         return stlMerPayacno;
    }
    public void setStlMerPayacno(String stlMerPayacno){
         this.stlMerPayacno=stlMerPayacno;
    }
    public String getStlStltype(){
         return stlStltype;
    }
    public void setStlStltype(String stlStltype){
         this.stlStltype=stlStltype;
    }
    public Long getStlApplystlamt(){
         return stlApplystlamt;
    }
    public void setStlApplystlamt(Long stlApplystlamt){
         this.stlApplystlamt=stlApplystlamt;
    }
    public Long getStlRefundAmt(){
         return stlRefundAmt;
    }
    public void setStlRefundAmt(Long stlRefundAmt){
         this.stlRefundAmt=stlRefundAmt;
    }
    public Date getStlFromTime(){
         return stlFromTime;
    }
    public void setStlFromTime(Date stlFromTime){
         this.stlFromTime=stlFromTime;
    }
    public Date getStlToTime(){
         return stlToTime;
    }
    public void setStlToTime(Date stlToTime){
         this.stlToTime=stlToTime;
    }
    public String getStlStatus(){
         return stlStatus;
    }
    public void setStlStatus(String stlStatus){
         this.stlStatus=stlStatus;
    }
    public String getSearchCustSetPeriod() {
		return searchCustSetPeriod;
	}
	public void setSearchCustSetPeriod(String searchCustSetPeriod) {
		this.searchCustSetPeriod = searchCustSetPeriod;
	}
	public String getStlApplDateStart() {
		return stlApplDateStart;
	}
	public void setStlApplDateStart(String stlApplDateStart) {
		this.stlApplDateStart = stlApplDateStart;
	}
	public String getStlApplDateEnd() {
		return stlApplDateEnd;
	}
	public void setStlApplDateEnd(String stlApplDateEnd) {
		this.stlApplDateEnd = stlApplDateEnd;
	}
	public String getStlApplicants(){
         return stlApplicants;
    }
    public void setStlApplicants(String stlApplicants){
         this.stlApplicants=stlApplicants;
    }
    public Date getStlApplTime(){
         return stlApplTime;
    }
    public void setStlApplTime(Date stlApplTime){
         this.stlApplTime=stlApplTime;
    }
    public String getStlApplIp(){
         return stlApplIp;
    }
    public void setStlApplIp(String stlApplIp){
         this.stlApplIp=stlApplIp;
    }
    public String getStlAuditPer(){
         return stlAuditPer;
    }
    public void setStlAuditPer(String stlAuditPer){
         this.stlAuditPer=stlAuditPer;
    }
    public Date getStlAuditTime(){
         return stlAuditTime;
    }
    public void setStlAuditTime(Date stlAuditTime){
         this.stlAuditTime=stlAuditTime;
    }
    public String getStlAuditIp(){
         return stlAuditIp;
    }
    public void setStlAuditIp(String stlAuditIp){
         this.stlAuditIp=stlAuditIp;
    }
    public Date getStlSucTime(){
         return stlSucTime;
    }
    public void setStlSucTime(Date stlSucTime){
         this.stlSucTime=stlSucTime;
    }
    public String getStlSucOperator() {
		return stlSucOperator;
	}
	public void setStlSucOperator(String stlSucOperator) {
		this.stlSucOperator = stlSucOperator;
	}
	public String getStlSeqno(){
         return stlSeqno;
    }
    public void setStlSeqno(String stlSeqno){
         this.stlSeqno=stlSeqno;
    }
    public String getStlType(){
         return stlType;
    }
    public void setStlType(String stlType){
         this.stlType=stlType;
    }
    public Long getStlFee(){
         return stlFee;
    }
    public void setStlFee(Long stlFee){
         this.stlFee=stlFee;
    }
    public Long getStlTotalordamt(){
         return stlTotalordamt;
    }
    public void setStlTotalordamt(Long stlTotalordamt){
         this.stlTotalordamt=stlTotalordamt;
    }
    public Long getStlTxncomamt(){
         return stlTxncomamt;
    }
    public void setStlTxncomamt(Long stlTxncomamt){
         this.stlTxncomamt=stlTxncomamt;
    }
    public Long getStlNetrecamt(){
         return stlNetrecamt;
    }
    public void setStlNetrecamt(Long stlNetrecamt){
         this.stlNetrecamt=stlNetrecamt;
    }
    public Date getStlApplDate(){
         return stlApplDate;
    }
    public void setStlApplDate(Date stlApplDate){
         this.stlApplDate=stlApplDate;
    }
    public String getStlAutoFlag(){
         return stlAutoFlag;
    }
    public void setStlAutoFlag(String stlAutoFlag){
         this.stlAutoFlag=stlAutoFlag;
    }
    public Long getStlApplystlCount(){
         return stlApplystlCount;
    }
    public void setStlApplystlCount(Long stlApplystlCount){
         this.stlApplystlCount=stlApplystlCount;
    }
    public Long getStlRefundCount(){
         return stlRefundCount;
    }
    public void setStlRefundCount(Long stlRefundCount){
         this.stlRefundCount=stlRefundCount;
    }
    public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    public String getStlFeeCode(){
         return stlFeeCode;
    }
    public void setStlFeeCode(String stlFeeCode){
         this.stlFeeCode=stlFeeCode;
    }
    
    public String getStlFeeName() {
		return stlFeeName;
	}
	public void setStlFeeName(String stlFeeName) {
		this.stlFeeName = stlFeeName;
	}
	public String getStlPeriod(){
         return stlPeriod;
    }
    public void setStlPeriod(String stlPeriod){
         this.stlPeriod=stlPeriod;
    }
    public String getStlPeriodDaySet(){
         return stlPeriodDaySet;
    }
    public void setStlPeriodDaySet(String stlPeriodDaySet){
         this.stlPeriodDaySet=stlPeriodDaySet;
	}
    
	public String getSettlementWay() {
		return settlementWay;
	}
	public void setSettlementWay(String settlementWay) {
		this.settlementWay = settlementWay;
	}
	
	public Long getAgentStlIn() {
		return agentStlIn;
	}
	public void setAgentStlIn(Long agentStlIn) {
		this.agentStlIn = agentStlIn;
	}
	public Long getAgentStlOut() {
		return agentStlOut;
	}
	public void setAgentStlOut(Long agentStlOut) {
		this.agentStlOut = agentStlOut;
	}
	
	public String getStlPeriodAgent() {
		return stlPeriodAgent;
	}
	public void setStlPeriodAgent(String stlPeriodAgent) {
		this.stlPeriodAgent = stlPeriodAgent;
	}
	public String getStlPeriodDaySetAgent() {
		return stlPeriodDaySetAgent;
	}
	public void setStlPeriodDaySetAgent(String stlPeriodDaySetAgent) {
		this.stlPeriodDaySetAgent = stlPeriodDaySetAgent;
	}
	
	public Date getStlFromTimeAgent() {
		return stlFromTimeAgent;
	}
	public void setStlFromTimeAgent(Date stlFromTimeAgent) {
		this.stlFromTimeAgent = stlFromTimeAgent;
	}
	public Date getStlToTimeAgent() {
		return stlToTimeAgent;
	}
	public void setStlToTimeAgent(Date stlToTimeAgent) {
		this.stlToTimeAgent = stlToTimeAgent;
	}
	
	public String getChargeWay() {
		return chargeWay;
	}
	public void setChargeWay(String chargeWay) {
		this.chargeWay = chargeWay;
	}
    public Long getReceiveCount(){
        return receiveCount;
   }
   public void setReceiveCount(Long receiveCount){
        this.receiveCount=receiveCount;
   }
   public Long getReceiveAmt(){
        return receiveAmt;
   }
   public void setReceiveAmt(Long receiveAmt){
        this.receiveAmt=receiveAmt;
   }
   public Long getReceiveFee(){
        return receiveFee;
   }
   public void setReceiveFee(Long receiveFee){
        this.receiveFee=receiveFee;
   }
   
	public String getStlPeriodReceive() {
		return stlPeriodReceive;
	}
	public void setStlPeriodReceive(String stlPeriodReceive) {
		this.stlPeriodReceive = stlPeriodReceive;
	}
	public Date getStlFromTimeReceive() {
		return stlFromTimeReceive;
	}
	public void setStlFromTimeReceive(Date stlFromTimeReceive) {
		this.stlFromTimeReceive = stlFromTimeReceive;
	}
	public Date getStlToTimeReceive() {
		return stlToTimeReceive;
	}
	public void setStlToTimeReceive(Date stlToTimeReceive) {
		this.stlToTimeReceive = stlToTimeReceive;
	}
	public String getStlPeriodSetReceive() {
		return stlPeriodSetReceive;
	}
	public void setStlPeriodSetReceive(String stlPeriodSetReceive) {
		this.stlPeriodSetReceive = stlPeriodSetReceive;
	}
	public Long getStlAmtOver() {
		return stlAmtOver;
	}
	public void setStlAmtOver(Long stlAmtOver) {
		this.stlAmtOver = stlAmtOver;
	}
	
	public Long getStlAmtFeeOver() {
		return stlAmtFeeOver;
	}
	public void setStlAmtFeeOver(Long stlAmtFeeOver) {
		this.stlAmtFeeOver = stlAmtFeeOver;
	}
	public String toString(){
        String temp = "";
        temp = temp + "stlId="+stlId+"\n";
        temp = temp + "stlMerId="+stlMerId+"\n";
        temp = temp + "stlMerCustno="+stlMerCustno+"\n";
        temp = temp + "stlMerPayacno="+stlMerPayacno+"\n";
        temp = temp + "stlStltype="+stlStltype+"\n";
        temp = temp + "stlApplystlamt="+stlApplystlamt+"\n";
        temp = temp + "stlRefundAmt="+stlRefundAmt+"\n";
        temp = temp + "stlFromTime="+stlFromTime+"\n";
        temp = temp + "stlToTime="+stlToTime+"\n";
        temp = temp + "stlStatus="+stlStatus+"\n";
        temp = temp + "stlApplicants="+stlApplicants+"\n";
        temp = temp + "stlApplTime="+stlApplTime+"\n";
        temp = temp + "stlApplIp="+stlApplIp+"\n";
        temp = temp + "stlAuditPer="+stlAuditPer+"\n";
        temp = temp + "stlAuditTime="+stlAuditTime+"\n";
        temp = temp + "stlAuditIp="+stlAuditIp+"\n";
        temp = temp + "stlSucTime="+stlSucTime+"\n";
        temp = temp + "stlSeqno="+stlSeqno+"\n";
        temp = temp + "stlType="+stlType+"\n";
        temp = temp + "stlFee="+stlFee+"\n";
        temp = temp + "stlTotalordamt="+stlTotalordamt+"\n";
        temp = temp + "stlTxncomamt="+stlTxncomamt+"\n";
        temp = temp + "stlNetrecamt="+stlNetrecamt+"\n";
        temp = temp + "stlApplDate="+stlApplDate+"\n";
        temp = temp + "stlAutoFlag="+stlAutoFlag+"\n";
        temp = temp + "stlApplystlCount="+stlApplystlCount+"\n";
        temp = temp + "stlRefundCount="+stlRefundCount+"\n";
        temp = temp + "stlSucOperator="+stlSucOperator+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "stlFeeCode="+stlFeeCode+"\n";
        temp = temp + "stlFeeName="+stlFeeName+"\n";
        temp = temp + "stlPeriod="+stlPeriod+"\n";
        temp = temp + "stlPeriodDaySet="+stlPeriodDaySet+"\n";
        return temp;
    }
    public JSONObject toJson(Map sysUserMap) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("stlId",stlId);
        json.put("stlMerId",stlMerId);
        json.put("storeName",merchant.storeName);
        String tmp = PayConstant.MER_STL_PERIOD.get(merchant.payCustStlInfo.custSetPeriod);
        json.put("custSetPeriod",tmp == null?"":tmp);
//        json.put("custStlTimeSet","D".equals(merchant.payCustStlInfo.custSetPeriod)?"T+"+merchant.payCustStlInfo.custSetFrey:
//        		PayCustStlInfoService.getShowInfoByStlPeriod(merchant.payCustStlInfo.custSetPeriod,
//        				merchant.payCustStlInfo.custStlTimeSet));
        json.put("depositBankCode",merchant.payCustStlInfo.depositBankCode);
        json.put("depositBankBrchName",merchant.payCustStlInfo.depositBankBrchName);
        json.put("custBankDepositName",merchant.payCustStlInfo.custBankDepositName);
        json.put("custStlBankAcNo",merchant.payCustStlInfo.custStlBankAcNo);
        json.put("stlApplystlamt",String.format("%.2f",((double)stlApplystlamt)*0.01));
        json.put("stlRefundAmt",String.format("%.2f",((double)stlRefundAmt)*0.01));
        try{
            json.put("stlFromTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlFromTime));
        } catch (Exception e) {}
        try{
            json.put("stlToTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlToTime));
        } catch (Exception e) {}
        json.put("stlStatus",stlStatus);
        JWebUser user = (JWebUser)sysUserMap.get(stlApplicants);
        json.put("stlApplicants",user == null?"":user.name);
        try{
            json.put("stlApplTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlApplTime));
        } catch (Exception e) {}
        json.put("stlApplIp",stlApplIp);
        user = (JWebUser)sysUserMap.get(stlAuditPer);
        json.put("stlAuditPer",user == null?"":user.name);
        try{
            json.put("stlAuditTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlAuditTime));
        } catch (Exception e) {}
        json.put("stlAuditIp",stlAuditIp);
        try{
            json.put("stlSucTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlSucTime));
        } catch (Exception e) {}
        user = (JWebUser)sysUserMap.get(stlSucOperator);
        json.put("stlSucOperator",user == null?"":user.name);
        json.put("stlSeqno",stlSeqno);
        json.put("stlType",stlType);
        json.put("stlTranFee",String.format("%.2f",((double)(stlFee-receiveFee))/100d));
        json.put("stlFee",String.format("%.2f",((double)stlFee)/100d));
        json.put("stlTotalordamt",String.format("%.2f",((double)stlTotalordamt)/100d));
        json.put("stlTxncomamt",String.format("%.2f",((double)stlTxncomamt)/100d));
        json.put("stlNetrecamt",String.format("%.2f",((double)stlNetrecamt)/100d));
        try{
            json.put("stlApplDate", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlApplDate));
        } catch (Exception e) {}
        json.put("stlAutoFlag","0".equals(stlAutoFlag)?"自动":"手动");
        json.put("stlApplystlCount",String.valueOf(stlApplystlCount));
        json.put("stlRefundCount",String.valueOf(stlRefundCount));
        json.put("remark",remark);
		json.put("stlFeeCode",stlFeeCode);
		json.put("stlFeeName",stlFeeName);
        json.put("stlPeriod",stlPeriod);
        json.put("stlPeriodDaySet","D".equals(stlPeriod)?"T+"+stlPeriodDaySet:
    		PayCustStlInfoService.getShowInfoByStlPeriod(stlPeriod,stlPeriodDaySet));
        json.put("settlementWay",settlementWay);
        json.put("agentStlIn",String.format("%.2f",((double)agentStlIn)/100d));
        json.put("agentStlOut",String.format("%.2f",((double)agentStlOut)/100d));
        json.put("stlPeriodAgent",stlPeriodAgent);
        json.put("stlPeriodDaySetAgent","D".equals(stlPeriodAgent)?"T+"+stlPeriodDaySetAgent:
    		PayCustStlInfoService.getShowInfoByStlPeriod(stlPeriodAgent,stlPeriodDaySetAgent));
        try{
            json.put("stlFromTimeAgent", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlFromTimeAgent));
        } catch (Exception e) {}
        try{
            json.put("stlToTimeAgent", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlToTimeAgent));
        } catch (Exception e) {}
        json.put("chargeWay","1".equals(chargeWay)?"预存收取":"结算收取");
        json.put("receiveCount",String.valueOf(receiveCount));
        json.put("receiveAmt",String.valueOf(String.format("%.2f",((double)receiveAmt)/100d)));
        json.put("receiveFee",String.valueOf(String.format("%.2f",((double)receiveFee)/100d)));
        json.put("stlPeriodReceive",stlPeriodReceive);
        try{
            json.put("stlFromTimeReceive", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlFromTimeReceive));
        } catch (Exception e) {}
        try{
            json.put("stlToTimeReceive", new java.text.SimpleDateFormat("yyyy-MM-dd").format(stlToTimeReceive));
        } catch (Exception e) {}
        json.put("stlPeriodSetReceive",stlPeriodSetReceive);
        json.put("stlAmtOver",String.format("%.2f",((double)stlAmtOver)/100d));
        json.put("stlAmtFeeOver",String.format("%.2f",((double)stlAmtFeeOver)/100d));
        return json;
    }
}