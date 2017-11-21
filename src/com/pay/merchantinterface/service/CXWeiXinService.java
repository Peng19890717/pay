package com.pay.merchantinterface.service;
import java.net.URI;
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
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.third.cx.AESUtil;
import com.third.cx.GatewayService;
import com.third.cx.GatewayUtil;
/**
 * 创新接口服务类
 * @author Administrator
 *
 */
public class CXWeiXinService {
	private static final Log log = LogFactory.getLog(CXWeiXinService.class);
	public static Map<String, String> status_map = new HashMap<String, String>();
	static {
		status_map.put("00","未付款");
		status_map.put("01","处理中");
		status_map.put("02","成功");
		status_map.put("03","失败");
		status_map.put("90","交易关闭");
	}
	/**
	 * 商户接口下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		try{
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "xyw.order.precreate");
			sParaTemp.put("signMethod", "RSA");
			sParaTemp.put("sign", "");
			sParaTemp.put("timestamp",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			sParaTemp.put("charset", "UTF-8");
			sParaTemp.put("v", "1.0");
			sParaTemp.put("format", "json");
			sParaTemp.put("canary",String.valueOf(System.currentTimeMillis()));
			//业务参数
			sParaTemp.put("appId",PayConstant.PAY_CONFIG.get("CXPAY_WEI_XIN_APP_ID"));
			if(payRequest.userName.length()>0)sParaTemp.put("appUserName",payRequest.userName);
//			sParaTemp.put("payType", "12");
//			sParaTemp.put("reqChannel", "WECHAT");
			sParaTemp.put("payType", "WECHAT".equals(productType)?"12":"30");
			sParaTemp.put("reqChannel", productType);
			sParaTemp.put("tradeAmt",String.format("%.2f", ((double)Long.parseLong(payRequest.merchantOrderAmt))/100d));
			sParaTemp.put("mchTradeNo",payOrder.payordno);
//			if(payRequest.merchantOrderDesc.length()>0)sParaTemp.put("subject",payRequest.merchantOrderDesc);
			sParaTemp.put("subject",PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
//			sParaTemp.put("body","erter");
//			sParaTemp.put("userNote","ert");
			sParaTemp.put("notifyUrl", PayConstant.PAY_CONFIG.get("CXPAY_WEI_XIN_NOTIFY_URL"));
			//{"retCode":"00000","orderNo":"20161028214467783640493590977985","appParams":"null","codeUrl":"weixin://wxpay/bizpayurl?pr=580dTTV"}
			log.info("创新支付微信扫码请求===============\n"+sParaTemp);
			String res = GatewayService.directPay(sParaTemp);
			log.info("创新支付微信扫码响应===============\n" + res);
			JSONObject jsonObject = JSON.parseObject(res);
			if(!"00000".equals(jsonObject.getString("retCode"))||
					jsonObject.getString("codeUrl")==null||jsonObject.getString("codeUrl").length()==0){
				String retMsg = jsonObject.getString("retMsg");
				throw new Exception(retMsg==null||retMsg.length()==0?"微信渠道未知错误":retMsg);
			}
			payOrder.bankjrnno=jsonObject.getString("orderNo");//ORDERID
			payOrder.filed5 = "";//SIGNPAY
			new PayInterfaceDAO().updatePayOrder(payOrder);
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
					"codeUrl=\""+jsonObject.getString("codeUrl")+"\" respCode=\"000\" respDesc=\"处理成功\"/>";
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
			//请求参数sParaTemp=========={timestamp=1477655574654, v=1.0, signMethod=RSA, appId=88888, orderNo=20161028413169394814610745704081, 
			//service=xyw.order.status.query, charset=UTF-8, canary=hikhoihjohjkhuijujkh, format=json, yyyy-MM-dd HH:mm:ss=2016-10-28 19:52:54}
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "xyw.order.status.query");
			sParaTemp.put("signMethod","RSA");
			sParaTemp.put("charset","utf-8");
			sParaTemp.put("v","1.0");
			sParaTemp.put("format","json");
			sParaTemp.put("canary",String.valueOf(System.currentTimeMillis()));
			sParaTemp.put("orderNo",payOrder.bankjrnno);
			sParaTemp.put("appId", PayConstant.PAY_CONFIG.get("CXPAY_WEI_XIN_APP_ID"));
			sParaTemp.put("timestamp",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			log.info("创新支付微信扫码查询请求===============\n"+sParaTemp);
			String res = GatewayService.queryResult(sParaTemp);
			log.info("创新支付微信扫码查询响应===============\n" + res);
			payOrder.actdat = new Date();
			//{"retCode":"00000","retMsg":"操作成功","tradeStatus":"02"}(交易状态。00：未付款；01：处理中；02：成功；03：失败；90：交易关闭；)
			JSONObject jsonObject = JSON.parseObject(res);
			if("00000".equals(jsonObject.getString("retCode"))&&"02".equals(jsonObject.getString("tradeStatus"))){
				payOrder.ordstatus="01";//支付成功
	        	payOrder.bankjrnno = jsonObject.getString("trade_no");
	        	new NotifyInterface().notifyMer(payOrder);//支付成功
			} else throw new Exception("支付渠道状态："+
					(status_map.get(jsonObject.getString("tradeStatus"))==null?jsonObject.getString("tradeStatus"):
						status_map.get(jsonObject.getString("tradeStatus"))));
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void main(String[] args) {
		//new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
		//String.format("%.2f", ((double)Long.parseLong(payRequest.merchantOrderAmt))/100d)
		//pay();
		//qer();
		query();

	}
	public static void pay(){
		try{
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("serviceName", "quickPayApi");
		sParaTemp.put("version", "V1");
		sParaTemp.put("merchantId", "000330048160189");
		sParaTemp.put("notifyUrl","http://www.baidu.com");
		sParaTemp.put("currency", "CNY");
		sParaTemp.put("payType","03");
		sParaTemp.put("charset", "UTF-8");
		//业务参数
		sParaTemp.put("productName", "payOnline");
		sParaTemp.put("productDesc", "payOnline");
		sParaTemp.put("tranTime","20170605171304");
		sParaTemp.put("payerId", "payOnline");//付款人标识。
		sParaTemp.put("bankCardType", "01");//01 借记卡 02 贷记卡
		sParaTemp.put("bankCode", "CCB");
		sParaTemp.put("idType", "01");
		sParaTemp.put("clientIp", "127.0.0.1");
		sParaTemp.put("merOrderId",String.valueOf(System.currentTimeMillis()));//订单号，AES加密。
		sParaTemp.put("tranAmt","0.10");//单位元。AES加密。
		sParaTemp.put("bankCardNo","6217000010074078455");//卡号。AES加密
		sParaTemp.put("idNo", "230224198006132014");//身份证号 AES加密。
		sParaTemp.put("bankAcctName","张志国");//账户名称， AES加密。
		sParaTemp.put("mobileNo","13120033859");//手机号， AES加密。

		String privateKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK4/1GMpitdy2foRpyQxPw5T9s9XYONp5M0AQfk4JYBKV41q" +
				"+kZdl32g0jXFUjesbmqaDT9mUNkcLHXrFXv3erGvP3A9DWucWwViecgitN5ZzDiYgxcA6iuMjBlg9e1nLHy9lYWzjKZMAnTEsWLWccYweZ4XS" +
				"cq4uHwBrNqag4gZAgMBAAECgYAqToUarnNvbAtQlAio2OweZnR+UxDLJmLPA0kBYYh5ZZNsWpT5kK+/aNShCY+0xMDqUQz2X7kxfY2t8" +
				"637s1Kuet/8KBup3AhOt6h2zfD4dYQg1DAWjiwlky8OIdJ3xeSs5KX0F8pcs0NRC7XASyVvQl9TNuBrI25nskCE2zJFoQJBANwwQyJdV" +
				"Ng4EFmlRZ3hYzoWLSmDHjO61LA/f9n0pmOAW0gUG5rs9j+3XkB4dZPd3jqZdQ5WtYN4k/mUQ7NXeosCQQDKltmSQqfTKRdFdvbEikWb" +
				"EXQyXRy9LoG7JZR+XzVgJEyOCv3Cq/itb3FmYamsinjCi0B9HURAzvqvBteVU/BrAkBu1qSUweKZzO7+EWIP7YsjjFJDmUs0wxGZfv" +
				"1+29Z+M/i/OdADltEGlemODpUxT/g0C1ePVjqc6a+jbOz/dEOtAkEAiA6ThGT7rRcVp/Nje0+Zu7EXJpPeLzfCrNtqRQzQHgeC2oqLL" +
				"mislF4Z/LZua5B71bwLzXsQUa4wMcgYlRNQjQJBAJ9EQnwa05i72DcBZakyh/NdnM1X2D4TBeLE4PMUN26TR1anYXy6M8BWzN296a" +
				"1H5cmsESJizfAsaaqc3+P2da8=";
		sParaTemp.put("sign",GatewayUtil.signRequest(sParaTemp,privateKey, "RSA"));
		sParaTemp.put("merOrderId",AESUtil.encrypt(sParaTemp.get("merOrderId"), "2F1c/g88S95y6nuz52XBDA=="));//订单号，AES加密。
		sParaTemp.put("tranAmt",AESUtil.encrypt("0.10", "2F1c/g88S95y6nuz52XBDA=="));//单位元。AES加密。
		sParaTemp.put("bankCardNo",AESUtil.encrypt("6217000010074078455", "2F1c/g88S95y6nuz52XBDA=="));//卡号。AES加密
		sParaTemp.put("idNo", AESUtil.encrypt("230224198006132014", "2F1c/g88S95y6nuz52XBDA=="));
		sParaTemp.put("bankAcctName",AESUtil.encrypt("张志国", "2F1c/g88S95y6nuz52XBDA=="));
		sParaTemp.put("mobileNo",AESUtil.encrypt("13120033859", "2F1c/g88S95y6nuz52XBDA=="));
		sParaTemp.put("signType", "RSA");
		log.info("创新快捷下单请求参数:"+sParaTemp);
		String resultString = GatewayUtil.httpPostByDefaultTime(
				new URI("http://106.120.193.133/gateway/quickpay/api/placeQuickPay"), sParaTemp, "UTF-8");
		log.info(resultString);
	} catch(Exception e){
		e.printStackTrace();
	}
	}
	public static void qer(){
		try {
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", "confirmPay");
			sParaTemp.put("version", "V1");
			sParaTemp.put("merchantId", "000330048160189");	
			sParaTemp.put("payType","03");
			sParaTemp.put("charset", "UTF-8");
			//业务参数
			sParaTemp.put("merOrderId","1496719347637");//订单号，AES加密。
			sParaTemp.put("verifyCode","455659");//单位元。AES加密。
			

			String privateKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK4/1GMpitdy2foRpyQxPw5T9s9XYONp5M0AQfk4JYBKV41q" +
					"+kZdl32g0jXFUjesbmqaDT9mUNkcLHXrFXv3erGvP3A9DWucWwViecgitN5ZzDiYgxcA6iuMjBlg9e1nLHy9lYWzjKZMAnTEsWLWccYweZ4XS" +
					"cq4uHwBrNqag4gZAgMBAAECgYAqToUarnNvbAtQlAio2OweZnR+UxDLJmLPA0kBYYh5ZZNsWpT5kK+/aNShCY+0xMDqUQz2X7kxfY2t8" +
					"637s1Kuet/8KBup3AhOt6h2zfD4dYQg1DAWjiwlky8OIdJ3xeSs5KX0F8pcs0NRC7XASyVvQl9TNuBrI25nskCE2zJFoQJBANwwQyJdV" +
					"Ng4EFmlRZ3hYzoWLSmDHjO61LA/f9n0pmOAW0gUG5rs9j+3XkB4dZPd3jqZdQ5WtYN4k/mUQ7NXeosCQQDKltmSQqfTKRdFdvbEikWb" +
					"EXQyXRy9LoG7JZR+XzVgJEyOCv3Cq/itb3FmYamsinjCi0B9HURAzvqvBteVU/BrAkBu1qSUweKZzO7+EWIP7YsjjFJDmUs0wxGZfv" +
					"1+29Z+M/i/OdADltEGlemODpUxT/g0C1ePVjqc6a+jbOz/dEOtAkEAiA6ThGT7rRcVp/Nje0+Zu7EXJpPeLzfCrNtqRQzQHgeC2oqLL" +
					"mislF4Z/LZua5B71bwLzXsQUa4wMcgYlRNQjQJBAJ9EQnwa05i72DcBZakyh/NdnM1X2D4TBeLE4PMUN26TR1anYXy6M8BWzN296a" +
					"1H5cmsESJizfAsaaqc3+P2da8=";
			sParaTemp.put("sign",GatewayUtil.signRequest(sParaTemp,privateKey, "RSA"));
			sParaTemp.put("merOrderId",AESUtil.encrypt(sParaTemp.get("merOrderId"), "2F1c/g88S95y6nuz52XBDA=="));//订单号，AES加密。
			sParaTemp.put("signType", "RSA");
			log.info("创新快捷确认请求参数:"+sParaTemp);
			String resultString = GatewayUtil.httpPostByDefaultTime(
					new URI("http://106.120.193.133/gateway/quickpay/api/confirmPay"), sParaTemp, "UTF-8");
			log.info(resultString);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public static void query(){
		log.info("dfdff");
		try {
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("serviceName", "queryResult");
			sParaTemp.put("version", "V1");
			sParaTemp.put("merchantId", "000330048160189");	
			sParaTemp.put("payType","03");
			sParaTemp.put("charset", "UTF-8");
			//业务参数
			sParaTemp.put("merOrderId","1496719347637");//订单号，AES加密。
			sParaTemp.put("clientIp","127.0.0.1");//单位元。AES加密。

			String privateKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK4/1GMpitdy2foRpyQxPw5T9s9XYONp5M0AQfk4JYBKV41q" +
					"+kZdl32g0jXFUjesbmqaDT9mUNkcLHXrFXv3erGvP3A9DWucWwViecgitN5ZzDiYgxcA6iuMjBlg9e1nLHy9lYWzjKZMAnTEsWLWccYweZ4XS" +
					"cq4uHwBrNqag4gZAgMBAAECgYAqToUarnNvbAtQlAio2OweZnR+UxDLJmLPA0kBYYh5ZZNsWpT5kK+/aNShCY+0xMDqUQz2X7kxfY2t8" +
					"637s1Kuet/8KBup3AhOt6h2zfD4dYQg1DAWjiwlky8OIdJ3xeSs5KX0F8pcs0NRC7XASyVvQl9TNuBrI25nskCE2zJFoQJBANwwQyJdV" +
					"Ng4EFmlRZ3hYzoWLSmDHjO61LA/f9n0pmOAW0gUG5rs9j+3XkB4dZPd3jqZdQ5WtYN4k/mUQ7NXeosCQQDKltmSQqfTKRdFdvbEikWb" +
					"EXQyXRy9LoG7JZR+XzVgJEyOCv3Cq/itb3FmYamsinjCi0B9HURAzvqvBteVU/BrAkBu1qSUweKZzO7+EWIP7YsjjFJDmUs0wxGZfv" +
					"1+29Z+M/i/OdADltEGlemODpUxT/g0C1ePVjqc6a+jbOz/dEOtAkEAiA6ThGT7rRcVp/Nje0+Zu7EXJpPeLzfCrNtqRQzQHgeC2oqLL" +
					"mislF4Z/LZua5B71bwLzXsQUa4wMcgYlRNQjQJBAJ9EQnwa05i72DcBZakyh/NdnM1X2D4TBeLE4PMUN26TR1anYXy6M8BWzN296a" +
					"1H5cmsESJizfAsaaqc3+P2da8=";
			sParaTemp.put("sign",GatewayUtil.signRequest(sParaTemp,privateKey, "RSA"));
			sParaTemp.put("signType", "RSA");
			log.info("创新快捷查询请求参数:"+sParaTemp);
			String resultString = GatewayUtil.httpPostByDefaultTime(
					new URI("http://106.120.193.133/gateway/order/queryResult"), sParaTemp, "UTF-8");
			log.info(resultString);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
