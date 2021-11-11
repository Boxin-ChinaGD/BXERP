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
	<link type="text/css" rel="stylesheet" href="css/bx_inventoryList.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_popupPage.css" />
	<title>BoXin-盘点单列表</title>
</head>

<body>
	<div id="inventoryMain">
		<div class="topNav layui-form topNav_head">
			<div class="layui-form-item">
				<div class="layui-inline">
				<label class="layui-form-label">门店：<span data="门店"><lable class="selech">所有 &nbsp;v</lable></span></label>
					<ul>
						<li>所有<input type="hidden" value="-1" /></li>
						<c:forEach items="${shopList}" var="shop">
							<li>
								<span style="display:none">门店</span>${shop.name} <input type="hidden"
										value="${shop.ID}" />
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">状态：<span data="状态"><lable class="selech">所有 &nbsp;v</lable></span></label>
					<ul>
						<li>所有<input type="hidden" value="-1" /></li>
						<li>待录入<input type="hidden" value="0" /></li>
						<li>已提交<input type="hidden" value="1" /></li>
						<li>已审核<input type="hidden" value="2" /></li>
					</ul>
				</div>
			</div>
		</div>
		<div id="leftSide">
			<div class="topArea layui-form">
				<input value="PD" type="text" name="${InventorySheetField.FIELD_NAME_queryKeyword}" class="layui-input"
					onkeyup="instantSearch(this)" lay-verify="checkKeywordHaveCommodityName"
					placeholder="请输入商品名称或单号(大于等于10位)搜索盘点单" title="请输入商品名称或单号(大于等于10位)搜索盘点单" maxlength="32" />
				<i class="layui-icon layui-icon-search" lay-submit lay-filter="inSheetSearch"></i>
			</div>
			<table id="inventorySheetList" lay-filter="inventorySheetList">
				<script type="text/html" id="inventorySheetOddNumbers">
					<span style="color: #1A4A9F; cursor: pointer;">{{d.sn}}</span>
				</script>
			</table>
		</div>
		<div id="rightSide" class="layui-form" lay-filter="inventorySheetInfo">
			<div class="topArea">
				<button class="layui-btn layui-btn-sm" lay-submit lay-filter="inventorySheetManage">新建</button>
				<button class="layui-btn layui-btn-sm" lay-submit lay-filter="inventorySheetManage">保存</button>
				<button class="layui-btn layui-btn-sm changeInSheetStatus" lay-submit lay-filter="inventorySheetManage">提交</button>
				<button class="layui-btn layui-btn-sm" lay-submit lay-filter="inventorySheetManage">删除</button>
				<button class="layui-btn layui-btn-sm" lay-submit lay-filter="inventorySheetManage">取消</button>
			</div>
			<h3><b>商品详细</b></h3>
			<input type="hidden" id="sessionStaffID" value="${sessionScope.SESSION_Staff.ID}" />
			<input type="hidden" id="sessionStaffName" value="${sessionScope.SESSION_Staff.name}" />
			<input type="hidden" name="${InventorySheetField.FIELD_NAME_ID}" class="inventorySheetID" />
			<div class="layui-form-item">
				<div class="layui-inline notShowWhenCreateInSheet">
					<label class="layui-form-label">单号</label>
					<div class="layui-input-inline">
						<input type="text" name="${InventorySheetField.FIELD_NAME_sn}" class="layui-input"
							readonly="readonly" />
					</div>
				</div>
				<div class="layui-inline notShowWhenCreateInSheet">
					<label class="layui-form-label">状态</label>
					<div class="layui-input-inline">
						<input type="text" name="${InventorySheetField.FIELD_NAME_status}" class="layui-input"
							readonly="readonly" />
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">创建者</label>
					<div class="layui-input-inline">
						<input type="hidden" name="${InventorySheetField.FIELD_NAME_staffID}" />
						<input type="text" name="${InventorySheetField.FIELD_NAME_creatorName}" class="layui-input"
							readonly="readonly" />
					</div>
				</div>
				<div class="layui-inline notShowWhenCreateInSheet">
					<label class="layui-form-label">创建时间</label>
					<div class="layui-input-inline">
						<input type="text" name="${InventorySheetField.FIELD_NAME_createDatetime}" class="layui-input"
							readonly="readonly" />
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">盘点仓库</label>
					<div class="layui-input-inline">
						<select name="${InventorySheetField.FIELD_NAME_warehouseID}" lay-verify="required">
							<option value="1">默认仓库</option>
						</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">盘点范围</label>
					<div class="layui-input-inline">
						<select name="${InventorySheetField.FIELD_NAME_scope}" lay-verify="required">
							<option value="0">全店盘点</option>
							<option value="1">指定类别</option>
							<option value="2">指定供应商</option>
							<option value="3">指定品牌</option>
						</select>
					</div>
				</div>
			</div>
			
			<div class="layui-form-item">
				<div class="layui-inline notShowWhenCreateInSheet">
					<label class="layui-form-label">审核人</label>
					<div class="layui-input-inline">
						<input type="text" name="${InventorySheetField.FIELD_NAME_approverName}" class="layui-input"
							readonly="readonly" />
					</div>
				</div>
				<div class="layui-inline notShowWhenCreateInSheet">
					<label class="layui-form-label">审核时间</label>
					<div class="layui-input-inline">
						<input type="text" name="${InventorySheetField.FIELD_NAME_approveDatetime}" class="layui-input"
							readonly="readonly" />
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">盘点门店</label>
					<div class="layui-input-inline">
						<select name="${InventorySheetField.FIELD_NAME_shopID}" lay-verify="required">
							<c:forEach items="${shopList}" var="shop">
							<li>
								<option value="${shop.ID}">${shop.name}</option>
							</li>
						</c:forEach>
						</select>
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">盘点总结</label>
					<div class="layui-input-inline">
						<textarea name="${InventorySheetField.FIELD_NAME_remark}" class="layui-textarea"
							onchange="inventorySheetInfoHasUpdate()" maxlength="100"></textarea>
					</div>
				</div>
			</div>
			<h3><b>盘点商品列表</b></h3>
			<div class="inventoryCommodityInfo">
				<div class="tableHeaderDescription">
					<table class="layui-table" lay-size="sm" lay-skin="nob" lay-even>
						<colgroup>
							<col width="50">
							<col width="130">
							<col width="130">
							<col width="80">
							<col width="80">
							<col>
						</colgroup>
						<thead>
							<tr>
								<th>序号</th>
								<th>商品名称/规格</th>
								<th>条形码</th>
								<th>包装单位</th>
								<th>实盘数量</th>
								<th>系统库存</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="inventoryCommodityTable">
					<table class="layui-table" lay-size="sm" lay-skin="nob" lay-even>
						<colgroup>
							<col width="50">
							<col width="130">
							<col width="130">
							<col width="80">
							<col width="80">
							<col>
						</colgroup>
						<tbody></tbody>
					</table>
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
				<ul class="layui-nav layui-nav-tree">
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
					<input type="text" name="${CommodityField.FIELD_NAME_queryKeyword}" onkeyup="instantSearch(this)"
						class="layui-input" lay-verify="checkKeywordHaveCommodityName"
						placeholder="请输入商品名称、简称、条形码或助记码搜索商品" title="请输入商品名称、简称、条形码或助记码搜索商品" maxlength="64" />
					<i class="layui-icon layui-icon-search" lay-submit lay-filter="commoditySearch"></i>
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
					<button class="layui-btn layui-btn-sm layui-btn-primary closePopupPage">取消</button>
					<button class="layui-btn layui-btn-sm layui-btn-primary confirmChoosedComm">确定</button>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_inventoryList.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_popupChooseCommodity.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
</body>

</html>