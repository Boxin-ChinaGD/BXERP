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
	<link type="text/css" rel="stylesheet" href="css/bx_salesRecord.css" />
	<title>销售记录</title>
</head>

<body>
	<div id="purchasingOrderMain">
		<!-- 头部导航区域 -->
		<div class="topNav layui-form">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">门店：<span><label>所有&nbsp;v</label></span></label>
					<ul>
						<li data='门店'>所有</li>
						<c:forEach items="${shopList}" var="shop">
							<li>${shop.name}<input type="hidden" value="${shop.ID}"></li>
						</c:forEach>
					</ul>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">操作人员：<span><label>所有&nbsp;v</label></span></label>
					<ul>
						<li data='操作人员'>所有</li>
						<c:forEach items="${objectList}" var="staff">
							<c:if test="${staff.roleID == 1 || staff.roleID == 4}">
								<li>${staff.name}<input type="hidden" value="${staff.ID}"></li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
				<div class="layui-inline pattern">
					<label class="layui-form-label">支付方式：<span><label>所有&nbsp;v</label></span></label>
					<ul>
						<li data='支付方式'>所有</li>
						<li>现金支付</li>
						<li>微信支付</li>
						<li>支付宝支付</li>
						<li>支付方式1</li>
						<li>支付方式2</li>
						<li>支付方式3</li>
						<li>支付方式4</li>
						<li>支付方式5</li>
					</ul>
				</div>
				<div class="layui-inline sell">
					<label class="layui-form-label">销售时间：<span><label>所有&nbsp;v</label></span></label>
					<ul>
						<li data='销售时间'>所有</li>
						<li>过去一周内</li>
						<li>过去一月内</li>
						<li data='自定义时间'>自定义时间</li>
					</ul>
				</div>
				<div class="top_search">
					<input class="top_input" type="text" value="LS" onkeyup="instantSearch(event)"
						placeholder="请输入单号(大于等于10位)、商品名称搜索销售记录" title="请输入单号(大于等于10位)、商品名称搜索销售记录" maxlength="32" />
					<i class="layui-icon layui-icon-search top_icon"></i>
				</div>
			</div>
		</div>
		<div class="left_table">
			<table id="allSalesNoteList" lay-filter="allSalesNoteList">
			</table>
			<div class="summarizing">
				<div>
					销售数量：<span class="totalCommNO"></span>
				</div>
				<div>
					销售总额：<span class="retailAmount"></span>
				</div>
				<div>
					销售毛利：<span class="totalGross"></span>
				</div>
			</div>
		</div>
		<div id="right-Area" class="layui-form">
			<div class="fullDiscountInfo">
				<div class="message">
					<b>基本信息</b>
					<div class="retailTradeSN">
						<label>零售单号：</label><span></span>
					</div>
					<div class="staffName">
						<label>收银员：</label><span></span>
					</div>
					<div class="saleDatetime">
						<label>销售时间：</label><span></span>
					</div>
					<div class="pos_SN">
						<label>POS机流水单号：</label><span></span>
					</div>
				</div>
				<div class="particulars">
					<br>
					<b>销售详情</b>
					<br>
					<div class="particulars_table">
						<table>
							<thead>
								<tr>
									<th>名称</th>
									<th>条码</th>
									<th>单价</th>
									<th>数量</th>
									<th>小计</th>
								</tr>
							</thead>
							<tbody>
								<tr class="demarcationRow">
									<td colspan="5"></td>
								</tr>
								<tr class="totalAmount">
									<td colspan="4">合计：</td>
									<td></td>
								</tr>
								<tr class="discountAmount">
									<td colspan="4">优惠：</td>
									<td></td>
								</tr>
								<tr class="payableAmount">
									<td colspan="4">应付：</td>
									<td></td>
								</tr>
								<tr class="change">
									<td colspan="4">找零：</td>
									<td></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="dateTime">
		<input type="text" id="timeQuantum" autocomplete="off" readonly="readonly" class="layui-input"
			placeholder="请选择查询时间" />
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_salesRecord.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
</body>

</html>