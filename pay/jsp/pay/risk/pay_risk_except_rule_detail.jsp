<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.risk.dao.PayRiskExceptRule"%>
<%@ page import="com.pay.risk.dao.PayRiskMerchantLimit"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayRiskExceptRule payRiskExceptRule = (PayRiskExceptRule)request.getAttribute("payRiskExceptRule");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payRiskExceptRule != null){ %>
        <table cellpadding="5" width="100%" style="margin-top:-10px;" id="updatePayRiskExceptRuleFormTable">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">规则名称：</td>
                        <td><%=payRiskExceptRule.ruleName != null ? payRiskExceptRule.ruleName : "" %></td>
                    </tr>
                    <tr>
                        <td align="right">规则类型：</td>
                        <td>
                        	<c:if test="${ payRiskExceptRule.ruleType eq '0'}">用户</c:if>
                        	<c:if test="${ payRiskExceptRule.ruleType eq '1'}">商户</c:if>
                         </td>
                    </tr>
                    <tr>
                        <td align="right">是否线上：</td>
                        <td>
                      		<c:if test="${ payRiskExceptRule.isOnline eq '0'}">线上</c:if>
                      		<c:if test="${ payRiskExceptRule.isOnline eq '1'}">线下</c:if>
                        </td>
                    </tr>
                    <tr>
                    	<td align="right">生效时间范围：</td>
                    	<td>
                    		<%=payRiskExceptRule.ruleStartDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskExceptRule.ruleStartDate):"" %>
                    		~
                    		<%=payRiskExceptRule.ruleEndDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(payRiskExceptRule.ruleEndDate):"" %>
                    	</td>
                    </tr>
                    <tr>
                        <td align="right">异常类型：</td>
                        <td>
                          <c:if test="${ payRiskExceptRule.excpType eq '1'}">异常</c:if>
                          <c:if test="${ payRiskExceptRule.excpType eq '2'}">可疑</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">预警项：</td>
                        <td>
                            <c:if test="${ payRiskExceptRule.ruleLevelItem eq '0'}">交易突增</c:if>
                           	<c:if test="${ payRiskExceptRule.ruleLevelItem eq '1'}">疑似套现</c:if>
                           	<c:if test="${ payRiskExceptRule.ruleLevelItem eq '2'}">分单</c:if>
                           	<c:if test="${ payRiskExceptRule.ruleLevelItem eq '3'}">非常规时段交易</c:if>
                           	<c:if test="${ payRiskExceptRule.ruleLevelItem eq '4'}">零交易</c:if>
                           	<c:if test="${ payRiskExceptRule.ruleLevelItem eq '5'}">其它</c:if>
                           	<c:if test="${ payRiskExceptRule.ruleLevelItem eq '6'}">无</c:if>
                        </td>
                    </tr>
                    <!--  
                    <tr>
                        <td align="right">业务类型：</td>
                        <td>
                       		<%	
		             			String result = null;
		               			java.util.Iterator<String> it = com.PayConstant.MER_BIZ_TYPE.keySet().iterator();
		               			while(it.hasNext()){
		               				String key = it.next();
		               				String value = com.PayConstant.MER_BIZ_TYPE.get(key);
		               				if(payRiskExceptRule != null && key.equals(payRiskExceptRule.comTypeNo)){
		               					result = value;
		               					break;
		               				}
		               				%>
		               			<%}%>
		               			<%=result%>
                        </td>
                    </tr>
                    -->
                    <tr>
                        <td align="right">规则描述：</td>
                        <td>
                             <%=payRiskExceptRule.ruleDes != null ? payRiskExceptRule.ruleDes : "" %>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">规则等级：</td>
                        <td>
	                        <c:if test="${ payRiskExceptRule.ruleLevel eq '0'}">一般</c:if>
		                   	<c:if test="${ payRiskExceptRule.ruleLevel eq '1'}">紧急</c:if>
		                   	<c:if test="${ payRiskExceptRule.ruleLevel eq '2'}">无等级</c:if>
                        </td>
                    </tr>
                    <tr>
                    	<td align="right" valign="top">阈值参数：</td>
                    	<td>
                    		<table border="1" bordercolor="#bbb" cellspacing="0" cellpadding="0" width="200px">
                    			<tr align="center">
                    				<th>名称</th>
                    				<th>数值</th>
                    				<th>类型</th>
                    			</tr>
                    			<c:forEach items="${ payRiskExceptRuleParamSet }" var="set">
							        <tr align="center">
							            <td>${ set.paramName }</td>
							            <td>${ set.paramValue }</td>
							            <td>${ set.paramType }</td>
							        </tr>
						        </c:forEach>
					        </table>
                    	</td>
                    </tr>
            </table>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payRiskExceptRuleDetailPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
