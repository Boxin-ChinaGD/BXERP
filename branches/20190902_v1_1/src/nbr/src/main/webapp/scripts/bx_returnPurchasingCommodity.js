layui.use(['element', 'form', 'table', "upload", 'laypage'], function () {
	var element = layui.element;
	var form = layui.form;
	var table = layui.table;
	var upload = layui.upload;		// 文件上传
	var laypage = layui.laypage;		// 分页

	const returnCommodityN_url = "returnCommoditySheet/retrieveNEx.bx";		//查询所有退货单
	const returnCommoditySheetR1_url = "returnCommoditySheet/retrieve1Ex.bx";		//查询一个退货单
	const createreturnCommodity_url = "returnCommoditySheet/createEx.bx";		//创建一个退货单
	const updatereturnCommodity_url = "returnCommoditySheet/updateEx.bx";		//修改退货单
	const approvereturnCommodity_url = "returnCommoditySheet/approveEx.bx";		//审核退货单
	const proRN_url = "provider/retrieveNEx.bx";		//供应商查询路径
	const shopRN_url = "shop/retrieveNEx.bx";		//查询门店接口
	const shopRNByFields_url = 'shop/retrieveNByFieldsEx.bx';		//模糊查询门店
	const proRNB_url = "provider/retrieveNByFieldsEx.bx";		//模糊搜索供应商路径
	const popupCommRN_url = "commodity/retrieveNEx.bx?status=-1&type=0";		 // 商品查询路径
	const proRN_tableID = "providerList";		//供应商列表ID
	const shopRN_tableID = "popupShopList";		//门店表格ID（弹窗）
	const popupCommRN_tableID = "popupCommodityList";
	const method_get = "GET";
	const method_post = "POST";
	const commodity_reloadTable = { "status": -1, "type": 0 };		//商品搜索传参
	const provider_reloadTable = {};		//供应商搜索传参
	const shop_reloadTable = {};		//查询门店的传参
	const returnPurchasing_reloadTable = {};		//采购退货搜索传参
	const curr_reloadTable = 1;			//页码
	const status = ["未审核", "已审核"]
	var providerName = '';		//供应商名称
	var shopName = '';		//门店名称
	var providerID = 0;			//供应商ID
	var shopID = 0;			//门店ID
	var indexReturnCommoditySheetID = 0;		//采购退货单ID
	var toCreateturnCommoditySheet = false;		//是否可以创建退货单 
	var toUpdatereturnCommoditySheet = false;		//是否可以修改退货单 
	var firstVisited = true;		//是否第一次进入页面
	var initialData = [];		//R1初始数据 
	var resetPage = false;		//是否重置页码 true为重置
	var bDesertLastOperation = false;		//存储是否出现"确定要放弃之前的操作吗？"提示框
	var currentPage = 1;		//记录当前页码

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
		var windowWidth = $(window).width();
		if (windowWidth >= 1060) {
			popupPageWidth = "1000px";
		} else {
			popupPageWidth = windowWidth - 60 + "px";
		}
	}

	// 页面初始化
	layer.ready(function () {
		returnPurchasing_reloadTable.status = -1;
		returnPurchasing_reloadTable.date1 = "1970/01/01 00:00:00";
		var date = new Date();
		returnPurchasing_reloadTable.date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss");
		returnPurchasing_reloadTable.staffID = -1;
		returnPurchasing_reloadTable.shopID = -1;
		returnPurchasing_reloadTable.providerID = -1;
		returnPurchasing_reloadTable.queryKeyword = '';
		returnPurchasing_reloadTable.pageIndex = curr_reloadTable;
		returnCommoditySheetRN(returnPurchasing_reloadTable);
		fieldFormat(form);
		//		uploading();// 文件上传
	});

	//退货单的列表数据
	function returnCommoditySheetRN (data) {
		var indexLoading = layer.load(1);
		unavailable("RN");
		var marquee = "";
		$.ajax({
			url: returnCommodityN_url,
			type: method_post,
			async: true,
			dataType: "json",
			data: data,
			success: function succFunction (data) {
				console.log(data);
				//
				if (data.ERROR != "EC_NoError") {
					unavailable("无数据");
					$("#CommodityTable tbody").empty();
					$("#div_span").text("/退货单");
					$(".returnCommoditySN label").text("");
					$(".returnPurchase-list ul").html('<span class="returnPurchaseRNHint">查无数据</span>');
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg("查询退货单失败");
					}
					layer.close(indexLoading);
					return;
				}
				if (data.count > 0) {
					$(".returnPurchase-list ul").empty();
					for (var i = 0; i < data.objectList.length; i++) {
						$(".returnPurchase-list ul").append(
							"<li data='" + data.objectList[i]['ID'] + "'>" +
							"<div class='list_option'>" +
							"<div class='option-status'>" +
							"<label class='status'><span class='iconse circle_color_" + data.objectList[i].status + "'></span>" + status[data.objectList[i].status] + "</label>" +
							"<label class='returnPurchase-time'>" + data.objectList[i]['createDatetime'].substring(0, 10) + "</label>" +
							"<br/>" +
							"<span class='provider-name' title='" + data.objectList[i].providerName + "'>" + data.objectList[i].providerName + "</span>" +
							"<span class='creator'>" + data.objectList[i].staffName + "</span>" +
							"</div>" +
							"</div>" +
							"</li>"
						);
					}
					restoreSelectedStatus();
					$("#pagination").show();
					switchPage(data.count, "turningPage");
					if (firstVisited) {
						firstVisited = false;
						$(".returnPurchase-list ul li").eq(0).click();
					}
				} else {
					unavailable("无数据");
					$(".founder_warp").text("创建人：");
					$("#pagination").hide();
					$(".money").text("");
					$("#CommodityTable tbody").empty();
					$("#div_span").text("/退货单");
					$(".returnCommoditySN label").text("");
					$(".returnPurchase-list ul").html('<span class="returnPurchaseRNHint">查无数据</span>');
				}
				layer.close(indexLoading);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg("服务器错误");
			}
		});
	}

	//翻页
	function switchPage (count, keyword) {
		var pageCurr;
		switch (keyword) {
			case "currentPage":
				pageCurr = currentPage;
				break;
			case "turningPage":
				pageCurr = location.hash.replace('#!indexCurr=', '');
				break;
			default:
				break;
		}
		if (resetPage) {
			resetPage = false;
			pageCurr = 1;
			currentPage = 1;
		}
		laypage.render({		//渲染分页
			elem: 'pagination',
			count: count,		//数据总数，从服务端得到
			limit: 10,		//定义每页显示的数量
			next: ">",
			prev: "<",
			hash: "indexCurr",
			curr: pageCurr,
			layout: ['prev', 'page', 'next'],
			jump: function (obj, first) {
				console.log(obj);
				if (!first) { // 首次不执行
					firstVisited = true;
					booleanVariable("dataChange");
					if (bDesertLastOperation) {
						layer.confirm('确定要放弃之前的操作吗？', {
							icon: 3, title: '提示', cancel: function () {
								switchPage(count, "currentPage");
							}
						}, function (index, layero) {
							currentPage = obj.curr;
							booleanVariable("restoreDefault");
							returnPurchasing_reloadTable.pageIndex = obj.curr;
							returnCommoditySheetRN(returnPurchasing_reloadTable);
							layer.close(index);
						}, function () {
							switchPage(count, "currentPage");
						})
					} else {
						currentPage = obj.curr;
						returnPurchasing_reloadTable.pageIndex = obj.curr;
						returnCommoditySheetRN(returnPurchasing_reloadTable);
					}
				}
			}
		});
	}

	//恢复退货单选中状态
	function restoreSelectedStatus () {
		var returnPurchaseOrderList = $(".returnPurchase-list ul li");
		for (var j = 0; j < returnPurchaseOrderList.length; j++) {
			var returnPurchaseOrderID = $(returnPurchaseOrderList[j]).attr("data");
			if (returnPurchaseOrderID == indexReturnCommoditySheetID) {
				$(returnPurchaseOrderList[j]).addClass("click").siblings().removeClass("click");
			}
		}
	}

	//默认显示第一行数据
	function returnCommoditySheetR1 (returnCommoditySheetID) {
		$(".content li button").removeClass("widthbtn");		//layui有提供按钮样式
		popupCommodityData = [];
		initialData = [];
		booleanVariable("restoreDefault");		//恢复默认值--查看退货单后
		var indexLoading = layer.load(1);
		$.ajax({
			url: returnCommoditySheetR1_url,
			type: method_get,
			async: true,
			dataType: "json",
			data: { "ID": returnCommoditySheetID },
			success: function succFunction (data) {
				layer.close(indexLoading);
				$(".returnCommoditySN").show();		//查询时显示单号
				if (data.ERROR != "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg("查询退货单失败");
					}
					return;
				}
				$(".founder_warp").text("创建人：" + data.returnCommoditySheet.staffName);
				$("#div_span").text(data.returnCommoditySheet.createDatetime.substring(0, 10) + "/" + status[data.returnCommoditySheet.status] + "退货单");
				$(".provider_name").text(data.returnCommoditySheet.providerName).attr("title", data.returnCommoditySheet.providerName);		// 供应商
				$(".providerID").text(data.returnCommoditySheet.providerID);		// 供应商ID
				$(".shopName").text(data.returnCommoditySheet.shopName).attr("title", data.returnCommoditySheet.shopName);		// 门店
				$(".shopID").text(data.returnCommoditySheet.shopID);
				providerID = data.returnCommoditySheet.providerID;
				providerName = data.returnCommoditySheet.providerName;
				shopID = data.returnCommoditySheet.shopID;
				shopName = data.returnCommoditySheet.shopName;
				indexReturnCommoditySheetID = data.returnCommoditySheet.ID;
				$(".returnCommoditySN label").text(data.returnCommoditySheet.sn);
				$("#CommodityTable tbody").empty();
				//将数据写进数组中
				for (var i = 0; i < data.returnCommoditySheet.listSlave1.length; i++) {
					var commID = data.returnCommoditySheet.listSlave1[i].commodity.ID;
					var commName = data.returnCommoditySheet.listSlave1[i].commodityName;
					var barcodeID = data.returnCommoditySheet.listSlave1[i].barcodeID;
					var barcodeName = data.returnCommoditySheet.listSlave1[i].barcodes;
					var packageUnitID = "";
					var packageUnitName = data.returnCommoditySheet.listSlave1[i].packageUnit;
					var NO = data.returnCommoditySheet.listSlave1[i].NO;
					var price = data.returnCommoditySheet.listSlave1[i].purchasingPrice;
					var specification = data.returnCommoditySheet.listSlave1[i].commodity.specification;
					var array = {};
					array.commID = commID;
					array.NO = NO;
					array.price = price;
					initialData.push(array);
					popupCommDataManage("add", commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, specification);
					$(".provider_icon i").hide();
					$(".shop_icon i").hide();
					//渲染表格
					$("#CommodityTable tbody").append(
						"<tr>" +
						"<td style='width:80px;'>" + (i + 1) + "<input type='hidden' name='商品ID' value='" + commID + "'/></td>" +
						"<td><div class='wrap' title='" + barcodeName + "'>" + barcodeName + "</div></td>" +
						"<td><div class='wrap' title='" + commName + "'>" + commName + "</div></td>" +
						"<td>" + packageUnitName + "</td>" +
						"<td>" + NO + "</td>" +
						"<td>" + parseFloat(price).toFixed(2) + "</td>" +
						"<td>" + parseFloat(NO * price).toFixed(2) + "</td>" +
						"</tr>"
					);
					if (data.returnCommoditySheet.status == 0) {
						unavailable("未审核");
						toUpdatereturnCommoditySheet = true;
						$(".content li").addClass("cost_audit_stastu");
						$(".content li button").removeAttr("disabled");
						$(".content li button").text("审核");
						$(".provider_icon").show();
						$(".addGeneralComm").show();
						$("#CommodityTable tbody tr:last td").eq(0).html("<i class='layui-icon layui-icon-close-fill deletecommodity' onclick='deletecommodity(this)'></i><span>" + (i + 1) + "</span><input name='商品ID' type='hidden' value='" + commID + "'/>");
						$("#CommodityTable tbody tr:last td").eq(2).html("<div class='wrap' title='" + commName + "'>" + commName + "</div><button><i class='layui-icon layui-icon-add-circle addGeneralComm' title='添加商品'></i></button>");
						$("#CommodityTable tbody tr:last td").eq(4).html("<input onchange='returnPurchasingCommNOManage(this);' value='" + NO + "' name='NO'>")
						$("#CommodityTable tbody tr:last td").eq(5).html("<input onchange='returnPurchasingCommPriceManage(this);'  value='" + parseFloat(price).toFixed(2) + "' name='price'>")
					} else {
						unavailable("其他");
						toUpdatereturnCommoditySheet = false;
						$(".content li").removeClass("cost_audit_stastu");
						$(".content li button").attr("disabled", "disabled");
						$(".content li button").text("已审核");
					}
				}
				// 表格行颜色
				$("#CommodityTable tbody tr:odd").addClass("odd");
				calculatedPurchaseTotalPrice();
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg("服务器错误");
				layer.close(indexLoading);
			}
		});
	}

	//计算退货单退货商品的总价
	function calculatedPurchaseTotalPrice () {
		var totalPrice = 0;
		$("#CommodityTable tbody tr").each(function () {
			var price = $(this).find("td").eq(6).text();
			totalPrice = (parseFloat(totalPrice) + parseFloat(price)).toFixed(2);
		})
		$(".money").text(totalPrice + "元");
	}

	//恢复表格初始状态
	function purchaseCommTableRestPose () {
		$("#CommodityTable tbody").html(
			"<tr>" +
			"<td style='width:70px;'>1</td>" +
			"<td><button><i class='layui-icon layui-icon-add-circle addGeneralComm' title='添加商品'></i></button></td>" +
			"<td></td>" +
			"<td></td>" +
			"<td></td>" +
			"<td></td>" +
			"<td></td>" +
			"</tr>"
		);
	}

	//创建退货单
	function createReturnCommoditySheet () {
		if (popupCommodityData.length > 0) {
			var commIDs = "";
			var barcodeIDs = "";
			var NOs = "";
			var prices = "";
			var specifications = "";
			for (var i = 0; i < popupCommodityData.length; i++) {
				commIDs += popupCommodityData[i].commID + ",";
				barcodeIDs += popupCommodityData[i].barcodeID + ",";
				NOs += popupCommodityData[i].NO + ",";
				prices += popupCommodityData[i].price + ",";
				specifications += popupCommodityData[i].specification + ",";
				CheckForBarcode(popupCommodityData[i].barcodeID, "创建");
				if (popupCommodityData[i].price < 0) {
					layer.msg("请检查价格合法性");
					return;
				}
				if (popupCommodityData[i].NO <= 0) {
					layer.msg("请检查数量合法性");
					return;
				}
			}
			$.ajax({
				url: createreturnCommodity_url,
				type: method_post,
				async: true,
				dataType: "json",
				data: {
					"providerID": providerID,//$(".providerID").text(),
					"shopID":shopID,
					"commIDs": commIDs,
					"commPrices": prices,
					"rcscNOs": NOs,
					"barcodeIDs": barcodeIDs,
					"rcscSpecifications": specifications
				},
				success: function succFunction (data) {
					console.log(data);
					if (data.ERROR == "EC_NoError") {
						layer.msg("创建成功");
						toCreateturnCommoditySheet = false;
						firstVisited = true;
						resetPage = true;
						booleanVariable("restoreDefault");		//恢复相关变量的默认值--创建退货单成功后(返回所有的数据，并显示第一页第一条退货单)
						initQueryCondition();		//恢复初始导航栏状态
						returnCommoditySheetRN(returnPurchasing_reloadTable);
						//						$(".returnPurchase-list ul li").eq(0).addClass("click").siblings().removeClass("click");
					} else {
						toCreateturnCommoditySheet = true;
						//						booleanVariable("restoreDefault");		//恢复默认值
						if (data.msg) {
							layer.msg(data.msg);
						} else {
							layer.msg("创建失败");
						}
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.msg("服务器错误");
				}
			});
		} else {
			layer.msg('请添加商品');
		}
	}

	//检查有无条形码
	function CheckForBarcode (barcodeID, msg) {
		var layMsg = '';
		switch (msg) {
			case "创建":
				layMsg = "不能创建无条形码商品的退货单";
				break;
			case "修改":
				layMsg = "不能修改无条形码商品的退货单";
				break;
			case "审核":
			default:
				layMsg = "不能审核无条形码商品的退货单";
				break;
		}
		if (barcodeID == null || barcodeID == '') {
			layer.msg(layMsg);
			return;
		}
	}

	//退货单管理(修改和审核功能)
	function returnCommoditySheetManage () {
		var indexLoading = layer.load(1);
		var toUsedUrl;
		var succeedMsg;
		var defeatedMsg;
		var commIDs = "";
		var barcodeIDs = "";
		var NOs = "";
		var prices = "";
		var specifications = "";
		var data = {};
		for (var i = 0; i < popupCommodityData.length; i++) {
			commIDs += popupCommodityData[i].commID + ",";
			barcodeIDs += popupCommodityData[i].barcodeID + ",";
			NOs += popupCommodityData[i].NO + ",";
			prices += popupCommodityData[i].price + ",";
			specifications += popupCommodityData[i].specification + ",";
			if (popupCommodityData[i].price < 0) {
				layer.msg("请检查价格合法性");
				return;
			}
			if (popupCommodityData[i].NO <= 0) {
				layer.msg("请检查数量合法性");
				return;
			}
			if (toUpdatereturnCommoditySheet) {
				CheckForBarcode(popupCommodityData[i].barcodeID, "修改");
			} else {
				CheckForBarcode(popupCommodityData[i].barcodeID, "审核");
			}
		}
		if (toUpdatereturnCommoditySheet) {
			toUsedUrl = updatereturnCommodity_url;
			succeedMsg = "退货单修改成功"
			defeatedMsg = "退货单修改失败"
			data.ID = indexReturnCommoditySheetID;
			data.providerID = providerID;
			data.commIDs = commIDs;
			data.commPrices = prices;
			data.rcscNOs = NOs;
			data.barcodeIDs = barcodeIDs;
			data.rcscSpecifications = specifications;
		} else {
			booleanVariable("dataChange");
			toUsedUrl = approvereturnCommodity_url;
			succeedMsg = "审核退货单成功"
			defeatedMsg = "审核退货单失败"
			if (dataChange) {
				data.ID = indexReturnCommoditySheetID;
				data.providerID = providerID;
				data.commIDs = commIDs;
				data.commPrices = prices;
				data.rcscNOs = NOs;
				data.barcodeIDs = barcodeIDs;
				data.rcscSpecifications = specifications;
				data.bReturnCommodityListIsModified = 1;
			} else {
				data.ID = indexReturnCommoditySheetID;
				data.bReturnCommodityListIsModified = 0;
			}
		}
		initialData = popupCommodityData;
		if (popupCommodityData.length > 0) {
			$.ajax({
				url: toUsedUrl,
				type: method_post,
				async: true,
				dataType: "json",
				data: data,
				success: function succFunction (data) {
					layer.close(indexLoading);
					toCreateturnCommoditySheet = false;
					if (data.ERROR == "EC_NoError" || data.ERROR == "EC_PartSuccess") {
						layer.msg(succeedMsg);
						booleanVariable("restoreDefault");		//恢复默认值--修改/审核退货单成功后
						firstVisited = false;
						var indexObject = $(".returnPurchase-list ul").find("li[data=" + indexReturnCommoditySheetID + "]");
						if (toUsedUrl == updatereturnCommodity_url) {		//修改成功后，刷新相应的数据
							indexObject.click().find(".provider-name").text(providerName).attr("title", providerName);
						} else if (toUsedUrl == approvereturnCommodity_url) {			//审核成功后，刷新相应的数据
							indexObject.find(".status").html('<span class="iconse circle_color_1"></span>已审核');
							indexObject.click().find(".provider-name").text(providerName).attr("title", providerName);
						}
					} else {
						if (data.msg) {
							layer.msg(data.msg);
						} else {
							layer.msg(defeatedMsg);
						}
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.close(indexLoading);
					layer.msg("服务器错误");
				}
			});
		} else {
			layer.msg('请添加商品');
			layer.close(indexLoading);
		}
	}

	var dataChange = false;		//记录退货单是否修改了数据
	var isChangePriceOrNO = false;		//是否修改了退货单商品的价格或数量
	//退货单中某个退货商品的数量修改监听
	window.returnPurchasingCommNOManage = function (index) {
		var NO = parseInt($(index).val());
		var commID = $(index).parents("tr").find('input[name="商品ID"]').val();
		var price = parseFloat($(index).parents("td").next().find("input").val());
		if (isNaN(NO) || NO <= 0 || NO > (Math.abs(2 << 30) - 1)) {
			layer.msg("请输入正确的商品数量");
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commID) {

					NO = popupCommodityData[i].NO;
				}
			}
		} else {
			popupCommDataManage("add", commID, "", "", "", "", "", NO, "", "");
			isChangePriceOrNO = true;
		}
		$(index).val(NO);
		$(index).parents("td").nextAll().eq(1).text((NO * price).toFixed(2));
		calculatedPurchaseTotalPrice();
	}

	//退货单中某个退货商品的价格修改监听
	window.returnPurchasingCommPriceManage = function (index) {
		var price = parseFloat($(index).val()).toFixed(2);
		var commID = $(index).parents("tr").find('input[name="商品ID"]').val();
		var NO = parseInt($(index).parents("td").prev().find("input").val());
		if (isNaN(price) || price < 0 || price > (Math.abs(2 << 30) - 1)) {
			layer.msg("请输入正确的商品价格");
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commID) {
					price = popupCommodityData[i].price;
				}
			}
		} else {
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commID) {
					popupCommodityData[i].price = price;
					isChangePriceOrNO = true;
				}
			}
			console.log(popupCommodityData);
		}
		$(index).val(parseFloat(price).toFixed(2));
		$(index).parents("td").next().text((NO * price).toFixed(2));
		calculatedPurchaseTotalPrice();
	}

	//即时搜索事件
	window.instantSearch = function (index) {
		$(index).next().click();
	}
	//删除商品
	window.deletecommodity = function (obj) {
		dataChange = true;
		bDesertLastOperation = true;
		var $index = $(obj);
		var commID = $index.siblings("input").val();
		console.log(popupCommodityData);
		popupCommDataManage("delete", commID);
		console.log(popupCommodityData);
		$index.parents("tr").remove();
		var purchaseCommTR = $("#CommodityTable tbody tr");		//获取剩余的商品tr单元行
		for (var i = 0; i < purchaseCommTR.length; i++) {
			$(purchaseCommTR[i]).find("span").text(i + 1);
		}
		if (purchaseCommTR.length == 0) {
			purchaseCommTableRestPose();
		}
	}

	//控制boolean变量--dataChange(退货单数据是否修改)、isChangePriceOrNO(是否修改了退货单价格或数量)、bDesertLastOperation(提示框)
	function booleanVariable (keyword) {
		switch (keyword) {
			case "dataChange":		//判断退货单商品的数据(数量、价格)是否修改
				if (isChangePriceOrNO) {
					if (!dataChange) {
						if (initialData.length == popupCommodityData.length) {
							for (var i = 0; i < initialData.length; i++) {
								var NO = popupCommodityData[i].NO;
								var price = popupCommodityData[i].price;
								if (initialData[i].NO != NO || initialData[i].price != price) {
									dataChange = true;
									bDesertLastOperation = true;
								}
							}
						} else {
							dataChange = true;
							bDesertLastOperation = true;
						}
					}
				}
				break;
			case "restoreDefault":		//恢复默认值
				dataChange = false;
				isChangePriceOrNO = false;
				bDesertLastOperation = false;
				break;
			default:
				break;
		}
	}

	//供应商数据渲染
	table.render({
		elem: '#providerList',
		url: proRN_url,
		id: proRN_tableID,
		method: method_post,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList',
		},
		skin: 'nob',
		even: true,
		page: true,
		cols: [[
			{ type: 'numbers', title: '序号' },
			{ templet: '#barDemo', title: '操作', width: 60, align: 'center', event: 'choose' },
			{ field: 'name', title: '供应商名称', width: 160, align: 'center', event: 'choose' },
			{ field: 'contactName', title: '联系人', width: 100, align: 'center', event: 'choose' },
			{ field: 'mobile', title: '联系电话', width: 140, align: 'center', event: 'choose' },
			{ field: 'address', title: '地址', width: 200, align: 'center', event: 'choose' }
		]],
		done: function (res, curr, count) {
			$("#chooseProviderWindow .rightRegion .layui-table tbody tr").each(function (i) {		//翻页后给选择的供应商添加样式
				if ($(this).find("td").eq(2).find("div.layui-table-cell").eq(0).text() == providerName) {
					$(this).find("td").eq(2).find("div.layui-table-cell").click();
				}
			});
		}
	});

	//渲染商品表格
	table.render({
		elem: '#popupCommodityList',
		url: popupCommRN_url,
		method: "post",
		id: popupCommRN_tableID,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList',
		},
		skin: "nob",
		even: true,
		page: true,
		cols: [[
			{ type: 'checkbox' },
			{
				field: 'name', title: '商品名称', width: 170, align: 'center',
				templet: function (data) {
					var name = data.commodity.name;
					return name;
				}
			},
			{
				field: 'string', title: '条形码', align: 'center', width: 150,
				templet: function (data) {
					var barcode = '';
					if (data.listBarcodes.length != 0) {
						barcode = data.listBarcodes[0].barcode;
					}
					return barcode;
				}
			},
			{ field: 'num', title: '数量', templet: '#popupNumManage', width: 150, align: 'center' },
			{
				field: 'packageUnitName', title: '包装单位', align: 'center', width: 90,
				templet: function (data) {
					var packageUnitName = data.commodity.packageUnitName;
					return packageUnitName;
				}
			},
			{
				field: 'latestPricePurchase', title: '最近采购价', width: 100, align: 'center',
				templet: function (data) {
					var latestPricePurchase = data.commodity.listSlave2[0].latestPricePurchase;
					if (latestPricePurchase == -1) {
						latestPricePurchase = "";
					} else {
						latestPricePurchase = parseFloat(latestPricePurchase).toFixed(2);
					}
					return latestPricePurchase;
				}
			}
		]],
		done: function (res, curr, count) {
			// 换页的时候检查新页面是否有已选择的商品
			if (res.ERROR == "EC_NoPermission") {
				if (res.msg) {
					layer.msg(res.msg);
				} else {
					layer.msg("无商品查询权限");
				}
				return;
			}
			var data = res.objectList;
			for (var i = 0; i < data.length; i++) {
				var index = data[i].LAY_TABLE_INDEX;
				var indexCommID = data[i].commodity.ID;
				for (var j = 0; j < popupCommodityData.length; j++) {
					if (popupCommodityData[j].commID == indexCommID) {
						$("#popupCommodityList + div").find("tr[data-index='" + index + "'] .layui-form-checkbox").click()
					}
				}
			}
		}
	});

	// 头部导航选项
	$(".topNav .layui-form-label span").click(function () {
		var index = $(this);
		var area = index.offset();
		var areaParent = index.parent().offset();
		var thisUl = index.parent().next();
		if (index.text() == '默认仓库 v') {
			layer.msg("功能暂未开放");
			return;
		}
		$(".topNav .layui-form-label + ul").hide();
		thisUl.css({
			"display": "block",
			"left": area.left - areaParent.left
		});
	})

	//根据条件查询
	function ConditionSearch (keyword, keydata, domIndex) {
		var index = domIndex.parents(".layui-inline").find(".layui-form-label span");
		var date = new Date();
		resetPage = true;
		firstVisited = true;
		returnPurchasing_reloadTable.pageIndex = curr_reloadTable;
		switch (keyword) {
			case "所有":
				switch (keydata) {
					case "状态":
						returnPurchasing_reloadTable.status = -1;
						break;
					case "门店":
						returnPurchasing_reloadTable.shopID = -1;
						break;
					case "经办人":
						returnPurchasing_reloadTable.staffID = -1;
						break;
					case "供应商":
						returnPurchasing_reloadTable.providerID = -1;
						break;
					case "创建日期":
						returnPurchasing_reloadTable.date1 = "1970/01/01 00:00:00";
						returnPurchasing_reloadTable.date2 = new Date().format("yyyy/MM/dd hh:mm:ss");
						break;
					default:
						break;
				}
				returnCommoditySheetRN(returnPurchasing_reloadTable);
				break;
			case "未审核":
				returnPurchasing_reloadTable.status = 0;
				returnCommoditySheetRN(returnPurchasing_reloadTable);
				break;
			case "已审核":
				returnPurchasing_reloadTable.status = 1;
				returnCommoditySheetRN(returnPurchasing_reloadTable);
				break;
			case "过去一周内":
				var sevenDaysAgo = new Date(date.getTime() - 168 * 60 * 60 * 1000);
				var date1 = sevenDaysAgo.format("yyyy/MM/dd") + " 00:00:00";
				var date2 = date.format("yyyy/MM/dd hh:mm:ss");
				returnPurchasing_reloadTable.date1 = date1;
				returnPurchasing_reloadTable.date2 = date2;
				returnCommoditySheetRN(returnPurchasing_reloadTable);
				break;
			case "过去一月内":
				var sevenDaysAgo = new Date(date.getTime() - 720 * 60 * 60 * 1000);
				var date1 = sevenDaysAgo.format("yyyy/MM/dd") + " 00:00:00";
				var date2 = date.format("yyyy/MM/dd hh:mm:ss");
				returnPurchasing_reloadTable.date1 = date1;
				returnPurchasing_reloadTable.date2 = date2;
				returnCommoditySheetRN(returnPurchasing_reloadTable);
				break;
			default:
				index.find("lable").text(domIndex.clone().children().remove().end().text());
				if (domIndex.find("span").text() == "经办人") {
					returnPurchasing_reloadTable.staffID = domIndex.find("input").val();
					returnCommoditySheetRN(returnPurchasing_reloadTable);
				} else if (domIndex.find("span").text() == "供应商") {
					returnPurchasing_reloadTable.providerID = domIndex.find("input").val();
					returnCommoditySheetRN(returnPurchasing_reloadTable);
				}else if (domIndex.find("span").text() == "门店") {
					returnPurchasing_reloadTable.shopID = domIndex.find("input").val();
					returnCommoditySheetRN(returnPurchasing_reloadTable);
				}
				break;
		}
	}

	$(".topNav .layui-form-label span").parent().next().find("li").click(function () {
		var indexObject = $(this).parents(".layui-inline").find(".layui-form-label span");
		var $next = $(this).parents(".layui-inline").siblings().find(".layui-form-label span").find("lable").text();
		var keyword = $(this).text();
		var keydata = indexObject.attr("data");
		var domTHIS = $(this);
		indexObject.parent().next().hide();
		booleanVariable("dataChange");
		if (bDesertLastOperation) {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {
				var i = layer.load(1);
				booleanVariable("restoreDefault");		//恢复默认值
				ConditionSearch(keyword, keydata, domTHIS);
				indexObject.find("lable").text(keyword);
				layer.close(i);
				layer.close(index);
			})
		} else {
			indexObject.find("lable").text(keyword);
			ConditionSearch(keyword, keydata, domTHIS);
		}
	})

	// 关闭头部导航选项区域
	$(document).click(function () {
		$(".topNav .layui-form-label + ul").hide();
	})
	$(document).delegate('.topNav .layui-form-label', 'click', function (event) {
		event.stopPropagation();
		console.log("阻止上述事件冒泡");
	});

	//按钮状态引导
	function unavailable (ButtonName) {
		switch (ButtonName) {
			case "新建":
				available();
				$(".tabControl ul li").eq(0).find("button").removeClass("tabColor").parent("li").next().find("button").addClass("tabColor");
				//				$(".tabControl ul li").eq(2).find("button").addClass('unavailable').attr("disabled", "disabled");
				break;
			case "取消":
				$(".tabControl ul li").eq(2).find("button").removeClass("tabColor");
				break;
			case "未审核":
				available();
				$(".tabControl ul li").eq(1).find("button").addClass("tabColor").parent("li").siblings().find("button").removeClass("tabColor");
				$("div.content ul li").eq(1).find("button").attr("disabled", "disabled");
				break;
			case "RN":
				available();
				$(".tabControl ul li button").removeClass("tabColor");
				break;
			case "无数据":
				$(".returnCommoditySheetManage").removeClass("unavailable");
				$(".tabControl ul li").eq(0).find("button").addClass("tabColor");
				$(".tabControl ul li:gt(0)").find("button").addClass("unavailable").attr("disabled", "disabled");
				$("div.content ul li").removeClass("cost_audit_stastu");
				$("div.content ul li button").attr("disabled", "disabled");
				break;
			case "promptBoxOfCancelButton":
				$(".tabControl ul li:eq(1)").find("button").addClass("tabColor").parent("li").siblings().find("button").removeClass("tabColor");
				break;
			default:
				$("div.content ul li").eq(1).removeAttr("disabled", "disabled")
				$(".returnCommoditySheetManage").removeClass("tabColor");
				$(".tabControl ul li:gt(0) button").attr("disabled", "disabled");
				$(".tabControl ul li:gt(0) button").addClass("unavailable");
				break;
		}
	}

	//清除所有按钮状态
	function available () {
		$(".returnCommoditySheetManage").removeAttr("disabled");
		$(".returnCommoditySheetManage").removeClass("unavailable");
	}

	// 退货单管理按钮事件
	$(".returnCommoditySheetManage").click(function () {
		$(this).addClass("tabColor").parent("li").siblings().find("button").removeClass("tabColor");
		switch ($(this).text()) {
			case "新建":
				booleanVariable("dataChange");
				if (bDesertLastOperation) {
					layer.confirm("确定要放弃之前的操作吗？", {
						icon: 3, title: '提示', cancel: function () {
							unavailable("promptBoxOfCancelButton");
						}
					}, function (index) {
						booleanVariable("restoreDefault");
						buttonBeClick_create();		//点击新建按钮时执行的函数
						layer.close(index);
					}, function () {
						unavailable("promptBoxOfCancelButton");
					})
				} else {
					buttonBeClick_create();
					bDesertLastOperation = true;
				}
				break;
			case "保存":
				if (toUpdatereturnCommoditySheet) {
					layer.confirm('确定要修改这个退货单吗？', { icon: 3, title: '提示' }, function (index, layero) {
						layer.close(index);
						booleanVariable("dataChange");
						if (dataChange) {
							returnCommoditySheetManage();
						} else {
							layer.msg("修改信息后再保存");
						}
					}, function (index, layero) {
						layer.close(index);
					})
				} else if (toCreateturnCommoditySheet) {
					layer.confirm('确定要创建这个退货单吗？', { icon: 3, title: '提示' }, function (index, layero) {
						layer.close(index);
						createReturnCommoditySheet();
					}, function (index, layero) {
						layer.close(index);
					})
				} else {
					layer.msg("已审核退货单不能修改");
				}
				break;
			case "取消":
				booleanVariable("dataChange");
				if(bDesertLastOperation){
					layer.confirm("确定要放弃之前的操作吗？", {
						icon: 3, title: '提示', cancel: function () {
							unavailable("promptBoxOfCancelButton");
						}
					}, function (index) {
						booleanVariable("restoreDefault");			//恢复相应变量默认值(确定取消后，会默认选中第一页的第一条数据，即查看了一个新的入库单)
						initQueryCondition();		//恢复查看条件的默认值
						unavailable("取消");
						firstVisited = true;
						resetPage = true;
						returnCommoditySheetRN(returnPurchasing_reloadTable);
						layer.close(index);
					}, function () {
						unavailable("promptBoxOfCancelButton");		//点击提示框的取消按钮后，默认选中保存按钮
					})
				} else {
					initQueryCondition();		//恢复查看条件的默认值
					firstVisited = true;
					resetPage = true;
					returnCommoditySheetRN(returnPurchasing_reloadTable);
				}
				break;
			case "审核":
				layer.confirm('确定要审核这个退货单吗？', { icon: 3, title: '提示' }, function (index, layero) {
					layer.close(index);
					toUpdatereturnCommoditySheet = false;
					returnCommoditySheetManage();
				}, function (index, layero) {
					layer.close(index);
				})
				break;
			default:
				break;
		}
	})

	//点击新建按钮时执行的函数
	function buttonBeClick_create () {
		unavailable("新建");
		popupCommodityData = [];
		initialData = [];
		providerID = 1;
		shopID = 2; // 默认门店id为2
		$(".provider_icon i").show();
		$(".shop_icon i").show();
		toCreateturnCommoditySheet = true;
		toUpdatereturnCommoditySheet = false;
		bDesertLastOperation = true;
		indexReturnCommoditySheetID = 0;
		//更新现在状态
		$("#div_span").text(new Date().format("yyyy-MM-dd") + "/新建退货单");
		$(".provider_name").text("默认供应商").attr("title", "默认供应商");
		$(".shopName").text("默认门店").attr("title", "默认门店");
		$(".providerID").text("1")
		$(".shopID").text("2")
		$("div.CommodityForm table tbody").empty();
		$(".returnPurchase-list ul li").removeClass("click");
		$(".content li").removeClass("cost_audit_stastu");
		$(".content li button").attr("disabled", "disabled");
		$(".content li button").addClass("widthbtn");
		$(".content li button").text("审核");
		$(".returnCommoditySN").hide();		//点击新建时隐藏单号
		$(".returnCommoditySN label").text("");
		$(".provider_icon").removeAttr("style");
		$(".shop_icon").removeAttr("style");
		purchaseCommTableRestPose();
		providerID = 1;
		shopID = 2;
		providerName = "默认供应商";
		shopName = "默认门店"
		$(".money").text("");
		$(".founder_warp").text("创建人：")
	}

	function initQueryCondition () {//恢复初始导航栏状态
		returnPurchasing_reloadTable.status = -1;
		returnPurchasing_reloadTable.date1 = "1970/01/01 00:00:00";
		var date = new Date();
		returnPurchasing_reloadTable.date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss");
		returnPurchasing_reloadTable.staffID = -1;
		returnPurchasing_reloadTable.providerID = -1;
		returnPurchasing_reloadTable.queryKeyword = '';
		returnPurchasing_reloadTable.pageIndex = curr_reloadTable;
		$(".layui-inline").each(function (i) {
			$(".layui-inline .layui-form-label").eq(i).find("span").html('<lable>' + $(".layui-inline").eq(i).find("ul li").eq(0).text() + '</lable>&nbsp;v</span>');
		});
		$(".td_inp").val("TH");
	}

	$("#CommodityTable tbody tr:odd").addClass("odd");// 表格行颜色

	//模糊搜索输入框监听
	$(".td_search i").click(function () {
		queryReturnPurchasingPromptBox(true);
	});
	$(".td_search input").keyup(function (event) {
		if (event.keyCode == "13" && event.key == "Enter") {
			queryReturnPurchasingPromptBox(true);
		} else {
			queryReturnPurchasingPromptBox(false);
		}
	})
	//“确定要放弃之前的操作吗？”提示框--模糊搜索功能
	function queryReturnPurchasingPromptBox (showMsg) {
		booleanVariable("dataChange");
		if (bDesertLastOperation) {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {
				var i = layer.load(1);
				booleanVariable("restoreDefault");		//将相应的变量恢复默认值
				if (showMsg) {		//输入回车键
					queryReturnCommoditySheet(showMsg);
				} else {
					clearDataOrRecoveryData();
				}
				layer.close(i);
				layer.close(index);
			})
		} else {
			queryReturnCommoditySheet(showMsg);
		}
	}
	function queryReturnCommoditySheet (showMsg) {
		var queryKeyword = $(".td_inp").val();
		if (queryKeyword.length <= 32) {		//检查输入的关键字长度
			if (queryKeyword.length == $(".td_inp").val().trim().length) {		//检查输入的关键字首尾是否有空格
				if (/^$|^[\u2014\u4E00-\u9FA5A-Za-z0-9_\()（）-\s]{1,32}$/.test(queryKeyword)) {		//检查输入的关键字是否满足数据格式要求
					if (queryKeyword.indexOf("TH") != -1 || queryKeyword.indexOf("T") != -1 || queryKeyword.indexOf("H") != -1 || !isNaN(queryKeyword)) {		//检查是否存在退货单号的关键字眼或者是否为纯数字
						if (queryKeyword.length >= 10 || queryKeyword.length == 0) {
							returnPurchasing_reloadTable.queryKeyword = queryKeyword;
						} else {
							if (showMsg) {		//输入回车键
								clearDataOrRecoveryData();
								layer.msg("数据格式不正确，退货单号至少需要输入10位", { id: "queryMsg" });
							}
							return;
						}
					} else {
						returnPurchasing_reloadTable.queryKeyword = queryKeyword;
					}
					firstVisited = true;
					returnPurchasing_reloadTable.pageIndex = curr_reloadTable;
					resetPage = true;
					returnCommoditySheetRN(returnPurchasing_reloadTable);
				} else {
					clearDataOrRecoveryData();
					layer.msg("数据格式不正确，请输入中英文、数字，只允许中间有空格，长度为(0,32]，支持输入的符号有：（）()_-——", { id: "queryMsg" });
				}
			} else {
				clearDataOrRecoveryData();
				layer.msg("输入的关键字首尾不能有空格，只允许中间有空格", { id: "queryMsg" });
			}
		} else {
			//			layer.msg("长度超出限制，输入的关键字最大长度为32", {id: "queryMsg"});		//前端界面已经限制了输入的最大长度，故超出长度时不做处理和提示
			return;
		}
	}

	//清空新建的数据/恢复退货单的数据--当模糊搜索值不符合查询需求时调用
	function clearDataOrRecoveryData () {
		if (indexReturnCommoditySheetID > 0) {		//上一步为查看入库单
			returnCommoditySheetR1(indexReturnCommoditySheetID);
		} else {		//上一步为新建入库单
			popupCommodityData = [];		//情况商品弹出层的数据
			buttonBeClick_create();		//恢复为新建状态
		}
	}

	//退货单列表单击事件
	$(".returnPurchase-list ul").delegate("li", "click", function () {
		var returnCommoditySheetID = $(this).attr("data");
		var domIndex = $(this);
		booleanVariable("dataChange");
		if (bDesertLastOperation) {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {
				var i = layer.load(1);
				booleanVariable("restoreDefault");		//恢复默认值
				domIndex.addClass("click").siblings().removeClass("click");
				returnCommoditySheetR1(returnCommoditySheetID);
				layer.close(i);
				layer.close(index);
			},
				function (index, layero) {
					layer.close(index);
				})
		} else {
			domIndex.addClass("click").siblings().removeClass("click");
			returnCommoditySheetR1(returnCommoditySheetID);
		}
	});

	var supplierindex;//供应商弹出层index
	//供应商弹出层
	$(".provider_icon").click(function () {
		supplierindex = layer.open({
			type: 1,
			title: "全部供应商",
			area: popupPageWidth,
			content: $("#chooseProviderWindow"),
			success: function (layero, index) {
				reloadTable(table, proRN_tableID, method_post, proRN_url, curr_reloadTable, provider_reloadTable);
			},
			cancel: function (index, layero) {
				providerID = $(".topNav .provider .providerID").text();			//获取页面供应商ID
				providerName = $(".topNav .provider .provider_name").text();		//获取页面的供应商名称
			}
		});
	})
	var shopPopupIndex;
	//门店弹出层
	$(".shop_icon").click(function () {
		shopPopupIndex = layer.open({
			type: 1,
			title: "全部门店",
			area: popupPageWidth,
			content: $("#chooseShopWindow"),
			success: function (layero, index) {
				reloadTable(table, shopRN_tableID, method_get, shopRN_url, 1, shop_reloadTable);
			},
			cancel: function (index, layero) {
				shopID = $(".topNav .shop .shopID").text();			//获取页面供应商ID
				shopName = $(".topNav .shop .shopName").text();		//获取页面的供应商名称
			}
		});
	})

	//监听表格操作providerList
	table.on('tool(providerList)', function (obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var tr = obj.tr
		if (layEvent === 'choose') {
			$(".barDemo").removeClass("icon-color");
			$(tr).find("i.layui-icon-ok-circle").addClass("icon-color");
			providerName = data.name;
			providerID = data.ID;
		}
	});

	// 模糊搜索供应商
	$("#seek_icon").click(function (data) {
		var indexLoading = layer.load(1);
		var queryKeyword = $("#providerinput").val();
		provider_reloadTable.queryKeyword = queryKeyword;
		reloadTable(table, proRN_tableID, method_post, proRNB_url, curr_reloadTable, provider_reloadTable);
		layer.close(indexLoading);
		return false;
	})

	// 根据供应商区域重载数据表格
	$("#chooseProviderWindow .layui-nav-child dd").click(function () {
		var loading = layer.load(1);
		var districtID = $(this).find("input").val();
		var districtName = $(this).children().text();
		$(".providerCategory").text(districtName);
		provider_reloadTable.districtID = districtID;
		reloadTable(table, proRN_tableID, method_post, proRN_url, curr_reloadTable, provider_reloadTable);
		layer.close(loading);
	});
	// 根据供门店区域重载数据表格
	$("#chooseShopWindow .layui-nav-child dd").click(function () {
		var loading = layer.load(1);
		var districtID = $(this).find("input").val();
		shop_reloadTable.districtID = districtID;
		reloadTable(table, shopRN_tableID, method_get, shopRN_url, 1, shop_reloadTable);
//		var districtName = $(this).children().text();
//		$(".providerCategory").text(districtName);
//		provider_reloadTable.districtID = districtID;
//		reloadTable(table, proRN_tableID, method_post, proRN_url, curr_reloadTable, provider_reloadTable);
		layer.close(loading);
	});
	// 是否全部展开供应商区域
	$(".showAllProviderDistrict").click(function () {
		if ($(this).text() == "全部展开") {
			$("#chooseProviderWindow .leftRegion .layui-nav-item").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		} else {
			$("#chooseProviderWindow .leftRegion .layui-nav-item").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
		}
	});
	
	// 是否全部展开供应商区域
	$(".showAllShopDistrict").click(function () {
		if ($(this).text() == "全部展开") {
			$("#chooseShopWindow .leftRegion .layui-nav-item").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		} else {
			$("#chooseShopWindow .leftRegion .layui-nav-item").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
		}
	});

	//供应商弹出层确定按钮
	$("#confirm").click(function () {
		dataChange = true;
		bDesertLastOperation = true;
		$(".provider_name").text(providerName).attr("title", providerName);
		$(".providerID").text(providerID);
		layer.close(supplierindex);
	})

	//供应商弹出层取消按钮
	$("#cancel").click(function () {
		providerID = $(".topNav .provider .providerID").text();			//获取页面供应商ID
		providerName = $(".topNav .provider .provider_name").text();		//获取页面的供应商名称
		layer.close(supplierindex);
	});

	var commodityindex;//商品弹出层index
	// 商品弹出层
	$("div.CommodityForm table tbody").delegate("button", "click", function () {
		commodityindex = layer.open({
			type: 1,
			title: "全部商品",
			area: popupPageWidth,
			content: $("#allCommodity"),
			success: function (layero, index) {
				// 弹出层的商品数
				$(".commodityKinds").text(popupCommodityData.length);
				notRecordActions(popupCommodityData, tempArray);
				reloadTable(table, popupCommRN_tableID, method_post, popupCommRN_url, curr_reloadTable, commodity_reloadTable);
			},
			cancel: function (index, layero) {
				notRecordActions(tempArray, popupCommodityData);
			}
		});
	});

	// 模糊搜索商品allCommodityList
	form.on('submit(commoditySearch)', function (data) {
		var indexLoading = layer.load(1);
		var queryKeyword = data.field.queryKeyword;
		commodity_reloadTable.queryKeyword = queryKeyword;
		reloadTable(table, popupCommRN_tableID, method_post, popupCommRN_url, curr_reloadTable, commodity_reloadTable);
		layer.close(indexLoading);
		return false;
	})

	// 根据商品类别重载数据表格allCommodityList
	$("#allCommodity dl.layui-nav-child dd").click(function () {
		var loading = layer.load(1);
		var categoryID = $(this).children().attr("indexid");
		commodity_reloadTable.categoryID = categoryID;
		reloadTable(table, popupCommRN_tableID, method_post, popupCommRN_url, curr_reloadTable, commodity_reloadTable);
		layer.close(loading);
	})

	// 是否全部展开商品类别
	$(".showAllCommCategory").click(function () {
		if ($(this).text() == "全部展开") {
			$("#allCommodity .layui-nav-item").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		} else {
			$("#allCommodity .layui-nav-item").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
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
			var barcodeID = data.listBarcodes.length == 0 ? "" : data.listBarcodes[0].ID;
			var barcodeName = data.listBarcodes.length == 0 ? "" : data.listBarcodes[0].barcode;
			var packageUnitID = data.commodity.packageUnitID;
			var packageUnitName = data.commodity.packageUnitName;
			var NO = 1;
			var price = data.commodity.listSlave2[0].latestPricePurchase >= 0 ? data.commodity.listSlave2[0].latestPricePurchase : 0.00;
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
					var barcodeID = data[i].listBarcodes.length == 0 ? "" : data[i].listBarcodes[0].ID;
					var barcodeName = data[i].listBarcodes.length == 0 ? "" : data[i].listBarcodes[0].barcode;
					var packageUnitID = data[i].commodity.packageUnitID;
					var packageUnitName = data[i].commodity.packageUnitName;
					var NO = 1;
					var price = data[i].commodity.listSlave2[0].latestPricePurchase >= 0 ? data[i].commodity.listSlave2[0].latestPricePurchase : 0.00;
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
						$(this).val("1");
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

	// 商品弹出层确定按钮
	$("#confirmChooseCommodity").click(function () {
		dataChange = true;
		bDesertLastOperation = true;
		initialData = popupCommodityData;
		$("#CommodityTable tbody").empty();
		if (popupCommodityData.length > 0) {
			for (var i = 0; i < popupCommodityData.length; i++) {
				var commID = popupCommodityData[i].commID;
				var commName = popupCommodityData[i].commName;
				var barcodeID = popupCommodityData[i].barcodeID;
				var barcodeName = popupCommodityData[i].barcodeName;
				var packageUnitID = popupCommodityData[i].packageUnitID;
				var packageUnitName = popupCommodityData[i].packageUnitName;
				var NO = popupCommodityData[i].NO;
				var price = popupCommodityData[i].price > 0 ? popupCommodityData[i].price : 0.00;
				var specification = popupCommodityData[i].specification;
				$("#CommodityTable tbody").append(
					"<tr data='" + i + "' >" +
					"<td style='width:80px;'><i class='layui-icon layui-icon-close-fill deletecommodity' onclick='deletecommodity(this)'></i><span>" + (i + 1) + "</span><input name='商品ID' type='hidden' value='" + commID + "'/></td>" +
					"<td><div class='wrap' title='" + barcodeName + "'>" + barcodeName + "</div></td>" +
					"<td><div class='wrap' title='" + commName + "'>" + commName + "</div><button ><i class='layui-icon layui-icon-add-circle addGeneralComm' title='添加商品'></i></button></td>" +
					"<td>" + packageUnitName + "<input id='id' name='条形码ID' type='hidden' value='" + barcodeID + "'/></td>" +
					"<td><input onchange='returnPurchasingCommNOManage(this);' value='" + NO + "' name='NO' style='width=50px;'></td>" +
					"<td><input onchange='returnPurchasingCommPriceManage(this);'  value='" + parseFloat(price).toFixed(2) + "' name='price' style='width=50px;'></td>" +
					"<td>" + parseFloat(NO * price).toFixed(2) + "</td>" +
					"</tr>"
				);
			}
			calculatedPurchaseTotalPrice();
			$("#CommodityTable tbody tr:odd").addClass("odd");// 表格行颜色
		} else {
			purchaseCommTableRestPose();
		}
		layer.close(commodityindex);
	});

	// 商品弹出层取消按钮
	$("#closeLayerPage").click(function () {
		notRecordActions(tempArray, popupCommodityData);
		layer.close(commodityindex);
	});
	
	//渲染门店表popupShopList（弹出层）
	table.render({
		elem: '#popupShopList',
		url: shopRN_url,
		id: shopRN_tableID,
		method: method_get,
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
			{ type: 'numbers', title: '序号' },
			{ templet: '#shopDemo', title: '操作', width: 60, align: 'center', event: 'choose' },
			{ field: 'name', title: '门店名称', width: 160, align: 'center', event: 'choose' },
			{ field: 'address', title: '地址', width: 190, align: 'center', event: 'choose' }
		]],
		done: function (res, curr, count) {
			if (!res) {
				layer.msg("服务器错误");
				return;
			}
			$("#toChooseShop .layui-table tr").each(function () {		//翻页后给选择的供应商添加样式
				if ($(this).find("input").val() == shopIDBeChoosed) {
					$(this).find("td").eq(0).click();
				}
			});
		}
	});
	
	//监听表格操作providerList
	table.on('tool(popupShopList)', function (obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var tr = obj.tr
		if (layEvent === 'choose') {
			$(".shopDemo").removeClass("icon-color");
			$(tr).find("i.layui-icon-ok-circle").addClass("icon-color");
			shopName = data.name;
			shopID = data.ID;
		}
	});
	
//	//门店表格监听事件
//	var shopIDBeChoosed = -1;
//	var shopNameBeChoosed = "";
//	table.on('tool(popupShopList)', function (obj) {
//		var data = obj.data;
//		var layEvent = obj.event;
//		var tr = obj.tr;
//		switch (layEvent) {
//			case "choose":
//				shopIDBeChoosed = data.ID;
//				shopNameBeChoosed = data.name;
//				tr.find(".shopDemo").addClass("shopBeChoosed");
//				tr.siblings().find(".shopDemo").removeClass("shopBeChoosed");
//				break;
//			default:		//其他情况
//				break;
//		}
//	});
	//确认选择门店
	$(".confirmChoosedShop").click(function () {
		bDesertLastOperation = true;
		$(".shopName").attr("title", shopName).text(shopName);
		$(".shopID").text(shopID);
		layer.close(shopPopupIndex);		//关闭最新弹出的层
	});
	
	//供应商弹出层取消按钮
	$(".exitChoosedShop").click(function () {
		shopID = $(".topNav .shop .shopID").text();			//获取页面供应商ID
		shopName = $(".topNav .shop .shopName").text();		//获取页面的供应商名称
		layer.close(shopPopupIndex);
	});
//	// 模糊搜索门店（名称）
//	form.on('submit(shopSearch)', function (data) {
//		var queryKeyword = data.field.queryKeyword;
//		if (queryKeyword.length <= 32) {
//			var indexLoading = layer.load(1);
//			var shopSearchData = new Object();
//			shopSearchData.queryKeyword = data.field.queryKeyword;
//			reloadTable(table, shopRN_tableID, method_post, shopRNByFields_url, 1, shopSearchData);
//		} else {
//			//超过长度限制不发送请求
//		}
//		return false;
//	});
	// 模糊搜索门店
	$("#seekShop_icon").click(function (data) {
		var indexLoading = layer.load(1);
		var queryKeyword = $("#shopinput").val();
		if (queryKeyword.length <= 32) {
			var indexLoading = layer.load(1);
			var shopSearchData = new Object();
			shopSearchData.queryKeyword = queryKeyword;
			reloadTable(table, shopRN_tableID, method_post, shopRNByFields_url, 1, shopSearchData);
		} else {
			//超过长度限制不发送请求
		}
		layer.close(indexLoading);
		return false;
	})
	
	function queryEvents (domTHIS) {		//头部导航栏查询功能
		var indexObject = domTHIS.parents(".layui-inline").find(".layui-form-label span");
		var $next = domTHIS.parents(".layui-inline").siblings().find(".layui-form-label span").find("lable").text();
		var keyword = domTHIS.text();
		var keydata = domTHIS.attr("data");
		indexObject.parent().next().hide();
		booleanVariable("dataChange");
		if (bDesertLastOperation) {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {
				var i = layer.load(1);
				booleanVariable("restoreDefault");		//恢复默认值
				ConditionSearch(keyword, keydata, domTHIS);
				indexObject.find("lable").text(keyword);
				layer.close(i);
				layer.close(index);
			})
		} else {
			indexObject.find("lable").text(keyword);
			ConditionSearch(keyword, keydata, domTHIS);
		}
	}
})
