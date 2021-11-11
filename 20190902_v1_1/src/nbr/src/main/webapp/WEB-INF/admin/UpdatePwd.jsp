<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<base href="${pageContext.request.contextPath}/">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<META HTTP-EQUIV="expires" CONTENT="0">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
	<meta name="renderer" content="webkit">
	<link rel="bookmark" type="image/x-icon" href="bx.ico" />
	<link rel="shortcut icon" href="bx.ico">
	<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_UpdatePwd.css" />
	<title>博销宝管理后台</title>
</head>
<body>
	<div id="firstLoginToUpadtePwdMain">
		<div class="background">
			<input type="hidden" id="sessionPhone" value="${sessionScope.SESSION_Staff.phone}" />
			<input type="hidden" id="changePassword" value="${identify}"/>
			<div class="layui-form">
				<div class="title">修改密码</div>
				<hr width="100%">
				<div class="layui-card-body" style="position: relative;">
					<div class="layui-form-item">
						
						<div class="layui-input-block">
							<input type="password" class="layui-input" name="password" placeholder="请输入原密码" lay-verify="required|password|noChinese" maxlength="16" />
						</div>
					</div>
					<div class="layui-form-item">
						
						<div class="layui-input-block">
							<input type="password" class="layui-input" name="newPassword" placeholder="请输入新密码" lay-verify="required|password" maxlength="16" />
						</div>
					</div>
					<div class="layui-form-item">
						
						<div class="layui-input-block">
							<input type="password" class="layui-input" name="confirmNewPassword" placeholder="请确认新密码" lay-verify="required|password" maxlength="16" />
						</div>
					</div>
					<button class="layui-btn layui-btn-normal" lay-submit lay-filter="firstLoginToUpadtePwd">保存</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_jsbn.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_prng4.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rng.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rsa.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_UpdatePwd.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
</body>
</html>