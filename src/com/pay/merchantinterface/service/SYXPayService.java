package com.pay.merchantinterface.service;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import util.DataTransUtil;

import com.PayConstant;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.service.PayOrderService;
import com.third.syx.util.AllscoreCore;

public class SYXPayService {

	private static final Log log = LogFactory.getLog(SYXPayService.class);
	public static Map<String, String> bank_code = new HashMap<String, String>();
	static {
		bank_code.put("ABC","ABC");//农业银行
		bank_code.put("BCCB","BJBANK");//北京银行
		bank_code.put("BOC","BOC");//中国银行
		bank_code.put("BOCOM","COMM");//交通银行
		bank_code.put("BOS","SHBANK");//上海
		bank_code.put("CCB","CCB");//建设
		bank_code.put("CEB","CEB");//光大
		bank_code.put("CIB","CIB");//兴业
		bank_code.put("CMB","CMB");//招商
		bank_code.put("CNCB","CITIC");//中信
		bank_code.put("GDB","GDB");//广发
		bank_code.put("HSBANK","HSBANK");//徽商
		bank_code.put("HXB","HXBANK");//华夏
		bank_code.put("ICBC","ICBC");//工商
		bank_code.put("PAB","SPABANK");//平安
		bank_code.put("PSBC","PSBC");//邮政
		bank_code.put("ZSBC","ZSB");//浙商
		bank_code.put("CMBC","CMBC");//民生
		bank_code.put("SPDB","SPDB");//浦发
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
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SYX"); // 支付渠道
			payOrder.bankcod = request.getParameter("banks"); // 银行代码
			payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			String ProductName=PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			req.put("service", "directPay");
			req.put("inputCharset", "UTF-8");
			req.put("returnUrl", PayConstant.PAY_CONFIG.get("SYX_WG_PAGERETURNURL"));
			req.put("notifyUrl", PayConstant.PAY_CONFIG.get("SYX_NOTIFY_URL"));
			req.put("merchantId", PayConstant.PAY_CONFIG.get("SYX_MERONO"));
			req.put("payMethod", "bankPay");
			req.put("outOrderId", payOrder.payordno);
			req.put("subject", ProductName);
			req.put("body", ProductName);
			req.put("defaultBank", bank_code.get(payOrder.bankcod));
			req.put("channel", "B2C");
			req.put("cardAttr", "01");
			req.put("transAmt", String.format("%.2f", ((double)payOrder.txamt)/100d));
			String sign=AllscoreCore.buildMysignRSA(req,null);
			req.put("sign",sign);
			req.put("signType","RSA");
			log.info("商银信网银请求参数"+req);
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
	public void pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SYX"); // 支付渠道
			payOrder.bankcod = payRequest.bankId; // 银行代码
			payOrder.bankCardType = payRequest.accountType;
			Map<String, String> req = new HashMap<String, String>();
			String ProductName=PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			req.put("service", "directPay");
			req.put("inputCharset", "UTF-8");
			req.put("returnUrl", PayConstant.PAY_CONFIG.get("SYX_WG_PAGERETURNURL"));
			req.put("notifyUrl", PayConstant.PAY_CONFIG.get("SYX_NOTIFY_URL"));
			req.put("merchantId", PayConstant.PAY_CONFIG.get("SYX_MERONO"));
			req.put("payMethod", "bankPay");
			req.put("outOrderId", payOrder.payordno);
			req.put("subject", ProductName);
			req.put("body", ProductName);
			req.put("defaultBank", bank_code.get(payOrder.bankcod));
			req.put("channel", "B2C");
			req.put("cardAttr", "01");
			req.put("transAmt", String.format("%.2f", ((double)payOrder.txamt)/100d));
			String sign=AllscoreCore.buildMysignRSA(req,PayConstant.PAY_CONFIG.get("SYX_PRIVATEKEY"));
			req.put("sign",sign);
			req.put("signType","RSA");
			log.info("商银信网银请求参数"+req);
			Set<String> set1 = req.keySet();
			Iterator<String> iterator1 = set1.iterator();
			while (iterator1.hasNext()) {
				String key0 = (String) iterator1.next();
				request.setAttribute(key0, req.get(key0));
			}
			new PayOrderService().updateOrderForBanks(payOrder);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获 
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try {
				Map<String, String> req = new HashMap<String, String>();
				req.put("service", "orderQuery");
				req.put("inputCharset", "UTF-8");
				req.put("outOrderId", payOrder.payordno);
				req.put("version", "1");
				req.put("merchantId", PayConstant.PAY_CONFIG.get("SYX_MERONO"));
				req.put("sign",AllscoreCore.buildMysignRSA(req,PayConstant.PAY_CONFIG.get("SYX_PRIVATEKEY")));
				req.put("signType","RSA");
				Set<String> set1 = req.keySet();
				Iterator<String> iterator1 = set1.iterator();
				String parms ="";
				while (iterator1.hasNext()) {
					String key0 = (String) iterator1.next();
					parms+=key0+"="+req.get(key0)+"&";
				}
				parms.substring(0,parms.length()-1);
				log.info("商银信查询请求参数："+req);
				String payResult = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("SYX_PAY_QUERY_URL"),parms.getBytes("UTF-8")),"UTF-8");
				log.info("商银信查询响应参数："+payResult);
				boolean signFlag = false;
				String sign = null;
				Document document = DocumentHelper.parseText(payResult);
				Element root = document.getRootElement();
				Element element2 = root.element("pays");
				Map<String,String> paras_ = new HashMap<String, String>();
				if(element2 != null){
					Element payEle = root.element("pays").element("pay");
					if(payEle != null){
						List<Element> payList = payEle.elements();
						for (Element element : payList) {
							if(element.getName().equals("sign")){
								sign = element.getText();
								continue;
							}
							paras_.put(element.getName(), element.getText());
						}
					}
					signFlag = com.third.syx.util.RSA.doCheck(paras_, sign, PayConstant.PAY_CONFIG.get("SYX_PUBLICKEY"), "UTF-8");
					if(signFlag){
						if("ORDER_STATUS_SUC".equals(paras_.get("payStatus"))){
							payOrder.ordstatus="01";//支付成功
						    new NotifyInterface().notifyMer(payOrder);//支付成功
			        	}
					}else new Exception("验签失败");
				}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
