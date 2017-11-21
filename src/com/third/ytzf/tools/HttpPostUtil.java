package com.third.ytzf.tools;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pay.merchantinterface.service.YTZFPayService;

public class HttpPostUtil {
	private static final Log log = LogFactory.getLog(HttpPostUtil.class);
	
	/** Ӧ������ */
	private String resContent;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	/**
	 * ����post����
	 * @param strUrl
	 * @return
	 */
	public boolean postRequest(String strUrl){
		//����url
		String url = this.getURL(strUrl);  
		//�������
		String queryString = this.getQueryString(strUrl); 
		try{
		if (url.indexOf("http://") != -1) {
			this.resContent = this.postHttp(url, queryString);
			return true;
		 } else if (url.indexOf("https://") != -1) {
			this.resContent = this.postHttps(url, queryString);
		    return true;
		  }
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String postHttp(String urlStr, String params) { 
		PrintWriter out = null; 
		BufferedReader in = null; 
		StringBuffer result = new StringBuffer();
		try { 
			URL realUrl = new URL(urlStr); 
			URLConnection conn = realUrl.openConnection(); 
			conn.setRequestProperty("accept", "*/*"); 
			conn.setRequestProperty("connection", "Keep-Alive"); 
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"); 
			conn.setDoOutput(true); 
			conn.setDoInput(true); 
			out = new PrintWriter(conn.getOutputStream()); 
			out.print(params); 
			out.flush(); 
			in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
			String line = ""; 
			while ((line = in.readLine()) != null) { 
				result.append(line); 
			}
		} catch (Exception e) { 
			e.printStackTrace();
		} finally { 
			try { 
				if (out != null) { 
					out.close(); 
					out = null; 
				} 
				if (in != null) { 
					in.close(); 
					in = null; 
				} 
			} catch (Exception ex) { 
				ex.printStackTrace();
			} 
		}
		return result.toString(); 
	}

	/**
	 * ��SSL��ʽ�ύ����
	 * @param urlString  �ύ��url��ַ
	 * @param data		   �ύ���������
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public String postHttps(String urlString, String data) throws NoSuchAlgorithmException, KeyManagementException, IOException
			{
		System.out.println("https��ʽ�ύ��URL:"+urlString);
		System.out.println("�ύ�Ĳ���"+data);
		OutputStreamWriter os = null;

			URL url = new URL(urlString);
			URLConnection con = url.openConnection();
			System.out.println("�������ͣ�"+ con.getClass());
			BufferedReader in = null; 
			StringBuffer result = new StringBuffer();
	
			if (con instanceof javax.net.ssl.HttpsURLConnection) {
				System.out.println("*** openConnection returns an instanceof javax.net.ssl.HttpsURLConnection");
				
				//��������֤�� ��ʼ
				javax.net.ssl.SSLContext sc = null;
				javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[] { new javax.net.ssl.X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				}
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs, String authType) {
				} 
				} };

				// Install the all-trusting trust manager
				sc = javax.net.ssl.SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				//��������֤�� ����
					
				javax.net.ssl.HostnameVerifier hv = new javax.net.ssl.HostnameVerifier() {
					public boolean verify(String urlHostName, javax.net.ssl.SSLSession session) {
						return urlHostName.equals(session.getPeerHost());
					}
				};
				javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(hv);
				javax.net.ssl.HttpsURLConnection conn = null;
				conn = (javax.net.ssl.HttpsURLConnection) con;
				// conn.setRequestProperty("Content-Type", "text/xml");
				conn.setDoOutput(true);
				conn.setFollowRedirects(true);
				// conn.setReadTimeout(30000);
				os = new OutputStreamWriter(conn.getOutputStream());
				os.write(data);
				os.flush();
				if (conn.getResponseCode() == 302 || conn.getResponseCode() == 200) {
					System.out.println("https�����ͳɹ���");
					System.out.println("�����룺" + conn.getResponseCode());
				}
				// ����BufferedReader����������ȡURL����Ӧ 
				in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				String line = ""; 
				while ((line = in.readLine()) != null) { 
					result.append(line); 
				}
			}
			else if (con instanceof com.sun.net.ssl.HttpsURLConnection) {
				System.out.println("***openConnection returns an instanceof com.sun.net.ssl.HttpsURLConnection");
				//��������֤�� ��ʼ
				com.sun.net.ssl.SSLContext sc = null;
				com.sun.net.ssl.TrustManager[] trustAllCerts = new com.sun.net.ssl.TrustManager[] { new com.sun.net.ssl.X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
//				@Override
				public boolean isClientTrusted(X509Certificate[] arg0) {
					// TODO Auto-generated method stub
					return true;
				}
//				@Override
				public boolean isServerTrusted(X509Certificate[] arg0) {
					// TODO Auto-generated method stub
					return true;
				}
				} };
				// Install the all-trusting trust manager
				sc = com.sun.net.ssl.SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				com.sun.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				//��������֤�� ����
				
				com.sun.net.ssl.HttpsURLConnection conn = null;
				conn = (com.sun.net.ssl.HttpsURLConnection) con;
				com.sun.net.ssl.HostnameVerifier hv = new com.sun.net.ssl.HostnameVerifier() {
//					@Override
					public boolean verify(String arg0, String arg1) {
						return true;
					}
				};
				com.sun.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(hv);
				conn.setAllowUserInteraction(true);
				conn.setDoOutput(true);
				
			    os = new OutputStreamWriter(conn.getOutputStream());
				os.write(data);
				os.flush();

				if (conn.getResponseCode() == 302 || conn.getResponseCode() == 200) {
					System.out.println("https�����ͳɹ���");
					System.out.println("�����룺" + conn.getResponseCode());
				}
				// ����BufferedReader����������ȡURL����Ӧ 
				in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
				String line = ""; 
				while ((line = in.readLine()) != null) { 
					result.append(line);
				}
			}
			System.out.println("Զ������ص��ı����"+result); 
			
		return result.toString();
	}
	
	/**
	 * ��ȡ�����ѯ����url
	 * @param strUrl
	 * @return String
	 */
	private String getURL(String strUrl) {

		if(null != strUrl) {
			int indexOf = strUrl.indexOf("?");
			if(-1 != indexOf) {
				return strUrl.substring(0, indexOf);
			} 
			
			return strUrl;
		}
		
		return strUrl;
		
	}
	
	/**
	 * ��ȡ��ѯ��
	 * @param strUrl
	 * @return String
	 */
	private String getQueryString(String strUrl) {
		
		if(null != strUrl) {
			int indexOf = strUrl.indexOf("?");
			if(-1 != indexOf) {
				return strUrl.substring(indexOf+1, strUrl.length());
			} 
			
			return "";
		}
		
		return strUrl;
	}

	public String getResContent() {
		return resContent;
	}

	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}
	
}
