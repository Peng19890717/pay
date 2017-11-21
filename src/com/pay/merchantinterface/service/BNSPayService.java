package com.pay.merchantinterface.service;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.third.BNS.HttpClientJSONUtil;
import com.third.BNS.JSONUtil;
import com.third.BNS.RandomUtil;
import com.third.BNS.WechatOrderQueryRequest;
import com.third.BNS.WechatScannedRequest;
/**
 * 北京农商银行接口服务类
 * @author Administrator
 *
 */
public class BNSPayService {
	private static final Log log = LogFactory.getLog(BNSPayService.class);
	/**
	 * 
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType 业务标示：//支付宝、微信 扫码产品编号分别为：
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		//下单处理
		try {
			String key = PayConstant.PAY_CONFIG.get("BNS_KEY");
	        String service_type = "WECHAT_SCANNED";
	        String appid = "";
	        String mch_id =PayConstant.PAY_CONFIG.get("BNS_MER_ID");
	        String out_trade_no =payOrder.payordno;
	        String device_info ="";
	        String body = "onLinePay";
	        String detail = "";
	        String attach = "onLinePay";
	        String fee_type ="";
	        String total_fee = String.valueOf(payOrder.txamt);
	        String spbill_create_ip = PayConstant.PAY_CONFIG.get("BNS_CLIENT_IP");
	        String notify_url = PayConstant.PAY_CONFIG.get("BNS_NOTIFY_URL");
	        String time_start = "";
	        String time_expire ="";
	        String op_user_id = mch_id;
	        String goods_tag = "";
	        String product_id = RandomUtil.getRandomStringByLength(10);
	        String nonce_str = RandomUtil.randomUUID();
	        String limit_pay = "";
	        WechatScannedRequest scannedRequest = new WechatScannedRequest(key, service_type, appid, mch_id, device_info, nonce_str, body, detail, attach, out_trade_no, fee_type, total_fee, spbill_create_ip, time_start, time_expire, op_user_id, goods_tag, notify_url, product_id, limit_pay);
	        log.info("北京农商银行微信扫码请求:"+JSONUtil.toJSONString(scannedRequest.toMap()));
	        String sendPost = HttpClientJSONUtil.postJSONUTF8(PayConstant.PAY_CONFIG.get("BNS_PAY_URL"), JSONUtil.toJSONString(scannedRequest.toMap()));
	        log.info("北京农商银行微信扫码响应:"+sendPost);
	        /**
	         * {"return_code":"SUCCESS","return_msg":"OK","sign":"B4FD9F2306EE9CB32E6B6EB25D56BBAC","appid":null,
	         * "mch_id":"C147730151686310379","device_info":null,"nonce_str":"34b4e6a41f1d4941a74887c412c5ebea",
	         * "result_code":"SUCCESS","err_code":null,"err_code_des":null,"trade_type":"WECHAT_SCANNED",
	         * "prepay_id":"wx20170414111057a9d969376b0363321531","code_url":"weixin://wxpay/bizpayurl?pr=CnkbjQO"}
	         */
	        JSONObject jsonObject = JSON.parseObject(sendPost);
			if ("SUCCESS".equals(jsonObject.getString("return_code"))) {
				if("SUCCESS".equals(jsonObject.getString("result_code"))){
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
							"codeUrl=\""+jsonObject.getString("code_url")+"\" respCode=\"000\" respDesc=\"成功\"/>";
				}else {
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
							"codeUrl=\"\" respCode=\"-1\" respDesc=\""+jsonObject.getString("err_code_des")+"\"/>";
				}
			} else throw new Exception("北京农下单商渠道异常");
		} catch(Exception e){
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
				"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try {
			String key = PayConstant.PAY_CONFIG.get("BNS_KEY");
	        String service_type = "WECHAT_ORDERQUERY";
	        String appid = "";
	        String mch_id =PayConstant.PAY_CONFIG.get("BNS_MER_ID");
	        String transaction_id = "";
	        String out_trade_no =payOrder.payordno;
	        String nonce_str = RandomUtil.randomUUID();
	        WechatOrderQueryRequest orderQueryRequest = new WechatOrderQueryRequest(key, service_type,
	                appid, mch_id, transaction_id, out_trade_no, nonce_str);
	        log.info("北京农商银行微信扫码查询请求:"+JSONUtil.toJSONString(orderQueryRequest.toMap()));
	        String sendPost = HttpClientJSONUtil.postJSONUTF8(PayConstant.PAY_CONFIG.get("BNS_PAY_URL"), JSONUtil.toJSONString(orderQueryRequest.toMap()));
	        log.info("北京农商银行微信扫码查询响应:"+sendPost);
	        /**
	         * {"return_code":"SUCCESS","return_msg":"OK","sign":"D6700B6CE822D6CD782DAB1758428176",
	         * "appid":null,"mch_id":"C147730151686310379","nonce_str":"09214ae9439a43a692f855bacce4fa80",
	         * "result_code":"USERPAYING","err_code":null,"err_code_des":null,"device_info":null,"openid":null,
	         * "is_subscribe":null,"trade_type":"WECHAT_SCANNED","trade_state":"USERPAYING","bank_type":null,
	         * "detail":null,"total_fee":null,"fee_type":null,"transaction_id":"O20170414111057010106323",
	         * "wechat_transaction_id":null,"out_trade_no":"1492139458072","attach":null,"time_end":null}
	         */
	        JSONObject jsonObject = JSON.parseObject(sendPost);
			if ("SUCCESS".equals(jsonObject.getString("return_code"))) {
				if("SUCCESS".equals(jsonObject.getString("trade_state"))){
					payOrder.ordstatus="01";//支付成功
		        	new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("PAYERROR".equals(jsonObject.getString("trade_state"))){
					payOrder.ordstatus="02";//支付失败
		        	new NotifyInterface().notifyMer(payOrder);//支付失败
				}
			}else throw new Exception("北京农商查询渠道异常");
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
