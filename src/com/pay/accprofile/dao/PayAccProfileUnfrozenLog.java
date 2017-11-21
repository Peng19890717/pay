package com.pay.accprofile.dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
/**
 * Table PAY_ACC_PROFILE_UNFROZEN_LOG entity.
 * 
 * @author administrator
 */
public class PayAccProfileUnfrozenLog {
    public String id;
    public Long unfrozenAmt;
    public String flag;
    public Date createTime;
    public Date createTimeStart;
	public Date createTimeEnd;
    public Date optTime;
    public Date optTimeStart;
    public Date optTimeEnd;
    public String userId;
    public String accFrozenId;
    public String remark;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    
    public Long getUnfrozenAmt() {
		return unfrozenAmt;
	}
	public void setUnfrozenAmt(Long unfrozenAmt) {
		this.unfrozenAmt = unfrozenAmt;
	}
	public String getFlag(){
         return flag;
    }
    public void setFlag(String flag){
         this.flag=flag;
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
    public String getUserId(){
         return userId;
    }
    public void setUserId(String userId){
         this.userId=userId;
    }
    public String getAccFrozenId(){
         return accFrozenId;
    }
    public void setAccFrozenId(String accFrozenId){
         this.accFrozenId=accFrozenId;
    }
    public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Date getOptTimeStart() {
		return optTimeStart;
	}
	public void setOptTimeStart(Date optTimeStart) {
		this.optTimeStart = optTimeStart;
	}
	public Date getOptTimeEnd() {
		return optTimeEnd;
	}
	public void setOptTimeEnd(Date optTimeEnd) {
		this.optTimeEnd = optTimeEnd;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "unfrozenAmt="+unfrozenAmt+"\n";
        temp = temp + "flag="+flag+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "optTime="+optTime+"\n";
        temp = temp + "userId="+userId+"\n";
        temp = temp + "accFrozenId="+accFrozenId+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("unfrozenAmt",new BigDecimal(unfrozenAmt).divide(new BigDecimal(100)).toPlainString());
        json.put("flag",flag);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        try{
            json.put("optTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(optTime));
        } catch (Exception e) {}
        json.put("userId",userId);
        json.put("accFrozenId",accFrozenId);
        json.put("remark",remark);
        return json;
    }
}