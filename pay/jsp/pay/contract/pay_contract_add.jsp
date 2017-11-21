<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ page import="util.JWebConstant"%>
<%@ page import="com.jweb.dao.JWebUser"%>
<%@ page import="com.jweb.dao.JWebAction"%>
<%@ page import="com.jweb.service.JWebUserService"%>
<% 
	String path = request.getContextPath();
%>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'" style="margin-top:-15px">
	<form method="post" id="payContractAddForm">
	 <input type="hidden"  name="pactRenew" value="0" />
	 <table cellpadding="5" width="100%">
	 	<tr><td width="150">&nbsp;</td><td align="right">&nbsp;</td></tr>
	 		<tr>
				<td align="right">商户编号：</td>
				<td><input class="easyui-textbox"  type="text" name="custId" id="custId"  validType="length[1,20]" invalidMessage="商户编号为1-20个字符"  data-options="required:true" style="width:200px"/></td>
		    </tr>
	 		<tr>
				<td align="right">合同名称：</td>
				<td><input class="easyui-textbox"  type="text" name="pactName" id="pactName" validType="length[1,40]" invalidMessage="合同名称为1-40个字符"  data-options="required:true" style="width:200px"/></td>
		    </tr>
			<!-- tr>
				<td align="right">合同续签类型：</td>
				<td>
                   <select name="pactRenew" id="pactRenew" class="easyui-combobox" data-options="required:true">
                         <option value="0"  > 手动续签</option>
                    </select> 
				</td>
			</tr -->
			<tr>
				<td align="right">合同类型：</td>
				<td><input class="easyui-textbox"  type="text" name="pactType" id="pactType"  data-options="required:true"  style="width:200px"
				validType="length[4,4]" invalidMessage="请输入4位数字" /></td>
		    </tr>
			<tr>
				<td align="right">合同版本号：</td>
				<td><input class="easyui-textbox" name="pactVersNo" id="pactVersNo"  type="text" maxlength="3" readonly  style="width:200px"/></td>
		    </tr>
		    <tr>
				<td align="right">合同面向客户类型：</td>
				<td><select name="pactCustType" id="pactCustType" class="easyui-combobox"  data-options="required:true" style="width:200px">
                         <option value="<%=com.PayConstant.CUST_TYPE_USER %>"  >个人</option>
                         <option value="<%=com.PayConstant.CUST_TYPE_MERCHANT %>"  >商户</option>  
                         <option value="<%=com.PayConstant.CUST_TYPE_PAY_CHANNEL %>"  >银行</option>  
                    </select> 
				</td>
			</tr>
		    <tr>
				<td align="right">合同状态：</td>
				<td><select name="pactStatus" id="pactStatus" class="easyui-combobox"  data-options="required:true" style="width:200px">
                         <option value="1"  >正常</option>  
                         <option value="2"  >禁用</option>  
                    </select> 
				</td>
			</tr>
			<tr>
				<td align="right">订立合同介质：</td>
				<td><select name="crePactMed" id="crePactMed" class="easyui-combobox"  data-options="required:true" style="width:200px">
                         <option value="0"  >电子</option>
                         <option value="1"  >书面</option>  
                    </select> 
				</td>
			</tr>
		    <tr>
				<td align="right">订立合同渠道：</td>
				<td><select name="crePactChnl" id="crePactChnl" class="easyui-combobox"  data-options="required:true" style="width:200px">
                         <option value="WEB"  >WEB网页</option> 
                         <option value="WAP"  >WAP网页</option>
                         <option value="MOB"  >手机</option>  
                         <option value="TLR"  >柜面</option>  
                         <option value="INB"  >网银</option>  
                    </select> 
				</td>
			  </tr>
        	<tr>
				<td align="right">订立合同时间：</td>
				<td>
					<input type="text" id="crePactTime" name="crePactTime" class="easyui-datetimespinner" value="" style="width:120px"
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
				<td align="right">合同生效日期：</td>
				<td><input class="easyui-datebox" style="width:200px" id="pactTakeEffDate" name="pactTakeEffDate" data-options="editable:false,required:true" />
				</td>
		    </tr>

			<tr>
				<td align="right">合同失效日期：</td>
				<td><input class="easyui-datebox" style="width:200px" id="pactLoseEffDate" name="pactLoseEffDate" data-options="editable:false,required:true" />
				</td>
		    </tr>
		    <tr>
				<td align="right">签约人：</td>
				<td>
					<input class="easyui-textbox" name="contractSignMan" type="text" data-options="required:true" style="width:200px"
					validType="length[1,20]" invalidMessage="1-20位字符"/>
				</td>
		    </tr>
		    <tr>
				 <td align="right" valign="top">售卖商品描述：</td>
				 <td>
					<input class="easyui-textbox" type="text" name="sellProductInfo"  missingMessage="协议内容请控制在500个字"
                          validType="length[0,500]" invalidMessage="内容为1-500个字符" data-options="multiline:true" 
                          style="width:240px;height:70px"/>                      
			     </td>		
			</tr>
			<tr>
				<td align="right">客户业务人员签字人：</td>
				<td>
					<input class="easyui-textbox" name="custBilaSignName" type="text" maxlength="15"  data-options="required:true"
					validType="length[1,20]" invalidMessage="1-20位字符" style="width:200px"/>
				</td>
		    </tr>
		    		
		    <tr>
				<td align="right">业务人员签字人：</td>
				<td>
					<input class="easyui-textbox" name="bilaSignName" type="text" maxlength="15"  data-options="required:true"
					validType="length[1,20]" invalidMessage="1-20位字符" style="width:200px"/>
				</td>
		    </tr>
			<tr>
				 <td align="right" valign="top">合同内容：</td>
				 <td>
					<input class="easyui-textbox" type="text" id="pactContent2" name="pactContent2"  missingMessage="协议内容请控制在500个字"
                          validType="length[0,500]" invalidMessage="内容为1-500个字符" data-options="multiline:true" 
                          style="width:240px;height:70px"/>
			     </td>		
			</tr>
	    </table>
	</form>
    </div>
    <div data-options="region:'south',border:false" style="text-align:center;padding:5px;">
    	<a id="savePayContract" iconCls="icon-ok" href="#" class="easyui-linkbutton">确定</a>
        <a href="javascript:addPayContractAddFormSubmit()" class="easyui-linkbutton" iconCls="icon-clear" onclick="$('#payContractAddForm').form('clear')">清空</a>
    </div>
</div>
<script type="text/javascript">
$('#pactType').textbox({
	onChange:function(value){
		if(value.length==4){
			//去后台请求版本号
    		$.post('<%=path %>/payContractPactVersNo.htm',{pactType:value},
	        function(data){
	        	$('#pactVersNo').textbox('setValue',data);
	        },
	        //把相应的数据转换成字符串形式，data为转换后的值
	        'text'
		    );  
		}
	}
});
 //为保存按钮绑定事件
$("#savePayContract").click(function(){
	$("#payContractAddForm").submit();
}); 

$('#payContractAddForm').form({
	    url:'<%=path %>/addPayContract.htm',
	    onSubmit: function(){
	        var addPayContractCheck=$(this).form('validate');
	        return addPayContractCheck;
	    },
	    success:function(data){
	    	//data为添加成功后响应的数据
	        if(data=='<%=JWebConstant.OK %>'){
	           topCenterMessage('<%=JWebConstant.OK %>','添加成功！');
	           closeTabFreshList('contractListTab',payContractAddPageTitle,payContractListPageTitle,'payContractList','<%=path %>/payContract.htm');
	        } else topCenterMessage('<%=JWebConstant.ERROR %>',data);
	    }
	});

</script>