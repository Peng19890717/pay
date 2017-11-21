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
        <table id="selectSmsExamplesList" class="easyui-datagrid" style="width:110%;height:100%" rownumbers="true" pagination="true"
   fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,checkOnSelect:true,singleSelect:true,url:'<%=path 
       %>/smsExamples.htm?flag=0',method:'post',toolbar:'#selectSmsExamplesListToolBar'">
	       <thead>
	        <tr>
	           <th data-options="field:'check',checkbox:true"></th>
	           <th field="groupName" width="10%" align="left" sortable="true">组</th>
	           <th field="content" width="100%" align="left" sortable="true">内容</th>
	       </tr>
	       </thead>
	</table>
      </div>
      <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
          <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:selectSmsExamples()" style="width:80px">选择</a>
          <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
              onclick="$('#selectSmsExamplesWindow').window('close')" style="width:80px">取消</a>
      </div>
  </div>
  <div id="selectSmsExamplesListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	内容<input type="text" id="selectSmsExamplesContent" name="searchSmsExamplesContent" class="easyui-textbox" value=""  style="width:130px"/>
	组<input class="easyui-combobox" id="selectSmsExamplesGroupId" name="selectSmsExamplesGroupId"
		data-options="url:'<%=path %>/getAllSmsExamplesGroup.htm',valueField:'id',textField:'name'"
		validType="inputExistInCombobox['selectSmsExamplesGroupId']"/>
  	<%if(actionSearch != null){//角色判断%>
 	<a href="javascript:searchSelectSmsExamplesList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
 	<%} %>
</div>
<script type="text/javascript">
function searchSelectSmsExamplesList(){        
    $('#selectSmsExamplesList').datagrid('load',{  
          content:$('#selectSmsExamplesContent').val(),
          groupId:$('#selectSmsExamplesGroupId').combobox('getValue')
    });  
}
function selectSmsExamples(){
	var checkedItems = $('#selectSmsExamplesList').datagrid('getChecked');
	var content = '';
	$.each(checkedItems, function(index, item){
		content = item.content;
	});
	$('#<%=request.getParameter("receiveObjId") %>').textbox('setValue',content);
	$('#selectSmsExamplesWindow').window('close')
}
</script>
