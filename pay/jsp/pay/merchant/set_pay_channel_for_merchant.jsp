<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("q4A8fff2Q3rKTOX1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("q4A8fff2Q3rKTOX2"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("q4A8fff2Q3rKTOX4"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("q4A8fff2Q3rKTOX3"));
%>
<script type="text/javascript">
var payMerchantChannelRelationListPageTitle="设置支付通道";
var payMerchantChannelRelationAddPageTitle="添加支付通道";
var payMerchantChannelRelationDetailPageTitle="payMerchantChannelRelation详情";
var payMerchantChannelRelationUpdatePageTitle="修改支付通道";
$(document).ready(function(){});
</script>
<table id="payMerchantChannelRelationList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/setPayChannelForMerchant.htm?flag=0',method:'post',toolbar:'#payMerchantChannelRelationListToolBar'">
       <thead>
        <tr>
           <th field="merno" width="30%" align="left" sortable="true">商户编号</th>
           <th field="storeName" width="30%" align="left" sortable="true">商户名称</th>
           <!-- <th field="channelId" width="20%" align="left" sortable="true">通道编号</th>
           <th field="channelName" width="20%" align="left" sortable="true">通道名称</th> -->
           <th field="operation" data-options="formatter:formatPayMerchantChannelRelationOperator" width="40%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payMerchantChannelRelationListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    商户编号<input type="text" id="searchPayMerchantChannelRelationMerno" name="searchPayMerchantChannelRelationMerno" class="easyui-textbox" value=""  style="width:130px"/>
    渠道<select  class="easyui-combobox" panelHeight="auto" id="searchPayMerchantChannelRelationChannelId" data-options="panelHeight:500,editable:false" name="searchPayMerchantChannelRelationChannelId">
 		<option value="">请选择</option>
  		<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
		PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
		<option value="<%=channel.bankCode %>"><%=channel.bankName %></option>
		<%  %>
	<%}%>
 </select>
    交易类型<select class="easyui-combobox" panelHeight="auto" id="searchPayMerchantChannelRelationTranType" data-options="editable:false" name="searchPayMerchantChannelRelationTranType">
 		<option value="">请选择</option>
  		<option value="7">网银借记卡</option>
		<option value="8">网银贷记卡</option>
		<option value="9">网银对公</option>
		<option value="10">微信扫码</option>
		<option value="11">快捷借记卡</option>
		<option value="12">快捷贷记卡</option>
		<option value="15">代收</option>
		<option value="16">代付</option>
		<option value="17">支付宝扫码</option>
		<option value="18">微信WAP</option>
		<option value="27">QQ扫码</option>
	</select>
	特定代付商户<select class="easyui-combobox" panelHeight="auto" id="searchPayOpenedMerchantPayFlag" data-options="editable:false" name="searchPayOpenedMerchantPayFlag">
 		<option value="">请选择</option>
  		<option value="0">是</option>
	</select>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayMerchantChannelRelationList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayMerchantChannelRelationPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payMerchantChannelRelationList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayMerchantChannelRelationOperator(val,row,index){
     var tmp=
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayMerchantChannelRelationPageOpen('"+row.merno+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayMerchantChannelRelation('"+row.merno+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayMerchantChannelRelationList(){
    $('#payMerchantChannelRelationList').datagrid('load',{
          merno:$('#searchPayMerchantChannelRelationMerno').val(),
          channelId:$('#searchPayMerchantChannelRelationChannelId').combobox('getValue'),
          tranType:$('#searchPayMerchantChannelRelationTranType').combobox('getValue'),
          openedMerchantPayFlag:$('#searchPayOpenedMerchantPayFlag').combobox('getValue')
    });  
}
function addPayMerchantChannelRelationPageOpen(){
    openTab('addPayMerchantChannelRelationPage',payMerchantChannelRelationAddPageTitle,'<%=path %>/jsp/pay/merchant/add_pay_channel_for_merchant.jsp');
}
function updatePayMerchantChannelRelationPageOpen(merno){
    openTab('updatePayMerchantChannelRelationPage',payMerchantChannelRelationUpdatePageTitle,'<%=path %>/updatePayChannelForMerchant.htm?flag=show&merno='+merno);
}
function removePayMerchantChannelRelation(merno){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayMerchantChannelRelation.htm',
            {merno:merno},
            function(data){
                $('#payMerchantChannelRelationList').datagrid('reload');
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
