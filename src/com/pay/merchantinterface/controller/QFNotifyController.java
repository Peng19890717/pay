package com.pay.merchantinterface.controller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.qianfang.MD5Util;

@Controller
public class QFNotifyController {
	private static final Log log = LogFactory.getLog(QFNotifyController.class);
    /**
     * 通知地址
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("QFSCANNotify")
    public String QFSCANNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("钱方微信扫码通知开始==========================");
    	OutputStream os = null;
    	InputStream is = null;
    	try {
    		request.setCharacterEncoding("utf-8");
        	response.setCharacterEncoding("utf-8");
    		String sign = request.getHeader("X-QF-SIGN");
    		StringBuffer req = new StringBuffer("");
    		is = request.getInputStream();
    		os = response.getOutputStream();
    		byte[] b = new byte[1024];
    		int len = -1;
    		while((len = is.read(b)) != -1) req.append(new String(b,0,len,"utf-8"));
    		log.info("钱方通知(sign):"+sign);
    		log.info("钱方通知:"+req.toString());
    		/**
    		 * {"status": "1", "sysdtm": "2017-08-30 16:23:39", "paydtm": "2017-08-30 16:24:46",
    		 *  "goods_name": "", "txcurrcd": "CNY", "mchid": "Z9OLBhzKKO", "cancel": "0",
    		 *   "pay_type": "800201", "cardcd": "oo3Lss3w6xTcmP6gWmtPTyH39t5w", 
    		 *   "txdtm": "2017-08-30 16:23:39", "txamt": "1", "out_trade_no": "J6YG2KL63JLXPT2PU",
    		 *    "syssn": "20170830005200020018438094", "respcd": "0000", "goods_info": "", "notify_type": "payment"}
    		 */
    		Map<String,Object> resParamMap = JSON.parseObject(req.toString());
    		String newSign = MD5Util.encode(req.toString()+PayConstant.PAY_CONFIG.get("QF_KEY")).toUpperCase();
    		if(sign.equals(newSign)){
    			 if("1".equals(resParamMap.get("status").toString())){
    				 	PayOrder tmpPayOrder = new PayOrder();
    	            	tmpPayOrder.payordno = resParamMap.get("out_trade_no").toString();
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="01";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
    			 }
    		 }else throw new Exception("验签失败");
    		os.write("SUCCESS".getBytes());
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if(is!=null)try {is.close();} catch (Exception e) {}
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
}