<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<form name="form1" id="form1" method="post" action="<%=PayConstant.PAY_CONFIG.get("rb_gateway")+"/web/portal" %>">
	<input type="hidden" name="merchant_id" value="<%=request.getAttribute("merchant_id") %>"/>
	<input type="hidden" name="data" value="<%=request.getAttribute("data") %>"/>
	<input type="hidden" name="encryptkey" value="<%=request.getAttribute("encryptkey") %>"/>
</form>
<script type="text/javascript">
document.getElementById("form1").submit();
</script>
</body>
</html>
