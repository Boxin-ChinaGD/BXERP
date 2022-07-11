package com.bx.erp.test;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.util.DataSourceContextHolder;

public class RetailTradePromotingFlowMapperTest extends BaseMapperTest {

	public static class DataInput {
		private static RetailTradePromotingFlow rtpf = null;
		private static RetailTradePromoting rtp = null;

		protected static final RetailTradePromoting getRetailTradePromoting() throws CloneNotSupportedException {
			Random ran = new Random();
			rtp = new RetailTradePromoting();
			rtp.setTradeID(ran.nextInt(5) + 1);
			return (RetailTradePromoting) rtp.clone();
		}

		protected static final RetailTradePromotingFlow getRetailTradePromotingFlow() throws CloneNotSupportedException {
			Random ran = new Random();
			rtpf = new RetailTradePromotingFlow();
			rtpf.setPromotionID(ran.nextInt(4) + 1);
			rtpf.setProcessFlow(UUID.randomUUID().toString().substring(1, 7));
			return (RetailTradePromotingFlow) rtpf.clone();
		}
	}

	@BeforeClass
	public void setup() {
		super.setup();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void createTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ Create RetailTradePromoting Test ------------------------");

		RetailTradePromoting rtp = DataInput.getRetailTradePromoting();
		Map<String, Object> params = rtp.getCreateParam(BaseBO.INVALID_CASE_ID, rtp);

		System.out.println("------------------------Case 1: Create a Provider first-----------------------------");
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingMapper.create(params); // ...
		rtp.setIgnoreIDInComparision(true);
		if (rtp.compareTo(retailTradePromoting) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromoting != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建失败");
		System.out.println(retailTradePromoting == null ? "providerCreate == null" : retailTradePromoting);
		// 创建从表数据
		RetailTradePromotingFlow rtpf = DataInput.getRetailTradePromotingFlow();
		rtpf.setRetailTradePromotingID(retailTradePromoting.getID());
		Map<String, Object> params2 = rtpf.getCreateParam(BaseBO.INVALID_CASE_ID, rtpf);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromotingFlow retailTradePromotingFlowCreate = (RetailTradePromotingFlow) retailTradePromotingFlowMapper.create(params2); // ...
		rtpf.setIgnoreIDInComparision(true);
		if (rtpf.compareTo(retailTradePromotingFlowCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromotingFlowCreate != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建失败");
		System.out.println(retailTradePromotingFlowCreate == null ? "retailTradePromotingFlowCreate == null" : retailTradePromotingFlowCreate);
	}

	@Test
	public void createTestCase2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:用不存在promotionID进行创建");
		RetailTradePromotingFlow rtp1 = DataInput.getRetailTradePromotingFlow();
		rtp1.setPromotionID(999999999);
		rtp1.setRetailTradePromotingID(1);
		Map<String, Object> params1 = rtp1.getCreateParam(BaseBO.INVALID_CASE_ID, rtp1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromotingFlow retailTradePromotingFlow = (RetailTradePromotingFlow) retailTradePromotingFlowMapper.create(params1); // ...
		Assert.assertTrue(retailTradePromotingFlow == null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void createTestCase3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:promotionID为空进行创建");

		RetailTradePromoting rtp = DataInput.getRetailTradePromoting();
		Map<String, Object> params = rtp.getCreateParam(BaseBO.INVALID_CASE_ID, rtp);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingMapper.create(params); // ...
		//
		Assert.assertTrue(retailTradePromoting != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建失败");
		rtp.setIgnoreIDInComparision(true);
		if (rtp.compareTo(retailTradePromoting) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		// 创建从表数据
		RetailTradePromotingFlow rtpf = DataInput.getRetailTradePromotingFlow();
		rtpf.setRetailTradePromotingID(retailTradePromoting.getID());
		rtpf.setPromotionID(0);
		Map<String, Object> params2 = rtpf.getCreateParam(BaseBO.INVALID_CASE_ID, rtpf);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromotingFlow retailTradePromotingFlowCreate = (RetailTradePromotingFlow) retailTradePromotingFlowMapper.create(params2); // ...
		//
		Assert.assertTrue(retailTradePromotingFlowCreate != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建失败");
		rtpf.setIgnoreIDInComparision(true);
		if (rtpf.compareTo(retailTradePromotingFlowCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
	}

	@Test(groups = { "RetailTradePromotingFlowUnitTest" })
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ RetrieveN RetailTradePromotingFlow Test ------------------------");
		// 创建
		RetailTradePromoting rtp = DataInput.getRetailTradePromoting();
		Map<String, Object> params = rtp.getCreateParam(BaseBO.INVALID_CASE_ID, rtp);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromoting retailTradePromoting = (RetailTradePromoting) retailTradePromotingMapper.create(params); // ...
		rtp.setIgnoreIDInComparision(true);
		if (rtp.compareTo(retailTradePromoting) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromoting != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建失败");

		RetailTradePromotingFlow rtpf = DataInput.getRetailTradePromotingFlow();
		rtpf.setRetailTradePromotingID(retailTradePromoting.getID());
		Map<String, Object> params2 = rtpf.getCreateParam(BaseBO.INVALID_CASE_ID, rtpf);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromotingFlow retailTradePromotingFlowCreate = (RetailTradePromotingFlow) retailTradePromotingFlowMapper.create(params2); // ...
		rtpf.setIgnoreIDInComparision(true);
		if (rtpf.compareTo(retailTradePromotingFlowCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradePromotingFlowCreate != null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建失败");
		// 查询

		RetailTradePromotingFlow rtpf2 = new RetailTradePromotingFlow();
		rtpf2.setRetailTradePromotingID(retailTradePromoting.getID());
		Map<String, Object> paramsRetrieveN = rtpf.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rtpf2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> rcsRetrieveN = (List<BaseModel>) retailTradePromotingFlowMapper.retrieveN(paramsRetrieveN);
		Assert.assertTrue(rcsRetrieveN.size() >= 1 && EnumErrorCode.values()[Integer.parseInt(paramsRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
	}
}
