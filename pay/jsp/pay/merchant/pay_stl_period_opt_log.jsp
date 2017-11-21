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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J9J7MJTY3JLXPT2PW2"));
%>
<script type="text/javascript">
var payStlPeriodOptLogListPageTitle="结算周期调整日志";
$(document).ready(function(){});
function formatPayMerchantSettlementStlPeriod(data,row, index){
	if(data=="D")return "日结";
	else if(data=="W")return "周结";
	else if(data=="M")return "月结";
	else return "";
}
function formatPayMerchantSettlementStlType(data,row, index){
	if(data=="0")return "交易";
	else if(data=="1")return "代理";
	else if(data=="2")return "代收";
	else return "";
}
</script>
<table id="payStlPeriodOptLogList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payStlPeriodOptLog.htm?flag=0',method:'post',toolbar:'#payStlPeriodOptLogListToolBar'">
       <thead>
        <tr>
           <th field="id" width="12%" align="left">编号</th>
           <th field="merNo" width="12%" align="left" >商户号</th>
           <th field="accBalance" width="12%" align="left">余额</th>
           <th field="frozenAmt" width="12%" align="left">冻结金额</th>
           <th field="custSetPeriodOld" width="12%" align="left"  data-options="formatter:formatPayMerchantSettlementStlPeriod">结算方式</th>
           <th field="stlType" width="12%" align="left"  data-options="formatter:formatPayMerchantSettlementStlType">结算周期类型</th>
           <th field="custStlTimeSetOld" width="12%" align="left">结算周期</th>
           <th field="createTime" width="12%" align="left">切换时间</th>
       </tr>
       </thead>
</table>
<div id="payStlPeriodOptLogListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    商户号<input type="text" id="searchPayStlPeriodOptLogMerNo" name="searchPayStlPeriodOptLogMerNo" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayStlPeriodOptLogList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payStlPeriodOptLogList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function searchPayStlPeriodOptLogList(){
    $('#payStlPeriodOptLogList').datagrid('load',{
          merNo:$('#searchPayStlPeriodOptLogMerNo').val()
    });  
}
</script>
