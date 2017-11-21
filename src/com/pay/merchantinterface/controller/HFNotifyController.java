package com.pay.merchantinterface.controller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.RSA;
import com.pay.order.dao.PayOrder;

import util.PayUtil;
@Controller
public class HFNotifyController {
	private static final Log log = LogFactory.getLog(HFNotifyController.class);
	
	public static String privateKey = PayConstant.PAY_CONFIG.get("PAY_HF_WX_PIV_KEY");
	//恒丰扫码收到系统提供的公钥，在收到异步回调通知或者响应报文时，进行验签使用
	public static String hfbankPublicKey =PayConstant.PAY_CONFIG.get("PAY_HF_WX_PUB_KEY");
	
	@RequestMapping("HFNotify")
    public String HFNotify(HttpServletRequest request,HttpServletResponse response) {
		OutputStream os = null;
    	InputStream is = null;
    	log.info("恒丰通知开始================");
        try {
        	 request.setCharacterEncoding("UTF-8");
        	 is= request.getInputStream();
        	 os=response.getOutputStream();
        	 String str_temp="";
     		 byte[] b = new byte[1024];
     		 int len= -1;
     		 while((len = is.read(b))!=-1){
     			str_temp+=new String(b,0,len,"UTF-8");
     		 }
     		 log.info("恒丰扫码异步通知数据:"+str_temp);
             String oldSign =request.getHeader("sign");
             JSONObject resJsonObj = JSONObject.fromObject(str_temp);
             //验签
			 boolean result = RSA.verify(str_temp, oldSign, hfbankPublicKey, "UTF-8");
			 if(result){
				 if("01".equals(resJsonObj.get("orderStatus"))){
	                	PayOrder tmpPayOrder = new PayOrder();
	                	tmpPayOrder.payordno = resJsonObj.get("merOrderNo").toString();
		            	tmpPayOrder.actdat = new Date();
		            	tmpPayOrder.ordstatus="01";
		            	new NotifyInterface().notifyMer(tmpPayOrder);
		            	//返回确认收到支付成功结果
		            	String resData = "{\"txStatus\":\"SUCCESS\"}";
					 	String sign = RSA.sign(resData, privateKey, "UTF-8");
		                response.setHeader("sign", sign);
		                os.write(resData.getBytes());
	                }else{
	                	PayOrder tmpPayOrder = new PayOrder();
	                	tmpPayOrder.payordno = resJsonObj.get("merOrderNo").toString();
		            	tmpPayOrder.actdat = new Date();
		            	tmpPayOrder.ordstatus="02";
		            	new NotifyInterface().notifyMer(tmpPayOrder);
	                }
			 }else throw new Exception("验签失败");
			 
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	e.printStackTrace();
        } finally {
        	if(is!=null)try {is.close();} catch (Exception e) {}
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
}
