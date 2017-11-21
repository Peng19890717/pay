package com.pay.risk.service;

import java.util.Date;
import java.util.List;

import com.PayConstant;
import com.jweb.service.TransactionService;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayTransferAccOrder;
import com.pay.order.dao.PayWithdrawCashOrder;
import com.pay.refund.dao.PayRefund;
import com.pay.risk.dao.PayRiskDayTranSum;
import com.pay.risk.dao.PayRiskDayTranSumDAO;
import com.pay.user.dao.PayTranUserInfo;
import com.pay.user.dao.PayTranUserInfoDAO;

/**
 * 消费（充值）、退款、转账、提现汇总
 * Object PAY_RISK_DAY_TRAN_SUM service. 
 * @author Administrator
 *
 */
public class PayRiskDayTranSumService extends TransactionService{
    /**
     * 商户交易汇总（消费）
     * @param payRequest
     * @throws Exception 
     */
	public void updateRiskTranSum(PayRequest payRequest) throws Exception {
		PayRiskDayTranSum sum = new PayRiskDayTranSum();
		PayRiskDayTranSumDAO payRiskDayTranSumDAO = new PayRiskDayTranSumDAO();
		try {
			transaction.beignTransaction(payRiskDayTranSumDAO);
			sum.timeType="0";//0日 1月
			sum.custType=com.PayConstant.CUST_TYPE_MERCHANT;//客户类型 0用户，1商户
			sum.custId=payRequest.productOrder.merno;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(payRequest.productOrder.ordertime);//统计日期
			sum.allAmount=payRequest.productOrder.ordamt;//当日累计金额
			sum.saleAmount=payRequest.productOrder.ordamt;//当日消费累计金额
			sum.allCount=1l;//当日累计笔数
			sum.saleCount=1l;//当日消费笔数
			if(payRequest.productOrder.ordamt<1000)sum.lessCount=1l;//当日金额小于10元笔数
			else sum.lessCount=0l;
			if(payRequest.productOrder.ordamt%100==0)sum.intCount=1l;//当日交易金额为整数的笔数
			else sum.intCount=0l;
			payRiskDayTranSumDAO.updateOrAddPayRiskTranSumForSale(sum);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	/**
     * 用户交易汇总（消费（账户、网银、快捷（在卡限额时处理））/充值）
     * @param payRequest
     * @throws Exception 
     */
	public void updateRiskTranSumForUser(PayRequest payRequest) throws Exception {
		PayTranUserInfo user = payRequest.tranUserMap.get(payRequest.payerId);
		if(user==null)return;
		PayRiskDayTranSum sum = new PayRiskDayTranSum();
		PayRiskDayTranSumDAO payRiskDayTranSumDAO = new PayRiskDayTranSumDAO();
		try {
			transaction.beignTransaction(payRiskDayTranSumDAO);
			sum.timeType="0";//0日 1月
			sum.custType=com.PayConstant.CUST_TYPE_USER;//客户类型 0用户，1商户
			sum.custId=user.userId;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(payRequest.productOrder.ordertime);//统计日期
			sum.allAmount=payRequest.productOrder.ordamt;//当日累计金额
			sum.allCount=1l;//当日累计笔数
			if(payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))){
				sum.rechargeAmount=payRequest.productOrder.ordamt;
				sum.rechargeCount=1l;
			} else {
				//支付方式，00 支付账户  01 网上银行 03 快捷支付
				//账户
				if("00".equals(payRequest.payOrder.paytype)){
					sum.saleAccAmt=payRequest.payOrder.txamt;
					sum.saleAccCount = 1l;
				//网银
				} else if("01".equals(payRequest.payOrder.paytype)){
					sum.saleAmount=payRequest.payOrder.txamt;
					sum.saleCount=1l;
				//终端 TODO
				} else if("02".equals(payRequest.payOrder.paytype)){
//				//快捷
//				} else if("03".equals(payRequest.payOrder.paytype)){
//					sum.saleQuickAmt=payRequest.payOrder.txamt;
//					sum.saleQuickCount=1l;
				}
				sum.rechargeAmount=0l;
			}
			if(payRequest.productOrder.ordamt<1000)sum.lessCount=1l;//当日金额小于10元笔数
			else sum.lessCount=0l;
			if(payRequest.productOrder.ordamt%100==0)sum.intCount=1l;//当日交易金额为整数的笔数
			else sum.intCount=0l;
			payRiskDayTranSumDAO.updateOrAddPayRiskTranSumForSale(sum);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
    /**
     * 用户交易汇总（快捷）
     * @param payRequest
     * @throws Exception 
     */
	public void updateRiskTranSumForQuick(PayRequest payRequest) throws Exception {
		PayRiskDayTranSum sum = new PayRiskDayTranSum();
		PayRiskDayTranSumDAO payRiskDayTranSumDAO = new PayRiskDayTranSumDAO();
		try {
			transaction.beignTransaction(payRiskDayTranSumDAO);
			sum.timeType="0";//0日 1月
			sum.custType=com.PayConstant.CUST_TYPE_PAY_CARD;//客户类型 0用户，1商户
			sum.custId=payRequest.cardNo;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(payRequest.productOrder.ordertime);//统计日期
			sum.allAmount=payRequest.productOrder.ordamt;//当日累计金额
			sum.allCount=1l;//当日累计笔数
			sum.saleQuickAmt=payRequest.productOrder.ordamt;//当日消费累计金额
			sum.saleQuickCount=1l;//当日消费笔数
			if(payRequest.productOrder.ordamt<1000)sum.lessCount=1l;//当日金额小于10元笔数
			else sum.lessCount=0l;
			if(payRequest.productOrder.ordamt%100==0)sum.intCount=1l;//当日交易金额为整数的笔数
			else sum.intCount=0l;
			payRiskDayTranSumDAO.updateOrAddPayRiskTranSumForSale(sum);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
    /**
     * 商户成功交易汇总（消费）
     * @param prdOrder
     * @param order
     * @throws Exception 
     */
	public void updateRiskTranSumForSuccess(PayProductOrder prdOrder, PayOrder order) throws Exception {
		PayRiskDayTranSum sum = new PayRiskDayTranSum();
		PayRiskDayTranSumDAO payRiskDayTranSumDAO = new PayRiskDayTranSumDAO();
		try {
			transaction.beignTransaction(payRiskDayTranSumDAO);
			//商户更新
			sum.timeType="0";//0日 1月
			sum.custType=com.PayConstant.CUST_TYPE_MERCHANT;//客户类型 0用户，1商户
			sum.custId=prdOrder.merno;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(prdOrder.ordertime);//统计日期
			sum.allSuccessAmount=prdOrder.ordamt;//当日累计金额
			sum.saleSuccessAmount=prdOrder.ordamt;//当日消费累计金额
			sum.rechargeSuccessAmount=0l;
			sum.allSuccessCount=1l;//当日累计笔数
			sum.saleSuccessCount=1l;//当日消费笔数
			if(prdOrder.ordamt<1000)sum.lessSuccessCount=1l;//当日金额小于10元笔数
			else sum.lessSuccessCount=0l;
			if(prdOrder.ordamt%100==0)sum.intSuccessCount=1l;//当日交易金额为整数的笔数
			else sum.intSuccessCount=0l;
			payRiskDayTranSumDAO.updateOrAddPayRiskTranSumForSuccessSale(sum);
			PayTranUserInfo user = new PayTranUserInfoDAO().selectSingleUser(prdOrder.custId);
			if(user==null){
				transaction.endTransaction();
				return;
			}
			//用户更新
			sum = new PayRiskDayTranSum();
			sum.timeType="0";//0日 1月
			sum.custType=com.PayConstant.CUST_TYPE_USER;//客户类型 0用户，1商户
			sum.custId=prdOrder.custId;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(prdOrder.ordertime);//统计日期
			sum.allSuccessAmount=prdOrder.ordamt;//当日累计金额
			sum.allSuccessCount=1l;//当日累计笔数
			if(prdOrder.ordamt<1000)sum.lessSuccessCount=1l;//当日金额小于10元笔数
			else sum.lessSuccessCount=0l;
			if(prdOrder.ordamt%100==0)sum.intSuccessCount=1l;//当日交易金额为整数的笔数
			else sum.intSuccessCount=0l;
			//充值记录
			if("1".equals(prdOrder.prdordtype)){
				sum.rechargeSuccessAmount=prdOrder.ordamt;
				sum.rechargeSuccessCount=1l;
			} else {
				//支付方式，00 支付账户  01 网上银行 03 快捷支付
				//账户
				if("00".equals(order.paytype)){
					sum.saleAccSuccessAmt=order.txamt;
					sum.saleAccSuccessCount = 1l;
				//网银
				} else if("01".equals(order.paytype)){
					sum.saleSuccessAmount=prdOrder.ordamt;//当日消费累计金额
					sum.saleSuccessCount=1l;//当日消费笔数
				//终端 TODO
				} else if("02".equals(order.paytype)){
				//快捷
				} else if("03".equals(order.paytype)){
					sum.custType=com.PayConstant.CUST_TYPE_PAY_CARD;
					sum.custId=order.payacno;
					sum.saleQuickSuccessAmt=order.txamt;
					sum.saleQuickSuccessCount=1l;
					sum.cardLastTranTime = new Date();
				}
				sum.rechargeSuccessAmount=0l;
				sum.rechargeSuccessCount=0l;
			}
			payRiskDayTranSumDAO.updateOrAddPayRiskTranSumForSuccessSale(sum);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	/**
	 * 交易汇总（退款）
	 * @param payRefund
	 * @throws Exception 
	 */
	public void updateRiskRefundSum(PayRefund payRefund) throws Exception {
		PayRiskDayTranSumDAO riskDayTranSumDAO = new PayRiskDayTranSumDAO();
		try {
			transaction.beignTransaction(riskDayTranSumDAO);
			PayRiskDayTranSum sum = new PayRiskDayTranSum();
			sum.timeType="0";//0日 1月
			sum.custType=com.PayConstant.CUST_TYPE_MERCHANT;//客户类型 0用户，1商户
			sum.custId=payRefund.merno;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(payRefund.rfordtime);//统计日期
			sum.allAmount=payRefund.rfamt;//当日累计金额
			sum.refundAmount=payRefund.rfamt;//当日消费累计金额
			sum.allCount=1l;//当日累计笔数
			sum.refundCount=1l;//当日消费笔数
			if(payRefund.rfamt<1000)sum.lessCount=1l;//当日金额小于10元笔数
			else sum.lessCount=0l;
			if(payRefund.rfamt%100==0)sum.intCount=1l;//当日交易金额为整数的笔数
			else sum.intCount=0l;
			riskDayTranSumDAO.updateOrAddPayRiskTranSumForRefund(sum);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	/**
	 * 成功交易汇总（退款）
	 * @param payRefund
	 * @throws Exception 
	 */
	public void updateRiskSuccessRefundSum(PayRefund payRefund) throws Exception {
		PayRiskDayTranSumDAO payRiskDayTranSumDAO = new PayRiskDayTranSumDAO();
		try {
			transaction.beignTransaction(payRiskDayTranSumDAO);
			PayRiskDayTranSum sum = new PayRiskDayTranSum();
			sum.timeType="0";//0日 1月
			sum.custType=com.PayConstant.CUST_TYPE_MERCHANT;//客户类型 0用户，1商户
			sum.custId=payRefund.merno;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(payRefund.rfordtime);//统计日期
			sum.allSuccessAmount=payRefund.rfamt;//当日累计金额
			sum.refundSuccessAmount=payRefund.rfamt;//当日消费累计金额
			sum.allSuccessCount=1l;//当日累计笔数
			sum.refundSuccessCount=1l;//当日消费笔数
			if(payRefund.rfamt<1000)sum.lessSuccessCount=1l;//当日金额小于10元笔数
			else sum.lessSuccessCount=0l;
			if(payRefund.rfamt%100==0)sum.intSuccessCount=1l;//当日交易金额为整数的笔数
			else sum.intSuccessCount=0l;
			payRiskDayTranSumDAO.updateOrAddPayRiskTranSumForSuccessRefund(sum);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	/**
	 * 转账汇总
	 * @param custType
	 * @param custId
	 * @param tsList
	 * @throws Exception
	 */
	public void updateRiskTransferSum(String custType,String custId,List <PayTransferAccOrder>tsList) throws Exception {
		PayRiskDayTranSumDAO payRiskDayTranSumDAO = new PayRiskDayTranSumDAO();
		try {
			transaction.beignTransaction(payRiskDayTranSumDAO);
			PayRiskDayTranSum sum = new PayRiskDayTranSum();
			sum.timeType="0";//0日 1月
			sum.custType=custType;//客户类型 0用户，1商户
			sum.custId=custId;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());//统计日期
			sum.allAmount = 0l;//累计金额
			sum.allSuccessAmount= 0l;//当日成功累计金额
			sum.transferAmount=0l;//当日转账累计金额
			sum.transferSuccessAmount=0l;//当日转账成功累计金额
			sum.allCount = (long) tsList.size();//累计笔数
			sum.allSuccessCount = 0l;//累计成功笔数
			sum.transferCount=0l;//当日转账累计金额
			sum.transferSuccessCount=0l;//当日转账成功累计笔数
			sum.lessCount=0l;//当日金额小于10元笔数
			sum.lessSuccessCount=0l;//当日成功金额小于10元笔数
			sum.intCount=0l;//当日交易金额为整数的笔数
			sum.intSuccessCount=0l;//当日成功交易金额为整数的笔数
			for(int i = 0; i<tsList.size(); i++){
				PayTransferAccOrder tsOrder = tsList.get(i);
				sum.allAmount += tsOrder.txamt;
				sum.transferAmount += tsOrder.txamt;
				sum.transferCount++;
				//成功转账
            	if(tsOrder.status==null || tsOrder.status.length() ==0){
            		sum.allSuccessAmount += tsOrder.txamt;
            		sum.transferSuccessAmount += tsOrder.txamt;
            		sum.allSuccessCount ++;
            		sum.transferSuccessCount++;
            		if(tsOrder.txamt<1000)sum.lessSuccessCount++;//当日金额小于10元笔数
                	if(tsOrder.txamt%100==0)sum.intSuccessCount++;//当日交易金额为整数的笔数
            	}
            	if(tsOrder.txamt<1000)sum.lessCount++;//当日金额小于10元笔数
            	if(tsOrder.txamt%100==0)sum.intCount++;//当日交易金额为整数的笔数
            }
			payRiskDayTranSumDAO.updateOrAddPayRiskTranSumForTransfer(sum);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	/**
	 * 提现汇总
	 * @param custType
	 * @param custId
	 * @param payWithdrawCashOrder
	 * @throws Exception
	 */
	public void updateWithdrawSum(PayWithdrawCashOrder pwcOrder) throws Exception {
		PayRiskDayTranSumDAO payRiskDayTranSumDAO = new PayRiskDayTranSumDAO();
		try {
			transaction.beignTransaction(payRiskDayTranSumDAO);
			PayRiskDayTranSum sum = new PayRiskDayTranSum();
			sum.timeType="0";//0日 1月
			sum.custType=pwcOrder.custType;//客户类型 0用户，1商户
			sum.custId=pwcOrder.custId;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(pwcOrder.actTime);//统计日期
			sum.allAmount = pwcOrder.txamt;//累计金额
			sum.cashAmount=pwcOrder.txamt;//当日提现累计金额
			sum.allCount=1l;//当日累计笔数
			sum.cashCount=1l;//当日消费笔数
			if(pwcOrder.txamt<1000)sum.lessCount=1l;//当日金额小于10元笔数
			else sum.lessCount=0l;
			if(pwcOrder.txamt%100==0)sum.intCount=1l;//当日交易金额为整数的笔数
			else sum.intCount=0l;
			payRiskDayTranSumDAO.updateOrAddPayRiskTranSumForWithdraw(sum);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	/**
	 * 成功提现汇总
	 * @param pwcOrder
	 * @throws Exception 
	 */
	public void updateRiskSuccessRefundSum(PayWithdrawCashOrder pwcOrder) throws Exception {
		PayRiskDayTranSumDAO payRiskDayTranSumDAO = new PayRiskDayTranSumDAO();
		try {
			transaction.beignTransaction(payRiskDayTranSumDAO);
			PayRiskDayTranSum sum = new PayRiskDayTranSum();
			sum.timeType="0";//0日 1月
			sum.custType=pwcOrder.custType;//客户类型 0用户，1商户
			sum.custId=pwcOrder.custId;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(pwcOrder.actTime);//统计日期
			sum.allSuccessAmount=pwcOrder.txamt;//当日累计金额
			sum.cashSuccessAmount=pwcOrder.txamt;//当日成功提现累计金额
			sum.allSuccessCount=1l;//当日累计笔数
			sum.cashSuccessCount=1l;//当日成功提现笔数
			if(pwcOrder.txamt<1000)sum.lessSuccessCount=1l;//当日金额小于10元笔数
			else sum.lessSuccessCount=0l;
			if(pwcOrder.txamt%100==0)sum.intSuccessCount=1l;//当日交易金额为整数的笔数
			else sum.intSuccessCount=0l;
			payRiskDayTranSumDAO.updateOrAddPayRiskTranSumForSuccessWithdraw(sum);
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
    /**
     * 代收付汇总
     * @param payRequest
     * @throws Exception 
     */
	public void updateRiskReceivePaySum(PayRequest payRequest,List<PayReceiveAndPay> list) throws Exception {
		PayRiskDayTranSum sum = new PayRiskDayTranSum();
		try {
			long totalAmt=0,count=0;
			for(int i = 0; i<list.size(); i++){
				PayReceiveAndPay rp = list.get(i);
				if(!"000".equals(rp.respCode))continue;
				count++;
				totalAmt = totalAmt + rp.amount;
			}
			sum.timeType="0";//0日 1月
			sum.custType=com.PayConstant.CUST_TYPE_MERCHANT;//客户类型 0用户，1商户
			sum.custId=payRequest.merchantId;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());//统计日期
			sum.allAmount=totalAmt;//当日累计金额
			sum.allCount=count;//当日累计笔数
			//代收
			if("0".equals(payRequest.receivePayType)){
				sum.receiveAmt=totalAmt;//当日代收累计金额
				sum.receiveCount=count;//当日代收累计数
				sum.payAmt=0l;//当日代付累计金额
				sum.payCount=0l;//当日代付累计数
			} else {//代付
				sum.receiveAmt=0l;//当日代收累计金额
				sum.receiveCount=0l;//当日代收累计数
				sum.payAmt=totalAmt;//当日代付累计金额
				sum.payCount=count;//当日代付累计数
			}
			new PayRiskDayTranSumDAO().updateOrAddPayRiskReceivePaySum(sum);
		} catch (Exception e) {
			throw e;
		}
	}
    /**
     * 代收付成功汇总
     * @param payRequest
     * @throws Exception 
     */
	public void updateRiskReceivePaySumForSuccess(PayRequest payRequest,List<PayReceiveAndPay> list) throws Exception {
		PayRiskDayTranSum sum = new PayRiskDayTranSum();
		try {
			long totalAmt = 0,count=0;
			for(int i = 0; i<list.size(); i++){
				PayReceiveAndPay rp = list.get(i);
				if("0".equals(payRequest.receivePayType)){//收款
					if(!"000".equals(rp.respCode))continue;//收款失败
				} else if("1".equals(payRequest.receivePayType)){//付款
					if(!"000".equals(rp.respCode))continue;
				}
				totalAmt = totalAmt + rp.amount;
				count++;
			}
			sum.timeType="0";//0日 1月
			sum.custType=com.PayConstant.CUST_TYPE_MERCHANT;//客户类型 0用户，1商户
			sum.custId=payRequest.merchantId;//客户编号
			sum.statDate=new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());//统计日期
			sum.allSuccessAmount=totalAmt;//当日累计金额
			sum.allSuccessCount=count;//当日累计笔数
			//代收
			if("0".equals(payRequest.receivePayType)){
				sum.receiveSuccessAmt=totalAmt;//当日代收累计金额
				sum.receiveSuccessCount=count;//当日代收累计数
				sum.paySuccessAmt=0l;//当日代付累计金额
				sum.paySuccessCount=0l;//当日代付累计数
			} else {//代付
				sum.receiveSuccessAmt=0l;//当日代收累计金额
				sum.receiveSuccessCount=0l;//当日代收累计数
				sum.paySuccessAmt=totalAmt;//当日代付累计金额
				sum.paySuccessCount=count;//当日代付累计数
			}
			new PayRiskDayTranSumDAO().updateOrAddPayRiskReceivePaySum(sum);
		} catch (Exception e) {
			throw e;
		}
	}
}