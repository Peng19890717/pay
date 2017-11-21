package com.pay.merchantinterface.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.DataTransUtil;
import util.MD5;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.pay.refund.dao.PayRefund;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.third.jingdong.service.MotoPayProvider;
import com.third.jingdong.util.DES;
import com.third.jingdong.util.XmlMsg;
import com.third.jingdongdaifu.wangyin.enctypt.util.Contants;
import com.third.jingdongdaifu.wangyin.enctypt.util.RequestUtil;
/**
 * 京东接口服务类
 * @author Administrator
 *
 */
public class JDPayService {
	private static final Log log = LogFactory.getLog(JDPayService.class);
	public static Map<String, String> B2B_BANK_CODE = new HashMap<String, String>();
	public static Map<String, String> B2C_BANK_CODE = new HashMap<String, String>();
	public static Map<String, String> QUICKPAY_BANK_CODE = new HashMap<String, String>();
	public static Map<String, String> JD_ERROR_INFO = new HashMap<String, String>();
	static {
		//B2C_BANK_CODE.put("平台银行码","京东码");//京东码//平台银行名
		B2C_BANK_CODE.put("EGBANK","344");//EGB//恒丰银行
		B2C_BANK_CODE.put("BOHC","317");//CBHB//渤海银行
		B2C_BANK_CODE.put("SHRCB","343");//SRCB//上海农商银行
		B2C_BANK_CODE.put("ICBC","1025");//ICBC//中国工商银行
		B2C_BANK_CODE.put("ABC","103");//ABC//中国农业银行
		B2C_BANK_CODE.put("BOC","104");//BOC//中国银行
		B2C_BANK_CODE.put("CCB","1051");//CCB//中国建设银行
		B2C_BANK_CODE.put("BOCOM","3407");//BCM//交通银行
		B2C_BANK_CODE.put("CMB","3080");//CMB//招商银行
		B2C_BANK_CODE.put("PSBC","3230");//PSBC//中国邮政储蓄银行
		B2C_BANK_CODE.put("CNCB","313");//CITIC//中信银行
		B2C_BANK_CODE.put("CEB","312");//CEB//中国光大银行
		B2C_BANK_CODE.put("HXB","311");//HXB//华夏银行
		B2C_BANK_CODE.put("CIB","309");//CIB//兴业银行
		B2C_BANK_CODE.put("GDB","3061");//CGB//广发银行
		B2C_BANK_CODE.put("PAB","307");//PAB//平安银行
		B2C_BANK_CODE.put("SPDB","314");//SPDB//上海浦东发展银行
		B2C_BANK_CODE.put("BCCB","310");//BOB//北京银行
		B2C_BANK_CODE.put("BOS","326");//BOS//上海银行
		B2C_BANK_CODE.put("NJBC","316");//NJCB//南京银行
		B2C_BANK_CODE.put("NBBC","302");//NBCB//宁波银行
		B2C_BANK_CODE.put("CZCB","403");//CZCB//稠州银行
		B2C_BANK_CODE.put("BRCB","335");//BJRCB//北京农村商业银行
		B2C_BANK_CODE.put("CMBC","305");//CMBC//民生银行
		
		//快捷支付映射编码。
		QUICKPAY_BANK_CODE.put("EGBANK", "EGB");//EGB//恒丰银行
		QUICKPAY_BANK_CODE.put("BOHC", "CBHB");//EGB//渤海银行
		QUICKPAY_BANK_CODE.put("SHRCB", "SRCB");//SRCB//上海农商银行
		QUICKPAY_BANK_CODE.put("ICBC", "ICBC");//ICBC//中国工商银行
		QUICKPAY_BANK_CODE.put("ABC", "ABC");//ABC//中国农业银行
		QUICKPAY_BANK_CODE.put("BOC", "BOC");///BOC//中国银行
		QUICKPAY_BANK_CODE.put("CCB", "CCB");//CCB//中国建设银行
		QUICKPAY_BANK_CODE.put("BOCOM", "BCM");//BCM//交通银行
		QUICKPAY_BANK_CODE.put("CMB", "CMB");//CMB//招商银行
		QUICKPAY_BANK_CODE.put("PSBC", "PSBC");//PSBC//中国邮政储蓄银行
		QUICKPAY_BANK_CODE.put("CNCB", "CITIC");//CITIC//中信银行
		QUICKPAY_BANK_CODE.put("CEB", "CEB");//CEB//中国光大银行
		QUICKPAY_BANK_CODE.put("HXB", "HXB");//HXB//华夏银行
		QUICKPAY_BANK_CODE.put("CIB", "CIB");//CIB//兴业银行
		QUICKPAY_BANK_CODE.put("GDB", "CGB");//CGB//广发银行
		QUICKPAY_BANK_CODE.put("PAB", "PAB");//PAB//平安银行
		QUICKPAY_BANK_CODE.put("SPDB", "SPDB");//SPDB//上海浦东发展银行
		QUICKPAY_BANK_CODE.put("BCCB", "BOB");//BOB//北京银行
		QUICKPAY_BANK_CODE.put("BOS", "BOS");//BOS//上海银行
		QUICKPAY_BANK_CODE.put("NJBC", "NJCB");///NJCB//南京银行
		QUICKPAY_BANK_CODE.put("NBBC", "NBBC");//NBCB//宁波银行
		QUICKPAY_BANK_CODE.put("CZCB", "CZCB");//CZCB//稠州银行
		QUICKPAY_BANK_CODE.put("BRCB", "BJRCB");//BJRCB//北京农村商业银行
		QUICKPAY_BANK_CODE.put("CMBC", "CMBC");//CMBC//民生银行
		
		JD_ERROR_INFO.put("EEE0001","系统异常");
		JD_ERROR_INFO.put("EEE0002","网络异常");
		JD_ERROR_INFO.put("EEE0003","银行异常");
		JD_ERROR_INFO.put("EEE0004","数据库异常");
		JD_ERROR_INFO.put("EES0001","报文解析异常");
		JD_ERROR_INFO.put("EES0002","字符集不正确");
		JD_ERROR_INFO.put("EES0003","版本号不正确");
		JD_ERROR_INFO.put("EES0004","商户号不正确");
		JD_ERROR_INFO.put("EES0005","终端号不正确");
		JD_ERROR_INFO.put("EES0006","交易数据不正确");
		JD_ERROR_INFO.put("EES0007","数据签名不正确");
		JD_ERROR_INFO.put("EES0008","权限不正确");
		JD_ERROR_INFO.put("EES0009","密钥不正确");
		JD_ERROR_INFO.put("EES0010","发卡行不正确");
		JD_ERROR_INFO.put("EES0011","卡类型不正确");
		JD_ERROR_INFO.put("EES0012","交易卡号不正确");
		JD_ERROR_INFO.put("EES0013","卡有效期不正确");
		JD_ERROR_INFO.put("EES0014","卡安全码不正确");
		JD_ERROR_INFO.put("EES0015","持卡人姓名不正确");
		JD_ERROR_INFO.put("EES0016","持卡人证件类型不正确");
		JD_ERROR_INFO.put("EES0017","持卡人证件号不正确");
		JD_ERROR_INFO.put("EES0018","持卡人手机号不正确");
		JD_ERROR_INFO.put("EES0019","交易类型不正确");
		JD_ERROR_INFO.put("EES0020","交易号不正确");
		JD_ERROR_INFO.put("EES0021","交易金额不正确");
		JD_ERROR_INFO.put("EES0022","交易币种不正确");
		JD_ERROR_INFO.put("EES0023","交易日期不正确");
		JD_ERROR_INFO.put("EES0024","交易时间不正确");
		JD_ERROR_INFO.put("EES0025","交易通知地址不正确");
		JD_ERROR_INFO.put("EES0026","交易备注不正确");
		JD_ERROR_INFO.put("EES0027","交易验证码不正确");
		JD_ERROR_INFO.put("EES0028","交易卡号网银不受理");
		JD_ERROR_INFO.put("EES0029","交易卡号商户不受理");
		JD_ERROR_INFO.put("EES0030","交易受理银行繁忙");
		JD_ERROR_INFO.put("EES0031","交易受理渠道繁忙");
		JD_ERROR_INFO.put("EES0032","交易重复");
		JD_ERROR_INFO.put("EES0033","交易号重复");
		JD_ERROR_INFO.put("EES0034","交易验证码申请不受理");
		JD_ERROR_INFO.put("EES0035","交易验证码过期");
		JD_ERROR_INFO.put("EES0036","交易不存在");
		JD_ERROR_INFO.put("EES0037","原交易号不正确");
		JD_ERROR_INFO.put("EES0038","原交易不允许此操作");
		JD_ERROR_INFO.put("EES0039","原交易处理中");
		JD_ERROR_INFO.put("EES0040","退款余额不足");
		JD_ERROR_INFO.put("EES0041","查询银行列表错误");
		JD_ERROR_INFO.put("EES0042","找不到相应的银行列表信息");
		JD_ERROR_INFO.put("EES0043","卡号未签约");
		JD_ERROR_INFO.put("EES0044","卡号未做签约申请");
		JD_ERROR_INFO.put("EEB0001","银行交易不支持");
		JD_ERROR_INFO.put("EEB0002","银行签约失败");
		JD_ERROR_INFO.put("EEB0003","银行解约失败");
		JD_ERROR_INFO.put("EEB0004","银行交易失败");
		JD_ERROR_INFO.put("EEB0005","银行签约姓名校验失败");
		JD_ERROR_INFO.put("EEB0006","银行签约手机号校验失败");
		JD_ERROR_INFO.put("EEB0007","银行签约证件号校验失败");
		JD_ERROR_INFO.put("EEB0008","银行签约卡有效期校验失败");
		JD_ERROR_INFO.put("EEB0009","银行签约卡安全码校验失败");
		JD_ERROR_INFO.put("EEB0010","银行不支持的卡类型");
		JD_ERROR_INFO.put("EEB0011","银行不支持的卡号");
		JD_ERROR_INFO.put("EEB0012","银行卡号状态异常");
		JD_ERROR_INFO.put("EEB0013","银行卡号未开通快捷业务");
		JD_ERROR_INFO.put("EEB0014","银行卡号余额不足");
		JD_ERROR_INFO.put("EEB0015","银行单笔金额超限");
		JD_ERROR_INFO.put("EEB0016","银行日交易金额超限");
		JD_ERROR_INFO.put("EEB0017","银行日交易次数超限");
		JD_ERROR_INFO.put("EER0001","风险校验失败");
		
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try{
			//初始化定义参数
			String v_amount = String.format("%.2f", ((double)payOrder.txamt)/100d);
			String v_oid = payOrder.payordno;
			String v_mid = PayConstant.PAY_CONFIG.get("JD_B2C_WEB_MERCHANT_ID");
			String v_url = prdOrder.returl;
			request.setAttribute("v_mid",PayConstant.PAY_CONFIG.get("JD_B2C_WEB_MERCHANT_ID"));// 1001是网银在线的测试商户号，商户要替换为自己的商户号。
			request.setAttribute("v_url",prdOrder.returl==null||prdOrder.returl.length()==0?"javascript:history.back()":prdOrder.returl);//商户自定义返回接收支付结果的页面
			request.setAttribute("remark2","[url:="+PayConstant.PAY_CONFIG.get("JD_B2C_WEB_NOTIFY_URL")+"]");//服务器异步通知的接收地址。对应AutoReceive.jsp示例。必须要有[url:=]格式
			request.setAttribute("v_oid",payOrder.payordno);//订单号
			request.setAttribute("v_amount",String.format("%.2f", ((double)payOrder.txamt)/100d));//订单金额
			request.setAttribute("v_moneytype","CNY");// 币种
			request.setAttribute("v_md5info",MD5.getDigest(v_amount+"CNY"+v_oid+v_mid+v_url+
					PayConstant.PAY_CONFIG.get("JD_B2C_WEB_MD5_KEY")).toUpperCase());// 对拼凑串MD5私钥加密后的值
			request.setAttribute("pmode_id",B2C_BANK_CODE.get(payOrder.bankcod)==null?payOrder.bankcod:B2C_BANK_CODE.get(payOrder.bankcod)); //代表选择的银行
			log.info("京东网关请求报文:"+"v_mid="+v_mid+"&v_oid="+v_oid+"&v_url"+v_url+"&remark2="+"[url:="+PayConstant.PAY_CONFIG.get("JD_B2C_WEB_NOTIFY_URL")+"]"
			+"&v_amount="+v_amount+"&v_moneytype=CNY&v_md5info="+request.getAttribute("v_md5info")+"&pmode_id="+request.getAttribute("pmode_id"));
	        new PayOrderService().updateOrderForBanks(payOrder);
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
	public void pay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_JD"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		try{
			//初始化定义参数
			String v_amount = String.format("%.2f", ((double)payOrder.txamt)/100d);
			String v_oid = payOrder.payordno;
			String v_mid = PayConstant.PAY_CONFIG.get("JD_B2C_WEB_MERCHANT_ID");
			String v_url = prdOrder.returl;
			String key = PayConstant.PAY_CONFIG.get("JD_B2C_WEB_MD5_KEY");
			request.setAttribute("v_mid",PayConstant.PAY_CONFIG.get("JD_B2C_WEB_MERCHANT_ID"));// 1001是网银在线的测试商户号，商户要替换为自己的商户号。
			request.setAttribute("v_url",prdOrder.returl);//商户自定义返回接收支付结果的页面
			request.setAttribute("remark2","[url:="+PayConstant.PAY_CONFIG.get("JD_B2C_WEB_NOTIFY_URL")+"]");//服务器异步通知的接收地址。对应AutoReceive.jsp示例。必须要有[url:=]格式
			request.setAttribute("v_oid",payOrder.payordno);//订单号
			request.setAttribute("v_amount",String.format("%.2f", ((double)payOrder.txamt)/100d));//订单金额
			request.setAttribute("v_moneytype","CNY");// 币种
			request.setAttribute("v_md5info",MD5.getDigest(v_amount+"CNY"+v_oid+v_mid+v_url+key).toUpperCase());// 对拼凑串MD5私钥加密后的值
			request.setAttribute("pmode_id",B2C_BANK_CODE.get(payOrder.bankcod)==null?payOrder.bankcod:B2C_BANK_CODE.get(payOrder.bankcod)); //代表选择的银行
			log.info("京东网关请求报文:"+"v_mid="+v_mid+"&v_oid="+v_oid+"&v_url"+v_url+"&remark2="+"[url:="+PayConstant.PAY_CONFIG.get("JD_B2C_WEB_NOTIFY_URL")+"]"
					+"&v_amount="+v_amount+"&v_moneytype=CNY&v_md5info="+request.getAttribute("v_md5info")+"&pmode_id="+request.getAttribute("pmode_id"));
	        new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	public void refund(PayRefund payRefund){
		try{
			log.info("网银退款");
		} catch (Exception e) {
			e.printStackTrace();
			//订单状态，00:退款处理中  01:退款成功 02:退款失败 ，默认00
			payRefund.banksts="02";
			payRefund.bankerror = e.getMessage();
			payRefund.rfsake = e.getMessage();
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			String requestData="{\"MERCHANT\":\""+PayConstant.PAY_CONFIG.get("JD_B2C_WEB_MERCHANT_ID")+"\"," +
					"\"VERSION\":\"1.0.0\",\"TRADE\":\""+payOrder.payordno+"\",\"TYPE\":\"Q\"}";
			//base64加密数据。
			String requestData_base64 = org.apache.commons.codec.binary.Base64.encodeBase64String(requestData.getBytes("UTF-8"));
			//生成MD5签名
	        String requestSign = com.third.jingdong.MD5.md5(requestData_base64,PayConstant.PAY_CONFIG.get("JD_B2C_WEB_MD5_KEY"), true);
	        String paras="CHAR=UTF-8"+"&DATA="+java.net.URLEncoder.encode(requestData_base64)+"&SIGN="+java.net.URLEncoder.encode(requestSign);
	        String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("JD_B2C_WEB_SEARCH_URL"),paras.getBytes("utf-8")),"utf-8");
	        log.info("京东查询返回为解密数据数据:"+res);
	        //将responseStr转换成map
	        String[] results = res.split("&");
	        Map<String, String> map = new HashMap<String, String>();
	        for (String value : results) {
	            //不要用split("="),因为value由base64生成，可能含有多个=号
	            int n = value.indexOf("=");
	            map.put(value.substring(0, n), value.substring(n + 1));
	        }
	        String data = new String(Base64.decode(map.get("DATA")),"utf-8");
	        log.info("京东查询解密后data数据:"+data);
	        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(data);
	        //{"AMOUNT":"1","CODE":"0000000","CURRENCY":"CNY","DATETIME":"2016-11-30 15:31:26 991","DESC":"鎴愬姛","MERCHANT":"110252781002","STATUS":"S","TRADE":"q41dXOo2Q3rKTOG","TYPE":"Q","VERSION":"1.0.0"}
	        if("0000000".equals(jsonObject.get("CODE"))){
	    	    if("S".equals(jsonObject.get("STATUS"))){
	    		    payOrder.ordstatus="01";//支付成功
	    		    new NotifyInterface().notifyMer(payOrder);//支付成功
	    	}
//	    	else if("F".equals(jsonObject.get("STATUS"))) payOrder.ordstatus="02";//支付失败。
//	    	else if("I".equals(jsonObject.get("STATUS"))) payOrder.ordstatus="03";//处理中
	        } else {
	    	    throw new Exception("交易返回码："+jsonObject.get("CODE")+"||"+"交易返回码描述："+jsonObject.get("DESC"));
	        }
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 快捷支付--签约
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String quickPay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);//通过卡号获取卡类信息。
		StringBuffer data = new StringBuffer();
		data.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		data.append("<DATA>");
		data.append("<CARD>");
		data.append("<BANK>"+QUICKPAY_BANK_CODE.get(cardBin.bankCode)+"</BANK>");
		if(cardBin.cardType.equals("0")){
			data.append("<TYPE>D</TYPE>");
			data.append("<NO>"+payRequest.cardNo+"</NO>");
			data.append("<EXP></EXP>");
			data.append("<CVV2></CVV2>");
		} else if(cardBin.cardType.equals("1")){
			data.append("<TYPE>C</TYPE>");
			data.append("<NO>"+payRequest.cardNo+"</NO>");
			data.append("<EXP>"+payRequest.validPeriod+"</EXP>");
			data.append("<CVV2>"+payRequest.cvv2+"</CVV2>");
		}
		data.append("<NAME>"+payRequest.userName+"</NAME>");
		data.append("<IDTYPE>I</IDTYPE>");
		data.append("<IDNO>"+payRequest.credentialNo+"</IDNO>");
		data.append("<PHONE>"+payRequest.userMobileNo+"</PHONE>");
		data.append("</CARD>");
		data.append("<TRADE>");
		data.append("<TYPE>V</TYPE>");
		data.append("<ID>"+payOrder.payordno+"</ID>");
		data.append("<AMOUNT>"+payRequest.merchantOrderAmt+"</AMOUNT>");
		data.append("<CURRENCY>CNY</CURRENCY>");
		data.append("</TRADE>");
		data.append("</DATA>");
		log.info("京东快捷签约请求data报文:"+data.toString());
		String dataDES = DES.encrypt(data.toString(),PayConstant.PAY_CONFIG.get("JD_QUICKPAY_DES"),"UTF-8");
		//计算数据签名version+merchant+terminal+data元素，MD5密钥是在商户在网银在线后台设置，签名是为了验证请求的合法性
		String signMD5 = com.third.jingdong.util.MD5.md5(PayConstant.PAY_CONFIG.get("JD_QUICKPAY_VERSION")+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MERCHANT_ID")+
				PayConstant.PAY_CONFIG.get("JD_QUICKPAY_TERMINAL")+dataDES, PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MD5KEY"));
		StringBuffer params= new StringBuffer();
		params.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		params.append("<CHINABANK>");
		params.append("<VERSION>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_VERSION")+"</VERSION>");
		params.append("<MERCHANT>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MERCHANT_ID")+"</MERCHANT>");
		params.append("<TERMINAL>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_TERMINAL")+"</TERMINAL>");
		params.append("<DATA>"+dataDES+"</DATA>");
		params.append("<SIGN>"+signMD5+"</SIGN>");
		params.append("</CHINABANK>");
		log.info("京东快捷签约请求params报文:"+params.toString());
		//最后将xml用base64加密
		String reqParams = Base64.encode(params.toString().getBytes());
		//发送请求到网银在线快捷支付地址
		MotoPayProvider provider = new MotoPayProvider();
		String respStr = provider.process("UTF-8", reqParams);
		log.info("京东快捷签约返回报文:"+respStr);
		//数据格式是resp=XML数据
		String temResp = respStr.substring(respStr.indexOf("=") + 1);
		if(JD_ERROR_INFO.get(temResp)!=null)throw new Exception("JD快捷签约错误："+JD_ERROR_INFO.get(temResp));
		//快捷支付数据先base64解码
		temResp = new String(Base64.decode(temResp));
		//解析xml中的数据
		Map<String, String> respParams= XmlMsg.parse(temResp);
		//验证数据签名的合法性。版本号+商户号+终端号+交易数据使用md5加密和数据签名比较，md5密钥在网银在线商户后台设置
		if(com.third.jingdong.util.MD5.verify(respParams.get("chinabank.version")
				+respParams.get("chinabank.merchant")+respParams.get("chinabank.terminal")
				+respParams.get("chinabank.data"), PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MD5KEY"), respParams.get("chinabank.sign"))){
			//使用DES解密data交易数据,des密钥网银在线商户后台设置
			String respdataDES = DES.decrypt(respParams.get("chinabank.data"),PayConstant.PAY_CONFIG.get("JD_QUICKPAY_DES"),"UTF-8");
			log.info("京东快捷签约返回报文--交易信息:"+respdataDES);
			Map<String, String> dataParams= XmlMsg.parse(respdataDES);
			if("0000".equals(dataParams.get("data.return.code"))){
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" " +
						"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
						"respCode=\"000\" respDesc=\"操作成功\" " +
						"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			} else{
				payOrder.bankerror=dataParams.get("data.return.desc");
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" " +
						"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
						"respCode=\"-1\" respDesc=\""+dataParams.get("data.return.desc")+"\" " +
						"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			}
		} else{
			throw new Exception("验签失败");
		}
	}
	/**
	 * 快捷支付-确认
	 * @param request
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String certPayConfirm(PayRequest payRequest) throws Exception{
		PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.payOrder.payacno);//通过卡号获取卡类信息。
		StringBuffer data = new StringBuffer();
		data.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		data.append("<DATA>");
		data.append("<CARD>");
		data.append("<BANK>"+QUICKPAY_BANK_CODE.get(cardBin.bankCode)+"</BANK>");
		if("0".equals(cardBin.cardType)){
			data.append("<TYPE>D</TYPE>");
			data.append("<EXP></EXP>");
			data.append("<CVV2></CVV2>");
		} else if("1".equals(cardBin.cardType)){
			data.append("<TYPE>C</TYPE>");
			data.append("<EXP>"+payRequest.validPeriod+"</EXP>");
			data.append("<CVV2>"+payRequest.cvv2+"</CVV2>");
		}
		data.append("<NO>"+payRequest.payOrder.payacno+"</NO>");
		data.append("<NAME>"+payRequest.payOrder.bankpayusernm+"</NAME>");
		data.append("<IDTYPE>I</IDTYPE>");
		data.append("<IDNO>"+payRequest.productOrder.credentialNo+"</IDNO>");
		data.append("<PHONE>"+payRequest.productOrder.mobile+"</PHONE>");
		data.append("</CARD>");
		data.append("<TRADE>");
		data.append("<TYPE>S</TYPE>");
		data.append("<ID>"+payRequest.payOrder.payordno+"</ID>");
		data.append("<AMOUNT>"+payRequest.productOrder.ordamt+"</AMOUNT>");
		data.append("<CURRENCY>CNY</CURRENCY>");
		data.append("<DATE>"+ new SimpleDateFormat("yyyyMMdd").format(new Date())+"</DATE>");
		data.append("<TIME>"+ new SimpleDateFormat("HHmmss").format(new Date())+"</TIME>");
		data.append("<NOTICE>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_NOTIFY_URL")+"</NOTICE>");
		data.append("<NOTE></NOTE>");
		data.append("<CODE>"+payRequest.checkCode+"</CODE>");
		data.append("</TRADE>");
		data.append("</DATA>");
		log.info("京东快捷消费请求data报文:"+data.toString());
		String dataDES = DES.encrypt(data.toString(),PayConstant.PAY_CONFIG.get("JD_QUICKPAY_DES"),"UTF-8");
		//计算数据签名version+merchant+terminal+data元素，MD5密钥是在商户在网银在线后台设置，签名是为了验证请求的合法性
		String signMD5 = com.third.jingdong.util.MD5.md5(PayConstant.PAY_CONFIG.get("JD_QUICKPAY_VERSION")+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MERCHANT_ID")+
				PayConstant.PAY_CONFIG.get("JD_QUICKPAY_TERMINAL")+dataDES, PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MD5KEY"));
		StringBuffer params= new StringBuffer();
		params.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		params.append("<CHINABANK>");
		params.append("<VERSION>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_VERSION")+"</VERSION>");
		params.append("<MERCHANT>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MERCHANT_ID")+"</MERCHANT>");
		params.append("<TERMINAL>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_TERMINAL")+"</TERMINAL>");
		params.append("<DATA>"+dataDES+"</DATA>");
		params.append("<SIGN>"+signMD5+"</SIGN>");
		params.append("</CHINABANK>");
		log.info("京东快捷消费请求params报文:"+params.toString());
		//最后将xml用base64加密
		String reqParams = Base64.encode(params.toString().getBytes());
		//发送请求到网银在线快捷支付地址
		MotoPayProvider provider = new MotoPayProvider();
		String respStr = provider.process("UTF-8", reqParams);
		log.info("京东快捷消费返回报文:"+respStr);
		//数据格式是resp=XML数据
		String temResp = respStr.substring(respStr.indexOf("=") + 1);
		if(JD_ERROR_INFO.get(temResp)!=null)throw new Exception("JD快捷确认错误："+JD_ERROR_INFO.get(temResp));
		//快捷支付数据先base64解码
		temResp = new String(Base64.decode(temResp));
		//解析xml中的数据
		Map<String, String> respParams= XmlMsg.parse(temResp);
		//验证数据签名的合法性。版本号+商户号+终端号+交易数据使用md5加密和数据签名比较，md5密钥在网银在线商户后台设置
		if(com.third.jingdong.util.MD5.verify(respParams.get("chinabank.version")
				+respParams.get("chinabank.merchant")+respParams.get("chinabank.terminal")
				+respParams.get("chinabank.data"), PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MD5KEY"), respParams.get("chinabank.sign"))){
				//使用DES解密data交易数据,des密钥网银在线商户后台设置
				String respdataDES = DES.decrypt(respParams.get("chinabank.data"),PayConstant.PAY_CONFIG.get("JD_QUICKPAY_DES"),"UTF-8");
				log.info("京东快捷消费返回报文--交易信息:"+respdataDES);
				Map<String, String> dataParams= XmlMsg.parse(respdataDES);
				if("0000".equals(dataParams.get("data.return.code"))){
					return "{\"tradeId\":\""+dataParams.get("data.trade.id")+"\",\"tradeStatus\":\""+dataParams.get("data.trade.status")+"\"}";
				} else throw new Exception("交易返回码："+dataParams.get("data.return.code")+"||"
					+"交易返回码描述："+dataParams.get("data.return.desc"));
		 } else{
			throw new Exception("验签失败");
		 }
	}
	/**
	 * 快捷支付渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void quickPayChannelQuery(PayOrder payOrder) throws Exception{
			StringBuffer data = new StringBuffer();
			data.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			data.append("<DATA>");
			data.append("<TRADE>");
			data.append("<TYPE>Q</TYPE>");
			data.append("<ID>"+payOrder.payordno+"</ID>");
			data.append("</TRADE>");
			data.append("</DATA>");
			log.info("京东快捷查询请求data报文:"+data.toString());
			String dataDES = DES.encrypt(data.toString(),PayConstant.PAY_CONFIG.get("JD_QUICKPAY_DES"),"UTF-8");
			//计算数据签名version+merchant+terminal+data元素，MD5密钥是在商户在网银在线后台设置，签名是为了验证请求的合法性
			String signMD5 = com.third.jingdong.util.MD5.md5(PayConstant.PAY_CONFIG.get("JD_QUICKPAY_VERSION")+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MERCHANT_ID")+
					PayConstant.PAY_CONFIG.get("JD_QUICKPAY_TERMINAL")+dataDES, PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MD5KEY"));
			StringBuffer params= new StringBuffer();
			params.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			params.append("<CHINABANK>");
			params.append("<VERSION>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_VERSION")+"</VERSION>");
			params.append("<MERCHANT>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MERCHANT_ID")+"</MERCHANT>");
			params.append("<TERMINAL>"+PayConstant.PAY_CONFIG.get("JD_QUICKPAY_TERMINAL")+"</TERMINAL>");
			params.append("<DATA>"+dataDES+"</DATA>");
			params.append("<SIGN>"+signMD5+"</SIGN>");
			params.append("</CHINABANK>");
			log.info("京东快捷查询请求params报文:"+params.toString());
			//最后将xml用base64加密
			String reqParams = Base64.encode(params.toString().getBytes());
			//发送请求到网银在线快捷支付地址
			MotoPayProvider provider = new MotoPayProvider();
			String respStr = provider.process("UTF-8", reqParams);
			log.info("京东快捷查询返回报文:"+respStr);
			//数据格式是resp=XML数据
			String temResp = respStr.substring(respStr.indexOf("=") + 1);
			if(JD_ERROR_INFO.get(temResp)!=null)throw new Exception("JD快捷渠道查询错误："+JD_ERROR_INFO.get(temResp));
			//快捷支付数据先base64解码
			temResp = new String(Base64.decode(temResp));
			//解析xml中的数据
			Map<String, String> respParams= XmlMsg.parse(temResp);
			//验证数据签名的合法性。版本号+商户号+终端号+交易数据使用md5加密和数据签名比较，md5密钥在网银在线商户后台设置
			if(com.third.jingdong.util.MD5.verify(respParams.get("chinabank.version")
					+respParams.get("chinabank.merchant")+respParams.get("chinabank.terminal")
					+respParams.get("chinabank.data"), PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MD5KEY"), respParams.get("chinabank.sign"))){
					//使用DES解密data交易数据,des密钥网银在线商户后台设置
					String respdataDES = DES.decrypt(respParams.get("chinabank.data"),PayConstant.PAY_CONFIG.get("JD_QUICKPAY_DES"),"UTF-8");
					log.info("京东快捷查询返回报文--交易信息:"+respdataDES);
					Map<String, String> dataParams= XmlMsg.parse(respdataDES);
					if("0000".equals(dataParams.get("data.return.code"))){
						if("0".equals(dataParams.get("data.trade.status"))){
							 payOrder.ordstatus="01";//支付成功
						     new NotifyInterface().notifyMer(payOrder);//支付成功
						} else if("7".equals(dataParams.get("data.trade.status"))){
							 payOrder.ordstatus="02";//支付失败
						     new NotifyInterface().notifyMer(payOrder);
						} else if("6".equals(dataParams.get("data.trade.status"))){
							 payOrder.ordstatus="03";//处理中
						     new NotifyInterface().notifyMer(payOrder);
						}
					} else{
						throw new Exception("交易返回码："+dataParams.get("data.return.code")+"||"+"交易返回码描述："+dataParams.get("data.return.desc"));
					}
			 } else{
				throw new Exception("验签失败");
			 }
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
    			 Map<String, String> map = new HashMap<String, String>();
	   	   		 String request_datetime= new SimpleDateFormat("yyyymmdd").format(new Date())+"T"+new SimpleDateFormat("HHMMSS").format(new Date());
	   	   		 map.put("customer_no",PayConstant.PAY_CONFIG.get("JD_DF_CUSTOMER_NO"));//会员号。
	   	   		 map.put("request_datetime",request_datetime);//订单时间。
	   	   		 map.put("out_trade_no",payRequest.receiveAndPaySingle.id);//商户订单号。
	   	   		 map.put("trade_amount",payRequest.amount);//交易金额。单位分。
	   	   		 map.put("trade_currency","CNY");//交易币种。
	   	   		 map.put("trade_subject","代付");//交易描述。
	   	   		 map.put("pay_tool","TRAN");//TRAN代付到银行卡。
	   	   		 PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
	   	   		 map.put("payee_bank_code",QUICKPAY_BANK_CODE.get(cardBin.bankCode));//收款银行编码
	   	   		 map.put("payee_bank_associated_cod","未定");//联行号。
	   	   		 if("0".equals(payRequest.accountProp)){
	   	   			 map.put("payee_account_type","P");//帐户类型，对私户=P；对公户=C
	   	   			 map.put("payee_bank_associated_cod","");//联行号。
	   	   		 }else{
	   	   			 map.put("payee_account_type","C");//帐户类型，对私户=P；对公户=C
	   	   			 map.put("payee_bank_associated_cod",payRequest.merchant.issuer);//联行号。
	   	   		 }
	   	   		 if("0".equals(cardBin.cardType)){
	   	   			 map.put("payee_card_type","DE");//借记卡=DE；信用卡=CR
	   	   		 } else if("1".equals(cardBin.cardType)){
	   	   			 map.put("payee_card_type","CR");//借记卡=DE；信用卡=CR
	   	   		 }
	   	   		 map.put("payee_account_no",payRequest.accNo);//收款帐户号
	   	   		 map.put("payee_account_name",payRequest.accName);//收款帐户名称
	   	   		 map.put("notify_url",PayConstant.PAY_CONFIG.get("JD_DF_NOTIFY_URL"));//商户处理数据的异步通知地址
	   	   		 RequestUtil demoUtil = new RequestUtil();
	       		//请求
	       		 String responseText = demoUtil.tradeRequestSSL(map,PayConstant.PAY_CONFIG.get("JD_DF_URL"),Contants.encryptType_RSA);
	       		 /**
	       		  * {"customer_no":"360080004001463150","out_trade_no":"qcpzGmm2Q3rKTOK","response_code":"0000","response_datetime":"20170227T144239",
	       		  * "response_message":"成功","sign_data":"83D82D5E8165825A1C5C01D68E70267A08B81FBAE412EF3734DCF6E35C8953BB",
	       		  * "sign_type":"SHA-256","trade_amount":"1","trade_currency":"CNY","trade_no":"20170227100042000053158666","trade_status":"WPAR"}
	       		  */
	       		
	   			//验证数据
	   			Map<String,String> map_res = demoUtil.verifySingReturnData(responseText);
	   			if(map_res==null){
	   				throw new Exception("验签失败");
	   			} else{
	   				if("0000".equals(map_res.get("response_code"))){
	   					if("WPAR".equals(map_res.get("trade_status"))||"BUID".equals(map_res.get("trade_status"))||"ACSU".equals(map_res.get("trade_status"))){
	   						rp.status="0";
							rp.errorMsg = "处理中";
				        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	   					} else if("FINI".equals(map_res.get("trade_status"))){
	   						rp.status="1";
							rp.errorMsg = "交易成功";
				        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	   					} else if("CLOS".equals(map_res.get("trade_status"))){
	   						rp.status="2";
							rp.errorMsg = "交易失败";
				        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	   					}
	   					
	   				} else{
	   					rp.status="2";
						rp.errorMsg =map_res.get("response_message") ;
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	   				}
	   			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
			String request_datetime= new SimpleDateFormat("yyyymmdd").format(new Date())+"T"+new SimpleDateFormat("HHMMSS").format(new Date());
			Map<String,String> paramMap = new HashMap<String, String>();
			paramMap.put("customer_no",PayConstant.PAY_CONFIG.get("JD_DF_CUSTOMER_NO"));//提交者会员号
			paramMap.put("request_datetime",request_datetime);//请求时间
			paramMap.put("out_trade_no",rp.id);//商户订单号
			paramMap.put("trade_type", "T_AGD");
			RequestUtil demoUtil = new RequestUtil();
			String responseText = "";
			//请求
			responseText = demoUtil.tradeRequestSSL(paramMap,PayConstant.PAY_CONFIG.get("JD_DF_QUERY_URL"),null);
			/**
			 * 京东代付响应参数:
			 * {response_datetime=20170227T160517, out_trade_date=Mon Feb 27 15:03:13 CST 2017, 
			 * out_trade_no=qcpFcxM2Q3rKTOG, trade_respmsg====>账务申请失败:360080004001463150000801:可用余额不足, 
			 * response_message=成功, seller_info={"customer_type":"CUSTOMER_NO"}, settle_currency=CNY, 
			 * trade_amount=1, trade_currency=CNY, trade_no=20170227100042000053163393, 
			 * trade_respcode=ACC3030007, response_code=0000, pay_currency=CNY,
			 *  customer_no=360080004001463150, bank_code=CCB, trade_class=DEFY, trade_status=CLOS, pay_tool=TRAN, card_type=DE, buyer_info={"customer_code":"360080004001463150","customer_type":"CUSTOMER_NO"}, trade_subject=代付, trade_pay_date=Mon Feb 27 15:03:13 CST 2017, trade_finish_date=Mon Feb 27 15:03:13 CST 2017, category_code=20EN0401}
			 */
			//验证数据
			Map<String,String> map = demoUtil.verifySingReturnData(responseText);
			if(map==null){
				throw new Exception("验签失败");
			} else {
				if("0000".equals(map.get("response_code"))){
					if("FINI".equals(map.get("trade_status"))){
						request.setRespCode("000");
						request.receiveAndPaySingle.setRespCode("000");
						request.receivePayRes = "0";
						request.respDesc="交易成功";
						rp.errorMsg = "交易成功";
						return true;
					} else if("CLOS".equals(map.get("trade_status"))){
						request.setRespCode("-1");
						request.receiveAndPaySingle.setRespCode("000");
						request.receivePayRes = "-1";
						request.respDesc="交易失败";
						rp.errorMsg = request.respDesc;
						return false;
					}
				} else {
					throw new Exception(map.get("response_message"));
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setRespCode("-1");
			rp.setRespCode(request.respCode);
			request.receivePayRes = "-1";
			request.respDesc=e.getMessage();
			rp.errorMsg = request.respDesc;
			throw e;
		}
    }
}
