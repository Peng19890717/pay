package com.third.ld;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LDUtil {
	
	private static final Log log = LogFactory.getLog(LDUtil.class);
	public static ReqData getReqData(Object obj, String encryptParamters, String method, String url) throws Exception{
		//得到数据对象
		Map<String, String> map = DataUtil.getData(obj);
		Map<String, String> mapfield = new HashMap();
		Map<String, String> mp = new HashMap();
		mp.putAll(map);
		//需RSA加密字段
		doEncrypt(mp,encryptParamters);
		//获取签名
		Map returnMap = PlainUtil.getPlainNocheck(mp);
		String plain = returnMap.get("plain").toString();
		String sign = returnMap.get("sign").toString();
		//组织http请求数据
		ReqData data = new ReqData();
		if("get".equalsIgnoreCase(method)){
			try{
				sign = URLEncoder.encode(sign,"UTF-8");
			}catch(Exception e){
				throw new Exception("字符编码出现异常,请联系联动优势开发人员");
			}
			String param = plain + "&" + "sign=" + sign;
			log.info("param=" + param);
			data.setUrl(url + "?" + param);
			log.info("请求平台应访问的完整url为：" + data.getUrl());
			data.setPlain(plain);
			data.setSign(sign);
			return data;
		}else if("post".equalsIgnoreCase(method)){
			data.setUrl(url);
			mp.put("sign", sign);
			for(String key : mp.keySet()){
				if(StringUtil.isNotEmpty(StringUtil.trim(mp.get(key)))){
					mapfield.put(key, mp.get(key));
				}
			}
			data.setField(mapfield);
			data.setSign(sign);
			data.setPlain(plain);
			log.info("返回给商户的ReqData为"+data.toString());
			return data;
		}else{
			throw new RuntimeException("未能获得数据请求的方式:" + method);
		}
	}
	
	private static void doEncrypt(Map map, String encryptParamters) throws Exception {
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			Object ob = map.get(key);
			String value = null;
			if(ob!= null){
				value = ob.toString();
			}
			String[] params = encryptParamters.split(",");
			HashSet encryptId = new HashSet();
			for(String param:params){
				if(StringUtil.isNotEmpty(param)){
					encryptId.add(param);
				}
			}
			
			try{
				if(encryptId.contains(key)&& StringUtil.isNotEmpty(value)){
					value = CipherUtil.Encrypt(value);
					map.put(key,value);
				}
			}catch(Exception e){
				throw new Exception("公钥证书进行加密发生异常");
			}
		}
	}
	public static String merNotifyResData(Object obj)throws Exception{
		Map map = new HashMap();
		map.putAll(PlainUtil.notifyPlain(obj, true));
		String plain = map.get("plain").toString();
		String sign = map.get("sign").toString();
		return plain + "&sign=" + sign;
	}
}
