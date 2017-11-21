package com.pay.merchantinterface.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.DataTransUtil;
import util.JWebConstant;
import util.PayUtil;
import util.Tools;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.dao.PayCoopBankDAO;
import com.pay.coopbank.service.PayCoopBankService;
import com.pay.fee.dao.PayFeeRate;
import com.pay.fee.service.PayFeeRateService;
import com.pay.merchantinterface.dao.PayRealNameAuthDAO;
import com.pay.order.dao.PayAccProfileDAO;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.risk.service.PayRiskDayTranSumService;

public class PayInterfaceOtherService {
	private static final Log log = LogFactory.getLog(PayInterfaceOtherService.class);
	/**
	 * 实名认证
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String realNameAuth(PayRequest payRequest) throws Exception{
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		try {
			//系统中查询曾经认证的用户（如果已经认证过，不再发送请求）
			Map map = new PayRealNameAuthDAO().getRealNameAuthedSuccessList(payRequest);
			for(int i=0; i<payRequest.receivePayList.size(); i++){
				PayReceiveAndPay rp = payRequest.receivePayList.get(i);
    			if(map.get(rp.accNo)!=null)rp.setRespInfo("000");
            }
			//畅捷
			if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(PayConstant.PAY_CONFIG.get("REAL_NAME_CHANNEL"))){
				new CJPayService().realNameAuth(payRequest);
				return mxu.realNameAuthToXml(payRequest);
			//富有
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY").equals(PayConstant.PAY_CONFIG.get("REAL_NAME_CHANNEL"))){
    			throw new Exception("认证通道暂未开通");
    		} throw new Exception("无可选择的认证通道");
		} catch (Exception e) {
			new Blog().info(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 取得代收付渠道
	 * @param payRequest
	 * @return
	 * @throws Exception 
	 */
	public PayCoopBank getRPChannel(PayRequest payRequest,Long amt) throws Exception{
		//取得代收付渠道
		String tranType = "";
		if("0".equals(payRequest.receivePayType))tranType="15";
		else if("1".equals(payRequest.receivePayType))tranType="16";
		String channelId = PayCoopBankService.MERCHANT_CHANNEL_MAP.get(payRequest.merchantId+","+tranType);//通过商户指定渠道获取
		PayCoopBank rpChannel = null;
		PayCoopBankDAO channelDAO = new PayCoopBankDAO();
		if(channelId != null)rpChannel = channelDAO.getChannelById(channelId);//取得商户指定通道
		if(rpChannel==null){//取得默认通道（通用指定）
			if("0".equals(payRequest.receivePayType))rpChannel = channelDAO.getChannelById(PayConstant.PAY_CONFIG.get("RP_RECEIVE_CHANNEL"));//代收
			else if("1".equals(payRequest.receivePayType))rpChannel = channelDAO.getChannelById(PayConstant.PAY_CONFIG.get("RP_PAY_CHANNEL"));//代付
		}
		if(rpChannel==null)rpChannel = new PayFeeRateService().getMinFeeChannelForReceivePay(payRequest,tranType,amt);//通过手续费最低取得
		if(rpChannel==null){
			if("CertPayConfirm".equals(payRequest.application))throw new Exception("平台快捷渠道错误（代收空）");
			else throw new Exception("代收付渠道错误（空）");
		}
		payRequest.rpChannel = rpChannel;
		return rpChannel;
	}
	/**
	 * 代收付
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String receivePaySingle(PayRequest payRequest) throws Exception{
		//检查商户余额是否充足
		List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
		PayCoopBank payChannel = getRPChannel(payRequest,Long.parseLong(payRequest.amount));
		payRequest.receiveAndPaySingle = addPayReceiveAndPaySingle(payRequest,"0");//代收付产生类型，0代收付，1快捷包装代收付
		list.add(payRequest.receiveAndPaySingle);
		checkMerchantAccountAmt(payRequest,list);
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		//畅捷
		if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(payChannel.bankCode)){
			new ReceivePayCJThread(payRequest,"single").start();
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//富有
		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY").equals(payChannel.bankCode)){
			throw new Exception("代收付通道暂未开通");
		//银生宝。
		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB").equals(payChannel.bankCode)){
			//单笔代收付。
			new YSBPayService().receivePaySingleInfo(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//易联单笔代扣。
		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YL").equals(payChannel.bankCode)){
			//单笔代收付。
			new YLPayService().receivePaySingleInfo(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		/*//随行付单笔代扣
		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXF").equals(payChannel.bankCode)){
			//单笔代收付。
			new SXFPayService().receivePaySingleInfo(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);*/
		//京东代付
		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD").equals(payChannel.bankCode)){
				//单笔代收付。
				new JDPayService().receivePaySingle(payRequest);
				PayRequest rTmp = new PayRequest();
				rTmp.application=payRequest.application;
				rTmp.version=payRequest.version;
				rTmp.merchantId=payRequest.merchantId;
				rTmp.tranId=payRequest.tranId;
				rTmp.timestamp=payRequest.timestamp;
				rTmp.setRespInfo("000");
				return mxu.receivePayToXml(rTmp);
		//随行付代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXF").equals(payChannel.bankCode)){
				//单笔代收付。
				new SXFpayService().receivePaySingle(payRequest);
				PayRequest rTmp = new PayRequest();
				rTmp.application=payRequest.application;
				rTmp.version=payRequest.version;
				rTmp.merchantId=payRequest.merchantId;
				rTmp.tranId=payRequest.tranId;
				rTmp.timestamp=payRequest.timestamp;
				rTmp.setRespInfo("000");
				return mxu.receivePayToXml(rTmp);
		//创新代付。
		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX").equals(payChannel.bankCode)){
			//单笔代收付。
			new CXPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//恒丰代收付。
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_HF").equals(payChannel.bankCode)){
			//单笔代收付。
			if("1".equals(payRequest.receivePayType)){//代付业务
				new HFPayService().receivePaySingle(payRequest);
				PayRequest rTmp = new PayRequest();
				rTmp.application=payRequest.application;
				rTmp.version=payRequest.version;
				rTmp.merchantId=payRequest.merchantId;
				rTmp.tranId=payRequest.tranId;
				rTmp.timestamp=payRequest.timestamp;
				rTmp.setRespInfo("000");
				return mxu.receivePayToXml(rTmp);
			}else{//代收业务。
				new HFPayService().receivePaySingleInfo(payRequest);
				PayRequest rTmp = new PayRequest();
				rTmp.application=payRequest.application;
				rTmp.version=payRequest.version;
				rTmp.merchantId=payRequest.merchantId;
				rTmp.tranId=payRequest.tranId;
				rTmp.timestamp=payRequest.timestamp;
				rTmp.setRespInfo("000");
				return mxu.receivePayToXml(rTmp);
			}
		//网上有名代付。
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_WSYM").equals(payChannel.bankCode)){
			//单笔代收付。
			new WSYMPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//支付通代收付。
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_ZFT").equals(payChannel.bankCode)){
				new ZFTPayService().receivePaySingleNew(payRequest);//
				PayRequest rTmp = new PayRequest();
				rTmp.application=payRequest.application;
				rTmp.version=payRequest.version;
				rTmp.merchantId=payRequest.merchantId;
				rTmp.tranId=payRequest.tranId;
				rTmp.timestamp=payRequest.timestamp;
				if(payRequest.respCode.equals("000")){
					rTmp.setRespInfo("000");
				}else{
					rTmp.setRespInfo("-1");
					rTmp.respDesc=payRequest.respDesc;
				}
				return mxu.receivePayToXml(rTmp);
		//天付宝代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TFB").equals(payChannel.bankCode)){
			new TFBpayService().receivePaySingle(payRequest);//
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			if(payRequest.respCode.equals("000")){
				rTmp.setRespInfo("000");
			}else{
				rTmp.setRespInfo("-1");
				rTmp.respDesc=payRequest.respDesc;
			}
			return mxu.receivePayToXml(rTmp);
		//首信易代付。
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXY").equals(payChannel.bankCode)){
			//单笔代收付。
			new SXYPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//高汇通代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT").equals(payChannel.bankCode)){
			//单笔代付。
			new GHTPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//邦付宝代付
		}else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB").equals(payChannel.bankCode)){
			new BFBPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		}
		//溢+代付
		else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_EMAX").equals(payChannel.bankCode)){
			new EjPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//亿联通付代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YLTF").equals(payChannel.bankCode)){
			//单笔代付。
			new YLTFPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//先锋代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equals(payChannel.bankCode)){
			//单笔代付。
			if("0".equals(payRequest.receivePayType)){//0：收款  1：付款
				new XFPayService().receivePaySingleInfo(payRequest);
			}else if("1".equals(payRequest.receivePayType)){
				new XFPayService().receivePaySingle(payRequest);
			}
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//联动代收付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD").equals(payChannel.bankCode)){
			if("0".equals(payRequest.receivePayType)){//0：收款  1：付款
				new LDPayService().receivePaySingleInfo(payRequest);
			} else if("1".equals(payRequest.receivePayType)){
				new LDPayService().receivePaySingle(payRequest);
			}
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//统统付代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TTF").equals(payChannel.bankCode)){
			//单笔代付
			new TTFPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//新酷宝代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KBNEW").equals(payChannel.bankCode)){
			new KBNEWPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
			//快乐付代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KLF").equals(payChannel.bankCode)){
			new KLFPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//新酷宝代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXFNEW").equals(payChannel.bankCode)){
			new SXFNEWpayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//易通支付代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YTZF").equals(payChannel.bankCode)){
			new YTZFPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//国付宝代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GFB").equals(payChannel.bankCode)){
			new GFBPayService().receivePaySingleNew(payRequest);//
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			if(payRequest.respCode.equals("000")){
				rTmp.setRespInfo("000");
			}else{
				rTmp.setRespInfo("-1");
				rTmp.respDesc=payRequest.respDesc;
			}
			return mxu.receivePayToXml(rTmp);
		//联通沃代付
		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LTW").equals(payChannel.bankCode)){
			new LTWPayService().receivePaySingle(payRequest);
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			if(payRequest.respCode.equals("000")){
				rTmp.setRespInfo("000");
			}else{
				rTmp.setRespInfo("-1");
				rTmp.respDesc=payRequest.respDesc;
			}
			return mxu.receivePayToXml(rTmp);
		}else throw new Exception("无可选择的代收付通道");
	}
	/**
	 * 代收付
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String receivePayBatch(PayRequest payRequest) throws Exception{
		//取得渠道
		long totalAmt = 0l;
		for(int i = 0; i<payRequest.receivePayList.size(); i++){
			PayReceiveAndPay rp = payRequest.receivePayList.get(i);
    		totalAmt = totalAmt + rp.amount;
		}
		PayCoopBank payChannel = getRPChannel(payRequest,totalAmt);
		//添加，赋值
		addPayReceiveAndPayBatch(payRequest);
		//检查商户余额是否充足
		checkMerchantAccountAmt(payRequest,payRequest.receivePayList);
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		//畅捷
		if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(payChannel.bankCode)){
			new ReceivePayCJThread(payRequest,"batch").start();
			PayRequest rTmp = new PayRequest();
			rTmp.application=payRequest.application;
			rTmp.version=payRequest.version;
			rTmp.merchantId=payRequest.merchantId;
			rTmp.tranId=payRequest.tranId;
			rTmp.timestamp=payRequest.timestamp;
			rTmp.setRespInfo("000");
			return mxu.receivePayToXml(rTmp);
		//富有
		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY").equals(payChannel.bankCode)){
			throw new Exception("代收付通道暂未开通");
		} throw new Exception("无可选择的批量代收付通道");
	}
	/**
	 * 风控检查&检查扣除金额
	 * @param payRequest
	 * @param list
	 * @throws Exception
	 */
	public long checkMerchantAccountAmt(PayRequest payRequest,List<PayReceiveAndPay> list) throws Exception{
		//添加风控汇总信息
		new PayRiskDayTranSumService().updateRiskReceivePaySum(payRequest,list);
		long totalAmt = 0, totalFee=0;
		for(int i = 0; i<list.size(); i++){
			PayReceiveAndPay rp = list.get(i);
			if(!"000".equals(rp.respCode))continue;
			totalAmt = totalAmt + rp.amount;
			totalFee = totalFee + rp.fee;
		}
		payRequest.merchant.accProfile = new com.pay.merchant.dao.PayAccProfileDAO()
			.detailPayAccProfileByAcTypeAndAcNo(PayConstant.CUST_TYPE_MERCHANT,payRequest.merchantId);
		try {
			if("0".equals(payRequest.receivePayType)){//代收
				//代收金额+手续费余额<手续费，提示余额不足
				if(totalAmt+payRequest.merchant.accProfile.cashAcBal<totalFee)throw new Exception("支付平台虚拟账户余额不足");
			} else {//代付
				if(payRequest.merchant.accProfile.cashAcBal<totalAmt+totalFee)throw new Exception("支付平台虚拟账户余额不足");
				//扣除金额
				if(new PayAccProfileDAO().updatePayAccProfileAmtByCustId(list,PayConstant.CUST_TYPE_MERCHANT,
					payRequest.merchantId, 0-totalAmt-totalFee)==0)throw new Exception("扣款失败，支付平台虚拟账户余额不足");
			}
		} catch (Exception e) {
			for(int i = 0; i<list.size(); i++){
				PayReceiveAndPay rp = list.get(i);
				rp.status="2";
				rp.retCode="-1";
				rp.errorMsg = e.getMessage();
			}
			new PayReceiveAndPayDAO().updatePayReceiveAndPay(list);
			e.printStackTrace();
			throw e;
		}
		return totalAmt;
	}
	/**
	 * 代收付查询
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String receivePayQuery(PayRequest payRequest) throws Exception{
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		new PayReceiveAndPayDAO().receivePayQuery(payRequest);
		payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		payRequest.setRespInfo("000");
		return mxu.receivePayQueryToXml(payRequest);
	}
	/**
	 * 代收付通知
	 * @param payRequest
	 * @param notifyList
	 * @throws Exception 
	 */
	public void receivePayNotify(PayRequest payRequest,List<PayReceiveAndPay> notifyList) throws Exception{
		//根据操作结果，失败扣费补回去
		long totalAmt=0,totalFee=0;
		List <PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
		for(int i = 0; i<notifyList.size(); i++){
			PayReceiveAndPay rp = notifyList.get(i);
			if("0".equals(payRequest.receivePayType)){//收款
				if(!"000".equals(rp.respCode))continue;//收款失败
				list.add(rp);
			} else if("1".equals(payRequest.receivePayType)){//付款
				if("000".equals(rp.respCode)){
					list.add(rp);
					continue;//付款失败
				}
			}
			totalAmt = totalAmt + rp.amount;
			totalFee = totalFee + rp.fee;
		}
		long optAmt = totalAmt+totalFee;
		if("0".equals(payRequest.receivePayType)){//代收
			optAmt = totalAmt- totalFee;
			//自动结算到虚拟账户，并且T+0的情况，更新账户余额
			if("3".equals(payRequest.merchant.settlementWay)//结算方式 0自动结算到虚拟账户 1线下打款 2自动结算到银行账户 3实时结算到虚拟账户  4实时结算到银行账户
				&&"D".equals(payRequest.merchant.custSetPeriodDaishou)
				&&"0".equals(payRequest.merchant.custStlTimeSetDaishou)){
				new PayAccProfileDAO().updatePayAccProfileAmtByCustId(
						PayConstant.CUST_TYPE_MERCHANT,payRequest.merchantId,optAmt);
				//更新代收记录
				new PayReceiveAndPayDAO().updatePayReceiveAndPayForStlsts(list);
			}
		} else {//代付，失败的金额补回去
//			new PayAccProfileDAO().updatePayAccProfileAmtByCustId(
//					PayConstant.CUST_TYPE_MERCHANT,payRequest.merchantId,optAmt);
		}
		String notifyXml = new MerchantXmlUtil().receivePayNotifyToXml(payRequest,notifyList);
		if(!"ReceivePay".equals(payRequest.application)&&!"ReceivePayBatch".equals(payRequest.application))return;
		//风控汇总
		new PayRiskDayTranSumService().updateRiskReceivePaySumForSuccess(payRequest,notifyList);
		try {
			if(payRequest.receivePayNotifyUrl!=null && payRequest.receivePayNotifyUrl.startsWith("http")){
				log.info("代收付通知======="+payRequest.receivePayNotifyUrl+"========\n"+notifyXml);
				new DataTransUtil().doPost(payRequest.receivePayNotifyUrl, CertUtil.createTransStr(notifyXml).getBytes("utf-8"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
	/**
	 * 添加代收付
	 * @param payRequest
	 * @param bussFromType 代收付产生类型，0代收付，1快捷包装代收付
	 * @return
	 * @throws Exception
	 */
	public PayReceiveAndPay addPayReceiveAndPaySingle(PayRequest payRequest,String bussFromType) throws Exception{
		PayReceiveAndPay payReceiveAndPay = new PayReceiveAndPay();
    	PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);
    	payReceiveAndPay.sn = payRequest.merchantId+"_"+Tools.getUniqueIdentify();
		payReceiveAndPay.respCode=payRequest.respCode;
		payReceiveAndPay.accType = payRequest.accType;
		payReceiveAndPay.accNo = payRequest.accNo;
		payReceiveAndPay.accName = payRequest.accName;
		payReceiveAndPay.credentialType = payRequest.credentialType;
		payReceiveAndPay.credentialNo = payRequest.credentialNo;
		//快捷代收付情况，代收id和订单号相同
		if(payRequest.payOrder!=null&&payRequest.payOrder.payordno!=null
			&&payRequest.payOrder.payordno.length()>0)payReceiveAndPay.id=payRequest.payOrder.payordno;
		else payReceiveAndPay.id=Tools.getUniqueIdentify();
		payReceiveAndPay.type=payRequest.receivePayType;
		payReceiveAndPay.custType=PayConstant.CUST_TYPE_MERCHANT;
		payReceiveAndPay.custId=payRequest.merchantId;
		payReceiveAndPay.channelId=payRequest.rpChannel==null?"_":payRequest.rpChannel.bankCode;
		payReceiveAndPay.merTranNo=payRequest.tranId;
		payReceiveAndPay.channelTranNo=payRequest.merchantId+"_"+payRequest.tranId;
		payReceiveAndPay.accountProp=payRequest.accountProp;
		payReceiveAndPay.timeliness="0";
		payReceiveAndPay.appointmentTime="";
		payReceiveAndPay.subMerchantId="";
		payReceiveAndPay.accountType="0".equals(payRequest.accountProp)?cardBin.cardType:"";
		payReceiveAndPay.accountNo=payRequest.accNo;
		payReceiveAndPay.accountName=payRequest.accName;
		payReceiveAndPay.bankName=payRequest.bankName;
		payReceiveAndPay.bankCode=payRequest.bankId;
		payReceiveAndPay.bankId = payRequest.bankId;
		payReceiveAndPay.protocolNo=payRequest.protocolNo;
		payReceiveAndPay.currency="CNY";
		payReceiveAndPay.amount=Long.parseLong(payRequest.amount);
		payReceiveAndPay.idType=payRequest.credentialType;
		payReceiveAndPay.certId=payRequest.credentialNo;
		payReceiveAndPay.tel=payRequest.tel;
		payReceiveAndPay.summary=payRequest.summary;
		if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){//测试模式
			payReceiveAndPay.status="1";
			payReceiveAndPay.setRespCode("000");
		} else {
			payReceiveAndPay.status="0";
			payReceiveAndPay.retCode="074";
			payReceiveAndPay.errorMsg=PayConstant.RESP_CODE_DESC.get(payReceiveAndPay.retCode);
		}
		PayFeeRate feeRate = null;
		if("0".equals(payRequest.receivePayType))feeRate=payRequest.merchant.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+payRequest.merchant.custId+",15");
		else if("1".equals(payRequest.receivePayType))feeRate=payRequest.merchant.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+payRequest.merchant.custId+",16");
		Object [] feeInfo = PayFeeRateService.getFeeByFeeRate(feeRate,payReceiveAndPay.amount);
		payReceiveAndPay.feeCode=feeRate!=null?feeRate.feeCode:"";
		payReceiveAndPay.feeInfo=feeRate!=null?(String)feeInfo[1]:"";
		payReceiveAndPay.batNo=payRequest.merchantId+"_"+payRequest.tranId;
		payReceiveAndPay.bankGeneralName=payRequest.bankGeneralName;//?
		payReceiveAndPay.fee=(Long)(feeInfo[0]);
		payReceiveAndPay.feeChannel=0l;
		payReceiveAndPay.tranType="0";
		payReceiveAndPay.receivePayNotifyUrl=payRequest.receivePayNotifyUrl;
		payReceiveAndPay.bussFromType = bussFromType;
		new PayReceiveAndPayDAO().addPayReceiveAndPay(payReceiveAndPay);
		return payReceiveAndPay;
    }
    public void addPayReceiveAndPayBatch(PayRequest payRequest) throws Exception{
    	for(int i = 0; i<payRequest.receivePayList.size(); i++){
			PayReceiveAndPay rp = payRequest.receivePayList.get(i);
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(rp.accNo);
			rp.id=Tools.getUniqueIdentify();
    		rp.type=payRequest.receivePayType;
    		rp.custType=PayConstant.CUST_TYPE_MERCHANT;
    		rp.custId=payRequest.merchantId;
    		rp.channelId=payRequest.rpChannel == null?"_":payRequest.rpChannel.bankCode;
    		rp.merTranNo=payRequest.tranId;
    		rp.channelTranNo=payRequest.merchantId+"_"+payRequest.tranId;
    		rp.accountProp=payRequest.accountProp;
    		rp.timeliness="0";
    		rp.appointmentTime="";
    		rp.sn=payRequest.merchantId+"_"+rp.sn;
    		rp.subMerchantId="";
    		rp.accountType="0".equals(payRequest.accountProp)?cardBin.cardType:"";
    		rp.accountNo=rp.accNo;
    		rp.accountName=rp.accName;
    		rp.bankCode=rp.bankId;
    		rp.currency="CNY";
    		rp.idType=rp.credentialType;
    		rp.certId=rp.credentialNo;
    		//测试模式和生产模式
    		rp.status="0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))?"1":"0";
    		rp.setRespCode("000");
    		PayFeeRate feeRate = null;
    		if("0".equals(payRequest.receivePayType))feeRate=payRequest.merchant.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+payRequest.merchant.custId+",15");
    		else if("1".equals(payRequest.receivePayType))feeRate=payRequest.merchant.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+payRequest.merchant.custId+",16");
    		Object [] feeInfo = PayFeeRateService.getFeeByFeeRate(feeRate,rp.amount);
    		rp.feeCode=feeRate!=null?feeRate.feeCode:"";
    		rp.feeInfo=feeRate!=null?(String)feeInfo[1]:"";
    		rp.batNo=payRequest.merchantId+"_"+payRequest.tranId;
    		rp.fee=(Long)(feeInfo[0]);
    		rp.feeChannel=0l;
    		rp.errorMsg=PayConstant.RESP_CODE_DESC.get(rp.retCode);
    		rp.tranType="1";
    		rp.receivePayNotifyUrl=payRequest.receivePayNotifyUrl;
		}
    	new PayReceiveAndPayDAO().addPayReceiveAndPayBatch(payRequest);
    }
}
class ReceivePayCJThread extends Thread {
	private static final Log log = LogFactory.getLog(ReceivePayCJThread.class);
	PayRequest payRequest;
	String flag = "";
	public ReceivePayCJThread(PayRequest payRequest,String flag){
		this.payRequest = payRequest;
		this.flag = flag;
	}
	public void run(){
		try {
			if("single".equals(flag))new CJPayService().receivePaySingle(payRequest);
			else if("batch".equals(flag))new CJPayService().receivePayBatch(payRequest);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
}
