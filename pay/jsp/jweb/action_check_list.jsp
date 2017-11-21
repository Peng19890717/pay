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
String roleId = request.getParameter("roleId");
%>
<ul id="sysRoleCheckList" class="easyui-tree" data-options="url:'<%=path 
	%>/sysRoleCheckActionList.htm?roleId=<%=roleId %>',method:'post',animate:true,checkbox:true"></ul>
	