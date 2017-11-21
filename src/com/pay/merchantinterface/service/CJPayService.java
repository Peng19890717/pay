package com.pay.merchantinterface.service;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.DataTransUtil;
import util.PayUtil;
import util.SMSUtil;
import util.Tools;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.merchantinterface.dao.PayRealNameAuth;
import com.pay.merchantinterface.dao.PayRealNameAuthDAO;
import com.pay.merchantinterface.dao.PayReceiveAndPaySign;
import com.pay.merchantinterface.dao.PayReceiveAndPaySignDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.pay.refund.dao.PayRefund;
import com.third.cj.CjSignHelper;
import com.third.cj.CjSignHelper.VerifyResult;
import com.third.cj.MD5;
/**
 * 畅捷接口服务类
 * @author Administrator
 *
 */
public class CJPayService {
	private static final Log log = LogFactory.getLog(CJPayService.class);
    private static String charset = "UTF-8";
    public static Map<String, String> CJ_BANK_MAP = new HashMap<String, String>();
    static int interval = 24;
    static int [] AUTH_TIME_FREQUENCY = new int[]{5,8,11,15,20};//认证查询时间间隔
    static int [] TRAN_FREQUENCY = new int[]{1,1,2,3,5,10};//认证查询时间间隔
    public static int [] CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY = new int[]{3,3,3,5,10,20,30,60,300,600,3600,7200};//代收付畅捷通道，查询时间频率，单位：秒
    static int RECEIVE_AND_PAY_SIGN_TIME = 365;
    //0借记卡/1贷记卡/2准贷记卡/3预付卡,00=银行卡，01=存折，02=信用卡，03-公司账号
    static Map <String, String>ACC_TYPE_MAP = new HashMap<String, String>();
    //01身份证,02军官证,03护照,04回乡证,05台胞证,06警官证,07士兵证,99其他
    //0：身份证,1: 户口簿，2：护照,3.军官证,4.士兵证，5. 港澳居民来往内地通行证,6. 台湾同胞来往内地通行证,7. 临时身份证,8. 外国人居留证,9. 警官证, X.其他证件
    static Map <String, String>CRET_TYPE_MAP = new HashMap<String, String>();
    //0代收，1代付，畅捷：G10001=实时收款、G10002=实时付款;
    static Map <String, String>RECEIVE_PAY_TYPE_SINGLE = new HashMap<String, String>();
    static Map <String, String>RECEIVE_PAY_TYPE_BATCH = new HashMap<String, String>();
    static {
    	init();
    	Timer timer = new Timer();
    	timer.schedule(new UpdateCJBankListTask(),0,interval*60*60*1000);
    	ACC_TYPE_MAP.put("0", "00");
    	ACC_TYPE_MAP.put("1", "02");
    	
    	CRET_TYPE_MAP.put("01","0");
    	CRET_TYPE_MAP.put("02","3");
    	CRET_TYPE_MAP.put("03","2");
    	CRET_TYPE_MAP.put("04","");
    	CRET_TYPE_MAP.put("05","6");
    	CRET_TYPE_MAP.put("06","9");
    	CRET_TYPE_MAP.put("07","4");
    	CRET_TYPE_MAP.put("99","X");
    	
    	RECEIVE_PAY_TYPE_SINGLE.put("0","G10001");
    	RECEIVE_PAY_TYPE_SINGLE.put("1","G10002");
    	RECEIVE_PAY_TYPE_BATCH.put("0","G10003");
    	RECEIVE_PAY_TYPE_BATCH.put("1","G10004");
    }
    public static void init(){
    	try {interval = Integer.parseInt(PayConstant.PAY_CONFIG.get("CJ_UPDATE_BANK_TIME"));} catch (Exception e) {}
    	try {
    		String [] tf = PayConstant.PAY_CONFIG.get("REAL_NAME_AUTH_TIME_FREQUENCY").replaceAll("，",",").split(",");
    		AUTH_TIME_FREQUENCY = new int[tf.length];
    		for(int i = 0; i<AUTH_TIME_FREQUENCY.length; i++)AUTH_TIME_FREQUENCY[i] = Integer.parseInt(tf[i]);
    	} catch (Exception e) {}
    	try {
    		String [] tf = PayConstant.PAY_CONFIG.get("CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY").replaceAll("，",",").split(",");
    		CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY = new int[tf.length];
    		for(int i = 0; i<CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++)CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i] = Integer.parseInt(tf[i]);
    	} catch (Exception e) {}
    	try {
    		RECEIVE_AND_PAY_SIGN_TIME = Integer.parseInt(PayConstant.PAY_CONFIG.get("RECEIVE_AND_PAY_SIGN_TIME"));
		} catch (Exception e) {}
    }
    static String yesterday = null;
    /**
     * 清理过期签约标识，每天运行一次
     * @return
     */
    public static boolean isClearProtocolSign(){
    	String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
    	if(yesterday == null){
    		yesterday = new SimpleDateFormat("yyyyMMdd").format(new Date());
    		return true;
    	} else {
    		if(yesterday.equals(today))return false;
    		else {
    			yesterday = new SimpleDateFormat("yyyyMMdd").format(new Date());
    			return true;
    		}
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
		 try {
		 	request.setCharacterEncoding("utf-8");
		 	payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ"); // 支付渠道
		 	payOrder.bankcod = request.getParameter("banks"); // 银行代码
		 	payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		 	Map<String, String> origMap = new HashMap<String, String>();
	        origMap.put("version", PayConstant.PAY_CONFIG.get("cj_version"));//版本号。
	        origMap.put("partner_id", PayConstant.PAY_CONFIG.get("cj_mchnt_id")); // 畅捷支付分配的商户号
	        origMap.put("_input_charset", charset);//数据编码。
	        origMap.put("is_anonymous", "Y");//若该值为Y，表示该笔订单的用户不需要是畅捷支付的用户。
	        origMap.put("bank_code", payOrder.bankcod);// 银行代码
	        origMap.put("out_trade_no",payOrder.payordno==null?"":payOrder.payordno);//订单号。
	        origMap.put("pay_method", "1");// 直连网银，不调用畅捷的收营台。
	        origMap.put("pay_type", payOrder.bankCardType.equals("0")?"C,DC":payOrder.bankCardType.equals("1")?"C,CC":"");// 支付对私，储蓄卡/信用卡。
	        origMap.put("service", "cjt_create_instant_trade");//直连网银服务代码。
	        try{
	        	DecimalFormat df  = new DecimalFormat("###.00");
	        	String trade_amount="";
	        	if(Double.parseDouble(payOrder.txamt.toString())/100<1){
	        		trade_amount="0"+df.format(Double.parseDouble(payOrder.txamt.toString())/100) ;
	        	}else{
	        		trade_amount=df.format(Double.parseDouble(payOrder.txamt.toString())/100);
	        	}
	        	origMap.put("trade_amount", trade_amount);// 金额
	        }catch(Exception e){throw new Exception("支付金额错误");};
	        origMap.put("return_url",  PayConstant.PAY_CONFIG.get("cj_pay_notify_url"));
	        origMap.put("notify_url",  PayConstant.PAY_CONFIG.get("cj_pay_notify_url"));
	        // 待请求参数数组
	        Map<String, String> sPara = buildRequestPara(origMap, "RSA", PayConstant.PAY_CONFIG.get("cj_merchant_private_key"), charset);
	        request.setAttribute("_input_charset", sPara.get("_input_charset"));
	        request.setAttribute("bank_code", sPara.get("bank_code"));
	        request.setAttribute("is_anonymous", sPara.get("is_anonymous"));
	        request.setAttribute("return_url", sPara.get("return_url"));
	        request.setAttribute("notify_url", sPara.get("notify_url"));
	        request.setAttribute("out_trade_no", sPara.get("out_trade_no"));
	        request.setAttribute("partner_id", sPara.get("partner_id"));
	        request.setAttribute("pay_method", sPara.get("pay_method"));
	        request.setAttribute("pay_type", sPara.get("pay_type"));
	        request.setAttribute("service", sPara.get("service"));
	        request.setAttribute("sign", sPara.get("sign"));
	        request.setAttribute("sign_type", sPara.get("sign_type"));
	        request.setAttribute("trade_amount", sPara.get("trade_amount"));
	        request.setAttribute("version", sPara.get("version"));
	        log.info("畅捷网关请求报文:"+sPara.toString());
	        new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
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
		 try {
		 	request.setCharacterEncoding("utf-8");
		 	payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ"); // 支付渠道
		 	payOrder.bankcod = payRequest.bankId; // 银行代码
		 	payOrder.bankCardType = payRequest.accountType;
		 	Map<String, String> origMap = new HashMap<String, String>();
	        origMap.put("version", PayConstant.PAY_CONFIG.get("cj_version"));//版本号。
	        origMap.put("partner_id", PayConstant.PAY_CONFIG.get("cj_mchnt_id")); // 畅捷支付分配的商户号
	        origMap.put("_input_charset", charset);//数据编码。
	        origMap.put("is_anonymous", "Y");//若该值为Y，表示该笔订单的用户不需要是畅捷支付的用户。
	        origMap.put("bank_code", payOrder.bankcod);// 银行代码
	        origMap.put("out_trade_no",payOrder.payordno==null?"":payOrder.payordno);//订单号。
	        origMap.put("pay_method", "1");// 直连网银，不调用畅捷的收营台。
	        origMap.put("pay_type", payOrder.bankCardType.equals("0")?"C,DC":payOrder.bankCardType.equals("1")?"C,CC":"");// 支付对私，储蓄卡/信用卡。
	        origMap.put("service", "cjt_create_instant_trade");//直连网银服务代码。
	        try{
	        	DecimalFormat df  = new DecimalFormat("###.00");
	        	String trade_amount="";
	        	if(Double.parseDouble(payOrder.txamt.toString())/100<1){
	        		trade_amount="0"+df.format(Double.parseDouble(payOrder.txamt.toString())/100) ;
	        	}else{
	        		trade_amount=df.format(Double.parseDouble(payOrder.txamt.toString())/100);
	        	}
	        	origMap.put("trade_amount", trade_amount);// 金额
	        }catch(Exception e){throw new Exception("支付金额错误");};
	        origMap.put("return_url",  PayConstant.PAY_CONFIG.get("cj_pay_notify_url"));
	        origMap.put("notify_url",  PayConstant.PAY_CONFIG.get("cj_pay_notify_url"));
	        // 待请求参数数组
	        Map<String, String> sPara = buildRequestPara(origMap, "RSA", PayConstant.PAY_CONFIG.get("cj_merchant_private_key"), charset);
	        log.info(createLinkString(sPara, true));
	        request.setAttribute("_input_charset", sPara.get("_input_charset"));
	        request.setAttribute("bank_code", sPara.get("bank_code"));
	        request.setAttribute("is_anonymous", sPara.get("is_anonymous"));
	        request.setAttribute("return_url", sPara.get("return_url"));
	        request.setAttribute("notify_url", sPara.get("notify_url"));
	        request.setAttribute("out_trade_no", sPara.get("out_trade_no"));
	        request.setAttribute("partner_id", sPara.get("partner_id"));
	        request.setAttribute("pay_method", sPara.get("pay_method"));
	        request.setAttribute("pay_type", sPara.get("pay_type"));
	        request.setAttribute("service", sPara.get("service"));
	        request.setAttribute("sign", sPara.get("sign"));
	        request.setAttribute("sign_type", sPara.get("sign_type"));
	        request.setAttribute("trade_amount", sPara.get("trade_amount"));
	        request.setAttribute("version", sPara.get("version"));
	        log.info("畅捷网关请求报文:"+sPara.toString());
	        new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
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
	    Map<String, String> origMap = new HashMap<String, String>();
        origMap.put("version", PayConstant.PAY_CONFIG.get("cj_version"));
        origMap.put("partner_id", PayConstant.PAY_CONFIG.get("cj_mchnt_id")); // 畅捷支付分配的商户号
        origMap.put("_input_charset", charset);// 字符集
        origMap.put("service", "cjt_quick_payment");// 支付的接口名
        origMap.put("out_trade_no", payOrder.payordno);// 订单号\
        try{
        	DecimalFormat df  = new DecimalFormat("###.00");
        	String trade_amount="";
        	if(Double.parseDouble(payRequest.merchantOrderAmt)/100<1){
        		trade_amount="0"+df.format(Double.parseDouble(payRequest.merchantOrderAmt)/100) ;
        	} else trade_amount=df.format(Double.parseDouble(payRequest.merchantOrderAmt)/100);
        	origMap.put("trade_amount", trade_amount);// 金额
        }catch(Exception e){throw new Exception("快捷支付金额错误");};
        PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);//通过卡号获取卡类信息。
        origMap.put("card_type",cardBin.cardType.equals("0")?"DC":(cardBin.cardType.equals("1")?"CC":""));// 卡类型
        if("1".equals(cardBin.cardType)){
        	origMap.put("expiry_date", encrypt(payRequest.validPeriod,  PayConstant.PAY_CONFIG.get("cj_merchant_public_key"), charset));//信用卡日期。
        	origMap.put("cvv2", encrypt(payRequest.cvv2,  PayConstant.PAY_CONFIG.get("cj_merchant_public_key"), charset));//信用卡日期。
        }
        origMap.put("pay_type", "C");// 对公对私
        origMap.put("bank_code",cardBin.bankCode );//正式环境银行编码。
        origMap.put("payer_name", encrypt(payRequest.userName,  PayConstant.PAY_CONFIG.get("cj_merchant_public_key"), charset));
        origMap.put("payer_card_no", encrypt(payRequest.cardNo,  PayConstant.PAY_CONFIG.get("cj_merchant_public_key"), charset));
        origMap.put("id_number", encrypt(payRequest.credentialNo,  PayConstant.PAY_CONFIG.get("cj_merchant_public_key"), charset));
        origMap.put("phone_number", encrypt(payRequest.userMobileNo,  PayConstant.PAY_CONFIG.get("cj_merchant_public_key"), charset));
        origMap.put("notify_url", PayConstant.PAY_CONFIG.get("cj_pay_notify_url") );// 后台通知的url
        //验证是否支持当前付款银行快捷支付。
        String ChannelBank="";
        ChannelBank=CJ_BANK_MAP.get(origMap.get("bank_code")+"_"+origMap.get("card_type")+"_"+origMap.get("pay_type")+"_QPAY");
        if(ChannelBank==null||ChannelBank.equals("")){
        	payOrder.bankerror="暂时不支持该银行快捷支付(CJ)";
        	return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" " +
					"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
					"respCode=\"-1\" respDesc=\""+payOrder.bankerror+"\" " +
					"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
        } else {
        	String resultString = gatewayPost(origMap, charset,  PayConstant.PAY_CONFIG.get("cj_merchant_private_key"));
			log.info("畅捷快捷支付post回的数据==========>" + resultString);
			com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(resultString);
			if(!"0".equals(jsonObject.getString("authenticate_status"))
					||(jsonObject.getString("err_msg")!=null&&jsonObject.getString("err_msg").length()>0)){
				payOrder.bankerror=jsonObject.getString("err_msg");
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" " +
						"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
						"respCode=\"-1\" respDesc=\""+jsonObject.getString("err_msg")+"\" " +
						"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			} else return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" " +
						"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
						"respCode=\""+("0".equals(jsonObject.getString("authenticate_status"))?"000":"-1")+"\" respDesc=\"\" " +
						"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
        }
	}
	/**
	 * 快捷确认
	 * @param payRequest
	 * @return
	 */
    public String certPayConfirm(PayRequest payRequest) {
        Map<String, String> origMap = new HashMap<String, String>();
        origMap.put("version", PayConstant.PAY_CONFIG.get("cj_version"));
        origMap.put("partner_id", PayConstant.PAY_CONFIG.get("cj_mchnt_id")); // 畅捷支付分配的商户号
        origMap.put("_input_charset", charset);// 字符集
        origMap.put("service", "cjt_quick_payment_confirm");// 支付的接口名
        origMap.put("out_trade_no",payRequest.payOrder.payordno);// 订单号
        origMap.put("verification_code", payRequest.checkCode);// 短信验证码
        return gatewayPost(origMap, charset,PayConstant.PAY_CONFIG.get("cj_merchant_private_key"));
    }
    /**
     * 直连网银支付查询接口。
     * @param payOrder
     */
    public void channelQuery(PayOrder payOrder) {
    	try{
	        Map<String, String> origMap = new HashMap<String, String>();
	        origMap.put("service", "cjt_query_trade");// 支付查询的接口名
	        origMap.put("version", PayConstant.PAY_CONFIG.get("cj_version"));
	        origMap.put("partner_id", PayConstant.PAY_CONFIG.get("cj_mchnt_id")); // 畅捷支付分配的商户号
	        origMap.put("_input_charset", charset);// 字符集
	        origMap.put("outer_trade_no", payOrder.payordno);// 支付订单号。
	        origMap.put("trade_type", "INSTANT");//交易的类型类型包括 即时到账(INSTANT)，担保交易(ENSURE)， 退款(REFUND),提现（WITHDRAWAL）,定金下定（PREPAY）
	        String res = gatewayPost(origMap, charset,  PayConstant.PAY_CONFIG.get("cj_merchant_private_key"));
	        log.info("畅捷订单查询接口返回数据:"+res);
		    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(res);
		    payOrder.actdat = new Date();
	        if("TRADE_SUCCESS".equals(jsonObject.getString("trade_status"))||
	        		"TRANSFER_SUCCESS".equals(jsonObject.getString("trade_status"))||"TRADE_FINISHED".equals(jsonObject.getString("trade_status"))){
	        	payOrder.ordstatus="01";//支付成功
	        	payOrder.bankjrnno = jsonObject.getString("outer_trade_no");
	        } else if("WAIT_PAY".equals(jsonObject.getString("trade_status"))
	        		||"WAIT_BUYER_PAY".equals(jsonObject.getString("trade_status"))){
	        	
	        } else {
	        	payOrder.ordstatus="02";//支付失败
	        	payOrder.bankjrnno = jsonObject.getString("outer_trade_no");
	        	payOrder.bankerror = jsonObject.getString("trade_status"); 
	        }
    	} catch (Exception e){
			e.printStackTrace();
		}
    }
    /**
     * 直连网银-退款接口。
     * @param payRefund
     */
    public void refund(PayRefund payRefund){
		try{
			 Map<String, String> origMap = new HashMap<String, String>();
		        origMap.put("service", "cjt_create_refund");// 退款的接口名
		        origMap.put("version", PayConstant.PAY_CONFIG.get("cj_version"));
		        origMap.put("partner_id", PayConstant.PAY_CONFIG.get("cj_mchnt_id")); // 畅捷支付分配的商户号
		        origMap.put("_input_charset", charset);// 字符集
		        origMap.put("outer_trade_no", payRefund.refordno);// 退款请求订单号。
		        origMap.put("orig_outer_trade_no", payRefund.oripayordno);//要退款的支付订单号。
		        try{
		        	DecimalFormat df  = new DecimalFormat("###.00");
		        	String refund_amount="";
		        	if(Double.parseDouble(payRefund.rfamt.toString())/100<1){
		        		refund_amount="0"+df.format(Double.parseDouble(payRefund.rfamt.toString())/100) ;
		        	}else{
		        		refund_amount=df.format(Double.parseDouble(payRefund.rfamt.toString())/100);
		        	}
		        	 origMap.put("refund_amount", refund_amount);//退款金额。
		        }catch(Exception e){throw new Exception("退款金额错误");};
		        origMap.put("notify_url",PayConstant.PAY_CONFIG.get("cj_refund_notify_url"));//退款结果异步通知。
		        String res =gatewayPost(origMap, charset, PayConstant.PAY_CONFIG.get("cj_merchant_private_key"));
		        log.info("畅捷退款返回数据:"+res);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	 /**
     * 查询银行列表。
	 * @throws Exception 
     */
    public String QueryChannelBank() {
    	try {
	        Map<String, String> origMap = new HashMap<String, String>();
	        origMap.put("version", PayConstant.PAY_CONFIG.get("cj_version"));
	        origMap.put("partner_id", PayConstant.PAY_CONFIG.get("cj_mchnt_id")); // 畅捷支付分配的商户号
	        origMap.put("_input_charset", charset);// 字符集
	        origMap.put("service", "cjt_get_paychannel");// 支付的接口名
	        origMap.put("product_code", "20201");//
	        Map<String, String> sPara = buildRequestPara(origMap, "RSA", PayConstant.PAY_CONFIG.get("cj_merchant_private_key"), charset);
	        Iterator it = sPara.keySet().iterator();
	        String paras = "";
	        while(it.hasNext()){
	        	String key = (String) it.next();
	        	String para = sPara.get(key);
	        	paras = paras+key+"="+java.net.URLEncoder.encode(para)+"&";
	        }
	        if(paras.length()>0)paras = paras.substring(0,paras.length()-1);
	        log.info("畅捷请求银行查询=========\n"+paras);
	        String res = new String(new util.DataTransUtil().
	        		doPost(PayConstant.PAY_CONFIG.get("cj_Pay_url"),paras.getBytes("utf-8")),"utf-8");
	        log.info("畅捷请求银行响应=========\n"+res);
	        return res;
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return "";
    }
    private static String encrypt(String src, String publicKey, String charset) {
        try {
            return Base64.encodeBase64String(RSA.encryptByPublicKey(src.getBytes(charset), publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 向测试服务器发送post请求
     * 
     * @param origMap
     *            参数map
     * @param charset
     *            编码字符集
     * @param MERCHANT_PRIVATE_KEY
     *            私钥
     * @throws Exception 
     */
    public String gatewayPost(Map<String, String> origMap, String charset, String MERCHANT_PRIVATE_KEY) {
        try {
            log.info("畅捷请求URL*****"+PayConstant.PAY_CONFIG.get("cj_Pay_url"));
            String resultString = buildRequest(origMap, "RSA", MERCHANT_PRIVATE_KEY, charset, PayConstant.PAY_CONFIG.get("cj_Pay_url"));
            log.info("畅捷响应*****"+resultString);
            return resultString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取钱包的处理结果 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值 如：buildRequest("", "",sParaTemp)
     *
     * @param strParaFileName
     *            文件类型的参数名
     * @param strFilePath
     *            文件路径
     * @param sParaTemp
     *            请求参数数组
     * @return 钱包处理结果
     * @throws Exception
     */
    public  String buildRequest(Map<String, String> sParaTemp, String signType, String key, String inputCharset, String gatewayUrl) throws Exception {
        // 待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp, signType, key, inputCharset);
//        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
//        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
//        // 设置编码集
//        request.setCharset(inputCharset);
//        request.setMethod(HttpRequest.METHOD_POST);
//        request.setParameters(generatNameValuePair(createLinkRequestParas(sPara), inputCharset));
//        request.setUrl("http://localhost:8080/get-request-all.jsp");
//        HttpResponse response = httpProtocolHandler.execute(request, null, null);
//        if (response == null) {
//            return null;
//        }
//        return response.getStringResult();
        Iterator it = sPara.keySet().iterator();
        String paras = "";
        while(it.hasNext()){
        	key = (String) it.next();
        	String para = sPara.get(key);
        	paras = paras+key+"="+java.net.URLEncoder.encode(java.net.URLEncoder.encode(para))+"&";
        }
        if(paras.length()>0)paras = paras.substring(0,paras.length()-1);
        
        log.info("畅捷请求银行查询=========\n"+paras);
        String res = new String(new util.DataTransUtil().
        		doPost(PayConstant.PAY_CONFIG.get("cj_Pay_url"),paras.getBytes("utf-8")),"utf-8");
        log.info("畅捷请求银行响应=========\n"+res);
        return res;
    }
    /**
     * 生成要请求给钱包的参数数组
     *
     * @param sParaTemp
     *            请求前的参数数组
     * @return 要请求的参数数组
     */
    public  static Map<String, String> buildRequestPara(Map<String, String> sParaTemp, String signType, String key, String inputCharset) throws Exception {
        // 除去数组中的空值和签名参数
        Map<String, String> sPara = paraFilter(sParaTemp);
        // 生成签名结果
        String mysign = "";
        if ("MD5".equalsIgnoreCase(signType)) {
            mysign = buildRequestByMD5(sPara, key, inputCharset);
        } else if ("RSA".equalsIgnoreCase(signType)) {
            mysign = buildRequestByRSA(sPara, key, inputCharset);
        }
        // 签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", signType);

        return sPara;
    }
    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @param encode
     *            是否需要urlEncode
     * @return 拼接后字符串
     */
    public  static String createLinkString(Map<String, String> params, boolean encode) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        String charset = params.get("_input_charset");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (encode) {
                try {
                    value = URLEncoder.encode(value, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }
    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     *            需要排序并参与字符拼接的参数组
     * @param encode
     *            是否需要urlEncode
     * @return 拼接后字符串
     */
    public static Map<String, String> createLinkRequestParas(Map<String, String> params) {
        Map<String, String> encodeParamsValueMap = new HashMap<String, String>();
        List<String> keys = new ArrayList<String>(params.keySet());
        String charset = params.get("_input_charset");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value;
            try {
                value = URLEncoder.encode(params.get(key), charset);
                encodeParamsValueMap.put(key, value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return encodeParamsValueMap;
    }
    /**
     * MAP类型数组转换成NameValuePair类型
     *
     * @param properties
     *            MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties, String charset) throws Exception {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            // nameValuePair[i++] = new NameValuePair(entry.getKey(), URLEncoder.encode(entry.getValue(),charset));
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }
    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray
     *            签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }
    /**
     * 生成MD5签名结果
     *
     * @param sPara
     *            要签名的数组
     * @return 签名结果字符串
     */
    public static String buildRequestByMD5(Map<String, String> sPara, String key, String inputCharset) throws Exception {
        String prestr = createLinkString(sPara, false); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        mysign = MD5.sign(prestr, key, inputCharset);
        return mysign;
    }

    /**
     * 生成RSA签名结果
     *
     * @param sPara
     *            要签名的数组
     * @return 签名结果字符串
     */
    public static String buildRequestByRSA(Map<String, String> sPara, String privateKey, String inputCharset) throws Exception {
        String prestr = createLinkString(sPara, false); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        mysign = RSA.sign(prestr, privateKey, inputCharset);
        return mysign;
    }
    /**
     * 实名认证
     * @param payRequest
     * @throws Exception
     */
    public void realNameAuth(PayRequest payRequest) throws Exception{
    	try {
    		payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    		String xmlDtl = "";
    		Map<String,PayReceiveAndPay> authMapBak = new HashMap<String,PayReceiveAndPay>();
    		for(int i=0; i<payRequest.receivePayList.size(); i++){
    			PayReceiveAndPay rp = payRequest.receivePayList.get(i);
    			rp.way="0";
    			rp.channelId=PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ");
    			if(rp.respCode.length()>0)continue;//已经成功认证过,或者值验证失败的
    			authMapBak.put(payRequest.merchantId+"_"+rp.sn, rp);
    			xmlDtl = xmlDtl + "<DTL><SN>"+ payRequest.merchantId+"_"+rp.sn+"</SN>"+
    				"<BANK_GENERAL_NAME>"+rp.bankGeneralName+"</BANK_GENERAL_NAME>" +
    				"<ACCOUNT_TYPE>"+(ACC_TYPE_MAP.get(rp.accType)==null?"00":ACC_TYPE_MAP.get(rp.accType))+"</ACCOUNT_TYPE>" +
    				"<ACCOUNT_NO>"+rp.accNo+"</ACCOUNT_NO>" +
    				"<ACCOUNT_NAME>"+rp.accName+"</ACCOUNT_NAME>" +
    				"<ID_TYPE>"+CRET_TYPE_MAP.get(rp.credentialType)+"</ID_TYPE>" +
    				"<ID>"+rp.credentialNo+"</ID>" +
    				"<TEL>"+rp.tel+"</TEL></DTL>";
    		}
    		if(xmlDtl.length()==0)return;
    		payRequest.authTranId = Tools.getUniqueIdentify();
    		//封装xml
    		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>" +
    				"<TRX_CODE>G60001</TRX_CODE><VERSION>01</VERSION>" +
    				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>" +
    				"<REQ_SN>"+ payRequest.authTranId+"</REQ_SN>" +
    				"<TIMESTAMP>"+payRequest.timestamp+"</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO><BODY><BATCH>" +
    				"<VALIDATE_MODE>V003</VALIDATE_MODE></BATCH>" +
    				"<TRANS_DETAILS>" + xmlDtl+ "</TRANS_DETAILS></BODY></MESSAGE>";
    		CjSignHelper singHelper = new CjSignHelper();
    		String signMsg = singHelper.signXml$Add(xml);
    		log.info("实名认证请求req======="+PayConstant.PAY_CONFIG.get("cj_receive_pay_url")+"=======\n"+signMsg);
    		//发送信息    		
    		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
    		log.info("实名认证响应resp==============\n"+res);
    		//验证签名
    		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
    		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
    		//解析响应
    		//<?xml version="1.0" encoding="UTF-8"?><MESSAGE><INFO><TRX_CODE>G60001</TRX_CODE><VERSION>01</VERSION><MERCHANT_ID>cp2016011227674</MERCHANT_ID><REQ_SN>ff4154572e814ab1a464421bb13303cf</REQ_SN><RET_CODE>0000</RET_CODE><ERR_MSG></ERR_MSG><TIMESTAMP>20160815135655</TIMESTAMP><SIGNED_MSG>MIIHdQYJKo...</SIGNED_MSG></INFO></MESSAGE>
    		Map <String,String>children = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","INFO"})[1];
    		if(!"0000".equals(children.get("RET_CODE")))throw new Exception("CJ渠道错误："+
    				(children.get("RET_CODE")==null?"未知":children.get("RET_CODE")+"("+children.get("ERR_MSG")+")"));
    		payRequest.setRespInfo("000");
    		PayRealNameAuthDAO payRealNameAuthDAO = new PayRealNameAuthDAO();
    		//保存认证结果
    		payRealNameAuthDAO.addPayRealNameAuthBatch(payRequest);
    		//开始查询认证结果（查询完毕后通知商户）
    		for(int i=0; i<AUTH_TIME_FREQUENCY.length; i++){
    			log.info("实名查询第"+(i+1)+"次==============间隔"+AUTH_TIME_FREQUENCY[i]+"秒");
    			if(authMapBak.size()==0)break;
    			try {Thread.sleep(AUTH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
    			List<PayReceiveAndPay> sList = realNameAuthQuery(payRequest,authMapBak,1);
    			for(int j=0; j<sList.size(); j++){
    				PayReceiveAndPay rb = sList.get(j);
    				if("000".equals(rb.respCode)){
    					PayReceiveAndPay rpTmp = authMapBak.get(rb.sn);
    					rpTmp.status = "1";
    					rpTmp.setRespInfo("000");
    					rpTmp.respDesc=rb.respDesc;
    					authMapBak.remove(rb.sn);
    				} else {
    					if(!"0001".equals(rb.respCode) && !"0002".equals(rb.respCode)){
    						PayReceiveAndPay rpTmp = authMapBak.get(rb.sn);
    						if(rpTmp==null)continue;
    						rpTmp.status = "2";
    						rpTmp.setRespCode("-1");
    						rpTmp.setRespDesc(rb.respDesc);
        					authMapBak.remove(rb.sn);
    					}
    				}
    			}
    		}
    		Iterator it = authMapBak.keySet().iterator();
    		while(it.hasNext()){
    			PayReceiveAndPay rpTmp = authMapBak.get(it.next());
    			rpTmp.status = "2";
    			rpTmp.setRespCode("-1");
    			rpTmp.setRespDesc("认证失败，未取到查询结果");
    		}
    		//更新认证结果
    		payRealNameAuthDAO.updatePayRealNameAuthBySearchChannel(payRequest.receivePayList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }
   /**
    * 
    * @param payRequest
    * @param map
    * @param flag 0:查询payRequest，1:查询map
    * @return
 * @throws Exception 
    */
    public List<PayReceiveAndPay> realNameAuthQuery(PayRequest request,Map<String,PayReceiveAndPay> map,int flag) throws Exception{
    	List<PayReceiveAndPay> searchList = new ArrayList<PayReceiveAndPay>();
    	List<PayReceiveAndPay> resultList = new ArrayList<PayReceiveAndPay>();
    	if(flag==0)for(int i=0; i<request.receivePayList.size(); i++)searchList.add(request.receivePayList.get(i));
    	else if(flag==1){
    		Iterator it = map.keySet().iterator();
    		while(it.hasNext())searchList.add(map.get(it.next()));
    	}
    	if(searchList.size()==0)return resultList;
		//封装xml
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>" +
				"<TRX_CODE>G60002</TRX_CODE><VERSION>01</VERSION>" +
				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>" +
				"<REQ_SN>"+Tools.getUniqueIdentify()+"</REQ_SN>" +
				"<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO><BODY><QRY_REQ_SN>"
					+ request.authTranId+"</QRY_REQ_SN></BODY></MESSAGE>";
		log.info("实名查询请求req==============\n"+xml);
		CjSignHelper singHelper = new CjSignHelper();
		String signMsg = singHelper.signXml$Add(xml);		
		//发送信息
		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
		log.info("实名查询响应resp==============\n"+res);
		// 验证签名
		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
		Node node = PayUtil.getXmlNode(res,new String []{"MESSAGE","BODY","TRANS_DETAILS"});
		NodeList nodeList = node.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++){
			NodeList nodeList1 = nodeList.item(i).getChildNodes();
			PayReceiveAndPay rp = new PayReceiveAndPay();
			for(int j=0; j<nodeList1.getLength(); j++){
				Node node1 = nodeList1.item(j);
				String value = node1.getTextContent().trim();
				if(node1.getNodeName().equals("SN"))rp.sn=value;
				else if(node1.getNodeName().equals("RET_CODE")){
					if("0000".equals(value))rp.setRespInfo("000");
					else rp.respCode=value;
				}
				else if(node1.getNodeName().equals("ERR_MSG"))rp.respDesc=value;
				else if(node1.getNodeName().equals("ACCOUNT_NO"))rp.accNo=value;
				else if(node1.getNodeName().equals("ACCOUNT_NAME"))rp.accName=value;
			}
			resultList.add(rp);
		}
		return resultList;
    }
    /**
     * 协议签约(收款使用)
     * @param payRequest
     * @param rnAuthList
     * @throws Exception
     */
    public void signProtocol(PayRequest payRequest,List<PayReceiveAndPay> rnAuthList) throws Exception{
    	try {
    		PayReceiveAndPaySignDAO payReceiveAndPaySignDAO = new PayReceiveAndPaySignDAO();
    		//清理过期的签约，过期的保存到备份表
    		if(isClearProtocolSign())payReceiveAndPaySignDAO.clearSignExpired();
    		payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    		String beginDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    		Calendar c = Calendar.getInstance();
    		c.add(Calendar.DATE,RECEIVE_AND_PAY_SIGN_TIME);
    		String endDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
    		String bussinessCode = "0".equals(payRequest.receivePayType)?PayConstant.PAY_CONFIG.get("cj_receive_business_code"):PayConstant.PAY_CONFIG.get("cj_pay_business_code");
    		for(int i=0; i<rnAuthList.size(); i++)rnAuthList.get(i).businessCode = bussinessCode;
    		//本地签约检查，根据账号和本渠道是否签约标识
    		Map<String,PayReceiveAndPaySign> signedSuccessMap = 
    			payReceiveAndPaySignDAO.getSignedSuccessRecord(rnAuthList,PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ"));
    		PayReceiveAndPaySign payReceiveAndPaySign = signedSuccessMap.get(rnAuthList.get(0).accNo);
    		if("ReceivePay".equals(payRequest.application)&&payReceiveAndPaySign!=null){
    			PayReceiveAndPay payReceiveAndPay = rnAuthList.get(0);
    			payReceiveAndPay.setRespInfo("000");
    			payReceiveAndPay.protocolNo = payReceiveAndPaySign.protocolNo;
    			return;
    		}
    		//去除已经签约成功的记录
    		String xmlDtl = "";
    		Map<String,PayReceiveAndPay> signMapBak = new HashMap<String,PayReceiveAndPay>();
    		for(int i=0; i<rnAuthList.size(); i++){
    			PayReceiveAndPay rp = rnAuthList.get(i);
    			rp.signProtocolChannel=PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ");
    			if(payReceiveAndPaySign!=null){
    				rp.setRespInfo("000");
    				rp.protocolNo = payReceiveAndPaySign.protocolNo;
    				continue;
    			}
    			if(rp.respCode.length()>0&&!"000".equals(rp.respCode))continue;//已经成功认证过,或者值验证失败的
    			rp.protocolNo=Tools.getUniqueIdentify();
    			signMapBak.put(rp.sn, rp);
    			xmlDtl = xmlDtl + "<DTL><SN>"+rp.sn+"</SN>"+
    				"<PROTOCOL_NO>"+rp.protocolNo+"</PROTOCOL_NO>" +
    				"<BANK_NAME>"+rp.bankName+"</BANK_NAME>" +
    				"<BANK_CODE>"+rp.bankId+"</BANK_CODE>" +
    				"<ACCOUNT_TYPE>"+(ACC_TYPE_MAP.get(rp.accType)==null?"00":ACC_TYPE_MAP.get(rp.accType))+"</ACCOUNT_TYPE>" +
    				"<ACCOUNT_NO>"+rp.accNo+"</ACCOUNT_NO>" +
    				"<ACCOUNT_NAME>"+rp.accName+"</ACCOUNT_NAME>" +
    				"<ID_TYPE>"+CRET_TYPE_MAP.get(rp.credentialType)+"</ID_TYPE>" +
    				"<ID>"+rp.credentialNo+"</ID>" +
    				"<BEGIN_DATE>"+beginDate+"</BEGIN_DATE>"+
    				"<END_DATE>"+endDate+"</END_DATE>" +
    				"<TEL>"+rp.tel+"</TEL></DTL>";
    		}
    		if(xmlDtl.length()==0)return;
    		payRequest.signTranId = Tools.getUniqueIdentify();
    		//封装xml
    		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>" +
    				"<TRX_CODE>G60003</TRX_CODE><VERSION>01</VERSION>" +
    				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>" +
    				"<REQ_SN>"+ payRequest.signTranId+"</REQ_SN>" +
    				"<TIMESTAMP>"+payRequest.timestamp+"</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO><BODY>" +
    				"<BATCH><CORP_ACCT_NO>"+PayConstant.PAY_CONFIG.get("RECEIVE_PAY_CORP_ACC_NO_ZX")+"</CORP_ACCT_NO><BUSINESS_CODE>"
    				+ bussinessCode +"</BUSINESS_CODE><ALTER_TYPE>0</ALTER_TYPE><PROTOCOL_TYPE>0</PROTOCOL_TYPE></BATCH>"+
    				"<TRANS_DETAILS>" + xmlDtl+ "</TRANS_DETAILS></BODY></MESSAGE>";
    		CjSignHelper singHelper = new CjSignHelper();
    		String signMsg = singHelper.signXml$Add(xml);
    		log.info("签约请求req==============\n"+signMsg);
    		//发送信息
    		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
    		log.info("签约响应resp==============\n"+res);
    		//验证签名
    		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
    		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
    		//解析响应
    		//<?xml version="1.0" encoding="UTF-8"?><MESSAGE><INFO><TRX_CODE>G60003</TRX_CODE><VERSION>01</VERSION><MERCHANT_ID>cp2016011227674</MERCHANT_ID><REQ_SN>1471486474425</REQ_SN><RET_CODE>0000</RET_CODE><ERR_MSG></ERR_MSG><TIMESTAMP>20160818101446</TIMESTAMP><SIGNED_MSG>MIIHdQ...</SIGNED_MSG></INFO></MESSAGE>
    		Map <String,String>children = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","INFO"})[1];
    		if(!"0000".equals(children.get("RET_CODE")))throw new Exception("CJ渠道错误："+
    				(children.get("RET_CODE")==null?"未知":children.get("RET_CODE")+"("+children.get("ERR_MSG")+")"));
    		//保存签约结果，未查询之前，已经成功的和验证失败的不插入
    		payReceiveAndPaySignDAO.addPayReceiveAndPaySignBatch(rnAuthList);
    		//开始查询认证结果（查询完毕后通知商户）
    		for(int i=0; i<AUTH_TIME_FREQUENCY.length; i++){
    			if(signMapBak.size()==0)break;
    			try {Thread.sleep(AUTH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
    			List<PayReceiveAndPay> sList = signProtocolQuery(payRequest,signMapBak,1);
    			for(int j=0; j<sList.size(); j++){
    				PayReceiveAndPay rb = sList.get(j);
    				if("000".equals(rb.respCode)){
    					PayReceiveAndPay rbTmp = signMapBak.get(rb.sn);
    					rbTmp.status="1";
    					rbTmp.setRespInfo("000");
    					rbTmp.respDesc=rb.respDesc;
    					signMapBak.remove(rb.sn);
    				} else {
    					if(!"0001".equals(rb.respCode) && !"0002".equals(rb.respCode)){
    						PayReceiveAndPay rpTmp = signMapBak.get(rb.sn);
    						if(rpTmp==null)continue;
    						rpTmp.status="2";
        					rpTmp.setRespCode("-1");
        					rpTmp.setRespDesc(rb.respDesc);
        					signMapBak.remove(rb.sn);
    					}
    				}
    			}
    		}
    		Iterator it = signMapBak.keySet().iterator();
    		while(it.hasNext()){
    			PayReceiveAndPay rpTmp = signMapBak.get(it.next());
    			rpTmp.status = "2";
    			rpTmp.setRespCode("-1");
    			rpTmp.setRespDesc("签约失败，未取到查询结果");
    		}
    		//更新签约结果
    		payReceiveAndPaySignDAO.updatePayReceiveAndPaySignBySearchChannel(rnAuthList);
    	} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }
    /**
    * 协议签约查询
    * @param request
    * @param map
    * @param flag
    * @return
    * @throws Exception
    */
    public List<PayReceiveAndPay> signProtocolQuery(PayRequest request,Map<String,PayReceiveAndPay> map,int flag) throws Exception{
    	List<PayReceiveAndPay> searchList = new ArrayList<PayReceiveAndPay>();
    	List<PayReceiveAndPay> resultList = new ArrayList<PayReceiveAndPay>();
    	if(flag==0)for(int i=0; i<request.receivePayList.size(); i++)searchList.add(request.receivePayList.get(i));
    	else if(flag==1){
    		Iterator it = map.keySet().iterator();
    		while(it.hasNext())searchList.add(map.get(it.next()));
    	}
    	if(searchList.size()==0)return resultList;
		//封装xml
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>" +
				"<TRX_CODE>G60004</TRX_CODE><VERSION>01</VERSION>" +
				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>" +
				"<REQ_SN>"+Tools.getUniqueIdentify()+"</REQ_SN>" +
				"<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO><BODY><QRY_REQ_SN>"
					+ request.signTranId+"</QRY_REQ_SN></BODY></MESSAGE>";
		log.info("签约查询请求req==============\n"+xml);
		CjSignHelper singHelper = new CjSignHelper();
		String signMsg = singHelper.signXml$Add(xml);		
		//发送信息
		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
		log.info("签约查询响应resp==============\n"+res);
		// 验证签名
		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
		Node node = PayUtil.getXmlNode(res,new String []{"MESSAGE","BODY","TRANS_DETAILS"});
		NodeList nodeList = node.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++){
			NodeList nodeList1 = nodeList.item(i).getChildNodes();
			PayReceiveAndPay rp = new PayReceiveAndPay();
			for(int j=0; j<nodeList1.getLength(); j++){
				Node node1 = nodeList1.item(j);
				String value = node1.getTextContent().trim();
				if(node1.getNodeName().equals("SN"))rp.sn=value;
				else if(node1.getNodeName().equals("RET_CODE")){
					if("0000".equals(value))rp.setRespInfo("000");
					else rp.respCode=value;
				}
				else if(node1.getNodeName().equals("ERR_MSG"))rp.respDesc=value;
				else if(node1.getNodeName().equals("ACCOUNT_NO"))rp.accNo=value;
				else if(node1.getNodeName().equals("ACCOUNT_NAME"))rp.accName=value;
			}
			resultList.add(rp);
		}
		return resultList;
    }
    /**
     * 实名认证，内部使用
     * @param payRequest
     * @param rnAuthList
     * @throws Exception
     */
    public void realNameAuthInner(PayRequest payRequest,List<PayReceiveAndPay> rnAuthList) throws Exception{
    	try {
    		payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    		String xmlDtl = "";
    		Map<String,PayRealNameAuth> authedMap = new PayRealNameAuthDAO().getRealNameAuthedSuccessList(payRequest,rnAuthList);
    		Map<String,PayReceiveAndPay> authMapBak = new HashMap<String,PayReceiveAndPay>();
    		if("ReceivePay".equals(payRequest.application)&&authedMap.get(rnAuthList.get(0).accNo)!=null){
    			rnAuthList.get(0).setRespInfo("000");
    			return;
    		}
    		for(int i=0; i<rnAuthList.size(); i++){
    			PayReceiveAndPay rp = rnAuthList.get(i);
    			if(authedMap.get(rp.accNo)!=null)rp.setRespInfo("000");
    			if((rp.respCode.length()>0&&!"000".equals(rp.respCode))||authedMap.get(rp.accNo)!=null)continue;//已经成功认证过,或者值验证失败的
    			rp.way="0";
    			rp.channelId=PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CJ");
    			authMapBak.put(rp.sn, rp);
    			xmlDtl = xmlDtl + "<DTL><SN>"+ rp.sn+"</SN>"+
    				"<BANK_GENERAL_NAME>"+rp.bankGeneralName+"</BANK_GENERAL_NAME>" +
    				"<ACCOUNT_TYPE>"+(ACC_TYPE_MAP.get(rp.accType)==null?"00":ACC_TYPE_MAP.get(rp.accType))+"</ACCOUNT_TYPE>" +
    				"<ACCOUNT_NO>"+rp.accNo+"</ACCOUNT_NO>" +
    				"<ACCOUNT_NAME>"+rp.accName+"</ACCOUNT_NAME>" +
    				"<ID_TYPE>"+CRET_TYPE_MAP.get(rp.credentialType)+"</ID_TYPE>" +
    				"<ID>"+rp.credentialNo+"</ID>" +
    				"<TEL>"+rp.tel+"</TEL></DTL>";
    		}
    		if(xmlDtl.length()==0)return;
    		payRequest.authTranId = Tools.getUniqueIdentify();
    		//封装xml
    		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>" +
    				"<TRX_CODE>G60001</TRX_CODE><VERSION>01</VERSION>" +
    				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>" +
    				"<REQ_SN>"+ payRequest.authTranId +"</REQ_SN>" +
    				"<TIMESTAMP>"+payRequest.timestamp+"</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO><BODY><BATCH>" +
    				"<VALIDATE_MODE>V003</VALIDATE_MODE></BATCH>" +
    				"<TRANS_DETAILS>" + xmlDtl+ "</TRANS_DETAILS></BODY></MESSAGE>";
    		CjSignHelper singHelper = new CjSignHelper();
    		String signMsg = singHelper.signXml$Add(xml);
    		log.info("实名认证请求req======="+PayConstant.PAY_CONFIG.get("cj_receive_pay_url")+"=======\n"+signMsg);
    		//发送信息
    		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
    		log.info("实名认证响应resp==============\n"+res);
    		//验证签名
    		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
    		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
    		//解析响应
    		//<?xml version="1.0" encoding="UTF-8"?><MESSAGE><INFO><TRX_CODE>G60001</TRX_CODE><VERSION>01</VERSION><MERCHANT_ID>cp2016011227674</MERCHANT_ID><REQ_SN>ff4154572e814ab1a464421bb13303cf</REQ_SN><RET_CODE>0000</RET_CODE><ERR_MSG></ERR_MSG><TIMESTAMP>20160815135655</TIMESTAMP><SIGNED_MSG>MIIHdQYJKo...</SIGNED_MSG></INFO></MESSAGE>
    		Map <String,String>children = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","INFO"})[1];
    		if(!"0000".equals(children.get("RET_CODE")))throw new Exception("CJ渠道错误："+
    				(children.get("RET_CODE")==null?"未知":children.get("RET_CODE")+"("+children.get("ERR_MSG")+")"));
    		payRequest.setRespInfo("000");
    		PayRealNameAuthDAO payRealNameAuthDAO = new PayRealNameAuthDAO();
    		//保存认证结果
    		payRealNameAuthDAO.addPayRealNameAuthBatch(payRequest,rnAuthList);
    		//开始查询认证结果（查询完毕后通知商户）
    		for(int i=0; i<AUTH_TIME_FREQUENCY.length; i++){
    			log.info("实名查询第"+(i+1)+"次==============间隔"+AUTH_TIME_FREQUENCY[i]+"秒");
    			if(authMapBak.size()==0)break;
    			try {Thread.sleep(AUTH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
    			List<PayReceiveAndPay> sList = realNameAuthQuery(payRequest,authMapBak,1);
    			for(int j=0; j<sList.size(); j++){
    				PayReceiveAndPay rb = sList.get(j);//查询列表
    				if("000".equals(rb.respCode)){
    					PayReceiveAndPay rpTmp = authMapBak.get(rb.sn);
    					rpTmp.status="1";
    					rpTmp.setRespInfo("000");
    					rpTmp.respDesc=rb.respDesc;
    					authMapBak.remove(rb.sn);
    				} else {
    					if(!"0001".equals(rb.respCode) && !"0002".equals(rb.respCode)){
    						PayReceiveAndPay rpTmp = authMapBak.get(rb.sn);
    						if(rpTmp==null)continue;
    						rpTmp.status="2";
    						rpTmp.setRespCode("-1");
    						rpTmp.setRespDesc(rb.respDesc);
        					authMapBak.remove(rb.sn);
    					}
    				}
    			}
    		}
    		Iterator it = authMapBak.keySet().iterator();
    		while(it.hasNext()){
    			PayReceiveAndPay rpTmp = authMapBak.get(it.next());
    			rpTmp.status = "2";
    			rpTmp.setRespCode("-1");
    			rpTmp.setRespDesc("认证失败，未取到查询结果");
    		}
    		//更新认证结果
    		payRealNameAuthDAO.updatePayRealNameAuthBySearchChannel(rnAuthList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }
    /**
     * 单笔代收付
     * @param payRequest
     * @throws Exception 
     */
    public void receivePaySingle(PayRequest payRequest) throws Exception{
    	try {
    		//实名认证检查，如果没有实名认证，进行实名认证
    		//添加，赋值
    		PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
    		List<PayReceiveAndPay> rnAuthList = new ArrayList<PayReceiveAndPay>();
    		rnAuthList.add(rp);
    		if("0".equals(payRequest.receivePayType)){//收款需要认证，付款不用
	    		realNameAuthInner(payRequest,rnAuthList);
	    		if(!"000".equals(rnAuthList.get(0).respCode)){
	    			rp.status="2";
	    			payRequest.receivePayRes = "-1";
	    			payRequest.respCode=rnAuthList.get(0).respCode;
	    			payRequest.respDesc = rnAuthList.get(0).respDesc;
	    			new PayReceiveAndPayDAO().updatePayReceiveAndPay(rnAuthList);
	    			//通知商户
	    			new PayInterfaceOtherService().receivePayNotify(payRequest,rnAuthList);
	    			return;
	    		}
    		}
    		//签约检查，如果未签约，进行签约
    		Map map = new HashMap();
    		map.put(rp.accNo, rp);
    		if("0".equals(payRequest.receivePayType)){//收款需要签约，付款不用
	    		signProtocol(payRequest,rnAuthList);
	    		if(!"000".equals(rnAuthList.get(0).respCode)){
	    			rp.status="2";
	    			payRequest.receivePayRes = "-1";
	    			payRequest.respCode=rnAuthList.get(0).respCode;
	    			payRequest.respDesc = rnAuthList.get(0).respDesc;
	    			new PayReceiveAndPayDAO().updatePayReceiveAndPay(rnAuthList);
	    			//通知商户
	    			new PayInterfaceOtherService().receivePayNotify(payRequest,rnAuthList);
	    			return;
	    		}
    		}
    		payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    		String accountType = (ACC_TYPE_MAP.get(payRequest.accType)==null?"00":ACC_TYPE_MAP.get(payRequest.accType));
    		if("4".equals(payRequest.accountProp))accountType="03";//对公
    		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>"+
					"<TRX_CODE>"+RECEIVE_PAY_TYPE_SINGLE.get(payRequest.receivePayType)+"</TRX_CODE><VERSION>01</VERSION>"+
					"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>"+
					"<REQ_SN>"+ payRequest.merchantId+"_"+payRequest.tranId+"</REQ_SN><TIMESTAMP>"+payRequest.timestamp+"</TIMESTAMP>"+
					"<SIGNED_MSG></SIGNED_MSG></INFO>"+
					"<BODY>"+
					"<BUSINESS_CODE>"+("0".equals(payRequest.receivePayType)?PayConstant.PAY_CONFIG.get("cj_receive_business_code"):PayConstant.PAY_CONFIG.get("cj_pay_business_code"))+"</BUSINESS_CODE>"+
					"<CORP_ACCT_NO>"+PayConstant.PAY_CONFIG.get("RECEIVE_PAY_CORP_ACC_NO_ZX")+"</CORP_ACCT_NO>"+
					"<PRODUCT_CODE>"+("0".equals(payRequest.receivePayType)?PayConstant.PAY_CONFIG.get("cj_receive_single"):PayConstant.PAY_CONFIG.get("cj_pay_single"))+"</PRODUCT_CODE>"+
					"<ACCOUNT_PROP>"+("0".equals(payRequest.accountProp)?"0":"1")+"</ACCOUNT_PROP>"+
					"<BANK_GENERAL_NAME>"+payRequest.bankGeneralName+"</BANK_GENERAL_NAME>"+
					"<ACCOUNT_TYPE>"+accountType+"</ACCOUNT_TYPE>"+
					"<ACCOUNT_NO>"+payRequest.accNo+"</ACCOUNT_NO>"+
					"<ACCOUNT_NAME>"+payRequest.accName+"</ACCOUNT_NAME>"+
					"<BANK_NAME>"+payRequest.bankName+"</BANK_NAME>" +
					"<BANK_CODE>"+payRequest.bankId+"</BANK_CODE>" +
					"<DRCT_BANK_CODE>"+payRequest.bankId+"</DRCT_BANK_CODE>" +
					("0".equals(payRequest.receivePayType)?"<PROTOCOL_NO>"+rp.protocolNo+"</PROTOCOL_NO>":"")+//收款需要签约，付款不用
					"<CURRENCY>CNY</CURRENCY>"+
					"<AMOUNT>"+payRequest.amount+"</AMOUNT>"+
					("0".equals(payRequest.accountProp)?(//对私使用
					"<ID_TYPE>"+CRET_TYPE_MAP.get(payRequest.credentialType)+"</ID_TYPE>"+
					"<ID>"+payRequest.credentialNo+"</ID>"+
					"<TEL>"+payRequest.tel+"</TEL>"):""
					)
					+					
					"<CORP_FLOW_NO>"+ payRequest.merchantId+"_"+payRequest.tranId+"</CORP_FLOW_NO>"+
					(payRequest.summary.length()==0?"":"<SUMMARY>"+PayUtil.encodeString(payRequest.summary)+"</SUMMARY>")+
					"</BODY>"+
					"</MESSAGE>";
    		CjSignHelper singHelper = new CjSignHelper();
    		String signMsg = singHelper.signXml$Add(xml);
    		log.info("单笔代收付请求req======"+("0".equals(payRequest.receivePayType)?"代收":"代付")+"========"+signMsg);
    		//发送信息
    		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
    		log.info("单笔代收付响应resp======"+("0".equals(payRequest.receivePayType)?"代收":"代付")+"========\n"+res);
    		//验证签名
    		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
    		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
    		//解析响应
    		//<?xml version="1.0" encoding="UTF-8"?><MESSAGE><INFO><TRX_CODE>G10001</TRX_CODE><VERSION>01</VERSION><MERCHANT_ID>5ee26ddb404a590</MERCHANT_ID><REQ_SN>pUiVRKp2Q3rKTOI</REQ_SN><RET_CODE>0000</RET_CODE><ERR_MSG></ERR_MSG><TIMESTAMP>20160819144506</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO></MESSAGE>
    		Map <String,String>children = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","INFO"})[1];
    		if(!"0000".equals(children.get("RET_CODE"))&&!"2000".equals(children.get("RET_CODE"))){
       			rp.status="2";
       			rp.setRetCode("-1");
       			rp.errorMsg = "CJ渠道错误："+
        				(children.get("RET_CODE")==null?"未知":children.get("RET_CODE")+"("+children.get("ERR_MSG")+")");
       			payRequest.receivePayRes = "-1";
       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(rnAuthList);
       			//通知商户
       			new PayInterfaceOtherService().receivePayNotify(payRequest,rnAuthList);
       			return;
    		}
    		payRequest.setRespInfo("000");
    		payRequest.protocolNo=rp.protocolNo;
    		//启动查询线程，查询处理结果、通知商户、更新本地记录
    		new NotifyMerForReceiveAndPaySingle(payRequest,rnAuthList).start();
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
		//封装xml
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>" +
				"<TRX_CODE>G20001</TRX_CODE><VERSION>01</VERSION>" +
				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>" +
				"<REQ_SN>"+Tools.getUniqueIdentify()+"</REQ_SN>" +
				"<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO><BODY><QRY_REQ_SN>"
					+ request.merchantId+"_"+request.tranId+"</QRY_REQ_SN></BODY></MESSAGE>";
		log.info("代收付查询请求req======="+("0".equals(request.receivePayType)?"代收":"代付")+"=======\n"+xml);
		CjSignHelper singHelper = new CjSignHelper();
		String signMsg = singHelper.signXml$Add(xml);
		//发送信息
		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
		log.info("代收付查询响应resp======="+("0".equals(request.receivePayType)?"代收":"代付")+"=======\n"+res);
		// 验证签名
		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
		Map <String,String>info = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","INFO"})[1];
		if(!"0000".equals(info.get("RET_CODE"))){
			request.setRespCode("-1");
			rp.setRespCode(request.respCode);
			request.receivePayRes = "-1";
			String msg = info.get("ERR_MSG")==null?"":info.get("ERR_MSG").replaceAll("CJ","");
			request.respDesc="操作失败(CJ)"+(msg.indexOf("单位返回信息:余额不足")>=0?msg.replaceAll("单位返回信息:余额不足","银行出款失败，请工作日内处理"):msg);
			rp.errorMsg = request.respDesc;
			if(info.get("ERR_MSG").indexOf("无此交易")>=0)return true;
			return false;
		}
		Map <String,String>body = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","BODY"})[1];
		if("0000".equals(body.get("RET_CODE"))){
			request.setRespCode("000");
			request.receiveAndPaySingle.setRespCode("000");
			request.receivePayRes = "0";
			return true;
		} else {
			if(!"0001".equals(body.get("RET_CODE")) && !"0002".equals(body.get("RET_CODE"))){
				request.setRespCode("-1");
				rp.setRespCode(request.respCode);
				request.receivePayRes = "-1";
				String msg = body.get("ERR_MSG")==null?"":body.get("ERR_MSG").replaceAll("CJ","");
				request.respDesc="操作失败(CJ)"+(msg.indexOf("单位返回信息:余额不足")>=0?msg.replaceAll("单位返回信息:余额不足","银行出款失败，请工作日内处理"):msg);
				rp.errorMsg = request.respDesc;
				return true;
			} else return false;
		}
    }
    /**
     * 批量代收付
     * @param payRequest
     * @throws Exception 
     */
    public void receivePayBatch(PayRequest payRequest) throws Exception{
    	try {
    		if("0".equals(payRequest.receivePayType)){//收款需要认证，付款不用
	    		//实名认证检查，如果没有实名认证，进行实名认证
	    		realNameAuthInner(payRequest,payRequest.receivePayList);
	    		if(new PayReceiveAndPayDAO().updatePayReceiveAndPay(payRequest.receivePayList)==0){
	    			//通知商户
	    			new PayInterfaceOtherService().receivePayNotify(payRequest,payRequest.receivePayList);
	    			return;
	    		}
    		}
    		//签约检查，如果未签约，进行签约
    		if("0".equals(payRequest.receivePayType)){//收款需要签约，付款不用
	    		signProtocol(payRequest,payRequest.receivePayList);
	    		if(new PayReceiveAndPayDAO().updatePayReceiveAndPay(payRequest.receivePayList)==0) {
	    			//通知商户
	    			new PayInterfaceOtherService().receivePayNotify(payRequest,payRequest.receivePayList);
	    			return;
	    		}
    		}
    		payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    		String dtlXml = "";
    		for(int i = 0; i<payRequest.receivePayList.size(); i++){
    			PayReceiveAndPay rp = payRequest.receivePayList.get(i);
    			if(!"000".equals(rp.retCode))continue;
    			payRequest.totalAmt +=rp.amount;
    			payRequest.totalCount++;
    			dtlXml = dtlXml + "<DTL>"+
				"<SN>"+rp.sn+"</SN>"+
				"<BANK_GENERAL_NAME>"+rp.bankGeneralName+"</BANK_GENERAL_NAME>"+
				"<ACCOUNT_TYPE>"+(ACC_TYPE_MAP.get(rp.accType)==null?"00":ACC_TYPE_MAP.get(rp.accType))+"</ACCOUNT_TYPE>"+
				"<ACCOUNT_NO>"+rp.accNo+"</ACCOUNT_NO>"+
				"<ACCOUNT_NAME>"+rp.accName+"</ACCOUNT_NAME>"+
				"<BANK_NAME>"+rp.bankName+"</BANK_NAME>"+
				"<BANK_CODE>"+rp.bankId+"</BANK_CODE>"+
				"<DRCT_BANK_CODE>"+rp.bankId+"</DRCT_BANK_CODE>"+
				"<CURRENCY>CNY</CURRENCY>"+
				"<AMOUNT>"+rp.amount+"</AMOUNT>"+
				("0".equals(payRequest.receivePayType)?"<PROTOCOL_NO>"+rp.protocolNo+"</PROTOCOL_NO>":"")+//收款需要签约，付款不用
				("0".equals(payRequest.accountProp)?(//对私使用
				"<ID_TYPE>"+CRET_TYPE_MAP.get(rp.credentialType)+"</ID_TYPE>"+
				"<ID>"+rp.credentialNo+"</ID>"+
				"<TEL>"+rp.tel+"</TEL>"):""
				)
				+				
				"<CORP_FLOW_NO>"+ payRequest.merchantId+"_"+payRequest.tranId+"</CORP_FLOW_NO>"+
				(payRequest.summary.length()==0?"":"<SUMMARY>"+PayUtil.encodeString(payRequest.summary)+"</SUMMARY>")+"</DTL>";
    		}
    		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE>"+
				"<INFO><TRX_CODE>"+RECEIVE_PAY_TYPE_BATCH.get(payRequest.receivePayType)+"</TRX_CODE><VERSION>01</VERSION>"+
				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>"+
				"<REQ_SN>"+ payRequest.merchantId+"_"+payRequest.tranId+"</REQ_SN>"+
				"<TIMESTAMP>"+payRequest.timestamp+"</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO><BODY><BATCH>"+
				"<BUSINESS_CODE>"+("0".equals(payRequest.receivePayType)?PayConstant.PAY_CONFIG.get("cj_receive_business_code"):PayConstant.PAY_CONFIG.get("cj_pay_business_code"))+"</BUSINESS_CODE>"+
				"<CORP_ACCT_NO>"+PayConstant.PAY_CONFIG.get("RECEIVE_PAY_CORP_ACC_NO_ZX")+"</CORP_ACCT_NO>"+
				"<PRODUCT_CODE>"+("0".equals(payRequest.receivePayType)?PayConstant.PAY_CONFIG.get("cj_receive_batch"):PayConstant.PAY_CONFIG.get("cj_pay_batch"))+"</PRODUCT_CODE>"+
				"<ACCOUNT_PROP>"+("0".equals(payRequest.accountProp)?"0":"1")+"</ACCOUNT_PROP>"+
				"<TIMELINESS>0</TIMELINESS>"+
				"<TOTAL_CNT>"+payRequest.totalCount+"</TOTAL_CNT>"+
				"<TOTAL_AMT>"+payRequest.totalAmt+"</TOTAL_AMT></BATCH>"+
				"<TRANS_DETAILS>"+dtlXml+"</TRANS_DETAILS></BODY></MESSAGE>";
    		CjSignHelper singHelper = new CjSignHelper();
    		String signMsg = singHelper.signXml$Add(xml);
    		log.info("批量代收付请求req======"+("0".equals(payRequest.receivePayType)?"代收":"代付")+"========"+("0".equals(payRequest.receivePayType)?"代收":"代付")+"\n"+signMsg);
    		//发送信息
    		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
    		log.info("批量代收付响应resp======"+("0".equals(payRequest.receivePayType)?"代收":"代付")+"========\n"+res);
    		//验证签名
    		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
    		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
    		//解析响应
    		//<?xml version="1.0" encoding="UTF-8"?><MESSAGE><INFO><TRX_CODE>G10003</TRX_CODE><VERSION>01</VERSION><MERCHANT_ID>cp2016011227674</MERCHANT_ID><REQ_SN>pUMk8cy2Q3rKTOI</REQ_SN><RET_CODE>0000</RET_CODE><ERR_MSG></ERR_MSG><TIMESTAMP>20160824152315</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO><BODY><TRANS_DETAILS><DTL><SN>pUMk8cy2Q3rKTOJ</SN><RET_CODE>0001</RET_CODE><ERR_MSG></ERR_MSG></DTL></TRANS_DETAILS></BODY></MESSAGE>
    		Map <String,String>children = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","INFO"})[1];
    		if(!"0000".equals(children.get("RET_CODE"))&&!"2000".equals(children.get("RET_CODE"))){
    			for(int i = 0; i<payRequest.receivePayList.size(); i++){
        			PayReceiveAndPay rp = payRequest.receivePayList.get(i);
	       			rp.status="2";
	       			rp.setRespCode("-1");
	       			rp.errorMsg = "CJ渠道错误："+
	        				(children.get("RET_CODE")==null?"未知":children.get("RET_CODE")+"("+children.get("ERR_MSG")+")");
	       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(payRequest.receivePayList);
    			}
       			//通知商户
       			new PayInterfaceOtherService().receivePayNotify(payRequest,payRequest.receivePayList);
       			return;
    		}
    		//启动查询线程，查询处理结果、通知商户、更新本地记录
    		new NotifyMerForReceiveAndPayBatch(payRequest).start();
    	} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }
    /**
     * 代收付查询(批量)
     * @param request
     * @param searchMap
     * @return
     * @throws Exception
     */
    public void receivePayBatchQuery(PayRequest request,Map <String,PayReceiveAndPay>searchMap) throws Exception{
		//封装xml
		String details = "";
		Iterator it = searchMap.keySet().iterator();
		while(it.hasNext()){
			PayReceiveAndPay rp = searchMap.get(it.next());
			details = details + "<SN>"+rp.sn+"</SN>";
		}
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>"+
				"<TRX_CODE>G20002</TRX_CODE><VERSION>01</VERSION>"+
				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>"+
				"<REQ_SN>"+Tools.getUniqueIdentify()+"</REQ_SN>"+
				"<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP>"+
				"<SIGNED_MSG></SIGNED_MSG></INFO><BODY>"+
				"<QRY_REQ_SN>"+ request.merchantId+"_"+request.tranId+"</QRY_REQ_SN>"+
				"<TRANS_DETAILS>"+details+"</TRANS_DETAILS></BODY>"+
				"</MESSAGE>";
		log.info("批量代收付查询请求req======="+("0".equals(request.receivePayType)?"代收":"代付")+"=======\n"+xml);
		CjSignHelper singHelper = new CjSignHelper();
		String signMsg = singHelper.signXml$Add(xml);		
		//发送信息
		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
		log.info("批量代收付查询响应resp======="+("0".equals(request.receivePayType)?"代收":"代付")+"=======\n"+res);
		// 验证签名
		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
		Map <String,String>info = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","INFO"})[1];
		//请求失败，结束查询
		it = searchMap.keySet().iterator();
		if(!"0000".equals(info.get("RET_CODE"))){
			while(it.hasNext()){
				Object key = it.next();
				PayReceiveAndPay rp = searchMap.get(key);
				if(!"000".equals(rp.retCode))continue;
				rp.setRespCode("-1");
				String msg = info.get("ERR_MSG")==null?"":info.get("ERR_MSG").replaceAll("CJ","");
				rp.setRespDesc("操作失败(CJ)"+(msg.indexOf("单位返回信息:余额不足")>=0?msg.replaceAll("单位返回信息:余额不足","银行出款失败，请工作日内处理"):msg));
				searchMap.remove(key);
			}
			return;
		}
		//遍历查询列表，如果非等待状态，就删除查询记录
		Node node = PayUtil.getXmlNode(res,new String []{"MESSAGE","BODY","TRANS_DETAILS"});
		NodeList nodeList = node.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++){
			NodeList nodeList1 = nodeList.item(i).getChildNodes();
			PayReceiveAndPay rpTmp = new PayReceiveAndPay();
			for(int j=0; j<nodeList1.getLength(); j++){
				Node node1 = nodeList1.item(j);
				String value = node1.getTextContent().trim();
				if(node1.getNodeName().equals("SN"))rpTmp.sn=value;
				else if(node1.getNodeName().equals("RET_CODE")){
					if("0000".equals(value))rpTmp.setRespInfo("000");
					else rpTmp.setRespCode(value);
				}
				else if(node1.getNodeName().equals("ERR_MSG"))rpTmp.setRespDesc(value);
			}
			PayReceiveAndPay rp = searchMap.get(rpTmp.sn);
			if(rp != null){
				if("000".equals(rpTmp.respCode)){
					rp.status = "1";
					rp.setRespCode("000");
					searchMap.remove(rp.sn);
				} else {
					if(!"0001".equals(rpTmp.respCode) && !"0002".equals(rpTmp.respCode)){
						rp.status="2";
    					rp.setRespCode("-1");
    					rp.setRespDesc(rpTmp.respDesc);
    					searchMap.remove(rp.sn);
					}
				}
			}
		}
    }
    /**
     * 代收付查询-补单(批量)
     * @param request
     * @param searchMap
     * @return
     * @throws Exception
     */
    public void receivePayBatchQueryForRepair(PayRequest request,Map <String,PayReceiveAndPay>searchMap) throws Exception{
		//封装xml
		String details = "";
		Iterator it = searchMap.keySet().iterator();
		while(it.hasNext()){
			PayReceiveAndPay rp = searchMap.get(it.next());
			details = details + "<SN>"+rp.sn+"</SN>";
		}
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>"+
				"<TRX_CODE>G20002</TRX_CODE><VERSION>01</VERSION>"+
				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>"+
				"<REQ_SN>"+Tools.getUniqueIdentify()+"</REQ_SN>"+
				"<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP>"+
				"<SIGNED_MSG></SIGNED_MSG></INFO><BODY>"+
				"<QRY_REQ_SN>"+ request.merchantId+"_"+request.tranId+"</QRY_REQ_SN>"+
				"<TRANS_DETAILS>"+details+"</TRANS_DETAILS></BODY>"+
				"</MESSAGE>";
		log.info("批量代收付查询请求req======="+("0".equals(request.receivePayType)?"代收":"代付")+"=======\n"+xml);
		CjSignHelper singHelper = new CjSignHelper();
		String signMsg = singHelper.signXml$Add(xml);		
		//发送信息
		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
		log.info("批量代收付查询响应resp======="+("0".equals(request.receivePayType)?"代收":"代付")+"=======\n"+res);
		// 验证签名
		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
		Map <String,String>info = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","INFO"})[1];
		//请求失败，结束查询
		if(!"0000".equals(info.get("RET_CODE")) && !"0002".equals(info.get("RET_CODE")))return;
		Node batchRetCode = PayUtil.getXmlNode(res,new String []{"MESSAGE","BODY","BATCH","RET_CODE"});
		if(batchRetCode==null||(!"0000".equals(batchRetCode.getTextContent().trim())&&!"0002".equals(batchRetCode.getTextContent().trim()))){
			Node batchRetMsg = PayUtil.getXmlNode(res,new String []{"MESSAGE","BODY","BATCH","ERR_MSG"});
			Iterator it1 = searchMap.keySet().iterator();
			while(it1.hasNext()){
				PayReceiveAndPay rp = searchMap.get(it1.next());
				rp.status="2";
				rp.setRespCode("-1");
				rp.setRespDesc(batchRetMsg==null?"批量响应信息解析失败":batchRetMsg.getTextContent().trim());
			}
			return;
		}
		//遍历查询列表，如果非等待状态，就删除查询记录
		Node node = PayUtil.getXmlNode(res,new String []{"MESSAGE","BODY","TRANS_DETAILS"});
		NodeList nodeList = node.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++){
			NodeList nodeList1 = nodeList.item(i).getChildNodes();
			PayReceiveAndPay rpTmp = new PayReceiveAndPay();
			for(int j=0; j<nodeList1.getLength(); j++){
				Node node1 = nodeList1.item(j);
				String value = node1.getTextContent().trim();
				if(node1.getNodeName().equals("SN"))rpTmp.sn=value;
				else if(node1.getNodeName().equals("RET_CODE")){
					if("0000".equals(value))rpTmp.setRespInfo("000");
					else rpTmp.setRespCode(value);
				}
				else if(node1.getNodeName().equals("ERR_MSG"))rpTmp.setRespDesc(value);
			}
			PayReceiveAndPay rp = searchMap.get(rpTmp.sn);
			if(rp != null){
				if("000".equals(rpTmp.respCode)){
					rp.status = "1";
					rp.setRespCode("000");
				} else {
					if(!"0001".equals(rpTmp.respCode) && !"0002".equals(rpTmp.respCode)){
						rp.status="2";
    					rp.setRespCode("-1");
    					rp.setRespDesc(rpTmp.respDesc);
					}
				}
			}
		}
    }
    /**
     * 代收包装快捷补单
     * @param payOrder
     * @param rp
     * @throws Exception
     */
    public void tranQueryForRepair(PayOrder payOrder,PayReceiveAndPay rp) throws Exception{
    	//封装xml
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>" +
				"<TRX_CODE>G20001</TRX_CODE><VERSION>01</VERSION>" +
				"<MERCHANT_ID>"+PayConstant.PAY_CONFIG.get("cj_merchant_receive_merchant_id")+"</MERCHANT_ID>" +
				"<REQ_SN>"+Tools.getUniqueIdentify()+"</REQ_SN>" +
				"<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP><SIGNED_MSG></SIGNED_MSG></INFO><BODY><QRY_REQ_SN>"
					+ rp.channelTranNo+"</QRY_REQ_SN></BODY></MESSAGE>";
		log.info("补单代收查询请求req==============\n"+xml);
		CjSignHelper singHelper = new CjSignHelper();
		String signMsg = singHelper.signXml$Add(xml);		
		//发送信息
		String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("cj_receive_pay_url"),signMsg.getBytes("utf-8")),"utf-8");
		log.info("补单代收查询响应resp==============\n"+res);
		// 验证签名
		VerifyResult verifyResult = singHelper.verifyCjServerXml(res);
		if(!verifyResult.result)throw new Exception("CJ验签失败，"+verifyResult.errMsg);
		Map <String,String>info = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","INFO"})[1];
		if(!"0000".equals(info.get("RET_CODE")))throw new Exception(info.get("ERR_MSG"));
		Map <String,String>body = PayUtil.parseXmlChildrenToMap(res,new String []{"MESSAGE","BODY"})[1];
		rp.retCode = body.get("RET_CODE");
		if("0000".equals(body.get("RET_CODE"))){
			//更新代收信息
			rp.status="1";
			rp.errorMsg = "";
        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			payOrder.ordstatus="01";//支付成功
			payOrder.actdat = new Date();
		} else {
			rp.status="2";
			rp.errorMsg = body.get("ERR_MSG");
			payOrder.ordstatus="02";
			payOrder.actdat = new Date();
			payOrder.bankerror=rp.errorMsg;
			try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
		}
    }
}
class UpdateCJBankListTask extends TimerTask {
	//{"inst_id":"0002","pay_mode":"online_bank","inst_code":"CMB","inst_name":"招商银行","card_type ":"CC","card_attribute ":"C"}
	public void run(){
		String res = new CJPayService().QueryChannelBank();
		if(null!=res&&!res.equals("")){
			JSONObject jObject = JSONObject.fromObject(res);
			JSONArray retjson = JSONArray.fromObject(jObject.get("pay_inst_list"));
			for(int i=0;i<retjson.size();i++){
				JSONObject jsonObject = (JSONObject)retjson.get(i);
				CJPayService.CJ_BANK_MAP.put(jsonObject.getString("instCode")+
						"_"+jsonObject.getString("cardType")+"_"+jsonObject.getString("cardAttribute")+"_"+jsonObject.getString("payMode"),
						jsonObject.getString("instName"));
			}
		}
	}
}
class NotifyMerForReceiveAndPaySingle extends Thread {
	private static final Log log = LogFactory.getLog(NotifyMerForReceiveAndPaySingle.class);
	List<PayReceiveAndPay> notifyList;
	PayRequest payRequest;
	public NotifyMerForReceiveAndPaySingle(PayRequest payRequest,List<PayReceiveAndPay> notifyList){
		this.notifyList = notifyList;
		this.payRequest = payRequest;
	}
	public void run(){
		CJPayService cJPayService = new CJPayService();
		//查询处理结果
		for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
			try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			try {
				if(cJPayService.receivePaySingleQuery(payRequest,notifyList.get(0)))break;
			} catch (Exception e) {}
		}
		PayReceiveAndPay rp = notifyList.get(0);
		rp.retCode=payRequest.respCode;
		rp.errorMsg = payRequest.respDesc;
		String msg = "";
		//处理状态，0初始，1成功，2失败，3查询失败 默认0
		if("000".equals(payRequest.respCode))rp.status="1";
		else if("-1".equals(payRequest.respCode)){
			rp.status="2";
			msg = "代"+("0".equals(rp.type)?"收":"付")+"失败，商户号："+rp.custId+"，ID："+rp.id+"("+payRequest.respDesc+")";
		}
		else {
			rp.status="3";
			msg = "代"+("0".equals(rp.type)?"收":"付")+"失败，商户号："+rp.custId+"，ID："+rp.id+"(查询失败，请及时补单)";
		}
		//发送短信通知
		try {
			String [] ms = PayConstant.PAY_CONFIG.get("NOTIFY_MER_FAIL_SMS_MOBILE").replaceAll("，",",").split(",");
			for(int i=0; i<ms.length&&i<5; i++){//最多5个手机号发送
				if(ms[i].length()!=11)continue;
				try {new Long(ms[i]);} catch (Exception e) {continue;}
				new SMSUtil().sendWithThread(ms[i], msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
		payRequest.receivePayRes = "0";
		//更新本地记录
		try {
			new PayReceiveAndPayDAO().updatePayReceiveAndPay(notifyList);
			//通知商户
			new PayInterfaceOtherService().receivePayNotify(payRequest,notifyList);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
}
class NotifyMerForReceiveAndPayBatch extends Thread {
	private static final Log log = LogFactory.getLog(NotifyMerForReceiveAndPayBatch.class);
	PayRequest payRequest;
	public NotifyMerForReceiveAndPayBatch(PayRequest payRequest){
		this.payRequest = payRequest;
	}
	public void run(){
		CJPayService cJPayService = new CJPayService();
		Map <String,PayReceiveAndPay>searchMap = new HashMap<String,PayReceiveAndPay>();
		for(int i = 0; i<payRequest.receivePayList.size(); i++){
			PayReceiveAndPay rp = payRequest.receivePayList.get(i);
			if(!"000".equals(rp.retCode))continue;
			searchMap.put(rp.sn, rp);
		}
		//查询处理结果
		for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
			if(searchMap.size()==0)break;
			try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			try {
				cJPayService.receivePayBatchQuery(payRequest,searchMap);
			} catch (Exception e) {e.printStackTrace();}
		}
		Iterator it = searchMap.keySet().iterator();
		while(it.hasNext()){
			PayReceiveAndPay rpTmp = searchMap.get(it.next());
			rpTmp.status = "2";
			rpTmp.setRespCode("-1");
			rpTmp.setRespDesc(("0".equals(payRequest.receivePayType)?"代收":"代付")+"未取到查询结果");
		}
		//更新本地记录
		try {
			new PayReceiveAndPayDAO().updatePayReceiveAndPay(payRequest.receivePayList);
			//通知商户
			new PayInterfaceOtherService().receivePayNotify(payRequest,payRequest.receivePayList);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
}

