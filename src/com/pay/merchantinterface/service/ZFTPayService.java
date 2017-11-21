package com.pay.merchantinterface.service;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.DataTransUtil;

import com.PayConstant;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayChannelRotation;
import com.pay.coopbank.dao.PayChannelRotationDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
/**
 * 支付通接口服务类
 * @author Administrator
 *
 */
public class ZFTPayService {
	private static final Log log = LogFactory.getLog(ZFTPayService.class);
	/**
	 * 
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType 业务标示：//支付宝、微信 扫码产品编号分别为：alipay/wechat
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,
			String productType,PayChannelRotation channelRotation){
		//下单处理
		try {
			String merchant_id =channelRotation!=null?channelRotation.merchantId:PayConstant.PAY_CONFIG.get("ZFT_MERCHANT_ID");
			String version="1.0";
			String reqId=payOrder.payordno;
			String acquirerType =productType;//wechat/alipay
			String totalAmount=String.valueOf(payOrder.txamt);
			String notify_url =PayConstant.PAY_CONFIG.get("ZFT_NOTIFY_URL");
			String MD5key=channelRotation!=null?channelRotation.md5Key:PayConstant.PAY_CONFIG.get("ZFT_MD5KEY");
			String reqStr ="{\"version\":\""+version+"\",\"reqId\":\""+reqId+"\",\"totalAmount\":\""+totalAmount+"\",\"acquirerType\":\""+acquirerType+"\",\"notify_url\"" +
					":\""+notify_url+"\",\"merchant_id\":\""+merchant_id+"\",\"MD5key\":\""+getMD5(MD5key)+"\"}";
			String payUrl=PayConstant.PAY_CONFIG.get("ZFT_PAY_URL");
			String sign = getMD5(URLEncoder.encode(reqStr,"utf-8")).toUpperCase();
			StringBuffer bufer = new StringBuffer();
			bufer.append("merchant_id="+merchant_id);
			bufer.append("&version="+version);
			bufer.append("&reqId="+reqId);
			bufer.append("&acquirerType="+acquirerType);
			bufer.append("&totalAmount="+totalAmount);
			bufer.append("&notify_url="+notify_url);
			bufer.append("&sign="+sign);
			log.info("支付通扫码请求数据:"+bufer.toString());
			String payResult = new String(new DataTransUtil().doPost(payUrl,bufer.toString().getBytes("utf-8")),"utf-8");
			/**
			 * {"responseCode":"00","reqId":"1491545155008","qrCode":"weixin://wxpay/bizpayurl?pr=bG8R6cb"}
			 */
			log.info("支付通扫码响应数据:"+payResult);
			if(payResult!=null&&payResult.length()>0){
				JSONObject resJsonObj = JSONObject.fromObject(payResult);
				if("00".equals(resJsonObj.getString("responseCode"))){
					String qrCode = resJsonObj.getString("qrCode");
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
					"codeUrl=\""+qrCode+"\" respCode=\"000\" respDesc=\"成功\"/>";
				}else throw new Exception("支付通扫码下单失败");
			}else throw new Exception("支付通扫码下单失败");
		} catch(Exception e){
			e.printStackTrace();
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
				"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
				"codeUrl=\"\" respCode=\"-1\" respDesc=\""+e.getMessage()+"\"/>";
		}
	}
	/**
	 * 渠道补单
	 * @param payordno 订单号
	 * @throws Exception 异常捕获
	 */
	public void channelQuery(PayOrder payOrder) throws Exception{
		try {
			PayChannelRotation rotation = new PayChannelRotationDAO().getPayChannelRotationOrderNo(payOrder.payordno);//轮询信息
			String merchant_id = rotation!=null?rotation.merchantId:PayConstant.PAY_CONFIG.get("ZFT_MERCHANT_ID");
			String version="1.0";
			String reqId=payOrder.payordno;
			String MD5key=rotation!=null?rotation.md5Key:PayConstant.PAY_CONFIG.get("ZFT_MD5KEY");
			String reqStr ="{\"version\":\""+version+"\",\"reqId\":\""+reqId+"\",\"merchant_id\":\""+merchant_id+"\",\"MD5key\":\""+getMD5(MD5key)+"\"}";
			String payQueryUrl=PayConstant.PAY_CONFIG.get("ZFT_PAY_QUERY_URL");
			String sign = getMD5(URLEncoder.encode(reqStr,"utf-8")).toUpperCase();
			StringBuffer bufer = new StringBuffer();
			bufer.append("merchant_id="+merchant_id);
			bufer.append("&version="+version);
			bufer.append("&reqId="+reqId);
			bufer.append("&sign="+sign);
			log.info("支付通扫码查询请求数据:"+bufer.toString());
			String payResult = new String(new DataTransUtil().doPost(payQueryUrl,bufer.toString().getBytes("utf-8")),"utf-8");
			/**
			 * {"responseCode":"00","reqId":"1491533532196","totalAmount":"1","transAmount":"1","transTime":"20170407111743",
			 * "transType":"1","transStatus":"2","acquirerType":"wechat","walletTransId":"4008822001201704076146085629",
			 * "walletOrderId":"1704070000004045"}
			 */
			log.info("支付通扫码查询响应数据:"+payResult);
			if(payResult!=null&&payResult.length()>0){
				JSONObject resJsonObj = JSONObject.fromObject(payResult);
				if("00".equals(resJsonObj.getString("responseCode"))){
					if("2".equals(resJsonObj.getString("transStatus"))){
						payOrder.ordstatus="01";//支付成功
			        	new NotifyInterface().notifyMer(payOrder);//支付成功
					}else if("0".equals(resJsonObj.getString("transStatus"))){
						payOrder.ordstatus="02";//失败成功
			        	new NotifyInterface().notifyMer(payOrder);//失败成功
					}
				}else throw new Exception("支付通扫码查询失败");
			}else throw new Exception("支付通扫码查询失败");
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 实时代付。
	 * @param payRequest
	 * @throws Exception
	 */
	 public void receivePaySingleNew(PayRequest payRequest) throws Exception{
		 try {
			 if("1".equals(payRequest.receivePayType)){//代付业务。
				 PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
			     PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
				 String version ="v1.0";
				 String sign_type="MD5";
				 String serialNum =rp.id;
				 String accountType ="02";
				 String payprice = String.format("%.2f", (Double.parseDouble(payRequest.amount))/100d);//金额，单位元。
				 String receive_cardholder = payRequest.accName;//付款人姓名
				 String receive_bankaccount =payRequest.accNo;//账号
				 String receive_phone_num = payRequest.tel;//手机号
				 String receive_bank_num = payRequest.bankId;//p cardBin.bankName"中国建设银行股份有限公司";//银行名称。
				 String receive_bank_name="4".equals(payRequest.accountProp)?payRequest.bankName:cardBin.bankName;//联行号
				 String flag = "4".equals(payRequest.accountProp)?"01":"00";//“00”对私，“01”对公。
				 String purpose ="payOnLine";
				 String merchant_id = PayConstant.PAY_CONFIG.get("ZFT_NEW_DF_MERID");
				 String key =PayConstant.PAY_CONFIG.get("ZFT_NEW_DF_KEY");
				 String signStr = version + serialNum + accountType + payprice + receive_cardholder + receive_bankaccount + receive_phone_num + receive_bank_num+receive_bank_name+flag+purpose+merchant_id;
				 String sign = getMD5((URLEncoder.encode(signStr,"utf-8")+getMD5(key))).toUpperCase();
				 StringBuffer bufer = new StringBuffer();
				 bufer.append("version="+version);
				 bufer.append("&sign_type="+sign_type);
				 bufer.append("&serialNum="+serialNum);
				 bufer.append("&accountType="+accountType);
				 bufer.append("&payprice="+payprice);
				 bufer.append("&receive_cardholder="+receive_cardholder);
				 bufer.append("&receive_bankaccount="+receive_bankaccount);
				 bufer.append("&receive_phone_num="+receive_phone_num);
				 bufer.append("&receive_bank_num="+receive_bank_num);
				 bufer.append("&receive_bank_name="+receive_bank_name);
				 bufer.append("&merchant_id="+merchant_id);
				 bufer.append("&flag="+flag);
				 bufer.append("&purpose="+purpose);
				 bufer.append("&merchant_id="+merchant_id);
				 bufer.append("&sign="+sign);
				 log.info("支付通代付业务请求参数:"+bufer.toString());
				 String payResult = new String(new DataTransUtil().doPost
							(PayConstant.PAY_CONFIG.get("ZFT_NEW_DF_URL"),bufer.toString().getBytes("utf-8")),"utf-8");
				 log.info("支付通代付业务响应参数:"+payResult);
				 /**
				  * 成功:{"status":"A000","serialNum":"1505118370729","payprice":"1.01","receiveprice":"1.01",
				  * "transStat":"000000","response_msg":"成功","result_msg":"Success","sign":"3E8C3E86D2F4FD7ECC9ADC9C16F5028C"}
				  * 
				  * 失败:{"status":"F99","serialNum":"1505118531630","payprice":"1.01","receiveprice":"1.01","transStat":"111111",
				  * "response_msg":"校验失败:无效的银行编码","result_msg":"Failed","sign":"483FEAE99BAEE1CFAB2F25D2E2CDB1BD"}
				  * 
				  * 失败2：{"result_msg":"转账金额应大于1元。","sign":"F864B91204CED98CE6DD4E77372EE625"}
				  * 
				  * 失败3：该商户没有权限访问
				  */
				 JSONObject resJsonObj=null;
					//处理直接返回中文开始。
					try {
						 resJsonObj = JSONObject.fromObject(payResult);
					} catch (Exception e) {
						payRequest.respCode="-1";
						payRequest.respDesc=payResult;
						rp.status="2";
						rp.retCode="-1";
						rp.errorMsg = payResult;
			        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
			        	e.printStackTrace();
					}
					if(resJsonObj.get("status")!=null&&!resJsonObj.get("status").equals("")){
						//处理直接返回中文结束。
						if("A000".equals(resJsonObj.getString("status"))){
								payRequest.respCode="000";
								rp.status="1";
								rp.retCode="000";
								rp.errorMsg = "交易成功-实际到账以发卡银行为准";
								payRequest.respDesc=rp.errorMsg;
					        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
					        	List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
								list.add(rp);
								new PayInterfaceOtherService().receivePayNotify(payRequest,list);
						}else if ("U11".equals(resJsonObj.getString("status"))){ 
							payRequest.respCode="000";
							rp.status="0";
							rp.retCode="074";
							rp.errorMsg = "提交成功";
							payRequest.respDesc=rp.errorMsg;
				        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
						}else{
							payRequest.respCode="-1";
							rp.status="2";
							rp.retCode="-1";
							rp.errorMsg = resJsonObj.getString("response_msg");
							payRequest.respDesc=rp.errorMsg;
				        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
				        	List<PayReceiveAndPay> list = new ArrayList<PayReceiveAndPay>();
							list.add(rp);
							new PayInterfaceOtherService().receivePayNotify(payRequest,list);
						}
					}else{
						payRequest.respCode="-1";
						rp.status="2";
						rp.retCode="-1";
						rp.errorMsg = resJsonObj.getString("result_msg");
						payRequest.respDesc=rp.errorMsg;
			        	new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
					}
	    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
    /**
	  * 实时代收付查询
	  * @param payRequest
	  * @param rp
	  * @return
	  * @throws Exception
	  */
	public void receivePaySingleQuery(PayReceiveAndPay rp) throws Exception{
		try{
			String version ="v1.0";
			String sign_type="MD5";
			String bizDate =new java.text.SimpleDateFormat("yyyyMMdd").format(rp.createTime);//"YYYYMMDD"
			String serialNum =rp.id;
			String merchant_id = PayConstant.PAY_CONFIG.get("ZFT_NEW_DF_MERID");
			String key =PayConstant.PAY_CONFIG.get("ZFT_NEW_DF_KEY");
			String signStr = version + bizDate + serialNum +merchant_id;
			String sign = getMD5((URLEncoder.encode(signStr,"utf-8")+getMD5(key))).toUpperCase();
			StringBuffer bufer = new StringBuffer();
			bufer.append("version="+version);
			bufer.append("&sign_type="+sign_type);
			bufer.append("&serialNum="+serialNum);
			bufer.append("&bizDate="+bizDate);
			bufer.append("&merchant_id="+merchant_id);
			bufer.append("&sign="+sign);
			log.info("支付通代付查询请求参数:"+bufer.toString());
			String payResult = new String(new DataTransUtil().doPost
					(PayConstant.PAY_CONFIG.get("ZFT_NEW_DF_QUERY"),bufer.toString().getBytes("utf-8")),"utf-8");
			log.info("支付通代付查询响应"+payResult);
			/**
			 * {"status":"A000","serialNum":"1505118370729","payprice":"-1.01","transStat":"2006","response_msg":"交易成功",
			 * "result_msg":"Success","sign":"EBFF362FAB4671C1B28AD00DE3AF6FA9"}
			 */
			
			JSONObject resJsonObj = JSONObject.fromObject(payResult);
			//处理直接返回中文结束。
			if("A000".equals(resJsonObj.getString("status"))){
					rp.status="1";
					rp.retCode="000";
					rp.errorMsg = "交易成功";
					new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
			}else if("F99".equals(resJsonObj.getString("status"))){
					rp.status="2";
					rp.retCode="-1";
					rp.errorMsg = resJsonObj.getString("response_msg");
					new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
			}else if("U11".equals(resJsonObj.getString("status"))){
					rp.status="0";
					rp.retCode="074";
					rp.errorMsg = "处理中";
					new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);
			}
		} catch (Exception e){
			e.printStackTrace();
		}	
	}
	/**
	 * MD5
	 * @param s
	 * @return
	 */
	public static String  getMD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes("UTF-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
