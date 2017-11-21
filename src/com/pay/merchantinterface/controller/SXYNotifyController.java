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
import com.capinfo.crypt.Md5;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
/**
 * 首信易支付通知接口
 * @author Administrator
 *
 */
@Controller
public class SXYNotifyController {
	private static final Log log = LogFactory.getLog(SXYNotifyController.class);
    /**
     * 通知地址(直连网银)网银同步通知。
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("SXYPageNotify")
    public String SXYPageNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("首信易同步通知开始==========================");
    	PrintWriter writer = null;
        try {
        	  request.setCharacterEncoding("GB2312");
        	  writer= response.getWriter();
        	  String v_oid = request.getParameter("v_oid");//订单编号
        	  String v_pmode = new String(request.getParameter("v_pmode").getBytes("ISO-8859-1"),"GB2312");//支付方式
        	  String v_pstatus = request.getParameter("v_pstatus");//支付结果 20支付成功 30 支付失败
        	  String v_pstring = new String(request.getParameter("v_pstring").getBytes("ISO-8859-1"),"GB2312");//支付结果信息说明
        	  String v_amount = request.getParameter("v_amount");//订单金额
        	  String v_moneytype = request.getParameter("v_moneytype");//币种
        	  String v_md5money = request.getParameter("v_md5money");//数字指纹
        	  String v_md5info = request.getParameter("v_md5info");//数字指纹
        	  String v_md5info_source = v_oid + v_pstatus + v_pstring + v_pmode;
        	  String v_md5money_source = v_amount +v_moneytype; 
			  log.info("首信易同步通知:v_id="+v_oid+",v_pmode="+v_pmode+",v_pstatus="+v_pstatus+",v_pstring="+v_pstring+
					  ",v_count=,v_amount="+v_amount+",v_moneytype="+v_moneytype+",v_md5money="+v_md5money+",v_md5info="+v_md5info);
			  //md5加密1
			  Md5 md5 = new Md5 ("") ;
			  md5.hmac_Md5(v_md5info_source , PayConstant.PAY_CONFIG.get("sxy_sign_type")) ;
			  byte b[]= md5.getDigest();
			  String digestString = md5.stringify(b) ;
			  //md5加密2
			  Md5 m = new Md5 ("") ;
			  m.hmac_Md5(v_md5money_source ,PayConstant.PAY_CONFIG.get("sxy_sign_type")) ;
			  byte a[]= m.getDigest();
			  String digestString2 = m.stringify(a) ;
			  if(digestString2.equals(v_md5money) && digestString.equals(v_md5info)){
    			if(!v_pstatus.equals("20"))throw new Exception("支付未完成（"+v_oid+"）"+v_pstatus);
    			String[] oid_temp=v_oid.split("-");
    			if(oid_temp.length==3){
    				String payordno = oid_temp[2];
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = payordno;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	tmpPayOrder.bankjrnno = v_oid;
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}else new Exception("通知订单号格式错误:"+v_oid);
    			writer.write("支付完成") ;
    			writer.flush();
    		} else throw new Exception("验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
    
    /**
     * 通知地址(直连网银)网银异步通知。
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("SXYNotify")
    public String SXYNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("首信易异步通知开始==========================");
    	PrintWriter writer = null;
        try {
        	  request.setCharacterEncoding("GB2312");
        	  writer= response.getWriter();
        	  String v_oid = request.getParameter("v_oid");//订单编号
        	  String v_pmode = new String(request.getParameter("v_pmode").getBytes("ISO-8859-1"),"GB2312");//支付方式
        	  String v_pstatus = request.getParameter("v_pstatus");//支付结果 1支付成功 3 支付失败
        	  String v_pstring = new String(request.getParameter("v_pstring").getBytes("ISO-8859-1"),"GB2312");//支付结果信息说明
        	  String v_count = request.getParameter("v_count");//订单个数
        	  String v_amount = request.getParameter("v_amount");//订单金额
        	  String v_moneytype = request.getParameter("v_moneytype");//币种
        	  String v_md5money = request.getParameter("v_md5money");//数字指纹
        	  String v_mac = request.getParameter("v_mac");//数字指纹
        	  String source1 = v_oid + v_pmode + v_pstatus + v_pstring + v_count;
        	  String source2 = v_amount +v_moneytype;
        	  //md5加密1
        	  Md5 md5 = new Md5 ("") ;
        	  md5.hmac_Md5(source1 ,PayConstant.PAY_CONFIG.get("sxy_sign_type") ) ;
        	  byte b[]= md5.getDigest();
        	  String digestString = md5.stringify(b) ;
        	  //md5加密2
        	  Md5 m = new Md5 ("") ;
        	  m.hmac_Md5(source2 , PayConstant.PAY_CONFIG.get("sxy_sign_type")) ;
        	  byte a[]= m.getDigest();
        	  String digestString2 = m.stringify(a) ;
			  log.info("首信易异步通知:v_id="+v_oid+",v_pmode="+v_pmode+",v_pstatus="+v_pstatus+
					  ",v_pstring="+v_pstring+",v_count="+v_count+",v_amount="+v_amount+",v_moneytype="+v_moneytype+",v_md5money="+v_md5money+",v_mac="+v_mac);
			  if(digestString2.equals(v_md5money) && digestString.equals(v_mac)){
    			if(!v_pstatus.equals("1"))throw new Exception("支付未完成（"+v_oid+"）"+v_pstatus);
    			String[] oid_temp=v_oid.split("-");
    			if(oid_temp.length==3){
    				String payordno = oid_temp[2];
    				PayOrder tmpPayOrder = new PayOrder();
                	tmpPayOrder.payordno = payordno;
                	tmpPayOrder.actdat = new Date();
                	tmpPayOrder.ordstatus="01";
                	tmpPayOrder.bankjrnno = v_oid;
                	new NotifyInterface().notifyMer(tmpPayOrder);
    			}else new Exception("通知订单号格式错误:"+v_oid);
    			writer.write("sent") ;
    			writer.flush();
    		} else throw new Exception("验签失败");
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if(writer!=null)try {writer.close();} catch (Exception e) {}
        }
        return null;
    }
}