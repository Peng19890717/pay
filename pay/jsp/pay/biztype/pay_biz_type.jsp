<%@page import="com.pay.biztype.service.PayBizTypeService"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.PayConstant"%>
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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pGkBIKb2Q3rKTOK1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pGkBIKb2Q3rKTP51"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pGlpz9B2Q3rKTOW1"));
%>
<script type="text/javascript">
var payBizTypeListPageTitle="业务类型管理";
var payBizTypeAddPageTitle="添加业务类型";
var payBizTypeDetailPageTitle="业务类型详情";
var payBizTypeUpdatePageTitle="修改业务类型";
$(document).ready(function(){});
function formatPayBizTypeIsActive(data,row,index){
	if(data=='0'){
		return "不生效";
	}else if(data=='1'){
		return "生效";
	}
}
function formatPayBizTypeIsRealname(data,row,index){
	if(data=='0'){
		return "非实名";
	}else if(data=='1'){
		return "实名";
	}
}
</script>
<table id="payBizTypeList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payBizType.htm?flag=0',method:'post',toolbar:'#payBizTypeListToolBar'">
       <thead>
        <tr>
           <th field="code" width="20%" align="left" sortable="true">业务代号</th>
           <th field="name" width="20%" align="left" sortable="true">类型名称</th>
           <th field="isActive" width="20%" align="left" sortable="true" data-options="formatter:formatPayBizTypeIsActive">生效状态</th>
           <th field="isRealname" width="20%" align="left" sortable="true" data-options="formatter:formatPayBizTypeIsRealname">实名类型</th>
           <th field="operation" data-options="formatter:formatPayBizTypeOperator" width="20%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payBizTypeListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    生效状态
    	<select class="easyui-combobox" panelHeight="auto" id="searchPayBizTypeIsActive" data-options="editable:false" name="searchPayBizTypeIsActive">
  	           <option value="">状态不限</option>
               <option value="0">不生效</option>
               <option value="1">生效</option>
		</select>
    业务类型名称<input type="text" id="searchPayBizTypeName" name="searchPayBizTypeName" class="easyui-textbox" style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayBizTypeList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayBizTypePageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payBizTypeList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayBizTypeOperator(val,row,index){
     var tmp=
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayBizTypePageOpen('"+row.seqNo+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayBizTypeList(){
    $('#payBizTypeList').datagrid('load',{
          isActive:$('#searchPayBizTypeIsActive').combobox('getValue'),//生效状态
          name:$('#searchPayBizTypeName').val()//业务类型名称
    });  
}
function addPayBizTypePageOpen(){
    openTab('addPayBizTypePage',payBizTypeAddPageTitle,'<%=path %>/jsp/pay/biztype/pay_biz_type_add.jsp');
}
function updatePayBizTypePageOpen(seqNo){
    openTab('updatePayBizTypePage',payBizTypeUpdatePageTitle,'<%=path %>/updatePayBizType.htm?flag=show&seqNo='+seqNo);
}
</script>
