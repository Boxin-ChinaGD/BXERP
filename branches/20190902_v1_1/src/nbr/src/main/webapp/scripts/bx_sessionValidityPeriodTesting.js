/*
 * 说明：此处是调用ajax方法时，判断session是否过期
 * Java代码写在：com.bx.erp.action.interceptor.StaffLoginInterceptor 中
 * 
 * 注：如果不想让ajax方法受这个影响，可以在ajax方法中写： global:false
 * 如下：
 * $.ajax({
 *	  url:"test.html",
 *	  global:false    //不触发全局ajax事件
 * })
 * 
 * **/
$(document).ajaxComplete(function (event, xhr, settings) {
	console.log("监听到了");
	console.log(xhr);
	console.log(event)
	console.log(xhr.getResponseHeader("sessionStatus"));
	console.log("staffLoginUrl" + xhr.getResponseHeader("staffLoginUrl"));
	console.log("bxstaffLoginUrl=" + xhr.getResponseHeader("bxstaffLoginUrl"));
	layui.use('layer', function () {
		var layer = layui.layer;		//引用layui弹出层模块
		if (xhr.getResponseHeader("sessionStatus") == "timeOut") {
			if (xhr.getResponseHeader("staffLoginUrl")) {
				window.top.layer.alert('登录过期，请重新登录...', {
					cancel: function () {
						window.top.location.href = xhr.getResponseHeader("staffLoginUrl");
					}
				}, function (index) {
					window.top.location.href = xhr.getResponseHeader("staffLoginUrl");
					layer.close(index);
				});
			} else if (xhr.getResponseHeader("bxstaffLoginUrl")) {
				window.top.layer.alert('登录过期，请重新登录...', {
					cancel: function () {
						window.top.location.href = xhr.getResponseHeader("bxstaffLoginUrl");
					}
				}, function (index) {
					window.top.location.href = xhr.getResponseHeader("bxstaffLoginUrl");
					layer.close(index);
				});
			} else {
				window.top.layer.alert('请求超时请重新登陆 !', function (index) {
					layer.close(index);
				});
			}
		} else if (xhr.getResponseHeader("sessionStatus") == "duplicatedSession") {
			window.top.layer.alert('你已经在其它地方登录', {
				cancel: function () {
					window.top.location.href = xhr.getResponseHeader("staffLoginUrl");
				}
			}, function (index) {
				window.top.location.href = xhr.getResponseHeader("staffLoginUrl");
				console.log(xhr.getResponseHeader("staffLoginUrl"));
				layer.close(index);
			});
		}
	})
});