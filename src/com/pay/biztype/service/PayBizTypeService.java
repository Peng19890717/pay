package com.pay.biztype.service;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.PayConstant;
import com.pay.biztype.dao.PayBizType;
import com.pay.biztype.dao.PayBizTypeDAO;

/**
 * Object PAY_BIZ_TYPE service. 
 * @author Administrator
 *
 */
public class PayBizTypeService {
	/**
	 * Object PAY_BIZ_TYPE service. 
	 * @author xuke
	 */
	public static void loadBizTypeList(){
		try {
			List tmp = new PayBizTypeDAO().getList(); 
			for(int i = 0; i<tmp.size(); i++){
				PayBizType bizType = (PayBizType)tmp.get(i);
				PayConstant.MER_BIZ_TYPE.put(bizType.code, bizType.name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /**
     * Get records list(json).
     * @return
     */
    public String getPayBizTypeList(PayBizType payBizType,int page,int rows,String sort,String order){
        try {
            PayBizTypeDAO payBizTypeDAO = new PayBizTypeDAO();
            List list = payBizTypeDAO.getPayBizTypeList(payBizType, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payBizTypeDAO.getPayBizTypeCount(payBizType)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayBizType)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayBizType
     * @param payBizType
     * @throws Exception
     */
    public void addPayBizType(PayBizType payBizType) throws Exception {
    	payBizType.setSeqNo(Tools.getUniqueIdentify());
    	payBizType.setCreTime(new Date());
    	payBizType.setUptTime(new Date());
        new PayBizTypeDAO().addPayBizType(payBizType);
        loadBizTypeList();
    }
    /**
     * update PayBizType
     * @param payBizType
     * @throws Exception
     */
    public void updatePayBizType(PayBizType payBizType) throws Exception {
    	payBizType.setUptTime(new Date());
        new PayBizTypeDAO().updatePayBizType(payBizType);
        loadBizTypeList();
    }
    /**
     * detail PayBizType
     * @param seqNo
     * @throws Exception
     */
    public PayBizType detailPayBizType(String seqNo) throws Exception {
        return new PayBizTypeDAO().detailPayBizType(seqNo);
    }
}