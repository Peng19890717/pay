<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<form method="post" id="form1" action="<%=PayConstant.PAY_CONFIG.get("CX_WG_PAY_URL")%>">
	<input name="serviceName" type="hidden" value="<%=request.getAttribute("serviceName")%>"/>
	<input name="version" type="hidden" value="<%=request.getAttribute("version")%>"/>
	<input name="merchantId" type="hidden" value="<%=request.getAttribute("merchantId")%>"/>
	<input name="payType" type="hidden" value="<%=request.getAttribute("payType")%>"/>
	<input name="charset" type="hidden" value="<%=request.getAttribute("charset")%>"/>
	<input name="merOrderId" type="hidden" value="<%=request.getAttribute("merOrderId")%>"/>
	<input name="currency" type="hidden" value="<%=request.getAttribute("currency")%>"/>
	<input name="notifyUrl" type="hidden" value="<%=request.getAttribute("notifyUrl")%>"/>
	<input name="productName" type="hidden" value="<%=request.getAttribute("productName")%>"/>
	<input name="productDesc" type="hidden" value="<%=request.getAttribute("productDesc")%>"/>
	<input name="tranAmt" type="hidden" value="<%=request.getAttribute("tranAmt")%>"/>
	<input name="tranTime" type="hidden" value="<%=request.getAttribute("tranTime")%>"/> 
	<input name="bankCardType" type="hidden" value="<%=request.getAttribute("bankCardType")%>"/> 
	<input name="bankCode" type="hidden" value="<%=request.getAttribute("bankCode")%>"/> 
	<input name="clientIp" type="hidden" value="<%=request.getAttribute("clientIp")%>"/> 
	<input name="signType" type="hidden" value="<%=request.getAttribute("signType")%>"/> 
	<input name="sign" type="hidden" value="<%=request.getAttribute("sign")%>"/> 
	
</form>
<script type="text/javascript">
document.getElementById("form1").submit();
</script>
</body>
</html>
