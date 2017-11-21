package com.third.cx;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {

	private static final String	AES				= "AES";
	private static final String	CHARSET_NAME	= "utf-8";

	/**
	 * 获取密钥
	 *
	 * @param password
	 *            加密密码
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKeySpec getKey(String password) throws NoSuchAlgorithmException {
		// 密钥加密器生成器
		KeyGenerator kgen = KeyGenerator.getInstance(AES);
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");// 需要自己手动设置
		random.setSeed(password.getBytes());
		kgen.init(128, random);

		// 创建加密器
		SecretKey secretKey = kgen.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();

		SecretKeySpec key = new SecretKeySpec(enCodeFormat, AES);

		return key;
	}

	/**
	 * 加密
	 * 
	 * @param str
	 *            原文
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static String encode(String str, String password) {
		byte[] arr = encodeToArr(str, password);
		return byteArrToString(arr);
	}
	/**
	 * AES加密
	 * 
	 * @param str
	 * @param key
	 * @return
	 * @throws NameAuthException
	 */
	public static String encrypt(final String str, final String key){
		try {
			System.out.println("[str]"+str+"[key]"+key);
			byte[] encryptByte = AESUtil.encrypt(str.getBytes("utf-8"), Base64Util.decode(key));
			return Base64Util.encode(encryptByte);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;
	}
	public static String sign(String content, String privateKey, String encode) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Util.decode(privateKey));

			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initSign(priKey);
			signature.update(content.getBytes(encode));
			byte[] signed = signature.sign();
			return Base64Util.encode(signed);// Base64.getEncoder().encodeToString(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	public static byte[] encrypt(byte[] data, byte[] key)
		    throws Exception
		  {
		    Key k = toKey(key);

		    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		    cipher.init(1, k);

		    return cipher.doFinal(data);
		  }
	private static Key toKey(byte[] key)
		    throws Exception
		  {
		    SecretKey secretKey = new SecretKeySpec(key, "AES");
		    return secretKey;
		  }

	/**
	 *
	 * 加密
	 * 
	 * @author wuhongbo
	 * @param str
	 *            原文
	 * @param password
	 *            加密密码
	 * @return
	 */
	private static byte[] encodeToArr(String str, String password) {
		try {
			Cipher cipher = Cipher.getInstance(AES);// 创建密码器
			byte[] byteContent = str.getBytes(CHARSET_NAME);

			cipher.init(Cipher.ENCRYPT_MODE, getKey(password));// 初始化
			byte[] result = cipher.doFinal(byteContent);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解密
	 *
	 * @param hexStr
	 *            密文
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static String decode(String hexStr, String password) {
		byte[] arr = string2ByteArr(hexStr);
		return decode(arr, password);
	}
	/**
	 * AES数据解密
	 * 
	 * @param encryptStr
	 * @param key
	 * @return
	 * @throws NameAuthException
	 */
	public static String decrypt(final String encryptStr, final String key) {
		try {
			byte[] encryptStrBytes = Base64Util.decode(encryptStr);
			byte[] aesKeyBytes = Base64Util.decode(key);//Base64.getDecoder().decode(key);
			byte[] encryptByte = decrypt(encryptStrBytes, aesKeyBytes);
			String decryptStr = new String(encryptByte);
			return decryptStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] decrypt(byte[] data, byte[] key)
		    throws Exception
		  {
		    Key k = toKey(key);

		    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		    cipher.init(2, k);

		    return cipher.doFinal(data);
		  }

	/**
	 * 解密
	 *
	 * @author wuhongbo
	 * @param arr
	 *            密文数组
	 * @param password
	 *            加密密码
	 * @return
	 */
	private static String decode(byte[] arr, String password) {
		try {
			// 创建密码器
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(Cipher.DECRYPT_MODE, getKey(password));// 初始化

			byte[] result = cipher.doFinal(arr);
			return new String(result, CHARSET_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * byte数组转成16进制字符串
	 *
	 * @param arr
	 * @return
	 */
	private static String byteArrToString(byte[] arr) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < arr.length; i++) {

			String s = Integer.toString(arr[i] + 128, 16);
			if (s.length() == 1) {
				s = "0" + s;
			}

			sb.append(s);
		}

		return sb.toString().toUpperCase();
	}

	/**
	 * 16进制字符串转成byte数组
	 *
	 * @param s
	 * @return
	 */
	private static byte[] string2ByteArr(String s) {
		s = s.toUpperCase();
		String str = "0123456789ABCDEF";

		byte[] arr = new byte[s.length() / 2];

		for (int i = 0; i < arr.length; i++) {
			char s1 = s.charAt(i * 2);
			char s2 = s.charAt(i * 2 + 1);

			int tmp1 = str.indexOf(s1) * 16;
			int tmp2 = str.indexOf(s2);

			arr[i] = (byte) (tmp1 + tmp2 - 128);

		}

		return arr;
	}

}