package com.pay.merchantinterface.controller;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.ght.RSA;
/**
 * 高汇通支付通知接口
 * @author xk
 *
 */
@Controller
public class GHTNotifyController {
	private static String charset = "utf-8";
	private static final Log log = LogFactory.getLog(GHTNotifyController.class);
	private static String publicKey = PayConstant.PAY_CONFIG.get("GHT_PUB_KEY");//高汇通公钥
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("GHTPayNotify")
    public String GHTPayNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("高汇通网银通知开始==========================");
    	PrintWriter writer = null;
    	String tmp = "";
        try {
        	writer = response.getWriter();
        	String chnOrderDate = request.getParameter("chnOrderDate");
        	String chnOrderNo = request.getParameter("chnOrderNo");
        	String chnOutOrderNo = request.getParameter("chnOutOrderNo");
        	String merOrderId = request.getParameter("merOrderId");
        	String merchantId = request.getParameter("merchantId");
        	String payType = request.getParameter("payType");
        	String platform = request.getParameter("platform");
        	String retCode = request.getParameter("retCode");
        	String retMsg = request.getParameter("retMsg");
        	String tradeStatus = request.getParameter("tradeStatus");
        	String tranAmt = request.getParameter("tranAmt");
        	String tranNo = request.getParameter("tranNo");
        	String tranTime = request.getParameter("tranTime");
        	tmp = (chnOrderDate==null?"":"chnOrderDate="+chnOrderDate+"&")
        			+ (chnOrderNo==null?"":"chnOrderNo="+chnOrderNo+"&")
    				+ (chnOutOrderNo==null?"":"chnOutOrderNo="+chnOutOrderNo+"&")
    				+ (merOrderId==null?"":"merOrderId="+merOrderId+"&")
    				+ (merchantId==null?"":"merchantId="+merchantId+"&")
    				+ (payType==null?"":"payType="+payType+"&")
    				+ (platform==null?"":"platform="+platform+"&")
    				+ (retCode==null?"":"retCode="+retCode+"&")
    				+ (retMsg==null?"":"retMsg="+retMsg+"&")
    				+ (tradeStatus==null?"":"tradeStatus="+tradeStatus+"&")
    				+ (tranAmt==null?"":"tranAmt="+tranAmt+"&")
    				+ (tranNo==null?"":"tranNo="+tranNo+"&")
        			+ (tranTime==null?"":"tranTime="+tranTime);
        	log.info("高汇通网银异步通知:"+tmp); 
    		if(RSA.verifySign(tmp, request.getParameter("sign"), publicKey, charset)){
    			if("0000".equals(request.getParameter("retCode")) ){
    				if("02".equals(request.getParameter("tradeStatus"))){
    					PayOrder tmpPayOrder = new PayOrder();
                    	tmpPayOrder.payordno = merOrderId;
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="01";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
    				}else if("03".equals(request.getParameter("tradeStatus"))){
    					PayOrder tmpPayOrder = new PayOrder();
                    	tmpPayOrder.payordno = merOrderId;
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="02";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
    				}
    				writer.write("success");
    			}
    		}else throw new Exception("高汇通通知验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        	writer.write("FAIL");
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
    /**
     * 通知地址(快捷)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("GHTQuickPayNotify")
    public String GHTQuickPayNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("高汇通快捷通知开始==========================");
    	PrintWriter writer = null;
    	String tmp = "";
        try {
        	writer = response.getWriter();
        	String bankId = request.getParameter("bankId");
        	String bindId = request.getParameter("bindId");
        	String chnSettleDate = request.getParameter("chnSettleDate");
        	String merOrderId = request.getParameter("merOrderId");
        	String merchantId = request.getParameter("merchantId");
        	String payAcctNo = request.getParameter("payAcctNo");
        	String payerId = request.getParameter("payerId");
        	String platform = request.getParameter("platform");
        	String retCode = request.getParameter("retCode");
        	String retMsg = request.getParameter("retMsg");
        	String tradeStatus = request.getParameter("tradeStatus");
        	String tranAmt = request.getParameter("tranAmt");
        	String tranNo = request.getParameter("tranNo");
        	tmp = (bankId==null?"":"bankId="+bankId+"&")
        			+ (bindId==null?"":"bindId="+bindId+"&")
    				+ (chnSettleDate==null?"":"chnSettleDate="+chnSettleDate+"&")
    				+ (merOrderId==null?"":"merOrderId="+merOrderId+"&")
    				+ (merchantId==null?"":"merchantId="+merchantId+"&")
    				+ (payAcctNo==null?"":"payAcctNo="+payAcctNo+"&")
    				+ (payerId==null?"":"payerId="+payerId+"&")
    				+ (platform==null?"":"platform="+platform+"&")
    				+ (retCode==null?"":"retCode="+retCode+"&")
    				+ (retMsg==null?"":"retMsg="+retMsg+"&")
    				+ (tradeStatus==null?"":"tradeStatus="+tradeStatus+"&")
    				+ (tranAmt==null?"":"tranAmt="+tranAmt+"&")
    				+ (tranNo==null?"":"tranNo="+tranNo);
        	log.info("高汇通快捷异步通知:"+tmp);
    		if(RSA.verifySign(tmp, request.getParameter("sign"), publicKey, charset)){
    			if("0000".equals(request.getParameter("retCode")) ){
    				if("02".equals(request.getParameter("tradeStatus"))){
    					PayOrder tmpPayOrder = new PayOrder();
                    	tmpPayOrder.payordno = merOrderId;
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="01";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
    				}else if("03".equals(request.getParameter("tradeStatus"))){
    					PayOrder tmpPayOrder = new PayOrder();
                    	tmpPayOrder.payordno = merOrderId;
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="02";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
    				}
    				writer.write("success");
    			}
    		}else throw new Exception("高汇通通知验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        	writer.write("FAIL");
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}