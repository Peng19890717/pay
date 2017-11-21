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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J5D6IU0S3JLXPT2QH1"));
%>
<script type="text/javascript">
var payTranSummaryListPageTitle="手续费列表";
$(document).ready(function(){});

</script>

<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<table id="payTranSummaryList" style="width:100%;height:100%" rownumbers="true"
		    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
		        %>/payTranSummary.htm?flag=0',method:'post',toolbar:'#payTranSummaryListToolBar'">
		       <thead>
		        <tr>
		           <th field="channelNo" width="10%" align="left">渠道编号</th>
		           <th field="channelName" width="10%" align="left">渠道名称</th>
		           <th field="tranTotalFee" width="10%" align="left">交易总额</th>
		           <th field="channelFee" width="10%" align="left">渠道手续费</th>
		           <th field="settlementAmt" width="10%" align="left">渠道结算额</th>
		       </tr>
		       </thead>
		</table>
	</div>
	<div id="payTranSummaryListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	    交易时间&nbsp;<input class="easyui-datebox" style="width:100px" id="searchpayTranSummaryCreateTimeStart" name="searchpayTranSummaryCreateTimeStart" data-options="editable:false" />
	    ~
    	<input class="easyui-datebox" style="width:100px" id="searchpayTranSummaryCreateTimeEnd" name="searchpayTranSummaryCreateTimeEnd" data-options="editable:false" />
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchpayTranSummaryList()" class="easyui-linkbutton" iconCls="icon-search">&nbsp;&nbsp;<%=actionSearch.name %></a>
	    <%} %>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<span id="payTranSummaryGetTotalMoneyId">&nbsp;</span>
	</div>
</div>



<script type="text/javascript">
$('#payTranSummaryList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
    }
});
function searchpayTranSummaryList(){
    $('#payTranSummaryList').datagrid('load',{
          tranTime:$('#searchpayTranSummaryCreateTimeStart').datebox('getValue'),
          tranTimeEnd:$('#searchpayTranSummaryCreateTimeEnd').datebox('getValue')
    });  
}
</script>
