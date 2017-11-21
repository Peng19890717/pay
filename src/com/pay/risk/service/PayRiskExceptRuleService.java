package com.pay.risk.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import util.Tools;

import com.pay.risk.dao.PayRiskExceptRule;
import com.pay.risk.dao.PayRiskExceptRuleDAO;
import com.pay.risk.dao.PayRiskExceptRuleParam;
import com.pay.risk.dao.PayRiskExceptRuleParamDAO;

/**
 * Object PAY_RISK_EXCEPT_RULE service. 
 * @author Administrator
 *
 */
public class PayRiskExceptRuleService {
	public static List RISK_RULE_LIST = new ArrayList();
	static {
		loadRiskRuleList();
	}
	public static void loadRiskRuleList(){
		try {
			RISK_RULE_LIST = new PayRiskExceptRuleDAO().getList();
			List<PayRiskExceptRuleParam> ruleParamList = new PayRiskExceptRuleParamDAO().getList();
			for(int i = 0; i<RISK_RULE_LIST.size(); i++){
				PayRiskExceptRule rule = (PayRiskExceptRule)RISK_RULE_LIST.get(i);
				for (PayRiskExceptRuleParam ruleParam : ruleParamList) {
					if(ruleParam.getRuleCode().equals(rule.ruleCode)){
						rule.paramList.add(ruleParam);
					}
				}
			}
			util.PayUtil.synNotifyForAllNode("8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /**
     * Get records list(json).
     * @return
     */
    public String getPayRiskExceptRuleList(PayRiskExceptRule payRiskExceptRule,int page,int rows,String sort,String order){
        try {
            PayRiskExceptRuleDAO payRiskExceptRuleDAO = new PayRiskExceptRuleDAO();
            List list = payRiskExceptRuleDAO.getPayRiskExceptRuleList(payRiskExceptRule, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payRiskExceptRuleDAO.getPayRiskExceptRuleCount(payRiskExceptRule)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayRiskExceptRule)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayRiskExceptRule
     * @param payRiskExceptRule
     * @param request 
     * @throws Exception
     */
    @Transactional
    public void addPayRiskExceptRule(PayRiskExceptRule payRiskExceptRule, HttpServletRequest request) throws Exception {
    	payRiskExceptRule.ruleCode = Tools.getUniqueIdentify();
    	payRiskExceptRule.createTime = new Date();
    	payRiskExceptRule.updateTime = new Date();
    	new PayRiskExceptRuleDAO().addPayRiskExceptRule(payRiskExceptRule);
    	addPayRiskExceptRuleParam(payRiskExceptRule, request);
    	loadRiskRuleList();
    }
    /**
     * remove PayRiskExceptRule
     * @param ruleCode
     * @throws Exception
     */
    @Transactional
    public void removePayRiskExceptRule(String ruleCode) throws Exception {
    	new PayRiskExceptRuleParamService().removePayRiskExceptRuleParam(ruleCode);
        new PayRiskExceptRuleDAO().removePayRiskExceptRule(ruleCode);
        loadRiskRuleList();
    }
    /**
     * update PayRiskExceptRule
     * @param payRiskExceptRule
     * @param request 
     * @throws Exception
     */
    @Transactional
    public void updatePayRiskExceptRule(PayRiskExceptRule payRiskExceptRule, HttpServletRequest request) throws Exception {
    	new PayRiskExceptRuleParamService().removePayRiskExceptRuleParam(request.getParameter("ruleCode"));
        new PayRiskExceptRuleDAO().updatePayRiskExceptRule(payRiskExceptRule);
        addPayRiskExceptRuleParam(payRiskExceptRule, request);
        loadRiskRuleList();
    }
    
    /**
     * detail PayRiskExceptRule
     * @param ruleCode
     * @throws Exception
     */
    public PayRiskExceptRule detailPayRiskExceptRule(String ruleCode) throws Exception {
        return new PayRiskExceptRuleDAO().detailPayRiskExceptRule(ruleCode);
    }
    
    /**
     * 修改规则状态
     * @param ruleCode 操作ID
     * @param columName 列名
     * @param opration 操作值
     * @throws Exception 数据库操作异常
     */
	public void updatePayRiskExceptRuleStatus(String ruleCode,String columName, String opration) throws Exception {
		new PayRiskExceptRuleDAO().updatePayRiskExceptRuleStatus(ruleCode,columName,opration);
		loadRiskRuleList();
	}
	
	/**
	 * 添加规则参数
	 * @param payRiskExceptRule
	 * @param request
	 * @throws Exception
	 */
	private void addPayRiskExceptRuleParam(PayRiskExceptRule payRiskExceptRule,HttpServletRequest request) throws Exception {
		String[] paramNames = request.getParameterValues("paramName");
		String[] paramValues = request.getParameterValues("paramValue");
		String[] paramTypes = request.getParameterValues("paramType");
		if(null != paramNames && null != paramValues && null != paramTypes) {
			for (int i = 0; i < paramNames.length; i++) {
				PayRiskExceptRuleParam payRiskExceptRuleParam = new PayRiskExceptRuleParam();
				payRiskExceptRuleParam.ruleCode = payRiskExceptRule.ruleCode;
				payRiskExceptRuleParam.paramId = Tools.getUniqueIdentify();
				payRiskExceptRuleParam.paramName = paramNames[i];
				payRiskExceptRuleParam.paramValue = paramValues[i];
				payRiskExceptRuleParam.paramType = paramTypes[i];
				new PayRiskExceptRuleParamService().addPayRiskExceptRuleParam(payRiskExceptRuleParam);
			}
		} else {
			throw new RuntimeException();
		}
	}
}