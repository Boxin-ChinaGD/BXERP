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
	<link type="text/css" rel="stylesheet" href="css/bx_QRcode.css" />
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_QRcode.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_jsbn.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_prng4.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rng.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rsa.js"></script>
	<title>微信二维码</title>
</head>

<body>
	<div id="codeMain">
		<div class="layui-form">
			<input type="text" class="layui-input QRwidth" lay-verify="QRwidth" placeholder="请输入生成二维码图片的宽度"
				title="请输入生成二维码图片的宽度" />
			<button class="layui-btn" lay-submit lay-filter="createQRcode"><i class="layui-icon layui-icon-edit"></i>
				生成二维码</button>
			<a href="" download="商家二维码.jpg" class="downloadQRcode"><i class="layui-icon layui-icon-download-circle"></i>
				下载二维码</a>
		</div>
		<div class="showQRcode">
			<div class="theQRcode">
				<img src="">
			</div>

		</div>
	</div>
</body>

</html>