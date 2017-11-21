package com.pay.risk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_RISK_EXCEPT_TRAN_INFO DAO. 
 * @author Administrator
 *
 */
public class PayRiskExceptTranInfoDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRiskExceptTranInfoDAO.class);
    public static synchronized PayRiskExceptTranInfo getPayRiskExceptTranInfoValue(ResultSet rs)throws SQLException {
        PayRiskExceptTranInfo payRiskExceptTranInfo = new PayRiskExceptTranInfo();
        payRiskExceptTranInfo.id = rs.getString("ID");
        payRiskExceptTranInfo.tranId = rs.getString("TRAN_ID");
        payRiskExceptTranInfo.entityType = rs.getString("ENTITY_TYPE");
        payRiskExceptTranInfo.tranSource = rs.getString("TRAN_SOURCE");
        payRiskExceptTranInfo.custId = rs.getString("CUST_ID");
        payRiskExceptTranInfo.tranType = rs.getString("TRAN_TYPE");
        payRiskExceptTranInfo.bankCardNo = rs.getString("BANK_CARD_NO");
        payRiskExceptTranInfo.bankDepNo = rs.getString("BANK_DEP_NO");
        payRiskExceptTranInfo.bankSignVal = rs.getString("BANK_SIGN_VAL");
        payRiskExceptTranInfo.regdtTime = rs.getTimestamp("REGDT_TIME");
        payRiskExceptTranInfo.warnType = rs.getString("WARN_TYPE");
        payRiskExceptTranInfo.ruleCode = rs.getString("RULE_CODE");
        payRiskExceptTranInfo.exceptTranFlag = rs.getString("EXCEPT_TRAN_FLAG");
        payRiskExceptTranInfo.updateUser = rs.getString("UPDATE_USER");
        payRiskExceptTranInfo.updateTime = rs.getTimestamp("UPDATE_TIME");
        payRiskExceptTranInfo.remark = rs.getString("REMARK");
        
        // ============start================
        try{
	        payRiskExceptTranInfo.storeName = rs.getString("CUST_NAME");
	        /*payRiskExceptTranInfo.prdordno = rs.getString("PRDORDNO");
	        payRiskExceptTranInfo.ordamt = rs.getLong("ORDAMT");
	        payRiskExceptTranInfo.ordstatus = rs.getString("ORDSTATUS");*/
	        payRiskExceptTranInfo.ruleName = rs.getString("RULE_NAME");
	        payRiskExceptTranInfo.ruleLevelItem = rs.getString("RULE_LEVEL_ITEM");
        }catch(Exception e){}
        // ============end================
        
        return payRiskExceptTranInfo;
    }
    
    public List getList() throws Exception{
        String sql = "select * from PAY_RISK_EXCEPT_TRAN_INFO";
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
                list.add(getPayRiskExceptTranInfoValue(rs));
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
     * Set query condition sql.
     * @param payRiskExceptTranInfo
     * @return
     */
    private String setPayRiskExceptTranInfoSql(PayRiskExceptTranInfo payRiskExceptTranInfo) {
        StringBuffer sql = new StringBuffer();
        
        if(payRiskExceptTranInfo.entityType != null && payRiskExceptTranInfo.entityType.length() !=0) {
            sql.append(" tmp.ENTITY_TYPE = ? and ");
        }
        if(payRiskExceptTranInfo.regdtTime != null) {
            sql.append(" tmp.REGDT_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payRiskExceptTranInfo.regdtTimeSearchEnd != null) {
        	sql.append(" tmp.REGDT_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payRiskExceptTranInfo.ruleName != null && payRiskExceptTranInfo.ruleName.length() !=0) {
        	sql.append(" PAY_RISK_EXCEPT_RULE.RULE_NAME like ? and ");
        }
        if(payRiskExceptTranInfo.warnType != null && payRiskExceptTranInfo.warnType.length() !=0) {
            sql.append(" tmp.WARN_TYPE = ? and ");
        }
        if(payRiskExceptTranInfo.exceptTranFlag != null && payRiskExceptTranInfo.exceptTranFlag.length() !=0) {
            sql.append(" tmp.EXCEPT_TRAN_FLAG = ? and ");
        }
        if(payRiskExceptTranInfo.tranType != null && payRiskExceptTranInfo.tranType.length() !=0) {
            sql.append(" tmp.TRAN_TYPE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    
    /**
     * Set query parameter.
     * @param ps
     * @param payRiskExceptTranInfo
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRiskExceptTranInfoParameter(PreparedStatement ps,PayRiskExceptTranInfo payRiskExceptTranInfo,int n)throws SQLException {
        if(payRiskExceptTranInfo.entityType != null && payRiskExceptTranInfo.entityType.length() !=0) {
            ps.setString(n++,payRiskExceptTranInfo.entityType);
        }
        if(payRiskExceptTranInfo.regdtTime != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskExceptTranInfo.regdtTime) + " 00:00:00");
        }
        if(payRiskExceptTranInfo.regdtTimeSearchEnd != null) {
        	ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskExceptTranInfo.regdtTimeSearchEnd) + " 23:59:59");
        }
        if(payRiskExceptTranInfo.ruleName != null && payRiskExceptTranInfo.ruleName.length() !=0) {
        	ps.setString(n++,"%" + payRiskExceptTranInfo.ruleName + "%");
        }
        if(payRiskExceptTranInfo.warnType != null && payRiskExceptTranInfo.warnType.length() !=0) {
            ps.setString(n++,payRiskExceptTranInfo.warnType);
        }
        if(payRiskExceptTranInfo.exceptTranFlag != null && payRiskExceptTranInfo.exceptTranFlag.length() !=0) {
            ps.setString(n++,payRiskExceptTranInfo.exceptTranFlag);
        }
        if(payRiskExceptTranInfo.tranType != null && payRiskExceptTranInfo.tranType.length() !=0) {
            ps.setString(n++,payRiskExceptTranInfo.tranType);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payRiskExceptTranInfo
     * @return
     */
    public int getPayRiskExceptTranInfoCount(PayRiskExceptTranInfo payRiskExceptTranInfo) {
        String sqlCon = setPayRiskExceptTranInfoSql(payRiskExceptTranInfo);
        String custId = payRiskExceptTranInfo.custId == null ? "" :payRiskExceptTranInfo.custId;
        String sqlTmp = "(SELECT * FROM (select cust_id,STORE_NAME AS CUST_NAME,'1' ENTITY_TYPE FROM PAY_MERCHANT union select USER_ID CUST_ID,REAL_NAME STORE_NAME,'0' ENTITY_TYPE from PAY_TRAN_USER_INFO)) cust";
        if(com.PayConstant.CUST_TYPE_USER.equals(payRiskExceptTranInfo.entityType))sqlTmp="(SELECT USER_ID CUST_ID,REAL_NAME AS CUST_NAME,'0' ENTITY_TYPE FROM PAY_TRAN_USER_INFO) cust ";
        else if(com.PayConstant.CUST_TYPE_MERCHANT.equals(payRiskExceptTranInfo.entityType))sqlTmp="(SELECT cust_id,STORE_NAME,'1' ENTITY_TYPE FROM PAY_MERCHANT) cust ";
        String sql = "select count(rownum) recordCount from (select * from PAY_RISK_EXCEPT_TRAN_INFO"+(custId.length()==0?"":" where CUST_ID='"+custId+"'")+") tmp" +
					"   LEFT JOIN"+sqlTmp+" on tmp.cust_id=cust.cust_id  and tmp.ENTITY_TYPE=cust.ENTITY_TYPE "+
        			"  LEFT JOIN (SELECT ID,PRDORDNO,ORDAMT,ORDSTATUS FROM PAY_PRODUCT_ORDER) PAY_PRODUCT_ORDER ON tmp.TRAN_ID = PAY_PRODUCT_ORDER.ID" + 
        			"  LEFT JOIN (SELECT RULE_CODE,RULE_NAME,RULE_LEVEL_ITEM FROM PAY_RISK_EXCEPT_RULE) PAY_RISK_EXCEPT_RULE ON tmp.RULE_CODE = PAY_RISK_EXCEPT_RULE.RULE_CODE" + 
        		(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRiskExceptTranInfoParameter(ps,payRiskExceptTranInfo,n);
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
     * @param payRiskExceptTranInfo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRiskExceptTranInfoList(PayRiskExceptTranInfo payRiskExceptTranInfo,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayRiskExceptTranInfoSql(payRiskExceptTranInfo);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String custId = payRiskExceptTranInfo.custId == null ? "" :payRiskExceptTranInfo.custId;
        String sqlTmp = "(SELECT * FROM (select cust_id,STORE_NAME AS CUST_NAME,'1' ENTITY_TYPE FROM PAY_MERCHANT union select USER_ID CUST_ID,REAL_NAME STORE_NAME,'0' ENTITY_TYPE from PAY_TRAN_USER_INFO)) cust";
        if(com.PayConstant.CUST_TYPE_USER.equals(payRiskExceptTranInfo.entityType))sqlTmp="(SELECT USER_ID CUST_ID,REAL_NAME AS CUST_NAME,'0' ENTITY_TYPE FROM PAY_TRAN_USER_INFO) cust ";
        else if(com.PayConstant.CUST_TYPE_MERCHANT.equals(payRiskExceptTranInfo.entityType))sqlTmp="(SELECT cust_id,STORE_NAME AS CUST_NAME,'1' ENTITY_TYPE FROM PAY_MERCHANT) cust ";
        
        /*// 根据监控状态的不同，连接不同的表
        String sqlForOrder = "  LEFT JOIN (SELECT ID AS PRDORDNO,ORDAMT,ORDSTATUS FROM PAY_PRODUCT_ORDER) PAY_PRODUCT_ORDER ON tmp.TRAN_ID = PAY_PRODUCT_ORDER.PRDORDNO";
        String sqlForRefund = "  LEFT JOIN (SELECT REFORDNO AS PRDORDNO,RFAMT as ORDAMT,BANKSTS as ORDSTATUS FROM PAY_REFUND) PAY_PRODUCT_ORDER ON tmp.TRAN_ID = PAY_PRODUCT_ORDER.PRDORDNO";*/
		
        String sql = "select * from (" +
    			"  select rownum rowno,tmp1.* from (" +
    			"  select tmp.*," +
    			"	cust.CUST_NAME,PAY_RISK_EXCEPT_RULE.RULE_NAME,PAY_RISK_EXCEPT_RULE.RULE_LEVEL_ITEM  " +
//    			"	cust.CUST_NAME,PAY_PRODUCT_ORDER.PRDORDNO,PAY_PRODUCT_ORDER.ORDAMT,PAY_PRODUCT_ORDER.ORDSTATUS,PAY_RISK_EXCEPT_RULE.RULE_NAME,PAY_RISK_EXCEPT_RULE.RULE_LEVEL_ITEM  " +
    			"	from (select * from PAY_RISK_EXCEPT_TRAN_INFO"+(custId.length()==0?"":" where CUST_ID='"+custId+"'")+") tmp " + 
    			"   LEFT JOIN"+sqlTmp+" on tmp.cust_id=cust.cust_id  and tmp.ENTITY_TYPE=cust.ENTITY_TYPE "+
//    			sqlForOrder + 
//    			sqlForRefund + 
    			
    			/*"	case when tmp.TRAN_TYPE = 1 then " + sqlForOrder + 
    			"	when tmp.TRAN_TYPE = 4 then " + sqlForRefund + 
    			"	end " + */
    			
    			/*"	if (tmp.TRAN_TYPE = 1) then " + sqlForOrder + 
    			"	elseif (tmp.TRAN_TYPE = 4) then " + sqlForOrder + 
    			"	end if;  "	 + */
    			
    			/*("1".equals(payRiskExceptTranInfo.tranType) ? sqlForOrder : "") + 
    			("4".equals(payRiskExceptTranInfo.tranType) ? sqlForRefund : "") + */
    			
    			"  LEFT JOIN (SELECT RULE_CODE,RULE_NAME,RULE_LEVEL_ITEM FROM PAY_RISK_EXCEPT_RULE) PAY_RISK_EXCEPT_RULE ON tmp.RULE_CODE = PAY_RISK_EXCEPT_RULE.RULE_CODE" + 
    			(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
    			"  ) tmp1 "+
    			")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayRiskExceptTranInfoParameter(ps,payRiskExceptTranInfo,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskExceptTranInfoValue(rs));
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
     * detail PayRiskExceptTranInfo
     * @param id
     * @return PayRiskExceptTranInfo
     * @throws Exception
     */
    public PayRiskExceptTranInfo detailPayRiskExceptTranInfo(String id) throws Exception {
        String sql = "select * from PAY_RISK_EXCEPT_TRAN_INFO where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayRiskExceptTranInfoValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayRiskExceptTranInfo
     * @param payRiskExceptTranInfo
     * @throws Exception
     */
    public void updatePayRiskExceptTranInfo(PayRiskExceptTranInfo payRiskExceptTranInfo) throws Exception {
        String sqlTmp = "";
        if(payRiskExceptTranInfo.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payRiskExceptTranInfo.tranId != null)sqlTmp = sqlTmp + " TRAN_ID=?,";
        if(payRiskExceptTranInfo.entityType != null)sqlTmp = sqlTmp + " ENTITY_TYPE=?,";
        if(payRiskExceptTranInfo.tranSource != null)sqlTmp = sqlTmp + " TRAN_SOURCE=?,";
        if(payRiskExceptTranInfo.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(payRiskExceptTranInfo.tranType != null)sqlTmp = sqlTmp + " TRAN_TYPE=?,";
        if(payRiskExceptTranInfo.bankCardNo != null)sqlTmp = sqlTmp + " BANK_CARD_NO=?,";
        if(payRiskExceptTranInfo.bankDepNo != null)sqlTmp = sqlTmp + " BANK_DEP_NO=?,";
        if(payRiskExceptTranInfo.bankSignVal != null)sqlTmp = sqlTmp + " BANK_SIGN_VAL=?,";
        if(payRiskExceptTranInfo.regdtTime != null)sqlTmp = sqlTmp + " REGDT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskExceptTranInfo.warnType != null)sqlTmp = sqlTmp + " WARN_TYPE=?,";
        if(payRiskExceptTranInfo.ruleCode != null)sqlTmp = sqlTmp + " RULE_CODE=?,";
        if(payRiskExceptTranInfo.exceptTranFlag != null)sqlTmp = sqlTmp + " EXCEPT_TRAN_FLAG=?,";
        if(payRiskExceptTranInfo.updateUser != null)sqlTmp = sqlTmp + " UPDATE_USER=?,";
        if(payRiskExceptTranInfo.updateTime != null)sqlTmp = sqlTmp + " UPDATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskExceptTranInfo.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_RISK_EXCEPT_TRAN_INFO "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payRiskExceptTranInfo.id != null)ps.setString(n++,payRiskExceptTranInfo.id);
            if(payRiskExceptTranInfo.tranId != null)ps.setString(n++,payRiskExceptTranInfo.tranId);
            if(payRiskExceptTranInfo.entityType != null)ps.setString(n++,payRiskExceptTranInfo.entityType);
            if(payRiskExceptTranInfo.tranSource != null)ps.setString(n++,payRiskExceptTranInfo.tranSource);
            if(payRiskExceptTranInfo.custId != null)ps.setString(n++,payRiskExceptTranInfo.custId);
            if(payRiskExceptTranInfo.tranType != null)ps.setString(n++,payRiskExceptTranInfo.tranType);
            if(payRiskExceptTranInfo.bankCardNo != null)ps.setString(n++,payRiskExceptTranInfo.bankCardNo);
            if(payRiskExceptTranInfo.bankDepNo != null)ps.setString(n++,payRiskExceptTranInfo.bankDepNo);
            if(payRiskExceptTranInfo.bankSignVal != null)ps.setString(n++,payRiskExceptTranInfo.bankSignVal);
            if(payRiskExceptTranInfo.regdtTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptTranInfo.regdtTime));
            if(payRiskExceptTranInfo.warnType != null)ps.setString(n++,payRiskExceptTranInfo.warnType);
            if(payRiskExceptTranInfo.ruleCode != null)ps.setString(n++,payRiskExceptTranInfo.ruleCode);
            if(payRiskExceptTranInfo.exceptTranFlag != null)ps.setString(n++,payRiskExceptTranInfo.exceptTranFlag);
            if(payRiskExceptTranInfo.updateUser != null)ps.setString(n++,payRiskExceptTranInfo.updateUser);
            if(payRiskExceptTranInfo.updateTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptTranInfo.updateTime));
            if(payRiskExceptTranInfo.remark != null)ps.setString(n++,payRiskExceptTranInfo.remark);
            if(payRiskExceptTranInfo.storeName != null)ps.setString(n++,payRiskExceptTranInfo.storeName);
            if(payRiskExceptTranInfo.prdordno != null)ps.setString(n++,payRiskExceptTranInfo.prdordno);
            /*if(payRiskExceptTranInfo.ordamt != null)ps.setLong(n++,payRiskExceptTranInfo.ordamt);
            if(payRiskExceptTranInfo.ordstatus != null)ps.setString(n++,payRiskExceptTranInfo.ordstatus);*/
            if(payRiskExceptTranInfo.ruleName != null)ps.setString(n++,payRiskExceptTranInfo.ruleName);
            if(payRiskExceptTranInfo.ruleLevelItem != null)ps.setString(n++,payRiskExceptTranInfo.ruleLevelItem);
            ps.setString(n++,payRiskExceptTranInfo.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

    // ===========================================风控规则监控======================================================
    
	/*
	    单笔异常交易规则	单笔异常交易金额＞＝X	X	100	元
	    日累计金额规则	当日累计交易金额＞＝X	X	1000000	元
	    日累计笔数规则	当日累计交易笔数＞＝X	X	10	笔
	    周累计金额规则	上周累计交易金额＞＝X	X	1000100	元
	    周累计笔数规则	上周累计交易笔数>=X	X	10	笔
	    月累计金额规则	上月累计交易金额>=X	X	1000000	元
	    
	    月累计笔数规则	上月累计交易笔数>=X	X	10	次
	    周累计退款金额规则	上周累计退款金额>=X	X	1000000	元
	    周累计退款笔数规则	上周累计退款笔数>=X	X	10	次
	    月累计退款金额规则	上月累计退款金额>=X	X	1000000	元
	    月累计退款笔数规则	上月累计退款笔数>=X	X	10	次
	    同商户下日累计金额上限规则	用户在同一商户当日累计交易金额>=X	X	100000	元
	    同商户下日累计笔数上限规则	用户在同一商户当日累计交易笔数>=X	X	10	次
	    同商户下周累计金额上限规则	用户在同一商户上周累计交易金额>=X	X	1000	元
	    同商户下周累计笔数上限规则	用户在同一商户上周累计交易笔数>=X	X	10	次
	    同商户下月累计金额上限规则	用户在同一商户上月累计交易金额>=X	X	1000	元
	    同商户下月累计笔数上限规则	用户在同一商户上月累计交易笔数>=X	X	10	次
	    未认证用户当月交易金额上限规则	未认证用户当月交易（充值+消费+转账+提现）金额>=X	X	10000	元
	*/
    
    // 单笔异常交易规则	单笔异常交易金额＞＝X	X	100	元
 	public void runRule1(PayRiskExceptRule rule,Date runDay)throws Exception {
 		String sql = "insert into pay_risk_except_tran_info(" +
 					"	ID," +
 					"	TRAN_ID," +
 					"	ENTITY_TYPE," +
 					"	TRAN_SOURCE," +
 					"	CUST_ID," +
 					"	TRAN_TYPE," +
 					"	WARN_TYPE," +
 					"	RULE_CODE," +
 					"	EXCEPT_TRAN_FLAG," +
 					"	UPDATE_USER)" +
 						"  	(" +
 						"	SELECT " +
 						"	PAY_FEE_CODE.nextval,"	+
 						"	po.id as TRAN_TYPE, " +
 						"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
 						"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
 						" 	po.merno as CUST_ID," +
 						" 	'1' as TRAN_TYPE , " +
 						" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
 						" 	? as RULE_CODE,	" + // 违反规则编号
 						" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
 						" 	'admin' as UPDATE_USER " +
 						"  	FROM pay_product_order po" +
 						"  	where po.ordamt >= ? " +
 						"  	and po.ORDSTATUS ='01' and ORDERTIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and ORDERTIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
 				" 	)";
         log.info(sql);
         Connection con = null;
         PreparedStatement ps = null;
         try {
             con = connection();
             ps = con.prepareStatement(sql);
             int n = 1;
             // 拼装参数
             Double pramaValue = Double.parseDouble(rule.paramList.get(0).paramValue) * 100;
             ps.setString(n++, rule.ruleType);
             ps.setString(n++, rule.excpType);
             ps.setString(n++, rule.ruleCode);
             ps.setString(n++, pramaValue.toString());
             ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(runDay)+" 00:00:00");
             ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(runDay)+" 23:59:59");
             ps.executeUpdate();
         } catch (Exception e) {
             e.printStackTrace();
             throw e;
         } finally {
             close(null , ps, con);
         }
 	}
    
 	// 日累计金额规则	当日累计交易金额＞＝X	X	1000000	元
 	public void runRule2(PayRiskExceptRule rule,Date preDay)throws Exception {
 		String timeScopeStart = new SimpleDateFormat("yyyy-MM-dd").format(preDay) + " 00:00:00"; 
 		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preDay) + " 23:59:59"; 
 		String sql = "insert into pay_risk_except_tran_info(" +
 					"	ID," +
 					"	TRAN_ID," +
 					"	ENTITY_TYPE," +
 					"	TRAN_SOURCE," +
 					"	CUST_ID," +
 					"	TRAN_TYPE," +
 					"	WARN_TYPE," +
 					"	RULE_CODE," +
 					"	EXCEPT_TRAN_FLAG," +
 					"	UPDATE_USER)" +
 						"  	(" +
 						"	SELECT " +
 						"	PAY_FEE_CODE.nextval,"	+
 						"	po.id as TRAN_TYPE, " +
 						"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
 						"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
 						" 	po.merno as CUST_ID," +
 						" 	'1' as TRAN_TYPE , " +
 						" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
 						" 	? as RULE_CODE,	" + // 违反规则编号
 						" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
 						" 	'admin' as UPDATE_USER " +
 						"  	FROM" +
 						" 	(" +
 						"   	SELECT merno,SUM(ORDAMT) as count" +
 						" 	FROM pay_product_order" +
 						"   	where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
 						"   	GROUP BY merno" +
 						" 	) tmp,pay_product_order po" +
 						"  	where tmp.count >= ? " +
 						"  	and po.merno=tmp.merno" +
 						"  	and po.ORDSTATUS ='01' " +
 						"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
 				" 	)";
         log.info(sql);
         Connection con = null;
         PreparedStatement ps = null;
         try {
             con = connection();
             ps = con.prepareStatement(sql);
             Double pramaValue = Double.parseDouble(rule.paramList.get(0).paramValue) * 100;
             int n = 1;
             // 拼装参数
             ps.setString(n++, rule.ruleType);
             ps.setString(n++, rule.excpType);
             ps.setString(n++, rule.ruleCode);
             ps.setString(n++, timeScopeStart);
             ps.setString(n++, timeScopeEnd);
             ps.setString(n++, pramaValue.toString());
             ps.setString(n++, timeScopeStart);
             ps.setString(n++, timeScopeEnd);
             ps.executeUpdate();
         } catch (Exception e) {
             e.printStackTrace();
             throw e;
         } finally {
             close(null , ps, con);
         }
 	}
    // 日累计笔数规则	当日累计交易笔数＞＝X	X	10	笔
	public void runRule3(PayRiskExceptRule rule,Date preDay) throws Exception {
		String sql = "insert into pay_risk_except_tran_info(" +
					"	ID," +
					"	TRAN_ID," +
					"	ENTITY_TYPE," +
					"	TRAN_SOURCE," +
					"	CUST_ID," +
					"	TRAN_TYPE," +
					"	WARN_TYPE," +
					"	RULE_CODE," +
					"	EXCEPT_TRAN_FLAG," +
					"	UPDATE_USER)" +
						"  	(" +
						"	SELECT " +
						"	PAY_FEE_CODE.nextval,"	+
						"	po.id as TRAN_TYPE, " +
						"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
						"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
						" 	po.merno as CUST_ID," +
						" 	'1' as TRAN_TYPE , " +
						" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
						" 	? as RULE_CODE,	" + // 违反规则编号
						" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
						" 	'admin' as UPDATE_USER " +
						"  	FROM " +
						" 	(" +
						"   	SELECT merno,COUNT(1) as count " +
						" 	FROM pay_product_order" +
						"   	where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
						"   	GROUP BY merno" +
						" 	) tmp,pay_product_order po" +
						"  	where tmp.count >= ? " +
						"  	and po.merno=tmp.merno" +
						"  	and po.ORDSTATUS ='01' " +
						"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            // 拼装参数
            ps.setString(n++, rule.ruleType);
            ps.setString(n++, rule.excpType);
            ps.setString(n++, rule.ruleCode);
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(preDay) + " 00:00:00");
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(preDay) + " 23:59:59");
            ps.setString(n++, rule.paramList.get(0).paramValue);
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(preDay) + " 00:00:00");
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(preDay) + " 23:59:59");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null , ps, con);
        }
	}
	
	
	// 周累计金额规则	上周累计交易金额＞＝X	X	1000100	元
	public void runRule4(PayRiskExceptRule rule,Date preWeekFirstDay,Date preWeekLastDay) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		String timeScopeStart= new SimpleDateFormat("yyyy-MM-dd").format(preWeekFirstDay) + " 00:00:00";
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preWeekLastDay)  + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	po.id as TRAN_TYPE, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	po.merno as CUST_ID," +
				" 	'1' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				"  	FROM" +
				" 	(" +
				"   	SELECT merno,SUM(ORDAMT) as count" +
				" 	FROM pay_product_order" +
				"   	where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				"   	GROUP BY merno" +
				" 	) tmp,pay_product_order po" +
				"  	where tmp.count >= ? " +
				"  	and po.merno=tmp.merno" +
				"  	and po.ORDSTATUS ='01' " +
				"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			Double pramaValue = Double.parseDouble(rule.paramList.get(0).paramValue) * 100;
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, pramaValue.toString());
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	// 周累计笔数规则	上周累计交易笔数>=X	X	10	笔
	public void runRule5(PayRiskExceptRule rule,Date preWeekFirstDay,Date preWeekLastDay) throws Exception {
		String timeScopeStart= new SimpleDateFormat("yyyy-MM-dd").format(preWeekFirstDay) + " 00:00:00";
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preWeekLastDay)  + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	po.id as TRAN_TYPE, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	po.merno as CUST_ID," +
				" 	'1' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				"  	FROM" +
				" 	(" +
				"   	SELECT merno,COUNT(1) as count" +
				" 	FROM pay_product_order" +
				"   	where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				"   	GROUP BY merno" +
				" 	) tmp,pay_product_order po" +
				"  	where tmp.count >= ? " +
				"  	and po.merno=tmp.merno" +
				"  	and po.ORDSTATUS ='01' " +
				"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, rule.paramList.get(0).paramValue);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	// 月累计金额规则	上月累计交易金额>=X	X	1000000	元
	public void runRule6(PayRiskExceptRule rule,Date preMonFirstDay,Date preMonLastDay) throws Exception {
		String timeScopeStart = new SimpleDateFormat("yyyy-MM-dd").format(preMonFirstDay) + " 00:00:00"; 
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preMonLastDay) + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
					"	ID," +
					"	TRAN_ID," +
					"	ENTITY_TYPE," +
					"	TRAN_SOURCE," +
					"	CUST_ID," +
					"	TRAN_TYPE," +
					"	WARN_TYPE," +
					"	RULE_CODE," +
					"	EXCEPT_TRAN_FLAG," +
					"	UPDATE_USER)" +
						"  	(" +
						"	SELECT " +
						"	PAY_FEE_CODE.nextval,"	+
						"	po.id as TRAN_TYPE, " +
						"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
						"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
						" 	po.merno as CUST_ID," +
						" 	'1' as TRAN_TYPE , " +
						" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
						" 	? as RULE_CODE,	" + // 违反规则编号
						" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
						" 	'admin' as UPDATE_USER " +
						"  	FROM" +
						" 	(" +
						"   	SELECT merno,SUM(ORDAMT) as count" +
						" 	FROM pay_product_order" +
						"   	where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
						"   	GROUP BY merno" +
						" 	) tmp,pay_product_order po" +
						"  	where tmp.count >= ? " +
						"  	and po.merno=tmp.merno" +
						"  	and po.ORDSTATUS ='01' " +
						"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            Double pramaValue = Double.parseDouble(rule.paramList.get(0).paramValue) * 100;
            int n = 1;
            // 拼装参数
            ps.setString(n++, rule.ruleType);
            ps.setString(n++, rule.excpType);
            ps.setString(n++, rule.ruleCode);
            ps.setString(n++, timeScopeStart);
            ps.setString(n++, timeScopeEnd);
            ps.setString(n++, pramaValue.toString());
            ps.setString(n++, timeScopeStart);
            ps.setString(n++, timeScopeEnd);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null , ps, con);
        }
	}
	
	// 月累计笔数规则	上月累计交易笔数>=X	X	10	次
	public void runRule7(PayRiskExceptRule rule,Date preMonLastDay,Date preWeekFirstDay) throws Exception {
		String timeScopeStart = new SimpleDateFormat("yyyy-MM-dd").format(preMonLastDay) + " 00:00:00"; 
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preWeekFirstDay) + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	po.id as TRAN_TYPE, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	po.merno as CUST_ID," +
				" 	'1' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				"  	FROM" +
				" 	(" +
				"   	SELECT merno,COUNT(1) as count" +
				" 	FROM pay_product_order" +
				"   	where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				"   	GROUP BY merno" +
				" 	) tmp,pay_product_order po" +
				"  	where tmp.count >= ? " +
				"  	and po.merno=tmp.merno" +
				"  	and po.ORDSTATUS ='01' " +
				"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, rule.paramList.get(0).paramValue);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	// 周累计退款金额规则	上周累计退款金额>=X	X	1000000	元
	public void runRule8(PayRiskExceptRule rule,Date preWeekFirstDay,Date preWeekLastDay)throws Exception {
		String timeScopeStart= new SimpleDateFormat("yyyy-MM-dd").format(preWeekFirstDay) + " 00:00:00";
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preWeekLastDay)  + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
					"  	(" +
					"	SELECT " +
					"	PAY_FEE_CODE.nextval,"	+
					"	pr.REFORDNO as TRAN_ID, " +
					"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
					"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
					" 	pr.MERNO as CUST_ID," +
					" 	'4' as TRAN_TYPE , " +
					" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
					" 	? as RULE_CODE,	" + // 违反规则编号
					" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
					" 	'admin' as UPDATE_USER " +
					" FROM" + 
					"  (SELECT merno," + 
					"	SUM(RFAMT) AS COUNT" + 
					"  FROM PAY_REFUND" + 
					"  WHERE RFORDTIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') " + 
					"  AND RFORDTIME   <= to_date(?,'yyyy-mm-dd hh24:mi:ss') " + 
					"  GROUP BY merno" + 
					"  ) tmp,PAY_REFUND pr " + 
					" WHERE tmp.count >= ? " + 
					" AND pr.merno=tmp.merno " + 
					" AND RFORDTIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') " + 
					" AND RFORDTIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') " + 
			" 	)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            Double pramaValue = Double.parseDouble(rule.paramList.get(0).paramValue) * 100;
            int n = 1;
            // 拼装参数
            ps.setString(n++, rule.ruleType);
            ps.setString(n++, rule.excpType);
            ps.setString(n++, rule.ruleCode);
            ps.setString(n++, timeScopeStart);
            ps.setString(n++, timeScopeEnd);
            ps.setString(n++, pramaValue.toString());
            ps.setString(n++, timeScopeStart);
            ps.setString(n++, timeScopeEnd);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null , ps, con);
        }
	}
	
	// 周累计退款笔数规则	上周累计退款笔数>=X	X	10	次
	public void runRule9(PayRiskExceptRule rule,Date preWeekFirstDay,Date preWeekLastDay)throws Exception {
		String timeScopeStart= new SimpleDateFormat("yyyy-MM-dd").format(preWeekFirstDay) + " 00:00:00";
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preWeekLastDay)  + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	pr.REFORDNO as TRAN_ID, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	pr.MERNO as CUST_ID," +
				" 	'4' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				" FROM" + 
				"  (SELECT merno," + 
				"	COUNT(1) AS COUNT" + 
				"  FROM PAY_REFUND" + 
				"  WHERE RFORDTIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"  AND RFORDTIME   <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"  GROUP BY merno" + 
				"  ) tmp,PAY_REFUND pr" + 
				" WHERE tmp.count >= ?" + 
				" AND pr.merno=tmp.merno" + 
				
				" AND RFORDTIME   >= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				" AND RFORDTIME   <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, rule.paramList.get(0).paramValue);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	// 月累计退款金额规则	上月累计退款金额>=X	X	1000000	元
	public void runRule10(PayRiskExceptRule rule,Date preMonFirstDay,Date preMonLastDay)throws Exception {
		String timeScopeStart = new SimpleDateFormat("yyyy-MM-dd").format(preMonFirstDay) + " 00:00:00"; 
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preMonLastDay) + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	pr.REFORDNO as TRAN_ID, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	pr.MERNO as CUST_ID," +
				" 	'4' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				" FROM" + 
				"  (SELECT merno," + 
				"	SUM(RFAMT) AS COUNT" + 
				"  FROM PAY_REFUND" + 
				"  WHERE RFORDTIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"  AND RFORDTIME   <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"  GROUP BY merno" + 
				"  ) tmp,PAY_REFUND pr" + 
				" WHERE tmp.count >= ?" + 
				" AND pr.merno=tmp.merno" + 
				" AND RFORDTIME   >= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				" AND RFORDTIME   <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			Double pramaValue = Double.parseDouble(rule.paramList.get(0).paramValue) * 100;
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, pramaValue.toString());
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	// 月累计退款笔数规则	上月累计退款笔数>=X	X	10	次
	public void runRule11(PayRiskExceptRule rule,Date preMonFirstDay,Date preMonLastDay)throws Exception {
		String timeScopeStart = new SimpleDateFormat("yyyy-MM-dd").format(preMonFirstDay) + " 00:00:00"; 
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preMonLastDay) + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	pr.REFORDNO as TRAN_ID, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	pr.MERNO as CUST_ID," +
				" 	'4' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				" FROM" + 
				"  (SELECT merno," + 
				"	COUNT(1) AS COUNT" + 
				"  FROM PAY_REFUND" + 
				"  WHERE RFORDTIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"  AND RFORDTIME   <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"  GROUP BY merno" + 
				"  ) tmp,PAY_REFUND pr" + 
				" WHERE tmp.count >= ?" + 
				" AND pr.merno =tmp.merno" + 
				" AND RFORDTIME   >= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				" AND RFORDTIME   <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, rule.paramList.get(0).paramValue);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	
	// 同商户下日累计金额上限规则	用户在同一商户当日累计交易金额>=X	X	100000	元
	public void runRule12(PayRiskExceptRule rule,Date preDay) throws Exception {
		String timeScopeStart = new SimpleDateFormat("yyyy-MM-dd").format(preDay) + " 00:00:00"; 
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preDay) + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
					"	ID," +
					"	TRAN_ID," +
					"	ENTITY_TYPE," +
					"	TRAN_SOURCE," +
					"	CUST_ID," +
					"	TRAN_TYPE," +
					"	WARN_TYPE," +
					"	RULE_CODE," +
					"	EXCEPT_TRAN_FLAG," +
					"	UPDATE_USER)" +
						"  	(" +
						"	SELECT " +
						"	PAY_FEE_CODE.nextval,"	+
						"	po.id as TRAN_TYPE, " +
						"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
						"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
						" 	po.merno as CUST_ID," +
						" 	'1' as TRAN_TYPE , " +
						" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
						" 	? as RULE_CODE,	" + // 违反规则编号
						" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
						" 	'admin' as UPDATE_USER " +
						"  	FROM" +
						" 	( SELECT " +
							 	 " merno , " +
								 " CUSTPAYACNO ," +
								 " SUM(ORDAMT) as count" +
								" FROM" +
								 "(" +
								    "SELECT * FROM pay_product_order WHERE CUSTPAYACNO is NOT NULL" +
								  ")" +
								" where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
								" GROUP BY merno,CUSTPAYACNO" +
						" 	) tmp,pay_product_order po" +
						"  	where tmp.count >= ? " +
						"  	and po.merno=tmp.merno" +
						"  	and po.CUSTPAYACNO=tmp.CUSTPAYACNO" +
						"  	and po.ORDSTATUS ='01' " +
						"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            Double pramaValue = Double.parseDouble(rule.paramList.get(0).paramValue) * 100;
            int n = 1;
            // 拼装参数
            ps.setString(n++, rule.ruleType);
            ps.setString(n++, rule.excpType);
            ps.setString(n++, rule.ruleCode);
            ps.setString(n++, timeScopeStart);
            ps.setString(n++, timeScopeEnd);
            ps.setString(n++, pramaValue.toString());
            ps.setString(n++, timeScopeStart);
            ps.setString(n++, timeScopeEnd);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null , ps, con);
        }
	}
	
	// 同商户下日累计笔数上限规则	用户在同一商户当日累计交易笔数>=X	X	10	次
	public void runRule13(PayRiskExceptRule rule,Date preDay) throws Exception {
		String timeScopeStart = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()) + " 00:00:00"; 
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()) + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	po.id as TRAN_TYPE, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	po.merno as CUST_ID," +
				" 	'1' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				"  	FROM" +
				" 	( SELECT " +
				" merno , " +
				" CUSTPAYACNO ," +
				" COUNT(1) as count" +
				" FROM" +
				"(" +
				"SELECT * FROM pay_product_order WHERE CUSTPAYACNO is NOT NULL" +
				")" +
				"where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"GROUP BY merno,CUSTPAYACNO" +
				" 	) tmp,pay_product_order po" +
				"  	where tmp.count >= ? " +
				"  	and po.merno=tmp.merno" +
				"  	and po.CUSTPAYACNO=tmp.CUSTPAYACNO" +
				"  	and po.ORDSTATUS ='01' " +
				"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, rule.paramList.get(0).paramValue);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	// 同商户下周累计金额上限规则	用户在同一商户上周累计交易金额>=X	X	1000	元
	public void runRule14(PayRiskExceptRule rule,Date preWeekFirstDay,Date preWeekLastDay) throws Exception {
		String timeScopeStart= new SimpleDateFormat("yyyy-MM-dd").format(preWeekFirstDay) + " 00:00:00";
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preWeekLastDay)  + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	po.id as TRAN_TYPE, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	po.merno as CUST_ID," +
				" 	'1' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				"  	FROM" +
				"	( SELECT " +
				" 		merno , " +
				" 		CUSTPAYACNO ," +
				" 		SUM(ORDAMT) as count" +
				"	  FROM" +
				"		(" +
				"			SELECT * FROM pay_product_order WHERE CUSTPAYACNO is NOT NULL" +
				"		)" +
				"		where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"		GROUP BY merno,CUSTPAYACNO" +
				" 	) tmp,pay_product_order po" +
				"  	where tmp.count >= ? " +
				"  	and po.merno=tmp.merno" +
				"  	and po.CUSTPAYACNO=tmp.CUSTPAYACNO" +
				"  	and po.ORDSTATUS ='01' " +
				"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			Double pramaValue = Double.parseDouble(rule.paramList.get(0).paramValue) * 100;
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, pramaValue.toString());
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	// 同商户下周累计笔数上限规则	用户在同一商户上周累计交易笔数>=X	X	10	次
	public void runRule15(PayRiskExceptRule rule,Date preWeekFirstDay,Date preWeekLastDay) throws Exception {
		String timeScopeStart= new SimpleDateFormat("yyyy-MM-dd").format(preWeekFirstDay) + " 00:00:00";
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preWeekLastDay)  + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	po.id as TRAN_TYPE, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	po.merno as CUST_ID," +
				" 	'1' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				"  	FROM" +
				"	( SELECT " +
				" 		merno , " +
				" 		CUSTPAYACNO ," +
				" 		COUNT(1) as count" +
				"	  FROM" +
				"		(" +
				"			SELECT * FROM pay_product_order WHERE CUSTPAYACNO is NOT NULL" +
				"		)" +
				"		where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"		GROUP BY merno,CUSTPAYACNO" +
				" 	) tmp,pay_product_order po" +
				"  	where tmp.count >= ? " +
				"  	and po.merno=tmp.merno" +
				"  	and po.CUSTPAYACNO=tmp.CUSTPAYACNO" +
				"  	and po.ORDSTATUS ='01' " +
				"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, rule.paramList.get(0).paramValue);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	// 同商户下月累计金额上限规则	用户在同一商户上月累计交易金额>=X	X	1000	元
	public void runRule16(PayRiskExceptRule rule,Date preMonFirstDay,Date preMonLastDay) throws Exception {
		String timeScopeStart = new SimpleDateFormat("yyyy-MM-dd").format(preMonFirstDay) + " 00:00:00"; 
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preMonLastDay) + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	po.id as TRAN_TYPE, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	po.merno as CUST_ID," +
				" 	'1' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				"  	FROM" +
				"	( SELECT " +
				" 		merno , " +
				" 		CUSTPAYACNO ," +
				" 		SUM(ORDAMT) as count" +
				"	  FROM" +
				"		(" +
				"			SELECT * FROM pay_product_order WHERE CUSTPAYACNO is NOT NULL" +
				"		)" +
				"		where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"		GROUP BY merno,CUSTPAYACNO" +
				" 	) tmp,pay_product_order po" +
				"  	where tmp.count >= ? " +
				"  	and po.merno=tmp.merno" +
				"  	and po.CUSTPAYACNO=tmp.CUSTPAYACNO" +
				"  	and po.ORDSTATUS ='01' " +
				"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			Double pramaValue = Double.parseDouble(rule.paramList.get(0).paramValue) * 100;
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, pramaValue.toString());
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
	
	// 同商户下月累计笔数上限规则	用户在同一商户上月累计交易笔数>=X	X	10	次
	public void runRule17(PayRiskExceptRule rule,Date preMonFirstDay,Date preMonLastDay) throws Exception {
		String timeScopeStart = new SimpleDateFormat("yyyy-MM-dd").format(preMonFirstDay) + " 00:00:00"; 
		String timeScopeEnd = new SimpleDateFormat("yyyy-MM-dd").format(preMonLastDay) + " 23:59:59"; 
		String sql = "insert into pay_risk_except_tran_info(" +
				"	ID," +
				"	TRAN_ID," +
				"	ENTITY_TYPE," +
				"	TRAN_SOURCE," +
				"	CUST_ID," +
				"	TRAN_TYPE," +
				"	WARN_TYPE," +
				"	RULE_CODE," +
				"	EXCEPT_TRAN_FLAG," +
				"	UPDATE_USER)" +
				"  	(" +
				"	SELECT " +
				"	PAY_FEE_CODE.nextval,"	+
				"	po.id as TRAN_TYPE, " +
				"	? as ENTITY_TYPE,	" +  // 主体类型 0用户，1商户
				"	'0' as TRAN_SOURCE,	" +  // 交易来源 0线上，1线下- 0
				" 	po.merno as CUST_ID," +
				" 	'1' as TRAN_TYPE , " +
				" 	? as WARN_TYPE,	" + // 异常交易类型 1异常交易，2可疑交易
				" 	? as RULE_CODE,	" + // 违反规则编号
				" 	'0' as EXCEPT_TRAN_FLAG, " + // 交易状态 0未处理，1已释放，2已确认
				" 	'admin' as UPDATE_USER " +
				"  	FROM" +
				"	( SELECT " +
				" 		merno , " +
				" 		CUSTPAYACNO ," +
				" 		COUNT(1) as count" +
				"	  FROM" +
				"		(" +
				"			SELECT * FROM pay_product_order WHERE CUSTPAYACNO is NOT NULL" +
				"		)" +
				"		where ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" + 
				"		GROUP BY merno,CUSTPAYACNO" +
				" 	) tmp,pay_product_order po" +
				"  	where tmp.count >= ? " +
				"  	and po.merno=tmp.merno" +
				"  	and po.CUSTPAYACNO=tmp.CUSTPAYACNO" +
				"  	and po.ORDSTATUS ='01' " +
				"  	and ordertime >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ordertime <= to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
				" 	)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			// 拼装参数
			ps.setString(n++, rule.ruleType);
			ps.setString(n++, rule.excpType);
			ps.setString(n++, rule.ruleCode);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.setString(n++, rule.paramList.get(0).paramValue);
			ps.setString(n++, timeScopeStart);
			ps.setString(n++, timeScopeEnd);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null , ps, con);
		}
	}
}