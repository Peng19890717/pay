<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pmTFf4E2Q3rKTPf1"));
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <table id="selectSmsUserList" class="easyui-datagrid" style="width:100%;height:100%" rownumbers="true" pagination="true"
   fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,multipleSelect:true,checkOnSelect:true,url:'<%=path 
       %>/smsUser.htm?flag=0',method:'post',toolbar:'#selectSmsUserListToolBar'">
       <thead>
        <tr>
           <th data-options="field:'check',checkbox:true"></th>
           <th field="name" width="15%" align="left" sortable="true">姓名</th>
           <th field="tel" width="15%" align="left" sortable="true">手机号</th>
           <th field="groupName" width="15%" align="left">组</th>
           <th field="remark" width="25%" align="left">备注</th>
           <th field="createId" width="12%" align="left">创建人</th>
           <th field="createTime" width="17%" align="left" sortable="true">创建时间</th>
       </tr>
       </thead>
</table>
     </div>
     <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
         <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:selectSmsUser()" style="width:80px">选择</a>
         <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
             onclick="$('#selectSmsUserWindow').window('close')" style="width:80px">取消</a>
     </div>
 </div>
 <div id="selectSmsUserListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
  姓名<input type="text" id="searchSmsUserName" name="searchSmsUserName" class="easyui-textbox" value=""  style="width:130px"/>
  手机号<input type="text" id="searchSmsUserTel" name="searchSmsUserTel" class="easyui-textbox" value=""  style="width:130px"/>
  组<input class="easyui-combobox" id="searchSmsUserGroupId" name="searchSmsUserGroupId"
data-options="url:'<%=path %>/getAllSmsUserGroup.htm',valueField:'id',textField:'name'"
validType="inputExistInCombobox['searchSmsUserGroupId']"/>
  <%if(actionSearch != null){//角色判断%>
 <a href="javascript:searchSelectSmsUserList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
 <%} %>
</div>
<script type="text/javascript">
function searchSelectSmsUserList(){
    $('#selectSmsUserList').datagrid('load',{  
          name:$('#searchSmsUserName').val(),
          tel:$('#searchSmsUserTel').val(),
          groupId:$('#searchSmsUserGroupId').combobox('getValue')
    });
}
function selectSmsUser(){
	var checkedItems = $('#selectSmsUserList').datagrid('getChecked');
	var tels = $('#<%=request.getParameter("receiveObjId") %>').val();
	$.each(checkedItems, function(index, item){
		tels = tels + ',' + item.tel;
	});
	if(tels.indexOf(',')==0)tels = tels.substring(1,tels.length);
	$('#<%=request.getParameter("receiveObjId") %>').textbox('setValue',tels);
	$('#selectSmsUserWindow').window('close');
}
</script>
