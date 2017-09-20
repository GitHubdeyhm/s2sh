<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>部门管理</title>
	<style type="text/css">
		form.input-form ul li {
			margin: 6px;
		}
		form.input-form ul li input[type='text'] {
			border: 1px solid #0081C2;
			width: 200px;
			height: 24px;
			text-indent: 4px;
		}
		form.input-form ul li textarea {
			border: 1px solid #0081C2;
			width: 240px;
			height: 80px;
			padding-left: 4px;
			resize: none;
		}
	</style>
</head>
<body class="easyui-layout">
	<div data-options="region:'west'" style="width:200px;">
		<div class="easyui-tabs" border="false" fit="true" >
			<div id="unit_tab" title="部门树">
				<ul id="unit_tree"></ul>
			</div>
		</div>
		
		<div id="unit_rightMenu" class="easyui-menu" data-options="onClick:menuHandler" style="width:80px;">
			<div id="rightMenu_add" data-options="name:'save',iconCls:'icon-add'">新增</div>
			<div id="rightMenu_remove" data-options="name:'delete',iconCls:'icon-remove'">删除</div>
		</div>
	</div>
	
	<div data-options="region:'center'">
		<form id="unit_input_form" method="post" class="input-form">
			<input type="hidden" name="unitId" id="unitId_input" />
			<input type="hidden" name="parentId" id="parentId_input" />
			<input type="hidden" name="unitCode" id="unitCode_input" />
			<ul>
				<li style="margin-left:90px;color:#0081C2;"><h4>部门信息</h4></li>
				<li>
					<label>上级部门</label>
					<input type="text" id="parentUnitName_input" readonly="readonly" class="text-read" />
				</li>
				<li>
					<label>部门名称</label>
					<input type="text" name="unitName" id="unitName_input" />
				</li>
				<li>
					<label>部门排序</label>
					<input type="text" name="unitOrder" id="unitOrder_input" />
				</li>
				<li>
					<label style="vertical-align:top;">部门描述</label>
					<textarea name="unitNote" id="unitNote_input"></textarea>
				</li>
				<li style="margin-left:100px;">
	    			<a class="easyui-linkbutton" onclick="save()">确定</a>
				</li>
			</ul>
		</form>
		<%--<input type="text" id="userSelectedId_input" style="display:none;" />
		<textarea rows="10" cols="60" id="userSelected_input" style="display:none;"></textarea>--%>
	</div>

	<script type="text/javascript">
		//当前节点，表示当前正在操作的节点
		var currentNode = null;
		
		$(function(){
			/**
			 * 表单验证
			 */
			$("#unitName_input").validatebox({
			    required: true,
			    missingMessage: '请输入部门名称',
			    validType: ['CHSA_alphanum', 'maxLength[40]']
			});
			$("#unitOrder_input").validatebox({
			    required: true,
			    missingMessage: '请输入部门排序',
			    validType: ['positive_num', 'maxLength[6]']
			});
			$("#unitNote_input").validatebox({
			    validType: ['maxLength[200]']
			});
			/**
			 * 部门树结构
			 */
			$("#unit_tree").tree({
				url: "${ctx}/uums/unit!showCodeLazyTree.action",
				cascadeCheck : false, //级联选择
				checkbox: false,//显示复选框
				lines: false,//显示虚线
				onClick: function(node) {
					//点击节点时回显信息--根节点不回显
					if (node.id == "-1") {
						return;
					}
					currentNode = node;
					fillForm(node);//回显表单信息
				},
				onContextMenu: function(e, node) {
					e.preventDefault();//阻止浏览器右键默认操作
					//右键时选中节点
					$(this).tree("select", node.target);
					currentNode = node;
					//根节点不允许删除
					if (node.id == "-1") {
						$("#rightMenu_remove").hide();
					} else {
						$("#rightMenu_remove").show();
					}
					//显示右键菜单
					$("#unit_rightMenu").menu("show", {
						left: e.pageX,
						top: e.pageY
					});
				},
				onLoadSuccess: function(node, data) {
					//alert("触发了加载成功事件====="+currentNode);
					if (!currentNode) {
						//加载成功时设置第一个节点高亮 
						var firstNode = $(this).find("li:eq(1)").children("div");
						//存在第一个节点
						if (firstNode.length > 0) {
							firstNode.trigger("click");
						}
					}
					coverSelectedNode();//使选中节点的背景色完全覆盖
				},
				onExpand: function(node) {
					coverSelectedNode();//使选中节点的背景色完全覆盖
				},
				onCollapse: function(node) {
					coverSelectedNode();//使选中节点的背景色完全覆盖
				}
			});
			/**
			 * 表单
			 */
			$("#unit_input_form").form({
			    url: "${ctx}/uums/unit!save.action",
			    onSubmit: function() {
			    	var isValid = $(this).form("validate");
			    	return isValid;
			    },   
			    success: function(data) {
			    	var json = $.parseJSON(data);
			    	//操作成功
			    	if (json.resultCode > 0) {
			    		var unitTree = $("#unit_tree");
			    		var u = json.obj;
			    		//修改
			    		if (json.resultCode == 1) {
				    		var optNode = unitTree.tree("find", u.unitCode);
				    		unitTree.tree("update", {
			    				target: optNode.target,
			    				text: u.unitName,
			    				attributes: {
			    					unitId: u.unitId,
			    					unitNote: u.unitNote,
			    					unitOrder: u.unitOrder
			    				}
			    			});
				    		coverSelectedNode();//使选中节点的背景色完全覆盖
			    		} else if (json.resultCode == 2) {
			    			//新增--会触发树加载成功事件
			    			var code = u.unitCode;
			    			var parentCode = code.substring(0, code.length-3);
			    			if (parentCode == "") {
			    				parentCode = "-1";
			    			}
			    			var optNode = unitTree.tree("find", parentCode);//父节点
			    			unitTree.tree("append", {
			    				parent: optNode.target,
			    				data: [{
			    					id: u.unitCode,
			    					text: u.unitName,
			    					attributes: {
				    					unitId: u.unitId,
				    					unitNote: u.unitNote,
				    					unitOrder: u.unitOrder
				    				}
			    				}]
			    			});
			    			var newNode = unitTree.tree("find", u.unitCode);//选中新增节点
			    			$(newNode.target).trigger("click");
			    		}
					}
					et.showMsg(json.msg);
			    }   
			});
		});
		/**
		 * 点击节点时回显表单信息
		 */
		function fillForm(node) {
			if (node.id != "-1") {
				//得到当前节点的父节点
				var parentNode = $("#unit_tree").tree("getParent", node.target);
				$("#parentId_input").val(parentNode.attributes.unitId);
				$("#parentUnitName_input").val(parentNode.text);
				
				$("#unitId_input").val(node.attributes.unitId);
				$("#unitCode_input").val(node.id);
				$("#unitName_input").val(node.text);
				$("#unitOrder_input").val(node.attributes.unitOrder);
				$("#unitNote_input").val(node.attributes.unitNote);
			}
		}
		/**
		 * 右键菜单点击处理函数
		 */
		function menuHandler(item) {
			var itemName = item.name;
			//新增
			if (itemName == "save") {
				var optNode = currentNode;
				$.ajax({
					type: "POST",
					url: "${ctx}/uums/unit!showOrder.action",
					dataType: "json",
					success: function(data) {
						if (data.resultCode == 1) {
							$("#unitId_input").val("");
							$("#unitName_input").val("").focus();
							$("#unitNote_input").val("");
							
							$("#parentId_input").val(optNode.attributes.unitId);
							$("#parentUnitName_input").val(optNode.text);
							$("#unitCode_input").val(optNode.id);
							$("#unitOrder_input").val(data.msg);
						} else {
							et.showMsg(data.msg);
						}
					}
				});
			}
			//删除
			if (itemName == "delete") {
				$.messager.confirm('删除部门', '删除该部门还会删除该部门下的所有子部门，您确定要删除？', function(r) {   
					if (r) {
						var optNode = currentNode;
						$.ajax({
							type: "POST",
							url: "${ctx}/uums/unit!delete.action",
							data: {unitCode: optNode.id},
							dataType: "json",
							success: function(data) {
								//操作成功
						    	if (data.resultCode == 1) {
						    		var unitTree = $("#unit_tree");
						    		unitTree.tree("remove", optNode.target);
						    		var code = optNode.id;
					    			var parentCode = code.substring(0, code.length-3);
					    			//删除最顶层的节点时选中第一个节点
					    			if (parentCode == "") {
					    				//加载成功时设置第一个节点高亮 
										var firstNode = unitTree.find("li:eq(1)").children("div");
										//存在第一个节点
										if (firstNode.length > 0) {
											firstNode.trigger("click");
										} else {
											clearForm();//没有子节点清楚表单
											currentNode = null;
										}
					    			} else {
					    				var parentNode = unitTree.tree("find", parentCode);//父节点
					    				$(parentNode.target).trigger("click");//选中父节点
					    			}
						    		coverSelectedNode();//使选中节点的背景色完全覆盖
								}
								et.showMsg(data.msg);
							}
						});
					}   
				});
			}
		}
		/**
		 * 保存
		 */
		function save() {
			//新增时展开节点
			var optNode = currentNode;
			if ($("#unitId_input").val() == "") {
				$("#unit_tree").tree("expand", optNode.target);
			}
			$("#unit_input_form").submit();
		}
		/**
		 * 清空表单
		 */
		function clearForm() {
			$("#parentId_input").val("");
			$("#parentUnitName_input").val("");
			$("#unitId_input").val("");
			$("#unitCode_input").val("");
			$("#unitName_input").val("");
			$("#unitNote_input").val("");
			$("#unitOrder_input").val("");
		}
		/**
		 * 使选中节点的背景色完全覆盖
		 */
		function coverSelectedNode() {
			$("#unit_tree").width("100%").width($("#unit_tab")[0].scrollWidth);//使选中节点的背景色完全覆盖
		}
		//多选人员对话框方法
		/*function chooseUsers() {
			//新增或编辑对话框
			$('<div id="userChoose"/>').dialog({
				title : "选择人员",
				width : 860,
				height : 480,//刚好适应笔记本最大高度
				modal : true, //模式窗口：窗口背景不可操作  
				resizable : false, //可缩放边框大小  
				href : "${ctx}/user/user!choose.action",
				buttons : [{
					text : '确定',
					iconCls : 'icon-ok',
					plain:true,
					handler : function() {
						getSelectedUsers();//得到选中的用户
						$("#userChoose").dialog('destroy');
					}
				}, {
					text : '取消',
					plain:true,
					iconCls : 'icon-cancel',
					handler : function() {
						$("#userChoose").dialog('destroy');
					}
				}],
				onClose : function() {
					$(this).dialog('destroy');
				}
			});
		}*/
	</script>
</body>
</html>