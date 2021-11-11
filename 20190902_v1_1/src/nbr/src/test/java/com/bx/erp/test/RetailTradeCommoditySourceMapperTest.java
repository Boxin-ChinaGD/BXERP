package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTradeCommoditySource;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.util.DataSourceContextHolder;

public class RetailTradeCommoditySourceMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static RetailTradeCommoditySource retailTradeCommoditySourceInput = null;

		protected static final RetailTradeCommoditySource getRetailTradeCommoditySource() throws CloneNotSupportedException, InterruptedException {
			retailTradeCommoditySourceInput = new RetailTradeCommoditySource();
			retailTradeCommoditySourceInput.setRetailTradeCommodityID(17);

			return (RetailTradeCommoditySource) retailTradeCommoditySourceInput.clone();
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

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常查询");
		RetailTradeCommoditySource recs = new RetailTradeCommoditySource();
		//
		Map<String, Object> params = recs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, recs);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNList = retailTradeCommoditySourceMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveNList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("【查询零售单来源】测试成功！");

	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:根据零售单商品ID查询");
		RetailTradeCommoditySource recs = DataInput.getRetailTradeCommoditySource();
		//
		Map<String, Object> params = recs.getRetrieveNParam(BaseBO.INVALID_CASE_ID, recs);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNList = retailTradeCommoditySourceMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveNList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		for (BaseModel bm : retrieveNList) {
			RetailTradeCommoditySource retailTradeCommoditySource = (RetailTradeCommoditySource) bm;
			assertTrue(retailTradeCommoditySource.getRetailTradeCommodityID() == recs.getRetailTradeCommodityID(), "RN的数据异常");
		}
		System.out.println("【查询零售单来源】测试成功！");

	}

}
