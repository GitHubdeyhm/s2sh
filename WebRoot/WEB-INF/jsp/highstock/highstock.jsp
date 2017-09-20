<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script src="${ctx}/jslib/jquery.1.7.2.js" type="text/javascript"></script>
	<script src="${ctx}/jslib/Highstock-2.1.4/js/highstock.js" type="text/javascript"></script>
	<script src="${ctx}/jslib/Highstock-2.1.4/js/highcharts-3d.js" type="text/javascript"></script>
	<title>系统设置</title>
	<style type="text/css">
		#plot-table {
			width: 1104px;
			margin: 20px auto;
		}
		#plot-table div {
			border: 1px solid blue;
		}
	</style>
</head>
<body>
	<table id="plot-table">
		<tr>
			<td>
				<div id="pieContainer" style="width:400px;height:320px;margin:auto;"></div>
			</td>
			<td>
				<div id="pie3DContainer" style="width:400px;height:320px;margin:auto;"></div>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div id="splineContainer" style="width:1000px;height:400px;margin:auto;"></div>
			</td>
		</tr>
	</table>
	
	
	<script type="text/javascript">
		$(function(){
			Highcharts.setOptions({                                                                              //  highchart图初始化
				global : {
					useUTC : false
				}
			});
			//初始化加载饼图
			var pie1 = loadPieChart("pieContainer", getRandomPieData());
			//初始化加载3D饼图
			var pie3D1 = loadPie3DChart("pie3DContainer", getRandomPieData());
			//加载动态时间折线图
			var spline1 = loadSplineChart("splineContainer", getRandomSpline());
			
			//console.log(pie1);
			//alert(pieChart.title.textStr);
			var i = 0;
			window.setInterval(function(){
				//pieChart.setTitle({text:i+i+"动态设置饼图标题"+i+i});
				//模拟图形的动态数据
				pie1.series[0].setData(getRandomPieData());
				
				pie3D1.series[0].setData(getRandomPieData());
				
				spline1.series[0].addPoint(getRandomSpline(), true, true);
				//加载饼图
			}, 2000);
		});
		/**
		 * 加载饼图，需传入div的ID，返回当前饼图对象
		 */
		function loadPieChart(id, data) {
			var pieChart = new Highcharts.Chart({
				title: {text: '饼图标题'},//图表标题
				credits: {enabled: false},//禁用版权提示信息
				chart: {
					renderTo: id,//容器div的ID选择器
					type: 'pie', //图表类型--绘制饼图
					plotBackgroundColor: null,//图形区域整体的背景颜色 
					plotBorderWidth: 2, //绘制图形区域整体边框宽度
					margin: [40, 10, 10, 10],//绘图区域与div之间的外边距
				},
				tooltip: {
					 pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'//单个点格式化--百分比
				},
				plotOptions: { 
					pie: {
						center: ['30%', '45%'],
						size: 150,//饼图直径
						cursor: 'pointer',//设置移上去鼠标手型
						allowPointSelect: true,//允许点击事件--点击那块饼图分离
						showInLegend: true,//显示图例
						borderWidth: 1,//图形边框宽度
						borderColor: '#B6CC8C',//图形的边框颜色 
						shadow: {width:12, offsetX: 2, offsetY: 1, color:'#CCC0CE'},//设置饼图阴影效果
						point: {
							events: {
								//图例点击事件
								legendItemClick: function(e){
									return false; //直接 “return false”即可禁用图例点击事件
								}
							}
						},
						dataLabels: {
							enabled: true, //是否启用数据标签
							color: '#000000',//数据信息颜色
							distance: -32,//数据提示信息与饼图之间的距离
							formatter: function() {
								return parseFloat(this.percentage).toFixed(1)+"%";//保留一位小数
							}
						}
					}
				},
				legend: {
					 backgroundColor: '#fff',//图例整体的背景色
					 borderWidth: 1,//图例整体边框宽度
					 borderColor: '#CDCCC8',//图例整体边框颜色
					 layout: 'vertical',//图例的排列方式--值是： “horizontal”， “vertical”。默认“horizontal”
					 floating: false,//设置浮动为true后，图例将不占位置 
					 align: 'right',//图例在图表中的对齐方式，有 “left”, "center", "right"，默认“center”
					 verticalAlign: 'middle',//水平对齐方式
					 x: -12,//偏移位置
					 //图例内容格式化函数
					 labelFormatter: function(){
						 return this.name + '('+this.y+')';
						 //console.log(this);//此方法查看对象详细信息
					 }
				},
				series: [{
					name: "数据列",//代表数据列的名字，并且会显示在数据提示框（Tooltip）及图例（Legend）中。 
					data: data
				}]
			});
			return pieChart;
		}
		/**
		 * 加载3D饼图，需传入div的ID
		 */
		function loadPie3DChart(id, data) {
			var pie3DChart = new Highcharts.Chart({
				title: {text: '3D饼图标题'},//图表标题
				credits: {enabled: false},//禁用版权提示信息
				chart: {
					renderTo: id,//容器div的ID选择器
					type: 'pie', //图表类型--绘制饼图
					plotBackgroundColor: null,//图形区域整体的背景颜色 
					plotBorderWidth: 2, //绘制图形区域整体边框宽度
					margin: [40, 10, 10, 10],//绘图区域与div之间的外边距
					options3d: {
						enabled: true,//启用3d效果
						alpha: 40,
						beta: 0
					}
				},
				tooltip: {
					 pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'//单个点格式化--百分比
				},
				plotOptions: { 
					pie: {
						center: ['30%', '45%'],
						size: 150,//饼图直径
						cursor: 'pointer',//设置移上去鼠标手型
						allowPointSelect: true,//允许点击事件--点击那块饼图分离
						showInLegend: true,//显示图例
						borderWidth: 1,//图形边框宽度
						borderColor: '#B6CC8C',//图形的边框颜色 
						depth: 35,//3d特性
						point: {
							events: {
								//图例点击事件
								legendItemClick: function(e){
									return false; //直接 “return false”即可禁用图例点击事件
								}
							}
						},
						dataLabels: {
							enabled: true, //是否启用数据标签
							color: '#000000',//数据信息颜色
							distance: -35,//数据提示信息与饼图之间的距离
							formatter: function() {
								return parseFloat(this.percentage).toFixed(1)+"%";//保留一位小数
							}
						}
					}
				},
				legend: {
					 backgroundColor: '#fff',//图例整体的背景色
					 borderWidth: 1,//图例整体边框宽度
					 borderColor: '#CDCCC8',//图例整体边框颜色
					 layout: 'vertical',//图例的排列方式--值是： “horizontal”， “vertical”。默认“horizontal”
					 floating: false,//设置浮动为true后，图例将不占位置 
					 align: 'right',//图例在图表中的对齐方式，有 “left”, "center", "right"，默认“center”
					 verticalAlign: 'middle',//水平对齐方式
					 x: -12,//偏移位置
					 //图例内容格式化函数
					 labelFormatter: function(){
						 //return this.name + '(' + Highcharts.numberFormat(this.y, 1) + ')';
						 return this.name + '('+this.y+')';
						 //console.log(this);//此方法查看对象详细信息
					 }
				},
				series: [{
					name: "数据列名称",//代表数据列的名字，并且会显示在数据提示框（Tooltip）及图例（Legend）中。 
					data: data
					/*data: [
					    //['火狐浏览器', 45.0], ['IE浏览器', 55.0]
					    ['谷歌浏览器', 12.8],
					    {name:'火狐浏览器', y: 32.2, color: '#A4FC48', selected: true, sliced: true},//默认选中且分离
					    {name:'IE浏览器', y: 55.0, color: '#679AF5'}
					]*/
				}]
			});
			return pie3DChart;
		}
		/**
		 * 动态时间折线图，需传入div的ID
		 */
		function loadSplineChart(id, data) {
			var splineChart = new Highcharts.Chart({
				title: {text: '动态时间折线图标题'},//图表标题
				credits: {enabled: false},//禁用版权提示信息
				chart: {
					renderTo: id,//容器div的ID选择器
					type: 'spline', //图表类型--绘制动态时间折线图
					animation: Highcharts.svg, //旧IE浏览器不支持动画
					plotBackgroundColor: null,//图形区域整体的背景颜色 
					plotBorderWidth: 1, //绘制图形区域整体边框宽度
					//margin: [40, 10, 10, 10],//绘图区域与div之间的外边距
					events: {
						//加载完成后触发
						load: function(){
							/*var series = this.series[0]; 
							setInterval(function() { 
								var x = (new Date()).getTime(), // current time 
								y = Math.random();
								series.addPoint([x, y], true, true); 
							}, 1000);*/
						}
					}
				},
				tooltip: {
					//格式化提示框
					 formatter: function() {
						 return '<b>'+ this.series.name +'：</b>'+ 
						 Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) +'<br>y轴的值：'+ 
						 Highcharts.numberFormat(this.y, 1); 
					 }
				},
				xAxis: {
					type: 'datetime',
					tickPixelInterval: 120 
				},
				yAxis: {
					min: 0,//y轴的刻度最小值
					title: {text: null},
					plotLines: [{
						value: 0,
						width: 1,
						color: '#CECFCA'//标示线的颜色
					}]
				},
				series: [{
					name: "数据列",//代表数据列的名字，并且会显示在数据提示框（Tooltip）及图例（Legend）中。 
					marker: {
						enabled: false//是否显示折线的点
					},
					data: (function() {
						var data = [], time = (new Date()).getTime(), i; 
						for (i = -19; i <= 0; i++) { 
							data.push({ x: time + i * 1000, y: 0 }); 
						} 
						return data; 
					})()
				}]
			});
			return splineChart;
		}
		/**
		 * 模拟随机数据饼图，需传入拼图对象
		 */
		function getRandomPieData() {
			var rand = parseFloat((Math.random()*40).toFixed(1));
			var rand1 = parseFloat((Math.random()*60).toFixed(1));
			var rand2 = parseFloat((Math.random()*40).toFixed(1));
			//alert(rand+"==="+rand1+"==="+rand2);
			var google = {name: "谷歌浏览器", y: rand};
			var firefox = {name:"火狐浏览器", y: rand1, color: '#A4FC48', selected: true, sliced: true};//默认选中且分离
			var ie = {name: "IE浏览器", y: rand2, color: '#679AF5'};
			var data = [google , firefox, ie];
			return data;
		}
		function getRandomSpline() {
			var data = [];
			data.push((new Date()).getTime());
			data.push(Math.random());
			return data;
		}
		
		
		/**
		 * 加载折线图
		 * @param x 数组类型，x轴数据
		 * @param data 数组类型，图表数据
		 * @param title 图标标题
		 */
		function loadDialogChart(id, x, data, title) {
			var code = data[0].name;//指标编码
			//图标单位
			var chartUnit = enumIndexUnit(code);
			var yUnit = chartUnit ? '（单位：'+chartUnit+'）' : '';
			var chartType = monitorChartType[code];//图表类型
			
			var gjChart = new Highcharts.Chart({
				credits: {enabled: false},//禁用版权提示信息
				chart: {
					renderTo: id,//容器div的ID选择器
					type: chartType, //图表类型--绘制动态时间折线图
					animation: Highcharts.svg, //旧IE浏览器不支持动画
					backgroundColor: 'rgba(0,0,0,0)',//背景透明
					events: {
						click: function(event) {//图标点击事件
							if (id != "gj_chart_big") {
								$("#gj_chart_big_wrap").prev("div").css("display", "block");
						    	$("#gj_chart_big_wrap").css("display", "block");
								var divObj = $("#"+id);
								var dateUnit = divObj.prev().find("td.kSelect").eq(0).attr("name");//得到点击表格的日期单位
						    	var indexTabel = $("#gj_chart_big").prev();
						    	indexTabel.attr("name", code);//设置指标
						    	indexTabel.find("td").each(function(){
						    		if ($(this).attr("name") == dateUnit) {
						    			$(this).addClass("kSelect").siblings('td').removeClass("kSelect");
						    			return false;
						    		}
						    	});
						    	$("#gj_chart_big_title").text(divObj.parent().prev().children(".kTitle").text());//标题
						    	loadDialogChart("gj_chart_big", x, data, title);
							}
						}
					},
					style: {
						cursor: (id == "gj_chart_big") ? 'default' : 'pointer'
					}
				},
				title: {text: null},
				subtitle: {
					align: 'left',
	           		text: yUnit,
	           		style: {
	                    color: '#30A0C2',//颜色
	                    cursor: 'default',
	                    fontSize:'12px'  //字体
	                }
	        	},
				xAxis: {
					categories: x,
					lineColor: '#084968', 		// 轴本身的线的颜色
	            	gridLineColor: '#084968', 	// 绘图区网格线的颜色
	            	tickColor: '#084968',		// 主刻度的颜色
	            	labels: {
		                style: {
		                    color: '#30A0C2',//颜色
		                    fontSize:'12px'  //字体
		                },
		                formatter: function(){
		                	if (x.length > 16) {
		                		if (this.value % 2 == 0) {
		                			return this.value;
		                		} else {
		                			return '';
		                		}
		                	} else {
		                		return this.value;
		                	}
		                }
	            	}
				},
				yAxis: {
					min: 0,//y轴的刻度最小值
					lineColor: '#084968', 		// 基线颜色
					lineWidth: 1,//y轴线宽度
	            	gridLineColor: '#084968', 	// 网格颜色
	            	tickPixelInterval: 42,//设置y轴标示线的间隔长度
	            	title: null,
	            	labels: {
		                style: {
		                    color: '#30A0C2',//颜色
		                    fontSize:'12px'  //字体
		                }
	            	}
				},
				plotOptions:{
	        		column:{
	        			borderWidth: 0,// 去边框
	                    colorByPoint: true //允许不同x轴点显示不同颜色
	        		},
	        		areaspline: {
	                    stacking: 'normal',
	                    /neColor: data[0].color,//线的颜色
	                    lineWidth: 3,
	                    marker: {
	        				radius: 3,// 点大小
	                        fillColor: '#FFFFFF' // 点填充色
	                    }
	                }
	        	},
				legend: {enabled: false},//不显示图例
				tooltip: {
					backgroundColor: '#1A2A43',
					style: {
						color: '#fff',//颜色
						padding: '3px 2px 0px 4px'
					},
					formatter: function() {//格式化提示框
						if (this.y > 0) {
	        				return this.y.toFixed(5);
			        	} else {
			        		return "0";//对于0需要以字符串格式返回
				        }
					}
				},
				series: data//数据
			});
			return gjChart;
		}
	</script>
</body>
</html>