/**
 * Copyright (c) 2011-2015 by AllScore
 * All rights reserved.
 */
package com.third.yaku.utils.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 验签辅助类。参照支付宝提供的实现并加入对RSA验签的支持。
 * 
 * @author niuyt
 * 
 */
public class SignatureGenerator
{
	/**
	 * 根据传入的参数，生成对应的验签串。
	 * 
	 * @param params
	 * @param signType
	 * @param privateKey
	 * @param inputCharset
	 * @return
	 */
	public static String genSignStr(Map<String, String> params, String signType, String privateKey, String inputCharset)
	{
		Properties properties = new Properties();
		for (Iterator<String> iter = params.keySet().iterator(); iter.hasNext();)
		{
			String name = iter.next();
			if (name == null || name.equalsIgnoreCase("sign") || name.equalsIgnoreCase("signType"))
			{
				continue;
			}
			String value = params.get(name);
			properties.setProperty(name, value);
		}
		String content = getSignatureContent(properties);
		return GenericSignTools.genSignStr(content, privateKey, inputCharset, signType);
	}

	public static String getSignatureContent(Properties properties)
	{
		StringBuffer content = new StringBuffer();
		List keys = new ArrayList(properties.keySet());
		Collections.sort(keys);

		for (int i = 0; i < keys.size(); i++)
		{
			String key = (String) keys.get(i);
			String value = properties.getProperty(key);

			content.append((i == 0 ? "" : "&") + key + "=" + value);
		}

		return content.toString();
	}

}
