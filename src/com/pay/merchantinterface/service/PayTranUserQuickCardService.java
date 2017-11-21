package com.pay.merchantinterface.service;

import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.jweb.service.TransactionService;
import com.pay.merchantinterface.dao.PayTranUserQuickCardDAO;
import com.pay.merchantinterface.dao.PayTranUserQuickCard;

/**
 * Object PAY_TRAN_USER_QUICK_CARD service. 
 * @author Administrator
 *
 */
public class PayTranUserQuickCardService extends TransactionService{
    /**
     * Get records list(json).
     * @return
     */
    public String getPayTranUserQuickCardList(PayTranUserQuickCard payTranUserQuickCard,int page,int rows,String sort,String order){
        try {
            PayTranUserQuickCardDAO payTranUserQuickCardDAO = new PayTranUserQuickCardDAO();
            List list = payTranUserQuickCardDAO.getPayTranUserQuickCardList(payTranUserQuickCard, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payTranUserQuickCardDAO.getPayTranUserQuickCardCount(payTranUserQuickCard)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayTranUserQuickCard)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayTranUserQuickCard
     * @param payTranUserQuickCard
     * @throws Exception
     */
    public void addPayTranUserQuickCard(PayRequest request) throws Exception {
    	PayTranUserQuickCardDAO cardDao = new PayTranUserQuickCardDAO();
    	//通过身份证取得用户信息，绑定卡号列表
    	PayTranUserQuickCard bindCard = new PayTranUserQuickCard();
    	try {
    		transaction.beignTransaction(cardDao);
        	if(request.credentialNo!=null&&request.cardNo!=null
        		&&request.credentialNo.length()!=0&&request.cardNo.length()!=0
        		&&!cardDao.exist(request.credentialNo, request.cardNo)){
        		bindCard.id=Tools.getUniqueIdentify();
        	    bindCard.credentialType="01";//01身份证
        	    bindCard.credentialNo=request.credentialNo;
        	    bindCard.cardNo=request.cardNo;
        	    bindCard.cvv2=request.cvv2;
        	    bindCard.validPeriod=request.validPeriod;
        	    bindCard.bankId=request.bankId;
        	    bindCard.status="0";
        	    bindCard.name=request.userName;
        	    bindCard.mobileNo=request.userMobileNo;
        	    bindCard.createTime=new Date();
        	    bindCard.merchantId=request.merchantId;
        	    bindCard.payerId=request.payerId;
        	    cardDao.addPayTranUserQuickCard(bindCard);
        	}
        	transaction.endTransaction();
    	} catch (Exception e) {
    		transaction.rollback();
    		throw e;
    	}
    }
    /**
     * remove PayTranUserQuickCard
     * @param id
     * @throws Exception
     */
    public void removePayTranUserQuickCard(String id) throws Exception {
        new PayTranUserQuickCardDAO().removePayTranUserQuickCard(id);
    }
    /**
     * update PayTranUserQuickCard
     * @param payTranUserQuickCard
     * @throws Exception
     */
    public void updatePayTranUserQuickCard(PayTranUserQuickCard payTranUserQuickCard) throws Exception {
        new PayTranUserQuickCardDAO().updatePayTranUserQuickCard(payTranUserQuickCard);
    }
    /**
     * update PayTranUserQuickCard
     * @param payTranUserQuickCard
     * @throws Exception
     */
    public void updatePayTranUserQuickCard(PayRequest request) throws Exception {
        new PayTranUserQuickCardDAO().updatePayTranUserQuickCard(request);
    }
    /**
     * detail PayTranUserQuickCard
     * @param id
     * @throws Exception
     */
    public PayTranUserQuickCard detailPayTranUserQuickCard(String id) throws Exception {
        return new PayTranUserQuickCardDAO().detailPayTranUserQuickCard(id);
    }
	public PayTranUserQuickCard getBindCardByNo(String cardNo) throws Exception {
		return new PayTranUserQuickCardDAO().getBindCardByNo(cardNo);
	}
}