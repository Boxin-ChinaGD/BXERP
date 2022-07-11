<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<base href="${pageContext.request.contextPath}/">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<META HTTP-EQUIV="expires" CONTENT="0">
		<link rel="bookmark" type="image/x-icon" href="bx.ico"/>
		<link rel="shortcut icon" href="bx.ico">
		<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
		<link type="text/css" rel="stylesheet" href="css/bx_wxPurchasingOrderApproval.css" />
		<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_popupChooseCommodity.js"></script>
		<script type="text/javascript" charset="utf-8" src="scripts/bx_wxPurchasingOrderApproval.js"></script>
		<title>博销宝-采购订单审核</title>
	</head>
	<body>
		<div class="purchasingOrderApprovalMain">
			<input id="purchasingOrdeID" type="hidden" value="${ID}">
			<div class="purchasingOrderApproval-warp">
				<div class="provider-warp">
					<span class="provider_text">供应商：</span>
					<span class="provider-name"></span>
					<div class="purchasingOrderSN-warp">
						<span class="purchasingOrderSN_text">单号：</span>
						<span class="purchasingOrderSN-SN"></span>
					</div>
				</div>
				<div class="commodityList">
					<table>
						<thead>
							<tr>
								<th style="width:60px;">序号</th>
								<th>商品名称</th>
								<th>条形码</th>
								<th>数量</th>
								<th>采购价</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<div class="summarizing">
						<div class="numberSummary-wrap">
							商品合计：<span class="numberSummary-text"></span>件
						</div>
						<div class="priceSummary-wrap">
							合计总金额：<span class="priceSummary-text"></span>元
						</div>
					</div>
					<br/>
					<br/>
					<button class="approvalBtn">审核</button>
				</div>
			</div>
		</div>
	</body>
</html>