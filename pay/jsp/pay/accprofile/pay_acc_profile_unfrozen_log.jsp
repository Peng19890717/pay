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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("J9OYDLRQ3JLXPT2QH1"));
JWebAction actionCheck = ((JWebAction)user.actionMap.get("J9OYDLRQ3JLXPT2QH2"));
String accFrozenId = request.getParameter("accFrozenId");
%>
<script type="text/javascript">
var payAccProfileUnfrozenLogListPageTitle="商户资金解冻日志";
$(document).ready(function(){});
function format_unfrozen_flag(data,row, index){
	if(data=="0") {
		return "未审";
	} else if(data=="1"){
		return "已审";
	} else if(data=="2"){
		return "审核不通过";
	}
}
</script>
<table id="payAccProfileUnfrozenLogList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payAccProfileUnfrozenLog.htm?flags=0',method:'post',toolbar:'#payAccProfileUnfrozenLogListToolBar'">
       <thead>
        <tr>
           <th field="id" width="10%" align="left">记录ID</th>
           <th field="unfrozenAmt" width="10%" align="left">解冻金额</th>
           <th field="flag" width="10%" align="left" formatter="format_unfrozen_flag">状态</th>
           <th field="createTime" width="10%" align="left">发起时间</th>
           <th field="optTime" width="10%" align="left">审核时间</th>
           <th field="userId" width="10%" align="left">处理人ID</th>
           <th field="accFrozenId" width="10%" align="left">冻结记录编号</th>
           <th field="operation" data-options="formatter:formatPayAccProfileUnfrozenLogOperator" width="10%" align="left">操作</th>
           <th field="remark" width="19%" align="left">备注</th>
       </tr>
       </thead>
</table>
<div id="payAccProfileUnfrozenLogListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    冻结资金编号<input type="text" id="searchPayAccProfileUnfrozenLogAccFrozenId" name="searchPayAccProfileUnfrozenLogAccFrozenId" class="easyui-textbox" value="<%=accFrozenId==null?"":accFrozenId %>"  style="width:130px"/>
    状态<select class="easyui-combobox" id="searchPayAccProfileUnFrozenFlag" name="searchPayAccProfileUnFrozenFlag" data-options="editable:false">					           
	           <option value="0">未审</option>
	           <option value="1">已审</option>
	           <option value="2">审核不通过</option>
	    </select>
	    发起时间<input type="text" id="searchPayAccProfileUnFrozenCreateTimeStart" name="searchPayAccProfileUnFrozenCreateTimeStart" class="easyui-datebox" value=""  style="width:100px"/>
  ~<input type="text" id="searchPayAccProfileUnFrozenCreateTimeEnd" name="searchPayAccProfileUnFrozenCreateTimeEnd" class="easyui-datebox" value=""  style="width:100px"/> 
 审核时间<input type="text" id="searchPayAccProfileUnFrozenOptTimeStart" name="searchPayAccProfileUnFrozenOptTimeStart" class="easyui-datebox" value=""  style="width:100px"/>
  ~<input type="text" id="searchPayAccProfileUnFrozenOptTimeEnd" name="searchPayAccProfileUnFrozenOptTimeEnd" class="easyui-datebox" value=""  style="width:100px"/>     
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayAccProfileUnfrozenLogList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<div id="payAccProfileUnfrozenCheckWindow" class="easyui-window" title="审核" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="payAccProfileUnfrozenCheckForm" method="post">
            	<input type="hidden" name="id" id="setPayAccProfileUnfrozenCheckId">
                <table cellpadding="5">
                    <tr><td align="right">审核结果：</td><td>
                    	<input type="radio" name="flag" value="1" checked>通过
                  		<input type="radio" name="flag" value="2">不通过</td></tr>
                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="remark" id="payAccProfileUnfrozenCheckRemark"
				          validType="length[0,150]" invalidMessage="内容请控制在150个字" data-options="multiline:true"
				          style="width:200px;height:70px"/></td></tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
            	id="payAccProfileUnfrozenCheckFormPass" style="width:80px" onclick="$('#payAccProfileUnfrozenCheckForm').submit()">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#payAccProfileUnfrozenCheckWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">
$('#payAccProfileUnfrozenLogList').datagrid({
onBeforeLoad: function(param){
		<%if(accFrozenId!=null){%>
        	param.accFrozenId=document.getElementById("searchPayAccProfileUnfrozenLogAccFrozenId").value;
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function payAccProfileUnfrozenCheckWindowOpen(checkId){
	$('#payAccProfileUnfrozenCheckWindow').form('clear');
	$('#payAccProfileUnfrozenCheckWindow').window('open');
	document.getElementById('setPayAccProfileUnfrozenCheckId').value=checkId;
	$("input[name='flag']").get(0).checked=true;
}
function formatPayAccProfileUnfrozenLogOperator(val,row,index){
     var tmp=
        <%if(actionCheck != null){//角色判断%>
        row.flag=="0"?("<a href=\"javascript:payAccProfileUnfrozenCheckWindowOpen('"+row.id+"')\"><%=actionCheck.name %></a>&nbsp;"):""+
        <%}%>
        "";
     return tmp;
}
//资金冻结审核
function payAccProfileUnfrozenCheckWindowOpen(checkId){
	$('#payAccProfileUnfrozenCheckWindow').form('clear');
	$('#payAccProfileUnfrozenCheckWindow').window('open');
	document.getElementById('setPayAccProfileUnfrozenCheckId').value=checkId;
	$("input[name='flag']").get(0).checked=true;
}
function searchPayAccProfileUnfrozenLogList(){
    $('#payAccProfileUnfrozenLogList').datagrid('load',{
          accFrozenId:$('#searchPayAccProfileUnfrozenLogAccFrozenId').val(),
          flag:$('#searchPayAccProfileUnFrozenFlag').combobox('getValue'),
          createTimeStart:$('#searchPayAccProfileUnFrozenCreateTimeStart').datebox('getValue'),
          createTimeEnd:$('#searchPayAccProfileUnFrozenCreateTimeEnd').datebox('getValue'),
          optTimeStart:$('#searchPayAccProfileUnFrozenOptTimeStart').datebox('getValue'),
          optTimeEnd:$('#searchPayAccProfileUnFrozenOptTimeEnd').datebox('getValue')
    });  
}
//审核操作
$('#payAccProfileUnfrozenCheckForm').form({
    url:'<%=path %>/checkPayAccProfileUnfrozenLog.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#payAccProfileUnfrozenCheckWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payAccProfileUnfrozenLogList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','审核成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
</script>
