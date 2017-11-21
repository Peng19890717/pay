package com.third.emaxPlus;

import java.io.Serializable;

/**
 * Title:e旅游支付返回对象
 * <p>
 * Description:
 * <p>
 * Company: 溢美金融
 *
 * @author peng
 * @version 1.0
 * @since：2017/2/23.
 */

public class EmaxPlusPayResponse extends EmaxPlusCommonResponse implements Serializable{

    private String qrCodeUrl;//二维码url地址
    private String orderNo;//订单号
    private String merCode;//溢+开户账户

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
}
