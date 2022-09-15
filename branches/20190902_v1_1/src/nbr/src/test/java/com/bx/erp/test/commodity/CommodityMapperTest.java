package com.bx.erp.test.commodity;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.cache.CacheManager;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.BaseModel.EnumBoolean;
import com.bx.erp.model.CacheType.EnumCacheType;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.Coupon;
import com.bx.erp.model.CouponScope;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.Role.EnumTypeRole;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.model.Shop;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.model.message.Message.EnumMessageCategory;
import com.bx.erp.model.purchasing.ProviderCommodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseCouponScopeTest;
import com.bx.erp.test.BaseCouponTest;
import com.bx.erp.test.BaseInventorySheetTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BasePromotionTest;
import com.bx.erp.test.BasePurchasingOrderTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.DatetimeUtil;
import com.bx.erp.util.GeneralUtil;

@WebAppConfiguration
public class CommodityMapperTest extends BaseMapperTest {

	/** 查询删除的这条数据 */
	public static final int RETRIEVE1_DELETED_COMMODITY = 0;

	/** 检查商品名字唯一字段是否已存在 */
	public static final int CASE_CHECK_UNIQUE_NAME = 1;

	public static class DataInput {


		private static PurchasingOrderCommodity orderCommInput = null;
		private static Random r = new Random();

		protected static final PurchasingOrderCommodity getPurchasingOrderCommodity() {
			if (orderCommInput == null) {
				orderCommInput = new PurchasingOrderCommodity();
				orderCommInput.setCommodityID(r.nextInt(10) + 1);
				orderCommInput.setPurchasingOrderID(2);
				orderCommInput.setCommodityNO(Math.abs(new Random().nextInt(300)));
				orderCommInput.setPriceSuggestion(1);
				orderCommInput.setBarcodeID(1);
			}
			return orderCommInput;
		}

		private static InventoryCommodity inventoryCommodityInput = null;

		protected static final InventoryCommodity getInventoryCommodity() throws CloneNotSupportedException, InterruptedException {

			inventoryCommodityInput = new InventoryCommodity();
			inventoryCommodityInput.setInventorySheetID(1);
			inventoryCommodityInput.setCommodityID(new Random().nextInt(3) + 1);
			inventoryCommodityInput.setBarcodeID(1);

			return (InventoryCommodity) inventoryCommodityInput.clone();
		}

		private static Barcodes barcodesInput = null;

		protected static final Barcodes getBarcodes() throws CloneNotSupportedException {
			Random ran = new Random();
			//
			barcodesInput = new Barcodes();
			barcodesInput.setCommodityID(ran.nextInt(15) + 1);
			barcodesInput.setBarcode("354" + System.currentTimeMillis() % 1000000);
			barcodesInput.setOperatorStaffID(STAFF_ID3);
			barcodesInput.setShopID(2);
			//
			return (Barcodes) barcodesInput.clone();
		}

		private static ReturnCommoditySheetCommodity rcscInput = null;

		protected static final ReturnCommoditySheetCommodity getReturnCommoditySheetCommodity() throws CloneNotSupportedException, InterruptedException {
			rcscInput = new ReturnCommoditySheetCommodity();
			rcscInput.setReturnCommoditySheetID(1);
			rcscInput.setCommodityID(5);
			rcscInput.setBarcodeID(7);
			rcscInput.setNO(50);
			rcscInput.setSpecification("箱");
			rcscInput.setPurchasingPrice(5.5d);

			return (ReturnCommoditySheetCommodity) rcscInput.clone();
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
	public void createSimpleTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case1: 添加名称不重复的商品");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		System.out.println("createTest1创建出来的商品ID为：" + commCreated.getID());
		Assert.assertTrue(commCreated.getListSlave2() != null && commCreated.getListSlave2().size() > 0, "没有创建或没有返回商品门店信息");
		// 删除创建的测试对象
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void createSimpleTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2:添加名字重复的商品");
		//
		final String commodityName = "薯片" + UUID.randomUUID().toString().substring(1, 8);
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName(commodityName);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		System.out.println("createTest2创建出来的商品ID为：" + commCreated.getID());
		//
		// 重复创建
		String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createSimpleTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case3:名称不重复，not null字段传入一个null");
		//
		Commodity commTemplate311 = BaseCommodityTest.DataInput.getCommodity();
		commTemplate311.setSpecification(null);
		// //
		Map<String, Object> params3 = commTemplate311.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate311);
		//
		params3.put(Commodity.field.getFIELD_NAME_specification(), commTemplate311.getSpecification());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params3);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_OtherError, params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createSimpleTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case4: 添加期初值商品");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setnOStart(100);
		comm.setPurchasingPriceStart(100);
		comm.setStartValueRemark("aaaaaaaaaaaaaa");
		//
		String error = comm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Map<String, Object> paramForComm = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(paramForComm);
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramForComm.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramForComm.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		comm.setIgnoreIDInComparision(true);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		int staffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodes = ((Barcodes) bmList.get(1).get(0)).getBarcode();
		commCreated.setBarcodes(barcodes);
		commCreated.setnOStart(comm.getnOStart());
		commCreated.setPurchasingPriceStart(comm.getPurchasingPriceStart());
		String commodityError = commCreated.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(commodityError, "");
		commCreated.setOperatorStaffID(staffID);
		commCreated.setBarcodes(null);
		//
		// 由于目前商品都需要有一个供应商，所以创建一个供应商商品
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		Barcodes barcodes1 = (Barcodes) bmList.get(1).get(0);
		Assert.assertTrue(barcodes1 != null && barcodes1.getCommodityID() == commCreated.getID() && barcodes1.getBarcode().equals(comm.getBarcodes()));
		//
		barcodes1.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodesError = barcodes1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(barcodesError, "");
		//
		Warehousing warehousing = (Warehousing) bmList.get(3).get(0);
		Assert.assertTrue(warehousing != null);
		// //暂无作用
		// String warehousingError = warehousing.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(warehousingError, "");
		//
		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bmList.get(4).get(0);
		Assert.assertTrue(warehousingCommodity != null && warehousingCommodity.getWarehousingID() == warehousing.getID() && warehousingCommodity.getCommodityID() == commCreated.getID());
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "期初数量不正确");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "期初采购价不正确");
		// //暂无作用
		// String warehousingCommodityError
		// =warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(warehousingCommodityError, "");
		//
		// 期初商品有记录，删不掉
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void createSimpleTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case5:创建商品时,staffID不存在，返回4");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setOperatorStaffID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Hack, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createSimpleTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case6: 创建普通商品时，品牌ID不存在，返回7");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setBrandID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.CASE_Commodity_CreateSingle, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("品牌不存在".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createSimpleTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case7: 创建普通商品时，类别ID不存在，返回7");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setCategoryID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.CASE_Commodity_CreateSingle, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("分类不存在".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createSimpleTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case8: 创建普通商品时，包装单位ID不存在，返回7");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setPackageUnitID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.CASE_Commodity_CreateSingle, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("包装单位不存在".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void createSimpleTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case9: 创建的商品与预淘汰商品同名");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("康师傅牛肉面");// 预淘汰商品无法直接创建，故直接拿数据库中的预淘汰商品测试
		//
		String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void createSimpleTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case10: 创建的商品与删除商品同名");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);

		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// 商品与删除商品同名的创建
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		commodity2.setName(commodity.getName());
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateSingle);
		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createSimpleTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:创建期初商品后，入库单的单号不为空");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setnOStart(100);
		comm.setNO(100);
		comm.setPurchasingPriceStart(100);
		comm.setLatestPricePurchase(comm.getPurchasingPriceStart()); // 期初商品latestPricePurchase要等于purchasingPriceStart
		comm.setStartValueRemark("aaaaaaaaaaaaaa");
		//
		String error = comm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Map<String, Object> paramForComm = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(paramForComm);
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramForComm.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramForComm.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Commodity commCreated = null;
		if (comm.getnOStart() > Commodity.NO_START_Default && comm.getPurchasingPriceStart() > Commodity.PURCHASING_PRICE_START_Default) {
			commCreated = (Commodity) bmList.get(5).get(0);
		} else {
			commCreated = (Commodity) bmList.get(0).get(0);
		}
		comm.setIgnoreIDInComparision(true);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		int staffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodes = ((Barcodes) bmList.get(1).get(0)).getBarcode();
		commCreated.setBarcodes(barcodes);
		commCreated.setnOStart(comm.getnOStart());
		commCreated.setPurchasingPriceStart(comm.getPurchasingPriceStart());
		String commodityError = commCreated.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(commodityError, "");
		commCreated.setOperatorStaffID(staffID);
		// 由于目前商品都需要有一个供应商，所以创建一个供应商商品
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		Barcodes barcodes1 = (Barcodes) bmList.get(1).get(0);
		Assert.assertTrue(barcodes1 != null && barcodes1.getCommodityID() == commCreated.getID() && barcodes1.getBarcode().equals(comm.getBarcodes()));
		//
		barcodes1.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodesError = barcodes1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(barcodesError, "");
		//
		Warehousing warehousing = (Warehousing) bmList.get(3).get(0);
		Assert.assertTrue(warehousing != null && !"".equals(warehousing.getSn()));
		//
		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bmList.get(4).get(0);
		Assert.assertTrue(warehousingCommodity != null && warehousingCommodity.getWarehousingID() == warehousing.getID() && warehousingCommodity.getCommodityID() == commCreated.getID());
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "期初数量不正确");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "期初采购价不正确");
		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void createSimpleTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case12:创建单品，RefCommodityID != 0，创建失败");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setRefCommodityID(1);
		//
		String error = comm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_refCommodity);
	}

	@Test
	public void createSimpleTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case13:创建单品，RefCommodityMultiple != 0，创建失败");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setRefCommodityMultiple(1);
		//
		String error = comm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_refCommodity);
	}
	
	@Test
	public void createSimpleTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case14: 创建商品，商品名称可以支持*“”、$#/符号");
		//
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		commodityGet.setName("*“”、$#/" + commodityGet.getName());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		System.out.println("createTest1创建出来的商品ID为：" + commCreated.getID());
		// 删除创建的测试对象
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createServiceTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常创建服务商品");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		BaseCommodityTest.deleteCommodityViaMapper(BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateService), EnumErrorCode.EC_NoError);
	}

	@Test
	public void createServiceTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2:添加名字重复的服务商品");
		//
		final String commodityName = "薯片" + UUID.randomUUID().toString().substring(1, 8);
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		commodity.setName(commodityName);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateService);
		//
		// 重复创建
		String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createServiceEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createServiceTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case3:名称不重复，not null字段传入一个null");
		//
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		commTemplate.setSpecification(null);
		// //
		Map<String, Object> params3 = commTemplate.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate);
		//
		params3.put(Commodity.field.getFIELD_NAME_specification(), commTemplate.getSpecification());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createServiceEx(params3);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_OtherError, params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createServiceTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case4:创建服务商品时,staffID不存在，返回4");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		comm.setOperatorStaffID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createServiceEx(params);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Hack, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createServiceTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case5: 创建服务商品时，品牌ID不存在，返回7");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		comm.setBrandID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.CASE_Commodity_CreateService, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createServiceEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("品牌不存在".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createServiceTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case6: 创建服务商品时，类别ID不存在，返回7");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		comm.setCategoryID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.CASE_Commodity_CreateService, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createServiceEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("分类不存在".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createServiceTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case7: 创建服务商品时，包装单位ID不存在，返回7");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		comm.setPackageUnitID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.CASE_Commodity_CreateService, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createServiceEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("包装单位不存在".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void createServiceTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case8: 创建的服务商品与预淘汰商品同名");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		commodity.setName("康师傅牛肉面");// 预淘汰商品无法直接创建，故直接拿数据库中的预淘汰商品测试
		//
		String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createServiceEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void createServiceTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case9: 创建的服务商品与删除商品同名");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateService);

		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// 商品与删除商品同名的创建
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		commodity2.setName(commodity.getName());
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateService);
		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createServiceTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:服务商品设置期初值");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		comm.setShelfLife(0);
		comm.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		comm.setPurchaseFlag(0);
		comm.setnOStart(10);
		comm.setPurchasingPriceStart(20d);
		//
		String error = comm.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertTrue(Commodity.FIELD_ERROR_nOStartOfService.equals(error), error);
	}

	@Test
	public void createMultiPackagingTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case12:正常创建多包装商品");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm1.setRefCommodityID(commCreated.getID());
		comm1.setRefCommodityMultiple(3);
		comm1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createMultiPackagingTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2:添加名字重复的多包装商品");
		//
		final String commodityName = "薯片" + UUID.randomUUID().toString().substring(1, 8);
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setName(commodityName);
		commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		// 重复创建
		String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createMultiPackagingTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case3:名称不重复，not null字段传入一个null");
		//
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commTemplate.setSpecification(null);
		// //
		Map<String, Object> params3 = commTemplate.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate);
		//
		params3.put(Commodity.field.getFIELD_NAME_specification(), commTemplate.getSpecification());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params3);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_OtherError, params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createMultiPackagingTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case4:创建多包装商品时,staffID不存在，返回4");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm.setOperatorStaffID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Hack, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createMultiPackagingTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case5: 创建多包装商品时，品牌ID不存在，返回7");
		//
		Commodity comm4 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm4.setBrandID(BaseAction.INVALID_ID);
		Map<String, Object> params7 = comm4.getCreateParamEx(BaseBO.CASE_Commodity_CreateMultiPackaging, comm4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params7);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("品牌不存在".equals(params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createMultiPackagingTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case6: 创建多包装商品时，类别ID不存在，返回7");
		//
		Commodity comm5 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm5.setCategoryID(BaseAction.INVALID_ID);
		Map<String, Object> params8 = comm5.getCreateParamEx(BaseBO.CASE_Commodity_CreateMultiPackaging, comm5);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params8);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("分类不存在".equals(params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createMultiPackagingTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case7: 创建多包装商品时，包装单位ID不存在，返回7");
		//
		Commodity comm6 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm6.setPackageUnitID(BaseAction.INVALID_ID);
		Map<String, Object> params9 = comm6.getCreateParamEx(BaseBO.CASE_Commodity_CreateMultiPackaging, comm6);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params9);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("包装单位不存在".equals(params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void createMultiPackagingTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case8: 创建的多包装商品与预淘汰商品同名");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setName("康师傅牛肉面");// 预淘汰商品无法直接创建，故直接拿数据库中的预淘汰商品测试
		commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		//
		String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void createMultiPackagingTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case9: 创建的多包装商品与删除商品同名");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateMultiPackaging);

		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// 商品与删除商品同名的创建
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity2.setName(commodity.getName());
		commodity2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createMultiPackagingTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case10:创建多包装商品，RefCommodityID 不存在，创建失败");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm1.setRefCommodityID(9999);
		comm1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		//
		String error = comm1.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertTrue("".equals(error), error);
		//
		Map<String, Object> params = comm1.getCreateParamEx(BaseBO.CASE_Commodity_CreateMultiPackaging, comm1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createMultiPackagingTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case18:创建多包装商品，RefCommodityID 对应的是组合商品，创建失败");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated1.getID());
		comm2.setRefCommodityMultiple(3);
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		//
		String error = comm2.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = comm2.getCreateParamEx(BaseBO.CASE_Commodity_CreateMultiPackaging, comm2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createMultiPackagingTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case12:创建多包装商品，RefCommodityMultiple 不大于1，创建失败");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated.getID());
		comm2.setRefCommodityMultiple(1);
		//
		String error = comm2.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_refCommodityOfMultiPackaging);
		// //
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createMultiPackagingTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case13:创建多包装商品，RefCommodityID 对应的是服务商品，创建失败");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateService);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated.getID());
		Map<String, Object> params = comm2.getCreateParamEx(BaseBO.CASE_Commodity_CreateMultiPackaging, comm2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createMultiPackagingTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case14:创建多包装商品，RefCommodityID 对应的是多包装商品，创建失败");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated.getID());
		Map<String, Object> params = comm2.getCreateParamEx(BaseBO.CASE_Commodity_CreateMultiPackaging, comm2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createCombinationTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case11:正常创建组合商品");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createCombinationTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2:添加名字重复的组合商品");
		//
		final String commodityName = "薯片" + UUID.randomUUID().toString().substring(1, 8);
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		commodity.setName(commodityName);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateComposition);
		//
		// 重复创建
		String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createCombinationEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createCombinationTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case3:名称不重复，not null字段传入一个null");
		//
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		commTemplate.setSpecification(null);
		// //
		Map<String, Object> params3 = commTemplate.getCreateParamEx(BaseBO.INVALID_CASE_ID, commTemplate);
		//
		params3.put(Commodity.field.getFIELD_NAME_specification(), commTemplate.getSpecification());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createCombinationEx(params3);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_OtherError, params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case4:创建组合商品时,staffID不存在，返回4");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		comm.setOperatorStaffID(BaseAction.INVALID_ID);
		Map<String, Object> params = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createCombinationEx(params);
		//
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Hack, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case5: 创建组合商品时，品牌ID不存在，返回7");
		//
		Commodity comm4 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		comm4.setBrandID(BaseAction.INVALID_ID);
		Map<String, Object> params7 = comm4.getCreateParamEx(BaseBO.CASE_Commodity_CreateComposition, comm4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createCombinationEx(params7);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("品牌不存在".equals(params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case6: 创建组合商品时，类别ID不存在，返回7");
		//
		Commodity comm5 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		comm5.setCategoryID(BaseAction.INVALID_ID);
		Map<String, Object> params8 = comm5.getCreateParamEx(BaseBO.CASE_Commodity_CreateComposition, comm5);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createCombinationEx(params8);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("分类不存在".equals(params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case7: 创建组合商品时，包装单位ID不存在，返回7");
		//
		Commodity comm6 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		comm6.setPackageUnitID(BaseAction.INVALID_ID);
		Map<String, Object> params9 = comm6.getCreateParamEx(BaseBO.CASE_Commodity_CreateComposition, comm6);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createCombinationEx(params9);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue("包装单位不存在".equals(params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void createCombinationTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case8: 创建的组合商品与预淘汰商品同名");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		commodity.setName("康师傅牛肉面");// 预淘汰商品无法直接创建，故直接拿数据库中的预淘汰商品测试
		//
		String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.CASE_Commodity_CreateComposition, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createCombinationEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void createCombinationTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case9: 创建的组合商品与删除商品同名");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateComposition);

		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// 商品与删除商品同名的创建
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		commodity2.setName(commodity.getName());
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateComposition);
		// 删除创建的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createCombinationTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:创建组合商品，子商品为组合商品，创建失败");
		// 先创建一个组合商品A
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		// 再创建一个组合商品B
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateComposition);
		// 组合A、B
		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(commCreate1.getID());
		subCommodity.setSubCommodityID(commCreate2.getID());
		subCommodity.setSubCommodityNO(1);
		Map<String, Object> subParams = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(subParams);
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("不能组合除了单品外的其他类型商品".equals(subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:创建组合商品，子商品为多包装商品，创建失败");
		// 先创建一个组合商品A
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		// 再创建一个多包装商品B
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(1);
		comm2.setRefCommodityMultiple(10);
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 组合A、B
		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(commCreate1.getID());
		subCommodity.setSubCommodityID(commCreate2.getID());
		subCommodity.setSubCommodityNO(1);
		Map<String, Object> subParams = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(subParams);
		System.out.println("错误信息：" + subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("不能组合除了单品外的其他类型商品".equals(subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12:创建组合商品，子商品为服务商品，创建失败");
		// 先创建一个组合商品A
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		// 先创建一个服务商品B
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		comm2.setType(EnumCommodityType.ECT_Service.getIndex());
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateService);
		// 组合A、B
		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(commCreate1.getID());
		subCommodity.setSubCommodityID(commCreate2.getID());
		subCommodity.setSubCommodityNO(1);
		Map<String, Object> subParams = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(subParams);
		System.out.println("错误信息：" + subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("不能组合除了单品外的其他类型商品".equals(subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case13:创建组合商品，RefCommodityID != 0，创建失败");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		comm1.setRefCommodityID(1);
		//
		String error = comm1.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_refCommodity);
	}

	@Test
	public void createCombinationTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case14:创建组合商品，RefCommodityMultiple != 0，创建失败");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		comm1.setRefCommodityMultiple(2);
		//
		String error = comm1.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_refCommodity);
	}

	// 情况一：供应商ID大于0时，查询出相应的商品,情况二：类别ID大于0时，查询出相应的商品,情况三：供应商ID跟类别ID都为0时，抛出错误。
	// 根据商品类别或供应商查询商品测试
	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:输入iCategoryID作为条件查询");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setCategoryID(2);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm1 = new Commodity();
		comm1.setStatus(BaseAction.INVALID_STATUS);
		comm1.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm1.setCategoryID(2);
		comm1.setBrandID(BaseAction.INVALID_ID);
		comm1.setType(BaseAction.INVALID_Type);
		comm1.setDate1(null);
		comm1.setDate2(null);
		comm1.setQueryKeyword("");
		comm1.setPageIndex(1);
		comm1.setPageSize(10);
		//
		String error1 = comm1.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commCreated.getCategoryID() == commodity.getCategoryID(), "查询出的CategoryID不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm.getOperatorStaffID());
			commodity.setBarcodes(comm.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:输入iBrandID作为条件查询");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setBrandID(3);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);
		comm.setBrandID(3);
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commCreated.getBrandID() == commodity.getBrandID(), "查询出的BrandID不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}

			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:输入sName作为条件查询");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName("薯愿香辣味薯片" + UUID.randomUUID().toString().substring(1, 8));
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword(commCreated.getName());
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commCreated.getName().equals(commodity.getName()), "查询出的Name不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:输入sMnemonicCode作为条件查询 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setMnemonicCode("SP");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("SP");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commCreated.getMnemonicCode().equals(commodity.getMnemonicCode()), "查询出的MnemonicCode不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:输入大于6位的sBarcode作为条件查询 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setQueryKeyword("3548293894545");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("3548293");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			// RN商品不会返回条形码数据
			// Assert.assertTrue(commCreated.getString1().equals(commodity.getString1()),
			// "查询出的Barcode不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:无条件查询 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:输入iNO作为条件查询 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setNO(100);
		comm1.setnOStart(100);
		comm1.setPurchasingPriceStart(100D);
		comm1.setLatestPricePurchase(comm1.getPurchasingPriceStart()); // 期初商品latestPricePurchase要等于purchasingPriceStart
		BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(100); // 查询库存只有两种情况，传BaseAction.INVALID_ID查询全部，否则查询大于0的
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
//			Assert.assertTrue(commodity.getNO() > 0, "查询出的NO不正确");
			CommodityShopInfo commodityShopInfo = BaseCommodityTest.DataInput.getCommodityShopInfo();
			commodityShopInfo.setCommodityID(commodity.getID());
			Map<String, Object> paramsCommShopInfoRN = commodityShopInfo.getRetrieveNParam(BaseBO.INVALID_CASE_ID, commodityShopInfo);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> listCommodityShopInfo = commodityShopInfoMapper.retrieveN(paramsCommShopInfoRN);
			//
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(listCommodityShopInfo.size() != 0);
			boolean existNOGreaterThanZero = false;
			for(BaseModel bm : listCommodityShopInfo) {
				CommodityShopInfo commodityShopInfoRN = (CommodityShopInfo) bm;
				if(commodityShopInfoRN.getNO() > 0) {
					existNOGreaterThanZero = true;
				}
			}
			Assert.assertTrue(existNOGreaterThanZero, "查询出的NO不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		// 商品有库存,删除不了这条创建的商品
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);

		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveNTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8:输入iStatus作为条件查询 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(0);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commCreated.getStatus() == commodity.getStatus(), "查询出的Status不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:输入iStatus和iNO作为条件查询 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setNO(100);
		comm1.setnOStart(100);
		comm1.setPurchasingPriceStart(100D);
		comm1.setLatestPricePurchase(comm1.getPurchasingPriceStart()); // 期初商品latestPricePurchase要等于purchasingPriceStart
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(0);
		comm.setNO(100);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commCreated.getStatus() == commodity.getStatus(), "查询出的Status不正确");
			//
//			Assert.assertTrue(commodity.getNO() > 0, "查询出的NO不正确");
			CommodityShopInfo commodityShopInfo = BaseCommodityTest.DataInput.getCommodityShopInfo();
			commodityShopInfo.setCommodityID(commodity.getID());
			Map<String, Object> paramsCommShopInfoRN = commodityShopInfo.getRetrieveNParam(BaseBO.INVALID_CASE_ID, commodityShopInfo);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> listCommodityShopInfo = commodityShopInfoMapper.retrieveN(paramsCommShopInfoRN);
			//
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(listCommodityShopInfo.size() != 0);
			boolean existNOGreaterThanZero = false;
			for(BaseModel bm : listCommodityShopInfo) {
				CommodityShopInfo commodityShopInfoRN = (CommodityShopInfo) bm;
				if(commodityShopInfoRN.getNO() > 0) {
					existNOGreaterThanZero = true;
				}
			}
			Assert.assertTrue(existNOGreaterThanZero, "查询出的NO不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		// 商品有库存,删除不了这条创建的商品
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);

		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveNTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:输入iType作为条件查询 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(EnumCommodityType.ECT_Combination.getIndex());
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			int esc = commodity.getStatus();
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setStatus(EnumStatusCommodity.ESC_Normal.getIndex()); //
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
			Assert.assertEquals(error, "");
			commodity.setStatus(esc); //
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:输入iType和iStatus作为条件查询 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm = new Commodity();
		comm.setStatus(0);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(EnumCommodityType.ECT_Combination.getIndex());
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commCreated.getStatus() == commodity.getStatus(), "查询出的Status不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
			Assert.assertEquals(error, "");
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:输入iType和iNO作为条件查询,组合商品库存为0  ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		// comm1.setNO(1);
		BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(BaseAction.INVALID_ID);
		comm.setCategoryID(BaseAction.INVALID_ID);
		comm.setBrandID(BaseAction.INVALID_ID);
		comm.setType(EnumCommodityType.ECT_Combination.getIndex());
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			// 组合商品是没有库存的，所以库存应该是0
//			Assert.assertTrue(commodity.getNO() == 0, "查询出的NO不正确");
			CommodityShopInfo commodityShopInfo = BaseCommodityTest.DataInput.getCommodityShopInfo();
			commodityShopInfo.setCommodityID(commodity.getID());
			Map<String, Object> paramsCommShopInfoRN = commodityShopInfo.getRetrieveNParam(BaseBO.INVALID_CASE_ID, commodityShopInfo);
			//
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> listCommodityShopInfo = commodityShopInfoMapper.retrieveN(paramsCommShopInfoRN);
			//
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(listCommodityShopInfo.size() != 0);
			for(BaseModel bm : listCommodityShopInfo) {
				CommodityShopInfo commodityShopInfoRN = (CommodityShopInfo) bm;
				Assert.assertTrue(commodityShopInfoRN.getNO() == 0, "查询出的NO不正确");
			}
			//
			int esc = commodity.getStatus();
			int staffID = commodity.getOperatorStaffID();
			commodity.setStatus(EnumStatusCommodity.ESC_Normal.getIndex()); //
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
			Assert.assertEquals(error, "");
			commodity.setStatus(esc); //
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		// 商品有库存,删除不了这条创建的商品
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);

		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveNTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:根据条形码查询商品类型为2的");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated.getID());
		comm2.setRefCommodityMultiple(3);
		comm2.setQueryKeyword("254521454628769752");
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("254521454628769752");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() == 0);
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14:条形码和商品名称一样 ");
		//
		final String commNameORBarcode = UUID.randomUUID().toString().substring(1, 8);
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName(commNameORBarcode);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setQueryKeyword(commNameORBarcode);
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(BaseAction.INVALID_NO);
		comm.setCategoryID(BaseAction.INVALID_ID);
		comm.setBrandID(BaseAction.INVALID_ID);
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword(commCreated1.getQueryKeyword());
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			// RN商品不会返回条形码数据
			// Assert.assertTrue(commCreated1.getString1().equals(commodity.getString1()) ||
			// commCreated.getName().equals(commodity.getName()), "查询出的Barcode或Name不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm2.getOperatorStaffID());
			commodity.setBarcodes(comm2.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_MultiPackaging.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			} else {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest15() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15:商品助记码和商品名称一样 ");
		//
		final String commNameORBarcode = UUID.randomUUID().toString().substring(1, 8);
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setMnemonicCode(commNameORBarcode);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setName(commNameORBarcode);
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword(commCreated1.getName());
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commCreated.getMnemonicCode().equals(commodity.getMnemonicCode()) || commCreated1.getName().equals(commodity.getName()), "查询出的MnemonicCode或Name不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm2.getOperatorStaffID());
			commodity.setBarcodes(comm2.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest16() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case16:通过name查询多包装商品,数据为null");
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated.getID());
		comm2.setRefCommodityMultiple(3);
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword(commCreated1.getName());
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() == 0);
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest17() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case17:通过name查询组合商品");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(EnumCommodityType.ECT_Combination.getIndex());
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword(commCreated.getName());
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commCreated.getName().equals(commodity.getName()), "查询出的Name不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
			Assert.assertEquals(error, "");
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest18() throws CloneNotSupportedException, InterruptedException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case18:根据一时间段进行查询");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setDate1(DatetimeUtil.getDate(new Date(), -1));
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateSingle);
		comm1.setDate2(DatetimeUtil.getDate(new Date(), +1));
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(comm1.getDate1());
		comm.setDate2(comm1.getDate2());
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commodity.getCreateDatetime().after(comm.getDate1()) && commodity.getCreateDatetime().before(comm.getDate2()), "查询出的CreateDatetime不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm2.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest19() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case19:查询某时间后的商品");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setDate1(DatetimeUtil.getDate(new Date(), -1));
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(comm1.getDate1());
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commodity.getCreateDatetime().after(comm.getDate1()), "查询出的CreateDatetime不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm2.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest20() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case20:查询某段时间前的商品");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateSingle);
		comm1.setDate2(DatetimeUtil.getDate(new Date(), +1));
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(comm1.getDate2());
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			Assert.assertTrue(commodity.getCreateDatetime().before(comm.getDate2()), "查询出的CreateDatetime不正确");
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm2.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			if (commodity.getType() == EnumCommodityType.ECT_Normal.getIndex()) {
				String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Combination.getIndex()) {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
				Assert.assertEquals(error, "");
			} else if (commodity.getType() == EnumCommodityType.ECT_Service.getIndex()) {
				commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
				commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
				Assert.assertEquals(error, "");
			} else {
				String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
				Assert.assertEquals(error, "");
			}
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest21() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case21:开始时间大于结束时间");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setDate1(new Date());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		Thread.sleep(1000); // 让两个Date不一样
		comm.setDate1(new Date());
		comm.setDate2(comm1.getDate1());
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() == 0);
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest22() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case22:输入小于7位的sBarcode作为条件查询,期望是查询不到商品 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setQueryKeyword("3548293894599");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("354829");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() == 0);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest23() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case23:传入QueryKeyword包含_的特殊字符进行模糊搜索 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName(System.currentTimeMillis() % 1000000 + "_1");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("_1");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() != 0);
		//
		for (BaseModel baseModel : list) {
			Commodity commodity = (Commodity) baseModel;
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(comm1.getOperatorStaffID());
			commodity.setBarcodes(comm1.getBarcodes());
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			//
			String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
			//
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		System.out.println("【查询N个商品】测试成功！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case24:条码有123456789 12345678 输入1234567 搜索");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName(System.currentTimeMillis() % 1000000 + "_1");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes = DataInput.getBarcodes();
		barcodes.setCommodityID(commCreated.getID());
		barcodes.setBarcode("123456789");
		Map<String, Object> barcodesparams = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate = (Barcodes) barcodesMapper.create(barcodesparams);
		assertTrue(barcodesCreate != null && EnumErrorCode.values()[Integer.parseInt(barcodesparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "条形码增加失败");
		//
		Barcodes barcodes2 = DataInput.getBarcodes();
		barcodes2.setCommodityID(commCreated.getID());
		barcodes2.setBarcode("12345678");
		Map<String, Object> barcodesparams2 = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate2 = (Barcodes) barcodesMapper.create(barcodesparams2);
		assertTrue(barcodesCreate2 != null && EnumErrorCode.values()[Integer.parseInt(barcodesparams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "条形码增加失败");
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(new Date());
		comm.setQueryKeyword("1234567");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(list.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		boolean b = false;
		for (BaseModel bm : list) {
			Commodity commodity = (Commodity) bm;
			if (commodity.getID() == commCreated.getID()) {
				b = true;
				break;
			}
		}
		Assert.assertTrue(b, "查询失败");
		//
		barcodesCreate.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> barcodesDeleteparams = barcodes.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodesCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(barcodesDeleteparams);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(barcodesDeleteparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "条形码删除失败");
		//
		barcodesCreate2.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> barcodesDeleteparams2 = barcodes.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodesCreate2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(barcodesDeleteparams2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(barcodesDeleteparams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "条形码删除失败");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest25() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case25:输入64位条形码查询商品 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		String barcodes = "12345678123456781234567812345678123456781234567812345678" + Shared.generateStringByTime(8);
		comm1.setBarcodes(barcodes);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword(barcodes);
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> list = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(list.size() == 1);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest26() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case26:输入大于64位条形码查询商品 ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setQueryKeyword("1234567812345678123456781234567812345678123456781234567812345678");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("12345678123456781234567812345678123456781234567812345678123456787777");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, Commodity.FIELD_ERROR_queryKeyword); // Commodity的checkRetrieveN验证不通过
		//
		// Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID,
		// comm);
		// //
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// List<BaseModel> list = commodityMapper.retrieveN(params);
		// //
		// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_NoError,
		// params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// Assert.assertTrue(list.size() == 1);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest27() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case27:检查商品是否默认降序");
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setBrandID(BaseAction.INVALID_ID);// -1代表不根据该字段查询
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		Map<String, Object> params = comm.getRetrieveNParam(BaseBO.INVALID_CASE_ID, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bmList = commodityMapper.retrieveN(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(bmList.size() > 0);
		for (int i = 1; i < bmList.size(); i++) {
			assertTrue(bmList.get(i - 1).getID() > bmList.get(i).getID(), "数据返回错误（非降序）");
		}
	}

	@Test
	public void retrieve1Test1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 1: 查询一个状态正常的商品");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "查询失败，无数据");
		//
		int staffID = comm.getOperatorStaffID();
		comm.setOperatorStaffID(comm1.getOperatorStaffID());
		comm.setBarcodes(comm1.getBarcodes());//
		comm.setnOStart(Commodity.NO_START_Default);
		comm.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		String error = comm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		comm.setOperatorStaffID(staffID);
		comm.setBarcodes(null);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieve1ExTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("Case 2: 查询一个删除状态的商品，不能查询");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		//
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "查询出删除状态的商品");
	}

	@Test
	public void retrieve1ExTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("Case 3: 我们测试时，添加一个includeDeleted，includeDeleted = 1时可以查询一个删除状态的商品");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		//
		commCreated.setIncludeDeleted(EnumBoolean.EB_Yes.getIndex());
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "查询删除状态的商品失败");
		// 因为是已经删除的，所以F_PackageUnitID，F_CategoryID，F_BrandID会设为0，checkCreate不会通过
		// String error = commRetrivedAfterDeleted2.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
	}

	@Test
	public void retrieve1ExTest4() throws CloneNotSupportedException {
		Shared.caseLog("Case4:查询不存在的ID ec=0");
		//
		Commodity commCreated = new Commodity();
		commCreated.setID(BaseAction.INVALID_ID);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm == null, "查询失败！不允许查询一个不存在的商品");
	}

	@Test
	public void retrieve1ExTest5() throws Exception {
		Shared.caseLog("Case5:查询组合商品 ec=0");

		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity compositionComm = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(compositionComm, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null && comm.getListSlave1() != null && comm.getListSlave1().size() == 1, "查询失败!");
		//
		BaseCommodityTest.deleteCommodityViaMapper(compositionComm, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常修改 --F_Name未修改 修改了其他属性");
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		Commodity c = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		c.setShortName("辣条");
		c.setOperatorStaffID(STAFF_ID3);
		//
		String error = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate = c.getUpdateParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase1 = (Commodity) commodityMapper.update(paramsForUpdate);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNotNull(updateCommodityCase1);
		//
		c.setIgnoreIDInComparision(true);
		// 更新后对比数据不一致返回-1，一致返回0
		c.setIgnoreSlaveListInComparision(true);
		if (c.compareTo(updateCommodityCase1) != 0) {
			Assert.assertTrue(false, "对象的字段与DB读出的不相等");
		}
		//
		int staffID = updateCommodityCase1.getOperatorStaffID();
		updateCommodityCase1.setOperatorStaffID(commCreate.getOperatorStaffID());
		updateCommodityCase1.setBarcodes(commCreate.getBarcodes());
		updateCommodityCase1.setnOStart(Commodity.NO_START_Default);
		updateCommodityCase1.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		String error1 = updateCommodityCase1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		updateCommodityCase1.setOperatorStaffID(staffID);
		updateCommodityCase1.setBarcodes(null);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(updateCommodityCase1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:F_Name修改为数据库不存在的名称  没修改其他属性");
		//
		Commodity commCreate2 = BaseCommodityTest.DataInput.getCommodity();
		commCreate2.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commCreate2.setOperatorStaffID(STAFF_ID3);
		Commodity c2 = BaseCommodityTest.createCommodityViaMapper(commCreate2, BaseBO.CASE_Commodity_CreateSingle);
		//
		c2.setName(UUID.randomUUID().toString().substring(1, 7));
		// c2.setInt1(1);
		c2.setOperatorStaffID(STAFF_ID3);
		//
		String error = c2.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate2 = c2.getUpdateParam(BaseBO.INVALID_CASE_ID, c2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase2 = (Commodity) commodityMapper.update(paramsForUpdate2);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForUpdate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNotNull(updateCommodityCase2);
		//
		c2.setIgnoreIDInComparision(true);
		// 更新后对比数据不一致返回-1，一致返回0
		c2.setIgnoreSlaveListInComparision(true);
		if (c2.compareTo(updateCommodityCase2) != 0) {
			Assert.assertTrue(false, "对象的字段与DB读出的不相等");
		}
		//
		int staffID = updateCommodityCase2.getOperatorStaffID();
		updateCommodityCase2.setOperatorStaffID(commCreate2.getOperatorStaffID());
		updateCommodityCase2.setBarcodes(commCreate2.getBarcodes());
		updateCommodityCase2.setnOStart(Commodity.NO_START_Default);
		updateCommodityCase2.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		String error1 = updateCommodityCase2.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		updateCommodityCase2.setOperatorStaffID(staffID);
		updateCommodityCase2.setBarcodes(null);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(updateCommodityCase2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:F_Name修改为数据库不存在的名称  修改了其他属性");
		//
		Commodity commCreate3 = BaseCommodityTest.DataInput.getCommodity();
		commCreate3.setOperatorStaffID(STAFF_ID3);
		commCreate3.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c3 = BaseCommodityTest.createCommodityViaMapper(commCreate3, BaseBO.CASE_Commodity_CreateSingle);
		//
		c3.setName(UUID.randomUUID().toString().substring(1, 7));
		c3.setShortName("辣条");
		// c3.setInt1(1);
		c3.setOperatorStaffID(STAFF_ID3);
		//
		String error = c3.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate3 = c3.getUpdateParam(BaseBO.INVALID_CASE_ID, c3);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase3 = (Commodity) commodityMapper.update(paramsForUpdate3);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForUpdate3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNotNull(updateCommodityCase3);
		//
		c3.setIgnoreIDInComparision(true);
		// 更新后对比数据不一致返回-1，一致返回0
		c3.setIgnoreSlaveListInComparision(true);
		if (c3.compareTo(updateCommodityCase3) != 0) {
			Assert.assertTrue(false, "对象的字段与DB读出的不相等");
		}
		//
		int staffID = updateCommodityCase3.getOperatorStaffID();
		updateCommodityCase3.setOperatorStaffID(commCreate3.getOperatorStaffID());
		updateCommodityCase3.setBarcodes(commCreate3.getBarcodes());
		updateCommodityCase3.setnOStart(Commodity.NO_START_Default);
		updateCommodityCase3.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		String error1 = updateCommodityCase3.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		updateCommodityCase3.setOperatorStaffID(staffID);
		updateCommodityCase3.setBarcodes(null);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(updateCommodityCase3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:F_Name修改为数据库存在的名称  理论上为：修改失败");
		//
		Commodity commCreate4 = BaseCommodityTest.DataInput.getCommodity();
		commCreate4.setOperatorStaffID(STAFF_ID3);
		commCreate4.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c4 = BaseCommodityTest.createCommodityViaMapper(commCreate4, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commCreate41 = BaseCommodityTest.DataInput.getCommodity();
		commCreate41.setName(UUID.randomUUID().toString().substring(1, 7));
		commCreate41.setOperatorStaffID(STAFF_ID3);
		commCreate41.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c41 = BaseCommodityTest.createCommodityViaMapper(commCreate41, BaseBO.CASE_Commodity_CreateSingle);
		//
		c4.setName(c41.getName());
		// c4.setInt1(1);
		c4.setOperatorStaffID(STAFF_ID3);
		//
		String error = c4.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate4 = c4.getUpdateParam(BaseBO.INVALID_CASE_ID, c4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase4 = (Commodity) commodityMapper.update(paramsForUpdate4);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updateCommodityCase4);
		// 删除创建出来的测试对象
		// c4.setInt1(0);
		// c41.setInt1(0);
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(c41, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:已删除的商品做更新操作");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
		//
		commCreate.setShortName("辣条");
		commCreate.setOperatorStaffID(STAFF_ID3);
		//
		String error = commCreate.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate5 = commCreate.getUpdateParam(BaseBO.INVALID_CASE_ID, commCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase5 = (Commodity) commodityMapper.update(paramsForUpdate5);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, paramsForUpdate5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updateCommodityCase5);
	}

	@Test
	public void updateTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:经办人不存在 ");
		//
		Commodity commCreate6 = BaseCommodityTest.DataInput.getCommodity();
		commCreate6.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c5 = BaseCommodityTest.createCommodityViaMapper(commCreate6, BaseBO.CASE_Commodity_CreateSingle);
		c5.setOperatorStaffID(Shared.BIG_ID);
		//
		String error = c5.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate6 = c5.getUpdateParam(BaseBO.INVALID_CASE_ID, c5);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase6 = (Commodity) commodityMapper.update(paramsForUpdate6);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Hack, paramsForUpdate6.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updateCommodityCase6);
		// 删除创建出来的测试对象
		c5.setOperatorStaffID(commCreate6.getOperatorStaffID());
		BaseCommodityTest.deleteCommodityViaMapper(c5, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "不能用不存在的BrandID修改商品";
		Shared.caseLog("Case7:" + message);
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		commCreate.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		c.setBrandID(BaseAction.INVALID_ID);
		//

		String error = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_brandID);
		//
		Map<String, Object> paramsForUpdate = c.getUpdateParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase = (Commodity) commodityMapper.update(paramsForUpdate);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确" + paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updateCommodityCase);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "不能用不存在的CategoryID修改商品";
		Shared.caseLog("Case8:" + message);
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		commCreate.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		c.setCategoryID(-5);
		//
		String error = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_categoryID);
		//
		Map<String, Object> paramsForUpdate = c.getUpdateParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase = (Commodity) commodityMapper.update(paramsForUpdate);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确" + paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updateCommodityCase);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "不能用不存在的PackageUnitID修改商品";
		Shared.caseLog("Case9:" + message);
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		commCreate.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		c.setPackageUnitID(-5);
		//
		String error = c.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_packageUnitID);
		//
		Map<String, Object> paramsForUpdate = c.getUpdateParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase = (Commodity) commodityMapper.update(paramsForUpdate);// ...
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确" + paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updateCommodityCase);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:修改商品名字为已删除的商品名字，修改成功");
		// 创建一个商品
		Commodity commCreate4 = BaseCommodityTest.DataInput.getCommodity();
		commCreate4.setOperatorStaffID(STAFF_ID3);
		commCreate4.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c4 = BaseCommodityTest.createCommodityViaMapper(commCreate4, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commCreate41 = BaseCommodityTest.DataInput.getCommodity();
		commCreate41.setName(UUID.randomUUID().toString().substring(1, 7));
		commCreate41.setOperatorStaffID(STAFF_ID3);
		commCreate41.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c41 = BaseCommodityTest.createCommodityViaMapper(commCreate41, BaseBO.CASE_Commodity_CreateSingle);
		// 删除商品，使其状态为2
		BaseCommodityTest.deleteCommodityViaMapper(c41, EnumErrorCode.EC_NoError);
		//
		c4.setName(c41.getName());
		// c4.setInt1(1);
		c4.setOperatorStaffID(STAFF_ID3);
		//
		String error = c4.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate4 = c4.getUpdateParam(BaseBO.INVALID_CASE_ID, c4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase4 = (Commodity) commodityMapper.update(paramsForUpdate4);// ...
		//
		Assert.assertTrue(updateCommodityCase4 != null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:组合商品修改期初值备注失败！"); // 组合商品不能修改
		// 创建一个商品
		Commodity commCreate4 = BaseCommodityTest.DataInput.getCommodity();
		commCreate4.setOperatorStaffID(STAFF_ID3);
		commCreate4.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commCreate4.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity c4 = BaseCommodityTest.createCommodityViaMapper(commCreate4, BaseBO.CASE_Commodity_CreateComposition);
		c4.setStartValueRemark("123");
		//
		c4.setOperatorStaffID(STAFF_ID3);
		//
		String error = c4.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_type);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:多包装商品修改期初值备注失败！");
		// 创建一个商品
		Commodity commCreate4 = BaseCommodityTest.DataInput.getCommodity();
		commCreate4.setOperatorStaffID(STAFF_ID3);
		commCreate4.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c4 = BaseCommodityTest.createCommodityViaMapper(commCreate4, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commCreate5 = BaseCommodityTest.DataInput.getCommodity();
		commCreate5.setOperatorStaffID(STAFF_ID3);
		commCreate5.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commCreate5.setRefCommodityID(c4.getID());
		commCreate5.setRefCommodityMultiple(3);
		commCreate5.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commCreate5.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity c5 = BaseCommodityTest.createCommodityViaMapper(commCreate5, BaseBO.CASE_Commodity_CreateMultiPackaging);
		c5.setStartValueRemark("123");
		//
		c5.setOperatorStaffID(STAFF_ID3);
		//
		String error = c5.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_type);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c5, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:服务商品修改期初值备注失败！");
		// 创建一个商品
		Commodity commCreate4 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		commCreate4.setOperatorStaffID(STAFF_ID3);
		commCreate4.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commCreate4.setType(EnumCommodityType.ECT_Service.getIndex());
		commCreate4.setShelfLife(0);
		commCreate4.setnOStart(Commodity.NO_START_Default);
		commCreate4.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		commCreate4.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		commCreate4.setPurchaseFlag(0);
		Commodity c4 = BaseCommodityTest.createCommodityViaMapper(commCreate4, BaseBO.CASE_Commodity_CreateService);
		c4.setStartValueRemark("123");
		//
		c4.setOperatorStaffID(STAFF_ID3);
		//
		String error = c4.checkUpdate(BaseBO.CASE_UpdateCommodityOfService);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_startValueRemark_Simple);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14:普通商品修改期初值备注成功1");
		// 创建一个商品
		Commodity commCreate4 = BaseCommodityTest.DataInput.getCommodity();
		commCreate4.setOperatorStaffID(STAFF_ID3);
		commCreate4.setType(EnumCommodityType.ECT_Normal.getIndex());
		commCreate4.setStartValueRemark("abc");
		Commodity c4 = BaseCommodityTest.createCommodityViaMapper(commCreate4, BaseBO.CASE_Commodity_CreateSingle);
		c4.setStartValueRemark("123");
		//
		c4.setOperatorStaffID(STAFF_ID3);
		//
		String error = c4.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate4 = c4.getUpdateParam(BaseBO.INVALID_CASE_ID, c4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase4 = (Commodity) commodityMapper.update(paramsForUpdate4);// ...
		//
		Assert.assertTrue(updateCommodityCase4 != null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		if (commCreate4.getStartValueRemark().equals(updateCommodityCase4.getStartValueRemark())) {
			Assert.assertTrue(false, "ID为：" + c4.getID() + "的普通商品修改期初值备注失败！");
		}
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest15() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15:普通商品修改期初值备注失败(长度超长)");
		// 创建一个商品
		Commodity commCreate4 = BaseCommodityTest.DataInput.getCommodity();
		commCreate4.setOperatorStaffID(STAFF_ID3);
		commCreate4.setType(EnumCommodityType.ECT_Normal.getIndex());
		commCreate4.setStartValueRemark("abc");
		Commodity c4 = BaseCommodityTest.createCommodityViaMapper(commCreate4, BaseBO.CASE_Commodity_CreateSingle);
		c4.setStartValueRemark("012345678901234567890123456789012345678901234567890123456789");
		//
		c4.setOperatorStaffID(STAFF_ID3);
		//
		String error = c4.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_startValueRemark);
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest16() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case16:普通商品修改期初值备注成功2");
		// 创建一个商品
		Commodity commCreate4 = BaseCommodityTest.DataInput.getCommodity();
		commCreate4.setOperatorStaffID(STAFF_ID3);
		commCreate4.setType(EnumCommodityType.ECT_Normal.getIndex());
		commCreate4.setStartValueRemark("abc");
		commCreate4.setStartValueRemark("123");
		Commodity c4 = BaseCommodityTest.createCommodityViaMapper(commCreate4, BaseBO.CASE_Commodity_CreateSingle);
		c4.setStartValueRemark("");
		//
		c4.setOperatorStaffID(STAFF_ID3);
		//
		String error = c4.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> paramsForUpdate4 = c4.getUpdateParam(BaseBO.INVALID_CASE_ID, c4);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updateCommodityCase4 = (Commodity) commodityMapper.update(paramsForUpdate4);// ...
		//
		Assert.assertTrue(updateCommodityCase4 != null && EnumErrorCode.values()[Integer.parseInt(paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsForUpdate4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		if (commCreate4.getStartValueRemark().equals(updateCommodityCase4.getStartValueRemark())) {
			Assert.assertTrue(false, "ID为：" + c4.getID() + "的普通商品修改期初值备注失败！");
		}
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updatePriceTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("CASE1:修改平均进货价 ");
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		commCreate.setPurchasingUnit("包");
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		// 有采购单位必须创建与采购单的关联
		PurchasingOrderCommodity poc = BasePurchasingOrderTest.createPurchasingOrderViaMapper(comm);
		//
		comm.setLatestPricePurchase(Math.abs(new Random().nextDouble()));
		comm.setOperatorStaffID(STAFF_ID3);
		comm.setShopID(commCreate.getShopID());
		comm.setPriceRetail(commCreate.getPriceRetail());
		List<CommodityShopInfo> listCommodityShopInfos = (List<CommodityShopInfo>) comm.getListSlave2();
		for(CommodityShopInfo commodityShopInfo : listCommodityShopInfos) {
			if(commodityShopInfo.getShopID() == comm.getShopID()) {
				commodityShopInfo.setLatestPricePurchase(comm.getLatestPricePurchase());
			}
		}
		//
		String error = comm.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> param = comm.getUpdateParam(BaseBO.CASE_Commodity_UpdatePrice, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updatePrice = (Commodity) commodityMapper.updatePrice(param);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNotNull(updatePrice);
		//
		CommodityShopInfo commodityShopInfo = BaseCommodityTest.DataInput.getCommodityShopInfo();
		commodityShopInfo.setCommodityID(updatePrice.getID());
		// 不根据门店查询，查询所有门店
		commodityShopInfo.setShopID(0);
		Map<String, Object> paramsCommShopInfoRN = commodityShopInfo.getRetrieveNParam(BaseBO.INVALID_CASE_ID, commodityShopInfo);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listCommodityShopInfoAfterUpdatePrice = commodityShopInfoMapper.retrieveN(paramsCommShopInfoRN);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(listCommodityShopInfoAfterUpdatePrice.size() != 0);
		updatePrice.setListSlave2(listCommodityShopInfoAfterUpdatePrice);
		//
		if (comm.compareTo(updatePrice) != 0) {
			Assert.assertTrue(false, "对象的字段与DB读出的不相等");
		}
		//
		int staffID = updatePrice.getOperatorStaffID();
		updatePrice.setOperatorStaffID(commCreate.getOperatorStaffID());
		updatePrice.setBarcodes(commCreate.getBarcodes());
		updatePrice.setnOStart(Commodity.NO_START_Default);
		updatePrice.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		String error1 = updatePrice.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		updatePrice.setOperatorStaffID(staffID);

		//
		// 删除与采购的依赖关系
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(poc);
		//
		// 删除本方法创建出来的商品comm
		BaseCommodityTest.deleteCommodityViaMapper(comm, EnumErrorCode.EC_NoError);
		// CASE2:修改进货价
		// System.out.println("------------------------ CASE2:修改进货价
		// ----------------------");
		// comm.setPricePurchase(Math.abs(new Random().nextFloat()));
		// Map<String, Object> Updateparams =
		// comm.getUpdateParam(BaseBO.CASE_Commodity_UpdatePrice, comm);
		//
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// mapper.updatePrice(Updateparams);
		//
		// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(Updateparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
		// EnumErrorCode.EC_NoError);
	}

	@Test
	public void updatePriceTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("CASE2:修改零售价 ");
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		commCreate.setPurchasingUnit("包");
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		String error = comm.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(error, "");
		//
		// 有采购单位必须创建与采购单的关联
		PurchasingOrderCommodity poc = BasePurchasingOrderTest.createPurchasingOrderViaMapper(comm);
		//
		comm.setPriceRetail(Math.abs(new Random().nextDouble()));
		comm.setOperatorStaffID(STAFF_ID3);
		comm.setLatestPricePurchase(commCreate.getLatestPricePurchase());
		List<CommodityShopInfo> listCommodityShopInfos = (List<CommodityShopInfo>) comm.getListSlave2();
		for(CommodityShopInfo commodityShopInfo : listCommodityShopInfos) {
			if(commodityShopInfo.getShopID() == comm.getShopID()) {
				commodityShopInfo.setPriceRetail(comm.getPriceRetail());
			}
		}
		Map<String, Object> param1 = comm.getUpdateParam(BaseBO.CASE_Commodity_UpdatePrice, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updatePrice = (Commodity) commodityMapper.updatePrice(param1);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, param1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNotNull(updatePrice);
		//
		CommodityShopInfo commodityShopInfo = BaseCommodityTest.DataInput.getCommodityShopInfo();
		commodityShopInfo.setCommodityID(updatePrice.getID());
		// 不根据门店查询，查询所有门店
		commodityShopInfo.setShopID(0);
		Map<String, Object> paramsCommShopInfoRN = commodityShopInfo.getRetrieveNParam(BaseBO.INVALID_CASE_ID, commodityShopInfo);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> listCommodityShopInfoAfterUpdatePrice = commodityShopInfoMapper.retrieveN(paramsCommShopInfoRN);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCommShopInfoRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(listCommodityShopInfoAfterUpdatePrice.size() != 0);
		updatePrice.setListSlave2(listCommodityShopInfoAfterUpdatePrice);
		//
		if (comm.compareTo(updatePrice) != 0) {
			Assert.assertTrue(false, "对象的字段与DB读出的不相等");
		}
		//
		int staffID = updatePrice.getOperatorStaffID();
		updatePrice.setOperatorStaffID(commCreate.getOperatorStaffID());
		updatePrice.setBarcodes(commCreate.getBarcodes());
		updatePrice.setnOStart(Commodity.NO_START_Default);
		updatePrice.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		String error1 = updatePrice.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		updatePrice.setOperatorStaffID(staffID);
		updatePrice.setBarcodes(null);
		//
		// 删除与采购的依赖关系
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(poc);
		//
		// 删除本方法创建出来的商品comm
		BaseCommodityTest.deleteCommodityViaMapper(comm, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updatePriceTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("CASE3:传入不存在的经办人ID");
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		commCreate.setPurchasingUnit("包");

		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		String error = comm.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(error, "");
		//
		// 有采购单位必须创建与采购单的关联
		PurchasingOrderCommodity poc = BasePurchasingOrderTest.createPurchasingOrderViaMapper(comm);
		//
		comm.setPriceRetail(Math.abs(new Random().nextDouble()));
		comm.setOperatorStaffID(BaseAction.INVALID_ID);
		Map<String, Object> param2 = comm.getUpdateParam(BaseBO.CASE_Commodity_UpdatePrice, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updatePrice = (Commodity) commodityMapper.updatePrice(param2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Hack, param2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updatePrice);
		//
		// 删除与采购的依赖关系
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(poc);
		//
		// 删除本方法创建出来的商品comm
		comm.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(comm, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 1: 单品无多包装 1条码 删除成功");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 2: 单品无多包装 2条码 删除成功");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes b = DataInput.getBarcodes();
		b.setCommodityID(commCreated.getID());
		BaseBarcodesTest.createBarcodesViaMapper(b, BaseBO.INVALID_CASE_ID);
		//
		commCreated.setOperatorStaffID(c.getOperatorStaffID());
		Map<String, Object> paramForDelete2 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 查询删除的这条数据
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "删除失败，还能查询出该商品");
	}

	@Test
	public void deleteSimpleTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 3: 单品有1个多包装 单品1条码 多包装1条码 删除失败");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity c2 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Map<String, Object> paramForDelete3 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete3);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询这条数据
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case3 测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(c2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 4: 单品有1个多包装 单品1条码 多包装1条码  先删除了副单位商品，删除成功");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity c2 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 先删除多包装的副单位
		c2.setOperatorStaffID(c1.getOperatorStaffID());
		BaseCommodityTest.deleteCommodityViaMapper(c2, EnumErrorCode.EC_NoError);
		//
		commCreated.setOperatorStaffID(c1.getOperatorStaffID());
		Map<String, Object> paramForDelete4 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		// 删除该单品， 删除成功
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete4);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 查询这条数据
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case4 测试失败！");
	}

	// @Test
	// public void deleteSimpleTest4() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	// Shared.caseLog("Case 4: 单品有1个多包装 单品2条码 多包装1条码 删除成功");
	// //
	// Commodity c = BaseCommodityTest.DataInput.getCommodity();
	// Commodity commCreated =
	// BaseCommodityTest.createCommodity(BaseBO.INVALID_CASE_ID, c);
	// //
	// Barcodes b = DataInput.getBarcodes();
	// b.setCommodityID(commCreated.getID());
	// createBarcodes(b);
	// //
	// Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
	// c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
	// c1.setRefCommodityID(commCreated.getID());
	// c1.setRefCommodityMultiple(3);
	// createCommodity(BaseBO.CASE_Commodity_CreateMultiPackaging, c1);
	// //
	// Map<String, Object> paramForDelete4 =
	// commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// commodityMapper.deleteSimple(paramForDelete4);
	// //
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError);
	// // 查询删除的这条数据
	// commCreated.setInt1(RETRIEVE1_DELETED_COMMODITY);
	// Map<String, Object> paramsRetrieve4 =
	// commCreated.getRetrieve1Param(BaseBO.INVALID_CASE_ID, commCreated);
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Commodity commRetrived4 = (Commodity)
	// commodityMapper.retrieve1(paramsRetrieve4);// ...
	// //
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoSuchData,
	// paramsRetrieve4.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// Assert.assertNull(commRetrived4, "Case4 测试失败！");
	// }
	//
	// @Test
	// public void deleteSimpleTest5() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case 5: 单品有1个多包装 单品1条码 多包装2条码 删除成功");
	// //
	// Commodity c = BaseCommodityTest.DataInput.getCommodity();
	// Commodity commCreated =
	// BaseCommodityTest.createCommodity(BaseBO.INVALID_CASE_ID, c);
	// //
	// Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
	// c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
	// c1.setRefCommodityID(commCreated.getID());
	// c1.setRefCommodityMultiple(3);
	// Commodity commCreated1 =
	// createCommodity(BaseBO.CASE_Commodity_CreateMultiPackaging, c1);
	// //
	// Barcodes b = DataInput.getBarcodes();
	// b.setCommodityID(commCreated1.getID());
	// createBarcodes(b);
	// //
	// Map<String, Object> paramForDelete5 =
	// commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// commodityMapper.deleteSimple(paramForDelete5);
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError);
	// // 查询删除的这条数据
	// commCreated.setInt1(RETRIEVE1_DELETED_COMMODITY);
	// Map<String, Object> paramsRetrieve5 =
	// commCreated.getRetrieve1Param(BaseBO.INVALID_CASE_ID, commCreated);
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Commodity commRetrived5 = (Commodity)
	// commodityMapper.retrieve1(paramsRetrieve5);// ...
	// //
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoSuchData,
	// paramsRetrieve5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// Assert.assertNull(commRetrived5, "Case5 测试失败！");
	// }
	//
	// @Test
	// public void deleteSimpleTest6() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case 6: 单品有1个多包装 单品2条码 多包装2条码 删除成功");
	// //
	// Commodity c = BaseCommodityTest.DataInput.getCommodity();
	// Commodity commCreated =
	// BaseCommodityTest.createCommodity(BaseBO.INVALID_CASE_ID, c);
	// //
	// Barcodes b = DataInput.getBarcodes();
	// b.setCommodityID(commCreated.getID());
	// createBarcodes(b);
	// //
	// Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
	// c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
	// c1.setRefCommodityID(commCreated.getID());
	// c1.setRefCommodityMultiple(3);
	// Commodity commCreated1 =
	// createCommodity(BaseBO.CASE_Commodity_CreateMultiPackaging, c1);
	// //
	// Barcodes b1 = DataInput.getBarcodes();
	// b1.setCommodityID(commCreated1.getID());
	// createBarcodes(b1);
	// //
	// Map<String, Object> paramForDelete6 =
	// commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// commodityMapper.deleteSimple(paramForDelete6);
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError);
	// // 查询删除的这条数据
	// commCreated.setInt1(RETRIEVE1_DELETED_COMMODITY);
	// Map<String, Object> paramsRetrieve6 =
	// commCreated.getRetrieve1Param(BaseBO.INVALID_CASE_ID, commCreated);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Commodity commRetrived6 = (Commodity)
	// commodityMapper.retrieve1(paramsRetrieve6);// ...
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoSuchData,
	// paramsRetrieve6.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// Assert.assertNull(commRetrived6, "Case6 测试失败！");
	// }
	//
	// @Test
	// public void deleteSimpleTest7() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case 7: 单品有2个多包装 单品1条码 多包装各1条码 删除成功");
	// //
	// Commodity c = BaseCommodityTest.DataInput.getCommodity();
	// Commodity commCreated =
	// BaseCommodityTest.createCommodity(BaseBO.INVALID_CASE_ID, c);
	// //
	// Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
	// c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
	// c1.setRefCommodityID(commCreated.getID());
	// c1.setRefCommodityMultiple(3);
	// createCommodity(BaseBO.CASE_Commodity_CreateMultiPackaging, c1);
	// //
	// Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
	// c2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
	// c2.setRefCommodityID(commCreated.getID());
	// c2.setRefCommodityMultiple(3);
	// createCommodity(BaseBO.CASE_Commodity_CreateMultiPackaging, c2);
	//
	// Map<String, Object> paramForDelete =
	// commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// commodityMapper.deleteSimple(paramForDelete);
	// //
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError);
	// // 查询删除的这条数据
	// commCreated.setInt1(RETRIEVE1_DELETED_COMMODITY);
	// Map<String, Object> paramsRetrieve7 =
	// commCreated.getRetrieve1Param(BaseBO.INVALID_CASE_ID, commCreated);
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Commodity commRetrived7 = (Commodity)
	// commodityMapper.retrieve1(paramsRetrieve7);// ...
	// //
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoSuchData,
	// paramsRetrieve7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// Assert.assertNull(commRetrived7, "Case7 测试失败！");
	// }
	//
	// @Test
	// public void deleteSimpleTest8() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case 8: 单品有2个多包装 单品2条码 多包装各2条码 删除成功");
	// //
	// Commodity c = BaseCommodityTest.DataInput.getCommodity();
	// Commodity commCreated =
	// BaseCommodityTest.createCommodity(BaseBO.INVALID_CASE_ID, c);
	// //
	// Barcodes b = DataInput.getBarcodes();
	// b.setCommodityID(commCreated.getID());
	// createBarcodes(b);
	// //
	// Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
	// c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
	// c1.setRefCommodityID(commCreated.getID());
	// c1.setRefCommodityMultiple(3);
	// Commodity commCreated1 =
	// createCommodity(BaseBO.CASE_Commodity_CreateMultiPackaging, c1);
	// //
	// Barcodes b1 = DataInput.getBarcodes();
	// b1.setCommodityID(commCreated1.getID());
	// createBarcodes(b1);
	// //
	// Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
	// c2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
	// c2.setRefCommodityID(commCreated.getID());
	// c2.setRefCommodityMultiple(3);
	// Commodity commCreated2 =
	// createCommodity(BaseBO.CASE_Commodity_CreateMultiPackaging, c2);
	// //
	// Barcodes b2 = DataInput.getBarcodes();
	// b2.setCommodityID(commCreated2.getID());
	// createBarcodes(b2);
	//
	// Map<String, Object> paramForDelete8 =
	// commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// commodityMapper.deleteSimple(paramForDelete8);
	// //
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError);
	// // 查询删除的这条数据
	// commCreated.setInt1(RETRIEVE1_DELETED_COMMODITY);
	// Map<String, Object> paramsRetrieve8 =
	// commCreated.getRetrieve1Param(BaseBO.INVALID_CASE_ID, commCreated);
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Commodity commRetrived8 = (Commodity)
	// commodityMapper.retrieve1(paramsRetrieve8);// ...
	// //
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoSuchData,
	// paramsRetrieve8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// Assert.assertNull(commRetrived8, "Case8 测试失败！");
	// }

	@Test
	public void deleteSimpleTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 9: 该商品是组合商品，删除失败");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateComposition);

		Map<String, Object> paramForDelete9 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete9);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的这条数据
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case9 测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 10: 单品或多包装存在销存记录 采购单商品有记录 删除商品及条码失败 ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		// 采购该商品产生存在的记录
		PurchasingOrderCommodity pocCreate = BasePurchasingOrderTest.createPurchasingOrderViaMapper(commCreated);
		//
		Map<String, Object> paramForDelete10 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete10);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete10.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询删除的这条数据
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, comm + "没数据");
		//
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(pocCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 11: 单品或多包装有销存记录，盘点商品有记录 删除商品及条码失败 ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodityViaMapper(commCreated.getID());

		Map<String, Object> paramForDelete11 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete11);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete11.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询删除的这条数据
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case11 测试失败！");
		//
		BaseInventorySheetTest.deleteInventoryCommodity(icCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 12: 单品或多包装有销存记录，零售商品有记录 删除商品及条码失败 ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		BaseRetailTradeTest.createRetailTradeCommodityViaMapper(commCreated.getID());

		Map<String, Object> paramForDelete12 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete12);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete12.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的这条数据
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case12 测试失败！");
		// 删除不了零售商品，所以也不能删除单品
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);

		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteSimpleTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 13: 单品或多包装有销存记录，入库商品有记录 删除商品及条码失败 ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		WarehousingCommodity wcCreated = BaseWarehousingTest.createWarehousingCommodityViaMapper(commCreated.getID());

		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的这条数据
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case13 测试失败！");
		//
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcCreated);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);

	}

	@Test
	public void deleteSimpleTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 14: 单品或多包装有销存记录，商品有库存 删除商品及条码失败 ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setNO(100);
		c.setnOStart(100);
		c.setPurchasingPriceStart(100D);
		c.setLatestPricePurchase(c.getPurchasingPriceStart()); // 期初商品latestPricePurchase要等于purchasingPriceStart
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);

		Map<String, Object> paramForDelete14 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete14);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete14.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete14.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询删除的这条数据
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case14 测试失败！数据为null");

		// 商品有库存,删除不了这条创建的商品
		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteSimpleTest15() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15:传入多包装的Id 单品有销存记录、多包装无销存记录 删除商品及条码失败 ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		WarehousingCommodity wcCreated = BaseWarehousingTest.createWarehousingCommodityViaMapper(commCreated.getID());
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);

		Map<String, Object> paramForDelete15 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete15);
		System.out.println(EnumErrorCode.values()[Integer.parseInt(paramForDelete15.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete15.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的这条数据
		commCreated1.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case15 测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcCreated);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest16() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 16: 重复删除商品");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// 重复删除该商品
		Map<String, Object> paramForDelete2 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);

	}

	@Test
	public void deleteSimpleTest17() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 17: 删除不存在的商品");
		//
		Commodity c = new Commodity();
		c.setID(BaseAction.INVALID_ID);
		Map<String, Object> paramForDelete = c.getDeleteParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void deleteSimpleTest18() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 18_1:  商品有促销依赖，促销没有被删除，那么该商品不可以被删除(促销为指定范围促销)");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(commCreated.getID());
		ps.setPromotionID(pCreate.getID());
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_NoError);
		// 尝试删除商品，删除失败
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Shared.caseLog("Case 18_2:商品有促销依赖，但是促销已经被删除(状态变为1)，商品又无其他依赖，那么该商品还是不可以被删除(促销为指定范围促销)");
		// 删除促销
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		// 尝试删除商品，删除失败
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteSimpleTest19() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 19_1:  商品与采购商品有依赖，如果该采购商品的主表采购订单被删除后，这个商品可以被删除");
		// 创建一个采购订单
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrder = BasePurchasingOrderTest.createPurchasingOrderViaMapper(po);
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 根据商品ID创建一个条形码t1
		Barcodes barcode1 = BaseBarcodesTest.retrieveNBarcodes(commCreated.getID(), Shared.DBName_Test);
		// 根据商品和条形码创建一个采购订单商品
		PurchasingOrderCommodity pOrderComm = new PurchasingOrderCommodity();
		pOrderComm.setCommodityID(commCreated.getID());
		pOrderComm.setPurchasingOrderID(purchasingOrder.getID());
		pOrderComm.setCommodityNO(Math.abs(new Random().nextInt(300)));
		pOrderComm.setPriceSuggestion(1);
		pOrderComm.setBarcodeID(barcode1.getID());
		//
		Map<String, Object> createpOrderCommparams = pOrderComm.getCreateParam(BaseBO.INVALID_CASE_ID, pOrderComm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(createpOrderCommparams);
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(createpOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		pOrderComm.setIgnoreIDInComparision(true);
		if (pOrderComm.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 尝试删除商品，删除失败
		commCreated.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除该采购订单，会把从表信息删除掉
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrder);
		// 然后再删除该单品，删除成功
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		commodityMapper.deleteSimple(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteSimpleTest20() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 20_1: 商品有全场促销依赖，促销没有被删除，那么该商品也可以被删除");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(0);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 尝试删除商品，删除成功
		commCreated.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除促销
		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void deleteSimpleTest21() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 21: 商品有全场促销依赖，促销被删除，那么该商品也可以被删除");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(0);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 删除促销
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		// 尝试删除商品，删除成功
		commCreated.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
	
	@Test
	public void deleteSimpleTest22() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 22:  商品有促销依赖，促销没有被删除，那么该商品不可以被删除(促销为指定范围促销), 它的条形码也不会被删除");
		// 创建促销
		Promotion promotionGet = BasePromotionTest.DataInput.getPromotion();
		Promotion promotionCreated = BasePromotionTest.createPromotionViaMapper(promotionGet);
		// 创建商品
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建条形码的
		Barcodes barcodesGet1 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreated1 = BaseBarcodesTest.createBarcodesViaMapper(barcodesGet1, BaseBO.INVALID_CASE_ID);
		// 创建促销范围
		PromotionScope promotionScope = new PromotionScope();
		promotionScope.setCommodityID(commCreated.getID());
		promotionScope.setPromotionID(promotionCreated.getID());
		BasePromotionTest.createPromotionScopeViaMapper(promotionScope, EnumErrorCode.EC_NoError);
		// 尝试删除商品，删除失败
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证条形码是否被删除
		BaseBarcodesTest.retrieve1ViaMapper(barcodesCreated1.getID(), Shared.DBName_Test);
	}
	
	@Test
	public void deleteSimpleTest23() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 23:商品有优惠券范围依赖，不能删除(优惠券已启用)");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 已启用
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// 创建优惠券范围
		CouponScope couponScopeGet = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), simpleCommodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScopeGet);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除测试数据
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
	}
	
	
	@Test
	public void deleteSimpleTest24() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 24:商品有优惠券范围依赖，不能删除(优惠券未启用)");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 未启用
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), 1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// 创建优惠券范围
		CouponScope couponScopeGet = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), simpleCommodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScopeGet);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除测试数据
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void deleteSimpleTest25() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 25:商品有优惠券范围依赖，不能删除(优惠券已过期)");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 已过期
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -5));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), -1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// 创建优惠券范围
		CouponScope couponScopeGet = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), simpleCommodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScopeGet);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除测试数据
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void deleteSimpleTest26() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 26:商品有优惠券范围依赖，不能删除(优惠券已删除)");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 已启用
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// 创建优惠券范围
		CouponScope couponScopeGet = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), simpleCommodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScopeGet);
		// 删除优惠券
		BaseCouponTest.deleteViaMapper(couponCreate);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除测试数据
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void deleteSimpleTest27() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 27:商品有优惠券全场依赖，能被删除");
		// 创建普通商品
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建优惠券
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// 已启用
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// 删除新建的普通商品，删除失败
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
		// 删除优惠券
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void deleteCombinationTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:传入单品,删除失败");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Map<String, Object> paramForDelete1 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		commodityMapper.deleteCombination(paramForDelete1);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询一下删除这条数据
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case1 测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteCombinationTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:该商品是组合商品 正常删除");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateComposition);
		//
		commCreated.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete2 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteCombination(paramForDelete2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询删除的这条数据
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case2 测试失败！");
		//
		BaseCommodityTest.retrieveNCommodityHistory(commCreated.getID(), c.getOperatorStaffID(), commodityHistoryMapper);
	}

	@Test
	public void deleteCombinationTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:传入组合商品ID，组合商品零售有记录");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateComposition);
		//
		BaseRetailTradeTest.createRetailTradeCommodityViaMapper(commCreated.getID());
		//
		Map<String, Object> paramForDelete3 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteCombination(paramForDelete3);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询删除的这条数据
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case3 测试失败！");
		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteCombinationTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:传入多包装的Id,删除失败");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Map<String, Object> paramForDelete4 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteCombination(paramForDelete4);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的这条数据
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case4 测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	// 组合商品是没有库存的,所以做不到这case的前提条件“传入库存不为0的组合商品Id”
	// @Test
	// public void deleteCombinationTest5() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case5:传入库存不为0的组合商品Id，删除失败");// 组合商品是没有库存的，所以在创建组合商品时不传库存NO字段
	// //
	// Commodity c = BaseCommodityTest.DataInput.getCommodity();
	// c.setType(EnumCommodityType.ECT_Combination.getIndex());
	// // c.setNO(1);
	// Commodity commCreated =
	// createCommodity(BaseBO.CASE_Commodity_CreateComposition, c);
	// //
	// Map<String, Object> paramForDelete5 =
	// commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// commodityMapper.deleteCombination(paramForDelete5);
	// //
	// //
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// // == EnumErrorCode.EC_BusinessLogicNotDefined);
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoError);
	// // 查询删除的这条数据
	// Map<String, Object> paramsRetrieve5 =
	// commCreated.getRetrieve1Param(BaseBO.INVALID_CASE_ID, commCreated);
	// //
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Commodity commRetrived5 = (Commodity)
	// commodityMapper.retrieve1Ex(paramsRetrieve5);// ...
	// //
	// //
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// // == EnumErrorCode.EC_NoError,
	// // paramsRetrieve5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoSuchData,
	// paramsRetrieve5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// // Assert.assertNotNull(commRetrived5, "Case5 测试失败！");
	// Assert.assertNull(commRetrived5, "测试失败");
	// // 删除条形码同步块缓存
	// deleteAllBarcodesSyncCache();
	// }

	@Test
	public void deleteCombinationTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:重复删除组合商品");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateComposition);
		//
		commCreated.setOperatorStaffID(c.getOperatorStaffID());
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteCombination(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询删除的这条数据
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case2 测试失败！");
		// 重复删除
		Map<String, Object> paramForDelete2 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteCombination(paramForDelete2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteCombinationTest7() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case7:删除不存在的商品");
		Commodity c = new Commodity();
		c.setID(BaseAction.INVALID_ID);
		//
		Map<String, Object> deleteParam = c.getDeleteParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteCombination(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteMultiPackagingTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		// @Transactional 数据回滚，防止commodity87~92 的barcodes被删除，在barcodes mapper test那里有用到
		Shared.caseLog("Case1:删除单品,删除失败");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Map<String, Object> paramForDelete1 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		commodityMapper.deleteMultiPackaging(paramForDelete1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的这条数据 删除失败数据存在 所以数据存在
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case1 测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteMultiPackagingTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:该商品是组合商品,删除失败");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateComposition);
		//
		Map<String, Object> paramForDelete2 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(paramForDelete2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的这条数据 删除失败数据存在 所以数据存在
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case2 测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteMultiPackagingTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:多包装存在销存记录 采购单商品有记录");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		// 创建多包装商品销存记录，创建一张零售单
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// 创建对应的零售单商品
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setTradeID(rtCreate.getID());
		rtc.setCommodityID(commCreated1.getID());
		rtc.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params); // ...
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(retailTradeCommodityCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		Map<String, Object> paramForDelete3 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(paramForDelete3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 查询删除的这条数据 含有关联关系，故删除失败数据存在 所以数据存在
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case3 测试失败！");
		//
		// 商品有销存记录无法删除，只删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteMultiPackagingTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:多包装有销存记录，零售商品有记录");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		BaseRetailTradeTest.createRetailTradeCommodityViaMapper(commCreated1.getID());
		//
		Map<String, Object> paramForDelete5 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(paramForDelete5);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的这条数据 含有关联关系，故删除失败数据存在 所以数据存在
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case5 测试失败！");

		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteMultiPackagingTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:传入多包装的Id 正常删除");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		commCreated1.setOperatorStaffID(c1.getOperatorStaffID());
		Map<String, Object> paramForDelete7 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(paramForDelete7);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 查询删除的这条数据 没有任何关联关系且库存为0，故删除数据成功
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case7 测试失败！");
		//
		BaseCommodityTest.retrieveNCommodityHistory(commCreated1.getID(), c1.getOperatorStaffID(), commodityHistoryMapper);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteMultiPackagingTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:重复删除多包装商品");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		commCreated1.setOperatorStaffID(c1.getOperatorStaffID());
		Map<String, Object> paramForDelete = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 查询删除的这条数据 没有任何关联关系且库存为0，故删除数据成功
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case7 测试失败！");
		// 重复删除
		Map<String, Object> paramForDelete2 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(paramForDelete2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);

		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteMultiPackagingTest8() {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case8:删除不存在的商品");
		Commodity c = new Commodity();
		c.setID(BaseAction.INVALID_ID);
		//
		Map<String, Object> deleteParam = c.getDeleteParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(deleteParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void deleteServiceTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:对组合商品进行删除");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateComposition);
		//
		Map<String, Object> paramForDelete2 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteService(paramForDelete2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除失败，则可以查找出这个商品
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteServiceTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2：对多包装商品进行删除");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		commCreated1.setOperatorStaffID(c1.getOperatorStaffID());
		Map<String, Object> paramForDelete2 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteService(paramForDelete2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除失败，则可以查找出这个商品
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteServiceTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:对普通商品进行删除");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Map<String, Object> deleteParam = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteService(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除失败，则可以查找出这个商品
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "测试失败！");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteServiceTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:对服务商品进行删除");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setShelfLife(0);
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c.setPurchaseFlag(0);
		Commodity createCommodity = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateService);
		//
		createCommodity.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteParam = createCommodity.getDeleteParam(BaseBO.INVALID_CASE_ID, createCommodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteService(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BaseCommodityTest.retrieve1ExCommodity(createCommodity, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void deleteServiceTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:对服务商品重复删除");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setShelfLife(0);
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c.setPurchaseFlag(0);
		Commodity createCommodity = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateService);
		//
		createCommodity.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteParam = createCommodity.getDeleteParam(BaseBO.INVALID_CASE_ID, createCommodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteService(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BaseCommodityTest.retrieve1ExCommodity(createCommodity, EnumErrorCode.EC_NoSuchData);
		// 重复进行删除
		Map<String, Object> deleteParam2 = createCommodity.getDeleteParam(BaseBO.INVALID_CASE_ID, createCommodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteService(deleteParam2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteServiceTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case6:对不存在的商品进行删除");
		Commodity c = new Commodity();
		c.setID(BaseAction.INVALID_ID);
		//
		Map<String, Object> deleteParam = c.getDeleteParam(BaseBO.INVALID_CASE_ID, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteService(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteServiceTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case7:零售商品有记录 删除商品及条码失败");

		Commodity c = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		c.setShelfLife(0);
		c.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		c.setPurchaseFlag(0);
		Commodity createCommodity = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateService);
		//
		BaseRetailTradeTest.createRetailTradeCommodityViaMapper(createCommodity.getID());
		//
		createCommodity.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteParam = createCommodity.getDeleteParam(BaseBO.INVALID_CASE_ID, createCommodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteService(deleteParam);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, deleteParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveNMultiPackageCommodityTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常查询");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c2.setRefCommodityID(commCreated.getID());
		c2.setRefCommodityMultiple(4);
		c2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated3 = BaseCommodityTest.createCommodityViaMapper(c2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_RetrieveNMultiPackageCommodity, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNMultiPackageCommodity = commodityMapper.retrieveNMultiPackageCommodity(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieveNMultiPackageCommodity.size() != 0, "查询不到数据");
		//
		for (BaseModel bm : retrieveNMultiPackageCommodity) {
			Commodity commodity = (Commodity) bm;
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(c.getOperatorStaffID());
			commodity.setBarcodes(c.getBarcodes());
			commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
			commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
			Assert.assertEquals(error, "");
			commodity.setOperatorStaffID(staffID);
			commodity.setBarcodes(null);
		}
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated3, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNMultiPackageCommodityTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:查询不存在的商品的多包装商品");
		Commodity c = new Commodity();
		c.setID(BaseAction.INVALID_ID);
		//
		Map<String, Object> params = c.getRetrieveNParam(BaseBO.CASE_RetrieveNMultiPackageCommodity, c);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNMultiPackageCommodity = commodityMapper.retrieveNMultiPackageCommodity(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieveNMultiPackageCommodity.size() == 0);
		//
		for (BaseModel bm : retrieveNMultiPackageCommodity) {
			Commodity commodity = (Commodity) bm;
			commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
			commodity.setnOStart(Commodity.NO_START_Default);
			commodity.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
			//
			String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
			Assert.assertEquals(error, "");
		}
	}

	@Test
	public void checkDependencyTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:商品未被使用");
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		// 删除创建出来的测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkDependencyTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "商品数量不为0不能删除";
		Shared.caseLog("Case2:" + message);
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setnOStart(100);
		comm.setNO(100);
		comm.setPurchasingPriceStart(100D);
		comm.setLatestPricePurchase(comm.getPurchasingPriceStart()); // 期初商品latestPricePurchase要等于purchasingPriceStart
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkDependencyTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "商品在RetailtradeCommodity有依赖";
		Shared.caseLog("Case3:" + message);
		// 先创建一个商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreate = BaseCommodityTest.retrieveNBarcodesViaMapper(commCreated);
		// 创建一个零售单商品
		Warehousing warehousingGet = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing WarehousingCreated = BaseWarehousingTest.createViaMapper(warehousingGet);
		WarehousingCommodity warehousingCommodity = BaseWarehousingTest.createWarehousingCommodityViaMapper(WarehousingCreated.getID(), commCreated.getID(), 30, barcodesCreate.getID(), 11, 11, 365);
		WarehousingCreated.setApproverID(EnumTypeRole.ETR_Boss.getIndex());
		BaseWarehousingTest.approveViaMapper(WarehousingCreated);
		BaseRetailTradeTest.createRetailTradeCommodityViaMapper(commCreated.getID(), warehousingCommodity.getNO());
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkDependencyTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "商品在PurchasingOrderCommodity有依赖";
		Shared.caseLog("Case4:" + message);
		// 先创建一个商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		// 创建一个采购订单商品
		PurchasingOrderCommodity purchasingOrderCommodity = BasePurchasingOrderTest.createPurchasingOrderViaMapper(commCreated);
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建出来的测试对象
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(purchasingOrderCommodity);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkDependencyTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "商品在InventoryCommodity有依赖";
		Shared.caseLog("Case5:" + message);
		// 先创建一个商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		// 创建一个盘点单商品
		InventoryCommodity inventoryCommodity = BaseInventorySheetTest.createInventoryCommodityViaMapper(commCreated.getID());
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建出来的测试对象
		BaseInventorySheetTest.deleteInventoryCommodity(inventoryCommodity);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkDependencyTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "商品在WarehousingCommodity有依赖";
		Shared.caseLog("Case6:" + message);
		// 先创建一个商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		// 创建一个入库单商品
		WarehousingCommodity warehousingCommodity = BaseWarehousingTest.createWarehousingCommodityViaMapper(commCreated.getID());
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建出来的测试对象
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(warehousingCommodity);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void updatePurchasingUnitTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:修改采购单位");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		commCreated.setID(commCreated.getID());
		commCreated.setPurchasingUnit("盒子");
		//
		String error = commCreated.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> param = commCreated.getUpdateParam(BaseBO.CASE_UpdatePurchasingUnit, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity updatePurchasingUnit = (Commodity) commodityMapper.updatePurchasingUnit(param);
		//
		Assert.assertNotNull(updatePurchasingUnit);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		commCreated.setIgnoreIDInComparision(true);
		// 更新后对比数据不一致返回-1，一致返回0
		commCreated.setIgnoreSlaveListInComparision(true);
		if (commCreated.compareTo(updatePurchasingUnit) != 0) {
			Assert.assertTrue(false, "对象的字段与DB读出的不相等");
		}
		//
		int staffID = updatePurchasingUnit.getOperatorStaffID();
		updatePurchasingUnit.setOperatorStaffID(c.getOperatorStaffID());
		updatePurchasingUnit.setBarcodes(c.getBarcodes());
		updatePurchasingUnit.setnOStart(Commodity.NO_START_Default);
		updatePurchasingUnit.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		String error1 = updatePurchasingUnit.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		updatePurchasingUnit.setOperatorStaffID(staffID);
		updatePurchasingUnit.setBarcodes(null);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	// @Test 此方法针对sp内部调用，已经有mapperTest，在java层不需要测试
	public void updateWarehousingTest() throws CloneNotSupportedException {
		// CommodityMapper没有实现这个方法 等待实现进行测试
	}

	@Test
	public void retrieveInventoryTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:查询未删除状态商品的库存");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setNO(100);
		c.setnOStart(100);
		c.setPurchasingPriceStart(100D);
		c.setLatestPricePurchase(c.getPurchasingPriceStart()); // 期初商品latestPricePurchase要等于purchasingPriceStart
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		commCreated.setShopID(2);
		//
		Map<String, Object> retrieve1Param = commCreated.getRetrieve1Param(BaseBO.CASE_Commodity_RetrieveInventory, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity retrieve1 = (Commodity) commodityMapper.retrieveInventory(retrieve1Param);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, retrieve1Param.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertNotNull(retrieve1);
		// 只返回数量
		// String error = retrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
		// 在创建商品时，是不传NO字段，NO的赋值是当nOStart的值大于0时，把nOStart赋值给NO的，如果nOStart等于-1时，NO是等于0的
		// TODO NO放在了listSlave2里面，比较listSlave2
		Assert.assertTrue(commCreated.getnOStart() == retrieve1.getNO(), "查询出的库存不正确");

		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveInventoryTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2：查询已经被删除的商品。返回错误码2");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		commCreated.setShopID(2);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		//
		Map<String, Object> retrieve1Param2 = commCreated.getRetrieve1Param(BaseBO.CASE_Commodity_RetrieveInventory, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity retrieve1 = (Commodity) commodityMapper.retrieveInventory(retrieve1Param2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Param2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, retrieve1Param2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(retrieve1);
	}

	@Test
	public void retrieveInventoryTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:查询不存在的商品ID");
		//
		Commodity comm = new Commodity();
		comm.setID(BaseAction.INVALID_ID);
		comm.setShopID(2);
		Map<String, Object> retrieve1Param3 = comm.getRetrieve1Param(BaseBO.CASE_Commodity_RetrieveInventory, comm);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity retrieve1 = (Commodity) commodityMapper.retrieveInventory(retrieve1Param3);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Param3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, retrieve1Param3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(retrieve1);
	}

	@Test
	public void checkUniqueFieldTest1() throws Exception {
		Shared.caseLog("Case1:查询一个不存在的商品名称");
		//
		Commodity commodity = new Commodity();
		commodity.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity.setUniqueField("不重复的商品名称");
		//
		String error1 = commodity.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params = commodity.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, commodity);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkUniqueField(params);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkUniqueFieldTest2() throws Exception {
		Shared.caseLog("Case2:查询一个已经存在的商品名称");
		//
		Commodity commodity1 = new Commodity();
		commodity1.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity1.setUniqueField("可比克薯片");
		//
		String error1 = commodity1.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params1 = commodity1.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, commodity1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkUniqueField(params1);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void checkUniqueFieldTest3() throws Exception {
		Shared.caseLog("Case3:查询一个已经被删除的商品名称");
		//
		Commodity commodity2 = new Commodity();
		commodity2.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity2.setUniqueField("百事青椒味薯片1");
		//
		String error1 = commodity2.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params2 = commodity2.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, commodity2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkUniqueField(params2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkUniqueFieldTest4() throws Exception {
		Shared.caseLog("Case4:查询一个已经存在的商品名称,但传入的ID是这个已存在的商品名称的商品ID");
		//
		Commodity commodity4 = new Commodity();
		commodity4.setID(1);
		commodity4.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity4.setUniqueField("可比克薯片");
		//
		String error1 = commodity4.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params4 = commodity4.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, commodity4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkUniqueField(params4);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkUniqueFieldTest5() throws Exception {
		Shared.caseLog("Case5:查询一个已经存在的商品名称,传入的ID不是这个已存在的商品名称的商品ID");
		//
		Commodity commodity5 = new Commodity();
		commodity5.setID(2);
		commodity5.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity5.setUniqueField("可比克薯片");
		//
		String error1 = commodity5.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params5 = commodity5.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, commodity5);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkUniqueField(params5);
		//
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
	}

	@Test
	public void checkUniqueFieldTest6() throws Exception {
		Shared.caseLog("Case3:查询一个已经被删除的商品名称,传入的ID是这个已存在的商品名称的商品ID");
		//
		Commodity commodity6 = new Commodity();
		commodity6.setID(49);
		commodity6.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity6.setUniqueField("百事青椒味薯片1");
		//
		String error1 = commodity6.checkRetrieveN(BaseBO.CASE_CheckUniqueField);
		Assert.assertEquals(error1, "");
		//
		Map<String, Object> params6 = commodity6.getRetrieveNParam(BaseBO.CASE_CheckUniqueField, commodity6);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkUniqueField(params6);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void testUnsalableCommodityRetrieveN1() throws Exception {
		Shared.caseLog("Case1:正常查询");
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Date nowDate = new Date();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList = commodityMapper.retrieveNUnsalableCommodityEx(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(bmList.get(1).size() > 0);
	}

	@Test
	public void testUnsalableCommodityRetrieveN2() throws Exception {
		Shared.caseLog("Case2:传入错误的消息分类ID");
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Date nowDate = new Date();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(Shared.BIG_ID);
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList = commodityMapper.retrieveNUnsalableCommodityEx(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(bmList.size() == 0);
	}

	@Test
	public void testUnsalableCommodityRetrieveN3() throws Exception {
		Shared.caseLog("Case3:传入错误的公司ID");
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Date nowDate = new Date();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(Shared.BIG_ID);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList = commodityMapper.retrieveNUnsalableCommodityEx(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(bmList.size() == 0);
	}

	@Test
	public void testUnsalableCommodityRetrieveN4() throws Exception {
		Shared.caseLog("Case4:新建一个商品,如果商品的零售时间在dStartDate、dEndDate之间,则该商品不是滞销商品");
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -29); // 开始时间为现在时间的29天前
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单A的商品(期初商品，因为要有库存)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setnOStart(100);
		comm.setPurchasingPriceStart(100);
		comm.setStartValueRemark("aaaaaaaaaaaaaa");
		//
		String error = comm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Map<String, Object> paramForComm = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(paramForComm);
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramForComm.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramForComm.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Warehousing warehousing = (Warehousing) bmList.get(3).get(0);
		BaseCommodityTest.setCommodityShopInfo(commCreated, comm, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, warehousing);
		List<CommodityShopInfo> listCommodityShopInfo = BaseCommodityTest.createListCommodityShopInfoViaMapper(commCreated, comm, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, warehousing);
		commCreated.setListSlave2(listCommodityShopInfo);
		comm.setIgnoreIDInComparision(true);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		int staffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodes = ((Barcodes) bmList.get(1).get(0)).getBarcode();
		commCreated.setBarcodes(barcodes);
		commCreated.setnOStart(comm.getnOStart());
		commCreated.setPurchasingPriceStart(comm.getPurchasingPriceStart());
		String commodityError = commCreated.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(commodityError, "");
		commCreated.setOperatorStaffID(staffID);
		commCreated.setBarcodes(null);
		//
		// 由于目前商品都需要有一个供应商，所以创建一个供应商商品
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		Barcodes barcodes1 = (Barcodes) bmList.get(1).get(0);
		Assert.assertTrue(barcodes1 != null && barcodes1.getCommodityID() == commCreated.getID() && barcodes1.getBarcode().equals(comm.getBarcodes()));
		//
		barcodes1.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodesError = barcodes1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(barcodesError, "");
		//
		Assert.assertTrue(warehousing != null);
		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bmList.get(4).get(0);
		Assert.assertTrue(warehousingCommodity != null && warehousingCommodity.getWarehousingID() == warehousing.getID() && warehousingCommodity.getCommodityID() == commCreated.getID());
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "期初数量不正确");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "期初采购价不正确");
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(commCreated.getID());
		retailTradeCommodity.setCommodityName(commCreated.getName());
		retailTradeCommodity.setBarcodeID(barcodes1.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsRN = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList2 = commodityMapper.retrieveNUnsalableCommodityEx(paramsRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		List<BaseModel> commList = bmList2.get(0);
		boolean notExist = true;
		for (BaseModel bm : commList) {
			if (bm.getID() == commCreated.getID()) {
				notExist = false;
			}
		}
		Assert.assertTrue(notExist, "预期新建的商品不是滞销商品，但是实际结果是滞销商品");
		// 期初商品有记录，删不掉
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void testUnsalableCommodityRetrieveN5() throws Exception {
		Shared.caseLog("Case5:新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,则该商品是滞销商品");
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // 开始时间为现在时间的29天前
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单A的商品(期初商品，因为要有库存)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setnOStart(100);
		comm.setPurchasingPriceStart(100);
		comm.setStartValueRemark("aaaaaaaaaaaaaa");
		//
		String error = comm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Map<String, Object> paramForComm = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(paramForComm);
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramForComm.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramForComm.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Warehousing warehousing = (Warehousing) bmList.get(3).get(0);
		BaseCommodityTest.setCommodityShopInfo(commCreated, comm, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, warehousing);
		List<CommodityShopInfo> listCommodityShopInfo = BaseCommodityTest.createListCommodityShopInfoViaMapper(commCreated, comm, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, warehousing);
		commCreated.setListSlave2(listCommodityShopInfo);
		comm.setIgnoreIDInComparision(true);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		int staffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodes = ((Barcodes) bmList.get(1).get(0)).getBarcode();
		commCreated.setBarcodes(barcodes);
		commCreated.setnOStart(comm.getnOStart());
		commCreated.setPurchasingPriceStart(comm.getPurchasingPriceStart());
		String commodityError = commCreated.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(commodityError, "");
		commCreated.setOperatorStaffID(staffID);
		commCreated.setBarcodes(null);
		//
		// 由于目前商品都需要有一个供应商，所以创建一个供应商商品
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		Barcodes barcodes1 = (Barcodes) bmList.get(1).get(0);
		Assert.assertTrue(barcodes1 != null && barcodes1.getCommodityID() == commCreated.getID() && barcodes1.getBarcode().equals(comm.getBarcodes()));
		//
		barcodes1.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodesError = barcodes1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(barcodesError, "");
		//
//		Warehousing warehousing = (Warehousing) bmList.get(3).get(0);
		Assert.assertTrue(warehousing != null);
		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bmList.get(4).get(0);
		Assert.assertTrue(warehousingCommodity != null && warehousingCommodity.getWarehousingID() == warehousing.getID() && warehousingCommodity.getCommodityID() == commCreated.getID());
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "期初数量不正确");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "期初采购价不正确");
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(commCreated.getID());
		retailTradeCommodity.setCommodityName(commCreated.getName());
		retailTradeCommodity.setBarcodeID(barcodes1.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsRN = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList2 = commodityMapper.retrieveNUnsalableCommodityEx(paramsRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		List<BaseModel> commList = bmList2.get(0);
		boolean exist = false;
		for (BaseModel bm : commList) {
			if (bm.getID() == commCreated.getID()) {
				exist = true;
			}
		}
		Assert.assertTrue(exist, "预期新建的商品是滞销商品，但是实际结果不是滞销商品");
		// 期初商品有记录，删不掉
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void testUnsalableCommodityRetrieveN6() throws Exception {
		Shared.caseLog("Case6:新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是退货时间是在这两个日期之间，则该商品还是滞销商品");
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // 开始时间为现在时间的29天前
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单A的商品(期初商品，因为要有库存)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setnOStart(100);
		comm.setPurchasingPriceStart(100);
		comm.setStartValueRemark("aaaaaaaaaaaaaa");
		//
		String error = comm.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		Map<String, Object> paramForComm = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(paramForComm);
		Assert.assertTrue(bmList.size() != 0 && EnumErrorCode.values()[Integer.parseInt(paramForComm.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramForComm.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Commodity commCreated = (Commodity) bmList.get(0).get(0);
		Warehousing warehousing = (Warehousing) bmList.get(3).get(0);
		BaseCommodityTest.setCommodityShopInfo(commCreated, comm, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, warehousing);
		List<CommodityShopInfo> listCommodityShopInfo = BaseCommodityTest.createListCommodityShopInfoViaMapper(commCreated, comm, BaseBO.CASE_Commodity_CreateSingle, Shared.DBName_Test, warehousing);
		commCreated.setListSlave2(listCommodityShopInfo);
		comm.setIgnoreIDInComparision(true);
		if (comm.compareTo(commCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		int staffID = commCreated.getOperatorStaffID();
		commCreated.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodes = ((Barcodes) bmList.get(1).get(0)).getBarcode();
		commCreated.setBarcodes(barcodes);
		commCreated.setnOStart(comm.getnOStart());
		commCreated.setPurchasingPriceStart(comm.getPurchasingPriceStart());
		String commodityError = commCreated.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(commodityError, "");
		commCreated.setOperatorStaffID(staffID);
		commCreated.setBarcodes(null);
		//
		// 由于目前商品都需要有一个供应商，所以创建一个供应商商品
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		//
		Barcodes barcodes1 = (Barcodes) bmList.get(1).get(0);
		Assert.assertTrue(barcodes1 != null && barcodes1.getCommodityID() == commCreated.getID() && barcodes1.getBarcode().equals(comm.getBarcodes()));
		//
		barcodes1.setOperatorStaffID(comm.getOperatorStaffID());
		String barcodesError = barcodes1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(barcodesError, "");
		//
		Assert.assertTrue(warehousing != null);
		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bmList.get(4).get(0);
		Assert.assertTrue(warehousingCommodity != null && warehousingCommodity.getWarehousingID() == warehousing.getID() && warehousingCommodity.getCommodityID() == commCreated.getID());
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "期初数量不正确");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "期初采购价不正确");
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(commCreated.getID());
		retailTradeCommodity.setCommodityName(commCreated.getName());
		retailTradeCommodity.setBarcodeID(barcodes1.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 新建退货型零售单B，对零售A退货,退货时间在dStartDate、dEndDate之间
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSn(Shared.generateRetailTradeSN(2));
		returnRetailTrade.setSourceID(rTrade.getID());
		Calendar returnSalTime = Calendar.getInstance();
		returnSalTime.setTime(nowDate);
		returnSalTime.add(Calendar.DATE, -29); // 开始时间为现在时间的29天前
		returnRetailTrade.setSaleDatetime(returnSalTime.getTime());
		RetailTrade returnTrade = BaseRetailTradeTest.createRetailTrade(returnRetailTrade);
		//
		RetailTradeCommodity returnRetailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		returnRetailTradeCommodity.setPriceReturn(2d);
		returnRetailTradeCommodity.setTradeID(returnTrade.getID());
		returnRetailTradeCommodity.setCommodityID(commCreated.getID());
		returnRetailTradeCommodity.setCommodityName(commCreated.getName());
		returnRetailTradeCommodity.setBarcodeID(barcodes1.getID());
		returnRetailTradeCommodity.setNO(15);
		returnRetailTradeCommodity.setNOCanReturn(10);
		returnRetailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(returnRetailTradeCommodity, EnumErrorCode.EC_NoError);
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsRN = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList2 = commodityMapper.retrieveNUnsalableCommodityEx(paramsRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		List<BaseModel> commList = bmList2.get(0);
		boolean exist = false;
		for (BaseModel bm : commList) {
			if (bm.getID() == commCreated.getID()) {
				exist = true;
			}
		}
		Assert.assertTrue(exist, "预期新建的商品是滞销商品，但是实际结果不是滞销商品");
		// 期初商品有记录，删不掉
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void testUnsalableCommodityRetrieveN7() throws Exception {
		Shared.caseLog("Case7:新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是该商品的库存为0，则该商品不是滞销商品");
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // 开始时间为现在时间的31天前
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单A的商品(无库存)
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsRN = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList2 = commodityMapper.retrieveNUnsalableCommodityEx(paramsRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		List<BaseModel> commList = bmList2.get(0);
		boolean notExist = true;
		for (BaseModel bm : commList) {
			if (bm.getID() == comm.getID()) {
				notExist = false;
			}
		}
		Assert.assertTrue(notExist, "预期新建的商品不是滞销商品，但是实际结果是滞销商品");
		// 期初商品有记录，删不掉
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// 删除条形码同步块缓存
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void testUnsalableCommodityRetrieveN8() throws Exception {
		Shared.caseLog("Case8:新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是该商品是组合商品，则该商品不是滞销商品");
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // 开始时间为现在时间的31天前
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单A的商品，该商品为组合商品
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(commCreated1.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(commCreated1.getID());
		retailTradeCommodity.setCommodityName(commCreated1.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsRN = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList2 = commodityMapper.retrieveNUnsalableCommodityEx(paramsRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		List<BaseModel> commList = bmList2.get(0);
		boolean notExist = true;
		for (BaseModel bm : commList) {
			if (bm.getID() == commCreated1.getID()) {
				notExist = false;
			}
		}
		Assert.assertTrue(notExist, "预期新建的组合商品不是滞销商品，但是实际结果是滞销商品");
		// 有零售依赖，不能删除
		// BaseCommodityTest.doDeleteCommodity(commCreated1, mappersMap);
	}

	@Test
	public void testUnsalableCommodityRetrieveN9() throws Exception {
		Shared.caseLog("Case9:新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是该商品是多包装商品，则该商品不是滞销商品");
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // 开始时间为现在时间的31天前
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单A的商品，该商品为组合商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm1.setRefCommodityID(commCreated.getID());
		comm1.setRefCommodityMultiple(3);
		comm1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64位条形码" + System.currentTimeMillis() % 1000000 + ";"); // 测试条形码
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(commCreated1.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(commCreated1.getID());
		retailTradeCommodity.setCommodityName(commCreated1.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsRN = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList2 = commodityMapper.retrieveNUnsalableCommodityEx(paramsRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		List<BaseModel> commList = bmList2.get(0);
		boolean notExist = true;
		for (BaseModel bm : commList) {
			if (bm.getID() == commCreated1.getID()) {
				notExist = false;
			}
		}
		Assert.assertTrue(notExist, "预期新建的多包装商品不是滞销商品，但是实际结果是滞销商品");
	}

	@Test
	public void testUnsalableCommodityRetrieveN10() throws Exception {
		Shared.caseLog("Case10:新建一个商品,如果商品的零售时间不在dStartDate、dEndDate之间,但是该商品是服务商品，则该商品不是滞销商品");
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // 开始时间为现在时间的31天前
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单A的商品，该商品为服务商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		comm.setShelfLife(0);
		comm.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		comm.setPurchaseFlag(0);
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateService);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(commCreated1.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(2d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(commCreated1.getID());
		retailTradeCommodity.setCommodityName(commCreated1.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsRN = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList2 = commodityMapper.retrieveNUnsalableCommodityEx(paramsRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		List<BaseModel> commList = bmList2.get(0);
		boolean notExist = true;
		for (BaseModel bm : commList) {
			if (bm.getID() == commCreated1.getID()) {
				notExist = false;
			}
		}
		Assert.assertTrue(notExist, "预期新建的多包装商品不是滞销商品，但是实际结果是滞销商品");
	}
	
	@Test
	public void testUnsalableCommodityRetrieveN11() throws Exception {
		Shared.caseLog("Case11:新建一个商品,该商品零售价为0，商品的零售时间不在dStartDate、dEndDate之间，该商品不是滞销商品");
		// 新建零售单A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // 开始时间为现在时间的31天前
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单A的商品，该商品为服务商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		comm.setShelfLife(0);
		comm.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		comm.setPurchaseFlag(0);
		comm.setPriceRetail(0.000000d);
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateService);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(commCreated1.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setPriceReturn(0.000000d);
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(commCreated1.getID());
		retailTradeCommodity.setCommodityName(commCreated1.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(25);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 查询滞销商品
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // 开始时间为现在时间的前30天
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"商品滞销\"}]");
		unsalableCommRN.setMessageCategoryID(EnumMessageCategory.EMC_UnsalableCommodity.getIndex());
		unsalableCommRN.setCompanyID(1);
		unsalableCommRN.setMessageSenderID(0);
		unsalableCommRN.setMessageReceiverID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsRN = unsalableCommRN.getRetrieveNParamEx(BaseBO.CASE_UnsalableCommodity_RetrieveN, unsalableCommRN);
		List<List<BaseModel>> bmList2 = commodityMapper.retrieveNUnsalableCommodityEx(paramsRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		List<BaseModel> commList = bmList2.get(0);
		boolean notExist = true;
		for (BaseModel bm : commList) {
			if (bm.getID() == commCreated1.getID()) {
				notExist = false;
			}
		}
		Assert.assertTrue(notExist, "预期新建的服务商品不是滞销商品，但是实际结果是滞销商品");
	}
}
