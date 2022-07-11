<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<base href="${pageContext.request.contextPath}/">
	<META HTTP-EQUIV="expires" CONTENT="0">
	<link rel="bookmark" type="image/x-icon" href="bx.ico" />
	<link rel="shortcut icon" href="bx.ico">
	<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_common.css" />
	<title>BoXin-CMS</title>
</head>

<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<div id="BX_side" class="layui-side">
			<div class="iconArea">
				<ol>
					<li><i class="layui-icon layui-icon-templeate-1" title="门店"></i>
						<p>门店</p>
					</li>
				</ol>
				<button class="layui-btn layui-btn-primary layui-btn-sm bxStaffloginOut">退出</button>
			</div>
			<div class="catalog">
				<div class="BX_title">
					<span>
						<marquee direction="left" behavior="scroll">客户管理系统</marquee>
					</span>
				</div>
				<div class="storeManagement">
					<ul class="layui-nav layui-nav-tree" lay-filter="">
						<li class="layui-nav-item">
							<a href="JavaScript:void(0)">门店管理</a>
							<dl class="layui-nav-child">
								<dd><a href="JavaScript:void(0)" lay-href="company.bx">门店列表</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="cache/update.bx">更新缓存</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="company/createShop.bx">创建门店</a></dd>
							</dl>
						</li>
					</ul>
				</div>
			</div>
			<!-- <div class="spreadNav">
				<i class="layui-icon layui-icon-spread-left"></i>
			</div>
			<div class="shrinkNavDiv">
				<div class="shrinkNav">
					<i class="layui-icon layui-icon-shrink-right"></i>
				</div>
			</div> -->
		</div>
		<!-- 主体部分 -->
		<div id="BX_body" class="layui-body">
			<!-- 页面标签及主体部分 -->
			<div class="layadmin-pagetabs" id="bxui-topTap">
				<div class="layui-tab layui-tab-brief" lay-allowClose="true" lay-filter="topTap">
					<ul class="layui-tab-title">
						<li class="layui-this">首页</li>
					</ul>
					<div class="layui-tab-content">
						<div class="layui-tab-item layui-show">
							<iframe frameborder="0" scrolling="no" src=""></iframe>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_common.js"></script>
</body>

</html>