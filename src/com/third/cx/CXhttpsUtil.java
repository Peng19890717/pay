package com.third.cx;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;


public class CXhttpsUtil {
	//测试jdk1.6访问jdk1.8https服务端hankshake failure问题
	public static void main(String[] args) {
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("appId", "88888");
			params.put("charset", "UTF-8");
			params.put("payType", "01");
			params.put("clientIp", "192.168.102.31");
			params.put("serviceName", "queryResult");
			params.put("version", "V1");
			params.put("signMethod", "RSA");
			params.put("merOrderId", "WLTCZ161111113352132505");
			params.put("sign", "Rrcr8yvEyyocOuW9TtOf/ckcbqMN9K8t0kah3C/Y2HQeCJm5ytS8y6+/TmIAcWMhohZ40HnlZYkVZIHG4SPFTKIjWLVU20s0FwIpIaMzm1ERjdozkuVe7swL6U0A2WjnxbumCAoPZLCBWyG55MMjJ+BxNl7v0tDCxU7LkXK4p70=");
			
			String urlStr ="https://gateway.xywallet.com/xywallet-api";
			
			
			new CXhttpsUtil().doPost(urlStr,params,"UTF-8");
//			doPost2();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String  doPost(String urlStr,Map<String,String> params ,String charset) throws Exception{  
		String returnStr="";
		try {
			URL url = new URL(urlStr);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Host", url.getHost());
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(true);
			conn.setRequestProperty("contentType", charset);
			conn.setSSLSocketFactory(new TLSSocketConnectionFactory());	
			conn.connect();

			//POST请求
			DataOutputStream out = new DataOutputStream(
			        conn.getOutputStream());
			
			String jsonString = buildQuery(params, charset);
			BufferedReader reader = null;
			try {
			    out.writeBytes(jsonString);
			    out.flush();
			    //读取响应
			    reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));
			    String lines;
			    StringBuffer sb = new StringBuffer("");
			    while ((lines = reader.readLine()) != null) {
			        lines = new String(lines.getBytes(), charset);
			        sb.append(lines);
			    }
			    System.out.println(sb.toString());
			    returnStr+=sb.toString();

			} finally {
				System.out.println(conn.getResponseCode());
			    if (out != null) {
			        out.close();
			    }
			    if (reader != null) {
			        reader.close();
			    }
			    if (conn != null) {
			        conn.disconnect();
			    }
			}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return returnStr;
          
    }
	
	public static String buildQuery(Map<String, String> params, String charset) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;

		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// 忽略参数名或参数值为空的参数
			if (isNotEmpty(name) && isNotEmpty(value)) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}
				query.append(name).append("=").append(URLEncoder.encode(value, charset));
			}
		}

		return query.toString();
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

}
