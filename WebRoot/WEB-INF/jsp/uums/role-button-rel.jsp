<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>按钮权限管理</title>
</head>
<body class="easyui-layout">
	<div data-options="region:'west'" style="width:210px;">
		<div style="margin:7px 0px;">
			<span>角色</span>
			<input type="text" id="role_search" />
		</div>
		<div class="easyui-tabs" border="false">
			<div id="menu_tab" title="菜单树">
				<ul id="menu_search"></ul>
			</div>
		</div>
	</div>
	<div data-options="region:'center'">
		<div id="roleBtnRel_datagrid_toolbar" class="search-toolbar clearfix">
			<div style="float:right;">
				<a onclick="showInputDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加按钮权限</a>
				<a onclick="del()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
			</div>
		</div>
		<table id="roleBtnRel_datagrid">
			<thead>
				<tr>
					<th field="ck" checkbox="true"></th><!-- 显示复选框 -->
					<th field="buttonName" width="100">按钮名称</th>
					<th field="buttonMark" width="100">按钮标识</th>
					<th field="buttonNote" width="100">按钮说明</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<script type="text/javascript">
		var roleBtnRel_datagrid;
		
		$(function(){
			
			/**
			 * 用户数据表格
			 */
			 roleBtnRel_datagrid = $('#roleBtnRel_datagrid').datagrid({
				toolbar: "#roleBtnRel_datagrid_toolbar",//工具栏--有助于页面布局
				//url : '${ctx}/uums/role-button-rel!showList.action',
				pagination: false,
				onLoadSuccess : function(data) {
					//加载完成后取消所有的已选择项
					$(this).datagrid('clearSelections');
				}
			});
			 /**
			 * 角色树
			 */
			$("#role_search").combobox({
				url: "${ctx}/uums/role!showCombobox.action",
				value: "-1",
		        onSelect: function(rec) {
		        	var roleId = rec.value;
		        	$.ajax({
		        		type: "post",
		        		url: "${ctx}/uums/menu!showCodeTreeByRole.action",
		        		data: {roleId: roleId},
		        		dataType: "json",
		        		success: function(data) {
		        			$("#menu_search").tree("loadData", data);
		        		}
		        	});
		        },
				onLoadSuccess: function(data) {
				}
			});
			/**
			 * 菜单树
			 */
			$("#menu_search").tree({
				idField: "id",
				textField: "text",
				parentField: "parentId",
				//url: "${ctx}/uums/menu!showCodeTree.action",
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
			var inputUrl = "${ctx}/uums/role-button-rel!input.action";
			var title = '新增按钮权限';
			//菜单树选中节点
			var selectedNode = $("#menu_search").tree("getSelected");
			//新增时需选中某个菜单
			if (!selectedNode || selectedNode.id == "-1") {
				et.showMsg("请选择要添加按钮权限的菜单");
				return;
			}
			//默认新增
			//新增或编辑对话框
			$('<div id="roleButtonRelInput"></div>').dialog({
				title : title,
				width : 600,
				height : 350,
				modal : true, //模式窗口：窗口背景不可操作  
				resizable : false, //可缩放边框大小  
				href : inputUrl,
				buttons : [{
					text : '确定',
					handler : function() {
						var rows = button_datagrid.datagrid("getSelections");
						if (!rows || rows.length < 1) {
							//如果没有任何一行选中，则弹出提示框 
							et.showMsg(Constant.emptyTipMsg);
							return;
						}
						var buttonIds = [];
						for (var i = 0; i < rows.length; i++) {
							buttonIds.push(row[i].id);
						}
						$.ajax({
			        		type: "post",
			        		url: "${ctx}/uums/role-button-rel!save.action",
			        		data: {
			        			buttonIds: buttonIds.join(","),
			        			menuCode: selectedNode.id,
			        			roleId: $("#role_search").combobox("getValue")
			        		},
			        		dataType: "json",
			        		success: function(data) {
			        			if (data.code == 1) {
			        				$("#roleButtonRelInput").dialog('destroy');
			        			}
			        			ibms.showMsg(data.msg);
			        		}
			        	});
					}
				}, {
					text : '取消',
					handler : function() {
						$("#roleButtonRelInput").dialog('destroy');
					}
				}],
				onClose : function() {
					$(this).dialog('destroy');
				},
				onLoad : function() {
					loadButtonDatagrid(selectedNode.id);
				}
			});
			//当窗口发生变化时重新调整对话框的大小
			resizeDialog("roleButtonRelInput");
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
						$("#buttonInput").dialog('destroy');//销毁对话框 
						roleBtnRel_datagrid.datagrid('reload');//重新加载列表数据
					}
					et.showMsg(json.msg);
				}
			});
		}
		/**
		 * 查询
		 */
		function search() {
			var roleId = $("#role_search").combobox("getValue");
			if (roleId == "-1") {
				ibms.showMsg("请选择角色");
				return;
			}
			var menuCode = "-1";
			var selectedNode = $("#menu_search").tree("getSelected");
			if (selectedNode) {
				menuCode = selectedNode.id;
			}
			$.ajax({
				type: "post",
				url: "${ctx}/uums/role-button-rel!showList.action",
				data: {menuCode: menuCode, roleId: roleId},
				dataType: "json",
				success: function(data) {
					roleBtnRel_datagrid.datagrid("loadData", data);
				}
			});
		}
		/**
		 * 批量删除
		 */
		function del() {
			//得到所有选中的行
			var rows = roleBtnRel_datagrid.datagrid("getSelections");
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
								roleBtnRel_datagrid.datagrid('reload');//重新加载列表数据
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
