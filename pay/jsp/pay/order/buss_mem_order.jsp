<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%@ page import="com.pay.cardbin.service.PayCardBinService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("q4t4e8d2Q3rKTP12"));
//商户补单
JWebAction actionMerchantSingleSupplement = ((JWebAction)user.actionMap.get("qaNmhXe2Q3rKTOM2"));
//渠道补单
JWebAction channelQuery = ((JWebAction)user.actionMap.get("qaNmhXe2Q3rKTOM1"));
String merno = request.getParameter("merno");
String createtimeStart = request.getParameter("createtimeStart");
String createtimeEnd = request.getParameter("createtimeEnd");
String ordstatus = request.getParameter("ordstatus");//9999970.72
request.setAttribute("ordstatusShow", ordstatus);
int PAY_DB_BAK_TRANS_DAYS=0;
try{PAY_DB_BAK_TRANS_DAYS = Integer.parseInt(com.PayConstant.PAY_CONFIG.get("PAY_DB_BAK_TRANS_DAYS"));}catch(Exception e){}
%>
<script type="text/javascript">
var detailPayOrderPageTitle="订单详情";
</script>
<div class="easyui-layout" data-options="fit:true">
	 <div data-options="region:'center'">
		<table id="payBussMemOrderList" style="width:132%;height:100%" rownumbers="true" pagination="true"
		    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,method:'post',toolbar:'#payBussMemOrderListToolBar'">
		       <thead>
		        <tr>
		           <th field="merno" width="5%" align="left" sortable="true">商户编号</th>
		           <th field="storeName" width="7%" align="left" sortable="true">商户名称</th>
		           <th field="custName" width="5%" align="left" sortable="true">充值账户号（名称）</th>
		           <th field="prdordno" width="5%" align="left" sortable="true">商品订单号</th>
		           <th field="createtime" width="7%" align="left" sortable="true">订单创建时间</th>
		           <th field="prdname" width="5%" align="left" sortable="true">商品名称</th>
		           <th field="txamt" width="5%" align="right" sortable="true">订单金额</th>
<!-- 		           <th field="fee" width="5%" align="right" sortable="true">手续费</th>
		           <th field="netrecamt" width="5%" align="right" sortable="true">结算金额</th> -->
		           <th field="payordno" width="9%" align="left" sortable="true">支付订单号</th>
		           <th field="actdat" width="7%" align="left" sortable="true">支付完成时间</th>
		           <th field="ordstatus" width="5%" align="left" sortable="true" formatter="format_ordstatus">支付状态</th>
		           <th field="paytype" width="5%" align="left" sortable="true" formatter="format_payType">支付方式</th>
		           <th field="payChannel" width="5%" align="left" sortable="true">支付渠道</th>
		           <th field="prdordstatus" width="5%" align="left" sortable="true" formatter="format_proordstatus">订单状态</th>
		           <th field="prdordtype" width="5%" align="left" sortable="true" formatter="format_prdordtype">订单类型</th>
		           <th field="operation" formatter="formatPayBussMemOrderOperator" width="5%" align="left">操作</th>
		           <th field="bankerror" width="5%" align="left" sortable="true">描述</th>
		       </tr>
		       </thead>
		</table>
	</div>
	<div id="payBussMemOrderListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	<form id="payBussMemOrderForm" method="post">
		<select class="easyui-combobox" name="timeRange" id="timeRangeForExcel">
            <option value="0"><%=PAY_DB_BAK_TRANS_DAYS==0?"近期交易":""+PAY_DB_BAK_TRANS_DAYS+"天之内交易" %></option>
            <option value="1"><%=PAY_DB_BAK_TRANS_DAYS==0?"历史交易":""+PAY_DB_BAK_TRANS_DAYS+"天之前交易" %></option>
        </select>
		商品订单号<input type="text" id="searchPayOrderPrdordno" name="searchPayOrderPrdordno" class="easyui-textbox" value=""  style="width:130px"/>
		支付订单号<input type="text" id="searchPayOrderPayordno" name="searchPayOrderPayordno" class="easyui-textbox" value=""  style="width:130px"/>
		商户编号<input type="text" id="searchPayOrderMerno" name="searchPayOrderMerno" class="easyui-textbox" value="<%=merno==null?"":merno %>"  style="width:130px"/>
<!-- 		商品名称<input type="text" id="searchPayOrderPrdname" name="searchPayOrderPrdname" class="easyui-textbox" value=""  style="width:130px"/> -->
 		订单日期<input class="easyui-datebox" style="width:100px"  data-options="required:true,editable:false" id="searchPayMemOrderCreateTimeStart" value="<%=createtimeStart==null?"":createtimeStart %>" name="searchPayMemOrderCreateTimeStart"/>
 		~<input class="easyui-datebox" style="width:100px" data-options="editable:false" id="searchPayMemOrderCreateTimeEnd" name="searchPayMemOrderCreateTimeEnd" value="<%=createtimeEnd==null?"":createtimeEnd %>"/>
 		支付状态<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderOrdstatus" 
			data-options="editable:false" name="searchPayOrderOrdstatus">
	    		   <option value="">请选择</option>
	               <option value="00">等待付款</option>
	               <option value="01" <c:if test="${ ordstatusShow eq '01'}">selected</c:if>>付款成功</option>
	               <option value="02">付款失败</option>
	    </select>
 		业务员<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderOrdByPerson" 
			data-options="editable:false" name="searchPayOrderOrdByPerson">
        		   <option value="">请选择</option>
	               <option value="myself">本人</option>
	    </select>
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchpayBussMemOrderList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
	    <%} %>
	</form>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<span id="getBussMemTotalMoneyId">&nbsp;</span>
	</div>
</div>
<script type="text/javascript">
//获取支付行
<%
String bankNames="";
java.util.Iterator<String> ite = PayCardBinService.BANK_CODE_NAME_MAP.keySet().iterator();
	while(ite.hasNext()){
		String key = ite.next();
		String value = (String)PayCardBinService.BANK_CODE_NAME_MAP.get(key);
  		bankNames = bankNames+"{\"id\":\""+key+"\",\"text\":\""+value+"\"},";
	}
if(bankNames.endsWith(","))bankNames = bankNames.substring(0,bankNames.length()-1); 
%>
var searchPayOrderBankcodData=[<%=bankNames %>];
$('#payBussMemOrderList').datagrid({
	onBeforeLoad: function(param){
		param.flag='0';
		<%if(merno!=null){%>
        param.merno=$('#searchPayOrderMerno').val();
        <%}%>
        <%if(createtimeStart!=null){%>
        try{param.createtimeStart=$('#searchPayMemOrderCreateTimeStart').datebox('getValue');}catch(e){
        	param.createtimeStart=document.getElementById("searchPayMemOrderCreateTimeStart").value;}
        <%}%>
        <%if(createtimeEnd!=null){%>
        try{param.createtimeEnd=$('#searchPayMemOrderCreateTimeEnd').datebox('getValue');}catch(e){
        	param.createtimeEnd=document.getElementById("searchPayMemOrderCreateTimeEnd").value;}
        <%}%>
        <%if(ordstatus!=null){%>
        try{param.ordstatus=$('#searchPayOrderOrdstatus').combobox('getValue');}catch(e){
        	param.ordstatus=document.getElementById("searchPayOrderOrdstatus").value;}
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    //data就是datagrid加载的json串
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#getBussMemTotalMoneyId").html("订单总金额："+(parseFloat(data.totalOrderMoney)*0.01).toFixed(2)
        	+"元，手续费总额："+(parseFloat(data.totalFeeMoney)*0.01).toFixed(2)+"元，总结算金额："
        	+(parseFloat(data.totalAppMoney)*0.01).toFixed(2)+"元");
    }
});
function formatPayBussMemOrderOperator(data,row,index){
    var tmp=
        //商户补单
        <%if(actionMerchantSingleSupplement != null){//角色判断%>
        (row.ordstatus=='01'?"<a href=\"javascript:merchantSingleSupplementPayBussMemOrderWindowOpen('"+row.payordno+"')\">&nbsp;&nbsp;<%=actionMerchantSingleSupplement.name %></a>":"")+
        <%}%>
        //渠道补单
        <%if(channelQuery != null){//角色判断%>
        ((row.ordstatus!='01' && row.payChannel!=undefined && row.payChannel!='')?"<a href=\"javascript:payBussMemOrderchannelQuery('"+row.payordno+"')\">&nbsp;&nbsp;<%=channelQuery.name %></a>":"")+
        <%}%>
        "";
    return tmp;
}
function format_payType(data,row, index){
	if(data=="00"){
		return "支付帐号";
	}else if(data=="01"){
		return "网上银行";
	}else if(data=="02"){
		return "终端";
	}else if(data=="03"){
		return "快捷支付";
	}else if(data=="04"){
		return "混合支付";
	}else if(data=="05"){
		return "支票/汇款";
	}else if(data=="07"){
		return "微信扫码";
	}else if(data=="10"){
		return "微信WAP";
	}else if(data=="11"){
		return "支付宝扫码";
	}else{
		return "";
	}
}
function searchpayBussMemOrderList(){
	try{
		if(!$('#payBussMemOrderForm').form('validate'))return;
		$('#payBussMemOrderList').datagrid({
			url:'<%=path %>/bussMemOrder.htm?flag=0',
			queryParams:{
			prdordno:$('#searchPayOrderPrdordno').val(),//商品订单号
	        payordno:$('#searchPayOrderPayordno').val(),//支付订单号
	        merno:$('#searchPayOrderMerno').val(),//商户编号
	        storeName:$('#searchPayOrderPrdname').val(),//商户名称
	        createtimeStart:$('#searchPayMemOrderCreateTimeStart').datebox('getValue'),
	        createtimeEnd:$('#searchPayMemOrderCreateTimeEnd').datebox('getValue'),
	        ordstatus:$('#searchPayOrderOrdstatus').combobox('getValue'),//支付状态
			timeRange:$('#timeRangeForExcel').combobox('getValue'),
	        searchPayOrderOrdByPerson:$('#searchPayOrderOrdByPerson').combobox('getValue')//业务员
	    }
	});
	}catch(e){alert(e);} 
}
//商户补单
function merchantSingleSupplementPayBussMemOrderWindowOpen(payordno){
    $.messager.confirm('提示', '确认要进行商户补单?', function(r){
    if(!r)return;
    $('#payBussMemOrderList').datagrid('loading');
    try{
        $.post('<%=path %>/notifyMer.htm',
            {payordno:payordno},
            function(data){
                $('#payBussMemOrderList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','补单成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        );
    }catch(e){alert(e);}
   });
}
//渠道补单
function payBussMemOrderchannelQuery(payordno){
    $.messager.confirm('提示', '确认要进行渠道补单?', function(r){
    if(!r)return;
    $('#payBussMemOrderList').datagrid('loading');
    try{
        $.post('<%=path %>/channelQuery.htm',
            {payordno:payordno},
            function(data){
                $('#payBussMemOrderList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','补单成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        );
    }catch(e){alert(e);}
   });
}

function format_paytype(data,row, index){
	if(data=="00"){
		return "支付帐号";
	}else if(data=="01"){
		return "网上银行";
	}else if(data=="02"){
		return "终端";
	}else if(data=="03"){
		return "快捷支付";
	}else if(data=="04"){
		return "混合支付";
	}else if(data=="05"){
		return "支票/汇款";
	}else if(data=="07"){
		return "微信扫码";
	}else if(data=="10"){
		return "微信WAP";
	}else if(data=="11"){
		return "支付宝扫码";
	}else{
		return "";
	}
}
function format_payterminal(data,row, index){
	if(data=="0"){
		return "PC";
	}else if(data=="1"){
		return "手机";
	}else{
		return "PC";
	}
}
function format_ordstatus(data,row, index){
	if(data=="00"){
		return "等待付款";
	}else if(data=="01"){
		return "付款成功";
	}else if(data=="02"){
		return "付款失败";
	}else{
		return "";
	}
}
function format_proordstatus(data,row, index){
	if(data=="00"){
		return "等待付款";
	}else if(data=="01"){
		return "付款成功";
	}else if(data=="02"){
		return "付款失败";
	}else if(data=="03"){
		return "支付处理中";
	}else if(data=="04"){
		return "退款成功";
	}else if(data=="05"){
		return "退款失败";
	}else if(data=="06"){
		return "撤销成功";
	}else if(data=="07"){
		return "撤销失败";
	}else if(data=="08"){
		return "订单作废";
	}else{
		return "";
	}
}
function format_prdordtype(data,row, index){
	if(data=="0"){
		return "消费";
	}else if(data=="1"){
		return "充值";
	}else if(data=="2"){
		return "担保支付";
	}else if(data=="3"){
		return "商户充值";
	}else{
		return "";
	}
}
function format_stlsts(data,row, index){
	if(data=="0"){
		return "未结算";
	} else if(data=="1"){
		return "已清算";
	} else if(data=="2"){
		return "已结算";
	}
}
function format_notifymerflag(data,row, index){
	if(data=="0"){
		return "未通知";
	}else if(data=="1"){
		return "已通知";
	}else if(data=="2"){
		return "通知失败";
	}
}
</script>
