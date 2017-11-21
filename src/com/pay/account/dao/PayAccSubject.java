package com.pay.account.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_ACC_SUBJECT entity.
 * 
 * @author administrator
 */
public class PayAccSubject {
    public String glCode;
    public String glName;
    public Date efctDate;
    public Date expiredDate;
    public String hasSl;
    public String hasDl;
    public String glType;
    public String debitCredit;
    public String zeroFlag;
    public String entBkGl;
    public Date createTime;
    public String createUser;
    public Date lastUpdTime;
    public String lastUpdUser;
    public Date opnTime;
    public String opnTlr;
    public Date clsTime;
    public String clsTlr;
    public Date lastTxnDate;
    public String lastTxnTlr;
    public Long preDayAccBal;
    public Long accBal;
    public Long fltAccBal;
    public Long drAccNum;
    public Long crAccNum;
    public String manualBkFlag;
    public String totalChkFlag;
    
    public String createName;
    public String lastUpdName;
    
    
    public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getLastUpdName() {
		return lastUpdName;
	}
	public void setLastUpdName(String lastUpdName) {
		this.lastUpdName = lastUpdName;
	}
	public String getGlCode(){
         return glCode;
    }
    public void setGlCode(String glCode){
         this.glCode=glCode;
    }
    public String getGlName(){
         return glName;
    }
    public void setGlName(String glName){
         this.glName=glName;
    }
    public Date getEfctDate(){
         return efctDate;
    }
    public void setEfctDate(Date efctDate){
         this.efctDate=efctDate;
    }
    public Date getExpiredDate(){
         return expiredDate;
    }
    public void setExpiredDate(Date expiredDate){
         this.expiredDate=expiredDate;
    }
    public String getHasSl(){
         return hasSl;
    }
    public void setHasSl(String hasSl){
         this.hasSl=hasSl;
    }
    public String getHasDl(){
         return hasDl;
    }
    public void setHasDl(String hasDl){
         this.hasDl=hasDl;
    }
    public String getGlType(){
         return glType;
    }
    public void setGlType(String glType){
         this.glType=glType;
    }
    public String getDebitCredit(){
         return debitCredit;
    }
    public void setDebitCredit(String debitCredit){
         this.debitCredit=debitCredit;
    }
    public String getZeroFlag(){
         return zeroFlag;
    }
    public void setZeroFlag(String zeroFlag){
         this.zeroFlag=zeroFlag;
    }
    public String getEntBkGl(){
         return entBkGl;
    }
    public void setEntBkGl(String entBkGl){
         this.entBkGl=entBkGl;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String getCreateUser(){
         return createUser;
    }
    public void setCreateUser(String createUser){
         this.createUser=createUser;
    }
    public Date getLastUpdTime(){
         return lastUpdTime;
    }
    public void setLastUpdTime(Date lastUpdTime){
         this.lastUpdTime=lastUpdTime;
    }
    public String getLastUpdUser(){
         return lastUpdUser;
    }
    public void setLastUpdUser(String lastUpdUser){
         this.lastUpdUser=lastUpdUser;
    }
    public Date getOpnTime(){
         return opnTime;
    }
    public void setOpnTime(Date opnTime){
         this.opnTime=opnTime;
    }
    public String getOpnTlr(){
         return opnTlr;
    }
    public void setOpnTlr(String opnTlr){
         this.opnTlr=opnTlr;
    }
    public Date getClsTime(){
         return clsTime;
    }
    public void setClsTime(Date clsTime){
         this.clsTime=clsTime;
    }
    public String getClsTlr(){
         return clsTlr;
    }
    public void setClsTlr(String clsTlr){
         this.clsTlr=clsTlr;
    }
    public Date getLastTxnDate(){
         return lastTxnDate;
    }
    public void setLastTxnDate(Date lastTxnDate){
         this.lastTxnDate=lastTxnDate;
    }
    public String getLastTxnTlr(){
         return lastTxnTlr;
    }
    public void setLastTxnTlr(String lastTxnTlr){
         this.lastTxnTlr=lastTxnTlr;
    }
    public Long getPreDayAccBal(){
         return preDayAccBal;
    }
    public void setPreDayAccBal(Long preDayAccBal){
         this.preDayAccBal=preDayAccBal;
    }
    public Long getAccBal(){
         return accBal;
    }
    public void setAccBal(Long accBal){
         this.accBal=accBal;
    }
    public Long getFltAccBal(){
         return fltAccBal;
    }
    public void setFltAccBal(Long fltAccBal){
         this.fltAccBal=fltAccBal;
    }
    public Long getDrAccNum(){
         return drAccNum;
    }
    public void setDrAccNum(Long drAccNum){
         this.drAccNum=drAccNum;
    }
    public Long getCrAccNum(){
         return crAccNum;
    }
    public void setCrAccNum(Long crAccNum){
         this.crAccNum=crAccNum;
    }
    public String getManualBkFlag(){
         return manualBkFlag;
    }
    public void setManualBkFlag(String manualBkFlag){
         this.manualBkFlag=manualBkFlag;
    }
    public String getTotalChkFlag(){
         return totalChkFlag;
    }
    public void setTotalChkFlag(String totalChkFlag){
         this.totalChkFlag=totalChkFlag;
    }
    public String toString(){
        String temp = "";
        temp = temp + "glCode="+glCode+"\n";
        temp = temp + "glName="+glName+"\n";
        temp = temp + "efctDate="+efctDate+"\n";
        temp = temp + "expiredDate="+expiredDate+"\n";
        temp = temp + "hasSl="+hasSl+"\n";
        temp = temp + "hasDl="+hasDl+"\n";
        temp = temp + "glType="+glType+"\n";
        temp = temp + "debitCredit="+debitCredit+"\n";
        temp = temp + "zeroFlag="+zeroFlag+"\n";
        temp = temp + "entBkGl="+entBkGl+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "createUser="+createUser+"\n";
        temp = temp + "lastUpdTime="+lastUpdTime+"\n";
        temp = temp + "lastUpdUser="+lastUpdUser+"\n";
        temp = temp + "opnTime="+opnTime+"\n";
        temp = temp + "opnTlr="+opnTlr+"\n";
        temp = temp + "clsTime="+clsTime+"\n";
        temp = temp + "clsTlr="+clsTlr+"\n";
        temp = temp + "lastTxnDate="+lastTxnDate+"\n";
        temp = temp + "lastTxnTlr="+lastTxnTlr+"\n";
        temp = temp + "preDayAccBal="+preDayAccBal+"\n";
        temp = temp + "accBal="+accBal+"\n";
        temp = temp + "fltAccBal="+fltAccBal+"\n";
        temp = temp + "drAccNum="+drAccNum+"\n";
        temp = temp + "crAccNum="+crAccNum+"\n";
        temp = temp + "manualBkFlag="+manualBkFlag+"\n";
        temp = temp + "totalChkFlag="+totalChkFlag+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("glCode",glCode);
        json.put("glName",glName);
        try{
            json.put("efctDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(efctDate));
        } catch (Exception e) {}
        try{
            json.put("expiredDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredDate));
        } catch (Exception e) {}
        json.put("hasSl",hasSl);
        json.put("hasDl",hasDl);
        json.put("glType",glType);
        json.put("debitCredit",debitCredit);
        json.put("zeroFlag",zeroFlag);
        json.put("entBkGl",entBkGl);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        json.put("createUser",createUser);
        try{
            json.put("lastUpdTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastUpdTime));
        } catch (Exception e) {}
        json.put("lastUpdUser",lastUpdUser);
        try{
            json.put("opnTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(opnTime));
        } catch (Exception e) {}
        json.put("opnTlr",opnTlr);
        try{
            json.put("clsTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(clsTime));
        } catch (Exception e) {}
        json.put("clsTlr",clsTlr);
        try{
            json.put("lastTxnDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastTxnDate));
        } catch (Exception e) {}
        json.put("lastTxnTlr",lastTxnTlr);
        json.put("preDayAccBal",String.valueOf(preDayAccBal));
        json.put("accBal",String.valueOf(accBal));
        json.put("fltAccBal",String.valueOf(fltAccBal));
        json.put("drAccNum",String.valueOf(drAccNum));
        json.put("crAccNum",String.valueOf(crAccNum));
        json.put("manualBkFlag",manualBkFlag);
        json.put("totalChkFlag",totalChkFlag);
        json.put("createName",createName);
        json.put("lastUpdName",lastUpdName);
        return json;
    }
}