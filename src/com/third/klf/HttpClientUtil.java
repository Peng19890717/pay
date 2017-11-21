package com.third.klf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.params.CoreConnectionPNames;

@SuppressWarnings("deprecation")
public class HttpClientUtil {

	public static String send(String webUrl, String xmlFile, String charset) throws UnsupportedEncodingException {
		StringBuffer sBuffer = new StringBuffer();
		// 构造HttpClient的实例
		HttpClient httpClient = new HttpClient();
		// 创建POS方法的实例
		PostMethod postMethod = new PostMethod(webUrl);
		postMethod.setRequestHeader("Content-Type","application/json");  
		postMethod.setRequestHeader("charset",charset);  
		postMethod.setRequestEntity(new StringRequestEntity(xmlFile, "application/json", charset));
		postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		postMethod.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000*100);
		postMethod.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000*100);
		try {
			// 执行getMethod
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + postMethod.getStatusLine());
			}
			// 读取内容
			byte[] responseBody = postMethod.getResponseBody();
			// 处理内容
			sBuffer.append(new String(responseBody, "UTF-8"));
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			postMethod.releaseConnection();
		}
		return sBuffer.toString();
	}
}
