package com.third.emaxPlus;

/**
 * Title:退款
 * <p>
 * Description:
 * <p>
 * Company: 溢美金融
 *
 * @author peng
 * @version 1.0
 * @since：2017/3/2.
 */

public class EmaxPlusPayCallbackReq extends EmaxPlusCommonReq{

    private String merCode;//溢+开户账户编号
    private String orderNo;//e游客订单号
    private String orderAmount;//订单金额
    private String payDate;//支付时间
    private String payCompletionDate;//支付完成时间
    private String resultStatus;//返回状态
    private String resultCode;//返回状态码
    private String resultMsg;//返回信息
    private String resultTime;//返回时间
    private String sign;//溢+sign

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultTime() {
        return resultTime;
    }

    public void setResultTime(String resultTime) {
        this.resultTime = resultTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMerCode() {
        return merCode;
    }

    public void setMerCode(String merCode) {
        this.merCode = merCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayCompletionDate() {
        return payCompletionDate;
    }

    public void setPayCompletionDate(String payCompletionDate) {
        this.payCompletionDate = payCompletionDate;
    }

    @Override
    public String toString() {
        return "EmaxPlusPayCallbackReq{" +
                "merCode='" + merCode + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderAmount='" + orderAmount + '\'' +
                ", payDate='" + payDate + '\'' +
                ", payCompletionDate='" + payCompletionDate + '\'' +
                '}';
    }
}
