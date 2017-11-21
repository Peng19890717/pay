<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.margin.dao.PayMargin"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayMargin payMargin = (PayMargin)request.getAttribute("payMargin");
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payMargin != null){ %>
        <form id="appendPayMarginForm" method="post">
        <input type="hidden" id="appendPayMarginSeqNo" name="seqNo" value="<%=payMargin.seqNo %>"/>
        <input type="hidden" id="appendPayMarginCustId" name="custId" value="<%=payMargin.custId %>"/>
        <input type="hidden" id="appendPayMarginPactNo" name="pactNo" value="<%=payMargin.pactNo %>"/>
        <input type="hidden" id="appendPayMarginPaidinAmt" name="paidInAmt" value="0"/>
        <table cellpadding="5" width="100%">
            <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
            <tr>
                <td align="right">客户编号：</td>
                <td>${payMargin.custId}</td>
            </tr>
            <tr>
                <td align="right">合同编号：</td>
                <td>${payMargin.pactNo}</td>
            </tr>
            <tr>
                <td align="right">已缴纳金额：</td>
                <td><%=String.format("%.2f",(float)payMargin.paidInAmt*0.01) %>元</td>
            </tr>
            <tr>
                <td align="right">扣缴金额：</td>
                <td><input class="easyui-numberbox" type="text" id="appendPayMarginPaidInAmtTmp" name="paidInAmtTmp" missingMessage="请输入金额"
                        data-options="required:true" validType="appendCheckMargin"
                        value="0" precision="2" max="99999999999"/>&nbsp;&nbsp;元
                    <input type="radio" name="PaidType" value="add" onclick="appendPayMarginPaidInAmtCheck()" checked>续费
                    <input type="radio" name="PaidType" value="cut" onclick="appendPayMarginPaidInAmtCheck()">扣费
                </td>
            </tr>
            <tr>
                <td align="right" valign="top">备注：</td>
                <td><input class="easyui-textbox" type="text" id="appendPayMarginMark" name="mark"
                    	validType="length[0,100]" invalidMessage="备注内容请控制在100个字符" data-options="multiline:true" 
                    	style="width:240px;height:70px"/></td>
            </tr>
        </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:center;padding:5px;">
    	<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:appendPayMarginFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payMarginAppendPageTitle)" style="width:80px">关闭</a>
    </div>
</div>
<script type="text/javascript">
$('#appendPayMarginForm').form({
    url:'<%=path %>/appendPayMargin.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payMargin',payMarginAppendPageTitle,payMarginListPageTitle,'payMarginList','<%=path %>/payMargin.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function appendPayMarginPaidInAmtCheck(){
	$('#appendPayMarginPaidInAmtTmp').numberbox("setValue",$('#appendPayMarginPaidInAmtTmp').val());
}
$.extend($.fn.validatebox.defaults.rules, {
	appendCheckMargin:{
		validator:function (value, param) {
		    var type=document.getElementById("appendPayMarginForm").PaidType.value;
			if(value.length>0){
				if(parseFloat(value)==0){
					$.fn.textbox.defaults.rules.appendCheckMargin.message = '请输入金额';
					return false;
				}
				if("add"==type){
					document.getElementById("appendPayMarginPaidinAmt").value = parseFloat(value);
				} else if("cut"==type){
					document.getElementById("appendPayMarginPaidinAmt").value = 0-parseFloat(value);
					if(parseFloat(value)><%=(float)payMargin.paidInAmt %>*0.01){
						$.fn.textbox.defaults.rules.appendCheckMargin.message = '所扣金额过多';
						return false;
					}
				}
			}
			return true;
		}
	}
});
function appendPayMarginFormSubmit(){
    $('#appendPayMarginForm').submit();
}
</script>