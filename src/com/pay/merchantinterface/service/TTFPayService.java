package com.pay.merchantinterface.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import util.JWebConstant;
import util.PayUtil;
import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.ttf.CryptNoRestrict;
import com.third.ttf.EncrytTool;
import com.third.ttf.HttpClientUtil;
import com.third.ttf.SumpaySubmit;
/**
 * 统统付接口服务类
 * @author xk
 */
public class TTFPayService {
	private static final Log log = LogFactory.getLog(TTFPayService.class);
	public static String mer_pfx_key = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("TTF_MERCHANT_PFX");//商户私钥
	public static String server_cert = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("TTF_SERVER_CERT");//平台公钥
	public static String mer_pfx_pwd = PayConstant.PAY_CONFIG.get("TTF_MERCHANT_PFX_PWD");//商户私钥密码
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TTF"); //支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try {
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("version", "1.0");
			sParaTemp.put("service", "sumpay.web.trade.netbankpreorder.apply");
			sParaTemp.put("app_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("timestamp",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("terminal_type", "web");
			sParaTemp.put("mer_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("trade_code", "T0002");//担保交易：T0001 即时交易：T0002  暂只支持即时交易
			sParaTemp.put("cstno", PayConstant.PAY_CONFIG.get("TTF_MERNO"));//付款用户标识 要求唯一
			sParaTemp.put("order_no", payOrder.payordno);
			sParaTemp.put("order_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("order_amt", String.format("%.2f", ((double)payOrder.txamt)/100d));//单位元
			sParaTemp.put("notify_url", PayConstant.PAY_CONFIG.get("TTF_PAY_NOTIFY_URL"));
			sParaTemp.put("return_url", PayConstant.PAY_CONFIG.get("TTF_RETURN_URL"));
			sParaTemp.put("goods_name", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			sParaTemp.put("goods_num", "1");
			sParaTemp.put("goods_type", "1");//1：实物商品；2：虚拟商品
			sParaTemp.put("logistics", "0");//是否物流  1：是；0：否
			sParaTemp.put("channel", "02");//01-网银对公，02-网银对私
			sParaTemp.put("card_type", "0");//0：储蓄卡，1：信用卡，2：储蓄卡或信用卡
			sParaTemp.put("bank_type", payOrder.bankcod);//银行编码
			sParaTemp = SumpaySubmit.buildRequestPara(sParaTemp);
			log.info("统统付网银下单请求数据："+sParaTemp);
			request.setAttribute("ttfReqData", sParaTemp);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 商户接口下单
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_TTF"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		try{
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("version", "1.0");
			sParaTemp.put("service", "sumpay.web.trade.netbankpreorder.apply");
			sParaTemp.put("app_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("timestamp",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("terminal_type", "web");
			sParaTemp.put("mer_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("trade_code", "T0002");//担保交易：T0001 即时交易：T0002  暂只支持即时交易
			sParaTemp.put("cstno", PayConstant.PAY_CONFIG.get("TTF_MERNO"));//付款用户标识 要求唯一
			sParaTemp.put("order_no", payOrder.payordno);
			sParaTemp.put("order_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("order_amt", String.format("%.2f", ((double)payOrder.txamt)/100d));//单位元
			sParaTemp.put("notify_url", PayConstant.PAY_CONFIG.get("TTF_PAY_NOTIFY_URL"));
			sParaTemp.put("return_url", PayConstant.PAY_CONFIG.get("TTF_RETURN_URL"));
			sParaTemp.put("goods_name", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
			sParaTemp.put("goods_num", "1");
			sParaTemp.put("goods_type", "1");//1：实物商品；2：虚拟商品
			sParaTemp.put("logistics", "0");//是否物流  1：是；0：否
			sParaTemp.put("channel", "02");//01-网银对公，02-网银对私
			sParaTemp.put("card_type", "0");//0：储蓄卡，1：信用卡，2：储蓄卡或信用卡
			sParaTemp.put("bank_type", payOrder.bankcod);//银行编码
			sParaTemp = SumpaySubmit.buildRequestPara(sParaTemp);
			log.info("统统付网银下单请求数据："+sParaTemp);
			request.setAttribute("ttfReqData", sParaTemp);
			new PayOrderService().updateOrderForBanks(payOrder);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获 
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("version", "1.0");
			sParaTemp.put("service", "sumpay.trade.order.search");
			sParaTemp.put("format", "JSON");
			sParaTemp.put("app_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("terminal_type", "web");
			sParaTemp.put("mer_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("order_no", payOrder.payordno);
			//数据签名
			String signSource = EncrytTool.getEncrySource(sParaTemp);
			log.info("待签参数--->" + signSource);
			CryptNoRestrict cnr = new CryptNoRestrict();
			String sign = cnr.SignMsg(signSource, mer_pfx_key, mer_pfx_pwd);
			sParaTemp.put("sign", sign);
			sParaTemp.put("sign_type", "CERT");
			log.info("统统付网银查询请求数据："+sParaTemp);
			String responseContent = HttpClientUtil.post(PayConstant.PAY_CONFIG.get("TTF_PAYRECEIVEANDPAY_URL"), sParaTemp);
			log.info("统统付网银查询响应数据："+responseContent);
			/**
			 * {"mer_id":"100000404","order_no":"1504081896929","order_time":"20170830163206",
			 * "resp_code":"000000","resp_msg":"success","serial_no":"T372309",,"status":"00","succ_amt":"0.01","succ_time":"0"
			 * "sign":"cXoW9ObpQm1aYx9/yrt8J517JoOYZIRFdy0T41oNKXsdTc5m9NuDu0/f/IWzrviXpDQgQrWeqN1kM3G5ssjn6xnFPp3tExBbyGJCH1gzrH7+MvCGSHiQG/NY3bmrbQ8nmK+1rYg7WVZF9m1vmaOcrsuQxxxw81yINCZsFrcgFCpjLNr1qpfgZZRGdAWMjRwAvWVHgqSmr3R+ywkTxYYlcur7K546MpWBI4zY3KsMfcUxj4uIUiyTVDOFxPXYJnBbx+q/ybQgP1QGlopE3DI1NKNOdSF5CxO5EgGJJqGDJjB7mI+EvsQU0KQc+g+ofk+8zJ0jGfH+gkvJpwR9Cec/Zw==","sign_type":"CERT"}
			 */
			Map<String, String> responseMap = JSON.parseObject(responseContent, Map.class);
			//验签
			if(cnr.VerifyMsg(responseMap.get("sign"), EncrytTool.getEncrySource(responseMap), server_cert)){
				if("000000".equals(responseMap.get("resp_code"))){
					if("1".equals(responseMap.get("status"))){//支付成功
						 payOrder.ordstatus="01";//支付成功
					     new NotifyInterface().notifyMer(payOrder);
					} else if("0".equals(responseMap.get("status"))){//支付失败
						 payOrder.ordstatus="02";
					     new NotifyInterface().notifyMer(payOrder);
					}
				}
			}else throw new Exception("验签失败");
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 单笔代付
	 * @param payRequest
	 * @throws Exception
	 */
	public void receivePaySingle(PayRequest payRequest) throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
		 	PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
		 	Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("version", "1.0");
			sParaTemp.put("service", "sumpay.trade.agentpay");
			sParaTemp.put("format", "JSON");
			sParaTemp.put("app_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("terminal_type", "web");
			sParaTemp.put("mer_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("pay_cstno", PayConstant.PAY_CONFIG.get("TTF_MERNO"));//扣款商户号/客户号
			sParaTemp.put("order_amt", String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d));
			sParaTemp.put("order_id", rp.id);
			sParaTemp.put("bank_type", cardBin.bankCode);//银行类型
			sParaTemp.put("user_name", rp.accountName);
			sParaTemp.put("card_no", rp.accountNo);
			sParaTemp.put("prop", "0");//扣款标识 0 商户账户扣款  1 个人账户扣款
			sParaTemp.put("notify_url", PayConstant.PAY_CONFIG.get("TTF_PAYRECEIVEANDPAY_NOTIFY_URL"));
			sParaTemp.put("fee_amt", "0");
			sParaTemp.put("card_type", "0");//0储蓄卡 1信用卡
			//数据签名
			String signSource = EncrytTool.getEncrySource(sParaTemp);
			log.info("待签参数--->" + signSource);
			CryptNoRestrict cnr = new CryptNoRestrict();
			String sign = cnr.SignMsg(signSource, mer_pfx_key, mer_pfx_pwd);
			sParaTemp.put("sign", sign);
			sParaTemp.put("sign_type", "CERT");
			log.info("统统付代付请求数据："+sParaTemp);
			String responseContent = HttpClientUtil.post(PayConstant.PAY_CONFIG.get("TTF_PAYRECEIVEANDPAY_URL"), sParaTemp);
			log.info("统统付代付响应数据："+responseContent);
			Map<String, String> responseMap = JSON.parseObject(responseContent, Map.class);
			/**
			 * {"resp_code":"000000","resp_msg":"success","resultModule":"{\"orderAmount\":\"0.01\",\"status\":\"00\",\"tradeId\":\"B122343\"}",
			 * "sign":"b0+jZfZlvzVi2zT6wXGvShQbJMfVaRS8ykTzuIWZh/HzVR/DFa4TQdBoSzv5gWhwzqIvjNMjRtDqBKXLjs44V8lr6OCWw0GDJ0gsW3IQfRVWLUSaPByMy2RTJNebb1kd7NfUv+WQW0z35GPx4yyjIY3GavMFMcZ8nhTNPodmDWu5tY5yjnS0SUuVhslze4SxYW1oqTSC1HGxOilSkvKrp1c48KdMp9jpykxzJkztO66L7RhwOFxx8sWPznTB6ns5Q4PJ+XMY0kS4hB6RxcZjMuB61nlcATdNtjHnfb2T4liveBn9CzmymwoKaELwhe2Roq8g3nuOMdzh4Y2EdnHBeQ==","sign_type":"CERT"}
			 * 订单号：1504084118367  查单用
			 */
			//验签
		 	if(cnr.VerifyMsg(responseMap.get("sign"), EncrytTool.getEncrySource(responseMap), server_cert)){
		 		if("000000".equals(responseMap.get("resp_code"))){
		 			net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(responseMap.get("resultModule"));
		 			if("00".equals(jsonObject.getString("status"))){//成功
		 				rp.status="1";
		 				rp.retCode="000";
						rp.errorMsg = "交易成功";
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
		 			}else if("01".equals(jsonObject.getString("status"))){//处理中
		 				rp.status="0";
		 				rp.retCode="074";
						rp.errorMsg = "处理中";
			        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			        	TTFDFQueryThread query = new TTFDFQueryThread(payRequest);
			        	query.start();
		 			}else if("03".equals(jsonObject.getString("status"))){//失败
		 				rp.status="2";
		 				rp.retCode="-1";
						rp.errorMsg = jsonObject.getString("remark");
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
		 			}
				}else{
					rp.status="2";
					rp.retCode="-1";
					rp.errorMsg = "操作失败";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				}
		 	}else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			throw e;
		}
	}
	/**
	 * 单笔代付查询
	 * @param request
	 * @param rp
	 * @throws Exception
	 */
	public void receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp) throws Exception{
		try {
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("version", "1.0");
			sParaTemp.put("service", "sumpay.trade.queryobopage");
			sParaTemp.put("format", "JSON");
			sParaTemp.put("app_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("terminal_type", "web");
			sParaTemp.put("mer_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("cst_no", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("obo_type", "00");
			sParaTemp.put("order_id", rp.id);
			sParaTemp.put("page", "1");
			sParaTemp.put("rows", "1");
			sParaTemp.put("is_inner", "01");
			//数据签名
			String signSource = EncrytTool.getEncrySource(sParaTemp);
			log.info("待签参数--->" + signSource);
			CryptNoRestrict cnr = new CryptNoRestrict();
			String sign = cnr.SignMsg(signSource, mer_pfx_key, mer_pfx_pwd);
			sParaTemp.put("sign", sign);
			sParaTemp.put("sign_type", "CERT");
			log.info("统统付代付查单请求数据："+sParaTemp);
			String responseContent = HttpClientUtil.post(PayConstant.PAY_CONFIG.get("TTF_PAYRECEIVEANDPAY_URL"), sParaTemp);
			log.info("统统付代付查单响应数据："+responseContent);
			Map<String, String> responseMap = JSON.parseObject(responseContent, Map.class);
			/**
			 * {"listObO":"[{\"bankCode\":\"cGYIu698GLMXN3pF4Djyl1W/NTNDwK86W9CoWxodW6A=\\n\",\"createTime\":20170830173347,\"cstinfo\":\"张志国\",\"cstno\":\"200100000723\",\"feeAmount\":0,\"id\":\"122535\",\"inOut\":\"01\",\"oboAmount\":0.01,\"oboExplain\":\"THD\",\"oboStatus\":\"00\",\"oboType\":\"00\",\"oboid\":\"B122344\",\"orderId\":\"1504085622216\",\"payNo\":\"t10295980\",\"sucessTime\":20170830173347}]",
			 * "resp_code":"000000","resp_msg":"success","rowCount":"1",
			 * "sign":"YXWchrWYo5I5VzYnnGGoJFpcyK0+ENpft3ndGNQ9/WGIJIU2/iDCqwH0KzracPCfIKCzqYzryU06Z3SVu1xfEDZaKhfrNVlmzdtG+86OcOP+6GXlsgo8vJ2WEBI4S7B+HltqSVEnXyPFyk5e+PEhITH9iX94jQHEv6hywuAMT/XOrTuv84LFHqRClUBSvMZZ04bbreqrKlFdCVaL536yWvW2Vx/FV8VAScQ6RiLfrDtvlTPxC/j1RGxNB6I7HNr/qbPcULCpAdxeWoyZO6yFi0ECPBJ/Og+eH3SNRqoyutrq3pAqylVo5Fw+/7fve8voJFZaV16oE/EvwUJxlmhqUg==","sign_type":"CERT"}
			 */
			if(cnr.VerifyMsg(responseMap.get("sign"), EncrytTool.getEncrySource(responseMap), server_cert)){
				if("000000".equals(responseMap.get("resp_code"))){
					net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(responseMap.get("listObO")); 
					net.sf.json.JSONObject jsonObject = jsonArray.getJSONObject(0);
					if("00".equals(jsonObject.getString("oboStatus"))){//交易成功。
						request.setRespCode("000");
						request.receiveAndPaySingle.setRespCode("000");
						request.receiveAndPaySingle.retCode="000";
						request.receivePayRes = "0";
						request.respDesc="交易成功";
						rp.errorMsg = "交易成功";
					}else if("03".equals(jsonObject.getString("oboStatus"))){//交易失败。
						request.setRespCode("-1");
						request.receiveAndPaySingle.setRespCode("-1");
						request.receivePayRes = "-1";
						request.respDesc="交易失败";
						rp.errorMsg = request.respDesc;
					}else if("01".equals(jsonObject.getString("oboStatus"))){//处理中。
						request.setRespCode("0");
						request.respDesc="处理中";
						rp.errorMsg = request.respDesc;
					}
				} else {
					request.setRespCode("-1");
					request.receiveAndPaySingle.setRespCode("-1");
					request.receivePayRes = "-1";
					request.respDesc="交易失败";
					rp.errorMsg = request.respDesc;
				}
			} else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
class TTFDFQueryThread extends Thread{
	private static final Log log = LogFactory.getLog(TTFDFQueryThread.class);
	private  PayRequest payRequest = new PayRequest();
	public TTFDFQueryThread(){};
	public TTFDFQueryThread(PayRequest payRequest){
		this.payRequest = payRequest;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("统统付代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query()throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("version", "1.0");
			sParaTemp.put("service", "sumpay.trade.queryobopage");
			sParaTemp.put("format", "JSON");
			sParaTemp.put("app_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("timestamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			sParaTemp.put("terminal_type", "web");
			sParaTemp.put("mer_id", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("cst_no", PayConstant.PAY_CONFIG.get("TTF_MERNO"));
			sParaTemp.put("obo_type", "00");
			sParaTemp.put("order_id", rp.id);
			sParaTemp.put("page", "1");
			sParaTemp.put("rows", "1");
			sParaTemp.put("is_inner", "01");
			//数据签名
			String signSource = EncrytTool.getEncrySource(sParaTemp);
			System.out.println("验签参数--->" + signSource);
			CryptNoRestrict cnr = new CryptNoRestrict();
			String sign = cnr.SignMsg(signSource, TTFPayService.mer_pfx_key, TTFPayService.mer_pfx_pwd);
			sParaTemp.put("sign", sign);
			sParaTemp.put("sign_type", "CERT");
			log.info("统统付代付查单请求数据："+sParaTemp);
			String responseContent = HttpClientUtil.post(PayConstant.PAY_CONFIG.get("TTF_PAYRECEIVEANDPAY_URL"), sParaTemp);
			log.info("统统付代付查单响应数据："+responseContent);
			Map<String, String> responseMap = JSON.parseObject(responseContent, Map.class);
			/**
			 * {"listObO":"[{\"bankCode\":\"cGYIu698GLMXN3pF4Djyl1W/NTNDwK86W9CoWxodW6A=\\n\",\"createTime\":20170830173347,\"cstinfo\":\"张志国\",\"cstno\":\"200100000723\",\"feeAmount\":0,\"id\":\"122535\",\"inOut\":\"01\",\"oboAmount\":0.01,\"oboExplain\":\"THD\",\"oboStatus\":\"00\",\"oboType\":\"00\",\"oboid\":\"B122344\",\"orderId\":\"1504085622216\",\"payNo\":\"t10295980\",\"sucessTime\":20170830173347}]",
			 * "resp_code":"000000","resp_msg":"success","rowCount":"1",
			 * "sign":"YXWchrWYo5I5VzYnnGGoJFpcyK0+ENpft3ndGNQ9/WGIJIU2/iDCqwH0KzracPCfIKCzqYzryU06Z3SVu1xfEDZaKhfrNVlmzdtG+86OcOP+6GXlsgo8vJ2WEBI4S7B+HltqSVEnXyPFyk5e+PEhITH9iX94jQHEv6hywuAMT/XOrTuv84LFHqRClUBSvMZZ04bbreqrKlFdCVaL536yWvW2Vx/FV8VAScQ6RiLfrDtvlTPxC/j1RGxNB6I7HNr/qbPcULCpAdxeWoyZO6yFi0ECPBJ/Og+eH3SNRqoyutrq3pAqylVo5Fw+/7fve8voJFZaV16oE/EvwUJxlmhqUg==","sign_type":"CERT"}
			 */
			//验签
			if(cnr.VerifyMsg(responseMap.get("sign"), EncrytTool.getEncrySource(responseMap), TTFPayService.server_cert)){
				if("000000".equals(responseMap.get("resp_code"))){
					net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(responseMap.get("listObO")); 
					net.sf.json.JSONObject jsonObject = jsonArray.getJSONObject(0);
					if("00".equals(jsonObject.getString("oboStatus"))){//成功
						rp.status = "1";
						rp.respCode = "000";
						payRequest.respCode = "000";
						rp.errorMsg = "交易成功";
						rp.retCode="000";
						payRequest.respDesc = rp.errorMsg;
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
						list.add(rp);
						new PayInterfaceOtherService().receivePayNotify(payRequest,list);
						return true;
					}else if("03".equals(jsonObject.getString("oboStatus"))){//失败
						rp.status = "2";
						rp.respCode = "-1";
						rp.errorMsg = "交易失败";
						rp.retCode="-1";
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						List<PayReceiveAndPay> list2 = new ArrayList<PayReceiveAndPay>();
						list2.add(rp);
						new PayInterfaceOtherService().receivePayNotify(payRequest,list2);
						return true;
					}else if("01".equals(jsonObject.getString("oboStatus"))){//处理中
						return false;
					}
				}else {
					rp.status="2";
					rp.respCode="-1";
					rp.retCode="-1";
					rp.errorMsg="交易失败";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					return true;
				}
			} else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}