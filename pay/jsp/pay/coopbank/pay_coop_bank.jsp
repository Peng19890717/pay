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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pxtMxAJ2Q3rKTOI1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pxtXTz02Q3rKTOJ1"));
JWebAction adjustChannelRouteRule = ((JWebAction)user.actionMap.get("pZt5P2I2Q3rKTP21"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pxtTim32Q3rKTOI1"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("ACTION_REMOVE_ID"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pxubFLn2Q3rKTOJ1"));
JWebAction actionUpdateStatus = ((JWebAction)user.actionMap.get("pxuuby92Q3rKTON1")); 
%>
<script type="text/javascript">
var payCoopBankListPageTitle="合作渠道维护";
var payCoopBankAddPageTitle="添加合作渠道";
var adjustChannelRouteRuleTitle="调整路由规则";
var payCoopBankDetailPageTitle="合作渠道详情";
var payCoopBankUpdatePageTitle="修改合作渠道";
$(document).ready(function(){});
</script>
<table id="payCoopBankList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payCoopBank.htm?flag=0',method:'post',toolbar:'#payCoopBankListToolBar'">
       <thead>
        <tr>
           <th field="bankCode" width="18%" align="left" sortable="true">银行编号</th>
           <th field="bankName" width="18%" align="left" sortable="true">银行名称</th>
           <th field="bankRelNo" width="18%" align="left" sortable="true">银行联行号 </th>
           <th field="bankStatus" width="18%" align="left" sortable="true" formatter="format_bankStatus">银行状态</th>
           <th field="trtBankFlg" width="17%" align="left" sortable="true" formatter="format_trtBankFlg">托管银行标志 </th>
           <th field="operation" data-options="formatter:formatPayCoopBankOperator" width="10%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payCoopBankListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    银行状态：
   		<select class="easyui-combobox" panelHeight="auto" id="searchPayCoopBankBankStatus" data-options="editable:false" name="searchPayCoopBankBankStatus">
  	           <option value="">请选择</option>
               <option value="0">正常</option>
               <option value="1">关闭</option>
		</select>
    银行编号：<input type="text" id="searchPayCoopBankBankCode" name="searchPayCoopBankBankCode" class="easyui-textbox" value=""  style="width:130px"/>
    银行名称：<input type="text" id="searchPayCoopBankBankName" name="searchPayCoopBankBankName" class="easyui-textbox" value=""  style="width:130px"/>
    托管银行标志：
    	<select class="easyui-combobox" panelHeight="auto" id="searchPayCoopBankTrtBankFlg" data-options="editable:false" name="searchPayCoopBankTrtBankFlg">
  	           <option value="">请选择</option>
               <option value="Y">是</option>
               <option value="N">否</option>
		</select>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayCoopBankList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayCoopBankPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
    <%if(adjustChannelRouteRule != null){//角色判断%>
    <a href="javascript:adjustChannelRouteRule()" class="easyui-linkbutton"
        iconCls="icon-config"><%=adjustChannelRouteRule.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payCoopBankList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function format_bankStatus(data,row,index) {
	if(data=="0"){
		return "正常";
	} else if(data=="1"){
		return "关闭";
	}
}
function format_trtBankFlg(data,row,index) {
	if(data=="Y"){
		return "是";
	} else if(data=="N"){
		return "否";
	}
}
function formatPayCoopBankOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayCoopBankPageOpen('"+row.bankCode+"')\"><%=actionDetail.name %></a>&nbsp;&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayCoopBankPageOpen('"+row.bankCode+"')\"><%=actionUpdate.name %></a>&nbsp;&nbsp;"+
        <%}%>
        <%if(actionUpdateStatus != null){//角色判断%>
        "<a href=\"javascript:updatePayCoopBankStatus('"+row.bankCode+"','"+(row.bankStatus=="0"?"1":"0")+"')\">"+(row.bankStatus=="0"?"关闭":"开启")+"</a>&nbsp;&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayCoopBank('"+row.bankCode+"')\"><%=actionRemove.name %></a>&nbsp;&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayCoopBankList(){
    $('#payCoopBankList').datagrid('load',{
          bankStatus:$('#searchPayCoopBankBankStatus').combobox('getValue'),
          bankCode:$('#searchPayCoopBankBankCode').val(),
          bankName:$('#searchPayCoopBankBankName').val(),
          trtBankFlg:$('#searchPayCoopBankTrtBankFlg').combobox('getValue')
    });  
}
function addPayCoopBankPageOpen(){
    openTab('addPayCoopBankPage',payCoopBankAddPageTitle,'<%=path %>/jsp/pay/coopbank/pay_coop_bank_add.jsp');
}
function adjustChannelRouteRule(){
    openTab('adjustChannelRouteRulePage',adjustChannelRouteRuleTitle,'<%=path %>/adjustChannelRule.htm');
}
function detailPayCoopBankPageOpen(bankCode){
    openTab('detailPayCoopBankPage',payCoopBankDetailPageTitle,'<%=path %>/detailPayCoopBank.htm?bankCode='+bankCode);
}
function updatePayCoopBankPageOpen(bankCode){
    openTab('updatePayCoopBankPage',payCoopBankUpdatePageTitle,'<%=path %>/updatePayCoopBank.htm?flag=show&bankCode='+bankCode);
}
// 更改关闭状态
function updatePayCoopBankStatus(bankCode,value){
    $.messager.confirm('提示',(value=='0'?'您确定要启用吗？':'您确定要关闭吗？'), function(r){
    if(!r)return;
    $('#payCoopBankList').datagrid('loading');
    try{
        $.post('<%=path %>/updatePayCoopBankStatus.htm',
            {bankCode:bankCode,value:value},
            function(data){
                $('#payCoopBankList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','操作成功!');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'text'
        );
    }catch(e){alert(e);}
   });
}
</script>
