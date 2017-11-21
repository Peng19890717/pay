<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.coopbank.dao.PayCoopBank"%>
<%@page import="com.pay.coopbank.service.PayCoopBankService"%>
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
        <form id="addPayChannelFrozenForm" method="post">
            <table cellpadding="5" width="100%" style="margin-top:-10px;">
                <tr><td width="100">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">渠道号：</td>
                        <td><select class="easyui-combobox" type="text" id="addPayChannelFrozenChannel" name="channel" 
                        	data-options="panelHeight:500,editable:false,required:true">
                        <option value=""></option>
                        <%for(int i=0; i<PayCoopBankService.CHANNEL_LIST_ALL.size(); i++){
								PayCoopBank channel = PayCoopBankService.CHANNEL_LIST_ALL.get(i);%>
								<option value="<%=channel.bankCode %>"><%=channel.bankName %></option>
							<%}%>
							</select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">初始冻结金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayChannelFrozenSrcAmtTmp" name="srcAmtTmp" missingMessage="请输入初始冻结金额"
                                data-options="required:true" precision="2" max="99999999999" invalidMessage=""/>&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right">商户号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayChannelFrozenMerNos" name="merNos" missingMessage="请输入商户号"
                                validType="length[1,20]" invalidMessage="商户号为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    <tr>
                        <td align="right">冻结开始时间：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="PayChannelFrozenFrozenTime" name="frozenTime"missingMessage="请输入冻结开始时间"/></td>
                    </tr>
                    <tr>
                        <td align="right">订单金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayChannelFrozenOrderTxamtTmp" name="orderTxamtTmp" missingMessage="请输入订单金额"
                                data-options="required:true" precision="2" max="99999999999" invalidMessage=""/>&nbsp;&nbsp;元</td>
                    </tr>
                    <tr>
                        <td align="right">冻结订单号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayChannelFrozenOrderIds" name="orderIds" missingMessage="请输入冻结订单号"
                                validType="length[1,200]" invalidMessage="冻结订单号为1-200个字符" data-options="required:true"/>多个已“,”或“;”分割</td>
                    </tr>
                    <tr>
                        <td align="right">冻结时长：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayChannelFrozenFrozenDays" name="frozenDays" missingMessage="请输入冻结时长"
                                data-options="required:true" prcision="0" min="0" invalidMessage="" value="0"/>天（0为未知）</td>
                    </tr>
                    <tr>
                    <td align="right">处理结果：</td>
                    <td><select class="easyui-combobox" id="addPayChannelFrozenStatus" name="status" data-options="editable:false">					           
					           <option value="0">初始</option>
					           <option value="1">处理中</option>
					           <option value="2">完结</option>
		     			    </select>
                        </td>
                    </tr>   
                    <!-- <tr>
                        <td align="right">剩余冻结金额：</td>
                        <td><input class="easyui-numberbox" type="text" id="addPayChannelFrozenCurAmtTmp" name="curAmtTmp" missingMessage="请输入剩余冻结金额"
                                data-options="required:true" precision="2" min="0.01" max="999999999" invalidMessage="" value="0"/>&nbsp;&nbsp;元</td>
                    </tr> -->
                    <tr>
                        <td align="right">业务员编号：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayChannelFrozenSalemanId" name="salemanId"
                                validType="length[1,50]" invalidMessage="业务员编号为1-50个字符" data-options="required:true"/></td>
                    </tr>
                    <!--  
                    <tr>
                        <td align="right">记录时间：</td>
                        <td><input class="easyui-datebox" data-options="editable:false,required:true" id="PayChannelFrozenCreateTime" name="createTime"missingMessage="请输入记录时间"
                        /></td>
                    </tr>
                    -->
                    <tr>
                        <td align="right" valign="top">备注：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayChannelFrozenRemark" name="remark" data-options="multiline:true" style="width:240px;height:70px"/></td>
                    </tr>
                    <!--  
                    <tr>
                        <td align="right">登记人员：</td>
                        <td><input class="easyui-textbox" type="text" id="addPayChannelFrozenOptId" name="optId" missingMessage="请输入登记人员"
                                validType="length[1,20]" invalidMessage="登记人员为1-20个字符" data-options="required:true"/></td>
                    </tr>
                    -->
                    <input type="hidden" name="srcAmt" id="addPayChannelFrozenSrcAmt">
                    <input type="hidden" name="orderTxamt" id="addPayChannelFrozenOrderTxamt">
                    <input type="hidden" name="curAmt" id="addPayChannelFrozenCurAmt">
            </table>
        </form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:right;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:addPayChannelFrozenFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#addPayChannelFrozenForm').form('clear')" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
$('#addPayChannelFrozenForm').form({
    url:'<%=path %>/addPayChannelFrozen.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
    success:function(data){
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
            closeTabFreshList('payChannelFrozen',payChannelFrozenAddPageTitle,payChannelFrozenListPageTitle,'payChannelFrozenList','<%=path %>/payChannelFrozen.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
});
$('#addPayChannelFrozenSrcAmtTmp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("addPayChannelFrozenSrcAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
			document.getElementById("addPayChannelFrozenCurAmt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});
$('#addPayChannelFrozenOrderTxamtTmp').numberbox({
	onChange:function(value){
		if(value.length>0){
			document.getElementById("addPayChannelFrozenOrderTxamt").value=parseInt((parseFloat(value)*100).toPrecision(12));
		}
	}
});
function addPayChannelFrozenFormSubmit(){
    $('#addPayChannelFrozenForm').submit();
}
</script>
