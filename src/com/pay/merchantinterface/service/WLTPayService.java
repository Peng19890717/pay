package com.pay.merchantinterface.service;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.third.wlt.HttpUtil;
import com.third.wlt.SignUtil;

public class WLTPayService {
	
	private static final Log log = LogFactory.getLog(WLTPayService.class);

	public static Map <String,String> PAY_BANK_MAP_NAME_KJ = new HashMap <String,String>();
	
	static {
		PAY_BANK_MAP_NAME_KJ.put("BOC", "BOC");//中国银行
		PAY_BANK_MAP_NAME_KJ.put("CCB", "CCB");//建设银行
		PAY_BANK_MAP_NAME_KJ.put("CEB", "CEB");//光大银行
		PAY_BANK_MAP_NAME_KJ.put("GDB", "GDB");//广发银行
		PAY_BANK_MAP_NAME_KJ.put("CIB", "CIB");//兴业银行
		PAY_BANK_MAP_NAME_KJ.put("PAB", "SPAB");//平安银行
		PAY_BANK_MAP_NAME_KJ.put("ICBC", "ICBC");//工商银行
		PAY_BANK_MAP_NAME_KJ.put("SPDB", "SPDB");//浦发银行
		PAY_BANK_MAP_NAME_KJ.put("HXB", "HXB");//华夏银行
		PAY_BANK_MAP_NAME_KJ.put("BOCOM", "COMM");//交通银行
		PAY_BANK_MAP_NAME_KJ.put("CMBC", "CMBC");//民生银行
		PAY_BANK_MAP_NAME_KJ.put("CNCB", "CITIC");//中信银行
		PAY_BANK_MAP_NAME_KJ.put("PSBC", "PSBC");//邮储银行
		PAY_BANK_MAP_NAME_KJ.put("CMB", "CMB");//招商银行
	}
	
	/**
	 * 快捷支付(下单)
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String quickPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		
		try {
			//列出请求参数
			String bankCode = null;//银行代码
			if (PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) {
				bankCode = PAY_BANK_MAP_NAME_KJ.get(payOrder.bankcod);//获取银行代码
			}
			if (bankCode == null) {
				throw new Exception("沃雷特无对应银行（"+payOrder.bankcod+"）");//不支持此银行
			}
			String service = "xyw.quickpay.api.precreate";//固定值，接口名称
			String signMethod = "RSA";//签名类型。目前仅支持RSA
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payOrder.createtime);//发起交易的时间戳
			String charset = "UTF-8";//系统之间交互信息时使用的编码字符集
			String v = "1.1";//API协议版本，固定值：1.1
			String format = "json";//响应格式。固定值：json
			String canary = PayConstant.PAY_CONFIG.get("WLT_KJ_RANDOM_STR");//随机字符串。不超过32位
			String appId = PayConstant.PAY_CONFIG.get("WLT_MERNO");//商户号
			String appUserName = payOrder.payordno;//用户名。用户ID或不超20位随机串，不能填固定值20秒内不成重复
			String payType = "70";//支付方式。固定值：70
			String tradeAmt = String.format("%.2f", (Double.parseDouble(payRequest.merchantOrderAmt)/100d)); //消费金额。单位：元；精确到分
			//String tradeAmt = payRequest.merchantOrderAmt; //消费金额。单位：元；精确到分
			String mchTradeNo = payOrder.payordno;//商户订单编号
			String subject = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称,商品标题
			String currency = "CNY";//币种，人民币
			String cardType = "01";//银行卡类标识。参数选项:01 借记卡 02 贷记卡
			String cardNo = payRequest.cardNo;//银行卡号
			String certType = "01";//证件类型。目前只支持身份证 ；固定值 01
			String certNo = payRequest.credentialNo;//身份证号
			String cardRealName = payRequest.userName;//银行卡账户名称
			String phone = payRequest.userMobileNo;//银行预留手机号
			String notifyUrl = PayConstant.PAY_CONFIG.get("WLT_KJ_URL");//异步通知地址
			
			//请求参数
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("service", service);
			paramsMap.put("signMethod", signMethod);
			paramsMap.put("timestamp", timestamp);
			paramsMap.put("charset", charset);
			paramsMap.put("v", v);
			paramsMap.put("format", format);
			paramsMap.put("canary", canary);
			paramsMap.put("appId", appId);
			paramsMap.put("appUserName", appUserName);
			paramsMap.put("payType", payType);
			paramsMap.put("tradeAmt", tradeAmt);
			paramsMap.put("mchTradeNo", mchTradeNo);
			paramsMap.put("subject", subject);
			paramsMap.put("currency", currency);
			paramsMap.put("cardType", cardType);
			paramsMap.put("cardNo", cardNo);
			paramsMap.put("certType", certType);
			paramsMap.put("certNo", certNo);
			paramsMap.put("cardRealName", cardRealName);
			paramsMap.put("phone", phone);
			paramsMap.put("notifyUrl", notifyUrl);
			String url = PayConstant.PAY_CONFIG.get("WLT_KJ_URL");//请求url
			String rsa = PayConstant.PAY_CONFIG.get("WLT_RSA_PRI_KEY");//加密私钥
			log.info("沃雷特-快捷下单请求："+JSON.toJSONString(paramsMap));
			String codeUrl = signAndPost(url,paramsMap,rsa);
			log.info("沃雷特-快捷下单响应："+codeUrl);
			JSONObject retjson = null;
			try{
				retjson = JSON.parseObject(codeUrl);
			} catch (Exception e) {
				log.error("沃雷特返回解析json失败！",e);
			}
			/*
			 * 返回:
			 * {"retCode":"00000","tradeStatus":"00","mchTradeNo":"J9ZC7QRI3JLXPT2PU","orderNo":"20171114409099926390640709865867","pcNo":"2017111422726326"}
			 */
			if(retjson != null){
				String retCode = retjson.getString("retCode");
				if("00000".equals(retCode)){//00000表示请求成功
					//沃雷特订单号，做为流水号
					String data = retjson.getString("orderNo");
					if(data != null && !"".equals(data)){
						PayOrderDAO payOrderDAO = new PayOrderDAO();
						payOrder.bankjrnno = data;
						payOrderDAO.updateOrderBankjrnno(payOrder);
					}
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
							"respCode=\"000\""+" respDesc=\"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
				} else {
					//下单失败
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
							"respCode=\"-1\" respDesc=\""+retjson.getString("respmsg")+"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
				}
			} else {
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" " +
						"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
						"respCode=\"-1\" respDesc=\""+"下单失败！"+"\" " +
						"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/**
	 * 快捷确认
	 * @param payRequest
	 * @return
	 * @throws Exception 
	 */
	public JSONObject certPayConfirm(PayRequest payRequest) throws Exception {
		
		try {
			//拼接请求参数
			String service = "xyw.quickpay.api.confirm";//接口名称。xyw.quickpay.api.confirm
			String signMethod = "RSA";//签名类型。目前仅支持RSA
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//发起交易的时间戳
			String charset = "UTF-8";//系统之间交互信息时使用的编码字符集。目前统一为UTF-8
			String v = "1.1";//API协议版本，固定值：1.1
			String format = "json";//响应格式。默认为json格式。目前统一为json
			String canary = PayConstant.PAY_CONFIG.get("WLT_KJ_RANDOM_STR");//随机字符串。不超过32位
			String appId = PayConstant.PAY_CONFIG.get("WLT_MERNO");//商户号
			String mchTradeNo = payRequest.payOrder.payordno;//发起“快捷预订单”时使用的商户订单号
			String verifyCode = payRequest.checkCode;//短信验证码
			Map<String,String> paramsMap = new HashMap<String,String>();
			paramsMap.put("service", service);
			paramsMap.put("signMethod", signMethod);
			paramsMap.put("timestamp", timestamp);
			paramsMap.put("charset", charset);
			paramsMap.put("v", v);
			paramsMap.put("format", format);
			paramsMap.put("canary", canary);
			paramsMap.put("appId", appId);
			paramsMap.put("mchTradeNo", mchTradeNo);
			paramsMap.put("verifyCode", verifyCode);
			String url = PayConstant.PAY_CONFIG.get("WLT_KJ_URL");//请求url
			String rsa = PayConstant.PAY_CONFIG.get("WLT_RSA_PRI_KEY");//加密私钥
			log.info("沃雷特-快捷确认请求数据："+JSON.toJSONString(paramsMap));
			String html = signAndPost(url,paramsMap,rsa);
			/*
			 * html数据示例：
			 * {"mchTradeNo":"J9ZEKWD43JLXPT2PU","orderNo":"20171114141942034973519484148526","retCode":"00000","retMsg":"订单修改状态成功","tradeStatus":"03"}
			 */
			JSONObject json = JSON.parseObject(html);
			log.info("沃雷特-快捷确认响应数据："+json);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 渠道补单（查单）
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try {
			//请求参数
			String service = "xyw.order.status.query";//接口名称xyw.order.status.query
			String signMethod = "RSA";//签名类型。目前仅支持RSA
			String charset = "UTF-8";//系统之间交互信息时使用的编码字符集。目前统一为UTF-8
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//发起交易的时间戳
			String v = "1.1";//API协议版本，固定值：1.1
			String format = "json";//响应格式。默认为json格式。目前统一为json
			String canary = PayConstant.PAY_CONFIG.get("WLT_KJ_RANDOM_STR");//随机字符串。不超过32位
			String appId = PayConstant.PAY_CONFIG.get("WLT_MERNO");//商户号
			String mchTradeNo = payOrder.payordno;//商户订单号
			String orderNo = payOrder.bankjrnno;//沃雷特交易流水号
			String url = PayConstant.PAY_CONFIG.get("WLT_KJ_URL");//请求url
			String rsa = PayConstant.PAY_CONFIG.get("WLT_RSA_PRI_KEY");//加密私钥 
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("service", service);
			map.put("signMethod", signMethod);
			map.put("charset", charset);
			map.put("timestamp", timestamp);
			map.put("v", v);
			map.put("format", format);
			map.put("canary", canary);
			map.put("appId", appId);
			map.put("mchTradeNo", mchTradeNo);
			map.put("orderNo", orderNo);
			
			log.info("沃雷特-查单请求："+JSON.toJSONString(map));
			String html = signAndPost(url,map,rsa);
			JSONObject json = JSONObject.parseObject(html);
			log.info("沃雷特-查单响应："+json.toString());
			String retCode = json.getString("retCode");
			/** 00000查询请求成功，其他均为失败 */
			if ("00000".equals(retCode)) {
				/** 判断支付状态 00：未付款；01：处理中；02：成功；03：失败；90：交易关闭；*/
				if ("02".equals(json.getString("tradeStatus"))) {
					payOrder.ordstatus="01";//支付成功
					new NotifyInterface().notifyMer(payOrder);//支付成功
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 发送请求
	 * @param url
	 * @param sParaTemp
	 * @param rsa
	 * @return
	 */
	public static String signAndPost(String url, Map<String, String> sParaTemp, String rsa) {
		try {
			sParaTemp.put("sign", SignUtil.encryptSign(sParaTemp, rsa, "RSA"));
			String resultString = HttpUtil.httpPostByDefaultTime(new URI(url), sParaTemp, "UTF-8");
			return resultString;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
