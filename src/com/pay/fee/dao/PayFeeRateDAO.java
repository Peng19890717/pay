package com.pay.fee.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
import com.pay.coopbank.dao.PayChannelBankRelation;
import com.pay.coopbank.dao.PayChannelBankRelationDAO;
import com.pay.coopbank.dao.PayChannelMaxAmt;
import com.pay.coopbank.dao.PayChannelMaxAmtDAO;
import com.pay.coopbank.dao.PayCoopBank;
import com.pay.coopbank.dao.PayCoopBankDAO;
/**
 * Table PAY_FEE_RATE DAO. 
 * @author Administrator
 *
 */
public class PayFeeRateDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayFeeRateDAO.class);
    public static synchronized PayFeeRate getPayFeeRateValue(ResultSet rs)throws SQLException {
        PayFeeRate payFeeRate = new PayFeeRate();
        payFeeRate.feeCode = rs.getString("FEE_CODE");
        payFeeRate.feeName = rs.getString("FEE_NAME");
        payFeeRate.ccy = rs.getString("CCY");
        payFeeRate.calMode = rs.getString("CAL_MODE");
        payFeeRate.multiSectionMode = rs.getString("MULTI_SECTION_MODE");
        payFeeRate.maxFee = rs.getLong("MAX_FEE");
        payFeeRate.minFee = rs.getLong("MIN_FEE");
        payFeeRate.startCalAmt = rs.getLong("START_CAL_AMT");
        payFeeRate.createTime = rs.getTimestamp("CREATE_TIME");
        payFeeRate.createUser = rs.getString("CREATE_USER");
        try{payFeeRate.createUserName = rs.getString("USER_NAME");}catch(Exception e){}
        payFeeRate.lastUpdTime = rs.getTimestamp("LAST_UPD_TIME");
        payFeeRate.lastUpdUser = rs.getString("LAST_UPD_USER");
        payFeeRate.bizType = rs.getString("BIZ_TYPE");
        payFeeRate.feeInfo = rs.getString("FEE_INFO");
        payFeeRate.tranType = rs.getString("TRAN_TYPE");
        payFeeRate.custType = rs.getString("CUST_TYPE");
        payFeeRate.custType = rs.getString("CUST_TYPE");
        payFeeRate.zeroFeeFlag = rs.getString("ZERO_FEE_FLAG");
        return payFeeRate;
    }
    public String addPayFeeRate(PayFeeRate payFeeRate) throws Exception {
        String sql = "insert into PAY_FEE_RATE("+
            "FEE_CODE," + 
            "FEE_NAME," + 
            "CCY," + 
            "CAL_MODE," + 
            "MULTI_SECTION_MODE," + 
            "MAX_FEE," + 
            "MIN_FEE," + 
            "START_CAL_AMT," + 
            "CREATE_USER," + 
            "LAST_UPD_USER," + 
            "BIZ_TYPE," + 
            "FEE_INFO," +
            "TRAN_TYPE," +
            "CUST_TYPE) select PAY_FEE_CODE.nextval,?,?,?,?,?,?,?,?,?,?,?,?,? from dual";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payFeeRate.feeName);
            ps.setString(n++,payFeeRate.ccy);
            ps.setString(n++,payFeeRate.calMode);
            ps.setString(n++,payFeeRate.multiSectionMode);
            ps.setLong(n++,payFeeRate.maxFee);
            ps.setLong(n++,payFeeRate.minFee);
            ps.setLong(n++,payFeeRate.startCalAmt);
            ps.setString(n++,payFeeRate.createUser);
            ps.setString(n++,payFeeRate.lastUpdUser);
            ps.setString(n++,payFeeRate.bizType);
            ps.setString(n++,payFeeRate.feeInfo);
            ps.setString(n++,payFeeRate.tranType);
            ps.setString(n++,payFeeRate.custType);
            ps.executeUpdate();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public PayFeeRate getPayFeeRateById(String feeCode) throws Exception{        
        String sql = "select * from PAY_FEE_RATE where FEE_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PayFeeRate payFeeRate = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, feeCode);
            rs = ps.executeQuery();
            if (rs.next()){
            	payFeeRate = getPayFeeRateValue(rs);
            	rs.close();
            	ps.close();
            	sql = "select * from PAY_FEE_SECTION where FEE_CODE='"+payFeeRate.feeCode+"'";
            	log.info(sql);
            	ps = con.prepareStatement(sql);
            	rs = ps.executeQuery();
                while(rs.next()){
                	payFeeRate.sectionList.add(PayFeeSectionDAO.getPayFeeSectionValue(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return payFeeRate;
    }
    public boolean getPayFeeRateByFeeInfo(String custType,String tranType) throws Exception{        
        String sql = "select FEE_CODE from PAY_FEE_RATE where CUST_TYPE=? and TRAN_TYPE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, custType);
            ps.setString(2, tranType);
            rs = ps.executeQuery();
            if (rs.next())return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return false;
    }
    public List getAllPayFeeRate() throws Exception{        
        String sql = "select * from PAY_FEE_RATE";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PayFeeRate payFeeRate = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
            	list.add(getPayFeeRateValue(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return list;
    }
    public Map getAllPayFeeRateMap() throws Exception{        
        String sql = "select * from PAY_FEE_RATE";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PayFeeRate payFeeRate = null;
        Map map = new HashMap();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
            	PayFeeRate fee = getPayFeeRateValue(rs);
            	map.put(fee.feeCode, fee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return map;
    }
    public Map<String,Long> getChannelTranMaxAmt() throws Exception{        
        String sql = "select * from PAY_CHANNEL_MAX_AMT";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String,Long> map = new HashMap<String,Long>();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
            	PayChannelMaxAmt amt = PayChannelMaxAmtDAO.getPayChannelMaxAmtValue(rs);
            	map.put(amt.bankCode+","+amt.tranType,amt.maxAmt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return map;
    }
    /**
     * Set query condition sql.
     * @param payFeeRate
     * @return
     */
    private String setPayFeeRateSql(PayFeeRate payFeeRate) {
        StringBuffer sql = new StringBuffer();
//        if(payFeeRate.bizType != null && payFeeRate.bizType.length() !=0) {
//            sql.append(" BIZ_TYPE = ? and ");
//        }
        if(payFeeRate.feeCode != null && payFeeRate.feeCode.length() !=0) {
            sql.append(" FEE_CODE like ? and ");
        }
        if(payFeeRate.feeName != null && payFeeRate.feeName.length() !=0) {
            sql.append(" FEE_NAME like ? and ");
        }
        if(payFeeRate.calMode != null && payFeeRate.calMode.length() !=0) {
            sql.append(" CAL_MODE = ? and ");
        }
        if(payFeeRate.multiSectionMode != null && payFeeRate.multiSectionMode.length() !=0) {
            sql.append(" MULTI_SECTION_MODE = ? and ");
        }
        if(payFeeRate.custType != null && payFeeRate.custType.length() !=0) {
        	sql.append(" CUST_TYPE = ? and ");
        }
        if(payFeeRate.tranType != null && payFeeRate.tranType.length() !=0) {
        	sql.append(" TRAN_TYPE = ? and ");
        }
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payFeeRate
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayFeeRateParameter(PreparedStatement ps,PayFeeRate payFeeRate,int n)throws SQLException {
//    	if(payFeeRate.bizType != null && payFeeRate.bizType.length() !=0) {
//            ps.setString(n++,payFeeRate.bizType);
//        }
        if(payFeeRate.feeCode != null && payFeeRate.feeCode.length() !=0) {
            ps.setString(n++,"%"+payFeeRate.feeCode+"%");
        }
        if(payFeeRate.feeName != null && payFeeRate.feeName.length() !=0) {
            ps.setString(n++,"%"+payFeeRate.feeName+"%");
        }
        if(payFeeRate.calMode != null && payFeeRate.calMode.length() !=0) {
            ps.setString(n++,payFeeRate.calMode);
        }
        if(payFeeRate.multiSectionMode != null && payFeeRate.multiSectionMode.length() !=0) {
            ps.setString(n++,payFeeRate.multiSectionMode);
        }
        if(payFeeRate.custType != null && payFeeRate.custType.length() !=0) {
        	ps.setString(n++,payFeeRate.custType);
        }
        if(payFeeRate.tranType != null && payFeeRate.tranType.length() !=0) {
        	ps.setString(n++,payFeeRate.tranType);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payFeeRate
     * @return
     */
    public int getPayFeeRateCount(PayFeeRate payFeeRate) {
        String sqlCon = setPayFeeRateSql(payFeeRate);
        String sql = "select count(rownum) recordCount from PAY_FEE_RATE " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayFeeRateParameter(ps,payFeeRate,n);
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
     * @param payFeeRate
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayFeeRateList(PayFeeRate payFeeRate,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayFeeRateSql(payFeeRate);
        String sortOrder = sort == null || sort.length()==0?" order by CREATE_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*  from (select p.*,u.NAME USER_NAME from PAY_FEE_RATE p left join PAY_JWEB_USER u on p.CREATE_USER=u.ID) tmp " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayFeeRateParameter(ps,payFeeRate,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayFeeRateValue(rs));
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
     * remove PayFeeRate
     * @param id
     * @throws Exception     
     */
    public void removePayFeeRate(String feeCode) throws Exception {
        String sql = "delete from PAY_FEE_RATE where FEE_CODE=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,feeCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayFeeRate
     * @param payFeeRate
     * @throws Exception
     */
    public void updatePayFeeRate(PayFeeRate payFeeRate) throws Exception {
        String sqlTmp = "";
        if(payFeeRate.feeCode != null)sqlTmp = sqlTmp + " FEE_CODE=?,";
        if(payFeeRate.feeName != null)sqlTmp = sqlTmp + " FEE_NAME=?,";
        if(payFeeRate.ccy != null)sqlTmp = sqlTmp + " CCY=?,";
        if(payFeeRate.calMode != null)sqlTmp = sqlTmp + " CAL_MODE=?,";
        if(payFeeRate.multiSectionMode != null)sqlTmp = sqlTmp + " MULTI_SECTION_MODE=?,";
        if(payFeeRate.maxFee != null)sqlTmp = sqlTmp + " MAX_FEE=?,";
        if(payFeeRate.minFee != null)sqlTmp = sqlTmp + " MIN_FEE=?,";
        if(payFeeRate.startCalAmt != null)sqlTmp = sqlTmp + " START_CAL_AMT=?,";
        if(payFeeRate.createTime != null)sqlTmp = sqlTmp + " CREATE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payFeeRate.createUser != null)sqlTmp = sqlTmp + " CREATE_USER=?,";
        if(payFeeRate.lastUpdTime != null)sqlTmp = sqlTmp + " LAST_UPD_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payFeeRate.lastUpdUser != null)sqlTmp = sqlTmp + " LAST_UPD_USER=?,";
        if(payFeeRate.bizType != null)sqlTmp = sqlTmp + " BIZ_TYPE=?,";
        if(payFeeRate.feeInfo != null)sqlTmp = sqlTmp + " FEE_INFO=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_FEE_RATE "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where FEE_CODE=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payFeeRate.feeCode != null)ps.setString(n++,payFeeRate.feeCode);
            if(payFeeRate.feeName != null)ps.setString(n++,payFeeRate.feeName);
            if(payFeeRate.ccy != null)ps.setString(n++,payFeeRate.ccy);
            if(payFeeRate.calMode != null)ps.setString(n++,payFeeRate.calMode);
            if(payFeeRate.multiSectionMode != null)ps.setString(n++,payFeeRate.multiSectionMode);
            if(payFeeRate.maxFee != null)ps.setLong(n++,payFeeRate.maxFee);
            if(payFeeRate.minFee != null)ps.setLong(n++,payFeeRate.minFee);
            if(payFeeRate.startCalAmt != null)ps.setLong(n++,payFeeRate.startCalAmt);
            if(payFeeRate.createTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payFeeRate.createTime));
            if(payFeeRate.createUser != null)ps.setString(n++,payFeeRate.createUser);
            if(payFeeRate.lastUpdTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payFeeRate.lastUpdTime));
            if(payFeeRate.lastUpdUser != null)ps.setString(n++,payFeeRate.lastUpdUser);
            if(payFeeRate.bizType != null)ps.setString(n++,payFeeRate.bizType);
            if(payFeeRate.feeInfo != null)ps.setString(n++,payFeeRate.feeInfo);
            ps.setString(n++,payFeeRate.feeCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    public Map getCustFee(String custType,String custId) throws Exception{        
        String sql = "select a.*,b.* from(select * from PAY_CUST_FEE where CUST_TYPE=? and CUST_ID=?) a left join PAY_FEE_RATE b on a.FEE_CODE=b.FEE_CODE";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map map = new HashMap();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,custType);
            ps.setString(2,custId);
            rs = ps.executeQuery();
            while (rs.next()){
            	PayCustFee pcf = new PayCustFee();
            	pcf.id=rs.getString("ID");
            	pcf.custType = rs.getString("CUST_TYPE");
            	pcf.custId = rs.getString("CUST_ID");
            	pcf.tranType = rs.getString("TRAN_TYPE");
            	pcf.feeCode = rs.getString("FEE_CODE");
            	map.put(pcf.custType+","+pcf.custId+","+pcf.tranType, getPayFeeRateValue(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return map;
    }
	public PayFeeRate getFeeRateByCust(String custType, String custId,String tranType) {
		//商户费率sql(指定费率)
        String sqlMerchant = "select b.* from PAY_CUST_FEE a,PAY_FEE_RATE b where a.FEE_CODE=b.FEE_CODE and a.CUST_TYPE=? and a.CUST_ID=? and a.TRAN_TYPE=?";
        //费率sql（通用费率）
        String sqlNormal = "select b.* from PAY_FEE_RATE b where CUST_TYPE=? and TRAN_TYPE=?";
        log.info(sqlMerchant);
        log.info(sqlNormal);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sqlMerchant);
            ps.setString(1, custType);
            ps.setString(2, custId);
            ps.setString(3, tranType);
            rs = ps.executeQuery();
	        if (rs.next())return getPayFeeRateValue(rs);
	        else {
	        	rs.close();
	        	ps.close();
            	ps = con.prepareStatement(sqlNormal);
            	ps.setString(1, custType);
                ps.setString(2, tranType);
            	rs = ps.executeQuery();
                if (rs.next())return getPayFeeRateValue(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
	public Map getChannelFeeRate() throws Exception{        
//        String sql = "select feeRa.cust_id channel_id,feeRa.tran_type,fee.* from"+
//		  "(select b.* from PAY_CHANNEL_BANK_RELATION a left join PAY_COOP_BANK b on a.channel_code=b.bank_code) channel "+ 
//		  "left join (select * from PAY_CUST_FEE where cust_type='"+PayConstant.CUST_TYPE_PAY_CHANNEL+"') feeRa on channel.BANK_CODE=feeRa.CUST_ID left join PAY_FEE_RATE fee on feeRa.FEE_CODE=fee.FEE_CODE";
        String sql="select feeRa.cust_id channel_id,feeRa.tran_type,fee.* from (select * FROM PAY_COOP_BANK  where bank_status='0') channel "+
       		"left join (select * from PAY_CUST_FEE where cust_type='2') feeRa on channel.BANK_CODE=feeRa.CUST_ID left join PAY_FEE_RATE fee on feeRa.FEE_CODE=fee.FEE_CODE";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map map = new HashMap();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
            	String channelId = rs.getString("channel_id");
            	String tranType = rs.getString("tran_type");
            	map.put(channelId+","+tranType,getPayFeeRateValue(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return map;
    }
	public List getChannelListByBank(String bankCode,String bankUserType) throws Exception{        
        String sql = "select b.*,a.* from PAY_CHANNEL_BANK_RELATION a ,PAY_COOP_BANK b " +
        		"where a.supported_user_type like '%"+bankUserType+",%' and a.channel_code=b.bank_code and b.BANK_STATUS='0' and  a.bank_code=?";
        log.info(sql);
        log.info("bankCode="+bankCode);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, bankCode);
            rs = ps.executeQuery();
            while (rs.next()){
            	PayCoopBank channel = PayCoopBankDAO.getPayCoopBankValue(rs);
            	channel.channelBankRelation = PayChannelBankRelationDAO.getPayChannelBankRelationValue(rs);
            	list.add(channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return list;
    }
	public List getQuickChannelListByBank(String bankCode,String quickUserType) throws Exception{        
        String sql = "select b.*,a.* from PAY_CHANNEL_BANK_RELATION a ,PAY_COOP_BANK b " +
        		"where a.QUICK_USER_TYPE like '%"+quickUserType+",%' and a.channel_code=b.bank_code and b.BANK_STATUS='0' and  a.bank_code=?";
        log.info(sql);
        log.info("bankCode="+bankCode);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, bankCode);
            rs = ps.executeQuery();
            while (rs.next()){
            	PayCoopBank channel = PayCoopBankDAO.getPayCoopBankValue(rs);
            	channel.channelBankRelation = PayChannelBankRelationDAO.getPayChannelBankRelationValue(rs);
            	list.add(channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return list;
    }
	public PayChannelBankRelation getSupportedBankForRP(String bankCode,String channelCode,String tranType) throws Exception{        
        String sql = "select * from PAY_CHANNEL_BANK_RELATION where BANK_CODE=? and CHANNEL_CODE=? and " +
        		("15".equals(tranType)?"RECEIVE_USER_TYPE":"WITHDRAW_USER_TYPE")+"='0'";
        log.info(sql);
        log.info("bankCode="+bankCode+",channelCode="+channelCode+",tranType"+tranType);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, bankCode);
            ps.setString(2, channelCode);
            rs = ps.executeQuery();
            if(rs.next())return PayChannelBankRelationDAO.getPayChannelBankRelationValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
	public void initZeroFeeRate() throws Exception {
		//查询系统零费率，如果记录13条，返回
        if(selectZeroFeeRecordCount() == 17) return;
        String sqlDelete = "delete from PAY_FEE_RATE where ZERO_FEE_FLAG='0'";
        log.info(sqlDelete);
        Connection con = null;
        PreparedStatement ps = null;
        try {
        	con = connection();
            con.setAutoCommit(false);
        	//删除系统零费率记录
            ps = con.prepareStatement(sqlDelete);
            ps.executeUpdate();
            ps.close();
        	//插入系统零费率，费率编码：ZERO_1_2
            ZeroFeeSql(con);
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
	
	/**
	 * 查询对应系统默认0费率列表数量
	 * @return 
	 */
	private Long selectZeroFeeRecordCount() {
        String sql = "select count(rownum) recordCount from PAY_FEE_RATE where ZERO_FEE_FLAG='0'";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("recordCount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
		return 0l;
	}
	
	/**
	 * 系统默认0费率数据批量添加
	 * @param ps 
	 * @throws SQLException 
	 */
	private void ZeroFeeSql(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("insert into PAY_FEE_RATE("+
	            "FEE_CODE," + 
	            "FEE_NAME," + 
	            "CCY," + 
	            "CAL_MODE," + 
	            "MULTI_SECTION_MODE," + 
	            "MAX_FEE," + 
	            "MIN_FEE," + 
	            "START_CAL_AMT," + 
	            "CREATE_USER," + 
	            "LAST_UPD_USER," + 
	            "BIZ_TYPE," + 
	            "FEE_INFO," +
	            "TRAN_TYPE," +
	            "CUST_TYPE," +
	            "ZERO_FEE_FLAG) select ?,?,?,?,?,?,?,?,?,?,?,?,?,?,? from dual");
		// 个人
//		setZeroFeeSqlParam(ps,"ZERO_0_1","个人消费零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","1","0","0");
//		setZeroFeeSqlParam(ps,"ZERO_0_2","个人充值零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","2","0","0");
//		setZeroFeeSqlParam(ps,"ZERO_0_3","个人结算零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","3","0","0");
//		setZeroFeeSqlParam(ps,"ZERO_0_4","个人退款零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","4","0","0");
//		setZeroFeeSqlParam(ps,"ZERO_0_5","个人提现零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","5","0","0");
//		setZeroFeeSqlParam(ps,"ZERO_0_6","个人转账零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","6","0","0");

		// 商户
		setZeroFeeSqlParam(ps,"ZERO_1_1","商户消费零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","1","1","0");
		setZeroFeeSqlParam(ps,"ZERO_1_2","商户充值零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","2","1","0");
		setZeroFeeSqlParam(ps,"ZERO_1_3","商户结算零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","3","1","0");
		setZeroFeeSqlParam(ps,"ZERO_1_4","商户退款零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","4","1","0");
		setZeroFeeSqlParam(ps,"ZERO_1_5","商户提现零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","5","1","0");
		setZeroFeeSqlParam(ps,"ZERO_1_6","商户转账零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","6","1","0");
		setZeroFeeSqlParam(ps,"ZERO_1_13","代理分成零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","13","1","0");
		setZeroFeeSqlParam(ps,"ZERO_1_15","代收零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","15","1","0");
		setZeroFeeSqlParam(ps,"ZERO_1_16","代付分成零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","16","1","0");
		// 支付渠道
		setZeroFeeSqlParam(ps,"ZERO_2_1","支付渠道消费零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","1","2","0");
		setZeroFeeSqlParam(ps,"ZERO_2_2","支付渠道充值零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","2","2","0");
		setZeroFeeSqlParam(ps,"ZERO_2_3","支付渠道结算零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","3","2","0");
		setZeroFeeSqlParam(ps,"ZERO_2_4","支付渠道退款零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","4","2","0");
		setZeroFeeSqlParam(ps,"ZERO_2_5","支付渠道提现零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","5","2","0");
		setZeroFeeSqlParam(ps,"ZERO_2_6","支付渠道转账零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","6","2","0");
		setZeroFeeSqlParam(ps,"ZERO_2_15","支付渠道代收零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","15","2","0");
		setZeroFeeSqlParam(ps,"ZERO_2_16","支付渠道代付零费率（系统默认）","CNY","0","0","0","0","0","admin","admin","","0-9999999999900,0,0;","16","2","0");
		// 执行批处理
		// ps.executeBatch();
	}
	/**
	 *  添加批处理参数
	 * @param ps
	 * @param args
	 * @throws SQLException
	 */
	private void setZeroFeeSqlParam(PreparedStatement ps,String... args) throws SQLException {
		int n = 1;
		ps.setString(n++,args[0]);
        ps.setString(n++,args[1]);
        ps.setString(n++,args[2]);
        ps.setString(n++,args[3]);
        ps.setLong(n++,Long.parseLong(args[4]));
        ps.setLong(n++,Long.parseLong(args[5]));
        ps.setLong(n++,Long.parseLong(args[6]));
        ps.setString(n++,args[7]);
        ps.setString(n++,args[8]);
        ps.setString(n++,args[9]);
        ps.setString(n++,args[10]);
        ps.setString(n++,args[11]);
        ps.setString(n++,args[12]);
        ps.setString(n++,args[13]);
        ps.setString(n++,args[14]);
		//ps.addBatch();
        ps.executeUpdate();
	}
	/**
     * update PayFeeRate
     * @param id
     * @throws Exception     
     */
    public void setCustZeroFeeRate(String feeCode) throws Exception {
        String sql = "update PAY_CUST_FEE set fee_code='ZERO_'||cust_type||'_'||tran_type where fee_code=?";
        log.info(sql);
        log.info("fee_code="+feeCode);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,feeCode);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
}