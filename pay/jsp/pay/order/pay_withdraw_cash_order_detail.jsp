<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.order.dao.PayWithdrawCashOrder"%>
<%@ page import="com.pay.order.dao.PayAccProfile"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayWithdrawCashOrder payWithdrawCashOrder = (PayWithdrawCashOrder)request.getAttribute("payWithdrawCashOrder");
PayAccProfile payAccProfile = (PayAccProfile)request.getAttribute("payAccProfile");
%>
<script type="text/javascript">
	$(function(){
		hideText("bankCardNoHiddenFormat");
		hideText("nameHiddenFormat");
	});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payWithdrawCashOrder != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
            <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">提现订单号：</td>
                <td><%=payWithdrawCashOrder.casordno %></td>
            </tr>
            <tr>
                <td align="right">申请时间：</td>
                <td><%=payWithdrawCashOrder.actTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(payWithdrawCashOrder.actTime):"" %></td>
            </tr>
            <tr>
                <td align="right">提现金额：</td>
                <td>
                	<%=String.format("%.2f",(double)payWithdrawCashOrder.txamt*0.01) %>元&nbsp;&nbsp;&nbsp;&nbsp;（<%=util.PayUtil.digitUppercase((double)payWithdrawCashOrder.txamt*0.01) %>）
                </td>
            </tr>
            <tr>
                <td align="right">账户余额：</td>
                <td>
                	<%=String.format("%.2f",(float)payAccProfile.consAcBal*0.01) %>元
                </td>
            </tr>
            <tr>
                <td align="right">客户名称：</td>
                <td>${ payMerchant.storeName }</td>
            </tr>
            <tr>
                <td align="right">银行名称：</td>
                <td><%=payWithdrawCashOrder.bankName==null?"":payWithdrawCashOrder.bankName %></td>
            </tr>
            <tr>
                <td align="right">结算帐号：</td>
                <td><span><%=payWithdrawCashOrder.bankpayacno %></span></td>
            </tr>
            <tr>
                <td align="right">收款帐户名：</td>
                <td><span><%=payWithdrawCashOrder.bankpayusernm==null?"":payWithdrawCashOrder.bankpayusernm %></span></td>
            </tr>
            <tr>
                <td align="right">提现成功时间：</td>
                <td><%=payWithdrawCashOrder.sucTime != null&&"02".equals(payWithdrawCashOrder.ordstatus) ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(payWithdrawCashOrder.sucTime):"" %></td>
            </tr>
            <tr>
                <td align="right">提现状态：</td>
                <td>
                	<c:if test="${ payWithdrawCashOrder.ordstatus eq '00' }">未处理</c:if>
                	<c:if test="${ payWithdrawCashOrder.ordstatus eq '01' }">提现处理中</c:if>
                	<c:if test="${ payWithdrawCashOrder.ordstatus eq '02' }">提现成功</c:if>
                	<c:if test="${ payWithdrawCashOrder.ordstatus eq '03' }">提现失败</c:if>
                	<c:if test="${ payWithdrawCashOrder.ordstatus eq '04' }">已退回支付帐号</c:if>
                	<c:if test="${ payWithdrawCashOrder.ordstatus eq '99' }">超时</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">备注：</td>
                <td><%=payWithdrawCashOrder.bankerror==null?"":payWithdrawCashOrder.bankerror %></td>
            </tr>
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payWithdrawCashOrderDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
