package com.pay.order.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_ACC_PROFILE entity.
 * 
 * @author administrator
 */
public class PayAccProfile {
    public String id;
    public String payAcNo;
    public String acType;
    public String brNo;
    public String ccy;
    public Long cashAcBal;
    public Long consAcBal;
    public Long minStlBalance;
    public Long frozBalance;
    public String glCode;
    public String acStatus;
    public String listStsFlg;
    public String resvFlg;
    public Long accSumNum;
    public Date lstTxTime;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getPayAcNo(){
         return payAcNo;
    }
    public void setPayAcNo(String payAcNo){
         this.payAcNo=payAcNo;
    }
    public String getAcType(){
         return acType;
    }
    public void setAcType(String acType){
         this.acType=acType;
    }
    public String getBrNo(){
         return brNo;
    }
    public void setBrNo(String brNo){
         this.brNo=brNo;
    }
    public String getCcy(){
         return ccy;
    }
    public void setCcy(String ccy){
         this.ccy=ccy;
    }
    public Long getCashAcBal(){
         return cashAcBal;
    }
    public void setCashAcBal(Long cashAcBal){
         this.cashAcBal=cashAcBal;
    }
    public Long getConsAcBal(){
         return consAcBal;
    }
    public void setConsAcBal(Long consAcBal){
         this.consAcBal=consAcBal;
    }
    public Long getMinStlBalance(){
         return minStlBalance;
    }
    public void setMinStlBalance(Long minStlBalance){
         this.minStlBalance=minStlBalance;
    }
    public Long getFrozBalance(){
         return frozBalance;
    }
    public void setFrozBalance(Long frozBalance){
         this.frozBalance=frozBalance;
    }
    public String getGlCode(){
         return glCode;
    }
    public void setGlCode(String glCode){
         this.glCode=glCode;
    }
    public String getAcStatus(){
         return acStatus;
    }
    public void setAcStatus(String acStatus){
         this.acStatus=acStatus;
    }
    public String getListStsFlg(){
         return listStsFlg;
    }
    public void setListStsFlg(String listStsFlg){
         this.listStsFlg=listStsFlg;
    }
    public String getResvFlg(){
         return resvFlg;
    }
    public void setResvFlg(String resvFlg){
         this.resvFlg=resvFlg;
    }
    public Long getAccSumNum(){
         return accSumNum;
    }
    public void setAccSumNum(Long accSumNum){
         this.accSumNum=accSumNum;
    }
    public Date getLstTxTime(){
         return lstTxTime;
    }
    public void setLstTxTime(Date lstTxTime){
         this.lstTxTime=lstTxTime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "payAcNo="+payAcNo+"\n";
        temp = temp + "acType="+acType+"\n";
        temp = temp + "brNo="+brNo+"\n";
        temp = temp + "ccy="+ccy+"\n";
        temp = temp + "cashAcBal="+cashAcBal+"\n";
        temp = temp + "consAcBal="+consAcBal+"\n";
        temp = temp + "minStlBalance="+minStlBalance+"\n";
        temp = temp + "frozBalance="+frozBalance+"\n";
        temp = temp + "glCode="+glCode+"\n";
        temp = temp + "acStatus="+acStatus+"\n";
        temp = temp + "listStsFlg="+listStsFlg+"\n";
        temp = temp + "resvFlg="+resvFlg+"\n";
        temp = temp + "accSumNum="+accSumNum+"\n";
        temp = temp + "lstTxTime="+lstTxTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("payAcNo",payAcNo);
        json.put("acType",acType);
        json.put("brNo",brNo);
        json.put("ccy",ccy);
        json.put("cashAcBal",String.valueOf(cashAcBal));
        json.put("consAcBal",String.valueOf(consAcBal));
        json.put("minStlBalance",String.valueOf(minStlBalance));
        json.put("frozBalance",String.valueOf(frozBalance));
        json.put("glCode",glCode);
        json.put("acStatus",acStatus);
        json.put("listStsFlg",listStsFlg);
        json.put("resvFlg",resvFlg);
        json.put("accSumNum",String.valueOf(accSumNum));
        try{
            json.put("lstTxTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lstTxTime));
        } catch (Exception e) {}
        return json;
    }
}