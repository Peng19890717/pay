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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("q4bWXAg2Q3rKTOV2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("q4bUj9f2Q3rKTOL1"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("q4bUj9f2Q3rKTOL2"));
%>
<script type="text/javascript">
var paySalesmanMerchantRelationListPageTitle="业务员商户关系";
var paySalesmanMerchantRelationAddPageTitle="添加业务员商户关系";
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
<div data-options="region:'center'">
<table id="paySalesmanMerchantRelationList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/paySalesmanMerchantRelation.htm?flag=0',method:'post',toolbar:'#paySalesmanMerchantRelationListToolBar'">
       <thead>
        <tr>
           <th field="userId" width="20%" align="left" sortable="true">用户号</th>
           <th field="uName" width="20%" align="left" sortable="true">用户名</th>
           <th field="merNo" width="20%" align="left" sortable="true">商户号</th>
           <th field="mName" width="20%" align="left" sortable="true">商户名</th>
           <th field="operation" data-options="formatter:formatPaySalesmanMerchantRelationOperator" width="33%" align="left">操作</th>
       </tr>
       </thead>
</table>
</div>
<div id="paySalesmanMerchantRelationListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    用户号：<input type="text" id="searchPaySalesmanMerchantRelationUserId" name="searchPaySalesmanMerchantRelationUserId" class="easyui-textbox" value=""  style="width:130px"/>
    商户号：<input type="text" id="searchPaySalesmanMerchantRelationMerNo" name="searchPaySalesmanMerchantRelationMerNo" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPaySalesmanMerchantRelationList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPaySalesmanMerchantRelationWindowOpens()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
    
</div>
<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
<a href="javascript:paySalesmanMerchantRelationExportExcel()"  class="easyui-linkbutton" iconCls="icon-config">业务员商户导出</a>
</div>
</div>
<div id="addPaySalesmanMerchantRelationWindowOpen" class="easyui-window" title="添加业务员商户关系" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:430px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="addPaySalesmanMerchantRelationForm" method="post">
                <table cellpadding="5">
                	<tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                    	<td align="right">用户号：</td>
                    	<td>
                    		<input class="easyui-textbox" type="text" id="addPaySalesmanMerchantRelationUserId" name="userId" missingMessage="请输入用户号"
                                validType="length[1,20]" invalidMessage="userId为1-20个字符" data-options="required:true"/>
                        </td>
                    	<td>
                    		<span></span>
                    	</td>
                   	</tr>
                    <tr>
                    	<td align="right">商户号：</td>
                    	<td>
                    		<input class="easyui-textbox" type="text" id="addPaySalesmanMerchantRelationMerNo" name="merNo" missingMessage="请输入商户号"
                                validType="length[1,20]" invalidMessage="merNo为1-20个字符" data-options="required:true"/>
                        </td>
                    	<td>
                    		<span></span>
                    	</td>
                   	</tr>
                </table>
            </form>
        </div> 
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPaySalesmanMerchantRelationSubmitScript()" 
            	id="addPaySalesmanMerchantRelationSubmit" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#addPaySalesmanMerchantRelationWindowOpen').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>

<script type="text/javascript">
$('#paySalesmanMerchantRelationList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPaySalesmanMerchantRelationOperator(val,row,index){
     var tmp=
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePaySalesmanMerchantRelation('"+row.merNo+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPaySalesmanMerchantRelationList(){
    $('#paySalesmanMerchantRelationList').datagrid('load',{
          userId:$('#searchPaySalesmanMerchantRelationUserId').val(),
          merNo:$('#searchPaySalesmanMerchantRelationMerNo').val()
    });  
}

// 页面弹窗
function addPaySalesmanMerchantRelationWindowOpens(){
	$('#addPaySalesmanMerchantRelationWindowOpen').form('clear');
    $('#addPaySalesmanMerchantRelationWindowOpen').window('open');
}

// 表单提交
function addPaySalesmanMerchantRelationSubmitScript(){
    $('#addPaySalesmanMerchantRelationForm').submit();
}

// 表单提交配置
$('#addPaySalesmanMerchantRelationForm').form({
    url:'<%=path %>/addPaySalesmanMerchantRelation.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#addPaySalesmanMerchantRelationWindowOpen').window('close');
       	return f;
    },
    success:function(data){
    	$('#paySalesmanMerchantRelationList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});

function removePaySalesmanMerchantRelation(merNo){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePaySalesmanMerchantRelation.htm',
            {merNo:merNo},
            function(data){
                $('#paySalesmanMerchantRelationList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
function paySalesmanMerchantRelationExportExcel(){
$.messager.confirm('提示', '您确认将当前记录导出至excel吗？', function(r){
        if(!r)return;
	    try{
	    	window.location.href = "<%=path %>/paySalesmanMerchatRelationExportExcel.htm?flag=0"+
			"&userId="+$('#searchPaySalesmanMerchantRelationUserId').val()+
	        "&merNo="+$('#searchPaySalesmanMerchantRelationMerNo').val();
	    }catch(e){alert(e);}
    });
}
</script>
