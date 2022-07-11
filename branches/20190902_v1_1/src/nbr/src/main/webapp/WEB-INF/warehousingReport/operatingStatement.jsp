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
	<link type="text/css" rel="stylesheet" href="css/bx_operatingStatement.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_popupPage.css" />
	<title>BoXin-经营状况报表</title>
</head>

<body>
	<div class="subject">
		<!--头部-->
		<div class="warehousingManageTop">
			<div>
				<label class="layui-form-label">门店：<span>默认门店&nbsp;v</span></label>
				<ul class="UlShopID" id="shop" style="display: none;">
				</ul>
			</div>
		</div>
		<div class="top">
			<!--头部按钮-->
			<div class="topTab font_color" id="top_div1">
				<ul>
					<li class="default">月报</li>
					<li>年报</li>
				</ul>
			</div>
			<!--头部数据-->
			<div class="report">
				<label class="topTime font_color">
					<div class="layui-inline">
						<label class="layui-form-label">时间</label>
						<div class="layui-input-block">
							<input type="text" name="date" id="datemonth" autocomplete="off" readonly="readonly"
								class="layui-input" style="width:158px">
							<input type="text" name="date" id="dateyear" autocomplete="off" readonly="readonly"
								class="layui-input" style="width:158px">
						</div>
					</div>
					<button id="btn">查询</button>
				</label>
				<div class="report_table">
					<table>
						<tr>
							<td>
								<div class="sale_wrap" data-color="#63C1BD">
									<div class="sale_name" data-color="#4DACA5">
										<span>销售额</span>
									</div>
									<div class="totalAmount">
									</div>
								</div>
							</td>
							<td>
								<div class="sale_wrap" data-color="#619AF4">
									<div class="sale_name" data-color="#457EDF">
										<span>销售毛利</span>
									</div>
									<div class="totalGross">
									</div>
								</div>
							</td>
							<td>
								<div class="sale_wrap" data-color="#E4984F">
									<div class="sale_name" data-color="#D77E31">
										<span>销售总笔数</span>
									</div>
									<div class="totalsales">
									</div>
								</div>
							</td>
							<td>
								<div class="sale_wrap" data-color="#7B72CF">
									<div class="sale_name" data-color="#6A53CC">
										<span>日均销售额</span>
									</div>
									<div class="averagedailysales">
									</div>
								</div>
							</td>
							<td>
								<div class="sale_wrap" data-color="#FFC1C1">
									<div class="sale_name" data-color="#FFAEB9">
										<span>日均销售毛利</span>
									</div>
									<div class="averagedailygrossprofit">
									</div>
								</div>
							</td>
							<td>
								<div class="sale_wrap" data-color="#A7C6E2">
									<div class="sale_name" data-color="#6EB5CF">
										<span>日均销售总笔数</span>
									</div>
									<div class="averagenumberofsalespeday">
									</div>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<div class="report_pie">
					<div id="pie"></div>
				</div>
			</div>
		</div>
		<!--月销售走势-->
		<br />
		<div class="salesTrends">
			<h5>2019-02-18日近七天销售走势</h5>
			<hr />
			<div id="sell"></div>
		</div>
		<!--业绩对比-->
		<br />
		<div class="results">
			<br>
			<div id="salesPerformance"></div>
		</div>
		<!--商品销售排行-->
		<br />
		<div class="salesRank">
			<br>
			<div id="salesRanking">gfh</div>
		</div>
		<div style="height:80px;width:100%;">
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/echarts.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_operatingStatement.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
</body>

</html>