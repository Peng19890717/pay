<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
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
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayYakuStlAccForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">商户号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayYakuStlAccMerno" name="merno" missingMessage="请输入商户号"
                                validType="checkPayYakuStlAccMerno[0]"  data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">开户行姓名：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayYakuStlAccAccName" name="accName" missingMessage="请输入开户行姓名"
                                validType="length[1,50]" invalidMessage="商户银行开户姓名为1-50位字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right" width="120">商户开户银行编码：</td>
			   			<td>
			            	<select name="bankCode" style="width:200px">
			            		<%
			            		for(int i=0; i<PayCardBinService.BANK_CODE_NAME_LIST.size(); i++){
			            			PayBank bank = PayCardBinService.BANK_CODE_NAME_LIST.get(i);
			            		 	%><option value="<%=bank.bankCode %>"><%=bank.bankName %></option><%
			            		}%>
							</select>
			   			</td>
                    </tr>
                    <tr>
                        <td align="right">开户行网点名称：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayYakuStlAccBankBranchName" name="bankBranchName" missingMessage="请输入开户行网点名称"
                                validType="length[1,50]" invalidMessage="开户银行网点名称为1-50位字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">商户结算银行帐号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayYakuStlAccAccNo" name="accNo" missingMessage="请输入商户结算银行帐号"
                                validType="length[1,30]" invalidMessage="商户结算银行账号为1-30位字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算账户开户行行号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayYakuStlAccBankBranchCode" name="bankBranchCode" missingMessage="请输入结算账户开户行行号"
                                validType="length[1,20]" invalidMessage="请输入开户行行号" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算账号开户省份：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayYakuStlAccProvince" name="province" missingMessage="请输入结算账号开户省份"
                                validType="length[1,20]" invalidMessage="请输入开户省份" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算账号开户城市：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayYakuStlAccCity" name="city" missingMessage="请输入结算账号开户城市"
                                validType="length[1,20]" invalidMessage="请输入开户城市" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算账号开户身份证号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayYakuStlAccCredentialNo" name="credentialNo" missingMessage="请输入结算账号开户身份证号"
                                validType="length[15,18]" invalidMessage="请输入15或18位身份证号" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">结算账号开户手机号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayYakuStlAccTel" name="tel" missingMessage="请输入结算账号开户手机号"
                                validType="length[11,11]" invalidMessage="请输入11位手机号" data-options="required:true"/></td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayYakuStlAccFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayYakuStlAccForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayYakuStlAccForm').form({
    url:'<%=path %>/addPayYakuStlAcc.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payYakuStlAcc',payYakuStlAccAddPageTitle,payYakuStlAccListPageTitle,'payYakuStlAccList','<%=path %>/payYakuStlAcc.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
$.extend($.fn.textbox.defaults.rules, {
	checkPayYakuStlAccMerno: {
  		validator: function (value, param) {
  			if(value.length<5 || value.length>10){
   			$.fn.textbox.defaults.rules.checkPayYakuStlAccMerno.message = '商户号为5-10位字符';
   			return false;
   		} else {
   			var result = $.ajax({
				url: '<%=path %>/validPayYakuStlAccMerno.htm',
				data:{custId:value},
				type: 'post',
				dataType: 'text',
				async: false,
				cache: false
			}).responseText;
			if(result != '0')return true;
			$.fn.textbox.defaults.rules.checkPayYakuStlAccMerno.message = '商户号不存在';
			return false; 
   		}
  		},
	message: ''
  }
});
function addPayYakuStlAccFormSubmit(){
    $('#addPayYakuStlAccForm').submit();
}
</script>
