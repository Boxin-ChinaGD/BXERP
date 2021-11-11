layui.use(['element', 'form', 'table', 'layer', 'colorpicker', 'laydate'], function () {
	var element = layui.element;		//常用元素模块
	var form = layui.form;		//表单模块
	var table = layui.table;		//表格模块
	var layer = layui.layer;		//弹出层模块
	var colorpicker = layui.colorpicker;		//颜色选择器模块
	var laydate = layui.laydate;		//时间选择模块

	const couponRN_url = "coupon/retrieveNEx.bx";		//查询优惠券的接口
	const couponCreate_url = "coupon/createEx.bx";		//创建优惠券的接口
	const couponEnd_url = "coupon/deleteEx.bx";		//终止优惠券的接口
	const commodityRN_url = "commodity/retrieveNEx.bx";		//查询商品的接口
	const couponTable_tableID = "couponTable";		//优惠券表格的表格ID
	const popupCommodityTable_tableID = "popupCommodityList";		//商品弹窗中商品表格的表格ID
	const commodity_reloadTable = { "status": -1, "type": 0 };		//默认查询商品的参数，status：-1 所有状态；type：0 单品；
	const method_post = "POST";
	const method_get = "GET";
	const originalCouponParam = {
		"ID": "",
		"title": "",
		"color": "#07C160",
		"status": "0",
		"type": "0",
		"leastAmount": "",
		"reduceAmount": "",
		"discount": "",
		"weekDayAvailable": "127",
		"beginTime": "00:00:00",
		"endTime": "23:59:59",
		"beginDateTime": "",
		"endDateTime": "",
		"bonus": "0",
		"personalLimit": "1",
		"quantity": "1",
		"remainingQuantity": "",
		"description": "",
		"scope": "0",
	}
	var bDesertLastOperation = true;		//用于提示是否放弃之前的操作，true表示放弃，false表示不放弃。比如，用户点击了新建，然后点击搜索，这时会弹出提示

	var popupPageWidth;		//商品弹窗的宽度，会随页面变化进行调整
	//页面加载后执行
	$(document).ready(function () {
		adaptiveScreen();
	})

	//缩放窗口时执行
	window.onresize = function () {
		adaptiveScreen();
	}

	fieldFormat(form);

	//自适应屏幕函数
	function adaptiveScreen () {
		var windowWidth = $(window).width();
		if (windowWidth >= 1060) {
			popupPageWidth = "1000px";
		} else {
			popupPageWidth = windowWidth - 60 + "px";
		}
	}
	//数值限制检查
	form.verify({
		leastAmount: function (value, item) {
			if (value > 99999 || isNaN(value)) {
				isToSend = true;
				return "请输入正确的起用金额";
			}
		},
		reduceAmount: function (value, item) {
			if (value > 99999 || isNaN(value)) {
				isToSend = true;
				return "请输入正确的减免金额";
			}
		},
		personalLimit: function (value, item) {
			if (value > 999 || isNaN(value)) {
				isToSend = true;
				return "请输入正确的每人可领券的限制数量";
			}
		},
		NAME_quantity: function (value, item) {
			if (value > 99999 || isNaN(value)) {
				isToSend = true;
				return "请输入正确的库存数量";
			}
		},
	})

	//选择优惠券类型
	function chooseCouponType (type) {
		if (type == "0") {		//代金券
			$("input[name='reduceAmount']").parents(".layui-form-item").show();
			$("input[name='discount']").val("0.00").parents(".layui-form-item").hide();
			$("input[name='discount']").siblings(".form_msg").hide();
		} else if (type == "1") {		//折扣券
			$("input[name='reduceAmount']").val("0.00").parents(".layui-form-item").hide();
			$("input[name='reduceAmount']").siblings(".form_msg").hide();
			$("input[name='discount']").parents(".layui-form-item").show();
		} else {
			console.log("未知的优惠券类型");
		}
	}

	//选择可用时段
	function chooseCouponAvailableTimeInterval (availableTimeInterval) {
		if (availableTimeInterval == 0) {		//全部时段
			$(".toChooseTime").hide();
		} else if (availableTimeInterval > 0) {		//部分时段
			$(".toChooseTime").show();
		} else {
			console.log("未知的可用时间段");
		}
	}

	//选择优惠券商品范围
	function chooseCouponCommodityScope (commodityScope) {
		if (commodityScope == 0) {		//全部商品
			$(".chooseCommodity").hide();
		} else if (commodityScope == 1) {		//部分商品
			$(".chooseCommodity").show();
		} else {
			console.log("未知的商品范围");
		}
	}

	//点击新建优惠券按钮
	function toCreateCoupon () {
		bDesertLastOperation = false;
		$("#couponTable + div tbody tr").removeClass("trBeChoosed");
		$(".couponDetail p.form_msg").hide();
		form.val("couponDetailForm", originalCouponParam);
		//恢复颜色选择器初始状态
		//		$("input[name='color']").next().remove().before("<div id='colorPicker'></div>");
		$(".couponColor").hide().siblings().show();
		colorpicker.render(colorPickerParameter);
		//隐藏优惠券状态字段
		$("input[name='status']").parents(".layui-form-item").hide();
		//显示减免金额字段，隐藏打折额度字段，因为默认选择代金券类型
		chooseCouponType(0);
		//可用时段默认选择全部时段，需隐藏部分时段区域
		chooseCouponAvailableTimeInterval(0);
		$("input[name=chooseTime][value=0]").attr("checked", "checked");
		$("input[name=chooseTime][value=1]").removeAttr("checked");
		$(".toChooseTime input[type='checkbox']").attr("checked", "checked");
		//恢复有效期区域
		$("#beginDateTime").show().next().hide().val("");
		$("#endDateTime").show().next().hide().val("");
		//商品范围默认选择全部商品，需隐藏部分商品的区域
		chooseCouponCommodityScope(0);
		//隐藏当前剩余库存数量字段
		$("input[name=remainingQuantity]").parents(".layui-form-item").hide();
		//显示创建优惠券按钮，隐藏终止优惠券按钮
		$(".createCoupon").show();
		$(".terminateCoupon").hide();
		$(".couponDetail select[name=type]").removeAttr("disabled");
		$(".couponDetail input[type=radio]").removeAttr("disabled");
		$(".couponDetail input[type=text]").removeAttr("readonly disabled");
		$(".toChooseTime input[type=checkbox]").removeAttr("disabled");
		$(".couponDetail textarea").removeAttr("readonly");
		form.render(null, "couponDetailForm");
		popupCommodityData = [];
		resetCommodityTable();
	}

	//查看一种优惠券的详情
	function couponR1 (data, tr) {
		console.log(data);
		bDesertLastOperation = true;
		$(".couponDetail p.form_msg").hide();
		tr.addClass("trBeChoosed").siblings().removeClass("trBeChoosed");
		form.val("couponDetailForm", {
			"ID": data.ID,
			"title": data.title,
			"status": data.status,
			"type": data.type,
			"leastAmount": data.leastAmount,
			"reduceAmount": data.reduceAmount,
			"discount": data.discount * 10,
			"weekDayAvailable": data.weekDayAvailable,
			"beginTime": data.beginTime,
			"endTime": data.endTime,
			"bonus": data.bonus,
			"personalLimit": data.personalLimit,
			"quantity": data.quantity,
			"remainingQuantity": data.remainingQuantity,
			"description": data.description
		})
		$(".couponColor").show().siblings().hide();
		$(".couponColor span").css("background-color", data.color);
		$("input[name='color']").val(data.color);
		$("input[name='status']").parents(".layui-form-item").show();
		$("input[name=remainingQuantity]").parents(".layui-form-item").show();
		$("#beginDateTime").hide().next().show().val(data.beginDateTime.substr(0, data.beginDateTime.length - 4));
		$("#endDateTime").hide().next().show().val(data.endDateTime.substr(0, data.endDateTime.length - 4));
		chooseCouponType(data.type);
		chooseCouponAvailableTimeInterval(data.weekDayAvailable);
		chooseCouponCommodityScope(data.scope);
		if (data.status == 0) {		//状态单选框
			$("input[name=status][value=0]").attr("checked", "checked");
			$("input[name=status][value=1]").removeAttr("checked");
			$(".terminateCoupon").show();
		} else if (data.status == 1) {
			$("input[name=status][value=0]").removeAttr("checked");
			$("input[name=status][value=1]").attr("checked", "checked");
			$(".terminateCoupon").hide();
		} else {
			console.log("未知的优惠券状态");
		}
		if (data.weekDayAvailable == 0) {		//可用时段的单选框
			$("input[name=chooseTime][value=0]").attr("checked", "checked");
			$("input[name=chooseTime][value=1]").removeAttr("checked");
		} else {
			$("input[name=chooseTime][value=0]").removeAttr("checked");
			$("input[name=chooseTime][value=1]").attr("checked", "checked");
			$(".toChooseTime input[type=checkbox]").removeAttr("checked");
			if ((data.weekDayAvailable & 32) == 32) {
				$("input[title=周一]").attr("checked", "checked");
			}
			if ((data.weekDayAvailable & 16) == 16) {
				$("input[title=周二]").attr("checked", "checked");
			}
			if ((data.weekDayAvailable & 8) == 8) {
				$("input[title=周三]").attr("checked", "checked");
			}
			if ((data.weekDayAvailable & 4) == 4) {
				$("input[title=周四]").attr("checked", "checked");
			}
			if ((data.weekDayAvailable & 2) == 2) {
				$("input[title=周五]").attr("checked", "checked");
			}
			if ((data.weekDayAvailable & 1) == 1) {
				$("input[title=周六]").attr("checked", "checked");
			}
			if ((data.weekDayAvailable & 64) == 64) {
				$("input[title=周日]").attr("checked", "checked");
			}
		}
		if (data.scope == 0) {		//商品范围的单选框
			$("input[name=scope][value=0]").attr("checked", "checked");
			$("input[name=scope][value=1]").removeAttr("checked");
		} else {
			$("input[name=scope][value=0]").removeAttr("checked");
			$("input[name=scope][value=1]").attr("checked", "checked");
			$(".chooseCommodity tbody").html("");
			for (var i = 0; i < data.listSlave1.length; i++) {
				var serialNumber = i + 1;
				var commID = data.listSlave1[i].commodityID;
				var commName = data.listSlave1[i].commodityName;
				var barcode = data.listSlave1[i].barcodes;
				var priceRetail = parseFloat(data.listSlave1[i].priceRetail).toFixed(2);
				commodityTableRender(serialNumber, commID, commName, barcode, priceRetail);
				$(".chooseCommodity tbody tr:last").find("td").eq(0).text(serialNumber);
			};
			$(".chooseCommodity tbody .addCommodity").remove();
		}
		$(".couponDetail select[name=type]").attr("disabled", "disabled");
		$(".couponDetail input[type=radio]").attr("disabled", "disabled");
		$(".couponDetail input[type=text]").attr({ "readonly": "readonly", "disabled": "disabled" });
		$(".toChooseTime input[type=checkbox]").attr("disabled", "disabled");
		$(".couponDetail textarea").attr("readonly", "readonly");
		$(".createCoupon").hide();
		form.render(null, "couponDetailForm");
	}

	//检查优惠券名称
	window.checkCouponTitle = function (value) {
		console.log(value);
		if (/^[\u4E00-\u9FA5A-Za-z0-9.]+$/.test(value) && value.length > 0 && value.length <= 9) {
			$(".couponDetail input[name=title]").siblings(".form_msg").hide();
		} else {
			$(".couponDetail input[name=title]").siblings(".form_msg").show();
		}
	}

	//检查优惠券起用金额
	window.checkCouponLeastAmount = function (value) {
		console.log(value);
		if (/^$|^[0-9]+(\.[0-9]{1,2})?$/.test(value) && value.length > 0) {
			$(".couponDetail input[name=leastAmount]").siblings(".form_msg").hide();
		} else {
			$(".couponDetail input[name=leastAmount]").siblings(".form_msg").show();
		}
	}

	//检查优惠券减免金额
	window.checkCouponReduceAmount = function (value) {
		console.log(value);
		var leastAmount = $(".couponDetail input[name=leastAmount]").val() == "" ? "" : $(".couponDetail input[name=leastAmount]").val();
		if (/^$|^[0-9]+(\.[0-9]{1,2})?$/.test(value) && parseFloat(value) > 0 && parseFloat(value) < parseFloat(leastAmount) && value.length > 0) {
			$(".couponDetail input[name=reduceAmount]").siblings(".form_msg").hide();
		} else {
			$(".couponDetail input[name=reduceAmount]").siblings(".form_msg").show();
		}
	}

	//检查优惠券打折额度
	window.checkCouponDiscount = function (value) {
		console.log(value);
		if (/^$|^[0-9]+(\.[0-9]{1,2})?$/.test(value) && parseFloat(value) > 0 && parseFloat(value) < 10 && value.length > 0) {
			$(".couponDetail input[name=discount]").siblings(".form_msg").hide();
		} else {
			$(".couponDetail input[name=discount]").siblings(".form_msg").show();
		}
	}

	//检查优惠券有效期
	window.checkCouponBeginDateTimeAndEndDateTime = function (time1, time2) {
		console.log(time1);
		console.log(time2);
		var beginDateTime = new Date(time1).getTime();
		var endDateTime = new Date(time2).getTime();
		console.log(beginDateTime);
		console.log(endDateTime);
		if (beginDateTime < endDateTime) {
			$(".couponDetail input[name=beginDateTime]").parents(".layui-inline").find(".form_msg").hide();
		} else {
			$(".couponDetail input[name=beginDateTime]").parents(".layui-inline").find(".form_msg").show();
		}
	}

	//检查优惠券是否支持积分兑换字段
	window.checkCouponBonus = function (value) {
		console.log(value);
		if (/^[0-9\s]+$/.test(value) && value.length > 0) {
			$(".couponDetail input[name=bonus]").siblings(".form_msg").hide();
		} else {
			$(".couponDetail input[name=bonus]").siblings(".form_msg").show();
		}
	}

	//检查优惠券每人可领数量限制或库存数量
	window.checkCouponPersonalLimitOrQuantity = function (index) {
		var value = $(index).val();
		console.log(value);
		if (/^[1-9]\d*$/.test(value) && value.length > 0) {
			$(index).siblings(".form_msg").hide();
		} else {
			$(index).siblings(".form_msg").show();
		}
	}

	//检查优惠券使用说明
	window.checkCouponDescription = function (value) {
		console.log(value);
		if (value.length <= 1024) {
			$(".couponDetail textarea[name=description]").siblings(".form_msg").hide();
		} else {
			$(".couponDetail textarea[name=description]").siblings(".form_msg").show();
		}
	}

	//渲染表格couponTable
	table.render({
		elem: "#couponTable",
		url: couponRN_url,
		id: couponTable_tableID,
		method: method_get,
		where: {},
		request: {
			pageName: "pageIndex",
			limitName: "pageSize"
		},
		response: {
			dataName: "objectList",
		},
		skin: "nob",
		limit: '10',
		limits: [10],
		page: true,
		cols: [[
			{ field: "title", title: "名称", align: "center" },
			{
				field: "type", title: "类型", align: "center",
				templet: function (data) {
					if (data.type == 0) {
						return "代金券";
					} else if (data.type == 1) {
						return "折扣券";
					} else {
						return "未知类型";
					}
				}
			},
			{
				field: "status", title: "状态", align: "center",
				templet: function (data) {
					if (data.status == 0) {
						return "正常";
					} else if (data.status == 1) {
						return "已终止";
					} else {
						return "未知状态";
					}
				}
			},
			{
				field: "bonus", title: "积分兑换", align: "center",
				templet: function (data) {
					if (data.bonus <= 0) {
						return "不支持使用积分兑换";
					} else {
						return "可使用" + data.bonus + "积分兑换";
					}
				}
			},
			{ field: "personalLimit", title: "每人可领数量", align: "center" },
			{ field: "quantity", title: "库存", align: "center" },
			{ field: "tool", title: "操作", align: "center", toolbar: "<div><span lay-event='detail' style='border:1px solid #eef;padding:3px; color:#888'>查看</span></div>", style: "cursor: pointer; color: #2196F3;" }
		]],
		done: function (res, curr, count) {
			console.log(res);
			if (res.objectList.length > 0) {
				$("#couponTable + div tbody tr:first td:last").find("span").click();
			} else {
				$(".toCreateCoupon").click();
			}
		}
	});

	//查看优惠券详情
	table.on("tool(couponTable)", function (obj) {
		$('.discount').attr('title', '');
		var data = obj.data;		//当前行的数据
		var layEvent = obj.event;	//当前行的事件关键字
		var tr = obj.tr;		//当前行的dom对象
		console.log(layEvent);
		switch (layEvent) {
			case "detail":
				if (bDesertLastOperation) {
					couponR1(data, tr);
				} else {
					layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
						layer.close(index);
						couponR1(data, tr);
					}, function (index, layero) {		//点击取消
						layer.close(index);
					})
				}

				break;
			default:
				console.log("未知的定义事件");
				return;
		}
	})

	//新建优惠券按钮监听
	$(".toCreateCoupon").click(function () {
		$('.discount').attr('title', '顾客应付的金额为商品价钱乘以折扣（最少折扣值为0.01）');
		if (bDesertLastOperation) {
			toCreateCoupon();
		} else {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
				layer.close(index);
				toCreateCoupon();
			}, function (index, layero) {		//点击取消
				layer.close(index);
			})
		}
	})

	//颜色选择器
	var colorPickerParameter = {
		elem: "#colorPicker",
		color: "#07C160",
		format: "hex",
		colors: ["#63b359", "#2c9f67", "#509fc9", "#5885cf", "#9062c0", "#d09a45", "#e4b138", "#ee903c", "#dd6549", "#cc463d"],
		predefine: "true",
		change: function (color) {
			console.log(color);
		},
		done: function (color) {
			console.log(color);
			$("input[name='color']").val(color);
		}
	}
	colorpicker.render(colorPickerParameter);

	//优惠券类型选择
	form.on("select(couponType)", function (data) {
		console.log(data.value);
		chooseCouponType(data.value);
	})

	//优惠券可用时间段选择
	form.on("radio(chooseTime)", function (data) {
		console.log(data.value);
		chooseCouponAvailableTimeInterval(data.value);
	})

	//选择指定周几可用
	form.on("checkbox(week)", function (data) {
		var checked = data.elem.checked;		//是否被选中，true或者false
		var num = Number(data.value);		//对应复选框的value值
		var originalNum = Number($("input[name='weekDayAvailable']").val());
		if (checked) {
			originalNum += num;
		} else {
			originalNum -= num;
		}
		$("input[name='weekDayAvailable']").val(originalNum);
		console.log(originalNum);
	})

	//选择时间
	laydate.render({
		elem: "#beginTime",
		type: "time",
		format: "HH:mm:ss",
		value: "00:00:00",
		btns: ['now', 'confirm']
	})
	laydate.render({
		elem: "#endTime",
		type: "time",
		format: "HH:mm:ss",
		value: "23:59:59",
		btns: ['now', 'confirm']
	})

	//选择有效期
	laydate.render({
		elem: "#beginDateTime",
		type: "datetime",
		format: "yyyy/MM/dd HH:mm:ss",
		//		value: new Date(),
		min: 0,
		done: function (value, date, endDate) {
			var endDateTime = $("input[name=endDateTime]").val() == "" ? 0 : $("input[name=endDateTime]").val();
			checkCouponBeginDateTimeAndEndDateTime(value, endDateTime);
		}
	})
	laydate.render({
		elem: "#endDateTime",
		type: "datetime",
		format: "yyyy/MM/dd HH:mm:ss",
		//		value: new Date(new Date().getTime() + 24 * 60 * 60 * 1000),
		min: 0,
		done: function (value, date, endDate) {
			var beginDateTime = $("input[name=beginDateTime]").val() == "" ? 0 : $("input[name=beginDateTime]").val();
			checkCouponBeginDateTimeAndEndDateTime(beginDateTime, value);
		}
	})

	//可使用的商品范围选择
	form.on("radio(scope)", function (data) {
		console.log(data.value);
		chooseCouponCommodityScope(data.value);
	})

	//渲染弹出层表格
	table.render({
		elem: '#popupCommodityList',
		url: commodityRN_url,
		id: popupCommodityTable_tableID,
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
					var priceRetail = parseFloat(data.commodity.priceRetail).toFixed(2);
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
					var latestPricePurchase = data.commodity.latestPricePurchase;
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
			console.log(res);
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

	//打开商品弹出层
	window.toChooseCommodity = function () {
		layer.open({
			type: 1,
			area: popupPageWidth,
			content: $("#toChooseComm"),
			success: function (layero, index) {
				notRecordActions(popupCommodityData, tempArray);		//先保留原始已选择的商品信息
				reloadTable(table, popupCommodityTable_tableID, method_post, commodityRN_url, 1, commodity_reloadTable);
				$(".footArea").find("strong").text(popupCommodityData.length);
			},
			cancel: function (index, layero) {
				notRecordActions(tempArray, popupCommodityData);		//还原原先保留的商品信息
			}
		})
	}

	//商品弹窗展开或关闭全部商品类别
	$(".showAllCommCategory").click(function () {
		if ($(this).text() === "全部展开") {
			$(this).parent().next().find("li").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		} else {
			$(this).parent().next().find("li").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
		}
	})

	//商品弹窗关键字模糊搜索
	form.on('submit(commoditySearch)', function (data) {		//1，获取输入的关键字2，判断数据是否符合格式3，把数据传给后端
		var queryKeyword = data.field.queryKeyword;
		if (queryKeyword.length <= 64) {
			var loading = layer.load(1);
			console.log(queryKeyword);
			commodity_reloadTable.queryKeyword = queryKeyword;
		}
		reloadTable(table, popupCommodityTable_tableID, method_post, commodityRN_url, 1, commodity_reloadTable);
		return false;
	});

	//商品弹窗根据商品类别搜索
	$(".layui-nav-tree li dl dd a").click(function () {
		var indexLoading = layer.load(1);
		var indexCategoryID = $(this).attr("indexID");
		console.log(indexCategoryID);
		commodity_reloadTable.categoryID = indexCategoryID;
		reloadTable(table, popupCommodityTable_tableID, method_post, commodityRN_url, 1, commodity_reloadTable);
	})

	//监听商品弹窗商品表格复选框选择
	table.on("checkbox(popupCommodityList)", function (obj) {
		var checked = obj.checked;		//当前是否选中状态
		var data = obj.data;		//选中行的相关数据
		var type = obj.type;		//如果触发的是全选，则为：all，如果触发的是单选，则为：one
		var tr = obj.tr;		//选中行的dom对象
		if (type == "one") {
			var commID = data.commodity.ID;
			var commName = data.commodity.name;
			var barcodeName = data.commodity.barcodes;
			var priceRetail = parseFloat(data.commodity.priceRetail).toFixed(2);
			if (checked) {
				popupCommDataManage("add", commID, commName, "", barcodeName, "", "", "", priceRetail, "");
			} else {
				popupCommDataManage("delete", commID);
			}
		} if (type == "all") {
			data = table.cache.popupCommodityList;
			if (checked) {
				for (var i = 0; i < data.length; i++) {
					var commID = data[i].commodity.ID;
					var commName = data[i].commodity.name;
					var barcodeName = data[i].commodity.barcodes;
					var priceRetail = parseFloat(data[i].commodity.priceRetail).toFixed(2);
					popupCommDataManage("add", commID, commName, "", barcodeName, "", "", "", priceRetail, "");
				}
			} else {
				for (var i = 0; i < data.length; i++) {
					var commID = data[i].commodity.ID;
					popupCommDataManage("delete", commID);
				}
			}
		}
	});

	//商品弹窗确认选择商品
	$(".confirmChoosedComm").click(function () {
		if (popupCommodityData.length > 0) {
			$(".chooseCommodity tbody").html("");
			for (var i = 0; i < popupCommodityData.length; i++) {
				var serialNumber = i + 1;
				var commID = popupCommodityData[i].commID;
				var commName = popupCommodityData[i].commName;
				var barcode = popupCommodityData[i].barcodeName;
				var priceRetail = parseFloat(popupCommodityData[i].price).toFixed(2);
				commodityTableRender(serialNumber, commID, commName, barcode, priceRetail);
			};
		} else {
			resetCommodityTable();
		}
		layer.closeAll("page");		//关闭商品弹出层
	});

	//渲染商品范围的商品表格
	function commodityTableRender (serialNumber, commID, commName, barcode, priceRetail) {
		$(".chooseCommodity tbody").append(
			'<tr>' +
			'<td><span>' + serialNumber + '</span><i class="layui-icon layui-icon-close-fill deleteCommodity" title="删除商品" onclick="deleteCommodity(this)"></i></td>' +
			'<td title="' + commName + '"><input type="hidden" class="commID" value="' + commID + '"/>' + commName + '<i class="layui-icon layui-icon-add-circle addCommodity" title="添加商品" onclick="toChooseCommodity()"></i></td>' +
			'<td title="' + barcode + '">' + barcode + '</td>' +
			'<td title="' + priceRetail + '">' + priceRetail + '</td>' +
			'</tr>'
		)
	}

	//商品弹窗取消按钮监听
	$(".footArea .confirmExitComm").click(function () {
		notRecordActions(tempArray, popupCommodityData);
		layer.closeAll("page");		//关闭商品弹出层
	});

	//商品范围商品表格删除商品
	window.deleteCommodity = function (index) {
		var commID = $(index).parents("tr").find(".commID").val();
		popupCommDataManage("delete", commID);
		$(index).parents("tr").remove();
		var commTR = $(".chooseCommodity tbody tr");		//获取剩余的商品tr单元行
		for (var i = 0; i < commTR.length; i++) {
			$(commTR[i]).find("span").text(i + 1);
		}
		if (commTR.length <= 0) {
			resetCommodityTable();
		}
	}

	//重置商品范围商品表格
	function resetCommodityTable () {
		$(".chooseCommodity tbody").html("");
		$(".chooseCommodity tbody").append(
			'<tr>' +
			'<td>1</td>' +
			'<td><i class="layui-icon layui-icon-add-circle addCommodity" title="添加商品" onclick="toChooseCommodity()"></i></td>' +
			'<td></td>' +
			'<td></td>' +
			'</tr>'
		)
	}

	//创建优惠券
	form.on("submit(createCoupon)", function (data) {
		console.log(data.field);
		var couponData = data.field;
		//由于title、leastAmount、reduceAmount、discount、有效期没有初始赋值，所以会在提交时检查一遍，以免用户不填
		checkCouponTitle(couponData.title);
		checkCouponLeastAmount(couponData.leastAmount);
		if (couponData.type == 0) {
			checkCouponReduceAmount(couponData.reduceAmount);
		} else if (couponData.type == 1) {
			checkCouponDiscount(couponData.discount);
		} else {
			console.log("未知的优惠券类型");
		}
		checkCouponBeginDateTimeAndEndDateTime(couponData.beginDateTime, couponData.endDateTime);
		//为了selenium能正常做结果验证添加的
		checkCouponBonus(couponData.bonus);
		checkCouponPersonalLimitOrQuantity($(".couponDetail input[name=personalLimit]"));
		checkCouponPersonalLimitOrQuantity($(".couponDetail input[name=quantity]"));
		//如果创建的是部分商品可使用的优惠券，需检查是否选择了商品\
		console.log(popupCommodityData.length);
		if (couponData.scope == 1 && popupCommodityData.length < 1) {
			layer.msg("请选择可使用该优惠券的商品");
			return;
		} else if (couponData.scope == 1 && popupCommodityData.length >= 1) {
			var commIDs = "";
			for (var i = 0; i < popupCommodityData.length; i++) {
				var commID = popupCommodityData[i].commID;
				commIDs += commID + ",";
			}
			couponData.commodityIDs = commIDs;
		} else {
			console.log("创建的不是部分商品可使用的优惠券");
		}
		var wrongLength = $(".couponDetail .form_msg:visible").length;
		console.log(wrongLength);
		if (wrongLength == 0) {
			console.log("优惠券的数据格式符合要求，可以创建优惠券");
			layer.confirm('确定创建该优惠券？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
				layer.close(index);
				couponData.discount = couponData.discount / 10;
				couponData.weekDayAvailable = couponData.chooseTime == 0 ? couponData.chooseTime : couponData.weekDayAvailable;
				couponData.remainingQuantity = couponData.quantity;
				delete couponData["chooseTime"];
				delete couponData["ID"];
				$.ajax({
					url: couponCreate_url,
					type: method_post,
					async: true,
					dataType: "json",
					data: couponData,
					success: function succFunction (data) {
						$('.discount').attr('title', '');
						console.log(data);
						if (data.ERROR == "EC_NoError") {
							bDesertLastOperation = true;
							reloadTable(table, couponTable_tableID, method_get, couponRN_url, 1, {});
							layer.msg("创建优惠券成功");
						} else {
							var failMsg = data.msg == "" ? "创建优惠券失败" : data.msg;
							layer.msg(failMsg);
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
					}
				});
			}, function (index, layero) {		//点击取消
				layer.close(index);
			})
		} else {
			layer.msg("优惠券数据格式不符合要求，请检查修改后重试");
		}
		return false;
	})

	//终止优惠券
	form.on("submit(terminateCoupon)", function (data) {
		var couponData = data.field;
		console.log(couponData.ID);
		layer.confirm('确定终止该优惠券？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
			layer.close(index);
			$.ajax({
				url: couponEnd_url,
				type: method_post,
				async: true,
				dataType: "json",
				data: { "ID": couponData.ID },
				success: function succFunction (data) {
					console.log(data);
					if (data.ERROR == "EC_NoError") {
						var couponList = table.cache.couponTable;
						for (var i = 0; i < couponList.length; i++) {
							if (couponList[i].ID == couponData.ID) {
								couponList[i].status = 1;
							} else {
								continue;
							}
						}
						$(".trBeChoosed").find("td").eq(2).find("div").text("已终止");
						$("input[name=status][value=0]").removeAttr("checked");
						$("input[name=status][value=1]").attr("checked", "checked");
						$(".terminateCoupon").hide();
						form.render("radio", "couponDetailForm");
						layer.msg("终止优惠券成功");
					} else {
						var failMsg = data.msg == "" ? "终止优惠券失败" : data.msg;
						layer.msg(failMsg);
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				}
			});
		}, function (index, layero) {		//点击取消
			layer.close(index);
		})
		return false;
	})
})