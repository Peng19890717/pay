package com.pay.risk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import util.Tools;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.jweb.dao.BaseDAO;
/**
 * Table PAY_RISK_MERCHANT_LIMIT DAO. 
 * @author Administrator
 *
 */
public class PayRiskMerchantLimitDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRiskMerchantLimitDAO.class);
    public static synchronized PayRiskMerchantLimit getPayRiskMerchantLimitValue(ResultSet rs)throws SQLException {
        PayRiskMerchantLimit payRiskMerchantLimit = new PayRiskMerchantLimit();
        payRiskMerchantLimit.id = rs.getString("ID");
        payRiskMerchantLimit.limitType = rs.getString("LIMIT_TYPE");
        payRiskMerchantLimit.limitCompCode = rs.getString("LIMIT_COMP_CODE");
        payRiskMerchantLimit.limitCompType = rs.getString("LIMIT_COMP_TYPE");
        payRiskMerchantLimit.limitBusCode = rs.getString("LIMIT_BUS_CODE");
        payRiskMerchantLimit.limitBusClient = rs.getString("LIMIT_BUS_CLIENT");
        payRiskMerchantLimit.limitMinAmt = rs.getLong("LIMIT_MIN_AMT");
        payRiskMerchantLimit.limitMaxAmt = rs.getLong("LIMIT_MAX_AMT");
        payRiskMerchantLimit.limitDayTimes = rs.getLong("LIMIT_DAY_TIMES");
        payRiskMerchantLimit.limitDaySuccessTimes = rs.getLong("LIMIT_DAY_SUCCESS_TIMES");
        payRiskMerchantLimit.limitDayAmt = rs.getLong("LIMIT_DAY_AMT");
        payRiskMerchantLimit.limitDaySuccessAmt = rs.getLong("LIMIT_DAY_SUCCESS_AMT");
        payRiskMerchantLimit.limitMonthTimes = rs.getLong("LIMIT_MONTH_TIMES");
        payRiskMerchantLimit.limitMonthSuccessTimes = rs.getLong("LIMIT_MONTH_SUCCESS_TIMES");
        payRiskMerchantLimit.limitMonthAmt = rs.getLong("LIMIT_MONTH_AMT");
        payRiskMerchantLimit.limitMonthSuccessAmt = rs.getLong("LIMIT_MONTH_SUCCESS_AMT");
        payRiskMerchantLimit.limitYearTimes = rs.getLong("LIMIT_YEAR_TIMES");
        payRiskMerchantLimit.limitYearSuccessTimes = rs.getLong("LIMIT_YEAR_SUCCESS_TIMES");
        payRiskMerchantLimit.limitYearAmt = rs.getLong("LIMIT_YEAR_AMT");
        payRiskMerchantLimit.limitYearSuccessAmt = rs.getLong("LIMIT_YEAR_SUCCESS_AMT");
        payRiskMerchantLimit.limitStartDate = rs.getTimestamp("LIMIT_START_DATE");
        payRiskMerchantLimit.limitEndDate = rs.getTimestamp("LIMIT_END_DATE");
        payRiskMerchantLimit.limitAddinfo = rs.getString("LIMIT_ADDINFO");
        payRiskMerchantLimit.isUse = rs.getString("IS_USE");
        payRiskMerchantLimit.compCode = rs.getString("COMP_CODE");
        payRiskMerchantLimit.compName = rs.getString("COMP_NAME");
        payRiskMerchantLimit.createUser = rs.getString("CREATE_USER");
        payRiskMerchantLimit.createTime = rs.getTimestamp("CREATE_TIME");
        payRiskMerchantLimit.updateUser = rs.getString("UPDATE_USER");
        payRiskMerchantLimit.updateTime = rs.getTimestamp("UPDATE_TIME");
        payRiskMerchantLimit.limitTimeFlag = rs.getString("LIMIT_TIME_FLAG");
        payRiskMerchantLimit.limitRiskLevel = rs.getString("LIMIT_RISK_LEVEL");
        try {payRiskMerchantLimit.storeName = rs.getString("STORE_NAME");} catch (Exception e) {} 
        return payRiskMerchantLimit;
    }
    public String addPayRiskMerchantLimit(PayRiskMerchantLimit payRiskMerchantLimit) throws Exception {
    	String sql = "insert into PAY_RISK_MERCHANT_LIMIT("+
                "ID," + 
                "LIMIT_TYPE," + 
                "LIMIT_COMP_CODE," + 
                "LIMIT_COMP_TYPE," + 
                "LIMIT_BUS_CODE," + 
                "LIMIT_BUS_CLIENT," + 
                "LIMIT_MIN_AMT," + 
                "LIMIT_MAX_AMT," + 
                "LIMIT_DAY_TIMES," +
                "LIMIT_DAY_SUCCESS_TIMES," + 
                "LIMIT_DAY_AMT," +
                "LIMIT_DAY_SUCCESS_AMT," + 
                "LIMIT_MONTH_TIMES," + 
                "LIMIT_MONTH_SUCCESS_TIMES," + 
                "LIMIT_MONTH_AMT," + 
                "LIMIT_MONTH_SUCCESS_AMT," + 
                "LIMIT_YEAR_TIMES," + 
                "LIMIT_YEAR_SUCCESS_TIMES," + 
                "LIMIT_YEAR_AMT," + 
                "LIMIT_YEAR_SUCCESS_AMT," + 
                "LIMIT_START_DATE," + 
                "LIMIT_END_DATE," + 
                "LIMIT_ADDINFO," + 
                "IS_USE," + 
                "COMP_CODE," + 
                "COMP_NAME," + 
                "CREATE_USER," + 
                "CREATE_TIME," + 
                "UPDATE_USER," + 
                "UPDATE_TIME," + 
                "LIMIT_TIME_FLAG," + 
                "LIMIT_RISK_LEVEL)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRiskMerchantLimit.id);
            ps.setString(n++,payRiskMerchantLimit.limitType);
            ps.setString(n++,payRiskMerchantLimit.limitCompCode);
            ps.setString(n++,payRiskMerchantLimit.limitCompType);
            ps.setString(n++,payRiskMerchantLimit.limitBusCode);
            ps.setString(n++,payRiskMerchantLimit.limitBusClient);
            ps.setLong(n++,payRiskMerchantLimit.limitMinAmt);
            ps.setLong(n++,payRiskMerchantLimit.limitMaxAmt);
            ps.setLong(n++,payRiskMerchantLimit.limitDayTimes);
            ps.setLong(n++,payRiskMerchantLimit.limitDaySuccessTimes);
            ps.setLong(n++,payRiskMerchantLimit.limitDayAmt);
            ps.setLong(n++,payRiskMerchantLimit.limitDaySuccessAmt);
            ps.setLong(n++,payRiskMerchantLimit.limitMonthTimes);
            ps.setLong(n++,payRiskMerchantLimit.limitMonthSuccessTimes);
            ps.setLong(n++,payRiskMerchantLimit.limitMonthAmt);
            ps.setLong(n++,payRiskMerchantLimit.limitMonthSuccessAmt);
            ps.setLong(n++,payRiskMerchantLimit.limitYearTimes);
            ps.setLong(n++,payRiskMerchantLimit.limitYearSuccessTimes);
            ps.setLong(n++,payRiskMerchantLimit.limitYearAmt);
            ps.setLong(n++,payRiskMerchantLimit.limitYearSuccessAmt);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskMerchantLimit.limitStartDate == null ? new Date():payRiskMerchantLimit.limitStartDate));
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskMerchantLimit.limitEndDate == null ? new Date():payRiskMerchantLimit.limitEndDate));
            ps.setString(n++,payRiskMerchantLimit.limitAddinfo);
            ps.setString(n++,payRiskMerchantLimit.isUse);
            ps.setString(n++,payRiskMerchantLimit.compCode);
            ps.setString(n++,payRiskMerchantLimit.compName);
            ps.setString(n++,payRiskMerchantLimit.createUser);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskMerchantLimit.createTime));
            ps.setString(n++,payRiskMerchantLimit.updateUser);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskMerchantLimit.updateTime));
            ps.setString(n++,payRiskMerchantLimit.limitTimeFlag);
            ps.setString(n++,payRiskMerchantLimit.limitRiskLevel);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_RISK_MERCHANT_LIMIT where IS_USE='1'";
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
                list.add(getPayRiskMerchantLimitValue(rs));
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
     * @param payRiskMerchantLimit
     * @return
     */
    private String setPayRiskMerchantLimitSql(PayRiskMerchantLimit payRiskMerchantLimit) {
        StringBuffer sql = new StringBuffer();
        
        if(payRiskMerchantLimit.limitType != null && payRiskMerchantLimit.limitType.length() !=0) {
            sql.append(" lIMIT_TYPE = ? and ");
        }
        if(payRiskMerchantLimit.isUse != null && payRiskMerchantLimit.isUse.length() !=0) {
            sql.append(" IS_USE = ? and ");
        }
        if(payRiskMerchantLimit.createTime != null) {
            sql.append(" CREATE_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payRiskMerchantLimit.createTimeEnd != null) {
            sql.append(" CREATE_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payRiskMerchantLimit.limitCompCode != null && payRiskMerchantLimit.limitCompCode.length() !=0) {
            sql.append(" LIMIT_COMP_CODE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payRiskMerchantLimit
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRiskMerchantLimitParameter(PreparedStatement ps,PayRiskMerchantLimit payRiskMerchantLimit,int n)throws SQLException {
    	if(payRiskMerchantLimit.limitType != null && payRiskMerchantLimit.limitType.length() !=0) {
            ps.setString(n++,payRiskMerchantLimit.limitType);
        }
    	if(payRiskMerchantLimit.isUse != null && payRiskMerchantLimit.isUse.length() !=0) {
            ps.setString(n++,payRiskMerchantLimit.isUse);
        }
        if(payRiskMerchantLimit.createTime != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskMerchantLimit.createTime)+" 00:00:00");
        }
        if(payRiskMerchantLimit.createTimeEnd != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskMerchantLimit.createTimeEnd)+" 23:59:59");
        }
        if(payRiskMerchantLimit.limitCompCode != null && payRiskMerchantLimit.limitCompCode.length() !=0) {
            ps.setString(n++,payRiskMerchantLimit.limitCompCode);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payRiskMerchantLimit
     * @return
     */
    public int getPayRiskMerchantLimitCount(PayRiskMerchantLimit payRiskMerchantLimit) {
        String sqlCon = setPayRiskMerchantLimitSql(payRiskMerchantLimit);
        String sql = "select count(rownum) recordCount from PAY_RISK_MERCHANT_LIMIT " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRiskMerchantLimitParameter(ps,payRiskMerchantLimit,n);
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
     * @param payRiskMerchantLimit
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRiskMerchantLimitList(PayRiskMerchantLimit payRiskMerchantLimit,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        payRiskMerchantLimit.limitCompCode = "所有商户".equals(payRiskMerchantLimit.limitCompCode) ? "-1" : payRiskMerchantLimit.limitCompCode;
        String sqlCon = setPayRiskMerchantLimitSql(payRiskMerchantLimit);
        String sortOrder = sort == null || sort.length()==0?" order by CREATE_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*,mer.STORE_NAME from PAY_RISK_MERCHANT_LIMIT tmp LEFT JOIN PAY_MERCHANT mer ON TMP.LIMIT_COMP_CODE=mer.CUST_ID" +(sqlCon.length()==0?"":" where "+ sqlCon) +
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
            setPayRiskMerchantLimitParameter(ps,payRiskMerchantLimit,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskMerchantLimitValue(rs));
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
     * remove PayRiskMerchantLimit
     * @param id
     * @throws Exception     
     */
    public void removePayRiskMerchantLimit(String id) throws Exception {
        String sql = "delete from PAY_RISK_MERCHANT_LIMIT where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayRiskMerchantLimit
     * @param id
     * @return PayRiskMerchantLimit
     * @throws Exception
     */
    public PayRiskMerchantLimit detailPayRiskMerchantLimit(String id) throws Exception {
        String sql = "select * from PAY_RISK_MERCHANT_LIMIT where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayRiskMerchantLimitValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayRiskMerchantLimit
     * @param payRiskMerchantLimit
     * @throws Exception
     */
    public void updatePayRiskMerchantLimit(PayRiskMerchantLimit payRiskMerchantLimit) throws Exception {
        String sqlTmp = "";
        if(payRiskMerchantLimit.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payRiskMerchantLimit.limitType != null)sqlTmp = sqlTmp + " LIMIT_TYPE=?,";
        if(payRiskMerchantLimit.limitCompCode != null)sqlTmp = sqlTmp + " LIMIT_COMP_CODE=?,";
        if(payRiskMerchantLimit.limitCompType != null)sqlTmp = sqlTmp + " LIMIT_COMP_TYPE=?,";
        if(payRiskMerchantLimit.limitBusCode != null)sqlTmp = sqlTmp + " LIMIT_BUS_CODE=?,";
        if(payRiskMerchantLimit.limitBusClient != null)sqlTmp = sqlTmp + " LIMIT_BUS_CLIENT=?,";
        if(payRiskMerchantLimit.limitMinAmt != null)sqlTmp = sqlTmp + " LIMIT_MIN_AMT=?,";
        if(payRiskMerchantLimit.limitMaxAmt != null)sqlTmp = sqlTmp + " LIMIT_MAX_AMT=?,";
        if(payRiskMerchantLimit.limitDayTimes != null)sqlTmp = sqlTmp + " LIMIT_DAY_TIMES=?,";
        if(payRiskMerchantLimit.limitDaySuccessTimes != null)sqlTmp = sqlTmp + " LIMIT_DAY_SUCCESS_TIMES=?,";
        if(payRiskMerchantLimit.limitDayAmt != null)sqlTmp = sqlTmp + " LIMIT_DAY_AMT=?,";
        if(payRiskMerchantLimit.limitDaySuccessAmt != null)sqlTmp = sqlTmp + " LIMIT_DAY_SUCCESS_AMT=?,";
        if(payRiskMerchantLimit.limitMonthTimes != null)sqlTmp = sqlTmp + " LIMIT_MONTH_TIMES=?,";
        if(payRiskMerchantLimit.limitMonthSuccessTimes != null)sqlTmp = sqlTmp + " LIMIT_MONTH_SUCCESS_TIMES=?,";
        if(payRiskMerchantLimit.limitMonthAmt != null)sqlTmp = sqlTmp + " LIMIT_MONTH_AMT=?,";
        if(payRiskMerchantLimit.limitMonthSuccessAmt != null)sqlTmp = sqlTmp + " LIMIT_MONTH_SUCCESS_AMT=?,";
        if(payRiskMerchantLimit.limitYearTimes != null)sqlTmp = sqlTmp + " LIMIT_YEAR_TIMES=?,";
        if(payRiskMerchantLimit.limitYearSuccessTimes != null)sqlTmp = sqlTmp + " LIMIT_YEAR_SUCCESS_TIMES=?,";
        if(payRiskMerchantLimit.limitYearAmt != null)sqlTmp = sqlTmp + " LIMIT_YEAR_AMT=?,";
        if(payRiskMerchantLimit.limitYearSuccessAmt != null)sqlTmp = sqlTmp + " LIMIT_YEAR_SUCCESS_AMT=?,";
        if(payRiskMerchantLimit.limitStartDate != null)sqlTmp = sqlTmp + " LIMIT_START_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskMerchantLimit.limitEndDate != null)sqlTmp = sqlTmp + " LIMIT_END_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskMerchantLimit.limitAddinfo != null)sqlTmp = sqlTmp + " LIMIT_ADDINFO=?,";
        if(payRiskMerchantLimit.isUse != null)sqlTmp = sqlTmp + " IS_USE=?,";
        if(payRiskMerchantLimit.compCode != null)sqlTmp = sqlTmp + " COMP_CODE=?,";
        if(payRiskMerchantLimit.compName != null)sqlTmp = sqlTmp + " COMP_NAME=?,";
        if(payRiskMerchantLimit.createUser != null)sqlTmp = sqlTmp + " CREATE_USER=?,";
        if(payRiskMerchantLimit.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskMerchantLimit.updateUser != null)sqlTmp = sqlTmp + " UPDATE_USER=?,";
        if(payRiskMerchantLimit.updateTime != null)sqlTmp = sqlTmp + " UPDATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskMerchantLimit.limitTimeFlag != null)sqlTmp = sqlTmp + " LIMIT_TIME_FLAG=?,";
        if(payRiskMerchantLimit.limitRiskLevel != null)sqlTmp = sqlTmp + " LIMIT_RISK_LEVEL=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_RISK_MERCHANT_LIMIT "+        
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
            if(payRiskMerchantLimit.id != null)ps.setString(n++,payRiskMerchantLimit.id);
            if(payRiskMerchantLimit.limitType != null)ps.setString(n++,payRiskMerchantLimit.limitType);
            if(payRiskMerchantLimit.limitCompCode != null)ps.setString(n++,payRiskMerchantLimit.limitCompCode);
            if(payRiskMerchantLimit.limitCompType != null)ps.setString(n++,payRiskMerchantLimit.limitCompType);
            if(payRiskMerchantLimit.limitBusCode != null)ps.setString(n++,payRiskMerchantLimit.limitBusCode);
            if(payRiskMerchantLimit.limitBusClient != null)ps.setString(n++,payRiskMerchantLimit.limitBusClient);
            if(payRiskMerchantLimit.limitMinAmt != null)ps.setLong(n++,payRiskMerchantLimit.limitMinAmt);
            if(payRiskMerchantLimit.limitMaxAmt != null)ps.setLong(n++,payRiskMerchantLimit.limitMaxAmt);
            if(payRiskMerchantLimit.limitDayTimes != null)ps.setLong(n++,payRiskMerchantLimit.limitDayTimes);
            if(payRiskMerchantLimit.limitDaySuccessTimes != null)ps.setLong(n++,payRiskMerchantLimit.limitDaySuccessTimes);
            if(payRiskMerchantLimit.limitDayAmt != null)ps.setLong(n++,payRiskMerchantLimit.limitDayAmt);
            if(payRiskMerchantLimit.limitDaySuccessAmt != null)ps.setLong(n++,payRiskMerchantLimit.limitDaySuccessAmt);
            if(payRiskMerchantLimit.limitMonthTimes != null)ps.setLong(n++,payRiskMerchantLimit.limitMonthTimes);
            if(payRiskMerchantLimit.limitMonthSuccessTimes != null)ps.setLong(n++,payRiskMerchantLimit.limitMonthSuccessTimes);
            if(payRiskMerchantLimit.limitMonthAmt != null)ps.setLong(n++,payRiskMerchantLimit.limitMonthAmt);
            if(payRiskMerchantLimit.limitMonthSuccessAmt != null)ps.setLong(n++,payRiskMerchantLimit.limitMonthSuccessAmt);
            if(payRiskMerchantLimit.limitYearTimes != null)ps.setLong(n++,payRiskMerchantLimit.limitYearTimes);
            if(payRiskMerchantLimit.limitYearSuccessTimes != null)ps.setLong(n++,payRiskMerchantLimit.limitYearSuccessTimes);
            if(payRiskMerchantLimit.limitYearAmt != null)ps.setLong(n++,payRiskMerchantLimit.limitYearAmt);
            if(payRiskMerchantLimit.limitYearSuccessAmt != null)ps.setLong(n++,payRiskMerchantLimit.limitYearSuccessAmt);
            if(payRiskMerchantLimit.limitStartDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskMerchantLimit.limitStartDate));
            if(payRiskMerchantLimit.limitEndDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskMerchantLimit.limitEndDate));
            if(payRiskMerchantLimit.limitAddinfo != null)ps.setString(n++,payRiskMerchantLimit.limitAddinfo);
            if(payRiskMerchantLimit.isUse != null)ps.setString(n++,payRiskMerchantLimit.isUse);
            if(payRiskMerchantLimit.compCode != null)ps.setString(n++,payRiskMerchantLimit.compCode);
            if(payRiskMerchantLimit.compName != null)ps.setString(n++,payRiskMerchantLimit.compName);
            if(payRiskMerchantLimit.createUser != null)ps.setString(n++,payRiskMerchantLimit.createUser);
            if(payRiskMerchantLimit.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskMerchantLimit.createTime));
            if(payRiskMerchantLimit.updateUser != null)ps.setString(n++,payRiskMerchantLimit.updateUser);
            if(payRiskMerchantLimit.updateTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskMerchantLimit.updateTime));
            if(payRiskMerchantLimit.limitTimeFlag != null)ps.setString(n++,payRiskMerchantLimit.limitTimeFlag);
            if(payRiskMerchantLimit.limitRiskLevel != null)ps.setString(n++,payRiskMerchantLimit.limitRiskLevel);
            ps.setString(n++,payRiskMerchantLimit.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}