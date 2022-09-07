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
	<link type="text/css" rel="stylesheet" href="css/bx_promotionList.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_popupPage.css" />
	<title>满减优惠</title>
</head>

<body>
	<div id="promotionMain">
		<div class="topNav layui-form topNav_head">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">活动状态：<span><b>所有</b>&nbsp;v</span></label>
					<ul>
						<li>所有<input type="hidden" value="-1" /></li>
						<li>未开始<input type="hidden" value="10" /></li>
						<li>进行中<input type="hidden" value="11" /></li>
						<li>已结束<input type="hidden" value="12" /></li>
						<li>已终止<input type="hidden" value="1" /></li>
					</ul>
				</div>
			</div>
		</div>
		<div id="left-Area">
			<div class="layui-form search">
				<input type="text" value="CX" name="${PromotionField.FIELD_NAME_queryKeyword}" class="layui-input"
					placeholder="请输入活动名称或单号(大于等于10位)搜索促销活动" title="请输入活动名称或单号(大于等于10位)搜索促销活动"
					onkeyup="instantSearch(this, event)" maxlength="32" />
				<i class="layui-icon layui-icon-search promotionSearch" style="font-size: 20px;" lay-submit
					lay-filter="promotionSearch"></i>
			</div>
			<table id="promotionList" lay-filter="promotionList" class="promotionList">
				<script type="text/html" id="promotionName">
					<span style="color: #2a7eed; cursor: pointer;">{{d.name}}</span>
				</script>
			</table>
		</div>
		<div id="right-Area" class="layui-form" lay-filter="promotionInfo">
			<div class="fixedButtonArea">
				<button class="layui-btn layui-btn-sm promotionManage createPromotion">新建</button>
				<button class="layui-btn layui-btn-sm promotionManage updatePromotion" lay-submit
					lay-filter="toCreatePromotion">保存</button>
				<button class="layui-btn layui-btn-sm promotionManage">取消</button>
				<button class="layui-btn layui-btn-sm promotionManage toStopPromotion">终止促销</button>
			</div>
			<div class="promotionInfo">
				<h3><b>基本信息</b></h3>
				<input type="hidden" id="sessionStaffID" value="${sessionScope.SESSION_Staff.ID}" />
				<input type="hidden" id="sessionStaffName" value="${sessionScope.SESSION_Staff.name}" />
				<input type="hidden" name="${PromotionField.FIELD_NAME_ID}" class="promotionID" />
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">创建人</label>
						<div class="layui-input-inline">
							<input type="text" class="layui-input" name="${PromotionField.FIELD_NAME_staff}"
								readOnly="readOnly" />
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label"><strong class="requiredField">*</strong>活动名称</label>
						<div class="layui-input-inline">
							<input type="text" class="layui-input promotionName" autocomplete="off"
								name="${PromotionField.FIELD_NAME_name}" readOnly="readOnly" maxlength="32"
								lay-verify="required" onchange="check_ifDataChange(this,this.value);" maxlength="32"
								style="width: 274px" />
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label"><strong class="requiredField">*</strong>活动时间</label>
						<div class="layui-input-inline">
							<input type="text" id="datetimeStart" class="layui-input"
								name="${PromotionField.FIELD_NAME_datetimeStart}" disabled="disabled"
								lay-verify="required" autocomplete="off" />
						</div>
						<div class="layui-form-mid">至</div>
						<div class="layui-input-inline">
							<input type="text" id="datetimeEnd" class="layui-input"
								name="${PromotionField.FIELD_NAME_datetimeEnd}" disabled="disabled"
								lay-verify="required" autocomplete="off" />
						</div>
					</div>
				</div>
				<b>活动规划</b>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label"><strong class="requiredField">*</strong>优惠门槛</label>
						<div class="layui-input-inline">
							<span>满</span>
							<input type="text" class="layui-input" autocomplete="off" onfocus="excecutionThresholdCheck(this)"
								name="${PromotionField.FIELD_NAME_excecutionThreshold}" readOnly="readOnly"
								lay-verify="required|pNumberPar" onchange="check_ifDataChange(this,this.value);" />
							<span>元</span>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label"><strong class="requiredField">*</strong>优惠内容</label>
						<div class="layui-input-inline">
							<span>减</span>
							<input type="text" class="layui-input cashReducingOfAmount" autocomplete="off"
								name="${PromotionField.FIELD_NAME_excecutionAmount}" readOnly="readOnly"
								lay-verify="pNumberPar" onchange="check_ifDataChange(this,this.value);" />
							<span>元</span>
							<input type="radio" name="${PromotionField.FIELD_NAME_type}" lay-ignore value="0"
								disabled="disabled" />
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label"></label>
						<div class="layui-input-inline">
							<span>打</span>
							<input type="text" class="layui-input discountOfAmount" autocomplete="off"
								name="${PromotionField.FIELD_NAME_excecutionDiscount}" readOnly="readOnly"
								lay-verify="pNumberPar" onchange="check_ifDataChange(this,this.value);" />
							<span>折</span>
							<input type="radio" name="${PromotionField.FIELD_NAME_type}" lay-ignore value="1"
								disabled="disabled" onchange="check_ifDataChange(this,this.value);" />
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label"><strong class="requiredField">*</strong>活动商品</label>
						<div class="layui-input-inline">
							<div>
								<span class="promotionScope">全部商品可用</span>
								<input type="radio" class="allCommodity" name="${PromotionField.FIELD_NAME_scope}"
									lay-ignore value="0" disabled="disabled"
									onchange="check_ifDataChange(this,this.value);" />
							</div>
							<div>
								<span class="promotionScope">指定商品可用</span>
								<input type="radio" class="designatedCommodity"
									name="${PromotionField.FIELD_NAME_scope}" lay-ignore value="1" disabled="disabled"
									onchange="check_ifDataChange(this,this.value);" />
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label"><strong class="requiredField">*</strong>活动门店</label>
						<div class="layui-input-inline">
							<div>
								<span class="promotionShopScope">全部门店可用</span>
								<input type="radio" class="allShop" name="${PromotionField.FIELD_NAME_shopScope}"
									lay-ignore value="0" disabled="disabled"
									onchange="check_ifDataChange(this,this.value);" />
							</div>
							<div>
								<span class="promotionShopScope">指定门店可用</span>
								<input type="radio" class="designatedShop"
									name="${PromotionField.FIELD_NAME_shopScope}" lay-ignore value="1" disabled="disabled"
									onchange="check_ifDataChange(this,this.value);" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="layui-form-item designatedCommodityList">
				<div class="layui-inline">
					<b>指定商品列表</b>
					<div></div>
				</div>
				<div class="designatedCommodityTable">
					<table id="commodityList" lay-filter="commodityList" class="layui-table" lay-size="sm"
						lay-skin="nob" lay-even="true">
						<thead>
							<tr>
								<th>序号</th>
								<th>商品名称</th>
								<th>条形码</th>
								<th>零售价</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="layui-form-item designatedShopList">
				<div class="layui-inline">
					<b>指定门店列表</b>
					<div></div>
				</div>
				<div class="designatedShopTable">
					<table id="shopList" lay-filter="shopList" class="layui-table" lay-size="sm"
						lay-skin="nob" lay-even="true">
						<thead>
							<tr>
								<th>序号</th>
								<th>门店名称</th>
<!-- 								<th>条形码</th> -->
<!-- 								<th>零售价</th> -->
							</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="toChooseComm" class="popupPage">
		<div class="leftRegion">
			<div class="topArea">
				<h3>选择商品</h3>
				<span>选择分类</span>
				<span class="showAllCommCategory">全部展开</span>
			</div>
			<ul class="layui-nav layui-nav-tree" lay-filter="generalCommRNByCategory">
				<li class="layui-nav-item layui-nav-itemed">
					<a href="javascript:void(0);" indexID="0">全部商品</a>
					<dl class="layui-nav-child">
						<dd class="layui-this"><a href="javascript:void(0);" indexID="-1">全部商品</a></dd>
					</dl>
				</li>
				<c:forEach items="${categoryParentList}" var="cpl">
					<li class="layui-nav-item">
						<a href="javascript:void(0);" indexID="${cpl.ID}">${cpl.name}</a>
						<dl class="layui-nav-child">
							<c:forEach items="${categoryList}" var="cl">
								<c:if test="${cl.parentID == cpl.ID}">
									<dd><a href="javascript:void(0);" parentID="${cl.parentID}"
											indexID="${cl.ID}">${cl.name}</a></dd>
								</c:if>
							</c:forEach>
						</dl>
					</li>
				</c:forEach>
			</ul>
			<button class="layui-btn layui-btn-sm layui-btn-primary"
				onclick="pageJumping('commodity.bx', 'commodity.bx', '商品列表')">新建商品</button>
		</div>
		<div class="rightRegion">
			<div class="topArea layui-form">
				<input type="text" name="${CommodityField.FIELD_NAME_queryKeyword}" class="layui-input"
					onkeyup="instantSearch(this)" lay-verify="checkKeywordHaveCommodityName"
					placeholder="请输入商品名称、简称、条形码或助记码搜索商品" title="请输入商品名称、简称、条形码或助记码搜索商品" maxlength="64" />
				<i class="layui-icon layui-icon-search commoditySearch" lay-submit lay-filter="commoditySearch"></i>
			</div>
			<table id="popupCommodityList" lay-filter="popupCommodityList">
				<script type="text/html" id="popupCommodityName">
 					<span style="color: #1A4A9F; cursor: pointer;">{{d.commodity.name}}/{{d.commodity.specification}}</span>
 				</script>
				<script type="text/html" id="popupNumManage">
 					<div class="popupNumManage">
						<button class="layui-btn layui-btn-xs layui-btn-normal reduceNum" onclick="popupNumManage(this, 'reduceNum')" type="button">-</button>
						<input type="text" class="layui-input popupCommNum" style="text-align: center;" value="" onchange="popupNumManage(this)" />
						<button class="layui-btn layui-btn-xs layui-btn-normal addNum" onclick="popupNumManage(this, 'addNum')" type="button">+</button>
						<i style="display: none;">{{d.commodity.ID}}</i>
					</div>
 				</script>
			</table>
			<div class="footArea">
				<span>你已选择了<strong>0</strong>种商品</span>
				<button class="layui-btn layui-btn-sm layui-btn-primary confirmExitComm">取消</button>
				<button class="layui-btn layui-btn-sm layui-btn-primary confirmChoosedComm">确定</button>
			</div>
		</div>
	</div>
	
		<!-- 		弹出门店表 -->
	<div id="chooseShopWindow" class="popupPage">
		<div class="leftRegion">
			<div class="topArea">
				<h3>选择门店</h3>
				<span>选择区域</span>
				<span class="showAllShopDistrict">全部关闭</span>
			</div>
			<ul class="layui-nav layui-nav-tree" lay-filter="providerDistrict">
				<li class="layui-nav-item layui-nav-itemed">
					<a href="javascript:void(0);">门店区域</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="JavaScript:void(0);">全部门店<input type="hidden" value="0"></a>
						</dd>
						<c:forEach items="${shopDistrictList}" var="sd">
							<dd>
								<a href="JavaScript:void(0);">${sd.name}<input type="hidden" value="${sd.ID}" /></a>
							</dd>
						</c:forEach>
					</dl>
				</li>
			</ul>
<!-- 			<button class="layui-btn layui-btn-sm layui-btn-primary" -->
<!-- 				onclick="pageJumping('commodity/about.bx', 'commodity/about.bx', '商品相关')">新建供应商</button> -->
		</div>
		
		<div class="rightRegion">
			<div class="topArea layui-form">
				<input type="text" name="${ShopField.FIELD_NAME_queryKeyword}" class="layui-input"
					id="shopinput" lay-verify="shopSearch" maxlength="32" onkeyup="instantSearch(this)"
					placeholder="请输入门店名称搜索门店" title="请输入门店名称搜索门店" />
				<i class="layui-icon layui-icon-search" id="seekShop_icon" lay-submit lay-filter="shopSearch"></i>
			</div>
			<table id="popupShopList" lay-filter="popupShopList">
<!-- 				<script type="text/html" id="shopDemo"> -->
<!--   					<i class="layui-icon layui-icon-ok-circle shopDemo" lay-event="choose" style="cursor: pointer;"></i> -->
<!-- 				</script> -->
				<script type="text/html" id="popupShopName">
 					<span style="color: #1A4A9F; cursor: pointer;">{{d.name}}</span>
 				</script>
				<script type="text/html" id="popupNumManage">
 					<div class="popupNumManage">
						<button class="layui-btn layui-btn-xs layui-btn-normal reduceNum" onclick="popupNumManage(this, 'reduceNum')" type="button">-</button>
						<input type="text" class="layui-input popupCommNum" style="text-align: center;" value="" onchange="popupNumManage(this)" />
						<button class="layui-btn layui-btn-xs layui-btn-normal addNum" onclick="popupNumManage(this, 'addNum')" type="button">+</button>
						<i style="display: none;">{{d.commodity.ID}}</i>
					</div>
 				</script>
			</table>
		</div>
		<div style="position: absolute; right: 20px; bottom: 2px;width: 713px;" class="shopFootArea">
			<span style="margin-right: 450px;color:rgb(33, 150, 243);">你已选择了<strong style="color:red">0</strong>个门店</span>
			<button class="layui-btn layui-btn-sm layui-btn-primary confirmChoosedShop">确定</button>
			<button class="layui-btn layui-btn-sm layui-btn-primary exitChoosedShop">取消</button>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_promotionList.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_popupChooseCommodity.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_popupChooseShop.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
</body>

</html>