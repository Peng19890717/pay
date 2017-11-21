package com.pay.merchantinterface.controller;
import java.io.IOException;
import java.io.OutputStream;
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

import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;

@Controller
public class SYXNotifyController {
	private static final Log log = LogFactory.getLog(SYXNotifyController.class);
    @RequestMapping("SYXNotify")
    public String SYXNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("商银信通知开始==========================");
    	OutputStream os = null;
    	
        try {
        	  request.setCharacterEncoding("utf-8");
        	  os=response.getOutputStream();
        	  Map<String,String> params = new HashMap<String,String>();
        	  Map requestParams = request.getParameterMap();
        	  String sign = request.getParameter("sign");
	    	  for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
	    			String name = (String) iter.next();
	    			String[] values = (String[]) requestParams.get(name);
	    			String valueStr = "";
	    			for (int i = 0; i < values.length; i++) {
	    				valueStr = (i == values.length - 1) ? valueStr + values[i]
	    						: valueStr + values[i] + ",";
	    			}
	    			params.put(name, valueStr);
	    		}
	    	  	log.info("商银信通知数据:"+params.toString());
	    		boolean signFlag=com.third.syx.util.RSA.doCheck(params, sign, PayConstant.PAY_CONFIG.get("SYX_PUBLICKEY"), "utf-8");
	    		if(signFlag){
	    			if("2".equals(params.get("tradeStatus"))){
	    				PayOrder tmpPayOrder = new PayOrder();
	                	tmpPayOrder.payordno = params.get("outOrderId");
		            	tmpPayOrder.actdat = new Date();
		            	tmpPayOrder.ordstatus="01";
		            	new NotifyInterface().notifyMer(tmpPayOrder);
	    			}else if("4".equals(params.get("tradeStatus"))){
	    				PayOrder tmpPayOrder = new PayOrder();
	                	tmpPayOrder.payordno = params.get("outOrderId");
		            	tmpPayOrder.actdat = new Date();
		            	tmpPayOrder.ordstatus="02";
		            	new NotifyInterface().notifyMer(tmpPayOrder);
	    			}
	    			os.write("fail".getBytes());
	    		}else new Exception("验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	try {
				os.write("fail".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
}