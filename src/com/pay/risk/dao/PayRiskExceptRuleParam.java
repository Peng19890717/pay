package com.pay.risk.dao;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Table PAY_RISK_EXCEPT_RULE_PARAM entity.
 * 
 * @author administrator
 */
public class PayRiskExceptRuleParam implements Comparable<PayRiskExceptRuleParam> {
    public String ruleCode;
    public String paramId;
    public String paramName;
    public String paramValue;
    public String paramType;
    public String getRuleCode(){
         return ruleCode;
    }
    public void setRuleCode(String ruleCode){
         this.ruleCode=ruleCode;
    }
    public String getParamId(){
         return paramId;
    }
    public void setParamId(String paramId){
         this.paramId=paramId;
    }
    public String getParamName(){
         return paramName;
    }
    public void setParamName(String paramName){
         this.paramName=paramName;
    }
    public String getParamValue(){
         return paramValue;
    }
    public void setParamValue(String paramValue){
         this.paramValue=paramValue;
    }
    public String getParamType(){
         return paramType;
    }
    public void setParamType(String paramType){
         this.paramType=paramType;
    }
    public String toString(){
        String temp = "";
        temp = temp + "ruleCode="+ruleCode+"\n";
        temp = temp + "paramId="+paramId+"\n";
        temp = temp + "paramName="+paramName+"\n";
        temp = temp + "paramValue="+paramValue+"\n";
        temp = temp + "paramType="+paramType+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("ruleCode",ruleCode);
        json.put("paramId",paramId);
        json.put("paramName",paramName);
        json.put("paramValue",paramValue);
        json.put("paramType",paramType);
        return json;
    }
    
    /**
     * 按照paramId的字典顺序进行排序
     */
	public int compareTo(PayRiskExceptRuleParam payriskexceptruleparam) {
		return this.paramId.compareTo(payriskexceptruleparam.paramId);
	}
}