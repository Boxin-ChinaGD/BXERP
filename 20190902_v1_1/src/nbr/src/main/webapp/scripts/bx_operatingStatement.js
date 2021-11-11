layui.use(['laydate', 'layer'], function () {

	var laydate = layui.laydate;
	var layer = layui.layer;

	const reportSummaryR1_URL = "retailTradeDailyReportSummary/retrieve1Ex.bx";		//表格数据
	const reportSummaryRN_URL = "retailTradeDailyReportSummary/retrieveNForChart.bx";		//月报表接口
	const MonthlyReportSummary_URL = "retailTradeMonthlyReportSummary/retrieveN.bx";		//年报表数据接口
	const retailTrade_url = "retailTradeDailyReportByCategoryParent/retrieveNEx.bx";		//销售大类数据接口
	const retailTradeDailyReportByStaff_url = "retailTradeDailyReportByStaff/retrieveNEx.bx";		//销售人员业绩数据接口
	const retailTradeDailyReportByCommodity_url = "retailTradeDailyReportByCommodity/retrieveN.bx";		//销售商品的排行数据
	const method_get = "GET";
	const mothod_post = "POST"
	const monthArrar = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];
	const dateMan = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

	var date1 = "";
	var date2 = "";

	//月报时间选择
	laydate.render({
		elem: '#datemonth', //指定元素
		type: 'month',
		format: 'yyyy/MM'
	});
	//年报时间选择
	laydate.render({
		elem: '#dateyear', //指定元素
		type: 'year',
		format: 'yyyy'
	});

	//页面初始化
	layer.ready(function () {
		var nowdate = new Date().format("yyyy/MM");
		var yeardate = new Date().format("yyyy");
		var newdate1 = new Date().format("yyyy/MM") + "/01 00:00:00";
		var newdate2 = new Date().format("yyyy/MM/dd") + " 23:59:59";
		$(".report_table table tr td").each(function (i) {
			$(this).find("div.sale_wrap").css({ "background": $(this).find("div.sale_wrap").attr("data-color") });
			$(this).find("div.sale_name").css({ "background": $(this).find("div.sale_name").attr("data-color") });
		});
		date(newdate1, newdate2, 2);
		mondate(newdate1, newdate2, 2);
		salesPie(newdate1, newdate2, 2);
		salesPerformanceOfSalesStaff(newdate1, newdate2, 2);
		salesRanking(newdate1, newdate2, 2);
		$(".salesTrends h5").text(nowdate + "月销售走势");
		$("#datemonth").val(nowdate);
	})

	//表格数据渲染
	function date (date1, date2, shopID) {
		var meandate;
		var ERROR = "EC_NoError";
		//表头数据	
		$.ajax({
			url: reportSummaryR1_URL,
			type: method_get,
			async: true,
			dataType: "json",
			data: { "datetimeStart": date1, "datetimeEnd": date2, "shopID": shopID },
			success: function succFunction (data) {
				console.log(data);
				//渲染表头数据
				if (data.ERROR == ERROR) {
					$(".totalAmount").text(data['retailTradeDailyReportSummary'][0][0]['totalAmount'].toFixed(2));
					$(".totalGross").text(data['retailTradeDailyReportSummary'][0][0]['totalGross'].toFixed(2));
					$(".totalsales").text(data['retailTradeDailyReportSummary'][0][0]['totalNO']);
					meandate = DateMinus(date1, date2);
					$(".averagedailysales").text((data['retailTradeDailyReportSummary'][0][0]['totalAmount'] / meandate).toFixed(2));
					$(".averagedailygrossprofit").text((data['retailTradeDailyReportSummary'][0][0]['totalGross'] / meandate).toFixed(2));
					$(".averagenumberofsalespeday").text((data['retailTradeDailyReportSummary'][0][0]['totalNO'] / meandate).toFixed(2));
				} else {
					$(".totalAmount").text("0.00");
					$(".totalGross").text("0.00");
					$(".totalsales").text("0.00");
					$(".averagedailysales").text("0.00");
					$(".averagedailygrossprofit").text("0.00");
					$(".averagenumberofsalespeday").text("0.00");
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg("服务器错误");
			}
		})
	}

	//月报表数据绘图
	function mondate (date1, date2, shopID) {
		var dateArrar = new Array();
		var totalAmountArrar = new Array();
		var totalGrossArrar = new Array();
		var sum = 0;
		$.ajax({
			url: reportSummaryRN_URL,
			type: method_get,
			async: true,
			dataType: "json",
			data: { "datetimeStart": date1, "datetimeEnd": date2, "shopID" : shopID },
			success: function succFunction (data) {
				console.log(data);
				
				var shopUl = document.getElementById("shop");
				for(var i = shopUl.childNodes.length - 1; i > 0; i--)
				{
					shopUl.removeChild(shopUl.childNodes[i]);
				}
				//
				for(var i=0;i<data.shopList.length;i++){
					var ele = document.createElement('li');
					ele.innerHTML = data.shopList[i].name;
					ele.id = data.shopList[i].ID;
					document.getElementById('shop').appendChild(ele);
				}
				
				$(".warehousingManageTop ul li").click(function () {
					var indexObject = $(this);
					queryEvents(indexObject);
				});
				
				//渲染绘图数据	
				if (data['retailTradeDailyReportSummary'].length > 0) {
					for (var i = date1.substring(8, 10); i <= DateMinus(date1, date2); i++) {
						$.each(data['retailTradeDailyReportSummary'], function (j, val) {
							if (i == parseInt(val['dateTime'].substring(8, 10))) {
								dateArrar[sum] = parseInt((val['dateTime']).substring(8, 10)) + "日";
								totalAmountArrar[sum] = val['totalAmount'].toFixed(2);
								totalGrossArrar[sum] = val['totalGross'].toFixed(2);
								return false;
							} else {
								dateArrar[sum] = parseInt(i) + "日";
								totalAmountArrar[sum] = "0.00";
								totalGrossArrar[sum] = "0.00";
							}

						});
						sum++;
					}
					sketch(dateArrar, totalAmountArrar, totalGrossArrar);//绘图
					sum = 0;
				} else {
					for (var i = date1.substring(8, 10); i <= DateMinus(date1, date2); i++) {
						//						alert(sum);
						dateArrar[sum] = parseInt(i) + "日";
						totalAmountArrar[sum] = "0.00";
						totalGrossArrar[sum] = "0.00";
						sum++;
					}
					sketch(dateArrar, totalAmountArrar, totalGrossArrar);//绘图
					sum = 0;
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg("服务器错误");
			}
		})
	}


	//年报表数据绘图
	function yeardate (date1, date2, shopID) {
		var totalAmountArrar = new Array();
		var totalGrossArrar = new Array();
		var sum = 0;
		$.ajax({
			url: MonthlyReportSummary_URL,
			type: mothod_post,
			async: true,
			dataType: "json",
			data: { "datetimeStart": date1, "datetimeEnd": date2, "shopID": shopID },
			success: function succFunction (data) {
				console.log(data);
				//渲染绘图数据		
				if (data['reportList'].length != 0) {
					for (var i = 1; i <= 12; i++) {
						$.each(data['reportList'], function (j, val) {
							if (i == val['dateTime'].substring(5, 7)) {
								totalAmountArrar[sum] = val['totalAmount'].toFixed(2);
								totalGrossArrar[sum] = val['totalGross'].toFixed(2);
								return false;
							} else {
								totalAmountArrar[sum] = "0.00";
								totalGrossArrar[sum] = "0.00";
							}

						});
						sum++;
					}
					sketch(monthArrar, totalAmountArrar, totalGrossArrar);//绘图
					sum = 0;
				} else {
					for (var i = 1; i <= 12; i++) {
						monthArrar[sum] = i + "月";
						totalAmountArrar[sum] = "0.00";
						totalGrossArrar[sum] = "0.00";
						sum++;
					}
					sketch(monthArrar, totalAmountArrar, totalGrossArrar);//绘图
					sum = 0;
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg("服务器错误");
			}
		})
	}


	//销售大类数据绘图
	function salesPie (date1, date2, shopID) {
		var categoryParentNameArr = [];
		var message = new Array();
		$.ajax({
			url: retailTrade_url,
			type: mothod_post,
			async: true,
			dataType: "json",
			data: { "datetimeStart": date1, "datetimeEnd": date2, "shopID": shopID },
			success: function succFunction (data) {
				console.log(data);
				if (data.ERROR == "EC_NoError") {
					if (data.reportList.length > 0) {
						for (var i = 0; i < data.reportList.length; i++) {
							var totalAmountSummaryarr = {};
							categoryParentNameArr[i] = data.reportList[i].totalAmountSummary.toFixed(2) < 0 ? data.reportList[i].categoryParentName + "(退货)" : data.reportList[i].categoryParentName;;
							totalAmountSummaryarr["value"] = data.reportList[i].totalAmountSummary.toFixed(2) < 0 ? Math.abs(data.reportList[i].totalAmountSummary.toFixed(2)) : data.reportList[i].totalAmountSummary.toFixed(2);
							totalAmountSummaryarr["name"] = data.reportList[i].totalAmountSummary.toFixed(2) < 0 ? data.reportList[i].categoryParentName + "(退货)" : data.reportList[i].categoryParentName;
							message.push(totalAmountSummaryarr);
						}
						$(".noData").remove();
						//绘图
						pie(categoryParentNameArr, message);
					} else {
						$(".noData").remove();
						pie(categoryParentNameArr, message);
						$("#pie").append("<div class='noData'><i class='layui-icon layui-icon-loading' style='font-size: 60px; color: #1E9FFF;'></i> <br>暂时无销售数据</div>");
					}
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg("服务器错误");
			}
		})

	}

	//销售人员的业绩绘图
	function salesPerformanceOfSalesStaff (date1, date2, shopID) {
		var staffName = [];
		var totalAmount = [];
		var NO = [];
		var grossMargin = [];
		$.ajax({
			url: retailTradeDailyReportByStaff_url,
			type: mothod_post,
			async: true,
			dataType: "json",
			data: { "datetimeStart": date1, "datetimeEnd": date2, "shopID": shopID },
			success: function succFunction (data) {
				console.log(data);
				if (data.ERROR == "EC_NoError") {
					if (data.retailTradeDailyReportByStaff.length > 0) {
						for (var i = 0; i < data.retailTradeDailyReportByStaff.length; i++) {
							staffName[i] = data.retailTradeDailyReportByStaff[i].staffName;
							totalAmount[i] = data.retailTradeDailyReportByStaff[i].totalAmount.toFixed(2);
							NO[i] = data.retailTradeDailyReportByStaff[i].NO;
							grossMargin[i] = data.retailTradeDailyReportByStaff[i].grossMargin.toFixed(2);
						}
						$(".performance").remove();
						results(staffName, totalAmount, NO, grossMargin);
					} else {
						$(".performance").remove();
						results(staffName, totalAmount, NO, grossMargin);
						$("#salesPerformance").append("<div class='performance'><i class='layui-icon layui-icon-loading' style='font-size: 60px; color: #1E9FFF;'></i> <br>暂无销售业绩</div>");
					}
				} else {
					if (data.msg == "" || data.msg) {
						layer.msg(服务器错误);
					} else {
						layer.msg(data.msg);
					}
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg("服务器错误");
			}
		})
	}

	//商品销售排行
	function salesRanking (date1, date2, shopID) {
		var commodityNo = [];
		var commodityName = [];
		$.ajax({
			url: retailTradeDailyReportByCommodity_url,
			type: mothod_post,
			async: true,
			dataType: "json",
			data: {
				"datetimeStart": date1,
				"datetimeEnd": date2,
				"shopID": shopID,
				"iCategoryID": -1,
				"bIgnoreZeroNO": 0,
				"iPageSize": 10,
				"isASC": 0,
				"iOrderBy": 1
			},
			success: function succFunction (data) {
				console.log(data);
				if (data.ERROR == "EC_NoError") {
					if (data.reportList.length > 0) {
						for (var i = 0; i < data.reportList.length; i++) {
							commodityNo[i] = data.reportList[i].NO;
							commodityName[i] = data.reportList[i].name;
						}
						$(".performances").remove();
						salesRank(commodityNo, commodityName);
					} else {
						$(".performances").remove();
						salesRank(commodityNo, commodityName);
						$("#salesRanking").append("<div class='performances'><i class='layui-icon layui-icon-loading' style='font-size: 60px; color: #1E9FFF;'></i> <br>暂无商品销售数据</div>");
					}
				} else {
					if (data.msg == "" || data.msg) {
						layer.msg(服务器错误);
					} else {
						layer.msg(data.msg);
					}
				}
			}
		})
	}
	//计算时间差
	function DateMinus (date1, date2) {
		var sdate = new Date(date1);
		var now = new Date(date2);
		var days = now.getTime() - sdate.getTime();
		var day = parseInt(days / (1000 * 60 * 60 * 24));
		return day + 1;
	}

	//判断闰年
	function isRunYear (fullYear) {
		return (fullYear % 4 == 0 && (fullYear % 100 != 0 || fullYear % 400 == 0));
	}

	//业绩图
	function sketch (arr, arr1, arr2) {
		var myChart = echarts.init($('#sell').get(0))

		// 指定图表的配置项和数据
		var trendsoption = {
			title: {

			},
			tooltip: {},
			grid: {//控制图表大小
				left: '4%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			legend: {
				data: ['销量额', '销售毛利'],
				y: 'top',
				icon: "circle"
			},
			xAxis: {
				data: arr
			},
			yAxis: {},
			series: [{
				name: '销量额',
				type: 'line',
				label: {
					normal: {
						show: true,
						position: 'top'
					}
				},
				data: arr1
			},
			{
				name: '销售毛利',
				type: 'line',
				label: {
					normal: {
						show: true,
						position: 'top'
					}
				},
				data: arr2
			}
			]
		};


		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(trendsoption);
	}


	//饼图
	function pie (categoryParentNameArr, totalAmountSummaryarr) {
		var myChart = echarts.init($('#pie').get(0))
		// 指定图表的配置项和数据
		option = {
			title: {
				text: '销售汇总',
				x: 'left'
			},
			tooltip: {
				trigger: 'item',
				formatter: "{a} <br/>{b} : {c} ({d}%)"
			},
			legend: {
				type: 'scroll',
				orient: 'vertical',
				right: 0,
				top: 50,
				bottom: 20,
				data: categoryParentNameArr,
				zlevel: 0,
			},
			series: [
				{
					name: '销售大类占比',
					type: 'pie',
					radius: '60%',
					center: ['40%', '60%'],
					data: totalAmountSummaryarr,
					zlevel: 1,
					itemStyle: {
						emphasis: {
							shadowBlur: 10,
							shadowOffsetX: 0,
							shadowColor: 'rgba(0, 0, 0, 0.5)'
						}
					},
					label: {//将数量与百分比一起显示
						show: true
						// 	normal: {
						// 		formatter: '{b}:{c}: ({d}%)',
						// 		textStyle: {
						// 			fontWeight: 'normal',
						// 			fontSize: 15
						// 		}
						// 	}
					},
				}
			]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	}

	//销售人员业绩图
	function results (staffName, totalAmount, NO, grossMargin) {
		var myChart = echarts.init($('#salesPerformance').get(0))
		// 指定图表的配置项和数据
		var posList = [
			'left', 'right', 'top', 'bottom',
			'inside',
			'insideTop', 'insideLeft', 'insideRight', 'insideBottom',
			'insideTopLeft', 'insideTopRight', 'insideBottomLeft', 'insideBottomRight'
		];

		myChart.configParameters = {
			rotate: {
				min: -90,
				max: 90
			},
			align: {
				options: {
					left: 'left',
					center: 'center',
					right: 'right'
				}
			},
			verticalAlign: {
				options: {
					top: 'top',
					middle: 'middle',
					bottom: 'bottom'
				}
			},
			position: {
				options: echarts.util.reduce(posList, function (map, pos) {
					map[pos] = pos;
					return map;
				}, {})
			},
			distance: {
				min: 0,
				max: 100
			}
		};

		myChart.config = {
			rotate: 90,
			align: 'left',
			verticalAlign: 'middle',
			position: 'insideBottom',
			distance: 15,
			onChange: function () {
				var labelOption = {
					normal: {
						rotate: app.config.rotate,
						align: app.config.align,
						verticalAlign: app.config.verticalAlign,
						position: app.config.position,
						distance: app.config.distance
					}
				};
				myChart.setOption({
					series: [{
						label: labelOption
					}, {
						label: labelOption
					}, {
						label: labelOption
					}, {
						label: labelOption
					}]
				});
			}
		};


		var labelOption = {
			normal: {
				show: true,
				position: myChart.config.position,
				distance: myChart.config.distance,
				align: myChart.config.align,
				verticalAlign: myChart.config.verticalAlign,
				rotate: myChart.config.rotate,
				formatter: '{c}  {name|{a}}',
				fontSize: 14,
				rich: {
					name: {
						textBorderColor: '#fff'
					}
				}
			}
		};

		option = {
			title: {
				text: '销售业绩对比图',
				x: 'left'
			},
			color: ['#003366', '#006699', '#4cabce'],
			tooltip: {
				trigger: 'axis',
				axisPointer: {
					type: 'shadow'
				}
			},
			grid: {//控制图表大小
				left: '2%',
				right: '2%',
				bottom: '1%',
				containLabel: true
			},
			legend: {
				data: ['销售总额', '销售毛利', '销售笔数']
			},
			// toolbox: {
			// 	show: true,
			// 	orient: 'vertical',
			// 	left: 'right',
			// 	top: 'center',
			// 	feature: {
			// 		mark: { show: true },
			// 		dataView: { show: true, readOnly: false },
			// 		magicType: { show: true, type: ['line', 'bar', 'stack', 'tiled'] },
			// 		restore: { show: true },
			// 		saveAsImage: { show: true }
			// 	}
			// },
			calculable: true,
			xAxis: [
				{
					type: 'category',
					axisTick: { show: false },
					data: staffName,
					axisPointer: {
						type: "shadow"
					}
				}
			],
			yAxis: [
				{
					type: 'value',
					name: '销售金额',
					min: 0,
					max: function (value) {
						return value.max;
					},
					axisLabel: {
						formatter: '{value}'
					},
					nameLocation: 'center',
					nameGap: 50,
					splitLine: { show: false },
					axisLine: {
						lineStyle: {
							color: {
								type: 'linear',
								x: 0,
								y: 0,
								x2: 0,
								y2: 1,
								colorStops: [{
									offset: 0, color: '#003366' // 0% 处的颜色
								}, {
									offset: 1, color: '#4cabce' // 100% 处的颜色
								}],
								global: false // 缺省为 false
							}
						}
					}
				},
				{
					type: 'value',
					name: '销售笔数',
					min: 0,
					max: function (value) {
						return value.max;
					},
					axisLabel: {
						formatter: '{value}'
					},
					nameLocation: 'center',
					nameGap: 30,
					nameRotate: -90,
					nameTextStyle: {
						color: "#4cabce"
					},
					splitLine: { show: false },
					axisLine: {
						lineStyle: {
							color: "#4cabce"
						}
					}
				}
			],
			series: [
				{
					name: '销售总额',
					type: 'bar',
					barGap: 0,
					barWidth: 30,
					label: labelOption,
					data: totalAmount
				},
				{
					name: '销售毛利',
					type: 'bar',
					barWidth: 30,
					label: labelOption,
					data: grossMargin
				},
				{
					name: '销售笔数',
					type: 'bar',
					barWidth: 30,
					label: labelOption,
					data: NO,
					yAxisIndex: 1,

				}
			]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	}

	function salesRank (commodityNo, commodityName) {
		var myChart = echarts.init($("#salesRanking").get(0));
		option = {
			title: {
				text: '商品销售排行',
				x: 'left'
			},
			color: ['#3398DB'],
			tooltip: {
				trigger: 'axis',
				axisPointer: {            // 坐标轴指示器，坐标轴触发有效
					type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			grid: {
				left: '3%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			xAxis: [
				{
					type: 'category',
					data: commodityName,
					axisTick: {
						alignWithLabel: true,
						interval: 0
					},
					axisLabel: {
						interval: 0,
						rotate: -10
					}
				}
			],
			yAxis: [
				{
					type: 'value'
				}
			],
			series: [
				{
					name: '商品数量',
					type: 'bar',
					barWidth: '20%',
					data: commodityNo,
					itemStyle: {
						normal: {
							label: {
								show: true, //开启显示
								position: 'top', //在上方显示
								textStyle: { //数值样式
									color: 'black',
									fontSize: 12
								}
							}
						}
					}
				}
			]
		};
		// 使用刚指定的配置项和数据显示图表。
		console.log(option);
		myChart.setOption(option);
	}


	//选项卡跳转
	$("#top_div1 ul li").click(function () {
		$(this).addClass('default').siblings().removeClass("default");
		if ($(this).index() == 0) {
			$("#datemonth").show();
			$("#dateyear").hide();
			$("#datemonth").val(new Date().format("yyyy/MM"));
			var newdate1 = new Date().format("yyyy/MM") + "/01 00:00:00";
			var newdate2 = new Date().format("yyyy/MM/dd") + " 23:59:59";
			date(newdate1, newdate2, 2);
			mondate(newdate1, newdate2, 2);
			salesPie(newdate1, newdate2, 2);
			salesPerformanceOfSalesStaff(newdate1, newdate2, 2)
			salesRanking(date1, date2, 2);
			$(".salesTrends h5").text(new Date().format("yyyy/MM") + "月销售走势");
		} else if ($(this).index() == 1) {
			$("#dateyear").show();
			$("#datemonth").hide();
			var date1 = new Date().format("yyyy") + "/01/01 00:00:00"
			var date2 = new Date().format("yyyy") + "/12/31 23:59:59"
			date(date1, date2, 2);
			yeardate(date1, date2, 2);
			salesPie(date1, date2, 2);
			salesPerformanceOfSalesStaff(date1, date2, 2);
			salesRanking(date1, date2, 2);
			$(".salesTrends h5").text(new Date().format("yyyy") + "年销售走势");
			$("#dateyear").val(new Date().format("yyyy"));
		}
	})
	//回车发起搜索
	$(document).keypress(function (e) {
		if (e.keyCode == 13) {
			$("#btn").click();
		}
	})
	//根据时间查询
	$("#btn").click(function () {
		if ($("#top_div1 ul li").eq(0).hasClass("default")) {
			if ($("#datemonth").val() == "") {
				$("#datemonth").focus();
			}
			date1 = $("#datemonth").val() + '/01 00:00:00';	//月报默认按照1号到月末
			if (isRunYear(($("#datemonth").val()).substring(0, 4))) {//闰年的月末处理
				if (($("#datemonth").val()).substring(5, 7) - 1 == 1) {
					date2 = $("#datemonth").val() + "/" + (dateMan[($("#datemonth").val()).substring(5, 7) - 1] + 1) + " 23:59:59";
				} else {
					date2 = $("#datemonth").val() + "/" + dateMan[($("#datemonth").val()).substring(5, 7) - 1] + " 23:59:59";
				}
			} else {//非闰年的月末处理
				date2 = $("#datemonth").val() + "/" + dateMan[($("#datemonth").val()).substring(5, 7) - 1] + " 23:59:59";
			}
			date(date1, date2, 2);
			mondate(date1, date2, 2);
			salesPie(date1, date2, 2);
			salesPerformanceOfSalesStaff(date1, date2, 2);
			salesRanking(date1, date2, 2);
			$(".salesTrends h5").text($("#datemonth").val() + "月销售走势");
		} else if ($("#top_div1 ul li").index() == 1) {
			if ($("#dateyear").val() == "") {
				$("#dateyear").focus();
			}
			date1 = $("#dateyear").val() + '/01/01 00:00:00';	//月报默认按照1号到月末
			date2 = $("#dateyear").val() + "/12/31 23:59:59";
			date(date1, date2, 2);
			yeardate(date1, date2, 2);
			salesPie(date1, date2, 2);
			salesPerformanceOfSalesStaff(date1, date2, 2);
			salesRanking(date1, date2, 2);
			$(".salesTrends h5").text($("#dateyear").val() + "年销售走势");
		}

	});
	
	function queryEvents (indexObject) {		//头部导航栏查询功能
		indexObject.parent().prev().children().text(indexObject.text() + " v");
		$(".warehousingManageTop label span").click();
		var keyword = indexObject.attr("id");
		var nowdate = new Date().format("yyyy/MM");
		var yeardate = new Date().format("yyyy");
		var newdate1 = new Date().format("yyyy/MM") + "/01 00:00:00";
		var newdate2 = new Date().format("yyyy/MM/dd") + " 23:59:59";
		$(".report_table table tr td").each(function (i) {
			$(this).find("div.sale_wrap").css({ "background": $(this).find("div.sale_wrap").attr("data-color") });
			$(this).find("div.sale_name").css({ "background": $(this).find("div.sale_name").attr("data-color") });
		});
		date(newdate1, newdate2, keyword);
		mondate(newdate1, newdate2, keyword);
		salesPie(newdate1, newdate2, keyword);
		salesPerformanceOfSalesStaff(newdate1, newdate2, keyword);
		salesRanking(newdate1, newdate2, keyword);
		$(".salesTrends h5").text(nowdate + "月销售走势");
		$("#datemonth").val(nowdate);
	}

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
});



