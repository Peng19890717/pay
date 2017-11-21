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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pXRUqXg2Q3rKTP21"));
JWebAction actionFundinFundoutDetail = ((JWebAction)user.actionMap.get("pY3KlU52Q3rKTP51"));
%>
<script type="text/javascript">
var payFundinFundoutListPageTitle="转入转出列表";
function format_application(data,row, index){
	if(data=="1"){
		return "转入";
	}else if(data=="2"){
		return "转出";
	}
}
function format_custType(data,row, index){
	if(data=="0"){
		return "个人";
	}else if(data=="1"){
		return "商户";
	}
}
$(document).ready(function(){});
</script>
<table id="payFundinFundoutList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payFundinFundout.htm?flag=0',method:'post',toolbar:'#payFundinFundoutListToolBar'">
       <thead>
        <tr>
           <th field="application" width="12%" align="left" sortable="true" formatter="format_application">资金流向</th>
           <th field="userid" width="12%" align="left" sortable="true">客户编号</th>
           <th field="custType" width="12%" align="left" sortable="true" formatter="format_custType">客户类型</th>
           <th field="orderno" width="12%" align="left" sortable="true">订单号</th>
           <th field="amt" width="15%" align="left" sortable="true">金额</th>
           <th field="remark" width="14%" align="left" sortable="true">备注</th>
           <th field="timestamp" width="13%" align="left" sortable="true">创建时间</th>
           <th field="operation" data-options="formatter:formatPayFundinFundoutListOperator" width="10%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payFundinFundoutListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    客户类型<select class="easyui-combobox" panelHeight="auto" id="searchPayFundinFundoutCustType" 
			data-options="editable:false" name="searchPayFundinFundoutCustType">
	    		   <option value="">请选择</option>
	               <option value="0">个人</option>
	               <option value="1">商户</option>
	    </select>
    客户编号<input type="text" id="searchPayFundinFundoutUserid" name="searchPayFundinFundoutUserid" class="easyui-textbox" value=""  style="width:130px"/>
    资金流向<select class="easyui-combobox" panelHeight="auto" id="searchPayFundinFundoutApplication" 
			data-options="editable:false" name="searchPayFundinFundoutApplication">
	    		   <option value="">请选择</option>
	               <option value="1">转入</option>
	               <option value="2">转出</option>
	    </select>
    订单号<input type="text" id="searchPayFundinFundoutOrderno" name="searchPayFundinFundoutOrderno" class="easyui-textbox" value=""  style="width:130px"/>
    创建时间
    <input class="easyui-datebox" style="width:100px"  data-options="editable:false" id="searchPayFundinFundoutTimestampStart" value="" name="searchPayOrderCreateTimeStart"/>
 		~<input class="easyui-datebox" style="width:100px" data-options="editable:false" id="searchPayFundinFundoutTimestampEnd" name="searchPayFundinFundoutTimestampEnd" value=""/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayFundinFundoutList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payFundinFundoutList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayFundinFundoutListOperator(val,row,index){
    var tmp=
       <%if(actionFundinFundoutDetail != null){//角色判断%>
    	   "<a href=\"javascript:checkListPayFundinFundoutWindowOpen('"+row.userid+"')\"><%=actionFundinFundoutDetail.name %></a>&nbsp;"+
       <%}%>
       "";
    return tmp;
}
//跳到错帐列表页面 
function checkListPayFundinFundoutWindowOpen(userId){
	var tabTmp = $('#mainTabs'),title='用户列表';
	/**当前的tab 是否存在如果存在就 将其关闭**/  
	if(tabTmp.tabs('exists', title))$('#mainTabs').tabs('close',title);
    /**添加一个tab标签**/
	tabTmp.tabs('add',{id:'userInfoList',title: title,selected: true,closable: true,border:false});
    $('#userInfoList').panel('refresh','<%=path %>/jsp/pay/user/pay_tran_user_info.jsp?userId='+userId);
}
function searchPayFundinFundoutList(){
    $('#payFundinFundoutList').datagrid('load',{
          merchantid:$('#searchPayFundinFundoutMerchantid').val(),
          application:$('#searchPayFundinFundoutApplication').combobox('getValue'),
          custType:$('#searchPayFundinFundoutCustType').combobox('getValue'),
          userid:$('#searchPayFundinFundoutUserid').val(),
          orderno:$('#searchPayFundinFundoutOrderno').val(),
          timestampStart:$('#searchPayFundinFundoutTimestampStart').datebox('getValue'),
          timestampEnd:$('#searchPayFundinFundoutTimestampEnd').datebox('getValue')
    });  
}
</script>
