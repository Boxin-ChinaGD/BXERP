<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<link type="text/css" rel="stylesheet" href="css/bx_couponManage.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_popupPage.css" />
	<title>BoXin-优惠券管理</title>
</head>

<body>
	<div id="couponMain">
		<div class="couponList">
			<button type="button" class="layui-btn layui-btn-sm toCreateCoupon">
				<i class="layui-icon">&#xe654;</i> 新建优惠券
			</button>
			<!-- 			<div class="queryByKeyword"> -->
			<!-- 				<input class="layui-input" placeholder="请输入关键字查询" /> -->
			<!-- 				<i class="layui-icon layui-icon-search"></i> -->
			<!-- 			</div> -->
			<table id="couponTable" lay-filter="couponTable">
			</table>

		</div>
		<div class="couponDetail">
			<form class="layui-form" lay-filter="couponDetailForm">
				<input type="hidden" name="${CouponField.FIELD_NAME_ID}" />
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>名称</label>
					<div class="layui-input-block">
						<input type="text" name="${CouponField.FIELD_NAME_title}" class="layui-input"
							oninput="checkCouponTitle(this.value)" />
						<p class="form_tips">字数上限为9个,建议涵盖卡券属性、服务及金额</p>
						<p class="form_msg">优惠券名称只能输入中文、英文或数字</p>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>颜色</label>
					<div class="layui-input-block">
						<div class="couponColor"><span></span></div>
						<div id="colorPicker"></div>
						<input type="hidden" name="${CouponField.FIELD_NAME_color}" />
						<p class="form_tips">可点击以选择需要的颜色</p>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>状态</label>
					<div class="layui-input-block">
						<input type="radio" name="${CouponField.FIELD_NAME_status}" value="0" title="正常" checked
							disabled />
						<input type="radio" name="${CouponField.FIELD_NAME_status}" value="1" title="已终止" disabled />
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>优惠券类型</label>
					<div class="layui-input-block">
						<select name="${CouponField.FIELD_NAME_type}" lay-filter="couponType">
							<option value="0" seleted>代金券</option>
							<option value="1">折扣券</option>
						</select>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>起用金额</label>
					<div class="layui-input-block">
						<input type="text" name="${CouponField.FIELD_NAME_leastAmount}" class="layui-input"
							lay-verify="leastAmount" oninput="checkCouponLeastAmount(this.value)" />元
						<p class="form_msg">优惠券起用金额需大于等于0，只允许有2位小数</p>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>减免金额</label>
					<div class="layui-input-block">
						<input type="text" name="${CouponField.FIELD_NAME_reduceAmount}" class="layui-input"
							lay-verify="reduceAmount" oninput="checkCouponReduceAmount(this.value)" />元
						<p class="form_msg">优惠券减免金额需大于0，并小于起用金额，只允许有2位小数</p>
					</div>
				</div>
				<div class="layui-form-item" style="display: none;">
					<label class="layui-form-label"><strong>*</strong>打折额度</label>
					<div class="layui-input-block">
						<input type="text" name="${CouponField.FIELD_NAME_discount}" class="layui-input discount"
							oninput="checkCouponDiscount(this.value)" />折
						<p class="form_msg">优惠券打折额度的输入需大于0，小于10，只允许有2位小数</p>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>可用时段</label>
					<div class="layui-input-block">
						<input type="radio" name="chooseTime" value="0" title="全部时段" lay-filter="chooseTime" checked />
						<input type="radio" name="chooseTime" value="1" title="部分时段" lay-filter="chooseTime" />
						<div class="toChooseTime">
							<input type="checkbox" title="周一" value="32" lay-filter="week" checked />
							<input type="checkbox" title="周二" value="16" lay-filter="week" checked />
							<input type="checkbox" title="周三" value="8" lay-filter="week" checked />
							<input type="checkbox" title="周四" value="4" lay-filter="week" checked />
							<input type="checkbox" title="周五" value="2" lay-filter="week" checked />
							<input type="checkbox" title="周六" value="1" lay-filter="week" checked />
							<input type="checkbox" title="周日" value="64" lay-filter="week" checked />
							<input type="hidden" name="${CouponField.FIELD_NAME_weekDayAvailable}" value="127" />
							<div class="detailTime">
								<span>时间：</span>
								<div class="layui-inline">
									<div class="layui-input-inline">
										<input type="text" name="${CouponField.FIELD_NAME_beginTime}" id="beginTime"
											class="layui-input" />
									</div>
									<div class="layui-form-mid">至</div>
									<div class="layui-input-inline">
										<input type="text" name="${CouponField.FIELD_NAME_endTime}" id="endTime"
											class="layui-input" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>有效期</label>
					<div class="layui-inline  theDatetime">
						<div class="layui-input-inline">
							<input type="text" name="${CouponField.FIELD_NAME_beginDateTime}" id="beginDateTime"
								class="layui-input" />
							<input type="text" class="layui-input" />
						</div>
						<div class="layui-form-mid">至</div>
						<div class="layui-input-inline">
							<input type="text" name="${CouponField.FIELD_NAME_endDateTime}" id="endDateTime"
								class="layui-input" />
							<input type="text" class="layui-input" />
						</div>
						<p class="form_msg">有效期范围不正确，结束时间应晚于开始时间</p>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>需要多少积分兑换</label>
					<div class="layui-input-block">
						<input type="text" name="${CouponField.FIELD_NAME_bonus}" class="layui-input"
							oninput="checkCouponBonus(this.value)" />分
						<p class="form_tips">填0代表不支持使用积分兑换该优惠券</p>
						<p class="form_msg">输入的积分需为大于等于0的整数</p>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>每人可领券的数量限制</label>
					<div class="layui-input-block">
						<input type="text" name="${CouponField.FIELD_NAME_personalLimit}" class="layui-input"
							lay-verify="personalLimit" oninput="checkCouponPersonalLimitOrQuantity(this)" />张
						<p class="form_msg">设置的优惠券每人可领券数量需为大于等于1的整数</p>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>库存数量</label>
					<div class="layui-input-block">
						<input type="text" name="${CouponField.FIELD_NAME_quantity}" class="layui-input"
							lay-verify="NAME_quantity" oninput="checkCouponPersonalLimitOrQuantity(this)" />张
						<p class="form_msg">设置的优惠券库存数量需为大于等于1的整数</p>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>当前剩余库存数量</label>
					<div class="layui-input-block">
						<input type="text" name="${CouponField.FIELD_NAME_remainingQuantity}" class="layui-input" />张
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">使用说明</label>
					<div class="layui-input-block">
						<textarea name="${CouponField.FIELD_NAME_description}" class="layui-textarea"
							oninput="checkCouponDescription(this.value)"></textarea>
						<p class="form_msg">优惠券使用的字数长度需小于等于1024</p>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><strong>*</strong>参与的商品的范围</label>
					<div class="layui-input-block">
						<input type="radio" name="${CouponField.FIELD_NAME_scope}" value="0" title="全部商品"
							lay-filter="scope" checked />
						<input type="radio" name="${CouponField.FIELD_NAME_scope}" value="1" title="部分商品"
							lay-filter="scope" />
						<table class="layui-table chooseCommodity" lay-size="sm" lay-skin="nob" lay-even>
							<colgroup>
								<col width="60">
								<col width="130">
								<col width="130">
								<col width="80">
							</colgroup>
							<thead>
								<tr>
									<th>序号</th>
									<th>商品名称</th>
									<th>条码</th>
									<th>零售价</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<th>1</th>
									<th><i class="layui-icon layui-icon-add-circle addCommodity" title="添加商品"
											onclick="toChooseCommodity()"></i></th>
									<th></th>
									<th></th>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="layui-form-item">
					<button type="button" class="layui-btn layui-btn-sm terminateCoupon" lay-submit
						lay-filter="terminateCoupon">终止该优惠券的使用</button>
					<button type="button" class="layui-btn layui-btn-sm createCoupon" lay-submit
						lay-filter="createCoupon">提交并创建优惠券</button>
				</div>
			</form>
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
					lay-verify="checkKeywordHaveCommodityName" placeholder="请输入商品名称、简称、条形码或助记码搜索商品"
					title="请输入商品名称、简称、条形码或助记码搜索商品" maxlength="64" />
				<i class="layui-icon layui-icon-search commoditySearch" lay-submit lay-filter="commoditySearch"></i>
			</div>
			<table id="popupCommodityList" lay-filter="popupCommodityList">
				<script type="text/html" id="popupCommodityName">
 					<span style="color: #1A4A9F; cursor: pointer;">{{d.commodity.name}}/{{d.commodity.specification}}</span>
 				</script>
			</table>
			<div class="footArea">
				<span>你已选择了<strong>0</strong>种商品</span>
				<button class="layui-btn layui-btn-sm layui-btn-primary confirmExitComm">取消</button>
				<button class="layui-btn layui-btn-sm layui-btn-primary confirmChoosedComm">确定</button>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_couponManage.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_popupChooseCommodity.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
</body>

</html>