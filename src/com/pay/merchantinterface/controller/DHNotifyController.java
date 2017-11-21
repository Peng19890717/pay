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

import util.PayUtil;

import com.PayConstant;
import com.pay.merchantinterface.service.DHPayService;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.daohe.JsonToMapUtil;
/**
 * 道合支付通知接口
 * @author Administrator
 *
 */
@Controller
public class DHNotifyController {
	private static final Log log = LogFactory.getLog(DHNotifyController.class);
    /**
     * 通知地址(微信扫码)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("DHNotify")
    public String DHNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("道合扫码通知开始==========================");
    	OutputStream os = null;
    	InputStream is = null;
    	try {
    		//取得通知信息
    		StringBuffer req = new StringBuffer("");
    		is = request.getInputStream();
    		os = response.getOutputStream();
    		byte[] b = new byte[1024];
    		int len = -1;
    		while((len = is.read(b)) != -1) req.append(new String(b,0,len,"utf-8"));
    		log.info("道合扫码异步通知:"+req.toString());
    		Map<String, Object> rmap = JsonToMapUtil.toMap(req.toString());
			Map<String, Object> _body = (Map<String, Object>) rmap.get("REP_BODY");
			Map<String, Object>  repHead= (Map<String, Object>) rmap.get("REP_HEAD");
			String rep_sign = String.valueOf(repHead.get("sign"));
			DHPayService service = new DHPayService();
			if(rep_sign.equals(service.getSign(_body,PayConstant.PAY_CONFIG.get("DH_KEY")))){
				if("01".equals(_body.get("orderState"))){
					PayOrder tmpPayOrder = new PayOrder();
	            	tmpPayOrder.payordno = _body.get("orderId").toString();
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
				}else if ("02".equals(_body.get("orderState"))){
					PayOrder tmpPayOrder = new PayOrder();
	            	tmpPayOrder.payordno = _body.get("orderId").toString();
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="02";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
				}
			}else throw new Exception("道合扫码支付验签失败");
    		os.write("SUCCESS".getBytes());
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        	if(is != null) try { is.close(); } catch (Exception e) {}
        }
        return null;
    }
}