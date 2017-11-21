/**
 * Copyright (c) 2011-2015 by AllScore
 * All rights reserved.
 */
package com.third.yaku.utils.security;

/**
 * 通用签名工具类。
 * 
 * @author niuyt
 * 
 */
public class GenericSignTools {

	/**
	 * 
	 * @param content
	 *            生成验签串的原始字符串
	 * @param privateKey
	 *            密钥
	 * @param input_charset
	 *            字符编码
	 * @return
	 */
	public static String genSignStr(String content, String privateKey, String inputCharset, String signType) {
		if (signType.equals("MD5")) {
			// 掺入沙子
			String signBefore = content + privateKey;
			return Md5Encrypt.md5(signBefore, inputCharset);
		} else if (signType.equals("RSA")) {
			return RSA.sign(content, privateKey, inputCharset);
		}

		return null;
	}
}
