package com.pay.merchant.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.merchant.dao.PayOpenedMerPayDAO;
import com.pay.merchant.dao.PayOpenedMerPay;

/**
 * Object PAY_OPENED_MER_PAY service. 
 * @author Administrator
 *
 */
public class PayOpenedMerPayService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayOpenedMerPayList(PayOpenedMerPay payOpenedMerPay,int page,int rows,String sort,String order){
        try {
            PayOpenedMerPayDAO payOpenedMerPayDAO = new PayOpenedMerPayDAO();
            List list = payOpenedMerPayDAO.getPayOpenedMerPayList(payOpenedMerPay, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payOpenedMerPayDAO.getPayOpenedMerPayCount(payOpenedMerPay)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayOpenedMerPay)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayOpenedMerPay
     * @param payOpenedMerPay
     * @throws Exception
     */
    public void addPayOpenedMerPay(PayOpenedMerPay payOpenedMerPay) throws Exception {
        new PayOpenedMerPayDAO().addPayOpenedMerPay(payOpenedMerPay);
    }
    /**
     * remove PayOpenedMerPay
     * @param merno
     * @throws Exception
     */
    public void removePayOpenedMerPay(String merno) throws Exception {
        new PayOpenedMerPayDAO().removePayOpenedMerPay(merno);
    }
    /**
     * update PayOpenedMerPay
     * @param payOpenedMerPay
     * @throws Exception
     */
//    public void updatePayOpenedMerPay(PayOpenedMerPay payOpenedMerPay) throws Exception {
//        new PayOpenedMerPayDAO().updatePayOpenedMerPay(payOpenedMerPay);
//    }
    /**
     * detail PayOpenedMerPay
     * @param merno
     * @throws Exception
     */
    public PayOpenedMerPay detailPayOpenedMerPay(String merno) throws Exception {
        return new PayOpenedMerPayDAO().detailPayOpenedMerPay(merno);
    }

}