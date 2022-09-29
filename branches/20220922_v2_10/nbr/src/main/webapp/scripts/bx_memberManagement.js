layui.use(['element', 'form', 'table', 'laydate', 'layer', 'upload'], function(){
	var element = layui.element;		//常用元素模块
	var form = layui.form;		//表单模块
	var table = layui.table;		//表格模块
	var laydate = layui.laydate;		//日期模块
	var layer = layui.layer;		//弹出层模块
	var upload = layui.upload;		//上传文件模块

	//定义常量
	const vipRN_url = "vip/retrieveNEx.bx";		//查询会员接口
	const createVip_url = "vip/createEx.bx";		//新建会员接口
	const retrieveNByMobileOrVipCardSNEx_url = "vip/retrieveNByMobileOrVipCardSNEx.bx";		//会员查询功能
	const updateBonus_url = "vip/updateBonusEx.bx";		//修改会员积分接口
	const updateVip_url = "vip/updateEx.bx";		//修改会员接口
	const uploadVipLogo_url = "";		//上传会员头像接口
	const vipTable_tableID = "vipTable";		//页面会员卡表格ID
	const method_get = "GET";		//get请求方式	
	const method_post = "POST";		//post请求方式
	var pageLoading = layer.load(1);
	var bDesertLastOperation = true;		//用于提示是否放弃之前的操作，true表示放弃，false表示不放弃。比如，用户点击了新建，然后点击搜索，这时会弹出提示
	var vipID = 0; //会员的ID
	
	//进入会员卡页面
	$(document).ready(function(){
		vipTable();
		layer.close(pageLoading);
	});
	
	//页面一打开就执行弹出层
	layer.ready(function(){
		return;
//		var loading = layer.load(1);
		$.ajax({
			url: vipCardR1_url,
			type: "",
			async: true,
			data: data,
			dataType: "json",
			success: function succFunction(data){
				console.log(data);
//				layer.close(loading);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
//				layer.close(loading);
			}
		})
	});	

	//检查数字类型
	window.checkNum = function(index){
		var num = parseInt($(index).val());
		if(isNaN(num)){
			$(index).siblings(".form_msg").show();
			$(index).val("");
		}else{
			if(num > 0 && num < 2147483648){
				var keyWord = $(index).attr("name");
				$(index).siblings(".form_msg").hide();
				$(".instructions ." + keyWord).text(num);
			}else if(num == 0){
				var keyWord = $(index).attr("name");
				if(keyWord == "initIncreaseBonus"){
					$(index).siblings(".form_msg").hide();
					$(".instructions ." + keyWord).text(num);
				}else{
					$(index).siblings(".form_msg").show();
				}
			}else{
				$(index).siblings(".form_msg").show();
			}
			$(index).val(num);
		}
	}

	//修改会员卡数据验证
	form.verify({
		title: function(value, item){
			if(value){
				if(value.length < 2||value.length > 9){					
					isToSend = true;
					return "请输入正确的会员卡名称";
				}
			}else{
				isToSend = true;
				return "必填信息错误";
			}
		},
		initIncreaseBonus: function(value, item){
			if(!value){
				isToSend = true;
				return "初始积分大于等于0";
			}
		},
		amountUnit: function(value, item){
			if(value<1){
				isToSend = true;
				return "消费金额需大于0";
				
			}
		},
		increaseBonus: function(value, item){
			if(value<1){
				isToSend = true;
				return "赠送积分需大于0";
			}
		},
		maxIncreaseBonus: function(value, item){
			if(value<1){
				isToSend = true;
				return "单次可获取的积分上限需大于0";
			}
		},
		clearBonusDay: function(value, item){
			if(value<1){
				isToSend = true;
				return "积分清零天数大于0";
			}
		},
		vipName: function(value, item){
			if(value.length < 1){
				isToSend = true;
				return "请输入至少一位长度的新会员名称";
			}
		},
		mobile: function(value, item){
			var RE=/^[1][3,4,5,7,8][0-9]{9}$/;
			if(!RE.test(value)){
				isToSend = true;
				return "请输入正确的11位手机号码";
			}
		},
		email: function(value, item){
			var emailPatt = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/i;
			if(value != ''){
				if(!emailPatt.test(value)){
					isToSend = true;
					return "请输入正确的邮箱格式";
				}
			}
		},
		district: function(value, item){
			if(value != ''){
				if(value.length < 2){
					isToSend = true;
					return "请输入正确的区域名";
				}
			}
		},
		iCID: function(value, item){
			if(value != ''){
				if(value.length !== 18){
					isToSend = true;
					return "请输入正确的18位身份证号码";
				}
			}
		}
		
	})

	//会员表格渲染
	function vipTable(){
		bDesertLastOperation = true;
		var tableIns = table.render({
			elem: '#vipTable',
			url: vipRN_url,
			id: vipTable_tableID,
			method: method_post,
			request: {
				pageName: 'pageIndex',
				limitName: 'pageSize'
			},
			where: {
				sValue: ""
			},
			response: {
				dataName: 'objectList'
			},
			limit: '10',
			limits: [10],
			page: true,
			skin: 'nob',
			even: true,
			page: true,
			cols: [ [
				{ field: 'name', title: '会员名称', align: 'center', width: "150" }, 
				{ field: 'mobile', title: '手机号码', align: 'center', width: "130" },
				{ field: 'consumeTimes', title: '消费次数', align: 'center', width: "130" },
				{ field: 'consumeAmount', title: '消费金额', align: 'center', width: "130" },
				{ field: 'bonus', title: '当前积分', align: 'center', width: "130" },
				{ field: 'buttonArea', title: '操作', align: 'center', width: "200", toolbar: '#buttonArea' }
			] ],
			text: {
				none: '没有符合条件的会员，请确认后重新搜索'
			},
			done: function(res, curr, count){
				console.log(res);
				$("#vipTable + div tbody tr:first td:last").find("a").click();
			}
		});
	}
	//查看一个会员详细
	function  vipR1(data, tr){
		console.log(data);
		vipID = data.ID
		bDesertLastOperation = true;
		$(".vipInfoArea .main").hide();
		$(".vipInfoForm input").attr("readonly","true");
		$(".vipInfoForm #district").removeAttr("readonly");
		$(".vipInfoForm .thisBonus").attr("readonly","true");
//		$(".vipInfoForm .name").removeAttr("readonly");
		$('.vipInfoForm .radio input').attr("disabled","disabled");
//		$(".vipInfoForm .birthday").removeAttr("readonly");
//		$(".vipInfoForm .iCID").removeAttr("readonly");
		form.val("vipInfoForm", {
			"ID":data.ID,
			"logo": data.logo,
			"sn": data.sn,
			"name": data.name,
			"sex": data.sex,
			"iCID": data.iCID,
			"mobile": data.mobile,
			"birthday": !data.birthday?"":new Date(data.birthday.replaceAll('-', '/').slice(0,-3)).format("yyyy/MM/dd"),
			"email": data.email,
			"district": data.district,
			"consumeTimes": data.consumeTimes,
			"consumeAmount": data.consumeAmount,
			"bonus": data.bonus,
			"lastConsumeDatetime": data.lastConsumeDatetime,
			"createDatetime": data.createDatetime,
			"updateDatetime": data.updateDatetime,
			"remark": data.remark,
		})
		$(".updateVip").show();
		$(".createVip").hide();
		$("input[name='sn']").parents(".layui-form-item").show();
		$("input[name='localPosSN']").parents(".layui-form-item").show();
		$("input[name='consumeTimes']").parents(".layui-form-item").show();
		$("input[name='consumeAmount']").parents(".layui-form-item").show();
		$("input[name='bonus']").parents(".layui-form-item").show();
		$("input[name='lastConsumeDatetime']").parents(".layui-form-item").show();
		$("input[name='createDatetime']").parents(".layui-form-item").show();
		$("input[name='updateDatetime']").parents(".layui-form-item").show();
	}
	
//	监听会员表格操作
	table.on('tool(vipTable)', function(obj){
		var data = obj.data;		//当前行的数据
		var layEvent = obj.event;	//当前行的事件关键字
		var tr = obj.tr;		//当前行的dom对象
		console.log(data);
		switch (layEvent){
		case "detail":
//			console.log($(this).index())
//			$(".vipTableArea tbody tr").eq($(this).index()).css("background-color","#0080c0").siblings().css("background-color","white");
			if(bDesertLastOperation){
				vipR1(data, tr);
			}else{
				layer.confirm('确定要放弃之前的操作吗？', {icon: 3, title: '提示'}, function(index, layero){		//点击确定
					layer.close(index);
					vipR1(data, tr);
				},function(index, layero){		//点击取消
					layer.close(index);
				})
			}
			
			break;
		default:
			console.log("未知的定义事件");
			return;
	  }
	})

	//根据手机号码或者会员卡号查询会员
	form.on("submit(queryVipByKeyword)", function(data){
		var mobile = $(".queryByKeywordArea input").val();
		var res = mobile.charAt(0);
		console.log(res);
		if(res==1 && mobile.length > 0){
				reloadTable(table, vipTable_tableID, method_post, retrieveNByMobileOrVipCardSNEx_url, 1, {"mobile":mobile});
		}else if(mobile.length > 12){
			reloadTable(table, vipTable_tableID, method_post, retrieveNByMobileOrVipCardSNEx_url, 1, {"vipCardSN":mobile});
		}else{
			 vipTable();
		}	
	})
	
	//即时搜索VIP
	window.instantSearch = function(index){
		$(".vipSearch").click();
	}

	
	//新建会员按钮
	function toCreateVip(){
		bDesertLastOperation = false;
		form.val("vipInfoForm", {
			"logo": "",
			"sn": "",
			"localPosSN": "",
			"name": "",
			"sex": "0",
			"iCID": "",
			"mobile": "",
			"birthday": "",
			"email": "",
			"district": "",
			"status": "0",
			"consumeTimes": "",
			"consumeAmount": "",
			"bonus": "",
			"lastConsumeDatetime": "",
			"createDatetime": "",
			"updateDatetime": "",
			"remark": "",
		})
		//新建会员时需隐藏的区域
		$(".vipInfoArea .main").show();
		$('.vipInfoForm .radio input').removeAttr('disabled');
		form.render('radio', 'vipInfoForm');
		$(".vipInfoForm input").removeAttr("readonly");
		$(".updateVip").hide();
		$(".createVip").show();
		$(".vipID").parents(".layui-form-item").hide();
		$("input[name='sn']").parents(".layui-form-item").hide();
		$("input[name='localPosSN']").parents(".layui-form-item").hide();
		$("input[name='consumeTimes']").parents(".layui-form-item").hide();
		$("input[name='consumeAmount']").parents(".layui-form-item").hide();
		$("input[name='bonus']").parents(".layui-form-item").hide();
		$("input[name='lastConsumeDatetime']").parents(".layui-form-item").hide();
		$("input[name='createDatetime']").parents(".layui-form-item").hide();
		$("input[name='updateDatetime']").parents(".layui-form-item").hide();
	}
	
	//新建会员按钮监听
	$(".toCreateVip").click(function(){
		if(bDesertLastOperation){
			toCreateVip();
		}else{
			layer.confirm('确定要放弃之前的操作吗？', {icon: 3, title: '提示'}, function(index, layero){		//点击确定
				layer.close(index);
				toCreateVip();
			},function(index, layero){		//点击取消
				layer.close(index);
			})
		}
	})
	//选择生日
//	 不允许修改生日
//	laydate.render({
//		elem: ".birthday",
//		type: "date",
//		format: "yyyy/MM/dd"
//	})
	
	//修改会员积分
	$(".revisePointBonus").click(function(){
		layer.prompt({title: '请输入修改积分值'},function(value, index, elem){
			var reg=new RegExp("^[0-9]+$");
			var checkBonus = reg.test(value);
//			console.log(val);
			if(!checkBonus || (value < 0 || value > 10000) || isNaN(value)){
					layer.msg("积分值只能是0到10000之间的整数");
					return 
			}
			$.ajax({
				url: updateBonus_url,
				type: method_post,
				async: true,
				data: {
					"bonus":value,
					"manuallyAdded":1,
					"remarkForBonusHistory":"修改原积分",
					"ID":vipID,
					"category": 1
				},
				dataType: "json",
				success: function succFunction(data){
					console.log(data)
					if(data.ERROR == "EC_NoError"){
						vipTable();
						layer.msg("修改积分成功");
					}else{
						layer.msg(data.msg);
						layer.close(pageLoading);
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				}
			})
			layer.close(index);
		})
	});
	
	//修改会员
	form.on("submit(updateVip)", function(data){
		console.log(data.field);
		var loading = layer.load(1);
		var vipData = data.field;
		var newVipDate = {
				"ID":vipData.ID,
				"category": 1,
				"remark":vipData.remark
			};
		if(vipData.birthday){
			newVipDate.birthday = vipData.birthday;
		}
		$.ajax({
			url: updateVip_url,
			type: method_post,
			async: true,
			data: newVipDate,
			dataType: "json",
			success: function succFunction(data){
				console.log(data);
				if(data.ERROR != "EC_NoError"){
					layer.msg("修改信息有误");
				}else{
					layer.close(loading);
					layer.msg("修改信息成功");
					vipTable();
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);

			}
		})
	})
	
	//创建会员
	form.on("submit(createVip)", function(data){
		bDesertLastOperation = true;
		console.log(data.field);
		var newVipData = data.field;
		var vipdata = {
				"name": newVipData.name,
				"sex": newVipData.sex,
				"iCID": newVipData.iCID,
				"mobile": newVipData.mobile,
				"email": newVipData.email,
				"district": newVipData.district,
				"remark": newVipData.remark,
				"category": 5,
				"cardID": 1,
				"remark":newVipData.remark
				
			};
		if(newVipData.birthday){		//生日为空时不需传该字段，否则会有400错误
			vipdata.birthday = newVipData.birthday;
		}
		layer.open({
			content: "创建名为"+ newVipData.name +"的会员",
			yes: function(index, layero){
				$.ajax({
					url: createVip_url,
					type: method_post,
					async: true,
					data: vipdata,
					dataType: "json",
					success: function succFunction(data){
						console.log(data);
						if(data.ERROR != "EC_NoError"){
							layer.msg(data.msg);
						}else{
							vipTable();
							layer.msg("创建成功");
							layer.close(pageLoading);
						}
					},
					error: function(XMLHttpRequest, textStatus, errorThrown){
						layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);

					}
				})
				layer.close(index); //如果设定了yes回调，需进行手工关闭
			}
		});
	});
})
