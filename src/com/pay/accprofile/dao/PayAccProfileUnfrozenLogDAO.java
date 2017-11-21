package com.pay.accprofile.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import util.Tools;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import com.jweb.dao.BaseDAO;
/**
 * Table PAY_ACC_PROFILE_UNFROZEN_LOG DAO. 
 * @author Administrator
 *
 */
public class PayAccProfileUnfrozenLogDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayAccProfileUnfrozenLogDAO.class);
    public static synchronized PayAccProfileUnfrozenLog getPayAccProfileUnfrozenLogValue(ResultSet rs)throws SQLException {
        PayAccProfileUnfrozenLog payAccProfileUnfrozenLog = new PayAccProfileUnfrozenLog();
        payAccProfileUnfrozenLog.id = rs.getString("ID");
        payAccProfileUnfrozenLog.unfrozenAmt = rs.getLong("UNFROZEN_AMT");
        payAccProfileUnfrozenLog.flag = rs.getString("FLAG");
        payAccProfileUnfrozenLog.createTime = rs.getTimestamp("CREATE_TIME");
        payAccProfileUnfrozenLog.optTime = rs.getTimestamp("OPT_TIME");
        payAccProfileUnfrozenLog.userId = rs.getString("USER_ID");
        payAccProfileUnfrozenLog.accFrozenId = rs.getString("ACC_FROZEN_ID");
        payAccProfileUnfrozenLog.remark = rs.getString("REMARK");
        return payAccProfileUnfrozenLog;
    }
    public String addPayAccProfileUnfrozenLog(PayAccProfileUnfrozenLog payAccProfileUnfrozenLog) throws Exception {
        String sql = "insert into PAY_ACC_PROFILE_UNFROZEN_LOG("+
            "ID," + 
            "UNFROZEN_AMT," + 
            "FLAG," + 
            "USER_ID," + 
            "ACC_FROZEN_ID,"+
            "REMARK)values(?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAccProfileUnfrozenLog.id);
            ps.setLong(n++,payAccProfileUnfrozenLog.unfrozenAmt);
            ps.setString(n++,payAccProfileUnfrozenLog.flag);
            ps.setString(n++,payAccProfileUnfrozenLog.userId);
            ps.setString(n++,payAccProfileUnfrozenLog.accFrozenId);
            ps.setString(n++,payAccProfileUnfrozenLog.remark);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * Set query condition sql.
     * @param payAccProfileUnfrozenLog
     * @return
     */
    private String setPayAccProfileUnfrozenLogSql(PayAccProfileUnfrozenLog payAccProfileUnfrozenLog) {
        StringBuffer sql = new StringBuffer();
        if(payAccProfileUnfrozenLog.accFrozenId != null && payAccProfileUnfrozenLog.accFrozenId.length() !=0) {
            sql.append(" ACC_FROZEN_ID = ? and ");
        }
        if(payAccProfileUnfrozenLog.flag != null && payAccProfileUnfrozenLog.flag.length() !=0) {
            sql.append(" FLAG = ? and ");
        }                           
        if(payAccProfileUnfrozenLog.createTimeStart != null ) {
            sql.append(" CREATE_TIME >= to_date('"+
	        		new SimpleDateFormat("yyyy-MM-dd").format(payAccProfileUnfrozenLog.createTimeStart)+" 00:00:00"+"','yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payAccProfileUnfrozenLog.createTimeEnd != null ) {
        	sql.append("  CREATE_TIME <= to_date('"+
	        		new SimpleDateFormat("yyyy-MM-dd").format(payAccProfileUnfrozenLog.createTimeEnd)+" 23:59:59"+"','yyyy-mm-dd hh24:mi:ss') and   ");
        }
        if(payAccProfileUnfrozenLog.optTimeStart != null ) {
        	sql.append(" OPT_TIME >= to_date('"+
	        		new SimpleDateFormat("yyyy-MM-dd").format(payAccProfileUnfrozenLog.optTimeStart)+" 00:00:00"+"','yyyy-mm-dd hh24:mi:ss') and  ");
        }
        if(payAccProfileUnfrozenLog.optTimeEnd != null ) {
        	sql.append(" OPT_TIME <= to_date('"+
	        		new SimpleDateFormat("yyyy-MM-dd").format(payAccProfileUnfrozenLog.optTimeEnd)+" 23:59:59"+"','yyyy-mm-dd hh24:mi:ss') and  ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payAccProfileUnfrozenLog
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayAccProfileUnfrozenLogParameter(PreparedStatement ps,PayAccProfileUnfrozenLog payAccProfileUnfrozenLog,int n)throws SQLException {
        if(payAccProfileUnfrozenLog.accFrozenId != null && payAccProfileUnfrozenLog.accFrozenId.length() !=0) {
            ps.setString(n++,payAccProfileUnfrozenLog.accFrozenId);
        }
        if(payAccProfileUnfrozenLog.flag != null && payAccProfileUnfrozenLog.flag.length() !=0) {
            ps.setString(n++,payAccProfileUnfrozenLog.flag);
        }
//        if(payAccProfileUnfrozenLog.createTimeStart != null ) {
//        	ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileUnfrozenLog.createTimeStart));
//        }
//        if(payAccProfileUnfrozenLog.createTimeEnd != null ) {
//        	ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileUnfrozenLog.createTimeEnd));
//        }
//        if(payAccProfileUnfrozenLog.optTimeStart != null ) {
//        	ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileUnfrozenLog.optTimeStart));
//        }
//        if(payAccProfileUnfrozenLog.optTimeEnd != null ) {
//        	ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileUnfrozenLog.optTimeEnd));
//        }
        return n;
    }
    /**
     * Get records count.
     * @param payAccProfileUnfrozenLog
     * @return
     */
    public int getPayAccProfileUnfrozenLogCount(PayAccProfileUnfrozenLog payAccProfileUnfrozenLog) {
        String sqlCon = setPayAccProfileUnfrozenLogSql(payAccProfileUnfrozenLog);
        String sql = "select count(rownum) recordCount from PAY_ACC_PROFILE_UNFROZEN_LOG " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayAccProfileUnfrozenLogParameter(ps,payAccProfileUnfrozenLog,n);
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
     * @param payAccProfileUnfrozenLog
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayAccProfileUnfrozenLogList(PayAccProfileUnfrozenLog payAccProfileUnfrozenLog,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayAccProfileUnfrozenLogSql(payAccProfileUnfrozenLog);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_ACC_PROFILE_UNFROZEN_LOG tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayAccProfileUnfrozenLogParameter(ps,payAccProfileUnfrozenLog,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayAccProfileUnfrozenLogValue(rs));
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
     * detail PayAccProfileUnfrozenLog
     * @param id
     * @return PayAccProfileUnfrozenLog
     * @throws Exception
     */
    public PayAccProfileUnfrozenLog detailPayAccProfileUnfrozenLog(String id) throws Exception {
        String sql = "select * from PAY_ACC_PROFILE_UNFROZEN_LOG where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayAccProfileUnfrozenLogValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayAccProfileUnfrozenLog
     * @param payAccProfileUnfrozenLog
     * @throws Exception
     */
    public void updatePayAccProfileUnfrozenLog(PayAccProfileUnfrozenLog payAccProfileUnfrozenLog) throws Exception {
        String sqlTmp = "";
        if(payAccProfileUnfrozenLog.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payAccProfileUnfrozenLog.unfrozenAmt != null)sqlTmp = sqlTmp + " UNFROZEN_AMT=?,";
        if(payAccProfileUnfrozenLog.flag != null)sqlTmp = sqlTmp + " FLAG=?,";
        if(payAccProfileUnfrozenLog.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAccProfileUnfrozenLog.optTime != null)sqlTmp = sqlTmp + " OPT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAccProfileUnfrozenLog.userId != null)sqlTmp = sqlTmp + " USER_ID=?,";
        if(payAccProfileUnfrozenLog.accFrozenId != null)sqlTmp = sqlTmp + " ACC_FROZEN_ID=?,";
        if(payAccProfileUnfrozenLog.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_ACC_PROFILE_UNFROZEN_LOG "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where ID=?"; 
        log.info(sql);
        log.info(payAccProfileUnfrozenLog);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payAccProfileUnfrozenLog.id != null)ps.setString(n++,payAccProfileUnfrozenLog.id);
            if(payAccProfileUnfrozenLog.unfrozenAmt != null)ps.setLong(n++,payAccProfileUnfrozenLog.unfrozenAmt);
            if(payAccProfileUnfrozenLog.flag != null)ps.setString(n++,payAccProfileUnfrozenLog.flag);
            if(payAccProfileUnfrozenLog.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileUnfrozenLog.createTime));
            if(payAccProfileUnfrozenLog.optTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileUnfrozenLog.optTime));
            if(payAccProfileUnfrozenLog.userId != null)ps.setString(n++,payAccProfileUnfrozenLog.userId);
            if(payAccProfileUnfrozenLog.accFrozenId != null)ps.setString(n++,payAccProfileUnfrozenLog.accFrozenId);
            if(payAccProfileUnfrozenLog.remark != null)ps.setString(n++,payAccProfileUnfrozenLog.remark);
            ps.setString(n++,payAccProfileUnfrozenLog.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayAccProfileUnfrozenLog
     * @param payAccProfileUnfrozenLog
     * @throws Exception
     */
    public void checkPayAccProfileUnfrozenLog(PayAccProfileUnfrozenLog payAccProfileUnfrozenLog,Long amt) throws Exception {
        String sql = "update PAY_ACC_PROFILE_UNFROZEN_LOG l set FLAG=?,REMARK=?,OPT_TIME=sysdate,USER_ID=? " +
        		"where exists(select 1 from PAY_ACC_PROFILE_FROZEN where l.UNFROZEN_AMT<=CUR_AMT and ID=?) and FLAG='0' and ID=?";
        log.info(sql);
        log.info("flag="+payAccProfileUnfrozenLog.flag+",remark="+payAccProfileUnfrozenLog.remark+",userId="+
        		payAccProfileUnfrozenLog.userId+",accFrozenId="+payAccProfileUnfrozenLog.accFrozenId+",id="+payAccProfileUnfrozenLog.id);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAccProfileUnfrozenLog.flag);
            ps.setString(n++,payAccProfileUnfrozenLog.remark);
            ps.setString(n++,payAccProfileUnfrozenLog.userId);
            ps.setString(n++,payAccProfileUnfrozenLog.accFrozenId);
            ps.setString(n++,payAccProfileUnfrozenLog.id);
            if(ps.executeUpdate()!=1)throw new Exception("解冻已处理或者解冻金额大于当前冻结金额");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
}