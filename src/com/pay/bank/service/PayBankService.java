package com.pay.bank.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.pay.bank.dao.PayBank;
import com.pay.bank.dao.PayBankDAO;
import com.pay.user.dao.PayBusinessMember;
import com.pay.user.dao.PayBusinessMemberDAO;

/**
 * Object PAY_BANK service. 
 * @author Administrator
 *
 */
public class PayBankService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayBankList(PayBank payBank,int page,int rows,String sort,String order){
        try {
            PayBankDAO payBankDAO = new PayBankDAO();
            List list = payBankDAO.getPayBankList(payBank, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payBankDAO.getPayBankCount(payBank)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayBank)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayBank
     * @param payBank
     * @throws Exception
     */
    public void addPayBank(PayBank payBank) throws Exception {
    	payBank.setId(Tools.getUniqueIdentify());
        new PayBankDAO().addPayBank(payBank);
    }
    /**
     * remove PayBank
     * @param id
     * @throws Exception
     */
    public void removePayBank(String id) throws Exception {
        new PayBankDAO().removePayBank(id);
    }
    /**
     * update PayBank
     * @param payBank
     * @throws Exception
     */
    public void updatePayBank(PayBank payBank) throws Exception {
        new PayBankDAO().updatePayBank(payBank);
    }
    /**
     * detail PayBank
     * @param id
     * @throws Exception
     */
    public PayBank detailPayBank(String id) throws Exception {
        return new PayBankDAO().detailPayBank(id);
    }
    
    /**
     * 检查银行编码或者银行是否唯一
     * @param payBank
     * @param flag 
     * @return 返回判断结果
     * @throws Exception 
     */
	public void checkUniq(PayBank payBank) throws Exception {
		PayBank checkPayBankCode = new PayBankDAO().selectPayBank(payBank,"BANK_CODE");
		PayBank checkPayBankName = new PayBankDAO().selectPayBank(payBank,"BANK_NAME");
		if(null != checkPayBankCode && null != checkPayBankCode.getId()) throw new Exception("银行编码已存在");
		if(null != checkPayBankName && null != checkPayBankName.getId()) throw new Exception("银行名称已存在");
	}
	public void addPayBusiMem(PayBusinessMember payBusinessMember) throws Exception {
		new PayBusinessMemberDAO().addPayBusiMem(payBusinessMember);
	}
	public void validPayMentRelationUserId(String userId) throws Exception {
		new PayBusinessMemberDAO().validPayMentRelationUserId(userId);
	}
}