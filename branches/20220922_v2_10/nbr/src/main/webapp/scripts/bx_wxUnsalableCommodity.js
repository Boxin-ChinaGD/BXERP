layui.use([ 'form', 'layer' ], function(){
    var form = layui.form;
    var layer = layui.layer;

    const messageItem_url = "messageItem/retrieveNEx.bx";

    //页面初始化
	layer.ready(function(){
        unsalableCommodityRN($("#messageCategoryID").val());
    });
    
    //滞销商品列表
	function unsalableCommodityRN(messageCategoryID){
		var indexLoading = layer.load(1);
		$.ajax({
			url: messageItem_url,
			type: 'post',
			async: true,
			dataType: "json",
			data: {
                'messageCategoryID': messageCategoryID,
                'pageSize':999
            },
			success: function succFunction(data) {
				console.log(data);
				if(data.ERROR == "EC_NoError"){
                    $(".number-warp").text(data.count);
                    $.each(data['objectList'], function(i, val) {
                        $(".commodityList table tbody").append(
                            '<tr>'+
                                '<td  style="width:60px;">'+(i+1)+'</td>'+
                                '<td>'+val.commodityName+'</td>'+
                                '<td>'+val.commodityBarcode+'</td>'+
                                '<td style="width:160px;">'+val.commodityPriceSuggestion.toFixed(2)+'</td>'+
                                '<td style="width:160px;">'+val.commodityPriceRetail.toFixed(2)+'</td>'+
                            '</tr>'
                        );
                    });
                    layer.close(indexLoading);
                    $("div.commodityList table tbody tr:odd").addClass("odd");	
                }else{
                    layer.close(indexLoading);
                }
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.close(indexLoading);
				layer.msg("服务器错误");
			}
		})
    }
})