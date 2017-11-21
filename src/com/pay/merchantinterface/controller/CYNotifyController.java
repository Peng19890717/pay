package com.pay.merchantinterface.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.MD5;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
/**
 * 长盈支付通知接口
 * @author lqq
 *
 */
@Controller
public class CYNotifyController {
	private static final Log log = LogFactory.getLog(CYNotifyController.class);
	/**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("CYPayNotify")
    public String CYPayNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("长盈网银通知开始==========================");
    	PrintWriter writer = null;
        try {
        	writer = response.getWriter();
        	//接收参数
        	request.setCharacterEncoding("utf-8");
        	Enumeration e =request.getParameterNames(); 
        	Map<String,String> map = new HashMap<String,String>();
	       	while (e.hasMoreElements()) {  
	       	  String paramName = (String) e.nextElement();  
	       	  String paramValue = request.getParameter(paramName);  
	       	   //形成键值对应的map  
	       	   map.put(paramName, paramValue);
	       	  } 
        	log.info("长盈异步通知响应数据："+JSON.toJSONString(map));
        	
        	String sign = (String) map.get("sign");
        	String order_status = (String) map.get("order_status");
        	String out_trade_no = (String) map.get("out_trade_no");
        	String key = PayConstant.PAY_CONFIG.get("CY_MER_PRIVATEKEY");//key
        	String tmpSign = makeReqParamMap(map,key);
        	if(tmpSign.equals(sign)){
        		//验签通过
        		writer.write("SUCCESS");
        		if("3".equals(order_status)){
        			PayOrder tmpPayOrder = new PayOrder();
					tmpPayOrder.payordno = out_trade_no;
					tmpPayOrder.actdat = new Date();
					tmpPayOrder.ordstatus="01";
					new NotifyInterface().notifyMer(tmpPayOrder);
        		}
        	}
        } catch (Exception e) {
        	e.printStackTrace();
        	writer.write("FAIL");
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
    
    public String formatString(String text) {
		return (text == null) ? "" : text.trim();
	}
    
    public String makeReqParamMap(Map<String, String> paramMap,String key) {
        List<String> params = new ArrayList<String>();
        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(sortedkeys);
        for (String rk : sortedkeys) {
        	String value = paramMap.get(rk);
        	if("".equals(value) || "sign".equals(rk)){
        		continue;
        	}else{
        		params.add(rk + "=" + value);
        	}
        }
        String presign = StringUtils.join(params, "&") + "&key=" + key;
        log.info("长盈异步通知：签名字符串:" + presign);
        String sign = StringUtils.upperCase(MD5.getDigest(presign));
        log.info("长盈异步通知：签名:" + sign);
        return sign;
    }
}