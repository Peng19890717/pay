<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.risk.dao.PayRiskMerchantLimit"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayRiskMerchantLimit payRiskMerchantLimit = (PayRiskMerchantLimit)request.getAttribute("payRiskMerchantLimit");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payRiskMerchantLimit != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
            <tr><td  width="100">&nbsp;</td><td width="200">&nbsp;</td><td width="120">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">状态类型：</td>
                <td	colspan="3">
                	<c:if test="${payRiskMerchantLimit.limitType eq '1'}">所有商户</c:if>
                	<c:if test="${payRiskMerchantLimit.limitType eq '2'}">指定商户</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">商户编号：</td>
                <td	colspan="3">
                	<%=payRiskMerchantLimit.limitCompCode.equals("-1") ? "所有商户" : payRiskMerchantLimit.limitCompCode %>
                </td>
            </tr>
            
            <tr>
                <td align="right">限额生效范围：</td>
                <td	colspan="3">
                	<c:choose>
                		<c:when test="${ payRiskMerchantLimit.limitTimeFlag eq '0' }">
                			永久
                		</c:when>
                		<c:otherwise>
                			<%=payRiskMerchantLimit.limitStartDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskMerchantLimit.limitStartDate):"" %>
		                	~
		                	<%=payRiskMerchantLimit.limitEndDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskMerchantLimit.limitEndDate):"" %>
                		</c:otherwise>
                	</c:choose>
                </td>
            </tr>
             <tr>
                <td align="right">风险级别：</td>
                <td	colspan="3">
             		<c:if test="${ payRiskMerchantLimit.limitRiskLevel eq '-' }">-</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitRiskLevel eq '0' }">低</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitRiskLevel eq '1' }">中</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitRiskLevel eq '2' }">高</c:if>
                </td>
            </tr>
             <tr>
                <td align="right">限额类型：</td>
                <td	colspan="3">
             		<c:if test="${ payRiskMerchantLimit.limitCompType eq '1' }">消费</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitCompType eq '2' }">充值</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitCompType eq '3' }">结算</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitCompType eq '4' }">退款</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitCompType eq '5' }">提现</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitCompType eq '6' }">转账</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitCompType eq '15' }">代收</c:if>
             		<c:if test="${ payRiskMerchantLimit.limitCompType eq '16' }">代付</c:if>
                </td>
            </tr>
            <tr>
				<td align="right">单笔最小金额：</td>
				<td>
					<fmt:formatNumber value="${ payRiskMerchantLimit.limitMinAmt/100 }" type="currency" pattern=",##0.00#"/>元
				</td>
				<td align="right">
					 单笔最大金额：
				</td>
				<td>
					<fmt:formatNumber value="${ payRiskMerchantLimit.limitMaxAmt/100 }" type="currency" pattern=",##0.00#"/>元
				</td>
			</tr>
			<tr>
				<td align="right">日交易次数上限：</td>
				<td>
					<%=payRiskMerchantLimit.limitDayTimes %>次
				</td>
				<td align="right">
					日成功交易次数上限：
				</td>
				<td>
					<%=payRiskMerchantLimit.limitDaySuccessTimes %>次
				</td>
			</tr>
			<tr>
				<td align="right">日交易额上限：</td>
				<td>
					<fmt:formatNumber value="${ payRiskMerchantLimit.limitDayAmt/100 }" type="currency" pattern=",##0.00#"/>元
				</td>
				<td align="right">
					日成功交易额上限：
				</td>
				<td>
					<fmt:formatNumber value="${ payRiskMerchantLimit.limitDaySuccessAmt/100 }" type="currency" pattern=",##0.00#"/>元
				</td>
			</tr>
			<tr>
				<td align="right">月交易次数上限：</td>
				<td>
					<%=payRiskMerchantLimit.limitMonthTimes %>次
				</td>
				<td align="right">
					月成功交易次数上限：
				</td>
				<td>
					<%=payRiskMerchantLimit.limitMonthSuccessTimes %>次
				</td>
			</tr>
			<tr>
				<td align="right">月交易额上限：</td>
				<td>
					<fmt:formatNumber value="${ payRiskMerchantLimit.limitMonthAmt/100 }" type="currency" pattern=",##0.00#"/>元
				</td>
				<td align="right">
					月成功交易额上限：
				</td>
				<td>
					<fmt:formatNumber value="${ payRiskMerchantLimit.limitMonthSuccessAmt/100 }" type="currency" pattern=",##0.00#"/>元
				</td>
			</tr>
            <tr>
                <td align="right">启用标志：</td>
                <td	colspan="3">
                	<c:if test="${payRiskMerchantLimit.isUse eq '0'}">不可用</c:if>
                	<c:if test="${payRiskMerchantLimit.isUse eq '1'}">可用</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">备注：</td>
                <td	colspan="3">
                	<%=payRiskMerchantLimit.limitAddinfo == null ? "" :  payRiskMerchantLimit.limitAddinfo%>
                </td>
            </tr>
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payRiskMerchantLimitDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
