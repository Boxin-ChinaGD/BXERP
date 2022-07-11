layui.use([ 'form', 'layer' ], function(){
    var form = layui.form;
    var layer = layui.layer;
  //定义常量
	const generateQRCode_url= "miniprogram/generateQRCode.bx";		//生成二维码接口
	const method_post = "POST";
	
	//验证前端输入的宽度值
	form.verify({
		QRwidth: function(value, item){
			if(value < 280 || value > 1280){
				isToSend = true;
				return "二维码的宽度值必须大于280并且小于1280";
			}
		},
	});
	
	$('.downloadQRcode').click(function(){
		if($('.downloadQRcode').attr('href') == ''){
			$('.downloadQRcode').attr('download','');
			layer.msg("尚无二维码可下载");
		}
	});

  //数据验证
	form.on('submit(createQRcode)', function(obj){
		var indexLoading = layer.load(1);
		var QRwidth = $(".QRwidth").val();
		$(".QRwidth").val("");
		$.ajax({
			url: generateQRCode_url,
			type: method_post,
			data: {				
				"width": QRwidth
			},
			cache: false,
			async: true,
			dataType: "json",
			success: function(data){
				console.log(data);
				$('.theQRcode img').attr('src',data.qrCodeUrl ); //图片链接（base64）
				$('.downloadQRcode').attr('href',data.qrCodeUrl );
				console.log($('.downloadQRcode').attr('href'))
				layer.close(indexLoading);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown){
				layer.msg(XMLHttpRequest.status + "：" + XMLHttpRequest.statusText);
				layer.close(indexLoading);
			}
		});
		return false;
	});

})
