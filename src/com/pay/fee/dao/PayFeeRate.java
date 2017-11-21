package com.pay.fee.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_FEE_RATE entity.
 * 
 * @author administrator
 */
public class PayFeeRate {
    public String feeCode;
    public String feeName;
    public String ccy;
    public String calMode;
    public String multiSectionMode;
    public Long maxFee = 0l;
    public Long minFee = 0l;
    public Long startCalAmt = 0l;
    public Date createTime;
    public String createUser;
    public String createUserName;
    public Date lastUpdTime;
    public String lastUpdUser;
    public String bizType;
    public String feeInfo;
    public String custType;
    public String zeroFeeFlag;
    public List<PayFeeSection> sectionList = new ArrayList<PayFeeSection>();
    
    public String tranType;
    
    public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getFeeCode(){
         return feeCode;
    }
    public void setFeeCode(String feeCode){
         this.feeCode=feeCode;
    }
    public String getFeeName(){
         return feeName;
    }
    public void setFeeName(String feeName){
         this.feeName=feeName;
    }
    public String getCcy(){
         return ccy;
    }
    public void setCcy(String ccy){
         this.ccy=ccy;
    }
    public String getCalMode(){
         return calMode;
    }
    public void setCalMode(String calMode){
         this.calMode=calMode;
    }
    public String getMultiSectionMode(){
         return multiSectionMode;
    }
    public void setMultiSectionMode(String multiSectionMode){
         this.multiSectionMode=multiSectionMode;
    }
    public long getMaxFee(){
         return maxFee;
    }
    public void setMaxFee(long maxFee){
         this.maxFee=maxFee;
    }
    public long getMinFee(){
         return minFee;
    }
    public void setMinFee(long minFee){
         this.minFee=minFee;
    }
    public long getStartCalAmt(){
         return startCalAmt;
    }
    public void setStartCalAmt(long startCalAmt){
         this.startCalAmt=startCalAmt;
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
    
    public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
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
    public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getFeeInfo() {
		return feeInfo;
	}
	public void setFeeInfo(String feeInfo) {
		this.feeInfo = feeInfo;
	}
	public void setMaxFee(Long maxFee) {
		this.maxFee = maxFee;
	}
	public void setMinFee(Long minFee) {
		this.minFee = minFee;
	}
	public String getCustType(){
        return custType;
    }
    public void setCustType(String custType){
        this.custType=custType;
    }
	public String toString(){
        String temp = "";
        temp = temp + "feeCode="+feeCode+"\n";
        temp = temp + "feeName="+feeName+"\n";
        temp = temp + "ccy="+ccy+"\n";
        temp = temp + "calMode="+calMode+"\n";
        temp = temp + "multiSectionMode="+multiSectionMode+"\n";
        temp = temp + "maxFee="+maxFee+"\n";
        temp = temp + "minFee="+minFee+"\n";
        temp = temp + "startCalAmt="+startCalAmt+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "createUser="+createUser+"\n";
        temp = temp + "lastUpdTime="+lastUpdTime+"\n";
        temp = temp + "lastUpdUser="+lastUpdUser+"\n";
        temp = temp + "bizType="+bizType+"\n";
        temp = temp + "feeInfo="+feeInfo+"\n";
        temp = temp + "custType="+custType+"\n";
        temp = temp + "tranType="+tranType+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("feeCode",feeCode);
        json.put("feeName",feeName);
        json.put("ccy",ccy);
        json.put("calMode","0".equals(calMode)?"定额":("1".equals(calMode)?"比例":"未知"));
        json.put("multiSectionMode","0".equals(multiSectionMode)?"不套档":
        	("1".equals(multiSectionMode)?"套档":("2".equals(multiSectionMode)?"分段累计":"未知")));
        json.put("maxFee",String.format("%.2f", ((double)maxFee)/100d));
        json.put("minFee",String.format("%.2f", ((double)minFee)/100d));
        json.put("startCalAmt",String.format("%.2f", ((double)startCalAmt)/100d));
        json.put("tranType",tranType);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        json.put("createUser",createUser);
        json.put("createUserName",createUserName);
        try{
            json.put("lastUpdTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastUpdTime));
        } catch (Exception e) {}
        json.put("lastUpdUser",lastUpdUser);
        json.put("bizType",bizType);
        if(feeInfo != null){
        	String [] fs = feeInfo.split(";");
        	String tmp = "";
	        for(int i = 0; i<fs.length; i++){
	        	String [] es = fs[i].split(",");
	        	if(es.length != 3)continue;
	        	String [] fees = es[0].split("-");
	        	if(fees.length != 2)continue;
	        	tmp = tmp + String.format("%.2f", ((double)Double.parseDouble(fees[0]))/100d)+"~"+
	        			String.format("%.2f", ((double)Double.parseDouble(fees[1]))/100d)+","+
	        			("0".equals(es[1])?"定额":"比例")+","+
	        			("0".equals(es[1])?String.format("%.2f", ((double)Double.parseDouble(es[2]))/100d):es[2]+"%")+";";
	        }
	        if(tmp.length()>0)json.put("feeInfo",tmp.substring(0,tmp.length()-1));
	        else json.put("feeInfo","");
        } else json.put("feeInfo","");
        json.put("custType",custType);
        json.put("zeroFeeFlag",zeroFeeFlag);
        return json;
    }
}