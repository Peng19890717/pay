<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.user.dao.PayBusinessMember"%>
<%
	String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
	return;
}
JWebAction actionAdd = ((JWebAction)user.actionMap.get("q4by2vU2Q3rKTOI1"));
JWebAction actionDelete = ((JWebAction)user.actionMap.get("q4by2vU2Q3rKTOI3"));
%>

<table id="busiMemList" class="easyui-treegrid" style="width:100%;height:100%"
	fit="true" rownumbers="true" data-options="border:false,collapsible:true,autoRowHeight:true,singleSelect:true,
            animate:true, url: '<%=request.getContextPath() %>/bussinessMemberList.htm?flag=0',method:'post',
            idField:'id',treeField:'name',lines:true">
    <thead>
        <tr>
            <th data-options="field:'name'" style="width:40%">业务员姓名</th>
            <th data-options="field:'id'" style="width:20%" formatter="format_busiId">业务员编号</th>
            <th data-options="field:'operation',formatter:formatBusiMemOperator" style="width:10%">操作</th>
        </tr>
    </thead>
</table>
<div id="addBusiMemPanel" class="easyui-window" title="添加功能"
	data-options="closed:true,minimizable:false,collapsible:false,maximizable:false,resizable:false,iconCls:'icon-add',modal:true" 
   	style="width:322px;padding-top:15px;padding-left:35px;padding-bottom:15px;">
	   <form id="addBusiMemForm" method="post">
	   	   <input type="hidden" id="busiMemParentId" name="parentId"/>
	       <table cellpadding="5">
	       		<tr>
	       			<td>业务员</td>
	       			<td>
	       				<input id="selectBusiMemLeft" name="userId">
	       			</td>
	       		</tr>
	       		<tr>
	       			<td></td>
               		<td><a href="javascript:addBusiMem()" class="easyui-linkbutton"  style="width:100%;" iconCls="icon-ok">保存</a></td>
	       		</tr>
	       </table>
	   </form>
</div>
<script type="text/javascript">
$(function(){
	$('#selectBusiMemLeft').combobox({
	    url:'<%=path %>/findBusiMemLeft.htm',
	    valueField:'id',
	    textField:'text'
	});
});
function format_busiId(data,row, index){
	if(data!="0"){
		return data;
	}else{
		return "";
	}
}
function formatBusiMemOperator(val,row,index){
    var tmp=
       <%if(actionAdd != null){//角色判断%>
       "<a href=\"javascript:addBusiMemPageOPen('"+row.id+"')\"><%=actionAdd.name %></a>&nbsp;"+
       <%}%>
       <%if(actionDelete !=null ){//角色判断%>
       (row.id == '0'? "":"<a href=\"javascript:deleteBusiMemPage('"+row.id+"')\"><%=actionDelete.name %></a>&nbsp;")+
       <%}%>
       "";
    return tmp;
}
function addBusiMemPageOPen(userId){
	$('#addBusiMemForm').form('clear');
	$('#selectBusiMemLeft').combobox({
	    url:'<%=path %>/findBusiMemLeft.htm',
	    valueField:'id',
	    textField:'text'
	});
	$('#addBusiMemPanel').window('open');
	var node = $('#busiMemList').treegrid('getSelected');
	busiMemParentId = node.id;
	document.getElementById("busiMemParentId").value = busiMemParentId;
}
function addBusiMem(){
	$('#addBusiMemPanel').window('close');
	$("#addBusiMemForm").submit();
}
//删除该节点及其子节点
function deleteBusiMemPage(userId){
	 $.messager.confirm('提示','您确定要删除该业务员吗？', function(r){
		    if(!r)return;
		    try{
		        $.post('<%=path %>/removeBusiMem.htm',
		            {userId:userId},
		            function(data){
		            	$("#selectBusiMemLeft").combobox("reload","<%=path %>/findBusiMemLeft.htm");
		                $('#busiMemList').treegrid('reload');
		                if(data=='<%=JWebConstant.OK %>'){
		                    topCenterMessage('<%=JWebConstant.OK %>','操作成功!');
		                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
		            },
		           'text'
		        );
		    }catch(e){alert(e);}
		   });
}
$("#addBusiMemForm").form({
    url:'<%=path %>/addPayBusiMem.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#busiMemList').treegrid('reload');
        $("#selectBusiMemLeft").combobox("reload","<%=path %>/findBusiMemLeft.htm");
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
</script>