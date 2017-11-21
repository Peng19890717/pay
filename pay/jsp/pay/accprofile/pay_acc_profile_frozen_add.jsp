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
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
%>
<!--  
<script type="text/javascript" src="<%=path %>/js/jquery-easyui-1.4-patch/jquery.easyui.patch.js"></script>
-->
<script type="text/javascript">
$(document).ready(function(){});
</script>

<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayAccProfileFrozenForm" method="post">
        	<input type="hidden" name="amt" id="addPayAccProfileFrozenAmt">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">客户类型：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" id="addPayAccProfileFrozenAccType"
								data-options="editable:false" name="accType"  style="width:130px">
						           <option value="1">商户</option>
						           <option value="0">个人</option>
						    </select>        
                        </td>
                    </tr>
                    <tr>
                        <td align="right">客户账号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayAccProfileFrozenAccNo" name="accNo" missingMessage="请输入客户账号"
                                validType="length[1,50]" invalidMessage="accNo为1-50个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">冻结金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayAccProfileFrozenAmtTemp" missingMessage="请输入冻结金额"
                                data-options="required:true" precision="2" max="99999999999"/>&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right">冻结结束时间：</td>
                        <td>
							<input type="text" id="payAccProfileFrozenEndTime" name="endTime" class="easyui-datetimespinner" value="" style="width:120px"
							data-options="showSeconds: true,
				            prompt: '点击上下箭头输入',
				            icons:[{
				                iconCls:'icon-clear',
				                handler: function(e){
				                    $(e.data.target).datetimespinner('clear');
				                }
				            }]"/>
		            	</td>
                    </tr>
                    <tr>
                        <td align="right">备注：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayAccProfileFrozenRemark" name="remark" missingMessage="请输入备注"
                        	validType="length[0,500]" invalidMessage="备注最大为 500 个字符"  style="width:200px;height:70px" data-options="multiline:true,required:true"/></td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayAccProfileFrozenFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayAccProfileFrozenForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayAccProfileFrozenForm').form({
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
function addPayAccProfileFrozenFormSubmit(){
	var endTime = $('#payAccProfileFrozenEndTime').timespinner('getValue');
	var now = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
	if(endTime != null && endTime != undefined && endTime != '' && endTime < now){
		topCenterMessage('<%=JWebConstant.ERROR %>','冻结结束时间不合法');
		return false;
	}
    $('#addPayAccProfileFrozenForm').submit();
}
</script>
