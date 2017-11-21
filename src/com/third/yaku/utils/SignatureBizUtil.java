package com.third.yaku.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.third.yaku.utils.security.Md5Encrypt;

/**
 * @author zhangly
 */
public class SignatureBizUtil {

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("signType")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) { // 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

	public static String createLinkStringForCharset(Map<String, String> params, String inputCharset)
			throws UnsupportedEncodingException {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) { // 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + URLEncoder.encode(value, inputCharset);
			} else {
				prestr = prestr + key + "=" + URLEncoder.encode(value, inputCharset) + "&";
			}
		}
		return prestr;
	}

	public static String createLinkStr(Map<String, String> params, String inputCharset)
			throws UnsupportedEncodingException {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) { // 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

	/**
	 * 生成签名结果
	 * 
	 * @param sArray
	 *            要签名的数组
	 * @return 签名结果字符串
	 */
	public static String buildMd5Sign(Map<String, String> sArray, String key) {
		Map<String, String> params = paraFilter(sArray); // 去除空值
		String prestr = createLinkString(params); // //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		prestr = prestr + key;// 把拼接后的字符串再与安全校验码直接连接起来
		String inputCharset = params.get("inputCharset");
		String localSign = Md5Encrypt.md5(prestr, inputCharset);
		return localSign;
	}

	/**
	 * 生成参与签名的参数
	 * 
	 * @param sArray
	 * @param key
	 * @return
	 */
	public static String consGenRsaSignParams(Map<String, String> sArray) {
		Map<String, String> params = paraFilter(sArray); // 去除空值
		String prestr = createLinkString(params); // //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		return prestr;
	}

}
