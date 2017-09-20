<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<div>
	<form id="button_input_form" method="post">
		<input type="hidden" name="id" id="id_input" />
		<input type="hidden" name="menuCode" id="menuCode_input" />

		<table cellspacing="0" class="input-table">
			<tbody>
				<tr>
					<th><em>*</em><label>所属菜单</label></th>
					<td><input type="text" name="menuName" disabled="disabled" class="text-read" id="menuName_input" /></td>
				</tr>
				<tr>
					<th><em>*</em><label>按钮名称</label></th>
					<td><input type="text" name="buttonName" id="buttonName_input" /></td>
				</tr>
				<tr>
					<th><label>按钮标识</label></th>
					<td>
						<input type="text" name="buttonMark" class="text-read" id="buttonMark_input" readonly="readonly" />
						<span>按钮标识是约定好的唯一标识</span>
					</td>
				</tr>
				<%--<tr>
					<th><em>*</em><label>按钮排序</label></th>
					<td><input type="text" name="buttonOrder" id="buttonOrder_input" /></td>
				</tr>--%>
				<tr>
					<th><label>按钮描述</label></th>
					<td>
						<textarea name="buttonNote" id="buttonNote_input"></textarea>
					</td>
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
		    /*$("#buttonOrder_input").validatebox({
		    	required: true,
		    	missingMessage: '请输入按钮排序号',
			    validType: ["positiveZ_num", "maxLengthNum[8]"]
			});*/
		    $("#buttonNote_input").validatebox({
			    validType: ["maxLengthNum[200]"]
			});
		    
		    //父级菜单
		    $("#buttonName_input").combobox({
		    	url: "${ctx}/json/button.json",
				panelHeight: "auto",
				value: "-1",
				onSelect: function(rec) {
					if (rec.value == "-1") {
						$("#buttonMark_input").val("");
					} else {
						$("#buttonMark_input").val(rec.value);
						var text = $("#buttonName_input").combobox("getText");
						$(this).combobox("setValue", text);
					}
				},
				onLoadSuccess: function(data) {
				}
		    });
		});
	</script>
</body>
</html>