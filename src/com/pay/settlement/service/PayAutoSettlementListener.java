package com.pay.settlement.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.JWebConstant;
import util.PayUtil;
import util.Tools;

import com.PayConstant;
import com.pay.account.service.PayBankAccountCheckService;
import com.pay.fee.dao.PayFeeRate;
import com.pay.fee.service.PayFeeRateService;
import com.pay.merchant.dao.PayAccProfile;
import com.pay.merchant.dao.PayAccProfileDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
import com.pay.order.dao.PayProductOrderDAO;
import com.pay.refund.dao.PayRefund;
import com.pay.refund.dao.PayRefundDAO;
import com.pay.settlement.dao.PayChargeLog;
import com.pay.settlement.dao.PayChargeLogDAO;
import com.pay.settlement.dao.PayMerchantSettlement;
import com.pay.settlement.dao.PayMerchantSettlementDAO;
/**
 * 跑批处理类
 * @author Administrator
 *
 */
public class PayAutoSettlementListener extends TimerTask {
	private static final Log log = LogFactory.getLog(PayAutoSettlementListener.class);
	public static boolean manual_run_flag = false;
	public static boolean auto_run_flag = false;
	PayMerchantSettlementDAO psDao = new PayMerchantSettlementDAO();
	Date day = null;
	String merNo = null;
	String stlAutoFlag = "0";
	/**
	 * 自动跑批
	 */
	public PayAutoSettlementListener(){}
	/**
	 * 手动跑批
	 * @param day
	 * @param merNo
	 */
	public PayAutoSettlementListener(Date day,String merNo,String stlAutoFlag){
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(day);
		this.day =gc.getTime();
		this.merNo = merNo;
		this.stlAutoFlag = stlAutoFlag;
	}
	public void run(){
		manual_run_flag = true;
		auto_run_flag = true;
		try {
			//自动跑批，取得当前时间
			if("0".equals(stlAutoFlag)){
				GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
				day = gc.getTime();
			}
			log.info("跑批开始==============="+day.toLocaleString());
			settle();
			log.info("跑批结束==============="+day.toLocaleString());
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
		manual_run_flag = false;
		auto_run_flag = false;
	}
	/**
	 * 自动结算
	 * @param day 结算日期
	 */
	public void settle(){
		try {
			//读取商户列表(结算未冻结)、包括费率、结算信息
			log.info("读取跑批商户开始===============");
			List<PayMerchant> merList = new ArrayList<PayMerchant>();
			Map <String,PayMerchant>merMap = new HashMap <String,PayMerchant>();
			if(merNo!=null && merNo.length()>0){
				Map<String,PayMerchant>  tMap = new PayMerchantDAO().getSettleMentMerchant(merNo);//取得商户和父商户
				merList.add(tMap.get(merNo));
			} else merList = new PayMerchantDAO().getSettleMentMerchantList();
			log.info("商户数量==============="+merList.size());
			log.info("读取跑批商户结束===============");
			//清除结算表、清除历史订单表、插入历史支付订单，指定商户数据可清理，已经结算过的不能清理
			log.info("清理跑批数据开始===============");
			//清理结算前一天的数据
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(day);
			gc.add(Calendar.DATE,-1);
			Map <String,Long []> oldStlRecord = psDao.clearPayMerchantSettlement(
					new SimpleDateFormat("yyyy-MM-dd").format(day),new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime()),merNo);
			log.info("清理跑批数据结束===============");
			log.info("生成对账数据开始===============");
			if("0".equals(PayConstant.PAY_CONFIG.get("check_settlement_flag")))
			new PayBankAccountCheckService().createAccountData(new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime()),null);
			log.info("生成对账数据结束===============");
			PayRefundDAO payRefundDao = new PayRefundDAO();
			PayMerchantSettlementDAO stlDAO = new PayMerchantSettlementDAO();
			PayAccProfileDAO accDAO = new PayAccProfileDAO();
			PayChargeLogDAO chargeDAO = new PayChargeLogDAO();
			CreateMerchantAccountFile accService = new CreateMerchantAccountFile();
			//构建树形结构
			for(int i = 0; i<merList.size(); i++)merMap.put(merList.get(i).custId,merList.get(i));
			for(int i = 0; i<merList.size(); i++) {
				PayMerchant mer = (PayMerchant)merList.get(i);
				PayMerchant parent = merMap.get(mer.parentId);
				if(parent == null)continue;
				parent.subMerList.add(mer);
				parent.subMerMap.put(mer.custId,mer);
			}
			//清算开始
			for(int i = 0; i<merList.size(); i++){
				PayMerchantSettlement stl = null;
				PayMerchant mer = (PayMerchant)merList.get(i);
				//平台商户不参与结算,商户分类，0一般商户，1平台商户，2担保商户，3代理，默认0
				if("1".equals(mer.merType))continue;
				Date [] stlDay = getStlDay(mer);
				Date [] agentStlDay = getAgentStlDay(mer);
				Date [] daishouStlDay = getDaishouStlDay(mer);
//				long appAmt = 0;//外扣结算额
				log.info(mer.custId + "（"+mer.settlementWay+"）计算开始(进度"+(i+1)+"/"+merList.size()+")===============stlDay:"
						+(stlDay==null?null:(stlDay[0].toLocaleString()+"~"+stlDay[1].toLocaleString())));
				if(stlDay == null && agentStlDay==null && daishouStlDay==null)continue;
				stl = new PayMerchantSettlement();
				stl.stlId = Tools.getUniqueIdentify();//	商户自助结算序列号
				stl.stlMerId=mer.custId;	//	结算商户序列号
				stl.stlMerCustno="";	//	结算商户客户号
				stl.stlMerPayacno="";	//	结算商户支付账号
				stl.stlStltype="T";	//	结算类型 T 固定
				stl.stlStatus="0";	//	0初始状态="";1待审核状态="";2审核通过状态="";3审核未通过状态="";4结算成功="";5结算失败,默认0
				stl.stlSeqno="";	//	后台返回的结算序列号
				stl.stlType="0";	//	结算种类 0转账、1现金 默认0
				stl.stlTxncomamt=0l;	//	结算交易佣金
				stl.stlAutoFlag=stlAutoFlag;	//	0自动 1手动 默认0
				stl.stlApplDate = day;
				stl.stlFeeCode="";
				stl.settlementWay=mer.settlementWay;//结算方式 0自动结算到虚拟账户 1线下打款 2自动结算到银行账户 3实时结算到虚拟账户  4实时结算到银行账户
				stl.chargeWay = mer.chargeWay;//交易手续费收取方式，0结算收取 1预存手续费收取
				stl.stlPeriod=mer.payCustStlInfo.custSetPeriod;//交易结算周期
				stl.stlPeriodDaySet="D".equals(stl.stlPeriod)?mer.payCustStlInfo.custSetFrey:mer.payCustStlInfo.custStlTimeSet;
				stl.stlPeriodAgent=mer.payCustStlInfo.custSetPeriodAgent;//代理结算周期
				stl.stlPeriodDaySetAgent="D".equals(stl.stlPeriodAgent)?mer.payCustStlInfo.custSetFreyAgent:mer.payCustStlInfo.custStlTimeSetAgent;
				stl.stlPeriodReceive=mer.payCustStlInfo.custSetPeriodDaishou;//代收结算周期
				stl.stlPeriodSetReceive=mer.payCustStlInfo.custStlTimeSetDaishou;
//				long txNetreAmt = 0;
				if(stlDay != null) {
					stl.stlFromTime=stlDay[0];	//	结算起始时间
					stl.stlToTime=stlDay[1];	//	结算终止时间
					//取得已结算交易信息
					Long [] tmp = stlDAO.getStlTotalAmtOver(mer, new SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTime),new SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTime));
					if(tmp!=null){
						stl.stlAmtOver=tmp[2];
						stl.stlAmtFeeOver=tmp[3];
					}
					//取得成功交易信息
					tmp = stlDAO.getStlTotalAmt(mer,new SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTime), new SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTime));
					if(tmp != null) {
						stl.stlApplystlamt = tmp[0];//交易总额
						stl.stlApplystlCount = tmp[1];//交易笔数
						stl.stlNetrecamt = tmp[2];//结算总额
						stl.stlFee = tmp[3];//总结算手续费
						//交易手续费收取方式，0结算收取 1预存手续费收取
						log.info(mer.custId + "=====交易额："+stl.stlApplystlamt+"=====交易笔数："+stl.stlApplystlCount+"=====交易结算额："+stl.stlNetrecamt+"=====交易手续费"+stl.stlFee);
						if(stl.stlApplystlamt>0) {//有交易的情况
							//取得商户退款列表（根据时间段获取）
							tmp = payRefundDao.getStlTotalRefundAmt(mer, new SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTime), new SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTime));
							if(tmp != null&&tmp[0]>0) {//有退款
								log.info(mer.custId + "=====退款金额："+tmp[0] + "=====退款笔数："+tmp[1]);
								stl.stlRefundAmt = tmp[0];//	退款金额
								stl.stlRefundCount =  tmp[1];//	退款笔数
								stl.stlNetrecamt = stl.stlNetrecamt-stl.stlRefundAmt;//结算净额
							}
						}
					}
				}
				if(agentStlDay!=null) {
//					stl.stlFromTimeAgent=agentStlDay[0];//结算起始时间
//					stl.stlToTimeAgent=agentStlDay[1];//结算终止时间
//					//取得成功交易订单总额，本期和上期的
//					Date [] agentPreStlDay = getAgentPreStlDay(mer);
//					List <Long []>tmpAgent = stlDAO.getAgentStl(mer,agentStlDay,agentPreStlDay);
//					//本期结算金额（交易额、手续费、结算额、代理费、结算笔数）
//					Long [] agentStl = tmpAgent.get(0);
//					//取得目标额
//					PayFeeRate agentFeeRate = mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",13");
//					long goalAmt = 0l;
//					try {
//						goalAmt = Long.parseLong(agentFeeRate.feeInfo.split(";")[0].split(",")[0].split("-")[1]);//0-1000,0,1000;1000-2000,1,30.00;2000-3000,1,30.00;
//						if(goalAmt>0){
//							//计算本期结算金额
//							Object [] feeInfo = PayFeeRateService.getFeeByFeeRate(agentFeeRate,agentStl[1]-agentStl[3]);
//							//没有达到目标，结算额=（商户手续费-代理费）*代理费率*未达到目标费率
//							if(agentStl[0]<goalAmt) stl.agentStlIn +=  (long)((double)(((Long)(feeInfo[0]))*mer.agentGoalDiscountRate)/100d+0.5);
//							else stl.agentStlIn +=  (Long)(feeInfo[0]);//达到或超出目标
//							stl.stlNetrecamt += stl.agentStlIn;//结算净额
//							if("1000000".equals(mer.custId))System.out.println("***********--------*********----------");
//							log.info(mer.custId + "=====代理分润："+stl.agentStlIn);
//						}
//					} catch (Exception e) {e.printStackTrace();}
					stl.stlFromTimeAgent=agentStlDay[0];//结算起始时间
					stl.stlToTimeAgent=agentStlDay[1];//结算终止时间
					//本期结算金额（交易额、手续费、结算额、代理费、结算笔数）
					Long [] agentStl = stlDAO.getAgentStl(mer,agentStlDay);
					//取得代理分润费率
					PayFeeRate agentShareFeeRate = mer.feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+mer.custId+",13");
					try {
						//计算本期结算金额
						if(agentStl[1]-agentStl[3] >0){//手续费要大于代理费用，有分润
							Object [] shareFeeInfo = PayFeeRateService.getFeeByFeeRate(agentShareFeeRate,agentStl[0],agentStl[1]-agentStl[3]);
							stl.agentStlIn =  (Long)shareFeeInfo[0];
							stl.stlNetrecamt += stl.agentStlIn;//结算净额
//							if("1000000".equals(mer.custId))System.out.println("***********--------*********----------");
							log.info(mer.custId + "=====代理分润："+stl.agentStlIn);
						}
					} catch (Exception e) {e.printStackTrace();}
				}
				if(daishouStlDay != null){//代收
					stl.stlFromTimeReceive=daishouStlDay[0];//结算起始时间
					stl.stlToTimeReceive=daishouStlDay[1];//结算终止时间
					//取得已结算总额
					Long [] tmp = stlDAO.getStlTotalAmtReceiveOver(mer, new SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTimeReceive),new SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTimeReceive));
					if(tmp!=null){
						stl.stlAmtOver=stl.stlAmtOver+(tmp[0]-tmp[2]);
						stl.stlAmtFeeOver=stl.stlAmtFeeOver+tmp[2];
					}
					//取得成功代收未结算总额（根据时间段获取）
					tmp = stlDAO.getStlTotalAmtReceive(mer,new SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTimeReceive),new SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTimeReceive));
					if(tmp != null) {
						stl.receiveAmt = tmp[0];//	结算金额
						stl.receiveCount = tmp[1];//	代收笔数
						stl.receiveFee = tmp[2];//	代收手续费
						log.info(mer.custId + "=====代收结算金额："+stl.receiveAmt +"=====代收笔数："+stl.receiveCount+"=====代收手续费："+stl.receiveFee);
						if(stl.receiveAmt>0) {
							stl.stlNetrecamt = stl.stlNetrecamt+ stl.receiveAmt - stl.receiveFee;//	结算净额
						}
					}
				}
				if(stl.stlApplystlamt+stl.receiveAmt==0&&stl.agentStlIn==0)continue;//无交易额、无代收额，不结算
				if(stl.stlNetrecamt+stl.stlRefundAmt == stl.stlAmtOver){//已经全部结算，不结算
					stl.stlStatus = "4";//已结算
					stl.stlNetrecamt=0l;
					stl.stlAmtOver -= stl.stlRefundAmt;
					if("1".equals(mer.chargeWay))stl.stlAmtOver=stl.stlAmtOver+stl.stlAmtFeeOver;//交易手续费收取方式，0结算收取 1预存手续费收取
				} else {
//					System.out.println("*********--stlNetrecamt="+stl.stlNetrecamt+"---stlFee="+stl.stlFee+"---receiveAmt="
//							+stl.receiveAmt+"---receiveFee="+stl.receiveFee+"---stlAmtOver="+stl.stlAmtOver+"---stlAmtFeeOver="+stl.stlAmtFeeOver);
					//交易手续费收取方式，0结算收取 1预存手续费收取
					if("1".equals(mer.chargeWay)){
						stlFeeToPreAcc(mer,stl);
						stl.stlAmtOver=stl.stlAmtOver+stl.stlAmtFeeOver;
//						stl.stlNetrecamt=stl.stlNetrecamt+stl.stlFee;
					}
					long tmp = stl.stlNetrecamt;
					if(stl.stlNetrecamt>0)stl.stlNetrecamt=stl.stlNetrecamt-stl.stlAmtOver;//此时stl.stlNetrecamt为结算总额//结算周期混合，自动到虚拟账户
					if("0".equals(mer.settlementWay)){//结算方式 0自动结算到虚拟账户 1线下打款 2手动结算到虚拟账户 3实时结算到虚拟账
						stl.stlStatus = "4";//已结算
						stlToAccProfile(mer,stl);//自动结算到虚拟账户
						stl.stlAmtOver = tmp;
						stl.stlNetrecamt=0l;
					}
//					System.out.println("*********--stlNetrecamt="+stl.stlNetrecamt+"---stlFee="+stl.stlFee+"---receiveAmt="
//							+stl.receiveAmt+"---receiveFee="+stl.receiveFee+"---stlAmtOver="+stl.stlAmtOver+"---stlAmtFeeOver="+stl.stlAmtFeeOver);
					stlDAO.updateTransStlInfo(stl,stlDay,daishouStlDay,mer.settlementWay);
					stl.stlAmtFeeOver = stl.stlAmtFeeOver+stl.stlFee+stl.receiveFee;
				}
				//添加结算信息，修改订单清结算状态，清算流水号、清算日期、清算时间、结算状态
				stlDAO.addPayMerchantSettlement(stl,stlDay,daishouStlDay,mer.settlementWay);
				//生成对账文件
				accService.createAccFile(stl);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(PayUtil.exceptionToString(e));
		}
	}
	/**
	 * 结算到虚拟账户
	 * @param mer
	 * @param stl
	 * @throws Exception
	 */
	public void stlToAccProfile(PayMerchant mer,PayMerchantSettlement stl) throws Exception{
		PayAccProfileDAO accDAO = new PayAccProfileDAO();
		//结算到商户虚拟账户
		PayAccProfile payAccProfile = new PayAccProfile();
		payAccProfile.acType=PayConstant.CUST_TYPE_MERCHANT;
		payAccProfile.payAcNo = stl.stlMerId;
		//交易手续费收取方式，0结算收取 1预存手续费收取，手续费充足的情况
		payAccProfile.cashAcBal = stl.stlNetrecamt;
		payAccProfile.consAcBal = stl.stlNetrecamt;
		accDAO.updatePayAccProfileForSettlement(payAccProfile);		
	}
	/**
	 * 手续费预存扣除
	 * @param mer
	 * @param stl
	 * @throws Exception
	 */
	public void stlFeeToPreAcc(PayMerchant mer,PayMerchantSettlement stl) throws Exception{
		PayChargeLogDAO chargeDAO = new PayChargeLogDAO();
		PayAccProfileDAO accDAO = new PayAccProfileDAO();
		long fee = stl.stlFee+stl.receiveFee-stl.stlAmtFeeOver;
		if(fee>0&&mer.preStorageFee>=fee){
			PayChargeLog oldChargeLog = null;
			PayChargeLog chargeLog = new PayChargeLog();
			//手续费操作日志
			chargeLog.id = Tools.getUniqueIdentify();
			chargeLog.custType = PayConstant.CUST_TYPE_MERCHANT;
			chargeLog.custId = mer.custId;
			chargeLog.chargeType = "0";//手续费类型 0交易结算，1代收，2代付
			chargeLog.chargeFrom = "0";//扣款来源 0手续费余额，1账户余额
			chargeLog.amt=fee;
			chargeLog.createId="admin";
			chargeLog.settleTime = stl.stlApplDate;
			Calendar c = Calendar.getInstance();
			c.setTime(chargeLog.settleTime);
			String fromDay = new java.text.SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTime);
			String toDay = new java.text.SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTime);
			if(fromDay.equals(toDay))chargeLog.remark="扣除"+fromDay+"日手续费"
					+stl.stlApplystlCount+"笔"+String.format("%.2f", ((double)chargeLog.amt)/100d)+"元。";
			else  chargeLog.remark="扣除"+fromDay+"日到"+toDay
						+"日手续费"+stl.stlApplystlCount+"笔"+String.format("%.2f", ((double)chargeLog.amt)/100d)+"元。";
			oldChargeLog = chargeDAO.deleteStlDayLog(chargeLog);
			stl.stlNetrecamt = stl.stlNetrecamt+fee; 
			//补回老的差额 
			if(oldChargeLog!=null) fee = fee - oldChargeLog.amt;
			chargeLog.curStorageFee = mer.preStorageFee-fee;
			chargeDAO.addPayChargeLog(chargeLog);
			accDAO.updatePayAccChargeForSettlement(mer,fee);
		} else stl.chargeWay="0";
	}
	/**
	 * 根据费率计算交易额
	 * @param allMer
	 * @param merTree
	 * @param mer
	 */
	private long getAgentFeeByFeeRate(String feeInfo,long amt){
		try {
			String [] es = feeInfo.split(",");
			if(es.length != 3)return 0l;
			if("0".equals(es[1]))return Long.parseLong(es[2]);//定额
			else if("1".equals(es[1]))return (long)((double)amt * (Double.parseDouble(es[2])/100d)+0.5);//比例
		} catch (Exception e) {}
		return 0l;
	}
	/**
	 * 取得商户结算日期
	 * @param mer
	 * @return
	 */
	private Date [] getStlDay(PayMerchant mer){
		try{
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(day);
			if("D".equals(mer.payCustStlInfo.custSetPeriod)){
				int days = Integer.parseInt(mer.payCustStlInfo.custSetFrey);
				gc.add(Calendar.DATE,0-(days==0?1:days));
				return new Date[]{gc.getTime(),gc.getTime()};
			} else if("W".equals(mer.payCustStlInfo.custSetPeriod)){
				String tmp = "|"+mer.payCustStlInfo.custStlTimeSet+"|";
				byte [] b = Tools.replaceStr(tmp,"|", "").getBytes();
				int week = gc.get(GregorianCalendar.DAY_OF_WEEK)-1;
				int i = 0,lastDay = 0;
				byte c = (byte)((week==0?7:week)+'0');
				//到清算日期了
				if(tmp.indexOf("|"+(week==0?7:week)+"|")!=-1){
					if(b.length == 1)lastDay=7;//结算是一周某一天
					else {//结算结算是一周大于一天
						for(; i<b.length; i++)if(c == b[i])break;
						if(i>0)lastDay = b[i]-b[i-1];
						else lastDay = 7-(b[b.length - 1]-b[0]);
					}
					gc.add(Calendar.DATE,-1);
					Date endD = gc.getTime();
					gc.add(Calendar.DATE,0-lastDay+1);
					Date startD = gc.getTime();
					return new Date[]{startD,endD};
				}
			} else if("M".equals(mer.payCustStlInfo.custSetPeriod)){
				if(Integer.parseInt(mer.payCustStlInfo.custStlTimeSet) == gc.get(Calendar.DAY_OF_MONTH)){
					gc.add(Calendar.MONTH, -1);
					Date startDay = gc.getTime();
					gc.add(Calendar.MONTH, 1);
					gc.add(Calendar.DATE, -1);
					//检查结算时间，本月是否存在
					Date endDay = gc.getTime();
					return new Date[]{startDay,endDay};
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	/**
	 * 取得代理结算日期
	 * @param mer
	 * @return
	 */
	private Date [] getAgentStlDay(PayMerchant mer){
		try{
			if(mer.subMerList.size()==0)return null;
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(day);
			if("D".equals(mer.payCustStlInfo.custSetPeriodAgent)){
				gc.add(Calendar.DATE,0-Integer.parseInt(mer.payCustStlInfo.custSetFreyAgent));
				return new Date[]{gc.getTime(),gc.getTime()};
			} else if("W".equals(mer.payCustStlInfo.custSetPeriodAgent)){
				String tmp = "|"+mer.payCustStlInfo.custStlTimeSetAgent+"|";
				byte [] b = Tools.replaceStr(tmp,"|", "").getBytes();
				int week = gc.get(GregorianCalendar.DAY_OF_WEEK)-1;
				int i = 0,lastDay = 0;
				byte c = (byte)((week==0?7:week)+'0');
				//到清算日期了
				if(tmp.indexOf("|"+(week==0?7:week)+"|")!=-1){
					if(b.length == 1)lastDay=7;//结算是一周某一天
					else {//结算结算是一周大于一天
						for(; i<b.length; i++)if(c == b[i])break;
						if(i>0)lastDay = b[i]-b[i-1];
						else lastDay = 7-(b[b.length - 1]-b[0]);
					}
					gc.add(Calendar.DATE,-1);
					Date endD = gc.getTime();
					gc.add(Calendar.DATE,0-lastDay+1);
					Date startD = gc.getTime();
					return new Date[]{startD,endD};
				}
			} else if("M".equals(mer.payCustStlInfo.custSetPeriodAgent)){
				if(Integer.parseInt(mer.payCustStlInfo.custStlTimeSetAgent) == gc.get(Calendar.DAY_OF_MONTH)){
					gc.add(Calendar.MONTH, -1);
					Date startDay = gc.getTime();
					gc.add(Calendar.MONTH, 1);
					gc.add(Calendar.DATE, -1);
					//检查结算时间，本月是否存在
					Date endDay = gc.getTime();
					return new Date[]{startDay,endDay};
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	/**
	 * 取得代收结算日期
	 * @param mer
	 * @return
	 */
	private Date [] getDaishouStlDay(PayMerchant mer){
		try{
			if("1".equals(mer.payWaySupported.substring(4,5)))return null;//不支持代收业务
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(day);
			if("D".equals(mer.payCustStlInfo.custSetPeriodDaishou)){
				int days = Integer.parseInt(mer.payCustStlInfo.custStlTimeSetDaishou);
				gc.add(Calendar.DATE,0-(days==0?1:days));
				return new Date[]{gc.getTime(),gc.getTime()};
			} else if("W".equals(mer.payCustStlInfo.custSetPeriodDaishou)){
				String tmp = "|"+mer.payCustStlInfo.custStlTimeSetDaishou+"|";
				byte [] b = Tools.replaceStr(tmp,"|", "").getBytes();
				int week = gc.get(GregorianCalendar.DAY_OF_WEEK)-1;
				int i = 0,lastDay = 0;
				byte c = (byte)((week==0?7:week)+'0');
				//到清算日期了
				if(tmp.indexOf("|"+(week==0?7:week)+"|")!=-1){
					if(b.length == 1)lastDay=7;//结算是一周某一天
					else {//结算结算是一周大于一天
						for(; i<b.length; i++)if(c == b[i])break;
						if(i>0)lastDay = b[i]-b[i-1];
						else lastDay = 7-(b[b.length - 1]-b[0]);
					}
					gc.add(Calendar.DATE,-1);
					Date endD = gc.getTime();
					gc.add(Calendar.DATE,0-lastDay+1);
					Date startD = gc.getTime();
					return new Date[]{startD,endD};
				}
			} else if("M".equals(mer.payCustStlInfo.custSetPeriodDaishou)){
				if(Integer.parseInt(mer.payCustStlInfo.custStlTimeSetDaishou) == gc.get(Calendar.DAY_OF_MONTH)){
					gc.add(Calendar.MONTH, -1);
					Date startDay = gc.getTime();
					gc.add(Calendar.MONTH, 1);
					gc.add(Calendar.DATE, -1);
					//检查结算时间，本月是否存在
					Date endDay = gc.getTime();
					return new Date[]{startDay,endDay};
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	/**
	 * 取得上一个代理结算日期
	 * @param mer
	 * @return
	 */
	private Date [] getAgentPreStlDay(PayMerchant mer){
		try{
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(new Date());
			if("D".equals(mer.payCustStlInfo.custSetPeriodAgent)){
				gc.add(Calendar.DATE,0-Integer.parseInt(mer.payCustStlInfo.custSetFreyAgent)-1);
				return new Date[]{gc.getTime(),gc.getTime()};
			} else if("W".equals(mer.payCustStlInfo.custSetPeriodAgent)){
				String tmp = "|"+mer.payCustStlInfo.custStlTimeSetAgent+"|";
				byte [] b = Tools.replaceStr(tmp,"|", "").getBytes();
				int week = gc.get(GregorianCalendar.DAY_OF_WEEK)-1;
				int i = 0,lastDay = 0;
				byte c = (byte)((week==0?7:week)+'0');
				//到清算日期了
				if(tmp.indexOf("|"+(week==0?7:week)+"|")!=-1){
					if(b.length == 1)lastDay=7;//结算是一周某一天
					else {//结算结算是一周大于一天
						for(; i<b.length; i++)if(c == b[i])break;
						if(i>0)lastDay = b[i]-b[i-1];
						else lastDay = 7-(b[b.length - 1]-b[0]);
					}
					gc.add(Calendar.DATE,-1);
					Date endD = gc.getTime();
					gc.add(Calendar.DATE,0-lastDay+1);
					Date startD = gc.getTime();
					gc.setTime(startD);
					gc.add(Calendar.WEEK_OF_MONTH,-1);
					startD = gc.getTime();
					gc.setTime(endD);
					gc.add(Calendar.WEEK_OF_MONTH,-1);
					endD = gc.getTime();
					return new Date[]{startD,endD};
				}
			} else if("M".equals(mer.payCustStlInfo.custSetPeriodAgent)){
				if(Integer.parseInt(mer.payCustStlInfo.custSetFreyAgent) == gc.get(Calendar.DAY_OF_MONTH)){
					gc.add(Calendar.MONTH, -2);
					Date startDay = gc.getTime();
					gc.add(Calendar.MONTH, 1);
					gc.add(Calendar.DATE, -1);
					//检查结算时间，本月是否存在
					Date endDay = gc.getTime();
					return new Date[]{startDay,endDay};
				}
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
}
class CreateMerchantAccountFile{
	public static String mainPath = JWebConstant.APP_PATH+"/dat/merchant_account_file/";
	public static long recordCount = 10000l;
	public void createAccFile(PayMerchantSettlement stl){
		//创建商家目录
		File merPath = new File(mainPath+stl.stlMerId);
		if(!merPath.exists())merPath.mkdir();
		//创建对账日期目录
		File merDayPath = new File(mainPath+stl.stlMerId+"/"+ new SimpleDateFormat("yyyyMMdd").format(stl.stlApplDate));
		if(!merDayPath.exists())merDayPath.mkdir();
		File [] files = merDayPath.listFiles();
		for(int i = 0; i<files.length; i++)files[i].delete();
		FileOutputStream fos = null;
		//生成商品订单对账单
		try{
			fos = new FileOutputStream(merDayPath.getAbsolutePath()+"/product_order.txt");
			List list = new ArrayList();
	    	long step = 0;
	    	//取得业务数据
	    	list = new PayProductOrderDAO().getStlProductOrderList(stl,step,step+recordCount);
	    	while(list.size()==recordCount){
	    		//写入对账文件
	    		writeProductOrderFile(list,fos);
	    		step = step + recordCount;
	    		//取得业务数据
	    		list = new PayProductOrderDAO().getStlProductOrderList(stl,step,step+recordCount);
	    	}
	    	//加入结尾的记录
	    	if(list.size()<recordCount){
	    		//写入对账文件
	    		writeProductOrderFile(list,fos);
	    	}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(fos != null)try{fos.close();} catch(Exception e){}
		}
		//生成支付对账单
		try{
			fos = new FileOutputStream(merDayPath.getAbsolutePath()+"/payment_order.txt");
			List list = new ArrayList();
	    	long step = 0;
	    	//取得业务数据
	    	list = new PayOrderDAO().getStlPayOrderList(stl,step,step+recordCount);
	    	while(list.size()==recordCount){
	    		//写入对账文件
	    		writePayOrderFile(list,fos);
	    		step = step + recordCount;
	    		//取得业务数据
	    		list = new PayOrderDAO().getStlPayOrderList(stl,step,step+recordCount);
	    	}
	    	//加入结尾的记录
	    	if(list.size()<recordCount){
	    		//写入对账文件
	    		writePayOrderFile(list,fos);
	    	}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(fos != null)try{fos.close();} catch(Exception e){}
		}
		//生成退款对账单
		try{
			fos = new FileOutputStream(merDayPath.getAbsolutePath()+"/refund.txt");
			List list = new ArrayList();
	    	long step = 0;
	    	//取得业务数据
	    	list = new PayRefundDAO().getStlPayRefundList(stl,step,step+recordCount);
	    	while(list.size()==recordCount){
	    		//写入对账文件
	    		writePayRefundFile(list,fos);
	    		step = step + recordCount;
	    		//取得业务数据
	    		list = new PayRefundDAO().getStlPayRefundList(stl,step,step+recordCount);
	    	}
	    	//加入结尾的记录
	    	if(list.size()<recordCount){
	    		//写入对账文件
	    		writePayRefundFile(list,fos);
	    	}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if(fos != null)try{fos.close();} catch(Exception e){}
		}
		try {
			//生成zip
	    	String zipPath = mainPath+stl.stlMerId+"/"+ new SimpleDateFormat("yyyyMMdd").format(stl.stlApplDate);
	    	File zipFile=new File(zipPath+".zip");
        	File srcFile[] = new File[]{
        		new File(zipPath+"/product_order.txt"),
        		new File(zipPath+"/payment_order.txt"),
        		new File(zipPath+"/refund.txt")};
        	Tools.zipFiles(srcFile, zipFile);
			Tools.deleteFile(new File(zipPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void writeProductOrderFile(List list, FileOutputStream fos) throws IOException {
		for(int i = 0; i<list.size(); i++){
			PayProductOrder prdOrder = (PayProductOrder)list.get(i);
			//商户订单号|交易时间|交易金额|退款金额
			fos.write((prdOrder.prdordno+"|"+
					new SimpleDateFormat("yyyyMMddHHmmss").format(prdOrder.ordertime)+"|"
					+prdOrder.ordamt+"|"+prdOrder.rftotalamt+"\r\n").getBytes());
		}
	}
	private void writePayOrderFile(List list, FileOutputStream fos) throws IOException {
		for(int i = 0; i<list.size(); i++){
			PayOrder payOrder = (PayOrder)list.get(i);
			//商户订单号|支付时间|支付金额
			fos.write((payOrder.prdordno+"|"+new SimpleDateFormat("yyyyMMddHHmmss").format(payOrder.actdat)+"|"+
					payOrder.txamt+"|"+(payOrder.fee==null?0:payOrder.fee)+"\r\n").getBytes());
		}
	}
	private void writePayRefundFile(List list, FileOutputStream fos) throws IOException {
		for(int i = 0; i<list.size(); i++){
			PayRefund refund = (PayRefund)list.get(i);
			//商户订单号|退款时间|退款金额
			fos.write((refund.oriprdordno+"|"+new SimpleDateFormat("yyyyMMddHHmmss").format(refund.rfordtime)+"|"+
					refund.rfamt+"\r\n").getBytes());
		}
	}
}
