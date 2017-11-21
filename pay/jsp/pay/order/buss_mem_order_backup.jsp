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
JWebAction actionSearch = ((JWebAction)user.actionMap.get("qgrO8Iw2Q3rKTPg1"));
String merno = request.getParameter("merno");
String createtimeStart = request.getParameter("createtimeStart");
String createtimeEnd = request.getParameter("createtimeEnd");
String ordstatus = request.getParameter("ordstatus");//9999970.72
request.setAttribute("ordstatusShow", ordstatus);
%>
<script type="text/javascript">
var detailPayOrderPageTitle="订单详情";
</script>
<div class="easyui-layout" data-options="fit:true">
	 <div data-options="region:'center'">
		<table id="payBussMemOrderBackUpList" style="width:132%;height:100%" rownumbers="true" pagination="true"
		    fit="true" data-options="border:false,collapsible:true,autoRowHeight:true,pageSize:20,singleSelect:true,url:'<%=path 
		        %>/bussMemOrderBackUp.htm?flag=0',method:'post',toolbar:'#payBussMemOrderBackUpListToolBar'">
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
	<div id="payBussMemOrderBackUpListToolBar" data-options="border:false" style="text-align:left;padding:5px 5px 5px 5px;">
		商品订单号<input type="text" id="searchPayOrderBackUpPrdordno" name="searchPayOrderBackUpPrdordno" class="easyui-textbox" value=""  style="width:130px"/>
		支付订单号<input type="text" id="searchPayOrderBackUpPayordno" name="searchPayOrderBackUpPayordno" class="easyui-textbox" value=""  style="width:130px"/>
		商户编号<input type="text" id="searchPayOrderBackUpMerno" name="searchPayOrderBackUpMerno" class="easyui-textbox" value="<%=merno==null?"":merno %>"  style="width:130px"/>
		商品名称<input type="text" id="searchPayOrderBackUpPrdname" name="searchPayOrderBackUpPrdname" class="easyui-textbox" value=""  style="width:130px"/>
 		订单日期<input class="easyui-datebox" style="width:100px"  data-options="editable:false" id="searchPayOrderBackUpCreateTimeStart" value="<%=createtimeStart==null?"":createtimeStart %>" name="searchPayOrderBackUpCreateTimeStart"/>
 		~<input class="easyui-datebox" style="width:100px" data-options="editable:false" id="searchPayOrderBackUpCreateTimeEnd" name="searchPayOrderBackUpCreateTimeEnd" value="<%=createtimeEnd==null?"":createtimeEnd %>"/>
 		支付状态<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderBackUpOrdstatus" 
			data-options="editable:false" name="searchPayOrderBackUpOrdstatus">
	    		   <option value="">请选择</option>
	               <option value="00">等待付款</option>
	               <option value="01" <c:if test="${ ordstatusShow eq '01'}">selected</c:if>>付款成功</option>
	               <option value="02">付款失败</option>
	    </select>
 		业务员<select class="easyui-combobox" panelHeight="auto" id="searchPayOrderBackUpOrdByPerson" 
			data-options="editable:false" name="searchPayOrderBackUpOrdByPerson">
        		   <option value="">请选择</option>
	               <option value="myself">本人</option>
	    </select>
	    <%if(actionSearch != null){//角色判断%>
	    <a href="javascript:searchpayBussMemOrderBackUpList()" class="easyui-linkbutton" iconCls="icon-search"><%=actionSearch.name %></a>
	    <%} %>
	</div>
	<div data-options="region:'south',border:false" style="text-align:left;margin-left:7px;margin-top:5px;margin-bottom:5px;">
		<span id="getBussMemBackTotalMoneyId">&nbsp;</span>
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
var searchPayOrderBackUpBankcodData=[<%=bankNames %>];
$('#payBussMemOrderBackUpList').datagrid({
	onBeforeLoad: function(param){
		param.flag='0';
		<%if(merno!=null){%>
        param.merno=$('#searchPayOrderBackUpMerno').val();
        <%}%>
        <%if(createtimeStart!=null){%>
        try{param.createtimeStart=$('#searchPayOrderBackUpCreateTimeStart').datebox('getValue');}catch(e){
        	param.createtimeStart=document.getElementById("searchPayOrderBackUpCreateTimeStart").value;}
        <%}%>
        <%if(createtimeEnd!=null){%>
        try{param.createtimeEnd=$('#searchPayOrderBackUpCreateTimeEnd').datebox('getValue');}catch(e){
        	param.createtimeEnd=document.getElementById("searchPayOrderBackUpCreateTimeEnd").value;}
        <%}%>
        <%if(ordstatus!=null){%>
        try{param.ordstatus=$('#searchPayOrderBackUpOrdstatus').combobox('getValue');}catch(e){
        	param.ordstatus=document.getElementById("searchPayOrderBackUpOrdstatus").value;}
        <%}%>
    },
    onLoadError: function(data){
        topCenterMessage('<%=JWebConstant.ERROR %>','数据加载失败！');
    },
    //data就是datagrid加载的json串
    onLoadSuccess: function(data){
        $(this).datagrid('fixRownumber');//行号自适应宽度
        $("#getBussMemBackTotalMoneyId").html("订单总金额："+(parseFloat(data.totalOrderMoney)*0.01).toFixed(2)
        	+"元，手续费总额："+(parseFloat(data.totalFeeMoney)*0.01).toFixed(2)+"元，总结算金额："
        	+(parseFloat(data.totalAppMoney)*0.01).toFixed(2)+"元");
    }
});
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
function searchpayBussMemOrderBackUpList(){
	try{
    $('#payBussMemOrderBackUpList').datagrid('load',{
    	  //把以下的参数传到后台通过SQL语句进行查询
          prdordno:$('#searchPayOrderBackUpPrdordno').val(),//商品订单号
          payordno:$('#searchPayOrderBackUpPayordno').val(),//支付订单号
          merno:$('#searchPayOrderBackUpMerno').val(),//商户编号
          storeName:$('#searchPayOrderBackUpPrdname').val(),//商户名称
          createtimeStart:$('#searchPayOrderBackUpCreateTimeStart').datebox('getValue'),
          createtimeEnd:$('#searchPayOrderBackUpCreateTimeEnd').datebox('getValue'),
          ordstatus:$('#searchPayOrderBackUpOrdstatus').combobox('getValue'),//支付状态
          searchPayOrderOrdByPerson:$('#searchPayOrderBackUpOrdByPerson').combobox('getValue')//业务员
    });
    }catch(e){alert(e);} 
}
//商户补单
function merchantSingleSupplementPayBussMemOrderWindowOpen(payordno){
    $.messager.confirm('提示', '确认要进行商户补单?', function(r){
    if(!r)return;
    $('#payBussMemOrderBackUpList').datagrid('loading');
    try{
        $.post('<%=path %>/notifyMer.htm',
            {payordno:payordno},
            function(data){
                $('#payBussMemOrderBackUpList').datagrid('reload');
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
