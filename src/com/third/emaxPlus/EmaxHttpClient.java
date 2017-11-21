package com.third.emaxPlus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

/**   
 * @Title: httpclient封装类，主要目的是记录httpaccess日志。
 * @Description: 
 * @author 孙钰佳
 * @date 2015年10月26日 下午9:16:16 
 */
public class EmaxHttpClient extends HttpClient{

	private static final Log log = LogFactory.getLog(EmaxHttpClient.class);

	private static final int BUFFER_SIZE = 1024;

	public EmaxHttpClient() {
		super(new HttpClientParams(),new SimpleHttpConnectionManager(true));
	}

	public int executeMethod(HttpMethod httpMethod) throws HttpException, IOException{
		return executeMethod(httpMethod,null);
	}

	public int executeMethod(HttpMethod httpMethod,String transactionTypeName) throws HttpException,IOException{
		return executeMethod(httpMethod,transactionTypeName,true);
	}

	public int executeMethod(HttpMethod httpMethod,String transactionTypeName,boolean isPrintLog) throws HttpException, IOException{
		return executeMethod(httpMethod,transactionTypeName,isPrintLog,false);
	}

	public int executeMethod(HttpMethod httpMethod,String transactionTypeName,boolean isPrintLog,boolean isTimeout) throws HttpException, IOException{
		return executeMethod(httpMethod,transactionTypeName,isPrintLog,isTimeout,5000);
	}
	public int executeMethod(HttpMethod httpMethod,String transactionTypeName,boolean isPrintLog,boolean timeout,int time) throws HttpException, IOException{
		String para="";
		httpMethod.setRequestHeader("Connection", "close");
		if(httpMethod.getName().equalsIgnoreCase("POST")){
			PostMethod postMethod=(PostMethod)httpMethod;
			NameValuePair[] nvp=postMethod.getParameters();
			if(nvp!=null){
				for (int i = 0; i < nvp.length; i++) {
					NameValuePair nv=nvp[i];
					para+="&"+nv.getName()+"="+nv.getValue();
				}
			}
		}
		long s=System.currentTimeMillis();
		int state=-1;
		try{
			if(timeout){
				//TODO  测试设置超时时间
				super.getHttpConnectionManager().getParams()
						.setConnectionTimeout(time);
				super.getHttpConnectionManager().getParams().setSoTimeout(time);
			}
			state = super.executeMethod(httpMethod);
		} catch (IllegalArgumentException e) {
			long e1=System.currentTimeMillis();
			log.error(httpMethod.getName()+"|"+httpMethod.getURI()+"|"+para+"|"+state+"|"+(e1-s)+"ms");
			throw e;
		} catch (Exception e) {
			long e2=System.currentTimeMillis();
			log.error("cmpHttpClient exception:"+httpMethod.getName()+"|"+httpMethod.getURI()+"|"+para+"|"+state+"|"+(e2-s)+"ms");
		}finally{
			long e=System.currentTimeMillis();
			if(isPrintLog){
				log.info(httpMethod.getName()+"|"+httpMethod.getURI()+"|"+para+"|"+state+"|"+(e-s)+"ms");
			}
		}
		return state;
	}
	
	public byte[] getResponseBodyAsByte(HttpMethod httpMethod){
		try {
			InputStream responseBody = httpMethod.getResponseBodyAsStream();
	        byte[] response = getBytesFromInputStream(responseBody);
	        return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private  byte[] getBytesFromInputStream(InputStream is)
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

	private  void in2OutStream(InputStream in, OutputStream out, int bufferSize) throws IOException {
		byte[] buffer = new byte[bufferSize];// 缓冲区
		for (int bytesRead = 0; (bytesRead = in.read(buffer)) != -1;) {
			out.write(buffer, 0, bytesRead);
			Arrays.fill(buffer, (byte) 0);
		}
	}
}
