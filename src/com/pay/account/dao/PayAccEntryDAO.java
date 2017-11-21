package com.pay.account.dao;

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
 * Table PAY_ACC_ENTRY DAO. 
 * @author Administrator
 *
 */
public class PayAccEntryDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayAccEntryDAO.class);
    public static synchronized PayAccEntry getPayAccEntryValue(ResultSet rs)throws SQLException {
        PayAccEntry payAccEntry = new PayAccEntry();
        payAccEntry.id=rs.getString("ID");
        payAccEntry.txnCod = rs.getString("TXN_COD");
        payAccEntry.txnSubCod = rs.getString("TXN_SUB_COD");
        payAccEntry.accSeq = rs.getString("ACC_SEQ");
        payAccEntry.drCrFlag = rs.getString("DR_CR_FLAG");
        payAccEntry.subjectFrom = rs.getString("SUBJECT_FROM");
        payAccEntry.subject = rs.getString("SUBJECT");
        payAccEntry.accOrgNo = rs.getString("ACC_ORG_NO");
        payAccEntry.rmkCod = rs.getString("RMK_COD");
        payAccEntry.glCode=rs.getString("GL_CODE");
        payAccEntry.glName= rs.getString("GL_NAME");
        return payAccEntry;
    }
    public String addPayAccEntry(PayAccEntry payAccEntry) throws Exception {
        String sql = "insert into PAY_ACC_ENTRY("+
        	"ID,"+
            "TXN_COD," + 
            "TXN_SUB_COD," + 
            "ACC_SEQ," + 
            "DR_CR_FLAG," + 
            "SUBJECT_FROM," + 
            "SUBJECT," + 
            "ACC_ORG_NO," + 
            "RMK_COD)values(?,?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAccEntry.id);
            ps.setString(n++,payAccEntry.txnCod);
            ps.setString(n++,payAccEntry.txnSubCod);
            ps.setString(n++,payAccEntry.accSeq);
            ps.setString(n++,payAccEntry.drCrFlag);
            ps.setString(n++,payAccEntry.subjectFrom);
            ps.setString(n++,payAccEntry.subject);
            ps.setString(n++,payAccEntry.accOrgNo);
            ps.setString(n++,payAccEntry.rmkCod);
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
        String sql = "select * from PAY_ACC_ENTRY";
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
                list.add(getPayAccEntryValue(rs));
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
     * @param payAccEntry
     * @return
     */
    private String setPayAccEntrySql(PayAccEntry payAccEntry) {
        StringBuffer sql = new StringBuffer();
        
        if(payAccEntry.txnCod != null && payAccEntry.txnCod.length() !=0) {
            sql.append(" TXN_COD = ? and ");
        }
        if(payAccEntry.txnSubCod != null && payAccEntry.txnSubCod.length() !=0) {
            sql.append(" TXN_SUB_COD = ? and ");
        }
        if(payAccEntry.accSeq != null && payAccEntry.accSeq.length() !=0) {
            sql.append(" ACC_SEQ = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payAccEntry
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayAccEntryParameter(PreparedStatement ps,PayAccEntry payAccEntry,int n)throws SQLException {
        if(payAccEntry.txnCod != null && payAccEntry.txnCod.length() !=0) {
            ps.setString(n++,payAccEntry.txnCod);
        }
        if(payAccEntry.txnSubCod != null && payAccEntry.txnSubCod.length() !=0) {
            ps.setString(n++,payAccEntry.txnSubCod);
        }
        if(payAccEntry.accSeq != null && payAccEntry.accSeq.length() !=0) {
            ps.setString(n++,payAccEntry.accSeq);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payAccEntry
     * @return
     */
    public int getPayAccEntryCount(PayAccEntry payAccEntry) {
        String sqlCon = setPayAccEntrySql(payAccEntry);
        String sql = "select count(rownum) recordCount from PAY_ACC_ENTRY " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayAccEntryParameter(ps,payAccEntry,n);
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
     * @param payAccEntry
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayAccEntryList(PayAccEntry payAccEntry,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayAccEntrySql(payAccEntry);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*,tmp2.GL_CODE,tmp2.GL_NAME from PAY_ACC_ENTRY tmp left join PAY_ACC_SUBJECT tmp2 on tmp.SUBJECT=tmp2.GL_CODE " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayAccEntryParameter(ps,payAccEntry,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayAccEntryValue(rs));
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
     * remove PayAccEntry
     * @param txnCod
     * @throws Exception     
     */
    public void removePayAccEntry(String ID) throws Exception {
        String sql = "delete from PAY_ACC_ENTRY where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,ID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayAccEntry
     * @param txnCod
     * @return PayAccEntry
     * @throws Exception
     */
    public PayAccEntry detailPayAccEntry(String id) throws Exception {
        String sql = "select tmp1.*,TMP2.GL_CODE,TMP2.GL_NAME from PAY_ACC_ENTRY tmp1 left join PAY_ACC_SUBJECT tmp2 on TMP1.SUBJECT=TMP2.GL_CODE where TMP1.ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayAccEntryValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayAccEntry
     * @param payAccEntry
     * @throws Exception
     */
    public void updatePayAccEntry(PayAccEntry payAccEntry) throws Exception {
        String sqlTmp = "";
        if(payAccEntry.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payAccEntry.txnCod != null)sqlTmp = sqlTmp + " TXN_COD=?,";
        if(payAccEntry.txnSubCod != null)sqlTmp = sqlTmp + " TXN_SUB_COD=?,";
        if(payAccEntry.accSeq != null)sqlTmp = sqlTmp + " ACC_SEQ=?,";
        if(payAccEntry.drCrFlag != null)sqlTmp = sqlTmp + " DR_CR_FLAG=?,";
        if(payAccEntry.subjectFrom != null)sqlTmp = sqlTmp + " SUBJECT_FROM=?,";
        if(payAccEntry.subject != null)sqlTmp = sqlTmp + " SUBJECT=?,";
        if(payAccEntry.accOrgNo != null)sqlTmp = sqlTmp + " ACC_ORG_NO=?,";
        if(payAccEntry.rmkCod != null)sqlTmp = sqlTmp + " RMK_COD=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_ACC_ENTRY "+        
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
            if(payAccEntry.id != null)ps.setString(n++,payAccEntry.id);
            if(payAccEntry.txnCod != null)ps.setString(n++,payAccEntry.txnCod);
            if(payAccEntry.txnSubCod != null)ps.setString(n++,payAccEntry.txnSubCod);
            if(payAccEntry.accSeq != null)ps.setString(n++,payAccEntry.accSeq);
            if(payAccEntry.drCrFlag != null)ps.setString(n++,payAccEntry.drCrFlag);
            if(payAccEntry.subjectFrom != null)ps.setString(n++,payAccEntry.subjectFrom);
            if(payAccEntry.subject != null)ps.setString(n++,payAccEntry.subject);
            if(payAccEntry.accOrgNo != null)ps.setString(n++,payAccEntry.accOrgNo);
            if(payAccEntry.rmkCod != null)ps.setString(n++,payAccEntry.rmkCod);
            ps.setString(n++,payAccEntry.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    //用于获取科目信息表中的数据。
    public List<SimplePayAccSubject> getPayAccSUBJECT() throws Exception {
    	
    	List<SimplePayAccSubject> list = new ArrayList<SimplePayAccSubject>();
        String sql = "select GL_CODE,GL_NAME from PAY_ACC_SUBJECT";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
            	SimplePayAccSubject simplePayAccSubject = new SimplePayAccSubject();
            	simplePayAccSubject.setGlCode(rs.getString("GL_CODE"));
            	simplePayAccSubject.setGlName(rs.getString("GL_NAME"));
                list.add(simplePayAccSubject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
        return list;
    }
  
    public boolean isRepeat(PayAccEntry payAccEntry) throws Exception {
    	
    	boolean flag=false;
        String sql = "select * from PAY_ACC_ENTRY where TXN_COD=? and TXN_SUB_COD=? and ACC_SEQ=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, payAccEntry.txnCod);
            ps.setString(2, payAccEntry.txnSubCod);
            ps.setString(3, payAccEntry.accSeq);
            rs=ps.executeQuery();
            flag=rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
        return flag;
    }
}