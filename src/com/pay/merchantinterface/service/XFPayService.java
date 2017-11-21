package com.pay.merchantinterface.service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.DataTransUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSONObject;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.xf.AESCoder;
import com.third.xf.UnRepeatCodeGenerator;
import com.ucf.sdk.UcfForOnline;
/**
 * 先锋接口服务类
 * @author xk
 *
 */
public class XFPayService {
	
	private static final Log log = LogFactory.getLog(XFPayService.class);
	private static String key = PayConstant.PAY_CONFIG.get("XF_KEY");
	private static String charset = "utf-8";
	// 唯一值处理-------------------start
	static byte [] cmb = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private static String getRadixOf62(long src){
		StringBuffer sb = new StringBuffer();		
		while(src > 0){
			int mod = (int) (src%cmb.length);
			sb.append(new String(new byte[]{cmb[mod]}));
			src = src/cmb.length;
		}
		return reverse(sb.toString());
	}
	private static String reverse(String str){   
	    int n=str.length();   
	    char []chars=new char[n];   
	    str.getChars(0, n, chars, 0);//获得了char[]可操作的数组.   
	       
	    int length=chars.length;   
	    StringBuffer sbStr=new StringBuffer();   
	    for(int i=0;i<length;i++){   
	        sbStr.append(chars[length-i-1]+"");//用StringBuffer将其逆转.   
	    }   
	    return sbStr.toString();//转换为String   
	}
	//(时间戳的62进制前缀 + 1开始的累加值，直到累加值大于countPerSecondS，然后重取得时间戳，累加值从1开始)
	private static String curTimeStr = getRadixOf62(System.currentTimeMillis());
	private static long identifySeed = 1;
	private static String countPerSecondS = "9999999";
	private static long countPerSecond = new Long(countPerSecondS);
	private static synchronized String getUniqueIdentify() {
		if (identifySeed >= countPerSecond) {
			identifySeed = 1;
			curTimeStr = getRadixOf62(System.currentTimeMillis());
		}
		String tmp = String.valueOf(identifySeed);
		int len = tmp.length();
		for(int i = 0; i<countPerSecondS.length()-len;i++)tmp = "0"+tmp;
		identifySeed ++;
		return curTimeStr+getRadixOf62(Long.parseLong("1"+tmp));
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try {
			Map<String, String> params = new TreeMap<String, String>();
			params.put("service", "REQ_PAY_BANK");
			params.put("secId", "RSA");
			params.put("version", PayConstant.PAY_CONFIG.get("XF_VERSION"));
			params.put("merchantId", PayConstant.PAY_CONFIG.get("XF_MERCHANT_ID"));
			params.put("reqSn", UnRepeatCodeGenerator.createUnRepeatCode(params.get("merchantId"), params.get("service"), payOrder.payordno));
			
			Map<String, String> busiParams = new HashMap<String, String>();//参与签名业务字段集合
			busiParams.put("merchantNo", payOrder.payordno);
			busiParams.put("source", "1");
			busiParams.put("amount", String.valueOf(payOrder.txamt));//单位分
			busiParams.put("transCur", "156");
			//对公对私默认对私
//			if("1".equals(request.getParameter("userType"))){
//				busiParams.put("userType", "1");//固定值：1或者2 （1：个人 2：企业）
//				if("0".equals(payOrder.bankCardType)){
//					busiParams.put("accountType", "1");//1:借记卡 2:信用卡 4:对公账户
//				}else if("1".equals(payOrder.bankCardType)){
//					busiParams.put("accountType", "2");//1:借记卡 2:信用卡 4:对公账户
//				}
//			}else if("2".equals(request.getParameter("userType"))){
//				busiParams.put("userType", "2");//固定值：1或者2 （1：个人 2：企业）
//				busiParams.put("accountType", "4");
//			}
			busiParams.put("userType", "1");//固定值：1或者2 （1：个人 2：企业）
			if("0".equals(payOrder.bankCardType)){
				busiParams.put("accountType", "1");//1:借记卡 2:信用卡 4:对公账户
			}else if("1".equals(payOrder.bankCardType)){
				busiParams.put("accountType", "2");//1:借记卡 2:信用卡 4:对公账户
			}
			busiParams.put("bankId", payOrder.bankcod);
			busiParams.put("productName", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			busiParams.put("returnUrl",  PayConstant.PAY_CONFIG.get("XF_RETURN_URL"));
			busiParams.put("noticeUrl", PayConstant.PAY_CONFIG.get("XF_NOTIFY_PAY_URL"));
			JSONObject jsonObject =new JSONObject();//参与加密的业务字段
			Iterator<String> iterator = busiParams.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = busiParams.get(key);
				jsonObject.put(key, value);
			}
			// AES加密
	    	String dataValue = AESCoder.encrypt(jsonObject.toString(), key);
			params.put("data", dataValue);
			String signValue = UcfForOnline.createSign(key, "sign", params, "RSA");
			params.put("sign", signValue);
			log.info("先锋网银下单原始请求数据："+jsonObject.toJSONString());
			log.info("先锋网银下单发送请求数据："+params);
			request.setAttribute("xfReqData", params);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		try{
			Map<String, String> params = new TreeMap<String, String>();
			params.put("service", "REQ_PAY_BANK");
			params.put("secId", "RSA");
			params.put("version", PayConstant.PAY_CONFIG.get("XF_VERSION"));
			params.put("merchantId", PayConstant.PAY_CONFIG.get("XF_MERCHANT_ID"));
			params.put("reqSn", UnRepeatCodeGenerator.createUnRepeatCode(params.get("merchantId"), params.get("service"), payOrder.payordno));
			
			Map<String, String> busiParams = new HashMap<String, String>();//参与签名业务字段集合
			busiParams.put("merchantNo", payOrder.payordno);
			busiParams.put("source", "1");
			busiParams.put("amount", String.valueOf(payOrder.txamt));//单位分
			busiParams.put("transCur", "156");
			if("1".equals(payRequest.userType)){
				busiParams.put("userType", "1");//固定值：1或者2 （1：个人 2：企业）
				if("0".equals(payRequest.accountType)){
					busiParams.put("accountType", "1");//1:借记卡 2:信用卡 4:对公账户
				}else if("1".equals(payRequest.accountType)){
					busiParams.put("accountType", "2");//1:借记卡 2:信用卡 4:对公账户
				}
			}else if("2".equals(payRequest.userType)){
				busiParams.put("userType", "2");//固定值：1或者2 （1：个人 2：企业）
				busiParams.put("accountType", "4");
			}
			busiParams.put("bankId", payOrder.bankcod);
			busiParams.put("productName", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			busiParams.put("returnUrl",  PayConstant.PAY_CONFIG.get("XF_RETURN_URL"));
			busiParams.put("noticeUrl", PayConstant.PAY_CONFIG.get("XF_NOTIFY_PAY_URL"));
			JSONObject jsonObject =new JSONObject();//参与加密的业务字段
			Iterator<String> iterator = busiParams.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = busiParams.get(key);
				jsonObject.put(key, value);
			}
			// AES加密
	    	String dataValue = AESCoder.encrypt(jsonObject.toString(), key);
			params.put("data", dataValue);
			String signValue = UcfForOnline.createSign(key, "sign", params, "RSA");
			params.put("sign", signValue);
			log.info("先锋网银下单原始请求数据："+jsonObject.toJSONString());
			log.info("先锋网银下单发送请求数据："+params);
			request.setAttribute("xfReqData", params);
			new PayOrderService().updateOrderForBanks(payOrder);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
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
	public String quickPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		try {
			Map<String, String> params = new TreeMap<String, String>();
			params.put("service", "REQ_PAY_QUICK_APPLY");
			params.put("secId", "RSA");
			params.put("version", PayConstant.PAY_CONFIG.get("XF_VERSION"));
			params.put("merchantId", PayConstant.PAY_CONFIG.get("XF_MERCHANT_ID"));
			params.put("reqSn", UnRepeatCodeGenerator.createUnRepeatCode(params.get("merchantId"), params.get("service"), payOrder.payordno));
			
			Map<String, String> busiParams = new HashMap<String, String>();//参与签名业务字段集合
			busiParams.put("merchantNo", payOrder.payordno);
			busiParams.put("amount", String.valueOf(payRequest.merchantOrderAmt));//单位分
			busiParams.put("transCur", "156");
			busiParams.put("certificateType", "0");
			busiParams.put("certificateNo", payRequest.credentialNo);
			busiParams.put("accountNo", payRequest.cardNo);
			busiParams.put("accountName", payRequest.userName);
			busiParams.put("mobileNo", payRequest.userMobileNo);
			busiParams.put("productName", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			
			JSONObject jsonObject =new JSONObject();//参与加密的业务字段
			String str = "";
			Iterator<String> iterator = busiParams.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = busiParams.get(key);
				jsonObject.put(key, value);
			}
			// AES加密
	    	String dataValue = AESCoder.encrypt(jsonObject.toString(),key);
			params.put("data", dataValue);
			String signValue = UcfForOnline.createSign(key, "sign", params, "RSA");
			params.put("sign", signValue);
			log.info("先锋网银下单原始请求数据："+jsonObject.toJSONString());
			log.info("先锋快捷下单发送请求数据："+params);
			Iterator<String> iterator2 = params.keySet().iterator();
			while(iterator2.hasNext()){
				String key = iterator2.next();
				String value = params.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("XF_PAY_UTL"), str.substring(0, str.length()-1).getBytes(charset)),charset);
			log.info("先锋快捷下单响应数据："+resData);
			String deResData = AESCoder.decrypt(resData, key);
			log.info("先锋快捷下单响应解密后的数据："+deResData);
			/**
			 * {"amount":"300","transCur":"156",
			 * "status":"I","tradeNo":"201707211515141031610000975253","resCode":"00000","resMessage":"成功","merchantId":"M200000550","merchantNo":"J5DIB0QB3JLXPT2QH"}
			 */
			JSONObject resJson = (JSONObject) JSONObject.parse(deResData);
			if("00000".equals(resJson.getString("resCode"))){
				if("I".equals(resJson.getString("status")) || "S".equals(resJson.getString("status"))){
					payOrder.bankerror = resJson.getString("retMsg");
					return  "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
							"respCode=\""+("I".equals(resJson.getString("status"))?"000":"-1")+"\" respDesc=\"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
				} else return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" " +
				"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
				"respCode=\"-1\" respDesc=\""+resJson.getString("resMessage")+"\" " +
				"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			}else return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
			"<message merchantId=\""+payRequest.merchantId+"\" " +
			"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
			"respCode=\"-1\" respDesc=\""+resJson.getString("resMessage")+"\" " +
			"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
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
	public String certPayConfirm(PayRequest payRequest) throws Exception {
		try {
			Map<String, String> params = new TreeMap<String, String>();
			params.put("service", "REQ_PAY_QUICK_CONFIRM");
			params.put("secId", "RSA");
			params.put("version", PayConstant.PAY_CONFIG.get("XF_VERSION"));
			params.put("merchantId", PayConstant.PAY_CONFIG.get("XF_MERCHANT_ID"));
			params.put("reqSn", UnRepeatCodeGenerator.createUnRepeatCode(params.get("merchantId"), params.get("service"), payRequest.payOrder.payordno));
			
			Map<String, String> busiParams = new HashMap<String, String>();//参与签名业务字段集合
			busiParams.put("merchantNo", payRequest.payOrder.payordno);
			busiParams.put("checkCode", payRequest.checkCode);
			
			JSONObject jsonObject =new JSONObject();//参与加密的业务字段
			String str = "";
			Iterator<String> iterator = busiParams.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = busiParams.get(key);
				jsonObject.put(key, value);
			}
			// AES加密
	    	String dataValue = AESCoder.encrypt(jsonObject.toString(),key);
			params.put("data", dataValue);
			String signValue = UcfForOnline.createSign(key, "sign", params, "RSA");
			params.put("sign", signValue);
			log.info("先锋快捷确认请求数据："+params);
			Iterator<String> iterator2 = params.keySet().iterator();
			while(iterator2.hasNext()){
				String key = iterator2.next();
				String value = params.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("XF_PAY_UTL"), str.substring(0, str.length()-1).getBytes(charset)),charset);
			log.info("先锋快捷确认响应数据："+resData);
			String deResData = AESCoder.decrypt(resData, key);
			log.info("先锋快捷确认响应解密后的数据："+deResData);
			/**
			 * {"amount":"10","transCur":"156",
			 * "status":"I","tradeNo":"201707201103221031610000974379","tradeTime":"20170720110323","resCode":"00000","resMessage":"成功","merchantId":"M200000550","merchantNo":"1500519786268"}
			 */
			JSONObject resJson = (JSONObject) JSONObject.parse(deResData);
			return resJson.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 渠道补单（网银快捷查单）
	 * @param payOrder
	 * @throws Exception
	 */
	public void channelQuery(PayOrder payOrder, String serviceType) throws Exception{
		Map<String, String> params = new TreeMap<String, String>();
		params.put("service", serviceType);//网银：REQ_ORDER_QUERY_BY_ID  快捷：REQ_QUICK_QUERY_BY_ID
		params.put("secId", "RSA");
		params.put("version", PayConstant.PAY_CONFIG.get("XF_VERSION"));
		params.put("merchantId", PayConstant.PAY_CONFIG.get("XF_MERCHANT_ID"));
		params.put("merchantNo", payOrder.payordno);
		params.put("reqSn", UnRepeatCodeGenerator.createUnRepeatCode(params.get("merchantId"), params.get("service"), params.get("merchantNo")));
		
		String signValue = UcfForOnline.createSign(key, "sign", params, "RSA");
		params.put("sign", signValue);
		log.info("先锋网银查单请求数据："+params);
		String str = "";
		Iterator<String> iterator2 = params.keySet().iterator();
		while(iterator2.hasNext()){
			String key = iterator2.next();
			String value = params.get(key);
			str += key+"="+URLEncoder.encode(value,charset)+"&";
		}
		String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("XF_PAY_UTL"), str.substring(0, str.length()-1).getBytes(charset)),charset);
		log.info("先锋网银查单响应数据："+resData);
		String deResData = AESCoder.decrypt(resData, key);
		log.info("先锋网银查单响应解密后的数据："+deResData);
		/**
		 * {"transCur":"156","memo":"","tradeNo":"201707211451321031610000975216",
		 * "status":"S","tradeTime":"20170721145134","amount":"1","merchantId":"M200000550","merchantNo":"J5DIB0QB3JLXPT2PU"}
		 */
		JSONObject resJson = (JSONObject) JSONObject.parse(deResData);
		if("S".equals(resJson.getString("status"))){
			 payOrder.ordstatus="01";//支付成功
		     new NotifyInterface().notifyMer(payOrder);//支付成功
		} else if("F".equals(resJson.getString("status"))){
			 payOrder.ordstatus="02";//支付失败
		     new NotifyInterface().notifyMer(payOrder);
		}
	}
	/**
	 * 单笔代付
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingle(PayRequest payRequest) throws Exception{
		 try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
		 	PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
		 	
		 	Map<String, String> params = new TreeMap<String, String>();
			params.put("service", "REQ_WITHDRAW");
			params.put("secId", "RSA");
			params.put("version", PayConstant.PAY_CONFIG.get("XF_VERSION"));
			params.put("merchantId", PayConstant.PAY_CONFIG.get("XF_MERCHANT_ID"));
			params.put("reqSn", UnRepeatCodeGenerator.createUnRepeatCode(params.get("merchantId"), params.get("service"), rp.channelTranNo));
			
			Map<String, String> busiParams = new HashMap<String, String>();//参与签名业务字段集合
			busiParams.put("merchantId", params.get("merchantId"));
			busiParams.put("merchantNo", rp.channelTranNo);
			busiParams.put("amount", payRequest.amount);//单位分
			busiParams.put("transCur", "156");
			if("0".equals(payRequest.accountProp)){
				busiParams.put("userType", "1");//固定值：1或者2 （1：对私 2：对公）
			}else{
				busiParams.put("userType", "2");//固定值：1或者2 （1：对私 2：对公）
				busiParams.put("issuer", "");//联行号（注：当userType=2时联行号为必填项）
			}
			busiParams.put("accountNo", rp.accountNo);
			busiParams.put("accountName", rp.accountName);
			busiParams.put("bankNo", cardBin.bankCode);
			busiParams.put("noticeUrl", PayConstant.PAY_CONFIG.get("XF_PAYRECEIVEANDPAY_NOTIFY_URL"));
			
			JSONObject jsonObject = new JSONObject();//参与加密的业务字段
			String str = "";
			Iterator<String> iterator = busiParams.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = busiParams.get(key);
				jsonObject.put(key, value);
			}
			// AES加密
	    	String dataValue = AESCoder.encrypt(jsonObject.toString(),key);
			params.put("data", dataValue);
			String signValue = UcfForOnline.createSign(key, "sign", params, "RSA");
			params.put("sign", signValue);
			
			Iterator<String> iterator2 = params.keySet().iterator();
			while(iterator2.hasNext()){
				String key = iterator2.next();
				String value = params.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			log.info("先锋单笔代付请求数据："+str.substring(0, str.length()-1));
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("XF_PAY_UTL"), str.substring(0, str.length()-1).getBytes(charset)),charset);
			log.info("先锋单笔代付响应数据："+resData);
			String deResData = AESCoder.decrypt(resData, key); 
			log.info("先锋单笔代付响应解密后数据："+deResData);
		 	/**
		 	 * {"transCur":"156","tradeNo":"201707211453551031410005112474",
		 	 * "status":"I","resMessage":"订单处理中","amount":"3","resCode":"00002","merchantId":"M200000550","merchantNo":"1000000_1500620006777"}
		 	 */
			JSONObject resJson = (JSONObject) com.alibaba.fastjson.JSONObject.parse(deResData);
			if("S".equals(resJson.getString("status"))){
				rp.status="1";
				rp.setRetCode("000");
				rp.errorMsg = "交易成功";
       			payRequest.receivePayRes = "0";
	        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			} else if("I".equals(resJson.getString("status"))) {
				XFQueryThread xfquerythread = new XFQueryThread(payRequest, "REQ_WITHDRAW_QUERY_BY_ID");
				xfquerythread.start();
			} else{
				rp.status="2";
				rp.setRetCode("-1");
				rp.errorMsg = "操作失败";
       			payRequest.receivePayRes = "-1";
	        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	 }
	 /**
	 * 单笔代扣
	 * @param payRequest
	 * @param payReceiveAndPaySign
	 * @return
	 * @throws Exception
	 */
	 public String receivePaySingleInfo(PayRequest payRequest) throws Exception{
		 try {
			PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
		 	PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
		 	PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
		 	
		 	rp.sn = getUniqueIdentify();
		 	rp.batNo = "YCF"+new SimpleDateFormat("yyMMdd").format(new Date())+rp.sn;
		 	dao.updatePayReceiveAndPaySn(rp);
        	List<PayReceiveAndPay> AuthList = new ArrayList<PayReceiveAndPay>();
        	AuthList.add(rp);
			 
			Map<String, String> params = new TreeMap<String, String>();
			params.put("service", "REQ_WITHOIDING");
			params.put("secId", "RSA");
			params.put("version", PayConstant.PAY_CONFIG.get("XF_VERSION"));
			params.put("merchantId", PayConstant.PAY_CONFIG.get("XF_MERCHANT_ID"));
			params.put("reqSn", UnRepeatCodeGenerator.createUnRepeatCode(params.get("merchantId"), params.get("service"), payRequest.receiveAndPaySingle.id));
			//参与签名业务字段集合
			Map<String, String> busiParams = new HashMap<String, String>();
			busiParams.put("merchantNo", rp.channelTranNo);
			busiParams.put("amount", payRequest.amount);//单位分
			busiParams.put("transCur", "156");
			if("0".equals(payRequest.accountProp)){
				busiParams.put("userType", "1");//固定值：1或者2 （1：对私 2：对公）
				busiParams.put("accountType", "1");
				busiParams.put("certificateType", "0");
				busiParams.put("certificateNo", rp.certId);
			}else{
				busiParams.put("userType", "2");//固定值：1或者2 （1：对私 2：对公）
				busiParams.put("accountType", "4");
				busiParams.put("branchProvince", rp.province);
				busiParams.put("branchCity", rp.city);
				busiParams.put("branchName", rp.bankName);
			}
			busiParams.put("accountNo", rp.accountNo);
			busiParams.put("accountName", rp.accountName);
			busiParams.put("bankId", cardBin.bankCode);
			busiParams.put("productName", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			busiParams.put("noticeUrl", PayConstant.PAY_CONFIG.get("XF_PAYRECEIVEANDPAY_NOTIFY_URL"));
			JSONObject jsonObject =new JSONObject();//参与加密的业务字段
			String str = "";
			Iterator<String> iterator = busiParams.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = busiParams.get(key);
				jsonObject.put(key, value);
			}
			// AES加密
	    	String dataValue = AESCoder.encrypt(jsonObject.toString(), key);
			params.put("data", dataValue);
			String signValue = UcfForOnline.createSign(key, "sign", params, "RSA");
			params.put("sign", signValue);
			log.info("先锋单笔代扣请求数据："+params);
			Iterator<String> iterator2 = params.keySet().iterator();
			while(iterator2.hasNext()){
				String key = iterator2.next();
				String value = params.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("XF_PAY_UTL"), str.substring(0, str.length()-1).getBytes(charset)),charset);
			log.info("先锋单笔代扣响应数据："+resData);
			String deResData = AESCoder.decrypt(resData, key);
			log.info("先锋单笔代扣响应解密后的数据："+deResData);
			/**
			 * {"transCur":"156","tradeNo":"201707211459401031610000975234",
			 * "status":"S","tradeTime":"20170721145942","resMessage":"成功","amount":"200","resCode":"00000","merchantId":"M200000550","merchantNo":"1000000_1500620354485"}
			 */
			JSONObject resJson = (JSONObject) com.alibaba.fastjson.JSONObject.parse(deResData);
			if("00000".equals(resJson.getString("resCode"))){
				if("S".equals(resJson.getString("status"))) {
					AuthList.get(0).status="1";
					AuthList.get(0).setRetCode("000");
					AuthList.get(0).errorMsg = "交易成功";
	       			payRequest.receivePayRes = "0";
	       			dao.updatePayReceiveAndPay(AuthList);
				} else if("I".equals(resJson.getString("status"))){
					XFQueryThread xfquerythread = new XFQueryThread(payRequest, "REQ_WITHOIDING_QUERY");
					xfquerythread.start();
				} else {
					AuthList.get(0).status="2";
					AuthList.get(0).setRetCode("-1");
					AuthList.get(0).errorMsg = resJson.getString("resMessage");
	       			payRequest.receivePayRes = "-1";
	       			dao.updatePayReceiveAndPay(AuthList);
				}
			} else {
				AuthList.get(0).status="2";
				AuthList.get(0).setRetCode("-1");
				AuthList.get(0).errorMsg = resJson.getString("resMessage");
       			payRequest.receivePayRes = "-1";
       			dao.updatePayReceiveAndPay(AuthList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
			
		}
		return null;
	 }
	 /**
	 * 单笔代收/付查询
	 * @param request
	 * @param rp
	 * @throws Exception
	 */
	public void receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp, String serviceType) throws Exception{
		try {
			Map<String, String> params = new TreeMap<String, String>();
			params.put("service", serviceType);//代付：REQ_WITHDRAW_QUERY_BY_ID   代扣：REQ_WITHOIDING_QUERY
			params.put("secId", "RSA");
			params.put("version", PayConstant.PAY_CONFIG.get("XF_VERSION"));
			params.put("merchantNo", rp.channelTranNo);
			params.put("merchantId", PayConstant.PAY_CONFIG.get("XF_MERCHANT_ID"));
			params.put("reqSn", UnRepeatCodeGenerator.createUnRepeatCode(params.get("merchantId"), params.get("service"), params.get("merchantNo")));
			String signValue = UcfForOnline.createSign(key, "sign", params, "RSA");
			params.put("sign", signValue);
			log.info("先锋单笔代收/付查单请求数据："+params);
			String str = "";
			Iterator<String> iterator = params.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = params.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("XF_PAY_UTL"), str.substring(0, str.length()-1).getBytes(charset)),charset);
			log.info("先锋单笔代收/付查单响应数据："+resData);
			String deResData = AESCoder.decrypt(resData, key);
			log.info("先锋单笔代收/付查单解密后响应数据："+deResData);
			/**
			 * {"transCur":"156","status":"S","tradeNo":"201707211453551031410005112474","tradeTime":"20170721145401","noticeUrl":"http://www.qtongpay.com:58080/pay/XFNotify.htm","resMessage":"成功","withdrawList":[],"amount":"3",
			 * "resCode":"00000","merchantId":"M200000550","merchantNo":"1000000_1500620006777"}
			 */
			JSONObject resJson = (JSONObject) com.alibaba.fastjson.JSONObject.parse(deResData);
			if("00000".equals(resJson.getString("resCode"))){
				if("S".equals(resJson.getString("status"))){
					request.setRespCode("000");
					rp.setRespCode("000");
					request.receivePayRes = "0";
					request.respDesc="交易成功";
					rp.errorMsg = "交易成功";
				//{"transCur":"156","tradeNo":"201709011334091031610038302833","status":"F","tradeTime":"20170901133414","resMessage":"交易失败，详情请咨询发卡行","amount":"1998400","resCode":"10049","merchantId":"M200004103","merchantNo":"1004297_J6ZS8VIE3JLXPT2TD"}
				} else if("F".equals(resJson.getString("status"))){
					request.setRespCode("-1");
					rp.setRespCode("000");
					request.receivePayRes = "-1";
					request.respDesc = resJson.getString("resMessage");
					rp.errorMsg = request.respDesc;
				}
			} else {
				request.setRespCode("-1");
				rp.setRespCode("000");
				request.receivePayRes = "-1";
				request.respDesc = resJson.getString("resMessage");
				rp.errorMsg = request.respDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
class XFQueryThread extends Thread{
	private static final Log log = LogFactory.getLog(XFQueryThread.class);
	private  PayRequest payRequest= new PayRequest();
	private String serviceType;
	public XFQueryThread(){};
	public XFQueryThread(PayRequest payRequest ,String serviceType){
		this.payRequest = payRequest;
		this.serviceType = serviceType;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("先锋代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query()throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
			Map<String, String> params = new TreeMap<String, String>();
			params.put("service", serviceType);//代付：REQ_WITHDRAW_QUERY_BY_ID   代扣：REQ_WITHOIDING_QUERY
			params.put("secId", "RSA");
			params.put("version", PayConstant.PAY_CONFIG.get("XF_VERSION"));
			params.put("merchantNo", rp.channelTranNo);
			params.put("merchantId", PayConstant.PAY_CONFIG.get("XF_MERCHANT_ID"));
			params.put("reqSn", UnRepeatCodeGenerator.createUnRepeatCode(params.get("merchantId"), params.get("service"), params.get("merchantNo")));
			String signValue = UcfForOnline.createSign(PayConstant.PAY_CONFIG.get("XF_KEY"), "sign", params, "RSA");
			params.put("sign", signValue);
			log.info("先锋单笔代收/付查单请求数据："+params);
			String str = "";
			Iterator<String> iterator = params.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = params.get(key);
				str += key+"="+URLEncoder.encode(value,"utf-8")+"&";
			}
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("XF_PAY_UTL"), str.substring(0, str.length()-1).getBytes("utf-8")),"utf-8");
			log.info("先锋单笔代收/付查单响应数据："+resData);
			String deResData = AESCoder.decrypt(resData, PayConstant.PAY_CONFIG.get("XF_KEY"));
			log.info("先锋单笔代收/付查单解密后响应数据："+deResData);
			/**
			 * {"transCur":"156","status":"S","tradeNo":"201707211453551031410005112474","tradeTime":"20170721145401","noticeUrl":"http://www.qtongpay.com:58080/pay/XFNotify.htm","resMessage":"成功","withdrawList":[],"amount":"3",
			 * "resCode":"00000","merchantId":"M200000550","merchantNo":"1000000_1500620006777"}
			 */
			JSONObject resJson = (JSONObject) com.alibaba.fastjson.JSONObject.parse(deResData);
			if("00000".equals(resJson.getString("resCode"))){
				if("S".equals(resJson.getString("status"))){
					rp.status="1";
					rp.respCode="000";
					rp.errorMsg="交易成功";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
					list.add(rp);
					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					return true;
				} else if("F".equals(resJson.getString("status"))){
					rp.status="2";
					rp.respCode="-1";
					rp.errorMsg = resJson.getString("resMessage");
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					return true;
				}
			} if("00002".equals(resJson.getString("resCode"))){//订单处理中
				if("I".equals(resJson.getString("status")))return false;
			} else {
				rp.status="2";
				rp.respCode="-1";
				rp.errorMsg="查询失败";
				try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
				list.add(rp);
				new PayInterfaceOtherService().receivePayNotify(payRequest,list);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}