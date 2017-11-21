package com.pay.coopbank.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_CHANNEL_UNFROZEN_DETAIL entity.
 * 
 * @author administrator
 */
public class PayChannelUnfrozenDetail {
    public String id;
    public Long amt;
    public String frozenId;
    public Date createTime;
    public String remark;
    public String optId;
    public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public Long getAmt(){
         return amt;
    }
    public void setAmt(Long amt){
         this.amt=amt;
    }
    public String getFrozenId(){
         return frozenId;
    }
    public void setFrozenId(String frozenId){
         this.frozenId=frozenId;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String getOptId(){
         return optId;
    }
    public void setOptId(String optId){
         this.optId=optId;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "amt="+amt+"\n";
        temp = temp + "frozenId="+frozenId+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "optId="+optId+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("amt",String.format("%.2f",((double)amt)/100d));
        json.put("frozenId",frozenId);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
        } catch (Exception e) {}
        json.put("remark",remark);
        json.put("optId",optId);
        return json;
    }
}