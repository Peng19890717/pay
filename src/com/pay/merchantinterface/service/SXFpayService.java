package com.pay.merchantinterface.service;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.PayUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.sxf.Base64Utils;
import com.third.sxf.DESUtils;
import com.third.sxf.DF1003Request;
import com.third.sxf.HttpClientUtil;
import com.third.sxf.HttpRequest;
import com.third.sxf.JsonUtils;
import com.third.sxf.PaymentUtils;
import com.third.sxf.RSAUtils;
import com.third.sxf.RequestMessage;

/**
 *随行付接口服务类
 * @author Administrator
 *
 */
public class SXFpayService {
	private static final Log log = LogFactory.getLog(SXFpayService.class);
	public static Map<String,String> BANK_MAP_B2C = new HashMap<String,String>();//b2c
	public static Map<String,String> BANK_MAP_PAYRECEIVE_AND_PAY = new HashMap<String,String>();//代付
	static{
		BANK_MAP_B2C.put("PSBC","PSBC");//邮政储蓄银行
		BANK_MAP_B2C.put("CMBC","CMBC");//民生银行
		BANK_MAP_B2C.put("BCCB","BOB");//北京银行
		//BANK_MAP_B2C.put("BOS","1025");//上海银行
		BANK_MAP_B2C.put("CMB","CMB");//招商银行
		BANK_MAP_B2C.put("CNCB","ECITIC");//中信银行
		BANK_MAP_B2C.put("SPDB","SPDB");//浦发银行
		BANK_MAP_B2C.put("CIB","CEB");//兴业银行
		//BANK_MAP_B2C.put("HXB","1009");//华夏银行
		BANK_MAP_B2C.put("ABC","ABC");//农业银行
		BANK_MAP_B2C.put("GDB","CGB");//广发银行
		BANK_MAP_B2C.put("ICBC","ICBC");//工商银行
		BANK_MAP_B2C.put("BOC","BOC");//中国银行
		BANK_MAP_B2C.put("BOCOM","BOCOM");//交通银行
		BANK_MAP_B2C.put("CCB","CCB");//建设银行
		BANK_MAP_B2C.put("PAB","PAB");//平安银行
		BANK_MAP_B2C.put("CEB","CEB");//光大银行
		
		//代付支持银行（全时）
		BANK_MAP_PAYRECEIVE_AND_PAY.put("ICBC", "102");//中国工商银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("ABC", "103");//中国农业银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("BOC", "104");//中国银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("CCB", "105");//中国建设银行
//		BANK_MAP_PAYRECEIVE_AND_PAY.put("", "203");//中国农业发展银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("BOCOM", "301");//交通银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("CNCB", "302");//中信银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("CEB", "303");//中国光大银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("HXB", "304");//华夏银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("CMBC", "305");//中国民生银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("GDB", "306");//广发银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("PAB", "307");//平安银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("CMB", "308");//招商银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("CIB", "309");//兴业银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("SPDB", "310");//上海浦东发展银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("ZSBC", "316");//浙商银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("BOHC", "318");//渤海银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("PSBC", "403");//中国邮政储蓄银行
		//代付支持银行（工作日）
//		BANK_MAP_PAYRECEIVE_AND_PAY.put("", "313");//城市商业银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("GNXS", "314");//农村商业银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("EGBANK", "315");//恒丰银行
//		BANK_MAP_PAYRECEIVE_AND_PAY.put("", "317");//农村合作银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("HSBANK", "319");//徽商银行
//		BANK_MAP_PAYRECEIVE_AND_PAY.put("", "320");//村镇银行
		BANK_MAP_PAYRECEIVE_AND_PAY.put("CCQTGB", "321");//重庆三峡银行
//		BANK_MAP_PAYRECEIVE_AND_PAY.put("", "322");;//上海农村商业银行
//		BANK_MAP_PAYRECEIVE_AND_PAY.put("", "785");//华商银行
//		BANK_MAP_PAYRECEIVE_AND_PAY.put("", "788");//农村信用社（含北京农村商业银行）
//		BANK_MAP_PAYRECEIVE_AND_PAY.put("", "888");//外资银行
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request,PayProductOrder prdOrder,PayOrder payOrder){
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXF"); // 支付渠道
			payOrder.bankcod = request.getParameter("banks"); // 银行代码
			payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			
			// 获取参数
			String mercNo = PayConstant.PAY_CONFIG.get("SXF_MERNO");
			String tranCd = "1001";
			String version = "1.0";
			String ip =PayConstant.PAY_CONFIG.get("SXF_IP") ;
			String encodeType = "RSA#RSA";
			String type="1";//页面返回。
			net.sf.json.JSONObject json = new net.sf.json.JSONObject();
			json.put("orderNo", payOrder.payordno);
			json.put("tranAmt",String.format("%.2f", ((double)payOrder.txamt)/100d) );
			json.put("ccy", "CNY");
			json.put("pname", "payOnline");
			json.put("pnum", "1");
			json.put("pdesc", "payOnline");
			json.put("retUrl", PayConstant.PAY_CONFIG.get("SXF_RETURL"));
			json.put("notifyUrl", PayConstant.PAY_CONFIG.get("SXF_NOTIFYURL"));
			json.put("bankWay", BANK_MAP_B2C.get(payOrder.bankcod));
			json.put("period", "120");
			json.put("desc", "payOnline");
			json.put("userId", payOrder.merno);
			json.put("ext", "123");
			//以下为快捷支付直连需要传入参数
			json.put("bankCardNo","");
			json.put("cvv", "");
			json.put("valid","");
			json.put("accountName", "");
			json.put("certificateNo", "");
			json.put("mobilePhone", "");
			// 利用工具类加密
			String mercPrivateKey=PayConstant.PAY_CONFIG.get("SXF_MERCPRIVATEKEY");
			String sxfPublic=PayConstant.PAY_CONFIG.get("SXF_SXFPUBLIC");
			String data=PaymentUtils.encrypt(json.toString(), sxfPublic);
			net.sf.json.JSONObject main = new net.sf.json.JSONObject();
			main.put("mercNo", mercNo);
			main.put("tranCd", tranCd);
			main.put("version", version);
			main.put("reqData", data);
			main.put("ip", ip);
			// 加签名，注意参数顺序
			String sign = PaymentUtils.sign(main.toString(), mercPrivateKey);
			request.setAttribute("mercNo", mercNo);
			request.setAttribute("tranCd", tranCd);
			request.setAttribute("version", version);
			request.setAttribute("reqData", data);
			request.setAttribute("ip", ip);
			request.setAttribute("sign", sign);
			request.setAttribute("encodeType", encodeType);
			request.setAttribute("type", type);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
			e.printStackTrace();
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
	public void pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) {
		try{
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXF"); // 支付渠道
			payOrder.bankcod = payRequest.bankId;  //银行代码
			payOrder.bankCardType = payRequest.accountType;
			// 获取参数
			String mercNo = PayConstant.PAY_CONFIG.get("SXF_MERNO");
			String tranCd = "1001";
			String version = "1.0";
			String ip =PayConstant.PAY_CONFIG.get("SXF_IP") ;
			String encodeType = "RSA#RSA";
			String type="1";//页面返回。
			net.sf.json.JSONObject json = new net.sf.json.JSONObject();
			json.put("orderNo", payOrder.payordno);
			json.put("tranAmt",String.format("%.2f", ((double)payOrder.txamt)/100d) );
			json.put("ccy", "CNY");
			json.put("pname", "payOnline");
			json.put("pnum", "1");
			json.put("pdesc", "payOnline");
			json.put("retUrl", PayConstant.PAY_CONFIG.get("SXF_RETURL"));
			json.put("notifyUrl", PayConstant.PAY_CONFIG.get("SXF_NOTIFYURL"));
			json.put("bankWay", BANK_MAP_B2C.get(payOrder.bankcod));
			json.put("period", "120");
			json.put("desc", "payOnline");
			json.put("userId", payOrder.merno);
			json.put("ext", "123");
			//以下为快捷支付直连需要传入参数
			json.put("bankCardNo","");
			json.put("cvv", "");
			json.put("valid","");
			json.put("accountName", "");
			json.put("certificateNo", "");
			json.put("mobilePhone", "");
			// 利用工具类加密
			String mercPrivateKey=PayConstant.PAY_CONFIG.get("SXF_MERCPRIVATEKEY");
			String sxfPublic=PayConstant.PAY_CONFIG.get("SXF_SXFPUBLIC");
			String data=PaymentUtils.encrypt(json.toString(), sxfPublic);
			net.sf.json.JSONObject main = new net.sf.json.JSONObject();
			main.put("mercNo", mercNo);
			main.put("tranCd", tranCd);
			main.put("version", version);
			main.put("reqData", data);
			main.put("ip", ip);
			// 加签名，注意参数顺序
			String sign = PaymentUtils.sign(main.toString(), mercPrivateKey);
			request.setAttribute("mercNo", mercNo);
			request.setAttribute("tranCd", tranCd);
			request.setAttribute("version", version);
			request.setAttribute("reqData", data);
			request.setAttribute("ip", ip);
			request.setAttribute("sign", sign);
			request.setAttribute("encodeType", encodeType);
			request.setAttribute("type", type);
			new PayOrderService().updateOrderForBanks(payOrder);
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获 
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			// 获取参数
			String mercNo = PayConstant.PAY_CONFIG.get("SXF_MERNO");
			String tranCd ="1002";
			String version = "1.0";
			String ip = PayConstant.PAY_CONFIG.get("SXF_IP");
			net.sf.json.JSONObject json = new net.sf.json.JSONObject();
			json.put("orderNo", payOrder.payordno);
			// 利用工具类加密
			String encodeType="RSA#RSA";
			String mercPrivateKey=PayConstant.PAY_CONFIG.get("SXF_MERCPRIVATEKEY");
			String sxfPublic=PayConstant.PAY_CONFIG.get("SXF_SXFPUBLIC");
			//进行加密
			String data=PaymentUtils.encrypt(json.toString(), sxfPublic);
			net.sf.json.JSONObject main = new net.sf.json.JSONObject();
			main.put("mercNo", mercNo);
			main.put("tranCd", tranCd);
			main.put("version", version);
			main.put("reqData", data);
			main.put("ip", ip);
			// 加签名，注意参数顺序
			String sign = PaymentUtils.sign(main.toString(), mercPrivateKey);
			main.put("sign", sign);
			main.put("encodeType", encodeType);
			String _t=main.toString();
			String sendData ="_t="+URLEncoder.encode(_t,"utf-8");
			log.info("随行付网银查询请求参数:"+_t);
			String payResult = HttpRequest.sendPost(PayConstant.PAY_CONFIG.get("SXF_PAYQUERYURL"), sendData);
			log.info("随行付网银查询响应参数:"+payResult);
			JSONObject jsonObject = JSON.parseObject(payResult);
			if("000000".equals(jsonObject.getString("resCode"))){
				String resData=PaymentUtils.decrypt(jsonObject.getString("resData"),mercPrivateKey);
				log.info("随行付网银查询响应业务参数:"+resData);
				JSONObject jsonObject2 = JSON.parseObject(resData);
				if("S".equals(jsonObject2.getString("tranSts"))){
					payOrder.ordstatus="01";//支付成功
					new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("F".equals(jsonObject2.getString("tranSts"))){
					payOrder.ordstatus="02";//支付失败
					 new NotifyInterface().notifyMer(payOrder);//支付失败
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 单笔代付。
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingle(PayRequest payRequest) throws Exception{
		 try {
		 	PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
		 	PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
		 	DF1003Request df3 = new DF1003Request();
			df3.setPayTyp("01");
			df3.setTotalPayAmt(String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d));
			df3.setTotalPayCount("1");
			List<DF1003Request.PayItems> piList = new ArrayList<DF1003Request.PayItems>();
			
			DF1003Request.PayItems pi = new DF1003Request.PayItems();
			pi.setPayItemId(rp.channelTranNo);
			log.info("随行付付款明细数据id="+pi.getPayItemId());
			pi.setSeqNo("1");
			pi.setPayAmt(String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d));
			if("0".equals(payRequest.accountProp)){
				pi.setActTyp("01");//账户类型， 00：对公    01：对私。
			}else{
				pi.setActTyp("00");//账户类型， 00：对公    01：对私。
			}
			pi.setActNm(rp.accountName);
			pi.setActNo(rp.accountNo);
			pi.setBnkCd(BANK_MAP_PAYRECEIVE_AND_PAY.get(cardBin.bankCode));
			pi.setBnkNm(cardBin.bankName);
			piList.add(pi);
			df3.setPayItems(piList);
			String json=JsonUtils.toJson(df3);
			
			RequestMessage rm = new RequestMessage();
			rm.setClientId(PayConstant.PAY_CONFIG.get("SXF_MERNO"));
			rm.setReqId(String.valueOf(System.currentTimeMillis()));
			rm.setTranCd("DF1003");
			rm.setVersion("0.0.0.1");
			byte[] bs = DESUtils.encrypt(json.getBytes("UTF-8"), PayConstant.PAY_CONFIG.get("SXF_DES_PWD"));
			//Base64编码
			String reqDataEncrypt = Base64Utils.encode(bs);
			rm.setReqData(reqDataEncrypt);
			rm.setSign(RSAUtils.sign(reqDataEncrypt, PayConstant.PAY_CONFIG.get("SXF_MERCPRIVATEKEY")));
			String reqStr = JsonUtils.toJson(rm);
			log.info("随行付单笔代付请求数据:"+reqStr);
			String body = HttpClientUtil.doPost(PayConstant.PAY_CONFIG.get("SXF_PAYRECEIVEANDPAY"),reqStr);
			log.info("随行付单笔代付响应数据:"+body);
			RequestMessage requestMessage = JsonUtils.fromJson(body,RequestMessage.class);
			String sign = requestMessage.getSign();
			String resData = requestMessage.getResData();
			if("000000".equals(requestMessage.getResCode())){
				if(RSAUtils.verify(resData, sign, PayConstant.PAY_CONFIG.get("SXF_SXFPUBLIC"))){//验签成功
					byte[] decodeResData = Base64Utils.decode(resData);
					byte[] decrypt = DESUtils.decrypt(decodeResData, PayConstant.PAY_CONFIG.get("SXF_DES_PWD"));
					JSONObject parseObject = JSONObject.parseObject(new  String(decrypt,"utf-8"));
					String jsonStr = parseObject.getString("payResultList");
					JSONArray jsonArray = new JSONArray();
					JSONArray parseArray = jsonArray.parseArray(jsonStr);
					JSONObject jsonObject = (JSONObject) parseArray.get(0);
					if("00".equals(jsonObject.getString("resCd"))){
						rp.status="0";
						rp.errorMsg = "操作成功";
			        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			        	SXFQueryThread sxfquerythread =new SXFQueryThread(payRequest);
			        	sxfquerythread.start();
					} else {
						rp.status="2";
						rp.errorMsg = "操作失败";
			        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					}
				} else{
					throw new Exception("随行付验签失败");
				}
			} else {
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
}

class SXFQueryThread extends Thread{
	private static final Log log = LogFactory.getLog(SXFQueryThread.class);
	private  PayRequest payRequest= new PayRequest();
	public SXFQueryThread(){};
	public SXFQueryThread(PayRequest payRequest){
		this.payRequest=payRequest;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("随行付代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query()throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
			net.sf.json.JSONObject json = new net.sf.json.JSONObject();
			json.put("clientId", PayConstant.PAY_CONFIG.get("SXF_MERNO"));
			json.put("reqId", String.valueOf(System.currentTimeMillis()));
			json.put("tranCd", "DF1004");
			json.put("version", "0.0.0.1");
			net.sf.json.JSONObject reqData = new net.sf.json.JSONObject();
			reqData.put("payItemId", rp.channelTranNo);
			
			byte[] bs = DESUtils.encrypt(reqData.toString().getBytes("UTF-8"), PayConstant.PAY_CONFIG.get("SXF_DES_PWD"));
			//Base64编码
			String reqDataEncrypt = Base64Utils.encode(bs);
			json.put("reqData", reqDataEncrypt);
			json.put("sign", RSAUtils.sign(reqDataEncrypt, PayConstant.PAY_CONFIG.get("SXF_MERCPRIVATEKEY")));
			log.info("随行付单笔代付结果查询请求数据："+json.toString());
			String body = HttpClientUtil.doPost(PayConstant.PAY_CONFIG.get("SXF_PAYREANDPAYSINGLEQUERY"),json.toString());
			log.info("随行付单笔代付结果查询响应数据："+body);
			/**
			 * 随行付单笔代付结果查询响应数据：{"clientId":"600000000000071","reqId":"1498627312314","resCode":"000000",
			 * "resData":"H1LO43pA6fQ8Etdf8uhviXvwBSvUC49CrdKi7yhd/ytu+MDwAUk7XJxdO26uybiN8gC2lSo1IZZdiAAqbFP52cqyE7v8+cVg/rlZt9RkL8s=",
			 * "resMsg":"成功","serverId":"000000","sign":"hMWNitfI6bHRSXhA/jecrPM1SKswHzif+ls8i+zQHCkW1T/8uHfr9iTdLBmaEthxvAlgPY9jn1w/fGzMST/KuDtGk079Hh97cnLT6bw+bdBJ8+5WJPPMp3hzSh5V5zgjh5/YSVFd1ypKrRVwNPgK36Sawr+EylpU7ubRn3W6FYg=",
			 * "tranCd":"DF1004","version":"0.0.0.1"}
			 */
			RequestMessage requestMessage = JsonUtils.fromJson(body,RequestMessage.class);
			String sign = requestMessage.getSign();
			String resData = requestMessage.getResData();
			if("000000".equals(requestMessage.getResCode())){
				if(RSAUtils.verify(resData, sign, PayConstant.PAY_CONFIG.get("SXF_SXFPUBLIC"))){//验签成功
					byte[] decodeResData = Base64Utils.decode(resData);
					byte[] decrypt = DESUtils.decrypt(decodeResData, PayConstant.PAY_CONFIG.get("SXF_DES_PWD"));
					JSONObject parseObject = JSONObject.parseObject(new  String(decrypt,"utf-8"));
					log.info(parseObject);
					//{"payItemId":"1234561231234567885","tranMsg":"处理中","tranSts":"01"}
					if("00".equals(parseObject.getString("tranSts"))){
						rp.status="1";
						rp.respCode="000";
						rp.retCode="000";
						rp.errorMsg="交易成功";
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
						list.add(rp);
						new PayInterfaceOtherService().receivePayNotify(payRequest,list);
						return true;
					} else if("01".equals(parseObject.getString("tranSts"))){
						return false;	
					} else {
						rp.status="2";
						rp.respCode="-1";
						rp.retCode="-1";
						rp.errorMsg = parseObject.getString("tranMsg");
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						return true;
					}
				} else {
					throw new Exception("随行付单笔代付结果查询验签失败");
				}
			} else return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}