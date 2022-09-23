//页面跳转
function pageJumping(url, layid, name){
	var isSet = true;
	window.parent.$(".layui-tab-title li[lay-id]").each(function() {
		if ($(this).attr("lay-id") == layid) {
			isSet = false;
			window.parent.tabManage.tabChange(layid);
			window.parent.$(".layui-tab-content .layui-show").find('iframe').attr("src", url);
		}
	})
	if (isSet) {
		window.parent.tabManage.tabAdd(url, layid, name);
		window.parent.tabManage.tabChange(layid);
	}
}
//页面刷新
function pageRefresh (url){
	for(var i = 0;i< window.parent.frames.length; i++){
		// console.log(window.parent.frames[i].location.pathname );
		// console.log(window.parent.frames[i]);
		if(window.parent.frames[i].location.pathname == url){
			// console.log(window.parent.frames[i].location.pathname == url);
			window.parent.frames[i].location.reload(true);
		}
	}
}
//关闭页面
function pageClose(layid){
	window.parent.$(".layui-tab-title li[lay-id]").each(function() {
		if ($(this).attr("lay-id") == layid) {
			window.parent.tabManage.tabDelete(layid);
		}
	})
}
//重载表格
function reloadTable(table, tableID, method, url, curr, data){
	table.reload(tableID, {
		url: url,
		method: method,
		page: {
			curr: curr
		},
		where: data
	})
}
//字符串去重函数（新版本的js暂时不需要用到此函数）
//function duplicateRemoval(string){
//	var strArr = string.split(",");
//	strArr.sort();
//	var result = new Array();
//	var tempStr = "";
//	for(var j=1; j<=strArr.length-1; j++){
//		if(strArr[j] != tempStr){
//			result.push(strArr[j]);
//			tempStr = strArr[j];
//		}else{
//			continue;
//		}
//	}
//	if(result.join(",")){
//		string = result.join(",") + ",";
//	}else{
//		string = "";
//	}
//	return string;
//}
//去除字符串中的某个数值（新版本的js暂时不需要用到此函数）
//function removalOfValues(string, value){
//	var strArr = string.split(",");
//	string = "";
//	for(var i=0; i<strArr.length-1; i++){
//		if(strArr[i] != value){
//			string += strArr[i] + ",";
//		}else{
//			continue;
//		}
//	}
//	return string;
//}
//数据表格复选框单选监听（新版本的js暂时不需要用到此函数）
//function checkboxSingleSelection(tr, tbody, string, id ){
//	var dataIndex = tr.attr("data-index");
//	var trIndex = tbody.find("tr[data-index='" + dataIndex + "']");
//	var checkboxIndex = trIndex.find(".layui-form-checkbox");
//	if(checkboxIndex.hasClass("layui-form-checked")){
//		string += id + ",";
//	}else{
//		var strArr = string.split(",");
//		string = "";
//		for(var i=0; i<strArr.length-1; i++){
//			if(strArr[i] != id){
//				string += strArr[i] + ",";
//			}else{
//				continue;
//			}
//		}
//	}
//	return string;
//}
//数据表格复选框全选监听（新版本的js暂时不需要用到此函数）
//function checkboxAllSelection(obj, string, data){
//	var ID = -1;
//	if(obj.checked){
//		for(var i=0; i<data.length; i++){
//			if (data[i].purchasingOrder != null) {		//采购订单
//				ID = data[i].purchasingOrder.ID; 
//			}else if(data[i].inventorySheet != null){		//盘点单
//				ID = data[i].inventorySheet.ID; 
//			}else if(data[i].warehousing != null){		//入库单
//				ID = data[i].warehousing.ID; 
//			}else if(data[i].commodity != null){
//				ID = data[i].commodity.ID;
//			}
//			else {
//				ID = data[i].ID;
//			}
//			string += ID + ",";
//		}
//	}else{
//		var strArr = string.split(",");
//		string = "";
//		for(var i=0; i<strArr.length-1; i++){
//			var isExist = false;
//			for(var j=0; j<data.length; j++){
//				if (data[j].purchasingOrder != null) {		//采购订单
//					ID = data[j].purchasingOrder.ID; 
//				}else if(data[j].inventorySheet != null){		//盘点单
//					ID = data[j].inventorySheet.ID; 
//				}else if(data[j].warehousing != null){		//入库单
//					ID = data[j].warehousing.ID; 
//				}else if(data[j].commodity != null){
//					ID = data[j].commodity.ID;
//				}else {
//					ID = data[j].ID;
//				}
//				if(strArr[i] == ID){
//					isExist = true;
//				}
//			}
//			if(!isExist){
//				string += strArr[i] + ",";
//			}
//		}
//	}
//	return string;
//}
//数据表格对已选择的复选框进行选择（新版本的js暂时不需要用到此函数）
//function checkboxSelect(data, string, tbody){		//string为接收的ID字符串1,2,3,...
//	var strArr = string.split(",");
//	strArr.sort();
//	for(var i=0; i<data.length; i++){
//		var index = data[i].LAY_TABLE_INDEX;
//		var indexID = -1;
//		if (data[i].purchasingOrder != null) {		//采购订单
//			indexID = data[i].purchasingOrder.ID;   
//			//else if (xxxx) {
//			//... 其他类似purchasingOrder的成员 
//			//		}
//		}else if(data[i].inventorySheet != null){		//盘点单
//			indexID = data[i].inventorySheet.ID; 
//		}else if(data[i].warehousing != null){		//入库单
//			indexID = data[i].warehousing.ID; 
//		}else if(data[i].commodity != null) {
//			indexID = data[i].commodity.ID; 
//		}else {
//			indexID = data[i].ID;
//		}
//		for(var j=1; j<strArr.length; j++){
//			if(indexID == strArr[j]){
//				tbody.find("tr[data-index='" + index + "'] .layui-form-checkbox").click();								
//			}
//		}
//	}
//}
//时间格式解析（新版本的js暂时不需要用到此函数）
//function timeNnalyzing(time){
//	var year = time.year + 1900;
//	var month = time.month + 1;
//	var date = time.date;
//	var hours = time.hours;
//	var minutes = time.minutes;
//	var seconds = time.seconds;
//	month = (month < 10) ? "0" + month : month;
//	date = (date < 10) ? "0" + date : date;
//	hours = (hours<10) ? "0" + hours:hours;
//	minutes = (minutes<10) ? "0" + minutes:minutes;
//	seconds = (seconds<10) ? "0" + seconds:seconds;
//	time = year + "/" + month + "/" + date + " " + hours + ":" + minutes + ":" + seconds;
//	return time;
//}














