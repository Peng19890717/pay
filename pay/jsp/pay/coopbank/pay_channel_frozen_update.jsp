<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.dao.PayChannelFrozen"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayChannelFrozen payChannelFrozen = (PayChannelFrozen)request.getAttribute("payChannelFrozenUpdate");
//if(payChannelFrozen.status.equals(0)){
//	payChannelFrozen.status="初始";
//}else if(payChannelFrozen.status.equals(1)){
//    payChannelFrozen.status="处理中";
//}else 
//    payChannelFrozen.status="完结";
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payChannelFrozen != null){ %>
        <form id="updatePayChannelFrozenForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                 
                    <tr>
                        <td align="right">渠道号：</td>
                        <%PayCoopBank c = PayCoopBankService.CHANNEL_MAP_ALL.get(payChannelFrozen.channel);%>
                        <td><%=c == null ? payChannelFrozen.channel : c.bankName %></td>
                    </tr>
                    <tr>
                        <td align="right">初始冻结金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayChannelFrozenSrcAmtTmp" name="srcAmtTmp" missingMessage="请输入初始冻结金额t"
                                data-options="required:true" precision="2" max="99999999999" 
                                value="<%=payChannelFrozen.srcAmt != null ? String.format("%.2f",(float)payChannelFrozen.srcAmt*0.01) : "" %>"/>&nbsp;&nbsp;元</td>
                    </tr> 
                    <tr>
                        <td align="right">商户号：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayChannelFrozenMerNos" name="merNos" missingMessage="请输入商户号"
                                validType="length[1,20]" invalidMessage="merNos为1-20个字符" data-options="required:true"
                                value="<%=payChannelFrozen.merNos != null ? payChannelFrozen.merNos : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">冻结开始时间：</td>
                        <td><%=payChannelFrozen.frozenTime != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(payChannelFrozen.frozenTime):"" %></td>
                    </tr>
                    <tr>
                        <td align="right">订单金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayChannelFrozenOrderTxamtTmp" name="orderTxamtTmp" missingMessage="请输入订单金额"
                                data-options="required:true" precision="2" max="99999999999" invalidMessage=""
                                value="<%=payChannelFrozen.orderTxamt != null ? String.format("%.2f",(float)payChannelFrozen.orderTxamt*0.01) : "" %>"/>&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right">冻结订单号：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayChannelFrozenOrderIds" name="orderIds" missingMessage="请输入冻结订单号"
                                validType="length[1,200]" invalidMessage="冻结订单号为1-200个字符" data-options="required:true"
                                value="<%=payChannelFrozen.orderIds != null ? payChannelFrozen.orderIds : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">冻结时长：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayChannelFrozenFrozenDays" name="frozenDays" missingMessage="请输入冻结时长"
                                data-options="required:true" validType="length[1,10]" invalidMessage="请输入正确冻结时长"
                                value="<%=payChannelFrozen.frozenDays != null ? payChannelFrozen.frozenDays : "" %>"/>&nbsp;&nbsp;天</td>
                    </tr>
                    <tr>
                        <td align="right">处理结果：</td>
                               <td><select class="easyui-combobox" id="updatePayChannelFrozenStatus" name="status" data-options="required:true" style="width:170px">
					           <option value="0" <c:if test="${payChannelFrozenUpdate.status eq '0'}">selected</c:if> >初始</option>
					           <option value="1" <c:if test="${payChannelFrozenUpdate.status eq '1'}">selected</c:if> >处理中</option>
					           <option value="2" <c:if test="${payChannelFrozenUpdate.status eq '2'}">selected</c:if> >完结</option>
		     			    </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">剩余冻结金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="updatePayChannelFrozenCurAmtTmp" name="curAmtTmp" missingMessage="请输入剩余冻结金额"
                                data-options="required:true" precision="2" max="99999999999" invalidMessage=""
                                value="<%=payChannelFrozen.curAmt != null ? String.format("%.2f",(float)payChannelFrozen.curAmt*0.01) : "" %>"/>&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right">业务员编号：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayChannelFrozenSalemanId" name="salemanId" missingMessage="请输入业务员编号"
                                validType="length[1,50]" invalidMessage="业务员编号为1-50个字符" data-options="required:true"
                                value="<%=payChannelFrozen.salemanId != null ? payChannelFrozen.salemanId : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">备注：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayChannelFrozenRemark" name="remark" data-options="multiline:true" style="width:240px;height:70px"
                                value="<%=payChannelFrozen.remark != null ? payChannelFrozen.remark : "" %>"/></td>
                    </tr>              
                    <input type="hidden" name="srcAmt" id="updatePayChannelFrozenSrcAmt" value="<%=payChannelFrozen.srcAmt%>">
                    <input type="hidden" name="orderTxamt" id="updatePayChannelFrozenOrderTxamt" value="<%=payChannelFrozen.orderTxamt%>">
                    <input type="hidden" name="curAmt" id="updatePayChannelFrozenCurAmt" value="<%=payChannelFrozen.curAmt%>">
                    <input type="hidden" name="id" value="<%= payChannelFrozen.id%>">
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayChannelFrozenFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payChannelFrozenUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayChannelFrozenForm').form({
    url:'<%=path %>/updatePayChannelFrozen.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payChannelFrozenList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');                 
            closeTabFreshList('payChannelFrozen',payChannelFrozenUpdatePageTitle,payChannelFrozenListPageTitle,'payChannelFrozenList','<%=path %>/payChannelFrozen.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
$('#updatePayChannelFrozenSrcAmtTmp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("updatePayChannelFrozenSrcAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});
$('#updatePayChannelFrozenOrderTxamtTmp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("updatePayChannelFrozenOrderTxamt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});
$('#updatePayChannelFrozenCurAmtTmp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("updatePayChannelFrozenCurAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});
function updatePayChannelFrozenFormSubmit(){
    $('#updatePayChannelFrozenForm').submit();
}
</script>
