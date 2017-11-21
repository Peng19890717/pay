<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%
String path = request.getContextPath();
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <table id="selectTestList" class="easyui-datagrid" style="width:100%;height:100%" rownumbers="true" pagination="true"
   fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,multipleSelect:true,checkOnSelect:true,url:'<%=path 
       %>/smsUser.htm?flag=0',method:'post',toolbar:'#selectTestListToolBar'">
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
         <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:selectTest()" style="width:80px">选择</a>
         <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
             onclick="$('#selectTestWindow').window('close')" style="width:80px">取消</a>
     </div>
 </div>
 <div id="selectTestListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
  姓名<input type="text" id="searchTestName" name="searchTestName" class="easyui-textbox" value=""  style="width:130px"/>
  手机号<input type="text" id="searchTestTel" name="searchTestTel" class="easyui-textbox" value=""  style="width:130px"/>
  组<input class="easyui-combobox" id="searchTestGroupId" name="searchTestGroupId"
data-options="url:'<%=path %>/getAllSmsUserGroup.htm',valueField:'id',textField:'name'"
validType="inputExistInCombobox['searchSmsUserGroupId']"/>
 <a href="javascript:searchSelectTestList()" class="easyui-linkbutton" iconCls="icon-search">查询</a>
</div>
