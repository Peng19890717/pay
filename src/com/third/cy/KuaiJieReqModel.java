/**
 * 
 */
package com.third.cy;

import java.util.HashMap;
import java.util.Map;

import com.PayConstant;
import com.alibaba.fastjson.JSONObject;

public class KuaiJieReqModel extends BaseReqModel {

    private String mch_id;
    private String total_fee;
    private String bankName;
    private String cardType;
    private String accoutNo;
    private String accountName;
    private String idType;
    private String idNumber;
    private String mobile;

    private String goods_name;
    private String out_trade_no;
    private String time_start;
    private String finish_url;

    private String trade_type = PayConstant.PAY_CONFIG.get("CY_KJ_TRADETYPE");//交易类型
    private String code;
    private String signSn;

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @param signSn
     *            the signSn to set
     */
    public void setSignSn(String signSn) {
        this.signSn = signSn;
    }

    public KuaiJieReqModel(String src_code, String key, String mch_id, String total_fee, String bankName, String cardType, String accoutNo, String accountName, String idType,
            String idNumber, String mobile, String goods_name, String out_trade_no, String time_start, String finish_url) {
        super(src_code, key);
        this.mch_id = mch_id;
        this.total_fee = total_fee;
        this.bankName = bankName;
        this.cardType = cardType;
        this.accoutNo = accoutNo;
        this.accountName = accountName;
        this.idType = idType;
        this.idNumber = idNumber;
        this.mobile = mobile;
        this.goods_name = goods_name;
        this.out_trade_no = out_trade_no;
        this.time_start = time_start;
        this.finish_url = finish_url;
    }

    public Map<String, String> toFastSignReqMap() {
        Map<String, String> paramMap = new HashMap<String, String>();
        if (this.src_code != null && !this.src_code.equals("")) {
            paramMap.put("src_code", this.src_code);
        }
        if (this.mch_id != null && !this.mch_id.equals("")) {
            paramMap.put("mch_id", this.mch_id);
        }
        if (this.total_fee != null && !this.total_fee.equals("")) {
            paramMap.put("total_fee", this.total_fee);
        }
        if (this.bankName !=null && !this.bankName.equals("")) {
            paramMap.put("bankName", this.bankName);
        }
        if (this.cardType != null && !this.cardType.equals("")) {
            paramMap.put("cardType", this.cardType);
        }
        if (this.accoutNo != null && !this.accoutNo.equals("")) {
            paramMap.put("accoutNo", this.accoutNo);
        }
        if (this.accountName != null && !this.accountName.equals("")) {
            paramMap.put("accountName", this.accountName);
        }
        if (this.idType != null && !this.idType.equals("")) {
            paramMap.put("idType", this.idType);
        }
        if (this.idNumber != null && !this.idNumber.equals("")) {
            paramMap.put("idNumber", this.idNumber);
        }
        if (this.mobile != null && !this.mobile.equals("")) {
            paramMap.put("Mobile", this.mobile);
        }
        makeReqParamMap(paramMap);
        return paramMap;

    }

    public Map<String, String> toPayReqMap() {
        Map<String, String> paramMap = new HashMap<String, String>();

        if (this.src_code != null && !this.src_code.equals("")) {
            paramMap.put("src_code", this.src_code);
        }
        if (this.mch_id != null && !this.mch_id.equals("")) {
            paramMap.put("mchid", this.mch_id);
        }
        if (this.total_fee != null && !this.total_fee.equals("")) {
            paramMap.put("total_fee", this.total_fee);
        }
        if (this.goods_name != null && !this.goods_name.equals("")) {
            paramMap.put("goods_name", this.goods_name);
        }
        if (this.trade_type != null && !this.trade_type.equals("")) {
            paramMap.put("trade_type", this.trade_type);
        }
        if (this.time_start != null && !this.time_start.equals("")) {
            paramMap.put("time_start", this.time_start);
        }
        if (this.out_trade_no != null && !this.out_trade_no.equals("")) {
            paramMap.put("out_trade_no", this.out_trade_no);
        }
        if (this.finish_url != null && !this.finish_url.equals("")) {
            paramMap.put("finish_url", this.finish_url);
        }
        if (this.getExtend() != null && !this.getExtend().equals("")) {
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
        extMap.put("accoutNo", this.accoutNo);
        extMap.put("bankName", this.bankName);
        extMap.put("accountName", this.accountName);
        extMap.put("idNumber", this.idNumber);
        extMap.put("Mobile", this.mobile);
        extMap.put("code", this.code);
        extMap.put("signSn", this.signSn);
        extMap.put("idType", this.idType);
        extMap.put("cardType", this.cardType);

        return JSONObject.toJSONString(extMap);
    }

}
