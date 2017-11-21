package com.pay.merchantinterface.service;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
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
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.coopbank.dao.PayChannelRotationDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.third.zhongxin.util.MD5;
import com.third.zhongxin.util.SignUtils;
import com.third.zhongxin.util.XmlUtils;
/**
 * 广州中信服务类
 * @author Administrator
 *
 */
public class ZXPayService {
	
	private static final Log log = LogFactory.getLog(ZXPayService.class);
	/**
	 * 
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType 业务标示：选择扫码机构的编码 支付宝pay.alipay.native ，微信pay.weixin.native
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,
			String productType,PayChannelRotation channelRotation){
		try {
			 Map<String,String> map = new HashMap<String, String>();
	    	 map.put("service",productType);
	    	 map.put("mch_id", (channelRotation!=null?channelRotation.merchantId:PayConstant.PAY_CONFIG.get("PAY_ZX_WX_MERCHAT_NO")));//商户好。
	    	 map.put("version", "2.0");//版本号。
	    	 map.put("charset", "UTF-8");//字符集。
	    	 map.put("sign_type", "MD5");//签名类型，只支持MD5
	    	 map.put("out_trade_no", payOrder.payordno);//订单号。
	    	 map.put("body",PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
						"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));//商品描述
	    	 map.put("total_fee", String.valueOf(payOrder.txamt));//总金额。单位分。
	    	 map.put("mch_create_ip", PayConstant.PAY_CONFIG.get("PAY_ZX_WX_IP"));//终端IP
	    	 map.put("notify_url", PayConstant.PAY_CONFIG.get("PAY_ZX_WX_NOTIFY_URL"));//通知地址。
	    	 map.put("nonce_str", String.valueOf(new Date().getTime()));//随机字符串。
	    	 Map<String,String> params = SignUtils.paraFilter(map);//过滤空值。
	         StringBuilder buf = new StringBuilder((params.size() +1) * 10);
	         SignUtils.buildPayParams(buf,params,false);
	         String preStr = buf.toString();
	         String sign = MD5.sign(preStr, "&key="+(channelRotation!=null?channelRotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZX_WX_KEY")), "utf-8");
	         map.put("sign", sign);
	         log.info("中信微信支付下单请求报文:"+XmlUtils.parseXML(map));
			 String rltXml=new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("PAY_ZX_WX_URL"), XmlUtils.parseXML(map).getBytes("utf-8")),"utf-8");
			 log.info("中信微信支付下单返回报文:"+rltXml);
			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		     DocumentBuilder builder = factory.newDocumentBuilder();   
		     Document document = builder.parse(new InputSource(new StringReader(rltXml)));   
		     Element root = document.getDocumentElement();   
		     NodeList list = root.getChildNodes();
		     Map<String,String> resultMap=new HashMap<String, String>();
		     for(int i=0;i<list.getLength();i++){
		           Node node = list.item(i);
		           if(!"#text".equals(node.getNodeName())){
		        	   resultMap.put(node.getNodeName(), node.getTextContent());
		           }
		     }
		     if("0".equals(resultMap.get("status"))){
		    	 //验签名。
				 if(SignUtils.checkParam(resultMap,channelRotation!=null?channelRotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZX_WX_KEY"))){
					 if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){//下单成功处理。
						 return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
									"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
									"codeUrl=\""+resultMap.get("code_url")+"\" respCode=\"000\" respDesc=\"处理成功\"/>";
			           }else if(!"0".equals(resultMap.get("result_code"))){//业务处理错误。
				        	 return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						       		   "<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
						       			"codeUrl=\"\" respCode=\"-1\" respDesc=\""+resultMap.get("err_msg").toString()+"\"/>";  
				      }else{
				    	  return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					       		   "<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
					       			"codeUrl=\"\" respCode=\"-1\" respDesc=\"(ZX)渠道错误\"/>";
				      }
				 }else throw new Exception("验签失败");
		     }else throw new Exception("(ZX)渠道错误！"+resultMap.get("message"));
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
		try{
			PayChannelRotation rotation = new PayChannelRotationDAO().getPayChannelRotationOrderNo(payOrder.payordno);//轮询信息
			Map<String,String> map = new HashMap<String, String>();
		   	map.put("service","unified.trade.query");
		   	map.put("mch_id",rotation!=null?rotation.merchantId:PayConstant.PAY_CONFIG.get("PAY_ZX_WX_MERCHAT_NO"));//商户号。
		   	map.put("version", "2.0");//版本号。
		   	map.put("charset", "UTF-8");//字符集。
		   	map.put("sign_type", "MD5");//签名类型，只支持MD5
		   	map.put("out_trade_no", payOrder.payordno);//订单号。
		   	map.put("nonce_str", String.valueOf(new Date().getTime()));//随机字符串。
		   	Map<String,String> params = SignUtils.paraFilter(map);//过滤空值。
		    StringBuilder buf = new StringBuilder((params.size() +1) * 10);
		    SignUtils.buildPayParams(buf,params,false);
		    String preStr = buf.toString();
		    String sign = MD5.sign(preStr, "&key="+(rotation!=null?rotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZX_WX_KEY")), "utf-8");
		    map.put("sign", sign);
		    log.info("中信微信支付查询请求报文:"+XmlUtils.parseXML(map));
			String rltXml=new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("PAY_ZX_WX_URL"), XmlUtils.parseXML(map).getBytes("utf-8")),"utf-8");
		    log.info("中信微信支付查询返回报文:"+rltXml);
			Map<String,String> resultMap = XmlUtils.toMap(rltXml.getBytes(), "utf8");
			if(SignUtils.checkParam(resultMap,rotation!=null?rotation.md5Key:PayConstant.PAY_CONFIG.get("PAY_ZX_WX_KEY"))){
				if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
			        if("SUCCESS".equalsIgnoreCase(resultMap.get("trade_state"))){
			            payOrder.ordstatus="01";//支付成功
				        new NotifyInterface().notifyMer(payOrder);//支付成功
			         }else throw new Exception("支付渠道状态："+resultMap.get("trade_state"));
				}
			}else throw new Exception("验签失败");
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
}
