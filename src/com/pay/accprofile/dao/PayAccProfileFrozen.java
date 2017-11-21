package com.pay.accprofile.dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
/**
 * Table PAY_ACC_PROFILE_FROZEN entity.
 * 
 * @author administrator
 */
public class PayAccProfileFrozen {
    public String id;
    public String accType;
    public String accNo;
    public Long amt;
    public Long curAmt;
    public Date beginTime;
    public Date endTime;
    public String status;
    public String optUser;
    public Date createTime;
    public String createTimeStart;
    public String createTimeEnd;
    public Date updateTime;
    public String remark;
    public String storeName;
    
    public String getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getAccType(){
         return accType;
    }
    public void setAccType(String accType){
         this.accType=accType;
    }
    public String getAccNo(){
         return accNo;
    }
    public void setAccNo(String accNo){
         this.accNo=accNo;
    }
    public Long getAmt(){
         return amt;
    }
    public void setAmt(Long amt){
         this.amt=amt;
    }
    public Long getCurAmt(){
         return curAmt;
    }
    public void setCurAmt(Long curAmt){
         this.curAmt=curAmt;
    }
    public Date getBeginTime(){
         return beginTime;
    }
    public void setBeginTime(Date beginTime){
         this.beginTime=beginTime;
    }
    public Date getEndTime(){
         return endTime;
    }
    public void setEndTime(Date endTime){
         this.endTime=endTime;
    }
    public String getStatus(){
         return status;
    }
    public void setStatus(String status){
         this.status=status;
    }
    public String getOptUser(){
         return optUser;
    }
    public void setOptUser(String optUser){
         this.optUser=optUser;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public Date getUpdateTime(){
         return updateTime;
    }
    public void setUpdateTime(Date updateTime){
         this.updateTime=updateTime;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    
    public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "accType="+accType+"\n";
        temp = temp + "accNo="+accNo+"\n";
        temp = temp + "amt="+amt+"\n";
        temp = temp + "curAmt="+curAmt+"\n";
        temp = temp + "beginTime="+beginTime+"\n";
        temp = temp + "endTime="+endTime+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "optUser="+optUser+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "updateTime="+updateTime+"\n";
        temp = temp + "remark="+remark+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("accType",accType);
        json.put("accNo",accNo);
        json.put("amt",new BigDecimal(amt).divide(new BigDecimal(100)).toPlainString());
        json.put("curAmt",new BigDecimal(curAmt).divide(new BigDecimal(100)).toPlainString());
        try{
            json.put("beginTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beginTime));
        } catch (Exception e) {}
        try{
            json.put("endTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime));
        } catch (Exception e) {}
        json.put("status",status);
        json.put("optUser",optUser);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        try{
            json.put("updateTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updateTime));
        } catch (Exception e) {}
        json.put("remark",remark);
        json.put("storeName",storeName);
        return json;
    }
}