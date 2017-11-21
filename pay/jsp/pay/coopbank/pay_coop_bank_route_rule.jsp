<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.pay.business.dao.PayBusinessParameterDAO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%@page import="com.PayConstant"%>
<%@page import="java.util.List"%>
<%@page import="com.pay.coopbank.dao.PayCoopBankRouteRule"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
List<PayCoopBankRouteRule> routeRuleList = (List<PayCoopBankRouteRule>) request.getAttribute("routeRuleList");
Long rChannelRouteRuleJJCount = (Long) request.getAttribute("rChannelRouteRuleJJCount");
Long rChannelRouteRuleJJAmt = (Long) request.getAttribute("rChannelRouteRuleJJAmt");
Long rChannelRouteRuleDJCount = (Long) request.getAttribute("rChannelRouteRuleDJCount");
Long rChannelRouteRuleDJAmt = (Long) request.getAttribute("rChannelRouteRuleDJAmt");
Long rChannelRouteRulePubCount = (Long) request.getAttribute("rChannelRouteRulePubCount");
Long rChannelRouteRulePubAmt = (Long) request.getAttribute("rChannelRouteRulePubAmt");
Long rChannelRouteRuleQuickJJCount = (Long) request.getAttribute("rChannelRouteRuleQuickJJCount");
Long rChannelRouteRuleQuickJJAmt = (Long) request.getAttribute("rChannelRouteRuleQuickJJAmt");
Long rChannelRouteRuleQuickDJCount = (Long) request.getAttribute("rChannelRouteRuleQuickDJCount");
Long rChannelRouteRuleQuickDJAmt = (Long) request.getAttribute("rChannelRouteRuleQuickDJAmt");
Long rChannelRouteRuleReceiveCount = (Long) request.getAttribute("rChannelRouteRuleReceiveCount");
Long rChannelRouteRuleReceiveAmt = (Long) request.getAttribute("rChannelRouteRuleReceiveAmt");
Long rChannelRouteRulePayCount = (Long) request.getAttribute("rChannelRouteRulePayCount");
Long rChannelRouteRulePayAmt = (Long) request.getAttribute("rChannelRouteRulePayAmt");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
    	<form id="adjustChannelRouteRuleForm" method="post">
    	<table style="border:solid #cccccc; border-width:1px 0px 0px 1px;" width="100%">
			<tr>
				<td colspan="2" style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" align="center"></td>
				<td style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" align="center">网银借记卡</td>
				<td style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" align="center">网银贷记卡</td>
				<td style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" align="center">对公</td>
				<td style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" align="center">快捷借记卡</td>
				<td style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" align="center">快捷贷记卡</td>
				<td style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" align="center">代收</td>
				<td style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" align="center">代付</td>
			</tr>
			<tr>
				<td colspan="2" style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" align="center">
				交易平均分配规则</td>
				<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
					前<input class="easyui-numberbox" type="text" id="channelRouteRuleJJCount" name="channelRouteRuleJJCount" max="99999" invalidMessage="99999笔，每笔之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleJJCount==null?"0":rChannelRouteRuleJJCount %>" />笔，每笔
					<input class="easyui-numberbox" type="text" id="channelRouteRuleJJAmt" name="channelRouteRuleJJAmt" max="999999" invalidMessage="999999元之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleJJAmt==null?"0":rChannelRouteRuleJJAmt %>"/>元之内
				</td>
				<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
					前<input class="easyui-numberbox" type="text" id="channelRouteRuleDJCount" name="channelRouteRuleDJCount" max="99999" invalidMessage="99999笔，每笔之内"  
						data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleDJCount==null?"0":rChannelRouteRuleDJCount %>" />笔，每笔
					<input class="easyui-numberbox" type="text" id="channelRouteRuleDJAmt" name="channelRouteRuleDJAmt" max="999999" invalidMessage="999999元之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleDJAmt==null?"0":rChannelRouteRuleDJAmt %>"/>元之内</td>
				<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
					前<input class="easyui-numberbox" type="text" id="channelRouteRulePubCount" name="channelRouteRulePubCount" max="99999" invalidMessage="99999笔，每笔之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRulePubCount==null?"0":rChannelRouteRulePubCount %>"/>笔，每笔
					<input class="easyui-numberbox" type="text" id="channelRouteRulePubAmt" name="channelRouteRulePubAmt" max="999999" invalidMessage="999999元之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRulePubAmt==null?"0":rChannelRouteRulePubAmt %>"/>元之内</td>
				<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;"> 
					前<input class="easyui-numberbox" type="text" id="rChannelRouteRuleQuickJJCount" name="channelRouteRuleQuickJJCount" max="99999" invalidMessage="99999笔，每笔之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleQuickJJCount==null?"0":rChannelRouteRuleQuickJJCount %>"/>笔，每笔
					<input class="easyui-numberbox" type="text" id="channelRouteRuleQuickJJAmt" name="channelRouteRuleQuickJJAmt" max="999999" invalidMessage="999999元之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleQuickJJAmt==null?"0":rChannelRouteRuleQuickJJAmt %>"/>元之内</td>
				<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
					前<input class="easyui-numberbox" type="text" id="channelRouteRuleQuickDJCount" name="channelRouteRuleQuickDJCount" max="99999" invalidMessage="99999笔，每笔之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleQuickDJCount==null?"0":rChannelRouteRuleQuickDJCount %>"/>笔，每笔
					<input class="easyui-numberbox" type="text" id="channelRouteRuleQuickDJAmt" name="channelRouteRuleQuickDJAmt" max="999999" invalidMessage="999999元之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleQuickDJAmt==null?"0":rChannelRouteRuleQuickDJAmt %>"/>元之内</td>
				<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
					前<input class="easyui-numberbox" type="text" id="channelRouteRuleReceiveCount" name="channelRouteRuleReceiveCount" max="99999" invalidMessage="99999笔，每笔之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleReceiveCount==null?"0":rChannelRouteRuleReceiveCount %>"/>笔，每笔
					<input class="easyui-numberbox" type="text" id="channelRouteRuleReceiveAmt" name="channelRouteRuleReceiveAmt" max="999999" invalidMessage="999999元之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRuleReceiveAmt==null?"0":rChannelRouteRuleReceiveAmt %>"/>元之内</td>
				<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
					前<input class="easyui-numberbox" type="text" id="channelRouteRulePayCount" name="channelRouteRulePayCount" max="99999" invalidMessage="99999笔，每笔之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRulePayCount==null?"0":rChannelRouteRulePayCount %>"/>笔，每笔
					<input class="easyui-numberbox" type="text" id="channelRouteRulePayAmt" name="channelRouteRulePayAmt" max="999999" invalidMessage="999999元之内"  
					data-options="required:true" style="width:50px" value="<%= rChannelRouteRulePayAmt==null?"0":rChannelRouteRulePayAmt %>"/>元之内</td>
			</tr>
			<tr>
				<td style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;" rowspan="<%=PayCoopBankService.CHANNEL_LIST.size()+1 %>" align="center" valign="middle">
					优先值规则<br/>(<%=PayCoopBankService.CHANNEL_LIST.size() %>个渠道)<br/>
				</td>
			</tr>
			<%
			for(int i=0; i<PayCoopBankService.CHANNEL_LIST.size(); i++){
				PayCoopBank channel = PayCoopBankService.CHANNEL_LIST.get(i);
				%><tr>
					<td style="border:solid #cccccc;background-color:#f4f4f4; border-width:0px 1px 1px 0px;"><%=channel.bankName %></td>
					<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
						<input class="easyui-numberbox" type="text" 
						value="<%= request.getAttribute("channelRouteRuleJJWeight_"+channel.bankCode)==null?"0":request.getAttribute("channelRouteRuleJJWeight_"+channel.bankCode) %>" 
						id="channelRouteRuleJJWeight_<%=channel.bankCode %>" name="channelRouteRuleJJWeight_<%=channel.bankCode %>" 
							max="999" invalidMessage="999之内"  data-options="required:true" style="width:40px"/></td>
					<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
						<input class="easyui-numberbox" type="text" 
						value="<%= request.getAttribute("channelRouteRuleDJWeight_"+channel.bankCode)==null?"0":request.getAttribute("channelRouteRuleDJWeight_"+channel.bankCode) %>" 
						id="channelRouteRuleDJWeight_<%=channel.bankCode %>" name="channelRouteRuleDJWeight_<%=channel.bankCode %>" 
							max="999" invalidMessage="999之内"  data-options="required:true" style="width:40px"/></td>
					<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
						<input class="easyui-numberbox" type="text" 
						value="<%= request.getAttribute("channelRouteRulePubWeight_"+channel.bankCode)==null?"0":request.getAttribute("channelRouteRulePubWeight_"+channel.bankCode) %>" 
						id="channelRouteRulePubWeight_<%=channel.bankCode %>" name="channelRouteRulePubWeight_<%=channel.bankCode %>" 
							max="999" invalidMessage="999之内"  data-options="required:true" style="width:40px"/></td>
					<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
						<input class="easyui-numberbox" type="text" 
						value="<%= request.getAttribute("channelRouteRuleQuickJJWeight_"+channel.bankCode)==null?"0":request.getAttribute("channelRouteRuleQuickJJWeight_"+channel.bankCode) %>" 
						id="channelRouteRuleQuickJJWeight_<%=channel.bankCode %>" name="channelRouteRuleQuickJJWeight_<%=channel.bankCode %>" 
							max="999" invalidMessage="999之内"  data-options="required:true" style="width:40px"/></td>
					<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
						<input class="easyui-numberbox" type="text" 
						value="<%= request.getAttribute("channelRouteRuleQuickDJWeight_"+channel.bankCode)==null?"0":request.getAttribute("channelRouteRuleQuickDJWeight_"+channel.bankCode) %>" 
						id="channelRouteRuleQuickDJWeight_<%=channel.bankCode %>" name="channelRouteRuleQuickDJWeight_<%=channel.bankCode %>" 
							max="999" invalidMessage="999之内"  data-options="required:true" style="width:40px"/></td>
					<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
						<input class="easyui-numberbox" type="text" 
						value="<%= request.getAttribute("channelRouteRuleReceiveWeight_"+channel.bankCode)==null?"0":request.getAttribute("channelRouteRuleReceiveWeight_"+channel.bankCode) %>" 
						id="channelRouteRuleReceiveWeight_<%=channel.bankCode %>" name="channelRouteRuleReceiveWeight_<%=channel.bankCode %>" 
							max="999" invalidMessage="999之内"  data-options="required:true" style="width:40px"/></td>
					<td style="border:solid #cccccc; border-width:0px 1px 1px 0px;">
						<input class="easyui-numberbox" type="text" 
						value="<%= request.getAttribute("channelRouteRulePayWeight_"+channel.bankCode)==null?"0":request.getAttribute("channelRouteRulePayWeight_"+channel.bankCode) %>" 
						id="channelRouteRulePayWeight_<%=channel.bankCode %>" name="channelRouteRulePayWeight_<%=channel.bankCode %>" 
							max="999" invalidMessage="999之内"  data-options="required:true" style="width:40px"/></td>
				</tr>
			<%} %>
		</table>
		交易平均分配规则启用<input type="checkbox" id="adjustChannelRouteRuleStart" 
					name="adjustChannelRouteRuleStart" <%= "1".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROUTE_RULE_DIVISION_FLAG"))  ? "checked" : "" %>/>&nbsp;&nbsp;&nbsp;&nbsp;
		优先规则设置<select name="channelRouteRulePriority">
						<option value="0,1" <%= "0,1".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROUTE_RULE_PRIORITY"))  ? "selected" : "" %>>手续费</option>
						<option value="1,0" <%= "1,0".equals(PayConstant.PAY_CONFIG.get("CHANNEL_ROUTE_RULE_PRIORITY"))  ? "selected" : "" %>>优先值</option>
					</select>
		</form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:adjustChannelRouteRuleFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',adjustChannelRouteRuleTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#adjustChannelRouteRuleForm').form({
    url:'<%=path %>/adjustChannelRouteRule.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payCoopBankList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','调整渠道规则成功！');
            closeTabFreshList('payCoopBank',adjustChannelRouteRuleTitle,payCoopBankListPageTitle,'payCoopBankList','<%=path %>/payCoopBank.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function adjustChannelRouteRuleFormSubmit(){
	if($("#adjustChannelRouteRuleStart").is(':checked')){
		$("#adjustChannelRouteRuleStart").val("1");
	}
    $('#adjustChannelRouteRuleForm').submit();
}
</script>
