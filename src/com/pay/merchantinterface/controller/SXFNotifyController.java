package com.pay.merchantinterface.controller;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.sxf.PaymentUtils;

@Controller
public class SXFNotifyController {
	private static final Log log = LogFactory.getLog(SXFNotifyController.class);
    
    @RequestMapping("SXFNotify")
    public String SXFNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("随行付通知开始==========================");
    	PrintWriter writer = null;
        try {
        	 writer= response.getWriter();
        	//获取商户密钥
        	String mercPrivateKey=PayConstant.PAY_CONFIG.get("SXF_MERCPRIVATEKEY");
			String sxfPublic=PayConstant.PAY_CONFIG.get("SXF_SXFPUBLIC");
        	// 获取参数
        	String _t=request.getParameter("_t");
        	log.info(_t);
        	net.sf.json.JSONObject resJson = net.sf.json.JSONObject.fromObject(_t);
        	String mercNo =resJson.getString("mercNo");
            String orderNo =resJson.getString("orderNo");
        	String tranCd = resJson.getString("tranCd");
        	String resCode = resJson.getString("resCode");
        	String resMsg = resJson.getString("resMsg");
        	if(!"000000".equals(resCode))new Exception("随行付通知失败");
        	String resData = resJson.getString("resData");
        	String sign = resJson.getString("sign");
        	net.sf.json.JSONObject main = new net.sf.json.JSONObject();
        	//注意签名顺序
        	main.put("mercNo", mercNo);
        	main.put("orderNo", orderNo);
        	main.put("tranCd", tranCd);
        	main.put("resCode", resCode);
        	main.put("resMsg", resMsg);
        	main.put("resData", resData);
        	//验签
        	boolean check=PaymentUtils.doCheck(main.toString(), sign, sxfPublic);
        	if(check){
            	String resData_temp=PaymentUtils.decrypt(resData, mercPrivateKey);
            	JSONObject jsonObject = JSON.parseObject(resData_temp);
            	if("S".equals(jsonObject.getString("tranSts"))){
            		PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = orderNo;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	new NotifyInterface().notifyMer(tmpPayOrder);
				}else if("F".equals(jsonObject.getString("tranSts"))){
					PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = orderNo;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="02";
                	new NotifyInterface().notifyMer(tmpPayOrder);
				}
            	writer.write("SUCCESS");
            	writer.flush();
        	}else new Exception("验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}