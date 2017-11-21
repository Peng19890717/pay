<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.pay.refund.dao.PayRefund"%>
<%
String path = request.getContextPath();
PayRefund refund = (PayRefund)request.getAttribute("payRefundDetail");
%>
	<div class="pageContent">
	    <table style="margin-left:40px;margin-top:20px">
	    	<tr>
	    		<td height="30" width="100">退款订单号：</td>
	    		<td height="30" width="100">${payRefundDetail.refordno }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">商品订单号：</td>
	    		<td height="30" width="100">${payRefundDetail.oriprdordno }</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">退款金额：</td>
	    		<td height="30" width="100"><%=String.format("%.2f",(float)refund.rfamt*0.01) %>元</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">退款状态：</td>
	    		<td height="30" width="100">
	    			<c:if test="${payRefundDetail.banksts eq 00}">
	    			退款处理中
	    			</c:if>
	    			<c:if test="${payRefundDetail.banksts eq 01}">
	    			退款成功
	    			</c:if>
	    			<c:if test="${payRefundDetail.banksts eq 02}">
	    			退款失败
	    			</c:if>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">支付帐号：</td>
	    		<td height="30" width="100">${payRefundDetail.payacno}</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">交易货币：</td>
	    		<td height="30" width="100">人民币</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">手续费：</td>
	    		<td height="30" width="100"><%=String.format("%.2f",(float)refund.fee*0.01) %>元</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">实收净额：</td>
	    		<td height="30" width="100"><%=String.format("%.2f",(float)refund.netrecamt*0.01) %>元</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">退款申请时间：</td>
	    		<td height="30" width="100">${payRefundDetail.rfordtime}</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100">退款成功时间：</td>
	    		<td height="30" width="100">${payRefundDetail.rfreqdate}</td>
	    	</tr>
	    </table>
	</div>