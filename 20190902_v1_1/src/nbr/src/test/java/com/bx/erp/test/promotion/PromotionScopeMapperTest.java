package com.bx.erp.test.promotion;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class PromotionScopeMapperTest extends BaseMapperTest {

	private static final int INVALID_ID = 999999999;

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

		Shared.caseLog("Case1 正常创建");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(pCreate.getID());
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_NoError);
		//
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		// 有依赖，无法删除
		// deleteCommodity(cCreate); // 删除商品时判断是否有依赖 查的是PromotionScope表
		// 而PromotionScope表无删除sp
	}

	// ...是否测试commodityID为null的情况？
	@Test
	public void createTest_CASE2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2: 使用不存在的商品ID进行创建");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setPromotionID(pCreate.getID());
		ps.setCommodityID(Shared.BIG_ID);
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void createTest_CASE3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3 用不存在的promotionID进行创建");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(INVALID_ID);
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		BaseCommodityTest.deleteCommodityViaMapper(cCreate, EnumErrorCode.EC_NoError); // 删除商品时判断是否有依赖 查的是PromotionScope表 而PromotionScope表无删除sp
	}

	@Test
	public void createTest_CASE4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4 促销活动加入服务商品");
		//
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setName("部分商品满多少减多少");
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setScope(1);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setPurchaseFlag(1);
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c.setPurchaseFlag(0);
		c.setShelfLife(0);
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateService);
		//
		PromotionScope ps = new PromotionScope();
		ps.setPromotionID(pCreate.getID());
		ps.setCommodityID(commodity.getID());
		PromotionScope psCreate = BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_BusinessLogicNotDefined);
		Assert.assertTrue(psCreate == null, "促销活动成功加入服务商品！！");
	}

	@Test
	public void createTest_CASE5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5 创建一个促销范围，主表被删除 ");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionDiscount(-1);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		//
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(pCreate.getID());
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		BaseCommodityTest.deleteCommodityViaMapper(cCreate, EnumErrorCode.EC_NoError); // 删除商品时判断是否有依赖 查的是PromotionScope表 而PromotionScope表无删除sp
	}

	@Test
	public void createTest_CASE6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6 指定商品范围的促销活动须保存原貌 ");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		cCreate.setOperatorStaffID(c.getOperatorStaffID());
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionDiscount(-1);
		p.setScope(1);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(pCreate.getID());
		PromotionScope pScope = BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_NoError);
		// 修改指定促销商品的名称
		cCreate.setName("修改商品" + UUID.randomUUID().toString().substring(1, 6));
		Map<String, Object> params = cCreate.getUpdateParam(BaseBO.INVALID_CASE_ID, cCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity commodity = (Commodity) commodityMapper.update(params);
		//
		Map<String, Object> params2 = pScope.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pScope);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = (List<BaseModel>) promotionScopeMapper.retrieveN(params2);
		PromotionScope promotionScope = (PromotionScope) list.get(0);
		// 结果验证
		Assert.assertTrue(promotionScope.getCommodityName().equals(pScope.getCommodityName()) //
				&& promotionScope.getCommodityName().equals(c.getName()) //
				&& !promotionScope.getCommodityName().equals(commodity.getName()), "CASE6:测试失败!指定商品范围的促销活动须保存原貌");
		//
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		// 有依赖，无法删除
		// deleteCommodity(commodity); // 删除商品时判断是否有依赖 查的是PromotionScope表
		// 而PromotionScope表无删除sp
	}

	@Test
	public void createTest_CASE7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7 指定商品范围的商品类型为多包装");
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(pCreate.getID());
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		BaseCommodityTest.deleteCommodityViaMapper(cCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest_CASE8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8 指定商品范围的商品类型为组合");
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateComposition);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(pCreate.getID());
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		BaseCommodityTest.deleteCommodityViaMapper(cCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest_CASE10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10 指定商品的促销范围中添加重复的商品");
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(pCreate.getID());
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_NoError);
		// 再次使用该商品创建促销范围
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_Duplicated);
		//
		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void retrieveNTest() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("retrieveNTest");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setName("部分商品满多少减多少");
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setScope(1);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setPromotionID(pCreate.getID());
		ps.setCommodityID(commodity.getID());
		PromotionScope psCreate = BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_NoError);
		// 查询创建的促销范围
		Map<String, Object> psParamsRN = ps.getRetrieveNParam(BaseBO.INVALID_CASE_ID, psCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> psRN = promotionScopeMapper.retrieveN(psParamsRN);
		//
		if (psRN.size() > 0) {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(psParamsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, psParamsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			String err;
			for (BaseModel bm : psRN) {
				err = ((PromotionScope) bm).checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(err, "");
				Assert.assertTrue(((PromotionScope) bm).getCommodityName().equals(commodity.getName()), "查询的从表的字段F_CommodityName信息不正确");
			}
			//
			System.out.println("查询成功： " + psRN);
		} else {
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(psParamsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, psParamsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			//
			System.out.println("查询成功： " + psRN);
		}
		//
		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}
}
