/**
 * 扩展easyui validatebox校验
 * easyui的validatebox只支持email和URL验证
 */
$.extend($.fn.validatebox.defaults.rules, {
	alphanum: {
        validator: function(value, param) {     
        	return /^[a-zA-Z0-9]+$/.test(value);   
        },     
        message: '请输入字母和数字'     
    },  
    minLength: { 
    	validator: function(value, param) { 
    	   value = $.trim(value);	//去空格
    	   return value.length >= param[0]; 
    	}, 
    	message: '请输入至少{0}个字符 '//不包括空格字符
    },
    maxLength: { 
    	validator: function(value, param) {    	
    	   value = $.trim(value);	//去空格
    	   return value.length <= param[0];
    	}, 
    	message: '请输入最多{0}个字符'
    },
    //中文unicode编码范围为\u4E00-\u9FA5
    CHS_alphanum: {
        validator: function (value, param) {
            return /^[\u4E00-\u9FA5a-zA-Z0-9（）()_-]+$/.test(value);
        },
        message: '请输入汉字、字母、数字、中划线、下划线、括号'
    },
    //字母、数字、汉字、下划线半全角
    CHSA_alphanum: {
        validator: function (value, param) {
            return /^[\u4E00-\u9FA5\uff41-\uff5a\uff21-\uff3a\uFF10-\uFF19a-zA-Z0-9_]+$/.test(value);
        },
        message: '请输入字母、数字、汉字、下划线'
    },
    //常用于编码
    code_alphanum: {
        validator: function (value, param) {
            return /^[a-zA-Z0-9_-]+$/.test(value);
        },
        message: '请输入字母、数字、中划线、下划线'
    },
    zip: {
        validator: function (value, param) {
            return /^\d{6}$/.test(value);
        },
        message: '请输入正确的邮政编码'
    },
    //腾讯QQ号从10000开始
    QQ: {
        validator: function (value, param) {
            return /^[1-9]\d{4,}$/.test(value);
        },
        message: '请输入正确的QQ号'
    },
    //手机号码以1开头的11位数字
    phone: {
        validator: function (value, param) {
            return /^1[0-9]{10}$/.test(value);
        },
        message: '请输入正确的手机号码格式'
    },
    equalTo: {
        validator: function (value, param) {
            return value == $(param[0]).val();
        },
        message: '两次输入的字符不一致'
    },
    money: {     
        validator: function(value) {
        	value = $.trim(value);
            return /^(([1-9]\d{0,7})|0)(\.\d{1,2})?$/.test(value);     
        },     
        message: '请输入最大可由8位正整数和两位小数组成'    
    },
    positive_num: {
    	validator: function(value) {     
    		return /^[1-9][0-9]*$/.test(value);     
        },     
        message: '请输入正整数'
    },
    positiveZ_num: {
    	validator: function(value) {     
    		return /^(0|[1-9][0-9]*)$/.test(value);     
        },     
        message: '请输入0或正整数'
    },
    allBlank: {
    	 validator: function (value) {
         	return $.trim(value) == "";
         },
         message: '输入内容不能全是空格'
    },
    IP: {
    	validator: function (value) {
    		return /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(value)
    	},
    	message: '请输入正确的IP地址'
    },
   //计算机协议的端口号，范围应该是0-65535
	port_num: {
    	validator: function(value) {
    		var isNum = /^(0|[1-9][0-9]*)$/.test(value);
			if (isNum && (value.length <= 5) && (value <= 65535)) {
				return true;
			}
    		return false;
        },    
        message: '请输入正确的端口号（0~65535）'
    },   
    no_specalChars: {
    	validator : function(value, param) {
    		return /[^\/@<>''""]/.test(value);
    	},
    	message: '不能输入特殊字符\\、/、@、<、>'
    },
    //匹配十六进制
//    hex_chars: {
//    	validator : function(value, param) {
//    		return /^#?([a-zA-Z0-9]{6}|[a-zA-Z0-9]{3})$/.test(value);
//    	},
//    	message: '请输入合法的十六进制'
//    }
});