<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
</head>
<body>
<div align="center">
<form name="form1" method="post" action="<%=path%>//jsp/pay/payinterface/end.jsp">
  <table width="100%" height="100%" border="0">
  	<input type="hidden" name="payordno" value="<%=request.getAttribute("payordno") %>"/>
    <tr>
      <td width="30%">&nbsp;</td>
      <td width="37%"><h3>模拟${ payOrder.bankcod }网上银行</h3></td>
      <td width="33%">&nbsp;</td>
    </tr>
    <tr>
      <td height="31" align="right">卡号：</td>
      <td><input type="text" name="card"  value="6225880145474578"></td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td height="31" align="right">密码：</td>
      <td><input type="password" name="pwd"  value="445547"></td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td height="31" align="right">手机号：</td>
      <td><input type="text" name="tel"  value="13125245475"></td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td height="31" align="right">验证码：</td>
      <td><input type="text" name="telCode"  value="325412"></td>
      <td>&nbsp;</td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td valign="top"><input type="submit" name="button" id="button" value="提交"></td>
      <td>&nbsp;</td>
    </tr>
  </table>
</form>
</div>
</body>
</html>
