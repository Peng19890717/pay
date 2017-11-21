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
%>
<script type="text/javascript">
var myMerchantPageTitle="我的商户";
$(document).ready(function(){});
</script>
<table id="paySalemanMerchantsList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/myMerchants.htm?flag=0',method:'post',toolbar:'#paySalemanMerchantsListToolBar'">
       <thead>
        <tr>
           <th field="merNo" width="50%" align="left" sortable="true">商户号</th>
           <th field="mName" width="50%" align="left" sortable="true">商户名</th>
       </tr>
       </thead>
</table>
<script type="text/javascript">
$('#paySalemanMerchantsList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
</script>
