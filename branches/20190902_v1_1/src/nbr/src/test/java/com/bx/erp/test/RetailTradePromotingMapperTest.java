package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.util.DataSourceContextHolder;

public class RetailTradePromotingMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static RetailTradePromoting retailTradeInput = null;

		protected static final RetailTradePromoting getRetailTradePromoting() throws CloneNotSupportedException {
			Random ran = new Random();
			retailTradeInput = new RetailTradePromoting();
			retailTradeInput.setTradeID(ran.nextInt(5) + 1);
			return (RetailTradePromoting) retailTradeInput.clone();
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
	public void retrieve1Test1() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("\n------------------------ Create RetailTradePromoting Test ------------------------");

		Shared.caseLog("------------------------Case 1: ID存在时，进行查询 错误码为0---------------------------");
		RetailTradePromoting rtp = DataInput.getRetailTradePromoting();
		Map<String, Object> params = rtp.getCreateParam(BaseBO.INVALID_CASE_ID, rtp);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingMapper.create(params); // ...
		rtp.setIgnoreIDInComparision(true);
		if (rtp.compareTo(retailTradePromoting) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromoting != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		System.out.println(retailTradePromoting == null ? "providerCreate == null" : retailTradePromoting);

		Map<String, Object> createParams = rtp.getRetrieve1Param(BaseBO.INVALID_CASE_ID, retailTradePromoting);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting rtpR1 = (RetailTradePromoting) retailTradePromotingMapper.retrieve1(createParams);

		Assert.assertTrue(rtpR1 != null && EnumErrorCode.values()[Integer.parseInt(createParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		Assert.assertTrue(rtpR1.compareTo(retailTradePromoting) == 0, "查找出的数据和DB的不一样");
	}

	@Test
	public void retrieve1Test2() throws CloneNotSupportedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("------------------------Case 2: ID不存在时，进行查询 错误码为7-------------------------");
		RetailTradePromoting rtp2 = DataInput.getRetailTradePromoting();
		Map<String, Object> params2 = rtp2.getCreateParam(BaseBO.INVALID_CASE_ID, rtp2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting2 = (RetailTradePromoting) retailTradePromotingMapper.create(params2); // ...
		rtp2.setIgnoreIDInComparision(true);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		System.out.println(retailTradePromoting2 == null ? "providerCreate == null" : retailTradePromoting2);
		//
		retailTradePromoting2.setID(-1);
		Map<String, Object> createParams2 = rtp2.getRetrieve1Param(BaseBO.INVALID_CASE_ID, retailTradePromoting2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting rtpCreate2 = (RetailTradePromoting) retailTradePromotingMapper.retrieve1(createParams2);
		//
		assertTrue(rtpCreate2 == null);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createParams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("\n------------------------ Create RetailTradePromoting Test ------------------------");

		RetailTradePromoting rtp = DataInput.getRetailTradePromoting();
		Map<String, Object> params = rtp.getCreateParam(BaseBO.INVALID_CASE_ID, rtp);

		Shared.caseLog("------------------------Case 1: Create a Provider first-----------------------------");
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingMapper.create(params); // ...
		rtp.setIgnoreIDInComparision(true);
		if (rtp.compareTo(retailTradePromoting) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromoting != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		System.out.println(retailTradePromoting == null ? "providerCreate == null" : retailTradePromoting);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("------------------------Case 2: 重复添加-----------------------------");
		RetailTradePromoting rtp = DataInput.getRetailTradePromoting();
		Map<String, Object> params = rtp.getCreateParam(BaseBO.INVALID_CASE_ID, rtp);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingMapper.create(params); // ...
		rtp.setIgnoreIDInComparision(true);
		if (rtp.compareTo(retailTradePromoting) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromoting != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		Map<String, Object> params1 = rtp.getCreateParam(BaseBO.INVALID_CASE_ID, rtp);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting2 = (RetailTradePromoting) retailTradePromotingMapper.create(params1); // ...
		rtp.setIgnoreIDInComparision(true);
		if (rtp.compareTo(retailTradePromoting2) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromoting2 != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("------------------------Case 2: 零售单ID不存在-----------------------------");
		RetailTradePromoting rtp = DataInput.getRetailTradePromoting();
		rtp.setTradeID(BaseAction.INVALID_ID);
		Map<String, Object> params = rtp.getCreateParam(BaseBO.INVALID_CASE_ID, rtp);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingMapper.create(params); // ...
		Assert.assertTrue(retailTradePromoting == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, "返回的错误码不正确");
	}
}
