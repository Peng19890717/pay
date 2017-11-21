package com.third.hengfeng.uitl;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 * 验证证书公共类
 * @author: sunc   
 * @sketch:
 */
public class CAUtil {
	
	private static final String ENCODING = "UTF-8";
	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	/**
	 * 加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public static String encrypt(PublicKey publicKey, String plainTextData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] signB = sha1X16(plainTextData, ENCODING);// 进行摘要算法
			byte[] b = cipher.doFinal(signB);
			return Hex2Str(b);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			throw new Exception("加密失败");
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * 解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static String decrypt(PrivateKey privateKey, String cipherData)
			throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] b = cipher.doFinal(Str2Hex(cipherData));
			return new String(b);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			throw new Exception("解密语法有误");
		} catch (InvalidKeyException e) {
			throw new Exception("解密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	// 获取私钥
	public static PrivateKey getPrivateKey(String privateKeyAddr,
			String privateKeyPwd) throws Exception {
		KeyStore ks;
		try {
			ks = KeyStore.getInstance("PKCS12");
			char[] nPassword = null;
			if ((privateKeyPwd == null) || privateKeyPwd.trim().equals("")) {
				privateKeyPwd = null;
			} else {
				nPassword = privateKeyPwd.toCharArray();
			}
			FileInputStream fis = new FileInputStream(privateKeyAddr);
			ks.load(fis, nPassword);
			fis.close();
			Enumeration<?> enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements()) {
				keyAlias = (String) enumas.nextElement();
			}
			PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
			return prikey;
		} catch (KeyStoreException e) {
			throw new Exception("获取KeyStore失败");
		} catch (FileNotFoundException e) {
			throw new Exception("无效的私钥地址");
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("读取私钥失败");
		} catch (CertificateException e) {
			throw new Exception("加载证书失败");
		} catch (IOException e) {
			throw new Exception("读取证书失败");
		} catch (UnrecoverableKeyException e) {
			throw new Exception("获取私钥失败");
		}
	}

	// 获取公钥
	public static PublicKey getPublicKey(String publicKeyAddr) throws Exception {
		try {
			CertificateFactory certificatefactory = CertificateFactory
					.getInstance("X.509");
			FileInputStream bais = new FileInputStream(publicKeyAddr);
			X509Certificate Cert = (X509Certificate) certificatefactory
					.generateCertificate(bais);
			bais.close();
			PublicKey pk = Cert.getPublicKey();
			return pk;
		} catch (CertificateException e) {
			throw new Exception("获取公钥失败");
		}
	}

	// 摘要算法sha1
	public static byte[] sha1X16(String data, String encoding) throws Exception {
		byte[] bytes = sha1(data, encoding);
		StringBuilder sha1StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha1StrBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			} else
				sha1StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
		}
		try {
			return sha1StrBuff.toString().getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new Exception("SHA1计算失败");
		}
	}

	public static byte[] sha1(byte[] data) throws Exception {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(data);
			return md.digest();
		} catch (Exception e) {
			throw new Exception("SHA1计算失败");
		}
	}

	public static byte[] sha1(String datas, String encoding) throws Exception {
		try {
			return sha1(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new Exception("SHA1计算失败");
		}
	}

	/**
	 * RSA签名
	 * 
	 * @param localPrivKey
	 *            私钥
	 * @param plaintext
	 *            需要签名的信息
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] signRSA(byte[] plainBytes,boolean useBase64Code,PrivateKey privKey) throws Exception {
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privKey);
		signature.update(plainBytes);
		// 如果是Base64编码的话，需要对签名后的数组以Base64编码
		if (useBase64Code) {
			return Base64.encodeBase64(signature.sign());
		} else {
			return signature.sign();
		}
	}

	/**
	 * 验签操作
	 * 
	 * @param peerPubKey
	 *            公钥
	 * @param plainBytes
	 *            需要验签的信息
	 * @param signBytes
	 *            签名信息
	 * @return boolean
	 */
	public static boolean verifyRSA(byte[] plainBytes, byte[] signBytes, boolean useBase64Code,PublicKey pubKey) throws Exception {
		boolean isValid = false;
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(plainBytes);
		// 如果是Base64编码的话，需要对验签的数组以Base64解码
		if (useBase64Code) {
			isValid = signature.verify(Base64.decodeBase64(signBytes));
		} else {
			isValid = signature.verify(signBytes);
		}
		return isValid;
	}
	
	//hex转str
	public static String Hex2Str(byte[] b) {
		StringBuffer d = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			char hi = Character.forDigit(b[i] >> 4 & 0xF, 16);
			char lo = Character.forDigit(b[i] & 0xF, 16);
			d.append(Character.toUpperCase(hi));
			d.append(Character.toUpperCase(lo));
		}
		return d.toString();
	}
	
	//str转hex
	public static byte[] Str2Hex(String str) {
		char[] ch = str.toCharArray();
		byte[] b = new byte[ch.length / 2];
		for (int i = 0; (i < ch.length) && (ch[i] != 0); i++) {
			if ((ch[i] >= '0') && (ch[i] <= '9'))
				ch[i] = ((char) (ch[i] - '0'));
			else if ((ch[i] >= 'A') && (ch[i] <= 'F')) {
				ch[i] = ((char) (ch[i] - 'A' + 10));
			}
		}
		for (int i = 0; i < b.length; i++) {
			b[i] = ((byte) ((ch[(2 * i)] << '\004' & 0xF0) + (ch[(2 * i + 1)] & 0xF)));
		}
		return b;
	}
		
	public static void main(String[] args) {
		//------------私钥加签  公钥解签例子	----------------------
		String privateKeyAddr = "d:/M000000000000700.pfx";// 私钥地址
		String privateKeyPwd = "123456";// 私钥密码
		try {
			String textStr = "xxxxxxxxxxxxxxx";
			PrivateKey privKey = getPrivateKey(privateKeyAddr, privateKeyPwd);
			String publicKeyAddr = "d:/M000000000000700.cer";// 公钥地址
			PublicKey peerPubKey = getPublicKey(publicKeyAddr);
			byte[] b = signRSA(textStr.getBytes(),true,privKey);
			String str = Hex2Str(b);
			System.out.println("加签后的串：："+Hex2Str(b));
			boolean bl = verifyRSA(textStr.getBytes(),Str2Hex(str),true,peerPubKey);
			System.out.println("验签结果：：："+bl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	//------------公钥加密  私钥解密例子----------------------
//		String cipher = null;// 加密后的串
//		try {
//			String text = "哈哈哈";// 要加密的字符串
//			System.out.println("加密前摘要后的串：：：" + new String(sha1X16(text, encoding)));
//			String publicKeyAddr = "d:\\hfBankCA.cer";// 公钥地址
//			cipher = encrypt(getPublicKey(publicKeyAddr), text); // 加密
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
//
//		String plainText = null;// 解密后的串
//		try {
//			String privateKeyAddr = "d:\\hfBankCA.pfx";// 私钥地址
//			String privateKeyPwd = "123456";// 私钥密码
//			plainText = decrypt(getPrivateKey(privateKeyAddr, privateKeyPwd),
//					cipher);// 解密
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
//		System.out.println("解密后的串：：：：：：" + plainText);
	}
}