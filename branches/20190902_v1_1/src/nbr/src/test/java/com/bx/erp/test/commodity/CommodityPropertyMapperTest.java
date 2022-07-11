package com.bx.erp.test.commodity;

import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.CommodityProperty;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class CommodityPropertyMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Retrieve1 CommodityProperty Test");
		CommodityProperty cp = BaseCommodityTest.DataInput.getCommodityProperty();
		Map<String, Object> paramsRetrieve = cp.getRetrieve1Param(BaseBO.INVALID_CASE_ID, cp);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CommodityProperty commodityPropertyRetrieve1 = (CommodityProperty) commodityPropertyMapper.retrieve1(paramsRetrieve);// ...
		//
		cp.setIgnoreIDInComparision(true);
		if (cp.compareTo(commodityPropertyRetrieve1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(commodityPropertyRetrieve1);
		Assert.assertTrue(commodityPropertyRetrieve1.getID() == 1);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Update CommodityProperty Test");
		CommodityProperty cp = BaseCommodityTest.DataInput.getCommodityProperty();
		cp.setName1("666666666666");
		Map<String, Object> paramsForUpdate = cp.getUpdateParam(BaseBO.INVALID_CASE_ID, cp);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		CommodityProperty commodityPropertyUpdate = (CommodityProperty) commodityPropertyMapper.update(paramsForUpdate); // ...
		//
		cp.setIgnoreIDInComparision(true);
		if (cp.compareTo(commodityPropertyUpdate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(commodityPropertyUpdate);
		Assert.assertTrue(commodityPropertyUpdate.getID() == 1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}
}
