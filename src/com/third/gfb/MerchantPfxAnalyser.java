package com.third.gfb;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.time.DateFormatUtils;


/**
 * 商户公私钥PFX文件分析器
 * @MerchantPfxAnalyser.java
 * @author fanghw
 * @2017年2月6日 下午3:56:32  www.gopay.com.cn Inc.All rights reserved.
 */
public class MerchantPfxAnalyser {


    /**
     * 获取公钥
     * @param filePath
     * @param password
     * @param alias
     * @return
     */
    public static PublicKey getPublicKey(String filePath, String password, String alias) {
        PublicKey pubKey = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, password.toCharArray());

            if (alias == null || "".equals(alias.trim())) {
                Enumeration<String> enumas = ks.aliases();
                if (enumas.hasMoreElements()) {
                    alias = (String) enumas.nextElement();
                }
            }

            pubKey = ks.getCertificate(alias).getPublicKey();
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

        return pubKey;
    }

    /**
     * 获取私钥
     * @param filePath
     * @param password
     * @param alias
     * @return
     */
    public static PrivateKey getPrivateKey(String filePath, String password, String alias) {
        PrivateKey priKey = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, password.toCharArray());

            if (alias == null || "".equals(alias.trim())) {
                Enumeration<String> enumas = ks.aliases();
                if (enumas.hasMoreElements()) {
                    alias = (String) enumas.nextElement();
                }
            }

            priKey = (PrivateKey) ks.getKey(alias, password.toCharArray());
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

        return priKey;
    }

    /**
     * base64编码密钥
     * @param key
     * @return
     */
    public static String encodeBase64(Key key) {
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * base64解码
     * @param str
     * @return
     */
    public static byte[] decodeBase64(String str) {
        return Base64.decodeBase64(str);
    }

    /**
     * 获取X509证书
     * @param filePath
     * @param password
     * @param certSignBuf
     * @return
     */
    public static X509Certificate getX509Certificate(String filePath, String password,StringBuilder certSignBuf) {
        X509Certificate x509Cert = null;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(fis, password.toCharArray());

            Enumeration<String> enumas = ks.aliases();
            String alias = (String) enumas.nextElement();
            Certificate cert = ks.getCertificate(alias);

            certSignBuf.append(Base64.encodeBase64String(cert.getEncoded()));

            final String strCertificate = "-----BEGIN CERTIFICATE-----\n"
                    + Base64.encodeBase64String(cert.getEncoded())
                    + "\n-----END CERTIFICATE-----\n";

            final ByteArrayInputStream streamCertificate = new ByteArrayInputStream
                    (strCertificate.getBytes("UTF-8"));

            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            x509Cert = (X509Certificate) certificatefactory.generateCertificate(streamCertificate);
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

        return x509Cert;
    }

    /**
     * 显示证书详情
     * @param x509Cert
     * @param certSignBuf
     * @return
     */
    public static String view(X509Certificate x509Cert,String certSignBuf) {
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------");
        sb.append("\n版本号=" + x509Cert.getVersion());
        sb.append("\n序列号=" + x509Cert.getSerialNumber().toString(16).toUpperCase());
        sb.append("\n主体名=" + x509Cert.getSubjectDN());
        sb.append("\n签发者=" + x509Cert.getIssuerDN());
        sb.append("\n有效期=" + DateFormatUtils.format(x509Cert.getNotBefore(), "yyyy-MM-dd HH:mm:ss") + " 至 " + DateFormatUtils.format(x509Cert.getNotAfter(), "yyyy-MM-dd HH:mm:ss"));
        sb.append("\n签名算法=" + x509Cert.getSigAlgName());
        sb.append("\n签名=" + certSignBuf);
        sb.append("\n-------------------------");
        return sb.toString();
    }


}
