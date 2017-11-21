package com.pay.account.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.account.dao.PayGeneralLedgerHisDAO;
import com.pay.account.dao.PayGeneralLedgerHis;

/**
 * Object PAY_GENERAL_LEDGER_HIS service. 
 * @author Administrator
 *
 */
public class PayGeneralLedgerHisService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayGeneralLedgerHisList(PayGeneralLedgerHis payGeneralLedgerHis,int page,int rows,String sort,String order){
        try {
            PayGeneralLedgerHisDAO payGeneralLedgerHisDAO = new PayGeneralLedgerHisDAO();
            List list = payGeneralLedgerHisDAO.getPayGeneralLedgerHisList(payGeneralLedgerHis, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payGeneralLedgerHisDAO.getPayGeneralLedgerHisCount(payGeneralLedgerHis)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayGeneralLedgerHis)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}