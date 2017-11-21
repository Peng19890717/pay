package com.third.kb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import com.PayConstant;

public class Utils {
	/**
	 * 签名
	 * 
	 * @param beanMap
	 * @param configLoadMap
	 * @return
	 */
	public static Map<String, String> sign(Map<String, String> beanMap, Map<String, String> configLoadMap,Log log) {
		
		Map<String, String> map;
		try {
			RSASignUtil util = new RSASignUtil((String) configLoadMap.get("certPath"), PayConstant.PAY_CONFIG.get("KB_PRV_PASSWROD"));
			String sf = RSASignUtil.coverMap2String(beanMap);
			String merchantSign = util.sign(sf, "GBK");
			String merchantCert = util.getCertInfo();
			map = new HashMap<String, String>();
			map.putAll(beanMap);
			map.put("merchantSign", merchantSign);
			map.put("merchantCert", merchantCert);
		} catch (Exception e) {
			log.error("未找到文件");
			e.printStackTrace();
			return null;
		}
		return map;
	}
	/**
	 * 解析响应数据
	 * 
	 * @param res
	 *            响应数据
	 * @param configLoadMap
	 *            配置信息
	 * @return
	 */
	public static Map analysis(String res, Map configLoadMap, String service) {
		
		String verify1 = "version!charset!signType!status!returnCode!returnMessage!merchantId!orderId!tradeNo!codeUrl!codeImageUrl!tokenId!payInfo!appId!ch_ord_no";
		String verify2 = "charset!version!serverCert!serverSign!signType!status!returnCode!returnMessage!requestId!merchantId!branchMerchantId!orderId!codeUrl!codeImageUrl!tokenId!payInfo!paySts!ch_ord_no";
		String verify3 = "version!charset!signType!service!status!returnCode!returnMessage!tradeNo!merchantId!orderId!refundAmount!retMsg";
		String verify4 = "version!charset!signType!service!status!returnCode!returnMessage!tradeNo!merchantId!orderId!refundAmount!retMsg";
		String verify5 = "backParam!bankAbbr!charset!fee!merchantId!orderId!orderTime!payTime!purchaserId!returnCode!returnMessage!service!signType!status!totalAmount!tradeNo!version!ch_ord_no";
		String verify6 = "backParam!bankAbbr!charset!fee!merchantId!orderId!orderTime!payTime!purchaserId!returnCode!returnMessage!service!signType!status!totalAmount!requestId!version!serverCert!serverSign!merchantId!branchMerchantId!ch_ord_no";
		String verify7 = "charset!merchantId!orderId!refundAmount!returnCode!returnMessage!service!signType!status!tradeNo!version";
		String verify8 = "charset!serverCert!merchantId!orderId!refundAmount!serverSign!returnCode!branchMerchantId!returnMessage!service!signType!status!requestId!version";
		String verify9 = "acDate!backParam!bankAbbr!ch_ord_no!charset!fee!merchantId!orderId!orderTime!payTime!purchaserId!returnCode!returnMessage!serverCert!serverSign!service!signType!status!totalAmount!tradeNo!version";
		
		
		Map<String, String> retMap = new LinkedHashMap<String, String>();
		RSASignUtil util = new RSASignUtil((String) configLoadMap.get("certPath"), PayConstant.PAY_CONFIG.get("KB_PRV_PASSWROD"));
		String version = util.getValue(res, "version");
		if ("newGatePayment".equals(service)) {
			if ("1.0".equals(version)) {
				String sdkVerify1 = verify1;
				String[] split = sdkVerify1.split("!");
				for (int i = 0; i < split.length; i++) {
					retMap.put(split[i], (String) util.getValue(res, split[i]));
				}
			} else {
				String sdkVerify1 = verify2;
				String[] split = sdkVerify1.split("!");
				for (int i = 0; i < split.length; i++) {
					retMap.put(split[i], (String) util.getValue(res, split[i]));
				}
			}
		} else if ("OrderReverse".equals(service)) {
			if ("1.0".equals(version)) {
				String sdkVerify1 = verify3;
				String[] split = sdkVerify1.split("!");
				for (int i = 0; i < split.length; i++) {
					retMap.put(split[i], (String) util.getValue(res, split[i]));
				}
			} else {
				String sdkVerify1 = verify4;
				String[] split = sdkVerify1.split("!");
				for (int i = 0; i < split.length; i++) {
					retMap.put(split[i], (String) util.getValue(res, split[i]));
				}
			}
		} else if ("OrderSearch".equals(service)) {
			if ("1.0".equals(version)) {
				String sdkVerify1 = verify5;
				String[] split = sdkVerify1.split("!");
				for (int i = 0; i < split.length; i++) {
					retMap.put(split[i], (String) util.getValue(res, split[i]));
				}
			} else {
				String sdkVerify1 = verify6;
				String[] split = sdkVerify1.split("!");
				for (int i = 0; i < split.length; i++) {
					retMap.put(split[i], (String) util.getValue(res, split[i]));
				}
			}
		} else if ("OrderRefund".equals(service)) {
			if ("1.0".equals(version)) {
				String sdkVerify1 = verify7;
				String[] split = sdkVerify1.split("!");
				for (int i = 0; i < split.length; i++) {
					retMap.put(split[i], (String) util.getValue(res, split[i]));
				}
			} else {
				String sdkVerify1 = verify8;
				String[] split = sdkVerify1.split("!");
				for (int i = 0; i < split.length; i++) {
					retMap.put(split[i], (String) util.getValue(res, split[i]));
				}
			}
		}else if ("OFFLINERESULT".equals(service)) {
			String sdkVerify9 = verify9;
			String[] split = sdkVerify9.split("!");
			for (int i = 0; i < split.length; i++) {
				retMap.put(split[i], (String) util.getValue(res, split[i]));
			}
		}
		Map responseMap = new HashMap();
		responseMap.putAll(retMap);
		Set set1 = retMap.keySet();
		Iterator iterator1 = set1.iterator();
		while (iterator1.hasNext()) {
			String key0 = (String) iterator1.next();
			String tmp = retMap.get(key0);
			if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
				responseMap.remove(key0);
			}
		}
		String sf = util.coverMap2String(responseMap);
		retMap.put("sf", sf);
		return retMap;
	}
	/**
	 * 验签
	 * 
	 * @param res
	 *            支付系统响应数据
	 * @param configLoadMap
	 *            配置信息
	 * @param map
	 *            解析后的响应数据
	 * @return
	 */
	public static Boolean verifySign(String res, Map configLoadMap, Map map, String pwd, String charset) {
		boolean flag = false;
		RSASignUtil util = new RSASignUtil((String) configLoadMap.get("certPath"), pwd);
		RSASignUtil rsautil = new RSASignUtil((String) configLoadMap.get("rootCertPath"));
		flag = rsautil.verify((String) map.get("sf"), util.getValue(res, "serverSign"),
				util.getValue(res, "serverCert"), charset);
		return flag;
	}
}
