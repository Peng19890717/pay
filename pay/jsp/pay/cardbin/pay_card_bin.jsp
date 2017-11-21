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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pxhcJyK2Q3rKTOI1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("qcvInJl2Q3rKTOK1"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("qcvInJl2Q3rKTOK2"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("qcvInJl2Q3rKTOK3"));
%>
<script type="text/javascript">
var payCardBinListPageTitle="银行卡BIN信息维护";
var payCardBinAddPageTitle="添加银行卡BIN信息";
var payCardBinDetailPageTitle="银行卡BIN信息详情";
var payCardBinUpdatePageTitle="修改银行卡BIN信息";
function formatPayCardType(data,row,index) {
	if(data=="0") return "借记卡";
	if(data=="1") return "贷记卡";
	if(data=="2") return "准贷记卡";
	if(data=="3") return "预付卡";
	return "";
}
</script>
<table id="payCardBinList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payCardBin.htm?flag=0',method:'post',toolbar:'#payCardBinListToolBar'">
       <thead>
        <tr>
           <th field="bankCode" width="15%" align="left" sortable="true">银行编码</th>
           <th field="bankName" width="15%" align="left" sortable="true">银行名称</th>
           <th field="cardName" width="20%" align="left" sortable="true">卡名称</th>
           <th field="cardType" width="10%" align="left" sortable="true" data-options="formatter:formatPayCardType">卡类型</th>
           <th field="binNo" width="10%" align="left" sortable="true">卡前缀</th>
           <th field="cardLength" width="10%" align="left" sortable="true">卡号长度</th>
           <!-- <th field="bankNo" width="10%" align="left" sortable="true">银行号</th> -->
           <th field="version" width="9%" align="left" sortable="true">版本</th>
           <th field="operation" data-options="formatter:formatPayCardBinOperator" width="15%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payCardBinListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    银行编码<input type="text" id="searchPayCardBinBankCode" name="searchPayCardBinBankCode" class="easyui-textbox" value=""  style="width:130px"/>
    银行名称<input type="text" id="searchPayCardBinBankName" name="searchPayCardBinBankName" class="easyui-textbox" value=""  style="width:130px"/>
    卡名称<input type="text" id="searchPayCardBinCardName" name="searchPayCardBinCardName" class="easyui-textbox" value=""  style="width:130px"/>
    卡前缀<input type="text" id="searchPayCardBinBinNo" name="searchPayCardBinBinNo" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayCardBinList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayCardBinPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payCardBinList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function searchPayCardBinList(){
    $('#payCardBinList').datagrid('load',{
          bankCode:$('#searchPayCardBinBankCode').val(),
          bankName:$('#searchPayCardBinBankName').val(),
          cardName:$('#searchPayCardBinCardName').val(),
          binNo:$('#searchPayCardBinBinNo').val()
    });  
}
function formatPayCardBinOperator(val,row,index){
    var tmp=
       <%if(actionUpdate != null){//角色判断%>
       "<a href=\"javascript:updatePayCardBinPageOpen('"+row.binId+"')\"><%=actionUpdate.name %></a>&nbsp;"+
       <%}%>
       <%if(actionRemove !=null ){//角色判断%>
       "<a href=\"javascript:removePayCardBin('"+row.binId+"')\"><%=actionRemove.name %></a>&nbsp;"+
       <%}%>
       "";
    return tmp;
}
function addPayCardBinPageOpen(){
    openTab('addPayCardBinPage',payCardBinAddPageTitle,'<%=path %>/jsp/pay/cardbin/pay_card_bin_add.jsp');
}
function updatePayCardBinPageOpen(binId){
    openTab('updatePayCardBinPage',payCardBinUpdatePageTitle,'<%=path %>/updatePayCardBin.htm?flag=show&binId='+binId);
}
function removePayCardBin(binId){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayCardBin.htm',
            {binId:binId},
            function(data){
                $('#payCardBinList').datagrid('reload');
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
