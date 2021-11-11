layui.use(['element', 'layer'], function () {
	var element = layui.element;
	var layer = layui.layer;

	//页面加载后执行
	$(document).ready(function () {
		tabWidth(304);
	})
	window.onload = function () {
		$(".iconArea i").eq(0).click();
	}
	//缩放窗口时执行
	window.onresize = function () {
		if ($("#BX_side .catalog").css("display") != "none") {
			tabWidth(304);
		} else {
			tabWidth(64);
		}
	}

	function tabWidth (num) {
		var windowWidth = $(window).width();
		console.log(windowWidth);
		$("#BX_body .layui-tab-title").css("width", windowWidth - Number(num) + "px");
	}

	//监听首页左边导航的选择
	element.on('nav(sideNav)', function (elem) {

	});
	//监听tab标签的删除
	element.on('tabDelete(topTap)', function (data) {
		//console.log(this); //当前Tab标题所在的原始DOM元素
		//console.log(data.index); //得到当前Tab的所在下标
		//console.log(data.elem); //得到当前的Tab大容器
	});
	var active = {
		tabAdd: function (url, layid, name) {
			//新增一个tap标签
			element.tabAdd('topTap', {
				title: name,
				content: '<iframe frameborder="0" src="' + url + '"></iframe>',
				id: layid
			});
		},
		tabChange: function (layid) {
			//切换到指定的tap标签
			element.tabChange('topTap', layid);
		},
		tabDelete: function (layid) {
			element.tabDelete('topTap', layid);
		},
		tabDeleteAll: function (layids) {
			$.each(layids, function (i, item) {
				element.tabDelete('topTap', item);
			})
		}
	}
	//当点击左侧的一级导航时
	$(".iconArea i").click(function () {
		var keyWord = $(this).siblings("p").text();
		$(".BX_title").siblings().hide();
		switch (keyWord) {
			case "管货":
				$(".cargoManagement").show();
				$(" .cargoManagement .layui-nav-item > a").click();
				break;
			case "管销":
				$(".barrelBolt").show();
				$(" .barrelBolt .layui-nav-item > a").click();
				break;	
			case "管帐":
				$(".keepAccounts").show();
				$(" .keepAccounts .layui-nav-item > a").click();
				break;
			case "基础资料":
				$(".basicProfile").show();
				$(" .basicProfile .layui-nav-item > a").click();
				break;
			case "设置":
				$(".installation").show();
				$(" .installation .layui-nav-item > a").click();
				break;
			case "会员相关":
				$(".vipRelated").show();
				$(" .vipRelated .layui-nav-item > a").click();
				break;
			case "门店":
				$(".storeManagement").show();
				$(" .storeManagement .layui-nav-item > a").click();
				break;
			default:
				return;
		}
	})
	//一级导航栏
	$(".iconArea ol li").click(function () {
		$(this).css("background-color", "#455A64").siblings("li").css("background-color", "#263238")

	});

	//当点击左侧的三级导航时
	$(".layui-nav-child>dd>a[lay-href]").click(function () {
		var index = $(this);
		if ($(".layui-tab-title li[lay-id]").length <= 0) {
			active.tabAdd(index.attr("lay-href"), index.attr("lay-href"), index.text());
		} else {
			var isSet = false;
			$(".layui-tab-title li[lay-id]").each(function () {
				if ($(this).attr("lay-id") == index.attr("lay-href")) {
					isSet = true;
				}
			})
			if (isSet == false) {
				active.tabAdd(index.attr("lay-href"), index.attr("lay-href"), index.text());
			}
		}
		active.tabChange(index.attr("lay-href"));
	});

	//子页面调用父页面的新增tab标签
	window.tabManage = active;

	//收缩菜单
	// $(".shrinkNav i.layui-icon-shrink-right").click(function () {
	// 	$("#BX_side").animate({ width: "64px" }, "2000", function () {
	// 		$("#BX_body, #BX_body .layui-tab-title").css("left", "64px");
	// 		$(".shrinkNavDiv").hide();
	// 		$(".spreadNav").show();
	// 		tabWidth(64);
	// 	});
	// 	$("#BX_side .catalog").animate({ width: "0px" }, "2000", function () {
	// 		$("#BX_side .catalog").hide();
	// 	});
	// })
	// //展开菜单
	// $(".spreadNav i.layui-icon-spread-left").click(function () {
	// 	$("#BX_side").animate({ width: "236px" }, "2000", function () {
	// 		$("#BX_body, #BX_body .layui-tab-title").css("left", "304px");
	// 		$(".spreadNav").hide();
	// 		$(".shrinkNavDiv").show();
	// 		$(".BX_title span").html($(".BX_title span").html());
	// 		tabWidth(304);
	// 	});
	// 	$("#BX_side .catalog").animate({ width: "240px" }, "2000", function () {
	// 		$("#BX_side .catalog").show();
	// 	});
	// })

	//退出登录
	$(".loginOut").click(function () {
		var loading = layer.load(2);
		$.ajax({
			url: "staff/logoutEx.bx",
			type: 'GET',
			async: true,
			dataType: "JSON",
			data: {},
			success: function succFunction (data) {
				layer.close(loading);
				if (data.ERROR != "EC_NoError") {
					layer.msg("退出登录失败", { time: 1000 }, function () {		//1s后跳转
						window.location.href = "../home/adminLogin.bx";
					});
					return;
				}
				layer.msg("退出登录成功", { time: 1000 }, function () {		//1s后跳转
					window.location.reload();
				});
			},
			error: function () {
				layer.close(loading);
				layer.msg("退出登录失败", { time: 1000 }, function () {		//1s后跳转
					window.location.href = "../home/adminLogin.bx";
				});
			}
		})
	})

	//博昕业务经理退出登录
	$(".bxStaffloginOut").click(function () {
		var loading = layer.load(2);
		$.ajax({
			url: "bxStaff/logoutEx.bx",
			type: 'GET',
			async: true,
			dataType: "JSON",
			data: {},
			success: function succFunction (data) {
				layer.close(loading);
				if (data.ERROR != "EC_NoError") {
					layer.msg('退出登录失败');
					return;
				}
				layer.msg("退出登录成功", { time: 1000 }, function () {		//1s后跳转
					window.location.reload();
				});
			},
			error: function () {
				layer.close(loading);
				layer.msg('退出登录失败');
			}
		})
	})
});
//首页左侧边导航
$(function () {
	$(".layui-side>ul>li").click(function () {
		var li = $(".layui-side>ul>li");
		for (var i = 0; i < li.length; i++) {
			if (this == li.get(i)) {
				if ($(".layui-side div ul").eq(i).css("display") == "block") {
					$(".layui-side div").css("display", "none");
					$(".layui-layout-admin .layui-side ul").css("width", "200px");
					$(".layui-side div ul").eq(i).css("display", "none");
					$(this).removeClass("layui-this");
				} else {
					$(".layui-layout-admin .layui-side ul").css("width", "100px");
					$(".layui-side div").css("display", "block");
					$(".layui-side div ul").eq(i).fadeIn(1000);
					$(".layui-side div ul").eq(i).siblings().css("display", "none");
				}
			}
		}
	})
});


/* 创建一个新的日期对象方法 */
Date.prototype.format = function(fmt) { 
     var o = { 
        "M+" : this.getMonth()+1,                 //月份 
        "d+" : this.getDate(),                    //日 
        "h+" : this.getHours(),                   //小时 
        "m+" : this.getMinutes(),                 //分 
        "s+" : this.getSeconds(),                 //秒 
        "q+" : Math.floor((this.getMonth()+3)/3), //季度 
        "S"  : this.getMilliseconds()             //毫秒 
    }; 
    if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
     for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
             fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
         }
     }
    return fmt; 
}  
/* 全部替换s2中的s1 */
String.prototype.replaceAll = function(s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}
