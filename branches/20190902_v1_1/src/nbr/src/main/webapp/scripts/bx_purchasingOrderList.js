layui.use([ 'element', 'form', 'table', "upload", 'laypage' ], function() {
	var element = layui.element;
	var form = layui.form;
	var table = layui.table;
	var upload = layui.upload; // 文件上传
	var laypage = layui.laypage; // 分页

	// 定义的全局变量
	const purchaseRN_url = "purchasingOrder/retrieveNEx.bx"; // 采购订单查询路径
	const purchaseR1_url = "purchasingOrder/retrieve1Ex.bx"; // 采购订单详情路径
	const createPurchase_url = "purchasingOrder/createEx.bx"; // 创建采购订单
	const updatePurchase_url = "purchasingOrder/updateEx.bx"; // 修改采购订单
	const purchaseRapprove_url = "purchasingOrder/approveEx.bx"; // 审核订单
	const deletePurchase_url = "purchasingOrder/deleteEx.bx"; // 删除采购订单
	const createWarehousingSheet_url = "warehousing/createEx.bx"; // 创建入库单
	const proRN_url = "provider/retrieveNEx.bx"; // 供应商查询路径
	const proRNB_url = "provider/retrieveNByFieldsEx.bx"; // 模糊搜索供应商路径
	const shopRNByFields_url = 'shop/retrieveNByFieldsEx.bx';		//模糊查询门店
	const shopRN_url = "shop/retrieveNEx.bx";		//查询门店接口
	const allCommRN_url = "commodity/retrieveNEx.bx"; // 商品查询路径
	// const purchaseRN_tableID = "purchasingOrderList";
	// //purchasingOrderList表格ID
	const proRN_tableID = "providerList"; // providerList供应商表格ID
	const shopRN_tableID = "popupShopList";		//门店表格ID（弹窗）
	const allCommRN_tableID = "popupCommodityList"; // popupCommodityList商品弹窗表格ID
	const method_get = "GET";
	const method_post = "POST";
	const commodity_reloadTable = {
		"status" : 0,
		"type" : 0
	}; // 商品搜索传参
	const provider_reloadTable = {}; // 供应商搜索传参
	const shop_reloadTable = {};		//查询门店的传参
	const purchase_reloadTable = {}; // 采购订单搜索传参
	const curr_reloadTable = 1; // 页码

	const RNMsg = "查询采购订单失败！需要权限！";
	const R1Msg = "查看采购订单商品详情时发生错误";
	const serverErrorMsg = "服务器错误";
	const checkPriceMsg = "请检查价格合法性";
	const checkQuantityMsgMsg = "请检查数量合法性";
	const addCommodityMsg = "请添加商品"
	const createWarehousingSheetSucceedMSgMSg = "创建入库单成功";
	const createWarehousingSheetUnsuccessfulMsg = "创建入库单失败,请重试";
	const createWarehousingSheetNOMsg = "不能创建一个入库数量都为零的入库单";
	const jurisdictionMsg = "无商品查询权限";
	const FunctionNotOpenMsg = "功能暂未开放";
	const deleteSucceedMsg = "删除采购单成功";
	const ghostDeletePurchaseOrderMsg = "不能删除非未审核状态的采购单";
	const selectDeletePurchasingOrderMsg = "请选择要删除的采购订单";
	const canNotUpdateMsg = "已审核的采购订单不可以修改";
	const modifySaveMsg = "修改信息后再保存";
	const specialCharactersCannotEnteredMsg = "不能输入特殊字符";

	// 自定义全局变量和初始加载层
	var status = [ '待审核', '待入库', '部分入库', '已完成' ];
	var statusall = [ '待审核', '已审核', '部分入库', '全部入库', '已删除' ];
	var commodityPopupIndex = ''; // 获取商品弹出层的index
	var providerPopupIndex = ''; // 获取供应商弹出层的index
	var shopPopupIndex = ''; // 获取门店弹出层的index
	var toCreateWarehouse = false; // 是否可以进行入库
	var toCreatePurchase = false; // 是否创建采购订单
	var toUpdatePurchase = false; // 是否修改采购订单
	var indexPurchaseID = 0; // 当前的采购订单ID
	var lastVisitPurchaseID; // 当前的采购订单ID
	var indexProviderName = ''; // 供应商名称
	var indexProviderID = 0; // 供应商ID
	var isFirstTimeToVisit = true; // 是否首次进入页面
	var resetPage = false; // 是否重置页码
	var isToUpdatePurchaseOrderStatus = false; // 是否要更新当前采购单状态
	var isToUpdatePurchaseOrderProvider = false; // 是否要更新当前采购单供应商

	var popupPageWidth;
	// 页面加载后执行
	$(document).ready(function() {
		adaptiveScreen();
	})

	// 缩放窗口时执行
	window.onresize = function() {
		adaptiveScreen();
	}

	// 自适应屏幕函数
	function adaptiveScreen() {
		var windowWidth = $(window).width();
		if (windowWidth >= 1060) {
			popupPageWidth = "1000px";
		} else {
			popupPageWidth = windowWidth - 60 + "px";
		}
	}

	// 页面初始化
	layer.ready(function() {
		initQueryCondition();
		purchaseOrderListRN(purchase_reloadTable); // 采购订单数据渲染
		fieldFormat(form);
	});

	// 初始化模糊搜索参数
	function initQueryCondition() {
		purchase_reloadTable.status = -1; // -1表示查询所有状态的采购订单
		purchase_reloadTable.pageIndex = 1; // 请求的页码
		purchase_reloadTable.pageSize = 10; // 每页显示的数量
		purchase_reloadTable.queryKeyword = ""; // 模糊搜索关键字
		purchase_reloadTable.staffID = 0; // 0表示查询所有操作人员的采购订单
		var date = new Date();
		purchase_reloadTable.date1 = "1970/01/01 00:00:00"; // 起始时间
		purchase_reloadTable.date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss"); // 终止时间，往后延一周时间防止产生时间差导致搜索不到最新的采购订单信息
	}

	// 查询显示采购订单列表
	function purchaseOrderListRN(data) {
		var indexLoading = layer.load(1);
		buttonStatusControl("RN");
		// 采购订单数据请求
		$.ajax({
			url : purchaseRN_url,
			type : method_post,
			async : true,
			dataType : "json",
			data : data,
			success : function succFunction(data) {
				console.log(data);
				if (data.ERROR != EnumErrorCode.EC_NoError) {
					$(".purchase-list ul").html('<span class="purchaseRNHint">查无数据</span>');
					$("div.table-data table tbody").empty();
					$("#div_span").text("/采购订单");
					$(".odd-numbers label").text("");
					buttonStatusControl("无数据");
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg(RNMsg);
					}
					layer.close(indexLoading);
					return;
				}
				if (data.count > 0) {
					$(".purchase-list ul").empty();
					for (var i = 0; i < data['objectList'].length; i++) {
						var purchaseListData = data['objectList'][i];
						purchaseOrderListRender(purchaseListData);
					}
					// restoreSelectedStatus();
					if (isFirstTimeToVisit) {
						isFirstTimeToVisit = false;
						$(".purchase-list ul").find("li").eq(0).click();
					} else {
						$(".purchase-list ul").find("li[data='" + indexPurchaseID + "']").click();
					}
				} else {
					$(".purchase-list ul").html('<span class="purchaseRNHint">查无数据</span>');
					$("div.table-data table tbody").empty();
					$("#div_span").text("/采购订单");
					$(".odd-numbers label").text("");
					$(".money").text("");
					$(".creator").text("创建人：");
					buttonStatusControl("无数据");
				}
				purchasePage(data.count);
				layer.close(indexLoading);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg(serverErrorMsg);
			}
		});
	}

	var purchasingOrderPageSize = 0;
	// 分页
	function purchasePage(count) {
		if (resetPage) {
			resetPage = false;
			pageCurr = curr_reloadTable;
		} else if (purchasingOrderPageSize !== 0) {
			pageCurr = purchasingOrderPageSize;
			purchasingOrderPageSize = 0;
		} else {
			pageCurr = location.hash.replace('#!indexCurr=', '');
		}
		laypage.render({ // 渲染分页
			elem : 'pagination',
			count : count, // 数据总数，从服务端得到
			limit : 10, // 定义每页显示的数量
			next : ">",
			prev : "<",
			hash : "indexCurr",
			curr : pageCurr,
			layout : [ 'prev', 'page', 'next' ],
			jump : function(obj, first) {
				console.log(obj);
				if (!first) { // 首次不执行
					if (bDesertLastOperation) {
						layer.confirm('确定要放弃之前的操作吗？', {
							icon : 3,
							title : '提示',
							cancel : function(index) {
								layer.close(index);
								purchasingOrderPageSize = pageCurr;
								purchasePage(count);
							}
						}, function(index, layero) {
							var i = layer.load(1);
							purchase_reloadTable.pageIndex = obj.curr;
							purchaseOrderListRN(purchase_reloadTable);
							isFirstTimeToVisit = true;
							bDesertLastOperation = false;
							layer.close(index);
							layer.close(i);
						}, function(index, layero) {
							layer.close(index);
							purchasingOrderPageSize = pageCurr;
							purchasePage(count);
						})
					} else {
						purchase_reloadTable.pageIndex = obj.curr;
						purchaseOrderListRN(purchase_reloadTable);
						isFirstTimeToVisit = true;
					}
				}
			}
		});
	}

	// 采购订单列表渲染
	function purchaseOrderListRender(purchaseListData) {
		var providerName = purchaseListData['status'] == 0 ? purchaseListData['provider']['name'] : purchaseListData['providerName'];
		$(".purchase-list ul").append(
			"<li data='" + purchaseListData['ID'] + "'>" +
			"<div class='list_option'>" +
			"<div class='option-status'>" +
			"<span class='iconse circle_color_" + purchaseListData['status'] + "'></span>" +
			"<span class='statusName'>" + status[purchaseListData['status']] + "</span>" +
			"<label class='purchase-time'>" + purchaseListData['createDatetime'].substring(0, 10) + "</label>" +
			"</div>" +
			"<label class='provider-name' title='" + providerName + "'>" + providerName + "</label>" +
			"<input class='hid' type='hidden' value='" + purchaseListData['ID'] + "'>" +
			"<span class='creators' title='" + purchaseListData['staffName'] + "'>" + purchaseListData['staffName'] + "</span>" +
			"</div>" +
			"</li>"
		);
	}

	// 恢复采购订单选中状态
	function restoreSelectedStatus(providerName) {
		var purchaseOrderList = $(".purchase-list ul li");
		for (var j = 0; j <= purchaseOrderList.length; j++) {
			var purchaseOrderID = $(purchaseOrderList[j]).attr("data");
			if (purchaseOrderID == indexPurchaseID) {
				$(purchaseOrderList[j]).addClass("click").siblings().removeClass("click");
				if (isToUpdatePurchaseOrderStatus) {// 更新左侧采购订单列表状态
					$(purchaseOrderList[j]).find("div.list_option div.option-status span.iconse").removeClass("circle_color_0").addClass("circle_color_1");
					$(purchaseOrderList[j]).find("div.list_option div.option-status span.statusName").text("待入库");
					isToUpdatePurchaseOrderStatus = false;
				}
				if (isToUpdatePurchaseOrderProvider) {// 更新左侧采购订单列表供应商
					$(purchaseOrderList[j]).find("div.list_option label.provider-name").attr("title", providerName).text(providerName);
					$(".providerName").attr("title", providerName).text(providerName);
					$(".providerID").text(indexProviderID);
					isToUpdatePurchaseOrderProvider = false;
				}
			}
		}
	}

	var initialData = []; // R1初始数据
	// 采购订单列表点击订单列表点击事件
	function purchasingOrderR1(ID) {
		var indexLoading = layer.load(1);
		popupCommodityData = []; // 临时数组清零
		initialData = [];
		lastVisitPurchaseID = ID;
		indexPurchaseID = ID; // 采购订单ID
		$(".table-data table thead tr th:gt(6)").show();
		$(".addProvider").hide();
		$(".addShop").hide();
		$.ajax({
			url : purchaseR1_url,
			type : method_get,
			async : true,
			dataType : "json",
			data : {
				"ID" : ID
			},
			success : function succFunction(data) {
				console.log(data);
				$(".odd-numbers").show(); // 查询时显示采购订单号
				if (data.ERROR != EnumErrorCode.EC_NoError) {
					$("div.table-data table tbody").empty();
					$("#div_span").text("/采购订单");
					$(".odd-numbers label").text("");
					$(".money").text("");
					$(".creator").text("创建人：");
					$("div.content ul li").eq(0).removeClass("cost_audit_stastu");
					$("#button_approve").attr("disabled", "disabled");
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg(R1Msg);
					}
					layer.close(indexLoading);
					return;
				}
				indexProviderID = data.object.providerID; // 供应商ID
				shopIDBeChoosed = data.object.shopID; // 供应商ID
				shopNameBeChoosed = data.object.shop.name;
				buttonState(data);
				restoreSelectedStatus(data.object.providerName);
				$.each(data['object']['listSlave1'], function(i, val) {
					var array = {};
					array.commID = data.object.listCommodity[i].ID;
					array.NO = data.object.listSlave1[i].commodityNO;
					array.price = data.object.listSlave1[i].priceSuggestion;
					initialData.push(array);
					addData(data, i);
					var warehousingNO = (val['commodityNO'] - val['warehousingNO']) < 0 ? "<font color='#FF6347'>已超计划  " + (val['warehousingNO'] - val['commodityNO']) + "</font>" : (val['commodityNO'] - val['warehousingNO']);
					$("div.table-data table tbody").append(
						"<tr data='" + i + "'>" +
						"<td style='width:60px;'><i class='layui-icon layui-icon-close-fill deletecommodity' onclick='deletecommodity(this)'></i><span>" + (i + 1) + "</span><input name='商品ID' type='hidden' value='" + data.object.listCommodity[i].ID + "'/></td>" +
						"<td><div class='wrap' title='" + val['barcode'] + "'>" + val['barcode'] + "</div><input id='id' name='订单ID' type='hidden' value='" + data.object.ID + "'/></td>" +
						"<td style='vertical-align: middle;'><div class='wrap' title='" + data.object.listCommodity[i].name + "'>" + data.object.listCommodity[i].name + "</div><button ><i class='layui-icon layui-icon-add-circle addGeneralComm' title='添加商品'></i></button></td>" +
						"<td>" + val['packageUnitName'] + "<input id='id' name='条形码ID' type='hidden' value='" + val['barcodeID'] + "'/></td>" +
						"<td><input type='text' onchange='check_ifDataChange(this,this.value);purchaseCommNOManage(this);' value='" + val['commodityNO'] + "'name = 'NO'/></td>" +
						"<td><input type='text' onchange='check_ifDataChange(this,this.value);purchaseCommPriceManage(this);'  value='" + parseFloat(val['priceSuggestion']).toFixed(2) + "' name='price'/>" + "</td>" +
						"<td><div class='wrap' title='" + parseFloat(val['commodityNO'] * val['priceSuggestion']).toFixed(2) + "'>" + parseFloat(val['commodityNO'] * val['priceSuggestion']).toFixed(2) + "</div></td>" +
						"<td>" + val['warehousingNO'] + "</td>" +
						"<td>" + warehousingNO + "</td>" +
						"<td>0</td>" +
						"<td>" + parseFloat(val['priceSuggestion']).toFixed(2) + "</td>" +
						"</tr>"
					);
					if (data['object']['status'] != 0) {
						buttonStatusControl("其他");
						toUpdatePurchase = false;
						$("div.table-data table tbody tr:last td").eq(0).html((i + 1) + "<input name='商品ID' type='hidden' value='" + data.object.listCommodity[i].ID + "'/>");
						$("div.table-data table tbody tr:last td").eq(2).find("button").remove();
						$("div.table-data table tbody tr:last td").eq(2).html("<div class='wrap' title='" + val['commodityName'] + "'>" + val['commodityName'] + "</div>");
						$("div.table-data table tbody tr:last td").eq(4).text(val['commodityNO']);
						$("div.table-data table tbody tr:last td").eq(5).text(parseFloat(val['priceSuggestion']).toFixed(2));
						$(".remark").text(data.object.remark);
					} else {
						buttonStatusControl("未审核");
						toUpdatePurchase = true;
						toCreateWarehouse = false;
						toCreatePurchase = false;
						$(".addProvider").show();
						$(".addShop").show();
						$(".remark").html("<textarea cols='80' rows='6' id='remark' onchange='remark(this)' style='resize:none'>" + data.object.remark + "</textarea>");
						$(".span_v").removeAttr("style");
					}
				});
				calculatedPurchaseTotalPrice();
				settingPurchaseCommTableStyles("toRetrieve1");
				$("div.table-data table tbody tr:odd").addClass("odd"); // 表格行颜色
				$(".creator").text("创建人：" + data.object.staffName);
				layer.close(indexLoading);
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg(serverErrorMsg);
			}
		})
	}

	// 采购订单详情区域按钮状态改变
	function buttonState(data) {
		setBtnStatus();// 恢复按钮状态
		$("div.table-data table tbody").empty();
		// 更新现在状态
		$("#div_span").text(data['object']['createDatetime'].substring(0, 10) + "/" + statusall[data['object']['status']] + "采购订单");
		$(".odd-numbers label").text(data.object.sn);
		$(".tabControl ul li").eq(1).addClass("tabColor").siblings().removeClass("tabColor");
		$(".providerName").attr("title", provider_Name).text(provider_Name); // 供应商
		$(".providerID").text(data['object']['providerID']); // 供应商ID
		$(".shopName").attr("title", shopNameBeChoosed).text(shopNameBeChoosed); // 门店
		$(".shopID").text(shopIDBeChoosed); // 门店ID
		indexProviderName = provider_Name; // 供应商名称
		indexProviderID = data['object']['providerID'];
		if (data['object']['status'] == 0) { // 未审核
			$("div.content ul li").eq(0).removeClass("cost_audit_stastu");
			$("#button_warehousing").text("入库");
			$("#button_warehousing").attr("disabled", "disabled");
			$("div.content ul li").eq(1).addClass("cost_audit_stastu");
			$("#button_approve").text("审核");
			$("#button_approve").attr("disabled", "disabled");
			$("#button_approve").removeAttr("disabled");
//			$("div.content ul li").eq(0).addClass("cost_audit_stastu");
//			$("div.content ul li").eq(0).find("button").text("审核");
//			$("div.content ul li").eq(0).find("button").removeAttr("disabled");
//			$("div.content ul li").eq(1).removeClass("cost_audit_stastu");
//			$("div.content ul li").eq(1).find("button").text("入库");
//			$("div.content ul li").eq(1).find("button").attr("disabled", "disabled");
		} else if (data['object']['status'] == 3) { // 订单已完成
			$("div.content ul li").eq(0).removeClass("cost_audit_stastu");
			$("#button_warehousing").text("已入库")
			$("#button_warehousing").attr("disabled", "disabled");
			$("div.content ul li").eq(1).removeClass("cost_audit_stastu");
			$("#button_approve").text("已审核");
			$("#button_approve").attr("disabled", "disabled");
//			$("div.content ul li").eq(0).find("button").text("已入库")
//			$("div.content ul li").eq(0).find("button").attr("disabled", "disabled");
//			$("div.content ul li").eq(0).removeClass("cost_audit_stastu");
//			$("div.content ul li").eq(1).removeClass("cost_audit_stastu");
//			$("div.content ul li").eq(1).find("button").text("已审核");
//			$("div.content ul li").eq(1).find("button").attr("disabled", "disabled");
		} else { // 已审核但未入库，部分入库
			$("div.content ul li").eq(1).removeClass("cost_audit_stastu");
			$("#button_approve").text("已审核");
			$("#button_approve").attr("disabled", "disabled"); // 审核按钮的状态
			$("div.content ul li").eq(0).addClass("cost_audit_stastu");
			$("#button_warehousing").text("入库");
			$("#button_warehousing").removeAttr("disabled");
		}
		if (data['object']['status'] == 0 || data['object']['status'] == 1) {
			$(".workflow ul li").eq(1).addClass("workflow_li").siblings().removeClass("workflow_li");// 工作流的状态
		} else if (data['object']['status'] == 2 || data['object']['status'] == 3) {
			$(".workflow ul li").eq(data['object']['status']).addClass("workflow_li").siblings().removeClass("workflow_li");// 工作流的状态
		}
	}

	// 采购订单商品数据保存至商品弹窗数组中
	function addData(data, i) {
		var commID = data['object']['listCommodity'][i]['ID'];
		var commName = data['object']['listSlave1'][i]['commodityName'];
		var barcodeID = data['object']['listSlave1'][i]['barcodeID'];
		var barcodeName = data['object']['listSlave1'][i]['barcode'];
		var packageUnitID = data['object']['listSlave1'][i]['packageUnitID'];
		var packageUnitName = data['object']['listSlave1'][i]['packageUnitName'];
		var NO = (data['object']['listSlave1'][i]['commodityNO'] - data['object']['listSlave1'][i]['warehousingNO']) <= 0 ? 0 : data['object']['listSlave1'][i]['commodityNO'] - data['object']['listSlave1'][i]['warehousingNO'];
		var price = data['object']['listSlave1'][i]['priceSuggestion'];
		var specification = data['object']['listSlave1'][i]['specification'];
		popupCommDataManage("add", commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, specification);
	}

	// 计算采购订单采购商品的总价
	function calculatedPurchaseTotalPrice() {
		var totalPrice = 0;
		$("div.table-data table tbody tr").each(function() {
			var price = $(this).find("td").eq(6).text();
			totalPrice = (parseFloat(totalPrice) + parseFloat(price)).toFixed(2);
		})
		$(".money").text(totalPrice + "元");
	}

	// 恢复表格初始状态
	function purchaseCommTableRestPose() {
		$("div.table-data table tbody").html(
				"<tr>" + "<td style='width:60px;'>1</td>" + "<td><button><i class='layui-icon layui-icon-add-circle addGeneralComm' title='添加商品'></i></button></td>" + "<td></td>" + "<td></td>" + "<td></td>" + "<td></td>" + "<td></td>"
						+ "</tr>");
	}

	// 检查是否有条形码
	function CheckForBarcode(barcodeID, msg) {
		var layMsg = '';
		switch (msg) {
		case "创建":
			layMsg = "不能创建无条形码商品的采购订单";
			break;
		case "修改":
			layMsg = "不能修改无条形码商品的采购订单";
			break;
		case "审核":
			layMsg = "不能审核无条形码商品的采购订单";
			break;
		}
		if (barcodeID == null || barcodeID == '') {
			layer.msg(layMsg);
			return;
		}
	}

	// 采购订单管理
	function purchaseOrderManage(toUsedUrl, succeedMsg, defeatedMsg) {
		if (popupCommodityData.length > 0) {
			let barcodeMsg = '';
			let providerID = $(".providerID").text();
			let shopID = $(".shopID").text();
			let commIDs = "";
			let barcodeIDs = "";
			let NOs = "";
			let priceSuggestions = "";
			let remark = $("#remark").val();
			let isModified = 0;
			switch (toUsedUrl) {
			case updatePurchase_url:
				barcodeMsg = "修改";
				isToUpdatePurchaseOrderProvider = true;
				break;
			case purchaseRapprove_url:
				barcodeMsg = "审核";
				isToUpdatePurchaseOrderProvider = true;
				isToUpdatePurchaseOrderStatus = true;
				if (bDesertLastOperation) { // 如果数据发生修改
					isModified = 1;
				}
				break;
			case createPurchase_url:
				barcodeMsg = "创建";
				toCreatePurchase = true;
				break;
			default:
				console.log("url错误");
				break;
			}
			for (let i = 0; i < popupCommodityData.length; i++) {
				commIDs += popupCommodityData[i].commID + ",";
				barcodeIDs += popupCommodityData[i].barcodeID + ",";
				NOs += popupCommodityData[i].NO + ",";
				priceSuggestions += popupCommodityData[i].price + ",";
				CheckForBarcode(popupCommodityData[i].barcodeID, barcodeMsg);
				if (popupCommodityData[i].price < 0) {
					layer.msg(checkPriceMsg);
					return;
				}
				if (popupCommodityData[i].NO <= 0) {
					layer.msg(checkQuantityMsgMsg);
					return;
				}
			}
			$.ajax({
				url : toUsedUrl,
				type : method_post,
				async : true,
				dataType : "json",
				data : {
					"providerID" : providerID, // 供应商ID
					"shopID" : shopID, // 门店ID
					"remark" : remark, // 采购总结
					"commIDs" : commIDs, // 商品ID
					"barcodeIDs" : barcodeIDs, // 条形码ID
					"NOs" : NOs, // 采购数量
					"priceSuggestions" : priceSuggestions, // 采购价格
					"ID" : indexPurchaseID === '' ? -1 : indexPurchaseID,
					"isModified" : isModified,
				},
				success : function succFunction(data) {
					console.log(data);
					if (data.ERROR == EnumErrorCode.EC_NoError) {
						if (data.msg) {
							layer.msg(data.msg);
						} else {
							layer.msg(succeedMsg);
						}
						toCreatePurchase = false;
						bDesertLastOperation = false;
						if (toUsedUrl === createPurchase_url) {
							isFirstTimeToVisit = true;
							resetPage = true;
							initQueryCondition();
							restoreDefault();
							purchaseOrderListRN(purchase_reloadTable);
						} else {
							resetPage = false;
							isFirstTimeToVisit = false;
							purchasingOrderR1(indexPurchaseID);
						}
					} else {
						isToUpdatePurchaseOrderStatus = false;
						if (toUsedUrl == updatePurchase_url) {
							bDesertLastOperation = true;
						}
						if (data.msg) {
							layer.msg(data.msg);
						} else {
							layer.msg(defeatedMsg);
						}
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					layer.msg(serverErrorMsg);
				}
			});
		} else {
			bDesertLastOperation = true;
			layer.msg(addCommodityMsg);
		}
	}

	// 创建入库单
	function createWarehousingSheet() {
		var CommIDs = "";
		var CommNOs = "";
		var CommPrices = "";
		var Amounts = "";
		var BarcodeIDs = "";
		var CommodityNames = "";
		var NoMoreThanZero = false;
		for (var i = 0; i < popupCommodityData.length; i++) {
			if (popupCommodityData[i].NO > 0) {
				NoMoreThanZero = true;
				CommIDs += popupCommodityData[i].commID + ",";
				CommodityNames += popupCommodityData[i].commName + ",";
				BarcodeIDs += popupCommodityData[i].barcodeID + ",";
				Amounts += popupCommodityData[i].NO * popupCommodityData[i].price + ",";
				CommNOs += popupCommodityData[i].NO + ",";
				CommPrices += popupCommodityData[i].price + ",";
			}
		}
		if (!NoMoreThanZero) {
			layer.msg(createWarehousingSheetNOMsg);
			return;
		}
		$.ajax({
			url : createWarehousingSheet_url,
			type : 'POST',
			async : true,
			dataType : "json",
			data : {
				"commIDs" : CommIDs,
				"commNOs" : CommNOs,
				"commPrices" : CommPrices,
				"amounts" : Amounts,
				"barcodeIDs" : BarcodeIDs,
				"commodityNames" : CommodityNames,
				"warehouseID" : 1,
				"providerID" : indexProviderID,
				"shopID" : shopIDBeChoosed,
				"purchasingOrderID" : indexPurchaseID
			},
			success : function succFunction(data) {
				console.log(data);
				console.log(window.parent.frames.length);

				NoMoreThanZero = false;
				if (data.ERROR == EnumErrorCode.EC_NoError) {
					window.parent.layer.msg(createWarehousingSheetSucceedMSgMSg);
					purchasingOrderR1(indexPurchaseID);
					pageJumping('warehousing.bx', 'warehousing.bx', '入库');
				} else {
					if (data.msg == "") {
						layer.msg(createWarehousingSheetUnsuccessfulMsg);
					} else {
						layer.msg(data.msg);
					}
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(serverErrorMsg);
			}
		});
	}

	// 设置采购单商品表样式
	function settingPurchaseCommTableStyles(key) {
		var tableStyle, textAreaStyle;
		switch (key) {
		case "toCreate":
			tableStyle = "120px";
			textAreaStyle = "104px";
			break;
		case "toRetrieve1":
			tableStyle = "104px";
			textAreaStyle = "88px";
			break;
		default:
			return;
		}
		$("#purchasingOrder th").not(":eq(0)").css("width", tableStyle);
		$("#purchasingOrder tbody .wrap").css("width", tableStyle);
		var tr = $("#purchasingOrder tbody tr");
		for (var i = 0; i < tr.length; i++) {
			$(tr[i]).find("td").not(":eq(0)").css("width", tableStyle);
			$(tr[i]).find("td:nth-child(3) .wrap").css({
				"padding-right" : "16px",
				"width" : textAreaStyle
			});
		}
	}

	var bDesertLastOperation = false; // 用于提示是否放弃之前的操作，false表示放弃，true表示不放弃。比如，用户点击了新建，然后点击搜索，这时会弹出提示。另外的一层意思：是否修改信息
	// 采购订单中某个采购商品的数量修改监听
	window.purchaseCommNOManage = function(index) {
		isModifyData();
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
			bDesertLastOperation = false;
		} else {
			popupCommDataManage("add", commID, "", "", "", "", "", NO, "", "");
		}
		$(index).val(NO);
		$(index).parents("td").nextAll().eq(1).find(".wrap").attr("title", (NO * price).toFixed(2)).text((NO * price).toFixed(2));
		calculatedPurchaseTotalPrice();
	}

	// 入库数量验证
	window.warehousingCommNOManage = function(index) {
		var NO = parseInt($(index).val());
		var commID = $(index).parents("tr").find('input[name="商品ID"]').val();
		if (isNaN(NO) || NO < 0 || NO > (Math.abs(2 << 30) - 1)) {
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
	}
	function isModifyData() {// 是否修改数据
		if (datachange) {
			bDesertLastOperation = true;
			datachange = false;
		} else {
			bDesertLastOperation = false;
		}
	}

	// 采购订单中某个o采购商品的建议采购价修改监听
	window.purchaseCommPriceManage = function(index) {
		isModifyData();
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
			bDesertLastOperation = false;
		} else {
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commID) {
					popupCommodityData[i].price = price;
				}
			}
		}
		$(index).val(parseFloat(price).toFixed(2));
		$(index).parents("td").next().find(".wrap").attr("title", (NO * price).toFixed(2)).text((NO * price).toFixed(2));
		calculatedPurchaseTotalPrice();
	}

	// 删除商品
	window.deletecommodity = function(obj) {
		bDesertLastOperation = true;
		var $index = $(obj);
		var commID = $index.siblings("input").val();
		console.log(popupCommodityData);
		popupCommDataManage("delete", commID);
		console.log(popupCommodityData);
		$index.parents("tr").remove();
		var purchaseCommTR = $("div.table-data table tbody tr"); // 获取剩余的商品tr单元行
		for (var i = 0; i < purchaseCommTR.length; i++) {
			$(purchaseCommTR[i]).find("span").text(i + 1);
		}
		if (purchaseCommTR.length == 0) {
			purchaseCommTableRestPose();
		}
	}

	// 即时搜索事件
	window.instantSearch = function(index) {
		$(index).next().click();
	}

	var datachange = false; // 是否数据进行改变
	// 检查是否改变数值，改变才可以进行修改
	window.check_ifDataChange = function(obj) {
		let jumpOut = false;
		let commID = '';
		let NO = '';
		let price = '';
		$(obj).parents("tbody").find("tr").each(function(i) {
			commID = $(obj).parents("tbody").find("tr").eq(i).find("td").eq(0).find("input[name='商品ID']").val();
			NO = $(obj).parents("tbody").find("tr").eq(i).find("td").eq(4).find("input[name='NO']").val();
			price = $(obj).parents("tbody").find("tr").eq(i).find("td").eq(5).find("input[name='price']").val();
			console.log("NO:" + NO + "price:" + price);
			for (let j = 0; j < initialData.length; j++) {
				if (initialData[j].commID == commID) {
					if (initialData[j].price == price && initialData[j].NO == NO) {
						datachange = false;
					} else {
						if (isNaN(NO) || NO < 0 || NO > (Math.abs(2 << 30) - 1) || isNaN(price) || price < 0 || price > (Math.abs(2 << 30) - 1)) {
							datachange = false;
						} else {
							datachange = true;
							jumpOut = true;
							break;
						}
					}
				}
			}
			if (jumpOut) {
				return false;
			}
		});
		console.log(datachange);
	}

	window.remark = function(index) {
		bDesertLastOperation = true;
	}

	// 供应商数据渲染
	table.render({
		elem : '#providerList',
		url : proRN_url,
		id : proRN_tableID,
		method : method_post,
		request : {
			pageName : 'pageIndex',
			limitName : 'pageSize'
		},
		response : {
			dataName : 'objectList',
		},
		skin : 'nob',
		even : true,
		page : true,
		cols : [ [ {
			type : 'numbers',
			title : '序号'
		}, {
			templet : '#barDemo',
			title : '操作',
			width : 60,
			align : 'center',
			event : 'choose'
		}, {
			field : 'name',
			title : '供应商名称',
			width : 160,
			align : 'center',
			event : 'choose'
		}, {
			field : 'contactName',
			title : '联系人',
			width : 100,
			align : 'center',
			event : 'choose'
		}, {
			field : 'mobile',
			title : '联系电话',
			width : 140,
			align : 'center',
			event : 'choose'
		}, {
			field : 'address',
			title : '地址',
			width : 200,
			align : 'center',
			event : 'choose'
		} ] ],
		done : function(res, curr, count) {
			$("#chooseProviderWindow .rightRegion .layui-table tbody tr").each(function(i) { // 翻页后给选择的供应商添加样式
				if ($(this).find("td").eq(2).find("div.layui-table-cell").eq(0).text() == indexProviderName) {
					$(this).find("td").eq(2).find("div.layui-table-cell").click();
				}
			});
		}
	});

	// 渲染商品弹窗表格popupCommodityList
	table.render({
		elem : '#popupCommodityList',
		url : allCommRN_url,
		where : {
			type : 0
		},
		method : "POST",
		id : allCommRN_tableID,
		request : {
			pageName : 'pageIndex',
			limitName : 'pageSize',
		},
		response : {
			dataName : 'objectList',
		},
		skin : "nob",
		even : true,
		page : true,
		cols : [ [ {
			type : 'checkbox'
		}, {
			field : 'name',
			title : '商品名称',
			width : 170,
			align : 'center',
			templet : function(data) {
				console.log(data);
				var name = data.commodity.name;
				return name;
			}
		}, {
			field : 'string',
			title : '条形码',
			align : 'center',
			width : 150,
			templet : function(data) {
				var barcode = '';
				if (data.listBarcodes.length != 0) {
					barcode = data.listBarcodes[0].barcode;
				}
				return barcode;
			}
		}, {
			field : 'num',
			title : '数量',
			templet : '#popupNumManage',
			width : 150,
			align : 'center'
		}, {
			field : 'packageUnitName',
			title : '包装单位',
			align : 'center',
			width : 90,
			templet : function(data) {
				var packageUnitName = data.commodity.packageUnitName;
				return packageUnitName;
			}
		}, {
			field : 'latestPricePurchase',
			title : '最近采购价',
			width : 100,
			align : 'center',
			templet : function(data) {
				var latestPricePurchase = data.commodity.listSlave2[0].latestPricePurchase;
				if (latestPricePurchase == -1) {
					latestPricePurchase = "";
				} else {
					latestPricePurchase = parseFloat(latestPricePurchase).toFixed(2);
				}
				return latestPricePurchase;
			}
		} ] ],
		done : function(res, curr, count) {
			// 换页的时候检查新页面是否有已选择的商品
			if (res.ERROR != EnumErrorCode.EC_NoError) {
				if (res.msg) {
					layer.msg(res.msg);
				} else {
					layer.msg(jurisdictionMsg);
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

	// 采购订单列表单击事件
	var provider_Name = '';
	$(".purchase-list ul").delegate("li", "click", function() {
		var purchasingOrderID = $(this).attr("data");
		var domIndex = $(this);
		provider_Name = $(this).find("label.provider-name").text();
		if (bDesertLastOperation) {
			layer.confirm('确定要放弃之前的操作吗？', {
				icon : 3,
				title : '提示'
			}, function(index, layero) {
				var i = layer.load(1);
				layer.close(index);
				domIndex.addClass("click").siblings().removeClass("click"); // 单击效果
				$(".content").css("margin-right", "80px");
				$(".creator").css("margin-right", "120px");
				purchasingOrderR1(purchasingOrderID);
				bDesertLastOperation = false;
				layer.close(i);
			}, function(index, layero) {
				layer.close(index);
			})
		} else {
			$(".content").css("margin-right", "80px");
			$(".creator").css("margin-right", "120px");
			domIndex.addClass("click").siblings().removeClass("click"); // 单击效果
			purchasingOrderR1(purchasingOrderID);
		}
	});

	// 供应商弹出层
	$(".addProvider").click(function() {
		providerPopupIndex = layer.open({
			type : 1,
			area : popupPageWidth,
			content : $("#chooseProviderWindow"),
			success : function(layero, index) {
				reloadTable(table, proRN_tableID, method_post, proRN_url, curr_reloadTable, provider_reloadTable);
			},
			cancel : function(index, layero) {
				indexProviderID = $(".topNav .supplier .providerID").text(); // 获取页面供应商ID(即采购订单的providerID)
				indexProviderName = $(".topNav .supplier .providerName").text(); // 获取页面的供应商名称
			}
		});
	})
	
	// 模糊搜索供应商
	form.on('submit(providerSearch)', function(data) {
		var queryKeyword = data.field.queryKeyword;
		if (queryKeyword.length <= 32) {
			var indexLoading = layer.load(1);
			provider_reloadTable.queryKeyword = queryKeyword;
			reloadTable(table, proRN_tableID, method_post, proRNB_url, curr_reloadTable, provider_reloadTable);
			layer.close(indexLoading);
		} else {
			// 关键字超过最大限制长度
		}
		return false;
	})

	// 根据供应商类别重载数据表格
	$("#chooseProviderWindow .layui-nav-child dd").click(function() {
		var loading = layer.load(1);
		var districtID = $(this).find("input").val();
		provider_reloadTable.districtID = districtID;
		reloadTable(table, proRN_tableID, method_post, proRN_url, curr_reloadTable, provider_reloadTable);
		layer.close(loading);
	});

	// 监听表格操作providerList
	table.on('tool(providerList)', function(obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var tr = obj.tr
		if (layEvent == 'choose') {
			$(".barDemo").removeClass("color");
			$(tr).find("i.layui-icon-ok-circle").addClass("color");
			indexProviderName = data.name;
			indexProviderID = data.ID;
		}
	})
	// 供应商弹出层确定按钮
	$("#confirm").click(function() {
		bDesertLastOperation = true;
		$(".providerName").attr("title", indexProviderName).text(indexProviderName);
		$(".providerID").text(indexProviderID);
		layer.close(providerPopupIndex);
	})
	// 供应商弹出层取消按钮
	$("#cancel").click(function() {
		indexProviderID = $(".topNav .supplier .providerID").text(); // 获取页面供应商ID(即采购订单的providerID)
		indexProviderName = $(".topNav .supplier .providerName").text(); // 获取页面的供应商名称
		layer.close(providerPopupIndex);
	})

	// 是否全部展开供应商类别
	$(".showAllProviderDistrict").click(function() {
		if ($(this).text() == "全部展开") {
			$("#chooseProviderWindow .layui-nav-item").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		} else {
			$("#chooseProviderWindow .layui-nav-item").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
		}
	});
	
	//门店弹出层事件
	$(".addShop").click(function() {
		shopPopupIndex = layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#toChooseShop"),
			success: function (layero, index) {
				reloadTable(table, shopRN_tableID, method_get, shopRN_url, 1, shop_reloadTable);
			},
			cancel: function (index, layero) {
				shopIDBeChoosed = $(".topNav .supplier .shopID").val();		//获取页面的门店ID
				providerNameBeChoosed = $(".topNav .supplier .shopName").text();		//获取页面的门店ID
			}
		});
	})
	
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
			{ title: '操作', align: 'center', width: 60, templet: '#shopDemo', event: 'choose' },
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
	//门店表格监听事件
	var shopIDBeChoosed = -1;
	var shopNameBeChoosed = "";
	table.on('tool(popupShopList)', function (obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var tr = obj.tr;
		switch (layEvent) {
			case "choose":
				shopIDBeChoosed = data.ID;
				shopNameBeChoosed = data.name;
				tr.find(".shopDemo").addClass("shopBeChoosed");
				tr.siblings().find(".shopDemo").removeClass("shopBeChoosed");
				break;
			default:		//其他情况
				break;
		}
	})
	//确认选择门店
	$("#toChooseShop .confirmChoosedShop").click(function () {
		bDesertLastOperation = true;
		$(".shopName").attr("title", shopNameBeChoosed).text(shopNameBeChoosed);
		$(".shopID").text(shopIDBeChoosed);
		layer.close(shopPopupIndex);		//关闭最新弹出的层
	});
	//门店弹出层“取消”按钮
	$("#toChooseShop .exitChoosedShop").click(function () {
		shopIDBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .shopID").val();		//获取页面的门店ID
		shopNameBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .shopName").text();		//获取页面的门店ID
		layer.close(shopPopupIndex);		//关闭最新弹出的层
	});
	// 模糊搜索门店（名称）
	form.on('submit(shopSearch)', function (data) {
		var queryKeyword = data.field.queryKeyword;
		if (queryKeyword.length <= 32) {
			var indexLoading = layer.load(1);
			var shopSearchData = new Object();
			shopSearchData.queryKeyword = data.field.queryKeyword;
			reloadTable(table, shopRN_tableID, method_post, shopRNByFields_url, 1, shopSearchData);
		} else {
			//超过长度限制不发送请求
		}
		return false;
	});
	//门店弹出层左侧列表事件
	$("#toChooseShop ul li a").click(function () {
		var districtID = $(this).attr("indexID");
		shop_reloadTable.districtID = districtID;
		reloadTable(table, shopRN_tableID, method_get, shopRN_url, 1, shop_reloadTable);
	});

	// 监听商品表格复选框选择popupCommodityList
	table.on('checkbox(popupCommodityList)', function(obj) {
		var checked = obj.checked; // 当前是否选中状态
		var data = obj.data; // 选中行的相关数据
		var type = obj.type; // 如果触发的是全选，则为：all，如果触发的是单选，则为：one
		var tr = obj.tr; // 选中行的dom对象
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
			for (var i = 0; i < popupCommodityData.length; i++) { // 检查是否已存在此商品(为表格切换页面时的bug而补充)
				if (popupCommodityData[i].commID == commID) {
					NO = popupCommodityData[i].NO;
				}
			}
			if (checked) {
				popupCommDataManage("add", commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, specification);
				initialData = popupCommodityData;
				$(tr).find(".popupCommNum").val(NO);
			} else {
				popupCommDataManage("delete", commID);
				initialData = popupCommodityData;
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
					for (var j = 0; j < popupCommodityData.length; j++) { // 检查是否已存在此商品(为全选时数量不一致的bug而补充)
						if (popupCommodityData[j].commID == commID) {
							commodityNotExist = false;
						}
					}
					if (commodityNotExist) {
						popupCommDataManage("add", commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, specification);
						initialData = popupCommodityData;
					}
				}
				$("#popupCommodityList + div .popupCommNum").each(function() { // 为空的输入框初始赋值
					if ($(this).val() == "") {
						$(this).val("1");
					}
				});
			} else {
				for (var i = 0; i < data.length; i++) {
					var commID = data[i].commodity.ID;
					popupCommDataManage("delete", commID);
					initialData = popupCommodityData;
				}
				$("#popupCommodityList + div .popupCommNum").val("");
			}
		}
	});

	// 商品弹出层
	$("div.table-data table tbody").delegate("button", "click", function() {
		commodityPopupIndex = layer.open({
			type : 1,
			area : popupPageWidth,
			content : $("#allCommodity"),
			success : function(layero, index) {
				// 弹出层的商品数
				$(".commodityKinds").text(popupCommodityData.length);
				notRecordActions(popupCommodityData, tempArray);
				reloadTable(table, allCommRN_tableID, method_post, allCommRN_url, curr_reloadTable, commodity_reloadTable);
			},
			cancel : function(index, layero) {
				notRecordActions(tempArray, popupCommodityData);
			}
		});
	});

	// 弹出层确定按钮
	$(".confirmChooseCommodity").click(
			function() {
				bDesertLastOperation = true;
				dataChange = true;// 暂时这样写
				var indexLoading = layer.load(1);
				$("div.table-data table tbody").empty();
				layer.close(commodityPopupIndex);
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
						$("div.table-data table tbody").append(
								"<tr data='" + i + "' >" + "<td style='width:60px;'><i class='layui-icon layui-icon-close-fill deletecommodity' onclick='deletecommodity(this)'></i><span>" + (i + 1)
										+ "</span><input name='商品ID' type='hidden' value='" + commID + "'/></td>" + "<td><div class='wrap' title='" + barcodeName + "'>" + barcodeName + "</div></td>" + "<td><div class='wrap' title='"
										+ commName + "'>" + commName + "</div><button ><i class='layui-icon layui-icon-add-circle addGeneralComm' title='添加商品'></i></button></td>" + "<td>" + packageUnitName
										+ "<input id='id' name='条形码ID' type='hidden' value='" + barcodeID + "'/></td>" + "<td><input type='text' onchange='check_ifDataChange(this,this.value);purchaseCommNOManage(this);' value='" + NO
										+ "'name = 'NO'/></td>" + "<td><input type='text' onchange='check_ifDataChange(this,this.value);purchaseCommPriceManage(this);'  value='" + parseFloat(price).toFixed(2) + "' name='price'/>" + "</td>"
										+ "<td><div class='wrap' title='" + parseFloat(NO * price).toFixed(2) + "'>" + parseFloat(NO * price).toFixed(2) + "</div></td>" + "</tr>");
						if (!toCreatePurchase) {
							$("div.table-data table tbody tr[data='" + i + "']").append("<td>0</td>" + "<td>" + NO + "</td>" + "<td>" + NO + "</td>" + "<td>" + parseFloat(price).toFixed(2) + "</td>");
							settingPurchaseCommTableStyles("toRetrieve1");
						} else {
							settingPurchaseCommTableStyles("toCreate");
						}
					}
					calculatedPurchaseTotalPrice();
				} else {
					purchaseCommTableRestPose();
				}
				layer.close(indexLoading);
				// 表格行颜色
				$("div.table-data table tbody tr:odd").addClass("odd");
			});
	// 商品弹出层取消按钮
	$(".closeLayerPage").click(function() {
		notRecordActions(tempArray, popupCommodityData);
		layer.close(commodityPopupIndex);
	});
	// 模糊搜索商品popupCommodityList
	form.on('submit(commoditySearch)', function(data) {
		var indexLoading = layer.load(1);
		var value = data.field.queryKeyword;
		// 补充数据验证 ......
		if (value.length <= 64) {
			commodity_reloadTable.queryKeyword = value;
			reloadTable(table, allCommRN_tableID, method_post, allCommRN_url, curr_reloadTable, commodity_reloadTable);
		}
		layer.close(indexLoading);
		return false;
	})

	// 是否全部展开商品类别
	$(".showAllCommCategory").click(function() {
		if ($(this).text() == "全部展开") {
			$("#allCommodity .layui-nav-item").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		} else {
			$("#allCommodity .layui-nav-item").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
		}
	});

	// 根据商品类别重载数据表格popupCommodityList
	$(".leftRegion ul li dl dd").click(function() {
		var loading = layer.load(1);
		var categoryID = $(this).find("a").attr("indexID");
		commodity_reloadTable.categoryID = categoryID;
		reloadTable(table, allCommRN_tableID, method_post, allCommRN_url, curr_reloadTable, commodity_reloadTable);
		layer.close(loading);
	})

	// 头部导航选项
	$(".topNav .layui-form-label span").click(function() {
		var index = $(this);
		var area = index.offset();
		var areaParent = index.parent().offset();
		var thisUl = index.parent().next();
		if (index.text() == '默认仓库 v') {
			layer.msg(FunctionNotOpenMsg);
			return;
		}
		$(".topNav .layui-form-label + ul").hide();
		thisUl.css({
			"display" : "block",
			"left" : area.left - areaParent.left
		});
	})

	// 根据条件查询采购单
	$(".topNav .layui-form-label span").parent().next().find("li").click(function() {
		var index = $(this).parents(".layui-inline").find(".layui-form-label span");
		var keyword = $(this).text();
		var keydata = index.attr("data");
		var domTHIS = $(this);
		index.parent().next().hide();
		index.find("lable").text($(this).text());
		purchase_reloadTable.pageIndex = curr_reloadTable;
		if (bDesertLastOperation) {
			layer.confirm('确定要放弃之前的操作吗？', {
				icon : 3,
				title : '提示'
			}, function(index, layero) {
				var i = layer.load(1);
				layer.close(index);
				bDesertLastOperation = false;
				conditionSearch(keyword, keydata, domTHIS);
				layer.close(i);
			}, function(index, layero) {
				layer.close(index);
			})
		} else {
			conditionSearch(keyword, keydata, domTHIS);
		}
	})

	function conditionSearch(keyword, keydata, domIndex) {
		var date = new Date();
		var index = domIndex.parents(".layui-inline").find(".layui-form-label span");
		isFirstTimeToVisit = true;
		resetPage = true;
		switch (keyword) {
		case "所有":
			switch (keydata) {
			case "门店":
				purchase_reloadTable.shopID = -1;
				purchaseOrderListRN(purchase_reloadTable);
				break;
			case "状态":
				purchase_reloadTable.status = -1;
				purchaseOrderListRN(purchase_reloadTable);
				break;
			case "经办人":
				purchase_reloadTable.staffID = 0;
				purchaseOrderListRN(purchase_reloadTable);
				break;
			case "供应商":
				purchase_reloadTable.queryKeyword = "";
				purchaseOrderListRN(purchase_reloadTable);
				break;
			case "创建日期":
				purchase_reloadTable.date1 = "1970/01/01 00:00:00";
				purchase_reloadTable.date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss");
				purchaseOrderListRN(purchase_reloadTable);
				break;
			}
			break;
		case "未审核":
			purchase_reloadTable.status = 0;
			purchaseOrderListRN(purchase_reloadTable);
			break;
		case "已审核":
			purchase_reloadTable.status = 1;
			purchaseOrderListRN(purchase_reloadTable);
			break;
		case "部分入库":
			purchase_reloadTable.status = 2;
			purchaseOrderListRN(purchase_reloadTable);
			break;
		case "全部入库":
			purchase_reloadTable.status = 3;
			purchaseOrderListRN(purchase_reloadTable);
			break;
		case "过去一周内":
			var sevenDaysAgo = new Date(date.getTime() - 168 * 60 * 60 * 1000);
			var date1 = sevenDaysAgo.format("yyyy/MM/dd") + " 00:00:00";
			var date2 = date.format("yyyy/MM/dd hh:mm:ss");
			purchase_reloadTable.date1 = date1;
			purchase_reloadTable.date2 = date2;
			purchaseOrderListRN(purchase_reloadTable);
			break;
		case "过去一月内":
			var sevenDaysAgo = new Date(date.getTime() - 720 * 60 * 60 * 1000);
			var date1 = sevenDaysAgo.format("yyyy/MM/dd") + " 00:00:00";
			var date2 = date.format("yyyy/MM/dd hh:mm:ss");
			purchase_reloadTable.date1 = date1;
			purchase_reloadTable.date2 = date2;
			purchaseOrderListRN(purchase_reloadTable);
			break;
		default:
			console.log(domIndex);
			index.find("lable").text(domIndex.clone().children().remove().end().text());
			if (domIndex.find("span").text() == "经办人") {
				purchase_reloadTable.staffID = parseInt(domIndex.find("input").val());
			} else if (domIndex.find("span").text() == "供应商") {
				purchase_reloadTable.queryKeyword = domIndex.clone().children().remove().end().text();
			}else if (domIndex.find("span").text() == "门店") {
				purchase_reloadTable.shopID = parseInt(domIndex.find("input").val());
			}
			
			purchaseOrderListRN(purchase_reloadTable);
			break;
		}
	}

	// 关闭头部导航选项区域
	$(document).click(function() {
		$(".topNav .layui-form-label + ul").hide();
	})
	$(document).delegate('.topNav .layui-form-label', 'click', function(event) {
		event.stopPropagation();
		console.log("阻止上述事件冒泡");
	})

	// 模糊搜索采购订单
	$(".td_search i").click(function() {
		queryPurchaseOrderPromptBox(true);
	});
	// 通过keyup事件触发模糊搜索采购订单执行的函数
	$(".td_search input").keyup(function(event) {
		if (event.keyCode == "13" && event.key == "Enter") {
			queryPurchaseOrderPromptBox(true);
		} else {
			queryPurchaseOrderPromptBox(false);
		}
	})
	// “确定要放弃之前的操作吗？”提示框--模糊搜索功能
	function queryPurchaseOrderPromptBox(showMsg) {
		if (bDesertLastOperation) {
			layer.confirm('确定要放弃之前的操作吗？', {
				icon : 3,
				title : '提示'
			}, function(index, layero) {
				var i = layer.load(1);
				bDesertLastOperation = false;
				if (showMsg) { // 输入回车键
					queryPurchaseOrder(showMsg);
				} else {
					clearDataOrRecoveryData();
				}
				layer.close(i);
				layer.close(index);
			})
		} else {
			queryPurchaseOrder(showMsg);
		}
	}
	function queryPurchaseOrder(showMsg) {
		var queryKeyword = $(".search-box").val();
		if (queryKeyword.length <= 32) { // 检查输入的关键字长度
			if (queryKeyword.length == $(".search-box").val().trim().length) { // 检查输入的关键字首尾是否有空格
				if (/^$|^[\u2014\u4E00-\u9FA5A-Za-z0-9_\()（）-\s]{1,32}$/.test(queryKeyword)) { // 检查输入的关键字是否满足数据格式要求
					if (queryKeyword.indexOf("CG") != -1 || queryKeyword.indexOf("C") != -1 || queryKeyword.indexOf("G") != -1 || !isNaN(queryKeyword)) { // 检查是否存在采购订单号的关键字眼或者是否为纯数字
						if (queryKeyword.length >= 10 || queryKeyword.length == 0) {
							purchase_reloadTable.queryKeyword = queryKeyword;
						} else {
							if (showMsg) {
								clearDataOrRecoveryData();
								layer.msg("数据格式不正确，采购订单号至少需要输入10位", {
									id : "queryMsg"
								});
							}
							return;
						}
					} else {
						purchase_reloadTable.queryKeyword = queryKeyword;
					}
					isFirstTimeToVisit = true;
					purchase_reloadTable.pageIndex = curr_reloadTable;
					resetPage = true;
					purchaseOrderListRN(purchase_reloadTable);
				} else {
					clearDataOrRecoveryData();
					layer.msg("数据格式不正确，请输入中英文、数字，只允许中间有空格，长度为(0,32]，支持输入的符号有：（）()_-——", {
						id : "queryMsg"
					});
				}
			} else {
				clearDataOrRecoveryData();
				layer.msg("输入的关键字首尾不能有空格，只允许中间有空格", {
					id : "queryMsg"
				});
			}
		} else {
			// layer.msg("长度超出限制，输入的关键字最大长度为32", {id: "queryMsg"});
			// //前端界面已经限制了输入的最大长度，故超出长度时不做处理和提示
			return;
		}
	}

	// 清空新建的数据/恢复采购订单的数据--当模糊搜索值不符合查询需求时调用
	function clearDataOrRecoveryData() {
		if (indexPurchaseID == '') {
			$(".tabControl .purchaseManage").eq(0).click();
			return;
		}
		purchasingOrderR1(indexPurchaseID);
	}

	// 按钮状态引导
	function buttonStatusControl(ButtonName) {
		switch (ButtonName) {
		case "新建":
			setBtnStatus();
			var Nouveau = $(".tabControl ul li").eq(0).find("button");
			Nouveau.removeClass("tabColor").parent("li").next().find("button").addClass("tabColor");
			Nouveau.parent("li").next().next().find("button").attr("disabled", "disabled");
			Nouveau.parent("li").next().next().find("button").addClass("unavailable");
			$("#button_warehousing").attr("disabled", "disabled");
			$("#button_approve").attr("disabled", "disabled");
			break;
		case "取消":
			$(".tabControl ul li").eq(3).find("button").removeClass("tabColor");
			break;
		case "未审核":
			setBtnStatus();
			$(".tabControl ul li").eq(1).find("button").addClass("tabColor");
			$("#button_warehousing").attr("disabled", "disabled");
			break;
		case "入库":
			setBtnStatus();
			$(".tabControl ul li").eq(1).find("button").addClass("tabColor");
			$(".tabControl ul li").eq(0).find("button").addClass("unavailable").attr("disabled", "disabled");
			$(".tabControl ul li").eq(2).find("button").addClass("unavailable").attr("disabled", "disabled");
			$("div.content ul li").eq(0).removeClass("cost_audit_stastu");
			$("#button_warehousing").attr("disabled", "disabled");
			break;
		case "RN":
			setBtnStatus();
			$(".tabControl ul li button").removeClass("tabColor");
			break;
		case "无数据":
			$(".purchaseManage").removeClass("unavailable");
			$(".tabControl ul li").eq(0).find("button").addClass("tabColor");
			$(".tabControl ul li:gt(0)").find("button").addClass("unavailable").attr("disabled", "disabled");
			;
			$("div.content ul li").removeClass("cost_audit_stastu");
			$("div.content ul li button").attr("disabled", "disabled");
			break;
		default:
			$("#button_warehousing").removeAttr("disabled", "disabled")
			$(".purchaseManage").removeClass("tabColor");
			$(".tabControl ul li:gt(0) button").attr("disabled", "disabled");
			$(".tabControl ul li:gt(0) button").addClass("unavailable");
			break;
		}
	}

	function setBtnStatus() { // 清空按钮状态与清楚按钮不可以单击
		$(".purchaseManage").removeAttr("disabled");
		$(".purchaseManage").removeClass("unavailable");
	}

	function toCreatePurchasingOrder() { // 新建时页面状态与相应变化
		$(".content").css("margin-right", "300px");
		$(".creator").css("margin-right", "340px");
		$(".table-data table thead tr th:gt(6)").hide();
		$(".addGeneralprovi").show();
		$(".addProvider").show();
		$(".addShop").show();
		$(".odd-numbers label").text("");
		$(".odd-numbers").hide(); // 新建时没有采购订单号
		popupCommodityData = [];
		toCreateWarehouse = false;
		toCreatePurchase = true;
		toUpdatePurchase = false;
		bDesertLastOperation = true;
		indexPurchaseID = '';
		// 更新现在状态
		$(".purchase-list ul li").removeClass("click");
		$("#div_span").text(new Date().format("yyyy-MM-dd") + "/新建采购订单");
		$(".providerName").attr("title", "默认供应商").text("默认供应商");
		$(".providerID").text("1");
		$(".shopName").attr("title", "默认门店").text("默认门店");
		$(".shopID").text("2");
		$(".creator").text("创建人：");
		$("div.table-data table tbody").empty();
		purchaseCommTableRestPose();
		settingPurchaseCommTableStyles("toCreate");
		// 添加入库按钮状态
		$("div.content ul li").eq(0).removeClass("cost_audit_stastu");
		$("#button_warehousing").attr("disabled", "disabled");
		// 添加审核按钮状态
		$("div.content ul li").eq(1).removeClass("cost_audit_stastu");
		$("#button_approve").text("审核")
		$("#button_approve").attr("disabled", "disabled");
		$(".money").text("0.00元");
		$(".workflow ul li").eq(0).addClass("workflow_li").siblings().removeClass("workflow_li");// 工作流的状态
		$(".span_v").removeAttr("style");
		$(".remark").html("<textarea cols='80' rows='6' id='remark' onchange='remark(this)'  style='resize:none'></textarea>");
		buttonStatusControl("新建");
		indexProviderName = "默认供应商"; // 供应商名称
		indexProviderID = 1;
		shopNameBeChoosed = "默认门店";
		shopIDBeChoosed = 2;
	}

	// 采购订单详情区顶部按钮监听
	$(".purchaseManage").click(function() {
		var tabBtn = $(this).text(); // 按钮文本
		$(this).addClass("tabColor").parent("li").siblings().find("button").removeClass("tabColor");
		switch (tabBtn) {
		case "新建":
			if (bDesertLastOperation) {
				layer.confirm('确定要放弃之前的操作吗？', {
					icon : 3,
					title : '提示'
				}, function(index, layero) {
					var i = layer.load(1);
					layer.close(index);
					bDesertLastOperation = false;
					toCreatePurchasingOrder();
					layer.close(i);
				}, function(index, layero) {
					$(".purchaseManage").removeClass("tabColor");
					$(".tabControl ul li").eq(1).find("button").addClass("tabColor");
					layer.close(index);
				})
			} else {
				toCreatePurchasingOrder();
			}
			break;
		case "保存":
			var indexLoading = layer.load(1);
			if (toCreatePurchase) {
				layer.confirm('确定要创建这个采购订单吗？', {
					icon : 3,
					title : '提示'
				}, function(index, layero) {
					var i = layer.load(1);
					layer.close(index);
					purchaseOrderManage(createPurchase_url, "创建采购订单成功", "创建采购订单失败");
					layer.close(i);
				}, function(index, layero) {
					layer.close(index);
				})
			} else if (toCreateWarehouse) {
				layer.confirm('确定要创建这个入库单吗？', {
					icon : 3,
					title : '提示'
				}, function(index, layero) {
					var i = layer.load(1);
					layer.close(index);
					createWarehousingSheet();
					layer.close(i);
				}, function(index, layero) {
					layer.close(index);
				})
			} else if (toUpdatePurchase) {
				layer.confirm('确定要修改这个采购订单吗？', {
					icon : 3,
					title : '提示'
				}, function(index, layero) {
					if (bDesertLastOperation) {
						var i = layer.load(1);
						bDesertLastOperation = false;
						purchaseOrderManage(updatePurchase_url, "修改采购订单成功", "修改采购订单失败");
						layer.close(i);
					} else {
						layer.msg(modifySaveMsg);
					}
				}, function(index, layero) {
					layer.close(index);
				})
			} else {
				layer.msg(canNotUpdateMsg);
			}
			layer.close(indexLoading);
			break;
		case "删除":
			layer.confirm('确定要删除这个采购订单吗？', {
				icon : 2,
				title : '提示'
			}, function(index, layero) {
				var i = layer.load(1);
				var indexLoading = layer.load(1);
				if (indexPurchaseID) {
					$.ajax({ // 删除采购订单
						url : deletePurchase_url,
						type : method_get,
						async : true,
						dataType : "json",
						data : {
							"ID" : indexPurchaseID
						},
						success : function succFunction(data) {
							console.log(data);
							if (data.ERROR != EnumErrorCode.EC_NoError) {
								if (data.msg) {
									layer.msg(data.msg);
								} else {
									layer.msg(ghostDeletePurchaseOrderMsg);
								}
							} else {
								layer.close(i);
								layer.msg(deleteSucceedMsg);
								isFirstTimeToVisit = true;
								resetPage = true;
								bDesertLastOperation = false;
								initQueryCondition();
								purchaseOrderListRN(purchase_reloadTable);
							}
							layer.close(indexLoading);
						},
						error : function(XMLHttpRequest, textStatus, errorThrown) {
							layer.close(indexLoading);
							layer.msg(serverErrorMsg);
						}
					});
				} else {
					layer.msg(selectDeletePurchasingOrderMsg);
					layer.close(indexLoading);
				}
			}, function(index) {
				$(".tabControl ul li").eq(1).find("button").addClass("tabColor");
				$(".tabControl ul li").eq(2).find("button").removeClass("tabColor");
				layer.close(index);
			});
			break;
		case "取消":
			if (bDesertLastOperation) {
				layer.confirm('确定要放弃之前的操作吗？', {
					icon : 3,
					title : '提示',
					cancel : function() {
						$(".tabControl ul li").eq(3).find("button").removeClass("tabColor");
						$(".tabControl ul li").eq(1).find("button").addClass("tabColor");
					}
				}, function(index, layero) {
					layer.close(index);
					bDesertLastOperation = false;
					buttonBeClick_cancel();
				}, function(index, layero) {
					layer.close(index);
					$(".tabControl ul li").eq(3).find("button").removeClass("tabColor");
					$(".tabControl ul li").eq(1).find("button").addClass("tabColor");
				})
			} else {
				buttonBeClick_cancel();
			}
			break;
		case "入库":
			$(this).attr("disabled", "disabled");
			toCreateWarehouse = true;
			toCreatePurchase = false;
			toUpdatePurchase = false;
			bDesertLastOperation = true;
			for (var i = 0; i < $("div.table-data table tbody tr").length; i++) {
				sNum = popupCommodityData[i].NO;
				sSum = $("div.table-data table tbody tr").eq(i).find("td").eq(10).text();
				$("div.table-data table tbody tr").eq(i).find("td").eq(9).html("<input type='text' onchange='warehousingCommNOManage(this);' value='" + sNum + "'/>");
				$("div.table-data table tbody tr").eq(i).find("td").eq(10).html("<input type='text' onchange='purchaseCommPriceManage(this);'  value='" + sSum + "'/>");
			}
			buttonStatusControl("入库");
			break;
		case "审核":
			layer.confirm('确定要审核这个采购订单吗？', {
				icon : 3,
				title : '提示'
			}, function(index, layero) {
				var i = layer.load(1);
				toUpdatePurchase = false;
				toCreateWarehouse = false;
				toCreatePurchase = false;
				purchaseOrderManage(purchaseRapprove_url, "审核采购订单成功", "审核采购订单失败");
				layer.close(i);
			}, function(index, layero) {
			})
			break;
		}
	})
	// 点击取消按钮要执行的函数
	function buttonBeClick_cancel() {
		var i = layer.load(1);
		if (lastVisitPurchaseID == null) {
			$(".providerName").attr("title", "").text("");
			$(".providerID").text("");
			$(".shopName").attr("title", "").text("");
			$(".shopID").text("");
			$(".span_v").attr("style", "display:none");
			$(".table-data table tbody").empty();
			$(".table-data table thead tr th:gt(6)").show();
			$(".content").css("margin-right", "80px");
			$(".creator").css("margin-right", "120px");
			$("#div_span").text("/采购订单");
			buttonStatusControl("无数据");
			layer.close(i);
			return;
		}
		isFirstTimeToVisit = true;
		resetPage = true;
		initQueryCondition();
		purchaseOrderListRN(purchase_reloadTable);
		restoreDefault();
		$(".content").css("margin-right", "80px");
		$(".creator").css("margin-right", "120px");
		buttonStatusControl("取消");
		layer.close(i);
	}

	// 恢复模糊搜索的页面初始状态
	function restoreDefault() {
		$(".layui-inline").each(function(i) {
			$(".layui-inline .layui-form-label").eq(i).find("span").html('<lable>' + $(".layui-inline").eq(i).find("ul li").eq(0).text() + '</lable>&nbsp;v</span>');
		});
		$(".search-box").val("CG");
	}

})