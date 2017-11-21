package com.third.reapal;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.UUID;

import util.JWebConstant;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;

public class ReapalUtil {
	/**
	 * 获得一个UUID
	 * @return String UUID
	 */
	public static String getUUID(){
		String s = UUID.randomUUID().toString();
		//去掉“-”符号
		return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);
	}
//	public static ResourceBundle resourceBundle = ResourceBundle.getBundle("application");
//	public static String privateKey = "D:\\cert\\100000000000022.pfx";// 私钥
//	public static String password = "111111";// 密码
//	public static String key = resourceBundle.getString("user_key");// 用户key
//	public static String merchant_id = resourceBundle.getString("merchant_ID");// 商户ID
//	public static String pubKeyUrl = "D:\\cert\\rong.cer";// 公钥
//	public static String url = resourceBundle.getString("url");
//	public static String notify_url = resourceBundle.getString("notify_url");// 回调地址
//	public static String version = resourceBundle.getString("version");// 版本
	public static String charset = "UTF-8";// 编码
//	public static String sign_type = resourceBundle.getString("sign_type");// 签名方式，暂时仅支持MD5
	/**
     * 参数加密
     * @return
     */
    public static Map<String, String> addkey(String json)throws Exception{
        System.out.println("数据=============>" + json);
        //商户获取融宝公钥
        PublicKey pubKeyFromCrt = RSA.getPubKeyFromCRT(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
				+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("rb_public_cert"));
        //随机生成16数字
        String key = RandomUtil.getRandom(16);

        System.out.println("key=============>" + key);
        //对随机数进行加密
        String encryptkey = RSA.encrypt(key, pubKeyFromCrt);
        String encryData = AES.encryptToBase64(json, key);

        System.out.println("密文key============>" + encryptkey);
        System.out.println("密文数据===========>" + encryData);

        Map<String, String> map = new HashMap<String, String>();
        map.put("data", encryData);
		map.put("encryptkey", encryptkey);

        return map;
    }
    /**
     * 解密
     * @param post
     * @return
     * @throws Exception
     */
    public static String pubkey(String post)throws Exception{
        System.out.println("密文================>" + post);
        // 将返回的json串转换为map
        TreeMap<String, String> map = JSON.parseObject(post,
                new TypeReference<TreeMap<String, String>>() {
                });
        String encryptkey = map.get("encryptkey");
        String data = map.get("data");
        //获取自己私钥解密
        PrivateKey pvkformPfx = RSA.getPvkformPfx(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
				+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("rb_merchant_cert"),
				PayConstant.PAY_CONFIG.get("rb_merchant_cert_pwd"));
        String decryptData = RSA.decrypt(encryptkey, pvkformPfx);
        post = AES.decryptFromBase64(data, decryptData);
        System.out.println("明文================>" + post);
        return post;
    }
    /**
     * 解密
     * @param post
     * @return
     * @throws Exception
     */
    public static String pubkey(String encryptkey,String data)throws Exception{
    	String post;
    	System.out.println("encryptkey================>" + encryptkey);
    	System.out.println("密文================>" + data);
        //获取自己私钥解密
        PrivateKey pvkformPfx = RSA.getPvkformPfx(JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"
				+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("rb_merchant_cert"),
				PayConstant.PAY_CONFIG.get("rb_merchant_cert_pwd"));
        String decryptData = RSA.decrypt(encryptkey, pvkformPfx);
        post = AES.decryptFromBase64(data, decryptData);
        System.out.println("明文================>" + post);
        return post;
    }
    
    public static String getMapOrderStr(Map<String,Object> request){
        List<String> fieldNames = new ArrayList<String>(request.keySet());
        Collections.sort(fieldNames);
        StringBuffer buf = new StringBuffer();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = String.valueOf(request.get(fieldName));
            if ((fieldValue != null) && (fieldValue.length() > 0)){
                buf.append(fieldName+"="+fieldValue+"&");
            }
        }
        if(buf.length()>1) buf.setLength(buf.length()-1);
        return buf.toString(); //去掉最后&

    }



    /**
     * 生成订单号
     * @return
     */
    public static String no(){
        String code = "10" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "01" ;
        return code;
    }

    /**
     *
     * @param sArray
     * @param key
     * @return
     */
    public static String BuildMysign(Map sArray, String key) {
        if(sArray!=null && sArray.size()>0){
            StringBuilder prestr = CreateLinkString(sArray);
            System.out.println("prestr====>" + prestr);
            //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            return Md5Encrypt.md5(prestr.toString()+key,charset);
        }
        return null;
    }

    /**
     * 拼装参数
     * @param params
     * @return
     */
    public static StringBuilder CreateLinkString(Map params){
        List keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        StringBuilder prestr = new StringBuilder();
        String key="";
        String value="";
        for (int i = 0; i < keys.size(); i++) {
            key=(String) keys.get(i);
            value = (String) params.get(key);
            if("".equals(value) || value == null ||
                    key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")){
                continue;
            }
            prestr.append(key).append("=").append(value).append("&");
        }
        return prestr.deleteCharAt(prestr.length()-1);
    }
    
    public static void main(String[] arg0) throws Exception{
    	ReapalUtil ru = new ReapalUtil();
    	Map<String, String> map = ru.addkey("wo ai beijing tian an men");
    	String anwer = ru.pubkey(JSON.toJSONString(map));
    }
}
