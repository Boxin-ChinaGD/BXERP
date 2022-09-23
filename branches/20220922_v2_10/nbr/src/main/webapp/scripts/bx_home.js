layui.use(['element', 'layer'], function () {
	var element = layui.element;
	var layer = layui.layer;
	//定义常用常量
	const saleReport_url = 'retailTradeDailyReportSummary/retrieveNForChart.bx';

	//页面加载后执行
	$(document).ready(function () {
		//绘制销售趋势图
		var newdate1 = new Date().format("yyyy/MM") + "/01";
		var newdate2 = new Date().format("yyyy/MM/dd");
		$(".footerText span").text(new Date().format("yyyy"));
		mondate(newdate1, newdate2, 2);
		//显示当天销售概况标题
		$(".salesOverviewOfTheDay h5").text(newdate2 + "销售概况");

		//实现底部宽度调整
		var width = $("#homeMain").width();
		$(".footer").css("width", width + "px");
	})

	//一打开页面就执行弹层
	layer.ready(function () {
	})

	var arr = new Array();
	var arr1 = new Array();
	var arr2 = new Array();
	//月报表数据绘图
	function mondate (date1, date2, shopID) {
		var sum = 0;
		$.ajax({
			url: saleReport_url,
			type: "GET",
			async: true,
			dataType: "json",
			data: { "datetimeStart": date1, "datetimeEnd": date2, "shopID": shopID },
			success: function succFunction (data) {
				console.log(data);
				if (data.ERROR != "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg("查询月报数据出错");
					}
					return;
				}
				//显示当天销售概况
				$(".salesOverviewOfTheDay .totalAmount span").text(data.totalAmount);
				$(".salesOverviewOfTheDay .totalNO span").text(data.totalNO);
				//
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
							arr[sum] = parseInt(i) + "日";
							if (i == parseInt(val['dateTime'].substring(8, 10))) {
								//								arr[sum] = parseInt((val['dateTime']).substring(8,10)) + "日";
								arr1[sum] = val['totalAmount'];
								arr2[sum] = val['totalGross'];
								return false;
							} else {
								//								arr[sum] = parseInt(i)+"日";
								arr1[sum] = "0.00";
								arr2[sum] = "0.00";
							}
						});
						sum++;
					}
					sketch(arr, arr1, arr2);		//绘图
					//					sum=0;
				} else {
					for (var i = date1.substring(8, 10); i <= DateMinus(date1, date2); i++) {
						arr[sum] = parseInt(i) + "日";
						arr1[sum] = "0.00";
						arr2[sum] = "0.00";
						sum++;
					}
					sketch(arr, arr1, arr2);		//绘图
					//					sum=0;
				}
				$(".salesTrends h5").html(new Date().format("yyyy-MM") + " 本月份销售走势");
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		})
	}
	//业绩图
	function sketch (arr, arr1, arr2) {
		var myChart = echarts.init($('#sell').get(0));
		// 指定图表的配置项和数据
		var trendsoption = {
			title: {},
			tooltip: {},
			grid: {		//控制图表大小
				left: '4%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			legend: {
				data: ['销量额', '销售毛利'],
				y: 'top',
				icon: "circle",
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
						position: 'top',
						color: 'rgb(0, 238, 170)'
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
						position: 'top',
						color: '#2196F3'
					}
				},
				data: arr2
			}]
		};
		myChart.setOption(trendsoption);// 使用刚指定的配置项和数据显示图表。
	}
	//计算时间差
	function DateMinus (date1, date2) {
		var sdate = new Date(date1);
		var now = new Date(date2);
		var days = now.getTime() - sdate.getTime();
		var day = parseInt(days / (1000 * 60 * 60 * 24));
		return day + 1;
	}

	window.onresize = function () {
		var width = $("#homeMain").width();
		if (width >= 660) {
			$(".footer").css("width", width + "px");
			$("#sell").remove();
			$(".salesTrends").append('<div id="sell"></div>');
			sketch(arr, arr1, arr2);
		} else {
			//比最小限制宽度还小时不要求处理
		}
	}
	

	
	function queryEvents (indexObject) {		//头部导航栏查询功能
		indexObject.parent().prev().children().text(indexObject.text() + " v");
		$(".warehousingManageTop label span").click();
		var keyClass = indexObject.parent().attr("class");
		var keyword = indexObject.attr("id");
		showFirstDataOfTable = true;
		var newdate1 = new Date().format("yyyy/MM") + "/01";
		var newdate2 = new Date().format("yyyy/MM/dd");
		mondate(newdate1, newdate2, keyword);
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