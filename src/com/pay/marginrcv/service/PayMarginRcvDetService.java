package com.pay.marginrcv.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.marginrcv.dao.PayMarginRcvDetDAO;
import com.pay.marginrcv.dao.PayMarginRcvDet;

/**
 * Object PAY_MARGIN_RCV_DET service. 
 * @author Administrator
 *
 */
public class PayMarginRcvDetService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayMarginRcvDetList(PayMarginRcvDet payMarginRcvDet,int page,int rows,String sort,String order){
        try {
            PayMarginRcvDetDAO payMarginRcvDetDAO = new PayMarginRcvDetDAO();
            List list = payMarginRcvDetDAO.getPayMarginRcvDetList(payMarginRcvDet, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payMarginRcvDetDAO.getPayMarginRcvDetCount(payMarginRcvDet)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayMarginRcvDet)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}