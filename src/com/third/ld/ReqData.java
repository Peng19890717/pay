package com.third.ld;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReqData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6931731592490427826L;
	
	/**请求url*/
	private String url;
	/**post数据*/
	private Map field = new HashMap();
	
	/**签名明文*/
	private String plain;
	/**签名密文*/
	private String sign;

	/**
	 * 构造方法
	 * @param url 请求url
	 */
	public ReqData(){
	}
	
	/**
	 * 构造方法
	 * @param url 请求url
	 */
	public ReqData(String url){
		this.url = url;
	}
	
	/**
	 * 构造方法
	 * @param url 请求url
	 * @param field 请求数据
	 */
	public ReqData(String url,Map field){
		this.url = url;
		this.field.putAll(field);
	}
	public String getUrl() {
		return url;
	}
	public Map getField() {
		return field;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setField(Map field) {
		this.field.putAll(field);
	}
	public void setField(String key,String value) {
		this.field.put(key, value);
	}
	public String getPlain() {
		return plain;
	}

	public void setPlain(String plain) {
		this.plain = plain;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	public String toString() {
		return "url=" + url + ",field=" + field+"plain=" + plain + "sign=" + sign;
	}
}
