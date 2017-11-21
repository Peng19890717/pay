package com.pay.merchantinterface.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.PayUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.yltf.HttpUtil;
import com.third.yltf.SignUtil;

/**
 * 亿联通付接口服务类
 * @author xk
 *
 */
public class YLTFPayService {
	private static String charset = "utf-8";
	private static final Log log = LogFactory.getLog(YLTFPayService.class);
	private static String key = PayConstant.PAY_CONFIG.get("YLTF_KEY");
	public static Map<String,String> BANK_MAP_JIEJI = new HashMap <String,String>();//借记卡银行码映射
	static{
		BANK_MAP_JIEJI.put("CCB", "105");//中国建设银行
		BANK_MAP_JIEJI.put("ICBC", "102");//中国工商银行
		BANK_MAP_JIEJI.put("ABC", "103");//中国农业银行
		BANK_MAP_JIEJI.put("BOC", "104");//中国银行
		BANK_MAP_JIEJI.put("CMB", "310");//招商银行
		BANK_MAP_JIEJI.put("GDB", "320");//广发银行
		BANK_MAP_JIEJI.put("CEB", "330");//中国光大银行
		BANK_MAP_JIEJI.put("PAB", "340");//平安银行
		BANK_MAP_JIEJI.put("BOCOM", "350");//交通银行
		BANK_MAP_JIEJI.put("PSBC", "403");//中国邮政储蓄银行
		BANK_MAP_JIEJI.put("CMBC", "360");//中国民生银行
		BANK_MAP_JIEJI.put("CNCB", "106");//中信银行
		BANK_MAP_JIEJI.put("CIB", "309");//兴业银行
		BANK_MAP_JIEJI.put("BCCB", "370");//北京银行
		BANK_MAP_JIEJI.put("HXB", "380");//华夏银行
		BANK_MAP_JIEJI.put("NJBC", "390");//南京银行
		BANK_MAP_JIEJI.put("BEAI", "400");//东亚银行
		BANK_MAP_JIEJI.put("SPDB", "410");//浦发银行
		BANK_MAP_JIEJI.put("BOS", "420");//上海银行
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YLTF"); //支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try {
			Map<String, String> paras = new HashMap<String, String>();
			paras.put("mid", PayConstant.PAY_CONFIG.get("YLTF_MERCHANTID_D0"));
			paras.put("orderNo", payOrder.payordno);
			paras.put("subject", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));//商品名称
			paras.put("amount", String.format("%.2f", ((double)payOrder.txamt)/100d));//金额元
			paras.put("bankCode", BANK_MAP_JIEJI.get(payOrder.bankcod));
			paras.put("notifyUrl", PayConstant.PAY_CONFIG.get("YLTF_NOTIFYURL"));
			paras.put("currencyType", "CNY");
			paras.put("body",PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));//商品描述
			paras.put("cardType", "01");//卡类型，默认01为借记卡，02为贷记卡
			paras.put("noise", String.valueOf(System.currentTimeMillis()));//随机字符串，不长于32位
			String sign = SignUtil.generateMD5(paras, PayConstant.PAY_CONFIG.get("YLTF_KEY_D0"));
			paras.put("sign",sign );
			log.info("亿联通付网银下单请求数据："+paras);
			request.setAttribute("yltfReqData", paras);
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
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YLTF"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		try{
			Map<String, String> paras = new HashMap<String, String>();
			paras.put("mid", PayConstant.PAY_CONFIG.get("YLTF_MERCHANTID_D0"));
			paras.put("orderNo", payOrder.payordno);
			paras.put("subject", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));//商品名称
			paras.put("amount", String.format("%.2f", ((double)payOrder.txamt)/100d));//金额元
			paras.put("bankCode", BANK_MAP_JIEJI.get(payOrder.bankcod));
			paras.put("notifyUrl", PayConstant.PAY_CONFIG.get("YLTF_NOTIFYURL"));
			paras.put("currencyType", "CNY");
			paras.put("body",PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));//商品描述
			paras.put("cardType", "01");//卡类型，默认01为借记卡，02为贷记卡
			paras.put("noise", String.valueOf(System.currentTimeMillis()));//随机字符串，不长于32位
			String sign = SignUtil.generateMD5(paras, PayConstant.PAY_CONFIG.get("YLTF_KEY_D0"));
			paras.put("sign",sign );
			log.info("亿联通付网银下单请求数据："+paras);
			request.setAttribute("yltfReqData", paras);
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
			Map<String, String> paras = new HashMap<String, String>();
			paras.put("mid", PayConstant.PAY_CONFIG.get("YLTF_MERCHANTID_D0"));
			paras.put("orderNo", payOrder.payordno);
			paras.put("noise", String.valueOf(System.currentTimeMillis()));//随机字符串，不长于32位
			String sign = SignUtil.generateMD5(paras, PayConstant.PAY_CONFIG.get("YLTF_KEY_D0"));
			paras.put("sign",sign );
			log.info("亿联通付网银查询请求数据："+paras);
			String result = HttpUtil.post(PayConstant.PAY_CONFIG.get("YLTF_ONLINEPAY_QUERY"), paras, "utf-8");
			log.info("亿联通付网银查询响应数据："+result);
			/**
			 * {"amount":0.80,"code":"SUCCESS","flowNo":"ZF20170717153158102122","mid":"822017071724115","msg":"ok","noise":"1JvBcScN5X04FQz0V59KW0u1maas6A1u","orderNo":"1500276725585",
			 * "orderTime":"20170717153158","resultCode":"SUCCESS","sign":"18DD4B5349B50DF410F533C054A384F7","status":"0","type":"netpay"}
			 */
			Map<String, Object> resMap = JSON.parseObject(result);
			Map<String, String> respMap = new HashMap<String, String>();
			Iterator<String> iterator = resMap.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = String.valueOf(resMap.get(key));
				if(!"".equals(value) && value != null && !"sign".equals(key)){ 
					respMap.put(key, value);
				}
			}
			String resSign = SignUtil.generateMD5(respMap, PayConstant.PAY_CONFIG.get("YLTF_KEY_D0"));
			if(resSign.equals(resMap.get("sign"))){
				JSONObject resJson = JSON.parseObject(result);
				if("SUCCESS".equals(resJson.getString("code")) && "SUCCESS".equals(resJson.getString("resultCode"))) {
					if("1".equals(resJson.getString("status"))){
						 payOrder.ordstatus="01";//支付成功
					     new NotifyInterface().notifyMer(payOrder);//支付成功
					} else if("2".equals(resJson.getString("status"))){
						 payOrder.ordstatus="02";//支付失败
					     new NotifyInterface().notifyMer(payOrder);
					}
				}
			}else throw new Exception("亿联通付验签失败");
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
		 	Map<String, String> paras = new HashMap<String, String>();
			paras.put("mid", PayConstant.PAY_CONFIG.get("YLTF_MERCHANTID"));
			paras.put("orderNo", rp.channelTranNo);
			paras.put("amount", String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d));//单位元
			paras.put("receiveName", rp.accountName);//收款人姓名
			paras.put("openProvince", "北京");//开户省rp.province
			paras.put("openCity", "北京");//开户市rp.city
			paras.put("bankName", cardBin.bankName);//收款银行
			paras.put("bankBranchName", "回龙观支行");//支行名称
			paras.put("cardNo", rp.accountNo);//卡号
			paras.put("bankLinked", "");//联行号
			paras.put("type", "02");//代付类型  01 普通(T+N) 02 额度(D0)
			paras.put("cardAccountType", "01");//01 个人 02 企业  目前只支持01个人
			paras.put("noise", String.valueOf(System.currentTimeMillis()));//随机字符串，不长于32位
			String sign = SignUtil.generateMD5(paras, key);
			paras.put("sign",sign );
			log.info("亿联通付单笔代付请求数据："+paras);
			String result = HttpUtil.post(PayConstant.PAY_CONFIG.get("YLTF_RECEIVEPAYSINGLE"), paras, "utf-8");
			log.info("亿联通付单笔代付响应数据："+result);
			/**
			 * {"code":"SUCCESS","mid":"822017071724115","msg":"系统错误","noise":"zM1CLrJjLFu7fKMrYgFPzvvTMwYluqYx",
			 * "resultCode":"ERROR","sign":"DDB4133F9770A2D0A21FBF532D79A75C"}
			 */
			Map<String, Object> resMap = JSON.parseObject(result);
			Map<String, String> respMap = new HashMap<String, String>();
			Iterator<String> iterator = resMap.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = String.valueOf(resMap.get(key));
				if(!"".equals(value) && value != null && !"sign".equals(key)){
					respMap.put(key, value);
				}
			}
			String resSign = SignUtil.generateMD5(respMap, PayConstant.PAY_CONFIG.get("YLTF_KEY"));
			if(resSign.equals(resMap.get("sign"))){
				JSONObject resJson = (JSONObject) JSONObject.parse(result);
				if("SUCCESS".equals(resJson.getString("code")) && "SUCCESS".equals(resJson.getString("resultCode"))){
					if("1".equals(resJson.getString("transState"))){
						rp.status="0";
						rp.errorMsg = "操作成功";
			        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			        	YLTFDFQueryThread query = new YLTFDFQueryThread(payRequest);
			        	query.start();
					}
				}else{
					rp.status="2";
					rp.errorMsg = "操作失败";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				}
			}else throw new Exception("亿联通付验签失败");
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
			Map<String, String> paras = new HashMap<String, String>();
			paras.put("mid", PayConstant.PAY_CONFIG.get("YLTF_MERCHANTID"));
			paras.put("orderNo", rp.channelTranNo);
			paras.put("noise", String.valueOf(System.currentTimeMillis()));//随机字符串，不长于32位
			String sign = SignUtil.generateMD5(paras, key);
			paras.put("sign",sign );
			log.info("亿联通付单笔代付查单请求数据："+paras);
			String result = HttpUtil.post(PayConstant.PAY_CONFIG.get("YLTF_RECEIVEPAYSINGLE_QUERY"), paras, charset);
			log.info("亿联通付单笔代付查单响应数据："+result);
			/**
			 * {"code":"SUCCESS","mid":"822017071724115","msg":"订单不存在","noise":"nGmKZhePIndeqteBV34We7676An4cW4U",
			 * "resultCode":"NO_ORDER","sign":"DA105A9227417CFA1562B823C7604CAF"}
			 */
			Map<String, Object> resMap = JSON.parseObject(result);
			Map<String, String> respMap = new HashMap<String, String>();
			Iterator<String> iterator = resMap.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = String.valueOf(resMap.get(key));
				if(!"".equals(value) && value != null && !"sign".equals(key)){
					respMap.put(key, value);
				}
			}
			String resSign = SignUtil.generateMD5(respMap, PayConstant.PAY_CONFIG.get("YLTF_KEY"));
			if(resSign.equals(resMap.get("sign"))){
				JSONObject resJson = (JSONObject) JSONObject.parse(result);
				if("SUCCESS".equals(resJson.getString("code")) && "SUCCESS".equals(resJson.getString("resultCode"))){
					if("3".equals(resJson.getString("transState"))){
						request.setRespCode("000");
						request.receiveAndPaySingle.setRespCode("000");
						request.receiveAndPaySingle.retCode="000";
						request.receivePayRes = "0";
						request.respDesc="交易成功";
						rp.errorMsg = "交易成功";
					} else if("4".equals(resJson.getString("transState"))){
						request.setRespCode("-1");
						request.receiveAndPaySingle.setRespCode("-1");
						request.receivePayRes = "-1";
						request.respDesc="交易失败";
						rp.errorMsg = request.respDesc;
					}
				} else {
					request.setRespCode("-1");
					request.receiveAndPaySingle.setRespCode("-1");
					request.receivePayRes = "-1";
					request.respDesc="交易失败";
					rp.errorMsg = request.respDesc;
				}
			} else throw new Exception("亿联通付验签失败");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
class YLTFDFQueryThread extends Thread{
	private static final Log log = LogFactory.getLog(YLTFDFQueryThread.class);
	private  PayRequest payRequest= new PayRequest();
	public YLTFDFQueryThread(){};
	public YLTFDFQueryThread(PayRequest payRequest){
		this.payRequest=payRequest;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("亿联通付代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query()throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
			Map<String, String> paras = new HashMap<String, String>();
			paras.put("mid", PayConstant.PAY_CONFIG.get("YLTF_MERCHANTID"));
			paras.put("orderNo", rp.channelTranNo);
			paras.put("noise", String.valueOf(System.currentTimeMillis()));//随机字符串，不长于32位
			String sign = SignUtil.generateMD5(paras, PayConstant.PAY_CONFIG.get("YLTF_KEY"));
			paras.put("sign",sign );
			log.info("亿联通付查单请求数据："+paras);
			String result = HttpUtil.post(PayConstant.PAY_CONFIG.get("YLTF_RECEIVEPAYSINGLE_QUERY"), paras, "UTF-8");
			log.info("亿联通付查单响应数据："+result);
			/**
			 * {"code":"SUCCESS","mid":"822017071724115","msg":"订单不存在","noise":"nGmKZhePIndeqteBV34We7676An4cW4U",
			 * "resultCode":"NO_ORDER","sign":"DA105A9227417CFA1562B823C7604CAF"}
			 */
			Map<String, Object> resMap = JSON.parseObject(result);
			Map<String, String> respMap = new HashMap<String, String>();
			Iterator<String> iterator = resMap.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = String.valueOf(resMap.get(key));
				if(!"".equals(value) && value != null && !"sign".equals(key)){
					respMap.put(key, value);
				}
			}
			String resSign = SignUtil.generateMD5(respMap, PayConstant.PAY_CONFIG.get("YLTF_KEY"));
			if(resSign.equals(resMap.get("sign"))){
				JSONObject resJson = (JSONObject) JSONObject.parse(result);
				if("SUCCESS".equals(resJson.getString("code")) && "SUCCESS".equals(resJson.getString("resultCode"))){
					if("3".equals(resJson.getString("transState"))){
						rp.status="1";
						rp.respCode="000";
						payRequest.respCode="000";
						rp.errorMsg="交易成功";
						payRequest.respDesc = rp.errorMsg;
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
						list.add(rp);
						new PayInterfaceOtherService().receivePayNotify(payRequest,list);
						return true;
					} else if("4".equals(resJson.getString("transState"))){//失败
						rp.status="2";
						rp.respCode="-1";
						rp.errorMsg="交易失败";
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
//						List<PayReceiveAndPay> list3 = new ArrayList<PayReceiveAndPay>();
//						list3.add(rp);
//						new PayInterfaceOtherService().receivePayNotify(payRequest,list3);
						return true;
					}
				} else {
					rp.status="2";
					rp.respCode="-1";
					rp.errorMsg="交易失败";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
//					List<PayReceiveAndPay> list3 = new ArrayList<PayReceiveAndPay>();
//					list3.add(rp);
//					new PayInterfaceOtherService().receivePayNotify(payRequest,list3);
					return true;
				}
			} else throw new Exception("亿联通付验签失败");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}