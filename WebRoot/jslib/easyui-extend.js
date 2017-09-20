/**
 * 扩展easyui的js文件
 * easyui的表格加载数据比较慢，如果一次加载两百条数据的话数据表格渲染很慢的。
 */
var et = $.extend({}, et);
/**
 * 弹出窗口 提示信息
 * @param msg 消息内容
 */
et.showMsg = function(msg) {
    $.messager.show({
        title: '提示信息',
        msg: msg,
        timeout: 3000,//设置为0不会自动消失
        showType: 'slide' //弹出框的显示方式，允许的值有：null,slide,fade,show
    });
};
/**
 * 弹出窗口 提示信息，居中显示，默认为info
 * @param msg：提示信息的内容
 * @param type：提示信息类型，允许的值有：info、error、question、warning，该值为空代表不显示图标
 */
et.showAlertMsg = function(msg, type) {
	if (!type) {
		type = 'info';
	}
	$.messager.alert('提示信息', msg, type);
};
/**
 * 进度条提示信息
 */
et.progress = function(text) {
	if (!text) text = '数据处理中，请稍后....';
	$.messager.progress({
		title : '提示信息',
		text : text
	});
};

/**
 * 设置easyui的面板或弹出对话框移动时不会超出所在窗口的宽度和高度。
 * 当对话框向上移动时不会超出窗口，向下移动时保持对话框标题可见便于拖动，左右设置最小和最大左边距
 * <p>1、对话框显示时会触发onMove事件</p>
 * <p>2、窗口标题的高度大约为24px</p>
 * @param left 移动时的左边距
 * @param top 移动时的上边距
 */
var easyuiPanelMove = function(left, top) {
	//24为窗口标题的高度，60为对话框可见的最小宽度
	var titleHeight = 24, panelMinWidth = 60;
	var moveLeft = left;
	var moveTop = (top < 0) ? 0 : top;
	var $panel = $(this).parent();
	//得到面板和窗口的宽度和高度
	var width = $panel.width()+12, height = $panel.height()+12;
	var winW = $(window).width(), winH = $(window).height();
	//最小左边距
	if ((left + width) < panelMinWidth) {
		moveLeft = panelMinWidth - width;
	}
	//最大左边距
	if ((winW - left) < panelMinWidth) {
		moveLeft = winW - panelMinWidth;
	}
	//最大顶边距
	if ((top + titleHeight) > winH) {//24为窗口标题的高度
		var tempH = winH - titleHeight;
		moveTop = tempH < 0 ? 0 : tempH;//保证不超出上边界（窗口标题title可见）
	}
	//重新调整面板位置，让弹出框不超出上边距
	$panel.css({
		left : moveLeft,
		top : moveTop
	});
};
$.fn.dialog.defaults.onMove = easyuiPanelMove;
$.fn.window.defaults.onMove = easyuiPanelMove;
//$.fn.panel.defaults.onMove = easyuiPanelMove;

/**
 * 扩展easyui的树型控件，使其支持平滑数据格式，即有parentId字段
 * 使用时需指明三个字段：idField、textField、parentField
 * @param data 加载成功时数据
 */
$.fn.tree.defaults.loadFilter = function(data, parent) {			
	//得到树的属性值
	var opt = $(this).data().tree.options;
	//存在父字段
	if (opt.parentField) {
		var id = opt.idField || 'id';
		var text = opt.textField || 'text';
		var parentId = opt.parentField;//父字段
		var i, len = data.length, treeData = [], tmpMap = [];
		for (i = 0; i < len; i++) {					
			tmpMap[data[i][id]] = data[i];//tempMap对象的属性以ID为标识符，其值为对象data[i]
		}				
		for (i = 0; i < len; i++) {
			//data[i]的名称必须是text标识符
			if (text != 'text') {
				data[i].text = data[i][text];
			}
			//得到根节点
			if (tmpMap[data[i][parentId]] == undefined || data[i][id] == data[i][parentId]) {						
				treeData.push(data[i]);
			} else {
				if (!tmpMap[data[i][parentId]].children) {
					tmpMap[data[i][parentId]].children = [];//不存在就创建子节点数组
				}						
				tmpMap[data[i][parentId]].children.push(data[i]);//添加子节点
			}
		}
		return treeData;
	}
	return data;
};

/*****设置下拉树的树型数据支持平滑数据格式*****/
$.fn.combotree.defaults.loadFilter = $.fn.tree.defaults.loadFilter;

/**重置easyui表格和面板的默认加载时的提示信息*/
$.fn.panel.defaults.loadingMessage = '加载中，请稍后...';
$.fn.datagrid.defaults.loadMsg = '加载中，请稍后...';
$.fn.treegrid.defaults.loadMsg = '加载中，请稍后...';

/**
 * 封装easyui的日期控件，实现加载月份控件效果
 * @Date 2016-7-19下午05:46:45 
 * @param id html标签的id属性值，必须参数
 * @param width 月份控件的宽度，可选参数，默认值为170
 * @param height 月份控件的高度，可选参数，默认值为24
 */
function initMonthBox(id, width, height) {
	var boxObj = $("#"+id);
	//存在改ID属性
	if (boxObj.length > 0) {
		var w = (!width || width <= 0) ? 170 : width;
		var h = (!height || height <= 0) ? 24 : height;
		boxObj.datebox({
			width: w,
			height: h,
			editable: false,
			currentText: '当月',
			//日期格式化：yyyy-MM
			formatter: function(dateObj) {
				var y = dateObj.getFullYear();
				var m = dateObj.getMonth()+1;
				if (m < 10) {
					m = '0' + m;
				}
				return y+'-'+m;
			},
			parser: function(dateStr) {
				var t = Date.parse(dateStr);
				if (isNaN(t)){
					return new Date();
				} else {
					return new Date(t);
				}
			},
			onShowPanel: function() {
				var datePanel = $(this).datebox("panel");
				datePanel.find(".calendar-title").children("span").trigger('click');
				//年份文本框不可输入
				var yearInput = datePanel.find("input.calendar-menu-year");
				if (!yearInput.attr("disabled")) {
					yearInput.attr("disabled", "disabled");
				}
				datePanel.find("td.calendar-menu-month").off().click(function(e){
					var month = $(this).attr("abbr");//月份值
					if (month < 10) {
						month = "0" + month;
					}
					var val =  yearInput.val()+"-"+month;
					boxObj.datebox("setValue", val);
					boxObj.datebox("hidePanel");
				});
			}
		});
	}
}

/**
 * 以下实现覆盖easyui下拉列表的一些默认值。实现统一展示样式和减少实际页面代码的编写
 */
//设置下拉框默认为不可输入
$.fn.combobox.defaults.editable = false;
$.fn.combotree.defaults.editable = false;
$.fn.datebox.defaults.editable = false;
$.fn.datetimebox.defaults.editable = false;

/**
 * 以下实现覆盖easyui表格组件的一些默认值。实现统一展示样式和减少实际页面代码的编写
 * 说明：1、实际编写时可以加上属性名覆盖这里的默认值
 *     2、实际编写时只需写url和toolbar属性（可选），例：
 * 		user_datagrid = $("#user_datagrid").datagrid({
 *			toolbar: "#user_datagrid_toolbar",//可选
 *			url: '${ctx}/shipport/shipPort!showList.action'
 *		});
 */
//设置为true将交替显示行背景，默认为false
$.fn.datagrid.defaults.striped = true;
//允许表格列自动缩放，以适应父容器的宽度，不会出现横向滚动条，默认为false
$.fn.datagrid.defaults.fitColumns = true;

//不允许远程排序，默认为true
$.fn.datagrid.defaults.remoteSort = false;
//设置自动适应父容器的宽度和高度，默认为false
$.fn.datagrid.defaults.fit = true;

//设置表格默认显示分页栏，因为基本上表格都存在分页。默认为false
$.fn.datagrid.defaults.pagination = true;
//不显示表格的边框线，默认为true
$.fn.datagrid.defaults.border = false;

//设置为true数据将不会换行显示，数据超出列长度时截取，默认值为true
//$.fn.datagrid.defaults.nowrap = true;
//是否只允许选择单行，默认为false
//$.fn.datagrid.defaults.singleSelect = false;

//设置easyui表格的分页栏和每页显示的记录数
$.fn.datagrid.defaults.pageSize = 15;
$.fn.datagrid.defaults.pageList = [ 15, 25, 40, 50 ];

//重写easyui表格的加载成功事件
$.fn.datagrid.defaults.onLoadSuccess = function(data) {
	initDatagrid(this);
};
/**
 * 当easyui表格加载成功时初始化表格一些配置
 * @Date 2017-2-20下午9:42:05
 * @param datagridObj 表格对象
 */
function initDatagrid(datagridObj) {
	//取消所有的已选择项
	$(datagridObj).datagrid('clearSelections');
	//添加悬浮提示
	addDatagridToolTip(datagridObj);
	
	//以下实现表格自适应滚动条，当没有出现滚动条时隐藏表格的最右边多余部分
	var dgPanel = $(datagridObj).datagrid("getPanel");
	var dgBody = dgPanel.find(".datagrid-view2 .datagrid-body");
	//表格没有出现滚动条
	if (dgBody[0].scrollHeight <= dgBody.height()) {
		dgPanel.find(".datagrid-view2 table").width(dgBody.width()+1);//加1隐藏边框线
	} else {
		dgPanel.find(".datagrid-view2 table").width("auto");
	}
}

/**
 * 以下实现覆盖easyui分页控件的一些默认值。实现统一展示样式和减少实际页面代码的编写
 */
//设置初始化总记录数为0，默认为1
$.fn.pagination.defaults.total = 0;
//设置easyui分页栏和每页显示的记录数
$.fn.pagination.defaults.pageSize = 15;
$.fn.pagination.defaults.pageList = [ 15, 25, 40, 50 ];
//设置分页栏信息
$.fn.pagination.defaults.displayMsg = '当前显示{from} - {to}条记录 共{total}条记录';
$.fn.pagination.defaults.beforePageText = '第';
$.fn.pagination.defaults.afterPageText = '页 共{pages}页',


/**
 * 以下实现覆盖easyui树形表格的一些默认值。实现统一展示样式和减少实际页面代码的编写
 */
//设置为true将交替显示行背景，默认为false
$.fn.treegrid.defaults.striped = true;
//允许表格列自动缩放，以适应父容器的宽度，不会出现横向滚动条，默认为false
$.fn.treegrid.defaults.fitColumns = true;

//不允许远程排序，默认为true
$.fn.treegrid.defaults.remoteSort = false;
//设置自动适应父容器的宽度和高度，默认为false
$.fn.treegrid.defaults.fit = true;
//不显示表格的边框线，默认为true
$.fn.treegrid.defaults.border = false;



//为表格的所有列都增加排序功能，不用每列都加sortable = true：
/*var columns = $("#regist_datagrid").datagrid("options").columns[0];//表头
for (i = 0; i < columns.length; i++) {
	columns[i].sortable = true;
}*/

/**
 * 设置easyui的日期控件的年份不可输入，防止日期格式不符合规范
 */
$.fn.datebox.defaults.onShowPanel = function() {
	//找到日期控件年份输入框
	var yearInput = $(this).datebox("panel").find("input.calendar-menu-year");
	if (!yearInput.attr("disabled")) {
		yearInput.attr("disabled", "disabled");
	}
};
$.fn.datetimebox.defaults.onShowPanel = function() {
	//找到日期控件年份输入框
	var yearInput = $(this).datetimebox("panel").find("input.calendar-menu-year");
	if (!yearInput.attr("disabled")) {
		yearInput.attr("disabled", "disabled");
	}
};

/**
 * 设置树控件和下拉树控件的展开和关闭事件的默认操作，使展开和关闭节点时背景颜色完全覆盖选中的节点
 * 1、树控件实现此效果需要在树的ul标签设置一个父标签，然后设置父标签的样式为overflow:auto;
 * @Date 2016-11-21上午11:40:17 
 * @param node 展开或关闭时的节点对象
 */
$.fn.tree.defaults.onExpand = function(node) {
	$(this).width(this.scrollWidth);
};
$.fn.tree.defaults.onCollapse = function(node) {
	$(this).width("auto");
};
$.fn.combotree.defaults.onExpand = function(node) {
	if ($(this).width() > 0) {
		$(this).width(this.scrollWidth);
	}
};
$.fn.combotree.defaults.onCollapse = function(node) {
	if ($(this).width() > 0) {
		$(this).width("auto");
	}
};

/**
 * 调整easyui的日期下拉选择框按钮的功能：
 * 1、为日期下拉选择框组件的增加清空按钮，让用户可以清空日期文本框的值
 * 2、重新调整easyui默认的今天按钮，使点击今天按钮触发onSelect事件
 * @Date 2016-11-7 下午10:32:28
 */
var datebox_buttons = $.extend([], $.fn.datebox.defaults.buttons);
var datetimebox_buttons = $.extend([], $.fn.datetimebox.defaults.buttons);
//重写今天按钮实现的功能
datebox_buttons.splice(0, 1, {
	text: '今天',
	handler: function(target) {
		var dateObj = new Date();
		var opts = $(target).datebox("options");
		$(target).datebox("setValue", opts.formatter(dateObj));
		$(target).datebox("hidePanel");
		opts.onSelect.call(target, dateObj);
	}
});
//增加清空按钮
datebox_buttons.push({
	text: '清空',
	handler: function(target){
		$(target).datebox("setValue", "");
		$(target).datebox("hidePanel");
	}
});
//重写今天按钮实现的功能
datetimebox_buttons.splice(0, 1, {
	text: '今天',
	handler: function(target) {
		var dateObj = new Date();
		var opts = $(target).datebox("options");
		var ymd = opts.formatter(dateObj);
		//得到时间分隔符:
		var timeSep = opts.timeSeparator;
		var hour = dateObj.getHours();//小时
		if (hour < 10) {
			hour = "0" + hour;
		}
		var minutes = dateObj.getMinutes();//分钟
		if (minutes < 10) {
			minutes = "0" + minutes;
		}
		var time = ymd+" "+hour+timeSep+minutes;
		//显示秒
		if (opts.showSeconds) {
			var seconds = dateObj.getSeconds();//秒
			if (seconds < 10) {
				seconds = "0" + seconds;
			}
			time = time+timeSep+seconds;
		}
		$(target).datebox("setValue", time);
		$(target).datebox("hidePanel");
		opts.onSelect.call(target, dateObj);
	}
});
//增加清空按钮
datetimebox_buttons.push({
	text: '清空',
	handler: function(target){
		$(target).datetimebox("setValue", "");
		$(target).datetimebox("hidePanel");
	}
});
$.fn.datebox.defaults.buttons = datebox_buttons;
$.fn.datetimebox.defaults.buttons = datetimebox_buttons;


/**
 * easyui各组件加载错误时统一提示的信息。
 * 相当于用ajax加载错误时发生的“error”事件
 */
var easyuiLoadError = function(XMLHttpRequest) {
	$.messager.progress('close');
//	var data = XMLHttpRequest.responseText;
	//防止火狐下快速点击菜单出现提示框的信息
	if (XMLHttpRequest.status != 404) {
		et.showAlertMsg('数据加载出错，请稍后重试！', 'error');
	}
	/*if (!data) {
		et.showAlertMsg('抱歉，数据加载出错，请稍后重试。', 'error');
	} else {
		et.showAlertMsg(data, 'error');
	}*/
};
$.fn.datagrid.defaults.onLoadError = easyuiLoadError;
$.fn.treegrid.defaults.onLoadError = easyuiLoadError;
$.fn.tree.defaults.onLoadError = easyuiLoadError;
$.fn.combogrid.defaults.onLoadError = easyuiLoadError;
$.fn.combobox.defaults.onLoadError = easyuiLoadError;
$.fn.combotree.defaults.onLoadError = easyuiLoadError;
$.fn.form.defaults.onLoadError = easyuiLoadError;


/**重置easyui验证框的提示位置，默认值为：right，允许的值有：top、left、right、bottom*/
$.fn.validatebox.defaults.tipPosition = 'bottom';
/**重置easyui时间选择框的提示位置，默认值为：right，允许的值有：top、left、right、bottom*/
$.fn.datebox.defaults.tipPosition = 'bottom';
$.fn.datetimebox.defaults.tipPosition = 'bottom';


/**
 * 重置easyui验证文本框提示信息的样式
 * 文本框应放在一个form标签里面
 */
/*var resetValidateBoxStyle = function () {
	$("form").find('.validatebox-text').validatebox({
		//得到悬浮提示框
		tipOptions: {	
			onShow: function() {
				$(this).tooltip('tip').css({
					color: '#000',//提示框颜色
					borderColor: '#8F9FAF',//边框颜色
					backgroundColor: '#CBDAE1'//背景颜色
				});
			},
			onHide: function() {
				if (!$(this).tooltip('options').prompt){
					$(this).tooltip('destroy');
				}
			}
		}
	});
};*/

/**
 * 修改easyui的footer视图
 * @param container：代表footer的容器。编写的所有html标签是该标签的子元素
 */ 
/*var footerView = $.extend({}, $.fn.datagrid.defaults.view, {
	renderFooter: function(target, container, frozen) {
		//范例
		var footerHtml = '<div style="text-align:left;margin-bottom:6px;"><img src="'+ctx+'/subei/img/state_ok.png" />'+
				'<span style="margin:0px 8px 0px 6px;display:inline-block;">正常状态</span>'+
				'<img src="'+ctx+'/subei/img/state_error.png" /><span style="margin-left:6px;">故障状态</span></div>'
		$(container).html(footerHtml);
	}
});*/

/**
 * 根据当前表格对象和索引得到当前正在编辑的行，用于单行的修改和删除
 * @param currentDatagrid ：数据表格对象
 * @param index ：当前行的索引，从0开始
 * @returns 当前编辑的行对象row
 */
var getCurrentRow = function(currentDatagrid, index) {
	//1.取消全选
	currentDatagrid.datagrid('unselectAll');
	//2.将当前点击编辑的这一行选中 保证只对这一行进行操作 
	currentDatagrid.datagrid('selectRow', index);
	//3.获取这一行内容 
	var row = currentDatagrid.datagrid('getSelected');
	return row;
};
/**
 * 根据当前表格对象得到当前表格的分页栏
 * @Date 2016-7-17上午12:33:12
 * @param currentDatagrid 数据表格对象，必须参数
 * @param isMiniPager 布尔类型，可选参数，为true代表是缩小版的分页栏。缩小的分页栏只显示页码，不
 * 			会显示displayMsg包含的信息。适用于不够宽的页面。
 * @param pageSize 数字类型，可选参数。分页栏默认显示的每页记录数的大小，默认为15
 * @param pageList 数组，可选参数。分页栏下拉选择可以显示的记录数，默认为[ 15, 25, 40, 50 ]
 * @returns 带有悬浮提示信息的分页栏
 */
var getCurrentPager = function(currentDatagrid, isMiniPager, pageSize, pageList) {
	//得到分页对象
	var p = currentDatagrid.datagrid('getPager');
	//该表格没有分页栏
	if (p.length == 0) {
		return;
	}
	//分页栏提示信息
	var displayMsg = '当前显示{from} - {to}条记录   共{total}条记录';
	//缩小版的不显示提示信息
	if (isMiniPager) {
		displayMsg = '';
	}
	var psize = parseInt(pageSize);
	if (isNaN(psize)) {
		psize = 15;
	}
	var plist = [ 15, 25, 40, 50 ];
	if (pageList) {
		plist = pageList;
	}
	//数字页码分页--1.3.5版本及以上
	/*p.pagination({
		total: 0,//总记录数
		layout: ['list', 'sep', 'first', 'prev', 'links', 'next', 'last', 'sep', 'refresh'],
	    pageSize: psize,//每页显示的记录条数，默认为10  
		pageList: plist,//可以设置每页记录条数的列表  
		displayMsg: displayMsg
	});*/
	//可输入文本框分页
	p.pagination({
		pageSize : psize,//每页显示的记录条数，默认为10
		pageList : plist,//可以设置每页记录条数的列表  
		total: 0,
		beforePageText : '第',//页数文本框前显示的汉字  
		afterPageText : '页    共{pages}页',
		displayMsg : displayMsg
	});
	/**
	 * 分页栏悬浮提示信息--不适用easyui-1.4
	 */
	p.find("a.l-btn").tooltip({
		position: "top",//悬浮提示信息的位置，允许的值有：top、left、right、bottom
		content: function() {
			var tipMsg = "";
			var cc = $(this).find('span.l-btn-empty').attr('class').split(' ');
			var tip = cc[1].split('-')[1];
			if (tip == "first") {
				tipMsg = "首页";
			} else if (tip == "prev") {
				tipMsg = "上一页";
			} else if (tip == "next") {
				tipMsg = "下一页";
			} else if (tip == "last") {
				tipMsg = "尾页";
			} else if (tip == "load") {
				tipMsg = "刷新"
			}
			return tipMsg;
		}
	});
};

/**
 * 为easyui表格整体添加悬浮框提示，当文字超出表格列的宽度时自动显示悬浮宽
 * <p>对于表格某列使用了创建了标签的formatter不生成悬浮框。</p>
 * @Date 2016-8-29下午05:59:09 
 * @param currentDatagird 创建表格的table对象
 */
function addDatagridToolTip(currentDatagird) {
	if (!currentDatagird) {
		return;
	}
	//得到easyui表格的面板对象
	var dgPanel = $(currentDatagird).datagrid("getPanel");
	//正常情况下easyui解析出的表格内容放在td标签的第一级子节点div标签
	dgPanel.find(".datagrid-btable td > div:not(:hidden)").each(function(index){
		var curObj = $(this);
		//1、对于表格某列使用了创建了标签的formatter不生成悬浮框。
		if (!isHasChild(curObj)) {
			var totalLength = this.scrollWidth;
			var curObjwidth = this.clientWidth;
			//2、实际内容的长度比当前对象的长度大，3、实际长度不能太小
			if ((totalLength > curObjwidth) && (totalLength > 10)) {
				var text = curObj.text();//获取表格每列的内容
				curObj.addClass("text-over");//添加文本溢出样式-base.css
				var pos = "bottom";//定义悬浮框提示的位置
				//设置悬浮框的宽度--初始化宽度为200
				var tipWidth = 200;
				if (totalLength <= tipWidth) {
					tipWidth = totalLength;
				} else {
					tipWidth = curObjwidth < tipWidth ? tipWidth : curObjwidth;//宽度最小为200
				}
				curObj.tooltip({
					position: pos,//提示位置
					content: '<div style="width:'+tipWidth+'px;white-space:normal;display:block;word-wrap:break-word;">'+text+'</div>',//提示内容
					trackMouse: false,//不随鼠标移动
					onShow: function(e) {
						//使鼠标可以停留在悬浮框上
						curObj.tooltip("tip").off().on({
							mouseenter: function() {
								curObj.tooltip("show");
							},
							mouseleave: function() {
								curObj.tooltip("hide");
							},
						});
					},
					onHide: function(e) {
					}
				});
			}
		}
	});
	/**
	 * 判断一个标签下是否还有子节点，有子节点返回true，否则返回false
	 * <p>说明：此方法配合easyui表格生成悬浮提示框使用，对于表格某列使用了创建了标签的formatter不生成悬浮框。
	 * 如果formatter里需使用标签应该使用下列标签span、div、a、img、input</p>
	 * @Date 2016-8-30上午11:16:11 
	 * @param jqObj jquery对象
	 * @returns 有子节点返回true，否则返回false
	 */
	function isHasChild(jqObj) {
		if (jqObj.children("span").length > 0) {
			return true;
		}
		if (jqObj.children("div").length > 0) {
			return true;
		}
		if (jqObj.children("a").length > 0) {
			return true;
		}
		if (jqObj.children("img").length > 0) {
			return true;
		}
		if (jqObj.children("input").length > 0) {
			return true;
		}
		return false;
	}
}

/**
 * 改变jQuery的AJAX默认属性和方法
 * @Date 2016-9-8上午11:04:01
 */
$.ajaxSetup({
	type: "POST",
	/**
	 * 设置全局complete函数使ajax请求当httpSession失效时跳转到登陆页面
	 * 1、当ajax请求完成时触发complete.成功和错误都会触发complete方法。且在success或error方法后调用
	 * 2、对于不需要跳转的ajax请求可以重写complete函数覆盖这里的实现。
	 */
	complete : function(xhr, textStatus) {
		var sessionInvalid = xhr.getResponseHeader("sessionInvalid");
		if (sessionInvalid === "sessionInvalid") {
			top.location.href ="/S2SHFrame/login.jsp";
		}
	},
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		$.messager.progress('close');
		var data = XMLHttpRequest.responseText;
		if (!data) {
			if (XMLHttpRequest.status != 404) {
				et.showAlertMsg('数据加载出错，请稍后重试！', 'error');
			}
		} else {
			et.showAlertMsg(data, 'error');
		}
	}
});

/**
 * 当窗口大小发生变化调整对话框的大小，使对话窗口居中页面显示？？？？
 * @param id 对话框div对象ID
 * @param width 对话框的宽度，默认为500
 * @param height 对话框的高度，默认为480？？？？？
 */
var resizeDialog = function(id, width, height) {
	$(window).resize(function(){
		var $dialog = $("#"+id);
		//存在该ID
		if ($dialog.length > 0) {
			//设置对话框的初始化宽度和高度，默认宽度为500，高度为480
			var initW = (!width || width < 0) ? 500 : width;
			var initH = (!height || height < 0) ? 480 : height;
			//窗口的宽度和高度
			var h = $(this).height();
  			var w = $(this).width();
  			//设置窗口变化后的窗口的宽度和高度
  			var nowW = (w > initW) ? initW : w;
  			var nowH = (h > initH) ? initH : h;
  			//设置窗口的位置，居中显示
  			var left = (w < initW) ? 0 : ((w - nowW) / 2);
  			var top = (h < initH) ? 0 : ((h - nowH) / 2);
  			$dialog.dialog("resize", {
				left: left,
				top: top,
				width: nowW,
				height: nowH
			});
		}
	});
};