package com.pay.merchantinterface.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.Tools;

import com.PayConstant;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayProductOrderDAO;
import com.pay.refund.dao.PayRefund;
import com.pay.refund.dao.PayRefundDAO;

public class PayOrderService {
	public void addOrder(PayMerchant merchant,PayRequest payRequest,
			PayProductOrder prdOrder,PayOrder order) throws Exception{
		//0初次支付1重复支付
		if("0".equals(payRequest.payMode)){
			prdOrder.id=Tools.getUniqueIdentify();//记录ID
			prdOrder.prdordno=payRequest.merchantOrderId;//商品订单号
			prdOrder.merno=payRequest.merchantId;//商户编号
			prdOrder.versionid=payRequest.version;//版本号
			prdOrder.prdordtype="0";//商品订单类型0-消费；1-充值2-担保支付3商户充值
			prdOrder.biztype=payRequest.bizType;//业务类型01:团购,02:网络游戏,03:医疗保健,04:教育培训,05:日常用品,06:鲜花礼品,07:旅游票务,08:影视娱乐,09:支付宝业务,10:图书音像,11:全国公共事业缴费,12:电子产品,13:交通违章罚款,14:网站建设,15:信用卡还款,16:会员论坛,17:软件产品,18:身份证查询,19:购买支付通业务,20:运动休闲,21:彩票,22:数字点卡,23:其它
			prdOrder.ordamt=Long.parseLong(payRequest.merchantOrderAmt);//订单金额
			prdOrder.ordstatus="00";//订单状态 00:未支付01:支付成功02:支付处理中03:退款审核中04:退款处理中05:退款成功06:退款失败07:撤销审核中08：同意撤销09：撤销成功10:撤销失败11：订单作废12预付款13延迟付款审核中14冻结15同意延迟付款16拒绝延迟付款17实付审核中18实付审核通过19实付审核拒绝99:超时
			prdOrder.ordccy="CNY";//货币类型
			prdOrder.notifyurl=payRequest.merchantNotifyUrl;//异步通知URL
			prdOrder.returl=payRequest.merchantFrontEndUrl;//同步返回URL
			prdOrder.signature=payRequest.sign;//签名信息
			prdOrder.signtype="RSA";//签名方式
			prdOrder.verifystring=payRequest.reqXml;//验证摘要字符串
			prdOrder.prddisurl="";//商品展示网址
			prdOrder.prdname=payRequest.merchantOrderDesc;//商品名称
			prdOrder.prdshortname=payRequest.merchantOrderDesc;//商品简称
			prdOrder.prddesc=payRequest.merchantOrderDesc;//商品描述
			prdOrder.merremark=payRequest.merchantName;//商户备注
			prdOrder.rpttype="";//收款方式
			prdOrder.prdunitprice=Long.parseLong(payRequest.merchantOrderAmt);//商品单价
			prdOrder.buycount=1l;//购买数量
			prdOrder.defpayway="01";//默认支付方式00支付账户01网上银行02终端03快捷支付04混合支付05支票/汇款06三方支付，默认01
			prdOrder.custId="";//客户ID
			prdOrder.merpayacno="";//卖方支付账号
			prdOrder.sellrptacno="";//卖方收款账号
			prdOrder.sellrptamt=0l;//卖方收款金额
			prdOrder.paymode=payRequest.payMode;//付款类型0初次支付1重复支付
			prdOrder.custpayacno="";//买方支付账号
			prdOrder.custrptacno="";//买方收款账号
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
			new PayProductOrderDAO().addPayProductOrder(prdOrder);
		}
		order.payordno=Tools.getUniqueIdentify();//支付订单号
		order.ordstatus="00";//'00'	订单状态，00:等待付款  01:付款成功  02:付款失败 03:退款申请 04:等待退款 05:退款成功 06:退款失败 07:同意撤销 08：拒绝撤销  09：撤销成功 10:撤销失败 12 预付款  17 实付审核中 18 实付审核通过 19 实付审核拒绝 99:超时，默认00
		order.prdordtype="0";//'0'	订单类型，0 消费 1 充值 2 担保支付 3 商户充值,默认0
		order.paytype="01";//支付方式，00 支付账户   01 网上银行    02 终端   03 快捷支付    04 混合支付    05 支票/汇款
		order.multype="01";//混合支付方式，01 网上银行    02 终端   03 快捷支付
		order.bankcod="";//银行编码，银行卡所属银行
		order.paybnkcod="";//收款银行编码
		order.bankjrnno="";//银行流水号
		order.txccy="CNY";//'CNY'	支付货币，默认：CNY（人民币）
		order.txamt=Long.parseLong(payRequest.merchantOrderAmt);//支付金额，单位：分
		order.accamt=0l;//虚拟账户支付金额，单位：分
		order.mulamt=0l;//第二种支付方式金额，单位：分
		order.fee=0l;//支付手续费，单位：分
		order.netrecamt=Long.parseLong(payRequest.merchantOrderAmt);//实收净额，单位：分
		order.bankpayacno="";//银行付款账号
		order.bankpayusernm="";//银行付款户名
		order.bankerror="";//银行返回出错原因
		order.backerror="";//后台返回出错原因
		order.merno=prdOrder.merno;//商户编号
		order.prdordno=prdOrder.prdordno;//商品订单号
		order.notifyurl=PayConstant.PAY_CONFIG.get("server_notify_url");//异步通知URL
		order.custId="";//客户ID
		order.payret="";//银行支付状态，00 成功   01 失败
		order.prdname=payRequest.merchantOrderDesc;//商品名称
		order.merremark=payRequest.merchantName;//商户信息
		order.bnkjnl=order.payordno;//银行网关订单号
		order.signmsg=payRequest.sign;//签名信息
		order.bustyp="02";//业务类型（01-B2C   02-B2B）
		order.bnkmerno="";//银行商户号
		order.tracenumber="";//系统跟踪号
		order.cbatno="";//批次号
		order.srefno="";//系统参考号
		order.termno="";//终端号
		order.trantyp="";//交易类型
		order.termtyp="";//终端类型，0  其他Mini终端  1 交行Mini终端  2 PBOC2.0
		order.credno="";//信用卡卡号
		order.versionno="";//服务版本号
		order.payacno="";//支付账号
		new PayOrderDAO().addPayOrder(order);
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
	 * 订单退款
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String refundOrder(PayRequest payRequest) throws Exception{
		MerchantXmlUtil mxu = new MerchantXmlUtil();
		PayProductOrderDAO prdDao = new PayProductOrderDAO();
		PayInterfaceDAO interfaceDao = new PayInterfaceDAO();
		//商品订单
		PayProductOrder prdOrder = prdDao.getProductOrderById(payRequest.merchantId,payRequest.merchantOrderId);
		//商品订单不存在
		if(prdOrder == null){
			payRequest.setRespInfo("008");
			return mxu.refundOrderToXml(payRequest);
		}
		//0未结算，1已结算，或者银行已经清算
		if("1".equals(prdOrder.stlsts) || (prdOrder.acjrnno!=null && prdOrder.acjrnno.length()>0)){
			payRequest.setRespInfo("009");
			return mxu.refundOrderToXml(payRequest);
		}
		if(!"01".equals(prdOrder.ordstatus)){
			payRequest.setRespInfo("010");
			return mxu.refundOrderToXml(payRequest);
		}
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
		//发送退款信息给银行 TODO
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
		payRefund.rfamt=refundAmt;//退款金额
		payRefund.fee=0l;//手续费
		payRefund.netrecamt=refundAmt;//实收净额
		payRefund.rfsake="";//退款理由
		payRefund.banksts="01";//银行网关状态，00:退款处理中01:退款成功02：退款失败，默认00
		payRefund.ordstatus="03";//订单状态，00:未处理01:退款处理中02:退款成功03:退款失败04:已退回支付账号05:已重新退款99:超时，默认00
		payRefund.obankjrnno="";//原银行流水号
		payRefund.bankjrnno="";//银行流水号
		payRefund.returl="";//同步通知URL
		payRefund.notifyurl="";//异步通知URL
		payRefund.bankerror="";//银行返回错误原因
		payRefund.merno=payRequest.merchantId;//商户编号
		payRefund.stlbatno="";//结算批次号
		payRefund.stlsts="0";//结算状态，0否1是，默认0
		payRefund.bustyp="";//业务类型,01-B2C02-B2B
		payRefund.operId="";//操作员
		payRefund.filed1="";//备注1
		payRefund.filed2="";//备注2
		payRefund.filed3="";//备注3
		payRefund.filed4="";//备注4
		payRefund.filed5="";//备注5
		//添加退款订单 修改商户订单退款状态  修改支付订单退款状态		
		new PayRefundDAO().addPayRefund(payRefund);
		payRequest.setRespInfo("000");
		return mxu.refundOrderToXml(payRequest);
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
		payRefund.banksts="01";//银行网关状态，00:退款处理中01:退款成功02：退款失败，默认00
		payRefund.ordstatus="03";//订单状态，00:未处理01:退款处理中02:退款成功03:退款失败04:已退回支付账号05:已重新退款99:超时，默认00
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
}
