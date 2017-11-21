package com.pay.merchantinterface.service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.JWebConstant;
import util.Tools;
import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ielpm.mer.sdk.secret.CertUtil;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
import com.third.ys.util.ParamUtil;
import com.third.ys.util.ResponseUtil;
import com.third.ys.util.http.Httpz;
/**
 * 富友接口服务类
 * @author Administrator
 *
 */
public class YSPayService {
	private static final Log log = LogFactory.getLog(YSPayService.class);

	public static Map<String, String> YS_B2C_BANK_CODE = new HashMap<String, String>();
	
	public static Properties prop = new Properties();
	public static CertUtil certUtil=null;
	static {
		prop.setProperty("keyStorePath", JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
				+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("YS_PRI_KEY"));
		prop.setProperty("cerPath",JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
				+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("YS_PUB_KEY"));
		prop.setProperty("keyPass", PayConstant.PAY_CONFIG.get("YS_KEY_PASSWROD"));
		certUtil = new CertUtil(prop);
		YS_B2C_BANK_CODE.put("ICBC","01020000");//工商
		YS_B2C_BANK_CODE.put("ABC","01030000");//农业
		YS_B2C_BANK_CODE.put("BOC","01040000");//中国
		YS_B2C_BANK_CODE.put("CCB","01050000");//建设
		YS_B2C_BANK_CODE.put("BOCOM","03010000");//交通
		YS_B2C_BANK_CODE.put("CMB","03080000");//招商
		YS_B2C_BANK_CODE.put("CNCB","03020000");//中信
		YS_B2C_BANK_CODE.put("CEB","03030000");//光大银行。
		YS_B2C_BANK_CODE.put("HXB","03040000");//华夏
		YS_B2C_BANK_CODE.put("PSBC","01000000");//邮政
		YS_B2C_BANK_CODE.put("CMBC","03050000");//民生
		YS_B2C_BANK_CODE.put("GDB","03060000");//广发
		YS_B2C_BANK_CODE.put("CIB","03090000");//兴业
		YS_B2C_BANK_CODE.put("SPDB","03100000");//浦发
		YS_B2C_BANK_CODE.put("BCCB","04031000");//北京
		YS_B2C_BANK_CODE.put("PAB","04100000");//平安银行。
		YS_B2C_BANK_CODE.put("SHRCB","65012900");//上海农商
		
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
			request.setCharacterEncoding("utf-8");
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YS");  //支付渠道
			payOrder.bankcod = request.getParameter("banks");  //银行代码
			payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			TreeMap<String, String> transMap = new TreeMap<String, String>();
			transMap.put("merchantNo", PayConstant.PAY_CONFIG.get("YS_MERCHANTNO"));
			transMap.put("version", "v1");
			transMap.put("channelNo", "03");//固定值03 pc
			transMap.put("tranSerialNum",payOrder.payordno);//交易流水。
			transMap.put("bankId", YS_B2C_BANK_CODE.get(payOrder.bankcod));//银行编号。
			transMap.put("cardType", "01");//支付卡种
			transMap.put("cardNum", "");//卡号。
			transMap.put("tranTime", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//交易时间
			transMap.put("currency", "CNY");
			transMap.put("amount", String.valueOf(payOrder.txamt));//交易金额，单位分。
			transMap.put("bizType", "16");//其他
			transMap.put("goodsName", "payOnline");
			transMap.put("goodsInfo", "");//商品信息可以为空
			transMap.put("goodsNum", "");//商品数量可以为空。
			transMap.put("notifyUrl",PayConstant.PAY_CONFIG.get("YS_PAY_WG_NOTIFY_URL"));//异步通知。
			transMap.put("returnUrl", prdOrder.returl==null||prdOrder.returl.length()==0?"#":prdOrder.returl);//同步通知。
			transMap.put("buyerName", certUtil.encrypt(Tools.getUniqueIdentify()));
			transMap.put("buyerId", Tools.getUniqueIdentify());//买家id
			transMap.put("contact", "");
			transMap.put("valid", "");
			transMap.put("ip", PayConstant.PAY_CONFIG.get("YS_IP"));
			transMap.put("referer", "");
			transMap.put("remark","");
			transMap.put("YUL1","");
			// 组织签名字符串
			String signMsg = ParamUtil.getSignMsg(transMap);
			// 签名
			String sign =certUtil.sign(signMsg);
			 //将签名放入交易map中
			transMap.put("sign", sign);
			log.info("易势直连网银请求数据:"+transMap.toString());
			request.setAttribute("dataMap", transMap);
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
	public void pay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YS"); // 支付渠道
			payOrder.bankcod = payRequest.bankId;  //银行代码
			payOrder.bankCardType = payRequest.accountType;
			TreeMap<String, String> transMap = new TreeMap<String, String>();
			transMap.put("merchantNo", PayConstant.PAY_CONFIG.get("YS_MERCHANTNO"));
			transMap.put("version", "v1");
			transMap.put("channelNo", "03");//固定值03 pc
			transMap.put("tranSerialNum",payOrder.payordno);//交易流水。
			transMap.put("bankId", YS_B2C_BANK_CODE.get(payOrder.bankcod));//银行编号。
			transMap.put("cardType", "01");//支付卡种
			transMap.put("cardNum", "");//卡号。
			transMap.put("tranTime", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			transMap.put("currency", "CNY");
			transMap.put("amount", String.valueOf(payOrder.txamt));//交易金额，单位分。
			transMap.put("bizType", "16");//
			transMap.put("goodsName", "payOnline");
			transMap.put("goodsInfo", "");//
			transMap.put("goodsNum", "");//。
			transMap.put("notifyUrl",PayConstant.PAY_CONFIG.get("YS_PAY_WG_NOTIFY_URL"));//。
			transMap.put("returnUrl", prdOrder.returl==null||prdOrder.returl.length()==0?"#":prdOrder.returl);//。
			transMap.put("buyerName", certUtil.encrypt(Tools.getUniqueIdentify()));
			transMap.put("buyerId", Tools.getUniqueIdentify());//
			transMap.put("contact", "");
			transMap.put("valid", "");
			transMap.put("ip", PayConstant.PAY_CONFIG.get("YS_IP"));
			transMap.put("referer", "");
			transMap.put("remark","");
			transMap.put("YUL1","");
			// 组织签名字符串
			String signMsg = ParamUtil.getSignMsg(transMap);
			// 签名
			String sign =certUtil.sign(signMsg);
			 //将签名放入交易map中
			transMap.put("sign", sign);
			log.info("商户接口-易势直连网银请求数据:"+transMap.toString());
			request.setAttribute("dataMap", transMap);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 渠道补单---网关
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			TreeMap<String, String> transMap = new TreeMap<String, String>();
			transMap.put("merchantNo", PayConstant.PAY_CONFIG.get("YS_MERCHANTNO"));
			transMap.put("version", "v1");
			transMap.put("channelNo", "03");//
			transMap.put("tranSerialNum", payOrder.payordno);
			transMap.put("remark","");
			transMap.put("YUL1","");
			 //组织签名字符串
			String signMsg = ParamUtil.getSignMsg(transMap);
			 //签名
			String sign = certUtil.sign(signMsg);
			 //将签名放入交易map中
			transMap.put("sign", sign);
			 //发送扫码请求报文
			log.info("易势网关查询请求参数:"+transMap.toString());
			String res = new Httpz().post(PayConstant.PAY_CONFIG.get("YS_PAY_WG_QUERY_URL"), transMap);
			/**
			 * {"YUL1":"","channelNo":"03","merchantNo":"000020161103001","remark":"","rtnCode":"2000","rtnMsg":"订单不存在",
			 * "sign":"IEQosVaKtdCVb5hqIZpdUTLa\/JZnKsqTo8gHph7IDtzmxtQ\/EJu88qUDT4vS7nl6n1nzR0Q32JErSdqcqPKYt4xqRIME73YtzqMPhrE9LfZFdJujQjCKod89YVcWBWUK7GK35QDYMBK5F1sXPyrgF76W+6l0bwxCreY85ZtfGvs=",
			 * "tranSerialNum":"16958755","version":"v1"}
			 * 
			 * 
			 * {"YUL1":"","amount":10000,"merchantNo":"000020161103001","paySerialNo":"2017050800136616","refundAmt":0,"refundCount":0,"remark":"",
			 * "rtnCode":"0000","rtnCodeY":"","rtnMsg":"交易成功","rtnMsgY":"",
			 * "sign":"HB9nMFdV\/A5C56RyfgFYdwuOFnc5ujj3dit4T4FnriJk5rLMnzvu9Fzch46FWK05hM2E7tefNbH\/0VblknlL9GOlgl4pKfNtyEo4Zd0OLhOg7jD36uyJ1vB7R8LosGaMBhoc1PQpORnhzqnWF0V2\/3nJhKXFOWMtjXZpn2n6p\/4=",
			 * "status":0,"tranSerialNum":"qj0VwaK2Q3rKTOO","tranSerialNumY":""}
			 */
			log.info("易势网关查询响应参数:"+res);
			JSONObject jsonObject = JSON.parseObject(res);
			if("0000".equals(jsonObject.getString("rtnCode"))){
				if("1".equals(jsonObject.getString("status"))&&"0000".equals(jsonObject.getString("rtnCodeY"))){
					payOrder.ordstatus="01";//支付成功
		        	new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("2".equals(jsonObject.getString("status"))&&!"0000".equals(jsonObject.getString("rtnCodeY"))){
					payOrder.ordstatus="02";//支付失败。
		        	new NotifyInterface().notifyMer(payOrder);//支付成功
				}
			}else new Exception("查询失败");
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 扫码接口
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType  1：微信扫码；2：支付宝扫码
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		try {
			TreeMap<String, String> transMap = new TreeMap<String, String>();
			transMap.put("merchantNo",PayConstant.PAY_CONFIG.get("YS_MERCHANTNO"));
			transMap.put("version", "v1");
			transMap.put("channelNo", "05");
			transMap.put("tranCode", "YS1003");
			transMap.put("tranFlow", payOrder.payordno);
			transMap.put("tranDate", new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()));
			transMap.put("tranTime", new java.text.SimpleDateFormat("HHmmss").format(new Date()));
			transMap.put("amount", String.valueOf(payOrder.txamt));
			transMap.put("payType", productType);//1：微信扫码，2：支付宝扫码。
			transMap.put("bindId", PayConstant.PAY_CONFIG.get("YS_BINDID"));
			transMap.put("notifyUrl",PayConstant.PAY_CONFIG.get("YS_PAY_SCAN_NOTIFY_URL"));
			transMap.put("bizType", "16");
			transMap.put("goodsName", "payOnline");
			transMap.put("buyerName", certUtil.encrypt(payRequest.userName));
			transMap.put("buyerId", Tools.getUniqueIdentify());
			transMap.put("remark","payOnline");
			transMap.put("goodsInfo", "");
			transMap.put("goodsNum", "");
			transMap.put("contact", "");
			transMap.put("ext1","payOnline");
			transMap.put("ext2","payOnline");
			transMap.put("YUL1","payOnline");
			transMap.put("YUL2","payOnline");
			transMap.put("YUL3","payOnline");
			 //组织签名字符串
			String signMsg = ParamUtil.getSignMsg(transMap);
			// 签名
			String sign = certUtil.sign(signMsg);
			 //将签名放入交易map中
			transMap.put("sign", sign);
			 //发送扫码请求报文
			log.info("易势扫码请求参数:"+transMap.toString());
			String asynMsg = new Httpz("utf-8",30000,30000).post(PayConstant.PAY_CONFIG.get("YS_PAY_SCAN_URL"), transMap);
			log.info("易势扫码响应参数:"+asynMsg);
			/**
			 * {YUL1=payOnline, YUL2=payOnline, YUL3=payOnline, channelNo=05, ext1=payOnline, ext2=payOnline, merchantNo=000020161103001,
			 *  qrCodeURL=https://qr.alipay.com/bax04999ys5r4t07dtsl00b1, rtnCode=0000, rtnMsg=成功, tranCode=YS1003, 
			 *  tranFlow=1494465983346, version=v1}
			 */
			Map<String,String> resMap = ResponseUtil.parseResponse(asynMsg,certUtil);
			if("0000".equals(resMap.get("rtnCode"))){
				 return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
							"codeUrl=\""+resMap.get("qrCodeURL")+"\" respCode=\"000\" respDesc=\"处理成功\"/>";
			}else{
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
			       		   "<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
			       			"codeUrl=\"\" respCode=\"-1\" respDesc=\""+resMap.get("rtnMsg").toString()+"\"/>"; 
			}
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
	public void ScanchannelQuery(PayOrder payOrder) throws Exception{
		try {
			TreeMap<String, String> transMap = new TreeMap<String, String>();
			transMap.put("merchantNo", PayConstant.PAY_CONFIG.get("YS_MERCHANTNO"));
			transMap.put("version", "v1");
			transMap.put("channelNo", "05");//固定值05
			transMap.put("tranCode", "YS2002");
			transMap.put("tranSerialNumY", payOrder.payordno);
			transMap.put("remark", "");
			transMap.put("YRL1", "");
			// 组织签名字符串
			String signMsg = ParamUtil.getSignMsg(transMap);
			// 签名
			String sign = certUtil.sign(signMsg);
			// 将签名放入交易map中
			transMap.put("sign", sign);
			// 发送扫码请求报文
			String asynMsg = new Httpz().post(PayConstant.PAY_CONFIG.get("YS_PAY_SCAN_URL"), transMap);
			log.info("易势扫码查询请求:"+transMap.toString());
			Map<String,String> resultMap = ResponseUtil.parseResponse(asynMsg,certUtil);
			log.info("易势扫码查询响应:"+asynMsg);
			if("0000".equals(resultMap.get("rtnCode"))){
				if("1".equals(resultMap.get("status"))&&"0000".equals(resultMap.get("rtnCodeY"))){
					payOrder.ordstatus="01";//支付成功
		        	new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("2".equals(resultMap.get("status"))&&!"0000".equals(resultMap.get("rtnCodeY"))){
					payOrder.ordstatus="02";//失败成功
		        	new NotifyInterface().notifyMer(payOrder);//失败成功
				}else if("0".equals(resultMap.get("status"))&&!"0000".equals(resultMap.get("rtnCodeY"))){
					//处理中暂时不处理。
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
