//隐藏身份证信息函数，首尾保留，其他用“*”替换，如1****************8
function hideIdCardFormatter(data,row,index){
	if(data == undefined)return "";
	var tmp = "";
	if(data.length == 15 || data.length == 18){
		for(var i=0; i<data.length-2; i++)tmp+="*";
		return data.substr(0,1)+tmp+data.substr(data.length-1,1);
	} else return data;
}
//隐藏姓名函数，数，两位字姓名第二个用“*”代替，多于两位首尾保留，其他用“*”替换
function hideNameFormatter(data,row,index){
	if(data == undefined)return "";
	var tmp = "";
	if(data.length == 2) return data.substr(0,1)+"*";
	else if(data.length > 2){
		for(var i=0; i<data.length-2; i++)tmp+="*";
		return data.substr(0,1)+tmp+data.substr(data.length-1,1);
	} else return data+"*";
}
//隐藏银行卡号，6226 58**  ****  1234，保留前六位和后四位
function hideBankCardNoFormatter(data,row,index){
	if(data == undefined)return "";
	var tmp = "";
	if(data.length > 7){
		for(var i=0; i<data.length-7; i++)tmp+="*";
		return data.substr(0,3)+tmp+data.substr(data.length-4,4);
	} else return data;
}
//隐藏手机号码函数，手机号“186****7491，屏蔽中间四位
function hideMobileFormatter(data,row,index){
	if(data == undefined)return "";
	var tmp = "";
	if(data.length > 7){
		for(var i=0; i<data.length-7; i++)tmp+="*";
		return data.substr(0,3)+tmp+data.substr(data.length-4,4);
	} else return data;
}
//隐藏邮箱函数，邮箱“u*c@163.com
function hideEmailFormatter(data,row,index){
	if(data == undefined)return "";
	var tmp = "";
	var sTmp = data.indexOf("@");
	if(sTmp<0)return data;
	var emailName = data.substr(0,sTmp);
	if(emailName.length <= 2 ) return data.substr(0,1)+"*"+data.substr(sTmp);
	else {
		for(var i=0; i<emailName.length-2; i++)tmp+="*";
		return emailName.substr(0,1)+tmp+emailName.substr(data.length-1,1)+data.substr(sTmp-1);
	}
}
/**隐藏信息函数 className为class名，如<div class="nameHiddenFormat">张三</div>*/
/**
当前隐藏信息包括
姓名class="nameHiddenFormat"
身份证号码class="idCardHiddenFormat"
银行卡号class="bankCardNoHiddenFormat"
手机号class="mobileHiddenFormat"
*/
function hideText(className){
	var hiddenText = $('.'+className);
	var len = hiddenText.length;
	try {
		for(var i=0;i<len;i++) {
			//文本框
			if(hiddenText[i].nodeName == "INPUT"){
				hiddenText[i].value = hideTextFunction(className,hiddenText[i].value);
			} else {//其他
				var text =hiddenText[i].innerText;
				if(text==undefined || text==null){
					text = hiddenText[i].innerHTML;
					hiddenText[i].innerHTML = hideTextFunction(className,text);
				} else {
					hiddenText[i].innerText = hideTextFunction(className,text);
				}
			}
		}
	} catch (e){}
}
function hideTextFunction(className,text){
	var tmp = "";
	//隐藏身份证信息函数，首尾保留，其他用“*”替换，如1****************8
	if(className == 'idCardHiddenFormat'){
		if(text.length == 15 || text.length == 18){
			for(var i=0; i<text.length-2; i++)tmp+="*";
			return text.substr(0,1)+tmp+text.substr(text.length-1,1);
		} else return text;
	} 
	//隐藏姓名函数，两位字姓名第二个用“*”代替，多于两位首尾保留，其他用“*”替换
	else if(className == 'nameHiddenFormat'){
		if(text.length == 2) return text.substr(0,1)+"*";
		else if(text.length > 2){
			for(var i=0; i<text.length-2; i++)tmp+="*";
			return text.substr(0,1)+tmp+text.substr(text.length-1,1);
		} else return text;
	}
	//隐藏银行卡号，6226 58**  ****  1234，保留前六位和后四位
	else if(className == 'bankCardNoHiddenFormat'){
		if(text.length > 10){
			for(var i=0; i<text.length-10; i++)tmp+="*";
			return text.substr(0,6)+tmp+text.substr(text.length-4,4);
		} else return text;
	}
	//隐藏手机号码函数，手机号“186****7491，屏蔽中间四位
	else if(className == 'mobileHiddenFormat'){
		if(text.length > 7){
			for(var i=0; i<text.length-7; i++)tmp+="*";
			return text.substr(0,3)+tmp+text.substr(text.length-4,4);
		} else return text;
	}
	//隐藏邮箱函数，邮箱“u*c@163.com
	else if(className == 'emailHiddenFormat'){
		var sTmp = text.indexOf("@");
		if(sTmp<0)return text;
		var emailName = text.substr(0,sTmp);
		if(emailName.length <= 2 ) return text.substr(0,1)+"*"+text.substr(sTmp);
		else {
			for(var i=0; i<emailName.length-2; i++)tmp+="*";
			return emailName.substr(0,1)+tmp+emailName.substr(text.length-1,1)+text.substr(sTmp-1);
		}
	}
}
/**
type=0 成功提示，其他为错误提示
*/
function topCenterMessage(type,info) {
	if (type == "0") info = "<font color=\"#4CB400\">" + info + "</font>";
	else{
		if (info == loginAtOtherNo || info == loginTimeOut || info == undefinedError) {
			window.location.href = scriptPath + "/jsp/jweb/login.jsp";
			return;
		}
		info = "<font color=\"#EB3939\">" + info + "</font>";
	}
	$.messager.show({title:"提示", msg:info, showType:"slide", style:{right:"", top:document.body.scrollTop + document.documentElement.scrollTop, bottom:""}});
}
var O2String = function (O) { 
//return JSON.stringify(jsonobj); 
	var S = [];
	var J = "";
	if (Object.prototype.toString.apply(O) === "[object Array]") {
		for (var i = 0; i < O.length; i++) {
			S.push(O2String(O[i]));
		}
		J = "[" + S.join(",") + "]";
	} else {
		if (Object.prototype.toString.apply(O) === "[object Date]") {
			J = "new Date(" + O.getTime() + ")";
		} else {
			if (Object.prototype.toString.apply(O) === "[object RegExp]" || Object.prototype.toString.apply(O) === "[object Function]") {
				J = O.toString();
			} else {
				if (Object.prototype.toString.apply(O) === "[object Object]") {
					for (var i in O) {
						O[i] = typeof (O[i]) == "string" ? "\"" + O[i] + "\"" : (typeof (O[i]) === "object" ? O2String(O[i]) : O[i]);
						S.push(i + ":" + O[i]);
					}
					J = "{" + S.join(",") + "}";
				}
			}
		}
	}
	return J;
};
//遮罩层
(function(){
    $.extend($.fn,{
        mask: function(msg){
            this.unmask();
            var original=$(document.body);
            var position={top:0,left:0};
            if(this[0] && this[0]!==window.document){
                original=this;
                position=original.position();
            }
            // 创建一个 Mask 层，追加到对象中
            var maskDiv=$('<div>&nbsp;</div>');
            maskDiv.appendTo(original);
            var maskWidth=original.outerWidth();
            if(!maskWidth){
                maskWidth=original.width();
            }
            var maskHeight=original.outerHeight();
            if(!maskHeight){
                maskHeight=original.height();
            }
            maskDiv.css({
                position: 'absolute',
                top: position.top,
                left: position.left,
                'z-index': 10000,
              	width: maskWidth,
                height:maskHeight,
                'background-color': '#F0F0F0',
                opacity: 0.4
            });
            if(msg){
                var msgDiv=$('<div style="position:absolute;border:#6593cf 2px solid;padding:6px;background:#F0F0F0;font-size:12px">'
                +'<table border="0" cellpadding="0" cellspacing="0"><tr><td align="right" valign="middle">'
                +'<img style="margin-top:2px;margin-right:5px;" src="'+scriptPath 
                +'/js/jquery-easyui-1.4/themes/default/images/loading.gif" alt=""/></td><td align="left" valign="middle">'+msg+'</td></tr></table></div>');
                msgDiv.appendTo(original);
                var widthspace=(maskDiv.width()-msgDiv.width());
                var heightspace=(maskDiv.height()-msgDiv.height());
                msgDiv.css({
                    cursor:'wait',
                    top:(heightspace/2-2),
                    left:(widthspace/2-2)
                });
            }
            return maskDiv;
        },
     unmask: function(){
                 var original=$(document.body);
                     if(this[0] && this[0]!==window.document){
                        original=$(this[0]);
                  }
                  original.find("> div.maskdivgen").fadeOut('slow',0,function(){
                      $(this).remove();
                  });
        }
    });
})();
function openTab(pageId,pageTitle,pageUrl){
	var tabTmp = $('#mainTabs');
	if(tabTmp.tabs('exists',pageTitle))tabTmp.tabs('close',pageTitle);
	tabTmp.tabs('add',{id:pageId,title: pageTitle,closable: true,border:false});
	$('#'+pageId).panel('refresh',pageUrl);
	tabTmp.tabs('select',pageTitle);
}
function closeTabFreshList(pageId,pageTitle,listTitle,listName,listUrl){
	try{
	var tabTmp = $('#mainTabs');
	tabTmp.tabs('close',pageTitle);
    if(tabTmp.tabs('exists',listTitle))$('#'+listName).datagrid('reload');
    else { //如果不存在，打开新商户列表
        tabTmp.tabs('add',{id:pageId,title:listTitle,closable: true,border:false});
        $('#'+pageId).panel('refresh',listUrl);
    }
    tabTmp.tabs('select',listTitle);
	}catch(e){alert(e);}
}
/**
 * add by cgh
 * 针对panel window dialog三个组件拖动时会超出父级元素的修正
 * 如果父级元素的overflow属性为hidden，则修复上下左右个方向
 * 如果父级元素的overflow属性为非hidden，则只修复上左两个方向
 * @param left
 * @param top
 * @returns
 */
var easyuiPanelOnMove = function(left, top) {
	var parentObj = $(this).panel('panel').parent();
	if (left < 0) {
		$(this).window('move', {
			left : 1
		});
	}
	if (top < 0) {
		$(this).window('move', {
			top : 1
		});
	}
	var width = $(this).panel('options').width;
	var height = $(this).panel('options').height;
	var right = left + width;
	var buttom = top + height;
	var parentWidth = parentObj.width();
	var parentHeight = parentObj.height();
	if(parentObj.css("overflow")=="hidden"){
		if(left > parentWidth-width){
			$(this).window('move', {
				"left":parentWidth-width
			});
		}
		if(top > parentHeight-height){
			$(this).window('move', {
				"top":parentHeight-height
			});
		}
	}
};
$.fn.panel.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
/**
 * 表头右键菜单
 */
function createColumnMenu(tableId){
    tableHeaderMenu = $('<div/>').appendTo('body');
    tableHeaderMenu.menu({
        onClick: function(item){
            if (item.iconCls == 'icon-ok'){
                $('#'+tableId).datagrid('hideColumn', item.name);
                tableHeaderMenu.menu('setIcon', {
                    target: item.target,
                    iconCls: 'icon-empty'
                });
            } else {
                $('#'+tableId).datagrid('showColumn', item.name);
                tableHeaderMenu.menu('setIcon', {
                    target: item.target,
                    iconCls: 'icon-ok'
                });
            }
        }
    });
    var fields = $('#'+tableId).datagrid('getColumnFields');
    for(var i=0; i<fields.length; i++){
        var field = fields[i];
        var col = $('#'+tableId).datagrid('getColumnOption', field);
        tableHeaderMenu.menu('appendItem', {
            text: col.title,
            name: field,
            iconCls: 'icon-ok'
        });
    }
    return tableHeaderMenu;
}
$.extend($.fn.validatebox.defaults.rules, {
	CHS:{
		validator:function (value, param) {
			return /^[\u0391-\uFFE5]+$/.test(value);
		}, message:"请输入汉字"
	},ZIP:{
		validator:function (value, param) {
			return /^[1-9]\d{5}$/.test(value);
		}, message:"邮政编码不存在"
	}, QQ:{
		validator:function (value, param) {
			return /^[1-9]\d{4,10}$/.test(value);
		}, message:"QQ号码不正确"
	}, mobile:{
		validator:function (value, param) {
			return /^1\d{10}$/.test(value);
		}, message:"手机号码不正确"
	}, loginName:{
		validator:function (value, param) {
			return /^[\u0391-\uFFE5\w]+$/.test(value);
		}, message:"登录名称只允许汉字、英文字母、数字及下划线。"
	}, safepass:{
		validator:function (value, param) {
			return safePassword(value);
		}, message:"\密码由字母和数字组成，至少6位"
	}, equalTo:{
		validator:function (value, param) {
			return value == $(param[0]).val();
		}, message:"两次输入的字符不一至"
	}, number:{
		validator:function (value, param) {
			return /^\d+$/.test(value);
		}, message:"请输入数字"
	}, IP:{
		validator:function (value, param) {
			return /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value);
		}, message:"请输入正确IP地址"
	}, IPS:{
		validator:function (value, param) {
			try{
				var ips= value.replace("；", ";").split(";"); //定义一数组
				if(ips.length>20)return false;
				for (i=0;i<ips.length ;i++){
					if(ips[i].length!=0){
						if(!/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(ips[i])){
							return false;
						}
					}
				}
				return true;
			}catch(e){alert(e);}
		}, message:"请输入正确IP地址"
	}, idcard:{
		validator:function (value, param) {
			if(($(param[0]).val() == '0')){
				$.fn.textbox.defaults.rules.idcard.message = '请输入正确的身份证号';
				return idCard(value);
			} else {
				if(value.length==0 ||value.length >20){
					$.fn.textbox.defaults.rules.idcard.message = '证件号码为1-20位字符';
					return false;
				}
			}
			return true;
		}
	}, url:{
		validator:function (value, param) {
			if (value) {
				return /(((https?)|(ftp)):\/\/([\-\w]+\.)+\w{2,3}(\/[%\-\w]+(\.\w{2,})?)*(([\w\-\.\?\\\/+@&#;`~=%!]*)(\.\w{2,})?)*\/?)/i.test(value);
			} else {
				return true;
			}
		}, message:"请输入有效的URL."
	}, inputExistInCombobox: {//combox下拉列表中输入值是否为列表中的值
		validator: function(value,param){
			try{
				var myValue=$('#'+param[0]).combobox('getValue');
				//topCenterMessage('0',myValue+","+value);
				if(myValue==undefined || myValue==null)return false;
				return true;
			} catch(e){}
		},   
		message: '请选择列表中的值'  
	}
});
/* 密码由字母和数字组成，至少8位 */
var safePassword = function (value) {
	return !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,7})$|\s/.test(value));
};
var idCard = function (value) {
	if (value.length == 18 && 18 != value.length) {
		return false;
	}
	var number = value.toLowerCase();
	var d, sum = 0, v = "10x98765432", w = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2], a = "11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91";
	var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/);
	if (re == null || a.indexOf(re[1]) < 0) {
		return false;
	}
	if (re[2].length == 9) {
		number = number.substr(0, 6) + "19" + number.substr(6);
		d = ["19" + re[4], re[5], re[6]].join("-");
	} else {
		d = [re[9], re[10], re[11]].join("-");
	}
	if (!isDateTime.call(d, "yyyy-MM-dd")) {
		return false;
	}
	for (var i = 0; i < 17; i++) {
		sum += number.charAt(i) * w[i];
	}
	return (re[2].length == 9 || number.charAt(17) == v.charAt(sum % 11));
};
var isDateTime = function (format, reObj) {
	format = format || "yyyy-MM-dd";
	var input = this, o = {}, d = new Date();
	var f1 = format.split(/[^a-z]+/gi), f2 = input.split(/\D+/g), f3 = format.split(/[a-z]+/gi), f4 = input.split(/\d+/g);
	var len = f1.length, len1 = f3.length;
	if (len != f2.length || len1 != f4.length) {
		return false;
	}
	for (var i = 0; i < len1; i++) {
		if (f3[i] != f4[i]) {
			return false;
		}
	}
	for (var i = 0; i < len; i++) {
		o[f1[i]] = f2[i];
	}
	o.yyyy = s(o.yyyy, o.yy, d.getFullYear(), 9999, 4);
	o.MM = s(o.MM, o.M, d.getMonth() + 1, 12);
	o.dd = s(o.dd, o.d, d.getDate(), 31);
	o.hh = s(o.hh, o.h, d.getHours(), 24);
	o.mm = s(o.mm, o.m, d.getMinutes());
	o.ss = s(o.ss, o.s, d.getSeconds());
	o.ms = s(o.ms, o.ms, d.getMilliseconds(), 999, 3);
	if (o.yyyy + o.MM + o.dd + o.hh + o.mm + o.ss + o.ms < 0) {
		return false;
	}
	if (o.yyyy < 100) {
		o.yyyy += (o.yyyy > 30 ? 1900 : 2000);
	}
	d = new Date(o.yyyy, o.MM - 1, o.dd, o.hh, o.mm, o.ss, o.ms);
	var reVal = d.getFullYear() == o.yyyy && d.getMonth() + 1 == o.MM && d.getDate() == o.dd && d.getHours() == o.hh && d.getMinutes() == o.mm && d.getSeconds() == o.ss && d.getMilliseconds() == o.ms;
	return reVal && reObj ? d : reVal;
	function s(s1, s2, s3, s4, s5) {
		s4 = s4 || 60, s5 = s5 || 2;
		var reVal = s3;
		if (s1 != undefined && s1 != "" || !isNaN(s1)) {
			reVal = s1 * 1;
		}
		if (s2 != undefined && s2 != "" && !isNaN(s2)) {
			reVal = s2 * 1;
		}
		return (reVal == s1 && s1.length != s5 || reVal > s4) ? -10000 : reVal;
	}
};
function Map() {
	this.keys = new Array();
	this.data = new Array();
	//添加键值对
	this.set = function (key, value) {
		if (this.data[key] == null) {//如键不存在则身【键】数组添加键名
		this.keys.push(value);
		}
		this.data[key] = value;//给键赋值
	};
	//获取键对应的值
	this.get = function (key) {
		return this.data[key];
	};
	//去除键值，(去除键数据中的键名及对应的值)
	this.remove = function (key) {
		this.keys.remove(key);
		this.data[key] = null;
	};
	//判断键值元素是否为空
	this.isEmpty = function () {
		return this.keys.length == 0;
	};
	//获取键值元素大小
	this.size = function () {
		return this.keys.length;
	};
}
