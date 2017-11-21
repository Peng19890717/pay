package com.pay.coopbank.dao;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.pay.coopbank.service.PayCoopBankService;
/**
 * Table PAY_CHANNEL_FROZEN entity.
 * 
 * @author administrator
 */
public class PayChannelFrozen {
    public String id;
    public String channel;
    public Long srcAmt;
    public String merNos;
    public String storeName;
    public Date frozenTime;
    public Date frozenTimeStart;
    public Date frozenTimeEnd;
	public Long orderTxamt;
    public String orderIds;
    public Long frozenDays;
    public String status;
    public Long curAmt;
    public String salemanId;
    public Date createTime;
    public String remark;
    public String optId;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getChannel(){
         return channel;
    }
    public void setChannel(String channel){
         this.channel=channel;
    }
    public Long getSrcAmt(){
         return srcAmt;
    }
    public void setSrcAmt(Long srcAmt){
         this.srcAmt=srcAmt;
    }
    public String getMerNos(){
         return merNos;
    }
    public void setMerNos(String merNos){
         this.merNos=merNos;
    }
    public Date getFrozenTime(){
         return frozenTime;
    }
    public void setFrozenTime(Date frozenTime){
         this.frozenTime=frozenTime;
    }
    public Long getOrderTxamt(){
         return orderTxamt;
    }
    public void setOrderTxamt(Long orderTxamt){
         this.orderTxamt=orderTxamt;
    }
    public String getOrderIds(){
         return orderIds;
    }
    public void setOrderIds(String orderIds){
         this.orderIds=orderIds;
    }
    public Long getFrozenDays(){
         return frozenDays;
    }
    public void setFrozenDays(Long frozenDays){
         this.frozenDays=frozenDays;
    }
    public String getStatus(){
         return status;
    }
    public void setStatus(String status){
         this.status=status;
    }
    public Long getCurAmt(){
         return curAmt;
    }
    public void setCurAmt(Long curAmt){
         this.curAmt=curAmt;
    }
    public String getSalemanId(){
         return salemanId;
    }
    public void setSalemanId(String salemanId){
         this.salemanId=salemanId;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String getOptId(){
         return optId;
    }
    public void setOptId(String optId){
         this.optId=optId;
    }
    public Date getFrozenTimeStart() {
		return frozenTimeStart;
	}
	public void setFrozenTimeStart(Date frozenTimeStart) {
		this.frozenTimeStart = frozenTimeStart;
	}
	public Date getFrozenTimeEnd() {
		return frozenTimeEnd;
	}
	public void setFrozenTimeEnd(Date frozenTimeEnd) {
		this.frozenTimeEnd = frozenTimeEnd;
	}
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "channel="+channel+"\n";
        temp = temp + "srcAmt="+srcAmt+"\n";
        temp = temp + "merNos="+merNos+"\n";
        temp = temp + "frozenTime="+frozenTime+"\n";
        temp = temp + "orderTxamt="+orderTxamt+"\n";
        temp = temp + "orderIds="+orderIds+"\n";
        temp = temp + "frozenDays="+frozenDays+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "curAmt="+curAmt+"\n";
        temp = temp + "salemanId="+salemanId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "optId="+optId+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        PayCoopBank c = PayCoopBankService.CHANNEL_MAP_ALL.get(channel);
        json.put("channel",c==null?channel:c.bankName);
        json.put("srcAmt",String.valueOf(srcAmt));
        json.put("storeName",storeName);
        json.put("merNos",merNos);
//        json.put("merNos",storeName==null||storeName.length()==0?merNos:storeName);
        try{
            json.put("frozenTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(frozenTime));
        } catch (Exception e) {}
        json.put("orderTxamt",String.valueOf(orderTxamt));
        json.put("orderIds",orderIds);
        json.put("frozenDays",String.valueOf(frozenDays));
        json.put("status",status);
        json.put("curAmt",String.valueOf(curAmt));
        json.put("salemanId",salemanId);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        json.put("remark",remark);
        json.put("optId",optId);
        return json;
    }
}