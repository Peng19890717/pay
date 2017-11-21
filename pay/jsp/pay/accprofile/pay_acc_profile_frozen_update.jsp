<%@page import="com.pay.accprofile.dao.PayAccProfileFrozen"%>
<%@page import="com.pay.merchant.dao.PayAccProfileDAO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
%>
<%
	PayAccProfileFrozen payAccProfileFrozen = (PayAccProfileFrozen)request.getAttribute("payAccProfileFrozenUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="updatePayAccProfileFrozenForm" method="post">
        	<input type="hidden" name="amt" id="addPayAccProfileFrozenAmt">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">客户类型：</td>
                        <td>
						    <% if(payAccProfileFrozen.accType.equals("0")){ %>
						    	个人
						    <%} %>
						    <% if(payAccProfileFrozen.accType.equals("1")){ %>
						    	商户
						    <%} %>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">客户账号：</td>
                        <td>
                                <%=payAccProfileFrozen.accNo != null ? payAccProfileFrozen.accNo : "" %>
                    </tr>
                    <tr>
                        <td align="right">冻结金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayAccProfileFrozenAmtTemp" missingMessage="请输入冻结金额"
                                data-options="required:true" precision="2" max="99999999999" 
                                value="<%=payAccProfileFrozen.curAmt != null ? payAccProfileFrozen.curAmt : "" %>" />&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right">冻结结束时间：</td>
                        <td>
							<%=payAccProfileFrozen.endTime != null ? payAccProfileFrozen.endTime : "" %>
		            	</td>
                    </tr>
                    <tr>
                        <td align="right">备注：</td>
                        <td>
                        	<%=payAccProfileFrozen.remark != null ? payAccProfileFrozen.remark : "" %>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayAccProfileFrozenFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#updatePayAccProfileFrozenForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayAccProfileFrozenForm').form({
    url:'<%=path %>/addPayAccProfileFrozen.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payAccProfileFrozen',payAccProfileFrozenAddPageTitle,payAccProfileFrozenListPageTitle,'payAccProfileFrozenList','<%=path %>/payAccProfileFrozen.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
$('#addPayAccProfileFrozenAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("addPayAccProfileFrozenAmt").value=Math.ceil(parseFloat(value)*100);
		}
	}
});
function updatePayAccProfileFrozenFormSubmit(){
    $('#updatePayAccProfileFrozenForm').submit();
}
</script>
