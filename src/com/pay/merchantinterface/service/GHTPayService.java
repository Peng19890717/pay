package com.pay.merchantinterface.service;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.DataTransUtil;
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
import com.third.ght.AesUtils;
import com.third.ght.GatewayEgCore;
import com.third.ght.RSA;
/**
 * 高汇通服务类
 * @author xk
 *
 */
public class GHTPayService {
	private static String charset = "utf-8";
	private static final Log log = LogFactory.getLog(GHTPayService.class);
	private static String publicKey = PayConstant.PAY_CONFIG.get("GHT_PUB_KEY");//高汇通公钥
	private static String privateKey = PayConstant.PAY_CONFIG.get("GHT_MER_PRV_KEY");//商户私钥
	public static Map<String,String> BANK_MAP_JIEJI = new HashMap <String,String>();//借记卡银行码映射
	public static Map <String,String> BANK_MAP_DAIJI = new HashMap <String,String>();//贷记卡银行码映射
	public static Map <String,String> PAY_BANK_MAP_JIEJI = new HashMap <String,String>();//借记卡银行码映射（网关）
	public static Map <String,String> PAY_BANK_MAP_DAIJI = new HashMap <String,String>();//贷记卡银行码映射（网关）
	static{
		//网银支持银行
		PAY_BANK_MAP_JIEJI.put("BOC", "BOC");
		PAY_BANK_MAP_JIEJI.put("CEB", "CEB");
		PAY_BANK_MAP_JIEJI.put("ICBC", "ICBC");
		PAY_BANK_MAP_JIEJI.put("ABC", "ABC");
		PAY_BANK_MAP_JIEJI.put("CCB", "CCB");
		PAY_BANK_MAP_JIEJI.put("CMBC", "CMBC");
		PAY_BANK_MAP_JIEJI.put("BCCB", "BJBANK");
		PAY_BANK_MAP_JIEJI.put("PSBC", "PSBC");
		
		PAY_BANK_MAP_DAIJI.put("ICBC", "ICBC");
		PAY_BANK_MAP_DAIJI.put("ABC", "ABC");
		//快捷/代付支持银行
		BANK_MAP_JIEJI.put("BOC", "BOC");//中国银行
		BANK_MAP_JIEJI.put("CEB", "CEB");//中国光大银行
		BANK_MAP_JIEJI.put("ICBC", "ICBC");//中国工商银行
		BANK_MAP_JIEJI.put("ABC", "ABC");//中国农业银行
		BANK_MAP_JIEJI.put("CCB", "CCB");//中国建设银行
		BANK_MAP_JIEJI.put("BOCOM", "COMM");//交通银行
		BANK_MAP_JIEJI.put("CMBC", "CMBC");//中国民生银行
		BANK_MAP_JIEJI.put("CNCB", "CITIC");//中信银行
		BANK_MAP_JIEJI.put("PAB", "SPABANK");//平安银行
		BANK_MAP_JIEJI.put("BCCB", "BJBANK");//北京银行
		BANK_MAP_JIEJI.put("CIB", "CIB");//兴业银行
		BANK_MAP_JIEJI.put("NBBC", "NBBANK");//宁波银行
		BANK_MAP_JIEJI.put("GDB", "GDB");//广发银行
		BANK_MAP_JIEJI.put("HXB", "HXBANK");//华夏银行
		BANK_MAP_JIEJI.put("PSBC", "PSBC");//中国邮政储蓄银行
		
		BANK_MAP_DAIJI.put("BOC", "BOC");//中国银行
		BANK_MAP_DAIJI.put("CEB", "CEB");//中国光大银行
		BANK_MAP_DAIJI.put("ICBC", "ICBC");//中国工商银行
		BANK_MAP_DAIJI.put("ABC", "ABC");//中国农业银行
		BANK_MAP_DAIJI.put("CCB", "CCB");//中国建设银行
		BANK_MAP_DAIJI.put("BOCOM", "COMM");//交通银行
		BANK_MAP_DAIJI.put("CMBC", "CMBC");//中国民生银行
		BANK_MAP_DAIJI.put("CNCB", "CITIC");//中信银行
		BANK_MAP_DAIJI.put("PAB", "SPABANK");//平安银行
		BANK_MAP_DAIJI.put("NBBC", "NBBANK");//宁波银行
		BANK_MAP_DAIJI.put("GDB", "GDB");//广发银行
		BANK_MAP_DAIJI.put("HXB", "HXBANK");//华夏银行
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try {
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType))bankCode = PAY_BANK_MAP_JIEJI.get(payOrder.bankcod);
			else if(PayConstant.CARD_BIN_TYPE_DAIJI.equals(payOrder.bankCardType))bankCode = PAY_BANK_MAP_DAIJI.get(payOrder.bankcod);
			else if("4".equals(payOrder.bankCardType))bankCode = PAY_BANK_MAP_DAIJI.get(payOrder.bankcod);//对公账号
			if(bankCode == null)throw new Exception("高汇通无对应银行（"+payOrder.bankcod+"）");
			
			Map<String, String> sParaTemp = new LinkedHashMap<String, String>();
			//0借记卡/1贷记卡/2准贷记卡/3预付卡
			if("0".equals(payOrder.bankCardType)){
				sParaTemp.put("bankCardType", "01");//银行卡类标识  01 借记卡 02 贷记卡
				sParaTemp.put("bankCode", PAY_BANK_MAP_JIEJI.get(payOrder.bankcod));
			}else if("1".equals(payOrder.bankCardType)){
				sParaTemp.put("bankCardType", "02");//银行卡类标识  01 借记卡 02 贷记卡
				sParaTemp.put("bankCode", PAY_BANK_MAP_JIEJI.get(payOrder.bankcod));
			}
			sParaTemp.put("charset", charset);
			sParaTemp.put("clientIp", PayConstant.PAY_CONFIG.get("GHT_CLIENT_IP"));
			sParaTemp.put("currency", "CNY");
			sParaTemp.put("merOrderId", payOrder.payordno);//商户订单号
			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("GHT_MERCHANTID"));//合作商户的商户号
			sParaTemp.put("notifyUrl", PayConstant.PAY_CONFIG.get("GHT_NOTIFY_PAY_URL"));
			sParaTemp.put("payType", "01");//支付类型
			sParaTemp.put("productDesc", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			sParaTemp.put("productName", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			sParaTemp.put("returnUrl", "127.0.0.1");//页面跳转地址prdOrder.returl
			sParaTemp.put("serviceName", "ebankPayApi");//服务名称
			sParaTemp.put("tranAmt", String.format("%.2f", ((double)payOrder.txamt)/100d));
			sParaTemp.put("tranTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("version", PayConstant.PAY_CONFIG.get("GHT_VERSION"));//版本号
			log.info("高汇通网银待签名数据："+sParaTemp);
			//签名
			String signData = GatewayEgCore.buildMysignRSA(sParaTemp, privateKey, charset);
			sParaTemp.put("sign", signData);
			sParaTemp.put("signType", "RSA");//签名类型
			log.info("高汇通网银下单请求数据："+sParaTemp.toString());
			request.setAttribute("ghtReqData", sParaTemp);
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
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GHT"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		try{
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType))bankCode = PAY_BANK_MAP_JIEJI.get(payOrder.bankcod);
			else if(PayConstant.CARD_BIN_TYPE_DAIJI.equals(payOrder.bankCardType))bankCode = PAY_BANK_MAP_DAIJI.get(payOrder.bankcod);
			else if("4".equals(payOrder.bankCardType))bankCode = PAY_BANK_MAP_DAIJI.get(payOrder.bankcod);//对公账号
			if(bankCode == null)throw new Exception("高汇通无对应银行（"+payOrder.bankcod+"）");
			
			Map<String, String> sParaTemp = new LinkedHashMap<String, String>();
			//0借记卡/1贷记卡/2准贷记卡/3预付卡
			if("0".equals(payOrder.bankCardType)){
				sParaTemp.put("bankCardType", "01");//银行卡类标识  01 借记卡 02 贷记卡
				sParaTemp.put("bankCode", PAY_BANK_MAP_JIEJI.get(payOrder.bankcod));
			}else if("1".equals(payOrder.bankCardType)){
				sParaTemp.put("bankCardType", "02");//银行卡类标识  01 借记卡 02 贷记卡
				sParaTemp.put("bankCode", PAY_BANK_MAP_DAIJI.get(payOrder.bankcod));
			}
			sParaTemp.put("charset", charset);
			sParaTemp.put("clientIp", PayConstant.PAY_CONFIG.get("GHT_CLIENT_IP"));
			sParaTemp.put("currency", "CNY");
			sParaTemp.put("merOrderId", payOrder.payordno);//商户订单号
			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("GHT_MERCHANTID"));//合作商户的商户号
			sParaTemp.put("notifyUrl", PayConstant.PAY_CONFIG.get("GHT_NOTIFY_PAY_URL"));
			sParaTemp.put("payType", "01");//支付类型
			sParaTemp.put("productDesc", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			sParaTemp.put("productName", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			sParaTemp.put("returnUrl", "127.0.0.1");//页面跳转地址prdOrder.returl
			sParaTemp.put("serviceName", "ebankPayApi");//服务名称
			sParaTemp.put("tranAmt", String.format("%.2f", ((double)payOrder.txamt)/100d));
			sParaTemp.put("tranTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("version", PayConstant.PAY_CONFIG.get("GHT_VERSION"));//版本号
			log.info("高汇通网银待签名数据："+sParaTemp);
			//签名
			String signData = GatewayEgCore.buildMysignRSA(sParaTemp, privateKey, charset);
			sParaTemp.put("sign", signData);
			sParaTemp.put("signType", "RSA");//签名类型
			log.info("高汇通网银下单请求数据："+sParaTemp.toString());
			request.setAttribute("ghtReqData", sParaTemp);
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
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);//通过卡号获取卡类信息。
			Map<String, String> sParaTemp = new LinkedHashMap<String, String>();
			sParaTemp.put("bankAcctName", payRequest.userName);//银行账户名称  AES加密传输
			sParaTemp.put("bankCardNo", payRequest.cardNo);//银行卡号   AES加密传输
			if(cardBin.cardType.equals("0")){//借记卡
				sParaTemp.put("bankCardType", "01");//银行卡类型  01 借记卡 02 贷记卡
			}else if(cardBin.cardType.equals("1")){//贷记卡
				sParaTemp.put("bankCardType", "02");//银行卡类型  01 借记卡 02 贷记卡
			}
			sParaTemp.put("bankCode", BANK_MAP_JIEJI.get(cardBin.bankCode));//银行编码 
			sParaTemp.put("charset", charset);
			sParaTemp.put("clientIp", PayConstant.PAY_CONFIG.get("GHT_CLIENT_IP"));
			sParaTemp.put("currency", "CNY");
			if("02".equals(sParaTemp.get("bankCardType"))){//贷记卡
				sParaTemp.put("cvv", payRequest.cvv2);//cvv码  bankCardType 为02 必传  AES加密传输
				sParaTemp.put("expireDate", payRequest.validPeriod);//有效期  格式:MMYY 例如0916;银行卡类型为贷记卡必填  AES加密传输
			}
			sParaTemp.put("idNo", payRequest.credentialNo);//证件号码  AES加密传输
			sParaTemp.put("idType", "01");//证件类型  01 身份证 固定值 01 
			sParaTemp.put("merOrderId", String.valueOf(payOrder.payordno));//AES加密传输
			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("GHT_MERCHANTID"));
			sParaTemp.put("mobileNo", payRequest.userMobileNo);//AES加密传输
			sParaTemp.put("notifyUrl", PayConstant.PAY_CONFIG.get("GHT_NOTIFY_QUICK_PAY_URL"));
			sParaTemp.put("payType", "03");//支付类型  固定值 03 快捷API模式
			sParaTemp.put("payerId", sParaTemp.get("merOrderId"));//付款人标识
			sParaTemp.put("productDesc", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			sParaTemp.put("productName", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			sParaTemp.put("serviceName", "quickPayApi");
			try{
	        	DecimalFormat df  = new DecimalFormat("###.00");
	        	String tranAmt="";
	        	if(Double.parseDouble(payRequest.merchantOrderAmt)/100<1){
	        		tranAmt="0"+df.format(Double.parseDouble(payRequest.merchantOrderAmt)/100) ;
	        	} else tranAmt=df.format(Double.parseDouble(payRequest.merchantOrderAmt)/100);
	        	sParaTemp.put("tranAmt", tranAmt);// 交易的总金额，单位为元 AES加密传输
	        }catch(Exception e){throw new Exception("快捷支付金额错误");};
			sParaTemp.put("tranTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("version", PayConstant.PAY_CONFIG.get("GHT_VERSION"));
			//待签名数据
			String signData = GatewayEgCore.createLinkString(sParaTemp);
			//生成签名结果
			String sign = RSA.sign(signData,PayConstant.PAY_CONFIG.get("GHT_MER_PRV_KEY"),charset);
			String tmp = "";
			Iterator<String> iterator = sParaTemp.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = sParaTemp.get(key);
				if("bankAcctName".equals(key) || "bankCardNo".equals(key) || "idNo".equals(key) || "merOrderId".equals(key) || "mobileNo".equals(key) || "tranAmt".equals(key)){
					value = AesUtils.encrypt(value, PayConstant.PAY_CONFIG.get("GHT_AES_KEY"));
				}
				tmp += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			tmp += "sign="+URLEncoder.encode(sign,charset)+"&signType=RSA";
			log.info("高汇通快捷下单请求数据："+tmp);
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("GHT_QUICKPAY_URL"), tmp.getBytes(charset)),charset);
			log.info("高汇通快捷下单响应数据："+resData);
			/**
			 * {"sign":"jaVnOmfZcpQjMyGRFrHw3KO64QAIYHHZ1nqTKr7nHKIKlVz0p7OaXvC5EmGqUvwap7t0jMgXmur48w+RrmUrpQOaMGTR+Ken+f5CqTiKnBtWnFnmV8MRC2aEUz5WXC7mFApOW3YLfgz44VLAQJhdsM2ppBMWI4VdQTIH5nLDtUHplSzGFbmq7YWN97cmQhbR0aUnUh1OddzPD65GAV+J3RWYLG5pQVxcOKyQ2Y/OuZfu8YAsL4fmkSDn581v4n+di30kPeLvlPjOV3ye9VNI3+4tdlQm+QB+6AWbo7PHHR8yPyI5ZefniqIk+cXSZCjgIO/NzwY3akOzJukc0sRXsA==",
			 * "retCode":"0000","retMsg":"交易成功","tranNo":"20170712095746076614","merchantId":"000110048160233","merOrderId":"1499824650619"}
			 */
			JSONObject resJson = (JSONObject) JSONObject.parse(resData);
			if("0000".equals(resJson.getString("retCode"))){
				Map<String,Object> resMap = JSON.parseObject(resData);
				if(RSA.verifySign(analysis(resMap), (String)resMap.get("sign"), publicKey, charset)){
					payOrder.bankerror = resJson.getString("retMsg");
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" " +
					"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
					"respCode=\"000\" respDesc=\"下单成功\" " +
					"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
				}else throw new Exception("高汇通快捷下单验签失败");
			} else return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" " +
				"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
				"respCode=\"-1\" respDesc=\""+resJson.getString("retMsg")+"\" " +
				"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
		} catch (Exception e) {
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
			"<message merchantId=\""+payRequest.merchantId+"\" " +
			"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
			"respCode=\"-1\"  respDesc=\""+e.getMessage()+"\" " +
			"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
		}
	
	}
	/**
	 * 快捷确认
	 * @param payRequest
	 * @return
	 * @throws Exception 
	 */
	public String certPayConfirm(PayRequest payRequest) throws Exception {
		Map<String, String> sParaTemp = new LinkedHashMap<String, String>();
		sParaTemp.put("charset", charset);
		sParaTemp.put("merOrderId", payRequest.payOrder.payordno);
		sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("GHT_MERCHANTID"));
		sParaTemp.put("payType", "03");
		sParaTemp.put("serviceName", "confirmPay");
		sParaTemp.put("verifyCode", payRequest.checkCode);
		sParaTemp.put("version", PayConstant.PAY_CONFIG.get("GHT_VERSION"));
		//待签名数据
		String signData = GatewayEgCore.createLinkString(sParaTemp);
		//生成签名结果
		String sign = RSA.sign(signData,PayConstant.PAY_CONFIG.get("GHT_MER_PRV_KEY"),charset);
		String tmp = "";
		Iterator<String> iterator = sParaTemp.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			String value = sParaTemp.get(key);
			if("merOrderId".equals(key)){
				value = AesUtils.encrypt(value, PayConstant.PAY_CONFIG.get("GHT_AES_KEY"));
			}
			tmp += key+"="+URLEncoder.encode(value,charset)+"&";
		}
		tmp += "sign="+URLEncoder.encode(sign,charset)+"&signType=RSA";
		log.info("高汇通快捷支付确认请求数据："+tmp);
		String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("GHT_QUICKPAY_CONFIRM_URL"), tmp.getBytes(charset)),charset);
		log.info("高汇通快捷支付确认响应数据："+resData);
		/**
		 * {"sign":"qoFSKjR7K58C1tKHvSGZfyooTTD4C3pVOPANdaVvka2ZWj3hNEYfUtRx7ZUp/MeaTXhUVZlFLfBF17CCoIUOB0wWc4h/1y4Khj6Qcxxp3rwalbf531eBDsK81lh9dfx7MJFftuFXANBdSOVrcLyKmsP5R/CNSbcSDIZ2d7gtv5hP4ia7Cy3WxP7PpCxY7QHzJnkMQGhx8M+iEODfwEULJkqPrIhWFA/21S4IAP0SDGByqCtvKdpPvjQR1AO84KmH8prTC5CE01TedGIyZR1/wjDvwZiZoTsN7G9Gb/g8uI6YnavZf6RUNLJWrdNBtvHSMgs9BhUJ3j82TN2LfUBu1A==",
		 * "tradeStatus":"01","retCode":"0000","tranAmt":"0.01","retMsg":"交易成功","tranNo":"201707121142025227421","merchantId":"000110048160233","chnSettleDate":null,"merOrderId":"1499830907251"}
		 */
		return resData;
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
			Map<String, String> sParaTemp = new LinkedHashMap<String, String>();
			sParaTemp.put("applyAmount", String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d));//代付申请金额 单位元
			sParaTemp.put("bankCode", BANK_MAP_JIEJI.get(cardBin.bankCode));
			sParaTemp.put("bankName", cardBin.bankName);
			if(Integer.parseInt(payRequest.amount) > 5000000){
				sParaTemp.put("bankProvince", rp.province);//开户行省，5W以上必填
			}
			sParaTemp.put("charset", charset);
			sParaTemp.put("merBatchNo", rp.channelTranNo);
			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("GHT_MERCHANTID"));
			sParaTemp.put("payType", "16");//固定值 16 单笔代付
			sParaTemp.put("payeeAcct", rp.accountNo);//收款人账号
			sParaTemp.put("payeeName", rp.accountName);//收款人名称
			sParaTemp.put("serviceName", "agentSinglePay");
			sParaTemp.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("version", PayConstant.PAY_CONFIG.get("GHT_VERSION"));
			log.info("待签名数据："+sParaTemp);
			//待签名数据
			String signData = GatewayEgCore.createLinkString(sParaTemp);
			//生成签名结果
			String sign = RSA.sign(signData,privateKey,charset);
			String tmp = "";
			Iterator<String> iterator = sParaTemp.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = sParaTemp.get(key);
				tmp += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			tmp += "sign="+URLEncoder.encode(sign,charset)+"&signType=RSA";
			log.info("高汇通单笔代付请求数据："+tmp);
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("GHT_RECEIVEPAYSINGLE_URL"), tmp.getBytes(charset)),charset);
			log.info("高汇通单笔代付响应数据："+resData);
			JSONObject resJson = (JSONObject) JSONObject.parse(resData);
			//高汇通单笔代付响应数据：{"sign":"vW5PxvNKLZPm4Ztasdq1VKji4Nq2fEfjOY7FHkIkQUeGIUddFU/3RRd5V/eB6zOyz+/Q+W0QqzxJLjoEvjjqOphv7hVjH43dN3vOhEs1sbx2hlb4MEeLDQ928oHRZR1iYp/yaSw5QN0PatWUf+O+s7in76HFnOo66INncW0cAcOiKsxIXtNaYKdY6KcdMa4Ual+hodG+G2uGqs/JC3frrJw6bdy8xqJtLYab4fTTPjW9wwC9jYHSt1dLX79Gi119amlOlKr8Wk/HORdvcc73KQz9cbEuMgoHFLbUNN130nzbH/deSOENd/3NXA0ffl/fMxpOoa7J2IDN8dSdqo+V5g==",
			//"platform":null,"tradeStatus":"01","retCode":"0000","merBatchNo":"1000000_1500878283034","retMsg":"交易成功","merchantId":"000320048160405","batchSerialNo":"20170724143809737673"}
			if("0000".equals(resJson.getString("retCode"))){
				Map<String,Object> resMap = JSON.parseObject(resData);
				if(RSA.verifySign(analysis(resMap), (String)resMap.get("sign"), publicKey, charset)){
//					if("01".equals(resJson.getString("tradeStatus"))){
						rp.status="0";
						rp.errorMsg = "操作成功";
			        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			        	GHTDFQueryThread query = new GHTDFQueryThread(payRequest);
			        	query.start();
//					}
				}else throw new Exception("高汇通单笔代付验签失败");
			}else{
				rp.status="2";
				rp.errorMsg = "操作失败";
				try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			throw e;
		}
	}
	/**
	 * 单笔代付查询
	 * @param request
	 * @param rp
	 * @throws Exception
	 */
	public void receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp) throws Exception{
		try {
			Map<String, String> sParaTemp = new LinkedHashMap<String, String>();
			sParaTemp.put("charset", charset);
			sParaTemp.put("merBatchNo", rp.channelTranNo);//1499849298662
			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("GHT_MERCHANTID"));
			sParaTemp.put("serviceName", "queryAgentSinglePay");
			sParaTemp.put("version", PayConstant.PAY_CONFIG.get("GHT_VERSION"));
			log.info("待签名数据："+sParaTemp);
			//待签名数据
			String signData = GatewayEgCore.createLinkString(sParaTemp);
			//生成签名结果
			String sign = RSA.sign(signData,privateKey,charset);
			String tmp = "";
			Iterator<String> iterator = sParaTemp.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = sParaTemp.get(key);
				tmp += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			tmp += "sign="+URLEncoder.encode(sign,charset)+"&signType=RSA";
			log.info("高汇通单笔代付查询请求数据："+tmp);
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("GHT_RECEIVEPAYSINGLEQUERY_URL"), tmp.getBytes(charset)),charset);
			log.info("高汇通单笔代付查询响应数据："+resData);
			/**
			 * {"platform":null,"payeeAcct":"621************8455","tradeStatus":"07","merBatchNo":"1499849298662","bankName":"中国建设银行","batchSerialNo":"20170712164833076788","sign":"cejuvQefUOvFP/VeWrCLwIjJO4R99D1DJ6ME+YEn+6IAHg07W1tWO5sg742f2SHFGQK8yT1g6BGxgtUTfk7e7OXKTcIcXdXzAArmyHZFoH61UsOzq7tCSwISmxg+Q/wABcUQqc/bgT176Z+uy+zhrP24DDKrBCVktqBTzDyfi3SBtUnWSvcTxwxykJey5jh61ApKmt5HyfD/5eizbZUxvS2cVLc/lCdio1fyxCUwyqahEsSj8cLy3KPCZZfL/3IMZR/9Ws/EdSmgqkX5ssEfEzOTGXs1cOR51nzlI5uN2bUzXRWTLfi1FGw4eujvFkkQEFyE/GiOBCaHbafcGTI9gA==",
			 * "payeeName":"*志国","retCode":"0000","applyReason":null,"retMsg":"交易成功","finishTime":"2017-07-12 16:48:33","merchantId":"000110048160233","failReason":"代付资金冻结失败|账户余额不足","applyAmount":"1"}
			 */
			JSONObject resJson = (JSONObject) JSONObject.parse(resData);
			if("0000".equals(resJson.getString("retCode"))){
				Map<String,Object> resMap = JSON.parseObject(resData);
				if(RSA.verifySign(analysis(resMap), (String)resMap.get("sign"), publicKey, charset)){
					if("06".equals(resJson.getString("tradeStatus"))){
						request.setRespCode("000");
						request.receiveAndPaySingle.setRespCode("000");
						request.receivePayRes = "0";
						request.respDesc="交易成功";
						rp.errorMsg = "交易成功";
					}else if("07".equals(resJson.getString("tradeStatus"))){
						request.setRespCode("-1");
						request.receiveAndPaySingle.setRespCode("000");
						request.receivePayRes = "-1";
						request.respDesc="交易失败";
						rp.errorMsg = request.respDesc;
					}
				}else throw new Exception("高汇通单笔代付查询验签失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 渠道补单（网银快捷查单）
	 * @param payOrder
	 * @throws Exception
	 */
	public void channelQuery(PayOrder payOrder, String payType) throws Exception{
		Map<String, String> sParaTemp = new LinkedHashMap<String, String>();
		sParaTemp.put("charset", charset);
		sParaTemp.put("clientIp", PayConstant.PAY_CONFIG.get("GHT_CLIENT_IP"));
		sParaTemp.put("merOrderId", payOrder.payordno);
		sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("GHT_MERCHANTID"));
		sParaTemp.put("payType", payType);//支付类型 01：网银API版本，02：网银界面版本，03：快捷API版本，04：快捷界面版本
		sParaTemp.put("serviceName", "queryResult");
		sParaTemp.put("version", PayConstant.PAY_CONFIG.get("GHT_VERSION"));
		//生成签名结果
		String signData = GatewayEgCore.buildMysignRSA(sParaTemp, PayConstant.PAY_CONFIG.get("GHT_MER_PRV_KEY"), charset);
		sParaTemp.put("sign", signData);
		sParaTemp.put("signType", "RSA");//签名类型
		
		String tmp = "";
		Iterator<String> iterator = sParaTemp.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			String value = sParaTemp.get(key);
			tmp += key+"="+URLEncoder.encode(value,charset)+"&";
		}
		tmp = tmp.substring(0, tmp.length()-1);
		log.info("高汇通查单请求数据："+tmp);
		String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("GHT_QUERYRESULT_URL"), tmp.getBytes(charset)),charset);
		log.info("高汇通查单响应数据："+resData);
		/**
		 * {"sign":"xjr119j5gAT2tp4CUElGbW2RrEbRym4vmtyEoW9+1Nu/oXQrc2rtakGBUrWZnybFJMMMFjZFWR+3/1azDlg6EuN5o8JuLCWNKz5Olf/hJdWcU3/Vh8jxYVm37cXS+FVgHSaMem6avaMhKTmkmNPBtSfXmuBaxqaD4bRNLhLSKMxRvXb7MNZ2VzxFldmVrWGId3AtHtymQabZ07H/uQRQ4iJCw0Ay80QOomPvAMx32o5P36cvSqAFDVAQczwy7sS8SwNlsS+4IOmb0gqEiGlS1guMfAdd3WHySviIBSQEv9IE7OOTshvsPA8YIN9Dub7RTkCmXnE2Cyk8X0KY4Or4tA==",
		 * "platform":null,"tradeStatus":"01","retCode":"0000","bindId":null,"tranAmt":"0.01",
		 * "retMsg":"交易成功","tranNo":"20170710162506076402","finishTime":null,"merchantId":"000110048160233","merOrderId":"1499675057076"}
		 */
		JSONObject resJson = (JSONObject) JSONObject.parse(resData);
		if("0000".equals(resJson.getString("retCode"))) {
			Map<String,Object> resMap = JSON.parseObject(resData);
			if(RSA.verifySign(analysis(resMap), (String)resMap.get("sign"), publicKey, charset)){
				if("02".equals(resJson.getString("tradeStatus"))){
					 payOrder.ordstatus="01";//支付成功
				     new NotifyInterface().notifyMer(payOrder);//支付成功
				} else if("03".equals(resJson.getString("tradeStatus"))){
					 payOrder.ordstatus="02";//支付失败
				     new NotifyInterface().notifyMer(payOrder);
				}
			}else throw new Exception("高汇通验签失败");
		}
	}
	/**
	 * 解析响应的参数以进行验签
	 * @param resMap
	 * @return
	 */
	public static String analysis(Map<String,Object> resMap){
		Map<String, String> treeMap = new  TreeMap<String, String>();
		Iterator<String> resIterator = resMap.keySet().iterator();
		while(resIterator.hasNext()){
			String key = resIterator.next();
			String value = (String) resMap.get(key);
			if(!"".equals(value) && value != null && !"sign".equals(key)){
				treeMap.put(key, value);
			}
		}
		String channelTmp = "" ;
		Iterator<String> respIterator = treeMap.keySet().iterator();
		while(respIterator.hasNext()){
			String key = respIterator.next();
			String value = treeMap.get(key);
			channelTmp += key+"="+value+"&";
		}
		channelTmp = channelTmp.substring(0,channelTmp.length()-1);
		return channelTmp;
	}
}
class GHTDFQueryThread extends Thread{
	private static final Log log = LogFactory.getLog(HFQueryThread.class);
	private  PayRequest payRequest= new PayRequest();
	public GHTDFQueryThread(){};
	public GHTDFQueryThread(PayRequest payRequest){
		this.payRequest=payRequest;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("高汇通代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query()throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
			Map<String, String> sParaTemp = new LinkedHashMap<String, String>();
			sParaTemp.put("charset", "utf-8");
			sParaTemp.put("merBatchNo", rp.channelTranNo);
			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("GHT_MERCHANTID"));
			sParaTemp.put("serviceName", "queryAgentSinglePay");
			sParaTemp.put("version", PayConstant.PAY_CONFIG.get("GHT_VERSION"));
			log.info("待签名数据："+sParaTemp);
			//待签名数据
			String signData = GatewayEgCore.createLinkString(sParaTemp);
			//生成签名结果
			String sign = RSA.sign(signData,PayConstant.PAY_CONFIG.get("GHT_MER_PRV_KEY"),"utf-8");
			String tmp = "";
			Iterator<String> iterator = sParaTemp.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = sParaTemp.get(key);
				tmp += key+"="+URLEncoder.encode(value,"utf-8")+"&";
			}
			tmp += "sign="+URLEncoder.encode(sign,"utf-8")+"&signType=RSA";
			log.info("高汇通单笔代付查询请求数据："+tmp);
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("GHT_RECEIVEPAYSINGLEQUERY_URL"), tmp.getBytes("utf-8")),"utf-8");
			log.info("高汇通单笔代付查询响应数据："+resData);
			/**
			 * {"platform":null,"payeeAcct":"621************8455","tradeStatus":"07","merBatchNo":"1499849298662","bankName":"中国建设银行","batchSerialNo":"20170712164833076788","sign":"cejuvQefUOvFP/VeWrCLwIjJO4R99D1DJ6ME+YEn+6IAHg07W1tWO5sg742f2SHFGQK8yT1g6BGxgtUTfk7e7OXKTcIcXdXzAArmyHZFoH61UsOzq7tCSwISmxg+Q/wABcUQqc/bgT176Z+uy+zhrP24DDKrBCVktqBTzDyfi3SBtUnWSvcTxwxykJey5jh61ApKmt5HyfD/5eizbZUxvS2cVLc/lCdio1fyxCUwyqahEsSj8cLy3KPCZZfL/3IMZR/9Ws/EdSmgqkX5ssEfEzOTGXs1cOR51nzlI5uN2bUzXRWTLfi1FGw4eujvFkkQEFyE/GiOBCaHbafcGTI9gA==",
			 * "payeeName":"*志国","retCode":"0000","applyReason":null,"retMsg":"交易成功","finishTime":"2017-07-12 16:48:33","merchantId":"000110048160233","failReason":"代付资金冻结失败|账户余额不足","applyAmount":"1"}
			 */
			JSONObject resJson = (JSONObject) JSONObject.parse(resData);
			if("0000".equals(resJson.getString("retCode"))){
				Map<String,Object> resMap = JSON.parseObject(resData);
				if(RSA.verifySign(GHTPayService.analysis(resMap), (String)resMap.get("sign"), PayConstant.PAY_CONFIG.get("GHT_PUB_KEY"), "utf-8")){
					if("06".equals(resJson.getString("tradeStatus"))){
						rp.status="1";
						rp.respCode="000";
						rp.errorMsg="交易成功";
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
						list.add(rp);
						new PayInterfaceOtherService().receivePayNotify(payRequest,list);
						return true;
					} else if("07".equals(resJson.getString("tradeStatus"))){//失败
						rp.status="2";
						rp.respCode="-1";
						rp.errorMsg="交易失败";
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
//						List<PayReceiveAndPay> list3 = new ArrayList<PayReceiveAndPay>();
//						list3.add(rp);
//						new PayInterfaceOtherService().receivePayNotify(payRequest,list3);
						return true;
					}
				} else throw new Exception("高汇通验签失败");
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}