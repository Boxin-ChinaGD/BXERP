<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<base href="${pageContext.request.contextPath}/">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<META HTTP-EQUIV="pragma" CONTENT="no-cache">
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
	<META HTTP-EQUIV="expires" CONTENT="0">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge，chrome=1">
	<meta name="renderer" content="webkit">
	<link rel="bookmark" type="image/x-icon" href="bx.ico" />
	<link rel="shortcut icon" href="bx.ico">
	<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_vipManage.css" />
	<title>BoXin-会员卡</title>
</head>

<body>
	<div id="vipMain">
		<div class="vipCardManage">
			<div class="tipArea">
				<div>
					<i class="layui-icon layui-icon-tips"></i>
					会员卡全新上线，欢迎体验
				</div>
			</div>
			<div class="vipCardInfo">
				<div class="leftArea">
					<div class="template">
						<img src="../../images/vipCardPic.png" alt="" id="companyLogin">
						<div class="cardBody">
							<h4>门店名称</h4>
							<img src="../../images/login.jpg" /><span>会员名称</span>
						</div>
					</div>
					<div class="instructions">
						<p>初始积分：<span class="initIncreaseBonus"></span>。</p>
						<p>积分规则：每消费<span class="amountUnit"></span>元增加<span class="increaseBonus"></span>积分。</p>
						<p>单次可获取的积分上限：<span class="maxIncreaseBonus"></span>。</p>
						<p>积分清零天数：<span class="clearBonusDay"></span></p>
					</div>
				</div>
				<div class="rightArea">
					<form class="layui-form" lay-filter="vipCardInfoForm">
						<div class="layui-form-item">
							<label class="layui-form-label"><strong>*</strong>会员卡名称</label>
							<input type="hidden" name="${VipCardField.FIELD_NAME_ID}" />
							<div class="layui-input-block">
								<input type="text" name="title" class="layui-input" value="" lay-verify="title"
									oninput="checkWordCount(this)" />
								<div class="wordCount">
									<span>0</span>/9
								</div>
								<p class="form_msg">字数上限为2~9个汉字、英文、数字。(建议涵盖会员卡属性、服务)</p>
							</div>
						</div>
						<div class="companyLogin">
							<label class="layui-form-label"><strong>*</strong>上传门店图片</label>
							<button type="button" class="layui-btn" id="test1" title="仅支持大小1920*1080，格式为jpg、png的图片">
								<i class="layui-icon">&#xe67c;</i>门店图片
							</button>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label"><strong>*</strong>会员卡颜色</label>
							<div class="layui-input-block">
								<div class="cardColor"></div>
								<div class="selectColor">
									<input type="radio" name="backgroundColor" lay-filter="vipCardColor"
										value="0,176,255;21,101,192" title="蓝色" checked="checked">
									<input type="radio" name="backgroundColor" lay-filter="vipCardColor"
										value="0,229,255;0,151,167" title="蓝绿">
									<input type="radio" name="backgroundColor" lay-filter="vipCardColor"
										value="255,82,82;198,40,40" title="红色">
									<input type="radio" name="backgroundColor" lay-filter="vipCardColor"
										value="139,195,74;56,142,60" title="绿色">
									<input type="radio" name="backgroundColor" lay-filter="vipCardColor"
										value="255,179,0;239,108,0" title="黄色">
								</div>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label"><strong>*</strong>积分清零天数</label>
							<div class="layui-input-block">
								<input type="text" name="${VipCardField.FIELD_NAME_clearBonusDay}"
									class="layui-input clearBonusDay" value="" lay-verify="clearBonusDay"
									oninput="checkNum(this)" /> <span>天</span>
								<p class="form_msg">积分清零天数大于0</p>
							</div>
						</div>
						<div class="layui-form-item">
							<button type="button" class="layui-btn layui-btn-sm" id="layui-button" lay-submit
								lay-filter="updateVipCard">修改属性</button>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label"><strong>*</strong>初始积分</label>
							<div class="layui-input-block">
								<input type="text" name="${BonusRuleField.FIELD_NAME_initIncreaseBonus}"
									class="layui-input" value="" lay-verify="initIncreaseBonus"
									oninput="checkNum(this)" />分
								<p class="form_msg">初始积分需为大于等于0的数字</p>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label"><strong>*</strong>积分规则</label>
							<div class="layui-input-block">
								<span>
									每消费<input type="text" name="${BonusRuleField.FIELD_NAME_amountUnit}"
										class="layui-input" value="" lay-verify="amountUnit"
										oninput="checkNum(this)" />元,
									<p class="form_msg">金额需大于0，小于2147483647。</p>
								</span>
								<span>
									赠送<input type="text" name="${BonusRuleField.FIELD_NAME_increaseBonus}"
										class="layui-input" value="" lay-verify="increaseBonus"
										oninput="checkNum(this)" />积分
									<p class="form_msg">积分需大于0，小于2147483647。</p>
								</span>
							</div>
						</div>
						<div class="layui-form-item">
							<label class="layui-form-label"><strong>*</strong>单次可获取的积分上限</label>
							<div class="layui-input-block">
								<input type="text" name="${BonusRuleField.FIELD_NAME_maxIncreaseBonus}"
									class="layui-input theMaxIncreaseBonus" value="" lay-verify="maxIncreaseBonus"
									oninput="checkNum(this)" /> <span>分</span>
								<p class="form_msg">单次可获取的积分上限需大于0，小于2147483648</p>
							</div>
						</div>
						<div class="layui-form-item theUpdateVipCardBouns">
							<button type="button" class="layui-btn layui-btn-sm" lay-submit
								lay-filter="updateVipCardBouns">积分规则修改</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_vipManage.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
</body>

</html>