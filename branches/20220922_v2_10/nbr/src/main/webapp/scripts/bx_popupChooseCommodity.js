var popupCommodityData = [];		//商品弹窗所选商品的临时储存位置
var tempArray = new Array();		//临时数组
var needCalculatedAmount = false;		//目前入库页面有使用到
//商品弹窗所选商品监听管理
function popupCommDataManage(status, commID, commName, barcodeID, barcodeName, packageUnitID, packageUnitName, NO, price, specification){
	switch (status){
		case "add":
			var commodityNotExist = true;
			for(var i=0; i<popupCommodityData.length; i++){
				if(popupCommodityData[i].commID == commID){
					commodityNotExist = false;
					popupCommodityData[i].NO = NO;
					if(needCalculatedAmount){
						popupCommodityData[i].specification = (parseInt(NO)*parseFloat(popupCommodityData[i].price)).toFixed(2);
					}
				}
			}
			if(commodityNotExist){		//没有缓存此商品
				var popupCommodity = {};
				popupCommodity["commID"] = commID;
				popupCommodity["commName"] = commName;
				popupCommodity["barcodeID"] = barcodeID;
				popupCommodity["barcodeName"] = barcodeName;
				popupCommodity["packageUnitID"] = packageUnitID;
				popupCommodity["packageUnitName"] = packageUnitName;
				popupCommodity["NO"] = NO;
				popupCommodity["price"] = price;
				popupCommodity["specification"] = specification;
				popupCommodityData.push(popupCommodity);
			}
		break;
		case "delete":
			for(var i=0; i<popupCommodityData.length; i++){
				if(popupCommodityData[i].commID == commID){
					popupCommodityData.splice(i, 1);
				}
			}
		break;
	}
	$(".popupPage .footArea strong").text(popupCommodityData.length);
	console.log(popupCommodityData);
}
//商品弹窗商品数量监听管理
function popupNumManage(index, key){
	var commID = $(index).parent().find("i").text();
	var inputBox = $(index).parent().find("input");
	var num = inputBox.val();
	var trNum = inputBox.parents("tr").attr("data-index");		//当前单元行的索引值
	if(isNaN(num)){		//不是数字
		num = "";
	}else{		//是数字
		if(num){
			num = parseInt(num);
			if(num > (Math.abs(2 << 30) - 1)){
				num = "";
				layui.use('layer', function(){
					var layer = layui.layer;
					layer.msg("输入的值超过最大限制,请重新输入");
				})
			}
		}else{
			num = 0;
		}
	}
	if(key == "addNum"){		//增加商品数量
		num += 1;
	}else if(key == "reduceNum"){		//减少商品数量
		num -= 1;
	}
	if(num > 0){
		if(!$("#popupCommodityList + div .layui-table").find("tr[data-index='" + trNum + "'] .layui-form-checkbox").hasClass("layui-form-checked")){
			$("#popupCommodityList + div .layui-table").find("tr[data-index='" + trNum + "'] .layui-form-checkbox").click();
		}
		popupCommDataManage("add", commID, "", "", "", "", "", num, "", "");
	}else{
		num = "";
		if($("#popupCommodityList + div .layui-table").find("tr[data-index='" + trNum + "'] .layui-form-checkbox").hasClass("layui-form-checked")){
			$("#popupCommodityList + div .layui-table").find("tr[data-index='" + trNum + "'] .layui-form-checkbox").click();
		}
	}
	inputBox.val(num);
}
//点击商品弹窗的取消或关闭按钮不记录此次的操作
function notRecordActions(beCopyData, needCopyData){
	needCopyData.length = 0;
	for(var i=0; i<beCopyData.length; i++){
		needCopyData[i] = JSON.parse(JSON.stringify(beCopyData[i]));
	}
}
