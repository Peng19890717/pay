package com.pay.merchantinterface.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.Tools;
import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jweb.dao.Blog;
import com.jweb.service.TransactionService;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.service.PayCoopBankService;
import com.pay.margin.service.PayMarginService;
import com.pay.merchant.dao.PayAccProfile;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.order.dao.PayAccProfileDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayProductOrderDAO;
import com.pay.refund.dao.PayRefund;
import com.pay.refund.dao.PayRefundDAO;
import com.pay.refund.service.PayRefundService;
import com.pay.risk.service.PayRiskDayTranSumService;
import com.pay.settlement.dao.PayMerchantSettlement;
import com.pay.settlement.dao.PayMerchantSettlementDAO;
import com.pay.settlement.service.PayAutoSettlementListener;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.third.ght.RSA;
import com.third.ld.SignUtil;

public class PayOrderInterfaceService extends TransactionService{
	private static final Log log = LogFactory.getLog(PayOrderInterfaceService.class);
	public void addOrder(PayMerchant merchant,PayRequest payRequest,
			PayProductOrder prdOrder,PayOrder order) throws Exception{
		PayProductOrderDAO productOrderDAO = new PayProductOrderDAO();
		PayOrderDAO payOrderDAO = new PayOrderDAO();
		PayTranUserQuickCardService payTranUserQuickCardService = new PayTranUserQuickCardService();
		PayRiskDayTranSumService payRiskDayTranSumService = new PayRiskDayTranSumService();
		try {
			transaction.beignTransaction(productOrderDAO,payOrderDAO,payTranUserQuickCardService,payRiskDayTranSumService);
			//0初次支付1重复支付
			if("0".equals(payRequest.payMode)){
				prdOrder.id=Tools.getUniqueIdentify();//记录ID
				prdOrder.prdordno=payRequest.merchantOrderId;//商品订单号
				prdOrder.merno=payRequest.merchantId;//商户编号
				prdOrder.versionid=payRequest.version;//版本号
				if(payRequest.guaranteeAmt.length()!=0&&Long.parseLong(payRequest.guaranteeAmt)>0)prdOrder.prdordtype="2";
				else prdOrder.prdordtype=
					payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))||
					payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))||
					payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))?"1":"0";//商品订单类型0-消费；1-充值2-担保支付3商户充值
				prdOrder.biztype=payRequest.bizType;//业务类型01:团购,02:网络游戏,03:医疗保健,04:教育培训,05:日常用品,06:鲜花礼品,07:旅游票务,08:影视娱乐,09:支付宝业务,10:图书音像,11:全国公共事业缴费,12:电子产品,13:交通违章罚款,14:网站建设,15:信用卡还款,16:会员论坛,17:软件产品,18:身份证查询,19:购买支付通业务,20:运动休闲,21:彩票,22:数字点卡,23:其它
				prdOrder.ordamt=Long.parseLong(payRequest.merchantOrderAmt);//订单金额
				prdOrder.ordstatus="00";//订单状态 00:未支付01:支付成功02:支付处理中03:退款审核中04:退款处理中05:退款成功06:退款失败07:撤销审核中08：同意撤销09：撤销成功10:撤销失败11：订单作废12预付款13延迟付款审核中14冻结15同意延迟付款16拒绝延迟付款17实付审核中18实付审核通过19实付审核拒绝99:超时
				prdOrder.ordccy="CNY";//货币类型
				prdOrder.notifyurl=payRequest.merchantNotifyUrl;//异步通知URL
				prdOrder.returl=payRequest.merchantFrontEndUrl;//同步返回URL
				prdOrder.receivePayNotifyUrl = payRequest.receivePayNotifyUrl;
				prdOrder.signature=payRequest.sign;//签名信息
				prdOrder.signtype="RSA";//签名方式
				prdOrder.verifystring=Base64.encode(payRequest.reqXml.getBytes("utf-8"));//验证摘要字符串
				prdOrder.prddisurl="";//商品展示网址
				prdOrder.prdname=payRequest.merchantOrderDesc;//商品名称
				prdOrder.prdshortname=payRequest.merchantOrderDesc;//商品简称
				prdOrder.prddesc=payRequest.merchantOrderDesc;//商品描述
				prdOrder.merremark=payRequest.merchantName;//商户备注
				prdOrder.rpttype="";//收款方式
				prdOrder.prdunitprice=Long.parseLong(payRequest.merchantOrderAmt);//商品单价
				prdOrder.buycount=1l;//购买数量
				prdOrder.defpayway="CertPayOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)?"03":"01";//默认支付方式00支付账户01网上银行02终端03快捷支付04混合支付05支票/汇款06三方支付，默认01
				prdOrder.custId=payRequest.payerId;//客户ID
				prdOrder.merpayacno="";//卖方支付账号
				prdOrder.sellrptacno=payRequest.salerId;//卖方收款账号
				prdOrder.sellrptamt=0l;//卖方收款金额
				prdOrder.paymode=payRequest.payMode;//付款类型0初次支付1重复支付
				prdOrder.custpayacno=payRequest.payerId.length()==0?payRequest.credentialNo:payRequest.payerId;//买方支付账号
				prdOrder.custrptacno="CertPayOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)?payRequest.cardNo:"";//买方收款账号
				prdOrder.transort="";//交易分类暂不使用
				prdOrder.noncashamt=0l;//非现金金额
				prdOrder.acjrnno="";//清算流水号
				prdOrder.txncomamt=0l;//交易佣金
				prdOrder.rftotalamt=0l;//已退款累计金额
				prdOrder.rfcomamt=0l;//已退款佣金
				prdOrder.bankerror="";//银行失败原因
				prdOrder.payerror="";//支付失败原因
				prdOrder.stlsts="";//结算状态
				prdOrder.stlbatno="";//结算批次号
				prdOrder.cpsflg="";//CPS返利标识
				prdOrder.payordno="";//支付订单号
				prdOrder.ordertime = payRequest.curTime;
				prdOrder.prddesc = payRequest.msgExt;
				prdOrder.mobile = payRequest.userMobileNo;
				prdOrder.credentialType = payRequest.credentialType;
				prdOrder.credentialNo = payRequest.credentialNo;
				//充值类别（充值使用），0用户充值 1商户账户充值 2预存手续费充值
				if(prdOrder.merno.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC")))prdOrder.rechargeType="0";
				else if(prdOrder.merno.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER")))prdOrder.rechargeType="1";
				else if(prdOrder.merno.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE")))prdOrder.rechargeType="2";
				else prdOrder.rechargeType="";
				productOrderDAO.addPayProductOrder(prdOrder);
			}
			order.payordno=Tools.getUniqueIdentify();//支付订单号
			order.ordstatus="00";//'00'	订单状态，00:等待付款  01:付款成功  02:付款失败 03:退款申请 04:等待退款 05:退款成功 06:退款失败 07:同意撤销 08：拒绝撤销  09：撤销成功 10:撤销失败 12 预付款  17 实付审核中 18 实付审核通过 19 实付审核拒绝 99:超时，默认00
			if(payRequest.guaranteeAmt.length()!=0&&Long.parseLong(payRequest.guaranteeAmt)>0)prdOrder.prdordtype="2";
			else order.prdordtype=
				payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))||
				payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER"))||
				payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_MER_CHARGE"))?"1":"0";//商品订单类型0-消费；1-充值2-担保支付3商户充值
			if("CertPayOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)){
				order.paytype="03";//支付方式，00 支付账户   01 网上银行    02 终端   03 快捷支付    04 混合支付    05 支票/汇款 07微信扫码 10微信WAP、11支付宝扫码、12QQ扫码
				order.multype="03";//混合支付方式，01 网上银行    02 终端   03 快捷支付
			} else if("WeiXinScanOrder".equals(payRequest.application)){
				order.paytype="07";//支付方式，00 支付账户   01 网上银行    02 终端   03 快捷支付    04 混合支付    05 支票/汇款 07微信扫码 10微信WAP、11支付宝扫码、12QQ扫码
				order.multype="07";//混合支付方式，01 网上银行    02 终端   03 快捷支付
			} else if("WeiXinWapOrder".equals(payRequest.application)){
				order.paytype="10";//支付方式，00 支付账户   01 网上银行    02 终端   03 快捷支付    04 混合支付    05 支票/汇款 07微信扫码 10微信WAP、11支付宝扫码、12QQ扫码
				order.multype="10";//混合支付方式，01 网上银行    02 终端   03 快捷支付
			} else if("ZFBScanOrder".equals(payRequest.application)){
				order.paytype="11";//支付方式，00 支付账户   01 网上银行    02 终端   03 快捷支付    04 混合支付    05 支票/汇款 07微信扫码 10微信WAP、11支付宝扫码、12QQ扫码
				order.multype="11";//混合支付方式，01 网上银行    02 终端   03 快捷支付
			} else if("QQScanOrder".equals(payRequest.application)){
				order.paytype="12";//支付方式，00 支付账户   01 网上银行    02 终端   03 快捷支付    04 混合支付    05 支票/汇款 07微信扫码 10微信WAP、11支付宝扫码、12QQ扫码
				order.multype="12";//混合支付方式，01 网上银行    02 终端   03 快捷支付
			} else {
				order.paytype="01";//支付方式，00 支付账户   01 网上银行    02 终端   03 快捷支付    04 混合支付    05 支票/汇款 07微信扫码 10微信WAP、11支付宝扫码、12QQ扫码
				order.multype="01";//混合支付方式，01 网上银行    02 终端   03 快捷支付
			}
			order.bankcod=payRequest.bankId==null?"":payRequest.bankId;//银行编码，银行卡所属银行
			order.paybnkcod="";//收款银行编码
			order.bankjrnno="";//银行流水号
			order.txccy="CNY";//'CNY'	支付货币，默认：CNY（人民币）
			order.txamt=Long.parseLong(payRequest.merchantOrderAmt);//支付金额，单位：分
			order.accamt=0l;//虚拟账户支付金额，单位：分
			order.mulamt=0l;//第二种支付方式金额，单位：分
			order.fee=0l;//支付手续费，单位：分
			order.netrecamt=Long.parseLong(payRequest.merchantOrderAmt);//实收净额，单位：分
			order.payacno=payRequest.cardNo;//银行付款账号
			order.bankpayacno=payRequest.cardNo;//银行付款账号
			order.bankpayusernm=payRequest.userName;//银行付款户名
			order.bankerror="";//银行返回出错原因
			order.backerror="";//后台返回出错原因
			order.merno=prdOrder.merno;//商户编号
			order.prdordno=prdOrder.prdordno;//商品订单号
			order.notifyurl=PayConstant.PAY_CONFIG.get("server_notify_url");//异步通知URL
			order.custId=payRequest.payerId;//客户ID
			order.payret="";//银行支付状态，00 成功   01 失败
			order.prdname=payRequest.merchantOrderDesc;//商品名称
			order.merremark=payRequest.merchantName;//商户信息
			order.bnkjnl=order.payordno;//银行网关订单号
			order.signmsg=payRequest.sign;//签名信息
			order.bustyp=payRequest.userType;//业务类型（1-B2C 2-B2B）
			order.bnkmerno="";//银行商户号
			order.tracenumber="";//系统跟踪号
			order.cbatno="";//批次号
			order.srefno="";//系统参考号
			order.termno="";//终端号
			order.trantyp="";//交易类型
			order.bankCardType = payRequest.accountType;//0借记卡/1贷记卡/2准贷记卡/3预付卡/4对公
			//order.termtyp="";//终端类型，终端类型，0 pc，1手机，2 pos,3 其他 默认0
			order.credno="";//信用卡卡号
			order.versionno="";//服务版本号
			order.payacno="CertPayOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)?payRequest.cardNo:"";//支付账号
			order.createtime = prdOrder.ordertime;
			payOrderDAO.addPayOrder(order);
			//快捷支付保存卡信息 
			if("CertPayOrder".equals(payRequest.application))payTranUserQuickCardService.addPayTranUserQuickCard(payRequest);
			//商户交易汇总更新
			payRiskDayTranSumService.updateRiskTranSum(payRequest);
			//汇总用户非快捷
			if("CertPayOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)){
				if(payRequest.cardNo.length()>0)payRiskDayTranSumService.updateRiskTranSumForQuick(payRequest);
			} else {
				//汇总用户支付信息
				if(payRequest.tranUserMap.get(payRequest.payerId) != null && !payRequest.isSaveUserSum){
					payRiskDayTranSumService.updateRiskTranSumForUser(payRequest);
					payRequest.isSaveUserSum = true;
				}
			}
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	public String queryOrder(PayRequest payRequest) throws Exception {
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		//商品订单
		PayProductOrder prdOrder = new PayProductOrderDAO().getProductOrderById(payRequest.merchantId,payRequest.merchantOrderId);
		//订单不存在
		if(prdOrder == null) return mxu.queryOrderToXml(payRequest,null,null,null,null);
		//取得支付列表
		List orderList = new PayInterfaceDAO().getPayOrderListByPrdordno(payRequest.merchantId,payRequest.merchantOrderId);
		//取得退款列表
		List refundList = new PayRefundDAO().getRefundListByMerchantOrderId(payRequest.merchantId,payRequest.merchantOrderId);
		//取得撤销列表
		List cancelList = new ArrayList();
		//封装通知数据
		return mxu.queryOrderToXml(payRequest,prdOrder,orderList,refundList,cancelList);
	}
	/**
	 * 订单退款，担保失败，也要发起退款
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String refundOrder(PayRequest payRequest) throws Exception{
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		PayProductOrderDAO prdDao = new PayProductOrderDAO();
		PayInterfaceDAO interfaceDao = new PayInterfaceDAO();
		PayRefundDAO refundDao = new PayRefundDAO();
		PayRiskDayTranSumService payRiskDayTranSumService = new PayRiskDayTranSumService();
		PayRefund payRefund = new PayRefund();
		PayRefundService refundService = new PayRefundService();
		try {
			transaction.beignTransaction(prdDao,interfaceDao,refundDao,payRiskDayTranSumService,refundService);
			//商品订单
			PayProductOrder prdOrder = prdDao.getProductOrderById(payRequest.merchantId,payRequest.merchantOrderId);
			//结算时间检查，结算前10中，不允许退款请求，结算过程中不允许退款
			Date curDate = new Date();
			Date t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").
				format(curDate) + " " + PayConstant.PAY_CONFIG.get("settlement_time"));
			long tmp = t.getTime() - curDate.getTime();
			if(PayAutoSettlementListener.auto_run_flag||(tmp>=0 && tmp<=1000*60*10)){
				payRequest.setRespInfo("024");
				return mxu.refundOrderToXml(payRequest);
			}
			//商品订单不存在
			if(prdOrder == null){
				payRequest.setRespInfo("008");
				return mxu.refundOrderToXml(payRequest);
			}
			//0未结算，1已结算，或者银行已经清算
	//		if(prdOrder.stlsts != null && prdOrder.stlsts.length()!=0 && !"0".equals(prdOrder.stlsts)){
	//			payRequest.setRespInfo("009");
	//			return mxu.refundOrderToXml(payRequest);
	//		}
			//未支付成功
			if(!"01".equals(prdOrder.ordstatus)){
				payRequest.setRespInfo("010");
				return mxu.refundOrderToXml(payRequest);
			}
			//退款总金额大于支付金额=======开始=========
			long txnamt = 0l,refundAmt = 0,refundAmtSum=new PayInterfaceDAO().
					getPrdRefundAmtSum(payRequest.merchantId, payRequest.merchantOrderId);
			try {refundAmt = Long.parseLong(payRequest.merchantRefundAmt);} catch (Exception e) {}
			List orderList = interfaceDao.getPayOrderListByPrdordno(payRequest.merchantId,payRequest.merchantOrderId);
			PayOrder payOrder = null;
			for(int i = 0;i<orderList.size(); i++){
				payOrder = (PayOrder)orderList.get(i);
				//支付成功订单金额求和
				if("01".equals(payOrder.ordstatus))txnamt = txnamt+payOrder.txamt;
			}
			if(txnamt < refundAmt + refundAmtSum || txnamt==0){
				payRequest.setRespInfo("011");
				return mxu.refundOrderToXml(payRequest);
			}
			//退款总金额大于支付金额=======结束=========
			payRefund.banksts="00";//银行网关状态，00:退款处理中01:退款成功02：退款失败，默认00
			//已经结算，扣除账户余额
			payRefund.rfsake="";//退款理由
			payRefund.stlsts="0";//清结算状态 0初始 1清算完成 2结算完成 默认0
			if("D".equals(payRequest.merchant.custSetPeriod)&&"0".equals(payRequest.merchant.custSetFrey)){
				try {
					PayAccProfile payAccProfileTmp = new com.pay.merchant.dao.PayAccProfileDAO().detailPayAccProfileByAcTypeAndAcNo("1",payRequest.merchantId);
		        	if(payAccProfileTmp!=null)log.info(payAccProfileTmp.toString().replaceAll("\n",";"));
				} catch (Exception e) {}
				PayAccProfile payAccProfile = new PayAccProfile();
				payAccProfile.acType=PayConstant.CUST_TYPE_MERCHANT;
				payAccProfile.payAcNo = payRequest.merchant.custId;
				payAccProfile.cashAcBal = refundAmt;
				payAccProfile.consAcBal = refundAmt;
				if(new com.pay.merchant.dao.PayAccProfileDAO().updatePayAccProfileForRefund(payAccProfile)==0){
					payRequest.respCode="-1";
					payRequest.respDesc="余额不足，商户【"+payRequest.merchant.storeName+"】";
					return mxu.refundOrderToXml(payRequest);
				}
				payRefund.rfsake="账户余额退款";//退款理由
				payRefund.stlsts="2";
			}
			payRefund.refordno=Tools.getUniqueIdentify();//退款订单号
			payRefund.oriprdordno=prdOrder.prdordno;//原商品订单号
			payRefund.oripayordno=payOrder.payordno;//原支付订单号
			payRefund.rfpayordno="";//退款支付订单号
			payRefund.bankcode="";//银行编码
			payRefund.getbankcode="";//收款银行编码
			payRefund.custpayacno="";//买家支付账号
			payRefund.bankpayusernm="";//收款账号户名
			payRefund.merpayacno="";//卖方支付账号
			payRefund.custId="";//客户ID
			payRefund.txccy="CNY";//支付货币,默认：CNY
			payRefund.rfamt=refundAmt;//退款金额
			payRefund.fee=0l;//手续费
			payRefund.netrecamt=refundAmt;//实收净额
			payRefund.ordstatus="00";//订单状态，00:未处理  01:退款处理中  02:退款成功  03:退款失败  04:已退回支付账号  05:已重新退款99:超时，默认00
			payRefund.obankjrnno="";//原银行流水号
			payRefund.bankjrnno="";//银行流水号
			payRefund.returl="";//同步通知URL
			payRefund.notifyurl="";//异步通知URL
			payRefund.rfordtime = payRequest.curTime;
			payRefund.bankerror="";//银行返回错误原因
			payRefund.merno=payRequest.merchantId;//商户编号
			payRefund.stlbatno="";//结算批次号
			payRefund.bustyp="";//业务类型,01-B2C02-B2B
			payRefund.operId="";//操作员
			payRefund.filed1="";//备注1
			payRefund.filed2="";//备注2
			payRefund.filed3="";//备注3
			payRefund.filed4="";//备注4
			payRefund.filed5="";//备注5
			if(refundDao.addPayRefund(payRefund)>0){//添加退款订单 修改商户订单退款状态  修改支付订单退款状态
				//发起线上退款（判断渠道是否支持线上退款）
				PayCoopBank channel = PayCoopBankService.CHANNEL_MAP.get(payOrder.payChannel);
				//是否支持线上退款,0支持，1不支持
				if(channel != null && "0".equals(channel.refundOnline)){
					//融保线上退款
					if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equals(channel.bankCode)){
//						new RBPayService().refund(payRefund);
//						payRefund.operId="admin";
//			            payRefund.rfsake = "线上退款";
//			            refundService.setResultRefund(payRefund);
					}
					//畅捷线上退款。
					if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(channel.bankCode)){
						new CJPayService().refund(payRefund);
						payRefund.operId="admin";
			            payRefund.rfsake = "线上退款";
			           // refundService.setResultRefund(payRefund);
					}
				}
				//交易汇总更新
				payRiskDayTranSumService.updateRiskRefundSum(payRefund);
				//退款成功，更新成功交易更新汇总
				if( "01".equals(payRefund.banksts)){
					payRiskDayTranSumService.updateRiskSuccessRefundSum(payRefund);
				}
				//退款成功，重新计算手续费（商户手续费、代理手续费）TODO
				payRequest.setRespInfo("000");
				transaction.endTransaction();
				return mxu.refundOrderToXml(payRequest);
			} else {
				payRequest.setRespInfo("-1");
				transaction.endTransaction();
				return mxu.refundOrderToXml(payRequest);
			}
		} catch (Exception e) {
			new Blog().info(payRefund.toString());
			transaction.rollback();
			throw e;
		}
	}
	/**
	 * 快捷支付短信验证码重发
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String sendCertPayReSms(PayRequest payRequest) throws Exception{
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		PayProductOrderDAO prdDao = new PayProductOrderDAO();
		try {
			//订单信息
			PayProductOrder prdOrder = prdDao.getProductOrderById(payRequest.merchantId,payRequest.merchantOrderId);
			PayOrder payOrder = new PayOrderDAO().getPayOrderByMerInfo(payRequest.merchantId,payRequest.merchantOrderId);
			//订单不存在
			if(prdOrder==null || payOrder==null){
				payRequest.setRespInfo("008");
				return mxu.certPayReSmsToXml(payRequest);
			}
			//非快捷支付订单
			if(!"03".equals(payOrder.paytype)){
				payRequest.setRespInfo("057");
				return mxu.certPayReSmsToXml(payRequest);
			}
			//订单已经支付成功
			if("01".equals(payOrder.ordstatus)){
				payRequest.setRespInfo("014");
				return mxu.certPayReSmsToXml(payRequest);
			}
			payRequest.productOrder = prdOrder;
			payRequest.payOrder = payOrder;
			//短信验证码重发
			//先锋支付
			if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equals(payOrder.payChannel)){
				payRequest.setRespInfo("058");
				payRequest.respDesc+="（"+payOrder.payChannel+"）";
				return mxu.certPayReSmsToXml(payRequest);
			//摩宝支付
			} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_MOBAO").equals(payOrder.payChannel)){
				payRequest.setRespInfo("059");
				payRequest.respDesc+="（"+payOrder.payChannel+"）";
				return mxu.certPayReSmsToXml(payRequest);
			//融保支付
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equals(payOrder.payChannel)){
    			JSONObject jsonObject = JSON.parseObject(new RBPayService().resendCheckCode(payRequest));
    			//{"merchant_id":"100000000011015","order_no":"20160509175748","phone":"","result_code":"0000","result_msg":"发送成功"}
    			if("0000".equals(jsonObject.getString("result_code"))){
    				payRequest.setRespInfo("000");
    				payRequest.userMobileNo = jsonObject.getString("phone");
    				return mxu.certPayReSmsToXml(payRequest);
    			} else throw new Exception(jsonObject.getString("result_msg"));
    		//平台支付
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_PLTPAY").equals(payOrder.payChannel)){
    			new PLTPayService().resendCheckCode(payRequest);
   				payRequest.setRespInfo("000");
   				return mxu.certPayReSmsToXml(payRequest);
    		} else throw new Exception("快捷支付渠道暂未开放");
		} catch (Exception e) {
			new Blog().info(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 快捷支付-支付确认
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String certPayConfirm(PayRequest payRequest) throws Exception{
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		try {
			//订单
			PayProductOrder prdOrder = payRequest.productOrder;
			PayOrder payOrder = payRequest.payOrder;
			//订单不存在
			if(prdOrder==null || payOrder==null){
				payRequest.setRespInfo("008");
				return mxu.certPayConfirmToXml(payRequest);
			}
			//非快捷支付订单
			if(!"03".equals(payOrder.paytype)){
				payRequest.setRespInfo("057");
				return mxu.certPayReSmsToXml(payRequest);
			}
			//订单已经支付成功
			if("01".equals(payOrder.ordstatus)){
				payRequest.setRespInfo("014");
				return mxu.certPayReSmsToXml(payRequest);
			}
			payRequest.productOrder = prdOrder;
			payRequest.payOrder = payOrder;
			//支付确认
			//先锋支付
			if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF").equals(payOrder.payChannel)){
//	    		XFPayService service = new XFPayService();
//	    		String res = service.certPayConfirm(payRequest);
//	    		JSONObject resJson = JSON.parseObject(res);
//	    		if("00000".equals(resJson.getString("resCode"))){
//	    			if("I".equals(resJson.getString("status")) || "S".equals(resJson.getString("status"))){
//		    			payRequest.setRespInfo("000");//成功
//	    			}else{
//		    			payRequest.setRespInfo("-1");
//						payRequest.respDesc = resJson.getString("resMessage");
//						PayOrder orderTmp = new PayOrder();
//						orderTmp.merno = payRequest.merchantId;
//						orderTmp.prdordno = payRequest.merchantOrderId;
//						orderTmp.bankerror = resJson.getString("resMessage");
//						new PayOrderDAO().updateOrderError(orderTmp);
//		    		}
//	    		}else{
//	    			payRequest.setRespInfo("-1");
//					payRequest.respDesc = resJson.getString("resMessage");
//					PayOrder orderTmp = new PayOrder();
//					orderTmp.merno = payRequest.merchantId;
//					orderTmp.prdordno = payRequest.merchantOrderId;
//					orderTmp.bankerror = resJson.getString("resMessage");
//					new PayOrderDAO().updateOrderError(orderTmp);
//	    		}
				return mxu.certPayConfirmToXml(payRequest);
			//摩宝支付。
	    	} else if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_MOBAO").equals(payOrder.payChannel)){
				payRequest.setRespInfo("059");
				payRequest.respDesc+="（"+payOrder.payChannel+"）";
				return mxu.certPayReSmsToXml(payRequest);
			//融保支付
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equals(payOrder.payChannel)){
    			JSONObject jsonObject = JSON.parseObject(new RBPayService().certPayConfirm(payRequest));
    			if("0000".equals(jsonObject.getString("result_code")))payRequest.setRespInfo("000");
    			else {
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc = jsonObject.getString("result_msg")!=null?
    					jsonObject.getString("result_msg").replaceAll("200.0分","2元"):"融保未知错误";//融保的提示要替换：交易金额不能低于200.0分
    			}
				//{"phone": "13552550781","cert_no": "410725199203121238","owner": "贾利娟","validthru": "","card_no": "6217991000002346674","cvv2": "",
				//"merchant_id": "100000000009085","check_code": "494461","order_no": "102015091714254601","version": ""}
				String cardNo = jsonObject.getString("card_no");
				PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(cardNo);
//				payRequest.bindId = "";//payRequest.payOrder.custId;
				payRequest.cardNo = cardNo;
				payRequest.cardType = cardBin!=null?cardBin.cardType:"";
				payRequest.bankId = cardBin!=null?cardBin.bankCode:"";
				payRequest.bankName = cardBin!=null?cardBin.bankName:"";
				payRequest.userMobileNo = jsonObject.getString("phone");
				try {payRequest.cvv2 = jsonObject.getString("cvv2");} catch (Exception e) {}
				try {payRequest.validPeriod = jsonObject.getString("validthru");} catch (Exception e) {}
				return mxu.certPayConfirmToXml(payRequest);
    		} 
			//畅捷支付
	    	else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(payOrder.payChannel)){
	    		CJPayService service = new CJPayService();
	    		String res = service.certPayConfirm(payRequest);
	    		log.info("\n畅捷快捷确认返回的数据==========\n"+res);
				JSONObject jsonObject = JSON.parseObject(res);
				if("2".equals(jsonObject.getString("trade_status"))){//失败
					payRequest.setRespInfo("-1");
					log.info(jsonObject.getString("err_msg"));
					payRequest.respDesc = jsonObject.getString("err_msg");
					PayOrder orderTmp = new PayOrder();
					orderTmp.merno = payRequest.merchantId;
					orderTmp.prdordno = payRequest.merchantOrderId;
					orderTmp.bankerror = jsonObject.getString("err_msg");
					new PayOrderDAO().updateOrderError(orderTmp);
				} else if("1".equals(jsonObject.getString("trade_status"))){//处理中
					int n=0;
					//查询处理结果
					for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
						try {service.channelQuery(payOrder);} catch (Exception e) {}
						log.info("CJ快捷查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
						n=n+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i];
						try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
						if(!"00".equals(payOrder.ordstatus))break;
						if(n>60)break;
					}
					if("00".equals(payOrder.ordstatus)){
						payRequest.setRespInfo("-1");
						log.info("CJ查询失败"+payOrder.payordno);
						payRequest.respDesc = "CJ支付失败";
						PayOrder orderTmp = new PayOrder();
						orderTmp.merno = payRequest.merchantId;
						orderTmp.prdordno = payRequest.merchantOrderId;
						orderTmp.bankerror = "渠道查单无返回，疑似掉单";
						new PayOrderDAO().updateOrderError(orderTmp);
					}
				} else if("0".equals(jsonObject.getString("trade_status"))) payRequest.setRespInfo("000");//成功
				return mxu.certPayConfirmToXml(payRequest);
			//高汇通支付
	    	}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT").equals(payOrder.payChannel)){
	    		GHTPayService service = new GHTPayService();
	    		String res = service.certPayConfirm(payRequest);
	    		JSONObject jsonObject = JSON.parseObject(res);
	    		if("0000".equals(jsonObject.getString("retCode"))){
	    			Map<String,Object> resMap = JSON.parseObject(res);
	    			if(RSA.verifySign(GHTPayService.analysis(resMap), (String)resMap.get("sign"), PayConstant.PAY_CONFIG.get("GHT_PUB_KEY"), "utf-8")){
		    			if("01".equals(jsonObject.getString("tradeStatus"))){
		    				payRequest.setRespInfo("000");//成功
		    			}
	    			}else throw new Exception("高汇通验签失败");
	    		}else{
	    			payRequest.setRespInfo("-1");
					payRequest.respDesc = jsonObject.getString("retMsg");
					PayOrder orderTmp = new PayOrder();
					orderTmp.merno = payRequest.merchantId;
					orderTmp.prdordno = payRequest.merchantOrderId;
					orderTmp.bankerror = jsonObject.getString("retMsg");
					new PayOrderDAO().updateOrderError(orderTmp);
	    		}
				return mxu.certPayConfirmToXml(payRequest);
			//宝付支付。
	    	} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BF").equals(payOrder.payChannel)){
    			JSONObject jsonObject = JSON.parseObject(new BFPayService().certPayConfirm(payRequest));
    			if("0000".equals(jsonObject.getString("resp_code"))){
    				payRequest.setRespInfo("000");
    				//更新支付结果
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = payRequest.payOrder.payordno;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	payRequest.payOrder.ordstatus = tmpPayOrder.ordstatus;
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			} else {
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc = jsonObject.getString("resp_msg");
    				log.info(jsonObject.getString("resp_msg"));
    				PayOrder orderTmp = new PayOrder();
					orderTmp.merno = payRequest.merchantId;
					orderTmp.prdordno = payRequest.merchantOrderId;
					orderTmp.bankerror = jsonObject.getString("resp_msg");
					new PayOrderDAO().updateOrderError(orderTmp);
    			}
    			payRequest.bindId = payRequest.payOrder.custId;
				payRequest.cardNo = "";
				payRequest.cardType = "";
				payRequest.bankId = "";
				payRequest.bankName ="";
				payRequest.userMobileNo = "";
				return mxu.certPayConfirmToXml(payRequest);
			//平台支付
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_PLTPAY").equals(payOrder.payChannel)){
    			new PLTPayService().certPayConfirm(payRequest);
				return mxu.certPayConfirmToXml(payRequest);
			//富友快捷支付确认支付。
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY").equals(payOrder.payChannel)){
    			JSONObject jsonObject = JSON.parseObject(new FYPayService().certPayConfirm(payRequest));
    			if(jsonObject.getString("responsecode").equals("0000")){
    				payRequest.setRespInfo("000");
    				payRequest.respDesc=jsonObject.getString("responsemsg");
    			}else{
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc=jsonObject.getString("responsemsg");
    			}
				return mxu.certPayConfirmToXml(payRequest);
    		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY2").equals(payOrder.payChannel)){
    			JSONObject jsonObject = JSON.parseObject(new FY2PayService().certPayConfirm(payRequest));
    			if(jsonObject.getString("responsecode").equals("0000")){
    				payRequest.setRespInfo("000");
    				payRequest.respDesc=jsonObject.getString("responsemsg");
    			}else{
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc=jsonObject.getString("responsemsg");
    			}
				return mxu.certPayConfirmToXml(payRequest);
    		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY3").equals(payOrder.payChannel)){
    			JSONObject jsonObject = JSON.parseObject(new FY3PayService().certPayConfirm(payRequest));
    			if(jsonObject.getString("responsecode").equals("0000")){
    				payRequest.setRespInfo("000");
    				payRequest.respDesc=jsonObject.getString("responsemsg");
    			}else{
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc=jsonObject.getString("responsemsg");
    			}
				return mxu.certPayConfirmToXml(payRequest);
			//京东快捷支付确认支付。
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD").equals(payOrder.payChannel)){
    			JSONObject jsonObject = JSON.parseObject(new JDPayService().certPayConfirm(payRequest));
    			if(jsonObject.getString("tradeStatus").equals("0")){
					payRequest.setRespInfo("000");
					payRequest.respDesc="支付成功";
					//更新支付结果
					PayOrder tmpPayOrder = new PayOrder();
	            	tmpPayOrder.payordno = payRequest.payOrder.payordno;
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	payRequest.payOrder.ordstatus = tmpPayOrder.ordstatus;
	            	new NotifyInterface().notifyMer(tmpPayOrder);
				} else if (jsonObject.getString("tradeStatus").equals("7")){
					payRequest.setRespInfo("-1");
					payRequest.respDesc="支付失败";
				}
				return mxu.certPayConfirmToXml(payRequest);
			//易连快捷支付去确认。
    		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YL").equals(payOrder.payChannel)){
//    			JSONObject jsonObject = JSON.parseObject(new YLPayService().certPayConfirm(payRequest));
//    			if(jsonObject.getString("trans_state").equals("000")){
//					payRequest.setRespInfo("000");
//					payRequest.respDesc="操作成功";
//				} else {
//					payRequest.setRespInfo("-1");
//					payRequest.respDesc=jsonObject.getString("trans_desc");
//				}
				return mxu.certPayConfirmToXml(payRequest);
			//金运通快捷支付确认。
    		} else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JYT").equals(payOrder.payChannel)){
    			JSONObject jsonObject = JSON.parseObject(new JYTPayService().certPayConfirm(payRequest));
    			if(jsonObject.getString("status").equals("000")){
					payRequest.setRespInfo("000");
					payRequest.respDesc="支付成功";
					//更新支付结果
					PayOrder tmpPayOrder = new PayOrder();
	            	tmpPayOrder.payordno = payRequest.payOrder.payordno;
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	payRequest.payOrder.ordstatus = tmpPayOrder.ordstatus;
	            	new NotifyInterface().notifyMer(tmpPayOrder);
				} else if (jsonObject.getString("status").equals("001")){//处理中
					payRequest.setRespInfo("000");
					payRequest.respDesc=jsonObject.getString("msg");
				}else if (jsonObject.getString("status").equals("-1")){
					payRequest.setRespInfo("-1");
					payRequest.respDesc=jsonObject.getString("msg");
				}
				return mxu.certPayConfirmToXml(payRequest);
			//创新快捷确认
    		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX").equals(payOrder.payChannel)){
    			
    			JSONObject jsonObject = JSON.parseObject(new CXPayService().certPayConfirm(payRequest));
    			if(jsonObject.getString("resCode").equals("000")){
    				payRequest.setRespInfo("000");
    				payRequest.respDesc=jsonObject.getString("resMsg");
    			}else{
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc=jsonObject.getString("resMsg");
    			}
				return mxu.certPayConfirmToXml(payRequest);
			//邦付宝快捷确认。
    		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB").equals(payOrder.payChannel)){
    			JSONObject jsonObject = JSON.parseObject(new BFBPayService().certPayConfirm(payRequest));
    			if(jsonObject.getString("resCode").equals("000")){
    				payRequest.setRespInfo("000");
    				payRequest.respDesc=jsonObject.getString("resMsg");
    			}else{
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc=jsonObject.getString("resMsg");
    			}
				return mxu.certPayConfirmToXml(payRequest);
			//联动快捷确认
    		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD").equals(payOrder.payChannel)){
    			Map jsonObject = new LDPayService().certPayConfirm(payRequest);
    			if("0000".equals(jsonObject.get("ret_code"))){
    				payRequest.setRespInfo("000");
    				payRequest.respDesc=(String) jsonObject.get("ret_msg");
    			}else{
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc=(String) jsonObject.get("ret_msg");
    			}
				return mxu.certPayConfirmToXml(payRequest);
			//长盈快捷确认
    		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CY").equals(payOrder.payChannel)) {
    			JSONObject jsonObject = new CYPayService().certPayConfirm(payRequest);
    			if(jsonObject.getString("respcd").equals("0000")){
    				payRequest.setRespInfo("000");
    				payRequest.respDesc=jsonObject.getString("respmsg");
    			} else {
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc=jsonObject.getString("respmsg");
    			}
    			return mxu.certPayConfirmToXml(payRequest);
    		//沃雷特快捷确认
    		}else if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_WLT").equals(payOrder.payChannel)) {
    			JSONObject jsonObject = new WLTPayService().certPayConfirm(payRequest);
    			if ("00000".equals(jsonObject.getString("retCode"))) {
    				payRequest.setRespInfo("000");
    				payRequest.respDesc=jsonObject.getString("retMsg");
    			}else {
    				payRequest.setRespInfo("-1");
    				payRequest.respDesc=jsonObject.getString("retMsg");
    			}
    			return mxu.certPayConfirmToXml(payRequest);
	    	} else throw new Exception("快捷支付渠道暂未开放");
		} catch (Exception e) {
			new Blog().info(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 已经清结算完成的，修改结算信息
	 * @throws Exception 
	 */
	public void updateSettlementForRefundResult(PayProductOrder prdOrder,PayRefund payRefund) throws Exception{
		PayRefundDAO refundDao = new PayRefundDAO();
		PayMerchantSettlementDAO pmsDao = new PayMerchantSettlementDAO();
		PayMarginService pmService = new PayMarginService();
		try {
			transaction.beignTransaction(refundDao,pmsDao,pmService);
			//已经清结算完成的，修改结算信息，0未结算，1已结算
			if(prdOrder.stlsts != null && prdOrder.stlsts.length()!=0 && !"0".equals(prdOrder.stlsts)){
				//取得结算信息
				PayMerchantSettlement stl = pmsDao.getSettlementById(prdOrder.acjrnno);
				if(stl!=null){
					stl.stlRefundCount++;//退款笔数
					stl.stlRefundAmt += payRefund.rfamt;//退款总额 
					stl.stlNetrecamt -= payRefund.netrecamt;//应结算金额
					stl.remark = (stl.remark==null?"":stl.remark)+";"
							+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"退款单号"+payRefund.refordno;
					pmsDao.updateSettlementForRefund(stl);//更新退款信息
					//结算完成的退款处理,0初始状态;1待审核状态;2审核通过状态;3审核未通过状态;4结算成功;5结算失败,默认0
					if("4".equals(stl.stlStatus)){
						//产生新的扣去保证金记录
						pmService.appendPayMarginForRefund(payRefund);
						//更新退款说明（保证金退款）
						payRefund.rfsake = "保证金退款";
						refundDao.updateRemarkForRefund(payRefund);
					}
				}
			}
			transaction.endTransaction();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
	/**
	 * 订单撤销
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String cancelOrder(PayRequest payRequest) throws Exception{
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		PayProductOrderDAO prdDao = new PayProductOrderDAO();
		PayInterfaceDAO interfaceDao = new PayInterfaceDAO();
		//商品订单
		PayProductOrder prdOrder = prdDao.getProductOrderById(payRequest.merchantId,payRequest.merchantOrderId);
		//结算时间检查，结算前10中，不允许退款请求，结算过程中不允许退款
		Date curDate = new Date();
		Date t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").
				format(curDate) + " " + PayConstant.PAY_CONFIG.get("settlement_time"));
		long tmp = t.getTime() - curDate.getTime();
		if(PayAutoSettlementListener.auto_run_flag||(tmp>=0 && tmp<=1000*60*10)){
			payRequest.setRespInfo("033");
			return mxu.refundOrderToXml(payRequest);
		}
		//商品订单不存在
		if(prdOrder == null){
			payRequest.setRespInfo("008");
			return mxu.refundOrderToXml(payRequest);
		}
		//0未结算，1已结算
		if("1".equals(prdOrder.stlsts) || (prdOrder.acjrnno!=null && prdOrder.acjrnno.length()>0)){
			payRequest.setRespInfo("009");
			return mxu.refundOrderToXml(payRequest);
		}
		//退款订单无法撤销
		if(prdOrder.rftotalamt>0){
			payRequest.setRespInfo("012");
			return mxu.refundOrderToXml(payRequest);
		}
		//只有当日订单才能撤销
		if(!new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()).equals(
				new java.text.SimpleDateFormat("yyyyMMdd").format(prdOrder.ordertime))){
			payRequest.setRespInfo("013");
			return mxu.refundOrderToXml(payRequest);
		}
		//支付成功的订单全额退款，其他状态修改成撤销09
		if(!"01".equals(prdOrder.ordstatus)){
			//更改订单状态 为撤销成功
			prdDao.updatePrdOrderStatus(prdOrder.id,"09");
			payRequest.respCode="000";
			payRequest.respDesc="操作成功";
			return mxu.refundOrderToXml(payRequest);
		}
		List orderList = interfaceDao.getPayOrderListByPrdordno(payRequest.merchantId,payRequest.merchantOrderId);
		PayOrder payOrder = null;
		long txnamt = 0;
		for(int i = 0;i<orderList.size(); i++){
			payOrder = (PayOrder)orderList.get(i);
			//支付成功订单金额求和
			if("01".equals(payOrder.ordstatus))txnamt = txnamt+payOrder.txamt;
		}
		//发送撤销信息给银行 TODO
		PayRefund payRefund = new PayRefund();
		payRefund.refordno=Tools.getUniqueIdentify();//退款订单号
		payRefund.oriprdordno=prdOrder.prdordno;//原商品订单号
		payRefund.oripayordno=payOrder.payordno;//原支付订单号
		payRefund.rfpayordno="";//退款支付订单号
		payRefund.bankcode="";//银行编码
		payRefund.getbankcode="";//收款银行编码
		payRefund.custpayacno="";//买家支付账号
		payRefund.bankpayusernm="";//收款账号户名
		payRefund.merpayacno="";//卖方支付账号
		payRefund.custId="";//客户ID
		payRefund.txccy="CNY";//支付货币,默认：CNY
		payRefund.rfamt=txnamt;//退款金额
		payRefund.fee=0l;//手续费
		payRefund.netrecamt=txnamt;//实收净额
		payRefund.rfsake="";//退款理由
		payRefund.banksts="00";//银行网关状态，00:退款处理中01:退款成功02：退款失败，默认00
		payRefund.ordstatus="00";//订单状态，00:未处理01:退款处理中02:退款成功03:退款失败04:已退回支付账号05:已重新退款99:超时，默认00
		payRefund.obankjrnno="";//原银行流水号
		payRefund.bankjrnno="";//银行流水号
		payRefund.returl="";//同步通知URL
		payRefund.notifyurl="";//异步通知URL
		payRefund.bankerror="";//银行返回错误原因
		payRefund.merno=payRequest.merchantId;//商户编号
		payRefund.stlbatno="";//结算批次号
		payRefund.rfordtime=new Date();//退款申请时间,默认：sysdate
		payRefund.stlsts="0";//结算状态，0否1是，默认0
		payRefund.bustyp="";//业务类型,01-B2C02-B2B
		payRefund.operId="";//操作员
		payRefund.filed1="";//备注1
		payRefund.filed2="";//备注2
		payRefund.filed3="";//备注3
		payRefund.filed4="";//备注4
		payRefund.filed5="";//备注5
		//添加退款订单 修改商户订单退款状态  修改支付订单退款状态		
		new PayRefundDAO().cancelOrder(payRefund);
		payRequest.setRespInfo("000");
		return mxu.refundOrderToXml(payRequest);
	}
	/**
	 * 担保通知
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String guaranteeNotice(PayRequest payRequest) throws Exception{
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		PayProductOrderDAO prdDao = new PayProductOrderDAO();
		PayRefundDAO refundDao = new PayRefundDAO();
		PayRiskDayTranSumService payRiskDayTranSumService = new PayRiskDayTranSumService();
		PayRefundService refundService = new PayRefundService();
		try {
			transaction.beignTransaction(prdDao,refundDao,payRiskDayTranSumService,refundService);
			//商品订单
			PayProductOrder prdOrder = prdDao.getProductOrderById(payRequest.merchantId,payRequest.merchantOrderId);
			//商品订单不存在
			if(prdOrder == null){
				payRequest.setRespInfo("008");
				return mxu.guaranteeNoticeToXml(payRequest);
			}
			//未支付成功
			if(!"01".equals(prdOrder.ordstatus)){
				payRequest.setRespInfo("010");
				return mxu.guaranteeNoticeToXml(payRequest);
			}
			//已经全部退款订单处理
			if(prdOrder.rftotalamt.longValue()>=prdOrder.ordamt.longValue()){
				payRequest.setRespInfo("012");
				return mxu.guaranteeNoticeToXml(payRequest);
			}
			//是否为担保支付判断，商品订单类型0-消费；1-充值2-担保支付3商户充值
	    	if(!"2".equals(prdOrder.prdordtype)){
	    		payRequest.setRespInfo("047");
				return mxu.guaranteeNoticeToXml(payRequest);
	    	}
	    	//担保已完成
	    	if("1".equals(prdOrder.guaranteeStatus)){
	    		payRequest.setRespInfo("049");
				return mxu.guaranteeNoticeToXml(payRequest);
	    	}
			payRequest.productOrder = prdOrder;
			//取得支付订单（目前商品订单、支付订单一一对应）
			payRequest.payOrder = (PayOrder) (new PayInterfaceDAO().
				getPayOrderListByPrdordno(payRequest.merchantId,payRequest.merchantOrderId)).get(0);
			//修改担保支付状态（清算时间、结算标识ACDATE=sysdate,ACTIME=sysdate,GUARANTEE_STATUS='1'）
			//担保成功
			if("0".equals(payRequest.guaranteeNoticeStatus)) new PayAccProfileDAO().guaranteeNoticeUserBalance(payRequest);
			else {//担保失败，发起退款
				PayRefund payRefund = new PayRefund();
				payRefund.refordno=Tools.getUniqueIdentify();//退款订单号
				payRefund.oriprdordno=prdOrder.prdordno;//原商品订单号
				payRefund.oripayordno=payRequest.payOrder.payordno;//原支付订单号
				payRefund.rfpayordno="";//退款支付订单号
				payRefund.bankcode="";//银行编码
				payRefund.getbankcode="";//收款银行编码
				payRefund.custpayacno="";//买家支付账号
				payRefund.bankpayusernm="";//收款账号户名
				payRefund.merpayacno="";//卖方支付账号
				payRefund.custId="";//客户ID
				payRefund.txccy="CNY";//支付货币,默认：CNY
				payRefund.rfamt=payRequest.payOrder.txamt-payRequest.productOrder.rftotalamt;//退款金额
				payRefund.fee=0l;//手续费
				payRefund.netrecamt=payRefund.rfamt;//实收净额
				payRefund.rfsake="";//退款理由
				payRefund.banksts="00";//银行网关状态，00:退款处理中01:退款成功02：退款失败，默认00
				payRefund.ordstatus="00";//订单状态，00:未处理  01:退款处理中  02:退款成功  03:退款失败  04:已退回支付账号  05:已重新退款99:超时，默认00
				payRefund.obankjrnno="";//原银行流水号
				payRefund.bankjrnno="";//银行流水号
				payRefund.returl="";//同步通知URL
				payRefund.notifyurl="";//异步通知URL
				payRefund.rfordtime = new Date();
				payRefund.bankerror="";//银行返回错误原因
				payRefund.merno=payRequest.merchantId;//商户编号
				payRefund.stlbatno="";//结算批次号
				payRefund.stlsts="2";//结算状态，0未结算 1已清算 2已结算
				payRefund.bustyp="";//业务类型,01-B2C02-B2B
				payRefund.operId="";//操作员
				payRefund.filed1="";//备注1
				payRefund.filed2="";//备注2
				payRefund.filed3="";//备注3
				payRefund.filed4="";//备注4
				payRefund.filed5="";//备注5
				if(refundDao.addPayRefund(payRefund)>0){//添加退款订单 修改商户订单退款状态  修改支付订单退款状态
					//发起线上退款（判断渠道是否支持线上退款）
					PayCoopBank channel = PayCoopBankService.CHANNEL_MAP.get(payRequest.payOrder.payChannel);
					//是否支持线上退款,0支持，1不支持
					if(channel != null && "0".equals(channel.refundOnline)){
						//融保线上退款
						if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB").equals(channel.bankCode)){
	//						new RBPayService().refund(payRefund);
	//						payRefund.operId="admin";
	//			            payRefund.rfsake = "线上退款";
	//			            refundService.setResultRefund(payRefund);
						}
						//畅捷线上退款。
						if(PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ").equals(channel.bankCode)){
							new CJPayService().refund(payRefund);
							payRefund.operId="admin";
				            payRefund.rfsake = "线上退款";
				           // refundService.setResultRefund(payRefund);
						}
					}
					//交易汇总更新
					payRiskDayTranSumService.updateRiskRefundSum(payRefund);
					//退款成功，更新成功交易更新汇总
					if( "01".equals(payRefund.banksts)){
						payRiskDayTranSumService.updateRiskSuccessRefundSum(payRefund);
					}
					//退款成功，重新计算手续费（商户手续费、代理手续费）TODO
					payRequest.setRespInfo("000");
					transaction.endTransaction();
					return mxu.refundOrderToXml(payRequest);
				}
			}
			transaction.endTransaction();
			payRequest.setRespInfo("000");
			return mxu.guaranteeNoticeToXml(payRequest);
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
	}
}
