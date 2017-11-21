package com.third.reapal;

import java.util.Map;

import util.DataTransUtil;
import util.PayUtil;

import com.PayConstant;
import com.alibaba.fastjson.JSON;
/* *
 *类名：ReapalSubmit
 *功能：融宝各接口请求提交类
 *详细：构造融宝各接口发送请求数据，获取远程HTTP数据
 *版本：3.1.2
 *日期：2015-08-15
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究融宝接口使用，只是提供一个参考。
 */

public class ReapalSubmit {
	/**
	 * 发送结果
	 * @param para
	 * @param merchant_id
	 * @param url
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String buildSubmit(Map<String, String> para,String merchant_id, String url,String key) throws Exception {
        //生成签名
		String mysign = Md5Utils.BuildMysign(para,key);
        para.put("sign", mysign);
        //将map转化为json 串
		String json = JSON.toJSONString(para);
		//加密数据
		Map<String, String> maps = Decipher.encryptData(json);
		maps.put("merchant_id", merchant_id);
		maps.put("sign_type",PayConstant.PAY_CONFIG.get("rb_sign_type"));
		//发送请求 并接收
		String post = new String(new DataTransUtil().doPost(url,PayUtil.createWebUrl(maps).getBytes()),"utf-8");
		return post;
	}
	
	
}
