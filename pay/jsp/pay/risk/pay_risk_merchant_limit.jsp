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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pCQhzU22Q3rKTOQ1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pCQhzU22Q3rKTOQ2"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pCQhzU22Q3rKTOQ5"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pCQhzU22Q3rKTOQ3"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pCQhzU22Q3rKTOQ4"));
%>
<script type="text/javascript">
var payRiskMerchantLimitListPageTitle="商户限额";
var payRiskMerchantLimitAddPageTitle="添加商户限额";
var payRiskMerchantLimitDetailPageTitle="商户限额详情";
var payRiskMerchantLimitUpdatePageTitle="修改商户限额";
$(document).ready(function(){});

// 状态类型
function limitType_formatter(data,row,index) {
	if(data=="1"){
		return "所有商户";
	} else if(data=="2"){
		return "指定商户 ";
	}
}

// 风险级别
function limitRiskLevel_formatter(data,row,index) {
	if(data=="0"){
		return "低";
	}else if(data=="1"){
		return "中 ";
	}else if(data=="2"){
		return "高 ";
	}else if(data=="000"){
		return "-";
	}
}

// 限额类型
function limitCompType_formatter(data,row,index) { 
	if(data=="1"){
		return "消费";
	}else if(data=="2"){
		return "充值";
	}else if(data=="3"){
		return "结算";
	}else if(data=="4"){
		return "退款";
	}else if(data=="5"){
		return "提现";
	}else if(data=="6"){
		return "转账";
	}else if(data=="15"){
		return "代收";
	}else if(data=="16"){
		return "代付";
	}
}

// 启用状态
function isUse_formatter(data,row,index) {
	if(data=="0"){
		return "未启用";
	}else if(data=="1"){
		return "启用 ";
	}
}

</script>
<table id="payRiskMerchantLimitList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payRiskMerchantLimit.htm?flag=0',method:'post',toolbar:'#payRiskMerchantLimitListToolBar'">
       <thead>
        <tr>
           <th field="limitType" width="8%" align="left" sortable="true" formatter="limitType_formatter">状态类型</th>
           <th field="limitCompCode" width="8%" align="left" sortable="true">客户编号</th>
           <th field="storeName" width="8%" align="left" sortable="true">客户名称</th>
           <th field="limitRiskLevel" width="8%" align="left" sortable="true" formatter="limitRiskLevel_formatter">风险级别</th>
           <th field="limitCompType" width="8%" align="left" sortable="true" formatter="limitCompType_formatter">限额类型</th>
           <th field="limitStartDate" width="10%" align="left" sortable="true">生效时间</th>
           <th field="limitEndDate" width="11%" align="left" sortable="true">失效时间</th>
           <th field="isUse" width="9%" align="left" sortable="true" formatter="isUse_formatter">启用状态</th>
           <th field="updateUser" width="9%" align="left" sortable="true">修改人</th>
           <th field="updateTime" width="11%" align="left" sortable="true">修改时间</th>
           <th field="operation" data-options="formatter:formatPayRiskMerchantLimitOperator" width="15%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payRiskMerchantLimitListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">

 状态类型<select class="easyui-combobox" data-options="editable:false" name="searchPayRiskMerchantLimitType" id="searchPayRiskMerchantLimitType">
    	<option value="">全部</option>
  		<option value="1">所有商户</option>
  		<option value="2">指定商户</option>
  	</select>

    启用状态<select class="easyui-combobox" data-options="editable:false" name="searchPayRiskMerchantLimitIsUse" id="searchPayRiskMerchantLimitIsUse">
    	<option value="">全部</option>
  		<option value="1">启用</option>
  		<option value="0">未启用</option>
  	</select>
    创建时间<input type="text" id="searchPayRiskMerchantLimitCreateTime" name="searchPayRiskMerchantLimitCreateTime" class="easyui-datebox" value=""  style="width:100px" data-options="editable:false"/>
    ~
    <input type="text" id="searchPayRiskMerchantLimitCreateTimeEnd" name="searchPayRiskMerchantLimitCreateTimeEnd" class="easyui-datebox" value=""  style="width:100px" data-options="editable:false"/>
    商户号<input type="text" id="searchPayRiskMerchantLimitLimitCompCode" name="searchPayRiskMerchantLimitLimitCompCode" class="easyui-textbox" value=""  style="width:130px"/>
    
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayRiskMerchantLimitList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayRiskMerchantLimitPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payRiskMerchantLimitList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayRiskMerchantLimitOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayRiskMerchantLimitPageOpen('"+row.id+"')\"><%=actionDetail.name %></a>&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayRiskMerchantLimitPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayRiskMerchantLimit('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayRiskMerchantLimitList(){
    $('#payRiskMerchantLimitList').datagrid('load',{
    	  limitType:$('#searchPayRiskMerchantLimitType').combobox('getValue'),
          isUse:$('#searchPayRiskMerchantLimitIsUse').combobox('getValue'),
          createTime:$('#searchPayRiskMerchantLimitCreateTime').datebox('getValue'),
          createTimeEnd:$('#searchPayRiskMerchantLimitCreateTimeEnd').datebox('getValue'),
          limitCompCode:$('#searchPayRiskMerchantLimitLimitCompCode').val()
    });  
}
function addPayRiskMerchantLimitPageOpen(){
    openTab('addPayRiskMerchantLimitPage',payRiskMerchantLimitAddPageTitle,'<%=path %>/jsp/pay/risk/pay_risk_merchant_limit_add.jsp');
}
function detailPayRiskMerchantLimitPageOpen(id){
    openTab('detailPayRiskMerchantLimitPage',payRiskMerchantLimitDetailPageTitle,'<%=path %>/detailPayRiskMerchantLimit.htm?id='+id);
}
function updatePayRiskMerchantLimitPageOpen(id){
    openTab('updatePayRiskMerchantLimitPage',payRiskMerchantLimitUpdatePageTitle,'<%=path %>/updatePayRiskMerchantLimit.htm?flag=show&id='+id);
}
function removePayRiskMerchantLimit(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayRiskMerchantLimit.htm',
            {id:id},
            function(data){
                $('#payRiskMerchantLimitList').datagrid('reload');
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
