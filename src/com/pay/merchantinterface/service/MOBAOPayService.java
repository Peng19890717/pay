package com.pay.merchantinterface.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jweb.dao.Blog;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
import com.third.mobao.AESUtil;
import com.third.mobao.DateUtil;
import com.third.mobao.EncodeUtil;
import com.third.mobao.MD5Util;
import com.third.reapal.Decipher;
import com.third.reapal.ReapalSubmit;
/**
 * 摩宝接口服务类
 * @author Administrator
 *
 */
public class MOBAOPayService {
	private static final Log log = LogFactory.getLog(MOBAOPayService.class);
	static	HttpClient   client = new HttpClient();
	static Map<String, String> CERT_TYPE_MATCHE = new HashMap<String, String>();
	static {
		CERT_TYPE_MATCHE.put("01","01");//‘01’身份证
		CERT_TYPE_MATCHE.put("02","03");//‘02’护照
		CERT_TYPE_MATCHE.put("03","02");//‘03’军官证
		CERT_TYPE_MATCHE.put("04","06");//‘04’港澳居民往来内地通行证
		CERT_TYPE_MATCHE.put("05","05");//‘05’台湾居民来往大陆通行证
		CERT_TYPE_MATCHE.put("06","06");//‘06’其他
		CERT_TYPE_MATCHE.put("07","06");
		CERT_TYPE_MATCHE.put("99","06");
	}
		/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_MOBAO"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try{
	        new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 商户接口下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_MOBAO"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		new PayOrderService().updateOrderForBanks(payOrder);
	}
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
		//商户号码
		//商户号私钥
		Map<String,String>  transmap= new LinkedHashMap<String, String>();
		transmap.put("versionId",PayConstant.PAY_CONFIG.get("mb_version"));
		transmap.put("businessType","1401");
		transmap.put("insCode","");
		transmap.put("merId",PayConstant.PAY_CONFIG.get("mb_merchant_id_quick"));
		transmap.put("orderId",DateUtil.getTimess1());
		transmap.put("transDate",DateUtil.getTimess1());
		transmap.put("transAmount",String.valueOf(payRequest.merchantOrderAmt));
		transmap.put("cardByName",MD5Util.encode(payRequest.userName.getBytes("UTF-8")));  //此处的MD5util为Base64加密
		transmap.put("cardByNo",payRequest.cardNo);
		if(PayConstant.CARD_BIN_TYPE_DAIJI.equals(payRequest.payOrder.bankCardType)){
			transmap.put("cardType","00");
			transmap.put("expireDate",payRequest.validPeriod);
			transmap.put("CVV",payRequest.cvv2);
		} else if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payRequest.payOrder.bankCardType))transmap.put("cardType","01");
		transmap.put("bankCode","");
		transmap.put("openBankName","");
		transmap.put("cerType",CERT_TYPE_MATCHE.get(payRequest.credentialType));
		transmap.put("cerNumber",payRequest.credentialNo);
		transmap.put("mobile",payRequest.userMobileNo);
		transmap.put("isAcceptYzm","00");//发送验证码，01不发送,00 发送
		transmap.put("pageNotifyUrl",PayConstant.PAY_CONFIG.get("mb_quick_notify_url"));
		transmap.put("backNotifyUrl","");
//		transmap.put("orderDesc",payRequest.merchantOrderDesc);
		transmap.put("orderDesc",PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
				"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
		transmap.put("dev","");
		transmap.put("fee","");
		//需要加密的字符串
		String signstr=EncodeUtil.getUrlStr(transmap);
		String  signtrue=MD5Util.MD5Encode(signstr+PayConstant.PAY_CONFIG.get("mb_key_quick"));
		transmap.put("signType",PayConstant.PAY_CONFIG.get("mb_sign_type"));
		transmap.put("signData",signtrue);
		//AES加密
		String  transData=EncodeUtil.getUrlStr(transmap);
		String  reqMsg=AESUtil.encrypt(transData, PayConstant.PAY_CONFIG.get("mb_key_quick"));
		//获取交易返回结果{"status":"01","refCode":"000017","refMsg":"证件号不能为空或者与规定长度不符"}
		//
		String res = requestBody(PayConstant.PAY_CONFIG.get("mb_merchant_id_quick"),reqMsg);
		log.info("解密返回的数据==========>" + res);
		JSONObject jsonObject = JSON.parseObject(res);
		String resultCode = jsonObject.getString("result_code");
		return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" " +
				"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\""+jsonObject.getString("bind_id")+"\" " +
				"respCode=\""+("0000".equals(resultCode)?"000":"-1")+"\" respDesc=\""+jsonObject.getString("result_msg")+"\" " +
				"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		Blog log = new Blog();
		try{
			//{"merchant_id":"10000000000147","order_no":"pJCE6Rj2Q3rKTOG","result_code":"0000","status":"completed","timestamp":"2016-04-28 17:28:30","total_fee":"1000","trade_no":"10160428000012769"}
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));
			map.put("version",PayConstant.PAY_CONFIG.get("rb_version"));
			map.put("order_no",payOrder.payordno);
			//返回结果
			String post = ReapalSubmit.buildSubmit(map,PayConstant.PAY_CONFIG.get("rb_merchant_id")
					,PayConstant.PAY_CONFIG.get("rb_gateway")+"/fast/search",PayConstant.PAY_CONFIG.get("rb_key"));
		    log.info("返回结果post==========>" + post);
		    //解密返回的数据
		    String res = Decipher.decryptData(post);
		    JSONObject jsonObject = JSON.parseObject(res);
		    log.info("解密返回的数据==========>" + res);
	        payOrder.actdat = new Date();
	        String resMsg = jsonObject.getString("result_msg");
	        if("completed".equals(jsonObject.getString("status"))){
	        	payOrder.ordstatus="01";//支付成功
	        	payOrder.bankjrnno = jsonObject.getString("trade_no");
	        	new NotifyInterface().notifyMer(payOrder);//支付成功
	        }
	        else throw new Exception("支付渠道状态："+jsonObject.getString("status")+(resMsg==null?"":"（"+resMsg+"）"));
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 快捷支付-短信验证码重发
	 * @param request
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String resendCheckCode(PayRequest payRequest) throws Exception{
//		Blog log = new Blog();
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id_quick"));
//		map.put("version",PayConstant.PAY_CONFIG.get("rb_version"));
//		//订单号
//		map.put("order_no",payRequest.payOrder.payordno);  
//		//返回结果
//		String post = ReapalSubmit.buildSubmit(map,PayConstant.PAY_CONFIG.get("rb_merchant_id_quick")
//				,PayConstant.PAY_CONFIG.get("rb_gateway")+"/fast/sms",PayConstant.PAY_CONFIG.get("rb_key_quick"));
//	    log.info("返回结果post==========>" + post);
//	    //解密返回的数据==========>{"merchant_id":"100000000011015","order_no":"20160509175748","phone":"","result_code":"0000","result_msg":"发送成功"}
//	    String res = Decipher.decryptData(post);
//	    log.info("解密返回的数据==========>" + res);
//	    return res;
		return null;
	}
	/**
	 * 快捷支付-支付确认
	 * @param request
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String certPayConfirm(PayRequest payRequest) throws Exception{
//		Blog log = new Blog();
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id_quick"));
//		map.put("version",PayConstant.PAY_CONFIG.get("rb_version"));
//		map.put("order_no",payRequest.payOrder.payordno);
//		map.put("check_code",payRequest.checkCode); //6位
//		//返回结果
//		String post = ReapalSubmit.buildSubmit(map,PayConstant.PAY_CONFIG.get("rb_merchant_id_quick")
//			,PayConstant.PAY_CONFIG.get("rb_gateway")+"/fast/pay",PayConstant.PAY_CONFIG.get("rb_key_quick"));
//	    log.info("返回结果post==========>" + post);
//	    //解密返回的数据==========>{"order_no":"20160509175748","result_code":"3069","result_msg":"验证码错误"}
//	    String res = Decipher.decryptData(post);
//	    log.info("解密返回的数据==========>" + res);
//	    return res;
		return null;
	}
	private static String requestBody(String merId,String transData){
		PostMethod  method= new PostMethod(PayConstant.PAY_CONFIG.get("mb_gateway"));
		method.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");    
		method.setParameter("merId",merId);
		method.setParameter("transData",transData);
		client.setConnectionTimeout(8000);
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != 200) {
				log.info("statusCode=" + statusCode);
				return null;
		    }else{
		    	String resp = method.getResponseBodyAsString();
		    	log.info("resp="+resp);
		    	return resp;
		    }
		} catch (Exception e){
			e.printStackTrace();
			return ""+e.getMessage();
		}
	}
	/***
	 * 
	 * @param url
	 * @param map
	 * @param charSet
	 * @return
	 */
	private static  String POSTReturnString(String url, Map<String, String> map,String charSet) {
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=" + charSet);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			method.setParameter(entry.getKey(), entry.getValue());
		}
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != 200) {
				log.info("statusCode=" + statusCode);
				return null;
			} else {
				String resp = method.getResponseBodyAsString();
				log.info("resp=" + resp);
				return resp;
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return "" + e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return "" + e.getMessage();
		}
	}
}
