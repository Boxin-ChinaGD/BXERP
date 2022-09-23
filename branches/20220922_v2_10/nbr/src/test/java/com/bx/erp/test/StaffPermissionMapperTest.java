package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.StaffPermission;
import com.bx.erp.util.DataSourceContextHolder;

public class StaffPermissionMapperTest extends BaseMapperTest {
	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Retrieve N StaffPermission Test ----------------------");

		StaffPermission sp = new StaffPermission();
		Map<String, Object> params = sp.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sp);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bmList = staffPermissionMapper.retrieveN(params);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		assertTrue(bmList.size() > 0);
		//
		System.out.println("【查询用户权限】测试成功！");
	}
}
