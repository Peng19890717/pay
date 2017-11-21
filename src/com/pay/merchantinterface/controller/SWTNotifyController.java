package com.pay.merchantinterface.controller;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
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
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.swt.util.MerchantMD5;
import com.third.swt.util.SignUtils;

/**
 * 商物通扫码支付通知
 * @author Administrator
 *
 */
@Controller
public class SWTNotifyController {
	private static final Log log = LogFactory.getLog(SWTNotifyController.class);
    /**
     * 通知地址(
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("SWTNotify")
    public String SWTNotify(HttpServletRequest request,HttpServletResponse response) {
    	PrintWriter writer = null;
    	log.info("商物通通知开始================");
        try {
        	writer = response.getWriter();
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		String versionId = request.getParameter("versionId")==null?"":request.getParameter("versionId");
    		String merchantId = request.getParameter("merchantId")==null?"":request.getParameter("merchantId");
    		String orderId = request.getParameter("orderId")==null?"":request.getParameter("orderId");
    		String settleDate = request.getParameter("settleDate")==null?"":request.getParameter("settleDate");
    		String completeDate = request.getParameter("completeDate")==null?"":request.getParameter("completeDate");
    		String status = request.getParameter("status")==null?"":request.getParameter("status");
    		String notifyTyp = request.getParameter("notifyTyp")==null?"":request.getParameter("notifyTyp");
    		String payOrdNo = request.getParameter("payOrdNo")==null?"":request.getParameter("payOrdNo");
    		String orderAmt = request.getParameter("orderAmt")==null?"":request.getParameter("orderAmt");
    		String notifyUrl = request.getParameter("notifyUrl")==null?"":request.getParameter("notifyUrl");
    		String signType = request.getParameter("signType")==null?"":request.getParameter("signType");
    		String signature = request.getParameter("signature")==null?"":request.getParameter("signature");

    		Map<String, String> params = new LinkedHashMap<String, String>();
    		params.put("versionId", versionId);
    		params.put("merchantId", merchantId);
    		params.put("orderId", orderId);
    		params.put("settleDate", settleDate);
    		params.put("completeDate", completeDate);
    		params.put("status" , status);
    		params.put("notifyTyp", notifyTyp);
    		params.put("payOrdNo", payOrdNo);
    		params.put("orderAmt", orderAmt);
    		params.put("notifyUrl", notifyUrl);
    		params.put("signType", signType);
    		String source = SignUtils.createLinkString(params);
    		log.info("商物通扫码异步通知："+source);
    		if (signType.equals("MD5")) {
    			String self_signature = MerchantMD5.MD5(source + PayConstant.PAY_CONFIG.get("SWT_PAY_KEY"));
    			if (self_signature.equals(signature)) {
    				if("1".equals(status)){
    					PayOrder tmpPayOrder = new PayOrder();
    	            	tmpPayOrder.payordno = orderId;
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="01";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
    				}else{
    					PayOrder tmpPayOrder = new PayOrder();
    	            	tmpPayOrder.payordno = orderId;
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="02";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
    				}
    				writer.write("success");
    			} else throw new Exception("验签失败");
    		}
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	writer.write("fail");
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}