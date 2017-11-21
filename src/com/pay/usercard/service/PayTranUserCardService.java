package com.pay.usercard.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.usercard.dao.PayTranUserCardDAO;
import com.pay.usercard.dao.PayTranUserCard;

/**
 * Object PAY_TRAN_USER_CARD service. 
 * @author Administrator
 *
 */
public class PayTranUserCardService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayTranUserCardList(PayTranUserCard payTranUserCard,int page,int rows,String sort,String order){
        try {
            PayTranUserCardDAO payTranUserCardDAO = new PayTranUserCardDAO();
            List list = payTranUserCardDAO.getPayTranUserCardList(payTranUserCard, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payTranUserCardDAO.getPayTranUserCardCount(payTranUserCard)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayTranUserCard)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}