package com.pay.complain.dao;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.service.PayCoopBankService;
/**
 * Table PAY_CUST_COMPLAIN entity.
 * 
 * @author administrator
 */
public class PayCustComplain {
    public String id;
    public String type;
    public String custId;
    public String storeName;
    public Long transAmt;
    public Long frozenAmt;
    public String optStatus;
    public String isFrozenFlag;
    public String unfrozenFlag;
    public String deductFlag;
    public String name;
    public String tel;
    public String channel;
    public String startFile;
    public String endFile;
    public String orderIds;
    public String cContent;
    public Date cTime;
    public Date createTime;
    public Date optTime;
    public String remark;
    public Long deductAmt=0l;
    public String cTimeStart;
    public String cTimeEnd;
    public String accFrozenId="";
    public String accAdjustId="";
    public String getcTimeStart() {
		return cTimeStart;
	}
	public void setcTimeStart(String cTimeStart) {
		this.cTimeStart = cTimeStart;
	}
	public String getcTimeEnd() {
		return cTimeEnd;
	}
	public void setcTimeEnd(String cTimeEnd) {
		this.cTimeEnd = cTimeEnd;
	}
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
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public Long getTransAmt(){
         return transAmt;
    }
    public void setTransAmt(Long transAmt){
         this.transAmt=transAmt;
    }
    public Long getFrozenAmt(){
         return frozenAmt;
    }
    public void setFrozenAmt(Long frozenAmt){
         this.frozenAmt=frozenAmt;
    }
    public String getOptStatus(){
         return optStatus;
    }
    public void setOptStatus(String optStatus){
         this.optStatus=optStatus;
    }
    public String getName(){
         return name;
    }
    public void setName(String name){
         this.name=name;
    }
    public String getTel(){
         return tel;
    }
    public void setTel(String tel){
         this.tel=tel;
    }
    public String getChannel(){
         return channel;
    }
    public void setChannel(String channel){
         this.channel=channel;
    }
    public String getStartFile(){
         return startFile;
    }
    public void setStartFile(String startFile){
         this.startFile=startFile;
    }
    public String getEndFile(){
         return endFile;
    }
    public void setEndFile(String endFile){
         this.endFile=endFile;
    }
    public String getOrderIds(){
         return orderIds;
    }
    public void setOrderIds(String orderIds){
         this.orderIds=orderIds;
    }
    public String getCContent(){
         return cContent;
    }
    public void setCContent(String cContent){
         this.cContent=cContent;
    }
    public Date getCTime(){
         return cTime;
    }
    public void setCTime(Date cTime){
         this.cTime=cTime;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public Date getOptTime(){
         return optTime;
    }
    public void setOptTime(Date optTime){
         this.optTime=optTime;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public Long getDeductAmt(){
         return deductAmt;
    }
    public void setDeductAmt(Long deductAmt){
         this.deductAmt=deductAmt;
    }
    
    public String getIsFrozenFlag() {
		return isFrozenFlag;
	}
	public void setIsFrozenFlag(String isFrozenFlag) {
		this.isFrozenFlag = isFrozenFlag;
	}
	
	public String getAccFrozenId() {
		return accFrozenId;
	}
	public void setAccFrozenId(String accFrozenId) {
		this.accFrozenId = accFrozenId;
	}
	
	public String getAccAdjustId() {
		return accAdjustId;
	}
	public void setAccAdjustId(String accAdjustId) {
		this.accAdjustId = accAdjustId;
	}
	
	public String getUnfrozenFlag() {
		return unfrozenFlag;
	}
	public void setUnfrozenFlag(String unfrozenFlag) {
		this.unfrozenFlag = unfrozenFlag;
	}
	public String getDeductFlag() {
		return deductFlag;
	}
	public void setDeductFlag(String deductFlag) {
		this.deductFlag = deductFlag;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "type="+type+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "transAmt="+transAmt+"\n";
        temp = temp + "frozenAmt="+frozenAmt+"\n";
        temp = temp + "optStatus="+optStatus+"\n";
        temp = temp + "name="+name+"\n";
        temp = temp + "tel="+tel+"\n";
        temp = temp + "channel="+channel+"\n";
        temp = temp + "startFile="+startFile+"\n";
        temp = temp + "endFile="+endFile+"\n";
        temp = temp + "orderIds="+orderIds+"\n";
        temp = temp + "cContent="+cContent+"\n";
        temp = temp + "cTime="+cTime+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "optTime="+optTime+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "deductAmt="+deductAmt+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("type",type);
        json.put("custId",custId);
        json.put("storeName",storeName);
        json.put("transAmt",String.format("%.2f",(float)transAmt*0.01));
        json.put("frozenAmt",String.format("%.2f",(float)frozenAmt*0.01));
        json.put("optStatus",optStatus);
        json.put("name",name);
        json.put("tel",tel);
        PayCoopBank c = PayCoopBankService.CHANNEL_MAP_ALL.get(channel);
        json.put("channel",c==null?channel:c.bankName);
        json.put("startFile",startFile);
        json.put("endFile",endFile);
        json.put("orderIds",orderIds);
        json.put("cContent",cContent);
        try{
            json.put("cTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cTime));
        } catch (Exception e) {}
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        try{
            json.put("optTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(optTime));
        } catch (Exception e) {}
        json.put("remark",remark);
        json.put("deductAmt",String.format("%.2f",(float)deductAmt*0.01));
        json.put("accFrozenId",accFrozenId);
        json.put("accAdjustId",accAdjustId);
        return json;
    }
}