<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>角色管理</title>
</head>
<body>
	<div id="role_datagrid_toolbar" class="search-toolbar clearfix">
		<div style="float:left;">
			<form id="role_search_form">
				<!-- 单个文本框时防止按enter键刷新页面 -->
				<input type="text" style="display:none;" />
				<label>角色名称</label>
				<input type="text" id="roleName_search" maxlength="20" />
				
				<a onclick="search_dg()" class="easyui-linkbutton" iconCls="icon-search" plain="true">查 询</a>
				<a onclick="reset()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">重 置</a>
			</form>
		</div>
		<div style="float:right;">
			<a onclick="showAuthDialog()" class="easyui-linkbutton" iconCls="icon-set" plain="true">角色授权</a>
			<a onclick="showInputDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
			<a onclick="showInputDialog(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
			<a onclick="del()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		</div>
	</div>
	<!-- 角色表格 -->
	<table id="role_datagrid">
		<thead>
			<tr>
				<th field="ck" checkbox="true"></th><!-- 显示复选框 -->
				<th field="roleName" width="120">角色名称</th>
				<%--<th field="roleLevel" width="100">角色级别</th>--%>
				<th field="roleOrder" width="100" sortable="true">排序号</th>
				<th field="roleNote" width="200">角色说明</th>
				<th field="createUser" width="100" formatter="createUserFmt">创建人</th>
				<th field="createTime" width="100">创建时间</th>
			</tr>
		</thead>
	</table>
	
<script type="text/javascript">
	//表格对象
	var role_datagrid;
	$(function(){
		/**
		 * 角色数据表格
		 */
		role_datagrid = $('#role_datagrid').datagrid({
			toolbar: "#role_datagrid_toolbar",//工具栏--有助于页面布局
			url : "${ctx}/uums/role!showList.action",
			sortName: "roleOrder",//根据角色排序号排序
			remoteSort: true,//允许远程排序
			sortOrder: "asc"//排序方式，升序
		});
	});
	
	//创建人列
	function createUserFmt(value, row, index) {
		if (value) {
			return value.realName;
		}
		return Constant.emptyContent;
	}
	/**
	 * 点击处理弹出框
	 */
	function showInputDialog(index) {
		var inputUrl = "${ctx}/uums/role!input.action";
		//默认新增
		var title = "新增角色", row = null;
		if (index != undefined) {
			title = "编辑角色";
			//得到表格所有选择的行
			var rows = role_datagrid.datagrid("getSelections");
			if (!rows || rows.length < 1) {
				//如果没有任何一行选中，则弹出提示框 
				et.showMsg(Constant.emptyTipMsg);
				return;
			}
			if (rows.length > 1) {
				//如果编辑时选中了多条记录，则默认选择编辑第一行并弹出提示框 
				et.showMsg(Constant.editFirstRow);
			}
			row = rows[0];
			inputUrl += "?id="+row.id;
		}
		//新增或编辑对话框
		$('<div id="dialogInput"></div>').dialog({
			title : title,
			width : 550,
			height : 480,
			modal : true, //模式窗口：窗口背景不可操作  
			resizable : false, //可缩放边框大小  
			href : inputUrl,
			buttons : [{
				text : '确定',
				handler : function() {
					$("#role_input_form").submit();//提交表单
				}
			}, {
				text : '取消',
				handler : function() {
					$("#dialogInput").dialog('destroy');
				}
			}],
			onClose : function() {
				$(this).dialog('destroy');//销毁对话框
			},
			onLoad : function() {
				if (row) {
					$('#role_input_form').form('load', row);
				} else {
					getOrder();//新增
				}
			}
		});
		//当窗口发生变化是调整对话框的大小
		resizeDialog("dialogInput");
	}
	
	//角色授权页面
	function showAuthDialog() {
		var url = "${ctx}/uums/role!auth.action";
		//默认新增
		var title = "角色授权", row = null;
		//得到表格所有选择的行
		var rows = role_datagrid.datagrid("getSelections");
		if (!rows || rows.length < 1) {
			//如果没有任何一行选中，则弹出提示框 
			et.showMsg(Constant.emptyTipMsg);
			return;
		}
		if (rows.length > 1) {
			//如果编辑时选中了多条记录，则默认选择编辑第一行并弹出提示框 
			et.showMsg(Constant.editFirstRow);
		}
		row = rows[0];
		url += "?id="+row.id;
		//新增或编辑对话框
		$('<div id="dialogInput"></div>').dialog({
			title : title,
			width : 550,
			height : 450,
			modal : true, //模式窗口：窗口背景不可操作  
			resizable : false, //可缩放边框大小  
			href : url,
			buttons : [{
				text : '确定',
				handler : function() {
					saveAuth();
				}
			}, {
				text : '取消',
				handler : function() {
					$("#dialogInput").dialog('destroy');
				}
			}],
			onClose : function() {
				$(this).dialog('destroy');//销毁对话框
			},
			onLoad : function() {
				$("#roleId_input").val(row.id);
				$('#roleName_auth').text(row.roleName);
			}
		});
	}
	/**
	 * 批量删除
	 */
	function del() {
		//得到所有选中的行
		var rows = role_datagrid.datagrid("getSelections");
		if (!rows || rows.length < 1) {
			//如果没有任何一行选中，则弹出提示框 
			et.showMsg(Constant.emptyTipMsg);
			return;
		}
		$.messager.confirm('删除角色', '您确定要删除所选角色？', function(r) {
	 		if(r) {
	 			var ids = [];
	 			for (var i = 0; i < rows.length; i++) {
	 				ids.push(rows[i].id);
	 			}
				$.ajax({
					type: "POST",
					url: "${ctx}/uums/role!delete.action",
					data: {id: ids.join(",")},
					dataType: 'json',
					success: function(data) {
						//1：成功，0：失败
						if (data.resultCode == 1) {
							role_datagrid.datagrid('reload');//重新加载列表数据
						}
						et.showMsg(data.msg);
					}
				});
	 		}
	 	});
	}
	/**
	 * 查询
	 */
	function search_dg() {
		var roleName = $("#roleName_search").val();
		role_datagrid.datagrid("load", {
			roleName: roleName
		});
	}
	/**
	 * 重置
	 */
	function reset() {
		$("#roleName_search").val("").focus();
	}
</script>
</body>
</html>