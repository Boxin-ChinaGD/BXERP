layui.use(['element', 'form', 'table', 'laypage', "upload", 'laydate'], function () {
	var element = layui.element;
	var upload = layui.upload;
	const importData_url = "import/importEx.bx";		//上传商品资料接口
	const downloadEx_url = "import/downloadEx.bx";		//下载存在格式错误的文件
	const method_get = "GET";
	const method_post = "POST";
	$('#test').click(function () {
		element.progress('demo', '0%');
		$('.theChooseFileName').html()
		$(".Tips").hide();
		$(".icon").hide();
		$(".Tips1").hide();
		$(".icon1").hide();
		$(".Tips2").hide();
		$(".icon2").hide();
	})

	$('#testListAction').click(function () {
		if ($('#testListAction').attr('lay-percent', '0%')) {
			element.progress('demo', '10%');
		} else {
			element.progress('demo', '0%');
		}
		$('.state').text("正在上传");
		$('.state').css("color", "green");
		$('.theChooseFileName').hide();
		$(".Tips").hide();
		$(".icon").hide();
		$(".Tips1").hide();
		$(".icon1").hide();
		$(".Tips2").hide();
		$(".icon2").hide();
	})

	//	$('#formatError').click(function(){
	//		 $.ajax({
	//			url: downloadEx_url,
	//			type: method_post,
	//			async: true,
	//			data: {
	//				
	//			},
	//			dataType: "json",
	//			success: function (data) {
	//				console.log(data);
	//				console.log(data.filePath)
	//				$('#formatError').attr('href', data.filePath);
	//				console.log($('#formatError').attr('href'))
	//			},
	//			error: function(XMLHttpRequest, textStatus, errorThrown){
	//				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
	//			}
	//		})
	//	})
	var demoListView = $('#demoList')
	uploadListIns = upload.render({
		elem: '#test',
		url: importData_url,
		auto: false, //选择文件后自动上传
		accept: 'file',
		bindAction: '#testListAction',
		size: 5120,
		exts: 'xlsm',
		multiple: false,
		choose: function (obj) {
			$('#progressCSS').attr('class', 'layui-progress-bar layui-bg-green')
			var files = this.files = obj.pushFile();
			obj.preview(function (index, file, result) {
				const fileName = '博销宝资料导入模板.xlsm';
				var chooseFileName = file.name;
				if (chooseFileName != fileName) {
					console.log("文件名错误");
					layer.msg("文件名必须为“博销宝资料导入模板.xlsm”");
					setTimeout(function () {
						window.location.reload()
					}, 1000);
					return (false);
				}
				var tr = $(['<tr id="upload-' + index + '" class = "files">'
					, '<td>' + file.name + '</td>'
					, '<td>' + (file.size / 1024).toFixed(1) + 'kb</td>'
					, '<td class="state">等待上传</td>'
					, '<td>'
					, '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
					, '<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
					, '</td>'
					, '</tr>'].join(''));
				tr.find('.demo-reload').on('click', function () {
					obj.upload(index, file);
				});

				tr.find('.demo-delete').on('click', function () {
					element.progress('demo', '0%');
					$('#testListAction').hide();
					delete files[index]; //删除对应的文件
					tr.remove();
					uploadListIns.config.elem.next()[0].value = '';
				});
				demoListView.append(tr);
				element.progress('demo', '0%');
				$('#testListAction').show();
				console.log(file.name);
			});
		},
		before: function (obj) {
		},
		done: function (res, index, upload) {
			console.log(res);
			$('.demo-delete').click();
			element.progress('demo', '100%');
			$('.state').text("等待上传");
			$('.testListAction').hide();
			$(".commodityTotalToCreate").text(res.commodityTotalToCreate);
			$(".commodityWrongFormatNumber").text(res.commodityWrongFormatNumber);
			$(".providerTotalToCreate").text(res.providerTotalToCreate);
			$(".providerWrongFormatNumber").text(res.providerWrongFormatNumber);
			$(".vipTotalToCreate").text(res.vipTotalToCreate);
			$(".vipWrongFormatNumber").text(res.vipWrongFormatNumber);
			$(".commodityFailCreateNumber").text(res.commodityFailCreateNumber);
			$(".vipFailCreateNumber").text(res.vipFailCreateNumber);
			if (res.needToDownload == 0) {
				$('.formatError').hide();
			}
			if (res.ERROR == "EC_NoError") {
				$(".commodityWrongFormatNumber").text(0);
				$(".providerWrongFormatNumber").text(0);
				$(".vipWrongFormatNumber").text(0);
				$(".Tips").show();
				$(".icon1").show();
			} else if (res.ERROR == "EC_WrongFormatForInputField") {
				$(".Tips1").show();
				$(".icon2").show();
				$('#progressCSS').attr('class', 'layui-progress-bar layui-bg-red')
				$.ajax({
					url: downloadEx_url,
					type: method_post,
					async: true,
					data: {

					},
					dataType: "json",
					success: function (data) {
						console.log(data);
						console.log(data.filePath)
						$('.formatError').attr('href', data.filePath);
						console.log($('.formatError').attr('href'))
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
					}
				})
			} else {
				$.ajax({
					url: downloadEx_url,
					type: method_post,
					async: true,
					data: {

					},
					dataType: "json",
					success: function (data) {
						console.log(data);
						console.log(data.filePath)
						$('.formatError').attr('href', data.filePath);
						console.log($('.formatError').attr('href'))
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
					}
				})
				if (res.needToDownload == 1) {
					$('.otherErrorDownload').show();
				}
				$('#progressCSS').attr('class', 'layui-progress-bar layui-bg-red')
				$(".Tips2").show();
				$(".icon").show();
			}

		},
		error: function (index, upload) {//请求异常回调
			// var tr = demoListView.find('tr#upload-'+ index),
			// tds = tr.children();
			// tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
			// tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
		}
	});
})