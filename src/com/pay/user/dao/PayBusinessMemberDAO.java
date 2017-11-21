package com.pay.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.jweb.dao.JWebUser;
import com.jweb.dao.JWebUserDAO;
/**
 * Table PAY_BUSINESS_MEMBER DAO. 
 * @author Administrator
 *
 */
public class PayBusinessMemberDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayBusinessMemberDAO.class);
    public static synchronized PayBusinessMember getPayBusinessMemberValue(ResultSet rs)throws SQLException {
        PayBusinessMember payBusinessMember = new PayBusinessMember();
        payBusinessMember.userId = rs.getString("USER_ID");
        payBusinessMember.parentId = rs.getString("PARENT_ID");
        try {payBusinessMember.realName = rs.getString("NAME");} catch (Exception e) {}
        return payBusinessMember;
    }
    /**
     * 查询已关联的业务员
     * @return
     * @throws Exception
     */
    public PayBusinessMember getBusinessMemberById(String userId) throws Exception{
        String sql = "select * from PAY_BUSINESS_MEMBER where USER_ID = ?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            rs = ps.executeQuery();
            if(rs.next()) return getPayBusinessMemberValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * 查询已关联的业务员
     * @return
     * @throws Exception
     */
    public List<PayBusinessMember> getList(String flag) throws Exception{
        String sql = "select * from PAY_BUSINESS_MEMBER a left join PAY_JWEB_USER b on a.USER_ID = b.ID";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PayBusinessMember> list = new ArrayList<PayBusinessMember>();
        try {
//        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
//        	else con = connection();
        	con = connection();
        	
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayBusinessMemberValue(rs));
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
     * 查询没有被关联的业务员
     * @return
     * @throws Exception
     */
	public List<PayBusinessMember> getBusiMemWithoutRelation() throws Exception{
		 String sql = "select tr.id,tr.name FROM PAY_JWEB_USER tr"
		 		+ " WHERE id NOT IN"
		 		+ " (SELECT a.user_id FROM PAY_BUSINESS_MEMBER a LEFT JOIN PAY_JWEB_USER b ON a.USER_ID = b.ID)";
	        log.info(sql);
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        List<PayBusinessMember> list = new ArrayList<PayBusinessMember>();
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            rs = ps.executeQuery();
	            while(rs.next()){
	            	PayBusinessMember payBusinessMember = new PayBusinessMember();
	            	payBusinessMember.userId = rs.getString("ID");
	            	payBusinessMember.realName = rs.getString("NAME");
	            	list.add(payBusinessMember);
	            }
	            return list;
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        } finally {
	            close(rs, ps, con);
	        }
	}
	public void addPayBusiMem(PayBusinessMember payBusinessMember) throws Exception{
		String sql = "insert into PAY_BUSINESS_MEMBER("+
	            "USER_ID," + 
	            "PARENT_ID)values(?,?)";
	        log.info(sql);
	        Connection con = null;
	        PreparedStatement ps = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            int n=1;
	            ps.setString(n++,payBusinessMember.userId);
	            ps.setString(n++,payBusinessMember.parentId);
	            ps.executeUpdate();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }finally {
	            close(null, ps, con);
	        }
	}
	public void removeBusiMem(String userId) throws Exception{
		String sql = "DELETE FROM pay_business_member WHERE user_id IN "+
	            " (SELECT user_id FROM pay_business_member WHERE user_id = ? OR parent_id = ?) ";
	        log.info(sql);
	        Connection con = null;
	        PreparedStatement ps = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            int n=1;
	            ps.setString(n++,userId);
	            ps.setString(n++,userId);
	            ps.executeUpdate();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }finally {
	            close(null, ps, con);
	        }
	}
	/**
	 * 删除该节点列表
	 * @param tmpList
	 * @throws Exception
	 */
	public void removeBusiMem(List<String> tmpList) throws Exception {
		String sql = " delete from pay_business_member where user_id in ";
		String tmp = "";
		if(tmpList.size() == 1)tmp += "('"+tmpList.get(0)+"')";
		else{
			for(int i =0 ;i<tmpList.size() ;i++){
				if(i == 0){
					tmp += "('"+tmpList.get(i)+"',";
				}else if(i == tmpList.size()-1){
					tmp += "'"+tmpList.get(i)+"')";
				}else{
					tmp += "'"+tmpList.get(i)+"',";
				}
			}
		}
		sql = sql + tmp;
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
        }finally {
            close(null, ps, con);
        }
    }
	
	public JWebUser selectJWebUserForUserId(String userId) throws Exception {
        String sql = "select * from PAY_JWEB_USER where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,userId);
            rs = ps.executeQuery();
            if(rs.next())return JWebUserDAO.getJwebUserValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
	public long validPayMentRelationUserId(String userId)  throws Exception {
		String sql = "select count(*) uCount from PAY_BUSINESS_MEMBER where user_id = ?";
		log.info(sql);
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,userId);
            rs = ps.executeQuery();
            if(rs.next()){
            	if(rs.getLong("uCount") == 0)throw new Exception("业务员不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
		return 0;
	}
}