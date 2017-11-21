package com.pay.account.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

import org.json.JSONArray;
import org.json.JSONObject;

import com.PayConstant;
import com.pay.account.dao.PayBankAccountCheck;
import com.pay.account.dao.PayBankAccountCheckDAO;
import com.pay.account.dao.PayBankAccountCheckTmp;
import com.pay.account.dao.PayBankAccountSum;

/**
 * Object PAY_BANK_ACCOUNT_CHECK service. 
 * @author Administrator
 *
 */
public class PayBankAccountCheckService {
    /**
     * Get records list(json).
     * @return
     */
    public String getPayBankAccountCheckList(PayBankAccountCheck payBankAccountCheck,int page,int rows,String sort,String order){
        try {
            PayBankAccountCheckDAO payBankAccountCheckDAO = new PayBankAccountCheckDAO();
            List list = payBankAccountCheckDAO.getPayBankAccountCheckList(payBankAccountCheck, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payBankAccountCheckDAO.getPayBankAccountCheckCount(payBankAccountCheck)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayBankAccountCheck)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 取得汇总列表(json).
     * @return
     */
    public String payBankAccountSumList(PayBankAccountSum payBankAccountSum,int page,int rows,String sort,String order){
        try {
            PayBankAccountCheckDAO payBankAccountCheckDAO = new PayBankAccountCheckDAO();
            List list = payBankAccountCheckDAO.getPayBankAccountSumList(payBankAccountSum, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payBankAccountCheckDAO.getPayBankAccountSumCount(payBankAccountSum)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayBankAccountSum)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public void updatePayBankAccountCheck(PayBankAccountCheck payBankAccountCheck) throws Exception {
        new PayBankAccountCheckDAO().updatePayBankAccountCheck(payBankAccountCheck);
    }
	public void setResultBankAccount(PayBankAccountCheck payBankAccountCheck) throws Exception {
		new PayBankAccountCheckDAO().setResultBankAccount(payBankAccountCheck);
	}
	/**
	 * 交易列表中获取交易数据保存到对账列表中
	 * @param transDate
	 * @param payChannel
	 * @throws Exception 
	 */
	public void createAccountData(String transDate,String payChannel) throws Exception {
		new PayBankAccountCheckDAO().createAccountData(transDate,payChannel);
	}
	/**
	 * 根据渠道（支付、退款），【对账临时表】和【对账表】对账
	 * @param payChannel
	 * @throws Exception 
	 */
	public void checkAccount(String payChannel) throws Exception{
		//支付订单对账
		new PayBankAccountCheckDAO().checkAccount();
		//退款对账 TODO
	}
}