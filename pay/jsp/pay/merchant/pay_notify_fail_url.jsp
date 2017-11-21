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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("qkkSlyU2Q3rKTP31"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("ACTION_ADD_ID"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("ACTION_DETAIL_ID"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("ACTION_REMOVE_ID"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("ACTION_UPDATE_ID"));
%>
<script type="text/javascript">
var payNotifyFailUrlListPageTitle="通知失败URL";
var payNotifyFailUrlAddPageTitle="添加payNotifyFailUrl";
var payNotifyFailUrlDetailPageTitle="payNotifyFailUrl详情";
var payNotifyFailUrlUpdatePageTitle="修改payNotifyFailUrl";
$(document).ready(function(){});
</script>
<table id="payNotifyFailUrlList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payNotifyFailUrl.htm?flag=0',method:'post',toolbar:'#payNotifyFailUrlListToolBar'">
       <thead>
        <tr>
           <th field="id" width="16%" align="left" sortable="true">支付订单号</th>
           <th field="merchantNo" width="16%" align="left" sortable="true">商户号</th>
           <th field="url" width="16%" align="left" sortable="true">失败URL</th>
           <th field="errorMsg" width="16%" align="left" sortable="true">失败信息</th>
           <th field="createTime" width="16%" align="left" sortable="true">创建时间</th>
           <!-- <th field="operation" data-options="formatter:formatPayNotifyFailUrlOperator" width="19%" align="left">操作</th> -->
       </tr>
       </thead>
</table>
<div id="payNotifyFailUrlListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    商户号<input type="text" id="searchPayNotifyFailUrlMerchantNo" name="searchPayNotifyFailUrlMerchantNo" class="easyui-textbox" value=""  style="width:130px"/>
    创建时间
    <input type="text" id="searchPayNotifyFailUrlCreateTime" name="searchPayNotifyFailUrlCreateTime" class="easyui-datebox" value=""  style="width:130px"/>
    ~
    &nbsp;结束时间
    <input type="text" id="searchPayNotifyFailUrlCreateEndTime" name="searchPayNotifyFailUrlCreateEndTime" class="easyui-datebox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayNotifyFailUrlList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayNotifyFailUrlPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payNotifyFailUrlList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayNotifyFailUrlOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayNotifyFailUrlPageOpen('"+row.id+"')\"><%=actionDetail.name %></a>&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayNotifyFailUrlPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayNotifyFailUrl('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayNotifyFailUrlList(){
    $('#payNotifyFailUrlList').datagrid('load',{
          merchantNo:$('#searchPayNotifyFailUrlMerchantNo').val(),
          createTime:$('#searchPayNotifyFailUrlCreateTime').datebox('getValue'),
          createEndTime:$('#searchPayNotifyFailUrlCreateEndTime').datebox('getValue')
    });  
}
function addPayNotifyFailUrlPageOpen(){
    openTab('addPayNotifyFailUrlPage',payNotifyFailUrlAddPageTitle,'<%=path %>/jsp/pay/merchant/pay_notify_fail_url_add.jsp');
}
function detailPayNotifyFailUrlPageOpen(id){
    openTab('detailPayNotifyFailUrlPage',payNotifyFailUrlDetailPageTitle,'<%=path %>/detailPayNotifyFailUrl.htm?id='+id);
}
function updatePayNotifyFailUrlPageOpen(id){
    openTab('updatePayNotifyFailUrlPage',payNotifyFailUrlUpdatePageTitle,'<%=path %>/updatePayNotifyFailUrl.htm?flag=show&id='+id);
}
function removePayNotifyFailUrl(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayNotifyFailUrl.htm',
            {id:id},
            function(data){
                $('#payNotifyFailUrlList').datagrid('reload');
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
