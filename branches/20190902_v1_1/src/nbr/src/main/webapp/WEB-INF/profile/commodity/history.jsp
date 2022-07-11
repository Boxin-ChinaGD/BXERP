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
	<link type="text/css" rel="stylesheet" href="css/bx_commodityHistory.css" />
	<title>BoXin-商品修改记录</title>
</head>

<body>
	<div id="commodityHistoryMain">
		<div class="commodityHistoryTop">
			<div>
				<label class="layui-form-label">操作人：<span id="operator"
						style="cursor: pointer;">所有&nbsp;v</span></label>
				<ul id="operatorList">
					<li value="-1">所有</li>
					<c:forEach items="${staffList}" var="staff">
						<c:if test="${staff.roleID == 1 || staff.roleID == 4}">
							<li value="${staff.ID}">${staff.name}</li>
						</c:if>
					</c:forEach>
				</ul>
<!-- 				<label class="layui-form-label">门店：<span id="shop" -->
<!-- 						style="cursor: pointer;">所有&nbsp;v</span></label> -->
<!-- 				<ul id="shopList"> -->
<!-- 					<li value="-1">所有</li> -->
<%-- 					<c:forEach items="${shopList}" var="shop"> --%>
<%-- 							<li value="${shop.ID}">${shop.name}</li> --%>
<%-- 					</c:forEach> --%>
<!-- 				</ul> -->
				<label class="layui-form-label">修改日期：<span id="updateDate"
						style="cursor: pointer;">全部&nbsp;v</span></label>
				<ul id="updateDateList">
					<li>全部</li>
					<li>今天</li>
					<li>七天</li>
					<li>三个月</li>
					<li>半年</li>
				</ul>
				<label class="layui-form-label">门店：<span id="shop"
						style="cursor: pointer;">所有&nbsp;v</span></label>
				<ul id="shopList">
					<li value="-1">所有</li>
					<c:forEach items="${shopList}" var="shop">
							<li value="${shop.ID}">${shop.name}</li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div class="commodityHistoryBottom">
			<div class="commodityHistoryBottomLeft layui-inline"
				style="width: 60%; display:inline-block; vertical-align: top;">
				<form class="layui-form">
					<input type="text" name="${CommodityHistoryField.FIELD_NAME_queryKeyword}"
						class="layui-input commodityHistorySearch" lay-verify="checkKeywordHaveCommodityName"
						placeholder="请输入商品名称或条形码搜索商品" title="请输入商品名称或条形码搜索商品" maxlength="64" />
					<i class="layui-icon layui-icon-search commodityHistorySearch" lay-submit
						lay-filter="commodityHistorySearch"></i>
				</form>
				<div class="commodityHistoryList">
					<table id="commodityHistoryList" lay-filter="commodityHistoryList">
						<script type="text/html" id="tableUpdateInfo">
							{{d.fieldName}}: {{d.oldValue}}  调整为: {{d.newValue}}<br>
						</script>
					</table>
				</div>
				<div id="packageUnits" style="margin-top: 20px; display: none; overflow-y: auto; overflow-x: auto;">
					<div class="layui-form-item" style="margin-bottom: 3px;">
						<h5>多包装基本单位 &nbsp;&nbsp; <span></span></h5>
					</div>
					<div>
						<table class="layui-table" lay-size="sm" lay-skin="nob" lay-even>
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
			<div class="commodityHistoryBottomRight layui-inline"
				style="width: 35%; display:inline-block; vertical-align: top;">
				<form class="layui-form" lay-filter="commodityDetails">
					<div class="layui-form-item">
						<label class="layui-form-label"><b>商品详情</b></label>
						<div class="layui-input-block">
							<input type="hidden" name="${CommodityHistoryField.FIELD_NAME_commodityID }"
								class="commodityID" />
							<input type="hidden" name="${CommodityHistoryField.FIELD_NAME_ID }" class="ID" />
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">条形码</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityHistoryField.FIELD_NAME_barcodes }"
									readonly="readonly" autocomplete="off" class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">品牌</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_brandName }" width="100"
									readonly="readonly" class="layui-input" style="width: 100px;">
							</div>
						</div>
					</div>
					<div id="otherBarcodes">
						<div class="layui-inline">
							<label class="layui-form-label">其他条形码</label>
							<div class="layui-input-inline" id="otherBarcodesList"></div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">名称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_name }" readonly="readonly"
									class="layui-input">
							</div>
						</div>
						<div class="layui-inline" style="">
							<label class="layui-form-label">规格</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_specification }"
									readonly="readonly" class="layui-input" style="width: 100px;">
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">简称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_shortName }" readonly="readonly"
									class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">保质期</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_shelfLife }" readonly="readonly"
									class="layui-input" style="width: 100px;">
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">分类</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_categoryName }" readonly="readonly"
									class="layui-input">
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">零售价</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_priceRetail }" readonly="readonly"
									class="layui-input" style="width: 100px;">
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline" style="height: 30px; margin-right: 0px;">
							<label class="layui-form-label">单位</label>
							<div class="layui-input-inline"
								style="width: 105px; margin-right: 0px; height: 30px; padding: 5px 0px 5px 15px;">
								<input type="text" name="${CommodityField.FIELD_NAME_packageUnit }" readonly="readonly"
									style="width: 30px;" />
								<span style="color: rgb(136, 136, 136);"><input type="checkbox" style="width: 20px;"
										lay-ignore id="units" />多单位</span>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">会员价</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_priceVIP }" readonly="readonly"
									class="layui-input" style="width: 100px;">
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline" style="margin-right: 0px; width: 242px;">
							<label class="layui-form-label">供应商</label>
							<div class="layui-input-block" style="width: 156px;">
								<input type="text" name="${CommodityField.FIELD_NAME_providerName }"
									class="providerName" readonly="readonly" autocomplete="off"
									style="width: 76px; padding: 5px 15px; padding-right: 0px;" />
								<a style="color: rgb(136, 136, 136); cursor: pointer;">更多</a>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label" style="padding-left: 7px;">批发价</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_priceWholesale }"
									readonly="readonly" class="layui-input" style="width: 100px;">
							</div>
						</div>
					</div>
					<div class="layui-form-item" id="otherProvider" style="display: none;">
						<div class="layui-inline">
							<label class="layui-form-label">其他供应商</label>
							<div class="layui-input-inline otherProviderList">
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">商品库存</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_NO }" readonly="readonly"
									autocomplete="off" class="layui-input">
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_commodityHistory.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
</body>

</html>