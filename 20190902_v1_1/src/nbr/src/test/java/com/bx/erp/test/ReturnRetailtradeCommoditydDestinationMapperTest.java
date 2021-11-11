package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.ReturnRetailtradeCommoditydDestination;
import com.bx.erp.util.DataSourceContextHolder;

public class ReturnRetailtradeCommoditydDestinationMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void testRetrieveN_case1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常查询,retailTradeCommodityID为存在的ID");
		ReturnRetailtradeCommoditydDestination rrcd = new ReturnRetailtradeCommoditydDestination();
		rrcd.setRetailTradeCommodityID(5320);
		//
		Map<String, Object> params = rrcd.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rrcd);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNList = returnRetailtradeCommoditydDestinationMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveNList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("【查询退货商品去向表】测试成功！");
	}
}
