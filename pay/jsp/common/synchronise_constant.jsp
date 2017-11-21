<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@page import="util.SHA1"%>
<%@ page import="com.PayConstant"%>
<%@ page import="com.pay.cardbin.service.PayCardBinService"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.fee.service.PayFeeRateService"%>
<%@ page import="com.pay.risk.service.PayRiskExceptRuleService"%>
<%@ page import="com.pay.risk.service.PayRiskMerchantLimitService"%>
<%@ page import="com.pay.risk.service.PayRiskUserLimitService"%>
<%@ page import="com.pay.business.service.PayBusinessParameterService"%>
<%
//验签处理
String type = request.getParameter("type");
String timestamp = request.getParameter("timestamp");
String sign = request.getParameter("sign");
if(SHA1.SHA1String(timestamp+PayConstant.PAY_CONFIG.get("SYS_COMM_PWD_WITH_INNER_PLT")).equals(sign)){
	try {
		if("0".equals(type)) {
			PayCardBinService.init();
		} else if("1".equals(type)) {
			PayCoopBankService.loadCoopBank();
		} else if("2".equals(type)) {
			PayCoopBankService.loadSupportedBank();
		} else if("3".equals(type)) {
			PayCoopBankService.loadRouteRule();
		} else if("4".equals(type)) {
			PayCoopBankService.loadMerchantChannelRelation();
		} else if("5".equals(type)) {
			PayFeeRateService.initZeroFeeRate();
		} else if("6".equals(type)) {
			PayFeeRateService.loadFee();
		} else if("7".equals(type)) {
			PayFeeRateService.loadChannelFeeRate();
		} else if("8".equals(type)) {
			PayRiskExceptRuleService.loadRiskRuleList();
		} else if("9".equals(type)) {
			PayRiskMerchantLimitService.loadMerchantLimit();
		} else if("10".equals(type)) {
			PayRiskUserLimitService.loadUserLimit();
		} else if("11".equals(type)) {
			PayBusinessParameterService.executePayBusinessParameter("1");
		} else {}
		out.println("0");
	} catch (Exception e){out.println(e.getMessage());}
} else out.println("sign fail");
%>