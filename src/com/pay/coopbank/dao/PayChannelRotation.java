package com.pay.coopbank.dao;

import org.json.JSONException;
import org.json.JSONObject;

import com.pay.coopbank.service.PayCoopBankService;

import java.util.Date;
/**
 * Table PAY_CHANNEL_ROTATION entity.
 * @author administrator
 */
public class PayChannelRotation { 
    public String id;
    public String type;
    public String channelId;
    public String merchantId;
    public String md5Key;
    public String status;
    public Date createTime;
    public Date createTimeStart;
    public Date createTimeEnd;
    public Date lastSucUsedTime;
    public Long lastSucTranAmt = 0l;
    public String batchNo;
    public Date cancelTime;
    public Long sumTranAmt = 0l;
    public Long sumSuccessTranAmt = 0l;
    public Long sumTranAccount = 0l;
    public Long sumSuccessTranAccount = 0l;
    public Long sumWithDrawTranAmt = 0l;
    public long sumDayCountZfbScan=0l;
    public long sumDayCountWeixinScan=0l;
    public long sumDayCountQqScan=0l;
    public long sumDaySucCountZfbScan=0l;
    public long sumDaySucCountWeixinScan=0l;
    public long sumDaySucCountQqScan=0l;
    public long sumDayAmtZfbScan=0l;
    public long sumDayAmtWeixinScan=0l;
    public long sumDayAmtQqScan=0l;
    public long sumDaySucAmtZfbScan=0l;
    public long sumDaySucAmtWeixinScan=0l;
    public long sumDaySucAmtQqScan=0l;
    public Long maxSumAmt = 0l;
    public String param1;
    public String param2;
    public String param3;
    public String param4;
    public String param5;
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
    public String getChannelId(){
         return channelId;
    }
    public void setChannelId(String channelId){
         this.channelId=channelId;
    }
    public String getMerchantId(){
         return merchantId;
    }
    public void setMerchantId(String merchantId){
         this.merchantId=merchantId;
    }
    public String getMd5Key(){
         return md5Key;
    }
    public void setMd5Key(String md5Key){
         this.md5Key=md5Key;
    }
    public String getStatus(){
         return status;
    }
    public void setStatus(String status){
         this.status=status;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    
    public String getBatchNo(){
         return batchNo;
    }
    public void setBatchNo(String batchNo){
         this.batchNo=batchNo;
    }
    public Date getCancelTime(){
         return cancelTime;
    }
    public void setCancelTime(Date cancelTime){
         this.cancelTime=cancelTime;
    }
    
    public Long getSumTranAmt() {
		return sumTranAmt;
	}
	public void setSumTranAmt(Long sumTranAmt) {
		this.sumTranAmt = sumTranAmt;
	}
	public Long getSumSuccessTranAmt() {
		return sumSuccessTranAmt;
	}
	public void setSumSuccessTranAmt(Long sumSuccessTranAmt) {
		this.sumSuccessTranAmt = sumSuccessTranAmt;
	}
	public Long getSumWithDrawTranAmt() {
		return sumWithDrawTranAmt;
	}
	public void setSumWithDrawTranAmt(Long sumWithDrawTranAmt) {
		this.sumWithDrawTranAmt = sumWithDrawTranAmt;
	}
	public Long getSumTranAccount() {
		return sumTranAccount;
	}
	public void setSumTranAccount(Long sumTranAccount) {
		this.sumTranAccount = sumTranAccount;
	}
	
	public Long getSumSuccessTranAccount() {
		return sumSuccessTranAccount;
	}
	public void setSumSuccessTranAccount(Long sumSuccessTranAccount) {
		this.sumSuccessTranAccount = sumSuccessTranAccount;
	}
	
	public Date getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	public Long getMaxSumAmt() {
		return maxSumAmt;
	}
	public void setMaxSumAmt(Long maxSumAmt) {
		this.maxSumAmt = maxSumAmt;
	}
	public Date getLastSucUsedTime() {
		return lastSucUsedTime;
	}
	public void setLastSucUsedTime(Date lastSucUsedTime) {
		this.lastSucUsedTime = lastSucUsedTime;
	}
	public Long getLastSucTranAmt() {
		return lastSucTranAmt;
	}
	public void setLastSucTranAmt(Long lastSucTranAmt) {
		this.lastSucTranAmt = lastSucTranAmt;
	}
	public String getParam1() {
		return param1;
	}
	public void setParam1(String param1) {
		this.param1 = param1;
	}
	public String getParam2() {
		return param2;
	}
	public void setParam2(String param2) {
		this.param2 = param2;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public String getParam4() {
		return param4;
	}
	public void setParam4(String param4) {
		this.param4 = param4;
	}
	public String getParam5() {
		return param5;
	}
	public void setParam5(String param5) {
		this.param5 = param5;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "type="+type+"\n";
        temp = temp + "channelId="+channelId+"\n";
        temp = temp + "merchantId="+merchantId+"\n";
        temp = temp + "md5Key="+md5Key+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "lastSucUsedTime="+lastSucUsedTime+"\n";
        temp = temp + "batchNo="+batchNo+"\n";
        temp = temp + "cancelTime="+cancelTime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("type",type);
        json.put("channelId",channelId);
        json.put("channelName",PayCoopBankService.CHANNEL_MAP_ALL.get(channelId)==null?""
        		:PayCoopBankService.CHANNEL_MAP_ALL.get(channelId).bankName);
        json.put("merchantId",merchantId);
        json.put("md5Key",md5Key);
        json.put("status",status);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        try{
            json.put("lastSucUsedTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastSucUsedTime));
        } catch (Exception e) {}
        json.put("batchNo",batchNo); 
        try{
            json.put("cancelTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cancelTime));
        } catch (Exception e) {}
        json.put("lastSucTranAmt",String.format("%.2f", ((double)lastSucTranAmt)/100d));
        json.put("sumTranAmt",String.format("%.2f", ((double)sumTranAmt)/100d));
        json.put("sumSuccessTranAmt",String.format("%.2f", ((double)sumSuccessTranAmt)/100d));
        json.put("sumTranAccount",String.valueOf(sumTranAccount));
        json.put("sumSuccessTranAccount",String.valueOf(sumSuccessTranAccount));
        json.put("sumWithDrawTranAmt",String.format("%.2f", ((double)sumWithDrawTranAmt)/100d));
        json.put("maxSumAmt",String.format("%.2f", ((double)maxSumAmt)/100d));
        json.put("sumDayCountZfbScan",String.valueOf(sumDayCountZfbScan));
        json.put("sumDayCountWeixinScan",String.valueOf(sumDayCountWeixinScan));
        json.put("sumDayCountQqScan",String.valueOf(sumDayCountQqScan));
        json.put("sumDaySucCountZfbScan",String.valueOf(sumDaySucCountZfbScan));
        json.put("sumDaySucCountWeixinScan",String.valueOf(sumDaySucCountWeixinScan));
        json.put("sumDaySucCountQqScan",String.valueOf(sumDaySucCountQqScan));
        json.put("sumDayAmtZfbScan",String.format("%.2f",((double)sumDayAmtZfbScan)/100d));
        json.put("sumDayAmtWeixinScan",String.format("%.2f",((double)sumDayAmtWeixinScan)/100d));
        json.put("sumDayAmtQqScan",String.format("%.2f",((double)sumDayAmtQqScan)/100d));
        json.put("sumDaySucAmtZfbScan",String.format("%.2f",((double)sumDaySucAmtZfbScan)/100d));
        json.put("sumDaySucAmtWeixinScan",String.format("%.2f",((double)sumDaySucAmtWeixinScan)/100d));
        json.put("sumDaySucAmtQqScan",String.format("%.2f",((double)sumDaySucAmtQqScan)/100d));
        return json;
    }
}