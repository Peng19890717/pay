package com.pay.merchantinterface.controller;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.PayUtil;

import com.PayConstant;
import com.pay.merchantinterface.service.NotifyInterface;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.third.yilian.service.encrypt.MD5;

@Controller
public class YLNotifyController {
	private static final Log log = LogFactory.getLog(YLNotifyController.class);
    @RequestMapping("YLNotify")
    public String ZXNotify(HttpServletRequest request,HttpServletResponse response) {
    	log.info("易连代扣结果通知开始....................");
        try {
        	request.setCharacterEncoding("utf-8");
        	String ORDER_NO = request.getParameter("ORDER_NO");
    		String MERCHANT_NO = request.getParameter("MERCHANT_NO");
    		String YL_BATCH_NO = request.getParameter("YL_BATCH_NO");
    		String SN = request.getParameter("SN");
    		String AMOUNT = request.getParameter("AMOUNT");
    		String CURRENCY = request.getParameter("CURRENCY");
    		String ACCOUNT_NO = request.getParameter("ACCOUNT_NO");
    		String MOBILE_NO = request.getParameter("MOBILE_NO");
    		String RESP_CODE = request.getParameter("RESP_CODE");
    		String RESP_REMARK = request.getParameter("RESP_REMARK");
    		RESP_REMARK = new String(RESP_REMARK.getBytes("gbk"),"utf-8");
    		String SETT_AMOUNT = request.getParameter("SETT_AMOUNT");
    		String SETT_CURRENCY = request.getParameter("SETT_CURRENCY");
    		String EXCHANGE_RATE = request.getParameter("EXCHANGE_RATE");
    		String NOTIFY_TIME = request.getParameter("NOTIFY_TIME");
    		String MER_ORDER_NO = request.getParameter("MER_ORDER_NO");
    		String TRANS_DESC = request.getParameter("TRANS_DESC");
    		if(TRANS_DESC!=null&&!TRANS_DESC.equals("")){
    			TRANS_DESC = new String(TRANS_DESC.getBytes("ISO-8859-1"),"utf-8") ;//ISO-8859-1
    		}
    		String MAC = request.getParameter("MAC");
    		log.info("MERCHANT_NO="+MERCHANT_NO);
    		log.info("ORDER_NO="+ORDER_NO);
    		log.info("YL_BATCH_NO="+YL_BATCH_NO);
    		log.info("SN="+SN);
    		log.info("AMOUNT="+AMOUNT);
    		log.info("CURRENCY="+CURRENCY);
    		log.info("ACCOUNT_NO="+ACCOUNT_NO);
    		log.info("MOBILE_NO="+MOBILE_NO);
    		log.info("RESP_CODE="+RESP_CODE);
    		log.info("RESP_REMARK="+RESP_REMARK);
    		log.info("SETT_AMOUNT="+SETT_AMOUNT);
    		log.info("SETT_CURRENCY="+SETT_CURRENCY);
    		log.info("EXCHANGE_RATE="+EXCHANGE_RATE);
    		log.info("NOTIFY_TIME="+NOTIFY_TIME);
    		log.info("MER_ORDER_NO="+MER_ORDER_NO);
    		log.info("TRANS_DESC="+TRANS_DESC);
    		
    		/*====MAC 顺序一致=====*/
    		//商户号 文件名 批次号 流水号 金额 币种 账号 手机号 返回码 结算金额 商户流水号 商户密钥
    		StringBuffer macSb = new StringBuffer(MERCHANT_NO);
    		if (!"".equals(ORDER_NO) && ORDER_NO != null )
    			macSb.append(" " + ORDER_NO);
    		if (!"".equals(YL_BATCH_NO) && YL_BATCH_NO != null)
    			macSb.append(" " + YL_BATCH_NO);
    		if (!"".equals(SN) && SN != null)
    			macSb.append(" " + SN);
    		if (!"".equals(AMOUNT) && AMOUNT != null)
    			macSb.append(" " + formatAmount(Double.parseDouble(AMOUNT))); // 固定两位小数
    		if (!"".equals(CURRENCY) && CURRENCY != null)
    			macSb.append(" " + CURRENCY);
    		if (!"".equals(ACCOUNT_NO) && ACCOUNT_NO != null)
    			macSb.append(" " + ACCOUNT_NO);
    		if (!"".equals(MOBILE_NO) && MOBILE_NO != null)
    			macSb.append(" " + MOBILE_NO);
    		if (!"".equals(RESP_CODE) && RESP_CODE != null)
    			macSb.append(" " + RESP_CODE);
    		if (!"".equals(SETT_AMOUNT) && SETT_AMOUNT != null)
    			macSb.append(" " + formatAmount(Double.parseDouble(SETT_AMOUNT))); // 固定两位小数
    		if (!"".equals(MER_ORDER_NO) && MER_ORDER_NO != null)
    			macSb.append(" " + MER_ORDER_NO);
    		String mer_key = PayConstant.PAY_CONFIG.get("YL_MERCHANT_NO_KEY");
    		if (!"".equals(mer_key) && mer_key != null)
    			macSb.append(" " + mer_key);
    		String mac = macSb.toString().toUpperCase();
    		log.info("MAC源码:" + mac);
    		MD5 md5 = new MD5();
    		mac = md5.getMD5ofStr(mac);
    		//验证签名。
    		if(mac.equals(MAC)){
    			PayReceiveAndPayDAO dao = new PayReceiveAndPayDAO();
    			PayReceiveAndPay rp = new PayReceiveAndPay();
    			//只处理支付成功的状态。
    			if("0000".equals(RESP_CODE)){
    				rp.sn=SN;
    				rp.status="1";
					rp.retCode="000";
					rp.errorMsg="交易成功";
					dao.updatePayReceiveAndPaystatus(rp);
					//平台包装快捷，通知
					if("1".equals(rp.bussFromType)){
    					PayOrder tmpPayOrder = new PayOrder();
                    	tmpPayOrder.payordno = rp.id;
                    	tmpPayOrder.actdat = new Date();
                    	tmpPayOrder.ordstatus="01";
                    	tmpPayOrder.bankjrnno = "";
                    	new NotifyInterface().notifyMer(tmpPayOrder);
					}
    			} else {
    				rp.sn=SN;
    				rp.status="2";
					rp.retCode="-1";
					rp.errorMsg="交易失败";
					dao.updatePayReceiveAndPaystatus(rp);
    			}
    		} else throw new Exception("验签失败");
        } catch (Exception e) {
        	log.info(PayUtil.exceptionToString(e));
        } 
        return null;
    }
    public static String formatAmount(double amt) {
        DecimalFormat numFormat=new DecimalFormat ("0.00");
        return numFormat.format(amt);
    }
}