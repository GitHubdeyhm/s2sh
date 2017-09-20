<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div>
	<form id="dict_input_form" method="post">
		<input type="hidden" name="id" id="id_input" />

		<table cellspacing="0" class="input-table">
			<tbody>
				<tr>
					<th><em>*</em><label>字典名称</label></th>
					<td><input type="text" name="dictName" id="dictName_input" /></td>
				</tr>
				<tr>
					<th><em>*</em><label>字典键</label></th>
					<td><input type="text" name="dictKey" id="dictKey_input" /></td>
				</tr>
				<tr>
					<th><em>*</em><label>字典值</label></th>
					<td><input type="text" name="dictValue" id="dictValue_input" /></td>
				</tr>
				<tr>
					<th><em>*</em><label>用户是否可编辑</label></th>
					<td>
						<input type="radio" name="dictUserEdit" value="1" id="dictUserEdit1_input" />是
						<input type="radio" name="dictUserEdit" value="0" checked="checked" id="dictUserEdit0_input" />否
					</td>
				</tr>
				<tr>
					<th><label>字典描述</label></th>
					<td>
						<textarea name="dictNote" id="dictNote_input"></textarea>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
	
<script type="text/javascript">
	$(function(){
		//提交表单
		$('#dict_input_form').form({
			url: "${ctx}/common/data-dictionary!save.action",
			onSubmit: function(param) {
				var isValid = $(this).form('validate');
				if (isValid) {
					et.progress();//表单验证通过启动进度条
				}
				return isValid;
			},
			success: function(data) {
				//表单验证且返回提交结果
				$.messager.progress('close');
				var json = $.parseJSON(data);
				//1：成功，0：失败
				if (json.resultCode == 1) {
					dict_datagrid.datagrid('reload');//重新加载列表数据
					$("#dialogInput").dialog('destroy');//销毁对话框 
				}
				et.showMsg(json.msg);
			}
		});
	});
	//验证表单
	function validateForm() {
		$("#dictName_input").validatebox({
			required: true,
		    missingMessage: "请输入字典名称",
		    validType: ["CHS_alphanum", "maxLength[20]"]
		});
		$("#dictKey_input").validatebox({
			required: true,
		    missingMessage: "请输入字典键",
		    validType: ["code_alphanum", "maxLengthNum[20]"]
		});
		$("#dictValue_input").validatebox({
			required: true,
		    missingMessage: "请输入字典值",
		    validType: ["CHS_alphanum", "maxLengthNum[20]"]
		});
		$("#dictNote_input").validatebox({
		    validType: ["maxLengthNum[200]"]
		});
	}
</script>
</body>
</html>