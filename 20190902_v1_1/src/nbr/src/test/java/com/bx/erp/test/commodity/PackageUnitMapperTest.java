package com.bx.erp.test.commodity;

import static org.testng.Assert.assertTrue;

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
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.PackageUnit;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.InventoryCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BasePackageUnitTest;
import com.bx.erp.test.BasePurchasingOrderTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

public class PackageUnitMapperTest extends BaseMapperTest {
	@BeforeClass
	public void setup() {

		super.setUp();
	}

	@AfterClass
	public void tearDown() {

		super.tearDown();
	}

	@Test
	public void createTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// Case1:正常创建
		Shared.caseLog("Case1:正常创建");
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnit = BasePackageUnitTest.createViaMapper(pu);

		// 删除并验证数据
		BasePackageUnitTest.deleteViaMapper(packageUnit);
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);

		// Case2:重复创建
		Shared.caseLog("Case2:重复创建");
		PackageUnit pu1 = BasePackageUnitTest.DataInput.getPackageUnit();
		pu1.setName(packageUnitCreate.getName());
		//
		Map<String, Object> paramsForCreate = pu1.getCreateParam(BaseBO.INVALID_CASE_ID, pu1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PackageUnit packageUnit = (PackageUnit) packageUnitMapper.create(paramsForCreate);
		//
		Assert.assertTrue(packageUnit == null && EnumErrorCode.values()[Integer.parseInt(paramsForCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated,
				paramsForCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除并验证数据
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);

		// Case1:无商品ID，查询全部
		Shared.caseLog("Case1:无商品ID，查询全部");
		PackageUnit pu1 = BasePackageUnitTest.DataInput.getPackageUnit();
		//
		Map<String, Object> params = pu1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pu1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNList = packageUnitMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveNList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		for (BaseModel bm : retrieveNList) {
			String error = ((PackageUnit) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}

		// 删除并验证数据
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		commodity1.setRefCommodityID(commodityCreate.getID());
		commodity1.setRefCommodityMultiple(2);
		commodity1.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity1.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + commodity1.getBarcodes() + ";");
		Commodity commodityCreate1 = BaseCommodityTest.createCommodityViaMapper(commodity1, BaseBO.CASE_Commodity_CreateMultiPackaging);

		// case2:用参照商品ID查询多包装商品单位
		Shared.caseLog("case2:用参照商品ID查询多包装商品单位");
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		pu.setNormalCommodityID(commodityCreate1.getRefCommodityID());
		//
		Map<String, Object> params = pu.getRetrieveNParam(BaseBO.INVALID_CASE_ID, pu);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNList = packageUnitMapper.retrieveN(params);
		//
		Assert.assertTrue(retrieveNList.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		for (BaseModel bm : retrieveNList) {
			String error = ((PackageUnit) bm).checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(error, "");
		}

		// 删除并验证测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate1, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);

		// Case1：正常删除
		Shared.caseLog("case1：正常删除");
		PackageUnit pu1 = BasePackageUnitTest.DataInput.getPackageUnit();
		pu1.setID(packageUnitCreate.getID());
		BasePackageUnitTest.deleteViaMapper(pu1);
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "该包装单位已被采购订单商品使用，不能删除";
		Shared.caseLog("case2：" + message);
		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		// 创建采购订单
		PurchasingOrder po = BasePurchasingOrderTest.DataInput.getPurchasingOrder();
		PurchasingOrder poCreate = BasePurchasingOrderTest.createPurchasingOrderViaMapper(po);
		// 查询商品的条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodityCreate.getID());
		barcodes.setOperatorStaffID(STAFF_ID3);
		List<BaseModel> listBarcode = BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
		// 创建采购订单商品
		PurchasingOrderCommodity purchasingOrderCommodity = BasePurchasingOrderTest.DataInput.getPurchasingOrderCommodity();
		purchasingOrderCommodity.setPurchasingOrderID(poCreate.getID());
		purchasingOrderCommodity.setBarcodeID(listBarcode.get(0).getID());
		purchasingOrderCommodity.setCommodityID(commodityCreate.getID());
		PurchasingOrderCommodity purchasingOrderCommodityCreate = BasePurchasingOrderTest.createPurchasingOrderCommodity(purchasingOrderCommodity);
		//
		PackageUnit pu1 = BasePackageUnitTest.DataInput.getPackageUnit();
		pu1.setID(packageUnitCreate.getID());
		Map<String, Object> paramsForDelete = pu1.getDeleteParam(BaseBO.INVALID_CASE_ID, pu1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		packageUnitMapper.delete(paramsForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息错误：" + paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除并验证测试数据
		BasePurchasingOrderTest.deletePurchasingOrderCommodity(purchasingOrderCommodityCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void deleteTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "该包装单位已被入库单商品使用，不能删除";
		Shared.caseLog("case3：" + message);
		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建入库单
		Warehousing warehousing = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing warehousingCraate = BaseWarehousingTest.createViaMapper(warehousing);
		// 查询商品的条形码
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodityCreate.getID());
		barcodes.setOperatorStaffID(STAFF_ID3);
		List<BaseModel> listBarcode = BaseBarcodesTest.retrieveNBarcodesViaMapper(barcodes);
		// 创建入库单商品
		WarehousingCommodity wc = BaseWarehousingTest.DataInput.getWarehousingCommodity();
		wc.setWarehousingID(warehousingCraate.getID());
		wc.setCommodityID(commodityCreate.getID());
		wc.setBarcodeID(listBarcode.get(0).getID());
		WarehousingCommodity wcCreate = BaseWarehousingTest.createWarehousingCommodityViaMapper(wc);
		//
		PackageUnit pu1 = BasePackageUnitTest.DataInput.getPackageUnit();
		pu1.setID(packageUnitCreate.getID());
		Map<String, Object> paramsForDelete = pu1.getDeleteParam(BaseBO.INVALID_CASE_ID, pu1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		packageUnitMapper.delete(paramsForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息错误：" + paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除并验证测试数据
		Map<String, Object> deleteWarehousingCommodityParams = wcCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, wcCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityMapper.delete(deleteWarehousingCommodityParams);
		// 结果验证：delete WarehousingCommodity
		Map<String, Object> retrieveWarehousingCommodityParams = wcCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, wcCreate);
		List<BaseModel> bmList = warehousingCommodityMapper.retrieveN(retrieveWarehousingCommodityParams);
		Assert.assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(retrieveWarehousingCommodityParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				retrieveWarehousingCommodityParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void deleteTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "该包装单位已被盘点单商品使用，不能删除";
		Shared.caseLog("case4：" + message);
		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		InventoryCommodity inventoryCommodity = new InventoryCommodity();
		inventoryCommodity.setBarcodeID(1);
		inventoryCommodity.setCommodityID(commodityCreate.getID());
		inventoryCommodity.setNoReal(1);
		inventoryCommodity.setInventorySheetID(1);
		String err = inventoryCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		Map<String, Object> icParams = inventoryCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, inventoryCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		InventoryCommodity icCreate = (InventoryCommodity) inventoryCommodityMapper.create(icParams);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(icParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, icParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		err = icCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(err, "");
		//
		inventoryCommodity.setIgnoreIDInComparision(true);
		if (inventoryCommodity.compareTo(icCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		//
		PackageUnit pu1 = BasePackageUnitTest.DataInput.getPackageUnit();
		pu1.setID(packageUnitCreate.getID());
		Map<String, Object> paramsForDelete = pu1.getDeleteParam(BaseBO.INVALID_CASE_ID, pu1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		packageUnitMapper.delete(paramsForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息错误：" + paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除并验证测试数据
		Map<String, Object> icParamsDelete = icCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, icCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		inventoryCommodityMapper.delete(icParamsDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(icParamsDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, icParamsDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证盘点单商品是否删除成功,如果根据ID查询为空并且错误码为0则删除成功
		Map<String, Object> icParamsRN = icCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, icCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> icList = (List<BaseModel>) inventoryCommodityMapper.retrieveN(icParamsRN);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(icParamsRN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, icParamsRN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		for (BaseModel bm : icList) {
			assertTrue(bm.getID() != icCreate.getID(), "删除对象失败！");
			break;
		}
		//
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void deleteTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		String message = "该包装单位已被商品使用，不能删除";
		Shared.caseLog("case5：" + message);
		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);
		//
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setPackageUnitID(packageUnitCreate.getID());
		Commodity commodityCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		PackageUnit pu1 = BasePackageUnitTest.DataInput.getPackageUnit();
		pu1.setID(packageUnitCreate.getID());
		Map<String, Object> paramsForDelete = pu1.getDeleteParam(BaseBO.INVALID_CASE_ID, pu1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		packageUnitMapper.delete(paramsForDelete);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForDelete.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Assert.assertTrue(message.equals(paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString()), "错误信息错误：" + paramsForDelete.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除并验证测试数据
		BaseCommodityTest.deleteCommodityViaMapper(commodityCreate, EnumErrorCode.EC_NoError);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void retrieve1Test() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);

		// case1:正常根据ID查询一条数据
		Shared.caseLog("case1:正常根据ID查询一条数据");
		Map<String, Object> paramsRetrieve = packageUnitCreate.getRetrieve1Param(BaseBO.INVALID_CASE_ID, packageUnitCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PackageUnit packageUnitRetrieve1 = (PackageUnit) packageUnitMapper.retrieve1(paramsRetrieve);// ...
		//
		pu.setIgnoreIDInComparision(true);
		if (pu.compareTo(packageUnitRetrieve1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(packageUnitRetrieve1);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsRetrieve.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsRetrieve.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error = packageUnitRetrieve1.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// 删除并验证测试数据
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void updateTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);

		// case1:正常修改，包装单位不重复
		Shared.caseLog("case1:正常修改，包装单位不重复");
		PackageUnit pu1 = BasePackageUnitTest.DataInput.getPackageUnit();
		pu1.setID(packageUnitCreate.getID());
		Map<String, Object> paramsForUpdate = pu1.getUpdateParam(BaseBO.INVALID_CASE_ID, pu1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PackageUnit packageUnitUpdate = (PackageUnit) packageUnitMapper.update(paramsForUpdate); // ...
		//
		System.out.println("修改品牌对象所需要的对象：" + pu1);
		System.out.println("修改出的品牌对象是：" + packageUnitUpdate);
		//
		pu1.setIgnoreIDInComparision(true);
		if (pu1.compareTo(packageUnitUpdate) != 0) {
			Assert.assertTrue(false, "修改的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(packageUnitUpdate);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 字段验证
		String error = packageUnitUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, "");

		// 删除并验证测试数据
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
	}

	@Test
	public void updateTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建测试数据
		PackageUnit pu = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate = BasePackageUnitTest.createViaMapper(pu);
		//
		PackageUnit pu1 = BasePackageUnitTest.DataInput.getPackageUnit();
		PackageUnit packageUnitCreate1 = BasePackageUnitTest.createViaMapper(pu1);

		// case2:包装单位重复
		Shared.caseLog("case2:包装单位重复");
		PackageUnit pu2 = BasePackageUnitTest.DataInput.getPackageUnit();
		pu2.setID(packageUnitCreate.getID());
		pu2.setName(packageUnitCreate1.getName());
		Map<String, Object> paramsForUpdate = pu2.getUpdateParam(BaseBO.INVALID_CASE_ID, pu2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PackageUnit packageUnitUpdate = (PackageUnit) packageUnitMapper.update(paramsForUpdate); // ...
		//
		Assert.assertNull(packageUnitUpdate);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(paramsForUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, paramsForUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除并验证测试数据
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate);
		BasePackageUnitTest.deleteViaMapper(packageUnitCreate1);
	}
}
