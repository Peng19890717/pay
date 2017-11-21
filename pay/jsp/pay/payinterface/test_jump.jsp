<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.PayConstant"%>
<%@page import="com.pay.order.dao.PayProductOrder"%>
<%@page import="com.pay.order.dao.PayOrder"%>
<%@page import="com.pay.merchantinterface.service.NotifyInterface"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%
PayProductOrder productOrder =(PayProductOrder)request.getAttribute("productOrder");
PayOrder payOrder =(PayOrder)request.getAttribute("payOrder");
if(payOrder!=null){
	payOrder.ordstatus="01";
	payOrder.actdat = new java.util.Date();
	payOrder.bankjrnno = util.Tools.getUniqueIdentify();
	new NotifyInterface().notifyMer(payOrder);
}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<style type="text/css">
.title {
	font-weight: bold;
	font-size: 24px;
	margin-top:200px;
	margin-left:100px
}
</style>
</head>
<body>
<div  style="margin-left:20%;">
<%if(payOrder!=null&&payOrder.bankcod.length()>0){ %>
    <img src="${ pageContext.request.contextPath }/jsp/pay/payinterface/banklogo/<%=payOrder.bankcod %>.gif" width="130" height="45"/><br/>
    <%} %>
</div>
<hr width="100%"/>
<table width="100%" border="0">
  <tr>
    <td width="34%">&nbsp;</td>
    <td colspan="2"><div class="title">网银模拟页面，支付成功。
    </div></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td width="16%" align="right">订单号：</td>
    <td width="60%"><%=payOrder!=null?payOrder.prdordno:"" %></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td width="16%" align="right">商品名称：</td>
    <td width="60%"><%=payOrder!=null?payOrder.prdname:"" %></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right">价格：</td>
    <td><%=payOrder!=null?payOrder.txamt:"" %>分</td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td width="16%" align="right">订单时间：</td>
    <td width="60%"><%=payOrder!=null?new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payOrder.actdat):"" %></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td align="right"><a href="<%=productOrder!=null?productOrder.returl:"#"  %>">返回商户</a></td>
    <td>&nbsp;</td>
  </tr>
</table>
</body>
</html>
