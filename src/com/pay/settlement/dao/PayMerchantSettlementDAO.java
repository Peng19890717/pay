package com.pay.settlement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
import com.jweb.dao.JWebUser;
import com.jweb.dao.JWebUserDAO;
import com.pay.custstl.dao.PayCustStlInfo;
import com.pay.merchant.dao.PayMerchant;
/**
 * Table PAY_MERCHANT_SETTLEMENT DAO. 
 * @author Administrator
 *
 */
public class PayMerchantSettlementDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayMerchantSettlementDAO.class);
    public static synchronized PayMerchantSettlement getPayMerchantSettlementValue(ResultSet rs)throws SQLException {
        PayMerchantSettlement stl = new PayMerchantSettlement();
        stl.merchant.payCustStlInfo = new PayCustStlInfo();
        stl.stlId = rs.getString("STL_ID");
        stl.stlMerId = rs.getString("STL_MER_ID");
        stl.stlMerCustno = rs.getString("STL_MER_CUSTNO");
        stl.stlMerPayacno = rs.getString("STL_MER_PAYACNO");
        stl.stlStltype = rs.getString("STL_STLTYPE");
        stl.stlApplystlamt = rs.getLong("STL_APPLYSTLAMT");
        stl.stlRefundAmt = rs.getLong("STL_REFUND_AMT");
        stl.stlFromTime = rs.getTimestamp("STL_FROM_TIME");
        stl.stlToTime = rs.getTimestamp("STL_TO_TIME");
        stl.stlStatus = rs.getString("STL_STATUS");
        stl.stlApplicants = rs.getString("STL_APPLICANTS");
        stl.stlApplTime = rs.getTimestamp("STL_APPL_TIME");
        stl.stlApplIp = rs.getString("STL_APPL_IP");
        stl.stlAuditPer = rs.getString("STL_AUDIT_PER");
        stl.stlAuditTime = rs.getTimestamp("STL_AUDIT_TIME");
        stl.stlAuditIp = rs.getString("STL_AUDIT_IP");
        stl.stlSucTime = rs.getTimestamp("STL_SUC_TIME");
        stl.stlSucOperator = rs.getString("STL_SUC_OPERATOR");
        stl.stlSeqno = rs.getString("STL_SEQNO");
        stl.stlType = rs.getString("STL_TYPE");
        stl.stlFee = rs.getLong("STL_FEE");
        stl.stlTotalordamt = rs.getLong("STL_TOTALORDAMT");
        stl.stlTxncomamt = rs.getLong("STL_TXNCOMAMT");
        stl.stlNetrecamt = rs.getLong("STL_NETRECAMT");
        stl.stlApplDate = rs.getTimestamp("STL_APPL_DATE");
        stl.stlAutoFlag = rs.getString("STL_AUTO_FLAG");
        stl.stlApplystlCount = rs.getLong("STL_APPLYSTL_COUNT");
        stl.stlRefundCount = rs.getLong("STL_REFUND_COUNT");
        stl.remark = rs.getString("REMARK");
        stl.stlFeeCode = rs.getString("STL_FEE_CODE");
        try{stl.stlFeeName = rs.getString("STL_FEE_NAME");}catch(Exception e){}
        stl.stlPeriod = rs.getString("STL_PERIOD");
        stl.stlPeriodDaySet = rs.getString("STL_PERIOD_DAY_SET");
        stl.settlementWay = rs.getString("SETTLEMENT_WAY");
        stl.agentStlIn = rs.getLong("AGENT_STL_IN");
        stl.agentStlOut = rs.getLong("AGENT_STL_OUT");
        stl.receiveCount = rs.getLong("RECEIVE_COUNT");
        stl.receiveAmt = rs.getLong("RECEIVE_AMT");
        stl.receiveFee = rs.getLong("RECEIVE_FEE");
        stl.stlPeriodReceive = rs.getString("STL_PERIOD_RECEIVE");
        stl.stlFromTimeReceive = rs.getTimestamp("STL_FROM_TIME_RECEIVE");
        stl.stlToTimeReceive = rs.getTimestamp("STL_TO_TIME_RECEIVE");
        stl.stlPeriodSetReceive = rs.getString("STL_PERIOD_SET_RECEIVE");
        stl.stlAmtOver = rs.getLong("STL_AMT_OVER");
        stl.stlAmtFeeOver = rs.getLong("STL_AMT_FEE_OVER");
        try{stl.stlFromTimeAgent = rs.getTimestamp("STL_FROM_TIME_AGENT");}catch(Exception e){}
        try{stl.stlToTimeAgent = rs.getTimestamp("STL_TO_TIME_AGENT");}catch(Exception e){}
        try{stl.merchant.storeName = rs.getString("STORE_NAME");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.seqNo = rs.getString("SEQ_NO");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.payAcNo = rs.getString("PAY_AC_NO");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custId = rs.getString("CUST_ID");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custBankDepositName = rs.getString("CUST_BANK_DEPOSIT_NAME");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.depositBankCode = rs.getString("DEPOSIT_BANK_CODE");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.depositBankBrchName = rs.getString("DEPOSIT_BANK_BRCH_NAME");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custStlBankAcNo = rs.getString("CUST_STL_BANK_AC_NO");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custStlType = rs.getString("CUST_STL_TYPE");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custSetPeriod = rs.getString("CUST_SET_PERIOD");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custSetFrey = rs.getString("CUST_SET_FREY");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custStlTimeSet = rs.getString("CUST_STL_TIME_SET");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custSetPeriodAgent = rs.getString("CUST_SET_PERIOD_AGENT");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custSetFreyAgent = rs.getString("CUST_SET_FREY_AGENT");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custStlTimeSetAgent = rs.getString("CUST_STL_TIME_SET_AGENT");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.minStlBalance = rs.getLong("MIN_STL_BALANCE");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.creOperId = rs.getString("CRE_OPER_ID");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.creTime = rs.getTimestamp("CRE_TIME");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.lstUptOperId = rs.getString("LST_UPT_OPER_ID");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.lstUptTime = rs.getTimestamp("LST_UPT_TIME");}catch(Exception e){}
        try{stl.merchant.payCustStlInfo.custSetDay = rs.getString("CUST_SET_DAY");}catch(Exception e){}
        stl.stlPeriodAgent = rs.getString("STL_PERIOD_AGENT");
        stl.stlPeriodDaySetAgent = rs.getString("STL_PERIOD_DAY_SET_AGENT");
        stl.chargeWay = rs.getString("CHARGE_WAY");
//        stl.stlPeriodAgent = stl.merchant.payCustStlInfo.custSetPeriodAgent;
//        stl.stlPeriodDaySetAgent = "D".equals(stl.stlPeriodAgent)?"T+"+stl.merchant.payCustStlInfo.custSetFreyAgent:
//        	stl.merchant.payCustStlInfo.custStlTimeSetAgent;
        return stl;
    }
    public static synchronized PayMerchant getPayMerchantValue(ResultSet rs)throws SQLException {
        PayMerchant payMerchant = new PayMerchant();
        payMerchant.id = rs.getString("ID");
        payMerchant.custId = rs.getString("CUST_ID");
        payMerchant.storeName = rs.getString("STORE_NAME");
        payMerchant.storeShortname = rs.getString("STORE_SHORTNAME");
        payMerchant.merType = rs.getString("MER_TYPE");
        payMerchant.bizType = rs.getString("BIZ_TYPE");
        payMerchant.merLawPerson = rs.getString("MER_LAW_PERSON");
        payMerchant.payChannel = rs.getString("PAY_CHANNEL");
        payMerchant.techContact = rs.getString("TECH_CONTACT");
        payMerchant.techTelNo = rs.getString("TECH_TEL_NO");
        payMerchant.techEmail = rs.getString("TECH_EMAIL");
        payMerchant.bizContact = rs.getString("BIZ_CONTACT");
        payMerchant.bizTelNo = rs.getString("BIZ_TEL_NO");
        payMerchant.bizEmail = rs.getString("BIZ_EMAIL");
        payMerchant.servContact = rs.getString("SERV_CONTACT");
        payMerchant.servTelNo = rs.getString("SERV_TEL_NO");
        payMerchant.servEmail = rs.getString("SERV_EMAIL");
        payMerchant.merAddr = rs.getString("MER_ADDR");
        payMerchant.bizRange = rs.getString("BIZ_RANGE");
        payMerchant.merOpnBankCode = rs.getString("MER_OPN_BANK_CODE");
        payMerchant.bankStlAcNo = rs.getString("BANK_STL_AC_NO");
        payMerchant.rfCommFlg = rs.getString("RF_COMM_FLG");
        payMerchant.rfRecFeeFlg = rs.getString("RF_REC_FEE_FLG");
        payMerchant.city = rs.getString("CITY");
        payMerchant.postCode = rs.getString("POST_CODE");
        payMerchant.lawPersonType = rs.getString("LAW_PERSON_TYPE");
        payMerchant.lawPersonCretType = rs.getString("LAW_PERSON_CRET_TYPE");
        payMerchant.lawPersonCretNo = rs.getString("LAW_PERSON_CRET_NO");
        payMerchant.longTermSign = rs.getString("LONG_TERM_SIGN");
        payMerchant.certBeginDate = rs.getTimestamp("CERT_BEGIN_DATE");
        payMerchant.certEndDate = rs.getTimestamp("CERT_END_DATE");
        payMerchant.webAddress = rs.getString("WEB_ADDRESS");
        payMerchant.webAddressAbbr = rs.getString("WEB_ADDRESS_ABBR");
        payMerchant.contractStrDate = rs.getTimestamp("CONTRACT_STR_DATE");
        payMerchant.contractEndDate = rs.getTimestamp("CONTRACT_END_DATE");
        payMerchant.contractor = rs.getString("CONTRACTOR");
        payMerchant.contractBizNo = rs.getString("CONTRACT_BIZ_NO");
        payMerchant.contractBizName = rs.getString("CONTRACT_BIZ_NAME");
        payMerchant.techMobNo = rs.getString("TECH_MOB_NO");
        payMerchant.bizMobNo = rs.getString("BIZ_MOB_NO");
        payMerchant.servMobNo = rs.getString("SERV_MOB_NO");
        payMerchant.margin = rs.getLong("MARGIN");
        payMerchant.frozStlSign = rs.getString("FROZ_STL_SIGN");
        payMerchant.cretMasterityDays = rs.getLong("CRET_MASTERITY_DAYS");
        payMerchant.taxRegistrationNo = rs.getString("TAX_REGISTRATION_NO");
        payMerchant.organizationNo = rs.getString("ORGANIZATION_NO");
        payMerchant.cpsSign = rs.getString("CPS_SIGN");
        payMerchant.regCapital = rs.getLong("REG_CAPITAL");
        payMerchant.compayEmail = rs.getString("COMPAY_EMAIL");
        payMerchant.compayTelNo = rs.getString("COMPAY_TEL_NO");
        payMerchant.compayFax = rs.getString("COMPAY_FAX");
        payMerchant.cycleDays = rs.getLong("CYCLE_DAYS");
        payMerchant.codeTypeId = rs.getString("CODE_TYPE_ID");
        payMerchant.cert = rs.getString("CERT");
        payMerchant.companyBrief = rs.getString("COMPANY_BRIEF");
        payMerchant.riskLevel = rs.getString("RISK_LEVEL");
        payMerchant.bussinessType = rs.getString("BUSSINESS_TYPE");
        payMerchant.merStatus = rs.getString("MER_STATUS");
        payMerchant.createTime = rs.getTimestamp("CREATE_TIME");
        payMerchant.createUser = rs.getString("CREATE_USER");
        payMerchant.businessLicencePic = rs.getString("BUSINESS_LICENCE_PIC");
        payMerchant.taxRegistrationPic = rs.getString("TAX_REGISTRATION_PIC");
        payMerchant.organizationPic = rs.getString("ORGANIZATION_PIC");
        payMerchant.openingLicensesPic = rs.getString("OPENING_LICENSES_PIC");
        payMerchant.certPicFront = rs.getString("CERT_PIC_FRONT");
        payMerchant.certPicBack = rs.getString("CERT_PIC_BACK");
        payMerchant.contractPic = rs.getString("CONTRACT_PIC");
        payMerchant.icpNo = rs.getString("ICP_NO");
        return payMerchant;
    }
    /**
     * 添加结算信息
     * @param payMerchantSettlement
     * @param stlDay
     * @param daishouStlDay
     * @param settlementWay
     * @return
     * @throws Exception
     */
    public String addPayMerchantSettlement(PayMerchantSettlement payMerchantSettlement,
    		Date [] stlDay,Date [] daishouStlDay,String settlementWay) throws Exception {
        String sql = "insert into PAY_MERCHANT_SETTLEMENT("+
            "STL_ID," + 
            "STL_MER_ID," + 
            "STL_MER_CUSTNO," + 
            "STL_MER_PAYACNO," + 
            "STL_STLTYPE," + 
            "STL_APPLYSTLAMT," + 
            "STL_REFUND_AMT," + 
            (payMerchantSettlement.stlFromTime!=null?"STL_FROM_TIME,":"") + 
            (payMerchantSettlement.stlToTime!=null?"STL_TO_TIME,":"") + 
            "STL_STATUS," + 
            "STL_SEQNO," + 
            "STL_TYPE," + 
            "STL_FEE," + 
            "STL_TOTALORDAMT," + 
            "STL_TXNCOMAMT," + 
            "STL_NETRECAMT," + 
            "STL_AUTO_FLAG," + 
            "STL_APPLYSTL_COUNT," +
            "STL_APPL_DATE," +
            "STL_REFUND_COUNT," + 
            "STL_FEE_CODE," + 
            "STL_PERIOD," + 
            "STL_PERIOD_DAY_SET," +
            "SETTLEMENT_WAY," +
            "AGENT_STL_IN," +
            "AGENT_STL_OUT," + 
            "CHARGE_WAY," +
            (payMerchantSettlement.stlFromTimeAgent!=null?"STL_FROM_TIME_AGENT,":"") + 
            (payMerchantSettlement.stlToTimeAgent!=null?"STL_TO_TIME_AGENT,":"") + 
            "STL_PERIOD_AGENT," + 
            "STL_PERIOD_DAY_SET_AGENT," + 
            "STL_PERIOD_RECEIVE," +
            (payMerchantSettlement.stlFromTimeReceive!=null?"STL_FROM_TIME_RECEIVE,":"") + 
            (payMerchantSettlement.stlToTimeReceive!=null?"STL_TO_TIME_RECEIVE,":"") + 
            "STL_PERIOD_SET_RECEIVE," +
            "RECEIVE_COUNT," + 
            "RECEIVE_AMT," + 
            "RECEIVE_FEE," +
            "STL_AMT_OVER," +
            "STL_AMT_FEE_OVER" +
            ")" +
            "values(?,?,?,?,?,?,?," +
            (payMerchantSettlement.stlFromTime!=null?"to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
            (payMerchantSettlement.stlToTime!=null?"to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
            "?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?," +
            (payMerchantSettlement.stlFromTimeAgent!=null?"to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
            (payMerchantSettlement.stlToTimeAgent!=null?"to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
            "?,?,?," +
            (payMerchantSettlement.stlFromTimeReceive!=null?"to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
            (payMerchantSettlement.stlToTimeReceive!=null?"to_date(?,'yyyy-mm-dd hh24:mi:ss'),":"") +
            "?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payMerchantSettlement.stlId);
            ps.setString(n++,payMerchantSettlement.stlMerId);
            ps.setString(n++,payMerchantSettlement.stlMerCustno);
            ps.setString(n++,payMerchantSettlement.stlMerPayacno);
            ps.setString(n++,payMerchantSettlement.stlStltype);
            ps.setLong(n++,payMerchantSettlement.stlApplystlamt);
            ps.setLong(n++,payMerchantSettlement.stlRefundAmt);
            if(payMerchantSettlement.stlFromTime!=null)ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlFromTime));
            if(payMerchantSettlement.stlToTime!=null)ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlToTime));
            ps.setString(n++,payMerchantSettlement.stlStatus);
            ps.setString(n++,payMerchantSettlement.stlSeqno);
            ps.setString(n++,payMerchantSettlement.stlType);
            ps.setLong(n++,payMerchantSettlement.stlFee);
            ps.setLong(n++,payMerchantSettlement.stlTotalordamt);
            ps.setLong(n++,payMerchantSettlement.stlTxncomamt);
            ps.setLong(n++,payMerchantSettlement.stlNetrecamt);
            ps.setString(n++,payMerchantSettlement.stlAutoFlag);
            ps.setLong(n++,payMerchantSettlement.stlApplystlCount);
            ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlApplDate));
            ps.setLong(n++,payMerchantSettlement.stlRefundCount);
            ps.setString(n++,payMerchantSettlement.stlFeeCode);
            ps.setString(n++,payMerchantSettlement.stlPeriod);
            ps.setString(n++,payMerchantSettlement.stlPeriodDaySet);
            ps.setString(n++,payMerchantSettlement.settlementWay);
            ps.setLong(n++,payMerchantSettlement.agentStlIn);
            ps.setLong(n++,payMerchantSettlement.agentStlOut);
            ps.setString(n++,payMerchantSettlement.chargeWay);
            if(payMerchantSettlement.stlFromTimeAgent!=null)ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlFromTimeAgent));
            if(payMerchantSettlement.stlToTimeAgent!=null)ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlToTimeAgent));
            ps.setString(n++,payMerchantSettlement.stlPeriodAgent);
            ps.setString(n++,payMerchantSettlement.stlPeriodDaySetAgent);
            ps.setString(n++,payMerchantSettlement.stlPeriodReceive);
            if(payMerchantSettlement.stlFromTimeReceive!=null)ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlFromTimeReceive));
            if(payMerchantSettlement.stlToTimeReceive!=null)ps.setString(n++,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantSettlement.stlToTimeReceive));
            ps.setString(n++,payMerchantSettlement.stlPeriodSetReceive);
            ps.setLong(n++,payMerchantSettlement.receiveCount);
            ps.setLong(n++,payMerchantSettlement.receiveAmt);
            ps.setLong(n++,payMerchantSettlement.receiveFee);
            ps.setLong(n++,payMerchantSettlement.stlAmtOver);
            ps.setLong(n++,payMerchantSettlement.stlAmtFeeOver);
            //插入结算信息
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * 更新消费、代收结算信息
     * @param payMerchantSettlement
     * @param stlDay
     * @param daishouStlDay
     * @param settlementWay
     * @return
     * @throws Exception
     */
    public String updateTransStlInfo(PayMerchantSettlement payMerchantSettlement,
    		Date [] stlDay,Date [] daishouStlDay,String settlementWay) throws Exception {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "";
        try {
            con = connection();
            con.setAutoCommit(false);
            int n=1;
            //结算方式 0自动结算到虚拟账户 1线下打款 2手动结算到虚拟账户 3实时结算到虚拟账户
            String stlsts = "0".equals(settlementWay)||"3".equals(settlementWay)?"2":"1";//清结算状态 0初始 1清算完成 2结算完成 默认0
            if(stlDay != null){
	            //修改订单清结算状态，清算流水号、清算日期、清算时间、结算状态
	            sql = "update PAY_PRODUCT_ORDER pr set ACJRNNO=?,ACDATE=sysdate,ACTIME=sysdate,STLSTS='"+stlsts+"',STL_TIME=sysdate "+
						" where exists(select 1 from PAY_ORDER po where pr.MERNO=po.MERNO and pr.PRDORDNO=po.PRDORDNO "+
						" and po.ORDSTATUS='01' and STLSTS!='2' and po.MERNO=?  and po.CREATETIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') " + 
						" and po.CREATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
	            log.info(sql);
	            ps = con.prepareStatement(sql);
	            ps.setString(n++, payMerchantSettlement.stlId);
	            ps.setString(n++, payMerchantSettlement.stlMerId);
	            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payMerchantSettlement.stlFromTime)+" 00:00:00");
	            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payMerchantSettlement.stlToTime)+" 23:59:59");
	            ps.executeUpdate();
	            ps.close();
	            //修改订单清结算状态，清算流水号、清算日期、清算时间、结算状态
	            sql = "UPDATE PAY_REFUND SET STLBATNO=?, BANKSTLDATE=sysdate, STLSTS='"+stlsts+"'"+
	            	"WHERE MERNO=? and RFORDTIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and RFORDTIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
	            log.info(sql);
	            n = 1;
	            ps = con.prepareStatement(sql);
	            ps.setString(n++, payMerchantSettlement.stlId);
	            ps.setString(n++, payMerchantSettlement.stlMerId);
	            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payMerchantSettlement.stlFromTime)+" 00:00:00");
	            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payMerchantSettlement.stlToTime)+" 23:59:59");
	            ps.executeUpdate();
            }
            if(daishouStlDay != null){//更新代收记录
            	ps.close();
	            //修改订单清结算状态，清算流水号、清算日期、清算时间、结算状态
	            sql = "UPDATE PAY_RECEIVE_AND_PAY SET STLBATNO=?, ACTIME=sysdate, STLSTS='"+stlsts+"', STL_TIME =sysdate " +
	            	"WHERE TYPE='0' and BUSS_FROM_TYPE='0' and STATUS='1' and CUST_ID=? and CREATE_TIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATE_TIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
	            log.info(sql);
	            n = 1;
	            ps = con.prepareStatement(sql);
	            ps.setString(n++, payMerchantSettlement.stlId);
	            ps.setString(n++, payMerchantSettlement.stlMerId);
	            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payMerchantSettlement.stlFromTime)+" 00:00:00");
	            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payMerchantSettlement.stlToTime)+" 23:59:59");
	            ps.executeUpdate();
            }
            con.commit();
            return "";
        } catch (Exception e) {
        	con.rollback();
            e.printStackTrace();
            throw e;
        } finally {
        	con.setAutoCommit(true);
            close(null, ps, con);
        }
    }
    /**
     * 未结算消费总金额、笔数、结算额、手续费
     * @param mer
     * @param stlDayStart
     * @param stlDayEnd
     * @return
     */
	public Long [] getStlTotalAmt(PayMerchant mer, String stlDayStart,String stlDayEnd) {
		String sql ="select sum(TXAMT) txamt_sum,count(PAYORDNO) tx_count,sum(NETRECAMT) netrecamt_sum,sum(FEE) fee_sum from " +
				"(select h.*,p.stlsts from PAY_ORDER_HISTORY h left join PAY_PRODUCT_ORDER p on h.merno=p.merno and h.prdordno=p.prdordno)PAY_ORDER_HISTORY where ORDSTATUS='01' and PRDORDTYPE='0' " +
				"and CREATETIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss') and MERNO='"+mer.custId+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,stlDayStart+" 00:00:00");
            ps.setString(2,stlDayEnd+" 23:59:59");
            rs = ps.executeQuery();
            if (rs.next()) {
            	Long l1 = rs.getLong("txamt_sum");
            	Long l2 = rs.getLong("netrecamt_sum");
            	Long l3 = rs.getLong("fee_sum");
                return new Long[]{
                	l1==null?0l:l1,
                	rs.getLong("tx_count"),
                	l2==null?0l:l2,
                	l3==null?0l:l3
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
    /**
     * 已结算消费总金额、笔数、结算额、手续费
     * @param mer
     * @param stlDayStart
     * @param stlDayEnd
     * @return
     */
	public Long [] getStlTotalAmtOver(PayMerchant mer, String stlDayStart,String stlDayEnd) {
		String sql ="select sum(TXAMT) txamt_sum,count(PAYORDNO) tx_count,sum(NETRECAMT) netrecamt_sum,sum(FEE) fee_sum from " +
				"(select h.*,p.stlsts from PAY_ORDER_HISTORY h left join PAY_PRODUCT_ORDER p on h.merno=p.merno and h.prdordno=p.prdordno)PAY_ORDER_HISTORY where ORDSTATUS='01' and PRDORDTYPE='0' " +
				"and stlsts='2' and CREATETIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss') and MERNO='"+mer.custId+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,stlDayStart+" 00:00:00");
            ps.setString(2,stlDayEnd+" 23:59:59");
            rs = ps.executeQuery();
            if (rs.next()) {
            	Long l1 = rs.getLong("txamt_sum");
            	Long l2 = rs.getLong("netrecamt_sum");
            	Long l3 = rs.getLong("fee_sum");
                return new Long[]{
                	l1==null?0l:l1,
                	rs.getLong("tx_count"),
                	l2==null?0l:l2,
                	l3==null?0l:l3
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
//	/**
//     * 结算消费总手续费
//     * @param mer
//     * @param stlDayStart
//     * @param stlDayEnd
//     * @return
//     */
//	public Long [] getStlTotalFeeAmt(PayMerchant mer, String stlDayStart,String stlDayEnd) {
//		String sql ="select sum(FEE) total_fee_amt,sum(NETRECAMT) total_stl_amt from " +
//				"(select h.*,p.stlsts from PAY_ORDER_HISTORY h left join PAY_PRODUCT_ORDER p on h.merno=p.merno and h.prdordno=p.prdordno)PAY_ORDER_HISTORY where ORDSTATUS='01' and PRDORDTYPE='0' " +
//				"and CREATETIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss') and MERNO='"+mer.custId+"'";
//        log.info(sql);
//        Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            con = connection();
//            ps = con.prepareStatement(sql);
//            ps.setString(1,stlDayStart+" 00:00:00");
//            ps.setString(2,stlDayEnd+" 23:59:59");
//            rs = ps.executeQuery();
//            if (rs.next()) {
//            	Long l1 = rs.getLong("total_fee_amt");
//            	Long l2 = rs.getLong("total_stl_amt");
//                return new Long[]{l1==null?0l:l1,l2==null?0l:l2};
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close(rs, ps, con);
//        }
//        return null;
//	}
//	/**
//     * 已结算消费总手续费
//     * @param mer
//     * @param stlDayStart
//     * @param stlDayEnd
//     * @return
//     */
//	public Long [] getStlTotalFeeAmtOver(PayMerchant mer, String stlDayStart,String stlDayEnd) {
//		String sql ="select sum(FEE) total_fee_amt,sum(NETRECAMT) total_stl_amt from " +
//				"(select h.*,p.stlsts from PAY_ORDER_HISTORY h left join PAY_PRODUCT_ORDER p on h.merno=p.merno and h.prdordno=p.prdordno)PAY_ORDER_HISTORY where ORDSTATUS='01' and PRDORDTYPE='0' " +
//				"and stlsts='2' and CREATETIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss') and MERNO='"+mer.custId+"'";
//        log.info(sql);
//        Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            con = connection();
//            ps = con.prepareStatement(sql);
//            ps.setString(1,stlDayStart+" 00:00:00");
//            ps.setString(2,stlDayEnd+" 23:59:59");
//            rs = ps.executeQuery();
//            if (rs.next()) {
//            	Long l1 = rs.getLong("total_fee_amt");
//            	Long l2 = rs.getLong("total_stl_amt");
//                return new Long[]{l1==null?0l:l1,l2==null?0l:l2};
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            close(rs, ps, con);
//        }
//        return null;
//	}
    /**
     * 结算代收总金额
     * @param mer
     * @param stlDayStart
     * @param stlDayEnd
     * @return
     */
	public Long [] getStlTotalAmtReceive(PayMerchant mer, String stlDayStart,String stlDayEnd) {
		String sql ="select sum(AMOUNT) total_amt,count(ID) total_count,sum(FEE) total_fee_amt from PAY_RECEIVE_AND_PAY where TYPE='0' and BUSS_FROM_TYPE='0' and STATUS='1' " +
				"and CREATE_TIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATE_TIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CUST_ID='"+mer.custId+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,stlDayStart+" 00:00:00");
            ps.setString(2,stlDayEnd+" 23:59:59");
            rs = ps.executeQuery();
            if (rs.next()) {
            	Long totalAmt = rs.getLong("total_amt");
            	Long totalCount = rs.getLong("total_count");
            	Long totalFeeAmt = rs.getLong("total_fee_amt");
                return new Long[]{totalAmt==null?0l:totalAmt,totalCount==null?0l:totalCount,totalFeeAmt==null?0l:totalFeeAmt};
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
    /**
     * 已结算结算代收总金额
     * @param mer
     * @param stlDayStart
     * @param stlDayEnd
     * @return
     */
	public Long [] getStlTotalAmtReceiveOver(PayMerchant mer, String stlDayStart,String stlDayEnd) {
		String sql ="select sum(AMOUNT) total_amt,count(ID) total_count,sum(FEE) total_fee_amt from PAY_RECEIVE_AND_PAY where TYPE='0' and BUSS_FROM_TYPE='0' and STATUS='1' " +
				"and stlsts='2' and CREATE_TIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATE_TIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CUST_ID='"+mer.custId+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,stlDayStart+" 00:00:00");
            ps.setString(2,stlDayEnd+" 23:59:59");
            rs = ps.executeQuery();
            if (rs.next()) {
            	Long totalAmt = rs.getLong("total_amt");
            	Long totalCount = rs.getLong("total_count");
            	Long totalFeeAmt = rs.getLong("total_fee_amt");
                return new Long[]{totalAmt==null?0l:totalAmt,totalCount==null?0l:totalCount,totalFeeAmt==null?0l:totalFeeAmt};
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
    /**
     * Set query condition sql.
     * @param payMerchantSettlement
     * @return
     */
    private String setPayMerchantSettlementSql(PayMerchantSettlement payMerchantSettlement) {
        StringBuffer sql = new StringBuffer();
        if(payMerchantSettlement.stlId != null && payMerchantSettlement.stlId.length() !=0) {
            sql.append(" STL_ID = ? and ");
        }
        if(payMerchantSettlement.stlMerId != null && payMerchantSettlement.stlMerId.length() !=0) {
            sql.append(" STL_MER_ID = ? and ");
        }
        if(payMerchantSettlement.storeName != null && payMerchantSettlement.storeName.length() !=0) {
            sql.append(" STORE_NAME like ? and ");
        }
        if(payMerchantSettlement.stlFromTime != null) {
            sql.append(" STL_FROM_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payMerchantSettlement.stlToTime != null) {
            sql.append(" STL_TO_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payMerchantSettlement.stlApplDateStart != null && payMerchantSettlement.stlApplDateStart.length()!=0) {
            sql.append(" STL_APPL_DATE >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payMerchantSettlement.stlApplDateEnd != null && payMerchantSettlement.stlApplDateEnd.length()!=0) {
            sql.append(" STL_APPL_DATE <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payMerchantSettlement.stlStatus != null && payMerchantSettlement.stlStatus.length() !=0) {
            sql.append(" STL_STATUS = ? and ");
        }
        if(payMerchantSettlement.searchCustSetPeriod != null && payMerchantSettlement.searchCustSetPeriod.length() !=0) {
            sql.append(" STL_PERIOD = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payMerchantSettlement
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayMerchantSettlementParameter(PreparedStatement ps,PayMerchantSettlement payMerchantSettlement,int n)throws SQLException {
    	if(payMerchantSettlement.stlId != null && payMerchantSettlement.stlId.length() !=0) {
    		ps.setString(n++,payMerchantSettlement.stlId);
    	}
        if(payMerchantSettlement.stlMerId != null && payMerchantSettlement.stlMerId.length() !=0) {
            ps.setString(n++,payMerchantSettlement.stlMerId);
        }
        if(payMerchantSettlement.storeName != null && payMerchantSettlement.storeName.length() !=0) {
        	ps.setString(n++,"%"+payMerchantSettlement.storeName+"%");
        }
        if(payMerchantSettlement.stlFromTime != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payMerchantSettlement.stlFromTime)+" 00:00:00");
        }
        if(payMerchantSettlement.stlToTime != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payMerchantSettlement.stlToTime)+" 23:59:59");
        }
        if(payMerchantSettlement.stlApplDateStart != null && payMerchantSettlement.stlApplDateStart.length()!=0) {
        	ps.setString(n++, payMerchantSettlement.stlApplDateStart+" 00:00:00");
        }
        if(payMerchantSettlement.stlApplDateEnd != null && payMerchantSettlement.stlApplDateEnd.length()!=0) {
        	ps.setString(n++, payMerchantSettlement.stlApplDateEnd+" 23:59:59");
        }
        if(payMerchantSettlement.stlStatus != null && payMerchantSettlement.stlStatus.length() !=0) {
            ps.setString(n++,payMerchantSettlement.stlStatus);
        }
        if(payMerchantSettlement.searchCustSetPeriod != null && payMerchantSettlement.searchCustSetPeriod.length() !=0) {
        	ps.setString(n++,payMerchantSettlement.searchCustSetPeriod);
        }
        if(payMerchantSettlement.settlementWay != null && payMerchantSettlement.settlementWay.length() !=0) {
        	ps.setString(n++,payMerchantSettlement.settlementWay);
        }
        return n;
    }
    /**
     * 清除结算表、清除历史订单表、插入历史支付订单
     * @param runDay 跑批日期
     * @param transDay 交易日期
     * @param merNo
     * @return 手动跑批情况下，返回老的结算到虚拟账户的数据
     * @throws Exception
     */
    public Map <String,Long []> clearPayMerchantSettlement(String runDay,String transDay,String merNo) throws Exception {
    	String oldStlSql = "select to_char(STL_APPL_DATE,'yyyymmdd') STL_DATE,STL_MER_ID,STL_NETRECAMT,STL_FEE+RECEIVE_FEE-STL_AMT_FEE_OVER STL_FEE" +
    			",CHARGE_WAY,SETTLEMENT_WAY,STL_STATUS,STL_AMT_OVER,STL_AMT_FEE_OVER from PAY_MERCHANT_SETTLEMENT where " +
            	"STL_APPL_DATE>=to_date('"+runDay+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and " +
            	"STL_APPL_DATE<=to_date('"+runDay+" 23:59:59','yyyy-mm-dd hh24:mi:ss')"+
            	(merNo!=null && merNo.length()>0?" and STL_MER_ID=? ":"");
        String sqlDelSettle = "delete from PAY_MERCHANT_SETTLEMENT where " +
        	"STL_APPL_DATE>=to_date('"+runDay+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and " +
        	"STL_APPL_DATE<=to_date('"+runDay+" 23:59:59','yyyy-mm-dd hh24:mi:ss')"+
        	(merNo!=null && merNo.length()>0?" and STL_MER_ID=? ":"");
        String sqlDelOrder = "delete from PAY_ORDER_HISTORY where " +
        	"CREATETIME>=to_date('"+transDay+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and " +
        	"CREATETIME<=to_date('"+transDay+" 23:59:59','yyyy-mm-dd hh24:mi:ss') " +
        	(merNo!=null && merNo.length()>0?" and MERNO=? ":"");
        String sqlAddOrder = "insert into PAY_ORDER_HISTORY(SELECT * FROM PAY_ORDER WHERE " +
        	"CREATETIME>=to_date('"+transDay+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and " +
        	"CREATETIME<=to_date('"+transDay+" 23:59:59','yyyy-mm-dd hh24:mi:ss')" +
        	(merNo!=null && merNo.length()>0?" and MERNO=? ":"")+
        	")";
        log.info(oldStlSql);
        log.info(sqlDelSettle);
        log.info(sqlDelOrder);
        log.info(sqlAddOrder);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map <String,Long []>oldStlRecord = new HashMap<String,Long []>();
        try {
            con = connection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(oldStlSql);
            if(merNo!=null && merNo.length()>0)ps.setString(1, merNo);
            rs = ps.executeQuery();
            while(rs.next()) oldStlRecord.put(rs.getString("STL_DATE")+","+rs.getString("STL_MER_ID"),
            	new Long [] {rs.getLong("STL_NETRECAMT"),rs.getLong("STL_FEE"),rs.getLong("CHARGE_WAY")
            		,rs.getLong("SETTLEMENT_WAY"),rs.getLong("STL_STATUS"),rs.getLong("STL_AMT_OVER"),rs.getLong("STL_AMT_FEE_OVER")});
            ps.close();
            ps = con.prepareStatement(sqlDelSettle);
            if(merNo!=null && merNo.length()>0)ps.setString(1, merNo);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sqlDelOrder);
            if(merNo!=null && merNo.length()>0)ps.setString(1, merNo);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sqlAddOrder);
            if(merNo!=null && merNo.length()>0)ps.setString(1, merNo);
            ps.executeUpdate();
            con.commit();
        } catch (Exception e) {
        	con.rollback();
            e.printStackTrace();
            throw e;
        } finally {
        	con.setAutoCommit(true);
            close(rs, ps, con);
        }
        return oldStlRecord;
    }
    /**
     * 取得结算商户
     * @return
     * @throws Exception
     */
    public List getSettleMerchant() throws Exception{        
        String sql = "select * from PAY_MERCHANT";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayMerchantValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * Get records count.
     * @param payMerchantSettlement
     * @return
     */
    public int getPayMerchantSettlementCount(PayMerchantSettlement payMerchantSettlement) {
        String sqlCon = setPayMerchantSettlementSql(payMerchantSettlement);
        if(payMerchantSettlement.settlementWay != null && payMerchantSettlement.settlementWay.length() !=0) {
        	if( !"".equals(sqlCon)){
        		sqlCon += " and SETTLEMENT_WAY = ? ";
        	}else{
        		sqlCon += " SETTLEMENT_WAY = ? ";
        	}
        }
        String sql = "select count(rownum) recordCount from " +
        		"(select stl.*,merStl.*,mer.STORE_NAME from PAY_MERCHANT_SETTLEMENT stl left join PAY_MERCHANT mer on stl.STL_MER_ID=mer.CUST_ID " +
                "	left join PAY_CUST_STL_INFO merStl on mer.CUST_ID=merStl.CUST_ID)tmp " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayMerchantSettlementParameter(ps,payMerchantSettlement,n);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("recordCount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return 0;
    }
    /**
     * Get records list.
     * @param payMerchantSettlement
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayMerchantSettlementList(PayMerchantSettlement payMerchantSettlement,int page,
    		int rows,String sort,String order,Map sysUserMap) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayMerchantSettlementSql(payMerchantSettlement);
        if(payMerchantSettlement.settlementWay != null && payMerchantSettlement.settlementWay.length() !=0) {
        	if( !"".equals(sqlCon)){
        		sqlCon += " and SETTLEMENT_WAY = ? ";
        	}else{
        		sqlCon += " SETTLEMENT_WAY = ? ";
        	}
        }
        String sortOrder = sort == null || sort.length()==0?" order by STL_APPL_DATE desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from " +
                "(select stl.*,merStl.*,mer.STORE_NAME,fee.FEE_NAME STL_FEE_NAME from PAY_MERCHANT_SETTLEMENT stl left join PAY_MERCHANT mer on stl.STL_MER_ID=mer.CUST_ID " +
                "	left join PAY_CUST_STL_INFO merStl on mer.CUST_ID=merStl.CUST_ID left join PAY_FEE_RATE fee on stl.STL_FEE_CODE=fee.FEE_CODE) tmp " +
                (sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        String sysUserIds = "";
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayMerchantSettlementParameter(ps,payMerchantSettlement,n);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayMerchantSettlement stl = getPayMerchantSettlementValue(rs);
            	if(stl.stlApplicants != null && stl.stlApplicants.length()>0)sysUserIds=sysUserIds+" ID='"+stl.stlApplicants+"' or ";
            	if(stl.stlAuditPer != null && stl.stlAuditPer.length()>0)sysUserIds=sysUserIds+" ID='"+stl.stlAuditPer+"' or ";
            	if(stl.stlSucOperator != null && stl.stlSucOperator.length()>0)sysUserIds=sysUserIds+" ID='"+stl.stlSucOperator+"' or ";
                list.add(stl);
            }
            if(sysUserIds.length()>0){
            	sysUserIds = sysUserIds.substring(0,sysUserIds.length()-4);
            	rs.close();
            	ps.close();
            	ps = con.prepareStatement("select * from PAY_JWEB_USER where "+sysUserIds);
            	rs = ps.executeQuery();
            	while(rs.next()){
            		JWebUser sysUser = JWebUserDAO.getJwebUserValue(rs);
            		sysUserMap.put(sysUser.id, sysUser);
            	}
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    
  //用于导出excel提供的列表数据操作方法。
    public List getPayMerchantSettlementList_excel(PayMerchantSettlement payMerchantSettlement,long page,
    		long rows,String sort,String order,Map sysUserMap) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayMerchantSettlementSql(payMerchantSettlement);
        if(payMerchantSettlement.settlementWay != null && payMerchantSettlement.settlementWay.length() !=0) {
        	if( !"".equals(sqlCon)){
        		sqlCon += " and SETTLEMENT_WAY = ? ";
        	}else{
        		sqlCon += " SETTLEMENT_WAY = ? ";
        	}
        }
        String sortOrder = sort == null || sort.length()==0?" order by STL_ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from " +
                "(select stl.*,merStl.*,mer.STORE_NAME,fee.FEE_NAME STL_FEE_NAME from PAY_MERCHANT_SETTLEMENT stl left join PAY_MERCHANT mer on stl.STL_MER_ID=mer.CUST_ID " +
                "	left join PAY_CUST_STL_INFO merStl on mer.CUST_ID=merStl.CUST_ID left join PAY_FEE_RATE fee on stl.STL_FEE_CODE=fee.FEE_CODE) tmp " +
                (sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "  ) tmp1 "+
                ")  where rowno > "+page+ " and rowno<= " + rows+ sortOrder;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        String sysUserIds = "";
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayMerchantSettlementParameter(ps,payMerchantSettlement,n);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayMerchantSettlement stl = getPayMerchantSettlementValue(rs);
            	if(stl.stlApplicants != null && stl.stlApplicants.length()>0)sysUserIds=sysUserIds+" ID='"+stl.stlApplicants+"' or ";
            	if(stl.stlAuditPer != null && stl.stlAuditPer.length()>0)sysUserIds=sysUserIds+" ID='"+stl.stlAuditPer+"' or ";
            	if(stl.stlSucOperator != null && stl.stlSucOperator.length()>0)sysUserIds=sysUserIds+" ID='"+stl.stlSucOperator+"' or ";
                list.add(stl);
            }
            if(sysUserIds.length()>0){
            	
            	sysUserIds = sysUserIds.substring(0,sysUserIds.length()-4);
            	rs.close();
            	ps.close();
            	ps = con.prepareStatement("select * from PAY_JWEB_USER where "+sysUserIds);
            	rs = ps.executeQuery();
            	while(rs.next()){
            		JWebUser sysUser = JWebUserDAO.getJwebUserValue(rs);
            		sysUserMap.put(sysUser.id, sysUser);
            	}
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * recheck PayMerchantSettlement
     * @param payMerchantSettlement
     * @throws Exception     
     */
    public void reCheckSettlement(PayMerchantSettlement payMerchantSettlement) throws Exception {
        String sql = "update PAY_MERCHANT_SETTLEMENT set STL_AUDIT_PER=?," +
        		" STL_AUDIT_TIME=sysdate,STL_AUDIT_IP=?,STL_STATUS=?," +
        		" REMARK = REMARK||? where STL_ID=? and STL_STATUS='1'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,payMerchantSettlement.stlAuditPer);
            ps.setString(2,payMerchantSettlement.stlAuditIp);
            ps.setString(3,payMerchantSettlement.stlStatus);
            ps.setString(4,payMerchantSettlement.remark==null||payMerchantSettlement.remark.length()==0
            		?"":"；"+payMerchantSettlement.remark);
            ps.setString(5,payMerchantSettlement.stlId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * set result PayMerchantSettlement
     * @param payMerchantSettlement
     * @throws Exception     
     */
    public void setResultSettlement(PayMerchantSettlement stl) throws Exception {
        String sql = "update PAY_MERCHANT_SETTLEMENT set STL_SUC_OPERATOR=?," +
        	" STL_SUC_TIME=sysdate,STL_STATUS=?," +
        	" REMARK = REMARK||?"+("4".equals(stl.stlStatus)?",STL_NETRECAMT=0,STL_AMT_OVER=STL_AMT_OVER+?,STL_AMT_FEE_OVER=STL_FEE+RECEIVE_FEE ":"")
        	+" where STL_ID=? and STL_STATUS='2'";
        String sql2 = "update PAY_PRODUCT_ORDER set stlsts='2' where stlsts='1' and ACJRNNO=?";
        String sql3 = "update PAY_RECEIVE_AND_PAY set stlsts='2' where stlsts='1' and STLBATNO=?";
        String sql4 = "update PAY_REFUND set stlsts='2' where stlsts='1' and STLBATNO=?";
        log.info(sql);
        log.info("STL_SUC_OPERATOR="+stl.stlSucOperator+";STL_STATUS="+stl.stlStatus+";REMARK="+stl.remark+";STL_AMT_OVER="+stl.stlAmtOver+";STL_ID="+stl.stlId);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,stl.stlSucOperator);
            ps.setString(n++,stl.stlStatus);
            ps.setString(n++,stl.remark==null||stl.remark.length()==0?"":stl.remark+"；");
            if("4".equals(stl.stlStatus))ps.setLong(n++,stl.stlNetrecamt);
            ps.setString(n++,stl.stlId);
            ps.executeUpdate();
            ps.close();
            //更新订单结算状态
            ps = con.prepareStatement(sql2);
            ps.setString(1,stl.stlId);
            ps.executeUpdate();
            ps.close();
            //更新代收结算状态
            ps = con.prepareStatement(sql3);
            ps.setString(1,stl.stlId);
            ps.executeUpdate();
            ps.close();
            //更新退款结算状态
            ps = con.prepareStatement(sql4);
            ps.setString(1,stl.stlId);
            ps.executeUpdate();
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            con.rollback();
            throw e;
        } finally {
        	con.setAutoCommit(true);
            close(null, ps, con);
        }
    }
    /**
     * detail PayMerchantSettlement
     * @param stlId
     * @return PayMerchantSettlement
     * @throws Exception
     */
    public PayMerchantSettlement detailPayMerchantSettlement(String stlId) throws Exception {
        String sql = "select * from PAY_MERCHANT_SETTLEMENT where STL_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,stlId);
            rs = ps.executeQuery();
            if(rs.next())return getPayMerchantSettlementValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
//    /**
//     * detail PayMerchantSettlement
//     * @param stlId
//     * @return PayMerchantSettlement
//     * @throws Exception
//     */
//    public PayMerchantSettlement getPayMerchantSettlementByDate(PayMerchantSettlement stl) throws Exception {
//        String sql = "select * from PAY_MERCHANT_SETTLEMENT where to_char(STL_APPL_DATE,'yyyymmdd')=? and SETTLEMENT_WAY='0' and STL_STATUS='4' and STL_MER_ID=?";
//        log.info(sql);
//        Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            con = connection();
//            ps = con.prepareStatement(sql);
//            int n=1;
//            ps.setString(n++,new SimpleDateFormat("yyyyMMdd").format(stl.stlApplDate));
//            ps.setString(n++,stl.stlMerId);
//            rs = ps.executeQuery();
//            if(rs.next())return getPayMerchantSettlementValue(rs); 
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            close(rs, ps, con);
//        }
//        return null;
//    }
    /**
     * applay PayMerchantSettlement
     * @param payMerchantSettlement
     * @throws Exception
     */
    public void applyPayMerchantSettlement(String [] stlId,PayMerchantSettlement stl) throws Exception {
        String sqlTmp = "";
        for(int i = 0; i<stlId.length; i++){
        	if(stlId[i].length()>20)throw new Exception("批次号非法");
        	sqlTmp = sqlTmp + " STL_ID='"+stlId[i]+"' or ";
        }
        if(sqlTmp.length() == 0)return;
        sqlTmp = sqlTmp.substring(0,sqlTmp.length()-4);
        String sql = "update PAY_MERCHANT_SETTLEMENT "+        
              "set STL_APPLICANTS='"+stl.stlApplicants+"',STL_APPL_TIME=sysdate,STL_APPL_IP='"+stl.stlApplIp+"',STL_STATUS='1'" +
              " where STL_STATUS='0' and ("+sqlTmp+")"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 通过结算ID取得结算号码
     * @param stlId
     * @return
     * @throws Exception
     */
    public List getStlByIds(String [] stlId) throws Exception {
    	List list = new ArrayList();
        String sqlTmp = "";
        for(int i = 0; i<stlId.length; i++)sqlTmp = sqlTmp + " STL_ID='"+stlId[i]+"' or ";
        if(sqlTmp.length() == 0)return list;
        sqlTmp = sqlTmp.substring(0,sqlTmp.length()-4);
        String sql = "select * from PAY_MERCHANT_SETTLEMENT where ("+sqlTmp+")"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayMerchantSettlementValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * reApplay PayMerchantSettlement
     * @param payMerchantSettlement
     * @throws Exception
     */
    public void reApplyPayMerchantSettlement(String [] stlId,PayMerchantSettlement stl) throws Exception {
        String sqlTmp = "";
        for(int i = 0; i<stlId.length; i++){
        	if(stlId[i].length()>20)throw new Exception("批次号非法");
        	sqlTmp = sqlTmp + " STL_ID='"+stlId[i]+"' or ";
        }
        if(sqlTmp.length() == 0)return;
        sqlTmp = sqlTmp.substring(0,sqlTmp.length()-4);
        String sql = "update PAY_MERCHANT_SETTLEMENT "+        
              "set STL_APPLICANTS='"+stl.stlApplicants+"',STL_APPL_TIME=sysdate,STL_APPL_IP='"+stl.stlApplIp+"',STL_STATUS='1'" +
              " where (STL_STATUS='3' or STL_STATUS='5') and ("+sqlTmp+")"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 根据结算批次号取得结算信息
     * @param id
     * @return
     * @throws Exception
     */
    public PayMerchantSettlement getSettlementById(String id) throws Exception {
        String sql = "select * from PAY_MERCHANT_SETTLEMENT where STL_ID='"+id+"'"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next())return getPayMerchantSettlementValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * 更新退款信息
     * @param stl
     * @throws Exception
     */
    public void updateSettlementForRefund(PayMerchantSettlement stl) throws Exception {
        String sql = "update PAY_MERCHANT_SETTLEMENT "+        
              "set STL_REFUND_COUNT="+stl.stlRefundCount+",STL_REFUND_AMT="+stl.stlRefundAmt+",STL_NETRECAMT="+
        		stl.stlNetrecamt+",REMARK='"+stl.remark+"'" +" where STL_ID='"+stl.stlId+"'"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	/**
     *通过用户账号ID，查询用户表信息。
     */
    public  String  select_user_messageByID(String userid){
    	
    	String userName=null;
    	String sql = "select * from PAY_JWEB_USER where id='"+userid+"'";
    	log.info(sql);
    	 Connection con = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
         try {
             con = connection();
             ps = con.prepareStatement(sql);
             rs = ps.executeQuery();
         	while(rs.next()){
         		userName=rs.getString("name");
         	}
         } catch (Exception e) {
             e.printStackTrace();
         }finally {
             close(null, ps, con);
         }
    	
    	return userName;
    }
    /**
     * 查询结算的总金额
     * @param payMerchantSettlement
     * @return
     */
	public Long[] getTotalSettlementMoney(
			PayMerchantSettlement payMerchantSettlement) {
		String sqlCon = setPayMerchantSettlementSql(payMerchantSettlement);
		if(payMerchantSettlement.settlementWay != null && payMerchantSettlement.settlementWay.length() !=0) {
			if( !"".equals(sqlCon)){
        		sqlCon += " and STL.SETTLEMENT_WAY = ? ";
        	}else{
        		sqlCon += " STL.SETTLEMENT_WAY = ? ";
        	}
        }
        String sql = 
        		" SELECT SUM(tmp.STL_APPLYSTL_COUNT) totalApplyCount,SUM(tmp.STL_APPLYSTLAMT) totalApplyAmt,SUM(tmp.STL_REFUND_COUNT) totalRefundCount," +
        	    " SUM(tmp.STL_REFUND_AMT) totalRefundAmt,SUM(tmp.STL_FEE) totalFee,SUM(tmp.STL_NETRECAMT) totalNetreAmt "+
        		" FROM" +
	        		" (SELECT STL.* " +
	        		" FROM PAY_MERCHANT_SETTLEMENT STL" +
	        		" LEFT JOIN PAY_MERCHANT MER" +
	        		" ON STL.STL_MER_ID = MER.CUST_ID" +
	        		" LEFT JOIN  PAY_CUST_STL_INFO MERSTL " +
	        		" ON MER.CUST_ID = MERSTL.CUST_ID"+  
	        		" LEFT JOIN  PAY_FEE_RATE FEE " +
	        		" ON STL.STL_FEE_CODE = FEE.FEE_CODE "+(sqlCon.length()==0?"":" where "+ sqlCon)+
	        		" ORDER BY STL.STL_APPL_DATE DESC" +
	        		" ) tmp";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayMerchantSettlementParameter(ps,payMerchantSettlement,n);
            rs = ps.executeQuery();
            if (rs.next()) {
            	Long l1 = rs.getLong("totalApplyCount");
            	Long l2 = rs.getLong("totalApplyAmt");
            	Long l3 = rs.getLong("totalRefundCount");
            	Long l4 = rs.getLong("totalRefundAmt");
            	Long l5 = rs.getLong("totalFee");
            	Long l6 = rs.getLong("totalNetreAmt");
                return new Long[]{l1==null?0l:l1,l2==null?0l:l2,l3==null?0l:l3,l4==null?0l:l4,l5==null?0l:l5,l6==null?0l:l6};
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return new Long[]{0l,0l,0l};
	}
	public Long [] getAgentStl(PayMerchant mer,Date [] agentStlDay) throws Exception {
		String sql = "select sum(TXAMT) total_amt,sum(FEE) total_fee_amt,sum(NETRECAMT) total_stl_amt,sum(AGENT_FEE) agent_fee,count(PAYORDNO) txn_count from PAY_ORDER_HISTORY where ORDSTATUS='01' and PRDORDTYPE='0' " +
		 		" and CREATETIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		String tmpStr = "";
		for(int i = 0; i<mer.subMerList.size(); i++){
			PayMerchant subMer = (PayMerchant)mer.subMerList.get(i);
			tmpStr = tmpStr + "MERNO='"+subMer.custId+"' or ";
		}
		if(tmpStr.length() > 0)sql = sql+" and ("+tmpStr.substring(0,tmpStr.length()-4)+")";
		else  return new Long[]{0l,0l,0l,0l,0l};
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            if(agentStlDay!=null){
	            ps = con.prepareStatement(sql);
	            int n=1;
	            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(agentStlDay[0])+" 00:00:00");
	            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(agentStlDay[1])+" 23:59:59");
	            rs = ps.executeQuery();
	            if(rs.next())return new Long[]{rs.getLong("total_amt"),
	            		rs.getLong("total_fee_amt"),rs.getLong("total_stl_amt"),rs.getLong("agent_fee"),rs.getLong("txn_count")};
	            else return new Long[]{0l,0l,0l,0l,0l};
            } else return new Long[]{0l,0l,0l,0l,0l};
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
	public List<Long []> getAgentStl(PayMerchant mer,Date [] agentStlDay,Date [] agentPreStlDay) throws Exception {
		String sql = "select sum(TXAMT) total_amt,sum(FEE) total_fee_amt,sum(NETRECAMT) total_stl_amt,sum(AGENT_FEE) agent_fee,count(PAYORDNO) txn_count from PAY_ORDER_HISTORY where ORDSTATUS='01' and PRDORDTYPE='0' " +
		 		" and CREATETIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
		String tmpStr = "";
		List <Long []>list = new ArrayList<Long []>();
		for(int i = 0; i<mer.subMerList.size(); i++){
			PayMerchant subMer = (PayMerchant)mer.subMerList.get(i);
			tmpStr = tmpStr + "MERNO='"+subMer.custId+"' or ";
		}
		if(tmpStr.length() > 0)sql = sql+" and ("+tmpStr.substring(0,tmpStr.length()-4)+")";
		else {
			list.add(new Long[]{0l,0l,0l,0l,0l});
			list.add(new Long[]{0l,0l,0l,0l,0l});
			return list;
		}
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            if(agentStlDay!=null){
	            ps = con.prepareStatement(sql);
	            int n=1;
	            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(agentStlDay[0])+" 00:00:00");
	            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(agentStlDay[1])+" 23:59:59");
	            rs = ps.executeQuery();
	            if(rs.next())list.add(new Long[]{rs.getLong("total_amt"),
	            		rs.getLong("total_fee_amt"),rs.getLong("total_stl_amt"),rs.getLong("agent_fee"),rs.getLong("txn_count")});
	            else list.add(new Long[]{0l,0l,0l,0l,0l});
	            rs.close();
	            ps.close();
            } else list.add(new Long[]{0l,0l,0l,0l,0l});
            if(agentPreStlDay!=null){
	            ps = con.prepareStatement(sql);
	            int n=1;
	            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(agentPreStlDay[0])+" 00:00:00");
	            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(agentPreStlDay[1])+" 23:59:59");
	            rs = ps.executeQuery();
	            if(rs.next())list.add(new Long[]{rs.getLong("total_amt"),
	            		rs.getLong("total_fee_amt"),rs.getLong("total_stl_amt"),rs.getLong("agent_fee"),rs.getLong("txn_count")});
	            else list.add(new Long[]{0l,0l,0l,0l,0l});
            } else list.add(new Long[]{0l,0l,0l,0l,0l});
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return list;
	}
}