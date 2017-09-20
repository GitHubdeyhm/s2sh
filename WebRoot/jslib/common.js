/**
 * 将html标签转义,要转义的特殊字符为：&，<，>
 * @Date 2016-7-17上午12:58:35
 * @param escapeStr 需要转义的字符串
 * @returns 转义后的字符串
 */
function escapeHtml(escapeStr) {
	//判断传入的字符串是否为：null，""， "   "
	if ((escapeStr == null) || (escapeStr.replace(/^\s+|\s+$/g, "") == "")) {
		return "";
	}
	//应该先转义&字符
	var es = escapeStr.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
	return es;
}
/**
 * 格式化日期对象--返回格式化后的日期字符串形式
 * @Date 2016-7-17上午1:00:07
 * @param dateObj 日期对象
 * @param noSeconds 布尔值，可选参数。是否显示秒。true代表不显示秒，否则显示秒
 * @returns {String} 格式化后的日期字符串格式：yyyy-MM-dd HH:mm:ss
 */
function formateTime(date, noSeconds) {
	var dateObj = null;
	//不是日期对象返回空字符串
	if ((typeof date == "object") && (date instanceof Date)) {
		dateObj = date;
	} else if (typeof date == "number") {
		dateObj = new Date(date);//毫秒数
	} else {
		return "";
	}
	var year = dateObj.getFullYear();//年份
	var month = dateObj.getMonth() + 1;//月份
	if (month < 10) {
		month = "0" + month;
	}
	var day = dateObj.getDate();//天
	if (day < 10) {
		day = "0" + day;
	}
	var hour = dateObj.getHours();//小时
	if (hour < 10) {
		hour = "0" + hour;
	}
	var minutes = dateObj.getMinutes();//分钟
	if (minutes < 10) {
		minutes = "0" + minutes;
	}
	var time = year+"-"+month+"-"+day+" "+hour+":"+minutes;
	//显示秒
	if (!noSeconds) {
		var seconds = dateObj.getSeconds();//秒
		if (seconds < 10) {
			seconds = "0" + seconds;
		}
		time += ":"+seconds;
	}
	return time;
}
/**
 * 得到系统当前时间
 * @Date 2016-7-17上午1:06:28
 * @param noSeconds 布尔值，可选参数。是否显示秒。true代表不显示秒，否则显示秒
 * @returns {String} 格式化后的日期字符串格式：yyyy-MM-dd HH:mm:ss
 */
function getCurrentTime(noSeconds) {
	return formateTime(new Date(), noSeconds);
}
/**
 * 得到系统当前日期
 * @Date 2016-7-17上午1:08:10
 * @returns {String} 格式化后的日期字符串格式：yyyy-MM-dd
 */
function getCurrentDate() {
	return formateTime(new Date()).split(" ")[0];
}

/**
 * 格式化日期对象，得到格式为某年某月某日的日期格式
 * @Date 2016-9-25下午7:38:24
 * @param date Date对象或者毫秒数
 * @returns 某年某月某日的日期格式
 */
function formateChineseDate(date) {
	var dateObj = null;
	//Date对象
	if ((typeof date == "object") && (date instanceof Date)) {
		dateObj = date;
	} else if (typeof date == "number") {
		dateObj = new Date(date);//毫秒数
	} else {
		return "";
	}
	var year = dateObj.getFullYear();//年份
	var month = dateObj.getMonth() + 1;//月份
	var day = dateObj.getDate();//天
	return year + "年" + month+ "月" + day + "日";
}


/**
 * 格式化日期长整型，毫秒数
 * @param longTime：时间的毫秒数
 * @returns：格式化后的日期字符串形式：yyyy-MM-dd HH:mm:ss
 */
function timeFmt(longTime) {
	if (longTime) {
		return formateTime(new Date(longTime));
	}
	return "";	
}
/**
 * 根据日期得到当天所属一周范围内的日期
 * @param dateStr：日期字符串：格式类似为yyyy-MM-dd或yyyy-MM-dd HH:mm:ss，分割符号也可为“/”
 */
function getWeeksByDay(dateStr) {
	if (!dateStr) {
		return null;
	}
	var date = new Date(dateStr);//创建日期对象
	var week = date.getDay();//得到一周中的星期几--0（周日） 到 6（周六）
	var n = 6;//默认为星期天
	if (week > 0) {
		n = --week;
	}
	date.setDate(date.getDate()-n);//确保从星期一开始
	var weeks = ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"];
	var weeksDay = [];
	for (var i = 0; i < 7; i++) {			
		var year = date.getFullYear();//年份
		var month = date.getMonth() + 1;//月份
		if (month < 10) {
			month = "0" + month;
		}
		var day = date.getDate();//天
		if (day < 10) {
			day = "0" + day;
		}
		var ymd = year+"-"+month+"-"+day;
		//var w = weeks[i]+"("+ymd+")";//格式为：星期一(2015-12-12)
		var w = ymd;
		weeksDay.push(w);
		date.setDate(date.getDate()+1);				
	}
	return weeksDay;
}

/**
 * 实现将数字保留指定的小数位数，默认为保留两位小数。此方法会四舍五入
 * @Date 2016-7-15上午10:51:43 
 * @param num 数字，必须参数
 * @param prec 保留的小数位数，默认为两位，可选参数
 * @param defaultValue 不能转换为数字时的默认值。默认为0。写此参数时prec参数不能为空
 * @returns 返回数值类型，如果不能转换为数值类型则返回默认值defaultValue。默认为0
 */
function saveDecimal(num, prec, defaultValue) {
	var dec = parseFloat(num);
	//不能转换为数字
	if (isNaN(dec)) {
		return defaultValue || 0;
	}
	var p = prec || 2;
	return Number(dec.toFixed(p));//必须是数值类型
}

/**
 * 屏蔽退格键(backspace)，防止按backspace键刷新页面。
 * <p>只对密码框、可输入文本框、可输入多行文本框开启backspace键</p>
 * @param e 事件对象
 * @returns 禁用返回true,否则为false
 */
function banBackSpace(e) {
	var ev = e || window.event;//获取event对象
	//backspace键，其它键不监听
	if (ev.keyCode == "8") {
		//获取触发事件的元素
		var target = ev.target || ev.srcElement;
		//得到标签的名称
		var tagName = target.nodeName;
		//如果不是input标签或textarea标签，屏蔽backspace键
		if ((tagName == "INPUT") || (tagName == "TEXTAREA")) {
			//得到input标签的type属性
		    var inputType = target.getAttribute("type");
			//得到input标签的readonly属性，将其属性值设置为readonly代表只读，不可输入。
		    var inputReadonly = target.getAttribute("readonly");
		    //1、对可输入的单行文本框启用backspace键，readonly属性值不是readonly
			if ((tagName == "INPUT") && (inputType == "text") && (inputReadonly != "readonly")) {
		    	return false;
		    }
			//2、对可输入的密码框启用backspace键，readonly属性值不是readonly
			if ((tagName == "INPUT") && (inputType == "password") && (inputReadonly != "readonly")) {
		    	return false;
		    }
		    //3、对可输入的多行文本标签启用backspace键，readonly属性值不是readonly
		    if ((tagName == "TEXTAREA") && (inputReadonly != "readonly")) {
		    	return false;
		    }
		}		
		return true;
	}
	return false;
}
/**
 * 此方法是在文档加载完成后屏蔽浏览器上的某些按键事件，防止按下这些按键刷新整个页面
 * 只能是keydown事件，对于keypress和keyup事件在IE和谷歌浏览器上backspace键和F5键无效
 * <p>enter键是13、backspace键是8、F5键是116</p>
 */
window.onload = function() {
	//屏蔽F5刷新键和backspace键
	document.onkeydown = function(event) {
		var code = event.keyCode;
		//屏蔽F5刷新键
		if (code == 116) {
			return false;
		}
		//屏蔽backspace键
		if (banBackSpace(event)) {
			return false;
		}
	};
	//只读文本框鼠标指针不可聚焦文本框
	inputReadBlur();
};
/**
 * 让只读文本框鼠标指针不可聚焦文本框
 * @Date 2016-8-13下午11:45:22
 * @param eleObj html标签对象
 */
function inputReadBlur(eleObj) {
	var ele = eleObj ? eleObj : window.document;
	//得到所有的input标签
	var inputTags = ele.getElementsByTagName("input");
	for (var i = 0; i < inputTags.length; i++) {
		var input = inputTags[i];
		//只读文本框
		if (input.getAttribute("readonly") == "readonly") {
			//绑定聚焦事件
			input.onfocus = function() {
				this.blur();//失去焦点
			}
		}
	}
}

/**
 * 实现选中单行文本框和多行文本框指定范围的文字
 * <p>也可用于跨浏览器使光标聚焦到文本框文字的最后面，此时开始索引和结束索引的值为文本框文字的长度</p>
 * <p>input标签或者textarea标签selectionStart属性selectionEnd属性，可以用于获取选中的文本</p>
 * @Date 2016-9-6下午03:58:14
 * @param ele input标签或者textarea标签对象或者其标签的ID属性
 * @param startIndex 开始索引（包含）
 * @param endIndex 结束索引（不包含）
 */
function selectTextRange(id, startIndex, endIndex) {
	if (typeof ele === "string") {
		ele = document.getElementById(ele);
	}
	startIndex = (startIndex == undefined) ? ele.value.length : startIndex;
	endIndex = (endIndex == undefined) ? ele.value.length : endIndex;
	//火狐、谷歌、IE9及以上支持setSelectionRange()方法
	if (ele.setSelectionRange) {
		ele.focus();
		ele.setSelectionRange(startIndex, endIndex);
	} else if (ele.createTextRange) {
		//IE支持createTextRange()方法
		var range = ele.createTextRange();
		range.collapse(true);
		range.moveEnd('character', endIndex);//保持与上面方法一致，应该写在moveStart前面
		range.moveStart('character', startIndex);
		range.select();
	}
}

/**
 * 区分不同主流浏览器，获取浏览器的类型及版本号
 * @Date 2016-9-15下午6:03:35
 * @returns 返回浏览器类型（小写）和版本号。例ie9返回“ie 9”浏览器类型和版本号中间隔一个英文空格
 */
function getBrowseType() {
	//获取浏览器的用户代理字符串
	var ua = navigator.userAgent.toLowerCase();
	//考虑到IE用户最多，所以最先检测。msie不能区分IE11版本浏览器
	var arr = ua.match(/msie ([\d.]+)/);
	if (arr) {
		return "ie "+arr[1];
	}
	//检测IE11
	arr = ua.match(/trident.*rv:([\d.]+)/);
	if (arr) {
		return "ie "+arr[1];
	}
	//检测win10的edge浏览器
	//要先于谷歌浏览器和Safari浏览器检测，因为edge的浏览器用户代理包含“Chrome”和“Safari”字符串
	arr = ua.match(/edge\/([\d.]+)/);
	if (arr) {
		return "edge "+arr[1];
	}
	//检测火狐浏览器
	arr = ua.match(/firefox\/([\d.]+)/);
	if (arr) {
		return "firefox "+arr[1];
	}
	//检测谷歌浏览器--谷歌浏览器的用户代理中包含“Safari”字符串
	arr = ua.match(/chrome\/([\d.]+)/);
	if (arr) {
		return "chrome "+arr[1];
	}
	//检测opera浏览器。。。未测试
	arr = ua.match(/opera.([\d.]+)/);
	if (arr) {
		return "opera "+arr[1];
	}
	//检测safari浏览器。。。未测试
	arr = ua.match(/version\/([\d.]+).*safari/);
	if (arr) {
		return "safari "+arr[1];
	}
	//其它未知浏览器返回-1
	return "-1";
}

/**
 * 实现上传图片立即预览选择的图片功能
 * options 配置属性对象
 * 使用示例如下：
 *	new uploadImagePreview({
 *		fileId: 'test_file',
 *		imageId: "test_image",
 *		divId: 'img_preview',
 *		callback: function(a) {
 *			//alert(a);
 *		}
 *	});
 **/
var uploadImagePreview = function(options) {
	if ((options == null) || (typeof options !== "object")) {
		options = {};
	}			
	this.defaultOpts = {			
		width: 200,//图片宽度
		height: 200,//图片高度
		type: ["jpg", "jpeg", "png", "gif"],//允许图片格式
		typeErrorMsg : "只允许上传jpg、jpeg、png、gif格式的图片文件！",//文件格式上传错误时的提示信息
		fileEmptyMsg: "文件名称不能为空！",//触发了选择事件但是文本框内容为空时的提示信息
		callback: null//触发了选择事件的回调函数，默认为null。仅一个参数，代表上传文件的名称
	};
	var _self = this;
	var opts = {
		fileId: options.fileId,//文件选择文本框的ID值，必填项
		imageId: options.imageId,//显示图片的ID值，必填项
		divId: options.divId,//IE下显示图片的ID值，必填项
		width: options.width || _self.defaultOpts.width,
		height: options.height || _self.defaultOpts.height,
		type: options.type || _self.defaultOpts.type,
		typeErrorMsg: options.typeErrorMsg || _self.defaultOpts.typeErrorMsg,
		fileEmptyMsg: options.fileEmptyMsg || _self.defaultOpts.fileEmptyMsg,
		callback: options.callback
	};		
	/**
	 * 获取文件数据，实现当文件选择框选择图片时立即回显图片的功能
	 * @param fileObj 文件对象，包含文件的名称、大小、最近修改时间、mime类型，
	 **/
	this.changeShowImage = function(fileObj) {
		var url = '';
		if (window.createObjectURL) {
			url = window.createObjectURL(fileObj);
		} else if (window.URL && window.URL.createObjectURL) {
			//谷歌、火狐、IE10+支持URL对象。
			//相应的revokeObjectURL()方法来释放。浏览器会在文档退出的时候自动释放它们，
			//但是为了获得最佳性能和内存使用状况，你应该在安全的时机主动释放掉它
			url = window.URL.createObjectURL(fileObj);
		} else if (window.webkitURL && window.webkitURL.createObjectURL) {
			//webkitURL支持使用了webkit内核的浏览器
			url = window.webkitURL.createObjectURL(fileObj);
		}
		return url;
	};
	/**
	 * 根据文件名称验证图片的拓展名
	 * @param fileName 文件名或者全路径名 
	 **/
	this.validateImageExtName = function(fileName, allowExtName) {
		if (fileName) {
			//根据文件名得到文件的扩展名
			var extName = fileName.substring(fileName.lastIndexOf(".")+1);					
			for (var i = 0; i < allowExtName.length; i++) {
				if (allowExtName[i] === extName.toLowerCase()) {
					return true;
				}
			}
			return false;
		} else {
			//对null、undefined、""值提示信息
			alert(_self.fileEmptyMsg);
		}
	};
	this.bindChange = function(id, imgId, divId) {
		document.getElementById(id).onchange = function() {
			//this.files;返回这个文本框选择的所有文件对象列表		
			var isValid = _self.validateImageExtName(this.value, opts.type);
			if (isValid) {
				var imgObj = document.getElementById(imgId);
				try {
					imgObj.src = _self.changeShowImage(this.files[0]);
					imgObj.style.display = "inline-block";//确保显示图片
					imgObj.style.width = opts.width+"px";
					imgObj.style.height = opts.height+"px";
				} catch(e) {
					//IE9及以下会报错，IE通过滤镜来实现图片预览效果
					try {
						imgObj.style.display = "none";//确保隐藏图片
						var divObj = document.getElementById(divId);
						divObj.style.filter = 'progid:DXImageTransform.Microsoft.'+
							'AlphaImageLoader(sizingMethod=scale,src='+this.value+')';
						divObj.style.width = opts.width+"px";
						divObj.style.height = opts.height+"px";
					} catch(e) {
						//对于不支持的浏览器提示信息
						alert("抱歉，你的浏览器不支持选择图片预览功能，请升级浏览器或者换一个浏览器重试！");
					}
				}
				//回调函数
				if (opts.callback) {
					opts.callback(this.value);
				}
			} else {
				alert(opts.typeErrorMsg);
			}
		};
	};
	//初始化绑定change事件
	this.bindChange(opts.fileId, opts.imageId, opts.divId);
};

