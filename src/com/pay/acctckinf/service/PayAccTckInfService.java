package com.pay.acctckinf.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.acctckinf.dao.PayAccTckInfDAO;
import com.pay.acctckinf.dao.PayAccTckInf;

/**
 * Object PAY_ACC_TCK_INF service. 
 * @author Administrator
 *
 */
public class PayAccTckInfService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayAccTckInfList(PayAccTckInf payAccTckInf,int page,int rows,String sort,String order){
        try {
            PayAccTckInfDAO payAccTckInfDAO = new PayAccTckInfDAO();
            List list = payAccTckInfDAO.getPayAccTckInfList(payAccTckInf, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payAccTckInfDAO.getPayAccTckInfCount(payAccTckInf)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayAccTckInf)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}