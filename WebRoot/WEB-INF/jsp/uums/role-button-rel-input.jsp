<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>

<div style="width:100%;height:100%;">
	<table id="button_datagrid">
		<thead>
			<tr>
				<th field="ck" checkbox="true"></th><!-- 显示复选框 -->
				<th field="buttonName" width="100">按钮名称</th>
				<th field="buttonMark" width="100">按钮标识</th>
				<th field="buttonOrder" width="100">排序</th>
			</tr>
		</thead>
	</table>
</div>
	
<script type="text/javascript">
	var button_datagrid;
	
	$(function(){
		/**
		 * 用户数据表格
		 */
		 
	});
	//加载按钮表格
	function loadButtonDatagrid(menuCode) {
		button_datagrid = $('#button_datagrid').datagrid({
			url : '${ctx}/uums/button!showList.action',
			queryParams: {menuCode: menuCode},
			pagination: false,
			onLoadSuccess : function(data) {
				//加载完成后取消所有的已选择项
				$(this).datagrid('clearSelections');
			}
		});
	}
</script>
</body>
</html>
