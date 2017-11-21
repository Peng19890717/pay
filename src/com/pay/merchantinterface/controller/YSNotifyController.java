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

import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.YSPayService;
import com.pay.order.dao.PayOrder;
import com.third.ys.util.ParamUtil;
/**
 *  易势通知接口
 * @author Administrator
 *
 */
@Controller
public class YSNotifyController {
	private static final Log log = LogFactory.getLog(YSNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("YSWGNotify")
    public String YSWGNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("易势网关通知开始==========================");
    	PrintWriter writer = null;
        try {
        	writer = response.getWriter();
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		Map<String, String> transMap =ParamUtil.getParamMap(request);
    		log.info("易势网关通知参数:"+transMap.toString());
    		/**
    		 * 易势通知参数:{YUL1=, amount=10000, merchantNo=000020161103001, paySerialNo=2017050800136674, remark=, rtnCode=0002, rtnMsg=, settDate=, 
    		 * sign=SxkiP/XBcBAqKdR6xnXINnZnOHdZcRWyNKPwH9+NToIJ+8o5V39e+tAMAvIsmbwSj1gj5QQyNIA6cx77xwRpspbK8XBbIVUHu/65CVHolEAUZEYzFpqkXnbNhC3cuhDSvNd0W6TbzQMW9tHdF0hdvVxQiuLjSZPoPpLNva8YGrY=,
    		 *  tranSerialNum=qj1hZFk2Q3rKTOG}
    		 */
    		String sign = transMap.get("sign");
    		transMap.remove("sign");
    		//验签
    		String transData = ParamUtil.getSignMsg(transMap);
    		boolean result = YSPayService.certUtil.verify(transData, sign);
    		if(result){
    			if(transMap.get("rtnCode").equals("0000")){
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = transMap.get("tranSerialNum");
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	tmpPayOrder.bankjrnno = "";
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}
    			writer.write("YYYYYY");
    		 } else throw new Exception("验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	writer.write("YYYYYY");
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
    /**
     * 通知地址(扫码)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("YSSCANNotify")
    public String YSSCANNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("易势扫码通知开始==========================");
    	PrintWriter writer = null;
        try {
        	writer = response.getWriter();
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		Map<String, String> transMap =ParamUtil.getParamMap(request);
    		log.info("易势扫码通知参数:"+transMap.toString());
    		String sign = transMap.get("sign");
    		transMap.remove("sign");
    		//验签
    		String transData = ParamUtil.getSignMsg(transMap);
    		boolean result = YSPayService.certUtil.verify(transData, sign);
    		if(result){
    			if(transMap.get("rtnCode").equals("0000")){
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = transMap.get("tranFlow");
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	tmpPayOrder.bankjrnno = "";
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}
    			writer.write("YYYYYY");
    		 } else throw new Exception("验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	writer.write("YYYYYY");
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}