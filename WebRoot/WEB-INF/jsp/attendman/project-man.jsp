<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>项目管理--用easyui画跨行列表格</title>
</head>
<body class="easyui-layout">

	<div data-options="region:'center',border:false">
		<table id="project_datagrid"></table>
	</div>
	
	<script type="text/javascript">
		var project_datagrid;
		$(function(){
			project_datagrid = $('#project_datagrid').datagrid({
				//toolbar: '#project_datagrid_toolbar',
				//url : '${ctx}/airforce/attendman/attendMan!showReportList.action',
				data: getData(),//模拟数据--加载本地数据
			    columns:[[
		              {field:'xmmc', title:'项目名称', rowspan:3},
		              {field:'syb', title:'所属事业部', rowspan:3},
		              {title:'事业部人月', colspan:4},
		              {title:'研发人月', colspan:4},
		              {field:'hjry', title:'合计人月', rowspan:3},
		              {field:'jhry', title:'计划人月', rowspan:3},
		              {field:'xmjl', title:'项目经理', rowspan:3},
		              {title:'成本', colspan:2},
		              {field:'zcb', title:'总成本<br/>（万元）', rowspan:3},
		              {field:'jhcb', title:'计划成本<br/>（万元）', rowspan:3},
		              {field:'jd', title:'阶段', rowspan:3}
				], [
					{title:'正式工', colspan:2},
					{title:'非正式工', colspan:2},
					{title:'正式工', colspan:2},
					{title:'非正式工', colspan:2},
					{field:'sybcb', title:'事业部成本<br/>（万元）', rowspan:2},
					{field:'yfcb', title:'研发成本<br/>（万元）', rowspan:2}
				], [
					{field:'syzszc', width:60, title:'正常'},
					{field:'syzsjb', width:60, title:'加班'},
					{field:'syfzszc', width:60, title:'正常'},
					{field:'syfzsjb', width:60, title:'加班'},
					{field:'yfzszc', width:60, title:'正常'},
					{field:'yfzsjb', width:60, title:'加班'},
					{field:'yffzszc', width:60, title:'正常'},
					{field:'yffzsjb', width:60, title:'加班'}
				]]
			});
			/**
			 * 模拟数据
			 */
			function getData() {
				var data = [];
				for (var i = 0; i < 12; i++) {
					data.push({
						xmmc: "项目名称列"+i,
						syb: "事业部"+i,
						hjry: "合计人月列"+i,
						jhry: "计划人月列"+i,
						xmjl: "项目经理列"+i,
						sybcb: "sybcb"+i,
						yfcb: "yfcb"+i,
						zcb: "总成本列"+i,
						jhcb: "计划成本列"+i,
						jd: "阶段列"+i,
						syzszc: "syzszc"+i,
						syzsjb: "syzsjb"+i,
						syfzszc: "syfzszc"+i,
						syfzsjb: "syfzsjb"+i,
						yfzszc: "yfzszc"+i,
						yfzsjb: "yfzsjb"+i,
						yffzszc: "yffzszc"+i,
						yffzsjb: "yffzsjb"+i
					});
				}
				return data;
			}
		});
	</script>
</body>
</html>