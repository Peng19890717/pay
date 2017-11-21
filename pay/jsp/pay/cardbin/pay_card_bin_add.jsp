<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
<%@page import="com.pay.bank.dao.PayBank"%>
<%@page import="java.util.List"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayCardBinForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">卡类型：</td>
                        <td>
                             <select name="cardType" id="addPayCardBinCardType" class="easyui-combobox"  data-options="required:true">
		                         <option value="0">借记卡</option>  
		                         <option value="1">贷记卡</option>  
		                         <option value="2">准贷记卡</option>  
		                         <option value="3">预付卡</option>  
		                    </select> 
                        </td>
                    </tr>
                    <tr>
                        <td align="right">卡前缀：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayCardBinBinNo" name="binNo" missingMessage="请输入卡前缀"
                                validType="length[1,25]" invalidMessage="卡前缀为1-25个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">卡长度：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayCardBinCardLength" name="cardLength" missingMessage="请输入卡长度"
                                data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">银行名称：</td>
                        <td>
                            <select name="bankCode" id="addPayCardBinBankCode" class="easyui-combobox"  data-options="required:true">
	                        	<%
								for(int i = 0 ; i < PayCoopBankService.BANK_CODE_NAME_LIST.size(); i++) {
									PayBank payBank = PayCoopBankService.BANK_CODE_NAME_LIST.get(i);%>
									<option value="<%=payBank.bankCode%>"><%=payBank.bankName%></option>  
								<%}%>   
		                    </select> 
                        </td>
                   </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayCardBinFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayCardBinForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayCardBinForm').form({
    url:'<%=path %>/addPayCardBin.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payCardBin',payCardBinAddPageTitle,payCardBinListPageTitle,'payCardBinList','<%=path %>/payCardBin.htm');
        } else topCenterMessage('<%= JWebConstant.ERROR %>',data);
    }
});
function addPayCardBinFormSubmit(){
    $('#addPayCardBinForm').submit();
}
</script>
