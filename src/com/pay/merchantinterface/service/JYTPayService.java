package com.pay.merchantinterface.service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.DataTransUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.jweb.dao.Blog;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
import com.third.jyt.baseUtil.WGUtil;
import com.third.jyt.mockClientMsgBase.MockClientMsgBase_RNpay;
/**
 * 金运通接口服务类
 * @author Administrator
 *
 */
public class JYTPayService {
	private static final Log log = LogFactory.getLog(JYTPayService.class);
	public static MockClientMsgBase_RNpay base =  new MockClientMsgBase_RNpay();
	public static Map<String, String> JYT_BANK_CODE = new HashMap<String, String>();
	static {
		
		JYT_BANK_CODE.put("ICBC", "01020000");//工商银行。
		JYT_BANK_CODE.put("CCB", "01050000");//建设
		JYT_BANK_CODE.put("ABC", "01030000");//农业。
		JYT_BANK_CODE.put("CMB", "03080000");//招商
		JYT_BANK_CODE.put("BOCOM", "03010000");//交通
		JYT_BANK_CODE.put("BOC", "01040000");//中国
		JYT_BANK_CODE.put("CEB", "03030000");//光大
		JYT_BANK_CODE.put("CMBC", "03050000");//民生
		JYT_BANK_CODE.put("CIB", "03090000");//兴业
		JYT_BANK_CODE.put("CNCB", "03020000");//中信
		JYT_BANK_CODE.put("GDB", "03060000");//广发
		JYT_BANK_CODE.put("SPDB", "03100000");//浦发
		JYT_BANK_CODE.put("PAB", "03070000");//平安
		JYT_BANK_CODE.put("HXB", "03040000");//华夏
		JYT_BANK_CODE.put("NBBC", "04083320");//宁波
		JYT_BANK_CODE.put("BEAI", "03200000");//东亚
		JYT_BANK_CODE.put("BOS", "04012900");//上海
		JYT_BANK_CODE.put("PSBC", "01000000");//邮政
		JYT_BANK_CODE.put("NJBC", "04243010");//南京
		JYT_BANK_CODE.put("SHRCB", "65012900");//上海农商
		JYT_BANK_CODE.put("BOHC", "03170000");//渤海
		//JYT_BANK_CODE.put("", "64296510");//成都
		JYT_BANK_CODE.put("BCCB", "04031000");//北京
	}
	
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JYT"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try{
			
			String tranCode = "TN1001";//网关支付交易代码。
			String version = "1.0.0";
			String charset = "utf-8";
			String uaType = "00";//支付客户端类型  -00 pc端。
			String merchantId=PayConstant.PAY_CONFIG.get("JYT_WG_PAY_MERNO");//PayConstant.PAY_CONFIG.get("JYT_MERCHANT_ID");//商户号。
			String merOrderId = payOrder.payordno;//订单号。
			String merTranTime =new SimpleDateFormat("yyyymmddhhmmss").format(payOrder.createtime);//订单交易时间。订单交易时间，格式：yyyymmddhhmmss
			String merUserId = payOrder.merno;//商户系统用户ID。
			String orderDesc = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//订单描述。
			String prodInfo = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称。
			String tranAmt = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额。
			String curType = "CNY";//币种。
			String payMode = payOrder.bustyp.equals("1")?"00":"01";//支付模式。支付方式，00:B2C；01:B2B；
			String bankCode = JYT_BANK_CODE.get(payOrder.bankcod);//银行编码。
			//01纯借记卡, 02 信用卡,99:企业账户。支付模式为00，此处可选01,02；若支付模式为01，则此处只能是99。
			String bankCardType="";
			if("00".equals(payMode)){
				bankCardType = payOrder.bankCardType.equals("0")?"01":"02";//银行卡类型。
			}else if("01".equals(payMode)){
				bankCardType = "99";//银行卡类型。
			}
			String notifyUrl = PayConstant.PAY_CONFIG.get("JYT_WG_NOTIFY_URL");//异步通知地址。
			String backUrl = prdOrder.returl==null?"":prdOrder.returl;//同步支付通知地址。
			String signType = "SHA256";
			//封装签名参数。
			Map<String,String> prmsMap = new HashMap<String,String>();
			prmsMap.put("tranCode", tranCode);
			prmsMap.put("version", version);
			prmsMap.put("charset", charset);
			prmsMap.put("uaType", uaType);
			prmsMap.put("merchantId", merchantId);
			prmsMap.put("merOrderId", merOrderId);
			prmsMap.put("merTranTime", merTranTime);
			prmsMap.put("merUserId", merUserId);
			prmsMap.put("orderDesc", orderDesc);
			prmsMap.put("prodInfo", prodInfo);
			prmsMap.put("tranAmt", tranAmt);
			prmsMap.put("curType", curType);
			prmsMap.put("payMode", payMode);
			prmsMap.put("bankCode", bankCode);
			prmsMap.put("bankCardType", bankCardType);
			prmsMap.put("notifyUrl", notifyUrl);
			prmsMap.put("backUrl", backUrl);
			prmsMap.put("signType", signType);
			String sign = WGUtil.getSign(prmsMap, PayConstant.PAY_CONFIG.get("JYT_WG_PAY_KEY"));
			prmsMap.put("sign", sign);
			log.info("金运通网关请求报文:"+prmsMap.toString());
			request.setAttribute("tranCode", tranCode);
			request.setAttribute("version", version);
			request.setAttribute("charset", charset);
			request.setAttribute("uaType", uaType);
			request.setAttribute("merchantId", merchantId);
			request.setAttribute("merOrderId", merOrderId);
			request.setAttribute("merTranTime", merTranTime);
			request.setAttribute("merUserId", merUserId);
			request.setAttribute("orderDesc", orderDesc);
			request.setAttribute("prodInfo", prodInfo);
			request.setAttribute("tranAmt", tranAmt);
			request.setAttribute("curType", curType);
			request.setAttribute("payMode", payMode);
			request.setAttribute("bankCode", bankCode);
			request.setAttribute("bankCardType", bankCardType);
			request.setAttribute("notifyUrl", notifyUrl);
			request.setAttribute("backUrl", backUrl);
			request.setAttribute("signType", signType);
			request.setAttribute("sign", sign);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 商户接口下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder){
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JYT"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		try{
			
			String tranCode = "TN1001";//网关支付交易代码。
			String version = "1.0.0";
			String charset = "utf-8";
			String uaType = "00";//支付客户端类型  -00 pc端。
			String merchantId=PayConstant.PAY_CONFIG.get("JYT_WG_PAY_MERNO");//PayConstant.PAY_CONFIG.get("JYT_MERCHANT_ID");//商户号。
			String merOrderId = payOrder.payordno;//订单号。
			String merTranTime =new SimpleDateFormat("yyyymmddhhmmss").format(payOrder.createtime);//订单交易时间。订单交易时间，格式：yyyymmddhhmmss
			String merUserId = payOrder.merno;//商户系统用户ID。
			String orderDesc = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//订单描述。
			String prodInfo = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称。
			String tranAmt = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额。
			String curType = "CNY";//币种。
			String payMode = payOrder.bustyp.equals("1")?"00":"01";//支付模式。支付方式，00:B2C；01:B2B；
			String bankCode = JYT_BANK_CODE.get(payOrder.bankcod);//银行编码。
			//01纯借记卡, 02 信用卡,99:企业账户。支付模式为00，此处可选01,02；若支付模式为01，则此处只能是99。
			String bankCardType="";
			if("00".equals(payMode)){
				bankCardType = payOrder.bankCardType.equals("0")?"01":"02";//银行卡类型。
			}else if("01".equals(payMode)){
				bankCardType = "99";//银行卡类型。
			}
			String notifyUrl = PayConstant.PAY_CONFIG.get("JYT_WG_NOTIFY_URL");//异步通知地址。
			String backUrl = prdOrder.returl==null?"":prdOrder.returl;//同步支付通知地址。
			String signType = "SHA256";
			//封装签名参数。
			Map<String,String> prmsMap = new HashMap<String,String>();
			prmsMap.put("tranCode", tranCode);
			prmsMap.put("version", version);
			prmsMap.put("charset", charset);
			prmsMap.put("uaType", uaType);
			prmsMap.put("merchantId", merchantId);
			prmsMap.put("merOrderId", merOrderId);
			prmsMap.put("merTranTime", merTranTime);
			prmsMap.put("merUserId", merUserId);
			prmsMap.put("orderDesc", orderDesc);
			prmsMap.put("prodInfo", prodInfo);
			prmsMap.put("tranAmt", tranAmt);
			prmsMap.put("curType", curType);
			prmsMap.put("payMode", payMode);
			prmsMap.put("bankCode", bankCode);
			prmsMap.put("bankCardType", bankCardType);
			prmsMap.put("notifyUrl", notifyUrl);
			prmsMap.put("backUrl", backUrl);
			prmsMap.put("signType", signType);
			String sign = WGUtil.getSign(prmsMap,PayConstant.PAY_CONFIG.get("JYT_WG_PAY_KEY"));
			prmsMap.put("sign", sign);
			log.info("金运通网关请求报文:"+prmsMap.toString());
			request.setAttribute("tranCode", tranCode);
			request.setAttribute("version", version);
			request.setAttribute("charset", charset);
			request.setAttribute("uaType", uaType);
			request.setAttribute("merchantId", merchantId);
			request.setAttribute("merOrderId", merOrderId);
			request.setAttribute("merTranTime", merTranTime);
			request.setAttribute("merUserId", merUserId);
			request.setAttribute("orderDesc", orderDesc);
			request.setAttribute("prodInfo", prodInfo);
			request.setAttribute("tranAmt", tranAmt);
			request.setAttribute("curType", curType);
			request.setAttribute("payMode", payMode);
			request.setAttribute("bankCode", bankCode);
			request.setAttribute("bankCardType", bankCardType);
			request.setAttribute("notifyUrl", notifyUrl);
			request.setAttribute("backUrl", backUrl);
			request.setAttribute("signType", signType);
			request.setAttribute("sign", sign);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 网关渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			String tranCode = "TN2001";
			String version ="1.2.0";
			String charset ="utf-8";
			String merchantId =PayConstant.PAY_CONFIG.get("JYT_WG_PAY_MERNO");
			String oriMerOrderId=payOrder.payordno;
			String orderType ="0";
			String signType ="SHA256";
			//封装签名参数。
			Map<String,String> prmsMap = new HashMap<String,String>();
			prmsMap.put("tranCode",tranCode );
			prmsMap.put("version",version );
			prmsMap.put("charset",charset );
			prmsMap.put("merchantId",merchantId );
			prmsMap.put("oriMerOrderId",oriMerOrderId );
			prmsMap.put("orderType",orderType );
			prmsMap.put("signType",signType );
			String sign = WGUtil.getSign(prmsMap,PayConstant.PAY_CONFIG.get("JYT_WG_PAY_KEY"));
			prmsMap.put("sign",sign );
			String params = "";
			Iterator<String> it = prmsMap.keySet().iterator();
			while(it.hasNext()){
				Object key = it.next();
				params = params+key+"="+prmsMap.get(key)+ "&";
			}
			params = params.substring(0,params.length()-1); 
			log.info("金运通网查询请求报文:"+params.toString());
			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("JYT_WG_PAY_QUERY_URL"), params.getBytes("utf-8")),"utf-8");
			log.info("金运通网关支付查询返回数据:"+res);
			/**
			 * {"charset":"utf-8","merchantId":"290060100013","orderType":"0","oriMerOrderId":"qbt5XiA2Q3rKTOG",
			 * "oriPayOrderId":"NP2017021702022710017576","respCode":"S0000000","respDesc":"查询订单成功",
			 * "sign":"2bfa7cf7ef96a4397c04f3dc256c02a06c0ddcacd41af15111448796a39c564a","signType":"SHA256",
			 * "tranAmt":"0.01","tranCode":"TN2001","tranFinishTime":"","tranState":"01","version":"1.2.0"}
			 */
			com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
			if("S0000000".equals(jsonObject.get("respCode"))){
				//支付成功。
				if("02".equals(jsonObject.get("tranState"))){
					 payOrder.ordstatus="01";//支付成功
				     new NotifyInterface().notifyMer(payOrder);//支付成功
				}
			}else throw new Exception("查询失败");
		} catch (Exception e){
			throw e;
		}
	}
	/**
	 * 快捷支付--签约
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String quickPay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		try {
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);//通过卡号获取卡类信息。
			String xml = getTD1004Xml(payRequest,cardBin);//封装xml报文。
			String mac = base.signMsg(xml);//加签，用商户端私钥进行加签
			String respMsg = base.sendMsg(xml,mac);//发送接收响应报文
			String respCode = base.getMsgRespCode(respMsg);
			log.info("金运通快捷签约响应报文"+respMsg);
			if(respCode.equals("S0000000")){
				if("01".equals(base.getMsgState(respMsg))){
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
							"respCode=\"000\" respDesc=\"操作成功\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
				} else return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
							"respCode=\"-1\" respDesc=\"金运通快捷鉴权不通过\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			} else return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" " +
						"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
						"respCode=\"-1\" respDesc=\"金运通快捷鉴权不通过\" " +
						"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
		} catch (Exception e) {
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
			"<message merchantId=\""+payRequest.merchantId+"\" " +
			"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
			"respCode=\"-1\" respDesc=\""+e.getMessage()+"\" " +
			"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>"; 
		}
	}
	/**
	 * 快捷支付-确认
	 * @param request
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String certPayConfirm(PayRequest payRequest) throws Exception{
		try {
			String xml = getTD4005Xml(payRequest);
			String mac = base.signMsg(xml);//加签，用商户端私钥进行加签
			String respMsg = base.sendMsg(xml,mac);//接收响应报文
			String respCode = base.getMsgRespCode(respMsg);
			log.info("金运通快捷支付下单响应报文:"+respMsg);
			if("S0000000".equals(respCode)){
				if("00".equals(base.getMsgState(respMsg))) return "{\"status\":\"000\",\"msg\":\"支付成功\"}";
				else if("01".equals(base.getMsgState(respMsg))
					||"02".equals(base.getMsgState(respMsg))) return "{\"status\":\"001\",\"msg\":\"处理中\"}";
				else return "{\"status\":\"-1\",\"msg\":\"支付失败\"}";
			} else return "{\"status\":\"-1\",\"msg\":\"支付请求失败\"}";
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"-1\",\"msg\":\""+e.getMessage()+"\"}";
		}
	}
	/**
	 * 快捷支付渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void quickPayChannelQuery(PayOrder payOrder) throws Exception{
		Blog log = new Blog();
		String xml = getTD2001Xml(payOrder);
		String mac = base.signMsg(xml);//加签，用商户端私钥进行加签
		String respMsg = base.sendMsg(xml,mac);//接收响应报文
		String respCode = base.getMsgRespCode(respMsg);
		log.info("金运通快捷支付查询响应报文:"+respMsg);
		if("S0000000".equals(respCode)){
			payOrder.actdat = new Date(); 
			if("00".equals(base.getMsgState(respMsg))) payOrder.ordstatus="01";
			else if("01".equals(base.getMsgState(respMsg))
				||"02".equals(base.getMsgState(respMsg))) payOrder.ordstatus="03";
			else payOrder.ordstatus="02";//支付失败。
			new NotifyInterface().notifyMer(payOrder);//支付成功
		}
			
	}
	public String getTD1004Xml(PayRequest payRequest,PayCardBin cardBin){
		StringBuffer xml = new StringBuffer();
		xml.append(base.getMsgHeadXml("TD1004"));
		xml.append("<body><cust_no>").append(payRequest.cardNo).append("</cust_no>");
		xml.append("<order_id>").append(payRequest.payOrder.payordno).append("</order_id>");
		xml.append("<bank_card_no>").append(payRequest.cardNo).append("</bank_card_no>");
		xml.append("<id_card_no>").append(payRequest.credentialNo).append("</id_card_no>");
		xml.append("<mobile>").append(payRequest.userMobileNo).append("</mobile>");
		xml.append("<name>").append(payRequest.userName).append("</name>");
		xml.append("<tran_amount>").append(String.format("%.2f", (Double.parseDouble(payRequest.merchantOrderAmt))/100d)).append("</tran_amount>");
		if("1".equals(cardBin.cardType)){
			xml.append("<bank_card_type>02</bank_card_type>");
			xml.append("<expired_date>"+payRequest.validPeriod+"</expired_date>");
			xml.append("<cvv2>"+payRequest.cvv2+"</cvv2>");
		}
		xml.append("</body></message>");
		return xml.toString();
	}
	public String getTD4005Xml(PayRequest payRequest){
		StringBuffer xml = new StringBuffer();
		xml.append(base.getMsgHeadXml("TD4005"));
		xml.append("<body>");
		xml.append("<verify_code>").append(payRequest.checkCode).append("</verify_code>");
		xml.append("<order_id>").append(payRequest.payOrder.payordno).append("</order_id>");
		xml.append("</body></message>");
		return xml.toString();
	}
	public String getTD2001Xml(PayOrder payOrder){
		StringBuffer xml = new StringBuffer();
		xml.append(base.getMsgHeadXml("TD2001"));
		xml.append("<body>");
		xml.append("<order_id>").append(payOrder.payordno).append("</order_id>");
		xml.append("</body></message>");
		return xml.toString();
	}
}
