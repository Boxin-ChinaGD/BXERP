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
import com.bx.erp.model.BxStaff;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseStaffTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class BxStaffMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();

	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void retrieveNTest() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("retrieveN case1:正常查询");
		//
		BxStaff bxStaff = new BxStaff();
		Map<String, Object> params = bxStaff.getRetrieveNParam(BaseBO.INVALID_CASE_ID, bxStaff);
		//
		DataSourceContextHolder.setDbName(Shared.BXDBName_Test);
		List<BaseModel> bxStaffList = bxStaffMapper.retrieveN(params);
		//
		Assert.assertTrue(bxStaffList.size() > 0//
				&& EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		String err;
		for (BaseModel bm : bxStaffList) {
			err = ((BxStaff) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(err, "");
		}
		//
		System.out.println("查询数据成功： " + bxStaffList);
	}

	@Test
	public void retrieve1Test_CASE1() {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("retrieve1Test case1:正常查询一个BxStaff");
		
		BxStaff bs = new BxStaff();
		bs.setID(1);
		bs.setMobile("");
		BaseStaffTest.retrieve1Bxstaff(bs);
	}
	
	@Test
	public void retrieve1Test_CASE2() {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("retrieve1Test case2:用不存在的ID查询一个BxStaff");
		
		BxStaff bs = new BxStaff();
		bs.setID(-1);
		bs.setMobile("");
		BaseStaffTest.retrieve1Bxstaff(bs);
	}
	
	@Test
	public void retrieve1Test_CASE3() {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("retrieve1Test case3:用mobile查询一个BxStaff");
		
		BxStaff bs = new BxStaff();
		bs.setID(0);
		bs.setMobile("13462346281");
		BaseStaffTest.retrieve1Bxstaff(bs);
	}
	
	@Test
	public void retrieve1Test_CASE4() {
		Shared.printTestMethodStartInfo();
		
		Shared.caseLog("retrieve1Test case4:用不存在的moblie查询 ");
		
		BxStaff bs = new BxStaff();
		bs.setID(0);
		bs.setMobile("12365478922");
		BaseStaffTest.retrieve1Bxstaff(bs);
	}
}
