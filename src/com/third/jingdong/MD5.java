package com.third.jingdong;

import java.security.MessageDigest;


public class MD5 {
	public static void main(String [] args){
	}
    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key) throws Exception {
        byte[] bytes = (text + key).getBytes();

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes);
        bytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                sb.append("0");
            }

            sb.append(Long.toString(bytes[i] & 0xff, 16));
        }

        return sb.toString().toLowerCase();
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @param key  字符集
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key, String charset) throws Exception {
        byte[] bytes = (text + key).getBytes(charset);

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes);
        bytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] & 0xff) < 0x10) {
                sb.append("0");
            }

            sb.append(Long.toString(bytes[i] & 0xff, 16));
        }

        return sb.toString().toLowerCase();
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @param type 大小写类型
     *             大写/小写(true/false)
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key, boolean type) throws Exception {
        String lowerCase = md5(text, key);
        return type ? lowerCase.toUpperCase() : lowerCase;
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @param type 大小写类型
     *             大写/小写(true/false)
     * @param key  字符集
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key, String charset, boolean type) throws Exception {
        String lowerCase = md5(text, key, charset);
        return type ? lowerCase.toUpperCase() : lowerCase;
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key  密钥
     * @param md5  密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text, String key, String md5) throws Exception {
        String md5Text = md5(text, key);
        if (md5Text.equalsIgnoreCase(md5)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key  密钥
     * @param md5  密文
     * @param key  字符集
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text, String key, String charset, String md5) throws Exception {
        String md5Text = md5(text, key, charset);
        if (md5Text.equalsIgnoreCase(md5)) {
            return true;
        } else {
            return false;
        }
    }
}