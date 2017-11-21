package com.pay.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import util.Tools;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.jweb.dao.BaseDAO;
/**
 * Table PAY_ACC_SUBJECT DAO. 
 * @author Administrator
 *
 */
public class PayAccSubjectDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayAccSubjectDAO.class);
    public static synchronized PayAccSubject getPayAccSubjectValue(ResultSet rs)throws SQLException {
        PayAccSubject payAccSubject = new PayAccSubject();
        payAccSubject.glCode = rs.getString("GL_CODE");
        payAccSubject.glName = rs.getString("GL_NAME");
        payAccSubject.efctDate = rs.getTimestamp("EFCT_DATE");
        payAccSubject.expiredDate = rs.getTimestamp("EXPIRED_DATE");
        payAccSubject.hasSl = rs.getString("HAS_SL");
        payAccSubject.hasDl = rs.getString("HAS_DL");
        payAccSubject.glType = rs.getString("GL_TYPE");
        payAccSubject.debitCredit = rs.getString("DEBIT_CREDIT");
        payAccSubject.zeroFlag = rs.getString("ZERO_FLAG");
        payAccSubject.entBkGl = rs.getString("ENT_BK_GL");
        payAccSubject.createTime = rs.getTimestamp("CREATE_TIME");
        payAccSubject.createUser = rs.getString("CREATE_USER");
        payAccSubject.lastUpdTime = rs.getTimestamp("LAST_UPD_TIME");
        payAccSubject.lastUpdUser = rs.getString("LAST_UPD_USER");
        payAccSubject.opnTime = rs.getTimestamp("OPN_TIME");
        payAccSubject.opnTlr = rs.getString("OPN_TLR");
        payAccSubject.clsTime = rs.getTimestamp("CLS_TIME");
        payAccSubject.clsTlr = rs.getString("CLS_TLR");
        payAccSubject.lastTxnDate = rs.getTimestamp("LAST_TXN_DATE");
        payAccSubject.lastTxnTlr = rs.getString("LAST_TXN_TLR");
        payAccSubject.preDayAccBal = rs.getLong("PRE_DAY_ACC_BAL");
        payAccSubject.accBal = rs.getLong("ACC_BAL");
        payAccSubject.fltAccBal = rs.getLong("FLT_ACC_BAL");
        payAccSubject.drAccNum = rs.getLong("DR_ACC_NUM");
        payAccSubject.crAccNum = rs.getLong("CR_ACC_NUM");
        payAccSubject.manualBkFlag = rs.getString("MANUAL_BK_FLAG");
        payAccSubject.totalChkFlag = rs.getString("TOTAL_CHK_FLAG");
        return payAccSubject;
    }
    
    public static synchronized PayAccSubject getPayAccSubjectValueDetail(ResultSet rs)throws SQLException {
        PayAccSubject payAccSubject = new PayAccSubject();
        payAccSubject.glCode = rs.getString("GL_CODE");
        payAccSubject.glName = rs.getString("GL_NAME");
        payAccSubject.efctDate = rs.getTimestamp("EFCT_DATE");
        payAccSubject.expiredDate = rs.getTimestamp("EXPIRED_DATE");
        payAccSubject.hasSl = rs.getString("HAS_SL");
        payAccSubject.hasDl = rs.getString("HAS_DL");
        payAccSubject.glType = rs.getString("GL_TYPE");
        payAccSubject.debitCredit = rs.getString("DEBIT_CREDIT");
        payAccSubject.zeroFlag = rs.getString("ZERO_FLAG");
        payAccSubject.entBkGl = rs.getString("ENT_BK_GL");
        payAccSubject.createTime = rs.getTimestamp("CREATE_TIME");
        payAccSubject.createUser = rs.getString("CREATE_USER");
        payAccSubject.lastUpdTime = rs.getTimestamp("LAST_UPD_TIME");
        payAccSubject.lastUpdUser = rs.getString("LAST_UPD_USER");
        payAccSubject.opnTime = rs.getTimestamp("OPN_TIME");
        payAccSubject.opnTlr = rs.getString("OPN_TLR");
        payAccSubject.clsTime = rs.getTimestamp("CLS_TIME");
        payAccSubject.clsTlr = rs.getString("CLS_TLR");
        payAccSubject.lastTxnDate = rs.getTimestamp("LAST_TXN_DATE");
        payAccSubject.lastTxnTlr = rs.getString("LAST_TXN_TLR");
        payAccSubject.preDayAccBal = rs.getLong("PRE_DAY_ACC_BAL");
        payAccSubject.accBal = rs.getLong("ACC_BAL");
        payAccSubject.fltAccBal = rs.getLong("FLT_ACC_BAL");
        payAccSubject.drAccNum = rs.getLong("DR_ACC_NUM");
        payAccSubject.crAccNum = rs.getLong("CR_ACC_NUM");
        payAccSubject.manualBkFlag = rs.getString("MANUAL_BK_FLAG");
        payAccSubject.totalChkFlag = rs.getString("TOTAL_CHK_FLAG");
        payAccSubject.createName = rs.getString("CREATE_NAME");
        payAccSubject.lastUpdName = rs.getString("LAST_UPD_NAME");
        return payAccSubject;
    }
    
    public String addPayAccSubject(PayAccSubject payAccSubject) throws Exception {
        String sql = "insert into PAY_ACC_SUBJECT("+
            "GL_CODE," + 
            "GL_NAME," + 
            "EFCT_DATE," + 
            "EXPIRED_DATE," + 
            "HAS_SL," + 
            "HAS_DL," + 
            "GL_TYPE," + 
            "DEBIT_CREDIT," + 
            "ZERO_FLAG," + 
            "ENT_BK_GL," + 
            "CREATE_TIME," + 
            "CREATE_USER," + 
            "LAST_UPD_TIME," + 
            "LAST_UPD_USER," + 
            "OPN_TIME," + 
            "OPN_TLR," + 
            "CLS_TIME," + 
            "CLS_TLR," + 
            "LAST_TXN_DATE," + 
            "LAST_TXN_TLR," + 
            "PRE_DAY_ACC_BAL," + 
            "ACC_BAL," + 
            "FLT_ACC_BAL," + 
            "DR_ACC_NUM," + 
            "CR_ACC_NUM," + 
            "MANUAL_BK_FLAG," + 
            "TOTAL_CHK_FLAG)values(?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAccSubject.glCode);
            ps.setString(n++,payAccSubject.glName);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.efctDate));
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.expiredDate));
            ps.setString(n++,payAccSubject.hasSl);
            ps.setString(n++,payAccSubject.hasDl);
            ps.setString(n++,payAccSubject.glType);
            ps.setString(n++,payAccSubject.debitCredit);
            ps.setString(n++,payAccSubject.zeroFlag);
            ps.setString(n++,payAccSubject.entBkGl);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.createTime));
            ps.setString(n++,payAccSubject.createUser);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.lastUpdTime));
            ps.setString(n++,payAccSubject.lastUpdUser);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.opnTime));
            ps.setString(n++,payAccSubject.opnTlr);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.clsTime));
            ps.setString(n++,payAccSubject.clsTlr);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.lastTxnDate));
            ps.setString(n++,payAccSubject.lastTxnTlr);
            ps.setLong(n++,payAccSubject.preDayAccBal==null?0:payAccSubject.preDayAccBal);
            ps.setLong(n++,payAccSubject.accBal==null?0:payAccSubject.accBal);
            ps.setLong(n++,payAccSubject.fltAccBal==null?0:payAccSubject.fltAccBal);
            ps.setLong(n++,payAccSubject.drAccNum==null?0:payAccSubject.drAccNum);
            ps.setLong(n++,payAccSubject.crAccNum==null?0:payAccSubject.crAccNum);
            ps.setString(n++,payAccSubject.manualBkFlag);
            ps.setString(n++,payAccSubject.totalChkFlag);
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
        String sql = "select * from PAY_ACC_SUBJECT";
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
                list.add(getPayAccSubjectValue(rs));
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
     * @param payAccSubject
     * @return
     */
    private String setPayAccSubjectSql(PayAccSubject payAccSubject) {
        StringBuffer sql = new StringBuffer();
        
        if(payAccSubject.glCode != null && payAccSubject.glCode.length() !=0) {
            sql.append(" GL_CODE = ? and ");
        }
        if(payAccSubject.glName != null && payAccSubject.glName.length() !=0) {
            sql.append(" GL_NAME = ? and ");
        }
        if(payAccSubject.glType != null && payAccSubject.glType.length() !=0) {
            sql.append(" GL_TYPE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payAccSubject
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayAccSubjectParameter(PreparedStatement ps,PayAccSubject payAccSubject,int n)throws SQLException {
        if(payAccSubject.glCode != null && payAccSubject.glCode.length() !=0) {
            ps.setString(n++,payAccSubject.glCode);
        }
        if(payAccSubject.glName != null && payAccSubject.glName.length() !=0) {
            ps.setString(n++,payAccSubject.glName);
        }
        if(payAccSubject.glType != null && payAccSubject.glType.length() !=0) {
            ps.setString(n++,payAccSubject.glType);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payAccSubject
     * @return
     */
    public int getPayAccSubjectCount(PayAccSubject payAccSubject) {
        String sqlCon = setPayAccSubjectSql(payAccSubject);
        String sql = "select count(rownum) recordCount from PAY_ACC_SUBJECT " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayAccSubjectParameter(ps,payAccSubject,n);
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
     * @param payAccSubject
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayAccSubjectList(PayAccSubject payAccSubject,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayAccSubjectSql(payAccSubject);
        String sortOrder = sort == null || sort.length()==0?" order by GL_CODE desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_ACC_SUBJECT tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayAccSubjectParameter(ps,payAccSubject,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayAccSubjectValue(rs));
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
     * remove PayAccSubject
     * @param glCode
     * @throws Exception     
     */
    public void removePayAccSubject(String glCode) throws Exception {
        String sql = "delete from PAY_ACC_SUBJECT where GL_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,glCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayAccSubject
     * @param glCode
     * @return PayAccSubject
     * @throws Exception
     */
    public PayAccSubject detailPayAccSubject(String glCode) throws Exception {
        String sql = " SELECT psu.*,juser1.name CREATE_NAME,juser2.name LAST_UPD_NAME FROM PAY_ACC_SUBJECT psu "
        		+ "LEFT JOIN PAY_JWEB_USER juser1 ON psu.CREATE_USER = juser1.ID "
        		+ "LEFT JOIN PAY_JWEB_USER juser2 ON psu.LAST_UPD_USER = juser2.ID "
        		+ "where GL_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,glCode);
            rs = ps.executeQuery();
            if(rs.next())return getPayAccSubjectValueDetail(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    
    public PayAccSubject detailPayAccSubjectByGlName(String glName) throws Exception {
        String sql = "select * from PAY_ACC_SUBJECT where GL_NAME=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,glName);
            rs = ps.executeQuery();
            if(rs.next())return getPayAccSubjectValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    
    public List detailPayAccSubjectListByGlName(String glName) throws Exception {
        String sql = "select * from PAY_ACC_SUBJECT where GL_NAME=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            ps.setString(1, glName);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayAccSubjectValue(rs));
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
     * update PayAccSubject
     * @param payAccSubject
     * @throws Exception
     */
    public void updatePayAccSubject(PayAccSubject payAccSubject) throws Exception {
        String sqlTmp = "";
        if(payAccSubject.glCode != null)sqlTmp = sqlTmp + " GL_CODE=?,";
        if(payAccSubject.glName != null)sqlTmp = sqlTmp + " GL_NAME=?,";
        if(payAccSubject.efctDate != null)sqlTmp = sqlTmp + " EFCT_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAccSubject.expiredDate != null)sqlTmp = sqlTmp + " EXPIRED_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAccSubject.hasSl != null)sqlTmp = sqlTmp + " HAS_SL=?,";
        if(payAccSubject.hasDl != null)sqlTmp = sqlTmp + " HAS_DL=?,";
        if(payAccSubject.glType != null)sqlTmp = sqlTmp + " GL_TYPE=?,";
        if(payAccSubject.debitCredit != null)sqlTmp = sqlTmp + " DEBIT_CREDIT=?,";
        if(payAccSubject.zeroFlag != null)sqlTmp = sqlTmp + " ZERO_FLAG=?,";
        if(payAccSubject.entBkGl != null)sqlTmp = sqlTmp + " ENT_BK_GL=?,";
        if(payAccSubject.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAccSubject.createUser != null)sqlTmp = sqlTmp + " CREATE_USER=?,";
        if(payAccSubject.lastUpdTime != null)sqlTmp = sqlTmp + " LAST_UPD_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAccSubject.lastUpdUser != null)sqlTmp = sqlTmp + " LAST_UPD_USER=?,";
        if(payAccSubject.opnTime != null)sqlTmp = sqlTmp + " OPN_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAccSubject.opnTlr != null)sqlTmp = sqlTmp + " OPN_TLR=?,";
        if(payAccSubject.clsTime != null)sqlTmp = sqlTmp + " CLS_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAccSubject.clsTlr != null)sqlTmp = sqlTmp + " CLS_TLR=?,";
        if(payAccSubject.lastTxnDate != null)sqlTmp = sqlTmp + " LAST_TXN_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payAccSubject.lastTxnTlr != null)sqlTmp = sqlTmp + " LAST_TXN_TLR=?,";
        if(payAccSubject.preDayAccBal != null)sqlTmp = sqlTmp + " PRE_DAY_ACC_BAL=?,";
        if(payAccSubject.accBal != null)sqlTmp = sqlTmp + " ACC_BAL=?,";
        if(payAccSubject.fltAccBal != null)sqlTmp = sqlTmp + " FLT_ACC_BAL=?,";
        if(payAccSubject.drAccNum != null)sqlTmp = sqlTmp + " DR_ACC_NUM=?,";
        if(payAccSubject.crAccNum != null)sqlTmp = sqlTmp + " CR_ACC_NUM=?,";
        if(payAccSubject.manualBkFlag != null)sqlTmp = sqlTmp + " MANUAL_BK_FLAG=?,";
        if(payAccSubject.totalChkFlag != null)sqlTmp = sqlTmp + " TOTAL_CHK_FLAG=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_ACC_SUBJECT "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where GL_CODE=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payAccSubject.glCode != null)ps.setString(n++,payAccSubject.glCode);
            if(payAccSubject.glName != null)ps.setString(n++,payAccSubject.glName);
            if(payAccSubject.efctDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.efctDate));
            if(payAccSubject.expiredDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.expiredDate));
            if(payAccSubject.hasSl != null)ps.setString(n++,payAccSubject.hasSl);
            if(payAccSubject.hasDl != null)ps.setString(n++,payAccSubject.hasDl);
            if(payAccSubject.glType != null)ps.setString(n++,payAccSubject.glType);
            if(payAccSubject.debitCredit != null)ps.setString(n++,payAccSubject.debitCredit);
            if(payAccSubject.zeroFlag != null)ps.setString(n++,payAccSubject.zeroFlag);
            if(payAccSubject.entBkGl != null)ps.setString(n++,payAccSubject.entBkGl);
            if(payAccSubject.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.createTime));
            if(payAccSubject.createUser != null)ps.setString(n++,payAccSubject.createUser);
            if(payAccSubject.lastUpdTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.lastUpdTime));
            if(payAccSubject.lastUpdUser != null)ps.setString(n++,payAccSubject.lastUpdUser);
            if(payAccSubject.opnTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.opnTime));
            if(payAccSubject.opnTlr != null)ps.setString(n++,payAccSubject.opnTlr);
            if(payAccSubject.clsTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.clsTime));
            if(payAccSubject.clsTlr != null)ps.setString(n++,payAccSubject.clsTlr);
            if(payAccSubject.lastTxnDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.lastTxnDate));
            if(payAccSubject.lastTxnTlr != null)ps.setString(n++,payAccSubject.lastTxnTlr);
            if(payAccSubject.preDayAccBal != null)ps.setLong(n++,payAccSubject.preDayAccBal);
            if(payAccSubject.accBal != null)ps.setLong(n++,payAccSubject.accBal);
            if(payAccSubject.fltAccBal != null)ps.setLong(n++,payAccSubject.fltAccBal);
            if(payAccSubject.drAccNum != null)ps.setLong(n++,payAccSubject.drAccNum);
            if(payAccSubject.crAccNum != null)ps.setLong(n++,payAccSubject.crAccNum);
            if(payAccSubject.manualBkFlag != null)ps.setString(n++,payAccSubject.manualBkFlag);
            if(payAccSubject.totalChkFlag != null)ps.setString(n++,payAccSubject.totalChkFlag);
            ps.setString(n++,payAccSubject.glCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}