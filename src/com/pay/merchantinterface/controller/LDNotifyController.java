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

import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.third.ld.DataUtil;
import com.third.ld.LDUtil;
import com.third.ld.PlainUtil;
import com.third.ld.SignUtil;
/**
 * 联动支付通知接口
 * @author xk
 *
 */
@Controller
public class LDNotifyController {
	private static final Log log = LogFactory.getLog(LDNotifyController.class);
    /**
     * 联动网银通知
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("LDPayNotify")
    public void LDPayNotify(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	log.info("联动网银通知开始==========================");
    	HashMap<String, String> resData = new HashMap<String, String>();
        HashMap<String, String> data = new HashMap<String, String>();
        PrintWriter write = response.getWriter();
    	try {
            data = (HashMap<String, String>) DataUtil.getData(request);
    		if(data==null || data.size()==0)throw new Exception("待解析的数据对象为空");
    		else log.info("网银通知数据:"+data.toString());
            if(SignUtil.verify(data.get("sign").toString(), PlainUtil.notifyPlain(request,false).get("plain").toString())){//验签
            	if("TRADE_SUCCESS".equals(data.get("trade_state"))){
            		PayOrder tmpPayOrder = new PayOrder();
					tmpPayOrder.payordno = (String) data.get("order_id");
					tmpPayOrder.actdat = new Date();
					tmpPayOrder.ordstatus="01";
					new NotifyInterface().notifyMer(tmpPayOrder);
            	}
            	resData.put("ret_code","0000");
            	resData.put("mer_id", data.get("mer_id")); 
                resData.put("sign_type", data.get("sign_type"));
                resData.put("version", data.get("version"));
                resData.put("order_id", data.get("order_id"));
                resData.put("mer_date", data.get("mer_date"));
            }else {
            	resData.put("ret_code","1111");
            }
            String resString = LDUtil.merNotifyResData(resData);
            resString = "<html><META NAME=\"MobilePayPlatform\" CONTENT=\"" + resString + "\" /></html>" ;
            write.print(resString);
            write.flush();
            write.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
    }
}