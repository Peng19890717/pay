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
 *随行付(new)接口服务类
 * @author Administrator
 *
 */
public class SXFNEWpayService {
	private static final Log log = LogFactory.getLog(SXFNEWpayService.class);
	public static Map<String,String> BANK_MAP_B2C = new HashMap<String,String>();//b2c
	static{
		/*
		 * 网银支持银行：
		 * 工商银行、农业银行、 中国银行 、交通银行、 招商银行 、邮政银行 、光大银行 、
		 * 中信银行、 浦发银行 、平安银行、 北京银行
		 */
		BANK_MAP_B2C.put("ICBC","ICBC");//工商银行
		BANK_MAP_B2C.put("ABC","ABC");//农业银行
		BANK_MAP_B2C.put("BOC","BOC");//中国银行
		BANK_MAP_B2C.put("BOCOM","BOCOM");//交通银行
		BANK_MAP_B2C.put("CMB","CMB");//招商银行
		BANK_MAP_B2C.put("PSBC","PSBC");//邮政储蓄银行
		BANK_MAP_B2C.put("CEB","CEB");//光大银行
		BANK_MAP_B2C.put("CNCB","ECITIC");//中信银行
		BANK_MAP_B2C.put("SPDB","SPDB");//浦发银行
		BANK_MAP_B2C.put("PAB","PAB");//平安银行
		BANK_MAP_B2C.put("BCCB","BOB");//北京银行
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
			payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXFNEW"); // 支付渠道
			payOrder.bankcod = request.getParameter("banks"); // 银行代码
			payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) bankCode = BANK_MAP_B2C.get(payOrder.bankcod);
			if (bankCode == null) throw new Exception("新随行付：无对应银行（"+payOrder.bankcod+"）");
			// 请求参数
			String mercNo = PayConstant.PAY_CONFIG.get("SXFNEW_MERNO");
			String tranCd = "1001";//交易码
			String version = "1.0";//版本号
			String ip = "" ;//服务器ip
			String encodeType = "RSA#RSA";//加密方式
			String type="1";//页面返回。
			String orderNo = payOrder.payordno;//订单号
			String tranAmt = String.format("%.2f", ((double)payOrder.txamt)/100d);//金额
			String ccy = "CNY";//币种
			String pname = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称 
			String pnum = "1";//商品数量
			String pdesc = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String retUrl = PayConstant.PAY_CONFIG.get("SXFNEW_RETURL");//支付完成后重定向地址
			String notifyUrl = PayConstant.PAY_CONFIG.get("SXFNEW_NOTIFYURL");//服务器通知地址
			String bankWay = bankCode;//银行代码
			String payWay = "2";//支付方式，直连
			String payChannel = "1";//支付渠道
			String userId = payOrder.merno;//商户用户id
			
			net.sf.json.JSONObject json = new net.sf.json.JSONObject();
			json.put("orderNo", orderNo);
			json.put("tranAmt",tranAmt);
			json.put("ccy", ccy);
			json.put("pname", pname);
			json.put("pnum", pnum);
			json.put("pdesc", pdesc);
			json.put("retUrl", retUrl);
			json.put("notifyUrl", notifyUrl);
			json.put("bankWay", bankWay);
			json.put("payWay", payWay);
			json.put("payChannel", payChannel);
			json.put("userId", userId);
			
			// 利用工具类加密
			String mercPrivateKey=PayConstant.PAY_CONFIG.get("SXFNEW_MERCPRIVATEKEY");
			String sxfPublic=PayConstant.PAY_CONFIG.get("SXFNEW_SXFPUBLIC");
			String data=PaymentUtils.encrypt(json.toString(), sxfPublic);
			net.sf.json.JSONObject main = new net.sf.json.JSONObject();
			main.put("mercNo", mercNo);
			main.put("tranCd", tranCd);
			main.put("version", version);
			main.put("reqData", data);
			main.put("ip", ip);
			// 签名，注意参数顺序
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
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_SXFNEW"); // 支付渠道
		payOrder.bankcod = payRequest.bankId;  //银行代码
		payOrder.bankCardType = payRequest.accountType;//卡类型
		try{
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) bankCode = BANK_MAP_B2C.get(payOrder.bankcod);
			if (bankCode == null) throw new Exception("新随行付：无对应银行（"+payOrder.bankcod+"）");
			// 请求参数
			String mercNo = PayConstant.PAY_CONFIG.get("SXFNEW_MERNO");
			String tranCd = "1001";//交易码
			String version = "1.0";//版本号
			String ip = "" ;//服务器ip
			String encodeType = "RSA#RSA";//加密方式
			String type="1";//页面返回。
			String orderNo = payOrder.payordno;//订单号
			String tranAmt = String.format("%.2f", ((double)payOrder.txamt)/100d);//金额
			String ccy = "CNY";//币种
			String pname = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称 
			String pnum = "1";//商品数量
			String pdesc = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String retUrl = PayConstant.PAY_CONFIG.get("SXFNEW_RETURL");//支付完成后重定向地址
			String notifyUrl = PayConstant.PAY_CONFIG.get("SXFNEW_NOTIFYURL");//服务器通知地址
			String bankWay = bankCode;//银行代码
			String payWay = "2";//支付方式，直连
			String payChannel = "1";//支付渠道
			String userId = payOrder.merno;//商户用户id
			
			net.sf.json.JSONObject json = new net.sf.json.JSONObject();
			json.put("orderNo", orderNo);
			json.put("tranAmt",tranAmt);
			json.put("ccy", ccy);
			json.put("pname", pname);
			json.put("pnum", pnum);
			json.put("pdesc", pdesc);
			json.put("retUrl", retUrl);
			json.put("notifyUrl", notifyUrl);
			json.put("bankWay", bankWay);
			json.put("payWay", payWay);
			json.put("payChannel", payChannel);
			json.put("userId", userId);
			
			// 利用工具类加密
			String mercPrivateKey=PayConstant.PAY_CONFIG.get("SXFNEW_MERCPRIVATEKEY");
			String sxfPublic=PayConstant.PAY_CONFIG.get("SXFNEW_SXFPUBLIC");
			String data=PaymentUtils.encrypt(json.toString(), sxfPublic);
			net.sf.json.JSONObject main = new net.sf.json.JSONObject();
			main.put("mercNo", mercNo);
			main.put("tranCd", tranCd);
			main.put("version", version);
			main.put("reqData", data);
			main.put("ip", ip);
			// 签名，注意参数顺序
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
	 * 渠道补单(网银)
	 * @param payordno 订单号
	 * @throws Exception 异常捕获 
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			// 获取参数
			String mercNo = PayConstant.PAY_CONFIG.get("SXFNEW_MERNO");//商户号
			String tranCd ="1002";//交易类型
			String version = "1.0";//版本号
			net.sf.json.JSONObject json = new net.sf.json.JSONObject();
			json.put("orderNo", payOrder.payordno);
			// 利用工具类加密
			String encodeType="RSA#RSA";
			String mercPrivateKey=PayConstant.PAY_CONFIG.get("SXFNEW_MERCPRIVATEKEY");
			String sxfPublic=PayConstant.PAY_CONFIG.get("SXFNEW_SXFPUBLIC");
			//进行加密
			String data=PaymentUtils.encrypt(json.toString(), sxfPublic);
			net.sf.json.JSONObject main = new net.sf.json.JSONObject();
			main.put("mercNo", mercNo);
			main.put("tranCd", tranCd);
			main.put("version", version);
			main.put("reqData", data);
			main.put("ip", "");
			// 加签名，注意参数顺序
			String sign = PaymentUtils.sign(main.toString(), mercPrivateKey);
			main.put("sign", sign);
			main.put("encodeType", encodeType);
			String _t=main.toString();
			String sendData ="_t="+URLEncoder.encode(_t,"utf-8");
			log.info("新随行付：网银查询请求参数:"+_t);
			String payResult = HttpRequest.sendPost(PayConstant.PAY_CONFIG.get("SXFNEW_QUERY_URL"), sendData);
			log.info("新随行付：网银查询响应参数:"+payResult);
			JSONObject jsonObject = JSON.parseObject(payResult);
			if("000000".equals(jsonObject.getString("resCode"))){
				String resData=PaymentUtils.decrypt(jsonObject.getString("resData"),mercPrivateKey);
				log.info("新随行付：网银查询响应业务参数:"+resData);
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
			if(cardBin == null) throw new Exception("无法识别银行账号");
		 	
		 	//请求参数
		 	String payTyp = "01";//代付类型，全时代付
		 	String totalPayAmt = String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d);
		 	String totalPayCount = "1";//总笔数
		 	DF1003Request df3 = new DF1003Request();
			df3.setPayTyp(payTyp);
			df3.setTotalPayAmt(totalPayAmt);
			df3.setTotalPayCount(totalPayCount);
			List<DF1003Request.PayItems> piList = new ArrayList<DF1003Request.PayItems>();
			
			DF1003Request.PayItems pi = new DF1003Request.PayItems();
			String payItemId = rp.channelTranNo.replace("_", "");// 付款明细id，固定19位
			rp.channelTranNo = payItemId;
			String seqNo = "1";//本条数据在本批次数据编号，从1开始
			String payAmt = String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d);
			String actNm = rp.accountName;//收款账户名
			String actNo = rp.accountNo;//卡号
			String actTyp = "01";//账户类型
			String bnkNm = cardBin.bankName;//银行名称
			pi.setPayItemId(payItemId);
			pi.setSeqNo(seqNo);
			pi.setPayAmt(payAmt);
			if("0".equals(payRequest.accountProp)){
				actTyp = "01";//账户类型， 00：对公    01：对私。
			}else{
				actTyp = "00";//账户类型， 00：对公    01：对私。
			}
			pi.setActTyp(actTyp);
			pi.setActNm(actNm);
			pi.setActNo(actNo);
			pi.setBnkNm(bnkNm);
			piList.add(pi);
			df3.setPayItems(piList);
			String json=JsonUtils.toJson(df3);
			
			RequestMessage rm = new RequestMessage();
		 	String clientId = PayConstant.PAY_CONFIG.get("SXFNEW_MERNO");//客户号
		 	String reqId = System.currentTimeMillis()+"";//请求序列号
		 	String tranCd = "DF1003";//报文交易码
		 	String version = "0.0.0.1";//版本号
			rm.setClientId(clientId);
			rm.setReqId(reqId);
			rm.setTranCd(tranCd);
			rm.setVersion(version);
			byte[] bs = DESUtils.encrypt(json.getBytes("UTF-8"), PayConstant.PAY_CONFIG.get("SXFNEW_DES_PWD"));
			//Base64编码
			String reqDataEncrypt = Base64Utils.encode(bs);
			rm.setReqData(reqDataEncrypt);
			rm.setSign(RSAUtils.sign(reqDataEncrypt, PayConstant.PAY_CONFIG.get("SXFNEW_DF_PRIKEY")));
			String reqStr = JsonUtils.toJson(rm);
			log.info("新随行付：单笔代付请求数据:"+json);
			String body = HttpClientUtil.doPost(PayConstant.PAY_CONFIG.get("SXFNEW_DF_URL"),reqStr);
			log.info("新随行付：单笔代付响应数据:"+body);
			RequestMessage requestMessage = JsonUtils.fromJson(body,RequestMessage.class);
			String sign = requestMessage.getSign();
			String resData = requestMessage.getResData();
			if("000000".equals(requestMessage.getResCode())){
				if(RSAUtils.verify(resData, sign, PayConstant.PAY_CONFIG.get("SXFNEW_DF_PUBKEY"))){//验签成功
					byte[] decodeResData = Base64Utils.decode(resData);
					byte[] decrypt = DESUtils.decrypt(decodeResData, PayConstant.PAY_CONFIG.get("SXFNEW_DES_PWD"));
					JSONObject parseObject = JSONObject.parseObject(new  String(decrypt,"utf-8"));
					String jsonStr = parseObject.getString("payResultList");
					JSONArray jsonArray = new JSONArray();
					JSONArray parseArray = jsonArray.parseArray(jsonStr);
					JSONObject jsonObject = (JSONObject) parseArray.get(0);
					if("00".equals(jsonObject.getString("resCd"))){
						rp.status="0";
						rp.errorMsg = "操作成功";
			        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
			        	SXFNEWQueryThread sxfquerythread =new SXFNEWQueryThread(payRequest);
			        	sxfquerythread.start();
					} else {
						rp.status="2";
						rp.errorMsg = "操作失败";
			        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					}
				} else{
					throw new Exception("新随行付验签失败！");
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
	/**
	 * 单笔代付查询
	 * @param request
	 * @param rp
	 * @throws Exception
	 */
	public void receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp) throws Exception{
		try {
			net.sf.json.JSONObject json = new net.sf.json.JSONObject();
			//请求参数
			String clientId = PayConstant.PAY_CONFIG.get("SXFNEW_MERNO");//商户id
			String reqId = System.currentTimeMillis()+"";//请求序列号
			String tranCd = "DF1004";//报文交易码
			String version = "0.0.0.1";//版本号
			String payItemId = rp.channelTranNo;//明细id
			json.put("clientId", clientId);
			json.put("reqId", reqId);
			json.put("tranCd", tranCd);
			json.put("version", version);
			net.sf.json.JSONObject reqData = new net.sf.json.JSONObject();
			reqData.put("payItemId", payItemId);
			
			byte[] bs = DESUtils.encrypt(reqData.toString().getBytes("UTF-8"), PayConstant.PAY_CONFIG.get("SXFNEW_DES_PWD"));
			//Base64编码
			String reqDataEncrypt = Base64Utils.encode(bs);
			json.put("reqData", reqDataEncrypt);
			json.put("sign", RSAUtils.sign(reqDataEncrypt, PayConstant.PAY_CONFIG.get("SXFNEW_DF_PRIKEY")));
			log.info("新随行付单笔代付结果查询请求数据："+json.toString());
			String body = HttpClientUtil.doPost(PayConstant.PAY_CONFIG.get("SXFNEW_DF_QUERY_URL"),json.toString());
			log.info("新随行付单笔代付结果查询响应数据："+body);
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
				if(RSAUtils.verify(resData, sign, PayConstant.PAY_CONFIG.get("SXFNEW_DF_PUBKEY"))){//验签成功
					byte[] decodeResData = Base64Utils.decode(resData);
					byte[] decrypt = DESUtils.decrypt(decodeResData, PayConstant.PAY_CONFIG.get("SXFNEW_DES_PWD"));
					JSONObject parseObject = JSONObject.parseObject(new  String(decrypt,"utf-8"));
					log.info(parseObject);
					if("00".equals(parseObject.getString("tranSts"))){
						request.setRespCode("000");
						request.receiveAndPaySingle.setRespCode("000");
						request.receiveAndPaySingle.retCode="000";
						request.receivePayRes = "0";
						request.respDesc="交易成功";
						rp.errorMsg = "交易成功";
					} else if("02".equals(parseObject.getString("tranSts"))){
						request.setRespCode("-1");
						request.receiveAndPaySingle.setRespCode("-1");
						request.receivePayRes = "-1";
						request.respDesc="交易失败";
						rp.errorMsg = request.respDesc;
					} else if("01".equals(parseObject.getString("tranSts"))){
						request.setRespCode("0");
						request.respDesc="处理中";
						rp.errorMsg = request.respDesc;
					}
				} else {
					throw new Exception("随行付单笔代付结果查询验签失败");
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 
}


class SXFNEWQueryThread extends Thread{
	private static final Log log = LogFactory.getLog(SXFNEWQueryThread.class);
	private  PayRequest payRequest= new PayRequest();
	public SXFNEWQueryThread(){};
	public SXFNEWQueryThread(PayRequest payRequest){
		this.payRequest=payRequest;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("新随行付：代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
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
			//请求参数
			String clientId = PayConstant.PAY_CONFIG.get("SXFNEW_MERNO");//商户id
			String reqId = System.currentTimeMillis()+"";//请求序列号
			String tranCd = "DF1004";//报文交易码
			String version = "0.0.0.1";//版本号
			String payItemId = rp.channelTranNo;//明细id
			json.put("clientId", clientId);
			json.put("reqId", reqId);
			json.put("tranCd", tranCd);
			json.put("version", version);
			net.sf.json.JSONObject reqData = new net.sf.json.JSONObject();
			reqData.put("payItemId", payItemId);
			
			byte[] bs = DESUtils.encrypt(reqData.toString().getBytes("UTF-8"), PayConstant.PAY_CONFIG.get("SXFNEW_DES_PWD"));
			//Base64编码
			String reqDataEncrypt = Base64Utils.encode(bs);
			json.put("reqData", reqDataEncrypt);
			json.put("sign", RSAUtils.sign(reqDataEncrypt, PayConstant.PAY_CONFIG.get("SXFNEW_DF_PRIKEY")));
			log.info("新随行付单笔代付结果查询请求数据："+json.toString());
			String body = HttpClientUtil.doPost(PayConstant.PAY_CONFIG.get("SXFNEW_DF_QUERY_URL"),json.toString());
			log.info("新随行付单笔代付结果查询响应数据："+body);
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
				if(RSAUtils.verify(resData, sign, PayConstant.PAY_CONFIG.get("SXFNEW_DF_PUBKEY"))){//验签成功
					byte[] decodeResData = Base64Utils.decode(resData);
					byte[] decrypt = DESUtils.decrypt(decodeResData, PayConstant.PAY_CONFIG.get("SXFNEW_DES_PWD"));
					JSONObject parseObject = JSONObject.parseObject(new  String(decrypt,"utf-8"));
					log.info(parseObject);
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
					} else if("02".equals(parseObject.getString("tranSts"))){
						rp.status="2";
						rp.respCode="-1";
						rp.retCode="-1";
						rp.errorMsg = parseObject.getString("tranMsg");
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						return true;
					} else {
						return false;
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