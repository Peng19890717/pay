<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<form method="post" id="form1" action="<%=PayConstant.PAY_CONFIG.get("BFB_PAY_URL")%>">
	<input name="service" type="hidden" value="<%=request.getAttribute("service")%>"/>
	<input name="charset" type="hidden" value="<%=request.getAttribute("charset")%>"/>
	<input name="version" type="hidden" value="<%=request.getAttribute("version")%>"/>
	<input name="signType" type="hidden" value="<%=request.getAttribute("signType")%>"/>
	<input name="pageReturnUrl" type="hidden" value="<%=request.getAttribute("pageReturnUrl")%>"/>
	<input name="offlineNotifyUrl" type="hidden" value="<%=request.getAttribute("offlineNotifyUrl")%>"/>
	<input name="requestId" type="hidden" value="<%=request.getAttribute("requestId")%>"/>
	<input name="notifyUrl" type="hidden" value="<%=request.getAttribute("notifyUrl")%>"/>
	<input name="merchantId" type="hidden" value="<%=request.getAttribute("merchantId")%>"/>
	<input name="merchantName" type="hidden" value="<%=request.getAttribute("merchantName")%>"/>
	<input name="orderId" type="hidden" value="<%=request.getAttribute("orderId")%>"/>
	<input name="cardType" type="hidden" value="<%=request.getAttribute("cardType")%>"/> 
	<input name="orderTime" type="hidden" value="<%=request.getAttribute("orderTime")%>"/> 
	<input name="totalAmount" type="hidden" value="<%=request.getAttribute("totalAmount")%>"/> 
	<input name="currency" type="hidden" value="<%=request.getAttribute("currency")%>"/> 
	<input name="validUnit" type="hidden" value="<%=request.getAttribute("validUnit")%>"/> 
	<input name="validNum" type="hidden" value="<%=request.getAttribute("validNum")%>"/> 
	<input name="productName" type="hidden" value="<%=request.getAttribute("productName")%>"/> 
	<input name="merchantSign" type="hidden" value="<%=request.getAttribute("merchantSign")%>"/> 
	<input name="merchantCert" type="hidden" value="<%=request.getAttribute("merchantCert")%>"/> 
	<input name="bankAbbr" type="hidden" value="<%=request.getAttribute("bankAbbr")%>"/>
</form>
<script type="text/javascript">
document.getElementById("form1").submit();
</script>
</body>
</html>
