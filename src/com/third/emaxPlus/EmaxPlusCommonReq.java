package com.third.emaxPlus;

/**
 * description
 * Created by lekaijun
 * Date 2017/3/10
 */
public class EmaxPlusCommonReq {
    /**
     * 溢+开户返回 商户编号
     */
    private String merCode;

    /**
     * 请求时间
     */
    private String dateTime;

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
