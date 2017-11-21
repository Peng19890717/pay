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
 * Table PAY_RISK_X_LIST_HISTORY DAO. 
 * @author Administrator
 *
 */
public class PayRiskXListHistoryDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRiskXListHistoryDAO.class);
    public static synchronized PayRiskXListHistory getPayRiskXListHistoryValue(ResultSet rs)throws SQLException {
        PayRiskXListHistory payRiskXListHistory = new PayRiskXListHistory();
        payRiskXListHistory.id = rs.getString("ID");
        payRiskXListHistory.clientType = rs.getString("CLIENT_TYPE");
        payRiskXListHistory.clientCode = rs.getString("CLIENT_CODE");
        payRiskXListHistory.xType = rs.getString("X_TYPE");
        payRiskXListHistory.regdtTime = rs.getTimestamp("REGDT_TIME");
        payRiskXListHistory.ruleCode = rs.getString("RULE_CODE");
        payRiskXListHistory.casType = rs.getString("CAS_TYPE");
        payRiskXListHistory.casBuf = rs.getString("CAS_BUF");
        payRiskXListHistory.createUser = rs.getString("CREATE_USER");
        payRiskXListHistory.createDatetime = rs.getTimestamp("CREATE_DATETIME");
        payRiskXListHistory.updateUser = rs.getString("UPDATE_USER");
        payRiskXListHistory.updateDatetime = rs.getTimestamp("UPDATE_DATETIME");
        return payRiskXListHistory;
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_RISK_X_LIST_HISTORY";
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
                list.add(getPayRiskXListHistoryValue(rs));
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
     * @param payRiskXListHistory
     * @return
     */
    private String setPayRiskXListHistorySql(PayRiskXListHistory payRiskXListHistory) {
        StringBuffer sql = new StringBuffer();
        
        if(payRiskXListHistory.clientType != null && payRiskXListHistory.clientType.length() !=0) {
            sql.append(" CLIENT_TYPE = ? and ");
        }
        if(payRiskXListHistory.clientCode != null && payRiskXListHistory.clientCode.length() !=0) {
            sql.append(" CLIENT_CODE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payRiskXListHistory
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRiskXListHistoryParameter(PreparedStatement ps,PayRiskXListHistory payRiskXListHistory,int n)throws SQLException {
        if(payRiskXListHistory.clientType != null && payRiskXListHistory.clientType.length() !=0) {
            ps.setString(n++,payRiskXListHistory.clientType);
        }
        if(payRiskXListHistory.clientCode != null && payRiskXListHistory.clientCode.length() !=0) {
            ps.setString(n++,payRiskXListHistory.clientCode);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payRiskXListHistory
     * @return
     */
    public int getPayRiskXListHistoryCount(PayRiskXListHistory payRiskXListHistory) {
        String sqlCon = setPayRiskXListHistorySql(payRiskXListHistory);
        String sql = "select count(rownum) recordCount from PAY_RISK_X_LIST_HISTORY " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRiskXListHistoryParameter(ps,payRiskXListHistory,n);
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
     * @param payRiskXListHistory
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRiskXListHistoryList(PayRiskXListHistory payRiskXListHistory,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayRiskXListHistorySql(payRiskXListHistory);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_RISK_X_LIST_HISTORY tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayRiskXListHistoryParameter(ps,payRiskXListHistory,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskXListHistoryValue(rs));
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