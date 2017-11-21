<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<form method="post" name="form1" id="form1" action="<%=PayConstant.PAY_CONFIG.get("bf_web_gate_way") %>">
	<input name="MemberID" type="hidden" value="<%=PayConstant.PAY_CONFIG.get("bf_web_member_id") %>"/>
	<input name="TerminalID" type="hidden" value="<%=PayConstant.PAY_CONFIG.get("bf_web_terminal_id") %>"/>
	<input name="InterfaceVersion" type="hidden" value= "<%=PayConstant.PAY_CONFIG.get("bf_web_interface_version") %>"/>
	<input name="KeyType" type="hidden" value= "1"/>
	<input name="PayID" type="hidden" value= "<%=request.getAttribute("PayID") %>"/>
	<input name="TradeDate" type="hidden" value= "<%=request.getAttribute("TradeDate") %>" />
	<input name="TransID" type="hidden" value= "<%=request.getAttribute("TransID") %>" />
	<input name="OrderMoney" type="hidden" value= "<%=request.getAttribute("OrderMoney") %>"/>
	<input name="ProductName" type="hidden" value= "<%=request.getAttribute("ProductName") %>"/>
	<input name="Amount" type="hidden" value= "1"/>
	<input name="Username" type="hidden" value= ""/>
	<input name="AdditionalInfo" type="hidden" value= ""/>
	<input name="PageUrl" type="hidden" value= "<%=request.getAttribute("PageUrl") %>"/>
	<input name="ReturnUrl" type="hidden" value= "<%=request.getAttribute("ReturnUrl") %>"/>
	<input name="Signature" type="hidden" value="<%=request.getAttribute("Signature") %>"/>
	<input name="NoticeType" type="hidden" value= "0"/>
</form>
<script type="text/javascript">
document.getElementById("form1").submit();
</script>
</body>
</html>
