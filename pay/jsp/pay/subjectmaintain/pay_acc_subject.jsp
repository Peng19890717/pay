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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pGkiXS92Q3rKTP15"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pGkiXS92Q3rKTP16"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pGkiXS92Q3rKTP19"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pGkiXS92Q3rKTP18"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("pGkiXS92Q3rKTP17"));
%>
<script type="text/javascript">
var payAccSubjectListPageTitle="科目维护";
var payAccSubjectAddPageTitle="添加科目";
var payAccSubjectDetailPageTitle="科目详情";
var payAccSubjectUpdatePageTitle="修改科目";
$(document).ready(function(){});

//科目类别格式化     1资产类；2负债类；3共同类；4权益类； 5成本类；6损益类；9-表外类
function formatPayAccSubjectGlType(data,row,index) {
	if(data=="1") return "资产类";
	if(data=="2") return "负债类";
	if(data=="3") return "共同类";
	if(data=="4") return "权益类";
	if(data=="5") return "成本类";
	if(data=="6") return "损益类";
	if(data=="9") return "表外类";
	return "";
}

//格式化时间
function formatPayAccSubjectEfctDate(data,row,index) {
    return data.substring(0,10);
}
function formatPayAccSubjectExpiredDate(data,row,index) {
	return data.substring(0,10);
}

</script>
<table id="payAccSubjectList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payAccSubject.htm?flag=0',method:'post',toolbar:'#payAccSubjectListToolBar'">
       <thead>
        <tr>
           <th field="glCode" width="16%" align="left" sortable="true">科目编号</th>
           <th field="glName" width="16%" align="left" sortable="true">科目名称</th>
           <th field="efctDate" width="16%" align="left" sortable="true" data-options="formatter:formatPayAccSubjectEfctDate">生效日期</th>
           <th field="expiredDate" width="16%" align="left" sortable="true" data-options="formatter:formatPayAccSubjectExpiredDate">失效日期</th>
           <th field="glType" width="16%" align="left" sortable="true" data-options="formatter:formatPayAccSubjectGlType">科目类别</th>
           <th field="operation" data-options="formatter:formatPayAccSubjectOperator" width="19%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payAccSubjectListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    	科目编号<input type="text" id="searchPayAccSubjectGlCode" name="searchPayAccSubjectGlCode" class="easyui-textbox" value=""  style="width:130px"/>
    	科目名称<input type="text" id="searchPayAccSubjectGlName" name="searchPayAccSubjectGlName" class="easyui-textbox" value=""  style="width:130px"/>
    	科目类别<select class="easyui-combobox" panelHeight="auto" id="searchPayAccSubjectGlType" 
					data-options="editable:false" name="searchPayAccSubjectGlType"  style="width:130px">
					 	   <option value="">请选择</option>
			    		   <option value="1">资产类</option>
						   <option value="2">负债类</option>
						   <option value="3">共同类</option>
						   <option value="4">权益类</option>
						   <option value="5">成本类</option>
						   <option value="6">损益类</option>
						   <option value="9">表外类</option>
			    </select>
    	
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayAccSubjectList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayAccSubjectPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div> 
<script type="text/javascript">
$('#payAccSubjectList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayAccSubjectOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayAccSubjectPageOpen('"+row.glCode+"')\"><%=actionDetail.name %></a>&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayAccSubjectPageOpen('"+row.glCode+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayAccSubject('"+row.glCode+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayAccSubjectList(){
    $('#payAccSubjectList').datagrid('load',{
          glCode:$('#searchPayAccSubjectGlCode').val(),
          glName:$('#searchPayAccSubjectGlName').val(),
          glType:$('#searchPayAccSubjectGlType').combobox('getValue')
    });  
}
function addPayAccSubjectPageOpen(){
    openTab('addPayAccSubjectPage',payAccSubjectAddPageTitle,'<%=path %>/jsp/pay/subjectmaintain/pay_acc_subject_add.jsp');
}
function detailPayAccSubjectPageOpen(glCode){
    openTab('detailPayAccSubjectPage',payAccSubjectDetailPageTitle,'<%=path %>/detailPayAccSubject.htm?glCode='+glCode);
}
function updatePayAccSubjectPageOpen(glCode){
    openTab('updatePayAccSubjectPage',payAccSubjectUpdatePageTitle,'<%=path %>/updatePayAccSubject.htm?flag=show&glCode='+glCode);
}
function removePayAccSubject(glCode){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayAccSubject.htm',
            {glCode:glCode},
            function(data){
                $('#payAccSubjectList').datagrid('reload');
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
