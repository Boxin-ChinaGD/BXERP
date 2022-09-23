layui.use([ 'form', 'layer' ], function(){
	var form = layui.form;
	var layer = layui.layer;

	const staffGetToken_url = "staff/getTokenEx.bx";
	const staffLogin_url = "staff/loginEx.bx";
	const method_post = "POST";
	//数据验证
	form.verify({
		companySN: function(value, item){
			if(value){
				if(!(/^[0-9]{6,8}$/.test(value))){
					isToSend = true;
					return "请输入正确的公司编号，只能输入6到8位的数字";
				}
			}else{
				isToSend = true;
				return "必填项不能为空";
			}
		},
		phone: function(value, item){
			if(value){
				if(!(/^(1[0-9])\d{9}$/.test(value))){
					isToSend = true;
					return "请输入正确的手机号码";
				}
			}else{
				isToSend = true;
				return "必填项不能为空";
			}
		},
		password: function(value, item){
			if(value){
				if(!(/^[^ ].{4,14}[^ ]$/.test(value))){
					isToSend = true;
					return "密码错误，请重新输入";
				}
			}else{
				isToSend = true;
				return "必填项不能为空";
			}
		}
	})
	//监听回车键
	var isToSend = true;		//准备发送表单数据用以登录，isToSend为true时表示可以通过回车键触发登录事件，为false时阻止通过回车键触发登录事件
	$(document).keypress(function(e){
		if(e.keyCode == 13 && isToSend){
			isToSend = false;
			$(".layui-btn-lg").click();
		}
	})
	//登录系统
	form.on("submit(login)", function(data){
		var loading = layer.load(1);
		var loginInfo = data.field;
		var password = loginInfo.pwdEncrypted;
		$.ajax({
			url: staffGetToken_url,
			type: method_post,
			dataType: "JSON",
			data: {"phone": loginInfo.phone},
			cache: false,
			async: true,
			success : function(data){
				var modulus = data.rsa.modulus;
				var exponent = data.rsa.exponent;
				var rsa = new RSAKey();
				rsa.setPublic(modulus, exponent);
				var res = rsa.encrypt(password);
				if(res){
					loginInfo.pwdEncrypted = res;
					$.ajax({
						url: staffLogin_url,
						type: method_post,
						async: true,
						dataType: "JSON",
						data: loginInfo,
						success: function(data){
							layer.close(loading);
							if(data){
								if(data.ERROR != "EC_NoError"){
									if(data.msg){
										layer.msg(data.msg);
									}else{
										layer.msg("手机号码或密码错误");
									}
									isToSend = true;
									return;
								}
								window.location.href = "../home.bx";
							}else{
								isToSend = true;
								layer.msg("登录失败，公司编号错误");
							}
						},
						error: function(){
							isToSend = true;
							layer.close(loading);
							layer.msg("服务器错误");
						}
					});
				}else{
					isToSend = true;
					layer.close(loading);
					layer.msg("登录失败");
				}
			},
			error: function(){
				isToSend = true;
				layer.close(loading);
				layer.msg("服务器错误");
			}
		});
		return false;
	})
})