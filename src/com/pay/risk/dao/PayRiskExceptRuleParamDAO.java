package com.pay.risk.dao;

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
 * Table PAY_RISK_EXCEPT_RULE_PARAM DAO. 
 * @author Administrator
 *
 */
public class PayRiskExceptRuleParamDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRiskExceptRuleParamDAO.class);
    public static synchronized PayRiskExceptRuleParam getPayRiskExceptRuleParamValue(ResultSet rs)throws SQLException {
        PayRiskExceptRuleParam payRiskExceptRuleParam = new PayRiskExceptRuleParam();
        payRiskExceptRuleParam.ruleCode = rs.getString("RULE_CODE");
        payRiskExceptRuleParam.paramId = rs.getString("PARAM_ID");
        payRiskExceptRuleParam.paramName = rs.getString("PARAM_NAME");
        payRiskExceptRuleParam.paramValue = rs.getString("PARAM_VALUE");
        payRiskExceptRuleParam.paramType = rs.getString("PARAM_TYPE");
        return payRiskExceptRuleParam;
    }
    public String addPayRiskExceptRuleParam(PayRiskExceptRuleParam payRiskExceptRuleParam) throws Exception {
        String sql = "insert into PAY_RISK_EXCEPT_RULE_PARAM("+
            "RULE_CODE," + 
            "PARAM_ID," + 
            "PARAM_NAME," + 
            "PARAM_VALUE," + 
            "PARAM_TYPE)values(?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRiskExceptRuleParam.ruleCode);
            ps.setString(n++,payRiskExceptRuleParam.paramId);
            ps.setString(n++,payRiskExceptRuleParam.paramName);
            ps.setString(n++,payRiskExceptRuleParam.paramValue);
            ps.setString(n++,payRiskExceptRuleParam.paramType);
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
        String sql = "select * from PAY_RISK_EXCEPT_RULE_PARAM";
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
                list.add(getPayRiskExceptRuleParamValue(rs));
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
     * @param payRiskExceptRuleParam
     * @return
     */
    private String setPayRiskExceptRuleParamSql(PayRiskExceptRuleParam payRiskExceptRuleParam) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payRiskExceptRuleParam
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRiskExceptRuleParamParameter(PreparedStatement ps,PayRiskExceptRuleParam payRiskExceptRuleParam,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payRiskExceptRuleParam
     * @return
     */
    public int getPayRiskExceptRuleParamCount(PayRiskExceptRuleParam payRiskExceptRuleParam) {
        String sqlCon = setPayRiskExceptRuleParamSql(payRiskExceptRuleParam);
        String sql = "select count(rownum) recordCount from PAY_RISK_EXCEPT_RULE_PARAM " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRiskExceptRuleParamParameter(ps,payRiskExceptRuleParam,n);
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
     * @param payRiskExceptRuleParam
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRiskExceptRuleParamList(PayRiskExceptRuleParam payRiskExceptRuleParam,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayRiskExceptRuleParamSql(payRiskExceptRuleParam);
        String sortOrder = sort == null || sort.length()==0?" order by RULE_CODE desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_RISK_EXCEPT_RULE_PARAM tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayRiskExceptRuleParamParameter(ps,payRiskExceptRuleParam,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskExceptRuleParamValue(rs));
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
     * remove PayRiskExceptRuleParam
     * @param ruleCode
     * @throws Exception     
     */
    public void removePayRiskExceptRuleParam(String ruleCode) throws Exception {
        String sql = "delete from PAY_RISK_EXCEPT_RULE_PARAM where RULE_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,ruleCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayRiskExceptRuleParam
     * @param ruleCode
     * @return PayRiskExceptRuleParam
     * @throws Exception
     */
    public PayRiskExceptRuleParam detailPayRiskExceptRuleParam(String ruleCode) throws Exception {
        String sql = "select * from PAY_RISK_EXCEPT_RULE_PARAM where RULE_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,ruleCode);
            rs = ps.executeQuery();
            if(rs.next())return getPayRiskExceptRuleParamValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayRiskExceptRuleParam
     * @param payRiskExceptRuleParam
     * @throws Exception
     */
    public void updatePayRiskExceptRuleParam(PayRiskExceptRuleParam payRiskExceptRuleParam) throws Exception {
        String sqlTmp = "";
        if(payRiskExceptRuleParam.ruleCode != null)sqlTmp = sqlTmp + " RULE_CODE=?,";
        if(payRiskExceptRuleParam.paramId != null)sqlTmp = sqlTmp + " PARAM_ID=?,";
        if(payRiskExceptRuleParam.paramName != null)sqlTmp = sqlTmp + " PARAM_NAME=?,";
        if(payRiskExceptRuleParam.paramValue != null)sqlTmp = sqlTmp + " PARAM_VALUE=?,";
        if(payRiskExceptRuleParam.paramType != null)sqlTmp = sqlTmp + " PARAM_TYPE=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_RISK_EXCEPT_RULE_PARAM "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where RULE_CODE=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payRiskExceptRuleParam.ruleCode != null)ps.setString(n++,payRiskExceptRuleParam.ruleCode);
            if(payRiskExceptRuleParam.paramId != null)ps.setString(n++,payRiskExceptRuleParam.paramId);
            if(payRiskExceptRuleParam.paramName != null)ps.setString(n++,payRiskExceptRuleParam.paramName);
            if(payRiskExceptRuleParam.paramValue != null)ps.setString(n++,payRiskExceptRuleParam.paramValue);
            if(payRiskExceptRuleParam.paramType != null)ps.setString(n++,payRiskExceptRuleParam.paramType);
            ps.setString(n++,payRiskExceptRuleParam.ruleCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    
    /**
     * 根据规则编号，查询所有规则参数
     * @param ruleCode
     * @return
     * @throws Exception 
     */
	public List<PayRiskExceptRuleParam> detailByPayRiskExceptRule(String ruleCode) throws Exception {
        String sql = "select * from PAY_RISK_EXCEPT_RULE_PARAM where RULE_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PayRiskExceptRuleParam> list = new ArrayList<PayRiskExceptRuleParam>();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            ps.setString(1,ruleCode);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskExceptRuleParamValue(rs));
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