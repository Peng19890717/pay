package com.third.baofo;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.JWebConstant;

import com.PayConstant;


/**
 * <b>Rsa加解密工具</b><br>
 * <br>
 * 公钥采用X509,Cer格式的<br>
 * 私钥采用PKCS12加密方式的PFX私钥文件<br>
 * 加密算法为1024位的RSA，填充算法为PKCS1Padding<br>
 * 
 * @author 行者
 * @version 4.1.0
 */
public final class RsaCodingUtil {
	static PublicKey publicKey;
	static PrivateKey privateKey;
	static {
		//宝付私钥
		String  pfxpath= JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
				+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("bf_merchant_pfx");
		//宝付私钥密码。
		String bf_merchant_pfx_pwd = PayConstant.PAY_CONFIG.get("bf_merchant_pfx_pwd");
		//宝付公钥路径。
        String  cerpath =JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
        		+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("bf_public_cert");
        privateKey = RsaReadUtil.getPrivateKeyFromFile(pfxpath, bf_merchant_pfx_pwd);
        publicKey = RsaReadUtil.getPublicKeyFromFile(cerpath);
	}
	
	// ======================================================================================
	// 公钥加密私钥解密段
	// ======================================================================================

	/**
	 * 指定Cer公钥路径加密
	 * 
	 * @param src
	 * @param pubCerPath
	 * @return hex串
	 */
	public static String encryptByPubCerFile(String src) {
		if (publicKey == null) return null;
		return encryptByPublicKey(src);
	}

	/**
	 * 用公钥内容加密
	 * 
	 * @param src
	 * @param pubKeyText
	 * @return hex串
	 */
	public static String encryptByPubCerText(String src) {
		if (publicKey == null)return null;
		return encryptByPublicKey(src);
	}

	/**
	 * 公钥加密返回
	 * 
	 * @param src
	 * @param publicKey
	 * @param encryptMode
	 * @return hex串
	 */
	public static String encryptByPublicKey(String src) {
		byte[] destBytes = rsaByPublicKey(src.getBytes(), Cipher.ENCRYPT_MODE);
		if (destBytes == null)return null;
		return FormatUtil.byte2Hex(destBytes);
	}

	/**
	 * 根据私钥文件解密
	 * 
	 * @param src
	 * @param pfxPath
	 * @param priKeyPass
	 * @return
	 */
	public static String decryptByPriPfxFile(String src) {
		if (FormatUtil.isEmpty(src)) return null;
		return decryptByPrivateKey(src);
	}

	/**
	 * 根据私钥文件流解密
	 * 
	 * @param src
	 * @param pfxPath
	 * @param priKeyPass
	 * @return
	 */
	public static String decryptByPriPfxStream(String src) {
		if (FormatUtil.isEmpty(src)) return null;
		return decryptByPrivateKey(src);
	}

	/**
	 * 私钥解密
	 * 
	 * @param src
	 * @param privateKey
	 * @return
	 */
	public static String decryptByPrivateKey(String src) {
		if (FormatUtil.isEmpty(src)) return null;
		try {
			byte[] destBytes = rsaByPrivateKey(FormatUtil.hex2Bytes(src), Cipher.DECRYPT_MODE);
			if (destBytes == null) return null;
			return new String(destBytes, RsaConst.ENCODE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// ======================================================================================
	// 私钥加密公钥解密
	// ======================================================================================

	/**
	 * 根据私钥文件加密
	 * 
	 * @param src
	 * @param pfxPath
	 * @param priKeyPass
	 * @return
	 */
	public static String encryptByPriPfxFile(String src) {
		if (privateKey == null) return null;
		return encryptByPrivateKey(src);
	}

	/**
	 * 根据私钥文件流加密
	 * 
	 * @param src
	 * @param pfxPath
	 * @param priKeyPass
	 * @return
	 */
	public static String encryptByPriPfxStream(String src) {
		if (privateKey == null) return null;
		return encryptByPrivateKey(src);
	}

	/**
	 * 根据私钥加密
	 * 
	 * @param src
	 * @param privateKey
	 */
	public static String encryptByPrivateKey(String src) {
		byte[] destBytes = rsaByPrivateKey(src.getBytes(),Cipher.ENCRYPT_MODE);
		if (destBytes == null) return null;
		return FormatUtil.byte2Hex(destBytes);

	}

	/**
	 * 指定Cer公钥路径解密
	 * 
	 * @param src
	 * @param pubCerPath
	 * @return
	 */
	public static String decryptByPubCerFile(String src) {
		if (publicKey == null) return null;
		return decryptByPublicKey(src);
	}

	/**
	 * 根据公钥文本解密
	 * 
	 * @param src
	 * @param pubKeyText
	 * @return
	 */
	public static String decryptByPubCerText(String src) {
		if (publicKey == null) return null;
		return decryptByPublicKey(src);
	}

	/**
	 * 根据公钥解密
	 * 
	 * @param src
	 * @param publicKey
	 * @return
	 */
	public static String decryptByPublicKey(String src) {
		try {
			byte[] destBytes = rsaByPublicKey(FormatUtil.hex2Bytes(src), Cipher.DECRYPT_MODE);
			if (destBytes == null) return null;
			return new String(destBytes, RsaConst.ENCODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ======================================================================================
	// 公私钥算法
	// ======================================================================================
	/**
	 * 公钥算法
	 * 
	 * @param srcData
	 *            源字节
	 * @param publicKey
	 *            公钥
	 * @param mode
	 *            加密 OR 解密
	 * @return
	 */
	public static byte[] rsaByPublicKey(byte[] srcData, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(RsaConst.RSA_CHIPER);
			cipher.init(mode, publicKey);
			// 分段加密
			int blockSize = (mode == Cipher.ENCRYPT_MODE) ? RsaConst.ENCRYPT_KEYSIZE : RsaConst.DECRYPT_KEYSIZE;
			byte[] encryptedData = null;
			for (int i = 0; i < srcData.length; i += blockSize) {
				// 注意要使用2的倍数，否则会出现加密后的内容再解密时为乱码
				byte[] doFinal = cipher.doFinal(subarray(srcData, i, i + blockSize));
				encryptedData = addAll(encryptedData, doFinal);
			}
			return encryptedData;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 私钥算法
	 * 
	 * @param srcData
	 *            源字节
	 * @param privateKey
	 *            私钥
	 * @param mode
	 *            加密 OR 解密
	 * @return
	 */
	public static byte[] rsaByPrivateKey(byte[] srcData, int mode) {
		try {
			Cipher cipher = Cipher.getInstance(RsaConst.RSA_CHIPER);
			cipher.init(mode, privateKey);
			// 分段加密
			int blockSize = (mode == Cipher.ENCRYPT_MODE) ? RsaConst.ENCRYPT_KEYSIZE : RsaConst.DECRYPT_KEYSIZE;
			byte[] decryptData = null;
			for (int i = 0; i < srcData.length; i += blockSize) {
				byte[] doFinal = cipher.doFinal(subarray(srcData, i, i + blockSize));
				decryptData = addAll(decryptData, doFinal);
			}
			return decryptData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static byte[] subarray(byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) return null;
		if (startIndexInclusive < 0) startIndexInclusive = 0;
		if (endIndexExclusive > array.length) endIndexExclusive = array.length;
		int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) return new byte[0];
		byte[] subarray = new byte[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}
	public static byte[] addAll(byte[] array1, byte[] array2) {
		if (array1 == null) return clone(array2);
		else if (array2 == null) return clone(array1);
		byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}
	public static byte[] clone(byte[] array) {
		if (array == null) return null;
		return (byte[]) array.clone();
	}
}
