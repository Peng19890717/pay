package com.third.swt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpUtil {

	public static String sendHttp(String url, String message) throws Exception {
		Map rtnMap = null;
		HttpUtil httpUtil = new HttpUtil();
		// 加载路径
		URL connurl = new URL(url);

		// 创建连接
		HttpURLConnection conn = httpUtil.getHttpsURLConnection(connurl);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.connect();

		OutputStream os = null;
		String ReceiveMsg = "";
		try {
			os = conn.getOutputStream();
			String messaget = message.replace("+", "%2B");
			message = messaget;
			byte[] d = message.getBytes("utf-8");
			os.write(d);
			os.flush();
			int retCod = conn.getResponseCode();
			if (retCod == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
				ReceiveMsg = br.readLine();
				rtnMap = XMLConvertor.xml2Map(ReceiveMsg);
			} else {
				String errmsg = conn.getResponseMessage();
				throw new IOException("HTTP返回失败:" + Integer.valueOf(retCod)
						+ errmsg);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				os.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return ReceiveMsg;
	}

	public HttpURLConnection getHttpsURLConnection(URL url) {

		try {
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			if ("https".equals(url.getProtocol())) // 如果是https协议
			{
				HttpsURLConnection httpsConn = (HttpsURLConnection) httpConnection;
				TrustManager[] managers = { new MyX509TrustManager() };// 证书过滤
				SSLContext sslContext;
				sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, managers, new SecureRandom());
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				httpsConn.setSSLSocketFactory(ssf);
				httpsConn.setHostnameVerifier(new MyHostnameVerifier());// 主机名过滤
				System.setProperty("https.protocols", "TLSv1");
				return httpsConn;

			}
			return httpConnection;

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;

	}
	public static HttpURLConnection createConnection(URL url)
			throws Exception {

		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		httpURLConnection.setConnectTimeout(10000);// 连接超时时间
		httpURLConnection.setReadTimeout(30000);// 读取结果超时时间
		httpURLConnection.setDoInput(true); // 可读
		httpURLConnection.setDoOutput(true); // 可写
		httpURLConnection.setUseCaches(false);// 取消缓存
		httpURLConnection.setRequestProperty("Encoding", "UTF-8");
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded; charset=utf-8");

		if ("https".equalsIgnoreCase(url.getProtocol())) {
			HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
			TrustManager[] managers = { new MyX509TrustManager() };// 证书过滤
			SSLContext sslContext;
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, managers, new SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			husn.setSSLSocketFactory(ssf);
			husn.setHostnameVerifier(new MyHostnameVerifier());// 主机名过滤
			System.setProperty("https.protocols", "TLSv1");
			return husn;
		}

		return httpURLConnection;
	}
}
