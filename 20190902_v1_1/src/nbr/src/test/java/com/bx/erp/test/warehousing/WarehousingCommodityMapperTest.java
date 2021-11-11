package com.bx.erp.test.warehousing;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.Commodity.EnumStatusCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.model.warehousing.WarehousingCommodity;
import com.bx.erp.test.BaseBarcodesTest;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class WarehousingCommodityMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	@DataProvider(name = "getCreateData")
	public Object[][] getCreateData() {

		return new Object[][] { //
				// caseID=0为正常NoError的Case，caseID！=0时为出了NoError以外的任意Case
				{ 1, 5, 300, 1, 10, 3000, 22, "case1:正常添加", EnumErrorCode.EC_NoError, 0 }, //
				{ 1, 51, 300, 1, 10, 3000, 20, "case2:用多包装商品创建有一个入库单商品", EnumErrorCode.EC_BusinessLogicNotDefined, 1 }, //
				{ 1, BaseWarehousingTest.INVALID_ID, 300, 1, 10, 3000, 22, "case3:创建一个commodityID不存在的入库商品表", EnumErrorCode.EC_NoSuchData, 1 }, //
				{ BaseWarehousingTest.INVALID_ID, 2, 300, 1, 10, 3000, 20, "case4:创建一个warehousing中不存在的WarehousingID入库商品表", EnumErrorCode.EC_NoSuchData, 1 }, //
				{ 1, 6, 300, BaseWarehousingTest.INVALID_ID, 10, 3000, 20, "case5:创建一个iBarcodeID不存在的入库商品表", EnumErrorCode.EC_NoSuchData, 1 }, //
				{ 1, 45, 300, 1, 10, 3000, 20, "case6:用组合商品创建有一个入库单商品", EnumErrorCode.EC_BusinessLogicNotDefined, 1 }, //
				{ 1, 49, 300, 1, 10, 3000, 20, "case7:用已删除的组合商品创建有一个入库单商品", EnumErrorCode.EC_NoSuchData, 1 }, //
				{ 1, 50, 300, 1, 10, 3000, 20, "case8:用已删除的商品创建有一个入库单商品", EnumErrorCode.EC_NoSuchData, 1 }, //
				{ 1, 2, 300, 1, 10, 3000, 22, "case9:创建一张入库单商品中有两个相同的商品", EnumErrorCode.EC_Duplicated, 1 }, //
				// { 1, 5, 0, 1, 10, 3000, 22, "康师傅牛腩面", "case10:创建一个本次收货数量为0的入库商品",
				// EnumErrorCode.EC_BusinessLogicNotDefined, 1 }, //
		};
	}

	public EnumErrorCode create(int warehousingID, int commodityID, int no, int barcodeID, double price, double amount, int shelfLife, int caseID) throws Exception {
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		if (caseID == 0) {
			// 创建一个正常状态的普通商品
			Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
			Commodity commodity = BaseCommodityTest.createCommodityViaMapper(tmpCommodity, BaseBO.CASE_Commodity_CreateSingle);
			// 创建对应的条形码
			Barcodes barcode = BaseBarcodesTest.retrieveNBarcodes(commodity.getID(), Shared.DBName_Test);
			commodityID = commodity.getID();
			barcodeID = barcode.getID();
		}

		warehousingCommodity.setWarehousingID(warehousingID);
		warehousingCommodity.setCommodityID(commodityID);
		warehousingCommodity.setNO(no);
		warehousingCommodity.setBarcodeID(barcodeID);
		warehousingCommodity.setPrice(price);
		warehousingCommodity.setAmount(amount);
		warehousingCommodity.setShelfLife(shelfLife);

		Map<String, Object> params = warehousingCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// 检查字段合法性
		String errorMassage = warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		WarehousingCommodity wc = (WarehousingCommodity) warehousingCommodityMapper.create(params);
		if (caseID == 0) {
			warehousingCommodity.setIgnoreIDInComparision(true);
			warehousingCommodity.setNoSalable(warehousingCommodity.getNO());
			if (warehousingCommodity.compareTo(wc) != 0) {
				Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
			}
			// 检查字段合法性
			errorMassage = wc.checkCreate(BaseBO.INVALID_CASE_ID);
			Assert.assertEquals(errorMassage, "");
			// 删除创建的数据，保证可以后续可正常运行
			BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wc);
		}
		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(dataProvider = "getCreateData")
	public void createTest(int warehousingID, int commodityID, int no, int barcodeID, double price, double amount, int shelfLife, String message, EnumErrorCode ec, int caseID) throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(message);
		Assert.assertEquals(create(warehousingID, commodityID, no, barcodeID, price, amount, shelfLife, caseID), ec);

	}

	@Test
	public void createTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case11:用已删除的多包装商品创建有一个入库单商品");
		// 创建一个删除状态的多包装商品
		Commodity commodityInput = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		commodityInput.setStatus(EnumStatusCommodity.ESC_Deleted.getIndex());
		commodityInput.setNO(Commodity.DEFAULT_VALUE_CommodityNO);

		Map<String, Object> commodityParams = commodityInput.getCreateParamEx(BaseBO.INVALID_CASE_ID, commodityInput);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmLists = commodityMapper.createSimpleEx(commodityParams);
		assertTrue(bmLists != null && EnumErrorCode.values()[Integer.parseInt(commodityParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				commodityParams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		Commodity commodityCreate = (Commodity) bmLists.get(0).get(0);
		commodityInput.setIgnoreIDInComparision(true);
		if (commodityInput.compareTo(commodityCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}

		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		warehousingCommodity.setWarehousingID(1);
		warehousingCommodity.setCommodityID(commodityCreate.getID());
		warehousingCommodity.setNO(300);
		warehousingCommodity.setBarcodeID(1);
		warehousingCommodity.setPrice(10);
		warehousingCommodity.setAmount(3000);
		warehousingCommodity.setShelfLife(22);
		// 检查字段合法性
		String errorMassage = warehousingCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		Map<String, Object> params = warehousingCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wc = (WarehousingCommodity) warehousingCommodityMapper.create(params);
		assertTrue(wc == null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

	}

	@DataProvider(name = "getRetrieveNData")
	public Object[][] getRetrieveNData() {
		return new Object[][] { //
				{ 6, 1, 10, "case1:正常查询", EnumErrorCode.EC_NoError }, //
				{ -1, 1, 10, "case2:WarehousingID", EnumErrorCode.EC_NoError }, //
				{ BaseWarehousingTest.INVALID_ID, 1, 10, "case3:WarehousingID不存在", EnumErrorCode.EC_NoError }, //
		};
	}

	public EnumErrorCode retrieveN(int id, int pageIndex, int pageSize) {
		WarehousingCommodity warehousingCommodity = new WarehousingCommodity();
		warehousingCommodity.setWarehousingID(id);
		warehousingCommodity.setPageIndex(pageIndex);
		warehousingCommodity.setPageSize(pageSize);

		Map<String, Object> params = warehousingCommodity.getRetrieveNParam(BaseBO.INVALID_CASE_ID, warehousingCommodity);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityMapper.retrieveN(params);

		return EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())];
	}

	@Test(dataProvider = "getRetrieveNData")
	public void retrieveNTest(int id, int pageIndex, int pageSize, String message, EnumErrorCode ec) {
		Shared.printTestMethodStartInfo();
		Shared.caseLog(message);
		Assert.assertEquals(retrieveN(id, pageIndex, pageSize), ec);
	}

	@Test
	public void updateTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case1正常修改");
		// 先创建测试对象
		Warehousing warehousingGet = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wsCreate = BaseWarehousingTest.createViaMapper(warehousingGet);
		WarehousingCommodity wcCreate = BaseWarehousingTest.createWarehousingCommodityViaMapper(wsCreate);
		//
		wcCreate.setWarehousingID(wcCreate.getWarehousingID());
		wcCreate.setCommodityID(wcCreate.getCommodityID());
		wcCreate.setPrice(300);
		wcCreate.setNO(10);
		wcCreate.setAmount(6000);
		// 检验字段合法性
		String errorMassage = wcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		Map<String, Object> paramsUpdate = wcCreate.getUpdateParam(BaseBO.INVALID_CASE_ID, wcCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcUpdate = (WarehousingCommodity) warehousingCommodityMapper.update(paramsUpdate); // ... 这里由于创建之后不会回滚，当第二次创建，Update就会返回多条数据导致出错。
		//
		Assert.assertTrue(wcUpdate != null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsUpdate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		wcCreate.setNoSalable(wcUpdate.getNO()); // 在修改入库商品时，F_NoSalable字段是拿F_No赋值的，但是wcCreate对象是java层修改前的数据，而wcUpdate对象是修改后的对象DB层返回的数据
		if (wcUpdate.compareTo(wcCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		// 检查字段合法性
		errorMassage = wcUpdate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		// 删除创建出来的测试对象
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcUpdate);
		BaseWarehousingTest.deleteViaMapper(wsCreate);

	}

	@Test
	public void updateTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case2：更新一个WarehousingID不存在的WarehousingCommodity，返回错误码2");
		//
		Warehousing warehousingGet = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wsCreate = BaseWarehousingTest.createViaMapper(warehousingGet);
		WarehousingCommodity wcCreate = BaseWarehousingTest.createWarehousingCommodityViaMapper(wsCreate);
		//
		wcCreate.setWarehousingID(99999);
		wcCreate.setCommodityID(wcCreate.getCommodityID());
		wcCreate.setPrice(300);
		wcCreate.setNO(200);
		wcCreate.setAmount(6000);
		// 检验字段合法性
		String errorMassage = wcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		Map<String, Object> paramsUpdate2 = wcCreate.getUpdateParam(BaseBO.INVALID_CASE_ID, wcCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcUpdate2 = (WarehousingCommodity) warehousingCommodityMapper.update(paramsUpdate2);
		//
		Assert.assertTrue(wcUpdate2 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				paramsUpdate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建出来的测试对象
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcCreate);
		BaseWarehousingTest.deleteViaMapper(wsCreate);
	}

	@Test
	public void updateTest3() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("case3：更新一个CommodityID不存在的WarehousingCommodity，返回错误码2");
		//
		Warehousing warehousingGet = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wsCreate = BaseWarehousingTest.createViaMapper(warehousingGet);
		WarehousingCommodity wcCreate = BaseWarehousingTest.createWarehousingCommodityViaMapper(wsCreate);
		int oldCommodityID = wcCreate.getCommodityID();
		//
		wcCreate.setWarehousingID(wcCreate.getWarehousingID());
		wcCreate.setCommodityID(Shared.BIG_ID);
		wcCreate.setPrice(300);
		wcCreate.setNO(200);
		wcCreate.setAmount(6000);
		// 检验字段合法性
		String errorMassage = wcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");
		//
		Map<String, Object> paramsUpdate331 = wcCreate.getUpdateParam(BaseBO.INVALID_CASE_ID, wcCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcUpdate331 = (WarehousingCommodity) warehousingCommodityMapper.update(paramsUpdate331);
		//
		Assert.assertTrue(wcUpdate331 == null && EnumErrorCode.values()[Integer.parseInt(paramsUpdate331.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				paramsUpdate331.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 删除创建出来的测试对象
		wcCreate.setCommodityID(oldCommodityID); //要删除的是创建出来的，不是Shared.BIG_ID对应的
		BaseWarehousingTest.deleteWarehousingCommodityViaMapper(wcCreate);
		BaseWarehousingTest.deleteViaMapper(wsCreate);
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case1：根据入库单和商品ID删除");
		String errorMassage;
		Warehousing warehousingGet = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wsCreate = BaseWarehousingTest.createViaMapper(warehousingGet);

		WarehousingCommodity wc = DataInput.getWarehousingCommodity();
		wc.setWarehousingID(wsCreate.getID());
		// 检验字段合法性
		errorMassage = wc.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		Map<String, Object> paramsWcCreate = wc.getCreateParam(BaseBO.INVALID_CASE_ID, wc);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcCreate = (WarehousingCommodity) warehousingCommodityMapper.create(paramsWcCreate); // ...
		//
		wc.setIgnoreIDInComparision(true);
		wc.setNoSalable(wc.getNO());
		if (wc.compareTo(wcCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		assertTrue(wcCreate != null && EnumErrorCode.values()[Integer.parseInt(paramsWcCreate.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsWcCreate.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 检验字段合法性
		errorMassage = wcCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		Map<String, Object> paramsWcDelete = wc.getDeleteParam(BaseBO.INVALID_CASE_ID, wcCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityMapper.delete(paramsWcDelete);
		//
		Map<String, Object> paramsWcRetrieveN = wc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, wcCreate);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bmList = warehousingCommodityMapper.retrieveN(paramsWcRetrieveN);
		assertTrue(bmList.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsWcRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsWcRetrieveN.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除创建出来的测试对象
		BaseWarehousingTest.deleteViaMapper(wsCreate);

	}

	@Test
	public void deleteTest2() throws Exception {
		Shared.printTestMethodStartInfo();
		Shared.caseLog("Case2：根据入库单删除");
		String errorMassage;
		Warehousing warehousingGet = BaseWarehousingTest.DataInput.getWarehousing();
		Warehousing wsCreate = BaseWarehousingTest.createViaMapper(warehousingGet);

		WarehousingCommodity wc2 = DataInput.getWarehousingCommodity();
		wc2.setWarehousingID(wsCreate.getID());
		// 检验字段合法性
		errorMassage = wc2.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		Map<String, Object> paramsWcCreate2 = wc2.getCreateParam(BaseBO.INVALID_CASE_ID, wc2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		WarehousingCommodity wcCreate2 = (WarehousingCommodity) warehousingCommodityMapper.create(paramsWcCreate2); // ...
		//
		wc2.setIgnoreIDInComparision(true);
		wc2.setNoSalable(wc2.getNO());
		if (wc2.compareTo(wcCreate2) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		assertTrue(wcCreate2 != null && EnumErrorCode.values()[Integer.parseInt(paramsWcCreate2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsWcCreate2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 检验字段合法性
		errorMassage = wcCreate2.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(errorMassage, "");

		wcCreate2.setCommodityID(0);
		Map<String, Object> paramsWcDelete2 = wc2.getDeleteParam(BaseBO.INVALID_CASE_ID, wcCreate2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		warehousingCommodityMapper.delete(paramsWcDelete2);
		//
		Map<String, Object> paramsWcRetrieveN2 = wc2.getRetrieveNParam(BaseBO.INVALID_CASE_ID, wcCreate2);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> bmList2 = warehousingCommodityMapper.retrieveN(paramsWcRetrieveN2);
		//
		assertTrue(bmList2.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsWcRetrieveN2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				paramsWcRetrieveN2.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		// 删除创建出来的测试对象
		BaseWarehousingTest.deleteViaMapper(wsCreate);
	}
}
