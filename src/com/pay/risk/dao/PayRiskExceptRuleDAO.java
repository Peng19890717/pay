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
 * Table PAY_RISK_EXCEPT_RULE DAO. 
 * @author Administrator
 *
 */
public class PayRiskExceptRuleDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRiskExceptRuleDAO.class);
    public static synchronized PayRiskExceptRule getPayRiskExceptRuleValue(ResultSet rs)throws SQLException {
        PayRiskExceptRule payRiskExceptRule = new PayRiskExceptRule();
        payRiskExceptRule.ruleCode = rs.getString("RULE_CODE");
        payRiskExceptRule.ruleName = rs.getString("RULE_NAME");
        payRiskExceptRule.ruleType = rs.getString("RULE_TYPE");
        payRiskExceptRule.isOnline = rs.getString("IS_ONLINE");
        payRiskExceptRule.excpType = rs.getString("EXCP_TYPE");
        payRiskExceptRule.comTypeNo = rs.getString("COM_TYPE_NO");
        payRiskExceptRule.ruleDes = rs.getString("RULE_DES");
        payRiskExceptRule.ruleLevel = rs.getString("RULE_LEVEL");
        payRiskExceptRule.ruleLevelItem = rs.getString("RULE_LEVEL_ITEM");
        payRiskExceptRule.ruleStartDate = rs.getTimestamp("RULE_START_DATE");
        payRiskExceptRule.ruleEndDate = rs.getTimestamp("RULE_END_DATE");
        payRiskExceptRule.createName = rs.getString("CREATE_NAME");
        payRiskExceptRule.createTime = rs.getTimestamp("CREATE_TIME");
        payRiskExceptRule.updateName = rs.getString("UPDATE_NAME");
        payRiskExceptRule.updateTime = rs.getTimestamp("UPDATE_TIME");
        payRiskExceptRule.ruleVer = rs.getString("RULE_VER");
        payRiskExceptRule.isUse = rs.getString("IS_USE");
        payRiskExceptRule.belongCompany = rs.getString("BELONG_COMPANY");
        return payRiskExceptRule;
    }
    public String addPayRiskExceptRule(PayRiskExceptRule payRiskExceptRule) throws Exception {
        String sql = "insert into PAY_RISK_EXCEPT_RULE("+
            "RULE_CODE," + 
            "RULE_NAME," + 
            "RULE_TYPE," + 
            "IS_ONLINE," + 
            "EXCP_TYPE," + 
            "COM_TYPE_NO," + 
            "RULE_DES," + 
            "RULE_LEVEL," + 
            "RULE_LEVEL_ITEM," + 
            (payRiskExceptRule.ruleStartDate == null?"":"RULE_START_DATE,") + 
            (payRiskExceptRule.ruleEndDate == null?"":"RULE_END_DATE,") + 
            "CREATE_NAME," + 
            "CREATE_TIME," + 
            "UPDATE_NAME," + 
            "UPDATE_TIME," + 
            "RULE_VER," + 
            "IS_USE," + 
            "BELONG_COMPANY)values(?,?,?,?,?,?,?,?,?," +
            (payRiskExceptRule.ruleStartDate == null?"":"to_date(?,'yyyy-mm-dd hh24:mi:ss'),") +
            (payRiskExceptRule.ruleEndDate == null?"":"to_date(?,'yyyy-mm-dd hh24:mi:ss'),") +
            "?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRiskExceptRule.ruleCode);
            ps.setString(n++,payRiskExceptRule.ruleName);
            ps.setString(n++,payRiskExceptRule.ruleType);
            ps.setString(n++,payRiskExceptRule.isOnline);
            ps.setString(n++,payRiskExceptRule.excpType);
            ps.setString(n++,payRiskExceptRule.comTypeNo);
            ps.setString(n++,payRiskExceptRule.ruleDes);
            ps.setString(n++,payRiskExceptRule.ruleLevel);
            ps.setString(n++,payRiskExceptRule.ruleLevelItem);
            if(payRiskExceptRule.ruleStartDate!=null){
            	ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.ruleStartDate));
            }
            if(payRiskExceptRule.ruleEndDate!=null){
            	ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.ruleEndDate));
            }
            ps.setString(n++,payRiskExceptRule.createName);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.createTime));
            ps.setString(n++,payRiskExceptRule.updateName);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.updateTime));
            ps.setString(n++,payRiskExceptRule.ruleVer);
            ps.setString(n++,payRiskExceptRule.isUse);
            ps.setString(n++,payRiskExceptRule.belongCompany);
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
        String sql = "select * from PAY_RISK_EXCEPT_RULE";
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
                list.add(getPayRiskExceptRuleValue(rs));
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
     * @param payRiskExceptRule
     * @return
     */
    private String setPayRiskExceptRuleSql(PayRiskExceptRule payRiskExceptRule) {
        StringBuffer sql = new StringBuffer();
        
        if(payRiskExceptRule.ruleName != null && payRiskExceptRule.ruleName.length() !=0) {
            sql.append(" RULE_NAME like ? and ");
        }
        if(payRiskExceptRule.ruleType != null && payRiskExceptRule.ruleType.length() !=0) {
            sql.append(" RULE_TYPE = ? and ");
        }
        if(payRiskExceptRule.excpType != null && payRiskExceptRule.excpType.length() !=0) {
            sql.append(" EXCP_TYPE = ? and ");
        }
        if(payRiskExceptRule.comTypeNo != null && payRiskExceptRule.comTypeNo.length() !=0) {
            sql.append(" COM_TYPE_NO = ? and ");
        }
        if(payRiskExceptRule.ruleLevelItem != null && payRiskExceptRule.ruleLevelItem.length() !=0) {
            sql.append(" RULE_LEVEL_ITEM = ? and ");
        }
        if(payRiskExceptRule.isUse != null && payRiskExceptRule.isUse.length() !=0) {
            sql.append(" IS_USE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payRiskExceptRule
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRiskExceptRuleParameter(PreparedStatement ps,PayRiskExceptRule payRiskExceptRule,int n)throws SQLException {
        if(payRiskExceptRule.ruleName != null && payRiskExceptRule.ruleName.length() !=0) {
            ps.setString(n++,"%"+payRiskExceptRule.ruleName+"%");
        }
        if(payRiskExceptRule.ruleType != null && payRiskExceptRule.ruleType.length() !=0) {
            ps.setString(n++,payRiskExceptRule.ruleType);
        }
        if(payRiskExceptRule.excpType != null && payRiskExceptRule.excpType.length() !=0) {
            ps.setString(n++,payRiskExceptRule.excpType);
        }
        if(payRiskExceptRule.comTypeNo != null && payRiskExceptRule.comTypeNo.length() !=0) {
            ps.setString(n++,payRiskExceptRule.comTypeNo);
        }
        if(payRiskExceptRule.ruleLevelItem != null && payRiskExceptRule.ruleLevelItem.length() !=0) {
            ps.setString(n++,payRiskExceptRule.ruleLevelItem);
        }
        if(payRiskExceptRule.isUse != null && payRiskExceptRule.isUse.length() !=0) {
            ps.setString(n++,payRiskExceptRule.isUse);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payRiskExceptRule
     * @return
     */
    public int getPayRiskExceptRuleCount(PayRiskExceptRule payRiskExceptRule) {
        String sqlCon = setPayRiskExceptRuleSql(payRiskExceptRule);
        String sql = "select count(rownum) recordCount from PAY_RISK_EXCEPT_RULE " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRiskExceptRuleParameter(ps,payRiskExceptRule,n);
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
     * @param payRiskExceptRule
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRiskExceptRuleList(PayRiskExceptRule payRiskExceptRule,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayRiskExceptRuleSql(payRiskExceptRule);
        String sortOrder = sort == null || sort.length()==0?" order by RULE_CODE desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_RISK_EXCEPT_RULE tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayRiskExceptRuleParameter(ps,payRiskExceptRule,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskExceptRuleValue(rs));
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
     * remove PayRiskExceptRule
     * @param ruleCode
     * @throws Exception     
     */
    public void removePayRiskExceptRule(String ruleCode) throws Exception {
        String sql = "delete from PAY_RISK_EXCEPT_RULE where RULE_CODE=?";
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
     * detail PayRiskExceptRule
     * @param ruleCode
     * @return PayRiskExceptRule
     * @throws Exception
     */
    public PayRiskExceptRule detailPayRiskExceptRule(String ruleCode) throws Exception {
        String sql = "select * from PAY_RISK_EXCEPT_RULE where RULE_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,ruleCode);
            rs = ps.executeQuery();
            if(rs.next())return getPayRiskExceptRuleValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayRiskExceptRule
     * @param payRiskExceptRule
     * @throws Exception
     */
    public void updatePayRiskExceptRule(PayRiskExceptRule payRiskExceptRule) throws Exception {
        String sqlTmp = "";
        if(payRiskExceptRule.ruleCode != null)sqlTmp = sqlTmp + " RULE_CODE=?,";
        if(payRiskExceptRule.ruleName != null)sqlTmp = sqlTmp + " RULE_NAME=?,";
        if(payRiskExceptRule.ruleType != null)sqlTmp = sqlTmp + " RULE_TYPE=?,";
        if(payRiskExceptRule.isOnline != null)sqlTmp = sqlTmp + " IS_ONLINE=?,";
        if(payRiskExceptRule.excpType != null)sqlTmp = sqlTmp + " EXCP_TYPE=?,";
        if(payRiskExceptRule.comTypeNo != null)sqlTmp = sqlTmp + " COM_TYPE_NO=?,";
        if(payRiskExceptRule.ruleDes != null)sqlTmp = sqlTmp + " RULE_DES=?,";
        if(payRiskExceptRule.ruleLevel != null)sqlTmp = sqlTmp + " RULE_LEVEL=?,";
        if(payRiskExceptRule.ruleLevelItem != null)sqlTmp = sqlTmp + " RULE_LEVEL_ITEM=?,";
        if(payRiskExceptRule.ruleStartDate != null)sqlTmp = sqlTmp + " RULE_START_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskExceptRule.ruleEndDate != null)sqlTmp = sqlTmp + " RULE_END_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskExceptRule.createName != null)sqlTmp = sqlTmp + " CREATE_NAME=?,";
        if(payRiskExceptRule.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskExceptRule.updateName != null)sqlTmp = sqlTmp + " UPDATE_NAME=?,";
        if(payRiskExceptRule.updateTime != null)sqlTmp = sqlTmp + " UPDATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskExceptRule.ruleVer != null)sqlTmp = sqlTmp + " RULE_VER=?,";
        if(payRiskExceptRule.isUse != null)sqlTmp = sqlTmp + " IS_USE=?,";
        if(payRiskExceptRule.belongCompany != null)sqlTmp = sqlTmp + " BELONG_COMPANY=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_RISK_EXCEPT_RULE "+        
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
            if(payRiskExceptRule.ruleCode != null)ps.setString(n++,payRiskExceptRule.ruleCode);
            if(payRiskExceptRule.ruleName != null)ps.setString(n++,payRiskExceptRule.ruleName);
            if(payRiskExceptRule.ruleType != null)ps.setString(n++,payRiskExceptRule.ruleType);
            if(payRiskExceptRule.isOnline != null)ps.setString(n++,payRiskExceptRule.isOnline);
            if(payRiskExceptRule.excpType != null)ps.setString(n++,payRiskExceptRule.excpType);
            if(payRiskExceptRule.comTypeNo != null)ps.setString(n++,payRiskExceptRule.comTypeNo);
            if(payRiskExceptRule.ruleDes != null)ps.setString(n++,payRiskExceptRule.ruleDes);
            if(payRiskExceptRule.ruleLevel != null)ps.setString(n++,payRiskExceptRule.ruleLevel);
            if(payRiskExceptRule.ruleLevelItem != null)ps.setString(n++,payRiskExceptRule.ruleLevelItem);
            if(payRiskExceptRule.ruleStartDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.ruleStartDate));
            if(payRiskExceptRule.ruleEndDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.ruleEndDate));
            if(payRiskExceptRule.createName != null)ps.setString(n++,payRiskExceptRule.createName);
            if(payRiskExceptRule.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.createTime));
            if(payRiskExceptRule.updateName != null)ps.setString(n++,payRiskExceptRule.updateName);
            if(payRiskExceptRule.updateTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.updateTime));
            if(payRiskExceptRule.ruleVer != null)ps.setString(n++,payRiskExceptRule.ruleVer);
            if(payRiskExceptRule.isUse != null)ps.setString(n++,payRiskExceptRule.isUse);
            if(payRiskExceptRule.belongCompany != null)ps.setString(n++,payRiskExceptRule.belongCompany);
            ps.setString(n++,payRiskExceptRule.ruleCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    
    /**
     * 修改规则状态
     * @param ruleCode
     * @param columName
     * @param opration
     * @throws Exception 
     */
	public void updatePayRiskExceptRuleStatus(String ruleCode,String columName, String operation) throws Exception {
		String sql = "UPDATE PAY_RISK_EXCEPT_RULE SET "+columName+" = '"+operation+"' WHERE RULE_CODE = '"+ruleCode+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}

}