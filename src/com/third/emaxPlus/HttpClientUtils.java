package com.third.emaxPlus;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;


/**
 * Http client 工具类
 * @author 孙钰佳
 *
 */
public class HttpClientUtils {
	private static final Log LOG = LogFactory.getLog(HttpClientUtils.class);
	private static final int BUFFER_SIZE = 1024;  

	/**
	 * 将map中的参数放入PostMethod中。
	 * @param method
	 * @param paras
	 * @throws URIException 
	 */
	public static void putParameters(HttpMethod method,Map<String,String> paras){
		try {
			Iterator<String> iterator=paras.keySet().iterator();
			String url=method.getURI().toString();
			String paraUrl="";
			while(iterator.hasNext()){
				String key=iterator.next();
				String value=paras.get(key);
				if(value==null){
					LOG.info(key+"http请求中参数为空,map;"+ JSONObject.toJSONString(paras));
				}else{
					if(method instanceof PostMethod){
						PostMethod pm = (PostMethod) method;
						pm.setParameter(key, value);
					}else{
						paraUrl+="&"+key+"="+value;
					}
				}
			}
			if(paraUrl.length()>0){
				paraUrl="?"+paraUrl.substring(1);
			}
			if(method instanceof GetMethod){
				method.setURI(new URI(url+paraUrl,false,"UTF-8"));
			}
			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		} catch (Exception e) {
			LOG.error("组装Http参数时失败"+e.getMessage(),e);
		}
	}

	public static byte[] getBytesFromInputStream(InputStream is)
			throws IOException {
		ByteArrayOutputStream bos = null;
		try {
			bos = new ByteArrayOutputStream();
			in2OutStream(is, bos, BUFFER_SIZE);
			return bos.toByteArray();
		} finally {
			if (is != null)
				is.close();
			if (bos != null)
				bos.close();		
		}
	}

	public static void in2OutStream(InputStream in, OutputStream out, int bufferSize) throws IOException {
		byte[] buffer = new byte[bufferSize];// 缓冲区
		for (int bytesRead = 0; (bytesRead = in.read(buffer)) != -1;) {
			out.write(buffer, 0, bytesRead);
			Arrays.fill(buffer, (byte) 0);
		}
	}
}
