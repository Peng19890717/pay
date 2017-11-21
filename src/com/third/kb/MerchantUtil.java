package com.third.kb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * 商户工具
 * 
 * @author lijinghao
 *
 */
public class MerchantUtil {
	/**
	 * 发送http请求
	 * 
	 * @param url
	 *            请求地址
	 * @param buf
	 *            K1&V1 ... Kn&Vn
	 * @param characterSet
	 *            编码格式,默认为GBK
	 * @param timeout
	 *            超时时间,默认为120000(ms)
	 * @return
	 * @throws IOException
	 */
	public static String sendAndRecv(String url, String buf, String characterSet, int timeout) throws IOException {
		String charType;
		if ("00".equals(characterSet) || "gbk".equalsIgnoreCase(characterSet)) {
			charType = "GBK";
		} else if ("01".equals(characterSet) || "GB2312".equalsIgnoreCase(characterSet)) {
			charType = "GB2312";
		} else if ("02".equals(characterSet) || "UTF-8".equalsIgnoreCase(characterSet))
			charType = "UTF-8";
		else {
			charType = "GBK";
		}
		String[] resArr = StringUtils.split(buf, "&");
		Map reqMap = new HashMap();
		for (int i = 0; i < resArr.length; i++) {
			String data = resArr[i];
			int index = StringUtils.indexOf(data, '=');
			String nm = StringUtils.substring(data, 0, index);
			String val = StringUtils.substring(data, index + 1);
			reqMap.put(nm, val);
		}
		String repMsg = dohttp(url, timeout, charType, reqMap);
		return repMsg;
	}
	/**
	 * 发送http请求
	 * 
	 * @param url
	 *            请求地址
	 * @param buf
	 *            K1&V1 ... Kn&Vn
	 * @param characterSet
	 *            编码格式,默认为GBK
	 * @return
	 * @throws IOException
	 */
	public static String sendAndRecv(String url, String buf, String characterSet) throws IOException {
		String charType;
		if ("00".equals(characterSet) || "gbk".equalsIgnoreCase(characterSet)) {
			charType = "GBK";
		} else if ("01".equals(characterSet) || "GB2312".equalsIgnoreCase(characterSet)) {
			charType = "GB2312";
		} else if ("02".equals(characterSet) || "UTF-8".equalsIgnoreCase(characterSet))
			charType = "UTF-8";
		else {
			charType = "GBK";
		}
		String[] resArr = StringUtils.split(buf, "&");
		Map reqMap = new HashMap();
		for (int i = 0; i < resArr.length; i++) {
			String data = resArr[i];
			int index = StringUtils.indexOf(data, '=');
			String nm = StringUtils.substring(data, 0, index);
			String val = StringUtils.substring(data, index + 1);
			reqMap.put(nm, val);
		}
		String repMsg = dohttp(url, 12000, charType, reqMap);
		return repMsg;
	}
	/**
	 * 发送http请求
	 * 
	 * @param url
	 *            请求地址
	 * @param dateMap
	 *            数据map,会自动移除空key
	 * @param charset
	 *            编码格式,默认gbk
	 * @return
	 * @throws Exception
	 */
	public static String sendAndRecv(String url, Map<String, String> dateMap, String charset) throws Exception {
		String charType;
		if ("00".equals(charset) || "gbk".equalsIgnoreCase(charset)) {
			charType = "GBK";
		} else if ("01".equals(charset) || "GB2312".equalsIgnoreCase(charset)) {
			charType = "GB2312";
		} else if ("02".equals(charset) || "UTF-8".equalsIgnoreCase(charset))
			charType = "UTF-8";
		else {
			charType = "GBK";
		}
		Map<String, String> reqMap = new HashMap<String, String>();
		Set<Entry<String, String>> entrySet = dateMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (StringUtils.isEmpty(entry.getKey()) || StringUtils.isEmpty(entry.getValue())) {
				continue;
			}
			reqMap.put(entry.getKey(), entry.getValue());
		}
		String repMsg = dohttp(url, 12000, charType, reqMap);
		return repMsg;
	}
	/**
	 * 发送http请求
	 * 
	 * @param url
	 *            请求地址
	 * @param dateMap
	 *            数据map,会自动移除空key
	 * @param charset
	 *            编码格式,默认gbk
	 * @param timeout
	 *            超时时间
	 * @return
	 * @throws Exception
	 */
	public static String sendAndRecv(String url, Map<String, String> dateMap, String charset, int timeout)
			throws Exception {
		String charType;
		if ("00".equals(charset) || "gbk".equalsIgnoreCase(charset)) {
			charType = "GBK";
		} else if ("01".equals(charset) || "GB2312".equalsIgnoreCase(charset)) {
			charType = "GB2312";
		} else if ("02".equals(charset) || "UTF-8".equalsIgnoreCase(charset))
			charType = "UTF-8";
		else {
			charType = "GBK";
		}
		Map<String, String> reqMap = new HashMap<String, String>();
		Set<Entry<String, String>> entrySet = dateMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			if (StringUtils.isEmpty(entry.getKey()) || StringUtils.isEmpty(entry.getValue())) {
				continue;
			}
			reqMap.put(entry.getKey(), entry.getValue());
		}
		String repMsg = dohttp(url, timeout, charType, reqMap);
		return repMsg;
	}

	private static String dohttp(String url, int timeout, String charType, Map reqMap) {
		SimpleHttpsClient httpsClient = new SimpleHttpsClient();
		HttpSendResult res = httpsClient.postRequest(url, reqMap, timeout, charType);
		String repMsg = res.getResponseBody();
		return repMsg;
	}
}