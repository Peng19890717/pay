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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pGeJW0d2Q3rKTOI3"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pGeJW0d2Q3rKTOI4"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pGeJW0d2Q3rKTOI6"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pGeJW0d2Q3rKTOI5"));
%>
<script type="text/javascript">
var payAccEntryListPageTitle="会计分录维护";
var payAccEntryAddPageTitle="添加";
var payAccEntryUpdatePageTitle="修改";
$(document).ready(function(){});
</script>
<table id="payAccEntryList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payAccEntry.htm?flag=0',method:'post',toolbar:'#payAccEntryListToolBar'">
       <thead>
        <tr>
           <th field="txnCod" width="11%" align="left" sortable="true">交易码</th>
           <th field="txnSubCod" width="11%" align="left" sortable="true">交易子码</th>
           <th field="accSeq" width="12%" align="left" sortable="true">记账序号</th>
           <th field="drCrFlag" width="9%" align="left" sortable="true" formatter="drCrFlag_formatter">借贷方向</th>
           <th field="subjectFrom" width="16%" align="left" sortable="true" formatter="subjectFrom_formatter">科目来源</th>
           <th field="glName" width="11%" align="left" sortable="true">科目</th>
           <th field="accOrgNo" width="10%" align="left" sortable="true" formatter="accOrgNo_formatter">财务机构</th>
           <th field="rmkCod" width="8%" align="left" sortable="true" formatter="rmkCod_formatter">交易类型</th>
           <th field="operation" data-options="formatter:formatPayAccEntryOperator" width="11%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payAccEntryListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    交易码<input type="text" id="searchPayAccEntryTxnCod" name="searchPayAccEntryTxnCod" class="easyui-textbox" value=""  style="width:130px"/>
    交易子码<input type="text" id="searchPayAccEntryTxnSubCod" name="searchPayAccEntryTxnSubCod" class="easyui-textbox" value=""  style="width:130px"/>
    记账序号<input type="text" id="searchPayAccEntryAccSeq" name="searchPayAccEntryAccSeq" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayAccEntryList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayAccEntryPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payAccEntryList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayAccEntryOperator(val,row,index){
     var tmp=
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayAccEntryPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayAccEntry('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayAccEntryList(){
    $('#payAccEntryList').datagrid('load',{
          txnCod:$('#searchPayAccEntryTxnCod').val(),
          txnSubCod:$('#searchPayAccEntryTxnSubCod').val(),
          accSeq:$('#searchPayAccEntryAccSeq').val()
    });  
}
function addPayAccEntryPageOpen(){
    openTab('addPayAccEntryPage',payAccEntryAddPageTitle,'<%=path %>/jsp/pay/accentry/pay_acc_entry_add.jsp');
}
function detailPayAccEntryPageOpen(txnCod){
    openTab('detailPayAccEntryPage',payAccEntryDetailPageTitle,'<%=path %>/detailPayAccEntry.htm?txnCod='+txnCod);
}
function updatePayAccEntryPageOpen(id){
    openTab('updatePayAccEntryPage',payAccEntryUpdatePageTitle,'<%=path %>/updatePayAccEntry.htm?flag=show&id='+id);
}
function removePayAccEntry(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayAccEntry.htm',
            {id:id},
            function(data){
                $('#payAccEntryList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
function drCrFlag_formatter(data,row,index){
	
	if(data=="D")
    {
		return "借方";
    }
	if(data=="C")
    {
    	return "贷方";
    }
	
}
function subjectFrom_formatter(data,row,index){
	
	if(data=="0")
    {
		return "根据分录操作账户消费金额";
    }
	if(data=="1")
    {
    	return "根据分录操作账户现金金额";
    }
   
	if(data=="2")
    {
    	return "根据分录操作账户现金+冻结";
    }
  
if(data=="3")
    {
    	return "根据分录操作科目";
    }
   
if(data=="4")
    {
    	return "根据输入操作科目";
    }
   
if(data=="5")
    {
    	return "根据分录批量操作账户现金";
    }
   
if(data=="6")
    {
    	return "根据分录批量操作账户消费";
    }
    
if(data=="7")
    {
    	return "根据输入批量操作科目";
    }
	
}
function accOrgNo_formatter(data,row,index){
	
	if(data=="A")
    {
		return "开户机构";
    }
if(data=="T")
    {
    	return "交易机构";
    }
	
}
function rmkCod_formatter(data,row,index){
	
	if(data=="1")
    {
		return "消费";
    }
if(data=="2")
    {
    	return "充值";
    }
    
if(data=="3")
    {
    	return "结算";
    }
   
if(data=="4")
    {
    	return "退款";
    }
 
 if(data=="5")
    {
    	return "提现";
    }
   
 if(data=="6")
    {
    	return "转账";
    }
   
if(data=="7")
    {
    	return "对账";
    }
  
 if(data=="8")
    {
    	return "综合记账";
    }
   
if(data=="9")
    {
    	return "错账挂账";
    }
 
 if(data=="a")
    {
    	return "商户保证金";
    }
}

</script>
