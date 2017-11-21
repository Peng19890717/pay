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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pvH1Mla2Q3rKTOO1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pvHm3XU2Q3rKTOI1"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pvLeHFy2Q3rKTOO1"));
JWebAction actionAppend = ((JWebAction)user.actionMap.get("pw911ww2Q3rKTOZ1"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pvLj5sZ2Q3rKTP21"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("ACTION_UPDATE_ID"));
%>
<script type="text/javascript">
var payMarginListPageTitle="保证金管理";
var payMarginAddPageTitle="添加保证金";
var payMarginDetailPageTitle="保证金详情";
var payMarginUpdatePageTitle="修改保证金";
var payMarginAppendPageTitle="扣缴保证金";
$(document).ready(function(){});
function format_marginSign(data,row,index) {
	if(data=="0"){
		return "无";
	} else if(data=="1"){
		return "有";
	}
}
function format_marginPayType(data,row,index) {
	if(data=="0"){
		return "一次性付清";
	} else if(data=="1"){
		return "部分从交易中收取";
	} else if(data=="2"){
		return "完全从交易中收取";
	}
}
</script>
<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<table id="payMarginList" style="width:100%;height:100%" rownumbers="true" pagination="true"
		    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
		        %>/payMargin.htm?flag=0',method:'post',toolbar:'#payMarginListToolBar'">
		       <thead>
		        <tr>
		           <th field="custId" width="14%" align="left" sortable="true">客户编号</th>
		           <th field="storeName" width="20%" align="left" sortable="true">客户名称</th>
		           <!-- th field="pactNo" width="17%" align="left" sortable="true">合同编号</th -->
		           <th field="paidInAmt" width="16%" align="left" sortable="true">现有金额（元）</th>
		           <th field="lstUptTime" width="20%" align="left" sortable="true">创建时间</th>
		           <th field="mark" width="20%" align="left" sortable="true">备注</th>
		           <th field="operation" data-options="formatter:formatPayMarginOperator" width="9%" align="left">操作</th>
		       </tr>
		       </thead>
		</table>
 	</div>
	<div id="payMarginListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
		客户编号<input type="text" id="searchPayMarginCustId" name="searchPayMarginCustId" class="easyui-textbox" value=""  style="width:100px"/>&nbsp;&nbsp;&nbsp;
		客户名称<input type="text" id="searchPayMarginStoreName" name="searchPayMarginStoreName" class="easyui-textbox" value=""  style="width:100px"/>&nbsp;&nbsp;&nbsp;
	       <!-- 合同编号<input type="text" id="searchPayMarginPactNo" name="searchPayMarginPactNo" class="easyui-textbox" value=""  style="width:100px"/>&nbsp;&nbsp;&nbsp; -->
	        修改日期<input type="text" id="searchLstUptTimeStart" name="lstUptTimeStart" class="easyui-datebox" value=""  style="width:100px" editable="fasle"/>~
	    	     <input type="text" id="searchLstUptTimeEnd" name="lstUptTimeEnd" class="easyui-datebox" value=""  style="width:100px" editable="fasle"/>
		<%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchPayMarginList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
	    <%} %>
	    <%if(actionAdd != null){//角色判断%>
	    <a href="javascript:addPayMarginPageOpen()" class="easyui-linkbutton"
	        iconCls="icon-add"><%=actionAdd.name %></a>
	    <%} %>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<span id="payMarginGetTotalMoneyId">&nbsp;</span>
	</div>
<script type="text/javascript">
$('#payMarginList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#payMarginGetTotalMoneyId").html("保证金总金额："+(parseFloat(data.totalMarginMoney)*0.01).toFixed(2)
            	+"元");
    }
});
function formatPayMarginOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayMarginPageOpen('"+row.seqNo+"')\"><%=actionDetail.name %></a>&nbsp;&nbsp;"+
        <%}%>
         <%if(actionAppend != null){//角色判断%>
        "<a href=\"javascript:appentPayMarginPageOpen('"+row.seqNo+"')\"><%=actionAppend.name %></a>&nbsp;&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayMarginPageOpen('"+row.seqNo+"')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayMargin('"+row.seqNo+"','"+row.marginAmt+"','"+row.paidInAmt+"')\"><%=actionRemove.name %></a>&nbsp;&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayMarginList(){
    $('#payMarginList').datagrid('load',{
          custId:$('#searchPayMarginCustId').val(),//商户编号
          storeName:$('#searchPayMarginStoreName').val(),//商户名称
          pactNo:$('#searchPayMarginPactNo').val(),//合同编号
          lstUptTimeStart:$('#searchLstUptTimeStart').datebox('getValue'),//修改日期开始
          lstUptTimeEnd:$('#searchLstUptTimeEnd').datebox('getValue')
    });
}
function addPayMarginPageOpen(){
    openTab('addPayMarginPage',payMarginAddPageTitle,'<%=path %>/jsp/pay/margin/pay_margin_add.jsp');
}
function detailPayMarginPageOpen(seqNo){
    openTab('detailPayMarginPage',payMarginDetailPageTitle,'<%=path %>/detailPayMargin.htm?seqNo='+seqNo);
}
function updatePayMarginPageOpen(seqNo){
    openTab('updatePayMarginPage',payMarginUpdatePageTitle,'<%=path %>/updatePayMargin.htm?flag=show&seqNo='+seqNo);
}
function appentPayMarginPageOpen(seqNo){
	openTab('appentPayMarginPage',payMarginAppendPageTitle,'<%=path %>/appendPayMargin.htm?flag=show&seqNo='+seqNo);
}
function removePayMargin(seqNo,marginAmt,paidInAmt){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
	    if(parseFloat(paidInAmt)==0){
	        $.post('<%=path %>/removePayMargin.htm',
	            {seqNo:seqNo},
	            function(data){
	                $('#payMarginList').datagrid('reload');
	                if(data=='<%=JWebConstant.OK %>'){
	                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
	                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	            },
	           'json'
	        );
	    } else topCenterMessage('<%=JWebConstant.ERROR %>','保证金余额不为0！');
    }catch(e){alert(e);}
   });
}
</script>
