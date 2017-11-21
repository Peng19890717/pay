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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pmKR7do2Q3rKTOK4"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pmKR7do2Q3rKTOK1"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pmKR7do2Q3rKTOK3"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pmKR7do2Q3rKTOK2"));
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<table id="smsUserList" class="easyui-datagrid" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/smsUser.htm?flag=0',method:'post',toolbar:'#smsUserListToolBar'">
       <thead>
        <tr>
           <th field="name" width="14%" align="left" sortable="true">姓名</th>
           <th field="tel" width="14%" align="left" sortable="true">手机号</th>
           <th field="groupName" width="14%" align="left">组</th>
           <th field="remark" width="14%" align="left">备注</th>
           <th field="createId" width="14%" align="left">创建人</th>
           <th field="createTime" width="14%" align="left" sortable="true">创建时间</th>
           <th field="operation" data-options="formatter:formatSmsUserOperator" width="15%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="smsUserListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    姓名<input type="text" id="searchSmsUserName" name="searchSmsUserName" class="easyui-textbox" value=""  style="width:130px"/>
    手机号<input type="text" id="searchSmsUserTel" name="searchSmsUserTel" class="easyui-textbox" value=""  style="width:130px"/>
    组<input class="easyui-combobox" id="searchSmsUserGroupId" name="searchSmsUserGroupId"
		data-options="url:'<%=path %>/getAllSmsUserGroup.htm',valueField:'id',textField:'name'"
		validType="inputExistInCombobox['searchSmsUserGroupId']"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchSmsUserList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addSmsUserWindowOpen()" class="easyui-linkbutton" iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<div id="addSmsUserWindow" class="easyui-window" title="添加短信用户" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:400px;height:400px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="addSmsUserForm" method="post">
                <table cellpadding="5">
                    <tr><td width="40%">&nbsp;</td><td width="60%">&nbsp;</td></tr>
                  <tr>
                      <td align="right">姓名:</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsUserName" name="name" missingMessage="请输入姓名"
                          validType="length[1,20]" invalidMessage="姓名为1-20个字符" data-options="required:true"/></td>
                  </tr>
                  <tr>
                      <td align="right">手机号:</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsUserTel" name="tel" missingMessage="请输入手机号"
                          validType="length[11,11]" invalidMessage="手机号为11个数字" data-options="required:true"/></td>
                  </tr>
                  <tr>
                      <td align="right">组:</td>
                      <td><input class="easyui-combobox" id="selectAddSmsUserGroupId" name="groupId" missingMessage="请选择组"
						  data-options="valueField:'id',textField:'name',required:true" 
						  validType="inputExistInCombobox['selectAddSmsUserGroupId']"/></td>
                  </tr>
                  <tr>
                      <td align="right" valign="top">备注:</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsUserRemark" name="remark"
                          validType="length[1,100]" invalidMessage="备注为1-100个字符" data-options="multiline:true" style="height:70px"/></td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addSmsUserFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#addSmsUserWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<div id="updateSmsUserWindow" class="easyui-window" title="修改短信用户" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:400px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="updateSmsUserForm" method="post">
		      	<input type="hidden" id="updateSmsUserId" name="userId" value=""/>
		          <table cellpadding="5">
		          	  <tr><td width="40%">&nbsp;</td><td width="60%">&nbsp;</td></tr>
		              <tr>
		                  <td align="right">姓名:</td>
                          <td><input class="easyui-textbox" type="text" id="updateSmsUserName" name="name" missingMessage="请输入姓名"
                          validType="length[1,20]" invalidMessage="姓名为1-20个字符" data-options="required:true"/></td>
		              </tr>
		              <tr>
		                  <td align="right">手机号:</td>
                      	  <td><input class="easyui-textbox" type="text" id="updateSmsUserTel" name="tel" missingMessage="请输入手机号"
                          validType="length[1,20]" invalidMessage="手机号为1-20个字符" data-options="required:true"/></td>
		              </tr>
		              <tr>
		                  <td align="right">组:</td>
                      	  <td><input class="easyui-combobox" id="selectUpdateSmsUserGroupId" name="groupId" missingMessage="请选择组"
							  data-options="valueField:'id',textField:'name',required:true,url:'<%=path %>/getAllSmsUserGroup.htm'" 
							  validType="inputExistInCombobox['selectUpdateSmsUserGroupId']"/>
		                   </td>
		               </tr>
		               <tr>
		                  <td align="right" valign="top">备注:</td>
                      	  <td><input class="easyui-textbox" type="text" id="updateSmsUserRemark" name="remark"
                          	validType="length[1,100]" invalidMessage="备注为1-100个字符" data-options="multiline:true" style="height:70px"/></td>
		               </tr>
		           </table>
		       </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updateSmsUserFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#updateSmsUserWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">
$('#addSmsUserForm').form({
    url:'<%=path %>/addSmsUser.htm',
    onSubmit: function(){
        var addSmsUserCheck=$(this).form('validate');
        if(addSmsUserCheck){
        	$('#addSmsUserWindow').window('close');
        	$('#smsUserList').datagrid('loading');
        }
        return addSmsUserCheck;
    },
    success:function(data){
    	$('#smsUserList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
$('#updateSmsUserForm').form({
    url:'<%=path %>/updateSmsUser.htm',
    onSubmit: function(){
        var updateSmsUserCheck=$(this).form('validate');
        if(updateSmsUserCheck){
        	$('#updateSmsUserWindow').window('close');
        	$('#smsUserList').datagrid('loading');
        }
        return updateSmsUserCheck;
    },
    success:function(data){
        $('#smsUserList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function formatSmsUserOperator(val,row,index){
     var tmp=
         <%if(actionUpdate != null){//角色判断%>
         "<a href=\"javascript:updateSmsUserWindowOpen()\"><%=actionUpdate.name %></a>&nbsp;"+
         <%}%>
         <%if(actionRemove !=null ){//角色判断%>
         "<a href=\"javascript:removeSmsUser('"+row.userId+"')\"><%=actionRemove.name %></a>&nbsp;"+
         <%}%>
         "";
     return tmp;
}
function searchSmsUserList(){
    $('#smsUserList').datagrid('load',{  
          name:$('#searchSmsUserName').val(),
          tel:$('#searchSmsUserTel').val(),
          groupId:$('#searchSmsUserGroupId').combobox('getValue')
    });  
}
function addSmsUserWindowOpen(){
	$('#addSmsUserForm').form('clear');
	$('#selectAddSmsUserGroupId').combobox('reload','<%=path %>/getAllSmsUserGroup.htm');
	$('#addSmsUserWindow').window('open');
}
function addSmsUserFormSubmit(){
    $('#addSmsUserForm').submit();
}
function updateSmsUserFormSubmit(){
    $('#updateSmsUserForm').submit();
}
function updateSmsUserWindowOpen(){
    $('#updateSmsUserForm').form('clear');
    $('#updateSmsUserWindow').window('open');
    var node = $('#smsUserList').treegrid('getSelected');
    document.getElementById('updateSmsUserId').value = node.userId;
    $('#updateSmsUserName').textbox('setValue',node.name);
    $('#updateSmsUserTel').textbox('setValue',node.tel);
    $('#selectUpdateSmsUserGroupId').combobox({
		onLoadSuccess: function(){
			$(this).combobox('select',node.groupId);
		}
	});
    $('#updateSmsUserRemark').textbox('setValue',node.remark);
}
function removeSmsUser(userId){
    $.messager.confirm('提示', '确认删除?', function(r){
        if(!r)return;
	    $('#testUserList').datagrid('loading');
	    try{
	        $.post('<%=path %>/removeSmsUser.htm',
	            {userId:userId},
	            function(data){
	                $('#smsUserList').datagrid('reload');
	                if(data=='<%=JWebConstant.OK %>'){
	                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
	                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	            },
	           'text'
	        );
	    }catch(e){alert(e);}
    });
}
</script>
