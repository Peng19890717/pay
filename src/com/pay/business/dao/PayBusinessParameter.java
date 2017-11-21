package com.pay.business.dao;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Table PAY_BUSINESS_PARAMETER entity.
 * 
 * @author administrator
 */
public class PayBusinessParameter {
    public String name;
    public String value;
    public String remark;
    public String getName(){
         return name;
    }
    public void setName(String name){
         this.name=name;
    }
    public String getValue(){
         return value;
    }
    public void setValue(String value){
         this.value=value;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String toString(){
        String temp = "";
        temp = temp + "name="+name+"\n";
        temp = temp + "value="+value+"\n";
        temp = temp + "remark="+remark+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("value",value);
        json.put("remark",remark);
        return json;
    }
}