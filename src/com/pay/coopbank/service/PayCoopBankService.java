package com.pay.coopbank.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.JWebUser;
import com.pay.bank.dao.PayBank;
import com.pay.business.dao.PayBusinessParameter;
import com.pay.business.dao.PayBusinessParameterDAO;
import com.pay.coopbank.dao.PayChannelBankRelation;
import com.pay.coopbank.dao.PayChannelBankRelationDAO;
import com.pay.coopbank.dao.PayChannelMaxAmt;
import com.pay.coopbank.dao.PayChannelMaxAmtDAO;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.dao.PayCoopBankDAO;
import com.pay.coopbank.dao.PayCoopBankRouteRule;
import com.pay.coopbank.dao.PayCoopBankRouteRuleDAO;
import com.pay.coopbank.dao.PayCoopBankRouteRuleCache;
import com.pay.coopbank.dao.PayCoopBankRouteRuleCacheDAO;
import com.pay.fee.service.PayFeeRateService;
/**
 * Object PAY_COOP_BANK service. 
 * @author Administrator
 *
 */
public class PayCoopBankService {
	//支持银行列表
	public static List<PayBank> BANK_CODE_NAME_LIST = new ArrayList<PayBank>();	
	public static Map<String, String>BANK_CODE_NAME_MAP = new HashMap<String, String>();
	//合作渠道列表(开启)
	public static List<PayCoopBank> CHANNEL_LIST = new ArrayList<PayCoopBank>();
	public static Map<String, PayCoopBank> CHANNEL_MAP = new HashMap<String, PayCoopBank>();
	//合作渠道列表(所有)
	public static List<PayCoopBank> CHANNEL_LIST_ALL = new ArrayList<PayCoopBank>();
	public static Map<String, PayCoopBank> CHANNEL_MAP_ALL = new HashMap<String, PayCoopBank>();
	//网银支持银行列表
	public static List <PayBank>SUPPORTED_BANK_LIST = new ArrayList<PayBank>();
	//网银支持银行列表（实时结算）
	public static List <PayBank>SUPPORTED_BANK_LIST_REAL_TIME = new ArrayList<PayBank>();
	//快捷支持银行列表
	public static List <PayBank>SUPPORTED_BANK_LIST_QUICK = new ArrayList<PayBank>();
	//快捷支持银行列表（实时结算）
	public static List <PayBank>SUPPORTED_BANK_LIST_REAL_TIME_QUICK = new ArrayList<PayBank>();
	//渠道路由缓存(key:“yyyyMMdd,渠道号,交易类型”)
	private static Map<String, PayCoopBankRouteRuleCache> ROUTE_RULE_CACHE = new HashMap<String, PayCoopBankRouteRuleCache>();
	//渠道路由规则(key:“规则类型,渠道号,交易类型”)
	public static Map<String, PayCoopBankRouteRule> ROUTE_RULE = new HashMap<String, PayCoopBankRouteRule>();
	//路由规则和接口交易类型对应关系，路由规则交易类型：7网银借记卡,8网银贷记卡,9对公,11快捷借记卡,12快捷贷记卡,15代收,16代付
	public static  Map<String, String> ROUTE_RULE_TRAN_TYPE_MAP = new HashMap<String, String>();
	//交易类型，-1全部,7网银借记卡,8网银贷记卡,9对公,11快捷借记卡,12快捷贷记卡,15代收,16代付
	public static String [] tranTypes = {"7","8","9","11","12","15","16"};
	//商户指定渠道(key:【merno,tranType】,value:【channelId】)
	public static Map<String, String> MERCHANT_CHANNEL_MAP = new HashMap<String, String>();
	static {
		loadCoopBank();
		loadSupportedBank();
		loadRouteRule();
		loadMerchantChannelRelation();
		ROUTE_RULE_TRAN_TYPE_MAP.put("web,0","7");
		ROUTE_RULE_TRAN_TYPE_MAP.put("web,1","8");
		ROUTE_RULE_TRAN_TYPE_MAP.put("web,4","9");
		ROUTE_RULE_TRAN_TYPE_MAP.put("quick,0","11");
		ROUTE_RULE_TRAN_TYPE_MAP.put("quick,1","12");
	}
	public static void loadCoopBank(){
		PayCoopBankDAO dao = new PayCoopBankDAO();
		try {
			//初始化渠道信息COOP_BANK_LIST、COOP_BANK_MAP
			CHANNEL_LIST = dao.getList();
			CHANNEL_LIST_ALL = dao.getListAll();
			//初始化BANK_CODE_NAME_LIST、BANK_CODE_NAME_MAP
			BANK_CODE_NAME_LIST = dao.getBankList();
			util.PayUtil.synNotifyForAllNode("1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadSupportedBank(){
		try {
			PayCoopBankDAO dao = new PayCoopBankDAO();
			SUPPORTED_BANK_LIST = dao.getSuppertedBank();
			SUPPORTED_BANK_LIST_REAL_TIME = dao.getSuppertedBankRealTime();
			SUPPORTED_BANK_LIST_QUICK = dao.getSuppertedQuickBank();
			SUPPORTED_BANK_LIST_REAL_TIME_QUICK = dao.getSuppertedQuickBankRealTime();
			util.PayUtil.synNotifyForAllNode("2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadRouteRule(){
		try {
			PayCoopBankRouteRuleDAO dao = new PayCoopBankRouteRuleDAO();
			ROUTE_RULE = dao.getRouteRule();
			//每种交易类型每个渠道笔数（PayCoopBankService.CHANNEL_LIST_ALL.size()）每种交易类型总数（1）
			if(ROUTE_RULE.size()!=(PayCoopBankService.CHANNEL_LIST_ALL.size()+1)*tranTypes.length)
				ROUTE_RULE = dao.addPayCoopBankRouteRuleBatch();
			util.PayUtil.synNotifyForAllNode("3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadMerchantChannelRelation(){
		try {
			MERCHANT_CHANNEL_MAP = new PayCoopBankDAO().getMerchantChannelRelation();
			util.PayUtil.synNotifyForAllNode("4");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Map<String,PayCoopBankRouteRuleCache> getRouteRuleCache(){
		String curDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		PayCoopBankRouteRuleCacheDAO dao = new PayCoopBankRouteRuleCacheDAO();
		try {
			//当天不存在，数据库中获取(通过某个交易类型“网银借记卡”判断，也可以通过其他类型8/9...)
			if(ROUTE_RULE_CACHE.get(curDate+",-1,7")==null){
				ROUTE_RULE_CACHE = dao.getRouteRule(curDate);
				//数据库不存在，生成新数据，插入数据库
				if(ROUTE_RULE_CACHE.size()==0)ROUTE_RULE_CACHE = dao.addPayCoopBankRouteRuleCacheBatch(curDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ROUTE_RULE_CACHE;
	}
    /**
     * Get records list(json).
     * @return
     */
    public String getPayCoopBankList(PayCoopBank payCoopBank,int page,int rows,String sort,String order){
        try {
            PayCoopBankDAO payCoopBankDAO = new PayCoopBankDAO();
            List list = payCoopBankDAO.getPayCoopBankList(payCoopBank, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payCoopBankDAO.getPayCoopBankCount(payCoopBank)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayCoopBank)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * detail PayCoopBank
     * @param bankCode
     * @throws Exception
     */
    public PayCoopBank detailPayCoopBank(String bankCode) throws Exception {
        return new PayCoopBankDAO().detailPayCoopBank(bankCode);
    }
	public void updatePayCoopBankStatus(String bankCode,String columName, String operation) throws Exception {
		new PayCoopBankDAO().updatePayCoopBankStatus(bankCode,columName,operation);
		loadCoopBank();
		loadSupportedBank();
		PayFeeRateService.loadFee();
		PayFeeRateService.loadChannelFeeRate();
	}
	/**
	 * 最大额存储
	 * @param payCoopBank
	 * @param request
	 * @throws Exception
	 */
	public void insertPayChannelMaxAmt(PayCoopBank payCoopBank,HttpServletRequest request) throws Exception{
		String [] nums = {"7","8","9","11","12","17","10","18","15","27"};
		PayChannelMaxAmtDAO dao = new PayChannelMaxAmtDAO();
		for(int i=0; i<nums.length; i++){
			String tmp = request.getParameter("PAY_CHANNEL_MAX_AMT_"+nums[i]);
	        if( tmp != null && !"".equals(tmp)) {
				PayChannelMaxAmt payChannelMaxAmt = new PayChannelMaxAmt();
				payChannelMaxAmt.id = Tools.getUniqueIdentify();
				payChannelMaxAmt.bankCode = payCoopBank.bankCode;
				payChannelMaxAmt.tranType = nums[i];
				payChannelMaxAmt.maxAmt = Long.parseLong(tmp)*100;
				payChannelMaxAmt.createTime = new Date();
				dao.addPayChannelMaxAmt(payChannelMaxAmt);
			}
		}
	}
	/**
	 * 添加银行渠道
	 * @param payCoopBank
	 * @param parameterValues
	 * @throws Exception 
	 */
	public void addPayCoopBank(PayCoopBank payCoopBank,HttpServletRequest request) throws Exception {
		//支持标识
		StringBuffer flag = new StringBuffer();
        String addSupportedZFBScanFlag = request.getParameter("addSupportedZFBScanFlag");
        String addSupportedWeixinScanFlag = request.getParameter("addSupportedWeixinScanFlag");
        String addSupportedWeixinWAPFlag = request.getParameter("addSupportedWeixinWAPFlag");
        String addSupportedQQScanFlag = request.getParameter("addSupportedQQScanFlag");
        //标识有值
    	if("1".equals(addSupportedZFBScanFlag)) flag.append(addSupportedZFBScanFlag+",");
    	else flag.append("0,");
    	if("1".equals(addSupportedWeixinScanFlag)) flag.append(addSupportedWeixinScanFlag+",");
    	else flag.append("0,");
    	if("1".equals(addSupportedWeixinWAPFlag)) flag.append(addSupportedWeixinWAPFlag+",");
    	else flag.append("0,");
    	if("1".equals(addSupportedQQScanFlag)) flag.append(addSupportedQQScanFlag+",");
    	else flag.append("0,");
    	String tmp = flag.toString();
    	if(tmp.length()>0) tmp=tmp.substring(0,flag.lastIndexOf(","));
    	payCoopBank.payScanFlag = tmp;
		// 更新银行渠道关系
		updateBankRelation(payCoopBank,request);
		//最大额存储
		insertPayChannelMaxAmt(payCoopBank, request);
		//渠道存储
		new PayCoopBankDAO().addPayCoopBank(payCoopBank);
		// 重新载入内存信息
        loadCoopBank();
        loadSupportedBank();
        loadRouteRule();
        PayFeeRateService.loadFee();
        PayFeeRateService.loadChannelFeeRate();
	}
	
	/**
	 * 
	 * @param payCoopBank
	 * @param parameterValues
	 * @throws Exception 
	 */
	public void updatePayCoopBank(PayCoopBank payCoopBank,HttpServletRequest request) throws Exception {
		//支持标识
		StringBuffer flag = new StringBuffer();
        String updateSupportedZFBScanFlag = request.getParameter("updateSupportedZFBScanFlag");
        String updateSupportedWeixinScanFlag = request.getParameter("updateSupportedWeixinScanFlag");
        String updateSupportedWeixinWAPFlag = request.getParameter("updateSupportedWeixinWAPFlag");
        String updateSupportedQQScanFlag = request.getParameter("updateSupportedQQScanFlag");
        //标识有值
        	if("1".equals(updateSupportedZFBScanFlag)) flag.append(updateSupportedZFBScanFlag+",");
        	else flag.append("0,");
        	if("1".equals(updateSupportedWeixinScanFlag)) flag.append(updateSupportedWeixinScanFlag+",");
        	else flag.append("0,");
        	if("1".equals(updateSupportedWeixinWAPFlag)) flag.append(updateSupportedWeixinWAPFlag+",");
        	else flag.append("0,");
        	if("1".equals(updateSupportedQQScanFlag)) flag.append(updateSupportedQQScanFlag+",");
        	else flag.append("0,");
        	String tmp = flag.toString();
        	if(tmp.length()>0) tmp=tmp.substring(0,flag.lastIndexOf(","));
        	payCoopBank.payScanFlag = tmp;
		// 更新银行渠道关系
		updateBankRelation(payCoopBank, request);
		//更新最大额
		new PayChannelMaxAmtDAO().removePayChannelMaxAmt(payCoopBank.bankCode);
		insertPayChannelMaxAmt(payCoopBank, request);
		// 更新渠道信息
		new PayCoopBankDAO().updatePayCoopBank(payCoopBank);
		// 重新载入内存
        loadCoopBank();
        loadSupportedBank();
        PayFeeRateService.loadFee();
        PayFeeRateService.loadChannelFeeRate();
	}
	
	/**
	 * 更新银行渠道关系
	 * @param payCoopBank
	 * @param parameterValues
	 * @throws Exception
	 */
	private void updateBankRelation(PayCoopBank payCoopBank,HttpServletRequest request) throws Exception {
		// 删除原有银行关联数据
		PayChannelBankRelationDAO bankRelationDAO = new PayChannelBankRelationDAO();
		bankRelationDAO.removePayChannelBankRelation(payCoopBank.bankCode);
		Map<String, PayChannelBankRelation> allBanks = new HashMap<String, PayChannelBankRelation>();
		// 处理获取的最新数据，进行银行数据关系的存储
		//网银支付
		String [] payBankCodes = request.getParameterValues("payBankCodes");
		if(payBankCodes!=null){
			for (String values : payBankCodes) {
				String[] split = values.split(",");
				for (String bankCode : split) {
					PayChannelBankRelation bankRelation = new PayChannelBankRelation();
					bankRelation.id = Tools.getUniqueIdentify();
					bankRelation.bankCode = bankCode;
					bankRelation.channelCode = payCoopBank.bankCode;
					String jCard = request.getParameter(bankRelation.bankCode+"UserType0");
					String dCard = request.getParameter(bankRelation.bankCode+"UserType1");
					String pCard = request.getParameter(bankRelation.bankCode+"UserType4");
					String jMaxAmt = request.getParameter("webDebitMaxAmt_"+bankRelation.bankCode);
					String dMaxAmt = request.getParameter("webCreditMaxAmt_"+bankRelation.bankCode);
					String pMaxAmt = request.getParameter("webPublicMaxAmt_"+bankRelation.bankCode);
					String sut = "";
					if("0".equals(jCard)){
						sut+=(jCard+",");
						if(jMaxAmt != null && !"".endsWith(jMaxAmt)){
							long webDebitMaxAmt = Long.parseLong(jMaxAmt);
							if(webDebitMaxAmt > 0) bankRelation.setWebDebitMaxAmt(webDebitMaxAmt*100);
						}
					}
					if("1".equals(dCard)){
						sut+=(dCard+",");
						if(dMaxAmt != null && !"".equals(dMaxAmt)){
							long webCreditMaxAmt = Long.parseLong(dMaxAmt);
							if(webCreditMaxAmt > 0) bankRelation.setWebCreditMaxAmt(webCreditMaxAmt*100);
						}
					}
					if("4".equals(pCard)){
						sut+=(pCard+",");
						if(pMaxAmt != null && !"".equals(pMaxAmt)){
							long webPublicMaxAmt = Long.parseLong(pMaxAmt);
							if(webPublicMaxAmt > 0) bankRelation.setWebPublicMaxAmt(webPublicMaxAmt*100);
						}
					}
					bankRelation.supportedUserType = sut;
					allBanks.put(bankCode+","+payCoopBank.bankCode, bankRelation);
                }
			}
		}
		//快捷支付
		String [] payBankCodesQuick = request.getParameterValues("payBankCodesQuick");
		if(payBankCodesQuick!=null){
			for (String values : payBankCodesQuick) {
				String[] split = values.split(",");
				for (String bankCode : split) {
					PayChannelBankRelation bankRelation = allBanks.get(bankCode+","+payCoopBank.bankCode);
					if(bankRelation==null){
						bankRelation=new PayChannelBankRelation();
						bankRelation.id = Tools.getUniqueIdentify();
						bankRelation.bankCode = bankCode;
						bankRelation.channelCode = payCoopBank.bankCode;
						allBanks.put(bankCode+","+payCoopBank.bankCode, bankRelation);
					}
					String jCard = request.getParameter(bankRelation.bankCode+"UserTypeQuick0");
					String dCard = request.getParameter(bankRelation.bankCode+"UserTypeQuick1");
					String jMaxAmt = request.getParameter("quickDebitMaxAmt_"+bankRelation.bankCode);
					String dMaxAmt = request.getParameter("quickCeditMaxAmt_"+bankRelation.bankCode);
					String sut = "";
					if("0".equals(jCard)){
						sut+=(jCard+",");
						if(jMaxAmt != null && !"".equals(jMaxAmt)){
							long quickDebitMaxAmt = Long.parseLong(jMaxAmt);
							if(quickDebitMaxAmt > 0) bankRelation.setQuickDebitMaxAmt(quickDebitMaxAmt*100);
						}
					}
					if("1".equals(dCard)){
						sut+=(dCard+",");
						if(dMaxAmt != null && !"".equals(dMaxAmt)){
							long quickCeditMaxAmt = Long.parseLong(dMaxAmt);
							if(quickCeditMaxAmt > 0) bankRelation.setQuickCeditMaxAmt(quickCeditMaxAmt*100);
						}
					}
					bankRelation.quickUserType = sut;
				}
			}
		}
		//代收
		String [] payBankCodesReceive = request.getParameterValues("payBankCodesReceive");
		if(payBankCodesReceive!=null){
			for (String values : payBankCodesReceive) {
				String[] split = values.split(",");
				for (String bankCode : split) {
					PayChannelBankRelation bankRelation = allBanks.get(bankCode+","+payCoopBank.bankCode);
					if(bankRelation==null){
						bankRelation=new PayChannelBankRelation();
						bankRelation.id = Tools.getUniqueIdentify();
						bankRelation.bankCode = bankCode;
						bankRelation.channelCode = payCoopBank.bankCode;
						allBanks.put(bankCode+","+payCoopBank.bankCode, bankRelation);
					}
					String rMaxAmt = request.getParameter("receiveMaxAmt_"+bankRelation.bankCode);
					if(rMaxAmt != null && !"".equals(rMaxAmt)) {
						long receiveMaxAmt = Long.parseLong(rMaxAmt);
						if(receiveMaxAmt > 0 ) bankRelation.setReceiveMaxAmt(receiveMaxAmt*100);
					}
					bankRelation.receiveUserType="0";
				}
			}
		}
		//代付
		String [] payBankCodesWithdraw = request.getParameterValues("payBankCodesWithdraw");
		if(payBankCodesWithdraw!=null){
			for (String values : payBankCodesWithdraw) {
				String[] split = values.split(",");
				for (String bankCode : split) {
					PayChannelBankRelation bankRelation = allBanks.get(bankCode+","+payCoopBank.bankCode);
					if(bankRelation==null){
						bankRelation=new PayChannelBankRelation();
						bankRelation.id = Tools.getUniqueIdentify();
						bankRelation.bankCode = bankCode;
						bankRelation.channelCode = payCoopBank.bankCode;
						allBanks.put(bankCode+","+payCoopBank.bankCode, bankRelation);
					}
					bankRelation.withdrawUserType="0";
				}
			}
		}
		Iterator<String> it = allBanks.keySet().iterator();
		while(it.hasNext()) {
			Object key = it.next();
			PayChannelBankRelation bankRelation = allBanks.get(key);
			bankRelationDAO.addPayChannelBankRelation(bankRelation);
		}
	}
	/**
	 * 获取支持的银行列表
	 * @return
	 */
	public String getPayBankList() {
		 try {
	            List<PayCoopBank> list = new PayCoopBankDAO().getPayBankList();
	            JSONArray row = new JSONArray();
	            for(int i = 0; i<list.size(); i++){
	                row.put(i, ((PayCoopBank)list.get(i)).toJson());
	            }
	            return row.toString();
	        } catch (Exception e) {
	            e.printStackTrace();
        }
        return "";
	}
	/**
	 * 根据银行编码查询重复性
	 * @param bankCode
	 * @return
	 * @throws Exception 
	 */
	public long selectByBankCode(String bankCode) throws Exception {
		return new PayCoopBankDAO().selectByBankCode(bankCode);
	}
	/**
	 * 查询支付渠道路由规则列表
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PayCoopBankRouteRule> adjustChannelRule() throws Exception {
		return new PayCoopBankRouteRuleDAO().getList();
	}
	/**
	 * 调整渠道路由权值
	 * @param request
	 * @throws Exception 
	 */
	public void adjustChannelRouteRule(HttpServletRequest request) throws Exception {
		JWebUser user = (JWebUser)request.getSession().getAttribute("user");
		//调整交易平均分配规则
		List<PayCoopBankRouteRule> list = new ArrayList<PayCoopBankRouteRule>();
        long b2cJCount = Long.valueOf(request.getParameter("channelRouteRuleJJCount")).longValue();
        long b2cJMaxAmt = Long.valueOf(request.getParameter("channelRouteRuleJJAmt")).longValue();
        PayCoopBankRouteRule bankRouteRule1 = new PayCoopBankRouteRule();
        bankRouteRule1.setDivisionCount(b2cJCount);
        bankRouteRule1.setDivisionMaxAmt(b2cJMaxAmt*100);
        bankRouteRule1.setRuleType("0");
        bankRouteRule1.setTranType("7");
        bankRouteRule1.setId(Tools.getUniqueIdentify());
        bankRouteRule1.setCreateId(user.id);
        
        long b2cDCount = Long.valueOf(request.getParameter("channelRouteRuleDJCount")).longValue();
        long b2cDMaxAmt = Long.valueOf(request.getParameter("channelRouteRuleDJAmt")).longValue();
        PayCoopBankRouteRule bankRouteRule2 = new PayCoopBankRouteRule();
        bankRouteRule2.setDivisionCount(b2cDCount);
        bankRouteRule2.setDivisionMaxAmt(b2cDMaxAmt*100);
        bankRouteRule2.setRuleType("0");
        bankRouteRule2.setTranType("8");
        bankRouteRule2.setId(Tools.getUniqueIdentify());
        bankRouteRule2.setCreateId(user.id);
        
        long b2bJCount = Long.valueOf(request.getParameter("channelRouteRulePubCount")).longValue();
        long b2bJMaxAmt = Long.valueOf(request.getParameter("channelRouteRulePubAmt")).longValue();
        PayCoopBankRouteRule bankRouteRule3 = new PayCoopBankRouteRule();
        bankRouteRule3.setDivisionCount(b2bJCount);
        bankRouteRule3.setDivisionMaxAmt(b2bJMaxAmt*100);
        bankRouteRule3.setRuleType("0");
        bankRouteRule3.setTranType("9");
        bankRouteRule3.setId(Tools.getUniqueIdentify());
        bankRouteRule3.setCreateId(user.id);
        
        long kauiJCount = Long.valueOf(request.getParameter("channelRouteRuleQuickJJCount")).longValue();
        long kuaiJMaxAmt = Long.valueOf(request.getParameter("channelRouteRuleQuickJJAmt")).longValue();
        PayCoopBankRouteRule bankRouteRule4 = new PayCoopBankRouteRule();
        bankRouteRule4.setDivisionCount(kauiJCount);
        bankRouteRule4.setDivisionMaxAmt(kuaiJMaxAmt*100);
        bankRouteRule4.setRuleType("0");
        bankRouteRule4.setTranType("11");
        bankRouteRule4.setId(Tools.getUniqueIdentify());
        bankRouteRule4.setCreateId(user.id);
        
        long kauiDCount = Long.valueOf(request.getParameter("channelRouteRuleQuickDJCount")).longValue();
        long kuaiDMaxAmt = Long.valueOf(request.getParameter("channelRouteRuleQuickDJAmt")).longValue();
        PayCoopBankRouteRule bankRouteRule5 = new PayCoopBankRouteRule();
        bankRouteRule5.setDivisionCount(kauiDCount);
        bankRouteRule5.setDivisionMaxAmt(kuaiDMaxAmt*100);
        bankRouteRule5.setRuleType("0");
        bankRouteRule5.setTranType("12");
        bankRouteRule5.setId(Tools.getUniqueIdentify());
        bankRouteRule5.setCreateId(user.id);
        
        long daiSCount = Long.valueOf(request.getParameter("channelRouteRuleReceiveCount")).longValue();
        long daiSMaxAmt = Long.valueOf(request.getParameter("channelRouteRuleReceiveAmt")).longValue();
        PayCoopBankRouteRule bankRouteRule6 = new PayCoopBankRouteRule();
        bankRouteRule6.setDivisionCount(daiSCount);
        bankRouteRule6.setDivisionMaxAmt(daiSMaxAmt*100);
        bankRouteRule6.setRuleType("0");
        bankRouteRule6.setTranType("15");
        bankRouteRule6.setId(Tools.getUniqueIdentify());
        bankRouteRule6.setCreateId(user.id);
        
        long daiFCount = Long.valueOf(request.getParameter("channelRouteRulePayCount")).longValue();
        long daiFMaxAmt = Long.valueOf(request.getParameter("channelRouteRulePayAmt")).longValue();
        PayCoopBankRouteRule bankRouteRule7 = new PayCoopBankRouteRule();
        bankRouteRule7.setDivisionCount(daiFCount);
        bankRouteRule7.setDivisionMaxAmt(daiFMaxAmt*100);
        bankRouteRule7.setRuleType("0");
        bankRouteRule7.setTranType("16");
        bankRouteRule7.setId(Tools.getUniqueIdentify());
        bankRouteRule7.setCreateId(user.id);
        
        list.add(bankRouteRule1);
        list.add(bankRouteRule2);
        list.add(bankRouteRule3);
        list.add(bankRouteRule4);
        list.add(bankRouteRule5);
        list.add(bankRouteRule6);
        list.add(bankRouteRule7);
        //清空表
        new PayCoopBankRouteRuleDAO().removePayCoopBankRouteRuleAll();
        //插入调整后的交易规则数据
        for (PayCoopBankRouteRule payCoopBankRouteRule : list) {
        	new PayCoopBankRouteRuleDAO().addPayCoopBankRouteRuleTypeTrade(payCoopBankRouteRule);
		}
        
        //调整优先值规则（插入调整后的优先值规则数据）
        for (int i = 0; i < 7; i++) {
			for (int j=0; j<PayCoopBankService.CHANNEL_LIST.size(); j++) {
				PayCoopBank channel = PayCoopBankService.CHANNEL_LIST.get(j);
				PayCoopBankRouteRule routeRule = new PayCoopBankRouteRule();
				routeRule.setId(Tools.getUniqueIdentify());
				routeRule.setChannelCode(channel.bankCode);
				routeRule.setCreateTime(new Date());
				routeRule.setCreateId(user.id);
				if( i == 0){
					long channelRouteRuleJJWeight = Long.valueOf(request.getParameter("channelRouteRuleJJWeight_" + channel.bankCode)).longValue();
					routeRule.setWeight(channelRouteRuleJJWeight);
					routeRule.setTranType("7");
				}else if(i == 1){
					long channelRouteRuleDJWeight = Long.valueOf(request.getParameter("channelRouteRuleDJWeight_" + channel.bankCode)).longValue();
					routeRule.setWeight(channelRouteRuleDJWeight);
					routeRule.setTranType("8");
				}else if(i == 2){
					long channelRouteRulePubWeight = Long.valueOf(request.getParameter("channelRouteRulePubWeight_" + channel.bankCode)).longValue();
					routeRule.setWeight(channelRouteRulePubWeight);
					routeRule.setTranType("9");
				}else if(i == 3){
					long channelRouteRuleQuickJJWeight = Long.valueOf(request.getParameter("channelRouteRuleQuickJJWeight_" + channel.bankCode)).longValue();
					routeRule.setWeight(channelRouteRuleQuickJJWeight);
					routeRule.setTranType("11");
				}else if(i == 4){
					long channelRouteRuleQuickDJWeight = Long.valueOf(request.getParameter("channelRouteRuleQuickDJWeight_" + channel.bankCode)).longValue();
					routeRule.setWeight(channelRouteRuleQuickDJWeight);
					routeRule.setTranType("12");
				}else if(i == 5){
					long channelRouteRuleReceiveWeight = Long.valueOf(request.getParameter("channelRouteRuleReceiveWeight_" + channel.bankCode)).longValue();
					routeRule.setWeight(channelRouteRuleReceiveWeight);
					routeRule.setTranType("15");
				}else if(i == 6){
					long channelRouteRulePayWeight = Long.valueOf(request.getParameter("channelRouteRulePayWeight_" + channel.bankCode)).longValue();
					routeRule.setWeight(channelRouteRulePayWeight);
					routeRule.setTranType("16");
				}
				new PayCoopBankRouteRuleDAO().addPayCoopBankRouteRuleTypePrior(routeRule);
			}
		}
        PayBusinessParameterDAO payBusinessParameterDAO = new PayBusinessParameterDAO();
        //判断是否启用交易平均分配规则   0未启动  1启动 
        PayBusinessParameter payBusinessParameter = new PayBusinessParameter();
        payBusinessParameter.setName("CHANNEL_ROUTE_RULE_DIVISION_FLAG");
        String adjustChannelRouteRuleStart = request.getParameter("adjustChannelRouteRuleStart");
        if("".equals(adjustChannelRouteRuleStart) || adjustChannelRouteRuleStart == null)adjustChannelRouteRuleStart = "0";
        payBusinessParameter.setValue(adjustChannelRouteRuleStart);
        payBusinessParameterDAO.updatePayBusinessParameter(payBusinessParameter);
        //加入缓存
        PayConstant.PAY_CONFIG.put("CHANNEL_ROUTE_RULE_DIVISION_FLAG",adjustChannelRouteRuleStart);
        //优先规则设置
        payBusinessParameter.setName("CHANNEL_ROUTE_RULE_PRIORITY");
        String channelRouteRulePriority = request.getParameter("channelRouteRulePriority");
        if(!"0,1".equals(channelRouteRulePriority)&&!"1,0".equals(channelRouteRulePriority))channelRouteRulePriority = "0,1";
        payBusinessParameter.setValue(channelRouteRulePriority);
        payBusinessParameterDAO.updatePayBusinessParameter(payBusinessParameter);
        //加入缓存
        PayConstant.PAY_CONFIG.put("CHANNEL_ROUTE_RULE_PRIORITY",channelRouteRulePriority);
        loadRouteRule();
	}
	/**
	 * 根据渠道编号和交易类型查询最大额
	 * @param bankCode
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String detailPayChannelMaxAmt(String bankCode, String type) throws Exception {
		 PayChannelMaxAmt channelMaxAmt = new PayChannelMaxAmtDAO().detailPayChannelMaxAmt(bankCode,type);
		 return channelMaxAmt == null?"0":String.format("%.2f",(float)channelMaxAmt.maxAmt*0.01);
	}
			
}