package com.third.mobao;

import java.util.Map;

public class EncodeUtil {
	/****
	 * 将字符串转换为加密的串
	 * @param transMap
	 * @return
	 */
	public  static String getUrlStr(Map<String,String> transMap){
		//组织需要加密的字符串
		String transStr="";
		int flag=0;
		for(String key:transMap.keySet()) {
			System.out.println(key+":"+transMap.get(key));
			if((transMap.size()-1)==flag) transStr=transStr+key+"="+transMap.get(key);
			else transStr=transStr+key+"="+transMap.get(key)+"&";
			flag++;
		} 
		return 	transStr;
	}
}
