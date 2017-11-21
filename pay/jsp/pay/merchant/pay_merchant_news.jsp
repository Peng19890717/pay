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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J5UC6Q7J3JLXPT2PW2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("J5UC6Q7J3JLXPT2PW5"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("J5UC6Q7J3JLXPT2QI1"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("J5UC6Q7J3JLXPT2PW4"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("J5UC6Q7J3JLXPT2PW3"));
%>
<script type="text/javascript">
var payMerchantNewsListPageTitle="商户公告";
var payMerchantNewsAddPageTitle="添加公告";
var payMerchantNewsDetailPageTitle="公告详情";
var payMerchantNewsUpdatePageTitle="修改公告";
$(document).ready(function(){});
</script>
<table id="payMerchantNewsList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payMerchantNews.htm?flag=0',method:'post',toolbar:'#payMerchantNewsListToolBar'">
       <thead>
        <tr>
           <th field="type" width="12%" align="left" sortable="true" formatter="format_type">类型</th>
           <th field="title" width="12%" align="left" sortable="true">标题</th>
           <th field="content" width="12%" align="left" sortable="true">内容</th>
           <th field="optTime" width="12%" align="left" sortable="true">操作时间</th>
           <th field="optUserId" width="12%" align="left" sortable="true">操作人</th>
           <th field="flag" width="12%" align="left" sortable="true" formatter="format_flag">状态</th>
           <th field="operation" data-options="formatter:formatPayMerchantNewsOperator" width="15%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payMerchantNewsListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
          类型 &nbsp;<select class="easyui-combobox" panelHeight="auto" id="searchPayMerchantNewsType" data-options="editable:false" name="searchPayMerchantNewsType">
  	           <option value="">请选择</option>
               <option value="1">公告</option>
               <option value="0">新闻</option>
               <option value="2">通知</option>
		</select>&nbsp;
         创建时间 &nbsp;<input type="text" id="searchPayMerchantNewsOptTimeStart" name="searchPayMerchantNewsOptTimeStart" class="easyui-datebox" value=""  style="width:100px"/>~
         <input type="text" id="searchPayMerchantNewsOptTimeEnd" name="searchPayMerchantNewsOptTimeEnd" class="easyui-datebox" value=""  style="width:100px"/>&nbsp;
         状态 &nbsp;<select class="easyui-combobox" panelHeight="auto" id="searchPayMerchantNewsFlag" data-options="editable:false" name="searchPayMerchantNewsFlag">
  	           <option value="">请选择</option>
               <option value="0">有效</option>
               <option value="1">无效</option>
		</select>
		 &nbsp;
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayMerchantNewsList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
     &nbsp;
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayMerchantNewsPageOpen()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
$('#payMerchantNewsList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function formatPayMerchantNewsOperator(val,row,index){
     var tmp=
        <%if(actionDetail != null){//角色判断%>
        "<a href=\"javascript:detailPayMerchantNewsPageOpen('"+row.id+"')\"><%=actionDetail.name %></a>&nbsp;"+
        <%}%>
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayMerchantNewsPageOpen('"+row.id+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionRemove !=null ){//角色判断%>
        "<a href=\"javascript:removePayMerchantNews('"+row.id+"')\"><%=actionRemove.name %></a>&nbsp;"+
        <%}%>
        "";
     return tmp;
}
function searchPayMerchantNewsList(){
    $('#payMerchantNewsList').datagrid('load',{
          type:$('#searchPayMerchantNewsType').combobox('getValue'),
          optTimeStart:$('#searchPayMerchantNewsOptTimeStart').datebox('getValue'),
          optTimeEnd:$('#searchPayMerchantNewsOptTimeEnd').datebox('getValue'),
          flag2:$('#searchPayMerchantNewsFlag').combobox('getValue')
    });  
}
function addPayMerchantNewsPageOpen(){
    openTab('addPayMerchantNewsPage',payMerchantNewsAddPageTitle,'<%=path %>/jsp/pay/merchant/pay_merchant_news_add.jsp');
}
function detailPayMerchantNewsPageOpen(id){
    openTab('detailPayMerchantNewsPage',payMerchantNewsDetailPageTitle,'<%=path %>/detailPayMerchantNews.htm?id='+id);
}
function updatePayMerchantNewsPageOpen(id){
    openTab('updatePayMerchantNewsPage',payMerchantNewsUpdatePageTitle,'<%=path %>/updatePayMerchantNews.htm?flag=show&id='+id);
}
function removePayMerchantNews(id){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayMerchantNews.htm',
            {id:id},
            function(data){
                $('#payMerchantNewsList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}

function format_type (data,row,index){
	if(data == "0"){
		return "新闻";
	}else if(data == "1"){
		return "公告";
	}else if(data == "2"){
		return "通知";
	}
}
function format_flag (data,row,index){
	if(data == "0"){
		return "有效";
	}else if(data == "1"){
		return "作废";
	}
}
</script>
