<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.merchant.dao.PayYakuStlAcc"%>
<%@ page import="com.pay.custstl.dao.PayCustStlInfo"%>
<%@ page import="com.pay.cardbin.service.PayCardBinService"%> 
<%@ page import="com.pay.bank.dao.PayBank"%> 
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayYakuStlAcc payYakuStlAcc = (PayYakuStlAcc)request.getAttribute("payYakuStlAccUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payYakuStlAcc != null){ %>
        <form id="updatePayYakuStlAccForm" method="post">
        	<input type="hidden" name="accNo" value="<%= payYakuStlAcc.accNo%>">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">商户号：</td>
                        <td>${payYakuStlAccUpdate.merno }</td>
                    </tr>
                    <tr>
                        <td align="right">开户行姓名：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayYakuStlAccAccName" name="accName" missingMessage="请输入开户行姓名"
                                validType="length[1,20]" invalidMessage="商户号为1-20个字符" data-options="required:true"
                                value="<%=payYakuStlAcc.accName != null ? payYakuStlAcc.accName : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right" width="120">商户开户银行编码：</td>
   						<td>
				            <select name="bankCode" style="width:200px">
								<%
			            		for(int i=0; i<PayCardBinService.BANK_CODE_NAME_LIST.size(); i++){
			            			PayBank bank = PayCardBinService.BANK_CODE_NAME_LIST.get(i);
			            		 	%><option value="<%=bank.bankCode %>" <%= bank.bankCode.equals(payYakuStlAcc.bankCode)?"selected":"" %>><%=bank.bankName %></option><%
			            		}%>
							</select>
			   			</td>
                    </tr>
                    <tr>
                        <td align="right">开户行网点名称：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayYakuStlAccBankBranchName" name="bankBranchName" missingMessage="请输入开户行网点名称"
                                validType="length[1,50]" invalidMessage="开户银行网点名称为1-50位字符" data-options="required:true"
                                value="<%=payYakuStlAcc.bankBranchName != null ? payYakuStlAcc.bankBranchName : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">商户结算银行帐号：</td>
                        <td>${payYakuStlAccUpdate.accNo }</td>
                    </tr>
                    <tr>
                        <td align="right">结算账户开户行行号：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayYakuStlAccBankBranchCode" name="bankBranchCode" missingMessage="请输入结算账户开户行行号"
                                validType="length[1,20]" invalidMessage="请输入开户行行号" data-options="required:true"
                                value="<%=payYakuStlAcc.bankBranchCode != null ? payYakuStlAcc.bankBranchCode : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算账号开户省份：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayYakuStlAccProvince" name="province" missingMessage="请输入结算账号开户省份"
                                validType="length[1,20]" invalidMessage="请输入开户省份" data-options="required:true"
                                value="<%=payYakuStlAcc.province != null ? payYakuStlAcc.province : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算账号开户城市：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayYakuStlAccCity" name="city" missingMessage="请输入结算账号开户城市"
                                validType="length[1,20]" invalidMessage="请输入开户城市" data-options="required:true"
                                value="<%=payYakuStlAcc.city != null ? payYakuStlAcc.city : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算账号开户身份证号：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayYakuStlAccCredentialNo" name="credentialNo" missingMessage="请输入结算账号开户身份证号"
                                validType="length[15,18]" invalidMessage="请输入15或18位身份证号" data-options="required:true"
                                value="<%=payYakuStlAcc.credentialNo != null ? payYakuStlAcc.credentialNo : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算账号开户手机号：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayYakuStlAccTel" name="tel" missingMessage="请输入结算账号开户手机号"
                                validType="length[11,11]" invalidMessage="请输入11位手机号" data-options="required:true"
                                value="<%=payYakuStlAcc.tel != null ? payYakuStlAcc.tel : "" %>"/></td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayYakuStlAccFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payYakuStlAccUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayYakuStlAccForm').form({
    url:'<%=path %>/updatePayYakuStlAcc.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payYakuStlAccList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payYakuStlAcc',payYakuStlAccUpdatePageTitle,payYakuStlAccListPageTitle,'payYakuStlAccList','<%=path %>/payYakuStlAcc.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayYakuStlAccFormSubmit(){
    $('#updatePayYakuStlAccForm').submit();
}
</script>
