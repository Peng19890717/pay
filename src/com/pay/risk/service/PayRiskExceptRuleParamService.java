package com.pay.risk.service;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pay.risk.dao.PayRiskExceptRuleParam;
import com.pay.risk.dao.PayRiskExceptRuleParamDAO;

/**
 * Object PAY_RISK_EXCEPT_RULE_PARAM service. 
 * @author Administrator
 *
 */
public class PayRiskExceptRuleParamService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayRiskExceptRuleParamList(PayRiskExceptRuleParam payRiskExceptRuleParam,int page,int rows,String sort,String order){
        try {
            PayRiskExceptRuleParamDAO payRiskExceptRuleParamDAO = new PayRiskExceptRuleParamDAO();
            List list = payRiskExceptRuleParamDAO.getPayRiskExceptRuleParamList(payRiskExceptRuleParam, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payRiskExceptRuleParamDAO.getPayRiskExceptRuleParamCount(payRiskExceptRuleParam)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayRiskExceptRuleParam)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayRiskExceptRuleParam
     * @param payRiskExceptRuleParam
     * @throws Exception
     */
    public void addPayRiskExceptRuleParam(PayRiskExceptRuleParam payRiskExceptRuleParam) throws Exception {
        new PayRiskExceptRuleParamDAO().addPayRiskExceptRuleParam(payRiskExceptRuleParam);
    }
    /**
     * remove PayRiskExceptRuleParam
     * @param ruleCode
     * @throws Exception
     */
    public void removePayRiskExceptRuleParam(String ruleCode) throws Exception {
        new PayRiskExceptRuleParamDAO().removePayRiskExceptRuleParam(ruleCode);
    }
    /**
     * update PayRiskExceptRuleParam
     * @param payRiskExceptRuleParam
     * @throws Exception
     */
    public void updatePayRiskExceptRuleParam(PayRiskExceptRuleParam payRiskExceptRuleParam) throws Exception {
        new PayRiskExceptRuleParamDAO().updatePayRiskExceptRuleParam(payRiskExceptRuleParam);
    }
    /**
     * detail PayRiskExceptRuleParam
     * @param ruleCode
     * @throws Exception
     */
    public PayRiskExceptRuleParam detailPayRiskExceptRuleParam(String ruleCode) throws Exception {
        return new PayRiskExceptRuleParamDAO().detailPayRiskExceptRuleParam(ruleCode);
    }
    
    /**
     * 根据规则编号查询所有对应规则参数
     * @param ruleCode
     * @return
     * @throws Exception 
     */
	public List<PayRiskExceptRuleParam> detailByPayRiskExceptRule(String ruleCode) throws Exception {
		return new PayRiskExceptRuleParamDAO().detailByPayRiskExceptRule(ruleCode);
	}

}