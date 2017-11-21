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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pmVVFP82Q3rKTOZ2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pmVVFP82Q3rKTOZ3"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pmVVFP82Q3rKTOZ4"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pmVVFP82Q3rKTOZ5"));
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<table id="smsExamplesList" class="easyui-datagrid" style="width:100%;height:120%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/smsExamples.htm?flag=0',method:'post',toolbar:'#smsExamplesListToolBar'">
       <thead>
        <tr>
           <th field="groupName" width="10%" align="left" sortable="true">组</th>
           <th field="content" width="80%" align="left" sortable="true">内容</th>
           <th field="operation" data-options="formatter:formatSmsExamplesOperator" width="6%" align="left">操作</th>
           <th field="createId" width="10%" align="left" sortable="true">创建人</th>
           <th field="createTime" width="14%" align="left" sortable="true">创建时间</th>
       </tr>
       </thead>
</table>
<div id="smsExamplesListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    内容<input type="text" id="searchSmsExamplesContent" name="searchSmsExamplesContent" class="easyui-textbox" value=""  style="width:130px"/>
    组<input class="easyui-combobox" id="searchSmsExamplesGroupId" name="searchSmsExamplesGroupId"
		data-options="url:'<%=path %>/getAllSmsExamplesGroup.htm',valueField:'id',textField:'name'"
		validType="inputExistInCombobox['searchSmsExamplesGroupId']"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchSmsExamplesList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addSmsExamplesWindowOpen()" class="easyui-linkbutton" iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<div id="addSmsExamplesWindow" class="easyui-window" title="添加短信样例" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:400px;height:300px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="addSmsExamplesForm" method="post">
                <table cellpadding="5">
                  <tr><td width="30%">&nbsp;</td><td width="70%">&nbsp;</td></tr>
                  <tr>
                      <td align="right" valign="top">内容:</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsExamplesContent" name="content"  missingMessage="请输入内容"
                          validType="length[1,100]" invalidMessage="内容为1-100个字符" data-options="multiline:true,required:true" 
                          style="width:210px;height:100px"/></td>
                  </tr>
                  <tr>
                      <td align="right">组:</td>
                      <td><input class="easyui-combobox" id="selectAddSmsExamplesGroupId" name="groupId"
							data-options="valueField:'id',textField:'name'" validType="inputExistInCombobox['selectAddSmsExamplesGroupId']"/></td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addSmsExamplesFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#addSmsExamplesWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<div id="updateSmsExamplesWindow" class="easyui-window" title="修改SmsExamples" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:300px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="updateSmsExamplesForm" method="post">
            	<input type="hidden" id="updateSmsExamplesId" name="examplesId"/>
                <table cellpadding="5">
                  <tr><td width="30%">&nbsp;</td><td width="70%">&nbsp;</td></tr>
                  <tr>
                      <td align="right" valign="top">内容:</td>
                      <td><input class="easyui-textbox" type="text" id="updateSmsExamplesContent" name="content"  missingMessage="请输入内容"
                          validType="length[1,100]" invalidMessage="内容为1-100个字符" data-options="multiline:true,required:true" 
                          style="width:210px;height:100px"/></td>
                  </tr>
                  <tr>
                      <td align="right">组:</td>
                      <td><input class="easyui-combobox" id="selectUpdateSmsExamplesGroupId" name="groupId"
							data-options="valueField:'id',textField:'name'" validType="inputExistInCombobox['selectUpdateSmsExamplesGroupId']"/></td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updateSmsExamplesFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#updateSmsExamplesWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">
function formatSmsExamplesOperator(val,row,index){
     var tmp=
         <%if(actionUpdate != null){//角色判断%>
         "<a href=\"javascript:updateSmsExamplesWindowOpen()\"><%=actionUpdate.name %></a>&nbsp;"+
         <%}%>
         <%if(actionRemove !=null ){//角色判断%>
         "<a href=\"javascript:removeSmsExamples('"+row.examplesId+"')\"><%=actionRemove.name %></a>&nbsp;"+
         <%}%>
         "";
     return tmp;
}
function searchSmsExamplesList(){        
    $('#smsExamplesList').datagrid('load',{  
          content:$('#searchSmsExamplesContent').val(),
          groupId:$('#searchSmsExamplesGroupId').combobox('getValue')
    });  
}
$('#addSmsExamplesForm').form({
    url:'<%=path %>/addSmsExamples.htm',
    onSubmit: function(){
        var addSmsExamplesCheck=$(this).form('validate');
        if(addSmsExamplesCheck){
        	$('#addSmsExamplesWindow').window('close');
        	$('#smsExamplesList').datagrid('loading');
        }
        return addSmsExamplesCheck;
    },
    success:function(data){
        $('#smsExamplesList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addSmsExamplesFormSubmit(){
    $('#addSmsExamplesForm').submit();
}
function addSmsExamplesWindowOpen(){
    $('#addSmsExamplesForm').form('clear');
    $('#selectAddSmsExamplesGroupId').combobox('reload','<%=path %>/getAllSmsExamplesGroup.htm');
    $('#addSmsExamplesWindow').window('open');
}
$('#updateSmsExamplesForm').form({
    url:'<%=path %>/updateSmsExamples.htm',
    onSubmit: function(){
        var updateSmsExamplesCheck=$(this).form('validate');
        if(updateSmsExamplesCheck){
        	$('#updateSmsExamplesWindow').window('close');
        	$('#smsExamplesList').datagrid('loading');
        }
        return updateSmsExamplesCheck;
    },
    success:function(data){
        $('#smsExamplesList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updateSmsExamplesFormSubmit(){
    $('#updateSmsExamplesForm').submit();
}
function updateSmsExamplesWindowOpen(){
    var updateSmsExamplesForm = $('#updateSmsExamplesForm');
    updateSmsExamplesForm.form('clear');
    $('#updateSmsExamplesWindow').window('open');
    var node = $('#smsExamplesList').treegrid('getSelected');
    document.getElementById('updateSmsExamplesId').value = node.examplesId;
    $('#updateSmsExamplesContent').textbox('setValue',node.content);
    $('#selectUpdateSmsExamplesGroupId').combobox({
    	url:'<%=path %>/getAllSmsExamplesGroup.htm',
		onLoadSuccess: function(){
			$(this).combobox('select',node.groupId);
		}
	});
}
function removeSmsExamples(examplesId){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removeSmsExamples.htm',
            {examplesId:examplesId},
            function(data){
                $('#smsExamplesList').datagrid('reload');
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
