package com.pay.merchantinterface.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import util.JWebConstant;
import util.PayUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.cfca.util.pki.api.CertUtil;
import com.cfca.util.pki.api.KeyUtil;
import com.cfca.util.pki.api.SignatureUtil;
import com.cfca.util.pki.cert.X509Cert;
import com.cfca.util.pki.cipher.JCrypto;
import com.cfca.util.pki.cipher.JKey;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.order.service.PayOrderService;
import com.third.yb.CallbackUtils;
import com.third.yb.Digest;
import com.third.yb.DigestUtil;
import com.third.yb.HttpUtils;

/**
 * 易宝
 * @author lqq
 */
public class YBPayService {

	private static final Log log = LogFactory.getLog(YBPayService.class);
	
	public static Map <String,String> PAY_BANK_MAP_JIEJI = new HashMap <String,String>();//借记卡银行码映射（网关）
	
	static {
		//网银支持银行
		PAY_BANK_MAP_JIEJI.put("ICBC", "ICBC-NET-B2C");//1工商银行
		PAY_BANK_MAP_JIEJI.put("CMB", "CMBCHINA-NET-B2C");//2招商银行
		PAY_BANK_MAP_JIEJI.put("CCB", "CCB-NET-B2C");//3建设银行
		PAY_BANK_MAP_JIEJI.put("BOCOM", "BOCO-NET-B2C");//4交通银行
		PAY_BANK_MAP_JIEJI.put("CIB", "CIB-NET-B2C");//5兴业银行  
		PAY_BANK_MAP_JIEJI.put("CMBC", "CMBC-NET-B2C");//6民生银行
		PAY_BANK_MAP_JIEJI.put("CEB", "CEB-NET-B2C");//7光大银行  
		PAY_BANK_MAP_JIEJI.put("BOC", "BOC-NET-B2C");//8中国银行
		PAY_BANK_MAP_JIEJI.put("PAB", "PINGANBANK-NET-B2C");//9平安银行  
		PAY_BANK_MAP_JIEJI.put("CNCB", "ECITIC-NET-B2C");//10中信银行  
		PAY_BANK_MAP_JIEJI.put("GDB", "GDB-NET-B2C");//10广发银行  
		PAY_BANK_MAP_JIEJI.put("BOS", "SHB-NET-B2C");//11上海银行  
		PAY_BANK_MAP_JIEJI.put("SPDB", "SPDB-NET-B2C");//12上海浦东发展银行  
		PAY_BANK_MAP_JIEJI.put("HXB", "HXB-NET-B2C");//13华夏银行  ----------
		PAY_BANK_MAP_JIEJI.put("BCCB", "BCCB-NET-B2C");//14北京银行  ---------
		PAY_BANK_MAP_JIEJI.put("ABC", "ABC-NET-B2C");//15中国农业银行
		PAY_BANK_MAP_JIEJI.put("PSBC", "POST-NET-B2C");//16中国邮政储蓄银行  
	}
	
	/**
	 * 收银台下单
	 * @param request
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public void pay(HttpServletRequest request, PayProductOrder prdOrder,PayOrder payOrder) throws Exception{
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YB"); // 支付渠道
		payOrder.bankcod = request.getParameter("banks"); // 银行代码
		payOrder.bankCardType = String.valueOf(Long.parseLong(request.getParameter("bankCardType")));//银行卡类型
		try {
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) bankCode = PAY_BANK_MAP_JIEJI.get(payOrder.bankcod);
			if (bankCode == null) throw new Exception("易宝无对应银行（"+payOrder.bankcod+"）");
			//拼接加密字段
			String p0_Cmd = "Buy";//业务类型
			String p1_MerId = PayConstant.PAY_CONFIG.get("YB_MERNO");//商户号
			String p2_Order = payOrder.payordno;//订单号
			String p3_Amt = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额
			String p4_Cur = "CNY";//交易币种
			String p5_Pid = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String p7_Pdesc = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品描述
			String p8_Url = PayConstant.PAY_CONFIG.get("YB_NOTIFYURL");//通知地址
			String pd_FrpId = bankCode;//银行代码
			String keyValue = PayConstant.PAY_CONFIG.get("YB_MER_PRIVATEKEY"); // key
			String[] strArr	= new String[] {p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p7_Pdesc, p8_Url,  pd_FrpId};
			String hmac	= DigestUtil.getHmac(strArr, keyValue);
			Map<String, String> paramsMap = new LinkedHashMap<String, String>();
			paramsMap.put("p0_Cmd", 	p0_Cmd);
			paramsMap.put("p1_MerId", 	p1_MerId);
			paramsMap.put("p2_Order",	p2_Order);
			paramsMap.put("p3_Amt",	p3_Amt);
			paramsMap.put("p4_Cur",	p4_Cur);
			paramsMap.put("p5_Pid",	p5_Pid);
			paramsMap.put("p7_Pdesc",	p7_Pdesc);
			paramsMap.put("p8_Url",	p8_Url);
			paramsMap.put("pd_FrpId",	pd_FrpId);
			paramsMap.put("hmac", hmac);
			request.setAttribute("paramsMap", paramsMap);
			log.info("易宝网银下单请求数据："+paramsMap.toString());
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
		payOrder.payChannel = PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YB"); // 支付渠道
		payOrder.bankcod = payRequest.bankId; // 银行代码
		payOrder.bankCardType = payRequest.accountType;//卡类型
		try {
			String bankCode = null;
			if(PayConstant.CARD_BIN_TYPE_JIEJI.equals(payOrder.bankCardType)) bankCode = PAY_BANK_MAP_JIEJI.get(payOrder.bankcod);
			if (bankCode == null) throw new Exception("易宝无对应银行（"+payOrder.bankcod+"）");
			//拼接加密字段
			String p0_Cmd = "Buy";//业务类型
			String p1_MerId = PayConstant.PAY_CONFIG.get("YB_MERNO");//商户号
			String p2_Order = payOrder.payordno;//订单号
			String p3_Amt = String.format("%.2f", ((double)payOrder.txamt)/100d);//交易金额
			String p4_Cur = "CNY";//交易币种
			String p5_Pid = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			String p7_Pdesc = PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品描述
			String p8_Url = PayConstant.PAY_CONFIG.get("YB_NOTIFYURL");//通知地址
			String pd_FrpId = bankCode;//银行代码
			String keyValue = PayConstant.PAY_CONFIG.get("YB_MER_PRIVATEKEY"); // key
			String[] strArr	= new String[] {p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p7_Pdesc, p8_Url, pd_FrpId};
			String hmac	= DigestUtil.getHmac(strArr, keyValue);
			Map<String, String> paramsMap = new LinkedHashMap<String, String>();
			paramsMap.put("p0_Cmd", p0_Cmd);
			paramsMap.put("p1_MerId", p1_MerId);
			paramsMap.put("p2_Order", p2_Order);
			paramsMap.put("p3_Amt", p3_Amt);
			paramsMap.put("p4_Cur", p4_Cur);
			paramsMap.put("p5_Pid", p5_Pid);
			paramsMap.put("p7_Pdesc", p7_Pdesc);
			paramsMap.put("p8_Url", p8_Url);
			paramsMap.put("pd_FrpId", pd_FrpId);
			paramsMap.put("hmac", hmac);
			request.setAttribute("paramsMap", paramsMap);
			log.info("易宝网银下单请求数据："+paramsMap.toString());
			new PayOrderService().updateOrderForBanks(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 渠道补单（查单）
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		//请求参数
		String p0_Cmd = "QueryOrdDetail";//业务类型
		String p1_MerId = PayConstant.PAY_CONFIG.get("YB_MERNO");//商户号
		String p2_Order = payOrder.payordno;//商户订单号
		String pv_Ver = "3.0";//版本
		String p3_ServiceType = "2";//查询类型
		String keyValue = PayConstant.PAY_CONFIG.get("YB_MER_PRIVATEKEY"); // key
		String[] strArr	= {p0_Cmd, p1_MerId, p2_Order, pv_Ver, p3_ServiceType};
		String hmac	= DigestUtil.getHmac(strArr, keyValue);//hmac
		
		String queryURL = PayConstant.PAY_CONFIG.get("YB_queryURL");//查询地址
		Map<String, String> queryParams	= new HashMap<String, String>();
		queryParams.put("p0_Cmd", p0_Cmd);
		queryParams.put("p1_MerId", p1_MerId);
		queryParams.put("p2_Order", p2_Order);
		queryParams.put("pv_Ver", pv_Ver);
		queryParams.put("p3_ServiceType", p3_ServiceType);
		queryParams.put("hmac", hmac);
		String r0_Cmd					= "";
		String r1_Code              	= "";
		String r2_TrxId             	= "";
		String r3_Amt               	= "";
		String r4_Cur               	= "";
		String r5_Pid               	= "";
		String r6_Order             	= "";
		String r8_MP                	= "";
		String rw_RefundRequestID   	= "";
		String rx_CreateTime        	= "";
		String ry_FinshTime        		= "";
		String rz_RefundAmount      	= "";
		String rb_PayStatus         	= "";
		String rc_RefundCount       	= "";
		String rd_RefundAmt         	= "";
		String hmacFromYeepay			= "";
		String hmac_safeFromYeepay		= "";
		List responseList		= null;
		log.info("易宝查单请求数据："+JSON.toJSONString(queryParams));
		responseList = HttpUtils.URLGet(queryURL, queryParams);
		log.info("易宝查单返回数据："+JSON.toJSONString(responseList));
		if(responseList == null) {
			throw new Exception("易宝查单失败！");
		} else {
			Iterator iter	= responseList.iterator();
			while(iter.hasNext()) {
				String temp = formatString((String)iter.next());
				if(temp.equals("")) {
					continue;
				}
				int i = temp.indexOf("=");
				int j = temp.length();
				if(i >= 0) {
					String tempKey		= temp.substring(0, i);
					String tempValue	= URLDecoder.decode(temp.substring(i+1, j),"GBK");
					if("r0_Cmd".equals(tempKey)) {
						r0_Cmd		= tempValue;
					} else if("r1_Code".equals(tempKey)) {
						r1_Code		= tempValue;
					} else if("r2_TrxId".equals(tempKey)) {
						r2_TrxId	= tempValue;
					} else if("r3_Amt".equals(tempKey)) {
						r3_Amt		= tempValue;
					} else if("r4_Cur".equals(tempKey)) {
						r4_Cur		= tempValue;
					} else if("r5_Pid".equals(tempKey)) {
						r5_Pid		= tempValue;						
					} else if("r6_Order".equals(tempKey)) {
						r6_Order	= tempValue;
					} else if("r8_MP".equals(tempKey)) {
						r8_MP		= tempValue;
					} else if("rw_RefundRequestID".equals(tempKey)) {
						rw_RefundRequestID	= tempValue;
					} else if("rx_CreateTime".equals(tempKey)) {
						rx_CreateTime		= tempValue;
					} else if("ry_FinshTime".equals(tempKey)) {
						ry_FinshTime		= tempValue;
					} else if("rz_RefundAmount".equals(tempKey)) {
						rz_RefundAmount		= tempValue;
					} else if("rb_PayStatus".equals(tempKey)) {
						rb_PayStatus		= tempValue;
					} else if("rc_RefundCount".equals(tempKey)) {
						rc_RefundCount		= tempValue;
					} else if("rd_RefundAmt".equals(tempKey)) {
						rd_RefundAmt		= tempValue;
					} else if("hmac".equals(tempKey)) {
						hmacFromYeepay		= tempValue;
					} else if("hmac_safe".equals(tempKey)){
						hmac_safeFromYeepay	= tempValue;
					}
				}
			}
			String[] stringArr	= {r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r8_MP, 
								rw_RefundRequestID, rx_CreateTime, ry_FinshTime, rz_RefundAmount, rb_PayStatus,
								rc_RefundCount, rd_RefundAmt};
			String localHmac	= DigestUtil.getHmac(stringArr, keyValue);
			boolean ishmac_safe = verifyCallbackHmac_safe(stringArr, hmac_safeFromYeepay);
			if(localHmac.equals(hmacFromYeepay) && ishmac_safe) {
				log.info("易宝验签成功");
				if("1".equals(r1_Code)){//查询正常
					if("SUCCESS".equals(rb_PayStatus)){
						payOrder.ordstatus="01";//支付成功
						new NotifyInterface().notifyMer(payOrder);//支付成功
					}
				}
			}else new Exception("易宝验签失败！");
		}
	}
	
	/**
	 * 单笔代付
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingle(PayRequest payRequest) throws Exception{
		 Map xmlBackMap = new LinkedHashMap();
		 String [] backDigestValues = "cmd,ret_Code,mer_Id,batch_No,total_Amt,total_Num,r1_Code,hmacKey".split(",");
		 try {
			 PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//获取单笔代收付object
			 rp.setCreateTime(new Date());
			 PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取银行卡信息
			 if(cardBin == null) throw new Exception("无法识别银行账号"); 
			 //商户常量
			 String hmacKey = PayConstant.PAY_CONFIG.get("YB_MER_PRIVATEKEY");//商户私钥
			 String df_url = PayConstant.PAY_CONFIG.get("YB_DF_URL");//代付地址
			 //请求参数
			 String cmd = "TransferSingle";//命令
			 String version = "1.1";//版本
			 String group_Id = PayConstant.PAY_CONFIG.get("YB_MERNO");//总公司商户编号
			 String mer_Id = PayConstant.PAY_CONFIG.get("YB_MERNO");//实际交易商户号
			 String tmp = rp.id;
			 String batch_No = rp.id.substring(tmp.length()-14,tmp.length());
			 rp.channelTranNo=batch_No;
			 String bank_Code = cardBin.bankCode;//收款银行编号
			 String order_Id = rp.id;//订单号
			 String amount = String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d);//代付金额，单文“元”
			 String account_Name = rp.accountName;//收款帐户的开户名称
			 String account_Number = rp.accountNo;//收款帐户的帐户号
	         String account_Type = "";//代付类型
	         if ("0".equals(rp.accountProp)) {//"0",对私
	        	 account_Type = "pr";//对私
	         } else {
	        	 account_Type = "pu";
	         }
	         String fee_Type = PayConstant.PAY_CONFIG.get("YB_DF_FEETYPE");//手续费收取类型
			 String urgency = PayConstant.PAY_CONFIG.get("YB_DF_SSPAY");//是否实时付款
			 //cmd,mer_Id,batch_No,order_Id,amount,account_Number,hmacKey
			 String hmacStr = cmd + mer_Id + batch_No + order_Id + amount + account_Number + hmacKey;
			 log.info("易宝加密之前的数据：命令："+cmd+",商户号："+mer_Id+",批次号："+batch_No+",订单号："+order_Id+",金额（元）："+amount+",账户号："+account_Number+",hmac："+hmacKey);
			//下面用数字证书进行签名
			com.cfca.util.pki.cipher.Session tempsession = null;
			String ALGORITHM = SignatureUtil.SHA1_RSA;
			JCrypto jcrypto =null;
			if(tempsession==null){
				try {
			        //初始化加密库，获得会话session
				    //多线程的应用可以共享一个session,不需要重复,只需初始化一次
				    //初始化加密库并获得session。
				    //系统退出后要jcrypto.finalize()，释放加密库
			        jcrypto = JCrypto.getInstance();
			        jcrypto.initialize(JCrypto.JSOFT_LIB, null);
			        tempsession = jcrypto.openSession(JCrypto.JSOFT_LIB);
			    } catch (Exception ex) {
			        System.out.println(ex.toString());
			    }
			}
			String certPath = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("YB_DF_MER_CER");//证书路径
			String certPassword = PayConstant.PAY_CONFIG.get("YB_DF_MER_PWD");//证书密码
			JKey jkey = KeyUtil.getPriKey(certPath,certPassword);
			X509Cert cert = CertUtil.getCert(certPath, certPassword);
			X509Cert[] cs=new X509Cert[1];
			cs[0]=cert;
			String signMessage ="";
			SignatureUtil signUtil =null;
			try {
				// 第二步:对请求的串进行MD5对数据进行签名
				String yphs = Digest.hmacSign(hmacStr);
				signUtil = new SignatureUtil();
				byte[] b64SignData;
				// 第三步:对MD5签名之后数据调用CFCA提供的api方法用商户自己的数字证书进行签名
				b64SignData = signUtil.p7SignMessage(true, yphs.getBytes(),ALGORITHM, jkey, cs, tempsession);
				 if(jcrypto!=null){
			         jcrypto.finalize (com.cfca.util.pki.cipher.JCrypto.JSOFT_LIB,null);
			     }
				signMessage = new String(b64SignData, "UTF-8");
				log.info("易宝加密之后的数据："+signMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//创建请求的xml
			Element root = DocumentHelper.createElement("data");
			Document document = DocumentHelper.createDocument(root);
			root.addElement("cmd").addText(cmd);
			root.addElement("version").addText(version);
			root.addElement("mer_Id").addText(mer_Id);
			root.addElement("group_Id").addText(group_Id);
			root.addElement("batch_No").addText(batch_No);
			root.addElement("order_Id").addText(order_Id);
			root.addElement("bank_Code").addText(bank_Code);
			root.addElement("cnaps");//银联号
			root.addElement("bank_Name");//银行名称
			root.addElement("branch_Bank_Name");//支行
			root.addElement("amount").addText(amount);
			root.addElement("account_Name").addText(account_Name);
			root.addElement("account_Number").addText(account_Number);
			root.addElement("account_Type").addText(account_Type);
			root.addElement("province");
			root.addElement("city");
			root.addElement("fee_Type").addText(fee_Type);
			root.addElement("payee_Email");
			root.addElement("payee_Mobile");
			root.addElement("leave_Word");
			root.addElement("abstractInfo");
			root.addElement("remarksInfo");
			root.addElement("urgency").addText(urgency);
			root.addElement("hmac").addText(signMessage);
			document.setXMLEncoding("GBK");
			//第四步:发送https请求
			String responseMsg = CallbackUtils.httpRequest(df_url, document.asXML(), "POST", "gbk","text/xml ;charset=gbk", false);
			log.info("易宝代付服务器响应:"+ responseMsg);
			try {
				document = DocumentHelper.parseText(responseMsg);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			root = document.getRootElement();
			String cmdValue = root.elementText("hmac");
			//第五步:对服务器响应报文进行验证签名
			boolean sigerCertFlag = false;
			if(cmdValue!=null && cmdValue != ""){
				sigerCertFlag = signUtil.p7VerifySignMessage(cmdValue.getBytes(), tempsession);
				String backmd5hmac = "";
				if(sigerCertFlag){
					log.info("易宝代付：证书验签成功");
					backmd5hmac = new String(signUtil.getSignedContent());
					//第六步.将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
					Document backDocument = null;
					try {
						backDocument = DocumentHelper.parseText(responseMsg);
					} catch (DocumentException e) {
						e.printStackTrace();
					} 
					Element backRootEle = backDocument.getRootElement();
					List backlist = backRootEle.elements();
					for(int i = 0; i < backlist.size(); i++){
						Element ele = (Element)backlist.get(i);
						String eleName = ele.getName();
						if(!eleName.equals("list")){
							xmlBackMap.put(eleName, ele.getText().trim());
						}else{
							continue;
						}
					}
					String backHmacStr="";
					for(int i = 0; i < backDigestValues.length;i++){
						if(backDigestValues[i].equals("hmacKey")){
							backHmacStr = backHmacStr + hmacKey;
							continue;
						}
						String tempStr = (String)xmlBackMap.get(backDigestValues[i]);
						backHmacStr = backHmacStr + ((tempStr == null) ? "" : tempStr);
					}
					String newmd5hmac = Digest.hmacSign(backHmacStr);
					if(newmd5hmac.equals(backmd5hmac)){
						log.info("易宝代付：md5验签成功");
						//第七步:判断该证书DN是否为易宝
						if(signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf("OU=YEEPAY,") > 0){
							log.info("易宝代付：证书DN是易宝的");
							//第八步:.......加上业务逻辑
							if("1".equals(xmlBackMap.get("ret_Code"))){//请求成功
								 rp.status="0";
				        		 rp.errorMsg = "操作成功";
				        		 try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){};
				        		 YBQueryThread emaxQueryThread = new YBQueryThread(payRequest);
				        		 emaxQueryThread.start();
							}else {
								rp.status="2";
								rp.errorMsg = "操作失败";
					        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
							}
						}else{
							log.info("证书DN不是易宝的");
						}
					}else{
						log.info("md5验签失败");
					}
					
				}
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
			Map xmlBackMap = new LinkedHashMap();
			String [] backDigestValues = "cmd,ret_Code,batch_No,total_Num,end_Flag,hmacKey".split(",");
			//商户常量
			String hmacKey = PayConstant.PAY_CONFIG.get("YB_MER_PRIVATEKEY");//商户私钥
			String url = PayConstant.PAY_CONFIG.get("YB_DF_QUERY_URL");//代付地址

			//请求参数
			String cmd = "BatchDetailQuery";//命令
			String version = "1.1";//版本
			String group_Id = PayConstant.PAY_CONFIG.get("YB_MERNO");//总公司商户编号
			String mer_Id = PayConstant.PAY_CONFIG.get("YB_MERNO");//实际交易商户号
			String query_Mode = "1";//查询模式1查询本公司自己发的交易
			String batch_No = new Date().getTime()+"00";//打款批次号
			String order_Id = rp.id;//订单号
			String page_No = "1";
			String hmacStr = cmd + mer_Id + batch_No + order_Id + page_No + hmacKey;
			log.info("易宝加密之前的数据：命令："+cmd+",商户号："+mer_Id+",批次号："+batch_No+",订单号："+order_Id+",页号："+page_No+",hamc："+hmacKey);
			//下面用数字证书进行签名
			com.cfca.util.pki.cipher.Session tempsession = null;
			String ALGORITHM = SignatureUtil.SHA1_RSA;
			JCrypto jcrypto =null;
			if(tempsession==null){
				try {
			        //初始化加密库，获得会话session
				    //多线程的应用可以共享一个session,不需要重复,只需初始化一次
				    //初始化加密库并获得session。
				    //系统退出后要jcrypto.finalize()，释放加密库
			        jcrypto = JCrypto.getInstance();
			        jcrypto.initialize(JCrypto.JSOFT_LIB, null);
			        tempsession = jcrypto.openSession(JCrypto.JSOFT_LIB);
			    } catch (Exception ex) {
			        System.out.println(ex.toString());
			    }
			}
			String certPath = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("YB_DF_MER_CER");//证书路径
			String certPassword = PayConstant.PAY_CONFIG.get("YB_DF_MER_PWD");//证书密码
			JKey jkey = KeyUtil.getPriKey(certPath,certPassword);
			X509Cert cert = CertUtil.getCert(certPath, certPassword);
			X509Cert[] cs=new X509Cert[1];
			cs[0]=cert;
			String signMessage ="";
			SignatureUtil signUtil =null;
			try {
				// 第二步:对请求的串进行MD5对数据进行签名
				String yphs = Digest.hmacSign(hmacStr);
				signUtil = new SignatureUtil();
				byte[] b64SignData;
				// 第三步:对MD5签名之后数据调用CFCA提供的api方法用商户自己的数字证书进行签名
				b64SignData = signUtil.p7SignMessage(true, yphs.getBytes(),ALGORITHM, jkey, cs, tempsession);
				 if(jcrypto!=null){
			         jcrypto.finalize (com.cfca.util.pki.cipher.JCrypto.JSOFT_LIB,null);
			     }
				signMessage = new String(b64SignData, "UTF-8");
				log.info("易宝加密之后的数据："+signMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//创建请求的xml
			Element root = DocumentHelper.createElement("data");
			Document document = DocumentHelper.createDocument(root);
			root.addElement("cmd").addText(cmd);
			root.addElement("version").addText(version);
			root.addElement("group_Id").addText(group_Id);
			root.addElement("mer_Id").addText(mer_Id);
			root.addElement("query_Mode").addText(query_Mode);
			root.addElement("batch_No").addText(batch_No);
			root.addElement("order_Id").addText(order_Id);
			root.addElement("page_No").addText(page_No);
			root.addElement("hmac").addText(signMessage);
			document.setXMLEncoding("GBK");
			//第四步:发送https请求
			String responseMsg = CallbackUtils.httpRequest(url, document.asXML(), "POST", "gbk","text/xml ;charset=gbk", false);
			log.info("易宝代付服务器响应:"+ responseMsg);
			try {
				document = DocumentHelper.parseText(responseMsg);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			root = document.getRootElement();
			String cmdValue = root.elementText("hmac");
			//第五步:对服务器响应报文进行验证签名
			boolean sigerCertFlag = false;
			if(cmdValue!=null && cmdValue != ""){
				sigerCertFlag = signUtil.p7VerifySignMessage(cmdValue.getBytes(), tempsession);
				String backmd5hmac = "";
				if(sigerCertFlag){
					log.info("易宝代付：证书验签成功");
					backmd5hmac = new String(signUtil.getSignedContent());
					//第六步.将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
					Document backDocument = null;
					try {
						backDocument = DocumentHelper.parseText(responseMsg);
					} catch (DocumentException e) {
						e.printStackTrace();
					} 
					Element backRootEle = backDocument.getRootElement();
					List backlist = backRootEle.elements();
					for(int i = 0; i < backlist.size(); i++){
						Element ele = (Element)backlist.get(i);
						String eleName = ele.getName();
						if(!eleName.equals("list")){
							xmlBackMap.put(eleName, ele.getText().trim());
						}else{
							continue;
						}
					}
					String backHmacStr="";
					for(int i = 0; i < backDigestValues.length;i++){
						if(backDigestValues[i].equals("hmacKey")){
							backHmacStr = backHmacStr + hmacKey;
							continue;
						}
						String tempStr = (String)xmlBackMap.get(backDigestValues[i]);
						backHmacStr = backHmacStr + ((tempStr == null) ? "" : tempStr);
					}
					String newmd5hmac = Digest.hmacSign(backHmacStr);
					if(newmd5hmac.equals(backmd5hmac)){
						log.info("易宝代付补单：md5验签成功");
						//第七步:判断该证书DN是否为易宝
						if(signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf("OU=YEEPAY,") > 0){
							log.info("易宝代付补单：证书DN是易宝的");
							//第八步:.......加上业务逻辑
							if("1".equals(xmlBackMap.get("ret_Code"))){//请求成功，系统返回码
								String r1Code = backRootEle.element("list").element("items").element("item").element("r1_Code").getText();//打款状态码
								String bankStatus = backRootEle.element("list").element("items").element("item").element("bank_Status").getText();//银行状态码
								String failMsg = backRootEle.element("list").element("items").element("item").element("fail_Desc").getText();//银行状态码
								if ("0026".equals(r1Code) && "S".equals(bankStatus)) {//已成功
									payRequest.setRespCode("000");
									payRequest.receiveAndPaySingle.setRespCode("000");
									payRequest.receivePayRes = "0";
									rp.errorMsg = "处理成功";
									return true;
								}else throw new Exception(failMsg);
							}else throw new Exception((String) ((xmlBackMap.get("error_Msg")!=null && xmlBackMap.get("error_Msg")!="") ? xmlBackMap.get("error_Msg"):"易宝补单失败!"));
						}else{
							log.info("易宝渠道补单：证书DN不是易宝的");
						}
					}else{
						log.info("易宝渠道补单：md5验签失败");
					}
					
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			payRequest.setRespCode("-1");
			rp.setRespCode(payRequest.respCode);
			payRequest.receivePayRes = "-1";
			payRequest.respDesc=e.getMessage();
			rp.errorMsg = payRequest.respDesc;
			return false;
		}
		return false;
	}
	 
	/**
	 * formatString(String text) : 字符串格式化方法
	 */
	public static String formatString(String text) {
		return (text == null ? "" : text.trim());
	}
	
	/**
	 * verifyCallbackHmac_safe() : 验证回调安全签名数据是否有效
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean verifyCallbackHmac_safe(String[] stringValue, String hmac_safeFromYeepay) throws UnsupportedEncodingException {
		
		String keyValue = PayConstant.PAY_CONFIG.get("YB_MER_PRIVATEKEY"); // key
		StringBuffer sourceData	= new StringBuffer();
		for(int i = 0; i < stringValue.length; i++) {
			if(!"".equals(stringValue[i])){
				sourceData.append(stringValue[i]+"#");
			}
		}
		sourceData = sourceData.deleteCharAt(sourceData.length()-1);
		String localHmac_safe		= DigestUtil.hmacSign(sourceData.toString(), keyValue);
		StringBuffer hmacSource	= new StringBuffer();
		for(int i = 0; i < stringValue.length; i++) {
			hmacSource.append(stringValue[i]);
		}
		return (localHmac_safe.equals(hmac_safeFromYeepay) ? true : false);
	}
	
}

/**
 * 查询代付 线程类
 * @author lqq
 *
 */
class YBQueryThread extends Thread {
	
	private static final Log log = LogFactory.getLog(YBQueryThread.class);
	
	private  PayRequest payRequest= new PayRequest();
	public YBQueryThread (PayRequest payRequest) {
		this.payRequest = payRequest;
	}
	
	@Override
	public void run() {
		for(int i=0; i<CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY.length; i++){
			try {if(query())break;} catch (Exception e) {}
			log.info("易宝 代付查询第"+(i+1)+"次,等待时间"+CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]+"秒");
			try {Thread.sleep(CJPayService.CJ_RECEIVE_PAY_SEARCH_TIME_FREQUENCY[i]*1000);} catch (Exception e) {}
		}
	}
	
	public boolean query()throws Exception{
		try {
			Map xmlBackMap = new LinkedHashMap();
			String [] backDigestValues = "cmd,ret_Code,batch_No,total_Num,end_Flag,hmacKey".split(",");
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;//代付对象
			
			//商户常量
			String hmacKey = PayConstant.PAY_CONFIG.get("YB_MER_PRIVATEKEY");//商户私钥
			String url = PayConstant.PAY_CONFIG.get("YB_DF_QUERY_URL");//代付地址

			//请求参数
			String cmd = "BatchDetailQuery";//命令
			String version = "1.1";//版本
			String group_Id = PayConstant.PAY_CONFIG.get("YB_MERNO");//总公司商户编号
			String mer_Id = PayConstant.PAY_CONFIG.get("YB_MERNO");//实际交易商户号
			String query_Mode = "1";//查询模式  1：查询本公司自己发的交易
			String batch_No = new Date().getTime()+"00";//打款批次号
			String order_Id = rp.id;//订单号
			String page_No = "1";
			String hmacStr = cmd + mer_Id + batch_No + order_Id + page_No + hmacKey;
			log.info("易宝加密之前的数据：命令："+cmd+",商户号："+mer_Id+",渠道交易号："+batch_No+",订单号"+order_Id+",页号："+page_No+",hmac："+hmacKey);
			//下面用数字证书进行签名
			com.cfca.util.pki.cipher.Session tempsession = null;
			String ALGORITHM = SignatureUtil.SHA1_RSA;
			JCrypto jcrypto =null;
			if(tempsession==null){
				try {
			        //初始化加密库，获得会话session
				    //多线程的应用可以共享一个session,不需要重复,只需初始化一次
				    //初始化加密库并获得session。
				    //系统退出后要jcrypto.finalize()，释放加密库
			        jcrypto = JCrypto.getInstance();
			        jcrypto.initialize(JCrypto.JSOFT_LIB, null);
			        tempsession = jcrypto.openSession(JCrypto.JSOFT_LIB);
			    } catch (Exception ex) {
			        System.out.println(ex.toString());
			    }
			}
			String certPath = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("YB_DF_MER_CER");//证书路径
			String certPassword = PayConstant.PAY_CONFIG.get("YB_DF_MER_PWD");//证书密码
			JKey jkey = KeyUtil.getPriKey(certPath,certPassword);
			X509Cert cert = CertUtil.getCert(certPath, certPassword);
			X509Cert[] cs=new X509Cert[1];
			cs[0]=cert;
			String signMessage ="";
			SignatureUtil signUtil =null;
			try {
				// 第二步:对请求的串进行MD5对数据进行签名
				String yphs = Digest.hmacSign(hmacStr);
				signUtil = new SignatureUtil();
				byte[] b64SignData;
				// 第三步:对MD5签名之后数据调用CFCA提供的api方法用商户自己的数字证书进行签名
				b64SignData = signUtil.p7SignMessage(true, yphs.getBytes(),ALGORITHM, jkey, cs, tempsession);
				 if(jcrypto!=null){
			         jcrypto.finalize (com.cfca.util.pki.cipher.JCrypto.JSOFT_LIB,null);
			     }
				signMessage = new String(b64SignData, "UTF-8");
				log.info("易宝加密之后的数据："+signMessage);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//创建请求的xml
			Element root = DocumentHelper.createElement("data");
			Document document = DocumentHelper.createDocument(root);
			root.addElement("cmd").addText(cmd);
			root.addElement("version").addText(version);
			root.addElement("group_Id").addText(group_Id);
			root.addElement("mer_Id").addText(mer_Id);
			root.addElement("query_Mode").addText(query_Mode);
			root.addElement("batch_No").addText(batch_No);
			root.addElement("order_Id").addText(order_Id);
			root.addElement("page_No").addText(page_No);
			root.addElement("hmac").addText(signMessage);
			document.setXMLEncoding("GBK");
			//第四步:发送https请求
			String responseMsg = CallbackUtils.httpRequest(url, document.asXML(), "POST", "gbk","text/xml ;charset=gbk", false);
			log.info("易宝代付服务器响应:"+ responseMsg);
			try {
				document = DocumentHelper.parseText(responseMsg);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			root = document.getRootElement();
			String cmdValue = root.elementText("hmac");
			//第五步:对服务器响应报文进行验证签名
			boolean sigerCertFlag = false;
			if(cmdValue!=null && cmdValue != ""){
				sigerCertFlag = signUtil.p7VerifySignMessage(cmdValue.getBytes(), tempsession);
				String backmd5hmac = "";
				if(sigerCertFlag){
					log.info("易宝代付查单：证书验签成功");
					backmd5hmac = new String(signUtil.getSignedContent());
					//第六步.将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
					Document backDocument = null;
					try {
						backDocument = DocumentHelper.parseText(responseMsg);
					} catch (DocumentException e) {
						e.printStackTrace();
					} 
					Element backRootEle = backDocument.getRootElement();
					List backlist = backRootEle.elements();
					for(int i = 0; i < backlist.size(); i++){
						Element ele = (Element)backlist.get(i);
						String eleName = ele.getName();
						if(!eleName.equals("list")){
							xmlBackMap.put(eleName, ele.getText().trim());
						}else{
							continue;
						}
					}
					String backHmacStr="";
					for(int i = 0; i < backDigestValues.length;i++){
						if(backDigestValues[i].equals("hmacKey")){
							backHmacStr = backHmacStr + hmacKey;
							continue;
						}
						String tempStr = (String)xmlBackMap.get(backDigestValues[i]);
						backHmacStr = backHmacStr + ((tempStr == null) ? "" : tempStr);
					}
					String newmd5hmac = Digest.hmacSign(backHmacStr);
					if(newmd5hmac.equals(backmd5hmac)){
						log.info("易宝代付查单：md5验签成功");
						//第七步:判断该证书DN是否为易宝
						if(signUtil.getSigerCert()[0].getSubject().toUpperCase().indexOf("OU=YEEPAY,") > 0){
							log.info("易宝代付查单：证书DN是易宝的");
							//第八步:.......加上业务逻辑
							if("1".equals(xmlBackMap.get("ret_Code"))){//请求成功，系统返回码
								String r1Code = backRootEle.element("list").element("items").element("item").element("r1_Code").getText();//打款状态码
								String bankStatus = backRootEle.element("list").element("items").element("item").element("bank_Status").getText();//银行状态码
								String failMsg = backRootEle.element("list").element("items").element("item").element("fail_Desc").getText();//银行状态码
								if ("0026".equals(r1Code) && "S".equals(bankStatus)) {//已成功
									payRequest.setRespCode("000");
									payRequest.receiveAndPaySingle.setRespCode("000");
									payRequest.receivePayRes = "0";
									rp.errorMsg = "处理成功";
									return true;
								}else throw new Exception(failMsg);
							}else throw new Exception((String) ((xmlBackMap.get("error_Msg")!=null && xmlBackMap.get("error_Msg")!="") ? xmlBackMap.get("error_Msg"):"易宝查单失败!"));
						}else{
							log.info("易宝查单：证书DN不是易宝的");
						}
					}else{
						log.info("易宝查单：md5验签失败");
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("易宝查单失败！");
		}
		return false;
	}
	
}
