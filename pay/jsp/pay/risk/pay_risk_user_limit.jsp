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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pGkxhDD2Q3rKTOP6"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pGkxhDD2Q3rKTOP7"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pGkxhDD2Q3rKTOP10"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pGkxhDD2Q3rKTOP8"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pGkxhDD2Q3rKTOP9"));
%>
<script type="text/javascript">
var payRiskUserLimitListPageTitle="用户限额";
var payRiskUserLimitAddPageTitle="添加用户限额";
var payRiskUserLimitDetailPageTitle="用户限额详情";
var payRiskUserLimitUpdatePageTitle="修改用户限额";
$(document).ready(function(){});
</script>
<table id="payRiskUserLimitList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payRiskUserLimit.htm?flag=0',method:'post',toolbar:'#payRiskUserLimitListToolBar'">
       <thead>
        <tr>
           <th field="userType" width="5%" align="left" formatter="limitUserTypeFormatter">实名状态</th>
           <th field="xListType" width="5%" align="left" formatter="xListFormatter">X名单</th>
           <th field="tranType" width="5%" align="left" formatter="limitTranTypeFormatter">交易类型</th>
           <th field="userCode" width="12%" align="left" sortable="true" formatter="limitUserCodeFormatter">用户编码</th>
           <th field="isUse" width="5%" align="left" formatter="isUseFormatter">可用</th>
           <th field=startDate width="5%" align="left" sortable="true">开始时间</th>
           <th field="endDate" width="5%" align="left" sortable="true">结束时间</th>
           <th field="createUserId" width="5%" align="left" sortable="true">创建人</th>
           <th field="createTime" width="10%" align="left" sortable="true">创建时间</th>
           <th field="updateUserId" width="5%" align="left" sortable="true">维护人</th>
           <th field="updateTime" width="10%" align="left" sortable="true">维护时间</th>
           <th field="operation" data-options="formatter:formatPayRiskUserLimitOperator" width="10%" align="left">操作</th>
           <th field="remark" width="15%" align="left" sortable="true">备注</th>
       </tr>
       </thead>
</table>
<div id="payRiskUserLimitListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    实名状态<select id="searchPayRiskUserLimitUserType">
    	<option value=""></option>
      	<option value="1">实名</option>
      	<option value="2">非实名</option>
      	<option value="3">指定用户</option>
   </select>
    X名单<select id="searchPayRiskUserLimitXListType">
    	<option value=""></option>
      	<option value="1">白名单</option>
      	<option value="4">红名单</option>
   </select>
    交易类型<select id="searchPayRiskUserLimitTranType">
    		<option value=""></option>
       		<option value="1">消费</option>
       		<option value="2">充值</option>
       		<!-- option value="3">结算</option>
       		<option value="4">退款</option -->
       		<option value="5">提现</option>
       		<option value="6">转账</option>
       		<option value="7">卡限额</option>
       	</select>
    用户编码<input type="text" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayRiskUserLimitList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayRiskUserLimitPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
// 状态类型
function limitUserTypeFormatter(data,row,index){
	if(data == '1') {
		return '实名';
	} else if(data == '2') {
		return '非实名';
	} else if(data == '3') {
		return '指定用户';
	}
}
// 启用状态
function isUseFormatter(data,row,index){
	if(data == '0'){
		return '可用';
	} else if(data == '1') {
		return '不可用';
	}
}
// x名单
function xListFormatter(data,row,index){
	if(data == '1'){
		return '白名单';
	} else if(data == '4') {
		return '红名单';
	}
}
// 用户编码
function limitUserCodeFormatter(data,row,index){
	if(data == '-1')return '';
	else return data;
}
// 交易类型
function limitTranTypeFormatter(data,row,index){
	if(data == '1'){
		return '消费';
	} else if(data == '2'){
		return '充值';
	} else if(data == '5'){
		return '提现';
	} else if(data == '6'){
		return '转账';
	} else if(data == '7'){
		return '卡限额';
	}
}
$('#payRiskUserLimitList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayRiskUserLimitOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayRiskUserLimitPageOpen('"+row.id+"')\"><%=actionDetail.name %></a>&nbsp;&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayRiskUserLimitPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayRiskUserLimit('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayRiskUserLimitList(){
    $('#payRiskUserLimitList').datagrid('load',{
          userType:$('#searchPayRiskUserLimitUserType').val(),
          xListType:$('#searchPayRiskUserLimitXListType').val(),
          tranType:$('#searchPayRiskUserLimitTranType').val(),
          userCode:$('#searchPayRiskUserLimitUserCode').val()
    });  
}
function addPayRiskUserLimitPageOpen(){
    openTab('addPayRiskUserLimitPage',payRiskUserLimitAddPageTitle,'<%=path %>/jsp/pay/risk/pay_risk_user_limit_add.jsp');
}
function detailPayRiskUserLimitPageOpen(id){
    openTab('detailPayRiskUserLimitPage',payRiskUserLimitDetailPageTitle,'<%=path %>/detailPayRiskUserLimit.htm?id='+id);
}
function updatePayRiskUserLimitPageOpen(id){
    openTab('updatePayRiskUserLimitPage',payRiskUserLimitUpdatePageTitle,'<%=path %>/updatePayRiskUserLimit.htm?flag=show&id='+id);
}
function removePayRiskUserLimit(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayRiskUserLimit.htm',
            {id:id},
            function(data){
                $('#payRiskUserLimitList').datagrid('reload');
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
