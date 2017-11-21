package com.third.gfb;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * 签名验签工具类
 * @SignVerifyUtil.java
 * @author fanghw
 * @2017年2月6日 下午3:58:37  www.gopay.com.cn Inc.All rights reserved.
 */
public class SignVerifyUtil {

    /**
     * 签名
     * @param message
     * @param priKeyStr
     * @return
     */
    public static String sign(String message,String priKeyStr){       
        try {

            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(priKeyStr));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance("SHA1withRSA");
            signature.initSign(priKey);
            System.out.println("签名,原串 = " + message);
            String md5Str = DigestUtils.md5Hex(message.getBytes("UTF-8"));
            System.out.println("签名,md5哈希值 = " + md5Str);
            signature.update(md5Str.getBytes());
            byte[] signed = signature.sign();
            String signValue = Base64.encodeBase64String(signed);
            System.out.println("签名,signValue = " + signValue);
            return signValue;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 
     * @param message
     * @param signValue
     * @param pubKeyStr
     * @return
     */
    public static  boolean verify(String message, String signValue, String pubKeyStr) {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decodeBase64(pubKeyStr);

            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature
                    .getInstance("SHA1withRSA");

            signature.initVerify(pubKey);

            String md5Str = DigestUtils.md5Hex(message.getBytes());
            signature.update( md5Str.getBytes() );

            return signature.verify( Base64.decodeBase64(signValue) );

        }
        catch (Exception e)
        {
           // e.printStackTrace();
        }

        return false;
    }


}
