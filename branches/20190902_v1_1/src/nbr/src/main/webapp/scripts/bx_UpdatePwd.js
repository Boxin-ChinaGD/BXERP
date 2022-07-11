layui.use(['form', 'layer'], function(){
	var form = layui.form;
	var layer = layui.layer;

	const staffGetToken_url = "staff/getTokenEx.bx";
	const staffResetMyPassword_url = "staff/resetMyPasswordEx.bx";
	const method_post = "POST";
	layer.ready(function(){
		if($("#changePassword").val() == 'changePassword'){
			$(".title").text("修改密码");
		}
	})
	//数据验证
	fieldFormat(form);
	//修改密码
	form.on("submit(firstLoginToUpadtePwd)", function(data){
		var loading = layer.load(2);
		var phone = window.parent.$("#sessionPhone").val();
		var password = data.field.password;
		var newPassword = data.field.newPassword;
		var confirmNewPassword = data.field.confirmNewPassword;
		if(newPassword == confirmNewPassword){
			if(password != newPassword){
				$.ajax({
					url: staffGetToken_url,
					type: method_post,
					data: {"phone": phone, "forModifyPassword": 1},		//用户手机号码待沟通是前端传还是后端自己在缓存中拿//forModifyPassword为1防止删除会话
					dataType: "JSON",
					cache: false,
					async: true,
					success : function(data){
						var modulus = data.rsa.modulus;
						var exponent = data.rsa.exponent;
						var rsa = new RSAKey();
						rsa.setPublic(modulus, exponent);
						var passwordOldEncrypted = rsa.encrypt(password);		//加密原密码
						var passwordNewEncrypted = rsa.encrypt(newPassword);		//加密新密码
						if (passwordOldEncrypted && passwordNewEncrypted) {
							$.ajax({
								url: staffResetMyPassword_url,
								type: method_post,
								async: true,
								data: {
									"phone": phone, 
									"sPasswordEncryptedOld": passwordOldEncrypted, 
									"sPasswordEncryptedNew": passwordNewEncrypted,
								},
								dataType: "JSON",
								success: function(data) {
									console.log(data);
									var msg = "";
									if(data.ERROR != "EC_NoError"){
										layer.close(loading);
										switch (data.ERROR) {
											case "EC_WrongFormatForInputField":
												msg = "新密码不能与原密码相同";
											break;
											case "EC_NoSuchData":
												msg = "原密码错误";
											break;
											default:
												msg = "修改密码失败";
											break;
										}
										if(data.msg){
											layer.msg(data.msg);
										}else{
											layer.msg(msg);
										}
									}else{
										layer.alert("修改密码成功", {icon: 1, 
											cancel: function(index, layero){
												if($("#changePassword").val() == 'LAST_Location_StaffManagement'){
													pageClose("home/updateMyPwd.bx");
													pageClose("staff.bx");
													pageJumping('staff.bx', 'staff.bx', '员工管理');
												}else if($("#changePassword").val() == 'LAST_Location_Navigation'){
													pageClose("home/updateMyPwd.bx");
												}else{
													window.location.href = "../home.bx";
												}
											}}, 
											function(index){
												if($("#changePassword").val() == 'LAST_Location_StaffManagement'){
													pageClose("home/updateMyPwd.bx");
													pageClose("staff.bx");
													pageJumping('staff.bx', 'staff.bx', '员工管理');
												}else if($("#changePassword").val() == 'LAST_Location_Navigation'){
													pageClose("home/updateMyPwd.bx");
												}else{
													window.location.href = "../home.bx";
												}
											}
										);
									}
									layer.close(loading);
								},
								error: function(){
									layer.close(loading);
									layer.msg("服务器错误");
								}
							});
						}else{
							layer.close(loading);
							layer.msg("修改密码失败");
						}
					},
					error: function(){
						layer.close(loading);
						layer.msg("服务器错误");
					}
				});
			}else{
				layer.close(loading);
				layer.msg("新密码不能与原密码相同");
			}
		}else{
			layer.close(loading);
			layer.msg("两次输入的新密码不一致");
		}
		return false;
	})
})