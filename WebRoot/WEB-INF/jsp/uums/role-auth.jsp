<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<!-- 角色授权 -->
<div>
	<input type="hidden" id="roleId_input" />
	<table cellspacing="0" class="input-table">
		<tbody>
			<tr>
				<th><label>角色名称</label></th>
				<td id="roleName_auth"></td>
			</tr>
		</tbody>
	</table>
	 
	<!-- 显示树信息 -->
	<div style="padding:6px 0px 6px 20px;background-color:#FFFDCA;">
		<div style="width:100%;overflow:auto;height:286px;">
			<h2 style="font-size:20px;font-weight:bold;color:#026EB9;">菜单权限</h2>
			<ul id="menu_tree"></ul>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function(){
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
		});
	});
	function saveAuth() {
		$.ajax({
			type: "post",
			url: "${ctx}/uums/role!roleAuth.action",
			data: {id: $("#roleId_input").val(), menuPrivils:getMenuPrivils()},
			dataType: "json",
			success: function(data) {
				if (data.resultCode == 1) {
					$("#dialogInput").dialog('destroy');
				}
				et.showMsg(data.msg);
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
			return "";
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
			return menuPrivils.substring(0, menuPrivils.length-1);
		}
	}
</script>
