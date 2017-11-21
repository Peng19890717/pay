package com.pay.merchantinterface.service;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
import com.third.yhj.HmacArray;
import com.third.yhj.YHJUtils;
/**
 * 易汇金接口服务类
 * @author Administrator
 */
public class YHJPayService {
	private static final Log log = LogFactory.getLog(YHJPayService.class);
	public static Map<String,String> BANK_MAP_B2C = new HashMap<String,String>();//b2c
	public static Map<String,String> BANK_MAP_B2B = new HashMap<String,String>();//b2b
	static final String[] ORDER_RESPONSE_HMAC_FIELDS = {"merchantId","requestId","status","scanCode","appParams","redirectUrl"};
	static final Object[] QUERY_RESPONSE_HMAC_FIELDS = { "merchantId", "requestId", "serialNumber", "totalRefundCount", "totalRefundAmount", "orderCurrency", "orderAmount", "status", "completeDateTime", "remark", "bindCardId", HmacArray.create("subOrders", new String[] { "requestId", "orderAmount", "serialNumber" }) };
	static{
		BANK_MAP_B2C.put("PSBC","BANK_CARD-B2C-POST-P2P");//邮政储蓄银行
		BANK_MAP_B2C.put("","BANK_CARD-B2C-SDB-P2P");//深圳发展银行
		BANK_MAP_B2C.put("CMBC","BANK_CARD-B2C-CMBC-P2P");//民生银行
		BANK_MAP_B2C.put("BCCB","BANK_CARD-B2C-BCCB-P2P");//北京银行
		BANK_MAP_B2C.put("BOS","BANK_CARD-B2C-SHB-P2P");//上海银行
		BANK_MAP_B2C.put("CMB","BANK_CARD-B2C-CMBCHINA-P2P");//招商银行
		BANK_MAP_B2C.put("CNCB","BANK_CARD-B2C-ECITIC-P2P");//中信银行
		BANK_MAP_B2C.put("SPDB","BANK_CARD-B2C-SPDB-P2P");//浦发银行
		BANK_MAP_B2C.put("CIB","BANK_CARD-B2C-CIB-P2P");//兴业银行
		BANK_MAP_B2C.put("HXB","BANK_CARD-B2C-HXB-P2P");//华夏银行
		BANK_MAP_B2C.put("ABC","BANK_CARD-B2C-ABC-P2P");//农业银行
		BANK_MAP_B2C.put("GDB","BANK_CARD-B2C-GDB-P2P");//广发银行
		BANK_MAP_B2C.put("ICBC","BANK_CARD-B2C-ICBC-P2P");//工商银行
		BANK_MAP_B2C.put("BOC","BANK_CARD-B2C-BOC-P2P");//中国银行
		BANK_MAP_B2C.put("BOCOM","BANK_CARD-B2C-BOCO-P2P");//交通银行
		BANK_MAP_B2C.put("CCB","BANK_CARD-B2C-CCB-P2P");//建设银行
		BANK_MAP_B2C.put("PAB","BANK_CARD-B2C-PINGANBANK-P2P");//平安银行
		BANK_MAP_B2C.put("CEB","BANK_CARD-B2C-CEB-P2P");//光大银行
		BANK_MAP_B2B.put("ICBC","BANK_CARD-B2B-ICBC-P2P");//工商银行
		BANK_MAP_B2B.put("CCB","BANK_CARD-B2B-CCB-P2P");//建设银行
		BANK_MAP_B2B.put("CMB","BANK_CARD-B2B-CMBCHINA-P2P");//招商银行
		BANK_MAP_B2B.put("BOC","BANK_CARD-B2B-BOC-P2P");//中国银行
		BANK_MAP_B2B.put("ABC","BANK_CARD-B2B-ABC-P2P");//中国农业银行
		BANK_MAP_B2B.put("CEB","BANK_CARD-B2B-CEB-P2P");//光大银行
		BANK_MAP_B2B.put("BOCOM","BANK_CARD-B2B-BOCO-P2P");//交通银行
		BANK_MAP_B2B.put("SPDB","BANK_CARD-B2B-SPDB-P2P");//浦发银行
		BANK_MAP_B2B.put("","BANK_CARD-B2B-SDB-P2P");//深圳发展银行
		BANK_MAP_B2B.put("CMBC","BANK_CARD-B2B-CMBC-P2P");//民生银行
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @return 
	 * @throws Exception
	 */
	public String pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YHJ"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try{
			String merchantId = PayConstant.PAY_CONFIG.get("YHJ_MERCHANT_ID");
			String requestId = payOrder.payordno;
			String orderAmount = String.valueOf(payOrder.txamt);
			String orderCurrency = "CNY";
			String notifyUrl = PayConstant.PAY_CONFIG.get("YHJ_NOTIFY_URL");
			String callbackUrl = prdOrder.returl==null||prdOrder.returl.length()==0?"#":prdOrder.returl;
			String remark = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")!=null
					?PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"):"线上支付";
			String paymentModeCode = BANK_MAP_B2C.get(payOrder.bankcod);
			
			JSONObject reqData = new JSONObject();
			reqData.put("merchantId", merchantId);
			reqData.put("requestId", requestId);
			reqData.put("orderAmount", orderAmount);
			reqData.put("orderCurrency", orderCurrency);
			reqData.put("paymentModeCode", paymentModeCode);
			reqData.put("remark", remark);
			reqData.put("notifyUrl", notifyUrl);
			reqData.put("callbackUrl", callbackUrl);
			reqData.put("clientIp", PayConstant.PAY_CONFIG.get("YHJ_clientIp"));
			
			String jsonPDStr = "[{'name':'"+remark+"','quantity':'1','amount':'"+orderAmount+"','description':'"+remark+"'}]";  
			
			JSONArray productDetailArray = JSONObject.parseArray(jsonPDStr);
			reqData.put("productDetails", productDetailArray);
			
			JSONObject payer = new JSONObject();
			payer.put("name", "");//付款方名称
			payer.put("phoneNum", "");//手机号码
			payer.put("idType", "");//证件类型
			payer.put("idNum", "");//证件号码
			payer.put("bankCardNum", "");//银行卡号
			reqData.put("payer", payer);
			//签名
			String hMac = YHJUtils.sign(reqData, PayConstant.PAY_CONFIG.get(PayConstant.PAY_CONFIG.get("YHJ_MERCHANT_ID")));
			reqData.put("hmac", hMac);
			log.info("易汇金网关请求数据:"+reqData.toJSONString());
			new PayOrderService().updateOrderForBanks(payOrder);
			String resData = YHJUtils.getResData(reqData, PayConstant.PAY_CONFIG.get("YHJ_ONLINEPAY_URL"));
			log.info("易汇金网关响应数据："+resData);
			JSONObject responseData = (JSONObject) JSONObject.parse(resData);
			//验签
			if(YHJUtils.verifyHmac(ORDER_RESPONSE_HMAC_FIELDS, responseData)){
				//验签成功
				new PayOrderService().updateOrderForBanks(payOrder);
				return responseData.getString("redirectUrl");
			}else throw new Exception("易汇金验签失败");
		} catch (Exception e){
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
	public String pay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YHJ"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		try{
			String merchantId = PayConstant.PAY_CONFIG.get("YHJ_MERCHANT_ID");
			String requestId = payOrder.payordno;
			String orderAmount = String.valueOf(payOrder.txamt);
			String orderCurrency = "CNY";
			String notifyUrl = PayConstant.PAY_CONFIG.get("YHJ_NOTIFY_URL");
			String callbackUrl = prdOrder.returl==null||prdOrder.returl.length()==0?"#":prdOrder.returl;
			String remark = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")!=null
					?PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"):"线上支付";
			String paymentModeCode = BANK_MAP_B2C.get(payOrder.bankcod);
			
			JSONObject reqData = new JSONObject();
			reqData.put("merchantId", merchantId);
			reqData.put("requestId", requestId);
			reqData.put("orderAmount", orderAmount);
			reqData.put("orderCurrency", orderCurrency);
			reqData.put("paymentModeCode", paymentModeCode);
			reqData.put("remark", remark);
			reqData.put("notifyUrl", notifyUrl);
			reqData.put("callbackUrl", callbackUrl);
			reqData.put("clientIp", PayConstant.PAY_CONFIG.get("YHJ_clientIp"));
			
			String jsonPDStr = "[{'name':'"+remark+"','quantity':'1','amount':'"+orderAmount+"','description':'"+remark+"'}]";  
			
			JSONArray productDetailArray = JSONObject.parseArray(jsonPDStr);
			reqData.put("productDetails", productDetailArray);
			
			JSONObject payer = new JSONObject();
			payer.put("name", "");//付款方名称
			payer.put("phoneNum", "");//手机号码
			payer.put("idType", "");//证件类型
			payer.put("idNum", "");//证件号码
			payer.put("bankCardNum", "");//银行卡号
			reqData.put("payer", payer);
			//签名
			String hMac = YHJUtils.sign(reqData, PayConstant.PAY_CONFIG.get(PayConstant.PAY_CONFIG.get("YHJ_MERCHANT_ID")));
			reqData.put("hmac", hMac);
			log.info("易汇金网关请求数据:"+reqData.toJSONString());
			new PayOrderService().updateOrderForBanks(payOrder);
			String resData = YHJUtils.getResData(reqData, PayConstant.PAY_CONFIG.get("YHJ_ONLINEPAY_URL"));
			log.info("易汇金网关响应数据："+resData);
			JSONObject responseData = (JSONObject) JSONObject.parse(resData);
			//验签
			if(YHJUtils.verifyHmac(ORDER_RESPONSE_HMAC_FIELDS, responseData)){
				//验签成功
				new PayOrderService().updateOrderForBanks(payOrder);
				return responseData.getString("redirectUrl");
			}else throw new Exception("易汇金验签失败");
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
		
	}
	/**
	 * 查单
	 * @param payOrder
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	public void channelQuery(PayOrder payOrder) throws UnsupportedEncodingException, Exception{
		JSONObject queryData = new JSONObject();
		queryData.put("merchantId", PayConstant.PAY_CONFIG.get("YHJ_MERCHANT_ID"));
		queryData.put("requestId", payOrder.payordno);
		//签名
		String hmac = YHJUtils.sign(queryData, PayConstant.PAY_CONFIG.get(PayConstant.PAY_CONFIG.get("YHJ_MERCHANT_ID")));
		queryData.put("hmac", hmac);
		log.info("易汇金查单请求的数据："+queryData.toString());
		String resData = YHJUtils.getResData(queryData, PayConstant.PAY_CONFIG.get("YHJ_ONLINEPAY_QUERY_URL"));
		log.info("易汇金查单响应的数据："+resData);
		JSONObject responseData = (JSONObject) JSONObject.parse(resData);
		payOrder.actdat = new Date();
		//验签
		if(YHJUtils.verifyHmac(QUERY_RESPONSE_HMAC_FIELDS, responseData)){
			//验签成功
			if("SUCCESS".equals(responseData.getString("status"))){
				payOrder.ordstatus="01";//支付成功
	        	payOrder.bankjrnno = responseData.getString("serialNumber");
	        	new NotifyInterface().notifyMer(payOrder);//支付成功
			}else{
				payOrder.ordstatus="02";//支付失败
	        	payOrder.bankjrnno = responseData.getString("serialNumber");
	        	payOrder.bankerror = responseData.getString("status"); 
	        	new NotifyInterface().notifyMer(payOrder);//支付失败。
			}
		} else throw new Exception("易汇金验签失败");
	}
}
