package com.pay.merchantinterface.controller;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.MD5;
import util.PayUtil;

import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
/**
 * 银生宝支付通知接口
 * @author Administrator
 *
 */
@Controller
public class YSBNotifyController {
	private static final Log log = LogFactory.getLog(YSBNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("YSBWGNotify")
    public String YSBWGNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("银生宝网关通知开始==========================");
    	OutputStream os = null;
        try {
        	response.setContentType("text/html;charset=UTF-8");
        	//接收参数
        	request.setCharacterEncoding("utf-8");
        	String merchantId = request.getParameter("merchantId")==null?"":request.getParameter("merchantId");//商户编号
        	String responseMode = request.getParameter("responseMode")==null?"":request.getParameter("responseMode");
        	String orderId = request.getParameter("orderId")==null?"":request.getParameter("orderId");
        	String currencyType = request.getParameter("currencyType")==null?"":request.getParameter("currencyType");
        	String amount = request.getParameter("amount")==null?"":request.getParameter("amount");
        	String returnCode = request.getParameter("returnCode")==null?"":request.getParameter("returnCode");
        	String returnMessage = request.getParameter("returnMessage")==null?"":request.getParameter("returnMessage");
        	String merchantKey = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_KEY");
        	String mac = request.getParameter("mac")==null?"":request.getParameter("mac");
        	//merchantId=2120170103101821001&responseMode=2&responseMode=2&orderId=qj0OWAx2Q3rKTOG&currencyType=CNY&amount=0.01&returnCode=0000&returnMessage=&merchantKey=qiantongzhifu123
        	//qiantongzhifu123
        	String signParams="merchantId="+merchantId+"&responseMode="+responseMode+"&orderId="+orderId+
        						"&currencyType="+currencyType+"&amount="+amount+"&returnCode="+returnCode+"&returnMessage="+returnMessage+"&merchantKey="+merchantKey;
        	log.info("sign="+mac);
        	log.info(signParams);
        	String mac_temp =MD5.getDigest(signParams).toUpperCase();
    		if(mac.equals(mac_temp)){
    			if(returnCode.equals("0000")){
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = orderId;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	tmpPayOrder.bankjrnno = "";
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}
    		 } else throw new Exception("验签失败");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	try {
        		os = response.getOutputStream();
        		if(os!=null)os.write((e.getMessage()==null?"":e.getMessage()).getBytes("utf-8"));
        	} catch (IOException e1) {}
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
    /**
     * 通知地址(代扣)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("YSBDKNotify")
    public String YSBDKNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("银生宝代扣通知开始==========================");
    	OutputStream os = null;
        try {
        	response.setContentType("text/html;charset=UTF-8");
        	//接收参数
        	request.setCharacterEncoding("utf-8");
        	String accountId = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_ID");
        	String result_code = request.getParameter("result_code")==null?"":request.getParameter("result_code");
        	String result_msg = request.getParameter("result_msg")==null?"":request.getParameter("result_msg");
        	String amount = request.getParameter("amount")==null?"":request.getParameter("amount");
        	String orderId = request.getParameter("orderId")==null?"":request.getParameter("orderId");
        	String merchantKey = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_KEY");
        	String mac = request.getParameter("mac")==null?"":request.getParameter("mac");
        	String signParams="accountId="+accountId+"&orderId="+orderId+"&amount="+amount+"&result_code="+result_code+
        						"&result_msg="+result_msg+"&key="+merchantKey;
        	log.info(signParams);
        	String mac_temp =MD5.getDigest(signParams).toUpperCase();
    		if(mac.equals(mac_temp)){
    			if(result_code.equals("0000")){
    				PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
    				List<PayReceiveAndPay> list = dao.getBatchPayReceiveAndPayById(orderId);//通过通知的单号，查询当前代扣记录。
    				if(list.size()>0){
    					PayReceiveAndPay payReceiveAndPay = list.get(0);
    					payReceiveAndPay.status="1";
    					payReceiveAndPay.respCode="000";
    					payReceiveAndPay.retCode="000";
    					payReceiveAndPay.errorMsg="处理成功";
    					dao.updatePayReceiveAndPay(list);
    					//平台包装快捷，通知
    					if("1".equals(payReceiveAndPay.bussFromType)){
	    					PayOrder tmpPayOrder = new PayOrder();
	                    	tmpPayOrder.payordno = payReceiveAndPay.id;
	                    	tmpPayOrder.actdat = new Date();
	                    	tmpPayOrder.ordstatus="01";
	                    	tmpPayOrder.bankjrnno = "";
	                    	new NotifyInterface().notifyMer(tmpPayOrder);
    					}
    				} else {
    					throw new Exception("订单号: ,<"+orderId+">无此代扣记录!");
    				}
    			} else {
    				PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
    				List<PayReceiveAndPay> list = dao.getBatchPayReceiveAndPayById(orderId);//通过通知的单号，查询当前代扣记录。
    				if(list.size()>0){
    					PayReceiveAndPay payReceiveAndPay = list.get(0);
    					payReceiveAndPay.status="2";
    					payReceiveAndPay.respCode="-1";
    					payReceiveAndPay.retCode="-1";
    					payReceiveAndPay.errorMsg=result_msg;
    					dao.updatePayReceiveAndPay(list);
    					//平台包装快捷，通知
    					if("1".equals(payReceiveAndPay.bussFromType)){
	    					PayOrder tmpPayOrder = new PayOrder();
	                    	tmpPayOrder.payordno = payReceiveAndPay.id;
	                    	tmpPayOrder.actdat = new Date();
	                    	tmpPayOrder.ordstatus="01";
	                    	tmpPayOrder.bankjrnno = "";
	                    	tmpPayOrder.backerror=result_msg;
	                    	new NotifyInterface().notifyMer(tmpPayOrder);
    					}
    				} else {
    					throw new Exception("订单号: ,<"+orderId+">无此代扣记录!");
    				}
    			}
    		 } else throw new Exception("验签失败");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	try {
        		os = response.getOutputStream();
        		if(os!=null)os.write((e.getMessage()==null?"":e.getMessage()).getBytes("utf-8"));
        	} catch (IOException e1) {}
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
}