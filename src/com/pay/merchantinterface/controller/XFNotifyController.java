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

import com.PayConstant;
import com.alibaba.fastjson.JSONObject;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.xf.AESCoder;
import com.third.xf.UcfForOnline;
/**
 * 先锋支付通知接口
 * @author Administrator
 *
 */
@Controller
public class XFNotifyController {
	private static final Log log = LogFactory.getLog(XFNotifyController.class);
    /**
     * 先锋网银通知
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("XFPayNotify")
    public String XFPayNotify(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	log.info("先锋网银通知开始==========================");
    	PrintWriter writer = null;
    	try {
    		writer = response.getWriter();
    		//取得通知信息
    		String parameters = request.getParameter("data");
    		String dataValue = AESCoder.decrypt(parameters, PayConstant.PAY_CONFIG.get("XF_KEY"));
    		log.info("解密后的JSONG串："+dataValue);
    		JSONObject jsonObject = JSONObject.parseObject(dataValue);
    		Map<String,String> signParameters = new HashMap<String,String>();
         	Iterator paiter = jsonObject.keySet().iterator();
         	while (paiter.hasNext()) {
                  String key = paiter.next().toString();
                  signParameters.put(key, jsonObject.getString(key));
         	 }   
    		if(UcfForOnline.verify(PayConstant.PAY_CONFIG.get("XF_KEY"), "sign", jsonObject.getString("sign"), signParameters, "RSA")){
				if("S".equals(jsonObject.getString("status"))){
					PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = jsonObject.getString("merchantNo");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
				}else if("F".equals(jsonObject.getString("status"))){
					PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = jsonObject.getString("merchantNo");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="02";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
				}
    			writer.write("SUCCESS");
    		}else throw new Exception("先锋通知验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
    /**
     * 先锋快捷通知
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("XFNotify")
    public String XFNotify(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	log.info("先锋快捷通知开始==========================");
    	PrintWriter writer = null;
    	try {
    		writer = response.getWriter();
    		//取得通知信息
    		String parameters = request.getParameter("data");
    		String dataValue = AESCoder.decrypt(parameters, PayConstant.PAY_CONFIG.get("XF_KEY"));
    		log.info("解密后的JSONG串："+dataValue);
    		/**
    		 * {"sign":"G1bpilC6hHDm5t818RsgiYv8w4nrV/hfnIwxWxOBAI9DHadXfdWpw0ct6HoUEnJJa2VOlN0UsnS/pQzaPXQgj5YWG5wI2sy7esKg2vP1NPH1zUkANKgUR76zP5CQAqBR2o7fRRfSV8Jm3bnHZh83dI/wIJwievqPweO50Fepjmg=","amount":"10","transCur":"156","memo":"","tradeNo":"201707191627431031410005071658",
    		 * "status":"S","tradeTime":"20170719162755","resCode":"00000","resMessage":"成功","merchantId":"M200000550","merchantNo":"1500452847233"}
    		 */
    		JSONObject jsonObject = JSONObject.parseObject(dataValue);
    		Map<String,String> signParameters = new HashMap<String,String>();
    		Iterator paiter = jsonObject.keySet().iterator();
    		while (paiter.hasNext()) {
    			String key = paiter.next().toString();
    			signParameters.put(key, jsonObject.getString(key));
    		}   
    		if(UcfForOnline.verify(PayConstant.PAY_CONFIG.get("XF_KEY"), "sign", jsonObject.getString("sign"), signParameters, "RSA")){
    			if("00000".equals(jsonObject.getString("resCode"))){
    				if("S".equals(jsonObject.getString("status"))){
    					PayOrder tmpPayOrder = new PayOrder();
    					tmpPayOrder.payordno = jsonObject.getString("merchantNo");
    					tmpPayOrder.actdat = new Date();
    					tmpPayOrder.ordstatus="01";
    					new NotifyInterface().notifyMer(tmpPayOrder);
    				}else if("F".equals(jsonObject.getString("status"))){
    					PayOrder tmpPayOrder = new PayOrder();
    					tmpPayOrder.payordno = jsonObject.getString("merchantNo");
    					tmpPayOrder.actdat = new Date();
    					tmpPayOrder.ordstatus="02";
    					new NotifyInterface().notifyMer(tmpPayOrder);
    				}
    				writer.write("SUCCESS");
    			}
    		}else throw new Exception("先锋通知验签失败");
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw e;
    	} finally {
    		if(writer!=null)try {writer.close();} catch (Exception e) {}
    	}
    	return null;
    }
}