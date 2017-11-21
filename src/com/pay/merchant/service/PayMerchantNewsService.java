package com.pay.merchant.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.merchant.dao.PayMerchantNewsDAO;
import com.pay.merchant.dao.PayMerchantNews;

/**
 * Object PAY_MERCHANT_NEWS service. 
 * @author Administrator
 *
 */
public class PayMerchantNewsService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayMerchantNewsList(PayMerchantNews payMerchantNews,int page,int rows,String sort,String order){
        try {
            PayMerchantNewsDAO payMerchantNewsDAO = new PayMerchantNewsDAO();
            List list = payMerchantNewsDAO.getPayMerchantNewsList(payMerchantNews, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payMerchantNewsDAO.getPayMerchantNewsCount(payMerchantNews)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayMerchantNews)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayMerchantNews
     * @param payMerchantNews
     * @throws Exception
     */
    public void addPayMerchantNews(PayMerchantNews payMerchantNews) throws Exception {
        new PayMerchantNewsDAO().addPayMerchantNews(payMerchantNews);
    }
    /**
     * remove PayMerchantNews
     * @param id
     * @throws Exception
     */
    public void removePayMerchantNews(String id) throws Exception {
        new PayMerchantNewsDAO().removePayMerchantNews(id);
    }
    /**
     * update PayMerchantNews
     * @param payMerchantNews
     * @throws Exception
     */
    public void updatePayMerchantNews(PayMerchantNews payMerchantNews) throws Exception {
        new PayMerchantNewsDAO().updatePayMerchantNews(payMerchantNews);
    }
    /**
     * detail PayMerchantNews
     * @param id
     * @throws Exception
     */
    public PayMerchantNews detailPayMerchantNews(String id) throws Exception {
        return new PayMerchantNewsDAO().detailPayMerchantNews(id);
    }

}