package com.pay.merchantinterface.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.PayConstant;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.refund.dao.PayRefund;


public class MerchantXmlUtil {
	private static DocumentBuilderFactory factory = null;
	private static DocumentBuilder builder = null;
	private Document document;
	static {
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 交易通知请求转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String orderNotifyToXml(PayProductOrder prdOrder,
			List orderList,List refundList,List cancelList) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("application", "NotifyOrder");
		root.setAttribute("version", PayConstant.PAY_CONFIG.get("merchant_interface_version"));
		root.setAttribute("merchantId", prdOrder.merno);
		root.setAttribute("merchantOrderId", prdOrder.prdordno);
		Element deduct = document.createElement("deductList");
		root.appendChild(deduct);
		for(int i = 0; i<orderList.size(); i++){
			PayOrder order = (PayOrder)orderList.get(i);
			Element item = document.createElement("item");
			item.setAttribute("payOrderId",order.payordno);
			item.setAttribute("payAmt",order.txamt.toString());
			item.setAttribute("payTime",new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(order.actdat));
			item.setAttribute("payStatus",order.ordstatus);
			item.setAttribute("payDesc",PayConstant.PAY_STATUS.get(order.ordstatus));
			deduct.appendChild(item);
		}
		Element refund = document.createElement("refundList");
		root.appendChild(refund);
		for(int i = 0; i<refundList.size(); i++){
			PayRefund payRefund = (PayRefund)refundList.get(i);
			Element item = document.createElement("item");
			item.setAttribute("merchantRefundAmt",payRefund.rfamt.toString());
			item.setAttribute("refundStatus",payRefund.banksts);
			item.setAttribute("refundDesc",PayConstant.REFUND_STATUS.get(payRefund.banksts));
			item.setAttribute("refundTime",new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			refund.appendChild(item);
		}
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 收付款通知请求转换成xml
	 * @param payRequest
	 * @param notifyList
	 * @return
	 */
	public String receivePayNotifyToXml(PayRequest payRequest,List <PayReceiveAndPay>notifyList) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("application", "ReceivePayNotify");
		root.setAttribute("version", PayConstant.PAY_CONFIG.get("merchant_interface_version"));
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("tranId", payRequest.tranId);
		root.setAttribute("timestamp", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		Element tranList = document.createElement("tranList");
		root.appendChild(tranList);
		for(int i = 0; i<notifyList.size(); i++){
			PayReceiveAndPay rp = notifyList.get(i);
			Element item = document.createElement("item");
			if(!(payRequest.merchantId+"_").equals(rp.sn))item.setAttribute("sn",rp.sn);
			item.setAttribute("respCode",rp.retCode);
			item.setAttribute("respDesc",rp.errorMsg);
			tranList.appendChild(item);
		}
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 退款请求转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String errorToXml(PayRequest payRequest) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		if(payRequest.merchantId.length()!=0)root.setAttribute("merchantId",payRequest.merchantId);
		if(payRequest.merchantOrderId.length()!=0)root.setAttribute("merchantOrderId",payRequest.merchantOrderId);
		if(payRequest.merchantName.length()!=0)root.setAttribute("merchantName",payRequest.merchantName);
		if(payRequest.merchantOrderAmt.length()!=0)root.setAttribute("merchantOrderAmt",payRequest.merchantOrderAmt);
		if(payRequest.merchantOrderDesc.length()!=0)root.setAttribute("merchantOrderDesc",payRequest.merchantOrderDesc);
		if(payRequest.merchantNotifyUrl.length()!=0)root.setAttribute("merchantNotifyUrl",payRequest.merchantNotifyUrl);
		if(payRequest.merchantFrontEndUrl.length()!=0)root.setAttribute("merchantFrontEndUrl",payRequest.merchantFrontEndUrl);
		if(payRequest.userMobileNo.length()!=0)root.setAttribute("userMobileNo",payRequest.userMobileNo);
		if(payRequest.credentialType.length()!=0)root.setAttribute("credentialType",payRequest.credentialType);
		if(payRequest.credentialNo.length()!=0)root.setAttribute("credentialNo",payRequest.credentialNo);
		if(payRequest.msgExt.length()!=0)root.setAttribute("msgExt",payRequest.msgExt);
		if(payRequest.orderTime.length()!=0)root.setAttribute("orderTime",payRequest.orderTime);
		if(payRequest.bizType.length()!=0)root.setAttribute("bizType",payRequest.bizType);
		if(payRequest.rptType.length()!=0)root.setAttribute("rptType",payRequest.rptType);
		if(payRequest.payMode.length()!=0)root.setAttribute("payMode",payRequest.payMode);
		if(payRequest.merchantRefundAmt.length()!=0)root.setAttribute("merchantRefundAmt",payRequest.merchantRefundAmt);
		if(payRequest.application.length()!=0)root.setAttribute("application",payRequest.application);
		if(payRequest.version.length()!=0)root.setAttribute("version",payRequest.version);
		if(payRequest.respCode.length()!=0)root.setAttribute("respCode",payRequest.respCode);
		if(payRequest.respDesc.length()!=0)root.setAttribute("respDesc",payRequest.respDesc);
		document.appendChild(root);
		payRequest.respXml = createXml(); 
		return payRequest.respXml;
	}
	/**
	 * 查询响应转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String queryOrderToXml(PayRequest payRequest,PayProductOrder prdOrder,
			List orderList,List refundList,List cancelList) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("application", payRequest.application);
		root.setAttribute("version", PayConstant.PAY_CONFIG.get("merchant_interface_version"));
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("merchantOrderId", payRequest.merchantOrderId);
		root.setAttribute("respCode", "000");
		root.setAttribute("respDesc", PayConstant.RESP_CODE_DESC.get("000"));
		if(prdOrder == null){
			root.appendChild(document.createElement("deductList"));
			root.appendChild(document.createElement("refundList"));
//			root.appendChild(document.createElement("cancelList"));
			document.appendChild(root);
			return createXml();
		}
		Element deduct = document.createElement("deductList");
		root.appendChild(deduct);
		for(int i = 0; i<orderList.size(); i++){
			PayOrder order = (PayOrder)orderList.get(i);
			Element item = document.createElement("item");
			item.setAttribute("payOrderId",order.payordno);
			item.setAttribute("payAmt",order.txamt.toString());
			item.setAttribute("payTime",new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(order.actdat));
			item.setAttribute("payStatus",order.ordstatus);
			item.setAttribute("payDesc",PayConstant.PAY_STATUS.get(order.ordstatus));
			deduct.appendChild(item);
		}
		Element refund = document.createElement("refundList");
		root.appendChild(refund);
		for(int i = 0; i<refundList.size(); i++){
			PayRefund payRefund = (PayRefund)refundList.get(i);
			Element item = document.createElement("item");
			item.setAttribute("merchantRefundAmt",payRefund.rfamt.toString());
			item.setAttribute("refundStatus",payRefund.banksts);
			item.setAttribute("refundDesc",PayConstant.REFUND_STATUS.get(payRefund.banksts));
			item.setAttribute("refundTime",new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			refund.appendChild(item);
		}
//		Element cancel = document.createElement("cancelList");
//		root.appendChild(cancel);
//		for(int i = 0; i<cancelList.size(); i++){
//			Element item = document.createElement("item");
//			item.setAttribute("cancelTime",new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
//			cancel.appendChild(item);
//		}
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 退款请求转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String refundOrderToXml(PayRequest payRequest) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("application", payRequest.application);
		root.setAttribute("version", PayConstant.PAY_CONFIG.get("merchant_interface_version"));
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("merchantOrderId", payRequest.merchantOrderId);
		root.setAttribute("respCode",payRequest.respCode);
		root.setAttribute("respDesc",payRequest.respDesc);
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 验证码重发请求转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String certPayReSmsToXml(PayRequest payRequest) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("merchantOrderId", payRequest.merchantOrderId);
		root.setAttribute("userMobileNo", payRequest.userMobileNo);
		root.setAttribute("respCode",payRequest.respCode);
		root.setAttribute("respDesc",payRequest.respDesc);
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 支付确认请求转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String certPayConfirmToXml(PayRequest payRequest) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("merchantOrderId", payRequest.merchantOrderId);
		root.setAttribute("bindId", payRequest.bindId);
		root.setAttribute("cardNo", payRequest.cardNo);
		root.setAttribute("cardType", payRequest.cardType);
		root.setAttribute("bankId", payRequest.bankId);
		root.setAttribute("bankName", payRequest.bankName);
		root.setAttribute("userMobileNo", payRequest.userMobileNo);
		root.setAttribute("cvv2", payRequest.cvv2);
		root.setAttribute("validPeriod", payRequest.validPeriod);
		root.setAttribute("respCode",payRequest.respCode);
		root.setAttribute("respDesc",payRequest.respDesc);
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 担保通知请求转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String guaranteeNoticeToXml(PayRequest payRequest) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("application", payRequest.application);
		root.setAttribute("version", PayConstant.PAY_CONFIG.get("merchant_interface_version"));
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("merchantOrderId", payRequest.merchantOrderId);
		root.setAttribute("respCode",payRequest.respCode);
		root.setAttribute("respDesc",payRequest.respDesc);
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 撤销请求转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String cancelOrderToXml(PayRequest payRequest) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("application", payRequest.application);
		root.setAttribute("version", PayConstant.PAY_CONFIG.get("merchant_interface_version"));
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("merchantOrderId", payRequest.merchantOrderId);
		root.setAttribute("respCode",payRequest.respCode);
		root.setAttribute("respDesc",payRequest.respDesc);
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 实名验证响应转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String realNameAuthToXml(PayRequest payRequest) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("application", payRequest.application);
		root.setAttribute("version", PayConstant.PAY_CONFIG.get("merchant_interface_version"));
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("tranId", payRequest.tranId);
		root.setAttribute("timestamp",payRequest.timestamp);
		root.setAttribute("respCode",payRequest.respCode);
		root.setAttribute("respDesc",payRequest.respDesc);
		Element authList = document.createElement("authList");
		root.appendChild(authList);
		for(int i = 0; i<payRequest.receivePayList.size(); i++){
			PayReceiveAndPay rp = payRequest.receivePayList.get(i);
			Element item = document.createElement("item");
			item.setAttribute("sn",rp.sn);
			item.setAttribute("respCode",rp.respCode);
			item.setAttribute("respDesc",rp.respDesc);
			authList.appendChild(item);
		}
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 代收付响应转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String receivePayToXml(PayRequest payRequest) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("application", payRequest.application);
		root.setAttribute("version", PayConstant.PAY_CONFIG.get("merchant_interface_version"));
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("tranId", payRequest.tranId);
		root.setAttribute("timestamp",payRequest.timestamp);
		root.setAttribute("respCode",payRequest.respCode);
		root.setAttribute("respDesc",payRequest.respDesc);
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 代收付查询响应转换成xml
	 * @param request 请求
	 * @return 请求的xml报文
	 */	
	public String receivePayQueryToXml(PayRequest payRequest) {
		document = builder.newDocument();
		Element root = document.createElement("message");
		root.setAttribute("application", payRequest.application);
		root.setAttribute("version", PayConstant.PAY_CONFIG.get("merchant_interface_version"));
		root.setAttribute("merchantId", payRequest.merchantId);
		root.setAttribute("tranId", payRequest.tranId);
		root.setAttribute("timestamp",payRequest.timestamp);
		root.setAttribute("respCode",payRequest.respCode);
		root.setAttribute("respDesc",payRequest.respDesc);
		Element queryList = document.createElement("queryList");
		root.appendChild(queryList);
		List list=payRequest.receivePayList;
		for(int i = 0; i<payRequest.receivePayList.size(); i++){
			PayReceiveAndPay rp = (PayReceiveAndPay)list.get(i);
			Element item = document.createElement("item");
			//批量
			if("1".equals(rp.tranType))item.setAttribute("sn",rp.sn.split("_")[1]);
			if("0".equals(rp.status)){
				item.setAttribute("respCode","074");
				item.setAttribute("respDesc","处理中");
			} else if("1".equals(rp.status)){
				item.setAttribute("respCode",rp.retCode);
				item.setAttribute("respDesc","074".equals(rp.retCode)?"处理中":rp.errorMsg);
			} else {
				if("000".equals(rp.retCode)){
					item.setAttribute("respCode","-1");
					item.setAttribute("respDesc",rp.errorMsg);
				} else {
					item.setAttribute("respCode",rp.retCode);
					item.setAttribute("respDesc","074".equals(rp.retCode)?"处理中":rp.errorMsg);
				}
			}
			queryList.appendChild(item);
		}
		document.appendChild(root);
		return createXml();
	}
	/**
	 * 生成xml
	 * @return
	 */
	private String createXml() {
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
}
