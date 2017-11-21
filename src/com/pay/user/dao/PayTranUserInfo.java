package com.pay.user.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.pay.merchantinterface.dao.PayTranUserQuickCard;
import com.pay.order.dao.PayAccProfile;
/**
 * Table PAY_TRAN_USER_INFO entity.
 * 
 * @author administrator
 */
public class PayTranUserInfo {
    public String id;
    public String userId;
    public String loginPassword;
    public String passwordStrength;
    public Long loginPwdFailCount;
    public Date loginPwdLastTime;
    public String payPassword;
    public String payPwdStrength;
    public Long payPwdFailCount;
    public Date payPwdLastTime;
    public String befname;
    public String realName;
    public String cretType;
    public String cretNo;
    public String email;
    public String emailStatus;
    public String fax;
    public String province;
    public String city;
    public String area;
    public String address;
    public String zip;
    public String nationality;
    public String job;
    public String tel;
    public String mobile;
    public String birthday;
    public String gender;
    public String publicPhoto;
    public String credPhotoFront;
    public String credPhotoBack;
    public String sendType;
    public String userStatus;
    public String checkUserId;
    
    public Date checkTime;
    public String remark;
    public String revFlag;
    public String mobileStatus;
    public String userType;
    public Date registerTime;
    public Date certSubmitTime;
    public String registerType;
    public String trialStatus;
    public Map bindCardMap = new HashMap();
    public Map <String,PayTranUserQuickCard> quickH5CardMap = new HashMap<String,PayTranUserQuickCard>();
    public List<PayTranUserQuickCard> quickH5CardList = new ArrayList<PayTranUserQuickCard>();
    public Long cashacbal;
    public Long consacbal;
    public Long frozbalance;
    public String acstatus;
    
    public Date registerTime_start;
    public Date registerTime_end;
    public Date checkTime_start;
    public Date checkTime_end;
    public String validTime;
    public String in_validTime;
    public PayAccProfile accProfile;
    
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	public String getIn_validTime() {
		return in_validTime;
	}
	public void setIn_validTime(String in_validTime) {
		this.in_validTime = in_validTime;
	}
	public Date getRegisterTime_start() {
		return registerTime_start;
	}
	public void setRegisterTime_start(Date registerTime_start) {
		this.registerTime_start = registerTime_start;
	}
	public Date getRegisterTime_end() {
		return registerTime_end;
	}
	public void setRegisterTime_end(Date registerTime_end) {
		this.registerTime_end = registerTime_end;
	}
	public Date getCheckTime_start() {
		return checkTime_start;
	}
	public void setCheckTime_start(Date checkTime_start) {
		this.checkTime_start = checkTime_start;
	}
	public Date getCheckTime_end() {
		return checkTime_end;
	}
	public void setCheckTime_end(Date checkTime_end) {
		this.checkTime_end = checkTime_end;
	}
	
	public Long getCashacbal() {
		return cashacbal;
	}
	public void setCashacbal(Long cashacbal) {
		this.cashacbal = cashacbal;
	}
	public Long getConsacbal() {
		return consacbal;
	}
	public void setConsacbal(Long consacbal) {
		this.consacbal = consacbal;
	}
	public Long getFrozbalance() {
		return frozbalance;
	}
	public void setFrozbalance(Long frozbalance) {
		this.frozbalance = frozbalance;
	}
	public String getAcstatus() {
		return acstatus;
	}
	public void setAcstatus(String acstatus) {
		this.acstatus = acstatus;
	}
	public Map getBindCardMap() {
		return bindCardMap;
	}
	public void setBindCardMap(Map bindCardMap) {
		this.bindCardMap = bindCardMap;
	}
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getUserId(){
         return userId;
    }
    public void setUserId(String userId){
         this.userId=userId;
    }
    public String getLoginPassword(){
         return loginPassword;
    }
    public void setLoginPassword(String loginPassword){
         this.loginPassword=loginPassword;
    }
    public String getPasswordStrength(){
         return passwordStrength;
    }
    public void setPasswordStrength(String passwordStrength){
         this.passwordStrength=passwordStrength;
    }
    public Long getLoginPwdFailCount(){
         return loginPwdFailCount;
    }
    public void setLoginPwdFailCount(Long loginPwdFailCount){
         this.loginPwdFailCount=loginPwdFailCount;
    }
    public Date getLoginPwdLastTime(){
         return loginPwdLastTime;
    }
    public void setLoginPwdLastTime(Date loginPwdLastTime){
         this.loginPwdLastTime=loginPwdLastTime;
    }
    public String getPayPassword(){
         return payPassword;
    }
    public void setPayPassword(String payPassword){
         this.payPassword=payPassword;
    }
    public String getPayPwdStrength(){
         return payPwdStrength;
    }
    public void setPayPwdStrength(String payPwdStrength){
         this.payPwdStrength=payPwdStrength;
    }
    public Long getPayPwdFailCount(){
         return payPwdFailCount;
    }
    public void setPayPwdFailCount(Long payPwdFailCount){
         this.payPwdFailCount=payPwdFailCount;
    }
    public Date getPayPwdLastTime(){
         return payPwdLastTime;
    }
    public void setPayPwdLastTime(Date payPwdLastTime){
         this.payPwdLastTime=payPwdLastTime;
    }
    public String getBefname(){
         return befname;
    }
    public void setBefname(String befname){
         this.befname=befname;
    }
    public String getRealName(){
         return realName;
    }
    public void setRealName(String realName){
         this.realName=realName;
    }
    public String getCretType(){
         return cretType;
    }
    public void setCretType(String cretType){
         this.cretType=cretType;
    }
    public String getCretNo(){
         return cretNo;
    }
    public void setCretNo(String cretNo){
         this.cretNo=cretNo;
    }
    public String getEmail(){
         return email;
    }
    public void setEmail(String email){
         this.email=email;
    }
    public String getEmailStatus(){
         return emailStatus;
    }
    public void setEmailStatus(String emailStatus){
         this.emailStatus=emailStatus;
    }
    public String getFax(){
         return fax;
    }
    public void setFax(String fax){
         this.fax=fax;
    }
    public String getProvince(){
         return province;
    }
    public void setProvince(String province){
         this.province=province;
    }
    public String getCity(){
         return city;
    }
    public void setCity(String city){
         this.city=city;
    }
    public String getArea(){
         return area;
    }
    public void setArea(String area){
         this.area=area;
    }
    public String getAddress(){
         return address;
    }
    public void setAddress(String address){
         this.address=address;
    }
    public String getZip(){
         return zip;
    }
    public void setZip(String zip){
         this.zip=zip;
    }
    public String getNationality(){
         return nationality;
    }
    public void setNationality(String nationality){
         this.nationality=nationality;
    }
    public String getJob(){
         return job;
    }
    public void setJob(String job){
         this.job=job;
    }
    public String getTel(){
         return tel;
    }
    public void setTel(String tel){
         this.tel=tel;
    }
    public String getMobile(){
         return mobile;
    }
    public void setMobile(String mobile){
         this.mobile=mobile;
    }
    public String getBirthday(){
         return birthday;
    }
    public void setBirthday(String birthday){
         this.birthday=birthday;
    }
    public String getGender(){
         return gender;
    }
    public void setGender(String gender){
         this.gender=gender;
    }
    public String getPublicPhoto(){
         return publicPhoto;
    }
    public void setPublicPhoto(String publicPhoto){
         this.publicPhoto=publicPhoto;
    }
    public String getCredPhotoFront(){
         return credPhotoFront;
    }
    public void setCredPhotoFront(String credPhotoFront){
         this.credPhotoFront=credPhotoFront;
    }
    public String getCredPhotoBack(){
         return credPhotoBack;
    }
    public void setCredPhotoBack(String credPhotoBack){
         this.credPhotoBack=credPhotoBack;
    }
    public String getSendType(){
         return sendType;
    }
    public void setSendType(String sendType){
         this.sendType=sendType;
    }
    public String getUserStatus(){
         return userStatus;
    }
    public void setUserStatus(String userStatus){
         this.userStatus=userStatus;
    }
    public String getCheckUserId(){
         return checkUserId;
    }
    public void setCheckUserId(String checkUserId){
         this.checkUserId=checkUserId;
    }
    public Date getCheckTime(){
         return checkTime;
    }
    public void setCheckTime(Date checkTime){
         this.checkTime=checkTime;
    }
    public String getRemark(){
         return remark;
    }
    public void setRemark(String remark){
         this.remark=remark;
    }
    public String getRevFlag(){
         return revFlag;
    }
    public void setRevFlag(String revFlag){
         this.revFlag=revFlag;
    }
    public String getMobileStatus(){
         return mobileStatus;
    }
    public void setMobileStatus(String mobileStatus){
         this.mobileStatus=mobileStatus;
    }
    public String getUserType(){
         return userType;
    }
    public void setUserType(String userType){
         this.userType=userType;
    }
    public Date getRegisterTime(){
         return registerTime;
    }
    public void setRegisterTime(Date registerTime){
         this.registerTime=registerTime;
    }
    public Date getCertSubmitTime(){
         return certSubmitTime;
    }
    public void setCertSubmitTime(Date certSubmitTime){
         this.certSubmitTime=certSubmitTime;
    }
    public String getRegisterType(){
         return registerType;
    }
    public void setRegisterType(String registerType){
         this.registerType=registerType;
    }
    public String getTrialStatus(){
         return trialStatus;
    }
    public void setTrialStatus(String trialStatus){
         this.trialStatus=trialStatus;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "userId="+userId+"\n";
        temp = temp + "loginPassword="+loginPassword+"\n";
        temp = temp + "passwordStrength="+passwordStrength+"\n";
        temp = temp + "loginPwdFailCount="+loginPwdFailCount+"\n";
        temp = temp + "loginPwdLastTime="+loginPwdLastTime+"\n";
        temp = temp + "payPassword="+payPassword+"\n";
        temp = temp + "payPwdStrength="+payPwdStrength+"\n";
        temp = temp + "payPwdFailCount="+payPwdFailCount+"\n";
        temp = temp + "payPwdLastTime="+payPwdLastTime+"\n";
        temp = temp + "befname="+befname+"\n";
        temp = temp + "realName="+realName+"\n";
        temp = temp + "cretType="+cretType+"\n";
        temp = temp + "cretNo="+cretNo+"\n";
        temp = temp + "email="+email+"\n";
        temp = temp + "emailStatus="+emailStatus+"\n";
        temp = temp + "fax="+fax+"\n";
        temp = temp + "province="+province+"\n";
        temp = temp + "city="+city+"\n";
        temp = temp + "area="+area+"\n";
        temp = temp + "address="+address+"\n";
        temp = temp + "zip="+zip+"\n";
        temp = temp + "nationality="+nationality+"\n";
        temp = temp + "job="+job+"\n";
        temp = temp + "tel="+tel+"\n";
        temp = temp + "mobile="+mobile+"\n";
        temp = temp + "birthday="+birthday+"\n";
        temp = temp + "gender="+gender+"\n";
        temp = temp + "publicPhoto="+publicPhoto+"\n";
        temp = temp + "credPhotoFront="+credPhotoFront+"\n";
        temp = temp + "credPhotoBack="+credPhotoBack+"\n";
        temp = temp + "sendType="+sendType+"\n";
        temp = temp + "userStatus="+userStatus+"\n";
        temp = temp + "checkUserId="+checkUserId+"\n";
        temp = temp + "checkTime="+checkTime+"\n";
        temp = temp + "remark="+remark+"\n";
        temp = temp + "revFlag="+revFlag+"\n";
        temp = temp + "mobileStatus="+mobileStatus+"\n";
        temp = temp + "userType="+userType+"\n";
        temp = temp + "registerTime="+registerTime+"\n";
        temp = temp + "certSubmitTime="+certSubmitTime+"\n";
        temp = temp + "registerType="+registerType+"\n";
        temp = temp + "trialStatus="+trialStatus+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("userId",userId);
        json.put("loginPassword",loginPassword);
        json.put("passwordStrength",passwordStrength);
        json.put("loginPwdFailCount",String.valueOf(loginPwdFailCount));
        try{
            json.put("loginPwdLastTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loginPwdLastTime));
        } catch (Exception e) {}
        json.put("payPassword",payPassword);
        json.put("payPwdStrength",payPwdStrength);
        json.put("payPwdFailCount",String.valueOf(payPwdFailCount));
        try{
            json.put("payPwdLastTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payPwdLastTime));
        } catch (Exception e) {}
        json.put("befname",befname);
        json.put("realName",realName);
        json.put("cretType",cretType);
        json.put("cretNo",cretNo);
        json.put("email",email);
        json.put("emailStatus",emailStatus);
        json.put("fax",fax);
        json.put("province",province);
        json.put("city",city);
        json.put("area",area);
        json.put("address",address);
        json.put("zip",zip);
        json.put("nationality",nationality);
        json.put("job",job);
        json.put("tel",tel);
        json.put("mobile",mobile);
        json.put("birthday",birthday);
        json.put("gender",gender);
        json.put("publicPhoto",publicPhoto);
        json.put("credPhotoFront",credPhotoFront);
        json.put("credPhotoBack",credPhotoBack);
        json.put("sendType",sendType);
        json.put("userStatus",userStatus);
        json.put("checkUserId",checkUserId);
        try{
            json.put("checkTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(checkTime));
        } catch (Exception e) {}
        json.put("remark",remark);
        json.put("revFlag",revFlag);
        json.put("mobileStatus",mobileStatus);
        json.put("userType",userType);
        try{
            json.put("registerTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(registerTime));
        } catch (Exception e) {}
        try{
            json.put("certSubmitTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(certSubmitTime));
        } catch (Exception e) {}
        json.put("registerType",registerType);
        json.put("trialStatus",trialStatus);
        
        json.put("acstatus",acstatus);
        json.put("cashacbal",String.format("%.2f", ((double)cashacbal)/100d));
        json.put("consacbal",String.format("%.2f", ((double)consacbal)/100d));
        json.put("frozbalance",String.format("%.2f", ((double)frozbalance)/100d));
        
        return json;
    }
}