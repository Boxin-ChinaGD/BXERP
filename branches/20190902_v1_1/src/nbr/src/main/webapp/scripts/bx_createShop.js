layui.use([ 'element', 'form', 'table', 'laypage', "upload", 'laydate' ], function() {
	var element = layui.element;
	var form = layui.form;
	var table = layui.table;
	var laypage = layui.laypage;		//分页
	var laydate = layui.laydate;
	var upload = layui.upload;		//文件上传
	
	const retrieveNCompany_url = "company/retrieveNEx.bx";		//查询多个公司门店
	const shopR1_url = "shop/retrieve1Ex.bx";		// 查询一个门店信息
	const createShop_Url = "company/createShopEx.bx";		//创建一个门店
//	const updateShop_url = "shop/updateEx.bx";		//修改一个门店
//	const shopToCheckUniqueField_url = "shop/retrieveNToCheckUniqueFieldEx.bx";		//门店唯一值检测
	const updateCompany_url = "company/updateEx.bx";		//修改一个公司的信息
	const createCompany_url = "company/createEx.bx";		//创建一个公司
	const companyToCheckUniqueField_url = "company/retrieveNToCheckUniqueFieldEx.bx";		//验证公司字段是否唯一
	const uploadCompanyLogo_url = "company/uploadLogoEx.bx";		//上传公司logo
	const uploadCompanyBusinessLicensePicture_url = "company/uploadBusinessLicensePictureEx.bx";		//上传营业执照图片
	const uploadCompanyAPI_url = "company/uploadAPICertEx.bx";		//上传营业执照证书
	const updateSubmchid_url = "company/updateSubmchidEx.bx";		//修改公司的子商户号接口
	const posGetToken_url = "pos/getTokenEx.bx";		//pos获取秘钥接口
	const posCreate_url = "posSync/createEx.bx";		//添加pos机接口
	const posDelete_url = "posSync/deleteEx.bx";		//删除POS机接口
	const recyclePOS_url= "pos/recycleApp.bx";		//重新注册POS机接口
	const method_get = "GET";
	const method_post = "POST";
	const shop_reloadTable = { "pageIndex": "1", "pageSize": "9" };		//条件查询
	//定义layui的use函数中的伪全局变量
	var bDesertLastOperation = true;		//用于提示是否放弃之前的操作，true表示放弃，false表示不放弃。比如，用户点击了新建，然后点击某个门店查看详情，这时会弹出提示
	var isCompanyInfoUpdated = false;		//用于判断是否修改了公司信息
	var isPictureUploaded = true;		//用于判断创建公司时是否上传营业执照图片
	var shopCount = 0;		//用于记录最近一次查询到的门店总数

//	// 页面初始化
//	layer.ready(function() {
//		shopListRN(shop_reloadTable);
//		uploadingCompanyLogo();		//渲染上传公司logo按钮
//		uploadCompanyAPI();		//上传公司营业执照证书
//		uploadingCompanyBusinessLicensePicture();		//上传公司营业执照图片
//		fieldFormat(form);		//加载表单数据验证方法
//	});
//	laydate.render({		//时间
//	    elem: '#datetime', 
//	    format: 'yyyy/MM/dd',
//	    value: '',
//	    isInitValue: false
//	});
	
	// 门店列表数据渲染
	function shopListRN(data) {
		var indexLoading = layer.load(1);
		$.ajax({
			url: retrieveNCompany_url,
			type: method_get,
			async: true,
			dataType: "json",
			data: data,
			success: function succFunction(data){
				layer.close(indexLoading);
				console.log(data);
				if(data.ERROR == "EC_NoError"){
					$(".store ul").empty();		//清空旧门店列表数据以便后续渲染新门店列表数据
					if (data.objectList.length > 0) {		//存在门店列表数据
						for(var i=0; i<data.objectList.length; i++){
							if (data.objectList[i].company.status == 0) {		//营业中的公司
								showShopInfo("使用中", "inBusiness", data.objectList[i]);
							} else if (data.objectList[i].company.status == 1) {		//停业的公司
								showShopInfo("停业中", "stopBusiness", data.objectList[i]);
							}else{
								//处于其他状态的公司暂不进行处理
							}
						}
						if (isCompanyInfoUpdated) {
							var shopID = $(".shopID").val();
							var dbName = $(".dbName input").val();
							$(".store ul li[data='" + shopID + "'][string='" + dbName + "']").click();
							isCompanyInfoUpdated = false;
						} else {
							$(".store ul li:first").click();
						}
						shopCount = data.count;
						paginationRender(data.count);		//渲染分页
					} else {
						$(".store ul").html("<span>无数据</span>");
						layer.msg("查无门店列表信息");
					}
				} else {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg("查询公司门店信息失败");
					}
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(indexLoading);
			}
		});
	}
	//渲染门店信息概况
	function showShopInfo (status, className, data) {
		$(".store ul").append(
			"<li data='" + data.ID + "' string='" + data.company.dbName + "'>" +		//通过门店的ID和公司名称确定每个li的唯一性
				"<div class='storeArea'>" +
					"<label>" + data.name + "</label>" +
					"<span class='" + className + "'>" + status + "</span>" +
					"<br />" +
					"<br />" +
					"主体信息：" + data.company.name +
					"<br/>" +
					"有效期至：" + data.company.expireDatetime.substring(0, 10) +
				"</div>" +
			"</li>"
		);
	}
	//渲染分页
	function paginationRender (count) {
		laypage.render({		//渲染分页
			elem: 'pagination',
			count: count,		//数据总数，从服务端得到
			limit: 9,		//定义每页显示的数量
			next: ">",
			prev: "<",
//			hash: "indexCurr",
//			curr: location.hash.replace('#!indexCurr=', ''),
			curr: shop_reloadTable.pageIndex,
			layout: [ 'prev', 'page', 'next' ],
			jump: function(obj, first){
				console.log(obj);
				if(!first){		//首次不执行
					if (bDesertLastOperation) {
						shop_reloadTable.pageIndex = obj.curr;
						shopListRN(shop_reloadTable);
					}else{
						layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示', cancel: function () {
								paginationRender(shopCount);
							}
						}, function (index, layero) {		//点击确定
							layer.close(index);
							bDesertLastOperation = true;
							shop_reloadTable.pageIndex = obj.curr;
							shopListRN(shop_reloadTable);
						}, function (index, layero) {		//点击取消
							layer.close(index);
							paginationRender(shopCount);
						})
					}
				}
			}
		});
	}
	// 门店列表单击事件
	$(".store ul").on("click", "li", function(){
		var shopIndex = $(this);
		if (bDesertLastOperation) {
			toR1ShopDetails(shopIndex);
		} else {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
				layer.close(index);
				bDesertLastOperation = true;
				toR1ShopDetails(shopIndex);
			}, function (index, layero) {		//点击取消
				layer.close(index);
			})
		}
	})
	function toR1ShopDetails (shopIndex) {
		var shopID = shopIndex.attr("data");
		var dbName = shopIndex.attr("string");
		shopIndex.addClass("choosed").siblings().removeClass("choosed");
		shopR1(dbName, shopID);
	}
	//查看门店详情
	function shopR1 (dbName, shopID) {
		var indexLoading = layer.load(1);
		$.ajax({
			url: shopR1_url,
			type: method_get,
			async: true,
			dataType: "json",
			data: {
				"ID": shopID,
				"dbName": dbName
			},
			success: function succFunction(data){
				console.log(data);
				layer.close(indexLoading);
				if(data.ERROR == "EC_NoError"){
					var shopInfo = data.object;		//获取到的门店信息
					var companyInfo = data.object.company;		//获取到的公司信息
					var posList = data.object.listPos;		//获取到的POS信息
					//渲染公司信息
					form.val("customerInformation", {
						"businessLicenseSN": companyInfo.businessLicenseSN,
						"submchid": companyInfo.submchid,
						"bossName": companyInfo.bossName,
						"ID": companyInfo.ID,
						"bossPhone": companyInfo.bossPhone,
						"bossPassword": companyInfo.bossPassword,
						"expireDatetime": companyInfo.expireDatetime.substring(0, 10).replace(/-/g, "/"),
						"bossWechat": companyInfo.bossWechat,
						"dbName": companyInfo.dbName,
						"name": companyInfo.name,
						"dbUserName": companyInfo.dbUserName,
						"dbUserPassword": companyInfo.dbUserPassword,
						"SN": companyInfo.SN,
						"brandName": companyInfo.brandName
					});
					companyInfo.logo = companyInfo.logo == "" ? " " : companyInfo.logo + "?timestamp=" + new Date().getTime();
					$(".companyLogo img ").attr("src", companyInfo.logo);
					$(".companyPermit img ").attr("src", companyInfo.businessLicensePicture + "?timestamp=" + new Date().getTime());
					$(".customerInformation .companySN").parent().show();		//查看门店信息详情时需显示公司编号
					$(".customerInformation input").attr("readonly", "readonly");
					$(".weChatSubmchid").show();		//微信子商户信息显示
					//渲染门店信息
					var status = "";
					if(shopInfo.status == 0){
						status = "营业中";
					}else if(shopInfo.status == 1){
						status = "离线中";
					}else if(shopInfo.status == 2){
						status = "断网中";
					}else if(shopInfo.status == 3){
						status = "欠费锁定";
					}else if(shopInfo.status == 4){
						status = "财务待审核";
					}else{}		//其他状态
					form.val("shopInformation", {
						"bxStaffName": shopInfo.bxStaffName,
						"ID": shopInfo.ID,
						"name": shopInfo.name,
						"status": status,
						"address": shopInfo.address,
						"longitude": shopInfo.longitude,
						"latitude": shopInfo.latitude,
						"companyID": shopInfo.companyID,
						"remark": shopInfo.remark
					});
					//渲染pos机信息
					$(".posInformation table tbody").empty();		//清空列表节点
					for(var i=0; i<posList.length; i++){
						posTableRender(i + 1, posList[i]);
					}
					$(".shopDetails input").css("border", "none");		//清除输入框边框
					$(".toUpdateCompany, .toUpdateSubmchid, .renewBtn, .toUpdateShopInfo, .toCreatePos").show();		//按钮状态控制
					$(".toUpdateCompany").text("修改");
					$(".createCompany, .cancel").hide();		//查看门店信息详情时先隐藏保存和取消按钮
					$(".uploadCompanyLogo, .uploadCompanyAPI, .uploadCompanyPermit").attr("disabled", "disabled").addClass("backgroudCss");		//不允许修改公司logo、公司营业执照证书和图片
					$(".dbUserName input, .dbUserpwd input").removeAttr("lay-verify");		//去除数据格式验证，因为查询门店信息详情时，该敏感信息为空，若添加验证将修改不了公司信息
					$(".periodOfValidity input").attr("disabled", "disabled").css({ "background-color": "transparent", "color": "#000" });
					//					$(".password input, .periodOfValidity input, .dbUserName input, .dbUserpwd input").removeAttr("lay-verify");
				}else{
					if(data.msg){
						layer.msg(data.msg);
					}else{
						layer.msg("查看公司门店详情信息失败");
					}
				}
				$(".posInformation tbody tr:odd").addClass("odd");
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(indexLoading);
			}
		});
	}
	//渲染POS表格
	function posTableRender (serialNumber, posInfo) {
		$(".posInformation table tbody").append(
			"<tr >" +
				"<td>" +
					"<i class='layui-icon layui-icon-close-fill deletePos' onclick='deletePos(this)'></i>" +
					"<span>" + serialNumber + "</span>" +
				"</td>" +
				"<td>" + posInfo.ID + "</td>" +
				"<td>" + posInfo.pos_SN +"</td>" +
				"<td>" + "<p class='recycle' title='重新注册一个Pos机。用户不小心删除了App后再重装App，App将无法正常使用。必须由OP重新注册才可以继续使用。' onclick='recyclePos(this)'>重置 </p>" + "</td>" +
			"</tr>"
		);
	}
	
	//上传营业执照证书API
	function uploadCompanyAPI () {
		var uploadCompanyAPI = upload.render({
			elem: '.uploadCompanyAPI',
			url: uploadCompanyAPI_url,
			method: method_post,
			accept: 'file',
			acceptMime: 'application/x-pkcs12',
			exts:'p12',
			data: {},
			before : function(obj) {
				// 预读本地文件示例，不支持ie8
				obj.preview(function(index, file, result) {
					console.log(index);
					console.log(file);
					console.log(result);
				});
			}, done: function (res) {
				console.log(res);
				bDesertLastOperation = false;
				if(res.ERROR="EC_NoError"){
					if (res.msg) {
						layer.msg(res.msg);
					} else {
						layer.msg("上传营业执照证书成功");
				}
				} else {
					if (res.msg) {
						layer.msg(res.msg);
					} else {
						layer.msg("上传营业执照证书失败");
					}
				}
			},error: function(){//请求异常回调
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
            }
		});
	}
	
	//上传营业执照图片
	function uploadingCompanyBusinessLicensePicture () {
		var uploadCompanyPermit = upload.render({
			elem: '.uploadCompanyPermit',
			url: uploadCompanyBusinessLicensePicture_url,
			method: method_post,
			accept: 'images',
			acceptMime: 'image/*',
			exts: 'jpg|png',
			size: 100,
			data: {},
			before: function(obj) {
				// 预读本地文件示例，不支持ie8
				obj.preview(function(index, file, result) {
					 $('.companyPermit img').attr('src', result); //图片链接（base64）
				});
			},
			done: function(res) {
				console.log(res);
				bDesertLastOperation = false;
				if(res.ERROR=="EC_NoError"){
					isPictureUploaded = true;
					if (res.msg) {
						layer.msg(res.msg);
					} else {
						layer.msg("上传营业执照图片成功");
					}
					$(".companyPermit img").attr("src",res.businessLicensePictureDestination)
				}else{
					if (res.msg) {
					layer.msg(res.msg);
					} else {
						layer.msg("上传营业执照图片失败");
				}
				}
			},error: function(){//请求异常回调
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
            }
		});
	}

	//上传公司logo
	function uploadingCompanyLogo () {
		var uploadingCompanyLogo = upload.render({
			elem: '.uploadCompanyLogo',
			url: uploadCompanyLogo_url,
			method: method_post,
			accept:'images',
			acceptMime: 'image/*',
			exts: 'jpg|png',
			size: 100,
			data: {},
			before: function(obj) {
				// 预读本地文件示例，不支持ie8
				obj.preview(function(index, file, result) {
					 $('.companyLogo img').attr('src', result); //图片链接（base64）
				});
			},
			done: function (res) {
				console.log(res);
				bDesertLastOperation = false;
				if(res.ERROR=="EC_NoError"){
					if (res.msg) {
						layer.msg(res.msg);
					} else {
						layer.msg("上传公司logo成功");
					}
				}else{
					if (res.msg) {
						layer.msg(res.msg);
					} else {
						layer.msg("上传公司logo失败");
					}
					$('.companyLogo img').attr('src', ' ');
				}
			},
			error: function () {//请求异常回调
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				$('.companyLogo img').attr('src', ' ');
            }
		});
	}
	
	//检查字段唯一性：fieldToCheckUnique=1代表要检查公司名称；2代表要检查营业执照；5代表要检查DB名称;6代表要检查子商户号;
	window.dataValidation = function(obj, uniqueField){
		var fieldToCheckUnique;
		var $obj = $(obj).attr("name");
		switch ($obj){
			case "name":
				if(/^[\u4E00-\u9FA5A-Za-z]+$/.test(uniqueField)){
					fieldToCheckUnique = 1;
				}else{
					layer.msg("公司名称数据格式不对，请输入中文或英文，不能为空");
					return;
				}
				break;
			case "businessLicenseSN":
				if(/^[A-Z0-9]{15}$|^[A-Z0-9]{18}$/.test(uniqueField)){
					fieldToCheckUnique = 2;
				}else{
					layer.msg("公司营业执照号数据格式不对，请输入15或18位的数字或大写字母或它们的组合");
					return;
				}
				break;
			case "dbName":
				if(/^[a-zA-Z][a-zA-Z0-9_]*$/.test(uniqueField)){
					fieldToCheckUnique = 5;
				}else{
					layer.msg("公司dbName数据格式不对，请输入数字、字母和下划线的组合，但首字符必须是字母，中间不能出现空格");
					return;
				}
				break;
			case "submchid":
				if(/^$|^[0-9\s]+$/.test(uniqueField) && /^$|^.{10}$/.test(uniqueField)){
					fieldToCheckUnique = 6;
				}else{
					layer.msg("公司子商户号数据格式不对，请输入10位数字或保持为空");
					return;
				}
				break;
			default:
				return;
		}
		var companyID = $(".customerInformation .companyID").val() == "" ? 0 : $(".customerInformation .companyID").val();
		console.log("companyID==" + companyID);
    	$.ajax({
			url: companyToCheckUniqueField_url,
			type: method_post,
			async: true,
			dataType: "json",
			data: {
				"uniqueField": uniqueField,
				"fieldToCheckUnique": fieldToCheckUnique,
				"ID": companyID
			},
			success: function succFunction(data) {
				console.log(data);
				if(data.ERROR != "EC_NoError"){
					layer.msg(data.msg);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
    	})
	}
	
	//即时搜索事件
	window.instantSearch = function(index){
		$(index).next().click();
	}
	
	//创建公司按钮的点击监听
	$(".toCreateCompany").click(function () {
		if (bDesertLastOperation) {
			bDesertLastOperation = false;
			toCreateCompany();
		} else {
			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
				layer.close(index);
				toCreateCompany();		//点击新建按钮时执行的函数
			}, function (index, layero) {		//点击取消
				layer.close(index);
			})
		}
	});
	function toCreateCompany () {
		isPictureUploaded = false;		//用于判断创建公司时是否上传营业执照图片
		$(".shopDetails input").val("");		//新建时清空表单内容
		$(".shoplists ul li").removeClass("choosed");		//去除门店列表的选中状态
		$(".customerInformation .companySN").parent().hide();		//创建公司时不需要显示公司编号，因为公司编号并非用户填写，是由后端生成
		$(".toUpdateCompany").hide();		//隐藏修改公司的保存按钮
		$(".createCompany, .cancel").show();		//显示创建公司的保存按钮和取消创建按钮
		$(".customerInformation input").removeAttr("readonly");		//清除公司表单的不可编辑状态
		$(".customerInformation input").css("border", "1px solid #1A4A9F");		//添加公司表单输入框边框
		$("#datetime").attr("readonly", "readonly");		//有效期不可手动输入
		$(".toUpdateSubmchid, .renewBtn, .toUpdateShopInfo, .toCreatePos").hide();		//隐藏不需要的按钮
		$(".posInformation tbody").empty();		// 清空POS列表
		$(".uploadCompanyLogo, .uploadCompanyAPI, .uploadCompanyPermit").removeAttr("disabled").removeClass("backgroudCss");
		$(".periodOfValidity input").removeAttr("disabled");
		$(".companyLogo img").attr("src", " ");
		$(".companyPermit img").attr("src", " ");
		$(".weChatSubmchid").hide();		//创建公司时微信子商户信息隐藏
	}
	//新建时的保存按钮
	$(".createCompany").click(function () {
		$(".dbUserName input").attr("lay-verify", "required|charDbNmae|noBlank");
		$(".dbUserpwd input").attr("lay-verify", "required|password|noChinese");
		$(".companyInfoSubmit").click();
	});
	//取消按钮
	$(".cancel").click(function(){
//		if (bDesertLastOperation) {
//			shop_reloadTable.pageIndex = 1;
//			shopListRN(shop_reloadTable);
//		} else {
//			layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
//				layer.close(index);
//				bDesertLastOperation = true;
//				shop_reloadTable.pageIndex = 1;
//				shopListRN(shop_reloadTable);
//			}, function (index, layero) {		//点击取消
//				layer.close(index);
//			})
//		}
//		
//		if (bDesertLastOperation) {
//			shop_reloadTable.pageIndex = 1;
//			shopListRN(shop_reloadTable);
//		} else {
		layer.confirm('确定要放弃之前的操作吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
			layer.close(index);
			$("#name").val("");
			$("#address").val("");
			$("#companyID").val("");
			$("#longitude").val("");
			$("#latitude").val("");
			$("#remark").val("");
			$("#name").val("");
			$("#status").val("");
            // 清空表单 （“addGoodsForm”是表单的id）
//            $("#shopInformation")[0].reset();
//            layui.form.render();
//			bDesertLastOperation = true;
//			shop_reloadTable.pageIndex = 1;
//			shopListRN(shop_reloadTable);
		}, function (index, layero) {		//点击取消
			layer.close(index);
		})
//		}
	});
	//修改公司信息
	$(".toUpdateCompany").click(function () {
		var text = $(".toUpdateCompany").text();
		switch (text){
			case "保存":
				$(".companyInfoSubmit").click();
			break;
			case "修改":
				$(this).text("保存");
				bDesertLastOperation = false;
				//恢复表单可修改内容的可编辑性
				$(".companyName, .businessLicenseNumber, .bossName, .bossPhone, .bossWechat, .brandName").find("input").removeAttr("readonly").css("border", "1px solid #1A4A9F");
				$(".uploadCompanyLogo, .uploadCompanyAPI, .uploadCompanyPermit").removeAttr("disabled").removeClass("backgroudCss");
				$(".cancel").show();
			break;
			default:
				break;
		}
	})
	//公司表单信息提交
	form.on('submit(companyInfoSubmit)', function (data) {
		var companyInfo = data.field;
		console.log(companyInfo)
		if (companyInfo.ID) {		//存在ID为修改公司
			layer.confirm('确定修改公司"' + companyInfo.name + '"吗？', { icon: 3, title: '提示' }, function (index) {
			var indexLoading = layer.load(1);
				layer.close(index);
				companyManage(updateCompany_url, companyInfo, "修改公司信息成功", "修改公司信息失败", indexLoading);
			})
		} else {		//不存在ID为创建公司
			layer.confirm('确定创建公司"' + companyInfo.name + '"吗？', { icon: 3, title: '提示' }, function (index) {
				delete companyInfo["ID"];
				delete companyInfo["SN"];
				var indexLoading = layer.load(1, {
					content: '创建中...',
					success: function (layero) {
						layero.find('.layui-layer-content').css({ 'padding-top': '39px', 'width': '60px', 'font-weight': 'bold' });
					}
				});
				layer.close(index);
				companyManage(createCompany_url, companyInfo, "创建公司成功", "创建公司失败", indexLoading);
			})
		}
		return false;
	});
	//公司的创建和修改
	function companyManage (url, requestedData, succText, failText, indexLoading) {
			$.ajax({
			url: url,
			type: method_post,
			async: true,
			dataType: "json",
			data: requestedData,
			success: function succFunction(data){
				console.log(data);
				layer.close(indexLoading);
				if(data.ERROR == "EC_NoError"){
					bDesertLastOperation = true;
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg(succText);
					}
					if (url == createCompany_url) {
						shop_reloadTable.pageIndex = 1;
					} else if (url == updateCompany_url) {
						isCompanyInfoUpdated = true;
					} else {
						//其他操作
					}
					shopListRN(shop_reloadTable);
				}else{
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg(failText);
					}
					if (url == createCompany_url) {
						$('.companyPermit img').attr('src', ' ');
						isPictureUploaded = false;
					}
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(indexLoading);
			}
		});
	}
	//公司信息是否发生修改
	window.companyInfoHasUpdate = function () {
		bDesertLastOperation = false;
	}

	//打开子商户号弹窗
	var submchidPopup;
	$(".toUpdateSubmchid").click(function () {
		layer.open({
			type: 1,
			title: "修改子商户号",
			content: $("#submchidPopup"),
			success: function(layero, index){
				var companyID = $(".customerInformation .companyID").val();
				var companyName = $(".customerInformation .companyName input").val();
				$("#submchidPopup input[type='hidden']").val(companyID);
				$("#submchidPopup strong").text(companyName);
				$("#submchidPopup input[name='submchid']").val("");
				submchidPopup = index;
			}
		});
	});
	//子商户号信息提交
	form.on('submit(updateSubmchid)', function(data){
		var indexLoading = layer.load(1);
		console.log(data.field);
		$.ajax({
			url: updateSubmchid_url,
			type: method_post,
			data: data.field,
			cache: false,
			async: true,
			dataType: "json",
			success: function(data){
				console.log(data);
				layer.close(indexLoading);
				if(data.ERROR == "EC_NoError"){
					var msg = data.msg == "" ? "修改子商户号成功" : data.msg;
					form.val("customerInformation", {
						"submchid": data.object.submchid
					})
					layer.msg(msg);
					layer.close(submchidPopup);
				}else{
					var msg = data.msg == "" ? "修改子商户号失败" : data.msg;
					layer.msg(msg);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(indexLoading);
			}
		})
		return false;
	})
	//取消修改公司子商户号
	$(".cancelUpdateSubmchid").click(function(){
		layer.close(submchidPopup);
	})
	
	//添加pos机按钮,打开创建pos机的表单
	var posPopup;
	$(".toCreatePos").click(function(){
		layer.open({
			type: 1,
			title: "添加POS机",
			content: $("#posPopup"),
			success: function(layero, index){
				var shopID = $(".shopInformation .shopID").val();
				var shopName = $(".shopInformation .shopName input").val();
				$("#posPopup").find("input[name='shopID']").val(shopID);
				$("#posPopup").find("strong").text(shopName);
				$("#posPopup .layui-input-inline input").val("");
				posPopup = index;
			}
		});
	});	
	//重置按钮（挽救失足用户弹窗）	
	window.recyclePos = function (obj) {
		var posID = $(obj).parent().siblings().eq(1).text();	//获取该POS对应的ID
		var companySN = $(".companySN input").val();	//获取该公司对应的SN
		layer.confirm('是否重置该SN码对应的POS机', { icon:3, title: '重置POS机' }, function (index) { 	 
			 layer.close(index);
			 var indexLoading = layer.load(1);
			 $.ajax({		    		 
				url: recyclePOS_url,
				type: method_post,
				data: {							
					"ID": posID,
					"returnSalt": 1,	//后端要求传的值
					"companySN": companySN,
				},
				cache: false,
				async: true,
				dataType: "json",
				success: function (data) {
					console.log(data);
					layer.close(indexLoading);
					if(data.ERROR === 'EC_NoError'){
						layer.msg('重置成功', {icon:1}); 
					}else{
						layer.msg('重置失败', {icon:2}); 
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
					layer.close(indexLoading);
				}
			 });
		})
	}
	//POS信息提交
	form.on('submit(createPos)', function(obj){
		var indexLoading = layer.load(1);
		console.log(obj);
		$.ajax({
			url: posGetToken_url,
			type: method_post,
			data: {
				"passwordInPOS": obj.field.passwordInPOS,
				"operationType": 1		//后端要求
			},
			cache: false,
			async: true,
			dataType: "json",
			success: function(data){
				console.log(data);
				var modulus = data.rsa.modulus;
				var exponent = data.rsa.exponent;
				var rsa = new RSAKey();
				rsa.setPublic(modulus, exponent);
				obj.field.passwordInPOS = rsa.encrypt(obj.field.passwordInPOS);
				if(obj.field.passwordInPOS){
					obj.field.companySN = $(".companySN input").val();
					obj.field.returnObject = 1;
					posManage(posCreate_url, method_post, obj.field, "添加pos机成功", "添加pos机失败", indexLoading);
				}else{
					layer.msg("加密pos机密码失败");
					layer.close(indexLoading);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(indexLoading);
			}
		});
		return false;
	});
	//删除POS机
	var posIndex;
	window.deletePos = function(obj){
		posIndex = $(obj);
		layer.confirm('确定要删除POS机' + posIndex.parents("tr").find("td").eq(2).text() + '吗？', { icon: 2, title: '提示' }, function (index) {
			var indexLoading = layer.load(1);
			var posID = posIndex.parents("td").next().text();
			var companySN = $(".companySN input").val();
			posManage(posDelete_url, method_get, { "ID": posID, "companySN": companySN }, "pos机删除成功", "pos机删除失败", indexLoading);
		})
	}
	//POS的添加和删除
	function posManage (url, method, requestedData, succText, failText, indexLoading) {
		console.log(requestedData);
		$.ajax({
			url: url,
			type: method,
			cache: false,
			async: true,
			dataType: "json",
			data: requestedData,
			success: function (data) {
				console.log(data);
				layer.close(indexLoading);
				if(data.ERROR == "EC_NoError"){
					var msg = data.msg == "" ? succText : data.msg;
					layer.msg(msg);
					layer.close(posPopup);
					if (url == posCreate_url) {		//添加POS
						var serialNumber = $(".posInformation table tbody tr").length + 1;
						posTableRender(serialNumber, data.object);
					} else if (url == posDelete_url) {		//删除POS
						posIndex.parents("tr").remove();
						for (var i = 1; i <= $(".posInformation table tbody tr").length; i++) {
							$(".posInformation table tbody tr").eq(i - 1).find("span").text(i);
						}
					} else { }		//其他操作
				}else{
					var msg = data.msg == "" ? failText : data.msg;
					layer.msg(msg);
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(indexLoading);
			}
		})
	}
	//pos机取消添加
	$(".cancelCreatePos").click(function (index) {
		layer.close(posPopup);
	})
//--------------  以下是未开放的功能  -----------------------------------------------------------
	// 头部导航选项
	$(".topNav .layui-form-label span").click(function() {
		layer.msg("功能未开放");
		return;
//		var index = $(this);
//		var area = index.offset();
//		var areaParent = index.parent().offset();
//		var thisUl = index.parent().next();
//		$(".topNav .layui-form-label + ul").hide();
//		thisUl.css({
//			"display" : "block",
//			"left" : area.left - areaParent.left
//		});
//		thisUl.find("li").click(function() {
//			thisUl.hide();
//			index.find("lable").text($(this).text());
//		})
	})
	
	// 关闭头部导航选项区域(暂未开放的功能)
//	$(document).click(function() {
//		$(".topNav .layui-form-label + ul").hide();
//	})
//	$(document).delegate('.topNav .layui-form-label', 'click', function(event) {
//		event.stopPropagation();
//		console.log("阻止上述事件冒泡");
//	})
	
	//模糊搜索
	$("#search").click(function(){
		layer.msg("功能暂未开放");
		return;
	});
	//门店区域列表单击事件
	$(".geographicList ul li dl dd").click(function (){
		layer.msg("功能暂未开放");
		return;
//		shop_reloadTable.districtID = $(this).find("a").attr("indexid");
//		shopListRN(shop_reloadTable);
	})
	//续费按钮事件
	$(".renewBtn").toggle(function(){
		layer.msg("功能未开放");
		return;
//		$(".renewBtn").text("保存");
//		$(".customerInformation .expireDatetime").removeAttr("readonly");	//清除不可编辑状态
//		$(".customerInformation .expireDatetime").css("border","1px solid #1A4A9F");//添加输入框边框
	},function(){
		layer.msg("功能未开放");
		return;
//		$(".renewBtn").text("续费");
//		$(".customerInformation input").attr("readonly","readonly");//添加不可编辑状态
//		$(".customerInformation input").css("border","none");		//清除输入框边框
//		$(".company").click();
	});

//	//新建门店
//	function newShop(name,companyID,address,status,longitude,latitude,key,bxStaffID,remark){
//		$.ajax({
//			url : createShop_Url,
//			type : method_post,
//			async : true,
//			dataType : "json",
//			data : {
//				"name":name,
//				"companyID":companyID,
//				"address":address,
//				"status":status,
//				"longitude":longitude,
//				"latitude":latitude,
//				"key":key,
//				"bxStaffID":bxStaffID,
//				"remark":remark
//			},
//			success : function succFunction(data) {
//				console.log(data);
//				if(data.ERROR == "EC_NoError"){
//					shopListRN(5);
//				}else{
//					layer.msg("创建失败");
//				}
//			}
//		});
//	}
	
	//门店信息修改
	$(".toUpdateShopInfo").click(function () {
		layer.msg("功能暂未开放");
		return;
		//		var text = $(".toUpdateShopInfo").text();
//		switch (text){
//			case "保存":
//				$(".present").click();
//			break;
//			case "修改":
		//				$(".toUpdateShopInfo").text("保存");
//				$(".shopInformation input").removeAttr("readonly");	//清除不可编辑状态
//				$(".shopInformation input").css("border","1px solid #1A4A9F");//添加输入框边框
//				$(".shopID").attr("readonly","readonly");
//				$(".shopID").css("border","none");
//				$(".companyID").attr("readonly","readonly");
//				$(".companyID").css("border","none");
//				$(".shopStatus").attr("readonly","readonly");
//				$(".shopStatus").css("border","none");
//				$(".salesman input").attr("readonly","readonly");
//				$(".salesman input").css("border","none");
//			break;
//		}
	});

//	//验证门店名称
//	window.loseFocus = function(index, uniqueField){
//    	$.ajax({
//			url: blurShop_url,
//			type: method_post,
//			async: true,
//			dataType: "json",
//			data: {
//				"uniqueField": uniqueField,
//				"fieldToCheckUnique":1
//			},
//			success: function succFunction(data) {
//				console.log(data);
//				if(data.ERROR != "EC_NoError"){
//					layer.msg(data.msg);
//				}
//			}
//    	})
//	}
	
	//门店信息提交(目前门店的CUD操作未开放)
//	form.on('submit(shopsubmit)', function(data) {
//		var indexLoading = layer.load(1);
//		var ID=parseInt(data.field.ID)
//		console.log(data);
//		$.ajax({
//			url : updateShop_url,
//			type : method_post,
//			async : true,
//			dataType : "json",
//			data : {
//				"name":data.field.name,
//				"address":data.field.address,
//				"ID":parseInt(data.field.ID),
//				"companyID":parseInt(data.field.companyID),
//				"longitude":data.field.longitude,
//				"latitude":data.field.latitude,
//				"key":data.field.key,
//				"remark":data.field.remark
//			},
//			success : function succFunction(data) {
//				console.log(data);
//				if(data.ERROR == "EC_NoError"){
//					$(".store ul li[data='"+indexShopID+"'][string='"+dbname+"']").click();
//					layer.msg("修改成功");
	//					$(".toUpdateShopInfo").text("修改");
//					$(".shopInformation input").attr("readonly","readonly");//添加不可编辑状态
//					$(".shopInformation input").css("border","none");		//清除输入框边框
//					layer.close(indexLoading);
//				}else{
//					layer.msg("修改失败");
//				}
//			}
//		});
//		return false;
//	});

	//新建门店时的保存按钮
	$(".createShop").click(function () {
//		$(".dbUserName input").attr("lay-verify", "required|charDbNmae|noBlank");
//		$(".dbUserpwd input").attr("lay-verify", "required|password|noChinese");
//		$(".companyInfoSubmit").click();
		$(".shopsubmit").click();
	});
	
	//公司门店表单信息提交
	form.on('submit(shopsubmit)', function (data) {
		layer.confirm('确定创建门店"' + data.field.name + '"吗？', { icon: 3, title: '提示' }, function (index, layero) {		//点击确定
			var indexLoading = layer.load(1);
	//		var ID=parseInt(data.field.ID)
			console.log(data);
			$.ajax({
				url : createShop_Url,
				type : method_post,
				async : true,
				dataType : "json",
				data : {
					"name":data.field.name,
					"address":data.field.address,
	//				"ID":parseInt(data.field.ID),
					"companyID":parseInt(data.field.companyID),
					"longitude":data.field.longitude,
					"latitude":data.field.latitude,
	//				"key":data.field.key,
					"remark":data.field.remark
				},
				success : function succFunction(data) {
					console.log(data);
					if(data.ERROR == "EC_NoError"){
	//					$(".store ul li[data='"+indexShopID+"'][string='"+dbname+"']").click();
						layer.msg("创建成功");
						$("#name").val("");
						$("#address").val("");
						$("#companyID").val("");
						$("#longitude").val("");
						$("#latitude").val("");
						$("#remark").val("");
						$("#name").val("");
						$("#status").val("");
	//						$(".toUpdateShopInfo").text("修改");
	//					$(".shopInformation input").attr("readonly","readonly");//添加不可编辑状态
	//					$(".shopInformation input").css("border","none");		//清除输入框边框
						layer.close(indexLoading);
					}else{
						layer.msg("创建失败," + data.msg);
						layer.close(indexLoading);
					}
				}
			});
			return false;
		});
	}, function (index, layero) {		//点击取消
		layer.close(index);
	})
})