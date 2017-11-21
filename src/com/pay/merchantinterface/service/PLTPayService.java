package com.pay.merchantinterface.service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.PayUtil;
import util.SMSUtil;
import util.Tools;

import com.PayConstant;
import com.pay.bank.dao.PayBank;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayChannelBankRelation;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.dao.PayCoopBankDAO;
import com.pay.fee.dao.PayFeeRateDAO;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.merchantinterface.dao.PayQuickPaySms;
import com.pay.merchantinterface.dao.PayQuickPaySmsDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.refund.dao.PayRefund;
/**
 * 平台接口服务类
 * @author Administrator
 *
 */
public class PLTPayService {
	private static final Log log = LogFactory.getLog(PLTPayService.class);
	static {
		Timer timer = new Timer();
		//短信码清理线程，每分钟运行一次
		timer.schedule(new ClearQuickPaySms(),300*1000,60*1000);
	}
	//短信验证码线程
//	public static Map<String,QuickPaySms> PAY_SMS_POOL = new Hashtable<String,QuickPaySms>();
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{}
	/**
	 * 商户接口下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{}
	/**
	 * 退款接口
	 * @param payRefund
	 */
	public void refund(PayRefund payRefund){}
	/**
	 * 快捷支付
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String quickPay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		PayQuickPaySmsDAO qsDAO = new PayQuickPaySmsDAO();
		payRequest.userMobileNo = payRequest.userMobileNo==null||payRequest.userMobileNo.length()==0?
				prdOrder.mobile:payRequest.userMobileNo;
		PayQuickPaySms qs = new PayQuickPaySms(payOrder.payordno);
//		qsDAO.removePayQuickPaySms(payOrder.payordno);
		qsDAO.addPayQuickPaySms(qs);
		String content = "校验码为："+qs.code+"，您的尾号"+payRequest.cardNo.substring(payRequest.cardNo.length()-4)+"的卡付款"
				+String.format("%.2f", ((double)Double.parseDouble(payRequest.merchantOrderAmt))/100d)
				+"元，"+(ClearQuickPaySms.existTime/60)+"分钟内输入，请勿泄露。";
		String res = new SMSUtil().send(payRequest.userMobileNo, content);
		log.info("支付短信发送：====================\n"+content);
		log.info("发送结果：====================\n"+res);
		if(!"0".equals(res)){
			qsDAO.removePayQuickPaySms(payOrder.payordno);
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><message merchantId=\""+payRequest.merchantId+"\" " +
				"merchantOrderId=\""+payRequest.merchantOrderId+"\" respCode=\"-1\" respDesc=\""+res+"\" " +
				"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
		}
		return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><message merchantId=\""+payRequest.merchantId+"\" " +
				"merchantOrderId=\""+payRequest.merchantOrderId+"\" respCode=\"000\" respDesc=\"处理成功\" " +
				"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
	}
	/**
	 * 快捷支付-短信验证码重发
	 * @param request
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public void resendCheckCode(PayRequest payRequest) throws Exception{
		if(payRequest.userMobileNo.length()==0)payRequest.userMobileNo=payRequest.productOrder.mobile;
		if(payRequest.cardNo.length()==0)payRequest.cardNo=payRequest.payOrder.bankpayacno;
		PayQuickPaySmsDAO qsDAO = new PayQuickPaySmsDAO();
		PayQuickPaySms qs = new PayQuickPaySms(payRequest.payOrder.payordno);
//		qsDAO.removePayQuickPaySms(payRequest.userMobileNo);
		qsDAO.addPayQuickPaySms(qs);
		String content = "校验码为："+qs.code+"，您的尾号"+ payRequest.cardNo.substring(payRequest.cardNo.length()-4)+"的卡付款"
				+String.format("%.2f", ((double)payRequest.payOrder.txamt)/100d) +"元，"+(ClearQuickPaySms.existTime/60)+"分钟内输入，请勿泄露。";
		String res = new SMSUtil().send(payRequest.userMobileNo, content);
		if(!"0".equals(res)){
			qsDAO.removePayQuickPaySms(payRequest.payOrder.payordno);
			throw new Exception(res);
		}
		log.info("支付短信发送：====================\n"+content);
		log.info("发送结果:====================\n"+res);	
	}
	/**
	 * 快捷支付-支付确认
	 * @param request
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public void certPayConfirm(PayRequest payRequest) throws Exception{
		//调用代扣，完成支付
		PayInterfaceOtherService service = new PayInterfaceOtherService();
		//检查商户余额是否充足
//		List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
		if(payRequest.cardNo.length()==0)payRequest.cardNo = payRequest.payOrder.bankpayacno;
		if(payRequest.userName.length()==0)payRequest.userName = payRequest.payOrder.bankpayusernm;
		if(payRequest.merchantOrderAmt.length()==0)payRequest.merchantOrderAmt = String.valueOf(payRequest.payOrder.txamt);
		if(payRequest.userMobileNo.length()==0)payRequest.userMobileNo = payRequest.productOrder.mobile;
		if(payRequest.credentialType.length()==0)payRequest.credentialType = payRequest.productOrder.credentialType;
		if(payRequest.credentialType.length()==0)payRequest.credentialType="01";
		if(payRequest.credentialNo.length()==0)payRequest.credentialNo = payRequest.productOrder.credentialNo;
		payRequest.receivePayNotifyUrl = payRequest.productOrder.receivePayNotifyUrl;
		//短信验证
		checkQuickSms(payRequest.payOrder.payordno,payRequest.checkCode);
		PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);
		//=======================
		PayBank bank = PayCardBinService.BANK_NAME_MAP.get(cardBin.bankName);
		payRequest.accType = cardBin.cardType;//账户类型
		payRequest.accNo = payRequest.cardNo;
		payRequest.accName = payRequest.userName;
		payRequest.receivePayType = "0";//代收
		payRequest.tranId = Tools.getUniqueIdentify();
		payRequest.accountProp = "0";
		payRequest.bankGeneralName = cardBin.bankName;
		payRequest.bankName = cardBin.bankName;
		payRequest.bankId = bank.bankNo;
		payRequest.protocolNo="";
		payRequest.amount = payRequest.merchantOrderAmt;
		payRequest.tel = payRequest.userMobileNo;
		payRequest.summary="商户"+payRequest.merchantId+"快捷支付，单号"+payRequest.merchantOrderId
				+"，金额"+payRequest.merchantOrderAmt;
		long allTime = 0l;
		//失败，保存错误信息
    	PayRequest payRequestReceive = new PayRequest();
    	payRequest.copyTo(payRequestReceive);
    	payRequestReceive.merchantId=PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT");
		payRequestReceive.merchant = new PayInterfaceDAO().getCertByMerchantId(payRequest.merchantId);
		//平台快捷取得指定渠道
		PayCoopBank payChannel =new PayCoopBankDAO().getChannelById(PayConstant.PAY_CONFIG.get("RECEIVE_PAY_CHANNEL"));
		//指定平台是否存在，代收的银行是否支持，代收银行最大额度是否满足，否则取费率最低的
		PayChannelBankRelation cbr = new PayFeeRateDAO().getSupportedBankForRP(cardBin.bankCode,payChannel.bankCode,"15");
		if(payChannel == null || cbr==null || (cbr.receiveMaxAmt>0&&cbr.receiveMaxAmt < Long.parseLong(payRequest.amount))){
			payChannel = service.getRPChannel(payRequest,Long.parseLong(payRequest.amount));
		}
//		if(payChannel==null ||
//			!new PayFeeRateDAO().getSupportedBankForRP(cardBin.bankCode,payChannel.bankCode,"15"))
//			payChannel = service.getRPChannel(payRequest,Long.parseLong(payRequest.amount));
		payRequestReceive.rpChannel = payChannel;
		payRequestReceive.receiveAndPaySingle = service.addPayReceiveAndPaySingle(payRequestReceive,"1");//代收付产生类型，0代收付，1快捷包装代收付
		//畅捷
		if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(payChannel.bankCode)){
//			list.add(payRequestReceive.receiveAndPaySingle);
			//通过畅捷发送代付请求
			new ReceivePayCJThread(payRequestReceive,"single").start();
			//循环检查支付结果
			//查询处理结果,支付结果在其他线程中设置值
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
				allTime += CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i];
				//用户等待时间过长，修改订单状态，疑似掉单
				if(allTime>180){
					payRequestReceive.setRespInfo("074");
					payRequest.payOrder.ordstatus="03";
					payRequest.payOrder.bankerror = "渠道查单无返回，疑似掉单";
					new PayOrderDAO().updateOrderError(payRequest.payOrder);
					payRequestReceive.respDesc = payRequestReceive.respDesc+",稍后通知";
					//掉单是否发送短信
					if("1".equals(PayConstant.PAY_CONFIG.get("NOTIFY_MER_FAIL_SMS_FLAG"))
							&&PayConstant.PAY_CONFIG.get("NOTIFY_MER_FAIL_SMS_MOBILE")!=null)
					try {
						String [] ms = PayConstant.PAY_CONFIG.get("NOTIFY_MER_FAIL_SMS_MOBILE").replaceAll("，",",").split(",");
						//最多5个手机号发送
						for(int j=0; j<ms.length&&j<5; j++){
							if(ms[j].length()!=11)continue;
							try {new Long(ms[j]);} catch (Exception e) {continue;}
							String msg = "畅捷包装快捷渠道掉单，商户号【"+payRequest.payOrder.merno+"】，支付订单号【"+payRequest.payOrder.payordno+"】。";
							log.info(msg);
							new SMSUtil().send(ms[j], msg);
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.info(PayUtil.exceptionToString(e));
					}
					break;
				}
				if(payRequestReceive.receivePayRes.length()>0)break;
			}
			payRequest.respCode = payRequestReceive.respCode;
			payRequest.respDesc = payRequestReceive.respDesc;
			if(!"03".equals(payRequest.payOrder.ordstatus)){//非等待状态发通知
				payRequest.receivePayRes="";
				payRequest.payOrder.actdat = new Date();
				if(!"000".equals(payRequest.respCode)){
					payRequest.payOrder.ordstatus="02";
					payRequest.payOrder.bankerror = payRequest.respDesc;
	    		} else payRequest.payOrder.ordstatus="01";
				payRequest.payOrder.bankjrnno = payRequest.payOrder.payordno;
	        	new NotifyInterface().notifyMer(payRequest.payOrder);
			} else {
				//启动查询线程
				new SearchPLTPayResThread(payRequestReceive).start();
			}
		//银生宝（上线）
		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB").equals(payChannel.bankCode)){
			new YSBPayService().receivePaySingleInfo(payRequestReceive);
		//易联（上线）
		} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YL").equals(payChannel.bankCode)){
			new YLPayService().receivePaySingleInfo(payRequestReceive);
		} else throw new Exception("平台快捷代收通道空！");
		
	}
	public static void checkQuickSms(String orderNo,String code) throws Exception{
		PayQuickPaySmsDAO qsDAO = new PayQuickPaySmsDAO();
		PayQuickPaySms qs = qsDAO.detailPayQuickPaySms(orderNo);
		if(qs==null)throw new Exception("请发送校验码");
		if(!qs.code.equals(code)){
			qs.times--;
			if(qs.times <=0)qsDAO.removePayQuickPaySms(orderNo);
			else qsDAO.updateQsTimes(qs);
			throw new Exception("校验码错误");
		}
		qsDAO.removePayQuickPaySms(orderNo);
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
			PayReceiveAndPay rp = dao.detailPayReceiveAndPay(payOrder.payordno);
			if(rp == null)throw new Exception("没有对应的代收信息");
			//畅捷补单
			if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(rp.channelId)){
				new CJPayService().tranQueryForRepair(payOrder, rp);
				new NotifyInterface().notifyMer(payOrder);
			//银生宝补单
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB").equals(rp.channelId)){
				new YSBPayService().tranQueryForRepair(payOrder, rp);
				new NotifyInterface().notifyMer(payOrder);
			//易联
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YL").equals(rp.channelId)){
				new YLPayService().tranQueryForRepair(payOrder, rp);
				new NotifyInterface().notifyMer(payOrder);
			//金运通
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JYT").equals(rp.channelId)){
				
			} else throw new Exception("位置的代收包装渠道："+rp.channelId);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
//class QuickPaySms{
//	public QuickPaySms(String mobile){
//		this.mobile = mobile;
//		Random r = new Random();
//		code = String.valueOf(r.nextInt(100000000)+100000000);
//		code = code.substring(code.length()-6);
//		PLTPayService.PAY_SMS_POOL.put(mobile,this);
//	}
//	public String mobile;
//	public String code;
//	public int times=3;
//	public Date time=new Date();
//}
/**
 * 清理过期短信码线程
 * @author Administrator
 *
 */
class ClearQuickPaySms extends TimerTask{
	public static long existTime = 600;//短信默认存在时间
	static {
		try {existTime=Long.parseLong(PayConstant.PAY_CONFIG.get("QUICK_PAY_SMS_TIME"));} catch (Exception e) {}
	}
	public void run(){
		Date curT = new Date();
		String time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(curT.getTime()-existTime*1000));
		try {
			new PayQuickPaySmsDAO().clearQsBatch(time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
class SearchPLTPayResThread extends Thread{
	private static final Log log = LogFactory.getLog(SearchPLTPayResThread.class);
	PayRequest payRequest;
	public SearchPLTPayResThread(PayRequest payRequest){
		this.payRequest = payRequest;
	}
	public void run(){
		try {
			//查询处理结果,支付结果在其他线程中设置值
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
				if(payRequest.receivePayRes.length()>0)break;
			}
			payRequest.payOrder.actdat = new Date();
			payRequest.payOrder.bankjrnno = payRequest.payOrder.payordno;
			//查询未果
			if(payRequest.receivePayRes.length()==0){
				payRequest.payOrder.ordstatus="03";
				payRequest.respDesc = payRequest.respDesc+",未取得查询结果";
				payRequest.payOrder.bankerror = payRequest.respDesc;
				log.info("商户"+payRequest.merchantId+"，单号"+payRequest.merchantOrderId +"，未取得查询结果");
			} else {
				if("000".equals(payRequest.respCode))payRequest.payOrder.ordstatus="01";
				else {
					payRequest.payOrder.ordstatus="02";
					payRequest.payOrder.bankerror = payRequest.respDesc;
				}
			}
			//通知,保存订单信息
			new NotifyInterface().notifyMer(payRequest.payOrder);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
}
