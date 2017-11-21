package com.pay.merchantinterface.service;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.PayConstant;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.third.swt.util.HexConver;
import com.third.swt.util.HttpUtil;
import com.third.swt.util.MerchantMD5;
import com.third.swt.util.SignUtils;
/**
 * 商务通接口服务类
 * @author Administrator
 *
 */
public class SWTPayService {
	private static final Log log = LogFactory.getLog(SWTPayService.class);
	/**
	 * 
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType 业务标示：//支付宝、微信 扫码产品编号分别为：CP00000018、CP00000017
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		//下单处理
		try {
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod( PayConstant.PAY_CONFIG.get("SWT_PAY_URL"));
			String orderDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 订单日期
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			post.addParameter("merchantId",PayConstant.PAY_CONFIG.get("SWT_MERCHANT_NO"));
			post.addParameter("prodCode", productType);
			post.addParameter("orderId", payOrder.payordno);
			post.addParameter("orderAmount", String.valueOf(payOrder.txamt));
			post.addParameter("orderDate", orderDate);
			post.addParameter("prdOrdType", "0");
			post.addParameter("retUrl",PayConstant.PAY_CONFIG.get("SWT_PAY_NITIFY_URL"));
			post.addParameter("returnUrl", "");
			post.addParameter("prdName", HexConver.Str2Hex(PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")));
			post.addParameter("prdDesc", HexConver.Str2Hex(PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")));
			post.addParameter("signType", "MD5");
			
			Map<String, String> params = new LinkedHashMap<String, String>();
			params.put("merchantId",PayConstant.PAY_CONFIG.get("SWT_MERCHANT_NO"));
			params.put("prodCode", productType);
			params.put("orderId", payOrder.payordno);
			params.put("orderAmount", String.valueOf(payOrder.txamt));
			params.put("orderDate", orderDate);
			params.put("prdOrdType", "0");
			params.put("retUrl", PayConstant.PAY_CONFIG.get("SWT_PAY_NITIFY_URL"));
			params.put("returnUrl","");
			params.put("prdName",HexConver.Str2Hex(PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")));
			params.put("prdDesc", HexConver.Str2Hex(PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")));
			params.put("signType", "MD5");
			String source = SignUtils.createLinkString(params);
			post.addParameter("signature", MerchantMD5.MD5(source + PayConstant.PAY_CONFIG.get("SWT_PAY_KEY")));
			//执行发送请求报文。
			NameValuePair [] nvp = post.getParameters();
			log.info("商物通扫码支付请求数据=====开始====");
			for(int i=0; i<nvp.length; i++)log.info(nvp[i].getName()+"="+nvp[i].getValue());
			log.info("商物通扫码支付请求数据=====结束====");
			httpclient.executeMethod(post);
			InputStream inputStream = post.getResponseBodyAsStream();   
	        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));   
	        StringBuffer stringBuffer = new StringBuffer();   
	        String str= "";   
	        while((str = br.readLine()) != null){   
	            stringBuffer .append(str);   
	        }   
            String info=new String(stringBuffer.toString().getBytes(),"UTF-8");
            log.info("商物通扫码支付响应数据:"+info);
			if (info != null && !"".equals(info)) {
					Document doc = DocumentHelper.parseText(info);
					Element root = doc.getRootElement();
					if (root.attributeValue("retCode").equals("0001")) {
						String qrURL = root.element("qrURL") == null ? "": root.element("qrURL").getStringValue();
						return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
						"codeUrl=\""+qrURL.substring(qrURL.indexOf("?")+1)+"\" respCode=\"000\" respDesc=\"成功\"/>";
					} else return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
								"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
								"codeUrl=\"\" respCode=\"-1\" respDesc=\""+root.attributeValue("retMsg")+"\"/>";
			} else throw new Exception("商物通渠道异常");
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
			Map<String, String> params = new LinkedHashMap<String, String>();
			params.put("merchantId",PayConstant.PAY_CONFIG.get("SWT_MERCHANT_NO"));
			params.put("queryType", "1");
			params.put("orderId", payOrder.payordno);
			params.put("signType", "MD5");
			String source = SignUtils.createLinkString(params);
			String signature = MerchantMD5.MD5(source +  PayConstant.PAY_CONFIG.get("SWT_PAY_KEY"));
			String str = source + "&signature=" + signature;
			log.info("商物通查询请求的参数："+str);
			String receiveMsg = HttpUtil.sendHttp(PayConstant.PAY_CONFIG.get("SWT_PAY_QUERY_URL"), str);
			log.info("商物通查询响应的参数："+receiveMsg);
			if (!"".equals(receiveMsg)) {
				Document doc = DocumentHelper.parseText(receiveMsg);
				Element root = doc.getRootElement();
				if (root.attributeValue("retCode").equals("0001")) {
					for (Iterator iter = root.elementIterator(); iter.hasNext();) {
							Element element = (Element) iter.next();
							if("1".equals(element.attributeValue("status"))){
								payOrder.ordstatus="01";//支付成功
					        	new NotifyInterface().notifyMer(payOrder);//支付成功
							}else{
								throw new Exception(element.attributeValue("statusDes"));
							}
						}
				} 
			}else{
				throw new Exception("商物通渠道异常");
			}
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	public static void main(String[] args) throws Exception {
		query(MerchantmerchantWechatPay());
		
    }
	public static String MerchantmerchantWechatPay() {
		String  orderId = null;
		try {
			HttpClient httpclient = new HttpClient();
			PostMethod post = new PostMethod("https://www.56zhifu.com/user/MerchantmerchantWechatPay.do");//
			String orderAmount = "1000"; // 订单金额 以人民币分为单位
			String prdOrdType = "0";// 订单类型:0消费
			String prodCode = "CP00000018";//支付宝、微信 扫码产品编号分别为：CP00000018、CP00000017
			String prdName = "商品";// 商品名称
			String prdDesc = "购买商品";// 商品描述
			orderId = "P" + System.currentTimeMillis();// 商品订单号
			String orderDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()); // 订单日期
			String retUrl = "http://59.41.60.154:8090/merchant_order_demo/page/notify_url.jsp";// 异步通知URL
			String signType = "MD5";
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			post.addParameter("merchantId", "00000000015146");
			post.addParameter("prodCode", prodCode);
			post.addParameter("orderId", orderId);
			post.addParameter("orderAmount", orderAmount);
			post.addParameter("orderDate", orderDate);
			post.addParameter("prdOrdType", prdOrdType);
			post.addParameter("retUrl", retUrl);
			post.addParameter("returnUrl", "");
			post.addParameter("prdName", HexConver.Str2Hex(prdName));
			post.addParameter("prdDesc", HexConver.Str2Hex(prdDesc));
			post.addParameter("signType", signType);

			Map<String, String> params = new LinkedHashMap<String, String>();
			params.put("merchantId", "00000000015146");
			params.put("prodCode", prodCode);
			params.put("orderId", orderId);
			params.put("orderAmount", orderAmount);
			params.put("orderDate", orderDate);
			params.put("prdOrdType", prdOrdType);
			params.put("retUrl", retUrl);
			params.put("returnUrl","");
			params.put("prdName", HexConver.Str2Hex(prdName));
			params.put("prdDesc", HexConver.Str2Hex(prdDesc));
			params.put("signType", signType);
			String source = SignUtils.createLinkString(params);
			post.addParameter("signature", MerchantMD5.MD5(source + "KN3RAPZLVL2I1RL1JCZ3BDWL"));
			httpclient.executeMethod(post);
			InputStream inputStream = post.getResponseBodyAsStream();   
	        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));   
	        StringBuffer stringBuffer = new StringBuffer();   
	        String str= "";   
	        while((str = br.readLine()) != null){   
	            stringBuffer .append(str );   
	        }   
            String info=stringBuffer.toString();
            log.info("响应的数据:"+info);
			if (info != null && !"".equals(info)) {
				try {
					Document doc = DocumentHelper.parseText(info);
					Element root = doc.getRootElement();
					if (root.attributeValue("retCode").equals("0001")) {
						String qrURL = root.element("qrURL") == null ? ""
								: root.element("qrURL").getStringValue();
						log.info(qrURL);
						log.info("支付二维码扫描地址---->" + qrURL.substring(qrURL.indexOf("?")+1));
					} else {
						log.info(root.attributeValue("retMsg"));
					}
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderId;
	}
	public static void query(String orderId){
		try {
			Map<String, String> params = new LinkedHashMap<String, String>();
			params.put("merchantId","00000000015146");
			params.put("queryType", "1");
			params.put("orderId", orderId);
			params.put("signType", "MD5");
			String source = SignUtils.createLinkString(params);
			String signature = MerchantMD5.MD5(source + "KN3RAPZLVL2I1RL1JCZ3BDWL");
			String str = source + "&signature=" + signature;
			log.info("查询请求的参数:" + str);
			String receiveMsg = HttpUtil.sendHttp("https://www.56zhifu.com/user/MerchantmerchantTransQuery.do", str);
			log.info("查询反悔的参数:"+receiveMsg);
			if (!"".equals(receiveMsg)) {
				try {
					Document doc = DocumentHelper.parseText(receiveMsg);
					Element root = doc.getRootElement();
					if (root.attributeValue("retCode").equals("0001")) {
						for (Iterator iter = root.elementIterator(); iter.hasNext();) {
								Element element = (Element) iter.next();
								String status=element.attributeValue("status");
								log.info("支付结果:"+status);
							}
					} else {
						log.info(root.attributeValue("retMsg"));
					}
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
