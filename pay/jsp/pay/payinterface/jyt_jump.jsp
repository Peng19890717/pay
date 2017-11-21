<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<form method="post" id="form1" action="<%=PayConstant.PAY_CONFIG.get("JYT_WG_PAY_URL")%>">
	<input name='tranCode' type='hidden' value="<%=request.getAttribute("tranCode")%>"><br/>
	<input name='version' type='hidden' value='<%=request.getAttribute("version")%>'><br/>
	<input name='charset' type='hidden' value="<%=request.getAttribute("charset")%>"><br/>
	<input name='uaType' type='hidden' value="<%=request.getAttribute("uaType")%>"><br/>
	<input name='merchantId' type='hidden' value="<%=request.getAttribute("merchantId")%>"><br/>
	<input name='merOrderId' type='hidden' value="<%=request.getAttribute("merOrderId")%>"><br/>
	<input name='merTranTime' type='hidden' value="<%=request.getAttribute("merTranTime")%>"><br/>
	<input name='merUserId' type='hidden' value="<%=request.getAttribute("merUserId")%>"><br/>
	<input name='orderDesc' type='hidden' value="<%=request.getAttribute("orderDesc")%>"><br/>
	<input name='prodInfo' type='hidden' value="<%=request.getAttribute("prodInfo")%>"><br/>
	<input name='tranAmt' type='hidden' value="<%=request.getAttribute("tranAmt")%>"><br/>
    <input name='curType' type='hidden' value="<%=request.getAttribute("curType")%>"><br/>
	<input name='payMode' type='hidden' value="<%=request.getAttribute("payMode")%>"><br/>
	<input name='bankCode' type='hidden' value="<%=request.getAttribute("bankCode")%>"><br/>
	<input name='bankCardType' type='hidden' value="<%=request.getAttribute("bankCardType")%>"><br/>
	<input name='notifyUrl' type='hidden' value="<%=request.getAttribute("notifyUrl")%>"><br/>
	<input name='backUrl' type='hidden' value="<%=request.getAttribute("backUrl")%>"><br/>
	<input name='signType' type='hidden' value="<%=request.getAttribute("signType")%>"><br/>
	<input name='sign' type="hidden" value="<%=request.getAttribute("sign")%>"><br/>
</form>
<script type="text/javascript">
document.getElementById("form1").submit();
</script>
</body>
</html>
