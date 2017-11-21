package com.pay.acctckinf.dao;

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
 * Table PAY_ACC_TCK_INF DAO. 
 * @author Administrator
 *
 */
public class PayAccTckInfDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayAccTckInfDAO.class);
    public static synchronized PayAccTckInf getPayAccTckInfValue(ResultSet rs)throws SQLException {
        PayAccTckInf payAccTckInf = new PayAccTckInf();
        payAccTckInf.accDat = rs.getString("ACC_DAT");
        payAccTckInf.tckNo = rs.getString("TCK_NO");
        payAccTckInf.tckSubNo = rs.getString("TCK_SUB_NO");
        payAccTckInf.bizTypCode = rs.getString("BIZ_TYP_CODE");
        payAccTckInf.txnTime = rs.getTimestamp("TXN_TIME");
        payAccTckInf.txnOrgNo = rs.getString("TXN_ORG_NO");
        payAccTckInf.reEntry = rs.getString("RE_ENTRY");
        payAccTckInf.updTckFlag = rs.getString("UPD_TCK_FLAG");
        payAccTckInf.chkFlag = rs.getString("CHK_FLAG");
        payAccTckInf.accOrgNo = rs.getString("ACC_ORG_NO");
        payAccTckInf.ccy = rs.getString("CCY");
        payAccTckInf.accSubject = rs.getString("ACC_SUBJECT");
        payAccTckInf.accNo = rs.getString("ACC_NO");
        payAccTckInf.drCrFlag = rs.getString("DR_CR_FLAG");
        payAccTckInf.txnAmt = rs.getLong("TXN_AMT");
        payAccTckInf.accNoBal = rs.getLong("ACC_NO_BAL");
        payAccTckInf.vchTyp = rs.getString("VCH_TYP");
        payAccTckInf.vchCod = rs.getString("VCH_COD");
        payAccTckInf.oppAccSign = rs.getString("OPP_ACC_SIGN");
        payAccTckInf.oppAccNo = rs.getString("OPP_ACC_NO");
        payAccTckInf.oppAccName = rs.getString("OPP_ACC_NAME");
        payAccTckInf.rmkCodInf = rs.getString("RMK_COD_INF");
        payAccTckInf.tckInf = rs.getString("TCK_INF");
        return payAccTckInf;
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_ACC_TCK_INF";
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
                list.add(getPayAccTckInfValue(rs));
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
     * @param payAccTckInf
     * @return
     */
    private String setPayAccTckInfSql(PayAccTckInf payAccTckInf) {
        StringBuffer sql = new StringBuffer();
        
        if(payAccTckInf.txnTimeStart != null) {
            sql.append(" TXN_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payAccTckInf.txnTimeEnd != null) {
        	sql.append(" TXN_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payAccTckInf.accSubject != null && payAccTckInf.accSubject.length() !=0) {
            sql.append(" ACC_SUBJECT like ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payAccTckInf
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayAccTckInfParameter(PreparedStatement ps,PayAccTckInf payAccTckInf,int n)throws SQLException {
        if(payAccTckInf.txnTimeStart != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccTckInf.txnTimeStart));
        }
        if(payAccTckInf.txnTimeEnd != null) {
        	ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccTckInf.txnTimeEnd));
        }
        if(payAccTckInf.accSubject != null && payAccTckInf.accSubject.length() !=0) {
            ps.setString(n++,"%"+payAccTckInf.accSubject+"%");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payAccTckInf
     * @return
     */
    public int getPayAccTckInfCount(PayAccTckInf payAccTckInf) {
        String sqlCon = setPayAccTckInfSql(payAccTckInf);
        String sql = "select count(rownum) recordCount from PAY_ACC_TCK_INF " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayAccTckInfParameter(ps,payAccTckInf,n);
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
     * @param payAccTckInf
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayAccTckInfList(PayAccTckInf payAccTckInf,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayAccTckInfSql(payAccTckInf);
        String sortOrder = sort == null || sort.length()==0?" order by ACC_DAT desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_ACC_TCK_INF tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayAccTckInfParameter(ps,payAccTckInf,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayAccTckInfValue(rs));
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