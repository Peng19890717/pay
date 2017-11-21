package com.third.syx.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AllscoreSubmit {

	/**
	 * 生成要请求给奥斯卡的参数数组
	 * @param sParaTemp 请求前的参数数组
	 * @return 要请求的参数数组
	 */
	public static Map<String, String> buildRequestParaRSA(Map<String, String> sParaTemp) {
		Map<String, String> sPara =sParaTemp;
		//除去数组中的空值和签名参数
		Map<String, String> sParaForSign = AllscoreCore.paraFilter(sParaTemp);
		//生成签名结果
		String mysign = AllscoreCore.buildMysignRSA(sParaForSign,null);
		//签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);
		return sPara;
	}    

	/**
	 * 生成要请求给奥斯卡的参数数组
	 * @param sParaTemp 请求前的参数数组 
	 * @return 要请求的参数数组
	 */
	public static Map<String, String> buildRequestParaMD5(Map<String, String> sParaTemp) {
		Map<String, String> sPara =sParaTemp;
		//除去数组中的空值和签名参数
		Map<String, String> sParaForSign = AllscoreCore.paraFilter(sParaTemp);
		//生成签名结果
		String mysign ="";
		try {
//			mysign = Md5SignUtils.generateSign(sParaForSign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		//签名结果与签名方式加入请求提交参数组中
		sPara.put("sign", mysign);

		return sPara;
	} 


	/**
	 * 构造提交表单HTML数据
	 * @param sParaTemp 请求参数数组
	 * @param gateway 网关地址
	 * @param strMethod 提交方式。两个值可选：post、get
	 * @param strButtonName 确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	public static String buildFormRSA(Map<String, String> sParaTemp, String gateway, String strMethod,
			String strButtonName) {
		//待请求参数数组
		Map<String, String> sPara = buildRequestParaRSA(sParaTemp);
		StringBuffer sbHtml = sbHtml(gateway, strMethod, strButtonName, sPara);
		return sbHtml.toString();
	}




	/**
	 * 构造提交表单HTML数据
	 * @param sParaTemp 请求参数数组
	 * @param gateway 网关地址
	 * @param strMethod 提交方式。两个值可选：post、get
	 * @param strButtonName 确认按钮显示文字
	 * @return 提交表单HTML文本
	 */
	public static String buildFormCA(Map<String, String> sParaTemp, String gateway, String strMethod,
			String strButtonName) {
		//待请求参数数组
		Map<String, String> sPara = null;
	 if("RSA".equalsIgnoreCase(sParaTemp.get("signType"))){
			sPara = buildRequestParaRSA(sParaTemp);
		}else if("MD5".equalsIgnoreCase(sParaTemp.get("signType"))){
			sPara = buildRequestParaMD5(sParaTemp);
		}
		StringBuffer sbHtml = sbHtml(gateway, strMethod, strButtonName, sPara);
		return sbHtml.toString();
	}
	public static Map<String,String> buildFormCAMap(Map<String, String> sParaTemp/*, String gateway, String strMethod,String strButtonName*/) {
		//待请求参数数组
		Map<String, String> sPara = null;
            if("RSA".equalsIgnoreCase(sParaTemp.get("signType"))){
			sPara = buildRequestParaRSA(sParaTemp);
		}else if("MD5".equalsIgnoreCase(sParaTemp.get("signType"))){
			sPara = buildRequestParaMD5(sParaTemp);
		}
		/*StringBuffer sbHtml = sbHtml(gateway, strMethod, strButtonName, sPara);*/
		return sPara;
	}
	public static StringBuffer sbHtml(String gatewayUrl, String strMethod, String strButtonName,
			Map<String, String> sPara) {
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();

		sbHtml.append("<form id=\"allscoresubmit\" name=\"allscoresubmit\" action=\"" + gatewayUrl
				+ "\" method=\"" + strMethod
				+ "\">");

		for (int i = 0; i < keys.size(); i++) {
			String name = keys.get(i);
			String value = sPara.get(name);

			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		//submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['allscoresubmit'].submit();</script>");
		System.out.println("数据表单："+sbHtml);
		return sbHtml;
	}    
}
