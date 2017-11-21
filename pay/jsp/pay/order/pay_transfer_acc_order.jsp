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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pGP8D702Q3rKTOI2"));
%>
<script type="text/javascript">
var payTransferAccOrderListPageTitle="转账列表";
var payTransferAccOrderAddPageTitle="添加payTransferAccOrder";
var payTransferAccOrderDetailPageTitle="payTransferAccOrder详情";
var payTransferAccOrderUpdatePageTitle="修改payTransferAccOrder";
$(document).ready(function(){});
//订单状态格式化
function formatPayTransferAccOrderStatus(data,row,index) {
	if(data=="00") return "代付款";
	if(data=="01") return "转账成功";
	if(data=="02") return "转账失败";
	if(data=="03") return "预付款";
	if(data=="50") return "金额错误";
	if(data=="51") return "订单号重复";
	if(data=="52") return "收款方账号不存在";
	if(data=="53") return "不能给自己转账";
	if(data=="99") return "超时";
	return "";
}
//账户类型格式化
function formatPayTransferTgetAccType(data,row,index) {
	if(data=="<%=com.PayConstant.CUST_TYPE_USER %>") return "个人";
	if(data=="<%=com.PayConstant.CUST_TYPE_MERCHANT %>") return "商户";
	return "";
}
</script>
<div class="easyui-layout" data-options="fit:true">
	 <div data-options="region:'center'">
	<table id="payTransferAccOrderList" style="width:100%;height:100%" rownumbers="true" pagination="true"
	    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
	        %>/payTransferAccOrder.htm?flag=0',method:'post',toolbar:'#payTransferAccOrderListToolBar'">
	       <thead>
	        <tr>
	           <th field="tranOrdno" width="9%" align="left" sortable="true">订单号</th>
	           <th field="batTranCustOrdno" width="9%" align="left" sortable="true">商户订单号</th>
	           <th field="batNo" width="9%" align="left" sortable="true">商户批次号</th>
	           <th field="custId" width="8%" align="left" sortable="true">转出账号</th>
	           <th field="tranTime" width="8%" align="left" sortable="true">申请时间</th>
	           <th field="tranSuccessTime" width="9%" align="left" sortable="true">完成时间</th>
	           <th field="status" width="8%" align="left" sortable="true" data-options="formatter:formatPayTransferAccOrderStatus">订单状态</th>
	           <th field="txamt" width="6%" align="left" sortable="true">转账金额</th>
	           <th field="tgetAccNo" width="8%" align="left" sortable="true">收款帐号</th>
	           <th field="tgetAccName" width="8%" align="left" sortable="true" formatter="hideNameFormatter">收款名称</th>
	           <th field="tgetAccType" width="6%" align="left" sortable="true" data-options="formatter:formatPayTransferTgetAccType">收款账户类型</th>
	           <th field="fee" width="6%" align="left" sortable="true">手续费</th>
	           <th field="filed2" width="16%" align="left" sortable="true">备注</th>
			   <!--<th field="operation" data-options="formatter:formatPayTransferAccOrderOperator" width="15%" align="left">操作</th>-->
	       </tr>
	       </thead>
	</table>
	</div>
	<div id="payTransferAccOrderListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	    	订单号<input type="text" id="searchPayTransferAccOrderTranOrdno" name="searchPayTransferAccOrderTranOrdno" class="easyui-textbox" value=""  style="width:130px"/>
	    	商户订单号<input type="text" id="searchPayTransferAccOrderBatTranCustOrdno" name="searchPayTransferAccOrderTranOrdno" class="easyui-textbox" value=""  style="width:130px"/>
	    	商户批次号<input type="text" id="searchPayTransferAccOrderBatNo" name="searchPayTransferAccOrderTranOrdno" class="easyui-textbox" value=""  style="width:130px"/>
	    	客户账号<input type="text" id="searchPayTransferAccOrderCustId" name="searchPayTransferAccOrderCustId" class="easyui-textbox" value=""  style="width:130px"/>
	    	收款帐号<input type="text" id="searchPayTransferAccOrderTgetAccNo" name="searchPayTransferAccOrderTgetAccNo" class="easyui-textbox" value=""  style="width:130px"/>
	    	转账时间<input class="easyui-datebox" style="width:100px"  data-options="editable:false" id="searchPayTransferAccOrderTranTimeStart" name="searchPayTransferAccOrderTranTimeStart"/>
	 		~<input class="easyui-datebox" style="width:100px" data-options="editable:false" id="searchPayTransferAccOrderTranTimeEnd" name="searchPayTransferAccOrderTranTimeEnd"/>
	    	订单状态<select class="easyui-combobox" panelHeight="auto" id="searchPayTransferAccOrderStatus" 
						data-options="editable:false" name="searchPayTransferAccOrderStatus"  style="width:130px">
						 	   <option value="">请选择</option>
							   <option value="01">转账成功</option>
							   <option value="02">转账失败</option>
							   <option value="99">超时</option>
				    </select>
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchPayTransferAccOrderList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
	    <%} %>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<span id="getTotalTransferAccMoneyId">&nbsp;</span>
	</div>
</div>
<script type="text/javascript">
$('#payTransferAccOrderList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#getTotalTransferAccMoneyId").html("转账金额汇总："+(parseFloat(data.totalTxamt)*0.01).toFixed(2)
        	+"元，手续费汇总："+(parseFloat(data.totalTotamt-data.totalTxamt)*0.01).toFixed(2)+"元");
    }
});
function searchPayTransferAccOrderList(){
    $('#payTransferAccOrderList').datagrid('load',{
          tranOrdno:$('#searchPayTransferAccOrderTranOrdno').val(),
          batTranCustOrdno:$('#searchPayTransferAccOrderBatTranCustOrdno').val(),
          batNo:$('#searchPayTransferAccOrderBatNo').val(),
          custId:$('#searchPayTransferAccOrderCustId').val(),
          tgetAccNo:$('#searchPayTransferAccOrderTgetAccNo').val(),
          clearDate:$('#searchPayTransferAccOrderClearDate').val(),
          tranTimeStart:$('#searchPayTransferAccOrderTranTimeStart').datebox('getValue'),
          tranTimeEnd:$('#searchPayTransferAccOrderTranTimeEnd').datebox('getValue'),
          status:$('#searchPayTransferAccOrderStatus').combobox('getValue')
    });  
}
</script>
