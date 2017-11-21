<%@page import="com.PayConstant"%>
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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pD825F42Q3rKTP41"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("ACTION_ADD_ID"));
String clientType = request.getParameter("clientType");
String clientCode = request.getParameter("clientCode");
%>
<script type="text/javascript">
function formatPayRiskXListHistoryClientType(data,row,index){
	if(data=='<%=PayConstant.CUST_TYPE_USER %>'){
		return "个人";
	}else if(data=='<%=PayConstant.CUST_TYPE_MERCHANT %>'){
		return "商户";
	}else if(data=='<%=com.PayConstant.CUST_TYPE_MOBLIE %>'){
		return "手机号";
	}else if(data=='<%=com.PayConstant.CUST_TYPE_CARD %>'){
		return "银行卡号";
	}
}
function formatPayRiskXListHistoryXType(data,row,index){
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
var payRiskXListHistoryListPageTitle="X名单历史记录";
$(document).ready(function(){});
</script>
<table id="payRiskXListHistoryList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payRiskXListHistory.htm?flag=0',method:'post',toolbar:'#payRiskXListHistoryListToolBar'">
       <thead>
        <tr>
           <th field="clientType" width="25%" align="left" sortable="true" data-options="formatter:formatPayRiskXListHistoryClientType">客户类型</th>
           <th field="clientCode" width="25%" align="left" sortable="true">客户编码</th>
           <th field="regdtTime" width="25%" align="left" sortable="true">登记日期</th>
           <th field="xType" width="24%" align="left" sortable="true" data-options="formatter:formatPayRiskXListHistoryXType">名单类型</th>
       </tr>
       </thead>
</table>
<div id="payRiskXListHistoryListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    客户类型<select class="easyui-combobox" panelHeight="auto" id="searchPayRiskXListHistoryClientType" data-options="editable:false" 
      		 name="searchPayRiskXListHistoryClientType">
  	           <option value="" <%="".equals(clientType)?"selected":""%>>全部</option>
               <option value="<%=com.PayConstant.CUST_TYPE_USER %>" <%=com.PayConstant.CUST_TYPE_USER.equals(clientType)?"selected":""%>>个人</option>
               <option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>" <%=com.PayConstant.CUST_TYPE_MERCHANT.equals(clientType)?"selected":""%>>商户</option>
               <option value="<%=com.PayConstant.CUST_TYPE_MOBLIE %>" <%=com.PayConstant.CUST_TYPE_MOBLIE.equals(clientType)?"selected":""%>>手机号</option>
               <option value="<%=com.PayConstant.CUST_TYPE_CARD %>" <%=com.PayConstant.CUST_TYPE_CARD.equals(clientType)?"selected":""%>>银行卡号</option>
		</select>
    客户编码
    	<input type="text" id="searchPayRiskXListHistoryClientCode" name="searchPayRiskXListHistoryClientCode" class="easyui-textbox" 
    	value="<%=clientCode==null?"":clientCode %>"  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayRiskXListHistoryList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayRiskXListHistoryPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payRiskXListHistoryList').datagrid({
    onBeforeLoad: function(param){
		param.flag='0';
        <%if(clientType!=null){%>
        try{param.clientType=$('#searchPayRiskXListHistoryClientType').combobox('getValue');}catch(e){
        	param.clientType=document.getElementById("searchPayRiskXListHistoryClientType").value;}
        <%}%>
		<%if(clientCode!=null){%>
        param.clientCode=$('#searchPayRiskXListHistoryClientCode').val();
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function searchPayRiskXListHistoryList(){
    $('#payRiskXListHistoryList').datagrid('load',{
          clientType:$('#searchPayRiskXListHistoryClientType').combobox('getValue'),
          clientCode:$('#searchPayRiskXListHistoryClientCode').val()
    });  
}
</script>
