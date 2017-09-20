<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>出错啦</title>
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
		<span style="margin:20px 0px 0px 0px;">很抱歉，服务器端出现错误，我们会尽快解决问题。</span>
	</div>
</body>
</html>