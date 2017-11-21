package com.pay.user.dao;

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

import com.PayConstant;
import com.jweb.dao.BaseDAO;
import com.pay.merchantinterface.service.PayRequest;
import com.pay.order.dao.PayAccProfileDAO;
/**
 * Table PAY_TRAN_USER_INFO DAO. 
 * @author Administrator
 *
 */
public class PayTranUserInfoDAO  extends BaseDAO {
    private static final Log log = LogFactory.getLog(PayTranUserInfoDAO.class);
    public static synchronized PayTranUserInfo getPayTranUserInfoValue(ResultSet rs)throws SQLException {
        PayTranUserInfo payTranUserInfo = new PayTranUserInfo();
        payTranUserInfo.id = rs.getString("ID");
        payTranUserInfo.userId = rs.getString("USER_ID");
        payTranUserInfo.loginPassword = rs.getString("LOGIN_PASSWORD");
        payTranUserInfo.passwordStrength = rs.getString("PASSWORD_STRENGTH");
        payTranUserInfo.loginPwdFailCount = rs.getLong("LOGIN_PWD_FAIL_COUNT");
        payTranUserInfo.loginPwdLastTime = rs.getTimestamp("LOGIN_PWD_LAST_TIME");
        payTranUserInfo.payPassword = rs.getString("PAY_PASSWORD");
        payTranUserInfo.payPwdStrength = rs.getString("PAY_PWD_STRENGTH");
        payTranUserInfo.payPwdFailCount = rs.getLong("PAY_PWD_FAIL_COUNT");
        payTranUserInfo.payPwdLastTime = rs.getTimestamp("PAY_PWD_LAST_TIME");
        payTranUserInfo.befname = rs.getString("BEFNAME");
        payTranUserInfo.realName = rs.getString("REAL_NAME");
        payTranUserInfo.cretType = rs.getString("CRET_TYPE");
        payTranUserInfo.cretNo = rs.getString("CRET_NO");
        payTranUserInfo.email = rs.getString("EMAIL");
        payTranUserInfo.emailStatus = rs.getString("EMAIL_STATUS");
        payTranUserInfo.fax = rs.getString("FAX");
        payTranUserInfo.province = rs.getString("PROVINCE");
        payTranUserInfo.city = rs.getString("CITY");
        payTranUserInfo.area = rs.getString("AREA");
        payTranUserInfo.address = rs.getString("ADDRESS");
        payTranUserInfo.zip = rs.getString("ZIP");
        payTranUserInfo.nationality = rs.getString("NATIONALITY");
        payTranUserInfo.job = rs.getString("JOB");
        payTranUserInfo.tel = rs.getString("TEL");
        payTranUserInfo.mobile = rs.getString("MOBILE");
        payTranUserInfo.birthday = rs.getString("BIRTHDAY");
        payTranUserInfo.gender = rs.getString("GENDER");
        payTranUserInfo.publicPhoto = rs.getString("PUBLIC_PHOTO");
        payTranUserInfo.credPhotoFront = rs.getString("CRED_PHOTO_FRONT");
        payTranUserInfo.credPhotoBack = rs.getString("CRED_PHOTO_BACK");
        payTranUserInfo.sendType = rs.getString("SEND_TYPE");
        payTranUserInfo.userStatus = rs.getString("USER_STATUS");
        payTranUserInfo.checkUserId = rs.getString("CHECK_USER_ID");
        payTranUserInfo.checkTime = rs.getTimestamp("CHECK_TIME");
        payTranUserInfo.remark = rs.getString("REMARK");
        payTranUserInfo.revFlag = rs.getString("REV_FLAG");
        payTranUserInfo.mobileStatus = rs.getString("MOBILE_STATUS");
        payTranUserInfo.userType = rs.getString("USER_TYPE");
        payTranUserInfo.registerTime = rs.getTimestamp("REGISTER_TIME");
        payTranUserInfo.certSubmitTime = rs.getTimestamp("CERT_SUBMIT_TIME");
        payTranUserInfo.registerType = rs.getString("REGISTER_TYPE");
        payTranUserInfo.trialStatus = rs.getString("TRIAL_STATUS");
        try {payTranUserInfo.acstatus=rs.getString("AC_STATUS");} catch (Exception e) {}
        try {payTranUserInfo.cashacbal=rs.getLong("CASH_AC_BAL");} catch (Exception e) {}
        try {payTranUserInfo.consacbal=rs.getLong("CONS_AC_BAL");} catch (Exception e) {}
        try {payTranUserInfo.frozbalance=rs.getLong("FROZ_BALANCE");} catch (Exception e) {}
        return payTranUserInfo;
    }
    public static synchronized PayTranUserInfo getPayTranUserAndAccInfoValue(ResultSet rs)throws SQLException {
        PayTranUserInfo payTranUserInfo = new PayTranUserInfo();
        payTranUserInfo.id = rs.getString("ID");
        payTranUserInfo.userId = rs.getString("USER_ID");
        payTranUserInfo.loginPassword = rs.getString("LOGIN_PASSWORD");
        payTranUserInfo.passwordStrength = rs.getString("PASSWORD_STRENGTH");
        payTranUserInfo.loginPwdFailCount = rs.getLong("LOGIN_PWD_FAIL_COUNT");
        payTranUserInfo.loginPwdLastTime = rs.getTimestamp("LOGIN_PWD_LAST_TIME");
        payTranUserInfo.payPassword = rs.getString("PAY_PASSWORD");
        payTranUserInfo.payPwdStrength = rs.getString("PAY_PWD_STRENGTH");
        payTranUserInfo.payPwdFailCount = rs.getLong("PAY_PWD_FAIL_COUNT");
        payTranUserInfo.payPwdLastTime = rs.getTimestamp("PAY_PWD_LAST_TIME");
        payTranUserInfo.befname = rs.getString("BEFNAME");
        payTranUserInfo.realName = rs.getString("REAL_NAME");
        payTranUserInfo.cretType = rs.getString("CRET_TYPE");
        payTranUserInfo.cretNo = rs.getString("CRET_NO");
        payTranUserInfo.email = rs.getString("EMAIL");
        payTranUserInfo.emailStatus = rs.getString("EMAIL_STATUS");
        payTranUserInfo.fax = rs.getString("FAX");
        payTranUserInfo.province = rs.getString("PROVINCE");
        payTranUserInfo.city = rs.getString("CITY");
        payTranUserInfo.area = rs.getString("AREA");
        payTranUserInfo.address = rs.getString("ADDRESS");
        payTranUserInfo.zip = rs.getString("ZIP");
        payTranUserInfo.nationality = rs.getString("NATIONALITY");
        payTranUserInfo.job = rs.getString("JOB");
        payTranUserInfo.tel = rs.getString("TEL");
        payTranUserInfo.mobile = rs.getString("MOBILE");
        payTranUserInfo.birthday = rs.getString("BIRTHDAY");
        payTranUserInfo.gender = rs.getString("GENDER");
        payTranUserInfo.publicPhoto = rs.getString("PUBLIC_PHOTO");
        payTranUserInfo.credPhotoFront = rs.getString("CRED_PHOTO_FRONT");
        payTranUserInfo.credPhotoBack = rs.getString("CRED_PHOTO_BACK");
        payTranUserInfo.sendType = rs.getString("SEND_TYPE");
        payTranUserInfo.userStatus = rs.getString("USER_STATUS");
        payTranUserInfo.checkUserId = rs.getString("CHECK_USER_ID");
        payTranUserInfo.checkTime = rs.getTimestamp("CHECK_TIME");
        payTranUserInfo.remark = rs.getString("REMARK");
        payTranUserInfo.revFlag = rs.getString("REV_FLAG");
        payTranUserInfo.mobileStatus = rs.getString("MOBILE_STATUS");
        payTranUserInfo.accProfile = PayAccProfileDAO.getPayAccProfileValue(rs);
        return payTranUserInfo;
    }
    //详情数据
    public static synchronized PayTranUserInfo getPayTranUserInfoValue_detail(ResultSet rs)throws SQLException {
        PayTranUserInfo payTranUserInfo = new PayTranUserInfo();
        payTranUserInfo.id = rs.getString("ID");
        payTranUserInfo.userId = rs.getString("USER_ID");
        payTranUserInfo.loginPassword = rs.getString("LOGIN_PASSWORD");
        payTranUserInfo.passwordStrength = rs.getString("PASSWORD_STRENGTH");
        payTranUserInfo.loginPwdFailCount = rs.getLong("LOGIN_PWD_FAIL_COUNT");
        payTranUserInfo.loginPwdLastTime = rs.getTimestamp("LOGIN_PWD_LAST_TIME");
        payTranUserInfo.payPassword = rs.getString("PAY_PASSWORD");
        payTranUserInfo.payPwdStrength = rs.getString("PAY_PWD_STRENGTH");
        payTranUserInfo.payPwdFailCount = rs.getLong("PAY_PWD_FAIL_COUNT");
        payTranUserInfo.payPwdLastTime = rs.getTimestamp("PAY_PWD_LAST_TIME");
        payTranUserInfo.befname = rs.getString("BEFNAME");
        payTranUserInfo.realName = rs.getString("REAL_NAME");
        payTranUserInfo.cretType = rs.getString("CRET_TYPE");
        payTranUserInfo.cretNo = rs.getString("CRET_NO");
        payTranUserInfo.email = rs.getString("EMAIL");
        payTranUserInfo.emailStatus = rs.getString("EMAIL_STATUS");
        payTranUserInfo.fax = rs.getString("FAX");
        payTranUserInfo.province = rs.getString("PROVINCE");
        payTranUserInfo.city = rs.getString("CITY");
        payTranUserInfo.area = rs.getString("AREA");
        payTranUserInfo.address = rs.getString("ADDRESS");
        payTranUserInfo.zip = rs.getString("ZIP");
        payTranUserInfo.nationality = rs.getString("NATIONALITY");
        payTranUserInfo.job = rs.getString("JOB");
        payTranUserInfo.tel = rs.getString("TEL");
        payTranUserInfo.mobile = rs.getString("MOBILE");
        payTranUserInfo.birthday = rs.getString("BIRTHDAY");
        payTranUserInfo.gender = rs.getString("GENDER");
        payTranUserInfo.publicPhoto = rs.getString("PUBLIC_PHOTO");
        payTranUserInfo.credPhotoFront = rs.getString("CRED_PHOTO_FRONT");
        payTranUserInfo.credPhotoBack = rs.getString("CRED_PHOTO_BACK");
        payTranUserInfo.sendType = rs.getString("SEND_TYPE");
        payTranUserInfo.userStatus = rs.getString("USER_STATUS");
        payTranUserInfo.checkUserId = rs.getString("CHECK_USER_ID");
        payTranUserInfo.checkTime = rs.getTimestamp("CHECK_TIME");
        payTranUserInfo.remark = rs.getString("REMARK");
        payTranUserInfo.revFlag = rs.getString("REV_FLAG");
        payTranUserInfo.mobileStatus = rs.getString("MOBILE_STATUS");
        payTranUserInfo.userType = rs.getString("USER_TYPE");
        payTranUserInfo.registerTime = rs.getTimestamp("REGISTER_TIME");
        payTranUserInfo.certSubmitTime = rs.getTimestamp("CERT_SUBMIT_TIME");
        payTranUserInfo.registerType = rs.getString("REGISTER_TYPE");
        payTranUserInfo.trialStatus = rs.getString("TRIAL_STATUS");
        
        return payTranUserInfo;
    }
//    public String addPayTranUserInfo(PayTranUserInfo payTranUserInfo) throws Exception {
//        String sql = "insert into PAY_TRAN_USER_INFO("+
//            "ID," + 
//            "USER_ID," + 
//            "LOGIN_PASSWORD," + 
//            "PASSWORD_STRENGTH," + 
//            "LOGIN_PWD_FAIL_COUNT," + 
////            "LOGIN_PWD_LAST_TIME," + 
//            "PAY_PASSWORD," + 
//            "PAY_PWD_STRENGTH," + 
//            "PAY_PWD_FAIL_COUNT," + 
////            "PAY_PWD_LAST_TIME," + 
//            "BEFNAME," + 
//            "REAL_NAME," + 
//            "CRET_TYPE," + 
//            "CRET_NO," + 
//            "EMAIL," + 
//            "EMAIL_STATUS," + 
//            "FAX," + 
//            "PROVINCE," + 
//            "CITY," + 
//            "AREA," + 
//            "ADDRESS," + 
//            "ZIP," + 
//            "NATIONALITY," + 
//            "JOB," + 
//            "TEL," + 
//            "MOBILE," + 
//            "BIRTHDAY," + 
//            "GENDER," + 
//            "PUBLIC_PHOTO," + 
//            "CRED_PHOTO_FRONT," + 
//            "CRED_PHOTO_BACK," + 
//            "SEND_TYPE," + 
//            "USER_STATUS," + 
//            "CHECK_USER_ID," + 
////            "CHECK_TIME," + 
//            "REMARK," + 
//            "REV_FLAG," + 
//            "MOBILE_STATUS," + 
//            "USER_TYPE," + 
////            "REGISTER_TIME," + 
////            "CERT_SUBMIT_TIME," + 
//            "REGISTER_TYPE," + 
//            "TRIAL_STATUS)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        log.info(sql);
//        Connection con = null;
//        PreparedStatement ps = null;
//        try {
//            con = connection();
//            ps = con.prepareStatement(sql);
//            int n=1;
//            ps.setString(n++,payTranUserInfo.id);
//            ps.setString(n++,payTranUserInfo.userId);
//            ps.setString(n++,payTranUserInfo.loginPassword);
//            ps.setString(n++,payTranUserInfo.passwordStrength);
//            ps.setLong(n++,payTranUserInfo.loginPwdFailCount);
////            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.loginPwdLastTime));
//            ps.setString(n++,payTranUserInfo.payPassword);
//            ps.setString(n++,payTranUserInfo.payPwdStrength);
//            ps.setLong(n++,payTranUserInfo.payPwdFailCount);
////            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.payPwdLastTime));
//            ps.setString(n++,payTranUserInfo.befname);
//            ps.setString(n++,payTranUserInfo.realName);
//            ps.setString(n++,payTranUserInfo.cretType);
//            ps.setString(n++,payTranUserInfo.cretNo);
//            ps.setString(n++,payTranUserInfo.email);
//            ps.setString(n++,payTranUserInfo.emailStatus);
//            ps.setString(n++,payTranUserInfo.fax);
//            ps.setString(n++,payTranUserInfo.province);
//            ps.setString(n++,payTranUserInfo.city);
//            ps.setString(n++,payTranUserInfo.area);
//            ps.setString(n++,payTranUserInfo.address);
//            ps.setString(n++,payTranUserInfo.zip);
//            ps.setString(n++,payTranUserInfo.nationality);
//            ps.setString(n++,payTranUserInfo.job);
//            ps.setString(n++,payTranUserInfo.tel);
//            ps.setString(n++,payTranUserInfo.mobile);
//            ps.setString(n++,payTranUserInfo.birthday);
//            ps.setString(n++,payTranUserInfo.gender);
//            ps.setString(n++,payTranUserInfo.publicPhoto);
//            ps.setString(n++,payTranUserInfo.credPhotoFront);
//            ps.setString(n++,payTranUserInfo.credPhotoBack);
//            ps.setString(n++,payTranUserInfo.sendType);
//            ps.setString(n++,payTranUserInfo.userStatus);
//            ps.setString(n++,payTranUserInfo.checkUserId);
////            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.checkTime));
//            ps.setString(n++,payTranUserInfo.remark);
//            ps.setString(n++,payTranUserInfo.revFlag);
//            ps.setString(n++,payTranUserInfo.mobileStatus);
//            ps.setString(n++,payTranUserInfo.userType);
////            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.registerTime));
////            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.certSubmitTime));
//            ps.setString(n++,payTranUserInfo.registerType);
//            ps.setString(n++,payTranUserInfo.trialStatus);
//            ps.executeUpdate();
//            return "";
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }finally {
//            close(null, ps, con);
//        }
//    }
    public List getList() throws Exception{
        String sql = "select * from PAY_TRAN_USER_INFO";
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
                list.add(getPayTranUserInfoValue(rs));
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
     * @param payTranUserInfo
     * @return
     */
    private String setPayTranUserInfoSql(PayTranUserInfo payTranUserInfo) {
        StringBuffer sql = new StringBuffer();
        
        if(payTranUserInfo.userId != null && payTranUserInfo.userId.length() !=0) {
            sql.append(" USER_ID = ? and ");
        }
        if(payTranUserInfo.realName != null && payTranUserInfo.realName.length() !=0) {
            sql.append(" REAL_NAME LIKE ? and ");
        }
        if(payTranUserInfo.mobile != null && payTranUserInfo.mobile.length() !=0) {
            sql.append(" MOBILE = ? and ");
        }
        if(payTranUserInfo.registerTime_start != null ) {
            sql.append(" REGISTER_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and  ");
        }
        if(payTranUserInfo.registerTime_end != null ) {
            sql.append(" REGISTER_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and  ");
        }
        if(payTranUserInfo.checkTime_start != null ) {
        	sql.append(" CHECK_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and  ");
        }
        if(payTranUserInfo.checkTime_end != null ) {
        	sql.append(" CHECK_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and  ");
        }
        if(payTranUserInfo.province != null && payTranUserInfo.province.length() !=0) {
            sql.append(" PROVINCE = ? and ");
        }
        if(payTranUserInfo.city != null && payTranUserInfo.city.length() !=0) {
            sql.append(" CITY = ? and ");
        }
        if(payTranUserInfo.area != null && payTranUserInfo.area.length() !=0) {
            sql.append(" AREA = ? and ");
        }
        if(payTranUserInfo.userStatus != null && payTranUserInfo.userStatus.length() !=0) {
            sql.append(" USER_STATUS = ? and ");
        }
        if(payTranUserInfo.acstatus != null && payTranUserInfo.acstatus.length() !=0) {
            sql.append(" tmp2.AC_STATUS = ? and ");
        }
        if(payTranUserInfo.userType != null && payTranUserInfo.userType.length() !=0) {
            sql.append(" USER_TYPE = ? and ");
        }
       
        String tmp = sql.toString();
        if(tmp.length()>0)tmp=tmp.substring(0,tmp.lastIndexOf(" and "));
        return tmp;
    }
    /**
     * Set query parameter.
     * @param ps
     * @param payTranUserInfo
     * @param n
     * @return
     * @throws SQLException
     */
    private int setPayTranUserInfoParameter(PreparedStatement ps,PayTranUserInfo payTranUserInfo,int n)throws SQLException {
        if(payTranUserInfo.userId != null && payTranUserInfo.userId.length() !=0) {
            ps.setString(n++,payTranUserInfo.userId);
        }
        if(payTranUserInfo.realName != null && payTranUserInfo.realName.length() !=0) {
            ps.setString(n++,"%"+payTranUserInfo.realName+"%");
        }
        if(payTranUserInfo.mobile != null && payTranUserInfo.mobile.length() !=0) {
            ps.setString(n++,payTranUserInfo.mobile);
        }
        if(payTranUserInfo.registerTime_start != null ) {
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payTranUserInfo.registerTime_start)+" 00:00:00");
        }
        if(payTranUserInfo.registerTime_end != null ) {
        	 ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payTranUserInfo.registerTime_end)+" 23:59:59");
        }
        if(payTranUserInfo.checkTime_start != null ) {
            ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payTranUserInfo.checkTime_start)+" 00:00:00");
        }
        if(payTranUserInfo.checkTime_end != null ) {
        	 ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd").format(payTranUserInfo.checkTime_end)+" 23:59:59");
        }
        if(payTranUserInfo.province != null && payTranUserInfo.province.length() !=0) {
            ps.setString(n++,payTranUserInfo.province);
        }
        if(payTranUserInfo.city != null && payTranUserInfo.city.length() !=0) {
            ps.setString(n++,payTranUserInfo.city);
        }
        if(payTranUserInfo.area != null && payTranUserInfo.area.length() !=0) {
            ps.setString(n++,payTranUserInfo.area);
        }
        if(payTranUserInfo.userStatus != null && payTranUserInfo.userStatus.length() !=0) {
            ps.setString(n++,payTranUserInfo.userStatus);
        }
        if(payTranUserInfo.acstatus != null && payTranUserInfo.acstatus.length() !=0) {
            ps.setString(n++,payTranUserInfo.acstatus);
        }
        if(payTranUserInfo.userType != null && payTranUserInfo.userType.length() !=0) {
            ps.setString(n++,payTranUserInfo.userType);
        }
        return n;
    }
    /**
     * Get records count.
     * @param payTranUserInfo
     * @return
     */
    public int getPayTranUserInfoCount(PayTranUserInfo payTranUserInfo) {
        String sqlCon = setPayTranUserInfoSql(payTranUserInfo);
        String sql = "select count(rownum) recordCount from PAY_TRAN_USER_INFO  tmp left join PAY_ACC_PROFILE tmp2 on tmp.user_id=tmp2.pay_ac_no" +(sqlCon.length()==0?"":" where "+ sqlCon);
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayTranUserInfoParameter(ps,payTranUserInfo,n);
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
     * @param payTranUserInfo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     * @throws Exception
     */
    public List getPayTranUserInfoList(PayTranUserInfo payTranUserInfo,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayTranUserInfoSql(payTranUserInfo);
        String sortOrder = sort == null || sort.length()==0?" order by tmp.ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*,tmp2.cash_ac_bal,tmp2.cons_ac_bal,tmp2.froz_balance,tmp2.ac_status  from " +
                "PAY_TRAN_USER_INFO tmp left join PAY_ACC_PROFILE tmp2 on tmp.user_id=tmp2.pay_ac_no " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "  ) tmp1 "+
                ")  where rowno > "+((page-1)*rows)+ " and rowno<= " + (page*rows) + " order by REGISTER_TIME desc";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayTranUserInfoParameter(ps,payTranUserInfo,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayTranUserInfoValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    
    public List getPayTranUserInfoList_excel(PayTranUserInfo payTranUserInfo,int page,int rows,String sort,String order) throws Exception{
        sort = Tools.paraNameToDBColumnName(sort);
        String sqlCon = setPayTranUserInfoSql(payTranUserInfo);
        String sortOrder = sort == null || sort.length()==0?" order by tmp.ID desc":(" order by " + sort +("desc".equals(order)?" desc ":" asc "));
        String sql = "select * from (" +
                "  select rownum rowno,tmp1.* from (" +
                "   select tmp.*,tmp2.cash_ac_bal,tmp2.cons_ac_bal,tmp2.froz_balance,tmp2.ac_status  from " +
                "PAY_TRAN_USER_INFO tmp left join PAY_ACC_PROFILE tmp2 on tmp.user_id=tmp2.pay_ac_no " +(sqlCon.length()==0?"":" where "+ sqlCon) +sortOrder+
                "  ) tmp1 "+
                ")  where rowno > "+page+ " and rowno<= " + rows + " order by ID desc";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            con = connection();
            ps = con.prepareStatement(sql.toString());
            int n = 1;
            setPayTranUserInfoParameter(ps,payTranUserInfo,n);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(getPayTranUserInfoValue(rs));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
    }
    public Map getTranUserByCreateOrder(PayRequest payRequest) {
		Map map = new HashMap();
        if(payRequest.payerId.length()==0&&payRequest.salerId.length()==0)return map;
        String sql = "select usr.*,acc.* from(select * from PAY_TRAN_USER_INFO where USER_ID=? or USER_ID=?)usr left join PAY_ACC_PROFILE acc on usr.USER_ID=acc.PAY_AC_NO";
        log.info(sql);        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, payRequest.payerId);
            ps.setString(2, payRequest.salerId);
            rs = ps.executeQuery();
            while(rs.next()){
            	PayTranUserInfo user = getPayTranUserAndAccInfoValue(rs);
                map.put(user.userId,user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return map;
    }
    /**
     * remove PayTranUserInfo
     * @param id
     * @throws Exception     
     */
    public void removePayTranUserInfo(String id) throws Exception {
        String sql = "delete from PAY_TRAN_USER_INFO_ROOT where ID=?";
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
     * detail PayTranUserInfo
     * @param id
     * @return PayTranUserInfo
     * @throws Exception
     */
    public PayTranUserInfo detailPayTranUserInfo(String id) throws Exception {
        String sql = "select * from PAY_TRAN_USER_INFO where USER_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayTranUserInfoValue_detail(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    
    /**
     * detail PayTranUserInfo
     * @param CUST_ID
     * @return PayTranUserInfo
     * @throws Exception
     */
    public PayTranUserInfo detailPayTranUserInfoByCustId(String id) throws Exception {
        String sql = "select * from PAY_TRAN_USER_INFO where USER_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if(rs.next())return getPayTranUserInfoValue_detail(rs); 
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    
    /**
     * update PayTranUserInfo
     * @param payTranUserInfo
     * @throws Exception
     */
    public void updatePayTranUserInfo(PayTranUserInfo payTranUserInfo) throws Exception {
        String sqlTmp = "";
        if(payTranUserInfo.id != null)sqlTmp = sqlTmp + " ID=?,";
        if(payTranUserInfo.userId != null)sqlTmp = sqlTmp + " USER_ID=?,";
        if(payTranUserInfo.loginPassword != null)sqlTmp = sqlTmp + " LOGIN_PASSWORD=?,";
        if(payTranUserInfo.passwordStrength != null)sqlTmp = sqlTmp + " PASSWORD_STRENGTH=?,";
        if(payTranUserInfo.loginPwdFailCount != null)sqlTmp = sqlTmp + " LOGIN_PWD_FAIL_COUNT=?,";
        if(payTranUserInfo.loginPwdLastTime != null)sqlTmp = sqlTmp + " LOGIN_PWD_LAST_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payTranUserInfo.payPassword != null)sqlTmp = sqlTmp + " PAY_PASSWORD=?,";
        if(payTranUserInfo.payPwdStrength != null)sqlTmp = sqlTmp + " PAY_PWD_STRENGTH=?,";
        if(payTranUserInfo.payPwdFailCount != null)sqlTmp = sqlTmp + " PAY_PWD_FAIL_COUNT=?,";
        if(payTranUserInfo.payPwdLastTime != null)sqlTmp = sqlTmp + " PAY_PWD_LAST_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payTranUserInfo.befname != null)sqlTmp = sqlTmp + " BEFNAME=?,";
        if(payTranUserInfo.realName != null)sqlTmp = sqlTmp + " REAL_NAME=data_encrypt(?),";
        if(payTranUserInfo.cretType != null)sqlTmp = sqlTmp + " CRET_TYPE=?,";
        if(payTranUserInfo.cretNo != null)sqlTmp = sqlTmp + " CRET_NO=data_encrypt(?),";
        if(payTranUserInfo.email != null)sqlTmp = sqlTmp + " EMAIL=data_encrypt(?),";
        if(payTranUserInfo.emailStatus != null)sqlTmp = sqlTmp + " EMAIL_STATUS=?,";
        if(payTranUserInfo.fax != null)sqlTmp = sqlTmp + " FAX=?,";
        if(payTranUserInfo.province != null)sqlTmp = sqlTmp + " PROVINCE=?,";
        if(payTranUserInfo.city != null)sqlTmp = sqlTmp + " CITY=?,";
        if(payTranUserInfo.area != null)sqlTmp = sqlTmp + " AREA=?,";
        if(payTranUserInfo.address != null)sqlTmp = sqlTmp + " ADDRESS=?,";
        if(payTranUserInfo.zip != null)sqlTmp = sqlTmp + " ZIP=?,";
        if(payTranUserInfo.nationality != null)sqlTmp = sqlTmp + " NATIONALITY=?,";
        if(payTranUserInfo.job != null)sqlTmp = sqlTmp + " JOB=?,";
        if(payTranUserInfo.tel != null)sqlTmp = sqlTmp + " TEL=?,";
        if(payTranUserInfo.mobile != null)sqlTmp = sqlTmp + " MOBILE=data_encrypt(?),";
        if(payTranUserInfo.birthday != null)sqlTmp = sqlTmp + " BIRTHDAY=?,";
        if(payTranUserInfo.gender != null)sqlTmp = sqlTmp + " GENDER=?,";
        if(payTranUserInfo.publicPhoto != null)sqlTmp = sqlTmp + " PUBLIC_PHOTO=?,";
        if(payTranUserInfo.credPhotoFront != null)sqlTmp = sqlTmp + " CRED_PHOTO_FRONT=?,";
        if(payTranUserInfo.credPhotoBack != null)sqlTmp = sqlTmp + " CRED_PHOTO_BACK=?,";
        if(payTranUserInfo.sendType != null)sqlTmp = sqlTmp + " SEND_TYPE=?,";
        if(payTranUserInfo.userStatus != null)sqlTmp = sqlTmp + " USER_STATUS=?,";
        if(payTranUserInfo.checkUserId != null)sqlTmp = sqlTmp + " CHECK_USER_ID=?,";
        if(payTranUserInfo.checkTime != null)sqlTmp = sqlTmp + " CHECK_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payTranUserInfo.remark != null)sqlTmp = sqlTmp + " REMARK=?,";
        if(payTranUserInfo.revFlag != null)sqlTmp = sqlTmp + " REV_FLAG=?,";
        if(payTranUserInfo.mobileStatus != null)sqlTmp = sqlTmp + " MOBILE_STATUS=?,";
        if(payTranUserInfo.userType != null)sqlTmp = sqlTmp + " USER_TYPE=?,";
        if(payTranUserInfo.registerTime != null)sqlTmp = sqlTmp + " REGISTER_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payTranUserInfo.certSubmitTime != null)sqlTmp = sqlTmp + " CERT_SUBMIT_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss'),";
        if(payTranUserInfo.registerType != null)sqlTmp = sqlTmp + " REGISTER_TYPE=?,";
        if(payTranUserInfo.trialStatus != null)sqlTmp = sqlTmp + " TRIAL_STATUS=?,";
        if(payTranUserInfo.validTime != null)sqlTmp = sqlTmp + " VALID_TIME=?,";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_TRAN_USER_INFO_ROOT "+        
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
            if(payTranUserInfo.id != null)ps.setString(n++,payTranUserInfo.id);
            if(payTranUserInfo.userId != null)ps.setString(n++,payTranUserInfo.userId);
            if(payTranUserInfo.loginPassword != null)ps.setString(n++,payTranUserInfo.loginPassword);
            if(payTranUserInfo.passwordStrength != null)ps.setString(n++,payTranUserInfo.passwordStrength);
            if(payTranUserInfo.loginPwdFailCount != null)ps.setLong(n++,payTranUserInfo.loginPwdFailCount);
            if(payTranUserInfo.loginPwdLastTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.loginPwdLastTime));
            if(payTranUserInfo.payPassword != null)ps.setString(n++,payTranUserInfo.payPassword);
            if(payTranUserInfo.payPwdStrength != null)ps.setString(n++,payTranUserInfo.payPwdStrength);
            if(payTranUserInfo.payPwdFailCount != null)ps.setLong(n++,payTranUserInfo.payPwdFailCount);
            if(payTranUserInfo.payPwdLastTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.payPwdLastTime));
            if(payTranUserInfo.befname != null)ps.setString(n++,payTranUserInfo.befname);
            if(payTranUserInfo.realName != null)ps.setString(n++,payTranUserInfo.realName);
            if(payTranUserInfo.cretType != null)ps.setString(n++,payTranUserInfo.cretType);
            if(payTranUserInfo.cretNo != null)ps.setString(n++,payTranUserInfo.cretNo);
            if(payTranUserInfo.email != null)ps.setString(n++,payTranUserInfo.email);
            if(payTranUserInfo.emailStatus != null)ps.setString(n++,payTranUserInfo.emailStatus);
            if(payTranUserInfo.fax != null)ps.setString(n++,payTranUserInfo.fax);
            if(payTranUserInfo.province != null)ps.setString(n++,payTranUserInfo.province);
            if(payTranUserInfo.city != null)ps.setString(n++,payTranUserInfo.city);
            if(payTranUserInfo.area != null)ps.setString(n++,payTranUserInfo.area);
            if(payTranUserInfo.address != null)ps.setString(n++,payTranUserInfo.address);
            if(payTranUserInfo.zip != null)ps.setString(n++,payTranUserInfo.zip);
            if(payTranUserInfo.nationality != null)ps.setString(n++,payTranUserInfo.nationality);
            if(payTranUserInfo.job != null)ps.setString(n++,payTranUserInfo.job);
            if(payTranUserInfo.tel != null)ps.setString(n++,payTranUserInfo.tel);
            if(payTranUserInfo.mobile != null)ps.setString(n++,payTranUserInfo.mobile);
            if(payTranUserInfo.birthday != null)ps.setString(n++,payTranUserInfo.birthday);
            if(payTranUserInfo.gender != null)ps.setString(n++,payTranUserInfo.gender);
            if(payTranUserInfo.publicPhoto != null)ps.setString(n++,payTranUserInfo.publicPhoto);
            if(payTranUserInfo.credPhotoFront != null)ps.setString(n++,payTranUserInfo.credPhotoFront);
            if(payTranUserInfo.credPhotoBack != null)ps.setString(n++,payTranUserInfo.credPhotoBack);
            if(payTranUserInfo.sendType != null)ps.setString(n++,payTranUserInfo.sendType);
            if(payTranUserInfo.userStatus != null)ps.setString(n++,payTranUserInfo.userStatus);
            if(payTranUserInfo.checkUserId != null)ps.setString(n++,payTranUserInfo.checkUserId);
            if(payTranUserInfo.checkTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.checkTime));
            if(payTranUserInfo.remark != null)ps.setString(n++,payTranUserInfo.remark);
            if(payTranUserInfo.revFlag != null)ps.setString(n++,payTranUserInfo.revFlag);
            if(payTranUserInfo.mobileStatus != null)ps.setString(n++,payTranUserInfo.mobileStatus);
            if(payTranUserInfo.userType != null)ps.setString(n++,payTranUserInfo.userType);
            if(payTranUserInfo.registerTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.registerTime));
            if(payTranUserInfo.certSubmitTime != null)ps.setString(n++,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payTranUserInfo.certSubmitTime));
            if(payTranUserInfo.registerType != null)ps.setString(n++,payTranUserInfo.registerType);
            if(payTranUserInfo.trialStatus != null)ps.setString(n++,payTranUserInfo.trialStatus);
            if(payTranUserInfo.validTime != null)ps.setString(n++,payTranUserInfo.validTime);
            ps.setString(n++,payTranUserInfo.id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    
    public PayTranUserInfo getTranUserByColumn(String column,String value) throws Exception{
        String sql = "select * from PAY_TRAN_USER_INFO where "+column+"=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1, value);
            rs = ps.executeQuery();
            if(rs.next())return getPayTranUserInfoValue(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return null;
    }
    /**
     * update PayTranUserInfo
     * @param payTranUserInfo
     * @throws Exception
     */
    public void updatePayTranUserInfoForXF(PayTranUserInfo payTranUserInfo) throws Exception {
        String sqlTmp = "";
        if(payTranUserInfo.realName != null)sqlTmp = sqlTmp + " REAL_NAME=data_encrypt(?),";
        if(payTranUserInfo.cretType != null)sqlTmp = sqlTmp + " CRET_TYPE=?,";
        if(payTranUserInfo.cretNo != null)sqlTmp = sqlTmp + " CRET_NO=data_encrypt(?),";
        if(payTranUserInfo.mobile != null)sqlTmp = sqlTmp + " MOBILE=data_encrypt(?),";
        if(sqlTmp.length()==0)return;
        String sql = "update PAY_TRAN_USER_INFO_ROOT "+        
              "set " + 
              sqlTmp.substring(0,sqlTmp.length()-1) + 
            " where USER_ID=?"; 
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n=1;
            if(payTranUserInfo.realName != null)ps.setString(n++,payTranUserInfo.realName);
            if(payTranUserInfo.cretType != null)ps.setString(n++,payTranUserInfo.cretType);
            if(payTranUserInfo.cretNo != null)ps.setString(n++,payTranUserInfo.cretNo);
            if(payTranUserInfo.mobile != null)ps.setString(n++,payTranUserInfo.mobile);
            ps.setString(n++,payTranUserInfo.userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }finally {
            close(null, ps, con);
        }
    }
    
    /**
     * 根据用户编号，查询该用户是否存在
     * @param limitUserCode
     * @return
     * @throws Exception 
     */
	public Boolean existUser(String limitUserCode) throws Exception {
		String sql = "select * from PAY_TRAN_USER_INFO where USER_ID=?";
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setString(1,limitUserCode);
            rs = ps.executeQuery();
            if(rs.next()) return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(rs, ps, con);
        }
        return false;
	}
    public void updatePayTranUserPwdCount(PayTranUserInfo user) throws Exception {
        String sql = "update PAY_TRAN_USER_INFO_ROOT set PAY_PWD_FAIL_COUNT=?,PAY_PWD_LAST_TIME=sysdate where USER_ID=?";
        log.info(sql);
        log.info("PAY_PWD_FAIL_COUNT="+user.payPwdFailCount+";USER_ID="+user.userId);
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            ps.setLong(1,user.payPwdFailCount);
            ps.setString(2,user.userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(null, ps, con);
        }
    }
    public PayTranUserInfo selectSingleUser(String userId) {
    	if(userId == null || userId.length() == 0)return null;
		String sql = "SELECT * FROM PAY_TRAN_USER_INFO  WHERE USER_ID = ? or MOBILE=? or EMAIL=? "; 
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PayTranUserInfo user = null;
	    try{
	    	con = connection();
	        ps = con.prepareStatement(sql);
	        int n = 1;
	        ps.setString(n++, userId);
	        ps.setString(n++, userId);
	        ps.setString(n++, userId);
	        rs = ps.executeQuery();
	        if(rs.next()){
	        	user = getPayTranUserInfoValue(rs);
	        	user.accProfile = new PayAccProfileDAO().getPayAccProfileByCustInfo(PayConstant.CUST_TYPE_USER,user.userId);
	        }
	    } catch(Exception e){
	    	e.printStackTrace();
	    } finally{
	    	close(rs, ps, con);
	    }
		
		return user;
	}
	public void setUserTypebyUserId(String userId, String userType) {
		if(userId!=null && !"".equalsIgnoreCase(userId) && userType!=null && !"".equalsIgnoreCase(userType))
		{
			  String sql = "update PAY_TRAN_USER_INFO SET USER_TYPE ='"+userType+"' where USER_ID ='"+userId+"'";
			  log.info(sql);
		        Connection con = null;
		        PreparedStatement ps = null;
		        ResultSet rs = null;
		        try {
		            con = connection();
		            ps = con.prepareStatement(sql);
		            ps.execute();
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            close(rs, ps, con);
		        }
		}
	}
}