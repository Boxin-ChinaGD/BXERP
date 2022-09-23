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
		<link type="text/css" rel="stylesheet" href="css/bx_wxUnsalableCommodity.css" />
		<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
		<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
        <script type="text/javascript" charset="utf-8" src="scripts/bx_wxUnsalableCommodity.js"></script>
        <script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
		<title>博销宝-滞销商品列表</title>
	</head>
	<body>
        <div class="unsalableCommodityMain">
            <input type="hidden" id="messageCategoryID" value="${categoryID}">
            <div class="unsalableCommodity">
                <div class="commodityNumber">
                    <span class="number-text">滞销商品总数：</span>
                    <span class="number-warp"></span>
                </div>
                <div class="commodityList">
                    <table>
                        <thead>
                            <tr>
                                <th style="width:60px;">序号</th>
                                <th>商品名称</th>
                                <th>条形码</th>
                                <th style="width:160px;">最近采购价</th>
                                <th style="width:160px;">零售价</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body> 
</html>