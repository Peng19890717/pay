package com.pay.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.pay.settlement.dao.PayMerchantSettlement;
/**
 * Table PAY_PRODUCT_ORDER DAO. 
 * @author Administrator
 *
 */
public class PayProductOrderDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayProductOrderDAO.class);
    public static synchronized PayProductOrder getPayProductOrderValue(ResultSet rs)throws SQLException {
        PayProductOrder payProductOrder = new PayProductOrder();
        payProductOrder.id = rs.getString("ID");
        payProductOrder.prdordno = rs.getString("PRDORDNO");
        payProductOrder.merno = rs.getString("MERNO");
        payProductOrder.versionid = rs.getString("VERSIONID");
        payProductOrder.prdordtype = rs.getString("PRDORDTYPE");
        payProductOrder.biztype = rs.getString("BIZTYPE");
        payProductOrder.ordertime = rs.getTimestamp("ORDERTIME");
        payProductOrder.ordamt = rs.getLong("ORDAMT");
        payProductOrder.ordstatus = rs.getString("ORDSTATUS");
        payProductOrder.ordccy = rs.getString("ORDCCY");
        payProductOrder.notifyurl = rs.getString("NOTIFYURL");
        payProductOrder.returl = rs.getString("RETURL");
        payProductOrder.signature = rs.getString("SIGNATURE");
        payProductOrder.signtype = rs.getString("SIGNTYPE");
        payProductOrder.verifystring = rs.getString("VERIFYSTRING");
        payProductOrder.prddisurl = rs.getString("PRDDISURL");
        payProductOrder.prdname = rs.getString("PRDNAME");
        payProductOrder.prdshortname = rs.getString("PRDSHORTNAME");
        payProductOrder.prddesc = rs.getString("PRDDESC");
        payProductOrder.merremark = rs.getString("MERREMARK");
        payProductOrder.rpttype = rs.getString("RPTTYPE");
        payProductOrder.prdunitprice = rs.getLong("PRDUNITPRICE");
        payProductOrder.buycount = rs.getLong("BUYCOUNT");
        payProductOrder.defpayway = rs.getString("DEFPAYWAY");
        payProductOrder.custId = rs.getString("CUST_ID");
        payProductOrder.merpayacno = rs.getString("MERPAYACNO");
        payProductOrder.sellrptacno = rs.getString("SELLRPTACNO");
        payProductOrder.sellrptamt = rs.getLong("SELLRPTAMT");
        payProductOrder.recacno1 = rs.getString("RECACNO1");
        payProductOrder.recamt1 = rs.getLong("RECAMT1");
        payProductOrder.recacno2 = rs.getString("RECACNO2");
        payProductOrder.recamt2 = rs.getLong("RECAMT2");
        payProductOrder.recacno3 = rs.getString("RECACNO3");
        payProductOrder.recamt3 = rs.getLong("RECAMT3");
        payProductOrder.recacno4 = rs.getString("RECACNO4");
        payProductOrder.recamt4 = rs.getString("RECAMT4");
        payProductOrder.recacno5 = rs.getString("RECACNO5");
        payProductOrder.recamt5 = rs.getLong("RECAMT5");
        payProductOrder.recacno6 = rs.getString("RECACNO6");
        payProductOrder.recamt6 = rs.getLong("RECAMT6");
        payProductOrder.recacno7 = rs.getString("RECACNO7");
        payProductOrder.recamt7 = rs.getLong("RECAMT7");
        payProductOrder.recacno8 = rs.getString("RECACNO8");
        payProductOrder.recamt8 = rs.getLong("RECAMT8");
        payProductOrder.recacno9 = rs.getString("RECACNO9");
        payProductOrder.recamt9 = rs.getLong("RECAMT9");
        payProductOrder.paymode = rs.getString("PAYMODE");
        payProductOrder.custpayacno = rs.getString("CUSTPAYACNO");
        payProductOrder.custrptacno = rs.getString("CUSTRPTACNO");
        payProductOrder.transort = rs.getString("TRANSORT");
        payProductOrder.noncashamt = rs.getLong("NONCASHAMT");
        payProductOrder.acjrnno = rs.getString("ACJRNNO");
        payProductOrder.acdate = rs.getTimestamp("ACDATE");
        payProductOrder.actime = rs.getTimestamp("ACTIME");
        payProductOrder.txncomamt = rs.getLong("TXNCOMAMT");
        payProductOrder.rftotalamt = rs.getLong("RFTOTALAMT");
        payProductOrder.rfcomamt = rs.getLong("RFCOMAMT");
        payProductOrder.bankerror = rs.getString("BANKERROR");
        payProductOrder.payerror = rs.getString("PAYERROR");
        payProductOrder.stlsts = rs.getString("STLSTS");
        payProductOrder.stlbatno = rs.getString("STLBATNO");
        payProductOrder.cpsflg = rs.getString("CPSFLG");
        payProductOrder.payordno = rs.getString("PAYORDNO");
        payProductOrder.cxtime = rs.getTimestamp("CXTIME");
        payProductOrder.filed1 = rs.getString("FILED1");
        payProductOrder.filed2 = rs.getString("FILED2");
        payProductOrder.filed3 = rs.getString("FILED3");
        payProductOrder.filed4 = rs.getString("FILED4");
        payProductOrder.filed5 = rs.getString("FILED5");
        payProductOrder.mobile = rs.getString("MOBILE");
        payProductOrder.guaranteeStatus = rs.getString("GUARANTEE_STATUS");
        payProductOrder.credentialType = rs.getString("CREDENTIAL_TYPE");
        payProductOrder.credentialNo = rs.getString("CREDENTIAL_NO");
        payProductOrder.rechargeType = rs.getString("RECHARGE_TYPE");
        payProductOrder.receivePayNotifyUrl = rs.getString("RECEIVE_PAY_NOTIFY_URL");
        return payProductOrder;
    }
    public String addPayProductOrder(PayProductOrder payProductOrder) throws Exception {
        String sql = "insert into PAY_PRODUCT_ORDER("+
            "ID," + 
            "PRDORDNO," + 
            "MERNO," + 
            "VERSIONID," + 
            "PRDORDTYPE," + 
            "BIZTYPE," + 
            "ORDAMT," + 
            "ORDSTATUS," + 
            "ORDCCY," + 
            "NOTIFYURL," + 
            "RETURL," + 
            "SIGNATURE," + 
            "SIGNTYPE," + 
            "VERIFYSTRING," + 
            "PRDDISURL," + 
            "PRDNAME," + 
            "PRDSHORTNAME," + 
            "PRDDESC," + 
            "MERREMARK," + 
            "RPTTYPE," + 
            "PRDUNITPRICE," + 
            "BUYCOUNT," + 
            "DEFPAYWAY," + 
            "CUST_ID," + 
            "MERPAYACNO," + 
            "SELLRPTACNO," + 
            "SELLRPTAMT," + 
            "PAYMODE," + 
            "CUSTPAYACNO," + 
            "CUSTRPTACNO," + 
            "TRANSORT," + 
            "NONCASHAMT," + 
            "ACJRNNO," + 
            "TXNCOMAMT," + 
            "RFTOTALAMT," + 
            "RFCOMAMT," + 
            "BANKERROR," + 
            "PAYERROR," + 
            "STLSTS," + 
            "STLBATNO," + 
            "CPSFLG," + 
            "PAYORDNO,ORDERTIME,MOBILE,CREDENTIAL_TYPE,CREDENTIAL_NO,RECHARGE_TYPE,RECEIVE_PAY_NOTIFY_URL" +
            ")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payProductOrder.id);
            ps.setString(n++,payProductOrder.prdordno);
            ps.setString(n++,payProductOrder.merno);
            ps.setString(n++,payProductOrder.versionid);
            ps.setString(n++,payProductOrder.prdordtype);
            ps.setString(n++,payProductOrder.biztype);
            ps.setLong(n++,payProductOrder.ordamt);
            ps.setString(n++,payProductOrder.ordstatus);
            ps.setString(n++,payProductOrder.ordccy);
            ps.setString(n++,payProductOrder.notifyurl);
            ps.setString(n++,payProductOrder.returl);
            ps.setString(n++,payProductOrder.signature);
            ps.setString(n++,payProductOrder.signtype);
            ps.setString(n++,payProductOrder.verifystring);
            ps.setString(n++,payProductOrder.prddisurl);
            ps.setString(n++,payProductOrder.prdname);
            ps.setString(n++,payProductOrder.prdshortname);
            ps.setString(n++,payProductOrder.prddesc);
            ps.setString(n++,payProductOrder.merremark);
            ps.setString(n++,payProductOrder.rpttype);
            ps.setLong(n++,payProductOrder.prdunitprice);
            ps.setLong(n++,payProductOrder.buycount);
            ps.setString(n++,payProductOrder.defpayway);
            ps.setString(n++,payProductOrder.custId);
            ps.setString(n++,payProductOrder.merpayacno);
            ps.setString(n++,payProductOrder.sellrptacno);
            ps.setLong(n++,payProductOrder.sellrptamt);
            ps.setString(n++,payProductOrder.paymode);
            ps.setString(n++,payProductOrder.custpayacno);
            ps.setString(n++,payProductOrder.custrptacno);
            ps.setString(n++,payProductOrder.transort);
            ps.setLong(n++,payProductOrder.noncashamt);
            ps.setString(n++,payProductOrder.acjrnno);
            ps.setLong(n++,payProductOrder.txncomamt);
            ps.setLong(n++,payProductOrder.rftotalamt);
            ps.setLong(n++,payProductOrder.rfcomamt);
            ps.setString(n++,payProductOrder.bankerror);
            ps.setString(n++,payProductOrder.payerror);
            ps.setString(n++,payProductOrder.stlsts);
            ps.setString(n++,payProductOrder.stlbatno);
            ps.setString(n++,payProductOrder.cpsflg);
            ps.setString(n++,payProductOrder.payordno);
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payProductOrder.ordertime));
            ps.setString(n++,payProductOrder.mobile);
            ps.setString(n++,payProductOrder.credentialType);
            ps.setString(n++,payProductOrder.credentialNo);
            ps.setString(n++,payProductOrder.rechargeType);
            ps.setString(n++,payProductOrder.receivePayNotifyUrl);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public PayProductOrder getProductOrderById(String merNo,String prdOrderNo) throws Exception{        
        String sql = "select * from PAY_PRODUCT_ORDER where MERNO=? and PRDORDNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, merNo);
            ps.setString(2, prdOrderNo);
            rs = ps.executeQuery();
            if(rs.next()) return getPayProductOrderValue(rs);
            else {
            	rs.close();
            	ps.close();
            	sql = "select * from PAY_PRODUCT_ORDER@pay_db_bak where MERNO=? and PRDORDNO=?";
                ps = con.prepareStatement(sql);
                ps.setString(1, merNo);
                ps.setString(2, prdOrderNo);
                rs = ps.executeQuery();
                if(rs.next())return getPayProductOrderValue(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    public String updatePrdOrderStatus(String id,String status) throws Exception {
        String sql = "update PAY_PRODUCT_ORDER set ORDSTATUS=? where ID=?";
        log.info(sql);
        log.info("ORDSTATUS="+status+";ID="+id);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, id);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
	public void updateOrderStlStatus(PayMerchantSettlement stl) throws Exception {
		String sql = "update PAY_PRODUCT_ORDER pr set ACJRNNO=?,ACDATE=sysdate,ACTIME=sysdate,STLSTS='1' "+
					" where exists(select 1 from PAY_ORDER po where pr.MERNO=po.MERNO and pr.PRDORDNO=po.PRDORDNO "+
					" and po.ORDSTATUS='01' and po.MERNO=?  and po.CREATETIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') " + 
					" and po.CREATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);

        log.info("ACJRNNO="+stl.stlId+";MERNO="+stl.stlMerId+";stlFromTime="+ new java.text.SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTime)+" 00:00:00"+";stlToTime"+
        		new java.text.SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTime)+" 23:59:59");

        log.info("ACJRNNO="+stl.stlId+";MERNO="+stl.stlMerId);

        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            ps.setString(n++, stl.stlId);
            ps.setString(n++, stl.stlMerId);
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTime)+" 00:00:00");
            ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTime)+" 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
	}
	public List getStlProductOrderList(PayMerchantSettlement stl,long start,long end) throws Exception{        
        String sql = "select * from(select rownum rowno,tmp.* from(select * from PAY_PRODUCT_ORDER where ACJRNNO=? order by ID)tmp)tmp1" +
        		" where rowno>"+start+" and rowno<="+end;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, stl.stlId);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayProductOrderValue(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return list;
    }
}