package com.pay.biztype.dao;

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
 * Table PAY_BIZ_TYPE DAO. 
 * @author Administrator
 *
 */
public class PayBizTypeDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayBizTypeDAO.class);
    public static synchronized PayBizType getPayBizTypeValue(ResultSet rs)throws SQLException {
        PayBizType payBizType = new PayBizType();
        payBizType.seqNo = rs.getString("SEQ_NO");
        payBizType.code = rs.getString("CODE");
        payBizType.name = rs.getString("NAME");
        payBizType.isActive = rs.getString("IS_ACTIVE");
        payBizType.isRealname = rs.getString("IS_REALNAME");
        payBizType.remark = rs.getString("REMARK");
        payBizType.creTime = rs.getTimestamp("CRE_TIME");
        payBizType.creOperId = rs.getString("CRE_OPER_ID");
        payBizType.uptTime = rs.getTimestamp("UPT_TIME");
        payBizType.uptOperId = rs.getString("UPT_OPER_ID");
        return payBizType;
    }
    public String addPayBizType(PayBizType payBizType) throws Exception {
        String sql = "insert into PAY_BIZ_TYPE("+
            "SEQ_NO," + 
            "CODE," + 
            "NAME," + 
            "IS_ACTIVE," + 
            "IS_REALNAME," + 
            "REMARK," + 
            "CRE_TIME," + 
            "CRE_OPER_ID," + 
            "UPT_TIME," + 
            "UPT_OPER_ID)values(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?)";
        log.info(sql);
        log.info("SEQ_NO="+payBizType.seqNo+";CODE="+payBizType.code+";NAME="+payBizType.name+";IS_ACTIVE="+payBizType.isActive+
        		";IS_REALNAME="+payBizType.isRealname+";REMARK="+payBizType.remark+";CRE_OPER_ID="+payBizType.creOperId+";UPT_OPER_ID="+payBizType.uptOperId);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payBizType.seqNo);
            ps.setString(n++,payBizType.code);
            ps.setString(n++,payBizType.name);
            ps.setString(n++,payBizType.isActive);
            ps.setString(n++,payBizType.isRealname);
            ps.setString(n++,payBizType.remark);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payBizType.creTime));
            ps.setString(n++,payBizType.creOperId);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payBizType.uptTime));
            ps.setString(n++,payBizType.uptOperId);
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
        String sql = "select * from PAY_BIZ_TYPE where IS_ACTIVE='1'";
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
                list.add(getPayBizTypeValue(rs));
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
     * @param payBizType
     * @return
     */
    private String setPayBizTypeSql(PayBizType payBizType) {
        StringBuffer sql = new StringBuffer();
        
        if(payBizType.isActive != null && payBizType.isActive.length() !=0) {
            sql.append(" IS_ACTIVE = ? and ");
        }
        if(payBizType.name != null && payBizType.name.length() !=0) {
            sql.append(" NAME like ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payBizType
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayBizTypeParameter(PreparedStatement ps,PayBizType payBizType,int n)throws SQLException {
        if(payBizType.isActive != null && payBizType.isActive.length() !=0) {
            ps.setString(n++,payBizType.isActive);
        }
        if(payBizType.name != null && payBizType.name.length() !=0) {
            ps.setString(n++,"%"+payBizType.name+"%");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payBizType
     * @return
     */
    public int getPayBizTypeCount(PayBizType payBizType) {
        String sqlCon = setPayBizTypeSql(payBizType);
        String sql = "select count(rownum) recordCount from PAY_BIZ_TYPE " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayBizTypeParameter(ps,payBizType,n);
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
     * @param payBizType
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayBizTypeList(PayBizType payBizType,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayBizTypeSql(payBizType);
        String sortOrder = sort == null || sort.length()==0?" order by UPT_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_BIZ_TYPE tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayBizTypeParameter(ps,payBizType,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayBizTypeValue(rs));
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
     * detail PayBizType
     * @param seqNo
     * @return PayBizType
     * @throws Exception
     */
    public PayBizType detailPayBizType(String seqNo) throws Exception {
        String sql = "select * from PAY_BIZ_TYPE where SEQ_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,seqNo);
            rs = ps.executeQuery();
            if(rs.next())return getPayBizTypeValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayBizType
     * @param payBizType
     * @throws Exception
     */
    public void updatePayBizType(PayBizType payBizType) throws Exception {
        String sqlTmp = "";
        if(payBizType.name != null)sqlTmp = sqlTmp + " NAME=?,";
        if(payBizType.isActive != null)sqlTmp = sqlTmp + " IS_ACTIVE=?,";
        if(payBizType.isRealname != null)sqlTmp = sqlTmp + " IS_REALNAME=?,";
        if(payBizType.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payBizType.uptTime != null)sqlTmp = sqlTmp + " UPT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payBizType.uptOperId != null)sqlTmp = sqlTmp + " UPT_OPER_ID=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_BIZ_TYPE "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where SEQ_NO=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payBizType.name != null)ps.setString(n++,payBizType.name);
            if(payBizType.isActive != null)ps.setString(n++,payBizType.isActive);
            if(payBizType.isRealname != null)ps.setString(n++,payBizType.isRealname);
            if(payBizType.remark != null)ps.setString(n++,payBizType.remark);
            if(payBizType.uptTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payBizType.uptTime));
            if(payBizType.uptOperId != null)ps.setString(n++,payBizType.uptOperId);
            ps.setString(n++,payBizType.seqNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
}