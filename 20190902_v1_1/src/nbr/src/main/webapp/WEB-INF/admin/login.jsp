<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<base href="${pageContext.request.contextPath}/">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="expires" CONTENT="0">
<link rel="bookmark" type="image/x-icon" href="bx.ico" />
<link rel="shortcut icon" href="bx.ico">
<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
<link type="text/css" rel="stylesheet" href="css/bx_adminLogin.css" />
<script type="text/javascript" charset="utf-8"
	src="scripts/jquery.min.js"></script>
<script type="text/javascript"
	src="scripts/bx_sessionValidityPeriodTesting.js"></script>
<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
<script type="text/javascript" charset="utf-8" src="scripts/bx_jsbn.js"></script>
<script type="text/javascript" charset="utf-8" src="scripts/bx_prng4.js"></script>
<script type="text/javascript" charset="utf-8" src="scripts/bx_rng.js"></script>
<script type="text/javascript" charset="utf-8" src="scripts/bx_rsa.js"></script>
<script type="text/javascript" charset="utf-8"
	src="scripts/bx_adminLogin.js"></script>
<title>博销宝-门店电商平台</title>
</head>

<body>
<!-- 	<p> -->
<!-- 		<img src="../../images/loginBGP.png" alt="" class="loginBGP1"> -->
<!-- 	</p> -->
	<div id="loginMain">
		<div id="loginPic">
			<img src="../../images/login.png" alt="">
		</div>
		<div id="loginInfomation" class="layui-form">
			<p>
				<img src="../../images/name.png" alt="">
			</p>
			<h4 style="line-height:50px;">欢迎登录博销宝管理后台</h4>
			<h4 style="font-size:15px; line-height:16px; margin-bottom: 15px;">v1.0.0</h4>
			<div class="layui-form-item">
				<label class="layui-form-label"><strong
					class="requiredField">*</strong>公司编号：</label>
				<div class="layui-input-block">
					<input type="text" class="layui-input"
						name="${staffField.FIELD_NAME_companySN}" lay-verify="companySN"
						placeholder="请输入公司编号" maxlength="8" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label"><strong
					class="requiredField">*</strong>手机号码：</label>
				<div class="layui-input-block">
					<input type="text" class="layui-input"
						name="${staffField.FIELD_NAME_phone}" lay-verify="phone"
						maxlength="11" placeholder="请输入手机号码" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label"><strong
					class="requiredField">*</strong>密码：</label>
				<div class="layui-input-block">
					<input type="password" class="layui-input"
						name="${staffField.FIELD_NAME_pwdEncrypted}" lay-verify="password"
						maxlength="16" placeholder="请输入密码" />
				</div>
			</div>
			<p>
				<button class="layui-btn layui-btn-lg layui-btn-normal" lay-submit
					lay-filter="login">登录</button>
			</p>
			<p>
				<img src="../../images/logoName.png" alt="">
			</p>
			<input type="hidden" id="CURRENT_ReleaseNbrVersionNO" value="2.0.0" /><br>
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
<!-- 	<p> -->
<!-- 		<img src="../../images/loginBGP.png" alt="" class="loginBGP2"> -->
<!-- 	</p> -->
</body>
</html>