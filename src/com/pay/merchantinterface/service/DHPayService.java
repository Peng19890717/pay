package com.pay.merchantinterface.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import com.PayConstant;
import com.pay.fee.dao.PayFeeRate;
import com.pay.fee.service.PayFeeRateService;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayProductOrder;
import com.third.daohe.JsonToMapUtil;

public class DHPayService {
	private static final Log log = LogFactory.getLog(DHPayService.class);
	public static Map<String, String> bankName_map = new HashMap<String, String>();
	static {
		bankName_map.put("ICBC","中国工商银行");
		bankName_map.put("ABC","中国农业银行");
		bankName_map.put("BOC","中国银行");
		bankName_map.put("CCB","中国建设银行");
		bankName_map.put("BOCOM","交通银行");
		bankName_map.put("CMB","招商银行");
		bankName_map.put("CEB","中光大银行");
		bankName_map.put("CMBC","民生银行");
		bankName_map.put("PSBC","中国邮政储蓄银行");
		bankName_map.put("SPDB","浦发银行");
		bankName_map.put("CNCB","中信银行");
		bankName_map.put("PAB","平安银行");
		bankName_map.put("HXB","华夏银行");
		bankName_map.put("CIB","兴业银行");
		bankName_map.put("BOHC","渤海银行");
		bankName_map.put("BCCB","北京银行");
		bankName_map.put("GDB","广发银行");
		bankName_map.put("BOS","上海银行");
		bankName_map.put("ZSBC","浙商银行");
		bankName_map.put("NBBC","宁波银行");
		bankName_map.put("NJBC","南京银行");
		bankName_map.put("SCB","渣打银行");
		bankName_map.put("WZCB","温州银行");
		bankName_map.put("YDXH","尧都农商银行");
		bankName_map.put("BRCB","北京农村商业银行");
		bankName_map.put("CCQTGB","重庆三峡银行");
		bankName_map.put("HCCB","杭州市商业银行");
		bankName_map.put("HNNXS","湖南农村信用合作社");
		bankName_map.put("SXJS","晋商银行");
		bankName_map.put("GZCB","广州银行股份有限公司");
		bankName_map.put("EGBANK","恒丰银行");
		bankName_map.put("CSCB","长沙银行");
		bankName_map.put("SHRCB","上海农村商业银行");
		bankName_map.put("GNXS","广州农村商业银行");
		bankName_map.put("SDE","顺德农商银行");
		bankName_map.put("CZCB","稠州银行");
		bankName_map.put("HKBCHINA","汉口银行");
		bankName_map.put("SNXS","深圳农村商业银行");
		bankName_map.put("HSBANK","徽商银行");
		bankName_map.put("CYB","集友银行");
		bankName_map.put("BEAI","稠州银行");
		bankName_map.put("CZCB","BEA东亚银行");
	}
	/**
	 * 
	 * @param request
	 * @param payRequest
	 * @param prdOrder
	 * @param payOrder
	 * @param productType 业务标示：选择扫码机构的编码 支付宝alipay ，微信wcpay
	 * @return
	 */
	public String scanPay(HttpServletRequest request,PayRequest payRequest,PayProductOrder prdOrder,PayOrder payOrder,String productType){
		try {
			
			//计算手续费开始===========
			Map<String,PayMerchant>  merMap = new PayMerchantDAO().getSettleMentMerchant(payRequest.merchantId);//取得商户和父商户
			PayMerchant mer = merMap.get(payRequest.merchantId);
			PayFeeRate feeRate=mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+","+("WEIXIN".equals(productType)?"10":"17"));//10微信扫码，17支付宝扫码 18微信WAP
			Object [] feeInfo = PayFeeRateService.getFeeByFeeRate(feeRate,payOrder.txamt);
			long fee = (Long)(feeInfo[0]);
			//计算手续费结束===========
			
			Map<String, Object> data = new HashMap<String,Object>();
			Map<String, Object> hdata = new HashMap<String,Object>();
			data.put("agtId", PayConstant.PAY_CONFIG.get("DH_AGTID"));
			data.put("merId", PayConstant.PAY_CONFIG.get("DH_MERID"));
			data.put("nonceStr", RANDOM(16, "0"));
			data.put("orderAmt", String.valueOf(payOrder.txamt));
			data.put("orderId", payOrder.payordno);
			data.put("notifyUrl", PayConstant.PAY_CONFIG.get("DH_NITIFY_URL"));
			data.put("payType", productType);
			data.put("pfee", String.valueOf(fee));//手续费。
			
			/*******结算信息 start*************************************/
			data.put("pidcard", payRequest.merchant.payCustStlInfo.custStlIdno);
			data.put("pbankcardnum", payRequest.merchant.payCustStlInfo.custStlBankAcNo);
			data.put("pcnaps", payRequest.merchant.payCustStlInfo.custStlBankNumber);
			data.put("stlType", "T0");
			data.put("productName", URLEncoder.encode(PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC")==null||PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC").length()==0?
					"_":PayConstant.PAY_CONFIG.get("COMMON_PRODUCT_DESC"), "utf-8"));
			data.put("pbankname", URLEncoder.encode(bankName_map.get(payRequest.merchant.payCustStlInfo.depositBankCode),"utf-8"));
			data.put("psubbranch", URLEncoder.encode(payRequest.merchant.payCustStlInfo.depositBankBrchName,"utf-8"));
			data.put("prealname", URLEncoder.encode(payRequest.merchant.payCustStlInfo.custBankDepositName,"utf-8"));
			/*******结算信息  end*************************************/
			
			hdata.put("sign", getSign(data,PayConstant.PAY_CONFIG.get("DH_KEY")));
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("REQ_BODY", data);
			map.put("REQ_HEAD", hdata);
			String sendData = JsonToMapUtil.toJsonString(map);
			log.info("道合扫码支付请求数据:"+sendData);
			String resData = httpPost(sendData,PayConstant.PAY_CONFIG.get("DH_PAY_URL"));
			log.info("道合扫码支付响应数据:"+resData);
			Map<String, Object> rmap = JsonToMapUtil.toMap(resData);
			Map<String, Object> _body = (Map<String, Object>) rmap.get("REP_BODY");
			Map<String, Object>  repHead= (Map<String, Object>) rmap.get("REP_HEAD");
			String rep_sign = String.valueOf(repHead.get("sign"));
			if("000000".equals(_body.get("rspcode"))){
				if(rep_sign.equals(getSign(_body,PayConstant.PAY_CONFIG.get("DH_KEY")))){
					String codeUrl= _body.get("codeUrl").toString();
					return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" +
					"<message merchantId=\""+payRequest.merchantId+"\" merchantOrderId=\""+payRequest.merchantOrderId+"\" " +
					"codeUrl=\""+codeUrl+"\" respCode=\"000\" respDesc=\"处理成功\"/>";
				}else throw new Exception("道合扫码支付验签失败");
			}else throw new Exception(URLDecoder.decode(String.valueOf(_body.get("rspmsg")),"utf-8"));
			
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
	public void channelQuer(PayOrder payOrder) throws Exception{
		try {
			Map<String, Object> data = new HashMap<String,Object>();
			Map<String, Object> hdata = new HashMap<String,Object>();
			data.put("agtId", PayConstant.PAY_CONFIG.get("DH_AGTID"));
			data.put("merId", PayConstant.PAY_CONFIG.get("DH_MERID"));
			data.put("nonceStr",  RANDOM(16, "0"));
			data.put("orderId", payOrder.payordno);
			data.put("payType", "07".equals(payOrder.paytype)?"wcpay":"alipay");
			hdata.put("sign", getSign(data,PayConstant.PAY_CONFIG.get("DH_KEY")));
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("REQ_BODY", data);
			map.put("REQ_HEAD", hdata);
			String sendData = JsonToMapUtil.toJsonString(map);
			log.info("道合扫码查询请求数据:"+sendData);
			String rdata = httpPost(sendData,PayConstant.PAY_CONFIG.get("DH_QUERY_URL"));
			log.info("道合扫码查询响应数据:"+rdata);
			Map<String, Object> rmap = JsonToMapUtil.toMap(rdata);
			Map<String, Object> _body = (Map<String, Object>) rmap.get("REP_BODY");
			Map<String, Object>  repHead= (Map<String, Object>) rmap.get("REP_HEAD");
			String rep_sign = String.valueOf(repHead.get("sign"));
			if("000000".equals(_body.get("rspcode"))){
				if(rep_sign.equals(getSign(_body,PayConstant.PAY_CONFIG.get("DH_KEY")))){
					if("01".equals(_body.get("orderState"))){
						payOrder.ordstatus="01";//支付成功
			        	new NotifyInterface().notifyMer(payOrder);//支付成功
					}else if("02".equals(_body.get("orderState"))){
						payOrder.ordstatus="02";//支付成功
			        	new NotifyInterface().notifyMer(payOrder);//支付成功
					}
				}else throw new Exception("道合扫码查询验签失败");
			}else throw new Exception(URLDecoder.decode(String.valueOf(_body.get("rspmsg")),"utf-8"));
		} catch (Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 签名类
	 * @param map
	 * @param key
	 * @return
	 */
    public static String getSign(Map<String,Object> map,String key){
        ArrayList<String> list = new ArrayList<String>();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString()+"key="+key;
        result = getMd5(result).toUpperCase();
        return result;
    }
    /**
     * MD5加密类
     * @param plainText 
     * @return 32位密文
     */
   public static String getMd5(String plainText) {
       String re_md5 = new String();
       try {
           MessageDigest md = MessageDigest.getInstance("MD5");
           md.update(plainText.getBytes());
           byte b[] = md.digest();
           int i;
           StringBuffer buf = new StringBuffer("");
           for (int offset = 0; offset < b.length; offset++) {
               i = b[offset];
               if (i < 0)
                   i += 256;
               if (i < 16)
                   buf.append("0");
               buf.append(Integer.toHexString(i));
           }
           re_md5 = buf.toString();
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
       }
       return re_md5;
   }
   /**
	 * 取随机字符串 <br>
	 * 
	 * @param arg0
	 *            构造指定长度的随机字符串
	 * @param arg1
	 *            指明是否包含字母，0-包含字母,数字和字母混合,默认是2 1-不包含数字,只有字母 2－不包含字母,只有数字
	 * @return @
	 */
	public static String RANDOM(int args1, String args2) {
		if (StringUtils.isEmpty(args2))
			throw new RuntimeException("RANDOM");
		int len = args1;
		args2 = StringUtils.trim(args2);

		if (StringUtils.equals(args2, "0")) {
			return RandomStringUtils.randomAlphanumeric(len);
		} else if (StringUtils.equals(args2, "1")) {
			return RandomStringUtils.randomAlphabetic(len);
		} else {
			return RandomStringUtils.randomNumeric(len);
		}

	}
	/**
	 * httpPost工具类
	 * @param parMap
	 * @param url
	 * @return
	 */
	public static String httpPost(String parMap,String url){	
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		InputStream input = null;//输入流
		InputStreamReader isr = null;
		BufferedReader buffer = null;
		StringBuffer sb = null;
		String line = null;
		try {
			/*post向服务器请求数据*/
			StringEntity se = new StringEntity(parMap);
			httpPost.addHeader("Content-type","application/json");
			httpPost.setHeader("Accept", "text/json");
			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			// 若状态值为200，则ok
			if (statusCode == HttpStatus.SC_OK) {
				//从服务器获得输入流
				input = response.getEntity().getContent();
				isr = new InputStreamReader(input);
				buffer = new BufferedReader(isr,10*1024);
				sb = new StringBuffer();
				while ((line = buffer.readLine()) != null) {
					sb.append(line);
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(buffer != null) {
					buffer.close();
					buffer = null;
				}
				if(isr != null) {
					isr.close();
					isr = null; 
				}
				if(input != null) {
					input.close();
					input = null;
				}
			} catch (Exception e) {
				}
		}
		return sb.toString();
	}
	
}
