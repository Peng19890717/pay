package com.pay.merchantinterface.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.merchantinterface.dao.PayRealNameAuthDAO;
import com.pay.merchantinterface.dao.PayRealNameAuth;

/**
 * Object PAY_REAL_NAME_AUTH service. 
 * @author Administrator
 *
 */
public class PayRealNameAuthService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayRealNameAuthList(PayRealNameAuth payRealNameAuth,int page,int rows,String sort,String order){
        try {
            PayRealNameAuthDAO payRealNameAuthDAO = new PayRealNameAuthDAO();
            List list = payRealNameAuthDAO.getPayRealNameAuthList(payRealNameAuth, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payRealNameAuthDAO.getPayRealNameAuthCount(payRealNameAuth)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayRealNameAuth)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayRealNameAuth
     * @param payRealNameAuth
     * @throws Exception
     */
    public void addPayRealNameAuth(PayRealNameAuth payRealNameAuth) throws Exception {
        new PayRealNameAuthDAO().addPayRealNameAuth(payRealNameAuth);
    }
    /**
     * update PayRealNameAuth
     * @param payRealNameAuth
     * @throws Exception
     */
    public void updatePayRealNameAuth(PayRealNameAuth payRealNameAuth) throws Exception {
        new PayRealNameAuthDAO().updatePayRealNameAuth(payRealNameAuth);
    }

}