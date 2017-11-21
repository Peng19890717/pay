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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J95EV5XF3JLXPT2PW2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("ACTION_ADD_ID"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("ACTION_DETAIL_ID"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("ACTION_REMOVE_ID"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("ACTION_UPDATE_ID"));
String frozenId = request.getParameter("frozenId");
%>
<script type="text/javascript">
var payChannelUnfrozenDetailListPageTitle="渠道解冻明细";
//var payChannelUnfrozenDetailAddPageTitle="添加解冻明细";
//var payChannelUnfrozenDetailDetailPageTitle="解冻详情";
//var payChannelUnfrozenDetailUpdatePageTitle="修改解冻明细";
$(document).ready(function(){});
</script>
<table id="payChannelUnfrozenDetailList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payChannelUnfrozenDetail.htm?flag=0',method:'post',toolbar:'#payChannelUnfrozenDetailListToolBar'">
       <thead>
        <tr>
           <th field="id" width="14%" align="left" sortable="true">解冻记录编号</th>
           <th field="amt" width="14%" align="left" sortable="true">解冻金额</th>
           <th field="frozenId" width="14%" align="left" sortable="true">冻结记录编号</th>
           <th field="createTime" width="14%" align="left" sortable="true">解冻时间</th>
           <th field="remark" width="14%" align="left" sortable="true">备注</th>
           <th field="optId" width="14%" align="left" sortable="true">解冻人员</th>
           <th field="operation" data-options="formatter:formatPayChannelUnfrozenDetailOperator" width="15%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payChannelUnfrozenDetailListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    冻结记录编号<input type="text" id="searchPayChannelUnfrozenDetailFrozenId" name="searchPayChannelUnfrozenDetailFrozenId" value="<%=frozenId==null?"":frozenId %>" class="easyui-textbox"  style="width:130px"/>
    解冻时间 <input type="text" id="searchPayChannelUnfrozenDetailCreateTime" name="searchPayChannelUnfrozenDetailCreateTime" class="easyui-datebox" value=""  style="width:100px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayChannelUnfrozenDetailList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayChannelUnfrozenDetailPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payChannelUnfrozenDetailList').datagrid({
	onBeforeLoad: function(param){
		param.flag='0';
		<%if(frozenId!=null){%>
        	param.frozenId=document.getElementById("searchPayChannelUnfrozenDetailFrozenId").value;
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayChannelUnfrozenDetailOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayChannelUnfrozenDetailPageOpen('"+row.id+"')\"><%=actionDetail.name %></a>&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayChannelUnfrozenDetailPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayChannelUnfrozenDetail('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayChannelUnfrozenDetailList(){
    $('#payChannelUnfrozenDetailList').datagrid('load',{
          frozenId:$('#searchPayChannelUnfrozenDetailFrozenId').val(),
          createTime:$('#searchPayChannelUnfrozenDetailCreateTime').datebox('getValue')
    });  
}
function addPayChannelUnfrozenDetailPageOpen(){
    openTab('addPayChannelUnfrozenDetailPage',payChannelUnfrozenDetailAddPageTitle,'<%=path %>/jsp/pay/coopbank/pay_channel_unfrozen_detail_add.jsp');
}
function detailPayChannelUnfrozenDetailPageOpen(id){
    openTab('detailPayChannelUnfrozenDetailPage',payChannelUnfrozenDetailDetailPageTitle,'<%=path %>/detailPayChannelUnfrozenDetail.htm?id='+id);
}
function updatePayChannelUnfrozenDetailPageOpen(id){
    openTab('updatePayChannelUnfrozenDetailPage',payChannelUnfrozenDetailUpdatePageTitle,'<%=path %>/updatePayChannelUnfrozenDetail.htm?flag=show&id='+id);
}
function removePayChannelUnfrozenDetail(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayChannelUnfrozenDetail.htm',
            {id:id},
            function(data){
                $('#payChannelUnfrozenDetailList').datagrid('reload');
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
