package com.pay.merchant.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.PayConstant;
import com.pay.custstl.dao.PayCustStlInfo;
import com.pay.fee.dao.PayFeeRate;
/**
 * Table PAY_MERCHANT entity.
 * 
 * @author administrator
 */
public class PayMerchant {
    public String id;
    public String custId;
    public String parentId;
    public String storeName;
    public String storeShortname;
    public String merType;
    public String bizType;
    public String merLawPerson;
    public String payChannel;
    public String techContact;
    public String techTelNo;
    public String techEmail;
    public String bizContact;
    public String bizTelNo;
    public String bizEmail;
    public String servContact;
    public String servTelNo;
    public String servEmail;
    public String merAddr;
    public String bizRange;
    public String merOpnBankCode;
    public String bankStlAcNo;
    public String rfCommFlg;
    public String rfRecFeeFlg;
    public String city;
    public String postCode;
    public String lawPersonType;
    public String lawPersonCretType;
    public String lawPersonCretNo;
    public String longTermSign;
    public Date certBeginDate;
    public Date certEndDate;
    public String webAddress;
    public String webAddressAbbr;
    public Date contractStrDate;
    public Date contractEndDate;
    public String contractor;
    public String contractBizNo;
    public String contractBizName;
    public String techMobNo;
    public String bizMobNo;
    public String servMobNo;
    public Long margin;
    public String frozStlSign = "0";
    public Long cretMasterityDays;
    public String taxRegistrationNo;
    public String organizationNo;
    public String cpsSign;
    public Long regCapital;
    public String compayEmail;
    public String compayTelNo;
    public String compayFax;
    public Long cycleDays;
    public Long agentGoalDiscountRate;
    public Long agentPayRate;
    public Long agentTaxRate;
    public String codeTypeId;
    public String cert;
    public String companyBrief;
    public String riskLevel;
    public String bussinessType;
    public String merStatus;
    public Date createTime;
    public String createUser;
    public String businessLicencePic;
    public String taxRegistrationPic;
    public String organizationPic;
    public String openingLicensesPic;
    public String certPicFront;
    public String certPicBack;
    public String contractPic;
    public String icpNo;
    public String province;
    public String checkUser;
    public Date checkTime;
    public String checkStatus;
    public String checkInfo;
    public String riskDesc;
    public String region;
    public String attentionLine;
    public String attentionLineTel;
    public String attentionLineEmail;
    public String businessLicenceNo;
    public Date businessLicenceBeginDate;
    public Date businessLicenceEndDate;
    public String icpCertNo;
    public String merchantLogoPic;
    public String bankStlAcNoType;
    public String issuer;
    public String merchantOperatorId;
    // 商户搜索时间段结束时间点
    public Date createEndTime;
    // 商户结算信息
    public String custBankDepositName;
    public String depositBankBrchName;
    public String depositBankCode;
    public String custStlType;
    public String custStlBankAcNo;
    public String custSetPeriod;
    public String custSetFrey;
    public String custSetFreyAgent;
    public String custStlTimeSet;
    public String custSetPeriodAgent;
    public String custStlTimeSetAgent;
    public String custSetPeriodDaishou;
    public String custStlTimeSetDaishou;
    public String settlementWay;
    public PayCustStlInfo payCustStlInfo;
    public String interfaceIp;
    public String payWaySupported;
    public String chargeWay;
    public Long preStorageFee;
    public Long cashAcBal=0l;
    public Long frozenBal=0l;
    public Long marginBal=0l;
    public String userInterflowFlag;
    public String userId;
    
	//key:custType+","+custId+","+tranType；value:PayFeeRate
    public Map<String,PayFeeRate> feeMap = new HashMap<String,PayFeeRate>();
    public Long minStlBalance;
    public List <PayMerchant>subMerList = new ArrayList();
    public Map <String,PayMerchant>subMerMap = new HashMap();
    public PayAccProfile accProfile = null;
    
    public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserInterflowFlag() {
		return userInterflowFlag;
	}
	public void setUserInterflowFlag(String userInterflowFlag) {
		this.userInterflowFlag = userInterflowFlag;
	}
	public Long getAgentGoalDiscountRate() {
		return agentGoalDiscountRate;
	}
	public void setAgentGoalDiscountRate(Long agentGoalDiscountRate) {
		this.agentGoalDiscountRate = agentGoalDiscountRate;
	}
	
	public Long getAgentPayRate() {
		return agentPayRate;
	}
	public void setAgentPayRate(Long agentPayRate) {
		this.agentPayRate = agentPayRate;
	}
	
	public Long getAgentTaxRate() {
		return agentTaxRate;
	}
	public void setAgentTaxRate(Long agentTaxRate) {
		this.agentTaxRate = agentTaxRate;
	}
	public String getCustSetFreyAgent() {
		return custSetFreyAgent;
	}
	public void setCustSetFreyAgent(String custSetFreyAgent) {
		this.custSetFreyAgent = custSetFreyAgent;
	}
	public String getCustSetPeriodAgent() {
		return custSetPeriodAgent;
	}
	public void setCustSetPeriodAgent(String custSetPeriodAgent) {
		this.custSetPeriodAgent = custSetPeriodAgent;
	}
	public String getCustStlTimeSetAgent() {
		return custStlTimeSetAgent;
	}
	public void setCustStlTimeSetAgent(String custStlTimeSetAgent) {
		this.custStlTimeSetAgent = custStlTimeSetAgent;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Long getMinStlBalance() {
		return minStlBalance;
	}
	public void setMinStlBalance(Long minStlBalance) {
		this.minStlBalance = minStlBalance;
	}
	
    public Date getCreateEndTime() {
		return createEndTime;
	}
	public String getCustBankDepositName() {
		return custBankDepositName;
	}
	public void setCustBankDepositName(String custBankDepositName) {
		this.custBankDepositName = custBankDepositName;
	}
	public String getDepositBankBrchName() {
		return depositBankBrchName;
	}
	public void setDepositBankBrchName(String depositBankBrchName) {
		this.depositBankBrchName = depositBankBrchName;
	}
	public String getDepositBankCode() {
		return depositBankCode;
	}
	public void setDepositBankCode(String depositBankCode) {
		this.depositBankCode = depositBankCode;
	}
	public String getCustStlType() {
		return custStlType;
	}
	public void setCustStlType(String custStlType) {
		this.custStlType = custStlType;
	}
	public String getCustStlBankAcNo() {
		return custStlBankAcNo;
	}
	public void setCustStlBankAcNo(String custStlBankAcNo) {
		this.custStlBankAcNo = custStlBankAcNo;
	}
	public String getCustSetPeriod() {
		return custSetPeriod;
	}
	public void setCustSetPeriod(String custSetPeriod) {
		this.custSetPeriod = custSetPeriod;
	}
	public String getCustSetFrey() {
		return custSetFrey;
	}
	public void setCustSetFrey(String custSetFrey) {
		this.custSetFrey = custSetFrey;
	}
	public String getCustStlTimeSet() {
		return custStlTimeSet;
	}
	public void setCustStlTimeSet(String custStlTimeSet) {
		this.custStlTimeSet = custStlTimeSet;
	}
	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}
    
    public Long getCashAcBal() {
		return cashAcBal;
	}
	public void setCashAcBal(Long cashAcBal) {
		this.cashAcBal = cashAcBal;
	}
	
	public Long getFrozenBal() {
		return frozenBal;
	}
	public void setFrozenBal(Long frozenBal) {
		this.frozenBal = frozenBal;
	}
	public Long getMarginBal() {
		return marginBal;
	}
	public void setMarginBal(Long marginBal) {
		this.marginBal = marginBal;
	}
	public String getId(){
         return id;
    }
    public void setId(String id){
         this.id=id;
    }
    public String getCustId(){
         return custId;
    }
    public void setCustId(String custId){
         this.custId=custId;
    }
    public String getStoreName(){
         return storeName;
    }
    public void setStoreName(String storeName){
         this.storeName=storeName;
    }
    public String getStoreShortname(){
         return storeShortname;
    }
    public void setStoreShortname(String storeShortname){
         this.storeShortname=storeShortname;
    }
    public String getMerType(){
         return merType;
    }
    public void setMerType(String merType){
         this.merType=merType;
    }
    public String getBizType(){
         return bizType;
    }
    public void setBizType(String bizType){
         this.bizType=bizType;
    }
    public String getMerLawPerson(){
         return merLawPerson;
    }
    public void setMerLawPerson(String merLawPerson){
         this.merLawPerson=merLawPerson;
    }
    public String getPayChannel(){
         return payChannel;
    }
    public void setPayChannel(String payChannel){
         this.payChannel=payChannel;
    }
    public String getTechContact(){
         return techContact;
    }
    public void setTechContact(String techContact){
         this.techContact=techContact;
    }
    public String getTechTelNo(){
         return techTelNo;
    }
    public void setTechTelNo(String techTelNo){
         this.techTelNo=techTelNo;
    }
    public String getTechEmail(){
         return techEmail;
    }
    public void setTechEmail(String techEmail){
         this.techEmail=techEmail;
    }
    public String getBizContact(){
         return bizContact;
    }
    public void setBizContact(String bizContact){
         this.bizContact=bizContact;
    }
    public String getBizTelNo(){
         return bizTelNo;
    }
    public void setBizTelNo(String bizTelNo){
         this.bizTelNo=bizTelNo;
    }
    public String getBizEmail(){
         return bizEmail;
    }
    public void setBizEmail(String bizEmail){
         this.bizEmail=bizEmail;
    }
    public String getServContact(){
         return servContact;
    }
    public void setServContact(String servContact){
         this.servContact=servContact;
    }
    public String getServTelNo(){
         return servTelNo;
    }
    public void setServTelNo(String servTelNo){
         this.servTelNo=servTelNo;
    }
    public String getServEmail(){
         return servEmail;
    }
    public void setServEmail(String servEmail){
         this.servEmail=servEmail;
    }
    public String getMerAddr(){
         return merAddr;
    }
    public void setMerAddr(String merAddr){
         this.merAddr=merAddr;
    }
    public String getBizRange(){
         return bizRange;
    }
    public void setBizRange(String bizRange){
         this.bizRange=bizRange;
    }
    public String getMerOpnBankCode(){
         return merOpnBankCode;
    }
    public void setMerOpnBankCode(String merOpnBankCode){
         this.merOpnBankCode=merOpnBankCode;
    }
    public String getBankStlAcNo(){
         return bankStlAcNo;
    }
    public void setBankStlAcNo(String bankStlAcNo){
         this.bankStlAcNo=bankStlAcNo;
    }
    public String getRfCommFlg(){
         return rfCommFlg;
    }
    public void setRfCommFlg(String rfCommFlg){
         this.rfCommFlg=rfCommFlg;
    }
    public String getRfRecFeeFlg(){
         return rfRecFeeFlg;
    }
    public void setRfRecFeeFlg(String rfRecFeeFlg){
         this.rfRecFeeFlg=rfRecFeeFlg;
    }
    public String getCity(){
         return city;
    }
    public void setCity(String city){
         this.city=city;
    }
    public String getPostCode(){
         return postCode;
    }
    public void setPostCode(String postCode){
         this.postCode=postCode;
    }
    public String getLawPersonType(){
         return lawPersonType;
    }
    public void setLawPersonType(String lawPersonType){
         this.lawPersonType=lawPersonType;
    }
    public String getLawPersonCretType(){
         return lawPersonCretType;
    }
    public void setLawPersonCretType(String lawPersonCretType){
         this.lawPersonCretType=lawPersonCretType;
    }
    public String getLawPersonCretNo(){
         return lawPersonCretNo;
    }
    public void setLawPersonCretNo(String lawPersonCretNo){
         this.lawPersonCretNo=lawPersonCretNo;
    }
    public String getLongTermSign(){
         return longTermSign;
    }
    public void setLongTermSign(String longTermSign){
         this.longTermSign=longTermSign;
    }
    public Date getCertBeginDate(){
         return certBeginDate;
    }
    public void setCertBeginDate(Date certBeginDate){
         this.certBeginDate=certBeginDate;
    }
    public Date getCertEndDate(){
         return certEndDate;
    }
    public void setCertEndDate(Date certEndDate){
         this.certEndDate=certEndDate;
    }
    public String getWebAddress(){
         return webAddress;
    }
    public void setWebAddress(String webAddress){
         this.webAddress=webAddress;
    }
    public String getWebAddressAbbr(){
         return webAddressAbbr;
    }
    public void setWebAddressAbbr(String webAddressAbbr){
         this.webAddressAbbr=webAddressAbbr;
    }
    public Date getContractStrDate(){
         return contractStrDate;
    }
    public void setContractStrDate(Date contractStrDate){
         this.contractStrDate=contractStrDate;
    }
    public Date getContractEndDate(){
         return contractEndDate;
    }
    public void setContractEndDate(Date contractEndDate){
         this.contractEndDate=contractEndDate;
    }
    public String getContractor(){
         return contractor;
    }
    public void setContractor(String contractor){
         this.contractor=contractor;
    }
    public String getContractBizNo(){
         return contractBizNo;
    }
    public void setContractBizNo(String contractBizNo){
         this.contractBizNo=contractBizNo;
    }
    public String getContractBizName(){
         return contractBizName;
    }
    public void setContractBizName(String contractBizName){
         this.contractBizName=contractBizName;
    }
    public String getTechMobNo(){
         return techMobNo;
    }
    public void setTechMobNo(String techMobNo){
         this.techMobNo=techMobNo;
    }
    public String getBizMobNo(){
         return bizMobNo;
    }
    public void setBizMobNo(String bizMobNo){
         this.bizMobNo=bizMobNo;
    }
    public String getServMobNo(){
         return servMobNo;
    }
    public void setServMobNo(String servMobNo){
         this.servMobNo=servMobNo;
    }
    public long getMargin(){
         return margin;
    }
    public void setMargin(long margin){
         this.margin=margin;
    }
    public String getFrozStlSign(){
         return frozStlSign;
    }
    public void setFrozStlSign(String frozStlSign){
         this.frozStlSign=frozStlSign;
    }
    public long getCretMasterityDays(){
         return cretMasterityDays;
    }
    public void setCretMasterityDays(long cretMasterityDays){
         this.cretMasterityDays=cretMasterityDays;
    }
    public String getTaxRegistrationNo(){
         return taxRegistrationNo;
    }
    public void setTaxRegistrationNo(String taxRegistrationNo){
         this.taxRegistrationNo=taxRegistrationNo;
    }
    public String getOrganizationNo(){
         return organizationNo;
    }
    public void setOrganizationNo(String organizationNo){
         this.organizationNo=organizationNo;
    }
    public String getCpsSign(){
         return cpsSign;
    }
    public void setCpsSign(String cpsSign){
         this.cpsSign=cpsSign;
    }
    public long getRegCapital(){
         return regCapital;
    }
    public void setRegCapital(long regCapital){
         this.regCapital=regCapital;
    }
    public String getCompayEmail(){
         return compayEmail;
    }
    public void setCompayEmail(String compayEmail){
         this.compayEmail=compayEmail;
    }
    public String getCompayTelNo(){
         return compayTelNo;
    }
    public void setCompayTelNo(String compayTelNo){
         this.compayTelNo=compayTelNo;
    }
    public String getCompayFax(){
         return compayFax;
    }
    public void setCompayFax(String compayFax){
         this.compayFax=compayFax;
    }
    public long getCycleDays(){
         return cycleDays;
    }
    public void setCycleDays(long cycleDays){
         this.cycleDays=cycleDays;
    }
    public String getCodeTypeId(){
         return codeTypeId;
    }
    public void setCodeTypeId(String codeTypeId){
         this.codeTypeId=codeTypeId;
    }
    public String getCert(){
         return cert;
    }
    public void setCert(String cert){
         this.cert=cert;
    }
    public String getCompanyBrief(){
         return companyBrief;
    }
    public void setCompanyBrief(String companyBrief){
         this.companyBrief=companyBrief;
    }
    public String getRiskLevel(){
         return riskLevel;
    }
    public void setRiskLevel(String riskLevel){
         this.riskLevel=riskLevel;
    }
    public String getBussinessType(){
         return bussinessType;
    }
    public void setBussinessType(String bussinessType){
         this.bussinessType=bussinessType;
    }
    public String getMerStatus(){
         return merStatus;
    }
    public void setMerStatus(String merStatus){
         this.merStatus=merStatus;
    }
    public Date getCreateTime(){
         return createTime;
    }
    public void setCreateTime(Date createTime){
         this.createTime=createTime;
    }
    public String getCreateUser(){
         return createUser;
    }
    public void setCreateUser(String createUser){
         this.createUser=createUser;
    }
    public String getBusinessLicencePic(){
         return businessLicencePic;
    }
    public void setBusinessLicencePic(String businessLicencePic){
         this.businessLicencePic=businessLicencePic;
    }
    public String getTaxRegistrationPic(){
         return taxRegistrationPic;
    }
    public void setTaxRegistrationPic(String taxRegistrationPic){
         this.taxRegistrationPic=taxRegistrationPic;
    }
    public String getOrganizationPic(){
         return organizationPic;
    }
    public void setOrganizationPic(String organizationPic){
         this.organizationPic=organizationPic;
    }
    public String getOpeningLicensesPic(){
         return openingLicensesPic;
    }
    public void setOpeningLicensesPic(String openingLicensesPic){
         this.openingLicensesPic=openingLicensesPic;
    }
    public String getCertPicFront(){
         return certPicFront;
    }
    public void setCertPicFront(String certPicFront){
         this.certPicFront=certPicFront;
    }
    public String getCertPicBack(){
         return certPicBack;
    }
    public void setCertPicBack(String certPicBack){
         this.certPicBack=certPicBack;
    }
    public String getContractPic(){
         return contractPic;
    }
    public void setContractPic(String contractPic){
         this.contractPic=contractPic;
    }
    public String getIcpNo(){
         return icpNo;
    }
    public void setIcpNo(String icpNo){
         this.icpNo=icpNo;
    }
//    public String getSettleFeeCode(){
//         return settleFeeCode;
//    }
//    public void setSettleFeeCode(String settleFeeCode){
//         this.settleFeeCode=settleFeeCode;
//    }
//    public String getWithdrawFeeCode(){
//         return withdrawFeeCode;
//    }
//    public void setWithdrawFeeCode(String withdrawFeeCode){
//         this.withdrawFeeCode=withdrawFeeCode;
//    }
//    public String getUrgentFeeCode(){
//         return urgentFeeCode;
//    }
//    public void setUrgentFeeCode(String urgentFeeCode){
//         this.urgentFeeCode=urgentFeeCode;
//    }
    public String getProvince(){
         return province;
    }
    public void setProvince(String province){
         this.province=province;
    }
    public String getCheckUser(){
         return checkUser;
    }
    public void setCheckUser(String checkUser){
         this.checkUser=checkUser;
    }
    public Date getCheckTime(){
         return checkTime;
    }
    public void setCheckTime(Date checkTime){
         this.checkTime=checkTime;
    }
    public String getCheckStatus(){
         return checkStatus;
    }
    public void setCheckStatus(String checkStatus){
         this.checkStatus=checkStatus;
    }
    public String getCheckInfo(){
         return checkInfo;
    }
    public void setCheckInfo(String checkInfo){
         this.checkInfo=checkInfo;
    }
    public String getRiskDesc(){
         return riskDesc;
    }
    public void setRiskDesc(String riskDesc){
         this.riskDesc=riskDesc;
    }
    public String getRegion(){
         return region;
    }
    public void setRegion(String region){
         this.region=region;
    }
    public String getAttentionLine(){
        return attentionLine;
   }
   public void setAttentionLine(String attentionLine){
        this.attentionLine=attentionLine;
   }
   public String getAttentionLineTel(){
        return attentionLineTel;
   }
   public void setAttentionLineTel(String attentionLineTel){
        this.attentionLineTel=attentionLineTel;
   }
   public String getAttentionLineEmail(){
        return attentionLineEmail;
   }
   public void setAttentionLineEmail(String attentionLineEmail){
        this.attentionLineEmail=attentionLineEmail;
   }
   public String getBusinessLicenceNo(){
        return businessLicenceNo;
   }
   public void setBusinessLicenceNo(String businessLicenceNo){
        this.businessLicenceNo=businessLicenceNo;
   }
   public Date getBusinessLicenceBeginDate(){
        return businessLicenceBeginDate;
   }
   public void setBusinessLicenceBeginDate(Date businessLicenceBeginDate){
        this.businessLicenceBeginDate=businessLicenceBeginDate;
   }
   public Date getBusinessLicenceEndDate(){
        return businessLicenceEndDate;
   }
   public void setBusinessLicenceEndDate(Date businessLicenceEndDate){
        this.businessLicenceEndDate=businessLicenceEndDate;
   }
   public String getIcpCertNo(){
        return icpCertNo;
   }
   public void setIcpCertNo(String icpCertNo){
        this.icpCertNo=icpCertNo;
    }
   
    public void setMargin(Long margin) {
		this.margin = margin;
	}
	public void setCretMasterityDays(Long cretMasterityDays) {
		this.cretMasterityDays = cretMasterityDays;
	}
	public void setRegCapital(Long regCapital) {
		this.regCapital = regCapital;
	}
	public void setCycleDays(Long cycleDays) {
		this.cycleDays = cycleDays;
	}
	
	public String getMerchantOperatorId() {
		return merchantOperatorId;
	}
	public void setMerchantOperatorId(String merchantOperatorId) {
		this.merchantOperatorId = merchantOperatorId;
	}
	public String getMerchantLogoPic(){
        return merchantLogoPic;
	}
	public void setMerchantLogoPic(String merchantLogoPic){
        this.merchantLogoPic=merchantLogoPic;
   	}
   	public String getBankStlAcNoType(){
        return bankStlAcNoType;
   	}
  	public void setBankStlAcNoType(String bankStlAcNoType){
        this.bankStlAcNoType=bankStlAcNoType;
   	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	public String getSettlementWay() {
		return settlementWay;
	}
	public void setSettlementWay(String settlementWay) {
		this.settlementWay = settlementWay;
	}
	
	public String getInterfaceIp() {
		return interfaceIp;
	}
	public void setInterfaceIp(String interfaceIp) {
		this.interfaceIp = interfaceIp;
	}
	
	public String getPayWaySupported() {
		return payWaySupported;
	}
	public void setPayWaySupported(String payWaySupported) {
		this.payWaySupported = payWaySupported;
	}
	
	public String getChargeWay() {
		return chargeWay;
	}
	public void setChargeWay(String chargeWay) {
		this.chargeWay = chargeWay;
	}
	
	public Long getPreStorageFee() {
		return preStorageFee;
	}
	public void setPreStorageFee(Long preStorageFee) {
		this.preStorageFee = preStorageFee;
	}
	public String toString(){
        String temp = "";
        temp = temp + "id="+id+"\n";
        temp = temp + "custId="+custId+"\n";
        temp = temp + "storeName="+storeName+"\n";
        temp = temp + "storeShortname="+storeShortname+"\n";
        temp = temp + "merType="+merType+"\n";
        temp = temp + "bizType="+bizType+"\n";
        temp = temp + "merLawPerson="+merLawPerson+"\n";
        temp = temp + "payChannel="+payChannel+"\n";
        temp = temp + "techContact="+techContact+"\n";
        temp = temp + "techTelNo="+techTelNo+"\n";
        temp = temp + "techEmail="+techEmail+"\n";
        temp = temp + "bizContact="+bizContact+"\n";
        temp = temp + "bizTelNo="+bizTelNo+"\n";
        temp = temp + "bizEmail="+bizEmail+"\n";
        temp = temp + "servContact="+servContact+"\n";
        temp = temp + "servTelNo="+servTelNo+"\n";
        temp = temp + "servEmail="+servEmail+"\n";
        temp = temp + "merAddr="+merAddr+"\n";
        temp = temp + "bizRange="+bizRange+"\n";
        temp = temp + "merOpnBankCode="+merOpnBankCode+"\n";
        temp = temp + "bankStlAcNo="+bankStlAcNo+"\n";
        temp = temp + "rfCommFlg="+rfCommFlg+"\n";
        temp = temp + "rfRecFeeFlg="+rfRecFeeFlg+"\n";
        temp = temp + "city="+city+"\n";
        temp = temp + "postCode="+postCode+"\n";
        temp = temp + "lawPersonType="+lawPersonType+"\n";
        temp = temp + "lawPersonCretType="+lawPersonCretType+"\n";
        temp = temp + "lawPersonCretNo="+lawPersonCretNo+"\n";
        temp = temp + "longTermSign="+longTermSign+"\n";
        temp = temp + "certBeginDate="+certBeginDate+"\n";
        temp = temp + "certEndDate="+certEndDate+"\n";
        temp = temp + "webAddress="+webAddress+"\n";
        temp = temp + "webAddressAbbr="+webAddressAbbr+"\n";
        temp = temp + "contractStrDate="+contractStrDate+"\n";
        temp = temp + "contractEndDate="+contractEndDate+"\n";
        temp = temp + "contractor="+contractor+"\n";
        temp = temp + "contractBizNo="+contractBizNo+"\n";
        temp = temp + "contractBizName="+contractBizName+"\n";
        temp = temp + "techMobNo="+techMobNo+"\n";
        temp = temp + "bizMobNo="+bizMobNo+"\n";
        temp = temp + "servMobNo="+servMobNo+"\n";
        temp = temp + "margin="+margin+"\n";
        temp = temp + "frozStlSign="+frozStlSign+"\n";
        temp = temp + "cretMasterityDays="+cretMasterityDays+"\n";
        temp = temp + "taxRegistrationNo="+taxRegistrationNo+"\n";
        temp = temp + "organizationNo="+organizationNo+"\n";
        temp = temp + "cpsSign="+cpsSign+"\n";
        temp = temp + "regCapital="+regCapital+"\n";
        temp = temp + "compayEmail="+compayEmail+"\n";
        temp = temp + "compayTelNo="+compayTelNo+"\n";
        temp = temp + "compayFax="+compayFax+"\n";
        temp = temp + "cycleDays="+cycleDays+"\n";
        temp = temp + "codeTypeId="+codeTypeId+"\n";
        temp = temp + "cert="+cert+"\n";
        temp = temp + "companyBrief="+companyBrief+"\n";
        temp = temp + "riskLevel="+riskLevel+"\n";
        temp = temp + "bussinessType="+bussinessType+"\n";
        temp = temp + "merStatus="+merStatus+"\n";
        temp = temp + "createTime="+createTime+"\n";
        temp = temp + "createEndTime="+createEndTime+"\n";
        temp = temp + "createUser="+createUser+"\n";
        temp = temp + "businessLicencePic="+businessLicencePic+"\n";
        temp = temp + "taxRegistrationPic="+taxRegistrationPic+"\n";
        temp = temp + "organizationPic="+organizationPic+"\n";
        temp = temp + "openingLicensesPic="+openingLicensesPic+"\n";
        temp = temp + "certPicFront="+certPicFront+"\n";
        temp = temp + "certPicBack="+certPicBack+"\n";
        temp = temp + "contractPic="+contractPic+"\n";
        temp = temp + "icpNo="+icpNo+"\n";
        temp = temp + "merchantLogoPic="+merchantLogoPic+"\n";
        temp = temp + "bankStlAcNoType="+bankStlAcNoType+"\n";
//        temp = temp + "settleFeeCode="+settleFeeCode+"\n";
//        temp = temp + "withdrawFeeCode="+withdrawFeeCode+"\n";
//        temp = temp + "urgentFeeCode="+urgentFeeCode+"\n";
        temp = temp + "province="+province+"\n";
        temp = temp + "checkUser="+checkUser+"\n";
        temp = temp + "checkTime="+checkTime+"\n";
        temp = temp + "checkStatus="+checkStatus+"\n";
        temp = temp + "checkInfo="+checkInfo+"\n";
        temp = temp + "riskDesc="+riskDesc+"\n";
        temp = temp + "region="+region+"\n";
        temp = temp + "custBankDepositName="+custBankDepositName+"\n";
        temp = temp + "depositBankBrchName="+depositBankBrchName+"\n";
        temp = temp + "depositBankCode="+depositBankCode+"\n";
        temp = temp + "custStlType="+custStlType+"\n";
        temp = temp + "custStlBankAcNo="+custStlBankAcNo+"\n";
        temp = temp + "custSetPeriod="+custSetPeriod+"\n";
        temp = temp + "custSetFrey="+custSetFrey+"\n";
        temp = temp + "custStlTimeSet="+custStlTimeSet+"\n";
//        temp = temp + "settleFeeName="+settleFeeName+"\n";
//        temp = temp + "withdrawFeeName="+settleFeeName+"\n";
//        temp = temp + "urgentFeeName="+settleFeeName+"\n";
        temp = temp + "attentionLine="+attentionLine+"\n";
        temp = temp + "attentionLineTel="+attentionLineTel+"\n";
        temp = temp + "attentionLineEmail="+attentionLineEmail+"\n";
        temp = temp + "businessLicenceNo="+businessLicenceNo+"\n";
        temp = temp + "businessLicenceBeginDate="+businessLicenceBeginDate+"\n";
        temp = temp + "businessLicenceEndDate="+businessLicenceEndDate+"\n";
        temp = temp + "icpCertNo="+icpCertNo+"\n";
        temp = temp + "interfaceIp="+interfaceIp+"\n";
        temp = temp + "payWaySupported="+payWaySupported+"\n";
        temp = temp + "parentId="+parentId+"\n";
        temp = temp + "custSetPeriodAgent="+custSetPeriodAgent+"\n";
        temp = temp + "custStlTimeSetAgent="+custStlTimeSetAgent+"\n";
        temp = temp + "custSetFreyAgent="+custSetFreyAgent+"\n";
        temp = temp + "agentGoalDiscountRate="+agentGoalDiscountRate+"\n";
        temp = temp + "agentPayRate="+agentPayRate+"\n";
        temp = temp + "agentTaxRate="+agentTaxRate+"\n";
        return temp;
    }
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("custId",custId);
        json.put("storeName",storeName);
        json.put("storeShortname",storeShortname);
        json.put("merType",merType);
        json.put("bizType",bizType);
        json.put("merLawPerson",merLawPerson);
        json.put("payChannel",payChannel);
        json.put("techContact",techContact);
        json.put("techTelNo",techTelNo);
        json.put("techEmail",techEmail);
        json.put("bizContact",bizContact);
        json.put("bizTelNo",bizTelNo);
        json.put("bizEmail",bizEmail);
        json.put("servContact",servContact);
        json.put("servTelNo",servTelNo);
        json.put("servEmail",servEmail);
        json.put("merAddr",merAddr);
        json.put("bizRange",bizRange);
        json.put("merOpnBankCode",merOpnBankCode);
        json.put("bankStlAcNo",bankStlAcNo);
        json.put("rfCommFlg",rfCommFlg);
        json.put("rfRecFeeFlg",rfRecFeeFlg);
        json.put("city",city);
        json.put("postCode",postCode);
        json.put("lawPersonType",lawPersonType);
        json.put("lawPersonCretType",lawPersonCretType);
        json.put("lawPersonCretNo",lawPersonCretNo);
        json.put("longTermSign",longTermSign);
        try{
            json.put("certBeginDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(certBeginDate));
        } catch (Exception e) {}
        try{
            json.put("certEndDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(certEndDate));
        } catch (Exception e) {}
        json.put("webAddress",webAddress);
        json.put("webAddressAbbr",webAddressAbbr);
        try{
            json.put("contractStrDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(contractStrDate));
        } catch (Exception e) {}
        try{
            json.put("contractEndDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(contractEndDate));
        } catch (Exception e) {}
        json.put("contractor",contractor);
        json.put("contractBizNo",contractBizNo);
        json.put("contractBizName",contractBizName);
        json.put("techMobNo",techMobNo);
        json.put("bizMobNo",bizMobNo);
        json.put("servMobNo",servMobNo);
        json.put("margin",String.valueOf(margin));
        json.put("frozStlSign",frozStlSign);
        json.put("cretMasterityDays",String.valueOf(cretMasterityDays));
        json.put("taxRegistrationNo",taxRegistrationNo);
        json.put("organizationNo",organizationNo);
        json.put("cpsSign",cpsSign);
        json.put("regCapital",String.valueOf(regCapital));
        json.put("compayEmail",compayEmail);
        json.put("compayTelNo",compayTelNo);
        json.put("compayFax",compayFax);
        json.put("cycleDays",String.valueOf(cycleDays));
        json.put("codeTypeId",codeTypeId);
        json.put("cert",cert);
        json.put("companyBrief",companyBrief);
        json.put("riskLevel",riskLevel);
        json.put("bussinessType",bussinessType);
        json.put("merStatus",merStatus);
        try{
            json.put("createTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime));
            json.put("createEndTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createEndTime));
        } catch (Exception e) {}
        json.put("createUser",createUser);
        json.put("businessLicencePic",businessLicencePic);
        json.put("taxRegistrationPic",taxRegistrationPic);
        json.put("organizationPic",organizationPic);
        json.put("openingLicensesPic",openingLicensesPic);
        json.put("certPicFront",certPicFront);
        json.put("certPicBack",certPicBack);
        json.put("contractPic",contractPic);
        json.put("icpNo",icpNo);
        json.put("settleFeeCode",feeMap.get("2,"+custId+",3")!=null?feeMap.get("2,"+custId+",3").feeCode:"");
        json.put("withdrawFeeCode",feeMap.get("2,"+custId+",5")!=null?feeMap.get("2,"+custId+",5").feeCode:"");
        json.put("urgentFeeCode","");
        json.put("province",province);
        json.put("checkUser",checkUser);
        try{
            json.put("checkTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(checkTime));
        } catch (Exception e) {}
        json.put("checkStatus",checkStatus);
        json.put("checkInfo",checkInfo);
        json.put("riskDesc",riskDesc);
        json.put("region",region);
        json.put("custBankDepositName",custBankDepositName);
        json.put("depositBankBrchName",depositBankBrchName);
        json.put("depositBankCode",depositBankCode);
        json.put("custStlType",custStlType);
        json.put("custStlBankAcNo",custStlBankAcNo);
        json.put("custSetPeriod",custSetPeriod);
        json.put("custSetFrey",custSetFrey);
        json.put("custStlTimeSet",custStlTimeSet);
        json.put("settleFeeName",feeMap.get("2,"+custId+",3")!=null?feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+custId+",3").feeName:"");
        json.put("withdrawFeeName",feeMap.get("2,"+custId+",5")!=null?feeMap.get(PayConstant.CUST_TYPE_MERCHANT+","+custId+",5").feeName:"");
        json.put("urgentFeeName","");
        json.put("attentionLine",attentionLine);
        json.put("attentionLineTel",attentionLineTel);
        json.put("attentionLineEmail",attentionLineEmail);
        json.put("businessLicenceNo",businessLicenceNo);
        json.put("cashAcBal",String.format("%.2f",(double)cashAcBal*0.01));
        json.put("frozenBal",String.format("%.2f",(double)frozenBal*0.01));
        json.put("marginBal",String.format("%.2f",(double)marginBal*0.01));
        try{
            json.put("businessLicenceBeginDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(businessLicenceBeginDate));
        } catch (Exception e) {}
        try{
            json.put("businessLicenceEndDate", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(businessLicenceEndDate));
        } catch (Exception e) {}
        json.put("icpCertNo",icpCertNo);
        json.put("merchantLogoPic",merchantLogoPic);
        json.put("bankStlAcNoType",bankStlAcNoType);
        json.put("settlementWay",settlementWay);
        json.put("issuer",issuer);
        json.put("interfaceIp",interfaceIp);
        json.put("payWaySupported",payWaySupported);
        json.put("chargeWay",chargeWay);
        json.put("parentId",parentId);
        json.put("agentGoalDiscountRate",agentGoalDiscountRate);
        json.put("agentPayRate",agentPayRate);
        json.put("agentTaxRate",agentTaxRate);
        json.put("preStorageFee",String.format("%.2f", ((double)preStorageFee)/100d));
        return json;
    }
    public JSONObject toJson1(String rootCustId) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id",custId);
        if(!rootCustId.equals(custId))json.put("_parentId",parentId);
		json.put("name",storeName);
		json.put("iconCls","icon-man");
		json.put("text",storeName);
		json.put("merStatus",merStatus);
		json.put("checkStatus",checkStatus);
		json.put("userInterflowFlag",userInterflowFlag);
        return json;
    }
}