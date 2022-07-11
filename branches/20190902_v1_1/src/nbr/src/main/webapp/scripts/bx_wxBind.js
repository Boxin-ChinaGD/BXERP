layui.use([ 'form', 'layer' ], function(){
    var form = layui.form;
    var layer = layui.layer;
  //定义常量
	const wxGetToken_url = "wx/getTokenEx.bx";
    const wxUserBind_url = "wx/userBind.bx";
    const method_post = "POST";
  //数据验证
//	fieldFormat(form); //这一行在Android上无问题，在iOS上有问题
	form.verify({
		checkCompanySN: [/^[0-9]{6,8}$/, '只能输入6到8位的数字'],
		checkPhone: [/^(1[0-9])\d{9}$/, '请输入正确的手机号码'],		//手机号码数据验证
		checkPassword: [/^[^ ].{4,14}[^ ]$/, '请输入6-16位的字符,首尾不能有空格']		//6-16位的字符，首尾不能有空格
	})
    //监听回车键
	$(document).keypress(function(e){
		if(e.keyCode == 13){
			$(".layui-btn-lg").click();
		}
    });
    //登录系统
	form.on("submit(bind)", function(data){
        console.log(data);
        var loginInfo = data.field;
		var password = loginInfo.pwdEncrypted;
        $.ajax({
        	url: wxGetToken_url,
			type: method_post,
			dataType: "JSON",
			data: {"phone": loginInfo.phone},
			cache: false,
			async: true,
			success : function(data){
				console.log(data);
				var modulus = data.rsa.modulus;
				var exponent = data.rsa.exponent;
				var rsa = new RSAKey();
				rsa.setPublic(modulus, exponent);
				var res = rsa.encrypt(password);
				if(res){
					loginInfo.pwdEncrypted = res;
					console.log(loginInfo.companySN);
					$.ajax({
						url: wxUserBind_url,
						type: method_post,
						async: true,
						dataType: "json",
						data: {
							"companySN": loginInfo.companySN,
							"openid": loginInfo.openid,
							"phone": loginInfo.phone,
							"pwdEncrypted": loginInfo.pwdEncrypted,
						},
						success: function succFunction(data) {
							if(data.ERROR == "EC_NoError"){
								$(".layui-btn").attr("disabled","disabled");
								$(".layui-btn").css({"background":"rgb(190, 188, 188)"});
								layer.open({
									time:3000, 
									title:false,
									shadeClose:false, 
									btn:false,
									closeBtn:false, 
									content: '<div class="MsgStyle">绑定成功</div>'
								  })
							}else{
								if(data.msg){
									layer.open({
										time:3000, 
										title:false,
										shadeClose:false, 
										btn:false,
										closeBtn:false, 
										content: '<div class="MsgStyle">'+data.msg+'</div>'
									  })
								}else{
									layer.open({
										time:3000, 
										title:false,
										shadeClose:false, 
										btn:false,
										closeBtn:false, 
										content: '<div class="MsgStyle">绑定失败</div>'
									})
								}
							}
						}
					})
				}
			}
        })
    })
})
