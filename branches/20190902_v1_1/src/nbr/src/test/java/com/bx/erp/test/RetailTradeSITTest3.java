package com.bx.erp.test;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.Provider;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.Warehousing.EnumStatusWarehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.util.DataSourceContextHolder;

public class RetailTradeSITTest3 extends BaseActionTest {
	private static final int COMMODITY_STARTNO = 10;

	public static class DataInput {
		private static Provider providerInput = null;
		private static ProviderCommodity providerCommodityInput = null;
		private static Warehousing warehousingInput = null;

		protected static final Warehousing getWarehousing() throws CloneNotSupportedException, InterruptedException {
			warehousingInput = new Warehousing();
			warehousingInput.setWarehouseID(1);
			warehousingInput.setStaffID(1);
			warehousingInput.setPurchasingOrderID(0);
			warehousingInput.setProviderID(1);
			return (Warehousing) warehousingInput.clone();
		}

		protected static final Provider getProvider() throws CloneNotSupportedException, InterruptedException {
			providerInput = new Provider();
			providerInput.setName(Shared.getLongestProviderName("淘宝"));
			Thread.sleep(1);
			providerInput.setDistrictID(1);
			providerInput.setAddress("广州市天河区二十八中学");
			providerInput.setContactName("zda");
			providerInput.setMobile(Shared.getValidStaffPhone());
			return (Provider) providerInput.clone();
		}

		protected static final ProviderCommodity getProviderCommodity() throws CloneNotSupportedException {
			if (providerCommodityInput == null) {
				providerCommodityInput = new ProviderCommodity();
			}
			return (ProviderCommodity) providerCommodityInput.clone();
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
	public void runRetailTradeProcess() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ 创建供应商A ------------------------");

		Provider p = DataInput.getProvider();
		Map<String, Object> providerParams = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Provider providerCreate = (Provider) providerMapper.create(providerParams);
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(providerCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(providerCreate != null && EnumErrorCode.values()[Integer.parseInt(providerParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		System.out.println(providerCreate == null ? "providerCreate == null" : providerCreate);

		System.out.println("------------------------ 创建期初商品A ----------------------");
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		commTemplate.setnOStart(COMMODITY_STARTNO);
		commTemplate.setNO(COMMODITY_STARTNO);
		commTemplate.setPurchasingPriceStart(1);

		Map<String, Object> commParams = commTemplate.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(commParams);
		//
		Assert.assertTrue(bmList != null && EnumErrorCode.values()[Integer.parseInt(commParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		Barcodes barcodesCreate = (Barcodes) bmList.get(1).get(0);
		Warehousing warehousingCreate = (Warehousing) bmList.get(3).get(0);
		WarehousingCommodity warehousingCommodityCreate = (WarehousingCommodity) bmList.get(4).get(0);
		Commodity commCreate = (Commodity) bmList.get(5).get(0);

		commTemplate.setIgnoreIDInComparision(true);
		if (commTemplate.compareTo(commCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(barcodesCreate != null && warehousingCreate != null && warehousingCommodityCreate != null && commCreate != null
				&& EnumErrorCode.values()[Integer.parseInt(commParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		Assert.assertTrue(barcodes != null && barcodes.getCommodityID() == commCreate.getID() && barcodes.getBarcode().equals(commTemplate.getBarcodes()));

		System.out.println("创建商品成功");

		System.out.println("\n--------------------- 创建供应商商品 -------------------");

		ProviderCommodity providerCommodity = new ProviderCommodity();
		providerCommodity.setProviderID(providerCreate.getID());
		providerCommodity.setCommodityID(commCreate.getID());

		Map<String, Object> providerCommodityParams = providerCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, providerCommodity);
		ProviderCommodity providerCommodityCreate = (ProviderCommodity) providerCommodityMapper.create(providerCommodityParams);
		providerCommodity.setIgnoreIDInComparision(true);
		if (providerCommodity.compareTo(providerCommodityCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Assert.assertTrue(providerCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(providerCommodityParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建供应商商品表成功");

		System.out.println("\n------------------------ 创建零售单A ------------------------");

		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		rt.setAmount(15);
		RetailTrade retailTradeCareateA = BaseRetailTradeTest.createRetailTrade(rt);	

		System.out.println("------------------------ 创建零售单商品A ----------------------");
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setTradeID(retailTradeCareateA.getID());
		rtc.setCommodityID(commCreate.getID());
		rtc.setBarcodeID(barcodesCreate.getID());
		rtc.setNO(5);
		rtc.setPriceOriginal(15);
		rtc.setOperatorStaffID(4);

		Map<String, Object> retailTreadeCommodityParamsA = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);

		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(retailTreadeCommodityParamsA); // ...
		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retailTreadeCommodityParamsA.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(retailTradeCommodityCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retailTreadeCommodityParamsA.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("创建零售单商品A成功！");

		// 验证商品库存
		Commodity commodityR1 = BaseCommodityTest.retrieve1ExCommodity(commCreate, EnumErrorCode.EC_NoError);
		Assert.assertTrue(commodityR1 != null, "查询对象失败！");
		Assert.assertTrue(commodityR1.getNO() == 5);

		// 验证入库单商品A可售数量
		Map<String, Object> warehousingCommodityRNParamsA = warehousingCommodityCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingCommodityCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> warehousingCommodityListA = warehousingCommodityMapper.retrieveN(warehousingCommodityRNParamsA);

		Assert.assertTrue(warehousingCommodityListA.size() > 0 && EnumErrorCode.values()[Integer.parseInt(warehousingCommodityRNParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		for (BaseModel bm : warehousingCommodityListA) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
			if (warehousingCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(warehousingCommodity.getNoSalable() == 5);
			}
		}

		System.out.println("------------------------ 创建入库单B ----------------------");
		Warehousing warehousingB = DataInput.getWarehousing();
		Map<String, Object> warehousingParamsB = warehousingB.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingB);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehousing warehousingCreateB = (Warehousing) warehousingMapper.create(warehousingParamsB);
		warehousingB.setIgnoreIDInComparision(true);
		if (warehousingB.compareTo(warehousingCreateB) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(warehousingCreateB != null && EnumErrorCode.values()[Integer.parseInt(warehousingParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		System.out.println("------------------------ 创建入库单商品B ----------------------");
		WarehousingCommodity warehousingCommodityB = new WarehousingCommodity();
		warehousingCommodityB = new WarehousingCommodity();
		warehousingCommodityB.setWarehousingID(warehousingCreateB.getID());
		warehousingCommodityB.setCommodityID(commCreate.getID());
		warehousingCommodityB.setCommodityName(commCreate.getName());
		warehousingCommodityB.setPackageUnitID(commCreate.getPackageUnitID());
		warehousingCommodityB.setNO(20);
		warehousingCommodityB.setBarcodeID(barcodesCreate.getID());
		warehousingCommodityB.setPrice(3);
		warehousingCommodityB.setAmount(60);
		warehousingCommodityB.setShelfLife(36);

		Map<String, Object> warehousingCommodityParamsB = warehousingCommodityB.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingCommodityB);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity warehousingCommodityCreateB = (WarehousingCommodity) warehousingCommodityMapper.create(warehousingCommodityParamsB);
		warehousingCommodityB.setIgnoreIDInComparision(true);
		// 判断可售数量是否正确改变
		Assert.assertTrue(warehousingCommodityB.getNoSalable() + warehousingCommodityB.getNO() == warehousingCommodityCreateB.getNoSalable(), "可售数量错误");
		warehousingCommodityB.setNoSalable(warehousingCommodityB.getNO());
		if (warehousingCommodityB.compareTo(warehousingCommodityCreateB) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(warehousingCommodityCreateB != null && EnumErrorCode.values()[Integer.parseInt(warehousingCommodityParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		System.out.println("------------------------审核入库单B ----------------------");
		warehousingCreateB.setApproverID(3);
		Map<String, Object> warehousingApproveParamsB = warehousingCreateB.getUpdateParam(BaseBO.CASE_ApproveWarhousing, warehousingCreateB);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> warebmList = warehousingMapper.approveEx(warehousingApproveParamsB);
		Warehousing warehousingApproveB = (Warehousing) warebmList.get(0).get(0);
		Assert.assertTrue(warehousingApproveB != null && warehousingApproveB.getStatus() == EnumStatusWarehousing.ESW_Approved.getIndex()
				&& EnumErrorCode.values()[Integer.parseInt(warehousingParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		// 验证商品库存是否为25
		commodityR1 = BaseCommodityTest.retrieve1ExCommodity(commCreate, EnumErrorCode.EC_NoError);
		Assert.assertTrue(commodityR1 != null, "查询对象失败");
		Assert.assertTrue(commodityR1.getNO() == 25);

		System.out.println("\n------------------------ 创建零售单B ------------------------");
		RetailTrade retailTradeB = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeB.setAmount(45d);
		retailTradeB.setAmountCash(45d);
		RetailTrade retailTradeCareateB = BaseRetailTradeTest.createRetailTrade(retailTradeB);

		System.out.println("------------------------ 创建零售单商品B ----------------------");
		RetailTradeCommodity rtcB = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtcB.setTradeID(retailTradeCareateB.getID());
		rtcB.setCommodityID(commCreate.getID());
		rtcB.setBarcodeID(barcodesCreate.getID());
		rtcB.setNO(15);
		rtcB.setPriceOriginal(45);
		rtcB.setOperatorStaffID(4);

		Map<String, Object> retailTreadeCommodityParamsB = rtcB.getCreateParam(BaseBO.INVALID_CASE_ID, rtcB);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreateB = (RetailTradeCommodity) retailTradeCommodityMapper.create(retailTreadeCommodityParamsB); // ...
		rtcB.setIgnoreIDInComparision(true);
		if (rtcB.compareTo(retailTradeCommodityCreateB) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityCreateB != null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建零售单商品B成功！");

		// 验证商品库存是否为10
		commodityR1 = BaseCommodityTest.retrieve1ExCommodity(commCreate, EnumErrorCode.EC_NoError);

		Assert.assertTrue(commodityR1 != null, "查询对象失败");
		Assert.assertTrue(commodityR1.getNO() == 10);

		// 验证入库单商品A可售数量是否为0
		warehousingCommodityRNParamsA = warehousingCommodityCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingCommodityCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityListA = warehousingCommodityMapper.retrieveN(warehousingCommodityRNParamsA);

		Assert.assertTrue(warehousingCommodityListA.size() > 0 && EnumErrorCode.values()[Integer.parseInt(warehousingCommodityRNParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		for (BaseModel bm : warehousingCommodityListA) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
			if (warehousingCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(warehousingCommodity.getNoSalable() == 0);
			}
		}

		// 验证入库单商品B可售数量是否为10
		Map<String, Object> warehousingCommodityRNParamsB = warehousingCommodityCreateB.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingCommodityCreateB);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> warehousingCommodityListB = warehousingCommodityMapper.retrieveN(warehousingCommodityRNParamsB);

		for (BaseModel bm : warehousingCommodityListB) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
			if (warehousingCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(warehousingCommodity.getNoSalable() == 10);
			}
		}

		System.out.println("\n------------------------ 创建零售单C ------------------------");

		RetailTrade retailTradeC = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeC.setAmount(45d);
		retailTradeC.setAmountCash(45d);
		RetailTrade retailTradeCareateC = BaseRetailTradeTest.createRetailTrade(retailTradeC);

		System.out.println("------------------------ 创建零售单商品C ----------------------");
		RetailTradeCommodity rtcC = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtcC.setTradeID(retailTradeCareateC.getID());
		rtcC.setCommodityID(commCreate.getID());
		rtcC.setBarcodeID(barcodesCreate.getID());
		rtcC.setNO(15);
		rtcC.setPriceOriginal(45);
		rtcC.setOperatorStaffID(4);

		Map<String, Object> retailTreadeCommodityParamsC = rtcC.getCreateParam(BaseBO.INVALID_CASE_ID, rtcC);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreateC = (RetailTradeCommodity) retailTradeCommodityMapper.create(retailTreadeCommodityParamsC); // ...
		rtcC.setIgnoreIDInComparision(true);
		if (rtcC.compareTo(retailTradeCommodityCreateC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityCreateC != null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityParamsC.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建零售单商品C成功！");

		// 验证商品库存是否为-5,当值入库ID是否为入库单B
		commodityR1 = BaseCommodityTest.retrieve1ExCommodity(commCreate, EnumErrorCode.EC_NoError);
		Assert.assertTrue(commodityR1 != null && commodityR1.getCurrentWarehousingID() == warehousingCreateB.getID() && commodityR1.getNO() == -5, "查询对象失败");

		// 验证入库单商品B可售数量是否为-5
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityListB = warehousingCommodityMapper.retrieveN(warehousingCommodityRNParamsB);

		for (BaseModel bm : warehousingCommodityListB) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
			if (warehousingCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(warehousingCommodity.getNoSalable() == -5);
			}
		}

		System.out.println("\n------------------------ 创建退货单A ------------------------");

		RetailTrade retailTradeReturnA = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeReturnA.setAmount(45);
		retailTradeReturnA.setSourceID(retailTradeCareateC.getID());
		RetailTrade retailTradeReturnCareateA = BaseRetailTradeTest.createRetailTrade(retailTradeReturnA);

		System.out.println("------------------------ 创建退货单商品A ----------------------");
		RetailTradeCommodity rtcReturnA = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtcReturnA.setTradeID(retailTradeReturnCareateA.getID());
		rtcReturnA.setCommodityID(commCreate.getID());
		rtcReturnA.setBarcodeID(barcodesCreate.getID());
		rtcReturnA.setNO(15);
		rtcReturnA.setOperatorStaffID(4);

		Map<String, Object> retailTreadeCommodityReturnParamsA = rtcReturnA.getCreateParam(BaseBO.INVALID_CASE_ID, rtcReturnA);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityReturnCreateA = (RetailTradeCommodity) retailTradeCommodityMapper.create(retailTreadeCommodityReturnParamsA); // ...
		rtcReturnA.setIgnoreIDInComparision(true);
		if (rtcReturnA.compareTo(retailTradeCommodityReturnCreateA) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityReturnCreateA != null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityReturnParamsA.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建退货单商品A成功！");

		// 验证商品库存是否为10
		commodityR1 = BaseCommodityTest.retrieve1ExCommodity(commCreate, EnumErrorCode.EC_NoError);
		Assert.assertTrue(commodityR1 != null && commodityR1.getNO() == 10, "查询对象失败");

		// 验证入库单商品B可售数量是否为10
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityListB = warehousingCommodityMapper.retrieveN(warehousingCommodityRNParamsB);

		for (BaseModel bm : warehousingCommodityListB) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
			if (warehousingCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(warehousingCommodity.getNoSalable() == 10);
			}
		}

		// 验证入库单商品C的可退货数量是否为0
		Map<String, Object> retailTradeCommodityRNParams = retailTradeCommodityCreateC.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeCommodityCreateC);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retailTradeCommodityList = retailTradeCommodityMapper.retrieveN(retailTradeCommodityRNParams);

		Assert.assertTrue(retailTradeCommodityList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retailTradeCommodityRNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		for (BaseModel bm : retailTradeCommodityList) {
			RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) bm;
			if (retailTradeCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(retailTradeCommodity.getNOCanReturn() == 0);
			}
		}

		System.out.println("\n------------------------ 创建退货单B ------------------------");

		RetailTrade retailTradeReturnB = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeReturnB.setAmount(45);
		retailTradeReturnB.setSourceID(retailTradeCareateB.getID());
		RetailTrade retailTradeReturnCareateB = BaseRetailTradeTest.createRetailTrade(retailTradeReturnB);

		System.out.println("------------------------ 创建退货单商品B ----------------------");
		RetailTradeCommodity rtcReturnB = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtcReturnB.setTradeID(retailTradeReturnCareateB.getID());
		rtcReturnB.setCommodityID(commCreate.getID());
		rtcReturnB.setBarcodeID(barcodesCreate.getID());
		rtcReturnB.setNO(15);
		rtcReturnB.setOperatorStaffID(4);
		Map<String, Object> retailTreadeCommodityReturnParamsB = rtcReturnB.getCreateParam(BaseBO.INVALID_CASE_ID, rtcReturnB);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityReturnCreateB = (RetailTradeCommodity) retailTradeCommodityMapper.create(retailTreadeCommodityReturnParamsB); // ...
		rtcReturnB.setIgnoreIDInComparision(true);
		if (rtcReturnB.compareTo(retailTradeCommodityReturnCreateB) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityReturnCreateB != null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityReturnParamsB.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建退货单商品B成功！");

		// 验证商品库存是否为25 商品的当值入库ID是否为入库单A
		commodityR1 = BaseCommodityTest.retrieve1ExCommodity(commCreate, EnumErrorCode.EC_NoError);
		Assert.assertTrue(commodityR1 != null && commodityR1.getNO() == 25 && commodityR1.getCurrentWarehousingID() == warehousingCreate.getID(), "查询对象失败");

		// 验证入库单商品A可售数量是否为5
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityListA = warehousingCommodityMapper.retrieveN(warehousingCommodityRNParamsA);

		for (BaseModel bm : warehousingCommodityListA) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
			if (warehousingCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(warehousingCommodity.getNoSalable() == 5);
			}
		}

		// 验证入库单商品B可售数量是否为20
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityListB = warehousingCommodityMapper.retrieveN(warehousingCommodityRNParamsB);

		for (BaseModel bm : warehousingCommodityListB) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
			if (warehousingCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(warehousingCommodity.getNoSalable() == 20);
			}
		}

		// 验证入库单商品C的可退货数量是否为0
		retailTradeCommodityRNParams = retailTradeCommodityCreateB.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeCommodityCreateB);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCommodityList = retailTradeCommodityMapper.retrieveN(retailTradeCommodityRNParams);

		Assert.assertTrue(retailTradeCommodityList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retailTradeCommodityRNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		for (BaseModel bm : retailTradeCommodityList) {
			RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) bm;
			if (retailTradeCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(retailTradeCommodity.getNOCanReturn() == 0);
			}
		}

		System.out.println("\n------------------------ 创建退货单C ------------------------");

		RetailTrade retailTradeReturnC = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeReturnC.setAmount(15);
		retailTradeReturnC.setSourceID(retailTradeCareateA.getID());
		RetailTrade retailTradeReturnCareateC = BaseRetailTradeTest.createRetailTrade(retailTradeReturnC);

		System.out.println("------------------------ 创建退货单商品C ----------------------");
		RetailTradeCommodity rtcReturnC = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtcReturnC.setTradeID(retailTradeReturnCareateC.getID());
		rtcReturnC.setCommodityID(commCreate.getID());
		rtcReturnC.setBarcodeID(barcodesCreate.getID());
		rtcReturnC.setNO(5);
		rtcReturnC.setOperatorStaffID(4);
		Map<String, Object> retailTreadeCommodityReturnParamsC = rtcReturnC.getCreateParam(BaseBO.INVALID_CASE_ID, rtcReturnC);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityReturnCreateC = (RetailTradeCommodity) retailTradeCommodityMapper.create(retailTreadeCommodityReturnParamsC); // ...
		rtcReturnC.setIgnoreIDInComparision(true);
		if (rtcReturnC.compareTo(retailTradeCommodityReturnCreateC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityReturnCreateC != null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityReturnParamsC.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建退货单商品C成功！");

		// 验证商品库存是否为30， 商品的当值入库ID是否为入库单A
		commodityR1 = BaseCommodityTest.retrieve1ExCommodity(commCreate, EnumErrorCode.EC_NoError);
		Assert.assertTrue(commodityR1 != null && commodityR1.getNO() == 30, "查询对象失败");

		// 验证入库单商品A可售数量是否为10
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityListA = warehousingCommodityMapper.retrieveN(warehousingCommodityRNParamsA);

		for (BaseModel bm : warehousingCommodityListA) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
			if (warehousingCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(warehousingCommodity.getNoSalable() == 10);
			}
		}

		// 验证入库单商品C的可退货数量是否为0
		retailTradeCommodityRNParams = retailTradeCommodityCreateC.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeCommodityCreateC);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCommodityList = retailTradeCommodityMapper.retrieveN(retailTradeCommodityRNParams);

		Assert.assertTrue(retailTradeCommodityList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retailTradeCommodityRNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		for (BaseModel bm : retailTradeCommodityList) {
			RetailTradeCommodity retailTradeCommodity = (RetailTradeCommodity) bm;
			if (retailTradeCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(retailTradeCommodity.getNOCanReturn() == 0);
			}
		}

		System.out.println("------------------------ 创建零售单D ----------------------");
		RetailTrade rtD = BaseRetailTradeTest.DataInput.getRetailTrade();
		rtD.setAmount(15);
		RetailTrade retailTradeCareateD = BaseRetailTradeTest.createRetailTrade(rtD);

		System.out.println("------------------------ 创建零售单商品D ----------------------");
		RetailTradeCommodity rtcD = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtcD.setTradeID(retailTradeCareateD.getID());
		rtcD.setCommodityID(commCreate.getID());
		rtcD.setBarcodeID(20);
		rtcD.setNO(5);
		rtcD.setPriceOriginal(15);
		rtcD.setOperatorStaffID(4);

		Map<String, Object> retailTreadeCommodityParamsD = rtcD.getCreateParam(BaseBO.INVALID_CASE_ID, rtcD);

		RetailTradeCommodity retailTradeCommodityCreateD = (RetailTradeCommodity) retailTradeCommodityMapper.create(retailTreadeCommodityParamsD); // ...
		Assert.assertTrue(retailTradeCommodityCreateD != null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityParamsD.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retailTreadeCommodityParamsD.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		rtcD.setIgnoreIDInComparision(true);
		if (rtcD.compareTo(retailTradeCommodityCreateD) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		Assert.assertTrue(retailTradeCommodityCreateD != null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityParamsD.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retailTreadeCommodityParamsD.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("创建零售单商品D成功！");

		// 验证商品库存是否为25
		Commodity commodityR1D = BaseCommodityTest.retrieve1ExCommodity(commCreate, EnumErrorCode.EC_NoError);

		Assert.assertTrue(commodityR1D != null, "查询对象失败");
		Assert.assertTrue(commodityR1D.getNO() == 25);

		// 验证入库单商品D可售数量
		Map<String, Object> warehousingCommodityRNParamsD = warehousingCommodityCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingCommodityCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> warehousingCommodityListD = warehousingCommodityMapper.retrieveN(warehousingCommodityRNParamsD);

		Assert.assertTrue(warehousingCommodityListD.size() > 0 && EnumErrorCode.values()[Integer.parseInt(warehousingCommodityRNParamsD.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		for (BaseModel bm : warehousingCommodityListD) {
			WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bm;
			if (warehousingCommodity.getCommodityID() == commCreate.getID()) {
				Assert.assertTrue(warehousingCommodity.getNoSalable() == 5);
			}
		}

		System.out.println("\n------------------------ 创建退货单D,退货商品 ------------------------");
		RetailTrade retailTradeReturnD = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTradeReturnD.setAmount(15);
		retailTradeReturnD.setSourceID(retailTradeCareateD.getID());
		RetailTrade retailTradeReturnCareateD = BaseRetailTradeTest.createRetailTrade(retailTradeReturnD);

		System.out.println("------------------------ 创建退货单商品D 超过可退货数量，返回7----------------------");
		RetailTradeCommodity rtcReturnD = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtcReturnD.setTradeID(retailTradeReturnCareateD.getID());
		rtcReturnD.setCommodityID(commCreate.getID());
		rtcReturnD.setBarcodeID(barcodesCreate.getID());
		rtcReturnD.setNO(6);
		rtcReturnD.setOperatorStaffID(4);

		Map<String, Object> retailTreadeCommodityReturnParamsD = rtcReturnD.getCreateParam(BaseBO.INVALID_CASE_ID, rtcReturnD);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityReturnCreateD = (RetailTradeCommodity) retailTradeCommodityMapper.create(retailTreadeCommodityReturnParamsD);

		Assert.assertTrue(
				retailTradeCommodityReturnCreateD == null && EnumErrorCode.values()[Integer.parseInt(retailTreadeCommodityReturnParamsD.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				"创建对象成功");
	}
}
