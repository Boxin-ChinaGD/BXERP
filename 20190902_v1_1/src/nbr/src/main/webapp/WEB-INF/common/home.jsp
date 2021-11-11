<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<base href="${pageContext.request.contextPath}/">
	<META HTTP-EQUIV="expires" CONTENT="0">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
	<meta name="renderer" content="webkit">
	<link rel="bookmark" type="image/x-icon" href="bx.ico" />
	<link rel="shortcut icon" href="bx.ico">
	<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_home.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_warehousingManage.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_popupPage.css" />
	<title>BoXin-首页</title>
</head>

<body>
	
	<div id="homeMain">
		<p class="head_tips">上午好，亲爱的朋友！工作之余记得让眼睛休息一下哦！</p>
		
		<div class="warehousingManageTop">
			<div>
				<label class="layui-form-label">门店：<span>默认门店&nbsp;v</span></label>
				<ul class="UlShopID" id="shop" style="display: none;">
				</ul>
			</div>
		</div>

		<div class="salesOverviewOfTheDay">
			<h5></h5>
			<div class="totalAmount">
				<label>销售总额</label><br><span></span><b>元</b>
			</div>
			<div class="totalNO">
				<label>零售单数</label><br><span></span><b>单</b>
			</div>
		</div>
		<div class="salesTrends">
			<h5>销售走势:</h5>
			<hr />
			<div id="sell"></div>
		</div>
		<div class="convenientEntrance">
			<p>快速选择</p>
			<ul>
				<li onclick="pageJumping('commodity.bx', 'commodity.bx', '商品列表')">
					<i class="layui-icon layui-icon-templeate-1"></i>
					<span>新建商品</span>
				</li>
				<li onclick="pageJumping('promotion.bx', 'promotion.bx', '满减满折')">
					<i class="layui-icon layui-icon-list"></i>
					<span>新建促销活动</span>
				</li>
				<li onclick="pageJumping('purchasingOrder/retrieveN.bx', 'purchasingOrder/retrieveN.bx', '采购订单列表')">
					<i class="layui-icon layui-icon-carousel"></i>
					<span>新建采购订单</span>
				</li>
				<li onclick="pageJumping('warehousing.bx', 'warehousing.bx', '入库')">
					<i class="layui-icon layui-icon-form"></i>
					<span>新建入库单</span>
				</li>
				<li
					onclick="pageJumping('warehousing/returnPurchasingCommodity.bx', 'warehousing/returnPurchasingCommodity.bx', '采购退货')">
					<i class="layui-icon layui-icon-tabs"></i>
					<span>新建退货单</span>
				</li>
				<li onclick="pageJumping('inventorySheet.bx', 'inventorySheet.bx', '盘点')">
					<i class="layui-icon layui-icon-chart-screen"></i>
					<span>新建盘点单</span>
				</li>
				<li onclick="pageJumping('coupon/index.bx', 'coupon/index.bx', '优惠券管理')">
					<i class="layui-icon layui-icon-note"></i>
					<span>新建优惠券</span>
				</li>
				<li onclick="pageJumping('vip/memberManagement.bx', 'vip/memberManagement.bx', '会员管理')">
					<i class="layui-icon layui-icon-user"></i>
					<span>会员管理</span>
				</li>
			</ul>
		</div>
		<div class="footer">
			<hr class="layui-bg-black">
			<div class="footerText">
				<p>版权所有: 广州市博昕信息技术有限公司 Copyright © 2015~<span></span> All Rights Reserved
				<p>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" src="BXUI/layui.js"></script>
	<script type="text/javascript" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" src="scripts/bx_home.js"></script>
	<script type="text/javascript" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" src="scripts/bx_format.js"></script>
	<script type="text/javascript" src="scripts/echarts.min.js"></script>
</body>

</html>