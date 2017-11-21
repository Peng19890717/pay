package com.pay.merchantinterface.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.yltf.SignUtil;
/**
 * 亿联通付支付通知接口
 * @author xk
 *
 */
@Controller
public class YLTFNotifyController {
	private static final Log log = LogFactory.getLog(YLTFNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("YLTFPayNotify")
    public String YLTFPayNotify(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	log.info("亿联通付网银通知开始==========================");
    	response.setCharacterEncoding("utf-8");
    	PrintWriter out = response.getWriter();
        try {
        	Map<String, String> paras = new HashMap<String, String>();
			paras.put("mid", request.getParameter("mid"));
			paras.put("noise", request.getParameter("noise"));
			paras.put("orderNo", request.getParameter("orderNo"));
			paras.put("flowNo", request.getParameter("flowNo"));
			paras.put("tradeNo", request.getParameter("tradeNo"));
			paras.put("orderAmount", request.getParameter("orderAmount"));
			paras.put("succAmount", request.getParameter("succAmount"));
			paras.put("status", request.getParameter("status"));
			paras.put("orderTime", request.getParameter("orderTime"));
			paras.put("payTime", request.getParameter("payTime"));
			log.info("亿联通付网银异步通知:"+paras); 
			String sign = SignUtil.generateMD5(paras, PayConstant.PAY_CONFIG.get("YLTF_KEY_D0"));
			if(sign.equals(request.getParameter("sign"))){
				if("1".equals(request.getParameter("status"))){
					PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = request.getParameter("orderNo");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
	            	out.println("{\"code\":\"SUCCESS\",\"msg\":\"ok\"}");
				}
			}else throw new Exception("亿联通付通知验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        	out.write("FAIL");
        } finally {
        	if(out!=null)try {out.close();} catch (Exception e) {}
        }
        return null;
    }
}