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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pmTFf4E2Q3rKTPf1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pmTFf4E2Q3rKTPf2"));
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<table id="smsLogList" class="easyui-datagrid" style="width:120%;height:110%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/smsLog.htm?flag=0',method:'post',toolbar:'#smsLogListToolBar'">
       <thead>
        <tr>
           <th field="tel" width="8%" align="left">手机号</th>
           <th field="content" width="75%" align="left">内容</th>
           <th field="status" width="4%" align="left">状态</th>
           <th field="createTime" width="11%" align="left">发送时间</th>
           <th field="userId" width="7%" align="left">接收人</th>
           <th field="createId" width="7%" align="left">发送人</th>
           <th field="remark" width="15%" align="left" sortable="true">备注</th>
       </tr>
       </thead>
</table>
<div id="smsLogListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    手机号<input type="text" id="searchSmsLogTel" name="searchSmsLogTel" class="easyui-textbox" value=""  style="width:130px"/>
    内容<input type="text" id="searchSmsLogContent" name="searchSmsLogContent" class="easyui-textbox" value=""  style="width:130px"/>
    发送时间<input type="text" id="searchSmsLogCreateTimeStart" name="searchSmsLogCreateTimeStart" class="easyui-datetimespinner" value=""  style="width:160px"
		data-options="
            showSeconds: true,
            prompt: '点击上下箭头输入',
            icons:[{
                iconCls:'icon-clear',
                handler: function(e){
                    $(e.data.target).datetimespinner('clear');
                }
            }]"/>
    到<input type="text" id="searchSmsLogCreateTimeEnd" name="searchSmsLogCreateTimeEnd" class="easyui-datetimespinner" value=""  style="width:160px"
		data-options="
            showSeconds: true,
            prompt: '点击上下箭头输入',
            icons:[{
                iconCls:'icon-clear',
                handler: function(e){
                    $(e.data.target).datetimespinner('clear');
                }
            }]"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchSmsLogList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addSmsLogWindowOpen()" class="easyui-linkbutton" iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<div id="addSmsLogWindow" class="easyui-window" title="发送短信" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:400px;height:450px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="addSmsLogForm" method="post">
                <table cellpadding="5">
                    <tr><td width="25%">&nbsp;</td><td width="75%">&nbsp;</td></tr>
                  <tr>
                      <td align="right">手机号:</td>
                      <td><a class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="javascript:void(0)" 
                			onclick="selectSmsUserWindowOpen()">从用户组选择</a></td>
                  </tr>
                  <tr>
                      <td align="right" valign="top">&nbsp;</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsLogTel" name="tel" 
                      	missingMessage="请输入手机号，多个手机号用“,”隔开，最多200个手机号"
                          	data-options="required:true,multiline:true,validType:'addSmsLogTelCheck'" style="width:240px;height:110px"/></td>
                  </tr>
                  <tr>
                      <td align="right">内容:</td>
                      <td><a class="easyui-linkbutton" data-options="iconCls:'icon-add'" href="javascript:void(0)" 
                			onclick="selectSmsExamplesWindowOpen()">从短信样例选择</a></td>
                  </tr>
                  <tr>
                      <td align="right" valign="top">&nbsp;</td>
                      <td><input class="easyui-textbox" type="text" id="addSmsLogContent" name="content"  missingMessage="请输入内容"
                          validType="length[1,100]" invalidMessage="内容为1-100个字符" data-options="multiline:true,required:true" 
                          style="width:240px;height:70px"/></td>
                  </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addSmsLogFormSubmit()" style="width:80px">发送</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#addSmsLogWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<div id="selectSmsUserWindow" class="easyui-window" title="选择用户" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:800px;height:500px;padding:5px;">
</div>
<div id="selectSmsExamplesWindow" class="easyui-window" title="选择短信样例" 
    data-options="iconCls:'icon-add',closed:true,modal:true" style="width:950px;height:500px;padding:5px;">
</div>
<script type="text/javascript">
function selectSmsUserWindowOpen(){
	$('#selectSmsUserWindow').window('refresh','<%=path %>/jsp/common/sms/sms_log_select_user.jsp?receiveObjId=addSmsLogTel');
	$('#selectSmsUserWindow').window('open');
}
function selectSmsExamplesWindowOpen(){
	$('#selectSmsExamplesWindow').window('refresh','<%=path %>/jsp/common/sms/sms_log_select_examples.jsp?receiveObjId=addSmsLogContent');
	$('#selectSmsExamplesWindow').window('open');
}
function searchSmsLogList(){        
    $('#smsLogList').datagrid('load',{  
          tel:$('#searchSmsLogTel').val(),
          content:$('#searchSmsLogContent').val(),
          createTimeStart:$('#searchSmsLogCreateTimeStart').val(),
          createTimeEnd:$('#searchSmsLogCreateTimeEnd').val()
    });  
}
$('#addSmsLogForm').form({
    url:'<%=path %>/addSmsLog.htm',
    onSubmit: function(){
        var addSmsLogCheck=$(this).form('validate');
        if(addSmsLogCheck){
        	$('#addSmsLogWindow').window('close');
        	$('#smsLogList').datagrid('loading');
        }
        return addSmsLogCheck;
    },
    success:function(data){
    	$('#smsLogList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','提交成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addSmsLogFormSubmit(){
    $('#addSmsLogForm').submit();
}
function addSmsLogWindowOpen(){
    $('#addSmsLogForm').form('clear');
	$('#addSmsLogWindow').window('open');
}
$.extend($.fn.validatebox.defaults.rules, {
	addSmsLogTelCheck:{
		validator:function (value,param) {
			var mobiles = value.split(',');
			var n=0;
			for(var i=0; i<mobiles.length; i++){
				if((!(/^\d+$/.test(mobiles[i]))||mobiles[i].length != 11) && mobiles[i].length !=0){
					$.fn.validatebox.defaults.rules.addSmsLogTelCheck.message = '手机号'+mobiles[i]+'非法';
					return false;
				} else {
					if(mobiles[i].length !=0 )n++;
				}
			}
			if(n > 200){
				$.fn.validatebox.defaults.rules.addSmsLogTelCheck.message = '手机号超出200';
				return false;
			}
			return true;
		}
	}
});
</script>
