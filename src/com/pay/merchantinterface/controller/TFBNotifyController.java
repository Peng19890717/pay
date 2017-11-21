package com.pay.merchantinterface.controller;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.tfb.util.RSAUtils;
import com.third.tfb.util.RequestUtils;

@Controller
public class TFBNotifyController {
	private static final Log log = LogFactory.getLog(TFBNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("TFBNotify")
    public String TFBNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("天付宝通知开始==========================");
    	PrintWriter writer = null;
        try {
        		writer = response.getWriter();
        		//获取天付宝GET过来反馈信息
        		String cipherData = request.getParameter("cipher_data");
        		String rsa_public_key=PayConstant.PAY_CONFIG.get("TFB_WG_RSA_PUBLIC_KEY");
        		String rsa_private_key=PayConstant.PAY_CONFIG.get("TFB_WG_RSA_PRIVATE_KEY");
        		//对服务器返回的加密数据进行rsa解密
    			String responseData = RSAUtils.decrypt(cipherData,rsa_private_key,"GBK");
    			log.info("天付宝异步通知数据:"+responseData);
    			//封装数据
    			HashMap<String, String> map = RequestUtils.parseString(responseData);
    			//rsa验签
    			if (RSAUtils.verify(map.get("source"), map.get("sign"),rsa_public_key,"GBK")) {
    				log.info("验签结果：通过");
    				if("1".equals(map.get("result"))){
    					PayOrder tmpPayOrder = new PayOrder();
                    	tmpPayOrder.payordno =map.get("spbillno");
                    	tmpPayOrder.actdat = new Date();
                    	tmpPayOrder.ordstatus="01";
                    	new NotifyInterface().notifyMer(tmpPayOrder);
                    	writer.write("<retcode>00</retcode>");
    				}
    			} else new Exception("验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}