package com.bx.erp.test.purchasing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bx.erp.action.BaseAction;
import com.bx.erp.action.bo.BaseBO;
import com.bx.erp.model.Barcodes;
import com.bx.erp.model.BaseModel;
import com.bx.erp.model.CommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.model.purchasing.PurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrder.EnumStatusPurchasingOrder;
import com.bx.erp.model.purchasing.PurchasingOrderCommodity;
import com.bx.erp.model.warehousing.Warehousing;
import com.bx.erp.test.BaseCommodityTest;
import com.bx.erp.test.BaseMapperTest;
import com.bx.erp.test.BaseWarehousingTest;
import com.bx.erp.test.Shared;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class PurchasingOrderCommodityMapperTest extends BaseMapperTest {

	private static final int INVALID_ID = -1;

	public static class DataInput {
		private static PurchasingOrderCommodity orderCommInput = null;
		private static PurchasingOrder poInput = null;

		protected static final PurchasingOrderCommodity getPurchasingOrderCommodity() {
			orderCommInput = new PurchasingOrderCommodity();
			orderCommInput.setCommodityID(1);
			orderCommInput.setPurchasingOrderID(2);
			orderCommInput.setCommodityNO(Math.abs(new Random().nextInt(300)));
			orderCommInput.setPriceSuggestion(1);
			orderCommInput.setBarcodeID(1);
			return orderCommInput;
		}

		protected static final PurchasingOrder getPurchasingOrder() throws CloneNotSupportedException, InterruptedException {

			poInput = new PurchasingOrder();
			poInput.setStatus(EnumStatusPurchasingOrder.ESPO_ToApprove.getIndex());
			poInput.setStaffID(1);
			poInput.setProviderID(1);
			poInput.setRemark("Remark:" + String.valueOf(System.currentTimeMillis()).substring(6));
			Thread.sleep(1);
			poInput.setProviderName("");

			return (PurchasingOrder) poInput.clone();
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
	public void createTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1： 正常添加采购商品");
		// 创建一个正常状态的普通商品
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodity1 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = createCommodityBarcodes(commodity1);
		// 创建一个采购订单商品
		PurchasingOrderCommodity poc1 = DataInput.getPurchasingOrderCommodity();
		poc1.setCommodityID(commodity1.getID());
		poc1.setBarcodeID(barcode.getID());
		Map<String, Object> params = poc1.getCreateParam(BaseBO.INVALID_CASE_ID, poc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params);
		poc1.setIgnoreIDInComparision(true);
		if (poc1.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		String errorMsg = pocCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");
		//
		// 删除对应的采购订单商品
		Map<String, Object> params2 = pocCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pocCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(params2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2： 添加不存在的商品");
		PurchasingOrderCommodity poc2 = DataInput.getPurchasingOrderCommodity();
		poc2.setCommodityID(INVALID_ID);
		Map<String, Object> params2 = poc2.getCreateParam(BaseBO.INVALID_CASE_ID, poc2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity poc2Create = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params2);
		Assert.assertTrue(poc2Create == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象失败");
	}

	@Test
	public void createTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:添加一个不存在iPurchasingOrderID(-1)，返回错误码3");
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate3 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 为商品添加条形码
		Barcodes barcode = createCommodityBarcodes(commCreate3);
		//
		PurchasingOrderCommodity poc3 = DataInput.getPurchasingOrderCommodity();
		poc3.setPurchasingOrderID(INVALID_ID);
		poc3.setCommodityID(commCreate3.getID());
		poc3.setBarcodeID(barcode.getID());
		Map<String, Object> params3 = poc3.getCreateParam(BaseBO.INVALID_CASE_ID, poc3);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity poc3Create = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params3);
		Assert.assertTrue(poc3Create == null && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_OtherError, "创建对象失败");
		BaseCommodityTest.deleteCommodityViaMapper(commCreate3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:添加一个数据表中已经存在iPurchasingOrderID(1)、iCommodityID(1)的组合，返回错误码3");
		PurchasingOrderCommodity poc4 = DataInput.getPurchasingOrderCommodity();
		poc4.setPurchasingOrderID(1);
		poc4.setCommodityID(1);
		Map<String, Object> params4 = poc4.getCreateParam(BaseBO.INVALID_CASE_ID, poc4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity poc4Create = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params4);
		Assert.assertTrue(poc4Create == null && EnumErrorCode.values()[Integer.parseInt(params4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, "创建对象失败");
	}

	@Test
	public void createTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case5:用组合商品创建有一个采购单商品");
		// 创建一个组合商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateComposition);
		//
		PurchasingOrderCommodity poc5 = DataInput.getPurchasingOrderCommodity();
		poc5.setPurchasingOrderID(1);
		poc5.setCommodityID(commCreate.getID());
		Map<String, Object> params5 = poc5.getCreateParam(BaseBO.INVALID_CASE_ID, poc5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity poc5Create = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params5);
		Assert.assertTrue(poc5Create == null && EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest6() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6:用已经删除的多包装商品创建采购单商品，返回错误码2");
		// 创建一个 多包装商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + commodity.getBarcodes() + ";");
		Commodity commCreate1 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateMultiPackaging);
		// 删除一个多包装商品
		BaseCommodityTest.deleteCommodityViaMapper(commCreate1, EnumErrorCode.EC_NoError);

		PurchasingOrderCommodity poc6 = DataInput.getPurchasingOrderCommodity();
		poc6.setPurchasingOrderID(1);
		poc6.setCommodityID(commCreate1.getID());
		Map<String, Object> params6 = poc6.getCreateParam(BaseBO.INVALID_CASE_ID, poc6);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity poc6Create = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params6);
		Assert.assertTrue(poc6Create == null && EnumErrorCode.values()[Integer.parseInt(params6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, params6.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest7() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case7:用已经删除的普通商品创建采购单商品，返回错误码2");
		// 创建一个普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate2 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 删除一个普通商品
		BaseCommodityTest.deleteCommodityViaMapper(commCreate2, EnumErrorCode.EC_NoError);

		PurchasingOrderCommodity poc7 = DataInput.getPurchasingOrderCommodity();
		poc7.setPurchasingOrderID(1);
		poc7.setCommodityID(commCreate2.getID());
		Map<String, Object> params7 = poc7.getCreateParam(BaseBO.INVALID_CASE_ID, poc7);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity poc7Create = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params7);
		Assert.assertTrue(poc7Create == null && EnumErrorCode.values()[Integer.parseInt(params7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData, params7.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest8() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case8:用普通商品创建采购单商品，返回错误码0");
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate3 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 为商品添加条形码
		Barcodes barcode = createCommodityBarcodes(commCreate3);
		//
		PurchasingOrderCommodity poc8 = DataInput.getPurchasingOrderCommodity();
		poc8.setPurchasingOrderID(1);
		poc8.setCommodityID(commCreate3.getID());
		poc8.setBarcodeID(barcode.getID());
		Map<String, Object> params8 = poc8.getCreateParam(BaseBO.INVALID_CASE_ID, poc8);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity poc8Create = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params8);
		poc8.setIgnoreIDInComparision(true);
		if (poc8.compareTo(poc8Create) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(poc8Create != null && EnumErrorCode.values()[Integer.parseInt(params8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		String errorMsg = poc8Create.checkCreate(BaseBO.INVALID_CASE_ID);
		assertEquals(errorMsg, "");
		//
		// 删除对应的采购订单商品
		Map<String, Object> params2 = poc8Create.getDeleteParam(BaseBO.INVALID_CASE_ID, poc8Create);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(params2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(commCreate3, EnumErrorCode.EC_NoError);
	}

	@Test
	public void createTest9() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case9:用多包装商品创建采购单商品，返回错误码7");
		// 创建一个正常状态的普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_MultiPackaging.getIndex());
		commodity.setMultiPackagingInfo("123456789" + ";1;1;1;8;8;" + commodity.getBarcodes() + ";");
		Commodity commCreate4 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateMultiPackaging);
		//
		// 创建一个采购订单商品
		PurchasingOrderCommodity poc9 = DataInput.getPurchasingOrderCommodity();
		poc9.setPurchasingOrderID(1);
		poc9.setCommodityID(commCreate4.getID());
		Map<String, Object> params9 = poc9.getCreateParam(BaseBO.INVALID_CASE_ID, poc9);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity poc9Create = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params9);
		Assert.assertTrue(poc9Create == null && EnumErrorCode.values()[Integer.parseInt(params9.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params9.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest10() throws Exception {

		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case10:用已经删除组合商品创建采购单商品，返回错误码2");
		// 创建一个普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Combination.getIndex());
		Commodity commCreate5 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateComposition);
		// 删除一个普通商品
		BaseCommodityTest.deleteCommodityViaMapper(commCreate5, EnumErrorCode.EC_NoError);

		PurchasingOrderCommodity poc10 = DataInput.getPurchasingOrderCommodity();
		poc10.setPurchasingOrderID(1);
		poc10.setCommodityID(commCreate5.getID());
		Map<String, Object> params10 = poc10.getCreateParam(BaseBO.INVALID_CASE_ID, poc10);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity poc10Create = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params10);
		Assert.assertTrue(poc10Create == null && EnumErrorCode.values()[Integer.parseInt(params10.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoSuchData,
				params10.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void createTest11() throws Exception {

		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case11:用服务商品创建采购单商品。返回错误码7");
		// 创建一个普通商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Service.getIndex());
		Commodity commCreate11 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateService);

		PurchasingOrderCommodity poc11 = DataInput.getPurchasingOrderCommodity();
		poc11.setPurchasingOrderID(1);
		poc11.setCommodityID(commCreate11.getID());
		Map<String, Object> params11 = poc11.getCreateParam(BaseBO.INVALID_CASE_ID, poc11);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.create(params11);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params11.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, params11.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1：正常查询");
		PurchasingOrderCommodity poc = DataInput.getPurchasingOrderCommodity();
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = poc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, poc);
		List<BaseModel> ls = purchasingOrderCommodityMapper.retrieveN(params);

		Assert.assertTrue(ls.size() > 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
	}

	@Test
	public void retrieveNTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case2:F_PurchasingOrderID输入一个不存在的数字(-1)  返回错误码为0");
		retrieveNPurasingOrderCommodity(INVALID_ID, 0);
	}

	@Test
	public void retrieveNTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case3:创建一个采购订单，查询对应的入库数量应当为0");
		// 创建一个采购订单以及采购订单商品
		PurchasingOrder createPo1 = createPurchasingOrder();
		PurchasingOrderCommodity pocCreate = createPurchasingOrderCommodity(createPo1);

		// 检查对应的入库数量，当前入库数量应当为0
		retrieveNPurasingOrderCommodity(createPo1.getID(), 0);

		// 删除对应的采购订单商品
		Map<String, Object> params2 = pocCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pocCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(params2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	// 目前无法创建一个入库数量为0的入库单
	// @Test
	// public void retrieveNTest4() throws Exception {
	// Shared.printTestMethodStartInfo();
	//
	// Shared.caseLog("case4: 创建一个入库单，入库数量为0，查询对应的入库数量应当为0");
	//
	// // 创建一个采购订单以及采购订单商品
	// PurchasingOrder createPo1 = createPurchasingOrder();
	// PurchasingOrderCommodity pocCreate =
	// createPurchasingOrderCommodity(createPo1);
	// //
	// // 将case3的采购订单的状态从 0变为1
	// PurchasingOrder updatePo = approverToPurasingOrder(createPo1);
	// //
	// // 创建一张入库单并进行审核并进行入库，入库数量为0
	// Warehousing approverW2 = createAndApproverForWarehousing(updatePo);
	// createWarehousingCommodity(approverW2.getID(), 0);
	// //
	// retrieveNPurasingOrderCommodity(updatePo.getID(), 0);
	//
	// // 删除对应的采购订单商品
	// Map<String, Object> params2 =
	// pocCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pocCreate);
	// DataSourceContextHolder.setDbName(Shared.DBName_Test);
	// purchasingOrderCommodityMapper.delete(params2);
	// Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())],
	// EnumErrorCode.EC_NoError);
	// }

	@Test
	public void retrieveNTest4() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case5: 入库第二次，入库数量为10，查询对应的入库数量应当为10");

		// 创建一个采购订单以及采购订单商品
		PurchasingOrder createPo1 = createPurchasingOrder();
		PurchasingOrderCommodity pocCreate = createPurchasingOrderCommodity(createPo1);
		//
		// 将case3的采购订单的状态从 0变为1
		PurchasingOrder updatePo = approverToPurasingOrder(createPo1);

		// 创建一张入库单并进行审核并进行入库，入库数量为10
		Warehousing createW3 = BaseWarehousingTest.createViaMapper(updatePo);
		BaseWarehousingTest.createWarehousingCommodityViaMapper(createW3.getID(), 10);
		createW3.setApproverID(1);
		BaseWarehousingTest.approveViaMapper(createW3);
		// 查询对应的入库数量应当为10
		retrieveNPurasingOrderCommodity(updatePo.getID(), 10);

		// 删除对应的采购订单商品
		Map<String, Object> params2 = pocCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pocCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(params2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest5() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case6: 将所有采购数量入库完成，查询对应的入库数量应当为所有的采购数量");
		// 创建一个采购订单以及采购订单商品,并将采购订单的状态从 0变为1
		PurchasingOrder createPo2 = createPurchasingOrder();
		PurchasingOrderCommodity pocCreate1 = createPurchasingOrderCommodity(createPo2);
		PurchasingOrder updatePo1 = approverToPurasingOrder(createPo2);

		// 创建一张入库单并进行审核并进行入库，入库数量为所有的采购数量
		Warehousing createW = BaseWarehousingTest.createViaMapper(updatePo1);
		BaseWarehousingTest.createWarehousingCommodityViaMapper(createW.getID(), pocCreate1.getCommodityNO());
		createW.setApproverID(1);
		BaseWarehousingTest.approveViaMapper(createW);

		// 检查对应的入库数量，当前入库数量应当为所有采购的数量
		retrieveNPurasingOrderCommodity(updatePo1.getID(), pocCreate1.getCommodityNO());

		// 删除对应的采购订单商品
		Map<String, Object> params2 = pocCreate1.getDeleteParam(BaseBO.INVALID_CASE_ID, pocCreate1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(params2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	/** 第一个参数是采购订单的ID，第二个参数是为了验证数量是否正常 **/
	protected void retrieveNPurasingOrderCommodity(int iPurasingOrderID, int iNO) {
		PurchasingOrderCommodity poc4 = DataInput.getPurchasingOrderCommodity();
		poc4.setPurchasingOrderID(iPurasingOrderID);
		Map<String, Object> params = poc4.getRetrieveNParam(BaseBO.INVALID_CASE_ID, poc4);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> ls = purchasingOrderCommodityMapper.retrieveN(params);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError);
		for (BaseModel bm : ls) {
			PurchasingOrderCommodity purchasingOrderCommodity = (PurchasingOrderCommodity) bm;
			assertTrue(purchasingOrderCommodity.getWarehousingNO() == iNO);
		}
	}

	/** @param purchasingOrderMapper
	 * @param createPo2
	 * @return */
	private PurchasingOrder approverToPurasingOrder(PurchasingOrder createPo2) {
		createPo2.setStatus(PurchasingOrder.EnumStatusPurchasingOrder.ESPO_Approved.getIndex());
		Map<String, Object> updataStatusParams1 = createPo2.getUpdateParam(BaseBO.CASE_UpdatePurchasingOrderStatus, createPo2);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder updatePo1 = (PurchasingOrder) purchasingOrderMapper.updateStatus(updataStatusParams1);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(updataStatusParams1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "更新失败！");
		return updatePo1;
	}

	private PurchasingOrderCommodity createPurchasingOrderCommodity(PurchasingOrder createPo1) {
		PurchasingOrderCommodity poc3 = DataInput.getPurchasingOrderCommodity();
		poc3.setPurchasingOrderID(createPo1.getID());
		Map<String, Object> paramsPOC = poc3.getCreateParam(BaseBO.INVALID_CASE_ID, poc3);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(paramsPOC);
		poc3.setIgnoreIDInComparision(true);
		if (poc3.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsPOC.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建失败！");
		return pocCreate;
	}

	private PurchasingOrder createPurchasingOrder() throws Exception {
		PurchasingOrder po1 = DataInput.getPurchasingOrder();
		Map<String, Object> paramsPO = po1.getCreateParam(BaseBO.INVALID_CASE_ID, po1);

		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrder createPo1 = (PurchasingOrder) purchasingOrderMapper.create(paramsPO);
		po1.setIgnoreIDInComparision(true);
		if (po1.compareTo(createPo1) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(paramsPO.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError, "创建失败！");
		return createPo1;
	}

	@Test
	public void deleteTest1() throws Exception {
		Shared.printTestMethodStartInfo();

		// 创建一个商品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commodity1 = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = createCommodityBarcodes(commodity1);
		// 创建一个采购订单
		PurchasingOrder po1 = createPurchasingOrder();

		// 创建一个采购订单商品表
		PurchasingOrderCommodity poc1 = DataInput.getPurchasingOrderCommodity();
		poc1.setCommodityID(commodity1.getID());
		poc1.setPurchasingOrderID(po1.getID());
		poc1.setBarcodeID(barcode.getID());
		Map<String, Object> params = poc1.getCreateParam(BaseBO.INVALID_CASE_ID, poc1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		PurchasingOrderCommodity pocCreate = (PurchasingOrderCommodity) purchasingOrderCommodityMapper.create(params);
		poc1.setIgnoreIDInComparision(true);
		if (poc1.compareTo(pocCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(pocCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");
		//
		Map<String, Object> params2 = pocCreate.getDeleteParam(BaseBO.INVALID_CASE_ID, pocCreate);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(params2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest2() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case2:根据采购单ID删除商品");
		// 创建一个采购订单
		PurchasingOrder po1 = createPurchasingOrder();

		// 创建采购订单商品表
		PurchasingOrderCommodity poc = createPurchasingOrderCommodity(po1);

		PurchasingOrderCommodity poc2 = new PurchasingOrderCommodity();
		poc2.setCommodityID(INVALID_ID);
		poc2.setPurchasingOrderID(poc.getID());
		Map<String, Object> params2 = poc2.getDeleteParam(BaseBO.INVALID_CASE_ID, poc2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(params2);
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);
	}

	@Test
	public void deleteTest3() throws Exception {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("Case3:采购单ID和商品ID都不存在");
		PurchasingOrderCommodity poc3 = new PurchasingOrderCommodity();
		poc3.setCommodityID(INVALID_ID);
		poc3.setPurchasingOrderID(INVALID_ID);

		Map<String, Object> params3 = poc3.getDeleteParam(BaseBO.INVALID_CASE_ID, poc3);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		purchasingOrderCommodityMapper.delete(params3);
		//
		Assert.assertEquals(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())], EnumErrorCode.EC_NoError);

		System.out.println("[采购单商品Delete测试成功！]");
	}

	@Test
	public void retrieveNWarehousingTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("case1:RetrieveN PurchasingOrderCommodityWarehousing Test");
		//
		PurchasingOrderCommodity poc = DataInput.getPurchasingOrderCommodity();
		poc.setPurchasingOrderID(10);
		Map<String, Object> retrieveNParams = poc.getRetrieveNParamEx(BaseBO.CASE_PurchasingOrderCommodityRetrieveNWarhousing, poc);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> retrieveNPo = purchasingOrderCommodityMapper.retrieveNWarhousing(retrieveNParams);
		Assert.assertTrue(retrieveNPo.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象成功");
	}

	@Test
	public void retrieveNWarehousingTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog(" case2:RetrieveN PurchasingOrderCommodityNoneWarehousing Test");
		//
		PurchasingOrderCommodity poc2 = DataInput.getPurchasingOrderCommodity();
		poc2.setPurchasingOrderID(10);
		Map<String, Object> retrieveNParams2 = poc2.getRetrieveNParam(BaseBO.CASE_PurchasingOrderCommodityRetrieveNNoneWarhousing, poc2);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<BaseModel> retrieveNPo2 = purchasingOrderCommodityMapper.retrieveNNoneWarhousing(retrieveNParams2);
		Assert.assertTrue(retrieveNPo2.size() > 0 && EnumErrorCode.values()[Integer.parseInt(retrieveNParams2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象成功");
	}

	@Test
	public void retrieveNWarehousingTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE3:传入一个非法的iWarehousing -1  返回错误码为7");
		//
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(PurchasingOrderCommodity.field.getFIELD_NAME_purchasingOrderID(), 10);
		params.put("iWarehousing", -1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> retrieveNPo3 = purchasingOrderCommodityMapper.retrieveNWarhousing(params);
		Assert.assertTrue(retrieveNPo3.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "查询对象成功");
	}

	@Test
	public void retrieveNWarehousingTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		Shared.caseLog("CASE4:传入一个不存在的iPurchasingOrderID 返回错误码为0");
		//
		PurchasingOrderCommodity poc4 = DataInput.getPurchasingOrderCommodity();
		poc4.setPurchasingOrderID(999);
		Map<String, Object> retrieveNParams4 = poc4.getRetrieveNParamEx(BaseBO.CASE_PurchasingOrderCommodityRetrieveNWarhousing, poc4);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> retrieveNPo4 = purchasingOrderCommodityMapper.retrieveNWarhousing(retrieveNParams4);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(retrieveNParams4.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "查询对象成功");
		assertTrue(retrieveNPo4.size() == 2 && retrieveNPo4.get(0).size() == 0 && retrieveNPo4.get(1).size() == 0);
	}

	private Barcodes createCommodityBarcodes(Commodity commodity) {
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodity.getID());
		barcodes.setBarcode("wee" + String.valueOf(System.currentTimeMillis() % 1000000));
		barcodes.setOperatorStaffID(STAFF_ID3);
		Map<String, Object> params = barcodes.getCreateParam(BaseBO.INVALID_CASE_ID, barcodes);
		//
		DataSourceContextHolder.setDbName(Shared.DBName_Test);

		Barcodes barcodesCreate = (Barcodes) barcodesMapper.create(params);
		assertTrue(barcodesCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败。param=" + params + "\n\r barcodes=" + barcodes);
		//
		barcodes.setIgnoreIDInComparision(true);
		if (barcodes.compareTo(barcodesCreate) != 0) {
			assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		barcodesCreate.setOperatorStaffID(barcodes.getOperatorStaffID()); // ...TODO 为了能通过下面的断言
		//
		String error1 = barcodesCreate.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, "");

		return barcodesCreate;
	}

}
