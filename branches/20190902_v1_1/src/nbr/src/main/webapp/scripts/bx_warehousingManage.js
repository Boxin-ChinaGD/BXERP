layui.use(['element', 'form', 'table', 'laypage'], function () {
	var element = layui.element;
	var form = layui.form;
	var table = layui.table;
	var laypage = layui.laypage;
	//定义函数内常量
	const warehousingRN_url = "warehousing/retrieveNEx.bx";		//入库单查询接口
	const warehousingRNB_url = "warehousing/retrieveNByFieldsEx.bx";		//模糊搜索入库单接口
	const warehousingR1_url = "warehousing/retrieve1Ex.bx";		//查询单个入库单接口
	const warehousingCreate_url = "warehousing/createEx.bx";		//创建入库单接口
	const warehousingUpdate_url = "warehousing/updateEx.bx";		//修改入库单接口
	const warehousingDelete_url = "warehousing/deleteEx.bx";		//删除入库单接口
	const warehousingApprove_url = 'warehousing/approveEx.bx';		//审核入库单接口
	const commRN_url = "commodity/retrieveNEx.bx";		//查询商品接口
	const providerRN_url = "provider/retrieveNEx.bx";		//查询供应商接口
	const providerRNByFields_url = 'provider/retrieveNByFieldsEx.bx';		//模糊查询供应商
	const shopRNByFields_url = 'shop/retrieveNByFieldsEx.bx';		//模糊查询门店
	const shopRN_url = "shop/retrieveNEx.bx";		//查询门店接口
	const purchasingOrderRN_url = "/purchasingOrder/retrieveN.bx";		//采购订单RN接口
	const commRN_tableID = "commodityList";		//弹出商品表表格ID
	const providerRN_tableID = "popupProviderList";		//供应商表格ID（弹窗）
	const shopRN_tableID = "popupShopList";		//门店表格ID（弹窗）
	const method_get = "GET";		//get请求方式
	const method_post = "POST";		//post请求方式
	const warehousing_reloadTable = {};		//查询入库单的传参
	const commodity_reloadTable = { "type": 0, "status": 0 };		//查询商品的传参
	const provider_reloadTable = {};		//查询供应商的传参
	const shop_reloadTable = {};		//查询门店的传参

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
		console.log(windowWidth);
		if (windowWidth >= 1060) {
			popupPageWidth = "1000px";
		} else {
			popupPageWidth = windowWidth - 60 + "px";
		}
	}

	//初始加载页面
	layer.ready(function () {
		needCalculatedAmount = true;
		warehousingListRN(warehousing_reloadTable, warehousingRN_url, method_get);		//初始加载表格
	});
	//获取入库单信息（左侧列表）
	var showFirstDataOfTable = true;		//是否显示第一条数据
	function warehousingListRN (requestData, url, type) {
		var indexLoading = layer.load(1);
		console.log(requestData);
		$.ajax({
			url: url,
			type: type,
			async: true,
			dataType: "json",
			data: requestData,
			success: function succFunction (data) {
				console.log(data);
				layer.close(indexLoading);
				if (!data) {
					layer.msg("服务器错误");
					return;
				}
				if (data.ERROR != "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg('查询入库单列表失败');
					}
					return;
				}
				$("#warehousingList").html("");
				var warehousingSheetCount = data.count;		//入库单总数
				if (warehousingSheetCount <= 0) {
					indexWarehousingSheetID = -1;
					noWarehousingData();
				} else {
					var date = "";
					var colorClass = "";
					var statusText = "";
					for (var i = 0; i < data.warehousingList.length; i++) {
						date = data.warehousingList[i].createDatetime.substring(0, 10);
						if (data.warehousingList[i].status == 0) {
							colorClass = "circle_notApprove";
							statusText = "未审核";
						} else {
							colorClass = "circle_hadApproved";
							statusText = "已审核";
						}
						$("#warehousingList").append(
							'<li>' +
							'<span class="icons ' + colorClass + '"></span><span class="statusText">' + statusText + '</span><span class="date">' + date + '</span><br>' +
							'<span class="provider" title="' + data.warehousingList[i].provider.name + '">' + data.warehousingList[i].provider.name + '</span><span class="creatorName">' + data.warehousingList[i].creatorName + '</span>' +
							'<input type="hidden" class="ID" value="' + data.warehousingList[i].ID + '"/>' +
							'<input type="hidden" class="status" value="' + data.warehousingList[i].status + '"/>' +
							'</li>'
						)
					}
					if (showFirstDataOfTable) {		//模拟单击事件，显示第一条入库信息的商品
						$("#warehousingList li").eq(0).click();
						showFirstDataOfTable = false;
					}
					warehousingSheetBeChoosedToAddClass();
					warehousingPaginationRender(warehousingSheetCount, requestData, url, type);		//设置页码的方法
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(indexLoading);
			}
		})
	}
	//添加入库单选中样式
	function warehousingSheetBeChoosedToAddClass () {
		var warehousingSheetID = $(".warehousingSheetInfo .warehousingSheetID").val();
		$("#warehousingList li").each(function (i) {
			if ($(this).find(".ID").eq(i).val() == warehousingSheetID) {
				$(this).css("background-color", "#f2f2f2").siblings().css("background-color", "#fff");		//给已点击的入库单添加样式
			}
		});
	}
	//设置页码
	function warehousingPaginationRender (count, requestData, url, method) {
		laypage.render({
			elem: 'warehousingListPage',		//注意，这里的 test1 是 ID，不用加 # 号
			count: count,		//数据总数，从服务端得到
			next: ">",
			prev: "<",
			hash: "indexCurr",
			groups: 3,
			curr: requestData.pageIndex,
			jump: function (obj, first) {
				if (!first) {		//首次不执行
					if (bDesertLastOperation) {
						layer.confirm("确定要放弃之前的操作吗？", {
							icon: 3, title: '提示', cancel: function () {
								warehousingPaginationRender(count, requestData, url, method);
							}
						}, function (index) {
							bDesertLastOperation = false;		//恢复默认值
							confirmTurningPage(obj.curr, requestData, url, method);
							layer.close(index);			//关闭当前提示框
						}, function () {
							warehousingPaginationRender(count, requestData, url, method);
						})
					} else {
						confirmTurningPage(obj.curr, requestData, url, method);
					}
				}
				$("#warehousingListPage").show();
			}
		});
	}
	//翻页(显示新一页的数据)
	function confirmTurningPage (curr, requestData, url, method) {
		showFirstDataOfTable = true;
		requestData.pageIndex = curr;
		warehousingListRN(requestData, url, method);
	}
	//即时模糊搜索事件
	window.instantSearch = function (index) {
		$(index).siblings("i").click();
	}
	//表单数据验证
	fieldFormat(form);
	//头部导航栏的显示与隐藏
	$(".warehousingManageTop label span").click(function () {
		window.event.stopPropagation();		//阻止冒泡事件
		if ($(this).parent("label").next().is(":hidden")) {
			$(this).parent("label").next().show();
			var top = $(this).offset().top + 18;
			var left = $(this).offset().left - 8;
			$(this).parent("label").next().css({ "top": top, "left": left });
		} else {
			$(this).parent("label").next().hide();
		}
		$(this).parent("label").next().siblings("ul").hide();
	});
	//头部导航栏搜索入库单事件
	var bDesertLastOperation = false;		//存储是否出现"确定要放弃之前的操作吗？"提示框
	var wareHousingFuzzySearchData = new Object();		//模糊搜索入库单传参
	wareHousingFuzzySearchData.status = -1;
	$(".warehousingManageTop ul li").click(function () {
		var indexObject = $(this);
		if (bDesertLastOperation) {
			layer.confirm("确定要放弃之前的操作吗？", { icon: 3, title: '提示' }, function (index) {
				bDesertLastOperation = false;		//恢复默认值
				layer.close(index);			//关闭当前提示框
				queryEvents(indexObject);
			})
		} else {
			queryEvents(indexObject);
		}
	});
	function queryEvents (indexObject) {		//头部导航栏查询功能
		indexObject.parent().prev().children().text(indexObject.text() + " v");
		var keyClass = indexObject.parent().attr("class");
		var keyword = indexObject.attr("data-keyword");
		showFirstDataOfTable = true;
		if (keyClass == "UlStatus") {		//根据状态查询入库单
			wareHousingFuzzySearchData.status = keyword;
		} else if (keyClass == "UlStaffID") {		//根据经办人查询入库单
			wareHousingFuzzySearchData.staffID = keyword;
		} else if (keyClass == "UlProviderID") {		//根据供应商查询入库单
			wareHousingFuzzySearchData.providerID = keyword;
		}else if (keyClass == "UlShopID") {		//根据供应商查询入库单
			wareHousingFuzzySearchData.shopID = keyword;
		} 
		else if (keyClass == "UlDate") {		//根据创建日期查询入库单
			var date = new Date();
			var date1 = "", date2 = new Date(date.getTime()).format("yyyy/MM/dd hh:mm:ss");
			if (keyword == "all") {		//全部时间
				date1 = "1970/1/1 00:00:00";
			} else if (keyword == "aWeekAgo") {		//1代表一周前
				var oneWeekAgo = new Date(date.getTime() - 7 * 24 * 60 * 60 * 1000);
				date1 = oneWeekAgo.format("yyyy/MM/dd") + " 00:00:00";
			} else if (keyword == "aMonthAgo") {		//2代表一个月前
				var oneMonthAgo = new Date(date.getTime() - 30 * 24 * 60 * 60 * 1000);
				date1 = oneMonthAgo.format("yyyy/MM/dd") + " 00:00:00";
			} else if (keyword == "threeMonthsAgo") {		//3代表三个月前
				var threeMonthsAgo = new Date(date.getTime() - 90 * 24 * 60 * 60 * 1000);
				date1 = threeMonthsAgo.format("yyyy/MM/dd") + " 00:00:00";
			} else {
				console.log("其他时间段：" + keyword);
			}
			wareHousingFuzzySearchData.date1 = date1;
			wareHousingFuzzySearchData.date2 = date2;
		} else if (keyClass == "UlWarehouse") {
			layer.msg("功能尚未开发，敬请期待");
			return;
		}
		wareHousingFuzzySearchData.pageIndex = 1;
		warehousingListRN(wareHousingFuzzySearchData, warehousingRNB_url, method_post);
	}
	$("body").click(function () {
		$(".warehousingManageTop ul").hide();
	});
	//模糊搜索（单号、商品名称、供应商名称、采购订单号）
	$(".queryWarehousingSheet").click(function () {
		queryWarehousingSheetPromptBox(true);
	})
	$(".queryWarehousingSheet").siblings("input").keyup(function (event) {
		if (event.keyCode == "13" && event.key == "Enter") {
			queryWarehousingSheetPromptBox(true);
		} else {
			queryWarehousingSheetPromptBox(false);
		}
	})
	//“确定要放弃之前的操作吗？”提示框--模糊搜索功能
	function queryWarehousingSheetPromptBox (showMsg) {
		if (bDesertLastOperation) {
			layer.confirm("确定要放弃之前的操作吗？", { icon: 3, title: '提示' }, function (index) {
				layer.close(index);		//关闭当前提示框
				bDesertLastOperation = false;			//恢复默认值
				if (showMsg) {		//输入回车键
					queryWarehousingSheet(showMsg);
				} else {
					clearDataOrRecoveryData();
				}
			})
		} else {
			queryWarehousingSheet(showMsg);
		}
	}
	//实现模糊搜索功能
	function queryWarehousingSheet (showMsg) {
		var queryKeyword = $(".queryWarehousingSheet").siblings("input").val();
		showFirstDataOfTable = true;
		if (queryKeyword.length <= 32) {		//检查输入的关键字长度
			if (queryKeyword.length == $(".queryWarehousingSheet").siblings("input").val().trim().length) {		//检查输入的关键字首尾是否有空格
				if (/^$|^[\u2014\u4E00-\u9FA5A-Za-z0-9_\()（）-\s]{1,32}$/.test(queryKeyword)) {		//检查输入的关键字是否满足数据格式要求
					if (queryKeyword.indexOf("RK") != -1 || queryKeyword.indexOf("R") != -1 || queryKeyword.indexOf("K") != -1 || !isNaN(queryKeyword)
						|| queryKeyword.indexOf("CG") != -1 || queryKeyword.indexOf("C") != -1 || queryKeyword.indexOf("G") != -1) {		//检查是否存在入库单号和采购订单号的关键字眼或者是否为纯数字
						if (queryKeyword.length >= 10 || queryKeyword.length == 0) {
							wareHousingFuzzySearchData.queryKeyword = queryKeyword;
						} else {
							if (showMsg) {
								clearDataOrRecoveryData();
								layer.msg("数据格式不正确，入库单号或者采购订单号至少需要输入10位", { id: "queryMsg" });
							}
							return;
						}
					} else {
						wareHousingFuzzySearchData.queryKeyword = queryKeyword;
					}
					wareHousingFuzzySearchData.pageIndex = 1;
					warehousingListRN(wareHousingFuzzySearchData, warehousingRNB_url, method_post);
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
	//清空新建的数据/恢复入库单的数据--当模糊搜索值不符合查询需求时调用
	function clearDataOrRecoveryData () {
		var warehousingSheetID = $(".warehousingSheetInfo .warehousingSheetID").val();		//入库单ID
		var warehousingSheetStatus = $(".amountTotalAndStatus .warehousingManage").val();
		if (warehousingSheetID > 0) {		//上一步为查看入库单
			warehousingSheetR1(warehousingSheetID, warehousingSheetStatus === "审核" ? 0 : 1);		//刷新数据--用户修改了入库单数据，但模糊搜索值又不符合查询要求
		} else {		//上一步为新建入库单
			popupCommodityData = [];		//情况商品弹出层的数据
			toCreateWarehousingSheet();		//恢复为新建状态
		}
	}
	//点击入库单查看入库单详情
	$("#warehousingList").delegate("li", "click", function () {
		var indexObject = $(this);		//记录当前用户所点击li，即用户所点击入库单
		if (bDesertLastOperation) {
			layer.confirm("确定要放弃之前的操作吗？", { icon: 3, title: '提示' }, function (index) {
				layer.close(index);		//关闭当前提示框
				bDesertLastOperation = false;			//查看新的一个入库单后，恢复默认值
				warehousingSheetToR1(indexObject)
			})
		} else {
			warehousingSheetToR1(indexObject);
		}
	})
	function warehousingSheetToR1 (indexObject) {
		var warehousingSheetID = indexObject.children("input.ID").val();		//入库单ID
		var warehousingSheetStatus = indexObject.find(".status").val();
		indexObject.css("background-color", "#f2f2f2").siblings().css("background-color", "#fff");		//给已点击的入库单添加样式
		warehousingSheetR1(warehousingSheetID, warehousingSheetStatus);
	}
	//查询单个入库单
	var indexWarehousingSheetID = -1;		//储存用户点击入库列表后的入库单ID(页面的“取消”按钮)
	var indexWarehousingSheetStatus = -1;		//储存用户最新查看的入库单的状态（页面“取消”按钮所使用）
	var initialData = [];		//存储入库单商品初始数据
	function warehousingSheetR1 (warehousingSheetID, warehousingSheetStatus) {
		var loading = layer.load(1);
		indexWarehousingSheetID = warehousingSheetID;
		$.ajax({
			url: warehousingR1_url,
			type: method_get,
			async: true,
			dataType: "json",
			data: { "ID": warehousingSheetID, "status": warehousingSheetStatus },
			success: function succFunction (data) {
				console.log(data);
				layer.close(loading);
				if (!data) {
					layer.msg("服务器错误");
					return;
				}
				if (data.ERROR != "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg('查询入库单详情失败');
					}
					if (data.object && data.object.listSlave1.length <= 0) {
						showWarehousingDetails(data);
					}
					return;
				}
				showWarehousingDetails(data);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(loading);
			}
		})
	}
	//查询入库单后的显示相关信息
	function showWarehousingDetails (data) {
		var warehousingSheetID = data.object.ID;
		var warehousingSheetSN = data.object.sn;
		var warehousingSheetProviderID = data.object.providerID;
		var warehousingSheetProviderName = data.object.provider.name;
		var warehousingSheetShopID = data.object.ShopID;
		var warehousingSheetShopName = data.object.shop.name;
		var warehousingSheetCreateDatetime = data.object.createDatetime.substring(0, 10);
		indexWarehousingSheetID = warehousingSheetID;
		providerIDBeChoosed = warehousingSheetProviderID;
		providerNameBeChoosed = warehousingSheetProviderName;
		shopIDBeChoosed = warehousingSheetShopID;
		shopNameBeChoosed = warehousingSheetShopName;
		warehousingSheetDataChange = false;		//恢复默认值(储存入库单是否有修改)
		$(".warehousingSheetInfo .warehousingSheetID").val(warehousingSheetID);
		$(".warehousingSheetInfo .warehousingSheetSN").text(warehousingSheetSN).parent().show();
		$(".warehousingSheetInfo .providerID").val(warehousingSheetProviderID);
		$(".warehousingSheetInfo .providerName").attr("title", warehousingSheetProviderName).text(warehousingSheetProviderName).show();
		$(".warehousingSheetInfo .shopID").val(warehousingSheetShopID);
		$(".warehousingSheetInfo .shopName").attr("title", warehousingSheetShopName).text(warehousingSheetShopName).show();
		$(".warehousingSheetInfo .createDatetime").text(warehousingSheetCreateDatetime);
		$(".warehousingSheetInfo .amountTotalAndStatus .createStaff").text("创建人：" + data.object.staffName);
		var warehousingCommodityList = data.object.listSlave1;
		if (warehousingCommodityList.length > 0) {		//渲染入库商品
			var totalAmount = 0;		//计算入库商品总价
			popupCommodityData = [];
			$(".warehousingCommodityList tbody").html("");
			for (var i = 0; i < warehousingCommodityList.length; i++) {
				var serialNum = i + 1;
				var warehousingCommodity = warehousingCommodityList[i];
				totalAmount += warehousingCommodity.amount;		//入库单总价的计算
				popupCommDataManage("add", warehousingCommodity.commodityID, warehousingCommodity.commodityName,
					warehousingCommodity.barcodeID, warehousingCommodity.barcode, warehousingCommodity.packageUnitID,
					warehousingCommodity.packageUnitName, warehousingCommodity.NO, warehousingCommodity.price, warehousingCommodity.amount);
				warehousingCommTableRender(serialNum, warehousingCommodity);
			}
			$(".amountTotalAndStatus strong").text(totalAmount.toFixed(2)).show();
		} else {
			//			layer.msg('所查看的入库单没有入库商品');
			if (data.object.status == "0") {
				warehousingCommTableInitialize();
			} else {
				$(".warehousingCommodityList tbody").html("");
			}
		}
		//通过入库单的状态和与采购订单有无关联进行页面调整
		var warehousingSheetStatus = data.object.status;
		indexWarehousingSheetStatus = warehousingSheetStatus;
		if (warehousingSheetStatus == 0) {		//未审核的入库单
			var purchasingOrderSN = data.object.purchasingOrderSN;
			if (purchasingOrderSN == "") {		//与采购订单没有关联
				$(".warehousingSheetInfo .addProvider").show();
				$(".warehousingSheetInfo .addShop").show();
				$(".warehousingSheetInfo .purchasingOrderSN").text("");
				$(".warehousingSheetInfo .purchasingOrderSN").parent().hide();
				$(".warehousingCommodityList tbody tr").find("td:nth-child(3)").css("padding-right", "30px");
			} else {		//与采购订单有关联
				$(".warehousingSheetInfo .addProvider").hide();
				$(".warehousingSheetInfo .addShop").hide();
				$(".warehousingSheetInfo .purchasingOrderSN").text(purchasingOrderSN).parent().show();
				$(".addWarehousingCommodity, .deleteWarehousingCommodity").remove();
				$(".warehousingCommodityList tbody tr").each(function () {
					var serialNum = $(this).find("td:nth-child(1) span").text();
					$(this).find("td").eq(0).text(serialNum);
				})
			}
			buttonAreaManage("R1_notApprove");
			$(".warehousingSheetInfo span:first b:eq(1)").text("未审核入库单");
			$(".amountTotalAndStatus button").removeClass("hadApproveButton layui-btn-disabled").text("审核").show();
		} else if (warehousingSheetStatus == 1) {		//已审核的入库单
			var purchasingOrderSN = data.object.purchasingOrderSN;
			if (purchasingOrderSN == "") {		//与采购订单没有关联
				$(".warehousingSheetInfo .purchasingOrderSN").parent().hide();
			} else {		//与采购订单有关联
				$(".warehousingSheetInfo .purchasingOrderSN").text(purchasingOrderSN).parent().show();
			}
			$(".warehousingSheetInfo .addProvider").hide();
			$(".warehousingSheetInfo .addShop").hide();
			$(".addWarehousingCommodity, .deleteWarehousingCommodity").remove();
			$(".warehousingCommodityList tbody tr").each(function () {
				var serialNum = $(this).find("td:nth-child(1) span").text();
				var warehousingCommodityNO = $(this).find("td:nth-child(5) input").val();
				var warehousingCommodityPrice = $(this).find("td:nth-child(6) input").val();
				var warehousingCommodityAmount = $(this).find("td:nth-child(7) input").val();
				$(this).find("td").eq(0).text(serialNum);
				$(this).find("td").eq(4).text(warehousingCommodityNO);
				$(this).find("td").eq(5).text(warehousingCommodityPrice);
				$(this).find("td").eq(6).text(warehousingCommodityAmount);
			})
			buttonAreaManage("R1_hadApprove");
			$(".warehousingSheetInfo span:first b:eq(1)").text("已审核入库单");
			$(".amountTotalAndStatus button").addClass("layui-btn-disabled hadApproveButton").text("已审核").show();
		} else { }		//其他状态
	}
	//渲染入库商品表格
	function warehousingCommTableRender (serialNum, warehousingCommodity) {
		$(".warehousingCommodityList tbody").append(
			'<tr>' +
			'<td><span>' + serialNum + '</span><i class="layui-icon layui-icon-close-fill deleteWarehousingCommodity" title="删除商品" onclick="deleteWarehousingCommodity(this)"></i></td>' +
			'<td title="' + warehousingCommodity.barcode + '">' + warehousingCommodity.barcode + '</td>' +
			'<td title="' + warehousingCommodity.commodityName + '">' +
			'<input type="hidden" class="commodityID" value="' + warehousingCommodity.commodityID + '"/>' + warehousingCommodity.commodityName +
			'<i class="layui-icon layui-icon-add-circle addWarehousingCommodity" title="添加商品" onclick="toChooseCommodity()"></i>' +
			'</td>' +
			'<td title="' + warehousingCommodity.packageUnitName + '">' + warehousingCommodity.packageUnitName + '</td>' +
			'<td title="' + warehousingCommodity.NO + '"><input onchange="commNOManage(this)" value="' + warehousingCommodity.NO + '"/></td>' +
			'<td title="' + parseFloat(warehousingCommodity.price).toFixed(2) + '"><input onchange="commPriceManage(this)" value="' + parseFloat(warehousingCommodity.price).toFixed(2) + '"/></td>' +
			'<td title="' + parseFloat(warehousingCommodity.amount).toFixed(2) + '"><input onchange="commAmountManage(this)" value="' + parseFloat(warehousingCommodity.amount).toFixed(2) + '"/></td>' +
			'</tr>'
		);
	}
	//初始化入库商品表格
	function warehousingCommTableInitialize () {
		$(".warehousingCommodityList tbody").html("");
		$(".warehousingCommodityList tbody").append(
			'<tr>' +
			'<td>1</td>' +
			'<td></td>' +
			'<td><i class="layui-icon layui-icon-add-circle addWarehousingCommodity" title="添加商品" onclick="toChooseCommodity()"></i></td>' +
			'<td></td>' +
			'<td></td>' +
			'<td></td>' +
			'<td></td>' +
			'</tr>'
		);
	}
	//计算入库单总价
	function totalAmountCalculation () {
		var totalAmount = 0;
		$(".warehousingCommodityList tbody tr").each(function () {		//计算合计（金额）
			totalAmount += parseFloat($(this).find("td:last input").val());
		});
		$(".amountTotalAndStatus strong").text(totalAmount.toFixed(2));
	}
	//某个入库商品的数量修改监听
	window.commNOManage = function (index) {
		var NO = parseInt($(index).val());
		var commodityID = $(index).parents("tr").find(".commodityID").val();
		if (isNaN(NO) || NO <= 0 || NO > (Math.abs(2 << 30) - 1)) {
			layer.msg("请输入正确的入库数量");
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commodityID) {
					NO = popupCommodityData[i].NO;
				}
			}
		} else {
			var price = parseFloat($(index).parent().next().find("input").val());
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commodityID) {
					popupCommodityData[i].NO = NO;
					popupCommodityData[i].specification = (NO * price).toFixed(2);		//由于原先写函数时没有考虑多的字段，故暂用规格字段存放数据
				}
			}
			console.log(popupCommodityData);
			$(index).parents("tr").find("td:last input").val((NO * price).toFixed(2));
			totalAmountCalculation();
		}
		buttonAreaManage("hadChange");
		$(index).val(NO);
	}
	//某个入库商品的入库价修改监听
	window.commPriceManage = function (index) {
		var price = parseFloat($(index).val()).toFixed(2);
		var commodityID = $(index).parents("tr").find(".commodityID").val();
		if (isNaN(price) || price < 0 || price > (Math.abs(2 << 30) - 1)) {
			layer.msg("请输入正确的入库价");
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commodityID) {
					price = popupCommodityData[i].price;
				}
			}
		} else {
			var NO = parseInt($(index).parent().prev().find("input").val());
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commodityID) {
					popupCommodityData[i].price = price;
					popupCommodityData[i].specification = (NO * price).toFixed(2);		//由于原先写函数时没有考虑多的字段，故暂用规格字段存放数据
				}
			}
			console.log(popupCommodityData);
			$(index).parents("tr").find("td:last input").val((NO * price).toFixed(2));
			totalAmountCalculation();
		}
		buttonAreaManage("hadChange");
		$(index).val(price);
	}
	//某个入库商品的入库金额修改监听
	window.commAmountManage = function (index) {
		var amount = parseFloat($(index).val()).toFixed(2);
		var commodityID = $(index).parents("tr").find(".commodityID").val();
		if (isNaN(amount) || amount < 0 || amount > (Math.abs(2 << 30) - 1)) {
			layer.msg("请输入正确的入库金额");
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commodityID) {
					amount = popupCommodityData[i].specification;		//由于原先写函数时没有考虑多的字段，故暂用规格字段存放数据
				}
			}
		} else {
			var NO = parseInt($(index).parent().prev().prev().find("input").val());
			for (var i = 0; i < popupCommodityData.length; i++) {
				if (popupCommodityData[i].commID == commodityID) {
					popupCommodityData[i].price = (amount / NO).toFixed(2);
					popupCommodityData[i].specification = amount;		//由于原先写函数时没有考虑多的字段，故暂用规格字段存放数据
				}
			}
			console.log(popupCommodityData);
			$(index).parent().prev().find("input").val((amount / NO).toFixed(2));
			totalAmountCalculation();
		}
		buttonAreaManage("hadChange");
		$(index).val(amount);
	}
	//按钮区域的切换
	var warehousingSheetDataChange = false;
	function buttonAreaManage (key) {
		switch (key) {
			case "hadChange":
				warehousingSheetDataChange = true;
				bDesertLastOperation = true;
				$(".warehousingSheetInfo .buttonArea button").eq(1).addClass("btnChoosed").siblings().removeClass("btnChoosed");
				break;
			case "R1_notApprove":
				$(".buttonArea button:eq(1)").addClass("btnChoosed").removeAttr("disabled").removeClass("disabledButton")
					.siblings().removeClass("btnChoosed").removeAttr("disabled").removeClass("disabledButton");
				break;
			case "R1_hadApprove":
				$(".buttonArea button:eq(0)").removeAttr("disabled").removeClass("btnChoosed disabledButton")
					.siblings().removeClass("btnChoosed").attr("disabled", "disabled").addClass("disabledButton");
				break;
			case "create":
				$(".buttonArea button:eq(1)").addClass("btnChoosed").removeAttr("disabled").removeClass("disabledButton")
					.siblings().removeClass("btnChoosed");
				$(".buttonArea button:eq(2)").addClass("disabledButton").attr("disabled", "disabled");
				$(".buttonArea button:eq(3)").removeClass("disabledButton").removeAttr("disabled");
				break;
			case "noWarehousingData":
				$(".amountTotalAndStatus button").addClass("layui-btn-disabled hadApproveButton").text("").hide();
				$(".buttonArea button:eq(0)").addClass("btnChoosed").removeAttr("disabled").removeClass("disabledButton")
					.siblings().removeClass("btnChoosed").attr("disabled", "disabled").addClass("disabledButton");
				break;
			case "promptBoxOfCancelButton":
				$(".buttonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
				break;
			default:
				break;
		}
	}
	//详情区头部按钮监听
	$(".warehousingManage").click(function () {
		var keyWord = $(this).text();
		$(this).addClass("btnChoosed").siblings().removeClass("btnChoosed");
		switch (keyWord) {
			case "新建":
				if (bDesertLastOperation) {
					layer.confirm("确定要放弃之前的操作吗？", {
						icon: 3, title: '提示', cancel: function () {
							buttonAreaManage("promptBoxOfCancelButton");
						}
					}, function (index) {
						toCreateWarehousingSheet();
						layer.close(index);
					}, function () {
						buttonAreaManage("promptBoxOfCancelButton");
					})
				} else {
					toCreateWarehousingSheet();
					bDesertLastOperation = true;
				}
				break;
			case "保存":
				var warehousingSheetID = $(".warehousingSheetInfo .warehousingSheetID").val();
				if (warehousingSheetID == "") {		//不存在入库单时
					layer.msg('没有选择入库单，不可进行修改操作');
					return;
				}
				if ($(".amountTotalAndStatus button").text() == "已审核") {
					layer.msg('已审核的入库单不能进行操作');
					return;
				}
				if (popupCommodityData.length <= 0) {
					layer.msg('请选择入库商品');
				} else {
					for (var i = 0; i < popupCommodityData.length; i++) {
						if (!popupCommodityData[i].barcodeID) {
							layer.msg('不能选择没有条形码的商品进行入库');
							return;
						}
					}
					var warehousingSheetID = $(".warehousingSheetInfo .warehousingSheetID").val();
					if (warehousingSheetID == "0") {		//创建入库单
						var text = "确认创建入库单？";
						var failText = "创建入库单失败";
						var succText = "创建入库单成功";
						var requestData = requestDataManage();
						showFirstDataOfTable = true;
						warehousingSheetManage(text, warehousingCreate_url, method_post, requestData, failText, succText);
					} else {		//修改入库单
						if (!warehousingSheetDataChange) {
							layer.msg('修改信息后再保存');
							return;
						}
						var warehousingSheetSN = $(".warehousingSheetInfo .warehousingSheetSN").text();
						var text = "确认修改入库单" + warehousingSheetSN + "？";
						var failText = "修改入库单失败";
						var succText = "修改入库单成功";
						var requestData = requestDataManage();
						requestData.ID = warehousingSheetID;
						warehousingSheetManage(text, warehousingUpdate_url, method_post, requestData, failText, succText);
					}
				}
				break;
			case "审核":
				var warehousingSheetID = $(".warehousingSheetInfo .warehousingSheetID").val();
				var warehousingSheetSN = $(".warehousingSheetInfo .warehousingSheetSN").text();
				var purchasingOrderSN = $(".warehousingSheetInfo .purchasingOrderSN").text();
				var text;
				if (purchasingOrderSN.length > 0) {
					text = "确认审核入库单" + warehousingSheetSN + "？";
				} else {
					text = "入库单" + warehousingSheetSN + "没有对应的采购订单，是否继续审核？";
				}
				var failText = "审核入库单失败";
				var succText = "审核入库单成功";
				if (warehousingSheetID == "") {		//不存在入库单时
					layer.msg('没有选择入库单，不可进行审核操作');
					return;
				}
				console.log("是否修改过→" + warehousingSheetDataChange);
				if (warehousingSheetDataChange) {		//审核前有修改过
					var requestData = requestDataManage();
					requestData.ID = warehousingSheetID;
					requestData.isModified = 1;
				} else {		//审核前没有修改过
					var requestData = { "ID": warehousingSheetID, "isModified": 0 };
				}
				console.log(requestData);
				warehousingSheetManage(text, warehousingApprove_url, method_post, requestData, failText, succText);
				break;
			case "删除":
				var warehousingSheetID = $(".warehousingSheetInfo .warehousingSheetID").val();
				if (warehousingSheetID == "") {		//不存在入库单时
					layer.msg('没有选择入库单，不可进行删除操作');
					return;
				}
				if ($(".amountTotalAndStatus button").text() == "已审核") {
					layer.msg('已审核的入库单不能进行操作');
					return;
				}
				var warehousingSheetID = $(".warehousingSheetInfo .warehousingSheetID").val();
				if (warehousingSheetID == "0") {		//创建入库单
					layer.msg('没有选择入库单，不可进行删除操作');
					return;
				}
				var warehousingSheetSN = $(".warehousingSheetInfo .warehousingSheetSN").text();
				layer.confirm("确定要删除入库单" + warehousingSheetSN + "吗？", { icon: 2, title: '提示' }, function (index) {
					var warehousingSheetID = $(".warehousingSheetInfo .warehousingSheetID").val();
					layer.close(index);
					$.ajax({
						url: warehousingDelete_url,
						type: 'GET',
						async: true,
						dataType: "json",
						data: { "ID": warehousingSheetID },
						success: function succFunction (data) {
							if (!data) {
								layer.msg("服务器错误");
								return;
							}
							if (data.ERROR != "EC_NoError") {
								if (data.msg) {
									layer.msg(data.msg);
								} else {
									layer.msg("删除入库单失败");
								}
								return;
							}
							layer.msg("删除入库单成功");
							bDesertLastOperation = false;		//删除成功后，恢复其默认值，即无需提示框
							initQueryCondition();		//删除成功后，查询条件恢复为默认状态，存储查询的数据模糊为默认值
							queryAllWarehousingSheet();		//返回所有的数据，并选择第一页第一条数据
						}, error: function (XMLHttpRequest, textStatus, errorThrown) {
							layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
						}
					});
				});
				break;
			case "取消":
				if (bDesertLastOperation) {
					layer.confirm("确定要放弃之前的操作吗？", {
						icon: 3, title: '提示', cancel: function () {
							buttonAreaManage("promptBoxOfCancelButton");
						}
					}, function (index) {
						bDesertLastOperation = false;			//恢复默认值(确定取消后，会默认选中第一页的第一条数据，即查看了一个新的入库单)
						initQueryCondition();
						queryAllWarehousingSheet();
						layer.close(index);
					}, function () {
						buttonAreaManage("promptBoxOfCancelButton");		//点击提示框的取消按钮后，默认选中保存按钮
					})
				} else {
					initQueryCondition();
					queryAllWarehousingSheet();
				}
				break;
			default:		//其他情况
				break;
		}
	})
	//初始化搜索入库单传参（页面层）
	function initQueryCondition () { //恢复初始导航栏状态，输入框状态
		wareHousingFuzzySearchData.status = -1;
		wareHousingFuzzySearchData.staffID = -1;
		wareHousingFuzzySearchData.providerID = -1;
		wareHousingFuzzySearchData.shopID = -1;
		wareHousingFuzzySearchData.date1 = "1970/1/1 00:00:00";
		var date = new Date();
		wareHousingFuzzySearchData.date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss");
		wareHousingFuzzySearchData.queryKeyword = "";
		$(".layui-form-label").each(function (i) {
			$(".layui-form-label").eq(i).find("span").html($(".layui-form-label").eq(i).next().find("li").eq(0).text() + '&nbsp;v');
		});
		$(".warehousingManageCenter input[name='queryKeyword']").val("RK");
	}
	//返回所有的数据，并选择第一页第一条数据
	function queryAllWarehousingSheet () {
		showFirstDataOfTable = true;		//取消操作后，默认选择第一页第一条数据
		warehousing_reloadTable.pageIndex = 1;
		warehousingListRN(warehousing_reloadTable, warehousingRN_url, method_get);
	}
	//数据处理（创建/修改）
	function requestDataManage () {
		var barcodeIDs = "", commIDs = "", commNOs = "", commPrices = "", amounts = "";
		for (var i = 0; i < popupCommodityData.length; i++) {
			barcodeIDs += popupCommodityData[i].barcodeID + ",";
			commIDs += popupCommodityData[i].commID + ",";
			commNOs += popupCommodityData[i].NO + ",";
			commPrices += popupCommodityData[i].price + ",";
			amounts += popupCommodityData[i].specification + ",";		//由于原先写函数时没有考虑多的字段，故暂用规格字段存放数据
		}
		var requestData = new Object();
		requestData.warehouseID = 1;
		requestData.barcodeIDs = barcodeIDs;
		requestData.commIDs = commIDs;
		requestData.commNOs = commNOs;
		requestData.commPrices = commPrices;
		requestData.amounts = amounts;
		requestData.providerID = $(".warehousingSheetInfo .providerID").val();
		requestData.shopID = $(".warehousingSheetInfo .shopID").val();
		console.log(requestData);
		return requestData;
	}
	//点击新建时的准备工作
	function toCreateWarehousingSheet () {
		buttonAreaManage("create");
		var date = new Date();		//获取当前时间
		var nowYear = date.getFullYear(), nowMonth = date.getMonth() + 1, nowDate = date.getDate();
		$("#warehousingList li").css("background-color", "#fff");
		$(".warehousingSheetInfo .createDatetime").text(nowYear + "-" + nowMonth + "-" + nowDate).next().text("新建入库单");
		$(".warehousingSheetInfo .warehousingSheetSN, .warehousingSheetInfo .purchasingOrderSN").parent().hide();
		$(".warehousingSheetInfo .purchasingOrderSN").text("");
		$(".warehousingSheetInfo .warehousingSheetID").val("0");
		$(".warehousingSheetInfo .providerID").val(1);
		$(".warehousingSheetInfo .providerName").text("默认供应商").show().attr("title", "默认供应商");
		$(".warehousingSheetInfo .shopID").val(2);
		$(".warehousingSheetInfo .shopName").text("默认门店").show().attr("title", "默认门店");
		$(".warehousingSheetInfo .addProvider").show();
		$(".warehousingSheetInfo .addShop").show();
		$(".warehousingSheetInfo .amountTotalAndStatus .createStaff").text("创建人：" + $("#sessionStaffName").val());
		$(".warehousingCommodityList tbody").html("");
		warehousingCommTableInitialize();
		$(".amountTotalAndStatus button").addClass("hadApproveButton layui-btn-disabled").text("").hide();
		$(".amountTotalAndStatus strong").text("0.00");
		popupCommodityData = [];
		providerIDBeChoosed = 1;
		providerNameBeChoosed = "默认供应商";
		shopIDBeChoosed = 2;
		shopNameBeChoosed = "默认门店";
	}
	//无入库单数据时
	function noWarehousingData () {
		showFirstDataOfTable = true;
		buttonAreaManage("noWarehousingData");
		$("#warehousingList").html('<div class="warehousingRNHint">无数据</div>');
		$("#warehousingListPage").hide();
		var date = new Date();		//获取当前时间
		var nowYear = date.getFullYear(), nowMonth = date.getMonth() + 1, nowDate = date.getDate();
		$(".warehousingSheetInfo .createDatetime").text(nowYear + "-" + nowMonth + "-" + nowDate).next().text("入库单");
		$(".warehousingSheetInfo .providerName, .warehousingSheetInfo .addProvider, .warehousingSheetInfo .addShop").hide();
		$(".warehousingSheetInfo .warehousingSheetID").val("");
		$(".warehousingCommodityList table tbody").html("");
		$(".warehousingSheetInfo .warehousingSheetSN").parent().hide();
		$(".warehousingSheetInfo .purchasingOrderSN").parent().hide();
		$(".amountTotalAndStatus .amountTotal strong").hide();
		$(".amountTotalAndStatus .createStaff").hide();
	}
	//保存入库单和审核入库单的确认弹窗
	function warehousingSheetManage (text, url, method, requestData, failText, succText) {
		console.log("创建入库单对象" + requestData);
		layer.confirm(text, { icon: 3, title: '提示' }, function (index) {
			$.ajax({
				url: url,
				type: method,
				async: true,
				dataType: "json",
				data: requestData,
				success: function succFunction (data) {
					console.log(data);
					layer.close(index);
					if (!data) {
						layer.msg("服务器错误");
						return;
					}
					if (data.ERROR != "EC_NoError" && data.ERROR != "EC_PartSuccess") {
						if (data.msg) {
							layer.msg(data.msg);
						} else {
							layer.msg(failText);
						}
						return;
					}
					bDesertLastOperation = false;		//保存/审核成功后，恢复其默认值，即无需提示框
					var msg = data.msg == "" ? succText : data.msg;
					layer.msg(msg);
					if (url == warehousingCreate_url) {		//创建
						initQueryCondition();		//将查询条件恢复为默认状态
						queryAllWarehousingSheet();		//返回所有的数据，并选择第一页第一条数据
					} else if (url == warehousingApprove_url) {
						$("#warehousingList li").each(function () {		//审核
							if ($(this).find(".ID").val() == requestData.ID) {
								var providerName = $(".warehousingSheetInfo .providerName").text();
								$(this).click().find(".provider").text(providerName).attr("title", providerName);
								$(this).find(".icons").removeClass("circle_notApprove").addClass("circle_hadApproved");
								$(this).find(".statusText").text("已审核");
								$(this).find(".status").val(1);
							}
						});
						warehousingSheetR1(requestData.ID, 1);
						pageRefresh(purchasingOrderRN_url);
					} else if (url == warehousingUpdate_url) {		//修改
						$("#warehousingList li").each(function () {
							if ($(this).find(".ID").val() == requestData.ID) {
								var providerName = $(".warehousingSheetInfo .providerName").text();
								$(this).click().find(".provider").text(providerName).attr("title", providerName);
							}
						});
						warehousingSheetR1(requestData.ID, 0);
					} else { }		//其他操作
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.close(index);
					layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				}
			});
		})
	}
	//商品弹出层与供应商弹出层的“全部关闭”与“全部展开”
	$("#warehousingManageMain .showAll").click(function () {
		if ($(this).text() == "全部展开") {
			$(this).text("全部关闭");
			$(this).parent().next().children("li").addClass("layui-nav-itemed");
		} else {
			$(this).text("全部展开");
			$(this).parent().next().children("li").removeClass("layui-nav-itemed");
		}
	});
	//添加入库商品事件（点击“添加商品”，商品弹出层）
	window.toChooseCommodity = function () {
		layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#toChooseCommodity"),
			success: function (layero, index) {
				notRecordActions(popupCommodityData, tempArray);
				reloadTable(table, commRN_tableID, method_post, commRN_url, 1, commodity_reloadTable);
				$("#toChooseCommodity .rightRegion .footArea strong").text(popupCommodityData.length);		//已选择的商品数量
			},
			cancel: function (index, layero) {
				notRecordActions(tempArray, popupCommodityData);
			}
		});
	}
	//渲染商品表commodityList（弹出层）
	table.render({
		elem: '#popupCommodityList',
		url: commRN_url,		//type设为0是为了只查询普通商品
		method: method_post,
		id: commRN_tableID,
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
			{ field: 'name', title: '商品名称/规格', templet: '#generalCcommodityName', width: 150, align: 'center' },
			{
				field: 'string', title: '条形码', align: 'center', width: 160,
				templet: function (data) {
					console.log(data);
					var barcode = data.listBarcodes.length > 0 ? data.listBarcodes[0].barcode : "";
					return barcode;
				}
			},
			{ field: 'num', title: '数量', templet: '#numManage', width: 150, align: 'center' },
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
			if (!res) {
				layer.msg("服务器错误");
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
	//监听商品弹出层表格复选框选择commodityList
	table.on('checkbox(commodityList)', function (obj) {
		var checked = obj.checked;		//当前是否选中状态
		var data = obj.data;		//选中行的相关数据
		var type = obj.type;		//如果触发的是全选，则为：all，如果触发的是单选，则为：one
		var tr = obj.tr;
		if (type == "one") {
			var commID = data.commodity.ID;
			var commName = data.commodity.name;
			var barcodeID = data.listBarcodes.length > 0 ? data.listBarcodes[0].ID : "";
			var barcodeName = data.listBarcodes.length > 0 ? data.listBarcodes[0].barcode : "";
			var packageUnitID = data.commodity.packageUnitID;
			var packageUnitName = data.commodity.packageUnitName;
			var NO = 1;
			var price = data.commodity.listSlave2[0].latestPricePurchase;
			if (price == -1) {
				price = 0.00;
			}
			var amount = NO * price;
			for (var i = 0; i < popupCommodityData.length; i++) {		//检查是否已存在此商品(为表格切换页面时的bug而补充)
				if (popupCommodityData[i].commID == commID) {
					NO = popupCommodityData[i].NO;
					price = popupCommodityData[i].price;
					amount = popupCommodityData[i].specification;		//由于原先写函数时没有考虑多的字段，故暂用规格字段存放数据
				}
			}
			if (checked) {
				popupCommDataManage("add", commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, amount);
				$(tr).find(".generalCommNum").val(NO);
			} else {
				popupCommDataManage("delete", commID);
				$(tr).find(".generalCommNum").val("");
			}
		} else if (type == "all") {
			data = table.cache.commodityList;
			if (checked) {
				var commodityNotExist, commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price;
				for (var i = 0; i < data.length; i++) {
					commodityNotExist = true;
					commID = data[i].commodity.ID;
					commName = data[i].commodity.name;
					barcodeID = data[i].listBarcodes.length > 0 ? data[i].listBarcodes[0].ID : "";
					barcodeName = data[i].listBarcodes.length > 0 ? data[i].listBarcodes[0].barcode : "";
					packageUnitID = data[i].commodity.packageUnitID;
					packageUnitName = data[i].commodity.packageUnitName;
					NO = 1;
					price = data[i].commodity.listSlave2[0].latestPricePurchase;
					if (price == -1) {
						price = 0.00;
					}
					for (var j = 0; j < popupCommodityData.length; j++) {		//检查是否已存在此商品(为全选时数量不一致的bug而补充)
						if (popupCommodityData[j].commID == commID) {
							commodityNotExist = false;
						}
					}
					if (commodityNotExist) {
						popupCommDataManage("add", commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, NO * price);
					}
				}
				$("#popupCommodityList + div .generalCommNum").each(function () {		//为空的输入框初始赋值
					if ($(this).val() == "") {
						$(this).val(1);
					}
				});
			} else {
				var commID = "";
				for (var i = 0; i < data.length; i++) {
					commID = data[i].commodity.ID;
					popupCommDataManage("delete", commID);
				}
				$("#popupCommodityList + div .generalCommNum").val("");
			}
		}
	});
	//从商品弹出层中获取商品(弹出层"确定"按钮)
	$("#toChooseCommodity .confirmChoosedComm").click(function () {
		layer.close(layer.index);		//关闭最新层
		$(".warehousingCommodityList tbody").html("");
		if (popupCommodityData.length <= 0) {
			warehousingCommTableInitialize();		//当用户选择0个商品时
		} else {
			var amount = 0;
			var totalAmount = 0;
			for (var i = 0; i < popupCommodityData.length; i++) {
				var serialNum = i + 1;
				var warehousingCommodity = new Object();
				warehousingCommodity.barcode = popupCommodityData[i].barcodeName;
				warehousingCommodity.commodityID = popupCommodityData[i].commID;
				warehousingCommodity.commodityName = popupCommodityData[i].commName;
				warehousingCommodity.packageUnitName = popupCommodityData[i].packageUnitName;
				warehousingCommodity.NO = popupCommodityData[i].NO;
				warehousingCommodity.price = popupCommodityData[i].price;
				warehousingCommodity.amount = popupCommodityData[i].NO * popupCommodityData[i].price;
				amount = warehousingCommodity.amount;
				totalAmount += amount;		//入库单总价的计算
				warehousingCommTableRender(serialNum, warehousingCommodity);
			}
			$(".warehousingCommodityList tbody tr").find("td:nth-child(3)").css("padding-right", "30px");
			$(".amountTotalAndStatus strong").text(totalAmount.toFixed(2));
		}
		buttonAreaManage("hadChange");
	});
	//删除已添加到页面的商品（点击x，删除数据行）
	window.deleteWarehousingCommodity = function (index) {
		var commodityID = $(index).parents("tr").find(".commodityID").val();
		popupCommDataManage("delete", commodityID);
		$(index).parents("tr").remove();
		totalAmountCalculation();
		var commodityTR = $(".warehousingCommodityList tbody tr");		//获取剩余的商品tr单元行
		if (commodityTR.length > 0) {		//重新设置序号
			for (var i = 0; i < commodityTR.length; i++) {
				$(commodityTR[i]).find("span").text(i + 1);
			}
		} else {
			warehousingCommTableInitialize();		//当商品全部删除后
			$(".amountTotalAndStatus strong").text("0.00");
		}
		buttonAreaManage("hadChange");
	}
	//商品弹出层的新建商品按钮
	$("#toChooseCommodity .leftRegion .layui-btn").click(function () {
		pageJumping('commodity.bx', 'commodity.bx', '商品列表');
	});
	//模糊搜索商品（商品名称、七位数及以上的条形码）
	form.on('submit(commoditySearch)', function (data) {
		if (data.field.queryKeyword.length <= 64) {
			commodity_reloadTable.queryKeyword = data.field.queryKeyword;
			reloadTable(table, commRN_tableID, method_post, commRN_url, 1, commodity_reloadTable);
		}
	});
	//商品弹出层左侧列表事件
	$("#toChooseCommodity ul li dd a").click(function () {
		var categoryID = $(this).attr("indexID");
		commodity_reloadTable.categoryID = categoryID;
		reloadTable(table, commRN_tableID, method_post, commRN_url, 1, commodity_reloadTable);
	});
	//商品弹出层“取消”按钮
	$("#toChooseCommodity .exitChoosedComm").click(function () {
		notRecordActions(tempArray, popupCommodityData);
		layer.close(layer.index);		//关闭最新弹出的层
	});
	
	//供应商弹出层事件
	window.toAddProvider = function () {
		layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#toChooseProvider"),
			success: function (layero, index) {
				reloadTable(table, providerRN_tableID, method_post, providerRN_url, 1, provider_reloadTable);
			},
			cancel: function (index, layero) {
				providerIDBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .providerID").val();		//获取页面的供应商ID
				providerNameBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .providerName").text();		//获取页面的供应商ID
			}
		});
	}
	
	//供应商弹出层事件
	window.toAddProvider1 = function () {
		layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#toChooseProvider"),
			success: function (layero, index) {
				reloadTable(table, providerRN_tableID, method_post, providerRN_url, 1, provider_reloadTable);
			},
			cancel: function (index, layero) {
				providerIDBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .providerID").val();		//获取页面的供应商ID
				providerNameBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .providerName").text();		//获取页面的供应商ID
			}
		});
	}
	
	//渲染供应商表popupProviderList（弹出层）
	table.render({
		elem: '#popupProviderList',
		url: providerRN_url,
		id: providerRN_tableID,
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
			{ title: '操作', align: 'center', width: 60, templet: '#barDemo', event: 'choose' },
			{ field: 'name', title: '供应商名称', width: 160, align: 'center', event: 'choose' },
			{ field: 'contactName', title: '联系人', width: 150, align: 'center', event: 'choose' },
			{ field: 'mobile', title: '联系电话', width: 160, align: 'center', event: 'choose' },
			{ field: 'address', title: '地址', width: 190, align: 'center', event: 'choose' }
		]],
		done: function (res, curr, count) {
			if (!res) {
				layer.msg("服务器错误");
				return;
			}
			$("#toChooseProvider .layui-table tr").each(function () {		//翻页后给选择的供应商添加样式
				if ($(this).find("input").val() == providerIDBeChoosed) {
					$(this).find("td").eq(0).click();
				}
			});
		}
	});
	//供应商表格监听事件
	var providerIDBeChoosed = -1;
	var providerNameBeChoosed = "";
	table.on('tool(popupProviderList)', function (obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var tr = obj.tr;
		switch (layEvent) {
			case "choose":
				providerIDBeChoosed = data.ID;
				providerNameBeChoosed = data.name;
				tr.find(".barDemo").addClass("providerBeChoosed");
				tr.siblings().find(".barDemo").removeClass("providerBeChoosed");
				break;
			default:		//其他情况
				break;
		}
	})
	//确认选择供应商
	$("#toChooseProvider .confirmChoosedProvider").click(function () {
		$(".warehousingSheetInfo .providerID").val(providerIDBeChoosed);
		$(".warehousingSheetInfo .providerName").text(providerNameBeChoosed).show().attr("title", providerNameBeChoosed);
		buttonAreaManage("hadChange");
		layer.close(layer.index);		//关闭最新弹出的层
	});
	//供应商弹出层“取消”按钮
	$("#toChooseProvider .exitChoosedProvider").click(function () {
		providerIDBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .providerID").val();		//获取页面的供应商ID
		providerNameBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .providerName").text();		//获取页面的供应商ID
		layer.close(layer.index);		//关闭最新弹出的层
	});
	// 模糊搜索供应商（名称、联系方式、业务员或联系人）
	form.on('submit(providerSearch)', function (data) {
		if (data.field.queryKeyword.length <= 32) {
			var providerSearchData = new Object();
			providerSearchData.queryKeyword = data.field.queryKeyword;
			reloadTable(table, providerRN_tableID, method_post, providerRNByFields_url, 1, providerSearchData);
		} else {
			//超过长度限制不发送请求
		}
	});
	//供应商弹出层左侧列表事件
	$("#toChooseProvider ul li a").click(function () {
		var districtID = $(this).attr("indexID");
		provider_reloadTable.districtID = districtID;
		reloadTable(table, providerRN_tableID, method_post, providerRN_url, 1, provider_reloadTable);
	});
	//供应商弹出层的新建商品按钮
	$("#toChooseProvider .leftRegion .layui-btn").click(function () {
		pageJumping('commodity/about.bx', 'commodity/about.bx', '商品相关');
	});
	
	
	//门店弹出层事件
	window.toAddShop = function () {
		layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#toChooseShop"),
			success: function (layero, index) {
				reloadTable(table, shopRN_tableID, method_get, shopRN_url, 1, shop_reloadTable);
			},
			cancel: function (index, layero) {
				shopIDBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(2) .shopID").val();		//获取页面的门店ID
				providerNameBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(2) .shopName").text();		//获取页面的门店ID
			}
		});
	}
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
		$(".warehousingSheetInfo .shopID").val(shopIDBeChoosed);
		$(".warehousingSheetInfo .shopName").text(shopNameBeChoosed).show().attr("title", shopNameBeChoosed);
		buttonAreaManage("hadChange");
		layer.close(layer.index);		//关闭最新弹出的层
	});
	//门店弹出层“取消”按钮
	$("#toChooseShop .exitChoosedShop").click(function () {
		shopIDBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .shopID").val();		//获取页面的门店ID
		shopNameBeChoosed = $(".warehousingSheetInfo .baseInfo p:eq(1) .shopName").text();		//获取页面的门店ID
		layer.close(layer.index);		//关闭最新弹出的层
	});
	// 模糊搜索门店（名称）
	form.on('submit(shopSearch)', function (data) {
		if (data.field.queryKeyword.length <= 32) {
			var shopSearchData = new Object();
			shopSearchData.queryKeyword = data.field.queryKeyword;
			reloadTable(table, shopRN_tableID, method_post, shopRNByFields_url, 1, shopSearchData);
		} else {
			//超过长度限制不发送请求
		}
	});
	//门店弹出层左侧列表事件
	$("#toChooseShop ul li a").click(function () {
		var districtID = $(this).attr("indexID");
		shop_reloadTable.districtID = districtID;
		reloadTable(table, shopRN_tableID, method_get, shopRN_url, 1, shop_reloadTable);
	});
})