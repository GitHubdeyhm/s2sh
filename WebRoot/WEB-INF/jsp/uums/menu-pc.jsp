<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/easyui-1.3.5/datagrid-detailview.js" charset="utf-8"></script>
	<title>菜单管理</title>
	<style type="text/css">
	</style>
</head>
<body class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center'">
		<div id="menu_datagrid_toolbar" class="search-toolbar clearfix">
			<div style="float:left;">
				<form id="menu_search_form" method="post">
					<label>菜单名称</label>
					<input type="text" id="menuName_search" maxlength="20" />
					<label>是否启用</label>
					<input type="text" name="enableStr" id="enableStr_search" />
					
					<a href="javascript:search();" class="easyui-linkbutton" iconCls="icon-search" plain="true">查 询</a>
					<a href="javascript:reset();" class="easyui-linkbutton" iconCls="icon-reload" plain="true">重 置</a>
				</form>
			</div>
			<div style="float:right;">
				<a onclick="showDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
				<a onclick="showDialog(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
				<a onclick="del()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			</div>
		</div>
		<table id="menu_datagrid">
			<thead>
				<tr>
					<%--<th field="ck" checkbox="true"></th>--%><!-- 显示复选框 -->
					<th field="menuName" width="100">菜单名称</th>
					<th field="menuUrl" width="100">菜单地址</th>
					<th field="menuMark" width="100">菜单标识</th>
					<th field="menuNote" width="100">菜单说明</th>
					<th field="isEnable" width="100" formatter="enableFormatter">是否启用</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<script type="text/javascript">
		var menu_datagrid;//当前表格对象
		var lastExpandIndex = null;
		$(function(){
			/**
			 * 用户数据表格
			 */
			 menu_datagrid = $('#menu_datagrid').datagrid({
				toolbar: "#menu_datagrid_toolbar",//工具栏--有助于页面布局
				url : '${ctx}/uums/menu!showList.action',
				view: detailview,
				detailFormatter: function(index, row){
					return '<div style="height:200px;"><table id="sub-grid-'+index+'"></table></div>';
				},
				onExpandRow: function(index, row){
					//第一次展开并且展开不等于当前索引
					if (lastExpandIndex != null && lastExpandIndex != index) {
						$(this).datagrid("collapseRow", lastExpandIndex);
					}
					lastExpandIndex = index;
					//加载子表格
					loadSubGrid(index, row);
				},
				onLoadSuccess : function(data) {
					//加载完成后取消所有的已选择项
					$(this).datagrid('clearSelections');
				}
			});
			/**
			 * 得到表格的分页栏
			 */
			getCurrentPager(menu_datagrid);
			
			$("#enableStr_search").combobox({
				valueField: "id",
		        textField: "text",
		        value: "-1",
		        panelHeight: "auto",
				url: "${ctx}/json/yes_no.json"
			});
		});
		
		function loadSubGrid(index, row) {
			$('#sub-grid-'+index).datagrid({
				//toolbar: "#menu_datagrid_toolbar",//工具栏--有助于页面布局
				url : '${ctx}/uums/menu!showList.action',
				columns: [[
				        {field:'menuName', title:'菜单名称', width:100},
				        {field:'menuUrl', title:'菜单地址', width:100},
				        {field:'menuMark', title:'菜单标识', width:100},
				        {field:'menuNote', title:'菜单说明', width:100},
				        {field:'isEnable', title:'是否启用', width:100}				        
				     ]
				],
				border: true,
				showHeader: false,
			    onLoadSuccess : function(data) {
					//加载完成后取消所有的已选择项
					$(this).datagrid('clearSelections');
				}
			});
		}
		
		function enableFormatter(value, row, index) {
			return value ? "是" : "否"
		}
		
		/**
		 * 点击处理弹出框
		 */
		function showDialog(index) {
			var inputUrl = "${ctx}/uums/menu!input.action";
			var title = '新增菜单', row = null;
			if (index != undefined) {
				title = '编辑菜单';
				var rows = menu_datagrid.datagrid("getSelections");
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
			//默认新增
			//新增或编辑对话框
			$('<div id="menuInput"></div>').dialog({
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
						$("#menuInput").dialog('destroy');
					}
				}],
				onClose : function() {
					$(this).dialog('destroy');
				},
				onLoad : function() {
					menu_input_form_init();
					if (row) {
						$('#menu_input_form').form('load', row);
					} else {
						$("#parentCode_input").combotree("setValue", "-1");
					}
				}
			});
			//当窗口发生变化时重新调整对话框的大小
			resizeDialog("menuInput");
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
						$("#menuInput").dialog('destroy');//销毁对话框 
						menu_datagrid.datagrid('reload');//重新加载列表数据
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
			var rows = menu_datagrid.datagrid("getSelections");
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
								menu_datagrid.datagrid('reload');//重新加载列表数据
							}
							et.showMsg(data.msg);
						}
					});
		 		}
		 	});
		}
		
		function search() {
			var enableStr = $("#enableStr_search").val();
			var menuName = $("#menuName_search").combobox("getValue");
			menu_datagrid.datagrid('load', {
				enableStr : enableStr,
				menuName: menuName
			});
		}
	</script>
</body>
</html>