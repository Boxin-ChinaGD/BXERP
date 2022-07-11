layui.use(['element', 'form', 'table', 'upload'], function () {
	var element = layui.element;
	var form = layui.form;
	var table = layui.table;
	var upload = layui.upload;
	//定义函数内常量
	const commRN_url = "commodity/retrieveNEx.bx";		//查询商品接口（包括模糊查询）
	const commCreate_url = "commoditySync/createEx.bx";		//创建商品接口(普通商品、组合商品、服务类商品)
	const commUpdate_url = "commoditySync/updateEx.bx";		//修改商品接口(普通商品、组合商品、服务类商品)
	const commDelete_url = "commoditySync/deleteEx.bx";		//删除商品接口(普通商品、组合商品、服务类商品)
	const commPictureUpload_url = "commoditySync/uploadPictureEx.bx";		//上传图片接口
	const commPictureDelete_url = "commoditySync/deleteCommodityPictureEx.bx";		//清除商品图片接口
	const refCommRN_url = "refCommodityHub/retrieveNByBarcodeEx.bx";		//查询商品参考库接口
	const barcodeCreate_url = "barcodesSync/createEx.bx";		//创建商品条形码接口
	const barcodeUpdate_url = "barcodesSync/updateEx.bx";		//修改商品条形码接口
	const barcodeDelete_url = "barcodesSync/deleteEx.bx";		//删除商品条形码接口
	const commRNToCheckUniqueField_url = "commodity/retrieveNToCheckUniqueFieldEx.bx";		//检查唯一字段接口
	const commRN_tableID = "commodityList";		//页面商品表表格ID
	const popupCommRN_tableID = "popupCommodityList";		//弹出普通商品表表格ID
	const refCommRN_tableID = "refCommodityList";		//商品参考库表格ID
	const method_get = "GET";		//get请求
	const method_post = "POST";		//post请求
	const pageData_reloadTable = { "status": -1, "type": -1 , "shopID" : 2};		//页码重载表格传参
	const popupData_reloadTable = { "status": -1, "type": 0 };		//弹窗重载表格传参
	const refData_reloadTable = { "barcode": "" };		//商品参考库重载表格传参
	const curr_reloadTable = 1;		//默认表格页码
	const LAST_OPERATION_TO_PICTURE_None = 0;		// 用户创建、R1、RN商品后，没有对图片进行任何操作。这是默认值 
	const LAST_OPERATION_TO_PICTURE_Upload = 1;		//用户最后一个操作是：上传图片 
	const LAST_OPERATION_TO_PICTURE_Clear = 2;		// 用户最后一个操作是：清空图片 
	var bDesertLastOperation = true;		//用于提示是否放弃之前的操作，true表示放弃，false表示不放弃。比如，用户点击了新建，然后点击搜索，这时会弹出提示
	var shopID = 2;
	var popupPageWidth;
	//页面加载后执行
	$(document).ready(function () {
		adaptiveScreen();
	})

	//缩放窗口时执行
	window.onresize = function () {
		adaptiveScreen();
	}
	//自适应屏幕函数
	function adaptiveScreen () {
		var windowHeight = $(window).height();
		var leftNavOffsetTop = $("#leftSide .layui-nav").offset().top;
		$("#leftSide .layui-nav").css("height", (windowHeight - leftNavOffsetTop) + "px");
		var middleOffsetTop = $("#middlePart").offset().top;
		$("#middlePart").css("height", (windowHeight - middleOffsetTop - 10) + "px");
		var rightCommInfoOffsetTop = $("#rightSide .commodityInfo").offset().top;
		$("#rightSide .commodityInfo").css("height", (windowHeight - rightCommInfoOffsetTop - 10) + "px");
		var documentWidth = $(document).width();
		if (documentWidth >= 1060) {
			popupPageWidth = "1000px";
		} else {
			popupPageWidth = documentWidth - 60 + "px";
		}
	}

	//页面初始加载层
	layer.ready(function () {
		var indexLoading = layer.load(1);
	});
	//头部导航选项
	$(".topNav .layui-form-label span").click(function () {
		var index = $(this);
		var area = index.offset();		//获取当前导航的坐标
		var areaParent = index.parent().offset();		//获取当前导航的父级对象的坐标
		var thisUl = index.parent().next();		//获取当前导航的选项列表
		thisUl.css({ "display": "block", "left": area.left - areaParent.left });		//计算坐标并显示当前导航的选项列表
	})
	//根据商品状态和创建时间搜索商品
	$(".topNav ul li").click(function () {
		var indexObject = $(this);
		if (bDesertLastOperation) {
			searchCommodityByStatusOrTime(indexObject);
		} else {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
				layer.close(index);
				bDesertLastOperation = true;
				searchCommodityByStatusOrTime(indexObject);
			}, function (index, layero) {		//点击取消
				layer.close(index);
			})
		}
	})
	function searchCommodityByStatusOrTime (indexObject) {
		var key = indexObject.parent().attr("key");		//获取当前点击对象所属的导航关键字
		switch (key) {
			case "status":		//根据商品状态进行查询
				var status = indexObject.find("input").val();
				pageData_reloadTable.status = status;
				break;
			case "time":		//根据商品创建日期进行查询
				var time = indexObject.find("input").val();
				var date = new Date();
				var date1 = "", date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss");		//date2时间往后推是避免查询过程中有其他人在该门店添加商品而没有被查询出来
				if (time == "all") {		//查询任何时候创建的所有商品
					date1 = "1970/01/01 00:00:00";
				} else if (time == "weekend") {		//查询一周内创建的所有商品
					var date_aWeekAgo = new Date(date.getTime() - 7 * 24 * 60 * 60 * 1000);
					var year = date_aWeekAgo.getFullYear(), month = date_aWeekAgo.getMonth() + 1, day = date_aWeekAgo.getDate();
					date1 = year + "/" + month + "/" + day + " 00:00:00";
				} else if (time == "month") {		//查询一月内创建的所有商品
					var date_aMonthAgo = new Date(date.getTime() - 30 * 24 * 60 * 60 * 1000);
					var year = date_aMonthAgo.getFullYear(), month = date_aMonthAgo.getMonth() + 1, day = date_aMonthAgo.getDate();
					date1 = year + "/" + month + "/" + day + " 00:00:00";
				} else {
					return;		//其他时间段
				}
				pageData_reloadTable.date1 = date1;
				pageData_reloadTable.date2 = date2;
				break;
			case "shopID":
				shopID = indexObject.find("input").val();
				pageData_reloadTable.shopID = shopID;
				break;
			default:
				break;
		}
		console.log(pageData_reloadTable);
		indexObject.parents(".layui-inline").find("b").text(indexObject.text());
		indexObject.parent().hide();
		reloadTable(table, commRN_tableID, method_post, commRN_url, curr_reloadTable, pageData_reloadTable);		//根据查询条件局部刷新表格
	}
	//关闭头部导航选项区域
	$(document).click(function () {
		$(".topNav .layui-form-label + ul, .refCommodityList").hide();
	})
	$(document).delegate('.topNav .layui-form-label, .refCommodityList', 'click', function (event) {
		event.stopPropagation();
		console.log("阻止上述事件冒泡");
	})
	//展开或关闭全部商品类别
	$(".showAllCommCategory").click(function () {
		if ($(this).text() === "全部展开") {
			$(this).parent().next().find("li").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		} else {
			$(this).parent().next().find("li").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
		}
	})
	//根据商品分类查询商品
	$(".layui-nav-tree li dl dd").click(function () {
		var indexObject = $(this);
		var layFilter = indexObject.parents(".layui-nav-tree").attr("lay-filter");		//获取当前选项的标识符
		if (layFilter == "commRNByCategory") {		//页面商品表格的查询
			if (bDesertLastOperation) {
				queryCommodityByCategory(indexObject);
			} else {
				layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
					layer.close(index);
					bDesertLastOperation = true;
					queryCommodityByCategory(indexObject);
				}, function (index, layero) {		//点击取消
					layer.close(index);
				})
			}
		} else if (layFilter == "generalCommRNByCategory") {		//弹窗商品表格的查询
			var indexLoading = layer.load(1);		//开启加载层，防止用户在查询过程中点击其他按钮
			var indexCategoryID = indexObject.find("a").attr("indexID");		//获取当前点击的商品小类ID
			popupData_reloadTable.categoryID = indexCategoryID;
			reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, popupData_reloadTable);
		} else { }		//其他情况
	})
	function queryCommodityByCategory (indexObject) {
		var indexLoading = layer.load(1);		//开启加载层，防止用户在查询过程中点击其他按钮
		var indexCategoryID = indexObject.find("a").attr("indexID");		//获取当前点击的商品小类ID
		pageData_reloadTable.categoryID = indexCategoryID;
		reloadTable(table, commRN_tableID, method_post, commRN_url, curr_reloadTable, pageData_reloadTable);
	}
	//表单进行数据验证
	fieldFormat(form);
	//根据关键字模糊搜索商品
	form.on('submit(commoditySearch)', function (data) {
		console.log(data.field);
		var tableID;
		if ($(data.elem).parent().next().hasClass("commodityTableArea")) {
			tableID = $(data.elem).parent().next().find("table").attr("lay-filter");
		} else {
			tableID = $(data.elem).parent().next().attr("lay-filter");
		}
		if (tableID == "commodityList") {
			if (bDesertLastOperation) {
				queryCommodityByKeyword(data);
			} else {
				layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
					layer.close(index);
					bDesertLastOperation = true;
					queryCommodityByKeyword(data);
				}, function (index, layero) {		//点击取消
					layer.close(index);
				})
			}
		} else if (tableID == "popupCommodityList") {
			var keyWord = data.field.queryKeyword;		//获取模糊搜索关键字
			if (keyWord.length <= 64) {
				var indexLoading = layer.load(1);
				popupData_reloadTable.queryKeyword = keyWord;
				reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, popupData_reloadTable);
			}
		}
		return false;
	})
	function queryCommodityByKeyword (data) {
		var keyWord = data.field.queryKeyword;		//获取模糊搜索关键字
		if (keyWord.length <= 64) {
			var indexLoading = layer.load(1);
			pageData_reloadTable.queryKeyword = keyWord;
			reloadTable(table, commRN_tableID, method_post, commRN_url, curr_reloadTable, pageData_reloadTable);
		}
	}
	//即时搜索事件
	window.instantSearch = function (index) {
		$(index).next().click();
	}
	//初始化搜索商品传参（页面层）
	function initQueryCondition () {
		var date = new Date();
		pageData_reloadTable.status = -1;
		pageData_reloadTable.shopID = 2;
		pageData_reloadTable.date1 = "1970/01/01 00:00:00";
		//防止查询不到最新创建的数据
		pageData_reloadTable.date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss");
		pageData_reloadTable.categoryID = -1;
		pageData_reloadTable.queryKeyword = "";
		$(".topNav .layui-inline").find("b").text("所有");
		$(".topNav .layui-inline").find("b").eq(2).text("默认门店");
		shopID = 2;
		$("#leftSide .layui-nav-tree li").eq(0).addClass("layui-nav-itemed").find("dd").addClass("layui-this");
		$("#leftSide .layui-nav-tree li").eq(0).siblings().removeClass("layui-nav-itemed").find("dd").removeClass("layui-this");
		$("#middlePart>.topArea input").val("");
		console.log(pageData_reloadTable);
	}
	//渲染商品表commodityList
	var isUpdateCommodityData = false;
	table.render({
		elem: '#commodityList',
		url: commRN_url,
		id: commRN_tableID,
		method: method_post,
		where: pageData_reloadTable,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList'
		},
		skin: "nob",
		size: 'sm',
		even: true,
		limit: '10',
		limits: [10],
		page: true,
		cols: [[
			{ field: 'name', title: '商品名称', templet: '#commodityName', align: 'center', event: 'detail' },
			{
				field: 'string', title: '商品条形码', align: 'center', event: 'detail',
				templet: function (data) {
					var barcodes = data.commodity.barcodes;
					return barcodes;
				}
			},
			{
				field: 'specification', title: '规格', align: 'center', event: 'detail',
				templet: function (data) {
					var specification = data.commodity.specification;
					return specification;
				}
			},
			{
				field: 'packageUnitID', title: '包装单位', align: 'center', event: 'detail',
				templet: function (data) {
					var packageUnitID = data.commodity.packageUnitName;
					return packageUnitID;
				}
			},
			{
				field: 'latestPricePurchase', title: '最近采购价', align: 'center', event: 'detail',
				templet: function (data) {
					var latestPricePurchase = data.commodity.listSlave2[0].latestPricePurchase;
					if (latestPricePurchase == -1) {
						latestPricePurchase = "";
					} else {
						latestPricePurchase = parseFloat(latestPricePurchase).toFixed(2);
					}
					return latestPricePurchase;
				}
			},
			{
				field: 'priceRetail', title: '零售价', align: 'center', event: 'detail',
				templet: function (data) {
					console.log(data);
					var priceRetail = parseFloat(data.commodity.listSlave2[0].priceRetail).toFixed(2);
					return priceRetail;
				}
			},
			//			{ field: 'priceVIP', title: '会员价', align: 'center', event: 'detail',
			//				templet: function(data){
			//					var priceVIP = parseFloat(data.commodity.priceVIP).toFixed(2);
			//					return priceVIP;
			//				}
			//			}
		]],
		done: function (res, curr, count) {
			console.log(res);
			$("input[name='lastOperationToPicture']").val(LAST_OPERATION_TO_PICTURE_None);
			if (res.ERROR != "EC_NoError") {
				var msg = res.msg == "" ? "查询商品失败" : res.msg;
				layer.msg(msg);
			} else {
				var resData = res.objectList;
				if (resData.length > 0) {
					if (isUpdateCommodityData) {
						var commodityID = -1;
						//根据商品类型获取相应的商品ID
						if (indexCommodityType == 0) {
							commodityID = $(".generalCommodityID").val();
						} else if (indexCommodityType == 1) {
							commodityID = $(".combinedCommodityID").val();
						} else if (indexCommodityType == 3) {
							commodityID = $(".serviceCommodityID").val();
						}
						for (var i = 0; i < resData.length; i++) {
							if (resData[i].commodity.ID == commodityID) {
								$("#middlePart #commodityList + div .layui-table tbody tr").eq(i).addClass("trChoosed");
							}
						}
						isUpdateCommodityData = false;
					} else {
						if (bDesertLastOperation) {
							commodityR1(resData[0]);
							$("#middlePart #commodityList + div .layui-table tbody tr").eq(0).addClass("trChoosed");
						} else {
							layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
								layer.close(index);
								bDesertLastOperation = true;
								commodityR1(resData[0]);
								$("#middlePart #commodityList + div .layui-table tbody tr").eq(0).addClass("trChoosed");
							}, function (index, layero) {		//点击取消
								layer.close(index);
							})
						}
					}
				} else {
					noSuchCommodityData();
					layer.msg("查无商品");
				}
			}
			layer.closeAll('loading');
		}
	});
	//监听商品表格查看商品详情commodityList
	table.on('tool(commodityList)', function (obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var tr = obj.tr;
		switch (layEvent) {
			case "detail":
				if (bDesertLastOperation) {
					commodityR1(data);
					$(tr).addClass("trChoosed").siblings().removeClass("trChoosed");
				} else {
					layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
						layer.close(index);
						bDesertLastOperation = true;
						commodityR1(data);
						$(tr).addClass("trChoosed").siblings().removeClass("trChoosed");
					}, function (index, layero) {		//点击取消
						layer.close(index);
					})
				}
				break;
			default:
				return;
		}
	})
	//填写商品详情(两种类型的商品：单品和组合商品，通过Type判断)
	var indexCommodityType = 0;		//记录当前查看的商品类型，0是普通商品，1是组合商品，3时服务类商品
	var isMultiPackageCommodity = false;		//判断当前查看的商品是否为多包装商品
	function commodityR1 (data) {
		console.log(data);
		var commodityData = data.commodity;		//接收到的商品数据
		var listProviderData = data.listProvider;		//接收到的供应商数据
		var listMultiPackageCommodityData = data.listMultiPackageCommodity;		//接收到的多包装商品数据
		var listBarcodes = data.listBarcodes;		//接收到的商品条形码数据
		var commID = commodityData.ID;
		var commPicture = commodityData.picture == "" ? " " : commodityData.picture + "?timestamp=" + new Date().getTime();
		var commBarcode = listBarcodes.length <= 0 ? "" : listBarcodes[0].barcode;
		var commBarcodeID = listBarcodes.length <= 0 ? "" : listBarcodes[0].ID;
		var commName = commodityData.name;
		var commShortName = commodityData.shortName;
		var commCategoryID = commodityData.categoryID;
		var commPackageUnitID = commodityData.packageUnitID;
		var commBrandID = commodityData.brandID;
		var commSpecification = commodityData.specification;
		var commPriceRetail = commodityData.listSlave2[0].priceRetail;
		var commPriceVIP = commodityData.priceVIP;
		var commPriceWholesale = commodityData.priceWholesale;
		var commType = commodityData.type;
		var commShelfLife = commodityData.shelfLife;
		//		var commPricingType = commodityData.pricingType;
		//		var commPurchaseFlag = commodityData.purchaseFlag;
		//		var commCanChangePrice = commodityData.canChangePrice;
		var commMnemonicCode = commodityData.mnemonicCode;
		var commpropertyValue1 = commodityData.propertyValue1;
		var commpropertyValue2 = commodityData.propertyValue2;
		var commpropertyValue3 = commodityData.propertyValue3;
		var commpropertyValue4 = commodityData.propertyValue4;
		//		var commRuleOfPoint = commodityData.ruleOfPoint;
		var commReturnDays = commodityData.returnDays;
		var commTag = commodityData.tag;
		var commNOStart = commodityData.nOStart;
		var commPurchasingPriceStart = commodityData.purchasingPriceStart;
		var commStartValueRemark = commodityData.startValueRemark;
		var commTotalPriceStart = (parseInt(commNOStart) * parseFloat(commPurchasingPriceStart)).toFixed(2);
		$("#rightSide .topArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
		$("#rightSide .topArea button").removeClass("disabledButton").removeAttr("disabled");
		$("input[name='lastOperationToPicture']").val(LAST_OPERATION_TO_PICTURE_None);
		if (commNOStart == "-1" || commPurchasingPriceStart == "-1") {		//期初数量和期初采购价为-1时代表不存在期初值
			commNOStart = "";
			commPurchasingPriceStart = "";
			commTotalPriceStart = "";
		} else {
			commPurchasingPriceStart = parseFloat(commPurchasingPriceStart).toFixed(2);
		}
		$('#commImage').attr('src', commPicture);		//商品图片信息	

		if (commType == 0) {		//0是普通商品
			if (commPicture == " ") {
				$('.explain').show();
				console.log("显示")
			} else {
				$('.explain').hide();
				console.log("隐藏")
			};
			recoverOrdinaryCommodityForm();
			var commProviderIDs = listProviderData.length > 0 ? listProviderData[0].ID : "";		//普通商品才有供应商
			indexCommodityType = 0;
			form.val("generalCommodityInfo", {
				"ID": commID,
				"barcodes": commBarcode,
				"name": commName,
				"shortName": commShortName,
				"categoryID": commCategoryID,
				"packageUnitID": commPackageUnitID,
				"providerIDs": commProviderIDs,
				"brandID": commBrandID,
				"specification": commSpecification,
				"priceRetail": parseFloat(commPriceRetail).toFixed(2),
				"priceVIP": parseFloat(commPriceVIP).toFixed(2),
				"priceWholesale": parseFloat(commPriceWholesale).toFixed(2),
				"type": commType,
				"shelfLife": commShelfLife,
				//				"pricingType": commPricingType,		//目前无业务使用且页面不需要显示的字段
				//				"purchaseFlag": commPurchaseFlag,		//目前无业务使用且页面不需要显示的字段
				//				"canChangePrice": commCanChangePrice,		//目前无业务使用且页面不需要显示的字段
				"mnemonicCode": commMnemonicCode,
				"propertyValue1": commpropertyValue1,
				"propertyValue2": commpropertyValue2,
				"propertyValue3": commpropertyValue3,
				"propertyValue4": commpropertyValue4,
				//				"ruleOfPoint": commRuleOfPoint,		//目前无业务使用且页面不需要显示的字段
				"returnDays": commReturnDays,
				"tag": commTag,
				"nOStart": commNOStart,
				"purchasingPriceStart": commPurchasingPriceStart,
				"startValueRemark": commStartValueRemark,
				"totalPriceStart": commTotalPriceStart
			})
			$(".toAddBarcode, .toDeleteBarcode").show();
			$(".generalCommBarcode").attr({ "identifier": commBarcodeID });
			$(".generalCommType").children("option").attr("disabled", "disabled");
			$("#rightSide .preliminaryTable tbody tr:first input").attr("readOnly", "readOnly");
			//多个条形码的处理
			$(".otherBarcode").remove();
			if (listBarcodes.length > 1) {
				for (var i = 1; i < listBarcodes.length; i++) {
					var barcodeID = listBarcodes[i].ID;
					var barcode = listBarcodes[i].barcode;
					appendBarcode(barcodeID, barcode);
				}
				$("div.otherBarcode:first label").text("其他条形码");
			}
			//多个供应商的处理
			$(".otherProvider").remove();
			if (listProviderData.length > 1) {	//该商品存在多个供应商
				var providerOption = $(".toAddProvider").siblings("select").html();
				for (var i = 1; i < listProviderData.length; i++) {
					var providerData = listProviderData[i];
					appendProvider(providerOption);
					$("div.otherProvider:last select option").removeAttr("selected");
					$("div.otherProvider:last select").find("option[value='" + providerData.ID + "']").attr("selected", "selected");
				}
				$("div.otherProvider:first label").text("其他供应商");
			}
			//初始化多包装区域信息
			multiPackageAreaInitialization();
			//多包装商品的处理
			var baseUnitName = commodityData.packageUnitName;
			form.val("multiPackage", {
				"baseUnit": commPackageUnitID
			})
			$(".baseUnitName").text(baseUnitName);
			$(".basePriceRetail").val(parseFloat(commPriceRetail).toFixed(2));
			$(".basePriceVIP").val(parseFloat(commPriceVIP).toFixed(2));
			$(".basePriceWholesale").val(parseFloat(commPriceWholesale).toFixed(2));
			$(".baseBarcode").val(commBarcode).attr("identifier", commBarcodeID);
			$(".baseCommName").val(commName);
			if (listMultiPackageCommodityData.length > 0) {		//该商品存在多包装商品
				isMultiPackageCommodity = true;
				var packageUnitOption = $(".toAddUnit").siblings("select").html();
				for (var i = 0; i < listMultiPackageCommodityData.length; i++) {
					var multiPackageComm = new Object();
					var serialNum = i + 1;
					multiPackageComm.refCommodityMultiple = listMultiPackageCommodityData[i].refCommodityMultiple;
					multiPackageComm.packageUnitName = listMultiPackageCommodityData[i].packageUnitName;
					multiPackageComm.priceRetail = parseFloat(listMultiPackageCommodityData[i].listSlave2[0].priceRetail).toFixed(2);
					multiPackageComm.priceVIP = parseFloat(listMultiPackageCommodityData[i].priceVIP).toFixed(2);
					multiPackageComm.priceWholesale = parseFloat(listMultiPackageCommodityData[i].priceWholesale).toFixed(2);
					multiPackageComm.barcode = listMultiPackageCommodityData[i].barcodes.trim();
					multiPackageComm.barcodeID = listMultiPackageCommodityData[i].barcodesID;
					multiPackageComm.name = listMultiPackageCommodityData[i].name;
					multiPackageComm.ID = listMultiPackageCommodityData[i].ID;
					appendPackageUnit(serialNum, packageUnitOption, baseUnitName, multiPackageComm);
					$("#middlePart .multiPackage .layui-form-item:last select option").removeAttr("selected");
					$("#middlePart .multiPackage .layui-form-item:last select").find("option[value='" + listMultiPackageCommodityData[i].packageUnitID + "']").attr("selected", "selected");
				}
				form.render('select', 'multiPackage');
				$(".toChoosedMultiUnit input:checkbox").attr("checked", "checked");
				$(".multiPackageBarcode .baseBarcode").attr("lay-verify", "required|checkBarcode");
				$("#middlePart .multiPackage").show();
			} else {
				//不存在多包装商品数据
			}
			$("#middlePart .combinedSubCommodity, #rightSide .commodityInfo .combinedCommodityInfo, #rightSide .commodityInfo .serviceCommodityInfo").hide();		//其他类型商品信息隐藏
			$("#rightSide .commodityInfo .generalCommodityInfo").show();
			form.render('select', 'generalCommodityInfo');
		} else if (commType == 1) {		//1是组合商品
			$('.explain').hide();
			indexCommodityType = 1;
			form.val("combinedCommodityInfo", {
				"ID": commID,
				"barcodes": commBarcode,
				"name": commName,
				"shortName": commShortName,
				"categoryID": commCategoryID,
				"packageUnitID": commPackageUnitID,
				"specification": commSpecification,
				"type": commType,
				//				"pricingType": commPricingType,		//目前无业务使用且页面不需要显示的字段
				//				"canChangePrice": commCanChangePrice,		//目前无业务使用且页面不需要显示的字段
				"mnemonicCode": commMnemonicCode,
				"tag": commTag
			})
			notSupportManagePicture();		//查看组合商品详情时设置上传图片按钮不可点击
			$(".combinedCommodityInfo .layui-form-item input").attr("readOnly", "readOnly");		//查看组合商品详情时设置输入框只读
			$(".combinedCommodityInfo .layui-form-item select option").attr("disabled", "disabled");		//查看组合商品详情时设置下拉框不可选
			$(".combinedCommodityInfo .layui-form-item select optgroup option").attr("disabled", "disabled");			//查看组合商品详情时设置下拉框不可选
			form.render('select', 'combinedCommodityInfo');
			$(".combinedCommodityInfo .layui-form-item .layui-select-tips").addClass("unabled");
			$(".combinedSubCommodity .layui-table tbody").html("");
			var subCommodityList = commodityData.listSlave1;		//从表信息:组合商品的子商品信息
			for (var i = 0; i < subCommodityList.length; i++) {
				var serialNum = i + 1;
				var subCommID = subCommodityList[i].ID;
				var subCommName = subCommodityList[i].name;
				var subCommBarcodeID = subCommodityList[i].barcodesID;
				var subCommBarcodeName = subCommodityList[i].barcodes.trim();
				var subCommPackageUnitID = subCommodityList[i].packageUnitID;
				var subCommPackageUnitName = subCommodityList[i].packageUnitName;
				var subCommNO = subCommodityList[i].subCommodityNO;
				var subCommPrice = subCommodityList[i].subCommodityPrice;
				var subCommSpecification = subCommodityList[i].specification;
				combinedSubCommTableRender(serialNum, subCommID, subCommName, subCommBarcodeID, subCommBarcodeName, subCommPackageUnitID, subCommPackageUnitName, subCommNO, subCommPrice, subCommSpecification);
				$(".combinedSubCommodity .layui-table tbody tr:last").find("td:first").text(serialNum);
				$(".combinedSubCommodity .layui-table tbody tr:last").find("td:nth-child(4)").text(subCommNO);
				$(".combinedSubCommodity .layui-table tbody tr:last").find("td:nth-child(6)").text(parseFloat(subCommPrice).toFixed(2));
			}
			$(".addSubComm").remove();
			$(".combinedSubCommodity .layui-table tbody tr td:nth-child(2)").css("padding-right", "10px");
			$("#middlePart .multiPackage, #rightSide .commodityInfo .generalCommodityInfo, #rightSide .commodityInfo .serviceCommodityInfo").hide();
			$("#middlePart .combinedSubCommodity, #rightSide .commodityInfo .combinedCommodityInfo").show();
		} else if (commType == 3) {		//3是服务类商品
			if (commPicture == " ") {
				$('.explain').show();
				console.log("显示")
			} else {
				$('.explain').hide();
				console.log("隐藏")
			};
			indexCommodityType = 3;
			form.val("serviceCommodityInfo", {
				"ID": commID,
				"barcodes": commBarcode,
				"name": commName,
				"shortName": commShortName,
				"categoryID": commCategoryID,
				"packageUnitID": commPackageUnitID,
				"brandID": commBrandID,
				"specification": commSpecification,
				"priceRetail": parseFloat(commPriceRetail).toFixed(2),
				"priceVIP": parseFloat(commPriceVIP).toFixed(2),
				"type": commType,
				//				"pricingType": commPricingType,		//目前无业务使用且页面不需要显示的字段
				//				"purchaseFlag": commPurchaseFlag,		//目前无业务使用且页面不需要显示的字段
				//				"canChangePrice": commCanChangePrice,		//目前无业务使用且页面不需要显示的字段
				"mnemonicCode": commMnemonicCode,
				"propertyValue1": commpropertyValue1,
				"propertyValue2": commpropertyValue2,
				"propertyValue3": commpropertyValue3,
				"propertyValue4": commpropertyValue4,
				//				"ruleOfPoint": commRuleOfPoint,		//目前无业务使用且页面不需要显示的字段
				"returnDays": commReturnDays,
				"tag": commTag
			})
			supportManagePicture();
			$(".serviceCommodityBarcode").attr("data-barcodeID", commBarcodeID);
			$(".commodityType").children("option").attr("disabled", "disabled");
			$("#middlePart .combinedSubCommodity, #middlePart .multiPackage, #rightSide .commodityInfo .combinedCommodityInfo, #rightSide .commodityInfo .generalCommodityInfo").hide();		//其他类型商品信息隐藏
			$("#rightSide .commodityInfo .serviceCommodityInfo").show();
			form.render('select', 'serviceCommodityInfo');
		}
	}
	//动态添加商品条形码
	function appendBarcode (ID, barcode) {
		$(".generalCommName").parents(".layui-form-item").before(
			'<div class="layui-form-item otherBarcode">' +
			'<div class="layui-inline">' +
			'<label class="layui-form-label">&emsp;</label>' +
			'<div class="layui-input-inline">' +
			'<input type="text" identifier="' + ID + '" class="layui-input" value="' + barcode + '" onfocus="barcodeManage(this)" lay-verify="required|checkBarcode" maxlength="64" />' +
			'</div>' +
			'</div>' +
			'<i class="layui-icon layui-icon-close-fill" onclick="deleteBarcode(this)"></i>' +
			'</div>'
		);
	}
	//动态添加商品供应商
	function appendProvider (providerOption) {
		$(".generalCommodityInfo .otherInfoArea").before(
			'<div class="layui-form-item otherProvider">' +
			'<div class="layui-inline">' +
			'<label class="layui-form-label">&emsp;</label>' +
			'<div class="layui-input-inline">' +
			'<select lay-filter="otherProvider" lay-verify="required">' +
			providerOption +
			'</select>' +
			'</div>' +
			'</div>' +
			'<i class="layui-icon layui-icon-close-fill" onclick="deleteProvider(this)" title="删除供应商"></i>' +
			'</div>'
		);
	}
	//初始化多包装区域
	function multiPackageAreaInitialization () {
		isMultiPackageCommodity = false;
		$("#middlePart .multiPackage").hide();
		$(".toChoosedMultiUnit input:checkbox").removeAttr("checked");
		$("#middlePart .multiPackage .layui-form-item:first").siblings(".layui-form-item").remove();
		$("#middlePart .multiPackage .layui-table thead tr").find("th:nth-child(2)").nextAll().remove();
		$("#middlePart .multiPackage .layui-table tbody tr").find("td:nth-child(2)").nextAll().remove();
	}
	//动态添加普通商品副单位
	function appendPackageUnit (serialNum, packageUnitOption, baseUnitName, multiPackageComm) {
		$("#middlePart .multiPackage .layui-form-item:last").after(
			'<div class="layui-form-item">' +
			'<div class="layui-inline">' +
			'<label class="layui-form-label">副单位' + serialNum + '</label>' +
			'<div class="layui-input-inline">' +
			'<select lay-filter="subPackageUnit">' +
			packageUnitOption +
			'</select>' +
			'</div>' +
			'<div class="layui-form-mid">=</div>' +
			'<div class="layui-input-inline">' +
			'<input type="text" class="layui-input" lay-verify="required|checkRefCommodityMultiple" value="' + multiPackageComm.refCommodityMultiple + '" onchange="updateSubUnitMultiple(this)" />' +
			'<span class="baseUnitName">' + baseUnitName + '</span>' +
			'</div>' +
			'</div>' +
			'<i class="layui-icon layui-icon-close-fill" onclick="deletePackageUnit(this)"></i>' +
			'</div>'
		);
		$(".multiPackageThead").append('<th>' + multiPackageComm.packageUnitName + '</th>');
		$(".multiPackagePriceRetail").append('<td><input type="text" class="layui-input" value="' + multiPackageComm.priceRetail + '" onchange="commodityInfoHasUpdate()" lay-verify="required|pDecimalsNull|checkTypeOfInt" /></td>');
		$(".multiPackagePriceVIP").append('<td><input type="text" class="layui-input" value="' + multiPackageComm.priceVIP + '" onchange="commodityInfoHasUpdate()" lay-verify="pDecimalsNull|checkTypeOfInt" /></td>');
		$(".multiPackagePriceWholesale").append('<td><input type="text" class="layui-input" value="' + multiPackageComm.priceWholesale + '" onchange="commodityInfoHasUpdate()" lay-verify="pDecimalsNull|checkTypeOfInt" /></td>');
		$(".multiPackageBarcode").append('<td><input type="text" identifier="' + multiPackageComm.barcodeID + '" class="layui-input" value="' + multiPackageComm.barcode + '" onchange="commodityInfoHasUpdate()" lay-verify="required|checkBarcode" maxlength="64" /></td>');
		$(".multiPackageCommName").append('<td><input type="text" class="layui-input" value="' + multiPackageComm.name + '" oninput="updateCommName(this),commodityInfoHasUpdate()" lay-verify="required|commodityName" maxlength="32" /><input type="hidden" value="' + multiPackageComm.ID + '" /></td>');
	}
	//组合商品无选择单品时恢复子商品表格初始状态
	function combinedSubCommTableRestPose () {
		$(".combinedSubCommodity .layui-table tbody").append(
			'<tr>' +
			'<td>1</td>' +
			'<td><i class="layui-icon layui-icon-add-circle addSubComm" title="添加商品" onclick="toChooseGeneralComm()"></i></td>' +
			'<td></td>' +
			'<td></td>' +
			'<td></td>' +
			'<td></td>' +
			'<td></td>' +
			'</tr>'
		);
	}
	//组合商品渲染子商品表
	function combinedSubCommTableRender (serialNum, commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, specification) {
		$(".combinedSubCommodity .layui-table tbody").append(
			'<tr>' +
			'<td><span>' + serialNum + '</span><i class="layui-icon layui-icon-close-fill deleteSubComm" title="删除商品" onclick="deleteSubComm(this)"></i></td>' +
			'<td title="' + commName + '/' + specification + '">' +
			commName + '/' + specification + '<input type="hidden" class="subCommID" value="' + commID + '" />' +
			'<i class="layui-icon layui-icon-add-circle addSubComm" title="添加商品" onclick="toChooseGeneralComm()"></i>' +
			'</td>' +
			'<td>' + barcodeName + '<input type="hidden" class="subCommBarcodeID" value="' + barcodeID + '" /></td>' +
			'<td><input type="text" onchange="subCommNOManage(this)" value="' + NO + '" /></td>' +
			'<td>' + packageUnitName + '<input type="hidden" class="subCommPackageUnitID" value="' + packageUnitID + '" /></td>' +
			'<td><input type="text" onchange="subCommPriceManage(this)" value="' + parseFloat(price).toFixed(2) + '" /></td>' +
			'<td>' + (parseInt(NO) * parseFloat(price)).toFixed(2) + '</td>' +
			'</tr>'
		);
	}
	//上传商品图片
	var uploadCommImage = upload.render({
		elem: '#uploadImage',
		url: commPictureUpload_url,
		accept: 'images',
		acceptMime: 'image/jpg, image/jpeg, image/png',
		exts: 'jpg|jpeg|png',
		size: 100,
		before: function (obj) {
			//预读本地文件示例，不支持ie8
			obj.preview(function (index, file, result) {
				$('#commImage').attr('src', result);		//图片链接（base64
			});
		},
		done: function (res) {
			bDesertLastOperation = false;
			$("input[name='lastOperationToPicture']").val(LAST_OPERATION_TO_PICTURE_Upload);
			if (res.ERROR == "EC_NoError") {		//上传成功
				var msg = res.msg == "" ? "上传图片成功" : res.msg;
				return layer.msg(msg);
			} else {		//上传失败
				var msg = res.msg == "" ? "上传图片失败" : res.msg;
				return layer.msg(msg);
			}
		},
		error: function (index, upload) {
			//演示失败状态，并实现重传
			var demoText = $('#hintText');
			demoText.html('<span style="color: #FF5722;">上传失败</span><a class="layui-btn layui-btn-xs demo-reload">重试</a>');
			demoText.find('.demo-reload').on('click', function () {
				uploadCommImage.upload();
			});
		}
	});
	//清除商品图片
	$("#cleanImage").click(function () {
		$.ajax({
			url: commPictureDelete_url,
			type: method_post,
			async: true,
			dataType: "json",
			data: {},
			success: function succFunction (data) {		//此接口后端不会返回错误码
				$('.explain').show();
				$('#commImage').attr('src', ' ');
				bDesertLastOperation = false;
				$("input[name='lastOperationToPicture']").val(LAST_OPERATION_TO_PICTURE_Clear);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.msg("清空商品图片失败");
			}
		})
	})
	//增加条形码按钮的监听
	$(".toAddBarcode").click(function () {
		var otherBarcodeLength = $(".otherBarcode").length;
		if (otherBarcodeLength >= 7) {
			layer.msg("一个商品最多只能有8个条形码");
		} else {
			appendBarcode("", "");
			$("div.otherBarcode:first label").text("其他条形码");
			$("div.otherBarcode:last .layui-input").focus();
		}
	})
	//创建或修改普通商品的条形码，商品条形码的修改=删除+增加
	window.barcodeManage = function (index) {
		$(index).unbind("blur");
		var commodityID = $(".generalCommodityID").val();
		var barcodeBeforeUpdate = $(index).val();
		var barcodeIDBeforeUpdate = $(index).attr("identifier");
		$(index).blur(function () {
			var barcode = $(this).val();
			if (barcode == barcodeBeforeUpdate) {		//未发生修改
				return;
			}
			var indexLoading = layer.load(1);
			if (/^[A-Za-z0-9]+$/.test(barcode) && barcode.length >= 7 && barcode.length <= 64) {
				//同步普通商品条形码的显示
				if ($(this).hasClass("generalCommBarcode")) {
					$(".baseBarcode").val(barcode);
				} else if ($(this).hasClass("baseBarcode")) {
					$(".generalCommBarcode").val(barcode);
				} else {		//其他情况
				}
				//进行条形码的创建或修改判断
				var curr = $("#commodityList + div .layui-laypage-skip").find("input").val();
				var toUsedUrl, requestData, succText, failText;
				if (barcodeIDBeforeUpdate && commodityID) {		//修改条形码
					toUsedUrl = barcodeUpdate_url;
					requestData = { "CommodityID": commodityID, "ID": barcodeIDBeforeUpdate, "barcode": barcode, "returnObject": 1 };
					succText = "修改条形码成功";
					failText = "修改条形码失败";
				} else if (!barcodeIDBeforeUpdate && commodityID) {		//创建条形码
					toUsedUrl = barcodeCreate_url;
					requestData = { "CommodityID": commodityID, "barcode": barcode, "returnObject": 1 };
					succText = "增加条形码成功";
					failText = "增加条形码失败";
				} else {		//处于创建商品状态
					layer.close(indexLoading);
					return;
				}
				$.ajax({
					url: toUsedUrl,
					type: method_post,
					async: true,
					dataType: "json",
					data: requestData,
					success: function succFunction (data) {
						console.log(data);
						if (data.ERROR == "EC_NoError") {
							var newBarcodeID = data.object.ID;
							var msg = data.msg == "" ? succText : data.msg;
							$(index).attr("identifier", newBarcodeID);
							layer.msg(msg);
							isUpdateCommodityData = true;
							reloadTable(table, commRN_tableID, method_post, commRN_url, curr, pageData_reloadTable);
						} else {
							if (data.msg) {
								layer.msg(data.msg);
							} else {
								layer.msg(failText);
							}
							$(index).val(barcodeBeforeUpdate);
						}
						layer.close(indexLoading);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
						layer.close(indexLoading);
					}
				})
			} else {
				$(this).val(barcodeBeforeUpdate);
				layer.msg('条形码格式错误，仅允许英文、数值形式，长度为[7,64]');
				layer.close(indexLoading);
			}
		})
	}
	//删除其他条形码函数
	window.deleteBarcode = function (index) {
		var barcodeID = $(index).parent().find(".layui-input").attr("identifier");
		var curr = $("#commodityList + div .layui-laypage-skip").find("input").val();
		var callAjax = false;
		var i;
		console.log("toDeleteBarcodeID=" + barcodeID);
		if ($(index).hasClass('toDeleteBarcode')) {
			if ($(".otherBarcode").length > 0) {
				var barcodeNull = true;
				for (i = 0; i < $(".otherBarcode").length; i++) {
					var barcode = $(".otherBarcode").eq(i).find("input").val();
					if (barcode != '') {
						barcodeNull = false;
						callAjax = true;
						break;
					}
				}
				if (barcodeNull) {
					layer.msg("已经是最后一个条形码了，不能删除了");
				}
			} else {
				layer.msg("已经是最后一个条形码了，不能删除了");
			}
		} else {
			if (barcodeID) {
				callAjax = true;
			} else {
				$(index).parent().remove();
				$("div.otherBarcode:first label").text("其他条形码");
			}
		}
		if (callAjax) {
			$.ajax({
				url: barcodeDelete_url,
				type: method_get,
				async: true,
				dataType: "json",
				data: { "ID": barcodeID },
				success: function succFunction (data) {
					console.log(data);
					if (data.ERROR == "EC_NoError") {
						if ($(index).hasClass("toDeleteBarcode")) {
							var barcode = $(".otherBarcode").eq(i).find("input").val();
							var identifier = $(".otherBarcode").eq(i).find("input").attr("identifier");
							$(index).siblings("input").attr("identifier", identifier).val(barcode);
							$(".otherBarcode").eq(i).remove();
							$(".baseBarcode").val(barcode);
						} else {
							$(index).parent().remove();
						}
						$("div.otherBarcode:first label").text("其他条形码");
						var msg = data.msg == "" ? "条形码已删除" : data.msg;
						layer.msg(msg);
						isUpdateCommodityData = true;
						reloadTable(table, commRN_tableID, method_post, commRN_url, curr, pageData_reloadTable);
					} else {
						if (data.msg) {
							layer.msg(data.msg);
						} else {
							layer.msg("条形码删除失败");
						}
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				}
			})
		}
	}
	//修改服务类商品条形码
	$(".serviceCommodityBarcode").focus(function () {
		$(this).unbind("blur");
		var commodityID = $(".serviceCommodityID").val();
		if (commodityID) {
			var barcodeBeforeUpdate = $(this).val();
			var barcodeIDBeforeUpdate = $(this).attr("data-barcodeID");
			$(this).blur(function () {
				var barcode = $(this).val();
				if (barcode == barcodeBeforeUpdate) {		//未发生修改
					return;
				}
				var indexLoading = layer.load(1);
				if (/^[A-Za-z0-9]+$/.test(barcode) && barcode.length >= 7 && barcode.length <= 64) {
					var requestData = { "CommodityID": commodityID, "ID": barcodeIDBeforeUpdate, "barcode": barcode, "returnObject": 1 };
					var curr = $("#commodityList + div .layui-laypage-skip").find("input").val();
					$.ajax({
						url: barcodeUpdate_url,
						type: method_post,
						async: true,
						dataType: "json",
						data: requestData,
						success: function succFunction (data) {
							console.log(data);
							if (data.ERROR == "EC_NoError") {
								var newBarcodeID = data.object.ID;
								$(".serviceCommodityBarcode").attr("data-barcodeID", newBarcodeID);
								layer.msg("修改条形码成功");
								isUpdateCommodityData = true;
								reloadTable(table, commRN_tableID, method_post, commRN_url, curr, pageData_reloadTable);
							} else {
								if (data.msg) {
									layer.msg(data.msg);
								} else {
									layer.msg("修改条形码失败");
								}
								$(".serviceCommodityBarcode").val(barcodeBeforeUpdate);
							}
							layer.close(indexLoading);
						},
						error: function (XMLHttpRequest, textStatus, errorThrown) {
							layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
							layer.close(indexLoading);
						}
					})
				} else {
					$(this).val(barcodeBeforeUpdate);
					layer.msg('条形码格式错误，仅允许英文、数值形式，长度为[7,64]');
					layer.close(indexLoading);
				}
			})
		} else {
			//处于创建商品状态
		}
	})
	//修改商品名称
	window.updateCommName = function (index) {
		var commID = 0;
		var commName = $(index).val().trim();
		if ($(index).hasClass("combinedCommName")) {		//组合商品
			commID = $(".combinedCommodityID").val() == "" ? 0 : $(".combinedCommodityID").val();
			checkUniqueField(commID, commName, index);		//判断用户输入的商品名称是否已存在于数据库
		} else if ($(index).hasClass("serviceCommName")) {
			commID = $(".serviceCommodityID").val() == "" ? 0 : $(".serviceCommodityID").val();
			checkUniqueField(commID, commName, index);		//判断用户输入的商品名称是否已存在于数据库
		} else {
			var i = 0;
			$(".multiPackageCommName td:first").siblings().each(function () {		//判断用户在页面上有无输入相同的商品名称
				if ($(this).find(".layui-input").val() == commName) {
					i += 1;
				}
			})
			if (i <= 1) {
				if ($(index).hasClass("generalCommName") || $(index).hasClass("baseCommName")) {		//普通商品或者多包装区域的单品
					commID = $(".generalCommodityID").val();
				} else {		//多包装商品
					commID = $(index).siblings("input:hidden").val();
				}
				commID = commID == "" ? 0 : commID;
				checkUniqueField(commID, commName, index);		//判断用户输入的商品名称是否已存在于数据库
			} else {
				layer.msg("不能输入相同的商品名称，请检查");
			}
		}
		$(index).val(commName);
		if ($(index).hasClass("generalCommName")) {
			$(".baseCommName").val(commName);
		}
		if ($(index).hasClass("baseCommName")) {
			$(".generalCommName").val(commName);
		}
	}
	//检查商品名称是否已存在
	function checkUniqueField (commID, commName, index) {
		console.log("commID:" + commID);
		console.log("commName:" + commName);
		if (commName) {
			if (/^[\u2014\u4E00-\u9FA5A-Za-z0-9_\()（）* $ # “ ” 、/  -\s]{1,32}$/.test(commName) && commName.trim().length == commName.length) {
				$.ajax({
					url: commRNToCheckUniqueField_url,
					type: method_post,
					async: true,
					dataType: "json",
					data: { "fieldToCheckUnique": 1, "ID": commID, "uniqueField": commName },
					success: function succFunction (data) {
						console.log(data);
						if (data.ERROR != "EC_NoError") {
							var msg = data.msg == "" ? "该商品名称已存在" : data.msg;
							layer.msg(msg, { id: "checkField" });
							$(index).focus();
						} else {
							layer.close(layer.index);
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
					}
				})
			} else {
				layer.msg("商品名称格式错误，请输入中英数值、空格(只允许中间出现)，支持的符号为()（）-——_，长度为（0,32]", { id: "checkField" });
			}
		} else {
			layer.msg("商品名称不能为空", { id: "checkField" });
		}
	}
	//修改商品零售价
	window.updatePriceRetail = function (index) {
		var priceRetail = $(index).val();
		if (/^([0-9][0-9]*)+(\.[0-9]{1,2})?$/.test(priceRetail) && priceRetail <= (Math.abs(2 << 30) - 1)) {
			$(index).val(parseFloat(priceRetail).toFixed(2));
			if ($(index).hasClass("textInput")) {
				$(".basePriceRetail").val(parseFloat(priceRetail).toFixed(2));
			} else {
				form.val("generalCommodityInfo", {
					"priceRetail": parseFloat(priceRetail).toFixed(2)
				})
			}
			var subPackageUnit = $("#middlePart .multiPackage .layui-form-item:first").siblings(".layui-form-item");
			if (subPackageUnit.length > 0) {
				for (var i = 0; i < subPackageUnit.length; i++) {
					var refCommodityMultiple = $(subPackageUnit[i]).find(".baseUnitName").siblings("input").val();
					$(".multiPackagePriceRetail").children().eq(i + 2).find("input").val((parseFloat(priceRetail) * parseInt(refCommodityMultiple)).toFixed(2));
				}
			} else {
				//该商品不存在多包装商品
			}
			var priceVIP = $(".generalCommodityInfo").find('input[name="priceVIP"]').val();
			var priceWholesale = $(".generalCommodityInfo").find('input[name="priceWholesale"]').val();
			if (!priceVIP) {
				form.val("generalCommodityInfo", {
					"priceVIP": parseFloat(priceRetail).toFixed(2)
				})
				$(".basePriceVIP").val(parseFloat(priceRetail).toFixed(2));
			}
			if (!priceWholesale) {
				form.val("generalCommodityInfo", {
					"priceWholesale": parseFloat(priceRetail).toFixed(2)
				})
				$(".basePriceWholesale").val(parseFloat(priceRetail).toFixed(2));
			}
		} else {
			layer.msg("格式错误，请输入非负数字，允许有两位小数");
			if ($(index).hasClass("basePriceRetail")) {
				var originalPrice = $(".generalCommodityInfo").find('input[name="priceRetail"]').val();
				$(index).val(parseFloat(originalPrice).toFixed(2));
			} else {
				var originalPrice = $(".basePriceRetail").val();
				$(index).val(parseFloat(originalPrice).toFixed(2));
			}
		}
	}
	//修改商品会员价
	window.updatePriceVIP = function (index) {
		var priceVIP = $(index).val();
		if (/^([0-9][0-9]*)+(\.[0-9]{1,2})?$/.test(priceVIP) && priceVIP <= (Math.abs(2 << 30) - 1)) {
			$(index).val(parseFloat(priceVIP).toFixed(2));
			if ($(index).hasClass("textInput")) {
				$(".basePriceVIP").val(parseFloat(priceVIP).toFixed(2));
			} else {
				form.val("generalCommodityInfo", {
					"priceVIP": parseFloat(priceVIP).toFixed(2)
				})
			}
			var subPackageUnit = $("#middlePart .multiPackage .layui-form-item:first").siblings(".layui-form-item");
			if (subPackageUnit.length > 0) {
				for (var i = 0; i < subPackageUnit.length; i++) {
					var refCommodityMultiple = $(subPackageUnit[i]).find(".baseUnitName").siblings("input").val();
					$(".multiPackagePriceVIP").children().eq(i + 2).find("input").val((parseFloat(priceVIP) * parseInt(refCommodityMultiple)).toFixed(2));
				}
			} else {
				//该商品不存在多包装商品
			}
		} else {
			if (priceVIP) {
				layer.msg("格式错误，请输入非负数字，允许有两位小数");
				if ($(index).hasClass("basePriceVIP")) {
					var originalPrice = $(".generalCommodityInfo").find('input[name="priceVIP"]').val();
					$(index).val(parseFloat(originalPrice).toFixed(2));
				} else {
					var originalPrice = $(".basePriceVIP").val();
					$(index).val(parseFloat(originalPrice).toFixed(2));
				}
			} else {		//价格为空，会员价可以为空
				$(".basePriceVIP").val("0.00");
				$(".generalCommodityInfo").find('input[name="priceVIP"]').val("0.00");
				$(".basePriceVIP").parent().nextAll().find("input").val("0.00");
			}
		}
	}
	//修改商品批发价
	window.updatePriceWholesale = function (index) {
		var priceWholesale = $(index).val();
		if (/^([0-9][0-9]*)+(\.[0-9]{1,2})?$/.test(priceWholesale) && priceWholesale <= (Math.abs(2 << 30) - 1)) {
			$(index).val(parseFloat(priceWholesale).toFixed(2));
			if ($(index).hasClass("textInput")) {
				$(".basePriceWholesale").val(parseFloat(priceWholesale).toFixed(2));
			} else {
				form.val("generalCommodityInfo", {
					"priceWholesale": parseFloat(priceWholesale).toFixed(2)
				})
			}
			var subPackageUnit = $("#middlePart .multiPackage .layui-form-item:first").siblings(".layui-form-item");
			if (subPackageUnit.length > 0) {
				for (var i = 0; i < subPackageUnit.length; i++) {
					var refCommodityMultiple = $(subPackageUnit[i]).find(".baseUnitName").siblings("input").val();
					$(".multiPackagePriceWholesale").children().eq(i + 2).find("input").val((parseFloat(priceWholesale) * parseInt(refCommodityMultiple)).toFixed(2));
				}
			} else {
				//该商品不存在多包装商品
			}
		} else {
			if (priceWholesale) {
				layer.msg("格式错误，请输入非负数字，允许有两位小数");
				if ($(index).hasClass("basePriceWholesale")) {
					var originalPrice = $(".generalCommodityInfo").find('input[name="priceWholesale"]').val();
					$(index).val(parseFloat(originalPrice).toFixed(2));
				} else {
					var originalPrice = $(".basePriceWholesale").val();
					$(index).val(parseFloat(originalPrice).toFixed(2));
				}
			} else {		//价格为空，批发价可以为空
				$(".basePriceWholesale").val("0.00");
				$(".generalCommodityInfo").find('input[name="priceWholesale"]').val("0.00");
				$(".basePriceWholesale").parent().nextAll().find("input").val("0.00");
			}
		}
	}
	//打开或关闭多单位区域
	$(".toChoosedMultiUnit input:checkbox").click(function () {
		var status = $(this).attr("checked");
		if (status == "checked") {
			$("#middlePart .multiPackage").show();
		} else {
			$("#middlePart .multiPackage").hide();
		}
	})
	//新增副单位按钮的监听
	$(".toAddUnit").click(function () {
		var unitLength = $("#middlePart .multiPackage .layui-form-item").length;
		if (unitLength > 5) {		//最多只能有5个副单位
			layer.msg("副单位数量已达到支持上限</br>请勿再添加副单位，谢谢");
		} else {
			bDesertLastOperation = false;
			var multiPackageComm = new Object();
			var serialNum = $("#middlePart .multiPackage .layui-form-item").length;
			var packageUnitOption = $(".toAddUnit").siblings("select").html();
			var baseUnitName = $("#middlePart .multiPackage .layui-table .baseUnitName").text();
			multiPackageComm.refCommodityMultiple = 1;
			multiPackageComm.packageUnitName = $(".toAddUnit").siblings("select").find("option:first").text();
			multiPackageComm.priceRetail = $(".basePriceRetail").val();
			multiPackageComm.priceVIP = $(".basePriceVIP").val();
			multiPackageComm.priceWholesale = $(".basePriceWholesale").val();
			multiPackageComm.barcode = "";
			multiPackageComm.barcodeID = "";
			multiPackageComm.name = "";
			multiPackageComm.ID = "";
			appendPackageUnit(serialNum, packageUnitOption, baseUnitName, multiPackageComm);
			form.render('select', 'multiPackage');
		}
	})
	//监听商品详情区包装单位的选择
	form.on('select(packageUnit)', function (data) {
		bDesertLastOperation = false;
		var baseUnitName = $(data.othis).find("dl dd.layui-this").text();
		form.val("multiPackage", {
			"baseUnit": data.value
		})
		$(".baseUnitName").text(baseUnitName);
	});
	//监听多包装商品信息区基本包装单位的选择
	form.on('select(baseUnit)', function (data) {
		bDesertLastOperation = false;
		var baseUnitName = $(data.othis).find("dl dd.layui-this").text();
		form.val("generalCommodityInfo", {
			"packageUnitID": data.value
		})
		$(".baseUnitName").text(baseUnitName);
	});
	//监听多包装商品信息区副单位的选择
	form.on('select(subPackageUnit)', function (data) {
		bDesertLastOperation = false;
		var index = $(data.othis).closest(".layui-form-item").index();
		var indexUnitName = $(data.othis).find("dl dd.layui-this").text();
		$(".multiPackageThead").children().eq(index + 1).text(indexUnitName);
	});
	//删除副单位函数
	window.deletePackageUnit = function (index) {
		bDesertLastOperation = false;
		var indexIcon = $(index);
		var index = $(index).closest(".layui-form-item").index();
		indexIcon.parent().remove();
		$(".multiPackageThead").children().eq(index + 1).remove();
		$(".multiPackagePriceRetail").children().eq(index + 1).remove();
		$(".multiPackagePriceVIP").children().eq(index + 1).remove();
		$(".multiPackagePriceWholesale").children().eq(index + 1).remove();
		$(".multiPackageBarcode").children().eq(index + 1).remove();
		$(".multiPackageCommName").children().eq(index + 1).remove();
		var unitItem = $("#middlePart .multiPackage .layui-form-item");
		for (var i = 1; i < unitItem.length; i++) {
			$(unitItem[i]).find(".layui-form-label").text("副单位" + i);
		}
	}
	//修改副单位倍数函数
	window.updateSubUnitMultiple = function (index) {
		bDesertLastOperation = false;
		var indexInput = $(index);
		var subUnitMultiple = indexInput.val();
		if (/^[1-9]\d*$/.test(subUnitMultiple) && subUnitMultiple > 1) {
			var index = indexInput.closest(".layui-form-item").index();
			var basePriceRetail = $(".basePriceRetail").val();
			var basePriceVIP = $(".basePriceVIP").val();
			var basePriceWholesale = $(".basePriceWholesale").val();
			if (basePriceRetail) {
				$(".multiPackagePriceRetail").children().eq(index + 1).find("input").val((parseFloat(basePriceRetail) * parseInt(subUnitMultiple)).toFixed(2));
			} else {
				$(".multiPackagePriceRetail").children().eq(index + 1).find("input").val("");
			}
			if (basePriceVIP) {
				$(".multiPackagePriceVIP").children().eq(index + 1).find("input").val((parseFloat(basePriceVIP) * parseInt(subUnitMultiple)).toFixed(2));
			} else {
				$(".multiPackagePriceVIP").children().eq(index + 1).find("input").val("");
			}
			if (basePriceWholesale) {
				$(".multiPackagePriceWholesale").children().eq(index + 1).find("input").val((parseFloat(basePriceWholesale) * parseInt(subUnitMultiple)).toFixed(2));
			} else {
				$(".multiPackagePriceWholesale").children().eq(index + 1).find("input").val("");
			}
		} else {
			layer.msg("商品倍数需为大于1的正整数");
		}
	}
	//增加供应商按钮的监听
	$(".toAddProvider").click(function () {
		bDesertLastOperation = false;
		var providerOption = $(this).siblings("select").html();
		appendProvider(providerOption);
		$("div.otherProvider:first label").text("其他供应商");
		form.render('select', 'generalCommodityInfo');
	})
	//删除供应商函数
	window.deleteProvider = function (index) {
		var providerNum = $(".otherProvider").length + 1;
		if (providerNum > 1) {
			bDesertLastOperation = false;
			if ($(index).hasClass("toDeleteProvider")) {
				var providerID = $("div.otherProvider:first .layui-input-inline").find("dd.layui-this").attr("lay-value");
				form.val("generalCommodityInfo", {
					"providerIDs": providerID,
				})
				$("div.otherProvider:first").remove();
			} else {
				$(index).parent().remove();
			}
			$("div.otherProvider:first label").text("其他供应商");
		} else {
			layer.msg("商品至少需要有一个供应商<br/>不支持删除全部的供应商");
		}
	}
	//监听商品详情区商品类型的选择
	form.on('select(commodityType)', function (data) {
		console.log(data);
		//切换商品类型的同时，清空图片
		$("#commImage").attr('src', ' ');
		$("#hintText").text("");
		$("input[name='lastOperationToPicture']").val(LAST_OPERATION_TO_PICTURE_None);
		if (data.value == 0) {
			indexCommodityType = 0;
			singleCommodityToCreate();
		} else if (data.value == 1) {
			indexCommodityType = 1;
			combinedCommodityToCreate();
		} else if (data.value == 3) {
			indexCommodityType = 3;
			serviceCommodityToCreate();
		}
	});
	//初始化创建普通商品时的商品详情区
	function singleCommodityToCreate () {
		clearSingleCommodityForm();
		recoverOrdinaryCommodityForm();
		$(".toAddBarcode, .toDeleteBarcode").hide();
		$(".otherBarcode, .otherProvider").remove();
		multiPackageAreaInitialization();		//初始化多包装区域
		form.val("multiPackage", {
			"baseUnit": 1
		})
		$(".baseUnitName").text($("#middlePart .multiPackage .layui-form-select dl dd.layui-this").text());
		$("#middlePart .multiPackage .layui-table tbody tr td").find("input").val("");
		$("#middlePart .combinedSubCommodity").hide();
		$("#rightSide .commodityInfo .generalCommodityInfo").show();
		$("#rightSide .commodityInfo .combinedCommodityInfo").hide();
		$("#rightSide .commodityInfo .serviceCommodityInfo").hide();
	}
	//初始化创建组合商品时的商品详情区
	function combinedCommodityToCreate () {
		popupCommodityData = [];
		clearCombinationCommodityForm();
		$(".combinedCommodityInfo .layui-form-item input").removeAttr("readOnly");
		$(".combinedCommodityInfo .layui-form-item select option").removeAttr("disabled");
		$(".combinedCommodityInfo .layui-form-item select optgroup option").removeAttr("disabled");
		form.render('select', 'combinedCommodityInfo');
		$(".combinedSubCommodity .layui-table tbody").html("");
		combinedSubCommTableRestPose();
		$("#middlePart .combinedSubCommodity").show();
		$("#middlePart .multiPackage").hide();
		$("#rightSide .commodityInfo .generalCommodityInfo").hide();
		$("#rightSide .commodityInfo .combinedCommodityInfo").show();
		$("#rightSide .commodityInfo .serviceCommodityInfo").hide();
	}
	//初始化创建服务类商品时的商品详情区
	function serviceCommodityToCreate () {
		clearServiceCommodityForm();
		$(".serviceCommodityBarcode").removeAttr("data-barcodeID");
		$(".commodityType").children("option").removeAttr("disabled");
		form.render('select', 'serviceCommodityInfo');
		$("#middlePart .combinedSubCommodity").hide();
		$("#middlePart .multiPackage").hide();
		$("#rightSide .commodityInfo .generalCommodityInfo").hide();
		$("#rightSide .commodityInfo .combinedCommodityInfo").hide();
		$("#rightSide .commodityInfo .serviceCommodityInfo").show();
	}
	//期初赋值时
	window.updateInitialInformation = function (index) {
		var keyName = $(index).attr("name");
		var NOStart = parseInt($(".NOStart").val());
		var purchasingPriceStart = parseFloat($(".purchasingPriceStart").val());
		var totalPriceStart = parseFloat($(".totalPriceStart").val());
		switch (keyName) {
			case "nOStart":
				if (NOStart) {
					if (purchasingPriceStart) {
						$(".totalPriceStart").val((NOStart * purchasingPriceStart).toFixed(2));
					} else if (totalPriceStart) {
						$(".purchasingPriceStart").val((totalPriceStart / NOStart).toFixed(2));
					} else {
						console.log("期初采购价和期初总金额为空");
					}
				}
				break;
			case "purchasingPriceStart":
				if (purchasingPriceStart && NOStart) {
					$(".totalPriceStart").val((NOStart * purchasingPriceStart).toFixed(2));
				} else {
					console.log("期初库存为空");
				}
				break;
			case "totalPriceStart":
				if (totalPriceStart && NOStart) {
					$(".purchasingPriceStart").val((totalPriceStart / NOStart).toFixed(2));
				} else {
					console.log("期初库存为空");
				}
				break;
			default:
				return;
		}
	}
	//弹出选择商品弹窗
	window.toChooseGeneralComm = function () {
		layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#toChooseGeneralComm"),
			success: function (layero, index) {
				notRecordActions(popupCommodityData, tempArray);
				reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, popupData_reloadTable);
				$(".popupPage .footArea strong").text(popupCommodityData.length);
			},
			cancel: function (index, layero) {
				notRecordActions(tempArray, popupCommodityData);
			}
		});
	}
	//关闭选择商品弹窗
	$(".closePopupPage").click(function () {
		notRecordActions(tempArray, popupCommodityData);
		layer.closeAll('page');
	})
	//渲染商品表popupCommodityList
	table.render({
		elem: '#popupCommodityList',
		url: commRN_url + "?status=-1&type=0",		//type设为0是为了查询所有的普通商品
		id: popupCommRN_tableID,
		method: method_post,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList'
		},
		skin: "nob",
		even: true,
		limit: '10',
		limits: [10],
		page: true,
		cols: [[
			{ type: 'checkbox' },
			{ field: 'name', title: '商品名称/规格', templet: '#popupCommodityName', width: 150, align: 'center' },
			{
				field: 'string', title: '条形码', align: 'center', width: 160,
				templet: function (data) {
					var barcode = data.listBarcodes.length > 0 ? data.listBarcodes[0].barcode : "";
					return barcode;
				}
			},
			{ field: 'num', title: '数量', templet: '#popupNumManage', width: 150, align: 'center' },
			{
				field: 'packageUnitID', title: '包装单位', align: 'center', width: 90,
				templet: function (data) {
					var packageUnitID = data.commodity.packageUnitName;
					return packageUnitID;
				}
			},
			{
				field: 'priceRetail', title: '零售价', width: 130, align: 'center',
				templet: function (data) {
					var priceRetail = data.commodity.priceRetail;
					return priceRetail;
				}
			}
		]],
		done: function (res, curr, count) {
			if (res.ERROR != "EC_NoError") {
				var msg = res.msg == "" ? "查询商品失败" : res.msg;
				layer.msg(msg);
			} else {
				var data = res.objectList;
				for (var i = 0; i < data.length; i++) {
					var index = data[i].LAY_TABLE_INDEX;
					var indexCommID = data[i].commodity.ID;
					for (var j = 0; j < popupCommodityData.length; j++) {
						if (popupCommodityData[j].commID == indexCommID) {
							$("#popupCommodityList + div").find("tr[data-index='" + index + "'] .layui-form-checkbox").click();
						}
					}
				}
			}
			layer.closeAll('loading');
		}
	});
	//监听商品表格复选框选择popupCommodityList
	table.on('checkbox(popupCommodityList)', function (obj) {
		var checked = obj.checked;		//当前是否选中状态
		var data = obj.data;		//选中行的相关数据
		var type = obj.type;		//如果触发的是全选，则为：all，如果触发的是单选，则为：one
		var tr = obj.tr;		//选中行的dom对象
		if (type == "one") {
			var commID = data.commodity.ID;
			var commName = data.commodity.name;
			var barcodeID = data.listBarcodes[0].ID;
			var barcodeName = data.listBarcodes[0].barcode;
			var packageUnitID = data.commodity.packageUnitID;
			var packageUnitName = data.commodity.packageUnitName;
			var NO = 1;
			var price = data.commodity.priceRetail;
			var specification = data.commodity.specification;
			for (var i = 0; i < popupCommodityData.length; i++) {		//检查是否已存在此商品(为表格切换页面时的bug而补充)
				if (popupCommodityData[i].commID == commID) {
					NO = popupCommodityData[i].NO;
				}
			}
			if (checked) {
				popupCommDataManage("add", commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, specification);
				$(tr).find(".popupCommNum").val(NO);
			} else {
				popupCommDataManage("delete", commID);
				$(tr).find(".popupCommNum").val("");
			}
		} else if (type == "all") {
			data = table.cache.popupCommodityList;
			if (checked) {
				for (var i = 0; i < data.length; i++) {
					var commodityNotExist = true;
					var commID = data[i].commodity.ID;
					var commName = data[i].commodity.name;
					var barcodeID = data[i].listBarcodes[0].ID;
					var barcodeName = data[i].listBarcodes[0].barcode;
					var packageUnitID = data[i].commodity.packageUnitID;
					var packageUnitName = data[i].commodity.packageUnitName;
					var NO = 1;
					var price = data[i].commodity.priceRetail;
					var specification = data[i].commodity.specification;
					for (var j = 0; j < popupCommodityData.length; j++) {		//检查是否已存在此商品(为全选时数量不一致的bug而补充)
						if (popupCommodityData[j].commID == commID) {
							commodityNotExist = false;
						}
					}
					if (commodityNotExist) {
						popupCommDataManage("add", commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, specification);
					}
				}
				$("#popupCommodityList + div .popupCommNum").each(function () {		//为空的输入框初始赋值
					if ($(this).val() == "") {
						$(this).val(1);
					}
				});
			} else {
				for (var i = 0; i < data.length; i++) {
					var commID = data[i].commodity.ID;
					popupCommDataManage("delete", commID);
				}
				$("#popupCommodityList + div .popupCommNum").val("");
			}
		}
	});
	//确定选择商品（组合商品的选择商品弹窗）
	$(".confirmChoosedComm").click(function () {
		$(".combinedSubCommodity .layui-table tbody").html("");
		if (popupCommodityData.length > 0) {
			for (var i = 0; i < popupCommodityData.length; i++) {
				var serialNum = i + 1;
				var subCommID = popupCommodityData[i].commID;
				var subCommName = popupCommodityData[i].commName;
				var subCommBarcodeID = popupCommodityData[i].barcodeID;
				var subCommBarcodeName = popupCommodityData[i].barcodeName;
				var subCommPackageUnitID = popupCommodityData[i].packageUnitID;
				var subCommPackageUnitName = popupCommodityData[i].packageUnitName;
				var subCommNO = popupCommodityData[i].NO;
				var subCommPrice = popupCommodityData[i].price;
				var subCommSpecification = popupCommodityData[i].specification;
				combinedSubCommTableRender(serialNum, subCommID, subCommName, subCommBarcodeID, subCommBarcodeName, subCommPackageUnitID, subCommPackageUnitName, subCommNO, subCommPrice, subCommSpecification);
			}
		} else {
			combinedSubCommTableRestPose();
		}
		layer.closeAll("page");
	})
	//组合商品中某个组合子商品的数量修改监听
	window.subCommNOManage = function (index) {
		var NO = parseInt($(index).val());
		var commID = $(index).parents("tr").find(".subCommID").val();
		var price = parseFloat($(index).parents("td").nextAll().eq(1).find("input").val());
		if (isNaN(NO) || NO <= 0) {
			layer.msg("请输入正确的商品数量");
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commID) {
					NO = popupCommodityData[i].NO;
				}
			}
		} else {
			popupCommDataManage("add", commID, "", "", "", "", "", NO, "", "");
		}
		$(index).val(NO);
		$(index).parents("tr").find("td:last-child").text((NO * price).toFixed(2));
	}
	//组合商品中某个组合子品的价格修改监听
	window.subCommPriceManage = function (index) {
		var price = parseFloat($(index).val()).toFixed(2);
		var commID = $(index).parents("tr").find(".subCommID").val();
		var NO = parseInt($(index).parents("td").prevAll().eq(1).find("input").val());
		if (isNaN(price) || price <= 0) {
			layer.msg("请输入正确的商品单价");
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commID) {
					price = popupCommodityData[i].price;
				}
			}
		} else {
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commID) {
					popupCommodityData[i].price = price;
				}
			}
		}
		$(index).val(parseFloat(price).toFixed(2));
		$(index).parents("tr").find("td:last-child").text((NO * price).toFixed(2));
	}
	//删除组合商品里的某个组合子商品
	window.deleteSubComm = function (index) {
		var commID = $(index).parents("tr").find(".subCommID").val();
		popupCommDataManage("delete", commID);
		$(index).parents("tr").remove();
		var subCommTR = $(".combinedSubCommodity .layui-table tbody tr");		//获取剩余的商品tr单元行
		for (var i = 0; i < subCommTR.length; i++) {
			$(subCommTR[i]).find("span").text(i + 1);
		}
		if (subCommTR.length <= 0) {
			combinedSubCommTableRestPose();
		}
	}
	//右侧详情区域头部按钮的监听
	$(".commodityManage").click(function () {
		var typeOfButton = $(this).text();
		var commodityID, commodityName;
		if (indexCommodityType == 0) {		//当前的对象是普通商品
			commodityName = $(".generalCommName").val();
			commodityID = $(".generalCommodityID").val();
		} else if (indexCommodityType == 1) {		//当前的对象是组合商品
			commodityName = $(".combinedCommName").val();
			commodityID = $(".combinedCommodityID").val();
		} else if (indexCommodityType == 3) {		//当前的对象是服务类商品
			commodityName = $(".serviceCommName").val();
			commodityID = $(".serviceCommodityID").val();
		}
		$(this).addClass("btnChoosed");
		$(this).siblings().removeClass("btnChoosed");
		switch (typeOfButton) {
			case "新建":		//点击新建时默认其行为为创建普通商品
				if (bDesertLastOperation) {
					$('.explain').show();
					bDesertLastOperation = false;
					buttonBeClick_create();		//点击新建按钮时执行的函数
				} else {
					layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
						layer.close(index);
						buttonBeClick_create();		//点击新建按钮时执行的函数
					}, function (index, layero) {		//点击取消
						layer.close(index);
					})
				}
				break;
			case "保存":
				if (indexCommodityType == 0) {		//操作的对象是普通商品
					$(".generalCommodityManage").click();
				} else if (indexCommodityType == 1) {		//操作的对象是组合商品
					$(".combinedCommodityManage").click();
				} else if (indexCommodityType == 3) {		//操作的对象是服务类商品
					$(".serviceCommodityManage").click();
				}
				break;
			case "删除":
				var text = '确认删除商品"' + commodityName + '"吗？',
					failText = "删除商品失败",
					succText = "删除商品成功";
				var curr = $("#commodityList + div .layui-laypage-skip").find("input").val();
				if (table.cache.commodityList.length <= 1) {
					curr = 1;
				}
				var requestData = new Object();
				requestData.ID = commodityID;
				requestData.type = indexCommodityType;
				console.log(requestData);
				$("input[name='lastOperationToPicture']").val(LAST_OPERATION_TO_PICTURE_Clear);
				commodityManage(commDelete_url, method_get, requestData, text, failText, succText, curr);
				break;
			case "取消":
				if (bDesertLastOperation) {
					initQueryCondition();		//初始化模糊搜索商品传参
					reloadTable(table, commRN_tableID, method_post, commRN_url, curr_reloadTable, pageData_reloadTable);
				} else {
					layer.confirm('确定要放弃之前的操作吗？', {
						icon: 3, title: '提示', cancel: function () {
							$("#rightSide .topArea button:eq(1)").addClass("btnChoosed");
							$("#rightSide .topArea button:eq(3)").removeClass("btnChoosed");
						}
					}, function (index, layero) {		//点击确定
						layer.close(index);
						bDesertLastOperation = true;
						initQueryCondition();		//初始化模糊搜索商品传参
						reloadTable(table, commRN_tableID, method_post, commRN_url, curr_reloadTable, pageData_reloadTable);
					}, function (index, layero) {		//点击取消
						layer.close(index);
						$("#rightSide .topArea button:eq(1)").addClass("btnChoosed");
						$("#rightSide .topArea button:eq(3)").removeClass("btnChoosed");
					})
				}
				break;
		}
	})
	//点击新建按钮要执行的函数
	function buttonBeClick_create () {
		indexCommodityType = 0;
		singleCommodityToCreate();
		$("#commImage").attr('src', ' ');
		$("#hintText").text("");
		$("#commodityList + div .layui-table tbody tr").removeClass("trChoosed layui-table-click");
		$("#rightSide .topArea button:eq(2)").attr("disabled", "disabled").addClass("disabledButton");		//点击新建时删除按钮不可用
		$("input[name='lastOperationToPicture']").val(LAST_OPERATION_TO_PICTURE_None);
	}
	//多包装区域表单提交
	var verifyMultiPackageFromResult = false;		//多包装商品信息验证结果
	form.on('submit(verifyMultiPackageFrom)', function (data) {		//验证多单位区域的信息是否符合要求的数据格式
		verifyMultiPackageFromResult = true;
		return false;
	})
	//普通商品表单提交
	form.on('submit(generalCommodityManage)', function (data) {		//普通商品详情表单验证通过会触发回调函数
		var commodityInfo = data.field;
		commodityInfo.shopID = shopID;
		if ((commodityInfo.nOStart && !commodityInfo.purchasingPriceStart) || (!commodityInfo.nOStart && commodityInfo.purchasingPriceStart)) {
			layer.msg("请补充完整期初信息");
			layer.close(loading);
		} else {
			//进行初始值判断和设定
			if (!commodityInfo.priceVIP) {
				commodityInfo.priceVIP = 0.000000;
			}
			if (!commodityInfo.priceWholesale) {
				commodityInfo.priceWholesale = 0.000000;
			}
			//			if(!commodityInfo.ruleOfPoint){
			//				commodityInfo.ruleOfPoint = 1;
			//			}
			if (!commodityInfo.returnDays) {
				commodityInfo.returnDays = 0;
			}
			//			if(!commodityInfo.purchaseFlag){
			//				commodityInfo.purchaseFlag = 1;
			//			}
			if (!commodityInfo.nOStart || !commodityInfo.purchasingPriceStart) {		//	期初数量和期初采购价不能只有一个值
				commodityInfo.nOStart = -1;
				commodityInfo.purchasingPriceStart = -1;
			}
			if ($(".otherProvider").length > 0) {		//存在其他供应商
				var haveSameProvider = false;
				$(".otherProvider").each(function () {
					var thisProID = $(this).find(".layui-form-select .layui-this").attr("lay-value");
					haveSameProvider = checkIfTheSameExists(thisProID, commodityInfo.providerIDs, haveSameProvider);
					commodityInfo.providerIDs += "," + thisProID;
				})
				if (haveSameProvider) {
					layer.msg("存在相同的供应商，请修改");
					return;
				}
			}
			var text = "", failText = "", succText = "";
			var curr = $("#commodityList + div .layui-laypage-skip").find("input").val();
			if (commodityInfo.ID) {		//赋予修改普通商品的接口
				toUsedUrl = commUpdate_url;
				text = '确认修改商品"' + commodityInfo.name + '"吗?';
				failText = "修改商品失败";
				succText = "修改商品成功";
			} else {		//赋予创建普通商品的接口
				toUsedUrl = commCreate_url;
				text = '确认新建商品"' + commodityInfo.name + '"吗?';
				failText = "创建商品失败";
				succText = "创建商品成功";
				curr = 1;
				commodityInfo.returnObject = 1;
				delete commodityInfo["ID"];
			}
			//multiPackagingInfo格式：条形码，包装单位，商品倍数，零售价，会员价， 批发价，商品名称 以X,X,X; X,X,X; X,X,X;
			if (isMultiPackageCommodity || $("#middlePart .multiPackage").css("display") == "block") {		//原先就是多包装商品或原先不是多包装商品但是多包装区域已打开
				$(".verifyMultiPackageFrom").click();
				console.log(verifyMultiPackageFromResult);
				console.log(commodityInfo.multiPackagingInfo);
				if (verifyMultiPackageFromResult) {		//多包装区域验证结果正确
					console.log(commodityInfo.multiPackagingInfo);
					var packageUnitLength = $("#middlePart .multiPackage .layui-form-item").length;
					var barcodeString = commodityInfo.barcodes;
					var packageUnitIDString = commodityInfo.packageUnitID;
					var multiplierString = 1;
					var priceRetailString = commodityInfo.priceRetail;
					var priceVIPString = commodityInfo.priceVIP;
					var priceWholesaleString = commodityInfo.priceWholesale;
					var commodityNameString = commodityInfo.name;
					var haveSamePackageUnit = false;
					var haveSameCommodityName = false;
					for (var i = 1; i < packageUnitLength; i++) {
						var multiPackageBarcode = $(".multiPackageBarcode").children().eq(i + 1).find("input").val();
						var multiPackagePackageUnitID = $("#middlePart .multiPackage .layui-form-item").eq(i).find(".layui-form-select .layui-this").attr("lay-value");
						var multiPackageMultiplier = $("#middlePart .multiPackage .layui-form-item").eq(i).find(".baseUnitName").prev().val();
						var multiPackagePriceRetail = $(".multiPackagePriceRetail").children().eq(i + 1).find("input").val();
						var multiPackagePriceVIP = $(".multiPackagePriceVIP").children().eq(i + 1).find("input").val();
						var multiPackagePriceWholesale = $(".multiPackagePriceWholesale").children().eq(i + 1).find("input").val();
						var multiPackageCommodityName = $(".multiPackageCommName").children().eq(i + 1).find("input").val();
						if (!multiPackagePriceVIP) {
							multiPackagePriceVIP = 0.000000;
						}
						if (!multiPackagePriceWholesale) {
							multiPackagePriceWholesale = 0.000000;
						}
						haveSamePackageUnit = checkIfTheSameExists(multiPackagePackageUnitID, packageUnitIDString, haveSamePackageUnit);
						haveSameCommodityName = checkIfTheSameExists(multiPackageCommodityName, commodityNameString, haveSameCommodityName);
						barcodeString += "," + multiPackageBarcode;
						packageUnitIDString += "," + multiPackagePackageUnitID;
						multiplierString += "," + multiPackageMultiplier;
						priceRetailString += "," + multiPackagePriceRetail;
						priceVIPString += "," + multiPackagePriceVIP;
						priceWholesaleString += "," + multiPackagePriceWholesale;
						commodityNameString += "," + multiPackageCommodityName;
					}
					if (haveSamePackageUnit) {
						layer.msg("存在相同的包装单位，请修改", { "id": "checkIfHaveSameField" });
					} else if (haveSameCommodityName) {
						layer.msg("存在相同的商品名称，请修改", { "id": "checkIfHaveSameField" });
					} else {
						commodityInfo.multiPackagingInfo = barcodeString + ";" + packageUnitIDString + ";" + multiplierString + ";" + priceRetailString + ";" + priceVIPString + ";" + priceWholesaleString + ";" + commodityNameString + ";";
						console.log(commodityInfo.multiPackagingInfo);
						continueSendingRequests = true;
						commodityManage(toUsedUrl, method_post, commodityInfo, text, failText, succText, curr);
					}
				} else {		//多包装区域验证结果不正确
					$("#middlePart .multiPackage").show();
					$(".toChoosedMultiUnit input:checkbox").attr("checked", "checked");
				}
				verifyMultiPackageFromResult = false;		//恢复初始设置
			} else {		//原先不是多包装商品且多包装区域未打开
				commodityInfo.multiPackagingInfo = commodityInfo.barcodes + ";" + commodityInfo.packageUnitID + ";1;" + commodityInfo.priceRetail + ";" + commodityInfo.priceVIP + ";" + commodityInfo.priceWholesale + ";" + commodityInfo.name + ";";
				console.log(commodityInfo);
				commodityManage(toUsedUrl, method_post, commodityInfo, text, failText, succText, curr);
			}
		}
		return false;
	})
	//检查拼接的字符串是否已存在
	function checkIfTheSameExists (obj, string, checkResult) {
		var array = string.split(",");
		for (var i = 0; i < array.length; i++) {
			if (obj == array[i]) {
				checkResult = true;
			}
		}
		return checkResult;
	}
	//组合商品表单的提交
	form.on('submit(combinedCommodityManage)', function (data) {		//组合商品详情表单验证通过会触发回调函数
		var commodityInfo = data.field;
		if (commodityInfo.ID) {		//存在ID为修改操作，组合商品不允许修改
			layer.msg("组合商品暂时不允许修改");
		} else if (popupCommodityData.length <= 1) {		//不存在ID为创建操作,组合商品需要两个或两个以上的商品组合
			layer.msg("至少需要选择两个商品");
		} else {		//进行创建操作
			var text = '确认新建组合商品"' + commodityInfo.name + '"吗?',
				failText = "创建组合商品失败",
				succText = "创建组合商品成功";
			delete commodityInfo["ID"];
			var listSlave1 = new Array();
			var subCommodityList = new Object();
			var totalPrice = 0;
			for (var i = 0; i < popupCommodityData.length; i++) {
				var subCommodity = new Object();
				subCommodity.subCommodityID = popupCommodityData[i].commID;
				subCommodity.subCommodityNO = popupCommodityData[i].NO;
				subCommodity.price = popupCommodityData[i].price;
				totalPrice = (parseFloat(totalPrice) + parseInt(popupCommodityData[i].NO) * parseFloat(popupCommodityData[i].price)).toFixed(2);
				listSlave1.push(subCommodity);
			}
			subCommodityList.listSlave1 = listSlave1;
			commodityInfo.subCommodityInfo = JSON.stringify(subCommodityList);
			commodityInfo.priceRetail = totalPrice;
			//......?
			commodityInfo.multiPackagingInfo = commodityInfo.barcodes + ";" + commodityInfo.packageUnitID + ";1;" + commodityInfo.priceRetail + ";" + commodityInfo.priceVIP + ";" + commodityInfo.priceWholesale + ";" + commodityInfo.name + ";";
			commodityInfo.returnObject = 1;
			console.log(commodityInfo);
			commodityManage(commCreate_url, method_post, commodityInfo, text, failText, succText, curr_reloadTable);
		}
		return false;
	})
	//服务类商品表单的提交
	form.on('submit(serviceCommodityManage)', function (data) {		//服务类商品详情表单验证通过会触发回调函数
		var commodityInfo = data.field;
		if (!commodityInfo.priceVIP) {
			commodityInfo.priceVIP = 0.000000;
		}
		if (!commodityInfo.returnDays) {
			commodityInfo.returnDays = 0;
		}
		commodityInfo.multiPackagingInfo = commodityInfo.barcodes + ";" + commodityInfo.packageUnitID + ";1;" + commodityInfo.priceRetail + ";" + commodityInfo.priceVIP + ";0.000000;" + commodityInfo.name + ";";
		commodityInfo.returnObject = 1;		//让后端返回执行后的商品最新信息
		if (commodityInfo.ID) {
			var text = '确认修改服务类商品"' + commodityInfo.name + '"吗?',
				failText = "修改服务类商品失败", succText = "修改服务类商品成功";
			var curr = $("#commodityList + div .layui-laypage-skip").find("input").val();
			console.log(commodityInfo);
			commodityManage(commUpdate_url, method_post, commodityInfo, text, failText, succText, curr);
		} else {
			delete commodityInfo["ID"];
			var text = '确认新建服务类商品"' + commodityInfo.name + '"吗?',
				failText = "创建服务类商品失败", succText = "创建服务类商品成功";
			console.log(commodityInfo);
			commodityManage(commCreate_url, method_post, commodityInfo, text, failText, succText, curr_reloadTable);
		}
		return false;
	})

	//ajax：商品管理（创建、修改、删除）
	function commodityManage (url, method, data, text, failText, succText, curr) {
		layer.confirm(text, { icon: 3, title: '提示' }, function (index) {
			var loading = layer.load(2);
			$.ajax({
				url: url,
				type: method,
				async: true,
				data: data,
				dataType: "json",
				success: function succFunction (data) {
					console.log(data);
					layer.close(index);
					layer.close(loading);
					bDesertLastOperation = true;
					switch (data.ERROR) {
						case "EC_NoError":
							if (data.msg) {
								layer.msg(data.msg);
							} else {
								layer.msg(succText);
							}
							if (url == commCreate_url) {
								initQueryCondition();
							} else if (url == commUpdate_url) {
								isUpdateCommodityData = true;
							}
							$("#rightSide .topArea button:eq(0)").removeClass("btnChoosed");
							$("#rightSide .topArea button:eq(1)").addClass("btnChoosed").removeClass("disabledButton").removeAttr("disabled");
							$("#rightSide .topArea button:eq(2), #rightSide .topArea button:eq(3)").removeClass("btnChoosed").removeAttr("disabled").removeClass("disabledButton");
							var date = new Date();//防止查询不到最新创建的数据
							pageData_reloadTable.date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss");
							reloadTable(table, commRN_tableID, method_post, commRN_url, curr, pageData_reloadTable);
							break;
						case "EC_PartSuccess":
							if (url == commCreate_url) {
								initQueryCondition();
							}
							if (data.msg) {
								layer.msg(data.msg);
							} else {
								layer.msg("部分操作成功");
							}
							$("#rightSide .topArea button:eq(0)").removeClass("btnChoosed");
							$("#rightSide .topArea button:eq(1)").addClass("btnChoosed").removeClass("disabledButton").removeAttr("disabled");
							$("#rightSide .topArea button:eq(2), #rightSide .topArea button:eq(3)").removeClass("btnChoosed").removeAttr("disabled").removeClass("disabledButton");
							reloadTable(table, commRN_tableID, method_post, commRN_url, curr, pageData_reloadTable);
							break;
						default:
							if (data.msg) {
								layer.msg(data.msg);
							} else {
								layer.msg(failText);
							}
							return;
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
					layer.close(loading);
				}
			})
		}, function (index) {
		});
	}
	//商品库表格渲染refCommodityList
	var firstRenderRefCommTable = true;		//第一次渲染商品参考库表格
	table.render({
		elem: '#refCommodityList',
		url: refCommRN_url,
		id: refCommRN_tableID,
		width: 380,
		where: refData_reloadTable,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList'
		},
		skin: "nob",
		size: 'sm',
		even: true,
		limit: '10',
		limits: [10],
		page: true,
		cols: [[
			{ type: 'numbers', title: '序号', fixed: 'left' },
			{ field: 'barcode', title: '商品条形码', width: 100, align: 'center', event: 'choose', sort: true, fixed: 'left' },
			{ field: 'name', title: '商品名称', width: 100, align: 'center', event: 'choose', fixed: 'left' },
			{ field: 'shortName', title: '商品简称', width: 80, align: 'center', event: 'choose' },
			{ field: 'specification', title: '规格', width: 80, align: 'center', event: 'choose' },
			{ field: 'packageUnitName', title: '包装单位', width: 80, align: 'center', event: 'choose' },
			{ field: 'brandName', title: '品牌名称', width: 80, align: 'center', event: 'choose' },
			{ field: 'categoryName', title: '类别名称', width: 80, align: 'center', event: 'choose' },
			{ field: 'pricePurchase', title: '进货价', width: 80, align: 'center', event: 'choose', sort: true },
			//			{ field: 'priceVIP', title: '会员价', width: 80, align: 'center', event: 'choose' },
			//			{ field: 'priceWholesale', title: '批发价', width: 80, align: 'center', event: 'choose' },
			{ field: 'shelfLife', title: '保质期', width: 80, align: 'center', event: 'choose', sort: true },
			{ field: 'returnDays', title: '退货天数', width: 80, align: 'center', event: 'choose' }
		]],
		done: function (res) {
			if (!firstRenderRefCommTable) {
				if (res.ERROR != "EC_NoError") {
					var msg = res.msg == "" ? "查询商品失败" : res.msg;
					layer.msg(msg);
				} else {
					if (res.objectList.length > 1) {		//返回多条数据
						$(".refCommodityList").show();
					} else {
						if (res.objectList.length > 0) {		//返回一条数据
							var data = res.objectList[0];
							infillingCommInfo(data);
						} else {		//返回零条数据
							layer.msg("参考商品库没有此商品");
						}
						$(".refCommodityList").hide();
					}
				}
			} else {
				firstRenderRefCommTable = false;
			}
		}
	});
	//商品参考库表格监听refCommodityList
	table.on('tool(refCommodityList)', function (obj) {
		var data = obj.data;
		var layEvent = obj.event;
		switch (layEvent) {
			case 'choose':
				infillingCommInfo(data);
				break;
		}
	})
	//快速填入商品数据
	function infillingCommInfo (data) {
		//赋值普通商品基础信息
		form.val("generalCommodityInfo", {
			"barcodes": data.barcode,
			"name": data.name,
			"shortName": data.shortName,
			"specification": data.specification,
			"mnemonicCode": data.mnemonicCode,
			"priceRetail": data.priceRetail,
			"priceVIP": data.priceVIP,
			"priceWholesale": data.priceWholesale,
			"shelfLife": data.shelfLife,
			"returnDays": data.returnDays,
			//			"pricingType": data.pricingType,		//目前无业务使用且页面不需要显示的字段
			"type": data.type
		})
		//检查商品名称是否已存在
		checkUniqueField(0, data.name, $(".generalCommName"));
		//存在的下拉框
		var packageUnitName = data.packageUnitName;
		var brandName = data.brandName;
		var categoryName = data.categoryName;
		var packageUnitID;
		$(".generalCommPackageUnit, .generalCommBrand, .generalCommCategory").children().removeAttr("selected");
		var packageUnitOptionList = $(".generalCommPackageUnit").children();
		var brandNameOptionList = $(".generalCommBrand").children();
		var categoryNameOptionList = $(".generalCommCategory").find("optgroup>option");
		for (var i = 0; i < packageUnitOptionList.length; i++) {
			if ($(packageUnitOptionList[i]).text() == packageUnitName) {
				$(packageUnitOptionList[i]).attr("selected", "selected");
				packageUnitID = $(packageUnitOptionList[i]).attr("value");
			}
		}
		for (var i = 0; i < brandNameOptionList.length; i++) {
			if ($(brandNameOptionList[i]).text() == brandName) {
				$(brandNameOptionList[i]).attr("selected", "selected");
			}
		}
		for (var i = 0; i < categoryNameOptionList.length; i++) {
			if ($(categoryNameOptionList[i]).text() == categoryName) {
				$(categoryNameOptionList[i]).attr("selected", "selected");
			}
		}
		form.render('select', 'generalCommodityInfo');
		//同步多包装区域信息
		form.val("multiPackage", {
			"baseUnit": packageUnitID
		})
		$(".baseUnitName").text(packageUnitName);
		$(".basePriceRetail").val(data.priceRetail);
		$(".basePriceVIP").val(data.priceVIP);
		$(".basePriceWholesale").val(data.priceWholesale);
		$(".baseBarcode").val(data.barcode);
		$(".baseCommName").val(data.name);
		$(".refCommodityList").hide();
	}
	//通过回车事件查找商品库
	$(".generalCommBarcode").keypress(function (e) {
		var commodityID = $(".generalCommodityID").val();
		if (!commodityID) {		//创建商品的时候才会触发
			var barcode = $(".generalCommBarcode").val();
			if (e.which == 13) {
				var area = $(".generalCommBarcode").offset();
				var inputHeight = $(".generalCommBarcode").css("height");
				refData_reloadTable.barcode = barcode;
				reloadTable(table, refCommRN_tableID, method_get, refCommRN_url, curr_reloadTable, refData_reloadTable);
				$(".refCommodityList").css({ "top": area.top + parseInt(inputHeight), "left": area.left });
			}
		}
	})
	//当无商品数据时
	function noSuchCommodityData () {
		//禁止使用图片功能
		notSupportManagePicture();
		//清空默认显示的普通商品表单内容
		clearSingleCommodityForm();
		$('#commImage').attr('src', ' ');
		$(".toAddBarcode, .toDeleteBarcode").hide();
		$(".toAddProvider, .toDeleteProvider").hide();
		$(".otherBarcode, .otherProvider").remove();
		//设置普通商品表单不可用
		$(".generalCommodityInfo input").attr("readonly", "readonly");
		$(".generalCommodityInfo select option").attr("disabled", "disabled");
		form.render('select', 'generalCommodityInfo');
		$(".generalCommodityInfo .layui-select-tips").addClass("layui-disabled");		//给类别的"请选择"选项添加不可点击状态
		$(".toChoosedMultiUnit input:checkbox").attr("disabled", "disabled");		//不可打开多包装区域
		//显示普通商品表单并隐藏其他表单
		$(".generalCommodityInfo").show();
		$(".combinedCommodityInfo").hide();
		$(".serviceCommodityInfo").hide();
		$("#middlePart .combinedSubCommodity").hide();
		$("#middlePart .multiPackage").hide();
		//设置详情区头部按钮的状态
		$("#rightSide .topArea button:eq(0)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
		$("#rightSide .topArea button:not(:eq(0))").attr("disabled", "disabled").addClass("disabledButton");
	}
	//恢复普通商品表单功能
	function recoverOrdinaryCommodityForm () {
		//恢复图片功能
		supportManagePicture();
		//显示添加供应商和删除供应商的按钮
		$(".toAddProvider, .toDeleteProvider").show();
		//恢复表单的可编辑性
		$(".generalCommodityInfo input").removeAttr("readonly");
		$(".generalCommodityInfo select option").removeAttr("disabled");
		form.render('select', 'generalCommodityInfo');
		$(".toChoosedMultiUnit input:checkbox").removeAttr("disabled");		//可打开多包装区域
		//设置详情区头部按钮的状态
		$("#rightSide .topArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
		$("#rightSide .topArea button").removeAttr("disabled").removeClass("disabledButton");
	}

	//得到左边小类的ID
	var selectedCategoryID = 1;
	$(".classification").click(function () {
		selectedCategoryID = $(this).attr("indexid")
	});
	//清空普通商品表单
	function clearSingleCommodityForm () {
		var categoryID = 1
		if (selectedCategoryID != -1) {
			categoryID = selectedCategoryID;
		}
		form.val("generalCommodityInfo", {
			"ID": "",
			"barcodes": "",
			"name": "",
			"shortName": "",
			"categoryID": categoryID,
			"packageUnitID": 1,
			"providerIDs": 1,
			"brandID": 1,
			"specification": "",
			"priceRetail": "",
			"priceVIP": "",
			"priceWholesale": "",
			"type": 0,
			"shelfLife": 365,
			//			"pricingType": 1,		//目前无业务使用且页面不需要显示的字段
			//			"purchaseFlag": "",		//目前无业务使用且页面不需要显示的字段
			//			"canChangePrice": 0,		//目前无业务使用且页面不需要显示的字段
			"mnemonicCode": "",
			"propertyValue1": "",
			"propertyValue2": "",
			"propertyValue3": "",
			"propertyValue4": "",
			//			"ruleOfPoint": "",		//目前无业务使用且页面不需要显示的字段
			"returnDays": "",
			"tag": "",
			"nOStart": "",
			"purchasingPriceStart": "",
			"startValueRemark": "",
			"totalPriceStart": ""
		})
	}
	//清空组合商品表单
	function clearCombinationCommodityForm () {
		form.val("combinedCommodityInfo", {
			"ID": "",
			"barcodes": "",
			"name": "",
			"shortName": "",
			"categoryID": 1,
			"packageUnitID": 1,
			"specification": "",
			"type": 1,
			//			"pricingType": 1,		//目前无业务使用且页面不需要显示的字段
			//			"canChangePrice": 0,		//目前无业务使用且页面不需要显示的字段
			"mnemonicCode": "",
			"tag": ""
		})
	}
	//清空服务商品表单
	function clearServiceCommodityForm () {
		form.val("serviceCommodityInfo", {
			"ID": "",
			"barcodes": "",
			"name": "",
			"shortName": "",
			"categoryID": 1,
			"packageUnitID": 1,
			"brandID": 1,
			"specification": "",
			"priceRetail": "",
			"priceVIP": "",
			"type": 3,
			//			"pricingType": 1,		//目前无业务使用且页面不需要显示的字段
			//			"purchaseFlag": "",		//目前无业务使用且页面不需要显示的字段
			//			"canChangePrice": 0,		//目前无业务使用且页面不需要显示的字段
			"mnemonicCode": "",
			"propertyValue1": "",
			"propertyValue2": "",
			"propertyValue3": "",
			"propertyValue4": "",
			//			"ruleOfPoint": "",		//目前无业务使用且页面不需要显示的字段
			"returnDays": 365,
			"tag": ""
		})
	}
	//商品图片功能不可用
	function notSupportManagePicture () {
		$("#uploadImage, #cleanImage").attr("disabled", "disabled").addClass("unabled");
	}
	//商品图片功能可用
	function supportManagePicture () {
		$("#uploadImage, #cleanImage").removeAttr("disabled").removeClass("unabled");
	}
	//商品信息是否发生修改
	window.commodityInfoHasUpdate = function () {
		bDesertLastOperation = false;
	}
	//其他下拉框的监听
	form.on('select(formSelect)', function (data) {
		bDesertLastOperation = false;
	});
	//供应商下拉框监听
	form.on('select(provider)', function (data) {
		bDesertLastOperation = false;
	});
	//其他供应商下拉框监听
	form.on('select(otherProvider)', function (data) {
		bDesertLastOperation = false;
	});
});
