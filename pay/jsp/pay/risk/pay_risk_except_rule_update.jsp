<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.risk.dao.PayRiskExceptRule"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayRiskExceptRule payRiskExceptRule = (PayRiskExceptRule)request.getAttribute("payRiskExceptRuleUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
$(function(){
	$("#addOneRowForUpdate").click(function() { 
		var pfrsTable=document.getElementById("payRiskExceptRuleValueTableForUpdate");
		var rowIndex = pfrsTable.rows.length;
		if(rowIndex>5){
			topCenterMessage("<%=JWebConstant.ERROR %>","超出最大行数（6行）");
			return;
		}
		var index = pfrsTable.rows.length +1;
		var tr=document.createElement("tr");
	    var td=document.createElement("td");
	    td.innerHTML = "<input class='easyui-textbox' type='text' name='paramName'  missingMessage='请输入参数名称' validType='length[1,20]' invalidMessage='参数名称为1-20个字符' data-options='required:true'/>" +
			"	<input class='easyui-numberbox' type='text' name='paramValue' missingMessage='请输入参数值（数字）' data-options='required:true' invalidMessage='参数值必填' precision='1' max='99999999999'/>" +
			"	<select class='easyui-combobox' name='paramType' missingMessage='请选择单位' data-options='editable:false'>" + 
			"		<option value='元'>元</option>" +
			"		<option value='倍'>倍</option>" +
			"		<option value='笔'>笔</option>" +
			"		<option value='次'>次</option>" +
			"		<option value='分钟'>分钟</option>" +
			"		<option value='日'>日</option>" +
			"   </select>"
	    tr.appendChild(td);
	    pfrsTable.appendChild (tr);
	    $.parser.parse("#payRiskExceptRuleValueTableForUpdate");
	}); 
	
	$("#delOneRowForUpdate").click(function() { 
		var pfrsTable=document.getElementById("payRiskExceptRuleValueTableForUpdate");
		if(pfrsTable.rows.length == 1)return;
		pfrsTable.deleteRow(pfrsTable.rows.length-1); 
	});
});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payRiskExceptRule != null){ %>
        <form id="updatePayRiskExceptRuleForm" method="post">
        	<table cellpadding="5" width="100%" style="margin-top:-10px;" id="updatePayRiskExceptRuleFormTable">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">规则名称：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayRiskExceptRuleRuleName" name="ruleName" missingMessage="请输入规则名称"
                                validType="length[1,20]" invalidMessage="ruleName为1-20个字符" data-options="required:true" value="<%=payRiskExceptRule.ruleName != null ? payRiskExceptRule.ruleName : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">规则类型：</td>
                        <td>
                            <select class="easyui-combobox" name="ruleType" missingMessage="请选择规则类型" data-options="editable:false">
	                       		<option value="<%=com.PayConstant.CUST_TYPE_USER %>" <c:if test="${ payRiskExceptRuleUpdate.ruleType eq '0'}">selected="selected"</c:if>>个人</option>
	                       		<option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>" <c:if test="${ payRiskExceptRuleUpdate.ruleType eq '1'}">selected="selected"</c:if>>商户</option>
                        	</select>
                         </td>
                    </tr>
                    <tr>
                        <td align="right">是否线上：</td>
                        <td>
                           <select class="easyui-combobox" name="isOnline" missingMessage="请选择是否线上" data-options="editable:false">
                           		<option value="0" <c:if test="${ payRiskExceptRuleUpdate.isOnline eq '0'}">selected="selected"</c:if>>线上</option>
                           		<option value="1" <c:if test="${ payRiskExceptRuleUpdate.isOnline eq '1'}">selected="selected"</c:if>>线下</option>
                           </select>
                        </td>
                    </tr>
                    <tr>
                    	<td align="right">生效时间范围：</td>
                    	<td>
                    		<input class="easyui-datebox" style="width:100px" data-options="editable:false"  name="ruleStartDate" missingMessage="请输入开始时间" value="<%=payRiskExceptRule.ruleStartDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.ruleStartDate):"" %>"/>
                    		~
                    		<input class="easyui-datebox" style="width:100px" data-options="editable:false"  name="ruleEndDate" missingMessage="请输入结束时间" value="<%=payRiskExceptRule.ruleEndDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payRiskExceptRule.ruleEndDate):"" %>"/>
                    	</td>
                    </tr>
                    <tr>
                        <td align="right">异常类型：</td>
                        <td>
                           <select class="easyui-combobox" name="excpType" missingMessage="请选择异常类型" data-options="editable:false">
                           		<option value="1" <c:if test="${ payRiskExceptRuleUpdate.excpType eq '1'}">selected="selected"</c:if>>异常</option>
                           		<option value="2" <c:if test="${ payRiskExceptRuleUpdate.excpType eq '2'}">selected="selected"</c:if>>可疑</option>
                           </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">预警项：</td>
                        <td>
                            <select class="easyui-combobox" name="ruleLevelItem" missingMessage="请选择预警项" data-options="editable:false">
                           		<option value="0" <c:if test="${ payRiskExceptRuleUpdate.ruleLevelItem eq '0'}">selected="selected"</c:if>>交易突增</option>
                           		<option value="1" <c:if test="${ payRiskExceptRuleUpdate.ruleLevelItem eq '1'}">selected="selected"</c:if>>疑似套现</option>
                           		<option value="2" <c:if test="${ payRiskExceptRuleUpdate.ruleLevelItem eq '2'}">selected="selected"</c:if>>分单</option>
                           		<option value="3" <c:if test="${ payRiskExceptRuleUpdate.ruleLevelItem eq '3'}">selected="selected"</c:if>>非常规时段交易</option>
                           		<option value="4" <c:if test="${ payRiskExceptRuleUpdate.ruleLevelItem eq '4'}">selected="selected"</c:if>>零交易</option>
                           		<option value="5" <c:if test="${ payRiskExceptRuleUpdate.ruleLevelItem eq '5'}">selected="selected"</c:if>>其它</option>
                           		<option value="6" <c:if test="${ payRiskExceptRuleUpdate.ruleLevelItem eq '6'}">selected="selected"</c:if>>无</option>
                           </select>
                        </td>
                    </tr>
                    <!-- 
                    <tr>
                        <td align="right">业务类型：</td>
                        <td>
                           <select class="easyui-combobox" name="comTypeNo" missingMessage="请选择业务类型" data-options="editable:false">
                        		<%	
                        			java.util.Iterator<String> it = com.PayConstant.MER_BIZ_TYPE.keySet().iterator();
                        			while(it.hasNext()){
                        				String key = it.next();
                        				String value = com.PayConstant.MER_BIZ_TYPE.get(key);
                        				%><option value="<%=key%>" 
                        				<%
                        					if(payRiskExceptRule != null && key.equals(payRiskExceptRule.comTypeNo)){
                        						%>selected="selected"
                        					<%}%>
                        				><%=value%></option>
                        			<%}
                        		%>
                        	</select>
                        </td>
                    </tr> -->
                    <tr>
                        <td align="right">规则描述：</td>
                        <td>
                             <textarea class="textbox-text" autocomplete="off" rows="10" cols="80" name="ruleDes"><%=payRiskExceptRule.ruleDes != null ? payRiskExceptRule.ruleDes : "" %></textarea>   
                        </td>
                    </tr>
                    <tr>
                        <td align="right">规则等级：</td>
                        <td>
                            <select class="easyui-combobox" name="ruleLevel" missingMessage="请选择规则等级" data-options="editable:false">
                           		<option value="0" <c:if test="${ payRiskExceptRuleUpdate.ruleLevel eq '0'}">selected="selected"</c:if>>一般</option>
                           		<option value="1" <c:if test="${ payRiskExceptRuleUpdate.ruleLevel eq '1'}">selected="selected"</c:if>>紧急</option>
                           		<option value="2" <c:if test="${ payRiskExceptRuleUpdate.ruleLevel eq '2'}">selected="selected"</c:if>>无等级</option>
                           </select>    
                        </td>
                    </tr>
                    <tr>
                    	<td align="right" valign="top">阈值参数：</td>
                    	<td>
                    		<table id="payRiskExceptRuleValueTableForUpdate">
                    			<c:forEach items="${ updatePayRiskExceptRuleParamSet }" var="updateSet">
							        <tr>
							            <td>
							            	<c:choose>
							            		<c:when test="${ payRiskExceptRuleUpdate.isUse eq '1' }">
							            			<input class="easyui-textbox" type="text" value="${ updateSet.paramName }" data-options="disabled:true"/>
					                    			<input class="easyui-numberbox" type="text" name="paramValue"  value="${ updateSet.paramValue }" missingMessage="请输入参数值" data-options="required:true" precision="1" max="99999999999"/>
					                    			<select class="easyui-combobox" data-options="disabled:true" >
					                           			<option data-options="editable:false" value="元" <c:if test="${ updateSet.paramType eq '元' }">selected="selected"</c:if>>元</option>
					                           			<option data-options="editable:false" value="分" <c:if test="${ updateSet.paramType eq '分' }">selected="selected"</c:if>>分</option>
					                           		</select>
					                           		<input type="hidden" name="paramName" value="${ updateSet.paramName }">
					                           		<input type="hidden" name="paramType" value="<c:if test="${ updateSet.paramType eq '元' }">元</c:if><c:if test="${ updateSet.paramType eq '分' }">分</c:if>">
							            		</c:when>
							            		<c:otherwise>
							            			<input class="easyui-textbox" type="text" name="paramName"   value="${ updateSet.paramName }" missingMessage="请输入参数名称" validType="length[1,20]" invalidMessage="参数名称为1-20个字符" data-options="required:true"/>
					                    			<input class="easyui-numberbox" type="text" name="paramValue"  value="${ updateSet.paramValue }" missingMessage="请输入参数值" data-options="required:true" precision="1" max="99999999999"/>
					                    			<select class="easyui-combobox" name="paramType" missingMessage="请选择单位">
					                           			<option value="元" <c:if test="${ updateSet.paramType eq '元' }">selected="selected"</c:if>>元</option>
					                           			<option value="倍" <c:if test="${ updateSet.paramType eq '倍' }">selected="selected"</c:if>>倍</option>
					                           			<option value="笔" <c:if test="${ updateSet.paramType eq '笔' }">selected="selected"</c:if>>笔</option>
					                           			<option value="次" <c:if test="${ updateSet.paramType eq '次' }">selected="selected"</c:if>>次</option>
					                           			<option value="分钟" <c:if test="${ updateSet.paramType eq '分钟' }">selected="selected"</c:if>>分钟</option> 
					                           			<option value="日" <c:if test="${ updateSet.paramType eq '日' }">selected="selected"</c:if>>日</option>
					                           		</select>
							            		</c:otherwise>
							            	</c:choose>
			                           	</td>
							        </tr>
						        </c:forEach>
					        </table>
                    	</td>
                    </tr>
                    <tr>
                    	<td>&nbsp;</td>
                    	<td>
                    		<c:if test="${ payRiskExceptRuleUpdate.isUse eq '0' }">
                    			<a class="easyui-linkbutton" data-options="iconCls:'icon-add'" id="addOneRowForUpdate" style="width:80px">增加一行</a>&nbsp;&nbsp;
   	    						<a class="easyui-linkbutton" data-options="iconCls:'icon-remove'" id="delOneRowForUpdate" style="width:80px">删除一行</a>
                    		</c:if>
                    	</td>
                    </tr>
            </table>
            <input type="hidden" name="ruleCode" value="${ payRiskExceptRuleUpdate.ruleCode }">
            <input type="hidden" name="isUse" value="${ payRiskExceptRuleUpdate.isUse }">
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayRiskExceptRuleFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payRiskExceptRuleUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#updatePayRiskExceptRuleForm').form({
    url:'<%=path %>/updatePayRiskExceptRule.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payRiskExceptRuleList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payRiskExceptRule',payRiskExceptRuleUpdatePageTitle,payRiskExceptRuleListPageTitle,'payRiskExceptRuleList','<%=path %>/payRiskExceptRule.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayRiskExceptRuleFormSubmit(){
    if(checkNameForUpdate()) $('#updatePayRiskExceptRuleForm').submit();
}
function checkNameForUpdate(){
	var tempY = $("input[name=paramName]");
	var tempZ = $("input[name=paramName]");
	var flag = true;
	for(var y = 0 ; y < tempY.length ; y ++) {
		if(!flag) break;
		for(var z = y + 1 ; z < tempZ.length ; z ++) {
			if($(tempY[y]).val() != "" && $(tempZ[z]).val() != "" && $(tempY[y]).val() == $(tempZ[z]).val()){
				flag = false;
				break;
			};
		};
	}
	if(!flag){
		topCenterMessage('<%=JWebConstant.ERROR %>','阈值名称不可重复！');
		return flag;
	}
	return flag;
};
</script>
