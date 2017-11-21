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
// JWebAction actionSearch = ((JWebAction)user.actionMap.get("ACTION_SEARCH_ID"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pU7KJmy2Q3rKTOI3"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pU7KJmy2Q3rKTOI5"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pU7KJmy2Q3rKTOI6"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pU7KJmy2Q3rKTOI4"));
%>
<script type="text/javascript">
var payBankListPageTitle="银行列表";
var payBankAddPageTitle="添加银行";
var payBankDetailPageTitle="银行详情";
var payBankUpdatePageTitle="修改银行";
$(document).ready(function(){});
</script>
<table id="payBankList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payBankList.htm?flag=0',method:'post',toolbar:'#payBankListToolBar'">
       <thead>
        <tr>
           <th field="bankCode" width="30%" align="left" sortable="true">银行编码</th>
           <th field="bankName" width="45%" align="left" sortable="true">银行名称</th>
           <th field="operation" data-options="formatter:formatPayBankOperator" width="24%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payBankListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    <%-- <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayBankList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %> --%>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayBankPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payBankList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayBankOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayBankPageOpen('"+row.id+"')\"><%=actionDetail.name %></a>&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayBankPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayBank('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayBankList(){
    $('#payBankList').datagrid('load',{
    });  
}
function addPayBankPageOpen(){
    openTab('addPayBankPage',payBankAddPageTitle,'<%=path %>/jsp/pay/bank/pay_bank_add.jsp');
}
function detailPayBankPageOpen(id){
    openTab('detailPayBankPage',payBankDetailPageTitle,'<%=path %>/detailPayBank.htm?id='+id);
}
function updatePayBankPageOpen(id){
    openTab('updatePayBankPage',payBankUpdatePageTitle,'<%=path %>/updatePayBank.htm?flag=show&id='+id);
}
function removePayBank(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayBank.htm',
            {id:id},
            function(data){
                $('#payBankList').datagrid('reload');
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
