<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pxtYtzC2Q3rKTOI1"));
JWebAction actionUpdate = ((JWebAction)user.actionMap.get("ACTION_UPDATE_ID"));
JWebAction actionSetResult = ((JWebAction)user.actionMap.get("pxEgdIC2Q3rKTOI1"));
String bankcode = request.getParameter("bankcode");
String actdateStart = request.getParameter("actdateStart");
String actdateEnd = request.getParameter("actdateEnd");
String cwtype = request.getParameter("cwtype");
request.setAttribute("cwtype", cwtype);
%>
<script type="text/javascript">
var payBankAccountCheckListPageTitle="对账明细";
$(document).ready(function(){});
function formatAccountCheckCwtype(data,row,index) {
	if(data=="-1") return "未对账";
	if(data=="0") return "正常";
	else if(data=="1") return "状态错误";
	else if(data=="2") return "银行有系统无";
	else if(data=="3") return "系统有银行无";
}
function formatAccountCheckChktype(data,row,index) {
	if(data=="1") return "收款";
	else if(data=="2") return "退款";
}
function formatAccountCheckStatus(data,row,index) {
	if(row.cwtype=='0')return "";
	if(data=="0") return "未处理";
	else if(data=="1") return "已处理";
}
</script>
<table id="payBankAccountCheckList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payBankAccountCheck.htm?flag=0',method:'post',toolbar:'#payBankAccountCheckListToolBar'">
       <thead>
        <tr>
           <th field="actdate" width="7%" align="left" sortable="true">对账日期</th>
           <th field="bankcode" width="7%" align="left" sortable="true">渠道编码</th>
           <th field="bankName" width="7%" align="left" sortable="true">渠道名称</th>
           <th field="orderTime" width="7%" align="left" sortable="true">订单时间</th>
           <th field="payTime" width="7%" align="left" sortable="true">支付时间</th>
           <th field="merName" width="7%" align="left" sortable="true">商户名称</th>
           <th field="godordcode" width="7%" align="left" sortable="true">商户订单号</th>
           <th field="bnkordno" width="7%" align="left" sortable="true">渠道流水号</th>
           <th field="inamt" width="7%" align="left" sortable="true">支付金额</th>
           <th field="fee" width="7%" align="left" sortable="true">手续费</th>
           <th field="inamt" width="7%" align="left" sortable="true">应结算金额</th>
           <th field="totalAmount" width="7%" align="left" sortable="true">总金额</th>
           <th field="cwtype" width="7%" align="left" sortable="true" data-options="formatter:formatAccountCheckCwtype">错账类型</th>
           <th field="chktype" width="7%" align="left" sortable="true" data-options="formatter:formatAccountCheckChktype">对账类型</th>
           <th field="status" width="7%" align="left" sortable="true" data-options="formatter:formatAccountCheckStatus">状态</th>
           <th field="operation" data-options="formatter:formatPayBankAccountCheckOperator" width="10%" align="left">操作</th>
           <th field="remark" width="13%" align="left" sortable="true">备注</th>
       </tr>
       </thead>
</table>
<div id="payBankAccountCheckListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
        渠道名称<select class="easyui-combobox" name="searchPayBankAccountCheckBankcode" id="searchPayBankAccountCheckBankcode" data-options="editable:false">
	      <option value="" select="select">全部</option>
	      <%for(int i = 0; i<PayCoopBankService.CHANNEL_LIST.size(); i++){ 
	      	PayCoopBank bank = (PayCoopBank)PayCoopBankService.CHANNEL_LIST.get(i);
	      	%><option value="<%=bank.bankCode %>" <%= bank.bankCode.equals(bankcode)?"selected":"" %>><%=bank.bankName %></option>
	      	<%}%>
    </select>
        商品订单号<input type="text" id="searchPayBankAccountCheckGodordcode" name="searchPayBankAccountCheckGodordcode" class="easyui-textbox" value=""  style="width:130px"/>
        商户名称<input type="text" id="searchPayBankAccountCheckMerName" name="searchPayBankAccountCheckMerName" class="easyui-textbox" value=""  style="width:130px"/>
        渠道流水号<input type="text" id="searchPayBankAccountCheckBnkordno" name="searchPayBankAccountCheckBnkordno" class="easyui-textbox" value=""  style="width:130px"/>
        对账类型<select class="easyui-combobox" name="searchPayBankAccountCheckChktype" id="searchPayBankAccountCheckChktype" data-options="editable:false">
	      <option value="" select="select">全部</option>
	      <option value="1">收款</option>
	      <option value="2">退款</option>
    </select>
        错账类型<select class="easyui-combobox" name="searchPayBankAccountCheckCwtype" id="searchPayBankAccountCheckCwtype" data-options="editable:false">
	      <option value="" select="select">全部</option>
	      <option value="-1">未对账</option>
	      <option value="0">正常</option>
	      <option value="X" <c:if test="${ cwtype eq 'X'}">selected</c:if>>错账</option>
	      <option value="1">状态错误</option>
	      <option value="2">银行有系统无</option>
	      <option value="3">系统有银行无</option>
    </select>
        状态<select class="easyui-combobox" name="searchPayBankAccountCheckStatus" id="searchPayBankAccountCheckStatus" data-options="editable:false">
	      <option value="" select="select">全部</option>
	      <option value="0">未处理</option>
	      <option value="1">已处理</option>
    </select>
        对账日期<input class="easyui-datebox" style="width:100px"  data-options="editable:false" id="searchPayBankAccountCheckActdateStart" value="<%=actdateStart==null?"":actdateStart %>" name="searchPayBankAccountCheckActdateStart"/>
 		~<input class="easyui-datebox" style="width:100px" data-options="editable:false" id="searchPayBankAccountCheckActdateEnd" value="<%=actdateEnd==null?"":actdateEnd %>" name="searchPayBankAccountCheckActdateEnd"/>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayBankAccountCheckList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
</div>
<div id="setResultPayBankAccountCheckWindow" class="easyui-window" title="处理结果设置" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:400px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="setResultPayBankAccountForm" method="post">
            	<input type="hidden" id="setResultPayBankAccountCheckBankCodeId" name="bankcode" value="">
            	<input type="hidden" id="setResultPayBankAccountCheckActDateId" name="actdate" value="">
            	<input type="hidden" id="setResultPayBankAccountCheckBnkOrdNoId" name="bnkordno" value="">
                <table cellpadding="5">
                    <tr><td align="right">处理结果：</td><td>
                    	<input type="radio" name="status" value="0" onclick="setResultPayBankAccountCheckRequired('0')" checked>未处理
                  		<input type="radio" name="status" value="1" onclick="setResultPayBankAccountCheckRequired('1')">已处理</td></tr>
                    <tr><td align="right">&nbsp;</td><td><input class="easyui-textbox" type="text" name="remark" id="setResultPayBankAccountCheckRemark"
				              validType="length[0,50]" invalidMessage="内容请控制在50个字" data-options="multiline:true"
				              style="width:200px;height:70px"/></td></tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
            	onclick="$('#setResultPayBankAccountForm').submit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#setResultPayBankAccountCheckWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">
$('#payBankAccountCheckList').datagrid({
	onBeforeLoad: function(param){
		param.flag='0';
		<%if(bankcode!=null){%>
        param.bankcode=$('#searchPayBankAccountCheckBankcode').val();
        <%}%>
		<%if(actdateStart!=null){%>
        try{param.actdateStart=$('#searchPayBankAccountCheckActdateStart').datebox('getValue');}catch(e){
        	param.actdateStart=document.getElementById("searchPayBankAccountCheckActdateStart").value;}
        <%}%>
		<%if(actdateEnd!=null){%>
        try{param.actdateEnd=$('#searchPayBankAccountCheckActdateEnd').datebox('getValue');}catch(e){
        	param.actdateEnd=document.getElementById("searchPayBankAccountCheckActdateEnd").value;}
        <%}%>
		<%if(cwtype!=null){%>
        try{param.cwtype=$('#searchPayBankAccountCheckCwtype').combobox('getValue');}catch(e){
        	param.cwtype=document.getElementById("searchPayBankAccountCheckCwtype").value;}
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
$('#setResultPayBankAccountForm').form({
    url:'<%=path %>/setResultBankAccount.htm',
    onSubmit: function(){
    	var f = $(this).form('validate');
    	if(f)$('#setResultPayBankAccountCheckWindow').window('close');
       	return f;
    },
    success:function(data){
    	$('#payBankAccountCheckList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','设置成功！');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function formatPayBankAccountCheckOperator(val,row,index){
     var tmp=
        <%if(actionUpdate != null){//角色判断%>
        "<a href=\"javascript:updatePayBankAccountCheckPageOpen('"+row.bankcode+"')\"><%=actionUpdate.name %></a>&nbsp;"+
        <%}%>
        <%if(actionSetResult != null){//角色判断%>
    	((row.status=="0"&&row.cwtype!='0')?"<a href=\"javascript:setResultPayBankAccountCheckWindowOpen('"+row.bankcode+"','"+row.actdate+"','"+row.bnkordno+"')\"><%=actionSetResult.name %></a>&nbsp;&nbsp;":"")+
    	<%}%>
        "";
     return tmp;
}
function searchPayBankAccountCheckList(){
try{
    $('#payBankAccountCheckList').datagrid('load',{
          bankcode:$('#searchPayBankAccountCheckBankcode').combobox('getValue'),
          godordcode:$('#searchPayBankAccountCheckGodordcode').val(),
          merName:$('#searchPayBankAccountCheckMerName').val(),
          bnkordno:$('#searchPayBankAccountCheckBnkordno').val(),
          chktype:$('#searchPayBankAccountCheckChktype').combobox('getValue'),
          cwtype:$('#searchPayBankAccountCheckCwtype').combobox('getValue'),
          status:$('#searchPayBankAccountCheckStatus').combobox('getValue'),
          actdateStart:$('#searchPayBankAccountCheckActdateStart').datebox('getValue'),
          actdateEnd:$('#searchPayBankAccountCheckActdateEnd').datebox('getValue')
    });
}catch(e){alert(e);}  
}
function addPayBankAccountCheckPageOpen(){
    openTab('addPayBankAccountCheckPage',payBankAccountCheckAddPageTitle,'<%=path %>/jsp/pay/account/pay_bank_account_check_add.jsp');
}
function detailPayBankAccountCheckPageOpen(bankcode){
    openTab('detailPayBankAccountCheckPage',payBankAccountCheckDetailPageTitle,'<%=path %>/detailPayBankAccountCheck.htm?bankcode='+bankcode);
}
function updatePayBankAccountCheckPageOpen(bankcode){
    openTab('updatePayBankAccountCheckPage',payBankAccountCheckUpdatePageTitle,'<%=path %>/updatePayBankAccountCheck.htm?flag=show&bankcode='+bankcode);
}
function removePayBankAccountCheck(bankcode){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayBankAccountCheck.htm',
            {bankcode:bankcode},
            function(data){
                $('#payBankAccountCheckList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}
function setResultPayBankAccountCheckWindowOpen(bankcode,actdate,bnkordno){
	document.getElementById('setResultPayBankAccountCheckBankCodeId').value=bankcode;
	document.getElementById('setResultPayBankAccountCheckActDateId').value=actdate;
	document.getElementById('setResultPayBankAccountCheckBnkOrdNoId').value=bnkordno;
    $('#setResultPayBankAccountCheckWindow').window('open');
}
function setResultPayBankAccountCheckRequired(flag){
	if(flag == '0'){
		$('#setResultPayBankAccountCheckRemark').textbox({
			required:false
	    });
	} else {
	 	$('#setResultPayBankAccountCheckRemark').textbox({
			required:true
	    });
	    $('#setResultPayBankAccountCheckRemark').textbox('textbox').focus();
	}
}
</script>
