<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>按钮管理</title>
</head>
<body class="easyui-layout">
	<div data-options="region:'west'" style="width:200px;">
		<div class="easyui-tabs" border="false" fit="true">
			<div id="menu_tab" title="菜单树">
				<ul id="menu_search"></ul>
			</div>
		</div>
	</div>
	<div data-options="region:'center'">
		<div id="button_datagrid_toolbar" class="search-toolbar clearfix">
			<div style="float:right;">
				<a onclick="showInputDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
				<a onclick="showInputDialog(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
				<a onclick="del()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			</div>
		</div>
		<table id="button_datagrid">
			<thead>
				<tr>
					<th field="ck" checkbox="true"></th><!-- 显示复选框 -->
					<th field="buttonName" width="100">按钮名称</th>
					<th field="buttonMark" width="100">按钮标识</th>
					<%--<th field="buttonOrder" width="100">排序</th>--%>
					<th field="buttonNote" width="100">按钮说明</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<script type="text/javascript">
		var button_datagrid;
		
		$(function(){
			/**
			 * 菜单按钮数据表格
			 */
			 button_datagrid = $('#button_datagrid').datagrid({
				toolbar: "#button_datagrid_toolbar",//工具栏--有助于页面布局
				url : '${ctx}/uums/button!showList.action',
				pagination: false,
			});
			/**
			 * 菜单树
			 */
			$("#menu_search").tree({
				idField: "id",
				textField: "text",
				parentField: "parentId",
				url: "${ctx}/uums/menu!showCodeTree.action",
		        onClick: function(node) {
		        	search();
		        },
				onLoadSuccess: function(data) {
					var root = $(this).tree("getRoot");//得到根节点
					if (root) {
						$(this).tree("collapseAll");//关闭所有节点
						$(this).tree("expand", root.target);//展开根节点--得到第一级子节点
					}
				}
			});
		});
		/**
		 * 点击处理弹出框
		 */
		function showInputDialog(index) {
			var inputUrl = "${ctx}/uums/button!input.action";
			var title = '新增按钮', row = null;
			//菜单树选中节点
			var selectedNode = $("#menu_search").tree("getSelected");
			if (index != undefined) {
				title = '编辑按钮';
				var rows = button_datagrid.datagrid("getSelections");
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
			} else {
				//新增时需选中某个菜单
				if (!selectedNode || selectedNode.id == "-1") {
					et.showMsg("请选择新增按钮所属的菜单");
					return;
				}
			}
			//默认新增
			//新增或编辑对话框
			$('<div id="dialogInput"></div>').dialog({
				title : title,
				width : 600,
				height : 350,
				modal : true, //模式窗口：窗口背景不可操作  
				resizable : false, //可缩放边框大小  
				href : inputUrl,
				buttons : [{
					text : '确定',
					handler : function() {
						$("#button_input_form").submit();//提交表单
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
					button_input_form_init();
					if (row) {
						$('#button_input_form').form('load', row);
					}
					$("#menuCode_input").val(selectedNode.id);
					$("#menuName_input").val(selectedNode.text);
				}
			});
			//当窗口发生变化时重新调整对话框的大小
			resizeDialog("dialogInput");
		}
		/**
		 * 点击处理弹出框
		 */
		function button_input_form_init() {
			$('#button_input_form').form({
				url: "${ctx}/uums/button!save.action",
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
						button_datagrid.datagrid('reload');//重新加载列表数据
					}
					et.showMsg(json.msg);
				}
			});
		}
		/**
		 * 查询
		 */
		function search() {
			var menuCode = "-1";
			var selectedNode = $("#menu_search").tree("getSelected");
			if (selectedNode) {
				menuCode = selectedNode.id;
			}
			button_datagrid.datagrid("load", {
				menuCode: menuCode
			});
		}
		/**
		 * 批量删除
		 */
		function del() {
			//得到所有选中的行
			var rows = button_datagrid.datagrid("getSelections");
			if (!rows || rows.length < 1) {
				//如果没有任何一行选中，则弹出提示框 
				et.showMsg(Constant.emptyTipMsg);
				return;
			}
			$.messager.confirm('删除按钮', '您确定要删除所选按钮？', function(r) {
		 		if(r) {
		 			var ids = [];
		 			for (var i = 0; i < rows.length; i++) {
		 				ids.push(rows[i].id);
		 			}
					$.ajax({
						type: "POST",
						url: "${ctx}/uums/button!delete.action",
						data: {id: ids.join(",")},
						dataType: "json",
						success: function(data) {
							//1：成功，0：失败
							if (data.resultCode == 1) {
								button_datagrid.datagrid('reload');//重新加载列表数据
							}
							et.showMsg(data.msg);
						}
					});
		 		}
		 	});
		}
	</script>
</body>
</html>
