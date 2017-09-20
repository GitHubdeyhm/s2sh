<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>周值班表</title>
	<style type="text/css">
		#container table {
			border-collapse: collapse;
			border: 1px solid #0081C2;
		}
		#container table td {
			border: 1px solid #0081C2;
			text-align: center;
			width: 150px;
			padding: 4px 0px;
		}
	</style>
</head>
<body>
	<div id="container">
		<div class="search-toolbar">
			<form action="">
				<label>值班日期</label>
				<input type="text" id="dutyTime_search" />
				
				<a href="javascript:search();" class="easyui-linkbutton" iconCls="icon-search" plain="true">查 询</a>
			</form>
		</div>
		
		<!-- 值班表格 -->
		<table id="duty-table" style="border:1px solid red;"></table>
	</div>
	
	<script type="text/javascript">
		//一周
		var weeks = ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"];
		$(function(){
			var today = formateTime(new Date()).split(" ")[0];
			$("#dutyTime_search").datebox({
				editable: false,
				value: today//默认今天
			});
			
			search();
		});
		/**
		 * 重新加载表格
		 */
		function loadDutyTable(weeksDay) {
			//var weeksDay = getWeeksByDay(dateStr);
			var dutyStr = '<caption>周值班表('+weeksDay[0]+'到'+weeksDay[6]+')</caption>';
			var row1 = '<tr><td></td>';
			var row2 = '<tr><td>值班领导</td>';
			var row3 = '<tr><td>值班干部人员</td>';
			var row4 = '<tr><td>门卫</td>';
			for (var i = 0; i < weeks.length; i++) {
				row1 += '<td>'+weeks[i]+'('+weeksDay[i]+')'+'</td>';
				row2 += '<td class="'+weeksDay[i]+'"></td>';//以日期作为当前表格列的class，通过class就可以得到该列
				row3 += '<td class="'+weeksDay[i]+'"></td>';
				row4 += '<td class="'+weeksDay[i]+'"></td>';
			}
			row1 += '</tr>';
			row2 += '</tr>';
			row3 += '</tr>';
			row4 += '</tr>';
			dutyStr = dutyStr + row1 + row2 + row3 + row4;
			$("#duty-table").html(dutyStr);
		}
		/**
		 * 设置表格的数据
		 */
		function getDutyData(data) {
			if (data) {
				var dutyTable = $("#duty-table");
				for (var i = 0; i < data.length; i++) {
					var duty = data[i];
					var tds = dutyTable.find("."+duty.dutyTime);//通过class得到列
					tds.eq(0).html(duty.leader);//领导
					tds.eq(1).html(duty.cadres);//干部
					tds.eq(2).html(duty.guard);//门卫
				}
			}
		}
		/**
		 * 查询
		 */
		function search() {
			var dateStr = $("#dutyTime_search").datebox("getValue");
			var weeksDay = getWeeksByDay(dateStr);
			$.ajax({
				type: "POST",
				url: "${ctx}/attendman/duty!showDutyTable.action",
				dataType: "json",
				data: {dateStr: weeksDay.join(",")},
				success: function(data) {
					loadDutyTable(weeksDay);
					getDutyData(data);
				}
			});
		}
	</script>
</body>
</html>