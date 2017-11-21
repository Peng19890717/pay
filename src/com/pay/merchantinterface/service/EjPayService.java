package com.pay.merchantinterface.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.PayUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.third.emaxPlus.AesEncryptUtil;
import com.third.emaxPlus.EmaxHttpClient;
import com.third.emaxPlus.EmaxPlusConstant;
import com.third.emaxPlus.EmaxPlusPayResponse;
import com.third.emaxPlus.EmaxPlusPayResult;
import com.third.emaxPlus.EmaxPlusUtil;
import com.third.emaxPlus.EmaxPlusWithdrawals;
import com.third.emaxPlus.HttpClientUtil;
import com.third.emaxPlus.HttpClientUtils;
import com.third.emaxPlus.JsonUtils;

/**
 * 溢+ 接口服务类
 * @author lqq
 *
 */
public class EjPayService {
	private static final Log log = LogFactory.getLog(EjPayService.class);
	/**
	 * 扫码
	 * @param productType("1":微信，"2":支付宝)
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		try {
			String productDesc = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String URL = PayConstant.PAY_CONFIG.get("EMAX_PAYURL")+EmaxPlusConstant.CREATEORDER_URL;
			String merCode = PayConstant.PAY_CONFIG.get("EMAX_MERNO");//溢+ 商户号
			String orderNo = payOrder.payordno;//订单编号
			String orderAmount = String.valueOf(payOrder.txamt);//订单金额
			String callbackUrl = PayConstant.PAY_CONFIG.get("EMAX_NOTIFYURL");//异步通知地址
			String showUrl = "";//跳转地址，为空
			String payType = productType;//扫码类型，1 微信,2 支付宝,5 QQ扫码
			String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime);
			String validityNum = "120";//有效时长
			
            Map<String ,String> payParam = new HashMap<String,String>();
            payParam.put("orderNo",orderNo);//订单号
            payParam.put("orderAmount",new BigDecimal(orderAmount).intValue()+"");//订单金额,以分为单位
            payParam.put("payType", payType);//支付类型
            payParam.put("callbackUrl", callbackUrl);//回调地址
            payParam.put("productDesc",productDesc);// 商品描述
            payParam.put("merCode",merCode);//溢+开户账户编号
            payParam.put("dateTime", dateTime);//时间
            payParam.put("validityNum",validityNum);//订单有效时间
            payParam.put("showUrl",showUrl);//支付成功页面
            String url = EmaxPlusUtil.getSignPlainText(payParam);//注意签名顺序
            String sign = EmaxPlusUtil._md5Encode(url);
            payParam.put("sign",sign);//签名
            log.info("溢+扫码请求数据:"+JSON.toJSONString(payParam));
            String body = _executePostMethod(URL,payParam);
            log.info("溢+扫码返回数据:"+body);
            
            if(body!=null&&body.length()>0){
            	EmaxPlusPayResponse result = JSON.parseObject(body, EmaxPlusPayResponse.class);
            	if("000000".equals(result.getResultCode())&&"SUCCESS".equals(result.getResultStatus())){
            			String qrCode = result.getQrCodeUrl();
            			if("5".equals(productType)){
            				qrCode = PayConstant.PAY_CONFIG.get("EMAX_QQSCAN_PIC_URL")+"?url="+java.net.URLEncoder.encode(qrCode, "utf-8");
            			}
            			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
    					"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
    					"codeUrl=\""+qrCode+"\" respCode=\"000\" respDesc=\"成功\"/>";
            	}else throw new Exception("溢+ 扫码下单失败");
            }else throw new Exception("溢+ 扫码下单失败");
			
		} catch (Exception e) {
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
				"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
		}
	}
	
	/**
	 * 渠道补单,(扫码、网银、快捷)
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try {
			String payUrl = PayConstant.PAY_CONFIG.get("EMAX_PAYURL");
			String merCode = PayConstant.PAY_CONFIG.get("EMAX_MERNO");//溢+ 商户号
			String orderNo = payOrder.payordno;//订单编号
			String dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//查询时间
			
			Map<String, String> param = new HashMap<String,String>();
			 param.put("merCode",merCode);
	            param.put("orderNo",orderNo);
	            param.put("dateTime",dateTime);
	            String url = EmaxPlusUtil.getSignPlainText(param);
	            String sign = EmaxPlusUtil._md5Encode(url);
	            param.put("sign", sign);
	            log.info("溢+渠道补单请求数据:"+JSON.toJSONString(param));
	            String txt = _executePostMethod(payUrl + EmaxPlusConstant.GETPAYINFO_URL,param);
	            log.info("溢+渠道补单响应数据:"+txt);
	            if(txt!=null&&txt.length()>0){
	            	EmaxPlusPayResult emaxPlusPayResult = JSON.parseObject(txt, EmaxPlusPayResult.class);
	            	if("000000".equals(emaxPlusPayResult.getResultCode())){
	            		if("SUCCESS".equals(emaxPlusPayResult.getResultStatus())){
	            			if("YWC".equals(emaxPlusPayResult.getOrderStatus())){
	            				payOrder.ordstatus="01";//支付成功
	            				new NotifyInterface().notifyMer(payOrder);//支付成功
	            			}
	            		}
	            	}else throw new Exception("溢+ 扫码查询失败");
	            }else throw new Exception("溢+ 扫码查询失败");
	            
		} catch (Exception e) {
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
			 PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//获取单笔代收付object
			 rp.setCreateTime(new Date());
			 PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取银行卡信息
			 if(cardBin == null) throw new Exception("无法识别银行账号"); 
			 //加载常量
			 String merchantID = PayConstant.PAY_CONFIG.get("EMAX_MERNO");//商户号
			 String privateKey = PayConstant.PAY_CONFIG.get("EMAX_PRIVATEKEY");//私钥
			 String payUrl = PayConstant.PAY_CONFIG.get("EMAX_PAYURL");//网关地址
			 /*
			  * 拼装请求JSON
			  */
			 String aesKey = privateKey.substring(0,16);//AES对称加密 ,取私钥前16位
			 Map<String,String> queryMap = new HashMap<String,String>();
	         queryMap.put("merCode",merchantID);//商户编号
	         queryMap.put("orderId",rp.channelTranNo);//商户订单号
	         queryMap.put("totalAmount",new BigDecimal(payRequest.amount)+"");//代付金额，分为单位
	         queryMap.put("cardByName", AesEncryptUtil.aesEncrypt(rp.accountName, aesKey));//持卡人姓名
	         queryMap.put("cardByNo", AesEncryptUtil.aesEncrypt(rp.accountNo, aesKey));//卡号
	         queryMap.put("tradeTime", new SimpleDateFormat("yyyyMMddHHmmss").format(rp.getCreateTime()));//交易时间
	         String accType = "";
	         if ("0".equals(rp.accountProp)) {//"0",对私
	        	 accType = "0";
	         } else {
	        	 accType = "1";
	         }
	         queryMap.put("accType",accType);//0对私，1对公
	         queryMap.put("bankCode",cardBin.bankCode);//银行编码
	         String url = EmaxPlusUtil.getSignPlainText(queryMap);
	         String sign = EmaxPlusUtil._md5Encode(url);
	         queryMap.put("sign", sign);
	         log.info("溢+代付请求数据:"+JSON.toJSONString(queryMap));
	         String body = _executePostMethod(payUrl+EmaxPlusConstant.WITHDRAWALS_URL,queryMap);
	         log.info("溢+代付响应数据:"+body);
	         EmaxPlusWithdrawals responseMessage = JsonUtils.fromJson(body,EmaxPlusWithdrawals.class);
	         if ("000000".equals(responseMessage.getResultCode())) {//请求成功
	        	 if("3".equals(responseMessage.getPayStatus())){//预设3
	        		 rp.status="0";
	        		 rp.errorMsg = "操作成功";
	        		 try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
	        		 EmaxQueryThread emaxQueryThread = new EmaxQueryThread(payRequest);
	        		 emaxQueryThread.start();
	        	 } else {
	        		 rp.status="2";
					 rp.errorMsg = "操作失败";
		        	 try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	        	 }
	         }else{
	        	 rp.status="2";
				 rp.errorMsg = "操作失败";
	        	 try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	         }
		 } catch (Exception e) {
			 e.printStackTrace();
			 log.info(PayUtil.exceptionToString(e));
			 throw e;
		 }
	 }
	 
    /**
	  * 代收付查询入口
	  * @param payRequest
	  * @param rp
	  * @return
	  * @throws Exception
	  */
	public boolean receivePaySingleQuery(PayRequest payRequest,PayReceiveAndPay rp) throws Exception{
		try {
			String payUrl = PayConstant.PAY_CONFIG.get("EMAX_PAYURL");//网关地址
			
			String merCode = PayConstant.PAY_CONFIG.get("EMAX_MERNO");//商户号
			String orderDt = new SimpleDateFormat("yyyyMMdd").format(rp.createTime);//订单日期
			String orderNo = rp.channelTranNo;//订单号
			Map<String ,String> queryMap = new HashMap<String,String>();
			queryMap.put("merCode",merCode);//溢+开户账户编号
			queryMap.put("orderDt", orderDt);//订单日期
            queryMap.put("orderId",orderNo);//订单号
            
            String url = EmaxPlusUtil.getSignPlainText(queryMap);
            String sign = EmaxPlusUtil._md5Encode(url);
            queryMap.put("sign", sign);
            log.info("溢+渠道补单请求数据:"+JSON.toJSONString(queryMap));
            String txt = _executePostMethod(payUrl + EmaxPlusConstant.GET_WITHDRAWALS_URL,queryMap);
            log.info("溢+渠道补单响应数据:"+txt);
            EmaxPlusWithdrawals result = JSON.parseObject(txt, EmaxPlusWithdrawals.class);
            if("000000".equals(result.getResultCode())){
            	if("S".equals(result.getPayStatus())){
            		payRequest.setRespCode("000");
					payRequest.receiveAndPaySingle.setRespCode("000");
					payRequest.receivePayRes = "0";
					rp.errorMsg = "处理成功";
					return true;
            	} else if("C".equals(result.getPayStatus())){
            		payRequest.respCode ="0";
    				payRequest.respDesc = result.getResultMsg();
    				rp.errorMsg = payRequest.respDesc;
    				return false;
            	} else {
            		payRequest.respCode ="-1";
    				payRequest.respDesc = result.getResultMsg();
    				rp.errorMsg = payRequest.respDesc;
            		return true;
            	}
            } else throw new Exception("查询失败!");
            
		} catch (Exception e) {
			e.printStackTrace();
			payRequest.setRespCode("-1");
			rp.setRespCode(payRequest.respCode);
			payRequest.receivePayRes = "-1";
			payRequest.respDesc=e.getMessage();
			rp.errorMsg = payRequest.respDesc;
			return false;
		}
	}
	 
	/**
	 * post
	 * @param url String
	 * @param param Map
	 * @return String
	 */
	private String _executePostMethod(String url ,Map<String ,String> param) {
	    log.info("调用溢+参数 :"+param);
	    EmaxHttpClient client = new EmaxHttpClient();
	    HttpMethod method = new PostMethod(url);
	    HttpClientUtils.putParameters(method,param);
	    return getExecuteTxt(client, method);
	}
	
    private String getExecuteTxt(EmaxHttpClient client,HttpMethod method) {
        try {
            int state = client.executeMethod(method);
            if(state==200){
                byte[] resp = client.getResponseBodyAsByte(method);
                String txt = new String(resp,"UTF-8");
                log.info("调用溢+返回:"+txt);
                return txt;
            }
        } catch (IOException e) {
            log.error("调用溢+出错:",e);
        }finally {
            releaseConnection(client,method);
        }
        return "{}";
    }
    
    private void releaseConnection(HttpClient httpClient, HttpMethod method){
        try {
            method.releaseConnection();
        } catch (Exception e) {
            log.error("httpclient,releaseConnection时出错："+e.getMessage(),e);
        }
        try {
            httpClient.getHttpConnectionManager().closeIdleConnections(0);
        } catch (Exception e) {
            log.error("httpclient,closeIdleConnections时出错："+e.getMessage(),e);
        }
    }
    
}

/**
 * 查询代付 线程类
 * @author lqq
 *
 */
class EmaxQueryThread extends Thread {
	
	private static final Log log = LogFactory.getLog(EmaxQueryThread.class);
	
	public static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
	String payUrl = PayConstant.PAY_CONFIG.get("EMAX_PAYURL");//网关地址
	String URL = payUrl + EmaxPlusConstant.GET_WITHDRAWALS_URL;//代付查询地址
	public static Map<String,String> msgMap = new HashMap<String, String>();
	static{
		msgMap.put("S", "收款完成！");
		msgMap.put("F", "代付订单失败！");
		msgMap.put("N", "代付订单不存在！");
		msgMap.put("R", "风控拒绝！");
		msgMap.put("C", "处理中！");
	}
	
	private  PayRequest payRequest= new PayRequest();
	public EmaxQueryThread (PayRequest payRequest) {
		this.payRequest = payRequest;
	}

	@Override
	public void run() {
		for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
			try {if(query())break;} catch (Exception e) {}
			log.info("溢+ 代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
			try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
		}
	}
	
	public boolean query()throws Exception{
		
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//代付对象
			Map<String,String> param = new HashMap<String, String>();
			param.put("merCode", PayConstant.PAY_CONFIG.get("EMAX_MERNO"));//商户号
			param.put("orderId", rp.channelTranNo);//订单号
			param.put("orderDt", sf.format(rp.getCreateTime()));//订单日期,精确到天
			String url = EmaxPlusUtil.getSignPlainText(param);//注意签名顺序
			String sign = EmaxPlusUtil._md5Encode(url);
			param.put("sign",sign);//签名
	        String reqStr=JsonUtils.toJson(param);
	        log.info("溢+ 代付查询请求参数:"+param);
	        String txt = HttpClientUtil.doPost(URL,reqStr);
	        log.info("溢+ 代付查询结果:"+txt);
	        EmaxPlusWithdrawals emaxPlusPayResult = JSON.parseObject(txt, EmaxPlusWithdrawals.class);//返回结果  封装对象
	        //溢+渠道补单响应数据:{"resultCode":"000000","resultMsg":"支付中","MerCode":"9001000178","OrderId":"1000000_1504596098515","totalAmount":"150.00","tradeTime":"20170905152140","PayStatus":"C"}
	        if("000000".equals(emaxPlusPayResult.getResultCode())){//返回码
	        	if("S".equals(emaxPlusPayResult.getPayStatus())){//收款完成
	        		rp.status="1";
					rp.respCode="000";
					rp.errorMsg="交易成功";
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
					list.add(rp);
					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
					return true;
	        	} else if ("C".equals(emaxPlusPayResult.getPayStatus())){//处理中
	        		return false;
	        	} else{
	        		rp.status="2";
					rp.respCode="-1";
					rp.errorMsg = msgMap.get(emaxPlusPayResult.getResultStatus());
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					return true;
	        	}
	        }else{
	        	rp.status="2";
				rp.respCode="-1";
				rp.errorMsg="查询失败";
				try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				return true;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
