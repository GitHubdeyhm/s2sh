<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp" %>

<style type="text/css">
	.menu-accordion ul li a {
		display: block;
		padding: 7px 0px 7px 18px;
		font-size: 16px;
		cursor: pointer;
	}
	.menu-accordion ul li a:hover {
		color: #0081C2
	}
	.menu-accordion ul li.selected a {
		color: #fff;
		background-color: #0081C2;
	}
</style>

<div id="nav_menu" class="menu-accordion"></div>

<script type="text/javascript">
	$(function(){
		/**
		 * 左侧导航菜单
		 */
		$.ajax({
			type: "POST",
			url: "${ctx}/uums/menu!showPrivilMenus.action?enableStr=1",
			dataType: "json",
			success: function(data) {
				if (data != null && data != "") {
					var menuLen = 3;//菜单的长度
					var menu1 = [], menu2 = [];//分别保存第一级节点和第二级节点
					//区分出第一级节点和第二级节点
					for (var i = 0; i < data.length; i++) {
						var menu = data[i];
						var code = menu.menuCode;
						if (code.length == menuLen) {
							menu1.push(menu);
						} else {
							menu2.push(menu);
						}
					}
					var menuStr = '';
					for (var i = 0; i < menu1.length; i++) {
						menuStr += '<div title="'+menu1[i].menuName+'"><ul>';
						for (var j = 0; j < menu2.length; j++) {
							var code = menu2[j].menuCode;
							if (menu1[i].menuCode == code.substring(0, code.length-menuLen)) {
								var url = menu2[j].menuUrl ? menu2[j].menuUrl : '';
		        				menuStr += '<li><a onclick="loadMenuContent(\''+url+'\')">'+menu2[j].menuName+'</a></li>';
							}
						}
						menuStr += '</ul></div>';
					}
	        		$("#nav_menu").html(menuStr).accordion({
						animate: true,
						border: true,
						fit: true
					});
	        		//添加点击样式
	        		$("#nav_menu li").click(function(){
	        			$("#nav_menu li.selected").removeClass("selected");
	        			$(this).addClass("selected");
	        		});
					//$("#nav_menu").accordion("select", 0);//默认选中第一个
				 } else {
					 $.messager.alert('无权限', '抱歉，您没有任何访问权限，请联系管理员分配权限。');
				 }
			 }
		 });
	});
	
	//加载菜单内容
	function loadMenuContent(url) {
		if (url) {
			$("#main_frame").attr("src", "${ctx}/"+url);
		} else {
			et.showMsg("无效的连接地址");
		}
	}
</script>
