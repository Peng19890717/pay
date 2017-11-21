package com.third.emaxPlus;

/**
 * Title:提现结果
 * <p>
 * Description:
 * <p>
 * Company: 溢美金融
 *
 * @author peng
 * @version 1.0
 * @since：2017/3/9.
 */

public class EmaxPlusWithdrawals extends EmaxPlusCommonResponse {
    private String merCode;
    private String orderId;
    private String totalAmount;//以分为单位
    private String tradeTime;//yyyyMMddHHmmss
    private String payStatus;//支付结果状态 收款完成:S 代付订单失败:F 代付订单不存在:N 风控拒绝:R 处理中:C 预设:3

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
