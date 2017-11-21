package com.pay.qtbao.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.qtbao.dao.PayFundinFundoutDAO;
import com.pay.qtbao.dao.PayFundinFundout;

/**
 * Object PAY_FUNDIN_FUNDOUT service. 
 * @author Administrator
 *
 */
public class PayFundinFundoutService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayFundinFundoutList(PayFundinFundout payFundinFundout,int page,int rows,String sort,String order){
        try {
            PayFundinFundoutDAO payFundinFundoutDAO = new PayFundinFundoutDAO();
            List list = payFundinFundoutDAO.getPayFundinFundoutList(payFundinFundout, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payFundinFundoutDAO.getPayFundinFundoutCount(payFundinFundout)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayFundinFundout)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayFundinFundout
     * @param payFundinFundout
     * @throws Exception
     */
    public void addPayFundinFundout(PayFundinFundout payFundinFundout) throws Exception {
        new PayFundinFundoutDAO().addPayFundinFundout(payFundinFundout);
    }
    /**
     * remove PayFundinFundout
     * @param merchantid
     * @throws Exception
     */
    public void removePayFundinFundout(String merchantid) throws Exception {
        new PayFundinFundoutDAO().removePayFundinFundout(merchantid);
    }
    /**
     * update PayFundinFundout
     * @param payFundinFundout
     * @throws Exception
     */
    public void updatePayFundinFundout(PayFundinFundout payFundinFundout) throws Exception {
        new PayFundinFundoutDAO().updatePayFundinFundout(payFundinFundout);
    }
    /**
     * detail PayFundinFundout
     * @param merchantid
     * @throws Exception
     */
    public PayFundinFundout detailPayFundinFundout(String merchantid) throws Exception {
        return new PayFundinFundoutDAO().detailPayFundinFundout(merchantid);
    }

}