function get_time(){
return new Date().getTime();
}
function _$(v){
	return document.getElementById(v);
}
//判断密码强度
function SetPWDStrength(n){

            _$("passwd_level_1").style.background="url(./images/bg.gif)";
            _$("passwd_level_2").style.background="url(./images/bg.gif)";
            _$("passwd_level_3").style.background="url(./images/bg.gif)";
     	    if(n==1){
				_$("passwd_level_1").style.background="url(./images/bg1.gif)";
			}
			if(n==2){
			   _$("passwd_level_1").style.background="url(./images/bg1.gif)";
			   _$("passwd_level_2").style.background="url(./images/bg1.gif)";
			}
			if(n==3){
			   _$("passwd_level_1").style.background="url(./images/bg1.gif)";
			   _$("passwd_level_2").style.background="url(./images/bg1.gif)";
			   _$("passwd_level_3").style.background="url(./images/bg1.gif)";
			}

}

function EntertoTab(){
	document.getElementById("input2").focus();
}

function FormSubmit(){  

	if(pgeditor.pwdValid()==1){
		alert("密码不符合要求");
		 _$("_ocx_password").focus();
		 return false;
	} 	
	if(pgeditor.pwdLength()==0){
	     alert("密码不能为空");
		 _$("_ocx_password").focus();
		 return false;
	}
	
	//alert(pgeditor.pwdTypes());

	
	$.ajax({
		url: "./srand_num.jsp?"+get_time(),
		type: "GET",
		async: false,
		success: function(srand_num){
		    pgeditor.pwdSetSk(srand_num);
		}
	 });

	var PwdResult=pgeditor.pwdResult();
	var machineNetwork=pgeditor.machineNetwork();
	var machineDisk=pgeditor.machineDisk();
	var machineCPU=pgeditor.machineCPU();
	
	_$("password").value=PwdResult;//获得密码密文,赋值给表单
	_$("local_network").value=machineNetwork;//获得网卡和MAC信息,赋值给表单
	_$("local_disk").value=machineDisk;//获得硬盘信息,赋值给表单
	_$("local_nic").value=machineCPU;//获得CPU信息,赋值给表单

	//alert(PwdResult);
	document.form1.submit();

}

function FormSubmit1(){  
	$.ajax({
		url: "./srand_num.jsp?"+get_time(),
		type: "GET",
		async: false,
		success: function(srand_num){
			pgeditorcvn.pwdSetSk(srand_num);
		}
	 });
	var PwdResult=pgeditorcvn.pwdResult();
	var machineNetwork=pgeditorcvn.machineNetwork();
	var machineDisk=pgeditorcvn.machineDisk();
	var machineCPU=pgeditorcvn.machineCPU();
	if(pgeditorcvn.pwdLength()==0){
	     alert("密码不能为空");
		 _$("_ocx_password").focus();
		 return false;
	}
	if(pgeditorcvn.pwdValid()==1){
		alert("密码不符合要求");
		 _$("_ocx_password").focus();
		 return false;
	} 

	_$("password").value=PwdResult;//获得密码密文,赋值给表单
	_$("local_network").value=machineNetwork;//获得网卡和MAC信息,赋值给表单
	_$("local_disk").value=machineDisk;//获得硬盘信息,赋值给表单
	_$("local_nic").value=machineCPU;//获得CPU信息,赋值给表单

	document.form1.submit();	
}

function FormSubmit2(){  
	$.ajax({
		url: "./srand_num.jsp?"+get_time(),
		type: "GET",
		async: false,
		success: function(srand_num){
		   pgeditoratm.pwdSetSk(srand_num);
		}
	 });
	var PwdResult=pgeditoratm.pwdResult();
	var machineNetwork=pgeditoratm.machineNetwork();
	var machineDisk=pgeditoratm.machineDisk();
	var machineCPU=pgeditoratm.machineCPU();
	if(pgeditoratm.pwdLength()==0){
	     alert("密码不能为空");
		 _$("_ocx_password2").focus();
		 return false;
	}
	if(pgeditoratm.pwdValid()==1){
		alert("密码不符合要求");
		 _$("_ocx_password2").focus();
		 return false;
	} 

	_$("password").value=PwdResult;//获得密码密文,赋值给表单
	_$("local_network").value=machineNetwork;//获得网卡和MAC信息,赋值给表单
	_$("local_disk").value=machineDisk;//获得硬盘信息,赋值给表单
	_$("local_nic").value=machineCPU;//获得CPU信息,赋值给表单

	document.form1.submit();	
}

function FormSubmit5(){  
	$.ajax({
		url: "./srand_num.jsp?"+get_time(),
		type: "GET",
		async: false,
		success: function(srand_num){
			pgeditor.pwdSetSk(srand_num);
			pgeditor1.pwdSetSk(srand_num);
			pgeditor2.pwdSetSk(srand_num);
		}
	 });

	if(pgeditor.pwdLength()==0){
	     alert("密码不能为空");
		 _$("_ocx_password").focus();
		 return false;
	}
	if(pgeditor.pwdValid()==1){
		alert("密码不符合要求");
		 _$("_ocx_password").focus();
		 return false;
	}
	if(pgeditor1.pwdLength()==0){
	     alert("新密码不能为空");
		 _$("_ocx_password").focus();
		 return false;
	}	
	if(pgeditor1.pwdHash()!=pgeditor2.pwdHash()){
	     alert("两次密码不一致");
		 _$("_ocx_password2").focus();
		 return false;
	}	
	var PwdResult=pgeditor.pwdResult();
	var machineNetwork=pgeditor.machineNetwork();
	var machineDisk=pgeditor.machineDisk();
	var machineCPU=pgeditor.machineCPU();
	_$("password").value=PwdResult;//获得密码密文,赋值给表单
	_$("local_network").value=machineNetwork;//获得网卡和MAC信息,赋值给表单
	_$("local_disk").value=machineDisk;//获得硬盘信息,赋值给表单
	_$("local_nic").value=machineCPU;//获得CPU信息,赋值给表单

	document.form1.submit();	
}
//清除密码强度  
function ClearLevel(){
    _$("passwd_level_1").style.background="url(./images/bg.gif)";
    _$("passwd_level_2").style.background="url(./images/bg.gif)";
    _$("passwd_level_3").style.background="url(./images/bg.gif)";
}
//获取密码强度
function GetLevel(){
  var n=pgeditor.pwdStrength();
  if(n>0){
  	  SetPWDStrength(n);
  }else{
       ClearLevel();
  }
}