package com.pay.merchantinterface.controller;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.PayUtil;

import com.PayConstant;
import com.pay.merchantinterface.service.FYMD5;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
/**
 * 连连网关支付通知接口
 * @author Administrator
 *
 */
@Controller
public class LLNotifyController {
	private static final Log log = LogFactory.getLog(LLNotifyController.class);
    /**
     * 通知地址(直连网银)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("LLNotify")
    public String LLNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("连连通知开始==========================");
    	OutputStream os = null;
        try {
        	response.setContentType("text/html;charset=UTF-8");
        	//接收参数
        	request.setCharacterEncoding("utf-8");
        	String mchnt_cd = request.getParameter("mchnt_cd")==null?"":request.getParameter("mchnt_cd");//商户编号
        	String order_id = request.getParameter("order_id")==null?"":request.getParameter("order_id");//订单编号。
        	String order_amt = request.getParameter("order_amt")==null?"":request.getParameter("order_amt");//订单金额。
        	String order_date = request.getParameter("order_date")==null?"":request.getParameter("order_date");//订单日期。
        	String order_st = request.getParameter("order_st")==null?"":request.getParameter("order_st");//订单状态 ；  11-为已支付或者支付成功。
        	String order_pay_code = request.getParameter("order_pay_code")==null?"":request.getParameter("order_pay_code");//错误代码。0000表示成功，其他失败。
        	String order_pay_error = request.getParameter("order_pay_error")==null?"":request.getParameter("order_pay_error");//错误描述。
        	String fy_ssn = request.getParameter("fy_ssn")==null?"":request.getParameter("fy_ssn");//富友流水。
        	String resv1 = request.getParameter("resv1")==null?"":request.getParameter("resv1");//保留字段。。
        	String md5 = request.getParameter("md5")==null?"":request.getParameter("md5");//md5摘要。
        	String mchnt_key= PayConstant.PAY_CONFIG.get("fy_mchnt_key");
        	//md5摘要。
        	String signDataStr = mchnt_cd + "|" + 
                      order_id + "|" + order_date + "|" + order_amt+ "|" +order_st+ "|" +order_pay_code+ "|" +
                      order_pay_error+ "|" + resv1+ "|" +
                      fy_ssn+ "|" + mchnt_key;
        	String md5_2 = FYMD5.MD5Encode(signDataStr);
    		if(md5.equals(md5_2)){
    			if(!order_st.equals("11"))throw new Exception("支付未完成（"+order_id+"）"+order_st);
    			PayOrder tmpPayOrder = new PayOrder();
            	tmpPayOrder.payordno = order_id;
            	tmpPayOrder.actdat = new Date();
            	tmpPayOrder.ordstatus="01";
            	tmpPayOrder.bankjrnno = "";
            	new NotifyInterface().notifyMer(tmpPayOrder);
            	//成功后响应
            	//response.setStatus(200);
    		 } else throw new Exception("验签失败");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        	try {
        		os = response.getOutputStream();
        		if(os!=null)os.write((e.getMessage()==null?"":e.getMessage()).getBytes("utf-8"));
        	} catch (IOException e1) {}
        } finally {
        	if(os!=null)try {os.close();} catch (Exception e) {}
        }
        return null;
    }
}