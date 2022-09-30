package com.bx.erp.test;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTradeAggregation;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;

public class RetailTradeAggregationMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static RetailTradeAggregation rtaInput = null;

		protected static final RetailTradeAggregation getRetailTradeAggregation() {
			Random ran = new Random();
			rtaInput = new RetailTradeAggregation();
			rtaInput.setStaffID(ran.nextInt(5) + 1);
			rtaInput.setPosID(ran.nextInt(5) + 1);
			rtaInput.setWorkTimeStart(new Date());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			rtaInput.setWorkTimeEnd(new Date());
			rtaInput.setTradeNO(ran.nextInt(500));
			rtaInput.setAmount(ran.nextInt(5000));
			rtaInput.setReserveAmount(500);
			rtaInput.setCashAmount(ran.nextInt(2000));
			rtaInput.setWechatAmount(ran.nextInt(2000));
			rtaInput.setAlipayAmount(ran.nextInt(2000));
			try {
				return (RetailTradeAggregation) rtaInput.clone();
			} catch (CloneNotSupportedException e) {
				Assert.assertTrue(false, "RetailTradeAggregation clone失败");
				return null;
			}
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
	public void createTest1() {
		Shared.printTestMethodStartInfo();

		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		Map<String, Object> params = rta.getCreateParam(BaseBO.INVALID_CASE_ID, rta);

		Shared.caseLog("Case 1: Create a RetailTradeAggregation first");

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeAggregation retailTradeAggregation = (RetailTradeAggregation) retailTradeAggregationMapper.create(params); // ...
		rta.setIgnoreIDInComparision(true);
		if (rta.compareTo(retailTradeAggregation) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeAggregation != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
	}

	@Test
	public void createTest2() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 2: 重复添加");
		RetailTradeAggregation rta2 = DataInput.getRetailTradeAggregation();
		// 首次创建
		Map<String, Object> params2 = rta2.getCreateParam(BaseBO.INVALID_CASE_ID, rta2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeAggregation retailTradeAggregation2 = (RetailTradeAggregation) retailTradeAggregationMapper.create(params2); // ...
		rta2.setIgnoreIDInComparision(true);
		if (rta2.compareTo(retailTradeAggregation2) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeAggregation2 != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		// 再次创建
		retailTradeAggregation2 = (RetailTradeAggregation) retailTradeAggregationMapper.create(params2); // ...
		Assert.assertTrue(retailTradeAggregation2 != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, "创建对象失败");

	}

	@Test
	public void createTest3() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 3: 用不存在的staffID进行创建");
		RetailTradeAggregation rta3 = DataInput.getRetailTradeAggregation();
		rta3.setStaffID(-99);
		Map<String, Object> params3 = rta3.getCreateParam(BaseBO.INVALID_CASE_ID, rta3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeAggregation retailTradeAggregation3 = (RetailTradeAggregation) retailTradeAggregationMapper.create(params3); // ...

		Assert.assertTrue(retailTradeAggregation3 == null && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象失败");
	}

	@Test
	public void createTest4() {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 4: 用不存在的posID进行创建");
		RetailTradeAggregation rta4 = DataInput.getRetailTradeAggregation();
		rta4.setPosID(BaseAction.INVALID_POS_ID);
		Map<String, Object> params4 = rta4.getCreateParam(BaseBO.INVALID_CASE_ID, rta4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeAggregation retailTradeAggregation4 = (RetailTradeAggregation) retailTradeAggregationMapper.create(params4); // ...

		Assert.assertTrue(retailTradeAggregation4 == null && EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象失败");

	}
	
	@Test
	public void createTest5() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 5: reserveAmount小于0");
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		rta.setReserveAmount(-1);
		String errorMsg = rta.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMsg, RetailTradeAggregation.FIELD_ERROR_ReserveAmount);
	}
	
	@Test
	public void createTest6() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 5: reserveAmount大于10000");
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		rta.setReserveAmount(10001);
		String errorMsg = rta.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMsg, RetailTradeAggregation.FIELD_ERROR_ReserveAmount);
	}
	
	@Test
	public void createTest7() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 7: workTimeStart为null");
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		rta.setWorkTimeStart(null);
		String errorMsg = rta.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMsg, RetailTradeAggregation.FIELD_ERROR_workTimeStart);
	}
	
	@Test
	public void createTest8() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 8: workTimeEnd为null");
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		rta.setWorkTimeEnd(null);
		String errorMsg = rta.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMsg, RetailTradeAggregation.FIELD_ERROR_workTimeEnd);
	}
	
	@Test
	public void createTest9() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 9: workTimeStart比workTimeEnd晚");
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		rta.setWorkTimeStart(new Date());
		rta.setWorkTimeEnd(DatetimeUtil.getDate(rta.getWorkTimeStart(), -1));
		String errorMsg = rta.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMsg, RetailTradeAggregation.FIELD_ERROR_workTimeStartAndEnd);
	}
	
	@Test
	public void createTest10() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 10: 交易单数小于0");
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		rta.setTradeNO(-1);
		String errorMsg = rta.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMsg, RetailTradeAggregation.FIELD_ERROR_TradeNO);
	}
	
	@Test
	public void createTest11() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 11: staffID小于0");
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		rta.setStaffID(-1);
		String errorMsg = rta.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMsg, RetailTradeAggregation.FIELD_ERROR_StaffID);
	}
	
	@Test
	public void createTest12() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 12: posID小于0");
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		rta.setPosID(-1);
		String errorMsg = rta.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMsg, RetailTradeAggregation.FIELD_ERROR_PosID);
	}
	
	@Test
	public void createTest13() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 13: amout小于0");
		RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
		retailTradeAggregation.setAmount(-1);
		retailTradeAggregation.setCashAmount(-1);
		retailTradeAggregation.setWechatAmount(0);
		retailTradeAggregation.setAlipayAmount(0);
		String error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void createTest14() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 14: cashAmout小于0");
		RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
		retailTradeAggregation.setCashAmount(-1);
		retailTradeAggregation.setAmount(-1);
		retailTradeAggregation.setWechatAmount(0);
		retailTradeAggregation.setAlipayAmount(0);
		String error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void createTest15() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 15: wechatAmout小于0");
		RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
		retailTradeAggregation.setWechatAmount(-1);
		retailTradeAggregation.setAmount(-1);
		retailTradeAggregation.setCashAmount(0);
		retailTradeAggregation.setAlipayAmount(0);
		String error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
	}
	
	@Test
	public void createTest16() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 16: 每种支付方式对应的钱加起来不等于营业额");
		RetailTradeAggregation retailTradeAggregation = DataInput.getRetailTradeAggregation();
		retailTradeAggregation.setAmount(500);
		retailTradeAggregation.setWechatAmount(100);
		retailTradeAggregation.setCashAmount(200);
		String error = retailTradeAggregation.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, RetailTradeAggregation.FIELD_ERROR_amountTotal);
	}

	@Test
	public void retrieve1Test1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 1: 收银员交班后，查询收银汇总");
		// 避免workTimeStart与前面的测试用例重复
		Thread.sleep(1000);
		//
		RetailTradeAggregation rta = DataInput.getRetailTradeAggregation();
		rta.setStaffID(2);
		rta.setPosID(1);
		Map<String, Object> paramsCreate = rta.getCreateParam(BaseBO.INVALID_CASE_ID, rta);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeAggregation retailTradeAggregationCreated = (RetailTradeAggregation) retailTradeAggregationMapper.create(paramsCreate); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		rta.setIgnoreIDInComparision(true);
		if (rta.compareTo(retailTradeAggregationCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Map<String, Object> paramsRetrieve1 = rta.getRetrieve1Param(BaseBO.INVALID_CASE_ID, rta);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeAggregation retailTradeAggregationRetrieve1 = (RetailTradeAggregation) retailTradeAggregationMapper.retrieve1(paramsRetrieve1); // ...
		Assert.assertTrue(retailTradeAggregationRetrieve1 != null, "查询新创建的对象失败");
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRetrieve1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (retailTradeAggregationRetrieve1.compareTo(retailTradeAggregationCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与Retrieve1读出的不相等");
		}
	}
}
