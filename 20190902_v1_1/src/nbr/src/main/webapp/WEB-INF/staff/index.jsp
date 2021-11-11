<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<link rel="bookmark" type="image/x-icon" href="bx.ico"/>
	<link rel="shortcut icon" href="bx.ico">
	<link type="text/css" rel="stylesheet" href="BXUI/css/layui.css" />
	<link type="text/css" rel="stylesheet" href="css/bx_staffList.css" />
	<title>BoXin-员工列表</title>
</head>
<body>
	<div id="staffMain">
		<input type="hidden" id="sessionStaffID" value="${sessionScope.SESSION_Staff.ID}">
		<!-- 头部导航区域 -->
		
		<!-- 中部显示区域 -->
		<div class="staff_middleArea">
			<div class="middleArea_left">
				<div class="topArea">
				<h3>员工分类</h3>
				<span class="showAll">全部展开</span>
			</div>
			<ul class="layui-nav layui-nav-tree">
				<li class="layui-nav-item layui-nav-itemed"><a href="javascript:void(0);">全部员工分类</a>
					<dl class="layui-nav-child">
						<dd class="layui-this">
							<a href="JavaScript:void(0);" >全部在职员工<input type="hidden" value="-1"></a>
						</dd>
						<c:forEach items="${roleList}" var="rl">
							<c:if test="${rl.name == '店长' || rl.name == '收银员'}">
								<dd>
									<a href="JavaScript:void(0);" >${rl.name}<input type="hidden" value="${rl.ID}"></a>
								</dd>
							</c:if>
						</c:forEach>
						<dd class="layui-nav-item"><a href="javascript:void(0);">离职员工</a></dd>
					</dl>
				</li>
			</ul>
		</div>
	</div>
	<div class="middleArea_right" >
		<div id="shousuo" class="layui-form">
			<input type="text" name="${StaffField.FIELD_NAME_queryKeyword}" class="layui-input" placeholder="请输入姓名、手机号码搜索员工" title="请输入姓名、手机号码搜索员工" autocomplete="off" onkeyup="instantSearch()" lay-verify="pNumEnChineseNull" maxlength="12" />
			<i class="layui-icon layui-icon-search staffSearch" style="font-size: 20px;" lay-submit lay-filter="staffSearch"></i>	
		</div>
		<table id="staffList" lay-filter="staffList">
			<script type="text/html" id="staffName">
				<span style="color: #1E9FFF; cursor: pointer;">{{d.name}}</span>
			</script>
		</table>
	</div>
	<!-- 详情区 -->
	<div id="staffDetail">
		<div class="staffInfo layui-form" lay-filter="staffInfo">
			<div class="fixedButtonArea">
			    <button class="layui-btn layui-btn-sm staffManage btnChoosed staffCreate">新建</button>
				<button class="layui-btn layui-btn-sm staffManage staffUpdate" lay-submit lay-filter="staffUpdate">保存</button>
				<button class="layui-btn layui-btn-sm staffManage deleteStaff">删除</button>
				<button class="layui-btn layui-btn-sm staffManage">取消</button>
			</div>
			<h3>基本信息</h3>
			<input type="hidden" name="${StaffField.FIELD_NAME_ID}" class="staffID" />
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label"><strong class="requiredField">*</strong>姓名</label>
					<div class="layui-input-inline">
						<input type="text" class="layui-input staffName" name="${StaffField.FIELD_NAME_name}" autocomplete="off" lay-verify="required|pEnChinese" maxlength="12" onchange="check_ifDataChange(this,this.value);"/>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"><strong class="requiredField">*</strong>角色</label>
					<div class="layui-input-inline">
						<select name="${StaffField.FIELD_NAME_roleID}" class="roleID" lay-filter="spinnerCheck" lay-verify="required">
							<c:forEach items="${roleList}" var="rl">
								<c:if test="${rl.name == '收银员' || rl.name == '店长'}">
							    	<option value="${rl.ID}">${rl.name}</option>
							    </c:if>
						    </c:forEach>
					    </select>
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">状态</label>
					<div class="layui-input-inline">
						<select name="${StaffField.FIELD_NAME_status}" class="staffStatus" lay-filter="spinnerCheck">
							<option value="0">在职</option>
							<option value="1" disabled="disabled" class="select_status layui-disabled">离职</option>
					    </select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"><strong class="requiredField">*</strong>手机号</label>
					<div class="layui-input-inline">
						<input type="text" class="layui-input staffPhone" name="${StaffField.FIELD_NAME_phone}" oninput="checkUniqueField(this)" onchange="check_ifDataChange(this,this.value);" autocomplete="off" lay-verify="required|phone" maxlength="11"/>
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">身份证号</label>
					<div class="layui-input-inline">
						<input type="text" class="layui-input staffICID" name="${StaffField.FIELD_NAME_ICID}" oninput="checkUniqueField(this)" onchange="check_ifDataChange(this,this.value);" autocomplete="off" lay-verify="ICIDNull" maxlength="18"/>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">微信号</label>
					<div class="layui-input-inline">
						<input type="text" class="layui-input staffWeChat" name="${StaffField.FIELD_NAME_weChat}" oninput="checkUniqueField(this)" onchange="check_ifDataChange(this,this.value);" autocomplete="off" lay-verify="staffWechat" maxlength="20"/>
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label"><strong class="requiredField">*</strong>部门</label>
					<div class="layui-input-inline">
						<select name="${StaffField.FIELD_NAME_departmentID}" class="departmentID" lay-filter="spinnerCheck">
							<c:forEach items="${departmentList}" var="dl">
					    		<option value="${dl.ID}">${dl.departmentName}</option>
					    	</c:forEach>
				    	</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label"><strong class="requiredField">*</strong>门店</label>
						<div class="layui-input-inline">
							<select name="${StaffField.FIELD_NAME_shopID}" class="shopID" lay-filter="spinnerCheck">
								<c:forEach items="${shopList}" var="sl">
					    			<option value="${sl.ID}">${sl.name}</option>
					    		</c:forEach>
					    	</select>
						</div>
					</div>
				</div>
				<div class="layui-form-item staffAppendPassword">
					<div class="layui-inline">
					<label class="layui-form-label"><strong class="requiredField">*</strong>新密码</label>
						<div class="layui-input-inline">
							<input type="password" name="${StaffField.FIELD_NAME_newPassword}" maxlength="16" autocomplete="off" lay-verify="required|password" class="layui-input newPassword" onchange="check_ifDataChange(this,this.value);"/>
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label"><strong class="requiredField">*</strong>确认新密码</label>
						<div class="layui-input-inline">
							<input type="password" name="${StaffField.FIELD_NAME_confirmNewPassword}" maxlength="16" autocomplete="off" lay-verify="required|password" class="layui-input confirmNewPassword" onchange="check_ifDataChange(this,this.value);"/>
						</div>
					</div>
				</div>
				<div class="layui-form-item createTime">
					<div class="layui-inline">
						<label class="layui-form-label">创建时间</label>
						<div class="layui-input-inline">
							<input type="text" class="layui-input" name="${StaffField.FIELD_NAME_createDatetime}" autocomplete="off" readOnly="readOnly" style="width: 220px" />
						</div>
					</div>
				</div>
				<div class="layui-form-item updateTime">
					<div class="layui-inline">
						<label class="layui-form-label">修改时间</label>
						<div class="layui-input-inline" style="width: 220px">
							<input type="text" class="layui-input" name="${StaffField.FIELD_NAME_updateDatetime}" autocomplete="off" readOnly="readOnly" style="width: 220px" />
						</div>
					</div>
				</div>
<!-- 				<div class="layui-form-item" style="display: none"> -->
<!-- 					<div class="layui-inline"> -->
<!-- 						<label class="layui-form-label">密码有效期</label> -->
<!-- 						<div class="layui-input-inline"> -->
<%-- 							<input type="text" id="passwordExpireDate" autocomplete="off" class="layui-input" name="${StaffField.FIELD_NAME_passwordExpireDate}" autocomplete="off" /> --%>
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 				</div> -->
			</div>
			<div  class="layui-form" lay-filter="layerResetPassword">
				<div class="layerResetPassword staffInfo">
					<div class="layui-button">
						<button class="layui-btn layui-btn-sm resetPassword">修改密码</button>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
						<label class="layui-form-label"><strong class="requiredField">*</strong>新密码</label>
							<div class="layui-input-inline">
								<input type="password" name="${StaffField.FIELD_NAME_resetNewPassword}" maxlength="16" autocomplete="off" lay-verify="required|password" class="layui-input resetNewPassword" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
						<label class="layui-form-label"><strong class="requiredField">*</strong>确认新密码</label>
							<div class="layui-input-inline">
								<input type="password" name="${StaffField.FIELD_NAME_confirmResetNewPassword}" maxlength="16" autocomplete="off" lay-verify="required|password" class="layui-input confirmResetNewPassword" />
							</div>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="submit_button">
							<button class="layui-btn layui-btn-sm saveResetPassword" lay-submit lay-filter="resetPassword">保存</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" charset="utf-8" src="scripts/jquery.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="BXUI/layui.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_jsbn.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_prng4.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rng.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_rsa.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_sessionValidityPeriodTesting.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_staffList.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_functionReuse.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_fieldFormat.js"></script>
	<script type="text/javascript" charset="utf-8" src="scripts/bx_format.js"></script>
</body>
</html>