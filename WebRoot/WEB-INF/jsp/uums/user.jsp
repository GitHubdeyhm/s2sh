<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>用户管理</title>
	<style type="text/css">
		.upload-file {
			text-align: center;
		}
		.upload-file img {
			max-width: 170px;
			max-width: 200px;
			dispaly: none;
		}
		.upload-file label {
			margin-top: 8px;
			display: inline-block;
			text-align: center;
			cursor: pointer;
			font-size: 12px;			
			border: 1px solid #BBB;
			padding: 4px 8px;
			color: #fff;
			border-color: #1f637b;
			background: #2984a4;
			background: -webkit-linear-gradient(top,#2984a4 0,#24748f 100%);
			background: -moz-linear-gradient(top,#2984a4 0,#24748f 100%);
			background: -o-linear-gradient(top,#2984a4 0,#24748f 100%);
			background: linear-gradient(to bottom,#2984a4 0,#24748f 100%);
			filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#2984a4,endColorstr=#24748f,GradientType=0);
		}
		.import-label {
			cursor: pointer;
			padding: 2px 4px;
		}
		.import-label:hover {
			background-color: #e6e6e6;
		}
		.icon-import {
			display: inline-block;
			width: 16px;
			height: 16px;
			margin-right: 2px;
			background: url(${ctx}/jslib/easyui-1.3.5/themes/icons/import.png) no-repeat center center;
		}
	</style>
</head>
<body class="easyui-layout">
	<div data-options="region:'west'" style="width:200px;">
		<div class="easyui-tabs" border="false" fit="true">
			<div id="unit_tab" title="部门树">
				<ul id="unit_search"></ul>
			</div>
		</div>
	</div>
	<div data-options="region:'center'">
		<div id="user_datagrid_toolbar" class="search-toolbar clearfix">
			<div style="float:left;">
				<form id="user_search_form" action="${ctx}/uums/user!exportExcel.action" method="post">
					<input type="hidden" name="unitCodes" id="unitCodes_search" />
					<label>用户名</label>
					<input type="text" name="userName" id="userName_search" maxlength="20" />
					<label>真实姓名</label>
					<input type="text" name="realName" id="realName_search" maxlength="20" />
					<label>所属角色</label>
					<input type="text" name="roleIds" id="roleIds_search" />
					
					<a href="javascript:search();" class="easyui-linkbutton" iconCls="icon-search" plain="true">查 询</a>
					<a href="javascript:reset();" class="easyui-linkbutton" iconCls="icon-reload" plain="true">重 置</a>
				</form>
			</div>
			<div style="float:right;">
				<a onclick="showInputDialog()" class="easyui-linkbutton" iconCls="icon-comment" plain="true">分配角色</a>
				<a onclick="showInputDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
				<a onclick="showInputDialog(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true">编辑</a>
				<a onclick="del()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
				<a onclick="resetPwd()" class="easyui-linkbutton" iconCls="icon-user-edit" plain="true">重置密码</a>
				<a onclick="exportExcel()" class="easyui-linkbutton" iconCls="icon-export" plain="true">导出Excel</a>
				<form id="import_excel_form" target="submit_frame" action="${ctx}/uums/user!importExcel.action" method="post" enctype="multipart/form-data">
					<label for="import_file" class="import-label">
						<em class="icon-import"></em>
						<font style="vertical-align:top;">导入Execl</font>
					</label>
					<input type="file" name="headImgUrl" id="import_file" style="position:absolute;left:-9999px;" />
				</form>
				<!-- 通过iframe实现无刷新表单提交 -->
				<iframe id="submit_frame" name="submit_frame" frameborder="0" style="position:absolute;top:-9999px;left:-9999px;"></iframe>
			</div>
		</div>
		<table id="user_datagrid">
			<thead>
				<tr>
					<th field="ck" checkbox="true"></th><!-- 显示复选框 -->
					<th field="userName" width="100">用户名</th>
					<th field="realName" width="120">真实姓名</th>
					<th field="genderText" width="50">性别</th>
					<th field="phone" width="80">手机号码</th>
					<th field="email" width="100">邮箱</th>
					<th field="unitNames" width="100">所属部门</th>
					<th field="roleNames" width="100">所属角色</th>
					<th field="createTime" width="100">注册时间</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<script type="text/javascript">
		var user_datagrid;
		
		$(function(){
			/**
			 * 用户数据表格
			 */
			user_datagrid = $('#user_datagrid').datagrid({
				toolbar: "#user_datagrid_toolbar",//工具栏--有助于页面布局
				url : '${ctx}/uums/user!showList.action'
			});
			/**
			 * 部门树
			 */
			$("#unit_search").tree({
				url: "${ctx}/uums/unit!showCodeLazyTree.action",
		        onClick: function(node) {
		        	//设置部门
					var selectedNode = $("#unit_search").tree("getSelected");
					var unitCode = selectedNode ? selectedNode.id : "";
					$("#unitCodes_search").val(unitCode);
					//点击树节点查询
		        	search();
		        }
			});
			/**
		     *　下拉列表
		     */
			$("#roleIds_search").combobox({
		        url: "${ctx}/uums/role!showCombobox.action",   
		      	value: "-1"
			});
			
			//导入excel文件
			$("#import_file").change(function(){
				if (this.value) {
					$("#import_excel_form").submit();
				}
			});
			//加载完成事件
			$("#submit_frame").load(function(){
				//得到iframe窗口的内容
				var content = "";
				//获取body标签的第一级子节点
				var nodes = this.contentWindow.document.body.childNodes;
				if (nodes) {
					for (var i = 0; i < nodes.length; i++) {
						if (nodes[i].nodeType == 1) {
							content += nodes[i].innerHTML;//得到文本内容
						}
					}
				}
				if (content) {
					//解析成json格式
					var json = $.parseJSON(content);
					et.showMsg(json.msg);
				}
				//提交完成后必须确保文件选择域的值为空，防止下次选同一文件时不会触发onchange事件
				$("#import_file").remove();
				$("#import_excel_form").append('<input type="file" name="headImgUrl" id="import_file" style="position:absolute;left:-9999px;" />');
				$("#import_file").change(function(){
					if (this.value) {
						$("#import_excel_form").submit();
					}
				});
			});
		});
		
		/**
		 * 点击新增/编辑弹出框
		 */
		function showInputDialog(index) {
			var inputUrl = "${ctx}/uums/user!input.action";
			var title = '新增用户', row = null;
			if (index != undefined) {
				title = '编辑用户';
				var rows = user_datagrid.datagrid("getSelections");
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
			$('<div id="userInput"></div>').dialog({
				title : title,
				width : 600,
				height : 410,
				modal : true, //模式窗口：窗口背景不可操作  
				resizable : false, //可缩放边框大小  
				href : inputUrl,
				buttons : [{
					text : '确定',
					handler : function() {
						$("#user_input_form").submit();//提交表单
					}
				}, {
					text : '取消',
					handler : function() {
						$("#userInput").dialog('destroy');
					}
				}],
				onClose : function() {
					$(this).dialog('destroy');
				},
				onLoad : function() {
					if (row) {
						var imgUrl = "";
						if (row.imgUrl_temp == undefined) {
							row.imgUrl_temp = row.headImgUrl ? row.headImgUrl : "";//临时存储图片的地址
							//保证easyui的表单load方法不会设置file文件选择框的值
							row.headImgUrl = null;
						}
						imgUrl = row.imgUrl_temp.substring(0, row.imgUrl_temp.indexOf("#"));
						$("#headImgUrl_img").attr("src", "${ctx}/upload/image/"+imgUrl).css({
							width: 180,
							height: 190,
							display: 'inline-block'
						});
						$('#user_input_form').form('load', row);
						$("#unitCodes_temp").val(row.unitCodes);
						$("#roleIds_temp").val(row.roleIds);
					}
				}
			});
		}
		/**
		 * 批量删除
		 */
		function del() {
			//得到所有选中的行
			var rows = user_datagrid.datagrid("getSelections");
			if (!rows || rows.length < 1) {
				//如果没有任何一行选中，则弹出提示框 
				et.showMsg(Constant.emptyTipMsg);
				return;
			}
			$.messager.confirm('删除用户', '您确定要删除所选用户？', function(r) {
		 		if(r) {
		 			var ids = [];
		 			for (var i = 0; i < rows.length; i++) {
		 				ids.push(rows[i].id);
		 			}
					$.ajax({
						type: "POST",
						url: "${ctx}/uums/user!delete.action",
						data: {id: ids.join(",")},
						dataType: "json",
						success: function(data) {
							//1：成功，0：失败
							if (data.resultCode == 1) {
								user_datagrid.datagrid('reload');//重新加载列表数据
							}
							et.showMsg(data.msg);
						}
					});
		 		}
		 	});
		}
		
		/**
		 * 重置密码
		 */
		function resetPwd() {
			//得到所有选中的行
			var rows = user_datagrid.datagrid("getSelections");
			if (!rows || rows.length < 1) {
				//如果没有任何一行选中，则弹出提示框 
				et.showMsg(Constant.emptyTipMsg);
				return;
			}
			var pwd = '<strong style="color:red;font-size:16px;">123456</strong>';
			$.messager.confirm('重置密码', '确定要重置所选用户的密码，重置后该用户的密码为&nbsp;'+pwd, function(r) {
		 		if(r) {
		 			var ids = [];
		 			for (var i = 0; i < rows.length; i++) {
		 				ids.push(rows[i].id);
		 			}
					$.ajax({
						type: "POST",
						url: "${ctx}/uums/user!resetPwd.action",
						data: {id: ids.join(",")},
						dataType: "json",
						success: function(data) {
							et.showMsg(data.msg);
						}
					});
		 		}
		 	});
		}
		/**
		 * 查询
		 */
		function search() {
			user_datagrid.datagrid("load", getSearchParam());
		}
		/**
		 * 重置查询栏
		 */
		function reset() {
			$("#userName_search").val("").focus();
			$("#realName_search").val("");
			$("#roleIds_search").combobox("setValue", "-1");
		}
		/**
		 * 导出excel表格
		 */
		function exportExcel() {
			$("#user_search_form").submit();
		}
		
		/**
		 * 封装查询参数对象
		 */
		function getSearchParam() {
			var userName = $("#userName_search").val();
			var realName = $("#realName_search").val();
			var roleId = $("#roleIds_search").combobox("getValue");
			var unitCode = $("#unitCodes_search").val();
			var param = {
				userName: userName,
				realName: realName,
				roleIds: roleId,
				unitCodes: unitCode
			}
			return param;
		}
	</script>
</body>
</html>
