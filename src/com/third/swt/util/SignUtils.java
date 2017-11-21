package com.third.swt.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SignUtils {

	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		StringBuffer sbuf = new StringBuffer("");
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
            if(i==0){
			   sbuf.append(key).append("=").append(value);
            }else{
               sbuf.append("&").append(key).append("=").append(value);
            }
		}
		return sbuf.toString();
	}

}
