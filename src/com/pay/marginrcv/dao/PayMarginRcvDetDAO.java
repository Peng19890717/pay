package com.pay.marginrcv.dao;

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
 * Table PAY_MARGIN_RCV_DET DAO. 
 * @author Administrator
 *
 */
public class PayMarginRcvDetDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayMarginRcvDetDAO.class);
    public static synchronized PayMarginRcvDet getPayMarginRcvDetValue(ResultSet rs)throws SQLException {
        PayMarginRcvDet payMarginRcvDet = new PayMarginRcvDet();
        payMarginRcvDet.seqNo = rs.getString("SEQ_NO");
        payMarginRcvDet.custId = rs.getString("CUST_ID");
        payMarginRcvDet.paidInAmt = rs.getLong("PAID_IN_AMT");
        payMarginRcvDet.marginAc = rs.getString("MARGIN_AC");
        payMarginRcvDet.custProvisionAcNo = rs.getString("CUST_PROVISION_AC_NO");
        payMarginRcvDet.marginRcvTime = rs.getTimestamp("MARGIN_RCV_TIME");
        payMarginRcvDet.mark = rs.getString("MARK");
        payMarginRcvDet.marginCurAmt = rs.getLong("MARGIN_CUR_AMT");
        payMarginRcvDet.pactNo = rs.getString("PACT_NO");
        return payMarginRcvDet;
    }
    
    /**
     * Set query condition sql.
     * @param payMarginRcvDet
     * @return
     */
    private String setPayMarginRcvDetSql(PayMarginRcvDet payMarginRcvDet) {
        StringBuffer sql = new StringBuffer();
        
        if(null != payMarginRcvDet.marginRcvTimeStart && 0 != payMarginRcvDet.marginRcvTimeStart.length()) {
            sql.append(" MARGIN_RCV_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(null != payMarginRcvDet.marginRcvTimeEnd && 0 != payMarginRcvDet.marginRcvTimeEnd.length()) {
        	sql.append(" MARGIN_RCV_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(null != payMarginRcvDet.custId && 0 != payMarginRcvDet.custId.length()) {
        	sql.append(" CUST_ID = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payMarginRcvDet
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayMarginRcvDetParameter(PreparedStatement ps,PayMarginRcvDet payMarginRcvDet,int n)throws SQLException {
    	
        if(null != payMarginRcvDet.marginRcvTimeStart && 0 != payMarginRcvDet.marginRcvTimeStart.length()) {
            ps.setString(n++, payMarginRcvDet.marginRcvTimeStart + " 00:00:00");
        }
        if(null != payMarginRcvDet.marginRcvTimeEnd && 0 != payMarginRcvDet.marginRcvTimeEnd.length()) {
        	ps.setString(n++, payMarginRcvDet.marginRcvTimeEnd + " 23:59:59");
        }
        if(null != payMarginRcvDet.custId) {
        	ps.setString(n++, payMarginRcvDet.custId);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payMarginRcvDet
     * @return
     */
    public int getPayMarginRcvDetCount(PayMarginRcvDet payMarginRcvDet) {
        String sqlCon = setPayMarginRcvDetSql(payMarginRcvDet);
        String sql = "select count(rownum) recordCount from PAY_MARGIN_RCV_DET " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayMarginRcvDetParameter(ps,payMarginRcvDet,n);
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
     * @param payMarginRcvDet
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayMarginRcvDetList(PayMarginRcvDet payMarginRcvDet,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayMarginRcvDetSql(payMarginRcvDet);
        String sortOrder = sort == null || sort.length()==0?" order by MARGIN_RCV_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_MARGIN_RCV_DET tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayMarginRcvDetParameter(ps,payMarginRcvDet,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayMarginRcvDetValue(rs));
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