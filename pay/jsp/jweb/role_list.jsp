<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="util.JWebConstant"%>
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
<div id="addSysRolePanel" class="easyui-window" title="添加角色"
	data-options="closed:true,minimizable:false,collapsible:false,maximizable:false,resizable:false,iconCls:'icon-add',modal:true" 
  	style="width:310px;padding-top:15px;padding-left:35px;padding-bottom:15px;">
      <form id="addSysRoleForm" method="post">
      	  <input type="hidden" id="sysRoleIdModify" name="sysRoleIdModify" value=""/>
      	  <input type="hidden" id="sysRoleFlagModify" name="sysRoleFlagModify" value=""/>
          <table cellpadding="5">
              <tr>
                  <td>角色名:</td>
                  <td><input class="easyui-textbox" type="text" id="sysRoleName" name="sysRoleName"
                  	 validType="length[1,15]" invalidMessage="请输入1-15为字符" data-options="required:true"></input></td>
              </tr>
            <tr>
                <td>备注:</td>
                <td><input class="easyui-textbox" id="sysRoleRemark" name="sysRoleRemark" data-options="multiline:true" style="height:60px"></input></td>
            </tr>
            <tr>
                <td></td>
                <td><a href="javascript:addSysRoleFormSubmit()" 
                	class="easyui-linkbutton"  style="width:100%;" iconCls="icon-save">保存</a></td>
            </tr>
        </table>
    </form>
</div>
<div class="easyui-layout" data-options="border:false,fit:true">
    <div data-options="border:false,region:'west',split:true,collapsible:false" title="权限列表" style="width:60%">
	    <table id="sysRoleList" class="easyui-datagrid" style="width:100%;height:100%;"
			rownumbers="true" pagination="true"
				data-options="border:true,collapsible:true,autoRowHeight:true,pageSize:<%=
					JWebConstant.WEB_RECORD_COUNT_PER_PAGE %>,pageList:<%=JWebConstant.WEB_RECORD_COUNT_SIZE %>,singleSelect:true,url:'<%=path 
					%>/sysRoleManager.htm?flag=0',method:'post',toolbar:'#sysRoleListToolBar'">
		    <thead>
		        <tr>
		        	<th field="id" width="20%" align="left">角色编号</th>
		            <th field="name" width="29%" align="left">角色名</th>
		            <th field="createId" width="20%" align="left">操作人</th>
		            <th field="createTime" width="15%" align="left" >操作时间</th>
		            <th field="operator" data-options="formatter:formatRoleOperator" width="15%" align="left">操作</th>
		        </tr>
		    </thead>
		</table>
		<div id="sysRoleListToolBar" style="padding:5px;height:auto,fit:true">
	           角色名 <input class="easyui-textbox" type="text" style="width:100px" id="sysRoleNameSearch" name="sysRoleNameSearch"/>
	            <a href="javascript:searchSysRole()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
	            <%if(user.actionMap.get("010201")!=null){ %>
	            <a href="javascript:addSysRolePanelOpen('0')" class="easyui-linkbutton" iconCls="icon-add">添加</a>&nbsp;&nbsp;
	            <%} %>
	    </div>
    </div>
    <div data-options="border:false,region:'center',split:true,tools:'#sysRoleActionTools'"  title="功能列表" style="width:40%">
	    <div id="sysRoleCheckListPanel" class="easyui-panel" style="width:100%;height:100%;" data-options="border:true">
	    </div>
	    <div id="sysRoleActionTools">
	        <a href="javascript:getSysRoleCheckedList()" class="icon-save"></a>
	    </div>
    </div>
</div>
<script type="text/javascript">
function getSysRoleActionDetail(id){
   $('#sysRoleCheckListPanel').panel('refresh','<%=path %>/jsp/jweb/action_check_list.jsp?roleId='+id);
}
function getSysRoleCheckedList(){
    var nodes = $('#sysRoleCheckList').tree('getChecked');
    var s = '';
    for(var i=0; i<nodes.length; i++){
        if (s != '') s += ',';
        s += nodes[i].id;
    }
    var node = $('#sysRoleList').treegrid('getSelected');
    $.post('<%=path %>/sysRoleAddAction.htm',
 		{actionIds:s,roleId:node.id},
 		function(data){
	       	if(data=='<%=JWebConstant.OK %>'){
        		topCenterMessage('<%=JWebConstant.OK %>','保存成功！');
           	} else topCenterMessage('<%=JWebConstant.ERROR %>',data);
       	},
		'json'
     );
}
function searchSysRole(){
    $('#sysRoleList').datagrid('load',{  
        	name:$('#sysRoleNameSearch').val()
        }
    );  
}
$('#addSysRoleForm').form({
    url:'<%=path %>/sysRoleAdd.htm',
    onSubmit: function(){
    	var addSysRoleCheck = $(this).form('validate');
    	if(addSysRoleCheck){
    		$('#addSysRolePanel').window('close');
    		$('#sysRoleList').datagrid('loading');
    	}
    	return addSysRoleCheck;
    },
    success:function(data){
    	$('#sysRoleList').datagrid('reload');
   		if(data=='<%=JWebConstant.OK %>'){
        	topCenterMessage('<%=JWebConstant.OK %>','处理成功！');
           } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addSysRolePanelOpen(flag){
	$('#addSysRoleForm').form('clear');
    $('#addSysRolePanel').window('open');
    document.getElementById('sysRoleFlagModify').value=flag;
    //修改
    if(flag == '1'){
    	$('#addSysRolePanel').panel({title:'角色修改',iconCls:'icon-edit'});
    	var node = $('#sysRoleList').treegrid('getSelected');
    	document.getElementById('sysRoleIdModify').value=node.id;
    	$('#sysRoleName').textbox('setValue',node.name);
    	$('#sysRoleRemark').textbox('setValue',node.remark);
    } else if(flag == '0')$('#addSysRolePanel').panel({title:'角色添加',iconCls:'icon-add'});
}
function formatRoleOperator(val,row,index){
    return row.id=='<%=JWebConstant.ROOT_DIR_ID %>' 
    	? <%=user.actionMap.get("010204")!=null ? "'<a href=\"javascript:getSysRoleActionDetail(\\''+row.id+'\\')\">功能</a>&nbsp;'" : "''"%> 
    	: <%=user.actionMap.get("010202")!=null ? "'<a href=\"javascript:addSysRolePanelOpen(\\'1\\')\">修改</a>&nbsp;'" : "''"%>+
    	<%=user.actionMap.get("010204")!=null ? "'<a href=\"javascript:getSysRoleActionDetail(\\''+row.id+'\\')\">功能</a>&nbsp;'" : "''"%>+
    	<%=user.actionMap.get("010203")!=null ? "'<a href=\"javascript:removeSysRole(\\''+row.id+'\\')\">删除</a>'" : "''"%>;  
}
function addSysRoleFormSubmit(){
	$('#addSysRoleForm').submit();
}
function removeSysRole(roleId){
	$.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    try{
    	$('#sysRoleList').datagrid('loading');
   		$.post('<%=path %>/sysRoleRemove.htm',
    		{roleId:roleId},
    		function(data){
    			$('#sysRoleList').datagrid('reload');
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