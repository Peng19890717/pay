package com.pay.merchant.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.merchant.dao.PayNotifyFailUrlDAO;
import com.pay.merchant.dao.PayNotifyFailUrl;

/**
 * Object PAY_NOTIFY_FAIL_URL service. 
 * @author Administrator
 *
 */
public class PayNotifyFailUrlService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayNotifyFailUrlList(PayNotifyFailUrl payNotifyFailUrl,int page,int rows,String sort,String order){
        try {
            PayNotifyFailUrlDAO payNotifyFailUrlDAO = new PayNotifyFailUrlDAO();
            List list = payNotifyFailUrlDAO.getPayNotifyFailUrlList(payNotifyFailUrl, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payNotifyFailUrlDAO.getPayNotifyFailUrlCount(payNotifyFailUrl)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayNotifyFailUrl)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayNotifyFailUrl
     * @param payNotifyFailUrl
     * @throws Exception
     */
    public void addPayNotifyFailUrl(PayNotifyFailUrl payNotifyFailUrl) throws Exception {
        new PayNotifyFailUrlDAO().addPayNotifyFailUrl(payNotifyFailUrl);
    }
}