package com.pay.usercard.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.usercard.dao.PaySameCardInOutDAO;
import com.pay.usercard.dao.PaySameCardInOut;

/**
 * Object PAY_SAME_CARD_IN_OUT service. 
 * @author Administrator
 *
 */
public class PaySameCardInOutService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPaySameCardInOutList(PaySameCardInOut paySameCardInOut,int page,int rows,String sort,String order){
        try {
            PaySameCardInOutDAO paySameCardInOutDAO = new PaySameCardInOutDAO();
            List list = paySameCardInOutDAO.getPaySameCardInOutList(paySameCardInOut, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(paySameCardInOutDAO.getPaySameCardInOutCount(paySameCardInOut)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PaySameCardInOut)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PaySameCardInOut
     * @param paySameCardInOut
     * @throws Exception
     */
    public void addPaySameCardInOut(PaySameCardInOut paySameCardInOut) throws Exception {
        new PaySameCardInOutDAO().addPaySameCardInOut(paySameCardInOut);
    }
    /**
     * remove PaySameCardInOut
     * @param id
     * @throws Exception
     */
    public void removePaySameCardInOut(String id) throws Exception {
        new PaySameCardInOutDAO().removePaySameCardInOut(id);
    }
    /**
     * update PaySameCardInOut
     * @param paySameCardInOut
     * @throws Exception
     */
    public void updatePaySameCardInOut(PaySameCardInOut paySameCardInOut) throws Exception {
        new PaySameCardInOutDAO().updatePaySameCardInOut(paySameCardInOut);
    }
    /**
     * detail PaySameCardInOut
     * @param id
     * @throws Exception
     */
    public PaySameCardInOut detailPaySameCardInOut(String id) throws Exception {
        return new PaySameCardInOutDAO().detailPaySameCardInOut(id);
    }

}