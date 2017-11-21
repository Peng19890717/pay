/**
 * 
 */
package com.third.cy;

import java.util.HashMap;
import java.util.Map;



import com.PayConstant;
import com.alibaba.fastjson.JSONObject;

public class WangGuanPayReqModel extends BaseReqModel {

    private String mch_id;
    private String total_fee;
    private String goods_name;
    private String out_trade_no;
    private String time_start;
    private String finish_url;

    private String trade_type = PayConstant.PAY_CONFIG.get("CY_GATEWAY_TRADETYPE");//交易类型：网银80101

    private String bankName;
    private String cardType;

    /**
     * @param trade_type
     *            the trade_type to set
     */
    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public WangGuanPayReqModel(String src_code, String key, String mch_id, String total_fee, String goods_name, String out_trade_no, String time_start, String finish_url,
            String bankName, String cardType) {
        super(src_code, key);
        this.mch_id = mch_id;
        this.total_fee = total_fee;
        this.goods_name = goods_name;
        this.out_trade_no = out_trade_no;
        this.time_start = time_start;
        this.finish_url = finish_url;
        this.cardType = cardType;
        this.bankName = bankName;
    }

    public Map<String, String> toReqMap() {
        Map<String, String> paramMap = new HashMap<String, String>();

        if (!this.src_code.equals("") && this.src_code != null) {
            paramMap.put("src_code", this.src_code);
        }
        if (!this.mch_id.equals("") && this.mch_id != null) {
            paramMap.put("mchid", this.mch_id);
        }
        if (!this.total_fee.equals("") && this.total_fee != null) {
            paramMap.put("total_fee", this.total_fee);
        }
        if (!this.goods_name.equals("") && this.goods_name != null) {
            paramMap.put("goods_name", this.goods_name);
        }
        if (!this.trade_type.equals("") && this.trade_type != null) {
            paramMap.put("trade_type", this.trade_type);
        }
        if (!this.time_start.equals("") && this.time_start != null) {
            paramMap.put("time_start", this.time_start);
        }
        if (!this.out_trade_no.equals("") && this.out_trade_no != null) {
            paramMap.put("out_trade_no", this.out_trade_no);
        }
        if (!this.finish_url.equals("") && this.finish_url != null) {
            paramMap.put("finish_url", this.finish_url);
        }
        if (!this.getExtend().equals("") && this.getExtend() != null) {
            paramMap.put("extend", this.getExtend());
        }
        makeReqParamMap(paramMap);
        return paramMap;
    }

    /**
     * @return
     */
    private String getExtend() {
        Map<String, String> extMap = new HashMap<String, String>();
        extMap.put("bankName", this.bankName);
        extMap.put("cardType", this.cardType);

        return JSONObject.toJSONString(extMap);
    }

}
