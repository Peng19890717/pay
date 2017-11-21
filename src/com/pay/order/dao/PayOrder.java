package com.pay.order.dao;

import java.util.Date;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.PayConstant;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.service.PayCoopBankService;
/**
 * Table PAY_ORDER entity.
 * 
 * @author administrator
 */
public class PayOrder {
    public String payordno;
    public Date actdat;
    public Date cleardat;
    public String ordstatus;//支付状态
    public String prdordtype;
    public String paytype;
    public String multype;
    public String bankcod;
    public String paybnkcod;
    public Date bankstldate;
    public String bankjrnno="";
    public String txccy;
    public Long txamt;
    public Long accamt;
    public Long mulamt;
    public Long fee;
    public Long channelFee;
    public Long netrecamt;
    public String bankpayacno;
    public String bankpayusernm;
    public String bankerror="";
    public String backerror;
    public String merno;
    public String prdordno;
    public String notifyurl;
    public String custId;
    public String custName;
    public String payret;
    public String prdname;
    public String merremark;
    public Date bnkdat;
    public String bnkjnl;
    public String signmsg;
    public String bustyp;
    public String bnkmerno;
    public String tracenumber;
    public Date tracetime;
    public String cbatno;
    public String srefno;
    public String termno;
    public String trantyp;
    public String termtyp;
    public String credno;
    public String versionno;
    public String payacno;
    public String filed1;
    public String filed2;
    public String filed3;
    public String filed4;
    public String filed5;
    public Date createtime;
    public String createtimeStart;
    public String createtimeEnd;
    public String createHMSStart;
    public String createHMSEnd;
    public String storeName;
    public String txamtstart;
    public String txamtend;
    public String stlsts;//结算状态
	public String prdordstatus;//商品订单表中的订单状态
    public Long rftotalamt;//商品订单表中的已退款累计金额
    public String payChannel="";
    public String bankCardType="";
    public String notifyMerFlag;
    public Date notifyMerTime;
    public Long agentFee=0l;
    public String agentStlBatchNo="";
    public String returl;
    public String receivePayNotifyUrl;
    public String mobile="";
    public String credentialNo="";
    public String signature;
    public String verifystring;
    
    public String getCreateHMSStart() {
		return createHMSStart;
	}
	public void setCreateHMSStart(String createHMSStart) {
		this.createHMSStart = createHMSStart;
	}
	public String getCreateHMSEnd() {
		return createHMSEnd;
	}
	public void setCreateHMSEnd(String createHMSEnd) {
		this.createHMSEnd = createHMSEnd;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getVerifystring() {
		return verifystring;
	}
	public void setVerifystring(String verifystring) {
		this.verifystring = verifystring;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCredentialNo() {
		return credentialNo;
	}
	public void setCredentialNo(String credentialNo) {
		this.credentialNo = credentialNo;
	}
	public String getReturl() {
		return returl;
	}
	public void setReturl(String returl) {
		this.returl = returl;
	}
	public String getReceivePayNotifyUrl() {
		return receivePayNotifyUrl;
	}
	public void setReceivePayNotifyUrl(String receivePayNotifyUrl) {
		this.receivePayNotifyUrl = receivePayNotifyUrl;
	}
	public String getStlsts() {
		return stlsts;
	}
	public void setStlsts(String stlsts) {
		this.stlsts = stlsts;
	}
	public String getCreatetimeStart() {
		return createtimeStart;
	}
	public void setCreatetimeStart(String createtimeStart) {
		this.createtimeStart = createtimeStart;
	}
	public String getCreatetimeEnd() {
		return createtimeEnd;
	}
	public void setCreatetimeEnd(String createtimeEnd) {
		this.createtimeEnd = createtimeEnd;
	}
	public Long getRftotalamt() {
		return rftotalamt;
	}
	public void setRftotalamt(Long rftotalamt) {
		this.rftotalamt = rftotalamt;
	}
	public String getTxamtstart() {
		return txamtstart;
	}
	public void setTxamtstart(String txamtstart) {
		this.txamtstart = txamtstart;
	}
	public String getTxamtend() {
		return txamtend;
	}
	public void setTxamtend(String txamtend) {
		this.txamtend = txamtend;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public void setTxamt(Long txamt) {
		this.txamt = txamt;
	}
	public void setAccamt(Long accamt) {
		this.accamt = accamt;
	}
	public void setMulamt(Long mulamt) {
		this.mulamt = mulamt;
	}
	public void setFee(Long fee) {
		this.fee = fee;
	}
	public void setNetrecamt(Long netrecamt) {
		this.netrecamt = netrecamt;
	}
	public String getPayordno(){
         return payordno;
    }
    public void setPayordno(String payordno){
         this.payordno=payordno;
    }
    public Date getActdat(){
         return actdat;
    }
    public void setActdat(Date actdat){
         this.actdat=actdat;
    }
    public Date getCleardat(){
         return cleardat;
    }
    public void setCleardat(Date cleardat){
         this.cleardat=cleardat;
    }
    public String getOrdstatus(){
         return ordstatus;
    }
    public void setOrdstatus(String ordstatus){
         this.ordstatus=ordstatus;
    }
    public String getPrdordtype(){
         return prdordtype;
    }
    public void setPrdordtype(String prdordtype){
         this.prdordtype=prdordtype;
    }
    public String getPaytype(){
         return paytype;
    }
    public void setPaytype(String paytype){
         this.paytype=paytype;
    }
    public String getMultype(){
         return multype;
    }
    public void setMultype(String multype){
         this.multype=multype;
    }
    public String getBankcod(){
         return bankcod;
    }
    public void setBankcod(String bankcod){
         this.bankcod=bankcod;
    }
    public String getPaybnkcod(){
         return paybnkcod;
    }
    public void setPaybnkcod(String paybnkcod){
         this.paybnkcod=paybnkcod;
    }
    public Date getBankstldate(){
         return bankstldate;
    }
    public void setBankstldate(Date bankstldate){
         this.bankstldate=bankstldate;
    }
    public String getBankjrnno(){
         return bankjrnno;
    }
    public void setBankjrnno(String bankjrnno){
         this.bankjrnno=bankjrnno;
    }
    public String getTxccy(){
         return txccy;
    }
    public void setTxccy(String txccy){
         this.txccy=txccy;
    }
    public long getTxamt(){
         return txamt;
    }
    public void setTxamt(long txamt){
         this.txamt=txamt;
    }
    public long getAccamt(){
         return accamt;
    }
    public void setAccamt(long accamt){
         this.accamt=accamt;
    }
    public long getMulamt(){
         return mulamt;
    }
    public void setMulamt(long mulamt){
         this.mulamt=mulamt;
    }
    public long getFee(){
         return fee;
    }
    public void setFee(long fee){
         this.fee=fee;
    }
    public long getNetrecamt(){
         return netrecamt;
    }
    public void setNetrecamt(long netrecamt){
         this.netrecamt=netrecamt;
    }
    public String getBankpayacno(){
         return bankpayacno;
    }
    public void setBankpayacno(String bankpayacno){
         this.bankpayacno=bankpayacno;
    }
    public String getBankpayusernm(){
         return bankpayusernm;
    }
    public void setBankpayusernm(String bankpayusernm){
         this.bankpayusernm=bankpayusernm;
    }
    public String getBankerror(){
         return bankerror;
    }
    public void setBankerror(String bankerror){
         this.bankerror=bankerror;
    }
    public String getBackerror(){
         return backerror;
    }
    public void setBackerror(String backerror){
         this.backerror=backerror;
    }
    public String getMerno(){
         return merno;
    }
    public void setMerno(String merno){
         this.merno=merno;
    }
    public String getPrdordno(){
         return prdordno;
    }
    public void setPrdordno(String prdordno){
         this.prdordno=prdordno;
    }
    public String getNotifyurl(){
         return notifyurl;
    }
    public void setNotifyurl(String notifyurl){
         this.notifyurl=notifyurl;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getPayret(){
         return payret;
    }
    public void setPayret(String payret){
         this.payret=payret;
    }
    public String getPrdname(){
         return prdname;
    }
    public void setPrdname(String prdname){
         this.prdname=prdname;
    }
    public String getMerremark(){
         return merremark;
    }
    public void setMerremark(String merremark){
         this.merremark=merremark;
    }
    public Date getBnkdat(){
         return bnkdat;
    }
    public void setBnkdat(Date bnkdat){
         this.bnkdat=bnkdat;
    }
    public String getBnkjnl(){
         return bnkjnl;
    }
    public void setBnkjnl(String bnkjnl){
         this.bnkjnl=bnkjnl;
    }
    public String getSignmsg(){
         return signmsg;
    }
    public void setSignmsg(String signmsg){
         this.signmsg=signmsg;
    }
    public String getBustyp(){
         return bustyp;
    }
    public void setBustyp(String bustyp){
         this.bustyp=bustyp;
    }
    public String getBnkmerno(){
         return bnkmerno;
    }
    public void setBnkmerno(String bnkmerno){
         this.bnkmerno=bnkmerno;
    }
    public String getTracenumber(){
         return tracenumber;
    }
    public void setTracenumber(String tracenumber){
         this.tracenumber=tracenumber;
    }
    public Date getTracetime(){
         return tracetime;
    }
    public void setTracetime(Date tracetime){
         this.tracetime=tracetime;
    }
    public String getCbatno(){
         return cbatno;
    }
    public void setCbatno(String cbatno){
         this.cbatno=cbatno;
    }
    public String getSrefno(){
         return srefno;
    }
    public void setSrefno(String srefno){
         this.srefno=srefno;
    }
    public String getTermno(){
         return termno;
    }
    public void setTermno(String termno){
         this.termno=termno;
    }
    public String getTrantyp(){
         return trantyp;
    }
    public void setTrantyp(String trantyp){
         this.trantyp=trantyp;
    }
    public String getTermtyp(){
         return termtyp;
    }
    public void setTermtyp(String termtyp){
         this.termtyp=termtyp;
    }
    public String getCredno(){
         return credno;
    }
    public void setCredno(String credno){
         this.credno=credno;
    }
    public String getVersionno(){
         return versionno;
    }
    public void setVersionno(String versionno){
         this.versionno=versionno;
    }
    public String getPayacno(){
         return payacno;
    }
    public void setPayacno(String payacno){
         this.payacno=payacno;
    }
    public String getFiled1(){
         return filed1;
    }
    public void setFiled1(String filed1){
         this.filed1=filed1;
    }
    public String getFiled2(){
         return filed2;
    }
    public void setFiled2(String filed2){
         this.filed2=filed2;
    }
    public String getFiled3(){
         return filed3;
    }
    public void setFiled3(String filed3){
         this.filed3=filed3;
    }
    public String getFiled4(){
         return filed4;
    }
    public void setFiled4(String filed4){
         this.filed4=filed4;
    }
    public String getFiled5(){
         return filed5;
    }
    public void setFiled5(String filed5){
         this.filed5=filed5;
    }
    public Date getCreatetime(){
         return createtime;
    }
    public void setCreatetime(Date createtime){
         this.createtime=createtime;
    }
    public String getPrdordstatus() {
		return prdordstatus;
	}
	public void setPrdordstatus(String prdordstatus) {
		this.prdordstatus = prdordstatus;
	}
    public String getPayChannel(){
        return payChannel;
   }
   public void setPayChannel(String payChannel){
        this.payChannel=payChannel;
   }
   public String getBankCardType(){
        return bankCardType;
   }
   public void setBankCardType(String bankCardType){
        this.bankCardType=bankCardType;
   }
	public String getNotifyMerFlag() {
		return notifyMerFlag;
	}
	public void setNotifyMerFlag(String notifyMerFlag) {
		this.notifyMerFlag = notifyMerFlag;
	}
	public Date getNotifyMerTime() {
		return notifyMerTime;
	}
	public void setNotifyMerTime(Date notifyMerTime) {
		this.notifyMerTime = notifyMerTime;
	}
	
	public Long getAgentFee() {
		return agentFee;
	}
	public void setAgentFee(Long agentFee) {
		this.agentFee = agentFee;
	}
	public String getAgentStlBatchNo() {
		return agentStlBatchNo;
	}
	public void setAgentStlBatchNo(String agentStlBatchNo) {
		this.agentStlBatchNo = agentStlBatchNo;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String toString(){
        String temp = "";
        temp = temp + "payordno="+payordno+"\n";
        temp = temp + "actdat="+actdat+"\n";
        temp = temp + "cleardat="+cleardat+"\n";
        temp = temp + "ordstatus="+ordstatus+"\n";
        temp = temp + "prdordtype="+prdordtype+"\n";
        temp = temp + "paytype="+paytype+"\n";
        temp = temp + "multype="+multype+"\n";
        temp = temp + "bankcod="+ PayCardBinService.BANK_CODE_NAME_MAP.get(bankcod)+"\n";
        temp = temp + "paybnkcod="+paybnkcod+"\n";
        temp = temp + "bankstldate="+bankstldate+"\n";
        temp = temp + "bankjrnno="+bankjrnno+"\n";
        temp = temp + "txccy="+txccy+"\n";
        temp = temp + "txamt="+txamt+"\n";
        temp = temp + "accamt="+accamt+"\n";
        temp = temp + "mulamt="+mulamt+"\n";
        temp = temp + "fee="+fee+"\n";
        temp = temp + "netrecamt="+netrecamt+"\n";
        temp = temp + "bankpayacno="+bankpayacno+"\n";
        temp = temp + "bankpayusernm="+bankpayusernm+"\n";
        temp = temp + "bankerror="+bankerror+"\n";
        temp = temp + "backerror="+backerror+"\n";
        temp = temp + "merno="+merno+"\n";
        temp = temp + "prdordno="+prdordno+"\n";
        temp = temp + "notifyurl="+notifyurl+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "payret="+payret+"\n";
        temp = temp + "prdname="+prdname+"\n";
        temp = temp + "merremark="+merremark+"\n";
        temp = temp + "bnkdat="+bnkdat+"\n";
        temp = temp + "bnkjnl="+bnkjnl+"\n";
        temp = temp + "signmsg="+signmsg+"\n";
        temp = temp + "bustyp="+bustyp+"\n";
        temp = temp + "bnkmerno="+bnkmerno+"\n";
        temp = temp + "tracenumber="+tracenumber+"\n";
        temp = temp + "tracetime="+tracetime+"\n";
        temp = temp + "cbatno="+cbatno+"\n";
        temp = temp + "srefno="+srefno+"\n";
        temp = temp + "termno="+termno+"\n";
        temp = temp + "trantyp="+trantyp+"\n";
        temp = temp + "termtyp="+termtyp+"\n";
        temp = temp + "credno="+credno+"\n";
        temp = temp + "versionno="+versionno+"\n";
        temp = temp + "payacno="+payacno+"\n";
        temp = temp + "filed1="+filed1+"\n";
        temp = temp + "filed2="+filed2+"\n";
        temp = temp + "filed3="+filed3+"\n";
        temp = temp + "filed4="+filed4+"\n";
        temp = temp + "filed5="+filed5+"\n";
        temp = temp + "createtime="+createtime+"\n";
        temp = temp + "storeName="+storeName+"\n";
        temp = temp + "txamtstart="+txamtstart+"\n";
        temp = temp + "txamtend="+txamtend+"\n";
        temp = temp + "prdordstatus="+prdordstatus+"\n";
        temp = temp + "rftotalamt="+rftotalamt+"\n";
        PayCoopBank channel = (PayCoopBank)PayCoopBankService.CHANNEL_MAP_ALL.get(payChannel);
        temp = temp + "payChannel="+ (channel==null?"":channel.getBankName())+"\n";
        temp = temp + "bankCardType="+bankCardType+"\n";
        temp = temp + "stlsts="+stlsts+"\n";
        temp = temp + "notifyMerFlag="+notifyMerFlag+"\n";
        temp = temp + "notifyMerTime="+notifyMerTime+"\n";
        temp = temp + "verifystring="+verifystring+"\n";
        return temp;
    }
    public JSONObject toJson(Map<String,String> custMap) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("payordno",payordno);
        try{
            json.put("actdat", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actdat));
        } catch (Exception e) {}
        try{
            json.put("cleardat", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cleardat));
        } catch (Exception e) {}
        json.put("ordstatus",ordstatus);
        json.put("prdordtype",prdordtype);
        json.put("paytype",paytype);
        json.put("multype",multype);
        json.put("bankcod", PayCardBinService.BANK_CODE_NAME_MAP.get(bankcod));
        json.put("paybnkcod",paybnkcod);
        try{
            json.put("bankstldate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bankstldate));
        } catch (Exception e) {}
        json.put("bankjrnno",bankjrnno);
        json.put("txccy",txccy);
        json.put("txamt",String.format("%.2f",(float)txamt*0.01));
        json.put("accamt",String.valueOf(accamt));
        json.put("mulamt",String.valueOf(mulamt));
        json.put("fee",String.format("%.2f",(float)fee*0.01));
        json.put("channelFee",String.format("%.2f",(float)channelFee*0.01));
        json.put("netrecamt",String.format("%.2f",(float)netrecamt*0.01));
        json.put("bankpayacno",bankpayacno);
        json.put("bankpayusernm",bankpayusernm);
        json.put("bankerror",bankerror);
        json.put("backerror",backerror);
        if(merno.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))||merno.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))||
        		merno.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))){
        	custName = custMap.get(custId)!=null?"("+custMap.get(custId)+")":"";
        	json.put("custName",custId+custName);
        }
        json.put("merno",merno);
    	json.put("storeName",storeName);
        json.put("prdordno",prdordno);
        json.put("notifyurl",notifyurl);
        json.put("custId",custId);
        json.put("payret",payret);
        json.put("prdname",prdname);
        json.put("merremark",merremark);
        try{
            json.put("bnkdat", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bnkdat));
        } catch (Exception e) {}
        json.put("bnkjnl",bnkjnl);
        json.put("signmsg",signmsg);
        json.put("bustyp",bustyp);
        json.put("bnkmerno",bnkmerno);
        json.put("tracenumber",tracenumber);
        try{
            json.put("tracetime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tracetime));
        } catch (Exception e) {}
        json.put("cbatno",cbatno);
        json.put("srefno",srefno);
        json.put("termno",termno);
        json.put("trantyp",trantyp);
        json.put("termtyp",termtyp);
        json.put("credno",credno);
        json.put("versionno",versionno);
        json.put("payacno",payacno);
        json.put("filed1",filed1);
        json.put("filed2",filed2);
        json.put("filed3",filed3);
        json.put("filed4",filed4);
        json.put("filed5",filed5);
        json.put("txamtstart",txamtstart);
        json.put("txamtend",txamtend);
        json.put("prdordstatus",prdordstatus);
        json.put("rftotalamt",rftotalamt);
        json.put("stlsts",stlsts);
        try{
            json.put("createtime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createtime));
            PayCoopBank channel = (PayCoopBank)PayCoopBankService.CHANNEL_MAP_ALL.get(payChannel);
            json.put("payChannel",channel==null?"":channel.getBankName());
        } catch (Exception e) {}
        json.put("bankCardType",bankCardType);
        json.put("notifyMerFlag",notifyMerFlag);
        try{
            json.put("notifyMerTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(notifyMerTime));
        } catch (Exception e) {}
        json.put("agentFee",String.format("%.2f",(float)agentFee*0.01));
        json.put("agentStlBatchNo",agentStlBatchNo);
        json.put("mobile",mobile);
        json.put("credentialNo",credentialNo);
        return json;
    }
}