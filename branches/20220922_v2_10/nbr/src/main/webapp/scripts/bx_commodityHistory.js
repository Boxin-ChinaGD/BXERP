layui.use(['element', 'layer','form' ,'table','laydate'], function(){
	var element = layui.element;
	var layer = layui.layer;
	var form = layui.form;
	var table = layui.table;
	var laydate = layui.laydate;
	//定义常量
	const commodityHistoryRN_url = "commodityHistory/retrieveNEx.bx";		//查询商品修改记录接口
	const commodityR1_url = "commodity/retrieve1Ex.bx";		//查询商品详情接口
	const commHistory_tableID = "commodityHistoryList";		//商品修改记录表格ID
	const where_reloadTable = {};		//商品搜索修改记录传参
	const method_get = "GET";		//get请求方式
	const method_post = "POST";		//post请求方式
	const curr_reloadTable = 1;		//默认页码
	
	var date = new Date();
	var nowYear = date.getFullYear(), nowMonth = date.getMonth() + 1, nowDate = date.getDate();
	var staffID = "";		//操作人ID
	var date1 = "1970/01/01 00:00:00";		//开始时间
	var date2 = new Date(date.getTime() + 168 * 60 * 60 * 1000).format("yyyy/MM/dd hh:mm:ss");	//今天
	//页面初始加载层
	layer.ready(function(){
		where_reloadTable.date1 = date1;
		where_reloadTable.date2 = date2;
	});
	//表单数据验证(模糊搜索字段的验证)
	fieldFormat(form);
	//操作人列表
	$("#operator").click(function(){
		window.event.stopPropagation();		//阻止冒泡事件
		if($("#operatorList").is(":hidden")){
			$("#operatorList").show();
		}else{
			$("#operatorList").hide();
		}
		$("#updateDateList").hide();
		$("#shopList").hide();
	});
	//操作人事件
	$("#operatorList li").click(function(){
		var loading = layer.load(1);
		$("#operator").text($(this).text() + " v");
		$("#operatorList").hide();
		staffID = $(this).val();
		where_reloadTable.staffID = staffID;
		reloadTable(table, commHistory_tableID, method_post, commodityHistoryRN_url, curr_reloadTable, where_reloadTable);
		layer.close(loading);
	});
	//门店列表
	$("#shop").click(function(){
		window.event.stopPropagation();		//阻止冒泡事件
		if($("#shopList").is(":hidden")){
			$("#shopList").show();
		}else{
			$("#shopList").hide();
		}
		$("#updateDateList").hide();
		$("#operatorList").hide();
	});
	//门店事件
	$("#shopList li").click(function(){
		var loading = layer.load(1);
		$("#shop").text($(this).text() + " v");
		$("#shopList").hide();
		shopID = $(this).val();
		where_reloadTable.shopID = shopID;
		reloadTable(table, commHistory_tableID, method_post, commodityHistoryRN_url, curr_reloadTable, where_reloadTable);
		layer.close(loading);
	});
	//修改日期列表
	$("#updateDate").click(function(){
		window.event.stopPropagation();		//阻止冒泡事件
		if($("#updateDateList").is(":hidden")){
			$("#updateDateList").show();
			var top = $("#updateDate").offset().top + 18;
			var left = $("#updateDate").offset().left - 8;
			$("#updateDateList").css({"top" : top, "left" : left});
		}else{
			$("#updateDateList").hide();
		}
		$("#operatorList").hide();
		$("#shopList").hide();
	});
	//修改日期列表事件
	$("#updateDateList li").click(function(){
		var loading = layer.load(1);
		$("#updateDate").text($(this).text()+" v");
		$("#updateDateList").hide();
		if($(this).text() == "全部"){
			date1 = "1970/1/1 00:00:00";
		}else if($(this).text() == "今天"){
			date1 = nowYear + "/" + nowMonth + "/" + nowDate + " 00:00:00"; 
		}else if($(this).text() == "七天"){
			var sevenDaysAgo = new Date(date.getTime() - 168*60*60*1000);
			date1 = sevenDaysAgo.format("yyyy/MM/dd") + " 00:00:00";
		}else if($(this).text() == "三个月"){
			var threeMonthsAgo = new Date(nowYear, nowMonth - 3, 1);		//三月前
			date1 = threeMonthsAgo.format("yyyy/MM/dd") + " 00:00:00";
		}else if($(this).text() == "半年"){
			var monthStartDate = new Date(nowYear, nowMonth - 1, nowDate - 180);
			date1 = monthStartDate.format("yyyy/MM/dd") + " 00:00:00";
		}
		where_reloadTable.date1 = date1;
		where_reloadTable.date2 = date2;
		reloadTable(table, commHistory_tableID, method_post, commodityHistoryRN_url, curr_reloadTable, where_reloadTable);
		layer.close(loading);
	});
	//隐藏头部下拉选项
	$("body").click(function(){
		$("#operatorList").hide();
		$("#updateDateList").hide();
	});
	//渲染表格 commHistoryList
	table.render({
		elem: '#commodityHistoryList',
		url: commodityHistoryRN_url,
		id: commHistory_tableID,
		method: method_post,
		where: where_reloadTable,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList',
		},
		limit: '10',
		page: true,
		even: 'true',
		size: 'sm',
		cols: [ [ 
			{ field: 'name', title: '商品名称', width: '15%', align: 'center', event: "commodityName",
				templet: function(data){
					return data.commodityName + '<input type="hidden" class="commodityID" value="' + data.commodityID + '"/>';
				}
			},
			{ field: 'barcode', title: '商品条码', width: '20%', align: 'center', sort: true, event: "commodityName",
				templet: function(data){
					return data.barcodes + '<input type="hidden" class="ID" value="' + data.ID + '"/>';
				}
			},
			{ field: 'datetime', title: '修改时间', width: '15%', align: 'center', sort: true, event: "commodityName",
				templet: function(data){
					var datetime = data.datetime;
					datetime = datetime.substring(0, datetime.length-4);
					return datetime;
				}
			},
			{ field: 'staffName', title: '操作人员', width: '10%', align: 'center', sort: true, event: "commodityName" },
			{ field: 'shopName', title: '门店', width: '10%', align: 'center', sort: true, event: "commodityName" },
			{ field: 'updateInfo', title: '修改内容', width: '30%', align: 'center', event: "commodityName", templet: "#tableUpdateInfo" }
		] ],
		done: function(res, curr, count){
			layer.closeAll('loading');
			if(res.ERROR != "EC_NoError"){
				var msg = res.msg == "" ? "查询商品修改记录失败" : res.msg;
				layer.msg(msg);
			}else{
				var resData = res.objectList;		//商品历史记录数据
				if(resData.length > 0){		//进入页面后显示第一条数据，添加第一条样式
					var commodityID = resData[0].commodityID;
					form.val("commodityDetails", {		//商品历史记录ID
						"ID": resData[0].ID,
					});
					commodityDetails(commodityID);
		    		var ID = $(".commodityHistoryBottomRight .ID").val();		//获取此时的修改记录ID
		        	$(".commodityHistoryList div tbody tr").each(function(){
		        		if($(this).children().next().children().children().val() == ID){
		        			$(this).addClass("layui-table-click");
		        		}
		        	});
				}else{
					layer.msg("查无商品修改记录");
					cleanUpCommodityInfo();
				}
			}
		}
	});
	//监听表格数据操作commHistoryList
	table.on('tool(commodityHistoryList)', function(obj) {
		var data = obj.data;
		var layEvent = obj.event;
		var commodityID = data.commodityID;
		switch (layEvent){
			case "commodityName":
				form.val("commodityDetails", {		//商品历史记录ID
					"ID": data.ID,
				});
				commodityDetails(commodityID);
			break;
		}
	})
	//重置商品详情信息
	function commodityDetails(ID){
		console.log(ID);
		var loading = layer.load(1);
		$.ajax({
			url: commodityR1_url,
			type: method_get,
			async: true,
			data: {"ID": ID},
			dataType: "json",
			success: function succFunction(json) {
				console.log(json);
				if(json.ERROR != "EC_NoError"){
					if(json.msg){
						layer.msg(json.msg);
					}else{
						layer.msg("查看商品详情失败");
					}
					if(json.ERROR == "EC_NoSuchData"){
						cleanUpCommodityInfo();
					}
				}else{
					$("#units").removeAttr("disabled");
					//获取多供应商
					var commodityInfo = json.object;		//获取到的商品信息
					var providerArray = commodityInfo.providerName.split(" ");		//获取到的供应商数据
					var barcodesArray = commodityInfo.barcodes.split(" ");		//获取到的条形码数据
					form.val("commodityDetails", {
						"commodityID": commodityInfo.ID,
						"barcodes": barcodesArray[0],
						"brandName": commodityInfo.brandName,
						"name": commodityInfo.name,
						"specification": commodityInfo.specification,
						"shortName": commodityInfo.shortName,
						"categoryName": commodityInfo.categoryName,
						"priceRetail": commodityInfo.listSlave2[0].priceRetail,	
						"packageUnit": commodityInfo.packageUnitName,
						"priceVIP": commodityInfo.priceVIP,
						"providerName": providerArray[0],
						"priceWholesale": commodityInfo.priceWholesale,
						"NO": commodityInfo.listSlave2[0].NO,
						"shelfLife": commodityInfo.shelfLife,
					});
					if(commodityInfo.type == 0){
						$("#units").parent().show();
						$(".providerName").parent().parent().show();
						$("#packageUnits div:first").html("<h5>多包装基本单位&nbsp;&nbsp; <span>" + commodityInfo.packageUnitName + "</span></h5>");
						$("#packageUnits .multiPackageThead").html('<th style="min-width: 48px;">类别</th><th>' + commodityInfo.packageUnitName + '</th>');
						$("#packageUnits .multiPackagePriceRetail").html('<td>零售价</td><td>' + commodityInfo.priceRetail + '</td>');
						$("#packageUnits .multiPackagePriceVIP").html('<td>会员价</td><td>' + commodityInfo.priceVIP + '</td>');
						$("#packageUnits .multiPackagePriceWholesale").html('<td>批发价</td><td>' + commodityInfo.priceWholesale + '</td>');
						$("#packageUnits .multiPackageNO").html('<td>商品库存</td><td>' + commodityInfo.NO + '</td>');
						$("#packageUnits .multiPackageBarcode").html('<td>条形码</td><td>' + commodityInfo.barcodes + '</td>');
						$("#packageUnits .multiPackageCommName").html('<td>商品名称</td><td>' + commodityInfo.name + '</td>');
						//条形码的处理
						$("#otherBarcodesList").html("");
						if(barcodesArray.length > 2){
							for(var i=1; i<barcodesArray.length-1; i++){
								$("#otherBarcodesList").append('<input readonly="readonly" class="layui-input" value="' + barcodesArray[i] + '"/>')
							}
							$("#otherBarcodes").show();
						}else{
							$("#otherBarcodes").hide();
						}
						//获供应商的处理
						if(providerArray.length > 2){
							$(".commodityHistoryBottomRight a").show();
							$("#otherProvider .otherProviderList").html("");
							for(var i=1; i<providerArray.length-1; i++){
								$("#otherProvider .otherProviderList").append('<input readonly="readonly" class="layui-input" value="' + providerArray[i] + '"/>')
							}
						}else{
							$(".commodityHistoryBottomRight a").hide();
							$("#otherProvider").hide();
						}
						//多包装的判断
						if(json.objectList.length > 0){
							$("#units").attr("checked", "checked");		//多单位勾选
							$("#packageUnits").show();
							for(var i=0; i<json.objectList.length; i++){
								$("#packageUnits div:first").append("<h5><span style='color: #888888;'>副单位" + (i+1)+ " </span> " 
										+ json.objectList[i].packageUnitName  + " <span> = &nbsp;" + json.objectList[i].refCommodityMultiple + " " + json.object.packageUnitName + "</span></h5>");
								$("#packageUnits .multiPackageThead").append('<th>' + json.objectList[i].packageUnitName + '</th>');
								$("#packageUnits .multiPackagePriceRetail").append('<td>' + json.objectList[i].priceRetail + '</td>');
								$("#packageUnits .multiPackagePriceVIP").append('<td>' + json.objectList[i].priceVIP + '</td>');
								$("#packageUnits .multiPackagePriceWholesale").append('<td>' + json.objectList[i].priceWholesale + '</td>');
								$("#packageUnits .multiPackageNO").append('<td>' + json.objectList[i].NO + '</td>');
								$("#packageUnits .multiPackageBarcode").append('<td>' + json.objectList[i].barcodes + '</td>');
								$("#packageUnits .multiPackageCommName").append('<td>' + json.objectList[i].name + '</td>');
							}
						}else{
							$("#packageUnits").hide();
							$("#units").removeAttr("checked");
						}
					}else if(commodityInfo.type === 2){
						$("#packageUnits").hide();
						$("#units").parent().hide();
						$("#otherBarcodes, #otherProvider").hide();
						$(".providerName").parent().parent().show();
					}else if(commodityInfo.type == 1){
						$("#packageUnits").hide();
						$("#units").parent().hide();
						$("#otherBarcodes, #otherProvider").hide();
						$(".providerName").parent().parent().show();
					}else if(commodityInfo.type == 3){
						$("#packageUnits").hide();
						$("#units").parent().hide();
						$("#otherBarcodes, #otherProvider").hide();
						$(".providerName").parent().parent().hide();
					}
				}
				layer.close(loading);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.close(loading);
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		});
	}
	function cleanUpCommodityInfo(){
		form.val("commodityDetails", {
			"ID": "",
			"commodityID": "",
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
			"shelfLife": ""
		});
		$("#otherBarcodes").hide();
		$(".commodityHistoryBottomRight .providerName").next().hide();
		if($("#units").is(":checked")){
			$("#units").click();
	 	}
		$("#units").attr("disabled", "disabled");
 		$("#packageUnits").hide();
	}
	//多单位列表的显示
	$("#units").click(function(){
		if($(this).is(":checked")){
			$("#packageUnits").show();
		}else{
			$("#packageUnits").hide();
		}
	}); 
	//多供应商的显示
	$(".commodityHistoryBottomRight .layui-form a").click(function(){
		window.event.stopPropagation();		//阻止冒泡事件
		if($("#otherProvider").is(":hidden")){
			$("#otherProvider").show();
		}else{
			$("#otherProvider").hide();
		}
	});
	// 模糊搜索商品（搜索名称、7位数及以上的条码）
	form.on('submit(commodityHistorySearch)', function(data){
		var queryKeyword = data.field.queryKeyword;
		if(queryKeyword.length <= 64){
			var loading = layer.load(1);
			where_reloadTable.queryKeyword = queryKeyword;
			reloadTable(table, commHistory_tableID, method_post, commodityHistoryRN_url, curr_reloadTable, where_reloadTable);
			layer.close(loading);
		}
	});
	//按键弹起时触发模糊搜索事件  
	$("div.commodityHistoryBottomLeft input.commodityHistorySearch").keyup(function(){
		$("i.commodityHistorySearch").click();
	});
})