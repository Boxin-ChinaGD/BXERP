layui.use(['element', 'form'], function () {
	var element = layui.element;
	var form = layui.form;

	const cacheRN_Url = "cache/retrieveNEx.bx";		//查询缓存的接口
	const updateCache_Url = "cache/updateCacheEx.bx";		//更新缓存接口
	const cache = {};
	//页面加载后执行
	$(document).ready(function(){
		var DBList = $("select[name='dbName'] option");
		for(var i=0; i < DBList.length; i++){
			var DBName = $(DBList[i]).attr("value");
			if(DBName == "nbr_bx"){
				$(DBList[i]).attr("selected", "selected");
			}
		}
		form.render("select", "cache");
	})
	//监听公司下拉框的选择
	form.on('select(company)', function (data) {
		console.log(data);
		$(".formSubmit").click();
	});
	//监听缓存类型的选择
	form.on('select(enumTpye)', function (data) {
		$(".formSubmit").click();
	});
	form.on('submit(formSubmit)', function (data) {
		var info = data.field;
		if (info.cacheType == 0) {
			info.whetherRetrieveAll = 0;
			delete info["cacheType"];
		} else {
			info.whetherRetrieveAll = 1;
		}
		cache.dbName = data.field.dbName;
		cache.cacheType = data.field.cacheType;
		console.log(info);
		QueryRN(info);
	})
	//查询缓存
	function QueryRN(info) {
		$.ajax({
			url: cacheRN_Url,
			type: "GET",
			async: true,
			dataType: "json",
			data: info,
			success: function succFunction(data) {		// 此接口后端只返回信息
				console.log(data);
				if (data.msg == null) {
					var indexLoading = layer.load(1);
					$(".showInfo").empty();
					for (var i = 0; i < data.objectList.length; i++) {
						var cacheIndex = data.objectList[i];
						if (cacheIndex.length > 0){
							for (var j = 0; j < cacheIndex.length; j++) {
								var cacheString = JSON.stringify(cacheIndex[j]);
								$('.showInfo').append(
									'<div ><span>' + cacheString + '</span><br/><button data=' + cacheIndex[j].ID + ' class="layui-btn layui-btn-sm refreshData">更新</button></div><br/>'
								);
							}
							$('.showInfo').append("<br/>")
						} else {
							continue;
						}
					}
					layer.close(indexLoading);
				} else {
					layer.msg(data.msg);
					$(".showInfo").empty();
				}
			},
			error: function (XMLHttpRequest, textStatus, errortdrown) {
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		});
	}
	//更新缓存
	$(".showInfo").delegate(".refreshData", 'click', function () {
		var indexLoading = layer.load(1);
		cache.cacheID = $(this).attr("data");
		$.ajax({
			url: updateCache_Url,
			type: "GET",
			async: true,
			dataType: "json",
			data: cache,
			success: function succFunction(data) {		// 此接口后端只返回信息
				console.log(data);
				layer.close(indexLoading);
				if (data.ERROR == "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg("更新缓存成功");
					}
					if (cache.cacheType == 0) {
						cache.whetherRetrieveAll = 0;
						cache.cacheType = "";
					} else {
						cache.whetherRetrieveAll = 1;
					}
					QueryRN(cache);
				} else {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg("更新缓存失败");
					}
				}
			},
			error: function (XMLHttpRequest, textStatus, errortdrown) {
				layer.close(indexLoading);
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		})
	})
})