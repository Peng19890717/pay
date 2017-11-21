package com.pay.merchant.dao;

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
import com.pay.order.dao.PayProductOrder;
/**
 * Table PAY_ACC_PROFILE DAO. 
 * @author Administrator
 *
 */
public class PayAccProfileDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayAccProfileDAO.class);
    public static synchronized PayAccProfile getPayAccProfileValue(ResultSet rs)throws SQLException {
        PayAccProfile payAccProfile = new PayAccProfile();
        payAccProfile.id = rs.getString("ID");
        payAccProfile.payAcNo = rs.getString("PAY_AC_NO");
        payAccProfile.acType = rs.getString("AC_TYPE");
        payAccProfile.brNo = rs.getString("BR_NO");
        payAccProfile.ccy = rs.getString("CCY");
        payAccProfile.cashAcBal = rs.getLong("CASH_AC_BAL");
        payAccProfile.consAcBal = rs.getLong("CONS_AC_BAL");
        payAccProfile.minStlBalance = rs.getLong("MIN_STL_BALANCE");
        payAccProfile.frozBalance = rs.getLong("FROZ_BALANCE");
        payAccProfile.glCode = rs.getString("GL_CODE");
        payAccProfile.acStatus = rs.getString("AC_STATUS");
        payAccProfile.listStsFlg = rs.getString("LIST_STS_FLG");
        payAccProfile.resvFlg = rs.getString("RESV_FLG");
        payAccProfile.accSumNum = rs.getLong("ACC_SUM_NUM");
        payAccProfile.lstTxTime = rs.getTimestamp("LST_TX_TIME");
        return payAccProfile;
    }
    public String addPayAccProfile(PayAccProfile payAccProfile) throws Exception {
        String sql = "insert into PAY_ACC_PROFILE("+
            "ID," + 
            "PAY_AC_NO," + 
            "AC_TYPE," + 
            "BR_NO," + 
            "CCY," + 
            "CASH_AC_BAL," + 
            "CONS_AC_BAL," + 
            "MIN_STL_BALANCE," + 
            "FROZ_BALANCE," + 
            "GL_CODE," + 
            "AC_STATUS," + 
            "LIST_STS_FLG," + 
            "RESV_FLG," + 
            "ACC_SUM_NUM," + 
            "LST_TX_TIME)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);
        log.info(payAccProfile.toString().replaceAll("\n",";"));
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAccProfile.id);
            ps.setString(n++,payAccProfile.payAcNo);
            ps.setString(n++,payAccProfile.acType);
            ps.setString(n++,payAccProfile.brNo);
            ps.setString(n++,payAccProfile.ccy);
            ps.setLong(n++,payAccProfile.cashAcBal);
            ps.setLong(n++,payAccProfile.consAcBal);
            ps.setLong(n++,payAccProfile.minStlBalance);
            ps.setLong(n++,payAccProfile.frozBalance);
            ps.setString(n++,payAccProfile.glCode);
            ps.setString(n++,payAccProfile.acStatus);
            ps.setString(n++,payAccProfile.listStsFlg);
            ps.setString(n++,payAccProfile.resvFlg);
            ps.setLong(n++,payAccProfile.accSumNum);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfile.lstTxTime));
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
        String sql = "select * from PAY_ACC_PROFILE";
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
                list.add(getPayAccProfileValue(rs));
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
     * @param payAccProfile
     * @return
     */
    private String setPayAccProfileSql(PayAccProfile payAccProfile) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payAccProfile
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayAccProfileParameter(PreparedStatement ps,PayAccProfile payAccProfile,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payAccProfile
     * @return
     */
    public int getPayAccProfileCount(PayAccProfile payAccProfile) {
        String sqlCon = setPayAccProfileSql(payAccProfile);
        String sql = "select count(rownum) recordCount from PAY_ACC_PROFILE " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayAccProfileParameter(ps,payAccProfile,n);
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
     * @param payAccProfile
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayAccProfileList(PayAccProfile payAccProfile,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayAccProfileSql(payAccProfile);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_ACC_PROFILE tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayAccProfileParameter(ps,payAccProfile,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayAccProfileValue(rs));
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
     * remove PayAccProfile
     * @param id
     * @throws Exception     
     */
    public void removePayAccProfile(String id) throws Exception {
        String sql = "delete from PAY_ACC_PROFILE where ID=?";
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
     * detail PayAccProfile
     * @param id
     * @return PayAccProfile
     * @throws Exception
     */
    public PayAccProfile detailPayAccProfile(String id) throws Exception {
        String sql = "select * from PAY_ACC_PROFILE where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayAccProfileValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayAccProfile
     * @param payAccProfile
     * @throws Exception
     */
    public void updatePayAccProfile(PayAccProfile payAccProfile) throws Exception {
        String sqlTmp = "";
        if(payAccProfile.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payAccProfile.payAcNo != null)sqlTmp = sqlTmp + " PAY_AC_NO=?,";
        if(payAccProfile.acType != null)sqlTmp = sqlTmp + " AC_TYPE=?,";
        if(payAccProfile.brNo != null)sqlTmp = sqlTmp + " BR_NO=?,";
        if(payAccProfile.ccy != null)sqlTmp = sqlTmp + " CCY=?,";
        if(payAccProfile.cashAcBal != null)sqlTmp = sqlTmp + " CASH_AC_BAL=?,";
        if(payAccProfile.consAcBal != null)sqlTmp = sqlTmp + " CONS_AC_BAL=?,";
        if(payAccProfile.minStlBalance != null)sqlTmp = sqlTmp + " MIN_STL_BALANCE=?,";
        if(payAccProfile.frozBalance != null)sqlTmp = sqlTmp + " FROZ_BALANCE=?,";
        if(payAccProfile.glCode != null)sqlTmp = sqlTmp + " GL_CODE=?,";
        if(payAccProfile.acStatus != null)sqlTmp = sqlTmp + " AC_STATUS=?,";
        if(payAccProfile.listStsFlg != null)sqlTmp = sqlTmp + " LIST_STS_FLG=?,";
        if(payAccProfile.resvFlg != null)sqlTmp = sqlTmp + " RESV_FLG=?,";
        if(payAccProfile.accSumNum != null)sqlTmp = sqlTmp + " ACC_SUM_NUM=?,";
        if(payAccProfile.lstTxTime != null)sqlTmp = sqlTmp + " LST_TX_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_ACC_PROFILE "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where ID=?"; 
        log.info(sql);
        log.info(payAccProfile.toString().replaceAll("\n",";"));
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payAccProfile.id != null)ps.setString(n++,payAccProfile.id);
            if(payAccProfile.payAcNo != null)ps.setString(n++,payAccProfile.payAcNo);
            if(payAccProfile.acType != null)ps.setString(n++,payAccProfile.acType);
            if(payAccProfile.brNo != null)ps.setString(n++,payAccProfile.brNo);
            if(payAccProfile.ccy != null)ps.setString(n++,payAccProfile.ccy);
            if(payAccProfile.cashAcBal != null)ps.setLong(n++,payAccProfile.cashAcBal);
            if(payAccProfile.consAcBal != null)ps.setLong(n++,payAccProfile.consAcBal);
            if(payAccProfile.minStlBalance != null)ps.setLong(n++,payAccProfile.minStlBalance);
            if(payAccProfile.frozBalance != null)ps.setLong(n++,payAccProfile.frozBalance);
            if(payAccProfile.glCode != null)ps.setString(n++,payAccProfile.glCode);
            if(payAccProfile.acStatus != null)ps.setString(n++,payAccProfile.acStatus);
            if(payAccProfile.listStsFlg != null)ps.setString(n++,payAccProfile.listStsFlg);
            if(payAccProfile.resvFlg != null)ps.setString(n++,payAccProfile.resvFlg);
            if(payAccProfile.accSumNum != null)ps.setLong(n++,payAccProfile.accSumNum);
            if(payAccProfile.lstTxTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfile.lstTxTime));
            ps.setString(n++,payAccProfile.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 
     * @param payAccProfile
     * @param con
     * @throws Exception
     */
    public void updatePayAccProfile(PayAccProfile payAccProfile,Connection con) throws Exception {
        String sqlTmp = "";
        if(payAccProfile.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payAccProfile.payAcNo != null)sqlTmp = sqlTmp + " PAY_AC_NO=?,";
        if(payAccProfile.acType != null)sqlTmp = sqlTmp + " AC_TYPE=?,";
        if(payAccProfile.brNo != null)sqlTmp = sqlTmp + " BR_NO=?,";
        if(payAccProfile.ccy != null)sqlTmp = sqlTmp + " CCY=?,";
        if(payAccProfile.cashAcBal != null)sqlTmp = sqlTmp + " CASH_AC_BAL=?,";
        if(payAccProfile.consAcBal != null)sqlTmp = sqlTmp + " CONS_AC_BAL=?,";
        if(payAccProfile.minStlBalance != null)sqlTmp = sqlTmp + " MIN_STL_BALANCE=?,";
        if(payAccProfile.frozBalance != null)sqlTmp = sqlTmp + " FROZ_BALANCE=?,";
        if(payAccProfile.glCode != null)sqlTmp = sqlTmp + " GL_CODE=?,";
        if(payAccProfile.acStatus != null)sqlTmp = sqlTmp + " AC_STATUS=?,";
        if(payAccProfile.listStsFlg != null)sqlTmp = sqlTmp + " LIST_STS_FLG=?,";
        if(payAccProfile.resvFlg != null)sqlTmp = sqlTmp + " RESV_FLG=?,";
        if(payAccProfile.accSumNum != null)sqlTmp = sqlTmp + " ACC_SUM_NUM=?,";
        if(payAccProfile.lstTxTime != null)sqlTmp = sqlTmp + " LST_TX_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_ACC_PROFILE "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where ID=?"; 
        log.info(sql);
        log.info(payAccProfile.toString().replaceAll("\n",";"));
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            int n=1;
            if(payAccProfile.id != null)ps.setString(n++,payAccProfile.id);
            if(payAccProfile.payAcNo != null)ps.setString(n++,payAccProfile.payAcNo);
            if(payAccProfile.acType != null)ps.setString(n++,payAccProfile.acType);
            if(payAccProfile.brNo != null)ps.setString(n++,payAccProfile.brNo);
            if(payAccProfile.ccy != null)ps.setString(n++,payAccProfile.ccy);
            if(payAccProfile.cashAcBal != null)ps.setLong(n++,payAccProfile.cashAcBal);
            if(payAccProfile.consAcBal != null)ps.setLong(n++,payAccProfile.consAcBal);
            if(payAccProfile.minStlBalance != null)ps.setLong(n++,payAccProfile.minStlBalance);
            if(payAccProfile.frozBalance != null)ps.setLong(n++,payAccProfile.frozBalance);
            if(payAccProfile.glCode != null)ps.setString(n++,payAccProfile.glCode);
            if(payAccProfile.acStatus != null)ps.setString(n++,payAccProfile.acStatus);
            if(payAccProfile.listStsFlg != null)ps.setString(n++,payAccProfile.listStsFlg);
            if(payAccProfile.resvFlg != null)ps.setString(n++,payAccProfile.resvFlg);
            if(payAccProfile.accSumNum != null)ps.setLong(n++,payAccProfile.accSumNum);
            if(payAccProfile.lstTxTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfile.lstTxTime));
            ps.setString(n++,payAccProfile.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    /**
     * 根据客户类型和客户账号查询该账户余额
     * @param acType
     * @param acNo
     * @return
     * @throws Exception
     */
	public PayAccProfile detailPayAccProfileByAcTypeAndAcNo(String acType,String acNo) throws Exception{
		 String sql = "select * from PAY_ACC_PROFILE where AC_TYPE=? and PAY_AC_NO=? ";
	        log.info(sql);
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            ps.setString(1,acType);
	            ps.setString(2,acNo);
	            rs = ps.executeQuery();
	            if(rs.next())return getPayAccProfileValue(rs); 
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        } finally {
	            close(rs, ps, con);
	        }
	        return null;
	}
    /**
     * update PayAccProfile
     * @param payAccProfile
     * @throws Exception
     */
    public void updatePayAccProfileForSettlement(PayAccProfile payAccProfile) throws Exception {
        String sql = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+?,LST_TX_TIME=sysdate where AC_TYPE=? and PAY_AC_NO=?"; 
        log.info(sql);
        log.info(payAccProfile.toString().replaceAll("\n",";"));
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setLong(n++,payAccProfile.cashAcBal);
            ps.setLong(n++,payAccProfile.consAcBal);
            ps.setString(n++,payAccProfile.acType);
            ps.setString(n++,payAccProfile.payAcNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayAccProfile
     * @param payAccProfile
     * @throws Exception
     */
    public void updatePayAccProfileForNotify(PayAccProfile payAccProfile,PayProductOrder prdOrder) throws Exception {
        String sql = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+?,LST_TX_TIME=sysdate where AC_TYPE=? and PAY_AC_NO=?" +
        		" and (select count(id) from PAY_PRODUCT_ORDER where MERNO=? and PRDORDNO=? and (stlsts!='2' or stlsts is null))>0"; 
        log.info(sql);
        log.info(payAccProfile.toString().replaceAll("\n",";")+";prdordno="+prdOrder.prdordno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setLong(n++,payAccProfile.cashAcBal);
            ps.setLong(n++,payAccProfile.consAcBal);
            ps.setString(n++,payAccProfile.acType);
            ps.setString(n++,payAccProfile.payAcNo);
            ps.setString(n++,payAccProfile.payAcNo);
            ps.setString(n++,prdOrder.prdordno);
            log.info("T+0结算更新条数="+ps.executeUpdate());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayAccProfile
     * @param payAccProfile
     * @throws Exception
     */
    public int updatePayAccProfileForRefund(PayAccProfile payAccProfile) throws Exception {
        String sql = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL-?,CONS_AC_BAL=CONS_AC_BAL-?,LST_TX_TIME=sysdate " +
        		"where CASH_AC_BAL-?>=0 and CONS_AC_BAL-?>=0 and AC_TYPE=? and PAY_AC_NO=?"; 
        log.info(sql);
        log.info(payAccProfile.toString().replaceAll("\n",";"));
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setLong(n++,payAccProfile.cashAcBal);
            ps.setLong(n++,payAccProfile.consAcBal);
            ps.setLong(n++,payAccProfile.cashAcBal);
            ps.setLong(n++,payAccProfile.consAcBal);
            ps.setString(n++,payAccProfile.acType);
            ps.setString(n++,payAccProfile.payAcNo);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayAccProfile
     * @param payAccProfile
     * @throws Exception
     */
    public void updatePayAccChargeForSettlement(PayMerchant mer,long amt) throws Exception {
        String sql = "update PAY_MERCHANT_ROOT set PRE_STORAGE_FEE=PRE_STORAGE_FEE-? where CUST_ID=?";
        log.info(sql);
        log.info("PRE_STORAGE_FEE="+amt+";CUST_ID="+mer.custId);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setLong(n++,amt);
            ps.setString(n++,mer.custId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
}