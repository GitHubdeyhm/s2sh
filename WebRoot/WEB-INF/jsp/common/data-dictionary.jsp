<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>数据字典设置</title>
</head>
<body>
	<div id="dict_datagrid_toolbar" class="search-toolbar clearfix">
		<div style="float:left;">
			<form id="dict_search_form">
				<div>
					<label>字典名称</label>
					<input type="text" id="dictName_search" maxlength="20" />
					<label>字典键</label>
					<input type="text" id="dictKey_search" maxlength="20" />
					<label>字典值</label>
					<input type="text" id="dictValue_search" maxlength="20" />
					
					<a onclick="search_dg()" class="easyui-linkbutton" iconCls="icon-search" plain="true">查 询</a>
					<a onclick="reset_dg()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">重 置</a>
				</div>
			</form>
		</div>
		<div style="float:right;">
			<a onclick="showInputDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
			<a onclick="showInputDialog(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
			<a onclick="del()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		</div>
	</div>
	<!-- 角色表格 -->
	<table id="dict_datagrid">
		<thead>
			<tr>
				<th field="ck" checkbox="true"></th><!-- 显示复选框 -->
				<th field="dictName" width="120">字典名称</th>
				<th field="dictKey" width="120">字典键</th>
				<th field="dictValue" width="100">字典值</th>
				<th field="dictUserEdit" width="60" formatter="yesNoFmt">用户编辑</th>
				<th field="dictNote" width="200">字典说明</th>
				<th field="createTime" width="100">操作时间</th>
			</tr>
		</thead>
	</table>
	
<script type="text/javascript">
	//表格对象
	var dict_datagrid;
	$(function(){
		/**
		 * 数据表格
		 */
		dict_datagrid = $('#dict_datagrid').datagrid({
			toolbar: "#dict_datagrid_toolbar",//工具栏--有助于页面布局
			url : "${ctx}/common/data-dictionary!showList.action"
		});
	});
	
	/**
	 * 批量删除
	 */
	function del() {
		//得到所有选中的行
		var rows = dict_datagrid.datagrid("getSelections");
		if (!rows || rows.length < 1) {
			//如果没有任何一行选中，则弹出提示框 
			et.showMsg(Constant.emptyTipMsg);
			return;
		}
		$.messager.confirm('删除数据字典', '您确定要删除所选数据字典？', function(r) {
	 		if(r) {
	 			var ids = [];
	 			for (var i = 0; i < rows.length; i++) {
	 				ids.push(rows[i].id);
	 			}
				$.ajax({
					type: "POST",
					url: "${ctx}/common/data-dictionary!delete.action",
					data: {id: ids.join(",")},
					dataType: 'json',
					success: function(data) {
						//1：成功，0：失败
						if (data.resultCode == 1) {
							dict_datagrid.datagrid('reload');//重新加载列表数据
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
		var dictName = $("#dictName_search").val();;
		var dictKey = $("#dictKey_search").val();
		var dictValue = $("#dictValue_search").val();
		dict_datagrid.datagrid("load", {
			dictName: dictName,
			dictKey: dictKey,
			dictValue: dictValue
		});
	}
	/**
	 * 点击处理弹出框
	 */
	function showInputDialog(index) {
		var inputUrl = "${ctx}/common/data-dictionary!input.action";
		//默认新增
		var title = "新增字典项", row = null;
		if (index != undefined) {
			title = "编辑字典项";
			//得到表格所有选择的行
			var rows = dict_datagrid.datagrid("getSelections");
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
		}
		//新增或编辑对话框
		$('<div id="dialogInput"></div>').dialog({
			title : title,
			width : 500,
			height : 400,
			modal : true, //模式窗口：窗口背景不可操作  
			resizable : false, //可缩放边框大小  
			href : inputUrl,
			buttons : [{
				text : '确定',
				handler : function() {
					$("#dict_input_form").submit();//提交表单
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
					$('#dict_input_form').form('load', row);
					if (row.dictUserEdit == 1) {
						$("#dictUserEdit1_input").attr("checked", true);
					} else if (row.dictUserEdit == 0) {
						$("#dictUserEdit0_input").attr("checked", true);
					}
				}
				//验证表单
				validateForm();
			}
		});
	}
	/**
	 * 重置
	 */
	function reset_dg() {
		$("#dictName_search").val("").focus();
		$("#dictKey_search").val("");
		$("#dictValue_search").val("");
	}
</script>
</body>
</html>