package com.third.wlt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;



public class SignUtil {

	/**
	 * 对请求进行签名。
	 */

	public static String encryptSign(Map<String, String> params, String privateKey, String signType)
			throws IOException {
		String content = getContentByParamMap(params);
		String signstr = RSAUtil.sign(content, privateKey, "utf-8");
		System.out.println("签名原串：" + content);
		System.out.println("签名串：" + signstr);
		return signstr;
	}

	public static boolean verifySign(Map<String, String> paramsMap, String publicKey) {
		String content = getContentByParamMap(paramsMap);
		System.out.println("SignCheckInterceptor.验签串" + content);
		// 验证RSA加密方式
		if (!RSAUtil.verify(content, paramsMap.get("sign"), publicKey, "utf-8")) {
			System.out.println("验签失败,{}" + content);
			return false;
		}
		return true;
	}

	private static String getContentByParamMap(Map<String, String> params) {
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder();
		boolean hasParam = false;
		for (String key : keys) {
			String value = params.get(key);
			// 除sign、signType字段
			if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value) && !key.equals("sign")
					&& !key.equals("signMethod")) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}
				query.append(key).append("=").append(value);
			}
		}
		return query.toString();
	}

}
