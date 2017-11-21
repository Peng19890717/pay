package com.pay.merchantinterface.service;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.PayUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.kbnew.Tools.GsonUtil;
import com.third.kbnew.Tools.RSAUtil;
import com.third.kbnew.Tools.SignUtil;
import com.third.kbnew.Tools.Tools;
/**
 * 新酷宝服务类
 * @author Administrator
 *
 */
public class KBNEWPayService {
	private static final Log log = LogFactory.getLog(KBNEWPayService.class);
	
	public static Map <String,String> PAY_BANK_MAP_JIEJI = new HashMap <String,String>();//借记卡银行码映射（网关）
	public static Map <String,String> PAY_BANK_MAP_DF = new HashMap <String,String>();//借记卡银行码映射（网关）
	
	static {
		//网银支持银行
		PAY_BANK_MAP_JIEJI.put("ABC", "ABC");//1中国农业银行
		PAY_BANK_MAP_JIEJI.put("BCCB", "BCCB");//2北京银行  ---------
		PAY_BANK_MAP_JIEJI.put("BOC", "BOC");//3中国银行
		PAY_BANK_MAP_JIEJI.put("ICBC", "ICBC");//4工商银行
		PAY_BANK_MAP_JIEJI.put("HXB", "HXB");//5华夏银行  ----------
		PAY_BANK_MAP_JIEJI.put("CCB", "CCB");//6建设银行
		PAY_BANK_MAP_JIEJI.put("CEB", "CEB");//7光大银行  
		PAY_BANK_MAP_JIEJI.put("CIB", "CIB");//8兴业银行  
		PAY_BANK_MAP_JIEJI.put("CNCB", "CITIC");//9中信银行  
		PAY_BANK_MAP_JIEJI.put("CMB", "CMB");//10招商银行
		PAY_BANK_MAP_JIEJI.put("CMBC", "CMBC");//11民生银行
		PAY_BANK_MAP_JIEJI.put("PAB", "SZPAB");//12平安银行  
		PAY_BANK_MAP_JIEJI.put("GDB", "GDB");//13广发银行  
		PAY_BANK_MAP_JIEJI.put("PSBC", "PSBC");//14中国邮政储蓄银行  
		PAY_BANK_MAP_JIEJI.put("SPDB", "SPDB");//15上海浦东发展银行  
		PAY_BANK_MAP_JIEJI.put("BOCOM", "COMM");//16交通银行
		PAY_BANK_MAP_JIEJI.put("BOS", "BOS");//17上海银行  
		PAY_BANK_MAP_JIEJI.put("HCCB", "HCCB");//18杭州银行  -----
		//代付支持银行
		PAY_BANK_MAP_DF.put("ABC", "ABC,农业银行");
		PAY_BANK_MAP_DF.put("BCCB", "BCCB,北京银行");
		PAY_BANK_MAP_DF.put("BRCB", "BJRCB,北京农商银行");
		PAY_BANK_MAP_DF.put("BOC", "BOC,中国银行");
		PAY_BANK_MAP_DF.put("BOS", "BOS,上海银行");
		PAY_BANK_MAP_DF.put("BOHC", "CBHB,渤海银行");
		PAY_BANK_MAP_DF.put("CCB", "CCB,建设银行");
		PAY_BANK_MAP_DF.put("CEB", "CEB,光大银行");
		PAY_BANK_MAP_DF.put("CIB", "CIB,兴业银行");
		PAY_BANK_MAP_DF.put("CNCB", "CITIC,中信银行");
		PAY_BANK_MAP_DF.put("CMB", "CMB,招商银行");
		PAY_BANK_MAP_DF.put("CMBC", "CMBC,民生银行");
		PAY_BANK_MAP_DF.put("BOCOM", "COMM,交通银行");
		PAY_BANK_MAP_DF.put("CSCB", "CSCB,长沙银行");
		PAY_BANK_MAP_DF.put("GDB", "GDB,广东发展银行");
		PAY_BANK_MAP_DF.put("GDB", "SHRCB,上海农村商业银行");
		PAY_BANK_MAP_DF.put("HCCB", "HCCB,杭州银行");
		PAY_BANK_MAP_DF.put("HXB", "HXB,华夏银行");
		PAY_BANK_MAP_DF.put("ICBC", "ICBC,工商银行");
		PAY_BANK_MAP_DF.put("NJBC", "NJCB,南京银行");
		PAY_BANK_MAP_DF.put("PSBC", "PSBC,中国邮储银行");
		PAY_BANK_MAP_DF.put("NBBC", "NBCB,宁波银行");
		PAY_BANK_MAP_DF.put("SPDB", "SPDB,浦发银行");
		PAY_BANK_MAP_DF.put("ZSBC", "CZB,浙商银行");
		PAY_BANK_MAP_DF.put("PAB", "SZPAB,平安银行");
		
	}
	
	/**
	 * 微信/支付宝扫码下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType     (WXZF/ZFBZF)
	 * @return
	 * @throws Exception 
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		try {
			String service = "qc_scancode_pay";
			String version = "1.0";
			String request_time =new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;
			String _input_charset = "UTF-8";
			String sign_type = "RSA";
			String out_trade_no = payOrder.payordno;
			String subject = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称 
			String merchant_id = PayConstant.PAY_CONFIG.get("KBNEW_MER_NO");
			String pay_method = productType;
			String limit_pay = "0";
			String amount = String.format("%.2f", ((double)payOrder.txamt)/100d);
			StringBuilder inputString = new StringBuilder("");
			inputString.append("service=" + service);
			inputString.append(",version=" + version);
			inputString.append(",request_time=" + request_time);
			inputString.append(",partner_id=" + PayConstant.PAY_CONFIG.get("KBNEW_PARTNER_ID"));
			inputString.append(",_input_charset=" + _input_charset);
			inputString.append(",sign_type=" + sign_type);
			inputString.append(",notify_url=" + PayConstant.PAY_CONFIG.get("KBNEW_NOTIFY_URL"));
			if (out_trade_no != null && out_trade_no != "") {
				inputString.append(",out_trade_no=" + out_trade_no);
			}
			if (subject != null && subject != "") {
				inputString.append(",subject=" + subject);
			}
			if (merchant_id != null && merchant_id != "") {
				inputString.append(",merchant_id=" + merchant_id);
			}
			if (pay_method != null && pay_method != "") {
				inputString.append(",pay_method=" + pay_method);
			}
			if (limit_pay != null && limit_pay != "") {
				inputString.append(",limit_pay=" + limit_pay);
			}
			if (amount != null && amount != "") {
				inputString.append(",amount=" + amount);
			}
			String[] splitString = inputString.toString().split(",");
			Tools tool = new Tools();
			String sortString = tool.sortStringWithSeparator(splitString, "&");
			String signString = tool.removeFromString(sortString, "&", "&","startswith", "sign").replaceAll("replaceFlag", ",");
			RSAUtil rsa = new RSAUtil();
			rsa.setPrivateKey(PayConstant.PAY_CONFIG.get("KBNEW_PRIVATEKEYRSA"));
			String sign = rsa.sign(signString);
			sortString = tool.removeFromString(sortString, "&", "&","startswith", "notify_url")+ "&notify_url="
			+ tool.textEncode(PayConstant.PAY_CONFIG.get("KBNEW_NOTIFY_URL"), "UTF-8");
			String requestString = sortString.replaceAll("replaceFlag", ",")+ "&sign=" + tool.textEncode(sign, "UTF-8");
			log.info("新酷宝扫码请求参数:"+requestString);
			String responseString = tool.textDncode(tool.post(PayConstant.PAY_CONFIG.get("KBNEW_PAY_URL"), requestString), "UTF-8");
			log.info("新酷宝扫码响应参数:"+responseString);
			Map<String, String> result_map = GsonUtil.fronJson2Map(responseString);
			if("APPLY_SUCCESS".equals(result_map.get("response_code"))){
				String sign_result = result_map.get("sign").toString();
				String sign_type_result = result_map.get("sign_type").toString();
				String _input_charset_result = result_map.get("_input_charset").toString();
				String result_string = Tools.calculateSignPlain(result_map);
				if(SignUtil.Check_sign(result_string.toString(), sign_result,sign_type_result, PayConstant.PAY_CONFIG.get("KBNEW_PUBLICCHECKKEY"), _input_charset_result)){
					
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
					"codeUrl=\""+ result_map.get("qrcode_url") +"\" respCode=\"000\" respDesc=\"处理成功\"/>";
				}else new Exception("验签失败!");
			}else{
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
			       		   "<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
			       			"codeUrl=\"\" respCode=\"-1\" respDesc=\"下单失败\"/>";
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
				"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
		}
	}
	/**
	 * 扫码查单
	 * @param payOrder
	 * @throws Exception
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		String service = "qc_query_single_pay";
		String version = "1.0";
		String request_time =new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;
		String _input_charset = "UTF-8";
		String sign_type = "RSA";
		String out_trade_no = payOrder.payordno;
		StringBuilder inputString = new StringBuilder("");
		inputString.append("service=" + service);
		inputString.append(",version=" + version);
		inputString.append(",request_time=" + request_time);
		inputString.append(",partner_id=" + PayConstant.PAY_CONFIG.get("KBNEW_PARTNER_ID"));
		inputString.append(",_input_charset=" + _input_charset);
		inputString.append(",sign_type=" + sign_type);
		if (out_trade_no != null && out_trade_no != "") {
			inputString.append(",out_trade_no=" + out_trade_no);
		}
		String[] splitString = inputString.toString().split(",");
		Tools tool = new Tools();
		String sortString = tool.sortStringWithSeparator(splitString, "&");
		String signString = tool.removeFromString(sortString, "&", "&","startswith", "sign").replaceAll("replaceFlag", ",");
		RSAUtil rsa = new RSAUtil();
		rsa.setPrivateKey(PayConstant.PAY_CONFIG.get("KBNEW_PRIVATEKEYRSA"));
		String sign = rsa.sign(signString);
		String requestString = sortString.replaceAll("replaceFlag", ",")+ "&sign=" + tool.textEncode(sign, "UTF-8");
		log.info("新酷宝扫码查询请求参数:"+requestString);
		String responseString = tool.textDncode(tool.post(PayConstant.PAY_CONFIG.get("KBNEW_PAY_URL"), requestString), "UTF-8");
		log.info("新酷宝扫码查询响应参数:"+responseString);
		Map<String, String> result_map = GsonUtil.fronJson2Map(responseString);
		if("APPLY_SUCCESS".equals(result_map.get("response_code"))){
			String sign_result = result_map.get("sign").toString();
			String sign_type_result = result_map.get("sign_type").toString();
			String _input_charset_result = result_map.get("_input_charset").toString();
			String result_string = Tools.calculateSignPlain(result_map);
			if(SignUtil.Check_sign(result_string.toString(), sign_result,sign_type_result, PayConstant.PAY_CONFIG.get("KBNEW_PUBLICCHECKKEY"), _input_charset_result)){
				if("TRADE_SUCCESS".equals(result_map.get("trade_status"))){
					payOrder.ordstatus="01";//支付成功
		        	new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("TRADE_FAILED".equals(result_map.get("trade_status"))){
					payOrder.ordstatus="02";//失败成功
		        	new NotifyInterface().notifyMer(payOrder);//失败成功
				}
			}else new Exception("验签失败!");
		}else throw new Exception("查询失败");
	}
	
	/**
	 * 网银：收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KBNEW"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));//银行卡类型
		try {
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) bankCode = PAY_BANK_MAP_JIEJI.get(payOrder.bankcod);
			if (bankCode == null) throw new Exception("新酷宝：无对应银行（"+payOrder.bankcod+"）");
			//请求参数
			String service = "create_instant_order";//接口名称
			String version = "1.0";//接口版本
			String request_time = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;//请求时间
			String partner_id = PayConstant.PAY_CONFIG.get("KBNEW_WY_PARTNER_ID");//商户编号
			String seller_identity_id = PayConstant.PAY_CONFIG.get("KBNEW_WY_PARTNER_ID");//卖家家ID
			String seller_identity_type = "MEMBER_ID";
			String _input_charset = "UTF-8";//参数编码字符集
			String sign_type = "RSA";//签名方式
			String notify_url = PayConstant.PAY_CONFIG.get("KBNEW_WY_NOTIFY_URL");//系统异步回调通知地址
			String out_trade_no = payOrder.payordno;//订单号
			String amount = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额
			String product_desc = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String pay_method = "online_bank^"+amount+"^"+bankCode+",DEBIT,C";

			Map<String,String> paramsMap = new HashMap<String, String>();
			paramsMap.put("service", service);
			paramsMap.put("version", version);
			paramsMap.put("request_time", request_time);
			paramsMap.put("partner_id", partner_id);
			paramsMap.put("seller_identity_id", seller_identity_id);
			paramsMap.put("seller_identity_type", seller_identity_type);
			paramsMap.put("_input_charset", _input_charset);
			paramsMap.put("sign_type", sign_type);
			paramsMap.put("notify_url", notify_url);
			paramsMap.put("out_trade_no", out_trade_no);
			paramsMap.put("amount", amount);
			paramsMap.put("product_desc", product_desc);
			paramsMap.put("pay_method", pay_method);
			String str2sign = makeReqParamMap(paramsMap);//获取待签名数据
			RSAUtil rsa = new RSAUtil();
			rsa.setPrivateKey(PayConstant.PAY_CONFIG.get("KBNEW_WY_PRIVATEKEYRSA"));
			String sign = rsa.sign(str2sign);
			paramsMap.put("sign", sign);
			log.info("新酷宝网银：请求参数:"+JSON.toJSONString(paramsMap));
			String result1 = doPost(PayConstant.PAY_CONFIG.get("KBNEW_WY_PAY_URL"), paramsMap, "UTF-8");
			result1 = result1.replaceAll("\r|\n", "");
			/*
			 * <html><head></head><body>https://api.hf-pay.com:9000/opfp-aip/orders?{"reqHead":{"transCode":"100004","version":"1.0","acceptBizJrnNo":"FI101010913000000587","transDate":"20170913","transTime":"174122","acceptBizNo":"SF300012","acceptBizSubNo":"SC000001","frontUrl":"http://fcw.yacolpay.com/fcw/page/EGBANK10101-VS.htm","notifyUrl":"http://fcw.yacolpay.com/fcw/server/EGBANK10101-VS.htm","signMsg":"VUixlA75NEO5/MkpxBXtQUcgzPNGdTZVVi9as6/NDdFk4+ZPl//WelQen1xQcpcZcnWWDh/7YPJ7+2kweyX9ig7E4RGI+YnjclzTtr6r6oOdh/fg0csbjuNUur36lDdNlNfZxBXATGooer+XWT0zLaMpNal/SYy6hdiA7CVaPtk="},"reqBody":{"goodsId":"","goodsInf":"","gateId":"01020000","ccy":"CNY","amt":"100"}}<script language="javascript">document.getElementById('frmBankID').submit();</script></body></html>
			 */
			result1 = result1.substring(result1.indexOf("<body>")+6, result1.indexOf("<script"));
			String[] strArray = result1.split("[?]");
			/*
			 * {"rspHead":{"transCode":"100004","version":"1.0","acceptBizJrnNo":"FI101010913000000594","transDate":"20170913","transTime":"181929","acceptBizNo":"SF300012","acceptBizSubNo":"SC000001","bizType":"WY01","retCode":"00000000","signMsg":"CO2YWGZQGfhsBCV4CvGYn3n+38ym+NycpQxMZjoBYk3F1+WY6+uxpKBgOpOQMTzFOaZlj4kO5WWY3h13S3QT9/WHtncKlxsQqnJ2hiSqnOm+XX2alcBhWjtVAYmVdO9oi5i3QorOIFVk48dBrnbrddPIBS0FVhvNy4HBfbqS7Hs="},"rspBody":{"submitForm":"<form id=\"payment\" name=\"paymentForm\" action=\"http://pay.soopay.net/spay/pay/payservice.do\" method=\"post\"><input type=\"hidden\" name=\"charset\" value=\"UTF-8\"/><input type=\"hidden\" name=\"amount\" value=\"10\"/><input type=\"hidden\" name=\"mer_id\" value=\"50050\"/><input type=\"hidden\" name=\"sign\" value=\"FMejUVGSvnTYXHI6OnziZcQQvYa5ImsMbLbE5h8RALFJXtM/FX5Gc9XlC8nJ4wd/udScz9b3CYM3D61ogUsjpgyIutxmz0Uw7CA8KISe7PWmeyjVLeEhT1I3ZxFzYQiVi+R3/Y3Td3iyha8gAaCLU1iKYF6AomVR36koiSpVu1c=\"/><input type=\"hidden\" name=\"expire_time\" value=\"120\"/><input type=\"hidden\" name=\"notify_url\" value=\"https://api.hf-pay.com:9000/opfp-aip/orders/ump/callback\"/><input type=\"hidden\" name=\"amt_type\" value=\"RMB\"/><input type=\"hidden\" name=\"version\" value=\"4.0\"/><input type=\"hidden\" name=\"mer_date\" value=\"20170913\"/><input type=\"hidden\" name=\"gate_id\" value=\"ICBC\"/><input type=\"hidden\" name=\"service\" value=\"req_front_page_pay\"/><input type=\"hidden\" name=\"pay_type\" value=\"B2CDEBITBANK\"/><input type=\"hidden\" name=\"interface_type\" value=\"02\"/><input type=\"hidden\" name=\"order_id\" value=\"U2017091300000107152\"/><input type=\"hidden\" name=\"res_format\" value=\"HTML\"/><input type=\"hidden\" name=\"sign_type\" value=\"RSA\"/><input type=\"hidden\" name=\"ret_url\" value=\"https://api.hf-pay.com:9000/opfp-aip/orders/ump/frontCallback\"/><input type=\"submit\" value=\"submit\" style=\"display:none;\"></form><script>document.forms['paymentForm'].submit();</script>"}}
			 */
			String result2 = doPost(strArray[0], strArray[1], "UTF-8");
			//返回的json验签
			JSONObject jsonObject = new JSONObject();
			jsonObject = JSON.parseObject(result2);
			String submitForm = jsonObject.getJSONObject("rspBody").getString("submitForm");
			request.setAttribute("submitForm", submitForm);
			new PayOrderService().updateOrderForBanks(payOrder);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 网银：商户接口下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_KBNEW"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;//卡类型
		try {
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) bankCode = PAY_BANK_MAP_JIEJI.get(payOrder.bankcod);
			if (bankCode == null) throw new Exception("新酷宝无对应银行（"+payOrder.bankcod+"）");
			//请求参数
			String service = "create_instant_order";//接口名称
			String version = "1.0";//接口版本
			String request_time = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;//请求时间
			String partner_id = PayConstant.PAY_CONFIG.get("KBNEW_WY_PARTNER_ID");//商户编号
			String seller_identity_id = PayConstant.PAY_CONFIG.get("KBNEW_WY_PARTNER_ID");//卖家家ID
			String seller_identity_type = "MEMBER_ID";
			String _input_charset = "UTF-8";//参数编码字符集
			String sign_type = "RSA";//签名方式
			String notify_url = PayConstant.PAY_CONFIG.get("KBNEW_WY_NOTIFY_URL");//系统异步回调通知地址
			String out_trade_no = payOrder.payordno;//订单号
			String amount = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额
			String product_desc = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String pay_method = "online_bank^"+amount+"^"+bankCode+",DEBIT,C";

			Map<String,String> paramsMap = new HashMap<String, String>();
			paramsMap.put("service", service);
			paramsMap.put("version", version);
			paramsMap.put("request_time", request_time);
			paramsMap.put("partner_id", partner_id);
			paramsMap.put("seller_identity_id", seller_identity_id);
			paramsMap.put("seller_identity_type", seller_identity_type);
			paramsMap.put("_input_charset", _input_charset);
			paramsMap.put("sign_type", sign_type);
			paramsMap.put("notify_url", notify_url);
			paramsMap.put("out_trade_no", out_trade_no);
			paramsMap.put("amount", amount);
			paramsMap.put("product_desc", product_desc);
			paramsMap.put("pay_method", pay_method);
			String str2sign = makeReqParamMap(paramsMap);//获取待签名数据
			RSAUtil rsa = new RSAUtil();
			rsa.setPrivateKey(PayConstant.PAY_CONFIG.get("KBNEW_WY_PRIVATEKEYRSA"));
			String sign = rsa.sign(str2sign);
			paramsMap.put("sign", sign);
			log.info("新酷宝网银：请求参数:"+JSON.toJSONString(paramsMap));
			String result1 = doPost(PayConstant.PAY_CONFIG.get("KBNEW_WY_PAY_URL"), paramsMap, "UTF-8");
			result1 = result1.replaceAll("\r|\n", "");
			/*
			 * <html><head></head><body>https://api.hf-pay.com:9000/opfp-aip/orders?{"reqHead":{"transCode":"100004","version":"1.0","acceptBizJrnNo":"FI101010913000000587","transDate":"20170913","transTime":"174122","acceptBizNo":"SF300012","acceptBizSubNo":"SC000001","frontUrl":"http://fcw.yacolpay.com/fcw/page/EGBANK10101-VS.htm","notifyUrl":"http://fcw.yacolpay.com/fcw/server/EGBANK10101-VS.htm","signMsg":"VUixlA75NEO5/MkpxBXtQUcgzPNGdTZVVi9as6/NDdFk4+ZPl//WelQen1xQcpcZcnWWDh/7YPJ7+2kweyX9ig7E4RGI+YnjclzTtr6r6oOdh/fg0csbjuNUur36lDdNlNfZxBXATGooer+XWT0zLaMpNal/SYy6hdiA7CVaPtk="},"reqBody":{"goodsId":"","goodsInf":"","gateId":"01020000","ccy":"CNY","amt":"100"}}<script language="javascript">document.getElementById('frmBankID').submit();</script></body></html>
			 */
			result1 = result1.substring(result1.indexOf("<body>")+6, result1.indexOf("<script"));
			String[] strArray = result1.split("[?]");
			/*
			 * {"rspHead":{"transCode":"100004","version":"1.0","acceptBizJrnNo":"FI101010913000000594","transDate":"20170913","transTime":"181929","acceptBizNo":"SF300012","acceptBizSubNo":"SC000001","bizType":"WY01","retCode":"00000000","signMsg":"CO2YWGZQGfhsBCV4CvGYn3n+38ym+NycpQxMZjoBYk3F1+WY6+uxpKBgOpOQMTzFOaZlj4kO5WWY3h13S3QT9/WHtncKlxsQqnJ2hiSqnOm+XX2alcBhWjtVAYmVdO9oi5i3QorOIFVk48dBrnbrddPIBS0FVhvNy4HBfbqS7Hs="},"rspBody":{"submitForm":"<form id=\"payment\" name=\"paymentForm\" action=\"http://pay.soopay.net/spay/pay/payservice.do\" method=\"post\"><input type=\"hidden\" name=\"charset\" value=\"UTF-8\"/><input type=\"hidden\" name=\"amount\" value=\"10\"/><input type=\"hidden\" name=\"mer_id\" value=\"50050\"/><input type=\"hidden\" name=\"sign\" value=\"FMejUVGSvnTYXHI6OnziZcQQvYa5ImsMbLbE5h8RALFJXtM/FX5Gc9XlC8nJ4wd/udScz9b3CYM3D61ogUsjpgyIutxmz0Uw7CA8KISe7PWmeyjVLeEhT1I3ZxFzYQiVi+R3/Y3Td3iyha8gAaCLU1iKYF6AomVR36koiSpVu1c=\"/><input type=\"hidden\" name=\"expire_time\" value=\"120\"/><input type=\"hidden\" name=\"notify_url\" value=\"https://api.hf-pay.com:9000/opfp-aip/orders/ump/callback\"/><input type=\"hidden\" name=\"amt_type\" value=\"RMB\"/><input type=\"hidden\" name=\"version\" value=\"4.0\"/><input type=\"hidden\" name=\"mer_date\" value=\"20170913\"/><input type=\"hidden\" name=\"gate_id\" value=\"ICBC\"/><input type=\"hidden\" name=\"service\" value=\"req_front_page_pay\"/><input type=\"hidden\" name=\"pay_type\" value=\"B2CDEBITBANK\"/><input type=\"hidden\" name=\"interface_type\" value=\"02\"/><input type=\"hidden\" name=\"order_id\" value=\"U2017091300000107152\"/><input type=\"hidden\" name=\"res_format\" value=\"HTML\"/><input type=\"hidden\" name=\"sign_type\" value=\"RSA\"/><input type=\"hidden\" name=\"ret_url\" value=\"https://api.hf-pay.com:9000/opfp-aip/orders/ump/frontCallback\"/><input type=\"submit\" value=\"submit\" style=\"display:none;\"></form><script>document.forms['paymentForm'].submit();</script>"}}
			 */
			String result2 = doPost(strArray[0], strArray[1], "UTF-8");
			//返回的json验签
			JSONObject jsonObject = new JSONObject();
			jsonObject = JSON.parseObject(result2);
			String submitForm = jsonObject.getJSONObject("rspBody").getString("submitForm");
			request.setAttribute("submitForm", submitForm);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 网银查单
	 * @param payOrder
	 * @throws Exception
	 */
	public void channelQuery_WY(PayOrder payOrder) throws Exception{
		//请求参数
		String service = "query_b2c_order";//服务名称
		String version = "1.0";//版本号
		String request_time = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;//请求时间
		String partner_id = PayConstant.PAY_CONFIG.get("KBNEW_WY_PARTNER_ID");//商户编号
		String _input_charset = "UTF-8";//编码字符集
		String sign_type = "RSA";//签名方式
		String sign_version = "1.0";//签名版本号
		String start_time = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime).substring(0,8)+"000000";
		String end_time = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime).substring(0,8)+"235959";
		String out_trade_no = payOrder.payordno;//商户订单号
		Map<String,String> paramsMap = new HashMap<String, String>();
		paramsMap.put("service", service);
		paramsMap.put("version", version);
		paramsMap.put("request_time", request_time);
		paramsMap.put("partner_id", partner_id);
		paramsMap.put("_input_charset", _input_charset);
		paramsMap.put("sign_type", sign_type);
		paramsMap.put("sign_version", sign_version);
		paramsMap.put("start_time", start_time);
		paramsMap.put("end_time", end_time);
		paramsMap.put("out_trade_no", out_trade_no);
		String str2sign = makeReqParamMap(paramsMap);//获取待签名数据
		 RSAUtil rsa = new RSAUtil();
		 rsa.setPrivateKey(PayConstant.PAY_CONFIG.get("KBNEW_WY_PRIVATEKEYRSA"));
		 Tools tool = new Tools();
		 String sign = rsa.sign(str2sign);
		 paramsMap.put("sign", sign);
		 String requestString = getDfReqStr(paramsMap);
		 log.info("新酷宝网银查询：请求数据 "+requestString);
		 String responseString = tool.textDncode(tool.post(PayConstant.PAY_CONFIG.get("KBNEW_WY_PAY_URL"), requestString), "UTF-8");
		 log.info("新酷宝网银查询：同步响应数据 " + responseString);
		 Map<String, String> content = GsonUtil.fronJson2Map(responseString);
		 String sign_result = content.get("sign").toString();
		 String sign_type_result = content.get("sign_type").toString();
		 String _input_charset_result = content.get("_input_charset").toString();
		 content.remove("sign");
		 content.remove("sign_type");
		 content.remove("sign_version");
		 String like_result = Tools.createLinkString(content,false);
		 String signKey = PayConstant.PAY_CONFIG.get("KBNEW_WY_PUBLICCHECKKEY");//获取签名公钥
		 if (SignUtil.Check_sign(like_result.toString(), sign_result,sign_type_result, signKey, _input_charset_result)) {
			 if("APPLY_SUCCESS".equals(content.get("response_code"))){
				 String record_list = content.get("record_list");
				 if(record_list == null || "".equals(record_list)){
					 throw new Exception("新酷宝网银：查询失败！");
				 }
				 String[] result = content.get("record_list").split("~");
				 if("PAY_FINISHED".equals(result[2])){//付款成功
					 payOrder.ordstatus="01";//支付成功
					 new NotifyInterface().notifyMer(payOrder);//支付成功
				 } else if ("TRADE_FAILED".equals(result[2])) {
					 payOrder.ordstatus="02";//支付失败
					 new NotifyInterface().notifyMer(payOrder);
				 }
			 }else throw new Exception ("新酷宝网银：查询请求失败！");
		 }
	}
	
	/**
	 * 单笔代付
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingle(PayRequest payRequest) throws Exception{
		 try {
			 RSAUtil rsa = new RSAUtil();
			 rsa.setPrivateKey(PayConstant.PAY_CONFIG.get("KBNEW_WY_PRIVATEKEYRSA"));
			 Tools tool = new Tools();
			 PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//获取单笔代收付object
			 rp.setCreateTime(new Date());
			 PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取银行卡信息
			 if(cardBin == null) throw new Exception("无法识别银行账号");
			 //请求参数
			 String service = "create_batch_pay2bank";//服务名称
			 String version = "1.0";//版本号
			 String request_time = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;//请求时间
			 String partner_id = PayConstant.PAY_CONFIG.get("KBNEW_WY_PARTNER_ID");//商户编号
			 String _input_charset = "UTF-8";//编码字符集
			 String sign_type = "RSA";//签名方式
			 String sign_version = "1.0";//签名版本号
			 String batch_no = rp.sn;//明细号作为：代付批次号
			 String bankName = "北京银行";
			 String bankCode = "BCCB";
			 if(PAY_BANK_MAP_DF.get(cardBin.bankCode)!=null){
				 bankCode = PAY_BANK_MAP_DF.get(cardBin.bankCode).split(",")[0];//银行全称
				 bankName = PAY_BANK_MAP_DF.get(cardBin.bankCode).split(",")[1];//银行全称
			 }
			 String province = (rp.province == null || "".equals(rp.province)) ? "北京" : rp.province;//省
			 String city = (rp.city == null || "".equals(rp.city)) ? "北京市" : rp.city;//市
			 String amount = String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d);
			 String publicKey = PayConstant.PAY_CONFIG.get("KBNEW_PUBLICCHECKKEY_SIGN");//获得加密公钥
			 String detail_list = rp.id + "^" + RSA.encryptByPublicKey(rp.accountName, publicKey) + "^^" + RSA.encryptByPublicKey(rp.accNo,publicKey) + "^" + bankName + "^" 
					 + bankCode +
					 "^" + province + "^" + city + "^" + bankName + "^" + amount + "^C^DEBIT";
			 Map<String,String> paramsMap = new HashMap<String, String>();
			 paramsMap.put("service", service);
			 paramsMap.put("version", version);
			 paramsMap.put("request_time", request_time);
			 paramsMap.put("partner_id", partner_id);
			 paramsMap.put("_input_charset", _input_charset);
			 paramsMap.put("sign_type", sign_type);
			 paramsMap.put("sign_version", sign_version);
			 paramsMap.put("batch_no", batch_no);
			 paramsMap.put("detail_list", detail_list);
			 String str2sign = makeReqParamMap(paramsMap);//获取待签名数据
			 String sign = rsa.sign(str2sign);
			 paramsMap.put("sign", sign);
			 String requestString = getDfReqStr(paramsMap);
			 log.info("新酷宝代付：请求数据 "+requestString);
			 String responseString = tool.textDncode(tool.post(PayConstant.PAY_CONFIG.get("KBNEW_WY_PAY_URL"), requestString), "UTF-8");
			 log.info("新酷宝代付：同步响应数据 " + responseString);
			 Map<String, String> content = GsonUtil.fronJson2Map(responseString);
			 String sign_result = content.get("sign").toString();
			 String sign_type_result = content.get("sign_type").toString();
			 String _input_charset_result = content.get("_input_charset").toString();
			 content.remove("sign");
			 content.remove("sign_type");
			 content.remove("sign_version");
			 String like_result = Tools.createLinkString(content,false);
			 String signKey = PayConstant.PAY_CONFIG.get("KBNEW_WY_PUBLICCHECKKEY");//获取签名公钥
			 if (SignUtil.Check_sign(like_result.toString(), sign_result,sign_type_result, signKey, _input_charset_result)) {
				if("APPLY_SUCCESS".equals(content.get("batch_status")) && "APPLY_SUCCESS".equals(content.get("response_code"))){//请求成功
					rp.status="0";
	        		rp.errorMsg = "操作成功";
	        		try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
	        		KBNEWQueryThread kbnewQueryThread = new KBNEWQueryThread(payRequest);
	        		kbnewQueryThread.start();
				} else {
					rp.status="2";
					rp.errorMsg = "操作失败";
		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				}
			 }
		 }catch (Exception e) {
			 e.printStackTrace();
			 log.info(PayUtil.exceptionToString(e));
			 throw e;
		}
	 }
	 
    /**
	  * 代收付查询入口
	  * @param payRequest
	  * @param rp
	  * @return
	  * @throws Exception
	  */
	 public boolean receivePaySingleQuery(PayRequest payRequest,PayReceiveAndPay rp) throws Exception{
		 try {
			 //请求参数
			 String service = "query_b2c_batch_fundout_order";//服务名
			 String version = "1.0";//版本号
			 String request_time = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;//请求时间
			 String partner_id = PayConstant.PAY_CONFIG.get("KBNEW_WY_PARTNER_ID");//商户编号
			 String _input_charset = "UTF-8";//编码字符集
			 String sign_type = "RSA";//签名方式
			 String sign_version = "1.0";//签名版本号
			 String out_batch_no = rp.sn;//批次号
			 Map<String,String> paramsMap = new HashMap<String, String>();
			 paramsMap.put("service", service);
			 paramsMap.put("version", version);
			 paramsMap.put("request_time", request_time);
			 paramsMap.put("partner_id", partner_id);
			 paramsMap.put("_input_charset", _input_charset);
			 paramsMap.put("sign_type", sign_type);
			 paramsMap.put("sign_version", sign_version);
			 paramsMap.put("out_batch_no", out_batch_no);
			 RSAUtil rsa = new RSAUtil();
			 rsa.setPrivateKey(PayConstant.PAY_CONFIG.get("KBNEW_WY_PRIVATEKEYRSA"));
			 Tools tool = new Tools();
			 String str2sign = makeReqParamMap(paramsMap);//获取待签名数据
			 String sign = rsa.sign(str2sign);
			 paramsMap.put("sign", sign);
			 String requestString = getDfReqStr(paramsMap);//获得请求数据
			 log.info("新酷宝代付【查询】：请求数据 "+requestString);
			 String responseString = tool.textDncode(tool.post(PayConstant.PAY_CONFIG.get("KBNEW_WY_PAY_URL"), requestString), "UTF-8");
			 log.info("新酷宝代付【查询】：同步响应数据 " + responseString);
			 Map<String, String> content = GsonUtil.fronJson2Map(responseString);
			 String sign_result = content.get("sign").toString();
			 String sign_type_result = content.get("sign_type").toString();
			 String _input_charset_result = content.get("_input_charset").toString();
			 content.remove("sign");
			 content.remove("sign_type");
			 content.remove("sign_version");
			 String like_result = Tools.createLinkString(content,false);
			 String signKey = PayConstant.PAY_CONFIG.get("KBNEW_WY_PUBLICCHECKKEY");//获取签名公钥
			 if (SignUtil.Check_sign(like_result.toString(), sign_result,sign_type_result, signKey, _input_charset_result)) {
				 if("APPLY_SUCCESS".equals(content.get("response_code"))){
					 String fundout_list = content.get("fundout_list");
					 if(fundout_list == null || "".equals(fundout_list)){
						 throw new Exception("新酷宝代付：查询失败！");
					 }
					 String[] result = content.get("fundout_list").split("~");
					 if("SUCCESS".equals(result[4])){
						 payRequest.setRespCode("000");
						 payRequest.receiveAndPaySingle.setRespCode("000");
						 payRequest.receiveAndPaySingle.retCode="000";
						 payRequest.receivePayRes = "0";
						 payRequest.respDesc="交易成功";
						 rp.errorMsg = "交易成功";
						 return true;
					 }else if("FAILED".equals(result[4])){
						 payRequest.setRespCode("-1");
						 payRequest.receiveAndPaySingle.setRespCode("-1");
						 payRequest.receivePayRes = "-1";
						 payRequest.respDesc="交易失败";
						 rp.errorMsg = payRequest.respDesc;
					 }else if ("PROCESSING".equals(result[4])){
						 payRequest.setRespCode("0");
						 payRequest.respDesc="处理中";
						 rp.errorMsg = payRequest.respDesc;
					 }
				 }else throw new Exception ("新酷宝代付：查询失败！");
			 }else throw new Exception("验签失败");
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 return false;
	 }
	 public String doPost(String url ,Map<String,String> paramsMap,String charset){
			HttpClient httpClient = null;
	        String body = "";
	        try {
				httpClient = new HttpClient();
		        // 设置超时时间
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
				httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
				PostMethod postMethod = new PostMethod(url);
		        //装填参数  
		        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		        if(paramsMap != null){
		        	 for (Entry<String, String> entry : paramsMap.entrySet()) {  
		                 nvps.add(new NameValuePair(entry.getKey(), URLEncoder.encode(entry.getValue(),"UTF-8")));  
		             }
		        }
		        //设置参数到请求对象中  
		        postMethod.addParameters(nvps.toArray(new NameValuePair[nvps.size()]));
		        //设置header信息
		        //指定报文头【Content-type】、【User-Agent】  
		        //httpPost.setHeader("Content-type", "application/x-www-form-urlencoded;text/html;charset=UTF-8");  
		        //httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		        //执行请求操作，并拿到结果（同步阻塞）  
		        int executeMethod = httpClient.executeMethod(postMethod);
		        byte[] responseBody = postMethod.getResponseBody();
		        body = new String(responseBody, "UTF-8");
		        //获取结果实体
		        postMethod.releaseConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return body;
		}
	 /**
	  * 
	  * @param url
	  * @param json
	  * @param charset
	  * @return
	  */
	 public String doPost(String url ,String json,String charset){
			HttpClient httpClient = null;
	        String body = "";
	        try {
	        	httpClient = new HttpClient();
		        // 设置超时时间
				httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
				httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
				PostMethod postMethod = new PostMethod(url);
	        	postMethod.addRequestHeader(new Header("Content-Type","application/json; charset=utf-8"));
	        	RequestEntity entity = new StringRequestEntity(json,"application/json","UTF-8");
	        	postMethod.setRequestEntity(entity);
	        	httpClient.executeMethod(postMethod);
	        	body = postMethod.getResponseBodyAsString();
	        	postMethod.releaseConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return body;
		}
	/**
	 * 拼接待签名串
	 * @param paramMap
	 * @param key
	 * @return 返回排序，拼接好的字符串
	 */
    public String makeReqParamMap(Map<String, String> paramMap) {
        List<String> params = new ArrayList<String>();
        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(sortedkeys);
        for (String rk : sortedkeys) {
        	String value = paramMap.get(rk);
        	if("".equals(value) || "sign".equals(rk)|| "sign_type".equals(rk)|| "sign_version".equals(rk)){
        		continue;
        	}else{
        		params.add(rk + "=" + value);
        	}
        }
        String presign = StringUtils.join(params, "&");
        log.info("新酷宝待签名数据："+presign);
        return presign;
    }
	/**
	 * 得到post请求字符串
	 * @param paramMap
	 * @param key
	 * @return 代付post请求字符串
	 * @throws Exception 
	 */
    public String getDfReqStr(Map<String, String> paramMap) throws Exception {
        List<String> params = new ArrayList<String>();
        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(sortedkeys);
        for (String rk : sortedkeys) {
        	String value = paramMap.get(rk);
        	params.add(rk + "=" + URLEncoder.encode(value, "UTF-8"));
        }
        String presign = StringUtils.join(params, "&");
        return presign;
    }
}

/**
 * 查询代付 线程类
 * @author lqq
 *
 */
class KBNEWQueryThread extends Thread {
	
	private static final Log log = LogFactory.getLog(KBNEWQueryThread.class);
	
	private  PayRequest payRequest= new PayRequest();
	public KBNEWQueryThread (PayRequest payRequest) {
		this.payRequest = payRequest;
	}
	
	@Override
	public void run() {
		for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
			try {if(query())break;} catch (Exception e) {}
			log.info("新酷宝代付：查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
			try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
		}
	}
	public boolean query() throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//代付对象
			//请求参数
			 String service = "query_b2c_batch_fundout_order";//服务名
			 String version = "1.0";//版本号
			 String request_time = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) ;//请求时间
			 String partner_id = PayConstant.PAY_CONFIG.get("KBNEW_WY_PARTNER_ID");//商户编号
			 String _input_charset = "UTF-8";//编码字符集
			 String sign_type = "RSA";//签名方式
			 String sign_version = "1.0";//签名版本号
			 String out_batch_no = rp.sn;//批次号
			 Map<String,String> paramsMap = new HashMap<String, String>();
			 paramsMap.put("service", service);
			 paramsMap.put("version", version);
			 paramsMap.put("request_time", request_time);
			 paramsMap.put("partner_id", partner_id);
			 paramsMap.put("_input_charset", _input_charset);
			 paramsMap.put("sign_type", sign_type);
			 paramsMap.put("sign_version", sign_version);
			 paramsMap.put("out_batch_no", out_batch_no);
			 RSAUtil rsa = new RSAUtil();
			 rsa.setPrivateKey(PayConstant.PAY_CONFIG.get("KBNEW_WY_PRIVATEKEYRSA"));
			 Tools tool = new Tools();
			 String str2sign = makeReqParamMap(paramsMap);//获取待签名数据
			 String sign = rsa.sign(str2sign);
			 paramsMap.put("sign", sign);
			 String requestString = getDfReqStr(paramsMap);//获得请求数据
			 log.info("新酷宝代付【查询】：请求数据 "+requestString);
			 String responseString = tool.textDncode(tool.post(PayConstant.PAY_CONFIG.get("KBNEW_WY_PAY_URL"), requestString), "UTF-8");
			 log.info("新酷宝代付【查询】：同步响应数据 " + responseString);
			 Map<String, String> content = GsonUtil.fronJson2Map(responseString);
			 String sign_result = content.get("sign").toString();
			 String sign_type_result = content.get("sign_type").toString();
			 String _input_charset_result = content.get("_input_charset").toString();
			 content.remove("sign");
			 content.remove("sign_type");
			 content.remove("sign_version");
			 String like_result = Tools.createLinkString(content,false);
			 String signKey = PayConstant.PAY_CONFIG.get("KBNEW_WY_PUBLICCHECKKEY");//获取签名公钥
			 if (SignUtil.Check_sign(like_result.toString(), sign_result,sign_type_result, signKey, _input_charset_result)) {
				 if ("APPLY_SUCCESS".equals(content.get("response_code"))) {
					 String[] result = content.get("fundout_list").split("~");
					 if("SUCCESS".equals(result[4])){
						 rp.status = "1";
						 rp.respCode = "000";
						 payRequest.respCode = "000";
						 rp.errorMsg = "交易成功";
						 payRequest.respDesc = rp.errorMsg;
						 try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						 List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
						 list.add(rp);
						 new PayInterfaceOtherService().receivePayNotify(payRequest,list);
						 return true;
					 }else if("FAILED".equals(result[4])){
						 rp.status = "2";
						 rp.respCode = "-1";
						 rp.errorMsg = "交易失败";
						 try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						 List<PayReceiveAndPay> list2 = new ArrayList<PayReceiveAndPay>();
						 list2.add(rp);
						 new PayInterfaceOtherService().receivePayNotify(payRequest,list2);
						 return true;					 
					 }else{
						 return false;
					 }
				 }else {
					 return false;
				 }
			 }else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 拼接待签名串
	 * @param paramMap
	 * @param key
	 * @return 返回排序，拼接好的字符串
	 */
    public String makeReqParamMap(Map<String, String> paramMap) {
        List<String> params = new ArrayList<String>();
        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(sortedkeys);
        for (String rk : sortedkeys) {
        	String value = paramMap.get(rk);
        	if("".equals(value) || "sign".equals(rk)|| "sign_type".equals(rk)|| "sign_version".equals(rk)){
        		continue;
        	}else{
        		params.add(rk + "=" + value);
        	}
        }
        String presign = StringUtils.join(params, "&");
        log.info("新酷宝待签名数据："+presign);
        return presign;
    }
	/**
	 * 得到post请求字符串
	 * @param paramMap
	 * @param key
	 * @return 代付post请求字符串
	 * @throws Exception 
	 */
    public String getDfReqStr(Map<String, String> paramMap) throws Exception {
        List<String> params = new ArrayList<String>();
        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(sortedkeys);
        for (String rk : sortedkeys) {
        	String value = paramMap.get(rk);
        	params.add(rk + "=" + URLEncoder.encode(value, "UTF-8"));
        }
        String presign = StringUtils.join(params, "&");
        return presign;
    }
	
}