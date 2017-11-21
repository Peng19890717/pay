package com.pay.merchantinterface.controller;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.bfb.util.RSASignUtil;

@Controller
public class BFBNotifyController {
	private static final Log log = LogFactory.getLog(BFBNotifyController.class);
    
    @RequestMapping("BFBNotify")
    public String BFBNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("邦付宝通知开始==========================");
    	OutputStream os = null;
        try {
        		request.setCharacterEncoding("utf-8");
        		response.setCharacterEncoding("utf-8");
        		response.setHeader("Content-type","text/html;charset=utf-8");
        		os=response.getOutputStream();
        		String charset     = request.getParameter("charset");
    			String version     = request.getParameter("version");
 	        	String serverCert = request.getParameter("serverCert");
 	        	String serverSign = request.getParameter("serverSign");
 	 			String signType = request.getParameter("signType");
 	 			String service       = request.getParameter("service");
 	 			String status         = request.getParameter("status");			
 	            String returnCode   = request.getParameter("returnCode");
 	            String returnMessage   = request.getParameter("returnMessage");
 	            String tradeNo         = request.getParameter("tradeNo");
 	            String purchaserId   = request.getParameter("purchaserId");
 	            String merchantId   = request.getParameter("merchantId"); 
 	            String orderId       = request.getParameter("orderId");
 	            String orderTime     = request.getParameter("orderTime");
 	            String totalAmount   = request.getParameter("totalAmount");
 	            String bankAbbr      = request.getParameter("bankAbbr");
 	            String payTime       = request.getParameter("payTime");  
 	            String acDate        = request.getParameter("acDate");
 	            String fee            = request.getParameter("fee");
 	            String backParam   = request.getParameter("backParam");

 	            Map<String,String> dataMap = new LinkedHashMap<String,String>();
 	        	dataMap.put("charset",charset);
 	        	dataMap.put("version",version);
 	        	dataMap.put("signType",signType);
 	        	dataMap.put("service",service);
 	        	dataMap.put("status",status);
 	        	dataMap.put("returnCode",returnCode);
 	        	dataMap.put("returnMessage",returnMessage);								
 	        	dataMap.put("tradeNo",tradeNo);
 	        	dataMap.put("purchaserId",purchaserId);
 	        	dataMap.put("merchantId",merchantId);
 	        	dataMap.put("orderId",orderId);
 	        	dataMap.put("orderTime",orderTime);
 	        	dataMap.put("totalAmount",totalAmount);
 	        	dataMap.put("bankAbbr",bankAbbr);
 	        	dataMap.put("payTime",payTime);
 	        	dataMap.put("acDate",acDate);
 	        	dataMap.put("fee",fee);	
 	        	dataMap.put("backParam",backParam);
 	        	log.info("邦付宝异步通知参数:"+dataMap.toString());
 	        	Map<String,String> requestMap = new HashMap<String,String>();
 	        	requestMap.putAll(dataMap);
 	            Set<String> set = dataMap.keySet();
 	            Iterator<String> iterator = set.iterator();
 	            while (iterator.hasNext()) {
 	              String key = (String)iterator.next();
 	              String tmp = dataMap.get(key);
 	              if (StringUtils.equals(tmp, "null")||StringUtils.isBlank(tmp)) {
 	            	  requestMap.remove(key);
 	              }
 	            }
 	            RSASignUtil util = new RSASignUtil();
 	        	String returnData=util.coverMap2String(requestMap);
 				boolean flag = util.verify(returnData,serverSign,serverCert,null);
 				if (flag) {
					if("000000".equals(returnCode)){
						if("SUCCESS".equals(status)){
							PayOrder tmpPayOrder = new PayOrder();
			            	tmpPayOrder.payordno = orderId;
			            	tmpPayOrder.actdat = new Date();
			            	tmpPayOrder.ordstatus="01";
			            	new NotifyInterface().notifyMer(tmpPayOrder);
						}else if("FAILED".equals(status)){
							PayOrder tmpPayOrder = new PayOrder();
			            	tmpPayOrder.payordno = orderId;
			            	tmpPayOrder.actdat = new Date();
			            	tmpPayOrder.ordstatus="02";
			            	new NotifyInterface().notifyMer(tmpPayOrder);
						}
					}
					os.write("SUCCESS".getBytes());
 				} else new Exception("邦付宝异步通知验签失败");
 	         } catch (Exception e) {
 	     		e.printStackTrace();
 	         }finally {
 	        	if(os!=null)try {os.close();} catch (Exception e) {};
 	        }
        return null;
    }
}