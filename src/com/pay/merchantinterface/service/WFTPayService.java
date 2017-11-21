package com.pay.merchantinterface.service;

import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.PayConstant;
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.coopbank.dao.PayChannelRotationDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.third.wft.MD5;
import com.third.wft.SignUtils;
import com.third.wft.XmlUtils;

/**
 * 威富通 接口服务类
 * @author lqq
 *
 */
public class WFTPayService {
	private static final Log log = LogFactory.getLog(WFTPayService.class);
	/**
	 * 扫码
	 * @param productType("手机QQ扫码支付")
	 */
	public String scanPayQQ(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder
			,PayChannelRotation channelRotation){
		try {
			String service = "pay.tenpay.native";//接口类型
			String version = "2.0";//版本号
			String charset = "UTF-8";//字符集
			String sign_type = "MD5";//加密方式
			String mch_id = channelRotation!=null?channelRotation.merchantId:PayConstant.PAY_CONFIG.get("WFT_MERNO");//商户号
			String out_trade_no = payOrder.payordno;//订单号
			String body = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String total_fee = String.valueOf(payOrder.txamt);//订单金额
			String notify_url = PayConstant.PAY_CONFIG.get("WFT_NOTIFYURL");//异步通知地址
			String nonce_str = String.valueOf(new Date().getTime());//随机字符串
			SortedMap<String,String> map = new TreeMap<String, String>();
			map.put("service", service);
			map.put("version", version);
			map.put("charset", charset);
			map.put("sign_type", sign_type);
			map.put("mch_id", mch_id);
			map.put("out_trade_no", out_trade_no);
			map.put("body", body);
			map.put("total_fee", total_fee);
			map.put("mch_create_ip", PayConstant.PAY_CONFIG.get("WFT_QQ_MCH_CREATE_IP"));
			map.put("notify_url", notify_url);
			map.put("nonce_str", nonce_str);
			Map<String,String> params = SignUtils.paraFilter(map);
			StringBuilder buf = new StringBuilder((params.size() +1) * 10);
            SignUtils.buildPayParams(buf,params,false);
            String preStr = buf.toString();
            String key = channelRotation!=null?channelRotation.md5Key:PayConstant.PAY_CONFIG.get("WFT_MERC_PRIVATEKEY");//key
            String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
            map.put("sign", sign);
            String reqUrl = PayConstant.PAY_CONFIG.get("WFT_SCAN_URL");//url地址
            log.info("威富通手机QQ扫码请求参数："+XmlUtils.parseXML(map));
           
            CloseableHttpResponse response = null;
            CloseableHttpClient client = null;
            String res = null;
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                log.info("威富通请求结果：" + res);
                if(resultMap.containsKey("sign")){
                    if(!SignUtils.checkParam(resultMap, key)){
                        throw new Exception("威富通手机QQ 扫码下单失败");
                    }else{
                    	String qrCode = resultMap.get("code_img_url");
                    	return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
    					"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
    					"codeUrl=\""+qrCode+"\" respCode=\"000\" respDesc=\"成功\"/>";
                    }
                } else throw new Exception("威富通手机QQ 扫码下单失败");
            }else throw new Exception("威富通手机QQ 扫码下单失败");
		}catch (Exception e) {
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
			"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
			"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
		}
	}
	/**
	 * 渠道补单,(扫码、网银、快捷)
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try {
			PayChannelRotation rotation = new PayChannelRotationDAO().getPayChannelRotationOrderNo(payOrder.payordno);//轮询信息
			String service = "unified.trade.query";//接口类型
			String version = "2.0";//版本
			String charset = "UTF-8";//字符
			String sign_type = "MD5";//加密方式
			String mch_id = rotation!=null?rotation.merchantId:PayConstant.PAY_CONFIG.get("WFT_MERNO");//商户号
			String out_trade_no = payOrder.payordno;//订单编号
			String nonce_str = String.valueOf(new Date().getTime());//随机字符串
			String key = rotation!=null?rotation.md5Key:PayConstant.PAY_CONFIG.get("WFT_MERC_PRIVATEKEY");//key
			String reqUrl = PayConstant.PAY_CONFIG.get("WFT_SCAN_URL");//url地址
			SortedMap<String,String> map = new TreeMap<String, String>();
			map.put("service", service);
			map.put("version", version);
			map.put("charset", charset);
			map.put("sign_type", sign_type);
			map.put("mch_id", mch_id);
			map.put("out_trade_no", out_trade_no);
			map.put("nonce_str", nonce_str);
			map.put("nonce_str", nonce_str);
			Map<String,String> params = SignUtils.paraFilter(map);
			StringBuilder buf = new StringBuilder((params.size() +1) * 10);
	        SignUtils.buildPayParams(buf,params,false);
	        String preStr = buf.toString();
	        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
	        map.put("sign", sign);
	        log.info("威富通手机QQ扫码查单请求参数："+preStr);
	        
	        CloseableHttpResponse response = null;
	        CloseableHttpClient client = null;
	        HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
            	Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                String res = XmlUtils.toXml(resultMap);
                log.info("威富通手机QQ扫码查单结果："+res);
                if(resultMap.containsKey("sign") && SignUtils.checkParam(resultMap, key)){
                	if("0".equals(resultMap.get("status"))&&"0".equals(resultMap.get("result_code"))){
                		if("SUCCESS".equals(resultMap.get("trade_state"))){
                			payOrder.ordstatus="01";//支付成功
                			new NotifyInterface().notifyMer(payOrder);//支付成功
                		}
                	}else throw new Exception("威富通手机QQ扫码查单失败！");
                }else throw new Exception("威富通手机QQ扫码查单失败！");
            }else throw new Exception("威富通手机QQ扫码查单失败！");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
