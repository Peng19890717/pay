package com.pay.fee.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pay.fee.dao.PayCustFee;
import com.pay.fee.dao.PayCustFeeDAO;

/**
 * Object PAY_CUST_FEE service. 
 * @author Administrator
 *
 */
public class PayCustFeeService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayCustFeeList(PayCustFee payCustFee,int page,int rows,String sort,String order){
        try {
            PayCustFeeDAO payCustFeeDAO = new PayCustFeeDAO();
            List list = payCustFeeDAO.getPayCustFeeList(payCustFee, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payCustFeeDAO.getPayCustFeeCount(payCustFee)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayCustFee)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayCustFee
     * @param payCustFee
     * @throws Exception
     */
    public void addPayCustFee(PayCustFee payCustFee) throws Exception {
        new PayCustFeeDAO().addPayCustFee(payCustFee);
    }
    /**
     * remove PayCustFee
     * @param id
     * @throws Exception
     */
    public void removePayCustFee(String id) throws Exception {
        new PayCustFeeDAO().removePayCustFee(id);
    }
    /**
     * update PayCustFee
     * @param payCustFee
     * @throws Exception
     */
    public void updatePayCustFee(PayCustFee payCustFee) throws Exception {
        new PayCustFeeDAO().updatePayCustFee(payCustFee);
    }
    /**
     * detail PayCustFee
     * @param id
     * @throws Exception
     */
    public PayCustFee detailPayCustFee(String id) throws Exception {
        return new PayCustFeeDAO().detailPayCustFee(id);
    }
    
    /**
     * 根据客户ID删除早期费率
     * @param custId
     * @return
     * @throws Exception 
     */
	public int removePayCustFeeForCustId(String custId) throws Exception {
		return new PayCustFeeDAO().removePayCustFeeForCustId(custId);
	}

}