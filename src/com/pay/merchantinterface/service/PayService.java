package com.pay.merchantinterface.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jweb.service.TransactionService;
import com.pay.merchantinterface.dao.PayTranUserQuickCardDAO;
import com.pay.order.dao.PayAccProfileDAO;

/**
 * 支付服务类
 * @author Administrator
 *
 */
public class PayService extends TransactionService{
	/**
	 * 账户余额支付
	 * @param payRequest
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String pay(PayRequest payRequest,HttpServletRequest request) throws Exception {
		PayAccProfileDAO profileDAO = new PayAccProfileDAO();
		NotifyInterface notifyInterface= new NotifyInterface();
		try {
			transaction.beignTransaction(profileDAO,notifyInterface);
			//用户出款，扣除买家账面余额、现金余额
			//公司虚拟账户入账
			//修改订单虚拟支付金额，订单支付方式
			if(profileDAO.deductUserBalance(payRequest)>0){ 
				//修改订单状态、买卖双方ID，发通知
				payRequest.payOrder.actdat = new Date();
				payRequest.payOrder.ordstatus = "01";
				notifyInterface.notifyMer(payRequest.payOrder);
			} else throw new Exception("账户余额不足");
			transaction.endTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
			return e.getMessage()==null?"空指针":e.getMessage();
		}
		return "";
	}
	public List getPayTranUserQuickCardByPayerId(String merNo,String payerId) throws Exception{
		return new PayTranUserQuickCardDAO().getPayTranUserQuickCardByPayerId(merNo,payerId); 
	}
}
