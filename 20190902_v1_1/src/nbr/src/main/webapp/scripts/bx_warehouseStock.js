layui.use(['element', 'layer', 'form', 'table', 'laydate'], function () {
	var element = layui.element;
	var layer = layui.layer;
	var form = layui.form;
	var table = layui.table;
	var laydate = layui.laydate;
	//自定义的全局变量
	const commodityRN_url = 'commodity/retrieveNEx.bx';		//商品列表?status=-1&type=0
	const warehouseRInventory_url = 'warehouse/retrieveInventoryEx.bx';		//库存总额和库存总额最高的商品
	const method_get = "GET";		//get请求方式
	const method_post = "POST";		//post请求方式
	//全局变量
	var showFirstDataOfTable = true;		//储存是否为第一次访问
	var data_reloadTable = {};
	var shopID = 2;		

	//页面加载后执行
	$(document).ready(function(){
		adaptiveScreen();
	})

	//缩放窗口时执行
	window.onresize = function(){
		adaptiveScreen();
	}

	//自适应屏幕函数
	function adaptiveScreen(){
		var windowHeight = $(window).height();
		var leftNavOffsetTop = $(".warehouseListBottom .layui-nav").offset().top;
		$(".warehouseListBottom .layui-nav").css("height", (windowHeight - leftNavOffsetTop) + "px");
	}

	//初始加载页面
	layer.ready(function () {
		data_reloadTable.status = 0;		//status=0不显示停用商品,status=-1显示停用商品
		data_reloadTable.type = 0;		//type=0普通商品
		data_reloadTable.date1 = "1970/01/01 00:00:00";
		var date = new Date();//防止查询不到最新创建的数据
		data_reloadTable.date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss");
		data_reloadTable.shopID = 2;
	});
	//渲染表格 commodityList
	table.render({
		elem: '#commodityList',
		url: commodityRN_url,
		id: 'commodityList',
		method: method_post,
		data: data_reloadTable,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList'
		},
		limit: '10',
		page: true,
		even: 'true',
		size: 'sm',
		skin: 'nob',
		even: true,
		cols: [
			[
				{
					field: 'stockPrice', title: '库存总额：', colspan: 2,
					templet: function (data) {
						return "";
					}
				},
				{
					field: 'stockLastprice', title: '库存总额最高的商品：', colspan: 6,
					templet: function (data) {
						return data.commodity.stockLastprice.toFixed(2);
					}
				}
			],
			[
				{
					field: 'name', title: '商品名称', width: 148, align: 'center', event: "detail",
					templet: function (data) {
						return data.commodity.name + '<input type="hidden" class="commodityID" value="' + data.commodity.ID + '"/>';
					}
				},
				{
					field: 'barcodes', title: '商品条码', width: 150, align: 'center', sort: true, event: "detail",
					templet: function (data) {
						return data.commodity.barcodes;
					}
				},
				{
					field: 'specification', title: '规格', width: 80, align: 'center', sort: true, event: "detail",
					templet: function (data) {
						return data.commodity.specification;
					}
				},
				{
					field: 'packageUnitName', title: '包装单位', width: 80, align: 'center', sort: true, event: "detail",
					templet: function (data) {
						return data.commodity.packageUnitName;
					}
				},
				{
					field: 'latestPricePurchase', title: '最近采购价', width: 118, align: 'center', event: "detail",
					templet: function (data) {
						if (data.commodity.listSlave2[0].latestPricePurchase == -1) {
							return '';
						} else {
							return data.commodity.listSlave2[0].latestPricePurchase.toFixed(2);
						}
					}
				},
				{
					field: 'priceRetail', title: '零售价', width: 80, align: 'center', event: "detail",
					templet: function (data) {
//						return data.commodity.priceRetail.toFixed(2);
						return data.commodity.listSlave2[0].priceRetail.toFixed(2);
					}
				},
				{
					field: 'priceVIP', title: '会员价', width: 80, align: 'center', event: "detail",
					templet: function (data) {
						return data.commodity.priceVIP.toFixed(2);
					}
				}
			]
		],
		done: function (res, curr, count) {
			console.log(res);
			if (res.ERROR != 'EC_NoError') {
				if (res.msg) {
					layer.msg(res.msg);
				} else {
					layer.msg("商品加载失败");
				}
				return;
			}
			stockTotalAndCommotity();
			if (res.objectList.length > 0) {
				reloadCommodityTable(res, curr);
			} else {
				form.val("commodityDetails", {
					"ID": "",
					"barcodes": "",
					"brandName": "",
					"name": "",
					"specification": "",
					"shortName": "",
					"categoryName": "",
					"priceRetail": "",
					"packageUnit": "",
					"priceVIP": "",
					"providerName": "",
					"priceWholesale": "",
					"NO": "",
					"shelfLife": "",
				});

				$("#otherBarcodes").hide();
				$(".commodityHistoryBottomRight .providerName").next().hide();
				$("#otherProvider").hide();
				$(".bottomRight .layui-form a").hide();
				if ($("#units").is(":checked")) {
					$("#units").click();
				}
				$("#units").attr("disabled", "disabled");
				$("#packageUnits").hide();
			}
			return;
		}
	});
	//表单渲染成功后
	window.reloadCommodityTable = function (res, curr) {
		var ID = $(".bottomRight .ID").val();
		if (showFirstDataOfTable) {
			$(".bottomCenter input[name=status]").attr("checked", "checked");		//默认勾选不显示停用商品
			$(".bottomCenter span div.layui-form-checkbox").first().addClass("layui-form-checked");		//默认勾选不显示停用商品(打钩)
			showFirstDataOfTable = false;
		}
		$('#commodityList + div .layui-table-box .layui-table-body table tr:first td').click();		//默认显示第一条数据的详细信息
	}
	window.stockTotalAndCommotity = function () {
		//库存总额和库存总额最高的商品
		$.ajax({
			url: warehouseRInventory_url,
			type: method_get,
			async: true,
			dataType: "json",
			data: {"shopID": shopID},
			success: function succFunction (data) {
				console.log(data);
				if (data.ERROR != "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg('所执行操作失败');
					}
					return;
				}
				if (data.object) {
					$(".layui-table-header .layui-table thead tr:first th:eq(0) div.layui-table-cell span").html("<b>库存总额：" + data.object.fTotalInventory.toFixed(2) + "元</b>");
					$(".layui-table-header .layui-table thead tr:first th:eq(1) div.layui-table-cell span").html("<b title='" + data.object.commodityName + "'>库存总额最高的商品：" + data.object.fMaxTotalInventory.toFixed(2) + "元&nbsp;&nbsp;" + data.object.commodityName + "</b>");
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		})
	}
	//商品详情的方法
	function updateCommodityDetailForm (commodity, providerName, listMultiPackageCommodity, listProvider) {
		$("#units").removeAttr("disabled");
		var barcodesArray = new Array();		//定义一数组 
		barcodesArray = commodity.barcodes.split(" ");		//字符分割
		form.val("commodityDetails", {
			"ID": commodity.ID,
			"barcodes": barcodesArray[0],
			"brandName": commodity.brandName,
			"name": commodity.name,
			"specification": commodity.specification,
			"shortName": commodity.shortName,
			"categoryName": commodity.categoryName,
			"priceRetail": commodity.listSlave2[0].priceRetail.toFixed(2),
			"packageUnit": commodity.packageUnitName,
			"priceVIP": commodity.priceVIP.toFixed(2),
			"providerName": providerName,
			"priceWholesale": commodity.priceWholesale.toFixed(2),
			"NO": commodity.NO,
			"shelfLife": commodity.shelfLife,
		});
		$("#otherBarcodesList").html("");
		if (barcodesArray.length > 2) {		//添加其他条形码
			for (var i = 1; i < barcodesArray.length - 1; i++) {
				$("#otherBarcodesList").append('<input sautocomplete="off" readonly="readonly" class="layui-input" value="' + barcodesArray[i] + '"/>')
			}
			$("#otherBarcodes").show();
		} else {
			$("#otherBarcodes").hide();
		}
		//获取多单位
		$("#packageUnits div:first").html("<h5>多包装基本单位&nbsp;&nbsp; <span>" + commodity.packageUnitName + "</span></h5>");
		$("#packageUnits .multiPackageThead").html('<th style="min-width: 48px;">类别</th><th>' + commodity.packageUnitName + '</th>');
		$("#packageUnits .multiPackagePriceRetail").html('<td>零售价</td><td>' + commodity.priceRetail.toFixed(2) + '</td>');
		$("#packageUnits .multiPackagePriceVIP").html('<td>会员价</td><td>' + commodity.priceVIP.toFixed(2) + '</td>');
		$("#packageUnits .multiPackagePriceWholesale").html('<td>批发价</td><td>' + commodity.priceWholesale.toFixed(2) + '</td>');
		$("#packageUnits .multiPackageNO").html('<td>商品库存</td><td>' + commodity.NO + '</td>');
		$("#packageUnits .multiPackageBarcode").html('<td>条形码</td><td>' + commodity.barcodes + '</td>');
		$("#packageUnits .multiPackageCommName").html('<td>商品名称</td><td>' + commodity.name + '</td>');
		for (var i = 0; i < listMultiPackageCommodity.length; i++) {
			var d = listMultiPackageCommodity[i];
			$("#packageUnits div:first").append("<h5><span style='color: #888888;'>副单位" + (i + 1) + " </span> "
				+ d.packageUnitName + " <span> = &nbsp;" + d.refCommodityMultiple + " " + commodity.packageUnitName + "</span></h5>");
			$("#packageUnits .multiPackageThead").append('<th>' + d.packageUnitName + '</th>');
			$("#packageUnits .multiPackagePriceRetail").append('<td>' + d.priceRetail.toFixed(2) + '</td>');
			$("#packageUnits .multiPackagePriceVIP").append('<td>' + d.priceVIP.toFixed(2) + '</td>');
			$("#packageUnits .multiPackagePriceWholesale").append('<td>' + d.priceWholesale.toFixed(2) + '</td>');
			$("#packageUnits .multiPackageNO").append('<td>' + d.NO + '</td>');
			$("#packageUnits .multiPackageBarcode").append('<td>' + d.barcodes + '</td>');
			$("#packageUnits .multiPackageCommName").append('<td>' + d.name + '</td>');
		}
		if (listMultiPackageCommodity.length > 0) {
			$("#packageUnits").show();
			$("#units").attr("checked", "checked");
		} else {
			$("#packageUnits").hide();
			$("#units").removeAttr("checked");
		}
		//获取多供应商
		if (listProvider.length > 1) {
			$(".bottomRight a").show();
			$("#otherProvider .otherProviderList").html("");
			for (var i = 1; i < listProvider.length; i++) {
				$("#otherProvider .otherProviderList").append('<input sautocomplete="off" readonly="readonly" class="layui-input" value="' + listProvider[i].name + '"/>');
			}
		} else {
			$(".bottomRight a").hide();
			$("#otherProvider").hide();
		}
	}
	//时间筛选
	$("#time").click(function () {
		window.event.stopPropagation();		//阻止冒泡事件
		if ($("#timeList").is(":hidden")) {
			$("#timeList").show();
		} else {
			$("#timeList").hide();
		}
		$("#storeNwarehouseList").hide();
	})
	//时间筛选事件
	$("#timeList li").click(function () {
		$("#time").text($(this).text() + " v");
		$("#timeList").hide();
		var value = $(this).attr("data-keyword");
		var date = new Date();
		var date1 = "";
		var date2 = "";
		switch (value) {
			case "":		//全部时间
				date1 = "1970/1/1 00:00:00";
				break;
			case "1":		//过去一周内
				var sevenDays = new Date(date.getTime() - 168 * 60 * 60 * 1000);
				date1 = sevenDays.format("yyyy/MM/dd") + " 00:00:00";
				break;
			case "2":		//过去一个月内
				var oneMonth = new Date(date.getTime() - 720 * 60 * 60 * 1000);
				date1 = oneMonth.format("yyyy/MM/dd") + " 00:00:00";
				break;
			case "3":		//三个月内
				var threeMonths = new Date(date.getTime() - 2160 * 60 * 60 * 1000);
				date1 = threeMonths.format("yyyy/MM/dd") + " 00:00:00";
				break;
			default:
				date1 = "1970/1/1 00:00:00";
				break;
		}
		date2 = new Date(date.getTime()).format("yyyy/MM/dd hh:mm:ss");
		data_reloadTable.date1 = date1;
		data_reloadTable.date2 = date2;
		reloadTable(table, 'commodityList', 'POST', commodityRN_url, 1, data_reloadTable);
	});
	//门店筛选
	$("#shop").click(function () {
		window.event.stopPropagation();		//阻止冒泡事件
		if ($("#shopList").is(":hidden")) {
			$("#shopList").show();
		} else {
			$("#shopList").hide();
		}
		$("#timeList").hide();
	})
	//门店事件
	$("#shopList li").click(function(){
		var loading = layer.load(1);
		$("#shop").text($(this).text() + " v");
		$("#shopList").hide();
		shopID = $(this).val();
		data_reloadTable.shopID = shopID;
		reloadTable(table, 'commodityList', 'POST', commodityRN_url, 1, data_reloadTable);
		layer.close(loading);
	});
	//门店及仓库
	$("#storeNwarehouse").click(function (e) {
		window.event.stopPropagation();		//阻止冒泡事件
		if ($("#storeNwarehouseList").is(":hidden")) {
			$("#storeNwarehouseList").show();
			var top = $("#storeNwarehouse").offset().top + 18;
			var left = $("#storeNwarehouse").offset().left - 8;
			$("#storeNwarehouseList").css({ "top": top, "left": left });
		} else {
			$("#storeNwarehouseList").hide();
		}
		$("#timeList").hide();
	});
	//门店及仓库事件
	$("#storeNwarehouseList li").click(function () {
		$("#storeNwarehouse").text($(this).text() + " v");
		$("#storeNwarehouseList").hide();
		layer.msg("功能尚未开放，敬请期待");
	});
	$("body").click(function () {
		$("#timeList").hide();
		$("#storeNwarehouseList").hide();
	});
	//分类全部展开
	$(".allCategoryShow").toggle(function () {
		$("#categoryList li").addClass("layui-nav-itemed");
		$(this).text("全部关闭");
	}, function () {
		$("#categoryList li").removeClass("layui-nav-itemed");
		$(this).text("全部展开");
	});
	//数据验证(数据长度由标签属性maxlength限制)
	fieldFormat(form);
	// 模糊搜索商品（搜索名称、7位数及以上的条码）
	form.on('submit(commoditySearch)', function (data) {
		var queryKeyword = data.field.queryKeyword;
		if (queryKeyword.length <= 64) {
			data_reloadTable.queryKeyword = queryKeyword;
			console.log(data_reloadTable);
			reloadTable(table, 'commodityList', 'POST', commodityRN_url, 1, data_reloadTable);
		}
	});
	//按键弹起时触发模糊搜索事件  
	$("div.bottomCenter input.commoditySearch").keyup(function () {
		$("i.commoditySearch").click();
	});
	//根据类别点击重载数据表格
	$("#categoryList dd a").click(function () {
		var categoryID = $(this).attr("indexID");
		data_reloadTable.categoryID = categoryID;
		console.log(data_reloadTable);
		reloadTable(table, 'commodityList', 'POST', commodityRN_url, 1, data_reloadTable);
	});
	//监听表格数据操作commodityList
	table.on('tool(commodityList)', function (obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var tr = obj.tr;
		var d = data.commodity;
		var providerName;
		console.log(data);
		if (data.listProvider.length == 0) {
			providerName = "";
		} else {
			providerName = data.listProvider[0].name;
		}
		switch (layEvent) {
			case "detail":
				updateCommodityDetailForm(data.commodity, providerName, data.listMultiPackageCommodity, data.listProvider);
				break;
		}
	})
	//不显示停用商品
	form.on('checkbox(stop)', function (data) {
		if (data.elem.checked) {
			data_reloadTable.status = 0;
		} else {
			data_reloadTable.status = -1;
		}
		console.log(data_reloadTable);
		reloadTable(table, 'commodityList', 'POST', commodityRN_url, 1, data_reloadTable);
	})
	//过滤零库存商品
	form.on('checkbox(filter)', function (data) {
		if (data.elem.checked) {
			data_reloadTable.NO = 1;
		} else {
			data_reloadTable.NO = 0;
		}
		console.log(data_reloadTable);
		reloadTable(table, 'commodityList', 'POST', commodityRN_url, 1, data_reloadTable);
	})
	//多单位列表的显示
	$("#units").click(function () {
		if ($(this).is(":checked")) {
			$("#packageUnits").show();
		} else {
			$("#packageUnits").hide();
		}
	});
	//多供应商的显示
	$(".bottomRight .layui-form a").click(function () {
		window.event.stopPropagation();		//阻止冒泡事件
		if ($("#otherProvider").is(":hidden")) {
			$("#otherProvider").show();
		} else {
			$("#otherProvider").hide();
		}
	});
});
