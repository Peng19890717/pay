package com.pay.contract.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_CONTRACT entity.
 * 
 * @author administrator
 */
public class PayContract {
    public String seqNo;
    public String pactType;
    public String pactVersNo;
    public String pactName;
    public String pactCustType;
    public Date pactTakeEffDate;
    public Date pactLoseEffDate;
    public String pactContent2;
    public String pactStatus;
    public String creOperId;
    public Date creTime;
    public String lstUptOperId;
    public String lstUptOperName;
    public Date lstUptTime;
    public String lstUptTimeStart;
    public String lstUptTimeEnd;
    public String crePactMed;
    public String crePactChnl;
    public String pactRenew;
    public String crePactTime;
    public String bilaSignName;
    public String custId;
    public Date pactLoseEffDateStart;
    public Date pactLoseEffDateEnd;
    public String storeName;
    public String bizType;
    public String sellProductInfo;
    public String updateInfo;
    public String contractSignMan;
    public String custBilaSignName;
    
    public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Date getPactLoseEffDateStart() {
		return pactLoseEffDateStart;
	}
	public void setPactLoseEffDateStart(Date pactLoseEffDateStart) {
		this.pactLoseEffDateStart = pactLoseEffDateStart;
	}
	public Date getPactLoseEffDateEnd() {
		return pactLoseEffDateEnd;
	}
	public void setPactLoseEffDateEnd(Date pactLoseEffDateEnd) {
		this.pactLoseEffDateEnd = pactLoseEffDateEnd;
	}
	public String getSeqNo(){
         return seqNo;
    }
    public void setSeqNo(String seqNo){
         this.seqNo=seqNo;
    }
    public String getPactType(){
         return pactType;
    }
    public void setPactType(String pactType){
         this.pactType=pactType;
    }
    public String getPactVersNo(){
         return pactVersNo;
    }
    public void setPactVersNo(String pactVersNo){
         this.pactVersNo=pactVersNo;
    }
    public String getPactName(){
         return pactName;
    }
    public void setPactName(String pactName){
         this.pactName=pactName;
    }
    public String getPactCustType(){
         return pactCustType;
    }
    public void setPactCustType(String pactCustType){
         this.pactCustType=pactCustType;
    }
    public Date getPactTakeEffDate(){
         return pactTakeEffDate;
    }
    public void setPactTakeEffDate(Date pactTakeEffDate){
         this.pactTakeEffDate=pactTakeEffDate;
    }
    public Date getPactLoseEffDate(){
         return pactLoseEffDate;
    }
    public void setPactLoseEffDate(Date pactLoseEffDate){
         this.pactLoseEffDate=pactLoseEffDate;
    }
    public String getPactContent2(){
         return pactContent2;
    }
    public void setPactContent2(String pactContent2){
         this.pactContent2=pactContent2;
    }
    public String getPactStatus(){
         return pactStatus;
    }
    public void setPactStatus(String pactStatus){
         this.pactStatus=pactStatus;
    }
    public String getCreOperId(){
         return creOperId;
    }
    public void setCreOperId(String creOperId){
         this.creOperId=creOperId;
    }
    public Date getCreTime(){
         return creTime;
    }
    public void setCreTime(Date creTime){
         this.creTime=creTime;
    }
    public String getLstUptOperId(){
         return lstUptOperId;
    }
    public void setLstUptOperId(String lstUptOperId){
         this.lstUptOperId=lstUptOperId;
    }
    public Date getLstUptTime(){
         return lstUptTime;
    }
    public void setLstUptTime(Date lstUptTime){
         this.lstUptTime=lstUptTime;
    }
    public String getLstUptOperName() {
		return lstUptOperName;
	}
	public void setLstUptOperName(String lstUptOperName) {
		this.lstUptOperName = lstUptOperName;
	}
	public String getLstUptTimeStart() {
		return lstUptTimeStart;
	}
	public void setLstUptTimeStart(String lstUptTimeStart) {
		this.lstUptTimeStart = lstUptTimeStart;
	}
	public String getLstUptTimeEnd() {
		return lstUptTimeEnd;
	}
	public void setLstUptTimeEnd(String lstUptTimeEnd) {
		this.lstUptTimeEnd = lstUptTimeEnd;
	}
	public String getCrePactMed(){
         return crePactMed;
    }
    public void setCrePactMed(String crePactMed){
         this.crePactMed=crePactMed;
    }
    public String getCrePactChnl(){
         return crePactChnl;
    }
    public void setCrePactChnl(String crePactChnl){
         this.crePactChnl=crePactChnl;
    }
    public String getPactRenew(){
         return pactRenew;
    }
    public void setPactRenew(String pactRenew){
         this.pactRenew=pactRenew;
    }
    public String getCrePactTime(){
         return crePactTime;
    }
    public void setCrePactTime(String crePactTime){
         this.crePactTime=crePactTime;
    }
    public String getBilaSignName(){
         return bilaSignName;
    }
    public void setBilaSignName(String bilaSignName){
         this.bilaSignName=bilaSignName;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getSellProductInfo(){
        return sellProductInfo;
   }
   public void setSellProductInfo(String sellProductInfo){
        this.sellProductInfo=sellProductInfo;
   }
   public String getUpdateInfo(){
        return updateInfo;
   }
   public void setUpdateInfo(String updateInfo){
        this.updateInfo=updateInfo;
   }
   public String getContractSignMan(){
        return contractSignMan;
   }
   public void setContractSignMan(String contractSignMan){
        this.contractSignMan=contractSignMan;
   }
   public String getCustBilaSignName(){
        return custBilaSignName;
   }
   public void setCustBilaSignName(String custBilaSignName){
        this.custBilaSignName=custBilaSignName;
   }
    public String toString(){
        String temp = "";
        temp = temp + "seqNo="+seqNo+"\n";
        temp = temp + "pactType="+pactType+"\n";
        temp = temp + "pactVersNo="+pactVersNo+"\n";
        temp = temp + "pactName="+pactName+"\n";
        temp = temp + "pactCustType="+pactCustType+"\n";
        temp = temp + "pactTakeEffDate="+pactTakeEffDate+"\n";
        temp = temp + "pactLoseEffDate="+pactLoseEffDate+"\n";
        temp = temp + "pactContent2="+pactContent2+"\n";
        temp = temp + "pactStatus="+pactStatus+"\n";
        temp = temp + "creOperId="+creOperId+"\n";
        temp = temp + "creTime="+creTime+"\n";
        temp = temp + "lstUptOperId="+lstUptOperId+"\n";
        temp = temp + "lstUptOperName="+lstUptOperName+"\n";
        temp = temp + "lstUptTime="+lstUptTime+"\n";
        temp = temp + "crePactMed="+crePactMed+"\n";
        temp = temp + "crePactChnl="+crePactChnl+"\n";
        temp = temp + "pactRenew="+pactRenew+"\n";
        temp = temp + "crePactTime="+crePactTime+"\n";
        temp = temp + "bilaSignName="+bilaSignName+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "pactLoseEffDateStart="+pactLoseEffDateStart+"\n";
        temp = temp + "pactLoseEffDateEnd="+pactLoseEffDateEnd+"\n";
        temp = temp + "storeName="+storeName+"\n";
        temp = temp + "bizType="+bizType+"\n";
        temp = temp + "sellProductInfo="+sellProductInfo+"\n";
        temp = temp + "updateInfo="+updateInfo+"\n";
        temp = temp + "contractSignMan="+contractSignMan+"\n";
        temp = temp + "custBilaSignName="+custBilaSignName+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("seqNo",seqNo);
        json.put("pactType",pactType);
        json.put("pactVersNo",pactVersNo);
        json.put("pactName",pactName);
        json.put("pactCustType",pactCustType);
        try{
            json.put("pactTakeEffDate", new java.text.SimpleDateFormat("yyyy-MM-dd").format(pactTakeEffDate));
        } catch (Exception e) {}
        try{
            json.put("pactLoseEffDate", new java.text.SimpleDateFormat("yyyy-MM-dd").format(pactLoseEffDate));
        } catch (Exception e) {}
        json.put("pactContent2",pactContent2);
        json.put("pactStatus",pactStatus);
        json.put("creOperId",creOperId);
        try{
            json.put("creTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(creTime));
        } catch (Exception e) {}
        json.put("lstUptOperId",lstUptOperId);
        json.put("lstUptOperName",lstUptOperName);
        try{
            json.put("lstUptTime", new java.text.SimpleDateFormat("yyyy-MM-dd").format(lstUptTime));
        } catch (Exception e) {}
        json.put("crePactMed",crePactMed);
        json.put("crePactChnl",crePactChnl);
        json.put("pactRenew",pactRenew);
        try{
            json.put("crePactTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(crePactTime));
        } catch (Exception e) {}
        json.put("bilaSignName",bilaSignName);
        json.put("custId",custId);
        json.put("storeName",storeName);
        json.put("bizType",bizType);
        json.put("sellProductInfo",sellProductInfo);
        json.put("updateInfo",updateInfo);
        json.put("contractSignMan",contractSignMan);
        json.put("custBilaSignName",custBilaSignName);
        try{
            json.put("pactLoseEffDateStart", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pactLoseEffDateStart));
            json.put("pactLoseEffDateEnd", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pactLoseEffDateEnd));
        } catch (Exception e) {}
        return json;
    }
}