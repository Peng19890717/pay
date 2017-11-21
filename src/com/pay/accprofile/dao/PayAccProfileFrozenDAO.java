package com.pay.accprofile.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
/**
 * Table PAY_ACC_PROFILE_FROZEN DAO. 
 * @author Administrator
 *
 */
public class PayAccProfileFrozenDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayAccProfileFrozenDAO.class);
    public static synchronized PayAccProfileFrozen getPayAccProfileFrozenValue(ResultSet rs)throws SQLException {
        PayAccProfileFrozen payAccProfileFrozen = new PayAccProfileFrozen();
        payAccProfileFrozen.id = rs.getString("ID");
        payAccProfileFrozen.accType = rs.getString("ACC_TYPE");
        payAccProfileFrozen.accNo = rs.getString("ACC_NO");
        payAccProfileFrozen.amt = rs.getLong("AMT");
        payAccProfileFrozen.curAmt = rs.getLong("CUR_AMT");
        payAccProfileFrozen.beginTime = rs.getTimestamp("BEGIN_TIME");
        payAccProfileFrozen.endTime = rs.getTimestamp("END_TIME");
        payAccProfileFrozen.createTime = rs.getTimestamp("CREATE_TIME");
        payAccProfileFrozen.status = rs.getString("STATUS");
        payAccProfileFrozen.optUser = rs.getString("OPT_USER");
        payAccProfileFrozen.updateTime = rs.getTimestamp("UPDATE_TIME");
        payAccProfileFrozen.remark = rs.getString("REMARK");
        return payAccProfileFrozen;
    }
    public String addPayAccProfileFrozen(PayAccProfileFrozen payAccProfileFrozen, Connection con) throws Exception {
        String sql = "insert into PAY_ACC_PROFILE_FROZEN("+
            "ID," + 
            "ACC_TYPE," + 
            "ACC_NO," + 
            "AMT," + 
            "CUR_AMT," + 
            "BEGIN_TIME," + 
            "END_TIME," + 
            "STATUS," + 
            "OPT_USER," + 
            "CREATE_TIME," + 
            "UPDATE_TIME," + 
            "REMARK)values(?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
        log.info(sql);
        log.info("ID="+payAccProfileFrozen.id+";ACC_TYPE="+payAccProfileFrozen.accType+";ACC_NO="+payAccProfileFrozen.accNo+";AMT="+payAccProfileFrozen.amt+
        		";CUR_AMT="+payAccProfileFrozen.curAmt+";STATUS="+payAccProfileFrozen.status+";OPT_USER="+payAccProfileFrozen.optUser+";REMARK="+payAccProfileFrozen.remark);
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAccProfileFrozen.id);
            ps.setString(n++,payAccProfileFrozen.accType);
            ps.setString(n++,payAccProfileFrozen.accNo);
            ps.setLong(n++,payAccProfileFrozen.amt);
            ps.setLong(n++,payAccProfileFrozen.curAmt);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.setString(n++,payAccProfileFrozen.endTime==null? "":new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileFrozen.endTime));
            ps.setString(n++,payAccProfileFrozen.status);
            ps.setString(n++,payAccProfileFrozen.optUser);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.setString(n++,payAccProfileFrozen.updateTime==null? "":new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileFrozen.updateTime));
            ps.setString(n++,payAccProfileFrozen.remark);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public List getList() throws Exception{
        String sql = "select * from PAY_ACC_PROFILE_FROZEN";
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
                list.add(getPayAccProfileFrozenValue(rs));
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
     * @param payAccProfileFrozen
     * @return
     */
    private String setPayAccProfileFrozenSql(PayAccProfileFrozen payAccProfileFrozen) {
        StringBuffer sql = new StringBuffer();
        
        if(payAccProfileFrozen.accType != null && payAccProfileFrozen.accType.length() !=0) {
            sql.append(" ACC_TYPE = ? and ");
        }
        if(payAccProfileFrozen.accNo != null && payAccProfileFrozen.accNo.length() !=0) {
            sql.append(" ACC_NO = ? and ");
        }
        if(payAccProfileFrozen.createTimeStart != null && payAccProfileFrozen.createTimeStart.length() !=0) {
        	sql.append(" CREATE_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and  ");
        }
        if(payAccProfileFrozen.createTimeEnd != null && payAccProfileFrozen.createTimeEnd.length() !=0) {
        	sql.append(" CREATE_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and  ");
        }
        if(payAccProfileFrozen.status != null && payAccProfileFrozen.status.length() !=0) {
            sql.append(" STATUS = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payAccProfileFrozen
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayAccProfileFrozenParameter(PreparedStatement ps,PayAccProfileFrozen payAccProfileFrozen,int n)throws SQLException {
        if(payAccProfileFrozen.accType != null && payAccProfileFrozen.accType.length() !=0) {
            ps.setString(n++,payAccProfileFrozen.accType);
        }
        if(payAccProfileFrozen.accNo != null && payAccProfileFrozen.accNo.length() !=0) {
            ps.setString(n++,payAccProfileFrozen.accNo);
        }
        if(payAccProfileFrozen.createTime != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileFrozen.createTime));
        }
        if(payAccProfileFrozen.createTimeStart != null && payAccProfileFrozen.createTimeStart.length() !=0) {
        	ps.setString(n++, payAccProfileFrozen.createTimeStart+" 00:00:00");
        }
        if(payAccProfileFrozen.createTimeEnd != null && payAccProfileFrozen.createTimeEnd.length() !=0 ) {
        	ps.setString(n++, payAccProfileFrozen.createTimeEnd+" 23:59:59");
        }
        if(payAccProfileFrozen.status != null && payAccProfileFrozen.status.length() !=0) {
            ps.setString(n++,payAccProfileFrozen.status);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payAccProfileFrozen
     * @return
     */
    public int getPayAccProfileFrozenCount(PayAccProfileFrozen payAccProfileFrozen) {
        String sqlCon = setPayAccProfileFrozenSql(payAccProfileFrozen);
        String sql = "select count(rownum) recordCount from PAY_ACC_PROFILE_FROZEN " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayAccProfileFrozenParameter(ps,payAccProfileFrozen,n);
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
     * @param payAccProfileFrozen
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayAccProfileFrozenList(PayAccProfileFrozen payAccProfileFrozen,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayAccProfileFrozenSql(payAccProfileFrozen);
        String sortOrder = sort == null || sort.length()==0?" order by CREATE_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_ACC_PROFILE_FROZEN tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PayAccProfileFrozen> list = new ArrayList<PayAccProfileFrozen>();
        List<String> ids = new ArrayList<String> ();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayAccProfileFrozenParameter(ps,payAccProfileFrozen,n);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayAccProfileFrozen fro = getPayAccProfileFrozenValue(rs); 
                list.add(fro);
                ids.add(fro.accNo);
            }
            Map<String,PayMerchant> merMap = new PayMerchantDAO().getMerchantesByCustIds(ids);
            for(int i=0; i<list.size(); i++)list.get(i).storeName=merMap.get(list.get(i).accNo).storeName;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * 更改资金状态
     * @param accNo
     * @param columName
     * @param operation
     * @throws Exception
     */
	public void updatePayAccProfileFrozenStatus(String accNo, String columName,String operation) throws Exception {
		String sql = "UPDATE PAY_ACC_PROFILE_FROZEN SET "+columName+" = '"+operation+"',BEGIN_TIME = sysdate WHERE ACC_NO = '"+accNo+"'";
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
	 * 根据Id查询
	 * @param id
	 * @return
	 * @throws Exception
	 */
	  public PayAccProfileFrozen detailPayAccProfileFrozen(String id) throws Exception {
	        String sql = "SELECT * FROM PAY_ACC_PROFILE_FROZEN WHERE ID = ?";
	        log.info(sql);
	        Connection con = null;
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        try {
	            con = connection();
	            ps = con.prepareStatement(sql);
	            ps.setString(1,id);
	            rs = ps.executeQuery();
	            if(rs.next())return getPayAccProfileFrozenValue(rs); 
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        } finally {
	            close(rs, ps, con);
	        }
	        return null;
	    }
	  /**
	     * update PayAccProfileFrozen
	     * @param payAccProfileFrozen
	     * @throws Exception
	     */
	    public void updatePayAccProfileFrozen(PayAccProfileFrozen payAccProfileFrozen) throws Exception {
	        String sqlTmp = "";
	        if(payAccProfileFrozen.id != null)sqlTmp = sqlTmp + " ID=?,";
	        if(payAccProfileFrozen.accType != null)sqlTmp = sqlTmp + " ACC_TYPE=?,";
	        if(payAccProfileFrozen.accNo != null)sqlTmp = sqlTmp + " ACC_NO=?,";
	        if(payAccProfileFrozen.amt != null)sqlTmp = sqlTmp + " AMT=?,";
	        if(payAccProfileFrozen.curAmt != null)sqlTmp = sqlTmp + " CUR_AMT=?,";
	        if(payAccProfileFrozen.beginTime != null)sqlTmp = sqlTmp + " BEGIN_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
	        if(payAccProfileFrozen.endTime != null)sqlTmp = sqlTmp + " END_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
	        if(payAccProfileFrozen.status != null)sqlTmp = sqlTmp + " STATUS=?,";
	        if(payAccProfileFrozen.optUser != null)sqlTmp = sqlTmp + " OPT_USER=?,";
	        if(payAccProfileFrozen.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
	        if(payAccProfileFrozen.updateTime != null)sqlTmp = sqlTmp + " UPDATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
	        if(payAccProfileFrozen.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
	        if(sqlTmp.length()==0)return;
	        String sql = "update PAY_ACC_PROFILE_FROZEN "+        
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
	            if(payAccProfileFrozen.id != null)ps.setString(n++,payAccProfileFrozen.id);
	            if(payAccProfileFrozen.accType != null)ps.setString(n++,payAccProfileFrozen.accType);
	            if(payAccProfileFrozen.accNo != null)ps.setString(n++,payAccProfileFrozen.accNo);
	            if(payAccProfileFrozen.amt != null)ps.setLong(n++,payAccProfileFrozen.amt);
	            if(payAccProfileFrozen.curAmt != null)ps.setLong(n++,payAccProfileFrozen.curAmt);
	            if(payAccProfileFrozen.beginTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileFrozen.beginTime));
	            if(payAccProfileFrozen.endTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileFrozen.endTime));
	            if(payAccProfileFrozen.status != null)ps.setString(n++,payAccProfileFrozen.status);
	            if(payAccProfileFrozen.optUser != null)ps.setString(n++,payAccProfileFrozen.optUser);
	            if(payAccProfileFrozen.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileFrozen.createTime));
	            if(payAccProfileFrozen.updateTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfileFrozen.updateTime));
	            if(payAccProfileFrozen.remark != null)ps.setString(n++,payAccProfileFrozen.remark);
	            ps.setString(n++,payAccProfileFrozen.id);
	            ps.executeUpdate();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw e;
	        }finally {
	            close(null, ps, con);
	        }
	    }
	/**
	 * 获取冻结总金额：初始冻结金额+当前冻结金额
	 * @param payAccProfileFrozen
	 * @return
	 * @throws Exception 
	 */
	public Long getTotalFrozenMoney(PayAccProfileFrozen payAccProfileFrozen) throws Exception {
		Long l0 = 0l;
		String sqlCon = setPayAccProfileFrozenSql(payAccProfileFrozen);
		String sql = "select sum(CUR_AMT) totalFrozenMoney " 
				+ " FROM" 
                + " (SELECT rownum rowno,tmp1.*"
                + " FROM "
                + " (SELECT tmp.* FROM PAY_ACC_PROFILE_FROZEN tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +") tmp1)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayAccProfileFrozenParameter(ps,payAccProfileFrozen,n);
            rs = ps.executeQuery();
            if (rs.next()) l0 = rs.getLong("totalFrozenMoney");
            return l0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
	}
}