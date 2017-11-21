package com.common.sms.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.common.sms.dao.SmsExamplesDAO;
import com.common.sms.dao.SmsExamples;

/**
 * Object SMS_EXAMPLES service. 
 * @author Administrator
 *
 */
public class SmsExamplesService {
    /**
     * Get records list(json).
     * @return
     */
    public String getSmsExamplesList(SmsExamples smsExamples,int page,int rows,String sort,String order){
        try {
            SmsExamplesDAO smsExamplesDAO = new SmsExamplesDAO();
            List list = smsExamplesDAO.getSmsExamplesList(smsExamples, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(smsExamplesDAO.getSmsExamplesCount(smsExamples)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((SmsExamples)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add SmsExamples
     * @param smsExamples
     * @throws Exception
     */
    public void addSmsExamples(SmsExamples smsExamples) throws Exception {
        new SmsExamplesDAO().addSmsExamples(smsExamples);
    }
    /**
     * remove SmsExamples
     * @param id
     * @throws Exception
     */
    public void removeSmsExamples(String id) throws Exception {
        new SmsExamplesDAO().removeSmsExamples(id);
    }
    /**
     * update SmsExamples
     * @param smsExamples
     * @throws Exception
     */
    public void updateSmsExamples(SmsExamples smsExamples) throws Exception {
        new SmsExamplesDAO().updateSmsExamples(smsExamples);
    }

}