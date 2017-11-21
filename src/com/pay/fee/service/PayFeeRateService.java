package com.pay.fee.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import util.JWebConstant;

import com.PayConstant;
import com.jweb.service.TransactionService;
import com.pay.cardbin.dao.PayCardBin;
import com.pay.cardbin.service.PayCardBinService;
import com.pay.coopbank.dao.PayChannelBankRelation;
import com.pay.coopbank.dao.PayChannelBankRelationDAO;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.dao.PayCoopBankRouteRule;
import com.pay.coopbank.dao.PayCoopBankRouteRuleCache;
import com.pay.coopbank.dao.PayCoopBankRouteRuleCacheDAO;
import com.pay.coopbank.service.PayCoopBankService;
import com.pay.fee.dao.PayFeeRate;
import com.pay.fee.dao.PayFeeRateDAO;
import com.pay.merchantinterface.service.PayRequest;

/**
 * Object PAY_FEE_RATE service. 
 * @author Administrator
 *
 */
public class PayFeeRateService extends TransactionService{
	private static final Log log = LogFactory.getLog(PayFeeRateService.class);
	public static Map <String,PayFeeRate>FEE_MAP = new HashMap<String,PayFeeRate>();
	public static Map <String,Long>CHANNEL_TRAN_MAX_AMT = new HashMap<String,Long>();
	private static Map<String,String> MER_CHANNEL_bankUserType = new HashMap<String,String>();
	/**
	 * 可用支付渠道费率表，key:【channel_id,tran_type】,value:PayFeeRate
	 */
	public static Map <String,PayFeeRate>CHANNEL_FEE_MAP = new HashMap<String,PayFeeRate>();
	static {
		initZeroFeeRate();
		loadFee();
		loadChannelFeeRate();
		//7消费B2C借记卡 8消费B2C信用卡 9消费B2B 10微信扫码 11快捷借记卡 12快捷贷记卡 13代理分润 14代理费率 15代收 16代付 17支付宝扫码 18微信WAP
		MER_CHANNEL_bankUserType.put("web,0","7");
		MER_CHANNEL_bankUserType.put("web,1","8");
		MER_CHANNEL_bankUserType.put("web,4","9");
		MER_CHANNEL_bankUserType.put("quick,0","11");
		MER_CHANNEL_bankUserType.put("quick,1","12");
	}
	public static void loadFee(){
		FEE_MAP = new HashMap();
		try {
			PayFeeRateDAO feeDao = new PayFeeRateDAO();
			List list = feeDao.getAllPayFeeRate();
			for(int i=0; i<list.size(); i++){
				PayFeeRate fee = (PayFeeRate)list.get(i);
				FEE_MAP.put(fee.feeCode, feeDao.getPayFeeRateById(fee.feeCode));
			}
			CHANNEL_TRAN_MAX_AMT = feeDao.getChannelTranMaxAmt();
			util.PayUtil.synNotifyForAllNode("6");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void loadChannelFeeRate(){
		try {
			CHANNEL_FEE_MAP = new PayFeeRateDAO().getChannelFeeRate();
			util.PayUtil.synNotifyForAllNode("7");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化零费率 为了保证客户费率存在，费率表必须有各类型客户（用户、商户、支付渠道）个交易类型零费率记录
	 */
	public static void initZeroFeeRate(){
		try {
			new PayFeeRateDAO().initZeroFeeRate();
			util.PayUtil.synNotifyForAllNode("5");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    /**
     * Get records list(json).
     * @return
     */
    public String getPayFeeRateList(PayFeeRate payFeeRate,int page,int rows,String sort,String order){
        try {
            PayFeeRateDAO payFeeRateDAO = new PayFeeRateDAO();
            List list = payFeeRateDAO.getPayFeeRateList(payFeeRate, page, rows, sort, order);
            JSONObject json = new JSONObject();
            json.put("total", String.valueOf(payFeeRateDAO.getPayFeeRateCount(payFeeRate)));
            JSONArray row = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                row.put(i, ((PayFeeRate)list.get(i)).toJson());
            }
            json.put("rows", row);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * add PayFeeRate
     * @param payFeeRate
     * @throws Exception
     */
    public String addPayFeeRate(PayFeeRate payFeeRate) {
    	PayFeeRateDAO payFeeRateDAO = new PayFeeRateDAO();
    	try {
	    	//取得费率信息
	    	if(PayConstant.CUST_TYPE_USER.equals(payFeeRate.custType) 
	    		&& payFeeRateDAO.getPayFeeRateByFeeInfo(payFeeRate.custType,payFeeRate.tranType))
	    		throw new Exception("该交易类型的用户费率已存在");
	    	payFeeRateDAO.addPayFeeRate(payFeeRate);
	    	loadFee();
	    	loadChannelFeeRate();
	    	PayCoopBankService.loadCoopBank();
			PayCoopBankService.loadSupportedBank();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
    	return JWebConstant.OK;
    }
    public PayFeeRate getPayFeeRateById(String feeCode) throws Exception{
    	return new PayFeeRateDAO().getPayFeeRateById(feeCode);
    }
    public List getAllPayFeeRate() throws Exception{
    	return new PayFeeRateDAO().getAllPayFeeRate();
    }
    public Map getAllPayFeeRateMap() throws Exception{
    	return new PayFeeRateDAO().getAllPayFeeRateMap();
    }
    /**
     * remove PayFeeRate
     * @param id
     * @throws Exception
     */
    public void removePayFeeRate(String feeCode) throws Exception {
    	PayFeeRateDAO feeRateDao = new PayFeeRateDAO();
    	try {
			transaction.beignTransaction(feeRateDao);
			//删除费率
			feeRateDao.removePayFeeRate(feeCode);
			//更新费率客户关系表数据，变成零费率
			feeRateDao.setCustZeroFeeRate(feeCode);
	        transaction.endTransaction();
	        loadFee();
	        loadChannelFeeRate();
	        PayCoopBankService.loadCoopBank();
			PayCoopBankService.loadSupportedBank();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		}
    }
	/**
	 * 取得交易手续费，根据费率
	 * @param payFeeRate
	 * @return 第一个元素为手续费Long，第二个元素为费率信息String
	 * @return 手续费 分
	 */
	public static synchronized Object [] getFeeByFeeRate(PayFeeRate payFeeRate,long amt){
		try{
			//计费方式 0-定额收费；1-按比例收费
			//费率信息 格式：金额起-止,计费方式,计费值;...
			//0-100,0,3;100-200,1,10;200-300,0,5;
			if(payFeeRate==null || payFeeRate.feeInfo==null)return new Object[]{0l,"0,-1,0"};
			String [] fs = payFeeRate.feeInfo.split(";");
			for(int i=0; i<fs.length; i++){
				String [] es = fs[i].split(",");
				if(es.length != 3)continue;
				String [] d = es[0].split("-");
				if(amt>Long.parseLong(d[0])&&amt<=Long.parseLong(d[1])){
					if("0".equals(es[1]))return new Object[]{Long.parseLong(es[2]),fs[i]};//定额
					else if("1".equals(es[1]))return new Object[]{(long)((double)amt * (Double.parseDouble(es[2])/100d)+0.5),fs[i]};//比例
					else throw new Exception("非法计费方式");
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return new Object[]{0l,"0,-1,0"};
	}
	/**
	 * 取得交易手续费，根据参考金额范围、和实际计算金额
	 * @param payFeeRate
	 * @param referAmt 参考金额
	 * @param amt 费率
	 * @return
	 */
	public static synchronized Object [] getFeeByFeeRate(PayFeeRate payFeeRate,long referAmt,long amt){
		try{
			//计费方式 0-定额收费；1-按比例收费
			//费率信息 格式：金额起-止,计费方式,计费值;...
			//0-100,0,3;100-200,1,10;200-300,0,5;
			if(payFeeRate==null || payFeeRate.feeInfo==null)return new Object[]{0l,"0,-1,0"};
			String [] fs = payFeeRate.feeInfo.split(";");
			for(int i=0; i<fs.length; i++){
				String [] es = fs[i].split(",");
				if(es.length != 3)continue;
				String [] d = es[0].split("-");
				if(referAmt>Long.parseLong(d[0])&&referAmt<=Long.parseLong(d[1])){
					if("0".equals(es[1]))return new Object[]{Long.parseLong(es[2]),fs[i]};//定额
					else if("1".equals(es[1]))return new Object[]{(long)((double)amt * (Double.parseDouble(es[2])/100d)+0.5),fs[i]};//比例
					else throw new Exception("非法计费方式");
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return new Object[]{0l,"0,-1,0"};
	}
	/**
	 * 取得客户费率信息
	 * @param custType
	 * @param custId
	 * @param tranType
	 * @return
	 */
	public PayFeeRate getFeeRateByCust(String custType,String custId,String tranType){
		return new PayFeeRateDAO().getFeeRateByCust(custType,custId,tranType);
	}
	/**
	 * 支付渠道路由，根据选择的银行，费用取得最少费率渠道(当前渠道支持1消费  4退款 5提现  6转账)
	 * @param payType web:网银，quick：快捷
	 * @param bankCode
	 * @param tranType 交易类型 1消费 2充值 3结算 4退款 5提现  6转账
	 * @param bankUserType 银行用户类型 ，0借记卡 1贷记卡 4对公
	 * @param amt
	 * @return
	 * @throws Exception
	 */
	public PayCoopBank getMinFeeChannel(PayRequest payRequest,String payType,String bankCode,String tranType,String bankUserType,long amt) throws Exception{
		//获取指定渠道
		String merChannelId = PayCoopBankService.MERCHANT_CHANNEL_MAP.get(payRequest.merchantId+","+MER_CHANNEL_bankUserType.get(payType+","+bankUserType));
		if(PayCoopBankService.CHANNEL_MAP_ALL.get(merChannelId) != null)return PayCoopBankService.CHANNEL_MAP_ALL.get(merChannelId);
		//获取渠道信息================开始===========
		List <PayCoopBank>list = null,tmpList = new ArrayList();
		//直连网银
		if("web".equalsIgnoreCase(payType)) {
			if("0,1,4,".indexOf(bankUserType+",") == -1)throw new Exception("银行账户类型错误");
			//通过银行获取渠道列表
			list = new PayFeeRateDAO().getChannelListByBank(bankCode,bankUserType);
		//快捷
		} else if("quick".equalsIgnoreCase(payType)) {
			if("0,1,".indexOf(bankUserType+",") == -1)throw new Exception("银行账户类型错误");
			//通过银行获取渠道列表
			list = new PayFeeRateDAO().getQuickChannelListByBank(bankCode,bankUserType);
		}
		if(list != null) {//获取支持的渠道银行列表
			for(int i = 0; i<list.size(); i++) {
				PayCoopBank channel = list.get(i);
				//网银或快捷必须支持的情况下才返回渠道
				if("web".equalsIgnoreCase(payType)){
					if("1".equals(channel.payWebFlag))continue;
				} else if("quick".equalsIgnoreCase(payType)){
					if("1".equals(channel.payQuickFlag))continue;
				}
				//最大限额过滤
				if(!checkBankMaxAmt(channel,payType,bankUserType,amt))continue;
				if(channel.bankCode.equals(merChannelId))return channel;//指定渠道直接返回
				//商户结算周期为T+0的情况必须走渠道T+0结算的情况
				if("0".equals(PayConstant.PAY_CONFIG.get("ADVANCE_MANEY_FLAG"))){//是否企业垫资（商户T+0结算，渠道没有T+0时起作用）,0不垫资,1垫资
					if("D".equals(payRequest.merchant.custSetPeriod)&&"0".equals(payRequest.merchant.custSetFrey)){
						if("D".equals(channel.stlTimeType)&&!"0".equals(channel.stlTime))continue;
					}
				}
				tmpList.add(list.get(i));
			}
		} else return null;
		list = tmpList;
		if(list.size()==0)return null;
		else if(list.size()==1)return list.get(0);
		//获取渠道信息================结束===========
		//获取渠道规则信息============开始===========
		String curDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String ruleTranType = PayCoopBankService.ROUTE_RULE_TRAN_TYPE_MAP.get(payType+","+bankUserType);
		PayCoopBankRouteRule ruleSum = PayCoopBankService.ROUTE_RULE.get("0,-1,"+ruleTranType);
		PayCoopBankRouteRuleCache ruleCacheSum = PayCoopBankService.getRouteRuleCache().get(curDate+",-1," + ruleTranType);
		//获取渠道规则信息============结束===========
		//平均分配规则启动,额度在指定范围，没有超出笔数，没有超出限额
		if("1".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROUTE_RULE_DIVISION_FLAG"))&&ruleSum!=null&&ruleCacheSum != null 
			&& ruleSum.divisionCount>0 && ruleSum.divisionMaxAmt > 0 && ruleCacheSum.divisionCount < ruleSum.divisionCount
			&& amt <= ruleSum.divisionMaxAmt){
			//取得渠道交易数最小的渠道
			PayCoopBank minChannel = list.get(0);
			PayCoopBankRouteRuleCache minCacheChannel = PayCoopBankService.getRouteRuleCache().get(curDate+","
					+minChannel.bankCode+","+ruleTranType);
			for(int i = 1; i<list.size(); i++){
				PayCoopBank channel = list.get(i);
				PayCoopBankRouteRuleCache cache = PayCoopBankService.getRouteRuleCache().get(curDate+","+channel.bankCode+","+ruleTranType);
				if(minCacheChannel != null && cache !=null &&minCacheChannel.divisionCount>cache.divisionCount){
					minChannel = channel;
					minCacheChannel = cache;
				}
			}
			if(minCacheChannel==null)return null;
			//保存渠道交易数
			minCacheChannel.divisionCount++;//缓存保存，渠道增加
			PayCoopBankRouteRuleCache allCache = PayCoopBankService.getRouteRuleCache().get(curDate+",-1,"+ruleTranType);//缓存保存，总数增加
			allCache.divisionCount++;
			//数据库保存
			new PayCoopBankRouteRuleCacheDAO().updateCache(minCacheChannel);
			return minChannel;
		} else {
			Map<String, long[]> map = new HashMap<String, long[]>();
			for(int i = 0; i<list.size(); i++){
				PayCoopBank channel = list.get(i);
				Object [] fee = getFeeByFeeRate(CHANNEL_FEE_MAP.get(channel.bankCode+","+tranType),amt);
				if(PayCoopBankService.ROUTE_RULE.get("1,"+channel.bankCode+","+ruleTranType)==null) PayCoopBankService.loadRouteRule();
				map.put(channel.bankCode+","+ruleTranType, new long[]{(Long)(fee[0]),
					PayCoopBankService.ROUTE_RULE.get("1,"+channel.bankCode+","+ruleTranType).weight});
			}
			int flag=0;
			try {flag=Integer.parseInt(PayConstant.PAY_CONFIG.get("CHANNEL_ROUTE_RULE_PRIORITY").split(",")[0]);} catch (Exception e) {}
			String channelName = getChannelNameByRouteRule(map,flag);
			if(channelName==null||channelName.split(",").length!=2)return null;
			return PayCoopBankService.CHANNEL_MAP.get(channelName.split(",")[0]);
		}
	}
	private boolean checkBankMaxAmt(PayCoopBank channel,String payType, String bankUserType,long amt) {
		if("web".equalsIgnoreCase(payType)) {
			if("0".equals(bankUserType)){
				if(channel.channelBankRelation.webDebitMaxAmt!=null&&channel.channelBankRelation.webDebitMaxAmt>0
						&&channel.channelBankRelation.webDebitMaxAmt<amt)return false;
				else return true;
			}
			else if("1".equals(bankUserType)){
				if(channel.channelBankRelation.webCreditMaxAmt!=null&&channel.channelBankRelation.webCreditMaxAmt>0
						&&channel.channelBankRelation.webCreditMaxAmt<amt)return false;
				else return true;
			}
			else if("4".equals(bankUserType)){
				if(channel.channelBankRelation.webPublicMaxAmt!=null&&channel.channelBankRelation.webPublicMaxAmt>0
						&&channel.channelBankRelation.webPublicMaxAmt<amt)return false;
				else return true;
			}
			else return false;
		} else if("quick".equalsIgnoreCase(payType)) {
			if("0".equals(bankUserType)){
				if(channel.channelBankRelation.quickDebitMaxAmt!=null&&channel.channelBankRelation.quickDebitMaxAmt>0
						&&channel.channelBankRelation.quickDebitMaxAmt<amt)return false;
				else return true;
			}
			else if("1".equals(bankUserType)){
				if(channel.channelBankRelation.quickCeditMaxAmt!=null&&channel.channelBankRelation.quickCeditMaxAmt>0
						&&channel.channelBankRelation.quickCeditMaxAmt<amt)return false;
				else return true;
			}
			else return false;
		}
		return false;
	}
	/**
	 * 支付渠道路由，取得提现费率最小的渠道
	 * @param tranType 交易类型 1消费 2充值 3结算 4退款 5提现  6转账
	 * @param cardBin
	 * @param amt
	 * @return
	 * @throws Exception
	 */
	public static PayCoopBank getMinFeeChannelForWithdraw(String tranType,String bankCode,long amt) throws Exception{
		if(PayCoopBankService.CHANNEL_LIST.size()==0)return null;
		PayCoopBank minFeeChannel = null;
		long minFee = 0;
		for(int i = 0; i<PayCoopBankService.CHANNEL_LIST.size(); i++){
			PayCoopBank channel = PayCoopBankService.CHANNEL_LIST.get(i);
			//渠道不支持提现
			if("1".equals(channel.payWithdrawFlag))continue;
			PayChannelBankRelation r = new PayChannelBankRelationDAO().getRelationByChannelAndBank(bankCode, channel.bankCode);
			//该银行不支持该渠道提现
			if(r==null || !"0".equals(r.withdrawUserType))continue;
			Object [] fee = getFeeByFeeRate(CHANNEL_FEE_MAP.get(channel.bankCode+","+tranType),amt);
			long tmp = (Long)(fee[0]);
			if(minFeeChannel==null){
				minFee=tmp;//最小值首次赋值
				minFeeChannel = channel;
			} else {
				if(tmp<minFee){
					minFee = tmp;
					minFeeChannel = channel;
				}
			}
		}
		return minFeeChannel;
	}
	/**
	 * 取得微信/支付宝扫码、微信WAP最小费率通道
	 * @param tranType 交易类型 7消费B2C借记卡 8消费B2C信用卡 9消费B2B 10微信扫码 11快捷借记卡 12快捷贷记卡 13代理分润 14代理费率 15代收 16代付 17支付宝扫码 18微信WAP
	 * @param amt
	 * @return
	 * @throws Exception
	 */
	public static PayCoopBank getMinFeeChannelForScan(PayRequest payRequest,String tranType,long amt) throws Exception{
		if(PayCoopBankService.CHANNEL_LIST.size()==0)return null;
		PayCoopBank minFeeChannel = null;
		long minFee = 0;
		for(int i = 0; i<PayCoopBankService.CHANNEL_LIST.size(); i++){			
			PayCoopBank channel = PayCoopBankService.CHANNEL_LIST.get(i);
			if(channel.payScanFlag==null)continue;
			String supported[] = channel.payScanFlag.split(",");
			if(supported.length<3)continue;
			//判断渠道是否支持
			if("10".equals(tranType)){//微信扫码
				if(!"1".equals(supported[1]))continue;//不支持微信扫码
			} else if("18".equals(tranType)){//微信WAP
				if(!"1".equals(supported[2]))continue;//不支持微信WAP
			} else if("17".equals(tranType)) {//支付宝扫码
				if(!"1".equals(supported[0]))continue;//不支持支付宝扫码
			} else if("27".equals(tranType)) {//支付宝扫码
				if(!"1".equals(supported[3]))continue;//不支持QQ扫码
			} else continue;
			//判断是否超出渠道单笔最大额
			Long maxAmt = CHANNEL_TRAN_MAX_AMT.get(channel.bankCode+","+tranType);
			if(maxAmt!=null&&maxAmt.longValue()>0&&amt>maxAmt.longValue())continue;
			//商户结算周期为T+0的情况必须走渠道T+0结算的情况
			if("0".equals(PayConstant.PAY_CONFIG.get("ADVANCE_MANEY_FLAG"))){//是否企业垫资（商户T+0结算，渠道没有T+0时起作用）,0不垫资,1垫资
				if("D".equals(payRequest.merchant.custSetPeriod)&&"0".equals(payRequest.merchant.custSetFrey)){
					if("D".equals(channel.stlTimeType)&&!"0".equals(channel.stlTime))continue;
				}
			}
			Object [] fee = getFeeByFeeRate(CHANNEL_FEE_MAP.get(channel.bankCode+","+tranType),amt);			
			long tmp = (Long)(fee[0]);
			//if(tmp==0)continue;//如果上游渠道费率为0，视为没开放
			if(minFeeChannel==null){
				minFee=tmp;//最小值首次赋值
				minFeeChannel = channel;
			} else {
				if(tmp<minFee){
					minFee = tmp;
					minFeeChannel = channel;
				}
			}
		}
		return minFeeChannel;
	}
	/**
	 * 取得代收付渠道
	 * @param tranType 交易类型 7消费B2C借记卡 8消费B2C信用卡 9消费B2B 10微信扫码 11快捷借记卡 12快捷贷记卡 13代理分润 14代理费率 15代收 16代付 17支付宝扫码 18微信WAP
	 * @param amt
	 * @return
	 * @throws Exception
	 */
	public static PayCoopBank getMinFeeChannelForReceivePay(PayRequest payRequest,String tranType,long amt) throws Exception{
		if(PayCoopBankService.CHANNEL_LIST.size()==0)return null;
		PayCoopBank minFeeChannel = null;
		long minFee = 0;
		for(int i = 0; i<PayCoopBankService.CHANNEL_LIST.size(); i++){
			PayCoopBank channel = PayCoopBankService.CHANNEL_LIST.get(i);
			if("15".equals(tranType)){//代收
				if(!"0".equals(channel.payReceiveFlag))continue;;
				//判断银行支持情况
				PayCardBin cardBin = PayCardBinService.getCardBinByCardNo(payRequest.accNo);
				//代收的银行是否支持，代收银行最大额度是否满足
				PayChannelBankRelation cbr = new PayFeeRateDAO().getSupportedBankForRP(cardBin.bankCode,channel.bankCode,tranType);
				if(cardBin == null || cbr==null || (cbr.receiveMaxAmt>0&&cbr.receiveMaxAmt < amt))continue;
			} else if("16".equals(tranType)){//代付
				if(!"0".equals(channel.payWithdrawFlag))continue;
			} else continue;
			Object [] fee = getFeeByFeeRate(CHANNEL_FEE_MAP.get(channel.bankCode+","+tranType),amt);
			long tmp = (Long)(fee[0]);
			//if(tmp==0)continue;//如果上游渠道费率为0，视为没开放
			if(minFeeChannel==null){
				minFee=tmp;//最小值首次赋值
				minFeeChannel = channel;
			} else {
				if(tmp<minFee){
					minFee = tmp;
					minFeeChannel = channel;
				}
			}
		}
		return minFeeChannel;
	}
	/**
	 * 权值手续费获取渠道规则，如果手续费优先，取手续费最小、权值最大的，如果有多个，随机取一个；权值优先也类似
	 * @param routeInfo key=【channelName,tranType】,value=【int[]{手续费,权值}】
	 * @param flag 0手续费优先，1权值优先
	 * @return
	 */
	public String getChannelNameByRouteRule(Map<String,long[]> routeInfo,int flag){
		if(routeInfo==null||routeInfo.size()==0)return null;
		try {
			Iterator it = routeInfo.keySet().iterator();
			String key = (String) it.next();
			long [] t = routeInfo.get(key);
			long minFee=t[0],maxWeight=t[1];
			String minFeeStr=key,maxWeightStr=key;
			while(it.hasNext()){//取得最小手续费和最大权值
				key = (String) it.next();
				t = routeInfo.get(key);
				if(minFee > t[0]){//查找最小费率渠道
					minFee = t[0];
					minFeeStr=key;
				}
				if(maxWeight<t[1]){//查找最大权值渠道
					maxWeight = t[1];
					maxWeightStr = key;
				}
			}
			List<String> minFeeList = new ArrayList<String>(),maxWeightList = new ArrayList<String>();
			Iterator it1 = routeInfo.keySet().iterator();		
			while(it1.hasNext()){//取得最小手续费多个和最大权值多个列表
				key = (String) it1.next();
				t = routeInfo.get(key);
				if(minFee == t[0])minFeeList.add(key);
				if(maxWeight == t[1])maxWeightList.add(key);
			}
			if(flag==0){//手续费规则优先
				if(minFeeList.size()==1)return minFeeStr;
				minFeeStr = minFeeList.get(0);
				t = routeInfo.get(minFeeList.get(0));
				maxWeight = t[1];
				for(int i=1; i<minFeeList.size(); i++){//费率最小且相等列表，取权值最大的
					t = routeInfo.get(minFeeList.get(i));
					if(maxWeight<t[1]){
						maxWeight = t[1];
						minFeeStr = minFeeList.get(i);
					}
				}
				List<String> tmp = new ArrayList<String>();
				for(int i=0; i<minFeeList.size(); i++){//权值最大的还是有多个，随机取一个
					t = routeInfo.get(minFeeList.get(i));
					if(maxWeight==t[1])tmp.add(minFeeList.get(i));
				}
				return tmp.get((int)(Math.random()*tmp.size()));
			} else if(flag==1){//权值规则优先
				if(maxWeightList.size()==1)return maxWeightStr;
				maxWeightStr = maxWeightList.get(0);
				t = routeInfo.get(maxWeightList.get(0));
				minFee = t[0];
				for(int i=1; i<maxWeightList.size(); i++){//权值最大且相等列表，取费率最小的的
					t = routeInfo.get(maxWeightList.get(i));
					if(minFee>t[0]){
						minFee = t[0];
						maxWeightStr = maxWeightList.get(i);
					}
				}
				List<String> tmp = new ArrayList<String>();
				for(int i=0; i<maxWeightList.size(); i++){//费率最小的还是有多个，随机取一个
					t = routeInfo.get(maxWeightList.get(i));
					if(minFee==t[0])tmp.add(maxWeightList.get(i));
				}
				return tmp.get((int)(Math.random()*tmp.size()));
			} else return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}