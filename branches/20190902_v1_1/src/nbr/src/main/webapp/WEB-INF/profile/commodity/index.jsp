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
	<link type="text/css" rel="stylesheet" href="css/bx_commodityList.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_popupPage.css" />
	<title>BoXin-商品列表</title>
</head>

<body>
	<div id="commodityMain">
		<div class="topNav layui-form navHead">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">状态：<span><b>所有</b>&nbsp;v</span></label>
					<ul key="status">
						<li>所有<input type="hidden" value="-1" /></li>
						<li>非预淘汰商品<input type="hidden" value="0" /></li>
						<li>预淘汰商品<input type="hidden" value="1" /></li>
					</ul>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">创建日期：<span><b>所有</b>&nbsp;v</span></label>
					<ul key="time">
						<li>所有<input type="hidden" value="all" /></li>
						<li>过去一周内<input type="hidden" value="weekend" /></li>
						<li>过去一月内<input type="hidden" value="month" /></li>
					</ul>
				</div>
				<div class="layui-inline">
				<label class="layui-form-label">门店：
<!-- 					<span data="门店"><lable class="selech">默认门店</lable></span> -->
					<span><b>默认门店</b>&nbsp;v</span>
				</label>
					<ul key="shopID">
<!-- 						<li>默认门店<input type="hidden" value="2" /></li> -->
						<c:forEach items="${shopList}" var="shop">
							<li>
								<span style="display:none">门店</span>${shop.name} <input type="hidden"
										value="${shop.ID}" />
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		<div id="leftSide">
			<div class="topArea">
				<h3>选择分类</h3>
				<span class="showAllCommCategory">全部展开</span>
			</div>
			<ul class="layui-nav layui-nav-tree" lay-filter="commRNByCategory">
				<li class="layui-nav-item layui-nav-itemed">
					<a href="javascript:void(0);" indexID="0" title="全部商品" class='categoryTitle'>全部商品</a>
					<dl class="layui-nav-child">
						<dd class="layui-this subclass"><a href="javascript:void(0);" indexID="-1" title="全部商品"
								class="classification">全部商品</a></dd>
					</dl>
				</li>
				<c:forEach items="${categoryParentList}" var="cpl">
					<li class="layui-nav-item">
						<a href="javascript:void(0);" indexID="${cpl.ID}" title="${cpl.name}"
							class='categoryTitle'>${cpl.name}</a>
						<dl class="layui-nav-child">
							<c:forEach items="${categoryList}" var="cl">
								<c:if test="${cl.parentID == cpl.ID}">
									<dd class='subclass'><a href="javascript:void(0);" parentID="${cl.parentID}"
											indexID="${cl.ID}" title="${cl.name}" class="classification">${cl.name}</a>
									</dd>
								</c:if>
							</c:forEach>
						</dl>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div id="middlePart">
			<div class="topArea layui-form abc">
				<input type="text" name="${CommodityField.FIELD_NAME_queryKeyword}" class="layui-input"
					onkeyup="instantSearch(this)" lay-verify="checkKeywordHaveCommodityName"
					placeholder="请输入商品名称、简称、条形码或助记码搜索商品" title="请输入商品名称、简称、条形码或助记码搜索商品" maxlength="64" />
				<i class="layui-icon layui-icon-search" lay-submit lay-filter="commoditySearch"></i>
			</div>
			<div class="commodityTableArea">
				<table id="commodityList" lay-filter="commodityList">
					<script type="text/html" id="commodityName">
						<span style="color: #1A4A9F; cursor: pointer;">{{d.commodity.name}}</span>
					</script>
				</table>
			</div>
			<div class="multiPackage layui-form" lay-filter="multiPackage">
				<div class="layui-form-item" style="margin-bottom: 3px;">
					<h5>多包装基本单位</h5>
					<select name="${CommodityField.FIELD_NAME_baseUnit}" lay-filter="baseUnit" lay-verify="required">
						<c:forEach items="${PackageUnitList}" var="pul">
							<option value="${pul.ID}">${pul.name}</option>
						</c:forEach>
					</select>
					<button class="layui-btn layui-btn-sm toAddUnit">增加副单位</button>
				</div>
				<table class="layui-table" lay-size="sm" lay-skin="nob" lay-even>
					<colgroup>
						<col width="80px">
					</colgroup>
					<thead>
						<tr class="multiPackageThead">
							<th>类别</th>
							<th class="baseUnitName"></th>
						</tr>
					</thead>
					<tbody>
						<tr class="multiPackagePriceRetail">
							<td>零售价</td>
							<td>
								<input type="text" class="layui-input basePriceRetail"
									onchange="updatePriceRetail(this),commodityInfoHasUpdate()"
									lay-verify="required|checkTypeOfInt" />
							</td>
						</tr>
						<!--				<tr class="multiPackagePriceVIP">
							<td>会员价</td>
							<td>
								<input type="text" class="layui-input basePriceVIP" onchange="updatePriceVIP(this),commodityInfoHasUpdate()" lay-verify="checkTypeOfInt" />
							</td>
						</tr>
						<tr class="multiPackagePriceWholesale">
							<td>批发价</td>
							<td>
								<input type="text" class="layui-input basePriceWholesale" onchange="updatePriceWholesale(this),commodityInfoHasUpdate()" lay-verify="checkTypeOfInt" />
							</td>
						</tr>    -->
						<tr class="multiPackageBarcode">
							<td>条形码</td>
							<td>
								<input type="text" class="layui-input baseBarcode" onfocus="barcodeManage(this)"
									lay-verify="required|checkBarcode" maxlength="64" />
							</td>
						</tr>
						<tr class="multiPackageCommName">
							<td>商品名称</td>
							<td>
								<input type="text" class="layui-input baseCommName" oninput="updateCommName(this)"
									onchange="commodityInfoHasUpdate()" lay-verify="required|commodityName"
									maxlength="32" />
							</td>
						</tr>
					</tbody>
				</table>
				<button type="submit" class="verifyMultiPackageFrom" lay-submit lay-filter="verifyMultiPackageFrom"
					style="display: none;"></button>
			</div>
			<div class="combinedSubCommodity">
				<table class="layui-table" lay-size="sm" lay-skin="nob" lay-even>
					<colgroup>
						<col width="50">
						<col width="135">
						<col width="110">
						<col width="90">
						<col width="75">
						<col width="90">
						<col width="90">
					</colgroup>
					<thead>
						<tr>
							<th>序号</th>
							<th>商品名称/规格</th>
							<th>条形码</th>
							<th>数量</th>
							<th>包装单位</th>
							<th>单价（元）</th>
							<th>金额（元）</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>1</td>
							<td><i class="layui-icon layui-icon-add-circle addGeneralComm" title="添加商品"
									onclick="toChooseGeneralComm()"></i></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div id="toChooseGeneralComm" class="popupPage">
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
		<div id="rightSide">
			<div class="topArea">
				<button class="layui-btn layui-btn-sm commodityManage">新建</button>
				<button class="layui-btn layui-btn-sm commodityManage">保存</button>
				<button class="layui-btn layui-btn-sm commodityManage">删除</button>
				<button class="layui-btn layui-btn-sm commodityManage btnChoosed">取消</button>
			</div>
			<div class="commodityInfo">
				<div class="commodityPicture layui-upload">
					<div class="layui-upload-list">
						<p class='explain'>支持100k大小的<br />jpg、png、jpeg格<br />式图片</p>
						<img class="imgStyle" id="commImage">
					</div>
					<button type="button" class="layui-btn layui-btn-sm" id="uploadImage">上传商品图片</button>
					<button type="button" class="layui-btn layui-btn-sm" id="cleanImage">清空商品图片</button>
				</div>
				<!-- 				generalCommodity和generalComm指的是普通商品 -->
				<div class="generalCommodityInfo layui-form" lay-filter="generalCommodityInfo">
					<button class="generalCommodityManage" lay-submit
						lay-filter="generalCommodityManage">普通商品所属按钮</button>
					<h3><b>基本信息</b></h3>
					<input type="hidden" name="${CommodityField.FIELD_NAME_ID}" class="generalCommodityID" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_pricingType}" value="1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_purchaseFlag}" value="1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_canChangePrice}" value="0" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_ruleOfPoint}" value="1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_lastOperationToPicture}" value="0" />
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>条形码</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_barcodes}"
									class="layui-input textInput generalCommBarcode" onfocus="barcodeManage(this)"
									onchange="commodityInfoHasUpdate()" lay-verify="required|checkBarcode"
									maxlength="64" />
								<i class="layui-icon layui-icon-close-fill toDeleteBarcode"
									onclick="deleteBarcode(this)" title="删除条形码"></i>
								<div class="toAddBarcode" title="增加条形码">增加条形码</div>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>品牌</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_brandID}" class="generalCommBrand"
									lay-filter="formSelect" lay-verify="required">
									<c:forEach items="${brandList}" var="bl">
										<option value="${bl.ID}">${bl.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>名称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_name}"
									class="layui-input textInput generalCommName" oninput="updateCommName(this)"
									onchange="commodityInfoHasUpdate()" lay-verify="required|commodityName"
									maxlength="32" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>规格</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_specification}"
									class="layui-input textInput" onchange="commodityInfoHasUpdate()"
									lay-verify="required|pNumEnSpace" maxlength="8" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">简称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_shortName}"
									class="layui-input textInput" onchange="commodityInfoHasUpdate()"
									lay-verify="pNumEnSpaceNull" maxlength="32" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>零售价</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_priceRetail}"
									class="layui-input textInput"
									onchange="updatePriceRetail(this),commodityInfoHasUpdate()"
									lay-verify="required|pDecimals|checkTypeOfInt" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>分类</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_categoryID}" class="generalCommCategory"
									lay-filter="formSelect" lay-verify="required">
									<option value="">请选择</option>
									<c:forEach items="${categoryParentList}" var="cpl">
										<optgroup label="${cpl.name}">
											<c:forEach items="${categoryList}" var="cl">
												<c:if test="${cl.parentID == cpl.ID}">
													<option value="${cl.ID}">${cl.name}</option>
												</c:if>
											</c:forEach>
										</optgroup>
									</c:forEach>
								</select>
							</div>
						</div>
						<!--					<div class="layui-inline">
							<label class="layui-form-label">会员价</label>
	    					<div class="layui-input-inline">
	    						<input type="text" name="${CommodityField.FIELD_NAME_priceVIP}" class="layui-input textInput" onchange="updatePriceVIP(this),commodityInfoHasUpdate()" lay-verify="pDecimalsNull|checkTypeOfInt" />
	    					</div>
						</div>  -->
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>单位</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_packageUnitID}" class="generalCommPackageUnit"
									lay-filter="packageUnit" lay-verify="required">
									<c:forEach items="${PackageUnitList}" var="pul">
										<option value="${pul.ID}">${pul.name}</option>
									</c:forEach>
								</select>
								<div class="toChoosedMultiUnit">
									<input type="checkbox" lay-ignore />
									<span>多单位</span>
								</div>
							</div>
						</div>
						<!--  		<div class="layui-inline">
							<label class="layui-form-label">批发价</label>
	    					<div class="layui-input-inline">
	    						<input type="text" name="${CommodityField.FIELD_NAME_priceWholesale}" class="layui-input textInput" onchange="updatePriceWholesale(this),commodityInfoHasUpdate()" lay-verify="pDecimalsNull|checkTypeOfInt" />
	    					</div>
						</div>  -->
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>供应商</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_providerIDs}" lay-filter="provider"
									lay-verify="required">
									<option value="">请选择</option>
									<c:forEach items="${providerList}" var="pl">
										<option value="${pl.ID}">${pl.name}</option>
									</c:forEach>
								</select>
								<i class="layui-icon layui-icon-close-fill toDeleteProvider"
									onclick="deleteProvider(this)" title="删除供应商"></i>
								<div class="toAddProvider" title="增加供应商">增加供应商</div>
							</div>
						</div>
					</div>
					<h3 class="otherInfoArea"><b>其他</b></h3>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>商品类型</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_type}" class="generalCommType"
									lay-filter="commodityType" lay-verify="required">
									<option value="0">普通商品</option>
									<option value="1">组合商品</option>
									<option value="3">服务类商品</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>保质期(天)</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_shelfLife}"
									class="layui-input textInput" onchange="commodityInfoHasUpdate()"
									lay-verify="required|pNature|checkTypeOfInt" />
							</div>
						</div>
					</div>
					<!-- 					<div class="layui-form-item"> -->
					<!-- 						<div class="layui-inline"> -->
					<!-- 							<label class="layui-form-label"><strong class="requiredField">*</strong>计价方式</label> -->
					<!-- 	    					<div class="layui-input-inline"> -->
					<%-- 	    						<select name="${CommodityField.FIELD_NAME_pricingType}" lay-verify="required"> --%>
					<%--	    							<option value="0">计重</option> --%>
					<%--	    							<option value="1">计件</option> --%>
					<!-- 	    						</select> -->
					<!-- 	    					</div> -->
					<!-- 						</div> -->
					<!-- 					</div> -->
					<!-- 					<div class="layui-form-item"> -->
					<!-- 						<div class="layui-inline"> -->
					<!-- 							<label class="layui-form-label">采购阀值</label> -->
					<!-- 	    					<div class="layui-input-inline"> -->
					<%-- 	    						<input type="text" name="${CommodityField.FIELD_NAME_purchaseFlag}" class="layui-input textInput" lay-verify="pNatureNull|checkTypeOfInt" /> --%>
					<!-- 	    					</div> -->
					<!-- 						</div> -->
					<!-- 					</div> -->
					<div class="layui-form-item">
						<!-- 						<div class="layui-inline"> -->
						<!-- 							<label class="layui-form-label"><strong class="requiredField">*</strong>前台是否能改价</label> -->
						<!-- 	    					<div class="layui-input-inline"> -->
						<%-- 	    						<select name="${CommodityField.FIELD_NAME_canChangePrice}" lay-verify="required"> --%>
						<%--	    							<option value="0" selected>否</option> --%>
						<%--	    							<option value="1">是</option> --%>
						<!-- 	    						</select> -->
						<!-- 	    					</div> -->
						<!-- 						</div> -->
						<div class="layui-inline">
							<label class="layui-form-label">助记码</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_mnemonicCode}"
									class="layui-input textInput" onchange="commodityInfoHasUpdate()"
									lay-verify="pNumEnNull" maxlength="32" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">退货天数(天)</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_returnDays}"
									class="layui-input textInput" onchange="commodityInfoHasUpdate()"
									lay-verify="pNumNull|checkTypeOfInt" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">${commodityProperty.name1}</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_propertyValue1}"
									class="layui-input textInput" onchange="commodityInfoHasUpdate()" maxlength="50" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">${commodityProperty.name2}</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_propertyValue2}"
									class="layui-input textInput" onchange="commodityInfoHasUpdate()" maxlength="50" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">${commodityProperty.name3}</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_propertyValue3}"
									class="layui-input textInput" onchange="commodityInfoHasUpdate()" maxlength="50" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">${commodityProperty.name4}</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_propertyValue4}"
									class="layui-input textInput" onchange="commodityInfoHasUpdate()" maxlength="50" />
							</div>
						</div>
					</div>
					<!-- 					<div class="layui-form-item"> -->
					<!-- 						<div class="layui-inline"> -->
					<!-- 							<label class="layui-form-label">积分规则</label> -->
					<!-- 	    					<div class="layui-input-inline"> -->
					<%-- 	    						<input type="text" name="${CommodityField.FIELD_NAME_ruleOfPoint}" class="layui-input textInput" lay-verify="pNatureNull|checkTypeOfInt" /> --%>
					<!-- 	    					</div> -->
					<!-- 						</div> -->
					<!-- 					</div> -->
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">备注</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_tag}" class="layui-input textInput"
									onchange="commodityInfoHasUpdate()" maxlength="32" />
							</div>
						</div>
					</div>
<!-- 					不通过前端创建期初商品、通过导入商品资料创建 -->
<!-- 					<h3><b>期初信息</b></h3> -->
<!-- 						&nbsp; <p>期初库存是指在启用本软件前仓库中已有的商品库存，</p> -->
<!-- 						&nbsp; <p>录入期初库存可使后续成本价、毛利、毛利率等值的计算更准确</p> -->
<!-- 						<table class="layui-table preliminaryTable" lay-size="sm" lay-skin="nob"> -->
<%-- 							<colgroup> --%>
<%-- 								<col width="80px"> --%>
<%-- 								<col width="120px"> --%>
<%-- 								<col width="120px"> --%>
<%-- 								<col width="120px"> --%>
<%-- 							</colgroup> --%>
<!-- 							<thead> -->
<!-- 								<tr> -->
<!-- 									<td>类别</td> -->
<!-- 									<td>数量</td> -->
<!-- 									<td>成本价(元)</td> -->
<!-- 									<td>金额(元)</td> -->
<!-- 								</tr> -->
<!-- 							</thead> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td>默认仓库</td> -->
<%-- 									<td><input type="text" name="${CommodityField.FIELD_NAME_nOStart}" --%>
<!-- 											class="layui-input NOStart" lay-verify="pNumNull|checkTypeOfInt" -->
<!-- 											onchange="updateInitialInformation(this)" /></td> -->
<%-- 									<td><input type="text" name="${CommodityField.FIELD_NAME_purchasingPriceStart}" --%>
<!-- 											class="layui-input purchasingPriceStart" -->
<!-- 											lay-verify="pDecimalsNull|checkTypeOfInt" -->
<!-- 											onchange="updateInitialInformation(this)" /></td> -->
<%-- 									<td><input type="text" name="${CommodityField.FIELD_NAME_totalPriceStart}" --%>
<!-- 											class="layui-input totalPriceStart" -->
<!-- 											onchange="updateInitialInformation(this)" /> -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td>备注</td> -->
<!-- 									<td colspan="3"><input type="text" -->
<%-- 											name="${CommodityField.FIELD_NAME_startValueRemark}" class="layui-input" --%>
<!-- 											onchange="commodityInfoHasUpdate()" maxlength="50" autocomplete="off" -->
<!-- 											style="text-align: left; padding-left: 10px;"></td> -->
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
				</div>
				<div class="combinedCommodityInfo layui-form" lay-filter="combinedCommodityInfo">
					<button class="combinedCommodityManage" lay-submit
						lay-filter="combinedCommodityManage">组合商品所属按钮</button>
					<input type="hidden" name="${CommodityField.FIELD_NAME_brandID}" value="1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_nOStart}" value="-1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_priceVIP}" value="0" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_priceWholesale}" value="0" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_propertyValue1}" value="" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_propertyValue2}" value="" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_propertyValue3}" value="" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_propertyValue4}" value="" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_purchaseFlag}" value="-1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_purchasingPriceStart}" value="-1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_returnDays}" value="0" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_ruleOfPoint}" value="-1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_shelfLife}" value="-1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_startValueRemark}" value="" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_providerIDs}" value="0" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_startValueRemark}" value="" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_pricingType}" value="1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_canChangePrice}" value="0" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_lastOperationToPicture}" value="0" />
					<h3><b>基本信息</b></h3>
					<input type="hidden" name="${CommodityField.FIELD_NAME_ID}" class="combinedCommodityID" />
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>条形码</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_barcodes}" class="layui-input"
									lay-verify="required|checkBarcode" maxlength="64" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>分类</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_categoryID}" lay-verify="required">
									<option value="">请选择</option>
									<c:forEach items="${categoryParentList}" var="cpl">
										<optgroup label="${cpl.name}">
											<c:forEach items="${categoryList}" var="cl">
												<c:if test="${cl.parentID == cpl.ID}">
													<option value="${cl.ID}">${cl.name}</option>
												</c:if>
											</c:forEach>
										</optgroup>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>名称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_name}"
									class="layui-input combinedCommName" oninput="updateCommName(this)"
									lay-verify="required|commodityName" maxlength="32" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>规格</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_specification}" class="layui-input"
									lay-verify="required|pNumEnSpace" maxlength="8" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">简称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_shortName}" class="layui-input"
									lay-verify="pNumEnSpaceNull" maxlength="32" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>单位</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_packageUnitID}" lay-verify="required">
									<c:forEach items="${PackageUnitList}" var="pul">
										<option value="${pul.ID}">${pul.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<h3 class="otherInfoArea"><b>其他</b>></h3>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>商品类型</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_type}" lay-filter="commodityType"
									lay-verify="required">
									<option value="0">普通商品</option>
									<option value="1">组合商品</option>
									<option value="3">服务类商品</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">助记码</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_mnemonicCode}" class="layui-input"
									lay-verify="pNumEnNull" maxlength="16" />
							</div>
						</div>
					</div>
					<!-- 					<div class="layui-form-item"> -->
					<!-- 						<div class="layui-inline"> -->
					<!-- 							<label class="layui-form-label"><strong class="requiredField">*</strong>计价方式</label> -->
					<!-- 	    					<div class="layui-input-inline"> -->
					<%-- 	    						<select name="${CommodityField.FIELD_NAME_pricingType}" lay-verify="required"> --%>
					<%--	    							<option value="0">计重</option> --%>
					<%--	    							<option value="1">计件</option> --%>
					<!-- 	    						</select> -->
					<!-- 	    					</div> -->
					<!-- 						</div> -->
					<!-- 					</div> -->
					<!-- 					<div class="layui-form-item"> -->
					<!-- 						<div class="layui-inline"> -->
					<!-- 							<label class="layui-form-label"><strong class="requiredField">*</strong>前台是否能改价</label> -->
					<!-- 	    					<div class="layui-input-inline"> -->
					<%-- 	    						<select name="${CommodityField.FIELD_NAME_canChangePrice}" lay-verify="required"> --%>
					<%--	    							<option value="0" selected>否</option> --%>
					<%-- 		   							<option value="1">是</option> --%>
					<!-- 	    						</select> -->
					<!-- 	    					</div> -->
					<!-- 						</div> -->
					<!-- 					</div> -->
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">备注</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_tag}" class="layui-input"
									maxlength="32" />
							</div>
						</div>
					</div>
				</div>
				<div class="serviceCommodityInfo layui-form" lay-filter="serviceCommodityInfo">
					<button class="serviceCommodityManage" lay-submit
						lay-filter="serviceCommodityManage">普通商品所属按钮</button>
					<h3><b>基本信息</b></h3>
					<input type="hidden" name="${CommodityField.FIELD_NAME_ID}" class="serviceCommodityID" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_latestPricePurchase}" value="-1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_pricingType}" value="1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_purchaseFlag}" value="0" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_canChangePrice}" value="0" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_ruleOfPoint}" value="1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_shelfLife}" value="0" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_nOStart}" value="-1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_purchasingPriceStart}" value="-1" />
					<input type="hidden" name="${CommodityField.FIELD_NAME_lastOperationToPicture}" value="0">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>条形码</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_barcodes}"
									class="layui-input serviceCommodityBarcode" lay-verify="required|checkBarcode"
									maxlength="64" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>品牌</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_brandID}" lay-filter="formSelect"
									lay-verify="required">
									<c:forEach items="${brandList}" var="bl">
										<option value="${bl.ID}">${bl.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>名称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_name}"
									class="layui-input serviceCommName" oninput="updateCommName(this)"
									onchange="commodityInfoHasUpdate()" lay-verify="required|commodityName"
									maxlength="32" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>规格</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_specification}"
									onchange="commodityInfoHasUpdate()" class="layui-input"
									lay-verify="required|pNumEnSpace" maxlength="8" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">简称</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_shortName}" class="layui-input"
									onchange="commodityInfoHasUpdate()" lay-verify="pNumEnSpaceNull" maxlength="32" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>零售价</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_priceRetail}" class="layui-input"
									onchange="commodityInfoHasUpdate()"
									lay-verify="required|pDecimals|checkTypeOfInt" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>分类</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_categoryID}" lay-filter="formSelect"
									lay-verify="required">
									<option value="">请选择</option>
									<c:forEach items="${categoryParentList}" var="cpl">
										<optgroup label="${cpl.name}">
											<c:forEach items="${categoryList}" var="cl">
												<c:if test="${cl.parentID == cpl.ID}">
													<option value="${cl.ID}">${cl.name}</option>
												</c:if>
											</c:forEach>
										</optgroup>
									</c:forEach>
								</select>
							</div>
						</div>
						<!--  	<div class="layui-inline">
							<label class="layui-form-label">会员价</label>
	    					<div class="layui-input-inline">
	    						<input type="text" name="${CommodityField.FIELD_NAME_priceVIP}" class="layui-input" onchange="commodityInfoHasUpdate()" lay-verify="pDecimalsNull|checkTypeOfInt" />
	    					</div>
						</div>  -->
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>单位</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_packageUnitID}" lay-filter="formSelect"
									lay-verify="required">
									<c:forEach items="${PackageUnitList}" var="pul">
										<option value="${pul.ID}">${pul.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<h3 class="otherInfoArea"><b>其他</b></h3>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label"><strong class="requiredField">*</strong>商品类型</label>
							<div class="layui-input-inline">
								<select name="${CommodityField.FIELD_NAME_type}" class="commodityType"
									lay-filter="commodityType" lay-verify="required">
									<option value="0">普通商品</option>
									<option value="1">组合商品</option>
									<option value="3">服务类商品</option>
								</select>
							</div>
						</div>
					</div>
					<!-- 					<div class="layui-form-item"> -->
					<!-- 						<div class="layui-inline"> -->
					<!-- 							<label class="layui-form-label"><strong class="requiredField">*</strong>计价方式</label> -->
					<!-- 	    					<div class="layui-input-inline"> -->
					<%-- 	    						<select name="${CommodityField.FIELD_NAME_pricingType}" lay-verify="required"> --%>
					<%--	    							<option value="0">计重</option> --%>
					<%--	    							<option value="1">计件</option> --%>
					<!-- 	    						</select> -->
					<!-- 	    					</div> -->
					<!-- 						</div> -->
					<!-- 					</div> -->
					<!-- 					<div class="layui-form-item"> -->
					<!-- 						<div class="layui-inline"> -->
					<!-- 							<label class="layui-form-label">采购阀值</label> -->
					<!-- 	    					<div class="layui-input-inline"> -->
					<%-- 	    						<input type="text" name="${CommodityField.FIELD_NAME_purchaseFlag}" class="layui-input textInput" lay-verify="pNatureNull|checkTypeOfInt" /> --%>
					<!-- 	    					</div> -->
					<!-- 						</div> -->
					<!-- 					</div> -->
					<div class="layui-form-item">
						<!-- 						<div class="layui-inline"> -->
						<!-- 							<label class="layui-form-label"><strong class="requiredField">*</strong>前台是否能改价</label> -->
						<!-- 	    					<div class="layui-input-inline"> -->
						<%-- 	    						<select name="${CommodityField.FIELD_NAME_canChangePrice}" lay-verify="required"> --%>
						<%--	    							<option value="0" selected>否</option> --%>
						<%--	    							<option value="1">是</option> --%>
						<!-- 	    						</select> -->
						<!-- 	    					</div> -->
						<!-- 						</div> -->
						<div class="layui-inline">
							<label class="layui-form-label">助记码</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_mnemonicCode}" class="layui-input"
									onchange="commodityInfoHasUpdate()" lay-verify="pNumEnNull" maxlength="32" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">退货天数(天)</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_returnDays}" class="layui-input"
									onchange="commodityInfoHasUpdate()" lay-verify="pNumNull|checkTypeOfInt" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">${commodityProperty.name1}</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_propertyValue1}"
									class="layui-input" onchange="commodityInfoHasUpdate()" maxlength="50" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">${commodityProperty.name2}</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_propertyValue2}"
									class="layui-input" onchange="commodityInfoHasUpdate()" maxlength="50" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">${commodityProperty.name3}</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_propertyValue3}"
									class="layui-input" onchange="commodityInfoHasUpdate()" maxlength="50" />
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">${commodityProperty.name4}</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_propertyValue4}"
									class="layui-input" onchange="commodityInfoHasUpdate()" maxlength="50" />
							</div>
						</div>
					</div>
					<!-- 					<div class="layui-form-item"> -->
					<!-- 						<div class="layui-inline"> -->
					<!-- 							<label class="layui-form-label">积分规则</label> -->
					<!-- 	    					<div class="layui-input-inline"> -->
					<%-- 	    						<input type="text" name="${CommodityField.FIELD_NAME_ruleOfPoint}" class="layui-input textInput" lay-verify="pNatureNull|checkTypeOfInt" /> --%>
					<!-- 	    					</div> -->
					<!-- 						</div> -->
					<!-- 					</div> -->
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">备注</label>
							<div class="layui-input-inline">
								<input type="text" name="${CommodityField.FIELD_NAME_tag}" class="layui-input"
									onchange="commodityInfoHasUpdate()" lay-verify="pNumEnChineseNull" maxlength="32" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="refCommodityList">
				<table id="refCommodityList" lay-filter="refCommodityList"></table>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_commodityList.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_popupChooseCommodity.js"></script>
</body>

</html>