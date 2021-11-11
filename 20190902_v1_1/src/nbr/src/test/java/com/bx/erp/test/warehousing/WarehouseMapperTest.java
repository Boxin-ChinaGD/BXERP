package com.bx.erp.test.warehousing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.warehousing.Warehouse;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseRetailTradeTest;
import com.bx.erp.test.BaseWarehouseTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;
import com.bx.erp.util.GeneralUtil;

public class WarehouseMapperTest extends BaseMapperTest {

	public final static double TOLERANCE = 0.000001d;

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
		Shared.caseLog("CASE1: 正确的进行创建仓库");
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		// 删除创建出来的测试对象
		BaseWarehouseTest.deleteViaMapper(warehouseCreate);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE2: 创建存在已有Name的仓库");
		// 先正常创建一个仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w); // ...
		Map<String, Object> params = w.getCreateParam(BaseBO.INVALID_CASE_ID, w);
		// Case 2: Create a category with same name
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse createWarehouseCase2 = (Warehouse) warehouseMapper.create(params); // ...
		Assert.assertTrue(createWarehouseCase2 == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		BaseWarehouseTest.deleteViaMapper(warehouseCreate);
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE3: 使用不存在的StaffID");
		Warehouse w3 = BaseWarehouseTest.DataInput.getWarehouse();
		w3.setStaffID(Shared.BigStaffID);
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		String errorMassage = w3.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		Map<String, Object> createParam3 = w3.getCreateParam(BaseBO.INVALID_CASE_ID, w3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.create(createParam3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createParam3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, createParam3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE4:地址，号码为空");
		Warehouse w5 = BaseWarehouseTest.DataInput.getWarehouse();
		w5.setAddress(null);
		w5.setPhone(null);
		w5.setStaffID(3);
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		String errorMassage = w5.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		Map<String, Object> createParam5 = w5.getCreateParam(BaseBO.INVALID_CASE_ID, w5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse warehouse5 = (Warehouse) warehouseMapper.create(createParam5);
		w5.setIgnoreIDInComparision(true);
		if (w5.compareTo(warehouse5) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(warehouse5 != null && EnumErrorCode.values()[Integer.parseInt(createParam5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				createParam5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		errorMassage = warehouse5.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		// 删除创建出来的测试对象
		BaseWarehouseTest.deleteViaMapper(warehouse5);
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Retrieve1 Warehouse Test");
		// 创建仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		BaseWarehouseTest.retrieve1WarehouseViaMapper(warehouseCreate);
		// 删除创建出来的测试对象
		BaseWarehouseTest.deleteViaMapper(warehouseCreate);
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("RetrieveN Warehouse Test");
		// 创建仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		//
		List<BaseModel> bmList = BaseWarehouseTest.retrieveNWarehouseViaMapper(warehouseCreate);
		// 删除创建出来的测试对象
		BaseWarehouseTest.deleteViaMapper(warehouseCreate);
		System.out.println("查询成功：" + bmList.toString());
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE1: 正确的进行修改仓库");
		// 创建仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		// 正常修改仓库
		Warehouse updateWh = BaseWarehouseTest.DataInput.getWarehouse();
		updateWh.setID(warehouseCreate.getID());
		Warehouse retrieve1Warehouse = BaseWarehouseTest.updateWarehouseViaMapper(updateWh);
		// 删除创建出来的测试对象
		BaseWarehouseTest.deleteViaMapper(retrieve1Warehouse);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE2: 修改为已有该名字的仓库 ");
		// 先正常创建两个名字不一样的仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		Warehouse w2 = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate2 = BaseWarehouseTest.createWareshouseViaMapper(w2);

		// 将创建出来的仓库名字修改为另一个创建出来的仓库的名字
		warehouseCreate2.setName(warehouseCreate.getName());
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		String errorMassage = warehouseCreate2.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		Map<String, Object> updateParam2 = warehouseCreate2.getUpdateParam(BaseBO.INVALID_CASE_ID, warehouseCreate2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.update(updateParam2);
		//
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, updateParam2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除创建出来的测试对象
		BaseWarehouseTest.deleteViaMapper(warehouseCreate);
		BaseWarehouseTest.deleteViaMapper(warehouseCreate2);
	}

	@Test
	public void updateTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE3: 修改仓库的StaffID为空");
		// 创建仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		// 修改仓库的StaffID为空
		Warehouse wh3 = BaseWarehouseTest.DataInput.getWarehouse();
		wh3.setID(warehouseCreate.getID());
		wh3.setStaffID(Shared.BigStaffID);
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		String errorMassage = wh3.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		Map<String, Object> updateParam3 = wh3.getUpdateParam(BaseBO.INVALID_CASE_ID, wh3);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.update(updateParam3);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(updateParam3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, updateParam3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除创建出来的测试对象
		BaseWarehouseTest.deleteViaMapper(warehouseCreate);
	}

	@Test
	public void updateTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE4: 修改仓库的联系人，电话号码，地址为空");
		// 先创建一个仓库
		// 创建仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		String errorMassage = warehouseCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		// 修改仓库的联系人，电话号码，地址为空
		Warehouse wh5 = BaseWarehouseTest.DataInput.getWarehouse();
		wh5.setID(warehouseCreate.getID());
		wh5.setAddress(null);
		wh5.setPhone(null);
		wh5.setStaffID(3);
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		errorMassage = wh5.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		Map<String, Object> updateParam5 = wh5.getUpdateParam(BaseBO.INVALID_CASE_ID, wh5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse warehouse5 = (Warehouse) warehouseMapper.update(updateParam5);
		Assert.assertTrue(warehouse5 != null && EnumErrorCode.values()[Integer.parseInt(updateParam5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				updateParam5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证
		Map<String, Object> retrieve1Param5 = warehouse5.getRetrieve1Param(BaseBO.INVALID_CASE_ID, warehouse5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve1Warehouse5 = (Warehouse) warehouseMapper.retrieve1(retrieve1Param5);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieve1Param5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, retrieve1Param5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		if (retrieve1Warehouse5.compareTo(warehouse5) != 0) {
			Assert.assertTrue(false, "读出的结果和修改的不一致");
		}
		// 用model.checkCreate()方法检验DB中出来的字段是否合法
		errorMassage = retrieve1Warehouse5.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		// 删除创建出来的测试对象
		BaseWarehouseTest.deleteViaMapper(retrieve1Warehouse5);

	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case1：正常删除一个仓库");
		// 创建仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		// 删除仓库
		Map<String, Object> params = warehouseCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.delete(params);
		// 结果验证：错误码
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证：查询被删除的测试对象
		Map<String, Object> retrieve1Param = warehouseCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve2Warehouse = (Warehouse) warehouseMapper.retrieve1(retrieve1Param);
		Assert.assertTrue(retrieve2Warehouse.getStatus() == 1, "删除测试对象失败");
		System.out.println("删除成功：");
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case1：仓库有依赖，无法删除");
		// 创建仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		// 添加入库单依赖
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		warehousing.setWarehouseID(warehouseCreate.getID());
		// 检验model字段合法性
		String errorMassage = warehousing.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params1 = warehousing.getCreateParam(BaseBO.INVALID_CASE_ID, warehousing);
		BaseModel wsCreate = warehousingMapper.create(params1);
		warehousing.setIgnoreIDInComparision(true);
		if (warehousing.compareTo(wsCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(wsCreate != null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 检验model字段合法性
		errorMassage = wsCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		// 删除仓库
		Map<String, Object> params = warehouseCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.delete(params);
		// 结果验证：错误码
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证：查询被删除的测试对象
		Map<String, Object> retrieve1Param = warehouseCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve2Warehouse = (Warehouse) warehouseMapper.retrieve1(retrieve1Param);
		Assert.assertTrue(retrieve2Warehouse.getStatus() == 0, "删除测试对象失败");

		// 删除创建出来的Warehousing测试对象
		Map<String, Object> deleteWarehousingParams = wsCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, wsCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingMapper.delete(deleteWarehousingParams);
		// 结果验证：delete Warehousing
		Map<String, Object> retrieveWarehousingParams = wsCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, wsCreate);
		List<List<BaseModel>> retrieveWarehousingList = (List<List<BaseModel>>) warehousingMapper.retrieve1Ex(retrieveWarehousingParams);
		Assert.assertTrue(retrieveWarehousingList.get(0).size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveWarehousingParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveWarehousingParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除仓库测试对象
		BaseWarehouseTest.deleteViaMapper(warehouseCreate);
	}

	@Test
	public void deleteTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case3：重复删除一个仓库");
		// 创建仓库
		Warehouse w = BaseWarehouseTest.DataInput.getWarehouse();
		Warehouse warehouseCreate = BaseWarehouseTest.createWareshouseViaMapper(w);
		// 删除仓库
		Map<String, Object> params = warehouseCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.delete(params);
		// 结果验证：错误码
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证：查询被删除的测试对象
		Map<String, Object> retrieve1Param = warehouseCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve1Warehouse = (Warehouse) warehouseMapper.retrieve1(retrieve1Param);
		Assert.assertTrue(retrieve1Warehouse.getStatus() == 1, "删除测试对象失败");
		System.out.println("删除成功：");

		// 重复删除仓库
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.delete(params);
		// 结果验证：错误码
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 结果验证：查询被删除的测试对象
		Map<String, Object> retrieve1Param2 = warehouseCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, warehouseCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Warehouse retrieve1Warehouse2 = (Warehouse) warehouseMapper.retrieve1(retrieve1Param2);
		Assert.assertTrue(retrieve1Warehouse2.getStatus() == 1, "删除测试对象失败");
		System.out.println("删除成功：");

	}

	@Test
	public void retrieveInventoryTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE1:正常查询");

		Warehouse wh = BaseWarehouseTest.DataInput.getWarehouse();
		String error = wh.checkRetrieve1(BaseBO.CASE_Warehouse_RetrieveInventory);
		Assert.assertEquals(error, "");
		Map<String, Object> params = wh.getRetrieve1Param(BaseBO.CASE_Warehouse_RetrieveInventory, wh);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehouseMapper.retrieveInventory(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveInventoryTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE2：查询创建单品后库存总额最高的商品及其库存总额");
		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commTemplate.setNO(99999);
		commTemplate.setnOStart(99999);
		commTemplate.setPurchasingPriceStart(1);
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commTemplate, BaseBO.CASE_Commodity_CreateSingle);

		Warehouse wh1 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> params1 = wh1.getRetrieve1Param(BaseBO.CASE_Warehouse_RetrieveInventory, wh1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		BaseModel bm1 = warehouseMapper.retrieveInventory(params1);
		Assert.assertTrue(((Warehouse) bm1).getCommodityName().equals(commCreate.getName()));
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 因为没有数据回滚，故创建完最大的平均采购价测试完后，要把其改成较小的数,以免对其他测试造成影响
		commCreate.setOperatorStaffID(commTemplate.getOperatorStaffID());
		commCreate.setLatestPricePurchase(0.5d);
		BaseCommodityTest.updateCommodityPrice(commCreate);
		// ...由于有库存 无法删除测试对象
	}

	@Test
	public void retrieveInventoryTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE3：验证库存总额");

		Commodity commTemplate1 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commTemplate1.setNO(99999);
		commTemplate1.setnOStart(99999);
		commTemplate1.setLatestPricePurchase(2);
		commTemplate1.setPurchasingPriceStart(commTemplate1.getLatestPricePurchase());// 期初商品latestPricePurchase要等于purchasingPriceStart
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaMapper(commTemplate1, BaseBO.CASE_Commodity_CreateSingle);

		Warehouse wh2 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> params2 = wh2.getRetrieve1Param(BaseBO.CASE_Warehouse_RetrieveInventory, wh2);
		Warehouse bm2 = (Warehouse) warehouseMapper.retrieveInventory(params2);
		// Assert.assertTrue(bm2.getCommodityName().equals(commCreate2.getName()));
		System.out.println("bm2.getfTotalInventory():" + bm2.getfTotalInventory());
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		Commodity commTemplate3 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commTemplate3.setNO(100);
		commTemplate3.setLatestPricePurchase(2);
		commTemplate3.setnOStart(commTemplate3.getNO());
		commTemplate3.setPurchasingPriceStart(commTemplate3.getLatestPricePurchase());
		Commodity commCreate3 = BaseCommodityTest.createCommodityViaMapper(commTemplate3, BaseBO.CASE_Commodity_CreateSingle);

		Warehouse wh3 = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> params3 = wh3.getRetrieve1Param(BaseBO.CASE_Warehouse_RetrieveInventory, wh3);
		Warehouse bm3 = (Warehouse) warehouseMapper.retrieveInventory(params3);
		System.out.println("bm3.getfTotalInventory():" + bm3.getfTotalInventory());
		System.out.println("bm2.getfTotalInventory() + commCreate3:" + GeneralUtil.sum(bm2.getfTotalInventory(), //
				GeneralUtil.mul(commCreate3.getLatestPricePurchase(), commCreate3.getNO())));
		Assert.assertTrue( //
				Math.abs(GeneralUtil.sub(bm3.getfTotalInventory(), //
						GeneralUtil.sum(bm2.getfTotalInventory(), //
								GeneralUtil.mul(commCreate3.getLatestPricePurchase(), commCreate3.getNO())))) < TOLERANCE);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params3.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 因为没有数据回滚，故创建完最大的平均采购价测试完后，要把其改成较小的数,以免对其他测试造成影响
		commCreate2.setOperatorStaffID(commTemplate1.getOperatorStaffID());
		commCreate2.setLatestPricePurchase(0.5d);
		BaseCommodityTest.updateCommodityPrice(commCreate2);
	}

	@Test
	public void retrieveInventoryTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("CASE4：库存总额为负数");

		Commodity commTemplate = BaseCommodityTest.DataInput.getCommodity();
		// 库存总额（对应的SP会返回3个数，库存总额是其中1个）是所有未删除的单品的最近进货价*数量然后相加的结果，为了使它变为负数，这里创建一个最近进货价很大，数量为负的数，这样相加就会为负数
		commTemplate.setLatestPricePurchase(1000000);
		List<List<BaseModel>> bmList = BaseCommodityTest.createCommodityViaMapper(BaseBO.CASE_Commodity_CreateSingle, commTemplate);
		Commodity commCreate = (Commodity) bmList.get(0).get(0);
		Barcodes barcodes = (Barcodes) bmList.get(1).get(0);
		//
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setCommodityID(commCreate.getID());
		retailTradeCommodity.setBarcodeID(barcodes.getID());
		retailTradeCommodity.setNO(9999);
		List<RetailTradeCommodity> listRetailTradeCommodity = new ArrayList<RetailTradeCommodity>();
		listRetailTradeCommodity.add(retailTradeCommodity);
		rt.setListSlave1(listRetailTradeCommodity);
		BaseRetailTradeTest.createRetailtradeViaMapper(rt);

		Warehouse wh = BaseWarehouseTest.DataInput.getWarehouse();
		Map<String, Object> params = wh.getRetrieve1Param(BaseBO.CASE_Warehouse_RetrieveInventory, wh);
		Warehouse warehouse = (Warehouse) warehouseMapper.retrieveInventory(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		Assert.assertTrue(warehouse.getfTotalInventory() < 0, "数据不符合测试目的" + warehouse.getfTotalInventory() + "不小于0");
		// 因为没有数据回滚，故创建完最大的平均采购价测试完后，要把其改成较小的数,以免对其他测试造成影响
		commCreate.setOperatorStaffID(commTemplate.getOperatorStaffID());
		commCreate.setLatestPricePurchase(0.5d);
		BaseCommodityTest.updateCommodityPrice(commCreate);
	}
}
