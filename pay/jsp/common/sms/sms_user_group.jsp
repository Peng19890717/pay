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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pmKR7do2Q3rKTOK5"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pmKR7do2Q3rKTOK6"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pmKR7do2Q3rKTOK8"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pmKR7do2Q3rKTOK7"));
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<table id="smsUserGroupList" class="easyui-datagrid" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/smsUserGroup.htm?flag=0',method:'post',toolbar:'#smsUserGroupListToolBar'">
       <thead>
        <tr>
           <th field="name" width="25%" align="left" sortable="true">组名</th>
           <th field="createId" width="25%" align="left">创建人</th>
           <th field="createTime" width="25%" align="left" sortable="true">创建时间</th>
           <th field="operation" data-options="formatter:formatSmsUserGroupOperator" width="25%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="smsUserGroupListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    组名<input type="text" id="searchSmsUserGroupName" name="searchSmsUserGroupName" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchSmsUserGroupList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#addSmsUserGroupForm').form('clear');$('#addSmsUserGroupWindow').window('open')"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<div id="addSmsUserGroupWindow" class="easyui-window" title="添加组" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:400px;height:170px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="addSmsUserGroupForm" method="post">
                <table cellpadding="5">
                    <tr><td width="40%">&nbsp;</td><td width="60%">&nbsp;</td></tr>
                  <tr>
                      <td align="right">组名:</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsUserGroupName" name="name"  missingMessage="请输入组名"
                          validType="length[1,20]" invalidMessage="name为1-20个字符" data-options="required:true"/></td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addSmsUserGroupFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#addSmsUserGroupWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<div id="updateSmsUserGroupWindow" class="easyui-window" title="修改组" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:170px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="updateSmsUserGroupForm" method="post">
            	<input type="hidden" id="updateSmsUserGroupId" name="id"/>
                <table cellpadding="5">
                    <tr><td width="40%">&nbsp;</td><td width="60%">&nbsp;</td></tr>
                  <tr>
                      <td align="right">组名:</td>
                      <td><input class="easyui-textbox" type="text" id="updateSmsUserGroupName" name="name" 
                          validType="length[1,20]" invalidMessage="name为1-20个字符" data-options="required:true"/></td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updateSmsUserGroupFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#updateSmsUserGroupWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">
function formatSmsUserGroupOperator(val,row,index){
     var tmp=
         <%if(actionUpdate != null){//角色判断%>
         "<a href=\"javascript:updateSmsUserGroupWindowOpen()\"><%=actionUpdate.name %></a>&nbsp;"+
         <%}%>
         <%if(actionRemove !=null ){//角色判断%>
         "<a href=\"javascript:removeSmsUserGroup('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
         <%}%>
         "";
     return tmp;
}
function searchSmsUserGroupList(){        
    $('#smsUserGroupList').datagrid('load',{  
          name:$('#searchSmsUserGroupName').val()
    });  
}
$('#addSmsUserGroupForm').form({
    url:'<%=path %>/addSmsUserGroup.htm',
    onSubmit: function(){
        var addSmsUserGroupCheck=$(this).form('validate');
        if(addSmsUserGroupCheck){
        	$('#addSmsUserGroupWindow').window('close');
        	$('#smsUserGroupList').datagrid('loading');
        }
        return addSmsUserGroupCheck;
    },
    success:function(data){
        $('#smsUserGroupList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addSmsUserGroupFormSubmit(){
    $('#addSmsUserGroupForm').submit();
}
$('#updateSmsUserGroupForm').form({
    url:'<%=path %>/updateSmsUserGroup.htm',
    onSubmit: function(){
        var updateSmsUserGroupCheck=$(this).form('validate');
        if(updateSmsUserGroupCheck){
        	$('#updateSmsUserGroupWindow').window('close');
        	$('#smsUserGroupList').datagrid('loading');
        }
        return updateSmsUserGroupCheck;
    },
    success:function(data){
        $('#smsUserGroupList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updateSmsUserGroupFormSubmit(){
    $('#updateSmsUserGroupForm').submit();
}
function updateSmsUserGroupWindowOpen(){
    var updateSmsUserGroupForm = $('#updateSmsUserGroupForm');
    updateSmsUserGroupForm.form('clear');
    $('#updateSmsUserGroupWindow').window('open');
    var node = $('#smsUserGroupList').treegrid('getSelected');
    document.getElementById('updateSmsUserGroupId').value = node.id;
    $('#updateSmsUserGroupName').textbox('setValue',node.name);
}
function removeSmsUserGroup(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removeSmsUserGroup.htm',
            {id:id},
            function(data){
                $('#smsUserGroupList').datagrid('reload');
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
