<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<link rel="bookmark" type="image/x-icon" href="bx.ico" />
	<link rel="shortcut icon" href="bx.ico">
	<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_importData.css" />
	<title>博销宝-导入资料</title>
</head>

<body>
	<div id="importData">
		<p class="ImportFlowHead">导入资料流程</p>
		<div class='ImportFlow'>
			<ul>
				<li>1、点击下载导入模板。</li>
				<li>2、填写导入模板。</li>
				<li>3、在导入模板内点击“检查格式”按钮。如果资料有格式错误，会弹出错误提示，格式有错的单元格会被描红并加错误批注。</li>
				<li>4、按照批注修改资料，去掉批注和描红。</li>
				<li>5、重复第3步到第4步，直到点击“检查格式”按钮没有提示错误为止。</li>
				<li>6、点击“导入文件”按钮，导入资料。导入的文件名必须是：博销宝资料导入模板.xlsm。</li>
				<li>7、如果发生格式错误，点击下载“博销宝资料导入模板_错误批注.xslm”文件。服务器会批注和描红有错的单元格。</li>
				<li>8、根据“博销宝资料导入模板_错误批注.xslm”文件中的错误批注，修改资料的格式，去掉批注和描红。</li>
				<li>9、重复第6步到第8步，直到没有格式问题。如果有非格式的问题，要重新导入。重新导入后，服务器会对重复的项目添加批注和描红，可以忽略这种类型的错误。</li>
				<li>10、导入成功。</li>
			</ul>
		</div>
		<p class="ImportFlowHead">下载模板</p>
		<div class="head">
			<a href="/f/博销宝资料导入模板.xlsm" download="博销宝资料导入模板.xlsm"><i class="layui-icon layui-icon-download-circle"></i>
				<span>点击下载导入模板<span></a>

		</div>
		<p class="ImportFlowHead">导入文件</p>
		<div class="importFile">
			<button class="layui-btn" id='test'><i class="layui-icon layui-icon-upload-drag"></i>选择文件</button>
			<div class="layui-upload-list">
				<table class="layui-table">
					<thead>
						<tr>
							<th>文件名</th>
							<th>大小</th>
							<th>状态</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="demoList"></tbody>
				</table>
			</div>
			<button id='testListAction'>上传文件</button>
			<div class="layui-progress progress layui-progress-big " lay-filter="demo" lay-showPercent="true">
				<div class="layui-progress-bar layui-bg-green " id="progressCSS" lay-percent="0%"></div>
			</div>
		</div>
		<div class='Tips'>
			<div>
				<i class="layui-icon layui-icon-ok-circle"
					style="color:rgba(0,191,165,1);font-size:21px;"></i><span>资料导入成功</span>
			</div>
			<p>共需创建商品 <span class='commodityTotalToCreate'>0</span>个，商品格式错误为<span
					class='commodityWrongFormatNumber'>0</span>个。共需创建会员 <span class='vipTotalToCreate'>0</span>
				个，会员格式错误为<span class='vipWrongFormatNumber'>0</span>个。供应商总数为 <span
					class='providerTotalToCreate'>0</span> 个,
				供应商格式错误个数为<span class='providerWrongFormatNumber'>0</span>个。
		</div>
		<div class='Tips1'>
			<div>
				<i class="layui-icon layui-icon-about" style="color:#FFB300;font-size:21px;"></i><span>资料格式有误</span>
			</div>
			<p>共需创建商品 <span class='commodityTotalToCreate'>0</span>个，商品格式错误为<span
					class='commodityWrongFormatNumber'>0</span>个。共需创建会员 <span class='vipTotalToCreate'>0</span>
				个，会员格式错误为<span class='vipWrongFormatNumber'>0</span>个。供应商总数为 <span
					class='providerTotalToCreate'>0</span> 个,
				供应商格式错误个数为<span class='providerWrongFormatNumber'></span>个。
				<br> 可以点击<a class="formatError" href="" style="color:#2196F3;cursor:pointer;"
					download="博销宝资料导入模板_错误批注.xlsm"><u>下载修改.</u></a></p>
		</div>
		<div class='Tips2'>
			<div>
				<i class="layui-icon layui-icon-refresh-3" style="color:#F44336;font-size:21px;"></i><span>资料导入失败</span>
			</div>
			<p>共需创建商品 <span class='commodityTotalToCreate'>0</span>个，商品创建失败个数为<span
					class='commodityFailCreateNumber'>0</span>。共需创建会员 <span class='vipTotalToCreate'>0</span>
				个，会员创建失败个数为<span class='vipFailCreateNumber'>0</span>。</p>
			<p style='display:none;' class="otherErrorDownload">可以点击<a class="formatError" href=""
					style="color:#2196F3;cursor:pointer;" download="博销宝资料导入模板_错误批注.xlsm"><u>下载修改.</u></a></p>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_importData.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
</body>

</html>