package com.pay.user.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_BUSINESS_MEMBER entity.
 * 
 * @author administrator
 */
public class PayBusinessMember {
    public String userId;
    public String parentId;
    public String realName;
    public List<PayBusinessMember> subList = new ArrayList<PayBusinessMember>();
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public JSONObject toJson(String parentId) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",userId);
        if(!"00".equals(parentId))json.put("_parentId",parentId);
		json.put("name",realName);
		json.put("iconCls","icon-man");
		json.put("text",realName);
        return json;
    }
    
}