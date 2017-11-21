<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.account.dao.PayAccEntry"%>
<%@ page import="com.pay.account.service.PayAccEntryService"%>
<%@ page import="com.pay.account.dao.SimplePayAccSubject"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayAccEntry payAccEntry = (PayAccEntry)request.getAttribute("payAccEntryUpdate");
java.util.List subjectlist = new PayAccEntryService().getPayAccSUBJECT();
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="updatePayAccEntryForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
              	<input type="hidden" name="id" value="<%= payAccEntry.id%>"/>
                    <tr>
                        <td align="right">交易码：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayAccEntryTxnCod" name="txnCod" missingMessage="请输入交易码"
                                validType="length[1,6]" invalidMessage="交易码为6个字符" data-options="required:true" maxlength="6" value="<%=payAccEntry.txnCod != null ? payAccEntry.txnCod : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">子交易码：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayAccEntryTxnSubCod" name="txnSubCod" missingMessage="请输入子交易码"
                                validType="length[1,3]" invalidMessage="子交易码为1-3个字符" data-options="required:true" maxlength="3" value="<%=payAccEntry.txnSubCod != null ? payAccEntry.txnSubCod : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">记账序号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayAccEntryAccSeq" name="accSeq" missingMessage="请输入记账序号"
                                validType="length[1,3]" invalidMessage="记账序号为1-3个字符" data-options="required:true" maxlength="3"  value="<%=payAccEntry.accSeq != null ? payAccEntry.accSeq : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">借贷方向：</td>
                        <td><select name="drCrFlag" id="addPayAccEntryAccDrCrFlag" style="width:200px">
						   		<option value="D" <%= payAccEntry.drCrFlag .equalsIgnoreCase("D")?"selected=\"selected\"":""%>>借方</option>
						   		<option value="C" <%= payAccEntry.drCrFlag.equalsIgnoreCase("C")?"selected=\"selected\"":""%>>贷方 </option>	
					 		</select>
					 	</td>
                    </tr>
                    <tr>
                        <td align="right">科目来源：</td>
                        <td><select name="subjectFrom" id="addPayAccEntryAccSubjectFrom" style="width:200px" >
						 	<option value="0" <%= payAccEntry.subjectFrom.equalsIgnoreCase("0")?"selected=\"selected\"":""%>>根据分录操作账户消费金额</option>
						 	<option value="1" <%= payAccEntry.subjectFrom.equalsIgnoreCase("1")?"selected=\"selected\"":""%>>根据分录操作账户现金金额</option>
						 	<option value="2" <%= payAccEntry.subjectFrom.equalsIgnoreCase("2")?"selected=\"selected\"":""%>>根据分录操作账户现金+冻结</option>
						 	<option value="3" <%= payAccEntry.subjectFrom.equalsIgnoreCase("3")?"selected=\"selected\"":""%>>根据分录操作科目</option>
						 	<option value="4" <%= payAccEntry.subjectFrom.equalsIgnoreCase("4")?"selected=\"selected\"":""%>>根据输入操作科目</option>
						 	<option value="5" <%= payAccEntry.subjectFrom.equalsIgnoreCase("5")?"selected=\"selected\"":""%>>根据分录批量操作账户现金</option>
						 	<option value="6" <%= payAccEntry.subjectFrom.equalsIgnoreCase("6")?"selected=\"selected\"":""%>>根据分录批量操作账户消费</option>
						 	<option value="7" <%= payAccEntry.subjectFrom.equalsIgnoreCase("7")?"selected=\"selected\"":""%>>根据输入批量操作科目</option>
					 		</select>
					 	</td>
                    </tr>
                    <tr>
                        <td align="right">科目：</td>
                         <td>
                         <!-- 
                         <select name="subject" id="addPayAccEntryAccSubject" style="width:200px">
						   		
						   		<%for(int i = 0; i<subjectlist.size(); i++){
						   			SimplePayAccSubject subject = (SimplePayAccSubject)subjectlist.get(i); 
									%><option value="<%=subject.glCode %>" <%= payAccEntry.subject.equalsIgnoreCase(subject.glCode)?"selected=\"selected\"":""%>><%=subject.glName %></option><%
							} %>
					 		</select> -->
					 		<input class="easyui-combobox" id="subject" name="subject" style="width:250px"
									data-options="valueField:'id',textField:'text',data:subjectListRuleData,required:true" validType="inputExistInCombobox['subject']" />
					 	</td>
                    </tr>
                    <tr>
                        <td align="right">账务机构：</td>
                        <td><select name="accOrgNo" id="addPayAccEntryAccOrgNo" style="width:200px">
						   		<option value="A" <%= payAccEntry.accOrgNo.equalsIgnoreCase("A")?"selected=\"selected\"":""%>>开户机构</option>
						   		<option value="T" <%= payAccEntry.accOrgNo.equalsIgnoreCase("T")?"selected=\"selected\"":""%>>交易机构 </option>	
					 		</select>
					 	</td>
                    </tr>
                    <tr>
                        <td align="right">交易类型：</td>
                        <td><select name="rmkCod" id="addPayAccEntryRmkCod" style="width:200px" >
						 	<option value="1" <%= payAccEntry.rmkCod.equalsIgnoreCase("1") ?"selected=\"selected\"":""%>>消费</option>
						 	<option value="2" <%= payAccEntry.rmkCod.equalsIgnoreCase("2") ?"selected=\"selected\"":""%>>充值</option>
						 	<option value="3" <%= payAccEntry.rmkCod.equalsIgnoreCase("3")?"selected=\"selected\"":""%>>结算</option>
						 	<option value="4" <%= payAccEntry.rmkCod.equalsIgnoreCase("4") ?"selected=\"selected\"":""%>>退款</option>
						 	<option value="5" <%= payAccEntry.rmkCod.equalsIgnoreCase("5") ?"selected=\"selected\"":""%>>提现</option>
						 	<option value="6" <%= payAccEntry.rmkCod.equalsIgnoreCase("6") ?"selected=\"selected\"":""%>>转账</option>
						 	<option value="7" <%= payAccEntry.rmkCod.equalsIgnoreCase("7") ?"selected=\"selected\"":""%>>对账</option>
						 	<option value="8" <%= payAccEntry.rmkCod.equalsIgnoreCase("8") ?"selected=\"selected\"":""%>>综合记账</option>
						 	<option value="9" <%= payAccEntry.rmkCod.equalsIgnoreCase("9") ?"selected=\"selected\"":""%>>错账挂账</option>
						 	<option value="a" <%= payAccEntry.rmkCod.equalsIgnoreCase("a") ?"selected=\"selected\"":""%>>商户保证金</option>
					 		</select>
					 	</td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayAccEntryFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payAccEntryUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
<%
//获取规则列表
String rules="";
for(int i = 0; i<subjectlist.size(); i++){
	SimplePayAccSubject rule = (SimplePayAccSubject)subjectlist.get(i); 
	if(rule.glCode.equalsIgnoreCase(payAccEntry.subject)){
		
		 rules = rules+"{\"id\":\""+rule.glCode+"\",\"text\":\""+rule.glName+"\",\"selected\":true},";
		
	}else{
		
		 rules = rules+"{\"id\":\""+rule.glCode+"\",\"text\":\""+rule.glName+"\"},";
	}
		
	
}
if(rules.endsWith(","))rules = rules.substring(0,rules.length()-1); 
%>
var subjectListRuleData=[<%=rules %>];
$('#updatePayAccEntryForm').form({
    url:'<%=path %>/updatePayAccEntry.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payAccEntryList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payAccEntry',payAccEntryUpdatePageTitle,payAccEntryListPageTitle,'payAccEntryList','<%=path %>/payAccEntry.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayAccEntryFormSubmit(){
    $('#updatePayAccEntryForm').submit();
}
</script>
