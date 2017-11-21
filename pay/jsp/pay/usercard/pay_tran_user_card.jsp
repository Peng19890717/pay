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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pLljT0u2Q3rKTOM1"));
%>
<script type="text/javascript">
var payTranUserCardListPageTitle="银行卡绑定列表";
$(document).ready(function(){});
</script>
<table id="payTranUserCardList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payTranUserCard.htm?flag=0',method:'post',toolbar:'#payTranUserCardListToolBar'">
       <thead>
        <tr>
           <th field="userId" width="8%" align="left" sortable="true">用户ID</th>
           <th field="cardBank" width="8%" align="left" sortable="true">绑定银行卡所属行</th>
           <th field="cardType" width="8%" align="left" sortable="true" formatter="format_cardType">银行卡类型</th>
           <th field="cardStatus" width="8%" align="left" sortable="true" formatter="format_cardStatus">绑定银行卡状态</th>
           <th field="cardNo" width="10%" align="left" sortable="true" formatter="hideBankCardNoFormatter">绑定银行卡卡号</th>
           <th field="cardBankBranch" width="8%" align="left" sortable="true">卡号所属支行</th>
           <th field="bakOpenTime" width="12%" align="left" sortable="true">绑定银行卡时间</th>
           <th field="bakOpenNum" width="8%" align="left" sortable="true">绑定银行卡次数</th>
           <th field="bakCloseTime" width="12%" align="left" sortable="true">解绑银行卡时间</th>
           <th field="bakCloseNum" width="8%" align="left" sortable="true">解绑银行卡次数</th>
           <th field="bakCloseRes" width="12%" align="left" sortable="true">解绑银行卡原因</th>
       </tr>
       </thead>
</table>
<div id="payTranUserCardListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    用户ID<input type="text" id="searchPayTranUserCardUserId" name="searchPayTranUserCardUserId" class="easyui-textbox" style="width:130px"/>
    绑定银行卡卡号<input type="text" id="searchPayTranUserCardCardNo" name="searchPayTranUserCardCardNo" class="easyui-textbox" style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayTranUserCardList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payTranUserCardList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function searchPayTranUserCardList(){
    $('#payTranUserCardList').datagrid('load',{
          userId:$('#searchPayTranUserCardUserId').val(),
          cardNo:$('#searchPayTranUserCardCardNo').val()
    });  
}
function format_cardStatus(data,row, index){
	if(data=="0"){
		return "绑定";
	}else if(data=="1"){
		return "解绑";
	}else if(data=="2"){
		return "无效";
	}
}
function format_cardType(data,row, index){
	if(data=="0"){
		return "借记卡";
	}else if(data=="1"){
		return "信用卡";
	}
}
</script>
