/**
 * Copyright (c) 2011-2015 by AllScore
 * All rights reserved.
 */
package com.third.syx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.log4j.Logger;



/**
 * Post通知发送类。
 * 
 * @author niuyt
 * 
 */
/**
 * <p>Description:非http请求</p>
 * @param urlAddr
 * @param map
 * @param inputCharset
 * @return
 * @author 
 * @date 2015年5月24日 上午9:20:41
*/
public class PostNotifySender
{
	private static final Logger          logger          = Logger.getLogger(PostNotifySender.class);
    @SuppressWarnings("deprecation")
	public static String doPostProcess(String urlAddr, Map<String, String> map,
            String inputCharset)
    {
    	if (logger.isInfoEnabled())
        {
            logger.info(" doPostProcess start.params >>>>>>>>>>  urlAddr  = " + urlAddr+",map="+map+",inputCharset="+inputCharset);
        }
    	
    	String returnStr = null;

		String protocol="";
		String p[]=urlAddr.split(":");
		protocol=p[0].trim();
		
		logger.info(" Protocol = "+protocol);
		
		if(protocol.equals("https")){
			returnStr=doPostProcessHttps(urlAddr,map,inputCharset);
		}else{
			returnStr=doPostProcessHttp(urlAddr,map,inputCharset);
		}
    	
		return returnStr;
    	
    }
   public static void main(String[] args) throws Exception {
	String a = "body=1611091731374190697&inputCharset=UTF-8&ip=110.84.143.19&merchantId=001015013101118&notifyUrl=http://localhost/notify/AllScorePay/Bank_notify.aspx&outAcctId=1611091731374190697&outOrderId=1611091731374190697&payMethod=default_wechat&service=directPay&sign=af07a515613f15582f94b0db7755f373&signType=MD5&subject=1611091731374190697&transAmt=0.10";
	 HttpURLConnection conn = null;
	    InputStream is = null;
	    OutputStreamWriter out = null;
	    String returnStr = null;
	URL url = new URL("http://192.168.9.21:8686/olgateway/scan/scanPay.htm");
    conn = (HttpURLConnection) url.openConnection();
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setUseCaches(false);
    conn.setRequestProperty("Content-Type",
            "application/x-www-form-urlencoded");
    conn.setRequestProperty("Content-Length",
            String.valueOf(a.length()));
    conn.setDoInput(true);
/*            conn.setConnectTimeout(2000);
    conn.setReadTimeout(3000);  增加超时时间*/ 
    conn.setConnectTimeout(500000000);
    conn.setReadTimeout(500000000);
    conn.connect();
    out = new OutputStreamWriter(
            conn.getOutputStream(), "UTF-8");
    out.write(a.toString());
    out.flush();
    out.close();

    is = conn.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(is,
            "UTF-8"));
    java.lang.StringBuffer retCodeBuffer = new java.lang.StringBuffer();
    while ((returnStr = br.readLine()) != null)
    {
        retCodeBuffer.append(returnStr);
    }
    is.close();
    conn.disconnect();

    returnStr = retCodeBuffer.toString();
    if (logger.isInfoEnabled())
    {
        logger.info("resultStr  = " + returnStr);
    }
   }
    @SuppressWarnings("deprecation")
	public static String doPostProcessHttp(String urlAddr, Map<String, String> map,
            String inputCharset)
    {
    	if (logger.isInfoEnabled())
        {
            logger.info(" doPostProcess start.params >>>>>>>>>>  urlAddr  = " + urlAddr+",map="+map+",inputCharset="+inputCharset);
        }
    	  HttpURLConnection conn = null;
    	    InputStream is = null;
    	    OutputStreamWriter out = null;
    	    String returnStr = null;
        StringBuffer params = new StringBuffer();

        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, String> element = it.next();
            String value = element.getValue();
//            if("sign".equals(element.getKey())){
//            	  value=URLEncoder.encode(value);//编码转化
//            }
            params.append(element.getKey());
            params.append("=");
            params.append(value);
            params.append("&");
        }

        if (params.length() > 0)
        {
            params.deleteCharAt(params.length() - 1);
        }
        
        if (logger.isInfoEnabled())
        {
            logger.info("post Info = " + params);
        }
        
        try
        {
        	
            URL url = new URL(urlAddr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(params.length()));
            conn.setDoInput(true);
/*            conn.setConnectTimeout(2000);
            conn.setReadTimeout(3000);  增加超时时间*/ 
            conn.setConnectTimeout(500000000);
            conn.setReadTimeout(500000000);
            conn.connect();
            out = new OutputStreamWriter(
                    conn.getOutputStream(), inputCharset);
            out.write(params.toString());
            out.flush();
            out.close();

            is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    inputCharset));
            java.lang.StringBuffer retCodeBuffer = new java.lang.StringBuffer();
            while ((returnStr = br.readLine()) != null)
            {
                retCodeBuffer.append(returnStr);
            }
            is.close();
            conn.disconnect();

            returnStr = retCodeBuffer.toString();
            if (logger.isInfoEnabled())
            {
                logger.info("resultStr  = " + returnStr);
            }
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("doPostProcess error >>>>  "+ e.getMessage(),e);
            returnStr=null;

        }
        catch (ProtocolException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("doPostProcess error >>>>  "+ e.getMessage(),e);
            returnStr=null;
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("doPostProcess error >>>>  "+ e.getMessage(),e);
            returnStr=null;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("doPostProcess error >>>>  "+ e.getMessage(),e);
            returnStr=null;
        }
        finally
        {
          try
          {
            if (is != null) is.close();
            if (out != null) out.close();
            if (conn != null) conn.disconnect(); 
          }
          catch (Exception ex) { ex.printStackTrace(); returnStr=null;}

        }
        if (returnStr != null)
        {
            returnStr = returnStr.trim();
        }
        if (logger.isInfoEnabled())
        {
            logger.info(" doPostProcess end.params returnStr=" + returnStr);
        }
        return returnStr;
    }
    

    /**
      * <p>Description:访问httpspost请求方法</p>
      * @param urlAddr
      * @param map
      * @param inputCharset
      * @return
      * @author 
      * @date 2014年5月24日 上午9:18:25
     */
    @SuppressWarnings("deprecation")
	public static String doPostProcessHttps(String urlAddr, Map<String, String> map,
            String inputCharset)
    {
    	if (logger.isInfoEnabled())
        {
            logger.info(" doPostProcess start.params >>>>>>>>>>  urlAddr  = " + urlAddr+",map="+map+",inputCharset="+inputCharset);
        }
    	  HttpURLConnection conn = null;
    	    InputStream is = null;
    	    OutputStreamWriter out = null;
    	    String returnStr = null;
        StringBuffer params = new StringBuffer();

        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, String> element = it.next();
            String value = element.getValue();
           
            params.append(element.getKey());
            params.append("=");
            params.append(value);
            params.append("&");
        }

        if (params.length() > 0)
        {
            params.deleteCharAt(params.length() - 1);
        }
        
        if (logger.isInfoEnabled())
        {
            logger.info("post Info = " + params);
        }
        try
        {
        	
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
            URL url = new URL(urlAddr);
            HttpsURLConnection conn1 = (HttpsURLConnection) url.openConnection();  
            conn1.setSSLSocketFactory(ssf);  
            //conn = (HttpURLConnection) url.openConnection();
            conn1.setDoOutput(true);
            conn1.setRequestMethod("POST");
            conn1.setUseCaches(false);
            conn1.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn1.setRequestProperty("Content-Length",
                    String.valueOf(params.length()));
            conn1.setDoInput(true);
/*            conn.setConnectTimeout(2000);
            conn.setReadTimeout(3000);  增加超时时间*/ 
            conn1.setConnectTimeout(600000);
            conn1.setReadTimeout(600000);
            conn1.connect();
            out = new OutputStreamWriter(
                    conn1.getOutputStream(), inputCharset);
            out.write(params.toString());
            out.flush();
            out.close();

            is = conn1.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    inputCharset));
            java.lang.StringBuffer retCodeBuffer = new java.lang.StringBuffer();
            while ((returnStr = br.readLine()) != null)
            {
                retCodeBuffer.append(returnStr);
            }
            is.close();
            conn1.disconnect();

            returnStr = retCodeBuffer.toString();
            if (logger.isInfoEnabled())
            {
                logger.info("resultStr  = " + returnStr);
            }
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("doPostProcess error >>>>  "+ e.getMessage(),e);
            returnStr=null;

        }
        catch (ProtocolException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("doPostProcess error >>>>  "+ e.getMessage(),e);
            returnStr=null;
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("doPostProcess error >>>>  "+ e.getMessage(),e);
            returnStr=null;
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("doPostProcess error >>>>  "+ e.getMessage(),e);
            returnStr=null;
        }
        catch(Exception e){
        	
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("doPostProcess error >>>>  "+ e.getMessage(),e);
            returnStr=null;
        }
        finally
        {
          try
          {
            if (is != null) is.close();
            if (out != null) out.close();
            if (conn != null) conn.disconnect(); 
          }
          catch (Exception ex) { ex.printStackTrace(); returnStr=null;}

        }
        if (returnStr != null)
        {
            returnStr = returnStr.trim();
        }
        if (logger.isInfoEnabled())
        {
            logger.info(" doPostProcess end.params returnStr=" + returnStr);
        }
        return returnStr;
    }
}
