package com.bx.erp.test.staff;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.RolePermission;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class PermissionRoleMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

//	@Test
//	public void deleteTest() throws CloneNotSupportedException, InterruptedException {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("Delete Role Test");
//
//		RolePermission rp = new RolePermission();
//		rp.setRoleID(EnumTypeRole.ETR_Manager.getIndex());
//		Map<String, Object> paramsForDelete = rp.getDeleteParam(BaseBO.INVALID_CASE_ID, rp);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		rolePermissionMapper.delete(paramsForDelete);
//		//
//		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//
//	}
}
