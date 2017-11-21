<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pxu2Cci2Q3rKTOI1"));
JWebAction actionManualAccountRun = ((JWebAction)user.actionMap.get("pzxfyod2Q3rKTOI1"));
JWebAction actionCheckDetail = ((JWebAction)user.actionMap.get("pxzvI6z2Q3rKTON1"));
%>
<script type="text/javascript">
var payBankAccountSumListPageTitle="对账列表";
$(document).ready(function(){});
function formatCwttype(data,row,index) {
	if(data=="1") return "收款";
	else if(data=="2") return "退款";
}
</script>
<table id="payBankAccountSumList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payBankAccountSum.htm?flag=0',method:'post',toolbar:'#payBankAccountSumListToolBar'">
       <thead>
        <tr>
           <th field="actdate" width="10%" align="left">对账日期</th>
           <th field="bankcode" width="10%" align="left">银行编码</th>
           <th field="bankName" width="10%" align="left">银行名称</th>
           <th field="sumCount" width="10%" align="left">总笔数</th>
           <th field="sumAmt" width="10%" align="left">总金额</th>
           <th field="sucCount" width="10%" align="left">成功笔数</th>
           <th field="sucAmt" width="10%" align="left">成功金额</th>
           <th field="failCount" width="10%" align="left">失败笔数</th>
           <th field="failAmt" width="10%" align="left">差错金额</th>
           <th field="operation" data-options="formatter:formatPayBankAccountSumOperator" width="10%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payBankAccountSumListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
        银行/渠道名称<input type="text" id="searchPayBankAccountSumBankName" name="searchPayBankAccountSumBankName" class="easyui-textbox" value=""  style="width:130px"/>
        对账日期<input class="easyui-datebox" style="width:100px"  data-options="editable:false" id="searchPayBankAccountSumActdateStart" name="searchPayBankAccountSumActdateStart"/>
 		~<input class="easyui-datebox" style="width:100px" data-options="editable:false" id="searchPayBankAccountSumActdateEnd" name="searchPayBankAccountSumActdateEnd"/>
        对账类型 <select class="easyui-combobox" name="searchPayBankAccountSumChktype" id="searchPayBankAccountSumChktype" data-options="editable:false">
	      <option value="" select="select">全部</option>
	      <option value="1">收款</option>
	      <option value="2">退款</option>
    </select>
    <%if(actionSearch != null){%>
    <a href="javascript:searchPayBankAccountSumList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionManualAccountRun != null){%>
    <a href="javascript:setResultManualAccountRunWindowOpen()" class="easyui-linkbutton" iconCls="icon-search"><%=actionManualAccountRun.name %></a>
    <%} %>
</div>
<div id="setResultManualAccountRunWindow" class="easyui-window" title="手动对账" 
    data-options="iconCls:'icon-edit',closed:true,modal:true" style="width:420px;height:200px;padding:5px;">
    <div class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <form id="setResultManualAccountForm" method="post" enctype="multipart/form-data">
                <table cellpadding="15">
                    <tr><td align="right">支付渠道：</td>
                    	<td>
                    		<select name="payChannel" id="payChannel">
                    			<%for(int i=0; i<PayCoopBankService.CHANNEL_LIST.size(); i++){
                    				 PayCoopBank bank = (PayCoopBank)PayCoopBankService.CHANNEL_LIST.get(i);
                    			%><option value="<%=bank.bankCode %>"><%=bank.bankName %></option><%
                    			} %>
                    		</select>
	                  	</td>
	                </tr>
                    <tr><td align="right">上传对账文件：</td>
                    	<td>
                    		<input type="file" name="manualAccountFile" id="manualAccountFile" data-options="multiline:true">
                    	</td>
				    </tr>
                </table>
            </form>
        </div>
        <div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
            <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" 
            	onclick="setResultManualAccountFormSubmit()" style="width:80px">确定</a>
            <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
                onclick="$('#setResultManualAccountRunWindow').window('close')" style="width:80px">取消</a>
        </div>
    </div>
</div>
<script type="text/javascript">
function setResultManualAccountFormSubmit(){
	//先锋对账文件格式检查
	if(document.getElementById('payChannel').value=='<%=com.PayConstant.PAY_CONFIG.get("PAY_CHANNEL_XF") %>'
		||document.getElementById('payChannel').value=='<%=com.PayConstant.PAY_CONFIG.get("PAY_CHANNEL_RB") %>'){
		var fn = document.getElementById('manualAccountFile').value;
		if(fn.length <=4 || fn.substring(fn.length-4,fn.length) != '.xls'){
			topCenterMessage('<%=JWebConstant.ERROR %>','文件类型错误（*.xls）');
			return;
		}
	}
	$('#setResultManualAccountForm').submit();
}
$('#payBankAccountSumList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
$('#setResultManualAccountForm').form({
    url:'<%=path %>/manualAccountRun.htm',
    onSubmit: function(){
    	$('#setResultManualAccountRunWindow').window('close');
    },
    success:function(data){
    	$('#payBankAccountSumList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
           topCenterMessage('<%=JWebConstant.OK %>','上传成功，对账处理中');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function formatPayBankAccountSumOperator(val,row,index){
     var tmp=
        <%if(actionCheckDetail != null){//角色判断%>
        (row.failCount >0?"<a href=\"javascript:checkListPayBankAccountCheckWindowOpen('"+row.bankcode+"','"+row.actdate+"')\"><%=actionCheckDetail.name %></a>&nbsp;":"")+
        <%}%>
        "";
     return tmp;
}
function searchPayBankAccountSumList(){
    $('#payBankAccountSumList').datagrid('load',{
          bankName:$('#searchPayBankAccountSumBankName').val(),
          actdateStart:$('#searchPayBankAccountSumActdateStart').datebox('getValue'),
          actdateEnd:$('#searchPayBankAccountSumActdateEnd').datebox('getValue'),
          chktype:$('#searchPayBankAccountSumChktype').combobox('getValue')
    });  
}
function detailPayBankAccountSumPageOpen(bankcode){
    openTab('updatePayBankAccountSumPage',payBankAccountSumUpdatePageTitle,'<%=path %>/updatePayBankAccountSum.htm?flag=show&bankcode='+bankcode);
}
//跳到错帐列表页面 
function checkListPayBankAccountCheckWindowOpen(bankcode,actdate){
	var tabTmp = $('#mainTabs'),title='对账明细';
	/**当前的tab 是否存在如果存在就 将其关闭**/  
	if(tabTmp.tabs('exists', title))$('#mainTabs').tabs('close',title);
    /**添加一个tab标签**/
	tabTmp.tabs('add',{id:'checkList',title: title,selected: true,closable: true,border:false});
    $('#checkList').panel('refresh','<%=path %>/jsp/pay/account/pay_bank_account_check.jsp?bankcode='+bankcode+'&actdateStart='+actdate+'&actdateEnd='+actdate+'&cwtype=-1');
}
function setResultManualAccountRunWindowOpen(){
	//$('#setResultManualAccountForm').form('clear');
	document.getElementById('manualAccountFile').value="";
    $('#setResultManualAccountRunWindow').window('open');
}
</script>
