package com.pay.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.settlement.dao.PayMerchantSettlement;
/**
 * Table PAY_ORDER DAO. 
 * @author Administrator
 *
 */
public class PayOrderDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayOrderDAO.class);
    public static synchronized PayOrder getPayOrderValue(ResultSet rs)throws SQLException {
        PayOrder payOrder = new PayOrder();
        payOrder.payordno = rs.getString("PAYORDNO");
        payOrder.actdat = rs.getTimestamp("ACTDAT");
        payOrder.cleardat = rs.getTimestamp("CLEARDAT");
        payOrder.ordstatus = rs.getString("ORDSTATUS");
        payOrder.prdordtype = rs.getString("PRDORDTYPE");
        payOrder.paytype = rs.getString("PAYTYPE");
        payOrder.multype = rs.getString("MULTYPE");
        payOrder.bankcod = rs.getString("BANKCOD");
        payOrder.paybnkcod = rs.getString("PAYBNKCOD");
        payOrder.bankstldate = rs.getTimestamp("BANKSTLDATE");
        payOrder.bankjrnno = rs.getString("BANKJRNNO");
        payOrder.txccy = rs.getString("TXCCY");
        payOrder.txamt = rs.getLong("TXAMT");
        payOrder.accamt = rs.getLong("ACCAMT");
        payOrder.mulamt = rs.getLong("MULAMT");
        payOrder.fee = rs.getLong("FEE");
        payOrder.netrecamt = rs.getLong("NETRECAMT");
        payOrder.bankpayacno = rs.getString("BANKPAYACNO");
        payOrder.bankpayusernm = rs.getString("BANKPAYUSERNM");
        payOrder.bankerror = rs.getString("BANKERROR");
        payOrder.backerror = rs.getString("BACKERROR");
        payOrder.merno = rs.getString("MERNO");
        payOrder.prdordno = rs.getString("PRDORDNO");
        payOrder.notifyurl = rs.getString("NOTIFYURL");
        payOrder.custId = rs.getString("CUST_ID");
        payOrder.payret = rs.getString("PAYRET");
        payOrder.prdname = rs.getString("PRDNAME");
        payOrder.merremark = rs.getString("MERREMARK");
        payOrder.bnkdat = rs.getTimestamp("BNKDAT");
        payOrder.bnkjnl = rs.getString("BNKJNL");
        payOrder.signmsg = rs.getString("SIGNMSG");
        payOrder.bustyp = rs.getString("BUSTYP");
        payOrder.bnkmerno = rs.getString("BNKMERNO");
        payOrder.tracenumber = rs.getString("TRACENUMBER");
        payOrder.tracetime = rs.getTimestamp("TRACETIME");
        payOrder.cbatno = rs.getString("CBATNO");
        payOrder.srefno = rs.getString("SREFNO");
        payOrder.termno = rs.getString("TERMNO");
        payOrder.trantyp = rs.getString("TRANTYP");
        payOrder.termtyp = rs.getString("TERMTYP");
        payOrder.credno = rs.getString("CREDNO");
        payOrder.versionno = rs.getString("VERSIONNO");
        payOrder.payacno = rs.getString("PAYACNO");
        payOrder.filed1 = rs.getString("FILED1");
        payOrder.filed2 = rs.getString("FILED2");
        payOrder.filed3 = rs.getString("FILED3");
        payOrder.filed4 = rs.getString("FILED4");
        payOrder.filed5 = rs.getString("FILED5");
        try{payOrder.createtime = rs.getTimestamp("CREATETIME");} catch (Exception e) {}
        try{payOrder.storeName = rs.getString("STORE_NAME");} catch (Exception e) {}
        try{payOrder.prdordstatus = rs.getString("PRDORDSTATUS");} catch (Exception e) {}
        try{payOrder.stlsts = rs.getString("STLSTS");} catch (Exception e) {}
        try{payOrder.rftotalamt = rs.getLong("RFTOTALAMT");} catch (Exception e) {}
        payOrder.payChannel = rs.getString("PAY_CHANNEL");
        payOrder.bankCardType = rs.getString("BANK_CARD_TYPE");
        payOrder.notifyMerFlag = rs.getString("NOTIFY_MER_FLAG");
        payOrder.notifyMerTime = rs.getTimestamp("NOTIFY_MER_TIME");
        payOrder.agentFee = rs.getLong("AGENT_FEE");
        payOrder.agentStlBatchNo = rs.getString("AGENT_STL_BATCH_NO");
        payOrder.channelFee = rs.getLong("CHANNEL_FEE");
        return payOrder;
    }
    public String addPayOrder(PayOrder payOrder) throws Exception {
        String sql = "insert into PAY_ORDER("+
            "PAYORDNO," + 
            "ORDSTATUS," + 
            "PRDORDTYPE," + 
            "PAYTYPE," + 
            "MULTYPE," + 
            "BANKCOD," + 
            "PAYBNKCOD," + 
            "BANKJRNNO," + 
            "TXCCY," + 
            "TXAMT," + 
            "ACCAMT," + 
            "MULAMT," + 
            "FEE," + 
            "NETRECAMT," + 
            "BANKPAYACNO," + 
            "BANKPAYUSERNM," + 
            "BANKERROR," + 
            "BACKERROR," + 
            "MERNO," + 
            "PRDORDNO," + 
            "NOTIFYURL," + 
            "CUST_ID," + 
            "PAYRET," + 
            "PRDNAME," + 
            "MERREMARK," + 
            "BNKJNL," + 
            "SIGNMSG," + 
            "BUSTYP," + 
            "BNKMERNO," + 
            "TRACENUMBER," + 
            "CBATNO," + 
            "SREFNO," + 
            "TERMNO," + 
            "TRANTYP," + 
            "TERMTYP," + 
            "CREDNO," + 
            "VERSIONNO," + 
            "PAYACNO,CREATETIME," + 
            "PAY_CHANNEL," + 
            "BANK_CARD_TYPE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
            "to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
        log.info(sql);
//        log.info(payOrder.toString());
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payOrder.payordno);
            ps.setString(n++,payOrder.ordstatus);
            ps.setString(n++,payOrder.prdordtype);
            ps.setString(n++,payOrder.paytype);
            ps.setString(n++,payOrder.multype);
            ps.setString(n++,payOrder.bankcod);
            ps.setString(n++,payOrder.paybnkcod);
            ps.setString(n++,payOrder.bankjrnno);
            ps.setString(n++,payOrder.txccy);
            ps.setLong(n++,payOrder.txamt);
            ps.setLong(n++,payOrder.accamt);
            ps.setLong(n++,payOrder.mulamt);
            ps.setLong(n++,payOrder.fee);
            ps.setLong(n++,payOrder.netrecamt);
            ps.setString(n++,payOrder.bankpayacno);
            ps.setString(n++,payOrder.bankpayusernm);
            ps.setString(n++,payOrder.bankerror);
            ps.setString(n++,payOrder.backerror);
            ps.setString(n++,payOrder.merno);
            ps.setString(n++,payOrder.prdordno);
            ps.setString(n++,payOrder.notifyurl);
            ps.setString(n++,payOrder.custId);
            ps.setString(n++,payOrder.payret);
            ps.setString(n++,payOrder.prdname);
            ps.setString(n++,payOrder.merremark);
            ps.setString(n++,payOrder.bnkjnl);
            ps.setString(n++,payOrder.signmsg);
            ps.setString(n++,payOrder.bustyp);
            ps.setString(n++,payOrder.bnkmerno);
            ps.setString(n++,payOrder.tracenumber);
            ps.setString(n++,payOrder.cbatno);
            ps.setString(n++,payOrder.srefno);
            ps.setString(n++,payOrder.termno);
            ps.setString(n++,payOrder.trantyp);
            ps.setString(n++,payOrder.termtyp);
            ps.setString(n++,payOrder.credno);
            ps.setString(n++,payOrder.versionno);
            ps.setString(n++,payOrder.payacno);
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payOrder.createtime));
            ps.setString(n++,payOrder.payChannel);
            ps.setString(n++,payOrder.bankCardType);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * Set query condition sql.
     * @param payOrder
     * @return
     */
    private String setPayOrderSql(PayOrder payOrder) {
        StringBuffer sql = new StringBuffer();
        if(payOrder.payacno != null && payOrder.payacno.length() !=0) {//卡号
        	sql.append(" ord.PAYACNO = ? and ");
        }
        if(payOrder.merno != null && payOrder.merno.length() !=0) {
            sql.append(" ord.MERNO = ? and ");
        }
//        if(payOrder.prdname != null && payOrder.prdname.length() !=0) {
//            sql.append(" ord.PRDNAME like ? and ");
//        }
        if(payOrder.paytype != null && payOrder.paytype.length() !=0) {
            sql.append(" ord.PAYTYPE = ? and ");
        }
//        if(payOrder.termtyp != null && payOrder.termtyp.length() !=0) {
//            sql.append(" ord.TERMTYP = ? and ");
//        }
//        if(payOrder.stlsts != null && payOrder.stlsts.length() !=0) {
//        	sql.append(" PAY_PRODUCT_ORDER.STLSTS = ? and ");
//        }
        if(payOrder.notifyMerFlag != null && payOrder.notifyMerFlag.length() !=0) {
        	sql.append(" NOTIFY_MER_FLAG = ? and ");
        }
//        if(payOrder.storeName != null && payOrder.storeName.length() !=0) {
//            sql.append(" PAY_MERCHANT.STORE_NAME like ? and ");
//        }
        if(payOrder.txamtstart != null && payOrder.txamtstart.length() !=0) {
            sql.append(" ord.TXAMT >= ? and ");
        }
        if(payOrder.txamtend != null && payOrder.txamtend.length() !=0) {
            sql.append(" ord.TXAMT <= ? and ");
        }
//        if(payOrder.prdordstatus != null && payOrder.prdordstatus.length() !=0) {
//            sql.append(" PAY_PRODUCT_ORDER.ORDSTATUS = ? and ");
//        }
        if(payOrder.payChannel != null && payOrder.payChannel.length() !=0) {
        	sql.append(" PAY_CHANNEL = ? and ");
        }
        if(payOrder.bankcod != null && payOrder.bankcod.length() !=0) {
        	sql.append(" BANKCOD = ? and ");
        }
        if(payOrder.ordstatus != null && payOrder.ordstatus.length() !=0) {
        	sql.append(" ord.ORDSTATUS = ? and ");
        }
        if(payOrder.createtimeStart != null && payOrder.createtimeStart.length() !=0) {
            sql.append(" ord.CREATETIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and  ");
        }
//        if(payOrder.createtimeEnd != null && payOrder.createtimeEnd.length() !=0 ) {
            sql.append(" ord.CREATETIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
//        }
        if(payOrder.prdordno != null && payOrder.prdordno.length() !=0) {
            sql.append(" ord.PRDORDNO = ? and ");
        }
        if(payOrder.payordno != null && payOrder.payordno.length() !=0) {
            sql.append(" ord.PAYORDNO = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payOrder
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayOrderParameter(PreparedStatement ps,PayOrder payOrder,int n)throws SQLException {
    	if(payOrder.payacno != null && payOrder.payacno.length() !=0) {
    		ps.setString(n++,payOrder.payacno);
    	}
        if(payOrder.merno != null && payOrder.merno.length() !=0) {
            ps.setString(n++,payOrder.merno);
        }
//        if(payOrder.prdname != null && payOrder.prdname.length() !=0) {
//            ps.setString(n++,"%"+payOrder.prdname+"%");
//        }
        if(payOrder.paytype != null && payOrder.paytype.length() !=0) {
        	ps.setString(n++,payOrder.paytype);
        }
//        if(payOrder.termtyp != null && payOrder.termtyp.length() !=0) {
//        	ps.setString(n++,payOrder.termtyp);
//        }
//        if(payOrder.stlsts != null && payOrder.stlsts.length() !=0) {
//        	ps.setString(n++,payOrder.stlsts);
//        }
        if(payOrder.notifyMerFlag != null && payOrder.notifyMerFlag.length() !=0) {
        	ps.setString(n++,payOrder.notifyMerFlag);
        }
        //设置商户名称的值
//        if(payOrder.storeName != null && payOrder.storeName.length() !=0) {
//            ps.setString(n++,"%"+payOrder.storeName+"%");
//        }
        if(payOrder.txamtstart != null && payOrder.txamtstart.length() !=0) {
            ps.setString(n++,String.valueOf(Float.parseFloat(payOrder.txamtstart)*100f));
        }
        if(payOrder.txamtend != null && payOrder.txamtend.length() !=0) {
            ps.setString(n++,String.valueOf(Float.parseFloat(payOrder.txamtend)*100f));
        }
//        if(payOrder.prdordstatus != null && payOrder.prdordstatus.length() !=0) {
//            ps.setString(n++,payOrder.prdordstatus);
//        }
        if(payOrder.payChannel != null && payOrder.payChannel.length() !=0) {
        	ps.setString(n++,payOrder.payChannel);
        }
        if(payOrder.bankcod != null && payOrder.bankcod.length() !=0) {
        	ps.setString(n++,payOrder.bankcod);
        }
        if(payOrder.ordstatus != null && payOrder.ordstatus.length() !=0) {
        	ps.setString(n++,payOrder.ordstatus);
        }
        if(payOrder.createtimeStart != null && payOrder.createtimeStart.trim().length() !=0) {
        	String hms="00:00:00";
        	if(payOrder.createHMSStart != null&&payOrder.createHMSStart.trim().length()!=0)hms=payOrder.createHMSStart.trim()+":00";
        	ps.setString(n++,payOrder.createtimeStart.trim()+" "+hms);
        }
        if(payOrder.createtimeEnd != null && payOrder.createtimeEnd.trim().length() !=0 ) {
        	String hms="23:59:59";
        	if(payOrder.createHMSEnd != null&&payOrder.createHMSEnd.trim().length()!=0)hms=payOrder.createHMSEnd.trim()+":59";
        	ps.setString(n++, payOrder.createtimeEnd.trim()+" "+hms);
        } else ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if(payOrder.prdordno != null && payOrder.prdordno.length() !=0) {
            ps.setString(n++,payOrder.prdordno);
        }
        if(payOrder.payordno != null && payOrder.payordno.length() !=0) {
            ps.setString(n++,payOrder.payordno);
        }
        return n;
    }
    /**
     * Get records count.
     * @param flag 
     * @param payOrder
     * @return
     * @throws Exception 
     */
    public int getPayOrderCount(String flag, PayOrder payOrder) throws Exception {
        String sqlCon = setPayOrderSql(payOrder);
        String sql ="SELECT count(ord.payordno)recordCount from PAY_ORDER ord " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
        	else con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayOrderParameter(ps,payOrder,n);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("recordCount");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return 0;
    }
    /**
     * Get records list.
     * @param flag 
     * @param payOrder
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayOrderList(String flag, PayOrder payOrder,int page,int rows,String sort,String order,Map<String,String> custMap) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayOrderSql(payOrder);
        String custNameTmp = "";
        String sortOrder = sort == null || sort.length()==0?" order by CREATETIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
/*        String sql =
        		" SELECT * " +
        		"FROM" +
	        		"( SELECT rownum rowno,tmp.*" +
	        		" FROM" +
		        		" (SELECT ord.*,PAY_MERCHANT.STORE_NAME,PAY_PRODUCT_ORDER.ORDSTATUS PRDORDSTATUS,PAY_PRODUCT_ORDER.STLSTS,PAY_PRODUCT_ORDER.RFTOTALAMT" +
		        		" FROM PAY_ORDER ord" +
		        		"  LEFT JOIN (select CUST_ID,STORE_NAME from PAY_MERCHANT)PAY_MERCHANT" +
		        		" ON ord.MERNO = PAY_MERCHANT.CUST_ID" +
		        		" LEFT JOIN (select MERNO,PRDORDNO,ORDSTATUS,RFTOTALAMT,STLSTS from PAY_PRODUCT_ORDER)PAY_PRODUCT_ORDER" +
		        		" ON ord.MERNO     = PAY_PRODUCT_ORDER.MERNO" +
		        		" AND ord.PRDORDNO = PAY_PRODUCT_ORDER.PRDORDNO" +
		        		  (sqlCon.length()==0?"":" where "+ sqlCon)  +
		        		" ORDER BY ord.CREATETIME DESC" +
		        		" ) tmp" +
		        		" ) tmp1" +
		        		" WHERE rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder;*/
        String sql =
        		"SELECT ord.*,PAY_MERCHANT.STORE_NAME" +
		        		" FROM (" +
        					"  select * from("+
        					"SELECT tmp.*,rownum rn"+
        					"  FROM"+
        					"    (SELECT * FROM PAY_ORDER ord "+(sqlCon.length()==0?"":" where "+ sqlCon)+
		        				"  ORDER BY ord.CREATETIME DESC )tmp"+
        					"  )tmp1"+
        					"  WHERE rn> "+((page-1)*rows)+ ""+
        					"  AND rn <= " + (page*rows)+
		        			") ord" +
		        		"  LEFT JOIN (select CUST_ID,STORE_NAME from PAY_MERCHANT)PAY_MERCHANT" +
		        		" ON ord.MERNO = PAY_MERCHANT.CUST_ID" + sortOrder;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<PayOrder> list = new ArrayList<PayOrder>();
        try {
        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
        	else con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayOrderParameter(ps,payOrder,n);
            rs = ps.executeQuery();
            String tmp = "";
            Map<String,PayOrder> mapTmp = new HashMap<String,PayOrder>();
            //pay_order 表查询
            while(rs.next()){
            	PayOrder o = getPayOrderValue(rs);
            	tmp=tmp+"(merno='"+o.merno+"' and PRDORDNO='"+o.prdordno+"') or ";
            	mapTmp.put(o.merno+","+o.prdordno, o);
                list.add(o);
                if(o.custId!=null&&o.custId.length()>0){
                	if(custMap.get(o.custId)==null){
                		custNameTmp = custNameTmp+" CUST_ID='"+o.custId+"' or ";
                		custMap.put(o.custId,"");
                	}
                }
            }
            //pay_product_order 表查询
            if(tmp.length()>0){
            	sql = "select merno,PRDORDNO,STLSTS from PAY_PRODUCT_ORDER where "+ tmp.substring(0,tmp.length()-4);
            	log.info(sql);
            	rs.close();
            	ps.close();
            	ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
            	while(rs.next()){
            		mapTmp.get(rs.getString("merno")+","+rs.getString("PRDORDNO")).stlsts=rs.getString("STLSTS");
            	}
            }
            if(custNameTmp.length()>0){
            	rs.close();
            	ps.close();
            	custNameTmp = custNameTmp.substring(0,custNameTmp.length()-4);
            	sql = "select CUST_ID,STORE_NAME CUST_NAME from PAY_MERCHANT_ROOT where " + custNameTmp+
                		" union select * from(select USER_ID CUST_ID,REAL_NAME CUST_NAME from PAY_TRAN_USER_INFO) where "+custNameTmp;
            	ps = con.prepareStatement(sql);
            	log.info(sql);
            	rs = ps.executeQuery();
                while(rs.next())custMap.put(rs.getString("CUST_ID"),rs.getString("CUST_NAME"));
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
     * Get records count.
     * @param payOrder
     * @param merIdList 
     * @return
     */
    public int getBussMemOrderCount(String flag, PayOrder payOrder, List<String> merIdList) {
    	if(merIdList.size()==0) return 0;
        String sqlCon = setPayOrderSql(payOrder);
        String tmp ="";
        for (int i = 0; i < merIdList.size(); i++) {
			if( i != merIdList.size()-1 ) tmp += merIdList.get(i)+",";
			else tmp += merIdList.get(i);
		}
//        String sql ="select count(rownum) recordCount from" +
//        		" (SELECT ord.*,PAY_MERCHANT.STORE_NAME,PAY_PRODUCT_ORDER.ORDSTATUS PRDORDSTATUS,PAY_PRODUCT_ORDER.STLSTS,PAY_PRODUCT_ORDER.RFTOTALAMT" +
//        		" FROM (select * from pay_order where merno in ("+ tmp +")) ord" +
//        		"  LEFT JOIN (select CUST_ID,STORE_NAME from PAY_MERCHANT)PAY_MERCHANT" +
//        		" ON ord.MERNO = PAY_MERCHANT.CUST_ID" +
//        		" LEFT JOIN (select MERNO,PRDORDNO,ORDSTATUS,RFTOTALAMT,STLSTS from PAY_PRODUCT_ORDER)PAY_PRODUCT_ORDER" +
//        		" ON ord.MERNO     = PAY_PRODUCT_ORDER.MERNO" +
//        		" AND ord.PRDORDNO = PAY_PRODUCT_ORDER.PRDORDNO" +
//        		(sqlCon.length()==0?"":" where "+ sqlCon)+
//        		" ORDER BY ord.CREATETIME DESC" +
//        		" ) tmp";
//        String sql ="SELECT count(ord.payordno)recordCount from PAY_ORDER ord " +(sqlCon.length()==0?"":" where "+ sqlCon);
        String sql ="SELECT count(payordno) recordCount FROM pay_order ord WHERE merno IN ("+ tmp +")"+(sqlCon.length()==0?"":" and "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
        	else con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayOrderParameter(ps,payOrder,n);
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
     * @param payOrder
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @param merIdList 
     * @return
     * @throws Exception
     */
    public List<PayOrder> getBussMemOrderList(String flag, PayOrder payOrder,int page,int rows,
    		String sort,String order,Map<String,String> custMap, List<String> merIdList) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayOrderSql(payOrder);
        String custNameTmp = "";
        String sortOrder = sort == null || sort.length()==0?" order by CREATETIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String tmp ="";
        List<PayOrder> list = new ArrayList<PayOrder>();
        if(merIdList.size()==0)return list;
        for (int i = 0; i < merIdList.size(); i++) {
			if( i != merIdList.size()-1 ) tmp = tmp + "'"+merIdList.get(i)+"',";
			else tmp = tmp+"'"+merIdList.get(i)+"'";
		}
//        String sql =
//        		" SELECT *" +
//        		"FROM" +
//	        		"( SELECT rownum rowno,tmp.*" +
//	        		" FROM" +
//		        		" (SELECT ord.*,PAY_MERCHANT.STORE_NAME,PAY_PRODUCT_ORDER.ORDSTATUS PRDORDSTATUS,PAY_PRODUCT_ORDER.STLSTS,PAY_PRODUCT_ORDER.RFTOTALAMT" +
//		        		" FROM (select * from pay_order where merno in ("+ tmp +")) ord" +
//		        		"  LEFT JOIN (select CUST_ID,STORE_NAME from PAY_MERCHANT)PAY_MERCHANT" +
//		        		" ON ord.MERNO = PAY_MERCHANT.CUST_ID" +
//		        		" LEFT JOIN (select MERNO,PRDORDNO,ORDSTATUS,RFTOTALAMT,STLSTS from PAY_PRODUCT_ORDER)PAY_PRODUCT_ORDER" +
//		        		" ON ord.MERNO     = PAY_PRODUCT_ORDER.MERNO" +
//		        		" AND ord.PRDORDNO = PAY_PRODUCT_ORDER.PRDORDNO" +
//		        		  (sqlCon.length()==0?"":" where "+ sqlCon)  +
//		        		" ORDER BY ord.CREATETIME DESC" +
//		        		" ) tmp" +
//		        		" ) tmp1" +
//		        		" WHERE rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + sortOrder;
        String sql =
        		"SELECT ord.*,PAY_MERCHANT.STORE_NAME" +
		        		" FROM (" +
        					"  select * from("+
        					"SELECT tmp.*,rownum rn"+
        					"  FROM"+
        					"    (SELECT * FROM PAY_ORDER ord  where merno in ("+ tmp +") "+(sqlCon.length()==0?"":" and "+ sqlCon)+
		        				"  ORDER BY ord.CREATETIME DESC )tmp"+
        					"  )tmp1"+
        					"  WHERE rn> "+((page-1)*rows)+ ""+
        					"  AND rn <= " + (page*rows)+
		        			") ord" +
		        		"  LEFT JOIN (select CUST_ID,STORE_NAME from PAY_MERCHANT)PAY_MERCHANT" +
		        		" ON ord.MERNO = PAY_MERCHANT.CUST_ID" + sortOrder;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
        	else con = connection();
        	
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayOrderParameter(ps,payOrder,n);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayOrder o = getPayOrderValue(rs); 
                list.add(o);
                if(o.custId!=null&&o.custId.length()>0){
                	if(custMap.get(o.custId)==null){
                		custNameTmp = custNameTmp+" CUST_ID='"+o.custId+"' or ";
                		custMap.put(o.custId,"");
                	}
                }
            }
            if(custNameTmp.length()>0){
            	rs.close();
            	ps.close();
            	custNameTmp = custNameTmp.substring(0,custNameTmp.length()-4);
            	sql = "select CUST_ID,STORE_NAME CUST_NAME from PAY_MERCHANT_ROOT where " + custNameTmp+
                		" union select * from(select USER_ID CUST_ID,REAL_NAME CUST_NAME from PAY_TRAN_USER_INFO) where "+custNameTmp;
            	ps = con.prepareStatement(sql);
            	log.info(sql);
            	rs = ps.executeQuery();
                while(rs.next())custMap.put(rs.getString("CUST_ID"),rs.getString("CUST_NAME"));
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
     * 根据订单号查询该订单
     * @param flag 
     * @param payOrder
     * @return
     */
	public PayOrder getPayOrderDetail(String flag, PayOrder payOrder) {
		  
        String sql = "SELECT tab1.PAYORDNO,tab1.CREATETIME,tab1.TXAMT,tab1.PAYTYPE,tab1.PRDORDTYPE,tab1.ORDSTATUS,tab1.ACTDAT,tab1.bankpayacno,tab1.bankpayusernm,tab2.NOTIFYURL,tab2.RETURL,tab2.RECEIVE_PAY_NOTIFY_URL,tab2.mobile,tab2.credential_no,tab2.signature,tab2.verifystring "
        		+ " FROM pay_order tab1 LEFT JOIN PAY_PRODUCT_ORDER tab2"
        		+ " ON tab1.merno = tab2.merno AND tab1.prdordno = tab2.prdordno "
        		+" where tab1.PAYORDNO = ?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, payOrder.getPayordno());
            rs = ps.executeQuery();
            if (rs.next()) {
            	PayOrder order=new PayOrder();
            	setPayOrderValue(order,rs);
                return order;
            } else {
            	close(rs, ps, con);
            	con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
            	ps = con.prepareStatement(sql);
                ps.setString(1, payOrder.getPayordno());
                rs = ps.executeQuery();
                if (rs.next()) {
                	PayOrder order=new PayOrder();
                	setPayOrderValue(order,rs);
                    return order;
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	private void setPayOrderValue(PayOrder order,ResultSet rs) throws SQLException{
		order.setPayordno(rs.getString("PAYORDNO"));//支付订单号
    	order.setCreatetime(rs.getDate("CREATETIME"));//订单时间
    	order.setTxamt(rs.getLong("TXAMT"));//订单金额
    	order.setPaytype(rs.getString("PAYTYPE"));//支付方式
    	order.setPrdordtype(rs.getString("PRDORDTYPE"));//订单类型
    	order.setOrdstatus(rs.getString("ORDSTATUS"));//支付状态
    	order.setActdat(rs.getDate("ACTDAT"));//支付时间
    	order.setNotifyurl(rs.getString("NOTIFYURL"));//通知地址
    	order.setReturl(rs.getString("RETURL"));//返回地址
    	order.setReceivePayNotifyUrl(rs.getString("RECEIVE_PAY_NOTIFY_URL"));//快捷通知
    	order.setBankpayacno(rs.getString("BANKPAYACNO"));
    	order.setBankpayusernm(rs.getString("BANKPAYUSERNM"));
    	order.setMobile(rs.getString("MOBILE"));
    	order.setCredentialNo(rs.getString("CREDENTIAL_NO"));
    	order.setSignature(rs.getString("SIGNATURE"));
    	order.setVerifystring(rs.getString("VERIFYSTRING"));
	}
	
	/**
	 * 根据订单号查询该订单
	 * @param flag 
	 * @param payOrder
	 * @return
	 */
	public PayOrder getPayOrderDetailForAll(PayOrder payOrder) {
		String sql = "select * from PAY_ORDER tmp where tmp.PAYORDNO = ?";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, payOrder.getPayordno());
			rs = ps.executeQuery();
			if (rs.next()) {
				return getPayOrderValue(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, con);
		}
		return null;
	}
	/**
	 * 查询订单的总金额
	 * @param flag 
	 * @param payOrder
	 * @return
	 */
    public Long [] getTotalOrderMoney(String flag, PayOrder payOrder) {
    	Long l1 = null,l2 = null,l3 = null,l4 = null;
    	String sqlCon = setPayOrderSql(payOrder);
        String sql1 = 
        		" SELECT SUM(TXAMT) totalOrderMoney FROM PAY_ORDER ord " +(sqlCon.length()==0?"":" where "+ sqlCon);
        String sql2 = 
        		" SELECT SUM(FEE) totalFeeMoney FROM PAY_ORDER ord " +(sqlCon.length()==0?"":" where "+ sqlCon);
        String sql3 = 
        		" SELECT SUM(CHANNEL_FEE) totalChannelFee FROM PAY_ORDER ord " +(sqlCon.length()==0?"":" where "+ sqlCon);
        String sql4 = 
        		" SELECT SUM(NETRECAMT) totalAppMoney FROM PAY_ORDER ord " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql1);
        log.info(sql2);
        log.info(sql3);
        log.info(sql4);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
        	else con = connection();
            ps = con.prepareStatement(sql1);
            setPayOrderParameter(ps,payOrder,1);
            rs = ps.executeQuery();
            if (rs.next()) l1 = rs.getLong("totalOrderMoney");
            rs.close();
            ps.close();
            ps = con.prepareStatement(sql2);
            setPayOrderParameter(ps,payOrder,1);
            rs = ps.executeQuery();
            if (rs.next()) l2 = rs.getLong("totalFeeMoney");
            rs.close();
            ps.close();
            ps = con.prepareStatement(sql3);
            setPayOrderParameter(ps,payOrder,1);
            rs = ps.executeQuery();
            if (rs.next()) l3 = rs.getLong("totalChannelFee");
            rs.close();
            ps.close();
            ps = con.prepareStatement(sql4);
            setPayOrderParameter(ps,payOrder,1);
            rs = ps.executeQuery();
            if (rs.next()) l4 = rs.getLong("totalAppMoney");
            return new Long[]{l1==null?0l:l1,l2==null?0l:l2,l3==null?0l:l3,l4==null?0l:l4};
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return new Long[]{0l,0l,0l,0l};
    }
	/**
	 * 查询业务员订单的总金额
	 * @param payOrder
	 * @param merIdList 
	 * @return
	 */
    public Long [] getTotalBussMemOrderMoney(String flag, PayOrder payOrder, List<String> merIdList) {
    	if(merIdList.size()==0)return new Long[]{0l,0l,0l};
    	Long l1 = null,l2 = null,l3 = null;
    	String sqlCon = setPayOrderSql(payOrder);
    	String tmp ="";
        for (int i = 0; i < merIdList.size(); i++) {
			if( i != merIdList.size()-1 ) tmp += merIdList.get(i)+",";
			else tmp += merIdList.get(i);
		}
        String sql = 
        		"SELECT SUM(ord.TXAMT) totalOrderMoney,SUM(ord.FEE) totalFeeMoney,SUM(ord.NETRECAMT) totalAppMoney from pay_order ord where merno in ("+ tmp +") "
        					+(sqlCon.length()==0?"":" and "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
        	else con = connection();
        	
            ps = con.prepareStatement(sql);
            setPayOrderParameter(ps,payOrder,1);
            rs = ps.executeQuery();
            if (rs.next()) {
            	l1 = rs.getLong("totalOrderMoney");
            	l2 = rs.getLong("totalFeeMoney");
            	l3 = rs.getLong("totalAppMoney");
            }
            return new Long[]{l1==null?0l:l1,l2==null?0l:l2,l3==null?0l:l3};
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return new Long[]{0l,0l,0l};
    }
	/**
     * excel报表导出
	 * @param flag 
     * @param payOrder
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayOrderList(String flag, PayOrder payOrder,Long start,Long end) throws Exception{
        String sqlCon = setPayOrderSql(payOrder);
        String sql =
        		" SELECT *" +
        		"FROM" +
	        		"( SELECT rownum rowno,tmp.*" +
	        		" FROM" +
		        		" (SELECT ord.*,PAY_MERCHANT.STORE_NAME,PAY_PRODUCT_ORDER.ORDSTATUS PRDORDSTATUS,PAY_PRODUCT_ORDER.STLSTS,PAY_PRODUCT_ORDER.RFTOTALAMT" +
		        		" FROM PAY_ORDER ord" +
		        		"  LEFT JOIN (select CUST_ID,STORE_NAME from PAY_MERCHANT)PAY_MERCHANT" +
		        		" ON ord.MERNO = PAY_MERCHANT.CUST_ID" +
		        		" LEFT JOIN (select MERNO,PRDORDNO,ORDSTATUS,STLSTS,RFTOTALAMT from PAY_PRODUCT_ORDER)PAY_PRODUCT_ORDER" +
		        		" ON ord.MERNO     = PAY_PRODUCT_ORDER.MERNO" +
		        		" AND ord.PRDORDNO = PAY_PRODUCT_ORDER.PRDORDNO" +
		        		  (sqlCon.length()==0?"":" where "+ sqlCon)  +
		        		" ORDER BY ord.CREATETIME DESC" +
		        		" ) tmp" +
		        		" ) tmp1" +
		        		" WHERE rowno > "+start+ " and rowno<= " + end + " order by CREATETIME desc";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
        	else con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayOrderParameter(ps,payOrder,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayOrderValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
	public List getStlPayOrderList(PayMerchantSettlement stl, long start, long end) {
		String sql ="select * from(select rownum rowno,tmp.* from(" +
				"select * from PAY_ORDER_HISTORY where ORDSTATUS='01' and MERNO='"+stl.stlMerId+"' " +
		 		" and CREATETIME>=to_date(?,'yyyy-mm-dd hh24:mi:ss') and CREATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss') order by PAYORDNO " +
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
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(stl.stlFromTime)+" 00:00:00");
            ps.setString(n++, new SimpleDateFormat("yyyy-MM-dd").format(stl.stlToTime)+" 23:59:59");
            rs = ps.executeQuery();
            while (rs.next()) {
            	list.add(getPayOrderValue(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return list;
	}
	
	/**
	 * 根据订单编号，更新订单银行信息
	 * @param orderCode
	 * @param bankCode
	 * @return
	 */
	public void updateOrderForBanks(PayOrder order)throws Exception {
		String sql = "UPDATE PAY_ORDER SET BANK_CARD_TYPE=?,BANKCOD = ?,PAY_CHANNEL=? WHERE MERNO=? and PRDORDNO = ? ";
        log.info(sql);
        log.info("BANK_CARD_TYPE="+order.bankCardType+";BANKCOD="+order.bankcod+";PAY_CHANNEL="+order.payChannel+";MERNO="+order.merno+";PRDORDNO="+order.prdordno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++, order.bankCardType);
            ps.setString(n++, order.bankcod);
            ps.setString(n++, order.payChannel==null?"":order.payChannel);
            ps.setString(n++, order.merno);
            ps.setString(n++, order.prdordno);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	
	/**
	 * 根据订单号，查询订单信息
	 * @param merchantId
	 * @param merchantOrderId
	 * @return
	 * @throws Exception
	 */
	public PayOrder getPayOrderByMerInfo(String merchantId,String merchantOrderId)throws Exception {
		String sql = "SELECT * FROM PAY_ORDER WHERE MERNO=? and PRDORDNO = ? ";
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
            if (rs.next())return getPayOrderValue(rs);
            else {
            	rs.close();
            	ps.close();
            	sql = "SELECT * FROM PAY_ORDER@pay_db_bak WHERE MERNO=? and PRDORDNO = ?";
            	ps = con.prepareStatement(sql);
                ps.setString(1, merchantId);
                ps.setString(2, merchantOrderId);
                rs = ps.executeQuery();
                if (rs.next())return getPayOrderValue(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
		return null;
	}
	public void updateOrderForQuickPayH5(PayRequest payRequest) throws Exception {
		String sqlPay = "UPDATE PAY_ORDER SET BANK_CARD_TYPE=?,BANKCOD = ?,PAY_CHANNEL=?,PAYACNO=?,BANKPAYACNO=?,BANKPAYUSERNM=? WHERE MERNO=? and PRDORDNO = ? ";
		String sqlPrd = "UPDATE PAY_PRODUCT_ORDER SET CREDENTIAL_TYPE=?,CREDENTIAL_NO=?,MOBILE = ? WHERE MERNO=? and PRDORDNO = ? ";
		log.info(sqlPay);
		log.info("BANK_CARD_TYPE="+payRequest.payOrder.bankCardType+";BANKCOD="+payRequest.payOrder.bankcod+";PAY_CHANNEL="+payRequest.payOrder.payChannel+
				";PAYACNO="+payRequest.cardNo+";BANKPAYACNO="+payRequest.cardNo+";BANKPAYUSERNM="+payRequest.userName+";MERNO="+payRequest.merchantId+
				";PRDORDNO="+payRequest.merchantOrderId);
        
        Connection con = null;
        PreparedStatement ps = null;
        try {
        	 con = connection();
             ps = con.prepareStatement(sqlPay);
             int n=1;
             ps.setString(n++, payRequest.payOrder.bankCardType);
             ps.setString(n++, payRequest.payOrder.bankcod);
             ps.setString(n++, payRequest.payOrder.payChannel==null?"":payRequest.payOrder.payChannel);
             ps.setString(n++, payRequest.cardNo);
             ps.setString(n++, payRequest.cardNo);
             ps.setString(n++, payRequest.userName);
             ps.setString(n++, payRequest.merchantId);
             ps.setString(n++, payRequest.merchantOrderId);
             ps.executeUpdate();
             ps.close();
             ps = con.prepareStatement(sqlPrd);

             log.info(sqlPrd);
             log.info("CREDENTIAL_TYPE="+payRequest.credentialType+";CREDENTIAL_NO="+payRequest.credentialNo+";MOBILE="+payRequest.userMobileNo+";MERNO="+payRequest.merchantId+
            		 ";PRDORDNO="+payRequest.merchantOrderId);

             log.info(sqlPrd);
             log.info("CREDENTIAL_TYPE="+payRequest.credentialType+";CREDENTIAL_NO="+payRequest.credentialNo+";MOBILE="+payRequest.userMobileNo+";MERNO="+
            		 payRequest.merchantId+";PRDORDNO="+payRequest.merchantOrderId);

             n=1;
             ps.setString(n++, payRequest.credentialType);
             ps.setString(n++, payRequest.credentialNo);
             ps.setString(n++, payRequest.userMobileNo);
             ps.setString(n++, payRequest.merchantId);
             ps.setString(n++, payRequest.merchantOrderId);
             ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	public void updateOrderError(PayOrder payOrder) throws Exception {
		String sqlPay = "UPDATE PAY_ORDER SET BANKERROR=? WHERE MERNO=? and PRDORDNO = ? ";
		log.info(sqlPay);
		log.info("BANKERROR="+payOrder.bankerror+";MERNO="+payOrder.merno+";PRDORDNO="+payOrder.prdordno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
        	 con = connection();
             ps = con.prepareStatement(sqlPay);
             int n=1;
             ps.setString(n++, payOrder.bankerror);
             ps.setString(n++, payOrder.merno);
             ps.setString(n++, payOrder.prdordno);
             ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	/**
	 * 更新银行流水号。
	 * @param payOrder
	 * @throws Exception
	 */
	public void updateOrderBankjrnno(PayOrder payOrder) throws Exception {
		String sqlPay = "UPDATE PAY_ORDER SET BANKJRNNO=? WHERE MERNO=? and PRDORDNO = ? ";
		log.info(sqlPay);
		log.info("BANKJRNNO="+payOrder.bankjrnno+";MERNO="+payOrder.merno+";PRDORDNO="+payOrder.prdordno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
        	 con = connection();
             ps = con.prepareStatement(sqlPay);
             int n=1;
             ps.setString(n++, payOrder.bankjrnno);
             ps.setString(n++, payOrder.merno);
             ps.setString(n++, payOrder.prdordno);
             ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
	/**
	 * 修改订单清结算状态
	 * @param flag 
	 * @param payordno
	 */
	public void settlementPayProOrder(String prdordno, String flag) throws Exception{
		String sql = "update PAY_PRODUCT_ORDER set STLSTS = ? , STL_TIME = sysdate where prdordno = ? ";
		log.info(sql);
		log.info("STLSTS=2"+";prdordno="+prdordno);
        Connection con = null;
        PreparedStatement ps = null;
        try {
        	if("1".equals(flag)) con = connection(PayConstant.ORDER_SQL_NAME.get("ORDER_DB_BAK_NAME"));
        	else con = connection();
            ps = con.prepareStatement(sql);
        	int n=1;
        	ps.setString(n++,"2");
        	ps.setString(n++,prdordno);
        	ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
	}
}