layui.use(['element', 'form', 'table', 'laydate', 'layer'], function () {
	var element = layui.element;
	var form = layui.form;
	var table = layui.table;
	var laydate = layui.laydate;
	var layer = layui.layer;
	//定义常量
	const promotionRN_url = "promotion/retrieveNEx.bx";		//查询促销活动满减优惠接口
	const promotionCreate_url = "promotionSync/createEx.bx";		//创建新的满减优惠活动接口
	const promotionDelete_url = "promotionSync/deleteEx.bx";		//终止（delete）满减优惠活动接口
	const commRN_url = "commodity/retrieveNEx.bx";		//查询商品接口
	const shopRN_url = "shop/retrieveNEx.bx";		//查询门店接口
	const shopRNByFields_url = 'shop/retrieveNByFieldsEx.bx';		//模糊查询门店
	const promotionRN_tableID = "promotionList";		//promotion表格ID
	const popupCommRN_tableID = "popupCommodityList";		//弹出普通商品表表格ID
	const shopRN_tableID = "popupShopList";		//门店表格ID（弹窗）
	const promotion_reloadTable = {};		//促销活动查询传参
	const commodity_reloadTable = { "status": -1, "type": 0 };		//商品查询传参
	const shop_reloadTable = {};		//查询门店的传参
	const method_get = "GET";		//get请求方式
	const method_post = "POST";		//post请求方式
	const curr_reloadTable = 1;		//默认页码
	var bDesertLastOperation = false;		//是否放弃上一步操作(数据是否发生改变)，false表示没有修改数据或者没有点击了新建按钮，true则相反

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

	//页面初始加载
	layer.ready(function () {
		var indexLoading = layer.load(1);
		initQueryCondition();
	});
	//初始化促销搜索功能传参
	function initQueryCondition () {
		promotion_reloadTable.status = -1;
		promotion_reloadTable.queryKeyword = "";
	}
	//开始日期
	laydate.render({
		elem: '#datetimeStart',
		type: 'datetime',
		format: 'yyyy-MM-dd HH:mm:ss', //自动生成的时间格式  
		change: function (value, date, endDate) {		//年月日时间被切换时都会触发。回调返回三个参数，分别代表：生成的值、日期时间对象、结束的日期时间对象
			bDesertLastOperation = true;
			$("#datetimeStart").attr("title", value);
		}
	});
	//结束日期
	laydate.render({
		elem: '#datetimeEnd',
		type: 'datetime',
		format: 'yyyy-MM-dd HH:mm:ss', //自动生成的时间格式  
		change: function (value, date, endDate) {
			bDesertLastOperation = true;
			$("#datetimeEnd").attr("title", value);
		}
	});
	//满减优惠的详情信息
	function promotionR1 (data) {
		var ID = data.ID;
		var name = data.name;
		var datetimeStart = data.datetimeStart;
		var datetimeEnd = data.datetimeEnd;
		var excecutionThreshold = data.excecutionThreshold;
		var excecutionAmount = data.excecutionAmount;
		var excecutionDiscount = data.excecutionDiscount * 10;
		var type = JSON.stringify(data.type);		//将数字类型转化成字符串
		var staff = data.staffName;
		var scope = JSON.stringify(data.scope);
		var shopScope = JSON.stringify(data.shopScope);
		datetimeStart = datetimeStart.substring(0, datetimeStart.length - 3);
		datetimeEnd = datetimeEnd.substring(0, datetimeEnd.length - 3);
		form.val("promotionInfo", {
			"ID": ID,
			"name": name,
			"datetimeStart": datetimeStart,
			"datetimeEnd": datetimeEnd,
			"excecutionThreshold": excecutionThreshold.toFixed(2),
			"excecutionAmount": excecutionAmount.toFixed(2),
			"excecutionDiscount": excecutionDiscount.toFixed(2),
			"type": type,
			"staff": staff,
			"scope": scope,
			"shopScope": shopScope,
		})
		$("#datetimeStart").attr("title", datetimeStart);		//给开始和结束时间加上title属性
		$("#datetimeEnd").attr("title", datetimeEnd);
		$(".promotionInfo .layui-form-item input").attr({ "readOnly": "readOnly", "disabled": "disabled" });
		$(".toStopPromotion").show();
		//对参与商品范围的判断
		if (scope == "0") {		//参与商品范围为全部商品
			$(".designatedCommodityList").hide();
		} else if (scope == "1") {		//参与商品范围为指定商品
			$("#commodityList tbody").html("");
			for (var i = 0; i < data.listSlave1.length; i++) {
				var serialNumber = i + 1;
				var commName = data.listSlave1[i].commodityName;
				var barcode = data.listSlave1[i].commodity.barcodes;
				var priceRetail = parseFloat(data.listSlave1[i].commodity.listSlave2[0].priceRetail).toFixed(2);
				barcode = barcode.substr(0, barcode.length - 1);		//把条形码的逗号去掉
				designatedCommTableRender(serialNumber, "", commName, barcode, priceRetail);
				$("#commodityList tbody tr:last").find("td").eq(0).text(serialNumber);
			}
			$("#commodityList tbody tr td:nth-child(2)").css("text-align", "center");
			$(".addGeneralComm").remove();
			$(".designatedCommodityList").show();
		}
		console.log("shopScope:" + shopScope);
		if(shopScope == "0") {
			$(".designatedShopList").hide();
		} else if (shopScope == "1") {		//参与门店范围为指定门店
			$("#shopList tbody").html("");
			for (var i = 0; i < data.listSlave2.length; i++) {
				var serialNumber = i + 1;
				var shopID = data.listSlave2[i].shopID;
				var shopName = data.listSlave2[i].shopName;
//				var priceRetail = parseFloat(data.listSlave1[i].commodity.priceRetail).toFixed(2);
//				barcode = barcode.substr(0, barcode.length - 1);		//把条形码的逗号去掉
//				designatedCommTableRender(serialNumber, "", commName, barcode, priceRetail);
				designatedShopTableRender(serialNumber, shopID, shopName);
				$("#shopList tbody tr:last").find("td").eq(0).text(serialNumber);
			}
			$("#shopList tbody tr td:nth-child(2)").css("text-align", "center");
			$(".addGeneralShop").remove();
			$(".designatedShopList").show();
		}
	}
	//渲染指定商品列表
	function designatedCommTableRender (serialNumber, commID, commName, barcode, price) {
		$("#commodityList tbody").append(
			'<tr>' +
			'<td><span>' + serialNumber + '</span><i class="layui-icon layui-icon-close-fill deleteGeneralComm" title="删除商品" onclick="deleteGeneralComm(this)"></i></td>' +
			'<td title="' + commName + '"><input type="hidden" class="commID" value="' + commID + '"/>' + commName + '<i class="layui-icon layui-icon-add-circle addGeneralComm" title="添加商品" onclick="toChooseGeneralComm()"></i></td>' +
			'<td><input type="text" class="designatedCommBarcode" title="' + barcode + '"  value = "' + barcode + '" readOnly="readOnly"/></td>' +
			'<td>' + price + '</td>' +
			'</tr>'
		)
	}
	//渲染指定门店列表
	function designatedShopTableRender (serialNumber, shopID, shopName) {
		$("#shopList tbody").append(
			'<tr>' +
			'<td><span>' + serialNumber + '</span><i class="layui-icon layui-icon-close-fill deleteGeneralShop" title="删除商品" onclick="deleteGeneralShop(this)"></i></td>' +
			'<td title="' + shopName + '"><input type="hidden" class="shopID" value="' + shopID + '"/>' + shopName + '<i class="layui-icon layui-icon-add-circle addGeneralShop" title="添加门店" onclick="toChooseGeneralShop()"></i></td>' +
//			'<td><input type="text" class="designatedCommBarcode" title="' + barcode + '"  value = "' + barcode + '" readOnly="readOnly"/></td>' +
//			'<td>' + price + '</td>' +
			'</tr>'
		)
	}
	//恢复指定商品列表初始状态
	function resetCommodityTable () {
		$("#commodityList tbody").html("");
		$("#commodityList tbody").append(
			'<tr>' +
			'<td><span>1</span><i class="layui-icon layui-icon-close-fill deleteGeneralComm" title="删除商品" onclick="deleteGeneralComm(this)"></i></td>' +
			'<td><i class="layui-icon layui-icon-add-circle addGeneralComm" title="添加商品" onclick="toChooseGeneralComm()"></i></td>' +
			'<td></td>' +
			'<td></td>' +
			'</tr>'
		);
	}
	//恢复指定门店列表初始状态
	function resetShopTable () {
		$("#shopList tbody").html("");
		$("#shopList tbody").append(
			'<tr>' +
			'<td><span>1</span><i class="layui-icon layui-icon-close-fill deleteGeneralShop" title="删除商品" onclick="deleteGeneralShop(this)"></i></td>' +
			'<td><i class="layui-icon layui-icon-add-circle addGeneralShop" title="添加商品" onclick="toChooseGeneralShop()"></i></td>' +
			'<td></td>' +
			'<td></td>' +
			'</tr>'
		);
	}
	//即时搜索促销活动名称和及时搜索商品名称
	window.instantSearch = function (index) {
		$(index).next().click();
	}
	//头部导航选项
	$(".topNav .layui-form-label span").click(function () {
		var index = $(this);
		var area = index.offset();		//获取当前点击对象的坐标
		var areaParent = index.parent().offset();		//获取当前点击对象父级元素的坐标
		var thisUl = index.parent().next();			//获取ul标签对象
		thisUl.css({ "display": "block", "left": area.left - areaParent.left });
	})
	// 根据活动状态搜索
	function statusSearch (thisStatus, thisText) {
		if (thisText != "所有" && thisText != "已删除") {
			promotion_reloadTable.status = 0;
			promotion_reloadTable.subStatusOfStatus = thisStatus;
		} else {
			promotion_reloadTable.status = thisStatus;
			promotion_reloadTable.subStatusOfStatus = 0;
		}
		reloadTable(table, promotionRN_tableID, method_post, promotionRN_url, curr_reloadTable, promotion_reloadTable);
	}
	//根据活动状态搜索满减活动
	$(".topNav ul li").click(function () {
		var thisStatus = $(this).find("input").val();
		var thisText = $(this).text();
		$(this).parent().hide();
		if (bDesertLastOperation) {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index) {
				bDesertLastOperation = false;
				$(".topNav .layui-form-label span").find("b").text(thisText);
				layer.close(index);
				statusSearch(thisStatus, thisText);		//点击确定执行根据状态搜索活动
			});
		} else {
			$(".topNav .layui-form-label span").find("b").text(thisText);
			statusSearch(thisStatus, thisText);
		}
	})
	//关闭头部导航选项区域
	$(document).click(function () {
		$(".topNav .layui-form-label + ul").hide();
	})
	$(document).delegate('.topNav .layui-form-label', 'click', function (event) {
		event.stopPropagation();
	})
	//	渲染表格promotionList
	var notStopPromotion = true;		//用于判断是否发生了终止促销操作，没有终止活动时，该变量的状态为true
	table.render({
		elem: '#promotionList',
		url: promotionRN_url,
		method: method_post,
		id: promotionRN_tableID,
		where: promotion_reloadTable,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList'
		},
		limit: '10',
		size: 'sm',
		skin: 'nob',
		even: true,
		page: true,
		cols: [[
			{ field: 'sn', title: '单号', width: 140, event: 'detail', align: 'center' },
			{ field: 'name', title: '活动名称', width: 140, templet: "#promotionName", event: 'detail', align: 'center' },
			{ field: 'staffName', title: '操作人员', width: 80, event: 'detail', align: 'center' },
			{
				field: 'status', title: '状态', width: 70, event: 'detail', align: 'center',
				templet: function (data) {		//由于渲染每行数据时需要时间，故时间的判断会有10毫秒左右的误差，目前先暂时忽略
					var promotionStatus = data.status;
					if (promotionStatus == 0) {
						var datetimeStart = data.datetimeStart.substring(0, data.datetimeStart.length - 4);
						var datetimeEnd = data.datetimeEnd.substring(0, data.datetimeEnd.length - 4);
						var todayTime = (new Date()).getTime();
						datetimeStart = (new Date(datetimeStart)).getTime();
						datetimeEnd = (new Date(datetimeEnd)).getTime();
						if (todayTime < datetimeStart) {
							promotionStatus = "未开始";
						} else if (todayTime < datetimeEnd) {
							promotionStatus = "进行中";
						} else {
							promotionStatus = "已结束";
						}
					} else if (promotionStatus == 1) {
						promotionStatus = "已删除";
					}
					return promotionStatus;
				}
			},
			{
				field: 'datetimeStart', title: '开始时间', width: 140, event: 'detail', align: 'center',
				templet: function (data) {
					var datetimeStart = data.datetimeStart.substring(0, data.datetimeStart.length - 4);
					return datetimeStart;
				}
			},
			{
				field: 'datetimeEnd', title: '结束时间', width: 140, event: 'detail', align: 'center',
				templet: function (data) {
					var datetimeEnd = data.datetimeEnd.substring(0, data.datetimeEnd.length - 4);
					return datetimeEnd;
				}
			},
			{ field: 'retailTradeNO', title: '参与单数', width: 80, event: 'detail', align: 'center' }
		]],
		done: function (res, curr, count) {		//每次加载完表格都会触发的回调函数
			if (res.ERROR != "EC_NoError") {
				var msg = res.msg == "" ? "查询满减优惠活动失败" : res.msg;
				layer.msg(msg);
			} else {
				var data = res.objectList;
				if (data.length > 0) {
					if (notStopPromotion) {
						if (bDesertLastOperation) {
							layer.confirm('确定要放弃之前的操作吗？', {
								icon: 3, title: '提示', cancel: function () {
									$(".fixedButtonArea button:eq(1), .fixedButtonArea button:eq(2)").removeClass("disabledButton").removeAttr("disabled");
								}
							}, function (index) {
								bDesertLastOperation = false;
								setStopPromotionButtonByStatus($("#promotionList + div .layui-table-body tbody tr").eq(0));
								promotionR1(data[0]);
								$("#promotionList + div .layui-table-body tbody tr").eq(0).addClass("trChoosed");
								layer.close(index);
							}, function () {
								$(".fixedButtonArea button:eq(1), .fixedButtonArea button:eq(2)").removeClass("disabledButton").removeAttr("disabled");
							})
						} else {
							setStopPromotionButtonByStatus($("#promotionList + div .layui-table-body tbody tr").eq(0));
							promotionR1(data[0]);
							$("#promotionList + div .layui-table-body tbody tr").eq(0).addClass("trChoosed");
						}
					} else {
						notStopPromotion = true;
						var promotionID = $(".promotionID").val();
						for (var i = 0; i < data.length; i++) {
							if (data[i].ID == promotionID) {
								$("#promotionList + div .layui-table-body tbody tr").eq(i).addClass("trChoosed");
							}
						}
					}
					$(".fixedButtonArea button:eq(1), .fixedButtonArea button:eq(2)").addClass("disabledButton").attr("disabled", "disabled");
				} else {
					clearPromotionInfo();
					layer.msg("查无优惠活动");
				}
				layer.closeAll('loading');
			}
		}
	})
	//根据活动状态判断终止按钮
	function setStopPromotionButtonByStatus (promotionObject) {
		var promotionStatus = $(promotionObject).children().eq(3).find("div").text();		//获取活动的状态
		if (promotionStatus == "已结束" || promotionStatus == "已删除") {		//当状态=已结束or已删除时，终止按钮不可用
			$(".toStopPromotion").addClass("disabledButton").attr("disabled", "disabled").removeClass("btnChoosed");
		} else {
			$(".toStopPromotion").removeClass("disabledButton").removeAttr("disabled").addClass("btnChoosed");
		}
	}
	//监听表格查看促销活动详情
	table.on('tool(promotionList)', function (obj) {
		var data = obj.data;
		var tr = obj.tr;
		switch (obj.event) {
			case 'detail':
				$(".fixedButtonArea button:eq(1), .fixedButtonArea button:eq(2)").addClass("disabledButton").attr("disabled", "disabled");
				$(".designatedCommodityTable").show();
				if (bDesertLastOperation) {
					layer.confirm('确定要放弃之前的操作吗？', {
						icon: 3, title: '提示', cancel: function () {
							$(".fixedButtonArea button:eq(1), .fixedButtonArea button:eq(2)").removeClass("disabledButton").removeAttr("disabled");
						}
					}, function (index) {
						$(tr).addClass("trChoosed").siblings().removeClass("trChoosed");
						bDesertLastOperation = false;
						promotionR1(data);
						setStopPromotionButtonByStatus(tr);
						layer.close(index);
					}, function () {
						$(".fixedButtonArea button:eq(1), .fixedButtonArea button:eq(2)").removeClass("disabledButton").removeAttr("disabled");
					})
				} else {
					$(tr).addClass("trChoosed").siblings().removeClass("trChoosed");
					promotionR1(data);
					setStopPromotionButtonByStatus(tr);
				}
				break;
		}
	});
	//判断是否修改数据
	window.check_ifDataChange = function (obj, value) {
		$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").removeClass("disabledButton").removeAttr("disabled")
			.siblings().removeClass("btnChoosed disabledButton").removeAttr("disabled");
		bDesertLastOperation = true;
	}
	//搜索功能 
	function queryPromotionByKeyword (data) {
		var queryKeyword = data.field.queryKeyword;
		if (queryKeyword.length <= 32) {
			promotion_reloadTable.queryKeyword = queryKeyword;
			reloadTable(table, promotionRN_tableID, method_post, promotionRN_url, curr_reloadTable, promotion_reloadTable);
		}
	}
	//模糊搜索功能
	form.on('submit(promotionSearch)', function (data) {		//1，获取输入的关键字2，判断数据是否符合格式3，把数据传给后端
		if (bDesertLastOperation) {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index) {
				bDesertLastOperation = false;
				queryPromotionByKeyword(data);
				layer.close(index);
			})
		} else {
			queryPromotionByKeyword(data);
		}
	});
	//数据验证
	fieldFormat(form);
	//准备创建促销活动
	function promotionToCreate () {
		popupCommodityData = [];
		$(".toStopPromotion").hide();
		form.val("promotionInfo", {
			"staff": $("#sessionStaffName").val(),
			"ID": "",
			"name": "",
			"datetimeStart": "",
			"datetimeEnd": "",
			"excecutionThreshold": "",
			"excecutionAmount": "",
			"excecutionDiscount": "",
			"scope": "0",
			"shopScope": "0",
			"type": "0",
		})
		$(".promotionInfo .layui-form-item input").removeAttr("readOnly disabled");
		$("#promotionList + div .layui-table-body tbody tr").removeClass("trChoosed layui-table-click");
		$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").removeClass("disabledButton").removeAttr("disabled")
			.siblings().removeClass("btnChoosed disabledButton").removeAttr("disabled");
		$(".promotionInfo input[name='staff']").attr("disabled", "disabled");
		$("#datetimeStart").attr("title", "");
		$("#datetimeEnd").attr("title", "");
		$(".designatedCommodityList").hide();
		$(".designatedShopList").hide();
		resetCommodityTable();
		resetShopTable();
	}
	//右边详情区域顶部按钮监听
	$(".promotionManage").click(function () {
		var keyword = $(this).text();
		$(this).addClass("btnChoosed").siblings().removeClass("btnChoosed");
		switch (keyword) {
			case "新建":
				if (bDesertLastOperation) {
					layer.confirm('确定要放弃之前的操作吗？', {
						icon: 3, title: '提示', cancel: function () {
							$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
						}
					}, function (index) {
						promotionToCreate();
						layer.close(index);
					}, function () {
						$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
					})
				} else {
					promotionToCreate();
					bDesertLastOperation = true;
				}
				break;
			case "取消":
				if (bDesertLastOperation) {
					layer.confirm('确定要放弃之前的操作吗？', {
						icon: 3, title: '提示', cancel: function () {
							$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
						}
					}, function (index) {
						layer.close(index);
						bDesertLastOperation = false;
						initQueryCondition();
						resetNavigationBar();		//恢复初始导航栏状态
						reloadTable(table, promotionRN_tableID, method_post, promotionRN_url, curr_reloadTable, promotion_reloadTable);
					}, function () {
						$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
					});
				} else {
					initQueryCondition();
					resetNavigationBar();		//恢复初始导航栏状态
					reloadTable(table, promotionRN_tableID, method_post, promotionRN_url, curr_reloadTable, promotion_reloadTable);
				}
				break;
			case "终止促销":
				var promotionID = $(".promotionID").val();
				var promotionName = $(".promotionName").val();
				layer.confirm('确定要终止 "' + promotionName + '"的促销活动吗？', { icon: 3, title: '提示' }, function (index) {
					var loading = layer.load(1);
					$.ajax({
						url: promotionDelete_url,
						type: method_get,
						async: true,
						data: { "ID": promotionID },
						dataType: "json",
						success: function succFunction (data) {
							var curr = data.curr;
							if (data.ERROR != "EC_NoError") {
								if (data.msg) {
									layer.msg(data.msg);
								} else {
									layer.msg("终止促销活动失败");
								}
								layer.close(loading)
								return;
							}
							notStopPromotion = false;
							reloadTable(table, promotionRN_tableID, method_post, promotionRN_url, curr, promotion_reloadTable);
							layer.msg("终止促销活动成功");
							$(".toStopPromotion").addClass("disabledButton").attr("disabled", "disabled").removeClass("btnChoosed");
							layer.close(loading);
						},
						error: function (XMLHttpRequest, textStatus, errorThrown) {
							layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
							layer.close(loading);
						}
					})
				}, function (index) {
					layer.close(index);
				});
				break;
		}
	})
	function resetNavigationBar () {		//恢复初始导航栏状态
		$(".topNav .layui-inline").find("b").text("所有");
		$("#left-Area input[name='queryKeyword']").val("CX");
	}
	//满减满折输入框获取焦点
	$(".cashReducingOfAmount, .discountOfAmount").focus(function () {
		$(this).siblings('input[type="radio"]').attr("checked", true);
		if ($(this).hasClass("cashReducingOfAmount")) {		//当选择满减时，满折的输入框的内容会被清空
			$(".discountOfAmount").val("");
		} else if ($(this).hasClass("discountOfAmount")) {		//当选择满折时，满减的输入框的内容会被清空
			$(".cashReducingOfAmount").val("");
		}
	})
	//右侧详情区域单选框按钮监听
	$(".promotionInfo .layui-form-item input[type='radio']").click(function () {
		if ($(this).hasClass("allCommodity")) {		//点击全部商品单选框时，指定商品列表会被隐藏
			$(".designatedCommodityList").hide();
		} else if ($(this).hasClass("designatedCommodity")) {		//点击指定商品单选框时，指定商品列表会显示
			$(".designatedCommodityList").show();
		} else if ($(this).hasClass("allShop")) {		//点击指定商品单选框时，指定商品列表会显示
			$(".designatedShopList").hide();
		} else if ($(this).hasClass("designatedShop")) {		//点击指定商品单选框时，指定商品列表会显示
			$(".designatedShopList").show();
		} else {
			var indexInput = $(this).siblings('input[type="text"]');
			if (indexInput.hasClass("cashReducingOfAmount")) {		//点击满减单选框时，清空满折输入框
				$(".discountOfAmount").val("");
			} else if (indexInput.hasClass("discountOfAmount")) {		//点击满折单选框时，清空满减输入框
				$(".cashReducingOfAmount").val("");
			}
		}
	})
	//选择促销范围
	$(".promotionScope").click(function () {
		var promotionID = $(".promotionID").val();
		if (!promotionID) {
			$(this).siblings("input[type='radio']").click();
		}
	})
	//保存按钮的监听
	form.on('submit(toCreatePromotion)', function (data) {
		var promotionName = $(".promotionName").val();
		var loading = layer.load(1);
		var promotionInfo = data.field;		//表单的字段信息
		var promotionID = $(".promotionID").val();		//获取促销活动ID
		var todayTime = ((new Date()).getTime() + 24 * 60 * 60 * 1000);		//获取当前时间
		var datetimeStart = ((new Date(promotionInfo.datetimeStart)).getTime());
		var datetimeEnd = ((new Date(promotionInfo.datetimeEnd)).getTime());
		var checkResult = false;		//初始化检查结果为false

		if (promotionID) {		//判断是新增满减优惠活动还是修改满减优惠活动
			return;
		} else {
			promotionInfo.staff = $("#sessionStaffID").val();		//当前登录人员的ID
			delete promotionInfo["ID"];
		}
		if (datetimeEnd < datetimeStart) {
			layer.msg('结束时间不能小于开始时间！');
		} else if (datetimeStart <= todayTime) {
			layer.msg('活动开始时间必须在24小时后！');
		} else if (datetimeStart == datetimeEnd) {
			layer.msg('结束时间不能小于或等于开始时间！');
		} else if (promotionInfo.excecutionThreshold > 10000) {
			layer.msg("优惠门槛不能大于10000！");
		} else if (parseFloat(promotionInfo.excecutionThreshold) < parseFloat(promotionInfo.excecutionAmount)) {
			layer.msg("优惠门槛需大于满减金额！");
		} else if (!(promotionInfo.excecutionAmount) && !(promotionInfo.excecutionDiscount)) {
			layer.msg("满减和满折不能都为空！");
		} else if (!promotionInfo.excecutionAmount && (promotionInfo.excecutionDiscount >= 10 || promotionInfo.excecutionDiscount <= 0)) {
			layer.msg("折扣需处于0-10的之间！");
		} else if (promotionInfo.excecutionAmount == "0" && !promotionInfo.excecutionDiscount) {
			layer.msg("满减金额不能等于0！");
		} else if (promotionInfo.scope == "1" && popupCommodityData.length <= 0) {
			layer.msg("请选择指定商品！");
		} else if (promotionInfo.shopScope == "1" && popupShopData.length <= 0) {
			layer.msg("请选择指定门店！");
		} else if (promotionInfo.scope == "1" || promotionInfo.shopScope == "1") {
			if(promotionInfo.scope == "1") {
				var commIDs = "";
				for (var i = 0; i < popupCommodityData.length; i++) {
					var barcodeName = popupCommodityData[i].barcodeName;
					if (barcodeName) {
						var commID = popupCommodityData[i].commID;
						commIDs += commID + ",";
					} else {
						layer.msg("不能选择不存在条形码的商品参与促销");
						layer.close(loading);
						return;
					}
				}
				promotionInfo.commodityIDs = commIDs;
				checkResult = true;
			}
			if(promotionInfo.shopScope == "1") {
				var shopIDs = "";
				for (var i = 0; i < popupShopData.length; i++) {
					var shopID = popupShopData[i].shopID;
					shopIDs += shopID + ",";
				}
				promotionInfo.shopIDs = shopIDs;
				checkResult = true;
			}
		}  else {
			checkResult = true;
		}
		if (checkResult) {
			if (!promotionInfo.excecutionAmount) {		//满减为空时，默认传0给后端		
				promotionInfo.excecutionAmount = 0;
			}
			if (!promotionInfo.excecutionDiscount) {		//满折为空时，默认传0给后端
				promotionInfo.excecutionDiscount = 0;
			}
			promotionInfo.datetimeStart = promotionInfo.datetimeStart.replace(new RegExp("-", "g"), "/");
			promotionInfo.datetimeEnd = promotionInfo.datetimeEnd.replace(new RegExp("-", "g"), "/");
			promotionInfo.excecutionDiscount = promotionInfo.excecutionDiscount * 0.1;		//根据后端需求，需要把折扣乘以0.1
			layer.confirm('确定要创建 "' + promotionName + '"的促销活动吗', { icon: 6, title: '提示' }, function (index) {
				var loading = layer.load(1);
				$.ajax({
					url: promotionCreate_url,
					type: method_post,
					async: true,
					data: promotionInfo,
					dataType: 'json',
					success: function succFunction (data) {
						if (data.ERROR != "EC_NoError") {
							if (data.msg) {
								layer.msg(data.msg);
							} else {
								layer.msg('新建满减优惠活动失败');
							}
							return;
						}
						bDesertLastOperation = false;
						notStopPromotion = true;
						layer.msg('新建满减优惠活动成功');
						initQueryCondition();
						resetNavigationBar();		//恢复初始导航栏状态
						reloadTable(table, promotionRN_tableID, method_post, promotionRN_url, curr_reloadTable, promotion_reloadTable);
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						layer.close(loading);
						layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
					}
				})
				layer.close(loading);
			}, function (index) {
				layer.close(index);
			});
		}
		layer.close(loading);
		return false;
	})
	//弹出选择商品弹窗
	window.toChooseGeneralComm = function () {
		layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#toChooseComm"),
			success: function (layero, index) {
				notRecordActions(popupCommodityData, tempArray);		//先保留原始已选择的商品信息
				reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, commodity_reloadTable);
				$(".footArea").find("strong").text(popupCommodityData.length);
			},
			cancel: function (index, layero) {
				notRecordActions(tempArray, popupCommodityData);		//还原原先保留的商品信息
			}
		})
	}
	//弹出选择门店弹窗
	window.toChooseGeneralShop = function () {
		layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#chooseShopWindow"),
			success: function (layero, index) {
//				notRecordActions(popupCommodityData, tempArray);		//先保留原始已选择的商品信息
//				reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, commodity_reloadTable);
//				$(".footArea").find("strong").text(popupCommodityData.length);
				reloadTable(table, shopRN_tableID, method_get, shopRN_url, 1, shop_reloadTable);
			},
			cancel: function (index, layero) {
				notRecordActions(tempArray, popupCommodityData);		//还原原先保留的商品信息
			}
		})
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
			{ type: 'checkbox' },
//			{ templet: '#shopDemo', title: '操作', width: 60, align: 'center', event: 'choose' },
			{ field: 'name', title: '门店名称', templet: '#popupShopName',width: 160, align: 'center', event: 'choose' },
			{ field: 'address', title: '地址', width: 190, align: 'center', event: 'choose' }
		]],
		done: function (res, curr, count) {
			if (!res) {
				layer.msg("服务器错误");
				return;
			} else {
				var data = res.objectList;
				console.log("data:" + JSON.stringify(data));
				for (var i = 0; i < data.length; i++) {
					var index = data[i].LAY_TABLE_INDEX;
					var indexShopID = data[i].ID;
					for (var j = 0; j < popupShopData.length; j++) {
						if (popupShopData[j].shopID == indexShopID) {
							$("#popupShopList + div").find("tr[data-index='" + index + "'] .layui-form-checkbox").click();
						}
					}
				}
			}
			layer.closeAll('loading');
			
//			$("#toChooseShop .layui-table tr").each(function () {		//翻页后给选择的供应商添加样式
//				if ($(this).find("input").val() == shopIDBeChoosed) {
//					$(this).find("td").eq(0).click();
//				}
//			});
		}
	});
	
	//渲染商品表popupCommodityList
	table.render({
		elem: '#popupCommodityList',
		url: commRN_url,
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
				field: 'string', title: '条形码', width: 220, align: 'center',
				templet: function (data) {
					var barcode = data.listBarcodes.length > 0 ? data.listBarcodes[0].barcode : "";
					return barcode;
				}
			},
			{
				field: 'priceRetail', title: '零售价', width: 75, align: 'center',
				templet: function (data) {
					console.log(data);
					var priceRetail = parseFloat(data.commodity.listSlave2[0].priceRetail).toFixed(2);
					return priceRetail;
				}
			},
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
		console.log("data:" + JSON.stringify(data));
		if (type == "one") {
			var commID = data.commodity.ID;
			var commName = data.commodity.name;
			var barcodeName = data.commodity.barcodes;
			var priceRetail = parseFloat(data.commodity.listSlave2[0].priceRetail).toFixed(2);
			if (checked) {
				popupCommDataManage("add", commID, commName, "", barcodeName, "", "", "", priceRetail, "");
			} else {
				popupCommDataManage("delete", commID);
				$(tr).find(".popupCommNum").val("");
			}
		} if (type == "all") {
			data = table.cache.popupCommodityList;
			if (checked) {
				for (var i = 0; i < data.length; i++) {
					var commodityNotExist = true;
					var commID = data[i].commodity.ID;
					var commName = data[i].commodity.name;
					var barcodeName = data[i].commodity.barcodes;
					var priceRetail = parseFloat(data[i].commodity.listSlave2[0].priceRetail).toFixed(2);
					for (var j = 0; j < popupCommodityData.length; j++) {		//检查是否已存在此商品(为全选时数量不一致的bug而补充)
						if (popupCommodityData[j].commID == commID) {
							commodityNotExist = false;
						}
					}
					if (commodityNotExist) {
						popupCommDataManage("add", commID, commName, "", barcodeName, "", "", "", priceRetail, "");
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
//				if (data.length <= 0) {
//					resetCommodityTable();
//				}
			}
		}
	});
	//监听门店表格复选框选择popupCommodityList
	table.on('checkbox(popupShopList)', function (obj) {
		var checked = obj.checked;		//当前是否选中状态
		var data = obj.data;		//选中行的相关数据
		var type = obj.type;		//如果触发的是全选，则为：all，如果触发的是单选，则为：one
		var tr = obj.tr;		//选中行的dom对象
		console.log("data:" + JSON.stringify(data));
		if (type == "one") {
			var shopID = data.ID;
			var shopName = data.name;
//			var barcodeName = data.commodity.barcodes;
//			var priceRetail = parseFloat(data.commodity.priceRetail).toFixed(2);
			if (checked) {
				popupShopDataManage("add", shopID, shopName);
//				popupCommDataManage("add", commID, commName, "", barcodeName, "", "", "", priceRetail, "");
			} else {
				popupShopDataManage("delete", shopID);
				$(tr).find(".popupCommNum").val("");
			}
		} if (type == "all") {
			data = table.cache.popupShopList;
			if (checked) {
				for (var i = 0; i < data.length; i++) {
					var shopNotExist = true;
					var shopID = data[i].ID;
					var shopName = data[i].name;
//					var barcodeName = data[i].commodity.barcodes;
//					var priceRetail = parseFloat(data[i].commodity.priceRetail).toFixed(2);
					for (var j = 0; j < popupShopData.length; j++) {		//检查是否已存在此门店(为全选时数量不一致的bug而补充)
						if (popupShopData[j].shopID == shopID) {
							shopNotExist = false;
						}
					}
					if (shopNotExist) {
						popupShopDataManage("add", shopID, shopName);
					}
				}
				$("#popupShopList + div .popupCommNum").each(function () {		//为空的输入框初始赋值
					if ($(this).val() == "") {
						$(this).val(1);
					}
				});
			} else {
				for (var i = 0; i < data.length; i++) {
					var shopID = data[i].ID;
					popupShopDataManage("delete", shopID);
				}
				$("#popupShopList + div .popupCommNum").val("");
			}
		}
	});
	//弹出框展开或关闭全部商品类别
	$(".showAllCommCategory").click(function () {
		if ($(this).text() === "全部展开") {
			$(this).parent().next().find("li").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		} else {
			$(this).parent().next().find("li").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
		}
	})
	//弹出商品模糊搜索功能
	form.on('submit(commoditySearch)', function (data) {		//1，获取输入的关键字2，判断数据是否符合格式3，把数据传给后端
		var queryKeyword = data.field.queryKeyword;
		if (queryKeyword.length <= 64) {
			var loading = layer.load(1);
			commodity_reloadTable.queryKeyword = queryKeyword;
		}
		reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, commodity_reloadTable);
		return false;
	});
	//根据商品分类查询商品
	$(".layui-nav-tree li dl dd a").click(function () {
		var indexLoading = layer.load(1);
		var indexCategoryID = $(this).attr("indexID");
		console.log(indexCategoryID);
		commodity_reloadTable.categoryID = indexCategoryID;
		reloadTable(table, popupCommRN_tableID, method_post, commRN_url, curr_reloadTable, commodity_reloadTable);
	})
	//商品弹窗确认选择商品
	$(".confirmChoosedComm").click(function () {
		$('.layui-icon-close').click();
		$("#commodityList tbody").html("");
		for (var i = 0; i < popupCommodityData.length; i++) {
			var serialNumber = i + 1;
			var commID = popupCommodityData[i].commID;
			var commName = popupCommodityData[i].commName;
			var barcode = popupCommodityData[i].barcodeName;
			var priceRetail = parseFloat(popupCommodityData[i].price).toFixed(2);
			designatedCommTableRender(serialNumber, commID, commName, barcode, priceRetail);
		};
		layer.closeAll('page');		//关闭商品弹窗
	});
	//门店弹窗确认选择门店
	$(".confirmChoosedShop").click(function () {
		$('.layui-icon-close').click();
		$("#shopList tbody").html("");
		for (var i = 0; i < popupShopData.length; i++) {
			var serialNumber = i + 1;
			var shopID = popupShopData[i].shopID;
			var shopName = popupShopData[i].shopName;
//			var barcode = popupCommodityData[i].barcodeName;
//			var priceRetail = parseFloat(popupCommodityData[i].price).toFixed(2);
//			designatedCommTableRender(serialNumber, commID, commName, barcode, priceRetail);
			designatedShopTableRender(serialNumber, shopID, shopName);
		};
		layer.closeAll('page');		//关闭商品弹窗
	});
	//弹窗取消按钮监听
	$(".footArea .confirmExitComm").click(function () {
		notRecordActions(tempArray, popupCommodityData);
		layer.closeAll('page');		//关闭商品弹窗
	});
	//弹窗取消按钮监听
	$(".exitChoosedShop").click(function () {
//		notRecordActions(tempArray, popupCommodityData);
		layer.closeAll('page');		//关闭门店弹窗
	});
	//删除已添加到页面的商品（点击x，删除数据行）
	window.deleteGeneralComm = function (index) {
		var commID = $(index).parents("tr").find(".commID").val();
		popupCommDataManage("delete", commID);
		$(index).parents("tr").remove();
		var commTR = $("#commodityList tbody tr");		//获取剩余的商品tr单元行
		for (var i = 0; i < commTR.length; i++) {
			$(commTR[i]).find("span").text(i + 1);
		}
		if (commTR.length <= 0) {
			resetCommodityTable();
		}
	}
	//删除已添加到页面的门店（点击x，删除数据行）
	window.deleteGeneralShop = function (index) {
		var shopID = $(index).parents("tr").find(".shopID").val();
		popupShopDataManage("delete", shopID);
		$(index).parents("tr").remove();
		var shopTR = $("#shopList tbody tr");		//获取剩余的商品tr单元行
		for (var i = 0; i < shopTR.length; i++) {
			$(shopTR[i]).find("span").text(i + 1);
		}
		if (shopTR.length <= 0) {
			resetShopTable();
		}
	}
	function clearPromotionInfo () {
		form.val("promotionInfo", {
			"ID": "",
			"name": "",
			"datetimeStart": "",
			"datetimeEnd": "",
			"excecutionThreshold": "",
			"excecutionAmount": "",
			"excecutionDiscount": "",
			"type": "",
			"scope": "",
			"staff": ""
		})
		$(".fixedButtonArea button:eq(0)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
		$(".fixedButtonArea button:not(:eq(0))").addClass("disabledButton").attr("disabled", "disabled");
		$(".toStopPromotion").addClass("disabledButton").attr("disabled", "disabled");
		$(".designatedCommodityTable tbody").html("");
	}
	
	// 是否全部展开门店区域
	$(".showAllShopDistrict").click(function () {
		if ($(this).text() == "全部展开") {
			$("#chooseShopWindow .leftRegion .layui-nav-item").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		} else {
			$("#chooseShopWindow .leftRegion .layui-nav-item").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
		}
	});
	
	// 根据供门店区域重载数据表格
	$("#chooseShopWindow .layui-nav-child dd").click(function () {
		var loading = layer.load(1);
		var districtID = $(this).find("input").val();
		shop_reloadTable.districtID = districtID;
		reloadTable(table, shopRN_tableID, method_get, shopRN_url, 1, shop_reloadTable);
		layer.close(loading);
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
});

