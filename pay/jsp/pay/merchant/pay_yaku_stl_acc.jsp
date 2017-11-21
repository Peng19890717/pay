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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("q5lwfHH2Q3rKTOW1"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("q5lwfHH2Q3rKTOW2"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("q5lwfHH2Q3rKTOW4"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("q5lwfHH2Q3rKTOW3"));
%>
<script type="text/javascript">
var payYakuStlAccListPageTitle="雅酷结算账号";
var payYakuStlAccAddPageTitle="添加雅酷结算账号";
var payYakuStlAccUpdatePageTitle="修改雅酷结算账号";
$(document).ready(function(){});
</script>
<table id="payYakuStlAccList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payYakuStlAcc.htm?flag=0',method:'post',toolbar:'#payYakuStlAccListToolBar'">
       <thead>
        <tr>
           <th field="merno" width="5%" align="left" sortable="true">商户号</th>
           <th field="storeName" width="9%" align="left" sortable="true">商户名称</th>
           <th field="accName" width="5%" align="left" sortable="true">开户行姓名</th>
           <th field="bankCode" width="9%" align="left" sortable="true">商户开户银行编码</th>
           <th field="bankBranchName" width="13%" align="left" sortable="true">开户行网点名称</th>
           <th field="accNo" width="9%" align="left" sortable="true">商户结算银行账号</th>
           <th field="bankBranchCode" width="9%" align="left" sortable="true">结算账户开户行行号</th>
           <th field="province" width="7%" align="left" sortable="true">结算账号开户省份</th>
           <th field="city" width="7%" align="left" sortable="true">结算账号开户城市</th>
           <th field="credentialNo" width="9%" align="left" sortable="true">结算账号开户身份证号</th>
           <th field="tel" width="9%" align="left" sortable="true">结算账号开户手机号</th>
           <th field="operation" data-options="formatter:formatPayYakuStlAccOperator" width="9%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payYakuStlAccListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    商户号<input type="text" id="searchPayYakuStlAccMerno" name="searchPayYakuStlAccMerno" class="easyui-textbox" value=""  style="width:130px"/>
    开户行姓名<input type="text" id="searchPayYakuStlAccAccName" name="searchPayYakuStlAccAccName" class="easyui-textbox" value=""  style="width:130px"/>
    商户结算银行账号<input type="text" id="searchPayYakuStlAccAccNo" name="searchPayYakuStlAccAccNo" class="easyui-textbox" value=""  style="width:130px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayYakuStlAccList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayYakuStlAccPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payYakuStlAccList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayYakuStlAccOperator(val,row,index){
     var tmp=
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayYakuStlAccPageOpen('"+row.accNo+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayYakuStlAcc('"+row.accNo+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayYakuStlAccList(){
    $('#payYakuStlAccList').datagrid('load',{
          merno:$('#searchPayYakuStlAccMerno').val(),
          accName:$('#searchPayYakuStlAccAccName').val(),
          accNo:$('#searchPayYakuStlAccAccNo').val()
    });  
}
function addPayYakuStlAccPageOpen(){
    openTab('addPayYakuStlAccPage',payYakuStlAccAddPageTitle,'<%=path %>/jsp/pay/merchant/pay_yaku_stl_acc_add.jsp');
}
function updatePayYakuStlAccPageOpen(accNo){
    openTab('updatePayYakuStlAccPage',payYakuStlAccUpdatePageTitle,'<%=path %>/updatePayYakuStlAcc.htm?flag=show&accNo='+accNo);
}
function removePayYakuStlAcc(accNo){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayYakuStlAcc.htm',
            {accNo:accNo},
            function(data){
                $('#payYakuStlAccList').datagrid('reload');
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
