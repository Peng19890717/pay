<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.account.dao.PayAccSubject"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayAccSubject payAccSubject = (PayAccSubject)request.getAttribute("payAccSubject");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payAccSubject != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;">
            <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">科目编号：</td>
                <td><%=payAccSubject.glCode %></td>
            </tr>
            <tr>
                <td align="right">科目名称：</td>
                <td><%=payAccSubject.glName %></td>
            </tr>
            <tr>
                <td align="right">生效时间：</td>
                <td><%=payAccSubject.efctDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(payAccSubject.efctDate):"" %></td>
            </tr>
            <tr>
                <td align="right">失效时间：</td>
                <td><%=payAccSubject.expiredDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(payAccSubject.expiredDate):"" %></td>
            </tr>
            <tr>
                <td align="right">有无子目：</td>
                <td>
	                <c:if test="${payAccSubject.hasSl eq 'Y'}">有</c:if>
		    		<c:if test="${payAccSubject.hasSl eq 'N'}">无</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">有无细目：</td>
                <td>
                	<c:if test="${payAccSubject.hasDl eq 'Y'}">有</c:if>
		    		<c:if test="${payAccSubject.hasDl eq 'N'}">无</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">科目类别：</td>
                <td>
                	<c:if test="${payAccSubject.glType eq 1}">资产类</c:if>
                	<c:if test="${payAccSubject.glType eq 2}">负债类</c:if>
                	<c:if test="${payAccSubject.glType eq 3}">共同类</c:if>
                	<c:if test="${payAccSubject.glType eq 4}">权益类</c:if>
                	<c:if test="${payAccSubject.glType eq 5}">成本类</c:if>
                	<c:if test="${payAccSubject.glType eq 6}">损益类</c:if>
                	<c:if test="${payAccSubject.glType eq 9}">表外类</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">余额方向：</td>
                <td>
	                <c:if test="${payAccSubject.debitCredit eq 'D'}">借方</c:if>
	                <c:if test="${payAccSubject.debitCredit eq 'C'}">贷方</c:if>
	                <c:if test="${payAccSubject.debitCredit eq 'B'}">轧差</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">余额零标志：</td>
                <td>
                	<c:if test="${payAccSubject.zeroFlag eq 'N'}">不限制</c:if>
	                <c:if test="${payAccSubject.zeroFlag eq 'D'}">日终等于零</c:if>
	                <c:if test="${payAccSubject.zeroFlag eq 'M'}">月终等于零</c:if>
	                <c:if test="${payAccSubject.zeroFlag eq 'Y'}">年终等于零</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">允许对科目记账：</td>
                <td>
               		<c:if test="${payAccSubject.manualBkFlag eq 'Y'}">有</c:if>
		    		<c:if test="${payAccSubject.manualBkFlag eq 'N'}">否</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">总分核对标志：</td>
                <td>
                	<c:if test="${payAccSubject.totalChkFlag eq 'Y'}">有</c:if>
		    		<c:if test="${payAccSubject.totalChkFlag eq 'N'}">否</c:if>
                </td>
            </tr>
            <tr>
                <td align="right">创建人：</td>
                <td><%=payAccSubject.createName %></td>
            </tr>
            <tr>
                <td align="right">创建时间：</td>
                <td><%=payAccSubject.createTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.createTime):"" %></td>
            </tr>
            <tr>
                <td align="right">最后修改人：</td>
                <td><%=payAccSubject.lastUpdName %></td>
            </tr>
            <tr>
                <td align="right">最后修改时间：</td>
                <td><%=payAccSubject.lastUpdTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.lastUpdTime):"" %></td>
            </tr>
            <!-- 
             <tr>
                <td align="right">企业记账科目：</td>
                <td><%=payAccSubject.entBkGl %></td>
            </tr>
            <tr>
                <td align="right">开户柜员：</td>
                <td><%=payAccSubject.opnTlr %></td>
            </tr>
            <tr>
                <td align="right">开户日期：</td>
                <td><%=payAccSubject.opnTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.opnTime):"" %></td>
            </tr>
            <tr>
                <td align="right">关户柜员：</td>
                <td><%=payAccSubject.clsTlr %></td>
            </tr>
            <tr>
                <td align="right">关户日期：</td>
                <td><%=payAccSubject.clsTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.clsTime):"" %></td>
            </tr>
            <tr>
                <td align="right">上次交易柜员：</td>
                <td><%=payAccSubject.lastTxnTlr %></td>
            </tr>
            <tr>
                <td align="right">上次交易时间：</td>
                <td><%=payAccSubject.lastTxnDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.lastTxnDate):"" %></td>
            </tr>
            <tr>
                <td align="right">上日余额：</td>
                <td><%=payAccSubject.preDayAccBal %></td>
            </tr>
            <tr>
                <td align="right">当前余额：</td>
                <td><%=payAccSubject.accBal %></td>
            </tr>
            <tr>
                <td align="right">浮动余额：</td>
                <td><%=payAccSubject.fltAccBal %></td>
            </tr>
            <tr>
                <td align="right">借方积数：</td>
                <td><%=payAccSubject.drAccNum %></td>
            </tr>
            <tr>
                <td align="right">贷方积数：</td>
                <td><%=payAccSubject.crAccNum %></td>
            </tr>
            -->
        </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payAccSubjectDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
