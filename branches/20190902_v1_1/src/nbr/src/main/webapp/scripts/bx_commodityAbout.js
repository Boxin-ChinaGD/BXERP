layui.use(['form', 'element', 'layer', 'table', 'laypage'], function () {
	var form = layui.form;
	var element = layui.element;
	var laypage = layui.laypage;
	var table = layui.table;
	//定义常用常量
	const providerRN_url = "provider/retrieveNEx.bx";		//查询供应商
	const providerCreate_url = "provider/createEx.bx";		//创建供应商
	const providerUpdate_url = "provider/updateEx.bx";		//修改供应商
	const providerDelete_url = "provider/deleteEx.bx";		//删除供应商
	const providerR1_url = "provider/retrieve1Ex.bx";		//查询供应商详细信息
	const providerRNByFields_url = 'provider/retrieveNByFieldsEx.bx';		//模糊查询供应商
	const providerDistrictCreate_url = "providerDistrict/createEx.bx";		//创建供应商区域
	const providerDistrictUpdate_url = "providerDistrict/updateEx.bx";		//修改供应商区域
	const providerDistrictDelete_url = "providerDistrict/deleteEx.bx";		//修改供应商区域
	const brandCreate_url = "brandSync/createEx.bx";		//创建品牌
	const brandUpdate_url = "brandSync/updateEx.bx";		//修改品牌
	const brandDelete_url = "brandSync/deleteEx.bx";		//删除品牌
	const categoryParentCreate_url = "categoryParent/createEx.bx";		//创建分类
	const categoryParentUpdate_url = "categoryParent/updateEx.bx";		//修改分类
	const categoryParentDelete_url = "categoryParent/deleteEx.bx";		//删除分类
	const categoryRAll_url = "category/retrieveAllEx.bx";		//查询所有的小类
	const categoryRNByParent_url = "category/retrieveNByParent.bx";		//大类查询小类
	const categoryCreate_url = "categorySync/createEx.bx";		//创建小分类
	const categoryUpdate_url = "categorySync/updateEx.bx";		//修改小分类
	const categoryDelete_url = "categorySync/deleteEx.bx";		//删除小分类
	const packageUnitCreate_url = "packageUnit/createEx.bx";		//创建商品单位
	const packageUnitUpdate_url = "packageUnit/updateEx.bx";		//修改商品单位
	const packageUnitDelete_url = "packageUnit/deleteEx.bx";		//删除商品单位
	const commodityPropertyUpdate_url = "commodityProperty/updateEx.bx";		//修改商品属性
	const providerRNToCheckUniqueField_url = 'provider/retrieveNToCheckUniqueFieldEx.bx';		//判断供应商是否已存在
	const categoryRNToCheckUniqueField_url = "category/retrieveNToCheckUniqueFieldEx.bx";		//判断商品小类是否已存在
	const method_post = "POST";
	const method_get = "GET";
	//初始加载页面
	layer.ready(function () {
		providerList({ "pageIndex": 1 }, providerRN_url);
		$("#providerDistrict p:first").addClass("provideDistrictSelect").children().css("color", "#000");
		$("#categoryParent p:first").addClass("categoryParentSelect");
	});
	//检查只能输入中英文数字
	function check_NumEnChinese (string) {
		if (/^[\u4E00-\u9FA5A-Za-z0-9]+$/.test(string)) {
			return true;
		} else {
			return "输入的值不符合数据格式<br/>数据要求为：中英文数字，不能输入空格";
		}
	}
	//检查只能输入中英文数字、空格
	function check_NumEnChineseSpace (string) {
		if (/^[\u4E00-\u9FA5A-Za-z0-9\s]+$/.test(string)) {
			return true;
		} else {
			return "输入的值不符合数据格式<br/>数据要求为：中英文数字、空格，不能只输入空格";
		}
	}
	//检查输入的数据是否为空或皆为空格
	function check_EmptyOrAllSpaces (string) {
		if (string && !/^\s*$/.test(string)) {
			return true;
		} else {
			return "数据不符合要求,不能为空且不能全为空格！";
		}
	}
	//检查输入的数据是否和已有的重复
	function check_Repeat (string, otherObjList, keyClass) {
		var otherObjListLength = otherObjList.length;
		for (var i = 0; i < otherObjListLength; i++) {
			var otherObjString = $(otherObjList[i]).find(keyClass).val();
			if (otherObjString == string) {
				return false;
			} else {
				continue;
			}
		}
	}
	//检查唯一字段的方法
	function toCheckUniqueField (url, requestData, repeatText) {
		var checkUniqueFieldResult = false;
		$.ajax({
			url: url,
			type: method_post,
			async: false,
			data: requestData,
			dataType: "json",
			success: function succFunction (data) {
				console.log(data);
				if (data.ERROR != "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg, { "id": "checkUniqueFieldMsg" });
					} else {
						layer.msg(repeatText, { "id": "checkUniqueFieldMsg" });
					}
					return;
				}
				checkUniqueFieldResult = true;
				console.log("ajax:通过了唯一字段检查");
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		})
		return checkUniqueFieldResult;
	}
	//表单数据验证
	fieldFormat(form);
	//供应商区域的CUD操作
	window.providerDistrictManage = function (index) {
		var index = $(index);
		window.event.stopPropagation();		//阻止冒泡事件
		$(".newDistrict").parent().remove();
		if (index.hasClass("add")) {
			$("#providerDistrict p").removeClass("provideDistrictSelect").children("input").attr("style", "color: #000");
			$("#providerDistrict p:first").after(
				'<p class="rowSpace provideDistrictSelect"><input type="text" class="layui-input newDistrict" maxlength="20" /></p>'
			);
			$(".newDistrict").focus();
			$(".newDistrict").blur(function () {
				var districtName = $(this).val();
				var isNotEmptyOrAllSpaces = check_EmptyOrAllSpaces(districtName);
				if (isNotEmptyOrAllSpaces == true) {
					console.log("通过了数据检查");
					var otherProviderDistrict = $(this).parent().siblings();
					var keyClass = ".district";
					var checkRepeatResult = check_Repeat(districtName, otherProviderDistrict, keyClass);
					if (checkRepeatResult != false) {
						var obj = "providerDistrict";
						var succText = "新增供应商区域成功";
						var failText = "新增供应商区域失败";
						var requestDataOfCreate = { "name": districtName };
						createObject(providerDistrictCreate_url, method_post, requestDataOfCreate, obj, succText, failText, $(this));
					} else {
						$(this).focus();
						layer.msg("该供应商区域已存在，请重新修改");
					}
				} else {
					$(this).parent().remove();
					layer.msg(isNotEmptyOrAllSpaces);
					$("#providerDistrict p").each(function () {
						console.log($(this).children(".districtId").val());
						if ($(this).children(".districtId").val() == clickDistrictID) {
							$(this).click();
						}
					});
				}
			})
		} else if (index.hasClass("update")) {
			index.parent().siblings().find("input.district").attr("readOnly", "readOnly");
			var districtID = index.siblings("input.districtId").val();
			if (districtID == 1) {
				layer.msg("默认区域不可修改");
				return;
			}
			var districtNameBeforeUpdate = index.siblings("input.district").val();		//获取修改前的供应商区域名称
			index.parent().click();
			index.siblings("input.district").removeAttr("readOnly").focus();
			index.siblings("input.district").blur(function () {
				var districtName = $(this).val();
				var checkEmptyOrAllSpacesResult = check_EmptyOrAllSpaces(districtName);
				if (checkEmptyOrAllSpacesResult == true) {
					var otherProviderDistrict = $(this).parent().siblings();
					var keyClass = ".district";
					var checkRepeatResult = check_Repeat(districtName, otherProviderDistrict, keyClass);
					if (checkRepeatResult != false) {
						var obj = "providerDistrict";
						var succText = "修改供应商区域成功";
						var failText = "修改供应商区域失败";
						var requestDataOfUpdate = { "ID": districtID, "name": districtName };
						updateObject(providerDistrictUpdate_url, method_post, requestDataOfUpdate, obj, succText, failText, $(this), districtNameBeforeUpdate);
					} else {
						layer.msg("该供应商区域已存在，请重新修改");
						$(this).val(districtNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
					}
				} else {
					layer.msg(checkEmptyOrAllSpacesResult);
					$(this).val(districtNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
				}
			});
		} else if (index.hasClass("delete")) {
			var districtID = index.siblings("input.districtId").val();
			if (districtID == 1) {
				layer.msg("默认区域不可删除");
				return;
			}
			var districtName = index.siblings("input.district").val();
			var objToRemove = index.parent("p");
			var obj = "providerDistrict";
			var hintText = '确定要删除"' + districtName + '"吗', succText = "删除供应商区域成功", failText = "删除供应商区域失败";
			var requestDataOfDelete = { "ID": districtID };
			deleteObject(providerDistrictDelete_url, method_get, requestDataOfDelete, hintText, succText, failText, obj, objToRemove);
		}
		return false;
	};
	//单击供应商区域追加样式并显示供应商信息
	var clickDistrictID = 0;		//存储用户点击的供应商区域(用于区域数据验证失败或新增失败后返回上一步的数据)
	window.providerDistrictShowManage = function (index) {
		if (datachange) {
			layer.confirm("确定要放弃之前的操作吗？", { icon: 3, title: '提示' }, function (confirmIndex) {
				datachange = false;
				showDistrictMessage(index);
				layer.close(confirmIndex);
			});
		} else {
			showDistrictMessage(index);
		}
	}
	//显示某区域下的供应商
	function showDistrictMessage (index) {
		var index = $(index);
		var districtID = index.find(".districtId").val();
		var requestDataOfSelectProvider = { "pageIndex": 1, "districtID": districtID };
		clickDistrictID = districtID;
		index.addClass("provideDistrictSelect").children("input").css("color", "#000");
		index.siblings().removeClass("provideDistrictSelect").children("input").css("color", "#000");
		showFirstDataOfTable = true;
		providerList(requestDataOfSelectProvider, providerRN_url);
	}
	//查询供应商信息
	var showFirstDataOfTable = true;		//是否显示第一条数据
	function providerList (requestData, url) {		//获取供应商列表
		var indexLoading = layer.load(1);
		$.ajax({
			url: url,
			type: method_post,
			async: true,
			data: requestData,
			dataType: "json",
			success: function succFunction (data) {
				console.log(data);
				layer.close(indexLoading);
				if (data.ERROR != "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg('查询供应商失败');
					}
					return;
				}
				//供应商列表
				$("#provider").html("");
				if (data.objectList.length <= 0) {
					providerListIsNull();
					$("#providerPage").hide();
				} else {
					var providerID = $("#providerMessage .ID").val();
					for (var i = 0; i < data.objectList.length; i++) {
						$("#provider").append(
							"<p class='rowSpace paddingLeft' onclick='providerMessageManage(this)'>" +
							"<input type='hidden' class='ID' value='" + data.objectList[i].ID + "' />" +
							"<input type='text' title='" + data.objectList[i].name + "' disabled value='" + data.objectList[i].name + "'>" +
							"<span class='blackIcon delete' onclick='providerManage(this)' title='删除'>x</span>" +
							"<span class='blackIcon update' onclick='providerManage(this)' title='修改'><i class='layui-icon layui-icon-edit'></i></span> " +
							"<span class='blackIcon add' onclick='providerManage(this)' title='新增'>+</span>" +
							"</p>"
						);
						if (providerID == data.objectList[i].ID) {		//保存的供应商ID与跳转页面的相同，则添加对应的样式
							$("#provider p:first").addClass("provideSelect").children("span").show();
						}
					}
					if (showFirstDataOfTable) {
						$("#provider p:first").click();
						showFirstDataOfTable = false;
					}
					providerPaginationRender(data.count, requestData, url);
				}
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		})
	}
	//渲染供应商页码
	function providerPaginationRender (count, requestData, url) {
		laypage.render({
			elem: 'providerPage',		//注意:这里的是 ID，不用加 # 号
			count: count,		//数据总数，从服务端得到
			next: ">",
			prev: "<",
			groups: 3,
			hash: "indexCurr",
			curr: requestData.pageIndex,
			jump: function (obj, first) {
				if (!first) {		//首次不执行
					requestData.pageIndex = obj.curr;
					showFirstDataOfTable = true;
					providerList(requestData, url);
				}
				$("#providerPage div a").each(function () {
					if ($(this).hasClass("layui-disabled")) {
						$(this).css("display", "none")
					}
				});
				$("#providerPage").show();
			}
		});
	}
	//当供应商数据为空时（模糊搜索后、根据供应商区域查看）
	function providerListIsNull () {
		$("#provider").prepend(
			"<p class='rowSpace paddingLeft provideSelect' onclick='providerMessageManage(this)'>" +
			"<input type='hidden' class='ID' value='0' />" +
			"<input type='text' disabled placeholder='没有内容，增加试试' />" +
			"<span class='blackIcon add' onclick='providerManage(this)' title='新增' style='margin-right: 10px; display: block;'>+</span>" +
			"</p>"
		);
		$("#provider p:first").click();
	}
	//单击供应商事件
	window.providerMessageManage = function (index) {
		if (datachange) {
			layer.confirm("确定要放弃之前的操作吗？", { icon: 3, title: '提示' }, function (confirmIndex) {
				datachange = false;
				showProviderMessage(index);
				layer.close(confirmIndex);
			});
		} else {
			showProviderMessage(index);
		}
	}
	//显示供应商的详细信息
	function showProviderMessage (index) {
		$(".layui-form-item input").removeClass("inputColor");
		console.log("datachange " + datachange);
		var index = $(index);
		var providerID = index.children("input.ID").val();		//获取选择的供应商的ID
		index.addClass("provideSelect").siblings("p").removeClass("provideSelect");		//单击供应商追加样式
		index.children("span").show();		//显示操作按钮
		index.siblings("p").children("span").hide();		///隐藏其他供应商的操作按钮
		$("#providerMessageButton").hide();		//隐藏下方按钮
		$("#providerMessage input").attr({ "readOnly": "readOnly", "placeholder": "" });
		$("#selectDistrict select option").attr("disabled", "disabled");		//区域下拉框更改为不可用
		if (providerID != 0) {
			getProviderMessage({ "ID": providerID });		//调用getProviderMessage()方法
		} else {
			form.val("providerDetails", {
				"ID": "",
				"name": "",
				"contactName": "",
				"districtID": 1,
				"address": "",
				"mobile": ""
			})
		}
	}
	//获取供应商详细信息(供应商的单击事件(和 新增供应商的取消按钮 和 修改供应商详细信息的返回按钮))
	function getProviderMessage (providerID) {
		var indexLoading = layer.load(1);
		$.ajax({
			url: providerR1_url,
			async: true,
			data: providerID,
			dataType: "json",
			success: function succFunction (data) {
				console.log(data);
				layer.close(indexLoading);
				if (data.ERROR != "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg('查询供应商详情失败');
					}
					return;
				}
				form.val("providerDetails", {
					"ID": data.object.ID,
					"name": data.object.name,
					"contactName": data.object.contactName,
					"districtID": data.object.districtID,
					"address": data.object.address,
					"mobile": data.object.mobile
				})
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		});
	}
	//供应商的CUD操作
	window.providerManage = function (index) {
		var index = $(index);
		checkUniqueField_providerNameResult = true;
		checkUniqueField_providerMobileResult = true;
		window.event.stopPropagation();		//阻止事件冒泡
		if (index.hasClass("add")) {
			var provideDistrictID_selected = $(".provideDistrictSelect .districtId").val();
			provideDistrictID_selected = provideDistrictID_selected == 0 ? 1 : provideDistrictID_selected;
			index.parent().removeClass("provideSelect").children("span").hide();
			$("#selectDistrict select option").removeAttr("disabled");		//区域下拉框更改为可用
			form.render('select', 'providerDetails');		//刷新select选择框渲染
			form.val("providerDetails", {
				"ID": "",
				"name": "",
				"contactName": "",
				"districtID": provideDistrictID_selected,		//默认选择默认区域
				"address": "",
				"mobile": ""
			})
			$("#providerMessage input").attr("placeholder", "请填写").removeAttr("readOnly");
			$("#providerMessage #name").focus();
			$("#providerMessageButton").show();
		} else if (index.hasClass("update")) {
			var ID = index.siblings("input").val();
			if (ID == 1) {
				layer.msg("默认供应商不可修改");
				return;
			}
			$("#selectDistrict select option").removeAttr("disabled");		//区域下拉框更改为可用
			form.render('select', 'providerDetails');		//刷新select选择框渲染
			$("#providerMessage input").attr("placeholder", " ").removeAttr("readOnly", "readOnly");
			$("#providerMessage #name").focus();
			$("#providerMessageButton").show();
		} else if (index.hasClass("delete")) {
			var providerID = $("#providerMessage input.ID").val();
			if (providerID == 1) {
				layer.msg("默认供应商不可删除");
				return;
			}
			var providerName = $("#provider .provideSelect input[type='text']").val();
			var objToRemove = index.parent("p");
			var obj = "provider";
			var hintText = '确定要删除"' + providerName + '"吗', succText = "删除供应商成功", failText = "删除供应商失败";
			var requestDataOfDelete = { "ID": providerID };
			deleteObject(providerDelete_url, method_get, requestDataOfDelete, hintText, succText, failText, obj, objToRemove);
		}
		return false;
	};

	//响应回车键按下的处理
	$("#providerMessage").on("keydown","input",function(){
	    var e = event || window.event || arguments.callee.caller.arguments[0];
	//判断是否按键为回车键
	    if(e && e.keyCode==13) {
	        var inputs = $("#providerMessage input");
	        var idx = inputs.index(this);     // 获取当前焦点输入框所处的位置
	        if (idx == inputs.length - 2) {       // 判断是否是最后一个输入框
	            $(".confirmCreateProvider").click(); // 提交表单
	        } else {
	            inputs[idx + 1].focus(); // 设置焦点
	            inputs[idx + 1].select(); // 选中文字
	        }
	    }
	});
	
	//表单提交（供应商的新增与修改）
	form.on('submit(providerCreate)', function (data) {
		console.log("checkUniqueField_providerNameResult=" + checkUniqueField_providerNameResult);
		console.log("checkUniqueField_providerMobileResult=" + checkUniqueField_providerMobileResult);
		if (checkUniqueField_providerNameResult) {
			if (checkUniqueField_providerMobileResult) {
				if (data.field.ID) {		//存在ID，为修改操作
					var obj = "provider";
					var succText = "修改供应商成功";
					var failText = "修改供应商失败";
					var requestDataOfUpdate = data.field;
					layer.confirm("确定要修改供应商" + data.field.name + "吗？", { icon: 3, title: '提示' }, function (index) {
						updateObject(providerUpdate_url, method_post, requestDataOfUpdate, obj, succText, failText, $("#providerMessage #name"), "");
					});
				} else {		//不存在ID，为创建操作
					delete data.field["ID"];
					var obj = "provider";
					var succText = "新增供应商成功";
					var failText = "新增供应商失败";
					var requestDataOfCreate = data.field;
					layer.confirm("确定要创建供应商" + data.field.name + "吗？", { icon: 3, title: '提示' }, function (index) {
						datachange = false;
						createObject(providerCreate_url, method_post, requestDataOfCreate, obj, succText, failText, $("#providerMessage #name"));
					});
				}
			} else {
				$("#providerMessage .mobile").focus();
				layer.msg("该联系电话已存在，请重新修改", { "id": "checkUniqueFieldMsg" });
			}
		} else {
			$("#providerMessage #name").focus();
			layer.msg("该供应商已存在，请重新修改", { "id": "checkUniqueFieldMsg" });
		}
		return false;
	});
	//检查供应商名称的唯一性
	var checkUniqueField_providerNameResult = true;
	window.checkUniqueField_providerName = function (index) {
		var providerName = index.val();
		if (/^[\u4E00-\u9FA5A-Za-z0-9]+$/.test(providerName)) {
			var providerID = $("#providerMessage .ID").val() == "" ? 0 : $("#providerMessage .ID").val();
			var repeatText = "已有供应商拥有此名称";
			var requestData = { "ID": providerID, "uniqueField": providerName, "fieldToCheckUnique": 1 };
			var checkUniqueFieldResult = toCheckUniqueField(providerRNToCheckUniqueField_url, requestData, repeatText);
			if (checkUniqueFieldResult == true) {
				checkUniqueField_providerNameResult = true;
			} else {
				index.focus();
				checkUniqueField_providerNameResult = false;
			}
		} else {
			index.focus();
			layer.msg("供应商名称格式不符合要求，请输入中英文和数字，不能为空，不能有空格");
		}
	}
	//检查供应商联系电话的唯一性
	var checkUniqueField_providerMobileResult = true;
	window.checkUniqueField_providerMobile = function (index) {
		var providerMobile = index.val();
		if (providerMobile.length >= 7 && providerMobile.length <= 24) {
			var providerID = $("#providerMessage .ID").val() == "" ? 0 : $("#providerMessage .ID").val();
			var repeatText = "已有供应商拥有此联系电话";
			var requestData = { "ID": providerID, "uniqueField": providerMobile, "fieldToCheckUnique": 2 };
			var checkUniqueFieldResult = toCheckUniqueField(providerRNToCheckUniqueField_url, requestData, repeatText);
			console.log("checkUniqueFieldResult=" + checkUniqueFieldResult);
			if (checkUniqueFieldResult == true) {
				checkUniqueField_providerMobileResult = true;
			} else {
				index.focus();
				checkUniqueField_providerMobileResult = false;
			}
		} else {
			index.focus();
			checkUniqueField_providerMobileResult = true;
		}
	}
	//判断用户是否修改供应商的数据(文本框)
	var datachange = false;
	window.check_ifDataChange = function () {
		datachange = true;
		console.log("datachange =" + datachange);
	}
	//判断用户是否修改供应商的数据(区域下拉框)
	form.on('select(providerDistrictList)', function (data) {
		if (data.elem != data.value) {
			datachange = true;
		}
	});
	
	$("#brand").on('keypress', '.newBrand', function(event) {
		if (event.keyCode == "13") {
			doCreateBrand();
		}
	});
	
	function doCreateBrand() {
		var brandName = $("#brand p input.newBrand").val();
		var checkEmptyOrAllSpacesResult = check_EmptyOrAllSpaces(brandName);
		if (checkEmptyOrAllSpacesResult == true) {
			var checkStringResult = check_NumEnChinese(brandName);
			if (checkStringResult == true) {
				console.log("通过了数据检查");
				var otherBrand = $("#brand p input.newBrand").parent().siblings();
				var keyClass = ".brand";
				var checkRepeatResult = check_Repeat(brandName, otherBrand, keyClass);
				if (checkRepeatResult != false) {
					var obj = "brand";
					var succText = "新增商品品牌成功";
					var failText = "新增商品品牌失败";
					var requestDataOfCreate = { "name": brandName, "returnObject": 1 };
					createObject(brandCreate_url, method_post, requestDataOfCreate, obj, succText, failText, $("#brand p input.newBrand"));
				} else {
					$("#brand p input.newBrand").focus();
					layer.msg("该商品品牌已存在，请重新修改");
				}
			} else {
				$("#brand p input.newBrand").focus();
				layer.msg(checkStringResult);
			}
		} else {
			$("#brand p input.newBrand").parent().remove();
			$("#brandExitButton").hide();
			$("#brandCreateButton").val("新建");
			layer.msg(checkEmptyOrAllSpacesResult);
		}
	}
	
	//品牌的新增
	$("#brandButton input").click(function () {
		if ($(this).val() == "新建") {
			$("#brand").prepend(
				'<p class="rowSpace"><input type="text" class="layui-input newBrand" maxlength="20"/></p>');
			$(this).val("确定");
			$("#brandExitButton").show();
			$("#brand p input.newBrand").focus();
		} else if ($(this).val() == "确定") {
			doCreateBrand();
		} else if ($(this).val() == "取消") {
			layer.msg("已取消新增品牌的操作");
			$("#brand p:first").remove();
			$("#brandExitButton").hide();
			$("#brandCreateButton").val("新建");
		}
	});
	
	function doUpdateBrand(brandID, brandNameBeforeUpdate, _this) {
		var brandName = $(_this).val();
		var checkEmptyOrAllSpacesResult = check_EmptyOrAllSpaces(brandName);
		console.log("checkEmptyOrAllSpacesResult:" + checkEmptyOrAllSpacesResult);
		if (checkEmptyOrAllSpacesResult == true) {
			var checkStringResult = check_NumEnChinese(brandName);
			if (checkStringResult == true) {
				var otherBrand = $(_this).parent().siblings();
				var keyClass = ".brand";
				var checkRepeatResult = check_Repeat(brandName, otherBrand, keyClass);
				if (checkRepeatResult != false) {
					var obj = "brand";
					var succText = "修改商品品牌成功";
					var failText = "修改商品品牌失败";
					var requestDataOfUpdate = { "ID": brandID, "name": brandName };
					updateObject(brandUpdate_url, method_post, requestDataOfUpdate, obj, succText, failText, $(_this), brandNameBeforeUpdate);
				} else {
					layer.msg("该商品品牌已存在，请重新修改");
					$(_this).val(brandNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
				}
			} else {
				layer.msg(checkStringResult);
				$(_this).val(brandNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
			}
		} else {
			alert("checkEmptyOrAllSpacesResult:" + checkEmptyOrAllSpacesResult)
			layer.msg(checkEmptyOrAllSpacesResult);
			$(_this).val(brandNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
		}
	}
	
	//品牌的修改与删除
	window.brandManage = function (index) {
		var index = $(index);
		window.event.stopPropagation();		//阻止冒泡事件
		if (index.hasClass("update")) {
			index.parent().siblings().find("input.brand").attr("readOnly", "readOnly");
			var brandID = index.siblings("input.ID").val();
			if (brandID == 1) {
				layer.msg("默认品牌不可修改");
				return;
			}
			var brandNameBeforeUpdate = index.siblings("input.brand").val();		//获取修改前的商品品牌名称
			index.siblings("input.brand").removeAttr("readOnly").focus();
			index.siblings("input.brand").blur(function () {
				doUpdateBrand(brandID, brandNameBeforeUpdate, $(this));
			});
			
			index.siblings("input.brand").bind('keypress', function(event) {
				if (event.keyCode == "13") {
					doUpdateBrand(brandID, brandNameBeforeUpdate, $(this));
				}
			});
		} else if (index.hasClass("delete")) {
			var brandID = index.siblings("input.ID").val();
			if (brandID == 1) {
				layer.msg("默认品牌不可删除");
				return;
			}
			var brandName = index.siblings("input.brand").val();
			var objToRemove = index.parent("p");
			var obj = "brand";
			var hintText = '确定要删除"' + brandName + '"吗', succText = "删除商品品牌成功", failText = "删除商品品牌失败";
			var requestDataOfDelete = { "ID": brandID };
			console.log(requestDataOfDelete);
			deleteObject(brandDelete_url, method_get, requestDataOfDelete, hintText, succText, failText, obj, objToRemove);
		}
	}
	//商品大类的CUD
	window.categoryParentManage = function (index) {
		var index = $(index);
		$(".newCategoryParent").parent().remove();
		window.event.stopPropagation();		//阻止冒泡事件
		if (index.hasClass("add")) {
			index.parent().removeClass("categoryParentSelect");
			index.hide().siblings(".update, .delete").hide();
			$("#categoryParent p:first").after(
				'<p class="rowSpace categoryParentSelect"><input type="text" class="layui-input newCategoryParent" maxlength="10" /></p>'
			);
			$(".newCategoryParent").focus();
			$(".newCategoryParent").blur(function () {
				doCreateCategoryParentName($(this));
			})
			$(".newCategoryParent").bind('keypress', function(event) {
				if (event.keyCode == "13") {
					doCreateCategoryParentName($(this));
				}
			})
		} else if (index.hasClass("update")) {
			index.parent().siblings().find("input.categoryParent").attr("readOnly", "readOnly");
			var categoryParentID = index.siblings("input").val();
			if (categoryParentID == 1) {
				layer.msg("默认分类不可修改");
				return;
			}
			var categoryParentNameBeforeUpdate = index.siblings("input.categoryParent").val();		//获取修改前的商品大类名称
			index.siblings("input.categoryParent").removeAttr("readOnly").focus();
			index.siblings("input.categoryParent").blur(function () {
				doUpdateCategoryParentName(categoryParentID, categoryParentNameBeforeUpdate, $(this));
			});
			index.siblings("input.categoryParent").bind('keypress', function(event) {
				if (event.keyCode == "13") {
					doUpdateCategoryParentName(categoryParentID, categoryParentNameBeforeUpdate, $(this));
				}
			})
		} else if (index.hasClass("delete")) {
			var categoryParentID = index.siblings("input.categoryParentID").val();
			if (categoryParentID == 1) {
				layer.msg("默认分类不可删除");
				return;
			}
			var categoryParentName = index.siblings("input.categoryParent").val();
			var objToRemove = index.parent("p");
			var obj = "categoryParent";
			var hintText = '确定要删除"' + categoryParentName + '"吗', succText = "删除商品大类成功", failText = "删除商品大类失败";
			var requestDataOfDelete = { "ID": categoryParentID };
			deleteObject(categoryParentDelete_url, method_get, requestDataOfDelete, hintText, succText, failText, obj, objToRemove);
		}
	}
	
	function doCreateCategoryParentName(_this) {
		var categoryParentName = $(_this).val();
		var checkEmptyOrAllSpacesResult = check_EmptyOrAllSpaces(categoryParentName);
		if (checkEmptyOrAllSpacesResult == true) {
			var checkStringResult = check_NumEnChinese(categoryParentName);
			if (checkStringResult == true) {
				console.log("通过了数据检查");
				var otherCategoryParent = $(_this).parent().siblings();
				var keyClass = ".categoryParent";
				var checkRepeatResult = check_Repeat(categoryParentName, otherCategoryParent, keyClass);
				console.log("checkRepeatResult=" + checkRepeatResult);
				if (checkRepeatResult != false) {
					var obj = "categoryParent";
					var succText = "新增商品大类成功";
					var failText = "新增商品大类失败";
					var requestDataOfCreate = { "name": categoryParentName };
					createObject(categoryParentCreate_url, method_post, requestDataOfCreate, obj, succText, failText, $(_this));
				} else {
					$(_this).focus();
					layer.msg("该商品大类已存在，请重新修改");
				}
			} else {
				$("#categoryParent p input.newCategoryParent").focus().parent().click();
				layer.msg(checkStringResult);
			}
		} else {
			$(_this).parent().remove();
			layer.msg(checkEmptyOrAllSpacesResult);
			index.parent().addClass("categoryParentSelect");
			index.show().siblings(".update, .delete").show();
		}
	}
	
	function doUpdateCategoryParentName(categoryParentID, categoryParentNameBeforeUpdate, _this) {
		var categoryParentName = $(_this).val();
		var checkStringResult = check_NumEnChinese(categoryParentName);
		if (checkStringResult == true) {
			console.log("通过了数据检查");
			var otherCategoryParent = $(_this).parent().siblings();
			var keyClass = ".categoryParent";
			var checkRepeatResult = check_Repeat(categoryParentName, otherCategoryParent, keyClass);
			console.log("checkRepeatResult=" + checkRepeatResult);
			if (checkRepeatResult != false) {
				var obj = "categoryParent";
				var succText = "修改商品大类成功";
				var failText = "修改商品大类失败";
				var requestDataOfUpdate = { "ID": categoryParentID, "name": categoryParentName };
				updateObject(categoryParentUpdate_url, method_post, requestDataOfUpdate, obj, succText, failText, $(_this), categoryParentNameBeforeUpdate);
			} else {
				$(_this).val(categoryParentNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
				layer.msg("该商品分类已存在，请重新修改");
			}
		} else {
			layer.msg(checkStringResult);
			$(_this).val(categoryParentNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
		}
	}
	
	//选择商品大类并查询其下所有的商品小类
	window.categoryParentShowManage = function (index) {
		var index = $(index);
		var categoryParentID = index.children("input[type=hidden]").val();
		index.children("span").show();
		index.siblings().children("span.blackIcon").hide();
		index.addClass("categoryParentSelect").siblings().removeClass("categoryParentSelect");
		if (categoryParentID == 0) {		//查询全部商品小类
			categoryList(categoryRAll_url, {});
		} else {		//根据商品大类查询商品小类
			categoryList(categoryRNByParent_url, { "ID": categoryParentID });
		}
	}
	//查询商品小类
	function categoryList (url, categoryParentID) {
		var indexLoading = layer.load(1);
		$.ajax({
			url: url,
			type: method_post,
			async: true,
			data: categoryParentID,
			dataType: "json",
			success: function succFunction (data) {
				if (data.ERROR != "EC_NoError") {
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg("查询商品小类失败");
					}
				} else {		//渲染显示查询到的商品小类
					$("#category").html("");
					if (data.categoryList.length <= 0) {
						categoryDateIsNull();
					} else {
						var categoryListLength = data.categoryList.length;
						for (var i = 0; i < categoryListLength; i++) {
							$("#category").append(
								'<p class="rowSpace">' +
								'<input type="hidden" class="parentID" value="' + data.categoryList[i].parentID + '" />' +
								'<input type="hidden" class="ID" value="' + data.categoryList[i].ID + '" />' +
								'<input class="category" readOnly="readOnly" maxlength="10" value="' + data.categoryList[i].name + '"/>' +
								'<span class="blackIcon delete" onclick="categoryManage(this)" title="删除">x</span>' +
								'<span class="blackIcon update" onclick="categoryManage(this)" title="修改"><i class="layui-icon layui-icon-edit"></i></span>' +
								'<span class="blackIcon add" onclick="categoryManage(this)" title="新增">+</span>' +
								'</p>'
							);
						}
					}
				}
				layer.close(indexLoading);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		});
	}
	//当商品小类数据空时
	window.categoryDateIsNull = function () {
		$("#category").append(
			'<div class="categoryQueryHints">没有内容，请添加<i class="layui-icon layui-icon-add-1 add" onclick="categoryManage(this)" title="新增"></i></div>'
		);
	}
	//商品小类的CUD
	window.categoryManage = function (index) {
		var index = $(index);
		$(".newCategory").parent().remove();
		window.event.stopPropagation();		//阻止冒泡事件
		if (index.hasClass("add")) {
			$(".categoryQueryHints").remove();
			$("#category").prepend(
				'<p class="rowSpace"><input type="text" class="layui-input newCategory" maxlength="10" /></p>'
			);
			$(".newCategory").focus();
			$(".newCategory").blur(function () {
				doCreateCategory($(this));
			})
			$(".newCategory").bind('keypress', function(event) {
				if (event.keyCode == "13") {
					doCreateCategory($(this));
				}
			})
		} else if (index.hasClass("update")) {
			index.parent().siblings().find("input.category").attr("readOnly", "readOnly");
			var categoryID = index.siblings("input.ID").val();
			if (categoryID == 1) {
				layer.msg("默认分类不可修改");
				return;
			}
			var categoryNameBeforeUpdate = index.siblings("input.category").val();		//获取修改前商品小类的名称
			index.siblings("input.category").removeAttr("readOnly").focus();
			index.siblings("input.category").blur(function () {
				doUpdateCategory(index, categoryID, categoryNameBeforeUpdate, $(this));
			});
			index.siblings("input.category").bind('keypress', function(event) {
				if (event.keyCode == "13") {
					doUpdateCategory(index, categoryID, categoryNameBeforeUpdate, $(this));
				}
			})
		} else if (index.hasClass("delete")) {
			var categoryID = index.siblings("input.ID").val();
			if (categoryID == 1) {
				layer.msg("默认分类不可删除");
				return;
			}
			var categoryName = index.siblings("input.category").val();
			var objToRemove = index.parent("p");
			var obj = "category";
			var hintText = '确定要删除"' + categoryName + '"吗', succText = "删除商品小类成功", failText = "删除商品小类失败";
			var requestDataOfDelete = { "ID": categoryID };
			deleteObject(categoryDelete_url, method_get, requestDataOfDelete, hintText, succText, failText, obj, objToRemove);
		}
	}
	
	function doCreateCategory(_this) {
		var categoryName = $(_this).val();
		var checkEmptyOrAllSpacesResult = check_EmptyOrAllSpaces(categoryName);
		if (checkEmptyOrAllSpacesResult == true) {
			var checkStringResult = check_NumEnChinese(categoryName);
			if (checkStringResult == true) {
				var repeatText = "已有商品小类拥有此名称";
				var requestData = { "uniqueField": categoryName, "fieldToCheckUnique": 1 };
				var checkUniqueFieldResult = toCheckUniqueField(categoryRNToCheckUniqueField_url, requestData, repeatText);
				console.log("checkUniqueFieldResult=" + checkUniqueFieldResult);
				if (checkUniqueFieldResult == true) {
					var obj = "category";
					var succText = "新增商品小类成功";
					var failText = "新增商品小类失败";
					var categoryParentID = $(".categoryParentSelect .categoryParentID").val() == 0 ? 1 : $(".categoryParentSelect .categoryParentID").val();
					var requestDataOfCreate = { "name": categoryName, "parentID": categoryParentID, "returnObject": 1 };
					createObject(categoryCreate_url, method_post, requestDataOfCreate, obj, succText, failText, $(_this));
				} else {
					$(_this).focus();
				}
			} else {
				$(_this).focus();
				layer.msg(checkStringResult);
			}
		} else {
			$(_this).parent().remove();
			layer.msg(checkEmptyOrAllSpacesResult);
			if ($("#category>.rowSpace").length <= 0) {
				categoryDateIsNull();
			}
		}
	}
	
	function doUpdateCategory(index, categoryID, categoryNameBeforeUpdate, _this){
		var categoryName = $(_this).val();
		var checkStringResult = check_NumEnChinese(categoryName);
		if (checkStringResult == true) {
			console.log("通过了数据检查");
			var repeatText = "已有商品小类拥有此名称";
			var categoryID = index.siblings("input.ID").val();
			var requestData = { "uniqueField": categoryName, "ID": categoryID, "fieldToCheckUnique": 1 };
			var checkUniqueFieldResult = toCheckUniqueField(categoryRNToCheckUniqueField_url, requestData, repeatText);
			console.log("checkUniqueFieldResult=" + checkUniqueFieldResult);
			if (checkUniqueFieldResult == true) {
				var obj = "category";
				var succText = "修改商品小类成功";
				var failText = "修改商品小类失败";
				var categoryParentID = index.siblings("input.parentID").val();
				var requestDataOfUpdate = { "ID": categoryID, "name": categoryName, "parentID": categoryParentID };
				updateObject(categoryUpdate_url, method_post, requestDataOfUpdate, obj, succText, failText, $(_this), categoryNameBeforeUpdate);
			} else {
				$(_this).val(categoryNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
			}
		} else {
			layer.msg(checkStringResult);
			$(_this).val(categoryNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
		}
	}
	
	//商品单位的新增
	$("#unitButton input").click(function () {
		if ($(this).val() == "新建") {
			$("#unit").prepend('<p class="rowSpace"><input type="text" class="layui-input newUnit" maxlength="8"/></p>');
			$(this).val("确定");
			$("#unitExitButton").show();
			$("#unit p input.newUnit").focus();
		} else if ($(this).val() == "确定") {
			var unitName = $("#unit p input.newUnit").val();
			var checkEmptyOrAllSpacesResult = check_EmptyOrAllSpaces(unitName);
			if (checkEmptyOrAllSpacesResult == true) {
				var checkStringResult = check_NumEnChineseSpace(unitName);
				if (checkStringResult == true) {
					console.log("通过了数据检查");
					var otherPackageUnit = $("#unit p input.newUnit").parent().siblings();
					var keyClass = ".unit";
					var checkRepeatResult = check_Repeat(unitName, otherPackageUnit, keyClass);
					if (checkRepeatResult != false) {
						var obj = "unit";
						var succText = "新增商品包装单位成功";
						var failText = "新增商品包装单位失败";
						var requestDataOfCreate = { "name": unitName };
						createObject(packageUnitCreate_url, method_post, requestDataOfCreate, obj, succText, failText, $("#unit p input.newUnit"));
					} else {
						$("#unit p input.newUnit").focus();
						layer.msg("该商品单位已存在，请重新输入");
					}
				} else {
					$("#unit p input.newUnit").focus();
					layer.msg(checkStringResult);
				}
			} else {
				$("#unit p input.newUnit").parent().remove();
				$("#unitExitButton").hide();
				$("#unitCreateButton").val("新建");
				layer.msg(checkEmptyOrAllSpacesResult);
			}
		} else if ($(this).val() == "取消") {
			layer.msg("已取消新增单位的操作");
			$("#unit p:first").remove();
			$("#unitExitButton").hide();
			$("#unitCreateButton").val("新建");
		}
	});
	//商品单位的修改与删除
	window.unitManage = function (index) {
		var index = $(index);
		window.event.stopPropagation();		//阻止冒泡事件
		if (index.hasClass("update")) {
			index.parent().siblings().find("input.unit").attr("readOnly", "readOnly");
			var unitID = index.siblings("input.ID").val();
			var unitNameBeforeUpdate = index.siblings("input.unit").val();		//获取修改前的单位名
			index.siblings("input.unit").removeAttr("readOnly").focus();
			index.siblings("input.unit").blur(function () {
				var unitName = $(this).val();
				var checkEmptyOrAllSpacesResult = check_EmptyOrAllSpaces(unitName);
				if (checkEmptyOrAllSpacesResult == true) {
					var checkStringResult = check_NumEnChineseSpace(unitName);
					if (checkStringResult == true) {
						console.log("通过了数据检查");
						var otherPackageUnit = $(this).parent().siblings();
						var keyClass = ".unit";
						var checkRepeatResult = check_Repeat(unitName, otherPackageUnit, keyClass);
						if (checkRepeatResult != false) {
							var obj = "unit";
							var succText = "修改商品包装单位成功";
							var failText = "修改商品包装单位失败";
							var requestDataOfUpdate = { "ID": unitID, "name": unitName };
							updateObject(packageUnitUpdate_url, method_post, requestDataOfUpdate, obj, succText, failText, $(this), unitNameBeforeUpdate);
						} else {
							layer.msg("该商品单位已存在，请重新修改");
							$(this).val(unitNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
						}
					} else {
						layer.msg(checkStringResult);
						$(this).val(unitNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
					}
				} else {
					layer.msg(checkEmptyOrAllSpacesResult);
					$(this).val(unitNameBeforeUpdate).attr("readOnly", "readOnly").unbind("blur");
				}
			});
		} else if (index.hasClass("delete")) {
			var unitID = index.siblings("input.ID").val();
			var unitName = index.siblings("input.unit").val();
			var objToRemove = index.parent("p");
			var obj = "unit";
			var hintText = '确定要删除"' + unitName + '"吗', succText = "删除商品包装单位成功", failText = "删除商品包装单位失败";
			var requestDataOfDelete = { "ID": unitID };
			deleteObject(packageUnitDelete_url, method_get, requestDataOfDelete, hintText, succText, failText, obj, objToRemove);
		}
	}
	//商品属性的修改事件
	var proeprtyNameBeforeUpdate = "";		//获取修改前的商品属性名
	window.proeprtyManage = function (index) {
		window.event.stopPropagation();		//阻止冒泡事件
		var index = $(index);
		proeprtyNameBeforeUpdate = index.siblings("input").val();		//获取修改前的商品属性名
		index.parents(".layui-form-item").siblings().find("input.proeprty").attr("readOnly", "readOnly");
		index.siblings("input").removeAttr("readOnly").focus();
		index.siblings("input").blur(function () {
			var proeprtyName = $(this).val();
			var otherCommodityProeprty = $(this).parents(".layui-form-item").siblings();
			var keyClass = ".proeprty";
			var checkRepeatResult = check_Repeat(proeprtyName, otherCommodityProeprty, keyClass);
			if (checkRepeatResult != false) {
				$("#proeprty button").click();
			} else {
				layer.msg("该商品属性已存在，请重新修改");
				$(this).val(proeprtyNameBeforeUpdate).focus();
			}
		});
		return;
	}
	form.on('submit(proeprtySubmit)', function (data) {
		var obj = "proeprty";
		var succText = "修改商品属性成功";
		var failText = "修改商品属性失败";
		var requestDataOfUpdate = data.field;
		var indexInput = $("#proeprty").find("input[readOnly!='readOnly']");
		updateObject(commodityPropertyUpdate_url, method_post, requestDataOfUpdate, obj, succText, failText, indexInput, proeprtyNameBeforeUpdate);
		return false;
	});
	//综合的新增方法，操作的对象为：providerDistrict、provider、brand、categoryParent、category、unit
	function createObject (url, method, data, obj, succText, failText, objBlur) {
		console.log("操作的对象为：" + obj);
		var indexLoading = layer.load(1);
		$.ajax({
			url: url,
			type: method,
			async: true,
			data: data,
			dataType: "json",
			success: function succFunction (data) {
				console.log(data);
				if (data.ERROR == "EC_NoError") {
					switch (obj) {
						case "providerDistrict":		//新增的为供应商区域
							var providerDistrictID = data.object.ID;
							var providerDistrictName = data.object.name;
							$(".newDistrict").parent().attr("onclick", "providerDistrictShowManage(this)");
							$(".newDistrict").parent().html(
								'<input type="hidden" class="districtId" value="' + providerDistrictID + '" />' +
								'<input type="text" class="district" value="' + providerDistrictName + '" maxlength="20" readOnly="readOnly"/>' +
								'<span class="whiteIcon delete" onclick="providerDistrictManage(this)" title="删除">x</span>' +
								'<span class="whiteIcon update" onclick="providerDistrictManage(this)" title="修改"><i class="layui-icon layui-icon-edit"></i></span>' +
								'<span class="whiteIcon add" onclick="providerDistrictManage(this)" title="新增">+</span>'
							);
							$("#providerDistrict p:eq(1)").click();
							$("#selectDistrict div select").prepend(
								"<option value='" + providerDistrictID + "'>" + providerDistrictName + "</option>"
							);
							form.render('select', 'providerDetails');		//刷新select选择框渲染
							break;
						case "provider":		//新增的为供应商
							$("#providerDistrict p:first").click();
							showFirstDataOfTable = true;
							break;
						case "brand":		//新增的为品牌
							$("#brand p input.newBrand").parent().html(
								'<input type="hidden" class="ID" value="' + data.object.ID + '" />' +
								'<input type="text" maxlength="20" class="brand" value="' + data.object.name + '" readOnly="readOnly" />' +
								'<span class="blackIcon delete" onclick="brandManage(this)" title="删除">x</span>' +
								'<span class="blackIcon update" onclick="brandManage(this)" title="修改"><i class="layui-icon layui-icon-edit"></i></span>');
							$("#brandExitButton").hide();
							$("#brandCreateButton").val("新建");
							break;
						case "categoryParent":		//新增的为商品大类
							var categoryParentID = data.categoryParent.ID;
							var categoryParentName = data.categoryParent.name;
							$(".newCategoryParent").parent().attr("onclick", "categoryParentShowManage(this)").removeClass("categoryParentSelect");
							$(".newCategoryParent").parent().html(
								'<input type="hidden" class="categoryParentID" value="' + categoryParentID + '" />' +
								'<input type="text" class="categoryParent" value="' + categoryParentName + '" maxlength="10" readOnly="readOnly" />' +
								'<span class="blackIcon delete" onclick="categoryParentManage(this)" title="删除">x</span>' +
								'<span class="blackIcon update" onclick="categoryParentManage(this)" title="修改"><i class="layui-icon layui-icon-edit"></i></span>' +
								'<span class="blackIcon add" onclick="categoryParentManage(this)" title="新增">+</span>'
							);
							$("#categoryParent p:nth-child(2)").click();
							break;
						case "category":		//新增的为商品小类
							var categoryID = data.object.ID;
							var categoryName = data.object.name;
							var parentID = data.object.parentID;
							$(".newCategory").parent().html(
								'<input type="hidden" class="parentID" value="' + parentID + '" />' +
								'<input type="hidden" class="ID" value="' + categoryID + '" />' +
								'<input class="category" readOnly="readOnly" maxlength="10" value="' + categoryName + '"/>' +
								'<span class="blackIcon delete" onclick="categoryManage(this)" title="删除">x</span>' +
								'<span class="blackIcon update" onclick="categoryManage(this)" title="修改"><i class="layui-icon layui-icon-edit"></i></span>' +
								'<span class="blackIcon add" onclick="categoryManage(this)" title="新增">+</span>'
							);
							break;
						case "unit":
							var unitID = data.object.ID;
							var unitName = data.object.name;
							$("#unit p input.newUnit").parent().html(
								'<input type="hidden" class="ID" value="' + unitID + '" />' +
								'<input type="text" class="unit" value="' + unitName + '" readOnly="readOnly" maxlength="8"/>' +
								'<span class="blackIcon delete" onclick="unitManage(this)" title="删除">x</span>' +
								'<span class="blackIcon update" onclick="unitManage(this)" title="修改"><i class="layui-icon layui-icon-edit"></i></span>');
							$("#unitExitButton").hide();
							$("#unitCreateButton").val("新建");
							break;
						default:		//其他情况
							break;
					}
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg(succText);
					}
				} else {
					if (data.ERROR == "EC_NoPermission") {	//无权限时
						switch (obj) {
							case "provider":		//新建的为供应商
								//当无权限人员创建供应商失败后，不给予其他操作
								break;
							case "brand":		//新建的为品牌
								objBlur.parent().remove();
								$("#brandExitButton").hide();
								$("#brandCreateButton").val("新建");
								break;
							case "unit":		//新建的为商品包装单位
								objBlur.parent().remove();
								$("#unitExitButton").hide();
								$("#unitCreateButton").val("新建");
								break;
							case "providerDistrict":		//新建的为供应商区域
							case "categoryParent":		//新建的为商品大类
							case "category":		//新建的为商品小类
								objBlur.parent().remove();
								break;
							default:		//其他情况
								break;
						}
					} else {
						objBlur.focus();
					}
					if (data.msg) {		//如果后端有传msg则显示
						layer.msg(data.msg);
					} else {
						layer.msg(failText);
					}
					if (obj == "providerDistrict") {
						$("#providerDistrict p").each(function () {
							if ($(this).children(".districtId").val() == clickDistrictID) {
								$(this).click();
							}
						});
					}
				}
				layer.close(indexLoading);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		})
	}
	//综合的修改方法，操作的对象为：providerDistrict、provider、brand、categoryParent、category、unit、proeprty
	function updateObject (url, method, data, obj, succText, failText, objBlur, nameBeforeUpdate) {
		var indexLoading = layer.load(1);
		$.ajax({
			url: url,
			type: method,
			async: true,
			data: data,
			dataType: "json",
			success: function succFunction (data) {
				if (data.ERROR == "EC_NoError") {
					console.log(data);
					switch (obj) {
						case "providerDistrict":		//修改的为供应商区域
							objBlur.val(data.object.name);		//去掉首尾的空格	
							$("#selectDistrict div select option").each(function () {
								if ($(this).val() == data.object.ID) {
									$(this).text(data.object.name);
								}
							});
							form.render('select'); //刷新select选择框渲染	
							break;
						case "provider":		//修改的为供应商
							$("#providerMessage input").attr({ "readOnly": "readOnly", "placeholder": "" });
							form.val("providerDetails", {
								"ID": data.object.ID,
								"name": data.object.name,
								"contactName": data.object.contactName,
								"districtID": data.object.districtID,
								"address": data.object.address,
								"mobile": data.object.mobile
							})
							datachange = false;
							var page = $("#providerPage .layui-laypage-curr").find("em").text();
							var districtID = $(".provideDistrictSelect .districtId").val();
							providerList({ "pageIndex": page, "districtID": districtID }, providerRN_url);
							$("#providerMessageButton").hide();		//新增或修改成功后隐藏按钮
							break;
						case "brand":		//修改的为品牌
							break;
						case "categoryParent":		//修改的为商品大类
							break;
						case "category":		//修改的为商品小类
							break;
						case "unit":		//修改的为商品包装单位
							objBlur.val(data.object.name);		//去掉首尾的空格
							break;
						case "proeprty":		//修改的为商品属性
							break;
						default:		//其他情况
							break;
					}
					objBlur.attr("readOnly", "readOnly");
					objBlur.unbind("blur");
					if (data.msg) {
						layer.msg(data.msg);
					} else {
						layer.msg(succText);
					}
				} else {
					if (data.ERROR == "EC_NoPermission") {	//无权限时
						switch (obj) {
							case "provider":		//新建的为供应商
								//当无权限人员创建供应商失败后，不给予其他操作
								break;
							case "providerDistrict":		//新建的为供应商区域
							case "brand":		//新建的为品牌
							case "categoryParent":		//新建的为商品大类
							case "category":		//新建的为商品小类
							case "unit":		//新建的为商品包装单位
							case "proeprty":		//新建的为商品包装单位
								console.log(nameBeforeUpdate);
								objBlur.val(nameBeforeUpdate).attr("readonly", "readonly").unbind("blur");
								break;
							default:		//其他情况
								break;
						}
					} else {
						objBlur.focus();
					}
					//					objBlur.focus();
					console.log(data);
					if (data.msg) {		//如果后端有传msg则显示
						layer.msg(data.msg);
					} else {
						layer.msg(failText);
					}
				}
				layer.close(indexLoading);
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		})
	}
	//综合的删除方法，操作的对象为：providerDistrict、provider、brand、categoryParent、category、unit
	function deleteObject (url, method, data, hintText, succText, failText, obj, objToRemove) {
		layer.confirm(hintText, { icon: 2, title: '提示' }, function (index) {
			var indexLoading = layer.load(1);
			$.ajax({
				url: url,
				type: method,
				async: true,
				data: data,
				dataType: "json",
				success: function succFunction (data) {
					if (data.ERROR == "EC_NoError") {
						switch (obj) {
							case "providerDistrict":		//删除的为供应商区域
								$("#selectDistrict div select option").each(function () {
									if ($(this).val() == objToRemove.find(".districtId").val()) {
										$(this).remove();
									}
								});
								form.render('select'); //刷新select选择框渲染
								objToRemove.remove();
								$("#providerDistrict p:first").click();
								break;
							case "provider":		//删除的为供应商
								$(".provideDistrictSelect").click();
								break;
							case "brand":		//删除的为商品品牌
								objToRemove.remove();
								break;
							case "categoryParent":		//删除的为商品大类
								objToRemove.remove();
								$("#categoryParent p:first").click();
								break;
							case "category":		//删除的为商品小类
								objToRemove.remove();
								break;
							case "unit":		//删除的为单位
								objToRemove.remove();
								break;
							default:		//其他情况
								break;
						}
						if (data.msg) {
							layer.msg(data.msg);
						} else {
							layer.msg(succText);
						}
					} else {
						if (data.msg) {		//如果后端有传msg则显示
							layer.msg(data.msg);
						} else {
							layer.msg(failText);
						}
					}
					layer.close(index);
					layer.close(indexLoading);
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					layer.close(index);
					layer.close(indexLoading);
					layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				}
			})
		}, function (index) {
		});
	}
	//模糊搜索供应商
	form.on('submit(providerSearch)', function (data) {
		if (data.field.queryKeyword.length <= 32) {
			showFirstDataOfTable = true;
			providerList({ "pageIndex": 1, "queryKeyword": data.field.queryKeyword }, providerRNByFields_url);
		}
	});
	$("#commodityAboutMain div.providerList .title input").keyup(function () {      // 按键弹起时触发的事件；  
		$("i.providerSearch").click();
	});
	//新增、修改供应商时的取消按钮
	$("#providerMessageButton input").click(function () {
		var providerID = $("#providerMessage .ID").val();
		if (providerID) {
			if (datachange) {
				layer.confirm("确定取消改动吗？", { icon: 3, title: '提示' }, function (index) {
					datachange = false;
					getProviderMessage({ "ID": providerID });
					layer.close(index);
				});
			} else {
				$("#selectDistrict select option").attr("disabled", "disabled");		//区域下拉框更改为不可用
				$("#providerMessage input").attr("readOnly", "readOnly");
				$("#providerMessageButton").hide();
				getProviderMessage({ "ID": providerID });
			}
		} else {
			$("#provider p:first").click();
		}
	});
});