package com.third.kbnew.Tools;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;

public class RSAUtil {

    private String privateKey;
    private String publicKey;

    public RSAUtil() {
    }

    public RSAUtil(String privateKey) {
	this.privateKey = privateKey;
    }

    public String getPrivateKey() {
	return privateKey;
    }

    public void setPrivateKey(String privateKey) {
	this.privateKey = privateKey;
    }

    public String getPublicKey() {
	return publicKey;
    }

    public void setPublicKey(String publicKey) {
	this.publicKey = publicKey;
    }

    private PrivateKey privateKey() throws Exception {
	byte[] keyBytes = new byte[Base64.decodeBase64(privateKey).length];
	DataInputStream privateKeyFile = new DataInputStream(
		new ByteArrayInputStream(Base64.decodeBase64(privateKey)));
	privateKeyFile.readFully(keyBytes);

	PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
	return privateKey;
    }

    public String sign(String value) throws Exception {
	Signature signature = Signature.getInstance("SHA1withRSA");

	signature.initSign(privateKey());
	// 处理request字符串
	value = RSAUtil.removeNull(value);
	byte[] message = value.getBytes("UTF-8");
	signature.update(message);
	byte[] sigBytes = signature.sign();

	return (Base64.encodeBase64String(sigBytes));
    }

    public String getRSA(String value) throws Exception {
	Signature signature = Signature.getInstance("SHA1withRSA");

	signature.initSign(privateKey());
	// 处理request字符串
	value = RSAUtil.removeNull(value);
	byte[] message = value.getBytes("UTF-8");
	signature.update(message);
	byte[] sigBytes = signature.sign();

	return (Base64.encodeBase64String(sigBytes));
    }

    public static String removeNull(String value) {
	value = value + "&";
	StringBuffer valueBuffer = new StringBuffer();
	int startIndex = 0;
	int tempIndex, equalIndex;
	while (true) {
	    equalIndex = value.indexOf("=", startIndex + 1);
	    tempIndex = value.indexOf("&", startIndex + 1);
	    if (equalIndex + 1 != tempIndex && tempIndex > 0) {
		if (!value.substring(equalIndex + 1, tempIndex).toUpperCase()
			.equals("NULL")) {
		    valueBuffer.append(value.substring(startIndex, tempIndex));
		    valueBuffer.append("&");
		}
	    }

	    if (tempIndex == value.length() - 1) {
		valueBuffer.deleteCharAt(valueBuffer.length() - 1);
		break;
	    }
	    startIndex = tempIndex + 1;
	}
	return valueBuffer.toString();
    }
    public static String byte2hex(byte[] b) // 二行制转字符串
    {
	String hs = "";
	String stmp = "";
	for (int n = 0; n < b.length; n++) {
	    stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
	    if (stmp.length() == 1) {
		hs = hs + "0" + stmp;
	    } else {
		hs = hs + stmp;
	    }
	    if (n < b.length - 1) {
		hs = hs + "";
	    }
	}
	return hs.toUpperCase();
    }

    public static byte[] hex2byte(String str) {
	if (str == null) {
	    return null;
	}
	str = str.trim();
	int len = str.length();
	if (len == 0 || len % 2 == 1) {
	    return null;
	}
	byte[] b = new byte[len / 2];
	try {
	    for (int i = 0; i < str.length(); i += 2) {
		b[i / 2] = (byte) Integer
			.decode("0x" + str.substring(i, i + 2)).intValue();
	    }
	    return b;
	} catch (Exception e) {
	    return null;
	}
    }

    /**
     * 取得新浪支付RSA公文加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public String encrypt(String data, String key) throws Exception {
	// 对公钥解密
	byte[] keyBytes = decryptBASE64(key);

	// 取得公钥
	X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);

	KeyFactory keyFactory = KeyFactory.getInstance("RSA");

	Key publicKey = keyFactory.generatePublic(x509KeySpec);
	Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
	cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	byte[] bytes = data.getBytes("UTF-8");
	return Base64.encodeBase64String(cipher.doFinal(bytes));

    }

    public String dencrypt(String data, String key) throws Exception {
	// 对私钥解密
	byte[] keyBytes = decryptBASE64(key);

	// 取得私钥
	PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

	KeyFactory keyFactory = KeyFactory.getInstance("RSA");

	Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
	Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
	cipher.init(Cipher.DECRYPT_MODE, privateKey);
	return new String(cipher.doFinal(Base64.decodeBase64(data)), "UTF-8");

    }

    public static byte[] decryptBASE64(String key) throws Exception {
	return (new BASE64Decoder()).decodeBuffer(key);
    }
}
