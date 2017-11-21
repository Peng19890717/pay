package com;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.PropertyReader;

import com.pay.biztype.service.PayBizTypeService;

public class PayConstant {
	private static final Log log = LogFactory.getLog(PayConstant.class);
	public static Map<String, String> PAY_CONFIG = null;
	//订单状态，00:等待付款  01:付款成功  02:付款失败 03:退款申请 04:等待退款 05:退款成功 06:退款失败 07:同意撤销 08：拒绝撤销  09：撤销成功 10:撤销失败 12 预付款  17 实付审核中 18 实付审核通过 19 实付审核拒绝 99:超时，默认00
	//数据库名称
	public static Map<String,String>  ORDER_SQL_NAME = new HashMap<String,String>();
	public static Map<String, String> PAY_STATUS = new HashMap();
	//00: 退款处理中 01:退款成功  02：退款失败
	public static Map<String, String> REFUND_STATUS = new HashMap();
	//响应信息提示码
	public static Map<String, String> RESP_CODE_DESC = new HashMap();
	//商户业务类型
	public static Map<String, String> MER_BIZ_TYPE = new HashMap();
	//商户类型
	public static Map<String, String> MER_TYPE = new HashMap();
	//用户互通标识
	public static Map<String, String> USER_INTERFLOW_FLAG = new HashMap();
	//证件类型
	public static Map<String, String> CERT_TYPE = new HashMap();
	//结算周期
	public static Map<String, String> MER_STL_PERIOD = new HashMap();
	//转账错误
	public static Map<String, String> TRANSFER_ACC_ERROR = new HashMap();
	//卡bin类型
	public static Map<String, String> CARD_BIN_TYPE = new HashMap();
	//用户限额认证状态
	public static Map<String, String> RISK_USER_TYPE = new HashMap();
	//X名单类型
	public static Map<String, String> X_LIST_TYPE = new HashMap();
	//借记卡
	public static String CARD_BIN_TYPE_JIEJI = "0";
	//信用卡
	public static String CARD_BIN_TYPE_DAIJI = "1";
	//准贷记卡
	public static String CARD_BIN_TYPE_ZHUNDAIJI = "2";
	//预付卡
	public static String CARD_BIN_TYPE_YUFU = "3";
	//0用户 1商户 2支付渠道
	public static String CUST_TYPE_USER="0";
	public static String CUST_TYPE_MERCHANT="1";
	public static String CUST_TYPE_MOBLIE="2";
	public static String CUST_TYPE_CARD="3";
	public static String CUST_TYPE_PAY_CHANNEL="2";
	public static String CUST_TYPE_CHARGE ="5";
	//手续费类型  0交易结算，1代收，2代付
	public static String TRAN_SETTLRMENT = "0";
	public static String CHARER_BEHALF = "1";
	public static String PAY_FEE_BEHALF = "2";
	//交易汇总，客户类型	
	public static String CUST_TYPE_PAY_MOBILE="2";
	public static String CUST_TYPE_PAY_CARD="3";
	//交易类型 1消费 2充值 3结算 4退款 5提现 6转账
	public static Map<String, String> TRAN_TYPE = new HashMap<String, String>();
	public static String TRAN_TYPE_CONSUME="1";
	public static String TRAN_TYPE_CONSUME_B2C_DEBIT="7";
	public static String TRAN_TYPE_CONSUME_B2C_CREBIT="8";
	public static String TRAN_TYPE_CONSUME_B2B="9";
	public static String TRAN_TYPE_CONSUME_TO_CARD="10";
	public static String TRAN_TYPE_CONSUME_QUICK_DEBIT="11";
	public static String TRAN_TYPE_CONSUME_QUICK_CREBIT="12";
	public static String TRAN_TYPE_RECHARGE="2";
	public static String TRAN_TYPE_SETTLE="3";
	public static String TRAN_TYPE_REFUND="4";
	public static String TRAN_TYPE_WITHDRAW="5";
	public static String TRAN_TYPE_TRANSFER="6";
	public static String TRAN_TYPE_CARD="7";//用户限额使用
	// ===========================================
	public static String TRAN_TYPE_B2CJJ=TRAN_TYPE_CONSUME_B2C_DEBIT; // 消费b2c借记卡
	public static String TRAN_TYPE_B2CXY=TRAN_TYPE_CONSUME_B2C_CREBIT; // 消费b2c信用卡
	public static String TRAN_TYPE_B2B=TRAN_TYPE_CONSUME_B2B; // 消费B2B
	public static String TRAN_TYPE_YHK=TRAN_TYPE_CONSUME_TO_CARD; // 消费直接到银行卡
	public static String TRAN_TYPE_KJ_JJ=TRAN_TYPE_CONSUME_QUICK_DEBIT; // 消费快捷借记卡
	public static String TRAN_TYPE_KJ_DJ=TRAN_TYPE_CONSUME_QUICK_CREBIT; // 消费快捷贷记卡
	public static String TRAN_TYPE_DS="15"; // 代收费率
	public static String TRAN_TYPE_DF="16"; // 代付费率
	public static String TRAN_TYPE_ZFB_SCAN="17"; // 支付宝扫码费率
	public static String TRAN_TYPE_QQ_SCAN="27"; // QQ扫码费率
	public static String TRAN_TYPE_WXWAP="18"; // 微信WAP费率
	// ===========================================
	public static String [] TRAN_TYPE_CONSUMES = {"虚拟账户","网银"};
	
	//订单状态 00:未处理   01:提现处理中  02:提现成功  03:提现失败  04:已退回支付账号  05:重新提现  99:超时 默认00
	public static String WITHDRAW_TYPE_SUCCESS="02";
	public static String WITHDRAW_TYPE_ERROR="03";
	public static String WITHDRAW_TYPE_RETURN="04";
	
	//转账状态 00:待付款 01:转账成功 02:转账失败 50 金额错误 51:订单号重复 52：收款方账号不存在 53：不能给自己转账
	public static String TRANSFER_STATUS_SUCCESS = "01";
	public static String TRANSFER_STATUS_ERROR="02";
	
	//转账类型 1转账 2担保收款 3担保付款
	public static String TRANSFER_TYPE_TRANSFER="1";
	//账户状态 0-正常；1-未激活；2-冻结；9-已销户
	public static Map<String, String> USER_ACC_STATUS = new HashMap();
	public static void init(){}
	static {
		try {loadPayParamenter();} catch (Exception e) {e.printStackTrace();}
		USER_ACC_STATUS.put("0","正常");
		USER_ACC_STATUS.put("1","未激活");
		USER_ACC_STATUS.put("2","冻结");
		USER_ACC_STATUS.put("9","已销户");
		
		ORDER_SQL_NAME.put("ORDER_DB_BAK_NAME", "JWEB_DBPool_BAK");
		
		PAY_STATUS.put("00","等待付款");
		PAY_STATUS.put("01","付款成功");
		PAY_STATUS.put("02","付款失败");
		PAY_STATUS.put("03","退款申请");
		PAY_STATUS.put("04","等待退款");
		PAY_STATUS.put("05","退款成功");
		PAY_STATUS.put("06","退款失败");
		PAY_STATUS.put("07","同意撤销");
		PAY_STATUS.put("08","拒绝撤销");
		PAY_STATUS.put("09","撤销成功");
		PAY_STATUS.put("10","撤销失败");
		PAY_STATUS.put("12","预付款");
		PAY_STATUS.put("17","实付审核中");
		PAY_STATUS.put("18","实付审核通过");
		PAY_STATUS.put("19","实付审核拒绝");
		PAY_STATUS.put("99","超时");
		
		REFUND_STATUS.put("00","退款处理中");
		REFUND_STATUS.put("01","退款成功");
		REFUND_STATUS.put("02","退款失败");
		
		MER_TYPE.put("0","一般商户");
		MER_TYPE.put("1","平台商户");
		MER_TYPE.put("2","担保商户");
		
		USER_INTERFLOW_FLAG.put("1", "是");
		USER_INTERFLOW_FLAG.put("0", "否");
		
		MER_STL_PERIOD.put("D","日结");
		MER_STL_PERIOD.put("W","周结");
		MER_STL_PERIOD.put("M","月结");
		
		
		CERT_TYPE.put("01","身份证");
		CERT_TYPE.put("02","军官证");
		CERT_TYPE.put("03","护照");
		CERT_TYPE.put("04","回乡证");
		CERT_TYPE.put("05","台胞证");
		CERT_TYPE.put("06","警官证");
		CERT_TYPE.put("07","士兵证");
		CERT_TYPE.put("99","其他");
		
		
		TRAN_TYPE.put(TRAN_TYPE_CONSUME,"消费");
		TRAN_TYPE.put(TRAN_TYPE_RECHARGE,"充值");
		TRAN_TYPE.put(TRAN_TYPE_SETTLE,"结算");
		TRAN_TYPE.put(TRAN_TYPE_REFUND,"退款");
		TRAN_TYPE.put(TRAN_TYPE_WITHDRAW,"提现");
		TRAN_TYPE.put(TRAN_TYPE_TRANSFER,"转账");
		TRAN_TYPE.put(TRAN_TYPE_CARD,"卡限额");
		
		RISK_USER_TYPE.put("1","实名");
		RISK_USER_TYPE.put("2","非实名");
		RISK_USER_TYPE.put("3","指定用户");
		RISK_USER_TYPE.put("4","银行卡");
		
		//系统X名单类型，1白 2灰 3黑 4红
		X_LIST_TYPE.put("1","白名单");
		X_LIST_TYPE.put("2","灰名单");
		X_LIST_TYPE.put("3","黑名单");
		X_LIST_TYPE.put("4","红名单");
		
		RESP_CODE_DESC.put("-1", "未知错误");
		RESP_CODE_DESC.put("000", "处理成功");
		RESP_CODE_DESC.put("001", "信息格式非法");
		RESP_CODE_DESC.put("002", "验证签名失败");
		RESP_CODE_DESC.put("003", "非法商户");
		RESP_CODE_DESC.put("004", "商户被冻结");
		RESP_CODE_DESC.put("005", "订单号重复");
		RESP_CODE_DESC.put("006", "");
		RESP_CODE_DESC.put("007", "商户未开启");
		RESP_CODE_DESC.put("008", "商品订单不存在");
		RESP_CODE_DESC.put("009", "清结算已经完成");
		RESP_CODE_DESC.put("010", "订单未支付");
		RESP_CODE_DESC.put("011", "退款金额大于支付金额");
		RESP_CODE_DESC.put("012", "订单已退款");
		RESP_CODE_DESC.put("013", "非本日订单");
		RESP_CODE_DESC.put("014", "订单状态错误");
		RESP_CODE_DESC.put("015", "商户代码非法");
		RESP_CODE_DESC.put("016", "商户名称非法");
		RESP_CODE_DESC.put("017", "订单号非法");
		RESP_CODE_DESC.put("018", "订单金额非法");
		RESP_CODE_DESC.put("019", "订单描述非法");
		RESP_CODE_DESC.put("020", "商户通知地址非法");
		RESP_CODE_DESC.put("021", "Url地址非法");
		RESP_CODE_DESC.put("022", "手机号码非法");
		RESP_CODE_DESC.put("023", "商户未通过审核");
		RESP_CODE_DESC.put("024", "清结算期间不能退款");
		RESP_CODE_DESC.put("025", "请求日期非法");
		RESP_CODE_DESC.put("026", "证件类型非法");
		RESP_CODE_DESC.put("027", "证件号码非法");
		RESP_CODE_DESC.put("028", "附加信息非法");
		RESP_CODE_DESC.put("029", "订单时间非法");
		RESP_CODE_DESC.put("030", "商户业务类型非法");
		RESP_CODE_DESC.put("031", "收款方式非法");
		RESP_CODE_DESC.put("032", "付款类型非法");
		RESP_CODE_DESC.put("033", "清结算期间不能撤销");
		RESP_CODE_DESC.put("034", "金额非法");
		RESP_CODE_DESC.put("035", "账号名称非法");
		RESP_CODE_DESC.put("036", "邮箱非法");
		RESP_CODE_DESC.put("037", "账户类型非法");
		RESP_CODE_DESC.put("038", "银行编码非法");
		RESP_CODE_DESC.put("039", "违反风控规则");
		RESP_CODE_DESC.put("040", "买家ID非法");
		RESP_CODE_DESC.put("041", "卖家ID非法");
		RESP_CODE_DESC.put("042", "担保金额非法");
		RESP_CODE_DESC.put("043", "只支持全额担保");
		RESP_CODE_DESC.put("044", "担保卖家不存在");
		RESP_CODE_DESC.put("045", "黑名单用户");
		RESP_CODE_DESC.put("046", "用户状态异常");
		RESP_CODE_DESC.put("047", "非担保支付订单");
		RESP_CODE_DESC.put("048", "担保通知状态非法");
		RESP_CODE_DESC.put("049", "担保已结束");
		RESP_CODE_DESC.put("050", "");
		RESP_CODE_DESC.put("051", "用户ID非法");
		RESP_CODE_DESC.put("052", "银行编码非法");
		RESP_CODE_DESC.put("053", "银行名称非法");
		RESP_CODE_DESC.put("054", "账户号非法");
		RESP_CODE_DESC.put("055", "信用卡信息非法");
		RESP_CODE_DESC.put("056", "验证码非法");
		RESP_CODE_DESC.put("057", "非快捷支付订单");
		RESP_CODE_DESC.put("058", "渠道不支持重发短信");
		RESP_CODE_DESC.put("059", "渠道暂未开通");
		RESP_CODE_DESC.put("060", "渠道不支持支付确认");
		RESP_CODE_DESC.put("061", "Connection refused,invalid client!");
		RESP_CODE_DESC.put("062", "不支持的交易类型");
		RESP_CODE_DESC.put("063", "实名认证书超限（最多1000条/次）");
		RESP_CODE_DESC.put("064", "认证明细号重复");
		RESP_CODE_DESC.put("065", "交易号/认证明细重复");
		RESP_CODE_DESC.put("066", "银行通用名非法");
		RESP_CODE_DESC.put("067", "开户行名称非法");
		RESP_CODE_DESC.put("068", "行号非法");
		RESP_CODE_DESC.put("069", "明细号非法");
		RESP_CODE_DESC.put("070", "收付款类型（0收款，1付款）非法");
		RESP_CODE_DESC.put("071", "企业账号非法");
		RESP_CODE_DESC.put("072", "公私类型错误");
		RESP_CODE_DESC.put("073", "备注非法");
		RESP_CODE_DESC.put("074", "处理中");
		RESP_CODE_DESC.put("075", "收付款笔数非法");
		RESP_CODE_DESC.put("076", "交易请求号非法");
		RESP_CODE_DESC.put("077", "快捷交易额不能低于");
		RESP_CODE_DESC.put("078", "");
		PayBizTypeService.loadBizTypeList();
		TRANSFER_ACC_ERROR.put("50","金额错误");
		TRANSFER_ACC_ERROR.put("51","订单号重复");
		TRANSFER_ACC_ERROR.put("52","收款方账号不存在");
		TRANSFER_ACC_ERROR.put("53","不能给自己转账");
		TRANSFER_ACC_ERROR.put("54","");
		TRANSFER_ACC_ERROR.put("55","");
		TRANSFER_ACC_ERROR.put("56","");
		TRANSFER_ACC_ERROR.put("57","");
		TRANSFER_ACC_ERROR.put("58","");
		TRANSFER_ACC_ERROR.put("59","");
		
		CARD_BIN_TYPE.put(CARD_BIN_TYPE_JIEJI,"储蓄卡");
		CARD_BIN_TYPE.put(CARD_BIN_TYPE_DAIJI,"信用卡");
		CARD_BIN_TYPE.put(CARD_BIN_TYPE_ZHUNDAIJI,"准贷记卡");
		CARD_BIN_TYPE.put(CARD_BIN_TYPE_YUFU,"预付卡");
		
	}
	/**
	 * 恢复系统默认参数
	 * @throws Exception
	 */
	public static void loadPayParamenter() throws Exception {
		try {
			PAY_CONFIG = PropertyReader.getHolder("resources/business_parameter.properties",true);
			log.info("业务参数===============");
			Iterator it = PAY_CONFIG.keySet().iterator();
			while(it.hasNext()){
				Object key = it.next();
				log.info(key+"="+PAY_CONFIG.get(key));
			}
			if(PAY_CONFIG==null)throw new Exception("读取系统默认参数文件错误！");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
