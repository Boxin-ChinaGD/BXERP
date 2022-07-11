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
import com.bx.erp.model.Department;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class DepartmentMapperTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieveN() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1: 正常查询");
		//
		Department d = new Department();
		Map<String, Object> retrieveNParam = d.getRetrieveNParam(BaseBO.INVALID_CASE_ID, d);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = departmentMapper.retrieveN(retrieveNParam);
		//
		Assert.assertTrue(retrieveN != null && retrieveN.size() > 0//
				&& EnumErrorCode.values()[Integer.parseInt(retrieveNParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				retrieveNParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err;
		for (BaseModel bm : retrieveN) {
			err = ((Department) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println("查询数据成功: " + retrieveN);
	}
}
