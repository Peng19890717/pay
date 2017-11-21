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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pmNqMCf2Q3rKTOJ1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pmNqMCf2Q3rKTOJ2"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pmNqMCf2Q3rKTOJ4"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pmNqMCf2Q3rKTOJ3"));
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<table id="smsTemplateList" class="easyui-datagrid" style="width:140%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/smsTemplate.htm?flag=0',method:'post',toolbar:'#smsTemplateListToolBar'">
       <thead>
        <tr>
           <th field="name" width="15%" align="left" sortable="true">名称</th>
           <th field="content" width="53%" align="left">内容</th>
           <th field="operation" data-options="formatter:formatSmsTemplateOperator" width="11%" align="left">操作</th>
           <th field="remark" width="26%" align="left">备注</th>
           <th field="modifyId" width="6%" align="left">修改人</th>
           <th field="modifyTime" width="11%" align="left" sortable="true">修改时间</th>
           <th field="createId" width="6%" align="left">创建人</th>
           <th field="createTime" width="11%" align="left" sortable="true">创建时间</th>
       </tr>
       </thead>
</table>
<div id="smsTemplateListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    名称<input type="text" id="searchSmsTemplateName" name="searchSmsTemplateName" class="easyui-textbox" value=""  style="width:130px"/>
    内容<input type="text" id="searchSmsTemplateContent" name="searchSmsTemplateContent" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchSmsTemplateList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#addSmsTemplateForm').form('clear');$('#addSmsTemplateWindow').window('open')"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<div id="addSmsTemplateWindow" class="easyui-window" title="添加短信模板" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:400px;height:400px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="addSmsTemplateForm" method="post">
                <table cellpadding="5">
                    <tr><td width="30%">&nbsp;</td><td width="70%">&nbsp;</td></tr>
                  <tr>
                      <td align="right">名称:</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsTemplateName" name="name" missingMessage="请输入名称"
                          validType="length[1,50]" invalidMessage="名称为1-50个字符" data-options="required:true"/></td>
                  </tr>
                  <tr>
                      <td align="right" valign="top">内容:</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsTemplateContent" name="content"  missingMessage="请输入内容"
                          validType="length[1,100]" invalidMessage="内容为1-100个字符" data-options="multiline:true,required:true" 
                          style="width:210px;height:70px"/></td>
                  </tr>
                  <tr>
                      <td align="right" valign="top">备注:</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsTemplateRemark" name="remark"  missingMessage="请输入备注"
                          validType="length[1,100]" invalidMessage="备注为1-100个字符" data-options="multiline:true" style="width:210px;height:70px"/></td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addSmsTemplateFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#addSmsTemplateWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<div id="updateSmsTemplateWindow" class="easyui-window" title="修改SmsTemplate" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:400px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="updateSmsTemplateForm" method="post">
            	<input type="hidden" id="updateSmsTemplateId" name="id"/>
                <table cellpadding="5">
                    <tr><td width="30%">&nbsp;</td><td width="70%">&nbsp;</td></tr>
                  <tr>
                      <td align="right">名称:</td>
                      <td><input class="easyui-textbox" type="text" id="updateSmsTemplateName" name="name" missingMessage="请输入名称"
                          validType="length[1,50]" invalidMessage="名称为1-50个字符" data-options="required:true"/></td>
                  </tr>
                  <tr>
                      <td align="right" valign="top">内容:</td>
                      <td><input class="easyui-textbox" type="text" id="updateSmsTemplateContent" name="content" missingMessage="请输入内容"
                          validType="length[1,100]" invalidMessage="内容为1-100个字符" data-options="multiline:true,required:true" 
                          style="width:210px;height:70px"/></td>
                  </tr>
                  <tr>
                      <td align="right" valign="top">备注:</td>
                      <td><input class="easyui-textbox" type="text" id="updateSmsTemplateRemark" name="remark" missingMessage="请输入备注"
                          validType="length[1,100]" invalidMessage="备注为1-100个字符"  data-options="multiline:true" style="width:210px;height:70px"/></td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updateSmsTemplateFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#updateSmsTemplateWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">
function formatSmsTemplateOperator(val,row,index){
     var tmp=
         <%if(actionUpdate != null){//角色判断%>
         "<a href=\"javascript:updateSmsTemplateWindowOpen()\"><%=actionUpdate.name %></a>&nbsp;"+
         <%}%>
         <%if(actionRemove !=null ){//角色判断%>
         "<a href=\"javascript:removeSmsTemplate('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
         <%}%>
         "";
     return tmp;
}
function searchSmsTemplateList(){        
    $('#smsTemplateList').datagrid('load',{  
          name:$('#searchSmsTemplateName').val(),
          content:$('#searchSmsTemplateContent').val()
    });  
}
$('#addSmsTemplateForm').form({
    url:'<%=path %>/addSmsTemplate.htm',
    onSubmit: function(){
        var addSmsTemplateCheck=$(this).form('validate');
        if(addSmsTemplateCheck){
        	$('#addSmsTemplateWindow').window('close');
        	$('#smsTemplateList').datagrid('loading');
        }
        return addSmsTemplateCheck;
    },
    success:function(data){
        $('#smsTemplateList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addSmsTemplateFormSubmit(){
    $('#addSmsTemplateForm').submit();
}
$('#updateSmsTemplateForm').form({
    url:'<%=path %>/updateSmsTemplate.htm',
    onSubmit: function(){
        var updateSmsTemplateCheck=$(this).form('validate');
        if(updateSmsTemplateCheck){
        	$('#updateSmsTemplateWindow').window('close');
        	$('#smsTemplateList').datagrid('loading');
        }
        return updateSmsTemplateCheck;
    },
    success:function(data){
        $('#smsTemplateList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updateSmsTemplateFormSubmit(){
    $('#updateSmsTemplateForm').submit();
}
function updateSmsTemplateWindowOpen(){
    var updateSmsTemplateForm = $('#updateSmsTemplateForm');
    updateSmsTemplateForm.form('clear');
    $('#updateSmsTemplateWindow').window('open');
    var node = $('#smsTemplateList').treegrid('getSelected');
    document.getElementById('updateSmsTemplateId').value = node.id;
    $('#updateSmsTemplateName').textbox('setValue',node.name);
    $('#updateSmsTemplateContent').textbox('setValue',node.content);
    $('#updateSmsTemplateRemark').textbox('setValue',node.remark);
}
function removeSmsTemplate(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removeSmsTemplate.htm',
            {id:id},
            function(data){
                $('#smsTemplateList').datagrid('reload');
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
