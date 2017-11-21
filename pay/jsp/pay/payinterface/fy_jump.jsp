<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="referrer" content="always">
<title>支付跳转中...</title>
</head>
<body>
<form action="<%=com.PayConstant.PAY_CONFIG.get("fy_mchnt_url") %>" method="POST"  id="formS">
  	<input type="hidden" name="md5"  value="<%=request.getAttribute("md5")%>">
	<input type="hidden" name="mchnt_cd" value="<%=request.getAttribute("mchnt_cd")%>">
	<input type="hidden" name="order_id" value="<%=request.getAttribute("order_id")%>">
	<input type="hidden" name="order_amt" value="<%=request.getAttribute("order_amt")%>">
	<input type="hidden" name="order_pay_type" value="<%=request.getAttribute("order_pay_type")%>">
	<input type="hidden" name="page_notify_url" value="<%=request.getAttribute("page_notify_url")%>">
	<input type="hidden" name="back_notify_url" value="<%=request.getAttribute("back_notify_url")%>">
	<input type="hidden" name="order_valid_time" value="<%=request.getAttribute("order_valid_time")%>">
	<input type="hidden" name="iss_ins_cd"  value="<%=request.getAttribute("iss_ins_cd")%>">
	<input type="hidden" name="goods_name"  value="<%=request.getAttribute("goods_name")%>">
	<input type="hidden" name="goods_display_url"  value="<%=request.getAttribute("goods_display_url")%>">
	<input type="hidden" name="rem"  value="<%=request.getAttribute("rem")%>">
	<input type="hidden" name="ver" value="<%=request.getAttribute("ver")%>">
</form>
<script type="text/javascript">
	document.getElementById("formS").submit();
</script>
</body>
</html>