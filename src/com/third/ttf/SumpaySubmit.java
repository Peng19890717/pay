package com.third.ttf;

import java.util.Map;

import util.JWebConstant;

import com.PayConstant;

public class SumpaySubmit {
	public static String mer_pfx_key = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("TTF_MERCHANT_PFX");//商户私钥
	public static String server_cert = JWebConstant.APP_PATH+JWebConstant.PATH_DIV+"config"+JWebConstant.PATH_DIV+PayConstant.PAY_CONFIG.get("TTF_SERVER_CERT");//平台公钥
	public static String mer_pfx_pwd = PayConstant.PAY_CONFIG.get("TTF_MERCHANT_PFX_PWD");//商户私钥密码
	public static CryptInf cryptInf = new CryptNoRestrict();
	/**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
	 * @throws Exception 
     */
    public static String buildRequestMysign(Map<String, String> sPara) throws Exception {
        String prestr = SumpayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        System.out.println("签名前参数:" + prestr);
        String mysign = null;
		try {
			mysign = cryptInf.SignMsg(prestr, mer_pfx_key, mer_pfx_pwd);//代签名串  私钥  私钥密码
		} catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("签名结果:" + mysign);
        return mysign;
    }
    /**
     * 生成要请求给商盟统统付的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     * @throws Exception 
     */
    public static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) throws Exception {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = SumpayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", "CERT");
        return sPara;
    }
    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    public static boolean getSignVeryfy(Map<String, String> Params, String sign) {
        //过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = SumpayCore.paraFilter(Params);
        //获取待签名字符串
        String preSignStr = SumpayCore.createLinkString(sParaNew);
        //获得签名验证结果
        try {
			return cryptInf.VerifyMsg(sign, preSignStr, server_cert);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return false;
    }
}
