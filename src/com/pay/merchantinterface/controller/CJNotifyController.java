package com.pay.merchantinterface.controller;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.PayUtil;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.pay.merchantinterface.service.CJPayService;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.RSA;
import com.pay.order.dao.PayOrder;
import com.pay.refund.dao.PayRefund;
import com.pay.refund.service.PayRefundService;
/**
 * 畅捷支付通知接口
 * @author Administrator
 *
 */
@Controller
public class CJNotifyController {
	private static final Log log = LogFactory.getLog(CJNotifyController.class);
    /**
     * 通知地址(直连网银支付通知)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("CJPayNotify")
    public String CJPayNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("畅捷支付通知开始==========================");
    	PrintWriter writer = null;
        try {
        	//接收参数
        	  request.setCharacterEncoding("utf-8");
        	  String notify_id = request.getParameter("notify_id")==null?"":request.getParameter("notify_id");//通知id
        	  String notify_type  = request.getParameter("notify_type")==null?"":request.getParameter("notify_type");// 交易通知此字段为：trade_status_sync  
        	  String notify_time = request.getParameter("notify_time")==null?"":request.getParameter("notify_time");//通知的发送时间，格式：yyyyMMddHHmmss 
        	  String _input_charset = request.getParameter("_input_charset")==null?"":request.getParameter("_input_charset");//参数字符集编码 
        	  String sign = request.getParameter("sign")==null?"":request.getParameter("sign");//签名。
        	  String sign_type = request.getParameter("sign_type")==null?"":request.getParameter("sign_type");//签名类型。
        	  String version  = request.getParameter("version")==null?"":request.getParameter("version");//版本号。
        	  String outer_trade_no = request.getParameter("outer_trade_no")==null?"":request.getParameter("outer_trade_no");//商户网站唯一订单号
        	  String inner_trade_no  = request.getParameter("inner_trade_no")==null?"":request.getParameter("inner_trade_no");//支付平台交易订单号   
        	  String trade_status = request.getParameter("trade_status")==null?"":request.getParameter("trade_status");//交易状态 
        	  String trade_amount = request.getParameter("trade_amount")==null?"":request.getParameter("trade_amount");//交易金额。
        	  String gmt_create  = request.getParameter("gmt_create")==null?"":request.getParameter("gmt_create");//交易创建时间，格式： yyyyMMddHHmmss
        	  String gmt_payment = request.getParameter("gmt_payment");// 交易支付时间  
        	  String gmt_close  = request.getParameter("gmt_close");//交易关闭时间。
        	  String extension  = request.getParameter("extension");//扩展参数。
        	  //通知参数map封装，用于加密验签。
        	  Map<String, String> notifyParameterMap = new HashMap<String, String>();
        	  notifyParameterMap.put("notify_id", notify_id);
        	  notifyParameterMap.put("notify_type", notify_type);
        	  notifyParameterMap.put("notify_time", notify_time);
        	  notifyParameterMap.put("_input_charset", _input_charset);
        	  notifyParameterMap.put("version", version);
        	  notifyParameterMap.put("outer_trade_no", outer_trade_no);
        	  notifyParameterMap.put("inner_trade_no", inner_trade_no);
        	  notifyParameterMap.put("trade_status", trade_status);
        	  notifyParameterMap.put("trade_amount", trade_amount);
        	  notifyParameterMap.put("gmt_create", gmt_create);
        	  Iterator it = notifyParameterMap.keySet().iterator();
        	  while(it.hasNext()){
        		  Object key = it.next();
        		  log.info(key+":"+notifyParameterMap.get(key));
        		  log.info(key+":"+notifyParameterMap.get(key));
        	  }
        	  if(gmt_payment!=null && gmt_payment.length()>0)notifyParameterMap.put("gmt_payment", gmt_payment);
        	  if(gmt_close!=null && gmt_close.length()>0)notifyParameterMap.put("gmt_close", gmt_close);
        	  if(extension!=null && extension.length()>0)notifyParameterMap.put("extension", extension);
			  //通过接受到参数map加密参数，获取签名值。
        	  if(RSA.verify(CJPayService.createLinkString(notifyParameterMap, false), sign,
        			  	PayConstant.PAY_CONFIG.get("cj_merchant_public_key"), _input_charset)){
        		  //更新通知状态。100  WAIT_BUYER_PAY  等待买家付款  ; 201  PAY_FINISHED  买家已付款 ;  301  TRADE_SUCCESS  交易成功  ;401  TRADE_FINISHED  交易结束  ; 999  TRADE_CLOSED  交易关闭
        		  if(null!=trade_status&&trade_status.equals("TRADE_SUCCESS")){
        			  	PayOrder tmpPayOrder = new PayOrder();
	                  	tmpPayOrder.payordno = outer_trade_no;
	                  	tmpPayOrder.actdat = new Date();
	                  	tmpPayOrder.ordstatus="01";
	                  	tmpPayOrder.bankjrnno = "";
	                  	new NotifyInterface().notifyMer(tmpPayOrder);
        		  }
        	  } else throw new Exception("验签失败");
        	  	//响应畅捷支付通知。
        	    writer=response.getWriter();
        	    writer.write("success");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
    /**
     * 通知地址(直连网银--退款通知)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("CJRefundNotify")
    public String CJRefundNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("畅捷退款通知开始==========================");
    	PrintWriter writer = null;
        try {
        	//接收参数
        	  request.setCharacterEncoding("utf-8");
        	  String notify_id = request.getParameter("notify_id")==null?"":request.getParameter("notify_id");//通知id
        	  String notify_type  = request.getParameter("notify_type")==null?"":request.getParameter("notify_type");// 交易通知此字段为：refund_status_sync  
        	  String notify_time = request.getParameter("notify_time")==null?"":request.getParameter("notify_time");//通知的发送时间，格式：yyyyMMddHHmmss 
        	  String _input_charset = request.getParameter("_input_charset")==null?"":request.getParameter("_input_charset");//参数字符集编码 
        	  String sign = request.getParameter("sign")==null?"":request.getParameter("sign");//签名。
        	  String sign_type = request.getParameter("sign_type")==null?"":request.getParameter("sign_type");//签名类型。
        	  String version  = request.getParameter("version")==null?"":request.getParameter("version");//版本号。
        	  String outer_trade_no = request.getParameter("outer_trade_no")==null?"":request.getParameter("outer_trade_no");//退款申请单号。
        	  String orig_outer_trade_no  = request.getParameter("orig_outer_trade_no")==null?"":request.getParameter("orig_outer_trade_no");//要退款的单号。 
        	  String refund_status = request.getParameter("refund_status")==null?"":request.getParameter("refund_status");//退款状态 REFUND_SUCCESS
        	  String inner_trade_no = request.getParameter("inner_trade_no")==null?"":request.getParameter("inner_trade_no");//畅捷退款单号。
        	  String refund_amount   = request.getParameter("refund_amount")==null?"":request.getParameter("refund_amount");//退款金额。
        	  String gmt_refund = request.getParameter("gmt_refund");// 退款时间。
        	  String extension  = request.getParameter("extension");//扩展。
        	  java.util.Enumeration e = request.getParameterNames();
        		while(e.hasMoreElements()){
        			String key = (String)e.nextElement();
        			log.info(key+"==============:"+request.getParameter(key));
        		}
        	  //通知参数map封装，用于加密验签。
        	  Map<String, String> notifyParameterMap = new HashMap<String, String>();
        	  notifyParameterMap.put("notify_id", notify_id);
        	  notifyParameterMap.put("notify_type", notify_type);
        	  notifyParameterMap.put("notify_time", notify_time);
        	  notifyParameterMap.put("_input_charset", _input_charset);
        	  notifyParameterMap.put("version", version);
        	  notifyParameterMap.put("outer_trade_no", outer_trade_no);
        	  notifyParameterMap.put("inner_trade_no", inner_trade_no);
        	  notifyParameterMap.put("orig_outer_trade_no", orig_outer_trade_no);
        	  notifyParameterMap.put("refund_status", refund_status);
        	  notifyParameterMap.put("refund_amount", refund_amount);
        	  Iterator it = notifyParameterMap.keySet().iterator();
        	  while(it.hasNext()){
        		  Object key = it.next();
        		  log.info(key+":"+notifyParameterMap.get(key));
        		  log.info(key+":"+notifyParameterMap.get(key));
        	  }
        	  if(gmt_refund!=null && gmt_refund.length()>0)notifyParameterMap.put("gmt_refund", gmt_refund);
        	  if(extension!=null && extension.length()>0)notifyParameterMap.put("extension", extension);
			  //通过接受到参数map加密参数，获取签名值。
        	  if(RSA.verify(CJPayService.createLinkString(notifyParameterMap, false), sign,
      			  	PayConstant.PAY_CONFIG.get("cj_merchant_public_key"), _input_charset)){
        		  PayRefund payRefund = new PayRefund();
        		  if(null!=refund_status&&refund_status.equals("REFUND_SUCCESS")){
        			  payRefund.refordno = outer_trade_no;
        			  payRefund.banksts="01";//退款成功	
        		  }else{
        			  payRefund.refordno = outer_trade_no;
        			  payRefund.banksts="02";
        		  }
        		  PayRefundService refundService = new PayRefundService();
        		  refundService.setResultRefund(payRefund);
        	  }else throw new Exception("验签失败");
        	  	//响应畅捷支付通知。
        	    writer=response.getWriter();
        	    writer.write("success");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}