package com.pay.merchantinterface.service;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;

import util.JWebConstant;
import util.MD5;

import com.PayConstant;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class CertUtil {
	private static RSAPrivateKey serverPrivateKey = null;
	private static RSAPublicKey serverPublicKey = null;
	static {
		//载入系统证书
		init(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
				+JWebConstant.PATH_DIV+"server_cert.pfx",PayConstant.PAY_CONFIG.get("server_cert_pwd"));
	}
	/**
	 * 载入系统证书
	 */
	private static void init(String certPath, String certPassword) {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			FileInputStream fis = new FileInputStream(certPath);
			char[] nPassword = null;
			if ((certPassword == null) || certPassword.trim().equals("")) nPassword = null;
			else nPassword = certPassword.toCharArray();
			ks.load(fis, nPassword);
			fis.close();
			Enumeration enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements()) keyAlias = (String) enumas.nextElement();
			serverPrivateKey = (RSAPrivateKey) ks.getKey(keyAlias, nPassword);
			serverPublicKey = (RSAPublicKey) ks.getCertificate(keyAlias).getPublicKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 签名
	 * @param sourceData 签名源数据
	 * @return 签名数据
	 * @throws Exception
	 */
	protected static byte[] signWithRSA(byte sourceData[]) throws Exception {
		byte signMessage[] = null;
		try {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initSign(serverPrivateKey);
			signature.update(sourceData);
			signMessage = signature.sign();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("签名失败");
		}
		return signMessage;
	}
	/**
	 * 验签
	 * @param sourceData 签名源数据
	 * @param signMessage 签名数据
	 * @return 验签结果
	 * @throws Exception
	 */
	protected static boolean verifyWithRSA(byte sourceData[], byte signMessage[],Certificate certificate)
			throws Exception {
		boolean verifySuccessed = false;
		try {
			Signature signature = Signature.getInstance("SHA1withRSA");
			signature.initVerify(certificate);
			signature.update(sourceData);
			verifySuccessed = signature.verify(signMessage);
		} catch (Exception e) {
			throw new Exception("签名不合法");
		}
		return verifySuccessed;
	}
	/**
	 * 发送信息拼接（原串+签名串）
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static String createTransStr(String src) throws Exception {
		String xmlBase64Sign = new String(Base64.encode(CertUtil.signWithRSA(MD5.getDigest(src.getBytes("utf-8")))));
		String xmlBase64 = new String(Base64.encode(src.getBytes("utf-8")));
		return xmlBase64+"|"+xmlBase64Sign;
	}
}
