package com.pay.merchantinterface.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.BNS.WechatScannedNotify;


/**
 * 北京农商银行扫码支付通知接口
 * @author Administrator
 *
 */
@Controller
public class BNSNotifyController {
	private static final Log log = LogFactory.getLog(BNSNotifyController.class);
    /**
     * 通知地址(
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("BNSNotify")
    public String BNSNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("北京农商银行微信扫码通知开始==========================");
    	OutputStream os = null;
    	try {
    		request.setCharacterEncoding("utf-8");
    		os=response.getOutputStream();
    		InputStream inputStream;
    		StringBuffer sb = new StringBuffer();
    		inputStream = request.getInputStream();
    		String s;
    		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    		while ((s = in.readLine()) != null) {
    			sb.append(s);
    		}
    		in.close();
    		inputStream.close();
    		String notifyData = sb.toString();
    		log.info("通知响应==================\n"+notifyData);
    		JSONObject jsonObject = JSON.parseObject(notifyData);
    		String return_code = jsonObject.getString("return_code");
    		if("SUCCESS".equals(return_code)){
    			String key = PayConstant.PAY_CONFIG.get("BNS_KEY");
    			String return_msg=jsonObject.getString("return_msg");
    			String appid=jsonObject.getString("appid");
    			String mch_id=jsonObject.getString("mch_id");
    			String device_info=jsonObject.getString("device_info");
    			String nonce_str=jsonObject.getString("nonce_str");
    			String result_code=jsonObject.getString("result_code");
    			String trade_state=jsonObject.getString("trade_state");
    			String err_code=jsonObject.getString("err_code");
    			String err_code_des=jsonObject.getString("err_code_des");
    			String openid=jsonObject.getString("openid");
    			String is_subscribe=jsonObject.getString("is_subscribe");
    			String trade_type=jsonObject.getString("trade_type");
    			String bank_type=jsonObject.getString("bank_type");
    			int total_fee=Integer.parseInt(jsonObject.getString("total_fee"));
    			String fee_type=jsonObject.getString("fee_type");
    			String transaction_id=jsonObject.getString("transaction_id");
    			String wechat_transaction_id=jsonObject.getString("wechat_transaction_id");
    			String out_trade_no=jsonObject.getString("out_trade_no");
    			String attach=jsonObject.getString("attach");
    			String time_end=jsonObject.getString("time_end");
    			WechatScannedNotify callbackRequest = 
    					new WechatScannedNotify(key, return_code, return_msg, appid, mch_id, device_info, nonce_str, result_code, trade_state,err_code, err_code_des, openid, is_subscribe, trade_type, bank_type, total_fee, fee_type, transaction_id, wechat_transaction_id, out_trade_no, attach, time_end);
        		if(callbackRequest.getSign().equals(jsonObject.getString("sign"))){
        			if("SUCCESS".equals(jsonObject.getString("trade_state"))){
        				PayOrder tmpPayOrder = new PayOrder();
    	            	tmpPayOrder.payordno = jsonObject.getString("out_trade_no");
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="01";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
    	            	os.write("SUCCESS".getBytes());
        			}
        		}else new Exception("验签失败");
    		}else new Exception("异步通知通信失败。");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {};
        }
        return null;
    }
}