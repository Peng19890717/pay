package com.pay.merchantinterface.service;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import util.DataTransUtil;
import util.PayUtil;
import util.Tools;

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
import com.third.cx.AESUtil;
import com.third.cx.GatewayUtil;
import com.third.swt.util.HexConver;
import com.third.swt.util.HttpUtil;
import com.third.swt.util.MerchantMD5;
import com.third.swt.util.SignUtils;
import com.third.wsym.CommonUtil;
import com.third.wsym.MD5Util;
/**
 * 商务通接口服务类
 * @author Administrator
 *
 */
public class WSYMPayService {
	private static final Log log = LogFactory.getLog(WSYMPayService.class);
	/**
	 * 
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType 业务标示：//微信：BGWXZF 支付宝：BGZFBZF
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		//下单处理
		try {
			String version = "3";
			String merchantId = PayConstant.PAY_CONFIG.get("WSYM_MERCHANTID");
			String channelMerchantId = PayConstant.PAY_CONFIG.get("WSYM_CHANNELMERCHANTID");
			String orderId =payOrder.payordno;
			String orderAmount = String.valueOf(payOrder.txamt);//单位分。
			String orderDate = new java.text.SimpleDateFormat("YYYY-MM-DD").format(new Date());
			String currency="RMB";
			String transType ="0101";//固定只。
			String retUrl =PayConstant.PAY_CONFIG.get("WSYM_NOTIFY_URL");//异步通知。
			String bizType ="00";
			String returnUrl ="" ;
			String prdDisUrl ="";
			String prdName = CommonUtil.Str2Hex(PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")); 
			String prdShortName ="";
			String prdDesc ="";
			String merRemark ="";
			String rptType ="1";
			String prdUnitPrice ="";
			String buyCount ="";
			String defPayWay ="";
			String buyMobNo ="";
			String cpsFlg ="";
			String signType ="MD5";
			String sourceData = "versionId=" + version+ "&merchantId=" + merchantId
	                  + "&orderId=" + orderId+ "&orderAmount=" + orderAmount + "&orderDate=" + orderDate
	                  + "&currency=" +currency + "&transType=" + transType + "&retUrl=" + retUrl
	                  + "&bizType=" +bizType + "&returnUrl=" + returnUrl + "&prdDisUrl=" + prdDisUrl
	                  + "&prdName=" + prdName + "&prdShortName=" + prdShortName + "&prdDesc=" + prdDesc + "&merRemark=" + merRemark
	                  + "&rptType=" + rptType + "&prdUnitPrice=" + prdUnitPrice + "&buyCount=" + buyCount
	                  + "&defPayWay=" + defPayWay + "&buyMobNo=" + buyMobNo + "&cpsFlg=" + cpsFlg
	                  + "&signType=" + signType;
			log.info("网上有名支付请求签名参数:"+sourceData);
	        String signature = MD5Util.getMD5(sourceData + PayConstant.PAY_CONFIG.get("WSYM_MD5_KEY"));
	        String txnCod ="MerchantmerchantPay";
	        String isNoLogin ="1";
			String bankCode =productType;//BGWXZF:微信；BGZFBZF：支付宝。
			String subAppid ="";
			String openId ="";
			String params=sourceData+"&channelMerchantId="+channelMerchantId+"&signature="+signature+"&txnCod="+txnCod+"&isNoLogin="+isNoLogin
					+"&bankCode="+bankCode+"&subAppid="+subAppid+"&openId="+openId;
			log.info("网上有名支付请求上送数据:"+params);
			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("WSYM_PAY_URL"), params.getBytes("utf-8")),"utf-8");
			/**
			 * {"prdOrdNo":"1490075460696","retCode":"00000","retMsg":"已创建支付订单号T017032100000104",
			 * "tranStr":"https://qr.alipay.com/bax09842qtskfk77bmzi60a1","merchantId":"00000000000194",
			 * "ordAmt":"1000","signature":"f1a93a0c63c6ea609257affaa86fc3ac","payOrdNo":"T017032100000104"}
			 */
			log.info("网上有名支付响应数据:"+res);
			JSONObject jsonObject = JSON.parseObject(res);
			if("00000".equals(jsonObject.getString("retCode"))){
				return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
						"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
						"codeUrl=\""+jsonObject.getString("tranStr")+"\" respCode=\"000\" respDesc=\"成功\"/>";
			}else throw new Exception("网上有名通道异常");
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
			
			String endDate = "";
			String merchantId = PayConstant.PAY_CONFIG.get("WSYM_MERCHANTID");
			String channelMerchantId = PayConstant.PAY_CONFIG.get("WSYM_CHANNELMERCHANTID");
			String ordStatus="";
			String prdOrdNo =payOrder.payordno;
			String startDate ="";
			String versionId="3";
			 String sourceData = "endDate=" + endDate + "&merchantId=" + merchantId + "&ordStatus=" + ordStatus
	                 + "&prdOrdNo=" + prdOrdNo + "&startDate=" + startDate + "&versionId=" + versionId;
	        String signature = MD5Util.getMD5(sourceData + PayConstant.PAY_CONFIG.get("WSYM_MD5_KEY"));
			String params=sourceData+"&channelMerchantId="+channelMerchantId+"&signature="+signature;
			log.info("网上有名渠道补单请求数据:"+params);
			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("WSYM_QUERY_URL"), params.getBytes("utf-8")),"utf-8");
			/**
			 * {"retCode":"00000","retMsg":"查询订单成功",
			 * "orderList":
			 * [{"fee":"800","ordStatus":"90","prdOrdNo":"1490075460696","merchantId":"00000000000194",
			 * "orderTime":"20170321134247","ordAmt":"1000","payOrdNo":"T017032100000104"}],"signature":"8f44f45fd4d269c4dce810fc8f61b1ef"}
			 */
			log.info("网上有名渠道补单响应数据:"+res);
			JSONObject jsonObject = JSON.parseObject(res);
			if("00000".equals(jsonObject.getString("retCode"))){
				
				JSONArray  orderList = jsonObject.getJSONArray("orderList");
				JSONObject jsonObject_tmp = JSON.parseObject(orderList.get(0).toString());
				log.info(jsonObject_tmp.get("ordStatus"));
				if("01".equals(jsonObject_tmp.get("ordStatus"))){
					payOrder.ordstatus="01";//支付成功
		        	new NotifyInterface().notifyMer(payOrder);//支付成功
				}else if("02".equals(jsonObject_tmp.get("ordStatus"))){
					payOrder.ordstatus="02";//支付失败。
		        	new NotifyInterface().notifyMer(payOrder);//支付成功
				}
			}else throw new Exception("补单失败");
		} catch (Exception e){
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
    		PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
    		if("1".equals(payRequest.receivePayType)){//代付业务。
    			PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);//通过卡号获取卡类信息。
    			String accName = payRequest.accName;//收款的账户姓名，签名不用做urlEncoder，提交的时候需要做，urlEncoder编码。
    			String accTel ="";
    			String amount =payRequest.amount;//单位分。
    			String bankName =cardBin.bankName;//签名不用做urlEncoder，提交的时候需要做，urlEncoder编码。
    			String cardNo=payRequest.accNo;//
    			String isB2b="";
    			if("0".equals(payRequest.accountProp)){
    				 isB2b = "0";//固定值：0--B2C   1--B2B 
    			}else{
    				isB2b="1";
    			}
    			String lowerPayOrderNo =rp.id;//商户自行生成的订单唯一标识
    			String merchantId =PayConstant.PAY_CONFIG.get("WSYM_MERCHANTID");
    			String channelMerchantId = PayConstant.PAY_CONFIG.get("WSYM_CHANNELMERCHANTID");
    			String merchantPwd =PayConstant.PAY_CONFIG.get("WSYM_DF_PASSWORD");
    			String noticeUrl=PayConstant.PAY_CONFIG.get("WSYM_DF_NOTIFY_URL");
    			String payDesc ="";
    			String tranCode ="rtSinglePay";//交易码，固定值：rtSinglePay
    			
    			String sourceData = "accName="+accName+"&accTel="+accTel+"&amount="+amount+"&bankName="+bankName+
    					 "&cardNo="+cardNo+"&isB2b="+isB2b+"&lowerPayOrderNo="+lowerPayOrderNo+"&merchantId="+merchantId+
    					"&merchantPwd="+merchantPwd+"&noticeUrl="+noticeUrl+"&payDesc="+payDesc;
    			log.info("网上有名代付签名参数:"+sourceData);	
    	        String signature = MD5Util.getMD5(sourceData + PayConstant.PAY_CONFIG.get("WSYM_MD5_KEY"));
    			String params="accName="+URLEncoder.encode(accName,"UTF-8")+"&accTel="+accTel+"&amount="+amount+"&bankName="+URLEncoder.encode(bankName,"UTF-8")+
    					 "&cardNo="+cardNo+"&isB2b="+isB2b+"&lowerPayOrderNo="+lowerPayOrderNo+"&merchantId="+merchantId+
    					"&merchantPwd="+merchantPwd+"&noticeUrl="+noticeUrl+"&payDesc="+payDesc+"&tranCode="+tranCode+"&signature="+URLEncoder.encode(signature,"UTF-8")+"&channelMerchantId="+channelMerchantId;
    			log.info("网上有名代付请求参数:"+params);
    			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("WSYM_DF_PAY_URL"), params.getBytes("utf-8")),"utf-8");
    			log.info("网上有名代付响应参数:"+res);
    			/**
    			 * {"amount":"10000","rspMsg":"已受理","payDesc":"","singleFee":"4000","lowerPayOrderNo":"1490257356450",
    			 * "rspType":"S","signature":"2f0b40756c147ac76f08b431ca444af4","payTranId":"902017032300451586"}
    			 */
    			JSONObject jsonObject = JSON.parseObject(res);
    			if("S".equals(jsonObject.getString("rspType"))){
    					rp.status="0";
    					rp.errorMsg = "提交成功";
    		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
    			}else{
    				rp.status="2";
					rp.errorMsg = jsonObject.getString("rspMsg");
		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 代收付查询(单笔)
     * @param request
     * @return
     * @throws Exception
     */
    public boolean receivePaySingleQuery(PayRequest request,PayReceiveAndPay rp) throws Exception{
		try {
			String lowerPayOrderNo =rp.id;//订单号。
			String merchantId =PayConstant.PAY_CONFIG.get("WSYM_MERCHANTID");
			String channelMerchantId = PayConstant.PAY_CONFIG.get("WSYM_CHANNELMERCHANTID");
			String payTranId ="";
			String tranCode ="qySinglePay";
			String sourceData = "lowerPayOrderNo="+lowerPayOrderNo+"&merchantId="+merchantId+"&payTranId="+payTranId;
			log.info("网上有名代付查询签名数据:"+sourceData);
	        String signature = MD5Util.getMD5(sourceData + PayConstant.PAY_CONFIG.get("WSYM_MD5_KEY"));
			String params="lowerPayOrderNo="+lowerPayOrderNo+"&merchantId="+merchantId+"&payTranId="+payTranId+"&channelMerchantId="+channelMerchantId+"&tranCode="+tranCode+"&signature="+URLEncoder.encode(signature,"UTF-8");
			log.info("网上有名查询请求数据:"+params);
			String res = new String(new DataTransUtil().doPost(PayConstant.PAY_CONFIG.get("WSYM_DF_QUERY_URL"), params.getBytes("utf-8")),"utf-8");
			/**
			 * {"amount":"10000","payResult":"S","singleFee":"4000","lowerPayOrderNo":"1490250658357","rspType":"S",
			 * "signature":"50384eaaee174bcb045ba45f6e4851d2","payTranId":"902017032300451582"}
			 */
			log.info("网上有名代付查询响应数据:"+res);
			JSONObject jsonObject = JSON.parseObject(res);
			if("S".equals(jsonObject.getString("rspType"))){
				if("S".equals(jsonObject.getString("payResult"))){
					request.setRespCode("000");
					request.receiveAndPaySingle.setRespCode("000");
					request.receivePayRes = "0";
					request.respDesc="交易成功";
					rp.errorMsg = "交易成功";
					return true;
				}else if("E".equals(jsonObject.getString("payResult"))){
					request.setRespCode("-1");
					request.receiveAndPaySingle.setRespCode("000");
					request.receivePayRes = "-1";
					request.respDesc="交易失败";
					rp.errorMsg = request.respDesc;
					return false;
				}else{
					return false;
				}
			}else throw new Exception("查询失败");
		} catch (Exception e) {
				log.info(PayUtil.exceptionToString(e));
				request.setRespCode("-1");
				rp.setRespCode(request.respCode);
				request.receivePayRes = "-1";
				request.respDesc=e.getMessage();
				rp.errorMsg = e.getMessage();
				return false;
		}
    }
}
