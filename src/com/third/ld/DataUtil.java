package com.third.ld;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class DataUtil {

	public static Map<String,String> getData(Object obj) throws Exception{
		if(obj==null){
			throw new RuntimeException("请求对象为NULL");
		}
		if(obj instanceof Map) {
			return (Map) obj;
		}else if(obj instanceof HttpServletRequest){
			HttpServletRequest request = (HttpServletRequest) obj;
			Map fieldMap = new HashMap();
			Enumeration names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				String values = request.getParameter(name);
				if(null!=values)values = values.trim();
				fieldMap.put(name, values);
			}
			return fieldMap;
		}else{
			throw new Exception("数据集合只支持java.util.Map 和 javax.servlet.http.HttpServletRequest");
		}
	}
}
