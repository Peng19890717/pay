package com.third.ld;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PlainUtil {
	
	/**
	 * 
	 * <br>description : 计算商户签名串，并校验商户请求数据
	 * @param obj 商户请求数据
	 * @return 明文串和加密串Map
	 * @throws ParameterCheckException
	 * @version     1.0
	 * @date        2014-7-25下午01:36:05
	 */
	public static Map getPlain(Object obj) throws Exception{
		Map map = DataUtil.getData(obj);
		Map returnMap = new HashMap();
		if(validate(map)){
			returnMap.putAll(getPlanAndCheckRule(map));
		}else{
			throw new Exception("请求信息" + map + "在进行必填字段校验发生异常");
		}
		return returnMap;
	}
	
	/**
	 * <br>description :  根据商户请求数据计算商户签名串，不校验请求数据
	 * @param 商户请求数据
	 * @return 明文串和加密串Map
	 * @throws ParameterCheckException
	 */
	public static Map getPlainNocheck(Object obj) throws Exception{
		Map map = DataUtil.getData(obj);
		Map returnMap = new HashMap();
		returnMap.putAll(getPlanNoCheck(map));
		return returnMap;
	}
	
	/**
	 * 
	 * <br>description : 1平台通知商户后，获取商户响应给平台的数据
	 *                   2解析平台主动通知给商户的数据并验签
	 * @param obj
	 * @param b
	 * @return
	 * @throws ParameterCheckException
	 * @version     1.0
	 * @date        2014-8-1下午01:30:29
	 */
	public static Map notifyPlain(Object obj, boolean b)
			throws Exception {
		Map map = new HashMap();
		Map reqMap = DataUtil.getData(obj);

		if (b) {//平台通知商户后，获取商户响应给平台的数据
			map.putAll(conMeta(reqMap));
		} else {//解析平台主动通知给商户的数据并验签
			map.putAll(merNotifyPlain(reqMap));
		}
		return map;
	}
	
	/**
	 * 
	 * <br>description : 获取响应给平台的明文
	 * @param res
	 * @return
	 * @throws ParameterCheckException
	 * @version     1.0
	 * @date        2014-8-1下午01:32:12
	 */
	public static Map getResPlain(String res) throws Exception{
		String[] str = null;
		Map map = new HashMap();
		try{
			str = res.split("&");
		    Arrays.sort(str);
		    map.putAll(resPlain(str));
		}catch(Exception e){
			throw new Exception("平台返回信息" + res + "解析异常");
		}
		return map;
	}
	
	private static boolean validate(Map map) throws Exception{
		//判断商户请求的参数中是否包含service字段，以便根据service字段对应的值获取配置文件中的请求必须字段值
		if(!map.containsKey("service")){                           
			throw new Exception("请求字段中不包含service字段");
		  }
		String service = map.get("service").toString();
		Object object = ServiceMapUtil.getServiceRule().get(service);
		if(object == null){
			return true;
		}
		String rule = object.toString();
		String[] str = rule.split(",");
		String key = null;
		Object value = null;
		for(int i = 0 ; i < str.length; i++){
			key = str[i];
			value = map.get(key);
			if(value == null || "".equals(value.toString())){
				String message = new StringBuffer().append("service=").append(map.get("service").toString())
								.append("请求中").append(key).append("字段不能为空").toString();
				throw new Exception(message);
			}
		}
		return true;
	}
	private static Map resPlain(String[] str) throws Exception{
		Map retMap = new HashMap();
		StringBuffer plainBuffer = new StringBuffer();
		Map map = new HashMap();
		for(int i = 0 ; i < str.length; i++){
			if(!str[i].startsWith("sign")){
				plainBuffer.append(str[i]).append("&");
			}
			map = split(str[i],2);
			retMap.putAll(map);
		}
		String plain = plainBuffer.substring(0,plainBuffer.length()-1).toString();
		retMap.put("plain", plain);
		return retMap;
	}
	private static Map split(String s ,int limit) throws Exception{
		Map map = new HashMap();
		String[] str = null;
		try{
			str = s.split("=",limit);
		}catch(Exception e){
		}if(str.length == 1){
			map.put(str[0], "");
		}else if(str.length == 2){
			map.put(str[0], str[1]);
		}else{
			throw new Exception("分解平台返回信息" + s + "发生异常");
		}
		return map;
	}
	/**该函数用于获取平台通知商户数据的明文串*/
	private static Map merNotifyPlain(Map map)throws Exception{
		Object[] obj = map.keySet().toArray();
		Arrays.sort(obj);
		String plain="",str="",value = "";
		StringBuffer signString = new StringBuffer();
		for(int i = 0 ; i < obj.length ; i++){
			str = obj[i].toString();
			value = map.get(str).toString();
			if(!str.startsWith("sign")){
				signString.append(str).append("=").append(value).append("&");
			}
		}
		plain = signString.subSequence(0, signString.length()-1).toString();
		Map returnMap = new HashMap();
		returnMap.put("plain", plain);
		return returnMap;
	}
	/**
	 * 函数用于商户通知平台，接收到的交易结果通知情况，拼凑明文串和密文串，明文串不进行编码
	 * @param map
	 * @return
	 * @throws ParameterCheckException
	 */
	private static Map conMeta(Map map) throws Exception{
		Object[] obj = map.keySet().toArray();
		Object object = null;
		Map ruleMap = ServiceMapUtil.getReqRule();
		Arrays.sort(obj);
		String plain,sign,str,value= null;
		StringBuffer plainString = new StringBuffer();
		StringBuffer signString = new StringBuffer();
		ReqRule rq = null;
		try{
			for(int i = 0 ; i < obj.length ; i++){
				str = obj[i].toString();
				value = map.get(str).toString();
				object = ruleMap.get(str);
				if(object == null){
					plainString.append(str).append("=").append(value).append("&");
					signString.append(str).append("=").append(value).append("&");
					continue;
				}
				rq = (ReqRule)object;
				if((!"".equals(rq.getRegex()))&&!(Pattern.matches(rq.getRegex(),value))){
					throw new Exception("请求字段" + str + "在进行规则校验发生异常");
				}else if(rq.getLength()>0 && value.length() > rq.getLength()){
					throw new Exception("请求字段" + str + "在进行规则校验发生异常");
				}else{
					if("sign_type".equals(str)){
						plainString.append(str).append("=").append(value).append("&");
					}else{
						plainString.append(str).append("=").append(value).append("&");
						signString.append(str).append("=").append(value).append("&");
					}
				}
			}
			plain = plainString.subSequence(0, plainString.length()-1).toString();
			sign = signString.substring(0,signString.length()-1).toString();
			sign = SignUtil.sign(sign,StringUtil.trim(map.get("mer_id")));
		}catch(Exception e){
			throw new Exception("组织商户返回平台数据发生异常" + e);
		}
		Map returnMap = new HashMap();
		returnMap.put("plain", plain);
		returnMap.put("sign", sign);
		return returnMap;
	}
	/**
	 * 
	 * @param 得到签名，并且做字段校验
	 * @return
	 * @throws ParameterCheckException
	 */
	private static Map getPlanAndCheckRule(Map map) throws Exception{
		Object[] obj = map.keySet().toArray();
		Object object = null;
		Map ruleMap = ServiceMapUtil.getReqRule();
		Arrays.sort(obj);
		String plain,sign,str,value,valueEncoder= null;
		StringBuffer plainString = new StringBuffer();
		StringBuffer signString = new StringBuffer();
		ReqRule rq = null;
		try{
			for(int i = 0 ; i < obj.length ; i++){
				str = obj[i].toString();
				value = map.get(str).toString().trim();
				object = ruleMap.get(str);
				if(object == null){
					plainString.append(str).append("=").append(value).append("&");
					signString.append(str).append("=").append(value).append("&");
					continue;
				}
				rq = (ReqRule)object;
				if((!"".equals(value.trim())&&!"".equals(rq.getRegex()))&&!(Pattern.matches(rq.getRegex(),value))){
					throw new Exception("请求字段" + str + "在进行规则校验发生异常");
				}else if(rq.getLength()>0 && value.length() > rq.getLength()){
					throw new Exception("请求字段" + str + "在进行规则校验发生异常");
				}else{
					if("sign_type".equals(str)){
						if(rq.isEncode()){
							value = URLEncoder.encode(value,"UTF-8");
						}
						plainString.append(str).append("=").append(value).append("&");
					}else{
						if(rq.isEncode()){
							valueEncoder = URLEncoder.encode(value,"UTF-8");
						}else
							valueEncoder = value;
						plainString.append(str).append("=").append(valueEncoder).append("&");
						signString.append(str).append("=").append(value).append("&");
					}
				}
			}
			plain = plainString.subSequence(0, plainString.length()-1).toString();
			sign = signString.substring(0,signString.length()-1).toString();
			sign = SignUtil.sign(sign,StringUtil.trim(map.get("mer_id")));
		}catch(Exception e){
			throw new Exception("请求字段在进行规则校验发生异常"+e.getMessage());
		}
		Map returnMap = new HashMap();
		returnMap.put("plain", plain);
		returnMap.put("sign", sign);
		return returnMap;
	}
	
	/**
	 * 
	 * <br>description : 得到签名,不校验字段
	 * @param map
	 * @return
	 * @throws ParameterCheckException
	 * @version     1.0
	 * @date        2014-7-25上午11:38:14
	 */
	private static Map getPlanNoCheck(Map map) throws Exception{
		Object[] obj = map.keySet().toArray();
		Arrays.sort(obj);
		String plain,sign,str,value,valueEncoder= null;
		StringBuffer plainString = new StringBuffer();
		StringBuffer signString = new StringBuffer();
		try{
			for(int i = 0 ; i < obj.length ; i++){
				str = obj[i].toString();
				value = StringUtil.trim(map.get(str).toString());
				if(StringUtil.isEmpty(value)){
					continue;
				}
				if("sign_type".equals(str) || "signType".equals(str)){
					value=URLEncoder.encode(value,"UTF-8");
					plainString.append(str).append("=").append(value).append("&");
				}else{
					valueEncoder =URLEncoder.encode(value,"UTF-8");
					plainString.append(str).append("=").append(valueEncoder).append("&");
					signString.append(str).append("=").append(value).append("&");
				}
			}
			plain = plainString.subSequence(0, plainString.length()-1).toString();
			sign = signString.substring(0,signString.length()-1).toString();
			String merId = StringUtil.isNotEmpty(StringUtil.trim(map.get("mer_id")))?StringUtil.trim(map.get("mer_id")):StringUtil.trim(map.get("merId"));
			sign = SignUtil.sign(sign,StringUtil.trim(merId));
		}catch(Exception e){
			throw new Exception("请求字段在进行规则校验发生异常"+e.getMessage());
		}
		Map returnMap = new HashMap();
		returnMap.put("plain", plain);
		returnMap.put("sign", sign);
		return returnMap;
	}
}
