package com.pay.settlement.dao;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.pay.coopbank.service.PayCoopBankService;

public class PayTranSummary {

	public String channelNo;//渠道编号
	public long tranTotalFee;//交易总额
	public long channelFee;//渠道手续费
	public Date tranTime;
	public Date tranTimeEnd;
	public Date getTranTimeEnd() {
		return tranTimeEnd;
	}
	public void setTranTimeEnd(Date tranTimeEnd) {
		this.tranTimeEnd = tranTimeEnd;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	public Date getTranTime() {
		return tranTime;
	}
	public void setTranTime(Date tranTime) {
		this.tranTime = tranTime;
	}
	
    public long getTranTotalFee() {
		return tranTotalFee;
	}
	public void setTranTotalFee(long tranTotalFee) {
		this.tranTotalFee = tranTotalFee;
	}
	public long getChannelFee() {
		return channelFee;
	}
	public void setChannelFee(long channelFee) {
		this.channelFee = channelFee;
	}
	public JSONObject toJson() throws JSONException{	
    JSONObject json = new JSONObject();
    json.put("channelNo",channelNo);
    json.put("channelName",PayCoopBankService.CHANNEL_MAP_ALL.get(channelNo)==null?"":PayCoopBankService.CHANNEL_MAP_ALL.get(channelNo).bankName);
    json.put("tranTotalFee",String.format("%.2f", ((double)tranTotalFee)/100d));
    json.put("channelFee",String.format("%.2f", ((double)channelFee)/100d));
    json.put("settlementAmt",String.format("%.2f", ((double)(tranTotalFee-channelFee))/100d));
    return json;
}
	
}
