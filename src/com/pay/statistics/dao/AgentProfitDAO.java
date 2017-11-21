package com.pay.statistics.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jweb.dao.BaseDAO;
import com.pay.fee.dao.PayFeeRate;
import com.pay.fee.dao.PayFeeRateDAO;
import com.pay.merchant.dao.PayMerchant;
import com.pay.merchant.dao.PayMerchantDAO;
/**
 * Table PAY_ACC_PROFILE DAO. 
 * @author Administrator
 *
 */
public class AgentProfitDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(AgentProfitDAO.class);
    public Map <String,List<UserMerchantRalition>> getUserMerchantRalition() throws Exception{
        String sql = "select tmp1.*,pju.name from(select a.* from "+
        		"(select smr.*,mr.store_name,mr.parent_id from PAY_SALESMAN_MERCHANT_RELATION smr left join PAY_MERCHANT_ROOT mr on smr.mer_no=mr.cust_id)a "+
        		"where a.mer_no in(select PARENT_ID from PAY_MERCHANT_ROOT where PARENT_ID is not null and length(PARENT_ID)<>1)) tmp1 left join PAY_JWEB_USER pju on tmp1.user_id=pju.id ";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String,List<UserMerchantRalition>> map = new HashMap<String, List<UserMerchantRalition>>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	UserMerchantRalition um = new UserMerchantRalition();
            	um.userId = rs.getString("USER_ID");
            	um.merNo = rs.getString("MER_NO");
            	um.storeName = rs.getString("STORE_NAME");
            	um.userName = rs.getString("NAME");
                if(map.get(um.userId)==null){
                	List<UserMerchantRalition> list = new ArrayList<UserMerchantRalition>();
                	list.add(um);
                	map.put(um.userId,list);
                } else map.get(um.userId).add(um);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public List<AgentProfitTrans> getAgentProfitByMer(PayMerchant agent,String dateStart,String dateEnd) throws Exception{
        String sql = "select count(merno)sumCount,merno,PAYTYPE,sum(txamt) sumAmt,sum(fee) sumFee,sum(AGENT_FEE) sumAgentAmt,sum(CHANNEL_FEE) sumChannelFee from  "+
        		"( "+
        		"  (select merno,PAYTYPE,txamt,fee,AGENT_FEE,CHANNEL_FEE from pay_order " +
        		"	where ordstatus='01' and CREATETIME >= to_date('"+dateStart+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and CREATETIME <= to_date('"+dateEnd+" 23:59:59','yyyy-mm-dd hh24:mi:ss')) "+
        		"  union "+
        		"  (select merno,PAYTYPE,txamt,fee,AGENT_FEE,CHANNEL_FEE from pay_order@pay_db_bak where ordstatus='01' and CREATETIME >= to_date('"+dateStart+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and   CREATETIME <= to_date('"+dateEnd+" 23:59:59','yyyy-mm-dd hh24:mi:ss')) "+
        		") pay_order where merno in(select cust_id from PAY_MERCHANT_ROOT where parent_id='"+agent.custId+"') group by PAYTYPE,merno";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AgentProfitTrans> list = new ArrayList<AgentProfitTrans>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
            	AgentProfitTrans apt = new AgentProfitTrans();
            	apt.sumCount=rs.getLong("sumCount");
            	apt.merno=rs.getString("merno");
            	apt.paytype=rs.getString("paytype");
            	apt.sumAmt=rs.getLong("sumAmt");
            	apt.sumFee=rs.getLong("sumFee");
            	apt.sumAgentAmt=rs.getLong("sumAgentAmt");
            	apt.sumChannelFee=rs.getLong("sumChannelFee");
            	list.add(apt);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public Map<String,PayMerchant>getAllMerchantAndAgentByAgent(Map<String,String> merAgent) throws Exception{
        String sql1 = "select * from PAY_MERCHANT_ROOT where PARENT_ID is not null and length(PARENT_ID)<>1";
        String sql2	="select * from PAY_MERCHANT_ROOT where cust_id in(select PARENT_ID from PAY_MERCHANT_ROOT where PARENT_ID is not null and length(PARENT_ID)<>1)";
        log.info(sql1);
        log.info(sql2);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String,PayMerchant> allTransMer = new HashMap<String,PayMerchant>();
        try {
            con = connection();
            ps = con.prepareStatement(sql1);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayMerchant mer = PayMerchantDAO.getPayMerchantValue(rs);
            	allTransMer.put(mer.custId,mer);
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayMerchant mer = PayMerchantDAO.getPayMerchantValue(rs);
            	allTransMer.put(mer.custId,mer);
            }
            return allTransMer;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    /**
     * detail PayCustFee
     * @param id
     * @return PayCustFee
     * @throws Exception
     */
    public Map<String,PayFeeRate> getCustFee(String custId,String custType,String custTypeAgent) throws Exception {
        String sql = "select fr.*,cf.tran_type tran_type1  from (select * from PAY_CUST_FEE where (TRAN_TYPE=? or TRAN_TYPE=?) and cust_id=?) cf left join PAY_FEE_RATE fr on cf.fee_code=fr.fee_code";
        log.info(sql);
        log.info("custId="+custId+",custType="+custType+",custTypeAgent="+custTypeAgent);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String,PayFeeRate> map = new HashMap<String,PayFeeRate>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,custType);
            ps.setString(2,custTypeAgent);
            ps.setString(3,custId);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayFeeRate fr = PayFeeRateDAO.getPayFeeRateValue(rs);
            	if(custType.equals(rs.getString("tran_type1")))map.put(custId+","+custType, fr);
            	else if(custTypeAgent.equals(rs.getString("tran_type1")))map.put(custId+","+custTypeAgent, fr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return map;
    }
}