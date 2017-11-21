<%@page import="java.util.List"%>
<%@page import="com.pay.margin.dao.PayMarginRcvDet"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.margin.dao.PayMargin"%>
<%@ page import="com.pay.margin.dao.PayMarginRcvDetDAO"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pz4fl0s2Q3rKTPv1"));
PayMargin payMargin = (PayMargin)request.getAttribute("payMargin");
List<PayMarginRcvDet> payMarginRcvDets = (List<PayMarginRcvDet>)request.getAttribute("payMarginRcvDetList");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payMargin != null){ %>
			<table id="payMarginRcvDetList" style="width:100%;height:100%" rownumbers="true" pagination="true"
			    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
			        %>/payMarginRcvDet.htm?flag=0&custId=${ payMarginRcvDetList[0].custId }',method:'post',toolbar:'#payMarginRcvDetListToolBar'">
			       <thead>
			        <tr>
			           <th field="paidInAmt" width="20%" align="left" sortable="true">实收金额</th>
			           <th field="marginRcvTime" width="20%" align="left" sortable="true">保证金扣缴时间</th>
			           <th field="marginCurAmt" width="20%" align="left" sortable="true">保证金余额</th>
			           <th field="mark" width="39%" align="left" sortable="true">备注</th>
			       </tr>
			       </thead>
			</table>
			<div id="payMarginRcvDetListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
			    保证金收取时间<input type="text" id="searchPayMarginRcvDetMarginRcvTimeStart" name="marginRcvTimeStart" class="easyui-datebox" value=""  style="width:130px" editable="fasle"/>~
			    <input type="text" id="searchPayMarginRcvDetMarginRcvTimeEnd" name="marginRcvTimeEnd" class="easyui-datebox" value=""  style="width:130px" editable="fasle"/>
			    <%if(actionSearch != null){//角色判断%>
			    	<a href="javascript:searchPayMarginRcvDetList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
			    <%} %>
			</div>
        <%} %>
    </div>
</div>
<script type="text/javascript">
$('#payMarginRcvDetList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function searchPayMarginRcvDetList(){
    $('#payMarginRcvDetList').datagrid('load',{
          marginRcvTimeStart:$('#searchPayMarginRcvDetMarginRcvTimeStart').datebox('getValue'),
          marginRcvTimeEnd:$('#searchPayMarginRcvDetMarginRcvTimeEnd').datebox('getValue')
    });  
}
function appentPayMarginPageOpen(seqNo){
	openTab('appentPayMarginPage',payMarginAppendPageTitle,'<%=path %>/appendPayMargin.htm?flag=show&seqNo='+seqNo);
}
</script>