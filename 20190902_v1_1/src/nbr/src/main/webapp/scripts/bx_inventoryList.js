layui.use(['element', 'form', 'table', 'layer'], function () {
	var element = layui.element;
	var form = layui.form;
	var table = layui.table;
	var layer = layui.layer;
	//定义常量
	const inventorySheetRN_url = "inventorySheet/retrieveNEx.bx";		//盘点单列表查询接口
	const inventorySheetRNB_url = "inventorySheet/retrieveNByFieldsEx.bx";		//盘点单模糊搜索接口
	const inventorySheetCreate_url = "inventorySheet/createEx.bx";		//创建盘点单接口
	const inventorySheetUpdate_url = "inventorySheet/updateEx.bx";		//修改盘点单接口
	const inventorySheetDelete_url = "inventorySheet/deleteEx.bx";		//删除盘点单接口
	const inventorySheetSubmit_url = "inventorySheet/submitEx.bx";		//提交盘点单接口
	const inventorySheetApprove_url = "inventorySheet/approveEx.bx";		//审核盘点单接口
	const commRN_url = "commodity/retrieveNEx.bx";		//查询商品接口
	const shopRNByFields_url = 'shop/retrieveNByFieldsEx.bx';		//模糊查询门店
	const shopRN_url = "shop/retrieveNEx.bx";		//查询门店接口
	const inventorySheetRN_tableID = "inventorySheetList";		//inventorySheetList表格ID
	const popupCommRN_tableID = "popupCommodityList";		//弹出普通商品表表格ID
	const inventorySheetStatus = ["待录入", "已提交", "已审核", "已删除", "未知状态"];		//盘点单的状态
	const method_get = "GET";		//get请求方式
	const method_post = "POST";		//post请求方式
	const inventorySheet_reloadTable = { "status": -1 };		//盘点单搜索传参
	const commodity_reloadTable = { "status": -1, "type": 0 };		//商品搜索传参
	const curr_reloadTable = 1;		//默认页码
	//定义layui的use函数中的伪全局变量
	var bDesertLastOperation = true;		//用于提示是否放弃之前的操作，true表示放弃，false表示不放弃。比如，用户点击了新建，然后点击搜索，这时会弹出提示
	var isUpdateInventorySheetData = false;		//更新了盘点单信息

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

	//页面初始加载层
	layer.ready(function () {
		var indexLoading = layer.load(1);
		fieldFormat(form);		//表单数据验证
	});
	//查看盘点单详情
	function inventorySheetR1 (data) {
		console.log(data);
		var ID = data.ID;
		var SN = data.sn;
		var status = data.status;
		var staffID = data.staffID;
		var shopID = data.shopID;
		var creatorName = data.creatorName;
		var createDatetime = data.createDatetime;
		var warehouseID = data.warehouseID;
		var scope = data.scope;
		var approverName = data.approverName;
		var approveDatetime = data.approveDatetime;
		var remark = data.remark;
		var inSheetCommList = data.listCommodity;
		var inSheetSlave1List = data.listSlave1;
		createDatetime = createDatetime.substring(0, createDatetime.length - 4);
		approveDatetime = approveDatetime.substring(0, approveDatetime.length - 4);
		$(".changeInSheetStatus").show();
		switch (status.toString()) {
			case "0":
				status = inventorySheetStatus[0];
				$(".changeInSheetStatus").text("提交");
				buttonStatusControl("waitForInput");
				break;
			case "1":
				status = inventorySheetStatus[1];
				$(".changeInSheetStatus").text("审核");
				buttonStatusControl("submitted");
				break;
			case "2":
				status = inventorySheetStatus[2];
				$(".changeInSheetStatus").hide();
				buttonStatusControl("hadApproved");
				break;
			default:
				status = inventorySheetStatus[4];
				break;
		}
		form.val("inventorySheetInfo", {
			"ID": ID,
			"sn": SN,
			"status": status,
			"staffID": staffID,
			"shopID": shopID,
			"creatorName": creatorName,
			"createDatetime": createDatetime,
			"warehouseID": warehouseID,
			"scope": scope,
			"approverName": approverName,
			"approveDatetime": approveDatetime,
			"remark": remark,
		})
		$("#rightSide .layui-form-item select").find("option").attr("disabled", "disabled");
		form.render('select', 'inventorySheetInfo');
		$(".notShowWhenCreateInSheet").show();
		inSheetCommRN(inSheetCommList, inSheetSlave1List, status);
	}
	//盘点单详情里的盘点商品的解析
	function inSheetCommRN (inSheetCommList, inSheetSlave1List, status) {
		console.log(status);
		for (var i = 0; i < inSheetCommList.length; i++) {
			inSheetSlave1List[i].currentCommName = inSheetCommList[i].name;
		}
		$("#rightSide .inventoryCommodityTable tbody").html("");
		popupCommodityData = [];
		for (var i = 0; i < inSheetSlave1List.length; i++) {
			var serialNumber = i + 1;		//序号
			var inCommSheetID = inSheetSlave1List[i].ID;		//盘点商品表ID
			var commID = inSheetSlave1List[i].commodityID;		//盘点商品ID
			var commName = inSheetSlave1List[i].commodityName;		//商品名称
			var specification = inSheetSlave1List[i].specification;		//商品规格
			var barcode = inSheetSlave1List[i].barcodes;		//商品条形码
			var packageUnitName = inSheetSlave1List[i].packageUnitName;		//商品包装单位
			var noReal = inSheetSlave1List[i].noReal;		//商品实盘数量
			var noSystem = inSheetSlave1List[i].noSystem;		//商品系统库存
			if (status == inventorySheetStatus[0] || status == inventorySheetStatus[1]) {		//待录入和已提交的盘点单显示最新的商品名称
				commName = inSheetSlave1List[i].currentCommName;
			}
			popupCommDataManage("add", commID, commName, "", barcode, "", packageUnitName, noReal, inCommSheetID, specification);		//盘点商品表ID存放于临时存储数据的price字段，因为本页面木有用到price，而原先定义的函数没有存放盘点商品表ID的字段
			inSheetCommTableRender(serialNumber, commID, commName, barcode, packageUnitName, specification, noReal);
			$("#rightSide .inventoryCommodityTable tbody tr:last").find("td:first").text(serialNumber);
			if (status == inventorySheetStatus[1]) {		//已提交的盘点单
				$("#rightSide .layui-table tbody tr:last").append('<td title="***">***</td>');
			} else if (status == inventorySheetStatus[2]) {		//已审核的盘点单
				if (noReal == noSystem) {
					$("#rightSide .layui-table tbody tr:last").append('<td title="' + noSystem + '">' + noSystem + '</td>');
				} else {
					$("#rightSide .layui-table tbody tr:last").append('<td title="' + noSystem + '" style="color: red;">' + noSystem + '</td>');
				}
			} else {
				//其他状态的盘点单暂不做特殊处理
			}
		}
		$(".addComm").remove();
		$("#rightSide .layui-textarea").removeAttr("readOnly");
		if (status == inventorySheetStatus[0]) {		//待录入
			$("#rightSide .layui-table thead tr th:last").hide();
			$("#rightSide .layui-table tbody tr .layui-input").addClass("textInput");
		} else {		//已提交和已审核
			$("#rightSide .layui-table thead tr th:last").show();
			$("#rightSide .layui-table tbody tr .layui-input").removeClass("textInput").attr("readOnly", "readOnly");
			if (status == inventorySheetStatus[2]) {		//已审核
				$("#rightSide .layui-textarea").attr("readOnly", "readOnly");
			}
		}
	}
	//渲染盘点单详情区的盘点单商品表
	function inSheetCommTableRender (serialNumber, commID, commName, barcode, packageUnitName, specification, noReal) {
		$("#rightSide .inventoryCommodityTable tbody").append(
			'<tr>' +
			'<td><span>' + serialNumber + '</span><i class="layui-icon layui-icon-close-fill deleteComm" title="删除商品" onclick="deleteComm(this)"></i></td>' +
			'<td title="' + commName + "/" + specification + '">' +
			commName + "/" + specification + '<input type="hidden" class="commID" value="' + commID + '" />' +
			'<i class="layui-icon layui-icon-add-circle addComm" title="添加商品" onclick="toChooseComm()"></i>' +
			'</td>' +
			'<td title="' + barcode + '">' + barcode + '</td>' +
			'<td title="' + packageUnitName + '">' + packageUnitName + '</td>' +
			'<td title="' + noReal + '"><input type="text" class="layui-input textInput" onchange="commNOManage(this),inventorySheetInfoHasUpdate()" value="' + noReal + '" /></td>' +
			'</tr>'
		);
	}
	//恢复盘点商品表初始状态
	function inSheetCommTableRestPose () {
		$("#rightSide .inventoryCommodityTable tbody").html(
			'<tr>' +
			'<td>1</td>' +
			'<td><i class="layui-icon layui-icon-add-circle addComm" title="添加商品" onclick="toChooseComm()"></i></td>' +
			'<td></td>' +
			'<td></td>' +
			'<td></td>' +
			'</tr>'
		);
	};
	//即时搜索事件
	window.instantSearch = function (index) {
		$(index).next().click();
	}
	//选择商品弹窗
	window.toChooseComm = function () {
		layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#toChooseComm"),
			success: function (layero, index) {
				notRecordActions(popupCommodityData, tempArray);
				reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, commodity_reloadTable);
				$(".popupPage .footArea strong").text(popupCommodityData.length);
			},
			cancel: function (index, layero) {
				notRecordActions(tempArray, popupCommodityData);
			}
		});
	}
	//某个盘点商品的数量修改监听
	window.commNOManage = function (index) {
		var NO = parseInt($(index).val());
		var commID = $(index).parents("tr").find(".commID").val();
		if (isNaN(NO)) {
			layer.msg("请输入正确的实盘数量");
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
	//删除某个盘点商品
	window.deleteComm = function (index) {
		var commID = $(index).parents("tr").find(".commID").val();
		popupCommDataManage("delete", commID);
		$(index).parents("tr").remove();
		var commTR = $("#rightSide .layui-table tbody tr");		//获取剩余的商品tr单元行
		for (var i = 0; i < commTR.length; i++) {
			$(commTR[i]).find("span").text(i + 1);
		}
		if (commTR.length <= 0) {
			inSheetCommTableRestPose();
		}
	}
	//头部导航选项
	$(".topNav .layui-form-label span").click(function () {
		var index = $(this);
		var area = index.offset();
		var areaParent = index.parent().offset();
		var thisUl = index.parent().next();
		thisUl.css({ "display": "block", "left": area.left - areaParent.left });
	})
	var shopIDBeChoosed = -1;
	var shopNameBeChoosed = "";
	//根据盘点单状态搜索盘点单
	$(".topNav .layui-form-label span").parent().next().find("li").click(function () {
		var indexObject = $(this);
		if (bDesertLastOperation) {
			queryInventorySheetByStatus(indexObject);
		} else {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
				layer.close(index);
				bDesertLastOperation = true;
				queryInventorySheetByStatus(indexObject);
			}, function (index, layero) {		//点击取消
				layer.close(index);
			})
		}
	})
	function queryInventorySheetByStatus (indexObject) {
		var index = indexObject.parents(".layui-inline").find(".layui-form-label span");
		var keydata = index.attr("data");
		index.parent().next().hide();
		index.find("lable").text(indexObject.text());
		if(indexObject.text() == "所有"){
			switch (keydata) {
			case "门店":
				inventorySheet_reloadTable.shopID = -1;
				break;
			case "状态":
				inventorySheet_reloadTable.status = -1;
				break;
			}
		}else{
			if(keydata == "门店"){
				inventorySheet_reloadTable.shopID = parseInt(indexObject.find("input").val());
			}
			
			if(keydata == "状态"){
				inventorySheet_reloadTable.status = parseInt(indexObject.find("input").val());
			}
		}
//		indexObject.parent().hide();
//		$(".topNav .layui-form-label span").find("b").text(indexObject.text());
		reloadTable(table, inventorySheetRN_tableID, method_post, inventorySheetRN_url, curr_reloadTable, inventorySheet_reloadTable);
	}
	//关闭头部导航选项区域
	$(document).click(function () {
		$(".topNav .layui-form-label + ul").hide();
	})
	$(document).delegate('.topNav .layui-form-label', 'click', function (event) {
		event.stopPropagation();
	})
	// 模糊搜索盘点单（关键字：商品名称、盘点单号）
	form.on('submit(inSheetSearch)', function (data) {
		if (bDesertLastOperation) {
			queryInventorySheetByKeyword(data);
		} else {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
				layer.close(index);
				bDesertLastOperation = true;
				queryInventorySheetByKeyword(data);
			}, function (index, layero) {		//点击取消
				layer.close(index);
			})
		}
		return false;
	})
	function queryInventorySheetByKeyword (data) {
		var queryKeyword = data.field.queryKeyword;
		if (queryKeyword.length <= 32) {
			var indexLoading = layer.load(1);
			var data_reloadTable = { "queryKeyword": queryKeyword };		//模糊搜索盘点单传参
			reloadTable(table, inventorySheetRN_tableID, method_post, inventorySheetRNB_url, curr_reloadTable, data_reloadTable);
		}
	}
	//初始渲染表格inventorySheetList
	table.render({
		elem: '#inventorySheetList',
		url: inventorySheetRN_url,
		id: inventorySheetRN_tableID,
		method: method_post,
		where: inventorySheet_reloadTable,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList',
		},
		skin: "nob",
		size: 'sm',
		even: true,
		limit: '10',
		limits: [10],
		page: true,
		cols: [[
			{ field: 'sn', title: '单号', width: 140, align: 'center', templet: '#inventorySheetOddNumbers', event: 'detail' },
			{ field: 'creatorName', title: '创建者', width: 80, align: 'center', event: 'detail' },
			{
				field: 'status', title: '状态', width: 80, align: 'center', event: 'detail',
				templet: function (data) {
					console.log(data);
					var status = data.status;
					if (status == 0) {
						return "待录入";
					} else if (status == 1) {
						return "已提交";
					} else if (status == 2) {
						return "已审核";
					}
				}
			},
			{
				field: 'warehouseName', title: '盘点仓库', width: 80, align: 'center', event: 'detail',
				templet: function (data) {
					var warehouseName = data.warehouse.name;
					return warehouseName;
				}
			},
			{
				field: 'createDatetime', title: '创建日期', width: 140, align: 'center', event: 'detail',
				templet: function (data) {
					var createDatetime = data.createDatetime;
					createDatetime = createDatetime.substring(0, createDatetime.length - 4);
					return createDatetime;
				}
			},
			{
				field: 'approveDatetime', title: '审核日期', width: 140, align: 'center', event: 'detail',
				templet: function (data) {
					var approveDatetime = data.approveDatetime;
					approveDatetime = approveDatetime.substring(0, approveDatetime.length - 4);
					return approveDatetime;
				}
			},
			{ field: 'approverName', title: '审核人', width: 100, align: 'center', event: 'detail' }
		]],
		done: function (res, curr, count) {
			if (res.ERROR != "EC_NoError") {
				var msg = res.msg == "" ? "查询盘点单失败" : res.msg;
				layer.msg(msg);
			} else {
				var inventorySheetList = res.objectList;
				if (inventorySheetList.length > 0) {
					if (isUpdateInventorySheetData) {
						for (var i = 0; i < inventorySheetList.length; i++) {
							if (inventorySheetList[i].ID == $(".inventorySheetID").val()) {
								$("#inventorySheetList + div .layui-table tbody tr").eq(i).addClass("trChoosed");
							}
						}
						isUpdateInventorySheetData = false;
					} else {
						if (bDesertLastOperation) {
							inventorySheetR1(inventorySheetList[0]);
							$("#inventorySheetList + div .layui-table tbody tr").eq(0).addClass("trChoosed");
						} else {
							layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
								layer.close(index);
								bDesertLastOperation = true;
								inventorySheetR1(inventorySheetList[0]);
								$("#inventorySheetList + div .layui-table tbody tr").eq(0).addClass("trChoosed");
							}, function (index, layero) {		//点击取消
								layer.close(index);
							})
						}
					}
				} else {
					noSuchInventorySheetData();
					layer.msg("查无盘点单");
				}
			}
			layer.closeAll('loading');
		}
	});
	//监听盘点单列表点击查看详情inventorySheetList
	table.on('tool(inventorySheetList)', function (obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var tr = obj.tr;
		switch (layEvent) {
			case 'detail':
				if (bDesertLastOperation) {
					inventorySheetR1(data);
					$(tr).addClass("trChoosed").siblings().removeClass("trChoosed");
				} else {
					layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
						layer.close(index);
						bDesertLastOperation = true;
						inventorySheetR1(data);
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
	//渲染商品表popupCommodityList
	table.render({
		elem: '#popupCommodityList',
		url: commRN_url + "?status=-1&type=0",		//type设为0是为了查询所有的普通商品
		id: popupCommRN_tableID,
		method: method_post,
		where: commodity_reloadTable,
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
				templet: function (data) {		//只显示后端传回来的listBarcodes里的第一个条形码
					var barcode = data.listBarcodes.length > 0 ? data.listBarcodes[0].barcode : "";
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
				field: 'latestPricePurchase', title: '最近采购价', width: 130, align: 'center',
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
			layer.closeAll('loading');
		}
	});
	//监听商品表格复选框选择popupCommodityList
	table.on('checkbox(popupCommodityList)', function (obj) {
		var checked = obj.checked;		//当前是否选中状态
		var data = obj.data;		//选中行的相关数据
		var type = obj.type;		//如果触发的是全选，则为：all，如果触发的是单选，则为：one
		var tr = obj.tr;		//选中行的DOM对象
		if (type == "one") {
			var commID = data.commodity.ID;
			var commName = data.commodity.name;
			var barcodeID = data.listBarcodes.length > 0 ? data.listBarcodes[0].ID : "";
			var barcodeName = data.listBarcodes.length > 0 ? data.listBarcodes[0].barcode : "";
			var packageUnitName = data.commodity.packageUnitName;
			var NO = 1;
			var specification = data.commodity.specification;
			for (var i = 0; i < popupCommodityData.length; i++) {		//检查是否已存在此商品(为表格切换页面时的bug而补充)
				if (popupCommodityData[i].commID == commID) {
					NO = popupCommodityData[i].NO;
				}
			}
			if (checked) {
				popupCommDataManage("add", commID, commName, barcodeID, barcodeName, "", packageUnitName, NO, "", specification);
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
					var barcodeID = data[i].listBarcodes.length > 0 ? data[i].listBarcodes[0].ID : "";
					var barcodeName = data[i].listBarcodes.length > 0 ? data[i].listBarcodes[0].barcode : "";
					var packageUnitName = data[i].commodity.packageUnitName;
					var NO = 1;
					var specification = data[i].commodity.specification;
					for (var j = 0; j < popupCommodityData.length; j++) {		//检查是否已存在此商品(为全选时数量不一致的bug而补充)
						if (popupCommodityData[j].commID == commID) {
							commodityNotExist = false;
						}
					}
					if (commodityNotExist) {
						popupCommDataManage("add", commID, commName, barcodeID, barcodeName, "", packageUnitName, NO, "", specification);
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
	// 模糊搜索商品（关键字：名称、简称、条形码、编号(现阶段没有编号字段)、助记码）
	form.on('submit(commoditySearch)', function (data) {
		var queryKeyword = data.field.queryKeyword;
		if (queryKeyword.length <= 64) {
			var indexLoading = layer.load(1);
			commodity_reloadTable.queryKeyword = queryKeyword;
			reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, commodity_reloadTable);
		}
		return false;
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
		var indexLoading = layer.load(1);
		var indexCategoryID = $(this).find("a").attr("indexID");
		commodity_reloadTable.categoryID = indexCategoryID;
		reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, commodity_reloadTable);
	})
	//确定选择商品（选择商品弹窗）
	$(".confirmChoosedComm").click(function () {
		$("#rightSide .layui-table tbody").html("");
		if (popupCommodityData.length > 0) {
			for (var i = 0; i < popupCommodityData.length; i++) {
				var serialNumber = i + 1;
				var commID = popupCommodityData[i].commID;
				var commName = popupCommodityData[i].commName;
				var barcode = popupCommodityData[i].barcodeName;
				var packageUnitName = popupCommodityData[i].packageUnitName;
				var noReal = popupCommodityData[i].NO;
				var specification = popupCommodityData[i].specification;
				inSheetCommTableRender(serialNumber, commID, commName, barcode, packageUnitName, specification, noReal);
				$("#rightSide .layui-table tbody tr:last").find("td").eq(1).css("padding-right", "20px");
			}
		} else {
			inSheetCommTableRestPose();
		}
		layer.closeAll("page");
	});
	//新建操作
	function inventorySheetToCreate () {
		clearInventorySheetForm();
		var sessionStaffID = $("#sessionStaffID").val();
		var sessionStaffName = $("#sessionStaffName").val();
		popupCommodityData = [];
		form.val("inventorySheetInfo", {
			"staffID": sessionStaffID,
			"creatorName": sessionStaffName,
			"warehouseID": "1",
			"shopID": "2",
			"scope": "0",
		})
		$(".changeInSheetStatus, .notShowWhenCreateInSheet").hide();
		$("#rightSide .layui-textarea").removeAttr("readOnly");
		$("#rightSide .layui-form-item select").find("option").removeAttr("disabled");
		form.render('select', 'inventorySheetInfo');
		$("#rightSide .layui-table thead tr th:last").hide();
		$("#rightSide .inventoryCommodityTable tbody").html("");
		inSheetCommTableRestPose();
		buttonStatusControl("toCreate");
		$("#inventorySheetList + div .layui-table tbody tr").removeClass("trChoosed layui-table-click");
	}
	//初始化盘点单搜索功能传参
	function initQueryCondition () {
		$(".topNav .layui-inline").find("b").text("所有");
		$("#leftSide .topArea input").val("PD");
		inventorySheet_reloadTable.status = -1;		//-1代表查询全部状态的盘点单
	}
	//点击右侧详情区顶部按钮
	form.on('submit(inventorySheetManage)', function (data) {
		var curr = $("#inventorySheetList + div .layui-laypage-em").next().text();		//当前表格停留的页码
		$(data.elem).addClass("btnChoosed").siblings().removeClass("btnChoosed");
		switch ($(data.elem).text()) {
			case "新建":
				if (bDesertLastOperation) {
					bDesertLastOperation = false;
					inventorySheetToCreate();		//点击新建按钮时执行的函数
				} else {
					layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
						layer.close(index);
						inventorySheetToCreate();		//点击新建按钮时执行的函数
					}, function (index, layero) {		//点击取消
						layer.close(index);
					})
				}
				break;
			case "保存":
				if (data.field.ID) {		//修改盘点单：待录入的盘点单可以修改盘点商品实盘数量和盘点总结,已提交的盘点单可以修改盘点总结		//
					var requestData = requestDataManage();
					delete requestData["barcodeIDs"];
					requestData.ID = data.field.ID;
					requestData.remark = data.field.remark;
					if (data.field.status == inventorySheetStatus[0]) {
						requestData.status = 0;
					} else {
						requestData.status = 1;
					}
					var text = '确认修改"盘点单' + data.field.sn + '"?';
					var failText = "修改盘点单失败";
					var succText = "修改盘点单成功";
					inventorySheetManage(inventorySheetUpdate_url, method_post, requestData, text, failText, succText, curr);
				} else {		//创建盘点单
					if (popupCommodityData.length > 0) {
						var requestData = requestDataManage();
						delete requestData["inventoryCommodityIDs"];
						requestData.staffID = data.field.staffID;
						requestData.warehouseID = data.field.warehouseID;
						requestData.scope = data.field.scope;
						requestData.remark = data.field.remark;
						requestData.shopID = data.field.shopID;
						var text = '是否创建该盘点单';
						var failText = "创建盘点单失败";
						var succText = "创建盘点单成功";
						inventorySheetManage(inventorySheetCreate_url, method_post, requestData, text, failText, succText, curr_reloadTable);
					} else {
						layer.msg("请选择盘点商品");
					}
				}
				break;
			case "提交":		//提交盘点单
				var text = '是否提交"盘点单' + data.field.sn + '"?';
				var failText = "提交盘点单失败";
				var succText = "提交盘点单成功";
				var requestData = requestDataManage();
				delete requestData["barcodeIDs"];
				requestData.ID = data.field.ID;
				requestData.remark = data.field.remark;
				inventorySheetManage(inventorySheetSubmit_url, method_post, requestData, text, failText, succText, curr);
				break;
			case "审核":		//审核盘点单
				var requestData = new Object();
				requestData.ID = data.field.ID;
				requestData.remark = data.field.remark;
				var text = '是否审核"盘点单' + data.field.sn + '"?';
				var failText = "审核盘点单失败";
				var succText = "审核盘点单成功";
				inventorySheetManage(inventorySheetApprove_url, method_post, requestData, text, failText, succText, curr);
				break;
			case "删除":		//删除盘点单
				var requestData = new Object();
				requestData.ID = data.field.ID;
				var text = '确认删除"盘点单' + data.field.sn + '"?';
				var failText = "删除盘点单失败";
				var succText = "删除盘点单成功";
				inventorySheetManage(inventorySheetDelete_url, method_get, requestData, text, failText, succText, curr_reloadTable);
				break;
			case "取消":
				if (bDesertLastOperation) {
					initQueryCondition();		//初始化模糊搜索盘点单传参
					reloadTable(table, inventorySheetRN_tableID, method_post, inventorySheetRN_url, curr_reloadTable, inventorySheet_reloadTable);
				} else {
					layer.confirm('确定要放弃之前的操作吗？', {
						icon: 3, title: '提示', cancel: function () {
							$("#rightSide .topArea button:eq(1)").addClass("btnChoosed");
							$("#rightSide .topArea button:eq(4)").removeClass("btnChoosed");
						}
					}, function (index, layero) {		//点击确定
						layer.close(index);
						bDesertLastOperation = true;
						initQueryCondition();		//初始化模糊搜索盘点单传参
						reloadTable(table, inventorySheetRN_tableID, method_post, inventorySheetRN_url, curr_reloadTable, inventorySheet_reloadTable);
					}, function (index, layero) {		//点击取消
						layer.close(index);
						$("#rightSide .topArea button:eq(1)").addClass("btnChoosed");
						$("#rightSide .topArea button:eq(4)").removeClass("btnChoosed");
					})
				}
				break;
			default:
				layer.msg("I dont't know what you want to do");
				break;
		}
	})
	//简单的按钮操作(请求后需要重载表格的操作)
	function inventorySheetManage (url, method, data, text, failText, succText, curr) {
		var iconNumber = 3;
		if (url == inventorySheetDelete_url) {
			iconNumber = 2;
		}
		layer.confirm(text, { icon: iconNumber, title: '提示' }, function (index) {
			var loading = layer.load(2);
			$.ajax({
				url: url,
				type: method,
				async: true,
				data: data,
				dataType: "JSON",
				success: function succFunction (data) {
					console.log(data);
					if (data.ERROR == "EC_NoError" || data.ERROR == "EC_PartSuccess") {		//错误码为EC_NoError或EC_PartSuccess，创建、修改、审核会存在部分成功的情况
						bDesertLastOperation = true;
						if (data.msg) {
							layer.msg(data.msg);
						} else {
							layer.msg(succText);
						}
						if (url == inventorySheetCreate_url || url == inventorySheetDelete_url) {		//调用了创建或删除接口
							initQueryCondition();		//初始化搜索功能传参
						} else if (url == inventorySheetUpdate_url) {		//调用了保存接口
							isUpdateInventorySheetData = true;
						} else if (url == inventorySheetSubmit_url || url == inventorySheetApprove_url) {		//调用了提交或审核接口
							isUpdateInventorySheetData = true;
							inventorySheetR1(data.object);
						} else {
							//其他接口不做特殊处理
						}
						reloadTable(table, inventorySheetRN_tableID, method_post, inventorySheetRN_url, curr, inventorySheet_reloadTable);
					} else {		//其他错误码
						if (data.msg) {		//如果后端有传msg则显示
							layer.msg(data.msg);
						} else {
							layer.msg(failText);
						}
					}
					layer.close(loading);
					layer.close(index);
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
					layer.close(loading);
				}
			})
		}, function (index) {
		});
	}
	//关闭选择商品弹窗
	$(".closePopupPage").click(function () {
		notRecordActions(tempArray, popupCommodityData);
		layer.closeAll('page');
	})
	//清空盘点单的详情（查询到无数据时）
	function noSuchInventorySheetData () {
		buttonStatusControl("noSuchInventorySheetData");
		clearInventorySheetForm();
		$("#rightSide .inventoryCommodityTable tbody").html("");
		$("#rightSide option").attr("disabled", "disabled");
		form.render("select", "inventorySheetInfo");
		$("#rightSide textarea").attr("readonly", "readonly");
	}
	//清空盘点单表单
	function clearInventorySheetForm () {
		form.val("inventorySheetInfo", {
			"ID": "",
			"sn": "",
			"status": "",
			"staffID": "",
			"creatorName": "",
			"createDatetime": "",
			"warehouseID": "",
			"scope": "",
			"approverName": "",
			"approveDatetime": "",
			"remark": ""
		})
	}
	//按钮状态控制
	function buttonStatusControl (keyword) {
		$(".topArea button").removeAttr("disabled");
		$(".topArea button").removeClass("unavailable");
		switch (keyword) {
			case "toCreate":		//点击新建按钮
				$(".topArea button").eq(1).addClass('btnChoosed').siblings().removeClass('btnChoosed');
				$(".topArea button").eq(3).attr("disabled", "disabled").addClass('unavailable');
				break;
			case "waitForInput":		//待录入
				$(".topArea button").eq(1).addClass('btnChoosed').siblings().removeClass('btnChoosed');
				break;
			case "submitted":		//已提交
				$(".topArea button").eq(1).addClass('btnChoosed').siblings().removeClass('btnChoosed');
				$(".topArea button").eq(3).attr("disabled", "disabled").addClass('unavailable');
				break;
			case "hadApproved":		//已审核
				$(".topArea button:gt(0)").attr("disabled", "disabled").addClass('unavailable');
				break;
			case "noSuchInventorySheetData":		//无数据
				$(".topArea button").eq(0).addClass('btnChoosed').siblings().removeClass('btnChoosed');
				$(".topArea button:gt(0)").attr("disabled", "disabled").addClass('unavailable');
				break;
		}
	}
	//处理商品弹窗选中的商品数据
	function requestDataManage () {
		var requestData = new Object();
		requestData.inventoryCommodityIDs = "";
		requestData.commListID = "";
		requestData.barcodeIDs = "";
		requestData.commNOReals = "";
		for (var i = 0; i < popupCommodityData.length; i++) {
			var inCommSheetID = popupCommodityData[i].price;		//盘点商品表ID存放于临时存储数据的price字段，因为本页面木有用到price，而原先定义的函数没有存放盘点商品表ID的字段
			var commID = popupCommodityData[i].commID;
			var barcodeID = popupCommodityData[i].barcodeID;
			var commNOReal = popupCommodityData[i].NO;
			requestData.inventoryCommodityIDs += inCommSheetID + ",";
			requestData.commListID += commID + ",";
			requestData.barcodeIDs += barcodeID + ",";
			requestData.commNOReals += commNOReal + ",";
		}
		return requestData;
	}
	//盘点单信息是否发生修改
	window.inventorySheetInfoHasUpdate = function () {
		bDesertLastOperation = false;
	}
});
