<%@page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%@ page import="com.pay.cardbin.service.PayCardBinService"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.PayConstant"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
JWebAction actionSearch = ((JWebAction)user.actionMap.get("puhUUgP2Q3rKTOI4"));
JWebAction actionDetail = ((JWebAction)user.actionMap.get("puhUUgP2Q3rKTOI3"));
JWebAction actionRefundDetail = ((JWebAction)user.actionMap.get("puWlQJt2Q3rKTOO1"));
JWebAction actionMerchantSingleSupplement = ((JWebAction)user.actionMap.get("pzswXFM2Q3rKTOM1"));
JWebAction channelQuery = ((JWebAction)user.actionMap.get("pzsWVsr2Q3rKTOW1"));
JWebAction settlementPayProOrder = ((JWebAction)user.actionMap.get("q3rS4yZ2Q3rKTP61"));
String merno = request.getParameter("merno");
String createtimeStart = request.getParameter("createtimeStart");
String createtimeEnd = request.getParameter("createtimeEnd");
String ordstatus = request.getParameter("ordstatus");
request.setAttribute("ordstatusShow", ordstatus);
int PAY_DB_BAK_TRANS_DAYS=0;
try{PAY_DB_BAK_TRANS_DAYS = Integer.parseInt(com.PayConstant.PAY_CONFIG.get("PAY_DB_BAK_TRANS_DAYS"));}catch(Exception e){}
String orderTimeRange="";
if(createtimeStart!=null && PAY_DB_BAK_TRANS_DAYS!=0){//历史订单标识查询
    try{
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(createtimeStart);
		Date today = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		int tmp = (int) ((today.getTime() - startDate.getTime()) / (1000*3600*24));
		if(tmp>PAY_DB_BAK_TRANS_DAYS)orderTimeRange="1";
		else orderTimeRange="0";
	} catch(Exception e){}
}
%>
<script type="text/javascript">
var detailPayOrderPageTitle="订单详情";
function payOrderExportExcel(){
	$.messager.confirm('提示', '您确认将当前记录导出至excel吗？', function(r){
       if(!r)return;
	    $('#payMerchantSettlementList').datagrid('loading');
	    try{
			window.location.href="<%=path %>/payOrderExportExcel.htm?"+
		 	"prdordno="+$('#searchPayOrderPrdordno').val()+//商品订单号
	        "&payordno="+$('#searchPayOrderPayordno').val()+//支付订单号
 		    "&txamtstart="+$('#searchPayOrderTxamtStart').val()+//订单金额 不能在这里乘100 否则会默认为0
		    "&txamtend="+$('#searchPayOrderTxamtEnd').val()+//订单金额右限 
	       	"&createtimeStart="+$('#searchPayOrderCreateTimeStart').datebox('getValue')+
	       	"&createtimeEnd="+$('#searchPayOrderCreateTimeEnd').datebox('getValue')+
	       	"&merno="+$('#searchPayOrderMerno').val()+//商户编号
/* 	       	"&storeName="+$('#searchPayOrderPrdname').val()+//商户名称
	       	"&prdname="+$('#searchPayOrderPrdname').val()+//商品名称 */
/* 	       	"&prdordstatus="+$('#searchPayOrderPrdordstatus').combobox('getValue')+//订单状态 */
	       	"&notifyMerFlag="+$('#searchPayOrderNotifyMerFlag').combobox('getValue')+//订单状态
	       	"&ordstatus="+$('#searchPayOrderOrdstatus').combobox('getValue')+//支付状态
/* 	       	"&stlsts="+$('#searchPayOrderStlsts').combobox('getValue')+//结算状态 */
	       	"&paytype="+$('#searchPayOrderPaytype').combobox('getValue')+
/* 			"&termtyp="+$('#searchPayOrderPayTermtyp').combobox('getValue')+ */
	       	"&payChannel="+$('#searchPayOrderPayChannel').combobox('getValue')+
	       	"&bankcod="+$('#searchPayOrderBankcod').combobox('getValue')+
	       	"&timeRange="+$('#searchPayOrderTimeRange').combobox('getValue');
   	  	}catch(e){alert(e);}
   	});
}

</script>
<div class="easyui-layout" data-options="fit:true">
	 <div data-options="region:'center'">
		<table id="payOrderList" style="width:132%;height:100%" rownumbers="true" pagination="true"
		    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,method:'post',toolbar:'#payOrderListToolBar'">
		       <thead>
		        <tr>
		           <th field="merno" width="8%" align="left"  >商户编号</th>
		           <th field="storeName" width="13%" align="left"  >商户名称</th>
		           <th field="custName" width="13%" align="left"  >充值账户号（名称）</th>
		           <th field="prdordno" width="8%" align="left"  >商品订单号</th>
		           <th field="createtime" width="9%" align="left"  >订单创建时间</th>
		           <th field="prdname" width="9%" align="left"  >商品名称</th>
		           <th field="stlsts" width="8%" align="left"   formatter="format_stlsts">结算状态</th>
		           <th field="txamt" width="7%" align="right"  >订单金额</th>
		           <th field="filed1" width="5%" align="right"  >费率</th>
		           <th field="fee" width="5%" align="right"  >手续费</th>
		           <th field="channelFee" width="5%" align="right">渠道费</th>
		           <th field="agentFee" width="5%" align="right"  >代理费</th>
		           <th field="netrecamt" width="7%" align="right"  >结算金额</th>
		           <th field="payordno" width="9%" align="left"  >支付订单号</th>
		           <th field="actdat" width="9%" align="left"  >支付完成时间</th>
		           <th field="paytype" width="5%" align="left"   formatter="format_paytype">支付方式</th>
		           <th field="payChannel" width="5%" align="left"  >支付渠道</th>
		           <th field="bankcod" width="5%" align="left"  >支付行</th>
		           <th field="termtyp" width="5%" align="left"   formatter="format_payterminal">支付终端</th>
		           <th field="ordstatus" width="4%" align="left"   formatter="format_ordstatus">支付状态</th>
		           <th field="prdordstatus" width="4%" align="left"   formatter="format_proordstatus">订单状态</th>
		           <th field="notifyMerFlag" width="4%" align="left"   formatter="format_notifymerflag">通知状态</th>
		           <th field="prdordtype" width="4%" align="left"   formatter="format_prdordtype">订单类型</th>
		           <!-- <th field="bankpayacno" width="4%" align="left"  >卡号</th>
		           <th field="bankpayusernm" width="4%" align="left"  >姓名</th> -->
		           <th field="bankerror" width="4%" align="left"  >描述</th>
		           <th field="operation" formatter="formatPayOrderOperator" width="10%" align="left">操作</th>
		       </tr>
		       </thead>
		</table>
	</div>
	<div id="payOrderListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
	<form id="searchPayOrderForm" method="post">
		<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderTimeRange" 
			data-options="editable:false" name="searchPayOrderTimeRange">
				<%if(orderTimeRange.length()==0){ %>
	    	   <option value="0"><%=PAY_DB_BAK_TRANS_DAYS==0?"近期交易":""+PAY_DB_BAK_TRANS_DAYS+"天之内交易" %></option>
	           <option value="1"><%=PAY_DB_BAK_TRANS_DAYS==0?"历史交易":""+PAY_DB_BAK_TRANS_DAYS+"天之前交易" %></option>
	           <%} else { %>
	           <option value="0" <%="0".equals(orderTimeRange)?"selected":"" %>><%=PAY_DB_BAK_TRANS_DAYS==0?"近期交易":""+PAY_DB_BAK_TRANS_DAYS+"天之内交易" %></option>
	           <option value="1" <%="1".equals(orderTimeRange)?"selected":"" %>><%=PAY_DB_BAK_TRANS_DAYS==0?"历史交易":""+PAY_DB_BAK_TRANS_DAYS+"天之前交易" %></option>
	           <%} %>
	    </select>
		商品订单号<input type="text" id="searchPayOrderPrdordno" name="searchPayOrderPrdordno" class="easyui-textbox" value=""  style="width:130px"/>
		支付订单号<input type="text" id="searchPayOrderPayordno" name="searchPayOrderPayordno" class="easyui-textbox" value=""  style="width:130px"/>
		商户编号<input type="text" id="searchPayOrderMerno" name="searchPayOrderMerno" class="easyui-textbox" value="<%=merno==null?"":merno %>"  style="width:130px"/>
<!-- 		商品名称<input type="text" id="searchPayOrderPrdname" name="searchPayOrderPrdname" class="easyui-textbox" value=""  style="width:130px"/> -->
 		订单日期<input class="easyui-datebox" style="width:100px"   data-options="required:true,editable:false" id="searchPayOrderCreateTimeStart" value="<%=createtimeStart==null?"":createtimeStart %>" name="searchPayOrderCreateTimeStart"/>
 		<input class="easyui-timespinner" id="searchPayOrderCreateHMSStart" name="searchPayOrderCreateHMSStart" style="width:60px">
 		~<input class="easyui-datebox" style="width:100px" data-options="editable:false" id="searchPayOrderCreateTimeEnd" name="searchPayOrderCreateTimeEnd" value="<%=createtimeEnd==null?"":createtimeEnd %>"/>
 		<input class="easyui-timespinner" id="searchPayOrderCreateHMSEnd" name="searchPayOrderCreateHMSEnd" style="width:60px">
 		支付状态<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderOrdstatus" 
			data-options="editable:false" name="searchPayOrderOrdstatus">
	    		   <option value="">请选择</option>
	               <option value="00">等待付款</option>
	               <option value="01" <c:if test="${ ordstatusShow eq '01'}">selected</c:if>>付款成功</option>
	               <option value="02">付款失败</option>
	    </select>
<!-- 		订单状态<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderPrdordstatus" 
			data-options="editable:false" name="searchPayOrderPrdordstatus">
	    		   <option value="">请选择</option>
	               <option value="00">未付款</option>
	               <option value="01">付款成功</option>
	               <option value="02">付款失败</option>
	               <option value="03">支付处理中</option>
	               <option value="04">退款成功</option>
	               <option value="05">退款失败</option>
	               <option value="06">撤销成功</option>
	               <option value="07">撤销失败</option>
	               <option value="08">订单作废</option>
	    </select><br/> -->
		通知商户状态<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderNotifyMerFlag" 
			data-options="editable:false" name="searchPayOrderNotifyMerFlag">
	    		   <option value="">请选择</option>
	               <option value="0">未通知</option>
	               <option value="1">已通知</option>
	               <option value="2">通知失败</option>
	    </select>
		支付方式<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderPaytype" data-options="editable:false" name="searchPayOrderPaytype">
		    	   <option value="">请选择</option>
		           <option value="00">支付帐户</option>
	               <option value="01">网上银行</option>
	               <option value="03">快捷支付</option>
	               <option value="07">微信扫码</option>
	               <option value="10">微信WAP</option>
	               <option value="11">支付宝扫码</option>
	               <option value="12">QQ扫码</option>
			    </select>
<!-- 		支付终端<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderPayTermtyp" data-options="editable:false" name="searchPayOrderPayTermtyp">
		    	   <option value="">请选择</option>
		           <option value="0">PC</option>
	               <option value="1">手机</option> -->
			    </select>
<!-- 	          结算状态<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderStlsts" data-options="editable:false" name="searchPayOrderStlsts">
		    	   <option value="">请选择</option>
		           <option value="0">未结算</option>
	               <option value="1">已清算</option>
	               <option value="2">已结算</option>
			    </select> -->
 		订单金额（元）<input type="text" id="searchPayOrderTxamtStart" name="searchPayOrderTxamtStart" class="easyui-numberbox" value=""  style="width:70px"/>~
		       <input type="text" id="searchPayOrderTxamtEnd" name="searchPayOrderTxamtEnd" class="easyui-numberbox" value=""  style="width:70px"/>
		<!-- 加支付渠道、支付行 -->
		支付渠道<select class="easyui-combobox" id="searchPayOrderPayChannel" data-options="panelHeight:500,editable:false" name="searchPayOrderPayChannel">
	    	    <option value="">请选择</option>
		        <%	
           			java.util.Iterator<String> it = PayCoopBankService.CHANNEL_MAP_ALL.keySet().iterator();
           			while(it.hasNext()){
           				String key = it.next();
           				String value = ((PayCoopBank)PayCoopBankService.CHANNEL_MAP_ALL.get(key)).getBankName();
           				%><option value="<%=key%>"><%=value%></option>
           			<%}
                %>
			  </select>
		支付行
			<input class="easyui-combobox" id="searchPayOrderBankcod" name="searchPayOrderBankcod"
			data-options="panelHeight:500,valueField:'id',textField:'text',data:searchPayOrderBankcodData"/>
		卡号
			<input type="text" id="searchPayOrderPayAcNo" name="searchPayOrderPayAcNo" class="easyui-textbox" value=""  style="width:130px"/>
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchPayOrderList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
	    <%} %>
	</form>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<%if(actionSearch != null){//角色判断%>
		<a href="javascript:payOrderExportExcel()"  class="easyui-linkbutton" iconCls="icon-config">excel导出</a>
		<%} %>
		<span id="getOrderTotalMoneyId">&nbsp;</span>
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
$('#payOrderList').datagrid({
	onBeforeLoad: function(param){
		<%if(merno!=null){%>
        param.merno=$('#searchPayOrderMerno').val();
        <%}%>
        <%if(createtimeStart!=null){
        //历史订单标识查询
        try{
	        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(createtimeStart);
			Date today = new SimpleDateFormat("yyyy-MM-dd").parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			int tmp = (int) ((today.getTime() - startDate.getTime()) / (1000*3600*24));
			if(tmp>PAY_DB_BAK_TRANS_DAYS){%>
				param.searchPayOrderTimeRange = "1";
			<%}
		}catch(Exception e){}
        %>
        try{param.createtimeStart=$('#searchPayOrderCreateTimeStart').datebox('getValue');}catch(e){
        	param.createtimeStart=document.getElementById("searchPayOrderCreateTimeStart").value;}
        <%}%>
        <%if(createtimeEnd!=null){%>
        try{param.createtimeEnd=$('#searchPayOrderCreateTimeEnd').datebox('getValue');}catch(e){
        	param.createtimeEnd=document.getElementById("searchPayOrderCreateTimeEnd").value;}
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
        $("#getOrderTotalMoneyId").html("订单总金额："+(parseFloat(data.totalOrderMoney)*0.01).toFixed(2)
        	+"元，手续费总额："+(parseFloat(data.totalFeeMoney)*0.01).toFixed(2)+"元，渠道费总额："
        	+(parseFloat(data.totalChannelFee)*0.01).toFixed(2)+"元，总结算金额："
        	+(parseFloat(data.totalAppMoney)*0.01).toFixed(2)+"元，订单总金额 - 渠道费："+(parseFloat(data.totalOrderMoney-data.totalChannelFee)*0.01).toFixed(2)+"元");
    }
});
function formatPayOrderOperator(data,row,index){
     var tmp=
         <%if(actionDetail != null){//角色判断%>
         "<a href=\"javascript:detailPayOrderPageOpen('"+row.payordno+"')\"><%=actionDetail.name %></a>&nbsp;&nbsp;"+
         <%}%>
         //如果该退款订单的退款金额不为0，显示退款列表
         //退款列表  参数：商户编号，商品订单号
         <%if(actionRefundDetail != null){//角色判断%>
         (row.rftotalamt >0?"<a href=\"javascript:refundListPayOrderWindowOpen('"+row.merno+"','"+row.prdordno+"')\">&nbsp;&nbsp;<%=actionRefundDetail.name %></a>":"")+
         <%}%>
         //商户补单
         <%if(actionMerchantSingleSupplement != null){//角色判断%>
         (row.ordstatus=='01'?"<a href=\"javascript:merchantSingleSupplementPayOrderWindowOpen('"+row.payordno+"')\">&nbsp;&nbsp;<%=actionMerchantSingleSupplement.name %></a>":"")+
         <%}%>
         //渠道补单
         <%if(channelQuery != null){//角色判断%>
         ((row.ordstatus!='01' && row.payChannel!=undefined && row.payChannel!='')?"<a href=\"javascript:channelQuery('"+row.payordno+"')\">&nbsp;&nbsp;<%=channelQuery.name %></a>":"")+
         <%}%>
         //结算
         <%if(settlementPayProOrder != null){//角色判断%>
         (row.stlsts=='2'||row.stlsts==undefined?"":"<a href=\"javascript:settlementPayProOrder('"+row.prdordno+"')\">&nbsp;&nbsp;&nbsp;<%=settlementPayProOrder.name %></a>")+
         <%}%>
         "";
     return tmp;
}
function searchPayOrderList(){
	try{
	if(!$('#searchPayOrderForm').form('validate'))return;
	$('#payOrderList').datagrid({
    	url:'<%=path %>/payOrder.htm?flag=0',  
    	queryParams:{  
      	  prdordno:$('#searchPayOrderPrdordno').val(),//商品订单号
          payordno:$('#searchPayOrderPayordno').val(),//支付订单号
          txamtstart:$('#searchPayOrderTxamtStart').val(),//订单金额 不能在这里乘100 否则会默认为0
          txamtend:$('#searchPayOrderTxamtEnd').val(),//订单金额右限 
          createtimeStart:$('#searchPayOrderCreateTimeStart').datebox('getValue'),
          createHMSStart:$('#searchPayOrderCreateHMSStart').timespinner('getValue'),
          createtimeEnd:$('#searchPayOrderCreateTimeEnd').datebox('getValue'),
          createHMSEnd:$('#searchPayOrderCreateHMSEnd').timespinner('getValue'),
          merno:$('#searchPayOrderMerno').val(),//商户编号
/*           storeName:$('#searchPayOrderPrdname').val(),//商户名称 */
/*           prdname:$('#searchPayOrderPrdname').val(),//商品名称 */
/*           prdordstatus:$('#searchPayOrderPrdordstatus').combobox('getValue'),//订单状态 */
          notifyMerFlag:$('#searchPayOrderNotifyMerFlag').combobox('getValue'),//通知状态
          ordstatus:$('#searchPayOrderOrdstatus').combobox('getValue'),//支付状态
/*           stlsts:$('#searchPayOrderStlsts').combobox('getValue'),//结算状态 */
          paytype:$('#searchPayOrderPaytype').combobox('getValue'),
/*           termtyp:$('#searchPayOrderPayTermtyp').combobox('getValue'), */
          payChannel:$('#searchPayOrderPayChannel').combobox('getValue'),
          bankcod:$('#searchPayOrderBankcod').combobox('getValue'),
          payacno:$('#searchPayOrderPayAcNo').val(),
          timeRange:$('#searchPayOrderTimeRange').combobox('getValue')
    	}  
	}); 
    }catch(e){alert(e);} 
}
//结算
function settlementPayProOrder(prdordno){
	$.messager.confirm('提示', '确认要进行结算吗?', function(r){
	    if(!r)return;
	    $('#payOrderList').datagrid('loading');
	    try{
	        $.post('<%=path %>/settlementPayProOrder.htm',
	            {prdordno:prdordno,timeRange:$('#searchPayOrderTimeRange').combobox('getValue')},
	            function(data){
	                $('#payOrderList').datagrid('reload');
	                if(data=='<%=JWebConstant.OK %>'){
	                    topCenterMessage('<%=JWebConstant.OK %>','结算成功！');
	                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	            }
	        );
	    }catch(e){alert(e);}
	   });
}
//跳转到订单详情页面
function detailPayOrderPageOpen(payordno){
    openTab('detailPayOrderPage',detailPayOrderPageTitle,'<%=path %>/payOrderDetail.htm?payordno='+payordno);
}
//跳到退款列表页面 
function refundListPayOrderWindowOpen(merno,prdordno){
	var tabTmp = $('#mainTabs'),title='退款查询';
	/**当前的tab 是否存在如果存在就 将其关闭**/  
	if(tabTmp.tabs('exists', title))$('#mainTabs').tabs('close',title);
    /**添加一个tab标签**/
	tabTmp.tabs('add',{id:'refundList',title: title,selected: true,closable: true,border:false});
    //$('#refundList').panel('refresh','<%=path %>/payRefund.htm');
    $('#refundList').panel('refresh','<%=path %>/jsp/pay/refund/pay_refund.jsp?merno='+merno+'&oriprdordno='+prdordno);
}
//商户补单
function merchantSingleSupplementPayOrderWindowOpen(payordno){
    $.messager.confirm('提示', '确认要进行商户补单?', function(r){
    if(!r)return;
    $('#payOrderList').datagrid('loading');
    try{
        $.post('<%=path %>/notifyMer.htm',
            {payordno:payordno},
            function(data){
                $('#payOrderList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','补单成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        );
    }catch(e){alert(e);}
   });
}

//渠道补单
function channelQuery(payordno){
    $.messager.confirm('提示', '确认要进行渠道补单?', function(r){
    if(!r)return;
    $('#payOrderList').datagrid('loading');
    try{
        $.post('<%=path %>/channelQuery.htm',
            {payordno:payordno},
            function(data){
                $('#payOrderList').datagrid('reload');
                if(data=='<%=JWebConstant.OK %>'){
                    topCenterMessage('<%=JWebConstant.OK %>','补单成功！');
                } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
            }
        );
    }catch(e){alert(e);}
   });
}

$(document).ready(function(){});
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
	}else if(data=="12"){
		return "QQ扫码";
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
