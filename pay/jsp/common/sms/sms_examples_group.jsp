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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pmVVFP82Q3rKTPx2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pmVVFP82Q3rKTPx3"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pmVVFP82Q3rKTPx4"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pmVVFP82Q3rKTPx5"));
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<table id="smsExamplesGroupList" class="easyui-datagrid" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/smsExamplesGroup.htm?flag=0',method:'post',toolbar:'#smsExamplesGroupListToolBar'">
       <thead>
        <tr>
           <th field="name" width="30%" align="left" sortable="true">组名</th>
           <th field="createId" width="30%" align="left" sortable="true">创建人</th>
           <th field="createTime" width="30%" align="left" sortable="true">创建时间</th>
           <th field="operation" data-options="formatter:formatSmsExamplesGroupOperator" width="19%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="smsExamplesGroupListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    组名<input type="text" id="searchSmsExamplesGroupName" name="searchSmsExamplesGroupName" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchSmsExamplesGroupList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addSmsExamplesGroupWindowOpen()" class="easyui-linkbutton" iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<div id="addSmsExamplesGroupWindow" class="easyui-window" title="添加短信样例组" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:400px;height:170px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="addSmsExamplesGroupForm" method="post">
                <table cellpadding="5">
                    <tr><td width="40%">&nbsp;</td><td width="60%">&nbsp;</td></tr>
                    <tr>
                        <td align="right">组名:</td>
                        <td><input class="easyui-textbox" type="text" id="addSmsExamplesGroupName" name="name" missingMessage="请输入组名"
                            validType="length[1,50]" invalidMessage="组名为1-50个字符" data-options="required:true"/></td>
                    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addSmsExamplesGroupFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#addSmsExamplesGroupWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<div id="updateSmsExamplesGroupWindow" class="easyui-window" title="修改短信样例组" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:170px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="updateSmsExamplesGroupForm" method="post">
            	<input type="hidden" id="updateSmsExamplesGroupId" name="id"/>
                <table cellpadding="5">
                  <tr><td width="40%">&nbsp;</td><td width="60%">&nbsp;</td></tr>
                  <tr>
                      <td align="right">组名:</td>
                      <td><input class="easyui-textbox" type="text" id="updateSmsExamplesGroupName" name="name" missingMessage="请输入组名"
                          validType="length[1,50]" invalidMessage="组名为1-50个字符" data-options="required:true"/></td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updateSmsExamplesGroupFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#updateSmsExamplesGroupWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">
function formatSmsExamplesGroupOperator(val,row,index){
     var tmp=
         <%if(actionUpdate != null){//角色判断%>
         "<a href=\"javascript:updateSmsExamplesGroupWindowOpen()\"><%=actionUpdate.name %></a>&nbsp;"+
         <%}%>
         <%if(actionRemove !=null ){//角色判断%>
         "<a href=\"javascript:removeSmsExamplesGroup('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
         <%}%>
         "";
     return tmp;
}
function searchSmsExamplesGroupList(){        
    $('#smsExamplesGroupList').datagrid('load',{  
          name:$('#searchSmsExamplesGroupName').val()
    });  
}
$('#addSmsExamplesGroupForm').form({
    url:'<%=path %>/addSmsExamplesGroup.htm',
    onSubmit: function(){
        var addSmsExamplesGroupCheck=$(this).form('validate');
        if(addSmsExamplesGroupCheck){
        	$('#addSmsExamplesGroupWindow').window('close');
        	$('#smsExamplesGroupList').datagrid('loading');
        }
        return addSmsExamplesGroupCheck;
    },
    success:function(data){
        $('#smsExamplesGroupList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addSmsExamplesGroupFormSubmit(){
    $('#addSmsExamplesGroupForm').submit();
}
function addSmsExamplesGroupWindowOpen(){
    $('#addSmsExamplesGroupForm').form('clear');
    $('#addSmsExamplesGroupWindow').window('open');
}
$('#updateSmsExamplesGroupForm').form({
    url:'<%=path %>/updateSmsExamplesGroup.htm',
    onSubmit: function(){
        var updateSmsExamplesGroupCheck=$(this).form('validate');
        if(updateSmsExamplesGroupCheck){
        	$('#updateSmsExamplesGroupWindow').window('close');
        	$('#smsExamplesGroupList').datagrid('loading');
        }
        return updateSmsExamplesGroupCheck;
    },
    success:function(data){
        $('#smsExamplesGroupList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updateSmsExamplesGroupFormSubmit(){
    $('#updateSmsExamplesGroupForm').submit();
}
function updateSmsExamplesGroupWindowOpen(){
    var updateSmsExamplesGroupForm = $('#updateSmsExamplesGroupForm');
    updateSmsExamplesGroupForm.form('clear');
    $('#updateSmsExamplesGroupWindow').window('open');
    var node = $('#smsExamplesGroupList').treegrid('getSelected');
    document.getElementById('updateSmsExamplesGroupId').value = node.id;
    $('#updateSmsExamplesGroupName').textbox('setValue',node.name);
}
function removeSmsExamplesGroup(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removeSmsExamplesGroup.htm',
            {id:id},
            function(data){
                $('#smsExamplesGroupList').datagrid('reload');
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
