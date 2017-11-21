<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.Tools"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="java.text.SimpleDateFormat"%>
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
<table id="sysActionList" class="easyui-treegrid" style="width:100%;height:100%"
	fit="true" rownumbers="true" data-options="border:false,collapsible:true,autoRowHeight:true,singleSelect:true,
            animate:true, url: '<%=path %>/action.htm',method:'post',
            idField:'id',treeField:'name',lines:true">
    <thead>
        <tr>
            <th data-options="field:'name'" style="width:37%">菜单列表</th>
            <th data-options="field:'actionId'" style="width:12%">功能编号</th>
            <th data-options="field:'actionUrl'" style="width:23%">URL</th>
            <th data-options="field:'operatorId'" style="width:8%">操作人</th>
            <th data-options="field:'operatorTime'" style="width:8%">操作时间</th>
            <th data-options="field:'operation',formatter:formatActionOperator" style="width:10%">操作</th>
        </tr>
    </thead>
</table>
<div id="actionOperatorMenuAll" class="easyui-menu" style="width:120px;">
    <div onclick="appendAction()" data-options="iconCls:'icon-add'">添加</div>
    <div onclick="modifyAction()" data-options="iconCls:'icon-edit'">修改</div>
    <div onclick="removeAction()" data-options="iconCls:'icon-remove'">删除</div>
</div>
<div id="actionOperatorMenuModifyRemove" class="easyui-menu" style="width:120px;">
    <div onclick="modifyAction()" data-options="iconCls:'icon-edit'">修改</div>
    <div onclick="removeAction()" data-options="iconCls:'icon-remove'">删除</div>
</div>
<div id="actionOperatorMenuAdd" class="easyui-menu" style="width:120px;">
    <div onclick="appendAction()" data-options="iconCls:'icon-add'">添加</div>
</div>
<div id="addSysActionPanel" class="easyui-window" title="添加功能"
	data-options="closed:true,minimizable:false,collapsible:false,maximizable:false,resizable:false,iconCls:'icon-add',modal:true" 
   	style="width:322px;padding-top:15px;padding-left:35px;padding-bottom:15px;">
   <form id="addSysActionForm" method="post">
   	   <input type="hidden" id="sysActionParentId" name="sysActionParentId" values=""></input>
   	   <input type="hidden" id="sysActionId" name="sysActionId" values=""></input>
       <table cellpadding="5">
           <tr>
               <td align="right">功能类型:</td>
               <td>
				<span id="sysActionTypeP1">
					<input type="radio" name="sysActionType" value="0" id="sysActionType0"
						onclick="$('#sysActionUrl').textbox('disable');actionAddType='0';"/>目录
					<input type="radio" name="sysActionType" value="1"  id="sysActionType1"
						onclick="$('#sysActionUrl').textbox('enable');actionAddType='1';"/>页面</span>
				<span id="sysActionTypeP2">
					<input type="radio" name="sysActionType" checked value="2" id="sysActionType2"/>操作
               </td>
           </tr>
           <tr>
               <td align="right">名称:</td>
               <td><input class="easyui-textbox" type="text" id="sysActionName" name="sysActionName" validType="length[1,20]"></input></td>
           </tr>
           <tr>
               <td align="right">URL:</td>
               <td><input class="easyui-textbox" type="text" id="sysActionUrl" name="sysActionUrl"></input></td>
           </tr>
           <tr>
               <td></td>
               <td><a href="javascript:addSysActionForm()" class="easyui-linkbutton"  style="width:100%;" iconCls="icon-ok">保存</a></td>
           </tr>
       </table>
   </form>
</div>
<div id="modifySysActionPanel" class="easyui-window" title="修改功能"
	data-options="closed:true,minimizable:false,collapsible:false,maximizable:false,resizable:false,iconCls:'icon-add',modal:true" 
   	style="width:322px;padding-top:15px;padding-left:35px;padding-bottom:15px;">
   <form id="modifySysActionForm" method="post">
   	   <input type="hidden" id="sysActionId1" name="sysActionId1" values=""></input>
       <table cellpadding="5">
           <tr>
               <td align="right">名称:</td>
               <td><input class="easyui-textbox" type="text" id="sysActionName1" name="sysActionName1" 
               		validType="length[1,20]"></input></td>
           </tr>
           <tr>
               <td align="right">URL:</td>
               <td><input class="easyui-textbox" type="text" id="sysActionUrl1" name="sysActionUrl1"></input></td>
           </tr>
           <tr>
               <td></td>
               <td><a href="javascript:modifySysActionForm()" class="easyui-linkbutton"  style="width:100%;" iconCls="icon-ok">修改</a></td>
           </tr>
       </table>
   </form>
</div>
<script type="text/javascript">
	function formatActionOperator(val,row,index){
	    return ''+
	    	<%=user.actionMap.get("010301")!=null ? "(row.actionType != 2 ? '<a href=\"javascript:appendAction()\">添加</a>&nbsp;':'')":"''"%>+
	    	<%=user.actionMap.get("010302")!=null ? "(row.actionId.substr(0,1)!='0'?'<a href=\"javascript:modifyAction()\">修改</a>&nbsp;':'')":"''"%>+
	    	<%=user.actionMap.get("010303")!=null ? "(row.actionId.substr(0,1)!='0'?'<a href=\"javascript:removeAction()\">删除</a>':'')":"''"%>
	    	;  
	}
	var actionMenuType;
    function appendAction(){
   		$('#addSysActionForm').form('clear');
   		var node = $('#sysActionList').treegrid('getSelected');
   		sysActionParentId = node.id;
   		actionMenuType = node.actionType;
    	if(node.actionType=='0'){
    		actionAddType = '0';
    		document.getElementById('sysActionTypeP1').style.display='';
    		document.getElementById('sysActionTypeP2').style.display='none';
    		document.getElementById('sysActionType0').checked=true;
    		$('#sysActionUrl').textbox('disable');
    	} else if(node.actionType=='1'){
    		actionAddType = '2';
    		document.getElementById('sysActionTypeP1').style.display='none';
    		document.getElementById('sysActionTypeP2').style.display='';
    		document.getElementById('sysActionType2').checked=true;
    		$('#sysActionUrl').textbox('enable');
    	}
    	$('#addSysActionPanel').window('open');
    }
    function modifyAction(){
   		$('#modifySysActionForm').form('clear');
    	$('#modifySysActionPanel').window('open');
    	var node = $('#sysActionList').treegrid('getSelected');
    	actionMenuType = node.actionType;
    	$('#sysActionName1').textbox('setValue',node.name);
    	document.getElementById("sysActionId1").value=node.id;
    	if(node.actionType=="0"){
    		$('#sysActionUrl1').textbox('disable');
    	} else {
    		$('#sysActionUrl1').textbox('enable');
    		$('#sysActionUrl1').textbox('setValue',node.actionUrl);
    	}
    }
    function removeAction(){
    	var node = $('#sysActionList').treegrid('getSelected');
   		$.messager.confirm('提示', '确认删除?', function(r){
            if(!r)return;
	        if (node){
	        	$.post('<%=path %>/sysActionRemove.htm',
	        		{actionId:node.id},
	        		function(data){
                       if(data=='<%=JWebConstant.OK %>'){
                        	$('#jwebMenu').tree('reload');
                        	$('#sysActionList').treegrid('remove', node.id);
                        	topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                       } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
			        },
	        		'json'
                );
	        }
        });
    }
    var actionAddType="0",sysActionSort="<%=Tools.getUniqueIdentify() %>",sysActionIdIndex = 0,sysActionParentId;
    var actionIcons=['icon-dir','icon-list','icon-config'];
    function addSysActionForm(){
		sysActionIdIndex ++;
		document.getElementById('sysActionParentId').value = sysActionParentId;
		document.getElementById('sysActionId').value = sysActionSort+sysActionIdIndex;
		$('#addSysActionForm').submit();
	}
    $('#addSysActionForm').form({
	    url:'<%=path %>/sysActionAdd.htm',
	    onSubmit: function(){
	    	if($('#sysActionName').textbox('getValue').length == 0){
	    		topCenterMessage('<%=JWebConstant.ERROR %>','名称不能为空！');
	    		return false;
	    	}
	    	if(actionAddType!='0'&& $('#sysActionUrl').textbox('getValue').length == 0){
	    		topCenterMessage('<%=JWebConstant.ERROR %>','URL不能为空！');
	    		return false;
	    	}
	    	$('#addSysActionPanel').window('close');
	    	$("#sysActionList").treegrid("loading","处理中...");
			return true;
	    },
	    success:function(data){
	    	$('#sysActionList').treegrid('loaded');
	    	var tmpUrl = $('#sysActionUrl').textbox('getValue');
	    	if(document.getElementById('addSysActionForm').sysActionType.value=='0')tmpUrl='';
		   	if(data=='<%=JWebConstant.OK %>'){
		   		/*
              	var node = $('#sysActionList').treegrid('getSelected');
		        $('#sysActionList').treegrid('append',{
		            parent: node.id,
		            data: [{
		                id: document.getElementById('sysActionId').value,
		                name: $('#sysActionName').textbox('getValue'),
		                actionUrl:tmpUrl,
		                actionId: document.getElementById('sysActionId').value,
		                actionType:actionAddType,
		                operatorId: '<%=user == null ? "" : user.id %>',
		                operatorTime: '<%=new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>',
		                iconCls:actionIcons[parseInt(actionAddType)]
		            }]
		        });
		        $('#sysActionList').tree('reload');
		        */
		        $('#jwebMenu').tree('reload');
		        $('#sysActionList').treegrid('reload');
		        topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	    }
	});
	function modifySysActionForm(){
    	try{
			$('#modifySysActionForm').submit();
		} catch (e){alert(e);}
	}
    $('#modifySysActionForm').form({
	    url:'<%=path %>/sysActionModify.htm',
	    onSubmit: function(){
	    	if($('#sysActionName1').textbox('getValue').length == 0){
	    		topCenterMessage('<%=JWebConstant.ERROR %>','名称不能为空！');
	    		return false;
	    	}
	    	if(actionMenuType!='0'&& $('#sysActionUrl1').textbox('getValue').length == 0){
	    		topCenterMessage('<%=JWebConstant.ERROR %>','URL不能为空！');
	    		return false;
	    	}
	    	$('#modifySysActionPanel').window('close');
	    	$("#sysActionList").treegrid("loading","处理中...");
			return true;
	    },
	    success:function(data){
	    	$('#sysActionList').treegrid('loaded');
		    if(data=='<%=JWebConstant.OK %>'){
		    	$('#jwebMenu').tree('reload');
		    	$('#sysActionList').treegrid('reload');
		    	topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	    }
	});
</script>