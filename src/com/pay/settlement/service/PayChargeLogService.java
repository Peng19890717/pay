package com.pay.settlement.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pay.settlement.dao.PayChargeLogDAO;
import com.pay.settlement.dao.PayChargeLog;

/**
 * Object PAY_CHARGE_LOG service. 
 * @author Administrator
 *
 */
public class PayChargeLogService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayChargeLogList(PayChargeLog payChargeLog,int page,int rows,String sort,String order){
        try {
            PayChargeLogDAO payChargeLogDAO = new PayChargeLogDAO();
            List list = payChargeLogDAO.getPayChargeLogList(payChargeLog, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payChargeLogDAO.getPayChargeLogCount(payChargeLog)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayChargeLog)list.get(i)).toJson());
            }
            //查询总金额
            Long money = payChargeLogDAO.getTotalChargeLogMoney(payChargeLog);
            json.put("rows", row);
            json.put("totalChargeLogMoney", String.valueOf(money));
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayChargeLog
     * @param payChargeLog
     * @throws Exception
     */
    public void addPayChargeLog(PayChargeLog payChargeLog) throws Exception {
        new PayChargeLogDAO().addPayChargeLog(payChargeLog);
    }
    /**
     * remove PayChargeLog
     * @param id
     * @throws Exception
     */
    public void removePayChargeLog(String id) throws Exception {
        new PayChargeLogDAO().removePayChargeLog(id);
    }
    /**
     * update PayChargeLog
     * @param payChargeLog
     * @throws Exception
     */
    public void updatePayChargeLog(PayChargeLog payChargeLog) throws Exception {
        new PayChargeLogDAO().updatePayChargeLog(payChargeLog);
    }
    /**
     * detail PayChargeLog
     * @param id
     * @throws Exception
     */
    public PayChargeLog detailPayChargeLog(String id) throws Exception {
        return new PayChargeLogDAO().detailPayChargeLog(id);
    }

}