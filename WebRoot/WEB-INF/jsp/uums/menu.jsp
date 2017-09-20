<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>菜单管理</title>
	<style type="text/css">
	</style>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',fit:true">
		<div id="menu_treegrid_toolbar" class="search-toolbar clearfix">
			<%--<div style="float:left;">
				<form id="menu_search_form" method="post">
					<label>菜单名称</label>
					<input type="text" id="menuName_search" maxlength="20" />
					<label>是否启用</label>
					<input type="text" name="enableStr" id="enableStr_search" />
					
					<a onclick="search_dg();" class="easyui-linkbutton" iconCls="icon-search" plain="true">查 询</a>
					<a onclick="reset();" class="easyui-linkbutton" iconCls="icon-reload" plain="true">重 置</a>
				</form>
			</div>--%>
			<div style="float:right;">
				<a onclick="showInputDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
				<a onclick="showInputDialog(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
				<a onclick="del()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			</div>
		</div>
		<table id="menu_treegrid">
			<thead>
				<tr>
					<!--<th field="radio" formatter="radioFmt"></th> 显示单选按钮 -->
					<th field="menuName" align="left" width="200">菜单名称</th>
					<th field="menuUrl" width="180">菜单地址</th>
					<th field="menuMark" width="100">菜单标识</th>
					<th field="menuOrder" width="100" sortable="true">排序号</th>
					<th field="menuNote" width="180">菜单说明</th>
					<th field="isEnable" width="70" formatter="enableFmt">是否启用</th>
				</tr>
			</thead>
		</table>
	</div>
	
<script type="text/javascript">
	var menu_treegrid;
	$(function(){
		/**
		 * 菜单树形数据表格
		 */
		 menu_treegrid = $('#menu_treegrid').treegrid({
			toolbar: "#menu_treegrid_toolbar",//工具栏--有助于页面布局
			url : '${ctx}/uums/menu!showList.action',
			idField: 'menuCode',//树的值所在列
			treeField: 'menuName',//树形表格的树列字段名称
			singleSelect: true,//单选
			sortName: "menuOrder",//根据角色排序号排序
			sortOrder: "asc",//排序方式，升序
			onClickRow: function(row) {
				//var index = getRowIndex(row);
				//alert(index);
				//menu_treegrid.find("input[type='radio']").eq(index)[0].checked = "checked";
			},
			onLoadSuccess : function(data) {
				//加载完成后取消所有的已选择项
				$(this).treegrid('collapseAll');
				//console.log($(this).treegrid("options"));
			}
		});
		
		//是否启用下拉框
		$("#enableStr_search").combobox({
	        value: "-1",//设置下拉框的默认值
	        panelHeight: "auto",
			url: "${ctx}/json/yes_no.json"
		});
	});
	
	//是否启用formatter
	function enableFmt(value, row, index) {
		return value ? "是" : "否"
	}
	
	//单选按钮formatter
	function radioFmt(value, row, index) {
		return '<input type="radio" name="grid_radio" />';
	}
	
	/**
	 * 点击处理弹出框
	 */
	function showInputDialog(index) {
		var inputUrl = "${ctx}/uums/menu!input.action";
		var title = '新增菜单', row = null;
		if (index != undefined) {
			title = '编辑菜单';
			var rows = menu_treegrid.treegrid("getSelections");
			var len = (rows == null) ? 0 : rows.length;
			if (len < 1) {
				//如果没有任何一行选中，则弹出提示框 
				et.showMsg(Constant.emptyTipMsg);
				return;
			}
			if (len > 1) {
				//如果编辑时选中了多条记录，则默认选择编辑第一行并弹出提示框 
				et.showMsg(Constant.editFirstRow);
			}
			row = rows[0];
		}
		//新增或编辑对话框
		$('<div id="dialogInput"></div>').dialog({
			title : title,
			width : 600,
			height : 450,
			modal : true, //模式窗口：窗口背景不可操作  
			resizable : false, //可缩放边框大小  
			href : inputUrl,
			buttons : [{
				text : '确定',
				handler : function() {
					$("#menu_input_form").submit();//提交表单
				}
			}, {
				text : '取消',
				handler : function() {
					$("#dialogInput").dialog('destroy');
				}
			}],
			onClose : function() {
				$(this).dialog('destroy');
			},
			onLoad : function() {
				menu_input_form_init();
				var val = "-1";
				if (row) {
					$('#menu_input_form').form('load', row);
					//设置父编码的值
					var menuCode = row.menuCode;
					if (menuCode.length > 3) {
						val = menuCode.substring(0, menuCode.length-3);
					}
					//设置是否启用单选按钮
					if (row.isEnable) {
						$("#enableStr_input1").attr("checked", true);
					} else {
						$("#enableStr_input0").attr("checked", true);
					}
					//编辑时禁用父级菜单
					$("#parentCode_input").combotree("disable");
				}
				$("#parentCode_input").combotree("setValue", val);
			}
		});
		//当窗口发生变化时重新调整对话框的大小
		resizeDialog("dialogInput");
	}
	/**
	 * 点击处理弹出框
	 */
	function menu_input_form_init() {
		$('#menu_input_form').form({
			url: "${ctx}/uums/menu!save.action",
			onSubmit: function(param) {
				var isValid = $(this).form('validate');
				//表单验证通过启用进度条
				if (isValid) {
					et.progress();
				}
				return isValid;
			},
			success: function(data) {
				//表单验证通过且返回提交结果
				$.messager.progress('close');
				var json = $.parseJSON(data);
				//1：成功，0：失败
				if (json.resultCode == 1) {
					$("#dialogInput").dialog('destroy');//销毁对话框 
					menu_treegrid.treegrid('reload');//重新加载列表数据
				}
				et.showMsg(json.msg);
			}
		});
	}
	/**
	 * 批量删除
	 */
	function del() {
		//得到所有选中的行
		var rows = menu_treegrid.datagrid("getSelections");
		if (!rows || rows.length < 1) {
			//如果没有任何一行选中，则弹出提示框 
			et.showMsg(Constant.emptyTipMsg);
			return;
		}
		$.messager.confirm('删除菜单', '您确定要删除所选菜单？', function(r) {
	 		if(r) {
	 			var ids = [];
	 			for (var i = 0; i < rows.length; i++) {
	 				ids.push(rows[i].menuCode);
	 			}
				$.ajax({
					type: "POST",
					url: "${ctx}/uums/menu!delete.action",
					data: {id: ids.join(",")},
					dataType: "json",
					success: function(data) {
						//1：成功，0：失败
						if (data.resultCode == 1) {
							menu_treegrid.treegrid('reload');//重新加载列表数据
						}
						et.showMsg(data.msg);
					}
				});
	 		}
	 	});
	}
	//查询
	function search_dg() {
		var menuName = $("#menuName_search").val();
		var enableStr = $("#enableStr_search").combobox("getValue");
		menu_treegrid.treegrid('load', {
			enableStr : enableStr,
			menuName: menuName
		});
	}
	//重置
	function reset() {
		$("#menuName_search").val("").focus();
		$("#enableStr_search").combobox("setValue", "-1");
	}
</script>
</body>
</html>