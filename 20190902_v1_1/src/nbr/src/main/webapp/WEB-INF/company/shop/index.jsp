<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
	<link type="text/css" rel="stylesheet" href="css/bx_shop.css" />
	<title>门店列表</title>
</head>

<body>
	<div class="shop_main">
		<!-- 头部导航区域 -->
		<div class="topNav layui-form topNav_head">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">状态：<span><label>所有</label>&nbsp;v</span></label>
					<ul>
						<li>所有</li>
						<li>使用中</li>
						<li>已停止</li>
					</ul>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">经办人：<span><label>所有</label>&nbsp;v</span></label>
					<ul>
						<li>所有</li>
						<c:forEach items="${bxStaffList}" var="st">
							<li>${st.name}<input type="hidden" value="${st.ID}"></li>
						</c:forEach>
					</ul>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">创建日期：<span><label>所有</label>&nbsp;v</span></label>
					<ul>
						<li>所有</li>
						<li>过去一周内</li>
						<li>过去一月内</li>
					</ul>
				</div>
				<br />
			</div>
		</div>
		<!-- 地区分类 -->
		<div class="geographicList">
			<div id="leftSide">
				<div class="topArea">
					<h3>选择分类</h3>
					<span class="showAllCommCategory">全部展开</span>
				</div>
				<ul class="layui-nav layui-nav-tree" lay-filter="commRNByCategory">
					<li class="layui-nav-item"><a href="javascript:void(0);" indexID="0">全部门店</a>
						<dl class="layui-nav-child">
							<dd class="layui-this">
								<a href="javascript:void(0);" indexID="0">全部门店</a>
							</dd>
							<c:forEach items="${shopDistrictList}" var="cpl">
								<dd class="layui-nav-item">
									<a href="javascript:void(0);" indexID="${cpl.ID}">${cpl.name}</a>
								</dd>
							</c:forEach>
						</dl>
					</li>
				</ul>
			</div>
		</div>
		<!-- 门店列表 -->
		<div class="shopList">
			<div class="operation">
				<button class="layui-btn layui-btn-sm toCreateCompany">新建</button>
				<div class="topArea layui-form">
					<input type="text" class="layui-input" onkeyup="instantSearch(this)"
						placeholder="请输入公司名称，手机号(6位及以上)搜索门店" title="请输入公司名称，手机号(6位及以上)搜索门店" />
					<i class="layui-icon layui-icon-search" lay-submit id="search"></i>
				</div>
			</div>
			<!-- 门店信息 -->
			<div class="store">
				<div class="shoplists">
					<ul>
					</ul>
				</div>
				<div id="pagination"></div>
			</div>
		</div>
		<!-- 详细信息 -->
		<div class="shopDetails">
			<!-- 客户信息 -->
			<div class="companyLogo">
				<img src=" " />
				<button class="uploadCompanyLogo backgroudCss" type="button" disabled="disabled"
					title="公司logo会在小程序中显示，作为店面的招牌">上传公司logo</button>
			</div>
			<div class="companyPermit">
				<img src="" height="120px" width="120px">
				<button class="uploadCompanyPermit backgroudCss" type="button" disabled="disabled">上传营业执照</button>
			</div>
			<div class="customerInformation layui-form" lay-filter="customerInformation">
				<h3>客户信息</h3>
				<button class="toUpdateCompany">修改</button>
				<button class="createCompany">保存</button>
				<button class="cancel">取消</button>
				<button class="uploadCompanyAPI backgroudCss" type="button" disabled="disabled">上传公司API证书</button>
				<input type="hidden" class="companyID" name="${company.FIELD_NAME_ID}" />
				<table>
					<tbody>
						<tr>
							<td><strong class="requiredField">*</strong>公司名称</td>
							<td class="message companyName">
								<input type="text" name="${company.FIELD_NAME_name}" value=""
									onchange="dataValidation(this, this.value),companyInfoHasUpdate()" maxlength="12"
									lay-verify="required|noBlank|pEnChinese" autocomplete="off" readonly="readonly" />
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>营业执照号</td>
							<td class="message businessLicenseNumber">
								<input type="text" name="${company.FIELD_NAME_businessLicenseSN}" value=""
									onchange="dataValidation(this, this.value),companyInfoHasUpdate()" maxlength="18"
									lay-verify="required|noBlank|businessLicenseSN" autocomplete="off"
									readonly="readonly" unselectable="on" />
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>公司编号</td>
							<td class="message companySN">
								<input type="text" name="${company.FIELD_NAME_SN}" value="" maxlength="50"
									autocomplete="off" readonly="readonly" unselectable="on" />
							</td>
						</tr>
						<tr>
							<td>子商户号</td>
							<td class="message submchid">
								<input type="text" name="${company.FIELD_NAME_submchid}" value="" maxlength="10"
									lay-verify="pNature0Null|noBlankNull|limitedSubmchidLength"
									onchange="dataValidation(this, this.value)" autocomplete="off" readonly="readonly"
									unselectable="on" />
								<button class="layui-btn layui-btn-xs toUpdateSubmchid">修改</button>
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>联系人</td>
							<td class="message bossName">
								<input type="text" name="${company.FIELD_NAME_bossName}" value=""
									onchange="companyInfoHasUpdate()" maxlength="12" lay-verify="required|pEnChinese"
									autocomplete="off" readonly="readonly" unselectable="on" />
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>联系电话</td>
							<td class="message bossPhone">
								<input type="text" name="${company.FIELD_NAME_bossPhone}" value=""
									onchange="companyInfoHasUpdate()" placeholder="老板的账号" maxlength="11"
									lay-verify="required|phone" autocomplete="off" readonly="readonly"
									unselectable="on" />
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>密码</td>
							<td class="message password">
								<input type="text" name="${company.FIELD_NAME_bossPassword}" value=""
									lay-verify="required|password|noChinese" placeholder="老板账号密码" maxlength="16"
									autocomplete="off" readonly="readonly" unselectable="on" />
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>有效期</td>
							<td class="message periodOfValidity">
								<input type="text" name="${company.FIELD_NAME_expireDatetime}" class="expireDatetime"
									id="datetime" value="" lay-verify="required" autocomplete="off" readonly="readonly"
									unselectable="on" />
								<button class="renewBtn">续费</button>
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>微信号</td>
							<td class="message bossWechat">
								<input type="text" name="${company.FIELD_NAME_bossWechat}" value=""
									onchange="companyInfoHasUpdate()" maxlength="20" lay-verify="required|boosWechat"
									autocomplete="off" readonly="readonly" unselectable="on" />
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>dbName</td>
							<td class="message dbName">
								<input type="text" name="${company.FIELD_NAME_dbName}" value=""
									lay-verify="required|noBlank|charDbNmae" maxlength="20"
									onchange="dataValidation(this, this.value)" autocomplete="off" readonly="readonly"
									unselectable="on" />
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>db用户名</td>
							<td class="message dbUserName">
								<input type="text" name="${company.FIELD_NAME_dbUserName}" value=""
									lay-verify="required|charDbNmae|noBlank" maxlength="20" autocomplete="off"
									readonly="readonly" unselectable="on" />
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>db用户密码</td>
							<td class="message dbUserpwd">
								<input type="text" name="${company.FIELD_NAME_dbUserPassword}" value=""
									lay-verify="required|password|noChinese" maxlength="16" autocomplete="off"
									readonly="readonly" unselectable="on" />
							</td>
						</tr>
						<tr>
							<td><strong class="requiredField">*</strong>店铺招牌</td>
							<td class="message brandName">
								<input type="text" name="${company.FIELD_NAME_brandName}" value="" lay-verify="required"
									maxlength="20" autocomplete="off" readonly="readonly" unselectable="on" />
							</td>
						</tr>
					</tbody>
				</table>
				<input type="hidden" lay-submit lay-filter="companyInfoSubmit" class="companyInfoSubmit" />
			</div>
			<!-- 门店信息 -->
			<div class="shopInformation layui-form" lay-filter="shopInformation">
				<h3>门店信息</h3>
				<button class="toUpdateShopInfo">修改</button>
				<button class="cancelUpdateShopInfo">取消</button>
				<input type="hidden" name="${shop.FIELD_NAME_companyID}" class="companyID" id="idForDelete_companyID" />
				<input type="hidden" name="${shop.FIELD_NAME_ID}" class="shopID" id="idForDelete_shopID" />
				<table>
					<tbody>
						<tr>
							<td>业务员</td>
							<td class="message salesman">
								<input type="text" name="${shop.FIELD_NAME_bxStaffName}" value="" lay-verify="required"
									autocomplete="off" readonly unselectable="on" />
							</td>
						</tr>
						<tr>
							<td>门店名称</td>
							<td class="message shopName">
								<input type="text" name="${shop.FIELD_NAME_name}" value="" lay-verify="required"
									autocomplete="off" readonly unselectable="on" />
							</td>
						</tr>
						<tr>
							<td>状态</td>
							<td class="message status">
								<input type="text" name="${shop.FIELD_NAME_status}" class="shopStatus" value=""
									lay-verify="required" autocomplete="off" readonly unselectable="on" />
								<button class="layui-btn layui-btn-xs toDeleteShop">冻结门店</button>
							</td>
						</tr>
						<tr>
							<td>地址</td>
							<td class="message site">
								<input type="text" name="${shop.FIELD_NAME_address}" value="" lay-verify="required"
									autocomplete="off" readonly unselectable="on" />
							</td>
						</tr>
						<tr>
							<td>经度</td>
							<td class="message longitudeAndLatitude">
								<input type="text" name="${shop.FIELD_NAME_longitude}" value="" lay-verify="required"
									autocomplete="off" readonly unselectable="on" />
							</td>
						</tr>
						<tr>
							<td>维度</td>
							<td class="message">
								<input type="text" name="${shop.FIELD_NAME_latitude}" value="" lay-verify="required"
									autocomplete="off" readonly unselectable="on" />
							</td>
						</tr>
<!-- 						<tr> -->
<!-- 							<td>钥匙</td> -->
<!-- 							<td class="message"> -->
<%-- 								<input type="text" name="${shop.FIELD_NAME_key}" value="" lay-verify="required" --%>
<!-- 									autocomplete="off" readonly unselectable="on" /> -->
<!-- 							</td> -->
<!-- 						</tr> -->
						<tr>
							<td>区域</td>
							<td class="message">
								<select disabled name="${shop.FIELD_NAME_districtID}" id="${shop.FIELD_NAME_districtID}" lay-verify="required">
<%-- 									<c:forEach items="${shopDistrictList}" var="sdl"> --%>
<%-- 										<option value="${sdl.ID}">${sdl.name}</option> --%>
<%-- 									</c:forEach> --%>
								</select>
<%-- 								<input type="text" name="${shop.FIELD_NAME_districtName}" value="" lay-verify="required" --%>
<!-- 									autocomplete="off" readonly unselectable="on" /> -->
							</td>
						</tr>
						<tr>
							<td>备注</td>
							<td class="message remark">
								<input type="text" name="${shop.FIELD_NAME_remark}" value="" lay-verify="required"
									autocomplete="off" readonly unselectable="on" />
							</td>
						</tr>
					</tbody>
				</table>
				<input type="hidden" lay-submit lay-filter="shopsubmit" class="present">
			</div>
			<!-- pos机信息 -->
			<div class="posInformation">
				<h3>POS机信息</h3>
				<button class="toCreatePos">添加</button>
				<div class="tableHeader">
					<table class="layui-table" lay-even lay-size="sm" lay-skin="nob">
						<colgroup>
							<col width="60px">
							<col width="80px">
							<col width="200px">
							<col>
						</colgroup>
						<thead>
							<tr>
								<th>序号</th>
								<th>ID</th>
								<th>SN</th>
								<th>操作</th>
							</tr>
						</thead>
					</table>
				</div>
				<div class="tableBody">
					<table class="layui-table" lay-even lay-size="sm" lay-skin="nob">
						<colgroup>
							<col width="60px">
							<col width="80px">
							<col width="200px">
							<col>
						</colgroup>
						<tbody></tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 	子商户号弹窗 -->
	<div id="submchidPopup">
		<form class="layui-form layui-form-pane">
			<div class="layui-form-item">
				<p>公司名称：<strong></strong></p>
				<input type="hidden" name="${company.FIELD_NAME_ID}" />
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">新子商户号</label>
				<div class="layui-input-block">
					<input type="text" name="${company.FIELD_NAME_submchid}" class="layui-input" maxlength="10"
						lay-verify="pNature0Null|noBlankNull|limitedSubmchidLength"
						onchange="dataValidation(this, this.value)" placeholder="请输入子商户号" autocomplete="off" />
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-input-block">
					<button class="layui-btn cancelUpdateSubmchid" type="button">取消</button>
					<button class="layui-btn" lay-submit lay-filter="updateSubmchid">确认修改</button>
				</div>
			</div>
		</form>
	</div>
	<!-- pos机弹窗 -->
	<div class="posPopup layui-form" id="posPopup">
		<form class="layui-form layui-form-pane">
			<input type="hidden" name="${pos.FIELD_NAME_shopID}" />
			<div class="layui-form-item">
				<p>门店名称：<strong></strong></p>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">POS机SN码</label>
				<div class="layui-input-inline">
					<input type="text" name="${pos.FIELD_NAME_pos_SN}" lay-verify="required|pPosSN|noBlank"
						placeholder="请输入SN码" autocomplete="off" class="layui-input" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">POS机密码</label>
				<div class="layui-input-inline">
					<input type="password" name="${pos.FIELD_NAME_passwordInPOS}" class="layui-input"
						lay-verify="required" placeholder="请输入密码" autocomplete="off" />
				</div>
			</div>
		</form>
		<div class="posBtnSubmit">
			<button class="layui-btn" lay-submit lay-filter="createPos">确定添加</button>
			<button class="layui-btn cancelCreatePos">取消</button>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_jsbn.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_prng4.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rng.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rsa.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_shop.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
</body>

</html>