package com.pay.custstl.service;

import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.jweb.dao.JWebUser;
import com.pay.custstl.dao.PayCustStlInfoDAO;
import com.pay.custstl.dao.PayCustStlInfo;
import com.pay.merchant.dao.PayMerchant;

/**
 * Object PAY_CUST_STL_INFO service. 
 * @author Administrator
 *
 */
public class PayCustStlInfoService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayCustStlInfoList(PayCustStlInfo payCustStlInfo,int page,int rows,String sort,String order){
        try {
            PayCustStlInfoDAO payCustStlInfoDAO = new PayCustStlInfoDAO();
            List list = payCustStlInfoDAO.getPayCustStlInfoList(payCustStlInfo, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payCustStlInfoDAO.getPayCustStlInfoCount(payCustStlInfo)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayCustStlInfo)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayCustStlInfo
     * @param payCustStlInfo
     * @throws Exception
     */
    public void addPayCustStlInfo(PayCustStlInfo payCustStlInfo) throws Exception {
        new PayCustStlInfoDAO().addPayCustStlInfo(payCustStlInfo);
    }
    /**
     * remove PayCustStlInfo
     * @param id
     * @throws Exception
     */
    public void removePayCustStlInfo(String seqNo) throws Exception {
        new PayCustStlInfoDAO().removePayCustStlInfo(seqNo);
    }
    /**
     * 组合商户结算信息
     * @param payMerchant
     * @param user
     * @param payCustStlInfo
     * @throws Exception 
     */
	public void addPayCustStlInfo(PayMerchant payMerchant, JWebUser user,PayCustStlInfo payCustStlInfo) throws Exception {
    	payCustStlInfo.setSeqNo(Tools.getUniqueIdentify());
    	payCustStlInfo.setCustId(payMerchant.getCustId());
    	payCustStlInfo.setCreTime(new Date());
    	payCustStlInfo.setCreOperId(user.getId());
    	payCustStlInfo.setLstUptTime(new Date());
    	payCustStlInfo.setLstUptOperId(user.getId());
    	new PayCustStlInfoDAO().addPayCustStlInfo(payCustStlInfo);
	}
	public PayCustStlInfo getPayCustStlInfoByMerchantId(String custId) throws Exception {
		return new PayCustStlInfoDAO().getPayCustStlInfoByMerchantId(custId);
	}
	/**
	 * 
	 * @return
	 */
	public static String getShowInfoByStlPeriod(String custSetPeriod,String custStlTimeSet){
		String result = "";
		custStlTimeSet = "|"+custStlTimeSet+"|";
		try {
			if(("M").equals(custSetPeriod)) {
	        	for(int i=0; i<31; i++){
	        		if(custStlTimeSet.indexOf("|"+(i+1)+"|") != -1)result = result+(i+1)+"日 ";
	        	}
	        	return result;
	        } else if(("W").equals(custSetPeriod)) {
	        	String [] week = {"一","二","三","四","五","六","日"};
	        	for(int i=0; i<week.length; i++){
	        		if(custStlTimeSet.indexOf("|"+(i+1)+"|") != -1){
	        			result = result+week[i]+" ";
	        		}
	        	}
	        	return "周"+result;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}