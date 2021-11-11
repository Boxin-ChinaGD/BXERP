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
		<link type="text/css" rel="stylesheet" href="css/bx_wxBind.css" />
		<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
		<script type="text/javascript" src="scripts/bx_fieldFormat.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_wxBind.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_jsbn.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_prng4.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_rng.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_rsa.js"></script>
		<title>博销宝-零售门店运营系统</title>
	</head>
	<body>
		<div id="bindMain">
			<div id="bindInfomation" class="layui-form">
				<h2>绑定门店</h2>
				<input type="hidden" name="${staffField.FIELD_NAME_openid}" value='${openid}'>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong class="requiredField">*</strong>公司编号：</label>
					<div class="layui-input-block">
						<input type="text" class="layui-input" name="${staffField.FIELD_NAME_companySN}" lay-verify="required|checkCompanySN" placeholder="请输入公司编号" maxlength="8" />
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong class="requiredField">*</strong>手机号码：</label>
					<div class="layui-input-block">
						<input type="text" class="layui-input" name="${staffField.FIELD_NAME_phone}" lay-verify="required|checkPhone" maxlength="11" placeholder="请输入手机号码" />
					</div>
				</div>
 				<div class="layui-form-item">
 					<label class="layui-form-label"><strong class="requiredField">*</strong>密码：</label> 
 					<div class="layui-input-block">
						<input type="password" class="layui-input" name="${staffField.FIELD_NAME_pwdEncrypted}" lay-verify="required|checkPassword" maxlength="16" placeholder="请输入密码" /> 
 					</div> 
 				</div> 
				<button class="layui-btn layui-btn-lg layui-btn-normal" lay-submit lay-filter="bind">绑定</button>
			</div>
		</div>
	</body>
</html>