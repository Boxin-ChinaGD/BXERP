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
					会员全新上线，欢迎体验
				</div>
			</div>
		</div>
		<div class="vipManage">
			<form class="layui-form queryByKeywordArea">
				<input type="text" class="layui-input" onkeyup="instantSearch()" placeholder="可通过会员手机号或卡号搜索会员"
					title="可通过姓名、手机号、会员卡号搜索会员" maxlength=16 />
				<i class="layui-icon layui-icon-search vipSearch" lay-submit lay-filter="queryVipByKeyword"></i>
			</form>
			<br>
			<div class="vipInfoArea">
				<div class="vipTableArea">
					<table id="vipTable" lay-filter="vipTable">
						<script type="text/html" id="buttonArea">
							<a class="layui-btn layui-btn-xs detail" lay-event="detail">查看</a>
						</script>
					</table>
					<button type="button" class="layui-btn layui-btn-sm toCreateVip">新建会员</button>
				</div>
				<form class="layui-form vipInfoForm" lay-filter="vipInfoForm">
					<input type="hidden" name="ID"  />
					<div class="layui-form-item">
						<label class="layui-form-label">会员编号</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_sn}" class="layui-input" readonly="true" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label"><strong class="main" readonly="true">*</strong>名称</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_name}" class="layui-input name"
								lay-verify="vipName" maxlength=32 />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">性别</label>
						<div class="layui-input-block radio">
							<input type="radio" name="${VipField.FIELD_NAME_sex}" value="0" title="女" checked>
							<input type="radio" name="${VipField.FIELD_NAME_sex}" value="1" title="男">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">身份证号</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_iCID}" class="layui-input" lay-verify="iCID"
								readonly="true" maxlength=18 />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label"><strong class="main">*</strong>手机号码</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_mobile}" class="layui-input"
								lay-verify="mobile" readonly="true" maxlength=11 />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">生日</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_birthday}" class="layui-input birthday" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">Email</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_email}" lay-verify="email"
								class="layui-input" readonly="true" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">区域</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_district}" lay-verify="district"
								id="district" class="layui-input" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">总消费次数</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_consumeTimes}" class="layui-input"
								readonly="true" />次
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">总消费金额</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_consumeAmount}" class="layui-input"
								readonly="true" />元
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">当前积分</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_bonus}" lay-verify="bonus"
								class="layui-input thisBonus" readonly="true" />分
							<a class="layui-btn layui-btn-xs revisePointBonus">修改积分</a>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">上次消费时间</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_lastConsumeDatetime}" class="layui-input"
								readonly="true" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">创建时间</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_createDatetime}" class="layui-input"
								readonly="true" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">修改时间</label>
						<div class="layui-input-block">
							<input type="text" name="${VipField.FIELD_NAME_updateDatetime}" class="layui-input"
								readonly="true" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">备注</label>
						<div class="layui-input-block">
							<textarea name="${VipField.FIELD_NAME_remark}" class="layui-textarea" readonly="true"></textarea>
						</div>
					</div>
					<div class="layui-form-item">
						<button type="button" class="layui-btn layui-btn-sm updateVip" lay-submit
							lay-filter="updateVip">保存</button>
						<button type="button" class="layui-btn layui-btn-sm createVip" lay-submit
							lay-filter="createVip">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_common.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_memberManagement.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
</body>

</html>