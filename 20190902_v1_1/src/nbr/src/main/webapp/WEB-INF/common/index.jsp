<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<link type="text/css" rel="stylesheet" href="css/bx_common.css" />
	<title>博销宝管理后台</title>
</head>

<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<!-- 头部部分 -->
		<input type="hidden" id="sessionPhone" value="${sessionScope.SESSION_Staff.phone}" />
		<!-- 边导航部分 -->
		<div id="BX_side" class="layui-side">
			<div class="iconArea">
				<div class="companyLogo"><img src="${sessionScope.SESSIONCompany.logo}" alt=""></div>
				<ol>
					<li><i class="layui-icon layui-icon-cart-simple"></i>
						<p>管货</p>
					</li>
					<li><i class="layui-icon layui-icon-rmb"></i>
						<p>管销</p>
					</li>
					<li><i class="layui-icon layui-icon-chart-screen"></i>
						<p>管帐</p>
					</li>
					<li><i class="layui-icon layui-icon-app"></i>
						<p>基础资料</p>
					</li>
					<li><i class="layui-icon layui-icon-diamond"></i>
						<p>会员相关</p>
					</li>
					<li><i class="layui-icon layui-icon-set-fill"></i>
						<p>设置</p>
					</li>
				</ol>
				<button class="layui-btn layui-btn-primary layui-btn-sm loginOut">退出</button>
			</div>
			<div class="catalog">
				<div class="BX_title">
					<span>
						<marquee direction="left" behavior="scroll">${sessionScope.SESSIONCompany.name}</marquee>
					</span>
				</div>
				<div class="barrelBolt">
					<ul class="layui-nav layui-nav-tree" lay-filter="">
						<li class="layui-nav-item">
							<a href="JavaScript:void(0)"><i class="layui-icon layui-icon-cols"></i> 促销</a>
							<dl class="layui-nav-child">
								<dd><a href="JavaScript:void(0)" lay-href="promotion.bx">满减优惠</a></dd>
							</dl>
						</li>
					</ul>
				</div>
				<div class="cargoManagement">
					<ul class="layui-nav layui-nav-tree" lay-filter="">
						<li class="layui-nav-item">
							<a href="JavaScript:void(0)"><i class="layui-icon layui-icon-list"></i> 采购</a>
							<dl class="layui-nav-child">
								<dd><a href="JavaScript:void(0)" lay-href="purchasingOrder/retrieveN.bx">采购订单列表</a></dd>
							</dl>
						</li>
						<li class="layui-nav-item">
							<a href="JavaScript:void(0)"><i class="layui-icon layui-icon-slider"></i> 库管</a>
							<dl class="layui-nav-child">
								<dd><a href="JavaScript:void(0)" lay-href="warehouse/warehouseStock.bx">库管查询</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="warehousing.bx">入库</a></dd>
								<dd><a href="JavaScript:void(0)"
										lay-href="warehousing/returnPurchasingCommodity.bx">采购退货</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="inventorySheet.bx">盘点</a></dd>
							</dl>
						</li>
					</ul>
				</div>
				<div class="keepAccounts">
					<ul class="layui-nav layui-nav-tree" lay-filter="">
						<li class="layui-nav-item">
							<a href="JavaScript:void(0)"> <i class="layui-icon layui-icon-cols"></i> 报表</a>
							<dl class="layui-nav-child">
								<dd><a href="JavaScript:void(0)"
										lay-href="warehousingReport/operatingStatement.bx">经营状况</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="retailTrade.bx">销售记录</a></dd>
							</dl>
						</li>
					</ul>
				</div>
				<div class="basicProfile">
					<ul class="layui-nav layui-nav-tree" lay-filter="">
						<li class="layui-nav-item">
							<a href="JavaScript:void(0)"> <i class="layui-icon layui-icon-date"></i> 商品资料</a>
							<dl class="layui-nav-child">
								<dd><a href="JavaScript:void(0)" lay-href="commodity.bx">商品列表</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="commodity/about.bx">商品相关</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="commodity/toImportData.bx">导入资料</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="commodityHistory.bx">修改记录</a></dd>
							</dl>
						</li>
					</ul>
				</div>
				<div class="vipRelated">
					<ul class="layui-nav layui-nav-tree" lay-filter="">
						<li class="layui-nav-item">
							<a href="JavaScript:void(0)"> <i class="layui-icon layui-icon-cols"></i> 会员与积分</a>
							<dl class="layui-nav-child">
								<dd><a href="JavaScript:void(0)" lay-href="vip/memberManagement.bx">会员管理</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="vip/bonusHistory.bx">积分历史</a></dd>
								<dd><a href="JavaScript:void(0)" lay-href="vip/vipCardManage.bx">会员卡管理</a></dd>
							</dl>
						</li>
						<li class="layui-nav-item">
							<a href="JavaScript:void(0)"> <i class="layui-icon layui-icon-slider"></i> 优惠券</a>
							<dl class="layui-nav-child">
								<dd><a href="JavaScript:void(0)" lay-href="coupon/index.bx">优惠券管理</a></dd>
							</dl>
						</li>
						<li class="layui-nav-item">
							<dl>
								<a href="JavaScript:void(0)"
									onclick="pageJumping('miniprogram/QRCodeToCreate.bx', 'miniprogram/QRCodeToCreate.bx', '微信小程序二维码');"><i
										class="layui-icon layui-icon-dialogue"></i> 微信小程序二维码</a>
							</dl>
						</li>
					</ul>
				</div>
				<div class="installation">
					<ul class="layui-nav layui-nav-tree" lay-filter="">
						<li class="layui-nav-item">
							<a href="JavaScript:void(0)"><i class="layui-icon layui-icon-util"></i> 员工资料</a>
							<dl class="layui-nav-child">
								<dd><a href="JavaScript:void(0)" lay-href="staff.bx">员工管理</a></dd>
							</dl>
						</li>
						<li class="layui-nav-item">
							<dl>
								<a href="JavaScript:void(0)"
									onclick="pageJumping('home/updateMyPwd.bx?lastLocation=3', 'home/updateMyPwd.bx', '修改密码');"><i
										class="layui-icon layui-icon-edit"></i> 修改密码</a>
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
			<!-- 内容主体区域 -->
			<div class="layui-tab layui-tab-brief" lay-allowClose="true" lay-filter="topTap">
				<ul class="layui-tab-title">
					<li class="layui-this">首页</li>
				</ul>
				<div class="layui-tab-content">
					<div class="layui-tab-item layui-show">
						<iframe frameborder="0" src="home/home.bx"></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_common.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
</body>

</html>