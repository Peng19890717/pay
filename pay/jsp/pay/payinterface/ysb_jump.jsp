<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<form method="post" id="form1" action="<%=PayConstant.PAY_CONFIG.get("YSB_WG_PAY_URL")%>">
	<input name="version" type="hidden" value="<%=request.getAttribute("version")%>"/>
	<input name="merchantId" type="hidden" value="<%=request.getAttribute("merchantId")%>"/>
	<input name="merchantUrl" type="hidden" value="<%=request.getAttribute("merchantUrl")%>"/>
	<input name="responseMode" type="hidden" value="<%=request.getAttribute("responseMode")%>"/>
	<input name="orderId" type="hidden" value="<%=request.getAttribute("orderId")%>"/>
	<input name="currencyType" type="hidden" value="<%=request.getAttribute("currencyType")%>"/>
	<input name="amount" type="hidden" value="<%=request.getAttribute("amount")%>"/>
	<input name="assuredPay" type="hidden" value="<%=request.getAttribute("assuredPay")%>"/>
	<input name="time" type="hidden" value="<%=request.getAttribute("time")%>"/>
	<input name="remark" type="hidden" value="<%=request.getAttribute("remark")%>"/>
	<input name="mac" type="hidden" value="<%=request.getAttribute("mac")%>"/>
	<input name="bankCode" type="hidden" value="<%=request.getAttribute("bankCode")%>"/> 
</form>
<script type="text/javascript">
document.getElementById("form1").submit();
</script>
</body>
</html>
