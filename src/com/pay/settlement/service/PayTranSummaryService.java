package com.pay.settlement.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pay.settlement.dao.PayChargeLogDAO;
import com.pay.settlement.dao.PayChargeLog;
import com.pay.settlement.dao.PayTranSummary;
import com.pay.settlement.dao.PayTranSummaryDAO;

/**
 * Object PAY_CHARGE_LOG service. 
 * @author Administrator
 *
 */
public class PayTranSummaryService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayTranSummaryList(PayTranSummary payTranSummary){
        try {
            PayTranSummaryDAO payTranSummaryDAO = new PayTranSummaryDAO();
            List list = payTranSummaryDAO.getList(payTranSummary);
            JSONObject json = new JSONObject();
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayTranSummary)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}