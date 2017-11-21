package com.pay.merchantinterface.controller;

import java.io.InputStream;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.emaxPlus.EmaxPlusUtil;

@Controller
public class EjNotifyController {

	private static final Log log = LogFactory.getLog(EjNotifyController.class);
    
    @RequestMapping("EMAXNotify")
    public String EMAXNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("溢+ 通知开始==========================");
    	PrintWriter writer = null;
    	InputStream is = null;
    	try {
        	writer = response.getWriter();
        	request.setCharacterEncoding("utf-8");
    		//取得通知信息
    		StringBuffer req = new StringBuffer("");
    		is = request.getInputStream();
    		byte[] b = new byte[1024];
    		int len = -1;
    		while((len = is.read(b)) != -1) req.append(new String(b,0,len,"utf-8"));
    		log.info("通知响应==================\n"+req.toString());
    		JSONObject jsonObject = JSON.parseObject(req.toString());
        	//获取参数
        	Map<String, String > paramMap = new HashMap<String,String>();
            paramMap.put("resultStatus",jsonObject.getString("resultStatus"));
            paramMap.put("resultCode",jsonObject.getString("resultCode"));
            paramMap.put("resultMsg",jsonObject.getString("resultMsg"));
            paramMap.put("resultTime",jsonObject.getString("resultTime"));
            paramMap.put("merCode",jsonObject.getString("merCode"));
            paramMap.put("orderNo",jsonObject.getString("orderNo"));
            paramMap.put("orderAmount",jsonObject.getString("orderAmount"));//// TODO: 2017/4/21  订单金额应该是接入方自己数据库金额，而非传递的金额,且不带小数点,单位为分
            paramMap.put("payDate",jsonObject.getString("payDate"));
            paramMap.put("payCompletionDate",jsonObject.getString("payCompletionDate"));
            String url = EmaxPlusUtil.getSignPlainText(paramMap);
            String sign = EmaxPlusUtil._md5Encode(url);
            String tmp_sign = jsonObject.getString("sign");
            if(!sign.equals(tmp_sign)) {
                throw new RuntimeException("签名不正确,商户签名字符串:"+url+".<br>商户签名:"+sign+".<br>溢+签名:"+tmp_sign);
            }else{
            	if("000000".equals(jsonObject.getString("resultCode"))
            			&&"SUCCESS".equals(jsonObject.getString("resultStatus"))){
            		PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = jsonObject.getString("orderNo");
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	new NotifyInterface().notifyMer(tmpPayOrder);	
            	}else{
            		PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = jsonObject.getString("orderNo");
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="02";
                	new NotifyInterface().notifyMer(tmpPayOrder);
            	}
            	writer.write("SUCCESS");
            	writer.flush();
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(writer!=null)try {writer.close();} catch (Exception e) {}
		}
    	return null;
    }
}
