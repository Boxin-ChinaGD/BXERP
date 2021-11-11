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
	<link type="text/css" rel="stylesheet" href="css/bx_purchasingOrderList.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_popupPage.css" />
	<title>BoXin-采购订单列表</title>
</head>

<body>
	<div id="purchasingOrderMain">
		<!-- 头部导航区域 -->
		<div class="topNav layui-form topNav_head">
			<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">门店：<span data="门店">
							<lable class="selech">所有 &nbsp;v</lable>
						</span></label>
					<ul>
						<li>所有</li>
						<c:forEach items="${shopList}" var="shop">
							<li>
								<span style="display:none">门店</span>${shop.name} <input type="hidden"
										value="${shop.ID}" />
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">状态：<span data="状态">
							<lable class="selech">所有 &nbsp;v</lable>
						</span></label>
					<ul>
						<li>所有</li>
						<li>未审核</li>
						<li>已审核</li>
						<li>部分入库</li>
						<li>全部入库</li>
					</ul>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">经办人：<span data="经办人">
							<lable class="selech">所有 &nbsp;v</lable>
						</span></label>
					<ul>
						<li>所有</li>
						<c:forEach items="${staffList}" var="staff">
							<c:if test="${staff.roleID == 1 || staff.roleID == 4}">
								<li>
									<span style="display:none">经办人</span>${staff.name} <input type="hidden"
										value="${staff.ID}" />
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">供应商：<span data="供应商">
							<lable class="selech">所有 &nbsp;v</lable>
						</span></label>
					<ul>
						<li>所有</li>
						<c:forEach items="${providerList}" var="pl">
							<li><span style="display:none">供应商</span>${pl.name}</li>
						</c:forEach>
					</ul>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">创建日期：<span data="创建日期">
							<lable class="selech">所有 &nbsp; v</lable>
						</span></label>
					<ul>
						<li>所有</li>
						<li>过去一周内</li>
						<li>过去一月内</li>
					</ul>
				</div>
				<div class="td_search">
					<input class="search-box" type="text" placeholder="请输入商品名称,供应商名称或单号(大于等于10位)搜索采购订单"
						title='请输入商品名称,供应商名称或单号(大于等于10位)搜索采购订单' maxlength="32" />
					<i class="layui-icon layui-icon-search td_icon"></i>
				</div>
			</div>
		</div>
		<!--主体部分-->
		<!--主体顶部状态选择-->
		<div class="tabControl">
			<ul>
				<li><button id="button_create" class="layui-btn layui-btn-sm purchaseManage">新建</button></li>
				<li><button id="button_save" class="layui-btn layui-btn-sm purchaseManage">保存</button></li>
				<li><button id="button_delete" class="layui-btn layui-btn-sm purchaseManage">删除</button></li>
				<li><button id="button_cancel" class="layui-btn layui-btn-sm purchaseManage">取消</button></li>
			</ul>
		</div>
		<div class="centerdiv">
			<!--主体左侧导航栏-->
			<div class="purchase-list">
				<ul>
				</ul>
				<div id="pagination"></div>
			</div>
			<!--主体右侧结果数据-->
			<div class="right-data">
				<!--主体右侧顶部数据-->
				<div class="top">
					<span id="div_span" class="div_span">
						/采购订单
					</span>
					<span class="warehouse div_span">
						采购仓库：默认仓库
					</span>
					<div class="topNav layui-form">
						<div class="layui-form-item">
							<label class="layui-form-label supplier">
								供应商：
								<span class="supplier_span">
									<lable class="providerName"></lable>
									<lable style="display:none;" class="providerID"></lable>
								</span>
								<span class="span_v" style="display:none;">
									<i class="layui-icon layui-icon-add-circle addProvider" title="添加供应商"></i>
								</span>
							</label>
							
							<label class="layui-form-label supplier">
								门店：
								<span class="supplier_span">
									<lable class="shopName"></lable>
									<lable style="display:none;" class="shopID"></lable>
								</span>
								<span class="span_v" style="display:none;">
									<i class="layui-icon layui-icon-add-circle addShop" title="添加门店"></i>
								</span>
							</label>
						</div>
						<div class="odd-numbers">
							采购订单号：<label></label>
						</div>
					</div>
				</div>

				<!--主体右侧表格数据-->
				<div class="table-data">
					<table id="purchasingOrder">
						<thead>
							<tr>
								<th style="width:60px;">编号</th>
								<th>商品条码</th>
								<th>商品名称</th>
								<th>包装单位</th>
								<th>数量</th>
								<th>采购价(元)</th>
								<th>金额(元)</th>
								<th>已入库数量</th>
								<th>待入库数量</th>
								<th>本次入库数量</th>
								<th>本次入库价格</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<!--主体右侧表格数据结算-->
				<div class="cost">
					<div class="content">
						<div class="creator"></div><span class="amount"><b>合计:<span
									class="money">0.00元</span></b></span>
						<ul>
							<li><button id="button_warehousing" class="layui-btn layui-btn-sm purchaseManage btn" disabled="disabled">
									入库</button>
							</li>
							<li><button id="button_approve" class="layui-btn layui-btn-sm purchaseManage btn" disabled="disabled">
									审核</button>
							</li>
						</ul>
					</div>
				</div>

				<!--主体右侧表格数据工作流-->
				<div class="workflow">
					<label>工作流：</label>
					<br>
					<ul>
						<li class="workflow_li">① 创建</li>
						<li>></li>
						<li>②审核</li>
						<li>></li>
						<li style="margin-right:15px;">③部分入库</li>
						<li>></li>
						<li>④全部入库</li>
					</ul>

				</div>

				<!--主体右侧表格数据其他-->
				<div class="else">
					<label>其他：</label>
					<br>
					<ul>
						<li>备注</li>
						<li>上传纸质单据</li>
						<li>备改动记录</li>

					</ul>
					<div class="remarkword">
						<textarea cols="80" rows="9" id="remark" onchange="remark(this)"></textarea>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 商品弹出层 -->
	<div id="allCommodity" class="popupPage">
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
					placeholder="请输入商品名称、简称、条形码或助记码搜索商品" title="请输入商品名称、简称、条形码或助记码搜索商品" id="searchcommodity"
					maxlength="64" />
				<i class="layui-icon layui-icon-search commoditySearch" lay-submit lay-filter="commoditySearch"></i>
			</div>
			<table id="popupCommodityList" lay-filter="popupCommodityList">
				<script type="text/html" id="generalCcommodityName">
 					<span style="color: #1A4A9F; cursor: pointer;">{{d.commodity.name}}/{{d.commodity.specification}}</span>
 				</script>
				<script type="text/html" id="popupNumManage">
 					<div class="popupNumManage">
						<button class="layui-btn layui-btn-xs layui-btn-normal reduceNum" onclick="popupNumManage(this,'reduceNum')"  type="button">-</button>
						<input type="text" class="layui-input popupCommNum test" style="text-align: center;" value="" onchange="popupNumManage(this)" />
						<button class="layui-btn layui-btn-xs layui-btn-normal addNum" onclick="popupNumManage(this,  'addNum')" type="button">+</button>
						<i style="display: none;">{{d.commodity.ID}}</i>
					</div>
 				</script>
			</table>
			<div class="footArea">
				<span>你已选择了<strong class="commodityKinds">0</strong>种商品</span>
				<button class="layui-btn layui-btn-sm layui-btn-primary closeLayerPage">取消</button>
				<button class="layui-btn layui-btn-sm layui-btn-primary confirmChooseCommodity">确定</button>
			</div>
		</div>
	</div>
	<!-- 		弹出供应商表 -->
	<div id="chooseProviderWindow" class="popupPage">
		<div class="leftRegion">
			<div class="topArea">
				<h3>选择供应商</h3>
				<span>选择区域</span>
				<span class="showAllProviderDistrict">全部关闭</span>
			</div>
			<ul class="layui-nav layui-nav-tree" lay-filter="providerDistrict">
				<li class="layui-nav-item layui-nav-itemed">
					<a href="javascript:void(0);">供应商区域</a>
					<dl class="layui-nav-child">
						<dd>
							<a href="JavaScript:void(0);">全部供应商<input type="hidden" value="0"></a>
						</dd>
						<c:forEach items="${providerDistrictList}" var="pd">
							<dd>
								<a href="JavaScript:void(0);">${pd.name}<input type="hidden" value="${pd.ID}" /></a>
							</dd>
						</c:forEach>
					</dl>
				</li>
			</ul>
			<button class="layui-btn layui-btn-sm layui-btn-primary"
				onclick="pageJumping('commodity/about.bx', 'commodity/about.bx', '商品相关')">新建供应商</button>
		</div>
		<div class="rightRegion">
			<div class="topArea layui-form">
				<input type="text" name="${ProviderField.FIELD_NAME_queryKeyword}" class="layui-input"
					id="providerinput" lay-verify="pNumEnSpaceNull" maxlength="32" onkeyup="instantSearch(this);"
					placeholder="请输入名称、联系人或联系方式搜索供应商" title="请输入名称、联系人或联系方式搜索供应商" />
				<i class="layui-icon layui-icon-search" id="seek_icon" lay-submit lay-filter="providerSearch"></i>
			</div>
			<table id="providerList" lay-filter="providerList">
				<script type="text/html" id="barDemo">
  					<i class="layui-icon layui-icon-ok-circle barDemo" lay-event="choose" style="cursor: pointer;"></i>
				</script>
			</table>
			<div class="footArea">
				<span></span>
				<button class="layui-btn layui-btn-sm layui-btn-primary " id="cancel">取消</button>
				<button class="layui-btn layui-btn-sm layui-btn-primary " id="confirm">确定</button>
			</div>
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
	
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_purchasingOrderList.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_popupChooseCommodity.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/CONST.js"></script>
</body>

</html>