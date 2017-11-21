<%@ page contentType="text/html; charset=gb2312" language="java"%>
<html>
	<title>Ö§¸¶Ìø×ªÖÐ...</title>
<body>
<form action="<%=com.PayConstant.PAY_CONFIG.get("sxy_merchant_url") %>" method="POST"  id="formS">
  	<input type="hidden" name="v_md5info" size="100"  value="<%=request.getAttribute("digestString")%>">
	<input type="hidden" name="v_mid" value="<%=request.getAttribute("v_mid")%>">
	<input type="hidden" name="v_oid" value="<%=request.getAttribute("v_oid")%>">
	<input type="hidden" name="v_rcvname" value="<%=request.getAttribute("v_rcvname")%>">
	<input type="hidden" name="v_rcvaddr" value="<%=request.getAttribute("v_rcvaddr")%>">
	<input type="hidden" name="v_rcvtel" value="<%=request.getAttribute("v_rcvtel")%>">
	<input type="hidden" name="v_rcvpost" value="<%=request.getAttribute("v_rcvpost")%>">
	<input type="hidden" name="v_amount" value="<%=request.getAttribute("v_amount")%>">
	<input type="hidden" name="v_ymd"  value="<%=request.getAttribute("v_ymd")%>">
	<input type="hidden" name="v_orderstatus"  value="<%=request.getAttribute("v_orderstatus")%>">
	<input type="hidden" name="v_ordername"  value="<%=request.getAttribute("v_ordername")%>">
	<input type="hidden" name="v_moneytype"  value="<%=request.getAttribute("v_moneytype")%>">
	<input type="hidden" name="v_pmode" value="<%=request.getAttribute("v_pmode")%>">
	<input type="hidden" name="v_url" value="<%=request.getAttribute("v_url")%>">
</form>
<script type="text/javascript">
	document.getElementById("formS").submit();
</script>
</body>
</html>