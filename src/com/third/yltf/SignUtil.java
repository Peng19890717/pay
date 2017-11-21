package com.third.yltf;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SignUtil {
	/**
	 * 签名
	 * @param params
	 * @param signKey
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static String generateMD5(Map<String,String> params, String signKey) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		
		if(signKey==null || "".equals(signKey) || "null".equals(signKey) || params==null || params.isEmpty()){
			return "";
		}
		List<String> sortList = new ArrayList<String>();
		for(Map.Entry<String,String> entry : params.entrySet()){
			String key = entry.getKey();
			String val = entry.getValue();
			if(val==null || "".equals(val)){
				continue;
			}
			sortList.add(key + "=" + val);
		}
		Collections.sort(sortList);
		sortList.add(signKey);
		
		String str = "";
		for (int i = 0; i < sortList.size()-1; i++) {
			String element = sortList.get(i);
			String[] para = element.split("=");
			String key = para[0];
			String value = para[1];
			str += key+"="+value+"&";
		}
		str += sortList.get(sortList.size()-1);//key=value&key2=value2...&32529
		MessageDigest m = MessageDigest.getInstance("md5");
		m.update(str.getBytes("utf-8"), 0, str.getBytes("utf-8").length);
		byte[] md5 =  m.digest();
		return StringUtil.hex(md5);
	}
}
