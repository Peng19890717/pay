package com.third.ttf;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

public class HttpClientUtil {
	/**
	 * HttpClientPost远程接口调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> params) {
		try {
			System.out.println("远程接口调用地址URL--->" + url);
			System.out.println("远程接口调用参数params--->" + params);
			HttpClient client = new HttpClient();
			if (url.startsWith("https")) {
				System.out.println("忽略https设置......");
				Protocol myhttps = new Protocol("https",
						new MySecureProtocolSocketFactory(), 443);
				Protocol.registerProtocol("https", myhttps);
				client.getParams().setBooleanParameter(
						"http.protocol.expect-continue", false);
			}
			client.getParams().setContentCharset("UTF-8");
			client.getParams().setHttpElementCharset("UTF-8");
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					15000);
			client.getHttpConnectionManager().getParams().setSoTimeout(15000);
			HttpMethod method = getPostMethod(url, params);
			client.executeMethod(method);
			String res = method.getResponseBodyAsString();
			// log.info("远程接口调用返回res--->" + res);
			method.releaseConnection();
			return res;
		} catch (Exception e) {
			System.out.println("远程接口调用异常-->");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * HttpClientPost远程接口调用
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static byte[] postReturnByteArray(String url,
			Map<String, String> params) {
		try {
			System.out.println("远程接口调用地址URL--->" + url);
			System.out.println("远程接口调用参数params--->" + params);
			HttpClient client = new HttpClient();
			if (url.startsWith("https")) {
				System.out.println("忽略https设置......");
				Protocol myhttps = new Protocol("https",
						new MySecureProtocolSocketFactory(), 443);
				Protocol.registerProtocol("https", myhttps);
				client.getParams().setBooleanParameter(
						"http.protocol.expect-continue", false);
			}
			client.getParams().setContentCharset("UTF-8");
			client.getParams().setHttpElementCharset("UTF-8");
			client.getHttpConnectionManager().getParams().setConnectionTimeout(
					5000);
			client.getHttpConnectionManager().getParams().setSoTimeout(5000);
			HttpMethod method = getPostMethod(url, params);
			client.executeMethod(method);
			byte[] resByte = method.getResponseBody();
			// log.info("远程接口调用返回res--->" + res);
			method.releaseConnection();
			return resByte;
		} catch (Exception e) {
			System.out.println("远程接口调用异常-->");
			e.printStackTrace();
		}
		return null;
	}

	private static HttpMethod getPostMethod(String url,
			Map<String, String> params) {
		PostMethod post = new PostMethod(url);
		try {
			Iterator iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				if (key == null || key.toString().trim().length() == 0
						|| val == null || val.toString().trim().length() == 0)
					continue;
				String paramName = key.toString();
				String paramValue = val.toString();
				post.addParameter(paramName, paramValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return post;
	}
}
