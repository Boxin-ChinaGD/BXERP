package com.bx.erp.test.promotion;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.action.trade.promotion.BasePromotion.EnumPromotionScope;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.Promotion.EnumStatusPromotion;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.RetailTradePromoting;
import com.bx.erp.model.trade.RetailTradePromotingFlow;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class PromotionMapperTest extends BaseMapperTest {

	/** 查询 尚未开始 的活动 */
	public static final int RETRIEVEN_NO_START_PROMOTION = 10;

	/** 查询 正在进行中 的活动 */
	public static final int RETRIEVEN_STARTING_PROMOTINO = 11;

	/** 查询 已经结束 的活动 */
	public static final int RETRIEVEN_ENDED_PROMOTION = 12;

	/** 查询进行中还有即将要进行的活动 */
	public static final int RETRIEVEN_NO_ENDED_PROMOTION = 13;

	/** 返回所有状态为0的促销活动 */
	public static final int RETRIEVEN_NO_DELETED_PROMOTION = 0;

	/** 输入未定义的 @iInt1 */
	public static final int UNDEFINE_STATUS_OF_PROMOTION = 7;

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

		Shared.caseLog("CASE1:添加促销活动 只传入满减金额");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setExcecutionDiscount(-1);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void createTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2:添加促销活动 只传入满减折扣 ");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		p.setExcecutionAmount(-1);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void createTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:添加促销活动 type 输入不存在的值");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(BaseAction.INVALID_Type);
		assertEquals(p.checkCreate(BaseBO.INVALID_CASE_ID), Promotion.FIELD_ERROR_type);
		// createPromotion(p); // 因创建失败，所有不用删除
	}

	@Test
	public void createTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:添加满减金额的促销活动 传入满减金额和满减折扣");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void createTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:添加满折金额促销活动 传入满减金额和满减折扣");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DiscountOnAmount.getIndex());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void createTest_CASE6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE6:满减阀值等于满减金额");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionThreshold(100);
		p.setExcecutionAmount(100);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void createTest_CASE7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE7:满减阀值大于满减金额");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionThreshold(100);
		p.setExcecutionAmount(10);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void createTest_CASE8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE8:满减阀值小于满减金额");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionThreshold(10);
		p.setExcecutionAmount(100);
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID); // 因为当前CASE是属于字段不合法,所以在Promotion的checkCreate方法中会报错并没有CAll相应的SP
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount_excecutionThreshold);
	}

	@Test
	public void createTest_CASE9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE9:满减阀值大于满减金额但是满减阀值超过最大值");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionThreshold(10001);
		p.setExcecutionAmount(10);
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID); // 因为当前CASE是属于字段不合法,所以在Promotion的checkCreate方法中会报错并没有CAll相应的SP
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
	}

	@Test
	public void createTest_CASE10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE10:满减阀值等于满减金额但是满减阀值超过最大值");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionThreshold(10001);
		p.setExcecutionAmount(10001);
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID); // 因为当前CASE是属于字段不合法,所以在Promotion的checkCreate方法中会报错并没有CAll相应的SP
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionThreshold);
	}

	@Test
	public void createTest_CASE11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE11:满减阀值小于满减金额但是满减金额超过最大值");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionThreshold(10);
		p.setExcecutionAmount(10001);
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID); // 因为当前CASE是属于字段不合法,所以在Promotion的checkCreate方法中会报错并没有CAll相应的SP
		Assert.assertEquals(err, Promotion.FIELD_ERROR_excecutionAmount);
	}

	@Test
	public void deleteTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1 ：正常删除促销");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void deleteTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE2 ：有依赖，也可删除");// 需求变动，满减活动可以随时终止
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		RetailTradePromoting retailTradePromoting = BasePromotionTest.createRetailTradePromotingViaMapper();
		// 创建从表数据
		RetailTradePromotingFlow rtpf = BasePromotionTest.DataInput.getRetailTradePromotingFlow();
		rtpf.setRetailTradePromotingID(retailTradePromoting.getID());
		rtpf.setPromotionID(pCreate.getID());
		BasePromotionTest.createRetailTradePromotingFlowViaMapper(rtpf);

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void deleteTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3 ：正常删除促销并且验证促销范围是否删除成功");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setPromotionID(pCreate.getID());
		ps.setCommodityID(cCreate.getID());
		//
		String err = ps.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> psParamsCreate = ps.getCreateParam(BaseBO.INVALID_CASE_ID, ps);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionScopeMapper.create(psParamsCreate); // 删除促销时会一起删除促销范围
		// 删除促销并且一起删除促销范围
		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void deleteTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4 ：重复删除促销");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 删除促销
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		// 重复删除促销
		Map<String, Object> paramsDelete = pCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionMapper.delete(paramsDelete);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_BusinessLogicNotDefined, //
				paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void retrieveNTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE1:查询所有的活动");

		Promotion p = new Promotion();
		p.setStatus(-1);
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:查询 尚未开始 的活动");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(RETRIEVEN_NO_START_PROMOTION);
		p.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:查询 正在进行中 的活动");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(RETRIEVEN_STARTING_PROMOTINO);
		p.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:查询 已经结束 的活动 ");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(RETRIEVEN_ENDED_PROMOTION);
		p.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:查询进行中还有即将要进行的活动 ");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(RETRIEVEN_NO_ENDED_PROMOTION);
		p.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:查询所有已删除的活动 ");

		Promotion p = new Promotion();
		p.setStatus(EnumStatusPromotion.ESP_Deleted.getIndex());
		p.setSubStatusOfStatus(RETRIEVEN_NO_DELETED_PROMOTION);
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:查询所有未删除的活动");

		Promotion p = new Promotion();
		p.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		p.setSubStatusOfStatus(RETRIEVEN_NO_DELETED_PROMOTION);
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8 :输入未定义的 @iInt1");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(UNDEFINE_STATUS_OF_PROMOTION);
		p.setStatus(EnumStatusPromotion.ESP_Active.getIndex());
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE9:创建一个Promotion和RetailTradePromotingFlow，查询并验证F_RetailTradeNo和F_StaffName是否正确");

		// 创建一个promotion
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setStaff(3);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		RetailTradePromoting retailTradePromoting = BasePromotionTest.createRetailTradePromotingViaMapper();

		// 创建一个RetailTradePromotingFlow
		RetailTradePromotingFlow rtpf = new RetailTradePromotingFlow();
		rtpf.setRetailTradePromotingID(retailTradePromoting.getID());
		rtpf.setPromotionID(pCreate.getID());
		rtpf.setProcessFlow("测试数据");
		Map<String, Object> rParams = rtpf.getCreateParam(BaseBO.INVALID_CASE_ID, rtpf);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradePromotingFlow rCreate = (RetailTradePromotingFlow) retailTradePromotingFlowMapper.create(rParams);
		//
		Assert.assertTrue(rCreate != null && EnumErrorCode.values()[Integer.parseInt(rParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		rtpf.setIgnoreIDInComparision(true);
		if (rtpf.compareTo(rCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		System.out.println(rCreate == null ? "null" : rCreate);
		//
		System.out.println("创建对象成功");

		// 验证F_RetailTradeNo是否等于1，F_StaffName是否为店员二号
		p.setSubStatusOfStatus(RETRIEVEN_NO_DELETED_PROMOTION);
		p.setStatus(-1);
		List<BaseModel> pRN = BasePromotionTest.retrieveNPromotionViaMapper(p);
		//
		for (BaseModel bm : pRN) {
			Promotion promotion = (Promotion) bm;
			if (promotion.getID() == pCreate.getID()) {
				Assert.assertTrue(promotion.getStaffName().equals("店员二号"), "返回不正确的操作人员");
				Assert.assertTrue(promotion.getRetailTradeNO() == 1, "返回错误的参与单数");
			}
		}

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void retrieveNTest_CASE10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:按活动名称模糊查询促销活动,@sName为开始");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(UNDEFINE_STATUS_OF_PROMOTION);
		p.setQueryKeyword("开始");
		p.setStatus(-1);
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:按活动名称模糊查询促销活动,查询一个不存在的促销活动");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(RETRIEVEN_NO_DELETED_PROMOTION);
		p.setQueryKeyword("-9999999");
		p.setStatus(-1);
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12：根据最小长度(10)的促销单号进行查询");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(UNDEFINE_STATUS_OF_PROMOTION);
		p.setQueryKeyword("CX20190605");
		p.setStatus(-1);
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:根据小于最小长度(10)的促销单号进行查询");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(RETRIEVEN_NO_DELETED_PROMOTION);
		p.setQueryKeyword("CX2019060");
		p.setStatus(-1);
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14:根据大于最大长度(20)的促销单号进行查询");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(RETRIEVEN_NO_DELETED_PROMOTION);
		p.setQueryKeyword("CX20190605123451234512345");
		p.setStatus(-1);
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE15() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15:根据等于最大长度(20)的促销单号进行查询");

		Promotion p = new Promotion();
		p.setSubStatusOfStatus(RETRIEVEN_NO_DELETED_PROMOTION);
		p.setQueryKeyword("CX201906051234512345");
		p.setStatus(-1);
		BasePromotionTest.retrieveNPromotionViaMapper(p);
	}

	@Test
	public void retrieveNTest_CASE16() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case16:查看默认返回的数据是否降序");

		Promotion p = new Promotion();
		p.setStatus(-1);
		List<BaseModel> bmList = BasePromotionTest.retrieveNPromotionViaMapper(p);
		for (int i = 1; i < bmList.size(); i++) {
			Assert.assertTrue(bmList.get(i - 1).getID() > bmList.get(i).getID(), "数据返回错误（非降序）");
		}
	}

	@Test
	public void retrieve1Test_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 1:查询一个正常的数据");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		BasePromotionTest.retrieve1PromotionViaMapper(pCreate);

		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void retrieve1Test_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 2:查询一个已经删除的数据");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 删除促销
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		//
		BasePromotionTest.retrieve1PromotionViaMapper(pCreate);
	}

	@Test
	public void updateTest_CASE1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 1:正常更新");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(EnumPromotionScope.EPS_AllCommodities.getIndex());
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		Promotion pUpdate = (Promotion) pCreate.clone();
		pUpdate.setName("更新测试A号" + System.currentTimeMillis() % 1000);
		BasePromotionTest.updatePromotionViaMapper(pCreate, pUpdate);

		BasePromotionTest.deletePromotionViaMapper(pUpdate);
	}

	@Test
	public void updateTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 2：用不存在staff进行修改");

		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);

		Promotion pUpdate = (Promotion) pCreate.clone();
		pUpdate.setStaff(Shared.BIG_ID);
		BasePromotionTest.updatePromotionViaMapper(pCreate, pUpdate);
	}

}
