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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pVw3cKt2Q3rKTOI1"));
%>
<script type="text/javascript">
var payChargeLogListPageTitle="手续费列表";
$(document).ready(function(){});
function formatPayChargeLogCustType(data,row,index){
	if(data=='<%=com.PayConstant.CUST_TYPE_USER %>'){
		return "个人";
	}else if(data=='<%=com.PayConstant.CUST_TYPE_MERCHANT %>'){
		return "商户";
	}else if(data=='<%=com.PayConstant.CUST_TYPE_PAY_CHANNEL %>'){
		return "支付渠道";
	}
}
function formatPayChargeLogChargeType(data,row,index){
	if(data=='<%=com.PayConstant.TRAN_SETTLRMENT %>'){
		return "交易结算";
	}else if(data=='<%=com.PayConstant.CHARER_BEHALF %>'){
		return "代收";
	}else if(data=='<%=com.PayConstant.PAY_FEE_BEHALF %>'){
		return "代付";
	}
}
</script>

<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<table id="payChargeLogList" style="width:100%;height:100%" rownumbers="true" pagination="true"
		    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
		        %>/payChargeLog.htm?flag=0',method:'post',toolbar:'#payChargeLogListToolBar'">
		       <thead>
		        <tr>
		           <th field="custType" width="5%" align="left" sortable="true" data-options="formatter:formatPayChargeLogCustType">客户类型</th>
		           <th field="custId" width="5%" align="left" sortable="true">客户ID</th>
		           <th field="custName" width="10%" align="left" sortable="true">客户名称</th>
		           <th field="amt" width="5%" align="left" sortable="true">金额</th>
		           <th field="curStorageFee" width="5%" align="left">手续费余额</th>
		           <th field="chargeType" width="15%" align="left" sortable="true" data-options="formatter:formatPayChargeLogChargeType">手续费类型</th>
		           <th field="createTime" width="15%" align="left" sortable="true">创建时间</th>
		           <th field="settleTime" width="15%" align="left" sortable="true">结算时间</th>
		           <th field="remark" width="25%" align="left" sortable="true">备注</th>
		       </tr>
		       </thead>
		</table>
	</div>
	<div id="payChargeLogListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	    客户类型&nbsp;<select class="easyui-combobox" panelHeight="auto" id="searchPayChargeLogCustType" data-options="editable:false" name="searchPayChargeLogCustType">
	  	           <option value="">全部</option>
	               <option value="<%=com.PayConstant.CUST_TYPE_USER %>">个人</option>
	               <option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>">商户</option>
	               <option value="<%=com.PayConstant.CUST_TYPE_PAY_CHANNEL %>">支付渠道</option>
			</select>&nbsp;&nbsp;
	    客户ID&nbsp;<input type="text" id="searchPayChargeLogCustId" name="searchPayChargeLogCustId" class="easyui-textbox" value=""  style="width:130px"/>&nbsp;&nbsp;
	    手续费类型&nbsp;<select class="easyui-combobox" panelHeight="auto" id="searchPayChargeLogChargeType" data-options="editable:false" name="searchPayChargeLogChargeType">
	  	           <option value="">全部</option>
	               <option value="<%=com.PayConstant.TRAN_SETTLRMENT %>">交易结算</option>
	               <option value="<%=com.PayConstant.CHARER_BEHALF %>">代收</option>
	               <option value="<%=com.PayConstant.PAY_FEE_BEHALF %>">代付</option>
			</select>&nbsp;&nbsp;
	    创建时间&nbsp;<input class="easyui-datebox" style="width:100px" id="searchPayChargeLogCreateTimeStart" name="searchPayChargeLogCreateTimeStart" data-options="editable:false" />~
				<input class="easyui-datebox" style="width:100px" id="searchPayChargeLogCreateTimeEnd" name="searchPayChargeLogCreateTimeEnd" data-options="editable:false" />
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchPayChargeLogList()" class="easyui-linkbutton" iconCls="icon-search">&nbsp;&nbsp;<%=actionSearch.name %></a>
	    <%} %>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<span id="payChargeLogGetTotalMoneyId">&nbsp;</span>
	</div>
</div>



<script type="text/javascript">
$('#payChargeLogList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#payChargeLogGetTotalMoneyId").html("总金额："+(parseFloat(data.totalChargeLogMoney)*0.01).toFixed(2)
            	+"元");
    }
});
function searchPayChargeLogList(){
    $('#payChargeLogList').datagrid('load',{
          custType:$('#searchPayChargeLogCustType').combobox('getValue'),
          custId:$('#searchPayChargeLogCustId').val(),
          chargeType:$('#searchPayChargeLogChargeType').combobox('getValue'),
          createTimeStart:$('#searchPayChargeLogCreateTimeStart').datebox('getValue'),
          createTimeEnd:$('#searchPayChargeLogCreateTimeEnd').datebox('getValue')
    });  
}
</script>
