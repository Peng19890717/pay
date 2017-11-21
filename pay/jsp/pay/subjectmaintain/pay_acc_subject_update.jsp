<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.account.dao.PayAccSubject"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayAccSubject payAccSubject = (PayAccSubject)request.getAttribute("payAccSubjectUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payAccSubject != null){ %>
        <form id="updatePayAccSubjectForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
            	<input type="hidden" name="glCode" value="<%=payAccSubject.glCode != null ? payAccSubject.glCode : "" %>"/>
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">科目名称：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayAccSubjectGlName" name="glName" missingMessage="请输入科目名称"
                                validType="length[1,20]" invalidMessage="glName为1-20个字符" data-options="required:true"
                                value="<%=payAccSubject.glName != null ? payAccSubject.glName : "" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">生效日期：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayAccSubjectEfctDate" name="efctDate"missingMessage="请输入生效日期"
                                value="<%=payAccSubject.efctDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.efctDate):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">失效日期：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="updatePayAccSubjectExpiredDate" name="expiredDate"missingMessage="请输入失效日期"
                                value="<%=payAccSubject.expiredDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payAccSubject.expiredDate):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">有无子目：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="hasSl" missingMessage="">
						        <option value="Y" <c:if test="${payAccSubjectUpdate.hasSl eq 'Y'}">selected</c:if>>有</option>
						        <option value="N" <c:if test="${payAccSubjectUpdate.hasSl eq 'N'}">selected</c:if>>无</option>
							</select>
						</td>
                    </tr>
                    <tr>
                        <td align="right">有无细目：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="hasDl" missingMessage="">
						        <option value="Y" <c:if test="${payAccSubjectUpdate.hasDl eq 'Y'}">selected</c:if>>有</option>
						        <option value="N" <c:if test="${payAccSubjectUpdate.hasDl eq 'N'}">selected</c:if>>无</option>
							</select>
						</td>
                    </tr>
                    <tr>
                        <td align="right">科目类别：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="glType" missingMessage="">
						        <option value="1" <c:if test="${payAccSubjectUpdate.glType eq 1}">selected</c:if>>资产类</option>
						        <option value="2" <c:if test="${payAccSubjectUpdate.glType eq 2}">selected</c:if>>负债类</option>
						        <option value="3" <c:if test="${payAccSubjectUpdate.glType eq 3}">selected</c:if>>共同类</option>
						        <option value="4" <c:if test="${payAccSubjectUpdate.glType eq 4}">selected</c:if>>权益类</option>
						        <option value="5" <c:if test="${payAccSubjectUpdate.glType eq 5}">selected</c:if>>成本类</option>
						        <option value="6" <c:if test="${payAccSubjectUpdate.glType eq 6}">selected</c:if>>损益类</option>
						        <option value="9" <c:if test="${payAccSubjectUpdate.glType eq 9}">selected</c:if>>表外类</option>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">余额方向：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="debitCredit" missingMessage="">
						        <option value="D" <c:if test="${payAccSubjectUpdate.debitCredit eq 'D'}">selected</c:if>>借方</option>
						        <option value="C" <c:if test="${payAccSubjectUpdate.debitCredit eq 'C'}">selected</c:if>>贷方</option>
						        <option value="B" <c:if test="${payAccSubjectUpdate.debitCredit eq 'B'}">selected</c:if>>轧差</option>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">余额零标志：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="zeroFlag" missingMessage="">
						        <option value="N" <c:if test="${payAccSubjectUpdate.zeroFlag eq 'N'}">selected</c:if>>不限制</option>
						        <option value="D" <c:if test="${payAccSubjectUpdate.zeroFlag eq 'D'}">selected</c:if>>日终等于零</option>
						        <option value="M" <c:if test="${payAccSubjectUpdate.zeroFlag eq 'M'}">selected</c:if>>月终等于零</option>
						        <option value="Y" <c:if test="${payAccSubjectUpdate.zeroFlag eq 'Y'}">selected</c:if>>年终等于零</option>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">允许对科目记账：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="manualBkFlag" missingMessage="">
						        <option value="Y" <c:if test="${payAccSubjectUpdate.manualBkFlag eq 'Y'}">selected</c:if>>有</option>
						        <option value="N" <c:if test="${payAccSubjectUpdate.manualBkFlag eq 'N'}">selected</c:if>>无</option>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">总分核对标志：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="totalChkFlag" missingMessage="">
						        <option value="Y" <c:if test="${payAccSubjectUpdate.totalChkFlag eq 'Y'}">selected</c:if>>有</option>
						        <option value="N" <c:if test="${payAccSubjectUpdate.totalChkFlag eq 'N'}">selected</c:if>>无</option>
							</select>
                        </td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayAccSubjectFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payAccSubjectUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">

$(function(){
	
})

$('#updatePayAccSubjectForm').form({
    url:'<%=path %>/updatePayAccSubject.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payAccSubjectList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payAccSubject',payAccSubjectUpdatePageTitle,payAccSubjectListPageTitle,'payAccSubjectList','<%=path %>/payAccSubject.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayAccSubjectFormSubmit(){
    $('#updatePayAccSubjectForm').submit();
}
</script>
