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
 * Table PAY_CUST_FEE DAO. 
 * @author Administrator
 *
 */
public class PayCustFeeDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayCustFeeDAO.class);
    public static synchronized PayCustFee getPayCustFeeValue(ResultSet rs)throws SQLException {
        PayCustFee payCustFee = new PayCustFee();
        payCustFee.id = rs.getString("ID");
        payCustFee.custType = rs.getString("CUST_TYPE");
        payCustFee.custId = rs.getString("CUST_ID");
        payCustFee.tranType = rs.getString("TRAN_TYPE");
        payCustFee.feeCode = rs.getString("FEE_CODE");
        payCustFee.createTime = rs.getTimestamp("CREATE_TIME");
        return payCustFee;
    }
    public String addPayCustFee(PayCustFee payCustFee) throws Exception {
        String sql = "insert into PAY_CUST_FEE("+
            "ID," + 
            "CUST_TYPE," + 
            "CUST_ID," + 
            "TRAN_TYPE," + 
            "FEE_CODE," + 
            "CREATE_TIME)values(?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payCustFee.id);
            ps.setString(n++,payCustFee.custType);
            ps.setString(n++,payCustFee.custId);
            ps.setString(n++,payCustFee.tranType);
            ps.setString(n++,payCustFee.feeCode);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCustFee.createTime));
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
        String sql = "select * from PAY_CUST_FEE";
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
                list.add(getPayCustFeeValue(rs));
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
     * @param payCustFee
     * @return
     */
    private String setPayCustFeeSql(PayCustFee payCustFee) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payCustFee
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayCustFeeParameter(PreparedStatement ps,PayCustFee payCustFee,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payCustFee
     * @return
     */
    public int getPayCustFeeCount(PayCustFee payCustFee) {
        String sqlCon = setPayCustFeeSql(payCustFee);
        String sql = "select count(rownum) recordCount from PAY_CUST_FEE " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayCustFeeParameter(ps,payCustFee,n);
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
     * @param payCustFee
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayCustFeeList(PayCustFee payCustFee,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayCustFeeSql(payCustFee);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_CUST_FEE tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayCustFeeParameter(ps,payCustFee,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayCustFeeValue(rs));
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
     * remove PayCustFee
     * @param id
     * @throws Exception     
     */
    public void removePayCustFee(String id) throws Exception {
        String sql = "delete from PAY_CUST_FEE where ID=?";
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
     * detail PayCustFee
     * @param id
     * @return PayCustFee
     * @throws Exception
     */
    public PayCustFee detailPayCustFee(String id) throws Exception {
        String sql = "select * from PAY_CUST_FEE where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayCustFeeValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayCustFee
     * @param payCustFee
     * @throws Exception
     */
    public void updatePayCustFee(PayCustFee payCustFee) throws Exception {
        String sqlTmp = "";
        if(payCustFee.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payCustFee.custType != null)sqlTmp = sqlTmp + " CUST_TYPE=?,";
        if(payCustFee.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(payCustFee.tranType != null)sqlTmp = sqlTmp + " TRAN_TYPE=?,";
        if(payCustFee.feeCode != null)sqlTmp = sqlTmp + " FEE_CODE=?,";
        if(payCustFee.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_CUST_FEE "+        
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
            if(payCustFee.id != null)ps.setString(n++,payCustFee.id);
            if(payCustFee.custType != null)ps.setString(n++,payCustFee.custType);
            if(payCustFee.custId != null)ps.setString(n++,payCustFee.custId);
            if(payCustFee.tranType != null)ps.setString(n++,payCustFee.tranType);
            if(payCustFee.feeCode != null)ps.setString(n++,payCustFee.feeCode);
            if(payCustFee.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCustFee.createTime));
            ps.setString(n++,payCustFee.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    
    /**
     * 根据客户ID删除早期的客户费率设置
     * @param custId
     * @return
     * @throws Exception 
     */
	public int removePayCustFeeForCustId(String custId) throws Exception {
		String sql = "delete from PAY_CUST_FEE where CUST_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,custId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
}