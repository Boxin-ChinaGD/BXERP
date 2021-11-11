package com.bx.erp.test.staff;

import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.ShopDistrict;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class ShopDistrictMapperTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:正常添加");

		ShopDistrict shopDistrict = BaseShopTest.DataInput.getShopDistrict();
		BaseShopTest.createShopDistrictViaMapper(shopDistrict);
	}

	@Test
	public void createTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:重复添加");
		// 首次添加
		ShopDistrict shopDistrict = BaseShopTest.DataInput.getShopDistrict();
		BaseShopTest.createShopDistrictViaMapper(shopDistrict);
		// 重复添加
		String err = shopDistrict.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		Map<String, Object> params = shopDistrict.getCreateParam(BaseBO.INVALID_CASE_ID, shopDistrict);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		shopDistrictMapper.create(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieve1Test_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:正常查询");
		// 添加一条数据去查询
		ShopDistrict sd = BaseShopTest.DataInput.getShopDistrict();
		ShopDistrict sdCreate = BaseShopTest.createShopDistrictViaMapper(sd);
		// 查询新添加的数据
		BaseShopTest.retrieve1ShopDistrictViaMapper(sdCreate);
	}

	@Test
	public void retrieve1Test_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:查询一个不存在的id");

		ShopDistrict sd = new ShopDistrict();
		sd.setID(999999999);
		Map<String, Object> paramsR1 = sd.getRetrieve1Param(BaseBO.INVALID_CASE_ID, sd);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ShopDistrict sdR1 = (ShopDistrict) shopDistrictMapper.retrieve1(paramsR1);
		//
		Assert.assertTrue(sdR1 == null//
				&& EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("查询失败：所查询的ID不存在 ");
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" retrieveN ShopDistrict Test ");

		BaseShopTest.retrieveNShopDistrictViaMapper();
	}
}
