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
 * Table PAY_GENERAL_LEDGER_HIS DAO. 
 * @author Administrator
 *
 */
public class PayGeneralLedgerHisDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayGeneralLedgerHisDAO.class);
    public static synchronized PayGeneralLedgerHis getPayGeneralLedgerHisValue(ResultSet rs)throws SQLException {
        PayGeneralLedgerHis payGeneralLedgerHis = new PayGeneralLedgerHis();
        payGeneralLedgerHis.seqNo = rs.getString("SEQ_NO");
        payGeneralLedgerHis.brNo = rs.getString("BR_NO");
        payGeneralLedgerHis.ccy = rs.getString("CCY");
        payGeneralLedgerHis.glCode = rs.getString("GL_CODE");
        payGeneralLedgerHis.glName = rs.getString("GL_NAME");
        payGeneralLedgerHis.acDate = rs.getTimestamp("AC_DATE");
        payGeneralLedgerHis.prevDrBal = rs.getLong("PREV_DR_BAL");
        payGeneralLedgerHis.prevCrBal = rs.getLong("PREV_CR_BAL");
        payGeneralLedgerHis.drCount = rs.getLong("DR_COUNT");
        payGeneralLedgerHis.drAmt = rs.getLong("DR_AMT");
        payGeneralLedgerHis.crCount = rs.getLong("CR_COUNT");
        payGeneralLedgerHis.crAmt = rs.getLong("CR_AMT");
        payGeneralLedgerHis.currDrBal = rs.getLong("CURR_DR_BAL");
        payGeneralLedgerHis.currCrBal = rs.getLong("CURR_CR_BAL");
        return payGeneralLedgerHis;
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_GENERAL_LEDGER_HIS";
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
                list.add(getPayGeneralLedgerHisValue(rs));
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
     * @param payGeneralLedgerHis
     * @return
     */
    private String setPayGeneralLedgerHisSql(PayGeneralLedgerHis payGeneralLedgerHis) {
        StringBuffer sql = new StringBuffer();
        
        if(payGeneralLedgerHis.glCode != null && payGeneralLedgerHis.glCode.length() !=0) {
            sql.append(" GL_CODE = ? and ");
        }
        if(payGeneralLedgerHis.acDate != null) {
            sql.append(" AC_DATE >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payGeneralLedgerHis
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayGeneralLedgerHisParameter(PreparedStatement ps,PayGeneralLedgerHis payGeneralLedgerHis,int n)throws SQLException {
        if(payGeneralLedgerHis.glCode != null && payGeneralLedgerHis.glCode.length() !=0) {
            ps.setString(n++,payGeneralLedgerHis.glCode);
        }
        if(payGeneralLedgerHis.acDate != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payGeneralLedgerHis.acDate));
        }
        return n;
    }
    /**
     * Get records count.
     * @param payGeneralLedgerHis
     * @return
     */
    public int getPayGeneralLedgerHisCount(PayGeneralLedgerHis payGeneralLedgerHis) {
        String sqlCon = setPayGeneralLedgerHisSql(payGeneralLedgerHis);
        String sql = "select count(rownum) recordCount from PAY_GENERAL_LEDGER_HIS " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayGeneralLedgerHisParameter(ps,payGeneralLedgerHis,n);
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
     * @param payGeneralLedgerHis
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayGeneralLedgerHisList(PayGeneralLedgerHis payGeneralLedgerHis,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayGeneralLedgerHisSql(payGeneralLedgerHis);
        String sortOrder = sort == null || sort.length()==0?" order by SEQ_NO desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.*, acc.gl_name as GL_NAME from (" +
                "   select tmp.*  from PAY_GENERAL_LEDGER_HIS tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "  ) tmp1 " +
                "	left join (select gl_code , gl_name from PAY_ACC_SUBJECT) acc on tmp1.GL_CODE = acc.GL_CODE "+
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
            setPayGeneralLedgerHisParameter(ps,payGeneralLedgerHis,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayGeneralLedgerHisValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
}