package com.pay.risk.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.PayConstant;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayTransferAccOrder;
import com.pay.order.dao.PayWithdrawCashOrder;
import com.pay.risk.dao.PayRiskDayTranSum;
import com.pay.risk.dao.PayRiskDayTranSumDAO;
import com.pay.risk.dao.PayRiskMerchantLimit;
import com.pay.risk.dao.PayRiskUserLimit;
import com.pay.risk.dao.PayRiskUserLimitSub;
import com.pay.risk.dao.PayRiskXListDAO;
import com.pay.user.dao.PayTranUserInfo;
import com.pay.user.dao.PayTranUserInfoDAO;

/**
 * 风控服务类
 * @author Administrator
 *
 */
public class RiskService {
	/**
	 * 交易风控检查
	 * @param riskObject 风控对象
	 * @param limitType 限额类型:1消费 2充值 3结算 4退款 5提现 6转账 默认1
	 * @return
	 */
//	public String checkTranRisk(Object riskObject,String limitType){
//		if("1".equals(limitType)||"4".equals(limitType)){
//			//用户黑名单检查， PAY_RISK_X_LIST->CLIENT_TYPE 客户类型，0用户，1商户
//			//if(checkClientIsBlackList("0",payRequest.custId)!=null)return "黑名单用户";
//			//商户黑名单检查
//			if(checkClientIsBlackList(PayConstant.CUST_TYPE_MERCHANT,((PayRequest)riskObject).merchantId))return "黑名单商户";
//		}
//		//用户限额检查
////		String msg = checkUserLimit(riskObject,limitType);
////		if(msg != null && msg.length() >0)return msg;
//		//商户限额检查
//		return checkMerchantLimit(riskObject,limitType);
//	}
	/**
	 * 商户限额检查，包括天、月的限额、限次(1消费、4退款、5提现、6转账)
	 * @param riskObject 风控对象
	 * @param limitType 限额类型:1消费 2充值 3结算 4退款 5提现 6转账 默认1
	 * @return
	 */
	public String checkMerchantLimit(Object riskObject,String merchantId,String limitType){
		if(checkClientIsBlackList(PayConstant.CUST_TYPE_MERCHANT,merchantId))return "黑名单商户";
		//消费/退款限额处理
		if(PayConstant.TRAN_TYPE_CONSUME.equals(limitType)||PayConstant.TRAN_TYPE_REFUND.equals(limitType)){
			PayRequest payRequest = (PayRequest)riskObject;
			//取得限额信息
			PayRiskMerchantLimit lim = PayRiskMerchantLimitService.getMerchantLimit(payRequest.merchantId,limitType);
			if(lim==null) return null;
			String startDate = "";
			String endDate = "";
			try {
				if("1".equals(lim.limitTimeFlag)){//限额时间标识 0永久，1限制时间 默认0
					startDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitStartDate);
					endDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitEndDate);
				}
			} catch (Exception e) {}
			//取得天、月的交易额、次
			String tranDate = new java.text.SimpleDateFormat("yyyyMMdd").format(payRequest.curTime);
			List sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_MERCHANT,payRequest.merchantId,tranDate);
			//消费限额
			if(PayConstant.TRAN_TYPE_CONSUME.equals(limitType)){
				long daySaleAmt = 0,monSaleAmt=0,daySaleCount=0,monSaleCount=0,
						daySaleSuccessAmt = 0,monSaleSuccessAmt=0,daySaleSuccessCount=0,monSaleSuccessCount=0;
				for(int i=0; i<sumList.size(); i++){
					PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
					if("0".equals(sum.timeType)){//时间类型 0日 1月
						daySaleAmt = sum.saleAmount;
						daySaleCount = sum.saleCount;
						daySaleSuccessAmt = sum.saleSuccessAmount;
						daySaleSuccessCount = sum.saleSuccessCount;
					} else if("1".equals(sum.timeType)){
						monSaleAmt = sum.saleAmount;
						monSaleCount = sum.saleCount;
						monSaleSuccessAmt = sum.saleSuccessAmount;
						monSaleSuccessCount = sum.saleSuccessCount;
					}
				}
				//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
				if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
						tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
					if(payRequest.tranAmt<lim.limitMinAmt||payRequest.tranAmt>lim.limitMaxAmt){
						return "消费额范围为"+String.format("%.2f",(float)lim.limitMinAmt*0.01)
								+"~"+String.format("%.2f",(float)lim.limitMaxAmt*0.01)+"元";
					}
					if(daySaleAmt+payRequest.tranAmt>lim.limitDayAmt)
						return "日消费金额超限"+String.format("%.2f",(float)lim.limitDayAmt*0.01)+"元";
					if(monSaleAmt+payRequest.tranAmt>lim.limitMonthAmt)
						return "月消费金额超限"+String.format("%.2f",(float)lim.limitMonthAmt*0.01)+"元";
					if(daySaleCount+1>lim.limitDayTimes)
						return "日消费次数超限"+lim.limitDayTimes+"次";
					if(monSaleCount+1>lim.limitMonthTimes)
						return "月消费次数超限"+lim.limitMonthTimes+"次";
					if(daySaleSuccessAmt+payRequest.tranAmt>lim.limitDaySuccessAmt)
						return "日成功消费金额超限"+String.format("%.2f",(float)lim.limitDaySuccessAmt*0.01)+"元";
					if(monSaleSuccessAmt+payRequest.tranAmt>lim.limitMonthSuccessAmt)
						return "月成功消费金额超限"+String.format("%.2f",(float)lim.limitMonthSuccessAmt*0.01)+"元";
					if(daySaleSuccessCount+1>lim.limitDaySuccessTimes)
						return "日成功消费次数超限"+lim.limitDaySuccessTimes+"次";
					if(monSaleSuccessCount+1>lim.limitMonthSuccessTimes)
						return "月成功消费次数超限"+lim.limitMonthSuccessTimes+"次";
				}
			} else {//退款限额
				long dayRefundAmt = 0,monRefundAmt=0,dayRefundCount=0,monRefundCount=0,
						dayRefundSuccessAmt = 0,monRefundSuccessAmt=0,dayRefundSuccessCount=0,monRefundSuccessCount=0;
				for(int i=0; i<sumList.size(); i++){
					PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
					if("0".equals(sum.timeType)){//时间类型 0日 1月
						dayRefundAmt = sum.refundAmount;
						dayRefundCount = sum.refundCount;
						dayRefundSuccessAmt = sum.refundSuccessAmount;
						dayRefundSuccessCount = sum.refundSuccessCount;
					} else if("1".equals(sum.timeType)){
						monRefundAmt = sum.refundAmount;
						monRefundCount = sum.refundCount;
						monRefundSuccessAmt = sum.refundSuccessAmount;
						monRefundSuccessCount = sum.refundSuccessCount;
					}
				}
				//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
				if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
						tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
					if(payRequest.tranAmt<lim.limitMinAmt||payRequest.tranAmt>lim.limitMaxAmt){
						return "退款额范围为"+String.format("%.2f",(float)lim.limitMinAmt*0.01)
								+"~"+String.format("%.2f",(float)lim.limitMaxAmt*0.01)+"元";
					}
					if(dayRefundAmt+payRequest.tranAmt>lim.limitDayAmt)
						return "日退款金额超限"+String.format("%.2f",(float)lim.limitDayAmt*0.01)+"元";
					if(monRefundAmt+payRequest.tranAmt>lim.limitMonthAmt)
						return "月退款金额超限"+String.format("%.2f",(float)lim.limitMonthAmt*0.01)+"元";
					if(dayRefundCount+1>lim.limitDayTimes)
						return "日退款次数超限"+lim.limitDayTimes+"次";
					if(monRefundCount+1>lim.limitMonthTimes)
						return "月退款次数超限"+lim.limitMonthTimes+"次";
					if(dayRefundSuccessAmt+payRequest.tranAmt>lim.limitDaySuccessAmt)
						return "日成功退款金额超限"+String.format("%.2f",(float)lim.limitDaySuccessAmt*0.01)+"元";
					if(monRefundSuccessAmt+payRequest.tranAmt>lim.limitMonthSuccessAmt)
						return "月成功退款金额超限"+String.format("%.2f",(float)lim.limitMonthSuccessAmt*0.01)+"元";
					if(dayRefundSuccessCount+1>lim.limitDaySuccessTimes)
						return "日成功退款次数超限"+lim.limitDaySuccessTimes+"次";
					if(monRefundSuccessCount+1>lim.limitMonthSuccessTimes)
						return "月成功退款次数超限"+lim.limitMonthSuccessTimes+"次";
				}
			}
		} else if(PayConstant.TRAN_TYPE_WITHDRAW.equals(limitType)){//提现限额
			PayWithdrawCashOrder pwcOrder = (PayWithdrawCashOrder)riskObject;
			PayRiskMerchantLimit lim = PayRiskMerchantLimitService.getMerchantLimit(pwcOrder.custId,limitType);
			if(lim==null) return null;
			String startDate = "";
			String endDate = "";
			try {
				if("1".equals(lim.limitTimeFlag)){//限额时间标识 0永久，1限制时间 默认0
					startDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitStartDate);
					endDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitEndDate);
				}
			} catch (Exception e) {}
			//取得天、月的交易额、次
			String tranDate = new java.text.SimpleDateFormat("yyyyMMdd").format(pwcOrder.actTime);
			List sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_MERCHANT,pwcOrder.custId,tranDate);
			long dayWithdrawAmt = 0,monWithdrawAmt=0,dayWithdrawSuccessAmt = 0,monWithdrawSuccessAmt=0,
					dayWithdrawCount = 0,monWithdrawCount=0,dayWithdrawSuccessCount = 0,monWithdrawSuccessCount=0;
			for(int i=0; i<sumList.size(); i++){
				PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
				if("0".equals(sum.timeType)){//时间类型 0日 1月
					dayWithdrawAmt = sum.cashAmount;
					dayWithdrawCount = sum.cashCount;
					dayWithdrawSuccessAmt = sum.cashSuccessAmount;
					dayWithdrawSuccessCount = sum.cashSuccessCount;
				} else if("1".equals(sum.timeType)){
					monWithdrawAmt = sum.cashAmount;
					monWithdrawCount = sum.cashCount;
					monWithdrawSuccessAmt = sum.cashSuccessAmount;
					monWithdrawSuccessCount = sum.cashSuccessCount;
				}
			}
			//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
			if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
					tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
				if(pwcOrder.txamt<lim.limitMinAmt){
					return "单笔提现额度最低为"+String.format("%.2f",(float)lim.limitMinAmt*0.01)+"元";
				}
				if(pwcOrder.txamt>lim.limitMaxAmt){
					return "单笔提现额度最高为" +String.format("%.2f",(float)lim.limitMaxAmt*0.01)+"元";
				}
				if(dayWithdrawAmt+pwcOrder.txamt>lim.limitDayAmt)
					return "日提现金额超限"+String.format("%.2f",(float)lim.limitDayAmt*0.01)+"元";
				if(monWithdrawAmt+pwcOrder.txamt>lim.limitMonthAmt)
					return "月提现金额超限"+String.format("%.2f",(float)lim.limitMonthAmt*0.01)+"元";
				if(dayWithdrawCount+1>lim.limitDayTimes)
					return "日提现次数超限"+lim.limitDayTimes+"次";
				if(monWithdrawCount+1>lim.limitMonthTimes)
					return "月提现次数超限"+lim.limitMonthTimes+"次";
				if(dayWithdrawSuccessAmt+pwcOrder.txamt>lim.limitDaySuccessAmt)
					return "日成功提现金额超限"+String.format("%.2f",(float)lim.limitDaySuccessAmt*0.01)+"元";
				if(monWithdrawSuccessAmt+pwcOrder.txamt>lim.limitMonthSuccessAmt)
					return "月成功提现金额超限"+String.format("%.2f",(float)lim.limitMonthSuccessAmt*0.01)+"元";
				if(dayWithdrawSuccessCount+1>lim.limitDaySuccessTimes)
					return "日成功提现次数超限"+lim.limitDaySuccessTimes+"次";
				if(monWithdrawSuccessCount+1>lim.limitMonthSuccessTimes)
					return "月成功提现次数超限"+lim.limitMonthSuccessTimes+"次";
			}
		} else if(PayConstant.TRAN_TYPE_TRANSFER.equals(limitType)){//转账限额
			Object [] obj = (Object [])riskObject;
			String custType = (String) obj[0];
			String custId = (String) obj[1];
			Date curTime = (Date) obj[3];
			List<PayTransferAccOrder>tsList = (List<PayTransferAccOrder>)obj[2];
			PayRiskMerchantLimit lim = PayRiskMerchantLimitService.getMerchantLimit(custId,limitType);
			if(lim==null) return null;
			String startDate = "";
			String endDate = "";
			try {
				if("1".equals(lim.limitTimeFlag)){//限额时间标识 0永久，1限制时间 默认0
					startDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitStartDate);
					endDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitEndDate);
				}
			} catch (Exception e) {}
			//取得天、月的交易额、次
			String tranDate = new java.text.SimpleDateFormat("yyyyMMdd").format(curTime);
			List sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_MERCHANT,custId,tranDate);
			long dayTransferAmt = 0,monTransferAmt=0,dayTransferSuccessAmt = 0,monTransferSuccessAmt=0,
					dayTransferCount = 0,monTransferCount=0,dayTransferSuccessCount = 0,monTransferSuccessCount=0;
			for(int i=0; i<sumList.size(); i++){
				PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
				if("0".equals(sum.timeType)){//时间类型 0日 1月
					dayTransferAmt = sum.transferAmount;
					dayTransferCount = sum.transferCount;
					dayTransferSuccessAmt = sum.transferSuccessAmount;
					dayTransferSuccessCount = sum.transferSuccessCount;
				} else if("1".equals(sum.timeType)){
					monTransferAmt = sum.transferAmount;
					monTransferCount = sum.transferCount;
					monTransferSuccessAmt = sum.transferSuccessAmount;
					monTransferSuccessCount = sum.transferSuccessCount;
				}
			}
			//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
			if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
					tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
				long transferAmount=0l;//转账累计金额
				long transferSuccessAmount=0l;//转账成功累计金额
				long transferCount=0l;//转账累计次数
				long transferSuccessCount=0l;//转账成功累计次数
				for(int i = 0; i<tsList.size(); i++){
					PayTransferAccOrder tsOrder = tsList.get(i);
					if(tsOrder.txamt<lim.limitMinAmt||tsOrder.txamt>lim.limitMaxAmt){
						return "转账金额超限（"+String.format("%.2f",(float)lim.limitMinAmt*0.01)
								+"~"+String.format("%.2f",(float)lim.limitMaxAmt*0.01)
								+"元），订单号："+tsOrder.batTranCustOrdno;
					}
					transferAmount += tsOrder.txamt;
					transferCount++;
					//成功转账
	            	if(tsOrder.status==null || tsOrder.status.length() ==0){
	            		transferSuccessAmount += tsOrder.txamt;
	            		transferSuccessCount++;
	            	}
	            }
				if(dayTransferAmt+transferAmount>lim.limitDayAmt)
					return "日转账金额超限"+String.format("%.2f",(float)lim.limitDayAmt*0.01)+"元";
				if(monTransferAmt+transferAmount>lim.limitMonthAmt)
					return "月转账金额超限"+String.format("%.2f",(float)lim.limitMonthAmt*0.01)+"元";
				if(dayTransferCount+transferCount>lim.limitDayTimes)
					return "日转账次数超限"+lim.limitDayTimes+"次";
				if(monTransferCount+transferCount>lim.limitMonthTimes)
					return "月转账次数超限"+lim.limitMonthTimes+"次";
				if(dayTransferSuccessAmt+transferSuccessAmount>lim.limitDaySuccessAmt)
					return "日成功转账金额超限"+String.format("%.2f",(float)lim.limitDaySuccessAmt*0.01)+"元";
				if(monTransferSuccessAmt+transferSuccessAmount>lim.limitMonthSuccessAmt)
					return "月成功转账金额超限"+String.format("%.2f",(float)lim.limitMonthSuccessAmt*0.01)+"元";
				if(dayTransferSuccessCount+transferSuccessCount>lim.limitDaySuccessTimes)
					return "日成功转账次数超限"+lim.limitDaySuccessTimes+"次";
				if(monTransferSuccessCount+transferSuccessCount>lim.limitMonthSuccessTimes)
					return "月成功转账次数超限"+lim.limitMonthSuccessTimes+"次";
			}
		} else if(PayConstant.TRAN_TYPE_DS.equals(limitType)){//代收限额
			Object [] obj = (Object [])riskObject;
			String custType = (String) obj[0];
			String custId = (String) obj[1];
			Date curTime = (Date) obj[3];
			List<PayRequest> tsList = (List<PayRequest>)obj[2];
			PayRiskMerchantLimit lim = PayRiskMerchantLimitService.getMerchantLimit(custId,limitType);
			if(lim==null) return null;
			String startDate = "";
			String endDate = "";
			try {
				if("1".equals(lim.limitTimeFlag)){//限额时间标识 0永久，1限制时间 默认0
					startDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitStartDate);
					endDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitEndDate);
				}
			} catch (Exception e) {}
			//取得天、月的交易额、次
			String tranDate = new java.text.SimpleDateFormat("yyyyMMdd").format(curTime);
			List sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_MERCHANT,custId,tranDate);
			long dayTransferAmt = 0,monTransferAmt=0,dayTransferSuccessAmt = 0,monTransferSuccessAmt=0,
					dayTransferCount = 0,monTransferCount=0,dayTransferSuccessCount = 0,monTransferSuccessCount=0;
			for(int i=0; i<sumList.size(); i++){
				PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
				if("0".equals(sum.timeType)){//时间类型 0日 1月
					dayTransferAmt = sum.receiveAmt;
					dayTransferCount = sum.receiveCount;
					dayTransferSuccessAmt = sum.receiveSuccessAmt;
					dayTransferSuccessCount = sum.receiveSuccessCount;
				} else if("1".equals(sum.timeType)){
					monTransferAmt = sum.receiveAmt;
					monTransferCount = sum.receiveCount;
					monTransferSuccessAmt = sum.receiveSuccessAmt;
					monTransferSuccessCount = sum.receiveSuccessCount;
				}
			}
			//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
			if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
					tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
				long transferAmount=0l;//代收累计金额
				long transferSuccessAmount=0l;//代收成功累计金额
				long transferCount=0l;//代收累计次数
				long transferSuccessCount=0l;//代收成功累计次数
				for(int i = 0; i<tsList.size(); i++){
					PayRequest payRequest = tsList.get(i);
					long amount = Long.parseLong(payRequest.amount);
					if(amount<lim.limitMinAmt||amount>lim.limitMaxAmt){
						return "代收金额超限（"+String.format("%.2f",(float)lim.limitMinAmt*0.01)
								+"~"+String.format("%.2f",(float)lim.limitMaxAmt*0.01)
								+"元），账号："+payRequest.accNo;
					}
					transferAmount += amount;
					transferCount++;
					transferSuccessAmount+=amount;
					transferSuccessCount++;
	            }
				if(dayTransferAmt+transferAmount>lim.limitDayAmt)
					return "日代收金额超限"+String.format("%.2f",(float)lim.limitDayAmt*0.01)+"元";
				if(monTransferAmt+transferAmount>lim.limitMonthAmt)
					return "月代收金额超限"+String.format("%.2f",(float)lim.limitMonthAmt*0.01)+"元";
				if(dayTransferCount+transferCount>lim.limitDayTimes)
					return "日代收次数超限"+lim.limitDayTimes+"次";
				if(monTransferCount+transferCount>lim.limitMonthTimes)
					return "月代收次数超限"+lim.limitMonthTimes+"次";
				if(dayTransferSuccessAmt+transferSuccessAmount>lim.limitDaySuccessAmt)
					return "日成功代收金额超限"+String.format("%.2f",(float)lim.limitDaySuccessAmt*0.01)+"元";
				if(monTransferSuccessAmt+transferSuccessAmount>lim.limitMonthSuccessAmt)
					return "月成功代收金额超限"+String.format("%.2f",(float)lim.limitMonthSuccessAmt*0.01)+"元";
				if(dayTransferSuccessCount+transferSuccessCount>lim.limitDaySuccessTimes)
					return "日成功代收次数超限"+lim.limitDaySuccessTimes+"次";
				if(monTransferSuccessCount+transferSuccessCount>lim.limitMonthSuccessTimes)
					return "月成功代收次数超限"+lim.limitMonthSuccessTimes+"次";
			}
		} else if(PayConstant.TRAN_TYPE_DF.equals(limitType)){//代付限额
			Object [] obj = (Object [])riskObject;
			String custType = (String) obj[0];
			String custId = (String) obj[1];
			Date curTime = (Date) obj[3];
			List<PayReceiveAndPay> tsList = (List<PayReceiveAndPay>)obj[2];
			PayRiskMerchantLimit lim = PayRiskMerchantLimitService.getMerchantLimit(custId,limitType);
			if(lim==null) return null;
			String startDate = "";
			String endDate = "";
			try {
				if("1".equals(lim.limitTimeFlag)){//限额时间标识 0永久，1限制时间 默认0
					startDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitStartDate);
					endDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.limitEndDate);
				}
			} catch (Exception e) {}
			//取得天、月的交易额、次
			String tranDate = new java.text.SimpleDateFormat("yyyyMMdd").format(curTime);
			List sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_MERCHANT,custId,tranDate);
			long dayTransferAmt = 0,monTransferAmt=0,dayTransferSuccessAmt = 0,monTransferSuccessAmt=0,
					dayTransferCount = 0,monTransferCount=0,dayTransferSuccessCount = 0,monTransferSuccessCount=0;
			for(int i=0; i<sumList.size(); i++){
				PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
				if("0".equals(sum.timeType)){//时间类型 0日 1月
					dayTransferAmt = sum.payAmt;
					dayTransferCount = sum.payCount;
					dayTransferSuccessAmt = sum.paySuccessAmt;
					dayTransferSuccessCount = sum.paySuccessCount;
				} else if("1".equals(sum.timeType)){
					monTransferAmt = sum.payAmt;
					monTransferCount = sum.payCount;
					monTransferSuccessAmt = sum.paySuccessAmt;
					monTransferSuccessCount = sum.paySuccessCount;
				}
			}
			//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
			if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
					tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
				long transferAmount=0l;//代收累计金额
				long transferSuccessAmount=0l;//代收成功累计金额
				long transferCount=0l;//代收累计次数
				long transferSuccessCount=0l;//代收成功累计次数
				for(int i = 0; i<tsList.size(); i++){
					PayReceiveAndPay rp = tsList.get(i);
					long amount = rp.amount;
					if(amount<lim.limitMinAmt||amount>lim.limitMaxAmt){
						return "代付金额超限（"+String.format("%.2f",(float)lim.limitMinAmt*0.01)
								+"~"+String.format("%.2f",(float)lim.limitMaxAmt*0.01)
								+"元），账号："+rp.accNo;
					}
					transferAmount += amount;
					transferCount++;
					transferSuccessAmount+=amount;
					transferSuccessCount++;
	            }
				if(dayTransferAmt+transferAmount>lim.limitDayAmt)
					return "日 代付金额超限"+String.format("%.2f",(float)lim.limitDayAmt*0.01)+"元";
				if(monTransferAmt+transferAmount>lim.limitMonthAmt)
					return "月 代付金额超限"+String.format("%.2f",(float)lim.limitMonthAmt*0.01)+"元";
				if(dayTransferCount+transferCount>lim.limitDayTimes)
					return "日 代付次数超限"+lim.limitDayTimes+"次";
				if(monTransferCount+transferCount>lim.limitMonthTimes)
					return "月 代付次数超限"+lim.limitMonthTimes+"次";
				if(dayTransferSuccessAmt+transferSuccessAmount>lim.limitDaySuccessAmt)
					return "日成功 代付金额超限"+String.format("%.2f",(float)lim.limitDaySuccessAmt*0.01)+"元";
				if(monTransferSuccessAmt+transferSuccessAmount>lim.limitMonthSuccessAmt)
					return "月成功 代付金额超限"+String.format("%.2f",(float)lim.limitMonthSuccessAmt*0.01)+"元";
				if(dayTransferSuccessCount+transferSuccessCount>lim.limitDaySuccessTimes)
					return "日成功 代付次数超限"+lim.limitDaySuccessTimes+"次";
				if(monTransferSuccessCount+transferSuccessCount>lim.limitMonthSuccessTimes)
					return "月成功 代付次数超限"+lim.limitMonthSuccessTimes+"次";
			}
		}
		return null;
	}
	/**
	 * 用户限额检查，包括天、月的限额、限次（(1消费（充值）、5提现、6转账)）
	 * @param riskObject 风控对象
	 * @param limitType 限额类型:1消费 2充值 5提现 6转账 默认1
	 * @return
	 * @throws Exception 
	 */
	public String checkUserLimit(Object riskObject,String userId,String limitType) throws Exception{
		//充值、提现、转账
		if(PayConstant.TRAN_TYPE_RECHARGE.equals(limitType) || PayConstant.TRAN_TYPE_WITHDRAW.equals(limitType)
				|| PayConstant.TRAN_TYPE_TRANSFER.equals(limitType)){
			//黑名单检查
			if(checkClientIsBlackList(PayConstant.CUST_TYPE_USER,userId))return "黑名单用户";
		//支付
		} else if(PayConstant.TRAN_TYPE_CONSUME.equals(limitType)){
			PayRequest payRequest = (PayRequest)riskObject;
			//快捷
			if("CertPayOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)){
				if(checkClientIsBlackList(PayConstant.CUST_TYPE_PAY_CARD,payRequest.cardNo))return "黑名单用户";
			//其他
			} else {
				if(payRequest.tranUserMap.get(payRequest.payerId)!=null){
					if(checkClientIsBlackList(PayConstant.CUST_TYPE_USER,payRequest.payerId))return "黑名单用户";
				}
			}
		}
		//消费(账户、网银、快捷支付、银行卡)
		if(PayConstant.TRAN_TYPE_CONSUME.equals(limitType)){
			PayRequest payRequest = (PayRequest)riskObject;
			PayTranUserInfo user = payRequest.tranUserMap.get(payRequest.payerId);
			PayRiskUserLimit lim = null;
			if(user == null){
				if(!"CertPayOrder".equals(payRequest.application)&&!"CertPayOrderH5".equals(payRequest.application))return null;
				lim = PayRiskUserLimitService.getUserLimit(payRequest.cardNo,"4",
					new PayRiskXListDAO().getClientTypeFromXList(PayConstant.CUST_TYPE_CARD,payRequest.cardNo),PayConstant.TRAN_TYPE_CARD);
			} else {
				lim = PayRiskUserLimitService.getUserLimit(
					user.userId,"2".equals(user.userStatus)?"1":"2",
					new PayRiskXListDAO().getClientTypeFromXList(PayConstant.CUST_TYPE_USER,user.userId),limitType);
			}
			long txAmt = Long.parseLong(payRequest.merchantOrderAmt);
			if(lim==null) return null;
			String startDate = "";
			String endDate = "";
			try {
				if("1".equals(lim.limitTimeFlag)){//限额时间标识 0永久，1限制时间 默认0
					startDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.startDate);
					endDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.endDate);
				}
			} catch (Exception e) {}
			//取得天、月的交易额、次
			String tranDate = new java.text.SimpleDateFormat("yyyyMMdd").format(payRequest.curTime);
			List <PayRiskDayTranSum>sumList = new ArrayList<PayRiskDayTranSum>();
			//快捷（检查卡号）
			if("CertPayOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)){
				sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_PAY_CARD,payRequest.cardNo,tranDate);
				long dayAmt = 0,monAmt=0,dayCount=0,monCount=0,daySuccessAmt = 0,monSuccessAmt=0,daySuccessCount=0,monSuccessCount=0;
				Date lastTranTime = null;
				for(int i=0; i<sumList.size(); i++){
					PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
					lastTranTime = sum.cardLastTranTime;
					if("0".equals(sum.timeType)){//时间类型 0日 1月
						dayAmt = sum.saleQuickAmt;
						dayCount = sum.saleQuickCount;
						daySuccessAmt = sum.saleQuickSuccessAmt;
						daySuccessCount = sum.saleQuickSuccessCount;
					} else if("1".equals(sum.timeType)){
						monAmt = sum.saleQuickAmt;
						monCount = sum.saleQuickCount;
						monSuccessAmt = sum.saleQuickSuccessAmt;
						monSuccessCount = sum.saleQuickSuccessCount;
					}
				}
				//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
				if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
						tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
					PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);
            		if(cardBin == null) return null;
            		if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(cardBin.cardType)){
            			if(txAmt>lim.debitCardOnceAmt)return "该卡充值额度最高为" +String.format("%.2f",(float)lim.debitCardOnceAmt*0.01)+"元";
            			if(daySuccessAmt+txAmt>lim.debitCardDayAmt)return "该卡日消费金额超限"+String.format("%.2f",(float)lim.debitCardDayAmt*0.01)+"元";
            			if(lim.crebitCardTxnInterval!=-1&&lastTranTime!=null&&payRequest.curTime.getTime()-lastTranTime.getTime()<=lim.crebitCardTxnInterval*1000)return "该储蓄卡交易过于频繁";
            		} else if(PayConstant.CARD_BIN_TYPE_DAIJI.equals(cardBin.cardType)){
            			if(txAmt>lim.crebitCardOnceAmt)return "该卡额度最高为" +String.format("%.2f",(float)lim.crebitCardOnceAmt*0.01)+"元";
            			if(daySuccessAmt+txAmt>lim.crebitCardDayAmt)return "该卡日消费金额超限"+String.format("%.2f",(float)lim.crebitCardDayAmt*0.01)+"元";
            			if(lim.debitCardTxnInterval!=-1&&lastTranTime!=null&&payRequest.curTime.getTime()-lastTranTime.getTime()<=lim.debitCardTxnInterval*1000)return "该信用卡交易过于频繁";
            		}
				}
			} else {
				sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_USER,user.userId,tranDate);
				long dayAmt=0,monAmt=0,daySuccessAmt=0,monSuccessAmt=0,dayCount=0,monCount=0,daySuccessCount=0,monSuccessCount=0;
				PayRiskUserLimitSub limSub = null;
				for(int i=0; i<sumList.size(); i++){
					PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
					//账户
					if("00".equals(payRequest.payOrder.paytype)){
						if("0".equals(sum.timeType)){//时间类型 0日 1月
							dayAmt = sum.saleAccAmt;
							dayCount = sum.saleAccCount;
							daySuccessAmt = sum.saleAccSuccessAmt;
							daySuccessCount = sum.saleAccSuccessCount;
						} else if("1".equals(sum.timeType)){
							monAmt = sum.saleAccAmt;
							monCount = sum.saleAccCount;
							monSuccessAmt = sum.saleAccSuccessAmt;
							monSuccessCount = sum.saleAccSuccessCount;
						}
					//网银
					} else if("01".equals(payRequest.payOrder.paytype)){
						if("0".equals(sum.timeType)){//时间类型 0日 1月
							dayAmt = sum.saleAmount;
							dayCount = sum.saleCount;
							daySuccessAmt = sum.saleSuccessAmount;
							daySuccessCount = sum.saleSuccessCount;
						} else if("1".equals(sum.timeType)){
							monAmt = sum.saleAmount;
							monCount = sum.saleCount;
							monSuccessAmt = sum.saleSuccessAmount;
							monSuccessCount = sum.saleSuccessCount;
						}
					}
				}
				//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
				if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
						tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
					Map<String,PayRiskUserLimitSub> m = new HashMap<String,PayRiskUserLimitSub>();
					for(int i=0; i<lim.subLimitList.size(); i++){
						PayRiskUserLimitSub tmp = lim.subLimitList.get(i);
						m.put(tmp.limitBusClient, tmp);
					}
					limSub = m.get(payRequest.payOrder.paytype);
					if(limSub == null)return null;
					if(txAmt<limSub.minAmt)return "单笔消费额度最低为"+String.format("%.2f",(float)limSub.minAmt*0.01)+"元";
					if(txAmt>limSub.maxAmt)return "单笔消费额度最高为" +String.format("%.2f",(float)limSub.maxAmt*0.01)+"元";
					if(dayAmt+txAmt>limSub.dayAmt)return "日消费金额超限"+String.format("%.2f",(float)limSub.dayAmt*0.01)+"元";
					if(monAmt+txAmt>limSub.monthAmt)return "月消费金额超限"+String.format("%.2f",(float)limSub.monthAmt*0.01)+"元";
					if(dayCount+1>limSub.dayTimes)return "日消费次数超限"+limSub.dayTimes+"次";
					if(monCount+1>limSub.monthTimes)return "月消费次数超限"+limSub.monthTimes+"次";
					if(daySuccessAmt+txAmt>limSub.daySucAmt)return "日消费金额超限"+String.format("%.2f",(float)limSub.daySucAmt*0.01)+"元";
					if(monSuccessAmt+txAmt>limSub.monthSucAmt)return "月消费金额超限"+String.format("%.2f",(float)limSub.monthSucAmt*0.01)+"元";
					if(daySuccessCount+1>limSub.daySucTimes)return "日消费次数超限"+limSub.daySucTimes+"次";
					if(monSuccessCount+1>limSub.monthSucTimes)return "月消费次数超限"+limSub.monthSucTimes+"次";
				}
			}
			//消费限额
			if("1".equals(limitType)){}
		} else if(PayConstant.TRAN_TYPE_RECHARGE.equals(limitType)){//充值
			PayRequest payRequest = (PayRequest)riskObject;
			PayTranUserInfo user = new PayTranUserInfoDAO().detailPayTranUserInfoByCustId(userId);
			if(user == null)return null;
			PayRiskUserLimit lim = PayRiskUserLimitService.getUserLimit(
				user.userId,"2".equals(user.userStatus)?"1":"2",
				new PayRiskXListDAO().getClientTypeFromXList(PayConstant.CUST_TYPE_USER,user.userId),limitType);
			long txAmt = Long.parseLong(payRequest.merchantOrderAmt);
			if(lim==null) return null;
			String startDate = "";
			String endDate = "";
			try {
				if("1".equals(lim.limitTimeFlag)){//限额时间标识 0永久，1限制时间 默认0
					startDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.startDate);
					endDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.endDate);
				}
			} catch (Exception e) {}
			//取得天、月的交易额、次
			String tranDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
			List sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_USER,user.userId,tranDate);
			long dayAmt = 0,monAmt=0,daySuccessAmt = 0,monSuccessAmt=0,dayCount = 0,monCount=0,daySuccessCount = 0,monSuccessCount=0;
			for(int i=0; i<sumList.size(); i++){
				PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
				if("0".equals(sum.timeType)){//时间类型 0日 1月
					dayAmt = sum.rechargeAmount;
					dayCount = sum.rechargeCount;
					daySuccessAmt = sum.rechargeSuccessAmount;
					daySuccessCount = sum.rechargeSuccessCount;
				} else if("1".equals(sum.timeType)){
					monAmt = sum.rechargeAmount;
					monCount = sum.rechargeCount;
					monSuccessAmt = sum.rechargeSuccessAmount;
					monSuccessCount = sum.rechargeSuccessCount;
				}
			}
			//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
			if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
					tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
				if(lim.subLimitList.size()!=1)return null;
				PayRiskUserLimitSub limSub = lim.subLimitList.get(0);
				if(txAmt<limSub.minAmt)return "单笔充值额度最低为"+String.format("%.2f",(float)limSub.minAmt*0.01)+"元";
				if(txAmt>limSub.maxAmt)return "单笔充值额度最高为" +String.format("%.2f",(float)limSub.maxAmt*0.01)+"元";
				if(dayAmt+txAmt>limSub.dayAmt)return "日充值金额超限"+String.format("%.2f",(float)limSub.dayAmt*0.01)+"元";
				if(monAmt+txAmt>limSub.monthAmt)return "月充值金额超限"+String.format("%.2f",(float)limSub.monthAmt*0.01)+"元";
				if(dayCount+1>limSub.dayTimes)return "日充值次数超限"+limSub.dayTimes+"次";
				if(monCount+1>limSub.monthTimes)return "月充值次数超限"+limSub.monthTimes+"次";
				if(daySuccessAmt+txAmt>limSub.daySucAmt)return "日充值金额超限"+String.format("%.2f",(float)limSub.daySucAmt*0.01)+"元";
				if(monSuccessAmt+txAmt>limSub.monthSucAmt)return "月充值金额超限"+String.format("%.2f",(float)limSub.monthSucAmt*0.01)+"元";
				if(daySuccessCount+1>limSub.daySucTimes)return "日充值次数超限"+limSub.daySucTimes+"次";
				if(monSuccessCount+1>limSub.monthSucTimes)return "月充值次数超限"+limSub.monthSucTimes+"次";
			}
		} else if(PayConstant.TRAN_TYPE_WITHDRAW.equals(limitType)){//提现
			PayWithdrawCashOrder pwcOrder = (PayWithdrawCashOrder)riskObject;
			PayTranUserInfo user = new PayTranUserInfoDAO().detailPayTranUserInfoByCustId(userId);
			if(user == null)return null;
			PayRiskUserLimit lim = PayRiskUserLimitService.getUserLimit(
					user.userId,"2".equals(user.userStatus)?"1":"2",
					new PayRiskXListDAO().getClientTypeFromXList(PayConstant.CUST_TYPE_USER,user.userId),limitType);
			if(lim==null) return null;
			String startDate = "";
			String endDate = "";
			try {
				if("1".equals(lim.limitTimeFlag)){//限额时间标识 0永久，1限制时间 默认0
					startDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.startDate);
					endDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.endDate);
				}
			} catch (Exception e) {}
			//取得天、月的交易额、次
			String tranDate = new java.text.SimpleDateFormat("yyyyMMdd").format(pwcOrder.actTime);
			List sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_USER,user.userId,tranDate);
			long dayAmt = 0,monAmt=0,daySuccessAmt = 0,monSuccessAmt=0,dayCount = 0,monCount=0,daySuccessCount = 0,monSuccessCount=0;
			for(int i=0; i<sumList.size(); i++){
				PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
				if("0".equals(sum.timeType)){//时间类型 0日 1月
					dayAmt = sum.cashAmount;
					dayCount = sum.cashCount;
					daySuccessAmt = sum.cashSuccessAmount;
					daySuccessCount = sum.cashSuccessCount;
				} else if("1".equals(sum.timeType)){
					monAmt = sum.cashAmount;
					monCount = sum.cashCount;
					monSuccessAmt = sum.cashSuccessAmount;
					monSuccessCount = sum.cashSuccessCount;
				}
			}
			//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
			if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
					tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
				if(lim.subLimitList.size()!=1)return null;
				PayRiskUserLimitSub limSub = lim.subLimitList.get(0);
				if(pwcOrder.txamt<limSub.minAmt)return "单笔提现额度最低为"+String.format("%.2f",(float)limSub.minAmt*0.01)+"元";
				if(pwcOrder.txamt>limSub.maxAmt)return "单笔提现额度最高为" +String.format("%.2f",(float)limSub.maxAmt*0.01)+"元";
				if(dayAmt+pwcOrder.txamt>limSub.dayAmt)return "日提现金额超限"+String.format("%.2f",(float)limSub.dayAmt*0.01)+"元";
				if(monAmt+pwcOrder.txamt>limSub.monthAmt)return "月提现金额超限"+String.format("%.2f",(float)limSub.monthAmt*0.01)+"元";
				if(dayCount+1>limSub.dayTimes)return "日提现次数超限"+limSub.dayTimes+"次";
				if(monCount+1>limSub.monthTimes)return "月提现次数超限"+limSub.monthTimes+"次";
				if(daySuccessAmt+pwcOrder.txamt>limSub.daySucAmt)return "日成功提现金额超限"+String.format("%.2f",(float)limSub.daySucAmt*0.01)+"元";
				if(monSuccessAmt+pwcOrder.txamt>limSub.monthSucAmt)return "月成功提现金额超限"+String.format("%.2f",(float)limSub.monthSucAmt*0.01)+"元";
				if(daySuccessCount+1>limSub.daySucTimes)return "日成功提现次数超限"+limSub.daySucTimes+"次";
				if(monSuccessCount+1>limSub.monthSucTimes)return "月成功提现次数超限"+limSub.monthSucTimes+"次";
			}
		} else if(PayConstant.TRAN_TYPE_TRANSFER.equals(limitType)){//转账
			Object [] obj = (Object [])riskObject;
			String custType = (String) obj[0];
			String custId = (String) obj[1];
			Date curTime = (Date) obj[3];
			List<PayTransferAccOrder>tsList = (List<PayTransferAccOrder>)obj[2];
			if(tsList.size()!=1)return null;
			PayTransferAccOrder ptOrder = tsList.get(0);
			PayTranUserInfo user = new PayTranUserInfoDAO().detailPayTranUserInfoByCustId(custId);
			if(user == null)return null;
			PayRiskUserLimit lim = PayRiskUserLimitService.getUserLimit(
					user.userId,"2".equals(user.userStatus)?"1":"2",
					new PayRiskXListDAO().getClientTypeFromXList(PayConstant.CUST_TYPE_USER,user.userId),limitType);
			if(lim==null) return null;
			String startDate = "";
			String endDate = "";
			try {
				if("1".equals(lim.limitTimeFlag)){//限额时间标识 0永久，1限制时间 默认0
					startDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.startDate);
					endDate = new java.text.SimpleDateFormat("yyyyMMdd").format(lim.endDate);
				}
			} catch (Exception e) {}
			//取得天、月的交易额、次
			String tranDate = new java.text.SimpleDateFormat("yyyyMMdd").format(curTime);
			List sumList = new PayRiskDayTranSumDAO().getCustTranSum(PayConstant.CUST_TYPE_USER,user.userId,tranDate);
			long dayAmt = 0,monAmt=0,daySuccessAmt = 0,monSuccessAmt=0,dayCount = 0,monCount=0,daySuccessCount = 0,monSuccessCount=0;
			for(int i=0; i<sumList.size(); i++){
				PayRiskDayTranSum sum = (PayRiskDayTranSum)sumList.get(i);
				if("0".equals(sum.timeType)){//时间类型 0日 1月
					dayAmt = sum.transferAmount;
					dayCount = sum.transferCount;
					daySuccessAmt = sum.transferSuccessAmount;
					daySuccessCount = sum.transferSuccessCount;
				} else if("1".equals(sum.timeType)){
					monAmt = sum.transferAmount;
					monCount = sum.transferCount;
					monSuccessAmt = sum.transferSuccessAmount;
					monSuccessCount = sum.transferSuccessCount;
				}
			}
			//指定时间范围判断(限额时间标识 0永久，1限制时间 默认0)
			if("0".equals(lim.limitTimeFlag)||("1".equals(lim.limitTimeFlag)&&
					tranDate.compareTo(startDate)>=0&&tranDate.compareTo(endDate)<=0)){
				if(lim.subLimitList.size()!=1)return null;
				PayRiskUserLimitSub limSub = lim.subLimitList.get(0);
				if(ptOrder.txamt<limSub.minAmt)return "单笔转账额度最低为"+String.format("%.2f",(float)limSub.minAmt*0.01)+"元";
				if(ptOrder.txamt>limSub.maxAmt)return "单笔转账额度最高为" +String.format("%.2f",(float)limSub.maxAmt*0.01)+"元";
				if(dayAmt+ptOrder.txamt>limSub.dayAmt)return "日转账金额超限"+String.format("%.2f",(float)limSub.dayAmt*0.01)+"元";
				if(monAmt+ptOrder.txamt>limSub.monthAmt)return "月转账金额超限"+String.format("%.2f",(float)limSub.monthAmt*0.01)+"元";
				if(dayCount+1>limSub.dayTimes)return "日转账次数超限"+limSub.dayTimes+"次";
				if(monCount+1>limSub.monthTimes)return "月转账次数超限"+limSub.monthTimes+"次";
				if(daySuccessAmt+ptOrder.txamt>limSub.daySucAmt)return "日转账金额超限"+String.format("%.2f",(float)limSub.daySucAmt*0.01)+"元";
				if(monSuccessAmt+ptOrder.txamt>limSub.monthSucAmt)return "月转账金额超限"+String.format("%.2f",(float)limSub.monthSucAmt*0.01)+"元";
				if(daySuccessCount+1>limSub.daySucTimes)return "日转账次数超限"+limSub.daySucTimes+"次";
				if(monSuccessCount+1>limSub.monthSucTimes)return "月转账次数超限"+limSub.monthSucTimes+"次";
			}
		}
		return null;
	}
	/**
	 * 检查客户是否在黑名单
	 * @param clientType
	 * @param clientCode
	 * @return
	 */
	private boolean checkClientIsBlackList(String clientType,String clientCode){
		return new PayRiskXListDAO().getClientFromXList(clientType,clientCode);
	}
}
