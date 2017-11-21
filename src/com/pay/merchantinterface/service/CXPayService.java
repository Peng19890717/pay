package com.pay.merchantinterface.service;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.DataTransUtil;
import util.PayUtil;
import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jweb.dao.Blog;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.cx.AESUtil;
import com.third.cx.GatewayUtil;

public class CXPayService {
	private static final Log log = LogFactory.getLog(CXPayService.class);
	public static Map<String, String> bank_code = new HashMap<String, String>();
	static {
		bank_code.put("ICBC", "ICBC");//中国工商银行
		bank_code.put("HXB", "HXB");//华夏
		bank_code.put("GNXS", "GRCB");//广州农村商业银行
		bank_code.put("PAB", "SPAB");//平安银行
		bank_code.put("BOC", "BOC");//中国银行
		bank_code.put("ABC", "ABC");//农业。
		bank_code.put("CCB", "CCB");//建设。
		bank_code.put("BOCOM", "COMM");//交通
		bank_code.put("CMB", "CMB");//招商
		bank_code.put("CEB", "CEB");//光大。
		bank_code.put("CMBC", "CMBC");//民生。
		bank_code.put("PSBC", "PSBC");//邮政。
		bank_code.put("SPDB", "SPDB");
		bank_code.put("CNCB", "CITIC");
		bank_code.put("CIB", "CIB");
		bank_code.put("BOHC", "CBHB");
		bank_code.put("BCCB", "BJB");
		bank_code.put("GDB", "GDB");
		bank_code.put("BOS", "SHB");
		bank_code.put("NBBC", "NBB");
		bank_code.put("NJBC", "NJCB");
		bank_code.put("SCB", "ZDYH");
		bank_code.put("WZCB", "WZCB");
		bank_code.put("BRCB", "BJRCB");
		bank_code.put("CCQTGB", "CCQTGB");
		bank_code.put("HCCB", "HZCB");
		bank_code.put("HNNXS", "HZLHNCSSYH");
		bank_code.put("SXJS", "JSHB");
		bank_code.put("GZCB", "GCB");
		bank_code.put("EGBANK", "EGB");
		bank_code.put("CSCB", "CSCB");
		bank_code.put("SHRCB", "SHRCB");
		bank_code.put("GNXS", "GRCB");
		bank_code.put("CZCB", "CZCB");
		bank_code.put("HKBCHINA", "HKBC");
		bank_code.put("SNXS", "SZCRU");
		bank_code.put("CYB", "CHIYUBANK");
	}
	 /**
     * 单笔代付
     * @param payRequest
     * @throws Exception 
     */
    public void receivePaySingle(PayRequest payRequest) throws Exception{
    	try {
    		PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
    		if("1".equals(payRequest.receivePayType)){//代付业务。
    			Map<String, String> sParaTemp = new HashMap<String, String>();
    			sParaTemp.put("serviceName", "agentSinglePay");
    			sParaTemp.put("version", "V1");
    			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("CX_QUICK_MERCHANT_ID"));
    			sParaTemp.put("payType", "16");
    			sParaTemp.put("charset", "UTF-8");
    			//业务参数
    			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);
    			sParaTemp.put("merBatchNo", payRequest.receiveAndPaySingle.id);
    			sParaTemp.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    			sParaTemp.put("payeeAcct", payRequest.accNo);
    			sParaTemp.put("payeeName", payRequest.accName);
    			sParaTemp.put("applyAmount", String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d));
    			sParaTemp.put("bankName", cardBin.bankName);
    			sParaTemp.put("bankCode", bank_code.get(cardBin.bankCode));
    			sParaTemp.put("sign",GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CX_QUICK_PRIVITE_KEY"), "RSA"));
    			sParaTemp.put("signType", "RSA");
    			log.info("创新代付请求参数:"+sParaTemp);
    			String resultString = GatewayUtil.httpPostByDefaultTime(new URI(PayConstant.PAY_CONFIG.get("CX_DF_PAY_URL_NEW")), sParaTemp, "UTF-8");
    			log.info("创新代付响应的参数:"+resultString);
    			JSONObject jsonObject = JSON.parseObject(resultString);
    			if("0000".equals(jsonObject.getString("retCode"))){
    				if("01".equals(jsonObject.get("tradeStatus"))||"05".equals(jsonObject.get("tradeStatus"))){
    					rp.status="0";
    					rp.errorMsg = "提交成功";
    		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
    		        	DFquery query = new DFquery(payRequest.receiveAndPaySingle.id,payRequest);
    		        	query.start();
    				}
    			}else{
    				rp.status="2";
					rp.errorMsg = jsonObject.getString("retMsg");
		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 代收付查询(单笔)
     * @param request
     * @return
     * @throws Exception
     */
    public boolean receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp) throws Exception{
		try {
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", "queryAgentSinglePay");
			sParaTemp.put("version", "V1");
			sParaTemp.put("merchantId",PayConstant.PAY_CONFIG.get("CX_QUICK_MERCHANT_ID"));
			sParaTemp.put("charset", "UTF-8");
			sParaTemp.put("merBatchNo", rp.id);
			sParaTemp.put("sign",GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CX_QUICK_PRIVITE_KEY"), "RSA"));
			sParaTemp.put("signType", "RSA");
			log.info("创新代付查询请求参数:"+sParaTemp);
			String resultString = GatewayUtil.httpPostByDefaultTime(new URI(PayConstant.PAY_CONFIG.get("CX_DF_QUERY_URL_NEW")), sParaTemp, "UTF-8");
			log.info("创新代付查询接口响应:"+resultString);
			JSONObject jsonObject = JSON.parseObject(resultString);
			if("0000".equals(jsonObject.getString("retCode"))){
				if("06".equals(jsonObject.getString("tradeStatus"))){
					request.setRespCode("000");
					request.receiveAndPaySingle.setRespCode("000");
					request.receivePayRes = "0";
					request.respDesc="交易成功";
					rp.errorMsg = "交易成功";
					return true;
				}else if("07".equals(jsonObject.getString("tradeStatus"))){
					request.setRespCode("-1");
					request.receiveAndPaySingle.setRespCode("000");
					request.receivePayRes = "-1";
					request.respDesc="交易失败";
					rp.errorMsg = request.respDesc;
					return false;
				}else{
					request.setRespCode("0");//处理中
					request.receiveAndPaySingle.setRespCode("000");
					request.receivePayRes = "-1";
					request.respDesc="处理中";
					rp.errorMsg = request.respDesc;
					return false;
				}
			}else throw new Exception(jsonObject.get("retMsg").toString());
		} catch (Exception e) {
				log.info(PayUtil.exceptionToString(e));
				request.setRespCode("-1");
				rp.setRespCode(request.respCode);
				request.receivePayRes = "-1";
				request.respDesc=e.getMessage();
				rp.errorMsg = e.getMessage();
				return false;
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
	public String  quickPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder)throws Exception{
		try {
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", "quickPayApi");
			sParaTemp.put("version", "V1");
			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("CX_QUICK_MERCHANT_ID"));
			sParaTemp.put("notifyUrl",PayConstant.PAY_CONFIG.get("CX_QUICK_NOTIFY_URL"));
			sParaTemp.put("currency", "CNY");
			sParaTemp.put("payType","03");
			sParaTemp.put("charset", "UTF-8");
			//业务参数
			sParaTemp.put("productName", "payOnline");
			sParaTemp.put("productDesc", "payOnline");
			sParaTemp.put("tranTime",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("payerId", "payOnline");//付款人标识。
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);//通过卡号获取卡类信息。
			if("1".equals(cardBin.cardType)){
				sParaTemp.put("bankCardType", "02");//01 借记卡 02 贷记卡
				sParaTemp.put("cvv", payRequest.cvv2);
				sParaTemp.put("expireDate",payRequest.validPeriod);
			}else{
				sParaTemp.put("bankCardType", "01");
			}
			sParaTemp.put("bankCode", bank_code.get(cardBin.bankCode));
			sParaTemp.put("idType", "01");
			sParaTemp.put("clientIp", PayConstant.PAY_CONFIG.get("CX_QUICK_IP"));
			sParaTemp.put("merOrderId",payOrder.payordno);//订单号，AES加密。
			sParaTemp.put("tranAmt",String.format("%.2f", ((double)Long.parseLong(payRequest.merchantOrderAmt))/100d));//单位元。AES加密。
			sParaTemp.put("bankCardNo",payRequest.cardNo);//卡号。AES加密
			sParaTemp.put("idNo", payRequest.credentialNo);//身份证号 AES加密。
			sParaTemp.put("bankAcctName",payRequest.userName);//账户名称， AES加密。
			sParaTemp.put("mobileNo",payRequest.userMobileNo);//手机号， AES加密。
			sParaTemp.put("sign",GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CX_QUICK_PRIVITE_KEY"), "RSA"));
			sParaTemp.put("merOrderId",AESUtil.encrypt(sParaTemp.get("merOrderId"),PayConstant.PAY_CONFIG.get("CX_QUICK_AES_KEY")));//订单号，AES加密。
			sParaTemp.put("tranAmt",AESUtil.encrypt(sParaTemp.get("tranAmt"),PayConstant.PAY_CONFIG.get("CX_QUICK_AES_KEY")));//单位元。AES加密。
			sParaTemp.put("bankCardNo",AESUtil.encrypt(sParaTemp.get("bankCardNo"), PayConstant.PAY_CONFIG.get("CX_QUICK_AES_KEY")));//卡号。AES加密
			sParaTemp.put("idNo", AESUtil.encrypt(sParaTemp.get("idNo"), PayConstant.PAY_CONFIG.get("CX_QUICK_AES_KEY")));
			sParaTemp.put("bankAcctName",AESUtil.encrypt(sParaTemp.get("bankAcctName"), PayConstant.PAY_CONFIG.get("CX_QUICK_AES_KEY")));
			sParaTemp.put("mobileNo",AESUtil.encrypt(sParaTemp.get("mobileNo"), PayConstant.PAY_CONFIG.get("CX_QUICK_AES_KEY")));
			sParaTemp.put("signType", "RSA");
			
			log.info("创新快捷下单请求报文:"+sParaTemp.toString());
			String resultString = GatewayUtil.httpPostByDefaultTime(
					new URI(PayConstant.PAY_CONFIG.get("CX_QUICK_PAY_URL")), sParaTemp, "UTF-8");
			log.info("创新快捷下单响应报文:"+resultString);
			JSONObject jsonObject = JSON.parseObject(resultString);
			if("0000".equals(jsonObject.getString("retCode"))){
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" " +
						"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
						"respCode=\"000\" respDesc=\"操作成功\" " +
						"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			}else{
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" " +
						"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
						"respCode=\"-1\" respDesc=\""+jsonObject.getString("retMsg")+"\" " +
						"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>"; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 快捷确认
	 * @param payRequest
	 * @return
	 */
    public String certPayConfirm(PayRequest payRequest) {
		try {
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", "confirmPay");
			sParaTemp.put("version", "V1");
			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("CX_QUICK_MERCHANT_ID"));	
			sParaTemp.put("payType","03");
			sParaTemp.put("charset", "UTF-8");
			//业务参数
			sParaTemp.put("merOrderId",payRequest.payOrder.payordno);//订单号，AES加密。
			sParaTemp.put("verifyCode",payRequest.checkCode);//单位元。AES加密。
			
			sParaTemp.put("sign",GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CX_QUICK_PRIVITE_KEY"), "RSA"));
			sParaTemp.put("merOrderId",AESUtil.encrypt(sParaTemp.get("merOrderId"), PayConstant.PAY_CONFIG.get("CX_QUICK_AES_KEY")));//订单号，AES加密。
			sParaTemp.put("signType", "RSA");
			log.info("创新快捷确认请求参数:"+sParaTemp);
			String resultString = GatewayUtil.httpPostByDefaultTime(
					new URI(PayConstant.PAY_CONFIG.get("CX_QUICK_CONFIRMPAY_URL")), sParaTemp, "UTF-8");
			log.info("创新快捷确认响应报文:"+resultString);
			JSONObject jsonObject = JSON.parseObject(resultString);
			if("0000".equals(jsonObject.getString("retCode"))){
				if("02".equals(jsonObject.getString("tradeStatus"))||"01".equals(jsonObject.getString("tradeStatus"))){
					return "{\"resCode\":\"000\",\"resMsg\":\"支付成功\"}";
				}else if("03".equals(jsonObject.getString("tradeStatus"))||"08".equals(jsonObject.getString("tradeStatus"))){
	    			return "{\"resCode\":\"-1\",\"resMsg\":\"支付失败\"}";
				}
			}else{
				return "{\"resCode\":\"-1\",\"resMsg\":\""+jsonObject.getString("retMsg")+"\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    /**
	 * 快捷支付渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void quickPaychannelQuery(PayOrder payOrder) throws Exception{
		try{
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", "queryResult");
			sParaTemp.put("version", "V1");
			sParaTemp.put("merchantId", PayConstant.PAY_CONFIG.get("CX_QUICK_MERCHANT_ID"));	
			sParaTemp.put("payType","03");
			sParaTemp.put("charset", "UTF-8");
			//业务参数
			sParaTemp.put("merOrderId",payOrder.payordno);//订单号，AES加密。
			sParaTemp.put("clientIp",PayConstant.PAY_CONFIG.get("CX_QUICK_IP"));
			sParaTemp.put("sign",GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CX_QUICK_PRIVITE_KEY"), "RSA"));
			sParaTemp.put("signType", "RSA");
			log.info("创新快捷查询请求参数:"+sParaTemp);
			String resultString = GatewayUtil.httpPostByDefaultTime(
					new URI(PayConstant.PAY_CONFIG.get("CX_QUICK_QUERY_URL")), sParaTemp, "UTF-8");
			log.info("创新快捷查询响应参数:"+resultString);
			JSONObject jsonObject = JSON.parseObject(resultString);
			if("0000".equals(jsonObject.getString("retCode"))){
				if("02".equals(jsonObject.getString("tradeStatus"))){
					payOrder.ordstatus="01";//支付成功
	            	new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("03".equals(jsonObject.getString("tradeStatus"))){
					payOrder.ordstatus="02";//支付成功
	            	new NotifyInterface().notifyMer(payOrder);//支付成功
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayProductOrder prdOrder,PayOrder payOrder){
		try{
			Blog log = new Blog();
			request.setCharacterEncoding("utf-8");
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX"); // 支付渠道
			payOrder.bankcod = request.getParameter("banks"); // 银行代码
			payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			//基本参数。
			String serviceName= "ebankPayApi";//固定值ebankPayApi
			String version="V1";
			String merchantId =PayConstant.PAY_CONFIG.get("CX_WG_MERID");;
			String signType="RSA";//不参与签名。
			String payType ="01";
			String charset="UTF-8";
			//业务参数。
			String merOrderId =payOrder.payordno;
			String currency="CNY";
			String notifyUrl =PayConstant.PAY_CONFIG.get("CX_WG_NOTIFY");
			String productName ="payOnline";
			String productDesc="payOnline";
			String tranAmt=String.format("%.2f", ((double)(payOrder.txamt))/100d);
			String tranTime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String bankCardType="01";
			if("0".equals(payOrder.bankCardType)){
				bankCardType="01";
			}
			if("1".equals(payOrder.bankCardType)){
				bankCardType="02";
			}
			String bankCode = bank_code.get(payOrder.bankcod);
			String clientIp= PayConstant.PAY_CONFIG.get("CX_WG_CLIENTIP");
			                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", serviceName);
			sParaTemp.put("version", version);
			sParaTemp.put("merchantId", merchantId);	
			sParaTemp.put("payType",payType);
			sParaTemp.put("charset", charset);
			sParaTemp.put("merOrderId", merOrderId);
			sParaTemp.put("currency", currency);
			sParaTemp.put("notifyUrl", notifyUrl);
			sParaTemp.put("productName", productName);
			sParaTemp.put("productDesc", productDesc);
			sParaTemp.put("tranAmt", tranAmt);
			sParaTemp.put("tranTime", tranTime);
			sParaTemp.put("bankCardType", bankCardType);
			sParaTemp.put("bankCode", bankCode);
			sParaTemp.put("clientIp", clientIp);
			String sign=GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CX_WG_PRIVITE_KEY"), "RSA");
			sParaTemp.put("signType", signType);
			sParaTemp.put("sign", sign);
			log.info("创新网线收银台下单请求参数:"+sParaTemp);
			Iterator<String> iterator = sParaTemp.keySet().iterator();
			while(iterator.hasNext()){
				String temp = iterator.next();
				request.setAttribute(temp, sParaTemp.get(temp));
			}
			new PayOrderService().updateOrderForBanks(payOrder);
		}catch(Exception e){
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
	public void pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder){
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX"); // 支付渠道
			payOrder.bankcod = payRequest.bankId; // 银行代码
			payOrder.bankCardType = payRequest.accountType;//String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			
			//基本参数。
			String serviceName= "ebankPayApi";//固定值ebankPayApi
			String version="V1";
			String merchantId =PayConstant.PAY_CONFIG.get("CX_WG_MERID");
			String signType="RSA";//不参与签名。
			String payType ="01";
			String charset="UTF-8";
			//业务参数。
			String merOrderId =payOrder.payordno;
			String currency="CNY";
			String notifyUrl =PayConstant.PAY_CONFIG.get("CX_WG_NOTIFY");
			String productName ="payOnline";
			String productDesc="payOnline";
			String tranAmt=String.format("%.2f", ((double)(payOrder.txamt))/100d);
			String tranTime=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String bankCardType="01";
			if("0".equals(payOrder.bankCardType)){
				bankCardType="01";
			}
			if("1".equals(payOrder.bankCardType)){
				bankCardType="02";
			}
			String bankCode = bank_code.get(payOrder.bankcod);
			String clientIp= PayConstant.PAY_CONFIG.get("CX_WG_CLIENTIP");
			                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", serviceName);
			sParaTemp.put("version", version);
			sParaTemp.put("merchantId", merchantId);	
			sParaTemp.put("payType",payType);
			sParaTemp.put("charset", charset);
			sParaTemp.put("merOrderId", merOrderId);
			sParaTemp.put("currency", currency);
			sParaTemp.put("notifyUrl", notifyUrl);
			sParaTemp.put("productName", productName);
			sParaTemp.put("productDesc", productDesc);
			sParaTemp.put("tranAmt", tranAmt);
			sParaTemp.put("tranTime", tranTime);
			sParaTemp.put("bankCardType", bankCardType);
			sParaTemp.put("bankCode", bankCode);
			sParaTemp.put("clientIp", clientIp);
			String sign=GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CX_WG_PRIVITE_KEY"), "RSA");
			sParaTemp.put("signType", signType);
			sParaTemp.put("sign", sign);
			log.info("创新网银商户接口下单请求参数:"+sParaTemp);
			Iterator<String> iterator = sParaTemp.keySet().iterator();
			while(iterator.hasNext()){
				String temp = iterator.next();
				request.setAttribute(temp, sParaTemp.get(temp));
			}
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			//基本参数。
			String serviceName= "queryResult";//固定值ebankPayApi
			String version="V1";
			String merchantId =PayConstant.PAY_CONFIG.get("CX_WG_MERID");;
			String signType="RSA";//不参与签名。
			String payType ="01";
			String charset="UTF-8";
			//业务参数.
			String merOrderId = payOrder.payordno;
			String clientIp = PayConstant.PAY_CONFIG.get("CX_WG_CLIENTIP");
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", serviceName);
			sParaTemp.put("version", version);
			sParaTemp.put("merchantId", merchantId);	
			sParaTemp.put("payType",payType);
			sParaTemp.put("charset", charset);
			sParaTemp.put("merOrderId", merOrderId);
			sParaTemp.put("clientIp", clientIp);
			String sign=GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CX_WG_PRIVITE_KEY"), "RSA");
			sParaTemp.put("signType", signType);
			sParaTemp.put("sign", URLEncoder.encode(sign,"utf-8"));
			String params = "";
			Iterator<String> it = sParaTemp.keySet().iterator();
				while(it.hasNext()){
					Object key = it.next();
					params = params+key+"="+sParaTemp.get(key)+ "&";
				}
				params = params.substring(0,params.length()-1); 
			log.info("创新网关查询请求报文:"+params.toString());
			String payResult = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("CX_WG_QUERY_URL"),params.getBytes("utf-8")),"utf-8");
			log.info("创新网关查询响应报文:"+payResult);
			JSONObject jsonObject = JSON.parseObject(payResult);
			if("0000".equals(jsonObject.getString("retCode"))){
				if("02".equals(jsonObject.getString("tradeStatus"))){
					payOrder.ordstatus="01";//支付成功
				    new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("03".equals(jsonObject.getString("tradeStatus"))){
					payOrder.ordstatus="02";
				    new NotifyInterface().notifyMer(payOrder);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
class DFquery extends Thread{
	private static final Log log = LogFactory.getLog(DFquery.class);
	public PayRequest payRequest= new PayRequest();
	public String merBatchNo="";
	public DFquery(String merBatchNo,PayRequest payRequest) {
		this.merBatchNo = merBatchNo;
		this.payRequest=payRequest;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(1000*60*1);
			for(int i=0;i<3;i++){
				if(query()){
					break;
				}
				Thread.sleep(1000*60);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query(){
		boolean flag =false;
		try {
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", "queryAgentSinglePay");
			sParaTemp.put("version", "V1");
			sParaTemp.put("merchantId",PayConstant.PAY_CONFIG.get("CX_QUICK_MERCHANT_ID"));
			sParaTemp.put("charset", "UTF-8");
			sParaTemp.put("merBatchNo", merBatchNo);
			sParaTemp.put("sign",GatewayUtil.signRequest(sParaTemp,PayConstant.PAY_CONFIG.get("CX_QUICK_PRIVITE_KEY"), "RSA"));
			sParaTemp.put("signType", "RSA");
			log.info("创新代付查询请求参数:"+sParaTemp);
			String resultString = GatewayUtil.httpPostByDefaultTime(new URI(PayConstant.PAY_CONFIG.get("CX_DF_QUERY_URL_NEW")), sParaTemp, "UTF-8");
			log.info("创新代付查询接口响应:"+resultString);
			JSONObject jsonObject = JSON.parseObject(resultString);
			if("0000".equals(jsonObject.getString("retCode"))){
				if("06".equals(jsonObject.getString("tradeStatus"))){
					PayReceiveAndPay rp = new PayReceiveAndPay();
					rp.id=merBatchNo;
					rp.status="1";
					rp.respCode="000";
					rp.errorMsg="交易成功";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					flag=true;
					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
					list.add(rp);
					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					return flag;
				}else if("07".equals(jsonObject.getString("tradeStatus"))){
					PayReceiveAndPay rp = new PayReceiveAndPay();
					rp.id=merBatchNo;
					rp.status="2";
					rp.respCode="-1";
					rp.errorMsg="交易失败";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					flag=false;
//					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
//					list.add(rp);
//					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					return flag;
				}else{
					return flag;
				}
			}else throw new Exception(jsonObject.get("retMsg").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
	}
}
