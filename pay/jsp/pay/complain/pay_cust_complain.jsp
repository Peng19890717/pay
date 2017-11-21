<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J956Q1OS3JLXPT2Q81"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("J958J4ZM3JLXPT2Q41"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("J95BW9DP3JLXPT2PY1"));
%>
<script type="text/javascript">
function formatCustComplainType(data,row,index) {
	if(data=="0") return "用户";
	if(data=="1") return "商户";
	return "";
}
function formatCustComplainOptStatus(data,row,index) {
	if(data=="0") return "初始";
	if(data=="1") return "结束";
	if(data=="2") return "作废";
	return "";
}
var payCustComplainListPageTitle="客户投诉管理";
var payCustComplainAddPageTitle="添加客户投诉";
var payCustComplainUpdatePageTitle="修改客户投诉";
$(document).ready(function(){});
</script>
<table id="payCustComplainList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,method:'post',url:'<%=path %>/payCustComplain.htm?flag=0',toolbar:'#payCustComplainListToolBar'">
       <thead>
        <tr>
           <th field="id" width="10%" align="left">投诉编号</th>
           <th field="type" width="4%" align="left" data-options="formatter:formatCustComplainType">投诉类型</th>
           <th field="custId" width="4%" align="left">所属商户号</th>
           <th field="storeName" width="12%" align="left">商户名称</th>
           <th field="transAmt" width="8%" align="left">交易金额</th>
           <th field="frozenAmt" width="10%" align="left">冻结金额</th>
           <th field="accFrozenId" width="10%" align="left">冻结编号</th>
           <th field="accAdjustId" width="10%" align="left">调账编号</th>
           <th field="optStatus" width="7%" align="left"  data-options="formatter:formatCustComplainOptStatus">资金处理结果</th>
           <th field="name" width="7%" align="left">客户姓名</th>
           <th field="tel" width="7%" align="left">客户电话</th>
           <th field="channel" width="5%" align="left">渠道</th>
           <th field="cTime" width="10%" align="left">投诉时间</th>
           <th field="operation" data-options="formatter:formatPayCustComplainOperator" width="16%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payCustComplainListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
  投诉编号<input type="text" id="searchPayCustComplainId" name="searchPayCustComplainId" class="easyui-textbox" value=""  style="width:100px"/>
    投诉类型
        <select id="searchPayCustComplainType" name="searchPayCustComplainType" data-option="editable:false" class="easyui-combobox">
	   	 	<option value="">请选择</option>
	   	 	<option value="0" selected>用户</option>
	   		<option value="1">商户</option>
	    </select>
    所属商户号<input type="text" id="searchPayCustComplainCustId" name="searchPayCustComplainCustId" class="easyui-textbox" value=""  style="width:100px"/>
    处理结果
        <select id="searchPayCustComplainOptStatus" name="searchPayCustComplainOptStatus" data-option="editable:false" class="easyui-combobox">
	   	 	<option value="">请选择</option>
	   	 	<option value="0" selected>初始</option>
	   	 	<option value="1">结束</option>
	   		<option value="2">作废</option>
	    </select>
    客户姓名<input type="text" id="searchPayCustComplainName" name="searchPayCustComplainName" class="easyui-textbox" value=""  style="width:100px"/>
    客户电话<input type="text" id="searchPayCustComplainTel" name="searchPayCustComplainTel" class="easyui-textbox" value=""  style="width:100px"/>
    渠道
    <select  class="easyui-combobox" panelHeight="auto" id="searchPayCustComplainChannel" data-options="panelHeight:500,editable:false" name="searchPayCustComplainChannel">
 		<option value="">请选择</option>
  		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
			PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
			<option value="<%=channel.bankCode %>"><%=channel.bankName %></option>
		<%}%>
 	</select>
    投诉时间<input type="text" id="searchPayCustComplainCTimeStart" name="searchPayCustComplainCTimeStart" data-options="editable:false"
    		class="easyui-datebox" value=""  style="width:100px"/>
    	 ~<input type="text" id="searchPayCustComplainCTimeEnd" name="searchPayCustComplainCTimeEnd"
			data-options="editable:false" class="easyui-datebox" style="width:100px" value=""/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayCustComplainList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayCustComplainPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payCustComplainList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayCustComplainOperator(val,row,index){
     var tmp=
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayCustComplainPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayCustComplainList(){
    $('#payCustComplainList').datagrid('load',{
    	  id:$('#searchPayCustComplainId').val(),
          type:$('#searchPayCustComplainType').datebox('getValue'),
          custId:$('#searchPayCustComplainCustId').val(),
          optStatus:$('#searchPayCustComplainOptStatus').datebox('getValue'),
          name:$('#searchPayCustComplainName').val(),
          tel:$('#searchPayCustComplainTel').val(),
          channel:$('#searchPayCustComplainChannel').datebox('getValue'),
          cTimeStart:$('#searchPayCustComplainCTimeStart').datebox('getValue'),
          cTimeEnd:$('#searchPayCustComplainCTimeEnd').datebox('getValue')
    });  
}
function addPayCustComplainPageOpen(){
    openTab('addPayCustComplainPage',payCustComplainAddPageTitle,'<%=path %>/jsp/pay/complain/pay_cust_complain_add.jsp');
}
function updatePayCustComplainPageOpen(id){
    openTab('updatePayCustComplainPage',payCustComplainUpdatePageTitle,'<%=path %>/updatePayCustComplain.htm?flag=show&id='+id);
}
</script>