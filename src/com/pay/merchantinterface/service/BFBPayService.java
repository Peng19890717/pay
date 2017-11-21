package com.pay.merchantinterface.service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.JWebConstant;
import util.SMSUtil;
import util.Tools;
import com.PayConstant;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.bfb.util.BfbUtil;
import com.third.bfb.util.RSASignUtil;

/**
 * 邦付宝接口服务类
 * @author Administrator
 *
 */
public class BFBPayService {
	
	private static final Log log = LogFactory.getLog(BFBPayService.class);
	public static Map<String, String> bank_code = new HashMap<String, String>();
	public static Map<String, String> BFB_B2C_DF_BANK_CODE = new HashMap<String, String>();
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
		
		BFB_B2C_DF_BANK_CODE.put("ICBC","102100004951");//工商银行
		BFB_B2C_DF_BANK_CODE.put("PSBC","403100000004");//邮政储蓄银行
		BFB_B2C_DF_BANK_CODE.put("CCB","105100000017");//建设银行
		BFB_B2C_DF_BANK_CODE.put("CMB","308584001024");//招商银行
		BFB_B2C_DF_BANK_CODE.put("BOC","104100000045");//中国银行
		BFB_B2C_DF_BANK_CODE.put("CMBC","305100000013");//民生银行
		BFB_B2C_DF_BANK_CODE.put("ABC","103100000018");//农业银行
		BFB_B2C_DF_BANK_CODE.put("PAB","307584008005");//平安银行
		
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
			Map<String, String> req = new HashMap<String, String>();
			request.setCharacterEncoding("utf-8");
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB"); // 支付渠道
			payOrder.bankcod = request.getParameter("banks"); // 银行代码
			payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			String ProductName=PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			req.put("service", "GWDirectPay");
			req.put("charset", "02");
			req.put("version", "1.0");
			req.put("signType", "RSA");
			req.put("pageReturnUrl", PayConstant.PAY_CONFIG.get("BFB_WG_PAGERETURNURL"));
			req.put("offlineNotifyUrl", PayConstant.PAY_CONFIG.get("BFB_NOTIFY_URL"));
			req.put("requestId", payOrder.payordno);
			req.put("merchantId", PayConstant.PAY_CONFIG.get("BFB_MERONO"));
			req.put("orderId", payOrder.payordno);
			req.put("cardType", "1");
			req.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			req.put("totalAmount", String.valueOf(payOrder.txamt));
			req.put("currency", "CNY");
			req.put("validUnit", "03");
			req.put("validNum", "999");
			req.put("productName", ProductName);
			req.put("bankAbbr",bank_code.get(payOrder.bankcod));
			RSASignUtil util = new RSASignUtil(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
					+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("BFB_CERTPATH"),PayConstant.PAY_CONFIG.get("BFB_CERTPASS")); 
			String reqData = util.coverMap2String(req);
			req.put("merchantSign", util.sign(reqData, "UTF-8"));
			req.put("merchantCert",util.getCerInfo());
			log.info("邦付宝收银台下单请求参数:"+req);
			Set<String> set1 = req.keySet();
			Iterator<String> iterator1 = set1.iterator();
			while (iterator1.hasNext()) {
				String key0 = (String) iterator1.next();
				request.setAttribute(key0, req.get(key0));
			}
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
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BFB"); // 支付渠道
			payOrder.bankcod = payRequest.bankId; // 银行代码
			payOrder.bankCardType = payRequest.accountType;//String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
			//把请求参数打包成数组
			Map<String, String> req = new HashMap<String, String>();
			String ProductName=PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			req.put("service", "GWDirectPay");
			req.put("charset", "02");
			req.put("version", "1.0");
			req.put("signType", "RSA");
			req.put("pageReturnUrl",PayConstant.PAY_CONFIG.get("BFB_WG_PAGERETURNURL"));
			req.put("offlineNotifyUrl", PayConstant.PAY_CONFIG.get("BFB_NOTIFY_URL"));
			req.put("requestId", payOrder.payordno);
			req.put("merchantId", PayConstant.PAY_CONFIG.get("BFB_MERONO"));
			req.put("orderId", payOrder.payordno);
			req.put("cardType", "1");
			req.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			req.put("totalAmount", String.valueOf(payOrder.txamt));
			req.put("currency", "CNY");
			req.put("validUnit", "03");
			req.put("validNum", "999");
			req.put("productName", ProductName);
			req.put("bankAbbr",bank_code.get(payOrder.bankcod));
			RSASignUtil util = new RSASignUtil(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
					+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("BFB_CERTPATH"),PayConstant.PAY_CONFIG.get("BFB_CERTPASS")); 
			String reqData = util.coverMap2String(req);
			req.put("merchantSign", util.sign(reqData, "UTF-8"));
			req.put("merchantCert",util.getCerInfo());
			log.info("邦付宝商户接口下单请求参数:"+req);
			Iterator<String> iterator = req.keySet().iterator();
			while(iterator.hasNext()){
				String temp = iterator.next();
				request.setAttribute(temp, req.get(temp));
			}
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch(Exception e){
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
		public String  quickPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder)throws Exception{
			try {
				Map<String, String> req = new HashMap<String, String>();
				req.put("charset", "02");
				req.put("version", "1.0");
				req.put("signType", "RSA");
				req.put("service", "ApplyQuickSms");
				req.put("merchantId", PayConstant.PAY_CONFIG.get("BFB_MERONO"));
				req.put("cardNo", payRequest.cardNo);//收款人账号
				req.put("idInfo", payRequest.credentialNo);//身份证号。
				req.put("idType", "00");
				req.put("cardType", "0");
				req.put("cardName", payRequest.userName);//收款人账号
				req.put("cardPhone", payRequest.userMobileNo);
				req.put("smsType", "0");//0：开通快捷。1：使用快捷。
				Set<String> set1 = req.keySet();
				Iterator<String> iterator1 = set1.iterator();
				while (iterator1.hasNext()) {
					String key0 = (String) iterator1.next();
					if (StringUtils.equals(req.get(key0), "null") || StringUtils.isBlank(req.get(key0))) iterator1.remove();
				}
				RSASignUtil util = new RSASignUtil(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
    					+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("BFB_CERTPATH"),PayConstant.PAY_CONFIG.get("BFB_CERTPASS")); 
				String reqData = util.coverMap2String(req);
				String buf = reqData + "&merchantCert="+util.getCerInfo()+"&merchantSign="+util.sign(reqData, "UTF-8");
				log.info("邦付宝快捷发送短信验证码请求:"+buf);
				String res = BfbUtil.sendAndRecv(PayConstant.PAY_CONFIG.get("BFB_PAY_URL"), buf, "UTF-8");
				log.info("邦付宝发送短信响应业务参数:"+res);
				Map<String, String> responseMap = RSASignUtil.getValue(res);
				/**
				 * 返回参数示例：通信状态---业务状态。
				 * 失败:returnCode=600003&returnMessage=输入参数错误
				 * 
				 * 成功:charset=02&chkNo=272299&returnCode=000000&returnMessage=成功&rspExTime=60&
				 * serverCert=308203B63082029EA003020102021438129EA54B6611327421582D0855752AAA7676A7300D0
				 * 6092A864886F70D0101050500306C3126302406035504030C1DE5A4A9E5A881E8AF9AE4BFA1525341E6B58BE
				 * 8AF95E794A8E688B7434131123010060355040B0C09525341E6B58BE8AF953121301F060355040A0C18E5A4
				 * A9E5A881E8AF9AE4BFA1E6B58BE8AF95E7B3BBE7BB9F310B300906035504061302434E301E170D3137303333
				 * 303133343131315A170D3233303332393133333931325A307B31183016060355040A0C0FE5A4A9E5A881E8AF
				 * 9AE4BFA1525341310B3009060355040B0C0252413130302E06092A864886F70D010901162141423034343333
				 * 336C697579756665694061622D696E737572616E63652E636F6D310F300D06035504040C0673657276657231
				 * 0F300D06035504030C0673657276657230819F300D06092A864886F70D010101050003818D003081890281810
				 * 08AEC5BFABD028E35CE7269C924BBA7B686643F58E903C71B350CD22186847470F4BDE9F6EC51BE5B925322051
				 * F5FDE94B780871F02276A01E1586AA8CDD72EDEB08B06A91F6D42987427BEEE76B01E824035593474F33696A4
				 * E9752BAD4E792D8D6E1FCAF91F40DCD8217C2809E20B5CC55FD03B521BDBF6918FCFC09CAF3A2F0203010001A38
				 * 1C43081C130090603551D1304023000300B0603551D0F0404030204F030670603551D1F0460305E305CA05AA05
				 * 88656687474703A2F2F697472757364656D6F2E636F6D2F546F7043412F7075626C69632F697472757363726C3
				 * F43413D30323039413642414638384545323539414244433738363030423239393331343332334141353734301
				 * F0603551D23041830168014B46E6591914BD17BC1A09FA43E7DCF57E0B52E48301D0603551D0E041604140D7F1
				 * EC1F593B52EE1DF934F8DC7C3A4E20BDF50300D06092A864886F70D010105050003820101005590D53B3D&
				 * serverSign=1614630AF2A75492FF02B4D0CE5851D04F70B35B9D1E53BA2708E98667155D&
				 * service=ApplyQuickSms&signType=RSA&version=1.0
				 */
				if("000000".equals(responseMap.get("returnCode"))){
					//调用自己短信瓶体发送快捷验证码。
//					String content = "校验码为："+responseMap.get("chkNo")+"，您的尾号"+payRequest.cardNo.substring(payRequest.cardNo.length()-4)+"的卡付款"
//							+String.format("%.2f", ((double)Double.parseDouble(payRequest.merchantOrderAmt))/100d)
//							+"元，"+(ClearQuickPaySms.existTime/60)+"分钟内输入，请勿泄露。";
//					new SMSUtil().send(payRequest.userMobileNo, content);
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" " +
					"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
					"respCode=\"000\" respDesc=\"操作成功\" " +
					"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
				}else{
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
							"respCode=\"-1\" respDesc=\""+responseMap.get("returnMessage")+"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>"; 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * 快捷确认并支付
		 * @param payRequest
		 * @return
		 */
	    public String certPayConfirm(PayRequest payRequest) {
			try {
				
				Map<String, String> req = new HashMap<String, String>();
				req.put("charset", "02");
				req.put("version", "1.0");
				req.put("signType", "RSA");
				req.put("service", "FirstQPayment");
				req.put("offlineNotifyUrl", PayConstant.PAY_CONFIG.get("BFB_NOTIFY_URL"));//后台通知
				req.put("mercReqNo", Tools.getUniqueIdentify());//支付流水号.
				req.put("merchantId",  PayConstant.PAY_CONFIG.get("BFB_MERONO"));
				req.put("mercOrdNo", payRequest.payOrder.payordno);//订单号。
				req.put("ordDate", new SimpleDateFormat("yyyyMMdd").format(new Date()));//订单时间。
				req.put("cardNo", payRequest.payOrder.bankpayacno);//收款人账号
				req.put("idInfo", payRequest.productOrder.credentialNo);//身份证号。
				req.put("idType", "00");
				req.put("cardType", "0");
				req.put("cardName", payRequest.payOrder.bankpayusernm);
				req.put("cardPhone", payRequest.productOrder.mobile);
				req.put("encryptFlag", "1");	
				req.put("totalAmount", String.valueOf(payRequest.payOrder.txamt));//金额单位分。
				req.put("currency", "CNY");
				req.put("productName", "payOnLine");//收款人账号
				req.put("smsCode",payRequest.checkCode );//短信验证码。
				req.put("isRegAgreement", "N");
				Set<String> set1 = req.keySet();
				Iterator<String> iterator1 = set1.iterator();
				while (iterator1.hasNext()) {
					String key0 = (String) iterator1.next();
					String tmp = req.get(key0);
					if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
						iterator1.remove();
					}
				}
				RSASignUtil util = new RSASignUtil(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
    					+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("BFB_CERTPATH"),PayConstant.PAY_CONFIG.get("BFB_CERTPASS")); 
				String reqData = util.coverMap2String(req);
				String buf = reqData + "&merchantCert="+util.getCerInfo()+"&merchantSign="+util.sign(reqData, "UTF-8");
				log.info("邦付宝快捷支付请求参数:"+buf);
				String res = BfbUtil.sendAndRecv(PayConstant.PAY_CONFIG.get("BFB_PAY_URL"), buf, "UTF-8");
				log.info("邦付宝快捷支付响应参数:"+res);
				Map<String, String> responseMap = RSASignUtil.getValue(res);
				/**
				 * 失败:acDate=20170702&agrExpTime=&agrNo=&backParam=&bankAbbr=&charset=02&fee=&lastCardNo=8455&
				 * mercOrdNo=1499408804072&merchantId=800010000050169&ordDate=20170707&payTime=&returnCode=600035&
				 * returnMessage=未发送短信验证码或验证码已过期&serverCert=CCE633CB0B3D&serverSign=2ADDE21C4FE026372CA13061E0D1B0FE69332F5AB&
				 * service=FirstQPayment&signType=RSA&tradeNo=&version=1.0
				 * 
				 * 成功:acDate=20170702&agrExpTime=&agrNo=201707070000055212&backParam=&bankAbbr=CCB&charset=02&fee=0&lastCardNo=8455&
				 * mercOrdNo=1499409079905&merchantId=800010000050169&ordDate=20170707&payTime=20170707143108&returnCode=000000&
				 * returnMessage=成功&serverCert=CB0B3D&serverSign=3CE30&service=FirstQPayment&signType=RSA&tradeNo=201707079096153531&version=1.0
				 */
				if("000000".equals(responseMap.get("returnCode"))){
					return "{\"resCode\":\"000\",\"resMsg\":\"支付成功\"}";
				}else{
					return "{\"resCode\":\"-1\",\"resMsg\":\""+responseMap.get("returnMessage")+"\"}";
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
			try {
				Map<String, String> req = new HashMap<String, String>();
				req.put("charset", "02");
				req.put("version", "1.0");
				req.put("signType", "RSA");
				req.put("service", "OrderSearch");
				req.put("requestId", Tools.getUniqueIdentify());//支付流水号。
				req.put("merchantId", PayConstant.PAY_CONFIG.get("BFB_MERONO"));
				req.put("orderId", payOrder.payordno);//订单号。
				Set<String> set1 = req.keySet();
				Iterator<String> iterator1 = set1.iterator();
				while (iterator1.hasNext()) {
					String key0 = (String) iterator1.next();
					String tmp = req.get(key0);
					if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
						iterator1.remove();
					}
				}
				RSASignUtil util = new RSASignUtil(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
    					+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("BFB_CERTPATH"),PayConstant.PAY_CONFIG.get("BFB_CERTPASS")); 
				String reqData = util.coverMap2String(req);
				String buf = reqData + "&merchantCert="+util.getCerInfo()+"&merchantSign="+util.sign(reqData, "UTF-8");
				log.info("邦付宝查询通用接口请求业务参数:"+buf);
				String res = BfbUtil.sendAndRecv(PayConstant.PAY_CONFIG.get("BFB_PAY_URL"), buf, "UTF-8");
				log.info("邦付宝查询通用接口应业务参数:"+res);
				/**
				 * 失败:charset=02&returnCode=600051&returnMessage=订单不存在&serverCert=0B3D&serverSign=F35&service=OrderSearch&signType=RSA&version=1.0
				 * 
				 * 成功:backParam=&bankAbbr=CCB&charset=02&fee=0&merchantId=800010000050169&orderId=1499394146921&orderTime=20170707102213&
				 * payTime=20170707102214&purchaserId=131****3859&returnCode=000000&returnMessage=成功&
				 * serverCert=0B3D&serverSign=B04&service=OrderSearch&signType=RSA&status=PD&subMerchantId=&totalAmount=0.01&tradeNo=201707079096154087&version=1.0
				 */
				Map<String, String> responseMap = RSASignUtil.getValue(res);
				if("000000".equals(responseMap.get("returnCode"))){
					if("PD".equals(responseMap.get("status"))){
						payOrder.ordstatus="01";//支付成功
		            	new NotifyInterface().notifyMer(payOrder);//支付成功
					}
				}else new Exception(responseMap.get("returnMessage"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	/**
	 * 代付
	 * @param payRequest
	 * @throws Exception
	 */
	public void receivePaySingle(PayRequest payRequest) throws Exception{
    	try {
    		PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
    		PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);
    		if("1".equals(payRequest.receivePayType)){//代付业务。
    			Map<String, String> req = new HashMap<String, String>();
    			req.put("version", "1.0");
    			req.put("transCode", "singlePayment");
    			req.put("signType", "01");
    			req.put("merchantId", PayConstant.PAY_CONFIG.get("BFB_DF_MERONO"));
    			req.put("mcSequenceNo", rp.id);
    			String mcTransDateTime= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    			rp.setCreateTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(mcTransDateTime));
    			req.put("mcTransDateTime", mcTransDateTime);//商户交易时间
    			req.put("orderNo", rp.id);
    			req.put("amount", payRequest.amount);
    			req.put("cardNo", payRequest.accNo);//收款人账号
    			req.put("accName", payRequest.accName);
    			req.put("accType", "0");
    			req.put("busType","01");//实时单笔代付业务编码
     			req.put("lBnkNo", BFB_B2C_DF_BANK_CODE.get(cardBin.bankCode));   			
    			Set<String> set1 = req.keySet();
    			Iterator<String> iterator1 = set1.iterator();
    			while (iterator1.hasNext()) {
    				String key0 = (String) iterator1.next();
    				String tmp = req.get(key0);
    				if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
    					iterator1.remove();
    				}
    			}
    			RSASignUtil util = new RSASignUtil(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
    					+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("BFB_DF_CERTPATH"),PayConstant.PAY_CONFIG.get("BFB_DF_CERTPASS"));   			
    			String reqData = util.coverMap2String(req);
    			String merchantSign = util.capSign(reqData, "GBK");
    			String buf = reqData + "&signature=" + merchantSign;
    			log.info("邦付宝代付请求业务参数:"+buf);
    			String tradUrl=PayConstant.PAY_CONFIG.get("BFB_DF_PAY_URL");
    			String res = BfbUtil.sendAndRecv(tradUrl, buf, "GBK");
    			log.info("邦付宝代付响应业务参数:"+res);
    			Map<String, String> responseMap = RSASignUtil.getValue(res);
    			Set<String> set2 = responseMap.keySet();
    			Iterator<String> iterator2 = set2.iterator();
    			while (iterator2.hasNext()) {
    				String key0 = (String) iterator2.next();
    				String tmp = responseMap.get(key0);
    				if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
    					responseMap.remove(key0);
    				}
    			}
    			String sf = util.coverMap2String(responseMap);
                String respCode=util.getValue(res,"respCode");
                if("B0".equals(respCode)){
	    			// -- 验证签名
	    			boolean flag = false;
	    			RSASignUtil rsautil = new RSASignUtil(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
	    					+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("BFB_SERVERCERTPATH"));
	    			String serverSign = util.getValue(res, "signature");
	    			if (!serverSign.equals("") && serverSign != null) {
	    				flag = rsautil.verifyBySoft(serverSign, sf, "GBK");
	    				if (!flag) {
	    					log.info("邦付宝代付验签错误,验签报文:"+res);
	    					rp.status="2";
	    					rp.errorMsg = "提交失败";
	    					rp.channelTranNo=responseMap.get("orderNo");
	    		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	    				} else {
	    					// 成功的业务逻辑处理
	    					log.info("邦付宝代付验签成功,验签报文:"+res);
	    					rp.status="0";
	    					rp.errorMsg = "提交成功";
	    					rp.channelTranNo=responseMap.get("orderNo");
	    		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);}catch(Exception e){}
	    		        	BFBDFquery query = new BFBDFquery(rp,payRequest);
	    		        	query.start();
	    				}
	    			} else new Exception("代付提交渠道验签失败");
	    		}else{
	    			//代付提交失败
	    			String retmsg = responseMap.get("respMsg");
	    			rp.status="2";
					rp.errorMsg = retmsg;
		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	    		}
    		}
		} catch (Exception e) { 
			e.printStackTrace();
		}
    }
	
	 /**
     * 代付查询(单笔)
     * @param request
     * @return
     * @throws Exception
     */
	public boolean receivePaySingleQuery(PayRequest payRequest,PayReceiveAndPay rp) throws Exception{
		try {
			Map<String, String> req = new HashMap<String, String>();
			req.put("version", "1.0");
			req.put("transCode", "orderQuery");
			req.put("signType", "01");
			req.put("merchantId", PayConstant.PAY_CONFIG.get("BFB_DF_MERONO"));
			req.put("mcSequenceNo", rp.id);
			req.put("mcTransDateTime", new SimpleDateFormat("yyyyMMddHHmmss").format(rp.createTime));//原交易商户交易时间
			req.put("orderNo", rp.id);
			req.put("amount", String.valueOf(rp.amount));
			Set<String> set1 = req.keySet();
			Iterator<String> iterator1 = set1.iterator();
			while (iterator1.hasNext()) {
				String key0 = (String) iterator1.next();
				String tmp = req.get(key0);
				if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
					iterator1.remove();
				}
			}
			RSASignUtil util = new RSASignUtil(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
					+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("BFB_DF_CERTPATH"),PayConstant.PAY_CONFIG.get("BFB_DF_CERTPASS"));   			
			String reqData = util.coverMap2String(req);
			String merchantSign = util.capSign(reqData, "GBK");
			String buf = reqData + "&signature=" + merchantSign;
			log.info("邦付宝代付查询请求业务参数:"+buf);
			String tradUrl=PayConstant.PAY_CONFIG.get("BFB_DF_PAY_URL");			
			String res = "";
			try {
				res = BfbUtil.sendAndRecv(tradUrl, buf, "GBK");
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.info("邦付宝代付查询响应业务参数:"+res);
			Map<String, String> responseMap = RSASignUtil.getValue(res);
			Set<String> set2 = responseMap.keySet();
			Iterator<String> iterator2 = set2.iterator();
			while (iterator2.hasNext()) {
				String key0 = (String) iterator2.next();
				String tmp = responseMap.get(key0);
				if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
					responseMap.remove(key0);
				}
			}
            String respCode=util.getValue(res,"respCode");
			if("B0".equals(respCode)) {
				if("S".equals(responseMap.get("ordSts"))){
					payRequest.setRespCode("000");
					payRequest.receiveAndPaySingle.setRespCode("000");
					payRequest.receivePayRes = "0";
					payRequest.respDesc="交易成功";
					rp.errorMsg = "交易成功";
					return true;
				}else if("F".equals(responseMap.get("ordSts"))){
					payRequest.setRespCode("-1");
					payRequest.receiveAndPaySingle.setRespCode("000");
					payRequest.receivePayRes = "-1";
					payRequest.respDesc="交易失败";
					rp.errorMsg = payRequest.respDesc;
					return false;
				}else{
					payRequest.setRespCode("0");//处理中
					payRequest.receiveAndPaySingle.setRespCode("000");
					payRequest.receivePayRes = "-1";
					payRequest.respDesc="处理中";
					rp.errorMsg = payRequest.respDesc;
					return false;
				}
			}else throw new Exception(util.getValue(res,"respMsg"));
		} catch (Exception e) {
				e.printStackTrace();
				payRequest.setRespCode("-1");
				rp.setRespCode(payRequest.respCode);
				payRequest.receivePayRes = "-1";
				payRequest.respDesc=e.getMessage();
				rp.errorMsg = e.getMessage();
				return false;
		}
    }
}
class BFBDFquery extends Thread{
	
	private static final Log log = LogFactory.getLog(BFBDFquery.class);
	public PayRequest payRequest= new PayRequest();
	public PayReceiveAndPay rp = new PayReceiveAndPay();
	public BFBDFquery(PayReceiveAndPay rp,PayRequest payRequest) {
		this.payRequest=payRequest;
		this.rp=rp;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("邦付宝代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query(){
		boolean flag =false;
		try {
			Map<String, String> req = new HashMap<String, String>();
			req.put("version", "1.0");
			req.put("transCode", "orderQuery");
			req.put("signType", "01");
			req.put("merchantId", PayConstant.PAY_CONFIG.get("BFB_DF_MERONO"));
			req.put("mcSequenceNo", rp.id);
			req.put("mcTransDateTime", new SimpleDateFormat("yyyyMMddHHmmss").format(rp.createTime));//原交易商户交易时间
			req.put("orderNo", rp.id);
			req.put("amount", String.valueOf(rp.amount));
			Set<String> set1 = req.keySet();
			Iterator<String> iterator1 = set1.iterator();
			while (iterator1.hasNext()) {
				String key0 = (String) iterator1.next();
				String tmp = req.get(key0);
				if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
					iterator1.remove();
				}
			}
			RSASignUtil util = new RSASignUtil(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
					+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("BFB_DF_CERTPATH"),PayConstant.PAY_CONFIG.get("BFB_DF_CERTPASS"));   			
			String reqData = util.coverMap2String(req);
			String merchantSign = util.capSign(reqData, "GBK");
			String buf = reqData + "&signature=" + merchantSign;
			log.info("邦付宝代付查询请求业务参数:"+buf);
			String tradUrl=PayConstant.PAY_CONFIG.get("BFB_DF_PAY_URL");
			String res  = BfbUtil.sendAndRecv(tradUrl, buf, "GBK");
			log.info("邦付宝代付查询响应业务参数:"+res);
			Map<String, String> responseMap = RSASignUtil.getValue(res);
			Set<String> set2 = responseMap.keySet();
			Iterator<String> iterator2 = set2.iterator();
			while (iterator2.hasNext()) {
				String key0 = (String) iterator2.next();
				String tmp = responseMap.get(key0);
				if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
					responseMap.remove(key0);
				}
			}
	        String respCode=util.getValue(res,"respCode");
			if("B0".equals(respCode)) {
				if("S".equals(responseMap.get("ordSts"))){
					rp.status="1";
					rp.respCode="000";
					rp.errorMsg="交易成功";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					flag=true;
					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
					list.add(rp);
					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					return flag;
				}else if("F".equals(responseMap.get("ordSts"))){
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
			}else throw new Exception(responseMap.get("respMsg"));
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
	}
}
	