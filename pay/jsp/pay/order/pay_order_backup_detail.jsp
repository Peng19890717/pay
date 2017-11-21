<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.pay.order.dao.PayOrder"%>
<%
String path = request.getContextPath();
PayOrder order = (PayOrder)request.getAttribute("payOrderDetail");
%>
	<div class="pageContent">
	    <table style="margin-left:40px;margin-top:20px">
	    	<tr>
	    		<td height="30" width="100">支付单号：</td>
	    		<td height="30" width="500">${payOrderDetail.payordno }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">订单时间：</td>
	    		<td height="30" width="500">${payOrderDetail.createtime }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">订单金额：</td>
	    		<td height="30" width="500"><%=String.format("%.2f",(float)order.txamt*0.01) %>元</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">支付方式：</td>
	    		<td height="30" width="500">
	    			<c:if test="${payOrderDetail.paytype eq 00}">
	    			支付帐户
	    			</c:if>
	    			<c:if test="${payOrderDetail.paytype eq 01}">
	    			网上银行
	    			</c:if>
	    			<c:if test="${payOrderDetail.paytype eq 02}">
	    			终端
	    			</c:if>
	    			<c:if test="${payOrderDetail.paytype eq 03}">
	    			快捷支付
	    			</c:if>
	    			<c:if test="${payOrderDetail.paytype eq 04}">
	    			混合支付
	    			</c:if>
	    			<c:if test="${payOrderDetail.paytype eq 05}">
	    			支票/汇款
	    			</c:if>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">订单类型：</td>
	    		<td height="30" width="500">
	    			<c:if test="${payOrderDetail.prdordtype eq 0}">
	    			消费
	    			</c:if>
	    			<c:if test="${payOrderDetail.prdordtype eq 1}">
	    			充值
	    			</c:if>
	    			<c:if test="${payOrderDetail.prdordtype eq 2}">
	    			担保支付
	    			</c:if>
	    			<c:if test="${payOrderDetail.prdordtype eq 3}">
	    			商户充值
	    			</c:if>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">订单状态：</td>
	    		<td height="30" width="500">
	    			<c:if test="${payOrderDetail.ordstatus eq 00}">
	    			等待付款
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 01}">
	    			付款成功
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 02}">
	    			付款失败
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 03}">
	    			退款申请
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 04}">
	    			等待退款
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 05}">
	    			退款成功
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 06}">
	    			退款失败
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 07}">
	    			同意撤销
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 08}">
	    			拒绝撤销
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 09}">
	    			撤销成功
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 10}">
	    			撤销失败
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 12}">
	    			预付款
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 17}">
	    			实付审核中
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 18}">
	    			实付审核通过
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 19}">
	    			实付审核拒绝
	    			</c:if>
	    			<c:if test="${payOrderDetail.ordstatus eq 99}">
	    			超时
	    			</c:if>
	    		
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">支付时间：</td>
	    		<td height="30" width="500">${payOrderDetail.actdat }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">通知地址：</td>
	    		<td height="30" width="500">${payOrderDetail.notifyurl }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">返回地址：</td>
	    		<td height="30" width="500">${payOrderDetail.returl }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">快捷通知地址：</td>
	    		<td height="30" width="500">${payOrderDetail.receivePayNotifyUrl }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">买家姓名：</td>
	    		<td height="30" width="500">${payOrderDetail.bankpayusernm }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">手机号：</td>
	    		<td height="30" width="500">${payOrderDetail.mobile }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">身份证号：</td>
	    		<td height="30" width="500">${payOrderDetail.credentialNo }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">银行卡号：</td>
	    		<td height="30" width="500">${payOrderDetail.bankpayacno }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">签名信息：</td>
	    		<td height="30" width="1000">${payOrderDetail.signature }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">请求报文：</td>
	    		<td height="30" width="1000"><c:out value="${payOrderDetail.verifystring }" escapeXml="true"></c:out></td>
	    	</tr>
	    </table>
	</div>