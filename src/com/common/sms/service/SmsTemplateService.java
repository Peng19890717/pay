package com.common.sms.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.common.sms.dao.SmsTemplateDAO;
import com.common.sms.dao.SmsTemplate;

/**
 * Object SMS_TEMPLATE service. 
 * @author Administrator
 *
 */
public class SmsTemplateService {
    /**
     * Get records list(json).
     * @return
     */
    public String getSmsTemplateList(SmsTemplate smsTemplate,int page,int rows,String sort,String order){
        try {
            SmsTemplateDAO smsTemplateDAO = new SmsTemplateDAO();
            List list = smsTemplateDAO.getSmsTemplateList(smsTemplate, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(smsTemplateDAO.getSmsTemplateCount(smsTemplate)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((SmsTemplate)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add SmsTemplate
     * @param smsTemplate
     * @throws Exception
     */
    public void addSmsTemplate(SmsTemplate smsTemplate) throws Exception {
        new SmsTemplateDAO().addSmsTemplate(smsTemplate);
    }
    /**
     * remove SmsTemplate
     * @param id
     * @throws Exception
     */
    public void removeSmsTemplate(String id) throws Exception {
        new SmsTemplateDAO().removeSmsTemplate(id);
    }
    /**
     * update SmsTemplate
     * @param smsTemplate
     * @throws Exception
     */
    public void updateSmsTemplate(SmsTemplate smsTemplate) throws Exception {
        new SmsTemplateDAO().updateSmsTemplate(smsTemplate);
    }

}