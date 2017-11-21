package com.pay.merchantinterface.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.pay.coopbank.service.PayCoopBankService;
import com.pay.merchant.dao.PayYakuStlAccDAO;

public class YKPayService {
//	private static final Log log = LogFactory.getLog(YKPayService.class);
//	public static Map<String, String> bankName_map = new HashMap<String, String>();
//	public static Map<String, String> status_map = new HashMap<String, String>();
//	private static Timer timer = new Timer();
//	public static void changeChannel(){
//		try {
//			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
//			gc.setTime(new Date());
//			gc.add(Calendar.DATE, 1);
//			Date t1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").
//					format(gc.getTime()) + " 08:50:00");
//			Date t2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd").
//					format(gc.getTime()) + " 21:40:00");
//			timer.schedule(new ChangeChannelToYK(),t1, 24*60*60*1000);
//			timer.schedule(new ChangeChannelToZX(),t2, 24*60*60*1000);
//		} catch (Exception e) {
//			
//		}
//	}
//	static {
//		bankName_map.put("ICBC","中国工商银行");
//		bankName_map.put("ABC","中国农业银行");
//		bankName_map.put("BOC","中国银行");
//		bankName_map.put("CCB","中国建设银行");
//		bankName_map.put("BOCOM","交通银行");
//		bankName_map.put("CMB","招商银行");
//		bankName_map.put("CEB","中光大银行");
//		bankName_map.put("CMBC","民生银行");
//		bankName_map.put("PSBC","中国邮政储蓄银行");
//		bankName_map.put("SPDB","浦发银行");
//		bankName_map.put("CNCB","中信银行");
//		bankName_map.put("PAB","平安银行");
//		bankName_map.put("HXB","华夏银行");
//		bankName_map.put("CIB","兴业银行");
//		bankName_map.put("BOHC","渤海银行");
//		bankName_map.put("BCCB","北京银行");
//		bankName_map.put("GDB","广发银行");
//		bankName_map.put("BOS","上海银行");
//		bankName_map.put("ZSBC","浙商银行");
//		bankName_map.put("NBBC","宁波银行");
//		bankName_map.put("NJBC","南京银行");
//		bankName_map.put("SCB","渣打银行");
//		bankName_map.put("WZCB","温州银行");
//		bankName_map.put("YDXH","尧都农商银行");
//		bankName_map.put("BRCB","北京农村商业银行");
//		bankName_map.put("CCQTGB","重庆三峡银行");
//		bankName_map.put("HCCB","杭州市商业银行");
//		bankName_map.put("HNNXS","湖南农村信用合作社");
//		bankName_map.put("SXJS","晋商银行");
//		bankName_map.put("GZCB","广州银行股份有限公司");
//		bankName_map.put("EGBANK","恒丰银行");
//		bankName_map.put("CSCB","长沙银行");
//		bankName_map.put("SHRCB","上海农村商业银行");
//		bankName_map.put("GNXS","广州农村商业银行");
//		bankName_map.put("SDE","顺德农商银行");
//		bankName_map.put("CZCB","稠州银行");
//		bankName_map.put("HKBCHINA","汉口银行");
//		bankName_map.put("SNXS","深圳农村商业银行");
//		bankName_map.put("HSBANK","徽商银行");
//		bankName_map.put("CYB","集友银行");
//		bankName_map.put("BEAI","稠州银行");
//		bankName_map.put("CZCB","BEA东亚银行");
//		
//		//订单状态 00 已申请, 01 支付提交,等待支付, 02 支付成功, 05 失败, 06 关闭, 07 不存在
//		status_map.put("00", "未支付");
//		status_map.put("01", "支付成功");
//		status_map.put("02", "支付失败");
//	}
//	/**
//	 * 
//	 * @param request
//	 * @param payRequest
//	 * @param prdOrder
//	 * @param payOrder
//	 * @param productType 业务标示：选择扫码机构的编码 支付宝ALIPAY ，微信WEIXIN
//	 * @return
//	 */
//	//如果由于系统错误，就持续下单三次
//	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
//		//结算预备处理
//		readyStlInfo(payRequest,payOrder);
//		//下单处理
//		try {
//			int max = 5;
//			try {max = Integer.parseInt(PayConstant.PAY_CONFIG.get("YK_RECREATE_ORDER_FOR_FAIL"));} catch (Exception e) {}
//			String oldOrderNo = payOrder.payordno;
//			PayInterfaceDAO dao = new PayInterfaceDAO();
//			for(int n=0; n<max; n++){
//				if(n!=0)payOrder.payordno=oldOrderNo+"_"+n;
//				//计算手续费
//				Map<String,PayMerchant>  merMap = new PayMerchantDAO().getSettleMentMerchant(payRequest.merchantId);//取得商户和父商户
//				PayMerchant mer = merMap.get(payRequest.merchantId);
//				PayFeeRate feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+","+("WEIXIN".equals(productType)?"10":"17"));//10微信扫码，17支付宝扫码 18微信WAP
//				Object [] feeInfo = PayFeeRateService.getFeeByFeeRate(feeRate,payOrder.txamt);
//				long fee = (Long)(feeInfo[0]);
//				log.info("雅酷扫码手续费==============="+fee);
//				//payRequest.merchant.payCustStlInfo.custstlban
//				Map<String, String> param = new LinkedHashMap<String, String>();
//				param.put("service", "unifiedOrder");// 接口名称
//				param.put("signType", "MD5"); // 签名类型 MD5/RSA/CA 目前支持MD5
//				param.put("inputCharset", "UTF-8");// 系统之间交互信息时使用的编码字符集 通常默认使用UTF-8
//				param.put("sysMerchNo", PayConstant.PAY_CONFIG.get("PAY_YK_MERCHANT_NO"));// 系统商户号 由我方分配开通
//				param.put("outOrderNo", payOrder.payordno);// 商户订单号
//				param.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 订单时间 格式：yyyyMMddHHmmss
//				param.put("orderAmt", String.format("%.2f", ((double)payOrder.txamt)/100d));// 订单金额 单元元,精确到分
//				param.put("orderTitle", PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
//						"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"));//商品名称
//				param.put("selectFinaCode", productType);// 选择扫码机构的编码 支付宝ALIPAY ，微信WEIXIN
//				param.put("tranAttr", "NATIVE");// 调用接口提交的交易属性，取值如下： NATIVE
//				param.put("backUrl", PayConstant.PAY_CONFIG.get("PAY_YK_NOTIFY_URL"));//支付通知地址。
//				//判断商户结算周期
//				if("D".equals(mer.payCustStlInfo.custSetPeriod)){
//					if("0".equals(mer.payCustStlInfo.custSetFrey))param.put("receiptCycle", "T0");//收款人到帐周期  T0 T1，默认为T1，如果为T0，以下打款账号为必填
//					else if("1".equals(mer.payCustStlInfo.custSetFrey))param.put("receiptCycle", "T1");
//					else throw new Exception("YK无效的结算周期");						
//				} else throw new Exception("YK无效的结算周期");
//				param.put("payeeFeeAmt",  String.format("%.2f", ((double)fee)/100d));// 订单手续费 金额单位元，到账金额＝订单金额－订单手续费，注意手续费不得小于商户结算手续费
//				param.put("payeeAcctNo", payRequest.merchant.payCustStlInfo.custStlBankAcNo);//收款人卡号。
//				param.put("payeeAcctName", payRequest.merchant.payCustStlInfo.custBankDepositName);//收款人姓名。
//				param.put("payeeFinaName",bankName_map.get(payRequest.merchant.payCustStlInfo.depositBankCode));//收款人银行名称。
//				param.put("payeeIdType", "01");// 收款人证件类型 01 身份证
//				param.put("payeeIdNo", payRequest.merchant.payCustStlInfo.custStlIdno);//收款人身份证号码。
//				param.put("payeeMobileNo",payRequest.merchant.payCustStlInfo.custStlMobileno);//收款人手机号。
//				param.put("payeeBankProvince",payRequest.merchant.payCustStlInfo.custStlBankProvince);//收款人开户省份。
//				param.put("payeeBankCity", payRequest.merchant.payCustStlInfo.custStlBankCity);//收款人开户城市。
//				param.put("payeeBankName", payRequest.merchant.payCustStlInfo.depositBankBrchName);//收款人办卡支行名称。
//				param.put("payeeBankNo", payRequest.merchant.payCustStlInfo.custStlBankNumber);//收款人持卡行号。
//				log.info("雅酷扫码请求参数===============\n"+param);			
//				// 数据的签名字符串
//				String sign = SignUtils.getSign(param, PayConstant.PAY_CONFIG.get("PAY_YK_KEY"), "UTF-8");
//				param.put("sign", sign);
//				String result = HttpClientUtils.submitPost(PayConstant.PAY_CONFIG.get("PAY_YK_API_URL"), param);
//				log.info("雅酷:"+productType+"返回参数"+ result);
//				JSONObject jsonObject = JSON.parseObject(result);
//				String tranDesc = jsonObject.getString("tranDesc");
//				//{"retCode":"0000","retMsg":"操作完成","sysMerchNo":"152016111501115","tranNo":"20161130105342090110091863","outOrderNo":"q40fdye2Q3rKTPf","orderStatus":"05","tranReqNo":"2000101839","tranDesc":"系统忙,请重试。","tranAttr":"NATIVE"}
//				//{"retCode":"0000","retMsg":"操作完成","sysMerchNo":"152016111501115","tranNo":"20161201075452090110102691","outOrderNo":"q45muJJ2Q3rKTP5","orderStatus":"05","tranReqNo":"2000112667","tranDesc":"交易时间段不允许","tranAttr":"NATIVE"}
//				if("05".equals(jsonObject.getString("orderStatus"))){
//					if(tranDesc!=null&&tranDesc.indexOf("系统忙,请重试")!=-1)continue;//【系统忙,请重试】就重新下单
//				}
//				if(!"0000".equals(jsonObject.getString("retCode"))||
//						jsonObject.getString("codeUrl")==null||jsonObject.getString("codeUrl").length()==0){
//					String retMsg = jsonObject.getString("retMsg");
//					if(jsonObject.getString("codeUrl")==null&&tranDesc!=null)throw new Exception(tranDesc);
//					else throw new Exception(retMsg==null||retMsg.trim().length()==0?tranDesc:retMsg);
//				}
//				payOrder.bankjrnno=jsonObject.getString("tranNo");//雅酷返回的流水号。
//				payOrder.filed5 = "";//SIGNPAY
//				if(payOrder.payordno.equals(oldOrderNo))dao.updatePayOrder(payOrder);
//				else {
//					dao.updatePayOrder(payOrder, oldOrderNo);
//				}
//				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
//						"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
//						"codeUrl=\""+jsonObject.getString("codeUrl")+"\" respCode=\"000\" respDesc=\"处理成功\"/>";
//			}
//			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
//				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
//				"codeUrl=\"\" respCode=\"-1\" respDesc=\"YC渠道错误\"/>";
//		} catch(Exception e){
//			e.printStackTrace();
//			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
//				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
//				"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
//		}
//	}
//	
//	/**
//	 * 
//	 * @param request
//	 * @param payRequest
//	 * @param prdOrder
//	 * @param payOrder
//	 * @param productType 业务标示：选择扫码机构的编码 支付宝alipay ，微信wcpay
//	 * @return
//	 */
//	//如果由于系统错误，就持续下单三次
//	public String scanPay_New(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
//		//结算预备处理
//		readyStlInfo(payRequest,payOrder);
//		//下单处理
//		try {
//			//计算手续费
//			Map<String,PayMerchant>  merMap = new PayMerchantDAO().getSettleMentMerchant(payRequest.merchantId);//取得商户和父商户
//			PayMerchant mer = merMap.get(payRequest.merchantId);
//			PayFeeRate feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+","+("wcpay".equals(productType)?"10":"17"));//10微信扫码，17支付宝扫码 18微信WAP
//			Object [] feeInfo = PayFeeRateService.getFeeByFeeRate(feeRate,payOrder.txamt);
//			long fee = (Long)(feeInfo[0]);
//			log.info("雅酷扫码手续费==============="+fee);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
//			Map<String, Object> request_p = new HashMap<String, Object>();
//			// 公共参数
//			request_p.put("parnterId", PayConstant.PAY_CONFIG.get("YK_MERCHANT_ID"));
//			request_p.put("termNo", PayConstant.PAY_CONFIG.get("YK_TERMNO"));
//			request_p.put("merNo", PayConstant.PAY_CONFIG.get("YK_MERCHANT_ID"));
//			request_p.put("signType", "MD5");
//			request_p.put("charset", "UTF-8");
//			request_p.put("timestamp", sdf.format(new Date()));
//			request_p.put("version", "1.0");
//			//业务参数。
//			request_p.put("service", "prepay");
//			request_p.put("totalFee", payOrder.txamt); // 金额，单位：分
//			request_p.put("outTradeNo", payOrder.payordno);
//			request_p.put("notifyUrl", PayConstant.PAY_CONFIG.get("YK_NOFIFY_URL"));// 回调地址
//			request_p.put("timeStart", dateFormat.format(new Date()));
//			request_p.put("body", "Pay_WeiXinOrAlipay");
//			request_p.put("tradeType", productType); // 选择扫码机构的编码 支付宝alipay，微信wcpay
//			request_p.put("receiptCycle", "T0");// 收款人到帐周期 T0 T1，默认为T1
//			request_p.put("payeeFeeAmt", fee);// 交易手续费。 到账金额＝下单金额－交易手续费
//			// 收款人信息不提供
//			request_p.put("payeeAcctNo", payRequest.merchant.payCustStlInfo.custStlBankAcNo);// 收款人账号
//			request_p.put("payeeAcctName", payRequest.merchant.payCustStlInfo.custBankDepositName);// 收款人户名
//			request_p.put("payeeFinaName", bankName_map.get(payRequest.merchant.payCustStlInfo.depositBankCode));// 收款人银行名称
//			request_p.put("payeeIdNo",payRequest.merchant.payCustStlInfo.custStlIdno);// 收款人证件号码
//			request_p.put("payeeBankName", payRequest.merchant.payCustStlInfo.depositBankBrchName);// 收款人银行支行名称
//			request_p.put("payeeBankNo", payRequest.merchant.payCustStlInfo.custStlBankNumber);// 收款人银行行号	
//			// 发送HTTP请求
//			String payResult=buildRequest(request_p, PayConstant.PAY_CONFIG.get("YK_PAY_URL"));
//			log.info("下单返回响应: "+payResult);
//			JSONObject jsonObject = JSON.parseObject(payResult);
//			String retCode = jsonObject.getString("retCode");//请求响应码。
//			if("00".equals(retCode)){
//				if("00".equals(jsonObject.getString("resultCode"))){
//					String codeUrl = jsonObject.getString("codeUrl");//二维码路劲。
//					byte[] utf8 = codeUrl.getBytes("utf-8");
//					codeUrl = new String(utf8, "utf-8");
//					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
//					"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
//					"codeUrl=\""+codeUrl+"\" respCode=\"000\" respDesc=\"处理成功\"/>";
//				}else{
//					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
//							"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
//							"codeUrl=\"\" respCode=\"-1\" respDesc=\""+jsonObject.getString("retMsg")+"\"/>";
//				}
//			}else{
//				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
//						"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
//						"codeUrl=\"\" respCode=\"-1\" respDesc=\""+jsonObject.getString("retMsg")+"\"/>";
//			}
//		} catch(Exception e){
//			e.printStackTrace();
//			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
//				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
//				"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
//		}
//		
//	}
//	/**
//	 * 渠道补单
//	 * @param payordno 订单号
//	 * @throws Exception 异常捕获
//	 */
//	public void channelQuery_New(PayOrder payOrder) throws Exception{
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
//			Map<String, Object> request_p = new HashMap<String, Object>();
//			// 公共参数
//			request_p.put("parnterId", PayConstant.PAY_CONFIG.get("YK_MERCHANT_ID"));
//			request_p.put("termNo", PayConstant.PAY_CONFIG.get("YK_TERMNO"));
//			request_p.put("merNo", PayConstant.PAY_CONFIG.get("YK_MERCHANT_ID"));
//			request_p.put("signType", "MD5");
//			request_p.put("charset", "UTF-8");
//			request_p.put("timestamp", sdf.format(new Date()));
//			request_p.put("version", "1.0");
//			//业务参数。
//			request_p.put("service", "perpayQuery");
//			request_p.put("orderNo", "");
//			request_p.put("outTradeNo", payOrder.payordno);
//			//payType支付方式，00 支付账户  01 网上银行 03 快捷支付 07微信扫码 10微信WAP、11支付宝扫码
//			request_p.put("tradeType", "07".equals(payOrder.paytype)?"wcpay":"alipay"); // 选择扫码机构的编码 支付宝alipay，微信wcpay
//			// 发送HTTP请求
//			String payResult=buildRequest(request_p, PayConstant.PAY_CONFIG.get("YK_PAY_URL"));
//			log.info("查询返回响应: "+payResult);
//			JSONObject jsonObject = JSON.parseObject(payResult);
//			if("01".equals(jsonObject.getString("orderStatus"))){
//				payOrder.ordstatus="01";//支付成功
//	        	new NotifyInterface().notifyMer(payOrder);//支付成功
//			}
//			else throw new Exception("支付渠道状态："+
//					(status_map.get(jsonObject.getString("orderStatus"))==null?jsonObject.getString("orderStatus"):
//						status_map.get(jsonObject.getString("orderStatus"))));
//		} catch (Exception e){
//			e.printStackTrace();
//			throw e;
//		}
//	}
//	/**
//	 * 渠道补单
//	 * @param payordno 订单号
//	 * @throws Exception 异常捕获
//	 */
//	public void channelQuer(PayOrder payOrder) throws Exception{
//		try {
//			Map<String, String> param = new LinkedHashMap<String, String>();
//			param.put("service", "query");
//			param.put("signType", "MD5");
//			param.put("inputCharset", "UTF-8");
//			param.put("sysMerchNo", PayConstant.PAY_CONFIG.get("PAY_YK_MERCHANT_NO"));
//			param.put("outOrderNo", payOrder.payordno);
//			param.put("tranNo", payOrder.bankjrnno);
//			// 数据的签名字符串
//			String sign = SignUtils.getSign(param, PayConstant.PAY_CONFIG.get("PAY_YK_KEY"), "UTF-8");
//			param.put("sign", sign);
//			String result = HttpClientUtils.submitPost(PayConstant.PAY_CONFIG.get("PAY_YK_QUERY_URL"), param);
//			log.info("雅酷查询返回参数"+ new String(result.getBytes("gbk"),"utf-8"));
//			payOrder.actdat = new Date();
//			JSONObject jsonObject = JSON.parseObject( new String(result.getBytes("gbk"),"utf-8"));
//			if("0000".equals(jsonObject.getString("retCode"))&&"02".equals(jsonObject.getString("orderStatus"))){
//				payOrder.ordstatus="01";//支付成功
//	        	payOrder.bankjrnno = jsonObject.getString("tranNo");
//	        	new NotifyInterface().notifyMer(payOrder);//支付成功
//			} else throw new Exception("支付渠道状态："+
//					(status_map.get(jsonObject.getString("orderStatus"))==null?jsonObject.getString("orderStatus"):
//						status_map.get(jsonObject.getString("orderStatus"))));
//		} catch (Exception e){
//			e.printStackTrace();
//			throw e;
//		}
//	}
//	public String buildRequest(Map<String, Object> sParaTemp, String url) throws Exception {
//		// 生成签名结果
//		String mysign = getMD5Sign(sParaTemp,PayConstant.PAY_CONFIG.get("YK_KEY"));
//		// 除去数组中的空值和签名参数
//		Map<String, Object> HeadPostParam = paraFilter(sParaTemp);
//		// 签名结果加入请求提交参数组中
//		HeadPostParam.put("sign", mysign);
//		
//		//封装发送的参数
//		String params = "";
//		Iterator<String> it = HeadPostParam.keySet().iterator();
//		while(it.hasNext()){
//			String key = it.next();
//			params = params+key+"="+HeadPostParam.get(key)+ "&";
//		}
//		params = params.substring(0,params.length()-1);
//		log.info("请求报文: "+params);
//		// 运行到此处请求参数（sPara）已组装完成，下面HTTP请求工具类不再提供。
//		String payResult = new String(new DataTransUtil().doPost(url,params.getBytes("utf-8")),"utf-8");
//		return payResult;
//	}
//
//	public  String getMD5Sign(Map<String, Object> sParaTemp, String key) {
//		// 除去数组中的空值和签名参数
//		Map<String, Object> sPara = paraFilter(sParaTemp);
//		// 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
//		String prestr = createLinkString(sPara);
//		// 将排序后的加平台提供的加密字符串转char类型
//		char[] charSign = (prestr + key).toCharArray();
//		// 升序排列
//		Arrays.sort(charSign);
//		// 升序后转换String
//		String stringSign = new String(charSign);
//		// 转小写加密
//		String mySign = DigestUtils.md5Hex(stringSign.toLowerCase());
//
//		return mySign;
//	}
//
//	public   Map<String, Object> paraFilter(Map<String, Object> sArray) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		if (sArray == null || sArray.size() <= 0) {
//			return result;
//		}
//
//		for (String key : sArray.keySet()) {
//			Object value = sArray.get(key);
//			if (value == null || value.equals("")|| key.equalsIgnoreCase("sign_key")) {
//				continue;
//			}
//			result.put(key, value);
//		}
//		return result;
//	}
//
//	public  String createLinkString(Map<String, Object> params) {
//		List<String> keys = new ArrayList<String>(params.keySet());
//
//		Collections.sort(keys);
//
//		String prestr = "";
//
//		for (int i = 0; i < keys.size(); i++) {
//			String key = keys.get(i);
//			Object value = params.get(key);
//
//			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
//				prestr = prestr + key + "=" + value;
//			} else {
//				prestr = prestr + key + "=" + value + "&";
//			}
//		}
//		return prestr;
//	}
//	
//	
//	/**
//	 * 结算额检查，准备
//	 * @param payOrder
//	 */
//	private void readyStlInfo(PayRequest payRequest,PayOrder order) {
//		PayYakuStlAccDAO dao = new PayYakuStlAccDAO();
//		try {
//			//取得此商户所有结算卡
//			Map<String,String> accInfo = dao.getAccInfo(order);
//			//原始的卡号，对私
//			if(PayCardBinService.getCardBinByCardNo(
//					payRequest.merchant.payCustStlInfo.custStlBankAcNo)!=null){
//				accInfo.put(payRequest.merchant.payCustStlInfo.custStlBankAcNo,"");
//			}
//			//取得此商户当日结算信息
//			Map<String,PayYakuStlAccCache> accStlInfo = dao.getAccStlInfo(order);
//			//检查当日结算信息是否存在，不存在的，添加
//			List<String> list = new ArrayList<String>();
//			Iterator<String> it = accInfo.keySet().iterator();
//			while(it.hasNext()){
//				String accNo = it.next();
//				if(accStlInfo.get(accNo)==null)list.add(accNo);
//			}
//			//添加当天结算卡信息
//			if(list.size()>0)dao.addAccStlInfo(order,list);
//			//取得当前金额结算最小的卡
//			String minAmtAccNo = dao.getMinStlAmtAccNo(order);
//			//保存订单结算信息
//			if(minAmtAccNo != null)dao.saveAccStlInfo(order,minAmtAccNo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 保存成功支付订单结算额
//	 * @param order
//	 */
//	public void saveStlAmt(PayOrder order) {
//		try {
//			//交易成功，添加结算金额到指定卡号
//			if("01".equals(order.ordstatus)){
//				PayYakuStlAccDAO dao = new PayYakuStlAccDAO();
//				//通过订单号取得结算卡号
//				String accNo = dao.getAccNoByPayordno(order);
//				//更新当天此卡号交易额
//				dao.updateStlAmt(order,accNo);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
class ChangeChannelToYK extends TimerTask{
	@Override
	public void run() {
		//取得渠道信息-老的保存信息
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("channel_yk_info.txt")));
			String line = null;
			List list = new ArrayList();
			while((line = br.readLine())!=null){
				if(line.trim().length()>0)list.add(line.trim());
			}
			//修改渠道信息为雅酷
			new PayYakuStlAccDAO().updateChannelToYK(list);
			PayCoopBankService.loadMerchantChannelRelation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
class ChangeChannelToZX extends TimerTask{
	@Override
	public void run() {
		try {
			PayYakuStlAccDAO dao = new PayYakuStlAccDAO();
			//取得渠道信息-雅酷
			List<String> list = dao.getChannelYK();
			//保存老的渠道信息
			FileOutputStream fos = new FileOutputStream("channel_yk_info.txt");
			for(int i=0; i<list.size(); i++)fos.write((list.get(i)+"\n").getBytes());
			fos.close();
			//修改渠道信息为微信
			dao.updateChannelToZX();
			PayCoopBankService.loadMerchantChannelRelation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
