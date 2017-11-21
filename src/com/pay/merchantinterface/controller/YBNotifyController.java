package com.pay.merchantinterface.controller;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.fastjson.JSON;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.YBPayService;
import com.pay.order.dao.PayOrder;

@Controller
public class YBNotifyController {
	private static String charset = "GBK";
	private static final Log log = LogFactory.getLog(YBNotifyController.class);

	/**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("YBPayNotify")
    public String YBPayNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("易宝网银通知开始==========================");
    	PrintWriter writer = null;
        try {
        	writer = response.getWriter();
        	//接收参数
        	request.setCharacterEncoding(charset);
        	String p1_MerId				= formatString(request.getParameter("p1_MerId"));
        	String r0_Cmd               = formatString(request.getParameter("r0_Cmd"));
        	String r1_Code              = formatString(request.getParameter("r1_Code"));
        	String r2_TrxId             = formatString(request.getParameter("r2_TrxId"));
        	String r3_Amt               = formatString(request.getParameter("r3_Amt"));
        	String r4_Cur               = formatString(request.getParameter("r4_Cur"));
        	
        	String tmp_r5 = request.getQueryString().substring(request.getQueryString().indexOf("r5_Pid")+7);
        	String result = tmp_r5.substring(0,tmp_r5.indexOf("&"));
        	String r5_Pid               = formatString(URLDecoder.decode(result,"gbk"));
        	String r6_Order             = formatString(request.getParameter("r6_Order"));
        	String r7_Uid               = formatString(request.getParameter("r7_Uid"));
        	String r8_MP                = formatString(request.getParameter("r8_MP"));
        	String r9_BType             = formatString(request.getParameter("r9_BType"));
        	String hmac		            = formatString(request.getParameter("hmac"));
        	String hmac_safe            = formatString(request.getParameter("hmac_safe"));
        	String[] strArr	= {p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType };
        	log.info("易宝异步通知响应数据："+JSON.toJSONString(strArr));
        	boolean hmacIsCorrect = YBPayService.verifyCallbackHmac_safe(strArr, hmac);
        	boolean hmacsafeIsCorrect = YBPayService.verifyCallbackHmac_safe(strArr, hmac_safe);
        	if(hmacIsCorrect && hmacsafeIsCorrect) {
        		if(r9_BType.equals("2")) {//服务器点对点
        			if("1".equals(r1_Code)){
        				PayOrder tmpPayOrder = new PayOrder();
    					tmpPayOrder.payordno = p1_MerId;
    					tmpPayOrder.actdat = new Date();
    					tmpPayOrder.ordstatus="01";
    					new NotifyInterface().notifyMer(tmpPayOrder);
        			}
        			writer.write("SUCCESS");
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
    public String formatString(String text) {
		return (text == null) ? "" : text.trim();
	}
}