package com.third.yaku.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SignUtils {
	//private static final String KEY = AliClientInfo.EBUSINESS.getPayKey();

	// 生成签名
	public static String getSign(Map<String, String> params,String key,String charset) {
		// 过滤空值
		Map<String, String> sParaNew = paraFilter(params);
		String sign = Md5Encrypt.md5(getContent(sParaNew, key),charset);
		return sign;

	}

	// 验签
	public static boolean verify(Map<String, String> params,String key,String charset) {
		
		boolean flag = false;
		String mysign = getSign(params,key,charset);
		String sign = "";
		if (params.get("sign") != null) {
			sign = params.get("sign");
		}
		if (mysign.equals(sign)) {// 验签
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 功能：将安全校验码和参数排序 参数集合
	 * 
	 * @param params
	 *            安全校验码
	 * @param privateKey
	 * */
	private static String getContent(Map<String, String> params,
			String privateKey) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);

			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		String signParams = prestr + privateKey;
		return signParams;
	}

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
			if (value == null || value.equals("")
					|| key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("signType")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}
}
