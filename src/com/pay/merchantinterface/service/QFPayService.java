package com.pay.merchantinterface.service;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.third.qianfang.HttpUtil;
import com.third.qianfang.MerchantApiUtil;
/**
 * 钱方服务类
 * @author Administrator
 *
 */
public class QFPayService {
	private static final Log log = LogFactory.getLog(QFPayService.class);
	/**
	 * 
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType 微信扫码:800201；支付宝扫码:800101
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		try{
			Map<String , Object> paramMap = new HashMap<String , Object>();
	        paramMap.put("txamt",payOrder.txamt);
	        paramMap.put("txcurrcd","CNY");
	        paramMap.put("pay_type",productType);
	        paramMap.put("out_trade_no",payOrder.payordno);
	        paramMap.put("txdtm",new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
	        paramMap.put("goods_name","payOnLine");
	        paramMap.put("mchid",PayConstant.PAY_CONFIG.get("QF_MCHID"));
	        paramMap.put("limit_pay","no_credit");
	        //签名及生成请求API的方法
	        String sign = MerchantApiUtil.getSign(paramMap, PayConstant.PAY_CONFIG.get("QF_KEY"));
	        log.info("钱方下单请求:"+paramMap);
	        String payResult =HttpUtil.post(PayConstant.PAY_CONFIG.get("QF_PAY_URL"), paramMap, sign,PayConstant.PAY_CONFIG.get("QF_APPCODE"), "UTF-8");
	        /**
	         * {"pay_type": "800201", "sysdtm": "2017-08-29 15:57:41", "cardcd": "",
	         *  "txdtm": "2017-08-29 15:57:44", "resperr": "\u4e0b\u5355\u6210\u529f",
	         *   "txcurrcd": "CNY", "txamt": "1", "respmsg": "", "out_trade_no": "1503993464688",
	         *    "syssn": "20170829005200020018087901",
	         *     "qrcode": "weixin://wxpay/bizpayurl?pr=yhPBgNr", "respcd": "0000"}
	         */
	        log.info("钱方下单响应:"+URLDecoder.decode( payResult));
	        JSONObject jsonObject = JSON.parseObject(payResult);
	        if("0000".equals(jsonObject.get("respcd").toString())){
	        	 return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
	      					"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
	      					"codeUrl=\""+jsonObject.get("qrcode").toString()+"\" respCode=\"000\" respDesc=\"处理成功\"/>";
	        }else throw new Exception("下单失败");
		} catch(Exception e){
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
				"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
		}
	}
//	/**
//	 * 渠道补单
//	 * @param payordno 订单号
//	 * @throws Exception 异常捕获
//	 */
//	public void channelQuery(PayOrder payOrder) throws Exception{
//		try {
//			Map<String , Object> paramMap = new HashMap<String , Object>();
//		  	paramMap.put("payKey", PayConstant.PAY_CONFIG.get("QF_PAYKEY"));//商户支付Key
//	        paramMap.put("outTradeNo",payOrder.payordno);//原交易订单号
//	        //签名及生成请求API的方法
//	        String sign = MerchantApiUtil.getSign(paramMap, PayConstant.PAY_CONFIG.get("QF_PAYSECRET"));
//	        String paramStr_temp = MerchantApiUtil.getParamStr(paramMap);
//	        String paramStr = paramStr_temp+"&sign="+sign;
//	        log.info("钱方支付宝WAP查询请求:"+paramStr);
//	        String payResult = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("QF_QUERYURL"),paramStr.getBytes("utf-8")),"utf-8");
//	        log.info("钱方支付宝WAP查询响应:"+payResult);
//	        payOrder.actdat = new Date();
//	        JSONObject jsonObject = JSON.parseObject(payResult);
//	        Object res_sign = jsonObject.get("sign");//响应的签名数据
//	        Object trxNo = jsonObject.get("trxNo");//响应的交易流水
//	        Object completeDate = jsonObject.get("completeDate");//响应的支付完成时间。
//	        Object errMsg = jsonObject.get("errMsg");//响应的错误信息。
//	        Object outTradeNo = jsonObject.get("outTradeNo");//响应的商户订单号。
//	        Object orderPrice = jsonObject.get("orderPrice");//响应的订单金额。
//	        Object orderStatus = jsonObject.get("orderStatus");//响应的订单状态。
//	        Object resultCode = jsonObject.get("resultCode");//响应码。
//	        //验签
//	        Map<String , Object> resParamMap = new HashMap<String , Object>();
//	        resParamMap.put("resultCode", resultCode);
//	        resParamMap.put("orderPrice", orderPrice);
//	        resParamMap.put("trxNo", trxNo);
//	        resParamMap.put("completeDate", completeDate);
//	        if("0000".equals(resultCode)){
//			     resParamMap.put("outTradeNo", outTradeNo);
//			     resParamMap.put("orderStatus", orderStatus);
//	        }else{
//	        	 resParamMap.put("errMsg", errMsg);
//	        }
//	        if(MerchantApiUtil.isRightSign(resParamMap,PayConstant.PAY_CONFIG.get("QF_PAYSECRET"),res_sign.toString())){
//	        	  if ("0000".equals(resultCode.toString())&&"SUCCESS".equals(orderStatus.toString())){//支付成功
//	        		  payOrder.ordstatus="01";//支付成功
//	  	        	  new NotifyInterface().notifyMer(payOrder);//支付成功
//			        }else{//支付失败
//			        	throw new Exception(errMsg.toString());
//			        }
//	        }else{
//	        	throw new Exception("验签失败");
//	        }
//		} catch (Exception e){
//			e.printStackTrace();
//			throw e;
//		}
//	}
//	
//	  public static void main(String[] args) throws Exception{
//		  //pay();
//		  query();
//		 // testNotify();
//	  }
//	  public static void testNotify(){
//		  String str = "<xml>" +
//		            "<attach><![CDATA[att]]></attach>"+
//					"<bank_type><![CDATA[CFT]]></bank_type>"+
//					"<charset><![CDATA[UTF-8]]></charset>"+
//					"<fee_type><![CDATA[CNY]]></fee_type>"+
//					"<is_subscribe><![CDATA[N]]></is_subscribe>"+
//					"<mch_id><![CDATA[7551000001]]></mch_id>"+
//					"<nonce_str><![CDATA[1480992003141]]></nonce_str>"+
//					"<openid><![CDATA[oywgtuHTnxPuME2XLmJAaeMZxDl0]]></openid>"+
//					"<out_trade_no><![CDATA[q4z7Hap2Q3rKTOJ]]></out_trade_no>"+
//					"<out_transaction_id><![CDATA[4008822001201612061937647985]]></out_transaction_id>"+
//					"<pay_result><![CDATA[0]]></pay_result>"+
//					"<result_code><![CDATA[0]]></result_code>"+
//					"<sign><![CDATA[78DF3560F77CC053D3D1E4382442830D]]></sign>"+
//					"<sign_type><![CDATA[MD5]]></sign_type>"+
//					"<status><![CDATA[0]]></status>"+
//					"<sub_appid><![CDATA[wxce38685bc050ef82]]></sub_appid>"+
//					"<sub_is_subscribe><![CDATA[N]]></sub_is_subscribe>"+
//					"<sub_openid><![CDATA[oHmbkt-SBdrO1eyK8enbxpZymN4A]]></sub_openid>"+
//					"<time_end><![CDATA[20161206104002]]></time_end>"+
//					"<total_fee><![CDATA[1]]></total_fee>"+
//					"<trade_type><![CDATA[pay.weixin.native]]></trade_type>"+
//					"<transaction_id><![CDATA[7551000001201612063174297140]]></transaction_id>"+
//					"<version><![CDATA[2.0]]></version>"+
//			"</xml>";
//		  try {
//			String payResult = new String(new DataTransUtil().doPost("http://localhost:8080/pay/ZXNotify.htm",str.getBytes("utf-8")),"utf-8");
//			log.info(payResult);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	  }
//	  public static void pay(){
//		  try {
//			  	Map<String , Object> paramMap = new HashMap<String , Object>();
//		        paramMap.put("txamt","1");
//		        paramMap.put("txcurrcd","CNY");
//		        paramMap.put("pay_type","800201");
//		        paramMap.put("out_trade_no",String.valueOf(System.currentTimeMillis()));
//		        paramMap.put("txdtm",new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
//		        paramMap.put("goods_name","中国");
//		        paramMap.put("mchid","Z9OLBhzKKO");
//		        paramMap.put("limit_pay","no_credit");
//		        String appCode = "2AD7B6E359F34519BE2D4B7DCDD5EA4D";
//		        //签名及生成请求API的方法
//		        String sign = MerchantApiUtil.getSign(paramMap, "57D7A768680B4BEBA68B78BEA8EEE7B1");
//		        log.info("钱方下单请求:"+paramMap);
//		        String payResult =HttpUtil.post("https://openapi.qfpay.com/trade/v1/payment", paramMap, sign,appCode, "UTF-8");
//		        log.info("钱方下单响应:"+URLDecoder.decode( payResult));
////		        JSONObject jsonObject = JSON.parseObject(payResult);
////		        Object resultCode = jsonObject.get("resultCode");//返回码
////		        Object payMessage = jsonObject.get("payMessage");//请求结果(请求成功时)
////		        Object errMsg = jsonObject.get("errMsg");//错误信息(请求失败时)
////		        Object resSign = jsonObject.get("sign");//签名数据。
//		        //验签
//		        Map<String , Object> resParamMap = new HashMap<String , Object>();
////		        resParamMap.put("resultCode", resultC ode);
////		        if("0000".equals(resultCode)){
////				     resParamMap.put("payMessage", payMessage);
////		        }else{
////		        	 resParamMap.put("errMsg", errMsg);
////		        }
////		        if(MerchantApiUtil.isRightSign(resParamMap, "7f07d80d5f5f402a9b654e05f3443980",resSign.toString())){
////		        	  if ("0000".equals(resultCode.toString())){//请求成功
////		      			 log.info(payMessage.toString());
////				        }else{//请求失败
////				        	log.info(errMsg);
////				        }
////		        }else{
////		        	throw new Exception("验签失败");
////		        }
//			  
//		  } catch (Exception e) {
//			e.printStackTrace();
//		}
//	  }
//	  public static void query(){
//		  try {
//			  	Map<String , Object> paramMap = new HashMap<String , Object>();
//			  	
//		        paramMap.put("outTradeNo","1478229287402");//原交易订单号
//		        String appCode = "2AD7B6E359F34519BE2D4B7DCDD5EA4D";
//		        //签名及生成请求API的方法
//		        String sign = MerchantApiUtil.getSign(paramMap, "57D7A768680B4BEBA68B78BEA8EEE7B1");
//		        String pay_url = "http://pay-gateway.roncoo.net/query/singleOrder";
//		        String payResult =HttpUtil.post("https://openapi.qfpay.com/trade/v1/query", paramMap, sign,appCode, "UTF-8");
//		        log.info("钱方下单响应:"+URLDecoder.decode( payResult));
//		        
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	  }
}
