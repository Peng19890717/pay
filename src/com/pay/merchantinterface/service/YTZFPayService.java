package com.pay.merchantinterface.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.ytzf.tools.GatewayRequest;
import com.third.ytzf.tools.HttpPostUtil;
import com.third.ytzf.tools.ResponseHelper;
import com.third.ytzf.util.ToolUtil;
/**
 * 易通支付
 * @author Administrator
 *
 */
public class YTZFPayService {
	private static final Log log = LogFactory.getLog(YTZFPayService.class);
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public String pay(HttpServletRequest request,PayProductOrder prdOrder,PayOrder payOrder){
		try{
			String ret_data="";
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YTZF"); // 支付渠道
			payOrder.bankcod = request.getParameter("banks"); // 银行代码
			payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			//商户号
			String merchant_id = PayConstant.PAY_CONFIG.get("YTZF_MERNO");
			//产品编码
			String product_id = "3";
			//支付方式编码
			String payment_id = "3";
			//银行编码  默认CUP（银联）， 跳转银联收银台
			String bank_code = payOrder.bankcod;
			//商户订单号，此处用系统当前毫秒数，商户根据自己情况调整，确保该订单号在商户系统中的全局唯一
			String order_id = payOrder.payordno;
			//支付金额
			String amount = String.format("%.2f", Double.parseDouble(String.valueOf(payOrder.txamt))/100d);
			//商品名称
//			String body = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
//					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称;
			String key = PayConstant.PAY_CONFIG.get("YTZF_MD5_KEY"); 
			//交易完成后跳转的URL，用来接收网关的页面转跳即时通知结果
			String return_url = "#";
			//接收支付网关异步通知的URL
			String notify_url = PayConstant.PAY_CONFIG.get("YTZF_NOTIFY_URL");
			//随机串
			String nonce_str = ToolUtil.getRandomString(32);
			//交易请求地址
			String gatewayUrl = PayConstant.PAY_CONFIG.get("YTZF_PAY_URL");

			//支付请求对象
			GatewayRequest gr = new GatewayRequest(request, null, GatewayRequest.BULID_SIGN);
			//通信对象
			HttpPostUtil postUtil = new HttpPostUtil();
			//设置商户密钥
			gr.setKey(key); 
			gr.setGatewayUrl(gatewayUrl);
			//设置支付请求参数
			gr.setParameter("merchant_id", merchant_id);		      
			gr.setParameter("product_id", product_id);		      
			gr.setParameter("payment_id", payment_id);		       
			gr.setParameter("bank_code", bank_code);	
			gr.setParameter("order_id", order_id);	
			gr.setParameter("amount", amount);			
			gr.setParameter("return_url", return_url);		    
			gr.setParameter("notify_url", notify_url);		   
			gr.setParameter("nonce_str", nonce_str);
			//构造签名
			gr.buildRequestSign();
			//获取提交到网关的请求地址
			String requestUrl = gr.getRequestURL(); 
			if (postUtil.postRequest(requestUrl)) {
				String json = postUtil.getResContent();
				log.info("易通支付网银响应数据:"+json);
				Map maps = (Map)JSON.parse(json);
				String code = maps.get("code").toString();
				if(code.equals("00")){
					ret_data=maps.get("pay_url").toString();
				}
			}else{
				log.info("易通支付网银请求失败");
			}
			new PayOrderService().updateOrderForBanks(payOrder);
			return ret_data;
		}catch(Exception e){
			e.printStackTrace();
			return null;
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
	public String pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		try{
			String ret_data="";
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YTZF"); // 支付渠道
			payOrder.bankcod = payRequest.bankId; // 银行代码
			payOrder.bankCardType = payRequest.accountType;
			//商户号
			String merchant_id = PayConstant.PAY_CONFIG.get("YTZF_MERNO");
			//产品编码
			String product_id = "3";
			//支付方式编码
			String payment_id = "3";
			//银行编码  默认CUP（银联）， 跳转银联收银台
			String bank_code = payOrder.bankcod;
			//商户订单号，此处用系统当前毫秒数，商户根据自己情况调整，确保该订单号在商户系统中的全局唯一
			String order_id = payOrder.payordno;
			//支付金额
			String amount = String.format("%.2f", Double.parseDouble(String.valueOf(payOrder.txamt))/100d);
			//商品名称
//			String body = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
//					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称;
			String key = PayConstant.PAY_CONFIG.get("YTZF_MD5_KEY"); 
			//交易完成后跳转的URL，用来接收网关的页面转跳即时通知结果
			String return_url = "#";
			//接收支付网关异步通知的URL
			String notify_url = PayConstant.PAY_CONFIG.get("YTZF_NOTIFY_URL");
			//随机串
			String nonce_str = ToolUtil.getRandomString(32);
			//交易请求地址
			String gatewayUrl = PayConstant.PAY_CONFIG.get("YTZF_PAY_URL");

			//支付请求对象
			GatewayRequest gr = new GatewayRequest(request, null, GatewayRequest.BULID_SIGN);
			//通信对象
			HttpPostUtil postUtil = new HttpPostUtil();
			//设置商户密钥
			gr.setKey(key); 
			gr.setGatewayUrl(gatewayUrl);
			//设置支付请求参数
			gr.setParameter("merchant_id", merchant_id);		      
			gr.setParameter("product_id", product_id);		      
			gr.setParameter("payment_id", payment_id);		       
			gr.setParameter("bank_code", bank_code);	
			gr.setParameter("order_id", order_id);	
			gr.setParameter("amount", amount);			
			gr.setParameter("return_url", return_url);		    
			gr.setParameter("notify_url", notify_url);		   
			gr.setParameter("nonce_str", nonce_str);
			//构造签名
			gr.buildRequestSign();
			//获取提交到网关的请求地址
			String requestUrl = gr.getRequestURL(); 
			if (postUtil.postRequest(requestUrl)) {
				String json = postUtil.getResContent();
				log.info("易通支付网银响应数据:"+json);
				Map maps = (Map)JSON.parse(json);
				String code = maps.get("code").toString();
				if(code.equals("00")){
					ret_data=maps.get("pay_url").toString();
				}
			}else{
				log.info("易通支付网银请求失败");
			}
			new PayOrderService().updateOrderForBanks(payOrder);
			return ret_data;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 渠道补单---网关
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			String gateway_url = PayConstant.PAY_CONFIG.get("YTZF_QUERY_URL");
			//商户号 
			String merchant_id = PayConstant.PAY_CONFIG.get("YTZF_MERNO");
			//订单号
			String order_id = payOrder.payordno;
			//随机穿
			String nonce_str = ToolUtil.getRandomString(32);;
			//密钥 
			String key =  PayConstant.PAY_CONFIG.get("YTZF_MD5_KEY");
			//创建查询请求对象
			GatewayRequest gr = new GatewayRequest(null, null);
			//通信对象
			HttpPostUtil postUtil = new HttpPostUtil();
			//应答对象
			ResponseHelper resHelper = new ResponseHelper();
			//设置请求参数
			gr.setKey(key);
			gr.setGatewayUrl(gateway_url);
			gr.setParameter("merchant_id", merchant_id);//商户号
			gr.setParameter("order_id", order_id); //订单号
			gr.setParameter("nonce_str", nonce_str); //随机串
			//构造签名
			gr.buildRequestSign();
			//设置请求内容
			String requestUrl = gr.getRequestURL();
			//后台调用
			if (postUtil.postRequest(requestUrl)) {
					String json = postUtil.getResContent();
					log.info("易通支付查询响应参数:"+json);
					resHelper.setContent(json);
					resHelper.setKey(key);
					//获取返回码
					String code = resHelper.getParameter("code");
					//判断签名及结果
					if (resHelper.verifySign() && "00".equals(code)) {
						if("3".equals(resHelper.getParameter("status"))){
							payOrder.ordstatus="01";//支付成功
				        	new NotifyInterface().notifyMer(payOrder);//支付成功
						}else if("4".equals(resHelper.getParameter("status"))){
							payOrder.ordstatus="02";//支付失败。
				        	new NotifyInterface().notifyMer(payOrder);//支付成功
						}
					} 
			} 
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 实时代付。
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingle(PayRequest payRequest) throws Exception{
		 try {
			 if("1".equals(payRequest.receivePayType)){//代付业务。
				 PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
			     PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
			     String gateway_url = PayConstant.PAY_CONFIG.get("YTZF_DF_PAY_URL");
			     String merchant_id = PayConstant.PAY_CONFIG.get("YTZF_MERNO");
			     String order_id =rp.id;
			     String product_id = "3";//产品编码
			     String payment_id = "3";//支付方式编码
			     String bank_account_no =payRequest.accNo;//卡号。
			     String bank_name = cardBin.bankName;//开户行。
			     String bank_branch = cardBin.bankName;//支行名称。
			     String bank_cnaps_code = payRequest.bankId;//联行号
			     String bank_account_name =payRequest.accName;//姓名
			     String amount = String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d);
			     String nonce_str = ToolUtil.getRandomString(32);//随机字符串
			     String key =  PayConstant.PAY_CONFIG.get("YTZF_MD5_KEY");//密钥 
			     //创建查询请求对象
			     GatewayRequest gr = new GatewayRequest(null, null);
			     //通信对象
			     HttpPostUtil postUtil = new HttpPostUtil();
			     //应答对象
			     ResponseHelper resHelper = new ResponseHelper();
			     //设置请求参数
			     gr.setKey(key);
			     gr.setGatewayUrl(gateway_url);
			     gr.setParameter("merchant_id", merchant_id);
			     gr.setParameter("order_id", order_id); 
			     gr.setParameter("product_id", product_id);
			     gr.setParameter("payment_id", payment_id);
			     gr.setParameter("bank_account_no", bank_account_no);
			     gr.setParameter("bank_name", bank_name);
			     gr.setParameter("bank_branch", bank_branch);
			     gr.setParameter("bank_cnaps_code", bank_cnaps_code);
			     gr.setParameter("bank_account_name", bank_account_name);
			     gr.setParameter("amount", amount);
			     gr.setParameter("nonce_str", nonce_str); 
			     //构造签名
			     gr.buildRequestSign();
			     //设置请求内容
			     String requestUrl = gr.getRequestURL();
			     if (postUtil.postRequest(requestUrl)) {
						String json = postUtil.getResContent();
						log.info("易通支付代付响应参数:"+new String(json.getBytes("utf-8"),"utf-8"));
						resHelper.setContent(json);
						resHelper.setKey(key);
						//判断签名及结果
						if (resHelper.verifySign() && "00".equals(resHelper.getParameter("code"))) {
							//成功
							if("3".equals(resHelper.getParameter("status"))){
								payRequest.respCode="000";
								rp.status="1";
								rp.retCode="000";
								rp.errorMsg = "交易成功";
								payRequest.respDesc=rp.errorMsg;
					        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
					        	List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
								list.add(rp);
								new PayInterfaceOtherService().receivePayNotify(payRequest,list);
							//失败
							}else if("4".equals(resHelper.getParameter("status"))){
								payRequest.respCode="-1";
								rp.status="2";
								rp.retCode="-1";
								rp.errorMsg = new String(resHelper.getParameter("msg").getBytes("utf-8"),"utf-8");
								payRequest.respDesc=rp.errorMsg;
					        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
					        	List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
								list.add(rp);
								new PayInterfaceOtherService().receivePayNotify(payRequest,list);
							//处理中
							}else if("2".equals(resHelper.getParameter("status"))){
								payRequest.respCode="000";
								rp.status="0";
								rp.retCode="074";
								rp.errorMsg = "提交成功";
								payRequest.respDesc=rp.errorMsg;
					        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
					        	//启用定时查询。
					        	YTZFQueryThread ytzfQueryThread = new YTZFQueryThread(payRequest);
					        	ytzfQueryThread.start();
							}
						}else{
							payRequest.respCode="-1";
							rp.status="2";
							rp.retCode="-1";
							rp.errorMsg = "代付失败";
							payRequest.respDesc=rp.errorMsg;
				        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
						} 
					} 
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
    /**
	  * 代收付查询
	  * @param payRequest
	  * @param rp
	  * @return
	  * @throws Exception
	  */
	public void receivePaySingleQuery(PayRequest payRequest,PayReceiveAndPay rp) throws Exception{
		try{
			String gateway_url = PayConstant.PAY_CONFIG.get("YTZF_DF_QUERY_URL");
			String merchant_id = PayConstant.PAY_CONFIG.get("YTZF_MERNO");
			String order_id =rp.id;
			String nonce_str = ToolUtil.getRandomString(32);//随机字符串
			String key =  PayConstant.PAY_CONFIG.get("YTZF_MD5_KEY");//密钥 
			//创建查询请求对象
			GatewayRequest gr = new GatewayRequest(null, null);
			//通信对象
			HttpPostUtil postUtil = new HttpPostUtil();
			//应答对象
			ResponseHelper resHelper = new ResponseHelper();
			//设置请求参数
			gr.setKey(key);
			gr.setGatewayUrl(gateway_url);
			gr.setParameter("merchant_id", merchant_id);
			gr.setParameter("order_id", order_id); 
			gr.setParameter("nonce_str", nonce_str); 
			//构造签名
			gr.buildRequestSign();
			//设置请求内容
			String requestUrl = gr.getRequestURL();
			//后台调用
			if (postUtil.postRequest(requestUrl)) {
					String json = postUtil.getResContent();
					/**
					 * {"code":"00","order_id":"1505957618442","out_order_id":"00007275_2394318000",
					 * "amount":"2.00","fee":"1.00","time_create":"2017-09-21 09:33:33",
					 * "time_update":"2017-09-21 09:36:01","status":"3",
					 * "nonce_str":"d6e2jGd","sign":"7c085a4021c44f607732f3460d563dad"}
					 */
					log.info("易通支付代付查询响应参数:"+new String(json.getBytes("utf-8"),"utf-8"));
					resHelper.setContent(json);
					resHelper.setKey(key);
					//判断签名及结果
					if (resHelper.verifySign() && "00".equals(resHelper.getParameter("code"))) {
						//成功
						if("3".equals(resHelper.getParameter("status"))){
							payRequest.setRespCode("000");
							payRequest.respDesc="交易成功";
							rp.status="1";
							rp.retCode="000";
							rp.errorMsg = "交易成功";
							new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
						//失败。
						}else if("4".equals(resHelper.getParameter("status"))){
							payRequest.setRespCode("-1");
							payRequest.respDesc="交易失败";
							rp.status="2";
							rp.retCode="-1";
							rp.errorMsg = "交易失败";
							new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
						//处理中，不处理。
						}else if("2".equals(resHelper.getParameter("status"))){
							
						}
					} 
			} 
		} catch (Exception e){
			e.printStackTrace();
		}	
	}
}
/**
 * 查询代付 线程类 
 */
class YTZFQueryThread extends Thread {
	
	private static final Log log = LogFactory.getLog(YTZFQueryThread.class);
	
	private  PayRequest payRequest= new PayRequest();
	public YTZFQueryThread (PayRequest payRequest) {
		this.payRequest = payRequest;
	}
	
	@Override
	public void run() {
		for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
			try {if(query())break;} catch (Exception e) {}
			log.info("易通支付代付：查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
			try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
		}
	}
	public boolean query() throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//代付对象
			String gateway_url = PayConstant.PAY_CONFIG.get("YTZF_DF_QUERY_URL");
			String merchant_id = PayConstant.PAY_CONFIG.get("YTZF_MERNO");
			String order_id =rp.id;
			String nonce_str = ToolUtil.getRandomString(32);//随机字符串
			String key =  PayConstant.PAY_CONFIG.get("YTZF_MD5_KEY");//密钥 
			//创建查询请求对象
			GatewayRequest gr = new GatewayRequest(null, null);
			//通信对象
			HttpPostUtil postUtil = new HttpPostUtil();
			//应答对象
			ResponseHelper resHelper = new ResponseHelper();
			//设置请求参数
			gr.setKey(key);
			gr.setGatewayUrl(gateway_url);
			gr.setParameter("merchant_id", merchant_id);
			gr.setParameter("order_id", order_id); 
			gr.setParameter("nonce_str", nonce_str); 
			//构造签名
			gr.buildRequestSign();
			//设置请求内容
			String requestUrl = gr.getRequestURL();
			//后台调用
			if (postUtil.postRequest(requestUrl)) {
					String json = postUtil.getResContent();
					/**
					 * {"code":"00","order_id":"1505957618442","out_order_id":"00007275_2394318000",
					 * "amount":"2.00","fee":"1.00","time_create":"2017-09-21 09:33:33",
					 * "time_update":"2017-09-21 09:36:01","status":"3",
					 * "nonce_str":"d6e2jGd","sign":"7c085a4021c44f607732f3460d563dad"}
					 */
					log.info("易通支付代付查询响应参数:"+new String(json.getBytes("utf-8"),"utf-8"));
					resHelper.setContent(json);
					resHelper.setKey(key);
					//判断签名及结果
					if (resHelper.verifySign() && "00".equals(resHelper.getParameter("code"))) {
						//成功
						if("3".equals(resHelper.getParameter("status"))){
							rp.status="1";
							rp.retCode="000";
							rp.errorMsg = "交易成功";
							new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
							List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
							list.add(rp);
							new PayInterfaceOtherService().receivePayNotify(payRequest,list);
							return true;
						//失败。
						}else if("4".equals(resHelper.getParameter("status"))){
							rp.status="2";
							rp.retCode="-1";
							rp.errorMsg = "交易失败";
							new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
							List<PayReceiveAndPay> list2 = new ArrayList<PayReceiveAndPay>();
							list2.add(rp);
							new PayInterfaceOtherService().receivePayNotify(payRequest,list2);
							return true;
						//处理中，不处理,继续执行查询。
						}else if("2".equals(resHelper.getParameter("status"))){
							return false;
						}
					} 
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
