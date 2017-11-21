package com.pay.merchant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.pay.order.dao.PayOrder;
/**
 * Table PAY_YAKU_STL_ACC DAO. 
 * @author Administrator
 *
 */
public class PayYakuStlAccDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayYakuStlAccDAO.class);
    public static synchronized PayYakuStlAcc getPayYakuStlAccValue(ResultSet rs)throws SQLException {
        PayYakuStlAcc payYakuStlAcc = new PayYakuStlAcc();
        payYakuStlAcc.merno = rs.getString("MERNO");
        payYakuStlAcc.accName = rs.getString("ACC_NAME");
        payYakuStlAcc.bankCode = rs.getString("BANK_CODE");
        payYakuStlAcc.bankBranchName = rs.getString("BANK_BRANCH_NAME");
        try {
        	payYakuStlAcc.accNo = rs.getString("ACC_NO");
		} catch (Exception e) {
			e.printStackTrace();
		}
        payYakuStlAcc.bankBranchCode = rs.getString("BANK_BRANCH_CODE");
        payYakuStlAcc.province = rs.getString("PROVINCE");
        payYakuStlAcc.city = rs.getString("CITY");
        payYakuStlAcc.credentialNo = rs.getString("CREDENTIAL_NO");
        payYakuStlAcc.tel = rs.getString("TEL");
        try {
        	payYakuStlAcc.storeName = rs.getString("STORE_NAME");
		} catch (Exception e) {
			e.printStackTrace();
		}
        return payYakuStlAcc;
    }
    public static synchronized PayYakuStlAccCache getPayYakuStlAccCacheValue(ResultSet rs)throws SQLException {
        PayYakuStlAccCache payYakuStlAccCache = new PayYakuStlAccCache();
        payYakuStlAccCache.merno = rs.getString("MERNO");
        payYakuStlAccCache.accNo = rs.getString("ACC_NO");
        payYakuStlAccCache.amt = rs.getLong("AMT");
        payYakuStlAccCache.stlDate = rs.getString("STL_DATE");
        return payYakuStlAccCache;
    }
    public String addPayYakuStlAcc(PayYakuStlAcc payYakuStlAcc) throws Exception {
        String sql = "insert into PAY_YAKU_STL_ACC("+
            "MERNO," + 
            "ACC_NAME," + 
            "BANK_CODE," + 
            "BANK_BRANCH_NAME," + 
            "ACC_NO," + 
            "BANK_BRANCH_CODE," + 
            "PROVINCE," + 
            "CITY," + 
            "CREDENTIAL_NO," + 
            "TEL)values(?,?,?,?,?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payYakuStlAcc.merno);
            ps.setString(n++,payYakuStlAcc.accName);
            ps.setString(n++,payYakuStlAcc.bankCode);
            ps.setString(n++,payYakuStlAcc.bankBranchName);
            ps.setString(n++,payYakuStlAcc.accNo);
            ps.setString(n++,payYakuStlAcc.bankBranchCode);
            ps.setString(n++,payYakuStlAcc.province);
            ps.setString(n++,payYakuStlAcc.city);
            ps.setString(n++,payYakuStlAcc.credentialNo);
            ps.setString(n++,payYakuStlAcc.tel);
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
        String sql = "select * from PAY_YAKU_STL_ACC";
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
                list.add(getPayYakuStlAccValue(rs));
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
     * @param payYakuStlAcc
     * @return
     */
    private String setPayYakuStlAccSql(PayYakuStlAcc payYakuStlAcc) {
        StringBuffer sql = new StringBuffer();
        
        if(payYakuStlAcc.merno != null && payYakuStlAcc.merno.length() !=0) {
            sql.append(" MERNO = ? and ");
        }
        if(payYakuStlAcc.accName != null && payYakuStlAcc.accName.length() !=0) {
            sql.append(" ACC_NAME like ? and ");
        }
        if(payYakuStlAcc.accNo != null && payYakuStlAcc.accNo.length() !=0) {
            sql.append(" ACC_NO = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payYakuStlAcc
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayYakuStlAccParameter(PreparedStatement ps,PayYakuStlAcc payYakuStlAcc,int n)throws SQLException {
        if(payYakuStlAcc.merno != null && payYakuStlAcc.merno.length() !=0) {
            ps.setString(n++,payYakuStlAcc.merno);
        }
        if(payYakuStlAcc.accName != null && payYakuStlAcc.accName.length() !=0) {
            ps.setString(n++,"%"+payYakuStlAcc.accName+"%");
        }
        if(payYakuStlAcc.accNo != null && payYakuStlAcc.accNo.length() !=0) {
            ps.setString(n++,payYakuStlAcc.accNo);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payYakuStlAcc
     * @return
     */
    public int getPayYakuStlAccCount(PayYakuStlAcc payYakuStlAcc) {
        String sqlCon = setPayYakuStlAccSql(payYakuStlAcc);
        String sql = "select count(rownum) recordCount from PAY_YAKU_STL_ACC a left join pay_merchant_root b on a.merno = b.cust_id " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayYakuStlAccParameter(ps,payYakuStlAcc,n);
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
     * @param payYakuStlAcc
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayYakuStlAccList(PayYakuStlAcc payYakuStlAcc,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayYakuStlAccSql(payYakuStlAcc);
        String sortOrder = sort == null || sort.length()==0?" order by MERNO desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
        		"  SELECT a.*,b.store_name FROM PAY_YAKU_STL_ACC a left join pay_merchant_root b on a.merno = b.cust_id "+(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayYakuStlAccParameter(ps,payYakuStlAcc,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayYakuStlAccValue(rs));
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
     * remove PayYakuStlAcc
     * @param merno
     * @throws Exception     
     */
    public void removePayYakuStlAcc(String accNo) throws Exception {
        String sql = "delete from PAY_YAKU_STL_ACC where ACC_NO =?";
        log.info(sql);
        log.info("ACC_NO="+accNo);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,accNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * detail PayYakuStlAcc
     * @param merno
     * @return PayYakuStlAcc
     * @throws Exception
     */
    public PayYakuStlAcc detailPayYakuStlAcc(String accNo) throws Exception {
        String sql = "select * from PAY_YAKU_STL_ACC where ACC_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,accNo);
            rs = ps.executeQuery();
            if(rs.next())return getPayYakuStlAccValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayYakuStlAcc
     * @param payYakuStlAcc
     * @throws Exception
     */
    public void updatePayYakuStlAcc(PayYakuStlAcc payYakuStlAcc) throws Exception {
        String sqlTmp = "";
        if(payYakuStlAcc.accName != null)sqlTmp = sqlTmp + " ACC_NAME=?,";
        if(payYakuStlAcc.bankCode != null)sqlTmp = sqlTmp + " BANK_CODE=?,";
        if(payYakuStlAcc.bankBranchName != null)sqlTmp = sqlTmp + " BANK_BRANCH_NAME=?,";
        if(payYakuStlAcc.bankBranchCode != null)sqlTmp = sqlTmp + " BANK_BRANCH_CODE=?,";
        if(payYakuStlAcc.province != null)sqlTmp = sqlTmp + " PROVINCE=?,";
        if(payYakuStlAcc.city != null)sqlTmp = sqlTmp + " CITY=?,";
        if(payYakuStlAcc.credentialNo != null)sqlTmp = sqlTmp + " CREDENTIAL_NO=?,";
        if(payYakuStlAcc.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_YAKU_STL_ACC "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where ACC_NO = ? "; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payYakuStlAcc.accName != null)ps.setString(n++,payYakuStlAcc.accName);
            if(payYakuStlAcc.bankCode != null)ps.setString(n++,payYakuStlAcc.bankCode);
            if(payYakuStlAcc.bankBranchName != null)ps.setString(n++,payYakuStlAcc.bankBranchName);
            if(payYakuStlAcc.bankBranchCode != null)ps.setString(n++,payYakuStlAcc.bankBranchCode);
            if(payYakuStlAcc.province != null)ps.setString(n++,payYakuStlAcc.province);
            if(payYakuStlAcc.city != null)ps.setString(n++,payYakuStlAcc.city);
            if(payYakuStlAcc.credentialNo != null)ps.setString(n++,payYakuStlAcc.credentialNo);
            if(payYakuStlAcc.tel != null)ps.setString(n++,payYakuStlAcc.tel);
            ps.setString(n++,payYakuStlAcc.accNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public String getAccNoByPayordno(PayOrder order) throws Exception {
        String sql = "select ACC_NO from PAY_YAKU_ORDER_ACC_RELATION where payordno='"+order.payordno+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next())return rs.getString("ACC_NO"); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return "";
    }
	public void updateStlAmt(PayOrder order,String accNo) throws Exception {
        String sql = "update PAY_YAKU_STL_ACC_CACHE set AMT=AMT+? where MERNO=? and ACC_NO=? and STL_DATE=?";
        String date = new SimpleDateFormat("yyyyMMdd").format(order.createtime);
        log.info(sql);
        log.info("AMT="+order.netrecamt+",MERNO="+order.merno+",ACC_NO="+accNo+",STL_DATE="+date);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setLong(n++,order.netrecamt);
            ps.setString(n++,order.merno);
            ps.setString(n++,accNo);
            ps.setString(n++,date);
            if(ps.executeUpdate()==0){
            	ps.close();
            	ps = con.prepareStatement(sql);
            	sql = "insert into PAY_YAKU_STL_ACC_CACHE(MERNO,ACC_NO,AMT,STL_DATE)values(?,?,?,?)";
            	n=1;
            	ps.setString(n++,order.merno);
            	ps.setString(n++,accNo);
                ps.setLong(n++,order.netrecamt);
                ps.setString(n++,date);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	public Map<String,String> getAccInfo(PayOrder order) throws Exception {
        String sql = "select ACC_NO from PAY_YAKU_STL_ACC where MERNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String,String> map = new HashMap<String,String>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, order.merno);
            rs = ps.executeQuery();
            while(rs.next())map.put(rs.getString("ACC_NO"),"");
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	public Map<String,PayYakuStlAccCache> getAccStlInfo(PayOrder order) throws Exception {
        String sql = "select * from PAY_YAKU_STL_ACC_CACHE where MERNO=? and STL_DATE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String,PayYakuStlAccCache> map = new HashMap<String,PayYakuStlAccCache>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, order.merno);
            ps.setString(2, new SimpleDateFormat("yyyyMMdd").format(order.createtime));
            rs = ps.executeQuery();
            while(rs.next()){
            	PayYakuStlAccCache accCashe = getPayYakuStlAccCacheValue(rs); 
                map.put(accCashe.accNo,accCashe);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	public void addAccStlInfo(PayOrder order, List<String> list) throws Exception {
        String sql = "insert into PAY_YAKU_STL_ACC_CACHE(MERNO,ACC_NO,AMT,STL_DATE)values(?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            String date = new SimpleDateFormat("yyyyMMdd").format(order.createtime);
            for(int i=0; i<list.size(); i++){
            	log.info("MERNO="+order.merno+",ACC_NO="+list.get(i)+",AMT=0,STL_DATE="+date);
            	n=1;
            	ps.setString(n++,order.merno);
            	ps.setString(n++,list.get(i));
                ps.setLong(n++,0l);
                ps.setString(n++,date);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	public String getMinStlAmtAccNo(PayOrder order) throws Exception {
        String sql = "select ACC_NO from PAY_YAKU_STL_ACC_CACHE where MERNO=? and STL_DATE=? order by amt asc";
        String date = new SimpleDateFormat("yyyyMMdd").format(order.createtime);
        log.info(sql);
        log.info("MERNO="+order.merno+",STL_DATE="+date);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String,PayYakuStlAccCache> map = new HashMap<String,PayYakuStlAccCache>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, order.merno);
            ps.setString(2, date);
            rs = ps.executeQuery();
            if(rs.next())return rs.getString("ACC_NO");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
	public void saveAccStlInfo(PayOrder order, String minAmtAccNo) throws Exception {
        String sql = "insert into PAY_YAKU_ORDER_ACC_RELATION(PAYORDNO,ACC_NO)values(?,?)";
        log.info(sql);
        log.info("PAYORDNO="+order.payordno+",ACC_NO="+minAmtAccNo);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, order.payordno);
            ps.setString(2, minAmtAccNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	public List getChannelYK() throws Exception {
        String sql = "select * from PAY_MERCHANT_CHANNEL_RELATION where CHANNEL_ID='"
        		+PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YK")+"'";
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
            	list.add(rs.getString("MERNO")+","+rs.getString("TRAN_TYPE"));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	public void updateChannelToZX() throws Exception {
        String sql = "update PAY_MERCHANT_CHANNEL_RELATION set CHANNEL_ID='"
        		+PayConstant.PAY_CONFIG.get("PAY_CHANNEL_CX")+"' where CHANNEL_ID='"
        		+PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YK")+"'";
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
	public void updateChannelToYK(List<String> list) throws Exception {
        String sql = "update PAY_MERCHANT_CHANNEL_RELATION set CHANNEL_ID='"
        		+PayConstant.PAY_CONFIG.get("PAY_CHANNEL_YK")+"' where MERNO=? and TRAN_TYPE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            for(int i=0; i<list.size(); i++){
            	String [] es = list.get(i).split(",");
            	if(es.length==2){
            		ps.setString(1, es[0]);
            		ps.setString(2, es[1]);
            		ps.addBatch();
            	}
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
}