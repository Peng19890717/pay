package com.third.ld;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Plat2Mer_v40 {
	private static final Log log = LogFactory.getLog(Plat2Mer_v40.class);

	/**
	 * 
	 * <br>description : 商户请求平台后，解析平台响应给商户的html格式的数据，并验证平台签名
	 * @param html
	 * @return
	 * @throws RetDataException
	 * @version     1.0
	 * @date        2014-7-24下午08:15:56
	 */
	public static Map getResData(String html) throws Exception{
		Map data = new HashMap();
		try{
			data = getData(html);
		}catch(Exception e){
			throw new Exception("解析后台平台响应数据出错",e);
		}
		return data;
	}
	
	/**
	 * 
	 * <br>description : 商户请求平台后，解析平台响应给商户的数据流格式的数据，并验证平台签名
	 * @param in
	 * @return
	 * @throws RetDataException
	 * @version     1.0
	 * @date        2014-7-24下午08:16:07
	 */
	public static Map getResData(InputStream in) throws Exception{
		Map data = new HashMap();
		try{
			data = getDataByStream(in);
		}catch(Exception e){
			throw new Exception("解析后台平台响应数据出错",e);
		}
		return data;
	}
	
	/**
	 * 
	 * <br>description : 商户请求平台后，解析平台响应给商户的meta格式的数据，并验证平台签名
	 * @param meta
	 * @return
	 * @throws RetDataException
	 * @version     1.0
	 * @date        2014-7-24下午08:16:16
	 */
	public static Map getResDataByMeta(String meta) throws Exception{
		Map data = new HashMap();
		try{
			data = getDataByContent(meta);
		}catch(Exception e){
			throw new Exception("解析后台平台响应数据出错",e);
		}
		return data;
	}
	
	/**
	 * 
	 * <br>description : 解析平台主动通知给商户的数据并验签
	 * @param obj
	 * @return
	 * @throws VerifyException
	 * @version     1.0
	 * @date        2014-7-24下午08:16:45
	 */
	public static Map getPlatNotifyData(Object obj) throws Exception{
		Map data = DataUtil.getData(obj);
		log.debug("支付结果通知请求数据为:" + data);
		if(data==null || data.size()==0){
			throw new Exception("待解析的数据对象为空");
		}
		String sign = data.get("sign").toString();
		Map retMap = PlainUtil.notifyPlain(obj,false);
		String plain = retMap.get("plain").toString();
		
		boolean checked = SignUtil.verify(sign, plain);
		if(!checked){
			throw new Exception("平台数据验签失败");
		}
		return data;
	}
	
	private static Map getData(String html) throws Exception{
		if(StringUtil.isEmpty(html)){
			throw new RuntimeException("请传入需解析的HTML");
		}
		String content = HttpMerParserUtil.getMeta(html);
		return getDataByContent(content);
	}
	
	private static Map getDataByStream(InputStream in) throws Exception{
		String html = HttpMerParserUtil.getHtml(in);
		log.info("根据流获取到的HTML为：" + html);
		String content = HttpMerParserUtil.getMeta(html);
		log.info("根据HTML获取到的meta内容为：" + content);
		return getDataByContent(content);
	}
	
	public static Map getDataByContent(String content) throws Exception{
		String plain = "",sign = "";
		Map map = new HashMap();
		try{
			map = PlainUtil.getResPlain(content);
		}catch(Exception e){
			log.info("请求数据分解发生异常" + e);
		}
		return map;
	}
}
