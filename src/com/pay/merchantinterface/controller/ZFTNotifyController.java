package com.pay.merchantinterface.controller;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.PayUtil;

import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;

/**
 * 支付通扫码支付通知
 * @author Administrator
 *
 */
@Controller
public class ZFTNotifyController {
	private static final Log log = LogFactory.getLog(ZFTNotifyController.class);
    /**
     * 通知地址(
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("ZFTNotify")
    public String ZFTNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("支付通通知开始================");
        try {
        	request.setCharacterEncoding("UTF-8");
    		String body = request.getParameter("body");
    		String sign = request.getParameter("sign");
    		/**
    		 * {"reqId":"1491533532196","totalAmount":"1","transAmount":"1","transTime":"20170407111743",
    		 * "currency":"CNY","transType":"1","transResult":"2","resultMsg":"","acquirerType":"wechat",
    		 * "walletTransId":"4008822001201704076146085629","walletOrderId":"1704070000004045"}
    		 */
    		log.info("支付通扫码异步通知数据："+body);
    		log.info("支付通扫码异步通知签名："+sign);
    		if(body!=null&&body.length()>0){
    			JSONObject resJsonObj = JSONObject.fromObject(body);
    			//PayChannelRotation rotation = new PayChannelRotationDAO().getPayChannelRotationOrderNo(resJsonObj.getString("reqId"));
    			if("2".equals(resJsonObj.getString("transResult"))){
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = resJsonObj.getString("reqId");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
    			}
    		}else throw new Exception("通知数据为空");
    		
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	e.printStackTrace();
        } 
        return null;
    }
}