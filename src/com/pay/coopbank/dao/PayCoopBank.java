package com.pay.coopbank.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.pay.fee.dao.PayFeeRate;
/**
 * Table PAY_COOP_BANK entity.
 * 
 * @author administrator
 */
public class PayCoopBank {
    public String bankCode;
    public String paySysId;
    public String bankName;
    public String bankRelNo;
    public String bankStatus;
    public String accountOnline;
    public String refundOnline;
    public Date accountTime;
    public String trtBankFlg;
    public String payWebFlag;
    public String payQuickFlag;
    public String payWithdrawFlag;
    public String payReceiveFlag;
    public String stlAcNo;
    public String certNo;
    public String regNo;
    public String legRep;
    public String custMgr;
    public String telNo;
    public String bankEmail;
    public String bankAddress;
    public String bizRange;
    public String remark;
    public String creOperId;
    public Date creTime;
    public String lstUptOperId;
    public Date lstUptTime;
    public String depSubCode;
    public String paySubCode;
    public String recSubCode;
    public String recerrSubCode;
    public String quickResendSms;
    public String stlTimeType;
    public String stlTime;
    public String payScanFlag;
    public PayChannelBankRelation channelBankRelation;
    public List<PayFeeRate> feeRateList = new ArrayList<PayFeeRate>();
    public Map<String,PayFeeRate> feeRateMap = new HashMap<String,PayFeeRate>();
    //临时接受费率编码
    public String feeCode;
    public String feeName;
    //退款费率、提现费率、退款费率
    public String tkFeeCode;
    public String txFeeCode;
    public String zzFeeCode;
    public String tkFeeName;
    public String txFeeName;
    public String zzFeeName;
    
    // 代收代付费率
    public String dsFeeCode;
    public String dsFeeName;
    public String dfFeeName;
	public String dfFeeCode;
    
    public String b2cjjFeeCode;
    public String b2cxyFeeCode;
    public String b2bFeeCode;
    public String yhkFeeCode;
    public String kjJJFeeCode;
    public String kjDJFeeCode ;
    public String b2cjjFeeName;
    public String b2cxyFeeName;
    public String b2bFeeName;
    public String yhkFeeName;
    public String kjJJFeeName;
    public String kjDJFeeName ;
    
    public String wxwapFeeCode;
    public String wxwapFeeName;
    public String zfbFeeCode;
    public String zfbFeeName;
    public String qqFeeCode;
    public String qqFeeName;
    
    public String getPayScanFlag() {
		return payScanFlag;
	}
	public void setPayScanFlag(String payScanFlag) {
		this.payScanFlag = payScanFlag;
	}
	public String getWxwapFeeCode() {
		return wxwapFeeCode;
	}
	public void setWxwapFeeCode(String wxwapFeeCode) {
		this.wxwapFeeCode = wxwapFeeCode;
	}
	public String getWxwapFeeName() {
		return wxwapFeeName;
	}
	public void setWxwapFeeName(String wxwapFeeName) {
		this.wxwapFeeName = wxwapFeeName;
	}
	public String getZfbFeeCode() {
		return zfbFeeCode;
	}
	public void setZfbFeeCode(String zfbFeeCode) {
		this.zfbFeeCode = zfbFeeCode;
	}
	public String getZfbFeeName() {
		return zfbFeeName;
	}
	public void setZfbFeeName(String zfbFeeName) {
		this.zfbFeeName = zfbFeeName;
	}

	//支持网银类型
    public String supportedUserType;
    
    public String getStlTimeType() {
		return stlTimeType;
	}
	public void setStlTimeType(String stlTimeType) {
		this.stlTimeType = stlTimeType;
	}
	public String getStlTime() {
		return stlTime;
	}
	public void setStlTime(String stlTime) {
		this.stlTime = stlTime;
	}
	public String getB2cjjFeeCode() {
		return b2cjjFeeCode;
	}
	public void setB2cjjFeeCode(String b2cjjFeeCode) {
		this.b2cjjFeeCode = b2cjjFeeCode;
	}
	public String getB2cxyFeeCode() {
		return b2cxyFeeCode;
	}
	public void setB2cxyFeeCode(String b2cxyFeeCode) {
		this.b2cxyFeeCode = b2cxyFeeCode;
	}
	public String getB2bFeeCode() {
		return b2bFeeCode;
	}
	public void setB2bFeeCode(String b2bFeeCode) {
		this.b2bFeeCode = b2bFeeCode;
	}
	public String getYhkFeeCode() {
		return yhkFeeCode;
	}
	public void setYhkFeeCode(String yhkFeeCode) {
		this.yhkFeeCode = yhkFeeCode;
	}
	public String getKjJJFeeCode() {
		return kjJJFeeCode;
	}
	public void setKjJJFeeCode(String kjJJFeeCode) {
		this.kjJJFeeCode = kjJJFeeCode;
	}
	public String getKjDJFeeCode() {
		return kjDJFeeCode;
	}
	public void setKjDJFeeCode(String kjDJFeeCode) {
		this.kjDJFeeCode = kjDJFeeCode;
	}
	public String getB2cjjFeeName() {
		return b2cjjFeeName;
	}
	public void setB2cjjFeeName(String b2cjjFeeName) {
		this.b2cjjFeeName = b2cjjFeeName;
	}
	public String getB2cxyFeeName() {
		return b2cxyFeeName;
	}
	public void setB2cxyFeeName(String b2cxyFeeName) {
		this.b2cxyFeeName = b2cxyFeeName;
	}
	public String getB2bFeeName() {
		return b2bFeeName;
	}
	public void setB2bFeeName(String b2bFeeName) {
		this.b2bFeeName = b2bFeeName;
	}
	public String getYhkFeeName() {
		return yhkFeeName;
	}
	public void setYhkFeeName(String yhkFeeName) {
		this.yhkFeeName = yhkFeeName;
	}
	
	public String getKjJJFeeName() {
		return kjJJFeeName;
	}
	public void setKjJJFeeName(String kjJJFeeName) {
		this.kjJJFeeName = kjJJFeeName;
	}
	public String getKjDJFeeName() {
		return kjDJFeeName;
	}
	public void setKjDJFeeName(String kjDJFeeName) {
		this.kjDJFeeName = kjDJFeeName;
	}

	//支持快捷类型
    public String quickUserType;
    //支持提现
    public String withdrawUserType;
    
    public String getSupportedUserType() {
		return supportedUserType;
	}
	public void setSupportedUserType(String supportedUserType) {
		this.supportedUserType = supportedUserType;
	}
	public String getQuickUserType() {
		return quickUserType;
	}
	public void setQuickUserType(String quickUserType) {
		this.quickUserType = quickUserType;
	}
	public String getWithdrawUserType() {
		return withdrawUserType;
	}
	public void setWithdrawUserType(String withdrawUserType) {
		this.withdrawUserType = withdrawUserType;
	}
	public String getPayWebFlag() {
		return payWebFlag;
	}
	public void setPayWebFlag(String payWebFlag) {
		this.payWebFlag = payWebFlag;
	}
	public String getPayQuickFlag() {
		return payQuickFlag;
	}
	public void setPayQuickFlag(String payQuickFlag) {
		this.payQuickFlag = payQuickFlag;
	}
	public String getAccountOnline() {
		return accountOnline;
	}
	public void setAccountOnline(String accountOnline) {
		this.accountOnline = accountOnline;
	}
	public String getRefundOnline() {
		return refundOnline;
	}
	public void setRefundOnline(String refundOnline) {
		this.refundOnline = refundOnline;
	}
	public String getTkFeeName() {
		return tkFeeName;
	}
	public void setTkFeeName(String tkFeeName) {
		this.tkFeeName = tkFeeName;
	}
	public String getTxFeeName() {
		return txFeeName;
	}
	public void setTxFeeName(String txFeeName) {
		this.txFeeName = txFeeName;
	}
	public String getZzFeeName() {
		return zzFeeName;
	}
	public void setZzFeeName(String zzFeeName) {
		this.zzFeeName = zzFeeName;
	}
	public String getTkFeeCode() {
		return tkFeeCode;
	}
	public void setTkFeeCode(String tkFeeCode) {
		this.tkFeeCode = tkFeeCode;
	}
	public String getTxFeeCode() {
		return txFeeCode;
	}
	public void setTxFeeCode(String txFeeCode) {
		this.txFeeCode = txFeeCode;
	}
	public String getZzFeeCode() {
		return zzFeeCode;
	}
	public void setZzFeeCode(String zzFeeCode) {
		this.zzFeeCode = zzFeeCode;
	}
	public String getFeeName() {
		return feeName;
	}
	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}
	public String getFeeCode() {
		return feeCode;
	}
	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	public String getBankCode(){
         return bankCode;
    }
    public void setBankCode(String bankCode){
         this.bankCode=bankCode;
    }
    public String getPaySysId(){
         return paySysId;
    }
    public void setPaySysId(String paySysId){
         this.paySysId=paySysId;
    }
    public String getBankName(){
         return bankName;
    }
    public void setBankName(String bankName){
         this.bankName=bankName;
    }
    public String getBankRelNo(){
         return bankRelNo;
    }
    public void setBankRelNo(String bankRelNo){
         this.bankRelNo=bankRelNo;
    }
    public String getBankStatus(){
         return bankStatus;
    }
    public void setBankStatus(String bankStatus){
         this.bankStatus=bankStatus;
    }
    public Date getAccountTime(){
         return accountTime;
    }
    public void setAccountTime(Date accountTime){
         this.accountTime=accountTime;
    }
    public String getTrtBankFlg(){
         return trtBankFlg;
    }
    public void setTrtBankFlg(String trtBankFlg){
         this.trtBankFlg=trtBankFlg;
    }
    public String getStlAcNo(){
         return stlAcNo;
    }
    public void setStlAcNo(String stlAcNo){
         this.stlAcNo=stlAcNo;
    }
    public String getCertNo(){
         return certNo;
    }
    public void setCertNo(String certNo){
         this.certNo=certNo;
    }
    public String getRegNo(){
         return regNo;
    }
    public void setRegNo(String regNo){
         this.regNo=regNo;
    }
    public String getLegRep(){
         return legRep;
    }
    public void setLegRep(String legRep){
         this.legRep=legRep;
    }
    public String getCustMgr(){
         return custMgr;
    }
    public void setCustMgr(String custMgr){
         this.custMgr=custMgr;
    }
    public String getTelNo(){
         return telNo;
    }
    public void setTelNo(String telNo){
         this.telNo=telNo;
    }
    public String getBankEmail(){
         return bankEmail;
    }
    public void setBankEmail(String bankEmail){
         this.bankEmail=bankEmail;
    }
    public String getBankAddress(){
         return bankAddress;
    }
    public void setBankAddress(String bankAddress){
         this.bankAddress=bankAddress;
    }
    public String getBizRange(){
         return bizRange;
    }
    public void setBizRange(String bizRange){
         this.bizRange=bizRange;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
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
    public void setLstUptTime(Date lstUptTime){
         this.lstUptTime=lstUptTime;
    }
    public String getDepSubCode(){
         return depSubCode;
    }
    public void setDepSubCode(String depSubCode){
         this.depSubCode=depSubCode;
    }
    public String getPaySubCode(){
         return paySubCode;
    }
    public void setPaySubCode(String paySubCode){
         this.paySubCode=paySubCode;
    }
    public String getRecSubCode(){
         return recSubCode;
    }
    public void setRecSubCode(String recSubCode){
         this.recSubCode=recSubCode;
    }
    public String getRecerrSubCode(){
         return recerrSubCode;
    }
    public void setRecerrSubCode(String recerrSubCode){
         this.recerrSubCode=recerrSubCode;
    }
    
    public String getPayWithdrawFlag() {
		return payWithdrawFlag;
	}
	public void setPayWithdrawFlag(String payWithdrawFlag) {
		this.payWithdrawFlag = payWithdrawFlag;
	}
	
	public String getPayReceiveFlag() {
		return payReceiveFlag;
	}
	public void setPayReceiveFlag(String payReceiveFlag) {
		this.payReceiveFlag = payReceiveFlag;
	}
	public String getDsFeeCode() {
		return dsFeeCode;
	}
	public void setDsFeeCode(String dsFeeCode) {
		this.dsFeeCode = dsFeeCode;
	}
	public String getDsFeeName() {
		return dsFeeName;
	}
	public void setDsFeeName(String dsFeeName) {
		this.dsFeeName = dsFeeName;
	}
	public String getDfFeeName() {
		return dfFeeName;
	}
	public void setDfFeeName(String dfFeeName) {
		this.dfFeeName = dfFeeName;
	}
	public String getDfFeeCode() {
		return dfFeeCode;
	}
	public void setDfFeeCode(String dfFeeCode) {
		this.dfFeeCode = dfFeeCode;
	}
	
	public String getQuickResendSms() {
		return quickResendSms;
	}
	public void setQuickResendSms(String quickResendSms) {
		this.quickResendSms = quickResendSms;
	}
	public String getQqFeeCode() {
		return qqFeeCode;
	}
	public void setQqFeeCode(String qqFeeCode) {
		this.qqFeeCode = qqFeeCode;
	}
	public String getQqFeeName() {
		return qqFeeName;
	}
	public void setQqFeeName(String qqFeeName) {
		this.qqFeeName = qqFeeName;
	}
	
	public String toString(){
        String temp = "";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "paySysId="+paySysId+"\n";
        temp = temp + "bankName="+bankName+"\n";
        temp = temp + "bankRelNo="+bankRelNo+"\n";
        temp = temp + "bankStatus="+bankStatus+"\n";
        temp = temp + "accountTime="+accountTime+"\n";
        temp = temp + "trtBankFlg="+trtBankFlg+"\n";
        temp = temp + "stlAcNo="+stlAcNo+"\n";
        temp = temp + "certNo="+certNo+"\n";
        temp = temp + "regNo="+regNo+"\n";
        temp = temp + "legRep="+legRep+"\n";
        temp = temp + "custMgr="+custMgr+"\n";
        temp = temp + "telNo="+telNo+"\n";
        temp = temp + "bankEmail="+bankEmail+"\n";
        temp = temp + "bankAddress="+bankAddress+"\n";
        temp = temp + "bizRange="+bizRange+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "creOperId="+creOperId+"\n";
        temp = temp + "creTime="+creTime+"\n";
        temp = temp + "lstUptOperId="+lstUptOperId+"\n";
        temp = temp + "lstUptTime="+lstUptTime+"\n";
        temp = temp + "depSubCode="+depSubCode+"\n";
        temp = temp + "paySubCode="+paySubCode+"\n";
        temp = temp + "recSubCode="+recSubCode+"\n";
        temp = temp + "recerrSubCode="+recerrSubCode+"\n";
        temp = temp + "accountOnline="+accountOnline+"\n";
        temp = temp + "refundOnline="+refundOnline+"\n";
        temp = temp + "payQuickFlag="+payQuickFlag+"\n";
        temp = temp + "payWebFlag="+payWebFlag+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("bankCode",bankCode);
        json.put("paySysId",paySysId);
        json.put("bankName",bankName);
        json.put("bankRelNo",bankRelNo);
        json.put("bankStatus",bankStatus);
        json.put("accountOnline",accountOnline);
        json.put("refundOnline",refundOnline);
        json.put("payQuickFlag",payQuickFlag);
        json.put("payWebFlag",payWebFlag);
        json.put("payWithdrawFlag",payWithdrawFlag);
        json.put("payReceiveFlag",payReceiveFlag);
        try{
            json.put("accountTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(accountTime));
        } catch (Exception e) {}
        json.put("trtBankFlg",trtBankFlg);
        json.put("stlAcNo",stlAcNo);
        json.put("certNo",certNo);
        json.put("regNo",regNo);
        json.put("legRep",legRep);
        json.put("custMgr",custMgr);
        json.put("telNo",telNo);
        json.put("bankEmail",bankEmail);
        json.put("bankAddress",bankAddress);
        json.put("bizRange",bizRange);
        json.put("remark",remark);
        json.put("creOperId",creOperId);
        try{
            json.put("creTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(creTime));
        } catch (Exception e) {}
        json.put("lstUptOperId",lstUptOperId);
        json.put("stlTimeType",stlTimeType);
        json.put("stlTime",stlTime);
        try{
            json.put("lstUptTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lstUptTime));
        } catch (Exception e) {}
        json.put("depSubCode",depSubCode);
        json.put("paySubCode",paySubCode);
        json.put("recSubCode",recSubCode);
        json.put("recerrSubCode",recerrSubCode);
        json.put("feeCode",feeCode);
        json.put("feeName",feeName);
        json.put("tkFeeCode",tkFeeCode);
        json.put("txFeeCode",txFeeCode);
        json.put("zzFeeCode",zzFeeCode);
        json.put("b2cjjFeeCode",b2cjjFeeCode);
        json.put("b2cxyFeeCode",b2cxyFeeCode);
        json.put("b2bFeeCode",b2bFeeCode);
        json.put("yhkFeeCode",yhkFeeCode);
        json.put("kjJJFeeCode",kjJJFeeCode);
        json.put("kjDJFeeCode",kjDJFeeCode);
        json.put("wxwapFeeCode",wxwapFeeCode);
        json.put("zfbFeeCode",zfbFeeCode);
        json.put("qqFeeCode",qqFeeCode);
        json.put("quickResendSms",quickResendSms);
        json.put("supportedUserType",supportedUserType == null ? "" : supportedUserType);
        json.put("quickUserType",quickUserType == null ? "" : quickUserType);
        json.put("withdrawUserType",withdrawUserType == null ? "" : withdrawUserType);
        return json;
    }
}