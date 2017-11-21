package com.pay.merchantinterface.controller;
import java.io.PrintWriter;
import java.util.Date;
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
import com.third.jyt.baseUtil.WGUtil;
import com.third.jyt.mockClientMsgBase.MockClientMsgBase_RNpay;
/**
 * 金运通支付通知接口
 * @author Administrator
 *
 */
@Controller
public class JYTNotifyController {
	private static final Log log = LogFactory.getLog(JYTNotifyController.class);
	/**
     * 通知地址(网关)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("JYTWGNotify")
    public String JYTWGNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("金运通通知开始(网关)==========================");
    	Blog log = null;
    	PrintWriter writer = null;
        try {
        	log = new Blog(this.getClass().getSimpleName()+".JYTNotify","金运通网关通知","admin",request);
        	request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		writer = response.getWriter();
        	Map<String, String> reqMap = WGUtil.getRequestMap(request);
    		log.info("接收到金运通通知：" + reqMap);
    		String ressign =WGUtil.getSign(reqMap, PayConstant.PAY_CONFIG.get("JYT_WG_PAY_KEY"));
    		if("S0000000".equals(reqMap.get("respCode"))){
    			if(ressign.equals(reqMap.get("sign"))){
    				if("02".equals(reqMap.get("tranState"))){
    					PayOrder tmpPayOrder = new PayOrder();
                    	tmpPayOrder.payordno =reqMap.get("oriMerOrderId");
                    	tmpPayOrder.actdat = new Date();
                    	tmpPayOrder.ordstatus="01";
                    	new NotifyInterface().notifyMer(tmpPayOrder);
    				}else if("03".equals(reqMap.get("tranState"))){
    					PayOrder tmpPayOrder = new PayOrder();
                    	tmpPayOrder.payordno =reqMap.get("oriMerOrderId");
                    	tmpPayOrder.actdat = new Date();
                    	tmpPayOrder.ordstatus="01";
                    	new NotifyInterface().notifyMer(tmpPayOrder);
    				}
    			}else throw new Exception("验签失败");
    		}
    		writer.write("S0000000");
        } catch (Exception e) {
        	e.printStackTrace();
        	if(log!=null)log.info("金运通通知,"+e.getMessage());
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
    @RequestMapping("JYTNotify")
    public String JYTNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("金运通快捷通知开始(快捷)==========================");
    	PrintWriter writer = null;
        try {
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		writer = response.getWriter();
    		String merchant_id = request.getParameter("merchant_id");		// 商户号。
    		String xml_enc = request.getParameter("xml_enc");	// 密文数据。
    		String key_enc = request.getParameter("key_enc");		// 会话密钥密文
    		String sign = request.getParameter("sign");	// 签名数据。
    		MockClientMsgBase_RNpay base =  new MockClientMsgBase_RNpay();
    		String xmlData = base.notifyMessageToXml(merchant_id, xml_enc, key_enc, sign);
    		log.info("金运通快捷通知响应报文"+xmlData);
    		String respCode = base.getMsgRespCode(xmlData);
    		if(respCode.equals("S0000000")) {
    			if("00".equals(base.getMsgState(xmlData))){//支付成功
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno =base.getOrderId(xmlData);
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			} else if("03".equals(base.getMsgState(xmlData))) {//支付失败。
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno =base.getOrderId(xmlData);
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="02";
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}
    		}
    		writer.write("S0000000");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}