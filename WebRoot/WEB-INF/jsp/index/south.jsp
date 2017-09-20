<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div style="text-align:center;padding-top:4px;">
	<span>当前时间：</span>
	<span id="currentTime"></span>
</div>
<script type="text/javascript">
	var currentEle = document.getElementById("currentTime");
	/**
	 * 获取当前时间
	 */
	window.setInterval(
		function() {
			var time = formateTime(new Date());
			currentEle.innerHTML = time;
		}, 1000
	);
</script>