package com.pay.merchantinterface.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.DataTransUtil;
import util.PayUtil;
import util.SMSUtil;
import util.Tools;
import com.PayConstant;
import com.jweb.service.TransactionService;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayChannelRotationLogDAO;
import com.pay.coopbank.service.PayCoopBankService;
import com.pay.fee.dao.PayFeeRate;
import com.pay.fee.service.PayFeeRateService;
import com.pay.merchant.dao.PayAccProfile;
import com.pay.merchant.dao.PayAccProfileDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.merchant.dao.PayNotifyFailUrl;
import com.pay.merchant.dao.PayNotifyFailUrlDAO;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayProductOrderDAO;
import com.pay.risk.service.PayRiskDayTranSumService;
import com.pay.settlement.dao.PayChargeLog;
import com.pay.settlement.dao.PayChargeLogDAO;

public class NotifyInterface extends TransactionService{
	private static final Log log = LogFactory.getLog(NotifyInterface.class); 
	private static Map<String,Date> orderCacheMap = new Hashtable<String,Date>();
	static int n = 3,interval = 300;
	static {
		init();
	}
	public static void init(){
		//支付结果通知商户失败重发信息设置，两个变量“n,m”，n表示通知次数，m表示时间间隔（分钟）。
		String tmp = PayConstant.PAY_CONFIG.get("NOTIFY_MER_FAIL_RESEND_INFO");
		if(tmp!=null){
			try {
				String tmp1[] = tmp.replaceAll("，",",").replaceAll("、",",").split(",");
				if(tmp1.length==2){
					n=Integer.parseInt(tmp1[0]);
					interval = Integer.parseInt(tmp1[1])*60;
				}
			} catch (Exception e) {
				log.info(PayUtil.exceptionToString(e));
			}
		}
	}
	/**
	 * 避免支付渠道同一笔订单同时通知（毫秒间隔2次以上通知）
	 * @param payordno
	 * @return true 可以处理，false 处理完成
	 */
	private static boolean repeatNotifyOrderCheck(String payordno){
		synchronized (orderCacheMap) {
			Date curTime = new Date();
			//清理3秒钟以前订单
			Iterator<String> it = orderCacheMap.keySet().iterator();
			List<String> removeCache = new ArrayList<String>();
			while(it.hasNext()){
				String key = it.next();
				Date tmp = orderCacheMap.get(key);
				if(curTime.getTime() - tmp.getTime() > 3*1000)removeCache.add(key);
			}
			for(int i=0; i<removeCache.size(); i++)orderCacheMap.remove(removeCache.get(i));
			if(orderCacheMap.get(payordno)==null){
				orderCacheMap.put(payordno, curTime);
				return true;
			} else return false;
		}
	}
	public void testNofify(String payordno) throws Exception{}
	/**
	 * 通知商户支付结果，注意，调用前需要设置两个参数tmpPayOrder.payordno tmpPayOrder.actdat
	 * @param payordno
	 * @throws Exception
	 */
	public void notifyMer(PayOrder tmpPayOrder) throws Exception{
		log.info("订单"+tmpPayOrder.payordno+"通知处理开始=======");
		//避免支付渠道同一笔订单同时通知（毫秒间隔2次以上通知）
		if(!repeatNotifyOrderCheck(tmpPayOrder.payordno)){
			log.info("订单"+tmpPayOrder.payordno+"处理中...");
			return;
		}
		PayInterfaceDAO payInterfaceDAO = new PayInterfaceDAO();
		PayMerchantDAO payMerchantDAO = new PayMerchantDAO();
		PayProductOrderDAO payProductOrderDAO = new PayProductOrderDAO();
		PayAccProfileDAO accDao = new PayAccProfileDAO();
		PayChargeLogDAO chargeDAO = new PayChargeLogDAO();
		PayRiskDayTranSumService payRiskDayTranSumService = new PayRiskDayTranSumService();
		PayChannelRotationLogDAO prlDAO = new PayChannelRotationLogDAO();
		try {
			transaction.beignTransaction(payInterfaceDAO,payMerchantDAO,payProductOrderDAO,accDao,chargeDAO,payRiskDayTranSumService,prlDAO);
			//取得订单
			PayOrder order = payInterfaceDAO.getOrderByPrdordno(tmpPayOrder.payordno);
			if(order == null)throw new Exception("支付订单不存在");
			PayProductOrder prdOrder = payProductOrderDAO.getProductOrderById(order.merno,order.prdordno);
			if(prdOrder == null)throw new Exception("商品订单不存在");
			if("01".equals(order.ordstatus)&&!"01".equals(tmpPayOrder.ordstatus))throw new Exception("订单已支付成功，再次通知的失败状态不做任何处理");
			order.bankerror=tmpPayOrder.bankerror;
			tmpPayOrder.custId = prdOrder.custId;
			//更新订单支付状态
			//计算手续费
			Map<String,PayMerchant>  merMap = payMerchantDAO.getSettleMentMerchant(order.merno);//取得商户和父商户
			PayMerchant mer = merMap.get(order.merno);
			if(mer == null)throw new Exception("商户状态异常");
			PayFeeRate feeRate = null;
			PayFeeRate channelFeeRate = null;
			//交易类型 1消费 2充值 3结算 4退款 5提现  6转账 7消费B2C借记卡 8消费B2C信用卡 9消费B2B 10消费直接到银行卡 11消费快捷借记卡 12消费快捷贷记卡
			//支付方式，00 支付账户  01 网上银行 03 快捷支付 07微信扫码 10微信WAP、11支付宝扫码、12手Q
			if("03".equals(order.paytype)){//快捷
				PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(order.payacno);
				//取得费率
				feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+","+(cardBin!=null&&"1".equals(cardBin.cardType)?"12":"11"));
				channelFeeRate = PayCoopBankService.CHANNEL_MAP_ALL.get(order.payChannel).
						feeRateMap.get(order.payChannel+","+(cardBin!=null&&"1".equals(cardBin.cardType)?"12":"11"));
				//修改绑定卡号状态卡号
				try {payInterfaceDAO.updateQuickPayBindCardStatus(prdOrder, order);} catch (Exception e) {e.printStackTrace();}
			} else if("07".equals(order.paytype)){//微信扫码
				feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",10");
				channelFeeRate = PayCoopBankService.CHANNEL_MAP_ALL.get(order.payChannel).feeRateMap.get(order.payChannel+","+"10");
			} else if("10".equals(order.paytype)){//微信WAP
				feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",18");
				channelFeeRate = PayCoopBankService.CHANNEL_MAP_ALL.get(order.payChannel).feeRateMap.get(order.payChannel+","+"18");
			} else if("11".equals(order.paytype)){//支付宝扫码
				feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",17");
				channelFeeRate = PayCoopBankService.CHANNEL_MAP_ALL.get(order.payChannel).feeRateMap.get(order.payChannel+","+"17");
			} else if("12".equals(order.paytype)){//手Q
				feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",27");
				channelFeeRate = PayCoopBankService.CHANNEL_MAP_ALL.get(order.payChannel).feeRateMap.get(order.payChannel+","+"27");
			} else {
				//B2C
				if("1".equals(order.bustyp)){
					feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+","+(order.bankCardType!=null&&"1".equals(order.bankCardType)?"8":"7"));
					try {
						channelFeeRate = PayCoopBankService.CHANNEL_MAP_ALL.get(order.payChannel)
							.feeRateMap.get(order.payChannel+","+(order.bankCardType!=null&&"1".equals(order.bankCardType)?"8":"7"));
					} catch (Exception e){}
				}
				//B2B
				else if("2".equals(order.bustyp)){
					feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",9");
					try {
						channelFeeRate = PayCoopBankService.CHANNEL_MAP_ALL.get(order.payChannel).
							feeRateMap.get(order.payChannel+",9");
					} catch (Exception e){}
				}
			}
			Object [] feeInfo = PayFeeRateService.getFeeByFeeRate(feeRate, order.txamt);
			Object [] channelFeeInfo = PayFeeRateService.getFeeByFeeRate(channelFeeRate, order.txamt);
			order.fee = (Long)(feeInfo[0]);
			order.channelFee = (Long)(channelFeeInfo[0]);
			//代理费率计算=========================start
			PayMerchant parent = merMap.get(mer.parentId);
			if(parent != null){//有上级代理，代理费包含在手续费中了
				PayFeeRate agentFeeRate = null;
				if("03".equals(order.paytype)){//快捷
					PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(order.payacno);
					//取得费率
					agentFeeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+","+(cardBin!=null&&"1".equals(cardBin.cardType)?"24":"23"));
				} else if("07".equals(order.paytype)){//微信扫码
					agentFeeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",22");
				} else if("10".equals(order.paytype)){//微信WAP
					agentFeeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",26");
				} else if("11".equals(order.paytype)){//支付宝扫码
					agentFeeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",25");
				} else if("12".equals(order.paytype)){//手Q
					agentFeeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",28");
				} else {
					//B2C
					if("1".equals(order.bustyp)){
						agentFeeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+","+(order.bankCardType!=null&&"1".equals(order.bankCardType)?"20":"19"));
					}
					//B2B
					else if("2".equals(order.bustyp)){
						agentFeeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",21");
					}
				}
				Object [] agentFee = PayFeeRateService.getFeeByFeeRate(agentFeeRate,order.txamt);
				order.agentFee = (Long)agentFee[0];
				log.info("代理费："+order.agentFee);
			}
			//代理费率计算=========================end
			order.netrecamt = order.txamt - order.fee;
			//结算金额小于0，归零
			order.netrecamt = order.netrecamt < 0l ? 0l :order.netrecamt;
			//雅酷微信、支付宝扫码渠道结算，不加到虚拟账户
			if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YK").equals(order.payChannel)){
				if(("07".equals(order.paytype)||"11".equals(order.paytype))) prdOrder.stlsts="2";
			} else {
				//结算方式 0自动结算到虚拟账户 1线下打款 2自动结算到银行账户 3实时结算到虚拟账户  4实时结算到银行账户
				if("D".equals(mer.payCustStlInfo.custSetPeriod)&&"0".equals(mer.payCustStlInfo.custSetFrey)){//T+0实时到虚拟账户
					if(!"2".equals(prdOrder.stlsts)&&!"2".equals(prdOrder.prdordtype)){//未结算、非担保订单
						prdOrder.stlsts="2";//清结算状态 0初始 1清算完成 2结算完成 默认0
						try {
							PayAccProfile payAccProfileTmp = accDao.detailPayAccProfileByAcTypeAndAcNo("1",mer.custId);
				        	if(payAccProfileTmp!=null)log.info(payAccProfileTmp.toString().replaceAll("\n",";"));
						} catch (Exception e) {}
						//商户虚拟账户加钱
						PayAccProfile payAccProfile = new PayAccProfile();
						payAccProfile.acType=PayConstant.CUST_TYPE_MERCHANT;
						payAccProfile.payAcNo = mer.custId;
						//交易手续费收取方式，0结算收取 1预存手续费收取，手续费充足的情况
						if("1".equals(mer.chargeWay)&&order.fee>0&&mer.preStorageFee>=order.fee
								&&new PayMerchantDAO().updatePayMerchantForStlPreStorageFee(order.fee, mer.custId)>0){
							payAccProfile.cashAcBal = order.netrecamt+order.fee;
							payAccProfile.consAcBal = order.netrecamt+order.fee;
							PayChargeLog chargeLog = new PayChargeLog();
							chargeLog.id = Tools.getUniqueIdentify();
							chargeLog.custType = PayConstant.CUST_TYPE_MERCHANT;
							chargeLog.custId = mer.custId;
							chargeLog.chargeType = "0";//手续费类型 0交易结算，1代收，2代付
							chargeLog.chargeFrom = "0";//扣款来源 0手续费余额，1账户余额
							chargeLog.amt=order.fee;
							chargeLog.createId="admin";
							chargeLog.settleTime = new Date();
							chargeLog.remark="扣除订单手续费"+String.format("%.2f", ((double)chargeLog.amt)/100d)+"元（支付订单号："+order.payordno+"）。";
							chargeLog.curStorageFee = mer.preStorageFee-chargeLog.amt;
							chargeDAO.addPayChargeLog(chargeLog);
						} else{
							payAccProfile.cashAcBal = order.netrecamt;
							payAccProfile.consAcBal = order.netrecamt;
						}
						accDao.updatePayAccProfileForNotify(payAccProfile,prdOrder);
					}
				}
			}
			order.filed1 = (feeRate==null?"":"结算："+(feeRate.feeName+"("+feeRate.feeCode+")"));
			order.filed3 = (String)feeInfo[1];
			String status = order.ordstatus; 
			order.ordstatus = tmpPayOrder.ordstatus;
			order.bankjrnno = tmpPayOrder.bankjrnno;
			order.actdat = "01".equals(tmpPayOrder.ordstatus)?new Date():order.actdat;//支付成功的情况下，取得银行支付时间戳
			payInterfaceDAO.updatePayOrder(prdOrder,order);
			if(!"01".equals(tmpPayOrder.ordstatus)){
				transaction.endTransaction();
				return;
			}
			//成功交易汇总更新，初次通知更新
			if(!"01".equals(status)){
				payRiskDayTranSumService.updateRiskTranSumForSuccess(prdOrder,order);
				//雅酷微信、支付宝扫码渠道结算金额保存（每张结算卡10万元）
//				if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YK").equals(order.payChannel)){
//					new YKPayService().saveStlAmt(order); 
//				}
				if("07".equals(order.paytype)||"10".equals(order.paytype)||"11".equals(order.paytype)
					||"12".equals(order.paytype))prlDAO.updatePayChannelRotationLogSuccessTran(order);
			}
			//取得支付列表
			List orderList = payInterfaceDAO.getPayOrderListByPrdordno(order.merno,order.prdordno);
			//取得退款列表
			List refundList = new ArrayList();
			//取得撤销列表
			List cancelList = new ArrayList();
			//封装通知数据
			String notifyXml = new MerchantXmlUtil().orderNotifyToXml(prdOrder,orderList,refundList,cancelList);
			//通知(若失败，间隔一定时间继续通知多次)
			Thread t = new NotifySender(prdOrder,order,notifyXml);
			t.start();
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			throw e;
		}
	}
}
class NotifySender extends Thread{
	private static final Log log = LogFactory.getLog(NotifySender.class);
	PayProductOrder prdOrder;
	PayOrder payOrder;
	String notifyXml;
	public NotifySender(PayProductOrder prdOrder,PayOrder payOrder,String notifyXml){
		this.prdOrder = prdOrder;
		this.payOrder = payOrder;
		this.notifyXml = notifyXml;
	}
	public void run(){
		boolean isSuccess = false;
		for(int i=0; i<NotifyInterface.n; i++){
			try {
				log.info("通知："+prdOrder.notifyurl);
				log.info(notifyXml);
				new DataTransUtil().doPost(prdOrder.notifyurl, CertUtil.createTransStr(notifyXml).getBytes("utf-8"));
				isSuccess = true;
				break;
			} catch (Exception e) {
				//保存错误url
				try {
					PayNotifyFailUrl pnfu = new PayNotifyFailUrl();
					pnfu.id = payOrder.payordno;
					pnfu.merchantNo = payOrder.merno;
					pnfu.url = prdOrder.notifyurl;
					pnfu.errorMsg = e.getMessage();
					pnfu.createTime = new Date();
					new PayNotifyFailUrlDAO().addPayNotifyFailUrl(pnfu);
				} catch (Exception e2) {}
				try {Thread.sleep(NotifyInterface.interval*1000);} catch (InterruptedException e1) {}
				e.printStackTrace();
				log.info(PayUtil.exceptionToString(e));
			}
		}
		//通知成功,更新通知状态 通知商户标识，0初始、1已通知、2通知失败，默认0
		if(isSuccess)payOrder.notifyMerFlag="1";
		else{
			payOrder.notifyMerFlag="2";
			//掉单是否发送短信
			if("1".equals(PayConstant.PAY_CONFIG.get("NOTIFY_MER_FAIL_SMS_FLAG"))
					&&PayConstant.PAY_CONFIG.get("NOTIFY_MER_FAIL_SMS_MOBILE")!=null)
			try {
				String [] ms = PayConstant.PAY_CONFIG.get("NOTIFY_MER_FAIL_SMS_MOBILE").replaceAll("，",",").split(",");
				//最多5个手机号发送
				for(int i=0; i<ms.length&&i<5; i++){
					if(ms[i].length()!=11)continue;
					try {new Long(ms[i]);} catch (Exception e) {continue;}
					new SMSUtil().send(ms[i], "商户掉单，商户号【"+payOrder.merno+"】，支付订单号【"+payOrder.payordno+"】。");
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info(PayUtil.exceptionToString(e));
			}
		}
		try {
			new PayInterfaceDAO().updatePayOrderNotifyFalg(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
}
