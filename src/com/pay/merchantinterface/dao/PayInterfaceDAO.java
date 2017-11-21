package com.pay.merchantinterface.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jweb.dao.BaseDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayOrder;
import com.pay.order.dao.PayOrderDAO;
import com.pay.order.dao.PayProductOrder;
/**
 * Table PAY_ITERFACE DAO. 
 * @author Administrator
 *
 */
public class PayInterfaceDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayInterfaceDAO.class);
    public PayMerchant getCertByMerchantId(String merchantId) throws Exception{
        String sql = "select m.*,s.* from (select * from PAY_MERCHANT  where CUST_ID=?) m left join PAY_CUST_STL_INFO s on m.CUST_ID=s.CUST_ID";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,merchantId);
            rs = ps.executeQuery();
            if(rs.next())return PayMerchantDAO.getPayMerchantValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    public PayOrder getOrderByPrdordno(String payordno) throws Exception{        
        String sql = "select * from PAY_ORDER where PAYORDNO=?";
        log.info(sql);
        log.info("PAYORDNO="+payordno);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, payordno);
            rs = ps.executeQuery();
            if (rs.next()) return PayOrderDAO.getPayOrderValue(rs);
            else {
            	rs.close();
            	ps.close();
            	sql = "select * from PAY_ORDER@pay_db_bak where PAYORDNO=?";
            	ps = con.prepareStatement(sql);
            	ps.setString(1, payordno);
                rs = ps.executeQuery();
                if (rs.next()) return PayOrderDAO.getPayOrderValue(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    public String getOrderOrdstatusByPrdordno(String payordno) throws Exception{        
        String sql = "select ORDSTATUS from PAY_ORDER where PAYORDNO='"+payordno+"'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next())return rs.getString("ORDSTATUS");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    public List getPayOrderListByPrdordno(String merno,String prdordno) throws Exception{        
        String sql = "select * from PAY_ORDER where merno=? and prdordno=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, merno);
            ps.setString(2, prdordno);
            rs = ps.executeQuery();
            if (rs.next())list.add(PayOrderDAO.getPayOrderValue(rs));
            else {
            	rs.close();
            	ps.close();
            	sql = "select * from PAY_ORDER@pay_db_bak where merno=? and prdordno=?";
            	ps = con.prepareStatement(sql);
            	ps.setString(1, merno);
                ps.setString(2, prdordno);
                rs = ps.executeQuery();
                if (rs.next()) list.add(PayOrderDAO.getPayOrderValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public long getPrdOrderByPrdordno(String merchantId,String merchantOrderId) throws Exception{        
        String sql = "select sum(txamt) allPayAmt from PAY_ORDER where ORDSTATUS='01' and merno=? and prdordno=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, merchantId);
            ps.setString(2, merchantOrderId);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getLong("allPayAmt");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return 0;
    }
    public long getPrdRefundAmtSum(String merchantId,String merchantOrderId) throws Exception{        
        String sql = "select sum(RFAMT) allRefundAmt from PAY_REFUND where BANKSTS='01' and merno=? and ORIPRDORDNO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, merchantId);
            ps.setString(2, merchantOrderId);
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getLong("allRefundAmt");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return 0;
    }
    public int updatePayOrder(PayProductOrder prdOrder,PayOrder order) throws Exception{        
        String sql = "update PAY_ORDER set ORDSTATUS=?,ACTDAT=to_date(?,'yyyy-mm-dd hh24:mi:ss'),FEE=?,NETRECAMT=?," +
        		"FILED1=?,FILED3=?,BANKJRNNO=?,AGENT_FEE=?,CHANNEL_FEE=?,BANKERROR=? where PAYORDNO='"+order.payordno+"'";
//        String sql = "update PAY_ORDER set ORDSTATUS=?,ACTDAT=to_date(?,'yyyy-mm-dd hh24:mi:ss'),FEE=?,NETRECAMT=?," +
//        		"FILED1=?,FILED3=?,BANKJRNNO=?,AGENT_FEE=?,CHANNEL_FEE=?,BANKERROR=? where ORDSTATUS!='01' and PAYORDNO='"+order.payordno+"'";
        log.info(sql);
        log.info("ORDSTATUS="+order.ordstatus+";FEE="+order.fee+";NETRECAMT="+order.netrecamt+";FILED1="+order.filed1+";FILED3="+order.filed3+
        		";BANKJRNNO="+order.bankjrnno+";AGENT_FEE="+order.agentFee+";CHANNEL_FEE="+order.channelFee+";BANKERROR="+order.bankerror);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            ps.setString(n++, order.ordstatus);
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.actdat));
            ps.setLong(n++, order.fee);
            ps.setLong(n++, order.netrecamt);
            ps.setString(n++, order.filed1);
            ps.setString(n++, order.filed3);
            ps.setString(n++, order.bankjrnno==null?"":order.bankjrnno);
            ps.setLong(n++, order.agentFee);
            ps.setLong(n++, order.channelFee);
            ps.setString(n++, order.bankerror==null?"":order.bankerror);
            ps.executeUpdate();
            ps.close();
            sql = "update PAY_PRODUCT_ORDER set ORDSTATUS=?,STLSTS=? where MERNO='"+prdOrder.merno+"' and PRDORDNO='"+
            		prdOrder.prdordno+"' and "+prdOrder.ordamt
            		+"=(select sum(txamt) allPayAmt from PAY_ORDER where MERNO='"+prdOrder.merno+"' and PRDORDNO='"+prdOrder.prdordno+"')";
//            sql = "update PAY_PRODUCT_ORDER set ORDSTATUS=?,STLSTS=? where MERNO='"+prdOrder.merno+"' and PRDORDNO='"+
//            		prdOrder.prdordno+"' and "+prdOrder.ordamt
//            		+"=(select sum(txamt) allPayAmt from PAY_ORDER where ORDSTATUS!='01' and MERNO='"+prdOrder.merno+"' and PRDORDNO='"+prdOrder.prdordno+"')";
            log.info(sql);
            log.info("ORDSTATUS="+order.ordstatus+";STLSTS="+prdOrder.stlsts);
            ps = con.prepareStatement(sql);
            ps.setString(1, order.ordstatus);
            ps.setString(2, prdOrder.stlsts==null||prdOrder.stlsts.length()==0?"0":prdOrder.stlsts);
            int tmp = ps.executeUpdate();
            if(tmp==0)return updatePayOrderBak(prdOrder,order);//更新备份数据库
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public int updatePayOrderBak(PayProductOrder prdOrder,PayOrder order) throws Exception{        
        String sql = "update PAY_ORDER@pay_db_bak set ORDSTATUS=?,ACTDAT=to_date(?,'yyyy-mm-dd hh24:mi:ss'),FEE=?,NETRECAMT=?," +
        		"FILED1=?,FILED3=?,BANKJRNNO=?,AGENT_FEE=?,CHANNEL_FEE=?,BANKERROR=? where PAYORDNO='"+order.payordno+"'";
//        String sql = "update PAY_ORDER@pay_db_bak set ORDSTATUS=?,ACTDAT=to_date(?,'yyyy-mm-dd hh24:mi:ss'),FEE=?,NETRECAMT=?," +
//        		"FILED1=?,FILED3=?,BANKJRNNO=?,AGENT_FEE=?,CHANNEL_FEE=?,BANKERROR=? where ORDSTATUS!='01' and PAYORDNO='"+order.payordno+"'";
        log.info(sql);
        log.info("ORDSTATUS="+order.ordstatus+";FEE="+order.fee+";NETRECAMT="+order.netrecamt+";FILED1="+order.filed1+";FILED3="+order.filed3+
        		";BANKJRNNO="+order.bankjrnno+";AGENT_FEE="+order.agentFee+";CHANNEL_FEE="+order.channelFee+";BANKERROR="+order.bankerror);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            ps.setString(n++, order.ordstatus);
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.actdat));
            ps.setLong(n++, order.fee);
            ps.setLong(n++, order.netrecamt);
            ps.setString(n++, order.filed1);
            ps.setString(n++, order.filed3);
            ps.setString(n++, order.bankjrnno==null?"":order.bankjrnno);
            ps.setLong(n++, order.agentFee);
            ps.setLong(n++, order.channelFee);
            ps.setString(n++, order.bankerror==null?"":order.bankerror);
            ps.executeUpdate();
            ps.close();
            sql = "update PAY_PRODUCT_ORDER@pay_db_bak set ORDSTATUS=?,STLSTS=? where MERNO='"+prdOrder.merno+"' and PRDORDNO='"+
            		prdOrder.prdordno+"' and "+prdOrder.ordamt
            		+"=(select sum(txamt) allPayAmt from PAY_ORDER@pay_db_bak where MERNO='"+prdOrder.merno+"' and PRDORDNO='"+prdOrder.prdordno+"')";
//            sql = "update PAY_PRODUCT_ORDER@pay_db_bak set ORDSTATUS=?,STLSTS=? where MERNO='"+prdOrder.merno+"' and PRDORDNO='"+
//            		prdOrder.prdordno+"' and "+prdOrder.ordamt
//            		+"=(select sum(txamt) allPayAmt from PAY_ORDER@pay_db_bak where ORDSTATUS!='01' and MERNO='"+prdOrder.merno+"' and PRDORDNO='"+prdOrder.prdordno+"')";
            log.info(sql);
            log.info("ORDSTATUS="+order.ordstatus+";STLSTS="+prdOrder.stlsts);
            ps = con.prepareStatement(sql);
            ps.setString(1, order.ordstatus);
            ps.setString(2, prdOrder.stlsts==null||prdOrder.stlsts.length()==0?"0":prdOrder.stlsts);
            int tmp = ps.executeUpdate();
            if(tmp==0)throw new Exception("订单已支付成功");
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public void updatePayOrderNotifyFalg(PayOrder payOrder) throws Exception{        
        String sql = "update PAY_ORDER set NOTIFY_MER_FLAG='"+payOrder.notifyMerFlag+"',NOTIFY_MER_TIME=sysdate where PAYORDNO='"+payOrder.payordno+"'";
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
    public void updateOrderForCustId(PayRequest payRequest) throws Exception{
    	String sql1 = "update PAY_PRODUCT_ORDER set CUST_ID=? where ID='"+payRequest.productOrder.id+"'";
        String sql2 = "update PAY_ORDER set CUST_ID=? where PAYORDNO='"+payRequest.payOrder.payordno+"'";
        log.info(sql1);
        log.info(sql2);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql1);
            ps.setString(1,payRequest.payerId);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql2);
            ps.setString(1, payRequest.payerId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public void updateQuickPayBindCardStatus(PayProductOrder prdOrder,PayOrder order) throws Exception{
        String sql = "update PAY_TRAN_USER_QUICK_CARD set status='1' where CARD_NO=?";
        log.info(sql);
        log.info("CARD_NO="+order.payacno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,order.payacno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * 更新FILED5和BANKJRNNO
     * @param order
     * @return
     * @throws Exception
     */
    public void updatePayOrder(PayOrder order) throws Exception{        
        String sql = "update PAY_ORDER set FILED5=?,BANKJRNNO=? where PAYORDNO='"+order.payordno+"'";
        log.info(sql);
        log.info("FILED5="+order.filed5+";BANKJRNNO="+order.bankjrnno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            ps.setString(n++, order.filed5==null?"":order.filed5);
            ps.setString(n++, order.bankjrnno==null?"":order.bankjrnno);
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * 更新FILED5和BANKJRNNO
     * @param order
     * @return
     * @throws Exception
     */
    public void updatePayOrder(PayOrder order,String oldOrderNo) throws Exception{        
        String sql = "update PAY_ORDER set FILED5=?,BANKJRNNO=?,PAYORDNO='"+order.payordno+"' where PAYORDNO='"+oldOrderNo+"'";
        String sql1 = "update PAY_YAKU_ORDER_ACC_RELATION set PAYORDNO='"+order.payordno+"' where PAYORDNO='"+oldOrderNo+"'";
        log.info(sql);
        log.info("FILED5="+order.filed5+";BANKJRNNO="+order.bankjrnno);
        log.info(sql1);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            int n = 1;
            ps.setString(n++, order.filed5==null?"":order.filed5);
            ps.setString(n++, order.bankjrnno==null?"":order.bankjrnno);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement(sql1);
            ps.executeUpdate();
            con.commit();
        } catch (Exception e) {
        	con.rollback();
            e.printStackTrace();
            throw e;
        } finally {
        	con.setAutoCommit(true);
            close(null, ps, con);
        }
    }
}