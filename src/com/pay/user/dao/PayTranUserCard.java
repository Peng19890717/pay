package com.pay.user.dao;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Table PAY_TRAN_USER_CARD entity.
 * 
 * @author administrator
 */
public class PayTranUserCard {
    public String id;
    public String userId;
    public String cardBank;
    public String cardType;
    public String cardStatus;
    public String cardStaRes;
    public String cardNo;
    public String cardBankBranch;
    public Date bakOpenTime;
    public Long bakOpenNum;
    public Date bakCloseTime;
    public Long bakCloseNum;
    public String bakCloseRes;
    public String bakUserId;
    public Date bakUpdTime;
    public String revFlag;
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
    public String getCardBank(){
         return cardBank;
    }
    public void setCardBank(String cardBank){
         this.cardBank=cardBank;
    }
    public String getCardType(){
         return cardType;
    }
    public void setCardType(String cardType){
         this.cardType=cardType;
    }
    public String getCardStatus(){
         return cardStatus;
    }
    public void setCardStatus(String cardStatus){
         this.cardStatus=cardStatus;
    }
    public String getCardStaRes(){
         return cardStaRes;
    }
    public void setCardStaRes(String cardStaRes){
         this.cardStaRes=cardStaRes;
    }
    public String getCardNo(){
         return cardNo;
    }
    public void setCardNo(String cardNo){
         this.cardNo=cardNo;
    }
    public String getCardBankBranch(){
         return cardBankBranch;
    }
    public void setCardBankBranch(String cardBankBranch){
         this.cardBankBranch=cardBankBranch;
    }
    public Date getBakOpenTime(){
         return bakOpenTime;
    }
    public void setBakOpenTime(Date bakOpenTime){
         this.bakOpenTime=bakOpenTime;
    }
    public Long getBakOpenNum(){
         return bakOpenNum;
    }
    public void setBakOpenNum(Long bakOpenNum){
         this.bakOpenNum=bakOpenNum;
    }
    public Date getBakCloseTime(){
         return bakCloseTime;
    }
    public void setBakCloseTime(Date bakCloseTime){
         this.bakCloseTime=bakCloseTime;
    }
    public Long getBakCloseNum(){
         return bakCloseNum;
    }
    public void setBakCloseNum(Long bakCloseNum){
         this.bakCloseNum=bakCloseNum;
    }
    public String getBakCloseRes(){
         return bakCloseRes;
    }
    public void setBakCloseRes(String bakCloseRes){
         this.bakCloseRes=bakCloseRes;
    }
    public String getBakUserId(){
         return bakUserId;
    }
    public void setBakUserId(String bakUserId){
         this.bakUserId=bakUserId;
    }
    public Date getBakUpdTime(){
         return bakUpdTime;
    }
    public void setBakUpdTime(Date bakUpdTime){
         this.bakUpdTime=bakUpdTime;
    }
    public String getRevFlag(){
         return revFlag;
    }
    public void setRevFlag(String revFlag){
         this.revFlag=revFlag;
    }
    public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "userId="+userId+"\n";
        temp = temp + "cardBank="+cardBank+"\n";
        temp = temp + "cardType="+cardType+"\n";
        temp = temp + "cardStatus="+cardStatus+"\n";
        temp = temp + "cardStaRes="+cardStaRes+"\n";
        temp = temp + "cardNo="+cardNo+"\n";
        temp = temp + "cardBankBranch="+cardBankBranch+"\n";
        temp = temp + "bakOpenTime="+bakOpenTime+"\n";
        temp = temp + "bakOpenNum="+bakOpenNum+"\n";
        temp = temp + "bakCloseTime="+bakCloseTime+"\n";
        temp = temp + "bakCloseNum="+bakCloseNum+"\n";
        temp = temp + "bakCloseRes="+bakCloseRes+"\n";
        temp = temp + "bakUserId="+bakUserId+"\n";
        temp = temp + "bakUpdTime="+bakUpdTime+"\n";
        temp = temp + "revFlag="+revFlag+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("userId",userId);
        json.put("cardBank",cardBank);
        json.put("cardType",cardType);
        json.put("cardStatus",cardStatus);
        json.put("cardStaRes",cardStaRes);
        json.put("cardNo",cardNo);
        json.put("cardBankBranch",cardBankBranch);
        try{
            json.put("bakOpenTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bakOpenTime));
        } catch (Exception e) {}
        json.put("bakOpenNum",String.valueOf(bakOpenNum));
        try{
            json.put("bakCloseTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bakCloseTime));
        } catch (Exception e) {}
        json.put("bakCloseNum",String.valueOf(bakCloseNum));
        json.put("bakCloseRes",bakCloseRes);
        json.put("bakUserId",bakUserId);
        try{
            json.put("bakUpdTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(bakUpdTime));
        } catch (Exception e) {}
        json.put("revFlag",revFlag);
        return json;
    }
}