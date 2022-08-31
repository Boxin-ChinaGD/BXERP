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
	<link type="text/css" rel="stylesheet" href="css/bx_warehouseStock.css" />
	<title>BoXin-库存查询</title>
</head>

<body>
	<div id="warehouseListMain">
		<div class="warehouseListTop">
			<div>
				<label class="layui-form-label">时间筛选：<span id="time" style="cursor: pointer;">所有&nbsp;v</span></label>
				<ul id="timeList">
					<li data-keyword="">所有</li>
					<li data-keyword="1">过去一周内</li>
					<li data-keyword="2">过去一月内</li>
					<li data-keyword="3">三个月内</li>
				</ul>
				<!-- 				    <label class="layui-form-label">门店及仓库：<span id="storeNwarehouse" style="cursor: pointer;">全部&nbsp;v</span></label> -->
				<!-- 				    <ul id="storeNwarehouseList"> -->
				<!-- 				    	<li>全部</li> -->
				<!-- 				    </ul> -->
			</div>
			<div>
				<label class="layui-form-label">门店：<span id="shop" data="门店">
						<lable>默认门店</lable>&nbsp;v
					</span></label>
				<ul id="shopList">
<!-- 					<li>所有</li> -->
<%-- 					<c:forEach items="${shopList}" var="shop"> --%>
<%-- 						<li><span>门店</span>${shop.name}<input type="hidden" --%>
<%-- 									value="${shop.ID}" /></li> --%>
<%-- 					</c:forEach> --%>
					<c:forEach items="${shopList}" var="shop">
							<li value="${shop.ID}">${shop.name}</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="warehouseListBottom">
			<div class="bottomLeft" id="categoryList">
				<div><span style="margin-right: 82px;">选择分类</span><span class="allCategoryShow">全部展开</span></div>
				<ul class="layui-nav layui-nav-tree">
					<li class="layui-nav-item layui-nav-itemed">
						<a href="javascript:void(0);" indexID="0">全部商品</a>
						<dl class="layui-nav-child">
							<dd class="layui-this"><a href="javascript:void(0);" indexID="-1">全部商品</a></dd>
						</dl>
					</li>
					<c:forEach items="${categoryParentList}" var="cpl">
						<li class="layui-nav-item">
							<a href="javascript:void(0);">${cpl.name}</a>
							<c:forEach items="${categoryList}" var="cl">
								<dl class="layui-nav-child">
									<c:if test="${cl.parentID == cpl.ID}">
										<dd><a href="javascript:void(0);" indexID="${cl.ID}">${cl.name}</a></dd>
									</c:if>
								</dl>
							</c:forEach>
						</li>
					</c:forEach>
				</ul>
			</div>
			<div class="bottomCenter">
				<div class="layui-form">
					<input type="text" name="${CommodityField.FIELD_NAME_queryKeyword}"
						class="layui-input commoditySearch" lay-verify="checkKeywordHaveCommodityName" maxlength="64"
						placeholder="请输入商品名称、简称、条形码或助记码搜索商品" title="请输入商品名称、简称、条形码或助记码搜索商品" />
					<i class="layui-icon layui-icon-search commoditySearch commoditySearchi" lay-submit
						lay-filter="commoditySearch"></i>
					<span style="float: right; line-height: 36px;">
						<input type="checkbox" lay-filter="stop" name="${CommodityField.FIELD_NAME_status}"
							title="不显示停用商品" lay-skin="primary">
						<input lay-filter="filter" type="checkbox" name="${CommodityField.FIELD_NAME_NO}"
							title="过滤零库存商品" lay-skin="primary">
					</span>
				</div>
				<div class="warehouseDetail">
					<table class="layui-table" id="commodityList" lay-filter="commodityList">

					</table>
				</div>
				<div id="packageUnits"
					style="margin-top: 20px; display: none; overflow-y: auto; width: 728px; overflow-x: auto;">
					<div class="layui-form-item" style="margin-bottom: 3px;">
						<h5>多包装基本单位 &nbsp;&nbsp; <span></span></h5>
					</div>
					<div>
						<table class="layui-table" lay-size="sm" lay-skin="nob" width="728" lay-even>
							<colgroup>
								<col width="80px">
							</colgroup>
							<thead>
								<tr class="multiPackageThead"></tr>
							</thead>
							<tbody>
								<tr class="multiPackagePriceRetail"></tr>
								<tr class="multiPackagePriceVIP"></tr>
								<tr class="multiPackagePriceWholesale"></tr>
								<tr class="multiPackageNO"></tr>
								<tr class="multiPackageBarcode"></tr>
								<tr class="multiPackageCommName"></tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="bottomRight">
				<form class="layui-form" lay-filter="commodityDetails">
					<div class="layui-form-item">
						<label class="layui-form-label">商品详情</label>
						<div class="layui-input-block">
							<input type="hidden" name="${CommodityField.FIELD_NAME_ID}" class="ID" />
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline" style="display: inline-block;">
							<label class="layui-form-label">条形码</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_barcodes}" readonly="readonly"
									autocomplete="off" class="layui-input" />
							</div>
						</div>
						<div class="layui-inline" style="display: inline-block; vertical-align: top;">
							<label class="layui-form-label">品牌</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_brandName}" width="100"
									readonly="readonly" autocomplete="off" class="layui-input" style="width: 100px;" />
							</div>
						</div>
					</div>
					<div id="otherBarcodes">
						<div class="layui-inline">
							<label class="layui-form-label" style="padding-left: 0px; width: 75px;">其他条形码</label>
							<div class="layui-input-inline" id="otherBarcodesList"></div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">名称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_name}" readonly="readonly"
									autocomplete="off" class="layui-input" />
							</div>
						</div>
						<div class="layui-inline" style="">
							<label class="layui-form-label">规格</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_specification}" readonly="readonly"
									autocomplete="off" class="layui-input" style="width: 100px;" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">简称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_shortName}" readonly="readonly"
									autocomplete="off" class="layui-input" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">保质期</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_shelfLife}" readonly="readonly"
									autocomplete="off" class="layui-input" style="width: 100px;" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">分类</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_categoryName}" readonly="readonly"
									autocomplete="off" class="layui-input" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">零售价</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_priceRetail}" readonly="readonly"
									autocomplete="off" class="layui-input" style="width: 100px;" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">单位</label>
							<div class="layui-input-block"
								style="width: 100px; margin-right: 0px; height: 20px; padding: 5px 20px;">
								<input type="text" name="${CommodityField.FIELD_NAME_packageUnit}" readonly="readonly"
									autocomplete="off" style="width: 30px;" />
								<span style="color: rgb(136, 136, 136);"><input type="checkbox" style="width: 20px;"
										lay-ignore id="units" />多单位</span>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">会员价</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_priceVIP}" readonly="readonly"
									autocomplete="off" class="layui-input" style="width: 100px;" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">供应商</label>
							<div class="layui-input-block" style="width: 130px;">
								<input type="text" name="${CommodityField.FIELD_NAME_providerName}" readonly="readonly"
									autocomplete="off" style="width: 76px; padding: 5px 15px; padding-right: 0px;" />
								<a style="color: rgb(136, 136, 136); display: none; cursor: pointer;">更多</a>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">批发价</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_priceWholesale}"
									readonly="readonly" autocomplete="off" class="layui-input" style="width: 100px;" />
							</div>
						</div>
					</div>
					<div class="layui-form-item" id="otherProvider" style="display: none;">
						<div class="layui-inline">
							<label class="layui-form-label" style="padding-left: 0px; width: 75px;">其他供应商</label>
							<div class="layui-input-inline otherProviderList">
							</div>
						</div>
					</div>
<!-- 					商品table添加添加了库存列 -->
<!-- 					<div class="layui-form-item"> -->
<!-- 						<div class="layui-inline"> -->
<!-- 							<label class="layui-form-label">商品库存</label> -->
<!-- 							<div class="layui-input-inline"> -->
<%-- 								<input type="text" name="${CommodityField.FIELD_NAME_NO}" readonly="readonly" --%>
<!-- 									autocomplete="off" class="layui-input"> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_warehouseStock.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
</body>

</html>