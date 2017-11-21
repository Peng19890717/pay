package com.pay.complain.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_CUST_COMPLAIN DAO. 
 * @author Administrator
 *
 */
public class PayCustComplainDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayCustComplainDAO.class);
    public static synchronized PayCustComplain getPayCustComplainValue(ResultSet rs)throws SQLException {
        PayCustComplain payCustComplain = new PayCustComplain();
        payCustComplain.id = rs.getString("ID");
        payCustComplain.type = rs.getString("TYPE");
        payCustComplain.custId = rs.getString("CUST_ID");
        try {payCustComplain.storeName = rs.getString("store_name");} catch (Exception e) {}
        payCustComplain.transAmt = rs.getLong("TRANS_AMT");
        payCustComplain.frozenAmt = rs.getLong("FROZEN_AMT");
        payCustComplain.optStatus = rs.getString("OPT_STATUS");
        payCustComplain.name = rs.getString("NAME");
        payCustComplain.tel = rs.getString("TEL");
        payCustComplain.channel = rs.getString("CHANNEL");
        payCustComplain.startFile = rs.getString("START_FILE");
        payCustComplain.endFile = rs.getString("END_FILE");
        payCustComplain.orderIds = rs.getString("ORDER_IDS");
        payCustComplain.cContent = rs.getString("C_CONTENT");
        payCustComplain.cTime = rs.getTimestamp("C_TIME");
        payCustComplain.createTime = rs.getTimestamp("CREATE_TIME");
        payCustComplain.optTime = rs.getTimestamp("OPT_TIME");
        payCustComplain.remark = rs.getString("REMARK");
        payCustComplain.deductAmt = rs.getLong("DEDUCT_AMT");
        payCustComplain.accFrozenId = rs.getString("ACC_FROZEN_ID");
        return payCustComplain;
    }
    public String addPayCustComplain(PayCustComplain payCustComplain) throws Exception {
        String sql = "insert into PAY_CUST_COMPLAIN("+
            "ID," + 
            "TYPE," + 
            "CUST_ID," + 
            "TRANS_AMT," + 
            "FROZEN_AMT," + 
            "OPT_STATUS," + 
            "NAME," + 
            "TEL," + 
            "CHANNEL," + 
            "START_FILE," + 
            "END_FILE," + 
            "ORDER_IDS," + 
            "C_CONTENT," + 
            "OPT_TIME," + 
            "REMARK," + 
            "DEDUCT_AMT,ACC_FROZEN_ID,ACC_ADJUST_ID)values(?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payCustComplain.id);
            ps.setString(n++,payCustComplain.type);
            ps.setString(n++,payCustComplain.custId);
            ps.setLong(n++,payCustComplain.transAmt);
            ps.setLong(n++,payCustComplain.frozenAmt);
            ps.setString(n++,payCustComplain.optStatus);
            ps.setString(n++,payCustComplain.name);
            ps.setString(n++,payCustComplain.tel);
            ps.setString(n++,payCustComplain.channel);
            ps.setString(n++,payCustComplain.startFile);
            ps.setString(n++,payCustComplain.endFile);
            ps.setString(n++,payCustComplain.orderIds);
            ps.setString(n++,payCustComplain.cContent);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payCustComplain.optTime));
            ps.setString(n++,payCustComplain.remark);
            ps.setLong(n++,payCustComplain.deductAmt);
            ps.setString(n++,payCustComplain.accFrozenId);
            ps.setString(n++,payCustComplain.accAdjustId);
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
        String sql = "select * from PAY_CUST_COMPLAIN";
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
                list.add(getPayCustComplainValue(rs));
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
     * @param payCustComplain
     * @return
     */
    private String setPayCustComplainSql(PayCustComplain payCustComplain) {
        StringBuffer sql = new StringBuffer();
        if(payCustComplain.id != null && payCustComplain.id.length() !=0) {
            sql.append(" ID = ? and ");
        }
        if(payCustComplain.type != null && payCustComplain.type.length() !=0) {
            sql.append(" TYPE = ? and ");
        }
        if(payCustComplain.custId != null && payCustComplain.custId.length() !=0) {
            sql.append(" CUST_ID = ? and ");
        }
        if(payCustComplain.optStatus != null && payCustComplain.optStatus.length() !=0) {
            sql.append(" OPT_STATUS = ? and ");
        }
        if(payCustComplain.name != null && payCustComplain.name.length() !=0) {
            sql.append(" NAME = ? and ");
        }
        if(payCustComplain.tel != null && payCustComplain.tel.length() !=0) {
            sql.append(" TEL = ? and ");
        }
        if(payCustComplain.channel != null && payCustComplain.channel.length() !=0) {
            sql.append(" CHANNEL = ? and ");
        }
        if(payCustComplain.cTimeStart != null && payCustComplain.cTimeStart.length()!=0) {
            sql.append(" C_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payCustComplain.cTimeEnd != null && payCustComplain.cTimeEnd.length()!=0) {
            sql.append(" C_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payCustComplain
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayCustComplainParameter(PreparedStatement ps,PayCustComplain payCustComplain,int n)throws SQLException {
    	if(payCustComplain.id != null && payCustComplain.id.length() !=0) {
            ps.setString(n++,payCustComplain.id);
        }
        if(payCustComplain.type != null && payCustComplain.type.length() !=0) {
            ps.setString(n++,payCustComplain.type);
        }
        if(payCustComplain.custId != null && payCustComplain.custId.length() !=0) {
            ps.setString(n++,payCustComplain.custId);
        }
        if(payCustComplain.optStatus != null && payCustComplain.optStatus.length() !=0) {
            ps.setString(n++,payCustComplain.optStatus);
        }
        if(payCustComplain.name != null && payCustComplain.name.length() !=0) {
            ps.setString(n++,payCustComplain.name);
        }
        if(payCustComplain.tel != null && payCustComplain.tel.length() !=0) {
            ps.setString(n++,payCustComplain.tel);
        }
        if(payCustComplain.channel != null && payCustComplain.channel.length() !=0) {
            ps.setString(n++,payCustComplain.channel);
        }
        if(payCustComplain.cTimeStart != null && payCustComplain.cTimeStart.length()!=0) {
        	ps.setString(n++, payCustComplain.cTimeStart+" 00:00:00");
        }
        if(payCustComplain.cTimeEnd != null && payCustComplain.cTimeEnd.length()!=0) {
        	ps.setString(n++, payCustComplain.cTimeEnd+" 23:59:59");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payCustComplain
     * @return
     */
    public int getPayCustComplainCount(PayCustComplain payCustComplain) {
        String sqlCon = setPayCustComplainSql(payCustComplain);
        String sql = "select count(rownum) recordCount from PAY_CUST_COMPLAIN " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayCustComplainParameter(ps,payCustComplain,n);
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
     * @param payCustComplain
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List<PayCustComplain> getPayCustComplainList(PayCustComplain payCustComplain,int page,int rows,String sort,String order) throws Exception{
        String sqlCon = setPayCustComplainSql(payCustComplain);
        String sql = "select tmp.*,m.store_name from(select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_CUST_COMPLAIN tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +" order by C_TIME desc "+
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + " order by C_TIME desc)tmp left join pay_merchant_root m on tmp.cust_id=m.cust_id";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayCustComplainParameter(ps,payCustComplain,n);
            rs = ps.executeQuery();
            while(rs.next())list.add(getPayCustComplainValue(rs));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * detail PayCustComplain
     * @param id
     * @return PayCustComplain
     * @throws Exception
     */
    public PayCustComplain detailPayCustComplain(String id) throws Exception {
        String sql = "select * from PAY_CUST_COMPLAIN where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayCustComplainValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayCustComplain
     * @param payCustComplain
     * @throws Exception
     */
    public void updatePayCustComplain(PayCustComplain payCustComplain) throws Exception {
        String sqlTmp = "";
        if(payCustComplain.type != null)sqlTmp = sqlTmp + " TYPE=?,";
        if(payCustComplain.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(payCustComplain.frozenAmt != null)sqlTmp = sqlTmp + " FROZEN_AMT=?,";
        if(payCustComplain.optStatus != null)sqlTmp = sqlTmp + " OPT_STATUS=?,";
        if(payCustComplain.name != null)sqlTmp = sqlTmp + " NAME=?,";
        if(payCustComplain.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(payCustComplain.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payCustComplain.deductAmt != null)sqlTmp = sqlTmp + " DEDUCT_AMT=?,";
        if(payCustComplain.accAdjustId != null)sqlTmp = sqlTmp + " ACC_ADJUST_ID=?,";
        if(payCustComplain.endFile != null&&payCustComplain.endFile.length()>0)sqlTmp = sqlTmp + " END_FILE=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_CUST_COMPLAIN "+        
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
            if(payCustComplain.type != null)ps.setString(n++,payCustComplain.type);
            if(payCustComplain.custId != null)ps.setString(n++,payCustComplain.custId);
            if(payCustComplain.frozenAmt != null)ps.setLong(n++,payCustComplain.frozenAmt);
            if(payCustComplain.optStatus != null)ps.setString(n++,payCustComplain.optStatus);
            if(payCustComplain.name != null)ps.setString(n++,payCustComplain.name);
            if(payCustComplain.tel != null)ps.setString(n++,payCustComplain.tel);
            if(payCustComplain.remark != null)ps.setString(n++,payCustComplain.remark);
            if(payCustComplain.deductAmt != null)ps.setLong(n++,payCustComplain.deductAmt);
            if(payCustComplain.accAdjustId != null)ps.setString(n++,payCustComplain.accAdjustId);
            if(payCustComplain.endFile != null&&payCustComplain.endFile.length()>0)ps.setString(n++,payCustComplain.endFile);
            ps.setString(n++,payCustComplain.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }

}