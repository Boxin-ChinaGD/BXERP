<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<base href="${pageContext.request.contextPath}/">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="bookmark" type="image/x-icon" href="bx.ico" />
	<link rel="shortcut icon" href="bx.ico">
	<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_updateCache.css" />
	<title>更新缓存</title>
</head>
<body>
	<div class="layui-form content" lay-filter="cache">
		<div class="layui-form-item">
			<button style="display: none;" class="formSubmit" lay-submit lay-filter="formSubmit"></button>
			<!-- 公司列表 -->
			<div class="layui-inline">
				<label class="layui-form-label">公司</label>
				<div class="layui-input-inline">
					<select name="${CacheField.FIELD_NAME_dbName}" class="" lay-filter="company">
						<c:forEach items="${company}" var="com">
							<option value="${com.dbName}">${com.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<!-- 缓存类型列表 -->
			<div class="layui-inline">
				<label class="layui-form-label">缓存类型</label>
				<div class="layui-input-inline">
					<select name="${CacheField.FIELD_NAME_cacheType}" class="" lay-filter="enumTpye">
						<option value="0">全部</option>
						<c:forEach items="${enumTpye}" var="et">
							<option value="${et}">${et}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
	</div>
	<hr>
	<div class="showSession">
		<c:forEach items="${session }" var="map">
			<p>${map }</p>
		</c:forEach>
	</div>
	<hr>
	<div class="showInfo"></div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_updateCache.js"></script>
</body>
</html>