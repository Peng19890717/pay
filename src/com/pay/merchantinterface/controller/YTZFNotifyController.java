package com.pay.merchantinterface.controller;
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
import com.third.ytzf.tools.GatewayRequest;

@Controller
public class YTZFNotifyController {
	private static final Log log = LogFactory.getLog(YTZFNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("YTZFWGNotify")
    public String YTZFWGNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("易通支付网关通知开始==========================");
        try {
        	//商户密钥
        	String key = PayConstant.PAY_CONFIG.get("YTZF_MD5_KEY");;
        	//创建支付应答对象
        	GatewayRequest notify = new GatewayRequest(request, response,GatewayRequest.VERIFY_SIGN);
        	log.info("异步通知参数:"+notify.getParameters());
        	//设置商户密钥
        	notify.setKey(key);
        	//验证签名
        	if(notify.verifySign()) {
        		if("00".equals(notify.getParameter("code"))) {
        			if("3".equals(notify.getParameter("status"))){
        				PayOrder tmpPayOrder = new PayOrder();
    	            	tmpPayOrder.payordno = notify.getParameter("order_id");
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="01";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
        			}else if("4".equals(notify.getParameter("status"))){
        				PayOrder tmpPayOrder = new PayOrder();
    	            	tmpPayOrder.payordno = notify.getParameter("order_id");
    	            	tmpPayOrder.actdat = new Date();
    	            	tmpPayOrder.ordstatus="02";
    	            	new NotifyInterface().notifyMer(tmpPayOrder);
        			}
        		}
        		//返回Success给易票联网关服务器，表示已经收到后台通知，不必再次发送通知
        		notify.responseToGateway("success");
        	} else throw new Exception("异步通知验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
}