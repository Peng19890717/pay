package com.pay.merchantinterface.controller;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.Tools;

import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.yyt.DSDES;
/**
 *  银盈通通知接口
 * @author Administrator
 *
 */
@Controller
public class YYTNotifyController {
	private static final Log log = LogFactory.getLog(YYTNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("YYTWGNotify")
    public String YYTWGNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("银盈通网关通知开始==========================");
    	OutputStream os = null;
        try {
    		request.setCharacterEncoding("utf-8");
    		response.setHeader("Content-type","text/html;charset=utf-8");
    		os=response.getOutputStream();
    		String dstbdata = request.getParameter("dstbdata");
    		String dstbdatasign = request.getParameter("dstbdatasign");
    		log.info("银盈通网关通知:"+dstbdata);
    		String desdata = DSDES.getBlackData(PayConstant.PAY_CONFIG.get("YYT_KEY").getBytes(), dstbdata.getBytes("utf-8"));
    		Map<String,String> resMap = new HashMap<String, String>();
    		if(desdata.equals(dstbdatasign)){
    			String[] resTemp1 = Tools.split(dstbdata, "&");
    			for(String resTemp2:resTemp1){
    				String[] resTemp3 = resTemp2.split("=");
    				resMap.put(resTemp3[0], resTemp3[1]);
    			}
    			if("00".equals(resMap.get("returncode"))){
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = resMap.get("dsorderid");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="01";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
	            	os.write("00".getBytes());
    			}else{
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = resMap.get("dsorderid");
	            	tmpPayOrder.actdat = new Date();
	            	tmpPayOrder.ordstatus="02";
	            	new NotifyInterface().notifyMer(tmpPayOrder);
	            	os.write("00".getBytes());
    			}
    		}else new Exception("验签失败");
    		os.write("00".getBytes());
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	try {
				os.write("00".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
}