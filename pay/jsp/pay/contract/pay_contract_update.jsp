<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<%@ page import="com.pay.contract.dao.PayContract"%>
<%
String path = request.getContextPath();
//登录和权限判断
JWebUser user = (JWebUser)session.getAttribute("user");
if(!JWebUserService.checkUser(user,request)){
%><script>window.location.href='<%=path %>/jsp/jweb/login.jsp'</script><%
    return;
}
PayContract payContract = (PayContract)request.getAttribute("payContractUpdate");
%>
<script type="text/javascript">
$(document).ready(function(){});
</script>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'" style="margin-top:-15px">
        <%if(payContract != null){ %>
        <form id="updatePayContractForm" method="post">
        	<input type="hidden" name="seqNo" value="${payContractUpdate.seqNo}">
        	<input type="hidden" name="creTime" value="${payContractUpdate.creTime}">
        	<input type="hidden" name="creOperId" value="${payContractUpdate.creOperId}">
        	<input type="hidden"  name="pactRenew" value="0" />
            <table cellpadding="5">
                <tr><td width="150">&nbsp;</td><td>&nbsp;</td></tr>
                    <tr>
                        <td align="right">合同编号：</td>
                        <td>${payContractUpdate.seqNo}</td>
                    </tr>
                    <tr>
                        <td align="right">商户编号：</td>
                        <td>${payContractUpdate.custId}</td>
                    </tr>
                    <tr>
                        <td align="right">合同类型：</td>
                        <td>${payContractUpdate.pactType}</td>
                    </tr>
                    <tr>
                        <td align="right">合同版本号：</td>
                        <td>${payContractUpdate.pactVersNo}</td>
                    </tr>
                    <tr>
                        <td align="right">合同名称：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayContractPactName" name="pactName" data-options="required:true" 
                                validType="length[1,40]" invalidMessage="合同名称为1-40个字符" value="${payContractUpdate.pactName}" style="width:200px"/></td>
                    </tr>
                    <tr>
                        <td align="right">合同面向客户类型：</td>
                        <td>
                        	<select name="pactCustType" id="updatePayContractPactCustType" class="easyui-combobox"  data-options="required:true" style="width:200px">
                        		<option value="<%=com.PayConstant.CUST_TYPE_USER %>" <c:if test="${payContractUpdate.pactCustType eq 0}">selected</c:if>>个人</option>
                        		<option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>" <c:if test="${payContractUpdate.pactCustType eq 1}">selected</c:if>>商户</option>  
                        		<option value="<%=com.PayConstant.CUST_TYPE_PAY_CHANNEL %>" <c:if test="${payContractUpdate.pactCustType eq 2}">selected</c:if>>银行</option>  
                    		</select> 
                        </td>
                                
                    </tr>
                    <tr>
                        <td align="right">合同生效日期：</td>
                        <td><input class="easyui-datebox" style="width:200px" data-options="editable:false,required:true" id="updatePayContractPactTakeEffDate" name="pactTakeEffDate"
                                value="<%=payContract.pactTakeEffDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payContract.pactTakeEffDate):"" %>"/></td>
                    </tr>
                    <tr>
                        <td align="right">合同失效日期：</td>
                        <td><input class="easyui-datebox" style="width:200px" data-options="editable:false,required:true" id="updatePayContractPactLoseEffDate" name="pactLoseEffDate"
                                value="<%=payContract.pactLoseEffDate != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(payContract.pactLoseEffDate):"" %>"/></td>
                    </tr>
                    <tr>
						<td align="right">签约人：</td>
						<td>
							<input class="easyui-textbox" name="contractSignMan" type="text" data-options="required:true"
							validType="length[1,20]" invalidMessage="1-20位字符" value="${payContractUpdate.contractSignMan}" style="width:200px"/>
						</td>
				    </tr>
				    <tr>
						 <td align="right" valign="top">售卖商品描述：</td>
						 <td>
							<input class="easyui-textbox" type="text" name="sellProductInfo"  missingMessage="协议内容请控制在500个字"
		                          validType="length[0,500]" invalidMessage="内容为1-500个字符" data-options="multiline:true" 
		                          style="width:240px;height:70px" value="${payContractUpdate.sellProductInfo}" style="width:200px"/>                      
					     </td>		
					</tr>
                    <tr>
                        <td align="right">合同状态：</td>
                        <td>
	                        <c:if test="${payContractUpdate.pactStatus eq 1}">
			    				正常
			    			</c:if>
			    			<c:if test="${payContractUpdate.pactStatus eq 2}">
			    				禁用
			    			</c:if>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">订立合同介质：</td>
                        <td>
                        	<select name="crePactMed" id="updatePayContractCrePactMed" class="easyui-combobox"  data-options="required:true" style="width:200px">
                         		<option value="0" <c:if test="${payContractUpdate.crePactMed eq 0}">selected</c:if> >电子</option>
                         		<option value="1" <c:if test="${payContractUpdate.crePactMed eq 1}">selected</c:if> >书面</option>  
                   			</select> 
                        </td>
                    </tr>
                    <tr>
                        <td align="right">订立合同渠道：</td>
                        <td>
                         	<select name="crePactChnl" id="updatePayContractCrePactChnl" class="easyui-combobox"  data-options="required:true" style="width:200px">
		                         <option value="WEB" <c:if test="${payContractUpdate.crePactChnl eq 'WEB'}">selected</c:if> >WEB网页</option> 
		                         <option value="WAP" <c:if test="${payContractUpdate.crePactChnl eq 'WAP'}">selected</c:if> >WAP网页</option>
		                         <option value="MOB" <c:if test="${payContractUpdate.crePactChnl eq 'WAP'}">selected</c:if> >手机</option>  
		                         <option value="TLR" <c:if test="${payContractUpdate.crePactChnl eq 'TLR'}">selected</c:if> >柜面</option>  
		                         <option value="INB" <c:if test="${payContractUpdate.crePactChnl eq 'INB'}">selected</c:if> >网银</option>  
                    		</select> 
                        </td>
                    </tr>
                    <!-- tr>
                        <td align="right">合同续签类型：</td>
                        <td>
	                        <select name="pactRenew" id="updatePayContractPactRenew" class="easyui-combobox" data-options="required:true">
	                         <option value="0" > 手动续签</option>
	                    	</select> 
                        </td>
                    </tr -->
                    <tr>
                        <td align="right">订立合同时间：</td>
                        <td>
                            <input type="text" id="updatePayContractCrePactTime" name="crePactTime" class="easyui-datetimespinner" value="${payContractUpdate.crePactTime}"  style="width:200px"
					data-options="required:true,
		            showSeconds: true,
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
						<td align="right">客户业务人员签字人：</td>
						<td>
							<input class="easyui-textbox" name="custBilaSignName" type="text" maxlength="15"  data-options="required:true"
							validType="length[1,20]" invalidMessage="1-20位字符" value="${payContractUpdate.custBilaSignName}" style="width:200px"/>
						</td>
				    </tr>
				    		
				    <tr>
						<td align="right">业务人员签字人：</td>
						<td>
							<input class="easyui-textbox" name="bilaSignName" type="text" maxlength="15"  data-options="required:true"
							validType="length[1,20]" invalidMessage="1-20位字符" value="${payContractUpdate.bilaSignName}" style="width:200px"/>
						</td>
				    </tr>
				    <tr>
                        <td align="right" valign="top">最后修改原因：</td>
                        <td><input class="easyui-textbox" type="text" name="updateInfo"
                                validType="length[0,500]" invalidMessage="协议内容请控制在500个字" data-options="multiline:true" value="${payContractUpdate.updateInfo}"
                                style="width:240px;height:70px"/></td>
                    </tr>
                    <tr>
                        <td align="right" valign="top">合同内容：</td>
                        <td><input class="easyui-textbox" type="text" id="updatePayContractPactContent2" name="pactContent2"
                                validType="length[0,500]" invalidMessage="协议内容请控制在500个字" data-options="multiline:true" value="${payContractUpdate.pactContent2}"
                                style="width:240px;height:70px"/></td>
                    </tr>
            </table>
        </form>
        <%} %>
    </div>
    <div data-options="region:'south',border:false" style="text-align:center;padding:5px;">
        <a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:updatePayContractFormSubmit()" style="width:80px">确定</a>
        <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" 
            onclick="$('#mainTabs').tabs('close',payContractUpdatePageTitle)" style="width:80px">取消</a>
    </div>
</div>
<script type="text/javascript">
function updatePayContractFormSubmit(){
    $('#updatePayContractForm').submit();
}
$('#updatePayContractForm').form({
    url:'<%=path %>/updatePayContract.htm',
    onSubmit: function(){
        return $(this).form('validate');
    },
   success:function(data){
        $('#payContractList').datagrid('reload');
        if(data=='<%=JWebConstant.OK %>'){
            topCenterMessage('<%=JWebConstant.OK %>','修改成功！');
            closeTabFreshList('payContract',payContractUpdatePageTitle,payContractListPageTitle,'payContractList','<%=path %>/payContract.htm');
        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
    }
 });   
$('#updatePayContractPactType').textbox({
	onChange:function(value){
		if(value.length==4){
			//去后台请求版本号
    		$.post('<%=path %>/payContractPactVersNo.htm',{pactType:value},
	        function(data){
	        	$('#updatePayContractPactVersNo').textbox('setValue',data);
	        },
	        //把相应的数据转换成字符串形式，data为转换后的值
	        'text'
		    );  
		}
	}
});
//为保存按钮绑定事件
$("#saveUpdatePayContract").click(function(){
	$("#updatePayContractForm").submit();
}); 
</script>
