layui.use(['element', 'form', 'table', 'laydate', 'layer', 'upload'], function(){
	var element = layui.element;		//常用元素模块
	var form = layui.form;		//表单模块
	var table = layui.table;		//表格模块
	var laydate = layui.laydate;		//日期模块
	var layer = layui.layer;		//弹出层模块
	var upload = layui.upload;		//上传文件模块

	//定义常量
	const vipCardR1_url = "vipCard/retrieve1Ex.bx";		//查询会员卡接口
	const vipRN_url = "vip/retrieveNEx.bx";		//查询会员接口
	const vipCardUpdate_url = "vipCard/updateEx.bx";		//修改会员卡属性接口
	const vipBonusUpdate_url = "bonusRule/updateEx.bx";		//修改会员卡积分规则		
	const method_get = "GET";		//get请求方式	
	const method_post = "POST";		//post请求方式
	var pageLoading = layer.load(1);
	var bDesertLastOperation = true;		//用于提示是否放弃之前的操作，true表示放弃，false表示不放弃。比如，用户点击了新建，然后点击搜索，这时会弹出提示
	var vipID = 0; //会员的ID
	
	//进入会员卡页面
	$(document).ready(function(){
		$.ajax({
			url: vipCardR1_url,
			type: method_get,
			async: true,
			data: {"ID": 1},
			dataType: "json",
			success: function succFunction(data){
				console.log(data);
				var vipCardInfo = data.object;
				var vipCardBonusRule = data.object2;
				console.log(vipCardBonusRule);
				form.val("vipCardInfoForm", {
					"ID": vipCardInfo.ID,
					"title": vipCardInfo.title,
					"clearBonusDay": vipCardInfo.clearBonusDay,
					"maxIncreaseBonus": vipCardBonusRule.maxIncreaseBonus,
					"increaseBonus": vipCardBonusRule.increaseBonus,
					"amountUnit": vipCardBonusRule.amountUnit/100,
					"initIncreaseBonus": vipCardBonusRule.initIncreaseBonus
				});
				console.log(vipCardInfo.backgroundColor.split(";"));
				$(".cardBody").css("background","linear-gradient(180deg,rgba(" + vipCardInfo.backgroundColor.split(";")[0] + ",1) 0%,rgba(" + vipCardInfo.backgroundColor.split(";")[1] + ",1) 100%)");
				$(".cardColor").css("background","linear-gradient(180deg,rgba(" + vipCardInfo.backgroundColor.split(";")[0] + ",1) 0%,rgba(" + vipCardInfo.backgroundColor.split(";")[1] + ",1) 100%)");
				$(".selectColor input[value='"+ vipCardInfo.backgroundColor + "']").attr("checked", "checked");
				form.render('radio', 'vipCardInfoForm');
				$(".wordCount span").text(vipCardInfo.title.length);
				$(".template .cardBody H3").text(vipCardInfo.title);
				$(".instructions .initIncreaseBonus").text(vipCardBonusRule.initIncreaseBonus);
				$(".instructions .amountUnit").text(vipCardBonusRule.amountUnit/100);
				$(".instructions .increaseBonus").text(vipCardBonusRule.increaseBonus);
				$(".instructions .maxIncreaseBonus").text(vipCardBonusRule.maxIncreaseBonus);
				$(".instructions .clearBonusDay").text(vipCardInfo.clearBonusDay);
				layer.close(pageLoading);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);

			}
		})
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

	//头部导航监听
	$("#vipMain .topArea .layui-breadcrumb a").click(function(){
		$(this).addClass("navigationBeChoosed").siblings().removeClass("navigationBeChoosed");
		if($(this).text() == "会员卡管理"){
			$(".vipCardManage").show();
			$(".vipManage").hide();
		}else if($(this).text() == "会员管理"){
			vipTable();
			$(".vipCardManage").hide();
			$(".vipManage").show();
		}else{
			//未定义的导航
		}
	})

	//会员卡字符输入监听
	window.checkWordCount = function(index){
		var word = $(index).val();
		var wordLength = word.length;
		var keyWord = $(index).attr("name");
		if(keyWord == "title"){
			$(".wordCount span").text(wordLength);
			$(".cardBody h3").text(word);
			if(wordLength > 9 || wordLength < 2){
				$(index).siblings(".form_msg").show();
			}else{
				$(index).siblings(".form_msg").hide();
			}
		}else if(keyWord == "bonusCleared"){
			$(".instructions .bonusCleared").text(word);
			if(wordLength > 128 || wordLength <= 0){
				$(index).siblings(".form_msg").show();
			}else{
				$(index).siblings(".form_msg").hide();
			}
		}else{
			//...
		}
	}

	//选择会员卡的颜色
	form.on('radio(vipCardColor)', function(data){
		console.log(data);
		var key = $(data.elem).attr("title");
		if(key === '蓝色'){
			$(".cardBody").css("background","linear-gradient(180deg,rgba(0,176,255,1) 0%,rgba(21,101,192,1) 100%)");
			$(".cardColor").css("background","linear-gradient(180deg,rgba(0,176,255,1) 0%,rgba(21,101,192,1) 100%)");
			var rgba = $(".cardColor").css('background')
			console.log(rgba) 
		};
		if(key === '蓝绿'){
			$(".cardBody").css("background","linear-gradient(180deg,rgba(0,229,255,1) 0%,rgba(0,151,167,1) 100%)");
			$(".cardColor").css("background","linear-gradient(180deg,rgba(0,229,255,1) 0%,rgba(0,151,167,1) 100%)");
		};
		if(key === '红色'){
			$(".cardBody").css("background","linear-gradient(180deg,rgba(255,82,82,1) 0%,rgba(198,40,40,1) 100%)");
			$(".cardColor").css("background","linear-gradient(180deg,rgba(255,82,82,1) 0%,rgba(198,40,40,1) 100%)");
		};
		if(key === '绿色'){
			$(".cardBody").css("background","linear-gradient(180deg,rgba(139,195,74,1) 0%,rgba(56,142,60,1) 100%)");
			$(".cardColor").css("background","linear-gradient(180deg,rgba(139,195,74,1) 0%,rgba(56,142,60,1) 100%)");
		};
		if(key === '黄色'){
			$(".cardBody").css("background","linear-gradient(180deg,rgba(255,179,0,1) 0%,rgba(239,108,0,1) 100%)");
			$(".cardColor").css("background","linear-gradient(180deg,rgba(255,179,0,1) 0%,rgba(239,108,0,1) 100%)");
		};
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

	//修改会员卡积分规则
	form.on("submit(updateVipCardBouns)",function(data){
		console.log(data);
		var vipCardBouns = data.field;
		layer.open({
			content: "是否修改会员卡积分规则",
			yes: function(index, layero){
				var loading = layer.load(1);
				$.ajax({
					url:vipBonusUpdate_url,
					type: method_post,
					async: true,
					data: {
						"ID": vipCardBouns.ID,
						"initIncreaseBonus":vipCardBouns.initIncreaseBonus,
						"amountUnit": vipCardBouns.amountUnit*100,
						"increaseBonus": vipCardBouns.increaseBonus,
						"maxIncreaseBonus": vipCardBouns.maxIncreaseBonus
					},
					dataType: "json",
					success: function succFunction(data){
						console.log(data)
						if(data.ERROR != "EC_NoError"){
							layer.msg("积分上限需要大于每次消费后赠送的积分");
						}else{
							layer.msg("修改积分规则成功");
						}
						layer.close(loading);
					},
					error: function(XMLHttpRequest, textStatus, errorThrown){
						layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);

					}
				})	
				layer.close(index); //如果设定了yes回调，需进行手工关闭
			}
		})
	});
	
	//修改会员卡属性
	form.on("submit(updateVipCard)", function(data){
		console.log(data.field);
		var vipCardData = data.field;
		layer.open({
			  content: "是否修改会员卡属性",
			  yes: function(index, layero){
				  var loading = layer.load(1);
					$.ajax({
						url:vipCardUpdate_url,
						type: method_post,
						async: true,
						data: {
							"ID":1,
							"backgroundColor": vipCardData.backgroundColor,
							"title": vipCardData.title,
							"clearBonusDay": vipCardData.clearBonusDay,
						},
						dataType: "json",
						success: function succFunction(data){
							console.log(data)
							if(data.ERROR != "EC_NoError"){
								layer.msg("填入信息有误");
							}else{
								layer.msg("修改属性成功");
							}
							layer.close(loading);
						},
						error: function(XMLHttpRequest, textStatus, errorThrown){
							layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
						}
						
					})
			    layer.close(index); //如果设定了yes回调，需进行手工关闭
			  }
			})	
	})
	
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

	
})