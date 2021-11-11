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
	<link type="text/css" rel="stylesheet" href="css/bx_warehousingManage.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_popupPage.css" />
	<title>BoXin-入库单列表</title>
</head>

<body>
	<div id="warehousingManageMain">
		<div>
			<input type="hidden" id="sessionStaffID" value="${sessionScope.SESSION_Staff.ID}" />
			<input type="hidden" id="sessionStaffName" value="${sessionScope.SESSION_Staff.name}" />
		</div>
		<div class="warehousingManageTop">
			<div>
				<label class="layui-form-label">门店：<span>所有&nbsp;v</span></label>
				<ul class="UlShopID">
					<li data-keyword="-1">所有</li>
					<c:forEach items="${shopList}" var="shop">
						<li data-keyword="${shop.ID}">${shop.name}</li>
					</c:forEach>
				</ul>
				<label class="layui-form-label">状态：<span>所有&nbsp;v</span></label>
				<ul class="UlStatus">
					<li data-keyword="-1">所有</li>
					<li data-keyword="0">未审核</li>
					<li data-keyword="1">已审核</li>
				</ul>
				<label class="layui-form-label">经办人：<span>所有&nbsp;v</span></label>
				<ul class="UlStaffID">
					<li data-keyword="-1">所有</li>
					<c:forEach items="${staffList}" var="staff">
						<c:if test="${staff.roleID == 1 || staff.roleID == 4}">
							<li data-keyword="${staff.ID}">${staff.name}</li>
						</c:if>
					</c:forEach>
				</ul>
				<label class="layui-form-label">供应商：<span>所有&nbsp;v</span></label>
				<ul class="UlProviderID">
					<li data-keyword="-1">所有</li>
					<c:forEach items="${providerList}" var="pl">
						<li data-keyword="${pl.ID}">${pl.name}</li>
					</c:forEach>
				</ul>
				<label class="layui-form-label">创建日期：<span>所有&nbsp;v</span></label>
				<ul class="UlDate">
					<li data-keyword="all">所有</li>
					<li data-keyword="aWeekAgo">过去一周内</li>
					<li data-keyword="aMonthAgo">过去一月内</li>
					<li data-keyword="threeMonthsAgo">三个月内</li>
				</ul>
				<!-- 				<label class="layui-form-label">采购仓库：<span>默认仓库&nbsp;v</span></label> -->
				<!-- 				<ul class="UlWarehouse"> -->
				<!-- 			    	<li>默认仓库</li> -->
				<!-- 			    </ul> -->
				<div class="warehousingManageCenter">
					<input type="text" name="${WarehousingField.FIELD_NAME_queryKeyword}" class="layui-input" value="RK"
						placeholder="请输入商品名称、供应商名称、单号或采购订单号(大于等于10位)搜索入库单" title="请输入商品名称、供应商名称、单号或采购订单号(大于等于10位)搜索入库单"
						maxlength="32" />
					<i class="layui-icon layui-icon-search queryWarehousingSheet"></i>
				</div>
			</div>
		</div>
		<div class="warehousingMainInfo">
			<div class="warehousingSheetList">
				<ul id="warehousingList"></ul>
				<div id="warehousingListPage"></div>
			</div>
			<div class="warehousingSheetInfo" align="left">
				<input type="hidden" class="warehousingSheetID" />
				<div class="buttonArea">
					<button class="layui-btn layui-btn-sm warehousingManage btnChoosed">新建</button>
					<button class="layui-btn layui-btn-sm warehousingManage">保存</button>
					<button class="layui-btn layui-btn-sm warehousingManage">删除</button>
					<button class="layui-btn layui-btn-sm warehousingManage">取消</button>
				</div>
				<div class="baseInfo">
					<p>
						<span><b class="createDatetime"></b>/<b>入库单</b></span>
						<span>采购仓库：默认仓库</span>
					</p>
					<p>
						<span class="theProvider">供应商：<span class="providerName"></span>
							<input type="hidden" class="providerID" />
							<i class="layui-icon layui-icon-add-circle addProvider" title="选择供应商"
								onclick="toAddProvider()"></i>
						</span>
						<span class="theShop">门店：<span class="shopName"></span>
							<input type="hidden" class="shopID" />
							<i class="layui-icon layui-icon-add-circle addShop" title="选择门店"
								onclick="toAddShop()"></i>
						</span>
						<span>采购订单号：<b class="purchasingOrderSN"></b></span>
						<span>入库单号：<b class="warehousingSheetSN"></b></span>
					</p>
				</div>
				<div class="warehousingCommodityTable">
					<div class="tableHeaderDescription">
						<table class="layui-table" lay-even lay-size="sm" lay-skin="nob">
							<colgroup>
								<col width="50">
								<col width="150">
								<col width="130">
								<col width="100">
								<col width="100">
								<col width="100">
								<col>
							</colgroup>
							<thead>
								<tr>
									<th>编号</th>
									<th>商品条码</th>
									<th>商品名称</th>
									<th>包装单位</th>
									<th>数量</th>
									<th>进货价(元)</th>
									<th>金额(元)</th>
								</tr>
							</thead>
						</table>
					</div>
					<div class="warehousingCommodityList">
						<table class="layui-table" lay-even lay-size="sm" lay-skin="nob">
							<colgroup>
								<col width="50">
								<col width="150">
								<col width="130">
								<col width="100">
								<col width="100">
								<col width="100">
								<col>
							</colgroup>
							<tbody></tbody>
						</table>
					</div>
				</div>
				<div class="amountTotalAndStatus">
					<span class="createStaff"></span>
					<span class="amountTotal">合计：<strong></strong></span><br>
					<button class="layui-btn layui-btn-sm warehousingManage">审核</button>

				</div>
			</div>
		</div>
		<div id="toChooseCommodity" class="popupPage">
			<div class="leftRegion">
				<div class="topArea">
					<h3>选择商品</h3>
					<span>选择分类</span>
					<span class="showAll">全部展开</span>
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
				<button class="layui-btn layui-btn-sm layui-btn-primary">新建商品</button>
			</div>
			<div class="rightRegion">
				<div class="topArea layui-form">
					<input type="text" name="${WarehousingField.FIELD_NAME_queryKeyword}" class="layui-input"
						onkeyup="instantSearch(this)" lay-verify="checkKeywordHaveCommodityName"
						placeholder="请输入商品条形码、名称、简称或助记码" title="请输入商品条形码、名称、简称或助记码搜索商品" maxlength="64" />
					<i class="layui-icon layui-icon-search" lay-submit lay-filter="commoditySearch"></i>
				</div>
				<table id="popupCommodityList" lay-filter="commodityList">
					<script type="text/html" id="generalCcommodityName">
 						<span style="color: #1A4A9F; cursor: pointer;">{{d.commodity.name}}/{{d.commodity.specification}}</span>
 					</script>
					<script type="text/html" id="numManage">
 						<div class="popupNumManage">
							<button class="layui-btn layui-btn-xs layui-btn-normal reduceNum" onclick="popupNumManage(this, 'reduceNum')" type="button">-</button>
							<input type="text" class="layui-input generalCommNum" style="text-align: center;" value="" onchange="popupNumManage(this)" />
							<button class="layui-btn layui-btn-xs layui-btn-normal addNum" onclick="popupNumManage(this, 'addNum')" type="button">+</button>
							<i style="display: none;">{{d.commodity.ID}}</i>
						</div>
 					</script>
				</table>
				<div class="footArea">
					<span>你已选择了<strong>0</strong>种商品</span>
					<button class="layui-btn layui-btn-sm layui-btn-primary exitChoosedComm">取消</button>
					<button class="layui-btn layui-btn-sm layui-btn-primary confirmChoosedComm">确定</button>
				</div>
			</div>
		</div>
		<div id="toChooseProvider" class="popupPage">
			<div class="leftRegion">
				<div class="topArea">
					<h3>选择供应商</h3>
					<span>选择区域</span>
					<span class="showAll">全部关闭</span>
				</div>
				<ul class="layui-nav layui-nav-tree" lay-filter="generalCommRNByCategory">
					<li class="layui-nav-item layui-nav-itemed">
						<a href="javascript:void(0);" indexID="0">供应商分类</a>
						<dl class="layui-nav-child">
							<dd class="layui-this"><a href="javascript:void(0);" indexID="0">全部供应商</a></dd>
						</dl>
						<c:forEach items="${providerDistrictList}" var="pdl">
							<dl class="layui-nav-child">
								<dd><a href="javascript:void(0);" indexID="${pdl.ID}">${pdl.name}</a></dd>
							</dl>
						</c:forEach>
					</li>
				</ul>
				<button class="layui-btn layui-btn-sm layui-btn-primary">新建供应商</button>
			</div>
			<div class="rightRegion">
				<div class="topArea layui-form">
					<input type="text" name="${WarehousingField.FIELD_NAME_queryKeyword}" class="layui-input"
						onkeyup="instantSearch(this)" lay-verify="pNumEnSpaceNull" placeholder="请输入名称、联系人或联系方式搜索供应商"
						title="请输入名称、联系人或联系方式搜索供应商" maxlength="32" />
					<i class="layui-icon layui-icon-search" lay-submit lay-filter="providerSearch"></i>
				</div>
				<table id="popupProviderList" lay-filter="popupProviderList">
					<script type="text/html" id="barDemo">
						<input type="hidden" value="{{d.ID}}"/>
  						<i class="layui-icon layui-icon-ok-circle barDemo" lay-event="choose" style="cursor: pointer;"></i>
					</script>
				</table>
			</div>
			<div style="position: absolute; right: 20px; bottom: 20px;">
				<button class="layui-btn layui-btn-sm layui-btn-primary confirmChoosedProvider">确定</button>
				<button class="layui-btn layui-btn-sm layui-btn-primary exitChoosedProvider">取消</button>
			</div>
		</div>
		<div id="toChooseShop" class="popupPage">
			<div class="leftRegion">
				<div class="topArea">
					<h3>选择门店</h3>
					<span>选择区域</span>
					<span class="showAll">全部关闭</span>
				</div>
				<ul class="layui-nav layui-nav-tree" lay-filter="generalCommRNByCategory">
					<li class="layui-nav-item layui-nav-itemed">
						<a href="javascript:void(0);" indexID="0">门店分类</a>
						<dl class="layui-nav-child">
							<dd class="layui-this"><a href="javascript:void(0);" indexID="0">全部门店</a></dd>
						</dl>
						<c:forEach items="${shopDistrictList}" var="sdl">
							<dl class="layui-nav-child">
								<dd><a href="javascript:void(0);" indexID="${sdl.ID}">${sdl.name}</a></dd>
							</dl>
						</c:forEach>
					</li>
				</ul>
			</div>
			<div class="rightRegion">
				<div class="topArea layui-form">
					<input type="text" name="${WarehousingField.FIELD_NAME_queryKeyword}" class="layui-input"
						onkeyup="instantSearch(this)" lay-verify="pNumEnSpaceNull" placeholder="请输入名称搜索门店"
						title="请输入名称搜索门店" maxlength="32" />
					<i class="layui-icon layui-icon-search" lay-submit lay-filter="shopSearch"></i>
				</div>
				<table id="popupShopList" lay-filter="popupShopList">
					<script type="text/html" id="shopDemo">
						<input type="hidden" value="{{d.ID}}"/>
  						<i class="layui-icon layui-icon-ok-circle shopDemo" lay-event="choose" style="cursor: pointer;"></i>
					</script>
				</table>
			</div>
			<div style="position: absolute; right: 20px; bottom: 20px;">
				<button class="layui-btn layui-btn-sm layui-btn-primary confirmChoosedShop">确定</button>
				<button class="layui-btn layui-btn-sm layui-btn-primary exitChoosedShop">取消</button>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_warehousingManage.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_popupChooseCommodity.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
</body>

</html>