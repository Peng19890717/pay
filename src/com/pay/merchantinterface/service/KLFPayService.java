package com.pay.merchantinterface.service;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.PayConstant;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.merchantinterface.dao.PayInterfaceDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayReceiveAndPay;
import com.pay.order.dao.PayReceiveAndPayDAO;
import com.third.klf.HttpClientUtil;
import com.third.klf.Md5Util;

public class KLFPayService {
	private static final Log log = LogFactory.getLog(KLFPayService.class);
	public static Map<String, String> status_map = new HashMap<String, String>();
	static {
		status_map.put("00","未付款");
		status_map.put("01","处理中");
		status_map.put("02","成功");
		status_map.put("03","失败");
		status_map.put("90","交易关闭");
	}
	/**
	 * 扫码
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @throws Exception
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayOrder payOrder,PayProductOrder prdOrder,String productType){
		try {
			String ProductName=PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC");//商品名称
			Map <String,String> paramsMap=new TreeMap<String, String>();
			paramsMap.put("saruLruid",PayConstant.PAY_CONFIG.get("KLF_MERNO"));
			paramsMap.put("out_trade_no",payOrder.payordno);
			paramsMap.put("transAmt",String.valueOf(payOrder.txamt));
			paramsMap.put("notify_url",PayConstant.PAY_CONFIG.get("KLF_PAY_NOTIFY_URL"));
			paramsMap.put("body", URLEncoder.encode(ProductName,"UTF-8"));
			paramsMap.put("frpcode",productType);//交易类型1、ALIPAY_NATIVE支付宝 2、WEIXIN_NATIVE微信	
			String ss =Md5Util.MD5(paramsMap+PayConstant.PAY_CONFIG.get("KLF_MD5_KEY"));
			paramsMap.put("sign", ss);
			JSONObject	json = JSONObject.fromObject(paramsMap);
			log.info("快乐付扫码请求==========="+json);
			String respData =HttpClientUtil.send(PayConstant.PAY_CONFIG.get("KLF_SC_PAY_URL"),json.toString(),"UTF-8");
			log.info("快乐付扫码响应==========="+respData);
			/**
			 * {"code_url":"weixin://wxpay/bizpayurl?pr=k3ivv13","errorMsg":"操作成功","out_trade_no":"J7ISBS6B3JLXPT2Q1",
			 * "resultCode":"100","saruLruid":"6000000158"}
			 */
			JSONObject respJson = JSONObject.fromObject(respData);
			if(!"100".equals(respJson.getString("resultCode"))||
					respJson.getString("code_url")==null||respJson.getString("code_url").length()==0){
				String retMsg = respJson.getString("errorMsg");
				throw new Exception(retMsg==null||retMsg.length()==0?"扫码支付渠道未知错误":retMsg);
			}
			payOrder.bankjrnno=respJson.getString("out_trade_no");//
			new PayInterfaceDAO().updatePayOrder(payOrder);
			return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
					"codeUrl=\""+respJson.getString("code_url")+"\" respCode=\"000\" respDesc=\"处理成功\"/>";			
		} catch (Exception e) {
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
			Map<String, String> sParaTemp = new TreeMap<String, String>();
			sParaTemp.put("saruLruid", PayConstant.PAY_CONFIG.get("KLF_MERNO"));
			sParaTemp.put("out_trade_no",payOrder.bankjrnno);
			String ss =Md5Util.MD5(sParaTemp+PayConstant.PAY_CONFIG.get("KLF_MD5_KEY"));
			sParaTemp.put("sign", ss);
			JSONObject	json = JSONObject.fromObject(sParaTemp);
			log.info("快乐付扫码查询请求==========="+json);
			String respData =HttpClientUtil.send(PayConstant.PAY_CONFIG.get("KLF_SC_QUERY_URL"),json.toString(),"UTF-8");
			log.info("快乐付扫码查询响应==========="+respData);
			/**
			 * {"out_trade_no":"J7ISBS6B3JLXPT2Q1","resultCode":"101","saruLruid":"6000000158"}
			 */
			JSONObject respJson = JSONObject.fromObject(respData);
			if("100".equals(respJson.getString("resultCode"))){
				payOrder.ordstatus="01";//支付成功
	        	payOrder.bankjrnno = respJson.getString("out_trade_no");
	        	new NotifyInterface().notifyMer(payOrder);//支付成功
			} else throw new Exception("支付渠道状态："+
					(status_map.get(respJson.getString("resultCode"))==null?respJson.getString("resultCode"):
						status_map.get(respJson.getString("resultCode"))));
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 代付
	 * @param payRequest
	 * @throws Exception
	 */
	public void receivePaySingle(PayRequest payRequest)throws Exception{
		try {
			PayReceiveAndPay rp = payRequest.receiveAndPaySingle;
    		PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);
    		if("1".equals(payRequest.receivePayType)){//代付业务。
    			TreeMap<String,String> treemap=new TreeMap<String,String>();
    			treemap.put("p1_MerchantNo",PayConstant.PAY_CONFIG.get("KLF_MERNO"));//商户号
    			treemap.put("p2_BatchNo", rp.id);//订单号
    			treemap.put("p3_Name",URLEncoder.encode(payRequest.accName,"UTF-8"));//开户名
    			treemap.put("p4_Backcard", payRequest.accNo);//卡号
    			treemap.put("p5_Amount", String.valueOf(rp.amount/100));//代付金额
    			treemap.put("p6_Province", URLEncoder.encode("北京","UTF-8"));//开户省
    			treemap.put("p7_City", URLEncoder.encode("北京","UTF-8"));//开户市
    			treemap.put("p8_SubbranchName", URLEncoder.encode(cardBin.bankName,"UTF-8"));//支行名称
    			treemap.put("p9_NotifyUrl", PayConstant.PAY_CONFIG.get("KLF_DF_NOTIFY_URL"));//回调地址
    			treemap.put("p10_BackName", URLEncoder.encode(cardBin.bankName,"UTF-8"));//总行名称
    			String ss = Md5Util.MD5(treemap+PayConstant.PAY_CONFIG.get("KLF_DF_MD5_KEY"));
    			treemap.put("sign", ss);
    			log.info("快乐付代付请求========="+treemap);
    			JSONObject	jsons = JSONObject.fromObject(treemap);
    			String str=	HttpClientUtil.send(PayConstant.PAY_CONFIG.get("KLF_DF_PAY_URL"), jsons.toString(),"UTF-8");
    			log.info("快乐付代付响应========="+str);
    			/**
    			 * {"errorMsg":"sign is error","p1_MerchantNo":"6000000158","rb_Code":"101","sign":"B1788FA92CCC335A111C67B8785D889D"}
    			 * {"errorMsg":"sign is error","p1_MerchantNo":"6000000158","rb_Code":"101","sign":"B1788FA92CCC335A111C67B8785D889D"}
    			 */
    			JSONObject respjson= JSONObject.fromObject(str);
    			TreeMap<String,String> respmap=new TreeMap<String,String>();
    			respmap.put("p1_MerchantNo",respjson.getString("p1_MerchantNo"));//订单号
    			respmap.put("errorMsg", respjson.getString("errorMsg"));
    			respmap.put("rb_Code", respjson.getString("rb_Code"));
    			respmap.put("p2_BatchNo", respjson.getString("p2_BatchNo"));
    			String rs = Md5Util.MD5(respmap+PayConstant.PAY_CONFIG.get("KLF_DF_MD5_KEY"));
    			if(rs.equals(respjson.getString("sign"))){
	    			if("100".equals(respjson.getString("rb_Code"))){
	    				rp.status="0";
    					rp.respCode="074";
    					rp.errorMsg="提交成功";
    					try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	    			}else{
	    				rp.status="2";
	    				rp.retCode="-1";
    					rp.errorMsg = "提交失败";
    		        	try{new PayReceiveAndPayDAO().updatePayReceiveAndPayById(rp);} catch(Exception e){}
	    			}
    			} else throw new Exception("代付提交渠道验签失败");
    		}
		} catch (Exception e) {
		}
	}
	 /**
     * 代收付查询(单笔)
     * @param payRequest
     * @return
     * @throws Exception
     */
	public boolean receivePaySingleQuery(PayRequest payRequest,PayReceiveAndPay rp)throws Exception{
		try {
			TreeMap<String,String> treemap=new TreeMap<String,String>();
			treemap.put("p1_MerchantNo",PayConstant.PAY_CONFIG.get("KLF_MERNO"));//商户号
			treemap.put("p2_BatchNo", rp.id);//批次号
			String signs = Md5Util.MD5(treemap+PayConstant.PAY_CONFIG.get("KLF_DF_MD5_KEY"));
			treemap.put("sign", signs);
			log.info("快乐付代付查询请求==========="+treemap);
			JSONObject	jsons = JSONObject.fromObject(treemap);
			String str=	HttpClientUtil.send(PayConstant.PAY_CONFIG.get("KLF_DF_QUERY_URL"), jsons.toString(), "UTF-8");
			log.info("快乐付代付查询响应==========="+str);
			JSONObject respjson= JSONObject.fromObject(str);
			if("100".equals(respjson.getString("rb_Code"))){
				payRequest.setRespCode("000");
				payRequest.receiveAndPaySingle.setRespCode("000");
				payRequest.receivePayRes = "0";
				payRequest.respDesc="交易成功";
				rp.errorMsg = "交易成功";
				return true;
			}else if ("102".equals(respjson.getString("rb_Code"))){
				payRequest.setRespCode("0");//处理中
				payRequest.receiveAndPaySingle.setRespCode("000");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc="处理中";
				rp.errorMsg = payRequest.respDesc;
				return false;
			}else if("101".equals(respjson.getString("rb_Code"))){
				payRequest.setRespCode("-1");
				payRequest.receiveAndPaySingle.setRespCode("-1");
				payRequest.receivePayRes = "-1";
				payRequest.respDesc="交易失败";
				rp.errorMsg = payRequest.respDesc;
				return false;
			}else throw new Exception(respjson.getString("errorMsg"));
		} catch (Exception e) {
			e.printStackTrace();
			payRequest.setRespCode("-1");
			rp.setRespCode(payRequest.respCode);
			payRequest.receivePayRes = "-1";
			payRequest.respDesc=e.getMessage();
			rp.errorMsg = e.getMessage();
			return false;
		}
	}
}