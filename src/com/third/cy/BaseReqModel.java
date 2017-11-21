/**
 * 
 */
package com.third.cy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.pay.merchantinterface.service.CYPayService;


public class BaseReqModel {
	
	private static final Log log = LogFactory.getLog(BaseReqModel.class);

    protected String src_code;

    protected String key;

    /**
     * @param src_code
     * @param key
     */
    public BaseReqModel(String src_code, String key) {
        super();
        this.src_code = src_code;
        this.key = key;
    }

    public void makeReqParamMap(Map<String, String> paramMap) {
        List<String> params = new ArrayList<String>();
        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(sortedkeys);
        for (String rk : sortedkeys) {
            params.add(rk + "=" + paramMap.get(rk));
        }
        String presign = StringUtils.join(params, "&") + "&key=" + this.key;
        log.info("长盈：签名字符串:" + presign);
        String sign = StringUtils.upperCase(DigestUtils.md5Hex(presign));
        log.info("长盈：签名:" + sign);
        paramMap.put("sign", sign);

    }

    public Map<String, String> makeReqParamMapByRsa(Map<String, String> paramMap) {
        List<String> params = new ArrayList<String>();
        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(sortedkeys);
        for (String rk : sortedkeys) {
            params.add(rk + "=" + paramMap.get(rk));
        }
        String presign = StringUtils.join(params, "&") + "&key=" + this.key;
        log.info("长盈：签名字符串:" + presign);
        String sign = StringUtils.upperCase(DigestUtils.md5Hex(presign));
        log.info("长盈：签名:" + sign);
        String encryptdata = StringUtils.join(params, "&") + "&sign=" + sign;
        log.info("长盈：RSA前数据:" + encryptdata);

        String publicKey = getPublicKey();
        try {
            encryptdata = Base64.encodeBase64String(RSAUtils.encryptByPublicKey(encryptdata.getBytes(), publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("RSA后数据:" + encryptdata);
        Map<String, String> newparamMap = new HashMap<String, String>();
        newparamMap.put("encrypt_data", encryptdata);
        return newparamMap;
    }

    public String getPublicKey() {
    	String publickey = PayConstant.PAY_CONFIG.get("CY_PUB_KEY");
        return publickey;
    }

}
