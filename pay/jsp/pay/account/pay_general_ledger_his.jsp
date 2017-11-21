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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pGIl7Jq2Q3rKTOS2"));
%>
<script type="text/javascript">
var payGeneralLedgerHisListPageTitle="总账查询";
$(document).ready(function(){});
</script>
<table id="payGeneralLedgerHisList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payGeneralLedgerHis.htm?flag=0',method:'post',toolbar:'#payGeneralLedgerHisListToolBar'">
       <thead>
        <tr>
           <th field="brNo" width="15%" align="left" sortable="true">机构号</th>
           <th field="ccy" width="14%" align="left" sortable="true">货币</th>
           <th field="acDate" width="15%" align="left" sortable="true">会计日期</th>
           <th field="glCode" width="14%" align="left" sortable="true">科目号</th>
           <th field="glName" width="13%" align="left" sortable="true">科目名称</th>
           <th field="currDrBal" width="14%" align="left" sortable="true">借方余额</th>
           <th field="currCrBal" width="14%" align="left" sortable="true">贷方余额</th>
       </tr>
       </thead>
</table>
<div id="payGeneralLedgerHisListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    会计科目号：
    <input type="text" id="searchPayGeneralLedgerHisGlCode" name="searchPayGeneralLedgerHisGlCode" class="easyui-textbox" value=""  style="width:130px"/>
    指定日期：
    <input type="text" id="searchPayGeneralLedgerHisAcDate" name="searchPayGeneralLedgerHisAcDate" class="easyui-datebox" value=""  style="width:130px" data-options="editable:false"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayGeneralLedgerHisList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payGeneralLedgerHisList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function searchPayGeneralLedgerHisList(){
    $('#payGeneralLedgerHisList').datagrid('load',{
          glCode:$('#searchPayGeneralLedgerHisGlCode').val(),
          acDate:$('#searchPayGeneralLedgerHisAcDate').datebox('getValue')
    });  
}

</script>
