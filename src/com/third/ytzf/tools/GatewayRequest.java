package com.third.ytzf.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.third.ytzf.util.Md5Util;
import com.third.ytzf.util.ToolUtil;

public class GatewayRequest { 
	private static final Log log = LogFactory.getLog(GatewayRequest.class);
	private String key;
	private SortedMap<String,String> parameters; 
	private String debugMsg;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String urlEncoding;
	private String gatewayUrl;
	public static int BULID_SIGN = 0;
	public static int VERIFY_SIGN = 1;
	public GatewayRequest(){};
	public GatewayRequest(HttpServletRequest request,
			HttpServletResponse response, int type) throws UnsupportedEncodingException {
		this.request = request; 
		this.response = response;
		this.key = "";
		this.parameters = new TreeMap<String,String>();
		this.debugMsg = "";
		this.urlEncoding = "";
		this.gatewayUrl = "http://www.xxxx.com";
		if(type==VERIFY_SIGN){
			this.request.setCharacterEncoding("UTF-8"); 
			Map<String,String[]> m = this.request.getParameterMap();
			Iterator<String> it = m.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = ((String[]) m.get(k))[0];
				this.setParameter(k, v);
			}
		}
	}
	public GatewayRequest(HttpServletRequest request,
			HttpServletResponse response)  {
		this.request = request; 
		this.response = response;
		this.key = "";
		this.parameters = new TreeMap<String,String>();
		this.debugMsg = "";
		this.urlEncoding = "";
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	public String getParameters() {
		String s = (String)this.parameters.toString(); 
		return (null == s) ? "" : s;
	}
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}
	public SortedMap getAllParameters() {
		return this.parameters;
	}
	public boolean verifySign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(v);
			}
		}
		sb.append(this.getKey());
		String sign = Md5Util.MD5(sb.toString()).toLowerCase();
		String merchantSign = this.getParameter("sign").toLowerCase();
		
		return merchantSign.equals(sign);
	}
	public static void main(String args []){

	}
	public void responseToGateway(String msg) throws IOException {
		String strHtml = msg;
		PrintWriter out = this.getHttpServletResponse().getWriter();
		out.println(strHtml);
		out.flush();
		out.close();

	}
	public String getUrlEncoding() {
		return urlEncoding;
	}
	public void setUrlEncoding(String uriEncoding)
			throws UnsupportedEncodingException {
		if (!"".equals(uriEncoding.trim())) {
			this.urlEncoding = uriEncoding;
			String enc = ToolUtil.getCharacterEncoding(request, response);
			Iterator it = this.parameters.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = this.getParameter(k);
				v = new String(v.getBytes(uriEncoding.trim()), enc);
				this.setParameter(k, v);
			}
		}
	}
	public String getDebugMsg() {
		return debugMsg;
	}
	protected void setDebugMsg(String debugInfo) {
		this.debugMsg = debugInfo;
	}
	protected HttpServletRequest getHttpServletRequest() {
		return this.request;
	}
	protected HttpServletResponse getHttpServletResponse() {
		return this.response;
	}
	public void buildRequestSign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(null != v && !"".equals(v) && !"sign".equals(k)) {
				sb.append(v);
			}
		}
		sb.append(this.getKey());
		String sign = Md5Util.MD5(sb.toString()).toLowerCase();
		this.setParameter("sign", sign);
		this.setDebugMsg(sb.toString() + ",sign:" + sign);
	}
	public String getGatewayUrl() {
		return gatewayUrl;
	}
	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}
	public String getRequestURL() throws UnsupportedEncodingException {
		
		this.buildRequestSign();
		StringBuffer sb = new StringBuffer();
		String enc = ToolUtil.getCharacterEncoding(this.request, this.response);
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
		}
		String reqPars = sb.substring(0, sb.lastIndexOf("&"));
		log.info("易通支付请求参数:"+reqPars);
		if(this.getGatewayUrl().indexOf("?")!=-1){
			int index = this.getGatewayUrl().indexOf("?");
			String noParamUrl = this.getGatewayUrl().substring(0,index);
			return noParamUrl + "?" + reqPars;
		}else{
			return this.getGatewayUrl() + "?" + reqPars;
		}
	}
}
