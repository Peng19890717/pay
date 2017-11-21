package com.pay.muser.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import util.SHA1;
import util.Tools;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.jweb.dao.BaseDAO;
/**
 * Table PAY_MERCHANT_USER DAO. 
 * @author Administrator
 *
 */
public class PayMerchantUserDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayMerchantUserDAO.class);
    public static synchronized PayMerchantUser getPayMerchantUserValue(ResultSet rs)throws SQLException {
        PayMerchantUser payMerchantUser = new PayMerchantUser();
        payMerchantUser.userId = rs.getString("USER_ID");
        payMerchantUser.custId = rs.getString("CUST_ID");
        payMerchantUser.userNam = rs.getString("USER_NAM");
        payMerchantUser.userPwd = rs.getString("USER_PWD");
        payMerchantUser.creaSign = rs.getString("CREA_SIGN");
        payMerchantUser.random = rs.getString("RANDOM");
        payMerchantUser.userDate = rs.getTimestamp("USER_DATE");
        payMerchantUser.state = rs.getString("STATE");
        payMerchantUser.descInf = rs.getString("DESC_INF");
        payMerchantUser.tel = rs.getString("TEL");
        payMerchantUser.email = rs.getString("EMAIL");
        payMerchantUser.loginFailCount = rs.getLong("LOGIN_FAIL_COUNT");
        payMerchantUser.currentLoginTime = rs.getTimestamp("CURRENT_LOGIN_TIME");
        payMerchantUser.preset1 = rs.getString("PRESET1");
        payMerchantUser.lastUppwdDate = rs.getTimestamp("LAST_UPPWD_DATE");
        payMerchantUser.userExpiredTime = rs.getLong("USER_EXPIRED_TIME");
        payMerchantUser.mailflg = rs.getString("MAILFLG");
        return payMerchantUser;
    }
    public String addPayMerchantUser(PayMerchantUser payMerchantUser) throws Exception {
        String sql = "insert into PAY_MERCHANT_USER("+
            "USER_ID," + 
            "CUST_ID," + 
            "USER_NAM," + 
            "USER_PWD," + 
            "CREA_SIGN," + 
            "RANDOM," + 
            "USER_DATE," + 
            "STATE," + 
            "DESC_INF," + 
            "TEL," + 
            "EMAIL," + 
            "LOGIN_FAIL_COUNT," + 
            "CURRENT_LOGIN_TIME," + 
            "PRESET1," + 
            "LAST_UPPWD_DATE," + 
            "USER_EXPIRED_TIME," + 
            "MAILFLG)values(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payMerchantUser.userId);
            ps.setString(n++,payMerchantUser.custId);
            ps.setString(n++,payMerchantUser.userNam);
            ps.setString(n++,payMerchantUser.userPwd);
            ps.setString(n++,payMerchantUser.creaSign);
            ps.setString(n++,payMerchantUser.random);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.setString(n++,payMerchantUser.state);
            ps.setString(n++,payMerchantUser.descInf);
            ps.setString(n++,payMerchantUser.tel);
            ps.setString(n++,payMerchantUser.email);
            ps.setLong(n++,payMerchantUser.loginFailCount);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.setString(n++,payMerchantUser.preset1);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.setLong(n++,payMerchantUser.userExpiredTime);
            ps.setString(n++,payMerchantUser.mailflg);
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
        String sql = "select * from PAY_MERCHANT_USER";
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
                list.add(getPayMerchantUserValue(rs));
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
     * @param payMerchantUser
     * @return
     */
    private String setPayMerchantUserSql(PayMerchantUser payMerchantUser) {
        StringBuffer sql = new StringBuffer();
        
        if(payMerchantUser.custId != null && payMerchantUser.custId.length() !=0) {
            sql.append(" CUST_ID like ? and ");
        }
        if(payMerchantUser.userNam != null && payMerchantUser.userNam.length() !=0) {
            sql.append(" USER_NAM like ? and ");
        }
        if(payMerchantUser.tel != null && payMerchantUser.tel.length() !=0) {
            sql.append(" TEL like ? and ");
        }
        if(payMerchantUser.email != null && payMerchantUser.email.length() !=0) {
            sql.append(" EMAIL like ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payMerchantUser
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayMerchantUserParameter(PreparedStatement ps,PayMerchantUser payMerchantUser,int n)throws SQLException {
        if(payMerchantUser.custId != null && payMerchantUser.custId.length() !=0) {
            ps.setString(n++,"%"+payMerchantUser.custId+"%");
        }
        if(payMerchantUser.userNam != null && payMerchantUser.userNam.length() !=0) {
            ps.setString(n++,"%"+payMerchantUser.userNam+"%");
        }
        if(payMerchantUser.tel != null && payMerchantUser.tel.length() !=0) {
            ps.setString(n++,"%"+payMerchantUser.tel+"%");
        }
        if(payMerchantUser.email != null && payMerchantUser.email.length() !=0) {
            ps.setString(n++,"%"+payMerchantUser.email+"%");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payMerchantUser
     * @return
     */
    public int getPayMerchantUserCount(PayMerchantUser payMerchantUser) {
        String sqlCon = setPayMerchantUserSql(payMerchantUser);
        String sql = "select count(rownum) recordCount from PAY_MERCHANT_USER " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayMerchantUserParameter(ps,payMerchantUser,n);
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
     * @param payMerchantUser
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayMerchantUserList(PayMerchantUser payMerchantUser,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayMerchantUserSql(payMerchantUser);
        String sortOrder = sort == null || sort.length()==0?" order by USER_DATE desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_MERCHANT_USER tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayMerchantUserParameter(ps,payMerchantUser,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayMerchantUserValue(rs));
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
     * remove PayMerchantUser
     * @param userId
     * @throws Exception     
     */
    public void removePayMerchantUser(String userId) throws Exception {
        String sql = "delete from PAY_MERCHANT_USER where USER_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayMerchantUser
     * @param userId
     * @return PayMerchantUser
     * @throws Exception
     */
    public PayMerchantUser detailPayMerchantUser(String userId) throws Exception {
        String sql = "select * from PAY_MERCHANT_USER where USER_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,userId);
            rs = ps.executeQuery();
            if(rs.next())return getPayMerchantUserValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayMerchantUser
     * @param payMerchantUser
     * @throws Exception
     */
    public void updatePayMerchantUser(PayMerchantUser payMerchantUser) throws Exception {
        String sqlTmp = "";
        if(payMerchantUser.userId != null)sqlTmp = sqlTmp + " USER_ID=?,";
        if(payMerchantUser.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(payMerchantUser.userNam != null)sqlTmp = sqlTmp + " USER_NAM=?,";
        if(payMerchantUser.userPwd != null)sqlTmp = sqlTmp + " USER_PWD=?,";
        if(payMerchantUser.creaSign != null)sqlTmp = sqlTmp + " CREA_SIGN=?,";
        if(payMerchantUser.random != null)sqlTmp = sqlTmp + " RANDOM=?,";
        if(payMerchantUser.userDate != null)sqlTmp = sqlTmp + " USER_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payMerchantUser.state != null)sqlTmp = sqlTmp + " STATE=?,";
        if(payMerchantUser.descInf != null)sqlTmp = sqlTmp + " DESC_INF=?,";
        if(payMerchantUser.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(payMerchantUser.email != null)sqlTmp = sqlTmp + " EMAIL=?,";
        if(payMerchantUser.loginFailCount != null)sqlTmp = sqlTmp + " LOGIN_FAIL_COUNT=?,";
        if(payMerchantUser.currentLoginTime != null)sqlTmp = sqlTmp + " CURRENT_LOGIN_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payMerchantUser.preset1 != null)sqlTmp = sqlTmp + " PRESET1=?,";
        if(payMerchantUser.lastUppwdDate != null)sqlTmp = sqlTmp + " LAST_UPPWD_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payMerchantUser.userExpiredTime != null)sqlTmp = sqlTmp + " USER_EXPIRED_TIME=?,";
        if(payMerchantUser.mailflg != null)sqlTmp = sqlTmp + " MAILFLG=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_MERCHANT_USER "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where CUST_ID=? and CREA_SIGN='0'"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payMerchantUser.userId != null)ps.setString(n++,payMerchantUser.userId);
            if(payMerchantUser.custId != null)ps.setString(n++,payMerchantUser.custId);
            if(payMerchantUser.userNam != null)ps.setString(n++,payMerchantUser.userNam);
            if(payMerchantUser.userPwd != null)ps.setString(n++,payMerchantUser.userPwd);
            if(payMerchantUser.creaSign != null)ps.setString(n++,payMerchantUser.creaSign);
            if(payMerchantUser.random != null)ps.setString(n++,payMerchantUser.random);
            if(payMerchantUser.userDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.userDate));
            if(payMerchantUser.state != null)ps.setString(n++,payMerchantUser.state);
            if(payMerchantUser.descInf != null)ps.setString(n++,payMerchantUser.descInf);
            if(payMerchantUser.tel != null)ps.setString(n++,payMerchantUser.tel);
            if(payMerchantUser.email != null)ps.setString(n++,payMerchantUser.email);
            if(payMerchantUser.loginFailCount != null)ps.setLong(n++,payMerchantUser.loginFailCount);
            if(payMerchantUser.currentLoginTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.currentLoginTime));
            if(payMerchantUser.preset1 != null)ps.setString(n++,payMerchantUser.preset1);
            if(payMerchantUser.lastUppwdDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.lastUppwdDate));
            if(payMerchantUser.userExpiredTime != null)ps.setLong(n++,payMerchantUser.userExpiredTime);
            if(payMerchantUser.mailflg != null)ps.setString(n++,payMerchantUser.mailflg);
            ps.setString(n++,payMerchantUser.custId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
   /**
    * 同步更新商户修改时关联的经办人手机和经办人邮箱信息。
    * @param payMerchantUser
    * @throws Exception
    */
    public void updatePayMerchantUserForMerchant(PayMerchantUser payMerchantUser) throws Exception {
        String sqlTmp = "";
       
        if(payMerchantUser.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(payMerchantUser.email != null)sqlTmp = sqlTmp + " EMAIL=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_MERCHANT_USER "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where CUST_ID=? and CREA_SIGN='0'"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
          
            if(payMerchantUser.tel != null)ps.setString(n++,payMerchantUser.tel);
            if(payMerchantUser.email != null)ps.setString(n++,payMerchantUser.email);
            ps.setString(n++,payMerchantUser.custId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	/**
     * 更新商户操作员状态信息
     * @param custId 商户id
     * @param columName 列名
     * @param operation 操作
     * @throws Exception 
     */
	public void updatePayMerchantUserStatus(String userId, String columName,String operation) throws Exception {
		String sql = "UPDATE PAY_MERCHANT_USER SET "+columName+" = '"+operation+"' WHERE USER_ID = '"+userId+"'";
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
	
	/**
	 * 根据商户编号，查询商户操作员详情信息
	 * @param custId 查询条件
	 * @return 返回查询结果
	 * @throws Exception 
	 */
	public PayMerchantUser selectByCustId(String custId) throws Exception {
		 String sql = "select * from PAY_MERCHANT_USER where CUST_ID=?";
	        log.info(sql);
	        log.info("CUST_ID="+custId);
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            ps.setString(1,custId);
	            rs = ps.executeQuery();
	            if(rs.next())return getPayMerchantUserValue(rs); 
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        } finally {
	            close(rs, ps, con);
	        }
        return null;
	}
	
	/**
	 * 重置密码
	 * @param payMerchantUser 更新载体
	 * @throws Exception 
	 */
	public void updatePayMerchantUserForResetPwd(PayMerchantUser payMerchantUser) throws Exception {
		String sql = "UPDATE PAY_MERCHANT_USER SET USER_PWD = ? , LAST_UPPWD_DATE = to_date(?,'yyyy-mm-dd hh24:mi:ss')  WHERE CUST_ID = ? " +
				" and USER_ID=?" ;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1 ;
            ps.setString(n++ , payMerchantUser.userPwd);
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payMerchantUser.lastUppwdDate));
            ps.setString(n++ ,payMerchantUser.custId);
            ps.setString(n++ ,payMerchantUser.userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	/**
	 * 通过表的列检查其值是否存在
	 * @param col
	 * @param value
	 * @return
	 */
	public boolean isExistRecordByColumn(String col,String value) {
		String sql = "SELECT USER_ID FROM PAY_MERCHANT_USER  WHERE "+col+" = ? "; 
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
	    try{
	    	con = connection();
	        ps = con.prepareStatement(sql);
	        ps.setString(1, value);
	        rs = ps.executeQuery();
	        if(rs.next()) return true;
	    } catch(Exception e){
	    	e.printStackTrace();
	    } finally{
	    	close(rs, ps, con);
	    }
		return false;
	}
}