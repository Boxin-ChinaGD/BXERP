<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<base href="${pageContext.request.contextPath}/">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<META HTTP-EQUIV="expires" CONTENT="0">
	<link rel="bookmark" type="image/x-icon" href="bx.ico" />
	<link rel="shortcut icon" href="bx.ico">
	<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_bxAdminLogin.css" />
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_jsbn.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_prng4.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rng.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rsa.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_bxAdminLogin.js"></script>
	<title>BoXin-BX业务经理登录页</title>
</head>

<body>
	<div id="loginMain">
<!-- 		<img src="../../images/loginBGP.png" alt="" class="loginBGP"> -->
<!-- 		<img src="../../images/loginBGP.png" alt="" class="loginBGP1"> -->
		<div id="loginPic">
			<img src="../../images/login.png" alt="">
		</div>
		<div id="loginInfomation" class="layui-form">
			<p style="margin-top:20px;">
				<img src="../../images/name.png" alt="">
			</p>
			<h3 style="margin-top:10px;">欢迎来到博昕管理后台</h3>
			<div style="margin-top:15px;" class="layui-form-item">
				<label class="layui-form-label"><strong class="requiredField">*</strong>手机号码：</label>
				<div class="layui-input-block">
					<input type="text" class="layui-input" name="${BxStaffField.FIELD_NAME_mobile }" lay-verify="phone"
						maxlength="11" placeholder="请输入手机号码" />
				</div>
			</div>
			<div style="margin-top:15px;" class="layui-form-item">
				<label class="layui-form-label"><strong class="requiredField">*</strong>密码：</label>
				<div class="layui-input-block">
					<input type="password" class="layui-input" name="${BxStaffField.FIELD_NAME_salt }"
						lay-verify="password" maxlength="16" placeholder="请输入密码" />
				</div>
			</div>
			<p style="margin-top:20px;">
 				<button class="layui-btn layui-btn-lg layui-btn-normal" lay-submit lay-filter="login">登录</button>
			</p>
		</div>
	</div>
	<hr />
	<br />
	<br />
	<br />
	<br />
	<p class="Reserved">博销宝管理后台 版权所有：广州市博昕信息技术有限公司 Copyright 2014-2020 All Right Reserved</p>
	<br />
	<p class="Reserved">2014 2020 版权所有 广州市博昕信息技术有限公司 粤ICP备15067421号</p>
</body>

</html>