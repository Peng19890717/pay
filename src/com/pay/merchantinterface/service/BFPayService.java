package com.pay.merchantinterface.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import util.DataTransUtil;
import util.MD5;
import util.Tools;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.merchantinterface.dao.PayTranUserQuickCard;
import com.pay.merchantinterface.dao.PayTranUserQuickCardDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
import com.pay.refund.dao.PayRefund;
import com.third.baofo.RsaCodingUtil;
import com.third.baofo.SecurityUtil;
/**
 * 宝付接口服务类
 * @author Administrator
 *
 */
public class BFPayService {
	public static Map<String,String>BANK_MAP_JIEJI = new HashMap <String,String>();//借记卡银行码映射
	public static Map <String,String> BANK_MAP_DAIJI = new HashMap <String,String>();//贷记卡银行码映射
	public static Map  <String,String>BANK_MAP_PUB = new HashMap <String,String>();//对公银行码映射
	public static Map<String,String> ORDER_QUERY_MSG = new HashMap<String,String>();//查询结果
	public static Map<String, String> BF_BANK_CODE = new HashMap<String, String>();
	static {
		BANK_MAP_JIEJI.put("ICBC","3002");//工商银行
		BANK_MAP_JIEJI.put("ABC","3005");//农业银行
		BANK_MAP_JIEJI.put("BOC","3026");//中国银行
		BANK_MAP_JIEJI.put("CCB","3003");//建设银行
		BANK_MAP_JIEJI.put("CNCB","3039");//中信银行
		BANK_MAP_JIEJI.put("BOCOM","3020");//交通银行
		BANK_MAP_JIEJI.put("CEB","3022");//光大银行
		BANK_MAP_JIEJI.put("HXB","3050");//华夏银行
		BANK_MAP_JIEJI.put("SPDB","3004");//浦发银行
		BANK_MAP_JIEJI.put("CIB","3009");//兴业银行
		BANK_MAP_JIEJI.put("GDB","3036");//广发银行
		BANK_MAP_JIEJI.put("PAB","3035");//平安银行
		BANK_MAP_JIEJI.put("CMB","3001");//招商银行
		BANK_MAP_JIEJI.put("HKBCHINA","3086");//汉口银行
		BANK_MAP_JIEJI.put("NJBC","3042");//南京银行
		BANK_MAP_JIEJI.put("NBBC","3046");//宁波银行
		BANK_MAP_JIEJI.put("BOS","3059");//上海银行
		BANK_MAP_JIEJI.put("GZCB","3053");//广州银行
		BANK_MAP_JIEJI.put("SHRCB","3037");//上海农商银行
		BANK_MAP_JIEJI.put("BRCB","3060");//北京农商银行
		BANK_MAP_JIEJI.put("ZSBC","3043");//浙商银行
		BANK_MAP_JIEJI.put("BOHC","3034");//渤海银行
		BANK_MAP_JIEJI.put("PSBC","3038");//邮政储蓄银行
		BANK_MAP_JIEJI.put("BCCB","3032");//北京银行
		BANK_MAP_JIEJI.put("SNXS","3077");//深圳农村商业银行
		BANK_MAP_JIEJI.put("SDE","3087");//顺德农商行
		BANK_MAP_JIEJI.put("HSBANK","3041");//徽商银行
		BANK_MAP_JIEJI.put("BEAI","3033");//东亚银行
		BANK_MAP_JIEJI.put("CMBC","3006");//民生银行
		
		BANK_MAP_DAIJI.put("ICBC","4002");//工商银行
		BANK_MAP_DAIJI.put("ABC","4005");//农业银行
		BANK_MAP_DAIJI.put("BOC","4026");//中国银行
		BANK_MAP_DAIJI.put("CCB","4003");//建设银行
		BANK_MAP_DAIJI.put("CNCB","4039");//中信银行
		BANK_MAP_DAIJI.put("BOCOM","4020");//交通银行
		BANK_MAP_DAIJI.put("CEB","4022");//光大银行
		BANK_MAP_DAIJI.put("HXB","4050");//华夏银行
		BANK_MAP_DAIJI.put("SPDB","4004");//浦发银行
		BANK_MAP_DAIJI.put("CIB","4009");//兴业银行
		BANK_MAP_DAIJI.put("GDB","4036");//广发银行
		BANK_MAP_DAIJI.put("PAB","4035");//平安银行
		BANK_MAP_DAIJI.put("CMB","4001");//招商银行
		BANK_MAP_DAIJI.put("HKBCHINA","4086");//汉口银行
		BANK_MAP_DAIJI.put("NBBC","4046");//宁波银行
		BANK_MAP_DAIJI.put("BOS","4059");//上海银行
		BANK_MAP_DAIJI.put("GZCB","4053");//广州银行
		BANK_MAP_DAIJI.put("SHRCB","4037");//上海农商银行
		BANK_MAP_DAIJI.put("BRCB","4060");//北京农商银行
		BANK_MAP_DAIJI.put("ZSBC","4043");//浙商银行
		BANK_MAP_DAIJI.put("PSBC","4038");//邮政储蓄银行
		BANK_MAP_DAIJI.put("BCCB","4032");//北京银行
		BANK_MAP_DAIJI.put("SNXS","4077");//深圳农村商业银行
		BANK_MAP_DAIJI.put("HSBANK","4041");//徽商银行
		BANK_MAP_DAIJI.put("CMBC","4006");//民生银行
		
		BANK_MAP_PUB.put("CMB","6001");//招商银行
		BANK_MAP_PUB.put("ICBC","6002");//工商银行
		BANK_MAP_PUB.put("CCB","6003");//建设银行
		BANK_MAP_PUB.put("SPDB","6004");//浦发银行
		BANK_MAP_PUB.put("ABC","6005");//农业银行
		BANK_MAP_PUB.put("CMBC","6006");//民生银行
		BANK_MAP_PUB.put("CIB","6009");//兴业银行
		BANK_MAP_PUB.put("BOCOM","6020");//交通银行
		BANK_MAP_PUB.put("CEB","6022");//光大银行
		BANK_MAP_PUB.put("BOC","6026");//中国银行
		BANK_MAP_PUB.put("BCCB","6032");//北京银行
		BANK_MAP_PUB.put("BEAI","6033");//东亚银行
		BANK_MAP_PUB.put("PAB","6035");//平安银行
		BANK_MAP_PUB.put("GDB","6036");//广发银行
		BANK_MAP_PUB.put("SHRCB","6037");//上海农商银行
		BANK_MAP_PUB.put("PSBC","6038");//邮政储蓄银行
		BANK_MAP_PUB.put("CNCB","6039");//中信银行
		BANK_MAP_PUB.put("HXB","6050");//华夏银行
		BANK_MAP_PUB.put("BOS","6059");//上海银行
		
		BF_BANK_CODE.put("ICBC","ICBC");
		BF_BANK_CODE.put("ABC","ABC");
		BF_BANK_CODE.put("CCB","CCB");
		BF_BANK_CODE.put("BOC","BOC");
		BF_BANK_CODE.put("BOCOM","BCOM");
		BF_BANK_CODE.put("CIB","CIB");
		BF_BANK_CODE.put("CNCB","CITIC");
		BF_BANK_CODE.put("CEB","CEB");
		BF_BANK_CODE.put("PAB","PAB");
		BF_BANK_CODE.put("PSBC","PSBC");
		BF_BANK_CODE.put("SPDB","SPDB");
		BF_BANK_CODE.put("BOS","SHB");
		BF_BANK_CODE.put("CMBC","CMBC");
		BF_BANK_CODE.put("CMB","CMB");
		BF_BANK_CODE.put("GDB","GDB");
		BF_BANK_CODE.put("HXB","HXB");
		//Y：成功 F：失败 P：处理中 N：没有订单
		ORDER_QUERY_MSG.put("Y","成功");
		ORDER_QUERY_MSG.put("F","失败");
		ORDER_QUERY_MSG.put("P","处理中");
		ORDER_QUERY_MSG.put("N","没有订单");
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		Blog log = new Blog();
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BF"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));//
		try{
			String PayID=null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType))PayID=BANK_MAP_JIEJI.get(payOrder.bankcod);
			else if(PayConstant.CARD_BIN_TYPE_DAIJI.equals(payOrder.bankCardType))PayID=BANK_MAP_DAIJI.get(payOrder.bankcod);
			else if("4".equals(payOrder.bankCardType))PayID=BANK_MAP_DAIJI.get(payOrder.bankcod);//对公账号
			if(PayID == null)throw new Exception("BaoFoo无对应银行（"+payOrder.bankcod+"）");
			String OrderMoney=String.valueOf(payOrder.txamt);
			String TransID = payOrder.payordno;//商户订单号（不能重复）
			String TradeDate = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime);//下单日期		
			String MemberID = PayConstant.PAY_CONFIG.get("bf_web_member_id");//商户号
			String TerminalID = PayConstant.PAY_CONFIG.get("bf_web_terminal_id");//终端号
			String ProductName = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String Amount = "1";//商品数量
			String Username = prdOrder.custId;//支付用户名称
			String AdditionalInfo = "";//订单附加信息
			String PageUrl = prdOrder.returl;//页面跳转地址
			String ReturnUrl = PayConstant.PAY_CONFIG.get("bf_web_notify_url");//服务器底层通知地址
			String NoticeType = "0";//通知类型	
			String MARK = "|";
			String Signature =MD5.getDigest(MemberID+MARK+PayID+MARK+TradeDate+MARK+TransID+MARK+OrderMoney
					+MARK+PageUrl+MARK+ReturnUrl+MARK+NoticeType+MARK+PayConstant.PAY_CONFIG.get("bf_web_md5_key"));//MD5签名格式
		    log.info("收银台下单请求（宝付）=============\n"
		    		+"MemberID="+MemberID+"\n"
		    		+"TerminalID="+TerminalID+"\n"
		    		+"InterfaceVersion="+PayConstant.PAY_CONFIG.get("bf_web_interface_version") +"\n"
		    		+"KeyType=1\n"+"PayID="+PayID+"\n"
		    		+"TradeDate="+TradeDate+"\n"+"TransID="+TransID+"\n"+"OrderMoney="+OrderMoney+"\n"
		    		+"ProductName="+ProductName+"\n"+"Amount="+Amount+"\n"+"Username="+Username+"\n"
		    		+"AdditionalInfo="+AdditionalInfo+"\n"+"PageUrl="+PageUrl+"\n"+"ReturnUrl="+ReturnUrl+"\n"
		    		+"Signature="+Signature+"\n");
			request.setAttribute("PayID",PayID);
			request.setAttribute("TradeDate", TradeDate);
			request.setAttribute("TransID", TransID);
			request.setAttribute("OrderMoney",OrderMoney);
			request.setAttribute("ProductName", ProductName);
			request.setAttribute("PageUrl", PageUrl);
			request.setAttribute("ReturnUrl", ReturnUrl);
			request.setAttribute("Signature", Signature);
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
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_BF"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		Blog log = new Blog();
		try{
			String PayID=null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType))PayID=BANK_MAP_JIEJI.get(payOrder.bankcod);
			else if(PayConstant.CARD_BIN_TYPE_DAIJI.equals(payOrder.bankCardType))PayID=BANK_MAP_DAIJI.get(payOrder.bankcod);
			else if("4".equals(payOrder.bankCardType))PayID=BANK_MAP_DAIJI.get(payOrder.bankcod);//对公账号
			if(PayID == null)throw new Exception("BaoFoo无对应银行（"+payOrder.bankcod+"）");
			String OrderMoney=String.valueOf(payOrder.txamt);
			String TransID = payOrder.payordno;//商户订单号（不能重复）
			String TradeDate = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime);//下单日期		
			String MemberID = PayConstant.PAY_CONFIG.get("bf_web_member_id");//商户号
			String TerminalID = PayConstant.PAY_CONFIG.get("bf_web_terminal_id");//终端号
			String ProductName = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String Amount = "1";//商品数量
			String Username = prdOrder.custId;//支付用户名称
			String AdditionalInfo = "";//订单附加信息
			String PageUrl = prdOrder.returl;//页面跳转地址
			String ReturnUrl = PayConstant.PAY_CONFIG.get("bf_web_notify_url");//服务器底层通知地址
			String NoticeType = "0";//通知类型	
			String MARK = "|";
			String Signature =MD5.getDigest(MemberID+MARK+PayID+MARK+TradeDate+MARK+TransID+MARK+OrderMoney
					+MARK+PageUrl+MARK+ReturnUrl+MARK+NoticeType+MARK+PayConstant.PAY_CONFIG.get("bf_web_md5_key"));//MD5签名格式
		    log.info("商户接口下单请求（宝付）=============\n"
		    		+"MemberID="+MemberID+"\n"
		    		+"TerminalID="+TerminalID+"\n"
		    		+"InterfaceVersion="+PayConstant.PAY_CONFIG.get("bf_web_interface_version") +"\n"
		    		+"KeyType=1\n"+"PayID="+PayID+"\n"
		    		+"TradeDate="+TradeDate+"\n"+"TransID="+TransID+"\n"+"OrderMoney="+OrderMoney+"\n"
		    		+"ProductName="+ProductName+"\n"+"Amount="+Amount+"\n"+"Username="+Username+"\n"
		    		+"AdditionalInfo="+AdditionalInfo+"\n"+"PageUrl="+PageUrl+"\n"+"ReturnUrl="+ReturnUrl+"\n"
		    		+"Signature="+Signature+"\n");
			request.setAttribute("PayID",PayID);
			request.setAttribute("TradeDate", TradeDate);
			request.setAttribute("TransID", TransID);
			request.setAttribute("OrderMoney",OrderMoney);
			request.setAttribute("ProductName", ProductName);
			request.setAttribute("PageUrl", PageUrl);
			request.setAttribute("ReturnUrl", ReturnUrl);
			request.setAttribute("Signature", Signature);
	        new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	public void refund(PayRefund payRefund){
		
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		Blog log = new Blog();
		try{
			String MemberID = PayConstant.PAY_CONFIG.get("bf_web_member_id");//商户号
			String TerminalID = PayConstant.PAY_CONFIG.get("bf_web_terminal_id");//终端号
			String TransID = payOrder.payordno;//商户订单号（不能重复）
			String Md5key = PayConstant.PAY_CONFIG.get("bf_web_md5_key");//md5密钥（KEY）
			String MARK = "|";
			String Signature =MD5.getDigest(MemberID+MARK+TerminalID+MARK+TransID+MARK+Md5key);//MD5签名格式
			//返回结果,MD5({MemberID}|{TerminalID}|{TransID}|{CheckResult}|{succMoney}|{SuccTime}|{密钥}),100000178|10000001|2014010101010101|N|0|20141029184503|b4d6a91f6af4ce07c87420762d73d0e
			String req = "MemberID="+MemberID+"&TerminalID="+TerminalID+"&TransID="+TransID+"&MD5Sign="+Signature;
			log.info("宝付网关渠道补单请求报文:"+req);
			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("bf_web_gate_way_query"),
				req.getBytes("utf-8")),"utf-8");
		    log.info("宝付网关渠道补单响应报文" + res);
	        payOrder.actdat = new Date();
	        String [] es = Tools.split(res,"|");
	        if(es.length != 7)throw new Exception("宝付查询结果错误（"+res+"）");
	        MemberID = es[0];
	        TerminalID = es[1];
	        TransID = es[2];
	        String CheckResult = es[3];
	        String succMoney = es[4];
	        String SuccTime = es[5];
	        Signature = es[6];
	        //MD5({MemberID}|{TerminalID}|{TransID}|{CheckResult}|{succMoney}|{SuccTime}|{密钥})
	        if(Signature.equals(MD5.getDigest(MemberID+"|"+TerminalID+"|"+TransID+"|"+CheckResult+"|"+succMoney+"|"
	        		+SuccTime+"|"+PayConstant.PAY_CONFIG.get("bf_web_md5_key")))){
		        if("Y".equals(CheckResult)){
		        	payOrder.ordstatus="01";//支付成功
		        	payOrder.bankjrnno = TransID;
		        	new NotifyInterface().notifyMer(payOrder);//支付成功
		        } else throw new Exception("支付渠道状态："+CheckResult+(
		        	ORDER_QUERY_MSG.get(CheckResult)==null?"（未知结果）":"（"+ORDER_QUERY_MSG.get(CheckResult)+"）"));
	        } else throw new Exception("渠道验签失败");
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 快捷支付下单入口。
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String quickPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		try{
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);
			if(cardBin==null)throw new Exception("不支持的卡号");
			//根据卡号查询绑定信息
			PayTranUserQuickCardDAO payTranUserQuickCardDAO = new PayTranUserQuickCardDAO();
			PayTranUserQuickCard  payTranUserQuickCard = payTranUserQuickCardDAO.getBindCardByNo(payRequest.cardNo);
			//支付卡号未进行绑定，进行绑定操作。
			if(payTranUserQuickCard==null){
				payTranUserQuickCard = new PayTranUserQuickCard();
				payTranUserQuickCard.id=Tools.getUniqueIdentify();
				payTranUserQuickCard.credentialType=payRequest.credentialType;
				payTranUserQuickCard.credentialNo=payRequest.credentialNo;
				payTranUserQuickCard.cardNo=payRequest.cardNo;
				payTranUserQuickCard.cvv2=payRequest.cvv2;
				payTranUserQuickCard.validPeriod=payRequest.validPeriod;
				payTranUserQuickCard.bankId=cardBin.bankCode;
				payTranUserQuickCard.name=payRequest.userName;
				payTranUserQuickCard.mobileNo=payRequest.userMobileNo;
				payTranUserQuickCard.merchantId=payRequest.merchantId;
				payTranUserQuickCard.payerId=payRequest.payerId;
				payTranUserQuickCardDAO.addPayTranUserQuickCard(payTranUserQuickCard);
				//调用宝付绑卡业务进行绑卡,并处理绑卡结果。
				String bindCardResultMeg=  bindCard(request,payRequest,prdOrder,payOrder);
				JSONObject bindCardjsonObject = JSONObject.fromObject(bindCardResultMeg);
					if("0000".equals(bindCardjsonObject.getString("resp_code"))){//绑定成功，更新绑卡状态.
						payTranUserQuickCard.status="1";
						payTranUserQuickCard.bindId=bindCardjsonObject.getString("bind_id");
						payTranUserQuickCardDAO.updatePayTranUserQuickCard(payTranUserQuickCard);
					} else {//绑定失败直接返回宝付返回错误信息。
						return  "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
							"respCode=\"-1\" respDesc=\"BF-"+bindCardjsonObject.getString("resp_msg")+"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
					}
			} else {
				//如果该卡绑定过，但是没有成功，需要再次进行绑卡操作。
				if(!payTranUserQuickCard.status.equals("1")
						||payTranUserQuickCard.bindId==null||payTranUserQuickCard.bindId.length()==0){
					//调用宝付绑卡业务进行绑卡,并处理绑卡结果。
					String bindCardResultMegtemp=  bindCard(request,payRequest,prdOrder,payOrder);
					JSONObject bindCardjsonObjecttmp = JSONObject.fromObject(bindCardResultMegtemp);
					//绑定成功，更新绑卡状态.
					if("0000".equals(bindCardjsonObjecttmp.getString("resp_code"))){
						payTranUserQuickCard.status="1";
						payTranUserQuickCard.credentialType=payRequest.credentialType;
						payTranUserQuickCard.credentialNo=payRequest.credentialNo;
						payTranUserQuickCard.name=payRequest.userName;
						payTranUserQuickCard.mobileNo=payRequest.userMobileNo;
						payTranUserQuickCard.bindId=bindCardjsonObjecttmp.getString("bind_id");
						payTranUserQuickCardDAO.updatePayTranUserQuickCard(payTranUserQuickCard);
					}//绑定失败直接返回宝付返回错误信息。
					else{
						return  "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
							"respCode=\"-1\" respDesc=\"BF-"+bindCardjsonObjecttmp.getString("resp_msg")+"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
					}
				}
			}
			//执行快捷下单请求，并接受处理结果。
			String quickPayResultMeg = quickPayRequest(request,payRequest,prdOrder,payOrder,payTranUserQuickCard);
			JSONObject quickPayjsonObject = JSONObject.fromObject(quickPayResultMeg);
			if("0000".equals(quickPayjsonObject.getString("resp_code"))){
				//下单成功后保存宝付返回的宝付业务流水号，用于确认订单使用。
				payOrder.bankjrnno=quickPayjsonObject.getString("business_no");
				new PayOrderDAO().updateOrderBankjrnno(payOrder);
				//返回支付成功结果。
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" " +
					"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
					"respCode=\"000\" respDesc=\"操作成功\" " +
					"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			} else {//直接返回支付失败错误信息。
				return  "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" " +
					"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
					"respCode=\"-1\" respDesc=\"BF-"+quickPayjsonObject.getString("resp_msg")+"\" " +
					"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 绑卡
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String  bindCard(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		
		Blog log = new Blog();
		try {
	        //需要加密的参数。
	        Map<String,Object> jsonObjectMap = new HashMap<String,Object>();
	        jsonObjectMap.put("txn_sub_type", "01");//子交易类。
	        jsonObjectMap.put("biz_type", "0000");//子交易类。
	        jsonObjectMap.put("terminal_id",PayConstant.PAY_CONFIG.get("bf_terminal_id"));//终端号。
	        jsonObjectMap.put("member_id", PayConstant.PAY_CONFIG.get("bf_member_id"));//商户号。
	        jsonObjectMap.put("trans_serial_no", Tools.getUniqueIdentify());//商户流水。
	        jsonObjectMap.put("trans_id",payOrder.payordno);//商户订单号，唯一的。
	        jsonObjectMap.put("acc_no", payRequest.cardNo);//卡号
	        jsonObjectMap.put("id_card_type", payRequest.credentialType==null||
					payRequest.credentialType.length()==0?"01":payRequest.credentialType);//证件类型固定01（身份证）
	        jsonObjectMap.put("id_card",payRequest.credentialNo);//身份证号
	        jsonObjectMap.put("id_holder", payRequest.userName);//姓名
	        jsonObjectMap.put("mobile", payRequest.userMobileNo);//手机号。
	        jsonObjectMap.put("valid_date", payRequest.validPeriod);
	        jsonObjectMap.put("valid_no", payRequest.cvv2);
	        jsonObjectMap.put("pay_code", BF_BANK_CODE.get(payOrder.bankcod));//银行编码。
	        jsonObjectMap.put("trade_date",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//订单时间
	        jsonObjectMap.put("additional_info", "");
	        jsonObjectMap.put("req_reserved", "");
	        JSONObject jsonObjectFromMap = JSONObject.fromObject(jsonObjectMap);
			String jsonData = jsonObjectFromMap.toString();
			log.info("宝付绑卡需要加密的参数: "+jsonData);
			//使用私钥加密数据。
			String data_content = RsaCodingUtil.encryptByPriPfxFile(SecurityUtil.Base64Encode(jsonData));
			//请求绑卡发出的参数。
			Map<String,String> HeadPostParam = new HashMap<String,String>();
			HeadPostParam.put("version", PayConstant.PAY_CONFIG.get("bf_version"));
	        HeadPostParam.put("member_id",PayConstant.PAY_CONFIG.get("bf_member_id"));
	        HeadPostParam.put("terminal_id", PayConstant.PAY_CONFIG.get("bf_terminal_id"));
	        HeadPostParam.put("txn_type", "0431");
	        HeadPostParam.put("txn_sub_type", "01");
	        HeadPostParam.put("data_type", "json");
			HeadPostParam.put("data_content", data_content);
			//封装发送的参数
			String params = "";
			Iterator<String> it = HeadPostParam.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				params = params+key+"="+HeadPostParam.get(key)+ "&";
			}
			params = params.substring(0,params.length()-1);
			log.info("宝付绑卡请求报文："+params.toString());
			//发送绑卡请求，并接受结果。
			String PostString  = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("bf_pay_request_url"),
					params.getBytes("utf-8")),"utf-8");
			//使用宝付公钥解密返回数据。
			String PostStringtemp = RsaCodingUtil.decryptByPubCerFile(PostString);
			if(PostStringtemp.isEmpty()){
				throw new Exception("解密数据出错!");
			}
			//使用base64解密还原返回数据。
			log.info("宝付绑卡响应报文: "+SecurityUtil.Base64Decode(PostStringtemp));
			return SecurityUtil.Base64Decode(PostStringtemp);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 快捷支付--提交请求--获取返回数据。
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String quickPayRequest(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,
			PayTranUserQuickCard  payTranUserQuickCard) throws Exception{
		Blog log = new Blog();
		try {
	        //需要加密的参数。
	        Map<String,String> jsonObjectMap = new HashMap<String,String>();
	        jsonObjectMap.put("txn_sub_type", "15");//子交易类。
	        jsonObjectMap.put("biz_type", "0000");//子交易类。
	        jsonObjectMap.put("terminal_id",PayConstant.PAY_CONFIG.get("bf_terminal_id"));//终端号。
	        jsonObjectMap.put("member_id", PayConstant.PAY_CONFIG.get("bf_member_id"));//商户号。
	        jsonObjectMap.put("trans_serial_no", Tools.getUniqueIdentify());//商户流水。
	        jsonObjectMap.put("trans_id",payOrder.payordno);//商户订单号，唯一的。
	        jsonObjectMap.put("bind_id",payTranUserQuickCard.bindId);//绑卡唯一标示。//
	        jsonObjectMap.put("txn_amt", String.valueOf(payRequest.merchantOrderAmt));//交易金额。单位：分。
	        jsonObjectMap.put("trade_date",new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime));//订单时间
	        jsonObjectMap.put("additional_info", "");
	        jsonObjectMap.put("req_reserved", "");
	        JSONObject jsonObjectFromMap = JSONObject.fromObject(jsonObjectMap);
			String jsonData = jsonObjectFromMap.toString();
			String str = jsonData.substring(0,jsonData.length()-1)+",\"risk_content\":{\"client_ip\":"+"\""+PayConstant.PAY_CONFIG.get("client_ip")+"\"}}";
			log.info("宝付快捷下单请求参数（签名）:  "+str);
			//使用私钥加密数据。
			String data_content = RsaCodingUtil.encryptByPriPfxFile(SecurityUtil.Base64Encode(str));
			//请求绑卡发出的参数。
			Map<String,String> HeadPostParam = new HashMap<String,String>();
			HeadPostParam.put("version", PayConstant.PAY_CONFIG.get("bf_version"));
	        HeadPostParam.put("member_id",PayConstant.PAY_CONFIG.get("bf_member_id"));
	        HeadPostParam.put("terminal_id", PayConstant.PAY_CONFIG.get("bf_terminal_id"));
	        HeadPostParam.put("txn_type", "0431");
	        HeadPostParam.put("txn_sub_type", "15");
	        HeadPostParam.put("data_type", "json");
			HeadPostParam.put("data_content", data_content);
			String params = "";
			Iterator<String> it = HeadPostParam.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				params = params+key+"="+HeadPostParam.get(key)+ "&";
			}
			params = params.substring(0,params.length()-1);
			log.info("宝付快捷下单请求参数:  "+params);
			String PostString  = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("bf_pay_request_url"), 
					params.getBytes("utf-8")),"utf-8");
			//使用宝付公钥解密返回数据。
			String PostStringtemp = RsaCodingUtil.decryptByPubCerFile(PostString);
			if(PostStringtemp==null)throw new Exception("解密数据出错!");
			//使用base64解密还原返回数据。
			log.info("宝付快捷下单响应的数据:  "+SecurityUtil.Base64Decode(PostStringtemp));
			return SecurityUtil.Base64Decode(PostStringtemp);	
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	/**
	 * 快捷支付确认订单。
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String certPayConfirm(PayRequest payRequest) throws Exception{
		Blog log = new Blog();
		try {
			 //需要加密的参数。
	        Map<String,Object> jsonObjectMap = new HashMap<String,Object>();
	        jsonObjectMap.put("txn_sub_type", "16");//子交易类。
	        jsonObjectMap.put("biz_type", "0000");//子交易类。
	        jsonObjectMap.put("terminal_id", PayConstant.PAY_CONFIG.get("bf_terminal_id"));//终端号。
	        jsonObjectMap.put("member_id", PayConstant.PAY_CONFIG.get("bf_member_id"));//商户号。
	        jsonObjectMap.put("trans_serial_no", Tools.getUniqueIdentify());//商户流水。
	        jsonObjectMap.put("business_no", payRequest.payOrder.bankjrnno);//宝付支付订单流水。
	        jsonObjectMap.put("sms_code", payRequest.checkCode);//短信验证码
	        jsonObjectMap.put("trade_date",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//订单时间
	        jsonObjectMap.put("additional_info", "");
	        jsonObjectMap.put("req_reserved", "");
	        JSONObject jsonObjectFromMap = JSONObject.fromObject(jsonObjectMap);
			String jsonData = jsonObjectFromMap.toString();
			log.info("宝付快捷确认支付需要加密的参数: "+jsonData);
			//使用私钥加密数据。
			String data_content = RsaCodingUtil.encryptByPriPfxFile(SecurityUtil.Base64Encode(jsonData));
			//请求绑卡发出的参数。
			Map<String,String> HeadPostParam = new HashMap<String,String>();
			HeadPostParam.put("version", PayConstant.PAY_CONFIG.get("bf_version"));
	        HeadPostParam.put("member_id",PayConstant.PAY_CONFIG.get("bf_member_id") );
	        HeadPostParam.put("terminal_id", PayConstant.PAY_CONFIG.get("bf_terminal_id"));
	        HeadPostParam.put("txn_type", "0431");
	        HeadPostParam.put("txn_sub_type", "16");
	        HeadPostParam.put("data_type", "json");
			HeadPostParam.put("data_content", data_content);
			String params = "";
			Iterator<String> it = HeadPostParam.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				params = params+key+"="+HeadPostParam.get(key)+ "&";
			}
			params = params.substring(0,params.length()-1);
			log.info("宝付快捷确认请求报文；"+params.toString());
			String PostString  = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("bf_pay_request_url"),
					params.getBytes("utf-8")),"utf-8");
			//使用宝付公钥解密返回数据。
			String PostStringtemp = RsaCodingUtil.decryptByPubCerFile(PostString);
			if(PostStringtemp.isEmpty()){
				throw new Exception("解密数据出错!");
			}
			//使用base64解密还原返回数据。
			String PostStringsrc = SecurityUtil.Base64Decode(PostStringtemp);
			log.info("宝付快捷确认响应报文： "+PostStringsrc);
			return PostStringsrc;
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
	public void quickPayChannelQuery(PayOrder payOrder) throws Exception{
		Blog log = new Blog();
		try{
			 //需要加密的参数。
	        Map<String,Object> jsonObjectMap = new HashMap<String,Object>();
	        jsonObjectMap.put("txn_sub_type", "06");//子交易类。
	        jsonObjectMap.put("biz_type", "0000");//子交易类。
	        jsonObjectMap.put("terminal_id", PayConstant.PAY_CONFIG.get("bf_terminal_id"));//终端号。
	        jsonObjectMap.put("member_id", PayConstant.PAY_CONFIG.get("bf_member_id"));//商户号。
	        jsonObjectMap.put("trans_serial_no", Tools.getUniqueIdentify());//商户流水。
	        jsonObjectMap.put("orig_trans_id", payOrder.payordno);//支付订单号。
	        jsonObjectMap.put("trade_date",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//订单时间
	        jsonObjectMap.put("additional_info", "");
	        jsonObjectMap.put("req_reserved", "");
	        JSONObject jsonObjectFromMap = JSONObject.fromObject(jsonObjectMap);
			String jsonData = jsonObjectFromMap.toString();
			log.info("宝付快捷支付查询需要加密的参数: "+jsonData);
			//使用私钥加密数据。
			String data_content = RsaCodingUtil.encryptByPriPfxFile(SecurityUtil.Base64Encode(jsonData));
			//请求绑卡发出的参数。
			Map<String,String> HeadPostParam = new HashMap<String,String>();
			HeadPostParam.put("version", PayConstant.PAY_CONFIG.get("bf_version"));
	        HeadPostParam.put("member_id",PayConstant.PAY_CONFIG.get("bf_member_id") );
	        HeadPostParam.put("terminal_id", PayConstant.PAY_CONFIG.get("bf_terminal_id"));
	        HeadPostParam.put("txn_type", "0431");
	        HeadPostParam.put("txn_sub_type", "06");
	        HeadPostParam.put("data_type", "json");
			HeadPostParam.put("data_content", data_content);
			String params = "";
			Iterator<String> it = HeadPostParam.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				params = params+key+"="+HeadPostParam.get(key)+ "&";
			}
			params = params.substring(0,params.length()-1);
			log.info("宝付快捷查询请求报文:"+params.toString());
			String PostString  = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("bf_pay_request_url"),
					params.getBytes("utf-8")),"utf-8");
			//使用宝付公钥解密返回数据。
			String PostStringtemp = RsaCodingUtil.decryptByPubCerFile(PostString);
			if(PostStringtemp.isEmpty()){
				throw new Exception("解密数据出错!");
			}
			//使用base64解密还原返回数据。
			String PostStringsrc = SecurityUtil.Base64Decode(PostStringtemp);
			log.info("宝付快捷支付查询响应报文： "+PostStringsrc);
			JSONObject jsonObjecttmp = JSONObject.fromObject(PostStringsrc);
		    if("0000".equals(jsonObjecttmp.getString("resp_code"))){
		        payOrder.ordstatus="01";//支付成功
		        payOrder.bankjrnno = jsonObjecttmp.getString("resp_code");
		        new NotifyInterface().notifyMer(payOrder);//支付成功
		    } else throw new Exception("支付渠道状态："+jsonObjecttmp.getString("resp_code")+(
		    		jsonObjecttmp.getString("resp_code")==null?"（未知结果）":"（"+jsonObjecttmp.getString("resp_msg")+"）"));
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
