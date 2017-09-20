<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp"%>

<div>
	<form id="user_input_form" method="post" enctype="multipart/form-data">
		<input type="hidden" name="id" id="userId_input" />
		<!-- 临时存储部门编码和角色ID值，用于编辑用户时回显部门和角色信息 -->
		<input type="hidden" id="unitCodes_temp" />
		<input type="hidden" id="roleIds_temp" />
		<table cellspacing="0" class="input-table">
			<tbody>
				<tr>
					<td rowspan="5">
						<div class="upload-file">
							<div id="headImgUrl_div"><img id="headImgUrl_img" /></div>
						</div>
					</td>
					<th><em>*</em><label>所属部门</label></th>
					<td><input type="text" name="unitCodes" id="unitCodes_input" /></td>
				</tr>
				<tr>
					<th><em>*</em><label>所属角色</label></th>
					<td><input type="text" name="roleIds" id="roleIds_input" /></td>
				</tr>
				<tr>
					<th><em>*</em><label>真实姓名</label></th>
					<td><input type="text" name="realName" id="realName_input" /></td>
				</tr>
				<tr>
					<th><em>*</em><label>性别</label></th>
					<td>
						<input type="radio" name="genderStr" value="0" checked="checked" id="genderStr_input0" />男
						<input type="radio" name="genderStr" value="1" id="genderStr_input1" />女
					</td>
				</tr>
				<tr>
					<th><em>*</em><label>用户名</label></th>
					<td><input type="text" name="userName" id="userName_input" /></td>
				</tr>
				<tr>
					<td rowspan="3">
						<div class="upload-file">
							<input type="file" name="headImgUrl" id="headImgUrl_input" style="display:none;" />
							<div><input type="text" disabled="disabled" id="headImgUrl_text" /></div>
							<div><label for="headImgUrl_input">上传头像</label></div>
						</div>
					</td>
					<th><label>手机号码</label></th>
					<td><input type="text" name="phone" id="phone_input" /></td>
				</tr>
				<tr>
					<th><label>邮箱</label></th>
					<td><input type="text" name="email" id="email_input" /></td>
				</tr>
				<tr>
					<th><label>密码</label></th>
					<td>新增用户的初始化密码为：<em style="font-weight:bold;">123456</em></td>
				</tr>
			</tbody>
		</table>
	</form>
</div>

<script type="text/javascript">
	$(function(){
		/**
		 * 表单验证
		 */
		$("#realName_input").validatebox({
		    required: true,
		    missingMessage: '请输入真实姓名',
		    validType: ['CHS_alphanum', 'maxLength[40]']
		});
		$("#userName_input").validatebox({
		    required: true,
		    missingMessage: '请输入用户名',
		    validType: ['CHS_alphanum', 'maxLength[40]']
		});
		$("#phone_input").validatebox({
		    validType: ['phone']
		});
		$("#email_input").validatebox({
		    validType: ['email']
		});
		/**
		 * 部门树--一次加载
		 */
		$("#unitCodes_input").combotree({
			idField: "id",
			textField: "text",
			parentField: "parentId",
	        value: "-1",
			url: "${ctx}/uums/unit!showCodeTree.action",
			cascadeCheck : false, //级联选择
			checkbox: true,//显示复选框
			multiple: true, //多选
			lines: false,//显示虚线
			onLoadSuccess: function(data) {
				var root = $(this).tree("getRoot");//得到根节点
				if (root) {
					$(this).tree("collapseAll");//关闭所有节点
					$(this).tree("expand", root.target);//展开根节点--得到第一级子节点
					var values = null;
					if ($("#unitCodes_temp").val() == "") {
						values = [];
						values.push(root.id);
					} else {
						values = $("#unitCodes_temp").val().split(",");
					}
					$("#unitCodes_input").combotree("setValues", values);
				}
			}
		});
		/**
	     *　角色下拉列表
	     */
		$("#roleIds_input").combobox({
	        url: "${ctx}/uums/role!showCombobox.action",   
	      	multiple: false, //多选
	      	value: "-1",
	        onLoadSuccess: function() {
	        }
		});
		//实现选中图片预览效果
		new uploadImagePreview({
	 		fileId: 'headImgUrl_input',
	 		imageId: 'headImgUrl_img',
	 		divId: 'headImgUrl_div',
	 		width: 180,
	 		height: 190,
	 		callback: function(fileName) {
	 			document.getElementById("headImgUrl_text").value = fileName;
	 		}
	 	});
		
		//表单提交
		$('#user_input_form').form({
			url: "${ctx}/uums/user!save.action",
			onSubmit: function(param) {
				var isValid = $(this).form('validate');
				//表单验证未通过关闭进度条
				if (isValid) {
					et.progress();//表单验证通过启动进度条
				}
				return isValid;
			},
			success: function(data) {
				//表单验证通过且返回提交结果
				$.messager.progress('close');
				var json = $.parseJSON(data);
				//1：成功，0：失败
				if (json.resultCode == 1) {
					$("#userInput").dialog('destroy');//销毁对话框 
					user_datagrid.datagrid('reload');//重新加载列表数据
				}
				et.showMsg(json.msg);
			}
		});
	});
</script>
