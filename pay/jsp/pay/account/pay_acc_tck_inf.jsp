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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pGIHSUk2Q3rKTON1"));
%>
<script type="text/javascript">
var payAccTckInfListPageTitle="科目实时明细查询";
var payAccTckInfAddPageTitle="添加payAccTckInf";
var payAccTckInfDetailPageTitle="payAccTckInf详情";
var payAccTckInfUpdatePageTitle="修改payAccTckInf";
$(document).ready(function(){});
function formatPayAccTckInfDrCrFlag(data,row, index){
	if(data=="D"){
		return "借方";
	}else if(data=="C"){
		return "贷方";
	}
}
</script>
<table id="payAccTckInfList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payAccTckInf.htm?flag=0',method:'post',toolbar:'#payAccTckInfListToolBar'">
       <thead>
        <tr>
           <th field="accOrgNo" width="14%" align="left" sortable="true">机构号</th>
           <th field="ccy" width="14%" align="left" sortable="true">货币</th>
           <th field="accSubject" width="14%" align="left" sortable="true">科目号</th>
           <th field="txnTime" width="15%" align="left" sortable="true">发生时间</th>
           <th field="drCrFlag" width="14%" align="left" sortable="true" data-options="formatter:formatPayAccTckInfDrCrFlag">借贷方向</th>
           <th field="txnAmt" width="14%" align="left" sortable="true">发生额（元）</th>
           <th field="rmkCodInf" width="14%" align="left" sortable="true">摘要</th>
       </tr>
       </thead>
</table>
<div id="payAccTckInfListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    会计科目<input type="text" id="searchPayAccTckInfAccSubject" name="searchPayAccTckInfAccSubject" class="easyui-textbox" value=""  style="width:130px"/>
    发生时间
    	<input type="text" id="searchPayAccTckInfTxnTimeStart" name="searchPayAccTckInfTxnTimeStart" class="easyui-datetimespinner" value=""  style="width:160px"
		data-options="
            showSeconds: true,
            prompt: '点击上下箭头输入',
            icons:[{
                iconCls:'icon-clear',
                handler: function(e){
                    $(e.data.target).datetimespinner('clear');
                }
            }]"/>&nbsp;到&nbsp;
         <input type="text" id="searchPayAccTckInfTxnTimeEnd" name="searchPayAccTckInfTxnTimeEnd" class="easyui-datetimespinner" value=""  style="width:160px"
		data-options="
            showSeconds: true,
            prompt: '点击上下箭头输入',
            icons:[{
                iconCls:'icon-clear',
                handler: function(e){
                    $(e.data.target).datetimespinner('clear');
                }
            }]"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayAccTckInfList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payAccTckInfList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function searchPayAccTckInfList(){
    $('#payAccTckInfList').datagrid('load',{
          txnTimeStart:$('#searchPayAccTckInfTxnTimeStart').val(),//发生时间始
          txnTimeEnd:$('#searchPayAccTckInfTxnTimeEnd').val(),//发生时间止
          accSubject:$('#searchPayAccTckInfAccSubject').val()//会计科目
    });  
}
</script>
