<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pM4Yusp2Q3rKTOK1"));
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<table id="payBankSupportList" style="width:100%;height:100%" rownumbers="true" pagination="false"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,singleSelect:true,url:'<%=path 
        %>/payBank.htm?flag=0',method:'post'">
       <thead>
        <tr>
           <th field="bankCode" width="20%" align="left" sortable="true">银行编码</th>
           <th field="bankName" width="20%" align="left" sortable="true">银行名称</th>
           <th field="supportedUserType" width="20%" align="left" sortable="true" formatter="format_supportedUserType">直连网银</th>
           <th field="quickUserType" width="20%" align="left" sortable="true" formatter="format_quickUserType">快捷 </th>
           <th field="withdrawUserType" width="20%" align="left" sortable="true" formatter="format_withdrawUserType">提现</th>
       </tr>
       </thead>
</table>
<script type="text/javascript">
$('#payBankSupportList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function format_supportedUserType(data,row,index){
	var str="";
	if(data.indexOf("0")!=-1)str+="借记卡，";
	if(data.indexOf("1")!=-1)str+="贷记卡，";
	if(data.indexOf("4")!=-1)str+="对公账户，";
	return str.length>0?str.substring(0, str.length-1):"";
}
function format_quickUserType(data,row,index){
	var str="";
	if(data.indexOf("0")!=-1)str+="借记卡，";
	if(data.indexOf("1")!=-1)str+="贷记卡，";
	return str.length>0?str.substring(0, str.length-1):"";
}
function format_withdrawUserType(data,row,index){
	if(data.indexOf("0")!=-1)return "支持";
	return "";
}
</script>
