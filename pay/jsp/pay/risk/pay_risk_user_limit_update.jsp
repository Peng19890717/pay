<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.risk.dao.PayRiskUserLimit"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayRiskUserLimit payRiskUserLimit = (PayRiskUserLimit)request.getAttribute("payRiskUserLimitUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="updatePayRiskUserLimitForm" method="post">
        	<input type="hidden" name="id" value="${payRiskUserLimitUpdate.id }"/>
        	<input type="hidden" name="tranType" value="${payRiskUserLimitUpdate.tranType }"/>
            <table cellpupdateing="5" width="100%" style="margin-top:-10px;">
                <tr><td  width="160">&nbsp;</td><td width="200">&nbsp;</td><td width="120">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">用户状态类型：</td>
                        <td colspan="3">
                        	<c:if test="${payRiskUserLimitUpdate.userType eq '1' }">实名</c:if>
                        	<c:if test="${payRiskUserLimitUpdate.userType eq '2' }">非实名</c:if>
                        	<c:if test="${payRiskUserLimitUpdate.userType eq '3' }">指定用户</c:if>
                        </td>
                    </tr>
                    <tr id="xListTypeFlagForUpdate">
                        <td align="right">用户名单状态：</td>
                        <td colspan="3">
                        	<c:if test="${payRiskUserLimitUpdate.xListType eq '1' }">白名单</c:if>
                        	<c:if test="${payRiskUserLimitUpdate.xListType eq '4' }">红名单</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">交易类型：</td>
                        <td colspan="3">
                        	<c:if test="${payRiskUserLimitUpdate.tranType eq '1' }">消费</c:if>
                        	<c:if test="${payRiskUserLimitUpdate.tranType eq '2' }">充值</c:if>
                        	<c:if test="${payRiskUserLimitUpdate.tranType eq '5' }">提现</c:if>
                        	<c:if test="${payRiskUserLimitUpdate.tranType eq '6' }">转账</c:if>
                        	<c:if test="${payRiskUserLimitUpdate.tranType eq '7' }">卡限额</c:if>
                        </td>
                    </tr>
                    <tr>
                    	<td align="right">生效时间：</td>
                        <td colspan="3">
                        	<input type="radio" name="limitTimeFlag"  value="0" data-options="required:true" <c:if test="${payRiskUserLimitUpdate.limitTimeFlag eq '0' }">checked</c:if>/>永久&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                       	<input type="radio" name="limitTimeFlag"  value="1" data-options="required:true" <c:if test="${payRiskUserLimitUpdate.limitTimeFlag eq '1' }">checked</c:if>/>按交易时间&nbsp;
	                       	<div style="display: inLine" id="timeScopeDivForUserRiskLimitTime">
	                        	<input class="easyui-datebox" style="width:100px" data-options="editable:false,required:true" value="<%="1".equals(payRiskUserLimit.limitTimeFlag)?new SimpleDateFormat("yyyy-MM-dd").format(payRiskUserLimit.startDate):""%>" 
	                        	id="payRiskUserLimitTimeStartDate" name="startDate" missingMessage="请输入开始时间"/>
	                        	 ~ 
	                        	<input class="easyui-datebox" style="width:100px" data-options="editable:false,required:true" value="<%="1".equals(payRiskUserLimit.limitTimeFlag)?new SimpleDateFormat("yyyy-MM-dd").format(payRiskUserLimit.endDate):""%>" 
	                        	id="payRiskUserLimitTimeEndDate" name="endDate" missingMessage="请输入结束时间"/>
                           	</div>
                       	</td>
                    </tr>
                    <!-- 交易类型为非卡限额非消费 -->
                    <c:if test="${payRiskUserLimitUpdate.tranType ne '1' and payRiskUserLimitUpdate.tranType ne '7'}">
	                    <tr id="tranTypeLimitTranForUpdate">
	                        <td colspan="4">
		                      <table cellpupdateing="5" width="100%">
		                       	<tr>
			                        <td width="160" align="right">单笔最小金额：</td>
			                        <td width="200"><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMinAmtTemp" missingMessage="请输入单笔最小金额" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(0).minAmt)/100d) %>"/>元</td>
			                        <td width="120" align="right">单笔最大金额：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMaxAmtTemp" missingMessage="请输入单笔最大金额" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(0).maxAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">日交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDayTimes" name="dayTimes" missingMessage="请输入日交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(0).dayTimes%>"/>次</td>
			                        <td align="right">日成功交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDaySucTimes" name="daySucTimes" missingMessage="请输入日成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(0).daySucTimes%>"/>次</td>
			                    </tr>
			                    <tr>
			                        <td align="right">日交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDayAmtTemp" missingMessage="请输入日交易限额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(0).dayAmt)/100d) %>"/>元</td>
			                        <td align="right">日成功交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDaySucAmtTemp" missingMessage="请输入日成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(0).daySucAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">月交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="updatePayRiskUserLimitMonthTimes" name="monthTimes" missingMessage="请输入月交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(0).monthTimes%>"/>次</td>
			                        <td align="right">月成功交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="updatePayRiskUserLimitMonthSucTimes" name="monthSucTimes" missingMessage="请输入月成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(0).monthSucTimes%>"/>次</td>
			                    </tr>
			                    <tr>
			                        <td align="right">月交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMonthAmtTemp" missingMessage="请输入月交易限额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(0).monthAmt)/100d) %>"/>元</td>
			                        <td align="right">月成功交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMonthSucAmtTemp" missingMessage="请输入月成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(0).monthSucAmt)/100d) %>"/>元</td>
			                    </tr>
		                      </table>
	                       	</td>
	                    </tr>
	                    <input type="hidden" name="minAmt" id="updatePayRiskUserLimitMinAmt" value="<%=payRiskUserLimit.subLimitList.get(0).minAmt %>">
						<input type="hidden" name="maxAmt" id="updatePayRiskUserLimitMaxAmt" value="<%=payRiskUserLimit.subLimitList.get(0).maxAmt %>">
						<input type="hidden" name="dayAmt" id="updatePayRiskUserLimitDayAmt" value="<%=payRiskUserLimit.subLimitList.get(0).dayAmt %>">
						<input type="hidden" name="daySucAmt" id="updatePayRiskUserLimitDaySucAmt" value="<%=payRiskUserLimit.subLimitList.get(0).daySucAmt %>">
						<input type="hidden" name="monthAmt" id="updatePayRiskUserLimitMonthAmt" value="<%=payRiskUserLimit.subLimitList.get(0).monthAmt %>">
						<input type="hidden" name="monthSucAmt" id="updatePayRiskUserLimitMonthSucAmt" value="<%=payRiskUserLimit.subLimitList.get(0).monthSucAmt %>">
	                </c:if>
	                <!-- 交易类型为消费-->
                    <c:if test="${payRiskUserLimitUpdate.tranType eq '1' }">
	                    <tr id="tranTypeLimitConsumeForUpdate">
	                        <td colspan="4">
	                        <%
		                    for(int i=0; i<com.PayConstant.TRAN_TYPE_CONSUMES.length; i++){ 
		                    	if(i==0){//账户支付，只修改成功交易部分
	                     	%><table cellpupdateing="5" width="800">
		                      	<tr><td bgcolor="#CCCCCC" colspan="4"><%=com.PayConstant.TRAN_TYPE_CONSUMES[i] %></td></tr>
		                      	<input type="hidden" name="dayTimes<%="0"+i %>" value="<%= payRiskUserLimit.subLimitList.get(i).dayTimes%>"/>
		                      	<input type="hidden" name="monthTimes<%="0"+i %>" value="<%= payRiskUserLimit.subLimitList.get(i).monthTimes%>"/>
		                       	<tr>
			                        <td width="160" align="right">单笔最小金额：</td>
			                        <td width="200"><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMinAmt<%="0"+i %>Temp" missingMessage="请输入单笔最小金额" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).minAmt)/100d) %>"/>元</td>
			                        <td width="120" align="right">单笔最大金额：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMaxAmt<%="0"+i %>Temp" missingMessage="请输入单笔最大金额" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).maxAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">日成功交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="updatePayRiskUserLimitDaySucTimes<%="0"+i %>" name="daySucTimes<%="0"+i %>" missingMessage="请输入日成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(i).daySucTimes%>"/>次</td>
			                        <td align="right">日成功交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDaySucAmt<%="0"+i %>Temp" missingMessage="请输入日成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).daySucAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">月成功交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="updatePayRiskUserLimitMonthSucTimes<%="0"+i %>" name="monthSucTimes<%="0"+i %>" missingMessage="请输入月成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(i).monthSucTimes%>"/>次</td>
			                        <td align="right">月成功交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMonthSucAmt<%="0"+i %>Temp" missingMessage="请输入月成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).monthSucAmt)/100d) %>"/>元</td>
			                    </tr>
		                      </table><%
		                      } else {
		                      %><table cellpupdateing="5" width="800">
		                      	<tr><td bgcolor="#CCCCCC" colspan="4"><%=com.PayConstant.TRAN_TYPE_CONSUMES[i] %></td></tr>
		                       	<tr>
			                        <td width="160" align="right">单笔最小金额：</td>
			                        <td width="200"><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMinAmt<%="0"+i %>Temp" missingMessage="请输入单笔最小金额" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).minAmt)/100d) %>"/>元</td>
			                        <td width="120" align="right">单笔最大金额：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMaxAmt<%="0"+i %>Temp" missingMessage="请输入单笔最大金额" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).maxAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">日交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="updatePayRiskUserLimitDayTimes<%="0"+i %>" name="dayTimes<%="0"+i %>" missingMessage="请输入日交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(i).dayTimes%>"/>次</td>
			                        <td align="right">日成功交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="updatePayRiskUserLimitDaySucTimes<%="0"+i %>" name="daySucTimes<%="0"+i %>" missingMessage="请输入日成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="日交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(i).daySucTimes%>"/>次</td>
			                    </tr>
			                    <tr>
			                        <td align="right">日交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDayAmt<%="0"+i %>Temp" missingMessage="请输入日交易限额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).dayAmt)/100d) %>"/>元</td>
			                        <td align="right">日成功交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDaySucAmt<%="0"+i %>Temp" missingMessage="请输入日成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).daySucAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">月交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="updatePayRiskUserLimitMonthTimes<%="0"+i %>" name="monthTimes<%="0"+i %>" missingMessage="请输入月交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(i).monthTimes%>"/>次</td>
			                        <td align="right">月成功交易次数上限：</td>
			                        <td><input class="easyui-numberbox" type="text" max="1000000000" id="updatePayRiskUserLimitMonthSucTimes<%="0"+i %>" name="monthSucTimes<%="0"+i %>" missingMessage="请输入月成功交易次数上限" data-options="required:true" validType="length[1,10]" invalidMessage="月交易次数上限必填" value="<%= payRiskUserLimit.subLimitList.get(i).monthSucTimes%>"/>次</td>
			                    </tr>
			                    <tr>
			                        <td align="right">月交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMonthAmt<%="0"+i %>Temp" missingMessage="请输入月交易限额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).monthAmt)/100d) %>"/>元</td>
			                        <td align="right">月成功交易额上限：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitMonthSucAmt<%="0"+i %>Temp" missingMessage="请输入月成功交易额上限" data-options="required:true" precision="2" max="99999999999" value="<%=String.format("%.2f", ((double)payRiskUserLimit.subLimitList.get(i).monthSucAmt)/100d) %>"/>元</td>
			                    </tr>
		                      </table><%
		                      	}
		                      } %>
	                       	</td>
	                    </tr>
	                    <%
			            for(int i=0; i<com.PayConstant.TRAN_TYPE_CONSUMES.length; i++){ %>
			            <input type="hidden" name="minAmt<%="0"+i %>" id="updatePayRiskUserLimitMinAmt<%="0"+i %>" value="<%=payRiskUserLimit.subLimitList.get(i).minAmt %>">
						<input type="hidden" name="maxAmt<%="0"+i %>" id="updatePayRiskUserLimitMaxAmt<%="0"+i %>" value="<%=payRiskUserLimit.subLimitList.get(i).maxAmt %>">
						<input type="hidden" name="dayAmt<%="0"+i %>" id="updatePayRiskUserLimitDayAmt<%="0"+i %>" value="<%=payRiskUserLimit.subLimitList.get(i).dayAmt %>">
						<input type="hidden" name="daySucAmt<%="0"+i %>" id="updatePayRiskUserLimitDaySucAmt<%="0"+i %>" value="<%=payRiskUserLimit.subLimitList.get(i).daySucAmt %>">
						<input type="hidden" name="monthAmt<%="0"+i %>" id="updatePayRiskUserLimitMonthAmt<%="0"+i %>" value="<%=payRiskUserLimit.subLimitList.get(i).monthAmt %>">
						<input type="hidden" name="monthSucAmt<%="0"+i %>" id="updatePayRiskUserLimitMonthSucAmt<%="0"+i %>" value="<%=payRiskUserLimit.subLimitList.get(i).monthSucAmt %>">	
			            <%} %>
	                </c:if>
	                <!-- 交易类型为卡限额 -->
	                <c:if test="${payRiskUserLimitUpdate.tranType eq '7' }">
	                    <tr id="tranTypeLimitCardForUpdate">
	                        <td colspan="4">
		                      <table cellpupdateing="5" width="100%">
		                       	<tr>
			                        <td width="160" align="right">信用卡单卡单笔最高限额：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitCrebitCardOnceAmtTemp" missingMessage="请输入限额" data-options="required:true" precision="2" max="99999999999" value="<%= String.format("%.2f", ((double)payRiskUserLimit.crebitCardOnceAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">信用卡单卡日累计最高限额：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitCrebitCardDayAmtTemp" missingMessage="请输入限额" data-options="required:true" precision="2" max="99999999999" value="<%= String.format("%.2f", ((double)payRiskUserLimit.crebitCardDayAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">信用卡两次卡交易时间间隔：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitCrebitCardTxnInterval" missingMessage="请输时间间隔" data-options="required:true" name="crebitCardTxnInterval" validType="length[1,10]" value="<%= payRiskUserLimit.crebitCardTxnInterval %>"/>秒（-1为无限制）</td>
			                    </tr>
			                    <tr>
			                        <td align="right">借记卡单卡单笔最高限额：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDebitCardOnceAmtTemp" missingMessage="请输入限额" data-options="required:true" precision="2" max="99999999999" value="<%= String.format("%.2f", ((double)payRiskUserLimit.debitCardOnceAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">借记卡单卡日累计最高限额：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDebitCardDayAmtTemp" missingMessage="请输入限额" data-options="required:true" precision="2" max="99999999999" value="<%= String.format("%.2f", ((double)payRiskUserLimit.debitCardDayAmt)/100d) %>"/>元</td>
			                    </tr>
			                    <tr>
			                        <td align="right">借记卡两次卡交易时间间隔：</td>
			                        <td><input class="easyui-numberbox" type="text" id="updatePayRiskUserLimitDebitCardTxnInterval" missingMessage="请输入时间间隔" data-options="required:true"  name="debitCardTxnInterval" validType="length[1,10]" value="<%= payRiskUserLimit.debitCardTxnInterval %>"/>秒（-1为无限制）</td>
			                    </tr>
		                      </table>
	                       	</td>
	                    </tr>
	                    <input type="hidden" name="crebitCardOnceAmt" id="updatePayRiskUserLimitCrebitCardOnceAmt" value="<%= payRiskUserLimit.crebitCardOnceAmt %>">
						<input type="hidden" name="crebitCardDayAmt" id="updatePayRiskUserLimitCrebitCardDayAmt" value="<%= payRiskUserLimit.crebitCardDayAmt %>">
						<input type="hidden" name="debitCardOnceAmt" id="updatePayRiskUserLimitDebitCardOnceAmt" value="<%= payRiskUserLimit.debitCardOnceAmt %>">
						<input type="hidden" name="debitCardDayAmt" id="updatePayRiskUserLimitDebitCardDayAmt" value="<%= payRiskUserLimit.debitCardDayAmt %>">
	                </c:if>
                    <tr>
                        <td width="160" align="right">启用标志：</td>
                        <td colspan="3">
                        	<input type="radio" name="isUse" value="0" <%="0".equals(payRiskUserLimit.isUse)?"checked=\"checked\"":"" %>/> 可用
                        	<input type="radio" name="isUse" value="1" <%="1".equals(payRiskUserLimit.isUse)?"checked=\"checked\"":"" %>/> 不可用
                        </td>
                    </tr>
                    <tr>
                        <td width="160" align="right" valign="top">备注：</td>
                        <td colspan="3">
                        	<input class="easyui-textbox" type="text" name="remark" data-options="multiline:true" value="${payRiskUserLimitUpdate.remark}"
                                style="width:240px;height:70px"/>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;pupdateing:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayRiskUserLimitFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#updatePayRiskUserLimitForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayRiskUserLimitForm').form({
    url:'<%=path %>/updatePayRiskUserLimit.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payRiskUserLimitList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payRiskUserLimit',payRiskUserLimitUpdatePageTitle,payRiskUserLimitListPageTitle,'payRiskUserLimitList','<%=path %>/payRiskUserLimit.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayRiskUserLimitFormSubmit(){
    $('#updatePayRiskUserLimitForm').submit();
}
function initHidenInfo(){
	$("#tranTypeLimitCardForUpdate").css("display","none");
	$("#tranTypeLimitTranForUpdate").css("display","none");
}
$('#updatePayRiskUserLimitMinAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitMinAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskUserLimitMaxAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitMaxAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskUserLimitDayAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitDayAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskUserLimitDaySucAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitDaySucAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskUserLimitMonthAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitMonthAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskUserLimitMonthSucAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitMonthSucAmt").value=Math.ceil(parseFloat(value)*100);
	}
});


<%for(int i=0; i<com.PayConstant.TRAN_TYPE_CONSUMES.length; i++){%>
	$('#updatePayRiskUserLimitMinAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("updatePayRiskUserLimitMinAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#updatePayRiskUserLimitMaxAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("updatePayRiskUserLimitMaxAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#updatePayRiskUserLimitDayAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("updatePayRiskUserLimitDayAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#updatePayRiskUserLimitDaySucAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("updatePayRiskUserLimitDaySucAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#updatePayRiskUserLimitMonthAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("updatePayRiskUserLimitMonthAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	$('#updatePayRiskUserLimitMonthSucAmt<%="0"+i %>Temp').numberbox({
		onChange:function(value){
			if(value.length>0)
			document.getElementById("updatePayRiskUserLimitMonthSucAmt<%="0"+i %>").value=Math.ceil(parseFloat(value)*100);
		}
	});
	<%}%>

$('#updatePayRiskUserLimitCrebitCardOnceAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitCrebitCardOnceAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskUserLimitCrebitCardDayAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitCrebitCardDayAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskUserLimitDebitCardOnceAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitDebitCardOnceAmt").value=Math.ceil(parseFloat(value)*100);
	}
});
$('#updatePayRiskUserLimitDebitCardDayAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0)
		document.getElementById("updatePayRiskUserLimitDebitCardDayAmt").value=Math.ceil(parseFloat(value)*100);
	}
});

var limitTimeFlagVal="${payRiskUserLimitUpdate.limitTimeFlag}";
if(limitTimeFlagVal=="1"){
	$("#payRiskUserLimitTimeStartDate").datebox({required:true});
	$("#payRiskUserLimitTimeEndDate").datebox({required:true});
	$("#timeScopeDivForUserRiskLimitTime").show();
}
if(limitTimeFlagVal=="0"){
	$("#payRiskUserLimitTimeStartDate").datebox({required:false});
	$("#payRiskUserLimitTimeEndDate").datebox({required:false});
	$("#timeScopeDivForUserRiskLimitTime").hide();
}
// 限额生效时间初始化加载处理
//限额生效时间范围选择切换
$("input[type=radio][name=limitTimeFlag]").change(function(){
	if($(this).val()==1){
		$("#payRiskUserLimitTimeStartDate").datebox({required:true});
		$("#payRiskUserLimitTimeEndDate").datebox({required:true});
		$("#timeScopeDivForUserRiskLimitTime").show();
	}else{
		$("#payRiskUserLimitTimeStartDate").datebox({required:false});
		$("#payRiskUserLimitTimeEndDate").datebox({required:false});
		$("#timeScopeDivForUserRiskLimitTime").hide();
	}
});
</script>
