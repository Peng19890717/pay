package com.pay.merchantinterface.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.yhj.HmacArray;
import com.third.yhj.YHJUtils;

/**
 * 易汇金支付结果通知
 * @author xk
 *
 */
@Controller
public class YHJNotifyController {
	private static final Log log = LogFactory.getLog(YHJNotifyController.class);
	static final Object[] QUERY_RESPONSE_HMAC_FIELDS = { "merchantId", "requestId", "serialNumber", "totalRefundCount", "totalRefundAmount", "orderCurrency", "orderAmount", "status", "completeDateTime", "remark", "bindCardId", HmacArray.create("subOrders", new String[] { "requestId", "orderAmount", "serialNumber" }) };
	/**
	 * 通知地址(直连网银支付通知)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("YHJNotify")
    public String YHJNotify(HttpServletRequest request,HttpServletResponse response){
		log.info("易汇金支付通知开始==========================");
		PrintWriter writer = null;
		OutputStream os = null;
    	InputStream is = null;
    	try {
    		//取得通知信息
    		StringBuffer req = new StringBuffer("");
    		is = request.getInputStream();
    		os = response.getOutputStream();
    		byte[] b = new byte[1024];
    		int len = -1;
    		while((len = is.read(b)) != -1) {
    			req.append(new String(b,0,len,"utf-8"));
    		}
    		log.info(req);
    		JSONObject notifyJson = JSONObject.parseObject(req.toString());
    		if(YHJUtils.verifyHmac(QUERY_RESPONSE_HMAC_FIELDS, notifyJson)){
    			//验签成功
    			if(notifyJson.getString("status") != null && "SUCCESS".equals(notifyJson.getString("status")) ){
    				PayOrder tmpPayOrder = new PayOrder();
                  	tmpPayOrder.payordno = notifyJson.getString("requestId");
                  	tmpPayOrder.actdat = new Date();
                  	tmpPayOrder.ordstatus="01";
                  	tmpPayOrder.bankjrnno = "";
                  	new NotifyInterface().notifyMer(tmpPayOrder);
    			}
    		} else throw new Exception("易汇金验签失败");
    		//响应易汇金支付通知。
    	    writer=response.getWriter();
    	    writer.write("SUCCESS");
		} catch (Exception e) {
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        	if(is != null) try { is.close(); } catch (Exception e) {}
        }
    	return null;
	}
	
}

