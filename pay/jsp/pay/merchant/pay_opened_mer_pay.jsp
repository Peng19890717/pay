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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J75MGBJR3JLXPT2Q51"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("J75MGBJR3JLXPT2Q52"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("J75JTA5R3JLXPT2QH1"));
%>
<script type="text/javascript">
var payOpenedMerPayListPageTitle="特定商户代付";
var payOpenedMerPayAddPageTitle="添加代付商户";
var payOpenedMerPayUpdatePageTitle="修改代付商户";
$(document).ready(function(){});
function formatPayOpenedMerPayType (data,row,index){
	if(data=="16")return "代付";
	else if(data=="1")return "消费";
	else if(data=="2")return "充值";
	else if(data=="3")return "结算";
	else if(data=="4")return "退款";
	else if(data=="6")return "转账";
	else if(data=="6")return "提现";
	else if(data=="7")return "消费B2C借记卡";
	else if(data=="8")return "消费B2C信用卡";
	else if(data=="9")return "消费B2B";
	else if(data=="10")return "微信扫码";
	else if(data=="11")return "消费快捷";
	else if(data=="12")return "其他消费";
	else if(data=="13")return "代理";
	else if(data=="14")return "代理商户";
	else if(data=="15")return "代收";
	else if(data=="17")return "支付宝扫码";
	else if(data=="18")return "微信WAP";
}
</script>
<table id="payOpenedMerPayList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payOpenedMerPay.htm?flag=0',method:'post',toolbar:'#payOpenedMerPayListToolBar'">
       <thead>
        <tr>
           <th field="merno" width="25%" align="left">商户号</th>
           <th field="type" width="25%" align="left"  formatter="formatPayOpenedMerPayType">交易类型</th>
           <th field="createtime" width="25%" align="left">创建时间</th>
           <th field="operation" data-options="formatter:formatPayOpenedMerPayOperator" width="24%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payOpenedMerPayListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    商户号<input type="text" id="searchPayOpenedMerPayMerno" name="searchPayOpenedMerPayMerno" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayOpenedMerPayList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayOpenedMerPayPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payOpenedMerPayList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayOpenedMerPayOperator(val,row,index){
     var tmp=
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayOpenedMerPay('"+row.merno+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayOpenedMerPayList(){
    $('#payOpenedMerPayList').datagrid('load',{
          merno:$('#searchPayOpenedMerPayMerno').val()
    });  
}
function addPayOpenedMerPayPageOpen(){
    openTab('addPayOpenedMerPayPage',payOpenedMerPayAddPageTitle,'<%=path %>/jsp/pay/merchant/pay_opened_mer_pay_add.jsp');
}
function detailPayOpenedMerPayPageOpen(merno){
    openTab('detailPayOpenedMerPayPage',payOpenedMerPayDetailPageTitle,'<%=path %>/detailPayOpenedMerPay.htm?merno='+merno);
}
function updatePayOpenedMerPayPageOpen(merno){
    openTab('updatePayOpenedMerPayPage',payOpenedMerPayUpdatePageTitle,'<%=path %>/updatePayOpenedMerPay.htm?flag=show&merno='+merno);
}
function removePayOpenedMerPay(merno){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayOpenedMerPay.htm',
            {merno:merno},
            function(data){
                $('#payOpenedMerPayList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
</script>
