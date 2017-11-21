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
<%@page import="com.pay.merchant.dao.PayMerchantChannelRelation"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
java.util.Map map = (java.util.Map) request.getAttribute("channelMerchantMap");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="updatePayMerchantChannelRelationForm" method="post">
          <input type="hidden" name="merno" value="<%=request.getAttribute("merno") %>">
          <table style="margin-left:40px;margin-top:20px">
	    	<tr>
	    		<td height="30" width="100" align="right">商户号：</td>
	    		<td height="30" width="500"><%=request.getAttribute("merno") %></td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">网银借记卡：</td>
	    		<td height="30" width="500">
	    		<!-- 交易类型 1消费 2充值 3结算 4退款 5提现  6转账 7消费B2C借记卡 8消费B2C信用卡 9消费B2B 10微信扫码 11快捷借记卡 12快捷贷记卡 13代理分润 14代理费率 15代收 16代付 17支付宝扫码 18微信WAP -->
	    			<select name="tranType_7">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("7,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
							<%  %>
						<%}%>
					</select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">网银贷记卡：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_8">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("8,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">对公：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_9">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("9,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">快捷借记卡：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_11">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("11,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">快捷贷记卡：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_12">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("12,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">微信扫码：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_10">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("10,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">代收：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_15">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("15,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
				</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">代付：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_16">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("16,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">支付宝扫码：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_17">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("17,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">微信WAP：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_18">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("18,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
	    		</td>
	    	</tr>
	    	<tr>
	    		<td height="30" width="100" align="right">QQ扫码：</td>
	    		<td height="30" width="500">
	    			<select name="tranType_27">
		    			<option value="">请选择</option>
			    		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
							PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
							<option value="<%=channel.bankCode %>" <%= map.get("27,"+channel.bankCode) != null?"selected":"" %>><%=channel.bankName %></option>
						<%}%>
					</select>
	    		</td>
	    	</tr>
    </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayMerchantChannelRelationFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
        		onclick="$('#mainTabs').tabs('close',payMerchantChannelRelationUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayMerchantChannelRelationForm').form({
    url:'<%=path %>/updatePayChannelForMerchant.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payMerchantChannelRelation',payMerchantChannelRelationUpdatePageTitle,payMerchantChannelRelationListPageTitle,'payMerchantChannelRelationList','<%=path %>/payMerchantChannelRelation.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayMerchantChannelRelationFormSubmit(){
    $('#updatePayMerchantChannelRelationForm').submit();
}
</script>
