package com.third.kb;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class SimpleHttpsClient
{
  private Map<Integer, Integer> registerPortList = new HashMap();

  public SimpleHttpsClient() {
    Protocol.registerProtocol("https", new Protocol("https", new SimpleHttpsSocketFactory(), 443));
    registerPort(Integer.valueOf(443));
  }

  public HttpSendResult postRequest(String url, Map<String, String> params, int timeout, String characterSet) {
    if (StringUtils.isEmpty(characterSet))
    {
      characterSet = "GBK";
    }
    HttpSendResult result = new HttpSendResult();
    PostMethod postMethod = new PostMethod(url);
    postMethod.setRequestHeader("Connection", "close");
    postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + characterSet);

    NameValuePair[] data = createNameValuePair(params);
    postMethod.setRequestBody(data);
    Integer port = getPort(url);
    if (isRegisterPort(port)) {
      Protocol myhttps = new Protocol("https", new SimpleHttpsSocketFactory(), port.intValue());
      Protocol.registerProtocol("https ", myhttps);
      registerPort(port);
    }

    HttpClient client = new HttpClient();
    client.getParams().setSoTimeout(timeout);
    try {
      long timeMillis = System.currentTimeMillis();
      int status = client.executeMethod(postMethod);
      long spendTime = System.currentTimeMillis() - timeMillis;
      System.out.println("http请求耗时:" + spendTime + "ms");
      InputStream is = postMethod.getResponseBodyAsStream();
      String responseBody = IOUtils.toString(is, characterSet);
      result.setStatus(status);
      result.setResponseBody(responseBody);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      postMethod.releaseConnection();
    }

    return result;
  }

  public HttpSendResult postRequest(String url, Map<String, String> params, int timeout)
  {
    return postRequest(url, params, timeout, "GBK");
  }

  public HttpSendResult getRequest(String url, Map<String, String> params, int timeout, String characterSet) {
    if (StringUtils.isEmpty(characterSet))
    {
      characterSet = "GBK";
    }
    HttpSendResult result = new HttpSendResult();
    Integer port = getPort(url);
    if (isRegisterPort(port)) {
      Protocol myhttps = new Protocol("https", new SimpleHttpsSocketFactory(), port.intValue());
      Protocol.registerProtocol("https ", myhttps);
      registerPort(port);
    }

    url = appendUrlParam(url, params);

    HttpClient httpclient = new HttpClient();
    GetMethod httpget = new GetMethod(url);
    try {
      int status = httpclient.executeMethod(httpget);
      InputStream is = httpget.getResponseBodyAsStream();
      String responseBody = IOUtils.toString(is, characterSet);
      result.setStatus(status);
      result.setResponseBody(responseBody);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      httpget.releaseConnection();
    }

    return result;
  }

  public HttpSendResult getRequest(String url, Map<String, String> params, int timeout)
  {
    return getRequest(url, params, timeout, "GBK");
  }

  private boolean isRegisterPort(Integer port) {
    return this.registerPortList.get(port) != null;
  }

  private void registerPort(Integer port) {
    this.registerPortList.put(port, port);
  }

  private Integer getPort(String uri) {
    try {
      URL url = new URL(uri);
      int port = url.getPort();
      if (port == -1) {
        if (uri.indexOf("https://") == 0)
          port = 443;
        else {
          port = 80;
        }
      }
      return Integer.valueOf(port);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  private NameValuePair[] createNameValuePair(Map<String, String> params) {
    NameValuePair[] pairs = new NameValuePair[params.size()];
    int index = 0;
    for (String key : params.keySet()) {
      pairs[(index++)] = new NameValuePair(key, (String)params.get(key));
    }

    return pairs;
  }

  private String appendUrlParam(String url, Map<String, String> params) {
    String result = "";
    if ((url.contains("?")) && (url.contains("=")))
      result = url + "&";
    else {
      result = url + "?";
    }

    for (String key : params.keySet()) {
      result = result + key + "=" + (String)params.get(key) + "&";
    }

    if (result.charAt(result.length() - 1) == '&') {
      result = result.substring(0, result.length() - 1);
    }

    return result;
  }

  private class SimpleHttpsSocketFactory implements ProtocolSocketFactory {
    private SSLContext sslcontext = null;

    public SimpleHttpsSocketFactory()
    {
    }

    private SSLContext createEasySSLContext() {
      try {
        X509TrustManager trustMgr = new X509TrustManager()
        {
          public void checkClientTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException
          {
          }

          public void checkServerTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException
          {
          }

          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }
        };
        TrustManager[] trustMgrs = { trustMgr };
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, trustMgrs, null);
        return context;
      } catch (Exception e) {
        e.printStackTrace();
        throw new HttpClientError(e.toString());
      }
    }

    private SSLContext getSSLContext()
    {
      if (this.sslcontext == null) {
        this.sslcontext = createEasySSLContext();
      }
      return this.sslcontext;
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
      throws IOException, UnknownHostException
    {
      return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params)
      throws IOException, UnknownHostException, ConnectTimeoutException
    {
      if (params == null) {
        throw new IllegalArgumentException("Parameters may not be null");
      }
      int timeout = params.getConnectionTimeout();
      SocketFactory socketfactory = getSSLContext().getSocketFactory();
      if (timeout == 0) {
        return socketfactory.createSocket(host, port, localAddress, localPort);
      }
      Socket socket = socketfactory.createSocket();
      SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
      SocketAddress remoteaddr = new InetSocketAddress(host, port);
      socket.bind(localaddr);
      socket.connect(remoteaddr, timeout);
      return socket;
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException
    {
      return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException
    {
      return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public boolean equals(Object obj) {
      return (obj != null) && (obj.getClass().equals(SSLSocketFactory.class));
    }

    public int hashCode() {
      return SimpleHttpsSocketFactory.class.hashCode();
    }
  }
}