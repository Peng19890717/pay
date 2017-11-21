<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%
	String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
	return;
}%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<table id="blogList" class="easyui-datagrid" style="width:100%;height:100%" rownumbers="true" pagination="true"
	fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
		%>/sysBlogManager.htm?flag=0',method:'post',toolbar:'#blogListToolBar'">
   	<thead>
        <tr>
           <th field="transactionId" width="11%" align="left" sortable="true">操作编号</th>
           <th field="operateCode" width="16%" align="left" sortable="true">操作码</th>
           <th field="content" data-options="formatter:formatBlogContentDetail" width="20%" align="left">内容</th>
           <th field="createTime" width="11%" align="left" sortable="true">时间</th>
           <th field="operatorId" width="10%" align="left" sortable="true">操作人</th>
           <th field="ip" width="9%" align="left" sortable="true">IP</th>
           <th field="reqUri" data-options="formatter:formatBlogReqUriDetail" width="21%" align="left" sortable="true">请求地址</th>
       </tr>
   	</thead>
</table>
<div id="blogListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="5%" align="right">操作编号</td>
    <td>&nbsp;&nbsp;<input type="text" id="searchBlogTransactionId" name="searchBlogTransactionId" class="easyui-textbox" value=""  style="width:130px"/></td>
    <td width="5%" align="right">操作码</td>
    <td>&nbsp;&nbsp;<input type="text" id="searchBlogOperateCode" name="searchBlogOperateCode" class="easyui-textbox" value=""  style="width:130px"/></td>
    <td align="right">内容</td>
    <td>&nbsp;&nbsp;<input type="text" id="searchBlogContent" name="searchBlogContent" class="easyui-textbox" value=""  style="width:130px"/></td>
    <td align="right">时间</td>
    <td>&nbsp;&nbsp;<input type="text" id="searchBlogCreateTimeStart" name="searchBlogCreateTimeStart" class="easyui-datetimespinner" value=""  style="width:180px"
		data-options="
            showSeconds: true,
            prompt: '点击上下箭头输入',
            icons:[{
                iconCls:'icon-clear',
                handler: function(e){
                    $(e.data.target).datetimespinner('clear');
                }
            }]"/>
    </td>
    <td align="right">到</td>
    <td>&nbsp;&nbsp;<input type="text" id="searchBlogCreateTimeEnd" name="searchBlogCreateTimeEnd" class="easyui-datetimespinner" value=""  style="width:180px"
		data-options="
            showSeconds: true,
            prompt: '点击上下箭头输入',
            icons:[{
                iconCls:'icon-clear',
                handler: function(e){
                    $(e.data.target).datetimespinner('clear');
                }
            }]"/>
    </td>
  </tr>
  <tr>
    <td height="30" align="right">操作人</td>
    <td>&nbsp;&nbsp;<input type="text" id="searchBlogOperatorId" name="searchBlogOperatorId" class="easyui-textbox" value=""  style="width:130px"/></td>
    <td align="right">IP</td>
    <td>&nbsp;&nbsp;<input type="text" id="searchBlogIp" name="searchBlogIp" class="easyui-textbox" value=""  style="width:110px"/></td>
    <td align="right">请求地址</td>
    <td>&nbsp;&nbsp;<input type="text" id="searchBlogReqUri" name="searchBlogReqUri" class="easyui-textbox" value=""  style="width:130px"/></td>
    <td align="right"><a href="javascript:searchBlogList()" class="easyui-linkbutton" iconCls="icon-search">查询</a></td>
    <!-- td>&nbsp;&nbsp;<a href="javascript:saveCacheBlog()" class="easyui-linkbutton" iconCls="icon-save">保存缓存日志</a></td -->
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
</div>
<script type="text/javascript">
$('#blogList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatBlogContentDetail(val,row,index){
	return "<span title='"+row.content+"'>"+row.content+"</span>";
}
function formatBlogReqUriDetail(val,row,index){
	return "<span title='"+row.reqUri+"'>"+row.reqUri+"</span>";
}
function searchBlogList(){		
	$('#blogList').datagrid('load',{
		transactionId:$('#searchBlogTransactionId').val(),
        operateCode:$('#searchBlogOperateCode').val(),
        content:$('#searchBlogContent').val(),
        createTimeStart:$('#searchBlogCreateTimeStart').val(),
        createTimeEnd:$('#searchBlogCreateTimeEnd').val(),
        operatorId:$('#searchBlogOperatorId').val(),
        ip:$('#searchBlogIp').val(),
        reqUri:$('#searchBlogReqUri').val()
	});
}
function saveCacheBlog(){
   	$.post('<%=path%>/sysBlogManager.htm?flag=1',
    	function(data){
    		$('#blogList').datagrid('reload');
            if(data=='<%=JWebConstant.OK %>'){
            	topCenterMessage('<%=JWebConstant.OK %>','保存成功！');
            }
            else topCenterMessage('<%=JWebConstant.ERROR %>',data);
        },
   		'json'
    );
}
</script>
