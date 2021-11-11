<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width,initial,initial-scale=1.0">
<link rel="bookmark" type="image/x-icon" href="bx.ico"/>
<link rel="shortcut icon" href="bx.ico">
<script type="text/javascript">
	// 目前是无用的页面
	function check() {
		var warehouseStatus = document.getgetElementById("hidbtn").value;
		var warehouseID = document.getgetElementById("warehouseID").value;
		var myurl = "/wx/getWarehouse.bx?int1=" + warehouseStatus + "&ID=" + warehouseID;
		window.location.assign(encodeURI(myurl));
	}

	function check2() {
		window.close();
	}
</script>
<title>审核</title>
</head>
<body style="text-align: center;">
	<form action="" method="get">

		<input type="hidden" id="warehouseID" value="${warehouse.ID }">
		<div>需要审核的ID： ${warehouse.ID }</div>

		<div>审核状态：${warehouse.int1 eq 0 ? '未审核' : '已审核' }</div>
		<input type="hidden" name="btnVal" value="1" id="hidbtn">
		<div>
			<c:if test="${warehouse.int1 eq 0 }">
				<button value="确认审核" onclick="check()">确认审核</button>
			</c:if>
			<button value="取消" onclick="check2()">取消</button>
		</div>
	</form>
</body>

</html>
