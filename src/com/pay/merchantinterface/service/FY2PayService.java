package com.pay.merchantinterface.service;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import util.DataTransUtil;

import com.PayConstant;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.fuiou.mpay.encrypt.RSAUtils;
import com.jweb.dao.Blog;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
/**
 * 富友接口服务类
 * @author Administrator
 *
 */
public class FY2PayService {
	private static final Log log = LogFactory.getLog(FY2PayService.class);
	//（钱通--富友）银行编码映射。
	public static Map<String, String> FY_B2C_BANK_CODE = new HashMap<String, String>();
	public static Map<String, String> fy_order_pay_type = new HashMap<String, String>();
	static {
		FY_B2C_BANK_CODE.put("ICBC","0801020000");//工商
		FY_B2C_BANK_CODE.put("ABC","0801030000");//农业
		FY_B2C_BANK_CODE.put("BOC","0801040000");//中国
		FY_B2C_BANK_CODE.put("CCB","0801050000");//建设
//		FY_B2C_BANK_CODE.put("BOCOM","0803010000");//交通
		FY_B2C_BANK_CODE.put("CMB","0803080000");//招商
		FY_B2C_BANK_CODE.put("ZSBC","0803160000");//浙商
		FY_B2C_BANK_CODE.put("CNCB","0803020000");//中信
		FY_B2C_BANK_CODE.put("CEB","0803030000");//光大银行。
		FY_B2C_BANK_CODE.put("HXB","0803040000");//华夏
		FY_B2C_BANK_CODE.put("PSBC","0801000000");//邮政
		FY_B2C_BANK_CODE.put("CMBC","0803050000");//民生
		FY_B2C_BANK_CODE.put("GDB","0803060000");//广发
		FY_B2C_BANK_CODE.put("CIB","0803090000");//兴业
		FY_B2C_BANK_CODE.put("SPDB","0803100000");//浦发
		FY_B2C_BANK_CODE.put("BCCB","0804031000");//北京
		FY_B2C_BANK_CODE.put("PAB","0804105840");//平安银行。
//		FY_B2C_BANK_CODE.put("SHRCB","0865012900");//上海农商
		//FY_B2C_BANK_CODE.put("","0804184930");//洛阳
		//FY_B2C_BANK_CODE.put("","0803202220");//大连鑫汇村镇银行
		fy_order_pay_type.put("1","B2C");	
		fy_order_pay_type.put("2","B2B");
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
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY"); // 支付渠道
			payOrder.bankcod = request.getParameter("banks"); // 银行代码
			payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			String mchnt_cd = PayConstant.PAY_CONFIG.get("fy_mchnt_cd2");//商户编号
			String order_id = payOrder.payordno==null?"":payOrder.payordno;
			String order_pay_type = fy_order_pay_type.get(payOrder.bustyp);//支付类型，只接入B2C/B2B.
			String order_valid_time = "10m";//默认的支付超时时间。10分钟。
			String page_notify_url = prdOrder.returl==null?"":prdOrder.returl;//支付通知地址。
			String back_notify_url = PayConstant.PAY_CONFIG.get("fy_back_notify_url2");//支付通知后台通知地址。
			String order_amt = payOrder.txamt.toString();//订单金额。
			String goods_name = "";//商品名称。
			String goods_display_url="";//商品展示网址。
			String rem="";//备注。
			String mchnt_key=PayConstant.PAY_CONFIG.get("fy_mchnt_key2");//商户编号
			String ver = PayConstant.PAY_CONFIG.get("fy_version");//版本号。
			String iss_ins_cd = FY_B2C_BANK_CODE.get(request.getParameter("banks"))==null?
					"0000000000": FY_B2C_BANK_CODE.get(request.getParameter("banks"));//银行编码。
			//MD5摘要数据。
			String signDataStr = mchnt_cd + "|" + order_id+ "|" +order_amt+ "|" +order_pay_type+ "|" +
                     page_notify_url+ "|" +back_notify_url+ "|" +order_valid_time+ "|" +
                     iss_ins_cd+ "|" +goods_name+ "|" +goods_display_url+ "|" 
                     +rem+ "|" +ver+ "|" + mchnt_key;
			//0001000F0306609|pZ7I3Wk2Q3rKTOG|1|B2C|https://www.qtongpay.com/pay/FYNotify.htm|https://www.qtongay.com/pay/FYNotify.htm|10m|0801020000||||1.0.1|9focss1ooei5tv51h0j8kk77jsqcnujw
			//mchnt_cd+"|" +order_id+"|"+order_amt+"|"+order_pay_type+"|"+page_notify_url+"|"+back_notify_url+"|"+order_valid_time+"|"+iss_ins_cd+"|"+goods_name+"|"+"+goods_display_url+"|"+rem+"|"+ver+"|"+mchnt_key
			String md5 = FYMD5.MD5Encode(signDataStr);
			log.info("富有直连网银签名数据==="+signDataStr);
			request.setAttribute("md5", md5);
			request.setAttribute("mchnt_cd", mchnt_cd);
			request.setAttribute("order_id", order_id);
			request.setAttribute("order_amt", order_amt);
			request.setAttribute("order_pay_type", order_pay_type);
			request.setAttribute("page_notify_url", page_notify_url);
			request.setAttribute("back_notify_url", back_notify_url);
			request.setAttribute("order_valid_time", order_valid_time);
			request.setAttribute("iss_ins_cd", iss_ins_cd);
			request.setAttribute("goods_name", goods_name);
			request.setAttribute("goods_display_url", goods_display_url);
			request.setAttribute("rem", rem);
			request.setAttribute("ver", ver);
			log.info("富有网关请求报文:"+"md5="+md5+"&mchnt_cd="+mchnt_cd+"&order_id="+order_id+"&order_amt="+order_amt+"&order_pay_type="+order_pay_type+
					"&page_notify_url="+page_notify_url+"&back_notify_url="+back_notify_url+"&order_valid_time="+order_valid_time+"&iss_ins_cd="+iss_ins_cd+
					"&goods_name="+goods_name+"&goods_display_url="+goods_display_url+"&rem="+rem+"&ver="+ver);
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
			Blog log = new Blog();
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_FY"); // 支付渠道
			payOrder.bankcod = payRequest.bankId; // 银行代码
			payOrder.bankCardType = payRequest.accountType;//String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			String mchnt_cd = PayConstant.PAY_CONFIG.get("fy_mchnt_cd2");//商户编号
			String order_id = payOrder.payordno==null?"":payOrder.payordno;
			String order_pay_type = fy_order_pay_type.get(payRequest.userType);//支付类型，只接入B2C/B2B.
			String order_valid_time = "10m";//默认的支付超时时间。10分钟。
			String page_notify_url = prdOrder.returl==null?"":prdOrder.returl;//支付通知地址。
			String back_notify_url = PayConstant.PAY_CONFIG.get("fy_back_notify_url2");//支付通知后台通知地址。
			String order_amt = payOrder.txamt.toString();//订单金额。
			String goods_name = "";//商品名称。
			String goods_display_url="";//商品展示网址。
			String rem="";//备注。
			String mchnt_key=PayConstant.PAY_CONFIG.get("fy_mchnt_key2");//商户编号
			String ver = PayConstant.PAY_CONFIG.get("fy_version");//版本号。
			String iss_ins_cd = FY_B2C_BANK_CODE.get(payRequest.bankId)==null?
					"0000000000": FY_B2C_BANK_CODE.get(payRequest.bankId);//银行编码。
			//MD5摘要数据。
			String signDataStr = mchnt_cd + "|" + order_id+ "|" +order_amt+ "|" +order_pay_type+ "|" +
                     page_notify_url+ "|" +back_notify_url+ "|" +order_valid_time+ "|" +
                     iss_ins_cd+ "|" +goods_name+ "|" +goods_display_url+ "|" 
                     +rem+ "|" +ver+ "|" + mchnt_key;
			String md5 = FYMD5.MD5Encode(signDataStr);
			log.info("富有直连网银签名数据==="+signDataStr);
			request.setAttribute("md5", md5);
			request.setAttribute("mchnt_cd", mchnt_cd);
			request.setAttribute("order_id", order_id);
			request.setAttribute("order_amt", order_amt);
			request.setAttribute("order_pay_type", order_pay_type);
			request.setAttribute("page_notify_url", page_notify_url);
			request.setAttribute("back_notify_url", back_notify_url);
			request.setAttribute("order_valid_time", order_valid_time);
			request.setAttribute("iss_ins_cd", iss_ins_cd);
			request.setAttribute("goods_name", goods_name);
			request.setAttribute("goods_display_url", goods_display_url);
			request.setAttribute("rem", rem);
			request.setAttribute("ver", ver);
			log.info("富有网关请求报文:"+"md5="+md5+"&mchnt_cd="+mchnt_cd+"&order_id="+order_id+"&order_amt="+order_amt+"&order_pay_type="+order_pay_type+
					"&page_notify_url="+page_notify_url+"&back_notify_url="+back_notify_url+"&order_valid_time="+order_valid_time+"&iss_ins_cd="+iss_ins_cd+
					"&goods_name="+goods_name+"&goods_display_url="+goods_display_url+"&rem="+rem+"&ver="+ver);
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
			Blog log = new Blog();
			String mchnt_cd = PayConstant.PAY_CONFIG.get("fy_mchnt_cd2");//商户编号
			String order_id = payOrder.payordno==null?"":payOrder.payordno;//订单号。
			String page_notify_url = PayConstant.PAY_CONFIG.get("fy_page_notify_ur2l");//支付通知地址。
			String back_notify_url = PayConstant.PAY_CONFIG.get("fy_back_notify_url2");//支付通知后台通知地址。
			String mchnt_key=PayConstant.PAY_CONFIG.get("fy_mchnt_key2");//秘钥。
			//MD5摘要数据。
			String signDataStr = mchnt_cd + "|" + order_id+ "|"
	                   + page_notify_url + "|" + back_notify_url + "|" + mchnt_key;
			String md5 = FYMD5.MD5Encode(signDataStr);
			log.info("富有查询签名数据==="+signDataStr);
			Map<String, String> data = new HashMap<String, String>();
			data.put("mchnt_cd", mchnt_cd);
			data.put("order_id", order_id);
			data.put("page_notify_url", page_notify_url);
			data.put("back_notify_url", back_notify_url);
			data.put("md5", md5);
			String params = "";
			Iterator<String> it = data.keySet().iterator();
				while(it.hasNext()){
					Object key = it.next();
					params = params+key+"="+data.get(key)+ "&";
				}
				params = params.substring(0,params.length()-1); 
				log.info("富有网关查询请求报文:"+params.toString());
				//返回结果以异步通知方式执行。
			new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("fy_mchnt_query_url"), params.getBytes("utf-8"));
		} catch (Exception e){
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
	public String  quickPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder)throws Exception{
		try {
			String key = PayConstant.PAY_CONFIG.get("fy_quickPay_key2"); //富友分配的32位商户秘钥key。
			String version = PayConstant.PAY_CONFIG.get("fy_quickPay_version"); //版本号。2.3 
			String userip = PayConstant.PAY_CONFIG.get("fy_quickPay_UserIp"); //用户ip地址。
			String mchntcd =PayConstant.PAY_CONFIG.get("fy_quickPay_mchntcd2"); //商户编码。
			String type = "03"; //交易类型。03 固定的。
			String mchntorderid =payOrder.payordno;//商户流水号。可以用payno。
			String userid = payRequest.userMobileNo;//可以使用用户的手机号。
			String amt = payRequest.merchantOrderAmt; //交易金额，单位为分。
			String bankcard =payRequest.cardNo; //支付使用的银行卡号。
			String name = payRequest.userName; //姓名。
			String idtype = "0";//证件类型。 身份证号-0 固定的。
			String idno = payRequest.credentialNo; //身份证号码。
			String mobile = payRequest.userMobileNo; //银行预留的手机号。
			String backurl = PayConstant.PAY_CONFIG.get("fy_quickPay_backurl2"); //结果异步通知路径。
			String signtp = "MD5";//默认MD5.
			String sign = FYMD5.MD5Encode(type+"|"+version+"|"+mchntcd+"|"+mchntorderid+"|"+userid+"|"+amt+"|"+bankcard+"|"+backurl+"|"+name+"|"+idno+"|"+idtype+"|"+mobile+"|"+userip+"|"+key);
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);//通过卡号获取卡类信息。
			StringBuffer bufXml = new StringBuffer();
			bufXml.append("<REQUEST>");
			bufXml.append("<VERSION>"+version+"</VERSION>");
			bufXml.append("<MCHNTCD>"+mchntcd+"</MCHNTCD>");
			bufXml.append("<TYPE>"+type+"</TYPE>");
			bufXml.append("<MCHNTORDERID>"+mchntorderid+"</MCHNTORDERID>");
			bufXml.append("<USERID>"+userid+"</USERID>");
			bufXml.append("<AMT>"+amt+"</AMT>");
			bufXml.append("<BANKCARD>"+bankcard+"</BANKCARD>");
			bufXml.append("<NAME>"+name+"</NAME>");
			bufXml.append("<IDTYPE>"+idtype+"</IDTYPE>");
			bufXml.append("<IDNO>"+idno+"</IDNO>");
			bufXml.append("<MOBILE>"+mobile+"</MOBILE>");
			if(cardBin.cardType.equals("0")){
				bufXml.append("<CVN></CVN>");
			}else if(cardBin.cardType.equals("1")){
				bufXml.append("<CVN>"+RSAUtils.encryptByPublicKey(payRequest.cvv2+";"+payRequest.validPeriod,PayConstant.PAY_CONFIG.get("fy_quickPay_puglicKey"))+"</CVN>");
			}
			bufXml.append("<BACKURL>"+backurl+"</BACKURL>");
			bufXml.append("<REM1></REM1>");
			bufXml.append("<REM2></REM2>");
			bufXml.append("<REM3></REM3>");
			bufXml.append("<SIGNTP>"+signtp+"</SIGNTP>");
			bufXml.append("<USERIP>"+userip+"</USERIP>");
			bufXml.append("<SIGN>"+sign+"</SIGN>");
			bufXml.append("</REQUEST>");
			log.info("富友快捷下单请求未加密的报文:     "+bufXml.toString());
			String APIFMS = DESCoderFUIOU.desEncrypt(bufXml.toString(), DESCoderFUIOU.getKeyLength8(key));
			String params = "APIFMS="+URLEncoder.encode(APIFMS,"utf-8")+"&MCHNTCD="+mchntcd;
			String result = DESCoderFUIOU.desDecrypt(new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("fy_quickPay_payurl"), 
					params.getBytes("utf-8")),"utf-8"),DESCoderFUIOU.getKeyLength8(key));
			log.info("富友快捷下单返回解密后报文:   "+result);
			//解析返回xml数据结果。
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
	        DocumentBuilder builder = factory.newDocumentBuilder();   
	        Document document = builder.parse(new InputSource(new StringReader(result)));   
	        Element root = document.getDocumentElement();   
	        NodeList list = root.getChildNodes();
	        Map<String,String> resultMap = new HashMap<String,String>();
	        for(int i=0;i<list.getLength();i++){
	            	 Node node = list.item(i);
	            	 if(node.getNodeName().equals("TYPE")){
	            		 resultMap.put("TYPE",node.getTextContent());
	            	 }else if(node.getNodeName().equals("VERSION")){
	            		 resultMap.put("VERSION",node.getTextContent());
	            	 }else if(node.getNodeName().equals("RESPONSECODE")){
	            		 resultMap.put("RESPONSECODE",node.getTextContent());
	            	 }else if(node.getNodeName().equals("MCHNTCD")){
	            		 resultMap.put("MCHNTCD",node.getTextContent());
	            	 }else if(node.getNodeName().equals("MCHNTORDERID")){
	            		 resultMap.put("MCHNTORDERID",node.getTextContent());
	            	 }else if(node.getNodeName().equals("USERID")){
	            		 resultMap.put("USERID",node.getTextContent());
	            	 }else if(node.getNodeName().equals("ORDERID")){
	            		 resultMap.put("ORDERID",node.getTextContent());
	            	 }else if(node.getNodeName().equals("AMT")){
	            		 resultMap.put("AMT",node.getTextContent());
	            	 }else if(node.getNodeName().equals("SIGN")){
	            		 resultMap.put("SIGN",node.getTextContent());
	            	 }else if(node.getNodeName().equals("SIGNPAY")){
	            		 resultMap.put("SIGNPAY",node.getTextContent());
	            	 }else if(node.getNodeName().equals("RESPONSEMSG")){
	            		 resultMap.put("RESPONSEMSG",node.getTextContent());
	            	 }
	          }
			String sing_temp = FYMD5.MD5Encode(resultMap.get("TYPE")+"|"+resultMap.get("VERSION")+"|"+resultMap.get("RESPONSECODE")+"|"+
					resultMap.get("MCHNTCD")+"|"+resultMap.get("MCHNTORDERID")+"|"+resultMap.get("USERID")+"|"+resultMap.get("ORDERID")+"|"+resultMap.get("AMT")+"|"+key);
			if(sing_temp.equals(resultMap.get("SIGN"))){
				if(resultMap.get("RESPONSECODE").equals("0000")){
					payOrder.bankjrnno=resultMap.get("ORDERID");//ORDERID
					payOrder.filed5 = resultMap.get("SIGNPAY");//SIGNPAY
					new PayInterfaceDAO().updatePayOrder(payOrder);
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" " +
					"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
					"respCode=\"000\" respDesc=\"操作成功\" " +
					"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>"; 
				}else{
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
							"respCode=\"-1\" respDesc=\""+resultMap.get("RESPONSEMSG")+"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>"; 
				}
			}else{
				throw new Exception("富友快捷下单返回信息,验签失败!");
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
			String key = PayConstant.PAY_CONFIG.get("fy_quickPay_key2"); //富友分配的32位商户秘钥key。
			String version = PayConstant.PAY_CONFIG.get("fy_quickPay_version"); //版本号。2.3 
			String userip = PayConstant.PAY_CONFIG.get("fy_quickPay_UserIp"); //用户ip地址。
			String mchntcd =PayConstant.PAY_CONFIG.get("fy_quickPay_mchntcd2"); //商户编码。
			String type = "03"; //交易类型。03 固定的。
			String mchntorderid =payRequest.payOrder.payordno;//商户流水号。可以用payno。
			String userid = payRequest.productOrder.mobile;//可以使用用户的手机号。
			String orderid = payRequest.payOrder.bankjrnno; //富友快捷下单返回的订单号。
			String bankcard = payRequest.payOrder.payacno; //支付使用的银行卡号。
			String mobile =  payRequest.productOrder.mobile; //银行预留的手机号。
			String vercd = payRequest.checkCode;//验证码。
			String signtp = "MD5";//默认MD5.
			String sign = FYMD5.MD5Encode(type+"|"+version+"|"+mchntcd+"|"+orderid+"|"+mchntorderid+"|"+userid+"|"+bankcard+"|"+vercd+"|"+mobile+"|"+ userip+"|"+key);
			String signpay = payRequest.payOrder.filed5; //原样传送下单验证码接口返回的SIGNPAY字段
			StringBuffer bufXml = new StringBuffer();
			bufXml.append("<REQUEST>");
			bufXml.append("<VERSION>"+version+"</VERSION>");
			bufXml.append("<MCHNTCD>"+mchntcd+"</MCHNTCD>");
			bufXml.append("<TYPE>"+type+"</TYPE>");
			bufXml.append("<MCHNTORDERID>"+mchntorderid+"</MCHNTORDERID>");
			bufXml.append("<USERID>"+userid+"</USERID>");
			bufXml.append("<ORDERID>"+orderid+"</ORDERID>");
			bufXml.append("<BANKCARD>"+bankcard+"</BANKCARD>");
			bufXml.append("<MOBILE>"+mobile+"</MOBILE>");
			bufXml.append("<VERCD>"+vercd+"</VERCD>");
			bufXml.append("<REM1></REM1>");
			bufXml.append("<REM2></REM2>");
			bufXml.append("<REM3></REM3>");
			bufXml.append("<SIGNTP>"+signtp+"</SIGNTP>");
			bufXml.append("<USERIP>"+userip+"</USERIP>");
			bufXml.append("<SIGN>"+sign+"</SIGN>");
			bufXml.append("<SIGNPAY>"+signpay+"</SIGNPAY>");
			bufXml.append("</REQUEST>");
			log.info("富友快捷支付确认支付发出报文:"+bufXml.toString());
			String APIFMS = DESCoderFUIOU.desEncrypt(bufXml.toString(), DESCoderFUIOU.getKeyLength8(key));
			String params ="APIFMS="+URLEncoder.encode(APIFMS,"utf-8")+"&MCHNTCD="+mchntcd;
			String result = DESCoderFUIOU.desDecrypt(new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("fy_quickPay_certPayConfirm"), params.getBytes("utf-8")),"utf-8"),DESCoderFUIOU.getKeyLength8(key));
			log.info("富友快捷确认支付返回报文:"+result);
			//解析返回xml数据结果。
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
	        DocumentBuilder builder = factory.newDocumentBuilder();   
	        Document document = builder.parse(new InputSource(new StringReader(result)));   
	        Element root = document.getDocumentElement();   
	        NodeList list = root.getChildNodes();
	        Map<String,String> resultMap = new HashMap<String,String>();
	        for(int i=0;i<list.getLength();i++){
	            	 Node node = list.item(i);
	            	 if(node.getNodeName().equals("TYPE")){
	            		 resultMap.put("TYPE",node.getTextContent());
	            	 }else if(node.getNodeName().equals("VERSION")){
	            		 resultMap.put("VERSION",node.getTextContent());
	            	 }else if(node.getNodeName().equals("RESPONSECODE")){
	            		 resultMap.put("RESPONSECODE",node.getTextContent());
	            	 }else if(node.getNodeName().equals("MCHNTCD")){
	            		 resultMap.put("MCHNTCD",node.getTextContent());
	            	 }else if(node.getNodeName().equals("MCHNTORDERID")){
	            		 resultMap.put("MCHNTORDERID",node.getTextContent());
	            	 }else if(node.getNodeName().equals("ORDERID")){
	            		 resultMap.put("ORDERID",node.getTextContent());
	            	 }else if(node.getNodeName().equals("AMT")){
	            		 resultMap.put("AMT",node.getTextContent());
	            	 }else if(node.getNodeName().equals("SIGN")){
	            		 resultMap.put("SIGN",node.getTextContent());
	            	 }else if(node.getNodeName().equals("BANKCARD")){
	            		 resultMap.put("BANKCARD",node.getTextContent());
	            	 }else if(node.getNodeName().equals("RESPONSEMSG")){
	            		 resultMap.put("RESPONSEMSG",node.getTextContent());
	            	 }
	          }
	        String sing_temp = FYMD5.MD5Encode(resultMap.get("TYPE")+"|"+resultMap.get("VERSION")+"|"+resultMap.get("RESPONSECODE")+"|"+
					resultMap.get("MCHNTCD")+"|"+resultMap.get("MCHNTORDERID")+"|"+resultMap.get("ORDERID")+"|"+resultMap.get("AMT")+"|"+resultMap.get("BANKCARD")+"|"+key);
	    	if(sing_temp.equals(resultMap.get("SIGN"))){
	    		if(!resultMap.get("RESPONSECODE").equals("0000")){
	    			PayOrder payOrder = new PayOrder();
	    	    	payOrder.merno = payRequest.merchantId;
	    	    	payOrder.prdordno = payRequest.merchantOrderId;
	    			payOrder.bankerror =resultMap.get("RESPONSEMSG");
	    			new PayOrderDAO().updateOrderError(payOrder);
	    			log.info("支付失败"+resultMap.get("RESPONSEMSG"));
	    		}
	    		return "{\"responsecode\":\""+resultMap.get("RESPONSECODE")+"\",\"responsemsg\":\""+resultMap.get("RESPONSEMSG")+"\"}";
	    	}else{
				throw new Exception("富友快捷确认支付返回信息,验签失败!");
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
		Blog log = new Blog();
		try{
			String mchntcd =PayConstant.PAY_CONFIG.get("fy_quickPay_mchntcd2"); //商户号。
			String OrderId = payOrder.bankjrnno; //富友下单后返回的订单号。
			String key = PayConstant.PAY_CONFIG.get("fy_quickPay_key2"); 
			String Sign =  FYMD5.MD5Encode(mchntcd+"|"+OrderId+"|"+key);
			StringBuffer bufXml = new StringBuffer();
			bufXml.append("<FM>");
			bufXml.append("<MchntCd>"+mchntcd+"</MchntCd>");
			bufXml.append("<OrderId>"+OrderId+"</OrderId>");
			bufXml.append("<Sign>"+Sign+"</Sign>");
			bufXml.append("</FM>");
			log.info("富友快捷支付查询发出报文:"+bufXml.toString());
			String params = "FM="+bufXml.toString();
			//<FM><Sign>1ec54e1d7ea932e9cf9574761b70ad4e</Sign><Rcd>5185</Rcd><RDesc>订单已支付</RDesc></FM>
			String result =  new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("fy_quickPay_Query"),params.getBytes("utf-8")),"utf-8");
			log.info("富友快捷支付查询返回的报文:"+result);
			String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
			//解析返回xml数据结果。
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
	        DocumentBuilder builder = factory.newDocumentBuilder();   
	        Document document = builder.parse(new InputSource(new StringReader(xmlHeader+result)));   
	        Element root = document.getDocumentElement();   
	        NodeList list = root.getChildNodes();
	        Map<String,String> resultMap = new HashMap<String,String>();
	        for(int i=0;i<list.getLength();i++){
	            	 Node node = list.item(i);
	            	 if(node.getNodeName().equals("Sign")){
	            		 resultMap.put("Sign",node.getTextContent());
	            	 }else if(node.getNodeName().equals("Rcd")){
	            		 resultMap.put("Rcd",node.getTextContent());
	            	 }else if(node.getNodeName().equals("RDesc")){
	            		 resultMap.put("RDesc",node.getTextContent());
	            	 }
	          }
	        String sing_temp = FYMD5.MD5Encode(resultMap.get("Rcd")+"|"+key);
	        if(sing_temp.equals(resultMap.get("Sign"))){
	        	if(resultMap.get("Rcd").equals("5185")){
	        		payOrder.ordstatus="01";//支付成功
	            	new NotifyInterface().notifyMer(payOrder);//支付成功
	        	}else{
	        		payOrder.ordstatus="02";//支付成功
	        		payOrder.backerror=resultMap.get("RDesc");
	            	new NotifyInterface().notifyMer(payOrder);//支付成功
	        	}
	        }else{
	        	throw new Exception("富友快捷支付查询，验签失败");
	        }
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
