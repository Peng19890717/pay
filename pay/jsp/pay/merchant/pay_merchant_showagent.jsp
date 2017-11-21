<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%
	String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
	return;
}
%>
<table id="merchantAgentList" class="easyui-treegrid" style="width:100%;height:100%"
	fit="true" rownumbers="true" data-options="border:false,collapsible:true,autoRowHeight:true,singleSelect:true,
            animate:true, url: '<%=request.getContextPath() %>/getPayMerchantAgentJson.htm?custId=<%=request.getParameter("custId") %>',method:'post',
            idField:'id',treeField:'name',lines:true">
    <thead>
        <tr>
            <th data-options="field:'name'" style="width:40%">商户名称</th>
            <th data-options="field:'id'" style="width:20%">商户编号</th>
            <th data-options="field:'merStatus'" style="width:20%" formatter="merStatusFormatter">商户状态</th>
            <th data-options="field:'checkStatus'" style="width:20%" formatter="checkStatus_formatter">审核状态</th>
        </tr>
    </thead>
</table>
<script type="text/javascript">
// 商户状态
function merStatusFormatter(data,row,index){
	if(data == "0") {
		return "开启";
	}else if(data == "1") {
		return "关闭";
	} else {
		return "";
	}
}
// 商户审核状态
function checkStatus_formatter(data,row,index){
	if(data=="0") {
		return "未审核";
	} else if(data=="1") {
		return "审核通过"
	} else if(data=="2"){
		return "审核失败";
	}
}
</script>