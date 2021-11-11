//数据验证（正则表达式）
function fieldFormat(form){
	form.verify({
//		commoditySearch: function(value, item){		//商品模糊搜索（名称、简称、条形码、助记码）
//			if(!(/^$|^[A-Za-z0-9]+$/.test(value)) && !(/^$|^[\u2014\u4E00-\u9FA5A-Za-z0-9_\()（）-\s]+$/.test(value))){
//				return '输入关键字格式不对';
//			}
//		},
		telephone: function(value, item){
			if((/^[\u4E00-\u9FA5A-Za-z0-9\s]+$/.test(value)) && !(/^[0-9]*$/.test(value))){
				return "手机号码格式不对";
			}
		},
		vipSearch: function(value, item){		//会员模糊搜索（手机号）
			if(!(/^[A-Za-z0-9]+$/.test(value)) && !(/^[\u4E00-\u9FA5A-Za-z0-9\s]+$/.test(value))){
				return '输入完整的手机号';
			}
		},
		providerName: function(value, item){		//供应商名称数据验证
			if(/^$| /.test(value)){
				return '不能输入空格';
			}
		},
		charDbNmae: function(value,item){//私有DB名称验证
			if(!(/^[a-zA-Z][a-zA-Z0-9_]*$/.test(value)) && !(/^$| /.test(value))){
				return '首字符只能为字母，整个字段名称不能出现中文或特殊符号';
			}
		},
		checkTypeOfInt: function(value, item){
			if(value > (Math.abs(2 << 30) - 1)){
				return '输入的值不符合要求，请修改';
			}
		},
		checkRefCommodityMultiple: function(value, item){
			if(!(/^[1-9]\d*$/.test(value)) || value <= 1){
				return '商品倍数为大于1的整数';
			}
		},
		commodityName: function(value, item){
			console.log(value);
			if(!(/^[\u2014\u4E00-\u9FA5A-Za-z0-9_\()（）* $ # “ ” 、/ -\s]{1,32}$/.test(value)) || value.trim().length != value.length){
				return '商品名称格式不正确，请输入中英文、数字，只允许中间有空格，长度为(0,32]，支持输入的符号有：（）()_-——';
			}
		},
		checkKeywordHaveCommodityName: function(value, item){
			console.log(value);
			if(!(/^$|^[\u2014\u4E00-\u9FA5A-Za-z0-9_\()（）-\s]{1,}$/.test(value)) || value.trim().length != value.length){
				return '数据格式不正确，请输入中英文、数字，只允许中间有空格，支持输入的符号有：（）()_-——';
			}
		},
		checkBarcode: function(value, item){
			if(!(/^[A-Za-z0-9]+$/.test(value)) || value.length < 7 || value.length > 64){
				return '条形码格式错误，仅允许英文、数值形式，长度为[7,64]';
			}
		},
//		phone: [/^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/, '请输入正确的手机号码'],		//手机号码，用户登录输入
		phone: [/^(1[0-9])\d{9}$/, '请输入正确的手机号码'],		//手机号码，用户登录输入
		phoneNull: [/^$|^(1[0-9])\d{9}$/, '请输入正确的手机号码'],		//手机号码，可空
		password: [/^[^ ].{4,14}[^ ]$/, '请输入6-16位的字符,首尾不能有空格'],		//6-16位的字符，首尾不能有空格
		ICIDNull:[/^$|^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, '请输入正确的身份证号码'],		//不可以为空
		pNumEnSpace:[/^[\u4E00-\u9FA5A-Za-z0-9\s]+$/, '允许以中英数值、空格形式出现，不允许使用特殊符号'],
		pNumEnSpaceNull:[/^$|^[\u4E00-\u9FA5A-Za-z0-9\s]+$/, '允许以中英数值、空格形式出现，不允许使用特殊符号'],		//可以为空
		pNumEn:[/^[A-Za-z0-9]+$/, '只允许数字和英文'],
		pNumEnNull:[/^$|^[A-Za-z0-9]+$/, '只允许数字和英文'],		//可以为空
		pNature:[/^[1-9]\d*$/, '只允许正整数'],
		pNatureNull:[/^$|^[1-9]\d*$/, '只允许正整数'],		//可以为空
		pDecimals:[/^([0-9][0-9]*)+(\.[0-9]{1,2})?$/, '请输入非负数字，最多带2位小数'],
		pDecimalsNull:[/^$|^([0-9][0-9]*)+(\.[0-9]{1,2})?$/, '请输入非负数字，最多带2位小数'],		//可以为空
		pNum:[/^[0-9]\d*$/, '只允许非负整数'],
		pNumNull:[/^$|^[0-9]\d*$/, '只允许非负整数'],		//可以为空
		pNumEnChinese:[/^[\u4E00-\u9FA5A-Za-z0-9]+$/, '只允许中文、数字和英文'],
		pNumEnChineseNull:[/^$|^[\u4E00-\u9FA5A-Za-z0-9]+$/, '只允许中文、数字和英文'],		//可以为空
		pNumber:[/^-?\d+$/, '只能输入整数'],
		pNumberNull:[/^$|^-?\d+$/, '只能输入整数'],		//可以为空
		pNature0:[/^[0-9\s]+$/, '只允许自然数'],		//包括0
		pNature0Null:[/^$|^[0-9\s]+$/, '只允许自然数'],		//包括0,可以为空
		pNumFloat:[/^(-?\d+)(\.\d+)?$/, '只允许浮点数'],
		NO: [/^(0|[1-9][0-9]*|-[1-9][0-9]*)$/, '只允许整数'],
		noSpace20: [/^[^\s]{0,20}$/, '不能使用空格，20个字符以内'],		//限制了长度{0,20}
		pDiscount: [/^([1-9](\.\d){0,1}?)$/, '请输入正整数，最多带1位小数'],
		pPar: [/^(\d{1,5}(\.\d){0,1}?)$/, '小数点前允许最多输入5位自然数，小数点后最多带1位小数'],
		pEnChinese:[/^$|^[\u4E00-\u9FA5A-Za-z]+$/, '只允许中文和英文'],		//可以为空
		pNumberPar:[/^$|^[0-9]+(\.[0-9]{1,2})?$/, '只允许正数，小数点后最多只能带两位小数'],
		pNumb:[/^[0-9]{6,8}$/, '只能输入6到8位的数字'],
		checkphone:[/^[0-9]{6,11}$/, '请输入6到11位的手机号'],
		provider:[/^[^ ].{0,20}[^ ]$/, '请输入20位的字符'],	//20位的字符，首尾不能有空格
		Email: [/^$|^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, '请输入正确的邮箱格式'],		//可以为空
		noBlank: [/^\S+$/,'不能输入空格'],			//不可以为空
		noBlankNull: [/^$|^\S+$/,'不能输入空格'],			//可以为空
		businessLicenseSN:[/^[A-Z0-9]{15}$|^[A-Z0-9]{18}$/, '数据格式不符合要求，请输入15位或18位的大写字母或数字或大写字母与数字的组合'],
		boosWechat:[/^[a-zA-Z][A-Za-z0-9_-]{5,20}$/,"必须以字母开头，5位以上20位以下，由字母、数字、下划线和减号组成，不支持设置中文"],		//5到20位
		companyKey:[/^[A-Z0-9]{32}$/,"只能32位的数字与大写字母组合"],	//key值验证
		noChinese:[/^[A-Za-z0-9\!@#$%^&*?._\s]+$/,"密码不可输入中文字符"],
		limitedSubmchidLength: [/^$|^.{10}$/, "格式不正确，请输入10位数字"],		//可为空
		staffWechat: [/^$|^[A-Za-z0-9]{5,}$/, "员工微信号数据格式不对，请输入5-20位的英文或数字"],		//可以为空
		noSpaceHeadOrFootNull: [/^(?! ).*(?<! )$/, "首尾不能有空格"],		//可以为空
		pPosSN:[/^[A-Za-z0-9-]+$/, '只允许数字和英文以及短横杠'],
		p7To24Char: [/^$|.{7,24}$/, "输入的字符不能少于7位"]		//可以为空
	})
}
