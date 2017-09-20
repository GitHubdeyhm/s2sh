<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<div>
	<form id="role_input_form" method="post">
		<input type="hidden" name="id" id="id_input" />
		<input type="hidden" name="menuPrivils" id="menuPrivils_input" />
		<table cellspacing="0" class="input-table">
			<tbody>
				<tr>
					<th><em>*</em><label>角色名称</label></th>
					<td><input type="text" name="roleName" id="roleName_input" /></td>
				</tr>
				<tr>
					<th><em>*</em><label>排序号</label></th>
					<td><input type="text" name="roleOrder" id="roleOrder_input" /></td>
				</tr>
				<tr style="display:none;">
					<th><em>*</em><label>角色级别</label></th>
					<td><input type="text" name="roleLevel" id="roleLevel_input" /></td>
				</tr>
				<tr>
					<th><label>角色描述</label></th>
					<td><textarea name="roleNote" id="roleNote_input"></textarea></td>
				</tr>
			</tbody>
		</table>
	</form>
	
	<!-- 显示树信息 -->
	<div style="padding:6px 0px 6px 20px;background-color:#FFFDCA;">
		<div style="width:100%;overflow:auto;height:186px;">
			<h2 style="font-size:20px;font-weight:bold;color:#026EB9;">菜单权限</h2>
			<ul id="menu_tree"></ul>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function(){
		/**
		 * 表单验证
		 */
		$("#roleName_input").validatebox({
		    required: true,
		    missingMessage: '请输入角色名称',
		    validType: ['CHSA_alphanum', 'maxLength[40]']
		});
		/*$("#roleLevel_input").validatebox({
		    required: true,
		    missingMessage: '请输入角色级别',
		    validType: ['positive_num', 'maxLength[4]']
		});*/
		$("#roleOrder_input").validatebox({
		    validType: ['positive_num', 'maxLength[6]']
		});
		$("#roleNote_input").validatebox({
		    validType: ['maxLength[200]']
		});
		/**
		 * 菜单树--一次性加载
		 */
		$("#menu_tree").tree({
			idField: "id",
			textField: "text",
			parentField: "parentId",
			url: "${ctx}/uums/menu!showCodeTree.action",
			cascadeCheck : true, //级联选择
			checkbox: true,//显示复选框
			lines: true,//显示虚线
			onlyLeafCheck: false,//只在叶子节点前显示复选框
			onLoadSuccess: function(data) {
				var root = $(this).tree("getRoot");//得到根节点
				if (root) {
					$(this).tree("collapseAll");//关闭所有节点
					$(this).tree("expand", root.target);//展开根节点--得到第一级子节点
					//回显节点
					if ($("#id_input").val() != "") {
						var menuCodes = "${menuCodes}";
						if (menuCodes.length > 1) {
							var nodes = $(this).tree("getChildren", root.target);
							var codes = menuCodes.split(",");
							for (var i = 0 ; i < codes.length; i++) {
								for (var j = 0; j < nodes.length; j++) {
									var isLeaf = $(this).tree("isLeaf", nodes[j].target);
									if (isLeaf && (nodes[j].id == codes[i])) {
										$(this).tree("check", nodes[j].target);
										break;
									}
								}
							}
						}
					}
				}
			}
		});
		
		//表单
		$('#role_input_form').form({
			url: "${ctx}/uums/role!save.action",
			onSubmit: function(param) {
				var isValid = $(this).form('validate');
				if (isValid) {
					et.progress();//表单验证通过启动进度条
					getMenuPrivils();//得到角色菜单权限
				}
				return isValid;
			},
			success: function(data) {
				//表单验证且返回提交结果
				$.messager.progress('close');
				var json = $.parseJSON(data);
				//1：成功，0：失败
				if (json.resultCode == 1) {
					role_datagrid.datagrid('reload');//重新加载列表数据
					$("#dialogInput").dialog('destroy');//销毁对话框 
				}
				et.showMsg(json.msg);
			}
		});
	});
	/**
	 * 新增时得到排序
	 */
	function getOrder() {
		$.ajax({
			type: "POST",
			url: "${ctx}/uums/role!showOrder.action",
			dataType: 'json',
			success: function(data) {
				//1：成功，0：失败
				if (data.resultCode == 1) {
					$("#roleOrder_input").val(data.msg);
				} else {
					et.showMsg(data.msg);
				}
			}
		});
	}
	/**
	 * 得到角色权限
	 */
	function getMenuPrivils() {
		//菜单权限树
		var menuTree = $("#menu_tree");
		var checkNodes = menuTree.tree("getChecked");//得到选中的节点
		var indetNodes = menuTree.tree("getChecked", "indeterminate");//得到混合节点
		//没有选中任何节点
		if (checkNodes == "") {
			$("#menuPrivils_input").val("");
		} else {
			var root = menuTree.tree("getRoot");
			var menuPrivils = "";
			if (indetNodes != "") {
				//混合节点
				for (var i = 0; i < indetNodes.length; i++) {
					if (indetNodes[i].id != root.id) {
						menuPrivils += indetNodes[i].id + ",";
					}
				}
			}
			//选中节点
			for (var j = 0; j < checkNodes.length; j++) {
				if (checkNodes[j].id != root.id) {
					menuPrivils += checkNodes[j].id + ",";
				}
			}
			$("#menuPrivils_input").val(menuPrivils.substring(0, menuPrivils.length-1));
		}
	}
</script>
