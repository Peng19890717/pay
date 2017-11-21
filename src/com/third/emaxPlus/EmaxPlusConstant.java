package com.third.emaxPlus;

/**
 * Title:调用溢+常量
 * <p>
 * Description:
 * <p>
 * Company: 溢美金融
 *
 * @author peng
 * @version 1.0
 * @since：2017/2/28.
 */

public class EmaxPlusConstant {

    public static final String CREATEORDER_URL = "/QRCode/CreateOrder";//获取支付信息
    public static final String GETPAYINFO_URL = "/QRCode/GetOrder";//查询支付结果
    public static final String MerchantBilling_URL = "/Pay/MerchantBilling";//对账文件下载接口
    public static final String WITHDRAWALS_URL = "/pay/agentPayToCard";//提现
    public static final String GET_WITHDRAWALS_URL = "/pay/agentPayOrderSearch";//提现
    public static final String QRY_ACCOUNTBALANCE_URL = "/account/getBalance";//账户余额
    public static final String MERCHANT_REGIST = "/openAccount";//开户（商家注册）
    public static final String ACCOUNT_STATUS = "/accountStatus";//商户开户状态查询
    public static final String OPENACCOUNT_URL = "/openAccount/createmember";//个人开户（导游\分销商）
    public static final String RECHARGE_URL = "/orderPay";//充值
    public static final String DEDUCT_URL = "/trade/deposit";//扣除保证金
    public static final String SPLITACCOUNTING_URL = "/trade/accounting";//分润
    public static final String GET_SPLITACCOUNTING_URL = "/trade/getAccounting";//分润结果查询
    public static final String SAVE_ACCOUNT_RATE_URL = "/account/saveAccoutingRate";//商户分账比例变更
    public static final String QRY_COMMISSION_URL = "/trade/getHandlingCharge";//订单佣金查询
    public static final String COMMISSION_REFUND_URL = "/trade/merchantTransfer";//订单佣金退回
    public static final String SAVE_BANK_INFO_URL = "/account/saveBankInfo";//商户银行卡信息变更
    public static final String QRY_INCOMINGRECORDS_URL = "/account/getAccountFlow";//收入记录
    public static final String QRY_TRANSACTION_CONFIG_URL = "/account/getTransactionConfig";//商户交易配置查询
    public static final String ACCOUNT_FREEZE = "/account/freeze";//账户冻结/解冻

    public class PayType{
        public static final String wechat_qrcode = "1";//微信
        public static final String alipay_qrcode = "2" ; //支付宝
        public static final String wechat = "3" ; //微信公众号
        public static final String alipay = "4" ; //支付宝服务窗
    }

    public class accountType{
        public static final String CASH = "202"; // 现金户
        public static final String WAITING_SETTLE = "201"; // 待结算户
        public static final String DEPOSIT = "204"; // 保证金
    }

    //溢+支付订单状态
    public class OrderStatus {
        public static final String WAITING_PAY = "DZF";
        public static final String FINISHED = "YWC";
    }

    public class responseStatus{
        public static final String SUCCESS = "SUCCESS";
        public static final String FAIL = "FALILD";
    }

    public class responseCode{
        public static final String SUCCESS = "000000";
    }

}
