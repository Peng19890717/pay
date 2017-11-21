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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("q65X6sP2Q3rKTOK1"));
JWebAction actionCheck = ((JWebAction)user.actionMap.get("q65ZfVm2Q3rKTOM1"));
JWebAction actionForDetail = ((JWebAction)user.actionMap.get("q65ZfVm2Q3rKTPa1"));
%>
<script type="text/javascript">
$(document).ready(function(){});

function formatPayReceiveCheckTranType(data,row,index) {
	if(data=="0") return "单笔";
	if(data=="1") return "批量";	
	return "";
}
</script>
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'center'">
			<table id="payReceiveAndPayCheckList" style="width:100%;height:100%" rownumbers="true" pagination="true"
			    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
			        %>/payReceiveAndPayCheck.htm?flag=0',method:'post',toolbar:'#payReceiveAndPayCheckListToolBar'">
			       <thead>
			       	<tr>
			           <th field="batNo" width="12%" align="left">批次号</th>
			           <th field="cou" width="12%" align="left">笔数</th>
			           <th field="tranType" width="12%" align="left" data-options="formatter:formatPayReceiveCheckTranType">单笔/批量</th>
			           <th field="custId" width="12%" align="left" sortable="true">客户编号</th>
			           <th field="amount" width="12%" align="left" sortable="true">总金额</th>
			           <th field="createTime" width="12%" align="left" sortable="true">创建时间</th>
			           <th field="fee" width="12%" align="left" sortable="true">手续费总额</th>
			           <th field="operation" formatter="formatPayReceiveAndPayCheckOperator" width="16%" align="left">操作</th>
			       </tr>
			      </thead>
			</table>
		</div>
		<div id="payReceiveAndPayCheckListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
				    客户编号<input type="text" id="searchPayReceiveAndPayCheckCustId" name="searchPayReceiveAndPayCheckCustId" class="easyui-textbox" style="width:130px"/>
				    批次号<input type="text" id="searchPayReceiveAndPayCheckBatNo" name="searchPayReceiveAndPayCheckBatNo" class="easyui-textbox" value=""  style="width:130px"/>
				    创建日期<input type="text" id="searchPayReceiveAndPayCheckCreateTimeStart" name="searchPayReceiveAndPayCheckCreateTimeStart"
						 data-options="editable:false" class="easyui-datebox" style="width:100px"/>
				    ~<input type="text" id="searchPayReceiveAndPayCheckCreateTimeEnd" name="searchPayReceiveAndPayCheckCreateTimeEnd"
						 data-options="editable:false" class="easyui-datebox" style="width:100px"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchpayReceiveAndPayCheckList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<div id="payReceiveAndPayCheckWindow" class="easyui-window" title="审核" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="payReceiveAndPayCheckForm" method="post">
            	<input type="hidden" name="batNo" id="setPayReceiveAndPayCheckBatNo">
                <table cellpadding="5">
                    <tr><td align="right">处理结果：</td><td>
                    	<input type="radio" name="status" value="1" onclick="payReceiveAndPayCheckRequired('1')" checked>通过
                  		<input type="radio" name="status" value="2" onclick="payReceiveAndPayCheckRequired('2')">不通过</td></tr>
                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="remark" id="payReceiveAndPayCheckRemark"
				              validType="length[0,50]" invalidMessage="内容请控制在50个字" data-options="multiline:true"
				              style="width:200px;height:70px"/></td></tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
            	id="payReceiveAndPayCheckPass" style="width:80px" onclick="$('#payReceiveAndPayCheckForm').submit()">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#payReceiveAndPayCheckWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>

<script type="text/javascript">
function formatPayReceiveAndPayCheckOperator(data,row,index){
     var tmp=
         //审核
         <%if(actionCheck != null){//角色判断%>
         "<a href=\"javascript:payReceiveAndPayCheckWindowOpen('"+row.batNo+"')\"><%=actionCheck.name %></a>&nbsp;&nbsp;"+
         <%}%>
         //明细
         <%if(actionForDetail != null){//角色判断%>
         "<a href=\"javascript:checkListpayReceiveAndPayChecWindowOpen('"+row.batNo+"')\"><%=actionForDetail.name %></a>"+
         <%}%>
         "";
     return tmp;
}

$('#payReceiveAndPayCheckList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function searchpayReceiveAndPayCheckList(){
    $('#payReceiveAndPayCheckList').datagrid('load',{
          custId:$('#searchPayReceiveAndPayCheckCustId').val(),
          batNo:$('#searchPayReceiveAndPayCheckBatNo').val(),
          createTimeStart:$('#searchPayReceiveAndPayCheckCreateTimeStart').datebox('getValue'),
          createTimeEnd:$('#searchPayReceiveAndPayCheckCreateTimeEnd').datebox('getValue')
    });  
}
//跳到代收付列表页面 
function checkListpayReceiveAndPayChecWindowOpen(batNo){
	var tabTmp = $('#mainTabs'),title='代收付';
	/**当前的tab 是否存在如果存在就 将其关闭**/  
	if(tabTmp.tabs('exists', title))$('#mainTabs').tabs('close',title);
    /**添加一个tab标签**/
	tabTmp.tabs('add',{id:'checkList',title: title,selected: true,closable: true,border:false});
    $('#checkList').panel('refresh','<%=path %>/jsp/pay/order/pay_receive_and_pay.jsp?batNo='+batNo);
}
//审核
function payReceiveAndPayCheckWindowOpen(batNo){
	alert("此功能暂未开发");
	return;
	$('#payReceiveAndPayCheckWindow').form('clear');
	document.getElementById('setPayReceiveAndPayCheckBatNo').value = batNo;
	$("input[name='status']").get(0).checked=true;
    $('#payReceiveAndPayCheckWindow').window('open');
}
//审核操作
$('#payReceiveAndPayCheckForm').form({
    url:'<%=path %>/setPayReceiveAndPayCheck.htm',
    onSubmit: function(){
    	return;
    	var f = $(this).form('validate');
    	if(f)$('#payReceiveAndPayCheckWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payReceiveAndPayCheckList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','审核成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});

function payReceiveAndPayCheckRequired(flag){
	if(flag == '1'){
		$('#payReceiveAndPayCheckRemark').textbox({
			required:false
	    });
	} else {
	 	$('#payReceiveAndPayCheckRemark').textbox({
			required:true
	    });
	    $('#payReceiveAndPayCheckRemark').textbox('textbox').focus();
	}
}
</script>
