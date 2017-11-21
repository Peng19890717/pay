<%@ page contentType="text/html; charset=gb2312" language="java"%>
<%@page import="com.PayConstant"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<form method="post" name="form1" id="form1" action="<%=PayConstant.PAY_CONFIG.get("JD_B2C_WEB_GAGEWAY_URL") %>">
	<input type="hidden" name="v_md5info" value="<%=request.getAttribute("v_md5info")==null?"": request.getAttribute("v_md5info") %>" size="100">
	<input type="hidden" name="v_mid" value="<%=request.getAttribute("v_mid")==null?"": request.getAttribute("v_mid") %>">
	<input type="hidden" name="v_oid" value="<%=request.getAttribute("v_oid")==null?"": request.getAttribute("v_oid") %>">
	<input type="hidden" name="v_amount" value="<%=request.getAttribute("v_amount")==null?"": request.getAttribute("v_amount") %>">
	<input type="hidden" name="v_moneytype" value="<%=request.getAttribute("v_moneytype")==null?"": request.getAttribute("v_moneytype") %>">
	<input type="hidden" name="v_url" value="<%=request.getAttribute("v_url")==null?"": request.getAttribute("v_url") %>"> 
	<input type="hidden" name="remark1" value="<%=request.getAttribute("remark1")==null?"": request.getAttribute("remark1") %>">
	<input type="hidden" name="remark2" value="<%=request.getAttribute("remark2")==null?"": request.getAttribute("remark2") %>">
	<input type="hidden" name="pmode_id" value="<%=request.getAttribute("pmode_id")==null?"": request.getAttribute("pmode_id") %>">
	<input type="hidden" name="v_rcvname" value="<%=request.getAttribute("v_rcvname")==null?"": request.getAttribute("v_rcvname") %>">
	<input type="hidden" name="v_rcvaddr" value="<%=request.getAttribute("v_rcvaddr")==null?"": request.getAttribute("v_rcvaddr") %>">
	<input type="hidden" name="v_rcvtel" value="<%=request.getAttribute("v_rcvtel")==null?"": request.getAttribute("v_rcvtel") %>">
	<input type="hidden" name="v_rcvpost" value="<%=request.getAttribute("v_rcvpost")==null?"": request.getAttribute("v_rcvpost") %>">
	<input type="hidden" name="v_rcvemail" value="<%=request.getAttribute("v_rcvemail")==null?"": request.getAttribute("v_rcvemail") %>">
	<input type="hidden" name="v_rcvmobile" value="<%=request.getAttribute("v_rcvmobile")==null?"": request.getAttribute("v_rcvmobile") %>">
	<input type="hidden" name="v_ordername" value="<%=request.getAttribute("v_ordername")==null?"": request.getAttribute("v_ordername") %>">
	<input type="hidden" name="v_orderaddr" value="<%=request.getAttribute("v_orderaddr")==null?"": request.getAttribute("v_orderaddr") %>">
	<input type="hidden" name="v_ordertel" value="<%=request.getAttribute("v_ordertel")==null?"": request.getAttribute("v_ordertel") %>">
	<input type="hidden" name="v_orderpost" value="<%=request.getAttribute("v_orderpost")==null?"": request.getAttribute("v_orderpost") %>">
	<input type="hidden" name="v_orderemail" value="<%=request.getAttribute("v_orderemail")==null?"": request.getAttribute("v_orderemail") %>">
	<input type="hidden" name="v_ordermobile" value="<%=request.getAttribute("v_ordermobile")==null?"": request.getAttribute("v_ordermobile") %>">
</form>
<script type="text/javascript">
document.getElementById("form1").submit();
</script>
</body>
</html>
