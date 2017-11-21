package com.pay.merchantinterface.service;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.DataTransUtil;
import util.Tools;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayWithdrawCashOrder;
import com.pay.order.dao.PayWithdrawCashOrderDAO;
import com.pay.order.service.PayOrderService;
import com.pay.refund.dao.PayRefund;
import com.third.reapal.AES;
import com.third.reapal.Decipher;
import com.third.reapal.Md5Utils;
import com.third.reapal.RSA;
import com.third.reapal.RandomUtil;
import com.third.reapal.ReapalSubmit;
import com.third.reapal.ReapalUtil;
/**
 * 融保接口服务类
 * @author Administrator
 *
 */
public class RBPayService {
	private static final Log log = LogFactory.getLog(RBPayService.class);
	public static Map<String, String> RB_B2B_BANK_CODE = new HashMap<String, String>();
	public static Map<String, String> RB_B2C_BANK_CODE = new HashMap<String, String>();
	static {
		RB_B2C_BANK_CODE.put("CMB","CMB");//招商银行
		RB_B2C_BANK_CODE.put("ICBC","ICBC");//工商银行
		RB_B2C_BANK_CODE.put("CCB","CCB");//建设银行
		RB_B2C_BANK_CODE.put("BOC","BOC");//中国银行
		RB_B2C_BANK_CODE.put("ABC","ABC");//农业银行
		RB_B2C_BANK_CODE.put("BOCOM","BOCM");//交通银行
		RB_B2C_BANK_CODE.put("SPDB","SPDB");//浦发银行
		RB_B2C_BANK_CODE.put("GDB","CGB");//广发银行
		RB_B2C_BANK_CODE.put("CNCB","CITIC");//中信银行
		RB_B2C_BANK_CODE.put("CEB","CEB");//光大银行
		RB_B2C_BANK_CODE.put("CIB","CIB");//兴业银行
		RB_B2C_BANK_CODE.put("PAB","SDB");//平安银行
		RB_B2C_BANK_CODE.put("CMBC","CMBC");//民生银行
		RB_B2C_BANK_CODE.put("HXB","HXB");//华夏银行
		RB_B2C_BANK_CODE.put("PSBC","PSBC");//邮储银行
		RB_B2C_BANK_CODE.put("BCCB","BCCB");//北京银行
		RB_B2C_BANK_CODE.put("BOS","SHBANK");//上海银行
		
		RB_B2B_BANK_CODE.put("ICBC","ICBC_B2B");
		RB_B2B_BANK_CODE.put("CCB","CCB_B2B");
		RB_B2B_BANK_CODE.put("ABC","ABC_B2B");
		RB_B2B_BANK_CODE.put("BOCM","BOCM_B2B");
		RB_B2B_BANK_CODE.put("SPDB","SPDB_B2B");
		RB_B2B_BANK_CODE.put("CEB","CEB_B2B");
		RB_B2B_BANK_CODE.put("SDB","SDB_B2B");
		RB_B2B_BANK_CODE.put("CMBC","CMBC_B2B");
		RB_B2B_BANK_CODE.put("HXB","HXB_B2B");
	}
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));
		Map<String, String> sPara = new HashMap<String, String>();
		try{
			sPara.put("seller_email",PayConstant.PAY_CONFIG.get("rb_seller_email"));//签约融宝支付账号或卖家收款融宝支付帐户
			sPara.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));//融保分配的商户号
			sPara.put("notify_url",PayConstant.PAY_CONFIG.get("rb_notify_url"));
			sPara.put("return_url",prdOrder.returl);
			sPara.put("transtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payOrder.createtime));
			sPara.put("currency", "156");//金额类型
			sPara.put("member_id","qtpay");//TODO
			sPara.put("member_ip", request.getRemoteAddr());
			sPara.put("terminal_type", "web");//web、wap、 mobile
			sPara.put("terminal_info", request.getRemoteAddr()+":"+request.getRemotePort());
			sPara.put("sign_type", PayConstant.PAY_CONFIG.get("rb_sign_type"));//签名类型
			sPara.put("order_no", payOrder.payordno);
			sPara.put("title", prdOrder.prdname==null||prdOrder.prdname.trim().length()==0?"online_pay":prdOrder.prdname);
			sPara.put("body",prdOrder.prdname==null||prdOrder.prdname.trim().length()==0?"online_pay":prdOrder.prdname);
			sPara.put("total_fee", String.valueOf(payOrder.txamt));//订单金额
			//支付类型  支付方式为银行间连时：值为1,支付方式为银行直连时：银行代码为B2B支付，值为1银行代码为B2C支付，1借记卡支付，2贷记卡支付
			String bankCode = RB_B2C_BANK_CODE.get(payOrder.bankcod)==null?payOrder.bankcod:RB_B2C_BANK_CODE.get(payOrder.bankcod);
			if("1".equals(payOrder.bustyp)){//个人B2C
				sPara.put("payment_type",String.valueOf(Long.parseLong(payOrder.bankCardType)+1));
				sPara.put("default_bank",bankCode);
			} else if("2".equals(payOrder.bustyp)){//B2B
				sPara.put("payment_type","1");
				sPara.put("default_bank", RB_B2B_BANK_CODE.get(bankCode)==null?(bankCode+"_B2B"):RB_B2B_BANK_CODE.get(bankCode));
			}
			//支付类型  支付方式为银行直连时：银行代码为B2B支付，值为1银行代码为B2C支付，1借记卡支付，2贷记卡支付
//			sPara.put("payment_type",String.valueOf(Long.parseLong(payOrder.bankCardType)+1));
//			sPara.put("default_bank",payOrder.bankcod);
			sPara.put("pay_method", "directPay");//支付方式 固定值 bankPay:银行间接支付，默认值； directPay:银行直连	
			String mysign = Md5Utils.BuildMysign(sPara,PayConstant.PAY_CONFIG.get("rb_key"));//生成签名结果
			sPara.put("sign", mysign);
		    String json = JSON.toJSONString(sPara);
		    log.info("收银台下单请求（融宝）:"+json.toString());
		    Map<String, String> maps = Decipher.encryptData(json);
			request.setAttribute("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));
			request.setAttribute("data",maps.get("data"));
			request.setAttribute("encryptkey",maps.get("encryptkey"));
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
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;
		Map<String, String> sPara = new HashMap<String, String>();
		try{
			sPara.put("seller_email",PayConstant.PAY_CONFIG.get("rb_seller_email"));//签约融宝支付账号或卖家收款融宝支付帐户
			sPara.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));//融保分配的商户号
			sPara.put("notify_url",PayConstant.PAY_CONFIG.get("rb_notify_url"));
			sPara.put("return_url",prdOrder.returl);
			sPara.put("transtime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payOrder.createtime));
			sPara.put("currency", "156");//金额类型
			sPara.put("member_id","qtpay");//TODO
			sPara.put("member_ip", request.getRemoteAddr());
			sPara.put("terminal_type", "web");//web、wap、 mobile
			sPara.put("terminal_info", request.getRemoteAddr()+":"+request.getRemotePort());
			sPara.put("sign_type", PayConstant.PAY_CONFIG.get("rb_sign_type"));//签名类型
			sPara.put("order_no", payOrder.payordno);
			sPara.put("title", prdOrder.prdname==null||prdOrder.prdname.trim().length()==0?"online_pay":prdOrder.prdname);
			sPara.put("body",prdOrder.prdname==null||prdOrder.prdname.trim().length()==0?"online_pay":prdOrder.prdname);
			sPara.put("total_fee", String.valueOf(payOrder.txamt));//订单金额
			String bankCode = RB_B2C_BANK_CODE.get(payOrder.bankcod)==null?payOrder.bankcod:RB_B2C_BANK_CODE.get(payOrder.bankcod);
			//支付类型  支付方式为银行间连时：值为1,支付方式为银行直连时：银行代码为B2B支付，值为1银行代码为B2C支付，1借记卡支付，2贷记卡支付
			if("1".equals(payRequest.userType)){//个人B2C
				sPara.put("payment_type",String.valueOf(Long.parseLong(payOrder.bankCardType)+1));
				sPara.put("default_bank", bankCode);
			} else if("2".equals(payRequest.userType)){//B2B
				sPara.put("payment_type","1");
				sPara.put("default_bank",RB_B2B_BANK_CODE.get(bankCode)==null?(bankCode+"_B2B"):RB_B2B_BANK_CODE.get(bankCode));
			}
			sPara.put("pay_method", "directPay");//支付方式 固定值 bankPay:银行间接支付，默认值； directPay:银行直连	
			String mysign = Md5Utils.BuildMysign(sPara,PayConstant.PAY_CONFIG.get("rb_key"));//生成签名结果
			sPara.put("sign", mysign);
		    String json = JSON.toJSONString(sPara);
		    log.info("商户接口下单（融宝）:"+json.toString());
		    Map<String, String> maps = Decipher.encryptData(json);
			request.setAttribute("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));
			request.setAttribute("data",maps.get("data"));
			request.setAttribute("encryptkey",maps.get("encryptkey"));
	        new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	public void refund(PayRefund payRefund){
		try{
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));
			map.put("version",PayConstant.PAY_CONFIG.get("rb_version"));
			map.put("order_no", Tools.getUniqueIdentify());
			map.put("orig_order_no",payRefund.oripayordno); // 原订单号
			//金额
			map.put("amount",String.valueOf(payRefund.rfamt));
			map.put("note", "退款");  //说明
			//返回结果
			String post = ReapalSubmit.buildSubmit(map,PayConstant.PAY_CONFIG.get("rb_merchant_id"),
					PayConstant.PAY_CONFIG.get("rb_gateway")+"/fast/refund",PayConstant.PAY_CONFIG.get("rb_key"));
			log.info("退款返回结果post==========>" + post);
			//解密返回的数据（{"result_code":"0000","result_msg":"退款成功"}）
			String res = Decipher.decryptData(post);
			JSONObject jsonObject = JSON.parseObject(res);	
    		String merchantId = jsonObject.getString("merchant_id");
    		String resultCode = jsonObject.getString("result_code");
    		String resultMsg = jsonObject.getString("result_msg");
    		if("0000".equals(resultCode))payRefund.banksts="01";//退款成功
    		else {
    			payRefund.banksts="02";
    			payRefund.bankerror=resultMsg+"（"+resultCode+"）";
    			payRefund.rfsake=resultMsg+"（"+resultCode+"）";
    		}
			log.info("解密退款返回的数据==========>" + res);
		} catch (Exception e) {
			e.printStackTrace();
			//订单状态，00:退款处理中  01:退款成功 02:退款失败 ，默认00
			payRefund.banksts="02";
			payRefund.bankerror = e.getMessage();
			payRefund.rfsake = e.getMessage();
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try{
			//{"merchant_id":"10000000000147","order_no":"pJCE6Rj2Q3rKTOG","result_code":"0000","status":"completed","timestamp":"2016-04-28 17:28:30","total_fee":"1000","trade_no":"10160428000012769"}
			Map<String, String> map = new HashMap<String, String>();
			map.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));
			map.put("version",PayConstant.PAY_CONFIG.get("rb_version"));
			map.put("order_no",payOrder.payordno);
			//返回结果
			String post = ReapalSubmit.buildSubmit(map,PayConstant.PAY_CONFIG.get("rb_merchant_id")
					,PayConstant.PAY_CONFIG.get("rb_gateway")+"/fast/search",PayConstant.PAY_CONFIG.get("rb_key"));
		    log.info("返回结果post==========>" + post);
		    //解密返回的数据
		    String res = Decipher.decryptData(post);
		    JSONObject jsonObject = JSON.parseObject(res);
		    log.info("解密返回的数据==========>" + res);
	        payOrder.actdat = new Date();
	        String resMsg = jsonObject.getString("result_msg");
	        if("completed".equals(jsonObject.getString("status"))){
	        	payOrder.ordstatus="01";//支付成功
	        	payOrder.bankjrnno = jsonObject.getString("trade_no");
	        	new NotifyInterface().notifyMer(payOrder);//支付成功
	        } else throw new Exception("支付渠道状态："+jsonObject.getString("status")+(resMsg==null?"":"（"+resMsg+"）"));
		} catch (Exception e){
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
	public String quickPay(HttpServletRequest request,PayRequest payRequest, 
			PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));
		map.put("version",PayConstant.PAY_CONFIG.get("rb_version"));
		map.put("card_no",payRequest.cardNo);
		map.put("owner",payRequest.userName);
		map.put("cert_type",payRequest.credentialType==null||
				payRequest.credentialType.length()==0?"01":payRequest.credentialType); // 默认
		map.put("cert_no",payRequest.credentialNo);
		map.put("phone",payRequest.userMobileNo);
		map.put("order_no",payOrder.payordno);
		map.put("transtime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payOrder.createtime));
		map.put("currency", "156"); // 默认
//		map.put("title",payRequest.merchantOrderDesc);
		map.put("title",PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
				"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
//		map.put("body",payRequest.merchantOrderDesc);
		map.put("body",PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
				"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));
		map.put("member_id",payRequest.payerId.length()==0?payRequest.credentialNo:payRequest.payerId);// 用户id
		map.put("terminal_type", "mobile");// 终端类型
		map.put("terminal_info", "djddh"); // 终端标识
		map.put("notify_url",PayConstant.PAY_CONFIG.get("rb_quick_notify_url"));
		map.put("member_ip",request.getRemoteAddr());// 用户IP
		map.put("seller_email",PayConstant.PAY_CONFIG.get("rb_seller_email"));
		// 金额
		map.put("total_fee",String.valueOf(payRequest.merchantOrderAmt));
		map.put("token_id", ReapalUtil.getUUID());
		log.info(map.toString());
		//发送
		String  url = "/fast/debit/portal";//默认借记卡
		if(PayConstant.CARD_BIN_TYPE_DAIJI.equals(payRequest.payOrder.bankCardType)){//信用卡
			map.put("cvv2",payRequest.cvv2);
			map.put("validthru",payRequest.validPeriod);
			url = "/fast/credit/portal";
		}
		String post = ReapalSubmit.buildSubmit(map,PayConstant.PAY_CONFIG.get("rb_merchant_id")
				,PayConstant.PAY_CONFIG.get("rb_gateway")+url,PayConstant.PAY_CONFIG.get("rb_key"));
		log.info("快捷下单返回结果post==========>" + post);
		//解密返回的数据
		String res = Decipher.decryptData(post);
		//{"bank_code":"CCB","bank_name":"建设银行","bind_id":"8790","member_id":"321345","merchant_id":"100000000011015","order_no":"20160509175748","result_code":"0000","result_msg":"签约成功"}
		log.info("解密返回的数据==========>" + res);
		JSONObject jsonObject = JSON.parseObject(res);
		String resultCode = jsonObject.getString("result_code");
		if(!"0000".equals(resultCode)){
			payOrder.bankerror = jsonObject.getString("result_msg");
			new PayOrderDAO().updateOrderError(payOrder);
		}
		return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" " +
				"merchantOrderId=\""+payRequest.merchantOrderId+"\" bindId=\""+jsonObject.getString("bind_id")+"\" " +
				"respCode=\""+("0000".equals(resultCode)?"000":"-1")+"\" respDesc=\""+jsonObject.getString("result_msg")+"\" " +
				"bankId=\""+payRequest.bankId+"\" bankName=\""+payRequest.bankName+"\"/>";
	}
	/**
	 * 快捷支付-短信验证码重发
	 * @param request
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String resendCheckCode(PayRequest payRequest) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));
		map.put("version",PayConstant.PAY_CONFIG.get("rb_version"));
		//订单号
		map.put("order_no",payRequest.payOrder.payordno);  
		//返回结果
		String post = ReapalSubmit.buildSubmit(map,PayConstant.PAY_CONFIG.get("rb_merchant_id")
				,PayConstant.PAY_CONFIG.get("rb_gateway")+"/fast/sms",PayConstant.PAY_CONFIG.get("rb_key"));
	    log.info("返回结果post==========>" + post);
	    //解密返回的数据==========>{"merchant_id":"100000000011015","order_no":"20160509175748","phone":"","result_code":"0000","result_msg":"发送成功"}
	    String res = Decipher.decryptData(post);
	    JSONObject jsonObject = JSON.parseObject(res);
	    if(!"0000".equals(jsonObject.getString("result_code"))){
	    	PayOrder payOrder = new PayOrder();
	    	payOrder.merno = payRequest.merchantId;
	    	payOrder.prdordno = payRequest.merchantOrderId;
			payOrder.bankerror = jsonObject.getString("result_msg");
			new PayOrderDAO().updateOrderError(payOrder);
		}
	    log.info("解密返回的数据==========>" + res);
	    return res;
	}
	/**
	 * 快捷支付-支付确认
	 * @param request
	 * @param payRequest
	 * @return
	 * @throws Exception
	 */
	public String certPayConfirm(PayRequest payRequest) throws Exception{
		Map<String, String> map = new HashMap<String, String>();
		map.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));
		map.put("version",PayConstant.PAY_CONFIG.get("rb_version"));
		map.put("order_no",payRequest.payOrder.payordno);
		map.put("check_code",payRequest.checkCode); //6位
		//返回结果
		String post = ReapalSubmit.buildSubmit(map,PayConstant.PAY_CONFIG.get("rb_merchant_id")
			,PayConstant.PAY_CONFIG.get("rb_gateway")+"/fast/pay",PayConstant.PAY_CONFIG.get("rb_key"));
	    log.info("返回结果post==========>" + post);
	    //解密返回的数据==========>{"order_no":"20160509175748","result_code":"3069","result_msg":"验证码错误"}
	    String res = Decipher.decryptData(post);
	    JSONObject jsonObject = JSON.parseObject(res);
	    if(!"0000".equals(jsonObject.getString("result_code"))){
	    	PayOrder payOrder = new PayOrder();
	    	payOrder.merno = payRequest.merchantId;
	    	payOrder.prdordno = payRequest.merchantOrderId;
			payOrder.bankerror = jsonObject.getString("result_msg");
			new PayOrderDAO().updateOrderError(payOrder);
		}
	    log.info("解密返回的数据==========>" + res);
	    return res;
	}
	/**
	 * 代付接口
	 * @param wcOrder
	 * @throws Exception
	 */
	public void createWithdraw(PayWithdrawCashOrder wcOrder) throws Exception{
		//1,   62220215080205389633,jack-cooper,工商银行,分行,支行,私,   0.01,CNY, 北京,北京,18910116131,身份证,  420321199202150718,0001,     12306,     hehe
		//序号,银行账户,             开户名,     开户行,  分行,支行,公/私,金额, 币种,省,  市,  手机号,     证件类型,证件号,            用户协议号,商户订单号,备注
		String content="";
		PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(wcOrder.bankpayacno);
		if(cardBin == null)throw new Exception("无法识别银行账号"); ;
		wcOrder.bankCode = cardBin.bankCode;
		content = content +"1,"//序号
			+wcOrder.bankpayacno+","//银行账户
			+wcOrder.bankpayusernm+","//开户名
			+cardBin.bankName+","//开户行
			+","//分行
			+","//支行
			+("1".equals(wcOrder.withdrawType)?"私":"公")+","//用户类型 1：对私 2：对公
			+String.format("%.2f", ((double)wcOrder.txamt)/100d)+","//金额
			+"CNY,"//币种
			+(wcOrder.branchProvince==null?"":wcOrder.branchProvince)+","//省
			+(wcOrder.branchCity==null?"":wcOrder.branchCity)+","//市
			+(wcOrder.mobileNo==null?"":wcOrder.mobileNo)+","//手机号
			+(wcOrder.certType==null?"":wcOrder.certType)+","//证件类型
			+(wcOrder.certNo==null?"":wcOrder.certNo)+","//证件号
			+","//用户协议号
			+wcOrder.casordno;//商户订单号
		String payType= PayConstant.PAY_CONFIG.get("rb_withdraw_pay_type");
		Map<String, String> map = new HashMap<String, String>(0);
		map.put("charset","UTF-8");
		map.put("trans_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		map.put("notify_url",PayConstant.PAY_CONFIG.get("rb_withdraw_notify_url"));
		map.put("batch_no", Tools.getUniqueIdentify());
		map.put("batch_count","1");
		map.put("batch_amount",String.format("%.2f", ((double)wcOrder.txamt)/100d));
		map.put("pay_type",payType==null?"1":payType);//0-加急；1-普通 	默认为普通
		map.put("content",content);		
		String mysign = ReapalUtil.BuildMysign(map,PayConstant.PAY_CONFIG.get("rb_key"));		
		map.put("sign", mysign);
		map.put("sign_type",PayConstant.PAY_CONFIG.get("rb_sign_type"));
		log.info("请求参数===================");
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){Object key = it.next();log.info(key+"="+map.get(key));}
		String json = JSON.toJSONString(map);
		Map<String, String> maps = ReapalUtil.addkey(json);
		maps.put("merchant_id",PayConstant.PAY_CONFIG.get("rb_merchant_id"));
		maps.put("version",PayConstant.PAY_CONFIG.get("rb_withdraw_version"));
//		String post = HttpClientUtil.post(PayConstant.PAY_CONFIG.get("rb_withdraw_url") + "agentpay/pay", maps);
		String post = new String(new DataTransUtil().doPost(
				PayConstant.PAY_CONFIG.get("rb_withdraw_url")+"agentpay/pay",getTranData(maps).getBytes()));
		log.info("响应==========>" + com.alibaba.fastjson.JSON.toJSONString(maps));
		String res = ReapalUtil.pubkey(post);
		//{"batch_no":"a1","charset":"UTF-8","merchant_id":"100000000000022","result_code":"0000","result_msg":"提交成功"}
		//{"result_code":"6003","result_msg":"同一日期内批次号不能重复"}
		//{"data":"ujJ++lo0Qw9cMIfKsCWyZO4Yl8pU212/Nzo8UD69AM1Rl01Ridvp/p4GRHVwNxYR/lcFe/IMcJSWcyH1UlSv5IF+HsAOqrukerl5HQZ75G8oNPo8RoQ20MdSgNk2/pTVXacrLfB6t+TIw5StCcCnmuxGVx7gpg1/IyhTHbmJwslm32OuU90JLW2xHEoae8tTqqV5w4acN1TfrtAcw4jTuhNzxVnBgsPGbned+214ryC27HGYI8wuQCbno10kvwZKCkMD57c+4Yky1ma1tc4hy000BjUCHzVh19iDZZk5lTzgntHlXL5NRyEMze9I2aKpIIQJsWo8R3eJgqJ8rDZbxJ4f80j2YH82vGnmzjO6x7rJXuYX60Y0/G/KoJzI+O5FB/J349muAI/PNACanAAPz7RSlDCPzhjRf1deXW3ImGTheXZP8bu6Htl86IObwvi5IEP+KGtB90iWRbBr4ZzrhrDDENphzSOghqwoYCCS62A=","encryptkey":"oCzmzwMGaMNeNAzG2jrDUG40YM2XOpX5zzsWgpQ7+Xq0MmLuTfbGdZioWmRebbwfdIrPP79ywHTJU9PZ3hYV6CW5PfFEJC6G9YNvdrXlZ22z9SdreSp8BORwAiWY/qD+LVS2zG+HOLPUwMO+wqtIRRtuaoRSCJpwU4dX6Gk49RYp9Cfc3uYyrmIUrcDIw1GHJWohozVPU0jtj0Ypg9MXA18zT72QKIGUgU2CP/VGthMFKFrDJ2zIta40zXoeAiH8KdpX1BGcSaib5fVrcfIjX+GAZlUOo5l2qYsuiUCAE815Gz6CiO8+j+SY9B4XXsxt9VMFIP7CdYmZWn/0MvTktg==","merchant_id":"100000000276349","version":"1.0"}
		//生产请求：{"batch_amount":"32.00","batch_count":"1","batch_no":"pM5ei5b2Q3rKTOF","charset":"UTF-8","content":"1,6212260200022222222,323,中国工商银行,,,公,32.00,CNY,,,,,,,pM5ejef2Q3rKTOI","notify_url":"http://www.qtongpay.com/pay/get-request-all.jsp","pay_type":"1","sign":"044458d45ac0442bda61e80005cfba4f","sign_type":"MD5","trans_time":"2016-05-24 10:40:03"}
		//生产响应：{"batch_no":"pM5ei5b2Q3rKTOF","charset":"UTF-8","merchant_id":"100000000276349","result_code":"0000","result_msg":"提交成功"}
		log.info("解密结果==========>"+res);
		JSONObject json1 = JSON.parseObject(res);
		wcOrder.bankerror = json1.getString("result_msg");
		wcOrder.resMessage = wcOrder.bankerror;
		//订单状态:00: 未处理   01:提现处理中  02:提现成功  03:提现失败  04:已退回支付账号  05:重新提现  99:超时 默认00
		if("0000".equals(json1.getString("result_code"))){
			wcOrder.ordstatus="01";
			wcOrder.sucTime = wcOrder.actTime;
		} else wcOrder.ordstatus="03";
	}
	public void withdrawNotify(String notifyInfo) throws Exception{
		JSONObject json = JSON.parseObject(ReapalUtil.pubkey(notifyInfo));
		String [] es = json.getString("data").replaceAll("，",",").split(",");
		String orderId = es[12];
		String status = es[13];
		PayWithdrawCashOrder wcOrder = new PayWithdrawCashOrderDAO().detailPayWithdrawCashOrder(orderId);
		wcOrder.updateUser="system";
		wcOrder.updateTime=new Date();
		wcOrder.withdrawWay="0";
		wcOrder.bankerror = ",融宝后台通知";
		wcOrder.bankjrnno = null;
		if("成功".equals(status)){
			wcOrder.ordstatus = "02";
			wcOrder.payret="00";
			wcOrder.sucTime=new Date();
		} else{
			wcOrder.payret="01";
			wcOrder.ordstatus = "03";
		}
		new PayWithdrawCashOrderDAO().updatePayWithdrawCashOrder(wcOrder);
	}
    public static Map<String, String> addkey(String json)throws Exception {
        //商户获取融宝公钥
        PublicKey pubKeyFromCrt = RSA.getPubKeyFromCRT(PayConstant.PAY_CONFIG.get("rb_public_cert"));
        //随机生成16数字
        String key = RandomUtil.getRandom(16);
        //对随机数进行加密
        String encryptkey = RSA.encrypt(key, pubKeyFromCrt);
        String encryData = AES.encryptToBase64(json, key);
        Map<String, String> map = new HashMap<String, String>();
        map.put("data", encryData);
		map.put("encryptkey", encryptkey);
        return map;
    }
    private String getTranData(Map<String,String> map){
    	if(map==null||map.size()==0)return"";
    	StringBuffer sb = new StringBuffer();
    	Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			Object key = it.next();
			sb.append(key).append("=").append(java.net.URLEncoder.encode(map.get(key))).append("&");
		}
		if(sb.length() > 0)return sb.substring(0, sb.length()-1);
		return "";
    }
}
