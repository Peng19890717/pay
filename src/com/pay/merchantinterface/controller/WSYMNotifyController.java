package com.pay.merchantinterface.controller;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import util.PayUtil;
import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.wsym.MD5Util;

/**
 * 网上有名
 * @author Administrator
 *
 */
@Controller
public class WSYMNotifyController {
	private static final Log log = LogFactory.getLog(WSYMNotifyController.class);
    /**
     * 通知地址(
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("WSYMNotify")
    public String WSYMNotify(HttpServletRequest request,HttpServletResponse response) {
    	PrintWriter writer = null;
    	log.info("网上有名通知开始================");
        try {
        	
        	 String versionId = request.getParameter("versionId");
             String merchantId = request.getParameter("merchantId");
             String orderId = request.getParameter("orderId");
             String settleDate = request.getParameter("settleDate");
             String completeDate = request.getParameter("completeDate");
             String status = request.getParameter("status");
             String notifyTyp = request.getParameter("notifyTyp");
             String payOrdNo = request.getParameter("payOrdNo");
             String orderAmt = request.getParameter("orderAmt");
             String notifyUrl = request.getParameter("notifyUrl");
             String signType = request.getParameter("signType");
             String oldsignature = request.getParameter("signature");
             writer=response.getWriter();
             String sourceData = "versionId=" + versionId + "&merchantId=" + merchantId +
                     "&orderId=" + orderId + "&settleDate=" + settleDate + "&completeDate=" + completeDate +
                     "&status=" + status + "&notifyTyp=" + notifyTyp + "&payOrdNo=" + payOrdNo +
                     "&orderAmt=" + orderAmt + "&notifyUrl=" + notifyUrl + "&signType=" + signType;
             log.info("网上有名异步通知参数:"+sourceData+"&signature="+oldsignature);
              //MD5验签方式
             String newsignature = MD5Util.getMD5(sourceData+PayConstant.PAY_CONFIG.get("WSYM_MD5_KEY"));
             if (oldsignature.equals(newsignature)) {
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
                writer.write("000000");
             }else throw new Exception("验签失败");
       
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	e.printStackTrace();
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
    
    @RequestMapping("WSYMDFNotify")
    public String WSYMDFNotify(HttpServletRequest request,HttpServletResponse response) {
    	PrintWriter writer = null;
    	log.info("网上有名代付通知开始================");
        try {
        	 String amount = request.getParameter("amount");
             String lowerPayOrderNo = request.getParameter("lowerPayOrderNo");
             String payDesc = request.getParameter("payDesc");
             String payTranId = request.getParameter("payTranId");
             String rspMsg = request.getParameter("rspMsg");
             String rspType = request.getParameter("rspType");
             String singleFee = request.getParameter("singleFee");
             String oldsignature = request.getParameter("signature");
             writer=response.getWriter();
             String sourceData = "amount=" + amount + "&lowerPayOrderNo=" + lowerPayOrderNo +
                     "&payDesc=" + payDesc + "&payTranId=" + payTranId + "&rspMsg=" + rspMsg +
                     "&rspType=" + rspType + "&singleFee=" + singleFee ;
             log.info("网上有名代付异步通知参数:"+sourceData+"&signature="+oldsignature);
              //MD5验签方式
             String newsignature = MD5Util.getMD5(sourceData+PayConstant.PAY_CONFIG.get("WSYM_MD5_KEY"));
             if (oldsignature.equals(newsignature)) {
                if("S".equals(rspType)){
                	PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = lowerPayOrderNo;
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
                }else{
                	PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = lowerPayOrderNo;
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="02";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
                }
                writer.write("000000");
             }else throw new Exception("验签失败");
       
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	e.printStackTrace();
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}