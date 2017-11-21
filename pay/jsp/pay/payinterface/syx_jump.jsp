<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<form method="post" id="form1" action="<%=PayConstant.PAY_CONFIG.get("SYX_PAY_URL")%>">
	<input name="service"  type="hidden" value="<%=request.getAttribute("service")%>"/>
	<input name="inputCharset" type="hidden" value="<%=request.getAttribute("inputCharset")%>"/>
	<input name="signType"  type="hidden" value="<%=request.getAttribute("signType")%>"/>
	<input name="returnUrl" type="hidden" value="<%=request.getAttribute("returnUrl")%>"/>
	<input name="notifyUrl" type="hidden" value="<%=request.getAttribute("notifyUrl")%>"/>
	<input name="merchantId" type="hidden" value="<%=request.getAttribute("merchantId")%>"/>
	<input name="outOrderId" type="hidden" value="<%=request.getAttribute("outOrderId")%>"/>
	<input name="payMethod" type="hidden" value="<%=request.getAttribute("payMethod")%>"/>
	<input name="subject"  type="hidden"value="<%=request.getAttribute("subject")%>"/> 
	<input name="body" type="hidden"  value="<%=request.getAttribute("body")%>"/> 
	<input name="defaultBank" type="hidden" value="<%=request.getAttribute("defaultBank")%>"/> 
	<input name="channel" type="hidden" value="<%=request.getAttribute("channel")%>"/> 
	<input name="cardAttr"  type="hidden" value="<%=request.getAttribute("cardAttr")%>"/> 
	<input name="transAmt" type="hidden" value="<%=request.getAttribute("transAmt")%>"/> 
	<input name="sign" type="hidden" value="<%=request.getAttribute("sign")%>"/> 
</form> 
<script type="text/javascript">
document.getElementById("form1").submit();
</script>
</body>
</html>
