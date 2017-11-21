package com.pay.contract.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Tools;

import com.jweb.dao.BaseDAO;
/**
 * Table PAY_CONTRACT DAO. 
 * @author Administrator
 *
 */
public class PayContractDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayContractDAO.class);
    public static synchronized PayContract getPayContractValue(ResultSet rs)throws SQLException {
        PayContract payContract = new PayContract();
        payContract.seqNo = rs.getString("SEQ_NO");
        payContract.pactType = rs.getString("PACT_TYPE");
        payContract.pactVersNo = rs.getString("PACT_VERS_NO");
        payContract.pactName = rs.getString("PACT_NAME");
        payContract.pactCustType = rs.getString("PACT_CUST_TYPE");
        payContract.pactTakeEffDate = rs.getTimestamp("PACT_TAKE_EFF_DATE");
        payContract.pactLoseEffDate = rs.getTimestamp("PACT_LOSE_EFF_DATE");
        payContract.pactContent2 = rs.getString("PACT_CONTENT2");
        payContract.pactStatus = rs.getString("PACT_STATUS");
        payContract.creOperId = rs.getString("CRE_OPER_ID");
        payContract.creTime = rs.getTimestamp("CRE_TIME");
        payContract.lstUptOperId = rs.getString("LST_UPT_OPER_ID");
        try{payContract.lstUptOperName = rs.getString("LST_UPT_OPER_NAME");}catch(Exception e){}
        payContract.lstUptTime = rs.getTimestamp("LST_UPT_TIME");
        payContract.crePactMed = rs.getString("CRE_PACT_MED");
        payContract.crePactChnl = rs.getString("CRE_PACT_CHNL");
        payContract.pactRenew = rs.getString("PACT_RENEW");
        payContract.crePactTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("CRE_PACT_TIME"));
        payContract.bilaSignName = rs.getString("BILA_SIGN_NAME");
        payContract.custId = rs.getString("CUST_ID");
        try{payContract.storeName = rs.getString("STORE_NAME");}catch(Exception e){}
        try{payContract.bizType = rs.getString("BIZ_TYPE");}catch(Exception e){}
        payContract.sellProductInfo = rs.getString("SELL_PRODUCT_INFO");
        payContract.updateInfo = rs.getString("UPDATE_INFO");
        payContract.contractSignMan = rs.getString("CONTRACT_SIGN_MAN");
        payContract.custBilaSignName = rs.getString("CUST_BILA_SIGN_NAME");
        return payContract;
    }
    /**
     * detail PayContract
     * @param seqNo
     * @return PayContract
     * @throws Exception
     */
    public PayContract detailPayContract(String seqNo) throws Exception {
        String sql = "select * from PAY_CONTRACT where SEQ_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,seqNo);
            rs = ps.executeQuery();
            if(rs.next())return getPayContractValue(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * 添加合同
     * @param payContract
     * @return
     * @throws Exception
     */
    public String addPayContract(PayContract payContract) throws Exception {
        String sql = "insert into PAY_CONTRACT("+
            "SEQ_NO," + 
            "PACT_TYPE," + 
            "PACT_VERS_NO," + 
            "PACT_NAME," + 
            "PACT_CUST_TYPE," + 
            "PACT_TAKE_EFF_DATE," + 
            "PACT_LOSE_EFF_DATE," + 
            "PACT_CONTENT2," + 
            "PACT_STATUS," + 
            "CRE_OPER_ID," + 
            "LST_UPT_OPER_ID," + 
            "CRE_PACT_MED," + 
            "CRE_PACT_CHNL," + 
            "PACT_RENEW," + 
            "CRE_PACT_TIME," + 
            "BILA_SIGN_NAME," + 
            "CUST_ID," +
            "SELL_PRODUCT_INFO," + 
            "UPDATE_INFO," + 
            "CONTRACT_SIGN_MAN," + 
            "CUST_BILA_SIGN_NAME)values(?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?)";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            ps.setString(n++,payContract.seqNo);
            ps.setString(n++,payContract.pactType);
            ps.setString(n++,payContract.pactVersNo);
            ps.setString(n++,payContract.pactName);
            ps.setString(n++,payContract.pactCustType);
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payContract.pactTakeEffDate));
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payContract.pactLoseEffDate));
            ps.setString(n++,payContract.pactContent2);
            ps.setString(n++,payContract.pactStatus);
            ps.setString(n++,payContract.creOperId);
            ps.setString(n++,payContract.lstUptOperId);
            ps.setString(n++,payContract.crePactMed);
            ps.setString(n++,payContract.crePactChnl);
            ps.setString(n++,payContract.pactRenew);
            ps.setString(n++,payContract.crePactTime);
            ps.setString(n++,payContract.bilaSignName);
            ps.setString(n++,payContract.custId);
            ps.setString(n++,payContract.sellProductInfo);
            ps.setString(n++,payContract.updateInfo);
            ps.setString(n++,payContract.contractSignMan);
            ps.setString(n++,payContract.custBilaSignName);
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
        String sql = "select * from PAY_CONTRACT";
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
                list.add(getPayContractValue(rs));
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
     * @param payContract
     * @return
     */
    private String setPayContractSql(PayContract payContract) {
        StringBuffer sql = new StringBuffer();
        
        if(payContract.seqNo != null && payContract.seqNo.length() !=0) {
            sql.append(" SEQ_NO like ? and ");
        }
        if(payContract.custId != null && payContract.custId.length() !=0) {
            sql.append(" mer.CUST_ID like ? and ");
        }
        if(payContract.pactStatus != null && payContract.pactStatus.length() !=0) {
            sql.append(" PACT_STATUS = ? and ");
        }
        if(payContract.pactLoseEffDateStart != null) {
            sql.append(" PACT_LOSE_EFF_DATE >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payContract.pactLoseEffDateEnd != null) {
        	sql.append(" PACT_LOSE_EFF_DATE <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
        }
        if(payContract.lstUptTimeStart != null && payContract.lstUptTimeStart.length() !=0) {
			sql.append(" LST_UPT_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
		}
        if(payContract.lstUptTimeEnd != null && payContract.lstUptTimeEnd.length() !=0) {
			sql.append(" LST_UPT_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
		}
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payContract
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayContractParameter(PreparedStatement ps,PayContract payContract,int n)throws SQLException {
        if(payContract.seqNo != null && payContract.seqNo.length() !=0) {
            ps.setString(n++,"%"+payContract.seqNo+"%");
        }
        if(payContract.custId != null && payContract.custId.length() !=0) {
        	ps.setString(n++,"%"+payContract.custId+"%");
        }
        if(payContract.pactStatus != null && payContract.pactStatus.length() !=0) {
            ps.setString(n++,payContract.pactStatus);
        }
        if(payContract.pactLoseEffDateStart != null) {
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payContract.pactLoseEffDateStart)+" 00:00:00");
        }
        if(payContract.pactLoseEffDateEnd != null) {
        	ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payContract.pactLoseEffDateEnd)+" 23:59:59");
        }
        if(payContract.lstUptTimeStart != null && payContract.lstUptTimeStart.length() !=0) {
        	ps.setString(n++, payContract.lstUptTimeStart+" 00:00:00");
		}
        if(payContract.lstUptTimeEnd != null && payContract.lstUptTimeEnd.length() !=0) {
			ps.setString(n++, payContract.lstUptTimeEnd+" 23:59:59");
		}
        return n;
    }
    /**
     * Get records count.
     * @param payContract
     * @return
     */
    public int getPayContractCount(PayContract payContract) {
        String sqlCon = setPayContractSql(payContract);
        String sql = "select count(rownum) recordCount from PAY_CONTRACT ctract left join PAY_MERCHANT mer on ctract.CUST_ID=mer.CUST_ID " +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayContractParameter(ps,payContract,n);
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
     * @param payContract
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayContractList(PayContract payContract,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayContractSql(payContract);
        String sortOrder = sort == null || sort.length()==0?" order by CRE_TIME desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select ctract.*,mer.STORE_NAME,mer.BIZ_TYPE from " +
                "   (select pc.*,ju.NAME LST_UPT_OPER_NAME from PAY_CONTRACT pc left join PAY_JWEB_USER ju on pc.LST_UPT_OPER_ID=ju.id" +
                "   ) ctract left join PAY_MERCHANT mer on ctract.CUST_ID=mer.CUST_ID " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
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
            setPayContractParameter(ps,payContract,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayContractValue(rs));
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
     * remove PayContract
     * @param id
     * @return 
     * @throws Exception     
     */
    public int updatePayContractColumn(String column, String value,String seqNo) throws Exception {
        String sql = "update PAY_CONTRACT set "+column+"='"+value+"' where SEQ_NO=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,seqNo);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    /**
     * update PayContract
     * @param payContract
     * @throws Exception
     */
    public void updatePayContract(PayContract payContract) throws Exception {
        String sqlTmp = "";
        if(payContract.pactType != null)sqlTmp = sqlTmp + " PACT_TYPE=?,";
        if(payContract.pactVersNo != null)sqlTmp = sqlTmp + " PACT_VERS_NO=?,";
        if(payContract.pactName != null)sqlTmp = sqlTmp + " PACT_NAME=?,";
        if(payContract.pactCustType != null)sqlTmp = sqlTmp + " PACT_CUST_TYPE=?,";
        if(payContract.pactTakeEffDate != null)sqlTmp = sqlTmp + " PACT_TAKE_EFF_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payContract.pactLoseEffDate != null)sqlTmp = sqlTmp + " PACT_LOSE_EFF_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payContract.pactContent2 != null)sqlTmp = sqlTmp + " PACT_CONTENT2=?,";
        if(payContract.pactStatus != null)sqlTmp = sqlTmp + " PACT_STATUS=?,";
        if(payContract.creOperId != null)sqlTmp = sqlTmp + " CRE_OPER_ID=?,";
        if(payContract.creTime != null)sqlTmp = sqlTmp + " CRE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payContract.lstUptOperId != null)sqlTmp = sqlTmp + " LST_UPT_OPER_ID=?,";
        if(payContract.lstUptTime != null)sqlTmp = sqlTmp + " LST_UPT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payContract.crePactMed != null)sqlTmp = sqlTmp + " CRE_PACT_MED=?,";
        if(payContract.crePactChnl != null)sqlTmp = sqlTmp + " CRE_PACT_CHNL=?,";
        if(payContract.pactRenew != null)sqlTmp = sqlTmp + " PACT_RENEW=?,";
        if(payContract.crePactTime != null)sqlTmp = sqlTmp + " CRE_PACT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payContract.bilaSignName != null)sqlTmp = sqlTmp + " BILA_SIGN_NAME=?,";
        if(payContract.custId != null)sqlTmp = sqlTmp + " CUST_ID=?,";
        if(payContract.sellProductInfo != null)sqlTmp = sqlTmp + " SELL_PRODUCT_INFO=?,";
        if(payContract.updateInfo != null)sqlTmp = sqlTmp + " UPDATE_INFO=?,";
        if(payContract.contractSignMan != null)sqlTmp = sqlTmp + " CONTRACT_SIGN_MAN=?,";
        if(payContract.custBilaSignName != null)sqlTmp = sqlTmp + " CUST_BILA_SIGN_NAME=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_CONTRACT "+        
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
            if(payContract.pactType != null)ps.setString(n++,payContract.pactType);
            if(payContract.pactVersNo != null)ps.setString(n++,payContract.pactVersNo);
            if(payContract.pactName != null)ps.setString(n++,payContract.pactName);
            if(payContract.pactCustType != null)ps.setString(n++,payContract.pactCustType);
            if(payContract.pactTakeEffDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payContract.pactTakeEffDate));
            if(payContract.pactLoseEffDate != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payContract.pactLoseEffDate));
            if(payContract.pactContent2 != null)ps.setString(n++,payContract.pactContent2);
            if(payContract.pactStatus != null)ps.setString(n++,payContract.pactStatus);
            if(payContract.creOperId != null)ps.setString(n++,payContract.creOperId);
            if(payContract.creTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payContract.creTime));
            if(payContract.lstUptOperId != null)ps.setString(n++,payContract.lstUptOperId);
            if(payContract.lstUptTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payContract.lstUptTime));
            if(payContract.crePactMed != null)ps.setString(n++,payContract.crePactMed);
            if(payContract.crePactChnl != null)ps.setString(n++,payContract.crePactChnl);
            if(payContract.pactRenew != null)ps.setString(n++,payContract.pactRenew);
            if(payContract.crePactTime != null)ps.setString(n++,payContract.crePactTime);
            if(payContract.bilaSignName != null)ps.setString(n++,payContract.bilaSignName);
            if(payContract.custId != null)ps.setString(n++,payContract.custId);
            if(payContract.sellProductInfo != null)ps.setString(n++,payContract.sellProductInfo);
            if(payContract.updateInfo != null)ps.setString(n++,payContract.updateInfo);
            if(payContract.contractSignMan != null)ps.setString(n++,payContract.contractSignMan);
            if(payContract.custBilaSignName != null)ps.setString(n++,payContract.custBilaSignName);
            ps.setString(n++,payContract.seqNo);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    /**
     * 合同详情
     * @param payContract
     * @return
     */
	public PayContract getPayContractDetail(PayContract payContract) {
		String sqlCon = setPayContractSql(payContract);
        String sql = "select ctract.*,'' STORE_NAME,'' BIZ_TYPE from PAY_CONTRACT ctract where ctract.seq_no=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,payContract.seqNo);
            rs = ps.executeQuery();
            if (rs.next()) {
            	return getPayContractValue(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	/**
	 * 查询合同版本号
	 * @param pactType
	 * @return
	 */
	public String getPayContractPactVersNo(String pactType) {
        String sql = "select max(PACT_VERS_NO) PACT_VERS_NO from PAY_CONTRACT where PACT_TYPE = ?" ;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, pactType);
            rs = ps.executeQuery();
            if (rs.next()) {
            	String pactVersNo=rs.getString("PACT_VERS_NO");
            	if(StringUtils.isBlank(pactVersNo)){
            		//如果查出的版本号是空的
            		return "001";
            	}else{
            		long r = Long.parseLong(pactVersNo)+1;
            		//直接返回long的话，如果数字是以0开头的，会默认省去0
            		if(r<10)return "00"+r;
            		else if(r>=10&&r<=99)return "0"+r;
            		else return String.valueOf(r);
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	public boolean existMerchant(String parameter) {
        String sql = "select CUST_ID from PAY_MERCHANT where CUST_ID = ?" ;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, parameter);
            rs = ps.executeQuery();
            if (rs.next()) {
            	return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return false;
	
	}
	/**
	 * 获取序列号
	 * @return
	 */
	public String getSeqNo() {
		String sql = "select max(SEQ_NO) SEQ_NO from PAY_CONTRACT" ;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
            	String seqNo=rs.getString("SEQ_NO");
            	if(StringUtils.isBlank(seqNo)){
            		//如果查出的版本号是空的
            		return "100000001";
            	}else{
            		long r = Long.parseLong(seqNo)+1;
            		return String.valueOf(r);
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return null;
	}
	/**
	 * 检查合同是否存在
	 * @param contractId
	 * @return
	 */
	public boolean isExistContract(String custId,String contractId) {
		String sql = "SELECT SEQ_NO FROM PAY_CONTRACT  WHERE CUST_ID=? and SEQ_NO = ?"; 
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
	    try{
	    	con = connection();
	        ps = con.prepareStatement(sql);
	        ps.setString(1, custId);
	        ps.setString(2, contractId);
	        rs = ps.executeQuery();
	        if(rs.next()) return true;
	    } catch(Exception e){
	    	e.printStackTrace();
	    } finally{
	    	close(rs, ps, con);
	    }
		return false;
	}
	/**
	 * 检查合同是否存在
	 * @param contractId
	 * @return
	 */
	public boolean isExistContract(String custId) {
		String sql = "SELECT SEQ_NO FROM PAY_CONTRACT  WHERE CUST_ID=?"; 
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
	    try{
	    	con = connection();
	        ps = con.prepareStatement(sql);
	        ps.setString(1, custId);
	        rs = ps.executeQuery();
	        if(rs.next()) return true;
	    } catch(Exception e){
	    	e.printStackTrace();
	    } finally{
	    	close(rs, ps, con);
	    }
		return false;
	}
	public List getExpiresMerchant(String firsttDate,String secondDate,String thirddDate) {

		String sql = "SELECT * FROM PAY_CONTRACT WHERE " +
				"((PACT_LOSE_EFF_DATE >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and PACT_LOSE_EFF_DATE <= to_date(?,'yyyy-mm-dd hh24:mi:ss')) or " +
				"(PACT_LOSE_EFF_DATE >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and PACT_LOSE_EFF_DATE <= to_date(?,'yyyy-mm-dd hh24:mi:ss')) or " +
				"(PACT_LOSE_EFF_DATE >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and PACT_LOSE_EFF_DATE <= to_date(?,'yyyy-mm-dd hh24:mi:ss'))) " +
				"and PACT_STATUS='1'"; 
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new ArrayList();
	    try{
	    	con = connection();
	        ps = con.prepareStatement(sql);
	        int n=1;
	        ps.setString(n++,firsttDate+" 00:00:00");
        	ps.setString(n++,firsttDate+" 23:59:59");
        	 ps.setString(n++,secondDate+" 00:00:00");
         	ps.setString(n++,secondDate+" 23:59:59");
         	 ps.setString(n++,thirddDate+" 00:00:00");
         	ps.setString(n++,thirddDate+" 23:59:59");
	        rs = ps.executeQuery();
	        while(rs.next()) list.add(getPayContractValue(rs));
	    } catch(Exception e){
	    	e.printStackTrace();
	    } finally{
	    	close(rs, ps, con);
	    }
		return list;
	}
}