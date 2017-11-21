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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("ACTION_SEARCH_ID"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pHttbBS2Q3rKTQm1"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pHttbBS2Q3rKTQ31"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pHttbBS2Q3rKTPs1"));
JWebAction actionExecute = ((JWebAction)user.actionMap.get("pHtOsfG2Q3rKTOR1"));
%>
<script type="text/javascript">
var payBusinessParameterListPageTitle="业务参数管理";
var payBusinessParameterAddPageTitle="添加业务参数";
var payBusinessParameterExecutePageTitle="执行业务参数";
var payBusinessParameterUpdatePageTitle="修改业务参数";
var payBusinessParameterDeletePageTitle="删除业务参数";
$(document).ready(function(){});
</script>
<table id="payBusinessParameterList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payBusinessParameter.htm?flag=0',method:'post',toolbar:'#payBusinessParameterListToolBar'">
       <thead>
        <tr>
           <th field="name" width="30%" align="left" sortable="true">变量名</th>
           <th field="value" width="30%" align="left" sortable="true">变量值</th>
           <th field="remark" width="30%" align="left" sortable="true">备注</th>
           <th field="operation" data-options="formatter:formatPayBusinessParameterOperator" width="10%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payBusinessParameterListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayBusinessParameterList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayBusinessParameterPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
    <%if(actionExecute != null){//角色判断%>
    <a href="javascript:executePayBusinessParameter()" class="easyui-linkbutton"
        iconCls="icon-config"><%=actionExecute.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payBusinessParameterList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayBusinessParameterOperator(val,row,index){
     var tmp=
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayBusinessParameterPageOpen('"+row.name+"')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;&nbsp;&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayBusinessParameter('"+row.name+"')\"><%=actionRemove.name %></a>"+
        <%}%>
        "";
     return tmp;
}
function searchPayBusinessParameterList(){
    $('#payBusinessParameterList').datagrid('load',{
    });  
}
function addPayBusinessParameterPageOpen(){
    openTab('addPayBusinessParameterPage',payBusinessParameterAddPageTitle,'<%=path %>/jsp/pay/business/pay_business_parameter_add.jsp');
}
function executePayBusinessParameter(){
    $.messager.confirm('提示', '确认执行?', function(r){
	    if(!r)return;
	    try{
	   		$.post('<%=path %>/executeParameterManager.htm',
	    		function(data){
				    if(data=='<%=JWebConstant.OK %>'){
                     	topCenterMessage('<%=JWebConstant.OK %>','执行成功！');
                    } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	            },
	   		'text'
	        );
	    }catch(e){alert(e);}
	   });
}
function updatePayBusinessParameterPageOpen(name){
    openTab('updatePayBusinessParameterPage',payBusinessParameterUpdatePageTitle,'<%=path %>/updatePayBusinessParameter.htm?flag=show&name='+name);
}
function removePayBusinessParameter(name){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayBusinessParameter.htm',
            {name:name},
            function(data){
                $('#payBusinessParameterList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
</script>
