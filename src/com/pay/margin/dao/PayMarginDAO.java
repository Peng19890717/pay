package com.pay.margin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_MARGIN DAO. 
 * @author Administrator
 *
 */
public class PayMarginDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayMarginDAO.class);
    public static synchronized PayMargin getPayMarginValue(ResultSet rs)throws SQLException {
        PayMargin payMargin = new PayMargin();
        payMargin.seqNo = rs.getString("SEQ_NO");
        payMargin.custType = rs.getString("CUST_TYPE");
        payMargin.custId = rs.getString("CUST_ID");
        payMargin.pactNo = rs.getString("PACT_NO");
        payMargin.marginSign = rs.getString("MARGIN_SIGN");
        payMargin.marginPayType = rs.getString("MARGIN_PAY_TYPE");
        payMargin.marginAmt = rs.getLong("MARGIN_AMT");
        payMargin.paidInAmt = rs.getLong("PAID_IN_AMT");
        payMargin.marginAc = rs.getString("MARGIN_AC");
        payMargin.custProvisionAcNo = rs.getString("CUST_PROVISION_AC_NO");
        payMargin.marginRaSign = rs.getString("MARGIN_RA_SIGN");
        payMargin.mark = rs.getString("MARK");
        payMargin.creOperId = rs.getString("CRE_OPER_ID");
        payMargin.creTime = rs.getTimestamp("CRE_TIME");
        payMargin.lstUptOperId = rs.getString("LST_UPT_OPER_ID");
        payMargin.lstUptTime = rs.getTimestamp("LST_UPT_TIME");
        try{payMargin.storeName = rs.getString("STORE_NAME");}catch(Exception e){}
        return payMargin;
    }
    public String addPayMargin(PayMargin payMargin) throws Exception {
        String sql = "insert into PAY_MARGIN("+
            "SEQ_NO," +
            "CUST_TYPE," + 
            "CUST_ID," + 
//            "PACT_NO," + 
            "PAID_IN_AMT," + 
            "MARGIN_AC," + 
            "CUST_PROVISION_AC_NO," + 
            "MARGIN_RA_SIGN," + 
            "MARK," + 
            "CRE_OPER_ID," + 
            "LST_UPT_OPER_ID)values(?,?,?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payMargin.seqNo);
            ps.setString(n++,payMargin.custType);
            ps.setString(n++,payMargin.custId);
//            ps.setString(n++,payMargin.pactNo);
            ps.setLong(n++,payMargin.paidInAmt);
            ps.setString(n++,payMargin.marginAc);
            ps.setString(n++,payMargin.custProvisionAcNo);
            ps.setString(n++,payMargin.marginRaSign);
            ps.setString(n++,payMargin.mark);
            ps.setString(n++,payMargin.creOperId);
            ps.setString(n++,payMargin.lstUptOperId);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public PayMargin getPayMarginByMerNo(String merno) throws Exception{
        String sql = "select * from PAY_MARGIN where CUST_ID='"+merno+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next())return getPayMarginValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * Set query condition sql.
     * @param payMargin
     * @return
     */
    private String setPayMarginSql(PayMargin payMargin) {
        StringBuffer sql = new StringBuffer();
        
        if(payMargin.custId != null && payMargin.custId.length() !=0) {
            sql.append(" mar.CUST_ID like ? and ");
        }
//        if(payMargin.pactNo != null && payMargin.pactNo.length() !=0) {
//            sql.append(" PACT_NO like ? and ");
//        }
        if(payMargin.lstUptTimeStart != null && payMargin.lstUptTimeStart.length() !=0) {
			sql.append(" LST_UPT_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
		}
        if(payMargin.lstUptTimeEnd != null && payMargin.lstUptTimeEnd.length() !=0) {
			sql.append(" LST_UPT_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
		}
//        if(payMargin.marginPayType != null && payMargin.marginPayType.length() !=0) {
//            sql.append(" MARGIN_PAY_TYPE = ? and ");
//        }
        if(payMargin.storeName != null && payMargin.storeName.length() !=0) {
        	sql.append(" mer.STORE_NAME like ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payMargin
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayMarginParameter(PreparedStatement ps,PayMargin payMargin,int n)throws SQLException {
        if(payMargin.custId != null && payMargin.custId.length() !=0) {
            ps.setString(n++,"%"+payMargin.custId+"%");
        }
//        if(payMargin.pactNo != null && payMargin.pactNo.length() !=0) {
//            ps.setString(n++,"%"+payMargin.pactNo+"%");
//        }
        if(payMargin.lstUptTimeStart != null && payMargin.lstUptTimeStart.length() !=0) {
        	ps.setString(n++, payMargin.lstUptTimeStart+" 00:00:00");
		}
        if(payMargin.lstUptTimeEnd != null && payMargin.lstUptTimeEnd.length() !=0) {
			ps.setString(n++, payMargin.lstUptTimeEnd+" 23:59:59");
		}
//        if(payMargin.marginPayType != null && payMargin.marginPayType.length() !=0) {
//            ps.setString(n++,payMargin.marginPayType);
//        }
        if(payMargin.storeName != null && payMargin.storeName.length() !=0) {
        	ps.setString(n++,"%"+payMargin.storeName+"%");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payMargin
     * @return
     */
    public int getPayMarginCount(PayMargin payMargin) {
        String sqlCon = setPayMarginSql(payMargin);
        String sql = "select count(rownum) recordCount from PAY_MARGIN mar left join " +
        		"(select m.CUST_ID,m.STORE_NAME from PAY_MERCHANT m union select u.USER_ID CUST_ID,u.REAL_NAME STORE_NAME from PAY_TRAN_USER_INFO u) mer on mar.CUST_ID=mer.CUST_ID " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayMarginParameter(ps,payMargin,n);
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
     * @param payMargin
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayMarginList(PayMargin payMargin,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayMarginSql(payMargin);
        String sortOrder = sort == null || sort.length()==0?" order by CRE_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select mar.*,mer.STORE_NAME  from PAY_MARGIN mar left join " +
                "	(select m.CUST_ID,m.STORE_NAME from PAY_MERCHANT m union select u.USER_ID CUST_ID,u.REAL_NAME STORE_NAME from PAY_TRAN_USER_INFO u) mer on mar.CUST_ID=mer.CUST_ID " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayMarginParameter(ps,payMargin,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayMarginValue(rs));
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
     * remove PayMargin
     * @param seqNo
     * @throws Exception     
     */
    public void removePayMargin(String seqNo) throws Exception {
    	String sql0 = "delete from PAY_MARGIN_RCV_DET where CUST_ID=(select CUST_ID from PAY_MARGIN where SEQ_NO=?)";
        String sql = "delete from PAY_MARGIN where SEQ_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql0);
            ps.setString(1,seqNo);
            ps.executeUpdate();
            ps.close();
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
     * detail PayMargin
     * @param seqNo
     * @return PayMargin
     * @throws Exception
     */
    public PayMargin detailPayMargin(String seqNo) throws Exception {
        String sql = "select * from PAY_MARGIN where SEQ_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,seqNo);
            rs = ps.executeQuery();
            if(rs.next())return getPayMarginValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * detail PayMargin
     * @param seqNo
     * @return PayMargin
     * @throws Exception
     */
    public PayMargin getPayMarginByCustId(String custId) throws Exception {
        String sql = "select * from PAY_MARGIN where CUST_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,custId);
            rs = ps.executeQuery();
            if(rs.next())return getPayMarginValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
//    public boolean isExistMargin(String custId,String pactNo) throws Exception{
    public boolean isExistMargin(String custId) throws Exception{
        String sql = "select SEQ_NO from PAY_MARGIN where CUST_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,custId);
            rs = ps.executeQuery();
            if (rs.next())return true;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * append PayMargin
     * @param payMargin
     * @throws Exception     
     */
    public int appendPayMargin(PayMargin payMargin) throws Exception {
        String sql = "update PAY_MARGIN set PAID_IN_AMT=PAID_IN_AMT+? where SEQ_NO=?";
        log.info(sql);
        log.info("PAID_IN_AMT"+payMargin.paidInAmt+"SEQ_NO="+payMargin.seqNo);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setLong(1,payMargin.paidInAmt);
            ps.setString(2,payMargin.seqNo);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * 查询保证金总金额
     * @param payMargin
     * @return
     * @throws Exception 
     */
	public Long getTotalMarginMoney(PayMargin payMargin) throws Exception {
		Long l0 = 0l;
		String sqlCon = setPayMarginSql(payMargin);
		String sql = " SELECT SUM(PAID_IN_AMT) totalMarginMoney " 
				+ " FROM" 
                + " (SELECT mar.*,mer.STORE_NAME FROM PAY_MARGIN mar"
                + " LEFT JOIN"
                + " (SELECT m.CUST_ID,m.STORE_NAME FROM PAY_MERCHANT m UNION SELECT u.USER_ID CUST_ID,u.REAL_NAME STORE_NAME FROM PAY_TRAN_USER_INFO u) mer"
                + " ON mar.CUST_ID=mer.CUST_ID " + (sqlCon.length()==0?"":" where "+ sqlCon) +")";
		log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayMarginParameter(ps,payMargin,n);
            rs = ps.executeQuery();
            if (rs.next()) l0 = rs.getLong("totalMarginMoney");
            return l0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
}