package com.pay.merchant.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pay.merchant.dao.PayYakuStlAcc;
import com.pay.merchant.dao.PayYakuStlAccDAO;

/**
 * Object PAY_YAKU_STL_ACC service. 
 * @author Administrator
 *
 */
public class PayYakuStlAccService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayYakuStlAccList(PayYakuStlAcc payYakuStlAcc,int page,int rows,String sort,String order){
        try {
            PayYakuStlAccDAO payYakuStlAccDAO = new PayYakuStlAccDAO();
            List list = payYakuStlAccDAO.getPayYakuStlAccList(payYakuStlAcc, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payYakuStlAccDAO.getPayYakuStlAccCount(payYakuStlAcc)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayYakuStlAcc)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayYakuStlAcc
     * @param payYakuStlAcc
     * @throws Exception
     */
    public void addPayYakuStlAcc(PayYakuStlAcc payYakuStlAcc) throws Exception {
        new PayYakuStlAccDAO().addPayYakuStlAcc(payYakuStlAcc);
    }
    /**
     * remove PayYakuStlAcc
     * @param merno
     * @throws Exception
     */
    public void removePayYakuStlAcc(String accNo) throws Exception {
        new PayYakuStlAccDAO().removePayYakuStlAcc(accNo);
    }
    /**
     * update PayYakuStlAcc
     * @param payYakuStlAcc
     * @throws Exception
     */
    public void updatePayYakuStlAcc(PayYakuStlAcc payYakuStlAcc) throws Exception {
        new PayYakuStlAccDAO().updatePayYakuStlAcc(payYakuStlAcc);
    }
    /**
     * detail PayYakuStlAcc
     * @param merno
     * @throws Exception
     */
    public PayYakuStlAcc detailPayYakuStlAcc(String accNo) throws Exception {
        return new PayYakuStlAccDAO().detailPayYakuStlAcc(accNo);
    }

}