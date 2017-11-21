package com.third.tfb.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.PayConstant;

public class RequestUtil {
	// 签名
	private static String sign;

	// 服务端返回的数据
	private static String responseData;

	/**
	 * 发起请求
	 * 
	 * @param url
	 * @param param
	 *            参数列表（按首字母进行排序）
	 */
	public static String  sendRequst(final String url, String param) {
		try {
			String cipherData = encrypt(param.toString());
			//log.info("天付宝代付请求报文:"+cipherData);
			responseData = doPost(url, "cipher_data=" + URLEncoder.encode(cipherData));
			responseData=new String(responseData.getBytes(),"utf-8");
			//log.info("天付宝代付响应的报文"+responseData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseData;
	}
	public static String querySign(TreeMap<String,String> param){
		StringBuffer paramstr =new StringBuffer();
		for (String pkey:param.keySet()){
			String pvalue=param.get(pkey);
			if(null != pvalue && "" != pvalue){
				paramstr.append(pkey+"="+pvalue+"&");
			}
		}
		String paramSrc=paramstr.substring(0, paramstr.length()-1);
		sign=sign(paramSrc);
		paramstr.append("sign=" + sign);
	
		return paramstr.toString();
	}

	/**
	 * 验签
	 * 
	 * @param source
	 *            签名内容
	 * @param sign
	 *            签名值
	 * @return
	 */
	public static boolean verify(String source, String sign) {
		//MD5验签，把返回的报文串（去掉sign和空串），作MD5加签，然后跟sign 比对
		return (sign(source).equals(sign)) ? true:false;
		
	}

	/**
	 * 对原串进行签名
	 * 
	 * @param paramSrc
	 *            the source to be signed
	 * @return
	 */
	public static String sign(String paramSrc) {
		StringBuffer strbuff = new StringBuffer();
		strbuff.append(paramSrc + "&key=" + RSAUtil.MD5_KEY);
		String sign = null;
		try {
			sign = RSAUtil.getMD5Str(strbuff.toString());
			System.out.println("签名:" + sign);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return sign;
	}

	/**
	 * 加密得到cipherData
	 * 
	 * @param paramstr
	 * @return
	 */
	public static String encrypt(String paramstr) {
		String publickey = PayConstant.PAY_CONFIG.get("TFB_WG_RSA_PUBLIC_KEY");
		String cipherData = null;
		try {
			cipherData = RSAUtil.encryptByPublicKey(paramstr.getBytes("UTF-8"), publickey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cipherData;
	}

	/**
	 * rsa解密
	 * 
	 * @param cipherData
	 *            the data to be decrypt
	 * @return
	 */
	public static String decryptResponseData(String cipherData) {
		String privatekey = PayConstant.PAY_CONFIG.get("TFB_WG_RSA_PRIVATE_KEY");
		String result;
		try {
			result = RSAUtil.decryptByPrivateKey(Base64.decode(cipherData), privatekey);
			//result = new String(result.getBytes("UTF-8"), "UTF-8");
			System.out.println("解密结果:" + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String,String> stringToMap(String strData)throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		if(strData!=null&&strData.length()>0){
			String[] strDatas = strData.split("&");
			for(int i=0;i<strDatas.length;i++){
				String[] strdatas_temp= strDatas[i].split("=");
				map.put(strdatas_temp[0],strdatas_temp[1] );
			}
		}else new Exception("转换字符为空");
		return map;
	}
	public static String parseXml(String responseData,String nodeName) {
		String cipher_data = null;
		try {
			Document dom = DocumentHelper.parseText(responseData);
			Element root = dom.getRootElement();
			cipher_data = root.element(nodeName).getText();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		return cipher_data;
	}

	public static String doPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲    
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.out.println("请求结果:" + result);
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
