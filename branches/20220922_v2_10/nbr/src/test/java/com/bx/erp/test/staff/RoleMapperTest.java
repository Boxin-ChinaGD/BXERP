package com.bx.erp.test.staff;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.Role;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

public class RoleMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Retrieve N Role Test");

		Role r = BaseRoleTest.DataInput.getRole();
		BaseRoleTest.retrieveNViaMapper(r);
	}

	@Test
	public void createTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 正常创建(角色名称不重复)");

		Role role = BaseRoleTest.DataInput.getRole();
		Role roleCreate = BaseRoleTest.createViaMapper(role);
		// 删除角色
		BaseRoleTest.deleteViaMapper(roleCreate);
	}

	@Test
	public void createTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:重复创建");

		Role role = BaseRoleTest.DataInput.getRole();
		Role roleCreate = BaseRoleTest.createViaMapper(role);
		
		//重复创建Role
		Map<String, Object> paramsForCreate = role.getCreateParam(BaseBO.INVALID_CASE_ID, role);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		roleMapper.create(paramsForCreate);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_Duplicated);
	
		BaseRoleTest.deleteViaMapper(roleCreate);
	}

	@Test
	public void deleteTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1：正常删除");
		// 创建角色
		Role role = BaseRoleTest.DataInput.getRole();
		Role roleCreate = BaseRoleTest.createViaMapper(role);
		// 删除角色
		BaseRoleTest.deleteViaMapper(roleCreate);

		// 强制删除角色会影响jenkins测试，由于staffPermission没有create功能所以暂时注释
		// case5:角色中仍然有权限，当bForceDelete=1的时候可以强制删除
		// r1.setID(3);
		// r1.setbForceDelete(1);
		// Map<String, Object> paramsForDelete3 =
		// r1.getDeleteParam(BaseBO.INVALID_CASE_ID, r1);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// mapper.delete(paramsForDelete3);
		// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
		// EnumErrorCode.EC_NoError);

		// 强制删除角色会影响jenkins测试，由于staffrole没有create功能所以暂时注释
		// case6:角色中已有员工在使用，当bForceDelete=1的时候可以强制删除
		// r1.setID(1);
		// r1.setbForceDelete(1);
		// Map<String, Object> paramsForDelete5 =
		// r1.getDeleteParam(BaseBO.INVALID_CASE_ID, r1);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// mapper.delete(paramsForDelete5);
		// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForDelete5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
		// EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2：bForceDelete = 1 时正常删除");
		// 创建角色
		Role role = BaseRoleTest.DataInput.getRole();
		Role roleCreate = BaseRoleTest.createViaMapper(role);
		roleCreate.setbForceDelete(EnumBoolean.EB_Yes.getIndex());
		// 删除角色
		BaseRoleTest.deleteViaMapper(roleCreate);
	}

	@Test
	public void deleteTest_CASE3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3：角色中仍然有权限，不可以删除");
		// 删除角色
		Role role = new Role();
		role.setID(3);
		Map<String, Object> paramsDelete = role.getDeleteParam(BaseBO.INVALID_CASE_ID, role);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		roleMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		role.setName("副店长");
		List<BaseModel> list = BaseRoleTest.retrieveNViaMapper(role);
		Assert.assertTrue(list.size() == 1);
	}

	@Test
	public void deleteTest_CASE4() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4：角色中已有员工在使用，不可以删除");
		// 创建角色
		Role role = new Role();
		role.setID(1);
		role.setName("收银员");
		// 删除角色
		Map<String, Object> paramsDelete = role.getDeleteParam(BaseBO.INVALID_CASE_ID, role);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		roleMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否查询成功
		List<BaseModel> list = BaseRoleTest.retrieveNViaMapper(role);
		Assert.assertTrue(list.size() >= 1);
	}

	@Test
	public void retrieveAlsoStaffTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" CASE1:正常查询 ");

		Role r = new Role();
		r.setID(4);
		Map<String, Object> params = r.getRetrieveNParam(BaseBO.CASE_Role_RetrieveAlsoStaff, r);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> roleList = roleMapper.retrieveAlsoStaff(params);
		//
		Assert.assertTrue(roleList.size() >= 0//
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void updateTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:正常修改");
		// 创建角色
		Role role = BaseRoleTest.DataInput.getRole();
		Role roleCreate = BaseRoleTest.createViaMapper(role);
		// 修改角色名称
		role.setID(roleCreate.getID());
		role.setName("收银员A1" + String.valueOf(System.currentTimeMillis() % 10000));
		Role roleUpdate = BaseRoleTest.updateViaMapper(role);
		// 删除角色
		BaseRoleTest.deleteViaMapper(roleUpdate);
	}

	@Test
	public void updateTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:修改已经存在的名字");
		// 创建角色
		Role role = BaseRoleTest.DataInput.getRole();
		Role roleCreate = BaseRoleTest.createViaMapper(role);
		// 修改角色名称
		role.setID(roleCreate.getID());
		role.setName("收银员");
		Map<String, Object> paramsUpdate = role.getUpdateParam(BaseBO.INVALID_CASE_ID, role);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Role roleUpdate = (Role) roleMapper.update(paramsUpdate);
		//
		Assert.assertTrue(roleUpdate == null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, //
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Role roleRN = (Role) BaseRoleTest.retrieveNViaMapper(roleCreate).get(0);
		//
		if (roleCreate.compareTo(roleRN) != 0) {
			Assert.assertTrue(false, "修改角色成功，期望是修改失败，该测试失败");
		}
		//
		System.out.println("修改角色失败: " + paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		// 删除角色
		BaseRoleTest.deleteViaMapper(roleCreate);
	}
}