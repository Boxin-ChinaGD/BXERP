package com.bx.erp.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.config.BxConfigGeneral;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;

public class BxConfigGeneralMapperTest extends BaseMapperTest {

	private static final String errorMSsg = "错误信息与预期不相符";

	public static class DataInput {
		protected static final BxConfigGeneral getBXConfigGeneral() throws Exception {
			BxConfigGeneral c = new BxConfigGeneral();
			c.setName("aa" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			c.setValue(String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);

			return (BxConfigGeneral) c.clone();
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

	// @Test
	// public void updateTest() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// System.out.println("\n------------------------ update BXConfigGeneral Test
	// ------------------------");
	//
	// BxConfigGeneralMapper mapper = (BxConfigGeneralMapper)
	// applicationContext.getBean("bxConfigGeneralMapper");
	// BxConfigGeneral bxConfigGeneral = DataInput.getBXConfigGeneral();
	// bxConfigGeneral.setID(1);
	//
	// Map<String, Object> params =
	// bxConfigGeneral.getUpdateParam(BaseBO.INVALID_CASE_ID, bxConfigGeneral);
	//
	// DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
	// BxConfigGeneral bxConfigGeneralCreate = (BxConfigGeneral)
	// mapper.update(params);
	//
	// assertTrue(bxConfigGeneralCreate != null &&
	// EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError, "修改对象失败");
	//
	// bxConfigGeneral.setIgnoreIDInComparision(true);
	// if (bxConfigGeneral.compareTo(bxConfigGeneralCreate) != 0) {
	// assertTrue(false, "创建的对象的字段与DB读出的不相等");
	// }
	//
	// System.out.println("修改BXConfigGeneral 成功！！");
	// }

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1：正常查询");
		//
		BxConfigGeneral bxConfigGeneral = DataInput.getBXConfigGeneral();
		bxConfigGeneral.setID(1);
		//
		String error = bxConfigGeneral.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, "", errorMSsg);
		//
		Map<String, Object> params = bxConfigGeneral.getRetrieve1Param(BaseBO.INVALID_CASE_ID, bxConfigGeneral);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		BxConfigGeneral bxConfigGeneralCreate = (BxConfigGeneral) bxConfigGeneralMapper.retrieve1(params);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(bxConfigGeneralCreate != null && bxConfigGeneralCreate.getID() == 1);
	}

	@Test
	public void retrieve1Test2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2：ID为0去进行查询");
		//
		BxConfigGeneral bxConfigGeneral = DataInput.getBXConfigGeneral();
		bxConfigGeneral.setID(0);
		//
		String error = bxConfigGeneral.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, BxConfigGeneral.FIELD_ERROR_BxConfigGeneralID, errorMSsg);
		//
		Map<String, Object> params = bxConfigGeneral.getRetrieve1Param(BaseBO.INVALID_CASE_ID, bxConfigGeneral);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		BxConfigGeneral bxConfigGeneralCreate = (BxConfigGeneral) bxConfigGeneralMapper.retrieve1(params);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(bxConfigGeneralCreate == null);
	}

	@Test
	public void retrieve1Test3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3：ID为-99去进行查询");
		//
		BxConfigGeneral bxConfigGeneral = DataInput.getBXConfigGeneral();
		bxConfigGeneral.setID(-99);
		//
		String error = bxConfigGeneral.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, BxConfigGeneral.FIELD_ERROR_BxConfigGeneralID, errorMSsg);
		//
		Map<String, Object> params = bxConfigGeneral.getRetrieve1Param(BaseBO.INVALID_CASE_ID, bxConfigGeneral);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		BxConfigGeneral bxConfigGeneralCreate = (BxConfigGeneral) bxConfigGeneralMapper.retrieve1(params);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(bxConfigGeneralCreate == null);
	}

	@Test
	public void retrieve1Test4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4：ID为超出公共配置表的ID去进行查询");
		//
		BxConfigGeneral bxConfigGeneral = DataInput.getBXConfigGeneral();
		bxConfigGeneral.setID(BxConfigGeneral.MAX_BxConfigGeneralID + 1);
		//
		String error = bxConfigGeneral.checkRetrieve1(BaseBO.INVALID_CASE_ID);
		assertEquals(error, BxConfigGeneral.FIELD_ERROR_BxConfigGeneralID, errorMSsg);
		//
		Map<String, Object> params = bxConfigGeneral.getRetrieve1Param(BaseBO.INVALID_CASE_ID, bxConfigGeneral);
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		BxConfigGeneral bxConfigGeneralCreate = (BxConfigGeneral) bxConfigGeneralMapper.retrieve1(params);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(bxConfigGeneralCreate == null);
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN BXConfigGeneral Test ------------------------");

		BxConfigGeneral bxConfigGeneral = DataInput.getBXConfigGeneral();

		Map<String, Object> params = bxConfigGeneral.getRetrieveNParam(BaseBO.INVALID_CASE_ID, bxConfigGeneral);

		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		List<BaseModel> bxConfigGeneralCreate = bxConfigGeneralMapper.retrieveN(params);

		assertTrue(bxConfigGeneralCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象失败");

		System.out.println("查询BXConfigGeneral 成功：" + bxConfigGeneralCreate);
	}
}
