<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<base href="${pageContext.request.contextPath}/">
	<META HTTP-EQUIV="expires" CONTENT="0">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
	<meta name="renderer" content="webkit">
	<link rel="bookmark" type="image/x-icon" href="favico.ico" />
	<link rel="shortcut icon" href="favico.ico">
	<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_commodityAbout.css" />
	<title>BoXin-商品相关</title>
</head>

<body>
	<div id="commodityAboutMain">
		<div class="layui-container">
			<!-- 上半部分		   -->
			<div class="layui-row layui-col-space10">
				<!-- 供应商列表 -->
				<div class="layui-col-md9 providerList">
					<div class="layui-form title">
						<span>供应商列表</span>
						<span class="buleSpace"></span>
						<input type="text" class="layui-input" name="${ProviderField.FIELD_NAME_queryKeyword}"
							maxlength="32" lay-verify="pNumEnSpaceNull" placeholder="请输入名称、联系人或联系方式"
							title="请输入名称、联系人或联系方式" style="font-size: 14px; font-weight: normal;">
						<i class="layui-icon layui-icon-search providerSearch" lay-submit lay-filter="providerSearch"
							style="font-size: 20px; font-weight: normal;"></i>
					</div>
					<!-- 全部供应商 -->
					<div class="provide">
						<div class="provideName"><span>全部供应商</span><span>供应商名称</span></div><br>
						<div class="layui-row layui-col-space">
							<div class="layui-col-md3">
								<div class="providerDistrictArea">
									<div id="providerDistrict">
										<p class="rowSpace" onclick="providerDistrictShowManage(this)">
											<input type="hidden" class="districtId" value="0" />
											<input type="text" class="district" value="全部供应商" maxlength="20"
												readOnly="readOnly" />
										</p>
										<c:forEach items="${providerDistrictList}" var="providerDistrict">
											<p onclick='providerDistrictShowManage(this)'>
												<input type="hidden" class="districtId"
													value="${providerDistrict.ID }" />
												<input type="text" class="district" value="${providerDistrict.name }"
													maxlength="20" readOnly="readOnly" />
												<span class="whiteIcon delete" onclick="providerDistrictManage(this)"
													title="删除">x</span>
												<span class="whiteIcon update" onclick="providerDistrictManage(this)"
													title="修改"><i class="layui-icon layui-icon-edit"></i></span>
												<span class="whiteIcon add" onclick="providerDistrictManage(this)"
													title="新增">+</span>
											</p>
										</c:forEach>
									</div>
								</div>
							</div>
							<div class="layui-col-md5" style="width: 300px; margin-left: 60px;margin-right: 60px;">
								<div style="height:264px;" id="provider"></div>
								<div align="right" style="overflow: auto; overflow-x:hidden;">
									<span id="providerPage"></span>
								</div>
							</div>
							<div class="layui-col-md4" style="margin-left: 20px;">
								<form class="layui-form" id="providerMessage" lay-filter="providerDetails">
									<div class="layui-form-item">
										<label class="layui-form-label">供应商信息</label>
										<div class="layui-input-block">
											<input type="text" name="${ProviderField.FIELD_NAME_ID}" value="" class="ID"
												style="display: none;" />
										</div>
									</div>
									<div class="layui-form-item">
										<label class="layui-form-label"><strong
												class="requiredField">*</strong>名称</label>
										<div class="layui-input-block">
											<input type="text" id="name" name="${ProviderField.FIELD_NAME_name}"
												oninput="checkUniqueField_providerName($(this))"
												onchange="check_ifDataChange();" maxlength="32" readOnly="readOnly"
												lay-verify="required|pNumEnChinese" autocomplete="off"
												class="layui-input" />
										</div>
									</div>
									<div class="layui-form-item">
										<label class="layui-form-label">联系人</label>
										<div class="layui-input-block">
											<input type="text" name="${ProviderField.FIELD_NAME_contactName}"
												maxlength="20" readOnly="readOnly"
												lay-verify="pNumEnSpaceNull|noSpaceHeadOrFootNull" autocomplete="off"
												onchange="check_ifDataChange();" class="layui-input" />
										</div>
									</div>
									<div class="layui-form-item" id="selectDistrict">
										<label class="layui-form-label">区域</label>
										<div class="layui-input-block">
											<select name="${ProviderField.FIELD_NAME_districtID}"
												lay-filter="providerDistrictList">
												<c:forEach items="${providerDistrictList}" var="providerDistrict">
													<c:choose>
														<c:when test="${providerDistrict.ID == 1}">
															<option selected="selected" disabled
																title="${providerDistrict.name}"
																value="${providerDistrict.ID}">${providerDistrict.name }
															</option>
														</c:when>
														<c:otherwise>
															<option disabled title="${providerDistrict.name}"
																value="${providerDistrict.ID}">${providerDistrict.name }
															</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="layui-form-item">
										<label class="layui-form-label">地址</label>
										<div class="layui-input-block">
											<input type="text" name="${ProviderField.FIELD_NAME_address}" maxlength="50"
												readOnly="readOnly" lay-verify="noSpaceHeadOrFootNull"
												autocomplete="off" class="layui-input"
												onchange="check_ifDataChange();" />
										</div>
									</div>
									<div class="layui-form-item">
										<label class="layui-form-label">电话</label>
										<div class="layui-input-block">
											<input type="text" name="${ProviderField.FIELD_NAME_mobile}"
												oninput="checkUniqueField_providerMobile($(this))"
												onchange="check_ifDataChange();" maxlength="24" readOnly="readOnly"
												lay-verify="p7To24Char" autocomplete="off" class="layui-input mobile" />
										</div>
									</div>
									<div class="layui-form-item">
										<div class="layui-input-block" id="providerMessageButton"
											style="margin-left: 95px; display: none;">
											<button lay-submit lay-filter="providerCreate"
												class="commodityButton">确定</button>
											<input type="button" value="取消" class="commodityUnButton" />
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
				<!-- 品牌设置 -->
				<div class="layui-col-md3 commodityBrand">
					<div class="title">
						<span>品牌设置</span><span class="buleSpace"></span>
					</div>
					<div style="margin-top: 30px;" id="brandButton">
						<input id="brandCreateButton" class="commodityButton" type="button" value="新建">
						<input id="brandExitButton" class="commodityUnButton" type="button" value="取消"
							style="display: none;">
					</div>
					<div id="brand"
						style="width: 200px; padding-right:20px; height:300px; overflow: auto; overflow-x:hidden;">
						<c:forEach items="${brandList}" var="brand">
							<p class="rowSpace">
								<input type="hidden" class="ID" value="${brand.ID}" />
								<input type="text" class="brand" value="${brand.name}" maxlength="20"
									readOnly="readOnly" />
								<span class="blackIcon delete" onclick="brandManage(this)" title="删除">x</span>
								<span class="blackIcon update" onclick="brandManage(this)" title="修改"><i
										class="layui-icon layui-icon-edit"></i></span>
							</p>
						</c:forEach>
					</div>
				</div>
			</div>
			<!-- 下半部分		   -->
			<div class="layui-row layui-col-space10">
				<!-- 商品分类 -->
				<div class="layui-col-md6 commodityCategory">
					<div class="title">
						<span>商品分类</span>&nbsp;<span class="buleSpace"></span>
					</div>
					<div class="layui-row layui-col-space10 categoryList">
						<div class="layui-col-md6">
							<div id="categoryParent"
								style="height:280px; overflow: auto; overflow-x:hidden; padding-rigth:10px;">
								<p class="rowSpace" onclick="categoryParentShowManage(this)">
									<input type="hidden" class="categoryParentID" value="0" />
									<input type="text" class="categoryParent" value="全部分类" maxlength="20"
										readOnly="readOnly" />
								</p>
								<c:forEach items="${categoryParentList}" var="parentList">
									<p class="rowSpace" onclick="categoryParentShowManage(this)">
										<input type="hidden" class="categoryParentID" value="${parentList.ID}" />
										<input type="text" class="categoryParent" value="${parentList.name}"
											maxlength="10" readOnly="readOnly" />
										<span class="blackIcon delete" onclick="categoryParentManage(this)"
											title="删除">x</span>
										<span class="blackIcon update" onclick="categoryParentManage(this)"
											title="修改"><i class="layui-icon layui-icon-edit"></i></span>
										<span class="blackIcon add" onclick="categoryParentManage(this)"
											title="新增">+</span>
									</p>
								</c:forEach>
							</div>
						</div>
						<div class="layui-col-md6">
							<div id="category"
								style="height:280px; overflow: auto; overflow-x:hidden; padding: 0 10px;">
								<c:forEach items="${categoryList}" var="categoryList">
									<p class="rowSpace">
										<input type="hidden" class="parentID" value="${categoryList.parentID}" />
										<input type="hidden" class="ID" value="${categoryList.ID}" />
										<input type="text" class="category" maxlength="10" readOnly="readOnly"
											value="${categoryList.name}" />
										<span class="blackIcon delete" onclick="categoryManage(this)"
											title="删除">x</span>
										<span class="blackIcon update" onclick="categoryManage(this)" title="修改"><i
												class="layui-icon layui-icon-edit"></i></span>
										<span class="blackIcon add" onclick="categoryManage(this)" title="新增">+</span>
									</p>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>
				<!-- 商品单位-->
				<div class="layui-col-md3 commodityUnit">
					<div class="title">
						<span>商品单位</span>&nbsp;<span class="buleSpace"></span>
					</div>
					<div id="unitButton">
						<input id="unitCreateButton" class="commodityButton" type="button" value="新建">
						<input id="unitExitButton" class="commodityUnButton" type="button" value="取消"
							style="display: none;">
					</div>
					<div style="width: 200px; height:270px; overflow: auto; overflow-x: hidden;" id="unit">
						<c:forEach items="${PackageUnitList}" var="unit">
							<p class="rowSpace">
								<input type="hidden" class="ID" value="${unit.ID}" />
								<input class="unit" style="float: left;" value="${unit.name}" readOnly="readOnly"
									maxlength="8" />
								<span class="blackIcon delete" onclick="unitManage(this)" title="删除">x</span>
								<span class="blackIcon update" onclick="unitManage(this)" title="修改"><i
										class="layui-icon layui-icon-edit"></i></span>
							</p>
						</c:forEach>
					</div>
				</div>
				<!-- 属性设置 -->
				<div class="layui-col-md3 commodityProeprty">
					<div class="title" style="margin-bottom: 20px;">
						<span>属性设置</span>&nbsp;<span class="buleSpace"></span>
					</div>
					<div id="proeprty" class="layui-form">
						<div class="layui-form-item">
							<label class="layui-form-label">自定义属性1</label>
							<p class="layui-input-block">
								<input type="text" class="proeprty" name="${CommodityPropertyField.FIELD_NAME_name1}"
									value="${commodityProperty.name1 }" autocomplete="off" readOnly="readOnly"
									maxlength="16" />
								<span class="blackIcon" onclick="proeprtyManage(this)" title="修改"><i
										class="layui-icon layui-icon-edit"></i></span>
							</p>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">自定义属性2</label>
							<p class="layui-input-block">
								<input type="text" class="proeprty" name="${CommodityPropertyField.FIELD_NAME_name2}"
									value="${commodityProperty.name2 }" autocomplete="off" readOnly="readOnly"
									maxlength="16" />
								<span class="blackIcon" onclick="proeprtyManage(this)" title="修改"><i
										class="layui-icon layui-icon-edit"></i></span>
							</p>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">自定义属性3</label>
							<p class="layui-input-block">
								<input type="text" class="proeprty" name="${CommodityPropertyField.FIELD_NAME_name3}"
									value="${commodityProperty.name3 }" autocomplete="off" readOnly="readOnly"
									maxlength="16" />
								<span class="blackIcon" onclick="proeprtyManage(this)" title="修改"><i
										class="layui-icon layui-icon-edit"></i></span>
							</p>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label">自定义属性4</label>
							<p class="layui-input-block">
								<input type="text" class="proeprty" name="${CommodityPropertyField.FIELD_NAME_name4}"
									value="${commodityProperty.name4 }" autocomplete="off" readOnly="readOnly"
									maxlength="16" />
								<span class="blackIcon" onclick="proeprtyManage(this)" title="修改"><i
										class="layui-icon layui-icon-edit"></i></span>
							</p>
						</div>
						<button lay-submit lay-filter="proeprtySubmit" style="display: none;">提交</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_commodityAbout.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
</body>

</html>