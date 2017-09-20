<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<title>easyui框架系统</title>
</head>
<body class="easyui-layout">
	<!-- 浏览器未开启脚本的提示信息 -->
	<noscript>
		<div style="position:absolute;z-index:1000;top:40px;left:400px;background:#fff;">
			<img src="${ctx}/image/common/errorCode.png" />
			<span>抱歉，请开启脚本支持！</span>
		</div>
	</noscript>
	
	<div data-options="region:'north',border:false" href="${ctx}/index/index!north.action" 
		style="height:24px;background:#B3DFDA;">
	</div>

	<div data-options="region:'center',title:'easyui框架系统',border:false" style="overflow:hidden;">
		<iframe name="mainFrame" id="main_frame" frameborder="0" style="width:100%;height:100%;padding:0;"></iframe>
	</div>
	
	<div data-options="region:'west',border:false,title:'菜单选项',iconCls:'icon-save'" 
		href="${ctx}/index/index!west.action" style="width:180px;">
	</div>

	<div data-options="region:'south',border:false"	style="background-color:#B3DFDA;height:26px;overflow:hidden;">
		<div style="text-align:center;line-height:24px;vertical-align:middle;">
			<span>当前时间：</span>
			<span id="currentTime"></span>
		</div>
	</div>
	
<script type="text/javascript">
	var currentEle = document.getElementById("currentTime");
	/**
	 * 获取当前时间
	 */
	function getCurrentTime() {
		var time = formateTime(new Date());
		currentEle.innerHTML = time;
	}
	getCurrentTime();
	window.setInterval("getCurrentTime()", 1000);
</script>
</body>
</html>