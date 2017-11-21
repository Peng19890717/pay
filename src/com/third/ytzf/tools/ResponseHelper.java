package com.third.ytzf.tools;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.jdom.JDOMException;
import com.alibaba.fastjson.JSON;
import com.third.ytzf.util.Md5Util;

public class ResponseHelper {
	

	private String content;
	

	private SortedMap parameters; 
	

	private String debugMsg;
	

	private String key;
	
	/** �ַ� */
	private String charset;
	
	public ResponseHelper() {
		this.content = "";
		this.parameters = new TreeMap();
		this.debugMsg = "";
		this.key = "";
		this.charset = "utf-8";
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) throws Exception {
		this.content = content;
		
		this.doParse();
	}
	
	/**
	 * ��ȡ����ֵ
	 * @param parameter �������
	 * @return String 
	 */
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	
	/**
	 * ���ò���ֵ
	 * @param parameter �������
	 * @param parameterValue ����ֵ
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}
	
	/**
	 * �������еĲ���
	 * @return SortedMap
	 */
	public SortedMap getAllParameters() {
		return this.parameters;
	}	

	public String getDebugMsg() {
		return debugMsg;
	}
	
	/**
	*��ȡ��Կ
	*/
	public String getKey() {
		return key;
	}

	/**
	*������Կ
	*/
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getCharset() {
		return this.charset;
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	/**
	 * ʹ��MD5�㷨��֤ǩ�������:���������a-z����,������ֵ�Ĳ���μ�ǩ��
	 * @return boolean
	 */
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
		
		//���ժҪ
		String sign = Md5Util.MD5(sb.toString()).toLowerCase();
		String merchantSign = this.getParameter("sign").toLowerCase();
		
		//debug��Ϣ
		this.setDebugMsg("���ز���:"+sb.toString()+",ǩ��:"+sign+",������ǩ��:"+merchantSign);
		
		return merchantSign.equals(sign);
	}
	

	protected void setDebugMsg(String debugMsg) {
		this.debugMsg = debugMsg;
	}
	
	/**
	 * ����json
	 */
	protected void doParse() throws JDOMException, IOException {
		String json = this.getContent();
		Map maps = (Map)JSON.parse(json);
		for (Object map : maps.entrySet()){
			String k = ((Map.Entry)map).getKey().toString();
			if(null != ((Map.Entry)map).getValue()) {
				String v = ((Map.Entry)map).getValue().toString();
				this.setParameter(k, v);
			}
	    }
	}
	
}
