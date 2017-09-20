<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>出错了</title>
	<style type="text/css">
		#container {
			width: 600px;
			margin: 40px auto 0px auto;
		}
		img {
			width: 130px;
			height: 130px;
			float: left;
		}
	</style>
</head>
<body>
	<div id="container">
		<img alt="出错了" src="${ctx}/images/common/errorCode.png" />
		<span style="margin:20px 0px 0px 0px;">很抱歉，您访问的链接不存在。</span>
	</div>
</body>
</html>