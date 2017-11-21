package com.pay.merchantinterface.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.PayInterfaceOtherService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayReceiveAndPayService;
import com.third.gfb.SignVerifyUtil;
/**
 * 国付宝支付通知接口
 * @author lqq
 *
 */
@Controller
public class GFBNotifyController {
	private static String charset = "utf-8";
	private static final Log log = LogFactory.getLog(GFBNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("GFBPayNotify")
    public String GHTPayNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("国付宝网银通知开始==========================");
    	PrintWriter writer = null;
    	String tmp = "";
        try {
        	writer = response.getWriter();
        	request.setCharacterEncoding(charset);
        	//接收参数
    	   String version = request.getParameter("version");
    	   String charset = request.getParameter("charset");
    	   String language = request.getParameter("language");
    	   String signType = request.getParameter("signType");
    	   String tranCode = request.getParameter("tranCode");
    	   String merchantID = request.getParameter("merchantID");
    	   String merOrderNum = request.getParameter("merOrderNum");
    	   String tranAmt = request.getParameter("tranAmt");
    	   String feeAmt = request.getParameter("feeAmt");
    	   String frontMerUrl = request.getParameter("frontMerUrl");
    	   String backgroundMerUrl = request.getParameter("backgroundMerUrl");
    	   String tranDateTime = request.getParameter("tranDateTime");
    	   String tranIP = request.getParameter("tranIP");
    	   String respCode = request.getParameter("respCode");
    	   String msgExt = request.getParameter("msgExt");
    	   String orderId = request.getParameter("orderId");
    	   String gopayOutOrderId = request.getParameter("gopayOutOrderId");
    	   String bankCode = request.getParameter("bankCode");
    	   String tranFinishTime = request.getParameter("tranFinishTime");
    	   String merRemark1 =  request.getParameter("merRemark1");
    	   String merRemark2 =  request.getParameter("merRemark2");
    	   String VerficationCode = PayConstant.PAY_CONFIG.get("GFB_MER_JYM");//校验码
    	   String signValueFromGopay = request.getParameter("signValue");
    	   String plain = "version=[" + version + "]tranCode=[" + tranCode + "]merchantID=[" + merchantID + "]merOrderNum=[" + merOrderNum + "]tranAmt=[" + tranAmt + "]feeAmt=[" + feeAmt+ "]tranDateTime=[" + tranDateTime + "]frontMerUrl=[" + frontMerUrl + "]backgroundMerUrl=[" + backgroundMerUrl + "]orderId=[" + orderId + "]gopayOutOrderId=[" + gopayOutOrderId + "]tranIP=[" + tranIP + "]respCode=[" + respCode + "]gopayServerTime=[]VerficationCode=[" + VerficationCode + "]";
    	   log.info("国付宝异步通知响应数据:"+plain);
    	   String pubKeyStr = PayConstant.PAY_CONFIG.get("GFB_GFB_PUBLICKEY");
    	   if(SignVerifyUtil.verify(plain, signValueFromGopay, pubKeyStr)){
    		   log.info("===国付宝验签通过===");
    		   log.info("===respCode:"+respCode+"===");
    		   if ("0000".equals(respCode)) {//支付成功
					PayOrder tmpPayOrder = new PayOrder();
					tmpPayOrder.payordno = merOrderNum;
					tmpPayOrder.actdat = new Date();
					tmpPayOrder.ordstatus="01";
					new NotifyInterface().notifyMer(tmpPayOrder);
					response.getWriter().write("RespCode=0000|JumpURL=");
    		   } else {
					response.getWriter().write("RespCode=9999|JumpURL=");
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
    /**
     * 通知地址-代付
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("GFBDFNotify")
    public String GFBDFNotify(HttpServletRequest request,HttpServletResponse response) {
    	
    	log.info("国付宝代付通知开始==========================");
    	PrintWriter writer = null;
        try {
        	writer = response.getWriter();
        	request.setCharacterEncoding("UTF-8");
        	//接收参数
		    String notifyMsg = request.getParameter("notifyMsg");
	        log.info("国付宝通知参数；" + notifyMsg);
	        Document document = DocumentHelper.parseText(notifyMsg);
	        Element root = document.getRootElement();
	        String version = root.element("version").getText();
	        String tranCode = root.element("tranCode").getText();
	        String customerId = root.element("customerId").getText();
	        String merOrderNum = root.element("merOrderNum").getText();
	        String tranAmt = root.element("tranAmt").getText();
	        String feeAmt = root.element("feeAmt").getText();
	        String totalAmount = root.element("totalAmount").getText();
	        String merURL = root.element("merURL").getText();
	        String recvBankAcctNum = root.element("recvBankAcctNum").getText();
	        String tranDateTime = root.element("tranDateTime").getText();
	        String orderId = root.element("orderId").getText();
	        String respCode = root.element("respCode").getText();
	        String VerficationCode = (String)PayConstant.PAY_CONFIG.get("GFB_MER_JYM");
	        String signValueFromGopay = root.element("SignValue").getText();
    	    String plain = "version=[" + version + "]tranCode=[" + tranCode + "]customerId=[" + customerId + 
    			   "]merOrderNum=[" + merOrderNum + "]tranAmt=[" + tranAmt + "]feeAmt=[" + feeAmt+ 
    			   "]totalAmount=["+totalAmount+"]merURL=[" + merURL + "]recvBankAcctNum=[" + recvBankAcctNum + 
    			   "]tranDateTime=[" + tranDateTime + "]orderId=[" + orderId + "]respCode=[" + respCode + "]VerficationCode=[" + VerficationCode + "]";
    	   log.info("国付宝代付通知验签参数:"+plain);
    	   String pubKeyStr = PayConstant.PAY_CONFIG.get("GFB_GFB_PUBLICKEY");
    	   if(SignVerifyUtil.verify(plain, signValueFromGopay, pubKeyStr)){
    		   log.info("===国付宝验签通过===");
    		   log.info("===respCode:"+respCode+"===");
    		   if ("8".equals(respCode)) {//支付成功
    			   	PayReceiveAndPay rp = new  PayReceiveAndPay();
    			   	rp.id=merOrderNum;
					rp.status="1";
					rp.retCode="000";
					rp.errorMsg = "交易成功-实际到账以发卡银行为准";
					new PayReceiveAndPayDAO().updatePayReceiveAndPaystatus(rp);
					new PayReceiveAndPayService().notifyReceiveAndPayMer(rp);
					response.getWriter().write("RespCode=0000");
    		   } 
    	   }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}