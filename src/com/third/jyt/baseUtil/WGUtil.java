package com.third.jyt.baseUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




public class WGUtil {
	private static final Log log = LogFactory.getLog(WGUtil.class);
	/**
	 * 转换请求参数为MAP
	 * @param request
	 * @return
	 */
	public static Map<String, String> getRequestMap(HttpServletRequest request) {
		Map<String, String[]> reqParams = request.getParameterMap();

		Map<String, String> paramMap = new HashMap<String, String>(); // 请求参数map

		Iterator<Entry<String, String[]>> it = reqParams.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String[]> ent = it.next();

			if (ent.getValue().length == 1) {
				paramMap.put(ent.getKey(), ent.getValue()[0]);
			} else {
				log.error("获取参数" + ent.getKey() + "的值异常。ent.getValue().length="	+ ent.getValue().length);
			}
		}
		return paramMap;
	}
	
	/**
	 * 签名类
	 * @param map
	 * @param key
	 * @return
	 */
    public static String getSign(Map<String,String> reqMap,String key){
    	Map<String, String> maps = sortMapByKey(reqMap);
		StringBuffer reqTex = new StringBuffer();
		Iterator<Entry<String, String>> it = maps.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> ent = it.next();
			if ("key".equals(ent.getKey())||"sign".equals(ent.getKey())){
				continue;
			}
			if (StringUtil.isNotBlank(ent.getValue()))
				reqTex.append(ent.getKey()).append("=").append(ent.getValue())
						.append("&");
		}
		reqTex.deleteCharAt(reqTex.length() - 1);
		//System.out.println("原串：" + reqTex.toString());
		String sign = DigestUtils.sha256Hex(reqTex.toString() + key);
        return sign;
    }
    /**
	 * 使用 Map按key进行排序
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, String> sortMap = new TreeMap<String, String>(
				new MapKeyComparator());
		sortMap.putAll(map);

		return sortMap;
	}

	// 比较器类
	private static class MapKeyComparator implements Comparator<String> {
		public int compare(String str1, String str2) {
			return str1.compareTo(str2);
		}
	}
   
    /** 
	   * 字符串 SHA 加密 
	   *  
	   * @param strSourceText 
	   * @return 
	   */  
	  public static String SHA256String(final String strText)  {  
	    // 返回值  
	    String strResult = null;  
	  
	    // 是否是有效字符串  
	    if (strText != null && strText.length() > 0)  
	    {  
	      try  
	      {  
	        // SHA 加密开始  
	        // 创建加密对象 并傳入加密類型  
	        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");  
	        // 传入要加密的字符串  
	        messageDigest.update(strText.getBytes());  
	        // 得到 byte 類型结果  
	        byte byteBuffer[] = messageDigest.digest();  
	  
	        // 將 byte 轉換爲 string  
	        StringBuffer strHexString = new StringBuffer();  
	        // 遍歷 byte buffer  
	        for (int i = 0; i < byteBuffer.length; i++)  
	        {  
	          String hex = Integer.toHexString(0xff & byteBuffer[i]);  
	          if (hex.length() == 1)  
	          {  
	            strHexString.append('0');  
	          }  
	          strHexString.append(hex);  
	        }  
	        // 得到返回結果  
	        strResult = strHexString.toString();  
	      }  
	      catch (NoSuchAlgorithmException e)  
	      {  
	        e.printStackTrace();  
	      }  
	    }  
	    return strResult;  
	  }  

}
