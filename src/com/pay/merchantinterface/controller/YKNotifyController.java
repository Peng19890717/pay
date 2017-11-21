package com.pay.merchantinterface.controller;
import org.springframework.stereotype.Controller;
/**
 * 雅酷支付通知接口
 * @author Administrator
 *
 */
@Controller
public class YKNotifyController {
//	private static final Log log = LogFactory.getLog(YKNotifyController.class);
//    /**
//     * 通知地址(
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping("YKNotify")
//    public String YKNotify(HttpServletRequest request,HttpServletResponse response) {
//    	PrintWriter writer = null;
//        try {
//        	writer = response.getWriter();
//    		request.setCharacterEncoding("UTF-8");
//    		response.setCharacterEncoding("UTF-8");
//    		response.setHeader("Content-type","text/html;charset=UTF-8");
//    		String tranNo = request.getParameter("tranNo");
//    		String tranTime = request.getParameter("tranTime");
//    		String sysMerchNo = request.getParameter("sysMerchNo");
//    		String outOrderNo = request.getParameter("outOrderNo");
//    		String orderAmt = request.getParameter("orderAmt");
//    		String tranAttr = request.getParameter("tranAttr");
//    		String tranAmt = request.getParameter("tranAmt");
//    		String tranResult = request.getParameter("tranResult");
//    		String signType = request.getParameter("signType");
//    		String sign = request.getParameter("sign");
//    		String inputCharset = request.getParameter("inputCharset");
//    		String orderTime = request.getParameter("orderTime");
//    		String noticeUrl = request.getParameter("noticeUrl");
//    		String sysId = request.getParameter("sysId");
//    		String tranCode = request.getParameter("tranCode");
//    		String tranFeeAmt = request.getParameter("tranFeeAmt");
//
//    		Map<String, String> param = new LinkedHashMap<String, String>();
//    		param.put("tranNo", tranNo);
//    		param.put("tranTime", tranTime);
//    		param.put("sysMerchNo", sysMerchNo);
//    		param.put("outOrderNo", outOrderNo);
//    		param.put("orderAmt", orderAmt);
//    		param.put("tranAttr", tranAttr);
//    		param.put("tranAmt", tranAmt);
//    		param.put("tranResult", tranResult);
//    		param.put("inputCharset", inputCharset);
//    		param.put("orderTime", orderTime);
//    		param.put("noticeUrl", noticeUrl);
//    		param.put("sysId", sysId);
//    		param.put("tranCode", tranCode);
//    		param.put("tranFeeAmt", tranFeeAmt);
//    		log.info("雅酷通知=================\n"+param);
//    		// 数据的签名字符串
//    		String sign_resp = SignUtils.getSign(param, PayConstant.PAY_CONFIG.get("PAY_YK_KEY"), "UTF-8");
//    		//解析密文数据
//    		if(sign!=null&&sign.equals(sign_resp) ){
//    			if(!"SUCCESS".equals(tranResult))throw new Exception("支付未完成（"+outOrderNo+"）"+tranResult);
//    			PayOrder tmpPayOrder = new PayOrder();
//            	tmpPayOrder.payordno = outOrderNo;
//            	tmpPayOrder.actdat = new Date();
//            	tmpPayOrder.ordstatus="01";
//            	tmpPayOrder.bankjrnno = tranNo;
//            	new NotifyInterface().notifyMer(tmpPayOrder);
//            	writer.write("SUCCESS");
//    		} else throw new Exception("验签失败");
//        } catch (Exception e) {
//        	log.info(PayUtil.exceptionToString(e));
//        	writer.write("FAIL");
//        } finally {
//        	if(writer!=null)try {writer.close();} catch (Exception e) {}
//        }
//        return null;
//    }
//    
//    /**
//     * 通知地址(新通道)
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping("NEWYKNotify")
//    public String NEWYKNotify(HttpServletRequest request,HttpServletResponse response) {
//    	PrintWriter writer = null;
//        try {
//        	writer = response.getWriter();
//    		request.setCharacterEncoding("UTF-8");
//    		response.setCharacterEncoding("UTF-8");
//    		response.setHeader("Content-type","text/html;charset=UTF-8");
//    		String parnterId = request.getParameter("parnterId");
//    		String termNo = request.getParameter("termNo");
//    		String merNo = request.getParameter("merNo");
//    		String signType = request.getParameter("signType");
//    		String charset = request.getParameter("charset");
//    		String timestamp = request.getParameter("timestamp");
//    		String version = request.getParameter("version");
//    		String sign = request.getParameter("sign");
//    		String orderNo = request.getParameter("orderNo");
//    		String outTradeNo = request.getParameter("outTradeNo");
//    		String orderStatus = request.getParameter("orderStatus");
//    		String receiptStatus = request.getParameter("receiptStatus");
//    		String receiptMsg = request.getParameter("receiptMsg");
//    		String totalFee = request.getParameter("totalFee");
//    		//封装签名参数。
//    		Map<String,Object> param = new HashMap<String, Object>();
//    		param.put("parnterId", parnterId);
//    		param.put("termNo", termNo);
//    		param.put("merNo", merNo);
//    		param.put("signType", signType);
//    		param.put("charset", charset);
//    		param.put("timestamp", timestamp);
//    		param.put("version", version);
//    		param.put("orderNo", orderNo);
//    		param.put("outTradeNo", outTradeNo);
//    		param.put("orderStatus", orderStatus);
//    		param.put("receiptStatus", receiptStatus);
//    		param.put("receiptMsg", receiptMsg);
//    		param.put("totalFee", totalFee);
//    		log.info("雅酷通知=================\n"+param);
//    		// 数据的签名字符串
//    		YKPayService service = new YKPayService();
//    		String mysign = service.getMD5Sign(param, PayConstant.PAY_CONFIG.get("YK_KEY"));
//    		//解析密文数据
//    		if(sign!=null&&sign.equals(mysign) ){
//    			if("01".equals(orderStatus)){
//    				PayOrder tmpPayOrder = new PayOrder();
//                	tmpPayOrder.payordno = outTradeNo;
//                	tmpPayOrder.actdat = new Date();
//                	tmpPayOrder.ordstatus="01";
//                	new NotifyInterface().notifyMer(tmpPayOrder);
//    			}
//            	
//    		} else throw new Exception("验签失败");
//        } catch (Exception e) {
//        	log.info(PayUtil.exceptionToString(e));
//        	writer.write("FAIL");
//        } finally {
//        	if(writer!=null)try {writer.close();} catch (Exception e) {}
//        }
//        return null;
//    }
}