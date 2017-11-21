package com.third.ld;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
/**
 * ***********************************************************************
 * <br>description : 缓存请求的servic接口、正则、加解密字段等
 * @author      umpay
 * @date        2014-8-1 上午09:40:46
 * @version     1.0  
 ************************************************************************
 */
public  class ServiceMapUtil {
	private static Map serviceRule = new HashMap();
	private static Map reqRule = new HashMap();
	private static HashSet encryptSet = new HashSet();
	private static HashSet encryptId = new HashSet();
	/** 一般支付请求*/
	public static final String PAY_REQ_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	/** IVR支付方式下单*/
	public static final String PAY_REQ_IVR_CALL_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	/** IVR转呼方式下单*/
	public static final String PAY_REQ_IVR_TCALL_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	/** 商户查询订单状态*/
	public static final String QUERY_ORDER_RULE="service,charset,sign_type,mer_id,version,order_id,mer_date";
	/** 商户撤销交易*/
	public static final String MER_CANCEL_RULE ="service,charset,sign_type,mer_id,version,order_id,mer_date,amount";
	/** 商户退费*/
	public static final String MER_REFUND_RULE ="service,charset,sign_type,mer_id,version,refund_no,order_id,mer_date,org_amount";
	/** 下载对账文件*/
	public static final String DOWNLOAD_SETTLE_FILE_RULE ="service,sign_type,mer_id,version,settle_date";
	/** 分账前端支付请求*/
	public static final String PAY_REQ_SPLIT_FRONT_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	/** 分账后端支付请求*/
	public static final String PAY_REQ_SPLIT_BACK_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	/** 分账退费*/
	public static final String SPLIT_REFUND_REQ_RULE = "service,charset,mer_id,sign_type,version,refund_no,order_id,mer_date,refund_amount,org_amount,sub_mer_id,sub_order_id";
	/** 直连网银*/
	public static final String PAY_REQ_SPLIT_DIRECT_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
	/** 交易结果通知*/
	public static final String PAY_RESULT_NOTIFY_RULE = "service,mer_id,sign_type,version,trade_no,order_id,mer_date,pay_date,amount,amt_type,pay_type,settle_date,trade_state";
	/** 分账结果通知*/
	public static final String SPLIT_REQ_RESULT_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,is_success";
	/** 分账退费结果通知*/
	public static final String SPLIT_REFUND_RESULT_RULE = "service,charset,sign_type,mer_id,version,refund_no,order_id,mer_date,refund_amount,org_amount,refund_amt,sub_mer_id,sub_order_id,sub_refund_amt,is_success";
	/** 信用卡直连*/
	public static final String CREDIT_DIRECT_PAY_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,pay_type,card_id,valid_date,cvv2";
	/** 借记卡直连*/
	public static final String DEBIT_DIRECT_PAY_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,pay_type,card_id";
	/**预授权直连申请*/
	public static final String PRE_AUTH_DIRECT_REQ = "service,charset,mer_id,sign_type,version,order_id,mer_date,media_id,media_type,amount,amt_type,pay_type,card_id,valid_date,cvv2";
	/**预授权完成*/
	public static final String PRE_AUTH_DIRECT_PAY = "service,charset,mer_id,sign_type,version,order_id,trade_no,mer_date,amount,amt_type,pay_type";
	/**预授权撤销*/
	public static final String PRE_AUTH_DIRECT_CANCEL = "service,charset,mer_id,sign_type,version,order_id,trade_no,mer_date";
	/**银行卡转账注册*/
	public static final String PAY_TRANSFER_REGISTER = "service,charset,mer_id,res_format,version,sign_type,req_date,req_time,media_type,media_id,identity_type,identity_code,cust_name";
	/**银行卡转账申请*/
	public static final String PAY_TRANSFER_REQ = "service,charset,mer_id,ret_url,notify_url,res_format,version,sign_type,order_id,mer_date,req_time,media_id,media_type,amount,fee_amount,recv_account_type,recv_bank_acc_pro,recv_account,recv_user_name,recv_gate_id,recv_type,purpose";
	/**银行卡转账订单查询*/
	public static final String PAY_TRANSFER_ORDER_QUERY = "service,charset,mer_id,res_format,version,sign_type,order_id,mer_date";
	/**银行卡转账退费*/
	public static final String PAY_TRANSFER_MER_REFUND = "service,charset,mer_id,res_format,version,sign_type,refund_no,order_id,mer_date";
	/**预授权查询*/
    public static final String PRE_AUTH_DIRECT_QUERY = "service,charset,mer_id,sign_type,version,order_id,mer_date";
    /**预授权退费*/
    public static final String PRE_AUTH_DIRECT_REFUND = "service,charset,sign_type,mer_id,version,order_id,mer_date,refund_no,refund_amount,org_amount";
    /**预授权下载对账文件*/
    public static final String PRE_AUTH_DIRECT_SETTLE = "service,sign_type,mer_id,version,settle_date";
    /**实名认证*/
    public static final String CARD_AUTH = "service,charset,mer_id,sign_type,version,mer_date,card_id";
    /**信用卡API快捷---获取短信验证码*/
    public static final String REQ_SMS_VERIFYCODE = "service,mer_id,charset,sign_type,version,trade_no,media_id,media_type";
    /**信用卡API快捷---确认支付*/
    public static final String PAY_CONFIRM = "service,mer_id,charset,sign_type,version,trade_no,pay_category,card_id";
    /**一键快捷--前端请求*/
    public static final String PAY_REQ_SHORTCUT_FRONT = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type,pay_type,gate_id";
    /**一键快捷--API下单*/
    public static final String PAY_REQ_SHORTCUT = "service,charset,mer_id,sign_type,version,order_id,mer_date,amount,amt_type";
    /**一键快捷--(首次支付)确认支付*/
    public static final String FIRST_PAY_CONFIRM_SHORTCUT = "service,mer_id,charset,sign_type,version,trade_no,media_id,media_type,card_id";
    /**一键快捷--（协议支付)确认支付*/
    public static final String AGREEMENT_PAY_CONFIRM_SHORTCUT = "service,mer_id,charset,sign_type,version,trade_no,usr_pay_agreement_id";
    /**一键快捷--获取短信验证码*/
    public static final String REQ_SMSVERIFY_SHORTCUT = "service,mer_id,sign_type,version,trade_no";
    /**一键快捷--查询商户支持的银行列表*/
    public static final String QUERY_MER_BANK_SHORTCUT = "service,sign_type,charset,mer_id,version,pay_type";
    /**一键快捷--查询用户签约的银行列表*/
    public static final String QUERY_MERCUST_BANK_SHORTCUT = "service,sign_type,charset,mer_id,version,pay_type";
    /**一键快捷--商户解除用户关联*/
    public static final String UNBIND_MERCUST_PROTOCOL_SHORTCUT = "service,sign_type,charset,mer_id,version";
    /**分账项目--分账指令*/
    public static final String SPLIT_REQ_RULE = "service,charset,mer_id,sign_type,version,order_id,mer_date";
    /**分账项目--分账状态查询*/
    public static final String QUERY_SPLIT_ORDER_RULE = "service,sign_type,charset,mer_id,version,order_id,mer_date";
    /**付款API直连--付款请求*/
    public static final String TRANSFER_DIRECT_REQ_RULE = "service,charset,mer_id,version,sign_type,order_id,mer_date,amount,recv_account_type,recv_bank_acc_pro,recv_account,recv_user_name,recv_gate_id,purpose,prov_name,city_name,bank_brhname";
    /**付款API直连--付款查询*/
    public static final String TRANSFER_QUERY_RULE = "service,charset,mer_id,version,sign_type,order_id,mer_date";
    /**历史订单查询*/
    public static final String MER_ORDER_INFO_QUERY = "service,sign_type,charset,mer_id,version,order_id,mer_date";
    /**退费订单状态查询*/
    public static final String MER_REFUND_QUERY = "service,sign_type,charset,mer_id,version,refund_no";
	
	static{
		serviceRule.put("pay_req", PAY_REQ_RULE);
		serviceRule.put("pay_req_ivr_call", PAY_REQ_IVR_CALL_RULE);
		serviceRule.put("pay_req_ivr_tcall", PAY_REQ_IVR_TCALL_RULE);
		serviceRule.put("query_order", QUERY_ORDER_RULE);
		serviceRule.put("mer_cancel", MER_CANCEL_RULE);
		serviceRule.put("mer_refund", MER_REFUND_RULE);
		serviceRule.put("download_settle_file", DOWNLOAD_SETTLE_FILE_RULE);
		serviceRule.put("pay_req_split_front", PAY_REQ_SPLIT_FRONT_RULE);
		serviceRule.put("pay_req_split_back", PAY_REQ_SPLIT_BACK_RULE);
		serviceRule.put("pay_req_split_direct", PAY_REQ_SPLIT_DIRECT_RULE);
		serviceRule.put("split_refund_req", SPLIT_REFUND_REQ_RULE);
		serviceRule.put("pay_result_notify",PAY_RESULT_NOTIFY_RULE);
		serviceRule.put("split_req_result", SPLIT_REFUND_REQ_RULE);
		serviceRule.put("split_refund_result", SPLIT_REFUND_RESULT_RULE);
		serviceRule.put("credit_direct_pay", CREDIT_DIRECT_PAY_RULE);
		serviceRule.put("debit_direct_pay", DEBIT_DIRECT_PAY_RULE);
		serviceRule.put("pre_auth_direct_req", PRE_AUTH_DIRECT_REQ);
		serviceRule.put("pre_auth_direct_pay", PRE_AUTH_DIRECT_PAY);
		serviceRule.put("pre_auth_direct_cancel", PRE_AUTH_DIRECT_CANCEL);
		serviceRule.put("pre_auth_direct_query", PRE_AUTH_DIRECT_QUERY);
		serviceRule.put("pre_auth_direct_refund", PRE_AUTH_DIRECT_REFUND);
		serviceRule.put("pre_auth_direct_settle", PRE_AUTH_DIRECT_SETTLE);
		serviceRule.put("pay_transfer_register", PAY_TRANSFER_REGISTER);
		serviceRule.put("pay_transfer_req", PAY_TRANSFER_REQ);
		serviceRule.put("pay_transfer_order_query", PAY_TRANSFER_ORDER_QUERY);
		serviceRule.put("pay_transfer_mer_refund", PAY_TRANSFER_MER_REFUND);
		serviceRule.put("card_auth", CARD_AUTH);
		serviceRule.put("req_sms_verifycode", REQ_SMS_VERIFYCODE);
		serviceRule.put("pay_confirm", PAY_CONFIRM);
		serviceRule.put("pay_req_shortcut_front", PAY_REQ_SHORTCUT_FRONT);
		serviceRule.put("pay_req_shortcut", PAY_REQ_SHORTCUT);
		serviceRule.put("first_pay_confirm_shortcut", FIRST_PAY_CONFIRM_SHORTCUT);
		serviceRule.put("agreement_pay_confirm_shortcut", AGREEMENT_PAY_CONFIRM_SHORTCUT);
		serviceRule.put("req_smsverify_shortcut", REQ_SMSVERIFY_SHORTCUT);
		serviceRule.put("query_mer_bank_shortcut", QUERY_MER_BANK_SHORTCUT);
		serviceRule.put("query_mercust_bank_shortcut", QUERY_MERCUST_BANK_SHORTCUT);
		serviceRule.put("unbind_mercust_protocol_shortcut", UNBIND_MERCUST_PROTOCOL_SHORTCUT);
		serviceRule.put("split_req", SPLIT_REQ_RULE);
		serviceRule.put("query_split_order", QUERY_SPLIT_ORDER_RULE);
		serviceRule.put("transfer_direct_req", TRANSFER_DIRECT_REQ_RULE);
		serviceRule.put("transfer_query", TRANSFER_QUERY_RULE);
		serviceRule.put("mer_order_info_query", MER_ORDER_INFO_QUERY);
		serviceRule.put("mer_refund_query", MER_REFUND_QUERY);
	}
	
	/**
	 * ReqRule 第一个参数true、false 标识是否允许为空
	 *         第二个参数标识，需要匹配的正则表达式
	 *         第三个参数标识，字符对应值允许的最大长度 0标识未设定
	 *         第四个参数表示，是否进行urlEncoder
	 */
	static{
		reqRule.put("service", new ReqRule(false,"[a-zA-Z0-9_]*",32,false));
		reqRule.put("charset", new ReqRule(false,"UTF-8|GBK|GB2312|GB18030",0,false));
		reqRule.put("mer_id", new ReqRule(false,"^[0-9]*$",8,false));
		reqRule.put("sign_type", new ReqRule(false,"RSA",0,false));
		reqRule.put("sign", new ReqRule(false,"",0,true));        //如商户请求POST方式,无需对sign字段编码
		reqRule.put("ret_url", new ReqRule(true,"",0,true));
		reqRule.put("notify_url", new ReqRule(true,"",0,true));
		reqRule.put("res_format", new ReqRule(false,"",0,false));
		reqRule.put("version", new ReqRule(false,"4.0|1.0",3,true));
		reqRule.put("goods_id", new ReqRule(true,"",0,false));
		reqRule.put("goods_inf", new ReqRule(true,"",0,true));
		reqRule.put("media_id", new ReqRule(true,"",0,true));
		reqRule.put("mobile_id", new ReqRule(true,"",11,false));
		reqRule.put("media_type", new ReqRule(true,"MOBILE|EMAIL|MERUSERID",0,false));       //
		reqRule.put("order_id", new ReqRule(false,"",32,true));       //
		reqRule.put("mer_date", new ReqRule(false,"[12][0-9]{7}",32,false));        //
		reqRule.put("amount", new ReqRule(false,"^[1-9][0-9]*$",0,false));
		reqRule.put("amt_type", new ReqRule(false,"RMB",0,false));        
		reqRule.put("pay_type", new ReqRule(true,"",0,false));
		reqRule.put("gate_id", new ReqRule(true,"",0,false));
		reqRule.put("mer_priv", new ReqRule(true,"",0,true));
		reqRule.put("user_ip", new ReqRule(true,"",0,false));
		reqRule.put("expand", new ReqRule(true,"",0,true));
		reqRule.put("expire_time", new ReqRule(true,"[0-9]*",32,false));
		reqRule.put("token", new ReqRule(false,"",0,false));
		reqRule.put("trade_state", new ReqRule(false,"",32,false));
		reqRule.put("ret_code", new ReqRule(false,"^[0-9]*$",0,false));
		reqRule.put("ret_msg", new ReqRule(true,"",0,true));
		reqRule.put("trade_no", new ReqRule(false,"",16,false));
		reqRule.put("pay_date", new ReqRule(false,"[12][0-9]{7}",8,false));
		reqRule.put("settle_date", new ReqRule(false,"[12][0-9]{7}",8,false));
		reqRule.put("pay_seq", new ReqRule(true,"",0,false));
		reqRule.put("error_code", new ReqRule(true,"^[0-9]*$",0,false));
		reqRule.put("mer_check_date", new ReqRule(true,"[12][0-9]{7}",0,false));
		reqRule.put("mer_trace", new ReqRule(true,"",0,false));
		reqRule.put("bank_check_state", new ReqRule(true,"",0,false));
		reqRule.put("product_id", new ReqRule(true,"",0,false));
		reqRule.put("refund_amt", new ReqRule(true,"^[1-9][0-9]*$",0,false));             //
		reqRule.put("refund_no", new ReqRule(false,"",16,false));
		reqRule.put("refund_amount", new ReqRule(false,"^[1-9][0-9]*$",0,false));     //
		reqRule.put("org_amount", new ReqRule(false,"^[1-9][0-9]*$",0,false));
		reqRule.put("refund_state", new ReqRule(false,"",0,false));
		reqRule.put("split_data", new ReqRule(true,"",0,true));
		reqRule.put("split_type", new ReqRule(true,"11|21|[1-2]{0,2}",0,false));
		reqRule.put("is_success", new ReqRule(false,"Y|N",1,false));
		reqRule.put("sub_mer_id", new ReqRule(false,"^[0-9]*$",8,false));
		reqRule.put("sub_order_id",new ReqRule(false,"",32,false));
		reqRule.put("refund_desc", new ReqRule(false,"",128,true));
		reqRule.put("card_id", new ReqRule(true,"",256,true));
		reqRule.put("valid_date", new ReqRule(false,"",256,true));
		reqRule.put("cvv2", new ReqRule(false,"",256,true));
		reqRule.put("pass_wd", new ReqRule(true,"",256,true));
		reqRule.put("identity_type",new ReqRule(true,"",256,false));
		reqRule.put("identity_code",new ReqRule(true,"",256,true));
		reqRule.put("card_holder",new ReqRule(true,"",256,true));
		reqRule.put("req_date", new ReqRule(true,"[12][0-9]{7}",8,false));
		reqRule.put("req_time", new ReqRule(true,"[0-9]{6}",6,false));
		reqRule.put("cust_name", new ReqRule(true,"",32,true));
		reqRule.put("mail_addr", new ReqRule(false,"",64,true));
		reqRule.put("birthday", new ReqRule(false,"[12][0-9]{7}",8,false));
		reqRule.put("sex", new ReqRule(false,"M|F",1,false));
		reqRule.put("contact_phone", new ReqRule(false,"",0,false));
		reqRule.put("contact_mobile", new ReqRule(false,"[0-9]{11}",11,false));
		reqRule.put("fee_amount", new ReqRule(false,"0|^[1-9][0-9]*$",0,false));
		reqRule.put("finance_vou_no", new ReqRule(false,"",32,false));
		reqRule.put("recv_account_type", new ReqRule(true,"[0-1]{2}",2,false));
		reqRule.put("recv_bank_acc_pro", new ReqRule(true,"[0-1]{1}",1,false));
		reqRule.put("recv_account", new ReqRule(true,"",0,true));
		reqRule.put("recv_user_name", new ReqRule(true,"",0,true));
		reqRule.put("recv_gate_id", new ReqRule(true,"",0,false));
		reqRule.put("recv_type", new ReqRule(true,"[0-1]",1,false));
		reqRule.put("purpose", new ReqRule(false,"",0,true));
		reqRule.put("prov_name", new ReqRule(false,"",0,true));
		reqRule.put("city_name", new ReqRule(false,"",0,true));
		reqRule.put("bank_brhname", new ReqRule(false,"",0,true));
		reqRule.put("debit_pay_type", new ReqRule(true,"1|2",1,false));
		reqRule.put("pay_category", new ReqRule(false,"01",2,false));
		reqRule.put("verify_code",new ReqRule(true,"",8,false));
		reqRule.put("mer_cust_id", new ReqRule(true,"",32,false));
		reqRule.put("usr_busi_agreement_id", new ReqRule(true,"",64,false));
		reqRule.put("usr_pay_agreement_id", new ReqRule(true,"",64,false));
		reqRule.put("split_category", new ReqRule(true,"1|2|3",1,false));
		reqRule.put("identity_holder", new ReqRule(true,"",256,true));
		reqRule.put("split_refund_list", new ReqRule(true,"",0,true));
		reqRule.put("split_cmd", new ReqRule(true,"",0,true));
		reqRule.put("settle_type", new ReqRule(true,"",0,false));
		reqRule.put("push_type", new ReqRule(true,"0|1|2|3",1,false));
		reqRule.put("order_type", new ReqRule(true,"1|2",1,false));
	}
	
	/**默认需要进行加密的字段名称*/
	static{
		 encryptId.add("card_id");
		 encryptId.add("cvv2");
		 encryptId.add("valid_date");
		 encryptId.add("card_holder");
		 encryptId.add("identity_code");
		 encryptId.add("pass_wd");
		 encryptId.add("recv_account");
		 encryptId.add("recv_user_name");
		 encryptId.add("identity_holder");
		 encryptId.add("cardId");
		 encryptId.add("validDate");
		 encryptId.add("cardHolder");
		 encryptId.add("identityCode");
		 encryptId.add("passWd");
		 encryptId.add("mer_cust_name");
		 encryptId.add("account_name");
	}
	public static Map getServiceRule() {
		return serviceRule;
	}

	public static Map getReqRule() {
		return reqRule;
	}

	public static HashSet getEncryptSetw() {
		return encryptSet;
	}

	public static HashSet getEncryptId() {
		return encryptId;
	}
}
