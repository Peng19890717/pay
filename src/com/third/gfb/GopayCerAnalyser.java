package com.third.gfb;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;

/**
 * 国付宝公钥分析器
 * @GopayCerAnalyser.java
 * @author fanghw
 * @2017年2月6日 下午3:58:04  www.gopay.com.cn Inc.All rights reserved.
 */
public class GopayCerAnalyser {

    /**
     * 获取公钥
     * @param filePath
     * @return
     */
    public static String getPublicKey(String filePath) {
        String pubKeyStr = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);

            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            X509Certificate Cert = (X509Certificate) certificatefactory.generateCertificate(fis);
            PublicKey pk = Cert.getPublicKey();

            pubKeyStr= Base64.encodeBase64String(pk.getEncoded());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return pubKeyStr;

    }

}



