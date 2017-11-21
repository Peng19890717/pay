package com.pay.merchantinterface.controller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.kbnew.Tools.SignUtil;
import com.third.kbnew.Tools.Tools;

@Controller
public class KBNEWNotifyController {
	private static final Log log = LogFactory.getLog(KBNEWNotifyController.class);
	@RequestMapping("KBNEWNotify")
    public String KBNEWNotify(HttpServletRequest request,HttpServletResponse response) {
		OutputStream os = null;
    	InputStream is = null;
    	log.info("新酷宝通知开始================");
        try {
        	 request.setCharacterEncoding("UTF-8");
        	 os = response.getOutputStream();
    		Enumeration e =request.getParameterNames(); 
        	Map<String,String> result_map = new HashMap<String,String>();
	       	while (e.hasMoreElements()) {  
	       	  String paramName = (String) e.nextElement();  
	       	  String paramValue = request.getParameter(paramName);  
	       	   //形成键值对应的map  
	       	  result_map.put(paramName, paramValue);
	       	 }
     		log.info("新酷宝扫码异步通知数据:" + result_map);
    		String sign_result = result_map.get("sign").toString();
    		String sign_type_result = result_map.get("sign_type").toString();
    		String _input_charset_result = result_map.get("_input_charset").toString();
    		String result_string = Tools.calculateSignPlain(result_map);
    		if(SignUtil.Check_sign(result_string.toString(), sign_result,sign_type_result, PayConstant.PAY_CONFIG.get("KBNEW_PUBLICCHECKKEY"), _input_charset_result)){
				if("PAY_FINISHED".equals(result_map.get("trade_status"))){
					PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = result_map.get("outer_trade_no");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
				}else if("TRADE_FAILED".equals(result_map.get("trade_status"))){
					PayOrder tmpPayOrder = new PayOrder();
					tmpPayOrder.payordno = result_map.get("outer_trade_no");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="02";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
				}
				os.write("success".getBytes());
    		}else new Exception("验签失败!");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if(is!=null)try {is.close();} catch (Exception e) {}
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
	
	/**
	 * 新酷宝 网银：接收异步通知
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("KBNEWWYNotify")
    public String KBNEWWYNotify(HttpServletRequest request,HttpServletResponse response) {
		OutputStream os = null;
    	InputStream is = null;
    	log.info("新酷宝网银：通知开始================");
    	try {
	       	  request.setCharacterEncoding("UTF-8");
	          os = response.getOutputStream();
	   		  Enumeration e =request.getParameterNames(); 
	       	  Map<String,String> result_map = new HashMap<String,String>();
		      while (e.hasMoreElements()) {  
		      String paramName = (String) e.nextElement();  
		      String paramValue = request.getParameter(paramName);  
		       	   //形成键值对应的map  
		       	  result_map.put(paramName, paramValue);
		       }
	    		log.info("新酷宝网银：异步通知数据:" + result_map);
	       		/**
	       		 * {notify_time=20170918110809, sign_type=RSA, notify_type=b2c_trade_status_sync, 
	       		 * out_trade_no=J7LLQOX53JLXPT2R4, trade_status=PAY_FINISHED, gmt_payment=20170918110809,
	       		 *  version=1.0, 
	       		 *  sign=ooaN5IHqo2LKvE7H5TgMjTHw1KAb/7T9c1aOxXN30F/Gkm0hrCmZbgyaNTy1IbFmIXfx9llttm6aPjYE5fASHXVHHr0av3xNCwXr8mLNQVOE4zC7rMPSQOMTg2IKdzfFhO0VgBVeYo2PVzAzyVzAkri3fu9zzz5vx3U25hQPn8SpTA0eawZ8keS+8ANMBAap5Oje0k32hMETcIvCR+uJuy596X1cK4t08lCZOIwLHXU96/r5mdnqqR4pc+kzNQBLgcmm1A5NE315vEihp5+ddzrAXRIGbK/mme8ePM/wkF/VWKKRBXdz/G3u/7DeEkdCYRrXAzAqikJ+IvaFjOoY9A==, 
	       		 *  fee=0.00, _input_charset=UTF-8, gmt_create=20170918110706, trade_amount=10.00, notify_id=201709180122909581, inner_trade_no=111505704026126471295}

	       		 */
	    		String sign_result = result_map.get("sign").toString();
	    		String sign_type_result = result_map.get("sign_type").toString();
	    		String _input_charset_result = result_map.get("_input_charset").toString();
	    		String result_string = Tools.calculateSignPlain(result_map);
	    		if(SignUtil.Check_sign(result_string.toString(), sign_result,sign_type_result, PayConstant.PAY_CONFIG.get("KBNEW_WY_PUBLICCHECKKEY"), _input_charset_result)){
	    			if("PAY_FINISHED".equals(result_map.get("trade_status"))){//付款成功
	    				PayOrder tmpPayOrder = new PayOrder();
						tmpPayOrder.payordno = result_map.get("out_trade_no");
						tmpPayOrder.actdat = new Date();
						tmpPayOrder.ordstatus="01";
						new NotifyInterface().notifyMer(tmpPayOrder);
	    			}else if ("TRADE_FAILED".equals(result_map.get("trade_status"))) {
	    				PayOrder tmpPayOrder = new PayOrder();
						tmpPayOrder.payordno = result_map.get("out_trade_no");
		            	tmpPayOrder.actdat = new Date();
		            	tmpPayOrder.ordstatus="02";
		            	new NotifyInterface().notifyMer(tmpPayOrder);
	    			}
	    			os.write("success".getBytes());
	    		}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(is!=null) try {is.close();} catch (Exception e) {}
        	if(os!=null) try {os.close();} catch (Exception e) {}
		}
		return null;
	}
}
