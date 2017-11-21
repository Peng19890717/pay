package com.pay.order.dao;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_PRODUCT_ORDER entity.
 * 
 * @author administrator
 */
public class PayProductOrder {
    public String id;
    public String prdordno;
    public String merno;
    public String versionid;
    public String prdordtype;
    public String biztype;
    public Date ordertime;
    public Long ordamt;
    public String ordstatus;
    public String ordccy;
    public String notifyurl;
    public String returl;
    public String receivePayNotifyUrl="";
    public String signature;
    public String signtype;
    public String verifystring;
    public String prddisurl;
    public String prdname;
    public String prdshortname;
    public String prddesc;
    public String merremark;
    public String rpttype;
    public Long prdunitprice;
    public Long buycount;
    public String defpayway;
    public String custId;
    public String merpayacno;
    public String sellrptacno;
    public Long sellrptamt;
    public String recacno1;
    public Long recamt1;
    public String recacno2;
    public Long recamt2;
    public String recacno3;
    public Long recamt3;
    public String recacno4;
    public String recamt4;
    public String recacno5;
    public Long recamt5;
    public String recacno6;
    public Long recamt6;
    public String recacno7;
    public Long recamt7;
    public String recacno8;
    public Long recamt8;
    public String recacno9;
    public Long recamt9;
    public String paymode;
    public String custpayacno;
    public String custrptacno;
    public String transort;
    public Long noncashamt;
    public String acjrnno;
    public Date acdate;
    public Date actime;
    public Long txncomamt;
    public Long rftotalamt;
    public Long rfcomamt;
    public String bankerror;
    public String payerror;
    public String stlsts;
    public String stlbatno;
    public String cpsflg;
    public String payordno;
    public Date cxtime;
    public String filed1;
    public String filed2;
    public String filed3;
    public String filed4;
    public String filed5;
    public String mobile="";
    public String guaranteeStatus="";
    public String credentialType="";
    public String credentialNo="";
    public String rechargeType="";
    
    public String getReceivePayNotifyUrl() {
		return receivePayNotifyUrl;
	}
	public void setReceivePayNotifyUrl(String receivePayNotifyUrl) {
		this.receivePayNotifyUrl = receivePayNotifyUrl;
	}
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getPrdordno(){
         return prdordno;
    }
    public void setPrdordno(String prdordno){
         this.prdordno=prdordno;
    }
    public String getMerno(){
         return merno;
    }
    public void setMerno(String merno){
         this.merno=merno;
    }
    
    public String getVersionid(){
         return versionid;
    }
    public void setVersionid(String versionid){
         this.versionid=versionid;
    }
    public String getPrdordtype(){
         return prdordtype;
    }
    public void setPrdordtype(String prdordtype){
         this.prdordtype=prdordtype;
    }
    public String getBiztype(){
         return biztype;
    }
    public void setBiztype(String biztype){
         this.biztype=biztype;
    }
    public Date getOrdertime(){
         return ordertime;
    }
    public void setOrdertime(Date ordertime){
         this.ordertime=ordertime;
    }
    public long getOrdamt(){
         return ordamt;
    }
    public void setOrdamt(long ordamt){
         this.ordamt=ordamt;
    }
    public String getOrdstatus(){
         return ordstatus;
    }
    public void setOrdstatus(String ordstatus){
         this.ordstatus=ordstatus;
    }
    public String getOrdccy(){
         return ordccy;
    }
    public void setOrdccy(String ordccy){
         this.ordccy=ordccy;
    }
    public String getNotifyurl(){
         return notifyurl;
    }
    public void setNotifyurl(String notifyurl){
         this.notifyurl=notifyurl;
    }
    public String getReturl(){
         return returl;
    }
    public void setReturl(String returl){
         this.returl=returl;
    }
    public String getSignature(){
         return signature;
    }
    public void setSignature(String signature){
         this.signature=signature;
    }
    public String getSigntype(){
         return signtype;
    }
    public void setSigntype(String signtype){
         this.signtype=signtype;
    }
    public String getVerifystring(){
         return verifystring;
    }
    public void setVerifystring(String verifystring){
         this.verifystring=verifystring;
    }
    public String getPrddisurl(){
         return prddisurl;
    }
    public void setPrddisurl(String prddisurl){
         this.prddisurl=prddisurl;
    }
    public String getPrdname(){
         return prdname;
    }
    public void setPrdname(String prdname){
         this.prdname=prdname;
    }
    public String getPrdshortname(){
         return prdshortname;
    }
    public void setPrdshortname(String prdshortname){
         this.prdshortname=prdshortname;
    }
    public String getPrddesc(){
         return prddesc;
    }
    public void setPrddesc(String prddesc){
         this.prddesc=prddesc;
    }
    public String getMerremark(){
         return merremark;
    }
    public void setMerremark(String merremark){
         this.merremark=merremark;
    }
    public String getRpttype(){
         return rpttype;
    }
    public void setRpttype(String rpttype){
         this.rpttype=rpttype;
    }
    public Long getPrdunitprice(){
         return prdunitprice;
    }
    public void setPrdunitprice(Long prdunitprice){
         this.prdunitprice=prdunitprice;
    }
    public long getBuycount(){
         return buycount;
    }
    public void setBuycount(long buycount){
         this.buycount=buycount;
    }
    public String getDefpayway(){
         return defpayway;
    }
    public void setDefpayway(String defpayway){
         this.defpayway=defpayway;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getMerpayacno(){
         return merpayacno;
    }
    public void setMerpayacno(String merpayacno){
         this.merpayacno=merpayacno;
    }
    public String getSellrptacno(){
         return sellrptacno;
    }
    public void setSellrptacno(String sellrptacno){
         this.sellrptacno=sellrptacno;
    }
    public long getSellrptamt(){
         return sellrptamt;
    }
    public void setSellrptamt(long sellrptamt){
         this.sellrptamt=sellrptamt;
    }
    public String getRecacno1(){
         return recacno1;
    }
    public void setRecacno1(String recacno1){
         this.recacno1=recacno1;
    }
    public long getRecamt1(){
         return recamt1;
    }
    public void setRecamt1(long recamt1){
         this.recamt1=recamt1;
    }
    public String getRecacno2(){
         return recacno2;
    }
    public void setRecacno2(String recacno2){
         this.recacno2=recacno2;
    }
    public long getRecamt2(){
         return recamt2;
    }
    public void setRecamt2(long recamt2){
         this.recamt2=recamt2;
    }
    public String getRecacno3(){
         return recacno3;
    }
    public void setRecacno3(String recacno3){
         this.recacno3=recacno3;
    }
    public long getRecamt3(){
         return recamt3;
    }
    public void setRecamt3(long recamt3){
         this.recamt3=recamt3;
    }
    public String getRecacno4(){
         return recacno4;
    }
    public void setRecacno4(String recacno4){
         this.recacno4=recacno4;
    }
    public String getRecamt4(){
         return recamt4;
    }
    public void setRecamt4(String recamt4){
         this.recamt4=recamt4;
    }
    public String getRecacno5(){
         return recacno5;
    }
    public void setRecacno5(String recacno5){
         this.recacno5=recacno5;
    }
    public long getRecamt5(){
         return recamt5;
    }
    public void setRecamt5(long recamt5){
         this.recamt5=recamt5;
    }
    public String getRecacno6(){
         return recacno6;
    }
    public void setRecacno6(String recacno6){
         this.recacno6=recacno6;
    }
    public long getRecamt6(){
         return recamt6;
    }
    public void setRecamt6(long recamt6){
         this.recamt6=recamt6;
    }
    public String getRecacno7(){
         return recacno7;
    }
    public void setRecacno7(String recacno7){
         this.recacno7=recacno7;
    }
    public long getRecamt7(){
         return recamt7;
    }
    public void setRecamt7(long recamt7){
         this.recamt7=recamt7;
    }
    public String getRecacno8(){
         return recacno8;
    }
    public void setRecacno8(String recacno8){
         this.recacno8=recacno8;
    }
    public long getRecamt8(){
         return recamt8;
    }
    public void setRecamt8(long recamt8){
         this.recamt8=recamt8;
    }
    public String getRecacno9(){
         return recacno9;
    }
    public void setRecacno9(String recacno9){
         this.recacno9=recacno9;
    }
    public long getRecamt9(){
         return recamt9;
    }
    public void setRecamt9(long recamt9){
         this.recamt9=recamt9;
    }
    public String getPaymode(){
         return paymode;
    }
    public void setPaymode(String paymode){
         this.paymode=paymode;
    }
    public String getCustpayacno(){
         return custpayacno;
    }
    public void setCustpayacno(String custpayacno){
         this.custpayacno=custpayacno;
    }
    public String getCustrptacno(){
         return custrptacno;
    }
    public void setCustrptacno(String custrptacno){
         this.custrptacno=custrptacno;
    }
    public String getTransort(){
         return transort;
    }
    public void setTransort(String transort){
         this.transort=transort;
    }
    public long getNoncashamt(){
         return noncashamt;
    }
    public void setNoncashamt(long noncashamt){
         this.noncashamt=noncashamt;
    }
    public String getAcjrnno(){
         return acjrnno;
    }
    public void setAcjrnno(String acjrnno){
         this.acjrnno=acjrnno;
    }
    public Date getAcdate(){
         return acdate;
    }
    public void setAcdate(Date acdate){
         this.acdate=acdate;
    }
    public Date getActime(){
         return actime;
    }
    public void setActime(Date actime){
         this.actime=actime;
    }
    public long getTxncomamt(){
         return txncomamt;
    }
    public void setTxncomamt(long txncomamt){
         this.txncomamt=txncomamt;
    }
    public long getRftotalamt(){
         return rftotalamt;
    }
    public void setRftotalamt(long rftotalamt){
         this.rftotalamt=rftotalamt;
    }
    public long getRfcomamt(){
         return rfcomamt;
    }
    public void setRfcomamt(long rfcomamt){
         this.rfcomamt=rfcomamt;
    }
    public String getBankerror(){
         return bankerror;
    }
    public void setBankerror(String bankerror){
         this.bankerror=bankerror;
    }
    public String getPayerror(){
         return payerror;
    }
    public void setPayerror(String payerror){
         this.payerror=payerror;
    }
    public String getStlsts(){
         return stlsts;
    }
    public void setStlsts(String stlsts){
         this.stlsts=stlsts;
    }
    public String getStlbatno(){
         return stlbatno;
    }
    public void setStlbatno(String stlbatno){
         this.stlbatno=stlbatno;
    }
    public String getCpsflg(){
         return cpsflg;
    }
    public void setCpsflg(String cpsflg){
         this.cpsflg=cpsflg;
    }
    public String getPayordno(){
         return payordno;
    }
    public void setPayordno(String payordno){
         this.payordno=payordno;
    }
    public Date getCxtime(){
         return cxtime;
    }
    public void setCxtime(Date cxtime){
         this.cxtime=cxtime;
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
    
    public String getGuaranteeStatus() {
		return guaranteeStatus;
	}
	public void setGuaranteeStatus(String guaranteeStatus) {
		this.guaranteeStatus = guaranteeStatus;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "prdordno="+prdordno+"\n";
        temp = temp + "merno="+merno+"\n";
        temp = temp + "versionid="+versionid+"\n";
        temp = temp + "prdordtype="+prdordtype+"\n";
        temp = temp + "biztype="+biztype+"\n";
        temp = temp + "ordertime="+ordertime+"\n";
        temp = temp + "ordamt="+ordamt+"\n";
        temp = temp + "ordstatus="+ordstatus+"\n";
        temp = temp + "ordccy="+ordccy+"\n";
        temp = temp + "notifyurl="+notifyurl+"\n";
        temp = temp + "returl="+returl+"\n";
        temp = temp + "signature="+signature+"\n";
        temp = temp + "signtype="+signtype+"\n";
        temp = temp + "verifystring="+verifystring+"\n";
        temp = temp + "prddisurl="+prddisurl+"\n";
        temp = temp + "prdname="+prdname+"\n";
        temp = temp + "prdshortname="+prdshortname+"\n";
        temp = temp + "prddesc="+prddesc+"\n";
        temp = temp + "merremark="+merremark+"\n";
        temp = temp + "rpttype="+rpttype+"\n";
        temp = temp + "prdunitprice="+prdunitprice+"\n";
        temp = temp + "buycount="+buycount+"\n";
        temp = temp + "defpayway="+defpayway+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "merpayacno="+merpayacno+"\n";
        temp = temp + "sellrptacno="+sellrptacno+"\n";
        temp = temp + "sellrptamt="+sellrptamt+"\n";
        temp = temp + "recacno1="+recacno1+"\n";
        temp = temp + "recamt1="+recamt1+"\n";
        temp = temp + "recacno2="+recacno2+"\n";
        temp = temp + "recamt2="+recamt2+"\n";
        temp = temp + "recacno3="+recacno3+"\n";
        temp = temp + "recamt3="+recamt3+"\n";
        temp = temp + "recacno4="+recacno4+"\n";
        temp = temp + "recamt4="+recamt4+"\n";
        temp = temp + "recacno5="+recacno5+"\n";
        temp = temp + "recamt5="+recamt5+"\n";
        temp = temp + "recacno6="+recacno6+"\n";
        temp = temp + "recamt6="+recamt6+"\n";
        temp = temp + "recacno7="+recacno7+"\n";
        temp = temp + "recamt7="+recamt7+"\n";
        temp = temp + "recacno8="+recacno8+"\n";
        temp = temp + "recamt8="+recamt8+"\n";
        temp = temp + "recacno9="+recacno9+"\n";
        temp = temp + "recamt9="+recamt9+"\n";
        temp = temp + "paymode="+paymode+"\n";
        temp = temp + "custpayacno="+custpayacno+"\n";
        temp = temp + "custrptacno="+custrptacno+"\n";
        temp = temp + "transort="+transort+"\n";
        temp = temp + "noncashamt="+noncashamt+"\n";
        temp = temp + "acjrnno="+acjrnno+"\n";
        temp = temp + "acdate="+acdate+"\n";
        temp = temp + "actime="+actime+"\n";
        temp = temp + "txncomamt="+txncomamt+"\n";
        temp = temp + "rftotalamt="+rftotalamt+"\n";
        temp = temp + "rfcomamt="+rfcomamt+"\n";
        temp = temp + "bankerror="+bankerror+"\n";
        temp = temp + "payerror="+payerror+"\n";
        temp = temp + "stlsts="+stlsts+"\n";
        temp = temp + "stlbatno="+stlbatno+"\n";
        temp = temp + "cpsflg="+cpsflg+"\n";
        temp = temp + "payordno="+payordno+"\n";
        temp = temp + "cxtime="+cxtime+"\n";
        temp = temp + "filed1="+filed1+"\n";
        temp = temp + "filed2="+filed2+"\n";
        temp = temp + "filed3="+filed3+"\n";
        temp = temp + "filed4="+filed4+"\n";
        temp = temp + "filed5="+filed5+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("prdordno",prdordno);
        json.put("merno",merno);
        json.put("versionid",versionid);
        json.put("prdordtype",prdordtype);
        json.put("biztype",biztype);
        try{
            json.put("ordertime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ordertime));
        } catch (Exception e) {}
        json.put("ordamt",String.valueOf(ordamt));
        json.put("ordstatus",ordstatus);
        json.put("ordccy",ordccy);
        json.put("notifyurl",notifyurl);
        json.put("returl",returl);
        json.put("signature",signature);
        json.put("signtype",signtype);
        json.put("verifystring",verifystring);
        json.put("prddisurl",prddisurl);
        json.put("prdname",prdname);
        json.put("prdshortname",prdshortname);
        json.put("prddesc",prddesc);
        json.put("merremark",merremark);
        json.put("rpttype",rpttype);
        json.put("prdunitprice",prdunitprice);
        json.put("buycount",String.valueOf(buycount));
        json.put("defpayway",defpayway);
        json.put("custId",custId);
        json.put("merpayacno",merpayacno);
        json.put("sellrptacno",sellrptacno);
        json.put("sellrptamt",String.valueOf(sellrptamt));
        json.put("recacno1",recacno1);
        json.put("recamt1",String.valueOf(recamt1));
        json.put("recacno2",recacno2);
        json.put("recamt2",String.valueOf(recamt2));
        json.put("recacno3",recacno3);
        json.put("recamt3",String.valueOf(recamt3));
        json.put("recacno4",recacno4);
        json.put("recamt4",recamt4);
        json.put("recacno5",recacno5);
        json.put("recamt5",String.valueOf(recamt5));
        json.put("recacno6",recacno6);
        json.put("recamt6",String.valueOf(recamt6));
        json.put("recacno7",recacno7);
        json.put("recamt7",String.valueOf(recamt7));
        json.put("recacno8",recacno8);
        json.put("recamt8",String.valueOf(recamt8));
        json.put("recacno9",recacno9);
        json.put("recamt9",String.valueOf(recamt9));
        json.put("paymode",paymode);
        json.put("custpayacno",custpayacno);
        json.put("custrptacno",custrptacno);
        json.put("transort",transort);
        json.put("noncashamt",String.valueOf(noncashamt));
        json.put("acjrnno",acjrnno);
        try{
            json.put("acdate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(acdate));
        } catch (Exception e) {}
        try{
            json.put("actime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actime));
        } catch (Exception e) {}
        json.put("txncomamt",String.valueOf(txncomamt));
        json.put("rftotalamt",String.valueOf(rftotalamt));
        json.put("rfcomamt",String.valueOf(rfcomamt));
        json.put("bankerror",bankerror);
        json.put("payerror",payerror);
        json.put("stlsts",stlsts);
        json.put("stlbatno",stlbatno);
        json.put("cpsflg",cpsflg);
        json.put("payordno",payordno);
        try{
            json.put("cxtime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cxtime));
        } catch (Exception e) {}
        json.put("filed1",filed1);
        json.put("filed2",filed2);
        json.put("filed3",filed3);
        json.put("filed4",filed4);
        json.put("filed5",filed5);
        json.put("mobile",mobile);
        json.put("guaranteeStatus",guaranteeStatus);
        json.put("credentialType",credentialType);
        json.put("credentialNo",credentialNo);
        json.put("rechargeType",rechargeType);
        return json;
    }
}