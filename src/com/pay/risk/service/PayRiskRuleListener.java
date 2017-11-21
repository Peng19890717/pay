package com.pay.risk.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.PayUtil;

import com.PayConstant;
import com.jweb.dao.Blog;
import com.pay.risk.dao.PayRiskExceptRule;
import com.pay.risk.dao.PayRiskExceptTranInfoDAO;
/**
 * 风控规则监控跑批处理类
 * @author Administrator
 *
 */
public class PayRiskRuleListener extends TimerTask {
	private static final Log log = LogFactory.getLog(PayRiskRuleListener.class);
	Date runDay;
	public PayRiskRuleListener(Date runDay){
		this.runDay = runDay;
	}
	public void run(){
		if("1".equals(PayConstant.PAY_CONFIG.get("RISK_RULE_RUN_FLAG")))return;
		try {
			log.info("风控跑批开始==========="+runDay.toLocaleString()+"===========");
			new PayRiskRuleListenerService(runDay).runRuleListener();
			log.info("风控跑批结束==========="+runDay.toLocaleString()+"===========");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
}
class PayRiskRuleListenerService {
	Date runDay;
	Date preDay;
	Calendar calendar;
	Date preMonFirstDay;//上月第一天
	Date preMonLastDay;//上月最后一天
	Date preWeekFirstDay;//上周第一天
	Date preWeekLastDay;//上周最后一天
	public PayRiskRuleListenerService(Date runDay){
		this.runDay = runDay;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(runDay);
		calendar.add(Calendar.DATE,-1);
		preDay = calendar.getTime();
		calendar.setTime(runDay);
		calendar.add(Calendar.MONTH,-1);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		preMonFirstDay = calendar.getTime();
		calendar.setTime(runDay);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.add(Calendar.DATE,-1);
		preMonLastDay = calendar.getTime();
		calendar.setTime(runDay);
		calendar.add(Calendar.WEEK_OF_YEAR,-1);
		calendar.set(Calendar.DAY_OF_WEEK,2);
		preWeekFirstDay = calendar.getTime();
		calendar.setTime(runDay);
		calendar.set(Calendar.DAY_OF_WEEK,1);
		preWeekLastDay = calendar.getTime();
		calendar.setTime(runDay);
	}
	public void runRuleListener() throws Exception{
		// 获取风控规则列表
		List<PayRiskExceptRule> ruleList = PayRiskExceptRuleService.RISK_RULE_LIST;
		for (PayRiskExceptRule rule : ruleList) {
			if(!"1".equals(rule.isUse))continue;//不可用
			new Blog().info("执行风控规则："+rule.ruleName);
			//每种规则需要清除指定时间的记录 TODO
            //日累计笔数规则【当日累计交易笔数>=X】
            if("日累计笔数规则".equals(rule.ruleName)){
            	new PayRiskExceptTranInfoDAO().runRule3(rule,preDay);//前一天
            }
            //月累计笔数规则【上月累计交易笔数>=X】
            else if("月累计笔数规则".equals(rule.ruleName) && calendar.get(Calendar.DAY_OF_MONTH) == 1){
            	new PayRiskExceptTranInfoDAO().runRule7(rule,preMonFirstDay,preMonLastDay);//上个月
            }
            //周累计退款金额规则【上周累计退款金额>=X】
            else if("周累计退款金额规则".equals(rule.ruleName) && (calendar.get(Calendar.DAY_OF_WEEK) - 1) == 1){
            	new PayRiskExceptTranInfoDAO().runRule8(rule,preWeekFirstDay,preWeekLastDay);
            }
            //同商户下日累计金额上限规则【用户在同一商户当日累计交易金额>=X】
            else if("同商户下日累计金额上限规则".equals(rule.ruleName)){
            	new PayRiskExceptTranInfoDAO().runRule12(rule,preDay);
            }
            //同商户下日累计笔数上限规则【用户在同一商户当日累计交易笔数>=X】
            else if("同商户下日累计笔数上限规则".equals(rule.ruleName)){
            	new PayRiskExceptTranInfoDAO().runRule13(rule,preDay);
            }
            //同商户下月累计笔数上限规则【用户在同一商户上月累计交易笔数>=X】
            else if("同商户下月累计笔数上限规则".equals(rule.ruleName) && calendar.get(Calendar.DAY_OF_MONTH) == 1){
            	new PayRiskExceptTranInfoDAO().runRule17(rule,preMonFirstDay,preMonLastDay);
            }
            //单笔异常交易规则【单笔异常交易金额>=X】
            else if("单笔异常交易规则".equals(rule.ruleName)){
				new PayRiskExceptTranInfoDAO().runRule1(rule,preDay);//每日统计
            }
            //日累计金额规则【当日累计交易金额>=X】
            else if("日累计金额规则".equals(rule.ruleName)){
            	new PayRiskExceptTranInfoDAO().runRule2(rule,preDay);
            }
            //未认证用户当月交易金额上限规则【未认证用户当月交易（充值+消费+转账+提现）金额>=X】
            else if("未认证用户当月交易金额上限规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内充值金额上限规则【连续N日充值金额>=X】
            else if("连续时间段内充值金额上限规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内充值次数上限规则【连续N日充值次数>=X】
            else if("连续时间段内充值次数上限规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内转账金额上限规则【连续N日转账金额>=X】
            else if("连续时间段内转账金额上限规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内转账次数上限规则【连续N日转账次数>=X】
            else if("连续时间段内转账次数上限规则".equals(rule.ruleName)){
                //TODO
            }
            //同用户重复交易金额上限规则【同一用户单日累计相同交易金额>=Y元】
            else if("同用户重复交易金额上限规则".equals(rule.ruleName)){
                //TODO
            }
            //单笔交易占月平均值上限规则【单笔交易金额/月单笔交易平均值>=X倍】
            else if("单笔交易占月平均值上限规则".equals(rule.ruleName)){
                //TODO
            }
            //单笔交易占日平均值上限规则【单笔交易金额/日单笔交易平均值>=X倍】
            else if("单笔交易占日平均值上限规则".equals(rule.ruleName)){
                //TODO
            }
            //卡类别下交易次数上限规则【当日判别为贷记卡的交易笔数/当日交易总笔数>=X,交易总金额为整数】
            else if("卡类别下交易次数上限规则".equals(rule.ruleName)){
                //TODO
            }
            //同卡bin次数上限规则【日单笔交易平均值>=X,同卡BIN交易次数>=Y】
            else if("同卡bin次数上限规则".equals(rule.ruleName)){
                //TODO
            }
            //卡类别下交易次数上限规则2【当月判别为贷记卡的交易笔数/当月交易总笔数>=X】
            else if("卡类别下交易次数上限规则2".equals(rule.ruleName)){
                //TODO
            }
            //卡类别下交易次数上限规则3【当日交易金额为整数交易的笔数/总笔数>=X】
            else if("卡类别下交易次数上限规则3".equals(rule.ruleName)){
                //TODO
            }
            //卡类别下交易次数上限规则4【日累计金额为整数的次数/30>=X】
            else if("卡类别下交易次数上限规则4".equals(rule.ruleName)){
                //TODO
            }
            //单笔异常交易规则4【单笔交易金额<=X元的交易笔数/日累计交易笔数=区间(Y1-Y2)；同时单笔交易金额>=M元的交易笔数/日累计交易笔数=区间(N1-N2)】
            else if("单笔异常交易规则4".equals(rule.ruleName)){
                //TODO
            }
            //单笔异常交易规则3【单笔交易金额<=X元，且<=X元的日累计交易笔数/日累计交易笔数>=Y】
            else if("单笔异常交易规则3".equals(rule.ruleName)){
                //TODO
            }
            //3个月累计交易金额规则【3个月累计交易金额<=0】
            else if("3个月累计交易金额规则".equals(rule.ruleName)){
                //TODO
            }
            //6个月累计交易金额规则【6个月累计交易金额<=0】
            else if("6个月累计交易金额规则".equals(rule.ruleName)){
                //TODO
            }
            //月累计交易笔数比例增加规则【上月累计交易笔数与上上月累计交易笔数增加比例>=X】
            else if("月累计交易笔数比例增加规则".equals(rule.ruleName)){
                //TODO
            }
            //月累计交易金额比例下降规则【上月累计交易金额与上上月累计交易金额下降>=X】
            else if("月累计交易金额比例下降规则".equals(rule.ruleName)){
                //TODO
            }
            //整数笔数/总笔数规则【交易金额为整数交易的笔数/总笔数>=X】
            else if("整数笔数/总笔数规则".equals(rule.ruleName)){
                //TODO
            }
            //3个月无交易规则【X个月累计交易金额<=0】
            else if("3个月无交易规则".equals(rule.ruleName)){
                //TODO
            }
            //同商户下月累计金额上限规则【用户在同一商户上月累计交易金额>=X】
            else if("同商户下月累计金额上限规则".equals(rule.ruleName) && calendar.get(Calendar.DAY_OF_MONTH) == 1){
            	new PayRiskExceptTranInfoDAO().runRule16(rule,preMonFirstDay,preMonLastDay);
            }
            //连续在设定时间段内金额上限规则【当日发生Z笔以上交易且交易金额>=x，交易金额为整数金额；(交易包括用户所有交易类型（充值、消费、转账、提现）)】
            else if("连续在设定时间段内金额上限规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内提现金额上限规则【连续N日提现金额>=X】
            else if("连续时间段内提现金额上限规则".equals(rule.ruleName)){
                //TODO
            }
            //月累计金额规则【上月累计交易金额>=X】
            else if("月累计金额规则".equals(rule.ruleName) && calendar.get(Calendar.DAY_OF_MONTH) == 1){
            	new PayRiskExceptTranInfoDAO().runRule6(rule,preMonFirstDay,preMonLastDay);
            }
            //次数交易比值规则【当月累计交易笔数/上月累计交易笔数>=X倍】
            else if("次数交易比值规则".equals(rule.ruleName)){
                //TODO
            }
            //月累计退款笔数规则【上月累计退款笔数>=X】
            else if("月累计退款笔数规则".equals(rule.ruleName) && calendar.get(Calendar.DAY_OF_MONTH) == 1){
            	new PayRiskExceptTranInfoDAO().runRule11(rule,preMonFirstDay,preMonLastDay);
            }
            //月累计交易金额比例增加规则【上月累计交易金额与上上月累计交易金额增加比例>=X】
            else if("月累计交易金额比例增加规则".equals(rule.ruleName)){
                //TODO
            }
            //日累计笔数上限规则【日累计交易笔数>=X；单笔交易金额<Y元，且<=Y元的累计交易笔数/日累计交易笔数>=Z】
            else if("日累计笔数上限规则".equals(rule.ruleName)){
                //TODO
            }
            //昨日今日金额差上限规则【当日累计交易-前日累计交易>=X元】
            else if("昨日今日金额差上限规则".equals(rule.ruleName)){
                //TODO
            }
            //月累计笔数突增规则【上月累计交易笔数〉X】
            else if("月累计笔数突增规则".equals(rule.ruleName)){
                //TODO
            }
            //同卡号次数规则【同卡号，卡BIN判定为外币卡，当日交易次数>=X】
            else if("同卡号次数规则".equals(rule.ruleName)){
                //TODO
            }
            //单笔异常交易规则2【用户连续N天，每天都发生交易，交易金额>=x且交易金额为整数】
            else if("单笔异常交易规则2".equals(rule.ruleName)){
                //TODO
            }
            //连续在设定时间段内金额规则【用户N个自然日内充值金额>=X元且消费+转账+提现金额<=Y元且>=Z元】
            else if("连续在设定时间段内金额规则".equals(rule.ruleName)){
                //TODO
            }
            //同卡号次数金额规则【同卡号，连续交易相隔时间<=X分钟；交易次数>=Y；首次交易金额<=Z元；渐次出现的单笔交易金额>=A元】
            else if("同卡号次数金额规则".equals(rule.ruleName)){
                //TODO
            }
            //小于设定金额内交易笔数设定规则【单笔交易金额<=X元，且<=X元的日累计交易笔数/日累计交易笔数>=Y】
            else if("小于设定金额内交易笔数设定规则".equals(rule.ruleName)){
                //TODO
            }
            //月累计笔数突增【上月累计交易笔数>X】
            else if("月累计笔数突增".equals(rule.ruleName)){
                //TODO
            }
            //周累计退款笔数规则【上周累计退款笔数>=X】
            else if("周累计退款笔数规则".equals(rule.ruleName) && (calendar.get(Calendar.DAY_OF_WEEK) - 1) == 1){
            	new PayRiskExceptTranInfoDAO().runRule9(rule,preWeekFirstDay,preWeekLastDay);
            }
            //月累计退款金额规则【上月累计退款金额>=X】
            else if("月累计退款金额规则".equals(rule.ruleName) && calendar.get(Calendar.DAY_OF_MONTH) == 1){
            	new PayRiskExceptTranInfoDAO().runRule10(rule,preMonFirstDay,preMonLastDay);
            }
            //周累计金额规则【上周累计交易金额>=X】
            else if("周累计金额规则".equals(rule.ruleName) && (calendar.get(Calendar.DAY_OF_WEEK) - 1) == 1){
            	new PayRiskExceptTranInfoDAO().runRule4(rule,preWeekFirstDay,preWeekLastDay);
            }
            //同商户下周累计金额上限规则【用户在同一商户上周累计交易金额>=X】
            else if("同商户下周累计金额上限规则".equals(rule.ruleName) && (calendar.get(Calendar.DAY_OF_WEEK) - 1) == 1){
            	new PayRiskExceptTranInfoDAO().runRule14(rule,preWeekFirstDay,preWeekLastDay);
            }
            //周累计笔数规则【上周累计交易笔数>=X】
            else if("周累计笔数规则".equals(rule.ruleName) && (calendar.get(Calendar.DAY_OF_WEEK) - 1) == 1){
            	new PayRiskExceptTranInfoDAO().runRule5(rule,preWeekFirstDay,preWeekLastDay);
            }
            //同商户下周累计笔数上限规则【用户在同一商户上周累计交易笔数>=X】
            else if("同商户下周累计笔数上限规则".equals(rule.ruleName) && (calendar.get(Calendar.DAY_OF_WEEK) - 1) == 1){
            	new PayRiskExceptTranInfoDAO().runRule15(rule,preWeekFirstDay,preWeekLastDay);
            }
            //连续设定时间段内同商户下规则【监控同一用户在同一商户下N个自然日内消费累计金额>=X元】
            else if("连续设定时间段内同商户下规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内提现次数上限规则【连续N日提现次数>=X】
            else if("连续时间段内提现次数上限规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内超消费规则【连续N日消费金额>=X】
            else if("连续时间段内超消费规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内重复交易笔数、金额上限规则【同一用户连续N日累计相同金额交易次数>=X次且单笔交易金额>=Y元】
            else if("连续时间段内重复交易笔数、金额上限规则".equals(rule.ruleName)){
                //TODO
            }
            //日累计次数上限最小金额下限规则【日累计交易笔数>=X；且单笔交易金额<Y元的累计交易笔数/日累计交易笔数>=Z%】
            else if("日累计次数上限最小金额下限规则".equals(rule.ruleName)){
                //TODO
            }
            //金额交易差规则【当日累计交易-前日累计交易>=X元】
            else if("金额交易差规则".equals(rule.ruleName)){
                //TODO
            }
            //金额交易比值规则【当月累计交易金额/上月累计交易金额>=X倍】
            else if("金额交易比值规则".equals(rule.ruleName)){
                //TODO
            }
            //同卡BIN借贷卡金额次数规则【同卡BIN，卡BIN判定为贷记卡，交易金额>=X；当日交易次数>=Y】
            else if("同卡BIN借贷卡金额次数规则".equals(rule.ruleName)){
                //TODO
            }
            //周累计退款笔数比例上限规则【上周累计退款金额占上周成功交易比例>=X】
            else if("周累计退款笔数比例上限规则".equals(rule.ruleName)){
                //TODO
            }
            //月累计退款笔数比例上限规则【上月累计退款金额占上月成功交易比例>=X】
            else if("月累计退款笔数比例上限规则".equals(rule.ruleName)){
                //TODO
            }
            //月累计交易笔数比例下降规则【上月累计交易笔数与上上月累计交易笔数下降>=X】
            else if("月累计交易笔数比例下降规则".equals(rule.ruleName)){
                //TODO
            }
            //交易金额占月平均值上限规则【单笔交易金额/月单笔交易平均值>=X倍】
            else if("交易金额占月平均值上限规则".equals(rule.ruleName)){
                //TODO
            }
            //日单笔金额占日平均值上限规则【单笔交易金额/日单笔交易平均值>=X倍】
            else if("日单笔金额占日平均值上限规则".equals(rule.ruleName)){
                //TODO
            }
            //6个月无交易规则【X个月累计交易金额<=0】
            else if("6个月无交易规则".equals(rule.ruleName)){
                //TODO
            }
            //交易笔数金额区间设定规则【单笔交易金额<=X元的交易笔数/日累计交易笔数=区间（Y~Z）；同时单笔交易金额>=A元的交易笔数/日累计交易笔数=区间（B~C）】
            else if("交易笔数金额区间设定规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内重复交易金额上限规则【同一用户连续N日累计相同交易金额>=Y元】
            else if("连续时间段内重复交易金额上限规则".equals(rule.ruleName)){
                //TODO
            }
            //借贷卡金额规则【当日判别为贷记卡的交易笔数/当日交易总笔数>=X,交易总金额为整数】
            else if("借贷卡金额规则".equals(rule.ruleName)){
                //TODO
            }
            //连续时间段内无消费规则【连续N日无消费交易且提现金额>=X】
            else if("连续时间段内无消费规则".equals(rule.ruleName)){
                //TODO
            }
            //同用户重复交易次数上限规则【同一用户单日累计相同金额交易次数>=X次且单笔交易金额>=Y元】
            else if("同用户重复交易次数上限规则".equals(rule.ruleName)){
                //TODO
            }
		}
	}
}