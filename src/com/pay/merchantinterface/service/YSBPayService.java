package com.pay.merchantinterface.service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.DataTransUtil;
import util.MD5;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.merchantinterface.dao.PayReceiveAndPaySign;
import com.pay.merchantinterface.dao.PayReceiveAndPaySignDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.pay.refund.dao.PayRefund;
/**
 * 银生宝服务类
 * @author Administrator
 *
 */
public class YSBPayService {
	private static final Log log = LogFactory.getLog(YSBPayService.class);
	public static Map<String, String> BANK_CODE = new HashMap<String, String>();
	public static Map<String, String> ERROR_MESSAGE = new HashMap<String, String>();
	static {
		BANK_CODE.put("CMB","cmb");//招商银行
		BANK_CODE.put("ICBC","icbc");//工商银行
		BANK_CODE.put("CCB","ccb");//建设银行
		BANK_CODE.put("BOC","boc");//中国银行
		BANK_CODE.put("ABC","abc");//农业银行
		BANK_CODE.put("BOCOM","comm");//交通银行
		BANK_CODE.put("SPDB","spdb");//浦发银行
		BANK_CODE.put("GDB","gdb");//广发银行
		BANK_CODE.put("CNCB","cncb");//中信银行
		BANK_CODE.put("CEB","ceb");//光大银行
		BANK_CODE.put("CIB","cib");//兴业银行
		BANK_CODE.put("CMBC","cmbc");//民生银行
		BANK_CODE.put("HXB","hxb");//华夏银行
		BANK_CODE.put("PSBC","psbc");//邮储银行
		BANK_CODE.put("BCCB","BCCB");//北京银行
		BANK_CODE.put("BOS","bosh");//上海银行
		BANK_CODE.put("BOS","bob");//北京银行
		BANK_CODE.put("SHRCB","shrcb");//上海农商银行。
		BANK_CODE.put("WZCB","bowz");//温州银行。
		
		ERROR_MESSAGE.put("0", "待确认");
		ERROR_MESSAGE.put("4", "支付失败");
		ERROR_MESSAGE.put("6", "支付取消");
		ERROR_MESSAGE.put("9", "退款成功");
		
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try{
			String version = "3.0.0";
			String merchantId=PayConstant.PAY_CONFIG.get("YSB_MERCHANT_ID");
			String merchantUrl = PayConstant.PAY_CONFIG.get("YSB_WG_PAY_NOTIFY_URL");
			String responseMode = "2";
			String orderId = payOrder.payordno;
			String currencyType = "CNY";
			String amount = String.format("%.2f", ((double)payOrder.txamt)/100d);
			String assuredPay = "false";//非担保支付。
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime);
			String remark = "WGPay";
			String merchantKey = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_KEY");
			String signParams = "merchantId="+merchantId+"&merchantUrl="+merchantUrl+"&responseMode="+responseMode+
								"&orderId="+orderId+"&currencyType="+currencyType+"&amount="+amount+
								"&assuredPay="+assuredPay+"&time="+time+"&remark="+remark+"&merchantKey="+merchantKey;
			log.info("银生网关签名原数据:"+signParams);
			String mac =MD5.getDigest(signParams).toUpperCase();
			String bankCode = BANK_CODE.get(payOrder.bankcod);
			request.setAttribute("version",version);
			request.setAttribute("merchantId",merchantId);
			request.setAttribute("merchantUrl",merchantUrl);
			request.setAttribute("responseMode",responseMode);
			request.setAttribute("orderId",orderId);
			request.setAttribute("currencyType",currencyType);
			request.setAttribute("amount",amount);
			request.setAttribute("assuredPay",assuredPay);
			request.setAttribute("remark",remark);
			request.setAttribute("mac",mac);
			request.setAttribute("bankCode",bankCode);
			request.setAttribute("time",time);
			String logStr = "version="+version+"&merchantId="+merchantId+"&merchantUrl="+merchantUrl+"&responseMode="+responseMode+
					"&orderId="+orderId+"&currencyType="+currencyType+"&amount="+amount+"&assuredPay="+assuredPay+"&remark="+remark+
					"&mac="+mac+"&bankCode="+bankCode+"&time="+time;
			log.info("银生宝网关请求数据:"+logStr);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
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
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		
		try{
			String version = "3.0.0";
			String merchantId=PayConstant.PAY_CONFIG.get("YSB_MERCHANT_ID");
			String merchantUrl = PayConstant.PAY_CONFIG.get("YSB_WG_PAY_NOTIFY_URL");
			String responseMode = "2";
			String orderId = payOrder.payordno;
			String currencyType = "CNY";
			String amount = String.format("%.2f", ((double)payOrder.txamt)/100d);
			String assuredPay = "false";//非担保支付。
			String time = new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.createtime);
			String remark = "WGPay";
			String merchantKey = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_KEY");
			String signParams = "merchantId="+merchantId+"&merchantUrl="+merchantUrl+"&responseMode="+responseMode+
								"&orderId="+orderId+"&currencyType="+currencyType+"&amount="+amount+
								"&assuredPay="+assuredPay+"&time="+time+"&remark="+remark+"&merchantKey="+merchantKey;
			log.info("银生网关签名原数据:"+signParams);
			String mac =MD5.getDigest(signParams).toUpperCase();
			String bankCode = BANK_CODE.get(payOrder.bankcod);
			request.setAttribute("version",version);
			request.setAttribute("merchantId",merchantId);
			request.setAttribute("merchantUrl",merchantUrl);
			request.setAttribute("responseMode",responseMode);
			request.setAttribute("orderId",orderId);
			request.setAttribute("currencyType",currencyType);
			request.setAttribute("amount",amount);
			request.setAttribute("assuredPay",assuredPay);
			request.setAttribute("time",time);
			request.setAttribute("remark",remark);
			request.setAttribute("mac",mac);
			request.setAttribute("bankCode",bankCode);
			String logStr = "version="+version+"&merchantId="+merchantId+"&merchantUrl="+merchantUrl+"&responseMode="+responseMode+
					"&orderId="+orderId+"&currencyType="+currencyType+"&amount="+amount+"&assuredPay="+assuredPay+"&remark="+remark+
					"&mac="+mac+"&bankCode="+bankCode;
			log.info("银生宝网关请求数据:"+logStr);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 渠道补单---网关
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		log.info("银生宝查单开始==================");
		try{
			String merchantId=PayConstant.PAY_CONFIG.get("YSB_MERCHANT_ID");
			String orderId = payOrder.payordno;
			String merchantKey = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_KEY");
			String signParams = "merchantId="+merchantId+"&orderId="+orderId+"&merchantKey="+merchantKey;
			log.info(signParams);
			String mac =MD5.getDigest(signParams).toUpperCase();
			Map<String, String> data = new HashMap<String, String>();
			data.put("merchantId", merchantId);
			data.put("orderId", orderId);
			data.put("mac", mac);
			String params = "";
			Iterator<String> it = data.keySet().iterator();
				while(it.hasNext()){
					Object key = it.next(); 
					params = params+key+"="+data.get(key)+ "&";
				}
				params = params.substring(0,params.length()-1); 
				String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("YSB_WG_PAY_QUERY_URL"), params.getBytes("utf-8")),"utf-8");
				log.info("银生宝支付查询返回数据:"+res);
				String [] resT = res.split("\\|");
				String returnCode = resT[3];
				String returnMessage = resT[4];
				if("0000".equals(returnCode)){
					String res_merchantId=resT[0];
					String res_orderId=resT[1];
					String res_amount=resT[2];
					String res_status=resT[5];
					String res_mac = resT[6];
					String res_signParms =  res_merchantId+"|"+res_orderId+"|"+res_amount+"|"+returnCode+"|"+returnMessage+"|"+res_status+"|"+merchantKey;
					String res_mac_temp =MD5.getDigest(res_signParms).toUpperCase();
					if(res_mac_temp.equals(res_mac)){
						if("3".equals(res_status)){
							payOrder.ordstatus="01";//支付成功
				        	new NotifyInterface().notifyMer(payOrder);//支付成功
						} else  throw new Exception("支付渠道状态："+ERROR_MESSAGE.get(res_status));
					} else throw new Exception("验签失败");
				} else throw new Exception(returnMessage);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 代扣请求入口
	 * @param payRequest
	 * @throws Exception
	 */
	 
    public void receivePaySingleInfo(PayRequest payRequest) throws Exception{
    	try {
    		PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
        	List<PayReceiveAndPay> AuthList = new ArrayList<PayReceiveAndPay>();
        	AuthList.add(rp);
        	//验证是否已经签约，如果已经签约，直接调用代扣接口，如果为签约，待用签约接口，并更新签约成功返回签约编码。
        	//本地签约检查，根据账号
        	PayReceiveAndPaySignDAO payReceiveAndPaySignDAO = new PayReceiveAndPaySignDAO();
    		Map<String,PayReceiveAndPaySign> signedSuccessMap = 
    			payReceiveAndPaySignDAO.getSignedSuccessRecord(AuthList,PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB"));//通过卡号查询是否已经签约。
    		PayReceiveAndPaySign payReceiveAndPaySign= new PayReceiveAndPaySign();
    		if(signedSuccessMap.get(AuthList.get(0).accNo)!=null){
    			//已经签约，检查签约是否到期。
    			payReceiveAndPaySign = signedSuccessMap.get(AuthList.get(0).accNo);
    			payReceiveAndPaySign.signProtocolChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB");
    			Date endDate =new SimpleDateFormat("yyyyMMdd").parse(payReceiveAndPaySign.endDate);//签约结束日期。
    			Date toDayDat = new SimpleDateFormat("yyyyMMdd").parse(new SimpleDateFormat("yyyyMMdd").format(new Date()));//当前日期。
    			//如果记录中的签约结束时间在当前时间之前，表示当前签约仍然在有效期内,否则需要重新签约。
    			if(toDayDat.before(endDate)){
    				//扣款--------开始-----
    				String res = receivePaySingle(payRequest,payReceiveAndPaySign);
    				JSONObject jsonObject = JSON.parseObject(res);
    				if("0000".equals(jsonObject.get("result_code"))){
    					payRequest.setRespInfo("000");
    					new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
    				} else {
    					AuthList.get(0).status="2";
    					AuthList.get(0).setRetCode("-1");
    					AuthList.get(0).errorMsg = jsonObject.getString("result_msg");
    	       			payRequest.receivePayRes = "-1";
    	       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
    				}
    			    //扣款--------结束-----
    			}else{
    				//签约--------开始-----
    				String resProtlcol = signProtocol(payRequest);
    				log.info(resProtlcol);
    				JSONObject jsonObject = JSON.parseObject(resProtlcol);
    				//解析签约结果0000：成功，2001:子协议已经签订，无需重复签订。
    				if(("0000".equals(jsonObject.get("result_code")))||("2001".equals(jsonObject.get("result_code")))){
    					//更新签约周期和签约编号。
    					payReceiveAndPaySign.protocolNo=jsonObject.get("subContractId").toString();
    					updatePayReceiveAndPaySign(payReceiveAndPaySign);
    				} else {
    					AuthList.get(0).status="2";
    					AuthList.get(0).setRetCode("-1");
    					AuthList.get(0).errorMsg = jsonObject.getString("result_msg");
    	       			payRequest.receivePayRes = "-1";
    	       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
    	       			throw new Exception("签约失败!");
    				}
    				//签约--------结束-----
    				
    				//扣款--------开始-----
    				String res = receivePaySingle( payRequest, payReceiveAndPaySign);
    				JSONObject jsonObject2 = JSON.parseObject(res);
    				if(("0000".equals(jsonObject2.get("result_code")))){
    					payRequest.setRespInfo("000");
    					new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
    				} else {
    					AuthList.get(0).status="2";
    					AuthList.get(0).setRetCode("-1");
    					AuthList.get(0).errorMsg = jsonObject2.getString("result_msg");
    	       			payRequest.receivePayRes = "-1";
    	       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
    				}
    			   //扣款--------结束-----
    			}
    		} else {
    			//签约--------开始-----
    			payReceiveAndPaySign.signProtocolChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB");
				String resProtlcol = signProtocol(payRequest);
				log.info(resProtlcol);
				JSONObject jsonObject = JSON.parseObject(resProtlcol);
				//解析签约结果。
				log.info(jsonObject.get("result_code"));
				//解析签约结果0000：成功，2001:子协议已经签订，无需重复签订。
				if(("0000".equals(jsonObject.get("result_code")))||("2001".equals(jsonObject.get("result_code")))){
					String newStartDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DATE,365);
					String newEndDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
					AuthList.get(0).beginDate=newStartDate;
					AuthList.get(0).endDate=newEndDate;
					AuthList.get(0).protocolNo=jsonObject.get("subContractId").toString();
					AuthList.get(0).status="1";
					AuthList.get(0).protocolNo=jsonObject.get("subContractId").toString();
					AuthList.get(0).signProtocolChannel=PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YSB");
					payReceiveAndPaySignDAO.addPayReceiveAndPaySignBatch(AuthList);
					payReceiveAndPaySign.protocolNo=jsonObject.get("subContractId").toString();
				}else {
					AuthList.get(0).status="2";
					AuthList.get(0).setRetCode("-1");
					AuthList.get(0).errorMsg = jsonObject.getString("result_msg");
	       			payRequest.receivePayRes = "-1";
	       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
	       			throw new Exception("签约失败!");
				}
				//签约--------结束-----
				
				//扣款--------开始-----
				String res = receivePaySingle(payRequest, payReceiveAndPaySign);
				JSONObject jsonObject2 = JSON.parseObject(res);
				if("0000".equals(jsonObject2.get("result_code"))){
					payRequest.setRespInfo("000");
					new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
				} else {
					AuthList.get(0).status="2";
					AuthList.get(0).setRetCode("-1");
					AuthList.get(0).errorMsg = jsonObject2.getString("result_msg");
	       			payRequest.receivePayRes = "-1";
	       			new PayReceiveAndPayDAO().updatePayReceiveAndPay(AuthList);
				}
			   //扣款--------结束-----
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	/**
	 * 执行协议签约(代扣使用)
	 * @param payRequest
	 * @return  {"result_code":"2001","result_msg":"子协议已经签订，无需重复签订","subContractId":"325895"}  |{"result_code":"0000","result_msg":"","subContractId":"325963"}  result_code=0000成功其他失败。
	 * @throws Exception
	 */
	 public String signProtocol(PayRequest payRequest) throws Exception{
		 try {
		 	String cardNo = payRequest.accNo;
			String name = payRequest.accName;
			String idCardNo= payRequest.credentialNo;
			String phoneNo= payRequest.tel;
			String startDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
    		Calendar c = Calendar.getInstance();
    		c.add(Calendar.DATE,100);
    		String endDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
			String contractId=PayConstant.PAY_CONFIG.get("YSB_MERCHANT_ID");
			String accountId = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_ID");
			String merchantKey = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_KEY");
			String signParams = "accountId="+accountId+"&contractId="+contractId+"&name="+name+"&phoneNo="+phoneNo+"&cardNo="+cardNo+
				"&idCardNo="+idCardNo+"&startDate="+startDate+"&endDate="+endDate+"&key="+merchantKey;
			log.info("银生宝协议签约待签名数据: "+signParams);
			String mac =MD5.getDigest(signParams).toUpperCase();
			Map<String, String> data = new HashMap<String, String>();
			data.put("accountId", accountId);
			data.put("contractId", contractId);
			data.put("name", name);
			data.put("phoneNo", phoneNo);
			data.put("cardNo", cardNo);
			data.put("idCardNo", idCardNo);
			data.put("startDate", startDate);
			data.put("endDate", endDate);
			data.put("mac", mac);
			String params = "";
			Iterator<String> it = data.keySet().iterator();
			while(it.hasNext()){
				Object key = it.next();
				params = params+key+"="+data.get(key)+ "&";
			}
			params = params.substring(0,params.length()-1);
			log.info("银生宝协议签约请求数据: "+params);
			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("YSB_DK_ZXY_URL"), params.getBytes("utf-8")),"utf-8");
			log.info("银生宝协议签约返回数据: "+res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	 }
	/**
	 * 执行单笔代扣
	 * @param payRequest
	 * @param receiveAndPaySingle
	 * @return {"result_code":"0000","result_msg":""} result_code=0000代扣请求成功,等待通知确定最终代扣状态。
	 * @throws Exception
	 */
	 public String receivePaySingle(PayRequest payRequest,PayReceiveAndPaySign payReceiveAndPaySign) throws Exception{
		 try {
		 	String purpose = "receivePaySingle";
			String amount = String.format("%.2f", Double.parseDouble(payRequest.amount)/100d);
			String subContractId = payReceiveAndPaySign.protocolNo;//已经签约的子协议编号。
			String accountId = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_ID");//商户编号。
			String orderId = payRequest.receiveAndPaySingle.id;//订单号。
			String responseUrl = PayConstant.PAY_CONFIG.get("YSB_DK_NITIFY_URL");//异步通知路劲。
			String merchantKey =PayConstant.PAY_CONFIG.get("YSB_MERCHANT_KEY");
			String signParams = "accountId="+accountId+"&subContractId="+subContractId+"&orderId="+orderId+"&purpose="+purpose+"&amount="+amount+
								"&responseUrl="+responseUrl+"&key="+merchantKey;
			log.info("银生宝代扣签名参数: "+signParams);
			String mac =MD5.getDigest(signParams).toUpperCase();
			Map<String, String> data = new HashMap<String, String>();
			data.put("accountId", accountId);
			data.put("subContractId", subContractId);
			data.put("orderId", orderId);
			data.put("purpose", purpose);
			data.put("amount", amount);
			data.put("responseUrl", responseUrl);
			data.put("mac", mac);
			String params = "";
			Iterator<String> it = data.keySet().iterator();
			while(it.hasNext()){
				Object key = it.next();
				params = params+key+"="+data.get(key)+ "&";
			}
			params = params.substring(0,params.length()-1); 
			log.info("银生宝代扣请求参数: "+params);
			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("YSB_DK_URL"), params.getBytes("utf-8")),"utf-8");
			log.info("银生宝代扣返回参数: "+res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 代扣查询
	 * @param payOrder
	 * @throws Exception
	 */
	 public boolean receivePaySingleQuery(PayRequest payRequest,PayReceiveAndPay rp) throws Exception{
		try {
			String accountId = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_ID");//商户编号。
			String orderId = payRequest.receiveAndPaySingle.id;
			String merchantKey = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_KEY");
			String signParams = "accountId="+accountId+"&orderId="+orderId+"&key="+merchantKey;
			log.info("银生宝代扣查询签名参数: "+signParams);
			String mac =MD5.getDigest(signParams).toUpperCase();
			Map<String, String> data = new HashMap<String, String>();
			data.put("accountId", accountId);
			data.put("orderId", orderId);
			data.put("mac", mac);
			String params = "";
			Iterator<String> it = data.keySet().iterator();
				while(it.hasNext()){
					Object key = it.next();
					params = params+key+"="+data.get(key)+ "&";
				}
			log.info("银生宝代扣查询请求数据: "+params);
			params = params.substring(0,params.length()-1); 
			//{"result_code":"0000","desc":"交易成功","result_msg":"查询成功","status":"00"}
			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("YSB_DK_QUERY_URL"), params.getBytes("utf-8")),"utf-8");
			log.info("银生宝代扣查询返回数据: "+res);
			JSONObject jsonObject = JSON.parseObject(res);
			if("0000".equals(jsonObject.get("result_code"))){
				if("00".equals(jsonObject.get("status"))){
					payRequest.setRespCode("000");
					payRequest.receiveAndPaySingle.setRespCode("000");
					payRequest.receivePayRes = "0";
					rp.errorMsg = "交易成功";
					return true;
				} else {
					payRequest.setRespCode("-1");
					rp.setRespCode(payRequest.respCode);
					payRequest.receivePayRes = "-1";
					payRequest.respDesc=jsonObject.getString("desc");
					rp.errorMsg = payRequest.respDesc;
					return true;
				}
			} else {
				payRequest.setRespCode("-1");
				rp.setRespCode(payRequest.respCode);
				payRequest.receivePayRes = "-1";
				payRequest.respDesc=jsonObject.getString("result_msg");
				rp.errorMsg = payRequest.respDesc;
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			payRequest.setRespCode("-1");
			rp.setRespCode(payRequest.respCode);
			payRequest.receivePayRes = "-1";
			payRequest.respDesc="银生宝渠道异常";
			rp.errorMsg = payRequest.respDesc;
			return false;
		}
	}
    /**
     * 代收包装快捷补单
     * @param payOrder
     * @param rp
     * @throws Exception
     */
    public void tranQueryForRepair(PayOrder payOrder,PayReceiveAndPay rp) throws Exception{
    	try {
			String accountId = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_ID");//商户编号。
			String merchantKey = PayConstant.PAY_CONFIG.get("YSB_MERCHANT_KEY");
			String signParams = "accountId="+accountId+"&orderId="+payOrder.payordno+"&key="+merchantKey;
			log.info("银生宝代扣查询签名参数: "+signParams);
			String mac =MD5.getDigest(signParams).toUpperCase();
			Map<String, String> data = new HashMap<String, String>();
			data.put("accountId", accountId);
			data.put("orderId", payOrder.payordno);
			data.put("mac", mac);
			String params = "";
			Iterator<String> it = data.keySet().iterator();
				while(it.hasNext()){
					Object key = it.next();
					params = params+key+"="+data.get(key)+ "&";
				}
			log.info("银生宝代扣查询请求数据: "+params);
			params = params.substring(0,params.length()-1); 
			//{"result_code":"0000","desc":"交易成功","result_msg":"查询成功","status":"00"}
			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("YSB_DK_QUERY_URL"), params.getBytes("utf-8")),"utf-8");
			log.info("银生宝代扣查询返回数据: "+res);
			JSONObject jsonObject = JSON.parseObject(res);
			if("0000".equals(jsonObject.get("result_code"))){
				if("00".equals(jsonObject.get("status"))){
					//更新代收信息
					rp.status="1";
					rp.errorMsg = "";
		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					payOrder.ordstatus="01";//支付成功
				} else {
					rp.status="2";
					rp.errorMsg = jsonObject.getString("desc");
					payOrder.ordstatus="02";
					payOrder.bankerror=rp.errorMsg;
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
				}
			} else {
				rp.status="2";
				rp.errorMsg = jsonObject.getString("result_msg");
				payOrder.ordstatus="02";
				payOrder.bankerror=rp.errorMsg;
				try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("银生宝代扣补单异常："+e.getMessage());
		}
    }
	 /**
	  * 网关---退款。
	  * @param payRefund
	  */
	public void refund(PayRefund payRefund){
		try{
    		if("0000".equals(""))payRefund.banksts="01";//退款成功
    		else {
    			payRefund.banksts="02";
    			payRefund.bankerror="";
    			payRefund.rfsake="";
    		}
		} catch (Exception e) {
			e.printStackTrace();
			//订单状态，00:退款处理中  01:退款成功 02:退款失败 ，默认00
			payRefund.banksts="02";
			payRefund.bankerror = e.getMessage();
			payRefund.rfsake = e.getMessage();
		}
	}
	/**
	 * 更新签约结果。
	  * @param payReceiveAndPaySign 
	  * @throws Exception
	 */
	 public void updatePayReceiveAndPaySign(PayReceiveAndPaySign payReceiveAndPaySign)throws Exception{
	    	//更新签约日期和签约返回的编码到  PAY_RECEIVE_AND_PAY_SIGN表中。
			String newStartDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE,365);
			String newEndDate = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
			payReceiveAndPaySign.beginDate=newStartDate;//签约开始时间。
			payReceiveAndPaySign.endDate =newEndDate;//签约结束时间
			new PayReceiveAndPaySignDAO().updatePayReceiveAndPaySign(payReceiveAndPaySign);
	    }
}
