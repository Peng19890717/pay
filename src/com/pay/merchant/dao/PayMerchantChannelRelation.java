package com.pay.merchant.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_MERCHANT_CHANNEL_RELATION entity.
 * 
 * @author administrator
 */
public class PayMerchantChannelRelation {
    public String merno;
    public String channelId;
    public String tranType;
    public String storeName;
    public String channelName;
    public String openedMerchantPayFlag;
    
    public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getMerno(){
         return merno;
    }
    public void setMerno(String merno){
         this.merno=merno;
    }
    public String getChannelId(){
         return channelId;
    }
    public void setChannelId(String channelId){
         this.channelId=channelId;
    }
    public String getTranType(){
         return tranType;
    }
    public void setTranType(String tranType){
         this.tranType=tranType;
    }
    public String getOpenedMerchantPayFlag() {
		return openedMerchantPayFlag;
	}
	public void setOpenedMerchantPayFlag(String openedMerchantPayFlag) {
		this.openedMerchantPayFlag = openedMerchantPayFlag;
	}
	public String toString(){
        String temp = "";
        temp = temp + "merno="+merno+"\n";
        temp = temp + "channelId="+channelId+"\n";
        temp = temp + "tranType="+tranType+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("merno",merno);
        json.put("channelId",channelId);
        json.put("tranType",tranType);
        json.put("storeName",storeName);
        json.put("channelName",channelName);
        return json;
    }
}