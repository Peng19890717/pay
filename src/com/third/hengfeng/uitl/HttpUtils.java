package com.third.hengfeng.uitl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.third.hengfeng.vo.Pagination;

/**
 * 
 * @ClassName: HttpUtils
 * @Description: HTTP工具类
 * @author zheng.shk
 * @date 2016年9月9日 下午4:37:15
 */
public class HttpUtils {


	/**
	 * 
	 * @param url
	 *            路径
	 * @param reqMap
	 *            参数
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String,Object> httpPost(String url, Map<String, Object> reqMap) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(JSON.toJSONString(reqMap));
		return httpPost(url, jsonParam);
	}


	/**
	 * post请求
	 * 
	 * @param url
	 *            url地址
	 * @param jsonParam
	 *            参数
	 * @return HashMap<String,Object>
	 * @throws Exception
	 */
	public static HashMap<String,Object> httpPost(String url, JSONObject jsonParam) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpRequst = new HttpPost(url);
		if (null != jsonParam) {
			StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8"); // 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpRequst.setEntity(entity);
			System.out.println("------------------------request url:"+url);
			System.out.println("------------------------send request data:"+jsonParam.toJSONString());

		}
		CloseableHttpResponse httpResponse = httpClient.execute(httpRequst);
		url = URLDecoder.decode(url, "UTF-8");
		HashMap<String,Object> responseMap = null;
		// 请求发送成功，并得到响应
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			responseMap = new HashMap<String,Object>();
			String responseBody = EntityUtils.toString(httpResponse.getEntity()); // 读取服务器返回过来的报文体
			responseMap.put("content", responseBody);
			System.out.println("------------------------response data :" + responseBody);
		}else{
			throw new Exception("请求失败或者服务器错误");
		}
		return responseMap;
	}
	
	

	
	/**
	 * post请求 并进行RSA加签，加签sign放到header中
	 * @param url  url地址
	 * @param privateKey  私钥
	 * @param reqMap  请求map
	 * @return Map
	 * @throws Exception
	 */
	public static HashMap<String,Object> httpPostAndSign(String url, String privateKey, Map<String, Object> reqMap) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(JSON.toJSONString(reqMap));
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpRequst = new HttpPost(url);
		if (null != jsonParam) {
			StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8"); // 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpRequst.setEntity(entity);
			String sign = RSA.sign(jsonParam.toString(), privateKey, "UTF-8");
			httpRequst.setHeader("sign", sign);
			System.out.println("------------------------request url:"+url);
			System.out.println("------------------------request header sign:"+sign);
			System.out.println("------------------------send request data:"+jsonParam.toJSONString());

		}
		CloseableHttpResponse httpResponse = httpClient.execute(httpRequst);
		url = URLDecoder.decode(url, "UTF-8");
		HashMap<String,Object> responseMap = null;
		// 请求发送成功，并得到响应
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			responseMap = new HashMap<String,Object>();
			String responseBody = EntityUtils.toString(httpResponse.getEntity()); // 读取服务器返回过来的报文体
			responseMap.put("content", responseBody);
			Header signHeader = httpResponse.getFirstHeader("sign");
			if(null!=signHeader){
				responseMap.put("sign", signHeader.getValue());
				System.out.println("------------------------response header sign :" + signHeader.getValue());
			}
			System.out.println("------------------------response data :" + responseBody);
		}else{
			throw new Exception("请求失败或者服务器错误");
		}
		return responseMap;
	}	

	/**
	 * 发送get请求
	 * 
	 * @param url
	 *            路径
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static JSONObject httpGet(String url) throws Exception {
		JSONObject jsonResult = null; // get请求返回结果
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(url);// 发送get请求
		CloseableHttpResponse response = client.execute(request);
		// 请求发送成功，并得到响应
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String strResult = EntityUtils.toString(response.getEntity()); // 读取服务器返回过来的json字符串数据
			jsonResult = JSONObject.parseObject(strResult); // 把json字符串转换成json对象
			url = URLDecoder.decode(url, "UTF-8");
		} else {
			System.out.println("get请求提交失败:" + url);
		}
		return jsonResult;
	}

	/**
	 * 
	 * @Title: getReqParam
	 * @Description: 获取http+json的请求参数
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String getReqJsonParam(HttpServletRequest request) throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @Title: getReqParam
	 * @Description: 获取http+json的请求参数，并返回对应的object
	 * @param request
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public static <T> T getReqParam(HttpServletRequest request, Class<T> clazz) throws Exception {
		String reqJsonParam = getReqJsonParam(request);
		try {
			T t = JSON.parseObject(reqJsonParam, clazz);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			String eMsg = e.getMessage();
			throw new Exception("json invalid,message:" + eMsg);
		}
	}

	
	

	public static void main(String[] args) throws Exception {
		String url = "http://192.168.98.85:33080/banking-fsbp-web/findMerCustInfoProcess.api";

		// ----1、组装请求参数map
		Map<String, Object> reqMap = new HashMap<String, Object>();
		Pagination pagination = new Pagination();
		reqMap.put("pagination", pagination);

		// ----2、发送http请求，并拿到结果responseMap
		HashMap<String,Object> responseMap = HttpUtils.httpPost(url, reqMap);
		JSONObject jsonObject = null;;
		if(null!=responseMap){
			String content = (String) responseMap.get("content");
			jsonObject = JSONObject.parseObject(content);
			// ---3、从jsonObject中获取对应的数据
			// 获取当个对象并进行映射
			pagination = jsonObject.getObject("pagination", Pagination.class);
		}

	}
	
	
}
