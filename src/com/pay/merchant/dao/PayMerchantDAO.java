package com.pay.merchant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.pay.custstl.dao.PayCustStlInfo;
import com.pay.fee.dao.PayFeeRateDAO;

/**
 * Table PAY_MERCHANT DAO.
 * 
 * @author Administrator
 * 
 */
public class PayMerchantDAO extends BaseDAO {
	private static PayFeeRateDAO payFeeRateDAO = new PayFeeRateDAO();
	private static final Log log = LogFactory.getLog(PayMerchantDAO.class);

	public static synchronized PayMerchant getPayMerchantValue(ResultSet rs)
			throws Exception {
		PayMerchant payMerchant = new PayMerchant();
		payMerchant.id = rs.getString("ID");
		payMerchant.custId = rs.getString("CUST_ID");
		payMerchant.parentId = rs.getString("PARENT_ID");
		payMerchant.storeName = rs.getString("STORE_NAME");
		payMerchant.storeShortname = rs.getString("STORE_SHORTNAME");
		payMerchant.merType = rs.getString("MER_TYPE");
		payMerchant.bizType = rs.getString("BIZ_TYPE");
		payMerchant.merLawPerson = rs.getString("MER_LAW_PERSON");
		payMerchant.payChannel = rs.getString("PAY_CHANNEL");
		payMerchant.techContact = rs.getString("TECH_CONTACT");
		payMerchant.techTelNo = rs.getString("TECH_TEL_NO");
		payMerchant.techEmail = rs.getString("TECH_EMAIL");
		payMerchant.bizContact = rs.getString("BIZ_CONTACT");
		payMerchant.bizTelNo = rs.getString("BIZ_TEL_NO");
		payMerchant.bizEmail = rs.getString("BIZ_EMAIL");
		payMerchant.servContact = rs.getString("SERV_CONTACT");
		payMerchant.servTelNo = rs.getString("SERV_TEL_NO");
		payMerchant.servEmail = rs.getString("SERV_EMAIL");
		payMerchant.merAddr = rs.getString("MER_ADDR");
		payMerchant.bizRange = rs.getString("BIZ_RANGE");
		payMerchant.merOpnBankCode = rs.getString("MER_OPN_BANK_CODE");
		payMerchant.bankStlAcNo = rs.getString("BANK_STL_AC_NO");
		payMerchant.rfCommFlg = rs.getString("RF_COMM_FLG");
		payMerchant.rfRecFeeFlg = rs.getString("RF_REC_FEE_FLG");
		payMerchant.city = rs.getString("CITY");
		payMerchant.postCode = rs.getString("POST_CODE");
		payMerchant.lawPersonType = rs.getString("LAW_PERSON_TYPE");
		payMerchant.lawPersonCretType = rs.getString("LAW_PERSON_CRET_TYPE");
		payMerchant.lawPersonCretNo = rs.getString("LAW_PERSON_CRET_NO");
		payMerchant.longTermSign = rs.getString("LONG_TERM_SIGN");
		payMerchant.certBeginDate = rs.getTimestamp("CERT_BEGIN_DATE");
		payMerchant.certEndDate = rs.getTimestamp("CERT_END_DATE");
		payMerchant.webAddress = rs.getString("WEB_ADDRESS");
		payMerchant.webAddressAbbr = rs.getString("WEB_ADDRESS_ABBR");
		payMerchant.contractStrDate = rs.getTimestamp("CONTRACT_STR_DATE");
		payMerchant.contractEndDate = rs.getTimestamp("CONTRACT_END_DATE");
		payMerchant.contractor = rs.getString("CONTRACTOR");
		payMerchant.contractBizNo = rs.getString("CONTRACT_BIZ_NO");
		payMerchant.contractBizName = rs.getString("CONTRACT_BIZ_NAME");
		payMerchant.techMobNo = rs.getString("TECH_MOB_NO");
		payMerchant.bizMobNo = rs.getString("BIZ_MOB_NO");
		payMerchant.servMobNo = rs.getString("SERV_MOB_NO");
		payMerchant.margin = rs.getLong("MARGIN");
		payMerchant.frozStlSign = rs.getString("FROZ_STL_SIGN");
		payMerchant.cretMasterityDays = rs.getLong("CRET_MASTERITY_DAYS");
		payMerchant.taxRegistrationNo = rs.getString("TAX_REGISTRATION_NO");
		payMerchant.organizationNo = rs.getString("ORGANIZATION_NO");
		payMerchant.cpsSign = rs.getString("CPS_SIGN");
		payMerchant.regCapital = rs.getLong("REG_CAPITAL");
		payMerchant.agentGoalDiscountRate = rs.getLong("AGENT_GOAL_DISCOUNT_RATE");
		payMerchant.agentPayRate = rs.getLong("AGENT_PAY_RATE");
		payMerchant.agentTaxRate = rs.getLong("AGENT_TAX_RATE");
		payMerchant.compayEmail = rs.getString("COMPAY_EMAIL");
		payMerchant.compayTelNo = rs.getString("COMPAY_TEL_NO");
		payMerchant.compayFax = rs.getString("COMPAY_FAX");
		payMerchant.cycleDays = rs.getLong("CYCLE_DAYS");
		payMerchant.codeTypeId = rs.getString("CODE_TYPE_ID");
		payMerchant.cert = rs.getString("CERT");
		payMerchant.companyBrief = rs.getString("COMPANY_BRIEF");
		payMerchant.riskLevel = rs.getString("RISK_LEVEL");
		payMerchant.bussinessType = rs.getString("BUSSINESS_TYPE");
		payMerchant.merStatus = rs.getString("MER_STATUS");
		payMerchant.createTime = rs.getTimestamp("CREATE_TIME");
		payMerchant.createUser = rs.getString("CREATE_USER");
		payMerchant.businessLicencePic = rs.getString("BUSINESS_LICENCE_PIC");
		payMerchant.taxRegistrationPic = rs.getString("TAX_REGISTRATION_PIC");
		payMerchant.organizationPic = rs.getString("ORGANIZATION_PIC");
		payMerchant.openingLicensesPic = rs.getString("OPENING_LICENSES_PIC");
		payMerchant.certPicFront = rs.getString("CERT_PIC_FRONT");
		payMerchant.certPicBack = rs.getString("CERT_PIC_BACK");
		payMerchant.contractPic = rs.getString("CONTRACT_PIC");
		payMerchant.icpNo = rs.getString("ICP_NO");
		// payMerchant.settleFeeCode = rs.getString("SETTLE_FEE_CODE");
		// payMerchant.withdrawFeeCode = rs.getString("WITHDRAW_FEE_CODE");
		// payMerchant.urgentFeeCode = rs.getString("URGENT_FEE_CODE");
		payMerchant.province = rs.getString("PROVINCE");
		payMerchant.checkUser = rs.getString("CHECK_USER");
		payMerchant.checkTime = rs.getTimestamp("CHECK_TIME");
		payMerchant.checkStatus = rs.getString("CHECK_STATUS");
		payMerchant.checkInfo = rs.getString("CHECK_INFO");
		payMerchant.riskDesc = rs.getString("RISK_DESC");
		payMerchant.region = rs.getString("REGION");
		payMerchant.userInterflowFlag = rs.getString("USER_INTERFLOW_FLAG");
		try {
			payMerchant.attentionLine = rs.getString("ATTENTION_LINE");
		} catch (Exception e) {
		}
		try {
			payMerchant.attentionLineTel = rs.getString("ATTENTION_LINE_TEL");
		} catch (Exception e) {
		}
		try {
			payMerchant.attentionLineEmail = rs
					.getString("ATTENTION_LINE_EMAIL");
		} catch (Exception e) {
		}
		try {
			payMerchant.businessLicenceNo = rs.getString("BUSINESS_LICENCE_NO");
		} catch (Exception e) {
		}
		try {
			payMerchant.businessLicenceBeginDate = rs
					.getTimestamp("BUSINESS_LICENCE_BEGIN_DATE");
		} catch (Exception e) {
		}
		try {
			payMerchant.businessLicenceEndDate = rs
					.getTimestamp("BUSINESS_LICENCE_END_DATE");
		} catch (Exception e) {
		}
		try {
			payMerchant.icpCertNo = rs.getString("ICP_CERT_NO");
		} catch (Exception e) {
		}
		payMerchant.merchantLogoPic = rs.getString("MERCHANT_LOGO_PIC");
		payMerchant.bankStlAcNoType = rs.getString("BANK_STL_AC_NO_TYPE");
		payMerchant.issuer = rs.getString("ISSUER");
		payMerchant.interfaceIp = rs.getString("INTERFACE_IP");
		payMerchant.payWaySupported = rs.getString("PAY_WAY_SUPPORTED");
		payMerchant.chargeWay = rs.getString("CHARGE_WAY");
		payMerchant.preStorageFee = rs.getLong("PRE_STORAGE_FEE");
		payMerchant.settlementWay = rs.getString("SETTLEMENT_WAY");
		try {
			payMerchant.custBankDepositName = rs
					.getString("CUST_BANK_DEPOSIT_NAME");
		} catch (Exception e) {
		}
		try {
			payMerchant.depositBankBrchName = rs
					.getString("CUST_STL_BANK_AC_NO");
		} catch (Exception e) {
		}
		try {
			payMerchant.depositBankCode = rs.getString("DEPOSIT_BANK_CODE");
		} catch (Exception e) {
		}
		try {
			payMerchant.custStlType = rs.getString("CUST_STL_TYPE");
		} catch (Exception e) {
		}
		try {
			payMerchant.custStlBankAcNo = rs.getString("CUST_STL_BANK_AC_NO");
		} catch (Exception e) {
		}
		try {
			payMerchant.custSetPeriod = rs.getString("CUST_SET_PERIOD");
		} catch (Exception e) {
		}
		try {
			payMerchant.custSetFrey = rs.getString("CUST_SET_FREY");
		} catch (Exception e) {
		}
		try {
			payMerchant.custSetFreyAgent = rs.getString("CUST_SET_FREY_AGENT");
		} catch (Exception e) {
		}
		try {
			payMerchant.custStlTimeSet = rs.getString("CUST_STL_TIME_SET");
		} catch (Exception e) {
		}
		try {
			payMerchant.custSetPeriodAgent = rs
					.getString("CUST_SET_PERIOD_AGENT");
		} catch (Exception e) {
		}
		try {
			payMerchant.custStlTimeSetAgent = rs
					.getString("CUST_STL_TIME_SET_AGENT");
		} catch (Exception e) {
		}
		try {
			payMerchant.custSetPeriodDaishou = rs
					.getString("CUST_SET_PERIOD_DAISHOU");
		} catch (Exception e) {
		}
		try {
			payMerchant.custStlTimeSetDaishou = rs
					.getString("CUST_STL_TIME_SET_DAISHOU");
		} catch (Exception e) {
		}
		try {
			payMerchant.minStlBalance = rs.getLong("MIN_STL_BALANCE");
		} catch (Exception e) {
		}
		try {
			payMerchant.cashAcBal = rs.getLong("CASH_AC_BAL");
		} catch (Exception e) {
		}
		payMerchant.feeMap = payFeeRateDAO.getCustFee(
				PayConstant.CUST_TYPE_MERCHANT, payMerchant.custId);
		return payMerchant;
	}

	public String addPayMerchant(PayMerchant payMerchant) throws Exception {
		String sql = "insert into PAY_MERCHANT_ROOT("
				+ "ID,"
				+ "CUST_ID,"
				+ "PARENT_ID,"
				+ "STORE_NAME,"
				+ "STORE_SHORTNAME,"
				+ "MER_TYPE,"
				+ "BIZ_TYPE,"
				+ "MER_LAW_PERSON,"
				+ "PAY_CHANNEL,"
				+ "TECH_CONTACT,"
				+ "TECH_TEL_NO,"
				+ "TECH_EMAIL,"
				+ "BIZ_CONTACT,"
				+ "ATTENTION_LINE_TEL,"
				+ "ATTENTION_LINE_EMAIL,"
				+ "SERV_CONTACT,"
				+ "SERV_TEL_NO,"
				+ "SERV_EMAIL,"
				+ "MER_ADDR,"
				+ "BIZ_RANGE,"
				+ "MER_OPN_BANK_CODE,"
				+ "BANK_STL_AC_NO,"
				+ "RF_COMM_FLG,"
				+ "RF_REC_FEE_FLG,"
				+ "CITY,"
				+ "POST_CODE,"
				+ "LAW_PERSON_TYPE,"
				+ "LAW_PERSON_CRET_TYPE,"
				+ "LAW_PERSON_CRET_NO,"
				+ "LONG_TERM_SIGN,"
				+ "CERT_BEGIN_DATE,"
				+ "CERT_END_DATE,"
				+ "WEB_ADDRESS,"
				+ "WEB_ADDRESS_ABBR,"
				+ "CONTRACT_STR_DATE,"
				+ "CONTRACT_END_DATE,"
				+ "CONTRACTOR,"
				+ "CONTRACT_BIZ_NO,"
				+ "CONTRACT_BIZ_NAME,"
				+ "TECH_MOB_NO,"
				+ "BIZ_MOB_NO,"
				+ "SERV_MOB_NO,"
				+ "MARGIN,"
				+ "FROZ_STL_SIGN,"
				+ "CRET_MASTERITY_DAYS,"
				+ "TAX_REGISTRATION_NO,"
				+ "ORGANIZATION_NO,"
				+ "CPS_SIGN,"
				+ "REG_CAPITAL,"
				+ "COMPAY_EMAIL,"
				+ "COMPAY_TEL_NO,"
				+ "COMPAY_FAX,"
				+ "CYCLE_DAYS,"
				+ "CODE_TYPE_ID,"
				+ "CERT,"
				+ "COMPANY_BRIEF,"
				+ "RISK_LEVEL,"
				+ "BUSSINESS_TYPE,"
				+ "MER_STATUS,"
				+ "CREATE_TIME,"
				+ "CREATE_USER,"
				+ "BUSINESS_LICENCE_PIC,"
				+ "TAX_REGISTRATION_PIC,"
				+ "ORGANIZATION_PIC,"
				+ "OPENING_LICENSES_PIC,"
				+ "CERT_PIC_FRONT,"
				+ "CERT_PIC_BACK,"
				+ "CONTRACT_PIC,"
				+ "ICP_NO,"
				+
				// "SETTLE_FEE_CODE," +
				// "WITHDRAW_FEE_CODE," +
				// "URGENT_FEE_CODE," +
				"PROVINCE,"
				+ "RISK_DESC,"
				+ "REGION,"
				+
				// "ATTENTION_LINE," +
				// "ATTENTION_LINE_TEL," +
				// "ATTENTION_LINE_EMAIL," +
				"BUSINESS_LICENCE_NO,"
				+ "BUSINESS_LICENCE_BEGIN_DATE,"
				+ "BUSINESS_LICENCE_END_DATE,"
				+ "ICP_CERT_NO,"
				+ "BANK_STL_AC_NO_TYPE,"
				+ "INTERFACE_IP,"
				+ "ISSUER,SETTLEMENT_WAY,PAY_WAY_SUPPORTED,AGENT_GOAL_DISCOUNT_RATE,CHARGE_WAY,PRE_STORAGE_FEE,USER_INTERFLOW_FLAG,AGENT_PAY_RATE,AGENT_TAX_RATE)values(?,?,?,?,?,?,?,data_encrypt(?),?,data_encrypt(?),data_encrypt(?),data_encrypt(?),"
				+ "data_encrypt(?),data_encrypt(?),data_encrypt(?),data_encrypt(?),data_encrypt(?),data_encrypt(?),?,?,?,data_encrypt(?),?,?,?,?,?,?,"
				+ "data_encrypt(?),?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,"
				+ "to_date(?,'yyyy-mm-dd hh24:mi:ss'),to_date(?,'yyyy-mm-dd hh24:mi:ss'),"
				+ "data_encrypt(?),?,?,data_encrypt(?),data_encrypt(?),data_encrypt(?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd'),to_date(?,'yyyy-mm-dd'),?,?,?,?,?,?,?,?,?,?,?,?)";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			ps.setString(n++, payMerchant.id);
			ps.setString(n++, payMerchant.custId);
			ps.setString(n++, payMerchant.parentId);
			ps.setString(n++, payMerchant.storeName);
			ps.setString(n++, payMerchant.storeShortname);
			ps.setString(n++, payMerchant.merType);
			ps.setString(n++, payMerchant.bizType);
			ps.setString(n++, payMerchant.merLawPerson);
			ps.setString(n++, payMerchant.payChannel);
			ps.setString(n++, payMerchant.techContact);
			ps.setString(n++, payMerchant.techTelNo);
			ps.setString(n++, payMerchant.techEmail);
			ps.setString(n++, payMerchant.bizContact);
			ps.setString(n++, payMerchant.attentionLineTel);
			ps.setString(n++, payMerchant.attentionLineEmail);
			ps.setString(n++, payMerchant.servContact);
			ps.setString(n++, payMerchant.servTelNo);
			ps.setString(n++, payMerchant.servEmail);
			ps.setString(n++, payMerchant.merAddr);
			ps.setString(n++, payMerchant.bizRange);
			ps.setString(n++, payMerchant.merOpnBankCode);
			ps.setString(n++, payMerchant.bankStlAcNo);
			ps.setString(n++, payMerchant.rfCommFlg);
			ps.setString(n++, payMerchant.rfRecFeeFlg);
			ps.setString(n++, payMerchant.city);
			ps.setString(n++, payMerchant.postCode);
			ps.setString(n++, payMerchant.lawPersonType);
			ps.setString(n++, payMerchant.lawPersonCretType);
			ps.setString(n++, payMerchant.lawPersonCretNo);
			ps.setString(n++, payMerchant.longTermSign);
			ps.setString(n++, new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(new Date()));
			ps.setString(n++, new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(new Date()));
			payMerchant.setCertEndDate(new Date());
			ps.setString(n++, payMerchant.webAddress);
			ps.setString(n++, payMerchant.webAddressAbbr);
			ps.setString(n++, new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(new Date()));
			payMerchant.setContractStrDate(new Date());
			ps.setString(n++, new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(new Date()));
			ps.setString(n++, payMerchant.contractor);
			ps.setString(n++, payMerchant.contractBizNo);
			ps.setString(n++, payMerchant.contractBizName);
			ps.setString(n++, payMerchant.techMobNo);
			ps.setString(n++, payMerchant.bizMobNo);
			ps.setString(n++, payMerchant.servMobNo);
			if (payMerchant.margin == null)
				payMerchant.setMargin(0);
			ps.setLong(n++, payMerchant.margin);
			ps.setString(n++, payMerchant.frozStlSign);
			if (payMerchant.cretMasterityDays == null)
				payMerchant.setCretMasterityDays(0);
			ps.setLong(n++, payMerchant.cretMasterityDays);
			ps.setString(n++, payMerchant.taxRegistrationNo);
			ps.setString(n++, payMerchant.organizationNo);
			ps.setString(n++, payMerchant.cpsSign);
			ps.setLong(n++, payMerchant.regCapital);
			ps.setString(n++, payMerchant.compayEmail);
			ps.setString(n++, payMerchant.compayTelNo);
			ps.setString(n++, payMerchant.compayFax);
			if (payMerchant.cycleDays == null)
				payMerchant.setCycleDays(0);
			ps.setLong(n++, payMerchant.cycleDays);
			ps.setString(n++, payMerchant.codeTypeId);
			ps.setString(n++, payMerchant.cert);
			ps.setString(n++, payMerchant.companyBrief);
			ps.setString(n++, payMerchant.riskLevel);
			ps.setString(n++, payMerchant.bussinessType);
			ps.setString(n++, payMerchant.merStatus);
			ps.setString(n++, new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss").format(new Date()));
			ps.setString(n++, payMerchant.createUser);
			ps.setString(n++, payMerchant.businessLicencePic);
			ps.setString(n++, payMerchant.taxRegistrationPic);
			ps.setString(n++, payMerchant.organizationPic);
			ps.setString(n++, payMerchant.openingLicensesPic);
			ps.setString(n++, payMerchant.certPicFront);
			ps.setString(n++, payMerchant.certPicBack);
			ps.setString(n++, payMerchant.contractPic);
			ps.setString(n++, payMerchant.icpNo);
			// ps.setString(n++,payMerchant.settleFeeCode);
			// ps.setString(n++,payMerchant.withdrawFeeCode);
			// ps.setString(n++,payMerchant.urgentFeeCode);
			ps.setString(n++, payMerchant.province);
			ps.setString(n++, payMerchant.riskDesc);
			ps.setString(n++, payMerchant.region);
			// ps.setString(n++,payMerchant.attentionLine);
			// ps.setString(n++,payMerchant.attentionLineTel);
			// ps.setString(n++,payMerchant.attentionLineEmail);
			ps.setString(n++, payMerchant.businessLicenceNo);
			ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd")
					.format(payMerchant.businessLicenceBeginDate));
			ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd")
					.format(payMerchant.businessLicenceEndDate));
			ps.setString(n++, payMerchant.icpCertNo);
			ps.setString(n++, payMerchant.bankStlAcNoType);
			ps.setString(n++, payMerchant.interfaceIp);
			ps.setString(n++, payMerchant.issuer == null ? ""
					: payMerchant.issuer);
			ps.setString(n++, payMerchant.settlementWay == null ? "0"
					: payMerchant.settlementWay);
			ps.setString(n++, payMerchant.payWaySupported == null ? "0000"
					: payMerchant.payWaySupported);
			ps.setLong(n++, payMerchant.agentGoalDiscountRate);
			ps.setString(n++, payMerchant.chargeWay == null ? "0"
					: payMerchant.chargeWay);
			ps.setLong(n++, payMerchant.preStorageFee == null ? 0L
					: payMerchant.preStorageFee);
			ps.setString(n++, payMerchant.userInterflowFlag);
			ps.setLong(n++, payMerchant.agentPayRate);
			ps.setLong(n++, payMerchant.agentTaxRate);
			ps.executeUpdate();
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null, ps, con);
		}
	}

	public List getList() throws Exception {
		String sql = "select * from PAY_MERCHANT";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(getPayMerchantValue(rs));
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
	 * detail PayMerchant
	 * 
	 * @param id
	 * @return PayMerchant
	 * @throws Exception
	 */
	public PayMerchant detailPayMerchant(String id) throws Exception {
		String sql = "SELECT MER.*,SMR.USER_ID FROM PAY_MERCHANT  MER LEFT JOIN PAY_SALESMAN_MERCHANT_RELATION SMR ON MER.CUST_ID = SMR.MER_NO WHERE ID=?";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next()){
				PayMerchant payMerchant = getPayMerchantValue(rs);
				payMerchant.setUserId(rs.getString("USER_ID"));
				return payMerchant;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
		return null;
	}

	/**
	 * detail PayMerchant
	 * 
	 * @param CUST_ID
	 * @return PayMerchant
	 * @throws Exception
	 */
	public PayMerchant detailPayMerchantByCustId(String id) throws Exception {
		String sql = "select * from PAY_MERCHANT where CUST_ID=?";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			if (rs.next())
				return getPayMerchantValue(rs);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
		return null;
	}
	/**
	 * detail PayMerchant
	 * 
	 * @param CUST_ID
	 * @return PayMerchant
	 * @throws Exception
	 */
	public Map<String,PayMerchant> getMerchantesByCustIds(List<String> ids) throws Exception {
		String sql = "select * from PAY_MERCHANT where";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,PayMerchant> map = new HashMap<String,PayMerchant>();
		if(ids==null||ids.size()==0)return map;
		for(int i=0; i<ids.size(); i++)sql = sql+" cust_id='"+ids.get(i)+"' or ";
		sql = sql.substring(0,sql.length()-4);
		log.info(sql);
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				PayMerchant mer = getPayMerchantValue(rs);
				map.put(mer.custId, mer);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
	}
	/**
	 * 根据商户号码取得指定列值
	 * 
	 * @param merNo
	 * @return
	 * @throws Exception
	 */
	public String getPayMerchantRiskLevel(String merNo) throws Exception {
		String sql = "select RISK_LEVEL from PAY_MERCHANT where CUST_ID=?";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, merNo);
			rs = ps.executeQuery();
			if (rs.next())
				return rs.getString("RISK_LEVEL");
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
	 * 
	 * @param payMerchant
	 * @return
	 */
	private String setPayMerchantSql(PayMerchant payMerchant) {
		StringBuffer sql = new StringBuffer();

		if (payMerchant.province != null && ("省").equals(payMerchant.province)) {
			payMerchant.province = null;
			payMerchant.city = null;
			payMerchant.region = null;
		}
		if (payMerchant.custId != null && payMerchant.custId.length() != 0) {
			sql.append(" CUST_ID like ? and ");
		}
		if (payMerchant.merStatus != null
				&& payMerchant.merStatus.length() != 0) {
			sql.append(" MER_STATUS = ? and ");
		}
		if (payMerchant.checkStatus != null
				&& payMerchant.checkStatus.length() != 0) {
			sql.append(" CHECK_STATUS = ? and ");
		}
		if (payMerchant.riskLevel != null
				&& payMerchant.riskLevel.length() != 0) {
			sql.append(" RISK_LEVEL = ? and ");
		}
		if (payMerchant.createTime != null) {
			sql.append(" CREATE_TIME >= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
		}
		if (payMerchant.createEndTime != null) {
			sql.append(" CREATE_TIME <= to_date(?,'yyyy-mm-dd hh24:mi:ss') and ");
		}
		if (payMerchant.bizType != null && payMerchant.bizType.length() != 0) {
			sql.append(" BIZ_TYPE = ? and ");
		}
		if (payMerchant.merType != null && payMerchant.merType.length() != 0) {
			sql.append(" MER_TYPE = ? and ");
		}
		if (payMerchant.province != null && payMerchant.province.length() != 0) {
			sql.append(" PROVINCE = ? and ");
		}
		if (payMerchant.city != null && payMerchant.city.length() != 0) {
			sql.append(" CITY = ? and ");
		}
		if (payMerchant.region != null && payMerchant.region.length() != 0) {
			sql.append(" REGION = ? and ");
		}
		if (payMerchant.storeName != null
				&& payMerchant.storeName.length() != 0) {
			sql.append(" STORE_NAME like ? and ");
		}
		String tmp = sql.toString();
		if (tmp.length() > 0)
			tmp = tmp.substring(0, tmp.lastIndexOf(" and "));
		return tmp;
	}

	/**
	 * Set query parameter.
	 * 
	 * @param ps
	 * @param payMerchant
	 * @param n
	 * @return
	 * @throws SQLException
	 */
	private int setPayMerchantParameter(PreparedStatement ps,
			PayMerchant payMerchant, int n) throws SQLException {
		if (payMerchant.custId != null && payMerchant.custId.length() != 0) {
			ps.setString(n++, "%" + payMerchant.custId + "%");
		}
		if (payMerchant.merStatus != null
				&& payMerchant.merStatus.length() != 0) {
			ps.setString(n++, payMerchant.merStatus);
		}
		if (payMerchant.checkStatus != null
				&& payMerchant.checkStatus.length() != 0) {
			ps.setString(n++, payMerchant.checkStatus);
		}
		if (payMerchant.riskLevel != null
				&& payMerchant.riskLevel.length() != 0) {
			ps.setString(n++, payMerchant.riskLevel);
		}
		if (payMerchant.createTime != null) {
			ps.setString(
					n++,
					new java.text.SimpleDateFormat("yyyy-MM-dd")
							.format(payMerchant.createTime) + " 00:00:00");
		}
		if (payMerchant.createEndTime != null) {
			ps.setString(
					n++,
					new java.text.SimpleDateFormat("yyyy-MM-dd")
							.format(payMerchant.createEndTime) + " 23:59:59");
		}
		if (payMerchant.bizType != null && payMerchant.bizType.length() != 0) {
			ps.setString(n++, payMerchant.bizType);
		}
		if (payMerchant.merType != null && payMerchant.merType.length() != 0) {
			ps.setString(n++, payMerchant.merType);
		}
		if (payMerchant.province != null && payMerchant.province.length() != 0) {
			ps.setString(n++, payMerchant.province);
		}
		if (payMerchant.city != null && payMerchant.city.length() != 0) {
			ps.setString(n++, payMerchant.city);
		}
		if (payMerchant.region != null && payMerchant.region.length() != 0) {
			ps.setString(n++, payMerchant.region);
		}
		if (payMerchant.storeName != null
				&& payMerchant.storeName.length() != 0) {
			ps.setString(n++, "%" + payMerchant.storeName + "%");
		}
		return n;
	}

	/**
	 * Get records count.
	 * 
	 * @param payMerchant
	 * @return
	 */
	public int getPayMerchantCount(PayMerchant payMerchant) {
		String sqlCon = setPayMerchantSql(payMerchant);
		String sql = "select count(rownum) recordCount from PAY_MERCHANT "
				+ (sqlCon.length() == 0 ? "" : " where " + sqlCon);
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			setPayMerchantParameter(ps, payMerchant, n);
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
	 * 
	 * @param payMerchant
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public List getPayMerchantList(PayMerchant payMerchant, int page, int rows,
			String sort, String order) throws Exception {
		sort = Tools.paraNameToDBColumnName(sort);
		String sqlCon = setPayMerchantSql(payMerchant);
		String sortOrder = sort == null || sort.length() == 0 ? " order by CUST_ID desc"
				: (" order by " + sort + ("desc".equals(order) ? " desc "
						: " asc "));
		String sql = "select * from ("
				+ "  select rownum rowno,tmp1.* from ("
				+ "   SELECT tmp.* ,pro.cash_ac_bal FROM PAY_MERCHANT tmp "
				+ "LEFT JOIN "
				+ "(SELECT pay_ac_no,cash_ac_bal FROM PAY_ACC_PROFILE WHERE AC_TYPE='"
				+ PayConstant.CUST_TYPE_MERCHANT + "') pro "
				+ "ON  tmp.cust_id=pro.pay_ac_no "
				+ (sqlCon.length() == 0 ? "" : " where " + sqlCon) + sortOrder
				+ "  ) tmp1 " + ")  where rowno > " + ((page - 1) * rows)
				+ " and rowno<= " + (page * rows) + sortOrder;
		String sql1 = "select PARENT_ID from PAY_MERCHANT where ";
		String tmp = "";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<PayMerchant> list = new ArrayList<PayMerchant>();
		Map<String, PayMerchant> tMap = new HashMap<String, PayMerchant>();
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			setPayMerchantParameter(ps, payMerchant, n);
			rs = ps.executeQuery();
			while (rs.next()) {
				PayMerchant mTmp = getPayMerchantValue(rs);
				tmp = tmp + "PARENT_ID='" + mTmp.custId + "' or ";
				list.add(mTmp);
				tMap.put(mTmp.custId, mTmp);
			}
			if (tmp.length() > 0) {
				tmp = tmp.substring(0, tmp.length() - 4);
				sql1 = sql1 + tmp + " group by PARENT_ID";
				rs.close();
				ps.close();
				log.info(sql1);
				ps = con.prepareStatement(sql1);
				rs = ps.executeQuery();
				while (rs.next()) {
					String custId = rs.getString("PARENT_ID");
					tMap.get(custId).merType = "3";
				}
			}
			//取得冻结资金、保证金
			String frozenSql = "select sum(CUR_AMT) frozenBal,acc_no from PAY_ACC_PROFILE_FROZEN where ";
			String marginSql = "select * from PAY_MARGIN where ";
			Map<String,PayMerchant> mMap = new HashMap<String,PayMerchant>();
			for(int i=0; i<list.size(); i++){
				PayMerchant m = list.get(i);
				mMap.put(m.custId,m);
				frozenSql = frozenSql + "acc_no='"+m.custId+"' or ";
				marginSql = marginSql + "cust_id='"+m.custId+"' or ";
			}
			if(list.size()>0){
				rs.close();
				ps.close();
				frozenSql = frozenSql.substring(0,frozenSql.length()-4)+" group by acc_no";
				ps = con.prepareStatement(frozenSql);
				rs = ps.executeQuery();
				while (rs.next()){
					PayMerchant m = mMap.get(rs.getString("acc_no"));
					m.frozenBal = rs.getLong("frozenBal");
				}
				rs.close();
				ps.close();
				marginSql = marginSql.substring(0,marginSql.length()-4);
				ps = con.prepareStatement(marginSql);
				rs = ps.executeQuery();
				while (rs.next()){
					PayMerchant m = mMap.get(rs.getString("cust_id"));
					m.marginBal = rs.getLong("PAID_IN_AMT");
				}
			}
			log.info("获取冻结金额："+frozenSql);
			log.info("获取保证金："+marginSql);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
	}

	/**
	 * remove PayMerchant
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void removePayMerchant(String id) throws Exception {
		String sql = "delete from PAY_MERCHANT_ROOT where ID=?";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null, ps, con);
		}
	}

	/**
	 * update PayMerchant
	 * 
	 * @param payMerchant
	 * @throws Exception
	 */
	public int updatePayMerchant(PayMerchant payMerchant) throws Exception {
		String sqlTmp = "";
		if (payMerchant.merType != null)
			sqlTmp = sqlTmp + " MER_TYPE=?,";
		if (payMerchant.bizType != null)
			sqlTmp = sqlTmp + " BIZ_TYPE=?,";
		if (payMerchant.storeName != null)
			sqlTmp = sqlTmp + " STORE_NAME=?,";
		if (payMerchant.storeShortname != null)
			sqlTmp = sqlTmp + " STORE_SHORTNAME=?,";
		if (payMerchant.merLawPerson != null)
			sqlTmp = sqlTmp + " MER_LAW_PERSON=data_encrypt(?),";
		if (payMerchant.techContact != null)
			sqlTmp = sqlTmp + " TECH_CONTACT=data_encrypt(?),";
		if (payMerchant.techTelNo != null)
			sqlTmp = sqlTmp + " TECH_TEL_NO=data_encrypt(?),";
		if (payMerchant.techEmail != null)
			sqlTmp = sqlTmp + " TECH_EMAIL=data_encrypt(?),";
		if (payMerchant.bizContact != null)
			sqlTmp = sqlTmp + " BIZ_CONTACT=data_encrypt(?),";
		if (payMerchant.bizTelNo != null)
			sqlTmp = sqlTmp + " BIZ_TEL_NO=data_encrypt(?),";
		if (payMerchant.bizEmail != null)
			sqlTmp = sqlTmp + " BIZ_EMAIL=data_encrypt(?),";
		if (payMerchant.servContact != null)
			sqlTmp = sqlTmp + " SERV_CONTACT=data_encrypt(?),";
		if (payMerchant.servTelNo != null)
			sqlTmp = sqlTmp + " SERV_TEL_NO=data_encrypt(?),";
		if (payMerchant.servEmail != null)
			sqlTmp = sqlTmp + " SERV_EMAIL=data_encrypt(?),";
		if (payMerchant.merAddr != null)
			sqlTmp = sqlTmp + " MER_ADDR=?,";
		if (payMerchant.bizRange != null)
			sqlTmp = sqlTmp + " BIZ_RANGE=?,";
		if (payMerchant.merOpnBankCode != null)
			sqlTmp = sqlTmp + " MER_OPN_BANK_CODE=?,";
		if (payMerchant.bankStlAcNo != null)
			sqlTmp = sqlTmp + " BANK_STL_AC_NO=data_encrypt(?),";
		if (payMerchant.rfCommFlg != null)
			sqlTmp = sqlTmp + " RF_COMM_FLG=?,";
		if (payMerchant.rfRecFeeFlg != null)
			sqlTmp = sqlTmp + " RF_REC_FEE_FLG=?,";
		if (payMerchant.city != null)
			sqlTmp = sqlTmp + " CITY=?,";
		if (payMerchant.postCode != null)
			sqlTmp = sqlTmp + " POST_CODE=?,";
		if (payMerchant.lawPersonType != null)
			sqlTmp = sqlTmp + " LAW_PERSON_TYPE=?,";
		if (payMerchant.lawPersonCretType != null)
			sqlTmp = sqlTmp + " LAW_PERSON_CRET_TYPE=?,";
		if (payMerchant.lawPersonCretNo != null)
			sqlTmp = sqlTmp + " LAW_PERSON_CRET_NO=data_encrypt(?),";
		if (payMerchant.longTermSign != null)
			sqlTmp = sqlTmp + " LONG_TERM_SIGN=?,";
		if (payMerchant.webAddress != null)
			sqlTmp = sqlTmp + " WEB_ADDRESS=?,";
		if (payMerchant.webAddressAbbr != null)
			sqlTmp = sqlTmp + " WEB_ADDRESS_ABBR=?,";
		if (payMerchant.contractor != null)
			sqlTmp = sqlTmp + " CONTRACTOR=data_encrypt(?),";
		if (payMerchant.contractBizNo != null)
			sqlTmp = sqlTmp + " CONTRACT_BIZ_NO=?,";
		if (payMerchant.contractBizName != null)
			sqlTmp = sqlTmp + " CONTRACT_BIZ_NAME=?,";
		if (payMerchant.techMobNo != null)
			sqlTmp = sqlTmp + " TECH_MOB_NO=data_encrypt(?),";
		if (payMerchant.bizMobNo != null)
			sqlTmp = sqlTmp + " BIZ_MOB_NO=data_encrypt(?),";
		if (payMerchant.servMobNo != null)
			sqlTmp = sqlTmp + " SERV_MOB_NO=data_encrypt(?),";
		if (payMerchant.margin != null)
			sqlTmp = sqlTmp + " MARGIN=?,";
		if (payMerchant.frozStlSign != null)
			sqlTmp = sqlTmp + " FROZ_STL_SIGN=?,";
		if (payMerchant.cretMasterityDays != null)
			sqlTmp = sqlTmp + " CRET_MASTERITY_DAYS=?,";
		if (payMerchant.taxRegistrationNo != null)
			sqlTmp = sqlTmp + " TAX_REGISTRATION_NO=?,";
		if (payMerchant.organizationNo != null)
			sqlTmp = sqlTmp + " ORGANIZATION_NO=?,";
		if (payMerchant.cpsSign != null)
			sqlTmp = sqlTmp + " CPS_SIGN=?,";
		if (payMerchant.regCapital != null)
			sqlTmp = sqlTmp + " REG_CAPITAL=?,";
		if (payMerchant.agentGoalDiscountRate != null)
			sqlTmp = sqlTmp + " AGENT_GOAL_DISCOUNT_RATE=?,";
		if (payMerchant.compayEmail != null)
			sqlTmp = sqlTmp + " COMPAY_EMAIL=?,";
		if (payMerchant.compayTelNo != null)
			sqlTmp = sqlTmp + " COMPAY_TEL_NO=?,";
		if (payMerchant.compayFax != null)
			sqlTmp = sqlTmp + " COMPAY_FAX=?,";
		if (payMerchant.cycleDays != null)
			sqlTmp = sqlTmp + " CYCLE_DAYS=?,";
		if (payMerchant.codeTypeId != null)
			sqlTmp = sqlTmp + " CODE_TYPE_ID=?,";
		if (payMerchant.cert != null)
			sqlTmp = sqlTmp + " CERT=?,";
		if (payMerchant.companyBrief != null)
			sqlTmp = sqlTmp + " COMPANY_BRIEF=?,";
		if (payMerchant.riskLevel != null)
			sqlTmp = sqlTmp + " RISK_LEVEL=?,";
		if (payMerchant.bussinessType != null)
			sqlTmp = sqlTmp + " BUSSINESS_TYPE=?,";
		if (payMerchant.merStatus != null)
			sqlTmp = sqlTmp + " MER_STATUS=?,";
		if (payMerchant.createUser != null)
			sqlTmp = sqlTmp + " CREATE_USER=?,";
		if (payMerchant.businessLicencePic != null)
			sqlTmp = sqlTmp + " BUSINESS_LICENCE_PIC=?,";
		if (payMerchant.taxRegistrationPic != null)
			sqlTmp = sqlTmp + " TAX_REGISTRATION_PIC=?,";
		if (payMerchant.organizationPic != null)
			sqlTmp = sqlTmp + " ORGANIZATION_PIC=?,";
		if (payMerchant.openingLicensesPic != null)
			sqlTmp = sqlTmp + " OPENING_LICENSES_PIC=?,";
		if (payMerchant.certPicFront != null)
			sqlTmp = sqlTmp + " CERT_PIC_FRONT=?,";
		if (payMerchant.certPicBack != null)
			sqlTmp = sqlTmp + " CERT_PIC_BACK=?,";
		if (payMerchant.contractPic != null)
			sqlTmp = sqlTmp + " CONTRACT_PIC=?,";
		if (payMerchant.icpNo != null)
			sqlTmp = sqlTmp + " ICP_NO=?,";
		if (payMerchant.bankStlAcNoType != null)
			sqlTmp = sqlTmp + " BANK_STL_AC_NO_TYPE=?,";
		if (payMerchant.interfaceIp != null)
			sqlTmp = sqlTmp + " INTERFACE_IP=?,";
		if (payMerchant.payWaySupported != null)
			sqlTmp = sqlTmp + " PAY_WAY_SUPPORTED=?,";
		if (payMerchant.chargeWay != null)
			sqlTmp = sqlTmp + " CHARGE_WAY=?,";
		if (payMerchant.preStorageFee != null)
			sqlTmp = sqlTmp + " PRE_STORAGE_FEE=?,";
		// if(payMerchant.settleFeeCode != null)sqlTmp = sqlTmp +
		// " SETTLE_FEE_CODE=?,";
		// if(payMerchant.withdrawFeeCode != null)sqlTmp = sqlTmp +
		// " WITHDRAW_FEE_CODE=?,";
		// if(payMerchant.urgentFeeCode != null)sqlTmp = sqlTmp +
		// " URGENT_FEE_CODE=?,";
		if (payMerchant.province != null)
			sqlTmp = sqlTmp + " PROVINCE=?,";
		if (payMerchant.riskDesc != null)
			sqlTmp = sqlTmp + " RISK_DESC=?,";
		if (payMerchant.region != null)
			sqlTmp = sqlTmp + " REGION=?,";
		if (payMerchant.attentionLine != null)
			sqlTmp = sqlTmp + " ATTENTION_LINE=data_encrypt(?),";
		if (payMerchant.attentionLineTel != null)
			sqlTmp = sqlTmp + " ATTENTION_LINE_TEL=data_encrypt(?),";
		if (payMerchant.attentionLineEmail != null)
			sqlTmp = sqlTmp + " ATTENTION_LINE_EMAIL=data_encrypt(?),";
		if (payMerchant.businessLicenceNo != null)
			sqlTmp = sqlTmp + " BUSINESS_LICENCE_NO=?,";
		if (payMerchant.businessLicenceBeginDate != null)
			sqlTmp = sqlTmp
					+ " BUSINESS_LICENCE_BEGIN_DATE=to_date(?,'yyyy-mm-dd'),";
		if (payMerchant.businessLicenceEndDate != null)
			sqlTmp = sqlTmp
					+ " BUSINESS_LICENCE_END_DATE=to_date(?,'yyyy-mm-dd'),";
		if (payMerchant.icpCertNo != null)
			sqlTmp = sqlTmp + " ICP_CERT_NO=?,";
		if (payMerchant.checkStatus != null)
			sqlTmp = sqlTmp + " CHECK_STATUS=?,";
		if (payMerchant.issuer != null)
			sqlTmp = sqlTmp + " ISSUER=?,";
		if (payMerchant.settlementWay != null)
			sqlTmp = sqlTmp + " SETTLEMENT_WAY=?,";
		if (payMerchant.parentId != null)
			sqlTmp = sqlTmp + " PARENT_ID=?,";
		if (payMerchant.userInterflowFlag != null)
			sqlTmp = sqlTmp + " USER_INTERFLOW_FLAG=?,";
		if (payMerchant.agentPayRate != null)
			sqlTmp = sqlTmp + " AGENT_PAY_RATE=?,";
		if (payMerchant.agentTaxRate != null)
			sqlTmp = sqlTmp + " AGENT_TAX_RATE=?,";
		if (sqlTmp.length() == 0)
			return 0;
		String sql = "update PAY_MERCHANT_ROOT " + "set "
				+ sqlTmp.substring(0, sqlTmp.length() - 1) + " where ID=?";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			if (payMerchant.merType != null)
				ps.setString(n++, payMerchant.merType);
			if (payMerchant.bizType != null)
				ps.setString(n++, payMerchant.bizType);
			if (payMerchant.storeName != null)
				ps.setString(n++, payMerchant.storeName);
			if (payMerchant.storeShortname != null)
				ps.setString(n++, payMerchant.storeShortname);
			if (payMerchant.merLawPerson != null)
				ps.setString(n++, payMerchant.merLawPerson);
			if (payMerchant.techContact != null)
				ps.setString(n++, payMerchant.techContact);
			if (payMerchant.techTelNo != null)
				ps.setString(n++, payMerchant.techTelNo);
			if (payMerchant.techEmail != null)
				ps.setString(n++, payMerchant.techEmail);
			if (payMerchant.bizContact != null)
				ps.setString(n++, payMerchant.bizContact);
			if (payMerchant.bizTelNo != null)
				ps.setString(n++, payMerchant.bizTelNo);
			if (payMerchant.bizEmail != null)
				ps.setString(n++, payMerchant.bizEmail);
			if (payMerchant.servContact != null)
				ps.setString(n++, payMerchant.servContact);
			if (payMerchant.servTelNo != null)
				ps.setString(n++, payMerchant.servTelNo);
			if (payMerchant.servEmail != null)
				ps.setString(n++, payMerchant.servEmail);
			if (payMerchant.merAddr != null)
				ps.setString(n++, payMerchant.merAddr);
			if (payMerchant.bizRange != null)
				ps.setString(n++, payMerchant.bizRange);
			if (payMerchant.merOpnBankCode != null)
				ps.setString(n++, payMerchant.merOpnBankCode);
			if (payMerchant.bankStlAcNo != null)
				ps.setString(n++, payMerchant.bankStlAcNo);
			if (payMerchant.rfCommFlg != null)
				ps.setString(n++, payMerchant.rfCommFlg);
			if (payMerchant.rfRecFeeFlg != null)
				ps.setString(n++, payMerchant.rfRecFeeFlg);
			if (payMerchant.city != null)
				ps.setString(n++, payMerchant.city);
			if (payMerchant.postCode != null)
				ps.setString(n++, payMerchant.postCode);
			if (payMerchant.lawPersonType != null)
				ps.setString(n++, payMerchant.lawPersonType);
			if (payMerchant.lawPersonCretType != null)
				ps.setString(n++, payMerchant.lawPersonCretType);
			if (payMerchant.lawPersonCretNo != null)
				ps.setString(n++, payMerchant.lawPersonCretNo);
			if (payMerchant.longTermSign != null)
				ps.setString(n++, payMerchant.longTermSign);
			if (payMerchant.webAddress != null)
				ps.setString(n++, payMerchant.webAddress);
			if (payMerchant.webAddressAbbr != null)
				ps.setString(n++, payMerchant.webAddressAbbr);
			if (payMerchant.contractor != null)
				ps.setString(n++, payMerchant.contractor);
			if (payMerchant.contractBizNo != null)
				ps.setString(n++, payMerchant.contractBizNo);
			if (payMerchant.contractBizName != null)
				ps.setString(n++, payMerchant.contractBizName);
			if (payMerchant.techMobNo != null)
				ps.setString(n++, payMerchant.techMobNo);
			if (payMerchant.bizMobNo != null)
				ps.setString(n++, payMerchant.bizMobNo);
			if (payMerchant.servMobNo != null)
				ps.setString(n++, payMerchant.servMobNo);
			if (payMerchant.margin != null)
				ps.setLong(n++, payMerchant.margin);
			if (payMerchant.frozStlSign != null)
				ps.setString(n++, payMerchant.frozStlSign);
			if (payMerchant.cretMasterityDays != null)
				ps.setLong(n++, payMerchant.cretMasterityDays);
			if (payMerchant.taxRegistrationNo != null)
				ps.setString(n++, payMerchant.taxRegistrationNo);
			if (payMerchant.organizationNo != null)
				ps.setString(n++, payMerchant.organizationNo);
			if (payMerchant.cpsSign != null)
				ps.setString(n++, payMerchant.cpsSign);
			if (payMerchant.regCapital != null)
				ps.setLong(n++, payMerchant.regCapital);
			if (payMerchant.agentGoalDiscountRate != null)
				ps.setLong(n++, payMerchant.agentGoalDiscountRate);
			if (payMerchant.compayEmail != null)
				ps.setString(n++, payMerchant.compayEmail);
			if (payMerchant.compayTelNo != null)
				ps.setString(n++, payMerchant.compayTelNo);
			if (payMerchant.compayFax != null)
				ps.setString(n++, payMerchant.compayFax);
			if (payMerchant.cycleDays != null)
				ps.setLong(n++, payMerchant.cycleDays);
			if (payMerchant.codeTypeId != null)
				ps.setString(n++, payMerchant.codeTypeId);
			if (payMerchant.cert != null)
				ps.setString(n++, payMerchant.cert);
			if (payMerchant.companyBrief != null)
				ps.setString(n++, payMerchant.companyBrief);
			if (payMerchant.riskLevel != null)
				ps.setString(n++, payMerchant.riskLevel);
			if (payMerchant.bussinessType != null)
				ps.setString(n++, payMerchant.bussinessType);
			if (payMerchant.merStatus != null)
				ps.setString(n++, payMerchant.merStatus);
			if (payMerchant.createUser != null)
				ps.setString(n++, payMerchant.createUser);
			if (payMerchant.businessLicencePic != null)
				ps.setString(n++, payMerchant.businessLicencePic);
			if (payMerchant.taxRegistrationPic != null)
				ps.setString(n++, payMerchant.taxRegistrationPic);
			if (payMerchant.organizationPic != null)
				ps.setString(n++, payMerchant.organizationPic);
			if (payMerchant.openingLicensesPic != null)
				ps.setString(n++, payMerchant.openingLicensesPic);
			if (payMerchant.certPicFront != null)
				ps.setString(n++, payMerchant.certPicFront);
			if (payMerchant.certPicBack != null)
				ps.setString(n++, payMerchant.certPicBack);
			if (payMerchant.contractPic != null)
				ps.setString(n++, payMerchant.contractPic);
			if (payMerchant.icpNo != null)
				ps.setString(n++, payMerchant.icpNo);
			if (payMerchant.bankStlAcNoType != null)
				ps.setString(n++, payMerchant.bankStlAcNoType);
			if (payMerchant.interfaceIp != null)
				ps.setString(n++, payMerchant.interfaceIp);
			if (payMerchant.payWaySupported != null)
				ps.setString(n++, payMerchant.payWaySupported);
			if (payMerchant.chargeWay != null)
				ps.setString(n++, payMerchant.chargeWay);
			if (payMerchant.preStorageFee != null)
				ps.setLong(n++, payMerchant.preStorageFee);
			// if(payMerchant.settleFeeCode !=
			// null)ps.setString(n++,payMerchant.settleFeeCode);
			// if(payMerchant.withdrawFeeCode !=
			// null)ps.setString(n++,payMerchant.withdrawFeeCode);
			// if(payMerchant.urgentFeeCode !=
			// null)ps.setString(n++,payMerchant.urgentFeeCode);
			if (payMerchant.province != null)
				ps.setString(n++, payMerchant.province);
			if (payMerchant.riskDesc != null)
				ps.setString(n++, payMerchant.riskDesc);
			if (payMerchant.region != null)
				ps.setString(n++, payMerchant.region);
			if (payMerchant.attentionLine != null)
				ps.setString(n++, payMerchant.attentionLine);
			if (payMerchant.attentionLineTel != null)
				ps.setString(n++, payMerchant.attentionLineTel);
			if (payMerchant.attentionLineEmail != null)
				ps.setString(n++, payMerchant.attentionLineEmail);
			if (payMerchant.businessLicenceNo != null)
				ps.setString(n++, payMerchant.businessLicenceNo);
			if (payMerchant.businessLicenceBeginDate != null)
				ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd")
						.format(payMerchant.businessLicenceBeginDate));
			if (payMerchant.businessLicenceEndDate != null)
				ps.setString(n++, new java.text.SimpleDateFormat("yyyy-MM-dd")
						.format(payMerchant.businessLicenceEndDate));
			if (payMerchant.icpCertNo != null)
				ps.setString(n++, payMerchant.icpCertNo);
			if (payMerchant.checkStatus != null)
				ps.setString(n++, payMerchant.checkStatus);
			if (payMerchant.issuer != null)
				ps.setString(n++, payMerchant.issuer);
			if (payMerchant.settlementWay != null)
				ps.setString(n++, payMerchant.settlementWay);
			if (payMerchant.parentId != null)
				ps.setString(n++, payMerchant.parentId);
			if (payMerchant.userInterflowFlag != null)
				ps.setString(n++, payMerchant.userInterflowFlag);
			if (payMerchant.agentPayRate != null)
				ps.setLong(n++, payMerchant.agentPayRate);
			if (payMerchant.agentTaxRate != null)
				ps.setLong(n++, payMerchant.agentTaxRate);
			if (payMerchant.id != null)
				ps.setString(n++, payMerchant.id);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null, ps, con);
		}
	}

	/**
	 * 查询最大的商户编号
	 * 
	 * @return
	 */
	public String selectMaxCustId() {
		String sql = "select max(CUST_ID) max_cust_id from PAY_MERCHANT";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("max_cust_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, con);
		}
		return null;
	}

	/**
	 * 更新商户状态信息
	 * 
	 * @param custId
	 *            商户id
	 * @param columName
	 *            列名
	 * @param operation
	 *            操作
	 * @throws Exception
	 */
	public void updatePayMerchantStatus(String custId, String columName,
			String operation) throws Exception {
		String tmp = columName + " = ? ";
		if ("MER_LAW_PERSON".equalsIgnoreCase(columName)
				|| "TECH_CONTACT".equalsIgnoreCase(columName)
				|| "TECH_TEL_NO".equalsIgnoreCase(columName)
				|| "TECH_EMAIL".equalsIgnoreCase(columName)
				|| "BIZ_CONTACT".equalsIgnoreCase(columName)
				|| "BIZ_TEL_NO".equalsIgnoreCase(columName)
				|| "BIZ_EMAIL".equalsIgnoreCase(columName)
				|| "SERV_CONTACT".equalsIgnoreCase(columName)
				|| "SERV_TEL_NO".equalsIgnoreCase(columName)
				|| "SERV_EMAIL".equalsIgnoreCase(columName)
				|| "BANK_STL_AC_NO".equalsIgnoreCase(columName)
				|| "LAW_PERSON_CRET_NO".equalsIgnoreCase(columName)
				|| "CONTRACTOR".equalsIgnoreCase(columName)
				|| "TECH_MOB_NO".equalsIgnoreCase(columName)
				|| "BIZ_MOB_NO".equalsIgnoreCase(columName)
				|| "SERV_MOB_NO".equalsIgnoreCase(columName)
				|| "ATTENTION_LINE".equalsIgnoreCase(columName)
				|| "ATTENTION_LINE_TEL".equalsIgnoreCase(columName)
				|| "ATTENTION_LINE_EMAIL".equalsIgnoreCase(columName))
			tmp = columName + " = data_encrypt(?) ";
		String sql = "UPDATE PAY_MERCHANT_ROOT SET " + tmp
				+ " WHERE CUST_ID = '" + custId + "'";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, operation);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null, ps, con);
		}
	}

	/**
	 * 根据商户编号查询商户详情信息
	 * 
	 * @param custId
	 *            商户编号
	 * @return 返回查询结果
	 */
	@SuppressWarnings("static-access")
	public PayMerchant selectByCustId(String custId) {
		String sql = "select * from PAY_MERCHANT WHERE CUST_ID = ? ";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, custId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return this.getPayMerchantValue(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, con);
		}
		return null;
	}

	/**
	 * 获取商户详情信息
	 * 
	 * @param custId
	 * @return
	 */
	public PayMerchant selectByMerchantDetail(String custId) {
		String sql = "SELECT "
				+ "M.*,SMR.USER_ID,"
				+ "S.CUST_BANK_DEPOSIT_NAME,"
				+ "S.CUST_STL_BANK_AC_NO,"
				+ "S.DEPOSIT_BANK_CODE,"
				+ "S.CUST_STL_TYPE,"
				+ "S.CUST_STL_BANK_AC_NO,"
				+ "S.CUST_SET_PERIOD,"
				+ "S.CUST_SET_FREY,"
				+ "S.CUST_SET_FREY_AGENT,"
				+ "S.CUST_STL_TIME_SET,"
				+ "S.CUST_SET_PERIOD_AGENT,"
				+ "S.CUST_STL_TIME_SET_AGENT,"
				+ "S.CUST_SET_PERIOD_DAISHOU,"
				+ "S.CUST_STL_TIME_SET_DAISHOU,"
				+ "(SELECT FEE_NAME FROM PAY_FEE_RATE WHERE FEE_CODE = M.SETTLE_FEE_CODE) AS SETTLE_FEE_NAME,"
				+ "(SELECT FEE_NAME FROM PAY_FEE_RATE WHERE FEE_CODE = M.WITHDRAW_FEE_CODE) AS WITHDRAW_FEE_NAME,"
				+ "(SELECT FEE_NAME FROM PAY_FEE_RATE WHERE FEE_CODE = M.URGENT_FEE_CODE) AS URGENT_FEE_NAME,"
				+ "S.MIN_STL_BALANCE " + "FROM PAY_MERCHANT M "
				+ "LEFT JOIN PAY_CUST_STL_INFO S "
				+ "ON M.CUST_ID  = S.CUST_ID " 
				+ "LEFT JOIN PAY_SALESMAN_MERCHANT_RELATION SMR "
				+ "ON M.CUST_ID    = SMR.MER_NO "
				+ "WHERE M.CUST_ID = ? ";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, custId);
			rs = ps.executeQuery();
			if (rs.next()) {
				PayMerchant payMerchant = this.getPayMerchantValue(rs);
				payMerchant.userId = rs.getString("USER_ID");
				return payMerchant;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, con);
		}
		return null;
	}

	/**
	 * 商户审核
	 * 
	 * @param mer
	 */
	public int checkMerchant(PayMerchant mer) {
		String sql = "update PAY_MERCHANT_ROOT set RISK_LEVEL=?,CHECK_STATUS = ?,CHECK_INFO = ?,CHECK_TIME=sysdate,RISK_DESC=?,CHECK_USER=? where CUST_ID = ? ";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			ps.setString(n++, mer.riskLevel);
			ps.setString(n++, mer.checkStatus);
			ps.setString(n++, mer.checkInfo);
			ps.setString(n++, mer.riskDesc);
			ps.setString(n++, mer.checkUser);
			ps.setString(n++, mer.custId);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(null, ps, con);
		}
		return 0;
	}

	/**
	 * 检查商户是否存在 通过商户编号
	 * 
	 * @param custId
	 * @return
	 */
	public boolean isExistMerchant(String custType, String custId) {
		String sql = "SELECT ID FROM PAY_MERCHANT  WHERE CUST_ID = ? ";
		if (com.PayConstant.CUST_TYPE_USER.equals(custType))
			sql = "SELECT USER_ID ID FROM PAY_TRAN_USER_INFO  WHERE USER_ID = ? ";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, custId);
			rs = ps.executeQuery();
			if (rs.next())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, con);
		}
		return false;
	}

	/**
	 * 通过表的列检查其值是否存在
	 * 
	 * @param col
	 * @param value
	 * @return
	 */
	public boolean isExistRecordByColumn(String col, String value) {
		String sql = "SELECT ID FROM PAY_MERCHANT  WHERE " + col + " = ? ";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, value);
			rs = ps.executeQuery();
			if (rs.next())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, con);
		}
		return false;
	}

	public String getMerchantNo() {
		String sql = "select PAY_MERCHANT_NO.nextval CUST_ID from dual";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
				return rs.getString("CUST_ID");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, con);
		}
		return "";
	}

	public static synchronized PayMerchant getPayMerchantSettleFeeValue(
			ResultSet rs) throws Exception {
		PayMerchant payMerchant = new PayMerchant();
		payMerchant.payCustStlInfo = new PayCustStlInfo();
		// payMerchant.payFeeRate = new PayFeeRate();
		payMerchant.id = rs.getString("ID");
		payMerchant.custId = rs.getString("CUST_ID");
		payMerchant.storeName = rs.getString("STORE_NAME");
		payMerchant.storeShortname = rs.getString("STORE_SHORTNAME");
		payMerchant.merType = rs.getString("MER_TYPE");
		payMerchant.bizType = rs.getString("BIZ_TYPE");
		payMerchant.merLawPerson = rs.getString("MER_LAW_PERSON");
		payMerchant.payChannel = rs.getString("PAY_CHANNEL");
		payMerchant.techContact = rs.getString("TECH_CONTACT");
		payMerchant.techTelNo = rs.getString("TECH_TEL_NO");
		payMerchant.techEmail = rs.getString("TECH_EMAIL");
		payMerchant.bizContact = rs.getString("BIZ_CONTACT");
		payMerchant.bizTelNo = rs.getString("BIZ_TEL_NO");
		payMerchant.bizEmail = rs.getString("BIZ_EMAIL");
		payMerchant.servContact = rs.getString("SERV_CONTACT");
		payMerchant.servTelNo = rs.getString("SERV_TEL_NO");
		payMerchant.servEmail = rs.getString("SERV_EMAIL");
		payMerchant.merAddr = rs.getString("MER_ADDR");
		payMerchant.bizRange = rs.getString("BIZ_RANGE");
		payMerchant.merOpnBankCode = rs.getString("MER_OPN_BANK_CODE");
		payMerchant.bankStlAcNo = rs.getString("BANK_STL_AC_NO");
		payMerchant.rfCommFlg = rs.getString("RF_COMM_FLG");
		payMerchant.rfRecFeeFlg = rs.getString("RF_REC_FEE_FLG");
		payMerchant.city = rs.getString("CITY");
		payMerchant.postCode = rs.getString("POST_CODE");
		payMerchant.lawPersonType = rs.getString("LAW_PERSON_TYPE");
		payMerchant.lawPersonCretType = rs.getString("LAW_PERSON_CRET_TYPE");
		payMerchant.lawPersonCretNo = rs.getString("LAW_PERSON_CRET_NO");
		payMerchant.longTermSign = rs.getString("LONG_TERM_SIGN");
		payMerchant.certBeginDate = rs.getTimestamp("CERT_BEGIN_DATE");
		payMerchant.certEndDate = rs.getTimestamp("CERT_END_DATE");
		payMerchant.webAddress = rs.getString("WEB_ADDRESS");
		payMerchant.webAddressAbbr = rs.getString("WEB_ADDRESS_ABBR");
		payMerchant.contractStrDate = rs.getTimestamp("CONTRACT_STR_DATE");
		payMerchant.contractEndDate = rs.getTimestamp("CONTRACT_END_DATE");
		payMerchant.contractor = rs.getString("CONTRACTOR");
		payMerchant.contractBizNo = rs.getString("CONTRACT_BIZ_NO");
		payMerchant.contractBizName = rs.getString("CONTRACT_BIZ_NAME");
		payMerchant.techMobNo = rs.getString("TECH_MOB_NO");
		payMerchant.bizMobNo = rs.getString("BIZ_MOB_NO");
		payMerchant.servMobNo = rs.getString("SERV_MOB_NO");
		payMerchant.margin = rs.getLong("MARGIN");
		payMerchant.frozStlSign = rs.getString("FROZ_STL_SIGN");
		payMerchant.cretMasterityDays = rs.getLong("CRET_MASTERITY_DAYS");
		payMerchant.taxRegistrationNo = rs.getString("TAX_REGISTRATION_NO");
		payMerchant.organizationNo = rs.getString("ORGANIZATION_NO");
		payMerchant.cpsSign = rs.getString("CPS_SIGN");
		payMerchant.regCapital = rs.getLong("REG_CAPITAL");
		payMerchant.agentGoalDiscountRate = rs.getLong("AGENT_GOAL_DISCOUNT_RATE");
		payMerchant.agentPayRate = rs.getLong("AGENT_PAY_RATE");
		payMerchant.agentTaxRate = rs.getLong("AGENT_TAX_RATE");
		payMerchant.compayEmail = rs.getString("COMPAY_EMAIL");
		payMerchant.compayTelNo = rs.getString("COMPAY_TEL_NO");
		payMerchant.compayFax = rs.getString("COMPAY_FAX");
		payMerchant.cycleDays = rs.getLong("CYCLE_DAYS");
		payMerchant.codeTypeId = rs.getString("CODE_TYPE_ID");
		payMerchant.cert = rs.getString("CERT");
		payMerchant.companyBrief = rs.getString("COMPANY_BRIEF");
		payMerchant.riskLevel = rs.getString("RISK_LEVEL");
		payMerchant.bussinessType = rs.getString("BUSSINESS_TYPE");
		payMerchant.merStatus = rs.getString("MER_STATUS");
		payMerchant.createTime = rs.getTimestamp("CREATE_TIME");
		payMerchant.createUser = rs.getString("CREATE_USER");
		payMerchant.businessLicencePic = rs.getString("BUSINESS_LICENCE_PIC");
		payMerchant.taxRegistrationPic = rs.getString("TAX_REGISTRATION_PIC");
		payMerchant.organizationPic = rs.getString("ORGANIZATION_PIC");
		payMerchant.openingLicensesPic = rs.getString("OPENING_LICENSES_PIC");
		payMerchant.certPicFront = rs.getString("CERT_PIC_FRONT");
		payMerchant.certPicBack = rs.getString("CERT_PIC_BACK");
		payMerchant.contractPic = rs.getString("CONTRACT_PIC");
		payMerchant.icpNo = rs.getString("ICP_NO");
		// payMerchant.settleFeeCode = rs.getString("SETTLE_FEE_CODE");
		// payMerchant.withdrawFeeCode = rs.getString("WITHDRAW_FEE_CODE");
		// payMerchant.urgentFeeCode = rs.getString("URGENT_FEE_CODE");
		payMerchant.province = rs.getString("PROVINCE");
		payMerchant.checkUser = rs.getString("CHECK_USER");
		payMerchant.checkTime = rs.getTimestamp("CHECK_TIME");
		payMerchant.checkStatus = rs.getString("CHECK_STATUS");
		payMerchant.checkInfo = rs.getString("CHECK_INFO");
		payMerchant.riskDesc = rs.getString("RISK_DESC");
		payMerchant.region = rs.getString("REGION");
		payMerchant.attentionLine = rs.getString("ATTENTION_LINE");
		payMerchant.attentionLineTel = rs.getString("ATTENTION_LINE_TEL");
		payMerchant.attentionLineEmail = rs.getString("ATTENTION_LINE_EMAIL");
		payMerchant.businessLicenceNo = rs.getString("BUSINESS_LICENCE_NO");
		payMerchant.businessLicenceBeginDate = rs
				.getTimestamp("BUSINESS_LICENCE_BEGIN_DATE");
		payMerchant.businessLicenceEndDate = rs
				.getTimestamp("BUSINESS_LICENCE_END_DATE");
		payMerchant.icpCertNo = rs.getString("ICP_CERT_NO");
		try {
			payMerchant.payCustStlInfo.seqNo = rs.getString("SEQ_NO");
			payMerchant.payCustStlInfo.payAcNo = rs.getString("PAY_AC_NO");
			payMerchant.payCustStlInfo.custId = rs.getString("CUST_ID");
			payMerchant.payCustStlInfo.custBankDepositName = rs
					.getString("CUST_BANK_DEPOSIT_NAME");
			payMerchant.payCustStlInfo.depositBankCode = rs
					.getString("DEPOSIT_BANK_CODE");
			payMerchant.payCustStlInfo.depositBankBrchName = rs
					.getString("DEPOSIT_BANK_BRCH_NAME");
			payMerchant.payCustStlInfo.custStlBankAcNo = rs
					.getString("CUST_STL_BANK_AC_NO");
			payMerchant.payCustStlInfo.custStlType = rs
					.getString("CUST_STL_TYPE");
			payMerchant.payCustStlInfo.custSetPeriod = rs
					.getString("CUST_SET_PERIOD");
			payMerchant.payCustStlInfo.custSetFrey = rs
					.getString("CUST_SET_FREY");
			payMerchant.payCustStlInfo.custStlTimeSet = rs
					.getString("CUST_STL_TIME_SET");
			payMerchant.payCustStlInfo.minStlBalance = rs
					.getLong("MIN_STL_BALANCE");
			payMerchant.payCustStlInfo.creOperId = rs.getString("CRE_OPER_ID");
			payMerchant.payCustStlInfo.creTime = rs.getTimestamp("CRE_TIME");
			payMerchant.payCustStlInfo.lstUptOperId = rs
					.getString("LST_UPT_OPER_ID");
			payMerchant.payCustStlInfo.lstUptTime = rs
					.getTimestamp("LST_UPT_TIME");
			payMerchant.payCustStlInfo.custSetDay = rs
					.getString("CUST_SET_DAY");
			payMerchant.settlementWay = rs.getString("SETTLEMENT_WAY");
			payMerchant.interfaceIp = rs.getString("INTERFACE_IP");
			payMerchant.payWaySupported = rs.getString("PAY_WAY_SUPPORTED");
			payMerchant.chargeWay = rs.getString("CHARGE_WAY");
			payMerchant.preStorageFee = rs.getLong("PRE_STORAGE_FEE");
			payMerchant.parentId = rs.getString("PARENT_ID");
			payMerchant.payCustStlInfo.custSetPeriodAgent = rs
					.getString("CUST_SET_PERIOD_AGENT");
			payMerchant.payCustStlInfo.custSetFreyAgent = rs
					.getString("CUST_SET_FREY_AGENT");
			payMerchant.payCustStlInfo.custStlTimeSetAgent = rs
					.getString("CUST_STL_TIME_SET_AGENT");
			payMerchant.payCustStlInfo.custSetPeriodDaishou = rs
					.getString("CUST_SET_PERIOD_DAISHOU");
			payMerchant.payCustStlInfo.custStlTimeSetDaishou = rs
					.getString("CUST_STL_TIME_SET_DAISHOU");
			payMerchant.feeMap = payFeeRateDAO.getCustFee(
					PayConstant.CUST_TYPE_MERCHANT, payMerchant.custId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payMerchant;
	}

	public List getSettleMentMerchantList() {
		String sql = "select pm.*,pc.*,pf.* from(select * from PAY_MERCHANT where FROZ_STL_SIGN='0')pm "
				+ "left join PAY_CUST_STL_INFO pc on pm.CUST_ID=pc.CUST_ID "
				+ "left join PAY_FEE_RATE pf on pm.SETTLE_FEE_CODE=pf.FEE_CODE";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(getPayMerchantSettleFeeValue(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, con);
		}
		return list;
	}

	public Map<String, PayMerchant> getSettleMentMerchant(String custId)
			throws Exception {
		String sql = "select pm.*,pc.*,pf.* from(select * from PAY_MERCHANT where FROZ_STL_SIGN='0' and "
				+ "(CUST_ID=? or cust_id=(select PARENT_ID from PAY_MERCHANT where cust_id=?)))pm "
				+ "left join PAY_CUST_STL_INFO pc on pm.CUST_ID=pc.CUST_ID "
				+ "left join PAY_FEE_RATE pf on pm.SETTLE_FEE_CODE=pf.FEE_CODE";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, PayMerchant> merMap = new HashMap<String, PayMerchant>();
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, custId);
			ps.setString(2, custId);
			rs = ps.executeQuery();
			while (rs.next()) {
				PayMerchant mer = getPayMerchantSettleFeeValue(rs);
				merMap.put(mer.custId, mer);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
		return merMap;
	}

	/**
	 * 导出excel商户列表
	 * 
	 * @param payMerchant
	 * @param start
	 * @param end
	 * @return
	 */
	public List<PayMerchant> getPayMerchantList(PayMerchant payMerchant,
			long start, long end) throws Exception {
		String sqlCon = setPayMerchantSql(payMerchant);
		String sql = "select * from ("
				+ "  select rownum rowno,tmp1.* from ("
				+ "   SELECT tmp.* ,pro.cash_ac_bal FROM PAY_MERCHANT tmp "
				+ "LEFT JOIN "
				+ "(SELECT pay_ac_no,cash_ac_bal FROM PAY_ACC_PROFILE WHERE AC_TYPE='"
				+ PayConstant.CUST_TYPE_MERCHANT + "') pro "
				+ "ON  tmp.cust_id=pro.pay_ac_no "
				+ (sqlCon.length() == 0 ? "" : " where " + sqlCon) 
				+ "  ) tmp1 " + ")  where rowno > " 
				+ start + " and rowno<= " + end + " order by CUST_ID desc";
		String sql1 = "select PARENT_ID from PAY_MERCHANT where ";
		String tmp = "";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List list = new ArrayList();
		Map<String, PayMerchant> tMap = new HashMap<String, PayMerchant>();
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			setPayMerchantParameter(ps, payMerchant, n);
			rs = ps.executeQuery();
			while (rs.next()) {
				PayMerchant mTmp = getPayMerchantValue(rs);
				tmp = tmp + "PARENT_ID='" + mTmp.custId + "' or ";
				list.add(mTmp);
				tMap.put(mTmp.custId, mTmp);
			}
			if (tmp.length() > 0) {
				tmp = tmp.substring(0, tmp.length() - 4);
				sql1 = sql1 + tmp + " group by PARENT_ID";
				rs.close();
				ps.close();
				log.info(sql1);
				ps = con.prepareStatement(sql1);
				rs = ps.executeQuery();
				while (rs.next()) {
					String custId = rs.getString("PARENT_ID");
					tMap.get(custId).merType = "3";
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
	}

	public long validMerchantId(String parentId) throws Exception {
		String sql = "select count(CUST_ID) pCount from PAY_MERCHANT where CUST_ID = ?";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, parentId);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getLong("pCount") == 0)
					throw new Exception("商户不存在！");
				else {
					rs.close();
					ps.close();
					String sqlFindParent = "select count(cust_id) from PAY_MERCHANT where cust_id=(select parent_id from PAY_MERCHANT where CUST_ID = ?)";
					log.info(sqlFindParent);
					ps = con.prepareStatement(sqlFindParent);
					ps.setString(1, parentId);
					rs = ps.executeQuery();
					if (rs.next() && rs.getLong(1) > 0) {
						throw new Exception("商户已存在上级代理！");
					}
				}
			} else
				throw new Exception("商户不存在！");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
		return 0;
	}

	public List<PayMerchant> getSubMerchantList(String custId) throws Exception {
		List<PayMerchant> list = new ArrayList<PayMerchant>();
		String sql = "select * from PAY_MERCHANT t where t.cust_id !=? and t.parent_id != '0'";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, custId);
			rs = ps.executeQuery();
			while (rs.next())
				list.add(getPayMerchantValue(rs));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
		return list;
	}

	/**
	 * 调账时修改商户预存手续费
	 * 
	 * @param amt
	 * @param custId
	 * @throws Exception
	 */
	public void updatePayMerchantForPreStorageFee(Long amt, String custId)
			throws Exception {
		String sql = "update PAY_MERCHANT_ROOT set PRE_STORAGE_FEE = PRE_STORAGE_FEE + ? where CUST_ID =? ";
		log.info(sql);
		log.info("PRE_STORAGE_FEE=" + amt + ";CUST_ID=" + custId);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setLong(1, amt);
			ps.setString(2, custId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(null, ps, con);
		}
	}

	/**
	 * 实时结算修改商户预存手续费
	 * 
	 * @param amt
	 * @param custId
	 * @throws Exception
	 */
	public int updatePayMerchantForStlPreStorageFee(Long amt, String custId)
			throws Exception {
		String sql = "update PAY_MERCHANT_ROOT set PRE_STORAGE_FEE = PRE_STORAGE_FEE-? where PRE_STORAGE_FEE-?>=0 and CUST_ID =?";
		log.info(sql);
		log.info("PRE_STORAGE_FEE=" + amt + ";CUST_ID=" + custId);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			ps.setLong(n++, amt);
			ps.setLong(n++, amt);
			ps.setString(n++, custId);
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			close(null, ps, con);
		}
	}

	public long validMerchantType(String parentId) {
		String sql = "select count(rownum) childCount from PAY_MERCHANT where PARENT_ID = ? ";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			int n = 1;
			ps.setString(n++, parentId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getLong("childCount");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(rs, ps, con);
		}
		return 0;
	}

	/**
	 * 根据商户号查询商户是否存在(存在返回1，不存在返回0)
	 * 
	 * @param custId
	 * @return
	 */
	public long validByCustId(String custId) throws Exception {
		String sql = "SELECT COUNT(CUST_ID) rCount FROM PAY_MERCHANT_ROOT WHERE CUST_ID = ? "
				+ " AND CUST_ID NOT IN (SELECT DISTINCT MERNO FROM PAY_MERCHANT_CHANNEL_RELATION )";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, custId);
			rs = ps.executeQuery();
			if (rs.next())
				return rs.getLong("rCount");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
		return 0;
	}

	/**
	 * 根据商户号判断商户是否存在
	 * 
	 * @param custId
	 * @return
	 */
	public long validPayYakuStlAccMerno(String custId) throws Exception {
		String sql = " SELECT COUNT(CUST_ID) RCOUNT FROM PAY_MERCHANT_ROOT WHERE CUST_ID = ? ";
		log.info(sql);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = connection();
			ps = con.prepareStatement(sql);
			ps.setString(1, custId);
			rs = ps.executeQuery();
			if (rs.next())
				return rs.getLong("rCount");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			close(rs, ps, con);
		}
		return 0;
	}
	
	/**
	 * 查询商户列表的总金额
	 * @param payMerchant
	 * @return
	 */
    public Long [] getTotalPayMerchantMoney(PayMerchant payMerchant) {
    	Long l0 = 0l,l1 = 0l,l2=0l,l3=0l;
    	String sqlCon = setPayMerchantSql(payMerchant);
        String sql = "  select SUM(tmp1.CASH_AC_BAL) totalCashAcBalMoney,SUM(tmp1.PRE_STORAGE_FEE) totalPreStorageFeeMoney from ("
				+ "   SELECT tmp.* ,pro.cash_ac_bal FROM PAY_MERCHANT tmp "
				+ "LEFT JOIN "
				+ "(SELECT pay_ac_no,cash_ac_bal FROM PAY_ACC_PROFILE WHERE AC_TYPE='"
				+ PayConstant.CUST_TYPE_MERCHANT + "') pro "
				+ "ON  tmp.cust_id=pro.pay_ac_no "
				+ (sqlCon.length() == 0 ? "" : " where " + sqlCon) 
				+ "  ) tmp1" ;
        log.info(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = connection();
            ps = con.prepareStatement(sql);
            int n = 1;
            setPayMerchantParameter(ps,payMerchant,n);
            rs = ps.executeQuery();
            if (rs.next()) {
            	try {l0 = rs.getLong("totalCashAcBalMoney");} catch (Exception e) {}
            	try {l1 = rs.getLong("totalPreStorageFeeMoney");} catch (Exception e) {}
            }
            //取得冻结总额
            rs.close();
            ps.close();
            
            String totalSqlT = setPayMerchantSql(payMerchant);
    		String totalFrozenSql = "select sum(f.CUR_AMT) totalFrozenBal from(select * from PAY_MERCHANT "
    				+ (totalSqlT.length() == 0 ? "" : " where " + totalSqlT)+") m left join PAY_ACC_PROFILE_FROZEN f on m.cust_id=f.acc_no";
    		log.info("冻结总额："+totalFrozenSql);
    		ps = con.prepareStatement(totalFrozenSql);
    		n=1;
    		setPayMerchantParameter(ps, payMerchant, n);
    		rs = ps.executeQuery();
            if (rs.next())try {l2 = rs.getLong("totalFrozenBal");} catch (Exception e) {}
            //取得保证金总额
            rs.close();
            ps.close();
            String totalMarginSql = "select sum(mar.PAID_IN_AMT) totalMarginBal from(select * from PAY_MERCHANT "
    				+ (totalSqlT.length() == 0 ? "" : " where " + totalSqlT)+") m left join PAY_MARGIN mar on m.cust_id=mar.cust_id";
    		log.info("冻结总额："+totalMarginSql);
    		ps = con.prepareStatement(totalMarginSql);
    		n=1;
    		setPayMerchantParameter(ps, payMerchant, n);
    		rs = ps.executeQuery();
            if (rs.next())try {l3 = rs.getLong("totalMarginBal");} catch (Exception e) {}            	
            return new Long[]{l0,l1,l2,l3};
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs, ps, con);
        }
        return new Long[]{0l,0l};
    }
}