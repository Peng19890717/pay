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
import com.pay.account.dao.PayBankAccountCheckTmp;
/**
 * Table PAY_RISK_X_LIST DAO. 
 * @author Administrator
 *
 */
public class PayRiskXListDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRiskXListDAO.class);
    public static synchronized PayRiskXList getPayRiskXListValue(ResultSet rs)throws SQLException {
        PayRiskXList payRiskXList = new PayRiskXList();
        payRiskXList.id = rs.getString("ID");
        payRiskXList.clientType = rs.getString("CLIENT_TYPE");
        payRiskXList.clientCode = rs.getString("CLIENT_CODE");
        payRiskXList.xType = rs.getString("X_TYPE");
        payRiskXList.regdtTime = rs.getTimestamp("REGDT_TIME");
        payRiskXList.ruleCode = rs.getString("RULE_CODE");
        payRiskXList.casType = rs.getString("CAS_TYPE");
        payRiskXList.casBuf = rs.getString("CAS_BUF");
        payRiskXList.createUser = rs.getString("CREATE_USER");
        payRiskXList.createDatetime = rs.getTimestamp("CREATE_DATETIME");
        payRiskXList.updateUser = rs.getString("UPDATE_USER");
        payRiskXList.updateDatetime = rs.getTimestamp("UPDATE_DATETIME");
        try {payRiskXList.realName = rs.getString("REAL_NAME");} catch (Exception e) {}
        try {payRiskXList.storeName = rs.getString("STORE_NAME");} catch (Exception e) {}
        try {payRiskXList.ruleName = rs.getString("RULE_NAME");} catch (Exception e) {}
        return payRiskXList;
    }
    
    public List getList() throws Exception{
        String sql = "select * from PAY_RISK_X_LIST";
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
                list.add(getPayRiskXListValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public String addPayRiskXList(PayRiskXList payRiskXList) throws Exception {
        String sql = "insert into PAY_RISK_X_LIST("+
            "ID," + 
            "CLIENT_TYPE," + 
            "CLIENT_CODE," + 
            "X_TYPE," + 
            "REGDT_TIME," + 
            "CAS_BUF," + 
            "CREATE_USER," + 
            "CREATE_DATETIME)values(?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRiskXList.id);
            ps.setString(n++,payRiskXList.clientType);
            ps.setString(n++,payRiskXList.clientCode);
            ps.setString(n++,payRiskXList.xType);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskXList.regdtTime));
            ps.setString(n++,payRiskXList.casBuf);
            ps.setString(n++,payRiskXList.createUser);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskXList.createDatetime));
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * Set query condition sql.
     * @param payRiskXList
     * @return
     */
    private String setPayRiskXListSql(PayRiskXList payRiskXList) {
        StringBuffer sql = new StringBuffer();
        
        if(payRiskXList.clientType != null && payRiskXList.clientType.length() !=0) {
            sql.append(" CLIENT_TYPE = ? and ");
        }
        if(payRiskXList.clientCode != null && payRiskXList.clientCode.length() !=0) {
            sql.append(" CLIENT_CODE = ? and ");
        }
        if(payRiskXList.xType != null && payRiskXList.xType.length() !=0) {
            sql.append(" X_TYPE = ? and ");
        }
        if(payRiskXList.regdtTimeStart != null) {
            sql.append(" REGDT_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payRiskXList.regdtTimeEnd != null) {
        	sql.append(" REGDT_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payRiskXList.ruleCode != null && payRiskXList.ruleCode.length() !=0) {
            sql.append(" RULE_CODE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payRiskXList
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRiskXListParameter(PreparedStatement ps,PayRiskXList payRiskXList,int n)throws SQLException {
        if(payRiskXList.clientType != null && payRiskXList.clientType.length() !=0) {
            ps.setString(n++,payRiskXList.clientType);
        }
        if(payRiskXList.clientCode != null && payRiskXList.clientCode.length() !=0) {
            ps.setString(n++,payRiskXList.clientCode);
        }
        if(payRiskXList.xType != null && payRiskXList.xType.length() !=0) {
            ps.setString(n++,payRiskXList.xType);
        }
        if(payRiskXList.regdtTimeStart != null) {
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskXList.regdtTimeStart)+" 00:00:00");
        }
        if(payRiskXList.regdtTimeEnd != null) {
        	ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskXList.regdtTimeEnd)+" 23:59:59");
        }
        if(payRiskXList.ruleCode != null && payRiskXList.ruleCode.length() !=0) {
            ps.setString(n++,payRiskXList.ruleCode);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payRiskXList
     * @return
     */
    public int getPayRiskXListCount(PayRiskXList payRiskXList) {
        String sqlCon = setPayRiskXListSql(payRiskXList);
        String sql = "select count(rownum) recordCount from PAY_RISK_X_LIST " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRiskXListParameter(ps,payRiskXList,n);
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
     * @param payRiskXList
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRiskXListList(PayRiskXList payRiskXList,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayRiskXListSql(payRiskXList);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "  select tmp.*  from (select xl.*,rule.RULE_NAME,tuser.REAL_NAME,pmer.STORE_NAME from PAY_RISK_X_LIST xl "
                + "left join PAY_RISK_EXCEPT_RULE rule on xl.RULE_CODE=rule.RULE_CODE "
                + "LEFT JOIN PAY_TRAN_USER_INFO tuser ON xl.CLIENT_CODE = tuser.USER_ID "
                + "LEFT JOIN PAY_MERCHANT pmer ON xl.CLIENT_CODE = pmer.CUST_ID"
                + ") tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayRiskXListParameter(ps,payRiskXList,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRiskXListValue(rs));
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
     * detail PayRiskXList
     * @param id
     * @return PayRiskXList
     * @throws Exception
     */
    public PayRiskXList detailPayRiskXList(String id) throws Exception {
        String sql = "select * from PAY_RISK_X_LIST where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayRiskXListValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    
    /**
     * 判断记录是否存在
     * @param id
     * @return PayRiskXList
     * @throws Exception
     */
    public Boolean isExistPayRiskXList(String type,String code) throws Exception {
        String sql = "select * from pay_risk_x_list where CLIENT_TYPE = ? and CLIENT_CODE = ?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,type);
            ps.setString(2,code);
            rs = ps.executeQuery();
            if(rs.next())return true; 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return false;
    }
    
    /**
     * update PayRiskXList
     * @param payRiskXList
     * @throws Exception
     */
    public void updatePayRiskXList(PayRiskXList payRiskXList) throws Exception {
    	String addHistorySql="insert into PAY_RISK_X_LIST_HISTORY(SELECT * FROM PAY_RISK_X_LIST WHERE " +
            	"ID='"+payRiskXList.getId()+"')";
        String sqlTmp = "";
        if(payRiskXList.xType != null)sqlTmp = sqlTmp + " X_TYPE=?,";
        if(payRiskXList.regdtTime != null)sqlTmp = sqlTmp + " REGDT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payRiskXList.casBuf != null)sqlTmp = sqlTmp + " CAS_BUF=?,";
        if(payRiskXList.updateUser != null)sqlTmp = sqlTmp + " UPDATE_USER=?,";
        if(payRiskXList.updateDatetime != null)sqlTmp = sqlTmp + " UPDATE_DATETIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_RISK_X_LIST "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where ID=?"; 
        log.info(addHistorySql);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(addHistorySql);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payRiskXList.xType != null)ps.setString(n++,payRiskXList.xType);
            if(payRiskXList.regdtTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskXList.regdtTime));
            if(payRiskXList.casBuf != null)ps.setString(n++,payRiskXList.casBuf);
            if(payRiskXList.updateUser != null)ps.setString(n++,payRiskXList.updateUser);
            if(payRiskXList.updateDatetime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskXList.updateDatetime));
            ps.setString(n++,payRiskXList.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 取得黑名单
     * @param clientType
     * @param clientNo
     * @return
     */
	public boolean getClientFromXList(String clientType, String clientCode) {
        String sql = "select ID from PAY_RISK_X_LIST where CLIENT_TYPE=? and X_TYPE='3' and CLIENT_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,clientType);
            ps.setString(n++,clientCode);
            rs = ps.executeQuery();
            if(rs.next())return true; 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return false;
	}
    /**
     * 取得名单类别
     * @param clientType
     * @param clientNo
     * @return
     */
	public String getClientTypeFromXList(String clientType, String clientCode) {
        String sql = "select X_TYPE from PAY_RISK_X_LIST where CLIENT_TYPE=? and CLIENT_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,clientType);
            ps.setString(n++,clientCode);
            rs = ps.executeQuery();
            if(rs.next())return rs.getString("X_TYPE"); 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return "1";//默认返回白名单
	}
	/**
	 * 批量导入
	 * @param payRiskXLists
	 */
	public void addPayRiskXListBatch(List<PayRiskXList> payRiskXLists) throws Exception {
		String sql = "insert into PAY_RISK_X_LIST("+
	            "ID," + 
	            "CLIENT_TYPE," + 
	            "CLIENT_CODE," + 
	            "X_TYPE," + 
	            "REGDT_TIME," + 
	            "CAS_BUF," + 
	            "CREATE_DATETIME)values(?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
	        log.info(sql);
	        Connection con = null;
	        PreparedStatement ps = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            for(int i = 0; i<payRiskXLists.size(); i++){
	            	PayRiskXList payRiskXList = (PayRiskXList)payRiskXLists.get(i);
		            int n=1;
		            ps.setString(n++,payRiskXList.id);
		            ps.setString(n++,payRiskXList.clientType);
		            ps.setString(n++,payRiskXList.clientCode);
		            ps.setString(n++,payRiskXList.xType);
		            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskXList.regdtTime));
		            ps.setString(n++,payRiskXList.casBuf);
		            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskXList.createDatetime));
		            ps.addBatch();
	            }
	            ps.executeBatch();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }finally {
	            close(null, ps, con);
	        }
	}
	/**
	 * 检查客户编码是否存在
	 * @param clientCode
	 * @param clientType 
	 * @return
	 */
	public boolean existsClientCode(String clientCode, String clientType)  throws Exception {
		String sql = "select count(*) codeCount from PAY_RISK_X_LIST where CLIENT_CODE = ? and CLIENT_TYPE = ?";
		log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,clientCode);
            ps.setString(n++,clientType);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
            	if(resultSet.getInt("codeCount")>0)
            			return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
		return false;
	}
}