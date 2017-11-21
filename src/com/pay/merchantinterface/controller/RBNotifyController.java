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

import util.PayUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jweb.dao.Blog;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.RBPayService;
import com.pay.order.dao.PayOrder;
import com.third.reapal.Decipher;
import com.third.reapal.Md5Utils;
/**
 * 融保支付通知接口
 * @author Administrator
 *
 */
@Controller
public class RBNotifyController {
	private static final Log log = LogFactory.getLog(RBNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("RBNotify")
    public String RBNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("融保直连网银通知开始==========================");
    	PrintWriter writer = null;
        try {
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		writer = response.getWriter();
    		
    		String merchant_id = request.getParameter("merchant_id");
    		String data = request.getParameter("data");
    		String encryptkey = request.getParameter("encryptkey");
    		//解析密文数据
    		String decryData = Decipher.decryptData(encryptkey,data);
    		//获取融宝支付的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
    		JSONObject jsonObject = JSON.parseObject(decryData);	
    		String merchantId = jsonObject.getString("merchant_id");
    		String tradeNo = jsonObject.getString("trade_no");
    		String orderNo = jsonObject.getString("order_no");
    		String totalFee = jsonObject.getString("total_fee");
    		String status = jsonObject.getString("status");
    		String resultCode = jsonObject.getString("result_code");
    		String resultMsg = jsonObject.getString("result_msg");
    		String sign = jsonObject.getString("sign");
    		String notifyId = jsonObject.getString("notify_id");
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("merchant_id", merchantId);
    		map.put("trade_no", tradeNo);
    		map.put("order_no", orderNo);
    		map.put("total_fee", totalFee);
    		map.put("status", status);
    		map.put("result_code", resultCode);
    		map.put("result_msg", resultMsg);
    		map.put("notify_id", notifyId);
    		log.info(map.toString());
    		//将返回的参数进行验签
    		String mysign = Md5Utils.BuildMysign(map, PayConstant.PAY_CONFIG.get("rb_key"));
    		if(mysign.equals(sign) ){
    			if(!status.equals("TRADE_FINISHED"))throw new Exception("支付未完成（"+orderNo+"）"+status);
    			PayOrder tmpPayOrder = new PayOrder();
            	tmpPayOrder.payordno = orderNo;
            	tmpPayOrder.actdat = new Date();
            	tmpPayOrder.ordstatus="01";
            	tmpPayOrder.bankjrnno = tradeNo;
            	new NotifyInterface().notifyMer(tmpPayOrder);
    		} else throw new Exception("验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        	if(log!=null)log.info("融保通知,"+e.getMessage());
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
    /**
     * 通知地址(快捷支付，目前和智联网银相同)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("RBQuickNotify")
    public String RBQuickNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("融保快捷通知开始==========================");
    	PrintWriter writer = null;
        try {
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		writer = response.getWriter();
//    		data:EjEBC5bSFaKQQe552b2LX5PWaxBH78pe/EWkmH7Nk/E3Mloui9ccBOaFqMhxu4DaY6A+vfULz3DqjAXzTg6wPpmwTdh+3XDNvQqsoQWXPZSA7YiIjU4i4mVXPgyHpz4wU4k11t6YNU14FwpgGZ8WXxdorIteojCaLR45xsTapUiM/Voh/EiVdFT4MGfe3h1YMH9aBp2c7y1Ye/tcuUIihQu6Z655m+NzkTWQm5anZ2NTL30PR/4aNDRUrd7cX+tRI5Vui2Hb6rJv53OtoE7teA==
//    		encryptkey:mHpS+4uQB+hHZy3pCNUDhdGeXulyH6uBGBUiHka9yrRY8VGCTi7+igODVrOqmdrtV5NqZZA41HuqIKefKfUnAs5Gf/3Cwd0AIkowLyAPz9mveUtuPH2penAMWazpU9/9Jz7Ows62PszQy5Nw4iOrr3nzMoSN7jW5ZOxhvyJf/gpRcEOAzVgO98R503x9mF/K/SSQZqKgwAZDsY1OJAU53lnBu7koBfCr4C0qvO1H3he7UMdLxznRYCKqDe6tUcbtPPrUD/xZx6mdIwvL/l72KisrYsrOPJasFwG4YdeOCs02g3oeZ/AYZhnwhLabuIrLGKhIqA3zId06TmhOWa8qbw==
//    		merchant_id:100000000011015
    		String merchant_id = request.getParameter("merchant_id");
    		String data = request.getParameter("data");
    		String encryptkey = request.getParameter("encryptkey");
    		//解析密文数据
    		String decryData = Decipher.decryptData(encryptkey,data);
    		//获取融宝支付的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
    		JSONObject jsonObject = JSON.parseObject(decryData);	
    		String merchantId = jsonObject.getString("merchant_id");
    		String tradeNo = jsonObject.getString("trade_no");
    		String orderNo = jsonObject.getString("order_no");
    		String totalFee = jsonObject.getString("total_fee");
    		String status = jsonObject.getString("status");
    		String resultCode = jsonObject.getString("result_code");
    		String resultMsg = jsonObject.getString("result_msg");
    		String sign = jsonObject.getString("sign");
    		String notifyId = jsonObject.getString("notify_id");
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("merchant_id", merchantId);
    		map.put("trade_no", tradeNo);
    		map.put("order_no", orderNo);
    		map.put("total_fee", totalFee);
    		map.put("status", status);
    		map.put("result_code", resultCode);
    		map.put("result_msg", resultMsg);
    		map.put("notify_id", notifyId);
    		log.info(map.toString());
    		//将返回的参数进行验签
    		String mysign = Md5Utils.BuildMysign(map, PayConstant.PAY_CONFIG.get("rb_key"));
    		if(mysign.equals(sign) ){
    			if(!status.equals("TRADE_FINISHED"))throw new Exception("支付未完成（"+orderNo+"）"+status);
    			PayOrder tmpPayOrder = new PayOrder();
            	tmpPayOrder.payordno = orderNo;
            	tmpPayOrder.actdat = new Date();
            	tmpPayOrder.ordstatus="01";
            	tmpPayOrder.bankjrnno = tradeNo;
            	new NotifyInterface().notifyMer(tmpPayOrder);
    		} else throw new Exception("验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        	if(log!=null)log.info("融保通知,"+e.getMessage());
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
    /**
     * 通知地址(代付)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("RBWithdrawNotify")
    public String RBWithdrawNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("融保代付通知开始==========================");
    	InputStream is = null;
        try {        	
        	is = request.getInputStream();
        	byte [] b = new byte[1024];
        	int len = -1;
        	String c = "";
        	while((len=is.read(b))!=-1)c = c + new String(b,0,len);
        	new RBPayService().withdrawNotify(c);        	
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(is!=null)try {is.close();} catch (Exception e) {}
        }
        return null;
    }
}