package com.pay.muser.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_MERCHANT_USER entity.
 * 
 * @author administrator
 */
public class PayMerchantUser {
    public String userId = "";
    public String custId = "";
    public String userNam = "";
    public String userPwd = "";
    public String creaSign = "";
    public String random = "";
    public Date userDate;
    public String state = "";
    public String descInf = "";
    public String tel = "";
    public String email = "";
    public Long loginFailCount = 0l;
    public Date currentLoginTime;
    public String preset1 = "";
    public Date lastUppwdDate;
    public Long userExpiredTime = 0l;
    public String mailflg = "";
    public String getUserId(){
         return userId;
    }
    public void setUserId(String userId){
         this.userId=userId;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getUserNam(){
         return userNam;
    }
    public void setUserNam(String userNam){
         this.userNam=userNam;
    }
    public String getUserPwd(){
         return userPwd;
    }
    public void setUserPwd(String userPwd){
         this.userPwd=userPwd;
    }
    public String getCreaSign(){
         return creaSign;
    }
    public void setCreaSign(String creaSign){
         this.creaSign=creaSign;
    }
    public String getRandom(){
         return random;
    }
    public void setRandom(String random){
         this.random=random;
    }
    public Date getUserDate(){
         return userDate;
    }
    public void setUserDate(Date userDate){
         this.userDate=userDate;
    }
    public String getState(){
         return state;
    }
    public void setState(String state){
         this.state=state;
    }
    public String getDescInf(){
         return descInf;
    }
    public void setDescInf(String descInf){
         this.descInf=descInf;
    }
    public String getTel(){
         return tel;
    }
    public void setTel(String tel){
         this.tel=tel;
    }
    public String getEmail(){
         return email;
    }
    public void setEmail(String email){
         this.email=email;
    }
    public long getLoginFailCount(){
         return loginFailCount;
    }
    public void setLoginFailCount(long loginFailCount){
         this.loginFailCount=loginFailCount;
    }
    public Date getCurrentLoginTime(){
         return currentLoginTime;
    }
    public void setCurrentLoginTime(Date currentLoginTime){
         this.currentLoginTime=currentLoginTime;
    }
    public String getPreset1(){
         return preset1;
    }
    public void setPreset1(String preset1){
         this.preset1=preset1;
    }
    public Date getLastUppwdDate(){
         return lastUppwdDate;
    }
    public void setLastUppwdDate(Date lastUppwdDate){
         this.lastUppwdDate=lastUppwdDate;
    }
    public long getUserExpiredTime(){
         return userExpiredTime;
    }
    public void setUserExpiredTime(long userExpiredTime){
         this.userExpiredTime=userExpiredTime;
    }
    public String getMailflg(){
         return mailflg;
    }
    public void setMailflg(String mailflg){
         this.mailflg=mailflg;
    }
    public String toString(){
        String temp = "";
        temp = temp + "userId="+userId+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "userNam="+userNam+"\n";
        temp = temp + "userPwd="+userPwd+"\n";
        temp = temp + "creaSign="+creaSign+"\n";
        temp = temp + "random="+random+"\n";
        temp = temp + "userDate="+userDate+"\n";
        temp = temp + "state="+state+"\n";
        temp = temp + "descInf="+descInf+"\n";
        temp = temp + "tel="+tel+"\n";
        temp = temp + "email="+email+"\n";
        temp = temp + "loginFailCount="+loginFailCount+"\n";
        temp = temp + "currentLoginTime="+currentLoginTime+"\n";
        temp = temp + "preset1="+preset1+"\n";
        temp = temp + "lastUppwdDate="+lastUppwdDate+"\n";
        temp = temp + "userExpiredTime="+userExpiredTime+"\n";
        temp = temp + "mailflg="+mailflg+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("userId",userId);
        json.put("custId",custId);
        json.put("userNam",userNam);
        json.put("userPwd",userPwd);
        json.put("creaSign",creaSign);
        json.put("random",random);
        try{
            json.put("userDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userDate));
        } catch (Exception e) {}
        json.put("state",state);
        json.put("descInf",descInf);
        json.put("tel",tel);
        json.put("email",email);
        json.put("loginFailCount",String.valueOf(loginFailCount));
        try{
            json.put("currentLoginTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentLoginTime));
        } catch (Exception e) {}
        json.put("preset1",preset1);
        try{
            json.put("lastUppwdDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastUppwdDate));
        } catch (Exception e) {}
        json.put("userExpiredTime",String.valueOf(userExpiredTime));
        json.put("mailflg",mailflg);
        return json;
    }
}