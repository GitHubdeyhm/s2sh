<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/easyuiMeta.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/ajax-pushlet-client.js" charset="utf-8"></script>
	<title>用户登录</title>
	<style type="text/css">
		.login-box {
			position: absolute;
			top: 230px;
			right: 180px;
			z-index: 100;
		}
		.login-box label {
			width: 50px;
			color: #fff;
		}
		.login-box input[type='text'], .login-box input[type='password'] {
			width: 208px;
			height: 30px;
			text-indent: 4px;
			border: 1px solid #29619A;
		}
		.login-box .btn {
			background-color: #2A7AAF;
			color: #fff;
			font-size: 24px;
			font-weight: bold;
			width: 210px;
			padding: 4px 0px;
			border: none;
			margin: 10px 0px;
			cursor: pointer;
		}
		.login-box .btn:hover {
			background-color: #29619A;
		}
		.input-check {
			line-height: 13px;
			margin: 6px 0;
		}
		.input-check label {
			vertical-align: top;
		}
		/********云朵背景*********/
		#cloudBG {
			width: 100%;
			height: 100%;
			position: relative;
		}
		.cloud_sun {
			position: absolute;
			top: 0px;/**太阳的位置*/
			left: 300px;
			background: url(${ctx}/image/common/sun.png) no-repeat ;
		}
		.cloud_sun img {
			margin:30px -90px;
			width: 350px;
		}
		.cloud_float .cloud {
			position: absolute;
			width: 560px;
			height: 240px;
			background: url(${ctx}/image/common/cloud.png) no-repeat;
			top: 100px;/*云朵的初始位置*/
			left: 80px;
			overflow: hidden;
		}
		.cloud_float .cloud1 {
			top: 92px;
			left: 228px;
		}
		.cloud_float .cloud2 {
			top: 340px;
			left: 38px;
		}
	</style>
	<script type="text/javascript">
		var userName = "${sessInfo.userName}";
		if (userName != "") {
			//location.href = "${ctx}/answer/paper.htm";
		}
	</script>
</head>
<body style="background:#5AB8EC url('${ctx}/image/common/cloud_bg.jpg') no-repeat;">
	<div id="container" class="clearfix" style="position:relative;">
		<div class="login-box">
			<form id="user_login_form" method="post">
				<ul class="formUL">
					<li style="text-align:left;">
						<div style="color:#FBFDFF;">用户名:</div>
						<input type="text" name="userName" value="administrator" placeholder="请输入用户名" id="userName_login"
							onkeypress="if(event.keyCode == 13) document.getElementById('password_login').focus();" />
					</li>
					<li style="text-align:left;">
						<div style="color:#FBFDFF;">密码:</div>
						<input type="password" name="userPwd" placeholder="请输入密码" id="password_login" 
							onkeypress="if(event.keyCode==13) login();" />
					</li>
					<li class="input-check">
						<input type="checkbox" id="remember_login" />
						<label for="remember_login">记住密码</label>
					</li>
					<li style="text-align:left;">
						<input type="button" class="btn" value="登 录" onClick="login()" />
					</li>
				</ul>
			</form>
		</div>
	</div>
	<!-- 背景图片 -->
	<div id="cloudBG">
		<!--云朵上的太阳-->
		<div class="cloud_sun">
			<img src="${ctx}/image/common/cloud.png" />
		</div>
		
		<!-- 两朵浮动的云 -->
		<div class="cloud_float">
			<div class="cloud cloud1" id="Jcloud1"></div>
			<div class="cloud cloud2" id="Jcloud2"></div>
		</div>
	</div>

<script type="text/javascript">

	//防止iframe嵌套
	if (window != top) {
		top.location.href = location.href;
	}
	$(function(){
		//页面加载完成聚焦第一个文本框
		$("#userName_login").focus();
		/**
		 * 云朵的移动
		 */
		(function() {
			var locationX1 = 340;//云朵的初始化位置
			var locationX2 = 92;
			var mainWidth = screen.width;//屏幕的宽度
			
			window.setInterval(function() {		
				if (locationX1 >= mainWidth) {
					locationX1 = -560;
				}
				if (locationX2 >= mainWidth) {
					locationX2 = -560;
				}
				$("#Jcloud1").css("left", locationX1 + "px");		
				$("#Jcloud2").css("left", locationX2 + "px");

				locationX1 += 1;
				locationX2 += 0.8;				
			}, 70);
		})();
		
		//回填密码
		$("#userName_login").blur(function(){
			$("#password_login").val(cookie($(this).val()));
		});
	});
	/**
	 * 登陆
	 */
	 function login() {
		var userName = $.trim($("#userName_login").val());
	    var pwd = $("#password_login").val();
	    if (userName == "" || pwd == "") {
	    	 et.showMsg("用户名或密码不能为空");
	    	 return;
	    }
		$.ajax({
			type : 'POST',
			url : '${ctx}/uums/user!login.action',
			dataType : 'json',
			data: {userName: userName, userPwd: pwd},
			success: function(data) {
				if (data.resultCode == 1) {
					//记住密码
					if (document.getElementById("remember_login").checked) {
						cookie(userName, pwd);
					}
					location.href = "${ctx}/index/index.action";
				} else {
					et.showMsg(data.msg);
				}
			}
		});
	}
	
	 function cookie(key, name) {
		 //key必须参数
		 if (key == undefined || $.trim(key) == "") {
			 return "";
		 }
		 //获取值
		 if (name == undefined) {
			var cookieStr = document.cookie;
			if (cookieStr) {
				var arr = cookieStr.split("; ");
				for (var i = 0; i < arr.length; i++) {
					var keyValue = arr[i].split("=");
					if (keyValue[0] == key) {
						return keyValue[1];
					}
				}
			}
			return "";
		 } else {
			 //设置值
			 document.cookie = key+"="+name+"; max-age=2592000";//60*60*24*30
		 }
	}
	// PL._init();   
	 //PL.setDebug(true);
    // PL.joinListen('/pushletEvnet');  
     //function onData(event) {  
      //   et.showMsg(event.get("name"));   
         // 离开  
         // PL.leave();  
    // }
</script>
</body>
</html>