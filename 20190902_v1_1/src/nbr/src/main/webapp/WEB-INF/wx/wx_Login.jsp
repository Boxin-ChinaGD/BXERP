<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<base href="${pageContext.request.contextPath}/">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<META HTTP-EQUIV="expires" CONTENT="0">
		<link rel="bookmark" type="image/x-icon" href="bx.ico"/>
		<link rel="shortcut icon" href="bx.ico">
		<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
		<link type="text/css" rel="stylesheet" href="css/bx_wxAdminLogin.css" />
		<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_jsbn.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_prng4.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_rng.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_rsa.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_wx_adminLogin.js"></script>
		<title>博销宝-商家管理后台</title>
	</head>
	<body>
		<div id="loginMain">
			<div id="loginInfomation" class="layui-form">
				<h2>欢迎登录博销宝管理后台</h2>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong class="requiredField">*</strong>公司编号：</label>
					 <div class="layui-input-block">
				    	<input type="text" class="layui-input" name="${Staff.FIELD_NAME_companySN}" value="" lay-verify="required|checkCompanySN" placeholder="请输入公司编号" />
				    </div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong class="requiredField">*</strong>手机号码：</label>
					<div class="layui-input-block">
						<input type="text" class="layui-input" name="${Staff.FIELD_NAME_phone}" value="" lay-verify="required|checkPhone" maxlength="11" placeholder="请输入手机号码" />
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong class="requiredField">*</strong>密码：</label>
					<div class="layui-input-block">
						<input type="password" class="layui-input" name="${Staff.FIELD_NAME_salt}" value="" lay-verify="required|checkPassword" placeholder="请输入密码" /> 
					</div>
				</div>
				<input type="hidden" value="${identify}" name="identify">
				<input type="hidden" value="${ID}"  name="ID">
				<button class="layui-btn layui-btn-lg layui-btn-normal" lay-submit lay-filter="wxLogin">登录</button>
			</div>
		</div>
	</body>
</html>