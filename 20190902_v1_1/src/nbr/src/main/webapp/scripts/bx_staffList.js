layui.use(['element', 'form', 'table', 'laydate', 'layer'], function(){
	var element = layui.element;
	var form = layui.form;
	var table = layui.table;
	var laydate = layui.laydate;
	var layer = layui.layer;
//定义常量
	const staffRN_url = "staff/retrieveNEx.bx";		//查询员工接口
	const staffRNByRole_url = "staffRole/retrieveNEx.bx";		//根据角色查询员工接口
	const staffRN_tableID = "staffList";		//staffList表格ID
	const staffCreate_url = "staff/createEx.bx";		//新建员工接口
	const staffUpdate_url = "staff/updateEx.bx";		//修改员工接口
	const staffDelete_url = "staff/deleteEx.bx";		//删除员工接口
	const staffGetToken_url = "staff/getTokenEx.bx";		//获取秘钥接口
	const staffResetPwd_url = "staff/resetOtherPasswordEx.bx";		//重置员工密码接口
	const staffCheckUniqueField = "staff/retrieveNToCheckUniqueFieldEx.bx";		//检查唯一值的接口
	const method_get = "GET";
	const method_post = "POST";
	const staff_reloadTable = {};
	const curr_reloadTable = 1;
	var bDesertLastOperation = false;		//是否放弃上一步操作(数据是否发生改变)，false表示没有修改数据或者没有点击了新建按钮，true则相反
	//初始加载层 
	layer.ready(function(){
		var indexLoading = layer.load(1);
	});
	//日期时间选择器(目前需求不需要显示密码有效期)
//	laydate.render({
//		elem: '#passwordExpireDate',
//		type: 'datetime',
//		format: 'yyyy-MM-dd HH:mm:ss'
//	});
//数据验证
	fieldFormat(form);
//员工详情区信息
	var useStaffRNByRole = false;		//判断是否使用通过角色查询员工的接口
	function staffR1(data){
		var staffID = data.ID;
		var staffName = data.name;
		var staffRole = data.roleID;
		var staffStatus = data.status;
		var staffPhone = data.phone;
		var staffICID = data.ICID;
		var staffWeChat = data.weChat;
		var staffDepartmentID = data.departmentID;
		var staffShopID = data.shopID;
		var staffCreateDatetime = data.createDatetime;
		var staffUpdateDatetime = data.updateDatetime;
//		var staffPasswordExpireDate = data.passwordExpireDate;		目前需求不需要显示密码有效期
		$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
		$(".fixedButtonArea button:not(:eq(0))").removeClass("disabledButton").removeAttr("disabled");
		if(useStaffRNByRole){		//如果通过员工角色查询员工信息渲染表格，员工的ID并不是所获取到的数据的ID
			staffID = data.staffID;
		}
		$(".resetPassword").text("修改密码");
		$(".layerResetPassword .layui-form-item").hide();
		$(".resetNewPassword, .confirmResetNewPassword").val("");
		if(staffStatus == "1"){		//离职员工不可以修改信息（除状态和角色意外），也不能修改密码
			$(".deleteStaff").addClass("disabledButton").attr("disabled", "disabled").removeClass("btnChoosed");
			$(".layerResetPassword").hide();
			$(".staffInfo input").attr("disabled", "disabled");
			$(".staffInfo select:eq(2) option, .staffInfo select:eq(3) option").attr("disabled", "disabled");		//页面的第三个和第四个下拉框
		}else{
			$(".layerResetPassword").show();
			$(".staffInfo input").removeAttr("disabled");
			$(".staffInfo select:eq(2) option, .staffInfo select:eq(3) option").removeAttr("disabled");
		}
		var sessionStaffID = $("#sessionStaffID").val();
		if(sessionStaffID == staffID){		
			$(".staffInfo select:eq(1) option").attr("disabled", "disabled");		//不可修改当前登录员工的状态
			$(".staffInfo select:eq(0) option:eq(1)").attr("disabled", "disabled");		//不可修改当前登录员工的角色
		}else{
			$(".staffInfo select:eq(1) option").removeAttr("disabled");
			$(".staffInfo select:eq(0) option:eq(1)").removeAttr("disabled");
		}
		$(".staffInfo select:eq(1) option:eq(1)").attr("disabled", "disabled");		//不可更改员工为离职状态
		$(".staffInfo .staffAppendPassword input").removeAttr("lay-verify");		//查看员工信息时不需要验证密码区域
		staffCreateDatetime = staffCreateDatetime.substring(0, staffCreateDatetime.length-3);
		staffUpdateDatetime = staffUpdateDatetime.substring(0, staffUpdateDatetime.length-3);
//		staffPasswordExpireDate = staffPasswordExpireDate.substring(0, staffPasswordExpireDate.length-3);		目前需求不需要显示密码有效期
		form.val("staffInfo",{
			"ID": staffID,
			"name": staffName,
			"roleID": staffRole,
			"status": staffStatus,
			"phone": staffPhone,
			"ICID": staffICID,
			"weChat": staffWeChat,
			"departmentID": staffDepartmentID,
			"shopID": staffShopID,
			"createDatetime": staffCreateDatetime,
			"updateDatetime": staffUpdateDatetime,
//			"passwordExpireDate": staffPasswordExpireDate,		目前需求不需要显示密码有效期
		})
		$(".staffInfo .staffAppendPassword").hide();
		$(".createTime, .updateTime").show();
	}
//渲染表格staffList
	table.render({
		elem: '#staffList',
		url: staffRN_url + "?status=0",
		method: method_post,
		id: staffRN_tableID,
		request: {
			pageName: 'pageIndex',
			limitName: 'pageSize'
		},
		response: {
			dataName: 'objectList'
		},
		limit: '10',
		size: "sm",
		skin: "nob",
		even: true,
		page: true,
		cols: [ [
			{ field: 'name', title: '姓名', width: 80, templet: '#staffName', event: 'detail', align: 'center' },
			{ field: 'roleName', title: '角色', width: 85, event: 'detail', align: 'center' }, 
			{ field: 'ICID', title: '身份证号', width: 180, event: 'detail', align: 'center' }, 
			{ field: 'phone', title: '手机号码', width: 120, event: 'detail', align: 'center' },
			{ field: 'weChat', title: '微信', width: 110, event: 'detail', align: 'center' },
			{ field: 'status', title: '状态', width: 70, event: 'detail', align: 'center', 
				templet:function(data){
					console.log(data);
					if(data.status == "1"){
						return "离职";
					}else if(data.status == "0"){
						return "在职";
					}
				}
			},
		] ],
		done: function(res, curr, count){		//每次加载完表格都会触发的回调函数
			console.log(res);
			if(res.ERROR != "EC_NoError"){
				var msg = res.msg == "" ? "查询员工信息失败" : res.msg;
				layer.msg(msg);
			}else{
				var data = res.objectList;		//1.先判断用户是否是第一次打开当前页面//2.默认获取第一条数据(是的情况下)//3.解析数据，显示在详情区
				if(data.length > 0){
					if(bDesertLastOperation){
						layer.confirm('确定要放弃之前的操作吗？', {icon: 3, title: '提示'}, function(index){
							bDesertLastOperation = false;
							staffR1(data[0]);
							$("#staffList + div tbody tr:first").addClass("trChoosed");		//为第一条数据添加样式
							layer.close(index);
						})
					}else{
						if($(".resetPassword").text() == "修改密码"){
							staffR1(data[0]);
							$("#staffList + div tbody tr:first").addClass("trChoosed");		//为第一条数据添加样式
						}else{
							layer.confirm('确定要放弃修改密码吗？', {icon: 3, title: '提示'}, function(index){
								giveUpChangePassword(index);
								staffR1(data[0]);
								$("#staffList + div tbody tr:first").addClass("trChoosed");		//为第一条数据添加样式
							})
						}
					}
				}else{
					cannotSearchStaffInfo();
					layer.msg("查无该员工");
				}
			}
			layer.closeAll('loading');
		}
	});
//监听表格查询店员详情信息
	table.on('tool(staffList)', function(obj){
		var data = obj.data;
		var tr = obj.tr;
		switch(obj.event){
			case 'detail':
				if(bDesertLastOperation){
					layer.confirm('确定要放弃之前的操作吗？', {icon: 3, title: '提示'}, function(index){
						$(tr).addClass("trChoosed").siblings().removeClass("trChoosed");
						bFieldIsUnique = true;
						bDesertLastOperation = false;
						staffR1(data);	
						layer.close(index);
					}, function(index){
						layer.close(index);
					})
				}else{
					if($(".resetPassword").text() == "修改密码"){
						$(tr).addClass("trChoosed").siblings().removeClass("trChoosed");
						bFieldIsUnique = true;
						staffR1(data);
					}else{
						layer.confirm('确定要放弃修改密码吗？', {icon: 3, title: '提示'}, function(index){
							giveUpChangePassword(index);
							$(tr).addClass("trChoosed").siblings().removeClass("trChoosed");
							bFieldIsUnique = true;
							staffR1(data);
						})
					}
				}
			break;
		};
	});
//左边全部展开事件
	$(".showAll").click(function(){
		if($(this).text() === "全部展开"){
			$(".middleArea_left .layui-nav-tree li").addClass("layui-nav-itemed");
			$(this).text("全部关闭");
		}else{
			$(".middleArea_left .layui-nav-tree li").removeClass("layui-nav-itemed");
			$(this).text("全部展开");
		}
	})
// 根据员工角色查询
	function roleQuery(indexText, roleID){
		var indexLoading = layer.load(1);
		if(indexText == "离职员工"){
			staff_reloadTable.queryKeyword = "";		//在根据员工角色（离职）查询时，需要把queryKeyword删除掉
			useStaffRNByRole = false;
			staff_reloadTable.status = 1;
			reloadTable(table, staffRN_tableID, method_post, staffRN_url, curr_reloadTable, staff_reloadTable);
		}else if(indexText == "全部在职员工"){
			useStaffRNByRole = false;
			reloadTable(table, staffRN_tableID, method_post, staffRN_url, curr_reloadTable, {status: 0});	//查全部只需要传状态0
		}else{
			useStaffRNByRole = true;
			staff_reloadTable.status = 0;
			staff_reloadTable.roleID = roleID;
			reloadTable(table, staffRN_tableID, method_get, staffRNByRole_url, curr_reloadTable, staff_reloadTable);
		}
		layer.close(indexLoading);
	}
//根据店员角色查询店员信息
	$(".middleArea_left .layui-nav-tree li dl dd").click(function(){
		var indexLoading = layer.load(1);
		var indexText = $(this).find("a").text();
		var roleID = $(this).find("input").val();
		$("input[name='queryKeyword']").val("");
		if(bDesertLastOperation){
			layer.confirm('确定要放弃之前的操作吗？', {icon: 3, title: '提示'}, function(index){
				layer.close(index);
				bDesertLastOperation = false;
				roleQuery(indexText, roleID);		//点击确定后执行的根据员工角色查询的函数
			})
		}else{
			if($(".resetPassword").text() == "修改密码"){
				roleQuery(indexText, roleID);
			}else{
				layer.confirm('确定要放弃修改密码吗？', {icon: 3, title: '提示'}, function(index){
					roleQuery(indexText, roleID);
					giveUpChangePassword(index);
				})
			}
		}
		layer.close(indexLoading);
	})
//根据关键字查询店员信息(模糊搜索只搜在职的)
	form.on('submit(staffSearch)', function(data){
		if(bDesertLastOperation){
			layer.confirm('确定要放弃之前的操作吗？', {icon: 3, title: '提示'}, function(index){
				bDesertLastOperation = false;
				$(".layerResetPassword .resetPassword").text("修改密码");
				searchStaff(data);		//点击确定执行搜索函数
				layer.close(index);
			});
		}else{
			if($(".resetPassword").text() == "修改密码"){
				searchStaff(data);
			}else{
				layer.confirm('确定要放弃修改密码吗？', {icon: 3, title: '提示'}, function(index){
					giveUpChangePassword(index);
					searchStaff(data);
				})
			}
		}
	})
//模糊搜索功能
	function searchStaff(data){
		var requestedData = {"queryKeyword": data.field.queryKeyword, "status": 0};
		useStaffRNByRole = false;
		reloadTable(table, staffRN_tableID, method_post, staffRN_url, curr_reloadTable, requestedData);
		$(".middleArea_left .layui-nav-tree li dd").removeClass("layui-this");
		return false;
	}
//即时搜索员工
	window.instantSearch = function(index){
		$(".staffSearch").click();
	}
//检查唯一值(//1.获取输入的相关的唯一值 //2.判断获取的是哪个字段，从而设置fieldToCheckUnique的值//3.传参给后端，根据接收的错误码判断进行什么操作//4.在创建的函数里添加条件限制)
	var bFieldIsUnique = true;		//唯一字段的检查结果
	var checkUniqueField_index;		//检查唯一字段时的操作对象
	window.checkUniqueField = function(index){		//检查手机号码、身份证号、微信是否存在相同的
		var uniqueField = $(index).val();
		var requestData = new Object();
		if($(index).hasClass("staffPhone")){
			if(uniqueField.length >= 11){
				if(/^(1[0-9])\d{9}$/.test(uniqueField)){
					requestData.fieldToCheckUnique = 1;
				}else{
					layer.msg("员工手机号码数据格式不对，请输入1开头的11位数字");
					return;
				}
			}else{
				return;
			}
		}else if($(index).hasClass("staffICID")){
			if(uniqueField.length >= 18){
				if(/^$|^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(uniqueField)){
					requestData.fieldToCheckUnique = 2;
				}else{
					layer.msg("员工身份证号数据格式不对，请输入正确的身份证号");
					return;
				}
			}else{
				return;
			}
		}else if($(index).hasClass("staffWeChat")){
			if(uniqueField.length >= 5){
				if(/^$|^[A-Za-z0-9]{5,}$/.test(uniqueField)){
					requestData.fieldToCheckUnique = 3;
				}else{
					layer.msg("员工微信号数据格式不对，请输入5-20位的英文或数字");
					return;
				}
			}else{
				return;
			}
		}else{
			return;		//其他对象不做检查
		}
		requestData.ID = $(".staffID").val() == "" ? 0 : $(".staffID").val();
		requestData.uniqueField = uniqueField;
		$.ajax({
			url: staffCheckUniqueField,
			type: method_post,
			async: true,
			dataType: "json",
			data: requestData,
			success: function succFunction(data){
				if(data.ERROR != "EC_NoError"){
					layer.msg(data.msg);
					bFieldIsUnique = false;
					checkUniqueField_index = $(index);
					$(index).focus();
				}else{
					bFieldIsUnique = true;
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		})
	}
//判断是否修改数据(文本框部分)
	window.check_ifDataChange = function(obj, value){
		$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
		bDesertLastOperation = true;
	}
//判断用户是否修改员工的数据(下拉框)
	form.on('select(spinnerCheck)', function(data){
		bDesertLastOperation = true;
	});
//顶部按钮监听事件
	$(".staffManage").click(function(){
		var keyword = $(this).text();
		$(this).addClass("btnChoosed").siblings().removeClass("btnChoosed");
		switch(keyword){
			case "新建":
				if(bDesertLastOperation){
					layer.confirm('确定要放弃之前的操作吗？', {icon: 3, title: '提示'}, function(index){
						bDesertLastOperation = false;
						staffToCreate();
						layer.close(index);
					})
				}else{
					if($(".resetPassword").text() == "修改密码"){
						staffToCreate();
					}else{
						layer.confirm('确定要放弃修改密码吗？', {icon: 3, title: '提示'}, function(index){
							giveUpChangePassword(index);
							staffToCreate();
						})
					}
				}
				break;
			case"删除":
				var staffID =  $(".staffID").val();
				if(staffID){
					var sessionStaffID = $("#sessionStaffID").val();
					if(sessionStaffID == staffID){
						layer.msg("不允许删除当前登录人员");
						return;
					}
					if($(".resetPassword").text() != "修改密码"){
						layer.confirm('确定要放弃修改密码吗？', {icon: 3, title: '提示'}, function(index){
							giveUpChangePassword(index);
							$(".deleteStaff").click();
						})
						return;
					}
					var staffName = $(".staffName").val();
					layer.confirm('确定要删除 "' + staffName +'"员工吗？', {icon: 2, title: '提示'}, function(index){
						var loading = layer.load(1);
						staffManage(staffDelete_url, method_get, {"ID": staffID}, "删除员工失败", "删除员工成功", loading);
					});
				}else{
					layer.msg("请先选择需要删除的员工", {icon: 2});
				}
				break;
			case"取消":
				if(bDesertLastOperation){
					layer.confirm('确定要放弃之前的操作吗？', {icon: 3, title: '提示',cancel: function(){
						$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
					}},function(index){
						bDesertLastOperation = false;
						layer.close(index);
						$(".layerResetPassword .resetPassword").text("修改密码");
						$(".layui-nav-item dl dd:eq(0) a").click();		//点击全部在职员工，返回所有数据
					},function(){
						$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
					})	
				}else{
					if($(".resetPassword").text() == "修改密码"){
						$(".layui-nav-item dl dd:eq(0) a").click();
					}else{
						layer.confirm('确定要放弃修改密码吗？', {icon: 3, title: '提示'}, function(index){
							giveUpChangePassword(index);
							$(".layui-nav-item dl dd:eq(0) a").click();
							checkStaffBeChoosed();
						})
					}
					
				}
			break;
		}
	})
	function restoreDefault(){	 //自动点击全部在职员工、清空输入框
		$(".layui-nav-item dl dd:eq(0) a").click();
		$("input[name='queryKeyword']").val("");
	}
//保存按钮的监听
	form.on('submit(staffUpdate)', function(data){
		var staffInfo = data.field;		//获取到的表单信息
		$(this).addClass("btnChoosed").siblings().removeClass("btnChoosed");		//为按钮添加选中状态
//		staffInfo.passwordExpireDate = staffInfo.passwordExpireDate.replace(new RegExp("-","g"), "/");		//根据需求修改日期传参格式,根据需求不需要显示有效期字段
		delete staffInfo["createDatetime"];
		delete staffInfo["updateDatetime"];
	//判断是新建员工还是修改员工信息
		if(staffInfo.ID){		//修改员工
			if(bDesertLastOperation){		//数据发生修改
				if($(".resetPassword").text() == "修改密码"){
					layer.confirm('确认修改员工"' + staffInfo.name + '"的信息？', {icon: 3, title: '提示'}, function(index){
						var loading = layer.load(1);
						var failText = "修改员工信息失败";
						var succText = "修改员工信息成功";
						staffManage(staffUpdate_url, method_post, staffInfo, failText, succText, loading);
					})
				}else{
					layer.confirm('确定要放弃修改密码吗？', {icon: 3, title: '提示'}, function(index){
						giveUpChangePassword(index);
						layer.confirm('确认修改员工"' + staffInfo.name + '"的信息？', {icon: 3, title: '提示'}, function(index){
							var loading = layer.load(1);
							var failText = "修改员工信息失败";
							var succText = "修改员工信息成功";
							staffManage(staffUpdate_url, method_post, staffInfo, failText, succText, loading);
						})
					})
				}
			}else{		//数据未修改
				if($(".resetPassword").text() == "修改密码"){
					layer.msg("当前员工信息尚未修改，请修改再保存！");
				}else{
					layer.confirm('确定要放弃修改密码吗？', {icon: 3, title: '提示'}, function(index){
						giveUpChangePassword(index);
						layer.msg("当前员工信息尚未修改，请修改再保存！");
					})
				}
			}
		}else{		//创建员工
			delete staffInfo["ID"];
			if(staffInfo.newPassword != staffInfo.confirmNewPassword){
				layer.msg("两次输入的密码不一致！");
				return;
			}
			layer.confirm('确认创建员工"' + staffInfo.name + '"?', {icon: 3, title: '提示'}, function(index){
				var loading = layer.load(1);
				$.ajax({
					url: staffGetToken_url,
					type: method_post,
					async: true,
					data: {"phone": staffInfo.phone, "createNewStaff": 1},		//createNewStaff为1防止删除会话
					dataType: "json",
					success: function succFunction(data){
						if(data.ERROR == "EC_NoError"){
							var modulus = data.rsa.modulus;
							var exponent = data.rsa.exponent;
							var rsa = new RSAKey();
							rsa.setPublic(modulus, exponent);
							var pwdEncrypted = rsa.encrypt(staffInfo.newPassword);
							if(pwdEncrypted){		//密码加密成功后
								var failText = "新建员工失败";
								var succText = "新建员工成功";
								staffInfo.pwdEncrypted = pwdEncrypted;
								staffManage(staffCreate_url, method_post, staffInfo, failText, succText, loading);
							}else{
								layer.msg("密码加密失败");
								layer.close(loading);
							}
						}else{
							var msg = data.msg == "" ? "新建员工失败，无法获取秘钥" : data.msg;
							layer.msg(msg);
							layer.close(loading);
						}
					},
					error: function(XMLHttpRequest, textStatus, errorThrown){
						layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
						layer.close(loading);
					}
				})
			}, function(index){
				layer.close(index);
			})
		}
	})
//新建或修改或删除员工
	function staffManage(url, method, staffInfo, failText, succText, loading){
		$.ajax({
			url: url,
			type: method,
			async: true,
			data: staffInfo,
			dataType: "json",
			success: function succFunction(data){
				if(data.ERROR == "EC_NoError"){
//					根据需求目前不需在页面显示密码有效期字段
//					staffInfo.passwordExpireDate = staffInfo.passwordExpireDate.replace(new RegExp("/","g"), "-");
//					form.val("staffInfo",{		//用户不填密码有效期时显示出默认的密码有效期
//						"passwordExpireDate": staffInfo.passwordExpireDate,
//					})
					var msg = data.msg == "" ? succText : data.msg;
					layer.msg(msg);
					bDesertLastOperation = false;
					useStaffRNByRole = false;
					$(".middleArea_left .layui-nav-tree li dl dd:first a").click();
				}else{
					var msg = data.msg == "" ? failText : data.msg;
					layer.msg(msg);
				}
				layer.close(loading);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(loading);
			}
		})
	}
//修改密码(点击让input显示或隐藏)
	$(".resetPassword").click(function(){
		if($(this).text() == "修改密码"){
			var sessionStaffID = $("#sessionStaffID").val();
			if($(".staffID").val() == $("#sessionStaffID").val()){		//修改自己的密码
				layer.confirm('确定要修改自己的密码吗？', {icon: 3, title: '提示'}, function(index, layero){
					layer.close(index);
					pageJumping('home/updateMyPwd.bx?lastLocation=2', 'home/updateMyPwd.bx', '修改密码');
				}, 
				function(index, layero){
					layer.close(index);
				})	
			}else{		//修改其他人的密码
				$(".layerResetPassword .layui-form-item").show();
				$(this).text("收起修改密码区域");
			}
		}else{
			$(".layerResetPassword .layui-form-item").hide();
			$(this).text("修改密码");
		}
	})
//修改密码的监听......
	form.on('submit(resetPassword)',function(data){
		var loading = layer.load(1);
		var resetNewPassword = data.field.resetNewPassword;
		var confirmResetNewPassword = data.field.confirmResetNewPassword;
		if(resetNewPassword == confirmResetNewPassword){		//两次输入的密码一致
			var phone = $(".staffPhone").val();		//如果用户改动了号码未保存时去修改密码，将会出错......
			$.ajax({
				url: staffGetToken_url,
				type: method_post,
				data: {"phone": phone, "forModifyPassword": 1},
				cache: false,
				async: true,
				dataType: "json",
				success: function(data){
					if(data.ERROR == "EC_NoError"){
						var modulus = data.rsa.modulus;
						var exponent = data.rsa.exponent;
						var rsa = new RSAKey();
						rsa.setPublic(modulus, exponent);
						var sPasswordEncryptedNew = rsa.encrypt(resetNewPassword);		//加密新密码
						if(sPasswordEncryptedNew){		//加密成功
							$.ajax({
								url: staffResetPwd_url,
								type: method_post,
								async: true,
								data: {"phone": phone, "sPasswordEncryptedNew": sPasswordEncryptedNew, "involvedResigned": 1},		//重置他人密码
								dataType: "json",
								success: function(data){
									console.log(data);
									if(data.ERROR == "EC_NoError"){
										var msg = data.msg == "" ? "重置密码成功" : data.msg;
										layer.msg(msg);
										$(".layerResetPassword input").val("");
										$(".layerResetPassword .resetPassword").click();
									}else{
										var msg = data.msg == "" ? "重置密码失败" : data.msg;
										layer.msg(msg);
									}
									layer.close(loading);
								},
								error: function(XMLHttpRequest, textStatus, errorThrown){
									layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
									layer.close(loading);
								}
							});
						}else{
							layer.msg("密码加密失败");
							layer.close(loading);
						}
					}else{
						var msg = data.msg == "" ? "重置密码失败，无法获取秘钥" : data.msg;
						layer.msg(msg);
						layer.close(loading);
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
					layer.close(loading);
				}
			})
		}else{		//两次输入的密码不一致
			layer.msg("两次输入的密码不一致！");
			layer.close(loading);
		}
	})
//清空员工信息的详情（查询到无数据时）
	function cannotSearchStaffInfo(){
		form.val("staffInfo",{
			"ID": "",
			"name": "",
			"roleID": "",
			"status": 0,
			"phone": "",
			"ICID": "",
			"weChat": "",
			"departmentID": 1,
			"shopID": 1,
			"newPassword": "",
			"confirmNewPassword": "",
//			"passwordExpireDate": "9999/12/30 23:59:59",		目前需求不需要显示密码有效期
			"createDatetime": "",
			"updateDatetime": ""
		})
		$(".fixedButtonArea button:eq(0)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
		$(".fixedButtonArea button:not(:eq(0))").addClass("disabledButton").attr("disabled", "disabled").removeClass("btnChoosed");
		$("#staffDetail .layerResetPassword").hide();
		$(".resetPassword").text("修改密码");
		$(".layerResetPassword .layui-form-item").hide();
		$(".resetNewPassword, .confirmResetNewPassword").val("");
		$("#staffDetail .staffInfo input, #staffDetail .staffInfo option").attr("disabled", "disabled");
		form.render("select");
	}
//准备创建员工
	function staffToCreate(){
		form.val("staffInfo",{
			"ID": "",
			"name": "",
			"roleID": 1,
			"status": 0,
			"phone": "",
			"ICID": "",
			"weChat": "",
			"departmentID": 1,
			"shopID": 1,
			"newPassword": "",
			"confirmNewPassword": "",
//			"passwordExpireDate": "9999/12/30 23:59:59"		目前需求不需要显示密码有效期
		})
		$(".staffInfo .staffAppendPassword").show();
		$(".createTime, .updateTime, .layerResetPassword").hide();
		$(".resetPassword").text("修改密码");
		$(".layerResetPassword .layui-form-item").hide();
		$(".resetNewPassword, .confirmResetNewPassword").val("");
		$(".staffInfo input, .staffInfo select option").removeAttr("disabled");
		$(".staffInfo select:eq(1) option:eq(1)").attr("disabled", "disabled");		//不能创建离职的员工
		$(".staffInfo .staffAppendPassword input").attr("lay-verify", "required|password");		//补充表单验证
		$(".fixedButtonArea button:not(:eq(0))").removeClass("disabledButton").removeAttr("disabled");
		$(".deleteStaff").addClass("disabledButton").attr("disabled", "disabled").removeClass("btnChoosed");		//新建时删除按钮不可用
		$(".middleArea_right #staffList + div .layui-table-body tbody tr").removeClass("trChoosed");
		$(".fixedButtonArea button:eq(1)").addClass("btnChoosed").siblings().removeClass("btnChoosed");
		bFieldIsUnique = true;
		bDesertLastOperation = true;
		form.render('select'); 
	}
//放弃修改密码
	function giveUpChangePassword(index){
		$(".layerResetPassword .resetPassword").click();
	    $(".resetNewPassword, .confirmResetNewPassword").val("");
	    layer.close(index);
	}
//恢复表格数据选中状态
	function checkStaffBeChoosed(){
		var staffID = $(".staffID").val();
		var staffObjectList = table.cache.staffList;
		if(useStaffRNByRole){		//如果通过角色查询员工，员工的ID并不是所获取到的数据的ID
			for(var i=0; i<staffObjectList.length; i++){
				if(staffObjectList[i].staffID == staffID){
					$(".middleArea_right #staffList + div .layui-table-body tbody tr").eq(i).addClass("trChoosed");
				}
			}
		}else{
			for(var i=0; i<staffObjectList.length; i++){
				if(staffObjectList[i].ID == staffID){
					$(".middleArea_right #staffList + div .layui-table-body tbody tr").eq(i).addClass("trChoosed");
				}
			}
		}
	}
});
