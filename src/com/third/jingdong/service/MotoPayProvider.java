package com.third.jingdong.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import util.JWebConstant;

import com.PayConstant;

public class MotoPayProvider {
	
	private static final String url = PayConstant.PAY_CONFIG.get("JD_QUICKPAY_URL");
	
	public String process(String charset, String req) {
		String resp = null;
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		InputStream in = null;
		try {
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost(url);

			if (url.indexOf("https") != -1) {
				KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
				in = new FileInputStream(new File(JWebConstant.APP_PATH+File.separator+"config"+File.separator+"/quick.keystore"));
				keyStore.load(in, "chinabank".toCharArray());
				SSLSocketFactory sslSocketFactory = new SSLSocketFactory(keyStore);
				Scheme scheme = new Scheme("https", 443, sslSocketFactory);
				httpClient.getConnectionManager().getSchemeRegistry().register(scheme);
			}
			HttpParams httpParams = httpClient.getParams();
			httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,1000 * 60);
			httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 1000 * 60);
			List<NameValuePair> reqPair = new ArrayList<NameValuePair>();
			reqPair.add(new BasicNameValuePair("charset", charset));
			reqPair.add(new BasicNameValuePair("req", req));
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(reqPair,charset);
			httpPost.setEntity(urlEncodedFormEntity);
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				InputStream is = responseEntity.getContent();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int ch = 0;
				while ((ch = is.read(buffer)) != -1) {
					baos.write(buffer, 0, ch);
				}
				byte bytes[] = baos.toByteArray();
				resp = new String(bytes, charset);
			}

			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new Exception(response.getStatusLine().toString() + "|"+ resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(in!=null){
					in.close();
				}
				httpPost.releaseConnection();
				httpClient.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return resp;
	}
}
