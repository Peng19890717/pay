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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J4QLYY4G3JLXPT2PW3"));
String channelId = request.getParameter("channelId");
String channelAcc = request.getParameter("channelAcc");
%>
<script type="text/javascript">
var payChannelRotationLogListPageTitle="渠道轮询日志";
$(document).ready(function(){});
function formatDay (data,row,index){
	return data.substring(0,4)+"-"+data.substring(4,6)+"-"+data.substring(6);
}
</script>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<table id="payChannelRotationLogList" style="width:100%;height:100%" rownumbers="true" pagination="true"
		    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
		        %>/payChannelRotationLog.htm?flag=0',method:'post',toolbar:'#payChannelRotationLogListToolBar'">
		       <thead>
		        <tr>
		           <th field="channelId" width="4%" align="left" sortable="true">渠道</th>
		           <th field="channelAcc" width="6%" align="left" sortable="true">渠道账号</th>
		           <th field="day" width="6%" align="left" formatter="formatDay">日期</th>
		           <th field="dayAmt" width="5%" align="left">总额</th>
		           <th field="daySuccessAmt" width="5%" align="left">成功额</th>
		           <th field="dayCount" width="5%" align="left">总笔数</th>
		           <th field="daySuccessCount" width="5%" align="left">成功笔数</th>
		           <th field="dayCountZfbScan" width="5%" align="left">支付宝笔数</th>
		           <th field="dayCountWeixinScan" width="5%" align="left">微信笔数</th>
		           <th field="dayCountQqScan" width="5%" align="left">QQ笔数</th>
		           <th field="daySucCountZfbScan" width="5%" align="left">支付宝成功笔数</th>
		           <th field="daySucCountWeixinScan" width="5%" align="left">微信成功笔数</th>
		           <th field="daySucCountQqScan" width="5%" align="left">QQ成功笔数</th>
		           <th field="dayAmtZfbScan" width="5%" align="left">支付宝额</th>
		           <th field="dayAmtWeixinScan" width="5%" align="left">微信额</th>
		           <th field="dayAmtQqScan" width="5%" align="left">QQ额</th>
		           <th field="daySucAmtZfbScan" width="5%" align="left">支付宝成功额</th>
		           <th field="daySucAmtWeixinScan" width="5%" align="left">微信成功额</th>
		           <th field="daySucAmtQqScan" width="5%" align="left">QQ成功额</th>
		           <th field="channelFee" width="5%" align="left">渠道费</th>
		       </tr>
		      </thead>
			</table>
		</div>
	<div id="payChannelRotationLogListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	    渠道号<input type="text" id="searchPayChannelRotationLogChannelId" name="searchPayChannelRotationLogChannelId" class="easyui-textbox" value="<%=channelId==null?"":channelId %>"  style="width:130px"/>
	    使用日期<input type="text" id="searchPayChannelRotationLogDay" name="searchPayChannelRotationLogDay" class="easyui-datebox" value=""  style="width:130px"/>
	    渠道账号<input type="text" id="searchPayChannelRotationLogChannelAcc" name="searchPayChannelRotationLogChannelAcc" class="easyui-textbox" value="<%=channelAcc==null?"":channelAcc %>"  style="width:130px"/>
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchPayChannelRotationLogList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
	    <%} %>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<%if(actionSearch != null){//角色判断%>
		<a href="javascript:payChannelRotationLogExportExcel()"  class="easyui-linkbutton" iconCls="icon-config">excel导出</a>
		<%} %>
		<span id="getChannelRotationLogTotalMoneyId">&nbsp;</span>
	</div>
</div>
<script type="text/javascript">
$('#payChannelRotationLogList').datagrid({
	onBeforeLoad: function(param){
		param.flag='0';
		<%if(channelId!=null){%>
        param.channelId=$('#searchPayChannelRotationLogChannelId').val();
        <%}%>
		<%if(channelAcc!=null){%>
        param.channelAcc=$('#searchPayChannelRotationLogChannelAcc').val();
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#getChannelRotationLogTotalMoneyId").html("总额汇总："+(parseFloat(data.totalDayAmt)*0.01).toFixed(2)
        	+"元，成功总额汇总："+(parseFloat(data.totalDaySuccessAmt)*0.01).toFixed(2)+"元，总比数汇总："
        	+(data.totalDayCount)+"笔，成功总比数汇总："
        	+(data.totalDaySuccessCount)+"笔，渠道费汇总："+(parseFloat(data.totalChannelFee)*0.01).toFixed(2)+"元");
    }
});
function searchPayChannelRotationLogList(){
    $('#payChannelRotationLogList').datagrid('load',{
          channelId:$('#searchPayChannelRotationLogChannelId').val(),
          day:$('#searchPayChannelRotationLogDay').datebox('getValue').replace("-","").replace("-",""),
          channelAcc:$('#searchPayChannelRotationLogChannelAcc').val()
    });  
}
	//导出Excel
	function payChannelRotationLogExportExcel() {
		var channelId = $("#searchPayChannelRotationLogChannelId").val();//渠道号
		var day = $("#searchPayChannelRotationLogDay").datebox('getValue').replace("-","");//使用日期
		var channelAcc = $("#searchPayChannelRotationLogChannelAcc").val();//渠道账号
		$.messager.confirm('提示', '您确认将当前记录导出至excel吗？', function(r){
			if(!r)return;
			//$('#payChannelRotationLogList').datagrid('loading');
			try {
				window.location.href="<%=path %>/payChannelRotationLogExportExcel.htm?"+
					"channelId="+$.trim(channelId)+
					"&day="+$.trim(day)+
					"&channelAcc="+$.trim(channelAcc);
			}catch(e){alert(e);};
		});
	};
</script>