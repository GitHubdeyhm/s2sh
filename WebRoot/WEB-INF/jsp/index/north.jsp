<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>

<div style="text-align:right;">
	<span>在线人数：${applicationScope.count}</span>
	<a href="${ctx}/uums/user!logout.action" class="easyui-linkbutton" iconCls="icon-back">退出系统</a>
</div>