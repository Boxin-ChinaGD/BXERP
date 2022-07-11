layui.use(['element', 'form', 'table', "upload", 'laypage', "laydate"], function () {
	var table = layui.table;
	var laydate = layui.laydate;
	//定义函数内常量
	const method_post = "POST";		//post请求方式
	const retailTradeN_url = "retailTrade/retrieveNEx.bx";		//查询零售单接口
	const retailTradeDailyReportByCommodity_url = "retailTradeDailyReportByCommodity/retrieveN.bx";		//销售商品的排行数据
	const retailTradeRN_tableID = "allSalesNoteList";		//零售单表格ID
	const data_reloadTable = {};		//搜索零售单传参
	const curr_reloadTable = 1;		//默认页码
	//页面初始化就绪
	layer.ready(function () {
		initQueryCondition();
	});
	//初始化零售单搜索功能传参
	function initQueryCondition () {
		data_reloadTable.datetimeStart = "1970/01/01 00:00:00";
		data_reloadTable.datetimeEnd = new Date().format("yyyy/MM/dd") + " 23:59:59";
		data_reloadTable.queryKeyword = "";
		data_reloadTable.staffID = 0;
		data_reloadTable.shopID = 0;
		data_reloadTable.paymentType = 0;
	}
	//判断支付类型
	function paymentOfType (paymentType) {
		var Type = "";
		if ((paymentType & 1) == 1) {
			Type = "现金支付";
		}
		if ((paymentType & 2) == 2) {
			Type += (Type == "" ? "" : "|") + "支付宝支付";
		}
		if ((paymentType & 4) == 4) {
			Type += (Type == "" ? "" : "|") + "微信支付";
		}
		if ((paymentType & 8) == 8) {
			Type += (Type == "" ? "" : "|") + "其他支付方式1";
		}
		if ((paymentType & 16) == 16) {
			Type += (Type == "" ? "" : "|") + "其他支付方式2";
		}
		if ((paymentType & 32) == 32) {
			Type += (Type == "" ? "" : "|") + "其他支付方式3";
		}
		if ((paymentType & 64) == 64) {
			Type += (Type == "" ? "" : "|") + "其他支付方式4";
		}
		if ((paymentType & 128) == 128) {
			Type += (Type == "" ? "" : "|") + "其他支付方式5";
		}
		return Type;
	}

	//左侧数据渲染
	var bShowRetailTradeSummary = false;
	table.render({
		elem: '#allSalesNoteList',
		url: retailTradeN_url,		//数据接口
		id: retailTradeRN_tableID,
		method: method_post,
		width: 740,
		where: data_reloadTable,
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
			{
				field: 'sn', title: '单号', width: 180, align: 'center',
				templet: function (data) {
					var sn = data.sn;
					if (sn.indexOf('_') > -1) {
						return "<font color='red'>退&nbsp;</font>" + sn;
					} else {
						return sn;
					}
				}
			},
			{ field: 'staffName', title: '操作人员', width: 100, align: 'center' },
			{
				field: 'paymentType', title: '支付方式', width: 100, align: 'center',
				templet: function (data) {
					console.log(data);
					var paymentType = paymentOfType(data.paymentType);
					return paymentType;
				}
			},
			{
				field: 'saleDatetime', title: '销售时间', width: 180, align: 'center',
				templet: function (data) {
					var saleDatetime = data.saleDatetime;
					return saleDatetime.substring(0, 19);
				}
			},
			{
				field: 'NO', title: '商品数量', width: 80, align: 'center',
				templet: function (data) {
					var NO = 0;
					for (var i = 0; i < data.listSlave1.length; i++) {
						NO += data.listSlave1[i].NO
					}
					return NO;
				}
			},
			{
				field: 'amount', title: '金额', width: 98, align: 'center',
				templet: function (data) {
					var amount = data.amount;
					return amount.toFixed(2);
				}
			}
		]],
		done: function (res, curr, count) {
			console.log(res);
			layer.close(layer.index);
			if (res.ERROR != "EC_NoError") {
				var msg = res.msg == "" ? "查询零售单失败" : res.msg;
				layer.msg(msg);
			} else {
				if (res.count > 0) {	//零售单单数>0
					$("#allSalesNoteList + div .layui-table tr[data-index='0']").click();		//默认显示第一条
				} else {
					$(".retailTradeSN span").text("");		//零售单号
					$(".staffName span").text("");		//收银员
					$(".saleDatetime span").text("");		//零售时间
					$(".pos_SN span").text("");		//POS机流水单号
					$(".retailTradeCommodity").remove();		//零售商品信息
					$(".totalAmount td").eq(1).text("0.00");		//合计
					$(".discountAmount td").eq(1).text("0.00");		//优惠
					$(".payableAmount td").eq(1).text("0.00");		//应付
					$(".paymentType").remove();		//支付方式明细
					$(".change td").eq(1).text("0.00");		//找零
					$(".particulars_table table tbody tr").removeClass("odd");
					$(".particulars_table table tbody tr:odd").addClass("odd");		//表格隔行添加背景色
				}
				if(bShowRetailTradeSummary){
					var totalCommNO = res.totalCommNO;
					var retailAmount = res.retailAmount;
					var totalGross = res.totalGross;
					$(".totalCommNO").text(totalCommNO);
					$(".retailAmount").text(retailAmount.toFixed(2) + "元");
					$(".totalGross").text(totalGross.toFixed(2) + "元");
					$(".summarizing").show();
				}else{
					$(".summarizing").hide();
				}
			}
		}
	});

	// 头部导航选项
	$(".topNav .layui-form-label span").click(function () {
		var index = $(this);
		var area = index.offset();
		var areaParent = index.parent().offset();
		thisUl = index.parent().next();
		$(".topNav .layui-form-label + ul").hide();
		thisUl.css({ "display": "block", "left": area.left - areaParent.left });
	})
	$(".topNav .layui-form-label span").parent().next().find("li").click(function (event) {
		var index = $(this).parents(".layui-inline").find(".layui-form-label span");
		var date = new Date();
		index.parent().next().hide();
		index.find("label").text($(this).clone().children().remove().end().text());
		switch ($(this).text()) {
			case "所有":
				if ($(this).attr("data") == "操作人员") {
					data_reloadTable.staffID = 0;
				} else if ($(this).attr("data") == "门店") {
					data_reloadTable.shopID = 0;
				} else if ($(this).attr("data") == "支付方式") {
					data_reloadTable.paymentType = 0;
				} else if ($(this).attr("data") == "销售时间") {
					data_reloadTable.datetimeStart = "1970/01/01 00:00:00";
					data_reloadTable.datetimeEnd = new Date().format("yyyy/MM/dd") + " 23:59:59";
				} else {
					return;		//不支持的查询
				}
				break;
			case "过去一周内":
				data_reloadTable.datetimeStart = new Date(date.getTime() - 168 * 60 * 60 * 1000).format("yyyy/MM/dd") + " 00:00:00";
				data_reloadTable.datetimeEnd = new Date().format("yyyy/MM/dd") + " 23:59:59";
				console.log(data_reloadTable.datetimeEnd);
				break;
			case "过去一月内":
				data_reloadTable.datetimeStart = new Date(date.getTime() - 720 * 60 * 60 * 1000).format("yyyy/MM/dd") + " 00:00:00";
				data_reloadTable.datetimeEnd = new Date().format("yyyy/MM/dd") + " 23:59:59";
				break;
			case "现金支付":
				data_reloadTable.paymentType = 1;
				break;
			case "微信支付":
				data_reloadTable.paymentType = 4;
				break;
			case "支付宝支付":
				data_reloadTable.paymentType = 2;
				break;
			case "支付方式1":
				data_reloadTable.paymentType = 8;
				break;
			case "支付方式2":
				data_reloadTable.paymentType = 16;
				break;
			case "支付方式3":
				data_reloadTable.paymentType = 32;
				break;
			case "支付方式4":
				data_reloadTable.paymentType = 64;
				break;
			case "支付方式5":
				data_reloadTable.paymentType = 128;
				break;
			case "自定义时间":
				queryRetailTradeByTime();
				return;
			default:		//通过收银员ID来查询
				index.find("lable").text(domIndex.clone().children().remove().end().text());
				if (domIndex.find("span").text() == "操作人员") {
					data_reloadTable.staffID = $(this).find("input").val();
				}else if (domIndex.find("span").text() == "门店") {
					data_reloadTable.shopID = $(this).find("input").val();
				}
				break;
		}
		reloadTable(table, retailTradeRN_tableID, method_post, retailTradeN_url, curr_reloadTable, data_reloadTable);
	})
	//通过自定义的时间查询零售单
	function queryRetailTradeByTime () {
		layer.open({
			title: "自定义时间搜索",
			type: 1,
			btn: ['确定'],
			content: $(".dateTime"),
			success: function () {
				$("div.layui-layer-title").attr("style", "background-color: #1A4A9F !important; color: #fff; cursor: move;");
				$("span.layui-layer-setwin").html('<a class="layui-icon layui-icon-close layui-layer-close layui-layer-close1" href="javascript:;" style="color: #ffffff !important; font-size: 15px;"></a>');
				$("#timeQuantum").val("");
			},
			yes: function (index, layero) {
				if ($("#timeQuantum").val() == "") {
					$(".dateTime").append("<font class='hint' color='red'>请选择时间段</font>");
				} else {
					layer.close(index);
					reloadTable(table, retailTradeRN_tableID, method_post, retailTradeN_url, curr_reloadTable, data_reloadTable);
				}
			}
		});
	}
	//时间段选择
	laydate.render({
		elem: '#timeQuantum',
		type: 'datetime',
		range: '到',
		format: 'yyyy年M月d日',
		min: '1970-1-1',
		max: new Date().format("yyyy-MM-dd"),
		done: function (value, date, endDate) {
			data_reloadTable.datetimeStart = new Date(date.year + "/" + date.month + "/" + date.date + " 00:00:00");
			data_reloadTable.datetimeEnd = new Date(endDate.year + "/" + endDate.month + "/" + endDate.date + " 23:59:59");
			$(".topNav .sell .layui-form-label span label").text(date.year + "/" + date.month + "/" + date.date + "-" + endDate.year + "/" + endDate.month + "/" + endDate.date);
			$(".hint").hide();
		}
	});
	// 关闭头部导航选项区域
	$(document).click(function () {
		$(".topNav .layui-form-label + ul").hide();
	})
	$(document).delegate('.topNav .layui-form-label', 'click', function (event) {
		event.stopPropagation();
		console.log("阻止上述事件冒泡");
	})

	//左侧数据表格单击事件
	table.on('row(allSalesNoteList)', function (obj) {
		$(this).addClass("click").siblings().removeClass("click");
		var data = obj.data;		//获取到的零售单数据
		console.log(obj.data)
		$(".retailTradeSN span").text(data.sn);
		$(".staffName span").text(data.staffName);
		$(".saleDatetime span").text((data.saleDatetime).substring(0, 19));
		$(".pos_SN span").text(data.pos_SN);

		//渲染零售单商品表格
		var totalAmount = 0;		//总金额
		var discountAmount = 0;		//优惠金额
		var payableAmount = 0;		//应付金额
		$(".retailTradeCommodity").remove();
		for (var i = 0; i < data.listSlave1.length; i++) {
			$(".demarcationRow").before(
				"<tr class='retailTradeCommodity'>" +
				"<td style='width: 92px;'><div class='wrap' title='" + data.listSlave1[i].commodityName + "'>" + data.listSlave1[i].commodityName + "</div></td>" +
				"<td style='width: 96px;'><div class='wrap' title='" + data.listSlave1[i].barcodes + "'>" + data.listSlave1[i].barcodes + "</div></td>" +
				"<td>" + data.listSlave1[i].priceOriginal.toFixed(2) + "</td>" +
				"<td>" + data.listSlave1[i].NO + "</td>" +
				"<td>" + (data.listSlave1[i].priceOriginal * data.listSlave1[i].NO).toFixed(2) + "</td>" +
				"</tr>"
			);
			totalAmount += data.listSlave1[i].priceOriginal * data.listSlave1[i].NO;
			payableAmount += data.listSlave1[i].priceReturn * data.listSlave1[i].NO;
		}
		discountAmount = totalAmount - payableAmount;
		$(".totalAmount td").eq(1).text(totalAmount.toFixed(2));		//合计总金额
		$(".discountAmount td").eq(1).text("-" + discountAmount.toFixed(2));		//优惠金额
		$(".payableAmount td").eq(1).text(payableAmount.toFixed(2));		//应付金额

		//获取支付方式的值
		var paymentType = paymentOfType(data.paymentType);		//解析支付方式
		$(".paymentType").remove();		//删除上一张零售单遗留的支付数据
		if (paymentType.indexOf("|") != -1) {		//不止一种支付方式
			var paymentTypeArray = paymentType.split("|");
			for (var i = 0; i < paymentTypeArray.length; i++) {
				paymentDetails_retailTrade(paymentTypeArray[i], data);
			}
		} else {		//只使用了一种支付方式
			paymentDetails_retailTrade(paymentType, data);
		}

		$(".change td").eq(1).text("0.00");		//找零
		$(".particulars_table table tbody tr").removeClass("odd");
		$(".particulars_table table tbody tr:odd").addClass("odd");		//表格隔行添加背景色
	})
	//零单支付明细
	function paymentDetails_retailTrade (paymentType, data) {
		$(".particulars_table table tbody tr:nth-last-child(1)").before(
			"<tr class='paymentType'>" +
				"<td colspan='4'>" + paymentType + "：</td>" +
				"<td></td>" +
			"</tr>"
		)
		switch (paymentType) {
			case "现金支付":
				$(".paymentType:last td").eq(1).text(data.amountCash.toFixed(2));
				break;
			case "微信支付":
				$(".paymentType:last td").eq(1).text(data.amountWeChat.toFixed(2));
				break;
			case "支付宝支付":
				$(".paymentType:last td").eq(1).text(data.amountAlipay.toFixed(2));
				break;
			case "其他支付方式1":
				$(".paymentType:last td").eq(1).text(data.amount1.toFixed(2));
				break;
			case "其他支付方式2":
				$(".paymentType:last td").eq(1).text(data.amount2.toFixed(2));
				break;
			case "其他支付方式3":
				$(".paymentType:last td").eq(1).text(data.amount3.toFixed(2));
				break;
			case "其他支付方式4":
				$(".paymentType:last td").eq(1).text(data.amount4.toFixed(2));
				break;
			case "其他支付方式5":
				$(".paymentType:last td").eq(1).text(data.amount5.toFixed(2));
				break;
			default:
				console.log("存在不支持解析的支付方式")
				break;
		}
	}

	//即时搜索事件
	window.instantSearch = function (event) {
		if (event.keyCode == "13" && event.key == "Enter") {
			queryRetailTradeSheet(true);
		} else {
			queryRetailTradeSheet(false);
		}
	}
	//模糊搜索销售记录
	$(".top_search i").click(function () {
		queryRetailTradeSheet(true);
	})
	//模糊搜索零售单的函数
	function queryRetailTradeSheet (showMsg) {
		var queryKeyword = $(".top_input").val();
		if (queryKeyword.length <= 32) {	//检查输入的关键字长度
			if (queryKeyword.length == $(".top_input").val().trim().length) {	//检查输入的关键字首尾是否有空格
				if (/^$|^[\u2014\u4E00-\u9FA5A-Za-z0-9_\()（）-\s]{1,32}$/.test(queryKeyword)) {	//检查输入的关键字是否满足数据格式要求
					//零售单SN格式:  LS+4位年2位月2位日+2位时2位分2位秒+4位pos_id+4位随机数，退货单再加_1。共26位。查询最小10位，可空串。LS可小写。
					if (queryKeyword.indexOf("LS") != -1 || queryKeyword.indexOf("L") != -1 || queryKeyword.indexOf("S") != -1 || queryKeyword.indexOf("ls") != -1 ||
						queryKeyword.indexOf("l") != -1 || queryKeyword.indexOf("s") != -1 || !isNaN(queryKeyword)) {  //检查是否存在零售单号的关键字眼或者是否为纯数字
						if (queryKeyword.length >= 10 || queryKeyword.length == 0) {
							if (queryKeyword.length > 26) {
								layer.msg("单据流水号长度不能大于26,且不能够小于10个字符");
								return;
							}
							data_reloadTable.queryKeyword = queryKeyword;
							bShowRetailTradeSummary = false;	//根据单号查询时不需要显示商品销售排行
						} else {
							if (showMsg) {
								layer.msg("数据格式不正确，零售单号至少需要输入10位", { id: "queryMsg" });
							}
							return;
						}
					} else {
						data_reloadTable.queryKeyword = queryKeyword;
						bShowRetailTradeSummary = true;
					}
					reloadTable(table, retailTradeRN_tableID, method_post, retailTradeN_url, curr_reloadTable, data_reloadTable);
				} else {
					layer.msg("数据格式不正确，请输入中英文、数字，只允许中间有空格，长度为(0,32]，支持输入的符号有：（）()_-——", { id: "queryMsg" });
				}
			} else {
				layer.msg("输入的关键字首尾不能有空格，只允许中间有空格", { id: "queryMsg" });
			}
		} else {
			//前端界面已经限制了输入的最大长度，故超出长度时不做处理和提示
			return;
		}
	}
})