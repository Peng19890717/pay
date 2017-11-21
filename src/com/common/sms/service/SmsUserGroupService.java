package com.common.sms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.common.sms.dao.SmsUserGroup;
import com.common.sms.dao.SmsUserGroupDAO;

/**
 * Object SMS_USER_GROUP service. 
 * @author Administrator
 *
 */
public class SmsUserGroupService {
    /**
     * Get records list(json).
     * @return
     */
    public String getSmsUserGroupList(SmsUserGroup smsUserGroup,int page,int rows,String sort,String order){
        try {
            SmsUserGroupDAO smsUserGroupDAO = new SmsUserGroupDAO();
            List list = smsUserGroupDAO.getSmsUserGroupList(smsUserGroup, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(smsUserGroupDAO.getSmsUserGroupCount(smsUserGroup)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((SmsUserGroup)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add SmsUserGroup
     * @param smsUserGroup
     * @throws Exception
     */
    public void addSmsUserGroup(SmsUserGroup smsUserGroup) throws Exception {
        new SmsUserGroupDAO().addSmsUserGroup(smsUserGroup);
    }
    /**
     * remove SmsUserGroup
     * @param id
     * @throws Exception
     */
    public void removeSmsUserGroup(String id) throws Exception {
        new SmsUserGroupDAO().removeSmsUserGroup(id);
    }
    /**
     * update SmsUserGroup
     * @param smsUserGroup
     * @throws Exception
     */
    public void updateSmsUserGroup(SmsUserGroup smsUserGroup) throws Exception {
        new SmsUserGroupDAO().updateSmsUserGroup(smsUserGroup);
    }
	public String getAllSmsUserGroup() throws Exception {
		List list = new SmsUserGroupDAO().getAllSmsUserGroup();
        JSONArray row = new JSONArray();
        for(int i = 0; i<list.size(); i++){
        	row.put(i, ((SmsUserGroup)list.get(i)).toJson());
        }
		return row.toString();
	}
}