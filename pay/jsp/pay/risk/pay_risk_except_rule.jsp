<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.PayConstant"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pDqfccv2Q3rKTOI6"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pDqfccv2Q3rKTOI1"));
JWebAction actionIsAble = ((JWebAction)user.actionMap.get("pDqfccv2Q3rKTOI2"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pDqfccv2Q3rKTOI4"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pDqfccv2Q3rKTOI5"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pDqfccv2Q3rKTOI3"));
%>
<script type="text/javascript">
var payRiskExceptRuleListPageTitle="规则管理";
var payRiskExceptRuleAddPageTitle="添加风控规则";
var payRiskExceptRuleDetailPageTitle="风控规则详情";
var payRiskExceptRuleUpdatePageTitle="修改风控规则";
$(document).ready(function(){});

// 规则类型
function ruleType_formatter(data,row,index) {
	if(data=="<%=com.PayConstant.CUST_TYPE_USER %>"){
		return "个人";
	} else if(data=="<%=com.PayConstant.CUST_TYPE_MERCHANT %>"){
		return "商户 ";
	}
}

// 业务类型
function comTypeNo_formatter(data,row,index) {
	<%
	java.util.Iterator<String> it = PayConstant.MER_BIZ_TYPE.keySet().iterator();
	while(it.hasNext()){
	String key = (String)it.next();
	%>if(data=="<%=key %>")return "<%=PayConstant.MER_BIZ_TYPE.get(key) %>";<%
	}%>
}

// 异常类型
function excpType_formatter(data,row,index) {
	if(data=="1"){
		return "异常";
	} else if(data=="2"){
		return "可疑";
	}
}
// 一般预警项
function ruleLevelItem_formatter(data,row,index) {
	if(data=="0"){
		return "交易突增";
	} else if(data=="1"){
		return "疑似套现 ";
	} else if(data=="2"){
		return "分单 ";
	} else if(data=="3"){
		return "非常规时段交易";
	} else if(data=="4"){
		return "零交易";
	} else if(data=="5"){
		return "其它 ";
	} else if(data=="6"){
		return "无";
	}
}
// 规则类型
function isUse_formatter(data,row,index) {
	if(data=="0"){
		return "关闭";
	} else if(data=="1"){
		return "启用 ";
	}
}

</script>
<table id="payRiskExceptRuleList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payRiskExceptRule.htm?flag=0',method:'post',toolbar:'#payRiskExceptRuleListToolBar'">
       <thead>
        <tr>
		   <th field="ruleCode" width="10%" align="left" sortable="true">编号</th>
		   <th field="ruleName" width="10%" align="left" sortable="true">规则名称</th>
		   <th field="ruleType" width="5%" align="left" sortable="true" formatter="ruleType_formatter">规则类型</th>
		   <!--  
		   <th field="comTypeNo" width="5%" align="left" sortable="true" formatter="comTypeNo_formatter">业务类型</th>-->
		   
		   <th field="excpType" width="5%" align="left" sortable="true" formatter="excpType_formatter">异常类型</th>
		   <th field="ruleLevelItem" width="8%" align="left" sortable="true" formatter="ruleLevelItem_formatter">一般预警项</th>
		   <th field="isUse" width="5%" align="left" sortable="true" formatter="isUse_formatter">规则状态</th>
		   <th field="ruleDes" width="14%" align="left" sortable="true">规则描述</th>
		   <th field="ruleStartDate" width="6%" align="left" sortable="true">生效时间</th>
		   <th field="ruleEndDate" width="6%" align="left" sortable="true">失效时间</th>
		   <th field="updateTime" width="6%" align="left" sortable="true">修改日期</th>
		   <th field="updateName" width="6%" align="left" sortable="true">修改操作员</th>
		   <th field="operation" data-options="formatter:formatPayRiskExceptRuleOperator" width="13%" align="left">操作</th>
		</tr>
       </thead>
</table>
<div id="payRiskExceptRuleListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
规则名称<input type="text" id="searchPayRiskExceptRuleRuleName" name="searchPayRiskExceptRuleRuleName" class="easyui-textbox" value=""  style="width:130px"/>
规则类型
	<select class="easyui-combobox" id="searchPayRiskExceptRuleRuleType" name="searchPayRiskExceptRuleRuleType" missingMessage="请选择规则类型" data-options="editable:false">
   		<option value="">请选择</option>
   		<option value="<%=com.PayConstant.CUST_TYPE_USER %>">个人</option>
   		<option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>">商户</option>
   	</select>
异常类型
	<select class="easyui-combobox" id="searchPayRiskExceptRuleExcpType" name="searchPayRiskExceptRuleExcpType" missingMessage="请选择异常类型" data-options="editable:false">
   		<option value="">请选择</option>
   		<option value="1">异常</option>
   		<option value="2">可疑</option>
    </select>
规则状态
    <select class="easyui-combobox" id="searchPayRiskExceptRuleIsUse" name="searchPayRiskExceptRuleIsUse" missingMessage="请选择规则状态" data-options="editable:false">
   		<option value="">请选择</option>
   		<option value="0">关闭</option>
   		<option value="1">启用</option>
    </select>
<!-- 
业务类型
	<select class="easyui-combobox" id="searchPayRiskExceptRuleComTypeNo" name="searchPayRiskExceptRuleComTypeNo" missingMessage="请选择业务类型" data-options="editable:false">
		<option value="">请选择</option>
		<%	
			java.util.Iterator<String> itm = com.PayConstant.MER_BIZ_TYPE.keySet().iterator();
			while(itm.hasNext()){
				String key = itm.next();
				String value = com.PayConstant.MER_BIZ_TYPE.get(key);
				%><option value="<%=key%>"><%=value%></option>
			<%}
		%>
	</select> -->
预警项
	<select class="easyui-combobox" id="searchPayRiskExceptRuleRuleLevelItem" name="searchPayRiskExceptRuleRuleLevelItem" missingMessage="请选择预警项" data-options="editable:false">
		<option value="">请选择</option>
		<option value="0">交易突增</option>
		<option value="1">疑似套现</option>
		<option value="2">分单</option>
		<option value="3">非常规时段交易</option>
		<option value="4">零交易</option>
		<option value="5">其它</option>
		<option value="6">无</option>
	</select>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayRiskExceptRuleList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayRiskExceptRulePageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payRiskExceptRuleList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayRiskExceptRuleOperator(val,row,index){
     var tmp=
		<%if(actionIsAble != null){//角色判断%>
		(1 == row.isUse ? "<a href=\"javascript:updatePayRiskExceptRuleStatus('disabledPayRiskExceptRuleStatus.htm','"+row.ruleCode+"','IS_USE','0','您确定要禁用（ "+row.ruleName+" ）规则吗？')\">禁用</a>&nbsp;&nbsp;" :  "<a href=\"javascript:updatePayRiskExceptRuleStatus('abledPayRiskExceptRuleStatus.htm','"+row.ruleCode+"','IS_USE','1','您确定要启用（ "+row.ruleName+" ）规则吗？')\">启用</a>&nbsp;&nbsp;")+
		<%}%>
		<%if(actionDetail != null){//角色判断%>
		"<a href=\"javascript:detailPayRiskExceptRulePageOpen('"+row.ruleCode+"')\"><%=actionDetail.name %></a>&nbsp;"+
		<%}%>
		<%if(actionUpdate != null){//角色判断%>
		"<a href=\"javascript:updatePayRiskExceptRulePageOpen('"+row.ruleCode+"')\"><%=actionUpdate.name %></a>&nbsp;"+
		<%}%>
		<%if(actionRemove !=null ){//角色判断%>
		"<a href=\"javascript:removePayRiskExceptRule('"+row.ruleCode+"')\"><%=actionRemove.name %></a>&nbsp;"+
		<%}%>
		"";
	 return tmp;
}
function searchPayRiskExceptRuleList(){
    $('#payRiskExceptRuleList').datagrid('load',{
          ruleName:$('#searchPayRiskExceptRuleRuleName').val(),
          ruleType:$('#searchPayRiskExceptRuleRuleType').combobox('getValue'),
          excpType:$('#searchPayRiskExceptRuleExcpType').combobox('getValue'),
          //comTypeNo:$('#searchPayRiskExceptRuleComTypeNo').combobox('getValue'),
          isUse:$('#searchPayRiskExceptRuleIsUse').combobox('getValue'),
          ruleLevelItem:$('#searchPayRiskExceptRuleRuleLevelItem').combobox('getValue')
    });  
}

// 更新规则启用禁用状态
function updatePayRiskExceptRuleStatus(url,ruleCode,columName,value,info){
    $.messager.confirm('提示', info, function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/'+url,
            {ruleCode:ruleCode,columName:columName,value:value},
            function(data){
                $('#payRiskExceptRuleList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功!');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'text'
        );
    }catch(e){alert(e);}
   });
}
function addPayRiskExceptRulePageOpen(){
    openTab('addPayRiskExceptRulePage',payRiskExceptRuleAddPageTitle,'<%=path %>/jsp/pay/risk/pay_risk_except_rule_add.jsp');
}
function detailPayRiskExceptRulePageOpen(ruleCode){
    openTab('detailPayRiskExceptRulePage',payRiskExceptRuleDetailPageTitle,'<%=path %>/detailPayRiskExceptRule.htm?ruleCode='+ruleCode);
}
function updatePayRiskExceptRulePageOpen(ruleCode){
    openTab('updatePayRiskExceptRulePage',payRiskExceptRuleUpdatePageTitle,'<%=path %>/updatePayRiskExceptRule.htm?flag=show&ruleCode='+ruleCode);
}
function removePayRiskExceptRule(ruleCode){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#payRiskExceptRuleList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayRiskExceptRule.htm',
            {ruleCode:ruleCode},
            function(data){
                $('#payRiskExceptRuleList').datagrid('reload');
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
