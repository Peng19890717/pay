package com.third.yaku.utils.security;
/*package com.wanjx.back.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wanjx.back.util.RSASignUtil;

public class CertifySecurityBean
{
	*//**
	 * Logger for this class
	 *//*
	private static final Logger logger = Logger.getLogger(CertifySecurityBean.class);

	*//**
	 * 信息加签
	 * 
	 * @param params
	 * @param privateKey
	 * @return
	 *//*
	public static String getSign(Map<String, String> params, final String privateKey)
	{
		logger.info("getSign(Map<String,String>, String) - start"); //$NON-NLS-1$
		logger.info("params is:" + params.get("data")); //$NON-NLS-1$
		logger.info("privateKey is:" + privateKey); //$NON-NLS-1$
		try
		{
			String signs = getContent(params, privateKey);
			logger.info("getSign content is:" + signs);
			// 私钥数字签名
			String sign = RSASignUtil.getSign(params, privateKey, "UTF-8");
			logger.info("getSign(Map<String,String>, String) - end ! result suc !!!"); //$NON-NLS-1$
			return sign;
		} catch (Exception e)
		{
			logger.error("getSign(Map<String,String>, String)", e); //$NON-NLS-1$
			e.printStackTrace();
		}
		logger.info("getSign(Map<String,String>, String) - end ! result err !!!"); //$NON-NLS-1$
		return null;
	}

	public static String verifySign(final String respParam, final String privateKey)
	{
		logger.info("verifySign(String, String) - start"); //$NON-NLS-1$
		try
		{
			// 私钥解密
			byte[] decodedData = RSAUtils.decryptByPrivateKey(Base64Utils.decode(respParam), privateKey);
			// 解密出来的字符串，以|隔开的
			String target = new String(decodedData);
			logger.info("verifySign target:" + target); //$NON-NLS-1$
			logger.info("verifySign(String, String) - end  ! result suc !!!"); //$NON-NLS-1$
			return target;
		} catch (Exception e)
		{
			logger.error("verifySign(String, String)", e); //$NON-NLS-1$
			e.printStackTrace();

			// throw new CertifyChnException(ERetCode.RSA_SIGN_ERR.getCode(),
			// "");
		}
		logger.info("verifySign(String, String) - end  ! result err !!!"); //$NON-NLS-1$
		return null;
	}

	*//**
	 * 功能：将安全校验码和参数排序 参数集合
	 * 
	 * @param params
	 *            安全校验码
	 * @param privateKey
	 * *//*
	private static String getContent(Map<String, String> params, String privateKey)
	{
		logger.info("getContent(Map<String,String>, String) - start"); //$NON-NLS-1$
		List<String> keys = new ArrayList<String>(params.keySet());
		String prestr = "";
		for (int i = 0; i < keys.size(); i++)
		{
			String key = (String) keys.get(i);
			String value = (String) params.get(key);

			if (i == keys.size() - 1)
			{
				prestr = prestr + value;
			} else
			{
				prestr = prestr + value + "&";
			}
		}
		logger.info("prestr is:" + prestr); //$NON-NLS-1$
		logger.info("getContent(Map<String,String>, String) - end"); //$NON-NLS-1$
		return prestr;
	}
}
*/