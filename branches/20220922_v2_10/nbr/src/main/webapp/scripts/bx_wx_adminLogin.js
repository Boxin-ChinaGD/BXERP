var dw = document.documentElement.clientWidth;
document.documentElement.style.fontSize = dw / 6.4 + "px";
window.onresize = function(){
	var dw = document.documentElement.clientWidth;
	document.documentElement.style.fontSize = dw / 6.4 + "px";
}
layui.use([ 'form', 'layer' ], function() {
	var form = layui.form;
	var layer = layui.layer;
	const wxLogin_url = "wx/afterWxLogin.bx";
	// 数据验证
//	fieldFormat(form); //这一行在Android上无问题，在iOS上有问题
	form.verify({
		checkCompanySN: [/^[0-9]{6,8}$/, '只能输入6到8位的数字'],
		checkPhone: [/^(1[0-9])\d{9}$/, '请输入正确的手机号码'],		//手机号码数据验证
		checkPassword: [/^[^ ].{4,14}[^ ]$/, '请输入6-16位的字符,首尾不能有空格']		//6-16位的字符，首尾不能有空格
	})
	//登录系统
	form.on("submit(wxLogin)", function(data){
		var indexLoading = layer.load(1);
		var info = data.field;
		$.ajax({
			url: wxLogin_url,
			type: 'get',
			async: true,
			dataType: "json",
			data: info,
			success: function succFunction(data) {
				console.log(data);
				layer.close(indexLoading);
				if (data) {
					if (data.ERROR != "EC_NoError") {
						if (data.msg) {
							layer.msg("<span style='font-size: 0.2rem;'>" + data.msg + "</span>");
						} else {
							layer.msg("<span style='font-size: 0.2rem;'>登录失败</span>");
						}
					} else {
						switch (info.identify) {
							case "purchasingOrder":
								window.location.href = "../wx/purchasingOrderApproval.bx?ID=" + info.ID;
								break;
							case "unSalableCommodity":
								window.location.href = "../wx/unsalableCommodity.bx";
								break;
							default:
								console.log("未定义的标识符");
								break;
						}
					}
				} else {
					layer.msg("<span style='font-size: 0.2rem;'>登录失败</span>");
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				layer.close(indexLoading);
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
			}
		});
	})
})