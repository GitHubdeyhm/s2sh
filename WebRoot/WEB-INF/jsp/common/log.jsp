<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>日志管理</title>
</head>
<body>
	<div id="log_datagrid_toolbar" class="search-toolbar clearfix">
		<div style="float:left;">
			<form id="log_search_form">
				<!-- 单个文本框时防止按enter键刷新页面 -->
				<input type="text" style="display:none;" />
				<div>
					<label>日志名称</label>
					<input type="text" id="logName_search" maxlength="20" />
					<label>日志内容</label>
					<input type="text" id="logContent_search" maxlength="20" />
					<label>操作类型</label>
					<input type="text" id="roleName_search" maxlength="20" />
					<label>操作人</label>
					<input type="text" id="realName_search" maxlength="20" />
				</div>
				<div style="margin-top:4px;">
					<label>操作时间</label>
					<input type="text" id="beginTime_search" class="easyui-datetimebox" />
					<span>-</span>
					<input type="text" id="endTime_search" class="easyui-datetimebox" />
					
					<a onclick="search_dg()" class="easyui-linkbutton" iconCls="icon-search" plain="true">查 询</a>
					<a onclick="reset_dg()" class="easyui-linkbutton" iconCls="icon-reload" plain="true">重 置</a>
				</div>
			</form>
		</div>
		<div style="float:right;">
			<a onclick="del()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		</div>
	</div>
	<!-- 角色表格 -->
	<table id="log_datagrid">
		<thead>
			<tr>
				<th field="ck" checkbox="true"></th><!-- 显示复选框 -->
				<th field="logName" width="120">日志名称</th>
				<th field="logContent" width="100">操作内容</th>
				<th field="optType" width="60">操作类型</th>
				<th field="optUrl" width="200">操作地址</th>
				<th field="optRequestMethod" width="60">请求方法</th>
				<th field="optUser" width="100" formatter="optUserFmt">操作人</th>
				<th field="userIP" width="100">IP地址</th>
				<th field="createTime" width="100">操作时间</th>
			</tr>
		</thead>
	</table>
	
<script type="text/javascript">
	//表格对象
	var log_datagrid;
	$(function(){
		/**
		 * 角色数据表格
		 */
		log_datagrid = $('#log_datagrid').datagrid({
			toolbar: "#log_datagrid_toolbar",//工具栏--有助于页面布局
			url : "${ctx}/common/log!showList.action"
		});
	});
	
	//创建人列
	function optUserFmt(value, row, index) {
		if (value) {
			return value.realName;
		}
		return Constant.emptyContent;
	}
	/**
	 * 批量删除
	 */
	function del() {
		//得到所有选中的行
		var rows = log_datagrid.datagrid("getSelections");
		if (!rows || rows.length < 1) {
			//如果没有任何一行选中，则弹出提示框 
			et.showMsg(Constant.emptyTipMsg);
			return;
		}
		$.messager.confirm('删除日志', '您确定要删除所选日志？', function(r) {
	 		if(r) {
	 			var ids = [];
	 			for (var i = 0; i < rows.length; i++) {
	 				ids.push(rows[i].id);
	 			}
				$.ajax({
					type: "POST",
					url: "${ctx}/common/log!delete.action",
					data: {id: ids.join(",")},
					dataType: 'json',
					success: function(data) {
						//1：成功，0：失败
						if (data.resultCode == 1) {
							log_datagrid.datagrid('reload');//重新加载列表数据
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
		var logName = $("#logName_search").val();
		var logContent = $("#logContent_search").val();
		var realName = $("#realName_search").val();
		var beginTime = $("#beginTime_search").datetimebox("getValue");
		var endTime = $("#endTime_search").datetimebox("getValue");
		if ((beginTime != "") && (endTime != "") && (beginTime > endTime)) {
			et.showMsg("开始日期不能大于结束日期");
			return;
		}
		log_datagrid.datagrid("load", {
			logName: logName,
			logContent: logContent,
			realName: realName,
			beginTime: beginTime,
			endTime: endTime
			
		});
	}
	/**
	 * 重置
	 */
	function reset_dg() {
		$("#logName_search").val("").focus();
		$("#logContent_search").val("");
		$("#realName_search").val("");
		$("#beginTime_search").datetimebox("setValue", "");
		$("#endTime_search").datetimebox("setValue", "");
	}
</script>
</body>
</html>