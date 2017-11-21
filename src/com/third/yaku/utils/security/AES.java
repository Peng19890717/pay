package com.third.yaku.utils.security;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * @author zhangly AES 算法
 */
public class AES
{
	/**
     * 密钥算法
     */
    public static final String KEY_ALGORITHM    = "AES";

    /**
     * 加密/解密算法 、工作模式、填充方式
     */
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 转换密钥
     * 
     * @param key
     *            二进制密钥
     * @return key 密钥
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception
    {
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    /**
     * 解密
     * 
     * @param data
     *            待解密数据
     * @param key
     *            密钥
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception
    {
        // 还原密钥
        Key k = toKey(key);
        /*
         * 实例化 使用PKCS7Paddingtian填充方式，按如下方式实现
         * Cipher.getInstance(CIPHER_ALGORITHM,"BC");
         */
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        // 初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, k);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 加密
     * 
     * @param data
     *            待加密数据
     * @param key
     *            密钥
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception
    {
        // 还原密钥
        Key k = toKey(key);
        /*
         * 实例化 使用PKCS7Padding填充方式，按如下方式实现
         * Cipher.getInstance(CIPHER_ALGORITHM,"BC");
         */
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, k);
        // 执行操作
        return cipher.doFinal(data);
    }

    public static byte[] initKey() throws Exception
    {
        // 实例化
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // AES要求密钥长度为128位，192或256位
        kg.init(128);
        // 生成私密密钥
        SecretKey secretKey = kg.generateKey();
        // 获得密钥的二进制编码形式
        return secretKey.getEncoded();
    }
}
