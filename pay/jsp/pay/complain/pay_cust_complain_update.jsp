<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.complain.dao.PayCustComplain"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%@ page import="com.pay.coopbank.service.PayCoopBankService"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayCustComplain payCustComplain = (PayCustComplain)request.getAttribute("payCustComplainUpdate");
%>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/upload/uploadify.css"  />
<script type="text/javascript" src="<%=path %>/js/upload/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        <%if(payCustComplain != null){ %>
        <form id="updatePayCustComplainForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">投诉类型：</td>
                        <td>
		                	<%= "0".equals(payCustComplain.type)?"用户":"商户" %> 
						</td>
                    </tr>
                    <tr>
                        <td align="right">所属商户号：</td>
                        <td><%=payCustComplain.custId != null ? payCustComplain.custId : "" %></td>
                    </tr>
                    <tr>
                        <td align="right">投诉交易金额：</td>
                        <td><%=payCustComplain.transAmt != null ? String.format("%.2f",(float)payCustComplain.transAmt*0.01) : "" %>元</td>
                    </tr>
                    <tr>
                        <td align="right">冻结金额：</td>
                        <td>
                        <%if("0".equals(payCustComplain.optStatus)&&payCustComplain.accFrozenId==null){ %>
                        	<input class="easyui-numberbox" type="text" id="updatePayCustComplainFrozenAmtTemp" missingMessage="请输入冻结金额"
                                data-options="required:true" precision="2" max="99999999999" invalidMessage="" 
                                value="<%=payCustComplain.frozenAmt != null ? String.format("%.2f",(float)payCustComplain.frozenAmt*0.01) : "" %>"/>
                        <%} else {
                        	%><%=payCustComplain.frozenAmt != null ? String.format("%.2f",(float)payCustComplain.frozenAmt*0.01) : "" %><%
                        } %>
                        &nbsp;&nbsp;元（资金处理结果为【初始】状态时，修改有效）
                        </td>
                    </tr>
                    <%if("0".equals(payCustComplain.optStatus)&&payCustComplain.accFrozenId!=null){ %>
                    <tr>
                        <td align="right">解冻方式：</td>
                        <td><select name="unfrozenFlag" id="addPayCustComplainUnfrozenFlag" class="easyui-combobox"  data-options="required:true">
	                        	<option value="0">手动解冻</option>
		                        <option value="1">自动解冻</option>
	                    	</select>（资金处理结果为【结束】状态有效）</td>
                    </tr>
                    <%} %>
                    <tr>
                        <td align="right">资金处理结果：</td>
                        <td>
                        <%if("0".equals(payCustComplain.optStatus)){ %>
	                        <select name="optStatus" id="updatePayCustComplainOptStatus" class="easyui-combobox"  data-options="required:true">
	                        	<option value="0" <%= "0".equals(payCustComplain.optStatus)?"selected":"" %>>初始</option>
		                        <option value="1" <%= "1".equals(payCustComplain.optStatus)?"selected":"" %>>结束</option>
		                        <option value="2" <%= "2".equals(payCustComplain.optStatus)?"selected":"" %>>作废</option>
	                    	</select> （处理【结束】状态并且【自动解冻】方式时，冻结资金扣减以外的部分返还到商户余额中）
	                    <%} else if("1".equals(payCustComplain.optStatus)){ %> 结束
	                    <%} else {%> 作废 <%} %>
						</td>
                    </tr>
                    <tr>
                        <td align="right">客户姓名：</td>
                        <td>
                        <%if("0".equals(payCustComplain.optStatus)){ %>
                        <input class="easyui-textbox" type="text" id="updatePayCustComplainName" name="name" data-options="required:true" value="<%=payCustComplain.name != null ? payCustComplain.name : "" %>"/>
                        <%} else {%><%=payCustComplain.name != null ? payCustComplain.name : "" %><%} %>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">客户电话：</td>
                        <td>
                        <%if("0".equals(payCustComplain.optStatus)){ %>
                        <input class="easyui-textbox" type="text" id="updatePayCustComplainTel" name="tel" data-options="required:true" value="<%=payCustComplain.tel != null ? payCustComplain.tel : "" %>"/>
                        <%} else {%><%=payCustComplain.tel != null ? payCustComplain.tel : "" %><%} %>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">渠道：</td>
                        <%PayCoopBank c = PayCoopBankService.CHANNEL_MAP_ALL.get(payCustComplain.channel);%>
                        <td><%=c == null ? payCustComplain.channel : c.bankName %></td>
                    </tr>
                    <tr>
                        <td align="right">投诉凭证：</td>
                        <%String sf = payCustComplain.startFile; %>
                        <td><a target="_blank" href="<%=sf != null ? request.getContextPath()+sf : "" %>">
                        <%=sf.substring(sf.indexOf("_")+1) %>
                        </a></td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">完成凭证：</td>
                        <%String ef = payCustComplain.endFile; %>
                        <td>
                        	<a target="_blank" href="<%=ef != null ? request.getContextPath()+ef : "" %>"><%=ef != null ? ef.substring(ef.indexOf("_")+1):"" %></a>
                        	<%if("0".equals(payCustComplain.optStatus)){ %>
                        	<%=ef != null ? "<br/>":"" %><input type="file" name="updateUploadFile" id="updatePayCustComplainEndFileTemp">
                        	<%} %>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">订单号：</td>
                        <td><%=payCustComplain.orderIds != null ? payCustComplain.orderIds : "" %></td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">投诉内容：</td>
                        <td><%=payCustComplain.cContent != null ? payCustComplain.cContent : "" %></td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">备注：</td>
                        <td>
                        <%if("0".equals(payCustComplain.optStatus)){ %>
                        <input class="easyui-textbox" type="text" id="updatePayCustComplainRemark" name="remark" data-options="multiline:true,required:true"  style="width:240px;height:70px"
                                value="<%=payCustComplain.remark != null ? payCustComplain.remark : "" %>"/>
                        <%} else {%>
                        	<%=payCustComplain.remark != null ? payCustComplain.remark : "" %>
                        <% } %> 
                        </td>
                    </tr>
                    <%if("0".equals(payCustComplain.optStatus)){ %>
                    <tr>
                        <td align="right">调账扣减方式：</td>
                        <td><select name="deductFlag" id="addPayCustComplainDeductFlag" class="easyui-combobox"  data-options="required:true">
	                        	<option value="0">自动扣减</option>
		                        <option value="1">手动扣减</option>
	                    	</select>（处理结果为【结束】状态有效）</td>
                    </tr>
                    <%} %>
                    
                    <tr>
                        <td align="right">调账扣减金额：</td>
                        <td>
                        <%if("0".equals(payCustComplain.optStatus)){ %>
                        	<input class="easyui-numberbox" type="text" id="updatePayCustComplainDeductAmtTemp" missingMessage="请输入扣减金额"
                                data-options="required:true" precision="2" max="99999999999" invalidMessage=""
                                value="<%=payCustComplain.deductAmt != null ? String.format("%.2f",(float)payCustComplain.deductAmt*0.01) : "" %>"/>&nbsp;&nbsp;元（资金处理结果为【结束】状态有效）
                        <%} else { %>
                        <%=payCustComplain.deductAmt != null ? String.format("%.2f",(float)payCustComplain.deductAmt*0.01) : "" %>&nbsp;&nbsp;元
                        <%} %>
                        </td>
                    </tr>
                    <input type="hidden" name="frozenAmt" id="updatePayCustComplainFrozenAmt">
                    <input type="hidden" name="deductAmt" id="updatePayCustComplainDeductAmt">
                    <input type="hidden" name="endFile" id="updatePayCustComplainEndFile">
                    <input type="hidden" name="id" value="<%= payCustComplain.id%>">
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayCustComplainFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payCustComplainUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$(function(){
	$('#updatePayCustComplainEndFileTemp').change(function(){
		var options = {
			url:'<%=path %>/updateUploadFile.htm?id=<%= payCustComplain.id%>',
			type : 'post',
			dataType : 'json',
			success : function(data) {
				$('#updatePayCustComplainEndFile').val(data.saveUrl);
			}
		};
		$('#updatePayCustComplainForm').ajaxSubmit(options);
	});
});
$('#updatePayCustComplainFrozenAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("updatePayCustComplainFrozenAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});
$('#updatePayCustComplainDeductAmtTemp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("updatePayCustComplainDeductAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});

$('#updatePayCustComplainForm').form({
    url:'<%=path %>/updatePayCustComplain.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        $('#payCustComplainList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payCustComplain',payCustComplainUpdatePageTitle,payCustComplainListPageTitle,'payCustComplainList','<%=path %>/payCustComplain.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
function updatePayCustComplainFormSubmit(){
	/*
	if('' == $.trim($('#updatePayCustComplainEndFile').val())){
		topCenterMessage('<%=JWebConstant.ERROR %>','请上传完成凭证');
		return;
	}*/
    $('#updatePayCustComplainForm').submit();
}
</script>
