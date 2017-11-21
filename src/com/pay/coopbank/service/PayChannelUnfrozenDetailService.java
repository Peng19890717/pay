package com.pay.coopbank.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.pay.coopbank.dao.PayChannelUnfrozenDetailDAO;
import com.pay.coopbank.dao.PayChannelUnfrozenDetail;

/**
 * Object pay_channel_unfrozen_detail service. 
 * @author Administrator
 *
 */
public class PayChannelUnfrozenDetailService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayChannelUnfrozenDetailList(PayChannelUnfrozenDetail payChannelUnfrozenDetail,int page,int rows,String sort,String order){
        try {
            PayChannelUnfrozenDetailDAO payChannelUnfrozenDetailDAO = new PayChannelUnfrozenDetailDAO();
            List list = payChannelUnfrozenDetailDAO.getPayChannelUnfrozenDetailList(payChannelUnfrozenDetail, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payChannelUnfrozenDetailDAO.getPayChannelUnfrozenDetailCount(payChannelUnfrozenDetail)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayChannelUnfrozenDetail)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayChannelUnfrozenDetail
     * @param payChannelUnfrozenDetail
     * @throws Exception
     */
    public void addPayChannelUnfrozenDetail(PayChannelUnfrozenDetail payChannelUnfrozenDetail) throws Exception {
        new PayChannelUnfrozenDetailDAO().addPayChannelUnfrozenDetail(payChannelUnfrozenDetail);
    }
    /**
     * remove PayChannelUnfrozenDetail
     * @param id
     * @throws Exception
     */
    public void removePayChannelUnfrozenDetail(String id) throws Exception {
        new PayChannelUnfrozenDetailDAO().removePayChannelUnfrozenDetail(id);
    }
    /**
     * update PayChannelUnfrozenDetail
     * @param payChannelUnfrozenDetail
     * @throws Exception
     */
    public void updatePayChannelUnfrozenDetail(PayChannelUnfrozenDetail payChannelUnfrozenDetail) throws Exception {
        new PayChannelUnfrozenDetailDAO().updatePayChannelUnfrozenDetail(payChannelUnfrozenDetail);
    }
    /**
     * detail PayChannelUnfrozenDetail
     * @param id
     * @throws Exception
     */
    public PayChannelUnfrozenDetail detailPayChannelUnfrozenDetail(String id) throws Exception {
        return new PayChannelUnfrozenDetailDAO().detailPayChannelUnfrozenDetail(id);
    }

}