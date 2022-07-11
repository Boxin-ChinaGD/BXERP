package com.bx.erp.test.staff;

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
import com.bx.erp.model.Staff;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.Staff.EnumStatusStaff;
import com.bx.erp.model.StaffRole;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class StaffRoleMapperTest extends BaseMapperTest {

	public final static int DEFAULT_VALUE_RoleID = -1;
	public final static int DEFAULT_VALUE_Status = -1;

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieveNTest_CASE1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1：查询有所角色(在职和离职)");

		StaffRole sr = new StaffRole();
		sr.setRoleID(DEFAULT_VALUE_RoleID);
		sr.setStatus(DEFAULT_VALUE_Status);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2：查询在职的角色");

		StaffRole sr = new StaffRole();
		sr.setRoleID(DEFAULT_VALUE_RoleID);
		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3：查询离职的角色");

		StaffRole sr = new StaffRole();
		sr.setRoleID(DEFAULT_VALUE_RoleID);
		sr.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE4() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4：查询有所收银员(在职和离职)");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		sr.setStatus(DEFAULT_VALUE_Status);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE5() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5：查询在职的收银员");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE6() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6：查询离职的收银员");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_Cashier.getIndex());
		sr.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	// 没有经理角色了
//	@Test
//	public void retrieveNTest_CASE7() {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("CASE7：查询有所经理(在职和离职)");
//
//		StaffRole sr = new StaffRole();
//		sr.setRoleID(EnumTypeRole.ETR_Manager.getIndex());
//		sr.setStatus(DEFAULT_VALUE_Status);
//		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
//		//
//		Assert.assertTrue(retrieveN.size() >= 0 //
//				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//	}
//
//	@Test
//	public void retrieveNTest_CASE8() {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("CASE8：查询在职的经理");
//
//		StaffRole sr = new StaffRole();
//		sr.setRoleID(EnumTypeRole.ETR_Manager.getIndex());
//		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
//		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
//		//
//		Assert.assertTrue(retrieveN.size() >= 0 //
//				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//	}
//
//	@Test
//	public void retrieveNTest_CASE9() {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("CASE9：查询离职的经理");
//
//		StaffRole sr = new StaffRole();
//		sr.setRoleID(EnumTypeRole.ETR_Manager.getIndex());
//		sr.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
//		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
//		//
//		Assert.assertTrue(retrieveN.size() >= 0 //
//				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//	}

	// 没有副店长角色了
//	@Test
//	public void retrieveNTest_CASE10() {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("CASE10：查询有所副店长(在职和离职)");
//
//		StaffRole sr = new StaffRole();
//		sr.setRoleID(EnumTypeRole.ETR_Assistant.getIndex());
//		sr.setStatus(DEFAULT_VALUE_Status);
//		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
//		//
//		Assert.assertTrue(retrieveN.size() >= 0 //
//				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//	}
//
//	@Test
//	public void retrieveNTest_CASE11() {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("CASE11：查询在职的副店长");
//
//		StaffRole sr = new StaffRole();
//		sr.setRoleID(EnumTypeRole.ETR_Assistant.getIndex());
//		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
//		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
//		//
//		Assert.assertTrue(retrieveN.size() >= 0 //
//				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//	}
//
//	@Test
//	public void retrieveNTest_CASE12() {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("CASE12：查询离职的副店长");
//
//		StaffRole sr = new StaffRole();
//		sr.setRoleID(EnumTypeRole.ETR_Assistant.getIndex());
//		sr.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
//		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
//		//
//		Assert.assertTrue(retrieveN.size() >= 0 //
//				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//	}

	@Test
	public void retrieveNTest_CASE13() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE13：查询有所店长(在职和离职)");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		sr.setStatus(DEFAULT_VALUE_Status);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE14() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE14：查询在职的店长");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE15() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE15：查询离职的店长");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_Boss.getIndex());
		sr.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	// 没有业务经理角色了
//	@Test
//	public void retrieveNTest_CASE16() {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("CASE16：查询有所业务经理(在职和离职)");
//
//		StaffRole sr = new StaffRole();
//		sr.setRoleID(EnumTypeRole.ETR_CommercialManager.getIndex());
//		sr.setStatus(DEFAULT_VALUE_Status);
//		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
//		//
//		Assert.assertTrue(retrieveN.size() >= 0 //
//				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//	}
//
//	@Test
//	public void retrieveNTest_CASE17() {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("CASE17：查询在职的业务经理");
//
//		StaffRole sr = new StaffRole();
//		sr.setRoleID(EnumTypeRole.ETR_CommercialManager.getIndex());
//		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
//		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
//		//
//		Assert.assertTrue(retrieveN.size() >= 0 //
//				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//	}
//
//	@Test
//	public void retrieveNTest_CASE18() {
//		Shared.printTestMethodStartInfo();
//
//		Shared.caseLog("CASE18：查询离职的业务经理");
//
//		StaffRole sr = new StaffRole();
//		sr.setRoleID(EnumTypeRole.ETR_CommercialManager.getIndex());
//		sr.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
//		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
//		//
//		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
//		//
//		Assert.assertTrue(retrieveN.size() >= 0 //
//				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
//				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
//	}

	@Test
	public void retrieveNTest_CASE19_1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE19_1：老板查询不出博昕售前(在职和离职)");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		sr.setStatus(DEFAULT_VALUE_Status);
		sr.setOperator(EnumBoolean.EB_NO.getIndex());
		sr.setPageSize(Shared.PAGE_SIZE_MAX);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel bm : retrieveN) {
			StaffRole staffRole = (StaffRole) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole.getPhone())), "RN StaffRole错误，不应该查询出售前账号。Phone=" + staffRole.getPhone());
		}
	}

	@Test
	public void retrieveNTest_CASE19_2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE19_2：OP查询出博昕售前(在职和离职)");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		sr.setStatus(DEFAULT_VALUE_Status);
		sr.setOperator(EnumBoolean.EB_Yes.getIndex());
		sr.setPageSize(Shared.PAGE_SIZE_MAX);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		boolean existPreSale = false;
		for (BaseModel bm : retrieveN) {
			StaffRole staffRole = (StaffRole) bm;
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole.getPhone())) {
				existPreSale = true;
				break;
			}
		}
		assertTrue(existPreSale, "RN StaffRole错误，应该查询出售前账号。");
	}

	@Test
	public void retrieveNTest_CASE20_1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE20_1：老板查询在职的博昕售前");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		sr.setOperator(EnumBoolean.EB_NO.getIndex());
		sr.setPageSize(Shared.PAGE_SIZE_MAX);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel bm : retrieveN) {
			StaffRole staffRole = (StaffRole) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole.getPhone())), "RN StaffRole错误，不应该查询出售前账号。Phone=" + staffRole.getPhone());
		}
	}

	@Test
	public void retrieveNTest_CASE20_2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE20_2：OP查询在职的博昕售前");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		sr.setOperator(EnumBoolean.EB_Yes.getIndex());
		sr.setPageSize(Shared.PAGE_SIZE_MAX);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		boolean existPreSale = false;
		for (BaseModel bm : retrieveN) {
			StaffRole staffRole = (StaffRole) bm;
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole.getPhone())) {
				existPreSale = true;
				break;
			}
		}
		assertTrue(existPreSale, "RN StaffRole错误，应该查询出售前账号。");
	}

	@Test
	public void retrieveNTest_CASE21_1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE21_1：老板查询离职的博昕售前");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		sr.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		sr.setOperator(EnumBoolean.EB_NO.getIndex());
		sr.setPageSize(Shared.PAGE_SIZE_MAX);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		for (BaseModel bm : retrieveN) {
			StaffRole staffRole = (StaffRole) bm;
			assertTrue(!(BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole.getPhone())), "RN StaffRole错误，不应该查询出售前账号。Phone=" + staffRole.getPhone());
		}
	}

	@Test
	public void retrieveNTest_CASE21_2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE21_2：OP查询离职的博昕售前");
		// 删除售前
		Staff staff = new Staff();
		staff.setID(Shared.PreSaleID);
		BaseStaffTest.deleteStaff(staff, EnumErrorCode.EC_NoError, staffMapper);
		//
		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		sr.setStatus(EnumStatusStaff.ESS_Resigned.getIndex());
		sr.setOperator(EnumBoolean.EB_Yes.getIndex());
		sr.setPageSize(Shared.PAGE_SIZE_MAX);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		boolean existPreSale = false;
		for (BaseModel bm : retrieveN) {
			StaffRole staffRole = (StaffRole) bm;
			if (BaseAction.ACCOUNT_Phone_PreSale.equals(staffRole.getPhone())) {
				existPreSale = true;
				break;
			}
		}
		assertTrue(existPreSale, "RN StaffRole错误，应该查询出售前账号。");
		// 恢复售前
		Staff staff3 = new Staff();
		staff3.setID(Shared.PreSaleID);
		staff3.setInvolvedResigned(Staff.INVOLVE_RESIGNED);
		Staff staffR1 = BaseStaffTest.retrieve1Staff(staff3, staffMapper);
		staffR1.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		staffR1.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		BaseStaffTest.updateStaff(BaseBO.INVALID_CASE_ID, staffR1, EnumErrorCode.EC_NoError, staffMapper);
	}

	@Test
	public void retrieveNTest_CASE22() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE22：使用不存在的值(负数)查询角色");

		StaffRole sr = new StaffRole();
		sr.setRoleID(-999999999);
		sr.setStatus(-999999999);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE23() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE23：使用不存在的角色ID(负数)查询角色");

		StaffRole sr = new StaffRole();
		sr.setRoleID(-999999999);
		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE24() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE24：使用不存在的状态(负数)查询角色");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		sr.setStatus(-999999999);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE25() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE25：使用不存在的值(正数)查询角色");

		StaffRole sr = new StaffRole();
		sr.setRoleID(999999999);
		sr.setStatus(999999999);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE26() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE26：使用不存在的角色ID(正数)查询角色");

		StaffRole sr = new StaffRole();
		sr.setRoleID(999999999);
		sr.setStatus(EnumStatusStaff.ESS_Incumbent.getIndex());
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest_CASE27() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE27：使用不存在的状态(正数)查询角色");

		StaffRole sr = new StaffRole();
		sr.setRoleID(EnumTypeRole.ETR_PreSale.getIndex());
		sr.setStatus(999999999);
		Map<String, Object> params = sr.getRetrieveNParam(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN = staffRoleMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveN.size() >= 0 //
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieve1Test_CASE1() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1 :根据ID查询");

		StaffRole sr = new StaffRole();
		sr.setID(4);
		Map<String, Object> params = sr.getRetrieve1Param(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		StaffRole staffRole = (StaffRole) staffRoleMapper.retrieve1(params);
		//
		Assert.assertTrue(staffRole != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err = staffRole.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		System.out.println("测试成功，查询成功：" + staffRole);
	}

	@Test
	public void retrieve1Test_CASE2() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2 : 根据StaffID查询");

		StaffRole sr = new StaffRole();
		sr.setStaffID(3);
		Map<String, Object> params = sr.getRetrieve1Param(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		StaffRole staffRole = (StaffRole) staffRoleMapper.retrieve1(params);
		//
		Assert.assertTrue(staffRole != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err = staffRole.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		System.out.println("测试成功，查询成功：" + staffRole);
	}

	@Test
	public void retrieve1Test_CASE3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3 : 根据ID和StaffID查询");

		StaffRole sr = new StaffRole();
		sr.setStaffID(4);
		sr.setID(4);
		Map<String, Object> params = sr.getRetrieve1Param(BaseBO.INVALID_CASE_ID, sr);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		StaffRole staffRole = (StaffRole) staffRoleMapper.retrieve1(params);
		//
		Assert.assertTrue(staffRole != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err = staffRole.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		System.out.println("测试成功，查询成功：" + staffRole);
	}

}
