<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebRole"%>
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
List roleList = (List)request.getAttribute("roleList");
%>
<script type="text/javascript">
$(document).ready(function(){

});
</script>
  <div id="addSysUserPanel" class="easyui-window" title="添加用户"
	data-options="closed:true,minimizable:false,collapsible:false,maximizable:false,resizable:false,iconCls:'icon-add',modal:true" 
  	style="width:322px;padding-top:15px;padding-left:35px;padding-bottom:15px;">
      <form id="addSysUserForm" method="post">
          <table cellpadding="5">
              <tr>
                  <td>登录名:</td>
                  <td><input class="easyui-textbox" type="text" id="sysUserId" name="sysUserId" style="width:160px;"
					 missingMessage="请输入登录名" validType="checkJwebUserId[0]" data-options="required:true"></input></td>
              </tr>
              <tr>
                  <td>密码:</td>
                  <td><input id="sysUserPassword" name="sysUserPassword" style="width:160px;"
                   validType="checkOldPassword[]" class="easyui-textbox" required="true" type="password" value=""></input></td>
              </tr>
              <tr>
                  <td>密码确认:</td>
                  <td><input id="reSysUserPassword" name="reSysUserPassword" style="width:160px;"
				validType="equalTo['#sysUserPassword']" class="easyui-textbox" required="true" type="password" value=""></input></td>
              </tr>
              <tr>
                  <td>姓名:</td>
                  <td><input class="easyui-textbox" type="text" id="sysUserName" name="sysUserName" style="width:160px;"
                  validType="length[1,20]" invalidMessage="姓名为1-20个字符" data-options="required:true"></input></td>
              </tr>
              <tr>
                  <td>手机号:</td>
                  <td><input class="easyui-numberbox" type="text" id="sysUserTel" name="sysUserTel"  missingMessage="请输入手机号" style="width:160px;"
                   data-options="required:true" validType="length[11,11]" invalidMessage="请输入11为手机号码"></input></td>
              </tr>
              <tr>
                  <td>邮箱:</td>
                  <td><input class="easyui-textbox" type="text"  id="sysUserEmail" name="sysUserEmail" style="width:160px;"
				data-options="validType:'email'" invalidMessage="请正确输入邮箱"></input></input></td>
              </tr>
              <tr>
                  <td>角色:</td>
                  <td>
					<input class="easyui-combobox" id="sysUserRole" name="sysUserRole" missingMessage="请选择角色" style="width:160px;"
						data-options="valueField:'id',textField:'name',required:true" validType="inputExistInCombobox['sysUserRole']"/>
                   </td>
               </tr>
               <tr>
                   <td>备注:</td>
                   <td><input class="easyui-textbox" id="sysUserRemark" name="sysUserRemark" data-options="multiline:true" style="width:160px;height:60px"></input></td>
               </tr>
               <tr>
                   <td></td>
                   <td><a href="javascript:addSysUserFormSubmit()" class="easyui-linkbutton" style="width:160px;" iconCls="icon-ok">保存</a></td>
               </tr>
           </table>
       </form>
   </div>
   <div id="modifySysUserPanel" class="easyui-window" title="修改用户"
	data-options="closed:true,minimizable:false,collapsible:false,maximizable:false,resizable:false,iconCls:'icon-edit',modal:true" 
  	style="width:322px;padding-top:15px;padding-left:35px;padding-bottom:15px;">
      <form id="modifySysUserForm" method="post">
      	<input type="hidden" id="sysUserIdModify" name="sysUserIdModify" value=""/>
          <table cellpadding="5">
              <tr>
                  <td>登录名:</td>
                  <td><span id="sysUserIdModify1"></span></td>
              </tr>
              <tr>
                  <td>姓名:</td>
                  <td><input class="easyui-textbox" type="text" id="sysUserNameModify" name="sysUserNameModify" 
                  validType="length[1,20]" invalidMessage="姓名为1-20个字符" data-options="required:true"></input></td>
              </tr>
              <tr>
                  <td>手机号:</td>
                  <td><input class="easyui-numberbox" type="text" id="sysUserTelModify" name="sysUserTelModify"  missingMessage="请输入手机号"
                   data-options="required:true" validType="length[11,11]" invalidMessage="请输入11为手机号码"></input></td>
              </tr>
              <tr>
                  <td>邮箱:</td>
                  <td><input class="easyui-textbox" type="text"  id="sysUserEmailModify" name="sysUserEmailModify"
				data-options="validType:'email'" invalidMessage="请正确输入邮箱"></input></input></td>
              </tr>
              <tr>
                  <td>角色:</td>
                  <td>
					<input class="easyui-combobox" id="sysUserRoleModify" name="sysUserRoleModify" missingMessage="请选择角色"
						data-options="valueField:'id',textField:'name',required:true,url:'<%=path %>/sysRoleCombobox.htm'" 
						validType="inputExistInCombobox['sysUserRoleModify']"/>
                   </td>
               </tr>
               <tr>
                   <td>备注:</td>
                   <td><input class="easyui-textbox" id="sysUserRemarkModify" name="sysUserRemarkModify" data-options="multiline:true" style="height:60px"></input></td>
               </tr>
               <tr>
                   <td></td>
                   <td><a href="javascript:modifySysUserForm()" class="easyui-linkbutton" style="width:100%;" iconCls="icon-ok">修改</a></td>
               </tr>
           </table>
       </form>
   </div>
	<table id="sysUserList" class="easyui-datagrid" style="width:100%;height:100%" rownumbers="true" pagination="true"
		fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:<%=JWebConstant.WEB_RECORD_COUNT_PER_PAGE 
		%>,pageList:<%=JWebConstant.WEB_RECORD_COUNT_SIZE %>,singleSelect:true,url:'<%=path 
			%>/sysUserManager.htm?flag=0',method:'post',toolbar:'#sysUserListToolBar'">
    	<thead>
	        <tr>
	            <th field="id" width="8%" align="left" sortable="true">登录名</th>
	            <th field="name" width="10%" align="left" sortable="true">姓名</th>
	            <th field="roleName" width="10%" align="left">角色</th>
	            <th field="state" width="10%" align="left" data-options="formatter:formatState">状态</th>
	            <th field="tel" width="10%" align="left" sortable="true">电话</th>
	            <th field="email" width="10%" align="left" sortable="true">邮箱</th>
	            <th field="createTime" width="10%" align="left" sortable="true">创建时间</th>
	            <th field="remark" width="20%" align="left">备注</th>
	            <th field="operation" data-options="formatter:formatUserOperator" width="10%" align="left">操作</th>
	        </tr>
    	</thead>
	</table>
	<div id="sysUserListToolBar" style="padding:5px;height:auto">
          登录名 <input class="easyui-textbox" type="text" style="width:100px" 
          id="sysSearchUserId" name="sysSearchUserId"/>
          姓名 <input class="easyui-textbox" type="text" style="width:100px" 
          id="sysSearchUserName"  name="sysSearchUserName"/>
          创建时间<input class="easyui-datebox" style="width:100px"  data-options="editable:false"
          id="sysSearchUserCreateTimeStart"  name="sysSearchUserCreateTimeStart" editable="fasle"/>
          到<input class="easyui-datebox" style="width:100px"  data-options="editable:false"
          id="sysSearchUserCreateTimeEnd"  name="sysSearchUserCreateTimeEnd"/>
          角色<select class="easyui-combobox" panelHeight="auto" 
           	id="sysSearchUserRoleId"  name="sysSearchUserRoleId" style="width:100px" validType="inputExistInCombobox['sysSearchUserRoleId']">
           	<option value="">全部</option>
          		<%if(roleList !=null){
         		for(int i = 0; i<roleList.size(); i++){
         			JWebRole role = (JWebRole)roleList.get(i);
         			%><option value="<%=role.id %>"><%=role.name %></option><%
         		}
         		}%>
         	<option value="-1">无角色</option>
           </select>
          状态
           <select class="easyui-combobox" panelHeight="auto" id="sysSearchUserState" data-options="editable:false" name="sysSearchUserState">
               <option value="">全部</option>
               <option value="0">正常</option>
               <option value="1">禁用</option>
           </select>
           <a href="javascript:searchSysUser()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
           <%if(user.actionMap.get("010101")!= null){ %>
           <a href="javascript:addSysUserPanelOpen()" class="easyui-linkbutton" iconCls="icon-add">添加</a>&nbsp;&nbsp;
           <%} %>
   </div>
<script type="text/javascript">
var roleMap = {};
<%for(int i = 0; roleList !=null && i<roleList.size(); i++){
	JWebRole role = (JWebRole)roleList.get(i);
	%>roleMap['<%=role.id %>'] = '<%=role.name %>';<%
}%>
function searchSysUser(){
      $('#sysUserList').datagrid('load',{  
          id:$('#sysSearchUserId').val(),
          name:$('#sysSearchUserName').val(),
          createTimeStart:$('#sysSearchUserCreateTimeStart').datebox('getValue'),
          createTimeEnd:$('#sysSearchUserCreateTimeEnd').datebox('getValue'),
          roleId:$('#sysSearchUserRoleId').combobox('getValue'),
          state:$('#sysSearchUserState').combobox('getValue')
      });  
}
function addSysUserPanelOpen(){
	$('#addSysUserForm').form('clear');
    $('#addSysUserPanel').window('open');
    $('#sysUserRole').combobox('reload','<%=path %>/sysRoleCombobox.htm');
}
function modifySysUserPanelOpen(){
	$('#modifySysUserForm').form('clear');
    $('#modifySysUserPanel').window('open');
    var node = $('#sysUserList').treegrid('getSelected');
    $('#sysUserRoleModify').combobox({
		onLoadSuccess: function(){
			$(this).combobox('select',node.roleId);
		}
	});
    document.getElementById('sysUserIdModify').value=node.id;
    document.getElementById('sysUserIdModify1').innerHTML=node.id;
   	$('#sysUserNameModify').textbox('setValue',node.name);
   	$('#sysUserTelModify').textbox('setValue',node.tel);
   	$('#sysUserEmailModify').textbox('setValue',node.email);
   	$('#sysUserRoleModify').textbox('setValue',node.role);
   	$('#sysUserRemarkModify').textbox('setValue',node.remark);
}
function formatUserOperator(val,row,index){
	var operationStr = 
		<%if(user.actionMap.get("010102")!=null){%>
		"<a href=\"javascript:modifySysUserPanelOpen()\">修改</a>&nbsp;"+
		<%}if(user.actionMap.get("010104")!=null){%>
			(row.state == "0" 
				? ("<a href=\"javascript:setSysUserState('"+row.id+"','1')\">禁用</a>&nbsp;")
				:("<a href=\"javascript:setSysUserState('"+row.id+"','0')\">解禁</a>&nbsp;")
			)+
		<%}if(user.actionMap.get("010103")!=null){%>
		"<a href=\"javascript:removeSysUser('"+row.id+"')\">删除</a>"+
		<%}%> "";
		/*
	var operationStr = <%=user.actionMap.get("010102")!=null ? "'<a href=\"javascript:modifySysUserPanelOpen()\">修改</a>&nbsp;'":"''"%>+
    	<%=user.actionMap.get("010104")!=null ? "(row.state == '0' ? '<a href=\"javascript:setSysUserState(\\''+row.id+'\\',\\'1\\')\">禁用</a>&nbsp;':'<a href=\"javascript:setSysUserState(\\''+row.id+'\\',\\'0\\')\">解禁</a>&nbsp;')":"''"%>+
    	<%=user.actionMap.get("010103")!=null ? "'<a href=\"javascript:removeSysUser(\\''+row.id+'\\')\">删除</a>'":"''"%>
    	;*/
    return  row.roleId!=undefined && roleMap[row.roleId]==undefined?'':operationStr;
}
function formatState(val,row,index){ 
    return val=='0'?'正常':'禁用';  
}
function setSysUserState(userId,state){
	$.messager.confirm('提示', state=='1'?'确认禁用？':'确认解禁？', function(r){
           if(!r)return;
        try{
       		$.post('<%=path %>/sysUserSetState.htm',
        		{userId:userId,state:state},
        		function(data){
        			$('#sysUserList').datagrid('reload');
					if(data=='<%=JWebConstant.OK %>'){
                      		topCenterMessage('<%=JWebConstant.OK %>',state=='1'?'禁用成功！':'解禁成功！');
                      	} else topCenterMessage('<%=JWebConstant.ERROR %>',data);
                  },
       		'json'
              );
        }catch(e){alert(e);}
       });
}
function removeSysUser(userId){
	$.messager.confirm('提示', '确认删除?', function(r){
       	if(!r)return;
        $("#sysUserList").datagrid("loading");
       	$.post('<%=path %>/sysUserRemove.htm',
        	{userId:userId},
        	function(data){
        		$('#sysUserList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    	topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                   } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
       		'json'
           );
       });
}
$('#addSysUserForm').form({
    url:'<%=path %>/sysUserAdd.htm',
    onSubmit: function(){
    	var addSysUserCheck = $(this).form('validate');
    	if(addSysUserCheck){
    		$('#addSysUserPanel').window('close');
    		$('#sysUserList').datagrid("loading");
    	}
    	return addSysUserCheck;
    },
    success:function(data){
    	$('#sysUserList').datagrid('reload');
   		if(data=='<%=JWebConstant.OK %>'){
           	topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
           } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
$('#modifySysUserForm').form({
    url:'<%=path %>/sysUserModify.htm',
    onSubmit: function(){
    	var modifySysUserCheck = $(this).form('validate');
    	if(modifySysUserCheck){
    		$('#modifySysUserPanel').window('close');
    		$('#sysUserList').datagrid("loading");
    	}
    	return modifySysUserCheck;
    },
    success:function(data){
    	$('#sysUserList').datagrid('reload');
   		if(data=='<%=JWebConstant.OK %>'){
           	topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
           } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addSysUserFormSubmit(){
    $('#addSysUserForm').submit();
}
function modifySysUserForm(){
	$('#modifySysUserForm').submit();
}
$.extend($.fn.textbox.defaults.rules, {
     	checkJwebUserId: {
      		validator: function (value, param) {
       		if(value.length < 6 || value.length > 15){
       			$.fn.textbox.defaults.rules.checkJwebUserId.message = '登录名为6-15位字符';
       			return false;
       		} else {
       			var result = $.ajax({
					url: '<%=path %>/sysUserManager.htm?flag=1',
					data:{userId:value},
					type: 'post',
					dataType: 'json',
					async: false,
					cache: false
				}).responseText;
				if(result == '1')return true;
				$.fn.textbox.defaults.rules.checkJwebUserId.message = '用户名已存在';
				return false; 
       		}
      		},
		message: ''
      }
   });
</script>
