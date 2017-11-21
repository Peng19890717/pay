package com.pay.merchant.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_OPENED_MER_PAY entity.
 * 
 * @author administrator
 */
public class PayOpenedMerPay {
    public String merno;
    public String type;
    public Date createtime;
    public String getMerno(){
         return merno;
    }
    public void setMerno(String merno){
         this.merno=merno;
    }
    public String getType(){
         return type;
    }
    public void setType(String type){
         this.type=type;
    }
    public Date getCreatetime(){
         return createtime;
    }
    public void setCreatetime(Date createtime){
         this.createtime=createtime;
    }
    public String toString(){
        String temp = "";
        temp = temp + "merno="+merno+"\n";
        temp = temp + "type="+type+"\n";
        temp = temp + "createtime="+createtime+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("merno",merno);
        json.put("type",type);
        try{
            json.put("createtime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createtime));
        } catch (Exception e) {}
        return json;
    }
}