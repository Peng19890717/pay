package com.pay.merchantinterface.controller;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.PayUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.merchantinterface.service.CXWeiXinService;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.third.cx.GatewayUtil;
import com.third.cx.RequestUtil;

/**
 * 创新支付通知接口
 * @author Administrator
 *
 */
@Controller
public class CXNotifyController {
	private static final Log log = LogFactory.getLog(CXNotifyController.class);
    /**
     * 通知地址(微信扫码)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("CXSCANNotify")
    public String CXSCANNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("创新微信扫码通知开始==========================");
    	OutputStream os = null;
    	InputStream is = null;
    	
    	try {
    		request.setCharacterEncoding("utf-8");
    		//取得通知信息
    		StringBuffer req = new StringBuffer("");
    		is = request.getInputStream();
    		os = response.getOutputStream();
    		byte[] b = new byte[1024];
    		int len = -1;
    		while((len = is.read(b)) != -1) req.append(new String(b,0,len,"utf-8"));
    		log.info("通知响应==================\n"+req.toString());
    		//商户业务处理{"endTime":"2016-10-28 17:47:14","mchTradeNo":"q0UWgZE2Q3rKTPe","orderNo":"20161028815530509457610404048574","payType":"12","sign":"P5fml+B7Wx8izzBEvlls4DyUeLm1hsPyQFZPX0b42GtbImla1OnUut+uSmEHZWEvJlkwo2laQCij39WF8p96yLqgSeWO6ZM1ShZvtLnzL7NFLxI1TJfBH81GxwvYxQOjETjSEnMtlqaduXjf6W6BELX/bBtXtqhDWPhoYUtfKG8=","subject":"小米4","tradeAmt":"0.01","tradeStatus":"02"}
    		JSONObject jsonObject = JSON.parseObject(req.toString());
    		Map <String,String>params = new HashMap <String,String>();
    		params.put("orderNo",jsonObject.getString("orderNo"));
			params.put("mchTradeNo",jsonObject.getString("mchTradeNo"));
			params.put("tradeStatus",jsonObject.getString("tradeStatus"));
			params.put("payType",jsonObject.getString("payType"));
			params.put("tradeAmt",jsonObject.getString("tradeAmt"));
			params.put("endTime",jsonObject.getString("endTime"));
			params.put("subject",jsonObject.getString("subject"));
			params.put("sign",jsonObject.getString("sign"));
    		if(GatewayUtil.veryfyResponse(params)){
    			if(!"02".equals(params.get("tradeStatus")))throw new Exception("支付未完成（"+params.get("mchTradeNo")+"）"
    						+ CXWeiXinService.status_map.get("tradeStatus"));
    			PayOrder tmpPayOrder = new PayOrder();
            	tmpPayOrder.payordno = params.get("mchTradeNo");
            	tmpPayOrder.actdat = new Date();
            	tmpPayOrder.ordstatus="01";
            	tmpPayOrder.bankjrnno = params.get("orderNo");
            	new NotifyInterface().notifyMer(tmpPayOrder);
    		} else throw new Exception("验签失败");
    		os.write("SUCCESS".getBytes());
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        	if(is != null) try { is.close(); } catch (Exception e) {}
        }
        return null;
    }
    @RequestMapping("CXDFNotify")
    public String CXDFNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("创新代付通知开始==========================");
    	OutputStream os = null;
    	InputStream is = null;
    	try {
    		String result =RequestUtil.getRequestBody(request);
    		Map<String,String > map =JSON.parseObject(result,Map.class);
    		if(GatewayUtil.veryfyResponse(map, PayConstant.PAY_CONFIG.get("CX_DF_RSA_PUB_KEY"))){
    			if("06".equals(map.get("status"))){
    				PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
        			PayReceiveAndPay rp = new PayReceiveAndPay();
    				rp.id=map.get("merBatchNo");
    				rp.status="1";
					rp.retCode="000";
					rp.errorMsg="交易成功";
					dao.updatePayReceiveAndPaystatus(rp);
    			}else if("07".equals(map.get("status"))){
    				PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
        			PayReceiveAndPay rp = new PayReceiveAndPay();
    				rp.id=map.get("merBatchNo");
    				rp.status="2";
					rp.retCode="-1";
					rp.errorMsg="交易失败";
					dao.updatePayReceiveAndPaystatus(rp);
    			}
    		}else{
    			throw new Exception("验签失败");
    		}
    		os = response.getOutputStream();
    		os.write("success".getBytes());
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        	if(is != null) try { is.close(); } catch (Exception e) {}
        }
        return null;
    }
    @RequestMapping("CXQUICKNotify")
    public String CXQUICKNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("创新快捷通知开始==========================");
    	OutputStream os = null;
    	try {
    		String merchantId = request.getParameter("merchantId");
    		String platform = request.getParameter("platform");
    		String retCode = request.getParameter("retCode");
    		String retMsg = request.getParameter("retMsg");
    		String merOrderId = request.getParameter("merOrderId");
    		String tranAmt = request.getParameter("tranAmt");
    		String tradeStatus = request.getParameter("tradeStatus");
    		String tranNo = request.getParameter("tranNo");
    		String bindId = request.getParameter("bindId");
    		String payerId = request.getParameter("payerId");
    		String payAcctNo = request.getParameter("payAcctNo");
    		String bankId = request.getParameter("bankId");
    		String chnSettleDate = request.getParameter("chnSettleDate");
    		String sign = request.getParameter("sign");
    		
    		Map<String, String> map = new HashMap<String,String>();
    		if(StringUtils.isNotEmpty(merchantId)){
    			map.put("merchantId", merchantId);
    		}
    		if(StringUtils.isNotEmpty(platform)){
    			map.put("platform", platform);
    		}
    		if(StringUtils.isNotEmpty(retCode)){
    			map.put("retCode", retCode);
    		}
    		if(StringUtils.isNotEmpty(retMsg)){
    			map.put("retMsg", retMsg);
    		}
    		if(StringUtils.isNotEmpty(merOrderId)){
    			map.put("merOrderId", merOrderId);
    		}
    		if(StringUtils.isNotEmpty(tranAmt)){
    			map.put("tranAmt", tranAmt);
    		}
    		if(StringUtils.isNotEmpty(tradeStatus)){
    			map.put("tradeStatus", tradeStatus);
    		}
    		if(StringUtils.isNotEmpty(tranNo)){
    			map.put("tranNo", tranNo);
    		}
    		if(StringUtils.isNotEmpty(bindId)){
    			map.put("bindId", bindId);
    		}
    		if(StringUtils.isNotEmpty(payerId)){
    			map.put("payerId", payerId);
    		}
    		if(StringUtils.isNotEmpty(payAcctNo)){
    			map.put("payAcctNo", payAcctNo);
    		}
    		if(StringUtils.isNotEmpty(bankId)){
    			map.put("bankId", bankId);
    		}
    		if(StringUtils.isNotEmpty(chnSettleDate)){
    			map.put("chnSettleDate", chnSettleDate);
    		}
    		if(StringUtils.isNotEmpty(sign)){
    			map.put("sign", sign);
    		}
    		log.info("创新快捷异步通知响应:"+map.toString());
    		if(GatewayUtil.veryfyResponse(map, PayConstant.PAY_CONFIG.get("CX_QUICK_PUBLIC_KEY"))){
    			if("02".equals(tradeStatus)){
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = merOrderId;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}else if("03".equals(tradeStatus)){
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = merOrderId;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="02";
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}
    		}else{
    			throw new Exception("验签失败");
    		}
    		os = response.getOutputStream();
    		os.write("success".getBytes());
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
    
    @RequestMapping("CXWGNotify")
    public String CXWGNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("创新网关通知开始==========================");
    	OutputStream os = null;
    	try {
    		String merchantId = request.getParameter("merchantId");
    		String platform = request.getParameter("platform");
    		String retCode = request.getParameter("retCode");
    		String retMsg = request.getParameter("retMsg");
    		String merOrderId = request.getParameter("merOrderId");
    		String tranAmt = request.getParameter("tranAmt");
    		String tradeStatus = request.getParameter("tradeStatus");
    		String tranNo = request.getParameter("tranNo");
    		String tranTime = request.getParameter("tranTime");
    		String chnOutOrderNo = request.getParameter("chnOutOrderNo");
    		String chnOrderNo = request.getParameter("chnOrderNo");
    		String chnOrderDate = request.getParameter("chnOrderDate");
    		String payType = request.getParameter("payType");
    		String sign = request.getParameter("sign");
    		
    		Map<String, String> map = new HashMap<String,String>();
    		if(StringUtils.isNotEmpty(merchantId)){
    			map.put("merchantId", merchantId);
    		}
    		if(StringUtils.isNotEmpty(platform)){
    			map.put("platform", platform);
    		}
    		if(StringUtils.isNotEmpty(retCode)){
    			map.put("retCode", retCode);
    		}
    		if(StringUtils.isNotEmpty(retMsg)){
    			map.put("retMsg", retMsg);
    		}
    		if(StringUtils.isNotEmpty(merOrderId)){
    			map.put("merOrderId", merOrderId);
    		}
    		if(StringUtils.isNotEmpty(tranAmt)){
    			map.put("tranAmt", tranAmt);
    		}
    		if(StringUtils.isNotEmpty(tradeStatus)){
    			map.put("tradeStatus", tradeStatus);
    		}
    		if(StringUtils.isNotEmpty(tranNo)){
    			map.put("tranNo", tranNo);
    		}
    		if(StringUtils.isNotEmpty(tranTime)){
    			map.put("tranTime", tranTime);
    		}
    		if(StringUtils.isNotEmpty(chnOutOrderNo)){
    			map.put("chnOutOrderNo", chnOutOrderNo);
    		}
    		if(StringUtils.isNotEmpty(chnOrderNo)){
    			map.put("chnOrderNo", chnOrderNo);
    		}
    		if(StringUtils.isNotEmpty(chnOrderDate)){
    			map.put("chnOrderDate", chnOrderDate);
    		}
    		if(StringUtils.isNotEmpty(payType)){
    			map.put("payType", payType);
    		}
    		if(StringUtils.isNotEmpty(sign)){
    			map.put("sign", sign);
    		}
    		log.info("创新网关异步通知响应:"+map.toString());
    		if(GatewayUtil.veryfyResponse(map, PayConstant.PAY_CONFIG.get("CX_WG_PUBLIC_KEY"))){
    			if("02".equals(tradeStatus)){
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = merOrderId;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}else if("03".equals(tradeStatus)){
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = merOrderId;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="02";
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}
    		}else{
    			throw new Exception("验签失败");
    		}
    		os = response.getOutputStream();
    		os.write("SUCCESS".getBytes());
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
}