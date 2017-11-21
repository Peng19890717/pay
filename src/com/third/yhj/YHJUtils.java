package com.third.yhj;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.apache.commons.lang.IllegalClassException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class YHJUtils {
	private static final Log log = LogFactory.getLog(YHJUtils.class);
	/**
	 * 获取响应数据
	 * @param requestData
	 * @param reqUrl
	 * @param resultListener
	 * @return
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	public static String getResData(JSONObject requestData, String reqUrl) throws UnsupportedEncodingException, Exception{
		log.info("请求的数据requestData："+requestData.toJSONString());
		String responseStr = HttpClientUtils.post(reqUrl, requestData.toJSONString());
		return responseStr;
	}
	/**
	 * 签名
	 * @param reqSource
	 * @return 
	 */
	public static String sign(JSONObject reqSource, String key){
		StringBuilder hmacSource = new StringBuilder();
		hmacSource.append(reqSource.getString("merchantId")==null?"":reqSource.getString("merchantId"))
			.append(reqSource.getString("orderAmount")==null?"":reqSource.getString("orderAmount"))
			.append(reqSource.getString("orderCurrency")==null?"":reqSource.getString("orderCurrency"))
			.append(reqSource.getString("requestId")==null?"":reqSource.getString("requestId"))
			.append(reqSource.getString("notifyUrl")==null?"":reqSource.getString("notifyUrl"))
			.append(reqSource.getString("callbackUrl")==null?"":reqSource.getString("callbackUrl"))
			.append(reqSource.getString("remark")==null?"":reqSource.getString("remark"))
			.append(reqSource.getString("paymentModeCode")==null?"":reqSource.getString("paymentModeCode"));
		if(reqSource.getString("productDetails") != null){
			String productDetails = reqSource.getString("productDetails");
			JSONArray prdDetails = JSONArray.parseArray(productDetails);
			for(int i=0;i<prdDetails.size();i++){
				JSONObject productDetail = prdDetails.getJSONObject(i);
				hmacSource.append(productDetail.getString("name")==null?"":productDetail.getString("name"))
				.append(productDetail.getString("quantity")==null?"":productDetail.getString("quantity"))
				.append(productDetail.getString("amount")==null?"":productDetail.getString("amount"))
				.append(productDetail.getString("receiver")==null?"":productDetail.getString("receiver"))
				.append(productDetail.getString("description")==null?"":productDetail.getString("description"));
			}
		}
		if (reqSource.getString("payer") != null) {
			JSONObject payer = JSONObject.parseObject(reqSource.getString("payer"));
			hmacSource.append(payer.getString("name") == null?"":payer.getString("name"))
				.append(payer.getString("phoneNum") == null?"":payer.getString("phoneNum"))
				.append(payer.getString("idNum") == null ?"":payer.getString("idNum"))
		        .append(payer.getString("bankCardNum") == null ?"":payer.getString("bankCardNum"));
		    }
	    hmacSource.append(StringUtils.defaultString(reqSource.getString("clientIp"), ""));
	    return SignUtils.signMd5(hmacSource.toString(), key);
	}
	/**
	 * 验签（下单结果验签）
	 * @param fields :merchantId,requestId,status,scanCode,appParams,redirectUrl
	 * @param responseData
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyHmac(String[] fields, JSONObject responseData) throws Exception{
		StringBuilder hmacSource = new StringBuilder();
	    for (String field : fields) {
    		if(responseData.getString(field) != null){
    			hmacSource.append(responseData.getString(field)==null?"":responseData.getString(field));
    		}
	    }
	    String source = hmacSource.toString();
	    log.info("source==========="+source);
		String sourceHmac = SignUtils.signMd5(source, PayConstant.PAY_CONFIG.get(PayConstant.PAY_CONFIG.get("YHJ_MERCHANT_ID")));
		log.info("sourceHmac==========="+sourceHmac);
		if (!sourceHmac.equals(responseData.getString("hmac"))){
			return false;
		}
		return true;
	}
	/**
	 * 验签（查单结果验签）
	 * @param fields
	 * @param responseData
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyHmac(Object[] fields, JSONObject responseData) throws Exception{
		String source = getHmacSource(fields, responseData);
		String hmac = responseData.getString("hmac");
	    String sourceHmac = SignUtils.signMd5(source, PayConstant.PAY_CONFIG.get(PayConstant.PAY_CONFIG.get("YHJ_MERCHANT_ID")));
	    if (!sourceHmac.equals(hmac)){
	    	return false;
	    }
		return true;
	} 
	/**
	 * 获取查单结果验签所需的参数
	 * @param fields
	 * @param responseData
	 * @return
	 * @throws Exception
	 */
	public static String getHmacSource(Object[] fields, JSONObject responseData) throws Exception{
		StringBuilder hmacSource = new StringBuilder();
		for (Object field : fields){
			if ((field instanceof String)) {
				String fieldStr = (String) field;
				hmacSource.append(responseData.getString(fieldStr)==null?"":responseData.getString(fieldStr));
		    }else{
		        HmacArray ha;
		        @SuppressWarnings("rawtypes")
				Iterator i$;
		        if ((field instanceof HmacArray)) {
		          ha = (HmacArray)field;
		          JSONArray jsons = responseData.getJSONArray(ha.getKey());
		          if (jsons != null) {
		            for (i$ = jsons.iterator(); i$.hasNext(); ) { 
		            	Object o = i$.next();
		                hmacSource.append(getHmacSource(ha.getItems(), (JSONObject)o));
		            }
		          }
		        }else{
		          throw new IllegalClassException("field class:[" + field.getClass() + "]");
		        }
		    }
		}
		log.info("hmacSource============="+hmacSource);
		return hmacSource.toString();
	}
}
