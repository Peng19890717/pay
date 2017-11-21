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

import com.PayConstant;
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.coopbank.dao.PayChannelRotationDAO;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.wft.SignUtils;
import com.third.wft.XmlUtils;

@Controller
public class WFTNotifyController {

	private static final Log log = LogFactory.getLog(WFTNotifyController.class);
    
    @RequestMapping("WFTNotify")
    public String EMAXNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("威富通手机QQ扫码 通知开始==========================");
    	PrintWriter writer = null;
    	try {
        	writer = response.getWriter();
        	request.setCharacterEncoding("utf-8");
    		//取得通知信息
        	String resString = XmlUtils.parseRequst(request);
        	log.info("威富通手机QQ扫码 通知结果："+resString);
        	String respString = "fail";
        	if(resString != null && !"".equals(resString)){
        		Map<String,String> map = XmlUtils.toMap(resString.getBytes(), "utf-8");
        		PayChannelRotation rotation = new PayChannelRotationDAO().getPayChannelRotationOrderNo(map.get("out_trade_no"));
            	String key = rotation!=null?rotation.md5Key:PayConstant.PAY_CONFIG.get("WFT_MERC_PRIVATEKEY");//key
        		if(SignUtils.checkParam(map, key)){
                    if("0".equals(map.get("status"))){
                    	if("0".equals(map.get("result_code"))){
                    		if("0".equals(map.get("pay_result"))){
                    			PayOrder tmpPayOrder = new PayOrder();
                    			tmpPayOrder.payordno = map.get("out_trade_no");
                    			tmpPayOrder.actdat = new Date();
                    			tmpPayOrder.ordstatus="01";
                    			new NotifyInterface().notifyMer(tmpPayOrder);
                    		} else {
                    			PayOrder tmpPayOrder = new PayOrder();
                            	tmpPayOrder.payordno = map.get("out_trade_no");
                            	tmpPayOrder.actdat = new Date();
                            	tmpPayOrder.ordstatus="02";
                            	new NotifyInterface().notifyMer(tmpPayOrder);
                    		}
                    		respString = "success";
                    	}else throw new Exception("威富通手机QQ扫码 接收异步通知失败！");
                    }else throw new Exception("威富通手机QQ扫码 接收异步通知失败！");
        		}else throw new Exception("威富通手机QQ扫码 接收异步通知失败！");
        	}
        	writer.write(respString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(writer!=null)try {writer.close();} catch (Exception e) {}
		}
    	return null;
    }
}
