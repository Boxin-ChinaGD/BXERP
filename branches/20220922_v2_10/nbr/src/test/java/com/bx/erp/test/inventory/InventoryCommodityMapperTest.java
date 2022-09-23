package com.bx.erp.test.inventory;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.InventorySheet;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseInventorySheetTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

import net.sf.json.JSONObject;

public class InventoryCommodityMapperTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	@Test
	public void updateTest_CASE1() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case 1:正常修改盘点单商品");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		// 修改盘点单商品的的实盘商品数量
		InventoryCommodity icU = new InventoryCommodity();
		icU.setID(icCreate.getID());
		icU.setNoReal(new Random().nextInt(500) + 1);
		InventoryCommodity icUpdate = BaseInventorySheetTest.updateInventoryCommodity(icCreate, icU, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_NoError);
		// 删除新创建的盘点单商品，以免影响测试
		BaseInventorySheetTest.deleteInventoryCommodity(icUpdate);
		// 删除新创建的盘点单，以免影响测试
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 删除商品数据
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest_CASE2() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:盘点单已提交 返回错误码7");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		// 把盘点单的状态修改为已提交，状态码为1
		BaseInventorySheetTest.submitInventorySheet(isCreate, EnumErrorCode.EC_NoError);
		icCreate.setNoSystem(commodityCreate.getNO()); // 提交盘点单后，会修改盘点单从表的F_NoSystem为盘点商品的F_NO
		// 修改盘点单商品的实盘商品数量为300
		InventoryCommodity icU = new InventoryCommodity();
		icU.setID(icCreate.getID());
		icU.setNoReal(300);
		BaseInventorySheetTest.updateInventoryCommodity(icCreate, icU, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void updateTest_CASE3() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:盘点单已审核 返回错误码7");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		// 把盘点单的状态修改为已提交，状态码为1
		InventorySheet isSubmit = BaseInventorySheetTest.submitInventorySheet(isCreate, EnumErrorCode.EC_NoError);
		icCreate.setNoSystem(commodityCreate.getNO()); // 提交盘点单后，会修改盘点单从表的F_NoSystem为盘点商品的F_NO
		isSubmit.setApproverID(isSubmit.getStaffID()); // 设置审核人ID
		// 把盘点单状态修改为已审核，状态码为2
		BaseInventorySheetTest.approveInventorySheet(isSubmit, EnumErrorCode.EC_NoError);
		// 修改盘点单商品的实盘商品数量为300
		InventoryCommodity icU = new InventoryCommodity();
		icU.setID(icCreate.getID());
		icU.setNoReal(300);
		BaseInventorySheetTest.updateInventoryCommodity(icCreate, icU, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void updateTest_CASE4() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:修改的实盘数量少于0 返回错误码7");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		// 修改盘点单商品的实盘商品数量为-1
		InventoryCommodity icU = new InventoryCommodity();
		icU.setID(icCreate.getID());
		icU.setNoReal(-1);
		BaseInventorySheetTest.updateInventoryCommodity(icCreate, icU, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 删除新创建的盘点单商品，以免影响测试
		BaseInventorySheetTest.deleteInventoryCommodity(icCreate);
		// 删除新创建的盘点单，以免影响测试
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 删除商品数据
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateTest_CASE5() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE5:盘点单已删除 返回错误码7");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 修改盘点单商品的实盘商品数量为300
		InventoryCommodity icU = new InventoryCommodity();
		icU.setID(icCreate.getID());
		icU.setNoReal(300);
		BaseInventorySheetTest.updateInventoryCommodity(icCreate, icU, BaseBO.INVALID_CASE_ID, EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@DataProvider
	public Object[][] retrieveN1() {
		return new Object[][] { { 1, 1, 10, EnumErrorCode.EC_NoError } };
	}

	public EnumErrorCode retrieveN(int inventorySheetID, int iPageIndex, int iPageSize) {
		InventoryCommodity ic = new InventoryCommodity();
		ic.setInventorySheetID(inventorySheetID);
		ic.setPageIndex(iPageIndex);
		ic.setPageSize(iPageSize);
		//
		Map<String, Object> params = ic.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ic);

		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.retrieveN(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(dataProvider = "retrieveN1")
	public void retrieveNTest(int inventorySheetID, int iPageIndex, int iPageSize, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();

		Assert.assertEquals(retrieveN(inventorySheetID, iPageIndex, iPageSize), ec);
	}

	@DataProvider
	public Object[][] delete1() {
		return new Object[][] { { 1, 1, EnumErrorCode.EC_NoError }, { 2, -1, EnumErrorCode.EC_NoError } };
	}

	public EnumErrorCode delete(int inventorySheetID, int ommodityID) {
		InventoryCommodity ic = new InventoryCommodity();
		ic.setInventorySheetID(inventorySheetID);
		ic.setPageSize(ommodityID);
		//
		Map<String, Object> params = ic.getDeleteParam(BaseBO.INVALID_CASE_ID, ic);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(params);
		//
		System.out.println("iErrorCode的值是：" + EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(dataProvider = "delete1")
	public void deleteTest(int inventorySheetID, int ommodityID, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();

		Assert.assertEquals(delete(inventorySheetID, ommodityID), ec);
	}

	@Test
	public void createTest_CASE1() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:正常插入盘点单商品");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		// 删除新创建的盘点单商品，以免影响测试
		BaseInventorySheetTest.deleteInventoryCommodity(icCreate);
		// 删除新创建的盘点单，以免影响测试
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 删除商品数据
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest_CASE2() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:同一个盘点单重复添加商品 返回1");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		InventoryCommodity icDuplicated = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		// 创建重复的盘点单商品
		BaseInventorySheetTest.createInventoryCommodity(icDuplicated, EnumErrorCode.EC_Duplicated, false);
		// 删除新创建的盘点单商品，以免影响测试
		BaseInventorySheetTest.deleteInventoryCommodity(icDuplicated);
		// 删除新创建的盘点单，以免影响测试
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 删除商品数据
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest_CASE3() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case3:添加组合商品到盘点单 返回7");
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建组合商品
		Commodity combinedCommodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		String json = JSONObject.fromObject(combinedCommodity).toString();
		combinedCommodity.setSubCommodityInfo(json);
		Commodity commCreated = BaseCommodityTest.createCommodityViaMapper(combinedCommodity, BaseBO.CASE_Commodity_CreateComposition);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commCreated.getID());
		//
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_BusinessLogicNotDefined, false);
		// 删除创建出来的InventorySheet测试对象
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 删除创建出来的Commodity测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commCreated, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest_CASE4() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case4:添加多包装商品到盘点单 返回7");
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建多包装商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + commodity.getBarcodes() + ";");
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		// commodity1.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		// commodity1.setRefCommodityID(commodityCreate.getID());
		// commodity1.setRefCommodityMultiple(2);
		// commodity1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + commodity1.getBarcodes() + ";");
		Commodity commodityCreate1 = BaseCommodityTest.createCommodityViaMapper(commodity1, BaseBO.CASE_Commodity_CreateMultiPackaging);

		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate1.getID());
		//
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_BusinessLogicNotDefined, false);
		// 删除创建出来的InventorySheet测试对象
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 删除创建出来的Commodity测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest_CASE5() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case5:添加服务商品到盘点单 返回7");
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建服务商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity(EnumCommodityType.ECT_Service.getIndex());
		comm.setShelfLife(0);
		comm.setnOStart(Commodity.NO_START_Default);
		comm.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		comm.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		comm.setPurchaseFlag(0);
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(comm, BaseBO.CASE_Commodity_CreateService);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		//
		BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_BusinessLogicNotDefined, false);
		// 删除创建出来的InventorySheet测试对象
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 删除创建出来的Commodity测试对象
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateNoRealTest_CASE1() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case1:正常修改盘点单商品的实盘数量");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		//
		InventoryCommodity icUpdate = new InventoryCommodity();
		icUpdate.setID(icCreate.getID());
		icUpdate.setNoReal(new Random().nextInt(500) + 1);
		icUpdate.setNoSystem(new Random().nextInt(500) + 1);
		BaseInventorySheetTest.updateInventoryCommodityNoReal(icCreate, icUpdate);
		// 删除新创建的盘点单商品，以免影响测试
		BaseInventorySheetTest.deleteInventoryCommodity(icCreate);
		// 删除新创建的盘点单，以免影响测试
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 删除商品数据
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void updateNoRealTest_CASE2() throws UnsupportedEncodingException, Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:修改不存在的ID盘点单商品的实盘数量");
		// 创建商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建盘点单
		InventorySheet is = BaseInventorySheetTest.DataInput.getInventorySheet();
		InventorySheet isCreate = BaseInventorySheetTest.createInventorySheet(is);
		// 创建盘点单商品
		InventoryCommodity ic = BaseInventorySheetTest.DataInput.getInventoryCommodity();
		ic.setInventorySheetID(isCreate.getID());
		ic.setCommodityID(commodityCreate.getID());
		InventoryCommodity icCreate = BaseInventorySheetTest.createInventoryCommodity(ic, EnumErrorCode.EC_NoError, false);
		// 设置不存在的盘点单商品ID
		ic.setID(Shared.BIG_ID);
		ic.setNoReal(new Random().nextInt(500) + 1);
		ic.setNoSystem(new Random().nextInt(500) + 1);
		BaseInventorySheetTest.updateInventoryCommodityNoReal(icCreate, ic);
		// 删除新创建的盘点单商品，以免影响测试
		BaseInventorySheetTest.deleteInventoryCommodity(icCreate);
		// 删除新创建的盘点单，以免影响测试
		BaseInventorySheetTest.deleteInventorySheet(isCreate);
		// 删除商品数据
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

}
