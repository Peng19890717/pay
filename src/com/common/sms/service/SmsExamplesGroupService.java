package com.common.sms.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.common.sms.dao.SmsExamplesGroup;
import com.common.sms.dao.SmsExamplesGroupDAO;

/**
 * Object SMS_EXAMPLES_GROUP service. 
 * @author Administrator
 *
 */
public class SmsExamplesGroupService {
    /**
     * Get records list(json).
     * @return
     */
    public String getSmsExamplesGroupList(SmsExamplesGroup smsExamplesGroup,int page,int rows,String sort,String order){
        try {
            SmsExamplesGroupDAO smsExamplesGroupDAO = new SmsExamplesGroupDAO();
            List list = smsExamplesGroupDAO.getSmsExamplesGroupList(smsExamplesGroup, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(smsExamplesGroupDAO.getSmsExamplesGroupCount(smsExamplesGroup)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((SmsExamplesGroup)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add SmsExamplesGroup
     * @param smsExamplesGroup
     * @throws Exception
     */
    public void addSmsExamplesGroup(SmsExamplesGroup smsExamplesGroup) throws Exception {
        new SmsExamplesGroupDAO().addSmsExamplesGroup(smsExamplesGroup);
    }
    /**
     * remove SmsExamplesGroup
     * @param id
     * @throws Exception
     */
    public void removeSmsExamplesGroup(String id) throws Exception {
        new SmsExamplesGroupDAO().removeSmsExamplesGroup(id);
    }
    /**
     * update SmsExamplesGroup
     * @param smsExamplesGroup
     * @throws Exception
     */
    public void updateSmsExamplesGroup(SmsExamplesGroup smsExamplesGroup) throws Exception {
        new SmsExamplesGroupDAO().updateSmsExamplesGroup(smsExamplesGroup);
    }
    public String getAllSmsExamplesGroup() throws Exception {
		List list = new SmsExamplesGroupDAO().getAllSmsExamplesGroup();
        JSONArray row = new JSONArray();
        for(int i = 0; i<list.size(); i++){
        	row.put(i, ((SmsExamplesGroup)list.get(i)).toJson());
        }
		return row.toString();
	}
}