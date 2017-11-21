package com.third.cx;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.PayConstant;



public class GatewayUtil {
	/**
	 * 对请求进行签名。
	 */
	public static String signRequest(Map<String, String> params, String privateKey) throws IOException {
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder();
		boolean hasParam = false;
		for (String key : keys) {
			String value = params.get(key);
			// 除sign、signType字段
			if (isNotEmpty(key) && isNotEmpty(value) && !key.equals("sign") && !key.equals("signMethod")) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}
				query.append(key).append("=").append(value);
			}
		}

		// 生成签名串
		String signstr = RSAUtil.sign(query.toString(), privateKey, "utf-8");

		return signstr;
	}
	public static String signRequest(Map<String, String> params, String privateKey, String signType) throws IOException {
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder();
		boolean hasParam = false;
		for (String key : keys) {
			String value = params.get(key);
			// 除sign、signType字段
			if (isNotEmpty(key) && isNotEmpty(value) && !key.equals("sign") && !key.equals("signMethod")) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}
				query.append(key).append("=").append(value);
			}
		}
		System.out.println("签名原串:"+query.toString());
		// 生成签名串
		String signstr = RSAUtil.sign(query.toString(), privateKey, "utf-8");
		System.out.println("签名数据:"+signstr);
//		System.out.println("签名原串：" + query.toString());
//		System.out.println("签名串：" + signstr);

		return signstr;
	}
	public static String httpPostByDefaultTime(URI url,Map<String,String> maps,String charset) throws IOException{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for(String key:maps.keySet()){
			params.add(new BasicNameValuePair(key,maps.get(key)));
		}
		return httpPost(url,params,30000,30000,charset);
	}
	/**
	 * 对响应验签。
	 */
	public static boolean veryfyResponse(Map<String, String> params) throws Exception {
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		StringBuffer tmp = new StringBuffer("");
		for(int i=0; i<keys.length; i++){
			if(keys[i].equals("sign") || keys[i].equals("signMethod"))continue;
			tmp.append(keys[i]+"="+params.get(keys[i])).append("&");
		}
		String signSrc = tmp.toString();
		if(signSrc.length()==0)throw new Exception("请求错误");
		signSrc = signSrc.substring(0,signSrc.length()-1);//去掉最后一个&
		System.out.println(signSrc);
		System.out.println(params.get("sign"));
		return RSAUtil.verify(signSrc, params.get("sign"),PayConstant.PAY_CONFIG.get("CXPAY_WEI_XIN_PUB_KEY"));
	}
	public static boolean veryfyResponse(Map<String, String> params,String pub_key) throws Exception {
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		StringBuffer tmp = new StringBuffer("");
		for(int i=0; i<keys.length; i++){
			if(keys[i].equals("sign") || keys[i].equals("signMethod"))continue;
			if(!params.get(keys[i]).equals("")){
				tmp.append(keys[i]+"="+params.get(keys[i])).append("&");
			}
		}
		String signSrc = tmp.toString();
		if(signSrc.length()==0)throw new Exception("请求错误");
		signSrc = signSrc.substring(0,signSrc.length()-1);//去掉最后一个&
//		System.out.println(signSrc);
//		System.out.println(params.get("sign"));
		return RSAUtil.verify(signSrc, params.get("sign"),pub_key);
	}
	public static void main(String[]args) throws Exception{
		//{"endTime":"2016-10-28 17:47:14","mchTradeNo":"q0UWgZE2Q3rKTPe","orderNo":"20161028815530509457610404048574","payType":"12","sign":"P5fml+B7Wx8izzBEvlls4DyUeLm1hsPyQFZPX0b42GtbImla1OnUut+uSmEHZWEvJlkwo2laQCij39WF8p96yLqgSeWO6ZM1ShZvtLnzL7NFLxI1TJfBH81GxwvYxQOjETjSEnMtlqaduXjf6W6BELX/bBtXtqhDWPhoYUtfKG8=","subject":"小米4","tradeAmt":"0.01","tradeStatus":"02"}
//		JSONObject jsonObject = JSON.parseObject("{\"endTime\":\"2016-10-28 17:47:14\",\"mchTradeNo\":\"q0UWgZE2Q3rKTPe\",\"orderNo\":\"20161028815530509457610404048574\",\"payType\":\"12\",\"sign\":\"P5fml+B7Wx8izzBEvlls4DyUeLm1hsPyQFZPX0b42GtbImla1OnUut+uSmEHZWEvJlkwo2laQCij39WF8p96yLqgSeWO6ZM1ShZvtLnzL7NFLxI1TJfBH81GxwvYxQOjETjSEnMtlqaduXjf6W6BELX/bBtXtqhDWPhoYUtfKG8=\",\"subject\":\"小米4\",\"tradeAmt\":\"0.01\",\"tradeStatus\":\"02\"}");
//		Map <String,String>params = new HashMap <String,String>();
//		params.put("orderNo",jsonObject.getString("orderNo"));
//		params.put("mchTradeNo",jsonObject.getString("mchTradeNo"));
//		params.put("tradeStatus",jsonObject.getString("tradeStatus"));
//		params.put("payType",jsonObject.getString("payType"));
//		params.put("tradeAmt",jsonObject.getString("tradeAmt"));
//		params.put("endTime",jsonObject.getString("endTime"));
//		params.put("subject",jsonObject.getString("subject"));
//		params.put("sign",jsonObject.getString("sign"));
//		System.out.println(veryfyResponse(params));
	}
	public static String httpPostByDefaultTime(URI url,Map<String,String> maps) throws IOException{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for(String key:maps.keySet()){
			params.add(new BasicNameValuePair(key,maps.get(key)));
		}
		return httpPost(url,params,30000,30000,"UTF-8");
	}
	
	public static String httpPostByMap(URI url,Map<String,String> maps,int socketTimeout,int connectTimeout,String charset) throws IOException{
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for(String key:maps.keySet()){
			params.add(new BasicNameValuePair(key,maps.get(key)));
		}
		return httpPost(url,params,socketTimeout,connectTimeout,charset);
	}
	
	public static String httpPost(URI uri,List<NameValuePair> param,int socketTimeout,int connectTimeout,String charset) throws IOException{
		String result="";
		CloseableHttpClient  client  =HttpClientBuilder.create().build();
		try{
			HttpPost httpost = new HttpPost(uri);
			httpost.setHeader("Content-Type","application/x-www-form-urlencoded;charset="+charset);
			HttpEntity entity = null;
			if(param==null){
				entity = new StringEntity("");
			}else{
				entity = new UrlEncodedFormEntity(param,"UTF-8");
			}
			httpost.setEntity(entity);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();  
			httpost.setConfig(requestConfig);
			HttpResponse response  = client.execute(httpost);
			HttpEntity responseEntity = response.getEntity(); 
			result = EntityUtils.toString(responseEntity,"utf-8");
			client.close();
		} finally{
			if(client!=null){
				client.close();
			}
		}
		return result;
	}

	/**
	 * 检查是否不为空
	 */
	public static boolean isNotEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return true;
			}
		}
		return false;
	}

	public static void paramsTransform(Map<String, String> params, String myParam, String pcParam) {
		if (params.get(myParam) != null) {
			params.put(pcParam, params.get(myParam).toString());
		}
		params.remove(myParam);
	}
}
