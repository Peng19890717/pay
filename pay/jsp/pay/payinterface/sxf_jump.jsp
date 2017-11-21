<%@page import="com.PayConstant"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<form method="post" id="form1" action="<%=PayConstant.PAY_CONFIG.get("SXF_PAYURl")%>">
	<input name="mercNo" type="hidden" value="<%=request.getAttribute("mercNo")%>"/>
	<input name="tranCd" type="hidden" value="<%=request.getAttribute("tranCd")%>"/>
	<input name="version" type="hidden" value="<%=request.getAttribute("version")%>"/>
	<input name="reqData" type="hidden" value="<%=request.getAttribute("reqData")%>"/>
	<input name="ip" type="hidden" value="<%=request.getAttribute("ip")%>"/>
	<input name="sign" type="hidden" value="<%=request.getAttribute("sign")%>"/>
	<input name="encodeType" type="hidden" value="<%=request.getAttribute("encodeType")%>"/>
	<input name="type" type="hidden" value="<%=request.getAttribute("type")%>"/>
</form>
<script type="text/javascript">
document.getElementById("form1").submit();
</script>
</body>
</html>
