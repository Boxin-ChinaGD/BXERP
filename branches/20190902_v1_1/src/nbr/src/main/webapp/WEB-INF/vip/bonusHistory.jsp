<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="${pageContext.request.contextPath}/">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="0">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
<meta name="renderer" content="webkit">
<link rel="bookmark" type="image/x-icon" href="bx.ico"/>
<link rel="shortcut icon" href="bx.ico">
<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
<link type="text/css" rel="stylesheet" href="css/bx_bonusHistory.css" />
<title>BoXin-积分历史</title>
</head>
<body>
	<div id="bonusHistoryMain">
		<div class="bonusHistory">
			<form class="layui-form queryByKeywordArea">
				<input type="text" class="layui-input" name="keyWord" placeholder="请输入手机号码、微信昵称进行搜索" maxlength="32" />
				<i class="layui-icon layui-icon-search" lay-submit lay-filter="queryByKeyword"></i>
			</form>
			<table id="bonusHistoryTable" lay-filter="bonusHistoryTable">
			</table>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_bonusHistory.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
</body>
</html>