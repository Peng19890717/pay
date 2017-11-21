<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.PayConstant"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.merchantinterface.service.PayRequest"%>
<%@ page import="com.pay.bank.dao.PayBank"%>
<%@ page import="java.util.List"%>
<%@ page import="com.pay.order.dao.PayOrder"%>
<%@ page import="com.pay.order.dao.PayProductOrder"%>
<%@ page import="com.pay.order.dao.PayProductOrderDAO"%>
<%@ page import="com.pay.merchantinterface.dao.PayInterfaceDAO"%>
<%@ page import="com.pay.user.dao.PayTranUserInfo"%>
<%
if(!"0".equals(util.JWebConstant.SYS_CONFIG.get("DEBUG"))){
	out.println("无权访问此页面");
	return;
}
String path = request.getContextPath();
String payordno = request.getParameter("payordno");
PayOrder payOrder = new PayInterfaceDAO().getOrderByPrdordno(payordno);
if(payOrder == null){
	out.println("无效订单");
	return;
}
PayProductOrder prdOrder = new PayProductOrderDAO().getProductOrderById(payOrder.merno,payOrder.prdordno);
payOrder.payordno = payordno;
payOrder.actdat = new java.util.Date();
payOrder.ordstatus="01";
payOrder.bankjrnno = payordno;
new com.pay.merchantinterface.service.NotifyInterface().notifyMer(payOrder);
%>
	<!DOCTYPE html>
	<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-easyui-1.4/jquery.min.js"></script>
	<title>模拟微信/支付宝收银台</title>
	<link rel="stylesheet" type="text/css" href="<%=path %>/jsp/pay/payinterface/checkoutquick_1.css">
	</head>
	 <body>
	<div class="warpper">
   	    <header id="header_weixin">
	        <span>支付成功（模拟微信/支付宝收银台）</span>
		    </header>
		    <div class="mains">
		    	<div class="box"><label>商品名称：</label><span><%=prdOrder.prdname %></span></div>
		        <div class="box"><label>订单编号：</label><span><%=payOrder.prdordno %></span></div>
		        <div class="boxs"><label>应付金额：</label><span><%=String.format("%.2f", ((double)payOrder.txamt)/100d) %>元</span></div>
		    </div>
	    <div class="main" style="background-color:#f5f5f7;">
	    <section class="con">  
	        <div class="pic"><img src="<%=path %>/images/ok.png"></div>
	    </section>
		</div>
		<%if(prdOrder.returl != null && prdOrder.returl.length()>0){ %>
		<input type="submit" class="btn btn3" onclick="location.href='<%=prdOrder.returl %>'" value="支付成功！返回商家">
		<%} else { %>
		<input type="submit" class="btn btn3" value="支付成功！">
		<%} %>
	</div>
	</body></html>
