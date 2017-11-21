package com.third.cx;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {
	// 加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static String getBase64(byte[] b) {
		String s = null;

		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	public static String encode(byte[] binaryData) {
		return Base64.encodeBase64String(binaryData);
	}

	public static byte[] decode(String encoded) {
		return Base64.decodeBase64(encoded);
	}

}
