package com.pay.merchantinterface.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.JWebConstant;
import util.MD5;
import util.PayUtil;
import util.Tools;

import com.PayConstant;
import com.pay.bank.dao.PayBank;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.custstl.dao.PayCustStlInfoDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchantinterface.controller.PayInterfaceController;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.merchantinterface.dao.PayRealNameAuthDAO;
import com.pay.merchantinterface.dao.PayTranUserQuickCardDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayProductOrderDAO;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.pay.risk.service.RiskService;
import com.pay.user.dao.PayTranUserInfo;
import com.pay.user.dao.PayTranUserInfoDAO;
import com.sun.org.apache.xml.internal.security.utils.Base64;

 
/**
 * pay interface service. 
 * @author Administrator
 *
 */
public class PayInterfaceService {
	private static final Log log = LogFactory.getLog(PayInterfaceService.class);
	public static String encode = "utf-8";
	PayMerchant merchant = null;
	String req;
	public PayInterfaceService(String req){
		this.req = req;
	}
	public PayRequest parseXml(HttpServletRequest request,PayInterfaceController tmp) throws Exception{
		String [] e = Tools.split(req, "|");
		String xmlStr = "";
		PayRequest payRequest = new PayRequest();
		if(e.length == 2) xmlStr = new String(Base64.decode(e[0]),encode);
		else {
			payRequest.setRespInfo("001");
			return payRequest;
		}
		ByteArrayInputStream inStream = null;
		try {
			log.info("req=========\n"+xmlStr);
			inStream = new ByteArrayInputStream(xmlStr.getBytes(encode));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			document = builder.parse(inStream);
			document.getDocumentElement().normalize();
			NamedNodeMap nnm = document.getFirstChild().getAttributes();
			for(int j = 0; j<nnm.getLength(); j++){
				Node nodeitm= nnm.item(j);
				String value = nodeitm.getNodeValue();
				if (nodeitm.getNodeName().equals("application"))  payRequest.application=value;
				else if (nodeitm.getNodeName().equals("version"))  payRequest.version=value;
				else if (nodeitm.getNodeName().equals("merchantId"))  payRequest.merchantId=value;
				else if (nodeitm.getNodeName().equals("merchantOrderId"))  payRequest.merchantOrderId=value;
				else if (nodeitm.getNodeName().equals("merchantName"))  payRequest.merchantName=value;
				else if (nodeitm.getNodeName().equals("merchantOrderAmt"))  payRequest.merchantOrderAmt=value;
				else if (nodeitm.getNodeName().equals("merchantOrderDesc"))  payRequest.merchantOrderDesc=value;
				else if (nodeitm.getNodeName().equals("merchantPayNotifyUrl"))  payRequest.merchantNotifyUrl=value;
				else if (nodeitm.getNodeName().equals("merchantFrontEndUrl"))  payRequest.merchantFrontEndUrl=value;
				else if (nodeitm.getNodeName().equals("userMobileNo"))  payRequest.userMobileNo=value;
				else if (nodeitm.getNodeName().equals("payerId"))  payRequest.payerId=value;
				else if (nodeitm.getNodeName().equals("userName"))  payRequest.userName=value;
				else if (nodeitm.getNodeName().equals("salerId"))  payRequest.salerId=value;
				else if (nodeitm.getNodeName().equals("guaranteeAmt"))  payRequest.guaranteeAmt=value;
				else if (nodeitm.getNodeName().equals("status"))  payRequest.guaranteeNoticeStatus=value;
				else if (nodeitm.getNodeName().equals("bankName"))  payRequest.bankName=value;
				else if (nodeitm.getNodeName().equals("bankId"))  payRequest.bankId=value;
				else if (nodeitm.getNodeName().equals("cardNo"))  payRequest.cardNo=value;
				else if (nodeitm.getNodeName().equals("cvv2"))  payRequest.cvv2=value;
				else if (nodeitm.getNodeName().equals("validPeriod"))  payRequest.validPeriod=value;
				else if (nodeitm.getNodeName().equals("userType"))payRequest.userType=value;
				else if (nodeitm.getNodeName().equals("accountType"))  payRequest.accountType=value;
				else if (nodeitm.getNodeName().equals("bankId"))  payRequest.bankId=value;
				else if (nodeitm.getNodeName().equals("credentialType"))  payRequest.credentialType=value;
				else if (nodeitm.getNodeName().equals("credentialNo"))  payRequest.credentialNo=value;
				else if (nodeitm.getNodeName().equals("msgExt"))  payRequest.msgExt=value;
				else if (nodeitm.getNodeName().equals("orderTime"))  payRequest.orderTime=value;
				else if (nodeitm.getNodeName().equals("bizType"))  payRequest.bizType=value;
				else if (nodeitm.getNodeName().equals("rptType"))  payRequest.rptType=value;
				else if (nodeitm.getNodeName().equals("payMode"))  payRequest.payMode=value;
				else if (nodeitm.getNodeName().equals("merchantRefundAmt"))  payRequest.merchantRefundAmt=value;
				else if (nodeitm.getNodeName().equals("accountDate"))  payRequest.accountDate=value;
				else if (nodeitm.getNodeName().equals("checkCode"))  payRequest.checkCode=value;
				else if(nodeitm.getNodeName().equals("tranId"))payRequest.tranId=value;
				else if(nodeitm.getNodeName().equals("queryTranId"))payRequest.queryTranId=value;
				else if(nodeitm.getNodeName().equals("receivePayNotifyUrl"))payRequest.receivePayNotifyUrl=value;
				else if(nodeitm.getNodeName().equals("receivePayType"))payRequest.receivePayType=value;
				else if(nodeitm.getNodeName().equals("timestamp"))payRequest.timestamp=value;
				else if(nodeitm.getNodeName().equals("accountProp"))payRequest.accountProp=value;
				else if(nodeitm.getNodeName().equals("timeliness"))payRequest.timeliness=value;
				else if(nodeitm.getNodeName().equals("subMerchantId"))payRequest.subMerchantId=value;
				else if(nodeitm.getNodeName().equals("appointmentTime"))payRequest.appointmentTime=value;
				else if(nodeitm.getNodeName().equals("bankGeneralName"))payRequest.bankGeneralName=value;
				else if(nodeitm.getNodeName().equals("accNo"))payRequest.accNo=value;
				else if(nodeitm.getNodeName().equals("accName"))payRequest.accName=value;
				else if(nodeitm.getNodeName().equals("amount"))payRequest.amount=value;
				else if(nodeitm.getNodeName().equals("tel"))payRequest.tel=value;
				else if(nodeitm.getNodeName().equals("summary"))payRequest.summary=value;
			}
			NodeList message = document.getFirstChild().getChildNodes();
			for(int i = 0; i<message.getLength(); i++){
				Node rpList = message.item(i);
				if(rpList.getNodeName().equals("authList")||rpList.getNodeName().equals("signList")
						||rpList.getNodeName().equals("tranList")||rpList.getNodeName().equals("queryList")){
					NodeList itemList = rpList.getChildNodes();
					for(int k = 0; k<itemList.getLength();k++){
						NamedNodeMap item = itemList.item(k).getAttributes();
						if(item == null)continue;
						PayReceiveAndPay rp = new PayReceiveAndPay();
						for(int j = 0; j<item.getLength(); j++){
							Node nodeitm= item.item(j);
							String valueTmp = nodeitm.getNodeValue();
							if(nodeitm.getNodeName().equals("sn"))rp.sn=valueTmp;
							else if(nodeitm.getNodeName().equals("bankGeneralName"))rp.bankGeneralName=valueTmp;
							else if(nodeitm.getNodeName().equals("bankName"))rp.bankName=valueTmp;
							else if(nodeitm.getNodeName().equals("bankId"))rp.bankId=valueTmp;
							else if(nodeitm.getNodeName().equals("accType"))rp.accType=valueTmp;
							else if(nodeitm.getNodeName().equals("accNo"))rp.accNo=valueTmp;
							else if(nodeitm.getNodeName().equals("accName"))rp.accName=valueTmp;
							else if(nodeitm.getNodeName().equals("province"))rp.province=valueTmp;
							else if(nodeitm.getNodeName().equals("city"))rp.city=valueTmp;
							else if(nodeitm.getNodeName().equals("amount"))try {rp.amount=Long.parseLong(valueTmp);} catch (Exception e2) {}
							else if(nodeitm.getNodeName().equals("credentialType"))rp.credentialType=valueTmp;
							else if(nodeitm.getNodeName().equals("credentialNo"))rp.credentialNo=valueTmp;
							else if(nodeitm.getNodeName().equals("beginDate"))rp.beginDate=valueTmp;
							else if(nodeitm.getNodeName().equals("endDate"))rp.endDate=valueTmp;
							else if(nodeitm.getNodeName().equals("tel"))rp.tel=valueTmp;
							else if(nodeitm.getNodeName().equals("summary"))rp.summary=valueTmp;
							else if(nodeitm.getNodeName().equals("respCode"))rp.respCode=valueTmp;
							else if(nodeitm.getNodeName().equals("respDesc"))rp.respDesc=valueTmp;
							else if(nodeitm.getNodeName().equals("charge"))rp.charge=valueTmp;
							else if(nodeitm.getNodeName().equals("payAccNo"))rp.payAccNo=valueTmp;
							else if(nodeitm.getNodeName().equals("payAccName"))rp.payAccName=valueTmp;
							else if(nodeitm.getNodeName().equals("recAccNo"))rp.recAccNo=valueTmp;
							else if(nodeitm.getNodeName().equals("recAccName"))rp.recAccName=valueTmp;
						}
						payRequest.receivePayList.add(rp);
					}
				}
			}
			tmp.application = payRequest.application;
			merchant = new PayInterfaceDAO().getCertByMerchantId(payRequest.merchantId);
			if(merchant == null){
				payRequest.setRespInfo("003");
				payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
				return payRequest;
			}
			payRequest.merchant = merchant;
			//校验商户IP,网银跳转、快捷H5不受IP控制

			if(!"SubmitOrder".equals(payRequest.application)&&!"CertPayOrderH5".equals(payRequest.application)){
				if("1".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))&&!"127.0.0.1".equals(merchant.interfaceIp)){//正式环境
					String rIp = request.getHeader("x-forwarded-for");
					rIp=rIp==null?request.getRemoteHost():rIp;
					if((";"+merchant.interfaceIp.replaceAll("；",";").trim()+";").indexOf(";"+rIp+";")==-1){
						payRequest.setRespInfo("061");
						payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
						return payRequest;
					}
				}
			}
			//根据商户和application检查操作访问频率==============开始==========
			String app = payRequest.application;
			//下单判断
			if("SubmitOrder".equals(payRequest.application)||"CertPayOrder".equals(payRequest.application)
					||"CertPayOrderH5".equals(payRequest.application)||"WeiXinScanOrder".equals(payRequest.application)
					||"WeiXinWapOrder".equals(payRequest.application)||"ZFBScanOrder".equals(payRequest.application)
					||"QQScanOrder".equals(payRequest.application))app="CreateOrder";
			if(!checkAccessFrequency(payRequest,app)){
				payRequest.setRespInfo("-1");
				payRequest.respDesc="访问频率过高";
				payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
				return payRequest;
			}
			//根据商户和application检查操作访问频率==============结束==========
			//获取当前商户结算信息。
            payRequest.merchant.payCustStlInfo=new PayCustStlInfoDAO().getPayCustStlInfoByMerchantId(payRequest.merchant.custId);
			Certificate certificate = getMerCertificate();
			//验证签名
			if (!CertUtil.verifyWithRSA(MD5.getDigest(xmlStr.getBytes(encode))
					,Base64.decode(e[1]),certificate)){
				payRequest.setRespInfo("002");
				payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
				return payRequest;
			}
			//验证商户
			if(!checkMerchant(payRequest,merchant)){
				payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
				return payRequest;
			}
			payRequest.reqXml = xmlStr;
			payRequest.sign = e[1];
			//直连网银下单或者快捷支付下单
			if("SubmitOrder".equals(payRequest.application)||"CertPayOrder".equals(payRequest.application)
					||"CertPayOrderH5".equals(payRequest.application)||"WeiXinScanOrder".equals(payRequest.application)
					||"WeiXinWapOrder".equals(payRequest.application)||"ZFBScanOrder".equals(payRequest.application)
					||"QQScanOrder".equals(payRequest.application)){
				//检查用户是否支持支付业务
				if(merchant.payWaySupported==null||merchant.payWaySupported.length()==0){
					payRequest.setRespInfo("062");
					payRequest.respDesc=payRequest.respDesc+"—支付方式非法";
					return payRequest;
				}
				if("SubmitOrder".equals(payRequest.application)){
					if(!checkSubmitOrderRequest(payRequest))return payRequest;
					//检查用户是否支持支付业务（PAY_WAY_SUPPORTED：支持的支付方式 第1位B2C借记卡、第2位B2C信用卡、第3位B2B、第4位快捷，0支持 1不支持，默认0000）
					//B2C
					if("1".equals(payRequest.userType)){
						if(("0".equals(payRequest.accountType) && "1".equals(merchant.payWaySupported.substring(0,1)))
							 ||("1".equals(payRequest.accountType) && "1".equals(merchant.payWaySupported.substring(1,2)))){
							payRequest.setRespInfo("062");
							payRequest.respDesc=payRequest.respDesc+"—B2C借记卡/信用卡";
							return payRequest;
						}
					} else { //B2B
						if("1".equals(merchant.payWaySupported.substring(2,3))){//不支B2B
							payRequest.setRespInfo("062");
							payRequest.respDesc=payRequest.respDesc+"—B2B";
							return payRequest;
						}
					}
				} else if("CertPayOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)){
					if("CertPayOrder".equals(payRequest.application) && !checkCertPayOrderRequest(payRequest))return payRequest;
					if("CertPayOrderH5".equals(payRequest.application) && !checkCertPayOrderH5Request(payRequest))return payRequest;
					//检查用户是否支持支付业务（PAY_WAY_SUPPORTED：支持的支付方式 第1位B2C借记卡、第2位B2C信用卡、第3位B2B、第4位快捷，0支持 1不支持，默认0000）
					if(("0".equals(payRequest.accountType) &&"1".equals(merchant.payWaySupported.substring(3,4)))
							||("1".equals(payRequest.accountType) && "1".equals(merchant.payWaySupported.substring(7,8)))){//不支持快捷
						payRequest.setRespInfo("062");
						payRequest.respDesc=payRequest.respDesc+"—快捷支付";
						return payRequest;
					}
					long quickMinAmt = 200;
					try {quickMinAmt = Long.parseLong(PayConstant.PAY_CONFIG.get("PAY_QUICK_MIN_AMT"));} catch (Exception e2) {}
					if(Long.parseLong(payRequest.merchantOrderAmt)<quickMinAmt){
						payRequest.setRespInfo("077");
						payRequest.respDesc=payRequest.respDesc+(String.format("%.2f", ((double)quickMinAmt)/100d))+"元";
						return payRequest;
					}
				} else if("WeiXinScanOrder".equals(payRequest.application)//微信扫码/微信WAP/支付宝扫码
					||"WeiXinWapOrder".equals(payRequest.application)||"ZFBScanOrder".equals(payRequest.application)
					||"QQScanOrder".equals(payRequest.application)){
					if(!checkWeiXinScanOrder(payRequest))return payRequest;
					//检查用户是否支持支付业务（PAY_WAY_SUPPORTED：支持的支付方式 第1位B2C借记卡、第2位B2C信用卡、第3位B2B、第4位快捷，0支持 1不支持，默认0000）
					if("WeiXinScanOrder".equals(payRequest.application)&&"1".equals(merchant.payWaySupported.substring(8,9))){//不支持微信扫码
						payRequest.setRespInfo("062");
						payRequest.respDesc=payRequest.respDesc+"—微信扫码支付";
						return payRequest;
					}
					if("WeiXinWapOrder".equals(payRequest.application)&&"1".equals(merchant.payWaySupported.substring(9,10))){//不支持微信WAP
						payRequest.setRespInfo("062");
						payRequest.respDesc=payRequest.respDesc+"—微信WAP支付";
						return payRequest;
					}
					if("ZFBScanOrder".equals(payRequest.application)&&"1".equals(merchant.payWaySupported.substring(10,11))){//不支持支付宝扫码
						payRequest.setRespInfo("062");
						payRequest.respDesc=payRequest.respDesc+"—支付宝扫码支付";
						return payRequest;
					}
					if("QQScanOrder".equals(payRequest.application)&&"1".equals(merchant.payWaySupported.substring(11,12))){//不支持QQ扫码
						payRequest.setRespInfo("062");
						payRequest.respDesc=payRequest.respDesc+"—QQ扫码支付";
						return payRequest;
					}
				}
				payRequest.tranAmt=Long.parseLong(payRequest.merchantOrderAmt);//订单金额
				//商户风控检查 限额类型:1消费 2充值 3结算 4退款 5提现 6转账
				RiskService riskService = new RiskService();
				String msg = riskService.checkMerchantLimit(payRequest,payRequest.merchantId,PayConstant.TRAN_TYPE_CONSUME);
				//违反风控规则
				if(msg!=null){
					payRequest.setRespInfo("039");
					payRequest.respDesc=payRequest.respDesc+"("+msg+")";
					return payRequest;
				}
				//用户快捷支付风控检查
				if("CertPayOrder".equals(payRequest.application)||"CertPayOrderH5".equals(payRequest.application)){
					msg = riskService.checkUserLimit(payRequest,payRequest.payerId,PayConstant.TRAN_TYPE_CONSUME);
					//违反风控规则
					if(msg!=null){
						payRequest.setRespInfo("039");
						payRequest.respDesc=msg;
						return payRequest;
					}
				}
				//用户充值风控检查 限额类型:1消费 2充值 3结算 4退款 5提现 6转账
				if(payRequest.merchantId.equals(PayConstant.PAY_CONFIG.get("PAY_RECHARGE_ACC"))){
					msg = riskService.checkUserLimit(payRequest,payRequest.payerId,PayConstant.TRAN_TYPE_RECHARGE);
					//违反风控规则
					if(msg!=null){
						payRequest.setRespInfo("039");
						payRequest.respDesc=msg;
						return payRequest;
					}
				}
				PayProductOrder prdOrder = new PayProductOrderDAO().getProductOrderById(payRequest.merchantId,
						payRequest.merchantOrderId);
				if(prdOrder != null){
//					if(!"00".equals(prdOrder.ordstatus)){
						payRequest.setRespInfo("005");
						return payRequest;
						//throw new PayException(payRequest.respCode,payRequest.respDesc);
//					}
				} else prdOrder = new PayProductOrder();
				//取得支付买家、卖家,账号为key,用户为值
				payRequest.tranUserMap = new PayTranUserInfoDAO().getTranUserByCreateOrder(payRequest);
				//检查买卖用户合法性
				if(!checkTranUser(payRequest)){
//					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;//throw new PayException(payRequest.respCode,payRequest.respDesc);
				}
				//担保支付 检查卖家
				if(payRequest.guaranteeAmt.length()>0&&Long.parseLong(payRequest.guaranteeAmt)>0){
					try {
						//系统目前只支持全额担保
						if(Long.parseLong(payRequest.guaranteeAmt) != Long.parseLong(payRequest.merchantOrderAmt)){
							payRequest.setRespInfo("043");
							return payRequest;
						}
						//检查担保卖家是否存在
						if(payRequest.tranUserMap.get(payRequest.salerId) == null){
							payRequest.setRespInfo("044");
							return payRequest;
						}
					} catch (Exception e2) {throw e2;}
				}
				PayOrder order = new PayOrder();
				//获取支付终端类型
				order.termtyp = getTerminalType(request);
				payRequest.productOrder = prdOrder;
				payRequest.payOrder = order;
				//H5快捷支付，载入快捷卡信息
				if("CertPayOrderH5".equals(payRequest.application)){
					PayTranUserInfo user = payRequest.tranUserMap.get(payRequest.payerId);
					if(user != null)new PayTranUserQuickCardDAO().loadQuickPayH5Card(user);
				}
				new PayOrderInterfaceService().addOrder(merchant,payRequest,prdOrder,order);
				document = builder.newDocument();
				Element root = document.createElement("message");
				root.setAttribute("application", "SubmitOrder");
				root.setAttribute("version",PayConstant.PAY_CONFIG.get("merchant_interface_version"));
				root.setAttribute("merchantId", payRequest.merchantId);
				root.setAttribute("merchantOrderId", payRequest.merchantOrderId);
				document.appendChild(root);
			}
			//查询
			else if("QueryOrder".equals(payRequest.application)){
				if(!checkQueryOrderRequest(payRequest)){
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				String xml = new PayOrderInterfaceService().queryOrder(payRequest);
				log.info(xml);
				//查询操作
				payRequest.responseStr =CertUtil.createTransStr(xml);
			}
			//退款
			else if("RefundOrder".equals(payRequest.application)){
				
				payRequest.setRespInfo("-1");
				payRequest.respDesc="此功能已停用";
				payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
				return payRequest;
				
//				if(!checkRefundOrderRequest(payRequest)){
//					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
//					return payRequest;
//				}
//				payRequest.tranAmt=Long.parseLong(payRequest.merchantRefundAmt);//退款金额
//				//风控检查 限额类型:1消费 2充值 3结算 4退款 5提现 6转账
//				String msg = new RiskService().checkMerchantLimit(payRequest,payRequest.merchantId,"4");
//				//违反风控规则
//				if(msg!=null){
//					payRequest.setRespInfo("039");
//					payRequest.respDesc=payRequest.respDesc+"("+msg+")";
//					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
//					return payRequest;
//				}
//				String xml = new PayOrderInterfaceService().refundOrder(payRequest);
//				log.info(xml);
//				//退款操作
//				payRequest.responseStr =CertUtil.createTransStr(xml);
			}
			//撤销
			else if("CancelOrder".equals(payRequest.application)){
				if(!checkCancelOrderRequest(payRequest)){
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				//撤销操作
				payRequest.responseStr =CertUtil.createTransStr(new PayOrderInterfaceService().cancelOrder(payRequest));
			}
			//对账文件下载
			else if("AccountFile".equals(payRequest.application)){
				String filePath = JWebConstant.APP_PATH+"/dat/merchant_account_file/";
				if(checkAccountFileRequest(payRequest)){
					payRequest.accountFilePath= filePath +payRequest.merchantId+"/"+payRequest.accountDate+".zip";
				}
				return payRequest;
			}
			//担保通知
			else if("GuaranteeNotice".equals(payRequest.application)){
				if(!checkGuaranteeNoticeRequest(payRequest)){
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				String xml = new PayOrderInterfaceService().guaranteeNotice(payRequest);
				log.info(xml);
				//担保通知处理
				payRequest.responseStr =CertUtil.createTransStr(xml);
			}
			//短信重发
			else if("CertPayReSms".equals(payRequest.application)){
				if(!checkCertPayReSmsRequest(payRequest)){
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
					PayProductOrder prdOrder = new PayProductOrderDAO().getProductOrderById(payRequest.merchantId,payRequest.merchantOrderId);
					PayOrder payOrder = new PayOrderDAO().getPayOrderByMerInfo(payRequest.merchantId,payRequest.merchantOrderId);
					//订单不存在
					if(prdOrder==null || payOrder==null){
        				payRequest.setRespInfo("008");
        				payRequest.responseStr =CertUtil.createTransStr(this.createTestCertPayReSms(payRequest, prdOrder));
        				return payRequest;
        			}
        			//非快捷支付订单
        			if(!"03".equals(payOrder.paytype)){
        				payRequest.setRespInfo("057");
        				payRequest.responseStr =CertUtil.createTransStr(
        						this.createTestCertPayReSms(payRequest, prdOrder));
        				return payRequest;
        			}
        			payRequest.setRespInfo("000");
            		payRequest.responseStr =CertUtil.createTransStr(
    						this.createTestCertPayReSms(payRequest, prdOrder));
				//查询操作
				} else payRequest.responseStr =CertUtil.createTransStr(new PayOrderInterfaceService().sendCertPayReSms(payRequest));
				return payRequest;
			}
			//支付确认
			else if("CertPayConfirm".equals(payRequest.application)){
				if(!checkCertPayConfirmRequest(payRequest)){
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				//订单
    			PayProductOrder prdOrder = new PayProductOrderDAO().getProductOrderById(payRequest.merchantId,payRequest.merchantOrderId);
    			PayOrder payOrder = new PayOrderDAO().getPayOrderByMerInfo(payRequest.merchantId,payRequest.merchantOrderId);
    			if(prdOrder==null||payOrder==null){
    				//商品订单不存在
    				if(prdOrder == null){
    					payRequest.setRespInfo("008");
    					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
    					return payRequest;
    				}
    			}
        		PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payOrder.bankpayacno);
        		if(!"00".equals(prdOrder.ordstatus)){//订单已经处理完成
					payRequest.setRespInfo("014");
    				payRequest.responseStr =CertUtil.createTransStr(
    						this.createTestCertPayConfirm(payRequest, prdOrder, payOrder, cardBin));
    				return payRequest;
				}
        		payRequest.productOrder = prdOrder;
        		payRequest.payOrder = payOrder;        		
				//测试环境
            	if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
            		//订单不存在
        			if(prdOrder==null || payOrder==null){
        				payRequest.setRespInfo("008");
        				payRequest.responseStr =CertUtil.createTransStr(
        						this.createTestCertPayConfirm(payRequest, prdOrder, payOrder, cardBin));
        				return payRequest;
        			}
        			//非快捷支付订单
        			if(!"03".equals(payOrder.paytype)){
        				payRequest.setRespInfo("057");
        				payRequest.responseStr =CertUtil.createTransStr(
        						this.createTestCertPayConfirm(payRequest, prdOrder, payOrder, cardBin));
        				return payRequest;
        			}
        			String msgCode = PayConstant.PAY_CONFIG.get("PAY_QUICK_MSG_CODE_TEST");
        			if(msgCode==null||msgCode.length()==0)msgCode="123456";
        			if(msgCode.equals(payRequest.checkCode)){
        				payRequest.setRespInfo("000");
        				payOrder.ordstatus="01";
        				payOrder.bankjrnno = String.valueOf(System.currentTimeMillis());
        				new NotifyInterface().notifyMer(payOrder);
        			}
        			else {
        				payRequest.respCode="-1";
        				payRequest.respDesc="验证码错误";
        			}
            		payRequest.responseStr =CertUtil.createTransStr(
    						this.createTestCertPayConfirm(payRequest, prdOrder, payOrder, cardBin));
            	//渠道提交
            	} else {
            		payRequest.responseStr = CertUtil.createTransStr(new PayOrderInterfaceService().certPayConfirm(payRequest));
            		if("1".equals(PayConstant.PAY_CONFIG.get("NOTIFY_SEARCH_CHANNEL_FLAG"))
            				&&!"01".equals(payRequest.payOrder.ordstatus)){
            			new PayChannelService().searchOrderForChannelNotifyFail(payRequest.payOrder.payordno);
            		}
            	}
				return payRequest;
			//实名认证
			} else if("RealNameAuth".equals(payRequest.application)){
				if(!checkRealNameAuthRequest(payRequest)){
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				//测试环境
            	if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
            		payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            		payRequest.setRespInfo("000");
            		for(int i=0; i<payRequest.receivePayList.size(); i++){
            			PayReceiveAndPay rp = payRequest.receivePayList.get(i);
            			rp.way="0";
            			rp.channelId=PayConstant.PAY_CONFIG.get("REAL_NAME_CHANNEL");
            			if(rp.respCode.length()==0)rp.setRespInfo("000");
            		}
            		//保存认证结果
            		new PayRealNameAuthDAO().addPayRealNameAuthBatch(payRequest);
            		payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().realNameAuthToXml(payRequest));
            	} else payRequest.responseStr = CertUtil.createTransStr(new PayInterfaceOtherService().realNameAuth(payRequest));
            //单笔实时代收付
			} else if("ReceivePay".equals(payRequest.application)){
				//检查是否开通代付
				if("1".equals(payRequest.receivePayType)){
					if("1".equals(PayConstant.PAY_CONFIG.get("PAY_IS_OPEN")) && !new PayReceiveAndPayDAO().busIsOpen(payRequest.merchantId, "16")){
						payRequest.setRespInfo("-1");
						payRequest.respDesc=PayConstant.PAY_CONFIG.get("PAY_IS_CLOSE_PROMPT")==null
								?"代付已关闭":PayConstant.PAY_CONFIG.get("PAY_IS_CLOSE_PROMPT");
						payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
						return payRequest;
					}
				}
				//代付时间段判断
				if(!checkPayTime(payRequest)){
					payRequest.setRespInfo("-1");
					payRequest.respDesc="代付时间非法(single)";
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				//业务支持判断
				if(("0".equals(payRequest.receivePayType) && "1".equals(merchant.payWaySupported.substring(4,5)))
						 ||("1".equals(payRequest.receivePayType) && "1".equals(merchant.payWaySupported.substring(5,6)))){
					payRequest.setRespInfo("062");
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				if(!checkReceivePayRequest(payRequest)){
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				//风控检查========开始
				List<PayRequest> list = new ArrayList<PayRequest>();
				list.add(payRequest);
				PayReceiveAndPay rp = new PayReceiveAndPay();
				rp.amount =Long.parseLong(payRequest.amount);
	    		rp.accNo=payRequest.accNo;
				payRequest.receivePayList.add(rp);
				Object [] obj = new Object[]{PayConstant.CUST_TYPE_MERCHANT,payRequest.merchantId,
						"0".equals(payRequest.receivePayType)?list:payRequest.receivePayList,new Date()};
				String msg = new RiskService().checkMerchantLimit(obj,payRequest.merchantId,
						"0".equals(payRequest.receivePayType)?PayConstant.TRAN_TYPE_DS:PayConstant.TRAN_TYPE_DF);
				//违反风控规则
				if(msg!=null){
					payRequest.setRespInfo("039");
					payRequest.respDesc=msg;
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				//风控检查========结束
				//测试环境
				PayInterfaceOtherService otherService = new PayInterfaceOtherService();
            	if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
            		payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            		payRequest.setRespInfo("000");
            		otherService.getRPChannel(payRequest, rp.amount);
            		PayReceiveAndPay payReceiveAndPay = otherService.addPayReceiveAndPaySingle(payRequest,"0");//代收付产生类型，0代收付，1快捷包装代收付
            		//通知商户
            		List<PayReceiveAndPay> notifyList = new ArrayList<PayReceiveAndPay>();
            		notifyList.add(payReceiveAndPay);
            		try {
            			new PayInterfaceOtherService().checkMerchantAccountAmt(payRequest, notifyList);
					} catch (Exception e2) {
						//修改代付状态
						List<PayReceiveAndPay> rnAuthList = new ArrayList<PayReceiveAndPay>();
						payReceiveAndPay.status="2";
						payReceiveAndPay.respCode="-1";
						payReceiveAndPay.respDesc=e2.getMessage();
						payReceiveAndPay.errorMsg=payReceiveAndPay.respDesc;
		    			payRequest.receivePayRes = "-1";
		    			payRequest.respCode=payReceiveAndPay.respCode;
		    			payRequest.respDesc = payReceiveAndPay.respDesc;
						rnAuthList.add(payReceiveAndPay);						
						new PayReceiveAndPayDAO().updatePayReceiveAndPay(rnAuthList);
						throw e2;
					}
            		payReceiveAndPay.setRetCode("000");
            		new ReceivePaySingleTest(payRequest,notifyList).start();
            		payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().receivePayToXml(payRequest));
            	} else payRequest.responseStr = CertUtil.createTransStr(otherService.receivePaySingle(payRequest));
			} else if("ReceivePayBatch".equals(payRequest.application)){
				//检查是否开通代付
				if("1".equals(payRequest.receivePayType)){
					if("1".equals(PayConstant.PAY_CONFIG.get("PAY_IS_OPEN"))){
						payRequest.setRespInfo("-1");
						payRequest.respDesc=PayConstant.PAY_CONFIG.get("PAY_IS_CLOSE_PROMPT")==null
								?"代付已关闭":PayConstant.PAY_CONFIG.get("PAY_IS_CLOSE_PROMPT");
						payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
						return payRequest;
					}
				}
				//代付时间段判断
				if(!checkPayTime(payRequest)){
					payRequest.setRespInfo("-1");
					payRequest.respDesc="批量代付时间非法(batch)";
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				//业务支持判断
				if(("0".equals(payRequest.receivePayType) && "1".equals(merchant.payWaySupported.substring(4,5)))
						 ||("1".equals(payRequest.receivePayType) && "1".equals(merchant.payWaySupported.substring(5,6)))){
					payRequest.setRespInfo("062");
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				if(!checkReceivePayBatchRequest(payRequest)){
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				//风控检查========开始
				Object [] obj = new Object[]{PayConstant.CUST_TYPE_MERCHANT,payRequest.merchantId,payRequest.receivePayList,new Date()};
				String msg = new RiskService().checkMerchantLimit(obj,payRequest.merchantId,
						"0".equals(payRequest.receivePayType)?PayConstant.TRAN_TYPE_DS:PayConstant.TRAN_TYPE_DF);
				//违反风控规则
				if(msg!=null)throw new Exception(msg);
				//风控检查========结束
				//测试环境
				PayInterfaceOtherService otherService = new PayInterfaceOtherService();
            	if("0".equals(JWebConstant.SYS_CONFIG.get("DEBUG"))){
            		//取得渠道
            		long totalAmt = 0l;
            		for(int i = 0; i<payRequest.receivePayList.size(); i++){
            			PayReceiveAndPay rp = payRequest.receivePayList.get(i);
                		totalAmt = totalAmt + rp.amount;
            		}
            		otherService.getRPChannel(payRequest,totalAmt);
            		otherService.addPayReceiveAndPayBatch(payRequest);
            		new PayInterfaceOtherService().checkMerchantAccountAmt(payRequest, payRequest.receivePayList);
            		//通知商户
            		new ReceivePaySingleTest(payRequest,payRequest.receivePayList).start();
            		payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().receivePayToXml(payRequest));
            	} else {
            		String xml = otherService.receivePayBatch(payRequest);
            		log.info(xml);
            		payRequest.responseStr = CertUtil.createTransStr(xml);
            	}
            //收付款查询
			} else if("ReceivePayQuery".equals(payRequest.application)){
				//业务支持判断
				if(("0".equals(payRequest.receivePayType) && "1".equals(merchant.payWaySupported.substring(4,5)))
						 ||("1".equals(payRequest.accountType) && "1".equals(merchant.payWaySupported.substring(5,6)))){
					payRequest.setRespInfo("062");
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				if(!checkReceivePayQueryRequest(payRequest)){
					payRequest.responseStr = CertUtil.createTransStr(new MerchantXmlUtil().errorToXml(payRequest));
					return payRequest;
				}
				String xml = new PayInterfaceOtherService().receivePayQuery(payRequest);
				log.info(xml);
				payRequest.responseStr = CertUtil.createTransStr(xml);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			if(inStream != null) try {inStream.close();} catch (Exception e2) {}
		}
		if("".equals(payRequest.orderTime)) payRequest.orderTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		return payRequest;
	}
	private String createTestCertPayReSms(PayRequest payRequest,PayProductOrder prdOrder){
		return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
        		"<message merchantId=\""+payRequest.merchantId+"\" " +
        		"merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
        		"userMobileNo=\""+(prdOrder != null ? (prdOrder.mobile!=null?prdOrder.mobile:""):"")+"\" " +
        		"respCode=\""+payRequest.respCode+"\" " +
    			"respDesc=\""+payRequest.respDesc+"\"/>";
	}
	private String createTestCertPayConfirm(PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,PayCardBin cardBin) throws Exception{
		if (PayConstant.PAY_CONFIG.get("PAY_CHANNEL_PLTPAY").equals(payOrder.payChannel)){
			PayReceiveAndPay rp = new PayReceiveAndPay();
			rp.amount = payOrder.txamt;
			payRequest.amount = String.valueOf(rp.amount);
			payRequest.accNo = payOrder.payacno;
			rp.accNo=payRequest.accNo;
			rp.accName = payOrder.bankpayusernm;
			payRequest.accName = rp.accName; 
			rp.tel = prdOrder.mobile;
			payRequest.userMobileNo = rp.tel;
			payRequest.tel = rp.tel;
			rp.credentialType = prdOrder.credentialType;
			payRequest.credentialType = rp.credentialType;
			rp.credentialNo = prdOrder.credentialNo;
			payRequest.credentialNo = rp.credentialNo;
			rp.bankCode = cardBin.bankCode;
			payRequest.bankId = rp.bankCode;
			rp.bankName = cardBin.bankName;
			payRequest.bankGeneralName = rp.bankName;
			payRequest.bankName = rp.bankName;
			payRequest.receivePayList.add(rp);
			payRequest.receivePayType = "0";
			//测试环境
			PayInterfaceOtherService otherService = new PayInterfaceOtherService();
			payRequest.timestamp=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			payRequest.setRespInfo("000");
			PayCoopBank rpChannel = otherService.getRPChannel(payRequest, rp.amount);
			otherService.addPayReceiveAndPaySingle(payRequest,"1");//代收付产生类型，0代收付，1快捷包装代收付
		}
		return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><message " +
    			"bankId=\""+(cardBin != null?cardBin.bankCode:"")+"\" " +
    			"bankName=\""+(cardBin != null?cardBin.bankName:"")+"\" " +
    			"bindId=\"\" " +
    			"cardNo=\""+(payOrder!=null?payOrder.bankpayacno:"")+"\" " +
    			"cardType=\""+(cardBin != null?cardBin.cardType:"")+"\" " +
    			"merchantId=\""+payRequest.merchantId+"\" " +
    			"merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
    			"respCode=\""+payRequest.respCode+"\" " +
    			"respDesc=\""+payRequest.respDesc+"\" " +
    			"userMobileNo=\""+(prdOrder != null ? (prdOrder.mobile!=null?prdOrder.mobile:""):"")+"\"/>";
	}
	private String createXml(Document document) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING,"utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			StreamResult result = new StreamResult(dos);
			transformer.transform(source, result);
			return new String(bos.toByteArray(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(dos != null){
				try {
					dos.close();
				} catch (Exception e2) {}
			}
			if(bos != null){
				try {
					bos.close();
				} catch (Exception e2) {}
			}
		}
		return "";
	}
	/**
	 * 获取商户证书
	 * @param merchantId
	 * @return
	 * @throws Exception 
	 */
	protected Certificate getMerCertificate() throws Exception{
		ByteArrayInputStream inStream = null;
		try {
			inStream = new ByteArrayInputStream(merchant.cert.getBytes());
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			return certFactory.generateCertificate(inStream);
		} catch (Exception e) {
			throw new Exception("商户证书错误");
		} finally {
			if (inStream != null) try {inStream.close();} catch (Exception e) {}
		}
	}
	private boolean checkMerchant(PayRequest request,PayMerchant merchant) {
		if("1".equals(merchant.frozStlSign)){
			request.setRespInfo("004");
			return false;
		}
		if(!"0".equals(merchant.merStatus)){
			request.setRespInfo("007");
			return false;
		}
		if(!"1".equals(merchant.checkStatus)){
			request.setRespInfo("023");
			return false;
		}
		return true;
	}
	private boolean checkSubmitOrderRequest(PayRequest request) throws UnsupportedEncodingException {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantName.trim().length() > 0 
				&& (request.merchantName.getBytes(encode).length > 24 
						|| !request.merchantName.equals(merchant.storeName))){
			request.setRespInfo("016");
			return false;
		}
		if(request.merchantOrderId.length() == 0 
				|| request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		try {
			long merchantOrderAmt = Long.parseLong(request.merchantOrderAmt);
			if(merchantOrderAmt <= 0 || merchantOrderAmt > 100000000000l)throw new Exception();
		} catch (Exception e) {
			request.setRespInfo("018");
			return false;
		}
		if(request.merchantOrderDesc.length() > 0 
			&& request.merchantOrderDesc.getBytes(encode).length > 128){
			request.setRespInfo("019");
			return false;
		}
		if(request.merchantNotifyUrl.length() > 200
			|| !request.merchantNotifyUrl.toLowerCase().trim().startsWith("http")){
			request.setRespInfo("020");
			return false;
		}
		if(request.merchantFrontEndUrl.length() > 200
			|| (request.merchantFrontEndUrl.length() !=0 && 
				!request.merchantFrontEndUrl.toLowerCase().trim().startsWith("http"))){
			request.setRespInfo("021");
			return false;
		}
		try {			
			String m = request.userMobileNo.trim();
			if(m.length() > 0){
				new Long(m);
				if(m.length() != 11 || !m.startsWith("1")){
					request.setRespInfo("022");
					return false;
				}
			}
		} catch (Exception e) {
			request.setRespInfo("022");
			return false;
		}
		if(request.getCredentialType().length() > 0){
			if(PayConstant.CERT_TYPE.get(request.getCredentialType())==null){
				request.setRespInfo("026");
				return false;
			}
			if(request.getCredentialNo().length()==0
				||request.getCredentialNo().length()>20){
				request.setRespInfo("027");
				return false;
			}
		}
		if(request.userName.length() > 0 
			&& request.userName.getBytes(encode).length > 50){
			request.setRespInfo("035");
			return false;
		}
		if((request.payerId.length() > 0 && request.payerId.getBytes(encode).length > 50)){
				request.setRespInfo("040");
				return false;
			}
		if(request.salerId.length() > 0 && request.salerId.getBytes(encode).length > 100){
			request.setRespInfo("041");
			return false;
		}
		if(request.guaranteeAmt.length()>0){
			try {
				long guaranteeAmt = Long.parseLong(request.guaranteeAmt);
				if(guaranteeAmt < 0 || guaranteeAmt > 999999999999l)throw new Exception();
			} catch (Exception e) {
				request.guaranteeAmt="0";
			}
		}
		if(!"1".equals(request.userType)&&!"2".equals(request.userType)){
			request.userType="1";
		}
		if(!"0".equals(request.accountType)&&!"1".equals(request.accountType)
				&&!"4".equals(request.accountType)){
			request.setRespInfo("037");
			return false;
		} else {
			if("1".equals(request.userType)){
				if("4".equals(request.accountType)){
					request.setRespInfo("037");
					return false;
				}
			} else {
				if(!"4".equals(request.accountType)){
					request.setRespInfo("037");
					return false;
				}
			}
		}
		if(request.bankId.length() > 0 
			&& request.bankId.getBytes(encode).length > 10){
			request.setRespInfo("038");
			return false;
		}
		if(request.getMsgExt().length() > 0 
			&& request.getMsgExt().getBytes(encode).length > 128){
			request.setRespInfo("028");
			return false;
		}
		try {
			Long.parseLong(request.getOrderTime());
			new java.text.SimpleDateFormat("yyyyMMddHHmmss").parse(request.getOrderTime());
			if(request.getOrderTime().length() != 14)throw new Exception();
		} catch (Exception e) {
			request.setRespInfo("029");
			return false;
		}
		if(request.getBizType().length() > 0){
			if(PayConstant.MER_BIZ_TYPE.get(request.getBizType())==null){
				request.setRespInfo("030");
				return false;
			}
		}
		if(!"1".equals(request.getRptType())){
			request.setRespInfo("031");
			return false;
		}
		if(!"0".equals(request.getPayMode())){
			request.setRespInfo("032");
			return false;
		}
		return true;
	}
	private boolean checkCertPayOrderRequest(PayRequest request) throws UnsupportedEncodingException {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantOrderId.length() == 0 
				|| request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		try {
			long merchantOrderAmt = Long.parseLong(request.merchantOrderAmt);
			if(merchantOrderAmt <= 0 || merchantOrderAmt > 100000000000l)throw new Exception();
		} catch (Exception e) {
			request.setRespInfo("018");
			return false;
		}
		if(request.merchantOrderDesc.length() > 0 
			&& request.merchantOrderDesc.getBytes(encode).length > 128){
			request.setRespInfo("019");
			return false;
		}
		if(request.merchantNotifyUrl.length() > 200
			|| !request.merchantNotifyUrl.toLowerCase().trim().startsWith("http")){
			request.setRespInfo("020");
			return false;
		}
		if(request.merchantFrontEndUrl.length() > 200
			|| (request.merchantFrontEndUrl.length() !=0 && 
				!request.merchantFrontEndUrl.toLowerCase().trim().startsWith("http"))){
			request.setRespInfo("021");
			return false;
		}
		if(request.userName.length() > 0 
			&& request.userName.getBytes(encode).length > 50){
			request.setRespInfo("035");
			return false;
		}
		if(request.payerId.length() > 0 
			&& request.payerId.getBytes(encode).length > 50){
			request.setRespInfo("051");
			return false;
		}
		if(request.salerId.length() > 0 && request.salerId.getBytes(encode).length > 50){
			request.setRespInfo("041");
			return false;
		}
		if(request.guaranteeAmt.length()>0){
			try {
				long guaranteeAmt = Long.parseLong(request.guaranteeAmt);
				if(guaranteeAmt < 0 || guaranteeAmt > 999999999999l)throw new Exception();
			} catch (Exception e) {
				request.guaranteeAmt="0";
			}
		}
		if(PayConstant.CERT_TYPE.get(request.getCredentialType())==null){
			request.setRespInfo("026");
			return false;
		}
		if(request.getCredentialNo().length()==0
			||request.getCredentialNo().length()>20){
			request.setRespInfo("027");
			return false;
		}
		try {			
			String m = request.userMobileNo.trim();
			if(m.length() > 0){
				new Long(m);
				if(m.length() != 11 || !m.startsWith("1")){
					request.setRespInfo("022");
					return false;
				}
			}
		} catch (Exception e) {
			request.setRespInfo("022");
			return false;
		}
		//取得卡类型，0借记卡、1贷记卡
		PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(request.cardNo);
		if((request.cardNo.length() > 0 && request.cardNo.getBytes(encode).length > 25)
				||cardBin==null
				||(!"0".equals(cardBin.cardType)&&!"1".equals(cardBin.cardType))){
			request.setRespInfo("054");
			return false;
		}
		request.cardType = cardBin.cardType;
		request.accountType = request.cardType; 
		//1贷记卡
		if("1".equals(cardBin.cardType)){
			if(request.cvv2.length() != 3||request.validPeriod.length() != 4){
				request.setRespInfo("055");
				return false;
			}
		}
		return true;
	}
	private boolean checkCertPayOrderH5Request(PayRequest request) throws UnsupportedEncodingException {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantOrderId.length() == 0 
				|| request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		if(request.merchantName.trim().length() > 0 
				&& (request.merchantName.getBytes(encode).length > 24 
						|| !request.merchantName.equals(merchant.storeName))){
			request.setRespInfo("016");
			return false;
		}
		try {
			long merchantOrderAmt = Long.parseLong(request.merchantOrderAmt);
			if(merchantOrderAmt <= 0 || merchantOrderAmt > 100000000000l)throw new Exception();
		} catch (Exception e) {
			request.setRespInfo("018");
			return false;
		}
		if(request.merchantOrderDesc.length() > 0 
			&& request.merchantOrderDesc.getBytes(encode).length > 128){
			request.setRespInfo("019");
			return false;
		}
		if(request.merchantNotifyUrl.length() > 200
			|| !request.merchantNotifyUrl.toLowerCase().trim().startsWith("http")){
			request.setRespInfo("020");
			return false;
		}
		if(request.merchantFrontEndUrl.length() > 200
			|| (request.merchantFrontEndUrl.length() !=0 && 
				!request.merchantFrontEndUrl.toLowerCase().trim().startsWith("http"))){
			request.setRespInfo("021");
			return false;
		}
		if(request.payerId.length() > 0 
			&& request.payerId.getBytes(encode).length > 50){
			request.setRespInfo("051");
			return false;
		}
		if(request.salerId.length() > 0 && request.salerId.getBytes(encode).length > 100){
			request.setRespInfo("041");
			return false;
		}
		if(request.guaranteeAmt.length()>0){
			try {
				long guaranteeAmt = Long.parseLong(request.guaranteeAmt);
				if(guaranteeAmt < 0 || guaranteeAmt > 999999999999l)throw new Exception();
			} catch (Exception e) {
				request.guaranteeAmt="0";
			}
		}
		return true;
	}
	private boolean checkWeiXinScanOrder(PayRequest request) throws UnsupportedEncodingException {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantOrderId.length() == 0 
				|| request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		try {
			long merchantOrderAmt = Long.parseLong(request.merchantOrderAmt);
			if(merchantOrderAmt <= 0 || merchantOrderAmt > 100000000000l)throw new Exception();
		} catch (Exception e) {
			request.setRespInfo("018");
			return false;
		}
		if(request.merchantOrderDesc.length() > 0 
			&& request.merchantOrderDesc.getBytes(encode).length > 128){
			request.setRespInfo("019");
			return false;
		}
		if(request.merchantNotifyUrl.length() > 200
			|| !request.merchantNotifyUrl.toLowerCase().trim().startsWith("http")){
			request.setRespInfo("020");
			return false;
		}
		if(request.userName.length() > 0 
			&& request.userName.getBytes(encode).length > 50){
			request.setRespInfo("035");
			return false;
		}
		if(request.payerId.length() > 0 
			&& request.payerId.getBytes(encode).length > 50){
			request.setRespInfo("051");
			return false;
		}
		if(request.salerId.length() > 0 && request.salerId.getBytes(encode).length > 100){
			request.setRespInfo("041");
			return false;
		}
		if(request.guaranteeAmt.length()>0){
			try {
				long guaranteeAmt = Long.parseLong(request.guaranteeAmt);
				if(guaranteeAmt < 0 || guaranteeAmt > 999999999999l)throw new Exception();
			} catch (Exception e) {
				request.guaranteeAmt="0";
			}
		}
		return true;
	}
	private boolean checkTranUser(PayRequest request) throws UnsupportedEncodingException {
		PayTranUserInfo payer = (PayTranUserInfo)request.tranUserMap.get(request.payerId);
		PayTranUserInfo saler = (PayTranUserInfo)request.tranUserMap.get(request.salerId);
		if(payer != null){
			//名单状态标志 1白 2灰 3黑 4红
			if("3".equals(payer.accProfile.listStsFlg)){
				request.setRespInfo("045");
				request.respDesc="买家为"+request.respDesc;
				return false;
			}
			//账户状态 0-正常；1-未激活；2-冻结；9-已销户
			if(!"0".equals(payer.accProfile.acStatus)){
				request.setRespInfo("046");
				request.respDesc=request.respDesc+"(买家："+PayConstant.USER_ACC_STATUS.get(payer.accProfile.acStatus)+")";
				return false;
			}
		}
		if(saler != null){
			//名单状态标志 1白 2灰 3黑 4红
			if("3".equals(saler.accProfile.listStsFlg)){
				request.setRespInfo("045");
				request.respDesc="卖家为"+request.respDesc;
				return false;
			}
			//账户状态 0-正常；1-未激活；2-冻结；9-已销户
			if(!"0".equals(saler.accProfile.acStatus)){
				request.setRespInfo("046");
				request.respDesc=request.respDesc+"(卖家："+PayConstant.USER_ACC_STATUS.get(saler.accProfile.acStatus)+")";
				return false;
			}
		}
		return true;
	}
	private boolean checkRefundOrderRequest(PayRequest request) throws UnsupportedEncodingException {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantOrderId.length() == 0 
				|| request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		try {
			long merchantRefundAmt = Long.parseLong(request.merchantRefundAmt);
			if(merchantRefundAmt <= 0 || merchantRefundAmt >= 100000000l)throw new Exception();
		} catch (Exception e) {
			request.setRespInfo("034");
			return false;
		}
		return true;
	}
	private boolean checkCertPayReSmsRequest(PayRequest request) throws Exception {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantOrderId.length() == 0 
				|| request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		return true;
	}
	private boolean checkCertPayConfirmRequest(PayRequest request) throws Exception {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantOrderId.length() == 0 || request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		if( request.checkCode.length() == 0 || request.checkCode.length() > 10){
			request.setRespInfo("056");
			return false;
		}
		return true;
	}
	private boolean checkRealNameAuthRequest(PayRequest request) throws Exception {
		if(request.tranId.length() == 0 || request.tranId.getBytes(encode).length > 32||request.tranId.indexOf('_')>=0){
			request.setRespInfo("076");
			return false;
		}
		int maxAuthCount = 1000;
		try {maxAuthCount = Integer.parseInt(PayConstant.PAY_CONFIG.get("REAL_NAME_AUTH_MAX_COUNT"));} catch (Exception e2) {}
		if(request.receivePayList.size()==0|| request.receivePayList.size()>maxAuthCount){
			request.setRespInfo("063");
			request.setRespDesc(request.getRespDesc()+"(不能包含字符\"_\")");
			request.respDesc="认证数量每次1—"+maxAuthCount+"条";
			return false;
		}
		Map tmp = new HashMap();
		for(int i=0; i<request.receivePayList.size(); i++){
			PayReceiveAndPay rp = request.receivePayList.get(i);
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(rp.accNo);
			if(cardBin==null){
				rp.setRespInfo("054");
				continue;
			}
			if(rp.sn.length()==0 || rp.sn.length()>32||rp.sn.indexOf('_')>=0){
				rp.setRespInfo("069");
				rp.respDesc = rp.respDesc +"(不能包含字符\"_\")";
				continue;
			}
			rp.bankGeneralName=cardBin.bankName;
			rp.accType = cardBin.cardType;
			if(rp.accName.length()==0||rp.accName.length()>60){
				rp.setRespInfo("035");
				continue;
			}
			if(rp.credentialType.length() != 2){
				rp.setRespInfo("026");
				continue;
			}
			if(rp.credentialNo.length()<10||rp.credentialNo.length()>22){
				rp.setRespInfo("027");
				continue;
			}
			try {
				if(rp.tel.length()!=11||!rp.tel.startsWith("1"))throw new Exception();
				Long.parseLong(rp.tel);
			} catch (Exception e) {
				rp.setRespInfo("022");
				continue;
			}
			if(tmp.get(rp.sn)!=null){
				request.setRespInfo("064");
				return false;
			} else tmp.put(rp.sn, "");
		}
		if(new PayRealNameAuthDAO().existTranIdSnIds(request)){
			request.setRespInfo("065");
			return false;
		}
		return true;
	}
	private boolean checkReceivePayRequest(PayRequest request) throws Exception {
		if(request.tranId.length() == 0 || request.tranId.getBytes(encode).length > 20||request.tranId.indexOf('_')>=0){
			request.setRespInfo("076");
			request.setRespDesc(request.getRespDesc()+"(不能包含字符\"_\")");
			return false;
		}
		if(request.receivePayNotifyUrl.length() > 200
			|| !request.receivePayNotifyUrl.toLowerCase().trim().startsWith("http")){
			request.setRespInfo("020");
			return false;
		}
		if(!"0".equals(request.receivePayType)&&!"1".equals(request.receivePayType)){
			request.setRespInfo("070");
			return false;
		}
		if(!"0".equals(request.accountProp)&&!"4".equals(request.accountProp)){
			request.setRespInfo("072");
			return false;
		}
		if("0".equals(request.accountProp)){//对私银行信息处理
			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(request.accNo);
			if(cardBin==null) {
				request.setRespInfo("054");
				return false;
			}
			request.bankGeneralName=cardBin.bankName;
			request.accType = cardBin.cardType;
			if(request.accName.length()==0||request.accName.length()>60){
				request.setRespInfo("035");
				return false;
			}
		}
		PayBank bank = PayCardBinService.BANK_NAME_MAP.get(request.bankGeneralName);
		if(bank==null){
			request.setRespInfo("054");
			return false;
		}
		request.bankName = request.bankGeneralName;
		request.bankId = bank.bankNo;
		try {
			long amount = Long.parseLong(request.amount);
			if(amount <= 0 || amount >= 10000000000l)throw new Exception();
		} catch (Exception e) {
			request.setRespInfo("034");
			return false;
		}
		if("0".equals(request.accountProp)&&PayConstant.CERT_TYPE.get(request.credentialType)==null){
			request.setRespInfo("026");
			return false;
		}
		if("0".equals(request.accountProp)&&(request.credentialNo.length()<10||request.credentialNo.length()>22)){
			request.setRespInfo("027");
			return false;
		}
		try {
			if("0".equals(request.accountProp)){
				if(request.tel.length()!=11||!request.tel.startsWith("1"))throw new Exception();
				Long.parseLong(request.tel);
			}
		} catch (Exception e) {
			request.setRespInfo("022");
			return false;
		}
		if(request.summary.getBytes(encode).length > 256){
			request.setRespInfo("073");
			return false;
		}
		if(new PayReceiveAndPayDAO().existTranIdSnIds(request)){
			request.setRespInfo("065");
			return false;
		}
		return true;
	}
	private boolean checkReceivePayBatchRequest(PayRequest request) throws Exception {
		int maxCount = 1000;
		try {maxCount = Integer.parseInt(PayConstant.PAY_CONFIG.get("RECEIVE_PAY_MAX_COUNT"));} catch (Exception e2) {}
		if(request.receivePayList.size()==0|| request.receivePayList.size()>maxCount){
			request.setRespInfo("075");
			request.setRespDesc(request.getRespDesc()+"(1—"+maxCount+"笔)");
			return false;
		}
		if(request.tranId.length() == 0 || request.tranId.getBytes(encode).length > 32||request.tranId.indexOf('_')>=0){
			request.setRespInfo("076");
			request.setRespDesc(request.getRespDesc()+"(不能包含字符\"_\")");
			return false;
		}
		if(request.receivePayNotifyUrl.length() > 200
			|| !request.receivePayNotifyUrl.toLowerCase().trim().startsWith("http")){
			request.setRespInfo("020");
			return false;
		}
		if(!"0".equals(request.receivePayType)&&!"1".equals(request.receivePayType)){
			request.setRespInfo("070");
			return false;
		}
		if(!"0".equals(request.accountProp)&&!"4".equals(request.accountProp)){
			request.setRespInfo("072");
			return false;
		}
		Map tmp = new HashMap();
		for(int i = 0; i<request.receivePayList.size(); i++){
			PayReceiveAndPay rp = request.receivePayList.get(i);
			if(rp.sn.length()==0 || rp.sn.length()>32||rp.sn.indexOf('_')>=0){
				request.setRespInfo("069");
				request.respDesc = request.respDesc +"，明细号："+rp.sn+"不能包含字符\"_\")";
				return false;
			}
			if("0".equals(request.accountProp)){//对私银行信息处理
				PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(rp.accNo);
				if(cardBin==null) {
					request.setRespInfo("054");
					request.respDesc = request.respDesc +"，明细号："+rp.sn;
					return false;
				}
				rp.bankGeneralName=cardBin.bankName;
				rp.accType = cardBin.cardType;
				if(rp.accName.length()==0||rp.accName.getBytes("utf-8").length>60){
					request.setRespInfo("035");
					request.respDesc = request.respDesc +"，明细号："+rp.sn;
					return false;
				}
			}
			PayBank bank = PayCardBinService.BANK_NAME_MAP.get(rp.bankGeneralName);
			if(bank==null){
				request.setRespInfo("054");
				request.respDesc = request.respDesc +"，无银行信息，明细号："+rp.sn;
				return false;
			}
			rp.bankName = rp.bankGeneralName;
			rp.bankId = bank.bankNo;
			if(rp.amount <= 0 || rp.amount >= 100000000000l) {
				request.setRespInfo("034");
				request.respDesc = request.respDesc +"，明细号："+rp.sn;
				return false;
			}
			if("0".equals(request.accountProp)&&PayConstant.CERT_TYPE.get(rp.credentialType)==null){
				request.setRespInfo("026");
				request.respDesc = request.respDesc +"，明细号："+rp.sn;
				return false;
			}
			if("0".equals(request.accountProp)&&(rp.credentialNo.length()<10||rp.credentialNo.length()>22)){
				request.setRespInfo("027");
				request.respDesc = request.respDesc +"，明细号："+rp.sn;
				return false;
			}
			try {
				if("0".equals(request.accountProp)){
					if(rp.tel.length()!=11||!rp.tel.startsWith("1"))throw new Exception();
					Long.parseLong(rp.tel);
				}
			} catch (Exception e) {
				request.setRespInfo("022");
				request.respDesc = request.respDesc +"，明细号："+rp.sn;
				return false;
			}
			if(rp.summary.getBytes(encode).length > 256){
				request.setRespInfo("073");
				request.respDesc = request.respDesc +"，明细号："+rp.sn;
				return false;
			}
			if(tmp.get(rp.sn)!=null){
				request.setRespInfo("064");
				return false;
			} else tmp.put(rp.sn, "");
		}
		if(new PayReceiveAndPayDAO().existTranIdSnIds(request)){
			request.setRespInfo("065");
			return false;
		}
		return true;
	}
	private boolean checkReceivePayQueryRequest(PayRequest request) throws Exception {
		if(request.queryTranId.length() == 0 || request.queryTranId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		return true;
	}
	private boolean checkQueryOrderRequest(PayRequest request) throws UnsupportedEncodingException {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantOrderId.length() == 0 
				|| request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		return true;
	}
	private boolean checkCancelOrderRequest(PayRequest request) throws UnsupportedEncodingException {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantOrderId.length() == 0 
				|| request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		return true;
	}
	private boolean checkAccountFileRequest(PayRequest request) throws UnsupportedEncodingException {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		try {
			if(request.accountDate==null || request.accountDate.length()!=8)throw new Exception();
			new java.text.SimpleDateFormat("yyyyMMdd").parse(request.accountDate);
		} catch (Exception e) {
			request.setRespInfo("025");
			return false;
		}
		return true;
	}
	private boolean checkGuaranteeNoticeRequest(PayRequest request) throws UnsupportedEncodingException {
		if(request.merchantId.length() > 24){
			request.setRespInfo("015");
			return false;
		}
		if(request.merchantOrderId.length() == 0 
				|| request.merchantOrderId.getBytes(encode).length > 32){
			request.setRespInfo("017");
			return false;
		}
		if(!"0".equals(request.guaranteeNoticeStatus) && !"1".equals(request.guaranteeNoticeStatus)){
			request.setRespInfo("048");
			return false;
		}
		return true;
	}
	public byte [] createErrorZipFile(PayRequest payRequest){
		byte [] b = null;
		String rad = Tools.getUniqueIdentify();
		try {
			String zipFile = JWebConstant.APP_PATH+"/dat/merchant_account_file/error_"+rad+".zip";
			String errFile = JWebConstant.APP_PATH+"/dat/merchant_account_file/error_"+rad+".txt"; 
			FileOutputStream fos = new FileOutputStream(errFile);
			fos.write((payRequest.respDesc+"("+payRequest.respCode+")").getBytes());
			fos.close();
			Tools.zipFiles(new File[]{new File(errFile)}, new File(zipFile));
			FileInputStream fis = new FileInputStream(zipFile);
			b = new byte[fis.available()];
			fis.read(b);
			fis.close();
			new File(errFile).delete();
			new File(zipFile).delete();			
		} catch (Exception e) {}
		return b;
	}
	/**
	 * 获取支付终端类型          终端类型，0 pc，1手机，2 pos,3 其他 默认0
	 * @param request
	 * @return
	 */ 
	public String getTerminalType(HttpServletRequest request){
		//暂时只判断  pc/手机
		String requestHeader = request.getHeader("user-agent");
		String[] deviceArray = new String[]{"android","mac os","windows phone"};
		//为空 pc
        if(requestHeader == null){
        	return "0";
        }
        requestHeader = requestHeader.toLowerCase();
        for(int i=0;i<deviceArray.length;i++){
            if(requestHeader.indexOf(deviceArray[i])>0){
            	//手机
                return "1";
            }
        }
        //pc
        return "0"; 
	}
	public static boolean checkPayTime(PayRequest payRequest){
		if("1".equals(payRequest.receivePayType)){
			String time = PayConstant.PAY_CONFIG.get("PAY_TIME");			
			if(time==null||"-".equals(time)||"—".equals(time))return true;
			String [] dayTimes = time.replaceAll("，",",").replaceAll("；",";").replaceAll("：",":").replaceAll("—","-").split(";");
			Map<String,long [][]> map = new HashMap<String,long [][]>();
			Calendar cl = Calendar.getInstance();
			Date d = cl.getTime();
			//1,9:30-10:20;2,9:30-10:20
			for(int i=0; i<dayTimes.length; i++){
				try{
					String [] weeks = dayTimes[i].split(",");//1,9:30-10:20
					String [] times = weeks[1].split("_");//9:30-10:20_13:30-15:20
					String week = weeks[0];
					long [][] tis = new long[times.length][];
					for(int j=0; j<times.length; j++){
						try {
							String [] tt = times[j].split("-");
							String tStart = tt[0],tEnd=tt[1];
							tStart = new SimpleDateFormat("yyyy-MM-dd").format(d)+" "+(tStart.split(":")[0].length()==1?"0"+tStart:tStart)+":00";
							tEnd = new SimpleDateFormat("yyyy-MM-dd").format(d)+" "+(tEnd.split(":")[0].length()==1?"0"+tEnd:tEnd)+":59";
							long tiStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tStart).getTime();
							long tiEnd =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tEnd).getTime();
							tis[j] = new long[]{tiStart,tiEnd};
						} catch (Exception e) {continue;}
					}
					map.put(week, tis);
				} catch (Exception e) {continue;}
			}
			long [][] tis = map.get(String.valueOf(cl.get(Calendar.DAY_OF_WEEK)-1));
			if(tis!=null){
				boolean ret = false;
				for(int i=0; i<tis.length; i++){
					if(d.getTime()>=tis[i][0]&&d.getTime()<=tis[i][1]){
						ret = true;
						break;
					}
				}
				return ret;
			}
		}
		return true;
	}
	/**
	 * 保存商户访问次数信息，最近多少秒内某服务访问信息
	 * 商户号_服务号：时间，次数
	 */
	public static Map<String,long[]> PAY_SERVICE_ACCESS_FREQUENCY = new Hashtable<String,long[]>();
	private boolean checkAccessFrequency(PayRequest payRequest, String app) {
		//变量：【PAY_SERVICE_FREQUENCY_CreateOrder】,值【30,5】,表示30秒之内最多5次访问
		String frequency = PayConstant.PAY_CONFIG.get("PAY_SERVICE_FREQUENCY_"+app);
		if(frequency==null)return true;
		long curT = System.currentTimeMillis();
		try {
			String [] tmp = frequency.trim().split(",");
			if(tmp.length!=2)return true;
			long second = Long.parseLong(tmp[0])*1000;
			long freq = Long.parseLong(tmp[1]);
			long [] merAccInfo = PAY_SERVICE_ACCESS_FREQUENCY.get(payRequest.merchant.custId+"_"+app);
			if(merAccInfo==null||curT-merAccInfo[0]>second){
				PAY_SERVICE_ACCESS_FREQUENCY.put(payRequest.merchant.custId+"_"+app, new long[]{curT,1l});
				return true;
			}
			if(merAccInfo[1]>=freq)return false;
			PAY_SERVICE_ACCESS_FREQUENCY.put(payRequest.merchant.custId+"_"+app, new long[]{merAccInfo[0],++merAccInfo[1]});
		} catch (Exception e) {
			log.info(PayUtil.exceptionToString(e));
		}
		return true;
	}
	static{
		Timer timer = new Timer();
		timer.schedule(new ClearPayServiceAccessFrequency(),5*60*1000,5*60*1000);//每5分钟清理一次
	}
}
class ClearPayServiceAccessFrequency extends TimerTask {
	@Override
	public void run() {
		try {
			Iterator<String> it = PayInterfaceService.PAY_SERVICE_ACCESS_FREQUENCY.keySet().iterator();
			long curT = System.currentTimeMillis();
			while(it.hasNext()){
				String key = it.next();
				long [] tmp = PayInterfaceService.PAY_SERVICE_ACCESS_FREQUENCY.get(key);
				if(curT-tmp[0]>5*60*1000)PayInterfaceService.PAY_SERVICE_ACCESS_FREQUENCY.remove(key);
			}
		} catch (Exception e) {}
	}
}