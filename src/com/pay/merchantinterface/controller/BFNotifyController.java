package com.pay.merchantinterface.controller;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

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
import com.pay.merchantinterface.service.RBPayService;
import com.pay.order.dao.PayOrder;
/**
 * 宝付支付通知接口
 * @author Administrator
 *
 */
@Controller
public class BFNotifyController {
	private static final Log log = LogFactory.getLog(BFNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("BFNotify")
    public String RBNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("宝付通知开始==========================");
    	PrintWriter writer = null;
        try {
        	writer = response.getWriter();
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		String MemberID = request.getParameter("MemberID");
    		String TerminalID = request.getParameter("TerminalID");
    		String TransID = request.getParameter("TransID");
    		String Result = request.getParameter("Result");
    		String ResultDesc = request.getParameter("ResultDesc");
    		String AdditionalInfo = request.getParameter("AdditionalInfo");
    		String SuccTime = request.getParameter("SuccTime");
    		String FactMoney = request.getParameter("FactMoney");
    		String Md5Sign = request.getParameter("Md5Sign");
    		String src = "MemberID="+MemberID+"~|~TerminalID="+TerminalID+"~|~" +
    				"TransID="+TransID+"~|~Result="+Result+"~|~ResultDesc="+ResultDesc+"~|~" +
    				"FactMoney="+FactMoney+"~|~AdditionalInfo="+AdditionalInfo+"~|~" +
    				"SuccTime="+SuccTime+"~|~Md5Sign="+PayConstant.PAY_CONFIG.get("bf_web_md5_key");
    		log.info(src);
    		String sign = MD5.getDigest(src);
    		//解析密文数据
    		if(Md5Sign!=null&&Md5Sign.equals(sign) ){
    			if(!"1".equals(Result))throw new Exception("支付未完成（"+TransID+"）"+Result);
    			PayOrder tmpPayOrder = new PayOrder();
            	tmpPayOrder.payordno = TransID;
            	tmpPayOrder.actdat = new Date();
            	tmpPayOrder.ordstatus="01";
            	tmpPayOrder.bankjrnno = TransID;
            	new NotifyInterface().notifyMer(tmpPayOrder);
            	writer.write("OK");
    		} else throw new Exception("验签失败");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	writer.write("FAIL");
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
    @RequestMapping("BFQuickNotify")
    public String RBQuickNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("宝付快捷通知开始==========================");
    	PrintWriter writer = null;
        try {
        	writer = response.getWriter();
    		request.setCharacterEncoding("UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		response.setHeader("Content-type","text/html;charset=UTF-8");
    		//将返回的参数进行验签
//    		if(mysign.equals(sign) ){
//    			if(!status.equals("TRADE_FINISHED"))throw new Exception("支付未完成（"+orderNo+"）"+status);
//    			PayOrder tmpPayOrder = new PayOrder();
//            	tmpPayOrder.payordno = orderNo;
//            	tmpPayOrder.actdat = new Date();
//            	tmpPayOrder.ordstatus="01";
//            	tmpPayOrder.bankjrnno = tradeNo;
//            	new NotifyInterface().notifyMer(tmpPayOrder);
//    		} else throw new Exception("验签失败");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
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
    @RequestMapping("BFWithdrawNotify")
    public String RBWithdrawNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("宝付代付通知开始==========================");
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