package com.pay.order.dao;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.PayConstant;
/**
 * Table PAY_RECEIVE_AND_PAY entity.
 * 
 * @author administrator
 */
public class PayReceiveAndPay {
    public String id;
    public String type;
    public String custType;
    public String custId;
    public String channelId;
    public String merTranNo;
    public String channelTranNo;
    public String corpAcctNo;
    public String businessCode;
    public String productCode;
    public String accountProp;
    public String timeliness;
    public String appointmentTime;
    public String sn="";
    public String subMerchantId;
    public String accountType;
    public String accountNo;
    public String accountName;
    public String bankName;
    public String bankCode;
    public String drctBankCode;
    public String protocolNo;
    public String currency;
    public Long amount=0l;
    public Long cou;
    public String idType;
    public String certId;
    public String tel;
    public String summary;
    public String status;
    public String retCode;
    public String errorMsg;
    public String feeCode;
    public String feeInfo;
    public String batNo;
    public Date createTime;
    public String bankGeneralName;
    public String tranType;
    public String receivePayNotifyUrl;
    public String bussFromType;
    public Date actime;
    public String stlbatno;
    public Date stlTime;
    public String createTimeStart;
    public String createTimeEnd;
    public String deductMoneyFlag;
    public String returnMoneyFlag;
    //======================
    public Long fee;
    public Long feeChannel;
	public Date completeTime;
	public String bankId = "";
	public String accType = "";
	public String accNo = "";
	public String accName = "";
	public String province = "";
	public String city = "";
	public String credentialType = "";
	public String credentialNo = "";
	public String beginDate = "";
	public String endDate = "";
	public String respCode = "";
	public String respDesc = "";
	public String charge = "";
	public String payAccNo = "";
	public String payAccName = "";
	public String recAccNo = "";
	public String recAccName = "";
	public String way="";
	public String protocolType="";
	public String postscript="";
	public String stlsts="";
	public Long preOptCashAcBal;
	public String signProtocolChannel;
	
	public String getReturnMoneyFlag() {
		return returnMoneyFlag;
	}
	public void setReturnMoneyFlag(String returnMoneyFlag) {
		this.returnMoneyFlag = returnMoneyFlag;
	}
	public String getDeductMoneyFlag() {
		return deductMoneyFlag;
	}
	public void setDeductMoneyFlag(String deductMoneyFlag) {
		this.deductMoneyFlag = deductMoneyFlag;
	}
	public Long getCou() {
		return cou;
	}
	public void setCou(Long cou) {
		this.cou = cou;
	}
	public String getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	public void setRespInfo(String respCode){
		this.respCode = respCode;
		this.retCode = respCode;
		respDesc = PayConstant.RESP_CODE_DESC.get(respCode);
		if(respDesc==null)respDesc="未知编码("+respCode+")";
		errorMsg = respDesc;
	}
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getType(){
         return type;
    }
    public void setType(String type){
         this.type=type;
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
    public String getChannelId(){
         return channelId;
    }
    public void setChannelId(String channelId){
         this.channelId=channelId;
    }
    public String getMerTranNo(){
         return merTranNo;
    }
    public void setMerTranNo(String merTranNo){
         this.merTranNo=merTranNo;
    }
    public String getChannelTranNo(){
         return channelTranNo;
    }
    public void setChannelTranNo(String channelTranNo){
         this.channelTranNo=channelTranNo;
    }
    public String getCorpAcctNo(){
         return corpAcctNo;
    }
    public void setCorpAcctNo(String corpAcctNo){
         this.corpAcctNo=corpAcctNo;
    }
    public String getAccountProp(){
         return accountProp;
    }
    public void setAccountProp(String accountProp){
         this.accountProp=accountProp;
    }
    public String getTimeliness(){
         return timeliness;
    }
    public void setTimeliness(String timeliness){
         this.timeliness=timeliness;
    }
    public String getAppointmentTime(){
         return appointmentTime;
    }
    public void setAppointmentTime(String appointmentTime){
         this.appointmentTime=appointmentTime;
    }
    public String getSn(){
         return sn;
    }
    public void setSn(String sn){
         this.sn=sn;
    }
    public String getSubMerchantId(){
         return subMerchantId;
    }
    public void setSubMerchantId(String subMerchantId){
         this.subMerchantId=subMerchantId;
    }
    public String getAccountType(){
         return accountType;
    }
    public void setAccountType(String accountType){
         this.accountType=accountType;
    }
    public String getAccountNo(){
         return accountNo;
    }
    public void setAccountNo(String accountNo){
         this.accountNo=accountNo;
    }
    public String getAccountName(){
         return accountName;
    }
    public void setAccountName(String accountName){
         this.accountName=accountName;
    }
    public String getBankName(){
         return bankName;
    }
    public void setBankName(String bankName){
         this.bankName=bankName;
    }
    public String getBankCode(){
         return bankCode;
    }
    public void setBankCode(String bankCode){
         this.bankCode=bankCode;
    }
    public String getDrctBankCode(){
         return drctBankCode;
    }
    public void setDrctBankCode(String drctBankCode){
         this.drctBankCode=drctBankCode;
    }
    public String getProtocolNo(){
         return protocolNo;
    }
    public void setProtocolNo(String protocolNo){
         this.protocolNo=protocolNo;
    }
    public String getCurrency(){
         return currency;
    }
    public void setCurrency(String currency){
         this.currency=currency;
    }
    public Long getAmount(){
         return amount;
    }
    public void setAmount(Long amount){
         this.amount=amount;
    }
    public String getIdType(){
         return idType;
    }
    public void setIdType(String idType){
         this.idType=idType;
    }
    public String getCertId(){
         return certId;
    }
    public void setCertId(String certId){
         this.certId=certId;
    }
    public String getTel(){
         return tel;
    }
    public void setTel(String tel){
         this.tel=tel;
    }
    public String getSummary(){
         return summary;
    }
    public void setSummary(String summary){
         this.summary=summary;
    }
    public String getStatus(){
         return status;
    }
    public void setStatus(String status){
         this.status=status;
    }
    public String getRetCode(){
         return retCode;
    }
    public void setRetCode(String retCode){
         this.retCode=retCode;
         this.respCode = retCode;
         respDesc = PayConstant.RESP_CODE_DESC.get(respCode);
 		if(respDesc==null)respDesc="未知编码("+respCode+")";
 		errorMsg = respDesc;
    }
    public String getErrorMsg(){
         return errorMsg;
    }
    public void setErrorMsg(String errorMsg){
         this.errorMsg=errorMsg;
         this.respDesc = errorMsg;
    }
    public String getFeeCode(){
         return feeCode;
    }
    public void setFeeCode(String feeCode){
         this.feeCode=feeCode;
    }
    public String getFeeInfo(){
         return feeInfo;
    }
    public void setFeeInfo(String feeInfo){
         this.feeInfo=feeInfo;
    }
    public String getBatNo(){
         return batNo;
    }
    public void setBatNo(String batNo){
         this.batNo=batNo;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String getBankGeneralName(){
         return bankGeneralName;
    }
    public void setBankGeneralName(String bankGeneralName){
         this.bankGeneralName=bankGeneralName;
    }
    public Long getFee(){
         return fee;
    }
    public void setFee(Long fee){
         this.fee=fee;
    }
    public Long getFeeChannel(){
         return feeChannel;
    }
    public void setFeeChannel(Long feeChannel){
         this.feeChannel=feeChannel;
    }
    public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCredentialType() {
		return credentialType;
	}
	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}
	public String getCredentialNo() {
		return credentialNo;
	}
	public void setCredentialNo(String credentialNo) {
		this.credentialNo = credentialNo;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
		this.retCode = respCode;
		respDesc = PayConstant.RESP_CODE_DESC.get(respCode);
		if(respDesc==null)respDesc="未知编码("+respCode+")";
		errorMsg = respDesc;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
		this.errorMsg = respDesc;
	}
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	public String getPayAccNo() {
		return payAccNo;
	}
	public void setPayAccNo(String payAccNo) {
		this.payAccNo = payAccNo;
	}
	public String getPayAccName() {
		return payAccName;
	}
	public void setPayAccName(String payAccName) {
		this.payAccName = payAccName;
	}
	public String getRecAccNo() {
		return recAccNo;
	}
	public void setRecAccNo(String recAccNo) {
		this.recAccNo = recAccNo;
	}
	public String getRecAccName() {
		return recAccName;
	}
	public void setRecAccName(String recAccName) {
		this.recAccName = recAccName;
	}
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	public String getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getReceivePayNotifyUrl() {
		return receivePayNotifyUrl;
	}
	public void setReceivePayNotifyUrl(String receivePayNotifyUrl) {
		this.receivePayNotifyUrl = receivePayNotifyUrl;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getBussFromType() {
		return bussFromType;
	}
	public void setBussFromType(String bussFromType) {
		this.bussFromType = bussFromType;
	}
	public Date getActime() {
		return actime;
	}
	public void setActime(Date actime) {
		this.actime = actime;
	}
	public String getStlbatno() {
		return stlbatno;
	}
	public void setStlbatno(String stlbatno) {
		this.stlbatno = stlbatno;
	}
	public Date getStlTime() {
		return stlTime;
	}
	public void setStlTime(Date stlTime) {
		this.stlTime = stlTime;
	}
	public String getStlsts() {
		return stlsts;
	}
	public void setStlsts(String stlsts) {
		this.stlsts = stlsts;
	}
	public Long getPreOptCashAcBal() {
		return preOptCashAcBal;
	}
	public void setPreOptCashAcBal(Long preOptCashAcBal) {
		this.preOptCashAcBal = preOptCashAcBal;
	}
	public String getSignProtocolChannel() {
		return signProtocolChannel;
	}
	public void setSignProtocolChannel(String signProtocolChannel) {
		this.signProtocolChannel = signProtocolChannel;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "type="+type+"\n";
        temp = temp + "custType="+custType+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "channelId="+channelId+"\n";
        temp = temp + "merTranNo="+merTranNo+"\n";
        temp = temp + "channelTranNo="+channelTranNo+"\n";
        temp = temp + "corpAcctNo="+corpAcctNo+"\n";
        temp = temp + "accountProp="+accountProp+"\n";
        temp = temp + "timeliness="+timeliness+"\n";
        temp = temp + "appointmentTime="+appointmentTime+"\n";
        temp = temp + "sn="+sn+"\n";
        temp = temp + "subMerchantId="+subMerchantId+"\n";
        temp = temp + "accountType="+accountType+"\n";
        temp = temp + "accountNo="+accountNo+"\n";
        temp = temp + "accountName="+accountName+"\n";
        temp = temp + "bankName="+bankName+"\n";
        temp = temp + "bankCode="+bankCode+"\n";
        temp = temp + "drctBankCode="+drctBankCode+"\n";
        temp = temp + "protocolNo="+protocolNo+"\n";
        temp = temp + "currency="+currency+"\n";
        temp = temp + "amount="+amount+"\n";
        temp = temp + "idType="+idType+"\n";
        temp = temp + "certId="+certId+"\n";
        temp = temp + "tel="+tel+"\n";
        temp = temp + "summary="+summary+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "retCode="+retCode+"\n";
        temp = temp + "errorMsg="+errorMsg+"\n";
        temp = temp + "feeCode="+feeCode+"\n";
        temp = temp + "feeInfo="+feeInfo+"\n";
        temp = temp + "batNo="+batNo+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "bankGeneralName="+bankGeneralName+"\n";
        temp = temp + "fee="+fee+"\n";
        temp = temp + "feeChannel="+feeChannel+"\n";
        temp = temp + "signProtocolChannel="+signProtocolChannel+"\n";
        temp = temp + "deductMoneyFlag="+deductMoneyFlag+"\n";
        temp = temp + "returnMoneyFlag="+returnMoneyFlag+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("type",type);
        json.put("custType",custType);
        json.put("custId",custId);
        json.put("channelId",channelId);
        json.put("merTranNo",merTranNo);
        json.put("channelTranNo",channelTranNo);
        json.put("corpAcctNo",corpAcctNo);
        json.put("accountProp",accountProp);
        json.put("timeliness",timeliness);
        json.put("appointmentTime",appointmentTime);
        json.put("sn",sn);
        json.put("subMerchantId",subMerchantId);
        json.put("accountType",accountType);
        json.put("accountNo",accountNo);
        json.put("accountName",accountName);
        json.put("bankName",bankName);
        json.put("bankCode",bankCode);
        json.put("drctBankCode",drctBankCode);
        json.put("protocolNo",protocolNo);
        json.put("currency",currency);
        json.put("amount",String.format("%.2f",(double)amount*0.01));
        json.put("idType",idType);
        json.put("certId",certId);
        json.put("tel",tel);
        json.put("summary",summary);
        json.put("status",status);
        json.put("retCode",retCode);
        json.put("errorMsg",errorMsg);
        json.put("feeCode",feeCode);
        json.put("feeInfo",feeInfo);
        json.put("batNo",batNo);
        json.put("tranType",tranType);
        json.put("bussFromType",bussFromType);
        json.put("cou",cou);
        json.put("signProtocolChannel",signProtocolChannel);
        json.put("deductMoneyFlag",deductMoneyFlag);
        json.put("receivePayNotifyUrl",receivePayNotifyUrl);
        json.put("returnMoneyFlag",returnMoneyFlag);
        try{json.put("preOptCashAcBal",String.format("%.2f",(double)(preOptCashAcBal)*0.01));} catch (Exception e) {}
        try{json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));} catch (Exception e) {}
        try{json.put("bankGeneralName",bankGeneralName);} catch (Exception e) {}
        try{json.put("fee",String.format("%.2f",(double)fee*0.01));} catch (Exception e) {}
        if(feeChannel != null) try{json.put("feeChannel",String.format("%.2f",(double)feeChannel*0.01));} catch (Exception e) {}
        try{json.put("completeTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(completeTime));} catch (Exception e) {}
        try{json.put("actime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actime));} catch (Exception e) {}
        try{json.put("stlbatno",stlbatno);} catch (Exception e) {}
        try{json.put("stlTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(stlTime));} catch (Exception e) {}
        try{json.put("stlsts","0".equals(type)&&"0".equals(bussFromType)?stlsts:"-");} catch (Exception e) {}
        return json;
    }
}