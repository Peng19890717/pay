<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.DataTransUtil"%>
<%
String path = request.getContextPath();
new DataTransUtil().doPost("https://localhost:8443/pay/testNotify.htm?payordno="
	+request.getParameter("payordno"),null);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<div align="center">
<form name="form1" method="post" action="">
  <table width="100%" height="100%" border="0">
    <tr>
      <td width="30%">&nbsp;</td>
      <td width="37%"><h3>模拟工商银行网上银行</h3></td>
      <td width="33%">&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>支付成功。<a href="http://localhost:8080/pay-interface/">返回商家</a></td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>
</form>
</div>
</body>
</html>
