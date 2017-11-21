package com.pay.merchantinterface.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import util.MD5;
import util.PayUtil;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.merchantinterface.service.PayInterfaceOtherService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayReceiveAndPayService;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.third.jingdong.util.DES;
import com.third.jingdong.util.XmlMsg;

/**
 * 京东支付通知接口
 * @author Administrator
 *
 */
@Controller
public class JDNotifyController {
	private static final Log log = LogFactory.getLog(JDNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("JDNotify")
    public String JDNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("京东通知地址(直连网银)==================");
    	PrintWriter writer = null;
        try {
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		writer = response.getWriter();
    		String v_oid = request.getParameter("v_oid");		// 订单号
    		String v_pstatus = request.getParameter("v_pstatus");	// 支付结果，20支付完成；30支付失败；
    		String v_amount = request.getParameter("v_amount");		// 订单实际支付金额
    		String v_moneytype = request.getParameter("v_moneytype");	// 币种
    		String v_md5str = request.getParameter("v_md5str");		// MD5校验码
    		String v_md5text = MD5.getDigest(v_oid+v_pstatus+v_amount+v_moneytype+
    				PayConstant.PAY_CONFIG.get("JD_B2C_WEB_MD5_KEY")).toUpperCase();
    		//将返回的参数进行验签
    		if (v_md5str!=null&&v_md5str.equals(v_md5text)) {
    			writer.print("ok"); // 告诉服务器验证通过,停止发送
    			if ("20".equals(v_pstatus)) {//支付结果，20支付完成；30支付失败；
        			PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = v_oid;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	tmpPayOrder.bankjrnno = v_oid;
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}

    		} else {
    			writer.print("error"); // 告诉服务器验证通过,停止发送
    			throw new Exception("验签失败");
    		}
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
   
	@RequestMapping("JDQuickPayNotify")
    public String JDQuickPayNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("京东快捷支付通知开始==========================");
    	OutputStream os = null;
    	InputStream is = null;
    	try {
    		//取得通知信息
    		StringBuffer req = new StringBuffer("");
    		is = request.getInputStream();
    		os = response.getOutputStream();
    		byte[] b = new byte[1024];
    		int len = -1;
    		while((len = is.read(b)) != -1) req.append(new String(b,0,len,"utf-8"));
    		//数据格式是resp=XML数据
    		String temResp = req.toString().substring(req.toString().indexOf("=") + 1);
    		temResp=java.net.URLDecoder.decode(temResp);
    		//快捷支付数据先base64解码
    		temResp = new String(Base64.decode(temResp));
    		//解析xml中的数据
    		Map<String, String> respParams= XmlMsg.parse(temResp);
    		//验证数据签名的合法性。版本号+商户号+终端号+交易数据使用md5加密和数据签名比较，md5密钥在网银在线商户后台设置
    		if(com.third.jingdong.util.MD5.verify(respParams.get("chinabank.version")
    				+respParams.get("chinabank.merchant")+respParams.get("chinabank.terminal")
    				+respParams.get("chinabank.data"), PayConstant.PAY_CONFIG.get("JD_QUICKPAY_MD5KEY"), respParams.get("chinabank.sign"))){
    				//使用DES解密data交易数据,des密钥网银在线商户后台设置
    				String respdataDES = DES.decrypt(respParams.get("chinabank.data"),PayConstant.PAY_CONFIG.get("JD_QUICKPAY_DES"),"UTF-8");
    				log.info("京东快捷消费通知报文--交易信息:"+respdataDES);
    				Map<String, String> dataParams= XmlMsg.parse(respdataDES);
    				if("0000".equals(dataParams.get("data.return.code"))){
    					if("0".equals(dataParams.get("data.trade.status"))){
    						PayOrder tmpPayOrder = new PayOrder();
        	            	tmpPayOrder.payordno = dataParams.get("data.trade.id");
        	            	tmpPayOrder.actdat = new Date();
        	            	tmpPayOrder.ordstatus="01";
        	            	new NotifyInterface().notifyMer(tmpPayOrder);
    					} else if ("7".equals(dataParams.get("data.trade.status"))){
    						PayOrder tmpPayOrder = new PayOrder();
        	            	tmpPayOrder.payordno = dataParams.get("data.trade.id");
        	            	tmpPayOrder.actdat = new Date();
        	            	tmpPayOrder.ordstatus="02";
        	            	new NotifyInterface().notifyMer(tmpPayOrder);
    					}
    				}else{
    					throw new Exception("交易返回码："+dataParams.get("data.return.code")+"||"+"交易返回码描述："+dataParams.get("data.return.desc"));
    				}
    		 }else{
    			throw new Exception("验签失败");
    		 }
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        	if(is != null) try { is.close(); } catch (Exception e) {}
        }
        return null;
    }
	@RequestMapping("JDDFPayNotify")
    public String JDDFPayNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("京东代付通知开始==========================");
    	OutputStream os = null;
    	InputStream is = null;
    	try {
    		//取得通知信息
    		StringBuffer req = new StringBuffer("");
    		is = request.getInputStream();
    		os = response.getOutputStream();
    		byte[] b = new byte[1024];
    		int len = -1;
    		while((len = is.read(b)) != -1) req.append(new String(b,0,len,"utf-8"));
    		com.third.jingdongdaifu.wangyin.enctypt.util.RequestUtil demoUtil = new com.third.jingdongdaifu.wangyin.enctypt.util.RequestUtil();
    		Map<String,String> map = demoUtil.verifySingNotify(req.toString());
    		log.info("京东代付通知数据:"+map);
			if(map==null){
				throw new Exception("验签失败");
			}else{
				if("FINI".equals(map.get("trade_status"))){
					PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
        			PayReceiveAndPay rp = new PayReceiveAndPay();
    				rp.id=map.get("out_trade_no");
    				rp.status="1";
					rp.retCode="000";
					rp.errorMsg="交易成功";
					dao.updatePayReceiveAndPaystatus(rp);
					new PayReceiveAndPayService().notifyReceiveAndPayMer(rp);
				}else if("CLOS".equals(map.get("trade_status"))){
					PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
        			PayReceiveAndPay rp = new PayReceiveAndPay();
    				rp.id=map.get("out_trade_no");
    				rp.status="2";
					rp.retCode="-1";
					rp.errorMsg="交易失败";
					dao.updatePayReceiveAndPaystatus(rp);
				}
			}
			os.write("success".getBytes());
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        	if(is != null) try { is.close(); } catch (Exception e) {}
        }
        return null;
    }
}