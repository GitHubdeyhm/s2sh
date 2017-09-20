/**
 * 此js文件实现通过JavaScript的方式对cookie的增删改查的操作常用方法封装。
 * @Date 2016-8-13下午6:11:08
 */
var Cookie = function() {
	
};

/**
 * 检测客户端浏览器是否开启了对cookie的支持
 * @Date 2016-8-13下午6:11:08
 * @returns 如果该客户端浏览器支持则返回true，否则返回false
 */
Cookie.enabled = function() {
	//多数浏览器支持通过navigator.cookieEnabled属性来检测cookie是否可用
	if (navigator.cookieEnabled != undefined) {
		return navigator.cookieEnabled;
	}
	//将检查的结果保存到cache字段中
	if (Cookie.enabled.cache != undefined) {
		return Cookie.enabled.cache;
	}
	//尝试去写入一个cookie
	document.cookie = "testKey=testValue; max-age=1000";
	//得到cookie
	var cookieStr = document.cookie;
	//尝试读取刚写入的cookie，如果能够读取则代表客户端浏览器支持，否则为不支持cookie
	if (cookieStr.indexOf("testKey=testValue") == -1) {
		Cookie.enabled.cache = false;
	} else {
		//支持cookie--删除刚写入的cookie
		document.cookie = "testKey=testValue; max-age=0";
		Cookie.enabled.cache = true;
	}
	return Cookie.enabled.cache;
};

function addCookie(key, name) {
	var cookieStr = document.cookie;
	if (cookieStr) {
		var arr = cookieStr.split("; ");
		for (var i = 0; i < arr.length; i++) {
			
		}
	}
	document.cookie = key+"="+name+"; max-age=3600";
}