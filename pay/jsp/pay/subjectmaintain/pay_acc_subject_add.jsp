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
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <form id="addPayAccSubjectForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">科目名称：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayAccSubjectGlName" name="glName" missingMessage="请输入科目名称"
                                validType="length[1,20]" invalidMessage="glName为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">生效日期：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="PayAccSubjectEfctDate" name="efctDate"missingMessage="请输入生效日期"/></td>
                    </tr>
                    <tr>
                        <td align="right">失效日期：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="PayAccSubjectExpiredDate" name="expiredDate"missingMessage="请输入失效日期"/></td>
                    </tr>
                    <tr>
                        <td align="right">有无子目：</td>
                        <td>
                            <select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="hasSl" missingMessage="">
						        <option value="Y">有</option>
						        <option value="N">无</option>
							</select>
                       </td>
                    </tr>
                    <tr>
                        <td align="right">有无细目：</td>
                        <td>
                            <select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="hasDl" missingMessage="">
						        <option value="Y">有</option>
						        <option value="N">无</option>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">科目类别：</td>
                        <td>
                            <select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="glType" missingMessage="">
						        <option value="1">资产类</option>
						        <option value="2">负债类</option>
						        <option value="3">共同类</option>
						        <option value="4">权益类</option>
						        <option value="5">成本类</option>
						        <option value="6">损益类</option>
						        <option value="9">表外类</option>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">余额方向：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="debitCredit" missingMessage="">
						        <option value="D">借方</option>
						        <option value="C">贷方</option>
						        <option value="B">轧差</option>
							</select>
						</td>
                    </tr>
                    <tr>
                        <td align="right">余额零标志：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="zeroFlag" missingMessage="">
						        <option value="N">不限制</option>
						        <option value="D">日终等于零</option>
						        <option value="M">月终等于零</option>
						        <option value="Y">年终等于零</option>
							</select>
						</td>
                    </tr>
                    <tr>
                        <td align="right">允许对科目记账：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="manualBkFlag" missingMessage="">
						        <option value="Y">有</option>
						        <option value="N">无</option>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">总分核对标志：</td>
                        <td>
                        	<select class="easyui-combobox" panelHeight="auto" data-options="required:true,editable:false" name="totalChkFlag" missingMessage="">
						        <option value="Y">有</option>
						        <option value="N">无</option>
							</select>
                        </td>
                    </tr>
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayAccSubjectFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayAccSubjectForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayAccSubjectForm').form({
    url:'<%=path %>/addPayAccSubject.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payAccSubject',payAccSubjectAddPageTitle,payAccSubjectListPageTitle,'payAccSubjectList','<%=path %>/payAccSubject.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function addPayAccSubjectFormSubmit(){
    $('#addPayAccSubjectForm').submit();
}
</script>
