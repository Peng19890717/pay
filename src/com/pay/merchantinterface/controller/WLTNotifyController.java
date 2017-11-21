package com.pay.merchantinterface.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.wlt.RequestUtil;
import com.third.wlt.SignUtil;

/**
 * 沃雷特 异步通知地址
 * @author lqq
 *
 */
@Controller
public class WLTNotifyController {
	
	private static final Log log = LogFactory.getLog(WLTNotifyController.class);
	
    @RequestMapping("WLTPayNotify")
    public String WLTPayNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("=====沃雷特接收异步通知开始=====");
    	PrintWriter writer = null;
    	try {
    		String result = RequestUtil.getRequestBody(request);
            log.info("沃雷特-接收到快捷通知信息为:" + result);
            writer = response.getWriter();
            if(StringUtils.isEmpty(result)){
            	writer.write("fail");
            }else{
            	@SuppressWarnings("unchecked")
				HashMap<String,String> map = JSON.parseObject(result, HashMap.class);
                if(SignUtil.verifySign(map, PayConstant.PAY_CONFIG.get("WLT_PUB_KEY"))){
                	//验签成功
                	writer.write("success");
                	if ("02".equals(map.get("tradeStatus"))) {
                		PayOrder tmpPayOrder = new PayOrder();
    					tmpPayOrder.payordno = map.get("mchTradeNo");
    					tmpPayOrder.actdat = new Date();
    					tmpPayOrder.ordstatus="01";
    					new NotifyInterface().notifyMer(tmpPayOrder);
                	}
                }else{
                	log.info("沃雷特-异步通知验签失败");
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			writer.write("fail");
		}finally{
			if(writer!=null) try {writer.close();} catch (Exception e) {}
		}
        return null;
    }

}
