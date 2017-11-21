package com.third.BNS;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 下游订单查询上送信息
 *
 * @author zhang.hui@pufubao.net
 * @version v1.0
 * @date 2016年10月14日 下午4:06:15
 */
@SuppressWarnings("serial")
public class WechatOrderQueryRequest{

    /**
     * 服务类型
     */
    private String service_type;
    /**
     * 公众账号ID
     */
    private String appid;
    /**
     * 商户号
     */
    private String mch_id;
    /**
     * 微信订单号
     */
    private String transaction_id;
    /**
     * 商户订单号
     */
    private String out_trade_no;
    /**
     * 随机字符串
     */
    private String nonce_str;
    /**
     * 签名
     */
    private String sign;

    public WechatOrderQueryRequest() {
        super();
    }

    public WechatOrderQueryRequest(String key, String service_type, String appid, String mch_id, String transaction_id,
                                   String out_trade_no, String nonce_str) {
        setService_type(service_type);
        setAppid(appid);
        setMch_id(mch_id);
        setTransaction_id(transaction_id);
        setOut_trade_no(out_trade_no);
        setNonce_str(nonce_str);
        String sign = getSign(toMap(), key);
        setSign(sign);
    }
    /**
  	 * 签名算法
  	 *
  	 * @param map
  	 * @param key
  	 * @param log
  	 * @return
  	 * @author zhang.hui@pufubao.net
  	 * @date 2016年11月11日 下午2:53:33
  	 */
  	public  String getSign(Map<String, Object> map, String key) {
  		ArrayList<String> list = new ArrayList<String>();
  		for (Map.Entry<String, Object> entry : map.entrySet()) {
  			if (entry.getValue() != null && StringUtils.isNotBlank(String.valueOf(entry.getValue()))) {
  				list.add(entry.getKey() + "=" + entry.getValue() + "&");
  			}
  		}
  		int size = list.size();
  		String[] arrayToSort = list.toArray(new String[size]);
  		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
  		StringBuilder sb = new StringBuilder();
  		for (int i = 0; i < size; i++) {
  			sb.append(arrayToSort[i]);
  		}
  		String result = sb.toString();
  		result = result.substring(0, result.length() - 1);
  		result += key;
  		result = MD5Util.MD5Encode(result).toUpperCase();
  		return result;
  	}
    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * 对象转化为Map
     *
     * @return
     * @author zhang.hui@pufubao.net
     * @date 2016年10月14日 下午6:19:18
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if (obj != null && StringUtils.isNotBlank(String.valueOf(obj))) {
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
