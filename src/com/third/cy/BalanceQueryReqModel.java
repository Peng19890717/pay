/**
 * 
 */
package com.third.cy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import util.MD5;


/**
 * 封装（查询订单）对象
 */
public class BalanceQueryReqModel extends BaseReqModel {

	private String out_trade_no;//商户订单号
	private String start_time;//开始时间
	private String end_time;//结束时间
	private String mchid;//商户号
    public BalanceQueryReqModel(String src_code, String key,String out_trade_no,String start_time
    		,String end_time,String mchid) {
        super(src_code, key);
        this.out_trade_no = out_trade_no;
        this.start_time = start_time;
        this.end_time = end_time;
        this.mchid = mchid;
    }

    public Map<String, String> toReqMap() {
        Map<String, String> paramMap = new HashMap<String, String>();

        if (this.src_code != null && !this.src_code.equals("")) {
            paramMap.put("src_code", this.src_code);
        }
        if (out_trade_no != null && !out_trade_no.equals("")) {
        	paramMap.put("out_trade_no", out_trade_no);
        }
        if (start_time != null && !start_time.equals("")) {
        	paramMap.put("start_time", start_time);
        }
        if (end_time != null && !end_time.equals("")) {
        	paramMap.put("end_time", end_time);
        }
        if (mchid != null && !mchid.equals("")) {
        	paramMap.put("mchid", mchid);
        }
        //排序，加密
        List<String> params = new ArrayList<String>();
        List<String> sortedkeys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(sortedkeys);
        for (String rk : sortedkeys) {
        	String value = paramMap.get(rk);
        	if("".equals(value) || "sign".equals(rk)){
        		continue;
        	}else{
        		params.add(rk + "=" + value);
        	}
        }
        String presign = StringUtils.join(params, "&") + "&key=" + key;
        String sign = StringUtils.upperCase(MD5.getDigest(presign));
        paramMap.put("sign", sign);
        return paramMap;
    }

}
