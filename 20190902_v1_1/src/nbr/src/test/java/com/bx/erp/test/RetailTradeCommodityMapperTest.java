package com.bx.erp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.RetailTradeCommodity;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.RetailTrade;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.commodity.CommodityHistory;
import com.bx.erp.model.commodity.CommodityShopInfo;
import com.bx.erp.model.commodity.SubCommodity;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class RetailTradeCommodityMapperTest extends BaseMapperTest {

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

		Shared.caseLog("Case1： 创建零售单商品 ");
		// 创建零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 结果验证
//		Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity(comm, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos = BaseCommodityTest.getListCommodityShopInfoByCommID(comm, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfos.get(0);
		Assert.assertTrue(commodityShopInfo != null && commodityShopInfo.getNO() == -10, "CASE1测试失败！创建零售单不正常，商品数量不正确!");
	}

	@Test
	public void createTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2： 创建退货零售单商品 ");
		// 创建零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 结果验证
//		Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity(comm, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos1 = BaseCommodityTest.getListCommodityShopInfoByCommID(comm, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo1 = (CommodityShopInfo) commodityShopInfos1.get(0);
		Assert.assertTrue(commodityShopInfo1 != null && commodityShopInfo1.getNO() == -10, "CASE2测试失败！创建零售单不正常，商品数量不正确!");
		// 创建零售退货单
		RetailTrade retailTradeReturn = (RetailTrade) rTrade.clone();
		retailTradeReturn.setSourceID(rTrade.getID());
		retailTradeReturn.setLocalSN((int) (System.currentTimeMillis() % 1000000));
		RetailTrade rTradeReturn = BaseRetailTradeTest.createRetailTrade(retailTradeReturn);
		// 创建零售退货商品
		RetailTradeCommodity retailTradeCommodityReturn = (RetailTradeCommodity) retailTradeCommodity.clone();
		retailTradeCommodityReturn.setTradeID(rTradeReturn.getID());
		retailTradeCommodityReturn.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodityReturn, EnumErrorCode.EC_NoError);
		// 结果验证
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		Commodity commR2 = BaseCommodityTest.retrieve1ExCommodity(comm, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos = BaseCommodityTest.getListCommodityShopInfoByCommID(comm, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfos.get(0);
		Assert.assertTrue(commodityShopInfo != null && commodityShopInfo.getNO() == 0, "CASE2测试失败！创建零售单不正常，商品数量不正确!");
	}

	@Test
	public void createTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3： 创建多包装零售单商品");
		// 创建零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		commodity1.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm1 = BaseCommodityTest.createCommodityViaMapper(commodity1, BaseBO.CASE_Commodity_CreateSingle);
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		commodity2.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commodity2.setType(EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity2.setRefCommodityID(comm1.getID());
		commodity2.setRefCommodityMultiple(2);
		commodity2.setMultiPackagingInfo(commodity2.getBarcodes() + ";" + commodity2.getPackageUnitID() + ";" //
				+ commodity2.getRefCommodityMultiple() + ";" + commodity2.getPriceRetail() + ";" + commodity2.getPriceVIP() + ";" //
				+ commodity2.getPriceWholesale() + ";" + commodity2.getName());
		Commodity comm2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm2.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm2.getID());
		retailTradeCommodity.setCommodityName(comm2.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 结果验证
//		Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity(comm1, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos = BaseCommodityTest.getListCommodityShopInfoByCommID(comm1, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfos.get(0);
		Assert.assertTrue(commodityShopInfo != null && commodityShopInfo.getNO() == -20, "CASE3测试失败！创建零售单不正常，商品数量不正确!");
	}

	@Test
	public void createTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case4： 创建组合零售单商品");
		// 创建零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		commodity1.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm1 = BaseCommodityTest.createCommodityViaMapper(commodity1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		commodity2.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		commodity3.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commodity3.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity comm3 = BaseCommodityTest.createCommodityViaMapper(commodity3, BaseBO.CASE_Commodity_CreateComposition);
		// 创建组合商品的子商品
		CreateSubCommodity(comm3.getID(), comm1.getID(), 10);
		CreateSubCommodity(comm3.getID(), comm2.getID(), 5);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm3.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm3.getID());
		retailTradeCommodity.setCommodityName(comm3.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 结果验证
//		Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity(comm1, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos1 = BaseCommodityTest.getListCommodityShopInfoByCommID(comm1, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo1 = (CommodityShopInfo) commodityShopInfos1.get(0);
		Assert.assertTrue(commodityShopInfo1 != null && commodityShopInfo1.getNO() == -100, "CASE4测试失败！创建零售单不正常，商品数量不正确!");
		//
//		Commodity commR2 = BaseCommodityTest.retrieve1ExCommodity(comm2, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos = BaseCommodityTest.getListCommodityShopInfoByCommID(comm2, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfos.get(0);
		Assert.assertTrue(commodityShopInfo != null && commodityShopInfo.getNO() == -50, "CASE4测试失败！创建零售单不正常，商品数量不正确!");
	}

	@Test
	public void createTest5() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5： 创建服务零售单商品");
		// 创建零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commodity.setType(EnumCommodityType.ECT_Service.getIndex());
		commodity.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		commodity.setPurchaseFlag(0);
		commodity.setPurchasingUnit("");
		commodity.setShelfLife(0);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateService);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 结果验证
//		Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity(comm, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos = BaseCommodityTest.getListCommodityShopInfoByCommID(comm, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfos.get(0);
		Assert.assertTrue(commodityShopInfo != null && commodityShopInfo.getNO() == 0, "CASE5测试失败！创建零售单不正常，商品数量不正确!");
	}

	@Test
	public void createTest6() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6： 使用不存在的商品ID创建零售商品");
		// 创建零售商品
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(1);
		retailTradeCommodity.setCommodityID(Shared.BIG_ID);
		retailTradeCommodity.setCommodityName("XXX");
		retailTradeCommodity.setBarcodeID(1);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoSuchData);
	}

	@Test
	public void createTest7() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7： 零售2次普通商品");
		// 创建零售单1
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品1
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(1);
		retailTradeCommodity.setNOCanReturn(1);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 结果验证
		int[] NO1 = { -1 };
		int[] history1 = { 1 };
		List<Commodity> list = new ArrayList<Commodity>();
		list.add(comm);
		verifyCommodityHistory(NO1, history1, list, rTrade.getShopID());
		// 创建零售单2
		RetailTrade retailTradeReturn = (RetailTrade) rTrade.clone();
		retailTradeReturn.setLocalSN((int) (System.currentTimeMillis() % 1000000));
		RetailTrade rTradeReturn = BaseRetailTradeTest.createRetailTrade(retailTradeReturn);
		// 创建零售退货商品2
		RetailTradeCommodity retailTradeCommodityReturn = (RetailTradeCommodity) retailTradeCommodity.clone();
		retailTradeCommodityReturn.setTradeID(rTradeReturn.getID());
		retailTradeCommodityReturn.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodityReturn, EnumErrorCode.EC_NoError);
		// 结果验证
		int[] NO2 = { -2 };
		int[] history2 = { 2 };
		verifyCommodityHistory(NO2, history2, list, rTrade.getShopID());
	}

	@Test
	public void createTest8() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8： 零售2次组合商品");
		// 创建零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity();
		commodity1.setNO(1);
		commodity1.setnOStart(1);
		commodity1.setPurchasingPriceStart(1D);
		Commodity comm1 = BaseCommodityTest.createCommodityViaMapper(commodity1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity();
		commodity2.setNO(2);
		commodity2.setnOStart(2);
		commodity2.setPurchasingPriceStart(2D);
		Commodity comm2 = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity();
		commodity3.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		commodity3.setType(EnumCommodityType.ECT_Combination.getIndex());
		Commodity comm3 = BaseCommodityTest.createCommodityViaMapper(commodity3, BaseBO.CASE_Commodity_CreateComposition);
		// 创建组合商品的子商品
		CreateSubCommodity(comm3.getID(), comm1.getID(), 2);
		CreateSubCommodity(comm3.getID(), comm2.getID(), 3);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm3.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm3.getID());
		retailTradeCommodity.setCommodityName(comm3.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(1);
		retailTradeCommodity.setNOCanReturn(1);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 结果验证
		int[] NO1 = { -1, -1 };
		int[] history1 = { 2, 2 };
		List<Commodity> list = new ArrayList<Commodity>();
		list.add(comm3);
		list.add(comm1);
		list.add(comm2);
		verifyCommodityHistory(NO1, history1, list, rTrade.getShopID());
		// 创建零售单2
		RetailTrade retailTradeReturn = (RetailTrade) rTrade.clone();
		retailTradeReturn.setLocalSN((int) (System.currentTimeMillis() % 1000000));
		RetailTrade rTradeReturn = BaseRetailTradeTest.createRetailTrade(retailTradeReturn);
		// 创建零售退货商品2
		RetailTradeCommodity retailTradeCommodityReturn = (RetailTradeCommodity) retailTradeCommodity.clone();
		retailTradeCommodityReturn.setTradeID(rTradeReturn.getID());
		retailTradeCommodityReturn.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodityReturn, EnumErrorCode.EC_NoError);
		// 结果验证
		int[] NO2 = { -3, -4 };
		int[] history2 = { 3, 3 };
		verifyCommodityHistory(NO2, history2, list, retailTradeReturn.getShopID());
	}

	@Test
	public void createTest9() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9：退货数量大于可退货数量 ");
		// 创建零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(20);
		retailTradeCommodity.setNOCanReturn(15);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID4);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 结果验证
//		Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity(comm, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos1 = BaseCommodityTest.getListCommodityShopInfoByCommID(comm, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo1 = (CommodityShopInfo) commodityShopInfos1.get(0);
		Assert.assertTrue(commodityShopInfo1 != null && commodityShopInfo1.getNO() == -20, "CASE9测试失败！创建零售单不正常，商品数量不正确!");
		// 创建零售退货单
		RetailTrade retailTradeReturn = (RetailTrade) rTrade.clone();
		retailTradeReturn.setSourceID(rTrade.getID());
		retailTradeReturn.setLocalSN((int) (System.currentTimeMillis() % 1000000));
		RetailTrade rTradeReturn = BaseRetailTradeTest.createRetailTrade(retailTradeReturn);
		// 创建零售退货商品
		RetailTradeCommodity retailTradeCommodityReturn = (RetailTradeCommodity) retailTradeCommodity.clone();
		retailTradeCommodityReturn.setNO(retailTradeCommodity.getNO() + 5);
		retailTradeCommodityReturn.setTradeID(rTradeReturn.getID());
		retailTradeCommodityReturn.setOperatorStaffID(STAFF_ID4);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodityReturn, EnumErrorCode.EC_BusinessLogicNotDefined);
		// 结果验证
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
//		Commodity commR2 = BaseCommodityTest.retrieve1ExCommodity(comm, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos = BaseCommodityTest.getListCommodityShopInfoByCommID(comm, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfos.get(0);
		Assert.assertTrue(commodityShopInfo != null && commodityShopInfo.getNO() == -20, "CASE9测试失败！创建零售单不正常，商品数量不正确!");
	}

	@Test
	public void createTest10() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10： 创建零售单商品，退货价priceReturn可以为0");
		// 创建零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes2 = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes2.getID());
		retailTradeCommodity.setNO(10);
		retailTradeCommodity.setNOCanReturn(10);
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		retailTradeCommodity.setPriceReturn(0);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 结果验证
		Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity(comm, EnumErrorCode.EC_NoError);
		List<BaseModel> commodityShopInfos = BaseCommodityTest.getListCommodityShopInfoByCommID(comm, Shared.DBName_Test, rTrade.getShopID());
		CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfos.get(0);
		Assert.assertTrue(commodityShopInfo != null && commodityShopInfo.getNO() == -10, "CASE1测试失败！创建零售单不正常，商品数量不正确!");
	}

	@Test
	public void createTest11() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11：创建零售单商品时，商品不存在");
		RetailTrade retailTradeGet = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade retailTradeCreate = BaseRetailTradeTest.createRetailTrade(retailTradeGet);
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setBarcodeID(Shared.BIG_ID);
		retailTradeCommodity.setTradeID(retailTradeCreate.getID());
		String err = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> params = retailTradeCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeCommodity);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCommodityMapper.create(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest12() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case12：创建零售单商品时，条形码不存在");

		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setBarcodeID(Shared.BIG_ID);
		retailTradeCommodity.setTradeID(Shared.BIG_ID);

		String err = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> params = retailTradeCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeCommodity);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCommodityMapper.create(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest13() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case13：创建零售单商品时，员工不存在");
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(Shared.BIG_ID);
		retailTradeCommodity.setOperatorStaffID(Shared.BIG_ID);

		String err = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> params = retailTradeCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeCommodity);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCommodityMapper.create(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest14() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case14：创建零售单商品时，零售单不存在");
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(Shared.BIG_ID);

		String err = retailTradeCommodity.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertTrue("".equals(err), err);
		//
		Map<String, Object> params = retailTradeCommodity.getCreateParam(BaseBO.INVALID_CASE_ID, retailTradeCommodity);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		retailTradeCommodityMapper.create(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest15() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case15：创建零售单时，有重复的商品");
		// 创建零售单
		RetailTrade retailTrade = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rTrade = BaseRetailTradeTest.createRetailTrade(retailTrade);
		// 创建零售单商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity();
		commodity.setNO(Commodity.DEFAULT_VALUE_CommodityNO);
		Commodity comm = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		//
		Barcodes barcodes = BaseBarcodesTest.retrieveNBarcodes(comm.getID(), Shared.DBName_Test);
		//
		RetailTradeCommodity retailTradeCommodity = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		retailTradeCommodity.setTradeID(rTrade.getID());
		retailTradeCommodity.setCommodityID(comm.getID());
		retailTradeCommodity.setCommodityName(comm.getName());
		retailTradeCommodity.setBarcodeID(barcodes.getID());
		retailTradeCommodity.setOperatorStaffID(STAFF_ID3);
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_NoError);
		// 重复创建
		BaseRetailTradeTest.createRetailTradeCommodity(retailTradeCommodity, EnumErrorCode.EC_Duplicated);
	}

	/**
	 * 验证商品的库存和历史记录
	 * 
	 * @param CommodityNOAfterRetailTrade
	 *            商品库存
	 * @param CommodityHistoryAfterRetailTrade
	 *            历史记录
	 * @param commList
	 *            第一位元素是零售商品，从第二位开始是子商品或者是参考商品
	 * @param shopID 
	 * @throws CloneNotSupportedException 
	 */
	public void verifyCommodityHistory(int[] CommodityNOAfterRetailTrade, int[] CommodityHistoryAfterRetailTrade, List<Commodity> commList, int shopID) throws CloneNotSupportedException {
		// 判断零售商品类型
		if (commList.get(0).getType() == EnumCommodityType.ECT_Combination.getIndex()) { // 组合商品
			// 检查库存是否正常变化
			for (int i = 1; i < commList.size(); i++) {
//				Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity(commList.get(i), EnumErrorCode.EC_NoError);
				List<BaseModel> commodityShopInfos = BaseCommodityTest.getListCommodityShopInfoByCommID(commList.get(i), Shared.DBName_Test, shopID);
				CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfos.get(0);
				Assert.assertTrue(commodityShopInfo != null && commodityShopInfo.getNO() == CommodityNOAfterRetailTrade[i - 1], "商品ID为：" + commList.get(0).getID() + "的零售商品对应的商品库存不正确!");
			}
			// 检查是否生成商品历史记录
			CommodityHistory ch = new CommodityHistory();
			for (int i = 1; i < commList.size(); i++) {
				ch.setCommodityID(commList.get(i).getID());
				ch.setFieldName("库存");
				ch.setShopID(shopID);
				Map<String, Object> params1 = ch.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch);
				DataSourceContextHolder.setDbName(Shared.DBName_Test);
				List<BaseModel> list1 = commodityHistoryMapper.retrieveN(params1);
				//
				Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
						params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
				Assert.assertTrue(list1.size() == CommodityHistoryAfterRetailTrade[i - 1], "商品历史记录不正常!");
			}
		} else {
			Commodity comm1 = commList.get(0);
			Commodity comm2 = null;
			if (commList.get(0).getType() == EnumCommodityType.ECT_MultiPackaging.getIndex()) { // 多包装商品
				comm2 = commList.get(1);
			}
			// 检查库存是否正常变化
//			Commodity commR1 = BaseCommodityTest.retrieve1ExCommodity((comm2 == null ? comm1 : comm2), EnumErrorCode.EC_NoError);
			List<BaseModel> commodityShopInfos = BaseCommodityTest.getListCommodityShopInfoByCommID(comm2 == null ? comm1 : comm2, Shared.DBName_Test, shopID);
			CommodityShopInfo commodityShopInfo = (CommodityShopInfo) commodityShopInfos.get(0);
			Assert.assertTrue(commodityShopInfo != null && commodityShopInfo.getNO() == CommodityNOAfterRetailTrade[0], "商品ID为：" + comm1.getID() + "的零售商品对应的商品库存不正确!");
			// 检查是否生成商品历史记录
			CommodityHistory ch = new CommodityHistory();
			ch.setCommodityID((comm2 == null ? comm1 : comm2).getID());
			ch.setFieldName("库存");
			Map<String, Object> params1 = ch.getRetrieveNParam(BaseBO.INVALID_CASE_ID, ch);
			DataSourceContextHolder.setDbName(Shared.DBName_Test);
			List<BaseModel> list1 = commodityHistoryMapper.retrieveN(params1);
			//
			Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, //
					params1.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
			Assert.assertTrue(list1.size() == CommodityHistoryAfterRetailTrade[0], "商品历史记录不正常!");
		}
	}

	/** 本类重复使用的创建组合商品的子商品 */
	private SubCommodity CreateSubCommodity(int commodityID, int subCommodityID, int subCommodityNO) {

		SubCommodity subCommodity = new SubCommodity();
		subCommodity.setCommodityID(commodityID);
		subCommodity.setSubCommodityID(subCommodityID);
		subCommodity.setSubCommodityNO(subCommodityNO);

		BaseCommodityTest.createSubCommodityViaMapper(subCommodity);
		return null;
	}

	@Test
	public void retrieveNTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		System.out.println("\n------------------------ retrieveN RetailTradeCommodity Test ------------------------");

		System.out.println("------------------------ 创建零售单 ----------------------");
		RetailTrade rt = BaseRetailTradeTest.DataInput.getRetailTrade();
		RetailTrade rtCreate = BaseRetailTradeTest.createRetailTrade(rt);

		RetailTradeCommodity rtc = BaseRetailTradeTest.DataInput.getRetailTradeCommodity();
		rtc.setTradeID(rtCreate.getID());
		rtc.setBarcodeID(1);
		rtc.setCommodityID(65);
		rtc.setOperatorStaffID(STAFF_ID1);
		Map<String, Object> createParams = rtc.getCreateParam(BaseBO.INVALID_CASE_ID, rtc);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		RetailTradeCommodity retailTradeCommodityCreate = (RetailTradeCommodity) retailTradeCommodityMapper.create(createParams); // ...
		rtc.setIgnoreIDInComparision(true);
		if (rtc.compareTo(retailTradeCommodityCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(retailTradeCommodityCreate != null && EnumErrorCode.values()[Integer.parseInt(createParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");

		System.out.println(retailTradeCommodityCreate == null ? "providerCreate == null" : retailTradeCommodityCreate);

		System.out.println("【创建零售单商品】测试成功！");

		Map<String, Object> params = retailTradeCommodityCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeCommodityCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retailTradeCommodityretrieveN = (List<BaseModel>) retailTradeCommodityMapper.retrieveN(params);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("查询零售单商品成功！" + retailTradeCommodityretrieveN.toString());

		System.out.println("------------------------ Case2：ID不存在 ----------------------");

		rtc.setBarcodeID(-1);

		Map<String, Object> params2 = retailTradeCommodityCreate.getRetrieveNParam(BaseBO.INVALID_CASE_ID, retailTradeCommodityCreate);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retailTradeCommodityretrieveN2 = (List<BaseModel>) retailTradeCommodityMapper.retrieveN(params2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println(retailTradeCommodityretrieveN2 == null ? "retailTradeCommodityretrieveN2 == null" : retailTradeCommodityretrieveN2);
	}
}
