package com.third.cx;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;

public class RSAUtil {
	public static String sign(String content, String privateKey, String input_charset) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Util.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initSign(priKey);
			signature.update(content.getBytes(input_charset));
			//System.out.println(content.getBytes(input_charset));

			byte[] signed = signature.sign();

			return Base64Util.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean verify(String content, String sign, String public_key) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64Util.decode(public_key);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			Signature signature = Signature.getInstance("SHA1WithRSA");

			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));

			return signature.verify(Base64Util.decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map keyMap = new HashMap(2);
		keyMap.put("RSAPublicKey", publicKey);
		keyMap.put("RSAPrivateKey", privateKey);
		return keyMap;
	}

	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get("RSAPrivateKey");
		return Base64Util.encode(key.getEncoded());
	}

	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get("RSAPublicKey");
		return Base64Util.encode(key.getEncoded());
	}

	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		byte[] keyBytes = Base64Util.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(2, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;

		int i = 0;

		while (inputLen - offSet > 0) {
			byte[] cache;
			if (inputLen - offSet > 128)
				cache = cipher.doFinal(encryptedData, offSet, 128);
			else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * 128;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	public static String decryptByPrivateKey(String encryptedData, String privateKey) throws Exception {
		return new String(decryptByPrivateKey(new BASE64Decoder().decodeBuffer(encryptedData), privateKey));
	}

	public static byte[] encryptByCertificate(byte[] data, String certificateData) throws Exception {
		byte[] keyBytes = Base64Util.decode(certificateData);
		CertificateFactory cff = CertificateFactory.getInstance("X.509");
		ByteArrayInputStream bais = new ByteArrayInputStream(keyBytes);
		Certificate cf = cff.generateCertificate(bais);

		PublicKey pk1 = cf.getPublicKey();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

		cipher.init(1, pk1);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;

		int i = 0;

		while (inputLen - offSet > 0) {
			byte[] cache;
			if (inputLen - offSet > 117)
				cache = cipher.doFinal(data, offSet, 117);
			else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * 117;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	public static String encryptByCertificate(String content, String publicKey) throws Exception {
		return Base64Util.encode(encryptByCertificate(content.getBytes(), publicKey));
	}

	public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
		byte[] keyBytes = Base64Util.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		Key publicK = keyFactory.generatePublic(x509KeySpec);

		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(1, publicK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;

		int i = 0;

		while (inputLen - offSet > 0) {
			byte[] cache;
			if (inputLen - offSet > 117)
				cache = cipher.doFinal(data, offSet, 117);
			else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * 117;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	public static String encryptByPublicKey(String content, String publicKey) throws Exception {
		return Base64Util.encode(encryptByPublicKey(content.getBytes(), publicKey));
	}
}