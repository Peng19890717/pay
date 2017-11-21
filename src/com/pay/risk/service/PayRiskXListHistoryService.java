package com.pay.risk.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.risk.dao.PayRiskXListHistoryDAO;
import com.pay.risk.dao.PayRiskXListHistory;

/**
 * Object PAY_RISK_X_LIST_HISTORY service. 
 * @author Administrator
 *
 */
public class PayRiskXListHistoryService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayRiskXListHistoryList(PayRiskXListHistory payRiskXListHistory,int page,int rows,String sort,String order){
        try {
            PayRiskXListHistoryDAO payRiskXListHistoryDAO = new PayRiskXListHistoryDAO();
            List list = payRiskXListHistoryDAO.getPayRiskXListHistoryList(payRiskXListHistory, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payRiskXListHistoryDAO.getPayRiskXListHistoryCount(payRiskXListHistory)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayRiskXListHistory)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}