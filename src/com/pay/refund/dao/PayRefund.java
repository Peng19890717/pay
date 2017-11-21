package com.pay.refund.dao;

import org.json.JSONException;
import org.json.JSONObject;

import util.Tools;

import java.util.Date;
/**
 * Table PAY_REFUND entity.
 * 
 * @author administrator
 */
public class PayRefund {
    public String refordno;
    public Date rfreqdate;
    public String oriprdordno = "";
    public String oripayordno = "";
    public String rfpayordno = "";
    public Date rfpaydate;
    public String bankcode = "";
    public String getbankcode = "";
    public String custpayacno = "";
    public String bankpayusernm = "";
    public String merpayacno = "";
    public String custId = "";
    public String txccy = "";
    public Long rfamt = 0l;
    public Long fee = 0l;
    public Long netrecamt = 0l;
    public String rfsake = "";
    public String banksts = "";
    public String ordstatus = "";
    public Date obankstldate;
    public String obankjrnno = "";
    public Date bankstldate;
    public String bankjrnno = "";
    public String returl = "";
    public String notifyurl = "";
    public String bankerror = "";
    public String merno = "";
    public Date bnkdat;
    public String stlbatno = "";
    public Date rfordtime;
    public String stlsts = "";
    public String bustyp = "";
    public String operId = "";
    public String filed1 = "";
    public String filed2 = "";
    public String filed3 = "";
    public String filed4 = "";
    public String filed5 = "";
    public Date rfreqdateStart;
    public Date rfreqdateEnd;
    public String payordno;
    public String payacno;
    
	public String getPayacno() {
		return payacno;
	}
	public void setPayacno(String payacno) {
		this.payacno = payacno;
	}
	public String getRefordno(){
         return refordno;
    }
    public void setRefordno(String refordno){
         this.refordno=refordno;
    }
    public Date getRfreqdate(){
         return rfreqdate;
    }
    public void setRfreqdate(Date rfreqdate){
         this.rfreqdate=rfreqdate;
    }
    public String getOriprdordno(){
         return oriprdordno;
    }
    public void setOriprdordno(String oriprdordno){
         this.oriprdordno=oriprdordno;
    }
    public String getOripayordno(){
         return oripayordno;
    }
    public void setOripayordno(String oripayordno){
         this.oripayordno=oripayordno;
    }
    public String getRfpayordno(){
         return rfpayordno;
    }
    public void setRfpayordno(String rfpayordno){
         this.rfpayordno=rfpayordno;
    }
    public Date getRfpaydate(){
         return rfpaydate;
    }
    public void setRfpaydate(Date rfpaydate){
         this.rfpaydate=rfpaydate;
    }
    public String getBankcode(){
         return bankcode;
    }
    public void setBankcode(String bankcode){
         this.bankcode=bankcode;
    }
    public String getGetbankcode(){
         return getbankcode;
    }
    public void setGetbankcode(String getbankcode){
         this.getbankcode=getbankcode;
    }
    public String getCustpayacno(){
         return custpayacno;
    }
    public void setCustpayacno(String custpayacno){
         this.custpayacno=custpayacno;
    }
    public String getBankpayusernm(){
         return bankpayusernm;
    }
    public void setBankpayusernm(String bankpayusernm){
         this.bankpayusernm=bankpayusernm;
    }
    public String getMerpayacno(){
         return merpayacno;
    }
    public void setMerpayacno(String merpayacno){
         this.merpayacno=merpayacno;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getTxccy(){
         return txccy;
    }
    public void setTxccy(String txccy){
         this.txccy=txccy;
    }
    public long getRfamt(){
         return rfamt;
    }
    public void setRfamt(long rfamt){
         this.rfamt=rfamt;
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
    public String getRfsake(){
         return rfsake;
    }
    public void setRfsake(String rfsake){
         this.rfsake=rfsake;
    }
    public String getBanksts(){
         return banksts;
    }
    public void setBanksts(String banksts){
         this.banksts=banksts;
    }
    public String getOrdstatus(){
         return ordstatus;
    }
    public void setOrdstatus(String ordstatus){
         this.ordstatus=ordstatus;
    }
    public Date getObankstldate(){
         return obankstldate;
    }
    public void setObankstldate(Date obankstldate){
         this.obankstldate=obankstldate;
    }
    public String getObankjrnno(){
         return obankjrnno;
    }
    public void setObankjrnno(String obankjrnno){
         this.obankjrnno=obankjrnno;
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
    public String getReturl(){
         return returl;
    }
    public void setReturl(String returl){
         this.returl=returl;
    }
    public String getNotifyurl(){
         return notifyurl;
    }
    public void setNotifyurl(String notifyurl){
         this.notifyurl=notifyurl;
    }
    public String getBankerror(){
         return bankerror;
    }
    public void setBankerror(String bankerror){
         this.bankerror=bankerror;
    }
    public String getMerno(){
         return merno;
    }
    public void setMerno(String merno){
         this.merno=merno;
    }
    public Date getBnkdat(){
         return bnkdat;
    }
    public void setBnkdat(Date bnkdat){
         this.bnkdat=bnkdat;
    }
    public String getStlbatno(){
         return stlbatno;
    }
    public void setStlbatno(String stlbatno){
         this.stlbatno=stlbatno;
    }
    public Date getRfordtime(){
         return rfordtime;
    }
    public void setRfordtime(Date rfordtime){
         this.rfordtime=rfordtime;
    }
    public String getStlsts(){
         return stlsts;
    }
    public void setStlsts(String stlsts){
         this.stlsts=stlsts;
    }
    public String getBustyp(){
         return bustyp;
    }
    public void setBustyp(String bustyp){
         this.bustyp=bustyp;
    }
    public String getOperId(){
         return operId;
    }
    public void setOperId(String operId){
         this.operId=operId;
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
    public Date getRfreqdateStart() {
		return rfreqdateStart;
	}
	public void setRfreqdateStart(Date rfreqdateStart) {
		this.rfreqdateStart = rfreqdateStart;
	}
	public Date getRfreqdateEnd() {
		return rfreqdateEnd;
	}
	public void setRfreqdateEnd(Date rfreqdateEnd) {
		this.rfreqdateEnd = rfreqdateEnd;
	}
    public String getPayordno() {
		return payordno;
	}
	public void setPayordno(String payordno) {
		this.payordno = payordno;
	}
	public void setRfamt(Long rfamt) {
		this.rfamt = rfamt;
	}
	public void setFee(Long fee) {
		this.fee = fee;
	}
	public void setNetrecamt(Long netrecamt) {
		this.netrecamt = netrecamt;
	}
	public String toString(){
        String temp = "";
        temp = temp + "refordno="+refordno+"\n";
        temp = temp + "rfreqdate="+rfreqdate+"\n";
        temp = temp + "oriprdordno="+oriprdordno+"\n";
        temp = temp + "oripayordno="+oripayordno+"\n";
        temp = temp + "rfpayordno="+rfpayordno+"\n";
        temp = temp + "rfpaydate="+rfpaydate+"\n";
        temp = temp + "bankcode="+bankcode+"\n";
        temp = temp + "getbankcode="+getbankcode+"\n";
        temp = temp + "custpayacno="+custpayacno+"\n";
        temp = temp + "bankpayusernm="+bankpayusernm+"\n";
        temp = temp + "merpayacno="+merpayacno+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "txccy="+txccy+"\n";
        temp = temp + "rfamt="+rfamt+"\n";
        temp = temp + "fee="+fee+"\n";
        temp = temp + "netrecamt="+netrecamt+"\n";
        temp = temp + "rfsake="+rfsake+"\n";
        temp = temp + "banksts="+banksts+"\n";
        temp = temp + "ordstatus="+ordstatus+"\n";
        temp = temp + "obankstldate="+obankstldate+"\n";
        temp = temp + "obankjrnno="+obankjrnno+"\n";
        temp = temp + "bankstldate="+bankstldate+"\n";
        temp = temp + "bankjrnno="+bankjrnno+"\n";
        temp = temp + "returl="+returl+"\n";
        temp = temp + "notifyurl="+notifyurl+"\n";
        temp = temp + "bankerror="+bankerror+"\n";
        temp = temp + "merno="+merno+"\n";
        temp = temp + "bnkdat="+bnkdat+"\n";
        temp = temp + "stlbatno="+stlbatno+"\n";
        temp = temp + "rfordtime="+rfordtime+"\n";
        temp = temp + "stlsts="+stlsts+"\n";
        temp = temp + "bustyp="+bustyp+"\n";
        temp = temp + "operId="+operId+"\n";
        temp = temp + "filed1="+filed1+"\n";
        temp = temp + "filed2="+filed2+"\n";
        temp = temp + "filed3="+filed3+"\n";
        temp = temp + "filed4="+filed4+"\n";
        temp = temp + "filed5="+filed5+"\n";
        temp = temp + "rfreqdateStart="+rfreqdateStart+"\n";
        temp = temp + "rfreqdateEnd="+rfreqdateEnd+"\n";
        temp = temp + "payordno="+payordno+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("refordno",refordno);
        try{
            json.put("rfreqdate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rfreqdate));
        } catch (Exception e) {}
        json.put("oriprdordno",oriprdordno);
        json.put("oripayordno",oripayordno);
        json.put("rfpayordno",rfpayordno);
        try{
            json.put("rfpaydate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rfpaydate));
        } catch (Exception e) {}
        json.put("bankcode",bankcode);
        json.put("getbankcode",getbankcode);
        json.put("custpayacno",custpayacno);
        json.put("bankpayusernm",bankpayusernm);
        json.put("merpayacno",merpayacno);
        json.put("custId",custId);
        json.put("txccy",txccy);
        json.put("rfamt",String.format("%.2f",(float)rfamt*0.01));
        json.put("fee",String.format("%.2f",(float)fee*0.01));
        json.put("netrecamt",String.format("%.2f",(float)netrecamt*0.01));
        json.put("rfsake",rfsake);
        json.put("banksts",banksts);
        json.put("banksts",banksts);
        try{
            json.put("obankstldate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(obankstldate));
        } catch (Exception e) {}
        json.put("obankjrnno",obankjrnno);
        try{
            json.put("bankstldate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bankstldate));
        } catch (Exception e) {}
        json.put("bankjrnno",bankjrnno);
        json.put("returl",returl);
        json.put("notifyurl",notifyurl);
        json.put("bankerror",bankerror);
        json.put("merno",merno);
        try{
            json.put("bnkdat", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bnkdat));
        } catch (Exception e) {}
        json.put("stlbatno",stlbatno);
        try{
            json.put("rfordtime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rfordtime));
        } catch (Exception e) {}
        json.put("stlsts",stlsts);
        json.put("bustyp",bustyp);
        json.put("operId",operId);
        json.put("filed1",filed1);
        json.put("filed2",filed2);
        json.put("filed3",filed3);
        json.put("filed4",filed4);
        json.put("filed5",filed5);
        json.put("payordno",payordno);
        try{
            json.put("rfreqdateStart", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rfreqdateStart));
            json.put("rfreqdateEnd", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rfreqdateEnd));
        } catch (Exception e) {}
        return json;
    }
}