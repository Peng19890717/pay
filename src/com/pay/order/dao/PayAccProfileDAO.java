package com.pay.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import util.Tools;
import java.sql.ResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.user.dao.PayTranUserInfo;
/**
 * Table PAY_ACC_PROFILE DAO. 
 * @author Administrator
 *
 */
public class PayAccProfileDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayAccProfileDAO.class);
    public static synchronized PayAccProfile getPayAccProfileValue(ResultSet rs)throws SQLException {
        PayAccProfile payAccProfile = new PayAccProfile();
        payAccProfile.id = rs.getString("ID");
        payAccProfile.payAcNo = rs.getString("PAY_AC_NO");
        payAccProfile.acType = rs.getString("AC_TYPE");
        payAccProfile.brNo = rs.getString("BR_NO");
        payAccProfile.ccy = rs.getString("CCY");
        payAccProfile.cashAcBal = rs.getLong("CASH_AC_BAL");
        payAccProfile.consAcBal = rs.getLong("CONS_AC_BAL");
        payAccProfile.minStlBalance = rs.getLong("MIN_STL_BALANCE");
        payAccProfile.frozBalance = rs.getLong("FROZ_BALANCE");
        payAccProfile.glCode = rs.getString("GL_CODE");
        payAccProfile.acStatus = rs.getString("AC_STATUS");
        payAccProfile.listStsFlg = rs.getString("LIST_STS_FLG");
        payAccProfile.resvFlg = rs.getString("RESV_FLG");
        payAccProfile.accSumNum = rs.getLong("ACC_SUM_NUM");
        payAccProfile.lstTxTime = rs.getTimestamp("LST_TX_TIME");
        return payAccProfile;
    }
    public String addPayAccProfile(PayAccProfile payAccProfile) throws Exception {
        String sql = "insert into PAY_ACC_PROFILE("+
            "ID," + 
            "PAY_AC_NO," + 
            "AC_TYPE," + 
            "BR_NO," + 
            "CCY," + 
            "CASH_AC_BAL," + 
            "CONS_AC_BAL," + 
            "MIN_STL_BALANCE," + 
            "FROZ_BALANCE," + 
            "GL_CODE," + 
            "AC_STATUS," + 
            "LIST_STS_FLG," + 
            "RESV_FLG," + 
            "ACC_SUM_NUM," + 
            "LST_TX_TIME)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'))";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payAccProfile.id);
            ps.setString(n++,payAccProfile.payAcNo);
            ps.setString(n++,payAccProfile.acType);
            ps.setString(n++,payAccProfile.brNo);
            ps.setString(n++,payAccProfile.ccy);
            ps.setLong(n++,payAccProfile.cashAcBal);
            ps.setLong(n++,payAccProfile.consAcBal);
            ps.setLong(n++,payAccProfile.minStlBalance);
            ps.setLong(n++,payAccProfile.frozBalance);
            ps.setString(n++,payAccProfile.glCode);
            ps.setString(n++,payAccProfile.acStatus);
            ps.setString(n++,payAccProfile.listStsFlg);
            ps.setString(n++,payAccProfile.resvFlg);
            ps.setLong(n++,payAccProfile.accSumNum);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfile.lstTxTime));
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
        String sql = "select * from PAY_ACC_PROFILE";
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
                list.add(getPayAccProfileValue(rs));
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
     * @param payAccProfile
     * @return
     */
    private String setPayAccProfileSql(PayAccProfile payAccProfile) {
        StringBuffer sql = new StringBuffer();
        
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payAccProfile
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayAccProfileParameter(PreparedStatement ps,PayAccProfile payAccProfile,int n)throws SQLException {
        return n;
    }
    /**
     * Get records count.
     * @param payAccProfile
     * @return
     */
    public int getPayAccProfileCount(PayAccProfile payAccProfile) {
        String sqlCon = setPayAccProfileSql(payAccProfile);
        String sql = "select count(rownum) recordCount from PAY_ACC_PROFILE " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayAccProfileParameter(ps,payAccProfile,n);
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
     * @param payAccProfile
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayAccProfileList(PayAccProfile payAccProfile,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayAccProfileSql(payAccProfile);
        String sortOrder = sort == null || sort.length()==0?" order by ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from PAY_ACC_PROFILE tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayAccProfileParameter(ps,payAccProfile,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayAccProfileValue(rs));
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
     * remove PayAccProfile
     * @param id
     * @throws Exception     
     */
    public void removePayAccProfile(String id) throws Exception {
        String sql = "delete from PAY_ACC_PROFILE where ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * 更新金额 PayAccProfile
     * @param id
     * @throws Exception     
     */
    public void updatePayAccProfileAmt(PayAccProfile payAccProfile,Long amt) throws Exception {
        String sql = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+?,LST_TX_TIME=sysdate where ID=?";
        log.info(sql);
        log.info("CASH_AC_BAL="+amt+";ID="+payAccProfile.id);
        log.info("cashAcBal:"+amt+","+"consAcBal:"+amt+","+"ID:"+payAccProfile.id);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setLong(1,amt);
            ps.setLong(2,amt);
            ps.setString(3,payAccProfile.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * 更新金额 PayAccProfile
     * @param custType
     * @param custId
     * @param amt
     * @return
     * @throws Exception
     */
    public int updatePayAccProfileAmtByCustId(String custType,String custId,Long amt) throws Exception {
        String sql = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+?,LST_TX_TIME=sysdate" +
        	" where CASH_AC_BAL+?>=0 and CONS_AC_BAL+?>=0 and AC_TYPE=? and PAY_AC_NO=?";
        log.info(sql);
        log.info("CASH_AC_BAL="+amt+";AC_TYPE="+custType+";PAY_AC_NO="+custId);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setLong(n++,amt);
            ps.setLong(n++,amt);
            ps.setLong(n++,amt);
            ps.setLong(n++,amt);
            ps.setString(n++,custType);
            ps.setString(n++,custId);
            int k = ps.executeUpdate();
            return k;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * 更新金额 PayAccProfile 余额，同时更新代付扣款标识
     * @param list
     * @param custType
     * @param custId
     * @param amt
     * @return
     * @throws Exception
     */
    public int updatePayAccProfileAmtByCustId(List<PayReceiveAndPay> list,String custType,String custId,Long amt) throws Exception {
        String sql = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+?,LST_TX_TIME=sysdate" +
        	" where CASH_AC_BAL+?>=0 and CONS_AC_BAL+?>=0 and AC_TYPE=? and PAY_AC_NO=?";
        String sql1 = "update PAY_RECEIVE_AND_PAY set DEDUCT_MONEY_FLAG='1' where id = ?";
        log.info(sql);
        log.info("CASH_AC_BAL="+amt+";AC_TYPE="+custType+";PAY_AC_NO="+custId);
        log.info(sql1);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setLong(n++,amt);
            ps.setLong(n++,amt);
            ps.setLong(n++,amt);
            ps.setLong(n++,amt);
            ps.setString(n++,custType);
            ps.setString(n++,custId);
            int k = ps.executeUpdate();
            log.info("更新"+k+"条数据");
            if(k>0){
            	ps.close();
            	ps = con.prepareStatement(sql1);
            	for(int i=0; i<list.size(); i++){
            		log.info("id="+list.get(i));
            		ps.setString(1, list.get(i).id);
            		ps.addBatch();
            	}
            	ps.executeBatch();
            }
            con.commit();
            return k;
        } catch (Exception e) {
        	con.rollback();
            e.printStackTrace();
            throw e;
        } finally {
        	con.setAutoCommit(true);
            close(null, ps, con);
        }
    }
    /**
     * detail PayAccProfile
     * @param id
     * @return PayAccProfile
     * @throws Exception
     */
    public PayAccProfile detailPayAccProfile(String id) throws Exception {
        String sql = "select * from PAY_ACC_PROFILE where PAY_AC_NO =?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayAccProfileValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * detail PayAccProfile
     * @param id
     * @return PayAccProfile
     * @throws Exception
     */
    public PayAccProfile getPayAccProfileByCustInfo(String custType,String custId) throws Exception {
        String sql = "select * from PAY_ACC_PROFILE where AC_TYPE =? and PAY_AC_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,custType);
            ps.setString(2,custId);
            rs = ps.executeQuery();
            if(rs.next())return getPayAccProfileValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayAccProfile
     * @param payAccProfile
     * @throws Exception
     */
    public void updatePayAccProfile(PayAccProfile payAccProfile) throws Exception {
        String sqlTmp = "";
        if(payAccProfile.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payAccProfile.payAcNo != null)sqlTmp = sqlTmp + " PAY_AC_NO=?,";
        if(payAccProfile.acType != null)sqlTmp = sqlTmp + " AC_TYPE=?,";
        if(payAccProfile.brNo != null)sqlTmp = sqlTmp + " BR_NO=?,";
        if(payAccProfile.ccy != null)sqlTmp = sqlTmp + " CCY=?,";
        if(payAccProfile.cashAcBal != null)sqlTmp = sqlTmp + " CASH_AC_BAL=?,";
        if(payAccProfile.consAcBal != null)sqlTmp = sqlTmp + " CONS_AC_BAL=?,";
        if(payAccProfile.minStlBalance != null)sqlTmp = sqlTmp + " MIN_STL_BALANCE=?,";
        if(payAccProfile.frozBalance != null)sqlTmp = sqlTmp + " FROZ_BALANCE=?,";
        if(payAccProfile.glCode != null)sqlTmp = sqlTmp + " GL_CODE=?,";
        if(payAccProfile.acStatus != null)sqlTmp = sqlTmp + " AC_STATUS=?,";
        if(payAccProfile.listStsFlg != null)sqlTmp = sqlTmp + " LIST_STS_FLG=?,";
        if(payAccProfile.resvFlg != null)sqlTmp = sqlTmp + " RESV_FLG=?,";
        if(payAccProfile.accSumNum != null)sqlTmp = sqlTmp + " ACC_SUM_NUM=?,";
        if(payAccProfile.lstTxTime != null)sqlTmp = sqlTmp + " LST_TX_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_ACC_PROFILE "+        
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
            if(payAccProfile.id != null)ps.setString(n++,payAccProfile.id);
            if(payAccProfile.payAcNo != null)ps.setString(n++,payAccProfile.payAcNo);
            if(payAccProfile.acType != null)ps.setString(n++,payAccProfile.acType);
            if(payAccProfile.brNo != null)ps.setString(n++,payAccProfile.brNo);
            if(payAccProfile.ccy != null)ps.setString(n++,payAccProfile.ccy);
            if(payAccProfile.cashAcBal != null)ps.setLong(n++,payAccProfile.cashAcBal);
            if(payAccProfile.consAcBal != null)ps.setLong(n++,payAccProfile.consAcBal);
            if(payAccProfile.minStlBalance != null)ps.setLong(n++,payAccProfile.minStlBalance);
            if(payAccProfile.frozBalance != null)ps.setLong(n++,payAccProfile.frozBalance);
            if(payAccProfile.glCode != null)ps.setString(n++,payAccProfile.glCode);
            if(payAccProfile.acStatus != null)ps.setString(n++,payAccProfile.acStatus);
            if(payAccProfile.listStsFlg != null)ps.setString(n++,payAccProfile.listStsFlg);
            if(payAccProfile.resvFlg != null)ps.setString(n++,payAccProfile.resvFlg);
            if(payAccProfile.accSumNum != null)ps.setLong(n++,payAccProfile.accSumNum);
            if(payAccProfile.lstTxTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccProfile.lstTxTime));
            ps.setString(n++,payAccProfile.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    
    /**
     * update updatePayAccProfileCashAcBalAndConsAcBal 修改 现金余额 账面余额
     * @param payAccProfile
     * @throws Exception
     */
    public void updatePayAccProfileCashAcBalAndConsAcBal(PayAccProfile payAccProfile) throws Exception {
        String sqlTmp = "";
        if(payAccProfile.cashAcBal != null)sqlTmp = sqlTmp + " CASH_AC_BAL=?,";
        if(payAccProfile.consAcBal != null)sqlTmp = sqlTmp + " CONS_AC_BAL=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_ACC_PROFILE "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where ID=?"; 
        log.info(sql);
        log.info("ID="+payAccProfile.id+";CASH_AC_BAL="+payAccProfile.consAcBal);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payAccProfile.cashAcBal != null)ps.setLong(n++,payAccProfile.cashAcBal);
            if(payAccProfile.consAcBal != null)ps.setLong(n++,payAccProfile.consAcBal);
            ps.setString(n++,payAccProfile.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 虚拟账户支付
     * @param payRequest
     * @return
     * @throws Exception
     */
	public int deductUserBalance(PayRequest payRequest) throws Exception {
		if(PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT") == null)throw new Exception("平台虚拟账号空");
		//扣除用户款
        String sql = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL-?,CONS_AC_BAL=CONS_AC_BAL-?" +
        		" where CASH_AC_BAL-?>=0 and CONS_AC_BAL-?>=0 and AC_TYPE='"+PayConstant.CUST_TYPE_USER+"' and PAY_AC_NO=?";
        //增加增加系统账户款
        String sql1="update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+? where AC_TYPE='"+PayConstant.CUST_TYPE_MERCHANT+"' and PAY_AC_NO=?";
        String sql2="update PAY_ORDER set PAYTYPE='00',ACCAMT=? where MERNO=? and PRDORDNO=?";
        String sql3="update PAY_PRODUCT_ORDER set DEFPAYWAY='00' where MERNO=? and PRDORDNO=?";
        log.info(sql);
       
        Connection con = null;
        PreparedStatement ps = null;
        int res = 0;
        try {
            con = connection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            PayTranUserInfo payer = (PayTranUserInfo)payRequest.tranUserMap.get(payRequest.payerId);
            int n=1;
            log.info("CASH_AC_BAL="+payRequest.payOrder.txamt+";PAY_AC_NO="+payer.userId);
            ps.setLong(n++,payRequest.payOrder.txamt);
            ps.setLong(n++,payRequest.payOrder.txamt);
            ps.setLong(n++,payRequest.payOrder.txamt);
            ps.setLong(n++,payRequest.payOrder.txamt);
            ps.setString(n++,payer.userId);
            res = ps.executeUpdate();
            if(res>0){
            	//更新平台账户余额 (非担保支付)//商品订单类型0-消费；1-充值2-担保支付3商户充值
            	if(!"2".equals(payRequest.productOrder.prdordtype)){
            		ps.close();
            		log.info(sql1);
            		log.info("CASH_AC_BAL="+payRequest.payOrder.txamt+";PAY_AC_NO="+PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT"));
            		ps = con.prepareStatement(sql1);
                	n=1;
                	ps.setLong(n++,payRequest.payOrder.txamt);
                    ps.setLong(n++,payRequest.payOrder.txamt);
                    ps.setString(n++,PayConstant.PAY_CONFIG.get("PAY_SYS_ACCOUNT"));
                    ps.executeUpdate();
                    
                    //更新结算状态
//                    sql3 = "update PAY_PRODUCT_ORDER set DEFPAYWAY='00',ACDATE=sysdate,ACTIME=sysdate,STLSTS='1' where MERNO=? and PRDORDNO=?";
            	}
            	ps.close();
            	//更新订单为账户支付 PAYTYPE 支付订单支付方式，00 支付账户  01 网上银行 03 快捷支付
            	log.info(sql2);
            	log.info("ACCAMT="+payRequest.payOrder.txamt+";MERNO="+payRequest.merchantId+";PRDORDNO="+payRequest.merchantOrderId);
            	ps = con.prepareStatement(sql2);
            	n=1;
            	ps.setLong(n++,payRequest.payOrder.txamt);
            	ps.setString(n++,payRequest.merchantId);
            	ps.setString(n++,payRequest.merchantOrderId);
            	if(ps.executeUpdate()>0){
            		ps.close();
            		//商品订单支付方式 DEFPAYWAY 支付订单支付方式，默认支付方式 00 支付账户 01 网上银行 02 终端 03 快捷支付 04 混合支付 05 支票/汇款 06三方支付，默认01
                	ps = con.prepareStatement(sql3);
                	log.info(sql3);
                	n=1;
                	ps.setString(n++,payRequest.merchantId);
                	ps.setString(n++,payRequest.merchantOrderId);
                	ps.executeUpdate();
            	}
            }
            con.commit();
        } catch (Exception e) {
        	con.rollback();
            e.printStackTrace();
            throw e;
        } finally {
        	con.setAutoCommit(true);
            close(null, ps, con);
        }
        return res;
	}
    /**
     * 担保支付结算处理
     * @param payRequest
     * @return
     * @throws Exception
     */
	public void guaranteeNoticeUserBalance(PayRequest payRequest) throws Exception {
		//成功，更新卖家余额(结算净额-退款金额)，
		String sql = "update PAY_PRODUCT_ORDER set ACDATE=sysdate,ACTIME=sysdate,GUARANTEE_STATUS='1',stlsts='2' where MERNO=? and PRDORDNO=?";
		String sql1 = "update PAY_ACC_PROFILE set CASH_AC_BAL=CASH_AC_BAL+?,CONS_AC_BAL=CONS_AC_BAL+? where AC_TYPE='"+PayConstant.CUST_TYPE_USER+"' and PAY_AC_NO=?";
        log.info(sql);
        log.info("MERNO="+payRequest.merchantId+";PRDORDNO="+payRequest.merchantOrderId);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
        	ps.setString(n++,payRequest.merchantId);
        	ps.setString(n++,payRequest.merchantOrderId);
        	//修改清结算状态
            if(ps.executeUpdate() >0){
            	ps.close();
            	//修改系统虚拟账户余额/卖家余额
            	log.info(sql1);
            	log.info("CASH_AC_BAL="+(payRequest.payOrder.netrecamt-payRequest.productOrder.rftotalamt)+
            			";PAY_AC_NO="+payRequest.productOrder.sellrptacno);
            	ps = con.prepareStatement(sql1);
            	n=1;
            	ps.setLong(n++,payRequest.payOrder.netrecamt-payRequest.productOrder.rftotalamt);//净额减去退款总额
                ps.setLong(n++,payRequest.payOrder.netrecamt-payRequest.productOrder.rftotalamt);
                ps.setString(n++,payRequest.productOrder.sellrptacno);
        		ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }

}