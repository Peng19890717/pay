package com.pay.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
/**
 * Table PAY_SALESMAN_MERCHANT_RELATION DAO. 
 * @author Administrator
 *
 */
public class PaySalesmanMerchantRelationDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PaySalesmanMerchantRelationDAO.class);
    public static synchronized PaySalesmanMerchantRelation getPaySalesmanMerchantRelationValue(ResultSet rs)throws SQLException {
        PaySalesmanMerchantRelation paySalesmanMerchantRelation = new PaySalesmanMerchantRelation();
        paySalesmanMerchantRelation.userId = rs.getString("USER_ID");
        paySalesmanMerchantRelation.merNo = rs.getString("MER_NO");
        try {paySalesmanMerchantRelation.uName = rs.getString("U_NAME");} catch (Exception e) {}
        try {paySalesmanMerchantRelation.mName = rs.getString("M_NAME");} catch (Exception e) {}
        return paySalesmanMerchantRelation;
    }
    public String addPaySalesmanMerchantRelation(PaySalesmanMerchantRelation paySalesmanMerchantRelation) throws Exception {
        String sql = "insert into PAY_SALESMAN_MERCHANT_RELATION("+
            "USER_ID," + 
            "MER_NO)values(?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,paySalesmanMerchantRelation.userId);
            ps.setString(n++,paySalesmanMerchantRelation.merNo);
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
        String sql = "select * from PAY_SALESMAN_MERCHANT_RELATION";
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
                list.add(getPaySalesmanMerchantRelationValue(rs));
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
     * @param paySalesmanMerchantRelation
     * @return
     */
    private String setPaySalesmanMerchantRelationSql(PaySalesmanMerchantRelation paySalesmanMerchantRelation) {
        StringBuffer sql = new StringBuffer();
        
        if(paySalesmanMerchantRelation.userId != null && paySalesmanMerchantRelation.userId.length() !=0) {
            sql.append(" USER_ID = ? and ");
        }
        if(paySalesmanMerchantRelation.merNo != null && paySalesmanMerchantRelation.merNo.length() !=0) {
            sql.append(" MER_NO = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param paySalesmanMerchantRelation
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPaySalesmanMerchantRelationParameter(PreparedStatement ps,PaySalesmanMerchantRelation paySalesmanMerchantRelation,int n)throws SQLException {
        if(paySalesmanMerchantRelation.userId != null && paySalesmanMerchantRelation.userId.length() !=0) {
            ps.setString(n++,paySalesmanMerchantRelation.userId.trim());
        }
        if(paySalesmanMerchantRelation.merNo != null && paySalesmanMerchantRelation.merNo.length() !=0) {
            ps.setString(n++,paySalesmanMerchantRelation.merNo.trim());
        }
        return n;
    }
    /**
     * Get records count.
     * @param paySalesmanMerchantRelation
     * @return
     */
    public int getPaySalesmanMerchantRelationCount(PaySalesmanMerchantRelation paySalesmanMerchantRelation) {
        String sqlCon = setPaySalesmanMerchantRelationSql(paySalesmanMerchantRelation);
        String sql = "select count(rownum) recordCount from PAY_SALESMAN_MERCHANT_RELATION " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPaySalesmanMerchantRelationParameter(ps,paySalesmanMerchantRelation,n);
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
     * @param paySalesmanMerchantRelation
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPaySalesmanMerchantRelationList(PaySalesmanMerchantRelation paySalesmanMerchantRelation,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPaySalesmanMerchantRelationSql(paySalesmanMerchantRelation);
        String sortOrder = sort == null || sort.length()==0?" order by USER_ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select u.NAME U_NAME, mu.STORE_NAME M_NAME,tmp.*  from PAY_SALESMAN_MERCHANT_RELATION tmp " +
                "	left join PAY_JWEB_USER u on u.id = tmp.user_id " +
                "	left join PAY_MERCHANT mu on mu.cust_id = tmp.mer_no " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPaySalesmanMerchantRelationParameter(ps,paySalesmanMerchantRelation,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPaySalesmanMerchantRelationValue(rs));
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
     * remove PaySalesmanMerchantRelation
     * @param userId
     * @throws Exception     
     */
    public void removePaySalesmanMerchantRelation(String merNo) throws Exception {
        String sql = "delete from PAY_SALESMAN_MERCHANT_RELATION where MER_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,merNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PaySalesmanMerchantRelation
     * @param userId
     * @return PaySalesmanMerchantRelation
     * @throws Exception
     */
    public PaySalesmanMerchantRelation detailPaySalesmanMerchantRelation(String userId) throws Exception {
        String sql = "select * from PAY_SALESMAN_MERCHANT_RELATION where USER_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,userId);
            rs = ps.executeQuery();
            if(rs.next())return getPaySalesmanMerchantRelationValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    
	public PaySalesmanMerchantRelation detailPaySalesmanMerchantRelationForMerno(String merNo) throws Exception {
        String sql = "select * from PAY_SALESMAN_MERCHANT_RELATION where MER_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,merNo);
            rs = ps.executeQuery();
            if(rs.next())return getPaySalesmanMerchantRelationValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	/**
	 * 根据用户列表查询商户列表
	 * @param tmpList
	 * @return
	 */
	public List<String> findMerListByUlist(String flag , List<String> tmpList) throws Exception {
		String tmp = "";
		for (int i = 0; i < tmpList.size(); i++) {
			if( i != tmpList.size()-1 ) tmp += "'"+tmpList.get(i)+"',";
			else  tmp += "'"+tmpList.get(i)+"'";
		}
		String sql = "select mer_no from PAY_SALESMAN_MERCHANT_RELATION where USER_ID in (" + tmp + ")";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<String>();
        try {
//        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
//        	else con = connection();
        	con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	list.add(rs.getString("mer_no"));
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
	 * 根据用户编号查询该用户下的商户
	 * @param userId
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	public List getMersByUid(String userId, int page, int rows, String sort,
			String order) throws Exception {
        String sql ="select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select u.NAME U_NAME, mu.STORE_NAME M_NAME,tmp.*  from PAY_SALESMAN_MERCHANT_RELATION tmp " +
                "	left join PAY_JWEB_USER u on u.id = tmp.user_id " +
                "	left join PAY_MERCHANT mu on mu.cust_id = tmp.mer_no where user_id = ? " +
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            ps.setString(1,userId);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPaySalesmanMerchantRelationValue(rs));
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
	 * 根据用户编号获取该用户下的商户个数
	 * @param userId
	 * @return
	 */
	public int getMersCount(String userId) {
        String sql = "select count(rownum) recordCount from PAY_SALESMAN_MERCHANT_RELATION where user_id = ? ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            ps.setString(1,userId);
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
	public void updatePaySalesmanMerchantRelation (
			PaySalesmanMerchantRelation paySalesmanMerchantRelation) throws Exception {
		String sql = "update PAY_SALESMAN_MERCHANT_RELATION set USER_ID=? where MER_NO=? ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,paySalesmanMerchantRelation.userId);
            ps.setString(2,paySalesmanMerchantRelation.merNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	public void deletePaySalesmanMerchantRelation(String custId) throws Exception {
		String sql = "delete from PAY_SALESMAN_MERCHANT_RELATION where MER_NO=? ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,custId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	public List getPaySalesmanRelationList(PaySalesmanMerchantRelation paySalesmanMerchantRelation) throws Exception{
		String sqlCon = setPaySalesmanMerchantRelationSql(paySalesmanMerchantRelation);
		String sql="select sp.* from (select um.id user_id,um.name,um.mer_no,um.store_name,ap.CASH_AC_BAL/100 cash,um.ONLINE_DATE from(  select * from" +
				"(select m.cust_id,m.store_name,to_char (m.check_time,'yyyy-mm-dd') ONLINE_DATE from PAY_SALESMAN_MERCHANT_RELATION s left join PAY_MERCHANT_ROOT m on s.mer_no=m.cust_id) sm left join" +
				"(select u.id,u.name,s.mer_no from PAY_SALESMAN_MERCHANT_RELATION s left join PAY_JWEB_USER u on s.user_id=u.id) su on su.mer_no=sm.cust_id" +
				") um left join PAY_ACC_PROFILE ap on um.mer_no=ap.PAY_AC_NO and ap.AC_TYPE='1') sp " +(sqlCon.length()==0?"":" where sp."+ sqlCon) +"order by sp.user_id desc";
		log.info(sql);
		Connection con=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		try {
			con=connection();
			ps=con.prepareStatement(sql);
			int n=1;
			setPaySalesmanMerchantRelationParameter(ps,paySalesmanMerchantRelation, n);
			rs = ps.executeQuery();			
			ResultSetMetaData md = rs.getMetaData();
			int columnCount = md.getColumnCount(); 			
			while (rs.next()) {
				Map<String, String> rowData = new HashMap<String, String>();
				for (int i = 1;i <= columnCount;i++)rowData.put(md.getColumnName(i),rs.getString(i));
				list.add(rowData);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			close(rs, ps, con);
		}
	}
}