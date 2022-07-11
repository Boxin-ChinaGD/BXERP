layui.use([ 'form', 'layer' ], function(){
    var form = layui.form;
    var layer = layui.layer;

    const purchaseR1_url = "purchasingOrder/retrieve1Ex.bx"; 		//采购订单详情路径
    const purchaseRapprove_url = "purchasingOrder/approveEx.bx";		//审核订单
    var purchasingOrdeID =null;
    //页面初始化
	layer.ready(function(){
		purchasingOrdeID = $("#purchasingOrdeID").val();
		
        purchasingOrderR1(purchasingOrdeID);
    });
    //采购订单列表点击订单列表点击事件
	function purchasingOrderR1(ID){
		var indexLoading = layer.load(1);
		var numberSummary = null;
		var priceSummary = null;
		popupCommodityData = [];		//临时数组清零
		$.ajax({
			url: purchaseR1_url,
			type: 'get',
			async: true,
			dataType: "json",
			data: { "ID": ID},
			success: function succFunction(data) {
				console.log(data);
				if(data.ERROR == "EC_NoError"){
                    $(".provider-name").html(data.object.providerName+"<input type='hidden' value='"+data.object.providerID+"' id='providerID'/>");
                    $(".purchasingOrderSN-SN").text(data.object.sn);
                    if(data.object.status != 0){
                        $(".approvalBtn").attr("disabled","disabled");
                        $(".approvalBtn").css({"background":"rgb(190, 188, 188)"});
                    }
                    $.each(data['object']['listSlave1'], function(i, val) {
                        $(".commodityList table tbody").append(
                            '<tr>'+
                                '<td  style="width:60px;">'+(i+1)+'</td>'+
                                '<td><div class="wrap">'+val.commodityName+'</div></td>'+
                                '<td><div class="wrap">'+val.barcode+'</div></td>'+
                                '<td>'+val.commodityNO+'</td>'+
                                '<td>'+val.priceSuggestion.toFixed(2)+'</td>'+
                            '</tr>'
                        );
                        var commID = data['object']['listCommodity'][i]['ID'];
                        var commName = data['object']['listSlave1'][i]['commodityName'];
                        var barcodeID = data['object']['listSlave1'][i]['barcodeID'];
                        var barcodeName = data['object']['listSlave1'][i]['barcode'];
                        var packageUnitID = data['object']['listSlave1'][i]['packageUnitID'];
                        var packageUnitName = data['object']['listSlave1'][i]['packageUnitName'];
                        var NO = (data['object']['listSlave1'][i]['commodityNO'] - data['object']['listSlave1'][i]['warehousingNO']) <= 0 ? 0:data['object']['listSlave1'][i]['commodityNO'] - data['object']['listSlave1'][i]['warehousingNO'];
                        var price = data['object']['listSlave1'][i]['priceSuggestion'];
						var specification = data['object']['listSlave1'][i]['specification'];
						//计算数量：
						priceSummary += val.priceSuggestion * val.commodityNO;
						numberSummary += val.commodityNO;
                        popupCommDataManage("add", commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, specification);
					})
					$(".numberSummary-text").text(numberSummary);
					$(".priceSummary-text").text((priceSummary).toFixed(2));
                    layer.close(indexLoading);
                    $("div.commodityList table tbody tr:odd").addClass("odd");	
                }
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.close(indexLoading);
				layer.open({
					time:0, 
					title:false,
					shadeClose:false, 
					btn:false,
					closeBtn:false, 
					content: '<div class="MsgStyle">服务器错误,请关闭页面重新打开</div>'
				  })
			}
		})
    }

    $(".approvalBtn").click(function(){
        purchaseOrderManage();
    });
    // 采购订单管理
	function purchaseOrderManage(){
		if(popupCommodityData.length > 0){
			var providerID = $("#providerID").val();
			var commIDs = "";
			var barcodeIDs = "";
			var commNOs = "";
			var commPrices = "";
			var remark = '';
			for (var i=0; i<popupCommodityData.length; i++){
				commIDs += popupCommodityData[i].commID + ",";
				barcodeIDs += popupCommodityData[i].barcodeID + ",";
				commNOs += popupCommodityData[i].NO + ",";
				commPrices += popupCommodityData[i].price + ",";
			}
			$.ajax({
				url: purchaseRapprove_url,
				type: 'post',
				async: true,
				dataType: "json",
				data: {
					"providerID": providerID, 		// 供应商ID
					"remark": remark, 		// 采购总结
					"commIDs": commIDs, 		// 商品ID
					"barcodeIDs": barcodeIDs, 		// 条形码ID
					"NOs": commNOs,		 // 采购数量
					"priceSuggestions": commPrices,		// 采购价格
					"ID": purchasingOrdeID
				},
				success: function succFunction(data) {
					console.log(data);
					if(data.ERROR == "EC_NoError"){
						layer.open({
							time:0, 
							title:false,
							shadeClose:false, 
							btn:false,
							closeBtn:false, 
							content: '<div class="MsgStyle">审核成功</div>'
						  })
                        $(".approvalBtn").attr("disabled","disabled");
                        $(".approvalBtn").css({"background":"rgb(190, 188, 188)"});
					}else{
						if(data.msg){
							layer.msg(data.msg);
						}else{
							layer.msg(defeatedMsg);
						}
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					layer.open({
						time:0, 
						title:false,
						shadeClose:false, 
						btn:false,
						closeBtn:false, 
						content: '<div class="MsgStyle">服务器错误,请关闭页面重新打开</div>'
					  })
				}
			});
		}else{
			layer.open({
				time:0, 
				title:false,
				shadeClose:false, 
				btn:false,
				closeBtn:false, 
				content: '<div class="MsgStyle">无商品</div>'
			  })
		}
	}
})