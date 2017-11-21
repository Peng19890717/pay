package com.common.sms.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.common.sms.dao.SmsUserDAO;
import com.common.sms.dao.SmsUser;

/**
 * Object SMS_USER service. 
 * @author Administrator
 *
 */
public class SmsUserService {
    /**
     * Get records list(json).
     * @return
     */
    public String getSmsUserList(SmsUser smsUser,int page,int rows,String sort,String order){
        try {
            SmsUserDAO smsUserDAO = new SmsUserDAO();
            List list = smsUserDAO.getSmsUserList(smsUser, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(smsUserDAO.getSmsUserCount(smsUser)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((SmsUser)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add SmsUser
     * @param smsUser
     * @throws Exception
     */
    public void addSmsUser(SmsUser smsUser) throws Exception {
        new SmsUserDAO().addSmsUser(smsUser);
    }
    /**
     * remove SmsUser
     * @param id
     * @throws Exception
     */
    public void removeSmsUser(String id) throws Exception {
        new SmsUserDAO().removeSmsUser(id);
    }
    /**
     * update SmsUser
     * @param smsUser
     * @throws Exception
     */
    public void updateSmsUser(SmsUser smsUser) throws Exception {
        new SmsUserDAO().updateSmsUser(smsUser);
    }

}