package com.pay.merchantinterface.service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import util.DataTransUtil;

import com.PayConstant;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.gfb.GopayUtils;
import com.third.gfb.SignVerifyUtil;

/**
 * 国付宝
 * @author lqq
 */
public class GFBPayService {

	private static final Log log = LogFactory.getLog(GFBPayService.class);
	
	public static Map <String,String> PAY_BANK_MAP_JIEJI = new HashMap <String,String>();//借记卡银行码映射（网关）
	
	static {
		//网银支持银行
		PAY_BANK_MAP_JIEJI.put("ICBC", "ICBC");//1工商银行
		PAY_BANK_MAP_JIEJI.put("CCB", "CCB");//2建设银行
		PAY_BANK_MAP_JIEJI.put("ABC", "ABC");//3中国农业银行
		PAY_BANK_MAP_JIEJI.put("HXB", "HXBC");//4华夏银行  ----------
		PAY_BANK_MAP_JIEJI.put("PAB", "PAB");//5平安银行  
		PAY_BANK_MAP_JIEJI.put("BCCB", "BOBJ");//6北京银行  ---------
		PAY_BANK_MAP_JIEJI.put("BOC", "BOC");//7中国银行
		PAY_BANK_MAP_JIEJI.put("CNCB", "CITIC");//8中信银行  
		PAY_BANK_MAP_JIEJI.put("CEB", "CEB");//9光大银行  
		PAY_BANK_MAP_JIEJI.put("GDB", "GDB");//10广发银行  
		PAY_BANK_MAP_JIEJI.put("CIB", "CIB");//11兴业银行  
		PAY_BANK_MAP_JIEJI.put("SPDB", "SPDB");//12上海浦东发展银行  
		PAY_BANK_MAP_JIEJI.put("PSBC", "PSBC");//13中国邮政储蓄银行  
		PAY_BANK_MAP_JIEJI.put("BOCOM", "BOCOM");//14交通银行
		PAY_BANK_MAP_JIEJI.put("BOS", "BOS");//15上海银行  
		PAY_BANK_MAP_JIEJI.put("NBBC", "NBCB");//16宁波银行  -----
		PAY_BANK_MAP_JIEJI.put("CMB", "CMB");//17招商银行
		//快捷、代付支持银行
	}
	
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GFB"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));//银行卡类型
		try {
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) bankCode = PAY_BANK_MAP_JIEJI.get(payOrder.bankcod);
			if (bankCode == null) throw new Exception("国付宝无对应银行（"+payOrder.bankcod+"）");
			Map<String, String> paramsMap = new LinkedHashMap<String, String>();
			//拼接加密字段
			String version = "2.2";//1：版本号
			String charset = "2";//2：1GBK,2UTF8
			String language = "1";//3：1中文
			String signType = "3";//3RSA加密
			String tranCode = "8888";//网关支付8888
			String merchantID = PayConstant.PAY_CONFIG.get("GFB_MERNO");//国付宝商户号
			String merOrderNum = payOrder.payordno;//订单号
			String tranAmt = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额
			String feeAmt = "";
			String currencyType = "156";//币种
			String frontMerUrl = "";//商户前台通知地址
			String backgroundMerUrl = PayConstant.PAY_CONFIG.get("GFB_NOTIFYURL");//商户后台通知地址
			String tranDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易时间
			String virCardNoIn = PayConstant.PAY_CONFIG.get("GFB_VIRCARDNOIN");//国付宝转入账户
			String tranIP = request.getRemoteAddr();//用户浏览器ip
			String isRepeatSubmit = "0";//是否允许订单重复提交,不允许重复
			String goodsName = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String goodsDetail = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品详情
			String buyerName = "";//买方姓名
			String buyerContact = "";//买方联系方式
			String merRemark1 = "";//扩展字段1
			String merRemark2 = "";//扩展字段2
								//银行代码
			String VerficationCode = PayConstant.PAY_CONFIG.get("GFB_MER_JYM");
			String gopayServerTime = GopayUtils.getGopayServerTime();
			String userType = "1";//用户类型,个人网银
			String plain = "version=[" + version + "]tranCode=[" + tranCode + "]merchantID=[" + merchantID + "]merOrderNum=[" + merOrderNum + "]tranAmt=[" + tranAmt + "]feeAmt=[" + feeAmt+ "]tranDateTime=[" + tranDateTime + "]frontMerUrl=[" + frontMerUrl + "]backgroundMerUrl=[" + backgroundMerUrl + "]orderId=[]gopayOutOrderId=[]tranIP=[" + tranIP + "]respCode=[]gopayServerTime=[" + gopayServerTime + "]VerficationCode=[" + VerficationCode + "]";
			log.info("国付宝网银待签名数据："+plain);
			String priKeyStr = PayConstant.PAY_CONFIG.get("GFB_MERC_PRIVATEKEY");
			String signValue = SignVerifyUtil.sign(plain, priKeyStr);//密文串
			log.info("国付宝网银签名数据："+signValue);
			
			paramsMap.put("version", version);
			paramsMap.put("charset", charset);
			paramsMap.put("language", language);
			paramsMap.put("signType", signType);
			paramsMap.put("tranCode", tranCode);
			paramsMap.put("merchantID", merchantID);
			paramsMap.put("merOrderNum", merOrderNum);
			paramsMap.put("tranAmt", tranAmt);
			paramsMap.put("feeAmt", feeAmt);
			paramsMap.put("currencyType", currencyType);
			paramsMap.put("frontMerUrl", frontMerUrl);
			paramsMap.put("backgroundMerUrl", backgroundMerUrl);
			paramsMap.put("tranDateTime", tranDateTime);
			paramsMap.put("virCardNoIn", virCardNoIn);
			paramsMap.put("tranIP", tranIP);
			paramsMap.put("isRepeatSubmit", isRepeatSubmit);
			paramsMap.put("goodsName", goodsName);
			paramsMap.put("goodsDetail", goodsDetail);
			paramsMap.put("buyerName", buyerName);
			paramsMap.put("buyerContact", buyerContact);
			paramsMap.put("merRemark1", merRemark1);
			paramsMap.put("merRemark2", merRemark2);
			paramsMap.put("bankCode", bankCode);
			paramsMap.put("userType", userType);
			paramsMap.put("signValue", signValue);
			paramsMap.put("gopayServerTime", gopayServerTime);
			request.setAttribute("paramsMap", paramsMap);
			log.info("国付宝网银下单请求数据："+paramsMap.toString());
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
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_GFB"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;//卡类型
		try {
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) bankCode = PAY_BANK_MAP_JIEJI.get(payOrder.bankcod);
			if (bankCode == null) throw new Exception("国付宝无对应银行（"+payOrder.bankcod+"）");
			Map<String, String> paramsMap = new LinkedHashMap<String, String>();
			//拼接加密字段
			String version = "2.2";//1：版本号
			String charset = "2";//2：1GBK,2UTF8
			String language = "1";//3：1中文
			String signType = "3";//3RSA加密
			String tranCode = "8888";//网关支付8888
			String merchantID = PayConstant.PAY_CONFIG.get("GFB_MERNO");//国付宝商户号
			String merOrderNum = payOrder.payordno;//订单号
			String tranAmt = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额
			String feeAmt = "";
			String currencyType = "156";//币种
			String frontMerUrl = "";//商户前台通知地址
			String backgroundMerUrl = PayConstant.PAY_CONFIG.get("GFB_NOTIFYURL");//商户后台通知地址
			String tranDateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//交易时间
			String virCardNoIn = PayConstant.PAY_CONFIG.get("GFB_VIRCARDNOIN");//国付宝转入账户
			String tranIP = request.getRemoteAddr();//用户浏览器ip
			String isRepeatSubmit = "0";//是否允许订单重复提交
			String goodsName = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String goodsDetail = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品详情
			String buyerName = "";//买方姓名
			String buyerContact = "";//买方联系方式
			String merRemark1 = "";//扩展字段1
			String merRemark2 = "";//扩展字段2
								//银行代码
			String VerficationCode = PayConstant.PAY_CONFIG.get("GFB_MER_JYM");
			String gopayServerTime = GopayUtils.getGopayServerTime();
			String userType = "1";//用户类型,个人网银
			String plain = "version=[" + version + "]tranCode=[" + tranCode + "]merchantID=[" + merchantID + "]merOrderNum=[" + merOrderNum + "]tranAmt=[" + tranAmt + "]feeAmt=[" + feeAmt+ "]tranDateTime=[" + tranDateTime + "]frontMerUrl=[" + frontMerUrl + "]backgroundMerUrl=[" + backgroundMerUrl + "]orderId=[]gopayOutOrderId=[]tranIP=[" + tranIP + "]respCode=[]gopayServerTime=[" + gopayServerTime + "]VerficationCode=[" + VerficationCode + "]";
			log.info("国付宝网银待签名数据："+plain);
			String priKeyStr = PayConstant.PAY_CONFIG.get("GFB_MERC_PRIVATEKEY");
			String signValue = SignVerifyUtil.sign(plain, priKeyStr);//密文串
			log.info("国付宝网银签名数据："+signValue);
			
			paramsMap.put("version", version);
			paramsMap.put("charset", charset);
			paramsMap.put("language", language);
			paramsMap.put("signType", signType);
			paramsMap.put("tranCode", tranCode);
			paramsMap.put("merchantID", merchantID);
			paramsMap.put("merOrderNum", merOrderNum);
			paramsMap.put("tranAmt", tranAmt);
			paramsMap.put("feeAmt", feeAmt);
			paramsMap.put("currencyType", currencyType);
			paramsMap.put("frontMerUrl", frontMerUrl);
			paramsMap.put("backgroundMerUrl", backgroundMerUrl);
			paramsMap.put("tranDateTime", tranDateTime);
			paramsMap.put("virCardNoIn", virCardNoIn);
			paramsMap.put("tranIP", tranIP);
			paramsMap.put("isRepeatSubmit", isRepeatSubmit);
			paramsMap.put("goodsName", goodsName);
			paramsMap.put("goodsDetail", goodsDetail);
			paramsMap.put("buyerName", buyerName);
			paramsMap.put("buyerContact", buyerContact);
			paramsMap.put("merRemark1", merRemark1);
			paramsMap.put("merRemark2", merRemark2);
			paramsMap.put("bankCode", bankCode);
			paramsMap.put("userType", userType);
			paramsMap.put("signValue", signValue);
			paramsMap.put("gopayServerTime", gopayServerTime);
			request.setAttribute("paramsMap", paramsMap);
			log.info("国付宝网银下单请求数据："+paramsMap.toString());
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 渠道补单（查单）
	 */
	public void channelQuery(PayOrder payOrder, String payType) throws Exception{
		//涉及到的参数
		String Version = "1.0";//版本号
		String TranCode = "BQ01";//交易代码
		String MerId = PayConstant.PAY_CONFIG.get("GFB_MERNO");//国付宝商户号
		String MerAcctId = PayConstant.PAY_CONFIG.get("GFB_VIRCARDNOIN");//国付宝商户账户ID
		String Charset = "2";//报文字符集
		String SignType = "3";//加密方式，证书
		String SignValue = "";//加密字符串
		String QryMerOrderId = payOrder.payordno;//商户订单号
		String QryGopayOrderId = "";//国付宝订单号
		String QryTranCode = "";//被查询订单的交易代码
		if ("01".equals(payType)) {//网银
			QryTranCode = "11";
		}
		String GopayTxnTmStart = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime).substring(0,8)+"000000";
		String GopayTxnTmEnd = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime).substring(0,8)+"235959";
		String TxnStat = "A";//查询交易状态，A全部，S成功，P进行中
		String PageNum = "1";//页码
		String VerficationCode = PayConstant.PAY_CONFIG.get("GFB_MER_JYM");
		//拼接加密数据
		Map<String,String> paramMap = new LinkedHashMap<String, String>();
		paramMap.put("Version",Version );
		paramMap.put("TranCode", TranCode);
		paramMap.put("MerId", MerId);
		paramMap.put("MerAcctId", MerAcctId);
		paramMap.put("QryGopayOrderId", QryGopayOrderId);
		paramMap.put("QryTranCode", QryTranCode);
		paramMap.put("GopayTxnTmStart", GopayTxnTmStart);
		paramMap.put("GopayTxnTmEnd", GopayTxnTmEnd);
		paramMap.put("PageNum", PageNum);
		paramMap.put("VerficationCode", VerficationCode);
		String plain = "";
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			plain += entry.getKey()+"=["+entry.getValue()+"]";
		}
		log.info("国付宝网银待签名数据："+plain);
		String priKeyStr = PayConstant.PAY_CONFIG.get("GFB_MERC_PRIVATEKEY");
		String signValue = SignVerifyUtil.sign(plain, priKeyStr);//密文串
		log.info("国付宝网银签名数据："+signValue);
		SignValue = signValue;
		String tmp = "";//查单字符串
		//拼接请求参数
		Map<String,String> queryMap = new LinkedHashMap<String, String>();
		queryMap.put("Version", Version);
		queryMap.put("TranCode", TranCode);
		queryMap.put("MerId", MerId);
		queryMap.put("MerAcctId", MerAcctId);
		queryMap.put("Charset", Charset);
		queryMap.put("SignType", SignType);
		queryMap.put("QryMerOrderId", QryMerOrderId);
		queryMap.put("QryGopayOrderId", QryGopayOrderId);
		queryMap.put("QryTranCode", QryTranCode);
		queryMap.put("GopayTxnTmStart", GopayTxnTmStart);
		queryMap.put("GopayTxnTmEnd", GopayTxnTmEnd);
		queryMap.put("TxnStat", TxnStat);
		queryMap.put("PageNum", PageNum);
		queryMap.put("SignValue", SignValue);
		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
			tmp += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),"utf-8")+"&";
		}
		tmp = tmp.substring(0, tmp.length()-1);
		log.info("国付宝查单请求数据："+tmp);
		String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("GFB_GATEWAYURL"), tmp.getBytes("utf-8")),"utf-8");
		log.info("国付宝查单返回数据："+resData);
		Document document = DocumentHelper.parseText(resData);
		Element root = document.getRootElement();
		String SysRtnCd = root.element("SysRtnInf").element("SysRtnCd").getText();//系统响应码
		String SysRtnTm = root.element("SysRtnInf").element("SysRtnTm").getText();//系统响应时间
		String MerId2 = root.element("ReqInf").element("MerId").getText();//国付宝 商户号
		String MerAcctId2 = root.element("ReqInf").element("MerAcctId").getText();//国付宝 商户账号ID
		String QryGopayOrderId2 = root.element("ReqInf").element("QryGopayOrderId").getText();//国付宝订单号
		String QryTranCode2 = root.element("ReqInf").element("QryTranCode").getText();//查询类型
		String GopayTxnTmStart2 = root.element("ReqInf").element("GopayTxnTmStart").getText();//查询开始时间
		String GopayTxnTmEnd2 = root.element("ReqInf").element("GopayTxnTmEnd").getText();//查询结束时间
		String PageNum2 = root.element("ReqInf").element("PageNum").getText();//页码
		String signValueFromGopay = root.element("SgnInf").element("SignValue").getText();
		String TxnSet = root.element("BizInf").element("TxnSet").element("TxnInf").element("BizStsCd").getText();//订单状态
		String plain2 = "SysRtnCd=["+SysRtnCd+"]SysRtnTm=["+SysRtnTm+"]MerId=["+MerId2+"]MerAcctId=["+MerAcctId2+"]QryGopayOrderId=["+QryGopayOrderId2+"]QryTranCode=["+QryTranCode2+"]GopayTxnTmStart=["+GopayTxnTmStart2+"]GopayTxnTmEnd=["
				+GopayTxnTmEnd2+"]PageNum=["+PageNum2+"]VerficationCode=["+VerficationCode+"]";
		String pubKeyStr = PayConstant.PAY_CONFIG.get("GFB_GFB_PUBLICKEY");//国付宝服务器公钥
		log.info("待验签数据："+plain2);
		if(SignVerifyUtil.verify(plain2, signValueFromGopay, pubKeyStr)){
			log.info("国付宝查询订单：验签成功");
			if("0000".equals(SysRtnCd)){
				if ("S".equals(TxnSet)) {
					payOrder.ordstatus="01";//支付成功
					new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("F".equals(TxnSet)){
					payOrder.ordstatus="02";//支付失败
					new NotifyInterface().notifyMer(payOrder);//支付成功
				}
			} 
		}else new Exception("国付宝查单验签失败");
	}
	/**
	 * 代付。
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingleNew(PayRequest payRequest) throws Exception{
		 try {
				 if("1".equals(payRequest.receivePayType)){//代付业务。
					 PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
				     PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
				     String version ="1.2";
		    		String tranCode = "4025";
		    		String merchantEncode = "2";
					String signType="3";
					String customerId=PayConstant.PAY_CONFIG.get("GFB_MERNO");
					String payAcctId=PayConstant.PAY_CONFIG.get("GFB_VIRCARDNOIN");
					String merOrderNum =  rp.id;
					String merURL=PayConstant.PAY_CONFIG.get("GFB_DF_NOTIFYURL");
					String tranAmt=String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d);
					String recvBankAcctName =payRequest.accName;
					String recvBankName=payRequest.bankId;
					String recvBankAcctNum=payRequest.accNo;
					String tranDateTime=new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
					String approve="2";
					String corpPersonFlag="2";
					String VerficationCode=PayConstant.PAY_CONFIG.get("GFB_MER_JYM");
					String gopayServerTime=tranDateTime;
					
					Map<String,String> map = new HashMap<String,String>();
			        map.put("version",version);
			        map.put("tranCode",tranCode);
			        map.put("merchantEncode",merchantEncode);
			        map.put("signType",signType);
			        map.put("customerId", customerId);
			        map.put("payAcctId",payAcctId);
			        map.put("merOrderNum", merOrderNum);
			        map.put("merURL",merURL);
			        map.put("tranAmt",tranAmt);
			        map.put("recvBankAcctName",recvBankAcctName);
			        map.put("recvBankName",recvBankName);
			        map.put("recvBankAcctNum",recvBankAcctNum);
			        map.put("tranDateTime",tranDateTime);
			        map.put("approve",approve);
			        map.put("corpPersonFlag",corpPersonFlag);
			        map.put("VerficationCode",VerficationCode);
			        map.put("gopayServerTime",gopayServerTime);
			        // 组织加密明文
			        String plain= "version=[" + version + "]tranCode=[" + tranCode + "]customerId=[" + customerId + "]" +
			                "merOrderNum=[" + merOrderNum + "]tranAmt=[" + tranAmt + "]feeAmt=[]totalAmount=[]" +
			                "merURL=[" + merURL + "]" + "recvBankAcctNum=[" + recvBankAcctNum + "]tranDateTime=[" + tranDateTime + "]" +
			                "orderId=[]respCode=[]payAcctId=[" + payAcctId + "]approve=[" + approve + "]" +
			                "VerficationCode=[" + VerficationCode + "]gopayServerTime=[" + gopayServerTime + "]";
			        map.put("signValue",SignVerifyUtil.sign(plain, PayConstant.PAY_CONFIG.get("GFB_MERC_PRIVATEKEY")));
			        String tmp="";
			        for (Map.Entry<String, String> entry : map.entrySet()) {
						tmp += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),"utf-8")+"&";
					}
					tmp = tmp.substring(0, tmp.length()-1);
					log.info("国付宝代付请求数据："+tmp);
					String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("GFB_GATEWAYURL")
							, tmp.getBytes("utf-8")),"utf-8");
					log.info("国付宝代付返回数据："+resData);
					Document document = DocumentHelper.parseText(resData);
					Element root = document.getRootElement();
					String respCode = root.element("respCode").getText();//系统响应码
					if("2".equals(respCode)){//处理中
						payRequest.respCode="000";
						rp.status="0";
						rp.retCode="074";
						rp.errorMsg = "提交成功";
						payRequest.respDesc=rp.errorMsg;
			        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
					}else if("7".equals(respCode)){//成功
						payRequest.respCode="000";
						rp.status="1";
						rp.retCode="000";
						rp.errorMsg = "交易成功-实际到账以发卡银行为准";
						payRequest.respDesc=rp.errorMsg;
			        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
			        	List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
						list.add(rp);
						new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					}else{//失败。
						payRequest.respCode="-1";
						rp.status="2";
						rp.retCode="-1";
						rp.errorMsg = root.element("GopayAPIResp").element("msgExt").getText();
						payRequest.respDesc=rp.errorMsg;
			        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
					}
				 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 /**
	  * 代付查询
	  * @param payRequest
	  * @param rp
	  * @return
	  * @throws Exception
	  */
	public void receivePaySingleQuery(PayReceiveAndPay rp) throws Exception{
		try{
			String version ="1.1";
    		String tranCode = "5570";
    		String merchantEncode = "2";
			String signType="3";
			String customerId=PayConstant.PAY_CONFIG.get("GFB_MERNO");
			String userAcct=PayConstant.PAY_CONFIG.get("GFB_VIRCARDNOIN");
			String merOrderNum =  rp.id;
			String queryType="4025";
			String pageNo="0";
			String VerficationCode=PayConstant.PAY_CONFIG.get("GFB_MER_JYM");
			
			Map<String,String> map = new HashMap<String,String>();
	        map.put("version",version);
	        map.put("tranCode",tranCode);
	        map.put("merchantEncode",merchantEncode);
	        map.put("signType",signType);
	        map.put("customerId", customerId);
	        map.put("userAcct",userAcct);
	        map.put("merOrderNum", merOrderNum);
	        map.put("queryType", queryType);
	        map.put("pageNo", pageNo);
	        map.put("VerficationCode",VerficationCode);
	        // 组织加密明文
	        String plain= "version=[" + version + "]tranCode=[" + tranCode + "]tranBeginTime=[]" +
	                "tranEndTime=[]merOrderNum=[" + merOrderNum + "]customerId=["+customerId+"]queryType=["+queryType+"]" +
	                "respCode=[]" + "userAcct=[" + userAcct + "]VerficationCode=[" + VerficationCode + "]" ;
	        map.put("signValue",SignVerifyUtil.sign(plain, PayConstant.PAY_CONFIG.get("GFB_MERC_PRIVATEKEY")));
	        String tmp="";
	        for (Map.Entry<String, String> entry : map.entrySet()) {
				tmp += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),"utf-8")+"&";
			}
			tmp = tmp.substring(0, tmp.length()-1);
			log.info("国付宝代付查询请求数据："+tmp);
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("GFB_GATEWAYURL")
					, tmp.getBytes("utf-8")),"utf-8");
			log.info("国付宝代付查询返回数据："+resData);
			Document document = DocumentHelper.parseText(resData);
			Element root = document.getRootElement();
			String respCode = root.element("respCode").getStringValue();//系统响应码
			if("2".equals(respCode)){
				if("2".endsWith(root.element("resultArr").element("resultRow").element("orgTxnStat").getText())){
					rp.status="1";
					rp.retCode="000";
					rp.errorMsg = "交易成功";
					new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
				}else if("3".endsWith(root.element("resultArr").element("resultRow").element("orgTxnStat").getText())){
					rp.status="2";
					rp.retCode="-1";
					rp.errorMsg = "交易失败";
					new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
				}else{
					rp.status="0";
					rp.retCode="074";
					rp.errorMsg = "处理中";
					new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}	
	}
}
