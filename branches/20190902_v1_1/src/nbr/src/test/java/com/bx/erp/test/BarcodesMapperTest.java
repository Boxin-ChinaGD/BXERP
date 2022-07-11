package com.bx.erp.test;

import static org.testng.Assert.assertTrue;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.trade.Promotion;
import com.bx.erp.model.trade.PromotionScope;
import com.bx.erp.model.trade.Promotion.EnumTypePromotion;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.util.DataSourceContextHolder;

public class BarcodesMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}
	
	@Test
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		//
		Shared.caseLog("Case1:正常添加");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:重复添加");

		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);

		final String barcode = "123" + System.currentTimeMillis() % 1000000;
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		barcodes.setBarcode(barcode);
		BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);

		// 重复添加
		barcodes.setCommodityID(commCreated.getID());
		barcodes.setBarcode(barcode);
		Map<String, Object> params = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes bcCreated = (Barcodes) barcodesMapper.create(params);
		//
		assertTrue(bcCreated != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated);

		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:用不存在的商品ID创建条形码");
		//
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(BaseAction.INVALID_ID);
		Map<String, Object> params = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesCreate = (Barcodes) barcodesMapper.create(params);
		//
		assertTrue(barcodesCreate == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:查询所有 ");
		//
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(BaseAction.INVALID_ID);
		barcodes.setBarcode("");
		BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:根据commodityID查找");
		//
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(1);
		barcodes.setBarcode("");
		BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
	}

	@Test
	public void retrieveNTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:根据barcode查找");
		//
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(BaseAction.INVALID_ID);
		barcodes.setBarcode("3548293894545");
		BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常删除");
		//
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		//
		BaseBarcodesTest.deleteBarcodesViaMapper(barcodesCreate);
		//
		Map<String, Object> retrieve1Param = barcodesCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodesCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve1 = barcodesMapper.retrieve1(retrieve1Param);
		//
		assertTrue(barcodesRetrieve1 == null && EnumErrorCode.values()[Integer.parseInt(retrieve1Param.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
	}

	@Test
	public void deleteTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:删除的条形码在零售商品中有依赖");
		//
		Barcodes barcodes2 = new Barcodes();
		barcodes2.setID(1);
		Map<String, Object> deleteParam2 = barcodes2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodes2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteParam2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("删除的条形码在零售商品中有依赖"), deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodes1Retrieve1 = barcodesMapper.retrieve1(deleteParam2);
		assertTrue(barcodes1Retrieve1 != null && EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:删除的条形码不存在");
		//
		Barcodes barcodes2 = new Barcodes();
		barcodes2.setID(BaseAction.INVALID_ID);
		Map<String, Object> deleteParam2 = barcodes2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodes2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteParam2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodes1Retrieve1 = barcodesMapper.retrieve1(deleteParam2);
		assertTrue(barcodes1Retrieve1 == null && EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:删除的条形码在采购订单商品中有依赖");
		//
		Barcodes barcodes2 = new Barcodes();
		barcodes2.setID(101);
		Map<String, Object> deleteParam2 = barcodes2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodes2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteParam2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("删除的条形码在采购订单商品中有依赖"), deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodes1Retrieve1 = barcodesMapper.retrieve1(deleteParam2);
		assertTrue(barcodes1Retrieve1 != null && EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5:删除的条形码在入库单商品中有依赖");
		//
		Barcodes barcodes2 = new Barcodes();
		barcodes2.setID(102);
		Map<String, Object> deleteParam2 = barcodes2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodes2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteParam2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("删除的条形码在入库单商品中有依赖"), deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodes1Retrieve1 = barcodesMapper.retrieve1(deleteParam2);
		assertTrue(barcodes1Retrieve1 != null && EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:删除的条形码在盘点单商品中有依赖");
		//
		Barcodes barcodes2 = new Barcodes();
		barcodes2.setID(104);
		Map<String, Object> deleteParam2 = barcodes2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcodes2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteParam2);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString().equals("删除的条形码在盘点单商品中有依赖"), deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodes1Retrieve1 = barcodesMapper.retrieve1(deleteParam2);
		assertTrue(barcodes1Retrieve1 != null && EnumErrorCode.values()[Integer.parseInt(deleteParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				deleteParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void deleteTest7() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case7:如果商品在采购订单商品中有依赖，那么它的条形码都是不能够删除的");
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 根据商品ID创建一个条形码t1
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		// 根据商品和条形码创建一个采购订单商品
		PurchasingOrderCommodity pOrderComm = new PurchasingOrderCommodity();
		pOrderComm.setCommodityID(commCreated.getID());
		pOrderComm.setPurchasingOrderID(2);
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
		// 尝试删除条形码t1，删除失败
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve1 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 再根据商品ID创建一个条形码t2，这个条形码没有直接关联采购订单商品
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 尝试删除条形码t2,也是删除失败
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve2 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve2 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 删除测试的采购订单商品
		Map<String, Object> deletepOrderCommparams = pocCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pocCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(deletepOrderCommparams);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(deletepOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 删除测试的商品(删除商品时会连它的所有条形码都删除掉)
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest8() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case8:如果商品在入库商品中有依赖，那么它的条形码都是不能够删除的");
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 根据商品ID创建一个条形码t1
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 根据商品和条形码创建一个入库商品
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		warehousingCommodity.setWarehousingID(1);
		warehousingCommodity.setCommodityID(commCreated.getID());
		warehousingCommodity.setNO(1);
		warehousingCommodity.setNoSalable(1);
		warehousingCommodity.setBarcodeID(barcode1.getID());
		warehousingCommodity.setPrice(11.1);
		warehousingCommodity.setAmount(11.1);
		warehousingCommodity.setShelfLife(Commodity.DEFAULT_VALUE_ShelfLife);
		//
		Map<String, Object> createWhCommparams = warehousingCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity warehousingCommCreated = (WarehousingCommodity) warehousingCommodityMapper.create(createWhCommparams);
		Assert.assertTrue(warehousingCommCreated != null && EnumErrorCode.values()[Integer.parseInt(createWhCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		warehousingCommodity.setIgnoreIDInComparision(true);
		if (warehousingCommodity.compareTo(warehousingCommCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 尝试删除条形码t1，删除失败
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve1 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 再根据商品ID创建一个条形码t2，这个条形码没有直接关联入库商品
		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 尝试删除条形码t2,也是删除失败
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve2 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve2 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 删除测试的入库单商品
		Map<String, Object> deletepOrderCommparams = warehousingCommCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, warehousingCommCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityMapper.delete(deletepOrderCommparams);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(deletepOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 删除测试的商品(删除商品时会连它的所有条形码都删除掉)
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest9() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case9:如果商品在盘点单商品中有依赖，那么它的条形码都是不能够删除的");
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 根据商品ID创建一个条形码t1
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 根据商品和条形码创建一个入库商品
		InventoryCommodity inventoryComm = new InventoryCommodity();
		inventoryComm.setInventorySheetID(1);
		inventoryComm.setCommodityID(commCreated.getID());
		inventoryComm.setBarcodeID(barcode1.getID());
		inventoryComm.setNoReal(new Random().nextInt(100));
		//
		Map<String, Object> createIcCommparams = inventoryComm.getCreateParam(BaseBO.INVALID_CASE_ID, inventoryComm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity inventoryCommCreated = (InventoryCommodity) inventoryCommodityMapper.create(createIcCommparams);
		Assert.assertTrue(inventoryCommCreated != null && EnumErrorCode.values()[Integer.parseInt(createIcCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		inventoryComm.setIgnoreIDInComparision(true);
		if (inventoryComm.compareTo(inventoryCommCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 尝试删除条形码t1，删除失败
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve1 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 再根据商品ID创建一个条形码t2，这个条形码没有直接关联入库商品
		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 尝试删除条形码t2,也是删除失败
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve2 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve2 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 删除测试的入库单商品
		Map<String, Object> deletepOrderCommparams = inventoryCommCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, inventoryCommCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(deletepOrderCommparams);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(deletepOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 删除测试的商品(删除商品时会连它的所有条形码都删除掉)
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest10() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case10:如果商品在退货单商品中有依赖，那么它的条形码都是不能够删除的");
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 根据商品ID创建一个条形码t1
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 创建退货单
		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();
		// 根据商品和条形码创建一个退货单商品
		ReturnCommoditySheetCommodity returnCommoditySheetCommodity = new ReturnCommoditySheetCommodity();
		returnCommoditySheetCommodity.setReturnCommoditySheetID(rcsCreate.getID());
		returnCommoditySheetCommodity.setCommodityID(commCreated.getID());
		returnCommoditySheetCommodity.setBarcodeID(barcode1.getID());
		returnCommoditySheetCommodity.setNO(50);
		returnCommoditySheetCommodity.setSpecification("箱");
		returnCommoditySheetCommodity.setPurchasingPrice(5.5d);
		//
		Map<String, Object> createIcCommparams = returnCommoditySheetCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, returnCommoditySheetCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		ReturnCommoditySheetCommodity returnCommoditySheetCommodityCreated = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(createIcCommparams);
		Assert.assertTrue(returnCommoditySheetCommodityCreated != null && EnumErrorCode.values()[Integer.parseInt(createIcCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		returnCommoditySheetCommodity.setIgnoreIDInComparision(true);
		if (returnCommoditySheetCommodity.compareTo(returnCommoditySheetCommodityCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 尝试删除条形码t1，删除失败
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve1 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 再根据商品ID创建一个条形码t2，这个条形码没有直接关联退货单商品
		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 尝试删除条形码t2,也是删除失败
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve2 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve2 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 删除测试的退货单商品
		Map<String, Object> deletepOrderCommparams = returnCommoditySheetCommodityCreated.getDeleteParam(BaseBO.INVALID_CASE_ID, returnCommoditySheetCommodityCreated);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		returnCommoditySheetCommodityMapper.delete(deletepOrderCommparams);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(deletepOrderCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		// 删除测试的商品(删除商品时会连它的所有条形码都删除掉)
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest11() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case11:如果商品在零售单商品中有依赖，那么它的条形码都是不能够删除的");
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 根据商品ID创建一个条形码t1
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 创建零售单
		// TODO
		// 根据商品和条形码创建一个零售单商品
		RetailTradeCommodity retailTradeCommodity = new RetailTradeCommodity();
		retailTradeCommodity.setTradeID(1);
		retailTradeCommodity.setCommodityID(commCreated.getID());
		retailTradeCommodity.setBarcodeID(barcode1.getID());
		retailTradeCommodity.setNO(0);
		retailTradeCommodity.setPriceOriginal(11);
		retailTradeCommodity.setPriceReturn(0.5d);
		retailTradeCommodity.setNOCanReturn(1);
		retailTradeCommodity.setPriceVIPOriginal(1d);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID1);
		//
		Map<String, Object> createRtCommparams = retailTradeCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreated = (RetailTradeCommodity) retailTradeCommodityMapper.create(createRtCommparams);
		Assert.assertTrue(retailTradeCommodityCreated != null && EnumErrorCode.values()[Integer.parseInt(createRtCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		retailTradeCommodity.setIgnoreIDInComparision(true);
		if (retailTradeCommodity.compareTo(retailTradeCommodityCreated) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 尝试删除条形码t1，删除失败
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve1 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve1 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 再根据商品ID创建一个条形码t2，这个条形码没有直接关联零售单商品
		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 尝试删除条形码t2,也是删除失败
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve2 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve2 != null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// // 删除测试的零售单商品
		// Map<String, Object> deletepRtCommparams =
		// retailTradeCommodityCreated.getDeleteParam(BaseBO.INVALID_CASE_ID,
		// retailTradeCommodityCreated);
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// retailTradeCommodityMapper.delete(deletepRtCommparams);
		// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(deletepRtCommparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
		// EnumErrorCode.EC_NoError);
		// 删除测试的商品(删除商品时会连它的所有条形码都删除掉)
		// commCreated.setOperatorStaffID(Commodity.DEFINE_SET_StaffID(STAFF_ID3));
		// 商品关联了零售单之后就有了库存，或者商品有了商品来源表依赖，不能删除
		// BaseCommodityTest.deleteCommodity(commCreated, mapMapper);
	}

	@Test
	public void deleteTest12() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case12:如果商品在指定促销范围中有依赖，那么它的条形码都是不能够删除的");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		Promotion pCreate = createPromotion(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(pCreate.getID());
		createPromotionScope(ps);
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 尝试删除条形码t1，删除失败
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 尝试删除条形码t2,也是删除失败
		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		deletePromotion(pCreate);
		// BaseCommodityTest.deleteCommodity(cCreate, mapMapper);
	}

	@Test
	public void deleteTest13() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case13:如果商品在指定促销范围中有依赖，但是活动已经结束了，商品又没有其他依赖，那么它的条形码还是不能够删除的");
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(4);
		createPromotionScope(ps);
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		// 尝试删除条形码t1，删除失败
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 尝试删除条形码t2,也是删除失败
		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// BaseCommodityTest.deleteCommodity(cCreate, mapMapper);
	}

	@Test
	public void deleteTest14() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case14:如果商品在指定促销范围中有依赖，但促销活动已经被删除了，商品又没有其他依赖，那么它的条形码还是不能够删除的");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionDiscount(-1);
		Promotion pCreate = createPromotion(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 创建促销范围
		PromotionScope ps = new PromotionScope();
		ps.setCommodityID(cCreate.getID());
		ps.setPromotionID(pCreate.getID());
		createPromotionScope(ps);
		// 删除促销
		deletePromotion(pCreate);
		// 尝试删除条形码t1，删除失败
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 尝试删除条形码t2,也是删除失败
		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 商品参与了指定型促销，不能删除
		// BaseCommodityTest.deleteCommodity(cCreate, mapMapper);
	}

	@Test
	public void deleteTest15() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case15:如果商品参与了全场促销，商品又没有其他依赖，那么它的条形码是能够删除的");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionDiscount(-1);
		p.setScope(0);
		createPromotion(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 尝试删除条形码t1，删除成功
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 尝试删除条形码t2,也是删除成功
		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		barcode2.setOperatorStaffID(STAFF_ID3);
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除商品
		BaseCommodityTest.deleteCommodityViaMapper(cCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest16() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case16:如果商品参与了全场促销，促销已经被删除了，商品又没有其他依赖，那么它的条形码是能够删除的");
		// 创建促销
		Promotion p = BasePromotionTest.DataInput.getPromotion();
		p.setType(EnumTypePromotion.ETP_DecreaseOnAmount.getIndex());
		p.setExcecutionDiscount(-1);
		p.setScope(0);
		Promotion pCreate = createPromotion(p);
		// 创建商品
		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		c.setPurchaseFlag(1);
		Commodity cCreate = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		// 删除促销
		deletePromotion(pCreate);
		// 尝试删除条形码t1，删除成功
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 尝试删除条形码t2,也是删除成功
		tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(cCreate.getID());
		Barcodes barcode2 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
		barcode2.setOperatorStaffID(STAFF_ID3);
		deleteBarcodesParam = barcode2.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode2);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除商品
		BaseCommodityTest.deleteCommodityViaMapper(cCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest17() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case17:如果商品在采购订单商品中有依赖，采购订单被删除后，商品无其他依赖，那么它的条形码能够删除至一个的");
		// 创建一个采购订单
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder purchasingOrder = purchasingOrderCreate(po);
		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 根据商品ID创建一个条形码t1
		Barcodes tmpCreateBarcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcode1 = BaseBarcodesTest.createBarcodesViaMapper(tmpCreateBarcodes, BaseBO.INVALID_CASE_ID);
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
		// 删除该采购订单，会把从表信息删除掉
		deleteAndVerificationPurchasingOrder(purchasingOrder);
		// 尝试删除条形码t1，删除成功
		barcode1.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> deleteBarcodesParam = barcode1.getDeleteParam(BaseBO.INVALID_CASE_ID, barcode1);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.delete(deleteBarcodesParam);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel barcodesRetrieve1 = barcodesMapper.retrieve1(deleteBarcodesParam);
		Assert.assertTrue(barcodesRetrieve1 == null && EnumErrorCode.values()[Integer.parseInt(deleteBarcodesParam.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		//
		commCreated.setOperatorStaffID(STAFF_ID3);
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieve1Test1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常查询");

		Commodity c = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodity = BaseCommodityTest.createCommodityViaMapper(c, BaseBO.CASE_Commodity_CreateSingle);
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commodity.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);

		Map<String, Object> params = barcodesCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel retrieve1 = barcodesMapper.retrieve1(params);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		//
		retrieve1.setOperatorStaffID(barcodes.getOperatorStaffID());// TODO 为了通过下面的检查，设置相应的值
		//
		String error1 = retrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");
		//
		retrieve1.setOperatorStaffID(0);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commodity, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteBySimpleCommodityIDTest1() throws Exception {
		// 这里有个隐患，第一次运行是按照测试删除，第二次运行是用第一次运行后的数据操作，那么之前删除成功的在此次测试就是删除不存在的条形码
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 1: 单品无多包装 1条码 删除成功");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(70);
		barcodes.setOperatorStaffID(STAFF_ID3);
		BaseBarcodesTest.deleteBarcodesBySimpleCommodityIDViaMapper(barcodes);
	}

	@Test
	public void deleteBySimpleCommodityIDTest2() throws Exception {
		// 这里有个隐患，第一次运行是按照测试删除，第二次运行是用第一次运行后的数据操作，那么之前删除成功的在此次测试就是删除不存在的条形码
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 2: 单品无多包装 2条码 删除成功");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(71);
		barcodes.setOperatorStaffID(STAFF_ID3);
		BaseBarcodesTest.deleteBarcodesBySimpleCommodityIDViaMapper(barcodes);
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
	}

	@Test
	public void deleteBySimpleCommodityIDTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 3: 多包装 1条码 删除失败");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(72);
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete3 = barcodes.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteBySimpleCommodityID(paramForDelete3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		Map<String, Object> paramForRetrieveN3 = barcodes.getRetrieveNParam(BaseAction.INVALID_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN3 = barcodesMapper.retrieveN(paramForRetrieveN3);
		assertTrue(retrieveN3.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramForRetrieveN3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteBySimpleCommodityIDTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 4: 多包装 2条码 删除失败");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(73);
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete4 = barcodes.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteBySimpleCommodityID(paramForDelete4);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		//
		Map<String, Object> paramForRetrieveN4 = barcodes.getRetrieveNParam(BaseAction.INVALID_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN4 = barcodesMapper.retrieveN(paramForRetrieveN4);
		assertTrue(retrieveN4.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramForRetrieveN4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteBySimpleCommodityIDTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 5: 单品有多包装 单品1条码 多包装1条码 删除成功");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(74);
		barcodes.setOperatorStaffID(STAFF_ID3);
		BaseBarcodesTest.deleteBarcodesBySimpleCommodityIDViaMapper(barcodes);
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
	}

	@Test
	public void deleteBySimpleCommodityIDTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 6: 单品有多包装 单品2条码 多包装1条码 删除成功");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(76);
		barcodes.setOperatorStaffID(STAFF_ID3);
		BaseBarcodesTest.deleteBarcodesBySimpleCommodityIDViaMapper(barcodes);
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
	}

	@Test
	public void deleteBySimpleCommodityIDTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 7: 单品有多包装 单品1条码 多包装2条码 删除成功");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(78);
		barcodes.setOperatorStaffID(STAFF_ID3);
		BaseBarcodesTest.deleteBarcodesBySimpleCommodityIDViaMapper(barcodes);
	}

	@Test
	public void deleteBySimpleCommodityIDTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 8: 单品有多包装 单品2条码 多包装2条码 删除成功");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(80);
		barcodes.setOperatorStaffID(STAFF_ID3);
		BaseBarcodesTest.deleteBarcodesBySimpleCommodityIDViaMapper(barcodes);
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
	}

	@Test
	public void deleteBySimpleCommodityIDTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 9: 单品有2个多包装 单品1条码 多包装各1条码 删除成功");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(82);
		barcodes.setOperatorStaffID(STAFF_ID3);
		BaseBarcodesTest.deleteBarcodesBySimpleCommodityIDViaMapper(barcodes);
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
	}

	@Test
	public void deleteBySimpleCommodityIDTest10() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 10: 单品有2个多包装 单品2条码 多包装各2条码 删除成功");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(85);
		barcodes.setOperatorStaffID(STAFF_ID3);
		BaseBarcodesTest.deleteBarcodesBySimpleCommodityIDViaMapper(barcodes);
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
	}

	@Test
	public void deleteBySimpleCommodityIDTest11() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 11: 商品采购单商品有记录 删除失败");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(88);
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete11 = barcodes.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteBySimpleCommodityID(paramForDelete11);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError); //... 添加检查依赖func后需要修改验证结果
		//
		Map<String, Object> paramForRetrieveN11 = barcodes.getRetrieveNParam(BaseAction.INVALID_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN11 = barcodesMapper.retrieveN(paramForRetrieveN11);
		//
		assertTrue(retrieveN11.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramForRetrieveN11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteBySimpleCommodityIDTest12() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 12: 商品的入库商品有记录 删除失败");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(89);
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete12 = barcodes.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteBySimpleCommodityID(paramForDelete12);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete12.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError); //... 添加检查依赖func后需要修改验证结果
		//
		Map<String, Object> paramForRetrieveN12 = barcodes.getRetrieveNParam(BaseAction.INVALID_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN12 = barcodesMapper.retrieveN(paramForRetrieveN12);
		//
		assertTrue(retrieveN12.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramForRetrieveN12.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteBySimpleCommodityIDTest13() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 13: 商品的零售商品有记录 删除失败");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(90);
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete13 = barcodes.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteBySimpleCommodityID(paramForDelete13);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete13.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError); //... 添加检查依赖func后需要修改验证结果
		//
		Map<String, Object> paramForRetrieveN13 = barcodes.getRetrieveNParam(BaseAction.INVALID_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN13 = barcodesMapper.retrieveN(paramForRetrieveN13);
		//
		assertTrue(retrieveN13.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramForRetrieveN13.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteBySimpleCommodityIDTest14() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 14: 商品的盘点商品有记录 删除失败");
		//
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(91);
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete14 = barcodes.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteBySimpleCommodityID(paramForDelete14);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete14.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError); //... 添加检查依赖func后需要修改验证结果
		//
		Map<String, Object> paramForRetrieveN14 = barcodes.getRetrieveNParam(BaseAction.INVALID_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveN14 = barcodesMapper.retrieveN(paramForRetrieveN14);
		//
		assertTrue(retrieveN14.size() > 0 && EnumErrorCode.values()[Integer.parseInt(paramForRetrieveN14.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
	}


	@Test
	public void deleteByCombinationCommodityIDTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:删除单品的条形码");
		// 创建商品(单品)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);

		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);;

		Map<String, Object> paramForDelete1 = barcodesCreate.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteByCombinationCommodityID(paramForDelete1);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的条形码，这条不能被删除，因为这是单品，这方法只能删除组合商品的条形码
		Map<String, Object> barcodesParams = barcodesCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel retrieve1 = barcodesMapper.retrieve1(barcodesParams);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(barcodesParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertNotNull(retrieve1);

		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteByCombinationCommodityIDTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:删除多包装的条形码");
		// 创建商品(多包装)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm.setRefCommodityID(3);
		comm.setRefCommodityMultiple(3);
		comm.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + comm.getBarcodes() + ";");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateMultiPackaging);

		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);

		Map<String, Object> paramForDelete2 = barcodesCreate.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteByCombinationCommodityID(paramForDelete2);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的条形码，这条不能被删除，因为这是多包装商品，这方法只能删除组合商品的条形码
		Map<String, Object> barcodesParams2 = barcodesCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel retrieve1 = barcodesMapper.retrieve1(barcodesParams2);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(barcodesParams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertNotNull(retrieve1);

		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteByCombinationCommodityIDTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:删除组合商品的条形码");
		// 创建商品(组合)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateComposition);
		//
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		//
		barcodesCreate.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete3 = barcodesCreate.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteByCombinationCommodityID(paramForDelete3);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 查询删除的条形码，删除成功，查不出数据
		Map<String, Object> barcodesParams3 = barcodesCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel retrieve3 = barcodesMapper.retrieve1(barcodesParams3);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(barcodesParams3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, barcodesParams3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertNull(retrieve3, "删除失败！");
		//
		BaseCommodityTest.retrieveNCommodityHistory(commCreated.getID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteByMultiPackagingCommodityIDTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:删除单品的条形码");
		// 创建商品(单品)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);

		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);

		Map<String, Object> paramForDelete1 = barcodesCreate.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteByMultiPackagingCommodityID(paramForDelete1);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的条形码，这条不能被删除，因为这是单品，这方法只能删除多包装商品的条形码
		Map<String, Object> barcodesParams = barcodesCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel retrieve = barcodesMapper.retrieve1(barcodesParams);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(barcodesParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertNotNull(retrieve, "删除失败！");

		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteByMultiPackagingCommodityIDTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:删除多包装商品有1条形码的条形码");
		// 创建商品(多包装)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm.setRefCommodityID(3);
		comm.setRefCommodityMultiple(3);
		comm.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + comm.getBarcodes() + ";");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		//
		barcodesCreate.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete2 = barcodesCreate.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteByMultiPackagingCommodityID(paramForDelete2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 查询删除的条形码，删除成功，差不到数据
		Map<String, Object> barcodesParams2 = barcodesCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel retrieve2 = barcodesMapper.retrieve1(barcodesParams2);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(barcodesParams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertNull(retrieve2, "删除失败！");
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteByMultiPackagingCommodityIDTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:删除多包装商品有2条形码的条形码");
		// 创建商品(多包装)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		comm.setRefCommodityID(3);
		comm.setRefCommodityMultiple(3);
		comm.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + comm.getBarcodes() + ";");
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateMultiPackaging);

		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);

		Barcodes barcodes2 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		BaseBarcodesTest.createBarcodesViaMapper(barcodes2, BaseBO.INVALID_CASE_ID);

		barcodesCreate.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> paramForDelete = barcodesCreate.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteByMultiPackagingCommodityID(paramForDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		// 查询删除的条形码，删除成功，差不到数据
		Map<String, Object> barcodesParams3 = barcodesCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel retrieve3 = barcodesMapper.retrieve1(barcodesParams3);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(barcodesParams3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertNull(retrieve3, "删除失败！");
		//
		BaseCommodityTest.retrieveNCommodityHistory(barcodes.getCommodityID(), barcodes.getOperatorStaffID(), commodityHistoryMapper);
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteByMultiPackagingCommodityIDTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 4: 删除组合商品的条形码");
		// 创建商品(组合)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateComposition);

		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);

		Map<String, Object> paramForDelete4 = barcodesCreate.getDeleteParam(BaseBO.CASE_DeleteBarcodesByCommodityID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.deleteByMultiPackagingCommodityID(paramForDelete4);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramForDelete4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
		// 查询删除的条形码，这条不能被删除，因为这是组合商品，这方法只能删除多包装商品的条形码
		Map<String, Object> barcodesParams4 = barcodesCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, barcodesCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel retrieve4 = barcodesMapper.retrieve1(barcodesParams4);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(barcodesParams4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		Assert.assertNotNull(retrieve4, "删除失败！");

		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常修改条形码");
		// 创建商品(单品)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		// 创建条形码
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		// 修改条形码
		barcodesCreate.setBarcode("354" + System.currentTimeMillis() % 1000000);
		barcodesCreate.setOperatorStaffID((barcodes.getOperatorStaffID())); // ...TODO 为了能通过下面的断言
		//
		ErrorInfo ecOut = new ErrorInfo();
		ecOut.setErrorCode(EnumErrorCode.EC_NoError);
		Barcodes barcodesUpdate = BaseBarcodesTest.updateBarcodesViaMapper(barcodesCreate, BaseBO.INVALID_CASE_ID, ecOut);
		Assert.assertNotNull(barcodesUpdate, "修改失败！");
		//
		barcodesCreate.setOperatorStaffID(0);// 还原相应的值
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:修改的条形码ID不存在");
		// 修改条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setID(BaseAction.INVALID_ID);
		barcodes.setCommodityID(1);
		barcodes.setBarcode("354" + System.currentTimeMillis() % 1000000);
		//
		ErrorInfo ecOut = new ErrorInfo();
		ecOut.setErrorCode(EnumErrorCode.EC_NoSuchData);
		Barcodes barcodesUpdate = BaseBarcodesTest.updateBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID, ecOut);
		Assert.assertNull(barcodesUpdate, "修改成功！");
	}

	@Test
	public void updateTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "修改的条形码在零售单商品中有依赖";
		Shared.caseLog("Case3:" + message);
		// 修改条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setID(1);
		barcodes.setCommodityID(1);
		barcodes.setBarcode("354" + System.currentTimeMillis() % 1000000);
		Thread.sleep(1);
		//
		Map<String, Object> params = barcodes.getUpdateParam(BaseBO.INVALID_CASE_ID, barcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesUpdate = (Barcodes) barcodesMapper.update(params);
		Assert.assertNull(barcodesUpdate, "修改成功！");
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg.toString()));
	}

	@Test
	public void updateTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4:传入的CommodityID不存在");
		// 创建商品(单品)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		// 创建条形码
		Barcodes barcodes = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate = BaseBarcodesTest.createBarcodesViaMapper(barcodes, BaseBO.INVALID_CASE_ID);
		// 修改条形码
		Barcodes barcodes1 = new Barcodes();
		barcodes1.setID(barcodesCreate.getID());
		barcodes1.setCommodityID(BaseAction.INVALID_ID);
		barcodes1.setBarcode("354" + System.currentTimeMillis() % 1000000);
		Thread.sleep(1);
		//
		ErrorInfo ecOut = new ErrorInfo();
		ecOut.setErrorCode(EnumErrorCode.EC_BusinessLogicNotDefined);
		Barcodes barcodesUpdate = BaseBarcodesTest.updateBarcodesViaMapper(barcodes1, BaseBO.INVALID_CASE_ID, ecOut);
		Assert.assertNull(barcodesUpdate, "修改成功！");
	}

	@Test
	public void updateTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "修改的条形码在采购订单商品中有依赖";
		Shared.caseLog("Case5:" + message);
		// 修改条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setID(101);
		barcodes.setCommodityID(1);
		barcodes.setBarcode("354" + System.currentTimeMillis() % 1000000);
		Thread.sleep(1);
		//
		Map<String, Object> params = barcodes.getUpdateParam(BaseBO.INVALID_CASE_ID, barcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesUpdate = (Barcodes) barcodesMapper.update(params);
		Assert.assertNull(barcodesUpdate, "修改成功！");
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg.toString()));
	}

	@Test
	public void updateTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "修改的条形码在入库单商品中有依赖";
		Shared.caseLog("Case6:" + message);
		// 修改条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setID(102);
		barcodes.setCommodityID(1);
		barcodes.setBarcode("354" + System.currentTimeMillis() % 1000000);
		Thread.sleep(1);
		//
		Map<String, Object> params = barcodes.getUpdateParam(BaseBO.INVALID_CASE_ID, barcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesUpdate = (Barcodes) barcodesMapper.update(params);
		Assert.assertNull(barcodesUpdate, "修改成功！");
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg.toString()));
	}

	@Test
	public void updateTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "修改的条形码在盘点单商品中有依赖";
		Shared.caseLog("Case7:" + message);
		// 修改条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setID(104);
		barcodes.setCommodityID(1);
		barcodes.setBarcode("354" + System.currentTimeMillis() % 1000000);
		Thread.sleep(1);
		//
		Map<String, Object> params = barcodes.getUpdateParam(BaseBO.INVALID_CASE_ID, barcodes);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Barcodes barcodesUpdate = (Barcodes) barcodesMapper.update(params);
		Assert.assertNull(barcodesUpdate, "修改成功！");
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg.toString()));
	}

	@Test
	public void updateTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		String message = "商品条码已存在，请重新修改";
		Shared.caseLog("Case8:" + message);
		// 创建商品(单品)
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateSingle);
		// 创建条形码1
		Barcodes barcodes1 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate1 = BaseBarcodesTest.createBarcodesViaMapper(barcodes1, BaseBO.INVALID_CASE_ID);
		// 创建条形码2
		Barcodes barcodes2 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate2 = BaseBarcodesTest.createBarcodesViaMapper(barcodes2, BaseBO.INVALID_CASE_ID);
		// 修改条形码
		barcodesCreate2.setBarcode(barcodesCreate1.getBarcode());
		barcodesCreate2.setOperatorStaffID(barcodes1.getOperatorStaffID()); // ...TODO 为了能通过下面的断言
		//
		Map<String, Object> params = barcodesCreate2.getUpdateParam(BaseBO.INVALID_CASE_ID, barcodesCreate2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.update(params);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(message.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg.toString()));
		//
		barcodesCreate2.setOperatorStaffID(0);// 还原相应的值
		//
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}
	
	@Test
	public void updateTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		//
		String errorMsg = "修改的条形码在指定促销范围中有依赖";
		Shared.caseLog("Case9:" + errorMsg);
		// 创建商品(单品)
		Commodity commdityGet = BaseCommodityTest.DataInput.getCommodity();
		commdityGet.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(commdityGet, BaseBO.CASE_Commodity_CreateSingle);
		// 创建条形码1
		Barcodes barcodesGet1 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		Barcodes barcodesCreate1 = BaseBarcodesTest.createBarcodesViaMapper(barcodesGet1, BaseBO.INVALID_CASE_ID);
		// 创建促销
		Promotion promotionGet = BasePromotionTest.DataInput.getPromotion();
		Promotion promotionCreated = BasePromotionTest.createPromotionViaMapper(promotionGet);
		// 创建促销范围
		PromotionScope promotionScope = new PromotionScope();
		promotionScope.setCommodityID(commCreated.getID());
		promotionScope.setPromotionID(promotionCreated.getID());
		createPromotionScope(promotionScope);
		// 将条形码1更新为条形码2
		Barcodes barcodesGet2 = BaseBarcodesTest.DataInput.getBarcodes(commCreated.getID());
		barcodesGet2.setID(barcodesCreate1.getID());
		//
		Map<String, Object> params = barcodesGet2.getUpdateParam(BaseBO.INVALID_CASE_ID, barcodesGet2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		barcodesMapper.update(params);
		assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		assertTrue(errorMsg.equals(params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息不正确：" + params.get(BaseAction.SP_OUT_PARAM_sErrorMsg.toString()));
		//
		barcodesGet2.setOperatorStaffID(0);// 还原相应的值
		BasePromotionTest.deletePromotionViaMapper(promotionCreated);
		// 商品有促销依赖，不能删除
//		BaseCommodityTest.deleteCommodityViaMapper(commCreated);
	}


	public ReturnCommoditySheet createReturnCommoditySheet() throws CloneNotSupportedException, InterruptedException {  //... 将来放到BaseReturnCommoditySheetTest中
		ReturnCommoditySheet rcs = new ReturnCommoditySheet();
		rcs.setStaffID(5);
		rcs.setProviderID(5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> rcsparams = rcs.getCreateParam(BaseBO.INVALID_CASE_ID, rcs);

		ReturnCommoditySheet rcsCreate = (ReturnCommoditySheet) returnCommoditySheetMapper.create(rcsparams);
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		System.out.println(rcsCreate == null ? "null" : rcsCreate);

		Assert.assertTrue(rcsCreate != null && EnumErrorCode.values()[Integer.parseInt(rcsparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建退货单成功");

		return rcsCreate;
	}

	protected Promotion createPromotion(Promotion p) {  //... 将来放到BasePromotionTest中
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsCreate = p.getCreateParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pCreate = (Promotion) promotionMapper.create(paramsCreate);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(pCreate != null, "返回的对象为null");
		err = pCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		p.setIgnoreIDInComparision(true);
		if (p.compareTo(pCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		return pCreate;
	}

	protected PromotionScope createPromotionScope(PromotionScope ps) { //...将来放到BasePromotionTest中
		String err = ps.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsCreate = ps.getCreateParam(BaseBO.INVALID_CASE_ID, ps);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PromotionScope psCreate = (PromotionScope) promotionScopeMapper.create(paramsCreate);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
				paramsCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(psCreate != null, "返回的对象为null");
		err = psCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		psCreate.setIgnoreIDInComparision(true);
		ps.setCommodityName(psCreate.getCommodityName());
		if (psCreate.compareTo(ps) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		return psCreate;
	}

	protected void deletePromotion(Promotion p) { //...将来放到BasePromotionTest中
		String err = p.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> paramsDelete = p.getDeleteParam(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		promotionMapper.delete(paramsDelete);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证是否删除成功
		Map<String, Object> paramsR1 = p.getRetrieve1Param(BaseBO.INVALID_CASE_ID, p);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Promotion pR1 = (Promotion) promotionMapper.retrieve1(paramsR1);
		//
		Assert.assertTrue(pR1.getStatus() == Promotion.EnumStatusPromotion.ESP_Deleted.getIndex() && EnumErrorCode.values()[Integer.parseInt(paramsR1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsR1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		System.out.println("删除对象成功");
	}

	protected PurchasingOrder purchasingOrderCreate(PurchasingOrder purchasingOrder) { //... 将来放到BasePurchasingOrderTest中

		// 传入参数字段验证
		String error = purchasingOrder.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");
		//
		Map<String, Object> params = purchasingOrder.getCreateParam(BaseBO.INVALID_CASE_ID, purchasingOrder);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo = (PurchasingOrder) purchasingOrderMapper.create(params);
		purchasingOrder.setIgnoreIDInComparision(true);
		if (purchasingOrder.compareTo(createPo) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(createPo != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error1 = createPo.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		return createPo;
	}

	/** 删除并结果验证创建的数据 */
	protected void deleteAndVerificationPurchasingOrder(PurchasingOrder createPo) { //... 将来放到BasePurchasingOrderTest中
		Map<String, Object> deleteParams1 = createPo.getDeleteParam(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderMapper.delete(deleteParams1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(deleteParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, deleteParams1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Map<String, Object> retrieve1Params = createPo.getRetrieve1ParamEx(BaseBO.INVALID_CASE_ID, createPo);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> poList = (List<List<BaseModel>>) purchasingOrderMapper.retrieve1Ex(retrieve1Params);
		List<BaseModel> r1Po = (List<BaseModel>) poList.get(0);
		Assert.assertTrue(r1Po.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieve1Params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieve1Params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}
}
