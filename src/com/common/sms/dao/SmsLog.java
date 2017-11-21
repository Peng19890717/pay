package com.common.sms.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table SMS_LOG entity.
 * 
 * @author administrator
 */
public class SmsLog {
    public String id;
    public String tel;
    public String content;
    public String userId = "";
    public String userName = "";
    public String createId;
    public Date createTime;
    public String createTimeStart;
    public String createTimeEnd;
    public String status = "";
    public String remark = "";
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getTel(){
         return tel;
    }
    public void setTel(String tel){
         this.tel=tel;
    }
    public String getContent(){
         return content;
    }
    public void setContent(String content){
         this.content=content;
    }
    public String getUserId(){
         return userId;
    }
    public void setUserId(String userId){
         this.userId=userId;
    }
    public String getCreateId(){
         return createId;
    }
    public void setCreateId(String createId){
         this.createId=createId;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "tel="+tel+"\n";
        temp = temp + "content="+content+"\n";
        temp = temp + "userId="+userId+"\n";
        temp = temp + "userName="+userName+"\n";
        temp = temp + "createId="+createId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "status="+status+"\n";
        temp = temp + "remark="+remark+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("tel",tel);
        json.put("content",content);
        json.put("userId",userId);
        json.put("userName",userName);
        json.put("createId",createId);
        json.put("status","0".equals(status)?"成功":"失败");
        json.put("remark",remark);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        return json;
    }
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}