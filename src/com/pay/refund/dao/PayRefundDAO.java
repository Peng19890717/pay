package com.pay.refund.dao;

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
import com.pay.merchant.dao.PayMerchant;
import com.pay.settlement.dao.PayMerchantSettlement;
/**
 * Table PAY_REFUND DAO. 
 * @author Administrator
 *
 */
public class PayRefundDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayRefundDAO.class);
    public static synchronized PayRefund getPayRefundValue(ResultSet rs)throws SQLException {
        PayRefund payRefund = new PayRefund();
        payRefund.refordno = rs.getString("REFORDNO");
        payRefund.rfreqdate = rs.getTimestamp("RFREQDATE");
        payRefund.oriprdordno = rs.getString("ORIPRDORDNO");
        payRefund.oripayordno = rs.getString("ORIPAYORDNO");
        payRefund.rfpayordno = rs.getString("RFPAYORDNO");
        payRefund.rfpaydate = rs.getTimestamp("RFPAYDATE");
        payRefund.bankcode = rs.getString("BANKCODE");
        payRefund.getbankcode = rs.getString("GETBANKCODE");
        payRefund.custpayacno = rs.getString("CUSTPAYACNO");
        payRefund.bankpayusernm = rs.getString("BANKPAYUSERNM");
        payRefund.merpayacno = rs.getString("MERPAYACNO");
        payRefund.custId = rs.getString("CUST_ID");
        payRefund.txccy = rs.getString("TXCCY");
        payRefund.rfamt = rs.getLong("RFAMT");
        payRefund.fee = rs.getLong("FEE");
        payRefund.netrecamt = rs.getLong("NETRECAMT");
        payRefund.rfsake = rs.getString("RFSAKE");
        payRefund.banksts = rs.getString("BANKSTS");
        payRefund.ordstatus = rs.getString("ORDSTATUS");
        payRefund.obankstldate = rs.getTimestamp("OBANKSTLDATE");
        payRefund.obankjrnno = rs.getString("OBANKJRNNO");
        payRefund.bankstldate = rs.getTimestamp("BANKSTLDATE");
        payRefund.bankjrnno = rs.getString("BANKJRNNO");
        payRefund.returl = rs.getString("RETURL");
        payRefund.notifyurl = rs.getString("NOTIFYURL");
        payRefund.bankerror = rs.getString("BANKERROR");
        payRefund.merno = rs.getString("MERNO");
        payRefund.bnkdat = rs.getTimestamp("BNKDAT");
        payRefund.stlbatno = rs.getString("STLBATNO");
        payRefund.rfordtime = rs.getTimestamp("RFORDTIME");
        payRefund.stlsts = rs.getString("STLSTS");
        payRefund.bustyp = rs.getString("BUSTYP");
        payRefund.operId = rs.getString("OPER_ID");
        payRefund.filed1 = rs.getString("FILED1");
        payRefund.filed2 = rs.getString("FILED2");
        payRefund.filed3 = rs.getString("FILED3");
        payRefund.filed4 = rs.getString("FILED4");
        payRefund.filed5 = rs.getString("FILED5");
        return payRefund;
    }
    public long addPayRefund(PayRefund payRefund) throws Exception {
        String sql = "insert into PAY_Refund("+
            "REFORDNO," + 
            "ORIPRDORDNO," + 
            "ORIPAYORDNO," + 
            "RFPAYORDNO," + 
            "BANKCODE," + 
            "GETBANKCODE," + 
            "CUSTPAYACNO," + 
            "BANKPAYUSERNM," + 
            "MERPAYACNO," + 
            "CUST_ID," + 
            "TXCCY," + 
            "RFAMT," + 
            "FEE," + 
            "NETRECAMT," + 
            "RFSAKE," + 
            "BANKSTS," + 
            "ORDSTATUS," + 
            "OBANKJRNNO," + 
            "BANKJRNNO," + 
            "RETURL," + 
            "NOTIFYURL," + 
            "BANKERROR," + 
            "MERNO," + 
            "STLBATNO," +
            "RFORDTIME," +
            "STLSTS," + 
            "BUSTYP," + 
            "OPER_ID," + 
            "FILED1," + 
            "FILED2," + 
            "FILED3," + 
            "FILED4," + 
            "FILED5)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?)";
        String refundAmtSql = "select sum(RFAMT) RFAMT_SUM from PAY_REFUND where MERNO='"+payRefund.merno+"' and ORIPRDORDNO='"+payRefund.oriprdordno+"'";
        String sql1 = "update PAY_PRODUCT_ORDER set " +
        		" RFTOTALAMT=? where MERNO='"+payRefund.merno+"' and PRDORDNO='"+ payRefund.oriprdordno+"'";
        log.info(sql);
        log.info(sql1);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRefund.refordno);
            ps.setString(n++,payRefund.oriprdordno);
            ps.setString(n++,payRefund.oripayordno);
            ps.setString(n++,payRefund.rfpayordno);
            ps.setString(n++,payRefund.bankcode);
            ps.setString(n++,payRefund.getbankcode);
            ps.setString(n++,payRefund.custpayacno);
            ps.setString(n++,payRefund.bankpayusernm);
            ps.setString(n++,payRefund.merpayacno);
            ps.setString(n++,payRefund.custId);
            ps.setString(n++,payRefund.txccy);
            ps.setLong(n++,payRefund.rfamt);
            ps.setLong(n++,payRefund.fee);
            ps.setLong(n++,payRefund.netrecamt);
            ps.setString(n++,payRefund.rfsake);
            ps.setString(n++,payRefund.banksts);
            ps.setString(n++,payRefund.ordstatus);
            ps.setString(n++,payRefund.obankjrnno);
            ps.setString(n++,payRefund.bankjrnno);
            ps.setString(n++,payRefund.returl);
            ps.setString(n++,payRefund.notifyurl);
            ps.setString(n++,payRefund.bankerror);
            ps.setString(n++,payRefund.merno);
            ps.setString(n++,payRefund.stlbatno);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRefund.rfordtime));
            ps.setString(n++,payRefund.stlsts);
            ps.setString(n++,payRefund.bustyp);
            ps.setString(n++,payRefund.operId);
            ps.setString(n++,payRefund.filed1);
            ps.setString(n++,payRefund.filed2);
            ps.setString(n++,payRefund.filed3);
            ps.setString(n++,payRefund.filed4);
            ps.setString(n++,payRefund.filed5);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(refundAmtSql);
            rs = ps.executeQuery();
            long refundAmt = 0l;
            if(rs.next())refundAmt = rs.getLong("RFAMT_SUM");
            rs.close();
            ps.close();
            ps = con.prepareStatement(sql1);
            ps.setLong(1, refundAmt);
            ps.executeUpdate();
            return refundAmt;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(rs, ps, con);
        }
    }
    public String cancelOrder(PayRefund payRefund) throws Exception {
        String sql = "insert into PAY_Refund("+
            "REFORDNO," + 
            "ORIPRDORDNO," + 
            "ORIPAYORDNO," + 
            "RFPAYORDNO," + 
            "BANKCODE," + 
            "GETBANKCODE," + 
            "CUSTPAYACNO," + 
            "BANKPAYUSERNM," + 
            "MERPAYACNO," + 
            "CUST_ID," + 
            "TXCCY," + 
            "RFAMT," + 
            "FEE," + 
            "NETRECAMT," + 
            "RFSAKE," + 
            "BANKSTS," + 
            "ORDSTATUS," + 
            "OBANKJRNNO," + 
            "BANKJRNNO," + 
            "RETURL," + 
            "NOTIFYURL," + 
            "BANKERROR," + 
            "MERNO," + 
            "STLBATNO," + 
            "RFORDTIME," + 
            "STLSTS," + 
            "BUSTYP," + 
            "OPER_ID," + 
            "FILED1," + 
            "FILED2," + 
            "FILED3," + 
            "FILED4," + 
            "FILED5)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?)";
        String sql1 = "update PAY_PRODUCT_ORDER set " +
        		" RFTOTALAMT=ORDAMT, " +
        		" CXTIME=sysdate," +
        		" ORDSTATUS='09'" +
        		" where MERNO='"+payRefund.merno+"' and PRDORDNO='"+ payRefund.oriprdordno+"'";
        log.info(sql);
        log.info(sql1);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payRefund.refordno);
            ps.setString(n++,payRefund.oriprdordno);
            ps.setString(n++,payRefund.oripayordno);
            ps.setString(n++,payRefund.rfpayordno);
            ps.setString(n++,payRefund.bankcode);
            ps.setString(n++,payRefund.getbankcode);
            ps.setString(n++,payRefund.custpayacno);
            ps.setString(n++,payRefund.bankpayusernm);
            ps.setString(n++,payRefund.merpayacno);
            ps.setString(n++,payRefund.custId);
            ps.setString(n++,payRefund.txccy);
            ps.setLong(n++,payRefund.rfamt);
            ps.setLong(n++,payRefund.fee);
            ps.setLong(n++,payRefund.netrecamt);
            ps.setString(n++,payRefund.rfsake);
            ps.setString(n++,payRefund.banksts);
            ps.setString(n++,payRefund.ordstatus);
            ps.setString(n++,payRefund.obankjrnno);
            ps.setString(n++,payRefund.bankjrnno);
            ps.setString(n++,payRefund.returl);
            ps.setString(n++,payRefund.notifyurl);
            ps.setString(n++,payRefund.bankerror);
            ps.setString(n++,payRefund.merno);
            ps.setString(n++,payRefund.stlbatno);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRefund.rfordtime));
            ps.setString(n++,payRefund.stlsts);
            ps.setString(n++,payRefund.bustyp);
            ps.setString(n++,payRefund.operId);
            ps.setString(n++,payRefund.filed1);
            ps.setString(n++,payRefund.filed2);
            ps.setString(n++,payRefund.filed3);
            ps.setString(n++,payRefund.filed4);
            ps.setString(n++,payRefund.filed5);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql1);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public List getRefundListByMerchantOrderId(String merchantId,
			String merchantOrderId) {        
        String sql = "select * from PAY_Refund where MERNO='"+merchantId+"' and ORIPRDORDNO='"+merchantOrderId+"'";
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
                list.add(getPayRefundValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return list;
    }
    public PayRefund getPayRefundById(String refordno) throws Exception{        
        String sql = "select * from PAY_REFUND where REFORDNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,refordno);
            rs = ps.executeQuery();
            if(rs.next())return getPayRefundValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * Set query condition sql.
     * @param payRefund
     * @return
     */
    private String setPayRefundSql(PayRefund payRefund) {
        StringBuffer sql = new StringBuffer();
        
        if(payRefund.custId != null && payRefund.custId.length() !=0) {
            sql.append(" MERNO = ? and ");
        }
        if(payRefund.banksts != null && payRefund.banksts.length() !=0) {
            sql.append(" BANKSTS = ? and ");
        }
        if(payRefund.rfreqdate != null) {
            sql.append(" RFREQDATE = to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payRefund.refordno != null && payRefund.refordno.length() !=0) {
            sql.append(" REFORDNO = ? and ");
        }
        if(payRefund.oriprdordno != null && payRefund.oriprdordno.length() !=0) {
            sql.append(" ORIPRDORDNO = ? and ");
        }
        if(payRefund.rfreqdateStart != null) {
            sql.append(" RFORDTIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payRefund.rfreqdateEnd != null) {
            sql.append(" RFORDTIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payRefund
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayRefundParameter(PreparedStatement ps,PayRefund payRefund,int n)throws SQLException {
        if(payRefund.custId != null && payRefund.custId.length() !=0) {
            ps.setString(n++,payRefund.custId);
        }
        if(payRefund.banksts != null && payRefund.banksts.length() !=0) {
            ps.setString(n++,payRefund.banksts);
        }
        if(payRefund.rfreqdate != null) {
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRefund.rfreqdate));
        }
        if(payRefund.refordno != null && payRefund.refordno.length() !=0) {
            ps.setString(n++,payRefund.refordno);
        }
        if(payRefund.oriprdordno != null && payRefund.oriprdordno.length() !=0) {
            ps.setString(n++,payRefund.oriprdordno);
        }
        if(payRefund.rfreqdateStart != null) {
        	//注意日期格式的转换
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRefund.rfreqdateStart)+" 00:00:00");
        }
        if(payRefund.rfreqdateEnd != null) {
        	 ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRefund.rfreqdateEnd)+" 23:59:59");
        }
        return n;
    }
    /**
     * Get records count.
     * @param payRefund
     * @return
     */
    public int getPayRefundCount(PayRefund payRefund) {
        String sqlCon = setPayRefundSql(payRefund);
        String sql = "select count(rownum) recordCount from PAY_REFUND " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRefundParameter(ps,payRefund,n);
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
     * @param payRefund
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayRefundList(PayRefund payRefund,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayRefundSql(payRefund);
        String sortOrder = sort == null || sort.length()==0?" order by RFORDTIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_REFUND tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            //设置占位符的值
            setPayRefundParameter(ps,payRefund,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRefundValue(rs));
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
     * 导出excel数据
     * @param payRefund
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public List getPayRefundList(PayRefund payRefund,long start,long end) throws Exception{
        String sqlCon = setPayRefundSql(payRefund);
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_REFUND tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +
                "   order by RFORDTIME desc) tmp1 "+
                ")  where rowno > "+start+ " and rowno<= " + end + "  order by RFORDTIME desc ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            //设置占位符的值
            setPayRefundParameter(ps,payRefund,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayRefundValue(rs));
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
     * 根据退款订单号查询该订单
     * @param payRefund
     * @return
     */
	public PayRefund getPayRefundDetail(PayRefund payRefund) {
		String sqlCon = setPayRefundSql(payRefund);
        String sql = "select tmp.*,tmp2.PAYACNO from PAY_REFUND tmp left join PAY_ORDER tmp2 on tmp.ORIPAYORDNO = tmp2.PAYORDNO " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayRefundParameter(ps,payRefund,n);
            rs = ps.executeQuery();
            if (rs.next()) {
            	PayRefund refund=new PayRefund();
            	refund.setRefordno(rs.getString("refordno"));
            	refund.setOriprdordno(rs.getString("oriprdordno"));
            	refund.setRfamt(rs.getLong("rfamt"));
            	refund.setBanksts(rs.getString("banksts"));
            	refund.setPayacno(rs.getString("payacno"));
            	refund.setFee(rs.getLong("fee"));
            	refund.setNetrecamt(rs.getLong("netrecamt"));
            	refund.setRfordtime(rs.getDate("rfordtime"));
            	refund.setRfreqdate(rs.getDate("rfreqdate"));
                return refund;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	public Long [] getStlTotalRefundAmt(PayMerchant mer, String stlDayStart,String stlDayEnd) {
		 String sql ="select sum(RFAMT) total_refund_amt,count(REFORDNO) total_refund_count  from PAY_REFUND where STLSTS!='2' and BANKSTS!='02' and MERNO='"+mer.custId+"' " +
		 		" and RFORDTIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and RFORDTIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss')";
       log.info(sql);
       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       try {
           con = connection();
           ps = con.prepareStatement(sql);
           ps.setString(1,stlDayStart+" 00:00:00");
           ps.setString(2,stlDayEnd+" 23:59:59");
           rs = ps.executeQuery();
           if (rs.next()) {
        	   Long l1 = rs.getLong("total_refund_amt");
               return new Long[]{l1==null?0l:l1,rs.getLong("total_refund_count")};
           }
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           close(rs, ps, con);
       }
       return new Long[]{0l,0l};
	}
	public List getStlPayRefundList(PayMerchantSettlement stl, long start, long end) {
		String sql ="select * from(select rownum rowno,tmp.* from(" +
				"select * from PAY_REFUND where BANKSTS='01' and MERNO='"+stl.stlMerId+"' " +
		 		" and RFORDTIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and RFORDTIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss') order by REFORDNO " +
		 		")tmp)tmp1 where rowno>"+start+" and rowno<="+end;
       log.info(sql);
       Connection con = null;
       PreparedStatement ps = null;
       ResultSet rs = null;
       List list = new ArrayList();
       try {
           con = connection();
           ps = con.prepareStatement(sql);
           int n=1;
           ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTime)+" 00:00:00");
           ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTime)+" 23:59:59");
           rs = ps.executeQuery();
           while (rs.next()) {
        	   list.add(getPayRefundValue(rs));
           }
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           close(rs, ps, con);
       }
       return list;
	}
	public void updateRemarkForRefund(PayRefund payRefund) throws Exception {        
        String sql = "update PAY_REFUND set RFSAKE='"+payRefund.rfsake+"' where REFORDNO='"+payRefund.refordno+"'";
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
     * set result PayRefund
     * @param PayRefund
     * @throws Exception     
     */
    public int setResultRefund(PayRefund payRefund) throws Exception {
        String sql = "update PAY_REFUND set OPER_ID=?," +
        		" RFREQDATE=sysdate,BANKSTS=?," +
        		" RFSAKE = RFSAKE||? where REFORDNO=? and BANKSTS='00'";
        log.info(sql);
        log.info("BANKSTS="+payRefund.banksts+";REFORDNO="+payRefund.refordno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,payRefund.operId);
            ps.setString(2,payRefund.banksts);
            ps.setString(3,payRefund.rfsake==null||payRefund.rfsake.length()==0
            		?"":"；"+payRefund.rfsake);
            ps.setString(4,payRefund.refordno);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
}