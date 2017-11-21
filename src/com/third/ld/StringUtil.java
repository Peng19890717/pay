package com.third.ld;

public class StringUtil {

	/**
	 * 将对象数据转换为String，并去除首尾空格
	 * @param obj
	 * @return
	 */
	public static String trim(Object obj){
		if(null == obj)return "";
		else return obj.toString().trim();
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		return null==str || "".equals(str.trim());
	}
	
	/**
	 * 判断字符串是否不为空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
}
