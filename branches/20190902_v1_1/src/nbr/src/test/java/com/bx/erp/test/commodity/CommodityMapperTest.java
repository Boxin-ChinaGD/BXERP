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

	/** ??????????????????????????? */
	public static final int RETRIEVE1_DELETED_COMMODITY = 0;

	/** ????????????????????????????????????????????? */
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
			rcscInput.setSpecification("???");
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
		Shared.caseLog("case1: ??????????????????????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		System.out.println("createTest1?????????????????????ID??????" + commCreated.getID());
		Assert.assertTrue(commCreated.getListSlave2() != null && commCreated.getListSlave2().size() > 0, "?????????????????????????????????????????????");
		// ???????????????????????????
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void createSimpleTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2:???????????????????????????");
		//
		final String commodityName = "??????" + UUID.randomUUID().toString().substring(1, 8);
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName(commodityName);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		System.out.println("createTest2?????????????????????ID??????" + commCreated.getID());
		//
		// ????????????
		String error = commodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createSimpleEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createSimpleTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case3:??????????????????not null??????????????????null");
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
		Shared.caseLog("case4: ?????????????????????");
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
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
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
		// ?????????????????????????????????????????????????????????????????????????????????
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
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
		// //????????????
		// String warehousingError = warehousing.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(warehousingError, "");
		//
		WarehousingCommodity warehousingCommodity = (WarehousingCommodity) bmList.get(4).get(0);
		Assert.assertTrue(warehousingCommodity != null && warehousingCommodity.getWarehousingID() == warehousing.getID() && warehousingCommodity.getCommodityID() == commCreated.getID());
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "?????????????????????");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "????????????????????????");
		// //????????????
		// String warehousingCommodityError
		// =warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(warehousingCommodityError, "");
		//
		// ?????????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void createSimpleTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case5:???????????????,staffID??????????????????4");
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
		Shared.caseLog("case6: ??????????????????????????????ID??????????????????7");
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
		assertTrue("???????????????".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createSimpleTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case7: ??????????????????????????????ID??????????????????7");
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
		assertTrue("???????????????".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createSimpleTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case8: ????????????????????????????????????ID??????????????????7");
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
		assertTrue("?????????????????????".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void createSimpleTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case9: ???????????????????????????????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setName("??????????????????");// ????????????????????????????????????????????????????????????????????????????????????
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
		Shared.caseLog("case10: ????????????????????????????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);

		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// ????????????????????????????????????
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		commodity2.setName(commodity.getName());
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateSingle);
		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createSimpleTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:???????????????????????????????????????????????????");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setnOStart(100);
		comm.setNO(100);
		comm.setPurchasingPriceStart(100);
		comm.setLatestPricePurchase(comm.getPurchasingPriceStart()); // ????????????latestPricePurchase?????????purchasingPriceStart
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
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
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
		// ?????????????????????????????????????????????????????????????????????????????????
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
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
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "?????????????????????");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "????????????????????????");
		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void createSimpleTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case12:???????????????RefCommodityID != 0???????????????");
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
		Shared.caseLog("case13:???????????????RefCommodityMultiple != 0???????????????");
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
		Shared.caseLog("case14: ???????????????????????????????????????*?????????$#/??????");
		//
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		commodityGet.setName("*?????????$#/" + commodityGet.getName());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		System.out.println("createTest1?????????????????????ID??????" + commCreated.getID());
		// ???????????????????????????
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createServiceTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:????????????????????????");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		BaseCommodityTest.deleteCommodityViaMapper(BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateService), EnumErrorCode.EC_NoError);
	}

	@Test
	public void createServiceTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2:?????????????????????????????????");
		//
		final String commodityName = "??????" + UUID.randomUUID().toString().substring(1, 8);
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		commodity.setName(commodityName);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateService);
		//
		// ????????????
		String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateService);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createServiceEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createServiceTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case3:??????????????????not null??????????????????null");
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
		Shared.caseLog("case4:?????????????????????,staffID??????????????????4");
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
		Shared.caseLog("case5: ??????????????????????????????ID??????????????????7");
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
		assertTrue("???????????????".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createServiceTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case6: ??????????????????????????????ID??????????????????7");
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
		assertTrue("???????????????".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createServiceTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case7: ????????????????????????????????????ID??????????????????7");
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
		assertTrue("?????????????????????".equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void createServiceTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case8: ?????????????????????????????????????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		commodity.setName("??????????????????");// ????????????????????????????????????????????????????????????????????????????????????
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
		Shared.caseLog("case9: ??????????????????????????????????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateService);

		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// ????????????????????????????????????
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		commodity2.setName(commodity.getName());
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateService);
		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createServiceTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:???????????????????????????");
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
		Shared.caseLog("case12:???????????????????????????");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm1.setRefCommodityID(commCreated.getID());
		comm1.setRefCommodityMultiple(3);
		comm1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createMultiPackagingTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case2:????????????????????????????????????");
		//
		final String commodityName = "??????" + UUID.randomUUID().toString().substring(1, 8);
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setName(commodityName);
		commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		// ????????????
		String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateMultiPackaging);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createMultiPackagingEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createMultiPackagingTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case3:??????????????????not null??????????????????null");
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
		Shared.caseLog("case4:????????????????????????,staffID??????????????????4");
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
		Shared.caseLog("case5: ?????????????????????????????????ID??????????????????7");
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
		assertTrue("???????????????".equals(params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createMultiPackagingTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case6: ?????????????????????????????????ID??????????????????7");
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
		assertTrue("???????????????".equals(params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createMultiPackagingTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case7: ???????????????????????????????????????ID??????????????????7");
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
		assertTrue("?????????????????????".equals(params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void createMultiPackagingTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case8: ????????????????????????????????????????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setName("??????????????????");// ????????????????????????????????????????????????????????????????????????????????????
		commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
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
		Shared.caseLog("case9: ?????????????????????????????????????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateMultiPackaging);

		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// ????????????????????????????????????
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity2.setName(commodity.getName());
		commodity2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createMultiPackagingTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case10:????????????????????????RefCommodityID ????????????????????????");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm1.setRefCommodityID(9999);
		comm1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
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
		Shared.caseLog("case18:????????????????????????RefCommodityID ???????????????????????????????????????");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated1.getID());
		comm2.setRefCommodityMultiple(3);
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
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
		Shared.caseLog("case12:????????????????????????RefCommodityMultiple ?????????1???????????????");
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
		Shared.caseLog("case13:????????????????????????RefCommodityID ???????????????????????????????????????");
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
		Shared.caseLog("case14:????????????????????????RefCommodityID ??????????????????????????????????????????");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
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
		Shared.caseLog("case11:????????????????????????");
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
		Shared.caseLog("case2:?????????????????????????????????");
		//
		final String commodityName = "??????" + UUID.randomUUID().toString().substring(1, 8);
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		commodity.setName(commodityName);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateComposition);
		//
		// ????????????
		String error = commodity.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = commodity.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodity);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createCombinationEx(params);
		//
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);
		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createCombinationTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case3:??????????????????not null??????????????????null");
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
		Shared.caseLog("case4:?????????????????????,staffID??????????????????4");
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
		Shared.caseLog("case5: ??????????????????????????????ID??????????????????7");
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
		assertTrue("???????????????".equals(params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case6: ??????????????????????????????ID??????????????????7");
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
		assertTrue("???????????????".equals(params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("case7: ????????????????????????????????????ID??????????????????7");
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
		assertTrue("?????????????????????".equals(params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@Test
	public void createCombinationTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("case8: ?????????????????????????????????????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		commodity.setName("??????????????????");// ????????????????????????????????????????????????????????????????????????????????????
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
		Shared.caseLog("case9: ??????????????????????????????????????????");
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateComposition);

		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// ????????????????????????????????????
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		commodity2.setName(commodity.getName());
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateComposition);
		// ???????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createCombinationTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case10:????????????????????????????????????????????????????????????");
		// ???????????????????????????A
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		// ???????????????????????????B
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateComposition);
		// ??????A???B
		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(commCreate1.getID());
		subCommodity.setSubCommodityID(commCreate2.getID());
		subCommodity.setSubCommodityNO(1);
		Map<String, Object> subParams = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(subParams);
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("????????????????????????????????????????????????".equals(subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case11:???????????????????????????????????????????????????????????????");
		// ???????????????????????????A
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		// ??????????????????????????????B
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(1);
		comm2.setRefCommodityMultiple(10);
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// ??????A???B
		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(commCreate1.getID());
		subCommodity.setSubCommodityID(commCreate2.getID());
		subCommodity.setSubCommodityNO(1);
		Map<String, Object> subParams = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(subParams);
		System.out.println("???????????????" + subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("????????????????????????????????????????????????".equals(subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case12:????????????????????????????????????????????????????????????");
		// ???????????????????????????A
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		// ???????????????????????????B
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		comm2.setType(EnumCommodityType.ECT_Service.getIndex());
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateService);
		// ??????A???B
		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(commCreate1.getID());
		subCommodity.setSubCommodityID(commCreate2.getID());
		subCommodity.setSubCommodityNO(1);
		Map<String, Object> subParams = subCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, subCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		SubCommodity subCommodityCreate = (SubCommodity) subCommodityMapper.create(subParams);
		System.out.println("???????????????" + subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(subCommodityCreate == null && EnumErrorCode.values()[Integer.parseInt(subParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue("????????????????????????????????????????????????".equals(subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), subParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createCombinationTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case13:?????????????????????RefCommodityID != 0???????????????");
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
		Shared.caseLog("case14:?????????????????????RefCommodityMultiple != 0???????????????");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		comm1.setRefCommodityMultiple(2);
		//
		String error = comm1.checkCreate(BaseBO.CASE_Commodity_CreateComposition);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_refCommodity);
	}

	// ?????????????????????ID??????0??????????????????????????????,??????????????????ID??????0??????????????????????????????,?????????????????????ID?????????ID??????0?????????????????????
	// ????????????????????????????????????????????????
	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:??????iCategoryID??????????????????");
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
			Assert.assertTrue(commCreated.getCategoryID() == commodity.getCategoryID(), "????????????CategoryID?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:??????iBrandID??????????????????");
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
			Assert.assertTrue(commCreated.getBrandID() == commodity.getBrandID(), "????????????BrandID?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:??????sName??????????????????");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName("?????????????????????" + UUID.randomUUID().toString().substring(1, 8));
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commCreated.getName().equals(commodity.getName()), "????????????Name?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:??????sMnemonicCode?????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setMnemonicCode("SP");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commCreated.getMnemonicCode().equals(commodity.getMnemonicCode()), "????????????MnemonicCode?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:????????????6??????sBarcode?????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setQueryKeyword("3548293894545");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			// RN?????????????????????????????????
			// Assert.assertTrue(commCreated.getString1().equals(commodity.getString1()),
			// "????????????Barcode?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:??????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:??????iNO?????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setNO(100);
		comm1.setnOStart(100);
		comm1.setPurchasingPriceStart(100D);
		comm1.setLatestPricePurchase(comm1.getPurchasingPriceStart()); // ????????????latestPricePurchase?????????purchasingPriceStart
		BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(100); // ????????????????????????????????????BaseAction.INVALID_ID?????????????????????????????????0???
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
//			Assert.assertTrue(commodity.getNO() > 0, "????????????NO?????????");
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
			Assert.assertTrue(existNOGreaterThanZero, "????????????NO?????????");
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
		System.out.println("?????????N???????????????????????????");
		// ???????????????,?????????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);

		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveNTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8:??????iStatus?????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(0);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commCreated.getStatus() == commodity.getStatus(), "????????????Status?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:??????iStatus???iNO?????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setNO(100);
		comm1.setnOStart(100);
		comm1.setPurchasingPriceStart(100D);
		comm1.setLatestPricePurchase(comm1.getPurchasingPriceStart()); // ????????????latestPricePurchase?????????purchasingPriceStart
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(0);
		comm.setNO(100);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commCreated.getStatus() == commodity.getStatus(), "????????????Status?????????");
			//
//			Assert.assertTrue(commodity.getNO() > 0, "????????????NO?????????");
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
			Assert.assertTrue(existNOGreaterThanZero, "????????????NO?????????");
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
		System.out.println("?????????N???????????????????????????");
		// ???????????????,?????????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);

		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveNTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:??????iType?????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:??????iType???iStatus?????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm = new Commodity();
		comm.setStatus(0);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commCreated.getStatus() == commodity.getStatus(), "????????????Status?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:??????iType???iNO??????????????????,?????????????????????0  ");
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
			// ??????????????????????????????????????????????????????0
//			Assert.assertTrue(commodity.getNO() == 0, "????????????NO?????????");
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
				Assert.assertTrue(commodityShopInfoRN.getNO() == 0, "????????????NO?????????");
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
		System.out.println("?????????N???????????????????????????");
		// ???????????????,?????????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);

		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveNTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:????????????????????????????????????2???");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated.getID());
		comm2.setRefCommodityMultiple(3);
		comm2.setQueryKeyword("254521454628769752");
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14:?????????????????????????????? ");
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
			// RN?????????????????????????????????
			// Assert.assertTrue(commCreated1.getString1().equals(commodity.getString1()) ||
			// commCreated.getName().equals(commodity.getName()), "????????????Barcode???Name?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest15() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15:???????????????????????????????????? ");
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
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commCreated.getMnemonicCode().equals(commodity.getMnemonicCode()) || commCreated1.getName().equals(commodity.getName()), "????????????MnemonicCode???Name?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest16() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case16:??????name?????????????????????,?????????null");
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm2 = BaseCommodityTest.DataInput.getCommodity();
		comm2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm2.setRefCommodityID(commCreated.getID());
		comm2.setRefCommodityMultiple(3);
		comm2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(comm2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest17() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case17:??????name??????????????????");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commCreated.getName().equals(commodity.getName()), "????????????Name?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest18() throws CloneNotSupportedException, InterruptedException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case18:??????????????????????????????");
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
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commodity.getCreateDatetime().after(comm.getDate1()) && commodity.getCreateDatetime().before(comm.getDate2()), "????????????CreateDatetime?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest19() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case19:???????????????????????????");
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
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commodity.getCreateDatetime().after(comm.getDate1()), "????????????CreateDatetime?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest20() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case20:??????????????????????????????");
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
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			Assert.assertTrue(commodity.getCreateDatetime().before(comm.getDate2()), "????????????CreateDatetime?????????");
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest21() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case21:??????????????????????????????");
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
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setType(BaseAction.INVALID_Type);
		Thread.sleep(1000); // ?????????Date?????????
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest22() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case22:????????????7??????sBarcode??????????????????,??????????????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setQueryKeyword("3548293894599");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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

		Shared.caseLog("Case23:??????QueryKeyword??????_????????????????????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setName(System.currentTimeMillis() % 1000000 + "_1");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
		System.out.println("?????????N???????????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest24() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case24:?????????123456789 12345678 ??????1234567 ??????");
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
		assertTrue(barcodesCreate != null && EnumErrorCode.values()[Integer.parseInt(barcodesparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "?????????????????????");
		//
		Barcodes barcodes2 = DataInput.getBarcodes();
		barcodes2.setCommodityID(commCreated.getID());
		barcodes2.setBarcode("12345678");
		Map<String, Object> barcodesparams2 = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate2 = (Barcodes) barcodesMapper.create(barcodesparams2);
		assertTrue(barcodesCreate2 != null && EnumErrorCode.values()[Integer.parseInt(barcodesparams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "?????????????????????");
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
		Assert.assertTrue(b, "????????????");
		//
		barcodesCreate.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> barcodesDeleteparams = barcodes.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodesCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(barcodesDeleteparams);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(barcodesDeleteparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "?????????????????????");
		//
		barcodesCreate2.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> barcodesDeleteparams2 = barcodes.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodesCreate2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(barcodesDeleteparams2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(barcodesDeleteparams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "?????????????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest25() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case25:??????64???????????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		String barcodes = "12345678123456781234567812345678123456781234567812345678" + Shared.generateStringByTime(8);
		comm1.setBarcodes(barcodes);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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

		Shared.caseLog("Case26:????????????64???????????????????????? ");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setQueryKeyword("1234567812345678123456781234567812345678123456781234567812345678");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setType(BaseAction.INVALID_Type);
		comm.setDate1(null);
		comm.setDate2(null);
		comm.setQueryKeyword("12345678123456781234567812345678123456781234567812345678123456787777");
		comm.setPageIndex(1);
		comm.setPageSize(10);
		//
		String error1 = comm.checkRetrieveN(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, Commodity.FIELD_ERROR_queryKeyword); // Commodity???checkRetrieveN???????????????
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

		Shared.caseLog("Case27:??????????????????????????????");
		//
		Commodity comm = new Commodity();
		comm.setStatus(BaseAction.INVALID_STATUS);
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		comm.setCategoryID(BaseAction.INVALID_ID);// -1??????????????????????????????
		comm.setBrandID(BaseAction.INVALID_ID);// -1??????????????????????????????
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
			assertTrue(bmList.get(i - 1).getID() > bmList.get(i).getID(), "?????????????????????????????????");
		}
	}

	@Test
	public void retrieve1Test1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 1: ?????????????????????????????????");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "????????????????????????");
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
		Shared.caseLog("Case 2: ????????????????????????????????????????????????");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		//
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "??????????????????????????????");
	}

	@Test
	public void retrieve1ExTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("Case 3: ??????????????????????????????includeDeleted???includeDeleted = 1??????????????????????????????????????????");
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateSingle);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		//
		commCreated.setIncludeDeleted(EnumBoolean.EB_Yes.getIndex());
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "?????????????????????????????????");
		// ?????????????????????????????????F_PackageUnitID???F_CategoryID???F_BrandID?????????0???checkCreate????????????
		// String error = commRetrivedAfterDeleted2.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
	}

	@Test
	public void retrieve1ExTest4() throws CloneNotSupportedException {
		Shared.caseLog("Case4:??????????????????ID ec=0");
		//
		Commodity commCreated = new Commodity();
		commCreated.setID(BaseAction.INVALID_ID);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm == null, "??????????????????????????????????????????????????????");
	}

	@Test
	public void retrieve1ExTest5() throws Exception {
		Shared.caseLog("Case5:?????????????????? ec=0");

		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity compositionComm = BaseCommodityTest.createCommodityViaMapper(comm1, BaseBO.CASE_Commodity_CreateComposition);
		//
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(compositionComm, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null && comm.getListSlave1() != null && comm.getListSlave1().size() == 1, "????????????!");
		//
		BaseCommodityTest.deleteCommodityViaMapper(compositionComm, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:???????????? --F_Name????????? ?????????????????????");
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		Commodity c = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		c.setShortName("??????");
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
		// ????????????????????????????????????-1???????????????0
		c.setIgnoreSlaveListInComparision(true);
		if (c.compareTo(updateCommodityCase1) != 0) {
			Assert.assertTrue(false, "??????????????????DB??????????????????");
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
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(updateCommodityCase1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case2:F_Name????????????????????????????????????  ?????????????????????");
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
		// ????????????????????????????????????-1???????????????0
		c2.setIgnoreSlaveListInComparision(true);
		if (c2.compareTo(updateCommodityCase2) != 0) {
			Assert.assertTrue(false, "??????????????????DB??????????????????");
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
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(updateCommodityCase2, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:F_Name????????????????????????????????????  ?????????????????????");
		//
		Commodity commCreate3 = BaseCommodityTest.DataInput.getCommodity();
		commCreate3.setOperatorStaffID(STAFF_ID3);
		commCreate3.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity c3 = BaseCommodityTest.createCommodityViaMapper(commCreate3, BaseBO.CASE_Commodity_CreateSingle);
		//
		c3.setName(UUID.randomUUID().toString().substring(1, 7));
		c3.setShortName("??????");
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
		// ????????????????????????????????????-1???????????????0
		c3.setIgnoreSlaveListInComparision(true);
		if (c3.compareTo(updateCommodityCase3) != 0) {
			Assert.assertTrue(false, "??????????????????DB??????????????????");
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
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(updateCommodityCase3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:F_Name?????????????????????????????????  ???????????????????????????");
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
		// ?????????????????????????????????
		// c4.setInt1(0);
		// c41.setInt1(0);
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(c41, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:?????????????????????????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
		//
		commCreate.setShortName("??????");
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

		Shared.caseLog("Case6:?????????????????? ");
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
		// ?????????????????????????????????
		c5.setOperatorStaffID(commCreate6.getOperatorStaffID());
		BaseCommodityTest.deleteCommodityViaMapper(c5, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "?????????????????????BrandID????????????";
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
		Assert.assertTrue(message.equals(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "?????????????????????" + paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updateCommodityCase);
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "?????????????????????CategoryID????????????";
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
		Assert.assertTrue(message.equals(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "?????????????????????" + paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updateCommodityCase);
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "?????????????????????PackageUnitID????????????";
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
		Assert.assertTrue(message.equals(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "?????????????????????" + paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(updateCommodityCase);
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:????????????????????????????????????????????????????????????");
		// ??????????????????
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
		// ??????????????????????????????2
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
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:??????????????????????????????????????????"); // ????????????????????????
		// ??????????????????
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
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12:?????????????????????????????????????????????");
		// ??????????????????
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
		commCreate5.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity c5 = BaseCommodityTest.createCommodityViaMapper(commCreate5, BaseBO.CASE_Commodity_CreateMultiPackaging);
		c5.setStartValueRemark("123");
		//
		c5.setOperatorStaffID(STAFF_ID3);
		//
		String error = c5.checkUpdate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, Commodity.FIELD_ERROR_type);
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c5, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13:??????????????????????????????????????????");
		// ??????????????????
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
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14:???????????????????????????????????????1");
		// ??????????????????
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
			Assert.assertTrue(false, "ID??????" + c4.getID() + "?????????????????????????????????????????????");
		}
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest15() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15:???????????????????????????????????????(????????????)");
		// ??????????????????
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
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest16() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case16:???????????????????????????????????????2");
		// ??????????????????
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
			Assert.assertTrue(false, "ID??????" + c4.getID() + "?????????????????????????????????????????????");
		}
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(c4, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updatePriceTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("CASE1:????????????????????? ");
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		commCreate.setPurchasingUnit("???");
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		// ????????????????????????????????????????????????
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
			Assert.assertTrue(false, "??????????????????DB??????????????????");
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
		// ??????????????????????????????
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(poc);
		//
		// ????????????????????????????????????comm
		BaseCommodityTest.deleteCommodityViaMapper(comm, EnumErrorCode.EC_NoError);
		// CASE2:???????????????
		// System.out.println("------------------------ CASE2:???????????????
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
		Shared.caseLog("CASE2:??????????????? ");
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		commCreate.setPurchasingUnit("???");
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		String error = comm.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(error, "");
		//
		// ????????????????????????????????????????????????
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
			Assert.assertTrue(false, "??????????????????DB??????????????????");
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
		// ??????????????????????????????
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(poc);
		//
		// ????????????????????????????????????comm
		BaseCommodityTest.deleteCommodityViaMapper(comm, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updatePriceTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.caseLog("CASE3:???????????????????????????ID");
		//
		Commodity commCreate = BaseCommodityTest.DataInput.getCommodity();
		commCreate.setPurchasingUnit("???");

		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commCreate, BaseBO.CASE_Commodity_CreateSingle);
		//
		String error = comm.checkUpdate(BaseBO.CASE_UpdatePurchasingUnit);
		Assert.assertEquals(error, "");
		//
		// ????????????????????????????????????????????????
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
		// ??????????????????????????????
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(poc);
		//
		// ????????????????????????????????????comm
		comm.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(comm, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 1: ?????????????????? 1?????? ????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 2: ?????????????????? 2?????? ????????????");
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
		// ???????????????????????????
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "???????????????????????????????????????");
	}

	@Test
	public void deleteSimpleTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 3: ?????????1???????????? ??????1?????? ?????????1?????? ????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity c2 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Map<String, Object> paramForDelete3 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete3);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// ??????????????????
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case3 ???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(c2, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 4: ?????????1???????????? ??????1?????? ?????????1??????  ??????????????????????????????????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity c2 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// ??????????????????????????????
		c2.setOperatorStaffID(c1.getOperatorStaffID());
		BaseCommodityTest.deleteCommodityViaMapper(c2, EnumErrorCode.EC_NoError);
		//
		commCreated.setOperatorStaffID(c1.getOperatorStaffID());
		Map<String, Object> paramForDelete4 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		// ?????????????????? ????????????
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete4);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// ??????????????????
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case4 ???????????????");
	}

	// @Test
	// public void deleteSimpleTest4() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	// Shared.caseLog("Case 4: ?????????1???????????? ??????2?????? ?????????1?????? ????????????");
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
	// // ???????????????????????????
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
	// Assert.assertNull(commRetrived4, "Case4 ???????????????");
	// }
	//
	// @Test
	// public void deleteSimpleTest5() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case 5: ?????????1???????????? ??????1?????? ?????????2?????? ????????????");
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
	// // ???????????????????????????
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
	// Assert.assertNull(commRetrived5, "Case5 ???????????????");
	// }
	//
	// @Test
	// public void deleteSimpleTest6() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case 6: ?????????1???????????? ??????2?????? ?????????2?????? ????????????");
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
	// // ???????????????????????????
	// commCreated.setInt1(RETRIEVE1_DELETED_COMMODITY);
	// Map<String, Object> paramsRetrieve6 =
	// commCreated.getRetrieve1Param(BaseBO.INVALID_CASE_ID, commCreated);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// Commodity commRetrived6 = (Commodity)
	// commodityMapper.retrieve1(paramsRetrieve6);// ...
	// Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
	// == EnumErrorCode.EC_NoSuchData,
	// paramsRetrieve6.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	// Assert.assertNull(commRetrived6, "Case6 ???????????????");
	// }
	//
	// @Test
	// public void deleteSimpleTest7() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case 7: ?????????2???????????? ??????1?????? ????????????1?????? ????????????");
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
	// // ???????????????????????????
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
	// Assert.assertNull(commRetrived7, "Case7 ???????????????");
	// }
	//
	// @Test
	// public void deleteSimpleTest8() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("Case 8: ?????????2???????????? ??????2?????? ????????????2?????? ????????????");
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
	// // ???????????????????????????
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
	// Assert.assertNull(commRetrived8, "Case8 ???????????????");
	// }

	@Test
	public void deleteSimpleTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 9: ???????????????????????????????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateComposition);

		Map<String, Object> paramForDelete9 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete9);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// ???????????????????????????
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case9 ???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 10: ???????????????????????????????????? ???????????????????????? ??????????????????????????? ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		// ????????????????????????????????????
		PurchasingOrderCommodity pocCreate = BasePurchasingOrderTest.createPurchasingOrderViaMapper(commCreated);
		//
		Map<String, Object> paramForDelete10 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete10);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete10.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ???????????????????????????
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, comm + "?????????");
		//
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(pocCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 11: ????????????????????????????????????????????????????????? ??????????????????????????? ec = 7");
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
		// ???????????????????????????
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case11 ???????????????");
		//
		BaseInventorySheetTest.deleteInventoryCommodity(icCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 12: ????????????????????????????????????????????????????????? ??????????????????????????? ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		BaseRetailTradeTest.createRetailTradeCommodityViaMapper(commCreated.getID());

		Map<String, Object> paramForDelete12 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete12);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete12.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// ???????????????????????????
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case12 ???????????????");
		// ??????????????????????????????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);

		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteSimpleTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 13: ????????????????????????????????????????????????????????? ??????????????????????????? ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		WarehousingCommodity wcCreated = BaseWarehousingTest.createWarehousingCommodityViaMapper(commCreated.getID());

		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// ???????????????????????????
		commCreated.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case13 ???????????????");
		//
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcCreated);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);

	}

	@Test
	public void deleteSimpleTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 14: ??????????????????????????????????????????????????? ??????????????????????????? ec = 7");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setNO(100);
		c.setnOStart(100);
		c.setPurchasingPriceStart(100D);
		c.setLatestPricePurchase(c.getPurchasingPriceStart()); // ????????????latestPricePurchase?????????purchasingPriceStart
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);

		Map<String, Object> paramForDelete14 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete14);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete14.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete14.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ???????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case14 ????????????????????????null");

		// ???????????????,?????????????????????????????????
		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteSimpleTest15() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case15:??????????????????Id ???????????????????????????????????????????????? ??????????????????????????? ec = 7");
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
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);

		Map<String, Object> paramForDelete15 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete15);
		System.out.println(EnumErrorCode.values()[Integer.parseInt(paramForDelete15.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete15.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// ???????????????????????????
		commCreated1.setIncludeDeleted(RETRIEVE1_DELETED_COMMODITY);
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case15 ???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcCreated);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteSimpleTest16() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 16: ??????????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
		// ?????????????????????
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

		Shared.caseLog("Case 17: ????????????????????????");
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

		Shared.caseLog("Case 18_1:  ?????????????????????????????????????????????????????????????????????????????????(???????????????????????????)");
		// ????????????
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// ??????????????????
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(commCreated.getID());
		ps.setPromotionID(pCreate.getID());
		BasePromotionTest.createPromotionScopeViaMapper(ps, EnumErrorCode.EC_NoError);
		// ?????????????????????????????????
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Shared.caseLog("Case 18_2:???????????????????????????????????????????????????(????????????1)?????????????????????????????????????????????????????????????????????(???????????????????????????)");
		// ????????????
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		// ?????????????????????????????????
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteSimpleTest19() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 19_1:  ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
		// ????????????????????????
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrder = BasePurchasingOrderTest.createPurchasingOrderViaMapper(po);
		// ??????????????????
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// ????????????ID?????????????????????t1
		Barcodes barcode1 = BaseBarcodesTest.retrieveNBarcodes(commCreated.getID(), Shared.DBName_Test);
		// ??????????????????????????????????????????????????????
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
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(createpOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		pOrderComm.setIgnoreIDInComparision(true);
		if (pOrderComm.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		// ?????????????????????????????????
		commCreated.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ???????????????????????????????????????????????????
		BasePurchasingOrderTest.deleteAndVerificationPurchasingOrder(purchasingOrder);
		// ???????????????????????????????????????
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		commodityMapper.deleteSimple(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteSimpleTest20() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 20_1: ???????????????????????????????????????????????????????????????????????????????????????");
		// ????????????
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(0);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// ?????????????????????????????????
		commCreated.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ????????????
		BasePromotionTest.deletePromotionViaMapper(pCreate);
	}

	@Test
	public void deleteSimpleTest21() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 21: ?????????????????????????????????????????????????????????????????????????????????");
		// ????????????
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setScope(0);
		Promotion pCreate = BasePromotionTest.createPromotionViaMapper(p);
		// ????????????
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// ????????????
		BasePromotionTest.deletePromotionViaMapper(pCreate);
		// ?????????????????????????????????
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

		Shared.caseLog("Case 22:  ?????????????????????????????????????????????????????????????????????????????????(???????????????????????????), ?????????????????????????????????");
		// ????????????
		Promotion promotionGet = BasePromotionTest.DataInput.getPromotion();
		Promotion promotionCreated = BasePromotionTest.createPromotionViaMapper(promotionGet);
		// ????????????
		Commodity commodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// ??????????????????
		Barcodes barcodesGet1 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreated1 = BaseBarcodesTest.createBarcodesViaMapper(barcodesGet1, BaseBO.INVALID_CASE_ID);
		// ??????????????????
		PromotionScope promotionScope = new PromotionScope();
		promotionScope.setCommodityID(commCreated.getID());
		promotionScope.setPromotionID(promotionCreated.getID());
		BasePromotionTest.createPromotionScopeViaMapper(promotionScope, EnumErrorCode.EC_NoError);
		// ?????????????????????????????????
		Map<String, Object> paramForDelete = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteSimple(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ??????????????????????????????
		BaseBarcodesTest.retrieve1ViaMapper(barcodesCreated1.getID(), Shared.DBName_Test);
	}
	
	@Test
	public void deleteSimpleTest23() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 23:?????????????????????????????????????????????(??????????????????)");
		// ??????????????????
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// ???????????????
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// ?????????
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// ?????????????????????
		CouponScope couponScopeGet = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), simpleCommodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScopeGet);
		// ??????????????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ??????????????????
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
	}
	
	
	@Test
	public void deleteSimpleTest24() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 24:?????????????????????????????????????????????(??????????????????)");
		// ??????????????????
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// ???????????????
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// ?????????
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), 1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// ?????????????????????
		CouponScope couponScopeGet = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), simpleCommodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScopeGet);
		// ??????????????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ??????????????????
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void deleteSimpleTest25() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 25:?????????????????????????????????????????????(??????????????????)");
		// ??????????????????
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// ???????????????
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// ?????????
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -5));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), -1));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// ?????????????????????
		CouponScope couponScopeGet = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), simpleCommodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScopeGet);
		// ??????????????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ??????????????????
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void deleteSimpleTest26() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 26:?????????????????????????????????????????????(??????????????????)");
		// ??????????????????
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// ???????????????
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// ?????????
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// ?????????????????????
		CouponScope couponScopeGet = BaseCouponScopeTest.DataInput.getCouponScope(couponCreate.getID(), simpleCommodityCreate.getID());
		CouponScope couponScopeCreate = BaseCouponScopeTest.createViaMapper(couponScopeGet);
		// ???????????????
		BaseCouponTest.deleteViaMapper(couponCreate);
		// ??????????????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_BusinessLogicNotDefined);
		// ??????????????????
		BaseCouponScopeTest.deleteViaMapper(couponScopeCreate);
		BaseCouponTest.deleteViaMapper(couponCreate);
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void deleteSimpleTest27() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case 27:?????????????????????????????????????????????");
		// ??????????????????
		Commodity simpleCommodityGet = BaseCommodityTest.DataInput.getCommodity();
		Commodity simpleCommodityCreate = BaseCommodityTest.createCommodityViaMapper(simpleCommodityGet, BaseBO.CASE_Commodity_CreateSingle);
		// ???????????????
		Coupon couponGet = BaseCouponTest.DataInput.getCoupon();
		// ?????????
		couponGet.setBeginDateTime(DatetimeUtil.getDays(new Date(), -1));
		couponGet.setEndDateTime(DatetimeUtil.getDays(new Date(), 5));
		Coupon couponCreate = BaseCouponTest.createViaMapper(couponGet);
		// ??????????????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(simpleCommodityCreate, EnumErrorCode.EC_NoError);
		// ???????????????
		BaseCouponTest.deleteViaMapper(couponCreate);
	}

	@Test
	public void deleteCombinationTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:????????????,????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Map<String, Object> paramForDelete1 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		commodityMapper.deleteCombination(paramForDelete1);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ??????????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case1 ???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteCombinationTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:???????????????????????? ????????????");
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
		// ???????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case2 ???????????????");
		//
		BaseCommodityTest.retrieveNCommodityHistory(commCreated.getID(), c.getOperatorStaffID(), commodityHistoryMapper);
	}

	@Test
	public void deleteCombinationTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:??????????????????ID??????????????????????????????");
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
		// ???????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case3 ???????????????");
		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteCombinationTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:??????????????????Id,????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Map<String, Object> paramForDelete4 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteCombination(paramForDelete4);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// ???????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case4 ???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	// ??????????????????????????????,??????????????????case????????????????????????????????????0???????????????Id???
	// @Test
	// public void deleteCombinationTest5() throws CloneNotSupportedException,
	// InterruptedException {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case5:??????????????????0???????????????Id???????????????");// ???????????????????????????????????????????????????????????????????????????NO??????
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
	// // ???????????????????????????
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
	// // Assert.assertNotNull(commRetrived5, "Case5 ???????????????");
	// Assert.assertNull(commRetrived5, "????????????");
	// // ??????????????????????????????
	// deleteAllBarcodesSyncCache();
	// }

	@Test
	public void deleteCombinationTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:????????????????????????");
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
		// ???????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case2 ???????????????");
		// ????????????
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

		Shared.caseLog("case7:????????????????????????");
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
		// @Transactional ?????????????????????commodity87~92 ???barcodes???????????????barcodes mapper test???????????????
		Shared.caseLog("Case1:????????????,????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Map<String, Object> paramForDelete1 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated);
		commodityMapper.deleteMultiPackaging(paramForDelete1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// ??????????????????????????? ???????????????????????? ??????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case1 ???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteMultiPackagingTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:????????????????????????,????????????");
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
		// ??????????????????????????? ???????????????????????? ??????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case2 ???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteMultiPackagingTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:??????????????????????????? ????????????????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		// ?????????????????????????????????????????????????????????
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTrade(rt);
		// ??????????????????????????????
		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setTradeID(rtCreate.getID());
		rtc.setCommodityID(commCreated1.getID());
		rtc.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Map<String, Object> params = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(params); // ...
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(retailTradeCommodityCreate) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
		//
		Map<String, Object> paramForDelete3 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(paramForDelete3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ??????????????????????????? ???????????????????????????????????????????????? ??????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case3 ???????????????");
		//
		// ?????????????????????????????????????????????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteMultiPackagingTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:????????????????????????????????????????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
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
		// ??????????????????????????? ???????????????????????????????????????????????? ??????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "Case5 ???????????????");

		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void deleteMultiPackagingTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:??????????????????Id ????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		commCreated1.setOperatorStaffID(c1.getOperatorStaffID());
		Map<String, Object> paramForDelete7 = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(paramForDelete7);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// ??????????????????????????? ????????????????????????????????????0????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case7 ???????????????");
		//
		BaseCommodityTest.retrieveNCommodityHistory(commCreated1.getID(), c1.getOperatorStaffID(), commodityHistoryMapper);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteMultiPackagingTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:???????????????????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		commCreated1.setOperatorStaffID(c1.getOperatorStaffID());
		Map<String, Object> paramForDelete = commCreated1.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteMultiPackaging(paramForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// ??????????????????????????? ????????????????????????????????????0????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoSuchData);
		Assert.assertTrue(comm == null, "Case7 ???????????????");
		// ????????????
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

		Shared.caseLog("case8:????????????????????????");
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

		Shared.caseLog("case1:???????????????????????????");
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
		// ?????????????????????????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteServiceTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2?????????????????????????????????");
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated1 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		commCreated1.setOperatorStaffID(c1.getOperatorStaffID());
		Map<String, Object> paramForDelete2 = commCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, commCreated1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.deleteService(paramForDelete2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramForDelete2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ?????????????????????????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated1, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated1, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteServiceTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:???????????????????????????");
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
		// ?????????????????????????????????????????????
		Commodity comm = BaseCommodityTest.retrieve1ExCommodity(commCreated, EnumErrorCode.EC_NoError);
		Assert.assertTrue(comm != null, "???????????????");
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteServiceTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case4:???????????????????????????");
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

		Shared.caseLog("case5:???????????????????????????");
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
		// ??????????????????
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

		Shared.caseLog("case6:?????????????????????????????????");
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
		Shared.caseLog("case7:????????????????????? ???????????????????????????");

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

		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveNMultiPackageCommodityTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity c1 = BaseCommodityTest.DataInput.getCommodity();
		c1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c1.setRefCommodityID(commCreated.getID());
		c1.setRefCommodityMultiple(3);
		c1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated2 = BaseCommodityTest.createCommodityViaMapper(c1, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Commodity c2 = BaseCommodityTest.DataInput.getCommodity();
		c2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		c2.setRefCommodityID(commCreated.getID());
		c2.setRefCommodityMultiple(4);
		c2.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
		Commodity commCreated3 = BaseCommodityTest.createCommodityViaMapper(c2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_RetrieveNMultiPackageCommodity, commCreated);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNMultiPackageCommodity = commodityMapper.retrieveNMultiPackageCommodity(params);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(retrieveNMultiPackageCommodity.size() != 0, "??????????????????");
		//
		for (BaseModel bm : retrieveNMultiPackageCommodity) {
			Commodity commodity = (Commodity) bm;
			//
			int staffID = commodity.getOperatorStaffID();
			commodity.setOperatorStaffID(c.getOperatorStaffID());
			commodity.setBarcodes(c.getBarcodes());
			commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
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

		Shared.caseLog("case2:??????????????????????????????????????????");
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

		Shared.caseLog("Case1:??????????????????");
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode????????????" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "???" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		// ?????????????????????????????????
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkDependencyTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "??????????????????0????????????";
		Shared.caseLog("Case2:" + message);
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setnOStart(100);
		comm.setNO(100);
		comm.setPurchasingPriceStart(100D);
		comm.setLatestPricePurchase(comm.getPurchasingPriceStart()); // ????????????latestPricePurchase?????????purchasingPriceStart
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode????????????" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "???" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkDependencyTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "?????????RetailtradeCommodity?????????";
		Shared.caseLog("Case3:" + message);
		// ?????????????????????
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodesCreate = BaseCommodityTest.retrieveNBarcodesViaMapper(commCreated);
		// ???????????????????????????
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
		System.out.println("iErrorCode????????????" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "???" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void checkDependencyTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "?????????PurchasingOrderCommodity?????????";
		Shared.caseLog("Case4:" + message);
		// ?????????????????????
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		// ??????????????????????????????
		PurchasingOrderCommodity purchasingOrderCommodity = BasePurchasingOrderTest.createPurchasingOrderViaMapper(commCreated);
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode????????????" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "???" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ?????????????????????????????????
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(purchasingOrderCommodity);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkDependencyTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "?????????InventoryCommodity?????????";
		Shared.caseLog("Case5:" + message);
		// ?????????????????????
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		// ???????????????????????????
		InventoryCommodity inventoryCommodity = BaseInventorySheetTest.createInventoryCommodityViaMapper(commCreated.getID());
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode????????????" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "???" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ?????????????????????????????????
		BaseInventorySheetTest.deleteInventoryCommodity(inventoryCommodity);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void checkDependencyTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "?????????WarehousingCommodity?????????";
		Shared.caseLog("Case6:" + message);
		// ?????????????????????
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		// ???????????????????????????
		WarehousingCommodity warehousingCommodity = BaseWarehousingTest.createWarehousingCommodityViaMapper(commCreated.getID());
		//
		Map<String, Object> params = commCreated.getRetrieveNParam(BaseBO.CASE_CheckDeleteDependency, commCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		commodityMapper.checkDependency(params);
		//
		System.out.println("iErrorCode????????????" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] + "???" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "????????????????????????" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// ?????????????????????????????????
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(warehousingCommodity);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void updatePurchasingUnitTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:??????????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		//
		commCreated.setID(commCreated.getID());
		commCreated.setPurchasingUnit("??????");
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
		// ????????????????????????????????????-1???????????????0
		commCreated.setIgnoreSlaveListInComparision(true);
		if (commCreated.compareTo(updatePurchasingUnit) != 0) {
			Assert.assertTrue(false, "??????????????????DB??????????????????");
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

	// @Test ???????????????sp????????????????????????mapperTest??????java??????????????????
	public void updateWarehousingTest() throws CloneNotSupportedException {
		// CommodityMapper???????????????????????? ????????????????????????
	}

	@Test
	public void retrieveInventoryTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:????????????????????????????????????");
		//
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setNO(100);
		c.setnOStart(100);
		c.setPurchasingPriceStart(100D);
		c.setLatestPricePurchase(c.getPurchasingPriceStart()); // ????????????latestPricePurchase?????????purchasingPriceStart
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
		// ???????????????
		// String error = retrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
		// Assert.assertEquals(error, "");
		// ??????????????????????????????NO?????????NO???????????????nOStart????????????0?????????nOStart?????????NO????????????nOStart??????-1??????NO?????????0???
		// TODO NO?????????listSlave2???????????????listSlave2
		Assert.assertTrue(commCreated.getnOStart() == retrieve1.getNO(), "???????????????????????????");

		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void retrieveInventoryTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2???????????????????????????????????????????????????2");
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

		Shared.caseLog("case3:????????????????????????ID");
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
		Shared.caseLog("Case1:????????????????????????????????????");
		//
		Commodity commodity = new Commodity();
		commodity.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity.setUniqueField("????????????????????????");
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
		Shared.caseLog("Case2:???????????????????????????????????????");
		//
		Commodity commodity1 = new Commodity();
		commodity1.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity1.setUniqueField("???????????????");
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
		Shared.caseLog("Case3:??????????????????????????????????????????");
		//
		Commodity commodity2 = new Commodity();
		commodity2.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity2.setUniqueField("?????????????????????1");
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
		Shared.caseLog("Case4:???????????????????????????????????????,????????????ID??????????????????????????????????????????ID");
		//
		Commodity commodity4 = new Commodity();
		commodity4.setID(1);
		commodity4.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity4.setUniqueField("???????????????");
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
		Shared.caseLog("Case5:???????????????????????????????????????,?????????ID?????????????????????????????????????????????ID");
		//
		Commodity commodity5 = new Commodity();
		commodity5.setID(2);
		commodity5.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity5.setUniqueField("???????????????");
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
		Shared.caseLog("Case3:??????????????????????????????????????????,?????????ID??????????????????????????????????????????ID");
		//
		Commodity commodity6 = new Commodity();
		commodity6.setID(49);
		commodity6.setFieldToCheckUnique(CASE_CHECK_UNIQUE_NAME);
		commodity6.setUniqueField("?????????????????????1");
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
		Shared.caseLog("Case1:????????????");
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Date nowDate = new Date();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Shared.caseLog("Case2:???????????????????????????ID");
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Date nowDate = new Date();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Shared.caseLog("Case3:?????????????????????ID");
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Date nowDate = new Date();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Shared.caseLog("Case4:??????????????????,??????????????????????????????dStartDate???dEndDate??????,??????????????????????????????");
		// ???????????????A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -29); // ??????????????????????????????29??????
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// ???????????????A?????????(?????????????????????????????????)
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
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
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
		// ?????????????????????????????????????????????????????????????????????????????????
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
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
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "?????????????????????");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "????????????????????????");
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
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Assert.assertTrue(notExist, "???????????????????????????????????????????????????????????????????????????");
		// ?????????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void testUnsalableCommodityRetrieveN5() throws Exception {
		Shared.caseLog("Case5:??????????????????,?????????????????????????????????dStartDate???dEndDate??????,???????????????????????????");
		// ???????????????A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // ??????????????????????????????29??????
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// ???????????????A?????????(?????????????????????????????????)
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
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
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
		// ?????????????????????????????????????????????????????????????????????????????????
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
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
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "?????????????????????");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "????????????????????????");
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
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Assert.assertTrue(exist, "???????????????????????????????????????????????????????????????????????????");
		// ?????????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void testUnsalableCommodityRetrieveN6() throws Exception {
		Shared.caseLog("Case6:??????????????????,?????????????????????????????????dStartDate???dEndDate??????,??????????????????????????????????????????????????????????????????????????????");
		// ???????????????A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // ??????????????????????????????29??????
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// ???????????????A?????????(?????????????????????????????????)
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
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
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
		// ?????????????????????????????????????????????????????????????????????????????????
		ProviderCommodity pc1 = new ProviderCommodity();
		pc1.setProviderID(1);
		pc1.setCommodityID(commCreated.getID());
		Map<String, Object> params = pc1.getCreateParam(BaseBO.INVALID_CASE_ID, pc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ProviderCommodity createPC = (ProviderCommodity) providerCommodityMapper.create(params);
		assertNotNull(createPC);
		pc1.setIgnoreIDInComparision(true);
		if (pc1.compareTo(createPC) != 0) {
			Assert.assertTrue(false, "???????????????????????????DB??????????????????");
		}
		Assert.assertTrue(createPC != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "??????????????????");
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
		Assert.assertTrue(warehousingCommodity.getNO() == comm.getnOStart(), "?????????????????????");
		Assert.assertTrue(Math.abs(GeneralUtil.sub(comm.getPurchasingPriceStart(), warehousingCommodity.getPrice())) < BaseModel.TOLERANCE, "????????????????????????");
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
		// ????????????????????????B????????????A??????,???????????????dStartDate???dEndDate??????
		RetailTrade returnRetailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		returnRetailTrade.setSn(Shared.generateRetailTradeSN(2));
		returnRetailTrade.setSourceID(rTrade.getID());
		Calendar returnSalTime = Calendar.getInstance();
		returnSalTime.setTime(nowDate);
		returnSalTime.add(Calendar.DATE, -29); // ??????????????????????????????29??????
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
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Assert.assertTrue(exist, "???????????????????????????????????????????????????????????????????????????");
		// ?????????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void testUnsalableCommodityRetrieveN7() throws Exception {
		Shared.caseLog("Case7:??????????????????,?????????????????????????????????dStartDate???dEndDate??????,???????????????????????????0?????????????????????????????????");
		// ???????????????A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // ??????????????????????????????31??????
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// ???????????????A?????????(?????????)
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
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Assert.assertTrue(notExist, "???????????????????????????????????????????????????????????????????????????");
		// ?????????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated, mappersMap);
		// ??????????????????????????????
		BaseBarcodesTest.deleteAllBarcodesSyncCacheViaMappe();
	}

	@Test
	public void testUnsalableCommodityRetrieveN8() throws Exception {
		Shared.caseLog("Case8:??????????????????,?????????????????????????????????dStartDate???dEndDate??????,???????????????????????????????????????????????????????????????");
		// ???????????????A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // ??????????????????????????????31??????
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// ???????????????A????????????????????????????????????
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
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Assert.assertTrue(notExist, "?????????????????????????????????????????????????????????????????????????????????");
		// ??????????????????????????????
		// BaseCommodityTest.doDeleteCommodity(commCreated1, mappersMap);
	}

	@Test
	public void testUnsalableCommodityRetrieveN9() throws Exception {
		Shared.caseLog("Case9:??????????????????,?????????????????????????????????dStartDate???dEndDate??????,??????????????????????????????????????????????????????????????????");
		// ???????????????A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // ??????????????????????????????31??????
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// ???????????????A????????????????????????????????????
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity comm1 = BaseCommodityTest.DataInput.getCommodity();
		comm1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm1.setRefCommodityID(commCreated.getID());
		comm1.setRefCommodityMultiple(3);
		comm1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + "64????????????" + System.currentTimeMillis() % 1000000 + ";"); // ???????????????
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
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Assert.assertTrue(notExist, "????????????????????????????????????????????????????????????????????????????????????");
	}

	@Test
	public void testUnsalableCommodityRetrieveN10() throws Exception {
		Shared.caseLog("Case10:??????????????????,?????????????????????????????????dStartDate???dEndDate??????,???????????????????????????????????????????????????????????????");
		// ???????????????A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // ??????????????????????????????31??????
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// ???????????????A????????????????????????????????????
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
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Assert.assertTrue(notExist, "????????????????????????????????????????????????????????????????????????????????????");
	}
	
	@Test
	public void testUnsalableCommodityRetrieveN11() throws Exception {
		Shared.caseLog("Case11:??????????????????,?????????????????????0??????????????????????????????dStartDate???dEndDate????????????????????????????????????");
		// ???????????????A
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		retailTrade.setSn(Shared.generateRetailTradeSN(1));
		Date nowDate = new Date();
		Calendar salTime = Calendar.getInstance();
		salTime.setTime(nowDate);
		salTime.add(Calendar.DATE, -31); // ??????????????????????????????31??????
		retailTrade.setSaleDatetime(salTime.getTime());
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// ???????????????A????????????????????????????????????
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
		// ??????????????????
		Commodity unsalableCommRN = new Commodity();
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(nowDate);
		calStart.add(Calendar.DATE, -30); // ?????????????????????????????????30???
		Date startDatetime = calStart.getTime();
		unsalableCommRN.setDate1(startDatetime);
		unsalableCommRN.setDate2(nowDate);
		unsalableCommRN.setMessageIsRead(0);
		unsalableCommRN.setMessageParameter("[{\"Link1 \": \"www.xxxx.com\"}, {\"Link1_Tag\": \"????????????\"}]");
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
		Assert.assertTrue(notExist, "?????????????????????????????????????????????????????????????????????????????????");
	}
}
