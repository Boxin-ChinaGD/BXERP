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
import com.bx.erp.model.Permission;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

public class PermissionMapperTest extends BaseMapperTest {

	/** 不可以强制删除 */
	public static final int NO_FORCE_DELETE = 0;

	/** 可以强制删除 */
	public static final int FORCE_DELETE = 1;

	public static class DataInput {
		private static Permission permissionInput = null;

		protected static final Permission getData() throws CloneNotSupportedException {
			permissionInput = new Permission();
			permissionInput.setSP("SP_Permission_Create" + String.valueOf(System.currentTimeMillis()).substring(6));
			permissionInput.setName("删除" + String.valueOf(System.currentTimeMillis()).substring(6));
			permissionInput.setDomain("供应商权限操作" + String.valueOf(System.currentTimeMillis()).substring(6));
			permissionInput.setRemark("供应商的删除" + String.valueOf(System.currentTimeMillis()).substring(6));
			return (Permission) permissionInput.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	private List<BaseModel> retrieveNPermission(Permission p) {
		Map<String, Object> params = p.getRetrieveNParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = permissionMapper.retrieveN(params);
		//
		Assert.assertTrue(list.size() >= 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (BaseModel bm : list) {
			Permission permission = (Permission) bm;
			String err = permission.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println("查询数据成功: " + list);
		//
		return list;
	}

	private Permission createPermission(Permission p) {
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsCreate = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Permission permissionCreate = (Permission) permissionMapper.create(paramsCreate);
		//
		if (permissionCreate != null) { // 创建成功
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			err = permissionCreate.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
			//
			p.setIgnoreIDInComparision(true);
			if (p.compareTo(permissionCreate) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			//
			System.out.println("创建对象成功： " + permissionCreate);
		} else { // name重复创建失败
			Assert.assertTrue(permissionCreate == null && //
					EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, //
					paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			System.out.println("创建对象失败: " + paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg));
		}
		//
		return permissionCreate;
	}

	@Test
	public void retrieveNTest_CASE1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 无参数查询");

		Permission p = new Permission();
		p.setName("");
		p.setDomain("");
		retrieveNPermission(p);
	}

	@Test
	public void retrieveNTest_CASE2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2: 根据Name查询");

		Permission p = new Permission();
		p.setName("商品");
		p.setDomain("");
		retrieveNPermission(p);
	}

	@Test
	public void createTest_CASE1() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常创建");

		Permission p = DataInput.getData();
		createPermission(p);
	}

	@Test
	public void createTest_CASE2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:重复创建");

		Permission p = DataInput.getData();
		p.setName("添加条形码");
		createPermission(p);
	}

	@Test
	public void deleteTest_CASE1() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("正常删除");
		// 创建权限
		Permission p = DataInput.getData();
		Permission pCreate = createPermission(p);
		pCreate.setForceDelete(NO_FORCE_DELETE);
		// 删除权限
		Map<String, Object> paramsDelete = pCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		permissionMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否删除成功
		List<BaseModel> list = retrieveNPermission(pCreate);
		Assert.assertTrue(list.size() == 0);
		//
		System.out.println("删除成功");
	}

	@Test
	public void deleteTest_CASE2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2：角色操作表中存在权限不可以删除");

		Permission p = new Permission();
		p.setID(3);
		p.setForceDelete(NO_FORCE_DELETE);
		p.setName("添加条形码");
		p.setDomain("条形码");
		// 删除权限
		Map<String, Object> paramsDelete = p.getDeleteParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		permissionMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否删除成功
		List<BaseModel> list = retrieveNPermission(p);
		Assert.assertTrue(list.size() == 1);
		//
		System.out.println("角色操作表中存在权限不可以删除");
	}

	@Test
	public void deleteTest_CASE3() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:当bForceDelete=1的时候可以强制删除");
		// 创建权限
		Permission p = DataInput.getData();
		Permission pCreate = createPermission(p);
		pCreate.setForceDelete(FORCE_DELETE);
		// 删除权限
		Map<String, Object> paramsDelete = pCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		permissionMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否删除成功
		List<BaseModel> list = retrieveNPermission(pCreate);
		Assert.assertTrue(list.size() == 0);
		//
		System.out.println("删除权限成功");
	}

	@Test
	public void retrieveAlsoRoleStaffTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1: retrieveAlsoRoleStaffTest");
		//
		Permission p = new Permission();
		p.setID(3);
		Map<String, Object> params = p.getRetrieveNParam(BaseBO.CASE_Permission_RetrieveAlsoRoleStaff, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> PermissionForName = permissionMapper.retrieveAlsoRoleStaff(params);
		//
		Assert.assertTrue(PermissionForName != null//
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err;
		for (BaseModel bm : PermissionForName) {
			err = ((Permission) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println("查询数据成功： " + PermissionForName);
	}
}
