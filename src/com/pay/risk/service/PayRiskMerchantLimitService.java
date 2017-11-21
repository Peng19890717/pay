package com.pay.risk.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.risk.dao.PayRiskMerchantLimit;
import com.pay.risk.dao.PayRiskMerchantLimitDAO;

/**
 * Object PAY_RISK_MERCHANT_LIMIT service. 
 * @author Administrator
 *
 */
public class PayRiskMerchantLimitService {
	public static Map LIMIT_MER_NO_KEY = new HashMap();
	public static Map LIMIT_NORMAL_KEY = new HashMap();
	static {
		loadMerchantLimit();
	}
	/**
	 * 载入商户限额表，限额表保存在内存，随时调用
	 * 指定商户情况下，商户号+业务类型为主键
	 * 其他情况下，业务类型为主键
	 */
	public static void loadMerchantLimit(){
		LIMIT_MER_NO_KEY = new HashMap();
		LIMIT_NORMAL_KEY = new HashMap();
		try {
			List list = new PayRiskMerchantLimitDAO().getList();
			for(int i = 0; i<list.size(); i++){
				PayRiskMerchantLimit lim = (PayRiskMerchantLimit)list.get(i);
				if(!"-1".equals(lim.limitCompCode)){
					LIMIT_MER_NO_KEY.put(lim.limitCompCode+","+lim.limitCompType, lim);
				} else {
					LIMIT_NORMAL_KEY.put(lim.limitRiskLevel+","+lim.limitCompType,lim);
				}
			}
			util.PayUtil.synNotifyForAllNode("9");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取商户限额
	 * @param compCode 可为空
	 * @param limitType 可为空  限额类型:1消费 2充值 3结算 4退款 5提现 6转账 默认1
	 * @return
	 */
	public static PayRiskMerchantLimit getMerchantLimit(String compCode,String limitType){
		//商户号获取
		PayRiskMerchantLimit lim = (PayRiskMerchantLimit)LIMIT_MER_NO_KEY.get(compCode+","+limitType);
		//未指定商户限额
		if(lim==null){
			try {
				String riskLevel = new PayMerchantDAO().getPayMerchantRiskLevel(compCode);
				if(riskLevel == null)return null;
				//风险级别获取限额
				lim = (PayRiskMerchantLimit) LIMIT_NORMAL_KEY.get(riskLevel+","+limitType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return lim;
	}
    /**
     * Get records list(json).
     * @return
     */
    public String getPayRiskMerchantLimitList(PayRiskMerchantLimit payRiskMerchantLimit,int page,int rows,String sort,String order){
        try {
            PayRiskMerchantLimitDAO payRiskMerchantLimitDAO = new PayRiskMerchantLimitDAO();
            List list = payRiskMerchantLimitDAO.getPayRiskMerchantLimitList(payRiskMerchantLimit, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payRiskMerchantLimitDAO.getPayRiskMerchantLimitCount(payRiskMerchantLimit)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayRiskMerchantLimit)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayRiskMerchantLimit
     * @param payRiskMerchantLimit
     * @throws Exception
     */
    public void addPayRiskMerchantLimit(PayRiskMerchantLimit payRiskMerchantLimit) throws Exception {
    	payRiskMerchantLimit.id = Tools.getUniqueIdentify();
    	payRiskMerchantLimit.createTime = new Date();
    	payRiskMerchantLimit.updateTime = new Date();
    	payRiskMerchantLimit.limitCompCode = "1".equals(payRiskMerchantLimit.limitType) ? "-1" : payRiskMerchantLimit.limitCompCode;
    	payRiskMerchantLimit.limitRiskLevel = "2".equals(payRiskMerchantLimit.limitType) ? "-" : payRiskMerchantLimit.limitRiskLevel;
        new PayRiskMerchantLimitDAO().addPayRiskMerchantLimit(payRiskMerchantLimit);
        loadMerchantLimit();
    }
    /**
     * remove PayRiskMerchantLimit
     * @param id
     * @throws Exception
     */
    public void removePayRiskMerchantLimit(String id) throws Exception {
        new PayRiskMerchantLimitDAO().removePayRiskMerchantLimit(id);
        loadMerchantLimit();
    }
    /**
     * update PayRiskMerchantLimit
     * @param payRiskMerchantLimit
     * @throws Exception
     */
    public void updatePayRiskMerchantLimit(PayRiskMerchantLimit payRiskMerchantLimit) throws Exception {
    	payRiskMerchantLimit.limitCompCode = "1".equals(payRiskMerchantLimit.limitType) ? "-1" : payRiskMerchantLimit.limitCompCode;
    	payRiskMerchantLimit.limitRiskLevel = "2".equals(payRiskMerchantLimit.limitType) ? "-" : payRiskMerchantLimit.limitRiskLevel;
    	new PayRiskMerchantLimitDAO().updatePayRiskMerchantLimit(payRiskMerchantLimit);
    	loadMerchantLimit();
    }
    /**
     * detail PayRiskMerchantLimit
     * @param id
     * @throws Exception
     */
    public PayRiskMerchantLimit detailPayRiskMerchantLimit(String id) throws Exception {
        return new PayRiskMerchantLimitDAO().detailPayRiskMerchantLimit(id);
    }

}