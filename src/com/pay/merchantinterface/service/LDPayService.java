package com.pay.merchantinterface.service;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.PayUtil;

import com.PayConstant;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.ld.DataTransUtil;
import com.third.ld.LDUtil;
import com.third.ld.Plat2Mer_v40;
import com.third.ld.ReqData;
import com.third.ld.SignUtil;
/**
 * 联动服务类
 * @author xk
 *
 */
public class LDPayService {
	//联动封装信息固定取值。
	private static String payParamters = "card_id,valid_date,cvv2,pass_wd,identity_code,card_holder,recv_account,recv_user_name,identity_holder,identityCode,cardHolder,mer_cust_name,account_name,bank_account,endDate";
	private static String charset = "UTF-8";
	//接口类统一请求路劲。
	private static String url = PayConstant.PAY_CONFIG.get("LD_URL");
	private static final Log log = LogFactory.getLog(LDPayService.class);
	//网银借记卡支持的银行
	public static Map<String, String> PAY_BANK_JIEJI = new HashMap<String, String>();
	//快捷借记卡支持的银行
	public static Map<String, String> PAY_BANK_QUICK_JIEJI = new HashMap<String, String>();
	static{
		PAY_BANK_JIEJI.put("ICBC", "ICBC");//中国工商银行
		PAY_BANK_JIEJI.put("CMB", "CMB");//招商银行
		PAY_BANK_JIEJI.put("ABC", "ABC");//中国农业银行
		PAY_BANK_JIEJI.put("CCB", "CCB");//中国建设银行
		PAY_BANK_JIEJI.put("CMBC", "CMBC");//中国民生银行
		PAY_BANK_JIEJI.put("SPDB", "SPDB");//浦发银行
		PAY_BANK_JIEJI.put("GDB", "GDB");//广发银行
		PAY_BANK_JIEJI.put("HXB", "HXB");//华夏银行
		PAY_BANK_JIEJI.put("PSBC", "PSBC");//邮储银行
		PAY_BANK_JIEJI.put("BOC", "BOC");//中国银行
		PAY_BANK_JIEJI.put("CEB", "CEB");//光大银行
		PAY_BANK_JIEJI.put("BEAI", "BEA");//东亚银行
		PAY_BANK_JIEJI.put("BOCOM", "COMM");//交通银行
		PAY_BANK_JIEJI.put("CNCB", "CITIC");//中信银行
		PAY_BANK_JIEJI.put("BCCB", "BJB");//北京银行
		PAY_BANK_JIEJI.put("PAB", "SPAB");//平安银行
		
		PAY_BANK_QUICK_JIEJI.put("SPDB", "SPDB");//浦发银行
		PAY_BANK_QUICK_JIEJI.put("ICBC", "ICBC");//中国工商银行
		PAY_BANK_QUICK_JIEJI.put("CCB", "CCB");//中国建设银行
		PAY_BANK_QUICK_JIEJI.put("ABC", "ABC");//中国农业银行
		PAY_BANK_QUICK_JIEJI.put("BOC", "BOC");//中国银行
		PAY_BANK_QUICK_JIEJI.put("CNCB", "CITIC");//中信银行
		PAY_BANK_QUICK_JIEJI.put("CEB", "CEB");//光大银行
		PAY_BANK_QUICK_JIEJI.put("CIB", "CIB");//兴业银行
		PAY_BANK_QUICK_JIEJI.put("ZSBC", "CZSB");//浙商银行
		PAY_BANK_QUICK_JIEJI.put("CMB", "CMB");//招商银行
		PAY_BANK_QUICK_JIEJI.put("CMBC", "CMBC");//中国民生银行
		PAY_BANK_QUICK_JIEJI.put("PAB", "SPAB");//平安银行
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		try {
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType))bankCode = PAY_BANK_JIEJI.get(payOrder.bankcod);
			if(bankCode == null)throw new Exception("不支持该银行（"+payOrder.bankcod+"）");
			Map<String, String> map = new HashMap<String, String>();
			map.put("service", "req_front_page_pay");
			map.put("charset", charset);
			map.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
			map.put("sign_type", "RSA");
			map.put("notify_url", PayConstant.PAY_CONFIG.get("LD_PAY_NOTIFY_URL"));
			map.put("version", "4.0");
			map.put("order_id", payOrder.payordno);
			map.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
			map.put("amount", String.valueOf(payOrder.txamt));
			map.put("amt_type", "RMB");
			map.put("pay_type", "B2CDEBITBANK");//个人网银借记卡，固定
			map.put("gate_id", PAY_BANK_JIEJI.get(payOrder.bankcod));
			map.put("interface_type", "02");
			ReqData reqData = LDUtil.getReqData(map, payParamters, "post", url);
			Map<String, String> ldReqData = reqData.getField();
			log.info("联动网银收银台下单请求数据："+ldReqData.toString());
			request.setAttribute("ldReqData", ldReqData);
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
	public void pay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_LD"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		try {
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType))bankCode = PAY_BANK_JIEJI.get(payOrder.bankcod);
			if(bankCode == null)throw new Exception("不支持该银行（"+payOrder.bankcod+"）");
			Map<String, String> map = new HashMap<String, String>();
			map.put("service", "req_front_page_pay");
			map.put("charset", charset);
			map.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
			map.put("sign_type", "RSA");
			map.put("notify_url", PayConstant.PAY_CONFIG.get("LD_PAY_NOTIFY_URL"));
			map.put("version", "4.0");
			map.put("order_id", payOrder.payordno);
			map.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
			map.put("amount", String.valueOf(payOrder.txamt));
			map.put("amt_type", "RMB");
			map.put("pay_type", "B2CDEBITBANK");//个人网银借记卡，固定
			map.put("gate_id", PAY_BANK_JIEJI.get(payOrder.bankcod));
			map.put("interface_type", "02");
			ReqData reqData = LDUtil.getReqData(map, payParamters, "post", url);
			Map<String, String> ldReqData = reqData.getField();
			log.info("联动网银接口下单请求数据："+ldReqData.toString());
			request.setAttribute("ldReqData", ldReqData);
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 快捷支付
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @return
	 * @throws Exception
	 */
	public String quickPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		try {
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.cardNo);//通过卡号获取卡类信息。
			Map<String, String> map = new HashMap<String, String>();
			map.put("service", "apply_pay_shortcut");
			map.put("charset", charset);
			map.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
			map.put("sign_type", "RSA");
			map.put("notify_url", PayConstant.PAY_CONFIG.get("LD_PAY_NOTIFY_URL"));
			map.put("version", "4.0");
			map.put("order_id", payOrder.payordno);
			map.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
			map.put("amount", payRequest.merchantOrderAmt);
			map.put("amt_type", "RMB");
			map.put("pay_type", "DEBITCARD");//CREDITCARD（信用卡） DEBITCARD(借记卡)
			map.put("gate_id", PAY_BANK_QUICK_JIEJI.get(cardBin.bankCode));
			ReqData reqData = LDUtil.getReqData(map, payParamters, "post", url);
			Map<String, String> field = reqData.getField();
			Iterator<String> iterator = field.keySet().iterator();
			String str = "";
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = field.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			log.info("联动快捷下单请求数据："+str.substring(0, str.length()-1));
			String resData = new String(new DataTransUtil().doPost(url, str.substring(0, str.length()-1).getBytes(charset)),charset);
			Map data = Plat2Mer_v40.getResData(resData);
			log.info("联动快捷下单响应数据："+data);
			/**
			 * {sign=N1y84ht00CS7nobr+nuX34bXbrUXz4GJ/W5W1p+ewVb0jihT1XPWVX7wlGE66hhIhLXXR1chDFcszuZXHlU+pF1vs91YcPrYfp0asV5Yt5o77Luk4Kzksik4NL+nJWWII/vPeHpxTYvX8jwCOSWKac6POulOPW/brq/SlabFmIk=, 
			 * trade_no=3708071507449721, ret_code=0000, mer_date=20170807, mer_id=50263, trade_state=WAIT_BUYER_PAY, sign_type=RSA, ret_msg=操作成功, payElements=,card_id,identity_type,card_holder,identity_code, order_id=1502089662356, plain=mer_date=20170807&mer_id=50263&order_id=1502089662356&payElements=,card_id,identity_type,card_holder,identity_code&ret_code=0000&
			 * ret_msg=操作成功&trade_no=3708071507449721&trade_state=WAIT_BUYER_PAY&version=4.0, version=4.0}
			 */
			if(SignUtil.verify(data.get("sign").toString(), data.get("plain").toString())){//验签成功
				if("0000".equals(data.get("ret_code"))){//操作成功
						 PayOrderDAO payOrderDAO = new PayOrderDAO();
						 payOrder.bankjrnno = (String) data.get("trade_no");
						 payOrderDAO.updateOrderBankjrnno(payOrder);
					 	//下单成功后，调用验证码接口发送短信给用户手机。
						 Map<String, String> codeMap = new HashMap<String, String>();
						 codeMap.put("service", "sms_req_shortcut");
						 codeMap.put("charset", charset);
						 codeMap.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
						 codeMap.put("sign_type", "RSA");
						 codeMap.put("version", "4.0");
						 codeMap.put("trade_no", payOrder.bankjrnno);
						 codeMap.put("media_id", payRequest.userMobileNo);
						 codeMap.put("media_type", "MOBILE");
						 //下单接口返回的快捷要素。card_id,identity_type,card_holder,identity_code
						 String payElements = (String) data.get("payElements");
						 String[] elements = payElements.split(",");
						 for (String element : elements) {
							if("card_id".equals(element)){//卡号
								codeMap.put("card_id", payRequest.cardNo);
								continue;
							}else if("identity_type".equals(element)){//证件类型
								codeMap.put("identity_type", "IDENTITY_CARD");
								continue;
							}else if("identity_code".equals(element)){//证件号
								codeMap.put("identity_code", payRequest.credentialNo);
								continue;
							}else if("card_holder".equals(element)){//持卡人姓名
								codeMap.put("card_holder", payRequest.userName);
								continue;
							}else if("valid_date".equals(element)){//信用卡有效期（YYMM）
								codeMap.put("valid_date", payRequest.validPeriod);
								continue;
							}else if("cvv2".equals(element)){//信用卡CVN2/CVV2
								codeMap.put("cvv2", payRequest.cvv2);
								continue;
							}
						}
						ReqData reqGetCodeData = LDUtil.getReqData(codeMap, payParamters, "post", url);
						Map<String, String> getCodeField = reqGetCodeData.getField();
						Iterator<String> getCodeIterator = getCodeField.keySet().iterator();
						String tmp = "";
						while(getCodeIterator.hasNext()){
							String key = getCodeIterator.next();
							String value = getCodeField.get(key);
							tmp += key+"="+URLEncoder.encode(value,charset)+"&";
						}
						log.info("联动获取验证码请求数据："+tmp.substring(0, tmp.length()-1));
						String responseData = new String(new DataTransUtil().doPost(url, tmp.substring(0, tmp.length()-1).getBytes(charset)),charset);
						Map respData = Plat2Mer_v40.getResData(responseData);
						log.info("联动获取验证码响应数据："+respData);
						/**
						 * mer_id=50263&ret_code=0000&ret_msg=操作成功&sign_type=RSA&version=4.0&
						 * sign=V85QzDL8Tr7oFw4fKRTWVwIwxXpCIlSSiL1ZyIgGsmE0ru50xEwpMYoEwVoIX86PlaM1DbWLC6rS8YC9eAWNilaSUTsrY18brdfoafxnpXbuSxq/6k9IOGUkhmrQzS46Xbqh64eVDMdC0Vsw3PiXZOoNBtbhH6BQ/LtoBePihDA=
						 */
						if(SignUtil.verify(respData.get("sign").toString(), respData.get("plain").toString())){//验签成功
							if("0000".equals(respData.get("ret_code"))){//操作成功
								payOrder.bankerror = (String) respData.get("ret_msg");
								return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
								"<message merchantId=\""+payRequest.merchantId+"\" " +
								"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\"\" " +
								"respCode=\"000\" respDesc=\"下单成功\" " +
								"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
							}else return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
							"<message merchantId=\""+payRequest.merchantId+"\" " +
							"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
							"respCode=\"-1\" respDesc=\""+respData.get("ret_msg")+"\" " +
							"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
						}else throw new Exception("验签失败");
				}throw new Exception("下单失败");
			}else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
			"<message merchantId=\""+payRequest.merchantId+"\" " +
			"merchantOrderId=\""+payRequest.merchantOrderId+"\"  bindId=\"\" " +
			"respCode=\"-1\"  respDesc=\""+e.getMessage()+"\" " +
			"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
		}
	}
	/**
	 * 快捷确认
	 * @param payRequest
	 * @return
	 * @throws Exception 
	 */
	public Map certPayConfirm(PayRequest payRequest) throws Exception {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("service", "confirm_pay_shortcut");
			map.put("charset", charset);
			map.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
			map.put("sign_type", "RSA");
			map.put("version", "4.0");
			map.put("trade_no", payRequest.payOrder.bankjrnno);
			map.put("verify_code", payRequest.checkCode);
			map.put("media_type", "MOBILE");
			map.put("media_id", payRequest.productOrder.mobile);
			map.put("card_id", payRequest.payOrder.bankpayacno);
			map.put("identity_type", "IDENTITY_CARD");
			map.put("identity_code", payRequest.productOrder.credentialNo);
			map.put("card_holder", payRequest.payOrder.bankpayusernm);
			
			ReqData reqData = LDUtil.getReqData(map, payParamters, "post", url);
			Map<String, String> field = reqData.getField();
			Iterator<String> iterator = field.keySet().iterator();
			String str = "";
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = field.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			log.info("联动快捷支付确认请求数据："+str.substring(0, str.length()-1));
			String resData = new String(new DataTransUtil().doPost(url, str.substring(0, str.length()-1).getBytes(charset)),charset);
			Map data = Plat2Mer_v40.getResData(resData);
			log.info("联动快捷支付确认响应数据："+data);
			if(SignUtil.verify(data.get("sign").toString(), data.get("plain").toString())){//验签成功
				return data;
			}else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 渠道补单（网银快捷代收查单）
	 * @param payOrder
	 * @throws Exception
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("service", "mer_order_info_query");
			map.put("charset", charset);
			map.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
			map.put("sign_type", "RSA");
			map.put("version", "4.0");
			map.put("order_id", payOrder.payordno);
			map.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(payOrder.createtime));
			ReqData reqData = LDUtil.getReqData(map, payParamters, "post", url);
			Map<String, String> field = reqData.getField();
			Iterator<String> iterator = field.keySet().iterator();
			String str = "";
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = field.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			log.info("联动查单请求数据："+str.substring(0, str.length()-1));
			String resData = new String(new DataTransUtil().doPost(url, str.substring(0, str.length()-1).getBytes(charset)),charset);
			Map data = Plat2Mer_v40.getResData(resData);
			log.info("联动查单响应数据："+data);
			/**
			 * {trade_no=3708070941061461, media_type=MOBILE, mer_date=20170807, trade_state=WAIT_BUYER_PAY, sign_type=RSA, ret_msg=操作成功, orig_retcode=, 
			 * settle_date=, order_id=1502070016684, version=4.0, refund_amt=0, amt_type=RMB, amount=1, sign=IphfevQP6aRTZyc6DdEY1S18N4ldSnzlVt+s0AgyrIvLlZ/MHIN5jIZLlZBvi7YnqC1chXNDKu0dpwd7xrRNQV+LDlbajhAvVT95jA/BDCJ5sJTn6zFmWqV80VgwxvGhP2XGFpdoDhrHoZSp8UBC9ws7uGlaZzrzSMvkNu1eGlU=, orig_retmsg=, ret_code=0000, pay_type=B2CDEBITBANK, mer_id=50263, pay_date=, plain=amount=1&amt_type=RMB&media_type=MOBILE&mer_date=20170807&mer_id=50263&order_id=1502070016684&orig_retcode=&orig_retmsg=&pay_date=&pay_type=B2CDEBITBANK&refund_amt=0&
			 * ret_code=0000&ret_msg=操作成功&settle_date=&trade_no=3708070941061461&trade_state=WAIT_BUYER_PAY&version=4.0}
			 */
			if(SignUtil.verify(data.get("sign").toString(), data.get("plain").toString())){//验签成功
				if("0000".equals(data.get("ret_code"))){
					if("TRADE_SUCCESS".equals(data.get("trade_state"))){
						 payOrder.ordstatus="01";//支付成功
					     new NotifyInterface().notifyMer(payOrder);//支付成功
					} 
				}
			}else throw new Exception("验签失败");
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
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
		 	PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
		 	Map<String, String> map = new HashMap<String, String>();
			map.put("service", "transfer_direct_req");
			map.put("charset", charset);
			map.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
			map.put("sign_type", "RSA");
			map.put("version", "4.0");
			map.put("notify_url", PayConstant.PAY_CONFIG.get("LD_RECEIVEPAY_SINGLE_NOTIFY_URL"));
			map.put("order_id", rp.id);
			map.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
			map.put("amount", payRequest.amount);
			map.put("recv_account_type", "00");//00-----银行卡    02-----U付
			map.put("recv_bank_acc_pro", "0");//0  对私    1对公
			map.put("recv_account", rp.accountNo);
			map.put("recv_user_name", rp.accountName);
			map.put("purpose", "purpose");//摘要  收款方账户类型为00时必填
//			map.put("bank_brhname", "");//开户行支行全称  收款方账户类型recv_account_type为00，且收款方账户属性recv_bank_acc_pro为1时必填
			ReqData reqData = LDUtil.getReqData(map, payParamters, "post", url);
			Map<String, String> field = reqData.getField();
			Iterator<String> iterator = field.keySet().iterator();
			String str = "";
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = field.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			log.info("联动代付请求数据："+str.substring(0, str.length()-1));
			String resData = new String(new DataTransUtil().doPost(url, str.substring(0, str.length()-1).getBytes(charset)),charset);
			Map<String,String> data = Plat2Mer_v40.getResData(resData);
			log.info("联动代付响应数据："+data);
			/**
			 * {trade_no=1709041639020142, mer_date=20170904, trade_state=3, sign_type=RSA, ret_msg=余额不足, fee_type=0, order_id=J75WYJ5E3JLXPT2PU, version=4.0, sign=Grm+Ua2IS77tEWecWWQIAXzqIoi8zxsN02l8XrqK90kE8Os3RN0fROXPIfVpAjbfiNUeSdSQ4ttoHN5CBVXw2Nlp5YWsJLAz0aa2blheU0b3gUf4+Jxf9DPcgp6SCGmkwpJAqeG73mgOGd0jIxuAfiikG1+kAEXSxz+bua0nrEw=, fee=100, 
			 * amount=1, transfer_settle_date=, ret_code=00131040, mer_id=50263, plain=amount=1&fee=100&fee_type=0&mer_date=20170904&
			 * mer_id=50263&order_id=J75WYJ5E3JLXPT2PU&ret_code=00131040&ret_msg=余额不足&trade_no=1709041639020142&trade_state=3&transfer_settle_date=&version=4.0}
			 */
			if(SignUtil.verify(data.get("sign").toString(), data.get("plain").toString())){//验签成功
    			if("0000".equals(data.get("ret_code"))){
    				if("4".equals(data.get("trade_state"))){//成功
    					rp.status="1";
    					rp.setRetCode("000");
    					rp.errorMsg = "交易成功";
    	       			payRequest.receivePayRes = "0";
    	       			try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
    				}else if("3".equals(data.get("trade_state"))){//失败
    					rp.status="2";
    					rp.setRetCode("-1");
    					rp.errorMsg = data.get("ret_msg");
    	       			payRequest.receivePayRes = "-1";
    	       			try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
    				}else if("1".equals(data.get("trade_state"))){//支付中
    					LDQueryThread ldquerythread = new LDQueryThread(payRequest,"transfer_query");
    					ldquerythread.start();
    				}
    			}else{
    				rp.status="2";
    				rp.setRetCode("-1");
    				rp.errorMsg = data.get("ret_msg")==null?"代付失败":data.get("ret_msg");
           			payRequest.receivePayRes = "-1";
           			try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
    			}
    		}else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
			throw e;
		}
	}
	/**
	 * 单笔代扣
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingleInfo(PayRequest payRequest) throws Exception{
		 try {
		 	PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
        	Map<String, String> map = new HashMap<String, String>();
    		map.put("service", "debit_direct_pay");
    		map.put("charset", charset);
    		map.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
    		map.put("sign_type", "RSA");
    		map.put("version", "4.0");
    		map.put("notify_url", PayConstant.PAY_CONFIG.get("LD_RECEIVEPAY_SINGLE_NOTIFY_URL"));
    		map.put("order_id", rp.id);
    		map.put("version", "4.0");
    		map.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
    		map.put("amount", payRequest.amount);
    		map.put("amt_type", "RMB");
    		map.put("pay_type", "DEBITCARD");
    		map.put("card_id", rp.accountNo);
    		map.put("identity_type", "IDENTITY_CARD");
    		map.put("identity_code", rp.certId);
    		map.put("card_holder", rp.accountName);
    		ReqData reqData = LDUtil.getReqData(map, payParamters, "post", url);
    		Map<String, String> field = reqData.getField();
    		Iterator<String> iterator = field.keySet().iterator();
    		String str = "";
    		while(iterator.hasNext()){
    			String key = iterator.next();
    			String value = field.get(key);
    			str += key+"="+URLEncoder.encode(value,charset)+"&";
    		}
    		log.info("联动单笔代扣请求数据："+str.substring(0, str.length()-1));
    		String resData = new String(new DataTransUtil().doPost(url, str.substring(0, str.length()-1).getBytes(charset)),charset);
    		Map<String,String> data = Plat2Mer_v40.getResData(resData);
    		log.info("联动单笔代扣响应数据："+data);
    		if(SignUtil.verify(data.get("sign").toString(), data.get("plain").toString())){//验签成功
    			if("0000".equals(data.get("ret_code"))){
    				if("TRADE_SUCCESS".equals(data.get("trade_state"))){
    					rp.status="1";
    					rp.setRetCode("000");
    					rp.errorMsg = "交易成功";
    	       			payRequest.receivePayRes = "0";
    	       			try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
    				}else if("TRADE_FAIL".equals(data.get("trade_state"))){
    					rp.status="2";
    					rp.setRetCode("-1");
    					rp.errorMsg = data.get("ret_msg");
    	       			payRequest.receivePayRes = "-1";
    	       			try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
    				}
    			} else if("00060761".equals(data.get("ret_code"))){//处理中,执行查询来确定最终的交易状态。
    				LDQueryThread ldquerythread = new LDQueryThread(payRequest,"mer_order_info_query");
					ldquerythread.start();
    			} else {
    				rp.status="2";
    				rp.setRetCode("-1");
    				rp.errorMsg = data.get("ret_msg")==null?"代收失败":data.get("ret_msg");
           			payRequest.receivePayRes = "-1";
           			try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
    			}
    		}else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	 }
	 /**
	 * 单笔代收付查询
	 * @param request
	 * @param rp
	 * @throws Exception
	 */
	public void receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp,String serviceType) throws Exception{
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("service", serviceType);//mer_order_info_query-代收查询   transfer_query-代付查询
			map.put("charset", charset);
			map.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
			map.put("sign_type", "RSA");
			map.put("version", "4.0");
			map.put("order_id", rp.id);
			map.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(rp.createTime));
			ReqData reqData = LDUtil.getReqData(map, payParamters, "post", url);
			Map<String, String> field = reqData.getField();
			Iterator<String> iterator = field.keySet().iterator();
			String str = "";
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = field.get(key);
				str += key+"="+URLEncoder.encode(value,charset)+"&";
			}
			log.info("联动代扣查单请求数据："+str.substring(0, str.length()-1));
			String resData = new String(new DataTransUtil().doPost(url, str.substring(0, str.length()-1).getBytes(charset)),charset);
			Map<String,String> data = Plat2Mer_v40.getResData(resData);
			log.info("联动代扣查单响应数据："+data);
			/**
			 * {trade_no=1709041640271422, mer_date=20170904, trade_state=3, sign_type=RSA, ret_msg=查询成功, purpose=purpose, order_id=J75WYJ5E3JLXPT2PY, version=4.0, sign=HeWo7zCgwGE+a0EmEhuabed75f0j9EBsMMa61tkEwSKlQ+5li6pMAU5njksxedSfR6qbcRIJJwUoR9XXxschxnw8GSYMwRYp+9WVLJ2z/cFpz1HeKxdzziiV6DfK4trdb0p5lm6Zu9Rv1Bd8hYSZYuOZWkBoONgBxrBEnPo2Bz4=, 
			 * fee=100, amount=12, transfer_settle_date=, ret_code=0000, mer_id=50263, transfer_date=, plain=amount=12&fee=100&mer_date=20170904&mer_id=50263&
			 * order_id=J75WYJ5E3JLXPT2PY&purpose=purpose&ret_code=0000&ret_msg=查询成功&trade_no=1709041640271422&trade_state=3&transfer_date=&transfer_settle_date=&version=4.0}
			 */
			if(SignUtil.verify(data.get("sign").toString(), data.get("plain").toString())){//验签成功
				if("0000".equals(data.get("ret_code"))){
					if("TRADE_SUCCESS".equals(data.get("trade_state")) || "4".equals(data.get("trade_state"))){//成功
						request.setRespCode("000");
						rp.setRespCode("000");
						request.receivePayRes = "0";
						request.respDesc="交易成功";
						rp.errorMsg = "交易成功";
					}else if("TRADE_FAIL".equals(data.get("trade_state")) || "3".equals(data.get("trade_state"))){//失败
						request.setRespCode("-1");
						rp.setRespCode("000");
						request.receivePayRes = "-1";
						request.respDesc = data.get("ret_msg");
						rp.errorMsg = request.respDesc;
					}
				} else {
					request.setRespCode("-1");
					request.receiveAndPaySingle.setRespCode("-1");
					request.receivePayRes = "-1";
					request.respDesc=data.get("ret_msg");
					rp.errorMsg = request.respDesc;
				}
			} else throw new Exception("验签失败");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
class LDQueryThread extends Thread{
	private static String payParamters = "card_id,valid_date,cvv2,pass_wd,identity_code,card_holder,recv_account,recv_user_name,identity_holder,identityCode,cardHolder,mer_cust_name,account_name,bank_account,endDate";
	private static final Log log = LogFactory.getLog(LDQueryThread.class);
	private  PayRequest payRequest = new PayRequest();
	private String serviceType;
	public LDQueryThread(){};
	public LDQueryThread(PayRequest payRequest,String serviceType){
		this.payRequest = payRequest;
		this.serviceType = serviceType;
	}
	@Override
	public void run() {
		try {
			for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
				try {if(query())break;} catch (Exception e) {}
				log.info("联动代扣查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
				try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean query()throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//已经添加的代扣记录。
			Map<String, String> map = new HashMap<String, String>();
			map.put("service", serviceType);//mer_order_info_query 代扣    transfer_query 代收
			map.put("charset", "UTF-8");
			map.put("mer_id", PayConstant.PAY_CONFIG.get("LD_MERCHANT_ID"));
			map.put("sign_type", "RSA");
			map.put("version", "4.0");
			map.put("order_id", rp.id);
			map.put("mer_date", new SimpleDateFormat("yyyyMMdd").format(rp.createTime));
			ReqData reqData = LDUtil.getReqData(map, payParamters, "post", PayConstant.PAY_CONFIG.get("LD_URL"));
			Map<String, String> field = reqData.getField();
			Iterator<String> iterator = field.keySet().iterator();
			String str = "";
			while(iterator.hasNext()){
				String key = iterator.next();
				String value = field.get(key);
				str += key+"="+URLEncoder.encode(value,"utf-8")+"&";
			}
			log.info("联动查单请求数据："+str.substring(0, str.length()-1));
			String resData = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("LD_URL"), str.substring(0, str.length()-1).getBytes("UTF-8")),"UTF-8");
			Map data = Plat2Mer_v40.getResData(resData);
			log.info("联动查单响应数据："+data);
			if(SignUtil.verify(data.get("sign").toString(), data.get("plain").toString())){//验签成功
				if("0000".equals(data.get("ret_code"))){
					if("TRADE_SUCCESS".equals(data.get("trade_state"))){
						rp.status="1";
						rp.respCode="000";
						rp.errorMsg="交易成功";
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
						list.add(rp);
						new PayInterfaceOtherService().receivePayNotify(payRequest,list);
						return true;
					} else if("TRADE_FAIL".equals(data.get("trade_state"))){
						rp.status="2";
						rp.respCode="-1";
						rp.errorMsg = (String) data.get("ret_msg");
						try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
						List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
						list.add(rp);
						new PayInterfaceOtherService().receivePayNotify(payRequest,list);
						return true;
					}
				} else {
					rp.status="2";
					rp.respCode="-1";
					rp.errorMsg=(String) data.get("ret_msg");
					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
					List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
					list.add(rp);
					new PayInterfaceOtherService().receivePayNotify(payRequest,list);
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