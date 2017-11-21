/** 
 * MyX509TrustManager.java   2013-11-29 
 * 
 * Copyright(c) 2000-2013 Rain, All Rights Reserved. 
 */  
package com.third.syx.util;  
  
import java.security.cert.CertificateException;  
import java.security.cert.X509Certificate;  
  
import javax.net.ssl.X509TrustManager;  
  
/** 
 * 证书信任管理器（用于https请求） 
 *  
 * @author Rain 
 * @date 2013-11-29 
 * @version 1.0 
 */  
public class MyX509TrustManager implements X509TrustManager {  
  
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {  
        // TODO Auto-generated method stub  
    }  
  
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {  
        // TODO Auto-generated method stub  
    }  
  
    public X509Certificate[] getAcceptedIssuers() {  
        // TODO Auto-generated method stub  
        return null;  
    }  
  
}  