<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.risk.service.PayRiskExceptRuleService"%>
<%@ page import="com.pay.risk.dao.PayRiskExceptRule"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pCQqnhO2Q3rKTP61"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pCQmS7e2Q3rKTOI1"));
JWebAction actionSearchHistory = ((JWebAction)user.actionMap.get("pD7o7Be2Q3rKTPp1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pD86utf2Q3rKTOI1"));
JWebAction actionManualxListRun = ((JWebAction)user.actionMap.get("pOCMziI2Q3rKTPp1"));
%>
<script type="text/javascript">
var payRiskXListListPageTitle="X名单管理";
var payRiskXListAddPageTitle="添加名单状态";
var payRiskXListUpdatePageTitle="修改名单状态";
var payRiskXListSearchHistoryPageTitle="历史记录";
$(document).ready(function(){});
function formatPayRiskXListClientType(data,row,index){
	if(data=='<%=com.PayConstant.CUST_TYPE_USER %>'){
		return "个人";
	}else if(data=='<%=com.PayConstant.CUST_TYPE_MERCHANT %>'){
		return "商户";
	}else if(data=='<%=com.PayConstant.CUST_TYPE_MOBLIE %>'){
		return "手机号";
	}else if(data=='<%=com.PayConstant.CUST_TYPE_CARD %>'){
		return "银行卡号";
	}
}
function formatPayRiskXListXType(data,row,index){
	if(data=='1'){
		return "白名单";
	}else if(data=='2'){
		return "灰名单";
	}else if(data=='3'){
		return "黑名单";
	}else if(data=='4'){
		return "红名单";
	}
}
// 格式化客户名称
function formatPayRiskXListName(data,row,index){
	var name = "";
	if(row.clientType=='0') name = hideNameFormatter(row.realName,row,index);
	if(row.clientType=='1') name = hideNameFormatter(row.storeName,row,index);
	return name;
}
</script>
<table id="payRiskXListList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payRiskXList.htm?flag=0',method:'post',toolbar:'#payRiskXListListToolBar'">
       <thead>
        <tr>
           <th field="clientType" width="10%" align="left" sortable="true" data-options="formatter:formatPayRiskXListClientType">客户类型</th>
           <th field="clientCode" width="13%" align="left" sortable="true">客户编码</th>
           <th field="name" width="13%" align="left" sortable="true" formatter="formatPayRiskXListName">客户名称</th>
           <th field="regdtTime" width="13%" align="left" sortable="true">登记日期</th>
           <th field="ruleName" width="20%" align="left" sortable="true">违反规则</th>
           <th field="xType" width="10%" align="left" sortable="true" data-options="formatter:formatPayRiskXListXType">名单类型</th>
           <th field="operation" data-options="formatter:formatPayRiskXListOperator" width="19%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payRiskXListListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    客户类型<select class="easyui-combobox" panelHeight="auto" id="searchPayRiskXListClientType" data-options="editable:false" name="searchPayRiskXListClientType">
  	           <option value="">全部</option>
               <option value="<%=com.PayConstant.CUST_TYPE_USER %>">个人</option>
               <option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>">商户</option>
               <option value="<%=com.PayConstant.CUST_TYPE_MOBLIE %>">手机号</option>
               <option value="<%=com.PayConstant.CUST_TYPE_CARD %>">银行卡号</option>
		</select>
    客户编码<input type="text" id="searchPayRiskXListClientCode" name="searchPayRiskXListClientCode" class="easyui-textbox" value=""  style="width:130px"/>
    名单类型<select class="easyui-combobox" panelHeight="auto" id="searchPayRiskXListXType" data-options="editable:false" name="searchPayRiskXListXType">
	  	           <option value="">全部</option>
	               <option value="1">白名单</option>
	               <option value="3">黑名单</option>
	               <option value="4">红名单</option>
			</select>
    登记日期<input class="easyui-datebox" style="width:100px" id="searchPayRiskXListRegdtTimeStart" name="searchPayRiskXListRegdtTimeStart" data-options="editable:false" />~
			<input class="easyui-datebox" style="width:100px" id="searchPayRiskXListRegdtTimeEnd" name="searchPayRiskXListRegdtTimeEnd" data-options="editable:false" />
    违反规则<input class="easyui-combobox" id="searchPayRiskXListRuleCode" name="searchPayRiskXListRuleCode" style="width:250px"
			data-options="valueField:'id',textField:'text',data:searchPayRiskXListRuleData" validType="inputExistInCombobox['searchPayRiskXListRuleCode']"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayRiskXListList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayRiskXListPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
    <%if(actionManualxListRun != null){//角色判断%>
    <a href="javascript:setResultManualxListRunWindowOpen()" class="easyui-linkbutton" iconCls="icon-search"><%=actionManualxListRun.name %></a>
    <%} %>
</div>
<div id="setResultManualxListRunWindow" class="easyui-window" title="批量导入" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:420px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="setResultManualxListForm" method="post" enctype="multipart/form-data">
                <table cellpadding="15">
                    <tr><td align="right">批量导入：</td>
                    	<td>
                    		<input type="file" name="manualxListFile" id="manualxListFile" data-options="multiline:true">
                    	</td>
				    </tr>
				    <tr>
				    	<td colspan="2" style="text-align:right;padding:5px 0 0;"><a href="<%=path %>/templet/x_list.xls">下载模版</a></td>
				    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
            	onclick="setResultManualxListFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#setResultManualxListRunWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">
<%
//获取规则列表
String rules="";
for(int i = 0; i<PayRiskExceptRuleService.RISK_RULE_LIST.size(); i++){
  	 PayRiskExceptRule rule=(PayRiskExceptRule)PayRiskExceptRuleService.RISK_RULE_LIST.get(i);
  	 rules = rules+"{\"id\":\""+rule.ruleCode+"\",\"text\":\""+rule.ruleName+"\"},";
}
if(rules.endsWith(","))rules = rules.substring(0,rules.length()-1); 
%>
var searchPayRiskXListRuleData=[<%=rules %>];
$('#payRiskXListList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayRiskXListOperator(val,row,index){
     var tmp=
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayRiskXListPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionSearchHistory != null){//角色判断%>
        "<a href=\"javascript:searchHistoryPayRiskXListPageOpen('"+row.clientType+"','"+row.clientCode+"')\"><%=actionSearchHistory.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayRiskXListList(){
    $('#payRiskXListList').datagrid('load',{
          clientType:$('#searchPayRiskXListClientType').combobox('getValue'),//客户类型
          clientCode:$('#searchPayRiskXListClientCode').val(),//客户编码
          xType:$('#searchPayRiskXListXType').combobox('getValue'),//系统X名单类型
          regdtTimeStart:$('#searchPayRiskXListRegdtTimeStart').datebox('getValue'),//登记日期始
          regdtTimeEnd:$('#searchPayRiskXListRegdtTimeEnd').datebox('getValue'),//登记日期止
          ruleCode:$('#searchPayRiskXListRuleCode').combobox('getValue')//违反规则编号
    });  
}
//批量导入
function setResultManualxListRunWindowOpen(){
	document.getElementById('manualxListFile').value="";
    $('#setResultManualxListRunWindow').window('open');
}
function setResultManualxListFormSubmit(){
	//批量导入文件格式检查
	var fn = document.getElementById('manualxListFile').value;
	if(fn.length <=4 || fn.substring(fn.length-4,fn.length) != '.xls'){
		topCenterMessage('<%=JWebConstant.ERROR %>','文件类型错误（*.xls）');
		return;
	}
	$('#setResultManualxListForm').submit();
}
$('#setResultManualxListForm').form({
    url:'<%=path %>/manualxListRun.htm',
    onSubmit: function(){
    	$('#setResultManualxListRunWindow').window('close');
    },
    success:function(data){
    	$('#payRiskXListList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','批量导入成功');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayRiskXListPageOpen(){
    openTab('addPayRiskXListPage',payRiskXListAddPageTitle,'<%=path %>/jsp/pay/risk/pay_risk_x_list_add.jsp');
}
function updatePayRiskXListPageOpen(id){
    openTab('updatePayRiskXListPage',payRiskXListUpdatePageTitle,'<%=path %>/updatePayRiskXList.htm?flag=show&id='+id);
}
function searchHistoryPayRiskXListPageOpen(clientType,clientCode){
    openTab('searchHistoryPayRiskXListPage',payRiskXListSearchHistoryPageTitle,'<%=path %>/jsp/pay/risk/pay_risk_x_list_history.jsp?clientType='+clientType+'&clientCode='+clientCode);
}
</script>