<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="referrer" content="always">
<title>支付跳转中...</title>
</head>
<body>
<form action="<%=com.PayConstant.PAY_CONFIG.get("cj_Pay_url") %>" method="POST"  id="formS">
  	<input type="hidden" name="_input_charset"  value="<%=request.getAttribute("_input_charset")%>">
	<input type="hidden" name="bank_code" value="<%=request.getAttribute("bank_code")%>">
	<input type="hidden" name="is_anonymous" value="<%=request.getAttribute("is_anonymous")%>">
	<input type="hidden" name="return_url" value="<%=request.getAttribute("return_url")%>">
	<input type="hidden" name="notify_url" value="<%=request.getAttribute("notify_url")%>">
	<input type="hidden" name="out_trade_no" value="<%=request.getAttribute("out_trade_no")%>">
	<input type="hidden" name="partner_id" value="<%=request.getAttribute("partner_id")%>">
	<input type="hidden" name="pay_method" value="<%=request.getAttribute("pay_method")%>">
	<input type="hidden" name="pay_type"  value="<%=request.getAttribute("pay_type")%>">
	<input type="hidden" name="service"  value="<%=request.getAttribute("service")%>">
	<input type="hidden" name="sign"  value="<%=request.getAttribute("sign")%>">
	<input type="hidden" name="sign_type"  value="<%=request.getAttribute("sign_type")%>">
	<input type="hidden" name="trade_amount" value="<%=request.getAttribute("trade_amount")%>">
	<input type="hidden" name="version" value="<%=request.getAttribute("version")%>">
	
</form>
<script type="text/javascript">
	document.getElementById("formS").submit();
</script>
</body>
</html>