package com.third.yaku.utils.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientUtils {

	public static String submitGet(String url) {
		System.out.println("--------------------------------------");
		String result = "";
		
		// 创建默认的HttpClient实例.
		CloseableHttpClient httpClient = HttpClients.createDefault();

		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(120000).setConnectTimeout(120000).build();
		
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int status = response.getStatusLine().getStatusCode();
			ProtocolVersion protocolVersion = response.getStatusLine().getProtocolVersion();
			System.out.println("Status Code: " + status);
			System.out.println("Protocol Version: " + protocolVersion.toString());
			
			// 状态码返回正常再处理返回内容
			if (status == 200) {
				HttpEntity entity = response.getEntity();
//				InputStream inputStream = null;
				if (entity != null) {
					InputStream inputStream = entity.getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					
					StringBuffer buffer = new StringBuffer();
					String line = "";
                    while((line = reader.readLine()) != null){
                    	buffer.append(line).append("\r\n");
                    }
                    
                    result = buffer.toString();
//					result = EntityUtils.toString(entity, "UTF-8");
					
//					System.out.println("--------------------------------------");
//					System.out.println("Response content: " /*+ result*/);
//					System.out.println("--------------------------------------");
				} else {
					System.out.println("Entity is null!");
				}
			} else {
				System.out.println("状态码不正确！");
			}
			
			// release
			response.close();
			httpClient.close();
		} catch (ClientProtocolException e) {
			System.out.println("发生异常！");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("发生异常！");
			e.printStackTrace();
		} 
		System.out.println("--------------------------------------");
		return result;
	}

	public static String submitPost(String url, Map<String, String> param) {
		String result = "";
		
		// 创建默认的httpClient实例.
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// 创建httppost
		HttpPost httpPost = new HttpPost(url);
		
		// 创建参数队列
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		for(Map.Entry<String, String> entry : param.entrySet()){    
			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		
		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
			httpPost.setEntity(uefEntity);
			System.out.println("executing request " + httpPost.getURI());
			CloseableHttpResponse response = httpclient.execute(httpPost);

			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inputStream = entity.getContent();
					StringBuffer buffer = new StringBuffer(); 
                    byte [] b = new byte [1024];
                    int len=-1;
                    while((len = inputStream.read(b))!=-1) buffer.append(new String(b,0,len,"utf-8"));
                    result = buffer.toString();
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		String url = "http://93.174.95.27/book/index.php?md5=C878554F674C0723E06E3EDBE6CABCC2";
		submitGet(url);
	}
}
