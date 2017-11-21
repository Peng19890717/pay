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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("pv1D0Ok2Q3rKTOI2"));
JWebAction actionAdd = ((JWebAction)user.actionMap.get("pv1D0Ok2Q3rKTOI1"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("pv1D0Ok2Q3rKTOI4"));
JWebAction actionRemove = ((JWebAction)user.actionMap.get("pyNOF2t2Q3rKTOI1"));
%>
<script type="text/javascript">
var payFeeRateAddPageTitle="添加费率";
var payFeeRateListPageTitle="费率设置";
</script>
<table id="payFeeRateList" style="width:100%;height:100%" rownumbers="true" pagination="true"
    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
        %>/payFeeRate.htm?flag=0',method:'post',toolbar:'#payFeeRateListToolBar'">
       <thead>
        <tr>
           <th field="feeCode" width="8%" align="left" sortable="true">费率代码</th>
           <th field="feeName" width="10%" align="left" sortable="true">费率名称</th>
           <th field="custType" width="9%" align="left" sortable="true" formatter="custTypeFormatter">客户类型</th>
           <th field="feeInfo" width="50%" align="left" sortable="true">计费信息</th>
            <th field="tranType" width="5%" align="left" sortable="true"  formatter="tranTypeFormatter">交易类型</th>
           <th field="createTime" width="9%" align="left" sortable="true">创建时间</th>
           <th field="createUserName" width="9%" align="left" sortable="true">创建人</th>
           <th field="operation" data-options="formatter:formatPayFeeRateOperator" width="5%" align="left">操作</th>
       </tr>
       </thead>
</table>
<div id="payFeeRateListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
    费率代码<input type="text" id="searchPayFeeRateFeeCode" name="searchPayFeeRateFeeCode" class="easyui-textbox" value=""  style="width:130px"/>
    费率名称<input type="text" id="searchPayFeeRateFeeName" name="searchPayFeeRateFeeName" class="easyui-textbox" value=""  style="width:130px"/>
    客户类型<select class="easyui-combobox" panelHeight="auto" id="searchPayFeeRateCustType" data-options="editable:false" name="searchPayFeeRateCustType">
    	   <option value="">请选择</option>
           <option value="0">个人</option>
           <option value="1">商户</option>
           <option value="2">支付渠道</option>
	    </select>
    类型<select class="easyui-combobox" panelHeight="auto" id="searchPayFeeRateTranType" data-options="editable:false" name="searchPayFeeRateTranType">
    	   <option value="">请选择</option>
           <option value="1">消费</option>
           <option value="2">充值</option>
           <option value="3">结算</option>
           <option value="4">退款</option>
           <option value="5">提现</option>
           <option value="6">转账</option>
           <option value="13">代理</option>
           <option value="15">代收</option>
		   <option value="16">代付</option>
	    </select>
    <%if(actionSearch != null){//角色判断%>
    <a href="javascript:searchPayFeeRateList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
    <%} %>
    <%if(actionAdd != null){//角色判断%>
    <a href="javascript:addPayFeeRate()" class="easyui-linkbutton"
        iconCls="icon-add"><%=actionAdd.name %></a>
    <%} %>
</div>
<script type="text/javascript">
function formatPayFeeRateOperator(val,row,index){
     var tmp=
         <%if(actionDetail != null){//角色判断%>
         "<a href=\"javascript:payFeeRateDetail('"+row.feeCode+"')\"><%=actionDetail.name %></a>&nbsp;&nbsp;&nbsp;"+
         <%}%>
         <%if(actionRemove != null){//角色判断%>
        (0 == row.zeroFeeFlag ? "" : "<a href=\"javascript:removePayFeeRate('"+row.feeCode+"')\"><%=actionRemove.name %></a>&nbsp;")+
         <%}%>
         "";
     return tmp;
}
$('#payFeeRateList').datagrid({
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
    }
});
function searchPayFeeRateList(){        
    $('#payFeeRateList').datagrid('load',{
          feeCode:$('#searchPayFeeRateFeeCode').val(),
          feeName:$('#searchPayFeeRateFeeName').val(),
          custType:$('#searchPayFeeRateCustType').combobox('getValue'),
          tranType:$('#searchPayFeeRateTranType').combobox('getValue')
          /* calMode:$('#searchPayFeeRateCalMode').combobox('getValue') */
    });  
}
function addPayFeeRate(){
	var tabTmp = $('#mainTabs'),id='payFeeRateAdd',title='添加费率';
	if(tabTmp.tabs('exists',title))$('#mainTabs').tabs('close',title);
	tabTmp.tabs('add',{id:id,title: title,closable: true,border:false});
	$('#'+id).panel('refresh','<%=path %>/jsp/pay/fee/pay_fee_rate_add.jsp');
	tabTmp.tabs('select',title);
}
function payFeeRateDetail(feeCode){
	var tabTmp = $('#mainTabs'),id='payFeeRateDetail',title='费率详情';
	if(tabTmp.tabs('exists',title))$('#mainTabs').tabs('close',title);
	tabTmp.tabs('add',{id:id,title: title,closable: true,border:false});
	$('#'+id).panel('refresh','<%=path %>/payFeeRateDetail.htm?feeCode='+feeCode);
	tabTmp.tabs('select',title);
}
function removePayFeeRate(feeCode){
    $.messager.confirm('提示', '确认删除?', function(r){
    if(!r)return;
    $('#testUserList').datagrid('loading');
    try{
        $.post('<%=path %>/removePayFeeRate.htm',
            {feeCode:feeCode},
            function(data){
                $('#payFeeRateList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','删除成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            },
           'json'
        );
    }catch(e){alert(e);}
   });
}


function tranTypeFormatter(data,row,index){
	if(data == "1") return "消费";
	else if(data == "2") return "充值";
	else if(data == "3") return "结算";
	else if(data == "4") return "退款";
	else if(data == "5") return "提现";
	else if(data == "6") return "转账";
	else if(data == "13") return "代理";
	else if(data == "15") return "代收";
	else if(data == "16") return "代付";
}
function custTypeFormatter(data,row,index){
	if(data == "<%=com.PayConstant.CUST_TYPE_USER %>") {
		return "个人";
	}else if(data == "<%=com.PayConstant.CUST_TYPE_MERCHANT %>") {
		return "商户";
	}else if(data == "<%=com.PayConstant.CUST_TYPE_PAY_CHANNEL %>") {
		return "支付渠道";
	}
}
</script>
