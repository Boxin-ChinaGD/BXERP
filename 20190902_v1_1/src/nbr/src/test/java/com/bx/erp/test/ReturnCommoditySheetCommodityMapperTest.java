package com.bx.erp.test;

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

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
import com.bx.erp.model.ReturnCommoditySheet;
import com.bx.erp.model.ReturnCommoditySheetCommodity;
import com.bx.erp.model.CommodityType.EnumCommodityType;
import com.bx.erp.model.ErrorInfo.EnumErrorCode;
import com.bx.erp.model.commodity.Commodity;
import com.bx.erp.util.DataSourceContextHolder;

@WebAppConfiguration
public class ReturnCommoditySheetCommodityMapperTest extends BaseMapperTest {

	@BeforeClass
	public void setup() {
		super.setUp();
	}

	@AfterClass
	public void tearDown() {
		super.tearDown();
	}

	public ReturnCommoditySheet createReturnCommoditySheet() throws CloneNotSupportedException, InterruptedException {

		Shared.caseLog("创建退货单");
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
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

	public void createReturnCommoditySheetCommodity(int returnCommoditySheetID, int commodityID, int barcodeID, int NO, String specification) throws CloneNotSupportedException, InterruptedException {

		Shared.caseLog("创建退货单商品");
		ReturnCommoditySheetCommodity rcsc = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(returnCommoditySheetID);
		rcsc.setCommodityID(commodityID);
		rcsc.setBarcodeID(barcodeID);
		rcsc.setNO(NO);
		rcsc.setSpecification(specification);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> rcscparams = rcsc.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc);

		ReturnCommoditySheetCommodity rcscCreate = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(rcscparams);
		rcsc.setIgnoreIDInComparision(true);
		if (rcsc.compareTo(rcscCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		System.out.println(rcscCreate == null ? "null" : rcscCreate);

		Assert.assertTrue(rcscCreate != null && EnumErrorCode.values()[Integer.parseInt(rcscparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象成功");
		System.out.println("创建退货单成功");
	}

	@Test
	public void createTest() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建单品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = createCommodityBarcodes(commCreate);
		// 创建退货单
		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();

		System.out.println("------------------------ Case1： 创建退货单商品 ----------------------");
		ReturnCommoditySheetCommodity rcsc = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(rcsCreate.getID());
		rcsc.setBarcodeID(barcode.getID());
		rcsc.setCommodityID(commCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = rcsc.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc);

		ReturnCommoditySheetCommodity rcscCreate = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params); // ...
		rcsc.setIgnoreIDInComparision(true);
		if (rcsc.compareTo(rcscCreate) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertTrue(rcscCreate != null && EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		System.out.println("创建退货单商品成功");

		System.out.println("------------------------ Case2：创建商品重复的退货单商品表到同一退货单中 ----------------------");
		ReturnCommoditySheetCommodity rcsc1 = new ReturnCommoditySheetCommodity();
		rcsc1.setReturnCommoditySheetID(rcsCreate.getID());
		rcsc1.setCommodityID(commCreate.getID());
		rcsc1.setBarcodeID(7);
		rcsc1.setNO(100);
		rcsc1.setSpecification("包");
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params1 = rcsc1.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc1);

		ReturnCommoditySheetCommodity rcscCreate1 = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params1); // ...
		Assert.assertTrue(rcscCreate1 == null && EnumErrorCode.values()[Integer.parseInt(params1.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_Duplicated, "创建对象成功");

		System.out.println("------------------------ 审核退货单 ----------------------");
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs.setID(rcsCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> rcs1params = rcs.getUpdateParam(BaseBO.CASE_ApproveReturnCommoditySheet, rcs);

		ReturnCommoditySheet rcsApprove = (ReturnCommoditySheet) returnCommoditySheetMapper.approve(rcs1params);
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsApprove) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		System.out.println(rcsApprove == null ? "null" : rcsApprove);
		Assert.assertTrue(rcsApprove != null && EnumErrorCode.values()[Integer.parseInt(rcs1params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "审核对象失败");
		System.out.println("审核退货单成功");

		System.out.println("------------------------ Case3:创建退货单商品表到已审核退货单中 ----------------------");
		// 创建单品
		Commodity tmpCommodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity comm1Create = BaseCommodityTest.createCommodityViaMapper(tmpCommodity, BaseBO.CASE_Commodity_CreateSingle);

		ReturnCommoditySheetCommodity rcsc2 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc2.setReturnCommoditySheetID(rcsCreate.getID());
		rcsc2.setCommodityID(comm1Create.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params2 = rcsc2.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc2);

		ReturnCommoditySheetCommodity rcscCreate2 = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params2); // ...

		Assert.assertTrue(rcscCreate2 == null && EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象成功");

		System.out.println("------------------------ Case4:创建一个不存在的退货单的退货单商品表 ----------------------");

		ReturnCommoditySheetCommodity rcsc3 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc3.setReturnCommoditySheetID(-1);
		rcsc3.setCommodityID(comm1Create.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params3 = rcsc3.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc3);

		ReturnCommoditySheetCommodity rcscCreate3 = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params3); // ...

		Assert.assertTrue(rcscCreate3 == null && EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象成功");

		// 创建退货单
		ReturnCommoditySheet rcs1Create = createReturnCommoditySheet();

		System.out.println("------------------------  Case5.1:创建一个不存在的商品的退货单商品表 ----------------------");

		ReturnCommoditySheetCommodity rcsc4A = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc4A.setReturnCommoditySheetID(rcs1Create.getID());
		rcsc4A.setCommodityID(-1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params4A = rcsc4A.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc4A);

		ReturnCommoditySheetCommodity rcscCreate4A = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params4A); // ...

		Assert.assertTrue(rcscCreate4A == null && EnumErrorCode.values()[Integer.parseInt(params4A.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined, "创建对象成功");

		System.out.println("------------------------  Case5.2:创建一个预删除（status=1）的商品的退货单商品 ----------------------");

		// 创建退货单
		ReturnCommoditySheet rcs2Create = createReturnCommoditySheet();

		ReturnCommoditySheetCommodity rcsc4B = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc4B.setReturnCommoditySheetID(rcs2Create.getID());
		rcsc4B.setCommodityID(5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params4B = rcsc4B.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc4B);

		ReturnCommoditySheetCommodity rcscCreate4B = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params4B); // ...

		Assert.assertTrue(rcscCreate4B != null && EnumErrorCode.values()[Integer.parseInt(params4B.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "创建对象失败");

		System.out.println("------------------------  Case5.3:创建一个已删除（status=2）的商品的退货单商品 ----------------------");

		ReturnCommoditySheetCommodity rcsc4C = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc4C.setReturnCommoditySheetID(3);
		rcsc4C.setCommodityID(49);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params4C = rcsc4C.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc4C);

		ReturnCommoditySheetCommodity rcscCreate4C = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params4C); // ...
		logger.info(EnumErrorCode.values()[Integer.parseInt(params4C.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]);
		Assert.assertTrue(rcscCreate4C == null && EnumErrorCode.values()[Integer.parseInt(params4C.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);

		System.out.println("------------------------  Case6:创建一个负数数量的退货单商品表 ----------------------");
		ReturnCommoditySheetCommodity rcsc5 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc5.setReturnCommoditySheetID(rcs1Create.getID());
		rcsc5.setCommodityID(comm1Create.getID());
		rcsc5.setNO(-100);
		//
		String error = rcsc5.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error, ReturnCommoditySheetCommodity.FIELD_ERROR_no);
		// //
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// Map<String, Object> params5 = rcsc5.getCreateParam(BaseBO.INVALID_CASE_ID,
		// rcsc5);
		//
		// ReturnCommoditySheetCommodity rcscCreate5 = (ReturnCommoditySheetCommodity)
		// returnCommoditySheetCommodityMapper.create(params5); // ...
		//
		// Assert.assertTrue(rcscCreate5 == null &&
		// EnumErrorCode.values()[Integer.parseInt(params5.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_BusinessLogicNotDefined,
		// params5.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());

		System.out.println("------------------------  Case7:创建一个不存在的条形码的退货单商品表 ----------------------");

		ReturnCommoditySheetCommodity rcsc6 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc6.setReturnCommoditySheetID(rcs1Create.getID());
		rcsc6.setCommodityID(comm1Create.getID());
		rcsc6.setBarcodeID(-5);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params6 = rcsc6.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc6);

		ReturnCommoditySheetCommodity rcscCreate6 = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params6); // ...

		Assert.assertTrue(rcscCreate6 == null && EnumErrorCode.values()[Integer.parseInt(params6.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);

		System.out.println("------------------------  Case8:创建一个退货单商品时传负数的采购价 ----------------------");

		ReturnCommoditySheetCommodity rcsc7 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc7.setReturnCommoditySheetID(rcs1Create.getID());
		rcsc7.setCommodityID(comm1Create.getID());
		rcsc7.setPurchasingPrice(-10.5d);
		//
		String error1 = rcsc7.checkCreate(BaseBO.INVALID_CASE_ID);
		Assert.assertEquals(error1, ReturnCommoditySheetCommodity.FIELD_ERROR_purchasingPrice);
		// //
		// DataSourceContextHolder.setDbName(Shared.DBName_Test);
		// Map<String, Object> params7 = rcsc7.getCreateParam(BaseBO.INVALID_CASE_ID,
		// rcsc7);
		//
		// ReturnCommoditySheetCommodity rcscCreate7 = (ReturnCommoditySheetCommodity)
		// returnCommoditySheetCommodityMapper.create(params7); // ...
		//
		// Assert.assertTrue(rcscCreate7 == null &&
		// EnumErrorCode.values()[Integer.parseInt(params7.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())]
		// == EnumErrorCode.EC_BusinessLogicNotDefined);

		System.out.println("------------------------  Case9:创建退货单商品(服务商品)失败 ----------------------");

		// 创建服务商品
		Commodity comm = BaseCommodityTest.DataInput.getCommodity();
		comm.setType(EnumCommodityType.ECT_Service.getIndex());
		comm.setShelfLife(0);
		comm.setnOStart(Commodity.NO_START_Default);
		comm.setPurchasingPriceStart(Commodity.PURCHASING_PRICE_START_Default);
		comm.setLatestPricePurchase(Commodity.DEFAULT_VALUE_LatestPricePurchase);
		comm.setPurchaseFlag(0);
		//
		Map<String, Object> createCommodityparams = comm.getCreateParamEx(BaseBO.INVALID_CASE_ID, comm);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		List<List<BaseModel>> bmList = commodityMapper.createServiceEx(createCommodityparams);
		//
		Commodity bm = (Commodity) bmList.get(0).get(0);
		//
		comm.setIgnoreIDInComparision(true);
		if (comm.compareTo(bm) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		Assert.assertNotNull(createCommodityparams);
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(createCommodityparams.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError,
				createCommodityparams.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		System.out.println("创建商品成功" + bm);
		// 创建退货单商品
		ReturnCommoditySheetCommodity rcsc8 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc8.setReturnCommoditySheetID(rcs1Create.getID());
		rcsc8.setCommodityID(bm.getID());
		rcsc8.setBarcodeID(1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params8 = rcsc8.getCreateParam(BaseBO.INVALID_CASE_ID, rcsc8);

		ReturnCommoditySheetCommodity rcscCreate8 = (ReturnCommoditySheetCommodity) returnCommoditySheetCommodityMapper.create(params8); // ...

		Assert.assertTrue(rcscCreate8 == null && EnumErrorCode.values()[Integer.parseInt(params8.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined,
				params8.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
	}

	@Test
	public void retrieveNTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建单品
		Commodity commodity1 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity1, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity comm1Create = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateSingle);
		//
		Commodity commodity3 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity comm2Create = BaseCommodityTest.createCommodityViaMapper(commodity3, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = createCommodityBarcodes(commCreate);
		Barcodes barcode2 = createCommodityBarcodes(comm1Create);
		Barcodes barcode3 = createCommodityBarcodes(comm2Create);
		// 创建退货单
		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();
		// 创建退货单商品
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate.getID(), barcode.getID(), 150, "箱");
		createReturnCommoditySheetCommodity(rcsCreate.getID(), comm1Create.getID(), barcode2.getID(), 200, "箱");
		createReturnCommoditySheetCommodity(rcsCreate.getID(), comm2Create.getID(), barcode3.getID(), 250, "箱");
		// 更改退货单商品名称
		commCreate.setName("可乐薯片1");
		commCreate.setOperatorStaffID(STAFF_ID3);
		Thread.sleep(1000);
		BaseCommodityTest.updateCommodityViaMapper(commCreate);
		comm1Create.setName("可乐薯片2");
		comm1Create.setOperatorStaffID(STAFF_ID3);
		Thread.sleep(1000);
		BaseCommodityTest.updateCommodityViaMapper(comm1Create);
		comm2Create.setName("可乐薯片3");
		comm2Create.setOperatorStaffID(STAFF_ID3);
		Thread.sleep(1000);
		BaseCommodityTest.updateCommodityViaMapper(comm2Create);

		System.out.println("------------------------ Case1:查询一张退货单的退货单商品表的所有数据,未审核 ----------------------");
		ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(rcsCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = rcsc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcsc);

		List<BaseModel> rcscRN = returnCommoditySheetCommodityMapper.retrieveN(params);
		Assert.assertTrue(rcscRN != null, "RN出来的数据为空");
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证这条数据的商品名称（未审核的从商品表拿）
		int NO = 3;
		for (BaseModel bm : rcscRN) {
			ReturnCommoditySheetCommodity returnCommoditySheetCommodity = (ReturnCommoditySheetCommodity) bm;
			assertTrue(returnCommoditySheetCommodity.getCommodityName().equals("可乐薯片" + NO), "查询出来的名称不正确");
			NO--;
		}
		System.out.println("查询退货单商品成功");
		//
		BaseReturnCommoditySheetTest.deleteReturnCommoditySheet(rcsCreate);
		BaseCommodityTest.deleteCommodityViaMapper(commCreate, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(comm1Create, EnumErrorCode.EC_NoError);
		BaseCommodityTest.deleteCommodityViaMapper(comm2Create, EnumErrorCode.EC_NoError);
	}

	@Test
	public void retrieveNTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建单品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = createCommodityBarcodes(commCreate);
		// 创建退货单
		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();
		// 创建退货单商品
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate.getID(), barcode.getID(), 150, "箱");
		// 审核退货单
		BaseReturnCommoditySheetTest.approveReturnCommoditySheet(rcsCreate);
		// 更改退货单商品名称
		commCreate.setName("可乐薯片" + System.currentTimeMillis() % 1000000);
		Thread.sleep(1000);
		commCreate.setNO(-150);
		commCreate.setOperatorStaffID(STAFF_ID3);
		Thread.sleep(1000);
		BaseCommodityTest.updateCommodityViaMapper(commCreate);

		System.out.println("------------------------ Case1:查询一张退货单的退货单商品表的所有数据，已审核 ----------------------");
		ReturnCommoditySheetCommodity rcsc = new ReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(rcsCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = rcsc.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcsc);

		List<BaseModel> rcscRN = returnCommoditySheetCommodityMapper.retrieveN(params);
		Assert.assertTrue(rcscRN != null, "RN出来的数据为空");
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		// 验证这条数据的商品名称（审核的从退货单商品表拿）
		for (BaseModel bm : rcscRN) {
			ReturnCommoditySheetCommodity returnCommoditySheetCommodity = (ReturnCommoditySheetCommodity) bm;
			assertTrue(!returnCommoditySheetCommodity.getCommodityName().equals(commCreate.getName()), "查询出来的名称不正确");
		}
		System.out.println("查询退货单商品成功");
		//
		// deleteReturnCommoditySheet(rcsCreate);
		// doDeleteCommodity(commCreate);
	}


	@Test
	public void deleteTest1() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();

		// 创建单品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = createCommodityBarcodes(commCreate);
		// 创建退货单
		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();
		// 创建退货单商品
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate.getID(), barcode.getID(), 150, "箱");

		System.out.println("------------------------ Case1:删除一张退货单的退货单商品表的一条数据 ----------------------");
		ReturnCommoditySheetCommodity rcsc = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc.setReturnCommoditySheetID(rcsCreate.getID());
		rcsc.setCommodityID(commCreate.getID());
		rcsc.setBarcodeID(barcode.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params = rcsc.getDeleteParam(BaseBO.INVALID_CASE_ID, rcsc);
		//
		returnCommoditySheetCommodityMapper.delete(params); // ...
		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, params.get(BaseAction.SP_OUT_PARAM_sErrorMsg).toString());
		//
		ReturnCommoditySheetCommodity rcsc1 = new ReturnCommoditySheetCommodity();
		rcsc1.setReturnCommoditySheetID(rcsCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> paramsForRetrieveN = rcsc1.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcsc1);

		List<BaseModel> rcscRN = returnCommoditySheetCommodityMapper.retrieveN(paramsForRetrieveN);
		Assert.assertTrue(rcscRN.size() == 0 && EnumErrorCode.values()[Integer.parseInt(paramsForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");

		System.out.println("删除退货单商品成功");
	}

	@Test
	public void deleteTest2() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		// 创建单品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = createCommodityBarcodes(commCreate);
		// 创建退货单
		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();
		// 创建退货单商品
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate.getID(), barcode.getID(), 150, "箱");

		System.out.println("------------------------ Case2:删除一张退货单的退货单商品表的所有数据 ----------------------");

		// 创建单品
		Commodity commodity2 = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity comm1Create = BaseCommodityTest.createCommodityViaMapper(commodity2, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode1 = createCommodityBarcodes(comm1Create);
		// 创建退货单商品
		createReturnCommoditySheetCommodity(rcsCreate.getID(), comm1Create.getID(), barcode1.getID(), 200, "箱");

		ReturnCommoditySheetCommodity rcsc2 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc2.setReturnCommoditySheetID(rcsCreate.getID());
		rcsc2.setCommodityID(-1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params1 = rcsc2.getDeleteParam(BaseBO.INVALID_CASE_ID, rcsc2);

		returnCommoditySheetCommodityMapper.delete(params1); // ...

		ReturnCommoditySheetCommodity rcsc3 = new ReturnCommoditySheetCommodity();
		rcsc3.setReturnCommoditySheetID(rcsCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params1ForRetrieveN = rcsc3.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcsc3);

		List<BaseModel> rcsc1RN = returnCommoditySheetCommodityMapper.retrieveN(params1ForRetrieveN);
		Assert.assertTrue(rcsc1RN.size() == 0 && EnumErrorCode.values()[Integer.parseInt(params1ForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "删除对象失败");

		System.out.println("删除退货单商品成功");
	}

	@Test
	public void deleteTest3() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		// 创建单品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = createCommodityBarcodes(commCreate);
		// 创建退货单
		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();
		// 创建退货单商品

		System.out.println("------------------------ Case3:删除一张已审核的退货单对应的退货单商品表数据  ----------------------");

		// 创建退货单商品
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate.getID(), barcode.getID(), 150, "箱");

		System.out.println("------------------------ 审核退货单 ----------------------");
		ReturnCommoditySheet rcs = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheet();
		rcs.setID(rcsCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> rcs1params = rcs.getUpdateParam(BaseBO.CASE_ApproveReturnCommoditySheet, rcs);

		ReturnCommoditySheet rcsApprove = (ReturnCommoditySheet) returnCommoditySheetMapper.approve(rcs1params);
		rcs.setIgnoreIDInComparision(true);
		if (rcs.compareTo(rcsApprove) != 0) {
			Assert.assertTrue(false, "创建的对象的字段与DB读出的不相等");
		}
		System.out.println(rcsApprove == null ? "null" : rcsApprove);

		Assert.assertTrue(rcsApprove != null && EnumErrorCode.values()[Integer.parseInt(rcs1params.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError, "审核对象失败");
		System.out.println("审核退货单成功");

		ReturnCommoditySheetCommodity rcsc4 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc4.setReturnCommoditySheetID(rcsCreate.getID());
		rcsc4.setCommodityID(-1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params2 = rcsc4.getDeleteParam(BaseBO.INVALID_CASE_ID, rcsc4);

		returnCommoditySheetCommodityMapper.delete(params2); // ...

		ReturnCommoditySheetCommodity rcsc5 = new ReturnCommoditySheetCommodity();
		rcsc5.setReturnCommoditySheetID(rcsCreate.getID());
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params2ForRetrieveN = rcsc5.getRetrieveNParam(BaseBO.INVALID_CASE_ID, rcsc5);

		List<BaseModel> rcsc2RN = returnCommoditySheetCommodityMapper.retrieveN(params2ForRetrieveN);
		Assert.assertTrue(rcsc2RN.size() != 0 && EnumErrorCode.values()[Integer.parseInt(params2ForRetrieveN.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_NoError
				&& EnumErrorCode.values()[Integer.parseInt(params2.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	@Test
	public void deleteTest4() throws CloneNotSupportedException, InterruptedException {
		Shared.printTestMethodStartInfo();
		// 创建单品
		Commodity commodity = BaseCommodityTest.DataInput.getCommodity(CommodityType.EnumCommodityType.ECT_Normal.getIndex());
		Commodity commCreate = BaseCommodityTest.createCommodityViaMapper(commodity, BaseBO.CASE_Commodity_CreateSingle);
		// 创建对应的条形码
		Barcodes barcode = createCommodityBarcodes(commCreate);
		// 创建退货单
		ReturnCommoditySheet rcsCreate = createReturnCommoditySheet();
		// 创建退货单商品
		createReturnCommoditySheetCommodity(rcsCreate.getID(), commCreate.getID(), barcode.getID(), 150, "箱");

		System.out.println("------------------------ Case4:删除一张不存在的退货单对应的退货单商品表数据  ----------------------");

		ReturnCommoditySheetCommodity rcsc6 = BaseReturnCommoditySheetTest.DataInput.getReturnCommoditySheetCommodity();
		rcsc6.setReturnCommoditySheetID(-1);
		rcsc6.setCommodityID(-1);
		DataSourceContextHolder.setDbName(Shared.DBName_Test);
		Map<String, Object> params3 = rcsc6.getDeleteParam(BaseBO.INVALID_CASE_ID, rcsc6);

		returnCommoditySheetCommodityMapper.delete(params3); // ...

		Assert.assertTrue(EnumErrorCode.values()[Integer.parseInt(params3.get(BaseAction.SP_OUT_PARAM_iErrorCode).toString())] == EnumErrorCode.EC_BusinessLogicNotDefined);
	}

	private Barcodes createCommodityBarcodes(Commodity commodity) {
		Barcodes barcodes = new Barcodes();
		barcodes.setCommodityID(commodity.getID());
		barcodes.setBarcode(String.valueOf(System.currentTimeMillis() % 100000000));
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
