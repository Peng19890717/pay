package com.pay.business.dao;

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
 * Table PAY_BUSINESS_PARAMETER DAO. 
 * @author Administrator
 *
 */
public class PayBusinessParameterDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayBusinessParameterDAO.class);
    public static synchronized PayBusinessParameter getPayBusinessParameterValue(ResultSet rs)throws SQLException {
        PayBusinessParameter payBusinessParameter = new PayBusinessParameter();
        payBusinessParameter.name = rs.getString("NAME");
        payBusinessParameter.value = rs.getString("VALUE");
        payBusinessParameter.remark = rs.getString("REMARK");
        return payBusinessParameter;
    }
    public String addPayBusinessParameter(PayBusinessParameter payBusinessParameter) throws Exception {
        String sql = "insert into PAY_BUSINESS_PARAMETER("+
            "NAME," + 
            "VALUE," + 
            "REMARK)values(?,?,?)";
        log.info(sql);
        log.info("NAME="+payBusinessParameter.name+";VALUE="+payBusinessParameter.value+";REMARK="+payBusinessParameter.remark);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payBusinessParameter.name);
            ps.setString(n++,payBusinessParameter.value);
            ps.setString(n++,payBusinessParameter.remark);
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
        String sql = "select * from PAY_BUSINESS_PARAMETER";
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
                list.add(getPayBusinessParameterValue(rs));
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
     * @param payBusinessParameter
     * @return
     */
    private String setPayBusinessParameterSql(PayBusinessParameter payBusinessParameter) {
        StringBuffer sql = new StringBuffer();
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payBusinessParameter
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayBusinessParameterParameter(PreparedStatement ps,PayBusinessParameter payBusinessParameter,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payBusinessParameter
     * @return
     */
    public int getPayBusinessParameterCount(PayBusinessParameter payBusinessParameter) {
        String sqlCon = setPayBusinessParameterSql(payBusinessParameter);
        String sql = "select count(rownum) recordCount from PAY_BUSINESS_PARAMETER " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayBusinessParameterParameter(ps,payBusinessParameter,n);
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
     * @param payBusinessParameter
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayBusinessParameterList(PayBusinessParameter payBusinessParameter,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayBusinessParameterSql(payBusinessParameter);
        String sortOrder = sort == null || sort.length()==0?" order by NAME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_BUSINESS_PARAMETER tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayBusinessParameterParameter(ps,payBusinessParameter,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayBusinessParameterValue(rs));
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
     * remove PayBusinessParameter
     * @param name
     * @throws Exception     
     */
    public void removePayBusinessParameter(String name) throws Exception {
        String sql = "delete from PAY_BUSINESS_PARAMETER where NAME=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,name);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayBusinessParameter
     * @param name
     * @return PayBusinessParameter
     * @throws Exception
     */
    public PayBusinessParameter detailPayBusinessParameter(String name) throws Exception {
        String sql = "select * from PAY_BUSINESS_PARAMETER where NAME=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,name);
            rs = ps.executeQuery();
            if(rs.next())return getPayBusinessParameterValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayBusinessParameter
     * @param payBusinessParameter
     * @throws Exception
     */
    public void updatePayBusinessParameter(PayBusinessParameter payBusinessParameter) throws Exception {
        String sqlTmp = "";
        if(payBusinessParameter.name != null)sqlTmp = sqlTmp + " NAME=?,";
        if(payBusinessParameter.value != null)sqlTmp = sqlTmp + " VALUE=?,";
        if(payBusinessParameter.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_BUSINESS_PARAMETER "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where NAME=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payBusinessParameter.name != null)ps.setString(n++,payBusinessParameter.name);
            if(payBusinessParameter.value != null)ps.setString(n++,payBusinessParameter.value);
            if(payBusinessParameter.remark != null)ps.setString(n++,payBusinessParameter.remark);
            ps.setString(n++,payBusinessParameter.name);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 判断添加的业务参数是否已存在
     * @param name
     * @return
     */
	public boolean existName(String name) {
		 String sql = "select NAME from PAY_BUSINESS_PARAMETER where NAME = ?" ;
	        log.info(sql);
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            ps.setString(1, name);
	            rs = ps.executeQuery();
	            if (rs.next()) {
	            	return true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            close(rs, ps, con);
	        }
	        return false;
	}
}