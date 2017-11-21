package com.pay.fee.dao;

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
 * Table PAY_FEE_SECTION DAO. 
 * @author Administrator
 *
 */
public class PayFeeSectionDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayFeeSectionDAO.class);
    public static synchronized PayFeeSection getPayFeeSectionValue(ResultSet rs)throws SQLException {
        PayFeeSection payFeeSection = new PayFeeSection();
        payFeeSection.seqNo = rs.getString("SEQ_NO");
        payFeeSection.feeCode = rs.getString("FEE_CODE");
        payFeeSection.maxAmt = rs.getLong("MAX_AMT");
        payFeeSection.feeAmt = rs.getLong("FEE_AMT");
        payFeeSection.feeRatio = rs.getLong("FEE_RATIO");
        return payFeeSection;
    }
    public String addPayFeeSection(PayFeeSection payFeeSection) throws Exception {
        String sql = "insert into PAY_FEE_SECTION("+
            "SEQ_NO," + 
            "FEE_CODE," + 
            "MAX_AMT," + 
            "FEE_AMT," + 
            "FEE_RATIO)values(?,?,?,?,?)";
        log.info(sql);
        log.info("SEQ_NO="+payFeeSection.seqNo+";FEE_CODE="+payFeeSection.feeCode+";MAX_AMT="+payFeeSection.maxAmt+";FEE_AMT="+payFeeSection.feeAmt+
        		";FEE_RATIO="+payFeeSection.feeRatio);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payFeeSection.seqNo);
            ps.setString(n++,payFeeSection.feeCode);
            ps.setLong(n++,payFeeSection.maxAmt);
            ps.setLong(n++,payFeeSection.feeAmt);
            ps.setLong(n++,payFeeSection.feeRatio);
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
        String sql = "select * from PAY_FEE_SECTION";
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
                list.add(getPayFeeSectionValue(rs));
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
     * @param payFeeSection
     * @return
     */
    private String setPayFeeSectionSql(PayFeeSection payFeeSection) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payFeeSection
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayFeeSectionParameter(PreparedStatement ps,PayFeeSection payFeeSection,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payFeeSection
     * @return
     */
    public int getPayFeeSectionCount(PayFeeSection payFeeSection) {
        String sqlCon = setPayFeeSectionSql(payFeeSection);
        String sql = "select count(rownum) recordCount from PAY_FEE_SECTION " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayFeeSectionParameter(ps,payFeeSection,n);
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
     * @param payFeeSection
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayFeeSectionList(PayFeeSection payFeeSection,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayFeeSectionSql(payFeeSection);
        String sortOrder = sort == null || sort.length()==0?" order by SEQ_NO desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_FEE_SECTION tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayFeeSectionParameter(ps,payFeeSection,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayFeeSectionValue(rs));
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
     * remove PayFeeSection
     * @param id
     * @throws Exception     
     */
    public void removePayFeeSection(String seqNo) throws Exception {
        String sql = "delete from PAY_FEE_SECTION where SEQ_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,seqNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayFeeSection
     * @param payFeeSection
     * @throws Exception
     */
    public void updatePayFeeSection(PayFeeSection payFeeSection) throws Exception {
        String sqlTmp = "";
        if(payFeeSection.seqNo != null)sqlTmp = sqlTmp + " SEQ_NO=?,";
        if(payFeeSection.feeCode != null)sqlTmp = sqlTmp + " FEE_CODE=?,";
        if(payFeeSection.maxAmt != null)sqlTmp = sqlTmp + " MAX_AMT=?,";
        if(payFeeSection.feeAmt != null)sqlTmp = sqlTmp + " FEE_AMT=?,";
        if(payFeeSection.feeRatio != null)sqlTmp = sqlTmp + " FEE_RATIO=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_FEE_SECTION "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where SEQ_NO=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payFeeSection.seqNo != null)ps.setString(n++,payFeeSection.seqNo);
            if(payFeeSection.feeCode != null)ps.setString(n++,payFeeSection.feeCode);
            if(payFeeSection.maxAmt != null)ps.setLong(n++,payFeeSection.maxAmt);
            if(payFeeSection.feeAmt != null)ps.setLong(n++,payFeeSection.feeAmt);
            if(payFeeSection.feeRatio != null)ps.setLong(n++,payFeeSection.feeRatio);
            ps.setString(n++,payFeeSection.seqNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}